class WithoutSynchronization {
  volatile int intCount=0;

  public static void main(String args[]) {
    try {
      WithoutSynchronization without=new WithoutSynchronization();

      WithoutSynchronization.WorkerThread thrWorker=without.new WorkerThread();
      WithoutSynchronization.WorkerThread thrWorker2=without.new WorkerThread();

      thrWorker.start();
      thrWorker2.start();

      thrWorker.join();
      thrWorker2.join();

      System.out.println("intCount: "+without.intCount);
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
        intCount=intCount+1;
      }
    }
  }
}