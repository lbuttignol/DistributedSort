package redes2017;

public class DistributedArray{
	
	private int length;

	private int partitions;
	/**
	 * 	Message manager 
	 */
	private Middlewar secretary;

	public DistributedArray(int vaules, DistSystem system){
		// TO-DO
	}

	/**
	 *	@param index int that represent a position on the array.
	 * 	@return the value on the given index.
	 */
	public int getVal(int index){

		return 0;
	}

	/**
	 *	@param procId int that represents a process 
	 *  @return the lower index on this process
	 */ 
	public int lowerIndex(int procId){
		// TO-DO
		return 0;
	}

	/**
	 *	@param procId int that represents a process 
	 *  @return the higher index on this process
	 */
	public int upperIndex(int procId){
		// TO-DO
		return 0;
	}

	/**
	 *	Sort the array memory available on the process
	 *	@param procId int that represents a process 
	 */
	public void bubbleSort(int procId){
		// TO-DO
	}

	/**
	 *	Swap the two elements on the 
	 */
	private void swap(int e1,int e2) {
		// TO-DO
	}	

	public void DistributedSort() {
	// /**
	//  *	True iff the distributed system is finished.
	//  * 	Global variable of the system, should be access sysnchonized
	//  */		

		// this.init();
		// // DistributedArray list = new DistributedArray(40,);
		// boolean finish = false;
		// while (! finish) {
		// 	finish = true;
		// 	list.bubbleSort(this.pid);
		// 	// secretary.barrier();

		// 	if (this.pid != this.size-1) {
		// 		if (list.getVal(list.upperIndex(this.pid)) > list.getVal(list.lowerIndex(this.pid))) {
		// 			this.swap(list.upperIndex(this.pid),list.lowerIndex(this.pid));
		// 			finish = false;
		// 		}
		// 	}

		// 	// finish = secretary.andReduce();
		// }

	}

}

