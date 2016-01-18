/**
 * Query.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layer;



/**
 * Bean que recoge las diferentes consultas SQL en los distintos
 * tipos de bases de datos
 * 
 * @author cotesa
 *
 */


public class Query implements Cloneable
{
    private int idQuery;
    private int tipoDatabase;
    private int idLayer;
    private String selectQuery = null;
    private String insertQuery = null;
    private String updateQuery = null;
    private String deleteQuery = null;
    
    /**
     * Constructor por defecto
     *
     */
    public Query()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * @return Returns the deleteQuery.
     */
    public String getDeleteQuery()
    {
        return deleteQuery;
    }
    
    
    /**
     * @param deleteQuery The deleteQuery to set.
     */
    public void setDeleteQuery(String deleteQuery)
    {
        this.deleteQuery = deleteQuery;
    }
    
    
    /**
     * @return Returns the id.
     */
    public int getIdQuery()
    {
        return idQuery;
    }
    
    
    /**
     * @param id The id to set.
     */
    public void setIdQuery(int idQuery)
    {
        this.idQuery = idQuery;
    }
    
    
    /**
     * @return Returns the idLayer.
     */
    public int getIdLayer()
    {
        return idLayer;
    }
    
    
    /**
     * @param idLayer The idLayer to set.
     */
    public void setIdLayer(int idLayer)
    {
        this.idLayer = idLayer;
    }
    
    
    /**
     * @return Returns the insertQuery.
     */
    public String getInsertQuery()
    {
        return insertQuery;
    }
    
    
    /**
     * @param insertQuery The insertQuery to set.
     */
    public void setInsertQuery(String insertQuery)
    {
        this.insertQuery = insertQuery;
    }
    
    
    /**
     * @return Returns the selectQuery.
     */
    public String getSelectQuery()
    {
        return selectQuery;
    }
    
    
    /**
     * @param selectQuery The selectQuery to set.
     */
    public void setSelectQuery(String selectQuery)
    {
        this.selectQuery = selectQuery;
    }
    
    
    /**
     * @return Returns the tipoDatabase.
     */
    public int getTipoDatabase()
    {
        return tipoDatabase;
    }
    
    
    /**
     * @param tipoDatabase The tipoDatabase to set.
     */
    public void setTipoDatabase(int tipoDatabase)
    {
        this.tipoDatabase = tipoDatabase;
    }
    
    
    /**
     * @return Returns the updateQuery.
     */
    public String getUpdateQuery()
    {
        return updateQuery;
    }
    
    
    /**
     * @param updateQuery The updateQuery to set.
     */
    public void setUpdateQuery(String updateQuery)
    {
        this.updateQuery = updateQuery;
    }
    
    
    /**
     * @return Devuelve la query correspondiente al tipo indicado en el parámetro tipoQuery.
     */
    public String getQuery (int tipoQuery)
    {
        
        switch (tipoQuery)
        {
        case LayersPanel.SELECT:            
            return selectQuery;
            
        case LayersPanel.INSERT:
            return insertQuery;
            
        case LayersPanel.UPDATE:
            return updateQuery;
            
        case LayersPanel.DELETE:
            return deleteQuery;
            
        default:            
            return null;
        
        }
    }
    
    
    /**
     * Fija el valor de una query de tipo tipoQuery al contenido de query
     * @param tipoQuery Tipo de query (select, update, insert, delete)
     * @param query Valor de la query
     */
    public void setQuery (int tipoQuery, String query)
    {
        
        switch (tipoQuery)
        {
        case LayersPanel.SELECT:     
            setSelectQuery(query);
            break;
            
        case LayersPanel.INSERT:
            setInsertQuery(query);
            break;
            
        case LayersPanel.UPDATE:
            setUpdateQuery(query);
            break;
            
        case LayersPanel.DELETE:
            setDeleteQuery(query);
            break;
            
        default:            
            break;
        
        }
    }
    
    
    public Object clone() {
        Cloneable theClone = new Query();
        return theClone;
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof Query)) return false;
        Query q = (Query)o; 
        
        int sonIguales = 1;
        
        if (q.getSelectQuery()!=null) 
        {
            if(q.getSelectQuery().equals(this.getSelectQuery()))                           
                sonIguales=sonIguales*1;                          
            else            
                sonIguales=sonIguales*0;                        
            
        }
        
        if (q.getUpdateQuery()!=null) 
        {
            if(q.getUpdateQuery().equals(this.getUpdateQuery()))                           
                sonIguales=sonIguales*1;                             
            else
                sonIguales=sonIguales*0;            
        }
        
        if (q.getInsertQuery()!=null) 
        {
            if(q.getInsertQuery().equals(this.getInsertQuery()))                           
                sonIguales=sonIguales*1;                          
            else            
                sonIguales=sonIguales*0;                        
            
        }
        
        if (q.getDeleteQuery()!=null) 
        {
            if(q.getDeleteQuery().equals(this.getDeleteQuery()))                           
                sonIguales=sonIguales*1;                             
            else
                sonIguales=sonIguales*0;            
        }
        
        if (sonIguales==1)
            return true;
        else
            return false;
    }
    
    
}
