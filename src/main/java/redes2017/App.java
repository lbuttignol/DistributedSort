package redes2017;
import java.util.LinkedList;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.lang.InterruptedException;
import java.lang.SecurityException;

/**
 *	Main class on this application this run the distributed bubblesort
 */ 
class App {
	/**
	 *	This method shuld initialize the System with some magical config 
	 *  file recived from main 
	 */
	private void init(){
		// TO-DO
	}
	
	public static void main(String[] args) {
		
		DistSystem ds = new DistSystem(2);
		Middlewar m0 = new Middlewar(Integer.parseInt(args[0]),ds);  
		DistributedArray a = new DistributedArray(11,m0); 

		System.out.println("Finish");

	}	
}