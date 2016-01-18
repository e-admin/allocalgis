/**
 * OkCancelButtonPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 11.04.2005 for PIROL
 * 
 * CVS header information:
 * $RCSfile: OkCancelButtonPanel.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:46 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/OkCancelButtonPanel.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import pirolPlugIns.i18n.PirolPlugInMessages;

/**
 * This class is a JPanel with a "Cancel" and a "OK" button.
 * @author Carsten Schulze
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * Project: PIROL (2005),
 * Subproject: Daten- und Wissensmanagement
 */
public class OkCancelButtonPanel extends JPanel {
	
    private static final long serialVersionUID = -4703181650847522122L;
    
    /**The constant ActionCommand String for the ok-button*/
	public static final String OK_BUTTON_ACTION_COMMAND = new String("OK_BUTTON_ACTION_COMMAND");
	/**The constant ActionCommand String for the cancel-button*/
	public static final String CANCEL_BUTTON_ACTION_COMMAND = new String("CANCEL_BUTTON_ACTION_COMMAND");
	private JButton cancelButton;
	private JButton okButton;
	/**
	 * This is the default constructor
	 */
	public OkCancelButtonPanel() {
		super();
		initialize();
	}
	/**
	 * Adds the given ActionListener to both buttons.
	 * @param listener the listener
	 */
	public void addActionListener(ActionListener listener){
		getOkButton().addActionListener(listener);
		getCancelButton().addActionListener(listener);
	}
	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	public JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText(PirolPlugInMessages.getString("cancel"));
			cancelButton.setActionCommand(CANCEL_BUTTON_ACTION_COMMAND);
			cancelButton.setFocusPainted(false);
		}
		return cancelButton;
	}
	
	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	public JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText(PirolPlugInMessages.getString("ok"));
			okButton.setActionCommand(OK_BUTTON_ACTION_COMMAND);
			okButton.setFocusPainted(false);
		}
		return okButton;
	}
	/**
	 * Enables/Disables the ok button. May be useful, if the user had to put in values
	 * that may not be correct. You can disable the ok button in this case.
	 * @param enable enables the ok button if true, else disables it
	 */
	public void setOkButtonEnabled(boolean enable){
	    this.getOkButton().setEnabled(enable);
	}
	/**
	 * This method initializes this
	 */
	private  void initialize() {
		this.setSize(300,40);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(Box.createHorizontalGlue(),null);
		this.add(getCancelButton(), null);
		this.add(Box.createRigidArea(new Dimension(10,0)));
		this.add(getOkButton(), null);
	}
}
