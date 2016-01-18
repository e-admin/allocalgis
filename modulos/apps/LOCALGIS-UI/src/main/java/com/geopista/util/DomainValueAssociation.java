/**
 * DomainValueAssociation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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