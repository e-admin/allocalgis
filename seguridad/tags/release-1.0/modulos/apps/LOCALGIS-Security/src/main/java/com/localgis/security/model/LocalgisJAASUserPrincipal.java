package com.localgis.security.model;

import java.io.Serializable;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

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
