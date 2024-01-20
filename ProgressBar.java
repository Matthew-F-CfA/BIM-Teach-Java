import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class ProgressBar extends Frame {
  volatile ProgressBarCanvas progressBarCanvas=new ProgressBarCanvas();
  volatile ProgressBarThread progressBarThread=new ProgressBarThread();

  public static void main(String args[]) {
    try {
      ProgressBar pFrame=new ProgressBar();
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      pFrame.setSize(dimScreen.width, dimScreen.height-40);
      pFrame.setVisible(true);

//By starting this thread after setVisible is called we are guaranteed that the Canvas will have dimensions.
      pFrame.progressBarThread.start();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  ProgressBar() {
    super("Progress Bar");

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    add("Center", progressBarCanvas);
  }

  class ProgressBarCanvas extends Canvas {
    volatile double dblProgressBarPercent=0.0d;
    volatile boolean blnDisplayText=true;

    ProgressBarCanvas() {
      super();
    }

//Each tick increments the progress bar by 10%.
    public void tick() {
      if(dblProgressBarPercent>=0.95d && dblProgressBarPercent<=1.05d) {
        dblProgressBarPercent=0.0d;

        blnDisplayText=!blnDisplayText;
      }
      else {
        dblProgressBarPercent+=0.1d;
      }

      repaint();
    }

    public void paint(Graphics graph) {
      Dimension dimCanvas=getSize();

      if(blnDisplayText) {
        Font fntObj=graph.getFont();
        fntObj=fntObj.deriveFont(30.0f);
        graph.setFont(fntObj);
        FontMetrics fMetr=graph.getFontMetrics();
        String strDisplay="Hello World";
        int intStringWidth=fMetr.stringWidth(strDisplay);

        graph.drawString(strDisplay, dimCanvas.width/2-intStringWidth/2, dimCanvas.height/4);
      }

      Color clrPrev=graph.getColor();

      graph.setColor(Color.blue);

      int intProgressBarWidth=dimCanvas.width/2;
      double dblElapsed=((double)intProgressBarWidth)*dblProgressBarPercent;
      int intElapsed=(int)Math.floor(dblElapsed);
      int intProgressBarTranslateX=dimCanvas.width/4;

//Draw the blue part of the progress bar
      graph.fillRect(intProgressBarTranslateX, dimCanvas.height*3/4, intElapsed, 50);

      graph.setColor(Color.gray);

//Draw the gray part of the progress bar
      graph.fillRect(intProgressBarTranslateX+intElapsed, dimCanvas.height*3/4, intProgressBarWidth-intElapsed, 50);

      graph.setColor(clrPrev);
    }
  }

  class ProgressBarThread extends Thread {
    volatile boolean blnKeepAlive=true;

    ProgressBarThread() {
      super();
    }

    public void run() {
      while(blnKeepAlive) {
        try {
          Thread.sleep(1000l);
        }
        catch(Exception ex) {
        }

        progressBarCanvas.tick();
      }
    }
  }
}