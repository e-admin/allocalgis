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