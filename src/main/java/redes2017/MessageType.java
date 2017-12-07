package redes2017;


public enum MessageType{
	END,
	GET,
	GETRSP,

	SET,
	SETRSP,		// Not necesary
	
	BARRIER,
	CONTINUE,

	REDUCE,
	REDUCERSP;

}
// get arrayname indexglobal 					hacer distpaching a get del array
// getresponse indexglobal valor 				hay que despertar siempre
// set arrayname indexglobal value				hacer distpaching a set del array

// barrier b 			hay que encolar el mensaje 
// continue				hay que despertar siempre 

// reduce 				hay que 
// reduceresp n