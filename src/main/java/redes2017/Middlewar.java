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
 *  A middlewar is responsable for the comminucation of a distributed node.
 *  Shuld kmow completely the system    
 */
public class Middlewar extends Thread {

    /**
     *  Number of the process.
     */
    private Integer procId;

    /**
     *  Distributed system Information. 
     */
    private DistSystem system;

    /**
     *  Communication channel with the other processes
     */
    private DatagramSocket socket;

    /**
     *  Thread that listen every message
     */
    private Listener ear;

    /**
     *  Process that coordinate important task like barriers
     */
    private static Integer coordinator = 0;

    /**
     *  Contains the info of the DistributedArrays that can call
     */
    private HashMap<String,DistributedArray> registry;

    /**
     *  Barrier Id
     */
    private Integer barrierId;
    
    /**
     *  Reduce Id
     */
    private Integer reduceId;
    
    /**
     *  Message queue for GETR messages
     */
    private BlockingQueue<String> getMailbox;

    /**
     *  Message queue for BARRIER messages
     */
    private BlockingQueue<String> barrierMailbox;

    /**
     *  Message queue for CONTINUE messages
     */
    private BlockingQueue<String> continueMailbox;

    /**
     *  Message queue for ANDREDUCE messages
     */
    private BlockingQueue<String> andRedMailbox;

    /**
     *  Message queue for ANDREDUCERSP messages
     */
    private BlockingQueue<String> andRedRspMailbox;

    private static final Integer BUFFSIZE = 2048;

    /**
     *  
     */
    public DistSystem getSys(){
        return this.system;
    }

    /**
     *  Default constructor of Middlewar
     *  @param  procNumber number of process on the system
     *  @param  info to the other nodes
     */
    public Middlewar(Integer procNumber, DistSystem info) {
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

        this.barrierId = 0;
        this.reduceId = 0;

        this.getMailbox       = new LinkedBlockingQueue<String>();
        this.barrierMailbox   = new LinkedBlockingQueue<String>();
        this.continueMailbox  = new LinkedBlockingQueue<String>();
        this.andRedMailbox    = new LinkedBlockingQueue<String>();
        this.andRedRspMailbox = new LinkedBlockingQueue<String>();


        this.registry = new HashMap<String,DistributedArray>();
    }   

    /**
     *  This method autosend an especial message to finish the listener
     */ 
    public void finish(){
        this.ear.finish();
        this.sendTo(this.procId, MessageType.END.toString() + " ");
    }

    /**
     *  Add an entry to the regisrty of arrays handled by this middlewar
     */
    public void bind(String name, DistributedArray ref){
        this.registry.put(name,ref);
    }

    /**
     *  @param name of the array on the system.
     *  @return the Distributed Array with that name.
     */
    public DistributedArray getArray(String name){
        return this.registry.get(name);
    }

    /**
     *  @return True: iff this instance coordinate all the tasks
     */
    private boolean iAmCoordinator(){
        return this.procId == this.coordinator;
    }

    /**
     *  Method to ask if another process is the last on the system
     *  @return True iff procId is the last on the system.
     */
    public boolean isTheLast(Integer procId){
        return procId == this.system.size() - 1;
    }
    
    /**
     *  @retrun True iff is the last process on the system
     */
    public boolean iAmLast(){
        return this.procId == this.system.size() - 1;
    }
    
    /**
     *  @return the number of this process
     */ 
    public Integer whoAmI(){
        return this.procId;
    }

