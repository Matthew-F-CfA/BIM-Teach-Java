import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;

class PongClient extends Frame
implements ActionListener, KeyEventDispatcher {
  volatile PongClient refThis;

  volatile Panel panLogin=new Panel();
  volatile TextField txtUserName=new TextField();
  volatile TextField txtServerAddress=new TextField();
  volatile TextField txtServerPort=new TextField();
  volatile Button btnLogin=new Button("Login");

  volatile Panel panQueue=new Panel();
  volatile List lstUsers=new List(5);
  volatile Button btnChallengeUser=new Button("Challenge User");

  volatile Panel panPlay=new Panel();
  volatile Button btnJoinQueue=new Button("Join Queue");
  volatile PongCanvas canvasPong=new PongCanvas();

  volatile ClientThread thrClient;

  public static void main(String args[]) {
    try {
      PongClient pFrame=new PongClient();
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      pFrame.setSize(dimScreen.width, dimScreen.height-40);
      pFrame.setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  PongClient() {
    super("Pong Client");
    refThis=this;

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        if(thrClient!=null) {
          try {
//Attempt to send the close connection int to the server, it will only work if the user has already connected
            thrClient.serverDOS.writeInt(PongConstants.CLOSE_CONNECTION);

            thrClient.serverDOS.flush();
          }
          catch(Exception ex) {
          }
        }

        System.exit(0);
      }
    });

//Add event handler for keyboard key press events
    KeyboardFocusManager manager=KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(this);


    Panel tempPan=new Panel();
    tempPan.setLayout(new GridLayout(4, 1));
    Panel tempPanA=new Panel();
    tempPanA.setLayout(new GridLayout(1, 2));
    tempPanA.add(new Label("User Name:"));
    tempPanA.add(txtUserName);
    tempPan.add(tempPanA);
    Panel tempPanB=new Panel();
    tempPanB.setLayout(new GridLayout(1, 2));
    tempPanB.add(new Label("Server Address:"));
    tempPanB.add(txtServerAddress);
    tempPan.add(tempPanB);
    Panel tempPanC=new Panel();
    tempPanC.setLayout(new GridLayout(1, 2));
    tempPanC.add(new Label("Server Port:"));
    tempPanC.add(txtServerPort);
    tempPan.add(tempPanC);
    Panel tempPanD=new Panel();
    tempPanD.add(btnLogin);
    btnLogin.addActionListener(this);
    tempPan.add(tempPanD);

    panLogin.setLayout(new BorderLayout());
    panLogin.add("North", tempPan);
    panLogin.add("Center", new Label(""));

    add("Center", panLogin);


    panQueue.setLayout(new BorderLayout());
    panQueue.add("North", new Label("Users:"));
    panQueue.add("Center", lstUsers);
    Panel tempPan2=new Panel();
    tempPan2.add(btnChallengeUser);
    btnChallengeUser.addActionListener(this);
    panQueue.add("South", tempPan2);


    panPlay.setLayout(null);
    panPlay.add(btnJoinQueue);
    btnJoinQueue.addActionListener(this);
    panPlay.add(canvasPong);    
  }

  public void actionPerformed(ActionEvent ae) {
    Object evSource=ae.getSource();

    if(evSource==btnLogin) {
      String strUserName=txtUserName.getText();
      if(strUserName.length()==0) {
        txtUserName.setText("User name required.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex) {
        }
        txtUserName.setText("");

        return;
      }

      String strServerAddress=txtServerAddress.getText();
      if(strServerAddress.length()==0) {
        txtServerAddress.setText("Server address required.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex) {
        }
        txtServerAddress.setText("");

        return;
      }

      String strServerPort=txtServerPort.getText();
      if(strServerPort.length()==0) {
        txtServerPort.setText("Server port required.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex) {
        }
        txtServerPort.setText("");

        return;
      }

      int intServerPort=-1;
      try {
        intServerPort=Integer.parseInt(strServerPort);
      }
      catch(Exception ex) {
        txtServerPort.setText("Digits only.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex2) {
        }
        txtServerPort.setText(strServerPort);

        return;
      }

      try {
//Create a socket for communicating with the server represented by the server address and server port
        Socket serverSocket=new Socket(strServerAddress, intServerPort);
        DataInputStream serverDIS=new DataInputStream(serverSocket.getInputStream());
        DataOutputStream serverDOS=new DataOutputStream(serverSocket.getOutputStream());

//Send the chosen String strUserName to the server
        serverDOS.writeInt(strUserName.length());
        serverDOS.writeBytes(strUserName);

        serverDOS.flush();

//Receive an int which tells the client whether or not the chosen user name has been accepted
        int intServerResponse=serverDIS.readInt();
        if(intServerResponse==PongConstants.USER_NAME_REJECTED) {
          txtUserName.setText("User name rejected. Try a different name.");
          try {
            Thread.sleep(4000);
          }
          catch(Exception ex) {
          }
          txtUserName.setText(strUserName);

          return;
        }

//Receive data for populating the list of challengeable users
        int intNumberOfUsers=serverDIS.readInt();
        lstUsers.removeAll();
        for(int i=0;i<intNumberOfUsers;i++) {
          int intUserNameLength0=serverDIS.readInt();
          byte byteUserName0[]=new byte[intUserNameLength0];
          serverDIS.readFully(byteUserName0);
          String strUserName0=new String(byteUserName0);

          lstUsers.add(strUserName0);
        }


//Obtain an InetAddress object for the local host which is the same as the client's computer
        InetAddress lAddress=InetAddress.getLocalHost();

//Loop until an unused port number has been found for establishing a ServerSocket
        ServerSocket serveSock;
        int intLocalPort=7000;
        while(true) {
          try {
            serveSock=new ServerSocket(intLocalPort, 0, lAddress);
            break;
          }
          catch(Exception ex) {
            ++intLocalPort;
          }
        }

//Send the local address and local port number to the server so that the server can construct a socket to this computer
        String strLocalAddress=lAddress.getHostAddress();
        serverDOS.writeInt(strLocalAddress.length());
        serverDOS.writeBytes(strLocalAddress);
        serverDOS.writeInt(intLocalPort);

        serverDOS.flush();

//Accepts a socket from the server connecting to the local ServerSocket
        Socket clientSocket=serveSock.accept();
        DataInputStream clientDIS=new DataInputStream(clientSocket.getInputStream());
        DataOutputStream clientDOS=new DataOutputStream(clientSocket.getOutputStream());

//Receive the data from the server specifying the dimensions for the PongCanvas
        canvasPong.intPongCanvasWidth=serverDIS.readInt();
        canvasPong.intPongCanvasHeight=serverDIS.readInt();
        canvasPong.intPongBallWidth=serverDIS.readInt();
        canvasPong.intPongBallHeight=serverDIS.readInt();
        canvasPong.intPongSliderWidth=serverDIS.readInt();
        canvasPong.intPongSliderHeight=serverDIS.readInt();
        canvasPong.intPongEdgeWidth=serverDIS.readInt();

//Construct and start the ClientThread with the socket and stream parameters
        thrClient=new ClientThread(serverSocket, serverDIS, serverDOS, clientSocket, clientDIS, clientDOS, strUserName);
        thrClient.start();

        remove(0);

        add("Center", panQueue);

        invalidate();
        validate();
        repaint();
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    else if(evSource==btnChallengeUser) {

synchronized(PongConstants.syncAll) {

      int intSelectedIndex=lstUsers.getSelectedIndex();
      if(intSelectedIndex==-1)
        return;

      try {
//Request to challenge the selected user to a game of pong
        thrClient.serverDOS.writeInt(PongConstants.REQUEST_CHALLENGE);
        String strSelectedUser=lstUsers.getItem(intSelectedIndex);
        thrClient.serverDOS.writeInt(strSelectedUser.length());
        thrClient.serverDOS.writeBytes(strSelectedUser);

        int intServerResponse=thrClient.serverDIS.readInt();

//If the user logged out or is involved in another game then the request is rejected
        if(intServerResponse==PongConstants.REQUEST_CHALLENGE_REJECTED) {
          return;
        }


        remove(0);

        add("Center", panPlay);

        canvasPong.setWinner("");

        invalidate();
        validate();
        repaint();

//Initialize the PongCanvas now because it was just added to the displayed frame when add("Center", panPlay); occurred
        canvasPong.initializeCanvas();
      }
      catch(Exception ex) {
      }

}

    }
    else if(evSource==btnJoinQueue) {

synchronized(PongConstants.syncAll) {

      try {
//The user clicked "Join Queue" so he/she should be added to the list of challengeable users
        thrClient.serverDOS.writeInt(PongConstants.JOIN_QUEUE);

        thrClient.serverDOS.flush();

        remove(0);

        add("Center", panQueue);

        invalidate();
        validate();
        repaint();
      }
      catch(Exception ex) {
      }

}

    }
  }

  public boolean dispatchKeyEvent(KeyEvent ke) {

//Only continue with the key event if the panPlay is currently displayed in the frame
    if(getComponent(0)!=panPlay)
      return false;

//If the event source is a TextField or TextArea then do not continue with the event
    Object evSource=ke.getSource();
    if(evSource instanceof TextField)
      return false;
    else if(evSource instanceof TextArea)
      return false;

    if(ke.getID()==KeyEvent.KEY_PRESSED) {
      int keyCode=ke.getKeyCode();

      if(keyCode==KeyEvent.VK_UP) {
//The keyboard key corresponding to the up arrow was pressed
        canvasPong.arrowUp();
      }
      else if(keyCode==KeyEvent.VK_DOWN) {
//The keyboard key corresponding to the down arrow was pressed
        canvasPong.arrowDown();
      }
    }

    return true;
  }

  class ClientThread extends Thread {
    volatile Socket clientSocket;
    volatile DataInputStream clientDIS;
    volatile DataOutputStream clientDOS;
    volatile Socket serverSocket;
    volatile DataInputStream serverDIS;
    volatile DataOutputStream serverDOS;
    volatile String strUserName;

    volatile boolean blnKeepAlive=true;

    ClientThread(Socket serverSocket, DataInputStream serverDIS, DataOutputStream serverDOS, Socket clientSocket, DataInputStream clientDIS, DataOutputStream clientDOS, String strUserName) {
      super();
      this.serverSocket=serverSocket;
      this.serverDIS=serverDIS;
      this.serverDOS=serverDOS;
      this.clientSocket=clientSocket;
      this.clientDIS=clientDIS;
      this.clientDOS=clientDOS;
      this.strUserName=strUserName;
    }

    public void run() {
      try {
        while(blnKeepAlive) {
          int intClientCommand=clientDIS.readInt();

          if(intClientCommand==PongConstants.NEW_USER) {
//A new user should be added to the list of challengeable users
            int intUserNameLength0=clientDIS.readInt();
            byte byteUserName0[]=new byte[intUserNameLength0];
            clientDIS.readFully(byteUserName0);

            String strUserName0=new String(byteUserName0);

            String strItems[]=lstUsers.getItems();
            boolean blnAlreadyContains=false;
            for(int i=0;i<strItems.length;i++) {
              if(strItems[i].equals(strUserName0)) {
                blnAlreadyContains=true;
                break;
              }
            }

            if(blnAlreadyContains)
              continue;

            lstUsers.add(strUserName0);
          }
          else if(intClientCommand==PongConstants.REMOVE_USER) {
//A current user has been removed from the list of challengeable users
            int intUserNameLength0=clientDIS.readInt();
            byte byteUserName0[]=new byte[intUserNameLength0];
            clientDIS.readFully(byteUserName0);

            String strUserName0=new String(byteUserName0);

            String strItems[]=lstUsers.getItems();
            for(int i=0;i<strItems.length;i++) {
              if(strItems[i].equals(strUserName0)) {
                lstUsers.remove(i);
                break;
              }
            }
          }
          else if(intClientCommand==PongConstants.REPAINT_BALL) {
//Repaint the position of the ball in the PongCanvas
            int intBallX=clientDIS.readInt();
            int intBallY=clientDIS.readInt();

            canvasPong.intBallX=intBallX;
            canvasPong.intBallY=intBallY;

            canvasPong.repaint();
          }
          else if(intClientCommand==PongConstants.REPAINT_LEFT_SLIDER) {
//Repaint the position of the left slider in the PongCanvas
            int intUserLeftSliderPosition=clientDIS.readInt();

            canvasPong.intUserLeftSliderPosition=intUserLeftSliderPosition;

            canvasPong.repaint();
          }
          else if(intClientCommand==PongConstants.REPAINT_RIGHT_SLIDER) {
//Repaint the position of the right slider in the PongCanvas
            int intUserRightSliderPosition=clientDIS.readInt();

            canvasPong.intUserRightSliderPosition=intUserRightSliderPosition;

            canvasPong.repaint();
          }
          else if(intClientCommand==PongConstants.ANNOUNCE_WINNER) {
//A user won the pong game and the winning player's name should be displayed on the screen
            int intUserNameLength0=clientDIS.readInt();
            byte byteUserName0[]=new byte[intUserNameLength0];
            clientDIS.readFully(byteUserName0);

            String strUserName0=new String(byteUserName0);

            canvasPong.setWinner(strUserName0);

            canvasPong.repaint();
          }
          else if(intClientCommand==PongConstants.CHALLENGE_INITIATED) {
//One of the users challenged this user to a game of pong
            refThis.remove(0);

            refThis.add("Center", panPlay);

            canvasPong.setWinner("");

            refThis.invalidate();
            refThis.validate();
            refThis.repaint();

//Initialize the PongCanvas now because it was just added to the displayed frame when add("Center", panPlay); occurred
            canvasPong.initializeCanvas();
          }
        }
      }
      catch(Exception ex) {

//If an exception is triggered in this thread then this code is executed and the user's connection is terminated

        ex.printStackTrace();

synchronized(PongConstants.syncAll) {

        try {
          serverDOS.writeInt(PongConstants.CLOSE_CONNECTION);

          serverDOS.flush();
        }
        catch(Exception ex2) {
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

  class PongCanvas extends Canvas {
    volatile int intPongCanvasWidth=-1;
    volatile int intPongCanvasHeight=-1;
    volatile int intPongBallWidth=-1;
    volatile int intPongBallHeight=-1;
    volatile int intPongSliderWidth=-1;
    volatile int intPongSliderHeight=-1;
    volatile int intPongEdgeWidth=-1;

    volatile int intBallX=-1;
    volatile int intBallY=-1;

    volatile int intUserLeftSliderPosition=0;
    volatile int intUserRightSliderPosition=0;

    volatile String strWinner="";

    PongCanvas() {
      super();
    }

    public void initializeCanvas() {
      Dimension dimPanel=panPlay.getSize();

//Set the x, y, width, height of btnJoinQueue
      btnJoinQueue.setBounds(dimPanel.width/2-40, 80, 80, 30);

      int intCanvasX=dimPanel.width/2-intPongCanvasWidth/2;
      int intCanvasY=dimPanel.height/2-intPongCanvasHeight/2;

//Set the x, y, width, height of this PongCanvas
      setBounds(intCanvasX, intCanvasY, intPongCanvasWidth, intPongCanvasHeight);

      invalidate();
      validate();
      repaint();
    }

    public void setWinner(String strWinner) {
      this.strWinner=strWinner;
    }

    public void arrowUp() {

synchronized(PongConstants.syncAll) {

      try {
//Send a message to the server that this user's slider should be moved up
        thrClient.serverDOS.writeInt(PongConstants.MOVE_SLIDER_UP);

        thrClient.serverDOS.flush();
      }
      catch(Exception ex) {
      }

}

    }

    public void arrowDown() {

synchronized(PongConstants.syncAll) {

      try {
//Send a message to the server that this user's slider should be moved down
        thrClient.serverDOS.writeInt(PongConstants.MOVE_SLIDER_DOWN);

        thrClient.serverDOS.flush();
      }
      catch(Exception ex) {
      }

}

    }

    public void paint(Graphics graph) {
      Dimension dimCanvas=getSize();

      if(strWinner.length()>0) {
//If the length of strWinner is greater than 0 then display the winner message instead of drawing the pong elements
        String strWins=strWinner+" Wins";

        FontMetrics fMetr=graph.getFontMetrics();
        int intWinsWidth=fMetr.stringWidth(strWins);
        int intWinsHeight=fMetr.getHeight();

        graph.drawString(strWins, dimCanvas.width/2-intWinsWidth/2, dimCanvas.height/2+intWinsHeight/2);

        return;
      }

synchronized(PongConstants.syncAll) {

      Color clrPrev=graph.getColor();

      graph.setColor(Color.black);

//Draw the northern edge
      graph.fillRect(0, 0, dimCanvas.width, intPongEdgeWidth);

//Draw the southern edge
      graph.fillRect(0, dimCanvas.height-intPongEdgeWidth, dimCanvas.width, intPongEdgeWidth);

      graph.setColor(Color.red);

//Draw the user's slider
      graph.fillRect(0, intUserLeftSliderPosition, intPongSliderWidth, intPongSliderHeight);

//Draw the AI's slider
      graph.fillRect(dimCanvas.width-intPongSliderWidth, intUserRightSliderPosition, intPongSliderWidth, intPongSliderHeight);

      graph.setColor(Color.green);

//Draw the ball
      graph.fillOval(intBallX-intPongBallWidth/2, intBallY-intPongBallHeight/2, intPongBallWidth, intPongBallHeight);

}

    }
  }
}