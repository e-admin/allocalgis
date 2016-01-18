/**
 * LocalgisJAASUserPrincipal.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.security.model;

import java.io.Serializable;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

public class LocalgisJAASUserPrincipal implements Principal, Serializable {
	
	    private final String _name;
	    private final Subject _subject;
	    private final LoginContext _loginContext;

	    /* ------------------------------------------------ */

	    public LocalgisJAASUserPrincipal(String name, Subject subject, LoginContext loginContext)
	    {
	        this._name = name;
	        this._subject = subject;
	        this._loginContext = loginContext;
	    }

	    /* ------------------------------------------------ */
	    /** Get the name identifying the user
	     */
	    public String getName ()
	    {
	        return _name;
	    }
	    
	    
	    /* ------------------------------------------------ */
	    /** Provide access to the Subject
	     * @return subject
	     */
	    public Subject getSubject ()
	    {
	        return this._subject;
	    }
	    
	    LoginContext getLoginContext ()
	    {
	        return this._loginContext;
	    }
	    
	    public String toString()
	    {
	        return getName();
	    }
	    
}
