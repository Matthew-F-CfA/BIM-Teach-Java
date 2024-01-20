import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import java.util.Iterator;
import java.io.*;

class BufferedImageSample {

  public static void main(String args[]) {
    BufferedImageSample tObj=new BufferedImageSample();

    BufferedImageSample.BufferedImageFrameImage bFrame=tObj.new BufferedImageFrameImage();
    BufferedImageSample.BufferedImageFrameDisplayer bFrame2=tObj.new BufferedImageFrameDisplayer(bFrame);

    Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();

    bFrame.setSize(dimScreen.width, dimScreen.height-40);
    bFrame.setVisible(true);

    bFrame2.setSize(dimScreen.width, dimScreen.height-40);
    bFrame2.setVisible(true);

    bFrame.bufferedImageCanvas.initCanvas();
  }

  class BufferedImageFrameImage extends Frame
  implements ActionListener, ItemListener {
    BufferedImageCanvasImage bufferedImageCanvas=new BufferedImageCanvasImage();
    List lstColors=new List(5);
    Button btnSetTransluscent=new Button("Set Transluscent Color");

    Menu menuFile=new Menu("File");
    MenuItem menuFileLoad=new MenuItem("Load");
    MenuItem menuFileSave=new MenuItem("Save");

    BufferedImageFrameImage() {
      super("Buffered Image Sprite");

      setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));

      add("Center", bufferedImageCanvas);
 
      lstColors.add("red");
      lstColors.add("green");
      lstColors.add("blue");

      Panel tempPan=new Panel();
      tempPan.setLayout(new BorderLayout());
      tempPan.add("Center", lstColors);
      lstColors.addItemListener(this);
      tempPan.add("South", btnSetTransluscent);
      btnSetTransluscent.addActionListener(this);

      add("South", tempPan);

      menuFile.add(menuFileLoad);
      menuFileLoad.addActionListener(this);
      menuFile.add(menuFileSave);
      menuFileSave.addActionListener(this);

