import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class MultiplicationTable {
  static TreeMap mapMultiplication=new TreeMap();

  static {
    for(int i=0;i<10;i++) {
      for(int ia=0;ia<10;ia++) {
        mapMultiplication.put(new TwoDimensionalKey(i, ia), new Integer(i*ia));
      }
    }
  }

  public static void main(String args[]) {
    try {
      BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

      do {
        System.out.println("Multiplication Table from 0-9");

        System.out.println("Enter the first number");

        String strFirst=br.readLine();
        int intFirst=Integer.parseInt(strFirst);
        if(intFirst<0) {
          System.out.println("Number is too low.\n");

          continue;
        }
        else if(intFirst>9) {
          System.out.println("Number is too high.\n");

          continue;
        }

        System.out.println("Enter the second number");

        String strSecond=br.readLine();
        int intSecond=Integer.parseInt(strSecond);
        if(intSecond<0) {
          System.out.println("Number is too low.\n");

          continue;
        }
        else if(intSecond>9) {
          System.out.println("Number is too high.\n");

          continue;
        }

        String strOut=String.valueOf(mapMultiplication.get(new TwoDimensionalKey(intFirst, intSecond)));

        System.out.println("\nThe product is "+strOut+"\n");

      } while(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}