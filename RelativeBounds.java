import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

class RelativeBounds extends Frame {
  RelativeBounds refThis;

  RelativeBoundsCanvas relativeBoundsCanvas=new RelativeBoundsCanvas();

  public static void main(String args[]) {
    try {
      RelativeBounds rFrame=new RelativeBounds();
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      rFrame.setSize(dimScreen.width, dimScreen.height-40);
      rFrame.setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  RelativeBounds() {
    super("Relative Bounds");

    refThis=this;

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    add("Center", relativeBoundsCanvas);
    relativeBoundsCanvas.addMouseListener(relativeBoundsCanvas);
    relativeBoundsCanvas.addMouseMotionListener(relativeBoundsCanvas);
  }

  class RelativeBoundsCanvas extends Canvas
  implements MouseListener, MouseMotionListener {
    Vector vecRectangles=new Vector();
    boolean blnDragging=false;
    int intCurrentX=-1; //The x pixel when mousePressed event is generated
    int intCurrentY=-1; //The y pixel when mousePressed event is generated
    int intCurrentX2=-1; //The x pixel when mouseDragged event is generated
    int intCurrentY2=-1; //The y pixel when mouseDragged event is generated

    RelativeBoundsCanvas() {
      super();
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {
      Point mePoint=me.getPoint();

      int intMods=me.getModifiersEx();
      intMods=intMods & MouseEvent.BUTTON3_DOWN_MASK;
      if(intMods==MouseEvent.BUTTON3_DOWN_MASK) {
//If the mouse button being pressed is the right mouse button then return because only a left mouse button drag should create a rectangle
        int meX=mePoint.x;
        int meY=mePoint.y;

        for(int i=0;i<vecRectangles.size();i++) {
          Rectangle nextRectangle=(Rectangle)vecRectangles.elementAt(i);

          if(nextRectangle.contains(meX, meY)) {
            MessageDialog mDialog=new MessageDialog(refThis, "Rectangle Information Dialog", "("+nextRectangle.getX()+", "+nextRectangle.getY()+", "+nextRectangle.getWidth()+", "+nextRectangle.getHeight()+")");
            mDialog.show();

            break;
          }
        }


        return;
      }

      intCurrentX=mePoint.x;
      intCurrentY=mePoint.y;
      intCurrentX2=intCurrentX;
      intCurrentY2=intCurrentY;
      blnDragging=true;
    }

    public void mouseReleased(MouseEvent me) {
      if(!blnDragging) {
//If not blnDragging then return because the mousePressed event was as a result of a right mouse button click
        return;
      }

      Point mePoint=me.getPoint();
      int meX=mePoint.x;
      int meY=mePoint.y;

      int intNewRectangleX=-1;
      int intNewRectangleWidth=-1;
      int intNewRectangleY=-1;
      int intNewRectangleHeight=-1;

      if(meX<intCurrentX) {
        intNewRectangleX=meX;
        intNewRectangleWidth=intCurrentX-meX;
      }
      else {
        intNewRectangleX=intCurrentX;
        intNewRectangleWidth=meX-intCurrentX;
      }

      if(meY<intCurrentY) {
        intNewRectangleY=meY;
        intNewRectangleHeight=intCurrentY-meY;
      }
      else {
        intNewRectangleY=intCurrentY;
        intNewRectangleHeight=meY-intCurrentY;
      }

      Rectangle rectangle=new Rectangle(intNewRectangleX, intNewRectangleY, intNewRectangleWidth, intNewRectangleHeight);

      vecRectangles.addElement(rectangle);

      blnDragging=false;

      repaint();
    }

    public void mouseClicked(MouseEvent me) {
    }

    public void mouseMoved(MouseEvent me) {
    }

    public void mouseDragged(MouseEvent me) {
      if(!blnDragging) {
//If not blnDragging then return because the mousePressed event was as a result of a right mouse button click
        return;
      }

      Point mePoint=me.getPoint();
      intCurrentX2=mePoint.x;
      intCurrentY2=mePoint.y;

      repaint();
    }

    public void paint(Graphics graph) {
      Color clrPrev=graph.getColor();

      graph.setColor(Color.red);

      for(int i=0;i<vecRectangles.size();i++) { 
        Rectangle nextRectangle=(Rectangle)vecRectangles.elementAt(i);

        int intNextX=(int)nextRectangle.getX();
        int intNextY=(int)nextRectangle.getY();
        int intNextWidth=(int)nextRectangle.getWidth();
        int intNextHeight=(int)nextRectangle.getHeight();

        graph.drawRect(intNextX, intNextY, intNextWidth, intNextHeight);
      }

      graph.setColor(Color.green);

      if(blnDragging) {
        int intNewRectangleX=-1;
        int intNewRectangleWidth=-1;
        int intNewRectangleY=-1;
        int intNewRectangleHeight=-1;

        if(intCurrentX2<intCurrentX) {
          intNewRectangleX=intCurrentX2;
          intNewRectangleWidth=intCurrentX-intCurrentX2;
        }
        else {
          intNewRectangleX=intCurrentX;
          intNewRectangleWidth=intCurrentX2-intCurrentX;
        }

        if(intCurrentY2<intCurrentY) {
          intNewRectangleY=intCurrentY2;
          intNewRectangleHeight=intCurrentY-intCurrentY2;
        }
        else {
          intNewRectangleY=intCurrentY;
          intNewRectangleHeight=intCurrentY2-intCurrentY;
        }

        graph.drawRect(intNewRectangleX, intNewRectangleY, intNewRectangleWidth, intNewRectangleHeight);
      }

      graph.setColor(clrPrev);
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