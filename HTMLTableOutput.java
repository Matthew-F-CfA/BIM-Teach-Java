import java.io.*;
import java.util.Vector;

class HTMLTableOutput {

  public static void main(String args[]) {
    try {
      HTMLTableOutput hTO=new HTMLTableOutput();

      HTMLTableOutput.ContactPerson cpObj=hTO.new ContactPerson("John", "012-345-6789", "john@fiction.org");
      HTMLTableOutput.ContactPerson cpObj2=hTO.new ContactPerson("Joe", "987-654-3210", "joe@fiction.org");
      HTMLTableOutput.ContactPerson cpObj3=hTO.new ContactPerson("Mary", "121-212-1212", "mary@fiction.org");

      Vector vecContacts=new Vector();

      vecContacts.addElement(cpObj);
      vecContacts.addElement(cpObj2);
      vecContacts.addElement(cpObj3);


      File newFile=null;

      int intCountFile=0;

//This loop makes sure that we don't overwrite an existing file.
      while(true) {
        newFile=new File("HTMLTableOutput"+intCountFile+".html");
        if(newFile.exists()) {
          ++intCountFile;
          continue;
        }

        break;
      }


      RandomAccessFile raf=new RandomAccessFile(newFile, "rw");

      raf.writeBytes("<html>");
      raf.write(13);
      raf.write(10);
      raf.writeBytes("<body>");
      raf.write(13);
      raf.write(10);

//Make the table have 3 columns and width be half the window size
      raf.writeBytes("<table cols='3' border='1' width='50%'>");
      raf.write(13);
      raf.write(10);


//Set the first column's width to 40% of the table width
      raf.writeBytes("<col style='width:40%'>");
      raf.write(13);
      raf.write(10);

//Set the second column's width to 30% of the table width
      raf.writeBytes("<col style='width:30%'>");
      raf.write(13);
      raf.write(10);

//Set the third column's width to 30% of the table width
      raf.writeBytes("<col style='width:30%'>");
      raf.write(13);
      raf.write(10);


//Output the table headers for each column
      raf.writeBytes("<tr><th>Name</th><th>Phone</th><th>Email</th></tr>");
      raf.write(13);
      raf.write(10);

      for(int i=0;i<vecContacts.size();i++) {
        HTMLTableOutput.ContactPerson nextContact=(HTMLTableOutput.ContactPerson)vecContacts.elementAt(i);

        raf.writeBytes("<tr>");

//Output the data for the first cell
        raf.writeBytes("<td>");
        raf.writeBytes(nextContact.getName());
        raf.writeBytes("</td>");

//Output the data for the second cell
        raf.writeBytes("<td>");
        raf.writeBytes(nextContact.getPhone());
        raf.writeBytes("</td>");

//Output the data for the third cell
        raf.writeBytes("<td>");
        raf.writeBytes(nextContact.getEmail());
        raf.writeBytes("</td>");

        raf.writeBytes("</tr>");

        raf.write(13);
        raf.write(10);
      }

      raf.writeBytes("</table>");
      raf.write(13);
      raf.write(10);

      raf.writeBytes("</body>");
      raf.write(13);
      raf.write(10);
      raf.writeBytes("</html>");
      raf.write(13);
      raf.write(10);

      raf.close();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  class ContactPerson {
    String strName;
    String strPhone;
    String strEmail;

    ContactPerson(String strName, String strPhone, String strEmail) {
      this.strName=strName;
      this.strPhone=strPhone;
      this.strEmail=strEmail;
    }

    public String getName() {
      return strName;
    }

    public String getPhone() {
      return strPhone;
    }

    public String getEmail() {
      return strEmail;
    }
  }
}