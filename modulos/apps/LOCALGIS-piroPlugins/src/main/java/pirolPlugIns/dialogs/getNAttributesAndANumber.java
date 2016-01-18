/**
 * getNAttributesAndANumber.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 14.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pirolPlugIns.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.colors.ColorGenerator;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * 
 * TODO: comment class
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
public class getNAttributesAndANumber extends JDialog implements ActionListener, ValueChecker {
	
    private static final long serialVersionUID = 8745733284509813104L;

    private double min, max, value;
	private String labelText;
	private JTextField dullnessBox;
	private JComboBox xAttributeBox, yAttributeBox;
	
	protected OkCancelButtonPanel okCancelPanel = new OkCancelButtonPanel();
    protected OKCancelListener listener = null;
    
    protected ColorsChooserPanel colorPanel = null;
    
	private FeatureSchema attributes;
	private String[] attrStrings;
	private static String xAttrCommand = "choose X", yAttrCommand = "choose Y";
	private String[] attributeStrs;
	private String xAttribute, yAttribute;

	public getNAttributesAndANumber(Frame parentFrame, String titel, boolean modal, double min, double max, String text, FeatureSchema attributes)
			throws HeadlessException {
		super(parentFrame, titel, modal);
		this.min = min;
		this.max = max;
		this.attributes = attributes;
		
		this.attrStrings = new String[this.attributes.getAttributeCount()];
		for ( int i=0; i<this.attributes.getAttributeCount(); i++){
			this.attrStrings[i] = this.attributes.getAttributeName(i) + ", (" + this.attributes.getAttributeType(i) + ")"; 
		}
		
		this.xAttribute = this.attrStrings[0];
		this.yAttribute = this.attrStrings[0];
		this.labelText = text;
		this.value = (this.min+this.max)/2;
		
		this.setupDialog();
	}
	
	private void setupDialog(){
	    JPanel dullness = new JPanel();
	    dullness.setLayout(new BorderLayout());
	    dullness.add( new JLabel(PirolPlugInMessages.getString("cluster-dullness") + ":"), BorderLayout.NORTH);
		this.dullnessBox = new JTextField( DialogTools.numberToLocalNumberString(this.value) );
		this.dullnessBox.setEditable(true);
		this.dullnessBox.setEnabled(true);
		dullness.add(this.dullnessBox, BorderLayout.SOUTH);
		
        
        this.listener = new OKCancelListener(this);
        this.listener.addValueChecker(this);
        this.okCancelPanel.addActionListener(this.listener);
		

		this.xAttributeBox = new JComboBox(this.attrStrings);
		this.xAttributeBox.setActionCommand(getNAttributesAndANumber.xAttrCommand);
		this.xAttributeBox.addActionListener(this);
		this.xAttributeBox.setPreferredSize(new Dimension(250,25));
		this.xAttributeBox.setSelectedIndex(0);
		
		this.yAttributeBox = new JComboBox(this.attrStrings);
		this.yAttributeBox.setActionCommand(getNAttributesAndANumber.yAttrCommand);
		this.yAttributeBox.addActionListener(this);
		this.yAttributeBox.setPreferredSize(new Dimension(250,25));
		this.yAttributeBox.setSelectedIndex(0);
		
		JPanel attr = new JPanel();
		//attr.setBorder(null);
		attr.setLayout(new BorderLayout());
		attr.add(this.xAttributeBox, BorderLayout.NORTH);
		attr.add(this.yAttributeBox, BorderLayout.SOUTH);
		
        this.colorPanel = new ColorsChooserPanel();
        this.colorPanel.setAllowChangingTheNumberOfSteps(false);
        
        JPanel content = new JPanel(new BorderLayout());
        
		JPanel texts = DialogTools.getPanelWithLabels(this.labelText,50);
        content.add(texts, BorderLayout.NORTH);
		
		JPanel attrsNFactor = new JPanel();
		attrsNFactor.setLayout(new GridLayout(5,1));
        attrsNFactor.add( new JSeparator(JSeparator.HORIZONTAL) );
		attrsNFactor.add( attr );
		attrsNFactor.add( new JSeparator(JSeparator.HORIZONTAL) );
		attrsNFactor.add(dullness);
		attrsNFactor.add( new JSeparator(JSeparator.HORIZONTAL) );
        content.add(attrsNFactor, BorderLayout.CENTER);
        
        JPanel colorsNok = new JPanel(new BorderLayout());
        colorsNok.add(this.colorPanel, BorderLayout.CENTER);
        colorsNok.add(this.okCancelPanel, BorderLayout.SOUTH);
        content.add(colorsNok, BorderLayout.SOUTH);
        
        
        content.doLayout();
		
		this.getContentPane().add(content);
		this.pack();
		
		this.setVisible(true);
	}
	
	public double getValue() {
		return value;
	}
	
	public String[] getAttributes() {
		this.attributeStrs = new String[]{ this.xAttribute, this.yAttribute };
		return this.attributeStrs;
	}
	
	private boolean checkAttrTypeNonDiscrete(int nr){
		if ( this.attributes.getAttributeType(nr) != AttributeType.INTEGER && this.attributes.getAttributeType(nr) != AttributeType.DOUBLE ){
			return false;
		}
		return true;		
	}
	
	private boolean inputsOk(){
		boolean ok = this.checkAttrTypeNonDiscrete(this.xAttributeBox.getSelectedIndex()) && this.checkAttrTypeNonDiscrete(this.yAttributeBox.getSelectedIndex());
		
		try{
			this.value = DialogTools.localNumberStringToDouble(this.dullnessBox.getText());
			ok = ok && true;
		} catch ( Exception e ){
			this.dullnessBox.setText("NAN");
			ok = ok && false;
		}		
		return ok;
	}

	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getSource().getClass() != JButton.class){
			if (arg0.getSource().getClass() == JComboBox.class){
				JComboBox box = (JComboBox) arg0.getSource();
				int nr;
				if ( box.getActionCommand().equals(getNAttributesAndANumber.xAttrCommand) ){
					nr = this.xAttributeBox.getSelectedIndex();
					this.xAttribute = this.attributes.getAttributeName( nr );
				} else if ( box.getActionCommand().equals(getNAttributesAndANumber.yAttrCommand) ){
					nr = this.yAttributeBox.getSelectedIndex();
					this.yAttribute = this.attributes.getAttributeName( nr );
				}
				
			} else if (arg0.getSource().getClass() == JTextField.class){ 
				try{
					this.value = DialogTools.localNumberStringToDouble(this.dullnessBox.getText());
				} catch ( Exception e ){
					this.dullnessBox.setText("NAN");
					return;
				}
			}

		}
	}
	
	public boolean isValueSet() {
		return this.listener.wasOkClicked();
	}

    /**
     *@inheritDoc
     */
    public boolean areValuesOk() {
        try{
            this.value = DialogTools.localNumberStringToDouble(this.dullnessBox.getText());
        } catch ( Exception e ){
            this.dullnessBox.setText("NAN");
            return false;
        }
        return this.inputsOk();
    }

    public ColorGenerator createColorGenerator() {
        return colorPanel.createColorGenerator();
    }

    public Color getColor(int num) {
        return colorPanel.getColor(num);
    }

    public Color[] getColors() {
        return colorPanel.getColors();
    }
    
    
}
