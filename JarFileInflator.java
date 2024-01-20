import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.net.URL;

class JarFileInflator {

  public static void main(String args[]) {
    try {
      String strJar=System.getProperty("sun.java.command");

      if(!strJar.endsWith(".jar")) {
        System.out.println("JAR file expected. Exiting...");

        return;
      }

      JarFile jFile=new JarFile(strJar);

      Enumeration enum0=jFile.entries();

      byte bbuf[]=new byte[10000];
      int inAvail=-1;

      File fileParent=new File(System.getProperty("user.dir"));
      URL urlParent=fileParent.toURI().toURL();
      if(!urlParent.toString().endsWith("/"))
        urlParent=new URL(urlParent.toString()+"/");

      while(enum0.hasMoreElements()) {
        JarEntry jEntry=(JarEntry)enum0.nextElement();

        URL urlAbsolute=new URL(urlParent.toString()+jEntry.getName());
        File fileAbsolute=new File(urlAbsolute.getFile());

        if(jEntry.isDirectory()) {
          fileAbsolute.mkdirs();
          continue;
        }

        File fileAbsoluteParent=fileAbsolute.getParentFile();
        fileAbsoluteParent.mkdirs();

        InputStream is=jFile.getInputStream(jEntry);
        BufferedInputStream bis=new BufferedInputStream(is);
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(fileAbsolute));
        while(true) {
          inAvail=bis.available();
          if(inAvail<=0) {
            int inInt=bis.read();
            if(inInt==-1)
              break;
            bos.write(inInt);
            continue;
          }
          if(inAvail>bbuf.length)
            inAvail=bbuf.length;
          bis.read(bbuf, 0, inAvail);
          bos.write(bbuf, 0, inAvail);
        }
        is.close();
        bis.close();
        bos.close();
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}