package redes2017;

import java.net.DatagramSocket;

public class Middlewar {

	private DistSystem system;

	private DatagramSocket soket;

	private int index;

	public Middlewar(int procNumber, DistSystem info){
		this.soket = new DatagramSocket(); 
	}	

	/**
	 *	the first process coordinates that everyone arrives at the barrier 
	 */
	public void barrier(){
		// TO-DO
	}

	/**
	 *	Find the value of a variable on every process and make an and operation
	 * 	@return True iff every process finish
	 */
	public boolean andReduce(){
		return false;
	}	

	/**
	 *	
	 */
	public void sendTo(int procNumber, String message){
		// TO-DO

	}
	public String receiveFrom(String ip_Port){
		// TO-DO
		return "";
	}

	public String receive(){
		// TO-DO
		return "";
	}

	// esta clase tiene que tener el dispacher paraa manejar los sets y gets
	// sobre el arreglo distribuido

	// y además 
}