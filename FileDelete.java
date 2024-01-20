import java.io.File;

class FileDelete {

  public static void main(String args[]) {
    try {
      String strFilePathToDelete=System.getProperty("user.dir");
      String strFileSeparator=System.getProperty("file.separator");
      if(!strFilePathToDelete.endsWith(strFileSeparator))
        strFilePathToDelete+=strFileSeparator;

      strFilePathToDelete+="files"+strFileSeparator+"CopyMe.txt";

      File fileToDelete=new File(strFilePathToDelete);

      if(!fileToDelete.exists()) {
        System.out.println("File doesn't exist. Exiting...");

        return;
      }

      fileToDelete.delete();

      System.out.println("Deleted file.");
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}