/*
Simulates rolling dice depending on how many dice are included on the command line as arguments.
Prints out each individual dice result and prints out the sum of all the dice rolls.
*/

class MathDice {

  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java MathDice <# of dice sides for 1st> <# of dice sides for 2nd> ...");

        return;
      }

      int intSum=0;

      for(int i=0;i<args.length;i++) {
        double dblNumberOfSides=Double.valueOf(args[i]).doubleValue();

        if(dblNumberOfSides<1.0d)
          throw new Exception("Invalid number of sides.");

        dblNumberOfSides=dblNumberOfSides-1.0d;

        double dblRoll=Math.random()*dblNumberOfSides;
        dblRoll=Math.rint(dblRoll);
        dblRoll=dblRoll+1.0d;

        int intRoll=(int)dblRoll;

        intSum=intSum+intRoll;

        System.out.println("Individual roll #"+i+": "+intRoll);
      }

      System.out.println("\nSum of all dice: "+intSum);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}