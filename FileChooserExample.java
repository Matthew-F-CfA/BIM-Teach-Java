import java.io.File;
import java.awt.Frame;
import javax.swing.JFileChooser;

class FileChooserExample {

  public static void main(String args[]) {
    try {
      if(args.length==0) {
        System.out.println("Usage:");
        System.out.println("  java FileChooserExample <load or save>");

        return;
      }

      if(args[0].equals("load")) {
        JFileChooser fDialog=new JFileChooser();
        fDialog.setCurrentDirectory(new File("."));
        fDialog.setDialogTitle("Load File Dialog");
        fDialog.setMultiSelectionEnabled(false);
        fDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int intResp=fDialog.showOpenDialog(new Frame());

        if(intResp!=JFileChooser.APPROVE_OPTION)
          System.exit(0);

        File fileLoad=fDialog.getSelectedFile();

        System.out.println("Absolute Path: "+fileLoad.getAbsolutePath());

        System.exit(0);
      }
      else if(args[0].equals("save")) {
        JFileChooser fDialog=new JFileChooser();
        fDialog.setCurrentDirectory(new File("."));
        fDialog.setDialogTitle("Save File Dialog");
        fDialog.setMultiSelectionEnabled(false);
        fDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int intResp=fDialog.showSaveDialog(new Frame());

        if(intResp!=JFileChooser.APPROVE_OPTION)
          System.exit(0);

        File fileSave=fDialog.getSelectedFile();

        System.out.println("Absolute Path: "+fileSave.getAbsolutePath());

        System.exit(0);
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}