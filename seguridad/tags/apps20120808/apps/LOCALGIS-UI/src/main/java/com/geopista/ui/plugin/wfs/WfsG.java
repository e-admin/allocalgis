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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Enumeration;
import org.jdom.Namespace;
import java.awt.Shape;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.net.URL;
import java.net.ConnectException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.JDOMException;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.app.AppContext;
import com.geopista.editor.WorkBench;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.GeopistaTaskFrame;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.snap.SnapManager;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.Table;
import com.geopista.model.GeopistaLayer;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.NoninvertibleTransformException;

import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import org.geotools.filter.ExpressionDOMParser;
import org.geotools.filter.FilterFactoryFinder;
import org.jdom.output.DOMOutputter;
import com.geopista.ui.cursortool.AbstractCursorTool;
import com.vividsolutions.jump.workbench.model.Layer;


/**
 * En esta clase se desarrollan todos los métodos necesarios para la implementación de un WFS-G
 * 
 */
public class WfsG{

    static AppContext appContext = (AppContext) AppContext.getApplicationContext();
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(WfsG.class);
	private WfsDialog dialog;
	private List features = new ArrayList();
    public List listaWfsG=new ArrayList();
    public List listaURLs=new ArrayList();

	
    // Cambiado por el interfaz general del Component raíz del framework. [Juan Pablo]
    public WfsG(WfsDialog dialog) {
    	this.dialog = dialog;
    }
    
  
    /**
     * Genero los resultados según los criterios de entrada
     */
    public void searchResults(String[] url) 
    {
	    dialog.vResult = new Vector();
		readFile(url);
	    dialog.jResultado.revalidate();
	    dialog.jResultado.setListData(dialog.vResult);
    }
    
    /**
     * Creo un fichero FICHERO_FEATURES.xml con la respuesta generada la consultar la url
     * correspondiente
     */
    public boolean readFile(String[] url){
    	Namespace nameSpaceIso = Namespace.getNamespace("http://www.opengis.net/iso19112");
    	Namespace nameSpaceGml = Namespace.getNamespace("http://www.opengis.net/gml");
    	try{
        	Document docNew = null;
	        int n = url.length;
	        for (int i=0;i<n;i++){
	        	URL nombreURL = new URL(url[i]);
		        SAXBuilder builder = new SAXBuilder(false);
		        docNew = builder.build(nombreURL.openStream());
			    Element rootElement = docNew.getRootElement();
			    List hijos = rootElement.getChildren();
			    Iterator ithijos = hijos.iterator();
			    while (ithijos.hasNext()){
			    	Element hijo = (Element)ithijos.next();
			    	if (hijo.getName().equals("relatedFeature")){
			    		hijos = hijo.getChildren();
					    Iterator ithijos2 = hijos.iterator();
					    while (ithijos2.hasNext()){
					    	hijo = (Element)ithijos2.next();
				    		String[] feature = new String[3];
				    		feature[0] = hijo.getAttributeValue("fid");
				    		feature[1] = hijo.getChildText("geographicIdentifier",nameSpaceIso);
				    		Element position = hijo.getChild("position",nameSpaceIso);
				    		Element point = position.getChild("Point",nameSpaceGml);
				    		feature[2] = point.getChildText("coordinates",nameSpaceGml);
					    	features.add(feature);
					    	dialog.vResult.add(feature[0]);
					    }
			    	}
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
    
    /**
     * Método que muestra toda la información disponible de una feature
     */
    public String createInformation(int index){
    	StringBuffer sbPagina = new StringBuffer();
        try
        {
	    	sbPagina = sbPagina.append("<html><head></head><body><table width='100%'>\n");
	        String[] feature = (String[])features.get(index);
	        sbPagina =  sbPagina.append("<tr><td><b>"+I18N.get("WfsDialog","fid")+":</b></td>&nbsp;<td>"+feature[0]+"</td></tr>\n");
	        sbPagina =  sbPagina.append("<tr><td><b>"+I18N.get("WfsDialog","geographicIdentifier")+":</b></td>&nbsp;<td>"+feature[1]+"</td></tr>\n");
	        sbPagina =  sbPagina.append("<tr><td><b>"+I18N.get("WfsDialog","posicionEspacial")+":</b></td>&nbsp;<td>"+feature[2]+"</td></tr>\n");
	        sbPagina = sbPagina.append("</table></html>");
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
    	//Primero compruebo que he seleccionado una feature
    	int index = dialog.jResultado.getSelectedIndex();
        try
        {
		    Hashtable tableGeometrias = (Hashtable)dialog.listaGeometrias.get(index);
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
            if (inCoord.getEPSGCode() != outCoord.getEPSGCode())
                point = (Point)CoordinateConversion.instance().reproject(point,inCoord, outCoord);

            x = point.getCoordinate().x;
            y = point.getCoordinate().y;

        	//Calculo las coordenadas actuales en las que se muestra el mapa para saber la 
        	//distancia que hay entre xmin xmax y ymin ymax
        	double xMin = ((TaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinX();
        	double xMax = ((TaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxX();
        	double yMin = ((TaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinY();
        	double yMax = ((TaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxY();
        	double distanciaX = (xMax - xMin)/2;
        	double distanciaY = (yMax - yMin)/2;
        		
	        Envelope envelope = new Envelope();
	        envelope.init(x-distanciaX,x+distanciaX,y-distanciaY,y+distanciaY);
	        	
	        ((TaskFrame)dialog.context.getActiveInternalFrame()).getLayerViewPanel().getViewport().zoom(envelope);
	        MarkCenterTool mark = new MarkCenterTool(dialog.context.getWorkbenchFrame().getContext());
	        mark.paintCenter(x, y);
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
	    	Geometry geom = null;
	    	FeatureSchema fSchema = null;
	    	GeopistaSchema gSchema = null;
	    	int geometryType = 0;
	    	if (dialog.capaCreacion.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema){
	    		gSchema = (GeopistaSchema)dialog.capaCreacion.getFeatureCollectionWrapper().getFeatureSchema();
	    		Table table = gSchema.getColumnByAttribute("GEOMETRIA").getTable();
	    		geometryType = table.getGeometryType();
	    	}else{
	    		fSchema = (FeatureSchema)dialog.capaCreacion.getFeatureCollectionWrapper().getFeatureSchema();
	    	}
		    Enumeration en = tableGeometrias.elements();
		    while (en.hasMoreElements()){
			    geom = (Geometry)en.nextElement();
		    	GeopistaFeature featureNew = null;
		    	if (gSchema != null){
		    		//Mira si la geometría es adecuada para insertarla en la capa
		    		if (!dialog.testGeometry(geometryType, geom))
		    			return;
		    		featureNew = new GeopistaFeature(gSchema);
		    	}else{
		    		featureNew = new GeopistaFeature(fSchema);
		    	}
			    featureNew.setNew(true);
    	    	CoordinateSystem inCoord = CoordinateSystemRegistry.instance(appContext.getBlackboard()).get(geom.getSRID());
                CoordinateSystem outCoord = dialog.context.getLayerManager().getCoordinateSystem();
                geom = CoordinateConversion.instance().reproject(geom,inCoord, outCoord);
			    featureNew.setGeometry(geom);
			    featureNew.setLayer((GeopistaLayer)dialog.capaCreacion);
			    dialog.capaCreacion.getFeatureCollectionWrapper().add(featureNew);
		    }
    	}catch(IllegalArgumentException e){
        	JOptionPane.showMessageDialog(dialog, I18N.get("WfsDialog","CapaIncorrecta"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	e.printStackTrace();
    	}catch(Exception e){
        	e.printStackTrace();
    	}
    }
        
  }