    /**
     *  The coordinator send a message to all the other process
     *  @param message to send
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
    private void waitForAll(MessageType msgType){
        if (!this.iAmCoordinator()) 
            throw new IllegalStateException("Error: only a coordinator can wait for all the others process");
        
        for (int i = 1; i < this.system.size(); i++ ) {
            this.receiveFrom(i,msgType);
        }
    }

    /**
     *  the first process coordinates that everyone arrives at the barrier 
     */
    public void barrier(){
        if (this.iAmCoordinator()) {
            this.waitForAll(MessageType.BARRIER);
            this.sendAll(MessageType.CONTINUE.toString()+ " " + this.barrierId + " " + this.procId + " ");
        }else {
            this.sendTo(this.coordinator,MessageType.BARRIER.toString() + " " + this.barrierId + " " +this.procId + " " );
            this.receiveFrom(this.coordinator,MessageType.CONTINUE);
        }   
        this.barrierId ++;
    }

    /**
     *  
     */
    private Boolean waitForAllAndRed(MessageType msgType,Boolean aVariable){
        if (!this.iAmCoordinator()) 
            throw new IllegalStateException("Error: only a coordinator can wait for all the others process");
        
        for (int i = 1; i < this.system.size(); i++ ) {
            String message = this.receiveFrom(i,msgType);
            aVariable = aVariable && Message.getBoolean(message);
        }
        return aVariable;
    }

    /**
     *  Find the value of a variable on every process and make an AND operation
     *  @return True iff every process finish
     */
    public Boolean andReduce(Boolean aVariable){
        String rsp = null;
        Boolean result;
        if (this.iAmCoordinator()) {
            result = this.waitForAllAndRed(MessageType.ANDREDUCE,aVariable);

            this.sendAll(MessageType.ANDREDUCERSP.toString()+ " " + this.reduceId + " " + this.procId + " " + result.toString() + " ");
        }else {
            this.sendTo(this.coordinator,MessageType.ANDREDUCE.toString() + " " + this.reduceId + " " + this.procId + " " + aVariable.toString() + " " );
            rsp = this.receiveFrom(this.coordinator,MessageType.ANDREDUCERSP);
            result = Message.getBoolean(rsp);
        }   
        this.reduceId ++;
        return result;
    }   

    /**
     *  This method send a message to another process
     *  @param procNumber receiver process
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
     *  This method wait for a message.
     *  @return the message received 
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
    public String receiveFrom(Integer procNumber, MessageType type){
        return this.dequeueMailFrom(procNumber, type);
    }

    /**
     *  
     */
    public void enqueueMail(String message){
        BlockingQueue<String> queue = getQueue(message);
        try{
            queue.put(message);
        }catch(InterruptedException e){
            System.out.println("Enqueue mail has failed");
            e.printStackTrace();
        }
    }

    /**
     *  
     */
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

    /**
     *  
     */
    private String dequeueMailFrom(Integer id, MessageType type){
        String message = "";
        boolean find = false;
        BlockingQueue<String> queue = getQueue(type);
        while (!find) {
            message = this.dequeueMail(queue);
            if (Message.whoSendIt(message) == id) {
                find = true;
            }else {
                this.enqueueMail(message);
            }
        }
        return message;
    }

    /**
     *  
     */
    private  BlockingQueue<String> getQueue(MessageType type){
        BlockingQueue<String> queue = null;
        switch (type) {
            case GETR:
                // System.out.println("GETR");
                queue = this.getMailbox;
                break;
            case BARRIER:
                // System.out.println("BARRIER");
                queue = this.barrierMailbox;
                break;
            case CONTINUE:
                // System.out.println("CONTINUE");
                queue = this.continueMailbox;
                break;
            case ANDREDUCE:
                // System.out.println("ANDREDUCE");
                queue = this.andRedMailbox;
                break;
            case ANDREDUCERSP:
                // System.out.println("ANDREDUCERSP");
                queue = this.andRedRspMailbox;
                break;
            default:
                System.out.println("Panic !!! There is no queue for this kind of message: "+ type);
                throw new IllegalStateException("Can not enqueue this kind of messages: " + type);
        }

        return queue;
    }

    /**
     *  
     */
    private BlockingQueue<String> getQueue(String message){
        return getQueue(Message.getType(message));
    }


}
