class Loops2 {

  public static void main(String args[]) {
    for(int i=0;i<5;i++) {
      if(i==1)
        continue;
      else if(i==4)
        break;

      System.out.println(i);
    }
  }
}