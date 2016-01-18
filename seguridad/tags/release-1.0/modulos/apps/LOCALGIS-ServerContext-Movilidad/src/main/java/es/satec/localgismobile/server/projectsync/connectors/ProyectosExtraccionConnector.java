package es.satec.localgismobile.server.projectsync.connectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.geopista.server.database.CPoolDatabase;


import es.satec.localgismobile.server.Global;
import es.satec.localgismobile.server.projectsync.beans.ExtractionProject;
import es.satec.localgismobile.server.projectsync.beans.ExtractionProjectList;
import es.satec.localgismobile.server.utils.DataSourceLocator;

/**
 * Clase para definir las operaciones de consulta a la BBDD sobre proyectos de extracción
 * @author irodriguez
 *
 */
public class ProyectosExtraccionConnector {

	private static final Logger logger = Logger.getLogger(ProyectosExtraccionConnector.class);
	
	private static final String SQL_LIST_PROJECTS = "SELECT DISTINCT p.id_proyecto, p.nombre_proyecto, p.fecha_extraccion, p.id_entidad, p.id_map  FROM proyectos_extraccion p INNER JOIN celdas_extraccion c ON p.id_proyecto=c.id_proyecto WHERE c.id_usuario_asign=?";
	
	/**
	 * Obtiene una conexión del DataSource.
	 * @param ds El DataSource del que queremos coger la conexión.
	 * @return Una conexión con la BD.
	 */
	protected Connection getConnection() throws SQLException {
		return CPoolDatabase.getConnection();
	}
	
	/**
	 * Obtiene los proyectos de extracción que tienen alguna celda asignada a un determinado usuario
	 */
	public ExtractionProjectList getExtractionProjectList(int idUsuario)throws SQLException {
		List<ExtractionProject> projInternalList = new ArrayList<ExtractionProject>();
 	 	Connection conn=null;
        PreparedStatement ps = null;
        ResultSet rs=null;
        ExtractionProject extProj = null;
    	String idProyecto;
    	String nombreProyecto; 
    	Date fechaExtraccion;
    	int idEntidad;
    	String idMap;
		
	       try{
	            conn=getConnection();  
	            logger.info(">> "+SQL_LIST_PROJECTS+" <<");
	            ps = conn.prepareStatement(SQL_LIST_PROJECTS);
	            ps.setInt(1, idUsuario);
	            rs = ps.executeQuery();
	            //almacenamos los proyectos de extracción en una lista
                while (rs.next())
                {
                	idProyecto = rs.getString(1);
                	nombreProyecto = rs.getString(2);
                	fechaExtraccion = rs.getTimestamp(3);
                	idEntidad = rs.getInt(4);
                	idMap = rs.getString(5);
                	extProj = new ExtractionProject(idProyecto, nombreProyecto, fechaExtraccion, idEntidad, idMap);
                	projInternalList.add(extProj);
                }

	      } catch (SQLException e) {
			   logger.error("ERROR al obtener los proyectos de extraccion asociados al usuario "+idUsuario+" :"+e,e);
	           throw e;
	       }finally {
	           try {
	        	   rs.close();
	        	   ps.close();
	        	   conn.close();
	           }catch (Exception e) {}
	       }
	       
	       ExtractionProjectList projListReturn = new ExtractionProjectList(projInternalList);
	       return projListReturn;
	}
	
}
