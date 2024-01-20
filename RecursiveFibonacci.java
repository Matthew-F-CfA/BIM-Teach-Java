import java.io.*;
import java.util.Vector;

class RecursiveFibonacci {

  public static void main(String args[]) {
    try {
      Vector vecFibonacci=new Vector();
//Required for initializing the sequence with the first two indices that will be used for adding the numbers
      vecFibonacci.addElement(new Integer(1));
      vecFibonacci.addElement(new Integer(1));

      BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

      while(true) {
        System.out.println("Type in \"1\" if you want to find the nth Fibonacci number or type in \"2\" if you want to exit.\n");

        String strRequest=br.readLine();

        if(strRequest.equals("2"))
          break;

        System.out.println("");

        System.out.println("What index(starting with 1) of Fibonacci number do you want to return?\n");

        String strIndex=br.readLine();

        int intIndex=-1;
        try {
          intIndex=Integer.parseInt(strIndex);
        }
        catch(Exception ex) {
          continue;
        }

        if(intIndex<1)
          continue;

        System.out.println("");

        if(intIndex<=vecFibonacci.size()) {
          System.out.println("The "+String.valueOf(intIndex)+"th Fibonacci number is "+String.valueOf(vecFibonacci.elementAt(intIndex-1))+"\n");
        }
        else {
          int intIndicesLeftToCompute=intIndex-vecFibonacci.size();

          computeFibonacci(intIndicesLeftToCompute, vecFibonacci);

          System.out.println("The "+String.valueOf(intIndex)+"th Fibonacci number is "+String.valueOf(vecFibonacci.elementAt(vecFibonacci.size()-1))+"\n");
        }
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void computeFibonacci(int intIndicesLeftToCompute, Vector vecFibonacci) {
    int intOne=((Integer)vecFibonacci.elementAt(vecFibonacci.size()-2)).intValue();
    int intTwo=((Integer)vecFibonacci.elementAt(vecFibonacci.size()-1)).intValue();

    vecFibonacci.addElement(new Integer(intOne+intTwo));

    --intIndicesLeftToCompute;

    if(intIndicesLeftToCompute==0)
      return;

    computeFibonacci(intIndicesLeftToCompute, vecFibonacci);
  }
}