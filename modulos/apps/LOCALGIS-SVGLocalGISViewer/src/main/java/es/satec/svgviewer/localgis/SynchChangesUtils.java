/**
 * SynchChangesUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.japisoft.fastparser.ParseException;
import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;
import com.japisoft.fastparser.dom.TextImpl;
import com.tinyline.svg.SVGAttr;
import com.tinyline.svg.SVGChangeEvent;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGMetadataElem;
import com.tinyline.svg.SVGNode;
import com.tinyline.svg.SVGParser;
import com.tinyline.tiny2d.TinyString;

import es.satec.svgviewer.localgis.util.BASE64EncoderStream;

/**
 * Clase de utilidades para hacer el guardado en remoto y sincronizacion con el servidor.
 * @author jpresa
 *
 */
public class SynchChangesUtils {
	
	public static final String ERRORS_LAYER_NAME = "errors";
	
	private static Logger logger = (Logger) Logger.getInstance(SynchChangesUtils.class);

	/**
	 * Genera el xml con los cambios realizados en un documento svg para enviar al servidor.
	 */
	public static void serializeToUpload(OutputStream outputStream, SVGDocument doc, Vector metaInfos, int srid) throws IOException {
		logger.debug("Creando XML para notificacion de cambios");
		
		OutputStreamWriter out = new OutputStreamWriter(outputStream, "UTF-8");
		// Cabeceras
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><upload2localgis><svg srid=\""+srid+"\"><![CDATA[");
		out.flush();

		// SVG de nodos modificados
		Vector nodesWithImage = doc.serializeModifiedNodes2XML(outputStream, true, true, ERRORS_LAYER_NAME);
		out.write("]]></svg><resources>");
		
		// Imagenes
		logger.debug("Hay " + nodesWithImage.size() + " nodos con imagenes asociadas");
		Hashtable images = new Hashtable(); // Para no repetir imagenes que referencian la misma url
		Enumeration en = nodesWithImage.elements();
		while (en.hasMoreElements()) {
			SVGNode node = (SVGNode) en.nextElement();
			Enumeration en2 = node.getImageURLs().elements();
			while (en2.hasMoreElements()) {
				String imUrl = (String) en2.nextElement();
				logger.debug("Imagen con URL: " + imUrl);
				URL url = new URL(imUrl);
				String protocol = url.getProtocol();
				// Solo enviar las imagenes con protocolo "file", pues son las que se han añadido
				if (protocol != null && protocol.equalsIgnoreCase("file") && images.get(imUrl) == null) {
					logger.debug("Se envia la imagen");
					images.put(imUrl, imUrl);
					
					InputStream urlStream = null;
					BASE64EncoderStream base64Stream = null;
					try {
						// Leer la imagen y escribirla codificada en Base64
						urlStream = url.openStream();
						base64Stream = new BASE64EncoderStream(outputStream);
						out.write(("<resource type=\"image\"><url>" + imUrl + "</url><body>"));
						out.flush();
						int chunk = 1024*256; // 256kb
						byte[] buf = new byte[chunk];
						int read = urlStream.read(buf, 0, chunk);
						while (read >= 0) {
							base64Stream.write(buf, 0, read);
							read = urlStream.read(buf, 0, chunk);
						}
						base64Stream.flush();
						out.write("</body></resource>");
						out.flush();
					} catch (IOException e) {
						throw e;
					} finally {
						if (urlStream != null) urlStream.close();
					}
				}
				else {
					logger.debug("NO se envia la imagen");
				}
			}
		}
		out.write("</resources>");
		
		// MetaInformacion (licencias de obra, bienes inmuebles...)
		out.write("<metainfo>");
		out.flush();
		en = metaInfos.elements();
		while (en.hasMoreElements()) {
			MetaInfo metaInfo = (MetaInfo) en.nextElement();
			metaInfo.serializeToSend(outputStream);
		}
		out.write("</metainfo>");
		
