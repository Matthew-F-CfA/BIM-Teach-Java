import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.File;

class CanvasImage extends Frame {
  CanvasImage refThis;

  CanvasImageCanvas canvasImageCanvas;

  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java CanvasImage <image file name>");

        return;
      }

      CanvasImage cFrame=new CanvasImage(args[0]);
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      cFrame.setSize(dimScreen.width, dimScreen.height-40);
      cFrame.setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  CanvasImage(String strImageName) throws Exception {
    super("Canvas Image");

    refThis=this;

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    String strUserDir=System.getProperty("user.dir");
    String strFileSep=System.getProperty("file.separator");
    if(!strUserDir.endsWith(strFileSep))
      strUserDir=strUserDir+strFileSep;


    String strRelativePathName=strUserDir+strImageName;
    File fileImage=new File(strRelativePathName);
    URL urlImage=fileImage.toURI().toURL();

    BufferedImage imgBuffered=ImageIO.read(urlImage);
    canvasImageCanvas=new CanvasImageCanvas(imgBuffered);
    add("Center", canvasImageCanvas);
  }

  class CanvasImageCanvas extends Canvas {
    BufferedImage imgBuffered;

    CanvasImageCanvas(BufferedImage imgBuffered) {
      super();

      this.imgBuffered=imgBuffered;
    }

    public void paint(Graphics graph) {
      Dimension dimCanvas=getSize();

      double dblCanvasWidth=(double)dimCanvas.width;
      double dblCanvasHeight=(double)dimCanvas.height;

      double dblImageWidth=(double)imgBuffered.getWidth();
      double dblImageHeight=(double)imgBuffered.getHeight();

      Graphics2D graph2d=(Graphics2D)graph;

      graph2d.scale(dblCanvasWidth/dblImageWidth, dblCanvasHeight/dblImageHeight);

      graph2d.drawImage(imgBuffered, 0, 0, null);
    }
  }
}