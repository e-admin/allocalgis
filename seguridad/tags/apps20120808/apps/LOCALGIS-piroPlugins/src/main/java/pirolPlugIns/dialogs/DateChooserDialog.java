/*
 * Created on 30.11.2005 for Pirol.
 *
 * CVS header information:
 *  $CSfile: $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/DateChooserDialog.java,v $
 */

package pirolPlugIns.dialogs;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class DateChooserDialog extends JDialog implements ActionListener{
	protected DateChooserPanel dateChooserPanel;
	protected OkCancelButtonPanel okCancel;
	
	boolean isValid = false;
	
	public DateChooserDialog(JFrame parentFrame) {
		super(parentFrame,true);
		dateChooserPanel = new DateChooserPanel(parentFrame, false, "date-chooser");
		
		okCancel = new OkCancelButtonPanel();
		okCancel.addActionListener(this);
		this.getContentPane().setLayout(new GridLayout(2,1));
		this.getContentPane().add(dateChooserPanel);
		this.getContentPane().add(okCancel);
		this.setSize(280,160);

	}
	
	public Date getDate() {
		if (isValid)
			return dateChooserPanel.getChoosenDate();
		return null;

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == OkCancelButtonPanel.OK_BUTTON_ACTION_COMMAND) {
			this.isValid = true;
			this.setVisible(false);
		} else
			this.setVisible(false);
		
	}

}
