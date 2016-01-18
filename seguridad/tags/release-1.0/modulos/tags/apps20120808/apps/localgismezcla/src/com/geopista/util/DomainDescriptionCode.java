/*
 * Created on 09-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.util;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DomainDescriptionCode
{
    private String code = null;
    private String description = null;
    
    public DomainDescriptionCode(String code, String description)
    {
        this.code = code;
        this.description = description;
    }
    /**
     * @return Returns the code.
     */
    public String getCode()
    {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}
