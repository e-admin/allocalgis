/**
 * DomainDescriptionCode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
