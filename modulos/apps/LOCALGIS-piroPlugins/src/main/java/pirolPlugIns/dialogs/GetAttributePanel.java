/**
 * GetAttributePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 06.05.2005 for Pirol
 *
 * CVS header information:
 * $RCSfile: GetAttributePanel.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:46 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/GetAttributePanel.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pirolPlugIns.i18n.PirolPlugInMessages;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * A JPanel which shows a ComboBox with attributes and their types. 
 * @author Stefan Ostermann
 */
public class GetAttributePanel extends JPanel {

    private static final long serialVersionUID = 2974879926044967978L;
    
    /* attributes: */
	protected JComboBox attributeBox;
	protected FeatureSchema tempAttributes;
	protected FeatureSchema attributes;
	protected AttributeType onlyTypes[] = null;
	protected String[] attrStrings;
	protected String attribute;
	protected FeatureCollection featureCollection = null;
	
	public static final String ACTION_COMMAND = "GetAttribute";
	
	/**
	 * Constructor.
	 * @param attributes FeatureSchema with the attributes to choose from.
	 * @param onlyTypes  Use only this AttributeTypes.
	 */
	public GetAttributePanel(FeatureSchema attributes, AttributeType onlyTypes[]) {
		super();
		this.tempAttributes = attributes;
		this.onlyTypes = onlyTypes;
		this.copySelectedAttributes();
		this.setupDialog();
	}
	
	/**
	 * Constructor.
	 * @param attributes FeatureSchema with the attributes to choose from.
	 */
	public GetAttributePanel(FeatureSchema attributes) {
		super();
		this.attributes = attributes;
		this.setupDialog();
	}
	
	protected void copySelectedAttributes() {
        this.attributes = new FeatureSchema();
        
        for (int i=0;i<tempAttributes.getAttributeCount();i++) {
        	for (int i2=0;i2<this.onlyTypes.length;i2++) {
	        	if (this.tempAttributes.getAttributeType(i) == this.onlyTypes[i2]) {
	        		this.attributes.addAttribute(tempAttributes.getAttributeName(i),tempAttributes.getAttributeType(i));
	        	}
        	}
        }
	}
	
	protected void setupDialog() {
		this.attrStrings = new String[this.attributes.getAttributeCount()];
		for ( int i=0; i<this.attributes.getAttributeCount(); i++){
			this.attrStrings[i] = this.attributes.getAttributeName(i) + ", (" + this.attributes.getAttributeType(i) + ")"; 
		}
		
		this.attributeBox = new JComboBox(this.attrStrings);
		this.attributeBox.setPreferredSize(new Dimension(250,25));

		this.setBorder(null);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(new JLabel(PirolPlugInMessages.getString("choose-attribute") + ": "));
		this.add(this.attributeBox);
	}
	/**
	 * 
	 * @return the attribute-name as a String.
	 */
	public String getAttribute() {
		if (attributes.getAttributeCount()>0)
			return this.attributes.getAttributeName(this.attributeBox.getSelectedIndex());
		return null;
	}
	
	public int getNumOfAttributes() {
		return attributes.getAttributeCount();
	}
	
	/**
	 * Adds an ActionListener to the combo-box and sets 
	 * the ActionCommand to the default-value.
	 * @param al ActionListener.
	 */
	public void addActionListener(ActionListener al) {
		this.attributeBox.addActionListener(al);
		this.attributeBox.setActionCommand(ACTION_COMMAND);
	}
	
	/**
	 * Overrides the default ActionCommand for the combo-box.
	 * @param ac ActionCommand String.
	 */
	public void setActionCommand(String ac) {
		this.attributeBox.setActionCommand(ac);
	}

    public Object getItemAt(int arg0) {
        return attributeBox.getItemAt(arg0);
    }

    public int getItemCount() {
        return attributeBox.getItemCount();
    }

    public int getSelectedIndex() {
        return attributeBox.getSelectedIndex();
    }

    public Object getSelectedItem() {
        return attributeBox.getSelectedItem();
    }

    public Object[] getSelectedObjects() {
        return attributeBox.getSelectedObjects();
    }

    public void setSelectedIndex(int arg0) {
        attributeBox.setSelectedIndex(arg0);
    }

    public void setSelectedItem(Object arg0) {
        attributeBox.setSelectedItem(arg0);
    }
    
    public boolean setSelectedAttribute(String attribute) {
        for (int i=0; i<this.attributeBox.getItemCount(); i++){
            if (this.attributeBox.getItemAt(i).toString().substring(0, this.attributeBox.getItemAt(i).toString().lastIndexOf(",")).equals(attribute)){
                this.attributeBox.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }

    public void setEnabled(boolean arg0) {
        super.setEnabled(arg0);
        this.attributeBox.setEnabled(arg0);
    }
    
    

}
