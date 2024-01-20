import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HashtableClone {

  public HashtableClone() {
  }

  public static Hashtable cloneH(Hashtable hashToClone) {
    Hashtable hClone=new Hashtable();

    Iterator iter0=hashToClone.entrySet().iterator();

    while(iter0.hasNext()) {
      Map.Entry mEntry=(Map.Entry)iter0.next();

      Object orig=mEntry.getKey();

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


      orig=mEntry.getValue();

      Object obj2=null;
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
        obj2=in.readObject();
        in.close();
      }
      catch(IOException e) {
        e.printStackTrace();
      }
      catch(ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
      }

      hClone.put(obj, obj2);

    }

    return hClone;
  }
}
