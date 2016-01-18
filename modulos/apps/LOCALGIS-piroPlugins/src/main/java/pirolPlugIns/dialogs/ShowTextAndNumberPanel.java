/**
 * ShowTextAndNumberPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 05.06.2005
 *
 * CVS header information:
 *  $RCSfile: ShowTextAndNumberPanel.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/ShowTextAndNumberPanel.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pirolPlugIns.utilities.NumberRepresentationTools;

/**
 * Small Panel which shows an explanation text and a number.
 * If the number is double, displayed value will be rounded and
 * showed in scientific notation if it's to big or to small.
 * @author Stefan Ostermann
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck
 */
public class ShowTextAndNumberPanel extends JPanel {

    private static final long serialVersionUID = -9092043481477657064L;

    protected JLabel	textLabel;
	protected JLabel	numberLabel;
	protected JPanel	topPanel;
	protected JPanel	bottomPanel;
	protected int		power = 0;
	
	
	public ShowTextAndNumberPanel(String text, int number) {
		super();
		textLabel = new JLabel(text);
		numberLabel = new JLabel(new Integer(number).toString());
		setupPanel();
	}
	
	public ShowTextAndNumberPanel(String text, double number) {
		textLabel = new JLabel(text);
		setupDoubleNumber(number);
		setupPanel();
	}
	
	public void setNumber(int number) {
		numberLabel.setText(new Integer(number).toString());
	}
	
	public void setNumber(double number) {
		setupDoubleNumber(number);
	}

	public void setText(String text) {
		textLabel.setText(text);
	}
	
	public String getText() {
		return textLabel.getText();
	}
	
	public String getNumberText() {
		return numberLabel.getText();
	}
	
	protected void setupDoubleNumber(double number) {
		String numberText;
		power = NumberRepresentationTools.getPower(number);
		double newNumber = NumberRepresentationTools.round(number*Math.pow(10,power),4);
		numberText = new Double(newNumber).toString();
		if (power>0) {
			numberText+="*10^"+power;
		}
		
	}
	
	protected void setupPanel() {
		this.setLayout(new GridLayout(2,1));


		topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(textLabel);
		
		bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.add(numberLabel);

		this.add(topPanel);
		this.add(bottomPanel);
	}

}
