package com.geopista.ui.autoforms;

public interface FormController {

	/**
	 * Invocado cuando el contenido del formulario ha cambiado
	 * No se informa de cual es el cambio que ha ocurrido, pero alguno de los
	 * componentes ha informado de cambio de estado
	 */
	public abstract void formChanged();

	/**
	 * El formulario adquiere el foco y está a punto de aparecer.
	 */
	public abstract void enter();

	/**
	 * El formulario va a desaparecer y sus datos están listos para procesarse.
	 */
	public abstract void exit();

	/**
	 */
	public abstract void setContext(FormContext formContext);

	/**
	 * Devuelve el objeto adecuado que representa el valor del campo de
	 * formulario attName Tiene que resolver las peculiaridades del interfaz de
	 * usuairo o la lógica de presentación y control de los datos.
	 * 
	 * @param attName
	 * @return
	 */
	public Object getValue(String attName);
	public boolean checkPanel(boolean updateView);
}
