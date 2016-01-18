package com.localgis.webservices.civilwork.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.naming.ConfigurationException;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.webservices.civilwork.model.ot.StatisticalDataOT;
import com.localgis.webservices.civilwork.util.OrderByColumn;


public interface ILocalGISInterventionsDAO
{

	/**
	 * Metodo para insertar una nueva intervencion en LocalGIS. Informacion basica
	 * @param connection - Conexion a base de datos
	 * @param warning - Objeto de tipo LocalGISNote
	 * @throws SQLException - Excepcion al insertar intervencion
	 */
	public void addIntervention(Connection connection,LocalGISIntervention warning) throws SQLException;
	
	/**
	 * Metodo para asociar geometrias a las notas. Debe existir el id de intervencion.
	 * @param connection - Conexion a base de datos
	 * @param layerFeatureBean - Lista de relaciones de idLayer e idFeature 
	 * @param idWarning - id de la intervencion relacionada
	 * @throws SQLException - Excepcion al insertar relacion
	 */
	public void addInterventionLayerFeatureReferences(Connection connection,ArrayList<LayerFeatureBean> layerFeatureBean,Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para agregar ficheros asociados a base de datos (No se usa por el momento). Debe existir el id de intervencion.
	 * @param connection - conexion a base de datos
	 * @param idWarning - id de la intervencion relacionada
	 * @param documents - lista de documentos a insertar
	 * @throws SQLException - Error al insertar el documento
	 */
	public void addInterventionDocument(Connection connection,Integer idWarning, Document document) throws SQLException;
	
	/**
	 * Metodo que genera un thumbnail a partir de una imagen y agrega el thumbnail a la base de datos.
	 * @param connection - conexion a base de datos
	 * @param document - imagen asociada
	 * @throws SQLException - Error al insertar el thumbnail
	 */
	public void addInterventionDocumentThumbnail(Connection connection,Document document) throws SQLException;
	
	/**
	 * Metodo para listar las intervenciones
	 * @param connection - Conexion a base de datos
	 * @param description - Descripcion asociada - se le agregaran los elementos %descripcion%
	 * @param from - fecha desde
	 * @param to - fecha hasta
	 * @param startElement - elemento inicial
	 * @param range - Numero de elementos a devolver
	 * @param idMunicipio - id de municipio
	 * @param userId - id de usuario
	 * @param orderColumns - lista ordenada de columnas. Por defecto, si es nulo, lo hace por fecha de alta
	 * @param features - Features que pueden contener intervenciones.
	 * @return Lista de notas ordenadas por orderColumns
	 * @throws SQLException - En caso de error al realizar la consulta.
	 */
	public ArrayList<LocalGISIntervention> getInterventionList(
			Connection connection, String description, Calendar fromStart,
			Calendar toStart,Calendar fromNext,
			Calendar toNext, Integer startElement, Integer range,
			Integer idMunicipio, Integer userId,
			ArrayList<OrderByColumn> orderColumns,
			ArrayList<LayerFeatureBean> features, String actuationType,
			String interventionType, Double foreseenBudgetFrom, Double foreseenBudgetTo,
			Double workPercentageFrom,Double workPercentageTo, String causes,Boolean isAdministrator) throws SQLException;
	
	/**
	 * Metodo para obtener una intervencion en particular con los datos basicos.
	 * @param connection - Conexion a base de datos
	 * @param idWarning - id de intervencion
	 * @param idMunicipio - id de municipio
	 * @param idUser - id de usuario
	 * @return - Nota devuelta o null si no existe la intervencion con esos datos
	 * @throws SQLException - En caso de error al consultar.
	 */
	public LocalGISIntervention getIntervention(Connection connection,Integer idWarning,Integer idMunicipio,Integer idUser) throws SQLException,ConfigurationException;
	
	/**
	 * Metodo para agregar la informacion especifica de una intervencion.
	 * @param connection - Conexion a base de datos
	 * @param idWarning - id de intervencion
	 * @param idMunicipio - id de municipio
	 * @param idUser - id de usuario
	 * @throws SQLException - En caso de error al consultar.
	 */
	public void addInterventionSpecificInfo(Connection connection,LocalGISIntervention intervention) throws SQLException,ConfigurationException;
	
	/**
	 * Metodo para obtener las relaciones de intervencion-lista<layer-feature>
	 * @param connection conexion a base de datos
	 * @param idWarning - id de intervencion
	 * @return lista de layer - feature para un id de intervencion dado
	 * @throws SQLException - En caso de error al consultar.
	 */
	public ArrayList<LayerFeatureBean> getInterventionLayerFeatureReferences(Connection connection,Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para obtener la lista de documentos de una intervencion dada
	 * @param connection - conexion a base de datos
	 * @param idWarning - id de intervencion
	 * @param returnCompleteDocuments - booleano - si se requieren los documentos completos (fichero y thumbnail si procede), o solo los datos de texto
	 * @return lista de documentos
	 * @throws SQLException - Error al consultar.
	 */
	public ArrayList<Document> getInterventionDocuments(Connection connection,Integer idWarning,Boolean returnCompleteDocuments) throws SQLException;

	/**
	 * Metodo para obtener el documento de una intervencion dada
	 * @param connection - conexion a base de datos
	 * @param idDocument - id del documento a obtener
	 * @param returnCompleteDocuments - si queremos el documento completo (fichero y thumbnail si procede)
	 * @return - Documento
	 * @throws SQLException - Error al consultar
	 */
	public Document getInterventionDocument(Connection connection,Integer idDocument,Boolean returnCompleteDocuments) throws SQLException;
	

	/**
	 * Metodo para indicar el numero de intervencion
	 * @param connection - Conexion a base de datos
	 * @param description - Descripcion asociada - se le agregaran los elementos %descripcion%
	 * @param from - fecha desde
	 * @param to - fecha hasta
	 * @param idMunicipio - id de municipio
	 * @param userId - id de usuario
	 * @param features - Features que pueden contener intervenciones.
	 * @throws SQLException - En caso de error al realizar la consulta.
	 */
	public int getNumInterventions(Connection connection, String description,
			Calendar fromStart, Calendar toStart,Calendar fromNext, Calendar toNext, Integer idMunicipio, Integer userId,
			ArrayList<LayerFeatureBean> features, String actuationType,
			String interventionType, Double foreseenBudgetFrom,Double foreseenBudgetTo,
			Double workPercentageFrom,Double workPercentageTo, String causes,Boolean isAdministrator) throws SQLException;
	
	/**
	 * Metodo para eliminar de base de datos una intervencion. Borra la intervencion y todos sus objetos relacionados.
	 * @param connection - conexion a base de datos
	 * @param idWarning - id de intervencion
	 * @throws SQLException - error al eliminar intervencion.
	 */
	public void removeIntervention(Connection connection,Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para eliminar las relaciones entre features de una intervencion
	 * @param connection - Conexion a base de datos
	 * @param idWarning - id de intervencion
	 * @throws SQLException - Error al eliminar intervencion
	 */
	public void removeInterventionLayerFeatureReferences(Connection connection,Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para eliminar los documentos de una intervencion
	 * @param connection - conexion a base de datos
	 * @param idWarning - id de intervencion
	 * @throws SQLException - Error al eliminar documento
	 */
	public void removeInterventionDocuments(Connection connection, Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para eliminar un documento en particular.
	 * @param connection - conexion a base de datos
	 * @param idDocument - id del documento al eliminar
	 * @throws SQLException - Error al eliminar el documento
	 */
	public void removeInterventionDocument(Connection connection, Integer idDocument) throws SQLException;
	
	/**
	 * Metodo para modificar una intervencion
	 * @param connection - conexion a base de datos
	 * @param note - nota cuyo id es el id de la intervencion que hay que modificar
	 * @throws SQLException - Error al modificar la intervencion
	 */
	public void setIntervention(Connection connection, LocalGISIntervention intervention) throws SQLException;
	/**
	 * Metodo para modificar una intervencion
	 * @param connection - conexion a base de datos
	 * @param note - nota cuyo id es el id de la intervencion que hay que modificar
	 * @throws SQLException - Error al modificar la intervencion
	 */
	public void setInterventionSpecificInfo(Connection connection, LocalGISIntervention intervention) throws SQLException;
	
	/**
	 * Metodo para obtener un id para una nueva intervencion
	 * @param connection - conexion a base de datos
	 * @return - id de la nueva intervencion
	 * @throws SQLException - error al obtener id de intervencion 
	 */
	public Integer getNextIdInterventionSequence(Connection connection)throws SQLException;
	
	/**
	 * Metodo para obtener un id para un nuevo documento
	 * @param connection - conexion a base de datos
	 * @return - id del nuevo documento
	 * @throws SQLException - error al obtener id de documento 
	 */
	public Integer getNextIdDocumentInterventionSequence(Connection connection)throws SQLException;

	public void changeWarningDate(Connection connection, Integer idWarning,Calendar calendar) throws SQLException;

	public ArrayList<StatisticalDataOT> getStatistics(Connection connection, Integer idEntidad) throws SQLException;

	public void removeNetworkIncident(Connection connection, Integer idNetwork, Integer idWarning) throws SQLException;

	public Integer getNetworkId(Connection connection,String propertyNetworkName) throws SQLException;

	public Integer getEdgeFromFeature(Connection connection, Integer idNetwork,
			LayerFeatureBean layerFeatureBean) throws SQLException;

	public void addNetworkIncident(Connection connection, Integer idNetwork,Integer idEdge,Integer idWarning, LocalGISIncident incident,String description,Integer incidentType)throws SQLException;

	public Integer getNetworkIncidentType(Connection connection, Integer id)throws SQLException;
}


