/**
 * Get2AttributesDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 18.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: Get2AttributesDialog.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/Get2AttributesDialog.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * dialog that asks for two attributes.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class Get2AttributesDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = -8741601703698146570L;
    
    protected GetAttributePanel attrPanel1 = null, attrPanel2 = null;
    protected OkCancelButtonPanel okCancelPanel = null; 
    protected String dialogText = null;
    
    protected FeatureCollection featureCollection = null;
    private boolean valueSet = false;

    /**
     *@param parent
     *@param title
     *@param modal
     *@throws java.awt.HeadlessException
     */
    public Get2AttributesDialog(Frame parent, String title, String text, boolean modal, FeatureCollection featureCollection)
            throws HeadlessException {
        super(parent, title, modal);
        this.featureCollection = featureCollection;
        this.dialogText = text;
        
        this.setupDialog();
    }
    
    private void setupDialog(){
        FeatureSchema fs = this.featureCollection.getFeatureSchema();
        
        JPanel texts = DialogTools.getPanelWithLabels(this.dialogText,50);
        
        AttributeType[] validAttributeTypes = new AttributeType[]{AttributeType.INTEGER, AttributeType.DOUBLE}; 
        this.attrPanel1 = new GetAttributePanel(fs, validAttributeTypes);
        this.attrPanel2 = new GetAttributePanel(fs, validAttributeTypes);
        
        this.okCancelPanel = new OkCancelButtonPanel();
        this.okCancelPanel.addActionListener(this);
        
        JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(7,1));
		panel.add( texts );
		panel.add( new JSeparator(JSeparator.HORIZONTAL) );
		panel.add( this.attrPanel1 );
		panel.add( new JSeparator(JSeparator.HORIZONTAL) );
		panel.add( this.attrPanel2 );
		panel.add( new JSeparator(JSeparator.HORIZONTAL) );
		panel.add(this.okCancelPanel);
		panel.doLayout();
		
		this.getContentPane().add(panel);
		this.pack();
		
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource().getClass() == JButton.class){
			JButton source = (JButton) arg0.getSource();

			if (source.getActionCommand().equals(this.okCancelPanel.getOkButton().getActionCommand())){
				this.valueSet = true;
			}
			this.setVisible(false);
		}
    }
    
    
    public String getAttribute1() {
        return attrPanel1.getAttribute();
    }

    public String getAttribute2() {
        return attrPanel2.getAttribute();
    }
    
    
    public boolean isValueSet() {
        return valueSet;
    }
}
