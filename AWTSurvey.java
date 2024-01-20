import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class AWTSurvey extends Frame
implements ActionListener {
  TextField txtName=new TextField();
  CheckboxGroup cbgGender=new CheckboxGroup();
  Checkbox cbGenderMale=new Checkbox("Male", cbgGender, true);
  Checkbox cbGenderFemale=new Checkbox("Female", cbgGender, false);
  Choice chcMonth=new Choice();
  Checkbox cbLikeEnglish=new Checkbox("I like English", false);
  Checkbox cbLikeMath=new Checkbox("I like Math", false);
  Checkbox cbLikeScience=new Checkbox("I like Science", false);
  Button btnShowSurvey=new Button("Show Survey");
  TextArea txtArea=new TextArea();

  public static void main(String args[]) {
    try {
      AWTSurvey aFrame=new AWTSurvey();
      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      aFrame.setSize(dimScreen.width, dimScreen.height-40);
      aFrame.setVisible(true);
    }
    catch(Exception ex) {
    }
  }

  AWTSurvey() {
    super("AWT Survey");

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    chcMonth.add("January");
    chcMonth.add("February");
    chcMonth.add("March");
    chcMonth.add("April");
    chcMonth.add("May");
    chcMonth.add("June");
    chcMonth.add("July");
    chcMonth.add("August");
    chcMonth.add("September");
    chcMonth.add("October");
    chcMonth.add("November");
    chcMonth.add("December");

    Panel tempPan=new Panel();
    tempPan.setLayout(new GridLayout(6, 1));
    Panel tempPanA=new Panel();
    tempPanA.setLayout(new BorderLayout());
    tempPanA.add("West", new Label("Name: "));
    tempPanA.add("Center", txtName);
    tempPan.add(tempPanA);
    Panel tempPanB=new Panel();
    tempPanB.setLayout(new GridLayout(1, 3));
    tempPanB.add(new Label("Gender:"));
    tempPanB.add(cbGenderMale);
    tempPanB.add(cbGenderFemale);
    tempPan.add(tempPanB);
    Panel tempPanC=new Panel();
    tempPanC.setLayout(new BorderLayout());
    tempPanC.add("West", new Label("Favorite Month:"));
    tempPanC.add("Center", chcMonth);
    tempPan.add(tempPanC);
    Panel tempPanD=new Panel();
    tempPanD.setLayout(new GridLayout(1, 3));
    tempPanD.add(cbLikeEnglish);
    tempPanD.add(cbLikeMath);
    tempPanD.add(cbLikeScience);
    tempPan.add(tempPanD);
    Panel tempPanE=new Panel();
    tempPanE.add(btnShowSurvey);
    btnShowSurvey.addActionListener(this);
    tempPan.add(tempPanE);
    tempPan.add(new Label("Survey Output:"));

    add("North", tempPan);

    add("Center", txtArea);
  }

  public void actionPerformed(ActionEvent ae) {
    Object evSource=ae.getSource();

    if(evSource==btnShowSurvey) {
      String strName=txtName.getText();

      boolean blnGenderMale=true;
      Checkbox cbSelected=cbgGender.getSelectedCheckbox();
      if(cbSelected==cbGenderFemale)
        blnGenderMale=false;

      String strMonth=chcMonth.getSelectedItem();

      boolean blnLikeEnglish=cbLikeEnglish.getState();
      boolean blnLikeMath=cbLikeMath.getState();
      boolean blnLikeScience=cbLikeScience.getState();

      StringBuffer strBuffer=new StringBuffer();

      strBuffer.append("Name: ");
      strBuffer.append(strName);
      strBuffer.append("\n");

      strBuffer.append("Gender: ");
      if(blnGenderMale) {
        strBuffer.append("Male");
      }
      else {
        strBuffer.append("Female");
      }
      strBuffer.append("\n");

      strBuffer.append("Favorite Month: ");
      strBuffer.append(strMonth);
      strBuffer.append("\n");

      if(blnLikeEnglish) {
        strBuffer.append("Likes English");
      }
      else {
        strBuffer.append("Doesn't like English");
      }
      strBuffer.append("\n");
      
      if(blnLikeMath) {
        strBuffer.append("Likes Math");
      }
      else {
        strBuffer.append("Doesn't like Math");
      }
      strBuffer.append("\n");

      if(blnLikeScience) {
        strBuffer.append("Likes Science");
      }
      else {
        strBuffer.append("Doesn't like Science");
      }
      strBuffer.append("\n");

      txtArea.setText(strBuffer.toString());
    }
  }
}