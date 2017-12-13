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
import java.lang.InterruptedException;

/**
 *	A middlewar is responsable for the comminucation of a distributed node.
 *	Shuld kmow completely the system	
 */
public class Middlewar extends Thread {

	/**
	 *	Number of the process.
	 */
	private Integer procId;

	/**
	 *	Distributed system Information. 
	 */
	private DistSystem system;

	/**
	 *	Communication channel with the other processes
	 */
	private DatagramSocket socket;

	/**
	 *	Thread that listen every message
	 */
	private Listener ear;

	/**
	 *	Process that coordinate important task like barriers
	 */
	private static Integer coordinator = 0;

	/**
	 *	Contains the info of the DistributedArrays that can call
	 */
	private HashMap<String,DistributedArray> registry;

	/**
	 *	Message queue for GETR messages
	 */
	private BlockingQueue<String> getMailbox;
	private BlockingQueue<String> barrierMailbox;
	private BlockingQueue<String> continueMailbox;

	private static final Integer BUFFSIZE = 2048;

	/**
	 *	
	 */
	public DistSystem getSys(){
		return this.system;
	}

	/**
	 *	Default constructor of Middlewar
	 *	@param	procNumber number of process on the system
	 *	@param	info to the other nodes
	 */
	public Middlewar(Integer procNumber, DistSystem info) {
		System.out.println("Starting Middlewar " + procNumber);
		this.procId = procNumber;
		this.system = info;
		try{
			this.socket = new DatagramSocket(5000 + procNumber); 
		}catch (Exception e) {
			System.out.println("Something was wrong building a Middlewar " + procNumber);
			e.printStackTrace();
		}

		this.ear = new Listener(this);
		this.ear.start();

		this.getMailbox = new LinkedBlockingQueue<String>();
		this.barrierMailbox = new LinkedBlockingQueue<String>();
		this.continueMailbox = new LinkedBlockingQueue<String>();

		this.registry = new HashMap<String,DistributedArray>();
	}	

	/**
	 *	This method autosend an especial message to finish the listener
	 */ 
	public void finish(){
		this.ear.finish();
		this.sendTo(this.procId, MessageType.END.toString() + " ");
	}

	/**
	 *	Add an entry to the regisrty of arrays handled by this middlewar
	 */
	public void bind(String name, DistributedArray ref){
		this.registry.put(name,ref);
	}

	/**
	 *	@param name of the array on the system.
	 *	@return the Distributed Array with that name.
	 */
	public DistributedArray getArray(String name){
		return this.registry.get(name);
	}

	/**
	 *	@return True: iff this instance coordinate all the tasks
	 */
	private boolean iAmCoordinator(){
		return this.procId == this.coordinator;
	}

	/**
	 *	Method to ask if another process is the last on the system
	 *	@return True iff procId is the last on the system.
	 */
	public boolean isTheLast(Integer procId){
		return procId == this.system.size() - 1;
	}
	
	/**
	 *	@retrun True iff is the last process on the system
	 */
	public boolean iAmLast(){
		return this.procId == this.system.size() - 1;
	}
	
	/**
	 *	@return the number of this process
	 */	
	public Integer whoAmI(){
		return this.procId;
	}

	/**
	 *	The coordinator send a message to all the other process
	 *	@param message to send
	 */
	private void sendAll(String message){
		if (!this.iAmCoordinator())
			throw new IllegalStateException("Error: Someone trying to make a coordinator task, but does not have the rigths");
		
		for (int i = 1;i < this.system.size() ; i++ ) {
			this.sendTo(i, message);
		}
	}

	/**
	 *	
	 */
	private void waitForAll(){
		if (!this.iAmCoordinator()) 
			throw new IllegalStateException("Error: only a coordinator can wait for all the others process");
		
		for (int i = 0; i < this.system.size(); i++ ) {
			this.receiveFrom(i);
		}
	}

	/**
	 *	the first process coordinates that everyone arrives at the barrier 
	 */
	public void barrier(){
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

		byte[] sendData = new byte[BUFFSIZE];
		sendData = message.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,  receiver.getIp(), receiver.getPort());
		try{
			this.socket.send(sendPacket);
		}catch (IOException e) {
			System.out.println("Error sending a message to: " + procNumber);	
			e.printStackTrace();	
		}
	}

	/**
	 * 	This method wait for a message.
	 *	@return the message received 
	 */
	public String receive(){
		byte[] receiveData = new byte[BUFFSIZE];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		try{
			this.socket.receive(receivePacket);
		}catch (IOException e) {
			System.out.println("Panic !!! socket.receive fail.");
			e.printStackTrace();
		}
		
		String message = new String(receivePacket.getData());
		return message;
	}

	/**
	 *	
	 */
	public synchronized String receiveFrom(int procNumber){
		return this.dequeueMailFrom(procNumber);
	}

	public void enqueueMail(String message){
		try{
			this.getMailbox.put(message);
		}catch(InterruptedException e){
			System.out.println("Enqueue mail has failed");
			e.printStackTrace();
		}
	}

	private String dequeueMail(BlockingQueue<String> aMailbox){
		String message = "";
		try{
			message = aMailbox.take();
		}catch(InterruptedException e){
			System.out.println("BlockingQueue take has fail");
			e.printStackTrace();
		}
		return message;
	}

	private String dequeueMailFrom(Integer id){
		String message = "";
		boolean find = false;
		while (!find) {
			message = this.dequeueMail(this.getMailbox);
			String[] parsedMsg = Message.parse(message);
			if (Message.whoSendIt(message) == id) {
				find = true;
			}else {
				this.enqueueMail(message);
			}
		}
		return message;
	}





}
