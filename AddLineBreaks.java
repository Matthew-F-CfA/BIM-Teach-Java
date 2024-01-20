class AddLineBreaks {

  public static void main(String args[]) {
    if(args.length==0) {
      System.out.println("Usage:");
      System.out.println("  java AddLineBreaks <text here>");

      return;
    }

    String strWithoutLineBreaks="";
    for(int i=0;i<args.length;i++)
      strWithoutLineBreaks=strWithoutLineBreaks+args[i]+" ";

    String strWithLineBreaks=addLineBreaks(strWithoutLineBreaks, 50);

    System.out.println(strWithLineBreaks);
  }

  public static String addLineBreaks(String messageStr, int intLineLength) {
    String retStr="";
    int intIndex=-1;
    int intIndex2=-1;
    while(messageStr.length()>0) {
      intIndex=messageStr.indexOf("\n");
      if(intIndex==-1) {
        if(messageStr.length()>intLineLength) {
          for(intIndex2=intLineLength;intIndex2>=0;intIndex2--) {
            char nextChar=messageStr.charAt(intIndex2);
            if(nextChar==' ') {

              break;
            }
          }
          if(intIndex2==0 || intIndex2==-1) {
            retStr+=messageStr.substring(0, intLineLength)+"-\n";
            messageStr=messageStr.substring(intLineLength);
          }
          else {
            retStr+=messageStr.substring(0, intIndex2)+"\n";
            messageStr=messageStr.substring(intIndex2+1);
          }
        }
        else {
          retStr+=messageStr;
          break;
        }
      }
      else {
        if(intIndex<=intLineLength) {
          retStr+=messageStr.substring(0, intIndex)+"\n";
          messageStr=messageStr.substring(intIndex+new String("\n").length());
        }
        else {
          for(intIndex2=intLineLength;intIndex2>=0;intIndex2--) {
            char nextChar=messageStr.charAt(intIndex2);
            if(nextChar==' ') {
              break;
            }
          }
          if(intIndex2==0 || intIndex2==-1) {
            retStr+=messageStr.substring(0, intLineLength)+"-\n";
            messageStr=messageStr.substring(intLineLength);
          }
          else {
            retStr+=messageStr.substring(0, intIndex2)+"\n";
            messageStr=messageStr.substring(intIndex2+1);
          }
        }
      }
    }
    return retStr;
  }
}