/*
 * Created on 01.12.2004
 *
 * CVS header information:
 *  $RCSfile: getAttributeAndNumberDialog.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/getAttributeAndNumberDialog.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.FeatureCollectionTools;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * Dialog to ask the user to choose one attribute and a number (that can be an integer or a double depending on the constructor used).
 * 
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class getAttributeAndNumberDialog extends JDialog implements ChangeListener, ActionListener, DocumentListener {
	
    private static final long serialVersionUID = 1755427318431628333L;
    
    private int iMin, iMax, iRange, iValue;
	private double dMin, dMax, dRange, dValue;
	private boolean usesDoublePrecision = false;
	
	private String labelText;
	private JSlider slider = null;
	private JLabel currValue = null, doubleMaxLabel, doubleMinLabel;
	private JComboBox attributeBox;
	private JTextArea doubleField = null;
	
	private boolean valueSet = false;
	
	private boolean numbersDisabled = false;
	
	private FeatureSchema attributes;
	private String[] attrStrings;
	private static String okCommand = PirolPlugInMessages.getString("ok"), cancelCommand = PirolPlugInMessages.getString("cancel"), attrCommand = "choose";
	private String attribute;
	private JButton okButton;
	
	protected FeatureCollection featureCollection = null;
	protected boolean reactsOnChangingAttribute = false;
	
	protected boolean checkMinMax = true;

	/**
	 * Initializes the integer version of this dialog. 
	 * In this version a JSlider will be used to choose the number. 
	 * getIntegerValue() will return the number value after ok was clicked.
	 * <br>Hint: This dialog will disable some controls, if the attribute currently choosen is not a numeric one.
	 *@param parentFrame parent frame
	 *@param titel the text that will be displayed in the title bar of the dialog
	 *@param modal flag to set if the dialog is supposed to be model (true) or not
	 *@param min int value that represents the min. chooseable value for the number
	 *@param max int value that represents the max. chooseable value for the number  
	 *@param text dialog text that will be displayed above the controls of this dialog
	 *@param attributes FeatureSchema that hold information on the attributes that are chooseable for the user
	 *@throws HeadlessException see JDialog description for more information on this Exception
	 */
	public getAttributeAndNumberDialog(Frame parentFrame, String titel, boolean modal, int min, int max, String text, FeatureSchema attributes)
			throws HeadlessException {
		super(parentFrame, titel, modal);
		this.iMin = min;
		this.iMax = max;
		this.iRange = this.iMax - this.iMin;
		this.attributes = attributes;
		
		this.attrStrings = new String[this.attributes.getAttributeCount()];
		for ( int i=0; i<this.attributes.getAttributeCount(); i++){
			this.attrStrings[i] = this.attributes.getAttributeName(i) + ", (" + this.attributes.getAttributeType(i) + ")"; 
		}
		
		this.attribute = this.attrStrings[0];
		this.labelText = text;
		this.iValue = this.iMin;
		
		this.setupDialog();
		
		//this.setVisible(true);
	}
	
	/**
	 * Initializes the double version of this dialog. 
	 * In this version a Textfield will be used to choose the number. 
	 * getDoubleValue() will return the number value after ok was clicked.
	 * <br>Hint: This dialog will disable some controls, if the attribute currently choosen is not a numeric one.
	 *@param parentFrame parent frame
	 *@param titel the text that will be displayed in the title bar of the dialog
	 *@param modal flag to set if the dialog is supposed to be model (true) or not
	 *@param min double value that represents the min. chooseable value for the number
	 *@param max double value that represents the max. chooseable value for the number  
	 *@param text dialog text that will be displayed above the controls of this dialog
	 *@param attributes FeatureSchema that hold information on the attributes that are chooseable for the user
	 *@throws HeadlessException see JDialog description for more information on this Exception
	 */
	public getAttributeAndNumberDialog(Frame parentFrame, String titel,
            boolean modal, double min, double max, String text,
            FeatureSchema attributes) throws HeadlessException {
        super(parentFrame, titel, modal);
        this.dMin = min;
        this.dMax = max;
        this.dRange = this.dMax - this.dMin;
        this.usesDoublePrecision = true;
        this.attributes = attributes;

        this.attrStrings = new String[this.attributes.getAttributeCount()];
        for (int i = 0; i < this.attributes.getAttributeCount(); i++) {
            this.attrStrings[i] = this.attributes.getAttributeName(i) + ", ("
                    + this.attributes.getAttributeType(i) + ")";
        }

        this.attribute = this.attrStrings[0];
        this.labelText = text;
        this.dValue = this.dMin;

        this.setupDialog();
    }

	/**
	 * Sets if the number input controls are to be disabled (not editable for the user).
	 *@param disable if true the number input will be disabled
	 */
	public void disableNumberInput(boolean disable){
	    numbersDisabled = disable;
	    
	    if (this.usesDoublePrecision){
	        this.doubleField.setEnabled(!disable);
	    } else {
	        this.slider.setEnabled(!disable);
	    }
	}
	
	/* added by oster */
	public void setReactsOnChangingAttribute(boolean _value) {
		this.reactsOnChangingAttribute = _value;
	}
	
	public boolean getReactsOnChangingAttribute() {
		return this.reactsOnChangingAttribute;
	}
	/* end added by oster */
	/**
	 * if a feature collection is set (and the reactsOnChangingAttribute is not set to false),
	 * the min/max values of the dialog will automatically be set to the choosen attribute's min./max. values
	 * that occur within the given FeatureCollection.
	 */
	public void setFeatureCollection(FeatureCollection fc){
	    this.featureCollection = fc;
	    this.reactsOnChangingAttribute = true;
	}
	
	/**
	 * Specifies, which value will be displayed, when the dialog is opened.
	 * Works with both double and integer version of this dialog.
	 * @param value default number value of the dialog
	 */
	public void setDefaultValueForNumberChooser(double value){
	    
	    if (this.usesDoublePrecision){
	        this.dValue = value;
	        this.doubleField.setText(Double.toString(this.dValue));
	    } else {
	        this.iValue = (int)value;
	        this.slider.setValue(this.iValue);
	    }
	}
	
	/**
	 * Sets up the display of the current value.
	 *
	 */
	private void setCurrentValue(){
	    if (this.numbersDisabled) return;
	    
	    String valStr = PirolPlugInMessages.getString("current-value") + ": ";
	    
	    if (this.currValue==null){
	        this.currValue = new JLabel(valStr);
	    }
	    
	    if (this.usesDoublePrecision){
	        valStr += DialogTools.numberToLocalNumberString(this.dValue);
	    } else {
	        valStr += this.iValue;
	    }
	    
	    this.currValue.setText(valStr);
	}
	
	/**
	 * Creates a number choosing panel for the current version (integer or double)
	 * of this dialog. 
	 * @return a panel that contains controls to choose a number
	 */
	private JPanel getNumberChoosingPanel(){
	    JPanel numberChooserPanel = new JPanel();
	    numberChooserPanel.setLayout(new BorderLayout());
	    
	    this.setCurrentValue();
	    
	    if (!this.usesDoublePrecision){
	        this.slider = new JSlider(JSlider.HORIZONTAL, this.iMin, this.iMax, this.iValue);
			this.slider.addChangeListener(this);
			this.slider.setSnapToTicks(true);
			this.slider.setMinorTickSpacing(1);
			this.slider.setMajorTickSpacing(this.iRange/10);
			this.slider.setPaintLabels(true);
			this.slider.setPaintTicks(true);
			this.slider.setPaintTrack(true);
			numberChooserPanel.add(this.slider, BorderLayout.NORTH);
	    } else {
	        JPanel flNumberChooser = new JPanel();
	        flNumberChooser.setLayout(new GridLayout(1,3));
	        this.doubleMinLabel = new JLabel("min. "+DialogTools.numberToLocalNumberString(this.dMin));
	        flNumberChooser.add(this.doubleMinLabel);
	        NumberInputDocument niDoc = new NumberInputDocument();
	        niDoc.addDocumentListener(this);
	        
	        this.doubleField = new JTextArea( niDoc, DialogTools.numberToLocalNumberString(this.dValue), 1, 15 );
	        this.doubleField.setEditable(true);

	        flNumberChooser.add(this.doubleField);
	        
	        this.doubleMaxLabel = new JLabel("max. "+DialogTools.numberToLocalNumberString(this.dMax));
	        flNumberChooser.add(this.doubleMaxLabel);
	        
	        numberChooserPanel.add(flNumberChooser, BorderLayout.NORTH);
	    }
	    numberChooserPanel.add(this.currValue, BorderLayout.SOUTH);
	    return numberChooserPanel;
	}
	
	/**
	 * initializes the controls and the layout of this dialog.
	 *
	 */
	private void setupDialog(){
		
		okButton = new JButton(getAttributeAndNumberDialog.okCommand);
		okButton.setActionCommand(okButton.getText());
		okButton.addActionListener(this);
		
		JButton cancelButton = new JButton(getAttributeAndNumberDialog.cancelCommand);
		cancelButton.setActionCommand(cancelButton.getText());
		cancelButton.addActionListener(this);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,2));
		buttons.add(okButton);
		buttons.add(cancelButton);

		this.attributeBox = new JComboBox(this.attrStrings);
		this.attributeBox.setActionCommand(getAttributeAndNumberDialog.attrCommand);
		this.attributeBox.addActionListener(this);
		this.attributeBox.setPreferredSize(new Dimension(250,25));
		
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
		panel.add( this.getNumberChoosingPanel() );
		panel.add( new JSeparator(JSeparator.HORIZONTAL) );
		panel.add( buttons );
		panel.doLayout();
		
		this.getContentPane().add(panel);
		this.pack();
		
		if (this.attributeBox.getItemCount()!=0)
		    this.attributeBox.setSelectedIndex(0);

	}
	
	/**
	 * gets the number choosen by the user as an integer
	 * @return choosen value as int.
	 */
	public int getIntegerValue() {
	    if (!this.usesDoublePrecision){
	        return iValue;
	    } 
        return (int)this.dValue;
	}
	/**
	 * gets the number choosen by the user as a double
	 * @return choosen value as double.
	 */
	public double getDoubleValue() {
	    if (!this.usesDoublePrecision){
	        return iValue;
	    }
	    return this.dValue;
	}
	
	/**
	 * Returns the name of the attribute choosen
	 * @return attribute's name
	 */
	public String getAttribute() {
		return attribute;
	}
	public void stateChanged(ChangeEvent arg0) {
		this.iValue = this.slider.getValue();
		this.setCurrentValue();
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().getClass() == JButton.class){
			JButton source = (JButton) arg0.getSource();
			
			if (source.getActionCommand().equals(getAttributeAndNumberDialog.okCommand))
				valueSet = true;
			this.setVisible(false);
		} else if (arg0.getSource().getClass() == JComboBox.class){
			int nr = this.attributeBox.getSelectedIndex();
			this.attribute = this.attributes.getAttributeName( nr );
			
			if ( !FeatureCollectionTools.isAttributeTypeNumeric( this.attributes.getAttributeType(nr) ) ){
				if (!this.usesDoublePrecision && this.slider != null)
				    this.slider.setEnabled(false);
				else if (this.doubleField!=null)
				    this.doubleField.setEnabled(false);
				
				if (this.currValue != null ) this.currValue.setEnabled(false);
			} else {
			    if (!this.numbersDisabled && !this.usesDoublePrecision && !this.slider.isEnabled()){
					this.slider.setEnabled(true);
					this.currValue.setEnabled(true);
				} else if (!this.numbersDisabled && this.usesDoublePrecision && !this.doubleField.isEnabled()){
					this.doubleField.setEnabled(true);
					this.currValue.setEnabled(true);
				}
				if ( this.reactsOnChangingAttribute ){
				    this.updateMinMax(this.attributes.getAttributeType(nr));
				}
			}
			
			if ( this.attributes.getAttributeType(nr).equals( AttributeType.GEOMETRY ) ){
				this.okButton.setEnabled(false);
			} else if ( !this.okButton.isEnabled() ){
				this.okButton.setEnabled(true);
			}
		}
	}
	
	/**
	 * updates the display of the min/max value and enables/disables the
	 * ok button, if the min/max range is exceeded by current value.
	 * This method is only called, if the reactsOnChangingAttribute flag is true.
	 * @param at
	 */
	public void updateMinMax( AttributeType at ){
	    if (this.numbersDisabled) return;
	    if (FeatureCollectionTools.isAttributeTypeNumeric( at )){
            double[] minmax = FeatureCollectionTools.getMinMaxAttributeValue(this.featureCollection, this.attribute);
	        if (!this.usesDoublePrecision){
	            
	            this.iMin = (int)minmax[0];
	            this.iMax = (int)minmax[1];
	            this.iRange = this.iMax - this.iMin;
	            this.iValue = this.iMin;
	            
	            this.slider.setMinimum(this.iMin);
	            this.slider.setMaximum(this.iMax);
	            
	            this.slider.setValue(this.iValue);
	        } else {
	            
	            this.dMin = minmax[0];
	            this.dMax = minmax[1];
	            this.dRange = this.dMax - this.dMin;
	            this.dValue = this.dMin;
	            
	            this.doubleMinLabel.setText("min. "+DialogTools.numberToLocalNumberString(this.dMin));
	            this.doubleMaxLabel.setText("max. "+DialogTools.numberToLocalNumberString(this.dMax));
	        }
	        
	        this.setCurrentValue();
	    } else {
	        
	    }
	}
	
	/**
	 * Using this method one can check, if the user pressed ok or cancel to close the dialog.
	 * @return true if ok button was pressed, else false
	 */
	public boolean isValueSet() {
		return valueSet;
	}
	
	public void handleDocUpdate(DocumentEvent e) {
	    if (this.numbersDisabled) return;
        Document d = e.getDocument();
        
        try {
            this.dValue = DialogTools.localNumberStringToDouble(d.getText(0,d.getLength()));
            if (this.dValue >= this.dMin && this.dValue <= this.dMax){
                this.setCurrentValue();
                if (!this.okButton.isEnabled())
                    this.okButton.setEnabled(true);
                
                this.doubleMinLabel.setForeground(Color.black);
                this.doubleMaxLabel.setForeground(Color.black);
            } else {
                if (this.dValue < this.dMin){
                    this.doubleMinLabel.setForeground(Color.red);
                } else {
                    this.doubleMaxLabel.setForeground(Color.red);
                }
                if (this.checkMinMax){
                    this.okButton.setEnabled(false);
                } else {
                    this.setCurrentValue();
                    this.okButton.setEnabled(true);
                }
            }
        } catch ( Exception ex ){
            try {
                d.remove(0,d.getLength());
            } catch (Exception e1) {
                //e1.printStackTrace();
            }
        }
        
    }
	
	

    public double getDMax() {
        return dMax;
    }
    public void setDMax(double max) {
        dMax = max;
    }
    public double getDMin() {
        return dMin;
    }
    public void setDMin(double min) {
        dMin = min;
    }
    public double getDRange() {
        return dRange;
    }
    public void setDRange(double range) {
        dRange = range;
    }
    public int getIMax() {
        return iMax;
    }
    public void setIMax(int max) {
        iMax = max;
    }
    public int getIMin() {
        return iMin;
    }
    public void setIMin(int min) {
        iMin = min;
    }
    public int getIRange() {
        return iRange;
    }
    public void setIRange(int range) {
        iRange = range;
    }
    public void insertUpdate(DocumentEvent e) {
        this.handleDocUpdate(e);        
    }

    public void removeUpdate(DocumentEvent e) {
        this.handleDocUpdate(e);
    }

    public void changedUpdate(DocumentEvent e) {}
    
    public boolean checksMinMax() {
        return checkMinMax;
    }
    public void setChecksMinMax(boolean checkMinMax) {
        this.checkMinMax = checkMinMax;
    }
}
