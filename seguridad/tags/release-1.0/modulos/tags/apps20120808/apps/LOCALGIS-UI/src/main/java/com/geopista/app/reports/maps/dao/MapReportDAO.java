package com.geopista.app.reports.maps.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.reports.maps.vo.SelectedLayerVO;
import com.geopista.app.reports.maps.vo.SelectedColumnVO;
import com.geopista.app.reports.maps.vo.SelectedMapVO;

public class MapReportDAO {
	
	public MapReportDAO(){
	}
	
    public List getMapasPrivadosPublicados(Connection conn, int idMunicipio) throws DataException{

    	List listMapas = new ArrayList();
        
        PreparedStatement s = null;
        ResultSet r = null;
        try {  
            
            s = conn.prepareStatement("GIGetMapasPrivadosPublicados");
            String locale = Locale.getDefault().toString();
            s.setString(1, locale);
            s.setInt(2, idMunicipio);
            r = s.executeQuery();  
            
            while (r.next()){
            	
            	SelectedMapVO map = new SelectedMapVO();
            	map.setIdMap(r.getInt("mapidgeopista"));
            	map.setNombre(r.getString("traduccion"));
            	listMapas.add(map);                
            }
            
        }
        catch (SQLException ex) {
            throw new DataException(ex);            
        } finally {
            if (s!=null) {
            	try { s.close(); } catch (SQLException e) {}
            }
            if (r!= null) {
            	try { r.close(); } catch (SQLException e) {} 
            }
        }
        
        return listMapas;    
    } 

    public List getCapasMapa(Connection conn, int idMap) throws DataException{

    	List listCapas = new ArrayList();
        
        PreparedStatement s = null;
        ResultSet r = null;
        try {  
            
            s = conn.prepareStatement("GIGetCapasMapa");
            String locale = Locale.getDefault().toString();
            s.setString(1, locale);
            s.setInt(2, idMap);
            r = s.executeQuery();  
            
            while (r.next()){
            	
            	SelectedLayerVO capa = new SelectedLayerVO();
            	capa.setIdCapa(r.getInt("layeridgeopista"));
            	capa.setNombre(r.getString("layername"));
            	capa.setTraduccion(r.getString("traduccion"));
            	listCapas.add(capa);                
            }

        }
        catch (SQLException ex) {
            throw new DataException(ex);            
        } finally {
            if (s!=null) {
            	try { s.close(); } catch (SQLException e) {}
            }
            if (r!= null) {
            	try { r.close(); } catch (SQLException e) {} 
            }
        }
        
        return listCapas;    
    } 

    public List getColumnasCapa(Connection conn, int idCapa) throws DataException{

    	List listColumnas = new ArrayList();
        
        PreparedStatement s = null;
        ResultSet r = null;
        try {
            s = conn.prepareStatement("GIGetColumnasCapa");
            String locale = Locale.getDefault().toString();
            s.setInt(1, idCapa);
            s.setString(2, locale);
            r = s.executeQuery();  
            
            while (r.next()){
            	SelectedColumnVO columna = new SelectedColumnVO();
            	columna.setAtributo(r.getString(1));
            	columna.setIdColumna(r.getInt(2));
            	columna.setNombre(r.getString(3));
            	columna.setIdTabla(r.getInt(4));
            	columna.setNombreTabla(r.getString(5));
            	listColumnas.add(columna);
            }
        }
        catch (SQLException ex) {
            throw new DataException(ex);            
        } finally {
            if (s!=null) {
            	try { s.close(); } catch (SQLException e) {}
            }
            if (r!= null) {
            	try { r.close(); } catch (SQLException e) {} 
            }
        }
        
        return listColumnas;    
    } 
}
