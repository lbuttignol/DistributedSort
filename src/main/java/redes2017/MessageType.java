package redes2017;


public enum MessageType{
	END,

	/**
	 *	A GET message is to ask to someone a remote value, this message must have the 
	 *  following structure (all message attributes must be separated by a space " "):
	 *
	 *	GET arrayName  globalIndex  procSender
	 */
	GET,

	/**
	 *	A GETR message is to answer a GET message, this message must have the 
	 *	following structure (all message attributes must be separated by a space " "):
	 *
	 *	GETR globalIndex  procSender  value
	 */
	GETR,

	/**
	 *	A SET message is to modify a remote value on the array, this message must
	 *	have the following structure (all message attributes must be separated by a space " "):
	 *
	 *	SET arrayName  globalIndex  value
	 */
	SET,
	
	/**
	 *	A BARRIER message is to synchronize process execution, this message must
	 *	have the following structure (all message attributes must be separated by a space " "):
	 *
	 *	BARRIER barrierId  procSender 
	 */ 
	BARRIER,

	/**
	 *	A CONTINUE message is the answer to a BARRIER message, to continue the 
	 *	program execution, this message must have the following structure (all
	 * 	message attributes must be separated by a space "");
	 *
	 *	CONTINUE  barrierId  procSender		
	 */
	CONTINUE,

	/**
	 *	An ANDREDUCE message is to calculate a distributed AND form one variable
	 * 	over remotes process, this message must have the following structure
	 *  (all message attributes must be separated by a space "");
	 *	
	 *	ANDREDUCE  reduceId  procSender  booleanVariableValue
	 */
	ANDREDUCE,

	/**
	 *	An ANDREDUCERSP message is the answer to an ANDREDUCE message, this
	 *	message must have the following structure (all message attributes must
	 * 	be separated by a space "");
	 *
	 *	ANDREDUCERSP reduceId  procSender booleanResult
	 */
	ANDREDUCERSP;

}
