import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class ArrayCopy extends Frame
implements ActionListener, ItemListener {
  List lstArray1=new List(5);
  List lstArray2=new List(5);
  TextField txt1=new TextField();
  TextField txt2=new TextField();
  Button btnSet1=new Button("Set To Text");
  Button btnSet2=new Button("Set To Text");
  CheckboxGroup cbg=new CheckboxGroup();
  Checkbox cbDeep=new Checkbox("Deep Copy", cbg, false);
  Checkbox cbShallow=new Checkbox("Shallow Copy", cbg, true);

  StringContainer strContainer1[]=new StringContainer[3];
  StringContainer strContainer2[]=new StringContainer[3];

  public static void main(String args[]) {
    try {
      ArrayCopy aFrame=new ArrayCopy();
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      aFrame.setSize(dimScreen.width, dimScreen.height-40);
      aFrame.setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  ArrayCopy() {
    super("Deep Copy vs Shallow Copy");

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    strContainer1[0]=new StringContainer("Testing 1");
    strContainer1[1]=new StringContainer("Testing 2");
    strContainer1[2]=new StringContainer("Testing 3");

    strContainer2=strContainer1;

    lstArray1.add(strContainer1[0].getString());
    lstArray1.add(strContainer1[1].getString());
    lstArray1.add(strContainer1[2].getString());

    lstArray2.add(strContainer2[0].getString());
    lstArray2.add(strContainer2[1].getString());
    lstArray2.add(strContainer2[2].getString());


    Panel tempPan=new Panel();
    tempPan.setLayout(new GridLayout(1, 2));

    Panel tempPanA=new Panel();
    tempPanA.setLayout(new BorderLayout());
    tempPanA.add("Center", lstArray1);
    Panel tempPanA1=new Panel();
    tempPanA1.setLayout(new GridLayout(3, 1));
    tempPanA1.add(txt1);
    tempPanA1.add(btnSet1);
    btnSet1.addActionListener(this);
    tempPanA1.add(cbDeep);
    cbDeep.addItemListener(this);
    tempPanA.add("South", tempPanA1);
    tempPan.add(tempPanA);

    Panel tempPanB=new Panel();
    tempPanB.setLayout(new BorderLayout());
    tempPanB.add("Center", lstArray2);
    Panel tempPanB1=new Panel();
    tempPanB1.setLayout(new GridLayout(3, 1));
    tempPanB1.add(txt2);
    tempPanB1.add(btnSet2);
    btnSet2.addActionListener(this);
    tempPanB1.add(cbShallow);
    cbShallow.addItemListener(this);
    tempPanB.add("South", tempPanB1);
    tempPan.add(tempPanB);

    add("Center", tempPan);
  }

  public void actionPerformed(ActionEvent ae) {
    Object evSource=ae.getSource();

    if(evSource==btnSet1) {
      int intSelectedIndex=lstArray1.getSelectedIndex();
      if(intSelectedIndex==-1)
        return;

      String strSet=txt1.getText();

      strContainer1[intSelectedIndex].setString(strSet);

      lstArray1.replaceItem(strContainer1[intSelectedIndex].getString(), intSelectedIndex);
      lstArray2.replaceItem(strContainer2[intSelectedIndex].getString(), intSelectedIndex);      
    }
    else if(evSource==btnSet2) {
      int intSelectedIndex=lstArray2.getSelectedIndex();
      if(intSelectedIndex==-1)
        return;

      String strSet=txt2.getText();

      strContainer2[intSelectedIndex].setString(strSet);

      lstArray1.replaceItem(strContainer1[intSelectedIndex].getString(), intSelectedIndex);
      lstArray2.replaceItem(strContainer2[intSelectedIndex].getString(), intSelectedIndex);      
    }
  }

  public void itemStateChanged(ItemEvent ie) {
    Checkbox cbSelected=cbg.getSelectedCheckbox();

    if(cbSelected==cbDeep) {
      strContainer2=new StringContainer[strContainer1.length];

      for(int i=0;i<strContainer2.length;i++)
        strContainer2[i]=new StringContainer(strContainer1[i].getString());
    }
    else if(cbSelected==cbShallow) {
      System.arraycopy(strContainer1, 0, strContainer2, 0, strContainer1.length);
    }
  }

  class StringContainer {
    String strStr="";

    StringContainer() {
    }

    StringContainer(String strStr) {
      this.strStr=strStr;
    }

    public String getString() {
      return strStr;
    }

    public void setString(String strStr) {
      this.strStr=strStr;
    }
  }
}