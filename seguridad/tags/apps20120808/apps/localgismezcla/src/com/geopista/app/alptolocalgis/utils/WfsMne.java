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
package com.geopista.app.alptolocalgis.utils;

import java.awt.geom.NoninvertibleTransformException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import org.jdom.Namespace;
import javax.swing.JOptionPane;
import java.net.URL;
import java.net.ConnectException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.JDOMException;
import com.geopista.app.AppContext;
import com.geopista.app.alptolocalgis.panels.GraphicEditorPanel;
import com.geopista.ui.plugin.wfs.CoordinateConversion;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.util.CoordinateArrays;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.I18N;
import org.geotools.filter.ExpressionDOMParser;
import org.geotools.filter.FilterFactoryFinder;
import org.jdom.output.DOMOutputter;

/**
 * En esta clase se desarrollan todos los métodos necesarios para la implementación de un WFS
 * 
 */
public class WfsMne{

    static AppContext appContext = (AppContext) AppContext.getApplicationContext();
	/**
	 * Logger for this class
	 */
	
	public static String FICHERO_FEATURES = "features.xml";
	public static MarkCenterTool mark = null;
	
    public List listaMnes=new ArrayList();
    public List listaURLs=new ArrayList();
    
    private ArrayList lstResultados = new ArrayList();
    private ArrayList lstGeometrias = new ArrayList();
    private ArrayList lstIds = new ArrayList();

    public WfsMne() {
    	
    	listaMnes.add("Via");
    	listaMnes.add("Portal");
    	listaMnes.add("Idee");
        listaURLs.add(AppContext.getApplicationContext().getString("geopista.conexion.servidor.wfsmne"));
        listaURLs.add(AppContext.getApplicationContext().getString("geopista.conexion.servidor.wfsmne"));
        listaURLs.add("http://www.idee.es/wfs/IDEE-WFS-Nomenclator/wfs");
    }
    
    private MarkCenterTool getMarkCenterTool(){

    	if (mark == null){
    		mark = new MarkCenterTool(GraphicEditorPanel.getEditor());
    	}
    	return mark;
    }
  
    public ArrayList getLstResultados(){    	
    	return lstResultados;
    }
    
    public ArrayList getLstGeometrias(){    	
    	return lstGeometrias;
    }
    
    public ArrayList getLstIds(){    	
    	return lstIds;
    }
    /**
     * Genero los resultados según los criterios de entrada. Para ello tendré que leer
     * las urls almacenadas en url[]
     */
        
    public boolean searchResults(String url, TaskMonitorDialog progressDialog) 
    {
	    borrarFichero();
		return readFile(url,progressDialog);
    }
    
    public boolean searchResults(String[] url, TaskMonitorDialog progressDialog) 
    {
	    borrarFichero();
		return readFile(url,progressDialog);
    }
        
    public boolean readFile(String url, TaskMonitorDialog progressDialog){
    	
    	Namespace nameSpaceMne = Namespace.getNamespace("http://www.idee.es/mne");
    	
    	try{
    		
        	Document docNew = null;
	        
	        	docNew = new Document();
	        	URL nombreURL = new URL(url);
		        SAXBuilder builder = new SAXBuilder(false);
		        InputStream inputStr = nombreURL.openStream();
		        Document docNew2 = builder.build(inputStr);
		        Element rootElement2 = docNew2.getRootElement();
		        
		        docNew = new Document();
		        docNew.setRootElement(new Element("ResultCollection"));
		       
		        Element rootElement = docNew.getRootElement();
			    List listaFeatures = rootElement2.removeContent();
			    int numFeatures = listaFeatures.size();

			    for (int j= 0;j<numFeatures;j++){
			    	if (progressDialog.isCancelRequested())
			    		return false;
			        if (listaFeatures.get(j) instanceof Element){
				        Element feature = ((Element)listaFeatures.get(j));
					    if (!feature.getName().equals("boundedBy")){
					        Element entidad = feature.getChild("Entidad",nameSpaceMne); 
					        //Guardo el identificador de la features
						    lstIds.add(entidad.getAttribute("fid").getValue());
						    
						    //Obtengo el nombre de la feature y lo añado a la lista
							Element nombreEntidad = entidad.getChild("nombreEntidad",nameSpaceMne);
							Element nombre = nombreEntidad.getChild("nombre",nameSpaceMne);
							lstResultados.add((String)nombre.getValue());
							
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
						    			while (nodeChild != null && !progressDialog.isCancelRequested()){
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
						    			lstGeometrias.add(listaGeomAux);
		
						    		}
								}
							rootElement.addContent(feature);
					    }
				    }
			    }
		        inputStr.close();
		    
            FileOutputStream outputStrean = null;
            try
            {
                outputStrean = new FileOutputStream(this.FICHERO_FEATURES);
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
        	e.printStackTrace();
        	return false;
        }catch(ConnectException e){
        	e.printStackTrace();
        	return false;
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }
    }
    
