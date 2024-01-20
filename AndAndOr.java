class AndAndOr {

  public static void main(String args[]) {
    if(args.length==0) {
      System.out.println("Usage:");
      System.out.println("  java AndAndOr <true or false> <true or false>");
      System.out.println("For example:");
      System.out.println("  java AndAndOr true true");

      return;
    }

    boolean blnFirst=Boolean.valueOf(args[0]).booleanValue();
    boolean blnSecond=Boolean.valueOf(args[1]).booleanValue();

    if(blnFirst && blnSecond) {
      System.out.println("blnFirst AND blnSecond evaluated to true.");
    }
    else {
      System.out.println("blnFirst AND blnSecond evaluated to false.");
    }

    if(blnFirst || blnSecond) {
      System.out.println("blnFirst OR blnSecond evaluated to true.");
    }
    else {
      System.out.println("blnFirst OR blnSecond evaluated to false.");
    }
  }
}