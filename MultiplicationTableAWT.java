import java.util.TreeMap;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class MultiplicationTableAWT extends Frame {
  static TreeMap mapMultiplication=new TreeMap();

  MultiplicationTableCanvas multiplicationCanvas=new MultiplicationTableCanvas();

  static {
    for(int i=0;i<10;i++) {
      for(int ia=0;ia<10;ia++) {
        mapMultiplication.put(new TwoDimensionalKey(i, ia), new Integer(i*ia));
      }
    }
  }

  public static void main(String args[]) {
    try {
      MultiplicationTableAWT mFrame=new MultiplicationTableAWT();
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      mFrame.setSize(dimScreen.width, dimScreen.height-40);
      mFrame.setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  MultiplicationTableAWT() {
    super("Multiplication Table");

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    add("Center", multiplicationCanvas);
    multiplicationCanvas.addMouseListener(multiplicationCanvas);
  }

  class MultiplicationTableCanvas extends Canvas
  implements MouseListener {
    TreeMap mapShowProduct=new TreeMap(); //Keys are TwoDimensionalKey and Values are Boolean(if true then show the product otherwise if false show the multiplication expression.

    MultiplicationTableCanvas() {
      super();

      for(int i=0;i<10;i++) {
        for(int ia=0;ia<10;ia++) {
          mapShowProduct.put(new TwoDimensionalKey(i, ia), new Boolean(false));
        }
      }
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
      Point mePoint=me.getPoint();
      int meX=mePoint.x;
      int meY=mePoint.y;

      Dimension dimCanvasSize=getSize();

      int intCanvasWidth=dimCanvasSize.width;
      int intCanvasHeight=dimCanvasSize.height;

      int intRectangleWidth=intCanvasWidth/10;
      int intRectangleHeight=intCanvasHeight/10;

      int intGridX=meX/intRectangleWidth;
      int intGridY=meY/intRectangleHeight;

      if(intGridX>9)
        return;

      if(intGridY>9)
        return;

      boolean blnShowProduct=((Boolean)mapShowProduct.get(new TwoDimensionalKey(intGridX, intGridY))).booleanValue();

      blnShowProduct=!blnShowProduct; //if blnShowProduct was false it will now be true. if blnShowProduct was true it will now be false.

      mapShowProduct.put(new TwoDimensionalKey(intGridX, intGridY), new Boolean(blnShowProduct));

      repaint();
    }

    public void paint(Graphics graph) {
      int intLetterHeight=graph.getFontMetrics().getHeight();

      Dimension dimCanvasSize=getSize();

      int intCanvasWidth=dimCanvasSize.width;
      int intCanvasHeight=dimCanvasSize.height;

      int intRectangleWidth=intCanvasWidth/10;
      int intRectangleHeight=intCanvasHeight/10;

      int intPositionX=0;
      int intPositionY=0;

      for(int i=0;i<10;i++) {
        intPositionX=0;
        for(int ia=0;ia<10;ia++) {
          boolean blnShowProduct=((Boolean)mapShowProduct.get(new TwoDimensionalKey(ia, i))).booleanValue();

          if(blnShowProduct) {
            int intProduct=((Integer)mapMultiplication.get(new TwoDimensionalKey(ia, i))).intValue();

            graph.drawString(String.valueOf(intProduct), intPositionX, intPositionY+intLetterHeight);
          }
          else {
            String strExpression=String.valueOf(ia)+" * "+String.valueOf(i);

            graph.drawString(strExpression, intPositionX, intPositionY+intLetterHeight);
          }

          intPositionX+=intRectangleWidth;
        }
        intPositionY+=intRectangleHeight;
      }
    }
  }
}