package redes2017;
/**
 *  This class has some static methods useful to handle messages
 */
public class Message{
    /**
     *  @param message to parse
     *  @return a list of strings that results from parse the message.
     */
    public static String[] parse(String message){
        String delims = "[ ]+";
        return message.split(delims);
    }

    /**
     *  @param message to analyze
     *  @param paramNum parameter to recover from message
     *  @return the parameter paramNum from message as an Integer
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
     *  @param message to analyze
     *  @param paramNum parameter to recover from message
     *  @return the parameter paramNum from message as a String
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
     *  @param message to analyze
     *  @return the procId of message sender (if the message has a sender).
     */
    public static Integer whoSendIt(String message){
        Integer result = null;
        switch (MessageType.valueOf(Message.getStringParam(message,0))) {
            case GETR:
            case BARRIER:
            case CONTINUE:
            case ANDREDUCE:
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
     *  @param message to analyze
     *  @return The type of the message
     */
    public static MessageType getType(String message){
        return MessageType.valueOf(Message.getStringParam(message, 0));
    }

    /**
     *  @param message to analyze
     *  @return Boolean value on the message, only ANDREDUCE and ANDREDUCERSP
     *  has this function
     *  @see MessageType.java
     */
    public static Boolean getBoolean(String message){
        if (   Message.getType(message) == MessageType.ANDREDUCE 
            || Message.getType(message) == MessageType.ANDREDUCERSP) {
            return new Boolean(Boolean.parseBoolean(Message.getStringParam(message, 3)));
        }else {
            throw new IllegalStateException("this kind of message do not have a boolean parameter");
        }
    }
}

 
