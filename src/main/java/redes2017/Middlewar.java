package redes2017;

import java.io.IOException;
import java.lang.SecurityException;
import java.lang.Thread;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.HashMap;

public class Middlewar extends Thread {

	private Integer procId;

	private DistSystem system;

	private DatagramSocket socket;

	private Listener ear;

	private static Integer coordinator = 0;

	private HashMap<String,DistributedArray> registry;

	private BlockingQueue<String> mailbox;

	public DistSystem getSys(){
		return this.system;
	}

	public Middlewar(Integer procNumber, DistSystem info) {
		System.out.println("Starting Middlewar " + procNumber);
		this.procId = procNumber;
		this.system = info;
		try{
			this.socket = new DatagramSocket(5000 + procNumber); 
		}catch (Exception e) {
			System.out.println("Something was wrong building a Middlewar " + procNumber);
		}

		this.ear = new Listener(this);
		this.ear.start();

		this.mailbox = new LinkedBlockingQueue<String>();
		this.registry = new HashMap<String,DistributedArray>();
	}	


	public BlockingQueue<String> getMailbox(){
		return this.mailbox;
	}

	public void bind(String name, DistributedArray ref){
		this.registry.put(name,ref);
	}

	/**
	 *	@return True: iff this instance coordinate all the tasks
	 */
	private boolean iAmCoordinator(){
		return this.procId == this.coordinator;
	}

	/**
	 *
	 */
	public boolean isTheLast(Integer procId){
		return procId == this.system.size() - 1;
	}
	
	/**
	 *
	 */
	public boolean iAmLast(){
		return this.procId == this.system.size() - 1;
	}
	
	/**
	 *
	 */	
	public Integer whoAmI(){
		return this.procId;
	}

	/**
	 *	The coordinator send a message to all the other process
	 *	@param message to send
	 */
	public void sendAll(String message){
		if (!this.iAmCoordinator())
			throw new IllegalStateException("Error: Someone trying to make a coordinator task, but does not have the rigths");
		
		for (int i = 1;i <= this.system.size() ; i++ ) {
			this.sendTo(i, message);
		}
	}

	/**
	 *	the first process coordinates that everyone arrives at the barrier 
	 */
	public void barrier(){
		// TO-DO
		if (this.iAmCoordinator()) {
			// wait a barrier for the other process
			// send a continue to all the process
			this.sendAll("a continue message");
		}else {
			this.sendTo(this.coordinator,"barrier");
			// whait for a continue
		}	
	}

	/**
	 *	Find the value of a variable on every process and make an AND operation
	 * 	@return True iff every process finish
	 */
	public boolean andReduce(){
		//TO-DO
		return false;
	}	

	/**
	 *  This method send a message to another process
	 *	@param procNumber receiver process
	 *  @param message to send
	 */
	public void sendTo(Integer procNumber, String message){
		
		Process receiver = system.getProcess(procNumber);

		byte[] sendData = new byte[1024];
		sendData = message.getBytes();
		System.out.println("LE MANDO AL : " + receiver.getIp().toString());
		System.out.println("LE MANDO AL : " + receiver.getPort());
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,  receiver.getIp(), receiver.getPort());
		try{
			this.socket.send(sendPacket);
		}catch (IOException e) {
			System.out.println("Error sending a message to: " + procNumber);		
		}
	}

	/**
	 * 	This method wait for a message.
	 *	@return the message received 
	 */
	public String receive(){
		System.out.println("---I am " + this.procId + " Listening in : " + this.system.getProcess(this.procId).getIp().toString());
		System.out.println("---I am " + this.procId + " Listening in : " + this.system.getProcess(this.procId).getPort());
		
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, 13);
		
		try{
			this.socket.receive(receivePacket);
		}catch (IOException e) {
			System.out.println("Panic !!! socket.receive fail.");
		}
		
		System.out.println("---I am " + this.procId + "llegó el mensaje. ");
		String message = new String(receivePacket.getData());
		return message;
	}

	/**
	 *	
	 */
	public String receiveFrom(int procNumber){
		// TO-DO
		return "";
	}






}