/**
 * AbstractFieldPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.autoforms;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.geopista.feature.AbstractValidator;
import com.geopista.feature.ValidationError;
import com.geopista.util.ApplicationContext;


public abstract class AbstractFieldPanel extends JPanel implements PropertyChangeListener, ItemListener, com.geopista.ui.autoforms.AutoForm {



	public abstract boolean checkPanel(boolean updateView);

	public boolean checkPanels() {
	    return checkPanel(false);
	}

	
	

	public abstract void disableAll();

	public void enter() {
	    // TODO Auto-generated method stub
	
	}

	public void exit() {
	    // TODO Auto-generated method stub
	
	}

	

	abstract public JComponent getComponent(String fieldName) ;

	abstract public JComponent getComponentByFieldName(String attName);

	abstract int getCurrentRowCount();

	
	
	/**
	 * @return
	 */
	public abstract Collection getFieldComponents();

	/**
	 * @return
	 */
	public abstract Collection getFieldLabels();

	/**
	 * @return
	 */
	public abstract Set getFieldNames() ;
	

	
	public abstract AbstractValidator getValidator();

	/**
	 * Devuelve el valor asociado a cada campo. Un Date si es un DateField si es
	 * DATE Un Integer si es tipo INTEGER Un Double si es tipo DOUBLE Un String
	 * si es tipo STRING Lanza una assertion si es tipo GEOMETRY o OBJECT
	 * Devuelve Null si hay un error de parse
	 * 
	 * @param attName
	 * @return
	 */
	public abstract Object getValue(String attName);

	public abstract boolean highLightFieldError(ValidationError err);

	
	protected abstract void initialize();

	public void itemStateChanged(ItemEvent e) {
	    checkPanel(true);
	
	}

	public void propertyChange(PropertyChangeEvent evt) {
	    if (evt.getPropertyName().equals("value"))
	    {
	        // revisa el formulario
	        checkPanel(true);
	    }
	
	}



	public void setApplicationContext(ApplicationContext context) {
	    // TODO Auto-generated method stub
	
	}

	public abstract void setCurrentTargetPanel(JPanel panel);



}
