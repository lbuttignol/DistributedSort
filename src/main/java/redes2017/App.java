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
	
	private static final Integer nodes = 2; 

	private static final Integer arrayLength = 11;

	public static void main(String[] args) {
		System.out.println(nodes);
		System.out.println(arrayLength);

		DistSystem ds = new DistSystem(nodes);
		Middlewar m0 = new Middlewar(Integer.parseInt(args[0]),ds);  
		DistributedArray a = new DistributedArray(arrayLength,m0); 

		System.out.println("sending message");

		a.set(0,5);
		a.set(1,3);
		a.set(2,6);
		a.set(3,45);
		a.set(4,6);
		// a.set(5,1);
		// a.set(6,1);
		// a.set(7,15);

		System.out.println("sending message2 ");

		for (int i=0; i<5; i++) {
			System.out.println("index "+ i +" = " +a.get(i));
		}

		m0.finish();
	}	
}