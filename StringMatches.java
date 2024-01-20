class StringMatches {

  public static void main(String args[]) {
    if(args.length==0) {
      System.out.println("Usage:");
      System.out.println("  java StringMatches <String> \"<String with Wildcards>\"");
      System.out.println("For example:");
      System.out.println("  java StringMatches abcde \"a*e\"");

      return;
    }

    System.out.println(stringWithWildCardsMatches(args[0], args[1]));
  }

//Return true if strStr matches the wildcard String strWildCards
  public static boolean stringWithWildCardsMatches(String strStr, String strWildCards) {
    boolean blnMatchFound=true;

    try {
      int intIndex=-1;
      int intIndex2=-1;
      int intIndex3=-1;
      int intCurrentStart=0;
      if(!strWildCards.startsWith("*")) {
        intIndex=strWildCards.indexOf('*', intIndex+1);
        if(intIndex==-1) {
          if(!strWildCards.equals(strStr))
            blnMatchFound=false;
        }
        else {
          String strNextPiece=strWildCards.substring(0, intIndex);
          if(strStr.length()<intIndex) {
            blnMatchFound=false;
          }
          else {
            String strNextPiece2=strStr.substring(0, intIndex);
            intCurrentStart=intIndex;
            if(!strNextPiece.equals(strNextPiece2)) {
              blnMatchFound=false;
            }
          }
        }
      }
      else
        intIndex=0;
      if(blnMatchFound) {
        while(intIndex!=-1) {
          intIndex2=strWildCards.indexOf('*', intIndex+1);

          if(intIndex2==-1) {
            if((intIndex+1)!=strWildCards.length()) {
              String strNextPiece=strWildCards.substring(intIndex+1);
              String strNextPiece2=strStr.substring(intCurrentStart);
              intIndex3=strNextPiece2.indexOf(strNextPiece);
              if(intIndex3!=(strNextPiece2.length()-strNextPiece.length()))
                blnMatchFound=false;
            }
            break;
          }
          else {
            String strNextPiece=strWildCards.substring(intIndex+1, intIndex2);
            String strNextPiece2=strStr.substring(intCurrentStart);
            intIndex3=strNextPiece2.indexOf(strNextPiece);
            if(intIndex3==-1) {
              blnMatchFound=false;
              break;
            }
            intCurrentStart=intIndex3+strNextPiece.length();
            intIndex=intIndex2;
          }
        }
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }

    return blnMatchFound;
  }
}