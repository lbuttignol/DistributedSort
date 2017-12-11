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

		DistSystem ds = new DistSystem(nodes);
		Middlewar m0 = new Middlewar(Integer.parseInt(args[0]),ds);  
		DistributedArray a = new DistributedArray(arrayLength,m0); 


		if (m0.whoAmI()== 0) {
			// a.set(0,5);
			// a.set(1,3);
			// a.set(2,6);
			// a.set(3,45);
			// a.set(4,6);
			a.set(5,2);
			// a.set(6,14);
			a.set(7,15);
			// a.set(8,10);
			a.set(9,13);
			// a.set(10,7);

			System.out.println("Final to send message ");

			
			for (int i=0; i<arrayLength; i++) {
				System.out.println("index "+ i +" = " +a.get(i));
			}

			try{
			Thread.sleep(4000);
			}catch (InterruptedException e) {
				System.out.println("sleep broken");
				e.printStackTrace();
			}
			m0.finish();
		}
	}	
}