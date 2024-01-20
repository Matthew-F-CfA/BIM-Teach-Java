import java.net.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.awt.Rectangle;
import java.awt.Dimension;

class PongServer {
  volatile PongServer refThis;

//Stores the logged in users with key of user name and value of ClientThread
  volatile Hashtable hashUsers=new Hashtable();

//Store the command line arguments defining the state of client PongCanvas
  volatile int intPongCanvasWidth=-1;
  volatile int intPongCanvasHeight=-1;
  volatile int intPongBallWidth=-1;
  volatile int intPongBallHeight=-1;
  volatile int intPongSliderWidth=-1;
  volatile int intPongSliderHeight=-1;
  volatile int intPongEdgeWidth=-1;

//Each time an user slider move is received move the slider this much
  volatile int intUserSliderPositionIncrement=10;


  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java PongServer <port number to listen on> <pong canvas width> <pong canvas height> <ball width> <ball height> <slider width> <slider height> <edge width>");

        System.out.println("For example:");
        System.out.println("  java PongServer 25432 800 400 11 11 20 80 20");
        return;
      }

      PongServer pongServer=new PongServer();

      String strPortNumber=args[0];
      int intPortNumber=Integer.parseInt(strPortNumber);

      String strPongCanvasWidth=args[1];
      pongServer.intPongCanvasWidth=Integer.parseInt(strPongCanvasWidth);

      String strPongCanvasHeight=args[2];
      pongServer.intPongCanvasHeight=Integer.parseInt(strPongCanvasHeight);

      String strPongBallWidth=args[3];
      pongServer.intPongBallWidth=Integer.parseInt(strPongBallWidth);

      String strPongBallHeight=args[4];
      pongServer.intPongBallHeight=Integer.parseInt(strPongBallHeight);

      String strPongSliderWidth=args[5];
      pongServer.intPongSliderWidth=Integer.parseInt(strPongSliderWidth);

      String strPongSliderHeight=args[6];
      pongServer.intPongSliderHeight=Integer.parseInt(strPongSliderHeight);

      String strPongEdgeWidth=args[7];
      pongServer.intPongEdgeWidth=Integer.parseInt(strPongEdgeWidth);

      ServerSocket serveSocket=new ServerSocket(intPortNumber);
      while(true) {
        Socket clientSocket=serveSocket.accept();

        try {
          PongServer.ClientThread thrClient=pongServer.new ClientThread(clientSocket);

          thrClient.start();
        }
        catch(Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  PongServer() {
    refThis=this;
  }

  class ClientThread extends Thread {
//The client socket is the socket that blocks in this thread which receives requests from the client
    volatile Socket clientSocket;
    volatile DataInputStream clientDIS;
    volatile DataOutputStream clientDOS;

//The server socket is the socket that is used to send requests to the client
    volatile Socket serverSocket;
    volatile DataInputStream serverDIS;
    volatile DataOutputStream serverDOS;

//Stores the user name corresponding to this thread
    volatile String strUserName;

//Stores the server side pong canvas that is used for keeping track of the ball and the sliders
    volatile PongCanvas canvasPong;
    volatile boolean blnPlaying=false;

    volatile boolean blnKeepAlive=true;

    ClientThread(Socket clientSocket) throws Exception {
      super();
      this.clientSocket=clientSocket;

      clientDIS=new DataInputStream(clientSocket.getInputStream());
      clientDOS=new DataOutputStream(clientSocket.getOutputStream());
    }

    public void run() {
      try {

//Before starting to listen for requests from the client first receive some initialization data
        int intUserNameLength=clientDIS.readInt();

        byte byteUserName[]=new byte[intUserNameLength];
        clientDIS.readFully(byteUserName);

        strUserName=new String(byteUserName);

synchronized(PongConstants.syncAll) {

//If hashUsers already contains a user with the same name as strUserName then notify the client of reject and exit this thread
        if(hashUsers.containsKey(strUserName)) {
          clientDOS.writeInt(PongConstants.USER_NAME_REJECTED);

          clientDOS.flush();

          try {
            clientSocket.close();
          }
          catch(Exception ex) {
          }

          return;
        }

        clientDOS.writeInt(PongConstants.USER_NAME_ACCEPTED);

        clientDOS.flush();

//Send the size of the Hashtable hashUsers
        clientDOS.writeInt(hashUsers.size());

//Iterate through all entries in the Hashtable
        Iterator iter0=hashUsers.entrySet().iterator();
        while(iter0.hasNext()) {
          Map.Entry mEntry=(Map.Entry)iter0.next();
          String strUserName0=(String)mEntry.getKey();

//Send the user names already in the Hashtable to the new user
          clientDOS.writeInt(strUserName0.length());
          clientDOS.writeBytes(strUserName0);

//Send the new user to all users already in the Hashtable
          try {
            ClientThread thrClient=(ClientThread)mEntry.getValue();
            thrClient.serverDOS.writeInt(PongConstants.NEW_USER);
            thrClient.serverDOS.writeInt(strUserName.length());
            thrClient.serverDOS.writeBytes(strUserName);

            thrClient.serverDOS.flush();
          }
          catch(Exception ex) {
          }
        }

        clientDOS.flush();

//Receive the server socket information from the client, so the server can establish a socket to the client's server
        int intSocketAddressLength=clientDIS.readInt();
        byte byteSocketAddress[]=new byte[intSocketAddressLength];
        clientDIS.readFully(byteSocketAddress);

        String strSocketAddress=new String(byteSocketAddress);

        int intPortNumber=clientDIS.readInt();

//This is the socket to the client's server
        serverSocket=new Socket(strSocketAddress, intPortNumber);
        serverDIS=new DataInputStream(serverSocket.getInputStream());
        serverDOS=new DataOutputStream(serverSocket.getOutputStream());

//Send PongCanvas state information to the client
        clientDOS.writeInt(intPongCanvasWidth);
        clientDOS.writeInt(intPongCanvasHeight);
        clientDOS.writeInt(intPongBallWidth);
        clientDOS.writeInt(intPongBallHeight);
        clientDOS.writeInt(intPongSliderWidth);
        clientDOS.writeInt(intPongSliderHeight);
        clientDOS.writeInt(intPongEdgeWidth);

        clientDOS.flush();

//Put this user name's name as a key and this thread as the value
        hashUsers.put(strUserName, this);

}

//Start a loop for receiving requests from the client
        while(blnKeepAlive) {
//This instruction blocks until a request is received from the client
          int intClientCommand=clientDIS.readInt();

          if(intClientCommand==PongConstants.MOVE_SLIDER_UP) {

synchronized(PongConstants.syncAll) {

//If this user is playing then compute the new slider position and call a repaint method for sending a request to repaint to the client
            if(blnPlaying) {
              if(canvasPong.thrClientLeft==this) {
                if((canvasPong.intUserLeftSliderPosition-intUserSliderPositionIncrement)<0)
                  canvasPong.intUserLeftSliderPosition=0;
                else
                  canvasPong.intUserLeftSliderPosition=canvasPong.intUserLeftSliderPosition-intUserSliderPositionIncrement;

                canvasPong.repaintUserLeftSliderPosition();
              }
              else {
                if((canvasPong.intUserRightSliderPosition-intUserSliderPositionIncrement)<0)
                  canvasPong.intUserRightSliderPosition=0;
                else
                  canvasPong.intUserRightSliderPosition=canvasPong.intUserRightSliderPosition-intUserSliderPositionIncrement;

                canvasPong.repaintUserRightSliderPosition();
              }
            }

}

          }
          else if(intClientCommand==PongConstants.MOVE_SLIDER_DOWN) {

synchronized(PongConstants.syncAll) {

//If this user is playing then compute the new slider position and call a repaint method for sending a request to repaint to the client
            if(blnPlaying) {
              if(canvasPong.thrClientLeft==this) {
                if((canvasPong.intUserLeftSliderPosition+intPongSliderHeight+intUserSliderPositionIncrement)>intPongCanvasHeight)
                  canvasPong.intUserLeftSliderPosition=intPongCanvasHeight-intPongSliderHeight;
                else
                  canvasPong.intUserLeftSliderPosition=canvasPong.intUserLeftSliderPosition+intUserSliderPositionIncrement;

                canvasPong.repaintUserLeftSliderPosition();
              }
              else {
                if((canvasPong.intUserRightSliderPosition+intPongSliderHeight+intUserSliderPositionIncrement)>intPongCanvasHeight)
                  canvasPong.intUserRightSliderPosition=intPongCanvasHeight-intPongSliderHeight;
                else
                  canvasPong.intUserRightSliderPosition=canvasPong.intUserRightSliderPosition+intUserSliderPositionIncrement;

                canvasPong.repaintUserRightSliderPosition();
              }
            }

}

          }
          else if(intClientCommand==PongConstants.CLOSE_CONNECTION) {

synchronized(PongConstants.syncAll) {

//Iterate through the current users and remove this user from their list of users
            Iterator iter0=hashUsers.entrySet().iterator();
            while(iter0.hasNext()) {
              Map.Entry mEntry=(Map.Entry)iter0.next();
              String strUserName0=(String)mEntry.getKey();
              if(strUserName0.equals(strUserName))
                continue;

              try {
                ClientThread thrClient=(ClientThread)mEntry.getValue();
                thrClient.serverDOS.writeInt(PongConstants.REMOVE_USER);
                thrClient.serverDOS.writeInt(strUserName.length());
                thrClient.serverDOS.writeBytes(strUserName);

                thrClient.serverDOS.flush();
              }
              catch(Exception ex) {
              }
            }

//Remove this user from hashUsers Hashtable
            hashUsers.remove(strUserName);

//If the user was in an active game then declare the other user the winner
            if(blnPlaying) {
              if(canvasPong.thrClientLeft==this) {
                canvasPong.userOnRightWins();
              }
              else {
                canvasPong.userOnLeftWins();
              }
            }

}

//Close the sockets for this user and break the loop that is listening for client requests
            try {
              clientSocket.close();
            }
            catch(Exception ex2) {
            }

            try {
              serverSocket.close();
            }
            catch(Exception ex2) {
            }

            break;
          }
          else if(intClientCommand==PongConstants.REQUEST_CHALLENGE) {

//Request to challenge a user to a game of pong

//Read the user name that this user has chosen to challenge
            int intUserNameLength0=clientDIS.readInt();
            byte byteUserName0[]=new byte[intUserNameLength0];
            clientDIS.readFully(byteUserName0);

            String strUserName0=new String(byteUserName0);

synchronized(PongConstants.syncAll) {

            ClientThread thrClient=(ClientThread)hashUsers.get(strUserName0);

//If thrClient isn't in the hashUsers then "continue" the loop instead of executing the following instructions
            if(thrClient==null) {
              clientDOS.writeInt(PongConstants.REQUEST_CHALLENGE_REJECTED);

              clientDOS.flush();

              continue;
            }

//If thrClient is already involved in a game then "continue" the loop instead of executing the following instructions
            if(thrClient.blnPlaying) {
              clientDOS.writeInt(PongConstants.REQUEST_CHALLENGE_REJECTED);

              clientDOS.flush();

              continue;
            }

//The challenge request has succeeded, so tell the user that is being challenged that a new game is about to start
            try {
              thrClient.serverDOS.writeInt(PongConstants.CHALLENGE_INITIATED);

              thrClient.serverDOS.flush();
            }
            catch(Exception ex) {
            }

            clientDOS.writeInt(PongConstants.REQUEST_CHALLENGE_ACCEPTED);

            clientDOS.flush();

//Set the boolean values indicating that each user is involved in a game
            blnPlaying=true;
            thrClient.blnPlaying=true;

//Creates a PongCanvas to store the state of the pong game between this user and thrClient
            PongCanvas canvasPong0=new PongCanvas(this, thrClient);

            canvasPong=canvasPong0;
            thrClient.canvasPong=canvasPong0;

//Initializes the PongCanvas for a new game
            canvasPong0.startGame();

//Remove the user names of the two users involved in the game from all of the other user's lists
            Iterator iter0=hashUsers.entrySet().iterator();
            while(iter0.hasNext()) {
              Map.Entry mEntry=(Map.Entry)iter0.next();

              try {
                ClientThread thrClient2=(ClientThread)mEntry.getValue();
                thrClient2.serverDOS.writeInt(PongConstants.REMOVE_USER);
                thrClient2.serverDOS.writeInt(strUserName.length());
                thrClient2.serverDOS.writeBytes(strUserName);

                thrClient2.serverDOS.writeInt(PongConstants.REMOVE_USER);
                thrClient2.serverDOS.writeInt(strUserName0.length());
                thrClient2.serverDOS.writeBytes(strUserName0);

                thrClient2.serverDOS.flush();
              }
              catch(Exception ex) {
              }
            }

}

          }
          else if(intClientCommand==PongConstants.JOIN_QUEUE) {

//During or after a pong game ends the user clicked the button to "Join Queue" which makes the user available for pong challenges

synchronized(PongConstants.syncAll) {

//Iterate through the users and add this user to the list of challengeable users
            Iterator iter0=hashUsers.entrySet().iterator();
            while(iter0.hasNext()) {
              Map.Entry mEntry=(Map.Entry)iter0.next();
              String strUserName0=(String)mEntry.getKey();
              if(strUserName0.equals(strUserName))
                continue;

              try {
                ClientThread thrClient2=(ClientThread)mEntry.getValue();
                thrClient2.serverDOS.writeInt(PongConstants.NEW_USER);
                thrClient2.serverDOS.writeInt(strUserName.length());
                thrClient2.serverDOS.writeBytes(strUserName);

                thrClient2.serverDOS.flush();
              }
              catch(Exception ex) {
              }
            }

}

          }
        }
      }
      catch(Exception ex) {

//If an exception is triggered in this thread then this code is executed and the user is removed from the pong server

        ex.printStackTrace();

synchronized(PongConstants.syncAll) {

        Iterator iter0=hashUsers.entrySet().iterator();
        while(iter0.hasNext()) {
          Map.Entry mEntry=(Map.Entry)iter0.next();
          String strUserName0=(String)mEntry.getKey();
          if(strUserName0.equals(strUserName))
            continue;

          try {
            ClientThread thrClient=(ClientThread)mEntry.getValue();
            thrClient.serverDOS.writeInt(PongConstants.REMOVE_USER);
            thrClient.serverDOS.writeInt(strUserName.length());
            thrClient.serverDOS.writeBytes(strUserName);

            thrClient.serverDOS.flush();
          }
          catch(Exception ex2) {
          }
        }

//Remove the user who's thread triggered an exception from hashUsers
        hashUsers.remove(strUserName);

//If this player was in a game when the exception occurred then declare the other user as the winner of the game
        if(blnPlaying) {
          if(canvasPong.thrClientLeft==this) {
            canvasPong.userOnRightWins();
          }
          else {
            canvasPong.userOnLeftWins();
          }
        }

}

//Close the user's sockets
        try {
          clientSocket.close();
        }
        catch(Exception ex2) {
        }

        try {
          serverSocket.close();
        }
        catch(Exception ex2) {
        }
      }
    }
  }

  class PongCanvas {
//The thread for the user on the left
    ClientThread thrClientLeft;
//The thread for the user on the right
    ClientThread thrClientRight;

//The vertical positions of the users on the canvas
    volatile int intUserLeftSliderPosition=-1;
    volatile int intUserRightSliderPosition=-1;

//The virtual ball's x and y
    volatile double dblBallX=0.0d;
    volatile double dblBallY=0.0d;

//The direction of the ball's x and y movement
    volatile double dblBallMoveX=0.0d;
    volatile double dblBallMoveY=0.0d;

//A reference to the ball's movement handling thread
    volatile BallMoveThread thrBall;

    PongCanvas(ClientThread thrClientLeft, ClientThread thrClientRight) {
      this.thrClientLeft=thrClientLeft;
      this.thrClientRight=thrClientRight;
    }

    public void startGame() {

synchronized(PongConstants.syncAll) {

      Dimension dimCanvas=new Dimension(intPongCanvasWidth, intPongCanvasHeight);

//Set the users slider positions to the middle of the screen
      intUserLeftSliderPosition=dimCanvas.height/2-intPongSliderHeight/2;
      intUserRightSliderPosition=dimCanvas.height/2-intPongSliderHeight/2;

//Set the ball position to the middle of the screen
      dblBallX=(double)(dimCanvas.width/2);
      dblBallY=(double)(dimCanvas.height/2);

      double dblMoveX=Math.random();

//Don't let the ball move x be too small of a number
      if(dblMoveX<=0.1d)
        dblMoveX=0.1d;

//Make dblMoveY smaller than dblMoveX
      double dblMoveY=dblMoveX/2.0d*Math.random();

//Don't let the ball move y be too small of a number
      if(dblMoveY<=0.05d)
        dblMoveY=0.05d;

//Half of the time dblMoveX is positive and half of the time dblMoveX is negative
      if(Math.random()>0.5d)
        dblMoveX=-dblMoveX;

//Half of the time dblMoveY is positive and half of the time dblMoveY is negative
      if(Math.random()>0.5d)
        dblMoveY=-dblMoveY;

//Get the magnitude of the vector formed by dblMoveX and dblMoveY
      double dblDistance=Math.sqrt(Math.pow(dblMoveX, 2.0d)+Math.pow(dblMoveY, 2.0d));

//Make the vector formed by dblMoveX and dblMoveY into a unit vector
      dblMoveX=dblMoveX/dblDistance;
      dblMoveY=dblMoveY/dblDistance;

      dblBallMoveX=dblMoveX;
      dblBallMoveY=dblMoveY;

//Scale up the vector formed by dblBallMoveX and dblBallMoveY up by a factor of 10
      dblBallMoveX*=10.0d;
      dblBallMoveY*=10.0d;

//Call the repaint functions which send data over the ClientThread's server socket
      repaintUserLeftSliderPosition();
      repaintUserRightSliderPosition();
      repaintBallPosition();

}

//Construct and start the ball's movement thread
      thrBall=new BallMoveThread(this);
      thrBall.start();
    }

    public void repaintBallPosition() {

synchronized(PongConstants.syncAll) {

      try {
        thrClientLeft.serverDOS.writeInt(PongConstants.REPAINT_BALL);
        thrClientLeft.serverDOS.writeInt((int)Math.rint(dblBallX));
        thrClientLeft.serverDOS.writeInt((int)Math.rint(dblBallY));

        thrClientLeft.serverDOS.flush();
      }
      catch(Exception ex) {
      }

      try {
        thrClientRight.serverDOS.writeInt(PongConstants.REPAINT_BALL);
        thrClientRight.serverDOS.writeInt((int)Math.rint(dblBallX));
        thrClientRight.serverDOS.writeInt((int)Math.rint(dblBallY));

        thrClientRight.serverDOS.flush();
      }
      catch(Exception ex) {
      }

}

    }

    public void repaintUserLeftSliderPosition() {
      try {
        thrClientLeft.serverDOS.writeInt(PongConstants.REPAINT_LEFT_SLIDER);
        thrClientLeft.serverDOS.writeInt(intUserLeftSliderPosition);

        thrClientLeft.serverDOS.flush();
      }
      catch(Exception ex) {
      }

      try {
        thrClientRight.serverDOS.writeInt(PongConstants.REPAINT_LEFT_SLIDER);
        thrClientRight.serverDOS.writeInt(intUserLeftSliderPosition);

        thrClientRight.serverDOS.flush();
      }
      catch(Exception ex) {
      }
    }

    public void repaintUserRightSliderPosition() {
      try {
        thrClientLeft.serverDOS.writeInt(PongConstants.REPAINT_RIGHT_SLIDER);
        thrClientLeft.serverDOS.writeInt(intUserRightSliderPosition);

        thrClientLeft.serverDOS.flush();
      }
      catch(Exception ex) {
      }

      try {
        thrClientRight.serverDOS.writeInt(PongConstants.REPAINT_RIGHT_SLIDER);
        thrClientRight.serverDOS.writeInt(intUserRightSliderPosition);

        thrClientRight.serverDOS.flush();
      }
      catch(Exception ex) {
      }
    }

    public void userOnRightWins() {
      try {
        thrClientLeft.serverDOS.writeInt(PongConstants.ANNOUNCE_WINNER);
        thrClientLeft.serverDOS.writeInt(thrClientRight.strUserName.length());
        thrClientLeft.serverDOS.writeBytes(thrClientRight.strUserName);

        thrClientLeft.serverDOS.flush();
      }
      catch(Exception ex) {
      }

      try {
        thrClientRight.serverDOS.writeInt(PongConstants.ANNOUNCE_WINNER);
        thrClientRight.serverDOS.writeInt(thrClientRight.strUserName.length());
        thrClientRight.serverDOS.writeBytes(thrClientRight.strUserName);

        thrClientRight.serverDOS.flush();
      }
      catch(Exception ex) {
      }

//Set the appropriate variables for specifying that the ClientThreads aren't currently involved in a game
      thrClientLeft.canvasPong=null;
      thrClientLeft.blnPlaying=false;

      thrClientRight.canvasPong=null;
      thrClientRight.blnPlaying=false;
    }

    public void userOnLeftWins() {
      try {
        thrClientLeft.serverDOS.writeInt(PongConstants.ANNOUNCE_WINNER);
        thrClientLeft.serverDOS.writeInt(thrClientLeft.strUserName.length());
        thrClientLeft.serverDOS.writeBytes(thrClientLeft.strUserName);

        thrClientLeft.serverDOS.flush();
      }
      catch(Exception ex) {
      }

      try {
        thrClientRight.serverDOS.writeInt(PongConstants.ANNOUNCE_WINNER);
        thrClientRight.serverDOS.writeInt(thrClientLeft.strUserName.length());
        thrClientRight.serverDOS.writeBytes(thrClientLeft.strUserName);

        thrClientRight.serverDOS.flush();
      }
      catch(Exception ex) {
      }

//Set the appropriate variables for specifying that the ClientThreads aren't currently involved in a game
      thrClientLeft.canvasPong=null;
      thrClientLeft.blnPlaying=false;

      thrClientRight.canvasPong=null;
      thrClientRight.blnPlaying=false;
    }
  }

  class BallMoveThread extends Thread {
//A reference to the PongCanvas that this ball is moving in
    PongCanvas canvasPong;

    volatile long lngSleepTime=50l;
    volatile boolean blnKeepAlive=true;

    BallMoveThread(PongCanvas canvasPong) {
      super();
      this.canvasPong=canvasPong;
    }

    public void run() {
      while(blnKeepAlive) {
synchronized(PongConstants.syncAll) {

//Move the ball's position
        canvasPong.dblBallX+=canvasPong.dblBallMoveX;
        canvasPong.dblBallY+=canvasPong.dblBallMoveY;


//Check to see if the ball's position places it for bouncing off of the user's slider
        Rectangle userLeft=new Rectangle(0, canvasPong.intUserLeftSliderPosition, intPongSliderWidth, intPongSliderHeight);
        if(userLeft.contains(canvasPong.dblBallX, canvasPong.dblBallY)) {
          canvasPong.dblBallMoveX=-canvasPong.dblBallMoveX;
          canvasPong.dblBallX=intPongSliderWidth+1;
        }


//Check to see if the ball's position places it for bouncing off of the AI's slider
        Rectangle userRight=new Rectangle(intPongCanvasWidth-intPongSliderWidth, canvasPong.intUserRightSliderPosition, intPongSliderWidth, intPongSliderHeight);
        if(userRight.contains(canvasPong.dblBallX, canvasPong.dblBallY)) {
          canvasPong.dblBallMoveX=-canvasPong.dblBallMoveX;
          canvasPong.dblBallX=intPongCanvasWidth-intPongSliderWidth-1;
        }


//Check to see if the ball's position places it for bouncing off of the northern edge
        Rectangle northernEdge=new Rectangle(0, 0, intPongCanvasWidth, intPongEdgeWidth);
        if(northernEdge.contains(canvasPong.dblBallX, canvasPong.dblBallY)) {
          canvasPong.dblBallMoveY=-canvasPong.dblBallMoveY;
        }


//Check to see if the ball's position places it for bouncing off of the southern edge
        Rectangle southernEdge=new Rectangle(0, intPongCanvasHeight-intPongEdgeWidth, intPongCanvasWidth, intPongEdgeWidth);
        if(southernEdge.contains(canvasPong.dblBallX, canvasPong.dblBallY)) {
          canvasPong.dblBallMoveY=-canvasPong.dblBallMoveY;
        }


        if(canvasPong.dblBallX<0.0d) {
//If the dblBallX is less than 0 then the ball passed by the left user's slider, so the user on the right wins

          canvasPong.userOnRightWins();

          blnKeepAlive=false;
        }
        else if(canvasPong.dblBallX>intPongCanvasWidth) {
//If the dblBallX is greater than PongCanvas's width then the ball passed by the right user's slider, so the user on the left wins

          canvasPong.userOnLeftWins();

          blnKeepAlive=false;
        }

}

        if(blnKeepAlive) {
//Send data to the user specifying the position of the ball
          canvasPong.repaintBallPosition();

          try {
            Thread.sleep(lngSleepTime);
          }
          catch(Exception ex) {
          }
        }
      }
    }
  }
}