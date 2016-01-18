/*
 * Created on 01.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pirolPlugIns.dialogs;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pirolPlugIns.i18n.PirolPlugInMessages;

/**
 * @author orahn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class getNumberDialog extends JDialog implements ChangeListener, ActionListener {
	
    private static final long serialVersionUID = 3001348212167838714L;
    
    private int min, max, range, value;
	private String labelText;
	private JSlider slider;
	private JLabel currValue;
	private boolean valueSet = false;

	public getNumberDialog(Frame parentFrame, String titel, boolean modal, int min, int max, String text)
			throws HeadlessException {
		super(parentFrame, titel, modal);
		this.min = min;
		this.max = max;
		this.range = this.max - this.min;
		this.labelText = text;
		this.value = this.min;
		
		this.setupDialog();
	}
	
	private void setupDialog(){
		this.slider = new JSlider(JSlider.HORIZONTAL, this.min, this.max, this.value);
		this.slider.addChangeListener(this);
		this.slider.setSnapToTicks(true);
		this.slider.setMinorTickSpacing(1);
		this.slider.setMajorTickSpacing(this.range/10);
		this.slider.setPaintLabels(true);
		this.slider.setPaintTicks(true);
		this.slider.setPaintTrack(true);
		
		this.currValue = new JLabel(PirolPlugInMessages.getString("current-value")+": " + DialogTools.numberToLocalNumberString(this.value));
		
		JButton okButton = new JButton(PirolPlugInMessages.getString("ok"));
		okButton.setActionCommand(okButton.getText());
		okButton.addActionListener(this);
		
		JButton cancelButton = new JButton(PirolPlugInMessages.getString("cancel"));
		cancelButton.setActionCommand(cancelButton.getText());
		cancelButton.addActionListener(this);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,2));
		buttons.add(okButton);
		buttons.add(cancelButton);

		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		JPanel texts = DialogTools.getPanelWithLabels(this.labelText,50);
		panel.add(texts);
		panel.add(this.slider);
		panel.add(this.currValue);
		panel.add(buttons);
		panel.doLayout();
		
		this.getContentPane().add(panel);
		this.pack();
		
		this.setVisible(true);
	}
	
	public int getValue() {
		return value;
	}

	public void stateChanged(ChangeEvent arg0) {
		this.value = this.slider.getValue();
		this.currValue.setText( PirolPlugInMessages.getString("current-value") +": " + DialogTools.numberToLocalNumberString(this.value ));
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
}
