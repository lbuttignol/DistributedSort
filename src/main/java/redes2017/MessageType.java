package redes2017;


public enum MessageType{
	END,

	/**
	 *	A GET message is to ask to someone a remote value, this message must have the 
	 *  folowing structure (all message attributes must be separated by a space " "):
	 *
	 *	GET arrayName  globalIndex  procSender
	 */
	GET,

	/**
	 *	A GETR message is to answer a GET message, this message must have the 
	 *	folowing structure (all message attributes must be separated by a space " "):
	 *
	 *	GETR globalIndex  procSender  value
	 */
	GETR,

	/**
	 *	A SET message is to modify a remote value on the array, this message must
	 *	have the folowing structure (all message attributes must be separated by a space " "):
	 *
	 *	SET arrayName  globalIndex  value
	 */
	SET,
	
	/**
	 *	A BARRIER message is to synchronise process execution, this message must
	 *	have the folowing structure (all message attributes must be separated by a space " "):
	 *
	 *	BARRIER barrierId  procSender 
	 */ 
	BARRIER,

	/**
	 *	A CONTINUE message is the answer to a BARRIER message, to continue the 
	 *	program execution, this message nust have the following structure (all
	 * 	message attributes must be separated by a space "");
	 *
	 *	CONTINUE  barrierId  procSender		
	 */
	CONTINUE,

	// reduce 				hay que 
	REDUCE,
	// reduceresp n
	REDUCERSP;

}
