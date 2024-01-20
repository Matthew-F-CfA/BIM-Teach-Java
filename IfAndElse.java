class IfAndElse {

  public static void main(String args[]) {
    boolean blnTrueOrFalse=true;
    if(blnTrueOrFalse)
      System.out.println("0:true");
    else
      System.out.println("0:false");

    blnTrueOrFalse=false;
    if(blnTrueOrFalse)
      System.out.println("1:true");
    else
      System.out.println("1:false");

    if(!blnTrueOrFalse)
      System.out.println("2:false");
    else
      System.out.println("2:true");
  }
}