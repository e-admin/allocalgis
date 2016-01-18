/**
 * PrjParser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.deegree.framework.xml.XMLTools;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.utils.LocalGISUtils;

public class PrjParser {
	private TableItem proyect=null;
	private String ruta="";
	private File fileProyect=null; 
	private Element e=null;
	private Vector valor=null;
	private Document doc=null;
	private String gridName=null;
	
	private static Logger logger = Global.getLoggerFor(PrjParser.class);
	
	public PrjParser(TableItem item, String rut){
		proyect=item;
		ruta=rut;
		loadProyect();
	}
	private void loadProyect() {

		if (ruta != null) {
			fileProyect = new File(ruta+File.separator+Config.PROJECT_PRJ);
			try {
				
				
				InputStream issld = new FileInputStream(fileProyect);
				Parser p = new Parser();
				p.setNodeFactory(new DomNodeFactory());
				p.setInputStream(issld);
				p.parse();
				doc = p.getDocument();
				// Extract the DOM root
				e = (Element) doc.getRoot();	
				//printNode(doc, "");
			}
			catch (Exception e) {
				logger.error(e);
			}
		}
	}
	public Document getDocument(){
		return doc;
	}
	private void printNode(Node node, String indent) {

		 switch (node.getNodeType()) {

         case Node.DOCUMENT_NODE:
             // recurse on each child
             NodeList nodes = node.getChildNodes();
             if (nodes != null) {
                 for (int i=0; i<nodes.getLength(); i++) {
                     printNode(nodes.item(i), "");
                 }
             }
             break;
             
         case Node.ELEMENT_NODE:
             String name = node.getNodeName();
             NamedNodeMap attributes = node.getAttributes();
             for (int i=0; i<attributes.getLength(); i++) {
                 Node current = attributes.item(i);
             }
             
             // recurse on each child
             NodeList children = node.getChildNodes();
             if (children != null) {
                 for (int i=0; i<children.getLength(); i++) {
                     printNode(children.item(i), indent + "  ");
                 }
             }
             break;

         case Node.TEXT_NODE:
        	 //getTextContent() en vez de getNodeValue()
        	 System.out.println(node.getNodeValue());
        	 String n=node.getNodeValue();
        	 if(!node.getNodeValue().equals("\n")){
        		 String valNode;
				try {
					valNode = new String (node.getNodeValue().getBytes(),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					valNode = node.getNodeValue();
					logger.error(e);
				} catch (DOMException e) {
					valNode ="";
					logger.error(e);
				}
        		 valor.add(valNode);
        	 }
             break;
     }
	}
	public String getDatosPrj(String campo){
		return XMLTools.getStringValue( campo, e, null );
	}
	
	public NodeList getElementsByTagName(String campo){
		return e.getElementsByTagName(campo);
	}
	
	//metodo que devuelve los item existentes en el prj
	public Vector getDetailItem(Node item) {

		try{
			valor= new Vector();
			printNode(item,"");
			//Node nodo=item.getFirstChild();
		}
		catch(Exception e){
			logger.error(e);
		}
		return valor;
	}
	public String getGridName() {

		return getDatosPrj("gridfile");
	}
	public String getPath() {

		return ruta;
	}
	public String getSrid() {
		return getDatosPrj("srid");
	}
	public URL getUrlGridFile(){
		URL urlGrid=null;
		try {
			File filePath = new File(ruta+File.separator+getDatosPrj("gridfile"));
			if (filePath.exists()) {
				urlGrid = new URL("file", "", LocalGISUtils.slashify(filePath.getAbsolutePath(), filePath.isDirectory()));
			}
		} catch (MalformedURLException e) {
			
			logger.error(e);
		}
		return urlGrid;
	}
	
	public Vector getEnabledApplications() {
		Vector enabledApps = new Vector();
		Element appsConfig = XMLTools.getChildElement(e, "appsconfig");
		if (appsConfig != null) {
			NodeList apps = appsConfig.getChildNodes();
			for (int i=0; i<apps.getLength(); i++) {
				if (apps.item(i) instanceof Element) {
					Element app = (Element) apps.item(i);
					try {
						String enabled = XMLTools.getRequiredAttrValue("enabled", app);
						if (Boolean.valueOf(enabled).booleanValue()) {
							enabledApps.addElement(app.getLocalName());
						}
					} catch (Exception e) {
						logger.error("Error al parsear aplicacion en el prj", e);
					}
				}
			}
		}
		return enabledApps;
	}
	
}
