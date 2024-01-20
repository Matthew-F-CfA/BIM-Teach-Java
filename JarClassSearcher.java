import java.util.*;
import java.util.jar.*;
import java.io.*;

class JarClassSearcher {
  static String strSearchClass="";

  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java JarClassSearcher <folder to search in> <class to search for; don't include the extension>");

        return;
      }

      String strBaseDir=args[0];
      strSearchClass=args[1]+".class";

      File fileDir=new File(strBaseDir);

      if(fileDir.isDirectory()) {
        checkDirectory(fileDir);
      }
      else {
        if(fileDir.getName().endsWith(".jar"))
          checkJarFile(fileDir);
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void checkDirectory(File fileDir) throws Exception {
    File fileList[]=fileDir.listFiles();
    for(int i=0;i<fileList.length;i++) {
      if(fileList[i].isDirectory()) {
        checkDirectory(fileList[i]);
      }
      else {
        if(fileList[i].getName().endsWith(".jar"))
          checkJarFile(fileList[i]);
      }
    }
  }

  public static void checkJarFile(File fileJar) throws Exception {
    JarFile jFile=new JarFile(fileJar);

    Enumeration enum0=jFile.entries();

    while(enum0.hasMoreElements()) {
      JarEntry jEntry=(JarEntry)enum0.nextElement();

      if(jEntry.isDirectory())
        continue;

      String strNextName=jEntry.getName();

      if(strNextName.endsWith(strSearchClass)) {
        System.out.println(fileJar.getName());
        System.out.println(strNextName);
      }
    }
  }
}