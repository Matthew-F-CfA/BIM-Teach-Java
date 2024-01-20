import java.io.*;

class FileTextSearcher {
  static String strText="";

  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java FileTextSearcher <directory to search in> <text to search for>");

        return;
      }

      String strDir=args[0];
      File fileDir=new File(strDir);

      strText=args[1];

      if(fileDir.isDirectory())
        searchDirectory(fileDir);
      else
        searchFile(fileDir);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  FileTextSearcher() {
  }

  public static void searchDirectory(File fileDir) throws Exception {
    File fileList[]=fileDir.listFiles();
    for(int i=0;i<fileList.length;i++) {
      if(fileList[i].isDirectory())
        searchDirectory(fileList[i]);
      else
        searchFile(fileList[i]);
    }
  }

  public static void searchFile(File fileFile) throws Exception {
    BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(fileFile)));
    String strLine="";
    int intCountLines=1;
    while((strLine=br.readLine())!=null) {
      if(strLine.indexOf(strText)!=-1) {
        System.out.println(fileFile.getAbsolutePath()+":"+String.valueOf(intCountLines));
        System.out.println(strLine+"\n");
      }
      ++intCountLines;
    }
    br.close();
  }
}
