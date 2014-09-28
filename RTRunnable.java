public class RTRunnable implements Runnable {// this is my thread class
  int startRow, endRow, threadId;
  RayTracer rt;

  public RTRunnable(RayTracer rt, int startRow, int endRow, int threadId) {
    this.startRow = startRow;
    this.endRow = endRow;
    this.rt = rt;
    this.threadId = threadId;

  }

  public void run() {
    rt.threadPixel(startRow, endRow, threadId);
  }
}
