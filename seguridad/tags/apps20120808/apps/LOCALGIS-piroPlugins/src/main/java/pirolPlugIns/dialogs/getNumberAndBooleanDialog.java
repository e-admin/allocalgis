/*
 * Created on 18.01.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: getNumberAndBooleanDialog.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/getNumberAndBooleanDialog.java,v $
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
import javax.swing.text.Document;

import pirolPlugIns.i18n.PirolPlugInMessages;

/**
 * @author Ole Rahn
 * 
 * FH Osnabr�ck - University of Applied Sciences Osnabr�ck
 * Project PIROL 2005
 * Daten- und Wissensmanagement
 * 
 */
public class getNumberAndBooleanDialog extends JDialog implements ActionListener, DocumentListener {
    	
    private static final long serialVersionUID = -4375683011761992146L;
    private String labelText, cbText, numberText;
    private JTextArea numberfield;
    private JCheckBox checkbox;
    private JButton okButton;
    
    private double number = 0.0;
    
    private boolean valueSet = false;
    
    public getNumberAndBooleanDialog(Frame parentFrame, String titel, boolean modal, String text, String numberText, String cbText)
    throws HeadlessException {
        super(parentFrame, titel, modal);
        this.labelText = text;
        this.cbText = cbText;
        this.numberText = numberText;
        
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
        
        this.numberfield = new JTextArea(DialogTools.numberToLocalNumberString(this.number));
        this.numberfield.setColumns(15);
        this.numberfield.setRows(1);
        this.numberfield.setLineWrap(false);
        this.numberfield.setToolTipText(this.labelText);
        this.numberfield.setAutoscrolls(false);
        this.numberfield.getDocument().addDocumentListener(this);
        
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(1,2));
        
        JPanel insideNumberPanel = new JPanel();
        insideNumberPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        JPanel inside2NumberPanel = new JPanel();
        inside2NumberPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        inside2NumberPanel.add(new JLabel(this.numberText));
        numberPanel.add(inside2NumberPanel);
        insideNumberPanel.add(this.numberfield);
        numberPanel.add(insideNumberPanel);
        
        this.checkbox = new JCheckBox();
        JPanel cbPanel = new JPanel();
        cbPanel.setLayout(new GridLayout(1,2));
        cbPanel.add(new JLabel(" " +this.cbText));
        cbPanel.add(this.checkbox);
        
        JPanel texts = DialogTools.getPanelWithLabels(this.labelText,50);
        
        JPanel panel = new JPanel();
        GridLayout gl = new GridLayout(5,1);
        gl.setHgap(5);
        panel.setLayout(gl);
        //panel.add(new JLabel(this.labelText));
        panel.add(texts);
        panel.add( new JSeparator());
        panel.add(numberPanel);
        panel.add(cbPanel);
        panel.add(buttons);
        panel.doLayout();
        
        this.getContentPane().add(panel);
        this.pack();
        
        this.setVisible(true);
    }
    
    public double getValue() {
        return this.number;
    }
    
    public boolean getBoolean(){
        return this.checkbox.isSelected();
    }
    
    public void actionPerformed(ActionEvent arg0) {
        // ok button pressed
        JButton source = (JButton) arg0.getSource();
        
        if (source.getActionCommand().equals(PirolPlugInMessages.getString("ok")))
            valueSet = true;
        this.setVisible(false);
    }
    
    public boolean isValueSet() {
        return valueSet;
    }
    
    public void handleDocUpdate(DocumentEvent e) {
        Document d = e.getDocument();
        
        try {
            this.number = DialogTools.localNumberStringToDouble(d.getText(0,d.getLength()));
            if (!this.okButton.isEnabled())
                this.okButton.setEnabled(true);
            
        } catch ( Exception ex ){
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
