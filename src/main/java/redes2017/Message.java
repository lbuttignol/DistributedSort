package redes2017;

public class Message{
	
	 public static String[] parse(String message){
	 	String delims = "[ ]+";
	 	return message.split(delims);
	 } 
}

 
