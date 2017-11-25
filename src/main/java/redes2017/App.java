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

class App {
	/**
	 *	This method shuld initialize the System with some magical config 
	 *  file recived from main 
	 */
	private void init(){
		// TO-DO
	}
	
	public static void main(String[] args) {
		
		DistSystem ds = new DistSystem();

		System.out.println("System created." );
		
		System.out.println("buinding Middlewar " + Integer.parseInt(args[0]) );

		
		Middlewar m0 = new Middlewar(Integer.parseInt(args[0]),ds);

		System.out.println("Middlewar created." );

		m0.start();
		
		System.out.println("Middlewars running . . ." );
		
		try{
			m0.join();
		}catch(InterruptedException e){
			System.out.println("Sleep broken . ");
		}


	}	
}