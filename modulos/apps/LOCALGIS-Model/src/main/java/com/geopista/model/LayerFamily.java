/**
 * LayerFamily.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model;

import com.vividsolutions.jump.workbench.model.Category;

public class LayerFamily extends Category
{
    private boolean systemLayerFamily = false;
    private String systemId="";
    private String description ="";
    
    public boolean isSystemLayerFamily()
    {
        return systemLayerFamily;
    }
    
    public void setSystemLayerFamily(boolean systemCategory)
    {
        this.systemLayerFamily = systemCategory;
    }
    
    public String getSystemId()
    {
        return systemId;
    }
    
    public void setSystemId(String newSystemId)
    {
        systemId = newSystemId;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
}