package redes2017;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
public class DistSystem {

	/**
	 *	This object has the ip and port of all the process.	
	 */
	private List<Process> sys;



	/**
	 *	Default constructor of a distribute system
	 */
	public DistSystem(){
		this.sys = new LinkedList<Process>();

		for (int i=0; i>4; i++) {
			Process newProc = new Process(i,5000+i);
			this.sys.set(i,newProc);
		}

		System.out.println(this.sys.toString());
	}

	/**
	 *
	 */
	public Process getProcess(Integer index){
		return this.sys.get(index);
	}


	/**
	 *	This method shuld initialize the System with some magical config 
	 *  file recived from main 
	 */
	private void init(){
		// TO-DO
	}

	


}
