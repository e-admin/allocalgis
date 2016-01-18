/*
 * Created on 09.03.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: getAttributeAndObjectDialog.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/getAttributeAndObjectDialog.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.FeatureCollectionTools;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * @author Ole Rahn
 * 
 * FH Osnabrück - University of Applied Sciences Osnabrück
 * Project PIROL 2005
 * Daten- und Wissensmanagement
 * 
 */
public class getAttributeAndObjectDialog extends JDialog implements ActionListener, DocumentListener {
	
    private static final long serialVersionUID = -3437748338226972575L;

    private Object value;
	
	private String labelText;
	private JLabel currValue = null;
	private JComboBox attributeBox;
	private JTextArea valueField = null;
	
	private boolean valueSet = false;
	
	private FeatureSchema attributes;
	private String[] attrStrings;
	private static String okCommand = PirolPlugInMessages.getString("ok"), cancelCommand = PirolPlugInMessages.getString("cancel"), attrCommand = "choose";
	private String attribute;
	private JButton okButton;
	
	public getAttributeAndObjectDialog(Frame parentFrame, String titel,
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
        
        this.value = "0.0";

        this.setupDialog();
    }

	private void setCurrentValue(){
	    String valStr = PirolPlugInMessages.getString("current-value") + ": ";
	    
	    if (this.currValue==null){
	        this.currValue = new JLabel(valStr);
	    }
	    
        valStr += this.value.toString();
	    
	    this.currValue.setText(valStr);
	}
	
	private JPanel getObjectInputPanel(){
	    JPanel objectInputPanel = new JPanel();
	    objectInputPanel.setLayout(new BorderLayout());
	    
	    this.setCurrentValue();
	    
	    
        JPanel flNumberChooser = new JPanel();
        flNumberChooser.setLayout(new GridLayout(1,1));

        ObjectInputDocument oiDoc = new ObjectInputDocument();
        oiDoc.addDocumentListener(this);
        
        this.valueField = new JTextArea( oiDoc, this.value.toString(), 1, 15 );
        this.valueField.setEditable(true);

        flNumberChooser.add(this.valueField);
        
        objectInputPanel.add(flNumberChooser, BorderLayout.NORTH);

	    objectInputPanel.add(this.currValue, BorderLayout.SOUTH);
	    return objectInputPanel;
	}
	
	private void setupDialog(){
		
		okButton = new JButton(getAttributeAndObjectDialog.okCommand);
		okButton.setActionCommand(okButton.getText());
		okButton.addActionListener(this);
		
		JButton cancelButton = new JButton(getAttributeAndObjectDialog.cancelCommand);
		cancelButton.setActionCommand(cancelButton.getText());
		cancelButton.addActionListener(this);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,2));
		buttons.add(okButton);
		buttons.add(cancelButton);

		this.attributeBox = new JComboBox(this.attrStrings);
		this.attributeBox.setActionCommand(getAttributeAndObjectDialog.attrCommand);
		this.attributeBox.addActionListener(this);
		this.attributeBox.setPreferredSize(new Dimension(250,25));
		this.attributeBox.setSelectedIndex(0);
		
		JPanel attr = new JPanel();
		attr.setBorder(null);
		attr.setLayout(new FlowLayout(FlowLayout.LEFT));
		attr.add(this.attributeBox);
		
		JPanel texts = DialogTools.getPanelWithLabels(this.labelText,50);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(7,1));
		panel.add( texts );
		panel.add( new JSeparator(JSeparator.HORIZONTAL) );
		panel.add( attr );
		panel.add( new JSeparator(JSeparator.HORIZONTAL) );
		panel.add( this.getObjectInputPanel() );
		panel.add( new JSeparator(JSeparator.HORIZONTAL) );
		panel.add( buttons );
		panel.doLayout();
		
		this.getContentPane().add(panel);
		this.pack();

	}
	
	public Object getValue() {
	    if (FeatureCollectionTools.isAttributeTypeNumeric(this.attributes.getAttributeType(this.attribute)) && String.class.isInstance(this.value) ){
	        this.value = Double.valueOf((String)this.value);
	    }
	    
	    if (Double.class.isInstance(this.value) && this.attributes.getAttributeType(this.attribute).equals(AttributeType.INTEGER) ){
	        this.value = new Integer(((Double)this.value).intValue());
	    }
        return this.value;
	}
	
	public String getAttribute() {
		return attribute;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().getClass() == JButton.class){
			JButton source = (JButton) arg0.getSource();
			
			if (source.getActionCommand().equals(getAttributeAndObjectDialog.okCommand))
				valueSet = true;
			this.setVisible(false);
		} else if (arg0.getSource().getClass() == JComboBox.class){
			int nr = this.attributeBox.getSelectedIndex();
			this.attribute = this.attributes.getAttributeName( nr );
			
			if (this.valueField == null) return;
			String tmp = this.valueField.getText();
			if ( !FeatureCollectionTools.isAttributeTypeNumeric( this.attributes.getAttributeType(nr) ) ){
			    ObjectInputDocument oiDoc = new ObjectInputDocument();
		        oiDoc.addDocumentListener(this);
			    this.valueField.setDocument(oiDoc);
			    this.valueField.setText(tmp);
			} else {
			    
			    if ( !Double.class.isInstance( this.value ) ){
			        this.value = new Double(0.0);
			    }

			    NumberInputDocument niDoc = new NumberInputDocument();
		        niDoc.addDocumentListener(this);
			    this.valueField.setDocument(niDoc);
			    
			    try {
                    Double.valueOf(tmp);
                    this.valueField.setText(tmp);
                    this.value = Double.valueOf(tmp);
                } catch (NumberFormatException e) {
                    this.valueField.setText(this.value.toString());
                }
			} 
			
			if ( this.attributes.getAttributeType(nr).equals( AttributeType.GEOMETRY ) ){
				this.okButton.setEnabled(false);
			} else if ( !this.okButton.isEnabled() ){
				this.okButton.setEnabled(true);
			}
			this.setCurrentValue();
			//System.out.println( this.attribute );
		}
	}
	
	public boolean isValueSet() {
		return valueSet;
	}
	
	public void handleDocUpdate(DocumentEvent e) {
        Document d = e.getDocument();
        
        try {
            
            if (FeatureCollectionTools.isAttributeTypeNumeric( this.attributes.getAttributeType(this.attribute) )){
                this.value = Double.valueOf(d.getText(0,d.getLength()));
            } else {
                this.value = d.getText(0,d.getLength());
            }

            if (!this.okButton.isEnabled() && d.getLength()!=0)
                this.okButton.setEnabled(true);
            
            this.setCurrentValue();
                
        } catch ( Exception ex ){
            try {
                d.remove(0,d.getLength());
            } catch (Exception e1) {
                //e1.printStackTrace();
            }
        }
        
    }
	

    public void insertUpdate(DocumentEvent e) {
        this.handleDocUpdate(e);        
    }

    public void removeUpdate(DocumentEvent e) {
        this.handleDocUpdate(e);
    }

    public void changedUpdate(DocumentEvent e) {}
    
}
