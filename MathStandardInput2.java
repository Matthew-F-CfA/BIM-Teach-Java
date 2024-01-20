import java.io.*;

class MathStandardInput2 {

  public static void main(String args[]) {
    try {
      BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

      String strNextOperation="";
      String strOperand="";
      String strOperand0="";

      double dblOperand=0.0d;
      double dblOperand0=0.0d;

      double dblOperated=0.0d;

      do {
        System.out.println("Type in \"addition\" or \"subtraction\" or \"multiplication\" or \"division\" or \"remainder\" or \"exponent\" or \"exit\"\n");

        strNextOperation=br.readLine();

        if(strNextOperation.equals("exit"))
          break;

        System.out.println("Type in first operand");

        strOperand=br.readLine();

        System.out.println("Type in second operand");

        strOperand0=br.readLine();

        try {
          dblOperand=Double.valueOf(strOperand).doubleValue();
          dblOperand0=Double.valueOf(strOperand0).doubleValue();
        }
        catch(Exception ex) {
          System.out.println("");
          continue;
        }

        if(strNextOperation.equals("addition")) {
          dblOperated=dblOperand+dblOperand0;
        }
        else if(strNextOperation.equals("subtraction")) {
          dblOperated=dblOperand-dblOperand0;
        }
        else if(strNextOperation.equals("multiplication")) {
          dblOperated=dblOperand*dblOperand0;
        }
        else if(strNextOperation.equals("division")) {
          dblOperated=dblOperand/dblOperand0;
        }
        else if(strNextOperation.equals("remainder")) {
          dblOperated=dblOperand%dblOperand0;
        }
        else if(strNextOperation.equals("exponent")) {
          dblOperated=Math.pow(dblOperand, dblOperand0);
        }
        else {
          System.out.println("\nUnrecognized operation.\n\n");
          continue;
        }

        System.out.println("\nThe numbers computed to a result of "+dblOperated+"\n\n");

      } while(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}