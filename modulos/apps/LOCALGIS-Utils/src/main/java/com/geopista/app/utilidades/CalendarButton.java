/**
 * CalendarButton.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPopupMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JCalendarBeanInfo;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 10-jun-2004
 * Time: 16:24:47
 */
public class CalendarButton extends JButton implements ActionListener, PropertyChangeListener
 {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(CalendarButton.class);

	protected JCalendar jCalendar;
	protected JPopupMenu popup;
	protected JFormattedTextField campoTexto;
    protected String sValor;
	protected Calendar fecha= null;
    public CalendarButton(JFormattedTextField jFcampoTexto)
    {
        super();
        campoTexto=jFcampoTexto;
        init();
    }

	public CalendarButton(Calendar cFecha, JFormattedTextField jFcampoTexto) {
        super();
        fecha =cFecha;
        campoTexto=jFcampoTexto;
        init();

    }
    public void init()
    {
        addActionListener(this);
        jCalendar = new JCalendar();
        popup=new JPopupMenu();
        popup.add(jCalendar);
        JCalendarBeanInfo calBI = new JCalendarBeanInfo();
        setIcon(new ImageIcon(calBI.getIcon(BeanInfo.ICON_COLOR_16x16)));
        setActionCommand("showCalendar");
        setPreferredSize(new java.awt.Dimension(20,20));
        setSize(new java.awt.Dimension(20,20));
        jCalendar.addPropertyChangeListener("calendar",this);

        campoTexto.addPropertyChangeListener("value",new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent event)
			{
				if (event.getSource()== campoTexto)
					firePropertyChange(event.getPropertyName(),event.getOldValue(),event.getNewValue());
			}
		});

    }

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		//campoTexto.requestFocus();
        if (event.getSource()!=this) return;

		getRootPane().setDefaultButton(null);
		if (!popup.isVisible())
		{
            campoTexto.requestFocus();
            Calendar fechaMuestra;
            if (fecha==null)
            {
          
                Calendar cFechaActual=Calendar.getInstance();
                fechaMuestra=cFechaActual;
            }
            else
            	{
            	
            	fechaMuestra=fecha;
            	
            	jCalendar.setCalendar(fechaMuestra);
            	}

		    Dimension dim = getSize();
		    popup.show(this, dim.width - popup.getPreferredSize().width,	dim.height);
		}
		else
		{
		    popup.setVisible(false);
		  	campoTexto.requestFocus();
		}
    }


	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() == jCalendar)
		{
			if (event.getPropertyName().equals("calendar"))
			{
				if (logger.isDebugEnabled())
					{
					logger.debug("propertyChange(PropertyChangeEvent)"
							+ dateFunc((Calendar) event.getOldValue()));
				
					logger.debug("propertyChange(PropertyChangeEvent)"
							+ dateFunc((Calendar) event.getNewValue()));
					}
			    setField((Calendar)event.getNewValue());
		        campoTexto.requestFocus();
			}
		}
	}

    private void setField(Calendar cValor)
	{
		campoTexto.setValue(cValor.getTime());
        sValor=new Long(cValor.getTime().getTime()).toString();
        fecha=cValor;
	}
	public String getText()
	{
    	return sValor;
	}

    public Calendar getCalendar()
    {
        return fecha;
    }
    public void setCampoTexto(JFormattedTextField auxCampo)
	{
        campoTexto=auxCampo;
        if (fecha!=null)
            campoTexto.setValue(fecha.getTime());
        else
            campoTexto.setText("");
     }

     public void setFecha(Calendar auxFecha)
     {
         fecha=auxFecha;

        if (fecha!=null)
            campoTexto.setValue(fecha.getTime());
        else
            campoTexto.setText("");
     }

     public static String dateFunc (Calendar calendar) {

			String YY = (calendar.get(Calendar.YEAR)+"").substring(2);
			String MM = ((calendar.get(Calendar.MONTH)+1)+"");
			String DD = (calendar.get(Calendar.DAY_OF_MONTH)+"");
			String HH = (calendar.get(Calendar.HOUR)+"");
			String MI = (calendar.get(Calendar.MINUTE)+"");

			// The AM_PM field contains 0 or 1, which I have to convert to AM or PM.
			int AMint = (calendar.get(Calendar.AM_PM));
			String AMStr;
			if (AMint == 0)	{AMStr = "AM"; } else {AMStr = "PM"; }

			// And I do this for consistant date appearance. For example, 00-06-10_04-42-PM.
			if (MM.length() == 1) {	MM = "0"+MM; }
			if (DD.length() == 1) {	DD = "0"+DD; }
			if (HH.length() == 1) {	HH = "0"+HH; }
			if (MI.length() == 1) {	MI = "0"+MI; }

			return (YY+"-"+MM+"-"+DD+"_"+HH+"-"+MI+"-"+AMStr);
	}


}
