/**
 * LoginFeatureActionForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.actionsforms;

import org.apache.struts.action.ActionForm;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Formulario de autenticación
 */
public class LoginFeatureActionForm extends ActionForm {

	/**
	 * Variables
	 */
    private String username;    
    private String password;      
    private String requesturi;

    /**
     * Devuelve el campo username
     * @return El campo username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el valor del campo username
     * @param username El campo username a establecer
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve el campo password
     * @return El campo password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece el valor del campo password
     * @param password El campo password a establecer
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Devuelve el campo requesturi
     * @return El campo requesturi
     */
	public String getRequesturi() {
		return requesturi;
	}
	
    /**
     * Establece el valor del campo requesturi
     * @param password El campo requesturi a establecer
     */
	public void setRequesturi(String requesturi) {
		this.requesturi = requesturi;
	}
    
    

}
