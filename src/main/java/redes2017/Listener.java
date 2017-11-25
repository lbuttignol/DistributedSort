package redes2017;
public class Listener extends Thread{

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
			System.out.println(message);

		}
	}
	

	// esta clase tiene que tener el dispacher paraa manejar los sets y gets
	// sobre el arreglo distribuido

	// y además 

}