/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * I18nOptionPane.java
 * 
 * Created on 15/05/2004
 *
 */

package it.businesslogic.ireport.util;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 * A internacionalization support for all static methods of JOptionPane. 
 * 
 * @author Egon
 *
 */
public class I18nOptionPane {

	/**
	 * Couldn't be instancied. 
	 */
	private I18nOptionPane() {
		super();
	}

	/**
	 * Brings up an internal confirmation dialog panel. The dialog is a information-message dialog titled "Message".  
	 * 
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 */
	public static void showInternalMessageDialog(Component parentComponent, String messageCID){
		
		String message = messageCID;
		
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		JOptionPane.showInternalMessageDialog(parentComponent, message);
	}

	/**
	 * Brings up an internal dialog panel that displays a message using a default icon determined by the messageType parameter.  
	 * @param parentComponent the parent Component for the dialog
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param messageType the type of message that is to be displayed: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 */
	public static void showInternalMessageDialog(Component parentComponent, String messageCID, String titleCID, int messageType){
		
		String message = messageCID;
		
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		JOptionPane.showInternalMessageDialog(parentComponent, message, title, messageType);
	}

	/**
	 * Brings up a dialog displaying a message, specifying all parameters. 
	 * @param parentComponent the parent Component for the dialog
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param messageType the type of message that is to be displayed: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @param icon the icon to display in the dialog
	 */
	public static void showInternalMessageDialog(Component parentComponent, String messageCID, String titleCID, int messageType, Icon icon){
		
		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		JOptionPane.showInternalMessageDialog(parentComponent, message, title, messageType, icon);
	}


	/**
	 * Brings up an internal dialog panel with the options Yes, No and Cancel; with the title, Select an Option.   
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 */
	public static int showInternalConfirmDialog(Component parentComponent,
										String messageCID)
	{

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		return JOptionPane.showInternalConfirmDialog(parentComponent, message);
	}

	/**
	 * Brings up a internal dialog panel where the number of choices is determined by the optionType parameter.    
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param optionType an int designating the options available on the dialog: YES_NO_OPTION, or YES_NO_CANCEL_OPTION  
	 */
	public static int showInternalConfirmDialog(Component parentComponent,
										String messageCID, String titleCID,
										int optionType)
	{

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		return JOptionPane.showInternalConfirmDialog(parentComponent, message, title, optionType);
	}
	
	/**
	 * Brings up an internal dialog panel where the number of choices is determined by the optionType parameter, where the messageType parameter determines the icon to display. The messageType parameter is primarily used to supply a default icon from the Look and Feel.    
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param optionType an int designating the options available on the dialog: YES_NO_OPTION, or YES_NO_CANCEL_OPTION  
	 * @param messageType an integer designating the kind of message this is, primarily used to determine the icon from the pluggable Look and Feel: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 */
	public static int showInternalConfirmDialog(Component parentComponent,
										String messageCID, String titleCID,
										int optionType, int messageType)
	{

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		return JOptionPane.showInternalConfirmDialog(parentComponent, message, title, optionType, messageType);
	}
	
	/**
	 * Brings up an internal dialog panel with a specified icon, where the number of choices is determined by the optionType parameter. The messageType parameter is primarily used to supply a default icon from the look and feel.     
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param optionType an int designating the options available on the dialog: YES_NO_OPTION, or YES_NO_CANCEL_OPTION  
	 * @param messageType an integer designating the kind of message this is, primarily used to determine the icon from the pluggable Look and Feel: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @param icon the icon to display in the dialog
	 */
	public static int showInternalConfirmDialog(Component parentComponent,
										String messageCID, String titleCID,
										int optionType, int messageType, Icon icon)
	{

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		return JOptionPane.showInternalConfirmDialog(parentComponent, message, title, optionType, messageType, icon);
	}
	
