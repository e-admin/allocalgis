package com.geopista.app.reports.maps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.SortedMap;
import java.util.TreeMap;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.exception.DataException;

/**
 * Clase que se encarga de obtener de la bbdd los tipos de imagenes 
 * de mapas que se permiten en los informes
 * 
 * @author fjcastro
 * 
 */
public class MapImageTypesParser {

    /**
     * Conexión a base de datos
     */
    private Connection conn = null;
    
    /**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    /*
     * Etiquetas del XML
     */

    private static final String ID_ELEMENT = "id";
    private static final String TABLE_ELEMENT = "table";
    private static final String COLUMN_ELEMENT = "column";
    private static final String DESCRIPTION_ELEMENT = "description";
    private static final String LAYERS_ELEMENT = "layers";
    private static final String STYLE_ELEMENT = "style";
    
    private SortedMap mapImageTypes;

    public MapImageTypesParser() {
    	try{
            conn = getDBConnection();
            mapImageTypes = getMapImageTypesList();
		}
    	catch (DataException e) {
			e.printStackTrace();
		}
        catch(Exception e){ 
            e.printStackTrace();
        }
    }
        
    /**
     * Devuelve el campo mapImageTypes
     * @return El campo mapImageTypes
     */
    public SortedMap getMapImageTypes() {
        return mapImageTypes;
    }

    /**
     * Establece el valor del campo mapImageTypes
     * @param mapImageTypes El campo mapImageTypes a establecer
     */
    public void setMapImageTypes(SortedMap mapImageTypes) {
        this.mapImageTypes = mapImageTypes;
    }

    /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException
    {        
        Connection con=  aplicacion.getConnection();
        con.setAutoCommit(false);
        return con;
    } 
    
    /**
     * Obtiene la lista de MapImageTypes
     * @return SortedMap con la lista de MapImageTypes de la BBDD
     * @throws DataException
     */
    private SortedMap getMapImageTypesList() throws DataException
    {

    	SortedMap mapImageTypeList = new TreeMap();
        
        try
        {  
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("GIGetMapImageTypesList");
            r = s.executeQuery();  
            
            while (r.next()){     
            	MapImageType mapImageType = new MapImageType();
            	mapImageType.setId(r.getString(ID_ELEMENT));
            	mapImageType.setTable(r.getString(TABLE_ELEMENT));
            	mapImageType.setColumn(r.getString(COLUMN_ELEMENT));
            	mapImageType.setDescription(r.getString(DESCRIPTION_ELEMENT));
            	mapImageType.setLayers(r.getString(LAYERS_ELEMENT));
            	mapImageType.setStyle(r.getString(STYLE_ELEMENT));            	
                mapImageTypeList.put(mapImageType.getId(), mapImageType);              
            }
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();                        
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);            
        }
        
        return mapImageTypeList;    
    }  
}
