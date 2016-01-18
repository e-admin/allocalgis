/**
 * getAttributeDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 08.03.2005 for Pirol
 *
 * CVS header information:
 * $RCSfile: getAttributeDialog.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:46 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/getAttributeDialog.java,v $
 */
package pirolPlugIns.dialogs;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import pirolPlugIns.i18n.PirolPlugInMessages;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * @author oster
 * Code from getAttributeAndNumberDialog from orahn
 * Shows a Selection-Dialog for Attributes of a featureCollection.
 */
public class getAttributeDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = -1175446588845780428L;
    
    private String labelText;
	private JComboBox attributeBox;
	
	private boolean valueSet = false;
	
	private FeatureSchema attributes;
	private String[] attrStrings;
	private static String okCommand = PirolPlugInMessages.getString("ok"), cancelCommand = PirolPlugInMessages.getString("cancel"), attrCommand = "choose";
	private String attribute;
	private JButton okButton;
	
	public getAttributeDialog(Frame parentFrame, String titel,
            boolean modal, String text,
            FeatureSchema attributes) throws HeadlessException {
        super(parentFrame, titel, modal);



        this.attributes = attributes;

        this.attrStrings = new String[this.attributes.getAttributeCount()];
        for (int i = 0; i < this.attributes.getAttributeCount(); i++) {
            this.attrStrings[i] = this.attributes.getAttributeName(i) + ", ("
                    + this.attributes.getAttributeType(i) + ")";
        }

        this.attribute = this.attrStrings[0];
        this.labelText = text;


        this.setupDialog();
    }
	
	private void setupDialog(){
		
		okButton = new JButton(getAttributeDialog.okCommand);
		okButton.setActionCommand(okButton.getText());
		okButton.addActionListener(this);
		
		JButton cancelButton = new JButton(getAttributeDialog.cancelCommand);
		cancelButton.setActionCommand(cancelButton.getText());
		cancelButton.addActionListener(this);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,2));
		buttons.add(okButton);
		buttons.add(cancelButton);

		this.attributeBox = new JComboBox(this.attrStrings);
		this.attributeBox.setActionCommand(getAttributeDialog.attrCommand);
		this.attributeBox.addActionListener(this);
		this.attributeBox.setPreferredSize(new Dimension(250,25));
		this.attributeBox.setSelectedIndex(0);
		
		JPanel attr = new JPanel();
		attr.setBorder(null);
		attr.setLayout(new FlowLayout(FlowLayout.LEFT));
		attr.add(this.attributeBox);
		
		JPanel texts = DialogTools.getPanelWithLabels(this.labelText,50);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		panel.add( texts );
		panel.add( new JSeparator(JSeparator.HORIZONTAL) );
		panel.add( attr );
		panel.add( buttons );
		panel.doLayout();
		
		this.getContentPane().add(panel);
		this.pack();

	}
	
	public String getAttribute() {
		return attribute;
	}


	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().getClass() == JButton.class){
			JButton source = (JButton) arg0.getSource();
			
			if (source.getActionCommand().equals(getAttributeDialog.okCommand))
				valueSet = true;
			this.setVisible(false);
		} else if (arg0.getSource().getClass() == JComboBox.class){
			int nr = this.attributeBox.getSelectedIndex();
			this.attribute = this.attributes.getAttributeName( nr );
			

			
			if ( this.attributes.getAttributeType(nr) == AttributeType.GEOMETRY ){
				this.okButton.setEnabled(false);
			} else if ( !this.okButton.isEnabled() ){
				this.okButton.setEnabled(true);
			}
		}
	}
	

	
	public boolean isValueSet() {
		return valueSet;
	}
	

}


