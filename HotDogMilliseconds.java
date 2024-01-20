import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

class HotDogMilliseconds extends Frame
implements ActionListener {
  double dblCostFlat=0.0d;
  double dblCostPer=0.0d;
  double dblRevenuePer=0.0d;
  double dblProfitPer=0.0d;
  String strLogFilePath="";

  TextField txtNumberOfHotDogs=new TextField();
  TextField txtYear=new TextField();
  TextField txtMonth=new TextField();
  TextField txtDate=new TextField();
  TextField txtHour=new TextField();
  Button btnSold=new Button("Sold");

  TextArea txtStatus=new TextArea();
  Button btnStatistics=new Button("Statistics");

  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java HotDogMilliseconds <flat cost> <per hot dog cost> <per hot dog revenue> <log file path>");
        System.out.println("OR");
        System.out.println("  java HotDogMilliseconds <existing log file path>");

        return;
      }

      if(args.length==4) {
        double dblCostFlat=Double.valueOf(args[0]).doubleValue();
        double dblCostPer=Double.valueOf(args[1]).doubleValue();
        double dblRevenuePer=Double.valueOf(args[2]).doubleValue();
        String strLogFilePath=args[3];

        HotDogMilliseconds hFrame=new HotDogMilliseconds(dblCostFlat, dblCostPer, dblRevenuePer, strLogFilePath);
        Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
        hFrame.setSize(dimScreen.width, dimScreen.height-40);
        hFrame.setVisible(true);
      }
      else if(args.length==1) {
        String strLogFilePath=args[0];

        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(strLogFilePath))));

        String strCostAndRevenue=br.readLine();

        int intSeparatorIndex=strCostAndRevenue.indexOf(",");
        int intSeparatorIndex2=strCostAndRevenue.indexOf(",", intSeparatorIndex+1);

        String strCostFlat=strCostAndRevenue.substring(0, intSeparatorIndex);
        String strCostPer=strCostAndRevenue.substring(intSeparatorIndex+1, intSeparatorIndex2);
        String strRevenuePer=strCostAndRevenue.substring(intSeparatorIndex2+1);

        double dblCostFlat=Double.valueOf(strCostFlat).doubleValue();
        double dblCostPer=Double.valueOf(strCostPer).doubleValue();
        double dblRevenuePer=Double.valueOf(strRevenuePer).doubleValue();

        HotDogMilliseconds hFrame=new HotDogMilliseconds(dblCostFlat, dblCostPer, dblRevenuePer, strLogFilePath);
        Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
        hFrame.setSize(dimScreen.width, dimScreen.height-40);
        hFrame.setVisible(true);