	/**
	 * Brings up an internal dialog panel with a specified icon, where the initial choice is determined by the initialValue parameter and the number of choices is determined by the optionType parameter. 
	 * If optionType is YES_NO_OPTION, or YES_NO_CANCEL_OPTION and the options parameter is null, then the options are supplied by the Look and Feel. 
	 * 
	 * The messageType parameter is primarily used to supply a default icon from the look and feel. 
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for a title to be displayed.
	 * @param optionType an integer designating the options available on the dialog: YES_NO_OPTION, or YES_NO_CANCEL_OPTION
	 * @param messageType an integer designating the kind of message this is, primarily used to determine the icon from the pluggable Look and Feel: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @param icon the icon to display in the dialog
	 * @param optionsCID an array of message CIDs indicating the possible choices the user can make
	 * @param initialCIDValue the message CID that represents the default selection for the dialog; only meaningful if options is used; can be null
	 * @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog 
	 */
	public static int showInternalOptionDialog(Component parentComponent,
									   String messageCID,
									   String titleCID,
									   int optionType,
									   int messageType,
									   Icon icon,
									   String[] optionsCID,
									   String initialCIDValue)
		{
   	
		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
								   	
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}
									   	
		String initialValue = initialCIDValue;
		if(initialCIDValue != null){
			initialValue = it.businesslogic.ireport.util.I18n.getString(initialCIDValue, initialCIDValue);
		}

		String[] options = optionsCID;
		if(optionsCID != null){
			for(int i = 0; i < optionsCID.length; i++){
				if (optionsCID[i] != null){
					options[i] = it.businesslogic.ireport.util.I18n.getString(optionsCID[i], optionsCID[i]);
				}
			}
		}
									   	
