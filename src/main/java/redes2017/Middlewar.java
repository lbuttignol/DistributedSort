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
 *  A middlewar is responsable for the communication of a distributed node.
 *  Should know completely the system to establish all the communication    
 */
public class Middlewar extends Thread {

    /**
     *  Process identification number.
     */
    private Integer procId;

    /**
     *  Distributed system information. Know process quantity and his location. 
     */
    private DistSystem system;

    /**
     *  Communication channel with the other processes
     */
    private DatagramSocket socket;

    /**
     *  Thread that listen and deliver every message
     */
    private Listener ear;

    /**
     *  Process that coordinate important task like barriers
     */
    private static Integer coordinator = 0;

    /**
     *  Contains the info of the arrays that can call
     */
    private HashMap<String,DistributedArray> registry;

    /**
     *  Barrier identification number 
     */
    private Integer barrierId;
    
    /**
     *  Reduce identification number 
     */
    private Integer reduceId;
    
    /**
     *  Queue for GETR messages
     */
    private BlockingQueue<String> getMailbox;

    /**
     *  Queue for BARRIER messages
     */
    private BlockingQueue<String> barrierMailbox;

    /**
     *  Queue for CONTINUE messages
     */
    private BlockingQueue<String> continueMailbox;

    /**
     *  Queue for ANDREDUCE messages
     */
    private BlockingQueue<String> andRedMailbox;

    /**
     *  Queue for ANDREDUCERSP messages
     */
    private BlockingQueue<String> andRedRspMailbox;

    private static final Integer BUFFSIZE = 2048;

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
     *  @return the info of the system
     */
    public DistSystem getSys(){
        return this.system;
    }

    /**
     *  This method auto send an especial message to finish the listener loop 
     *  and end the process.
     */ 
    public void finish(){
        this.ear.finish();
        this.sendTo(this.procId, MessageType.END.toString() + " ");
    }

    /**
     *  Add an entry to the registry of arrays handled by this middlewar.
     *  @param name, internal array name to bind on this middlewar.
     *  @param ref, distributed array to store.
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
     *  Is a synchronization method. The coordinator, coordinates that everyone
     *  arrives at the barrier waiting for BARRIER messages, when all the messages
     *  are received, then the coordinator send a CONTINUE message to all the 
     *  nodes to follow the execution. If a no coordinator process run this method
     *  then send a BARRIER message to the coordinator and then wait for a CONTINUE
     *  message from the coordinator.
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
     *  Search a value of a boolean variable and make an AND operation, then 
     *  return the AND result.
     *  @param aVariable boolean variable to make a remote AND.
     *  @return True iff aVariable is true on every node.
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
     *  This method wait for a specific message from procNumber
     *  @param procNumber message sender
     *  @param type kind of message to receive.
     *  @return the message received from procNumber. 
     */
    public String receiveFrom(Integer procNumber, MessageType type){
        return this.dequeueMailFrom(procNumber, type);
    }

    /**
     *  Put a message on his queue
     *  @param message to enqueue
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
     *  @return True: iff this instance coordinate all the tasks that requires
     *  a coordinator process  
     */
    private boolean iAmCoordinator(){
        return this.procId == this.coordinator;
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
     *  This method suspend the coordinator thread until all the nodes sends to
     *  the coordinator a msgType message
     *  @param msgType kind of message to wait from all the nodes.
     */
    private void waitForAll(MessageType msgType){
        if (!this.iAmCoordinator()) 
            throw new IllegalStateException("Error: only a coordinator can wait for all the others process");
        
        for (int i = 1; i < this.system.size(); i++ ) {
            this.receiveFrom(i,msgType);
        }
    }

    /**
     *  A version of waitForAll to the andReduce method. Wait for a message 
     *  from all the nodes that contains a boolean variable, and make and AND
     *  with this results
     *  @return true iff aVariable is true and all the message has a true parameter
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
     *  Given a queue, take a message from it an return this message
     *  @param aMailbox queue to use.
     *  @return a message from aMailbox queue
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
     *  This method dequeue messages until find one sent by id, if the message
     *  dequeue do not came from id then enqueue it again.
     *  @param id of the sender expected 
     *  @param type of the message expected
     *  @return a type message prom id 
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
     *  Given a MessageType return the queue where put or take this kind of message
     *  @param essageType to enqueue or dequeue     
     *  @return the queue that has this message type
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
     *  Given a message return the queue where put or take this kind of message
     *  @param message to enqueue
     *  @return a queue where puts this kind of message
     */
    private BlockingQueue<String> getQueue(String message){
        return getQueue(Message.getType(message));
    }


}
