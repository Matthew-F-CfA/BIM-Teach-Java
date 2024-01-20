import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;

class ClientSideApplication extends Frame
implements ActionListener {
  volatile Socket clientSocket;
  volatile DataInputStream dis;
  volatile DataOutputStream dos;
  volatile ClientSideThread clientSideThread;

  volatile TextArea txtArea=new TextArea();
  volatile TextField txtMessage=new TextField();
  volatile Button btnSendMessage=new Button("Send Message");

  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java ClientSideApplication <port number to connect to>");

        return;
      }

      int intPort=Integer.parseInt(args[0]);

      Socket clientSocket=new Socket("localhost", intPort);

      ClientSideApplication cFrame=new ClientSideApplication(clientSocket);
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      cFrame.setSize(dimScreen.width, dimScreen.height-40);
      cFrame.setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  ClientSideApplication(Socket clientSocket) throws Exception {
    super("Client Side Application");
    this.clientSocket=clientSocket;
    dis=new DataInputStream(clientSocket.getInputStream());
    dos=new DataOutputStream(clientSocket.getOutputStream());

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    add("Center", txtArea);

    Panel tempPan=new Panel();
    tempPan.setLayout(new BorderLayout());
    tempPan.add("West", new Label("Message:"));
    tempPan.add("Center", txtMessage);
    tempPan.add("East", btnSendMessage);
    btnSendMessage.addActionListener(this);

    add("South", tempPan);

    clientSideThread=new ClientSideThread();
    clientSideThread.start();
  }

  public void actionPerformed(ActionEvent ae) {
    Object evSource=ae.getSource();

    if(evSource==btnSendMessage) {
      String strMessage=txtMessage.getText();

      if(strMessage.length()==0)
        return;

      try {
        dos.writeInt(strMessage.length());
        dos.writeBytes(strMessage);

        dos.flush();
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  class ClientSideThread extends Thread {
    volatile boolean blnKeepAlive=true;

    ClientSideThread() {
      super();
    }

    public void run() {
      try {
        while(blnKeepAlive) {
          int intMessageLength=dis.readInt();
          byte byteBuf[]=new byte[intMessageLength];
          dis.readFully(byteBuf);

          String strMessage=new String(byteBuf);

          txtArea.append(strMessage+"\n");
        }
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}