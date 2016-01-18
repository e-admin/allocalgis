/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 19-nov-2004 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.wfs;

import java.awt.geom.NoninvertibleTransformException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.filter.ExpressionDOMParser;
import org.geotools.filter.FilterFactoryFinder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.Table;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.TaskFrame;



/**
 * En esta clase se desarrollan todos los métodos necesarios para la implementación de un WFS-G
 * 
 */
public class WfsMne{

    static AppContext appContext = (AppContext) AppContext.getApplicationContext();
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(WfsMne.class);
	private WfsDialog dialog;
    public List listaMnes=new ArrayList();
    public List listaURLs=new ArrayList();

	
    // Cambiado por el interfaz general del Component raíz del framework. [Juan Pablo]
    public WfsMne(WfsDialog dialog) {
    	this.dialog = dialog;
    	String urlMne = appContext.getString("geopista.conexion.servidor.wfsmne");
    	String urlIdee = appContext.getString("geopista.conexion.servidor.idee");
    	listaMnes.add("Via");
    	listaMnes.add("Portal");
    	listaMnes.add("Idee");
        listaURLs.add(urlMne);
        listaURLs.add(urlMne);
        listaURLs.add(urlIdee);    	
    }
    
  
    /**
     * Genero los resultados según los criterios de entrada. Para ello tendré que leer
     * las urls almacenadas en url[]
     */
    public void searchResults(String[] url) 
    {
	    borrarFichero();
	    dialog.vResult = new Vector();
		readFile(url);
	    dialog.jResultado.revalidate();
	    dialog.jResultado.setListData(dialog.vResult);
    }
    
