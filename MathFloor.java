/*
Randomly chooses the day of the week that pasta should be cooked on.
*/

class MathFloor {

  public static void main(String args[]) {
    try {
      String strDays[]={"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

      double dblRandom=Math.random()*((double)strDays.length);
      double dblFloor=Math.floor(dblRandom);

      int intFloor=(int)dblFloor;

      System.out.println("Cook pasta on "+strDays[intFloor]+".");
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}