package redes2017;

import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.Collections;

public class DistributedArray{
	
	/**
	 *	Total length of the distributed array
	 */
	private Integer totalLength;

	/**
	 *	Elements on this part of the distributed array  
	 */
	private Integer localLength;

	/**
	 *	List of elements on this part of the array  
	 */
	private ArrayList<Integer> list;

	/**
	 *	Number of parts that have the distributed array  
	 */
	private Integer partitions;

	/**
	 *	This number indicate the order of this partition on the global array  
	 */
	private Integer procId;

	/**
	 *	Quantity of elements added to the last partition 
	 */
	private Integer resto;

	/**
	 *	Internal name of the array, this shoild be unique. 
	 */
	private String name;

	/**
	 *	Counter of DistributedArray instances. This is for create the name of the instance. 
	 */
	private static Integer counter = 0;

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

		//name treatment
		this.name = "a" + this.counter.toString();
		this.counter++;

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
	 *	Change the value on the given index with val
	 *	@param index to set on the array 
	 *	@param val value to put on the index 
	 */
	public void setValue(Integer index, Integer val){
		if (!this.rightIndex(index)) {
			throw new IndexOutOfBoundsException("Index out of bound on setValue");
		}
		if (!this.isHere(index)) {
			this.secretary.sendTo(this.whoGotIt(index), "SET " + this.name + " " + index + " " + val);
		}else {
			this.list.set(index - this.lowerIndex(this.procId), val);
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

			this.secretary.sendTo(this.whoGotIt(index) ,"GET " + this.name + " " + index.toString() );
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

