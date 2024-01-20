import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.*;

class OpenStream extends Frame
implements ActionListener {
  TextField txtURL=new TextField();
  Button btnOpenStream=new Button("Open Stream");
  TextArea txtArea=new TextArea();

  public static void main(String args[]) {
    OpenStream oFrame=new OpenStream();
    Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
    oFrame.setSize(dimScreen.width, dimScreen.height);
    oFrame.setVisible(true);
  }

  OpenStream() {
    super("Open Stream to Internet");

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    Panel tempPan=new Panel();
    tempPan.setLayout(new BorderLayout());
    tempPan.add("West", new Label("URL:"));
    tempPan.add("Center", txtURL);
    tempPan.add("East", btnOpenStream);
    btnOpenStream.addActionListener(this);
 
    add("North", tempPan);

    add("Center", txtArea);
  }

  public void actionPerformed(ActionEvent ae) {
    Object evSource=ae.getSource();

    if(evSource==btnOpenStream) {
      String strURL=txtURL.getText();
      if(strURL.length()==0) {
        txtURL.setText("Enter URL here.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex) {
        }
        txtURL.setText("");

        return;
      }

      txtArea.setText("");

      try {
        URL url=new URL(strURL);

        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));
        String strNextLine="";
        while((strNextLine=br.readLine())!=null) {
          txtArea.append(strNextLine+"\n");
        }
        br.close();
      }
      catch(Exception ex) {
        StackTraceElement ste[]=ex.getStackTrace();
        for(int i=0;i<ste.length;i++) {
          txtArea.append(ste[i].toString()+"\n");
        }
      }
    }
  }
}