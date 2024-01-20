import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class CanvasVector extends Frame
implements ActionListener {
  TextField txtRadius=new TextField();
  TextField txtTheta=new TextField();
  Button btnDisplayVector=new Button("Display Vector");
  CanvasVectorCanvas canvasVectorCanvas=new CanvasVectorCanvas();

  public static void main(String args[]) {
    try {
      CanvasVector cFrame=new CanvasVector();
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      cFrame.setSize(dimScreen.width, dimScreen.height-40);
      cFrame.setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  CanvasVector() {
    super("Canvas Vector");

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    Panel tempPan=new Panel();
    tempPan.setLayout(new GridLayout(3, 1));
    Panel tempPanA=new Panel();
    tempPanA.setLayout(new GridLayout(1, 2));
    tempPanA.add(new Label("Radius:"));
    tempPanA.add(txtRadius);
    tempPan.add(tempPanA);
    Panel tempPanB=new Panel();
    tempPanB.setLayout(new GridLayout(1, 2));
    tempPanB.add(new Label("Theta(in degrees):"));
    tempPanB.add(txtTheta);
    tempPan.add(tempPanB);
    Panel tempPanC=new Panel();
    tempPanC.add(btnDisplayVector);
    btnDisplayVector.addActionListener(this);
    tempPan.add(tempPanC);

    add("North", tempPan);

    add("Center", canvasVectorCanvas);
  }

  public void actionPerformed(ActionEvent ae) {
    Object evSource=ae.getSource();

    if(evSource==btnDisplayVector) {
      String strRadius=txtRadius.getText();

      if(strRadius.length()==0) {
        txtRadius.setText("Radius required.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex) {
        }
        txtRadius.setText("");

        return;
      }

      double dblRadius=0.0d;
      try {
        dblRadius=Double.valueOf(strRadius).doubleValue();
      }
      catch(Exception ex) {
        txtRadius.setText("Digits only.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex2) {
        }
        txtRadius.setText(strRadius);

        return;
      }


      String strTheta=txtTheta.getText();

      if(strTheta.length()==0) {
        txtTheta.setText("Theta required.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex) {
        }
        txtTheta.setText("");

        return;
      }

      double dblTheta=0.0d;
      try {
        dblTheta=Double.valueOf(strTheta).doubleValue();
      }
      catch(Exception ex) {
        txtTheta.setText("Digits only.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex2) {
        }
        txtTheta.setText(strTheta);

        return;
      }

      canvasVectorCanvas.setVector(dblRadius, dblTheta);
    }
  }

  class CanvasVectorCanvas extends Canvas {
    double dblRadius=0.0d;
    double dblTheta=0.0d;
    int intX=-1;
    int intY=-1;
    boolean blnInitialized=false;

    CanvasVectorCanvas() {
      super();
    }

    public void setVector(double dblRadius, double dblTheta) {
      dblTheta=dblTheta%360.0d;
      double dblThetaRadians=0.0d;
      double dblX=0.0d;
      double dblY=0.0d;
      if(dblTheta>=0.0d && dblTheta<=90.0d) {
        dblThetaRadians=dblTheta*Math.PI/180.0d;

        dblX=Math.cos(dblThetaRadians);
        dblY=Math.sin(dblThetaRadians);
      }
      else if(dblTheta>=90.0d && dblTheta<=180.0d) {
        dblTheta=180.0d-dblTheta;
        
        dblThetaRadians=dblTheta*Math.PI/180.0d;

        dblX=-Math.cos(dblThetaRadians);
        dblY=Math.sin(dblThetaRadians);
      }
      else if(dblTheta>=180.0d && dblTheta<=270.0d) {
        dblTheta=dblTheta-180.0d;
        
        dblThetaRadians=dblTheta*Math.PI/180.0d;

        dblX=-Math.cos(dblThetaRadians);
        dblY=-Math.sin(dblThetaRadians);
      }
      else if(dblTheta>=270.0d && dblTheta<=360.0d) {
        dblTheta=360.0d-dblTheta;
        
        dblThetaRadians=dblTheta*Math.PI/180.0d;

        dblX=Math.cos(dblThetaRadians);
        dblY=-Math.sin(dblThetaRadians);
      }

      dblX=dblX*dblRadius;
      dblY=dblY*dblRadius;

      dblY=-dblY;

      intX=(int)Math.rint(dblX);
      intY=(int)Math.rint(dblY);

      Dimension dimCanvas=getSize();

      int intMiddleX=dimCanvas.width/2;
      int intMiddleY=dimCanvas.height/2;

      intX+=intMiddleX;
      intY+=intMiddleY;

      blnInitialized=true;

      repaint();
    }

    public void paint(Graphics graph) {
      if(!blnInitialized)
        return;

      Dimension dimCanvas=getSize();

      int intMiddleX=dimCanvas.width/2;
      int intMiddleY=dimCanvas.height/2;

      graph.drawLine(intMiddleX, intMiddleY, intX, intY);
    }
  }
}