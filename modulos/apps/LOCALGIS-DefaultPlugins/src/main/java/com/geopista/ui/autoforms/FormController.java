/**
 * FormController.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
