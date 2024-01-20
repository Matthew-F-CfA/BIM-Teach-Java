class AndConditionWithString {

  public static void main(String args[]) {
    if(args.length==1 && args[0].equals("John")) {
      System.out.println("args.length is 1 and args[0] is the String \"John\".");
    }
    else {
      System.out.println("Either args.length isn't 1 or args[0] isn't the String \"John\".");
    }


    String strName=null;
    if(strName!=null && strName.equals("Joe")) {
      System.out.println("strName equals \"Joe\".");
    }
    else {
      System.out.println("strName doesn't equals \"Joe\".");
    }

    strName="Joe";
    if(strName!=null && strName.equals("Joe")) {
      System.out.println("strName equals \"Joe\".");
    }
    else {
      System.out.println("strName doesn't equals \"Joe\".");
    }
  }
}