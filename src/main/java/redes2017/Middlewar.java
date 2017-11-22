package redes2017;

import java.io.IOException;
import java.lang.SecurityException;
import java.lang.Thread;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Middlewar extends Thread {

	private Integer procId;

	private DistSystem system;

	private DatagramSocket socket;

	public Middlewar(int procNumber, DistSystem info) {
		System.out.println("Starting Middlewar " + procNumber);
		this. procId = procNumber;
		this.system = info;
		try{
			this.socket = new DatagramSocket(); 
		}catch (Exception e) {
			System.out.println("Something was wrong building a Middlewar " + procNumber);
		}
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
		
		Process receiver = system.getProcess(procNumber);

		byte[] sendData = new byte[1024];
		sendData = message.getBytes();

		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,  receiver.getIp(), receiver.getPort());
		try{
			this.socket.send(sendPacket);
		}catch (IOException e) {
			System.out.println("Error sending a message to: " + procNumber);		
		}
	}

	/**
	 *	
	 */
	public String receiveFrom(int procNumber){
		// TO-DO
		return "";
	}

	/**
	 *	
	 */
	public String receive(){
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		try{
			this.socket.receive(receivePacket);
		}catch (IOException e) {
			System.out.println("Panic !!! socket.receive fail.");
		}
		
		String message = new String( receivePacket.getData());
		return message;
	}

	/**
	 *	
	 */
	@Override
	public void run() {
		if (this.procId == 0){
			sendTo(this.procId +1 , "Hey! I am 0 .");
		}else {
			String m = receive();
			System.out.println("I am " + this.procId + "Someone send: " + m);
			sendTo(this.procId +1, "The other guy are talking. I am " + this.procId );
		}
		// while(true){
		// 	String message = this.receive();
		// 	System.out.println(message);

		// }
	}
	// esta clase tiene que tener el dispacher paraa manejar los sets y gets
	// sobre el arreglo distribuido

	// y además 


// get i 				hacer distpaching a get del array
// getrest i v 			hay que despertar siempre
// set i 				hacer distpaching a set del array

// barrier b 			hay que encolar el mensaje 
// continue				hay que despertar siempre 

// reduce 				hay que 
// reduceresp n


}