import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class AWTExample extends Frame
implements ActionListener {
  Menu menuFile=new Menu("File");
  MenuItem menuFileExit=new MenuItem("Exit");
  TextField txtColor=new TextField();
  Button btnAddColor=new Button("Add Color");
  List lstColors=new List(5);
  Button btnRemoveColor=new Button("Remove Color");
  Label lblMostRecentFavoriteColor=new Label("");
  TextArea txtMyFavoriteColors=new TextArea(4, 100);
  Button btnChooseFavoriteColor=new Button("Choose Favorite Color");

  public static void main(String args[]) {
    AWTExample awtExample=new AWTExample("AWT Example");
    Toolkit toolkit=Toolkit.getDefaultToolkit();
    Dimension dimScreen=toolkit.getScreenSize();
    awtExample.setSize(dimScreen.width, dimScreen.height-40);
    awtExample.setVisible(true);
  }

  AWTExample(String strTitle) {
    super(strTitle);

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    Panel tempPan=new Panel();
    tempPan.setLayout(new BorderLayout());
    tempPan.add("West", new Label("Color: "));
    tempPan.add("Center", txtColor);
    tempPan.add("East", btnAddColor);
    btnAddColor.addActionListener(this);

    add("North", tempPan);

    Panel tempPan2=new Panel();
    tempPan2.setLayout(new BorderLayout());
    tempPan2.add("North", new Label("List of Colors:"));
    tempPan2.add("Center", lstColors);
    tempPan2.add("South", btnRemoveColor);
    btnRemoveColor.addActionListener(this);

    add("Center", tempPan2);

    Panel tempPan3=new Panel();
    tempPan3.setLayout(new BorderLayout());
    Panel tempPan3A=new Panel();
    tempPan3A.setLayout(new GridLayout(2, 1));
    Panel tempPan3A1=new Panel();
    tempPan3A1.setLayout(new GridLayout(1, 2));
    tempPan3A1.add(new Label("Most Recent Favorite Color: "));
    tempPan3A1.add(lblMostRecentFavoriteColor);
    tempPan3A.add(tempPan3A1);
    tempPan3A.add(new Label("Past Favorite Colors:"));   
    tempPan3.add("North", tempPan3A);
    tempPan3.add("Center", txtMyFavoriteColors);
    Panel tempPan3B=new Panel();
    tempPan3B.add(btnChooseFavoriteColor);
    btnChooseFavoriteColor.addActionListener(this);
    tempPan3.add("South", tempPan3B);

    add("South", tempPan3);

    menuFile.add(menuFileExit);
    menuFileExit.addActionListener(this);

    MenuBar mBar=new MenuBar();
    mBar.add(menuFile);

    setMenuBar(mBar);
  }

  public void actionPerformed(ActionEvent ae) {
    Object evSource=ae.getSource();

    if(evSource==menuFileExit) {
      System.exit(0);
    }
    else if(evSource==btnAddColor) {
      lstColors.add(txtColor.getText());
    }
    else if(evSource==btnRemoveColor) {
      int intSelectedIndex=lstColors.getSelectedIndex();
      if(intSelectedIndex==-1)
        return;

      lstColors.remove(intSelectedIndex);
    }
    else if(evSource==btnChooseFavoriteColor) {
      String strItems[]=lstColors.getItems();

      ChooseFavoriteColorDialog cDialog=new ChooseFavoriteColorDialog(this, strItems);
      cDialog.show();

      if(cDialog.isCanceled())
        return;

      String strChoice=cDialog.getChoice();

      lblMostRecentFavoriteColor.setText(strChoice);
      txtMyFavoriteColors.append(strChoice+"\n");
    }
  }

  class ChooseFavoriteColorDialog extends Dialog
  implements ActionListener, ItemListener {
    List lstColorChoices=new List(5);
    Button btnCancel=new Button("Cancel");
    boolean blnCanceled=false;

    ChooseFavoriteColorDialog(Frame parent, String strItems[]) {
      super(parent, "Choose Favorite Color Dialog", true);

      for(int i=0;i<strItems.length;i++) {
        lstColorChoices.add(strItems[i]);
      }

      add("North", new Label("Color Choices:"));

      add("Center", lstColorChoices);
      lstColorChoices.addItemListener(this);

      add("South", btnCancel);
      btnCancel.addActionListener(this);

      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(dimScreen.width/4, dimScreen.height/4);
      setSize(dimScreen.width/2, dimScreen.height/2);
    }

    public boolean isCanceled() {
      return blnCanceled;
    }

    public String getChoice() {
      return lstColorChoices.getSelectedItem();
    }

    public void itemStateChanged(ItemEvent ie) {
      Object evSource=ie.getSource();

      if(evSource==lstColorChoices) {
        int intSelectedIndex=lstColorChoices.getSelectedIndex();
        if(intSelectedIndex==-1)
          return;

        blnCanceled=false;

        dispose();
      }
    }

    public void actionPerformed(ActionEvent ae) {
      Object evSource=ae.getSource();

      if(evSource==btnCancel) {
        blnCanceled=true;

        dispose();
      }
    }
  }
}