		out.write("</upload2localgis>");
		out.flush();
	}

	/**
	 * Analiza la respuesta del servidor tras enviar los cambios.
	 * La respuesta es un xml de la forma:
	 * <upload2localgisresponse code="0" />
	 * o bien:
	 * <upload2localgisresponse code="1">
 * 			<path id="3434" ... errorcode="x" errordescription="..." />
	 * </upload2localgisresponse>
	 * @return el codigo de respuesta.
	 */
	public static int readResponseToResetSVGChanges(Document response, SVGDocument doc, /*OutputStream outputStreamLocal, */
		Vector metaInfos) throws XMLParsingException, IOException {
		
		Element root = (Element) response.getRoot();
		if (doc == null || doc.root == null) return 0;

		int code = Integer.parseInt(XMLTools.getRequiredAttrValue("code", root));
		logger.debug("Codigo de respuesta: " + code);
		if (code == 0) {
			// Elimina la capa de errores
			removeErrorsLayer(doc.root);
			// Resetear todos los cambios en el svg
			resetSVGChanges(doc.root, null);
			doc.setModified(false);
			// Resetear los cambios en la metainformacion
			resetMetaInfoChanges(metaInfos, null);
			// Actualizar el SVG con los resultados devueltos
			Element results = XMLTools.getChildElement(root, "results");
			if (results != null) {
				updateResults(doc.root, results);
				Enumeration e = metaInfos.elements();
				while (e.hasMoreElements()) {
					MetaInfo metaInfo = (MetaInfo) e.nextElement();
					updateMetadataResults(metaInfo.getMetaInfoLayerElement(), results);
				}
				
			}
		}
		else if ((code == 1) || (code ==3)){
			//String svgErrores = XMLTools.getStringValue(root);
			String svgErrores = null;
			Element results = null;
			NodeList childNodes = root.getChildNodes();
			for (int i=0; i<childNodes.getLength(); i++) {
				Node node = childNodes.item(i);
				if (node.getNodeType() == Node.TEXT_NODE) {
					svgErrores = node.getNodeValue();
				}
				else if (node instanceof Element) {
					if (node.getLocalName().equals("results")) {
						results = (Element) node;
					}
				}
			}
			
			if (svgErrores != null) {
				// Parsear los errores en un documento svg nuevo
				ByteArrayInputStream bais = new ByteArrayInputStream(svgErrores.getBytes());
				SVGDocument svgDocErrores = new SVGDocument();
				SVGParser svgParser = new SVGParser(new SVGAttr());
				svgParser.load(svgDocErrores, bais);
				bais.close();

				// Elimina la capa de errores
				removeErrorsLayer(doc.root);

				// Resetear los cambios en los nodos que no tienen errores
				if (!resetSVGChanges(doc.root, svgDocErrores.root))
					doc.setModified(false);
				
				// Resetear los cambios en la metainformacion
				resetMetaInfoChanges(metaInfos, svgDocErrores.root);
				
				// Anadir el grupo de errores al final del SVG cargado
				if (svgDocErrores != null && svgDocErrores.root != null && svgDocErrores.root.children.count > 0) {
					SVGNode errorsLayer = (SVGNode) svgDocErrores.root.children.data[0];
					logger.debug("Añadiendo capa de errores. Numero de elementos: " + errorsLayer.children.count);
					for (int i=0; i<errorsLayer.children.count; i++) {
						SVGNode errorNode = (SVGNode) errorsLayer.children.data[i];
						errorNode.id = null;
						errorNode.changeEvent = null;
					}
					doc.root.addChild(errorsLayer, -1);//doc.root.children.count);
				}
			}
			// Actualizar el SVG con los resultados devueltos
			if (results != null) {
				updateResults(doc.root, results);
			}
		}
		else {
			return code;
		}

		// Guardar el documento svg en local para que no haya inconsistencias
		// se hace fuera doc.serializeSVG2XML(outputStreamLocal);
		
		return code;
	}
	
	/**
	 * Resetea los cambios en la metainformación
	 * @param metaInfos
	 * @param errorsGroup
	 * @throws IOException
	 */
	private static void resetMetaInfoChanges(Vector metaInfos, SVGNode errorsGroup) throws IOException {
		Enumeration e = metaInfos.elements();
		while (e.hasMoreElements()) {
			MetaInfo metaInfo = (MetaInfo) e.nextElement();
			metaInfo.resetChanges(errorsGroup);
		}
		
	}

	/**
	 * Elimina la capa de errores del svg, si existe
	 * @param root Nodo raiz del svg
	 * @return true si existia la capa; false en caso contrario
	 */
	private static boolean removeErrorsLayer(SVGNode root) {
		SVGNode errorsLayer = SVGNode.getNodeById(root, new TinyString(ERRORS_LAYER_NAME.toCharArray()));
		if (errorsLayer != null) {
			int pos = root.children.indexOf(errorsLayer, 0);
			if (pos != -1) {
				logger.debug("Existe capa de errores. Se elimina");
				int ret = root.removeChild(pos);
				if (ret != -1) return true;
			}
		}
		return false;
	}
	
	/**
	 * Resetea los cambios en un documento SVG
	 * @param root Nodo raiz del documento
	 * @param groupErrores Nodo con el conjunto de nodos erroneos
	 * @return true si quedan errores pendientes; false si no quedan errores
	 */
	protected static boolean resetSVGChanges(SVGNode root, SVGNode groupErrores) {
		boolean errorsLeft = false;
		logger.debug("Reseteando cambios");
		if (root.children != null && root.children.count > 0) {
			// Recorrer las capas
			for (int i=0; i<root.children.count; i++) {
				SVGNode layer = (SVGNode) root.children.data[i];
				if (layer.children != null && layer.children.count > 0) {
					// Recorrer los nodos de cada capa
					for (int j=0; j<layer.children.count; j++) {
						SVGNode node = (SVGNode) layer.children.data[j];
						if (node.getChangeEvent() != null ) {
							boolean isErrorNode = false;
							// Comprobar si existe en la lista de nodos erroneos
							if (groupErrores != null && node.id != null) {
								if (SVGNode.getNodeById(groupErrores, node.id) != null) {
									isErrorNode = true;
									errorsLeft = true;
								}
							}

							if (!isErrorNode) {
								logger.debug("Reseteando nodo " + j  + " de la capa " + i);
								//node.id = null;
								int operationType = node.getChangeEvent().getChangeType();
								boolean opInserccion = (operationType & SVGChangeEvent.CHANGE_TYPE_NEW) != 0;
								boolean opModificacion = (operationType & SVGChangeEvent.CHANGE_TYPE_MODIFIED) != 0;
								boolean opEliminacion = (operationType & SVGChangeEvent.CHANGE_TYPE_DELETED) != 0;
								boolean opMetadata = (operationType & SVGChangeEvent.CHANGE_TYPE_METADATA_MODIFIED) != 0;
								// Resetear los cambios
								if(opEliminacion){
									layer.removeChild(j);
									j--;
								}
								else if(opInserccion || opModificacion || opMetadata){
									node.changeEvent = null;
									// Eliminar las imagenes que hemos enviado recientemente y mantener las que ya venian del servidor
									if (node.getImageURLs() != null) {
										Vector newImageURLs = new Vector();
										Enumeration en = node.getImageURLs().elements();
										while (en.hasMoreElements()) {
											String urlString = (String) en.nextElement();
											try {
												URL url = new URL(urlString);
												String protocol = url.getProtocol();
												if (protocol == null || !protocol.equalsIgnoreCase("file")) {
													newImageURLs.addElement(urlString);
												}
												//else
												//	newImageURLs.addElement(urlString);
											} catch (Exception e) {
												logger.error(e, e);
											}
										}
										if (newImageURLs.isEmpty()) {
											node.imageURLs = null;
										}
										else {
											node.imageURLs = newImageURLs;
										}
									}
									//break;
								}
							}
						}
					}
				}
			}
		}
		return errorsLeft;
	}
	
	/**
	 * Actualiza el documento SVG con la información devuelta por el servidor tras ejecutar
	 * las operaciones sobre los elementos modificados.
	 * @param root Nodo raiz del documento
	 * @param results Nodo XML con el conjunto de resultados devueltos
	 */
	private static void updateResults(SVGNode root, Element results) {
		NodeList childNodes = results.getChildNodes();
		for (int i=0; i<childNodes.getLength(); i++) {
			try {
				Node node = childNodes.item(i);
				if (node instanceof Element) {
					if (node.getLocalName().equals("insert")) {
						String id = XMLTools.getRequiredAttrValue("id", node);
						//CAMBIAR : v1 por feature con name id
						//String v1 = XMLTools.getRequiredAttrValue("v1", node);
						String v1 = XMLTools.getRequiredAttrValue("v2", node);
						SVGNode svgNode = SVGNode.getNodeById(root, new TinyString(id.toCharArray()));
						if (svgNode != null && svgNode.nameAtts != null) {
							logger.debug("Actualizando nodo id=" + id + " con v1=" + v1);
							svgNode.nameAtts.setElementAt(v1, 0);
						}
						else {
							logger.error("El nodo con id=" + id + " no existe en el documento svg");
						}
					}					
				}
			} catch (Exception e) {
				logger.error("El resultado de la operacion no tiene formato correcto", e);
			}
		}
	}
	/**
	 * Actualiza el documento SVG con la información devuelta por el servidor tras ejecutar
	 * las operaciones sobre los elementos modificados.
	 * @param root Nodo raiz del documento
	 * @param results Nodo XML con el conjunto de resultados devueltos
	 */
	private static void updateMetadataResults(SVGNode root, Element results) {
		NodeList childNodes = results.getChildNodes();
		for (int i=0; i<childNodes.getLength(); i++) {
			try {
				Node node = childNodes.item(i);
				if (node instanceof Element) {
					if (node.getLocalName().equals("insertMetadata")) {
						String id = XMLTools.getRequiredAttrValue("id", node);
						

					    NamedNodeMap localNamedNodeMap = node.getAttributes();
					    if (localNamedNodeMap != null)
					    {
					    	Attr localAttr = null;
					    	String strName = null;
					    	String strValue = null;
					    	for (int j=0;j<localNamedNodeMap.getLength();j++){
					    		 localAttr = (Attr)localNamedNodeMap.item(j);
					    		 strName = localAttr.getName();
					    		 strValue = localAttr.getValue();
					    		 
					    		 //Actualizamos todo menos el ID
					    		 if (!strName.equals("id")){
						    		 SVGNode svgNode = SVGNode.getNodeById(root, new TinyString(id.toCharArray()));
									 if (svgNode != null && svgNode.nameAtts != null) {
											logger.debug("Metadata.Actualizando nodo id=" + id + " con key=" + strName+" Valor="+strValue);
											updateMetaData(svgNode,strName,strValue);											
										}
										else {
											logger.error("Metadata.El nodo con id=" + id + " no existe en el documento svg");
										}
					    		 }
					    	}
					      
					    }
												
					}
				}
			} catch (Exception e) {
				logger.error("El resultado de la operacion no tiene formato correcto", e);
			}
		}
	}

	/**
	 * Actualizamos la informacion de metadatos en el XML
	 * @param svgNode
	 * @param v1
	 */
	private static void updateMetaData(SVGNode svgNode, String key,String value) {

		if (svgNode.children.count>0){
			
			//El campo content.data tiene un XML que es necesario parsear para cambiar.
			SVGMetadataElem svgMetadata = (SVGMetadataElem)svgNode.children.data[0];	
			String contenidoMetadata=new String (svgMetadata.content.data);
			

			try {
				InputStream isMetadata = new ByteArrayInputStream(contenidoMetadata.getBytes());

				Parser p = new Parser();
				p.setNodeFactory(new DomNodeFactory());
				p.setInputStream(isMetadata);
				p.parse();
				

				Document documentMetadata = p.getDocument();
				Element elementDocumentMetadata = (Element) documentMetadata.getRoot();
				
				NodeList listaNodosTab=elementDocumentMetadata.getElementsByTagName("tab");
				for(int i=0;i<listaNodosTab.getLength();i++){
					/*Se obtiene la informacion correspondiente a cada tab*/
					Node tab=listaNodosTab.item(i);
					
					NodeList listaNodosItem=tab.getChildNodes();

					for(int j=0;j<listaNodosItem.getLength();j++){
						
						Node item=listaNodosItem.item(j);					
						NamedNodeMap nameItems=item.getAttributes();
						Node idItem=nameItems.getNamedItem("rm");
						//logger.info("Id Item:"+idItem.getNodeValue()+" KeyTab:"+keyTab);
						String nodeValue=idItem.getNodeValue();
						
						if (nodeValue.equals(key)){
							if (item.getChildNodes().getLength()>0)
								item.getChildNodes().item(0).setNodeValue(value);
							else{
								TextImpl valorNodo = new TextImpl(value);
								item.appendChild(valorNodo);
							}
						}
										
					}
					
				}
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				documentMetadata.write(baos);
				String metadata = baos.toString();
				//logger.debug("Metadatos modificados: " + metadata);					
				svgMetadata.content.data=metadata.toCharArray();
					
			} catch (ParseException e) {

				logger.error(e);
			}
			catch (Exception e1)
			{
				logger.error(e1);
			}
		}
	
	}

}
