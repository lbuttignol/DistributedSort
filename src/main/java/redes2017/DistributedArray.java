package redes2017;

import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.Collections;

public class DistributedArray{
	
	private Integer totalLength;

	private Integer localLength;

	private ArrayList<Integer> list;

	private Integer partitions;

	private Integer procId;

	private Integer resto;
	/**
	 * 	Message manager 
	 */
	private Middlewar secretary;

	/**
	 *	Distributed Array constructor
	 *	@param Values size of the array
	 *	@param 
	 */
	public DistributedArray(int val, Middlewar mid ){
		this.secretary   = mid;
		this.procId  	 = this.secretary.whoAmI();
		this.totalLength = val;

		DistSystem sys 	 = this.secretary.getSys();
		this.partitions  = sys.size();
		this.resto 		 = val % partitions;
		this.localLength = val / partitions; 
		
		if(resto != 0 && this.secretary.iAmLast()){
			this.localLength += this.resto;
		}
		this.list = new ArrayList<Integer>(this.localLength);

	}

	/**
	 *	@param index on the Distributed Array
	 *	@retrun True iff the given index is on this part of the array
	 */
	private boolean isHere(Integer index){
		return index >= this.lowerIndex(this.procId) && index >= this.upperIndex(this.procId);
	}

	/**
	 *	@param index on the Distributed Array
	 *	@return True iff the given index belongs to the array. 
	 */
	private boolean rightIndex(Integer index){
		return index >= 0 && index < this.totalLength;
	}

	/**
	 *	@param index on the Distributed Array
	 *	@preturn the process number that has the given index
	 */
	private Integer whoGotIt(Integer index){
		if(!this.rightIndex(index)){
			throw new IndexOutOfBoundsException("Index out of bound on whoGotIt");
		}
		return index * this.partitions / this.totalLength;
	}

	/**
	 *
	 */
	public void setValue(Integer index, Integer val){
		if (!this.rightIndex(index)) {
			throw new IndexOutOfBoundsException("Index out of bound on setValue");
		}
		if (this.isHere(index)) {
			// TO-DO
		}
	}

	/**
	 *	@param index Integer that represent a position on the array.
	 * 	@return the value on the given index.
	 */
	public Integer getValue(Integer index){
		if (!this.rightIndex(index)) {
			throw new IndexOutOfBoundsException("Index out of bound on getValue");
		}
		if(!this.isHere(index)){

			this.secretary.sendTo(this.whoGotIt(index) ,"GET " + "a1 " + index.toString() );
			throw new IllegalArgumentException("Panic !!! Error on getValue");
		}
		return this.list.get(index - this.lowerIndex(this.procId));
	}

	/**
	 *	@param procId int that represents a process 
	 *  @return the lower index on this process
	 */ 
	private int lowerIndex(int procId){
		return this.procId * this.totalLength / this.partitions;
	}

	/**
	 *	@param procId int that represents a process 
	 *  @return the higher index on this process
	 */
	private Integer upperIndex(int procId){
		
		return this.procId * this.totalLength / this.partitions + this.localLength - 1;
	}

	/**
	 *	Sort the array memory available on the process
	 *	@param procId int that represents a process 
	 */
	public void sort(int procId){
		Collections.sort(this.list);
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

