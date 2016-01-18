/*
 * Created on 07.06.2005
 *
 * CVS header information:
 * $RCSfile: OptionPaneParameters.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:46 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/OptionPaneParameters.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.Frame;

import javax.swing.JOptionPane;

import pirolPlugIns.i18n.PirolPlugInMessages;

/**
 * Helper Class to create messages via {@link javax.swing.JOptionPane}.
 * @author Stefan Ostermann
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck
 */
public class OptionPaneParameters {
	public static final String ERROR_TITLE = PirolPlugInMessages.getString("error")+":";
	public static final String WARNING_TITLE = PirolPlugInMessages.getString("warning")+": ";
	public static final String INFORMATION_TITLE = PirolPlugInMessages.getString("information")+": ";
	public static final String QUESTION_TITLE = PirolPlugInMessages.getString("question")+": ";
	
	protected String message, title;
	protected int messageType = JOptionPane.ERROR_MESSAGE;
	
	/** Constructor.
	 * 
	 * @param message the message to show
	 * @param title the title of the message window
	 * @param messageType type of message, e.g. JOptionPane.ERROR_MESSAGE
	 */
	public OptionPaneParameters(String message, String title, int messageType) {
		this.message = message;
		this.title = title;
		this.messageType = messageType;
	}
	
	/**
	 * This constructor automatically sets the title according to the message type.
	 * 
	 * @param message the message to show
	 * @param messageType type of message, e.g. JOptionPane.ERROR_MESSAGE
	 */
	public OptionPaneParameters(String message, int messageType) {
		this.message = message;
		this.messageType = messageType;
		switch (messageType) {
			case JOptionPane.ERROR_MESSAGE:
				title = ERROR_TITLE;
			break;
			case JOptionPane.WARNING_MESSAGE:
				title = WARNING_TITLE;
			break;
			case JOptionPane.INFORMATION_MESSAGE:
				title = INFORMATION_TITLE;
			break;
			case JOptionPane.QUESTION_MESSAGE:
				title = QUESTION_TITLE;
			break;
			default:
				title = "";
			break;
				
		}
	}
	
	public OptionPaneParameters(String message) {
		this(message,JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @return Returns the messageType.
	 */
	public int getMessageType() {
		return messageType;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	
	public void showDialog() {
		Object[] options = {PirolPlugInMessages.getString("ok")};
		
		JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, options, options[0]);
	}
	
	public boolean showOkCancelDialog() {
		Object[] options = {PirolPlugInMessages.getString("ok"), PirolPlugInMessages.getString("cancel")};
		int num = JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, options, options[0]);
		if (num==0) {
			return true;
		}
		return false;
	}
}
