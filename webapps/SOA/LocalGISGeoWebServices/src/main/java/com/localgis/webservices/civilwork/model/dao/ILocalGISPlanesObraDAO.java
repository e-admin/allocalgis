/**
 * ILocalGISPlanesObraDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.naming.ConfigurationException;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISPlanesObra;
import com.localgis.webservices.civilwork.model.ot.StatisticalDataOT;
import com.localgis.webservices.civilwork.util.OrderByColumn;


public interface ILocalGISPlanesObraDAO
{

	/**
	 * Metodo para insertar una nueva nota en LocalGIS
	 * @param connection - Conexion a base de datos
	 * @param warning - Objeto de tipo LocalGISNote
	 * @throws SQLException - Excepcion al insertar nota
	 */
	public void addPlanesObra(Connection connection,LocalGISPlanesObra warning) throws SQLException;
	
	/**
	 * Metodo para asociar geometrias a las notas. Debe existir el id de nota.
	 * @param connection - Conexion a base de datos
	 * @param layerFeatureBean - Lista de relaciones de idLayer e idFeature 
	 * @param idWarning - id de la nota relacionada
	 * @throws SQLException - Excepcion al insertar relacion
	 */
	public void addPlanesObraLayerFeatureReferences(Connection connection,ArrayList<LayerFeatureBean> layerFeatureBean,Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para agregar ficheros asociados a base de datos (No se usa por el momento). Debe existir el id de nota.
	 * @param connection - conexion a base de datos
	 * @param idWarning - id de la nota relacionada
	 * @param documents - lista de documentos a insertar
	 * @throws SQLException - Error al insertar el documento
	 */
	public void addPlanesObraDocument(Connection connection,Integer idWarning, Document document) throws SQLException;
	
	/**
	 * Metodo que genera un thumbnail a partir de una imagen y agrega el thumbnail a la base de datos.
	 * @param connection - conexion a base de datos
	 * @param document - imagen asociada
	 * @throws SQLException - Error al insertar el thumbnail
	 */
	public void addPlanesObraDocumentThumbnail(Connection connection,Document document) throws SQLException;
	
	/**
	 * Metodo para listar las notas
	 * @param connection - Conexion a base de datos
	 * @param description - Descripcion asociada - se le agregaran los elementos %descripcion%
	 * @param from - fecha desde
	 * @param to - fecha hasta
	 * @param startElement - elemento inicial
	 * @param range - Numero de elementos a devolver
	 * @param idMunicipio - id de municipio
	 * @param userId - id de usuario
	 * @param orderColumns - lista ordenada de columnas. Por defecto, si es nulo, lo hace por fecha de alta
	 * @param features - Features que pueden contener notas.
	 * @return Lista de notas ordenadas por orderColumns
	 * @throws SQLException - En caso de error al realizar la consulta.
	 */
	public ArrayList<LocalGISPlanesObra> getPlanesObraList(Connection connection,
			String description, Calendar fromStart, Calendar toStart,Calendar fromAprobacion,
			Calendar toAprobacion, Integer startElement, Integer range, Integer idMunicipio, Integer userId,
			ArrayList<OrderByColumn> orderColumns, ArrayList<LayerFeatureBean> features, 
			String plan, String anios, String nombre, String paraje, Double presupuestoEstimado, 
			Double presupuestoDefinitivo, String existeProyecto, String infraestructura, String obraNueva, 
			String estudioAmbiental, String datosEIEL, String permisos, String personaContacto, 
			String telefonoContacto, String[] documentosAdjuntos, 
			String[] serviciosAfectados, String destinatario, String recibi,
			Calendar toFechaRecibi, Calendar fromFechaRecibi, String supervision, String directorProyecto,
			String autorProyecto, String directorObra, String empresaAdjudicataria, 
			Calendar toFechaResolucion, Calendar fromFechaResolucion, Double presupuestoAdjudicacion,
			String coordinadorSegSalud, Calendar toActaReplanteo, Calendar fromActaReplanteo, 
			Calendar toFechaComienzo,  Calendar fromFechaComienzo, 	Calendar toFechaFinalizacion, 
			Calendar fromFechaFinalizacion, Calendar toProrrogas, Calendar fromProrrogas, 
			Calendar toActaRecepcion, Calendar fromActaRecepcion, Double certificacionFinal, 
			Calendar toResolucionCertificacion, Calendar fromResolucionCertificacion, 
			Calendar toInformacionCambiosEIEL, Calendar fromInformacionCambiosEIEL, Calendar toReformados, 
			Calendar fromReformados, Double liquidacion, Calendar toFechaLiquidacion, 
			Calendar fromFechaLiquidacion, String detalles, Calendar toFechaSolicitud, Calendar fromFechaSolicitud, 
			Calendar toFechaAprobacionPrincipado, Calendar fromFechaAprobacionPrincipado, Integer idEntidad) throws SQLException;
	
	/**
	 * Metodo para obtener una nota en particular.
	 * @param connection - Conexion a base de datos
	 * @param idWarning - id de nota
	 * @param idMunicipio - id de municipio
	 * @param idUser - id de usuario
	 * @return - Nota devuelta o null si no existe la nota con esos datos
	 * @throws SQLException - En caso de error al consultar.
	 */
	public LocalGISPlanesObra getPlanesObra(Connection connection,Integer idWarning,Integer idMunicipio,Integer idUser, Integer idEntidad) throws SQLException,ConfigurationException;
	
	/**
	 * Metodo para obtener las relaciones de nota-lista<layer-feature>
	 * @param connection conexion a base de datos
	 * @param idWarning - id de nota
	 * @return lista de layer - feature para un id de nota dado
	 * @throws SQLException - En caso de error al consultar.
	 */
	public ArrayList<LayerFeatureBean> getPlanesObraLayerFeatureReferences(Connection connection,Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para obtener la lista de documentos de una nota dada
	 * @param connection - conexion a base de datos
	 * @param idWarning - id de nota
	 * @param returnCompleteDocuments - booleano - si se requieren los documentos completos (fichero y thumbnail si procede), o solo los datos de texto
	 * @return lista de documentos
	 * @throws SQLException - Error al consultar.
	 */
	public ArrayList<Document> getPlanesObraDocuments(Connection connection,Integer idWarning,Boolean returnCompleteDocuments) throws SQLException;

	/**
	 * Metodo para obtener el documento de una nota dada
	 * @param connection - conexion a base de datos
	 * @param idDocument - id del documento a obtener
	 * @param returnCompleteDocuments - si queremos el documento completo (fichero y thumbnail si procede)
	 * @return - Documento
	 * @throws SQLException - Error al consultar
	 */
	public Document getPlanesObraDocument(Connection connection,Integer idDocument,Boolean returnCompleteDocuments) throws SQLException;
	

	/**
	 * Metodo para indicar el numero de notas
	 * @param connection - Conexion a base de datos
	 * @param description - Descripcion asociada - se le agregaran los elementos %descripcion%
	 * @param from - fecha desde
	 * @param to - fecha hasta
	 * @param idMunicipio - id de municipio
	 * @param userId - id de usuario
	 * @param features - Features que pueden contener notas.
	 * @return Lista de notas ordenadas por orderColumns
	 * @throws SQLException - En caso de error al realizar la consulta.
	 */
	public int getNumPlanesObra(Connection connection, String description, Calendar fromStart, Calendar toStart,
			Integer startElement, Integer idMunicipio, Integer userId,	ArrayList<LayerFeatureBean> features, 
			Calendar fromAprobacion, Calendar toAprobacion, String plan, String anios, String nombre, String paraje, Double presupuestoEstimado, 
			Double presupuestoDefinitivo, String existeProyecto, String infraestructura, String obraNueva, 
			String estudioAmbiental, String datosEIEL, String permisos, String personaContacto, 
			String telefonoContacto, String[] documentosAdjuntos, 
			String[] serviciosAfectados, String destinatario, String recibi,
			Calendar toFechaRecibi, Calendar fromFechaRecibi, String supervision, String directorProyecto,
			String autorProyecto, String directorObra, String empresaAdjudicataria, 
			Calendar toFechaResolucion, Calendar fromFechaResolucion, Double presupuestoAdjudicacion,
			String coordinadorSegSalud, Calendar toActaReplanteo, Calendar fromActaReplanteo, 
			Calendar toFechaComienzo,  Calendar fromFechaComienzo, 	Calendar toFechaFinalizacion, 
			Calendar fromFechaFinalizacion, Calendar toProrrogas, Calendar fromProrrogas, 
			Calendar toActaRecepcion, Calendar fromActaRecepcion, Double certificacionFinal, 
			Calendar toResolucionCertificacion, Calendar fromResolucionCertificacion, 
			Calendar toInformacionCambiosEIEL, Calendar fromInformacionCambiosEIEL, Calendar toReformados, 
			Calendar fromReformados, Double liquidacion, Calendar toFechaLiquidacion, 
			Calendar fromFechaLiquidacion, String detalles, Calendar toFechaSolicitud, Calendar fromFechaSolicitud, 
			Calendar toFechaAprobacionPrincipado, Calendar fromFechaAprobacionPrincipado, Integer idEntidad) throws SQLException;
	
	/**
	 * Metodo para eliminar de base de datos una nota. Borra la nota y todos sus objetos relacionados.
	 * @param connection - conexion a base de datos
	 * @param idWarning - id de nota
	 * @throws SQLException - error al eliminar nota.
	 */
	public void removePlanesObra(Connection connection,Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para eliminar las relaciones entre features de una nota
	 * @param connection - Conexion a base de datos
	 * @param idWarning - id de nota
	 * @throws SQLException - Error al eliminar nota
	 */
	public void removePlanesObraLayerFeatureReferences(Connection connection,Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para eliminar los documentos de una nota
	 * @param connection - conexion a base de datos
	 * @param idWarning - id de nota
	 * @throws SQLException - Error al eliminar documento
	 */
	public void removePlanesObraDocuments(Connection connection, Integer idWarning) throws SQLException;
	
	/**
	 * Metodo para eliminar un documento en particular.
	 * @param connection - conexion a base de datos
	 * @param idDocument - id del documento al eliminar
	 * @throws SQLException - Error al eliminar el documento
	 */
	public void removePlanesObraDocument(Connection connection, Integer idDocument) throws SQLException;
	
	/**
	 * Metodo para modificar una nota
	 * @param connection - conexion a base de datos
	 * @param note - nota cuyo id es el id de la nota que hay que modificar
	 * @throws SQLException - Error al modificar la nota
	 */
	public void setPlanesObra(Connection connection, LocalGISPlanesObra planesObra) throws SQLException;
	
	/**
	 * Metodo para obtener un id para una nueva nota
	 * @param connection - conexion a base de datos
	 * @return - id de la nueva nota
	 * @throws SQLException - error al obtener id de nota 
	 */
	public Integer getNextIdPlanesObraSequence(Connection connection)throws SQLException;
	
	/**
	 * Metodo para obtener un id para un nuevo documento
	 * @param connection - conexion a base de datos
	 * @return - id del nuevo documento
	 * @throws SQLException - error al obtener id de documento 
	 */
	public Integer getNextIdDocumentPlanesObraSequence(Connection connection)throws SQLException;

	void addPlanesObraSpecificInfo(Connection connection,
			LocalGISPlanesObra planesObra) throws SQLException,
			ConfigurationException;

	void setPlanesObraSpecificInfo(Connection connection,
			LocalGISPlanesObra planesObra) throws SQLException;

	ArrayList<StatisticalDataOT> getStatistics(Connection connection,
			Integer idEntidad) throws SQLException;
	
	/**
	 * Metodo para obtener el thumbnail de un documento imagen
	 * @param connection - conexion a base de datos
	 * @param idNote - id del nuevo documento
	 * @param documento - Documento
	 * @throws SQLException - error al obtener id de documento 
	 */
	//public void getNoteDocumentThumbnail(Connection connection, Integer idNote,DocumentoAviso document) throws SQLException;
}