    /**
     * Creo un fichero FICHERO_FEATURES con el resultado de la consulta que se ha hecho a uno
     * o varios mne
     */
    public boolean readFile(String[] url){
    	Namespace nameSpaceMne = Namespace.getNamespace("http://www.idee.es/mne");
    	Namespace nameSpaceGml = Namespace.getNamespace("http://www.opengis.net/gml");
    	try{
        	Document docNew = null;
	        int n = url.length;
	        for (int i=0;i<n;i++){
	        	if (i==0){
	        		docNew = new Document();
	        	}
	        	URL nombreURL = new URL(url[i]);
		        SAXBuilder builder = new SAXBuilder(false);
		        InputStream inputStr = nombreURL.openStream();
		        Document docNew2 = builder.build(inputStr);
		        Element rootElement2 = docNew2.getRootElement();
		        if (i == 0){
		        	docNew = new Document();
		        	docNew.setRootElement(new Element("ResultCollection"));
		        }
		        Element rootElement = docNew.getRootElement();
			    List listaFeatures = rootElement2.removeContent();
			    int numFeatures = listaFeatures.size();
		    	dialog.listaGeometrias = new ArrayList();

			    for (int j= 0;j<numFeatures;j++){
			    	if (dialog.progressDialog.isCancelRequested())
			    		return false;
			        if (listaFeatures.get(j) instanceof Element){
				        Element feature = ((Element)listaFeatures.get(j));
					    if (!feature.getName().equals("boundedBy")){
					        Element entidad = feature.getChild("Entidad",nameSpaceMne); 
					        //Guardo el identificador de la features
						    dialog.listaIds.add(entidad.getAttribute("fid").getValue());
						    
						    //Obtengo el nombre de la feature y lo añado a la lista
							Element nombreEntidad = entidad.getChild("nombreEntidad",nameSpaceMne);
							Element nombre = nombreEntidad.getChild("nombre",nameSpaceMne);
							dialog.vResult.add((String)nombre.getValue());
							
							//Guardo las geometrías encontradas en las features
							Element posicionEspacial = entidad.getChild("posicionEspacial",nameSpaceMne);
							if (posicionEspacial != null){
					    		org.jdom.Document doc = new org.jdom.Document();
					    		Element elemento = (Element)posicionEspacial.clone();
					    		doc.setRootElement(elemento);
					    		DOMOutputter dou= new DOMOutputter();
					    		org.w3c.dom.Document nod= dou.output(doc);
					    		
								Hashtable listaGeomAux = new Hashtable();
					    		ExpressionDOMParser gmlparser = new ExpressionDOMParser(FilterFactoryFinder.createFilterFactory());
					    		org.w3c.dom.Node node = null;
					    		if (nod.getFirstChild() != null)
					    			node = nod.getFirstChild();
						    		if (node != null){
						    			org.w3c.dom.Node nodeChild = null;
						    			if (node.getFirstChild() != null)
						    				nodeChild = node.getFirstChild();
						    			while (nodeChild != null && !dialog.progressDialog.isCancelRequested()){
						    				if (nodeChild.getNodeType()!=org.w3c.dom.Node.TEXT_NODE){
								    			Geometry geo= gmlparser.gml(nodeChild);
						    					String[] srsName = nodeChild.getAttributes().getNamedItem("srsName").getNodeValue().split("[#:]");
						    					int longitud = srsName.length;
						    					int srid = 0;
						    					if (longitud>0){
						    						srid = Integer.parseInt(srsName[longitud-1]);
						    						geo.setSRID(srid);
						    					}
						    			        geo.geometryChanged();
							    				listaGeomAux.put(geo.getGeometryType(),geo);
							    				if (!geo.getGeometryType().equals("Point")){
							    					Point point = geo.getCentroid();
							    					point.setSRID(srid);
							    					point.geometryChanged();
							    					listaGeomAux.put("Point",point);
							    				}
						    				}
						    				nodeChild = nodeChild.getNextSibling();
						    			}
						    			dialog.listaGeometrias.add(listaGeomAux);
		
						    		}
								}
							rootElement.addContent(feature);
					    }
				    }
			    }
		        inputStr.close();
		    }
            FileOutputStream outputStrean = null;
            try
            {
                outputStrean = new FileOutputStream(dialog.FICHERO_FEATURES);
                XMLOutputter outp = new XMLOutputter();
                outp.output(docNew, outputStrean);
                	
            } finally
            {
                try
                {
                    outputStrean.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return true;
        }catch(JDOMException e){
        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","ErrorWfs"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	e.printStackTrace();
        	return false;
        }catch(ConnectException e){
        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","ErrorConexion"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	e.printStackTrace();
        	return false;
        }catch(Exception e){
        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","ErrorWfs"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	e.printStackTrace();
        	return false;
        }
    }
    
    public void borrarFichero(){
	    File fichero = new File(dialog.FICHERO_FEATURES);
	    if (fichero.exists()&& false)
	    	fichero.delete();
    }
    
    

    /**
     * Método que muestra toda la información disponible de una feature
     */
    public String createInformation(String fidEntidad, int index){
    	Namespace nameSpaceGml = Namespace.getNamespace("http://www.opengis.net/gml");
    	Namespace nameSpaceMne = Namespace.getNamespace("http://www.idee.es/mne");
    	StringBuffer sbPagina = new StringBuffer();
        try
        {
        	//Saco el campo nombreEntidad/nombre del fichero para todas las features
        	//encontradas en la consultas para luego sacarlas en el JList. 
        	//También almacenaré en un array el identificador de cada entidad.
	        SAXBuilder builder = new SAXBuilder(false);
	        Document docNew = builder.build(dialog.FICHERO_FEATURES);
	        Element rootElement = docNew.getRootElement();
	        List listaFeatures = rootElement.getChildren(); 
	        Iterator itFeatures = listaFeatures.iterator();
	        boolean seguir = true;
	        Element entidad = null;
	        while(itFeatures.hasNext() && seguir == true){
	        	Element feature = ((Element)itFeatures.next());
	        	if (!feature.getName().equals("boundedBy")){
			        entidad = feature.getChild("Entidad",nameSpaceMne);
			        if (entidad.getAttribute("fid").getValue().equals(fidEntidad)){
					    Element nombreEntidad = entidad.getChild("nombreEntidad",nameSpaceMne);
					    Element nombre = nombreEntidad.getChild("nombre",nameSpaceMne);
			        	if (nombre.getValue().equals((String)dialog.jResultado.getSelectedValue()))
			        		seguir = false;
			        }
	        	}
	        }
	        List listaEntidad = entidad.getChildren();
	        Iterator itEntidad = listaEntidad.iterator();
	    	sbPagina = sbPagina.append("<html><head></head><body><table width='100%'>\n");
	    	//Guardo los puntos de las geometrías para poder centrarlas en el mapa
	        while(itEntidad.hasNext()){
	        	Element cabecera = ((Element)itEntidad.next());
	        	List listaElementos = cabecera.getChildren();
	        	Iterator itElementos =listaElementos.iterator();
	        	if (cabecera.getName().equals("posicionEspacial")){
	        		Hashtable hTableGeo = (Hashtable)dialog.listaGeometrias.get(index);
	        		Geometry geo = (Geometry)hTableGeo.get("Point");
	        		if (geo != null){
	    	        	sbPagina = sbPagina.append("<tr bgcolor='Silver'><td colspan='2' align='center'><b>"+I18N.get("WfsDialog",cabecera.getName())+"</b></td></tr>\n");
		        		sbPagina =  sbPagina.append("<tr><td><b>"+I18N.get("WfsDialog","SistemaRef")+":</b></td>&nbsp;<td>EPSG:"+geo.getSRID()+"</td></tr>\n");
		        		sbPagina =  sbPagina.append("<tr><td><b>"+I18N.get("WfsDialog","Coord")+":</b></td>&nbsp;<td>"+geo.getCoordinate().x+","+geo.getCoordinate().y+"</td></tr>\n");
	        		}
	        	}else{
		        	sbPagina = sbPagina.append("<tr bgcolor='Silver'><td colspan='2' align='center'><b>"+I18N.get("WfsDialog",cabecera.getName())+"</b></td></tr>\n");
		        	while (itElementos.hasNext()){
		        		Element elemento = ((Element)itElementos.next());
		        		sbPagina =  sbPagina.append("<tr><td><b>"+I18N.get("WfsDialog",elemento.getName())+":</b></td>&nbsp;<td>"+elemento.getValue()+"</td></tr>\n");
		        	}
	        	}
	        }
	        sbPagina = sbPagina.append("</table></html>");
        } catch (JDOMException e){
        	e.printStackTrace();
        } catch (IOException e){
        	e.printStackTrace();
        } catch (Exception e){
        	e.printStackTrace();
        }finally{
        	return sbPagina.toString();
        }
    }

    /**
     * Método que seleccionada una feature, centra el mapa de acuerdo a su centroide
     */
    public void centreMap(){
        try
        {
        	int index = dialog.jResultado.getSelectedIndex();
		    Hashtable tableGeometrias = (Hashtable)dialog.listaGeometrias.get(index);
		    if (!tableGeometrias.isEmpty()){
		        //Consideraré que el punto que tengo deberá estar en el centro del mapa. Así, calculo
		        //las coordenadas que debe tener el rectángulo donde se muestra el mapa
			    Point point = (Point)tableGeometrias.get("Point");
			    Coordinate coordinate = point.getCoordinate();
		        double x = coordinate.x;
		        double y = coordinate.y;
		        coordinate = new Coordinate(x,y);
	
		    	//Convierto las coordenadas del punto que tengo al sistema de referencia de coordenadas del mapa
	    	    CoordinateSystem inCoord = CoordinateSystemRegistry.instance(appContext.getBlackboard()).get(point.getSRID());
	            CoordinateSystem outCoord = dialog.context.getLayerManager().getCoordinateSystem();
	            if (inCoord.getEPSGCode() != outCoord.getEPSGCode()){
	                point = (Point)CoordinateConversion.instance().reproject(point,inCoord, outCoord);
	                point.setSRID(outCoord.getEPSGCode());
	            }
		    	if (dialog.progressDialog.isCancelRequested())
		    		return;
	
		    	x = point.getCoordinate().x;
	            y = point.getCoordinate().y;
	
	        	//Calculo las coordenadas actuales en las que se muestra el mapa para saber la 
	        	//distancia que hay entre xmin xmax y ymin ymax
	            ILayerViewPanel layerPanel = null;
	            if(dialog.context.getActiveInternalFrame() != null)
	            	layerPanel = ((TaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel();
	            else
	            	layerPanel = dialog.context.getLayerViewPanel();
	        	double xMin = layerPanel.getViewport().getEnvelopeInModelCoordinates().getMinX();
	        	double xMax = layerPanel.getViewport().getEnvelopeInModelCoordinates().getMaxX();
	        	double yMin = layerPanel.getViewport().getEnvelopeInModelCoordinates().getMinY();
	        	double yMax = layerPanel.getViewport().getEnvelopeInModelCoordinates().getMaxY();
	        	double distanciaX = (xMax - xMin)/2;
	        	double distanciaY = (yMax - yMin)/2;
	        		
		        Envelope envelope = new Envelope();
		        envelope.init(x-distanciaX,x+distanciaX,y-distanciaY,y+distanciaY);
		        	
		        layerPanel.getViewport().zoom(envelope);
		        MarkCenterTool mark = new MarkCenterTool(dialog.context.getWorkbenchContext());
		        mark.paintCenter(x, y);
		    }else{
	        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","GeometriaNula"),
	        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
		    }
        }catch(NoninvertibleTransformException ne)
        {
        	ne.printStackTrace();
        }catch(Exception ne)
        {
        	ne.printStackTrace();
        }
    }
    
    
    /**
     * Se crea la feature en el mapa
     */
    public void createFeature(){
    	try{
	    	int index = dialog.jResultado.getSelectedIndex();
	    	Hashtable tableGeometrias = (Hashtable)dialog.listaGeometrias.get(index);
	    	CoordinateSystem outCoord;
	    	if (tableGeometrias != null){
		    	Geometry geom = null;
		    	FeatureSchema fSchema = null;
		    	GeopistaSchema gSchema = null;
		    	int geometryType = 0;
	    		if (dialog.capaCreacion == null){
		        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","SelCapa"),
		        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
		        	return;
	    		}
		    	if (dialog.capaCreacion.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema){
		    		gSchema = (GeopistaSchema)dialog.capaCreacion.getFeatureCollectionWrapper().getFeatureSchema();
			    	if (gSchema.getCoordinateSystem().getEPSGCode() == 0){
			        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","SelSistCoordCapa"),
			        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
			        	return;
			    	}
			    	Table table;
			    	if (gSchema.getColumnByAttribute("GEOMETRIA") != null)
			    		table = gSchema.getColumnByAttribute("GEOMETRIA").getTable();
			    	else
			    		table = gSchema.getColumnByAttribute("GEOMETRY").getTable();
		    		geometryType = table.getGeometryType();
			    	outCoord = gSchema.getCoordinateSystem();
		    	}else{
		    		fSchema = (FeatureSchema)dialog.capaCreacion.getFeatureCollectionWrapper().getFeatureSchema();
			    	if (fSchema.getCoordinateSystem().getEPSGCode() == 0){
			        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","SelSistCoordCapa"),
			        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
			        	return;
			    	}
			    	outCoord = fSchema.getCoordinateSystem();
		    	}
			    Enumeration en = tableGeometrias.elements();
			    while (en.hasMoreElements()){
			    	if (dialog.progressDialog.isCancelRequested())
			    		return;
				    geom = (Geometry)en.nextElement();
			    	GeopistaFeature featureNew = null;
			    	if (gSchema != null){
			    		//Mira si la geometría es adecuada para insertarla en la capa
			    		if (!dialog.testGeometry(geometryType, geom)){
				        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","GeometriaNoPermitida"),
				        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
			    			return;
			    		}
			    		featureNew = new GeopistaFeature(gSchema);
			    	}else{
			    		featureNew = new GeopistaFeature(fSchema);
			    	}
				    featureNew.setNew(true);
	    	    	CoordinateSystem inCoord = CoordinateSystemRegistry.instance(appContext.getBlackboard()).get(geom.getSRID());
	                if (inCoord.getEPSGCode() != outCoord.getEPSGCode())
	                	geom = CoordinateConversion.instance().reproject(geom,inCoord, outCoord);
				    featureNew.setGeometry(geom);
				    featureNew.setLayer((GeopistaLayer)dialog.capaCreacion);
				    dialog.capaCreacion.getFeatureCollectionWrapper().add(featureNew);
			    }
		    }else{
	        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","GeometriaNula"),
	        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
		    }
	    }catch(IllegalArgumentException e){
        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","CapaIncorrecta"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	e.printStackTrace();
    	}catch(Exception e){
        	e.printStackTrace();
    	}
    }
    
    
    
    
    /**
     * Compruebo que la geometría que se quiere centrar o que se quiere crear dentro del mapa, está dentro
     * del bounding box
     */
/*    private boolean insideBoundingBox(Geometry geom){
		double xMin = ((GeopistaTaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinX();
		double xMax = ((GeopistaTaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxX();
		double yMin = ((GeopistaTaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinY();
		double yMax = ((GeopistaTaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxY();
		
    	Envelope envelope = new Envelope();
    	envelope.init(xMin,xMax,yMin,yMax);
    	Envelope envelopeGeometry = geom.getEnvelopeInternal();
    	GeopistaLayer layer = (GeopistaLayer)dialog.context.getLayerManager().getLayer(0);
    	if (layer != null && layer.isLocal())
    		return true;
    	else{
	    	if (envelope.contains(envelopeGeometry)||envelope.intersects(envelopeGeometry))
	    		return true;
	    	else{
	        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","FueraBBOX"),
	        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
	    		return false;
	    	}
    	}
    }*/
    
  }