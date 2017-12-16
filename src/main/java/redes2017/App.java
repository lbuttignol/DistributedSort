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
			a.set(0,5);
			a.set(1,3000);
			a.set(2,60);
			a.set(3,45000);
			a.set(4,6000);
			a.set(5,200);
			a.set(6,14);
			a.set(7,1500);
			a.set(8,1);
			a.set(9,130000);
			// a.set(10,7);

			System.out.println("Final to send message ");

			// show all values
			for (int i=0; i<arrayLength; i++) {
				System.out.println("index "+ i +" = " +a.get(i));
			}

			// System.out.println("--local swap");
			// System.out.println("index "+ 0 +" = " +a.get(0));
			// System.out.println("index "+ 1 +" = " +a.get(1));
			// a.swap(0,1);
			// System.out.println("index "+ 0 +" = " +a.get(0));
			// System.out.println("index "+ 1 +" = " +a.get(1));
			
			// System.out.println("--All remote swap");
			// System.out.println("index "+ 8 +" = " +a.get(8));
			// System.out.println("index "+ 9 +" = " +a.get(9));
			// a.swap(8,9);
			// System.out.println("index "+ 8 +" = " +a.get(8));
			// System.out.println("index "+ 9 +" = " +a.get(9));

			// System.out.println("--half local and half remote swap");
			// System.out.println("index "+ 2 +" = " + a.get(2));
			// System.out.println("index "+ 7 +" = " + a.get(7));
			// a.swap(2,7);
			// System.out.println("index "+ 2 +" = " + a.get(2));
			// System.out.println("index "+ 7 +" = " + a.get(7));

			// System.out.println("--half local and half remote null swap");
			// System.out.println("index "+ 3 +" = " + a.get(3));
			// System.out.println("index "+ 10 +" = " + a.get(10));
			// a.swap(3,10);
			// System.out.println("index "+ 3 +" = " + a.get(3));
			// System.out.println("index "+ 10 +" = " + a.get(10));



			// try{
			// Thread.sleep(4000);
			// }catch (InterruptedException e) {
			// 	System.out.println("sleep broken");
			// 	e.printStackTrace();
			// }
			// m0.finish();
		}
		try{
			Thread.sleep(4000);
		}catch (InterruptedException e) {
			System.out.println("sleep broken");
			e.printStackTrace();
		}

		System.out.println("begin SORT!--------------------------------------------");
		a.distributedSort();
		System.out.println("end SORT!--------------------------------------------");

		if (m0.whoAmI()== 0) {
			// show all values
			for (int i=0; i<arrayLength; i++) {
				System.out.println("index "+ i +" = " +a.get(i));
			}
		}

	}	

	
}