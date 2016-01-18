/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * Created on 10-may-2004
 */
package com.geopista.ui.autoforms;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.feature.AbstractValidator;
import com.geopista.ui.components.TwoColumnsFieldPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;

/**
 * Panel de campos de formularios que se genera a partir de un GeopistaSchema. Se generan campos de tipo: -Texto -CheckBox -ComboBox -Calendar Alineados en dos columnas
 * @author   juacas
 * @uml.dependency   supplier="com.geopista.ui.autoforms.FormBuilder"
 */
public class FeatureFieldPanel extends TwoColumnsFieldPanel implements AutoForm,FeatureExtendedPanel, FeatureExtendedForm, PropertyChangeListener, ItemListener
{
    /**
	 *  
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(FeatureFieldPanel.class);

	protected FormBuilder builder;

	protected FeatureFormContext context;

	protected FeatureFormController controller;
	
	
	
   /**
     * This is the default constructor
     */
    public FeatureFieldPanel(Feature feature)
        {
            super();
            
            context= new FeatureFormContext(feature,this);
            setName(I18N.get("Atributoscapalbl"));
        }

    /**
     * 
     */
    public FeatureFieldPanel()
        {
            super();
        }
	/**
     * @param feature2
     */
    public void buildDialogByGroupingTables()
    {
        builder = new FeatureByTablesBuilder();
		builder.setContext(context);
        builder.buildForm();
        controller = new FeatureFormController(context,this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.geopista.util.FeatureExtendedForm#initialize(com.geopista.util.FeatureDialogHome)
     */
    public void initialize(FeatureDialogHome home)
    {
        home.addPanel(this);
        initialize();
    }

  



	/**
	 * Get a clone of the feature represented with current field values set.
	 * 
	 * @return
	 */
	public Feature getModifiedFeature() {
	    Feature clone = ((Feature) context.getModel()).clone(true);
	
	    for (Iterator i = getFieldNames().iterator(); i.hasNext();)
	    {
	        String attName = (String) i.next();
	        Object value = getValue(attName);
	        clone.setAttribute(attName, value);
	    }
	
	    return clone;
	}

	public Object getValue(String attName) {
	    // Obtiene el componente y devuelve el valor según su tipo
	    try
	    {
	        AttributeType type = ((Feature) context.getModel()).getSchema().getAttributeType(attName);
	
	        if (type.equals(AttributeType.DATE))
	            return getDate(attName);
	        else if (type.equals(AttributeType.DOUBLE)||type.equals(AttributeType.FLOAT))
	        	{
	        	if (getDouble(attName)==null){
	        		// Se ha puesto trim() para que quite los espacios en blanco al mostrar los datos:
	        		return getText(attName).trim();
	        	}
	        	else
	        	return getDouble(attName);
	        	}
	        else if (type.equals(AttributeType.INTEGER))
	            {
	            if (getInteger(attName)==null){
	            	// Se ha puesto trim() para que quite los espacios en blanco al mostrar los datos:
	            	return getText(attName).trim();
	            }
	            else
	            	return getInteger(attName);
	            }
	        else if (type.equals(AttributeType.STRING))
	        	{
	        	String text=getText(attName);
	        	// Se ha puesto trim() para que quite los espacios en blanco al mostrar los datos:
	        	text = text.trim();
	            return "".equals(text) ? null : text;
	        	}
	        else if (type.equals(AttributeType.OBJECT))
	        {
	            // este formulario no soporta más tipos
	            Assert
	                    .shouldNeverReachHere("El formulario no soporta este tipo de atributo."
	                            + type);
	        } else if (type.equals(AttributeType.GEOMETRY))
	        {
	            // este formulario no soporta más tipos
	            Assert
	                    .shouldNeverReachHere("El formulario no soporta este tipo de atributo."
	                            + type);
	
	        }
	
	    } catch (NumberFormatException e)
	    {
	        if (logger.isDebugEnabled())
	        {
	            logger.debug("getValue(attName = " + attName + ") - Error en el campo Numérico: ",e);
	        }
	     // Se ha puesto trim() para que quite los espacios en blanco al mostrar los datos:
	        return getText(attName).trim();
	    }
	 // Se ha puesto trim() para que quite los espacios en blanco al mostrar los datos:
	    return getText(attName).trim(); // Trata como texto cualquier otro tipo no
	                                // conocido de datos
	}


//	public AbstractValidator getValidator() {
//		return validator;
//	}

	public boolean checkPanels() {
		return controller.checkPanel(true);
	}

	public void enter() {
        if (controller==null)
            controller = new FeatureFormController(context,this);
		controller.enter();
		
	}

	public void exit() {
		controller.exit();
		
	}


	public void setApplicationContext(ApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	public void propertyChange(PropertyChangeEvent evt) {
		
		super.propertyChange(evt);
		
		controller.formChanged();
		
	}

	public boolean checkPanel(boolean updateView) {
		
		return controller.checkPanel(updateView);
	}

	public AbstractValidator getValidator() {
		
		return controller.getValidator();
	}
    
    public void itemStateChanged(ItemEvent e) {
        super.itemStateChanged(e) ;
        controller.formChanged();
    }




}

