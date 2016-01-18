package com.geopista.feature;

public class Attribute 
{
    private String name;
    private String type;
    private Column column;
    private GeopistaSchema schema;
    private String accessType = GeopistaSchema.READ_WRITE;
    private String category;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String newType)
    {
        type = newType;
    }
    
    public Column getColumn()
    {
        return column;
    }
    
    public void setColumn(Column newColumn)
    {
        column = newColumn;
    }
    
    public GeopistaSchema getSchema()
    {
        return schema;
    }
    
    public void setSchema(GeopistaSchema newSchema)
    {
        schema = newSchema;
    }
    
    public String getAccessType()
    {
        return accessType;
    }
    
    public void setAccessType(String newAccessType)
    {
        accessType = newAccessType;
    }
    
    
    
    public String getCategory()
    {
        return category;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }
}