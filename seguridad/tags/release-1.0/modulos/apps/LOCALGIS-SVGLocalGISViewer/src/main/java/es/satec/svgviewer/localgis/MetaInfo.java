package es.satec.svgviewer.localgis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.japisoft.fastparser.sax.Sax2Parser;
import com.tinyline.svg.SVGAttr;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGNode;
import com.tinyline.svg.SVGParser;

/**
 * Clase para gestionar la información sobre elementos no gráficos asociados a features
 * del mapa.
 * @author jpresa
 *
 */
public class MetaInfo {

	private String projectPath;
	private String baseFileName;
	private String infoType;
	private ArrayList keyAttribute;
	private int numberOfFiles;
	
	private String currentFileName;
	private SVGDocument currentSVGDocument;
	private Vector modifiedFiles;
	
	private static Logger logger = (Logger) Logger.getInstance(MetaInfo.class);
	
	/**
	 * 
	 * @param projectPath Ruta con los ficheros del proyecto
	 * @param baseFileName Nombre del fichero svg base 
	 * @param infoType Tipo de información a tratar (bienes inmuebles, actividades contaminantes,
	 * licencias de obra...)
	 */
	public MetaInfo(String projectPath, String baseFileName, String infoType, ArrayList keyAttribute, int numberOfFiles) {
		if (projectPath != null && projectPath.length() > 0 && projectPath.charAt(projectPath.length()-1) != File.separatorChar) {
			this.projectPath = projectPath + File.separator;
		}
		else {
			this.projectPath = projectPath;
		}
		this.baseFileName = baseFileName;
		this.infoType = infoType;
		this.keyAttribute = keyAttribute;
		this.numberOfFiles = numberOfFiles;
	}
	
	public String getInfoType() {
		return infoType;
	}
	
	public ArrayList getKeyAttribute() {
		return keyAttribute;
	}
	
	//NUEVO
	/**
	 * Carga el fichero que corresponde a una feature
	 */
	private SVGDocument loadByIdFeature(HashMap idFeatures) throws IOException {
//		return load((int)(idFeature % numberOfFiles));
		long idLong = 0;
		Iterator it = idFeatures.keySet().iterator();
		while(it.hasNext()){
			Integer key = (Integer) it.next();
			CharSequence cSeq = (CharSequence) idFeatures.get(key);					
			for (int i = 0; i < cSeq.length(); i++) {
				idLong += cSeq.charAt(i);
			}
		}
		return load((int)(idLong % numberOfFiles));
	}
	//FIN NUEVO
	
	/**
	 * Carga el fichero que corresponde a una feature
	 */
	private SVGDocument loadByIdFeature(String idFeature) throws IOException {
//		return load((int)(idFeature % numberOfFiles));
		CharSequence cSeq = idFeature;
		long idLong = 0;
		for (int i = 0; i < cSeq.length(); i++) {
			idLong += cSeq.charAt(i);
		}
		return load((int)(idLong % numberOfFiles));
	}
	
	/**
	 * Carga el fichero de indice especificado
	 */
	private SVGDocument load(int n) throws IOException {
		String fileName = baseFileName + infoType + n + ".svg";
		logger.debug("Cargando fichero " + fileName);
		if (currentFileName == null || !fileName.equals(currentFileName)) {
			// Guarda el fichero abierto actualmente
			unload();
			FileInputStream fis = null;
			try {
				currentFileName = fileName;
				fis = new FileInputStream(projectPath + fileName);
				currentSVGDocument = new SVGDocument();
				SVGParser svgParser = new SVGParser(new SVGAttr());
				svgParser.load(currentSVGDocument, fis);
			} catch (FileNotFoundException e) {
				logger.warn("El fichero " + fileName + " no existe");
				currentSVGDocument = null;
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {}
				}
			}
			
		}
		return currentSVGDocument;
	}
	
