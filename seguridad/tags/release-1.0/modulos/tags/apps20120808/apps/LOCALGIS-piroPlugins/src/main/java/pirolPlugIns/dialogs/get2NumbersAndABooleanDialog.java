/*
 * Created on 29.03.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: get2NumbersAndABooleanDialog.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/get2NumbersAndABooleanDialog.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pirolPlugIns.i18n.PirolPlugInMessages;

/**
 * @author Ole Rahn
 * 
 * FH Osnabr�ck - University of Applied Sciences Osnabr�ck
 * Project PIROL 2005
 * Daten- und Wissensmanagement
 * 
 */
public class get2NumbersAndABooleanDialog extends JDialog implements
        ActionListener, DocumentListener {
	
    private static final long serialVersionUID = -8641333439820891310L;

    private String labelText, cbText, numberText1, numberText2;
	private JTextArea numberField1, numberField2;
	private JCheckBox checkbox;
	private JButton okButton;

	private double number1 = 0.0, number2 = 0.0;
	
	private boolean valueSet = false;

	public get2NumbersAndABooleanDialog(Frame parentFrame, String titel, boolean modal, String text, String numberText1, String numberText2, String cbText)
			throws HeadlessException {
		super(parentFrame, titel, modal);
		this.labelText = text;
		this.cbText = cbText;
		
		this.numberText1 = DialogTools.numberStringToLocalNumberString(numberText1);
		this.numberText2 = DialogTools.numberStringToLocalNumberString(numberText2);
		
		this.setupDialog();
	}
	
	private void setupDialog(){
		
		okButton = new JButton(PirolPlugInMessages.getString("ok"));
		okButton.setActionCommand(okButton.getText());
		okButton.addActionListener(this);
		
		JButton cancelButton = new JButton(PirolPlugInMessages.getString("cancel"));
		cancelButton.setActionCommand(cancelButton.getText());
		cancelButton.addActionListener(this);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,2));
		buttons.add(okButton);
		buttons.add(cancelButton);
		
		NumberInputDocument document1 = new NumberInputDocument();
		document1.setActionCommand("one");
		document1.addDocumentListener(this);
		this.numberField1 = new JTextArea(DialogTools.numberToLocalNumberString(this.number1));
		this.numberField1.setColumns(15);
		this.numberField1.setRows(1);
		this.numberField1.setLineWrap(false);
		this.numberField1.setToolTipText(this.labelText);
		this.numberField1.setAutoscrolls(false);
		this.numberField1.setDocument(document1);

		NumberInputDocument document2 = new NumberInputDocument();
		document2.setActionCommand("two");
		document2.addDocumentListener(this);
		this.numberField2 = new JTextArea(DialogTools.numberToLocalNumberString(this.number2));
		this.numberField2.setColumns(15);
		this.numberField2.setRows(1);
		this.numberField2.setLineWrap(false);
		this.numberField2.setToolTipText(this.labelText);
		this.numberField2.setAutoscrolls(false);
		this.numberField2.setDocument(document2);
		
		JPanel numberPanel1 = new JPanel();
		numberPanel1.setLayout(new GridLayout(1,2));
		JPanel insideNumberPanel1 = new JPanel();
		insideNumberPanel1.setLayout(new FlowLayout(FlowLayout.LEADING));
		JPanel inside2NumberPanel1 = new JPanel();
		inside2NumberPanel1.setLayout(new FlowLayout(FlowLayout.LEADING));
		inside2NumberPanel1.add(new JLabel(this.numberText1));
		numberPanel1.add(inside2NumberPanel1);
		insideNumberPanel1.add(this.numberField1);
		numberPanel1.add(insideNumberPanel1);
		
		JPanel numberPanel2 = new JPanel();
		numberPanel2.setLayout(new GridLayout(1,2));
		JPanel insideNumberPanel2 = new JPanel();
		insideNumberPanel2.setLayout(new FlowLayout(FlowLayout.LEADING));
		JPanel inside2NumberPanel2 = new JPanel();
		inside2NumberPanel2.setLayout(new FlowLayout(FlowLayout.LEADING));
		inside2NumberPanel2.add(new JLabel(this.numberText2));
		numberPanel2.add(inside2NumberPanel2);
		insideNumberPanel2.add(this.numberField2);
		numberPanel2.add(insideNumberPanel2);
		
		this.checkbox = new JCheckBox();
		this.checkbox.setActionCommand("bool");
		this.checkbox.addActionListener(this);
		JPanel cbPanel = new JPanel();
		cbPanel.setLayout(new GridLayout(1,2));
		cbPanel.add(new JLabel(" " +this.cbText));
		cbPanel.add(this.checkbox);
		
		this.numberField2.setEnabled(this.checkbox.isSelected());
		
		JPanel texts = DialogTools.getPanelWithLabels(this.labelText,50);
		
		JPanel panel = new JPanel();
		GridLayout gl = new GridLayout(6,1);
		gl.setHgap(5);
		panel.setLayout(gl);
		//panel.add(new JLabel(this.labelText));
		panel.add(texts);
		panel.add( new JSeparator());
		panel.add(numberPanel1);
		panel.add(cbPanel);
		panel.add(numberPanel2);
		panel.add(buttons);
		panel.doLayout();
		
		this.getContentPane().add(panel);
		this.pack();
		
		this.setVisible(true);
	}
	
	public double getNumber1() {
		return this.number1;
	}
    public double getNumber2() {
        return number2;
    }
	public boolean getBoolean(){
	    return this.checkbox.isSelected();
	}
	
	public void actionPerformed(ActionEvent arg0) {
	    
	    if ( arg0.getSource().getClass().equals(JButton.class) ){
			// ok button pressed
			JButton source = (JButton) arg0.getSource();
			
			if (source.getActionCommand().equals("ok"))
				valueSet = true;
			this.setVisible(false);
	    } else if (arg0.getSource().getClass().equals(JCheckBox.class)){
	        if (this.checkbox.isSelected()){
	            this.number2 = this.number1/2.0;
	            this.numberField2.setText(DialogTools.numberToLocalNumberString(this.number2));
	        } else {
	            this.number2 = 0;
	        }
	        this.numberField2.setEnabled(this.checkbox.isSelected());
	    }
	}
	
	public boolean isValueSet() {
		return valueSet;
	}
	
	public void handleDocUpdate(DocumentEvent e) {
	    NumberInputDocument d = (NumberInputDocument)e.getDocument();
        double number;
        
        try {
            number = DialogTools.localNumberStringToDouble(d.getText(0,d.getLength()));
            if (d.getActionCommand().equals("one")){
                if (!this.okButton.isEnabled())
                    this.okButton.setEnabled(true);
                this.number1 = number;
            } else {
                this.number2 = number;
            }
                
        } catch ( Exception ex ){
            if (d.getActionCommand().equals("one"))
                this.okButton.setEnabled(false);
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

