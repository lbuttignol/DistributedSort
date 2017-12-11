package redes2017;

/**
 *	This is the thread that listen the messages that arrives to a
 *  Middlewar.
 *	@see redes2017.Middlewar.java
 */
public class Listener extends Thread{

	/**
	 *	Middleware to give service.
	 */
	private Middlewar master;


	private boolean finish;

	/**
	 *	Default constructor of a Listener
	 */ 
	public Listener(Middlewar m){
		this.master = m;
		this.finish = false;
	}

	/**
	 *	This process make that the Thread end.
	 */
	public void finish(){
		this.finish = true;
		// 	Send an especial message to the socket to finish.
	}

	/**
	 *	Listen every message 
	 */
	@Override
	public void run() {
		
		while(!this.finish){
			String message = this.master.receive();
			System.out.println("listener receive");
			System.out.println(message);

			String[] parsedMessage = Message.parse(message);
			
			String arrayName;
			Integer index, val;
			DistributedArray arr;
			Integer sender;
			switch (MessageType.valueOf(parsedMessage[0].trim())) {
				case GET: 
					System.out.println("is a get");
					arrayName = parsedMessage[1];
					index = Integer.parseInt(parsedMessage[2]);
					sender = Integer.parseInt(parsedMessage[3]);
					arr = this.master.getArray(arrayName);
					Integer result = arr.get(index);
					System.out.println("the value is " + result);
					this.master.sendTo(sender, MessageType.GETRSP.toString() + " " + index + " " + result + " " + this.master.whoAmI() + " ");
					break;

				case GETRSP: 
					System.out.println("is a getrsp");
					this.master.enqueueMail(message);
					break;

				case SET: 
					System.out.println("is a set");
					arrayName = parsedMessage[1];
					index = Integer.parseInt(parsedMessage[2]);
					arr = this.master.getArray(arrayName);
					val = Integer.parseInt(parsedMessage[3].trim());
					arr.set(index,val);
					break;

				case SETRSP: System.out.println("is a setrsp");	
					break;

				case BARRIER:System.out.println("is a barrier");
					break;

				case CONTINUE: System.out.println("is a CONTINUE");
					break;

				case REDUCE: System.out.println("is a REDUCE");
					break;

				case REDUCERSP: System.out.println("is a REDUCERSP");
					break;

				case END: System.out.println("Bye Bye");
					break;

				default:	System.out.println("Panic! on the Listener");
					break;
				
			}

			//enqueue some messages and othes must to be dispatched 

		}
	}
	

	// esta clase tiene que tener el dispacher paraa manejar los sets y gets
	// sobre el arreglo distribuido

	// y además 

}