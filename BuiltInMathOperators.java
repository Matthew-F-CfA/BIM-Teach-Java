class BuiltInMathOperators {

  public static void main(String args[]) {
/*
This text is inside of a multiline Java comment. Any text in between the comment indicators isn't processed by the compiler.
The purpose of this text is to aid the programmer in understanding the code.
For example, this comment can be used to remind the programmer that the next "if" condition determines if the Usage specifier
is displayed to the user.
*/
    if(args.length==0) {
      System.out.println("Usage:");
      System.out.println("  java BuiltInMathOperators <operation> <first number> <second number>\n");
      System.out.println("<operation> must be one of addition, subtraction, multiplication, division, remainder, exponent");

      return;
    }

    String strOperation=args[0];
    String strOperand=args[1];
    String strOperand0=args[2];

    double dblOperand=Double.valueOf(strOperand).doubleValue();
    double dblOperand0=Double.valueOf(strOperand0).doubleValue();

    double dblOperated=0.0d;

//This text is a single line comment. Any text appearing after the indicator doesn't get processed by the compiler.
//The purpose of this text is to aid the programmer in understanding the code.

//The next "if" condition checks if the operation to perform is "addition".
    if(strOperation.equals("addition")) {
      dblOperated=dblOperand+dblOperand0;
    }
    else if(strOperation.equals("subtraction")) {
      dblOperated=dblOperand-dblOperand0;
    }
    else if(strOperation.equals("multiplication")) {
      dblOperated=dblOperand*dblOperand0;
    }
    else if(strOperation.equals("division")) {
      dblOperated=dblOperand/dblOperand0;
    }
    else if(strOperation.equals("remainder")) {
      dblOperated=dblOperand%dblOperand0;
    }
    else if(strOperation.equals("exponent")) {
      dblOperated=Math.pow(dblOperand, dblOperand0);
    }
    else {
      System.out.println("Unrecognized operation.");
      return;
    }

    System.out.println("The numbers computed to a result of "+dblOperated);
  }
}