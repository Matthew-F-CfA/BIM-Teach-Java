class BankClient {
  static double MAX_BALANCE=200000.0d;

  private String strName;
  private String strSocialSecurityNumber;
  private double dblBankBalance=0.0d;

  public static void main(String args[]) {
    if(args.length==0) {
      System.out.println("Usage:");
      System.out.println("  java BankClient <client name> <client social security number> <bank balance>");

      return;
    }

    String strName=args[0];
    String strSocialSecurityNumber=args[1];
    double dblBankBalance=Double.valueOf(args[2]).doubleValue();

    BankClient udtBankClient=new BankClient(strName, strSocialSecurityNumber, dblBankBalance);

    System.out.println(udtBankClient.getName());
    System.out.println(udtBankClient.getSocialSecurityNumber());
    System.out.println(udtBankClient.getBankBalance());
  }

  BankClient(String strName, String strSocialSecurityNumber, double dblBankBalance) {
    this.strName=strName;
    this.strSocialSecurityNumber=strSocialSecurityNumber;
    this.dblBankBalance=dblBankBalance;
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
  }
}