		return JOptionPane.showInternalOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue);			   	
	}

	/**
	 * Shows an internal question-message dialog requesting input from the user parented to parentComponent. The dialog is displayed in the Component's frame, and is usually positioned below the Component. 
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @return
	 */
	public static String showInternalInputDialog(Component parentComponent, String messageCID){
		
		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		return JOptionPane.showInternalInputDialog(parentComponent, message);
	}

	/**
	 * Prompts the user for input in a blocking internal dialog where the initial selection, possible selections, and all other options can be specified. The user will able to choose from selectionValues, where null implies the user can input whatever they wish, usually by means of a JTextField. initialSelectionValue is the initial value to prompt the user with. It is up to the UI to decide how best to represent the selectionValues, but usually a JComboBox, JList, or JTextField will be used.  
	 * @param parentComponent the parent Component for the dialog
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param messageType the type of message that is to be displayed: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @param icon the Icon image to display
	 * @param selectionCIDValues  message country identifications for an array of strings that gives the possible selections
	 * @param initialSelectionCIDValue message country identification for the value used to initialize the input field.
	 * @return
	 */
	public static String showInternalInputDialog(Component parentComponent,
										 String messageCID,
										 String titleCID,
										 int messageType,
										 Icon icon,
										 String[] selectionCIDValues,
										 String initialSelectionCIDValue)
	{
		
		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		String initialSelectionValue = initialSelectionCIDValue;
		if(initialSelectionCIDValue != null){
			initialSelectionValue = it.businesslogic.ireport.util.I18n.getString(initialSelectionCIDValue, initialSelectionCIDValue);
		}

		String[] selectionValues = selectionCIDValues;
		if(selectionCIDValues != null){
			for(int i = 0; i < selectionCIDValues.length; i++){
				if (selectionCIDValues[i] != null){
					selectionValues[i] = it.businesslogic.ireport.util.I18n.getString(selectionCIDValues[i], selectionCIDValues[i]);
				}
			}
		}

		return (String) JOptionPane.showInternalInputDialog(parentComponent, message, title, messageType, icon, selectionValues, initialSelectionValue);
	}

	/**
	 * Brings up an information-message dialog titled with the message associated with the Country ID resource bundle. 
	 * 
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 */
	public static void showMessageDialog(Component parentComponent, String messageCID){
		
		String message = messageCID;
		
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		JOptionPane.showMessageDialog(parentComponent, message);
	}

	/**
	 * Shows an internal dialog requesting input from the user parented to parentComponent with the dialog having the title title and message type messageType.    
	 * @param parentComponent the parent Component for the dialog
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param messageType the type of message that is to be displayed: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @return
	 */
	public static String showInternalInputDialog(Component parentComponent, String messageCID, String titleCID, int messageType){

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		return JOptionPane.showInternalInputDialog(parentComponent, message, title, messageType);
	}


	/**
	 * Brings up a dialog that displays a message using a default icon determined by the messageType parameter. 
	 * @param parentComponent the parent Component for the dialog
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param messageType the type of message that is to be displayed: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 */
	public static void showMessageDialog(Component parentComponent, String messageCID, String titleCID, int messageType){
		
		String message = messageCID;
		
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
	}

	/**
	 * Brings up a dialog displaying a message, specifying all parameters. 
	 * @param parentComponent the parent Component for the dialog
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param messageType the type of message that is to be displayed: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @param icon the icon to display in the dialog
	 */
	public static void showMessageDialog(Component parentComponent, String messageCID, String titleCID, int messageType, Icon icon){
		
		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		JOptionPane.showMessageDialog(parentComponent, message, title, messageType, icon);
	}


	/**
	 * Brings up a dialog with the options Yes, No and Cancel; with the title, Select an Option.  
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 */
	public static int showConfirmDialog(Component parentComponent,
										String messageCID)
	{

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		return JOptionPane.showConfirmDialog(parentComponent, message);
	}

	/**
	 * Brings up a dialog where the number of choices is determined by the optionType parameter.   
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param optionType an int designating the options available on the dialog: YES_NO_OPTION, or YES_NO_CANCEL_OPTION  
	 */
	public static int showConfirmDialog(Component parentComponent,
										String messageCID, String titleCID,
										int optionType)
	{

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType);
	}

	/**
	 * Brings up a dialog where the number of choices is determined by the optionType parameter, where the messageType parameter determines the icon to display. The messageType parameter is primarily used to supply a default icon from the Look and Feel.   
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param optionType an int designating the options available on the dialog: YES_NO_OPTION, or YES_NO_CANCEL_OPTION  
	 * @param messageType an integer designating the kind of message this is, primarily used to determine the icon from the pluggable Look and Feel: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 */
	public static int showConfirmDialog(Component parentComponent,
										String messageCID, String titleCID,
										int optionType, int messageType)
	{

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType, messageType);
	}

	/**
	 * Brings up a dialog with a specified icon, where the number of choices is determined by the optionType parameter. The messageType parameter is primarily used to supply a default icon from the look and feel.    
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param optionType an int designating the options available on the dialog: YES_NO_OPTION, or YES_NO_CANCEL_OPTION  
	 * @param messageType an integer designating the kind of message this is, primarily used to determine the icon from the pluggable Look and Feel: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @param icon the icon to display in the dialog
	 */
	public static int showConfirmDialog(Component parentComponent,
										String messageCID, String titleCID,
										int optionType, int messageType, Icon icon)
	{

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType, messageType, icon);
	}


	/**
	 * Brings up a dialog with a specified icon, where the initial choice is determined by the initialValue parameter and the number of choices is determined by the optionType parameter. 
	 * If optionType is YES_NO_OPTION, or YES_NO_CANCEL_OPTION and the options parameter is null, then the options are supplied by the look and feel. 
	 *
	 * The messageType parameter is primarily used to supply a default icon from the look and feel.
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for a title to be displayed.
	 * @param optionType an integer designating the options available on the dialog: YES_NO_OPTION, or YES_NO_CANCEL_OPTION
	 * @param messageType an integer designating the kind of message this is, primarily used to determine the icon from the pluggable Look and Feel: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @param icon the icon to display in the dialog
	 * @param optionsCID an array of message CIDs indicating the possible choices the user can make
	 * @param initialCIDValue the message CID that represents the default selection for the dialog; only meaningful if options is used; can be null
	 * @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog 
	 */
	public static int showOptionDialog(Component parentComponent,
									   String messageCID,
									   String titleCID,
									   int optionType,
									   int messageType,
									   Icon icon,
									   String[] optionsCID,
									   String initialCIDValue
                                                                           )
        {                                                                           
            String firstLine = "";
            
		return showOptionDialog(parentComponent,
									   messageCID,
									   titleCID,
									   optionType,
									   messageType,
									   icon,
									   optionsCID,
									   initialCIDValue,
                                                                           firstLine);
	}
        
        
	/**
	 * Brings up a dialog with a specified icon, where the initial choice is determined by the initialValue parameter and the number of choices is determined by the optionType parameter. 
	 * If optionType is YES_NO_OPTION, or YES_NO_CANCEL_OPTION and the options parameter is null, then the options are supplied by the look and feel. 
	 *
	 * The messageType parameter is primarily used to supply a default icon from the look and feel.
	 * @param parentComponent determines the Frame in which the dialog is displayed; if null, or if the parentComponent has no Frame, a default Frame is used
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for a title to be displayed.
	 * @param optionType an integer designating the options available on the dialog: YES_NO_OPTION, or YES_NO_CANCEL_OPTION
	 * @param messageType an integer designating the kind of message this is, primarily used to determine the icon from the pluggable Look and Feel: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @param icon the icon to display in the dialog
	 * @param optionsCID an array of message CIDs indicating the possible choices the user can make
	 * @param initialCIDValue the message CID that represents the default selection for the dialog; only meaningful if options is used; can be null
         * @param firstLine parameter is there to precede the messageCID with some situational information, like e.g. the name of a file.
	 * @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog 
         *
         * Typical usage for using the parameter firstLine:
         *      showOptionDialog(........, jfc.getSelectedFile().getPath() )
	 */
	public static int showOptionDialog(Component parentComponent,
									   String messageCID,
									   String titleCID,
									   int optionType,
									   int messageType,
									   Icon icon,
									   String[] optionsCID,
									   String initialCIDValue,
                                                                           String firstLine)
		{
   	
   		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
                // begin modification Robert lamping, may 3, 2005
                if (firstLine != null || ! firstLine.equals("") ) {
                    message = firstLine + "\n" + message;
                }
                // End modification
                        
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}
									   	
		String initialValue = initialCIDValue;
		if(initialCIDValue != null){
			initialValue = it.businesslogic.ireport.util.I18n.getString(initialCIDValue, initialCIDValue);
		}

		String[] options = optionsCID;
		if(optionsCID != null){
			for(int i = 0; i < optionsCID.length; i++){
				if (optionsCID[i] != null){
					options[i] = it.businesslogic.ireport.util.I18n.getString(optionsCID[i], optionsCID[i]);
				}
			}
		}
									   	
		return JOptionPane.showOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue);			   	
	}

	/**
	 * Shows a question-message dialog requesting input from the user. The dialog uses the default frame, which usually means it is centered on the screen.
	 * @param messageCID message country identification for a message to be displayed.
	 * @return
	 */
	public static String showInputDialog(String messageCID){
		
		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		return JOptionPane.showInputDialog(message);
	}
	
	
	/**
	 * Shows a question-message dialog requesting input from the user, with the input value initialized to initialSelectionValue. The dialog uses the default frame, which usually means it is centered on the screen. 
	 * @param messageCID message country identification for a message to be displayed.
	 * @param initialSelectionCIDValue message country identification for the value used to initialize the input field.
	 * @return
	 */
	public static String showInputDialog(String messageCID, String initialSelectionCIDValue){

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String initialSelectionValue = initialSelectionCIDValue;
		if(initialSelectionCIDValue != null){
			initialSelectionValue = it.businesslogic.ireport.util.I18n.getString(initialSelectionCIDValue, initialSelectionCIDValue);
		}

		return JOptionPane.showInputDialog(message, initialSelectionValue);
	}


	/**
	 * Shows a question-message dialog requesting input from the user parented to parentComponent. The dialog is displayed on top of the Component's frame, and is usually positioned below the Component.
	 * @param messageCID message country identification for a message to be displayed.
	 * @param parentComponent the parent Component for the dialog
	 * @return
	 */
	public static String showInputDialog(Component parentComponent, String messageCID){
		
		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		return JOptionPane.showInputDialog(parentComponent, message);
	}
	
	/**
	 * Shows a question-message dialog requesting input from the user and parented to parentComponent. The input value will be initialized to initialSelectionValue. The dialog is displayed on top of the Component's frame, and is usually positioned below the Component.  
	 * @param parentComponent the parent Component for the dialog
	 * @param messageCID message country identification for a message to be displayed.
	 * @param initialSelectionCIDValue message country identification for the value used to initialize the input field.
	 * @return
	 */
	public static String showInputDialog(Component parentComponent, String messageCID, String initialSelectionCIDValue){

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String initialSelectionValue = initialSelectionCIDValue;
		if(initialSelectionCIDValue != null){
			initialSelectionValue = it.businesslogic.ireport.util.I18n.getString(initialSelectionCIDValue, initialSelectionCIDValue);
		}

		return JOptionPane.showInputDialog(parentComponent, message, initialSelectionValue);
	}
	
	/**
	 * Shows a dialog requesting input from the user parented to parentComponent with the dialog having the title title and message type messageType.   
	 * @param parentComponent the parent Component for the dialog
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param messageType the type of message that is to be displayed: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @return
	 */
	public static String showInputDialog(Component parentComponent, String messageCID, String titleCID, int messageType){

		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		return JOptionPane.showInputDialog(parentComponent, message, title, messageType);
	}
	
	
	/**
	 * Prompts the user for input in a blocking dialog where the initial selection, possible selections, and all other options can be specified. The user will able to choose from selectionValues, where null implies the user can input whatever they wish, usually by means of a JTextField. initialSelectionValue is the initial value to prompt the user with. It is up to the UI to decide how best to represent the selectionValues, but usually a JComboBox, JList, or JTextField will be used. 
	 * @param parentComponent the parent Component for the dialog
	 * @param messageCID message country identification for a message to be displayed.
	 * @param titleCID message country identification for the String to display in the dialog title bar
	 * @param messageType the type of message that is to be displayed: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE
	 * @param icon the Icon image to display
	 * @param selectionCIDValues  message country identifications for an array of strings that gives the possible selections
	 * @param initialSelectionCIDValue message country identification for the value used to initialize the input field.
	 * @return
	 */
	public static String showInputDialog(Component parentComponent,
										 String messageCID,
										 String titleCID,
										 int messageType,
										 Icon icon,
										 String[] selectionCIDValues,
										 String initialSelectionCIDValue)
	{
		
		String message = messageCID;
		if(messageCID != null){
			message = it.businesslogic.ireport.util.I18n.getString(messageCID, messageCID);
		}
		
		String title = titleCID;
		if(titleCID != null){
			title = it.businesslogic.ireport.util.I18n.getString(titleCID, titleCID);
		}

		String initialSelectionValue = initialSelectionCIDValue;
		if(initialSelectionCIDValue != null){
			initialSelectionValue = it.businesslogic.ireport.util.I18n.getString(initialSelectionCIDValue, initialSelectionCIDValue);
		}

		String[] selectionValues = selectionCIDValues;
		if(selectionCIDValues != null){
			for(int i = 0; i < selectionCIDValues.length; i++){
				if (selectionCIDValues[i] != null){
					selectionValues[i] = it.businesslogic.ireport.util.I18n.getString(selectionCIDValues[i], selectionCIDValues[i]);
				}
			}
		}

		return (String) JOptionPane.showInputDialog(parentComponent, message, title, messageType, icon, selectionValues, initialSelectionValue);
	}



}
