package redes2017;


public class DistSystem {
	
	/**
	 *	Number of this particular process
	 */
	private int pid;

	/**
	 *	Number of process on the system
	 */
	private int process;

	/**
	 *	True iff the distributed system is finished.
	 * 	Global variable of the system, should be access sysnchonized
	 */
	private boolean finish;

	/**
	 *	Contains the global distributed array to sort.	
	 * 	Global variable of the system, should be access sysnchonized.
	 */
	private DistributedArray list;

	/**
	 *	This method shuld initialize the System with some magical config 
	 *  file recived from main 
	 */
	private void init(){
		// TO-DO
	}

	private void swap () {
		// to-do
	}

	private void barrier(){

	}

	private boolean andReduce(){
		return false;
	}

	public void DistributedSort() {
		this.init();
		list = new DistributedArray(4000);
		finish = false;
		while (! finish) {
			finish = true;
			list.bubbleSort(this.pid);
			// barrier, ver!

			if (this.pid != this.process-1) {
				if (list.getVal(list.upperIndex(this.pid)) > list.getVal(list.lowerIndex(this.pid))) {
					this.swap();
					finish = false;
				}
			}
			
			finish = this.andReduce();
		}

	}


}
