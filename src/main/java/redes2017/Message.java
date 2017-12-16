package redes2017;

public class Message{
	/**
	 *	
	 */
	public static String[] parse(String message){
		String delims = "[ ]+";
		return message.split(delims);
	}

	/**
	 *	
	 */
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

	/**
	 *	
	 */
	public static String getStringParam(String message, Integer paramNum){
		String[] parsedMessage = Message.parse(message);
		try{
			return parsedMessage[paramNum].trim();
		}catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 *	
	 */
	public static Integer whoSendIt(String message){
		Integer result = null;
		switch (MessageType.valueOf(Message.getStringParam(message,0))) {
			case GETR:
				result = Message.getIntParam(message, 2);
				break;
			case BARRIER:
				result = Message.getIntParam(message, 2);
				break;
			case CONTINUE:
				result = Message.getIntParam(message, 2);
				break;
			case ANDREDUCE:
				result = Message.getIntParam(message, 2);
				break;
			case ANDREDUCERSP:
				result = Message.getIntParam(message, 2);
				break;
			default: 
				System.out.println("Panic !!! Message without sender");
				throw new IllegalStateException("Can not find the sender on this kind of message");
			
		}
		return result;
	}

	/**
	 *	
	 */
	public static MessageType getType(String message){
		return MessageType.valueOf(Message.getStringParam(message, 0));
	}

	/**
	 *	
	 */
	public static Boolean getBoolean(String message){
		if (Message.getType(message) == MessageType.ANDREDUCE || Message.getType(message) == MessageType.ANDREDUCERSP) {
			return new Boolean(Boolean.parseBoolean(Message.getStringParam(message, 3)));
		}else {
			throw new IllegalStateException("this kind of message do not have a boolean parameter");
		}
	}
}

 
