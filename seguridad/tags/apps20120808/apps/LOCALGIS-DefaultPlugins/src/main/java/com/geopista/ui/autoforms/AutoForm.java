package com.geopista.ui.autoforms;

import java.util.Collection;
import java.util.Set;
import javax.swing.JComponent;
import com.geopista.feature.ValidationError;
import com.geopista.util.ApplicationContext;


/**
 * @uml.dependency   supplier="com.geopista.ui.autoforms.FormBuilder"
 * @uml.dependency   supplier="com.geopista.ui.autoforms.FormController"
 */
public interface AutoForm extends FieldPanelAccessor {

	public abstract boolean getBoolean(String fieldName);

	public abstract Double getDouble(String fieldName);

	public abstract Integer getInteger(String fieldName);

	public abstract String getText(String fieldName);

	/**
	 * Realiza la comprobación de los atributos del panel según los dominios
	 * 
	 * @param updateView
	 *            si es true se actualiza el interfaz de usuario marcando los
	 *            campos erróneos
	 * @return
	 */
	public abstract boolean checkPanel(boolean updateView);

	
 
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geopista.util.FeatureExtendedForm#setApplicationContext(com.geopista.util.AppContext)
	 */
	public abstract void setApplicationContext(ApplicationContext context);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geopista.util.FeatureExtendedForm#checkPanels()
	 */
	public abstract boolean checkPanels();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geopista.util.FeatureExtendedPanel#enter()
	 */
	public abstract void enter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geopista.util.FeatureExtendedPanel#exit()
	 */
	public abstract void exit();

	public abstract void disableAll();
	/**
	 * Remarks the erroneous field with data stored in err
	 * @param err
	 * @return true if the component erroneous is really in this Form/Panel
	 */
	public boolean highLightFieldError(ValidationError err);
	/**
	 * Clears all error highlighting
	 *
	 */
	public void restoreFieldsDecoration();

	public abstract JComponent getComponent(String fieldName);

	public abstract JComponent getComponentByFieldName(String attName);

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
	public abstract Set getFieldNames();

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
}