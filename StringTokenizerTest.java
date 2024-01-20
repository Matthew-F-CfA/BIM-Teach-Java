import java.util.StringTokenizer;

class StringTokenizerTest {

  public static void main(String args[]) {
    StringTokenizer strTokenizer=new StringTokenizer(args[0], ":", false);

    while(strTokenizer.hasMoreTokens()) {
      System.out.println(strTokenizer.nextToken());
    }
  }
}