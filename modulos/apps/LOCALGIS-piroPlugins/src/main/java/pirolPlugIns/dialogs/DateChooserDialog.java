/**
 * DateChooserDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
