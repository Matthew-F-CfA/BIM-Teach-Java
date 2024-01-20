import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.util.zip.*;
import javax.crypto.*;
import javax.crypto.spec.*;

class ClientSideApplicationZippedAndEncrypted extends Frame
implements ActionListener {
  volatile Socket clientSocket;
  volatile DataInputStream dis;
  volatile DataOutputStream dos;
  volatile ClientSideThread clientSideThread;

  volatile TextArea txtArea=new TextArea();
  volatile TextField txtMessage=new TextField();
  volatile Button btnSendMessage=new Button("Send Message");

  volatile SecretKey key;

  public static void main(String args[]) {
    try {
      if(args.length!=2) {
        System.out.println("Usage:");
        System.out.println("  java ClientSideApplicationZippedAndEncrypted <port number to connect to> <password for encryption key; must be at least 8 characters>");

        return;
      }
      else if(args[1].length()<8) {
        System.out.println("Usage:");
        System.out.println("  java ClientSideApplicationZippedAndEncrypted <port number to connect to> <password for encryption key; must be at least 8 characters>");

        return;
      }

      int intPort=Integer.parseInt(args[0]);

      Socket clientSocket=new Socket("localhost", intPort);

      ClientSideApplicationZippedAndEncrypted cFrame=new ClientSideApplicationZippedAndEncrypted(clientSocket);
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      cFrame.setSize(dimScreen.width, dimScreen.height-40);
      cFrame.setVisible(true);

      DESKeySpec dks=new DESKeySpec(args[1].getBytes());
      SecretKeyFactory skf=SecretKeyFactory.getInstance("DES");
      cFrame.key=skf.generateSecret(dks);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  ClientSideApplicationZippedAndEncrypted(Socket clientSocket) throws Exception {
    super("Client Side Application Zipped");
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
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ZipOutputStream zos=new ZipOutputStream(baos);
        byte byteBuf[]=strMessage.getBytes();
        zos.putNextEntry(new ZipEntry("message"));
        zos.write(byteBuf, 0, byteBuf.length);
        zos.closeEntry();
        zos.flush();
        byte byteCompressedBuf[]=baos.toByteArray();

        Cipher desCipher=Cipher.getInstance("DES/ECB/PKCS5Padding");

        // Initialize the cipher for encryption
        desCipher.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt the text
        byte textEncrypted[]=desCipher.doFinal(byteCompressedBuf);
        
        dos.writeInt(textEncrypted.length);
        dos.write(textEncrypted, 0, textEncrypted.length);

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

          Cipher desCipher=Cipher.getInstance("DES/ECB/PKCS5Padding");

          // Initialize the cipher for decryption
          desCipher.init(Cipher.DECRYPT_MODE, key);

          // Encrypt the text
          byte textDecrypted[]=desCipher.doFinal(byteBuf);


          String strMessage="";

          ByteArrayInputStream bais=new ByteArrayInputStream(textDecrypted);
          ZipInputStream zis=new ZipInputStream(bais);
          ZipEntry zEntry=zis.getNextEntry();
          if(zEntry!=null) {
            int n=-1;
            byte buf[]=new byte[1024];
            while((n=zis.read(buf, 0, 1024))>-1) {
              strMessage+=new String(buf, 0, n);
            }

            zis.closeEntry();
          }

          txtArea.append(strMessage+"\n");
        }
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}