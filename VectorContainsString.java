import java.util.Vector;
import java.util.Hashtable;

class VectorContainsString {
  String strStr="";

  public static void main(String args[]) {
    VectorContainsString vcs=new VectorContainsString("Testing");

    Vector vecMy=new Vector();
    vecMy.addElement(vcs);

System.out.println("before vecMy contains call");
    if(vecMy.contains(vcs)) {
      System.out.println("Does contain.");
    }
    else {
      System.out.println("Doesn't contain.");
    }
System.out.println("after vecMy contains call\n");


    Hashtable hshMy=new Hashtable();
    hshMy.put(vcs, "Testing too");

System.out.println("before hshMy containsKey call");
    if(hshMy.containsKey(vcs)) {
      System.out.println("Does contain.");
    }
    else {
      System.out.println("Doesn't contain.");
    }
System.out.println("after hshMy contains call");
  }

  VectorContainsString(String strStr) {
    this.strStr=strStr;
  }

  public boolean equals(Object objObj) {
System.out.println("equals");
    if(objObj instanceof VectorContainsString) {
      VectorContainsString vcs=(VectorContainsString)objObj;

      if(vcs.strStr.equals(strStr))
        return true;
      else
        return false;
    }

    return false;
  }

  public int hashCode() {
System.out.println("hashCode");
    return strStr.hashCode();
  }
}