//Skip over the second line in the file because the first two lines don't have transaction data.
        br.readLine();

        String strNextLine="";
        while((strNextLine=br.readLine())!=null) {
          intSeparatorIndex=strNextLine.indexOf(",");

          String strNumberOfHotDogs=strNextLine.substring(0, intSeparatorIndex);

          String strMilliseconds=strNextLine.substring(intSeparatorIndex+1);

          Date datePurchased=new Date(Long.valueOf(strMilliseconds).longValue());

          String strTransaction=strNumberOfHotDogs+","+String.valueOf(datePurchased);

          hFrame.txtStatus.append(strTransaction+"\n");
        }

        br.close();
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  HotDogMilliseconds(double dblCostFlat, double dblCostPer, double dblRevenuePer, String strLogFilePath) {
    super("Hot Dog Shop");
    this.dblCostFlat=dblCostFlat;
    this.dblCostPer=dblCostPer;
    this.dblRevenuePer=dblRevenuePer;
    this.dblProfitPer=dblRevenuePer-dblCostPer;
    this.strLogFilePath=strLogFilePath;

    setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    Panel tempPan=new Panel();
    tempPan.setLayout(new GridLayout(7, 1));
    Panel tempPanA=new Panel();
    tempPanA.setLayout(new GridLayout(1, 2));
    tempPanA.add(new Label("Number of Hot Dogs Sold:"));
    tempPanA.add(txtNumberOfHotDogs);
    tempPan.add(tempPanA);
    Panel tempPanB=new Panel();
    tempPanB.setLayout(new GridLayout(1, 2));
    tempPanB.add(new Label("Year:"));
    tempPanB.add(txtYear);
    tempPan.add(tempPanB);
    Panel tempPanC=new Panel();
    tempPanC.setLayout(new GridLayout(1, 2));
    tempPanC.add(new Label("Month(0-11):"));
    tempPanC.add(txtMonth);
    tempPan.add(tempPanC);
    Panel tempPanD=new Panel();
    tempPanD.setLayout(new GridLayout(1, 2));
    tempPanD.add(new Label("Day(1-31):"));
    tempPanD.add(txtDate);
    tempPan.add(tempPanD);
    Panel tempPanE=new Panel();
    tempPanE.setLayout(new GridLayout(1, 2));
    tempPanE.add(new Label("Hour(0-23):"));
    tempPanE.add(txtHour);
    tempPan.add(tempPanE);
    Panel tempPanF=new Panel();
    tempPanF.add(btnSold);
    btnSold.addActionListener(this);
    tempPan.add(tempPanF);
    tempPan.add(new Label("Status:"));

    add("North", tempPan);

    add("Center", txtStatus);

    Panel tempPan2=new Panel();
    tempPan2.add(btnStatistics);
    btnStatistics.addActionListener(this);

    add("South", tempPan2);
  }

  public void actionPerformed(ActionEvent ae) {
    Object evSource=ae.getSource();

    if(evSource==btnSold) {
      String strNumberOfHotDogs=txtNumberOfHotDogs.getText();
      if(strNumberOfHotDogs.length()==0) {
        txtNumberOfHotDogs.setText("Number required.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex) {
        }
        txtNumberOfHotDogs.setText("");

        return;
      }

      int intNumberOfHotDogs=-1;
      try {
        intNumberOfHotDogs=Integer.parseInt(strNumberOfHotDogs);
      }
      catch(Exception ex) {
        txtNumberOfHotDogs.setText("Digits only required.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex2) {
        }
        txtNumberOfHotDogs.setText(strNumberOfHotDogs);

        return;
      }

      if(intNumberOfHotDogs<1 || intNumberOfHotDogs>20) {
        txtNumberOfHotDogs.setText("Number must be between 1-20.");
        try {
          Thread.sleep(3000);
        }
        catch(Exception ex2) {
        }
        txtNumberOfHotDogs.setText(strNumberOfHotDogs);

        return;
      }

      Date dateNow=new Date();

      int intYear=-1;
      String strYear=txtYear.getText();
      if(strYear.length()==0) {
        intYear=dateNow.getYear()+1900;
      }
      else {
        try {
          intYear=Integer.parseInt(strYear);
        }
        catch(Exception ex) {
          txtYear.setText("Digits only required.");
          try {
            Thread.sleep(3000);
          }
          catch(Exception ex2) {
          }
          txtYear.setText(strYear);

          return;
        }
      }


      int intMonth=-1;
      String strMonth=txtMonth.getText();
      if(strMonth.length()==0) {
        intMonth=dateNow.getMonth();
      }
      else {
        try {
          intMonth=Integer.parseInt(strMonth);
        }
        catch(Exception ex) {
          txtMonth.setText("Digits only required.");
          try {
            Thread.sleep(3000);
          }
          catch(Exception ex2) {
          }
          txtMonth.setText(strMonth);

          return;
        }

        if(intMonth<0 || intMonth>11) {
          txtMonth.setText("Number must be between 0-11.");
          try {
            Thread.sleep(3000);
          }
          catch(Exception ex2) {
          }
          txtMonth.setText(strMonth);

          return;
        }
      }


      int intDate=-1;
      String strDate=txtDate.getText();
      if(strDate.length()==0) {
        intDate=dateNow.getDate();
      }
      else {
        try {
          intDate=Integer.parseInt(strDate);
        }
        catch(Exception ex) {
          txtDate.setText("Digits only required.");
          try {
            Thread.sleep(3000);
          }
          catch(Exception ex2) {
          }
          txtDate.setText(strDate);

          return;
        }

        if(intDate<1 || intDate>31) {
          txtDate.setText("Number must be between 1-31.");
          try {
            Thread.sleep(3000);
          }
          catch(Exception ex2) {
          }
          txtDate.setText(strDate);

          return;
        }
      }


      int intHour=-1;
      String strHour=txtHour.getText();
      if(strHour.length()==0) {
        intHour=dateNow.getHours();
      }
      else {
        try {
          intHour=Integer.parseInt(strHour);
        }
        catch(Exception ex) {
          txtHour.setText("Digits only required.");
          try {
            Thread.sleep(3000);
          }
          catch(Exception ex2) {
          }
          txtHour.setText(strHour);

          return;
        }

        if(intHour<0 || intHour>23) {
          txtHour.setText("Number must be between 0-23.");
          try {
            Thread.sleep(3000);
          }
          catch(Exception ex2) {
          }
          txtHour.setText(strHour);

          return;
        }
      }

      Date datePurchased=new Date(intYear-1900, intMonth, intDate, intHour, 0);

      try {
        File fileLog=new File(strLogFilePath);
        if(!fileLog.exists()) {
          fileLog.createNewFile();

          RandomAccessFile raf=new RandomAccessFile(fileLog, "rw");

          raf.writeBytes(String.valueOf(dblCostFlat));
          raf.writeBytes(",");
          raf.writeBytes(String.valueOf(dblCostPer));
          raf.writeBytes(",");
          raf.writeBytes(String.valueOf(dblRevenuePer));

          raf.write(13);
          raf.write(10);
          raf.write(13);
          raf.write(10);

          raf.close();
        }

        RandomAccessFile raf=new RandomAccessFile(fileLog, "rw");
        raf.seek(fileLog.length());

        String strTransaction=strNumberOfHotDogs+","+String.valueOf(datePurchased.getTime());

        raf.writeBytes(strTransaction);
        raf.write(13);
        raf.write(10);

        raf.close();

        strTransaction=strNumberOfHotDogs+","+String.valueOf(datePurchased);

        txtStatus.append(strTransaction+"\n");
      }
      catch(Exception ex) {
        txtStatus.append(String.valueOf(ex)+"\n");
      }
    }
    else if(evSource==btnStatistics) {
      File fileLog=new File(strLogFilePath);
      if(!fileLog.exists()) {
        return;
      }

      try {
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(fileLog)));

//Skip over the first two lines in the file because those lines don't have transaction data.
        br.readLine();
        br.readLine();

        int intSeparatorIndex=-1;
        int intNumberOfHotDogs=-1;
        double dblNumberOfHotDogsAccumulated=0.0d;
        int intNumberOfHotDogsHours[]=new int[24];
        for(int i=0;i<intNumberOfHotDogsHours.length;i++)
          intNumberOfHotDogsHours[i]=0;

        String strNextLine="";
        while((strNextLine=br.readLine())!=null) {
          intSeparatorIndex=strNextLine.indexOf(",");
          String strNumberOfHotDogs=strNextLine.substring(0, intSeparatorIndex);
          String strMilliseconds=strNextLine.substring(intSeparatorIndex+1);

          intNumberOfHotDogs=Integer.parseInt(strNumberOfHotDogs);
          dblNumberOfHotDogsAccumulated+=((double)intNumberOfHotDogs);

          Date dateNext=new Date(Long.valueOf(strMilliseconds).longValue());

          intNumberOfHotDogsHours[dateNext.getHours()]+=intNumberOfHotDogs;
        }

        br.close();


        String strNumberSold="Number of Hot Dogs Sold: "+String.valueOf(((int)Math.rint(dblNumberOfHotDogsAccumulated)));
        double dblProfit=dblProfitPer*dblNumberOfHotDogsAccumulated-dblCostFlat;

        dblProfit*=100.0d;
        dblProfit=Math.floor(dblProfit);
        dblProfit/=100.0d;

        String strProfit="Profit: $"+String.valueOf(dblProfit);

        String strNumberOfHotDogsHours[]=new String[24];
        for(int i=0;i<strNumberOfHotDogsHours.length;i++)
          strNumberOfHotDogsHours[i]=String.valueOf(intNumberOfHotDogsHours[i]);

        StatisticsDisplayerDialog sDialog=new StatisticsDisplayerDialog(this, strNumberSold, strProfit, strNumberOfHotDogsHours);
        sDialog.show();
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  class StatisticsDisplayerDialog extends Dialog
  implements ActionListener {
    Button btnClose=new Button("Close");

    StatisticsDisplayerDialog(Frame parent, String strNumberSold, String strProfit, String strNumberOfHotDogsHours[]) {
      super(parent, "Statistics Displayer Dialog", true);

      Panel tempPan=new Panel();
      tempPan.setLayout(new GridLayout(27, 1));
      tempPan.add(new Label(strNumberSold));
      tempPan.add(new Label(strProfit));
      tempPan.add(new Label("Number Sold in Each Hour:"));
      for(int i=0;i<strNumberOfHotDogsHours.length;i++) {
        tempPan.add(new Label(String.valueOf(i)+": "+strNumberOfHotDogsHours[i]));
      }

      add("North", tempPan);

      add("Center", new Label(""));

      Panel tempPan2=new Panel();
      tempPan2.add(btnClose);
      btnClose.addActionListener(this);

      add("South", tempPan2);

      Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(dimScreen.width/4, 0);
      setSize(dimScreen.width/2, dimScreen.height-40);
    }

    public void actionPerformed(ActionEvent ae) {
      Object evSource=ae.getSource();

      if(evSource==btnClose) {
        dispose();
      }
    }
  }
}