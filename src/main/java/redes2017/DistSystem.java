package redes2017;

import java.util.HashMap;

public class DistSystem {
	
	/**
	 *	Number of this particular process
	 */
	private Integer pid;

	/**
	 *	Number of process on the system
	 */
	private Integer size;

	/**
	 *	This object has the ip and port of all the process.	
	 */
	private HashMap<Integer,String> sys;




	/**
	 *	Default constructor of a distribute system
	 */
	public DistSystem(){
		size = 4;
		sys = new HashMap<Integer,String>();
		sys.put(0,"127.0.0.1:5000");
		sys.put(1,"127.0.0.1:5001");
		sys.put(2,"127.0.0.1:5002");
		sys.put(3,"127.0.0.1:5003");
		sys.put(4,"127.0.0.1:5004");

	}

	public void setPid(Integer val){
		this.pid = val;
	}
	
	/**
	 *	This method shuld initialize the System with some magical config 
	 *  file recived from main 
	 */
	private void init(){
		// TO-DO
	}

	/**
	 *	Swap the two elements on the 
	 */
	private void swap(int e1,int e2) {
		// TO-DO
	}	

	public void DistributedSort(DistributedArray list) {
	// /**
	//  *	True iff the distributed system is finished.
	//  * 	Global variable of the system, should be access sysnchonized
	//  */		

		this.init();
		// DistributedArray list = new DistributedArray(40,);
		boolean finish = false;
		while (! finish) {
			finish = true;
			list.bubbleSort(this.pid);
			// secretary.barrier();

			if (this.pid != this.size-1) {
				if (list.getVal(list.upperIndex(this.pid)) > list.getVal(list.lowerIndex(this.pid))) {
					this.swap(list.upperIndex(this.pid),list.lowerIndex(this.pid));
					finish = false;
				}
			}

			// finish = secretary.andReduce();
		}

	}


}
