package redes2017;

public class Message{
	
	public static String[] parse(String message){
		String delims = "[ ]+";
		return message.split(delims);
	}

	public static Integer getIntParam(String message, Integer paramNum){
		String[] parsedMessage = Message.parse(message);
		try{
			return Integer.parseInt(parsedMessage[paramNum].trim());
		}catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}catch (NumberFormatException e) {
			return null;
		}
	}

	public static String getStringParam(String message, Integer paramNum){
		String[] parsedMessage = Message.parse(message);
		try{
			return parsedMessage[paramNum].trim();
		}catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public static Integer whoSendIt(String message){
		Integer result = null;
		switch (MessageType.valueOf(Message.getStringParam(message,0))) {
			case GETR:
				result = Message.getIntParam(message,2);
				break;
			case BARRIER;
				result = 5555555;
				break;
			
		}
		return result;
	}
}

 
