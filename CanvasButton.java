import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class CanvasButton extends Frame {
  CanvasButton refThis;

  CanvasButtonCanvas canvasButtonCanvas=new CanvasButtonCanvas();

  public static void main(String args[]) {
    try {
      CanvasButton cFrame=new CanvasButton();
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      cFrame.setSize(dimScreen.width, dimScreen.height-40);
      cFrame.setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  CanvasButton() {
    super("Canvas Button");

    refThis=this;

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    add("Center", canvasButtonCanvas);
    canvasButtonCanvas.addMouseListener(canvasButtonCanvas);
  }

  class CanvasButtonCanvas extends Canvas
  implements MouseListener {

    CanvasButtonCanvas() {
      super();
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {
    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseClicked(MouseEvent me) {
      MessageDialog mDialog=new MessageDialog(refThis, "Mouse Clicked Dialog", "Mouse clicked inside the canvas.");
      mDialog.show();
    }

    public void paint(Graphics graph) {
      FontMetrics fMetr=graph.getFontMetrics();

      int intLetterHeightDividedByTwo=fMetr.getHeight()/2;

      int intStrWidthDividedByTwo=fMetr.stringWidth("Click Me")/2;

      Dimension dimCanvas=getSize();

      graph.drawString("Click Me", dimCanvas.width/2-intStrWidthDividedByTwo, dimCanvas.height/2+intLetterHeightDividedByTwo);
    }
  }

  class MessageDialog extends Dialog
  implements ActionListener {
    Button btnOkay=new Button("Okay");

    MessageDialog(Frame parent, String strTitle, String strMessage) {
      super(parent, strTitle, true);

      add("Center", new Label(strMessage));

      add("South", btnOkay);
      btnOkay.addActionListener(this);

      Dimension screenDim=Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(screenDim.width/4, screenDim.height/4);
      setSize(screenDim.width/2, screenDim.height/2);
    }

    public void actionPerformed(ActionEvent ae) {
      Object evSource=ae.getSource();

      if(evSource==btnOkay) {
        dispose();
      }
    }
  }
}