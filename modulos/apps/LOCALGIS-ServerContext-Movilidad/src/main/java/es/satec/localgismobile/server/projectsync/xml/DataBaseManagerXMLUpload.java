/**
 * DataBaseManagerXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.editor.LightGeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.ui.dialogs.global.Constants_LCGIII;
import com.geopista.ui.dialogs.global.Utils;
import com.geopista.util.GeopistaStreamHandleFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;

import es.satec.localgismobile.server.config.ConfigurationManager;
import es.satec.localgismobile.server.projectsync.MobilePermissionException;
import es.satec.localgismobile.server.projectsync.beans.ResultString;
import es.satec.localgismobile.server.projectsync.xml.beans.AttributeXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.FeatureXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.MetadataXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.ResourceXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.ResourcesXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.eiel.XMLUploadEIEL;
import es.satec.localgismobile.server.projectsync.xml.exceptions.ServerException;
import es.satec.localgismobile.server.projectsync.xml.licencias.XMLUploadInvAndLic;

public class DataBaseManagerXMLUpload {

	public com.geopista.security.SecurityManager sm;
	
	private static Logger logger = Logger
			.getLogger(DataBaseManagerXMLUpload.class);

	private LightGeopistaEditor geopistaEditor = null;
	private AdministradorCartografiaClient acClient;

	private static String SERVER = ConfigurationManager
			.getApplicationProperty(ConstantsXMLUpload.PROP_GEOPISTA_CON_SERVER);

	private final static String urlAdmCar = SERVER
	+ "/" + WebAppConstants.GEOPISTA_WEBAPP_NAME + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME;
	
	private final static String urlAdmCarEIEL = SERVER
	+ "/" + WebAppConstants.EIEL_WEBAPP_NAME + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME;
	
	private SvgConversorXMLUpload svgConversor;
	private String urlDocServlet;

	private XMLUploadInvAndLic xmlUploadInvAndLic;
	private XMLUploadEIEL xmlUploadEIEL;

	public DataBaseManagerXMLUpload(com.geopista.security.SecurityManager sm)
			throws ServerException {
		this.sm = sm;
		initialize();
	}
	
	private void initialize() throws ServerException{
		try {			
			geopistaEditor = new LightGeopistaEditor();

			AppContext.releaseResources();
			acClient = new AdministradorCartografiaClient(urlAdmCar, sm);
			svgConversor = new SvgConversorXMLUpload();
			urlDocServlet = urlAdmCar + "/DocumentServlet";
					
			String urlInventario = urlAdmCar + "/InventarioServlet";
			xmlUploadInvAndLic = new XMLUploadInvAndLic(urlInventario, new InventarioClient(urlInventario,sm));
			//CAMBIAR por /LicenciasObra /LicenciasActividad ...
			CConstantesComando.servletLicenciasUrl = SERVER + "/Licencias" + "/CServletLicencias"; 
						//CAMBIAR por /Eiel/EIELServlet
			//String urlEIEL = urlAdmCarEIEL + "/EIELServlet";
			String urlEIEL = urlAdmCar + "/EIELServlet";
			xmlUploadEIEL = new XMLUploadEIEL(urlEIEL, new LocalGISEIELClient(urlEIEL), sm);				
					
		} catch (Exception e) {
			logger.error("Error al conectarse al admcar: " + e, e);
			throw new ServerException(
					"Error al conectarse al administrador de cartografia. "
							+ e.getMessage());
		} catch (Error e) {
			e.printStackTrace();
		}
	}
		
	public void setSmIdMunicipio(String idMunicipio){
		if(idMunicipio!=null){
			sm.setIdMunicipioNS(idMunicipio);
			List<Municipio> alMunicipios = new ArrayList<Municipio>();
			Municipio municipio = new Municipio();
			municipio.setId(Integer.valueOf(idMunicipio));
			alMunicipios.add(municipio);
			sm.getSession().setAlMunicipios(alMunicipios);
			AppContext.setIdMunicipio(Integer.valueOf(idMunicipio));
		}
	}	

	/**
	 * Metodo para cargar una capa a partir de la Base de Datos.
	 * 
	 * @throws ServerException
	 */
	public void loadLayer(String systemIdLayer, Geometry mapGeom, int srid)
			throws ServerException {			
		// CARGA DE LA CAPA
		GeopistaLayer layer = null;
		try {
			// TODO: comentar cuando esto funcione
			// mapGeom = null;

			layer = acClient.loadLayer(null, systemIdLayer, ConstantsXMLUpload.LOCALE, mapGeom,
					FilterLeaf.equal("1", new Integer(1)), true, srid,null);
		} catch (Exception e) {
			logger.error("Error al cargar la capa " + systemIdLayer + ": " + e,
					e);
			throw new ServerException("Error al cargar la capa "
					+ systemIdLayer);
		}

		if (layer.getName() == null || layer.getName().equals("error")) {
			logger.warn("Problemas de permisos");
		}
		//layer.setLayerManager(geopistaEditor.getContext().getLayerManager());
		layer.setLayerManager(geopistaEditor.getLayerManager());

		GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
		URL urlLayer = null;
		try {
			URLStreamHandler createURLStreamHandler = new GeopistaStreamHandleFactory()
					.createURLStreamHandler("geopistalayer");
			urlLayer = new URL(null, "geopistalayer://default/"
					+ ((GeopistaLayer) layer).getSystemId(),
					createURLStreamHandler);
		} catch (MalformedURLException e) {
			logger.error("Error al acceder a la capa " + systemIdLayer + " "
					+ e, e);
			throw new ServerException("Error al acceder a la capa "
					+ systemIdLayer + " " + e.getMessage());
		}

		Properties properties = new Properties();
		properties.put("nodofiltro", FilterLeaf.equal("1", new Integer(1)));
		serverDataSource.setProperties(properties);
		layer.setFeatureCollectionModified(false);
		DataSourceQuery dataSourceQuery = new DataSourceQuery();
		dataSourceQuery.setQuery(urlLayer.toString());
		dataSourceQuery.setDataSource(serverDataSource);
		layer.setDataSourceQuery(dataSourceQuery);

		geopistaEditor.getLayerManager().addLayer(layer.getName(), layer);
		GeopistaLayer l1 = (GeopistaLayer) geopistaEditor.getLayerManager()
				.getLayer(systemIdLayer);
		logger.info("Capa cargada: " + l1.getName());
	}

	/**
	 * Función que se encarga de eliminar, añadir o modificar metadata
	 * 
	 * @param results
	 *            modificarla para meter informacion sobre los resultados
	 *            obtenidos
	 * @param groupList
	 * @throws Exception
	 */
	public void updateMetadata(MetadataXMLUpload metadata, ResultString results)
			throws Exception,MobilePermissionException {
		try {
			logger.info("Iniciado updateMetadata()");
			String systemIdLayer = metadata.getLayerId();
			String numExpediente = ConstantsXMLUpload.NUM_EXP_PREFIX + metadata.getIdFeatures();
			int operationType = Integer.parseInt(metadata.getChangeType());			
			logger.info("Mascara de operacion: " + operationType);
			boolean opInsercion = (operationType & ConstantsXMLUpload.MASK_CHANGE_TYPE_INS) != 0;
			boolean opEliminacion = (operationType & ConstantsXMLUpload.MASK_CHANGE_TYPE_DEL) != 0;
			boolean opMetadata = (operationType & ConstantsXMLUpload.MASK_CHANGE_TYPE_METADATA) != 0;

			CExpedienteLicencia cExpLic = null;
			/**
			 * insercion de metadata = 9 borrado de metadata = 4 modificacion de
			 * metadata = 8
			 */
			// inserción de metadata. Solo para Bienes.
			if (opInsercion && opMetadata) {
				logger.info("Insertando metadata....");

				// INVENTARIO
				if (Utils.isInArray(Constants_LCGIII.TIPOS_INVENTARIO,systemIdLayer)) {
					//insertamos el bien
					xmlUploadInvAndLic.insertaBien(metadata, results);
				}
				// EIEL
				if(Utils.isInArray(Constants_LCGIII.TIPOS_EIEL,systemIdLayer)){
					xmlUploadEIEL.insertarModificarEIEL(metadata, results, systemIdLayer);
				}
			}
			// borrado de metadata
			else if (opEliminacion) {
				logger.info("Eliminando metadata...");

				// LICENCIAS
				// COperacionesLicencias.bloquearExpediente(numExpediente,
				// true);
				// INVENTARIO
				if (Utils.isInArray(Constants_LCGIII.TIPOS_INVENTARIO, systemIdLayer)) {
					// obtenemos los datos de búsqueda del inventario
					xmlUploadInvAndLic.borrarBien(metadata, results);
				}
				// EIEL
				if(Utils.isInArray(Constants_LCGIII.TIPOS_EIEL,systemIdLayer)){
					xmlUploadEIEL.borrarEIEL(metadata, results, systemIdLayer);
				}
			}
			// modificación de metadata
			else if (opMetadata) {
				logger.info("Modificando metadata... ");

				// LICENCIAS
				if(Utils.isInArray(Constants_LCGIII.TIPOS_LICENCIAS, systemIdLayer)){
					xmlUploadInvAndLic.modificarLicencia(metadata, systemIdLayer, cExpLic, numExpediente);
				}
				// INVENTARIO
				if (Utils.isInArray(Constants_LCGIII.TIPOS_INVENTARIO, systemIdLayer)) {
					xmlUploadInvAndLic.modificarBien(metadata, results);
				}
				// EIEL
				if(Utils.isInArray(Constants_LCGIII.TIPOS_EIEL, systemIdLayer)){
					xmlUploadEIEL.insertarModificarEIEL(metadata, results, systemIdLayer);
				}
			}
		} 
		catch (Exception e) {
			logger.error("Se ha producido un error al actualizar los elementos EIEL:",e);
			//CAMBIAR : DESCOMENTAR
	        Throwable errorCause = e.getCause();
            if(errorCause instanceof PermissionException){
				String errorMsg = "Error de Permisos. Se ha producido un error en los metadatos:  "
					+ e.getMessage();
				String strFinalError = "<path id=" + "\"" + metadata.getIdFeatures().get("id")
					+ "\" v1=\"" + errorMsg + "\" " + metadata.getGeometry()
					+ " />";
				throw new MobilePermissionException(strFinalError + "\n");
            	
            } else if(errorCause instanceof com.geopista.server.administradorCartografia.LockException){
				String errorMsg = "El objeto está bloqueado por otro usuario:  "
					+ e.getMessage();
				String strFinalError = "<path id=" + "\"" + metadata.getIdFeatures().get("id")
					+ "\" v1=\"" + errorMsg + "\" " + metadata.getGeometry()
					+ " />";
				throw new MobilePermissionException(strFinalError + "\n");
            	
            }
            else{
	
				String errorMsg = "Se ha producido un error en los metadatos: "
						+ e.getMessage();
				String strFinalError = "<path id=" + "\"" + metadata.getIdFeatures().get("id")
						+ "\" v1=\"" + errorMsg + "\" " + metadata.getGeometry()
						+ " />";
				throw new Exception(strFinalError + "\n");
            }
		}
	}
	

	/**
	 * Metodo para buscar una feature almacenada en la capa y actualizarla.
	 * 
	 * @param resourcesXMLUpload
	 * @param results
	 *            variable donde se puede añadir información sobre los
	 *            resultados obtenidos
	 * @param attrList
	 * @param changeType
	 * @param m
	 * @param l
	 * @param list
	 */
	public void updateFeature(String systemIdLayer, String primaryKeyId, 
			FeatureXMLUpload featureXML, double despX, double despY,
			ResourcesXMLUpload resourcesXMLUpload, ResultString results,
			int srid, List<MetadataXMLUpload> metadataList) throws Exception {
		
	
		logger.info("Iniciado updateFeature()");
		String primaryKeyIdValue = featureXML.getIdFeature();
		//NUEVO
		String primaryKeyIdMunicipioValue = featureXML.getIdMunicipioFeature();
		setSmIdMunicipio(primaryKeyIdMunicipioValue);
		
		//Estado MOVILIDAD (9699999999)
		if(Utils.isInArray(Constants_LCGIII.TIPOS_EIEL, systemIdLayer)){
			AppContext.getApplicationContext().getBlackboard().put(Const.ESTADO_VALIDACION, Const.ESTADO_PUBLICABLE_MOVILIDAD);
		}
		//FIN NUEVO
		List<AttributeXMLUpload> attrList = featureXML.getDataBaseAttFeatList();
		String changeType = featureXML.getChangeType();
				
		/*
		 * tipo de operación
		 * 
		 * inserción=1, modificación=2, borrado=4, metadatos=8, ins+meta=10,
		 * ins+mod=12
		 */
		int operationType = Integer.parseInt(changeType);
		boolean opInsercion = (operationType & ConstantsXMLUpload.MASK_CHANGE_TYPE_INS) != 0;
		boolean opModificacion = (operationType & ConstantsXMLUpload.MASK_CHANGE_TYPE_MOD) != 0;
		boolean opEliminacion = (operationType & ConstantsXMLUpload.MASK_CHANGE_TYPE_DEL) != 0;
		String tipoOperacionStr = "";
		// personalizar mensaje
		if (opEliminacion) {
			tipoOperacionStr = "eliminacion";
		} else if (opModificacion) {
			tipoOperacionStr = "modificacion";
		} else if (opInsercion) {
			tipoOperacionStr = "insercion";
		}

		String errorDescriptionBase = "Error: No se ha podido realizar la "
				+ tipoOperacionStr + " de la feature " + primaryKeyId + " - "
				+ primaryKeyIdValue + ".";

		GeopistaFeature featureGIS = null;

		// modificación o borrado
		if (opModificacion || opEliminacion) {
			Collection collection = null;
			// primero buscamos la feature en BBDD
			try {
				collection = geopistaEditor.searchByAttribute(systemIdLayer,
						primaryKeyId, primaryKeyIdValue);
			} catch (Exception e) {
				logger
						.error(
								errorDescriptionBase
										+ " No se ha podido obtener la feature de base de datos: "
										+ e, e);
				throw new Exception(
						errorDescriptionBase
								+ " No se ha podido obtener la feature de base de datos.");
			}
			Iterator it = collection.iterator();
			if (it.hasNext()) {
				// feature a modificar o eliminar
				featureGIS = (GeopistaFeature) it.next();

				// borrado
				if (opEliminacion) {
					logger.info("Borrando... " + primaryKeyId + " - "
							+ featureGIS.getAttribute(primaryKeyId));
					featureGIS.setDeleted(true);
					
				}
				// modificacion
				else if (opModificacion && attrList != null
						&& attrList.size() > 1) {
					logger.info("Actualizando... " + primaryKeyId + " - "
							+ featureGIS.getAttribute(primaryKeyId));
					// modificación de la feature
					AttributeXMLUpload attFeat = null;
					String attFeatName = null;
					String attFeatValue = null;
					for (int i = 0; i < attrList.size(); i++) {
						attFeat = attrList.get(i);
						attFeatName = attFeat.getKey();
						attFeatValue = attFeat.getValue();
						if (!attFeatName.equals(primaryKeyId)) {
							logger.info("> Modificando atributo feature => "
									+ attFeatName + "=" + attFeatValue);
							featureGIS.setAttribute(attFeatName, attFeatValue);
						}
					}
				}

			} else {
				throw new Exception(errorDescriptionBase
						+ " No existe dicha feature en base de datos.");
			}
		}
		// inserción
		else if (opInsercion) {
			logger.info("Insertando nueva feature...");

			GeopistaLayer geolayer = (GeopistaLayer) geopistaEditor
					.getLayerManager().getLayer(systemIdLayer);
			FeatureSchema featureSchema = geolayer
					.getFeatureCollectionWrapper().getFeatureSchema();

			// creación de una nueva feature
			featureGIS = new GeopistaFeature(featureSchema);
			// feature.setSystemId(null);
			featureGIS.setLayer(geolayer);
			// recibimos las coordenadas ya convertidas del SVG
			Geometry geom = svgConversor.convertSVG(featureXML, despX, despY);
			featureGIS.setGeometry(geom);
			featureGIS.setNew(true);
			AttributeXMLUpload attFeat = null;
			String attFeatName = null;
			String attFeatValue = null;
			for (int i = 0; i < attrList.size(); i++) {
				attFeat = attrList.get(i);
				attFeatName = attFeat.getKey();
				attFeatValue = attFeat.getValue();
				if (!attFeatName.equals(primaryKeyId)) {
					logger.info("> Agregando atributo nueva feature => "
							+ attFeatName + "=" + attFeatValue);
					featureGIS.setAttribute(attFeatName, attFeatValue);
				}
			}

		}

		// aqui es donde realmente se realiza la operación en BBDD
		Object[] updateFeatArray = new Object[1];
		updateFeatArray[0] = featureGIS;
		boolean bLoadData = false;
		boolean bValidateData = true;
		try {
			if (opInsercion) {
				bLoadData = true;
			} // para recibir resultados
			Collection uploadedFeatures = acClient.uploadFeatures(ConstantsXMLUpload.LOCALE,
					updateFeatArray, bLoadData, bValidateData, null, srid);
			// información añadida sobre las inserciones para poder trabajar
			// desde la pda
			if (opInsercion) {
				for (Iterator iterator = uploadedFeatures.iterator(); iterator
						.hasNext();) {
					GeopistaFeature gf = (GeopistaFeature) iterator.next();
					logger.info("Id feature insertada: " + gf.getSystemId());
					results.setText(results.getText() + "\n<insert id=\""
							+ featureXML.getId() + "\" v2=\""
							+ gf.getSystemId() + "\"/>");
				}
			}
//			if(opEliminacion){
//				Iterator it = metadataList.iterator();
//				while(it.hasNext()){
//					MetadataXMLUpload metadataXMLUpload = (MetadataXMLUpload) it.next();
//					if (Utils.isInArray(Constants.TIPOS_INVENTARIO, systemIdLayer)) {
//						// obtenemos los datos de búsqueda del inventario
//						xmlUploadInvAndLic.borrarBien(metadataXMLUpload, results);
//					}
//					// EIEL
//					if(Utils.isInArray(Constants.TIPOS_EIEL,systemIdLayer)){
//						xmlUploadEIEL.borrarEIEL(metadataXMLUpload, results, systemIdLayer);
//					}
//				}
//			}
		} catch (Exception e) {
			logger.error(errorDescriptionBase + ": " + e, e);
			throw new Exception(errorDescriptionBase + e.getMessage());
		}

		logger.info("La " + tipoOperacionStr
				+ " de la feature se ha realizado satisfactoriamente.");

		// modificación de resources
		if (opInsercion || opModificacion) {
			boolean updateResources = false;
			try {
				updateResources = updateFeatureResources(featureXML,
						resourcesXMLUpload, featureGIS);
			} catch (Exception e) {
				logger.error("Error al agregar los resources a la feature. "
						+ e, e);
				throw new Exception(
						"Error al agregar los resources a la feature. "
								+ e.getMessage());
			}
			if (updateResources == true) {
				logger.info("Se han agregado resources a la feature.");
			}
		}

	}

	/**
	 * Actualiza los resources de la feature
	 * 
	 * @param resourcesXMLUpload
	 * @param featureXML
	 * @param featureGIS
	 * @throws Exception
	 */
	private boolean updateFeatureResources(FeatureXMLUpload featureXML,
			ResourcesXMLUpload resourcesXMLUpload, GeopistaFeature featureGIS)
			throws Exception {
		logger.info("Iniciado updateFeatureResources()");
		boolean updateResources = false;
		if (featureGIS == null) {
			return updateResources;
		}

		List<String> featImageUrls = featureXML.getImageUrls();
		Map<String, ResourceXMLUpload> resourcesMap = resourcesXMLUpload
				.getResourcesMap();

		if (resourcesMap == null || resourcesMap.size() <= 0) {
			return updateResources;
		}

		// objetos para realizar la actualización en BBDD
		DocumentClient documentClient = new DocumentClient(urlDocServlet);

		// buscamos los resources a actualizar
		String url = null;
		ResourceXMLUpload resourceXML = null;
		DocumentBean document = null;
		String resourceBody = null;
		String fileName = null;
		byte[] byteOut = null;
		GeopistaFeature[] featureArray = { featureGIS };
		for (int i = 0; i < featImageUrls.size(); i++) {
			url = featImageUrls.get(i);
			// si tenemos el resource en el fichero XML
			if (resourcesMap.containsKey(url)) {
				resourceXML = resourcesMap.get(url);
				// creación del documento para añadir a la feature
				document = new DocumentBean();
				resourceBody = resourceXML.getBody();
				if (resourceBody != null && resourceBody.length() > 0) {
					byteOut = Base64.decodeBase64(resourceBody.getBytes());
					document.setContent(byteOut);
					document.setSize(resourceBody.length());
					fileName = url.substring(url.lastIndexOf("/"));
					document.setFileName(fileName);
					if (resourceXML.getType().equals(
							ConstantsXMLUpload.RESOURCE_IMAGE_TYPE)) {
						document.setIsImagen();
					}
					// aqui es donde verdaderamente se realiza la modificación
					// en BBDD
					documentClient.attachDocument(featureArray, document);
					updateResources = true;
				}
			}
		}

		return updateResources;
	}
	
}
