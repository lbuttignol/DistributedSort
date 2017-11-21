package redes2017;

import java.io.IOException;
import java.lang.SecurityException;
import java.lang.Thread;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Middlewar extends Thread {

	private Process p;

	private DistSystem system;

	private DatagramSocket soket;

	private int index;

	/**
	 *	Only a test attribute
	 */
	@Deprecated
	private int port;

	@Deprecated
	public Middlewar(int port){
		this.port = port;
	}

	public Middlewar(int procNumber, DistSystem info) throws SocketException,SecurityException {
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

	/**
	 *	
	 */
	public String receiveFrom(String ip_Port){
		// TO-DO
		return "";
	}

	/**
	 *	
	 */
	public String receive(){
		// TO-DO
		return "";
	}

	/**
	 *	
	 */
	@Override
	public void run() {

		try{
			this.soket = new DatagramSocket(this.port);
		}catch (SocketException e) {
			System.out.println("ERROR_1");
		}catch (SecurityException e) {
			System.out.println("ERROR_2");
			
		}

		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try{
			this.soket.receive(receivePacket);
			System.out.println("aa-----------------ERROR_1");
			System.out.println("aa-----------------ERROR_2");
		}catch (IOException e) {
			System.out.println("ERROR_3");
		}
		
			System.out.println("aa-----------------ERROR_3");
		String sentence = new String( receivePacket.getData());
		System.out.println("RECEIVED: " + sentence);
		InetAddress IPAddress = receivePacket.getAddress();
		int port = receivePacket.getPort();
		String capitalizedSentence = sentence.toUpperCase();
		sendData = capitalizedSentence.getBytes();
		DatagramPacket sendPacket =
		new DatagramPacket(sendData, sendData.length, IPAddress, port);
		try{
			this.soket.send(sendPacket);
		}catch (IOException e) {
			System.out.println("ERROR_4");
		}
		// while(true){

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