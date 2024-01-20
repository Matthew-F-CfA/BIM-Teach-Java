import java.util.Comparator;
import java.util.Vector;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class SortTelephoneNumbers {

  public static void main(String args[]) {
    try {
      SortTelephoneNumbers udtSTN=new SortTelephoneNumbers();

      BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

      Vector vecContacts=new Vector();

      do {
        System.out.println("Type in \"new\" if you want to enter a new contact or type in \"list\" if you want to see a list or type \"exit\" to exit the program.");

        String strNextOperation=br.readLine();

        if(strNextOperation.equals("new")) {
          System.out.println("Enter the contact's name:");

          String strName=br.readLine();

          System.out.println("Enter the contact's phone number:");

          String strTelephoneNumber=br.readLine();

          SortTelephoneNumbers.TelephoneContact nextContact=udtSTN.new TelephoneContact(strName, strTelephoneNumber);

          vecContacts.addElement(nextContact);

          System.out.println("");
        }
        else if(strNextOperation.equals("list")) {
          SortTelephoneNumbers.TelephoneContact contacts[]=new SortTelephoneNumbers.TelephoneContact[vecContacts.size()];
          for(int i=0;i<vecContacts.size();i++) {
            contacts[i]=(SortTelephoneNumbers.TelephoneContact)vecContacts.elementAt(i);
          }

          Arrays.sort(contacts, udtSTN.new TelephoneContactComparator());

          System.out.println("Contact List:");
          System.out.println("Format: <name>:<number>");
          for(int i=0;i<contacts.length;i++) {
            System.out.println(contacts[i].getName()+":"+contacts[i].getTelephoneNumber());
          }

          System.out.println("");
        }
        else if(strNextOperation.equals("exit")) {
          break;
        }

      } while(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  class TelephoneContact {
    String strName;
    String strTelephoneNumber;

    TelephoneContact(String strName, String strTelephoneNumber) {
      this.strName=strName;
      this.strTelephoneNumber=strTelephoneNumber;
    }

    public String getName() {
      return strName;
    }

    public String getTelephoneNumber() {
      return strTelephoneNumber;
    }
  }

  class TelephoneContactComparator
  implements Comparator {

    TelephoneContactComparator() {
    }

    public int compare(Object obj1, Object obj2) {
      TelephoneContact objTC1=(TelephoneContact)obj1;
      TelephoneContact objTC2=(TelephoneContact)obj2;

      return objTC1.strName.compareTo(objTC2.strName);
    }

    public boolean equals(Object obj) {
      return false;
    }
  }
}