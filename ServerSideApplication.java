import java.net.*;
import java.io.*;
import java.util.Vector;

class ServerSideApplication {
  volatile ServerSocket serverSocket;
  volatile Vector vecClients=new Vector();
  static volatile Object syncObj=new Object();

  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java ServerSideApplication <port number>");

        return;
      }

      ServerSideApplication sApp=new ServerSideApplication();

      sApp.serverSocket=new ServerSocket(Integer.parseInt(args[0]));

      System.out.println("Server Up!!");

      while(true) {
        Socket socketNew=sApp.serverSocket.accept();

        System.out.println("Client Accepted");

        ServerSideApplication.ServerSideThread serverSideThread=sApp.new ServerSideThread(sApp, socketNew);
        serverSideThread.start();

//        System.out.println("Client Thread Started");

synchronized(ServerSideApplication.syncObj) {
        sApp.vecClients.addElement(serverSideThread);
}

//        System.out.println("Client Added to Vector");
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  ServerSideApplication() {
  }

  class ServerSideThread extends Thread {
    ServerSideApplication sApp;
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

    ServerSideThread(ServerSideApplication sApp, Socket socket) throws Exception {
      this.sApp=sApp;
      this.socket=socket;
      dis=new DataInputStream(socket.getInputStream());
      dos=new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
      try {
        while(true) {
          int intBufferSize=dis.readInt();

          byte byteBuf[]=new byte[intBufferSize];
          dis.readFully(byteBuf);

          System.out.println("Size of Message: "+intBufferSize);

synchronized(ServerSideApplication.syncObj) {

          for(int i=0;i<sApp.vecClients.size();i++) {
            ServerSideThread serverSideThread=(ServerSideThread)sApp.vecClients.elementAt(i);
            serverSideThread.dos.writeInt(byteBuf.length);
            serverSideThread.dos.write(byteBuf, 0, byteBuf.length);

            serverSideThread.dos.flush();
          }

}
        }
      }
      catch(Exception ex) {
synchronized(ServerSideApplication.syncObj) {
        sApp.vecClients.remove(this);
}
      }
    }
  }
}