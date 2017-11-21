package redes2017;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.lang.SecurityException;

class App {
	
	public static void main(String[] args) {
		
		Middlewar m0 = new Middlewar(5555);


			System.out.println("aaa-------------------FaROM SERVER:" );
		m0.start();
			System.out.println("aaa-------------------FaROM SERVER:" );

		try{
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName("localhost");
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			String sentence = "inFromUser.readLine();----- un mjs";
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5555);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + modifiedSentence);
			clientSocket.close();
		}catch (SocketException e) {
			System.out.println("ERROR 1 on the clientSocket");
		}catch (SecurityException e) {
			System.out.println("ERROR 2 on the clientSocket");
		}catch (UnknownHostException e){
			System.out.println("ERROR 3 on the clientSocket");
		}catch (IOException e){
			System.out.println("ERROR 4 on the clientSocket");
		}


	}	
}