//	/**
//	 * Obtiene los elementos con metainformación de la feature especificada.
//	 * @param idFeature Identificador de la feature
//	 * @return Vector de nodos SVG
//	 */
//	public Vector getElementsByIdFeature(String idFeature) throws IOException {
//		logger.debug("Obteniendo elementos para la feature " + idFeature);
//		Vector elements = new Vector();
//		loadByIdFeature(idFeature);
//		if (currentSVGDocument != null && currentSVGDocument.root != null &&
//			currentSVGDocument.root.children != null && currentSVGDocument.root.children.count == 1) {
//			// Tiene que venir exactamente un grupo con los nodos de información dentro
//			SVGNode groupNode = (SVGNode) currentSVGDocument.root.children.data[0];
//			
//			//int posIdFeature = groupNode.getPosByNameLayertAtt(keyAttribute); //IDFEATURE_ATTRIBUTE_NAME);
//			ArrayList<Integer> posIdFeatures = groupNode.getPosByNameLayertAtt(keyAttribute); //IDFEATURE_ATTRIBUTE_NAME);
//			Iterator it = posIdFeatures.iterator();
//			while(it.hasNext()){
//				int posIdFeature = (Integer) it.next();
//				if (posIdFeature != -1 && groupNode.children != null) {
//					for (int i=0; i<groupNode.children.count; i++) {
//						SVGNode node = (SVGNode) groupNode.children.data[i];
//						try {
//							String idFeatureNode = node.getValueLayertAtt(posIdFeature);
//							if (idFeatureNode.equals(idFeature)) {
//								elements.addElement(node);
//							}
//						} catch (Exception e) {
//							logger.error("Valor de idFeature incorrecto para el nodo " + i, e);
//						}
//					}
//				}
//				else {
//					logger.error("No se ha encontrado el atributo clave " + keyAttribute);
//				}
//			}			
//		}
//		return elements;
//	}
	
	//NUEVO
	/**
	 * Obtiene los elementos con metainformación de la feature especificada.
	 * @param idFeature Identificador de la feature
	 * @return Vector de nodos SVG
	 */
	public Vector getElementsByIdFeature(HashMap idFeature) throws IOException {
		logger.debug("Obteniendo elementos para la feature " + idFeature);
		Vector elements = new Vector();
		loadByIdFeature(idFeature);
		if (currentSVGDocument != null && currentSVGDocument.root != null &&
			currentSVGDocument.root.children != null && currentSVGDocument.root.children.count == 1) {
			// Tiene que venir exactamente un grupo con los nodos de información dentro
			SVGNode groupNode = (SVGNode) currentSVGDocument.root.children.data[0];
			
			//int posIdFeature = groupNode.getPosByNameLayertAtt(keyAttribute); //IDFEATURE_ATTRIBUTE_NAME);
			ArrayList posIdFeatures = groupNode.getPosByNameLayertAtt(keyAttribute); //IDFEATURE_ATTRIBUTE_NAME);
			
			for (int i=1; i<groupNode.children.count; i++) {
				SVGNode node = (SVGNode) groupNode.children.data[i];
				if(node.getChangeEvent()==null || node.getChangeEvent().getChangeType()!=4){
					Iterator it = posIdFeatures.iterator();
					int numEqualsIds = posIdFeatures.size();
					while(it.hasNext()){
						int posIdFeature = ((Integer) it.next()).intValue();					
						try {
							String idFeatureNode = node.getValueLayertAtt(posIdFeature);							
							if (idFeatureNode.equals(idFeature.get(Integer.valueOf(posIdFeature)))) 								
								numEqualsIds--;
							if(numEqualsIds==0)
								elements.addElement(node);					
						} catch (Exception e) {
							logger.error("Valor de idFeature incorrecto para el nodo " + i, e);
						}		
					}					
					numEqualsIds = posIdFeatures.size();
				}
			}	
		}
		return elements;
	}
	//FIN NUEVO
	
	public SVGNode getPrototypeElement() {
		logger.debug("Obteniendo elemento prototipo");
		if (currentSVGDocument != null && currentSVGDocument.root != null &&
			currentSVGDocument.root.children != null && currentSVGDocument.root.children.count == 1) {
			// Tiene que venir exactamente un grupo con los nodos de información dentro
			SVGNode groupNode = (SVGNode) currentSVGDocument.root.children.data[0];
				
			//int posIdFeature = groupNode.getPosByNameLayertAtt(keyAttribute);
			ArrayList posIdFeatures = groupNode.getPosByNameLayertAtt(keyAttribute); //IDFEATURE_ATTRIBUTE_NAME);
			Iterator it = posIdFeatures.iterator();
			while(it.hasNext()){
				int posIdFeature = ((Integer) it.next()).intValue();
				if (posIdFeature != -1 || groupNode.children != null) {
					for (int i=0; i<groupNode.children.count; i++) {
						SVGNode node = (SVGNode) groupNode.children.data[i];
						try {
							int idFeatureNode = Integer.parseInt(node.getValueLayertAtt(posIdFeature));
							//El nodo con Identificador -1 es el skeleton para cargar mas informacion
							//Siempre tiene que venir uno con el valor -1 al comienzo de la capa.
							if (idFeatureNode == -1) {
								return node;
							}
						} catch (Exception e) {
							logger.error("Valor de idFeature incorrecto para el nodo " + i, e);
						}
					}
				}
			}
		}
		logger.error("Elemento prototipo no encontrado");
		return null;
	}
	
	/**
	 * Devuelve el nodo del grupo de elementos
	 * @return
	 * @throws IOException 
	 */
	public SVGNode getMetaInfoLayerElement() throws IOException {
		for (int i=0; i<numberOfFiles && currentSVGDocument == null; i++) {
			load(i);
		}
		
		if (currentSVGDocument == null || currentSVGDocument.root == null || currentSVGDocument.root.children == null ||
			currentSVGDocument.root.children.count != 1) return null;
		
		return (SVGNode) currentSVGDocument.root.children.data[0];
	}
	
	/**
	 * Serializa los cambios en la metainformacion
	 * @param outputStream
	 * @throws IOException
	 */
	public void serializeToSend(OutputStream outputStream) throws IOException {
		// Obtiene los ficheros modificados
		modifiedFiles = getModifiedFiles();
		
		// Enviar los cambios serializados para cada fichero modificado
		if (modifiedFiles.isEmpty()) {
			logger.debug("No hay ficheros de metainformacion modificados");
		}
		else {
			Enumeration en = modifiedFiles.elements();
			while (en.hasMoreElements()) {
				Integer i = (Integer) en.nextElement();
				load(i.intValue());
				currentSVGDocument.serializeModifiedNodes2XML(outputStream, true, false,SynchChangesUtils.ERRORS_LAYER_NAME);
			}
		}
	}
	
	/**
	 * Resetea los cambios en los nodos en los que no ha habido errores
	 * @param errorsGroup Nodo con los nodos erroneos
	 * @throws IOException
	 */
	public void resetChanges(SVGNode errorsGroup) throws IOException {
		if (modifiedFiles != null && !modifiedFiles.isEmpty()) {
			Enumeration en = modifiedFiles.elements();
			while (en.hasMoreElements()) {
				Integer i = (Integer) en.nextElement();
				load(i.intValue());
				if (!SynchChangesUtils.resetSVGChanges(currentSVGDocument.root, errorsGroup)){
					currentSVGDocument.setModified(false);
					currentSVGDocument.setSavePending(true);
				}
			}
		}
		modifiedFiles = null;
	}

	/**
	 * Indica si se ha modificado algo en la metainformacion
	 * @throws IOException 
	 */
	public boolean isModified() throws IOException {
		return !getModifiedFiles().isEmpty();
	}
	
	/**
	 * Obtiene los ficheros de metainformacion que han sido modificados.
	 * @return Vector con los indices de los ficheros modificados
	 * @throws IOException 
	 */
	private Vector getModifiedFiles() throws IOException {
		unload();
		// Vector para almacenar los nombres de los ficheros modificados
		Vector modifiedFiles = new Vector();
		// Recorrido de los ficheros de metainformacion
		for (int i=0; i<numberOfFiles; i++) {
			String fileName = baseFileName + infoType + i + ".svg";
			String filePath = projectPath + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				// Parsear el svg para ver si ha sido modificado
				FileInputStream fis = null;
				try {
					Sax2Parser p = new Sax2Parser();
					p.setContentHandler(new SVGContentHandler(modifiedFiles, i, p));
					fis = new FileInputStream(file);
					p.setInputStream(fis);
					p.parse();
				} catch (Exception e) {
					// No deberia ocurrir nunca
					logger.error("Error al parsear el fichero " + filePath);
				} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {}
					}
				}
			}
		}
		return modifiedFiles;
	}
	
	/**
	 * Indica si hay un documento abierto con cambios pendientes de guardar
	 */
	public boolean isSavePending() {
		if (currentSVGDocument != null)
			return currentSVGDocument.isSavePending();
		return false;
	}

	/**
	 * Guarda el documento de metainformacion modificado en su fichero correspondiente
	 * @throws IOException
	 */
	public void unload() throws IOException {
		if (currentSVGDocument != null && currentSVGDocument.isSavePending()) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(projectPath + currentFileName);
				currentSVGDocument.serializeSVG2XML(fos);
				currentSVGDocument.setSavePending(false);
			} catch (IOException e) {
				logger.error("Error al guardar el fichero de metainformación modificado", e);
				throw e;
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {}
				}
			}
		}
	}

	/**
	 * Listener que recibe los eventos al parsear el svg e interrumpe el proceso tras interpretar
	 * el atributo modified del nodo svg.
	 */
	private class SVGContentHandler implements ContentHandler {

		private Vector modifiedFiles;
		private int file;
		private Sax2Parser parser;

		public SVGContentHandler(Vector modifiedFiles, int file, Sax2Parser parser) {
			this.modifiedFiles = modifiedFiles;
			this.file = file;
			this.parser = parser;
		}

		/**
		 * Detecta el nodo svg, busca el atributo modified y si vale true lo incluye en una lista
		 * de celdas modificadas. Tras tratar el nodo svg, detiene el proceso.
		 */
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
			if (localName.equalsIgnoreCase("svg")) {
				for (int j=0; j<atts.getLength(); j++) {
					if (atts.getLocalName(j).equals("modified")) {
						boolean modified = Boolean.valueOf(atts.getValue(j)).booleanValue();
						if (modified) {
							logger.debug("El fichero " + file + " ha sido modificado");
							modifiedFiles.addElement(new Integer(file));
						}
						break;
					}
				}
				parser.interruptParsing();
			}
		}

		public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		}

		public void endDocument() throws SAXException {
		}

		public void endElement(String arg0, String arg1, String arg2) throws SAXException {
		}

		public void endPrefixMapping(String arg0) throws SAXException {
		}

		public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
		}

		public void processingInstruction(String arg0, String arg1) throws SAXException {
		}

		public void setDocumentLocator(Locator arg0) {
		}

		public void skippedEntity(String arg0) throws SAXException {
		}

		public void startDocument() throws SAXException {
		}

		public void startPrefixMapping(String arg0, String arg1) throws SAXException {
		}
	}



}
