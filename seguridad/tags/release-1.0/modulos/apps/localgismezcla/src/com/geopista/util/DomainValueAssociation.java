/*
 * Created on 06-may-2005
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
public class DomainValueAssociation
{
    private DomainDescriptionCode existingDomain = null; 
    private String newDomain = null;
    
    public DomainValueAssociation(String newDomain,DomainDescriptionCode existingDomain)
    {
        this.existingDomain = existingDomain;
        this.newDomain = newDomain;
    }
    
  
 
    /**
     * @return Returns the existingDomain.
     */
    public DomainDescriptionCode getExistingDomain()
    {
        return existingDomain;
    }
    /**
     * @param existingDomain The existingDomain to set.
     */
    public void setExistingDomain(DomainDescriptionCode existingDomain)
    {
        this.existingDomain = existingDomain;
    }
    /**
     * @return Returns the newDomain.
     */
    public String getNewDomain()
    {
        return newDomain;
    }
    /**
     * @param newDomain The newDomain to set.
     */
    public void setNewDomain(String newDomain)
    {
        this.newDomain = newDomain;
    }
}