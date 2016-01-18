/**
 * MapReportDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.maps.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.geopista.app.reports.maps.exception.ReportMapException;
import com.geopista.app.reports.maps.vo.SelectedColumnVO;
import com.geopista.app.reports.maps.vo.SelectedLayerVO;
import com.geopista.app.reports.maps.vo.SelectedMapVO;

public class MapReportDAO {
	
	public MapReportDAO(){
	}
	
    public List getMapasPrivadosPublicados(Connection conn, int idMunicipio) throws ReportMapException{

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
            throw new ReportMapException(ex);            
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

    public List getCapasMapa(Connection conn, int idMap) throws ReportMapException{

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
            throw new ReportMapException(ex);            
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

    public List getColumnasCapa(Connection conn, int idCapa) throws ReportMapException{

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
            throw new ReportMapException(ex);            
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
