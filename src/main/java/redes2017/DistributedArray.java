package redes2017;

import java.lang.IndexOutOfBoundsException;
import java.util.Arrays;
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
	private Integer[] list;

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
	public DistributedArray(Integer size, Middlewar mid ){
		this.secretary   = mid;
		this.procId  	 = this.secretary.whoAmI();
		this.totalLength = size;

		//name treatment
		this.name = "a" + this.counter.toString();
		this.counter++;
		// System.out.println(this.name);

		// bind this array on the middlewar
		this.secretary.bind(this.name,this);
		
		DistSystem sys 	 = this.secretary.getSys();
		this.partitions  = sys.size();
		this.resto 		 = size % partitions;
		this.localLength = size / partitions; 
		
		if(resto != 0 && this.secretary.iAmLast()){
			this.localLength += this.resto;
		}
		this.list = new Integer[localLength];

	}

	/**
	 *	@param index on the Distributed Array
	 *	@retrun True iff the given index is on this part of the array
	 */
	private boolean isHere(Integer index){
		return index >= this.lowerIndex(this.procId) && index <= this.upperIndex(this.procId);
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
		Integer result = index / (this.totalLength / this.partitions);

		if (result > this.partitions - 1) {
			result = this.partitions - 1;
		}
		return result;
	}

	/**
	 *	Change the value on the given index with val
	 *	@param index to set on the array 
	 *	@param val value to put on the index 
	 */
	public synchronized void set(Integer index, Integer val){
		if (!this.rightIndex(index)) {
			throw new IndexOutOfBoundsException("Index out of bound on set");
		}
		if(!this.isHere(index)) {
			this.secretary.sendTo(this.whoGotIt(index), MessageType.SET.toString() + " " + this.name + " " + index + " " + val + " ");
		}else {
			this.list[index - this.lowerIndex(this.procId)] = val;
		}
	}

	/**
	 *	@param index Integer that represent a position on the array.
	 * 	@return the value on the given index.
	 */
	public synchronized Integer get(Integer index){
		if (!this.rightIndex(index)) {
			throw new IndexOutOfBoundsException("Index out of bound on get");
		}
		if(!this.isHere(index)){

			this.secretary.sendTo(this.whoGotIt(index) , MessageType.GET.toString() +" " + this.name + " " + index.toString() + " " + this.procId + " " );
			String message = this.secretary.receiveFrom(this.whoGotIt(index),MessageType.GETR);
			return Message.getIntParam(message,3);
		}
		return this.list[index - this.lowerIndex(this.procId)];
	}

	/**
	 *	@param procId Integer that represents a process 
	 *  @return the lower index on this process
	 */ 
	private Integer lowerIndex(Integer procId){
		return this.procId * this.totalLength / this.partitions;
	}

	/**
	 *	@param procId Integer that represents a process 
	 *  @return the higher index on this process
	 */
	private Integer upperIndex(Integer procId){ 
		return this.procId * this.totalLength / this.partitions + this.localLength - 1;
	}

	/**
	 *	Sort the array memory available on the process
	 *	@param procId Integer that represents a process 
	 */
	public void internalSort(){
		Arrays.sort(this.list);
	}

	/**
	 *	Swap two elements on the Array
	 *	@param e0 global index to swap
	 *	@param e1 globat index to swap
	 */
	public synchronized void swap(Integer e0,Integer e1) {
		Integer aux0 = this.get(e0);
		Integer aux1 = this.get(e1);
		this.set(e1, aux0);
		this.set(e0, aux1);
	}	

	public void distributedSort() {
	// /**
	//  *	True iff the distributed system is finished.
	//  * 	Global variable of the system, should be access sysnchonized
	//  */		

		boolean finish = false;
		while (! finish) {
			finish = true;
			this.internalSort();
			this.secretary.barrier();

			if (this.procId != this.totalLength-1) {
				if (this.get(this.upperIndex(this.procId)).compareTo(this.get(this.lowerIndex(this.procId))) < 0) {
					this.swap(this.upperIndex(this.procId),this.lowerIndex(this.procId));
					finish = false;
				}
			}

			// finish = secretary.andReduce();
		}

	}

	/**
	 *
	 */
	@Override
	public String toString(){
		return  "Array name " 	 + this.name + "\n" +
				"global length " + this.totalLength + "\n" +
				"local length "  + this.localLength + "\n" +
				"node number " 	 + this.procId + "\n" +
				"list length " 	 + this.list.length + "\n" ;
	}

	

}

