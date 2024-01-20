class RintCutoff {

  public static void main(String args[]) {
    try {
      double dblDouble=0.5d;
      dblDouble=Math.rint(dblDouble);
      System.out.println(dblDouble);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}