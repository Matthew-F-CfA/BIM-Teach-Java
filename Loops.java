class Loops {

  public static void main(String args[]) {
    for(int i=0;i<3;i++) {
      System.out.println(i);
    }

    int i=0;
    while(i<3) {
      System.out.println(i);

      ++i;
    }

    i=0;
    do {
      System.out.println(i);

      ++i;
    } while(i<3);
  }
}