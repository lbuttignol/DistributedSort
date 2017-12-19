package redes2017;

/**
 *  This is the thread that listen the messages that arrives to a
 *  Middlewar.
 *  @see redes2017.Middlewar.java
 */
public class Listener extends Thread{

    /**
     *  Middleware to give service.
     */
    private Middlewar master;

    /**
     *  Used to make that the listener end his task.
     */
    private volatile boolean finish;

    /**
     *  Default constructor of a Listener
     */ 
    public Listener(Middlewar m){
        this.master = m;
        this.finish = false;
    }

    /**
     *  This process make that the Thread end.
     */
    public void finish(){
        this.finish = true;
        //  Send an especial message to the socket to finish.
    }

    /**
     *  Listen every message 
     */
    @Override
    public void run() {
        
        while(!this.finish){
            String message = this.master.receive();

            String arrayName;
            Integer index, val;
            DistributedArray arr;
            Integer sender;
            switch (MessageType.valueOf(Message.getStringParam(message,0))) {
                case GET: 
                    // System.out.println("is a GET");
                    arrayName = Message.getStringParam(message,1);
                    index = Message.getIntParam(message, 2);
                    sender = Message.getIntParam(message, 3);
                    arr = this.master.getArray(arrayName);
                    Integer result = arr.get(index);
                    this.master.sendTo(sender, MessageType.GETR.toString() + " " + index + " " + this.master.whoAmI() + " " + result + " ");
                    break;

                case SET: 
                    // System.out.println("is a SET");
                    arrayName = Message.getStringParam(message, 1);
                    index = Message.getIntParam(message, 2);
                    arr = this.master.getArray(arrayName);
                    val = Message.getIntParam(message, 3);
                    arr.set(index,val);
                    break;

                case GETR: 
                    // System.out.println("is a GETR");
                    this.master.enqueueMail(message);
                    break;

                case BARRIER:
                    // System.out.println("is a BARRIER");
                    this.master.enqueueMail(message);
                    break;

                case CONTINUE:
                    // System.out.println("is a CONTINUE");
                    this.master.enqueueMail(message);
                    break;

                case ANDREDUCE: 
                    // System.out.println("is a ANDREDUCE");
                    this.master.enqueueMail(message);
                    break;

                case ANDREDUCERSP: 
                    // System.out.println("is a ANDREDUCERSP");
                    this.master.enqueueMail(message);
                    break;

                case END: 
                    // System.out.println("Bye Bye");
                    this.finish = false;
                    break;

                default:    System.out.println("Panic! on the Listener");
                    break;
                
            }


        }
    }
    
}