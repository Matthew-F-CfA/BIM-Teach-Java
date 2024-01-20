import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class SortJobs {

  public static void main(String args[]) {
    try {
      SortJobs udtSJ=new SortJobs();

      BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

      Hashtable hshContacts=new Hashtable();

      do {
        System.out.println("Type in \"new\" if you want to enter a new worker");
        System.out.println("Type in \"jobs\" if you want to add to the # of jobs that a worker has completed");
        System.out.println("Type in \"list\" if you want to see a list");
        System.out.println("Type in \"exit\" to exit the program");

        String strNextOperation=br.readLine();

        if(strNextOperation.equals("new")) {
          System.out.println("Enter the worker's name:");

          String strName=br.readLine();

          System.out.println("Enter the worker's phone number:");

          String strTelephoneNumber=br.readLine();

          SortJobs.JobContact nextContact=udtSJ.new JobContact(strName, strTelephoneNumber);

          hshContacts.put(strName, nextContact);

          System.out.println("");
        }
        else if(strNextOperation.equals("jobs")) {
          System.out.println("Enter the worker's name:");

          String strName=br.readLine();

          System.out.println("Enter the number of jobs completed:");

          String strJobsCompleted=br.readLine();

          int intJobsCompleted=Integer.parseInt(strJobsCompleted);

          SortJobs.JobContact contact=(SortJobs.JobContact)hshContacts.get(strName);

          contact.setJobsCompleted(contact.getJobsCompleted()+intJobsCompleted);

          System.out.println("");
        }
        else if(strNextOperation.equals("list")) {
          SortJobs.JobContact contacts[]=new SortJobs.JobContact[hshContacts.size()];

          Iterator iter0=hshContacts.entrySet().iterator();
          int intCountI=0;
          while(iter0.hasNext()) {
            Map.Entry mEntry=(Map.Entry)iter0.next();

            contacts[intCountI++]=(SortJobs.JobContact)mEntry.getValue();
          }

          Arrays.sort(contacts, udtSJ.new JobContactComparator());

          System.out.println("Contact List:");
          System.out.println("Format: <name>:<number>:<jobs completed>");
          for(int i=0;i<contacts.length;i++) {
            System.out.println(contacts[i].getName()+":"+contacts[i].getTelephoneNumber()+":"+contacts[i].getJobsCompleted());
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

  class JobContact {
    String strName;
    String strTelephoneNumber;
    int intJobsCompleted=0;

    JobContact(String strName, String strTelephoneNumber) {
      this.strName=strName;
      this.strTelephoneNumber=strTelephoneNumber;
    }

    public String getName() {
      return strName;
    }

    public String getTelephoneNumber() {
      return strTelephoneNumber;
    }

    public int getJobsCompleted() {
      return intJobsCompleted;
    }

    public void setJobsCompleted(int intJobsCompleted) {
      this.intJobsCompleted=intJobsCompleted;
    }
  }

  class JobContactComparator
  implements Comparator {

    JobContactComparator() {
    }

    public int compare(Object obj1, Object obj2) {
      JobContact objJC1=(JobContact)obj1;
      JobContact objJC2=(JobContact)obj2;

      if(objJC1.getJobsCompleted()<objJC2.getJobsCompleted())
        return -1;
      else if(objJC1.getJobsCompleted()>objJC2.getJobsCompleted())
        return 1;

      return 0;
    }

    public boolean equals(Object obj) {
      return false;
    }
  }
}