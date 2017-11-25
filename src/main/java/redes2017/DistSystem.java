package redes2017;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
public class DistSystem {

	/**
	 *	This object has the ip and port of all the process.	
	 */
	private LinkedList<Process> sys;

	/**
	 *	Default constructor of a distribute system
	 */
	public DistSystem(){
		this.sys = new LinkedList<Process>();
		System.out.println(this.sys.toString());

		for (int i = 0; i < 4; i++) {
			this.sys.add(new Process(i,5000+i));
		}
		System.out.println(this.sys.toString());
	}

	/**
	 * 	@param index the id of the process to search
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
