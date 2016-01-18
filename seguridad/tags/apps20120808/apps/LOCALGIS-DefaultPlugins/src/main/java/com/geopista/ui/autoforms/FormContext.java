/**
 * 
 */
package com.geopista.ui.autoforms;

import javax.swing.JComponent;

/**
 * @author juacas
 * @uml.stereotype uml_id="Archetype::description"
 */
public interface FormContext {

	/**
	 */
	public abstract Object getModel();

	/**
	 */
	public abstract AutoForm getForm();

}
