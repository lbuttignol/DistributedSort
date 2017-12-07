package redes2017;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
/**
 *	Have the info of the distributed system, this calss know every node and
 *	the information to communicate with everyone on this distributed system.
 */
public class DistSystem {

	/**
	 *	This object has the ip and port of all the process.	
	 */
	private LinkedList<Process> sys;

	/**
	 *	Default constructor of a distribute system
	 *	@param procNum. Size of the distributed system
	 */
	public DistSystem(Integer procNum){
		this.sys = new LinkedList<Process>();

		for (int i = 0; i < procNum; i++) {
			this.sys.add(new Process(i,5000+i));
		}
	}

	/**
	 * 	@param index the id of the process to search on the system
	 *	@return The process with id equals to {@code index}
	 */
	public Process getProcess(Integer index){
		if(index >= this.sys.size()) 
			throw new IllegalArgumentException("Error: the given id is not on the system. ");
		return this.sys.get(index);
	}

	/**
	 *	Calculate the number of process on the system.  
	 */ 
	public Integer size(){
		return this.sys.size();
	}

}
