package classes;

import java.util.Calendar;

public class Input {
	
	private static final char startCharacter = '&';
	
	private static final String deviceName = "Computer";
	private static final String messageCharacter = "[M]";
	
	public static final int MES_UNKNOWN = -1;
	public static final int MES_MESSAGE = 0;
	public static final int MES_REGISTER = 1;
	
	private Calendar time;
	private String sender;
	private String command;
	private String message;
	
	private int messageType;
	
	public Input(String inputMsg) {
		time = Calendar.getInstance();
		convert(inputMsg);
	}
	
	private void convert(String inputMsg) {
		
		sender = "?";
		command = "?";
		message = "UNKNOWN";
		messageType = MES_UNKNOWN;
		
		if (inputMsg.isEmpty() || inputMsg == null) return;
		
		if (inputMsg.charAt(0) != startCharacter) return;
		// Remove first (start)character
		inputMsg = inputMsg.substring(1);
		
		int length = inputMsg.length();
		
		// Check message type
		String type = inputMsg.substring(0,3);
		switch(type) {
		case messageCharacter: messageType = MES_MESSAGE; break;
		default: messageType = MES_UNKNOWN; return;
		}
		
		// Remove type
		inputMsg = inputMsg.substring(3, length-1);
		
		// Split 
		String[] m = inputMsg.split("\\:");
		
		this.sender = m[0];
		this.command = m[1];
		this.message = m[2];
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
