class EqualityOperator {

  public static void main(String args[]) {
    String strOne=new String("Test");
    String strTwo=new String("Test");

    if(strOne==strTwo) {
      System.out.println("strOne equals strTwo");
    }
    else {
      System.out.println("strOne doesn't equal strTwo");
    }

    if(strOne.equals(strTwo)) {
      System.out.println("strOne equals strTwo");
    }
    else {
      System.out.println("strOne doesn't equal strTwo");
    }

    strTwo=strOne;

    if(strOne==strTwo) {
      System.out.println("strOne equals strTwo");
    }
    else {
      System.out.println("strOne doesn't equal strTwo");
    }
  }
}