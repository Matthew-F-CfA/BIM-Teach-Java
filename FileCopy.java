import java.io.File;
import java.awt.Frame;
import javax.swing.JFileChooser;
import java.nio.file.*;

class FileCopy {

  public static void main(String args[]) {
    try {
      JFileChooser fDialog=new JFileChooser();
      fDialog.setCurrentDirectory(new File("."));
      fDialog.setDialogTitle("File To Copy Dialog");
      fDialog.setMultiSelectionEnabled(false);
      fDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
      int intResp=fDialog.showOpenDialog(new Frame());

      if(intResp!=JFileChooser.APPROVE_OPTION)
        System.exit(0);

      File fileCopy=fDialog.getSelectedFile();

      if(!fileCopy.getName().equals("CopyMe.txt")) {
        System.out.println("Selected the wrong file. Exiting...");

        System.exit(0);
      }

      fDialog=new JFileChooser();
      fDialog.setCurrentDirectory(new File("."));
      fDialog.setDialogTitle("File To Copy Target Dialog");
      fDialog.setMultiSelectionEnabled(false);
      fDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
      intResp=fDialog.showSaveDialog(new Frame());

      if(intResp!=JFileChooser.APPROVE_OPTION)
        System.exit(0);

      File fileTarget=fDialog.getSelectedFile();

      File fileTargetParent=fileTarget.getParentFile();

      if(!fileTargetParent.getAbsolutePath().equals(new File("files").getAbsolutePath())) {
        System.out.println("Tried to copy to the wrong directory. Exiting...");

        System.exit(0);
      }


      Path pathCopySource=FileSystems.getDefault().getPath(fileCopy.getAbsolutePath());
      Path pathCopyTarget=FileSystems.getDefault().getPath(fileTarget.getAbsolutePath());

      Files.copy(pathCopySource, pathCopyTarget);

      System.exit(0);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}