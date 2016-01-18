/**
 * DateField.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.components;

import java.util.Calendar;
import java.util.Date;

import com.toedter.calendar.JDateChooser;

/**
 * encapsulates a JDateChooser
 *
 * @author juacas
 *
 */
public class DateField extends JDateChooser {


	
	
	public DateField(Calendar cal, int aproxlenght)
	{
	super();
	this.setCalendar(cal);
	}
	
	/**
	 * @param fecha
	 * @param aproxLenght
	 */
	public DateField(Date fecha, int aproxLenght) {

	super(fecha);
	}

	public DateField(Date fecha, Date initialDate, Date finalDate) {

	super(fecha);
	if (finalDate!=null)this.setMaxSelectableDate(finalDate);
	if (initialDate!=null)this.setMinSelectableDate(initialDate);
	
	}

	/** Auto-generated main method */
	public static void main(String[] args){
		showGUI();
	}

	/**
	* This static method creates a new instance of this class and shows
	* it inside a new JFrame, (unless it is already a JFrame).
	*
	* It is a convenience method for showing the GUI, but it can be
	* copied and used as a basis for your own code.	*
	* It is auto-generated code - the body of this method will be
	* re-generated after any changes are made to the GUI.
	* However, if you delete this method it will not be re-created.	*/
	public static void showGUI(){
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			Calendar cal=Calendar.getInstance();
			//cal.set(1973,3,19);
			DateField inst = new DateField(cal,15);
			frame.setContentPane(inst);
			frame.getContentPane().setSize(inst.getSize());
			frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	



	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setToolTipText(java.lang.String)
	 */
//	public void setToolTipText(String text) {
//		
//		super.setToolTipText(text);
//		field.setToolTipText(text);
//		calendarButton.setToolTipText(text);
//	}
	
	public Date getValue()
	{
		return this.getDate();
//		if ("".equals(texto))
//			return null; // permite especificar nulos
//		else	// prevalece la fecha del componente calendario
//		{
//			DateFormat df=DateFormat.getDateInstance(DateFormat.SHORT);
//			try
//			{
//				return df.parse(texto);
//			} catch (ParseException e)
//			{
//				return null;
//			}
//		}
		//return calendar.getCalendar().getTime();
	}
	public String getText()
	{
	return this.getDateFormatString();
	}

	/**
	 * @param b
	 */
	public void setEditable(boolean b) {
		this.setEnabled(b);
		
	}
}
