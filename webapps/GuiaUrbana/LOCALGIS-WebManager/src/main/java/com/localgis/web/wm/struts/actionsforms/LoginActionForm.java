/**
 * LoginActionForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.struts.actionsforms;

import org.apache.struts.action.ActionForm;

public class LoginActionForm extends ActionForm {

    private String username;
    
    private String password;
   
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
    
}
