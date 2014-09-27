
public class RTRunnable implements Runnable {//this is my thread class
	int startRow, endRow,isFinishedArrayPlace;
	RayTracer rt;
	
		public RTRunnable(RayTracer rt, int startRow, int endRow,int isFinishedArrayPlace){
			this.startRow=startRow;
			this.endRow=endRow;
			this.rt = rt;
			this.isFinishedArrayPlace=isFinishedArrayPlace;
			
		}

	    public void run() {
	        rt.threadPixel( startRow,endRow,isFinishedArrayPlace);	      
	    }
	}

