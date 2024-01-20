import java.util.Vector;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class VectorClone {

  public VectorClone() {
  }

  public static Vector cloneV(Vector vecToClone) {
    Vector vClone=new Vector();

    for(int i=0;i<vecToClone.size();i++) {
      Object orig=vecToClone.elementAt(i);

      Object obj=null;
      try {
        // Write the object out to a byte array
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream out=new ObjectOutputStream(bos);
        out.writeObject(orig);
        out.flush();
        out.close();

        // Make an input stream from the byte array and read
        // a copy of the object back in.
        ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        obj=in.readObject();
        in.close();
      }
      catch(IOException e) {
        e.printStackTrace();
      }
      catch(ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
      }

      vClone.addElement(obj);
    }

    return vClone;
  }
}
