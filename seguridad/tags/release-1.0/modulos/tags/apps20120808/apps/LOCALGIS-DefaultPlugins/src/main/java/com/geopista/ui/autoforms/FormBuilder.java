package com.geopista.ui.autoforms;

public interface FormBuilder {

	public abstract void buildForm();

	/**
	 * Setter of the property <tt>formContext</tt>
	 * @param formContext  The formContext to set.
	 * @uml.property  name="formContext"
	 */
	public abstract void setContext(FormContext formContext);

}