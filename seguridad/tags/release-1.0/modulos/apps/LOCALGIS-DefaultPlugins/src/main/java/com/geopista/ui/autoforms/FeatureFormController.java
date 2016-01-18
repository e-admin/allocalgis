/**
 * 
 */
package com.geopista.ui.autoforms;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.AutoFieldDomain;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.feature.ValidationError;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;

/** 
 * @author juacas
 */
public class FeatureFormController implements com.geopista.ui.autoforms.FormController {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(FeatureFormController.class);

	private FeatureFormContext formcontext;
	private FeatureFieldPanel formpanel;
	private AbstractValidator validator = new SchemaValidator(null);
	
	public FeatureFormController(FeatureFormContext context, FeatureFieldPanel panel) {
		this.formcontext=context;
		this.formpanel=panel;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.autoforms.FormController#formChanged()
	 */
	public void formChanged() {
		checkPanel(true);

	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.autoforms.FormController#enter()
	 */
	public void enter() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.autoforms.FormController#exit()
	 */
	public void exit() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.autoforms.FormController#setContext(com.geopista.ui.autoforms.FormContext)
	 */
	public void setContext(FormContext formContext) {
		// TODO Auto-generated method stub

	}
	
	public boolean checkPanel(boolean updateView) {
		
	    Feature clone = formpanel.getModifiedFeature();
	    boolean valid = false;
	    valid = validator.validateFeature(clone);
	    formpanel.restoreFieldsDecoration();
	    
	    if (valid == false && updateView == true)
	    {
	      
	        // highlight erroneus
	        for (Iterator i = validator.getErrorListIterator(); i.hasNext();)
	        {
	            ValidationError err = (ValidationError) i.next();
	            if (err == null)
	            {
	                if (logger.isErrorEnabled())
	                {
	                    logger.error(
	                            "checkPanel() - Informe del error nulo.  : Feature clone = "
	                                    + clone, null);
	                }
	
	                return false;
	            }
	            formpanel.highLightFieldError(err);
	            
	            /**
	             * Comprobación para depurar los casos en que ha aparecido un componente sin referencia
	             */
	            JComponent comp = (JComponent) formpanel.getComponentByFieldName(err.attName);
	            if (comp == null)
	            {
	                if (logger.isErrorEnabled())
	                {
	                    logger
	                            .error(
	                                    "checkPanel(boolean) - Un error se ha generado sin referencia a un campo del formulario. No se actualiza el interfaz de usuario: ValidationError err = "
	                                            + err + ", Feature clone = " + clone,
	                                    null);
	                }
	                return false;
	            }
	            /**
	             * Los valores automáticos se pueden actualizar
	             */
	            if (err.domain.getType() == Domain.AUTO) // sets automatic
	                                                        // Value
	            {
	
	                DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
	                format.applyLocalizedPattern(err.domain.getFormat());
	                Object rightValue = ((AutoFieldDomain) err.domain)
	                        .getRightValue(clone,err.attName);
	                if (rightValue != null)
	                	{
	                	
	                	try
							{
							if (rightValue instanceof String)
								{
								String previousText=((JTextComponent) comp).getText();
								if (!(rightValue).equals(previousText))
									((JTextComponent) comp).setText((String) rightValue);
								}
								else
								{
								if (!format.format(rightValue).equals( ((JTextComponent) comp).getText()))
									((JTextComponent) comp).setText(format.format(rightValue));
								}
							}
						catch (IllegalStateException e)
							{
							// Se produce un bucle cerrado de notificationes de eventos
							logger.error("checkPanel(boolean)", e);
							}
	                	}
	            } else
	            {
	                comp
	                        .setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
	                                Color.red));
	                comp.setToolTipText(AppContext.getApplicationContext().getI18nString(
	                        err.message));
	            }
	        }
	    }
	    return valid;
	
	}
	/**
	 * Crea dominios por defecto para los atributos que no tengan ninguno definido
	 * @param gSchema
	 */
	private void checkSchema(GeopistaSchema gSchema) {
	
	    for (int i = 0; i < gSchema.getAttributeCount(); i++)
	    {
	        Domain localDomain = gSchema.getAttributeDomain(i);
	        if (localDomain == null
	                && gSchema.getAttributeType(i) != AttributeType.GEOMETRY)
	        {
	            Column attColumn = gSchema.getColumnByAttribute(i);
	            String attname=attColumn.getName();
	            
	            Domain defDomain = Domain.getDefaultDomain(gSchema, attname);
	            attColumn.setDomain(defDomain);
	        }
	    }
	
	}
	public AbstractValidator getValidator() {
		
		return validator;
	}
	/**
	 * Devuelve el objeto adecuado que representa el valor del campo de formulario attName
	 * Tiene que resolver las peculiaridades del interfaz de usuairo o la lógica de presentación
	 * y control de los datos.
	 * @param attName
	 * @return
	 */
	public Object getValue(String attName) {
	    // Obtiene el componente y devuelve el valor según su tipo
	    try
	    {
	        AttributeType type = ((Feature) formcontext.getModel()).getSchema().getAttributeType(attName);
	
	        if (type.equals(AttributeType.DATE))
	            return formpanel.getDate(attName);
	        else if (type.equals(AttributeType.DOUBLE))
	        	{
	        	if (formpanel.getDouble(attName)==null)
	        		return formpanel.getText(attName);
	        	else
	        	return formpanel.getDouble(attName);
	        	}
	        else if (type.equals(AttributeType.INTEGER))
	            {
	            if (formpanel.getInteger(attName)==null)
	            	return formpanel.getText(attName);
	            else
	            	return formpanel.getInteger(attName);
	            }
	        else if (type.equals(AttributeType.STRING))
	        	{
	        	String text=formpanel.getText(attName);
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
	        return formpanel.getText(attName);
	    }
	
	    return formpanel.getText(attName); // Trata como texto cualquier otro tipo no
	                                // conocido de datos
	}

}