    public boolean readFile(String[] url, TaskMonitorDialog progressDialog){
    	Namespace nameSpaceMne = Namespace.getNamespace("http://www.idee.es/mne");
    	
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

			    for (int j= 0;j<numFeatures;j++){
			    	if (progressDialog.isCancelRequested())
			    		return false;
			        if (listaFeatures.get(j) instanceof Element){
				        Element feature = ((Element)listaFeatures.get(j));
					    if (!feature.getName().equals("boundedBy")){
					        Element entidad = feature.getChild("Entidad",nameSpaceMne); 
					        //Guardo el identificador de la features
						    lstIds.add(entidad.getAttribute("fid").getValue());
						    
						    //Obtengo el nombre de la feature y lo añado a la lista
							Element nombreEntidad = entidad.getChild("nombreEntidad",nameSpaceMne);
							Element nombre = nombreEntidad.getChild("nombre",nameSpaceMne);
							lstResultados.add((String)nombre.getValue());
							
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
						    			while (nodeChild != null && !progressDialog.isCancelRequested()){
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
						    			lstGeometrias.add(listaGeomAux);
		
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
                outputStrean = new FileOutputStream(FICHERO_FEATURES);
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
        	e.printStackTrace();
        	return false;
        }catch(ConnectException e){
        	e.printStackTrace();
        	return false;
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }
    }
    
    public void borrarFichero(){
	    File fichero = new File(this.FICHERO_FEATURES);
	    if (fichero.exists()&& false)
	    	fichero.delete();
    }
    
    

    /**
     * Método que muestra toda la información disponible de una feature
     */
    public String createInformation(String fidEntidad, int index, String resultadoSeleccionado){
    	
    	Namespace nameSpaceMne = Namespace.getNamespace("http://www.idee.es/mne");
    	StringBuffer sbPagina = new StringBuffer();
    	try
    	{
    		//Saco el campo nombreEntidad/nombre del fichero para todas las features
    		//encontradas en la consultas para luego sacarlas en el JList. 
    		//También almacenaré en un array el identificador de cada entidad.
    		SAXBuilder builder = new SAXBuilder(false);
    		Document docNew = builder.build(FICHERO_FEATURES);
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
    					if (nombre.getValue().equals(resultadoSeleccionado))
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
    			if (cabecera.getName().equals("posicionEspacial") && getLstGeometrias()!=null){
    				Hashtable hTableGeo = (Hashtable)getLstGeometrias().get(index);
    				Geometry geo = (Geometry)hTableGeo.get("Point");
    				if (geo != null){
    					sbPagina = sbPagina.append("<tr bgcolor='Silver' align='center'><td align='center' colspan='2'><b>"+I18N.get("WfsDialog",cabecera.getName())+"</b></td></tr>\n");
    					sbPagina =  sbPagina.append("<tr><td><b>"+I18N.get("WfsDialog","SistemaRef")+":</b></td>&nbsp;<td>EPSG:"+geo.getSRID()+"</td></tr>\n");
    					sbPagina =  sbPagina.append("<tr><td><b>"+I18N.get("WfsDialog","Coord")+":</b></td>&nbsp;<td>"+geo.getCoordinate().x+","+geo.getCoordinate().y+"</td></tr>\n");
    				}
    			}else{
    				sbPagina = sbPagina.append("<tr bgcolor='Silver' align='center'><td align='center' colspan='2'><b>"+I18N.get("WfsDialog",cabecera.getName())+"</b></td></tr>\n");
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
    	}
    	return sbPagina.toString();
    }

    /**
     * Método que seleccionada una feature, centra el mapa de acuerdo a su centroide
     */
    public void centreMap(int indice, TaskMonitorDialog progressDialog){
        try
        {
        	int index = indice;
		    Hashtable tableGeometrias = (Hashtable)lstGeometrias.get(index);
		    if (!tableGeometrias.isEmpty()){
		        //Consideraré que el punto que tengo deberá estar en el centro del mapa. Así, calculo
		        //las coordenadas que debe tener el rectángulo donde se muestra el mapa
		    	
		    	MultiLineString multiLine = null;
		    	Polygon polygon = null;
		    	Point point = null;
		    	LineString line = null;
		    	
		    	if (tableGeometrias.get("MultiLineString")!= null && 
		    			tableGeometrias.get("MultiLineString") instanceof MultiLineString){
		    		multiLine = (MultiLineString)tableGeometrias.get("MultiLineString");
		    	}
		    	
		    	if (tableGeometrias.get("LineString")!= null && 
		    			tableGeometrias.get("LineString") instanceof LineString){
		    		line = (LineString)tableGeometrias.get("LineString");
		    	}
		    	
		    	if (tableGeometrias.get("Polygon")!= null && 
		    			tableGeometrias.get("Polygon") instanceof Polygon){
		    		polygon = (Polygon)tableGeometrias.get("Polygon");
		    	}
		    	
		    	if (tableGeometrias.get("Point")!= null && 
		    			tableGeometrias.get("Point") instanceof Point){
		    		point = (Point)tableGeometrias.get("Point");
		    	}
					    
			  //Convierto las coordenadas del punto que tengo al sistema de referencia de coordenadas del mapa
	    	    CoordinateSystem inCoord = CoordinateSystemRegistry.instance(appContext.getBlackboard()).get(point.getSRID());
	            CoordinateSystem outCoord = GraphicEditorPanel.getEditor().getLayerManager().getCoordinateSystem();
	            if (inCoord.getEPSGCode() != outCoord.getEPSGCode()){
	            	if (point != null){
	            		point = (Point)CoordinateConversion.instance().reproject(point,inCoord, outCoord);
	            		point.setSRID(outCoord.getEPSGCode());
	            	}
	            	if (multiLine != null){
	            		multiLine = (MultiLineString)CoordinateConversion.instance().reproject(multiLine,inCoord, outCoord);
	            	}
	            	if (line != null){
	            		line = (LineString)CoordinateConversion.instance().reproject(line,inCoord, outCoord);
	            	}
	            	if (polygon != null){
	            		polygon = (Polygon)CoordinateConversion.instance().reproject(polygon,inCoord, outCoord);
	            	}
	            }
		    	if (progressDialog.isCancelRequested())
		    		return;
		    			    	
		    	if (multiLine != null){
		    		GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport()
					.zoom(EnvelopeUtil.bufferByFraction(multiLine.getEnvelopeInternal(),0.03));	
		    		
		    		getMarkCenterTool().activate(GraphicEditorPanel.getEditor().getLayerViewPanel());
		    		getMarkCenterTool().deactivate();
		    		getMarkCenterTool().setFactorTolerance(2.0);
		    		getMarkCenterTool().paint(multiLine);
		    	}
		    	else if (line != null){	
		    		GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport()
					.zoom(EnvelopeUtil.bufferByFraction(line.getEnvelopeInternal(),0.03));	
		    		
		    		getMarkCenterTool().activate(GraphicEditorPanel.getEditor().getLayerViewPanel());
		    		getMarkCenterTool().deactivate();
		    		getMarkCenterTool().setFactorTolerance(2.0);
		    		getMarkCenterTool().paint(line);
		    	}
		    	else if (polygon != null){	
		    		GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport()
		    		.zoom(EnvelopeUtil.bufferByFraction(polygon.getEnvelopeInternal(), 0.03));
		    		
		    		getMarkCenterTool().activate(GraphicEditorPanel.getEditor().getLayerViewPanel());
		    		getMarkCenterTool().deactivate();
		    		getMarkCenterTool().setFactorTolerance(2.0);
		    		getMarkCenterTool().paint(polygon);
		    	}
		    	else if (point != null){
		    		GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport()
		    		.zoom(toEnvelope(point.getCoordinate(), GraphicEditorPanel.getEditor().getLayerManager()));
		    		
		    		getMarkCenterTool().activate(GraphicEditorPanel.getEditor().getLayerViewPanel());
		    		getMarkCenterTool().deactivate();
		    		getMarkCenterTool().setFactorTolerance(0.5);
		    		getMarkCenterTool().paint(point);
		    	}
			    		        
		    }else{
	        	JOptionPane.showMessageDialog(null, I18N.get("AlpToLocalGIS","GeometriaNula"),
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
    
    private Envelope toEnvelope(Coordinate coordinate, LayerManager layerManager) {
		int segments = 0;
		int segmentSum = 0;
		outer : for (Iterator i = layerManager.iterator(); i.hasNext(); ) {
			Layer layer = (Layer) i.next();
			for (Iterator j = layer.getFeatureCollectionWrapper().iterator(); j
					.hasNext(); ) {
				Feature feature = (Feature) j.next();
                Collection coordinateArrays = CoordinateArrays.toCoordinateArrays(feature.getGeometry(), false);
                for (Iterator k = coordinateArrays.iterator(); k.hasNext(); ) {
                	Coordinate[] coordinates = (Coordinate[]) k.next();
                    for (int a = 1; a < coordinates.length; a++) {
                        segments++;
                    	segmentSum += coordinates[a].distance(coordinates[a-1]);
                        if (segments > 100) { break outer; }
                    }
                }
			}
		}
		Envelope envelope = new Envelope(coordinate);
		if (segmentSum > 0) {
			envelope = EnvelopeUtil.expand(envelope,
					segmentSum / (double) segments);
		} else {
			envelope = EnvelopeUtil.expand(envelope, 50);
		}
		return envelope;
	}
  }