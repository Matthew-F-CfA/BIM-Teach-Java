import java.io.*;

class MealChooser {
  static int HAM_AND_BEANS=0;
  static int PASTA_WITH_MEAT_SAUCE=1;
  static int TACOS=2;
  static int STEAK_AND_CHEESE_GRINDER=3;
  static int DOUBLE_CHEESE_BURGER=4;
  static int CAESAR_SALAD=5;

  static String MEAL_STRINGS[]=new String[0];

  static {
    MEAL_STRINGS=new String[6];

    MEAL_STRINGS[HAM_AND_BEANS]="Ham and Beans";
    MEAL_STRINGS[PASTA_WITH_MEAT_SAUCE]="Pasta with Meat Sauce";
    MEAL_STRINGS[TACOS]="Tacos";
    MEAL_STRINGS[STEAK_AND_CHEESE_GRINDER]="Steak and Cheese Grinder";
    MEAL_STRINGS[DOUBLE_CHEESE_BURGER]="Double Cheese Burger";
    MEAL_STRINGS[CAESAR_SALAD]="Caesar Salad";
  }

  public static void main(String args[]) {
    try {
      System.out.println("To use our menu, enter the number corresponding to the meal you would like to order.");

      BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

      do {
        System.out.println("Menu:");
        for(int i=0;i<MEAL_STRINGS.length;i++) {
          System.out.println(i+"> "+MEAL_STRINGS[i]);
        }

        String strNextChoose=br.readLine();

        int intNextChoose=-1;
        try {
          intNextChoose=Integer.parseInt(strNextChoose);
        }
        catch(Exception ex) {
          System.out.println("Unrecognized choice: "+strNextChoose+"\n");

          continue;
        }

        if(intNextChoose<0 || intNextChoose>=MEAL_STRINGS.length) {
          System.out.println("Unrecognized choice: "+strNextChoose+"\n");

          continue;
        }

        System.out.println("You chose to order "+MEAL_STRINGS[intNextChoose]+"\n");

        writeToLogFile(MEAL_STRINGS[intNextChoose]);

      } while(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void writeToLogFile(String strMessage) throws Exception {
    if(!(new File("log").exists())) {
      new File("log").mkdirs();
    }
    String strFileSep=System.getProperty("file.separator");
    String strFilePath="log"+strFileSep+"LogMeals.txt";
    File fileLog=new File(strFilePath);
    RandomAccessFile raf=new RandomAccessFile(fileLog, "rw");
    raf.seek(fileLog.length());
    raf.writeBytes(strMessage);
    raf.write(13);
    raf.write(10);
    raf.close();
  }
}