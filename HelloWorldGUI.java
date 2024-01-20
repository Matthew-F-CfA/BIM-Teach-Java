package helloWorldPackage;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class HelloWorldGUI extends Frame {

  public static void main(String args[]) {
    HelloWorldGUI hFrame=new HelloWorldGUI();
    Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
    hFrame.setSize(dimScreen.width, dimScreen.height-40);
    hFrame.setVisible(true);
  }

  HelloWorldGUI() {
    super("Hello World GUI");

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    add("Center", new Label("Hello World"));
  }
}