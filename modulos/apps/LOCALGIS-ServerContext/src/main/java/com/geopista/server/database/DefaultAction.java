/**
 * DefaultAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database;

/**
 * Acción base para la implementación de acciones.
 * 
 */
public abstract class DefaultAction implements IAction {

    /** Información sobre el resultado de la acción. */
    private String info = null;
    
    /** Código de error. */
    private int errorCode = -1;
    
    
    /**
     * Constructor.
     * 
     */
    public DefaultAction() {}
    
    /**
     * Obtiene la información causante de que la ejecución de la acción haya 
     * dado resultado negativo.
     * @return Cadena con información.
     */
    public String getInfo() {
        return info;
    }

    /**
     * Establece la información causante de que la ejecución de la acción haya 
     * dado resultado negativo.
     * @param Cadena con información.
     */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
     * Obtiene el código de error. Este código es un entero que indentifica al 
     * causante de que la ejecución de la acción de resultado negativo.
     * @return Código de error.
     */
    public int getErrorCode() {
        return errorCode;
    }

	/**
     * Establece el código de error. Este código es un entero que indentifica al 
     * causante de que la ejecución de la acción de resultado negativo.
     * @param Código de error.
     */
    public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
