import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;

class BankClientSerialization
implements Serializable {
  static double MAX_BALANCE=200000.0d;

  private String strName;
  private String strSocialSecurityNumber;
  private Double dblBankBalanceO=new Double(0.0d);
  private transient double dblBankBalance=0.0d;

  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java BankClientSerialization <either \"input\" or \"output\">");
        System.out.println("For example:");
        System.out.println("  java BankClientSerialization output");

        return;
      }

      if(args[0].equals("output")) {
        String strName="John Smith";
        String strSocialSecurityNumber="012345678";
        double dblBankBalance=100.0d;

        BankClientSerialization udtBankClient=new BankClientSerialization(strName, strSocialSecurityNumber, dblBankBalance);

        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(new File("bankClientSerialization")));
        oos.writeObject(udtBankClient);
        oos.close();
      }
      else if(args[0].equals("input")) {
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(new File("bankClientSerialization")));
        BankClientSerialization udtBankClient=(BankClientSerialization)ois.readObject();
        ois.close();

        System.out.println(udtBankClient.getName());
        System.out.println(udtBankClient.getSocialSecurityNumber());
        System.out.println(udtBankClient.getBankBalance());
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  BankClientSerialization(String strName, String strSocialSecurityNumber, double dblBankBalance) {
    this.strName=strName;
    this.strSocialSecurityNumber=strSocialSecurityNumber;
    this.dblBankBalance=dblBankBalance;
    this.dblBankBalanceO=new Double(dblBankBalance);
  }

  public String getName() {
    return strName;
  }

  public void setName(String strName) {
    this.strName=strName;
  }

  public String getSocialSecurityNumber() {
    return strSocialSecurityNumber;
  }

  public void setSocialSecurityNumber(String strSocialSecurityNumber) {
    this.strSocialSecurityNumber=strSocialSecurityNumber;
  }

  public double getBankBalance() {
    return dblBankBalance;
  }

  public void setBankBalance(double dblBankBalance) {
    this.dblBankBalance=dblBankBalance;
    this.dblBankBalanceO=new Double(dblBankBalance);
  }

  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    initializeTransients();
  }

  public void initializeTransients() {
    dblBankBalance=dblBankBalanceO.doubleValue();
  }
}