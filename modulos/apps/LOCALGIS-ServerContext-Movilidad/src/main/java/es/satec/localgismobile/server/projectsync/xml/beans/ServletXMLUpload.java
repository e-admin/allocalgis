/**
 * ServletXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.geopista.app.AppContext;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import es.satec.localgismobile.server.config.ConfigurationManager;
import es.satec.localgismobile.server.projectsync.MobilePermissionException;
import es.satec.localgismobile.server.projectsync.beans.ResultString;
import es.satec.localgismobile.server.projectsync.xml.ConstantsXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.DataBaseManagerXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.SvgConversorXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.exceptions.FeatureException;
import es.satec.localgismobile.server.projectsync.xml.exceptions.ServerException;

public class ServletXMLUpload {
	
	private static Logger logger = Logger.getLogger(ServletXMLUpload.class);
	
	//información sobre le documento recogida en un bean
	private DocumentXMLUpload docXmlUp;
	
	//campos de parseo auxiliares
	private Document svgDoc;
	private NodeList resources;
	private NodeList metainfo;

	//objeto actualizador de las features en BBDD
	private DataBaseManagerXMLUpload dbXMLManager;
	
	private String sridValue;

	public ServletXMLUpload(com.geopista.security.SecurityManager sm){
		try {
			dbXMLManager = new DataBaseManagerXMLUpload(sm);
		}catch (Exception e) {
			logger.error(e,e);
		}
	}
	
	/**
	 * Desencadena el parseo del fichero
	 * @param f
	 * @return
	 * @throws Exception 
	 */
	public String execute(InputStream is) throws Exception {
		xmlParser(is);
		String results = updateDataBase();
		
		//mensaje de respuesta
		String returnStr = "<upload2localgisresponse code=\""+ConstantsXMLUpload.RESPONSE_CODE_OK+"\">";
		returnStr += "\n" + results;
		returnStr += "\n" + "</upload2localgisresponse>";
		
		return returnStr;
	}

	private void xmlParser(InputStream is) throws Exception{		
		//inicialización de campos internos
        updateInternalFields(is);
        //parsea los elementos ya actualizados
        parseInternalFields();
	}
	
	/**
	 * Obtenemos los elementos necesarios para el parseo del XML. Actualizamos los campos svgDoc y resourcesNode
	 * @param is
	 * @return
	 * @throws Exception
	 */
	private void updateInternalFields(InputStream is) throws Exception {
		ByteArrayInputStream bais = null;
		svgDoc = null;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(is); 
	
	        //nodo raíz
	        Element firstChild = (Element) doc.getFirstChild();
	        
	        //nodo svg
	        Node svgNode = firstChild.getElementsByTagName("svg").item(0);
	        String svgText = svgNode.getFirstChild().getNodeValue();
	        sridValue = ((Element) svgNode).getAttribute("srid");
	        
	        //parseo del SVG 
	        bais = new ByteArrayInputStream(svgText.getBytes("UTF-8"));
	        svgDoc = builder.parse(bais);
	        
	        //nodo resources
	        resources = firstChild.getElementsByTagName("resource");
	        
	        //nodo metainfo
	        metainfo = firstChild.getElementsByTagName("metainfo");
	        
		}catch (Exception e) {
			logger.error("Error en el parseo del XML: " + e,e);
			throw new ServerException("Error en el parseo del XML de subida: Falta algun TAG principal." + e.getMessage());
		}finally {
			try{
			if(bais!=null){bais.close();}
			}catch (Exception e) {}
		}
	}
	
	/**
	 * Parsea los elementos para componer el bean DocumentXMLUpload
	 * @throws ServletException 
	 * @throws ServerException 
	 */
	private void parseInternalFields() throws ServerException {
		try {
	        /** parseo  de los atributos SVG **/
	        List<AttributeXMLUpload> attributeSvgList = new ArrayList<AttributeXMLUpload>();
	        Node svgFirstNode = svgDoc.getFirstChild();
	        NamedNodeMap attributesSvg = svgFirstNode.getAttributes();
	        for (int i = 0; i < attributesSvg.getLength(); i++) {
	        	attributeSvgList.add(new AttributeXMLUpload(attributesSvg.item(i).getNodeName(), attributesSvg.item(i).getNodeValue()));
			}
	        
	        /** GRUPOS **/
	        NodeList gLayersList = svgDoc.getElementsByTagName("g");
	        List<GroupXMLUpload> groupXmlUp = parseGLayers(gLayersList);
	        
	        /** RESOURCES **/
	        Map<String, ResourceXMLUpload> resourceXmlMap = parseResources(resources);
	        
	        /** METAINFO **/
	        List<MetadataXMLUpload> metadataXmlMap = parseMetainfo(metainfo);
	         
	        //componemos las partes del documento
	        SvgXMLUpload svgXmlUp = new SvgXMLUpload(groupXmlUp, attributeSvgList);
	        ResourcesXMLUpload resourcesXmlUp = new ResourcesXMLUpload(resourceXmlMap);
	        MetainfoXMLUpload metainfoXmlUp = new MetainfoXMLUpload(metadataXmlMap);
	        
			//componemos el documento completo
			docXmlUp = new DocumentXMLUpload();
			docXmlUp.setSvg(svgXmlUp);
			docXmlUp.setResources(resourcesXmlUp);
			docXmlUp.setMetainfo(metainfoXmlUp);
			docXmlUp.setSrid(Integer.parseInt(sridValue));
		}catch (Exception e) {
			logger.error("Error en el parseo del XML: " + e,e);
			throw new ServerException("Error en el parseo del XML de subida: Formato incorrecto de nodos internos." + e.getMessage());
		}
	}
	
	/** 
	 * 
	 * @param metainfo2
	 * @return
	 * @throws Exception 
	 */
	private List<MetadataXMLUpload> parseMetainfo(NodeList metainfoSrc) throws Exception {
		List<MetadataXMLUpload> metadataXmlList = new ArrayList<MetadataXMLUpload>();
		
		try {
			if(metainfoSrc==null || metainfoSrc.item(0)==null){ return metadataXmlList;}
			NodeList gLayerNodes = metainfoSrc.item(0).getChildNodes();
			Element gLayer = null;
			String layerId = null;
			NodeList metaPathList = null;
			NodeList metaPolylineList = null;	
			NodeList metaEllipseList = null;	
			NodeList metaCircleList = null;
			//<g l1="ID de licencia" l2="ID de parcela" stroke-dasharray="" editable="true" id="licObra" >
	        for (int i = 0; i < gLayerNodes.getLength(); i++) {
	        	if(!(gLayerNodes.item(i) instanceof Element)){ continue;}
	        	gLayer = (Element) gLayerNodes.item(i);
	        	if(gLayer==null){ continue;}
	        	layerId = gLayer.getAttribute(ConstantsXMLUpload.ATT_SYSTEM_ID_LAYER);
	        	metaPathList = gLayer.getElementsByTagName(ConstantsXMLUpload.ATT_GEOTYPE_PATH);
	        	metaPolylineList = gLayer.getElementsByTagName(ConstantsXMLUpload.ATT_GEOTYPE_POLYLINE);
	        	metaEllipseList = gLayer.getElementsByTagName(ConstantsXMLUpload.ATT_GEOTYPE_ELLIPSE);
	        	metaCircleList = gLayer.getElementsByTagName(ConstantsXMLUpload.ATT_GEOTYPE_CIRCLE);
	        	if(metaPathList==null && metaPolylineList==null && metaEllipseList==null){ continue;}
	        	//<path d="M207.26562 309.90625 L220.83594 314.9375 229.83594 319.9375 z" v1="3232" ...
	        	//<polyline points="1.0,2.0 3.0,4.0 ..." v1="3232" ...
	        	if(metaPathList!=null){
	        		getMetadataFeature(metaPathList, ConstantsXMLUpload.ATT_GEOTYPE_PATH, metadataXmlList, layerId);
	        	}
	        	if(metaPolylineList!=null){
	        		getMetadataFeature(metaPolylineList, ConstantsXMLUpload.ATT_GEOTYPE_POLYLINE, metadataXmlList, layerId);
	        	}
	        	if(metaEllipseList!=null){
	        		getMetadataFeature(metaEllipseList, ConstantsXMLUpload.ATT_GEOTYPE_ELLIPSE, metadataXmlList, layerId);
	        	}
	        	if(metaCircleList!=null){
	        		getMetadataFeature(metaCircleList, ConstantsXMLUpload.ATT_GEOTYPE_CIRCLE, metadataXmlList, layerId);
	        	}
	        }
	        
	        
		}catch (Exception e) {
			throw new Exception("Error en el parseo del metadata. " + e);
		}
        
        return metadataXmlList;
	}

	/**
	 * Añade los campos internos al metadata parseando el xml enviado para cada feature que lo compone
	 * @param metaPathList
	 * @param geometryType
	 * @param metadataXmlList
	 * @param layerId
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void getMetadataFeature(NodeList metaFeatureList, String geometryType, List<MetadataXMLUpload> metadataXmlList, 
			String layerId) throws ParserConfigurationException, SAXException, IOException {
		String changeType = null;
		Node metaPath = null;
		Element metaPathEle = null;
		HashMap<String,String> idMeta = null;
		String idNode = null;
		String metadataText = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		ByteArrayInputStream bais = null;
		Document metaDataDoc = null;
		NodeList metaTabList = null;
		Node metaTab = null;
		Element metaTabEle = null;
		String classId = null;
		NodeList metaItems = null;
		Node metaItem = null; 
		Element metaItemEle = null;
		MetadataXMLUpload metadataXmlUp = null;
		ItemXMLUpload itemXmlUp = null;
		String rmAtt = null;
		String updatableAtt = null;
		String nodeName = null;
		String nodeValue = null;
		List<String> nodeListValues = null;
		NodeList subItemList = null;
		List<ItemXMLUpload> listItemXmlUp = null;
		TabXMLUpload tabXmlUp = null;
		List<TabXMLUpload> tabListXmlUp = null;
		String geometry = null;
    	for (int j = 0; j < metaFeatureList.getLength(); j++) {
    		metaPath = metaFeatureList.item(j);
    		if(metaPath==null){ continue;}
    		if(!(metaPath instanceof Element)){ continue;}
    		metaPathEle = (Element)metaPath;
    		
    		//NUEVO
			idMeta = new HashMap<String,String>();
//			if(Utils.isInArray(Constants.TIPOS_INVENTARIO, layerId)){
//				if(!metaPathEle.getAttribute("v1").equals("-1"))
//					idMeta.put("id", metaPathEle.getAttribute("v1"));
//			}
//			else if(Utils.isInArray(Constants.TIPOS_EIEL, layerId)){
				int i=1;
				while(true){
					if(metaPathEle.getAttribute("v"+i)!=null && metaPathEle.getParentNode().getAttributes().getNamedItem("l"+i)!=null){
						if(!metaPathEle.getAttribute("v"+i).equals("-1"))
							idMeta.put(metaPathEle.getParentNode().getAttributes().getNamedItem("l"+i).getNodeValue(), metaPathEle.getAttribute("v"+i));
					}
					else break;
					i++;				
				}
//			}			
			//FIN NUEVO
			
			idNode= metaPathEle.getAttribute(ConstantsXMLUpload.ATT_ID);
			changeType = metaPathEle.getAttribute(ConstantsXMLUpload.ATT_CHANGE_TYPE);
			
			//depende del tipo de geometría
			if(geometryType.equals(ConstantsXMLUpload.ATT_GEOTYPE_PATH)){
				geometry = "d=\""+ metaPathEle.getAttribute("d") + "\"";
			}else if(geometryType.equals(ConstantsXMLUpload.ATT_GEOTYPE_PATH)){
				geometry = "points=\""+ metaPathEle.getAttribute("points") + "\"";
			}
			
			//Buscamos la información de Metadata.
			//<metadata><![CDATA[<skeleton><tab classId="CExped...
			metadataText = metaPathEle.getElementsByTagName("metadata").item(0).getFirstChild().getNodeValue();
			bais = new ByteArrayInputStream(metadataText.getBytes("UTF-8"));
			try{
				metaDataDoc = builder.parse(bais);
			}
			finally{
			    bais.close();
			}			
			
			metaTabList = ((Element) metaDataDoc.getFirstChild()).getElementsByTagName("tab");
			if(metaTabList==null){ continue;}
			
			tabListXmlUp = new ArrayList<TabXMLUpload>();
			for (int k = 0; k < metaTabList.getLength(); k++) {
				metaTab = metaTabList.item(k);
				if(metaTab==null){ continue;}
        		if(!(metaTab instanceof Element)){ continue;}
        		metaTabEle = (Element)metaTab;
        		classId = metaTabEle.getAttribute(ConstantsXMLUpload.ATT_CLASSID);
        		metaItems = metaTabEle.getChildNodes();
        		if(metaItems==null){continue;}
        		listItemXmlUp = new ArrayList<ItemXMLUpload>();
				for (int z = 0; z < metaItems.getLength(); z++) {
					metaItem = metaItems.item(z);
					if(metaItem==null){ continue;}
	        		if(!(metaItem instanceof Element)){ continue;}
	        		metaItemEle = (Element)metaItem;
	        		rmAtt = metaItemEle.getAttribute(ConstantsXMLUpload.ATT_REFLECTMETHOD);
	        		
	        		//Obtenemos si el campo es actualizable. Al insertar el elemento hay campos
	        		//en los cuales se debe de obtener un nuevo valor por ejemplo un identificador
	        		//autogenerado.
	        		updatableAtt= metaItemEle.getAttribute(ConstantsXMLUpload.ATT_UPDATABLEMETHOD);
	        		
	        		nodeName = metaItemEle.getNodeName();
	        		//<item rm="IdTipoObra" name="" edit="false" label="Tipo Obra:">5</item>  
	        		if(nodeName.equals(ConstantsXMLUpload.TAG_ITEM)){
	        			nodeValue = metaItemEle.getFirstChild().getNodeValue().trim().replaceAll("\n", "");
	        			itemXmlUp = new ItemXMLUpload(rmAtt, updatableAtt,nodeValue, nodeName, null);
	        			listItemXmlUp.add(itemXmlUp);
	        		}

	        		
	        		//<itemlist rm="ReferenciasCatastralesString" name="" edit="false" label="Referencia Catastral:"><subitem>0666801UN9106N</subitem>...
	        		else if(nodeName.equals(ConstantsXMLUpload.TAG_ITEMLIST)){
	        			nodeListValues = new ArrayList<String>();
	        			subItemList = metaItemEle.getElementsByTagName(ConstantsXMLUpload.TAG_SUBITEM);
	        			if(subItemList!=null){
	        				for (int l = 0; l < subItemList.getLength(); l++) {
		        				String subItemListValue = subItemList.item(l).getNodeValue();
		        				if(subItemListValue!=null){
		        					nodeListValues.add(subItemListValue.trim().replaceAll("\n", ""));
		        				}
							}
	        			}
	        			itemXmlUp = new ItemXMLUpload(rmAtt, updatableAtt,null, nodeName, nodeListValues);
	        			listItemXmlUp.add(itemXmlUp);
	        		}
				}
				tabXmlUp = new TabXMLUpload(classId, listItemXmlUp);
				tabListXmlUp.add(tabXmlUp);
			}
			
			//creado el metadata
			metadataXmlUp = new MetadataXMLUpload(idMeta, idNode,tabListXmlUp, layerId, changeType, geometry);
			metadataXmlList.add(metadataXmlUp);
		}
	}

	/**
	 * Parsea la información referente a los resources metiendola en los beans correspondientes
	 * @param resources2
	 * @return
	 */
	private Map<String, ResourceXMLUpload> parseResources(NodeList resourcesSrc) {
		Map<String, ResourceXMLUpload> resourceXmlMap = new HashMap<String, ResourceXMLUpload>();
        ResourceXMLUpload resourceXmlUp = null;
        //NodeList resourceList = resourcesNode.getChildNodes();
        Element resource = null;
        String typeAtr = null;
        NodeList resourceFields = null;
        Node resourceField = null;
        String resourceFieldValue = null;
        for (int i = 0; i < resourcesSrc.getLength(); i++) {
        	if(!(resourcesSrc.item(i) instanceof Element)){ continue;}
        	resource = (Element) resourcesSrc.item(i);
        	typeAtr = resource.getAttribute("type");
        	resourceFields = resource.getChildNodes();
        	resourceXmlUp = new ResourceXMLUpload();
        	resourceXmlUp.setType(typeAtr);
        	for (int j = 0; j < resourceFields.getLength(); j++) {
        		if(!(resourceFields.item(j) instanceof Element)){ continue;}
        		resourceField = resourceFields.item(j);
        		resourceFieldValue = resourceField.getFirstChild().getNodeValue();
        		if(resourceField.getNodeName().equals("url")){
        			resourceXmlUp.setUrl(resourceFieldValue);
        		}
        		else if(resourceField.getNodeName().equals("body")){
        			resourceXmlUp.setBody(resourceFieldValue);
        		}
			}
        	resourceXmlMap.put(resourceXmlUp.getUrl(), resourceXmlUp);
		}
        return resourceXmlMap;
	}

	/**
	 * Parsea la información de un grupo de layers y creando los correspondientes beans
	 * @param gLayersList
	 * @return
	 */
	private List<GroupXMLUpload> parseGLayers(NodeList gLayersList) {
		List<GroupXMLUpload> groupXmlUp = new ArrayList<GroupXMLUpload>();
        Element nodeLayer = null;
        NamedNodeMap layerAtrList = null;
        NodeList nodeFeatList = null;
        Node nodeFeature = null;
        Node attrItem = null;
        NamedNodeMap featureAtrValues = null;
        List<AttributeXMLUpload> groupAttributeXmlUp = null;
        List<FeatureXMLUpload> featureListXmlUp = null;
        List<AttributeXMLUpload> featureAttributeXmlUp = null;
		for (int i = 0; i < gLayersList.getLength(); i++) { //<g
        	if(!(gLayersList.item(i) instanceof Element)){ continue;}
        	nodeLayer = (Element) gLayersList.item(i);
        	layerAtrList = nodeLayer.getAttributes();
        	if(layerAtrList==null){ continue;}
        	
        	/** ATRIBUTOS DE GRUPO **/
        	groupAttributeXmlUp = new ArrayList<AttributeXMLUpload>();
        	for (int k = 0; k < layerAtrList.getLength(); k++) { //l1="id" l2="nombre" l3="Área"
        		if(!(layerAtrList.item(k) instanceof Node)){ continue;}
        		attrItem = layerAtrList.item(k);
				groupAttributeXmlUp.add(new AttributeXMLUpload(attrItem.getNodeName(), attrItem.getNodeValue()));
        		//System.out.println(layerAtrList.item(k).getNodeName() + " : " + layerAtrList.item(k).getNodeValue());
			}
        	
        	nodeFeatList = nodeLayer.getChildNodes();
        	if(nodeFeatList==null){ continue;}
        	
        	/** FEATURES **/
        	featureListXmlUp = new ArrayList<FeatureXMLUpload>();
    		for (int j = 0; j < nodeFeatList.getLength(); j++) { //<polygon ó <ellipse ó <polyline ...
    			if(!(nodeFeatList.item(j) instanceof Node)){ continue;}
				nodeFeature = nodeFeatList.item(j);
            	featureAtrValues = nodeFeature.getAttributes(); 
    			if(featureAtrValues==null){ continue;}
            	
    			/** ATRIBUTOS DE FEATURE **/
    			featureAttributeXmlUp =  new ArrayList<AttributeXMLUpload>();
    			for (int k = 0; k < featureAtrValues.getLength(); k++) { //d="M233.9375... v1="1745"
    				featureAttributeXmlUp.add(new AttributeXMLUpload(featureAtrValues.item(k).getNodeName(), featureAtrValues.item(k).getNodeValue()));
                	//System.out.println(featureAtrValues.item(k).getNodeName() + " = " + featureAtrValues.item(k).getNodeValue());
				}
    			
    			featureListXmlUp.add(new FeatureXMLUpload(nodeFeature.getNodeName(), groupAttributeXmlUp, featureAttributeXmlUp));
    			//System.out.println(featureListXmlUp.get(j).toXml());
			}
    		
    		//finalmente añadimos una nueva layer G al modelo
    		groupXmlUp.add(new GroupXMLUpload(groupAttributeXmlUp, featureListXmlUp));
		}
		
		return groupXmlUp;
	}

	/**
	 * Actualiza los cambios en BBDD y devuelve una cadena a interpretar por la PDA
	 * @return 
	 * @return
	 */
	public String updateDataBase() throws Exception{
		
		/*try {
			//logueamos en la aplicación
			dbXMLManager.login(user, password);
		}catch (Exception e) {
			logger.error("Error en el login del admcar: " + e,e);
			throw new ServerException("No se ha podido loguear en el administrador de cartografia.");
		}*/

		//iteramos sobre las capas para ir actualizando las features
		List<GroupXMLUpload> groupList = docXmlUp.getSvg().getGroupList();
		String featureErrors = "";
		boolean todoOk = true;
		boolean permissionException=false;
		
		ResultString results = new ResultString("<results>");
		
		
		logger.info("Actualizando Features");
		for (int i = 0; i < groupList.size(); i++) {
			try {
				updateLayerDataBase(groupList.get(i), results);
			}catch (ServerException e) {
				throw e;
			}catch (Exception e) {
				logger.error("Error Actualizando Feature: " + e.getMessage() + "\n");
				//capturamos las excepciones producidas para devolver una final
				todoOk = false;
				featureErrors += e.getMessage();
			}
		}
		logger.info("Actualizando Metadata");
		//actualización de metadata
		List<MetadataXMLUpload> metadataList = docXmlUp.getMetainfo().getMetadataMap();
		MetadataXMLUpload metadataXMLUpload = null;
		for (Iterator iterator = metadataList.iterator(); iterator.hasNext();) {
			metadataXMLUpload = (MetadataXMLUpload) iterator.next();
			try {
				dbXMLManager.updateMetadata(metadataXMLUpload, results);
			}
			catch(MobilePermissionException me){
				//capturamos las excepciones producidas para devolver una final
				todoOk = false;
				permissionException=true;
				featureErrors += me.getMessage();
				logger.error("Error Actualizando Metadata: Se ha producido un MobilePermissionException");
			}
			catch (Exception e) {
				//capturamos las excepciones producidas para devolver una final
				todoOk = false;
				featureErrors += e.getMessage();
				logger.error("Error Actualizando Metadata: Se ha producido un error al actualizar el metadata",e);
			}
		}
		
		results.setText(results.getText()+"\n</results>");
		
		if(!todoOk){
			logger.error("Actualizando Feature y Metadata: todoOk=false\n");
			throw new FeatureException(featureErrors, results.getText(),permissionException);
		}
		
		return results.getText();
	}
	
	/**
	 * Actualiza una capa de la base de datos
	 * @param groupXMLUpload
	 * @param results 
	 */
	private void updateLayerDataBase(GroupXMLUpload groupXMLUpload, ResultString results) throws Exception{
		
		
		//vamos actualizando cada feature individualmente
		List<FeatureXMLUpload> featureList = groupXMLUpload.getFeatureList();
		
		if (featureList.size()==0){	
			logger.debug("La capa no se carga ya que no hay features a tratar. Capa:"+groupXMLUpload.getSystemId());
			return;
		}
		//NUEVO
		dbXMLManager.setSmIdMunicipio(featureList.get(0).getIdMunicipioFeature());
		//FIN NUEVO
		
		String systemId = groupXMLUpload.getSystemId();
		String primaryKeyId = groupXMLUpload.getPkIdDB();
	
		//desplazamiento del mapa
		double despX = docXmlUp.getSvg().getDespX();
		double despY = docXmlUp.getSvg().getDespY();
		//cuadrado donde se ubica el mapa
		Coordinate leftDownViewBox = docXmlUp.getSvg().getLeftDownViewBox();
		Coordinate rightUpViewBox = docXmlUp.getSvg().getRightUpViewBox();
		
		try {
			//obtenemos la geometría que recubre todas las features del mapa desconectado
			GeometryFactory geometryFactory = new GeometryFactory();
			leftDownViewBox = SvgConversorXMLUpload.transformCoordinates(leftDownViewBox, despX, despY);
			rightUpViewBox = SvgConversorXMLUpload.transformCoordinates(rightUpViewBox, despX, despY);
			Envelope mapEnvelope = new Envelope(leftDownViewBox, rightUpViewBox);
			
			CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:"+docXmlUp.getSrid());
			//CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
						
	        CRSAuthorityFactory factory;
	        CoordinateReferenceSystem targetCRS;	            
	        factory = CRS.getAuthorityFactory(true);
	        
	        //targetCRS = factory.createCoordinateReferenceSystem("EPSG:4326");
	        //Lo que esta almacenado en la BD es 4230 y no 4326. Esto cambio en algun
	        //momento porque antes funcionaba con 4326
	        targetCRS = factory.createCoordinateReferenceSystem("EPSG:4258");

			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			Envelope result = JTS.transform(mapEnvelope, transform);

			logger.debug("Envelope que recubre las features: leftDownViewBox -> " + leftDownViewBox + " rightUpViewBox -> " + rightUpViewBox);
			Geometry mapGeometry = geometryFactory.toGeometry(result);
			logger.debug("Geometria EPSG:4258 --> " + mapGeometry.toText());
			
			//cargamos la capa en el manager
			dbXMLManager.loadLayer(systemId, mapGeometry, docXmlUp.getSrid());
			//dbXMLManager.loadLayer(systemId, null, docXmlUp.getSrid());
		}catch (Exception e) {
			logger.error("Error al cargar la capa " + systemId + ": " + e,e);
			throw new ServerException("No se ha podido cargar la capa systemId: " + systemId);
		}
		
		//System.out.println("Features a tratar: " + featureList.size());
		logger.debug("Features a tratar: " + featureList.size());
		
		String featureErrors = "";
		boolean todoOk = true;
		FeatureXMLUpload feature = null;
		//actualización de las features
		for (int i = 0; i < featureList.size(); i++) {
			feature = featureList.get(i);
			try {
				//insercción, borrado o actualización de la feature en BBDD
				dbXMLManager.updateFeature(systemId, primaryKeyId, feature, despX, despY, docXmlUp.getResources(), results, docXmlUp.getSrid(), docXmlUp.getMetainfo().getMetadataMap());
			}catch (Exception e) {
				logger.error("Error Actualizando Feature: " + e.getMessage() + "\n");				
				//controlamos las excepciones por cada feature
				todoOk = false;
				featureErrors += feature.printFeatureError(e.getMessage()) + "\n";
			}
		}
		
		if(!todoOk){
			logger.error("Actualizando Feature: todoOk=false\n");
			throw new Exception(featureErrors);
		}
		
	}
	
	//***********************************************
	//***********************************************
	//***********************************************
	
	/*private static void verificaCeldasDesconexion(){
		
		
		double despX = 573572.3000000059;
		double despY = 4436492.309999997;
		//cuadrado donde se ubica el mapa
		Coordinate leftDownViewBox = new Coordinate(0.0,0.0);
		Coordinate rightUpViewBox = new Coordinate(377.12,286.46);
		
		double despX = 573575.4000000058;
		double despY = 4436627.249999997;
		//cuadrado donde se ubica el mapa
		Coordinate leftDownViewBox = new Coordinate(0.0,0.0);
		Coordinate rightUpViewBox = new Coordinate(366.81,206.8);
		
		double despX = 573876.3100000059;
		double despY = 4436627.159999996;
		//cuadrado donde se ubica el mapa
		Coordinate leftDownViewBox = new Coordinate(0.0,0.0);
		Coordinate rightUpViewBox = new Coordinate(356.89,211.51);
		
		try {
			//obtenemos la geometría que recubre todas las features del mapa desconectado
			GeometryFactory geometryFactory = new GeometryFactory();
			leftDownViewBox = SvgConversorXMLUpload.transformCoordinates(leftDownViewBox, despX, despY);
			rightUpViewBox = SvgConversorXMLUpload.transformCoordinates(rightUpViewBox, despX, despY);
			Envelope mapEnvelope = new Envelope(leftDownViewBox, rightUpViewBox);
			Geometry mapGeometryOriginal = geometryFactory.toGeometry(mapEnvelope);
			System.out.println("GEOMETRY:"+mapGeometryOriginal.toText());
			
			CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:23030");
			//CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
						

	        //CRSAuthorityFactory factory=ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null );
			CRSAuthorityFactory factory;
			
	        CoordinateReferenceSystem targetCRS;	            
	        factory = CRS.getAuthorityFactory(true);
	        targetCRS = factory.createCoordinateReferenceSystem("EPSG:4326");
	        
			// CoordinateReferenceSystem targetCRS = factory.createCoordinateReferenceSystem("EPSG:4326");

			
			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS,true);
			Envelope result = JTS.transform(mapEnvelope, transform);

			logger.debug("Envelope que recubre las features: leftDownViewBox -> " + leftDownViewBox + " rightUpViewBox -> " + rightUpViewBox);
			Geometry mapGeometry = geometryFactory.toGeometry(result);
			System.out.println("Geometria -> " + mapGeometry.toText());
			//cargamos la capa en el manager
			//dbXMLManager.loadLayer(systemId, mapGeometry, docXmlUp.getSrid());
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	*/
	/*public static Coordinate convertCoordinates( Coordinate coordSource, String refSource, String refTarget )
    {
		Coordinate coordDest = new Coordinate();	
		try {
			
			CoordinateReferenceSystem sourceCRS = CRS.decode(refSource, true);
			CoordinateReferenceSystem targetCRS = CRS.decode(refTarget, true);
			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			JTS.transform( coordSource,coordDest, transform);
		} catch (Exception e) {e.printStackTrace();}
        return coordDest;
    }*/

		
	/*public static void main(String args[]){
		
		
		System.out.println("Resultado:(-2.13722591899663, 40.0722211627317)");
		Coordinate coordSource=new Coordinate(573572.3000000059,4436205.849999997);
		Coordinate coordDest=ServletXMLUpload.convertCoordinates(coordSource, "EPSG:23030", "EPSG:4326");
		System.out.println("Resultado:"+coordDest.toString());
		
		//ServletXMLUpload.verificaCeldasDesconexion();

		
	}*/
	
	
	public static void main2(String[] args) {
		try {
			//ConfigurationManager cManager = new ConfigurationManager();
			String PROP_GEOPISTA_CON_SERVER = "localgis.server.admcar";
		    String SERVER = ConfigurationManager.getApplicationProperty(PROP_GEOPISTA_CON_SERVER);

			com.geopista.security.SecurityManager sm = new com.geopista.security.SecurityManager();
			sm.setUrlNS(SERVER + "/geopista");
			System.out.println("SecurityManager url: " + sm.getUrlNS());
			System.out.println("Ralizado login:"+ sm.loginNS("adm1", "adm1","Geopista"));
			System.out.println("Identificador de sesion: \n"+sm.getIdSesionNS());
			/*if (sm.isLoggedNS()){
				ServletXMLUpload uploadXmlParser = new ServletXMLUpload(sm);
				FileInputStream mapas=new FileInputStream("c:\\ejemploUp3.xml");
				String response = uploadXmlParser.execute(mapas);
				System.out.println("Respuesta devuelta por el servlet:\n" + response);
			}*/
			
			com.geopista.security.SecurityManager sm2 = new com.geopista.security.SecurityManager();
			com.geopista.security.SecurityManager.setSm(sm2);
			AppContext.releaseResources();
			sm2.setUrlNS(SERVER + "/geopista");
			System.out.println("SecurityManager url: " + sm2.getUrlNS());
			System.out.println("Ralizado login:"+ sm2.loginNS("adm2", "adm2","Geopista"));
			
			System.out.println("Los identificadores de sesion son: \n"
					            +sm.getIdSesionNS()+"\n"
					            +sm2.getIdSesionNS() );
		
			//Acedemos con el 2 security manager
			
		}catch (Exception e) {
			System.err.println("Error devuelto por el servlet:\n" + e.getMessage());
			e.printStackTrace();
		} catch (Error ex) {
			System.err.print("Error no controlado: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			System.exit(0);
		}
	}
	
	
     
  
	
}
