/**
 * SpinnerPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 03.05.2005 for Pirol.
 *
 * CVS header information:
 *  $RCSfile: SpinnerPanel.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/SpinnerPanel.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 * Helper Panel for dialogs to enter numbers using a JSpinner. Also shows a little help-text.
 * Possible to show min/max values, too.
 * 
 * @author Stefan Ostermann
 */
public class SpinnerPanel extends JPanel {
	protected SpinnerNumberModel 	model;
	protected JSpinner 				spinner;
	protected JLabel				textLabel;
	protected JLabel				rangeLabel;
	protected JPanel				topPanel;
	protected JPanel				bottomPanel;
	protected String				text;
	protected String				rangeText;
	protected boolean				showRangeText;
	protected LayoutManager			layoutManager;	
	
	/**
	 * Constructor.
	 * @param defaultValue The value the dialog shows up with.
	 * @param minValue Minimum.
	 * @param maxValue Maximum.
	 * @param stepSize Up/Down Step
	 * @param text Helper-Text.
	 */
	public SpinnerPanel(int defaultValue, int minValue, int maxValue, int stepSize, String text) {
		this(new FlowLayout(FlowLayout.LEFT),defaultValue,minValue,maxValue,stepSize, text, true);
	}
	

	public SpinnerPanel(int defaultValue, int minValue, int maxValue, int stepSize, String text, boolean showRangeText) {
		this(new FlowLayout(FlowLayout.LEFT),defaultValue,minValue,maxValue,stepSize, text, showRangeText);
	}
	
	/**
	 * Constructor.
	 * @param defaultValue The value the dialog shows up with.
	 * @param minValue Minimum.
	 * @param maxValue Maximum.
	 * @param stepSize Up/Down Step
	 * @param text Helper-Text.
	 */
	public SpinnerPanel(double defaultValue, double minValue, double maxValue, double stepSize, String text) {
		this(new FlowLayout(FlowLayout.LEFT),defaultValue,minValue,maxValue,stepSize, text, true);
	}
	
	public SpinnerPanel(double defaultValue, double minValue, double maxValue, double stepSize, String text, boolean showRangeText) {
		this(new FlowLayout(FlowLayout.LEFT),defaultValue,minValue,maxValue,stepSize, text, showRangeText);
	}

	/**
	 * Constructor.
	 * @param LayoutManager Want to use an other LayoutManager? No Problem...
	 * @param defaultValue The value the dialog shows up with.
	 * @param minValue Minimum.
	 * @param maxValue Maximum.
	 * @param stepSize Up/Down Step
	 * @param text Helper-Text.
	 * @param showRangeText show minimum/maximum values?
	 */
	public SpinnerPanel(LayoutManager layoutManager,int defaultValue, int minValue, int maxValue, int stepSize, String text, boolean showRangeText) {
		this.layoutManager=layoutManager;
		this.showRangeText = showRangeText;
		model = new SpinnerNumberModel(defaultValue, minValue, maxValue, stepSize);
		if (this.showRangeText) {
			rangeText = "(" + minValue + " to " + maxValue + ")";
		} else {
			this.rangeText = "";
		}
		this.text = text;
		this.setupPanel();
		
		
	}
	/**
	 * 
	 * @param LayoutManager Want to use an other LayoutManager? No Problem...
	 * @param defaultValue The value the dialog shows up with.
	 * @param minValue Minimum.
	 * @param maxValue Maximum.
	 * @param stepSize Up/Down Step
	 * @param text Helper-Text.
	 * @param showRangeText show minimum/maximum values?
	 */
	public SpinnerPanel(LayoutManager layoutManager,double defaultValue, double minValue, double maxValue, double stepSize, String text, boolean showRangeText) {
		this.layoutManager=layoutManager;
		this.showRangeText = showRangeText;
		model = new SpinnerNumberModel(defaultValue, minValue, maxValue, stepSize);
		
		if (this.showRangeText) {
			rangeText = "(" + DialogTools.numberToLocalNumberString(Math.rint(minValue*Math.pow(10,3))/Math.pow(10,3)) + " to " + DialogTools.numberToLocalNumberString(Math.rint(maxValue*Math.pow(10,3))/Math.pow(10,3)) + ")";
		} else {
			rangeText = "";
		}
		this.text = text;
		this.setupPanel();
		
	}
	
	protected void setupPanel() {
		this.setLayout(new GridLayout(2,1));
		textLabel = new JLabel(text);
		rangeLabel = new JLabel(rangeText);
		spinner = new JSpinner(model);
		spinner.setVerifyInputWhenFocusTarget(true);
		topPanel = new JPanel(layoutManager);
		topPanel.add(textLabel);
		
		bottomPanel = new JPanel(layoutManager);
		bottomPanel.add(spinner);
		bottomPanel.add(rangeLabel);
		this.add(topPanel);
		this.add(bottomPanel);
	}
	
	/**
	 * returns the choosen value as double. 
	 * @return
	 */
	public double getDoubleValue() {
		if (spinner!=null)
			return ((Double)spinner.getValue()).doubleValue();
		else return 0.0;
	}
	
	public void setDoubleValue(double val) {
		spinner.setValue(new Double(val));
	}
	
	/**
	 * returns the choosen value as integer.
	 * @return
	 */
	public int getIntValue() {
		if (spinner!=null)
			return ((Integer)spinner.getValue()).intValue();
		else return 0;
	}
	
	public void setIntValue(int val) {
		spinner.setValue(new Integer(val));
	}
	
	/**
	 * Makes it possible to directly manipulate the JSpinner.
	 * @return JSpinner
	 */
//	public JSpinner getSpinner() {
//		return spinner;
//	}

	/**
	 * @return Returns the isRangeVisible.
	 */
	public boolean isRangeVisible() {
		return rangeLabel.isVisible();
	}
	/**
	 * @param isRangeVisible sets the visibility of the range label.
	 */
	public void setRangeVisible(boolean isRangeVisible) {
		this.rangeLabel.setVisible(isRangeVisible);
	}
	
	public void addChangeListener(ChangeListener cl) {
		this.spinner.addChangeListener(cl);
	}
	
	/**
	 * @param enabled enables/disables the JSpinner.
	 */
	public void setEnabled(boolean enabled) {
		this.spinner.setEnabled(enabled);
	}
	
	/** 
	 * Sets the label text.
	 * @param text
	 */
	public void setText(String text) {
		this.textLabel.setText(text);
	}
	
	/** Sets the tool tip text.
	 * @param text
	 */
	public void setToolTipText(String text){
		this.spinner.setToolTipText(text);
		this.textLabel.setToolTipText(text);
		this.spinner.validate();
		this.textLabel.validate();
		
	}
}
