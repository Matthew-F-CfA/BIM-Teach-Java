class WithSynchronization {
  volatile int intCount=0;

  static volatile Object syncObj=new Object();

  public static void main(String args[]) {
    try {
      WithSynchronization with=new WithSynchronization();

      WithSynchronization.WorkerThread thrWorker=with.new WorkerThread();
      WithSynchronization.WorkerThread thrWorker2=with.new WorkerThread();

      thrWorker.start();
      thrWorker2.start();

      thrWorker.join();
      thrWorker2.join();

      System.out.println("intCount: "+with.intCount);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  class WorkerThread extends Thread {

    WorkerThread() {
      super();
    }

    public void run() {
      for(int i=0;i<1000;i++) {

synchronized(WithSynchronization.syncObj) {

        intCount=intCount+1;

}

      }
    }
  }
}