      MenuBar mBar=new MenuBar();
      mBar.add(menuFile);
      setMenuBar(mBar);
    }

    public void itemStateChanged(ItemEvent ie) {
      Object evSource=ie.getSource();

      if(evSource==lstColors) {
        int intSelectedIndex=lstColors.getSelectedIndex();
        if(intSelectedIndex==-1)
          return;

        String strSelectedItem=lstColors.getSelectedItem();

        if(strSelectedItem.equals("red")) {
          bufferedImageCanvas.clrCurrent=Color.red;
        }
        else if(strSelectedItem.equals("green")) {
          bufferedImageCanvas.clrCurrent=Color.green;
        }
        else if(strSelectedItem.equals("blue")) {
          bufferedImageCanvas.clrCurrent=Color.blue;
        }
      }
    }

    public void actionPerformed(ActionEvent ae) {
      Object evSource=ae.getSource();

      if(evSource==menuFileLoad) {
        try {
          File fileImg=new File("BufferedImageSampleImage.png");

          ImageInputStream input=ImageIO.createImageInputStream(fileImg);

          Iterator<ImageReader> readers=ImageIO.getImageReaders(input);

          if(!readers.hasNext()) {
            input.close();

            throw new IllegalArgumentException("No reader for: " + fileImg); // Or simply return null
          }

          ImageReader reader = readers.next();

          reader.setInput(input);

          // Configure the param to use the destination type you want
          ImageReadParam param = reader.getDefaultReadParam();
          param.setDestinationType(ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB));

          bufferedImageCanvas.bufferedImage=reader.read(0, param);

          bufferedImageCanvas.repaint();

          reader.dispose();

          input.close();
        }
        catch(Exception ex) {
          ex.printStackTrace();
        }
      }
      else if(evSource==menuFileSave) {
        try {
          File fileImg=new File("BufferedImageSampleImage.png");

          ImageIO.write(bufferedImageCanvas.bufferedImage, "png", fileImg);
        }
        catch(Exception ex) {
          ex.printStackTrace();
        }
      }
      else if(evSource==btnSetTransluscent) {
        int intSelectedIndex=lstColors.getSelectedIndex();
        if(intSelectedIndex==-1)
          return;

        BufferedImage bufferedImage=bufferedImageCanvas.bufferedImage;

        int intTransluscentColor=-1;

        String strSelectedItem=lstColors.getSelectedItem();

        if(strSelectedItem.equals("red")) {
          intTransluscentColor=255<<16;
        }
        else if(strSelectedItem.equals("green")) {
          intTransluscentColor=255<<8;
        }
        else if(strSelectedItem.equals("blue")) {
          intTransluscentColor=255;
        }


        DataBufferInt dbi=(DataBufferInt)bufferedImage.getRaster().getDataBuffer();
        int intPixels[]=dbi.getData();


        int intRGBMask=-1;
        int intRed=255;
        intRed=intRed<<16;
        int intGreen=255;
        intGreen=intGreen<<8;
        int intBlue=255;
        intRGBMask=intRed|intGreen|intBlue;

        int intTransluscentMask=255;
        intTransluscentMask=intTransluscentMask<<24;

        for(int i=0;i<intPixels.length;i++) {
          int intNextPixel=intPixels[i];
          intNextPixel=intNextPixel&intRGBMask;

          if(intNextPixel==intTransluscentColor || intNextPixel==0) {
            intPixels[i]=intNextPixel;
          }
          else {
            intPixels[i]=intNextPixel|intTransluscentMask;
          }
        }

        bufferedImageCanvas.repaint();
      }
    }
  }

  class BufferedImageFrameDisplayer extends Frame {
    BufferedImageFrameImage bufferedImageFrameImage;
    BufferedImageCanvasDisplayer bufferedImageCanvas=new BufferedImageCanvasDisplayer();

    BufferedImageFrameDisplayer(BufferedImageFrameImage bufferedImageFrameImage) {
      super("Buffered Image Sprite Displayer");
      this.bufferedImageFrameImage=bufferedImageFrameImage;

      bufferedImageCanvas.bufferedImageFrameImage=bufferedImageFrameImage;

      setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));

      add("Center", bufferedImageCanvas);
    }
  }

  class BufferedImageCanvasImage extends Canvas
  implements MouseListener {
    Color clrCurrent=Color.red;
    BufferedImage bufferedImage;
    Graphics bufferedImageGraphics;

    BufferedImageCanvasImage() {
      super();

      addMouseListener(this);
    }

    public void initCanvas() {
      Dimension dimCanvas=getSize();
      bufferedImage=new BufferedImage(dimCanvas.width, dimCanvas.height, BufferedImage.TYPE_INT_ARGB);
      bufferedImageGraphics=bufferedImage.getGraphics();

      repaint();
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

      bufferedImageGraphics.setColor(clrCurrent);

      bufferedImageGraphics.fillRect(meX, meY, 50, 50);

      repaint();
    }

    public void paint(Graphics graph) {
      if(bufferedImage==null)
        return;

      graph.drawImage(bufferedImage, 0, 0, null);
    }
  }

  class BufferedImageCanvasDisplayer extends Canvas
  implements MouseMotionListener {
    BufferedImageFrameImage bufferedImageFrameImage;
    int intMouseMovedX=0;
    int intMouseMovedY=0;

    BufferedImageCanvasDisplayer() {
      super();

      addMouseMotionListener(this);
    }

    public void mouseMoved(MouseEvent me) {
      Point mePoint=me.getPoint();
      intMouseMovedX=mePoint.x;
      intMouseMovedY=mePoint.y;

      repaint();
    }

    public void mouseDragged(MouseEvent me) {
    }

    public void paint(Graphics graph) {
      Color clrPrev=graph.getColor();

      graph.setColor(Color.gray);

      graph.fillRect(0, 0, getSize().width, getSize().height);

      BufferedImage bufferedImage=bufferedImageFrameImage.bufferedImageCanvas.bufferedImage;
      int intImageWidth=bufferedImage.getWidth();
      int intImageHeight=bufferedImage.getHeight();

      graph.drawImage(bufferedImage, intMouseMovedX-intImageWidth/2, intMouseMovedY-intImageHeight/2, null);

      graph.setColor(clrPrev);
    }
  }
}