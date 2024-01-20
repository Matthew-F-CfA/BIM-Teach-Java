class FunctionCall {

  public static void main(String args[]) {
    System.out.println("1");
    System.out.println("2");
    System.out.println("3");

    callingStaticFunction();

    System.out.println("7");
    System.out.println("8");
    System.out.println("9");
  }

  public static void callingStaticFunction() {
    System.out.println("4");
    System.out.println("5");
    System.out.println("6");
  }
}