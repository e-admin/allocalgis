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
 * Created on 21-sep-2004 by juacas
 *
 *
 */
package com.geopista.ui.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.control.ISesion;
import com.geopista.ui.components.FeatureExpressionPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.expression.FeatureExpresionParser;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureDatasetFactory;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.geom.InteriorPointFinder;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.ArrowLineStringEndpointStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.CircleLineStringEndpointStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class DistanceLinkingPanel extends JPanel
{
	/**
	 *
	 */

	private static final Log logger = LogFactory.getLog(DistanceLinkingPanel.class);
	private  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private static final double BUFFERDISTANCEINCREMENT = 10.0;
	private static final String FINAL_LINKING_ARRAY = DistanceLinkingPanel.class.getName()+"_FinalLinking";
	public static final String SELECTEDSOURCELAYER = DistanceLinkingPanel.class.getName()+"_sourceLayer";
	public static final String SELECTEDTARGETLAYER = DistanceLinkingPanel.class.getName()+"_targetLayer";
	public static final String EXPRESION_FORMULA = DistanceLinkingPanel.class.getName()+"_expresionFormula";
	private final String REPORTING_LAYER_NAME =  aplicacion.getI18nString("lyrPoliceStreetLayer");
	private final String ATTRIBUTE_ID_ON_TARGET = DistanceLinkingPanel.class.getName()+"ATTRIBUTE_ID_ON_TARGET";
	private final String BUFFERLAYER_NAME =  aplicacion.getI18nString("lyrBufferLayer");
	private final String ATTRIBUTE_ID_ON_SOURCE = DistanceLinkingPanel.class.getName()+"ATTRIBUTE_ID_ON_SOURCE";
	private final String ERRORLAYER_NAME =  aplicacion.getI18nString("lyrErrorLayer");
	private HashMap arrowFeatureRelation = new HashMap();

	private JButton btnAutoLink = null;
	private JButton btnManualLink = null;
	private JButton btnReset = null;
	private JButton btnSetField = null;
	private Layer bufferLayer = null;

	private JList lstSourceField = null;
	private JComboBox cbSourceLayer = null;
	private JList lstTargetField = null;
	private JComboBox cbTargetLayer = null;

	private Color color = Color.BLUE;
	private Color colorError = Color.RED;

	private TreeMap finalRelation = new TreeMap();

	private FeatureCollection initialErrorFeature=null;
	private FeatureCollection initialFeature = null;
	private FeatureCollection initialReportDataset=null;

	private javax.swing.JPanel jContentPane = null;
	private JLabel lblTargetLayer = null;

	private PlugInContext localContext;
	private JLabel sourceLabel = null;
	private ToolboxDialog toolbox;

	private JLabel jLabel1 = null;
	private JScrollPane sourceFieldScrollPane = null;
	private JScrollPane targetFieldScrollPane = null;
	private JCheckBox chkUpdateMap = null;
	private JPanel jPanel = null;
	private FeatureExpressionPanel featureExpressionPanel = null;
	private FeatureSchema	initialFeatureSchema;
	/**
	 * This is the default constructor
	 */
	public DistanceLinkingPanel(ToolboxDialog toolbox) {
		super();
		this.toolbox=toolbox;

        toolbox.addWindowListener(new WindowAdapter() {

            public void windowActivated(WindowEvent e) {
                setup();
            }
        });
        if(toolbox.getContext().getIWorkbench().getGuiComponent().getDesktopPane() instanceof JDesktopPane)
        {
        GUIUtil
            .addInternalFrameListener( // solo para el modelo MDI con InternalFrames
                (JDesktopPane)toolbox.getContext().getIWorkbench().getGuiComponent().getDesktopPane(),
                GUIUtil.toInternalFrameListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setup();
            }


        }));}


        //TODO: las features de error deben tener el mismo esquema que las
        // source feature ya que se realizan inserciones de ese tipo de features.

		initialFeatureSchema = new FeatureSchema();
		initialFeatureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        initialFeatureSchema.addAttribute("Message",AttributeType.STRING);
        initialFeature = new FeatureDataset(initialFeatureSchema);
        initialErrorFeature = new FeatureDataset(initialFeatureSchema);
        initialReportDataset = new FeatureDataset(initialFeatureSchema);


		initialize();
	}
    protected void applyArrowStyles(Layer layer) {
        if (null == layer.getStyle(ArrowLineStringEndpointStyle.class)) {
            layer.addStyle(new ArrowLineStringEndpointStyle.SolidEnd());
        }
//        if (null == layer.getStyle(CircleLineStringEndpointStyle.Start.class)) {
//            layer.addStyle(new CircleLineStringEndpointStyle.Start());
//        }
        layer.getBasicStyle().setLineColor(color);
        layer.getBasicStyle().setFillColor(color);
        layer.getBasicStyle().setRenderingFill(false);
        layer.setDrawingLast(true);
    }
    protected void applyErrorStyles(Layer layer) {

        if (null == layer.getStyle(CircleLineStringEndpointStyle.Start.class)) {
            layer.addStyle(new CircleLineStringEndpointStyle.Start());
        }

        layer.getBasicStyle().setLineColor(colorError);
        layer.getBasicStyle().setFillColor(colorError);
        layer.getBasicStyle().setRenderingFill(true);
        layer.setDrawingLast(true);
        layer.getVertexStyle().setSize(6);
        layer.getVertexStyle().setEnabled(true);
        layer.setDrawingLast(true);
    }

    private void associateConnect(ActionEvent e)
    {
    TreeMap    finalRelation=(TreeMap) getFromBlackboard(FINAL_LINKING_ARRAY);
    if (finalRelation==null)
    {
    	finalRelation=new TreeMap();
    	putToBlackboard(FINAL_LINKING_ARRAY,finalRelation);
    }
    Set finalRelationSet = finalRelation.keySet();

      Layer sourceLayer = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
      List sourceFeatures = sourceLayer.getFeatureCollectionWrapper().getFeatures();
      Iterator featuresPoliceNumberIter = sourceFeatures.iterator();
      while(featuresPoliceNumberIter.hasNext())
      {
        GeopistaFeature actualFeature = (GeopistaFeature) featuresPoliceNumberIter.next();
        actualFeature.setAttribute(ATTRIBUTE_ID_ON_SOURCE,new Integer(0));
      }

      Iterator finalRelationSetIter = finalRelationSet.iterator();
      while(finalRelationSetIter.hasNext())
      {
        GeopistaFeature actualSourceFeature = (GeopistaFeature) finalRelationSetIter.next();
        GeopistaFeature actualStreetFeature = (GeopistaFeature) finalRelation.get(actualSourceFeature);
        actualSourceFeature.setAttribute(ATTRIBUTE_ID_ON_SOURCE,actualStreetFeature.getAttribute(ATTRIBUTE_ID_ON_TARGET));
      }
    }
    private void zoomFeature(Feature actualFeature)
    {
    	if (!chkUpdateMap.isSelected())return;
    	Envelope env=actualFeature.getGeometry().getEnvelopeInternal();
		double width=Math.max(env.getWidth(),1);
		double height=Math.max(env.getHeight(),1);
		// anula el ajuste
		width=0;height=0;
		Envelope env2 = new Envelope(
				env.getMinX()-width*0.3,
				env.getMinY()-height*0.3,
				env.getMaxX()+width*0.3,
				env.getMaxY()+height*0.3);
		try
		{
		localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getViewport().zoom(env);
		}catch(NoninvertibleTransformException ex){
			logger.error("zoomFeature(Feature)", ex);
		}
    }
    private void automaticConnectBrute(TaskMonitor monitor, ActionEvent e)
    {
    resetAll();
    // Comprobamos primero que se cumplan las condiciones para realizar la union
    // automatica
    monitor.allowCancellationRequests();
    monitor.report(aplicacion.getI18nString("BuscandoElementosdestino"));

    Layer capaNumerosPolicia = getSourceLayer();
    Layer capaCallejero = getTargetLayer();

    if (capaNumerosPolicia == null)
    	{
    	JOptionPane
    	.showMessageDialog(
    			(Component) localContext.getWorkbenchGuiComponent(),
    			aplicacion
    			.getI18nString("ConnectPoliceStreetPlugIn.CapaPoliciasNoCargada"));
    	return;
    	}
    if (capaCallejero == null)
    	{
    	JOptionPane.showMessageDialog((Component) localContext
    			.getWorkbenchGuiComponent(), aplicacion
    			.getI18nString("ConnectPoliceStreetPlugIn.CapaViasNoCargada"));
    	return;
    	}

    ILayerViewPanel layerViewPanel = localContext.getLayerViewPanel();

    Layer errorLayer = null;
    Layer reportLayer = null;

    // ================================================0
    // Crea las capas de informes
    // =======================================
    reportLayer = localContext.getWorkbenchGuiComponent()
    .getActiveTaskComponent().getLayerViewPanel().getLayerManager()
    .getLayer(REPORTING_LAYER_NAME);

    if (reportLayer == null)
    	{

         initialReportDataset = new FeatureDataset(initialFeatureSchema);
    	reportLayer = localContext.addLayer(StandardCategoryNames.WORKING,
    			REPORTING_LAYER_NAME, initialReportDataset);
    	applyArrowStyles(reportLayer);
    	((GeopistaLayer) reportLayer).setActiva(false);
    	}
    else
    	{ // Si existen las capas de informe al usuario hay que vaciarlas
    	reportLayer.getFeatureCollectionWrapper().removeAll(
    			reportLayer.getFeatureCollectionWrapper().getFeatures());
    	}

    errorLayer = localContext.getLayerManager().getLayer(ERRORLAYER_NAME);
    if (errorLayer == null)
    	{
    	 initialErrorFeature = new FeatureDataset(initialFeatureSchema);
    	errorLayer = localContext.addLayer(StandardCategoryNames.WORKING,
    			ERRORLAYER_NAME, initialErrorFeature);
    	applyErrorStyles(errorLayer);
    	((GeopistaLayer) errorLayer).setActiva(false);
    	// errorLayer.getLabelStyle().setColor(colorError);
    	// errorLayer.getLabelStyle().setAttribute("messsage");
    	// errorLayer.getLabelStyle().setEnabled(true);
    	}
    else
    	{
    	errorLayer.getFeatureCollectionWrapper().removeAll(
    			errorLayer.getFeatureCollectionWrapper().getFeatures());
    	}

    // ==========================================

    boolean firing = localContext.getLayerManager().isFiringEvents();
    localContext.getLayerManager().setFiringEvents(false);

    try
    {
    FeatureCollection targetFeaturesCollection = capaCallejero
    .getFeatureCollectionWrapper().getUltimateWrappee();

    Collection callesCollection = capaCallejero
    .getFeatureCollectionWrapper().getFeatures();
    // Crea una collection para alojar las features sin clasificar
    FeatureCollection sinClasificar = new FeatureDataset(capaNumerosPolicia
    		.getFeatureCollectionWrapper().getFeatureSchema());
    sinClasificar.addAll(capaNumerosPolicia.getFeatureCollectionWrapper()
    		.getFeatures());
    // introduce todos los origenes en la capa de error
    errorLayer.getFeatureCollectionWrapper().addAll(
    		capaNumerosPolicia.getFeatureCollectionWrapper().getFeatures());

    Map targetToSourceLinkingMap = new HashMap();// enlace 1:n

    finalRelation = new TreeMap(); // borra anteriores resultados.
    putToBlackboard(FINAL_LINKING_ARRAY, finalRelation);
    // repasa las features destino para extraer los buffer y filtrar

    List allPoliceNumberFeatures = capaNumerosPolicia.getFeatureCollectionWrapper().getFeatures();
    int iteraciones = 0;
    int cont = 0;


    	cont = 0;

    	Iterator itTargetFeatures = targetFeaturesCollection.iterator();
    	GeopistaConnection geoConn = (GeopistaConnection)capaNumerosPolicia.getDataSourceQuery().getDataSource().getConnection();
		ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
    	String sridInicial = geoConn.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
    	//String sridDefecto = geoConn.getSRIDDefecto(true, Integer.parseInt(iSesion.getIdEntidad()));
    	String sridDefecto = geoConn.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
    	logger.info("Srid Inicial:"+sridInicial+"-Srid Defecto:"+sridDefecto);
		CoordinateSystem inCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridDefecto));
		CoordinateSystem outCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridInicial));
        GeopistaSchema schema = (GeopistaSchema) capaNumerosPolicia
        .getFeatureCollectionWrapper().getFeatureSchema();
    //    String attrMuni = schema.getAttributeByColumn("id_municipio");
	 	String sMunicipio = ((ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY)).getIdMunicipio();
		List listFeatures = new ArrayList();


		String attrMunicipio = schema.getAttributeByColumn("id_municipio");
    	while (itTargetFeatures.hasNext()){

    		Feature targetFeature = (Feature)itTargetFeatures.next();

    		FeatureSchema featureSchema = targetFeature.getSchema();
    		if (featureSchema instanceof GeopistaSchema){
    			GeopistaSchema geopistaSchema = (GeopistaSchema)featureSchema;
    			String attributeName = geopistaSchema.getAttributeByColumn("id_municipio");


    		// MOD --> La columna con el id de municipio no siempre se llama id_municipio
    		// Se busca cuál es su nombre
//    		try {
//				targetFeature.getAttribute(attrMuni);
//			} catch (Exception e1) {
//				attrMuni = "ID de municipio";
//				try{
//					targetFeature.getAttribute(attrMuni);
//				}catch (Exception e2){
//					attrMuni = "ID de Municipio";
//					}
//			}
    		if (sMunicipio.equals(targetFeature.getAttribute(attributeName).toString()))
   	 		{
				Feature currentFeatureTransform = (Feature) targetFeature.clone();
			    Geometry geom = currentFeatureTransform.getGeometry();
			    geom.setSRID(inCoord.getEPSGCode());
				if (inCoord.getEPSGCode() != outCoord.getEPSGCode()){
				    Reprojector.instance().reproject(geom,inCoord, outCoord);
				    geom.setSRID(outCoord.getEPSGCode());
/*				    if (!geom.isValid()){
				    	Feature currentFeatureTransform2 = (Feature) targetFeature.clone();
				    	Geometry geom2 = currentFeatureTransform2.getGeometry();
				    	geom2.setSRID(outCoord.getEPSGCode());
				    	Reprojector.instance().reproject(geom2,outCoord, inCoord);
				    	geom2.setSRID(inCoord.getEPSGCode());
				    	geom = geom2;
//				    }*/
				}
				currentFeatureTransform.setGeometry(geom);
				listFeatures.add(currentFeatureTransform);
   	 		}
    		}
    	}
    	Iterator policeFeaturesIter = allPoliceNumberFeatures.iterator();
    	while (policeFeaturesIter.hasNext()){
    		if (monitor.isCancelRequested()) return;
    		monitor
    		.report(
    				cont++,
    				allPoliceNumberFeatures.size(),
    				aplicacion
    				.getI18nString("Revisandoelementosdestinodelenlace"));
    		Feature sourceFeature = (Feature) policeFeaturesIter.next();

    		// zoomFeature(actualFeature);


    		// MOD --> La columna con el id de municipio no siempre se llama id_municipio
    		// Se busca cuál es su nombre
//    		try {
//				sourceFeature.getAttribute(attrMuni);
//			} catch (Exception e1) {
//				attrMuni = "ID de municipio";
//				try{
//					sourceFeature.getAttribute(attrMuni);
//				}catch (Exception e2){
//					attrMuni = "ID de Municipio";
//					}
//			}
    		attrMunicipio = schema.getAttributeByColumn("id_municipio");
    		FeatureSchema featureSchema = sourceFeature.getSchema();
    		if (featureSchema instanceof GeopistaSchema){
    			GeopistaSchema geopistaSchema = (GeopistaSchema)featureSchema;
    			String attributeName = geopistaSchema.getAttributeByColumn("id_municipio");


   	 		if (sMunicipio.equals(sourceFeature.getAttribute(attributeName).toString())){

	    		// Busca todas la feature del grupo destino más cercana
	    		ArrayList actualNearestFeature = nearestFeaturesBrute(sourceFeature, listFeatures);
	    		// Asociamos las features candidatas NumeroPolicia<->calles
	    		targetToSourceLinkingMap.put(sourceFeature, actualNearestFeature);
   	 		}
    		}
    	}

    	// Genera la representación visual de los enlaces y clasifica entre
    	// enlazados, ambiguos y sin asignar
    	policeFeaturesIter = allPoliceNumberFeatures.iterator();

    	GeometryFactory geometryFactory = new GeometryFactory();
    	FeatureSchema featureSchema = new FeatureSchema();
    	featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
    	FeatureCollection arrows = new FeatureDataset(featureSchema);

    	cont = 0;
    	while (policeFeaturesIter.hasNext()) // Revisa las features
    		// origen
    	{

    		GeopistaFeature sourceFeature = (GeopistaFeature) policeFeaturesIter
    		.next();
    		FeatureSchema featSchema = sourceFeature.getSchema();
    		if (featSchema instanceof GeopistaSchema){
    			GeopistaSchema geopistaSchema = (GeopistaSchema)featSchema;
    			String attributeName = geopistaSchema.getAttributeByColumn("id_municipio");

	   	 	if (sMunicipio.equals(sourceFeature.getAttribute(attributeName).toString())){
	    		// Obtiene las features target candidatas
	    		ArrayList targetCandidateFeatures = (ArrayList) targetToSourceLinkingMap
	    		.get(sourceFeature);

	    		// zoomFeature();

	    		monitor
	    		.report(
	    				cont++,
	    				allPoliceNumberFeatures.size(),
	    				aplicacion
	    				.getI18nString("Etiquetandolosenlacesrealizadosyloserrores"));

	    		Iterator targetCandidateIter = targetCandidateFeatures.iterator();
	    		ArrayList arrowArray = new ArrayList(); // Lista de geometrías
	    		// para etiquetar
	    		ArrayList matchedArray = new ArrayList(); // Lista de features
	    		// que se han
	    		// emparejado para
	    		// esta
	    		// SourceFeature
	    		GeopistaFeature candidateTargetFeature=null;
	    		while (targetCandidateIter.hasNext()) // Itera calles
	    			// candidatas para esta SourceFeature
	    			{
	    			candidateTargetFeature=(GeopistaFeature) targetCandidateIter
	    			.next();
	    			if (monitor.isCancelRequested()) return;
	    			// TODO: Si el algoritmo devolviera más de una habría que manejar la ambigüedad en este paso
	    			// Actualmente el algoritmo devuelve solo UNA targetFeature
	    			sinClasificar.remove(sourceFeature, true);
	    			finalRelation.put(sourceFeature, candidateTargetFeature);
	    			matchedArray.add(candidateTargetFeature);
	    			}

	    			Feature targetFeature = (Feature) finalRelation
	    			.get(sourceFeature);
	    			// aún no existe en los enlaces finales por lo que se
	    			// incluye y se genera la pista visual.
	    			Coordinate[] coordenadas = new Coordinate[2];
	    			coordenadas[0] = sourceFeature.getGeometry()
	    			.getCoordinate();
	    			coordenadas[1] = geometryMidPoint(targetFeature
	    					.getGeometry(), sourceFeature
	    					.getGeometry());
	    			LineString arrow = geometryFactory
	    			.createLineString(coordenadas);
	    			// Crea la Feature de la flecha
	    			Feature featureArrow = new BasicFeature(arrows
	    					.getFeatureSchema());
	    			featureArrow.setGeometry(arrow);

	    			arrows.add(featureArrow);
	    			arrowArray.add(featureArrow);

	    			// Hastable que contiene las relaciones finales, cada
	    			// sourceFeature con la targetFeature que
	    			// le corresponde
	    			finalRelation.put(sourceFeature, targetFeature);
	    			arrowFeatureRelation.put(sourceFeature, featureArrow);


	    		// arrowFeatureRelation.put(candidateSourceFeature,arrowArray);
	    		reportLayer.getFeatureCollectionWrapper().addAll(arrowArray);
	    		errorLayer.getFeatureCollectionWrapper()
	    		.removeAll(matchedArray); // se eliminan de la capa de
	    		// errores
	    		// Se eliminan de la capa sin clasificar ya que estan enlazados
	    		sinClasificar.removeAll(matchedArray);
	    	}
    	}}
    }catch(Exception e1){
    	e1.printStackTrace();
    }
    finally
    {
    localContext.getLayerManager().setFiringEvents(firing);
    }

    }
    private void automaticConnect(TaskMonitor monitor, ActionEvent e)

    {

      // Comprobamos primero que se cumplan las condiciones para realizar la
		// union
      // automatica
    	monitor.allowCancellationRequests();
    	monitor.report(aplicacion.getI18nString("BuscandoElementosdestino"));

      Layer capaNumerosPolicia = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);//cbSourceLayer.getSelectedItem();//localContext.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);
      Layer capaCallejero = (Layer) getFromBlackboard(SELECTEDTARGETLAYER);//cbTargetLayer.getSelectedItem();//localContext.getLayerManager().getLayer(GeopistaSystemLayers.VIAS);

      if(capaNumerosPolicia==null)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.CapaPoliciasNoCargada"));
        return;
      }
      if(capaCallejero==null)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.CapaViasNoCargada"));
        return;
      }

      ILayerViewPanel layerViewPanel = localContext.getLayerViewPanel();


      Layer errorLayer = null;
      Layer reportLayer = null;

      /**
       * Crea la capa para las operaciones de buffer
       */
      bufferLayer = localContext.getLayerManager().getLayer(BUFFERLAYER_NAME);
      if(bufferLayer==null)
      {
       bufferLayer = localContext.addLayer(StandardCategoryNames.WORKING, BUFFERLAYER_NAME , initialFeature);
       bufferLayer.getBasicStyle().setAlpha(100);
       bufferLayer.getBasicStyle().setLinePattern("5,5");
       ((GeopistaLayer) bufferLayer).setActiva(false);
      }
      else
       bufferLayer.getFeatureCollectionWrapper().removeAll(bufferLayer.getFeatureCollectionWrapper().getFeatures());
//================================================0
//    Crea las capas de informes
//=======================================
      reportLayer = localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel()
		.getLayerManager().getLayer(REPORTING_LAYER_NAME);

      if (reportLayer == null)
      {
      	reportLayer = localContext.addLayer(StandardCategoryNames.WORKING,
      			REPORTING_LAYER_NAME, initialReportDataset);
      	applyArrowStyles(reportLayer);
      	((GeopistaLayer) reportLayer).setActiva(false);
      }
      else
      { // Si existen las capas de informe al usuario hay que vaciarlas
      	reportLayer.getFeatureCollectionWrapper().removeAll(
      			reportLayer.getFeatureCollectionWrapper().getFeatures());
      }

      errorLayer = localContext.getLayerManager().getLayer(ERRORLAYER_NAME);
      if (errorLayer == null)
      {
      	errorLayer = localContext.addLayer(StandardCategoryNames.WORKING,
      			ERRORLAYER_NAME, initialErrorFeature);
      	applyErrorStyles(errorLayer);
      	((GeopistaLayer) errorLayer).setActiva(false);
      	//             errorLayer.getLabelStyle().setColor(colorError);
      	//             errorLayer.getLabelStyle().setAttribute("messsage");
      	//             errorLayer.getLabelStyle().setEnabled(true);
      }
      else
      {
      	errorLayer.getFeatureCollectionWrapper().removeAll(
      			errorLayer.getFeatureCollectionWrapper().getFeatures());
      }

         //==========================================

boolean firing=   localContext.getLayerManager().isFiringEvents();
localContext.getLayerManager().setFiringEvents(false);

        FeatureCollection featuresSourceCollection = capaNumerosPolicia.getFeatureCollectionWrapper().getUltimateWrappee();

        Collection featuresTarget = capaCallejero.getFeatureCollectionWrapper().getFeatures();
// Crea una collection para alojar las features sin clasificar
        FeatureCollection sinClasificar = new FeatureDataset(featuresSourceCollection.getFeatureSchema());
        sinClasificar.addAll(featuresSourceCollection.getFeatures());
  //introduce todos los origenes en la capa de error
        errorLayer.getFeatureCollectionWrapper().addAll(capaNumerosPolicia.getFeatureCollectionWrapper().getFeatures());

       Map targetToSourceLinkingMap = new HashMap();// enlace 1:n

      finalRelation=new TreeMap(); // borra anteriores resultados.
       putToBlackboard(FINAL_LINKING_ARRAY,finalRelation);
       // repasa las features destino para extraer los buffer y filtrar

       List allPoliceNumberFeatures = capaNumerosPolicia.getFeatureCollectionWrapper().getFeatures();
       int iteraciones=0;
       int cont=0;
       while (sinClasificar.size()>0 && iteraciones++<2)
       {
       	cont=0;
        Iterator targetFeaturesIter = featuresTarget.iterator();
        while(targetFeaturesIter.hasNext())
        {
        	if (monitor.isCancelRequested())return;
        	monitor.report(cont++,featuresTarget.size(),aplicacion.getI18nString("Revisandoelementosdestinodelenlace"));
        	Feature actualFeature = (Feature) targetFeaturesIter.next();

        		zoomFeature(actualFeature);


			// Busca todas las sourceFeatures que cortan a una distancia de buffer primera iteración
			ArrayList actualNearestFeature = nearestFeatures(actualFeature, sinClasificar);

			//Asociamos las features candidatas
			targetToSourceLinkingMap.put(actualFeature,actualNearestFeature);

        }

        // Genera la representación visual de los enlaces y clasifica entre
        // enlazados, ambiguos y sin asignar
        targetFeaturesIter = featuresTarget.iterator();


        GeometryFactory geometryFactory = new GeometryFactory();
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        FeatureCollection arrows = new FeatureDataset(featureSchema);


        cont=0;
        while(targetFeaturesIter.hasNext()) //Revisa las features destino
        {
          GeopistaFeature targetFeature = (GeopistaFeature) targetFeaturesIter.next();
          //Obtiene las features source candidatas
          ArrayList candidateFeatures = (ArrayList) targetToSourceLinkingMap.get(targetFeature);

          zoomFeature(targetFeature);

          monitor.report(cont++,featuresTarget.size(),aplicacion.getI18nString("Etiquetandolosenlacesrealizadosyloserrores"));

          Iterator candidateSourcesIter = candidateFeatures.iterator();
          ArrayList arrowArray = new ArrayList(); // Lista de geometrías para etiquetar
          ArrayList matchedArray= new ArrayList(); // Lista de features que se han emparejado para esta TargetFeature

          while(candidateSourcesIter.hasNext())
          {

            GeopistaFeature candidateSourceFeature = (GeopistaFeature) candidateSourcesIter.next();
            if (monitor.isCancelRequested())return;
            if(finalRelation.containsKey(candidateSourceFeature))
            {
            // si ya existe la feature en los enlaces finales se añade a los errores.
            // se clasifica como una  ambigüedad
              errorLayer.getFeatureCollectionWrapper().add(candidateSourceFeature,true);
              // se quita de la capa sinClasificar porque ya está clasificado como ambigüedad
              sinClasificar.remove(candidateSourceFeature,true);

              // evalúa las distancias de ambas asignaciones y mantiene la menor
              Feature targetLinked = (Feature)finalRelation.get(candidateSourceFeature);
              double distance1 = targetLinked.getGeometry().distance(candidateSourceFeature.getGeometry());
              double distance2 = targetFeature.getGeometry().distance(candidateSourceFeature.getGeometry());
              if (distance2<distance1)// esta es mejor hipótesis
              {
              	// elimina la flecha de la anterior pareja
              	Feature arrow=(Feature)arrowFeatureRelation.get(candidateSourceFeature);
              	arrowFeatureRelation.remove(candidateSourceFeature);
              	arrows.remove(arrow);
              	arrowArray.remove(arrow);
              	reportLayer.getFeatureCollectionWrapper().remove(arrow);
              	matchedArray.remove(candidateSourceFeature); // no se elimina para que se mantenga en el informe de errores
              }
              else
              	continue;
            }
           else
           	matchedArray.add(candidateSourceFeature); // en caso de que se haya cambiado la hipótesis no se elimina de los errores

         // aún no existe en los enlaces finales por lo que se incluye y se genera la pista visual.
              Coordinate[] coordenadas = new Coordinate[2];
              coordenadas[0] = candidateSourceFeature.getGeometry().getCoordinate();
              coordenadas[1] = geometryMidPoint(targetFeature.getGeometry(),candidateSourceFeature.getGeometry());
              LineString arrow = geometryFactory.createLineString(coordenadas);
             // Crea la Feature de la flecha
              Feature featureArrow = new BasicFeature(arrows.getFeatureSchema());
                  featureArrow.setGeometry( arrow);

           arrows.add(featureArrow);
           arrowArray.add(featureArrow);


              //Hastable que contiene las relaciones finales, cada sourceFeature con la targetFeature que
              //le corresponde
              finalRelation.put(candidateSourceFeature,targetFeature);
              arrowFeatureRelation.put(candidateSourceFeature,featureArrow);

          }
       //arrowFeatureRelation.put(candidateSourceFeature,arrowArray);
          reportLayer.getFeatureCollectionWrapper().addAll(arrowArray);
          errorLayer.getFeatureCollectionWrapper().removeAll(matchedArray); // se eliminan de la capa de errores
          // Se eliminan de la capa sin clasificar ya que estan enlazados
          sinClasificar.removeAll(matchedArray);
        }
          }
   localContext.getLayerManager().setFiringEvents(firing);
    }
	private void automaticConnectOld(TaskMonitor monitor, ActionEvent e)
	{


	      //Comprobamos primero que se cumplan las condiciones para realizar la union
	      //automatica
	    	monitor.allowCancellationRequests();
	    	monitor.report(aplicacion.getI18nString("BuscandoElementosdestino"));

	      Layer capaNumerosPolicia = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);//cbSourceLayer.getSelectedItem();//localContext.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);
	      Layer capaCallejero = (Layer) getFromBlackboard(SELECTEDTARGETLAYER);//cbTargetLayer.getSelectedItem();//localContext.getLayerManager().getLayer(GeopistaSystemLayers.VIAS);

	      if(capaNumerosPolicia==null)
	      {
	        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.CapaPoliciasNoCargada"));
	        return;
	      }
	      if(capaCallejero==null)
	      {
	        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.CapaViasNoCargada"));
	        return;
	      }

	      ILayerViewPanel layerViewPanel = localContext.getLayerViewPanel();


	      Layer errorLayer = null;
	      Layer streetLayer = null;

	      bufferLayer = localContext.getLayerManager().getLayer(BUFFERLAYER_NAME);
	      if(bufferLayer==null)
	      {
	       bufferLayer = localContext.addLayer(StandardCategoryNames.WORKING, BUFFERLAYER_NAME , initialFeature);
	       bufferLayer.getBasicStyle().setAlpha(100);
	       bufferLayer.getBasicStyle().setLinePattern("5,5");
	       ((GeopistaLayer) bufferLayer).setActiva(false);
	      }
	      else
	       bufferLayer.getFeatureCollectionWrapper().removeAll(bufferLayer.getFeatureCollectionWrapper().getFeatures());


	        FeatureCollection featuresCollectionNumerosPolicia = capaNumerosPolicia.getFeatureCollectionWrapper().getUltimateWrappee();
	        Collection featuresCallejero = capaCallejero.getFeatureCollectionWrapper().getFeatures();
	       int cont=0;

	        Map targetToSourceLinkingMap = new HashMap();
	       finalRelation=new TreeMap(); // borra anteriores resultados.
	        Iterator targetFeaturesIter = featuresCallejero.iterator();
	        while(targetFeaturesIter.hasNext())
	        {
	        	 if (monitor.isCancelRequested())return;
	       	 monitor.report(cont++,featuresCallejero.size(),aplicacion.getI18nString("Revisandoelementosdestinodelenlace"));

	          Feature actualFeature = (Feature) targetFeaturesIter.next();
	      zoomFeature(actualFeature);

	          ArrayList actualNearestFeature = nearestFeatures(actualFeature, featuresCollectionNumerosPolicia);

	          //Asociamos la calle con sus numeros de policia
	          targetToSourceLinkingMap.put(actualFeature,actualNearestFeature);

	        }
//	 Genera la representación visual de los enlaces
	        targetFeaturesIter = featuresCallejero.iterator();
	        GeometryFactory geometryFactory = new GeometryFactory();
	        FeatureSchema featureSchema = new FeatureSchema();
	        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
	        FeatureCollection arrows = new FeatureDataset(featureSchema);

	        streetLayer = localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getLayerManager().getLayer(REPORTING_LAYER_NAME);

	        if(streetLayer==null)
	        {
	          streetLayer = localContext.addLayer(StandardCategoryNames.WORKING,REPORTING_LAYER_NAME,initialReportDataset);
	          applyArrowStyles(streetLayer);
	          ((GeopistaLayer) streetLayer).setActiva(false);
	        }
	        else
	        { // Si existen las capas de informe al usuario hay que vaciarlas
	          streetLayer.getFeatureCollectionWrapper().removeAll(streetLayer.getFeatureCollectionWrapper().getFeatures());
	        }

	           errorLayer = localContext.getLayerManager().getLayer(ERRORLAYER_NAME);
	           if(errorLayer==null)
	           {
	             errorLayer = localContext.addLayer(StandardCategoryNames.WORKING,ERRORLAYER_NAME,initialErrorFeature);
	            applyErrorStyles(errorLayer);
	             ((GeopistaLayer) errorLayer).setActiva(false);
//	             errorLayer.getLabelStyle().setColor(colorError);
//	             errorLayer.getLabelStyle().setAttribute("messsage");
//	             errorLayer.getLabelStyle().setEnabled(true);
	           }
	           else
	           {
	            errorLayer.getFeatureCollectionWrapper().removeAll(errorLayer.getFeatureCollectionWrapper().getFeatures());
	           }
	           List allPoliceNumberFeatures = capaNumerosPolicia.getFeatureCollectionWrapper().getFeatures();//localContext.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA).getFeatureCollectionWrapper().getFeatures();
	           //introduce todos los origenes en la capa de error
	           errorLayer.getFeatureCollectionWrapper().addAll(allPoliceNumberFeatures);
	           cont=0;
	        while(targetFeaturesIter.hasNext()) //Revisa las features destino
	        {
	          GeopistaFeature targetFeature = (GeopistaFeature) targetFeaturesIter.next();
	          ArrayList sourceFeatures = (ArrayList) targetToSourceLinkingMap.get(targetFeature);


	          zoomFeature(targetFeature);

	          Iterator sourcesIter = sourceFeatures.iterator();
	          ArrayList arrowArray = new ArrayList(); // Lista de geometrías para etiquetar
	          ArrayList matchedArray= new ArrayList(); // Lista de features emparejados

	          while(sourcesIter.hasNext())
	          {

	            GeopistaFeature matchedSourceFeature = (GeopistaFeature) sourcesIter.next();
	            monitor.report(cont++,featuresCollectionNumerosPolicia.size(),aplicacion.getI18nString("Etiquetandolosenlacesrealizadosyloserrores"));
	        	 if (monitor.isCancelRequested())return;
	            if(finalRelation.containsKey(matchedSourceFeature))
	            {// si ya existe la feature en los enlaces finales se añade a los errores.
	            	// Se supone que es una duplicidad
	              errorLayer.getFeatureCollectionWrapper().add(matchedSourceFeature,true);
	            }
	            else
	            {
	         // aún no existe en los enlaces finales por lo que se incluye y se genera la pista visual.

	              Coordinate[] coordenadas = new Coordinate[2];
	              coordenadas[0] = matchedSourceFeature.getGeometry().getCoordinate();
	              coordenadas[1] = geometryMidPoint(targetFeature.getGeometry(),matchedSourceFeature.getGeometry());
	              LineString arrow = geometryFactory.createLineString(coordenadas);
	             // Crea una Feature

	              Feature featureArrow = new BasicFeature(arrows.getFeatureSchema());
	                  featureArrow.setGeometry( arrow);
	                  arrows.add(featureArrow);

	           arrowArray.add(featureArrow);
	           matchedArray.add(matchedSourceFeature);

	              //Hastable que contiene las relaciones finales, cada numero de polica con la calle que
	              //le corresponde
	              finalRelation.put(matchedSourceFeature,targetFeature);
	              arrowFeatureRelation.put(matchedSourceFeature,featureArrow);
	            }
	          }

	          streetLayer.getFeatureCollectionWrapper().addAll(arrowArray);
	          errorLayer.getFeatureCollectionWrapper().removeAll(matchedArray); // se eliminan de la capa de errores

	        }

	}
    private void correctionConnect(ActionEvent e)
    {
    	
    	
	    /*TreeMap    finalRelation=(TreeMap) getFromBlackboard(FINAL_LINKING_ARRAY);
	    if (finalRelation==null)
	    {
	    	finalRelation=new TreeMap();
	    	putToBlackboard(FINAL_LINKING_ARRAY,finalRelation);
	    }*/
	
    	 finalRelation = new TreeMap(); // borra anteriores resultados.
    	 putToBlackboard(FINAL_LINKING_ARRAY, finalRelation);
	    
      Layer targetLayer = (Layer)getTargetLayer();

      Layer layerSource = (Layer) getSourceLayer();

      Layer errorLayer =  localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().getLayer(ERRORLAYER_NAME);
      Layer reportLayer = localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().getLayer(REPORTING_LAYER_NAME);

      if(layerSource==null)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.CapaPoliciasNoCargada"));
        return;
      }
      if(targetLayer==null)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.CapaViasNoCargada"));
        return;
      }



      Collection selectedStreets = localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(targetLayer);

      if(selectedStreets.size()==0||selectedStreets.size()>1)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("DistanceLinkingPlugIn.NumeroTargetsErroneo"));
        return;
      }
      Collection selectedSource = localContext.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(layerSource);
      if(selectedSource.size()==0)
      		selectedSource=localContext.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(errorLayer);

      if(selectedSource.size()==0)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("DistanceLinkingPlugIn.NumeroSourcesErroneo"));
        return;
      }



      if(reportLayer==null)
      {
        reportLayer = localContext.addLayer(StandardCategoryNames.WORKING,REPORTING_LAYER_NAME,initialReportDataset);
        applyArrowStyles(reportLayer);
        ((GeopistaLayer) reportLayer).setActiva(false);
      }


      if(errorLayer==null)
      {
       errorLayer = localContext.addLayer(StandardCategoryNames.WORKING,ERRORLAYER_NAME,initialErrorFeature);
      applyErrorStyles(errorLayer);
       ((GeopistaLayer) errorLayer).setActiva(false);
      }

      GeopistaFeature featureStreet = (GeopistaFeature) selectedStreets.iterator().next();

      Iterator featuresSourceIter = selectedSource.iterator();




      while(featuresSourceIter.hasNext())
      {
          GeopistaFeature actualFeaturesSource = (GeopistaFeature) featuresSourceIter.next();
          Coordinate[] coordenadas = new Coordinate[2];
          coordenadas[1] = geometryMidPoint(featureStreet.getGeometry(),actualFeaturesSource.getGeometry());
          GeometryFactory geometryFactory = new GeometryFactory();
          coordenadas[0] = actualFeaturesSource.getGeometry().getCoordinate();
          LineString arrow = geometryFactory.createLineString(coordenadas);
          ArrayList arrowArray = new ArrayList();
          arrowArray.add(arrow);

          FeatureCollection arrowCollection = FeatureDatasetFactory.createFromGeometry(arrowArray);

          GeopistaFeature featureArrow = (GeopistaFeature) arrowCollection.getFeatures().get(0);
          deleteArrow(actualFeaturesSource);
          arrowFeatureRelation.put(actualFeaturesSource,featureArrow);


          reportLayer.getFeatureCollectionWrapper().addAll(arrowCollection.getFeatures());
          errorLayer.getFeatureCollectionWrapper().remove(actualFeaturesSource);
          
          finalRelation.put(actualFeaturesSource, featureStreet);
      }



    }

    public void deleteArrow(GeopistaFeature deleteFeature)
    {
      GeopistaFeature arrowFeature = (GeopistaFeature) arrowFeatureRelation.get(deleteFeature);
      if(arrowFeature!=null)
      {
        localContext.getLayerManager().getLayer(this.REPORTING_LAYER_NAME).getFeatureCollectionWrapper().remove(arrowFeature);
      }
    }

    private Coordinate geometryMidPoint(Geometry workGeometry, Geometry policePoint)
    {
      double geometriesDistance = policePoint.distance(workGeometry);
      Geometry bufferedPoliceLocation = policePoint.buffer(geometriesDistance+1);

      Geometry intersection = workGeometry.intersection(bufferedPoliceLocation);

      if (intersection.isEmpty()) // a veces ciertas configuraciones de puntos y lineas dan lugar a una intersección nula.
    	  {
    	  intersection=workGeometry;
    	  }
      InteriorPointFinder interiorPointFinder = new InteriorPointFinder();


      return interiorPointFinder.findPoint(intersection);


    }

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnAutoLink() {
		if (btnAutoLink == null) {
			btnAutoLink = new JButton();
			btnAutoLink.setName("automaticButton");
			btnAutoLink.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Computer.gif")));
			btnAutoLink.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			btnAutoLink.setToolTipText(aplicacion.getI18nString("AutomaticLinking"));
			btnAutoLink.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					runTask(e);
				}
			});

		}
		return btnAutoLink;
	}
	/**
	 * This method initializes jButton1
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnManualLink() {
		if (btnManualLink == null) {
			btnManualLink = new JButton();
			btnManualLink.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Draw.gif")));
			btnManualLink.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			btnManualLink.setToolTipText(aplicacion.getI18nString("ManualLinking"));
			btnManualLink.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					correctionConnect(e);
				}
			});
		}
		return btnManualLink;
	}
	/**
	 * This method initializes jButton2
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnReset() {
		if (btnReset == null) {
			btnReset = new JButton();
			btnReset.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Undo.gif")));
			btnReset.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			btnReset.setToolTipText(aplicacion.getI18nString("ResetLinking"));
			btnReset.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					resetAll(); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return btnReset;
	}

	/**
	 * This method initializes jButton3
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnSetField() {
		if (btnSetField == null) {
			btnSetField = new JButton();
			btnSetField.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/SmallDown.gif")));
			btnSetField.setEnabled(false);
			btnSetField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			btnSetField.setToolTipText(aplicacion.getI18nString("TransferLinkingData"));
			btnSetField.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setFieldsTask(e);
				}
			});
		}
		return btnSetField;
	}

	private void setFieldsTask(final ActionEvent e)
	{
		new TaskMonitorManager().execute(new ThreadedBasePlugIn() {
            public boolean execute(PlugInContext context) throws Exception {
                return true;
            }
            public void run(TaskMonitor monitor, PlugInContext context)
                throws Exception {

                setFields( monitor, e);
                JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                        .getI18nString("Inforeferencia.GuardarCambios4"));        		

            }
        }, localContext);
	}

	/**
	 * This method initializes jComboBox1
	 *
	 * @return javax.swing.JComboBox
	 */
	private JList getLstSourceField() {
		if (lstSourceField == null) {
			lstSourceField = new JList();
			lstSourceField.setName("sourceField");

			lstSourceField.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstSourceField.setVisibleRowCount(3);
			lstSourceField.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount()==2)

							featureExpressionPanel.pasteText("source_"+((JList)e.getSource()).getSelectedValue().toString().replace(' ','_'));

				}
			});
			lstSourceField.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e)
				{
					String attName=(String) ((JList)e.getSource()).getSelectedValue();
					putToBlackboard(ATTRIBUTE_ID_ON_SOURCE, attName);
					getFeatureExpressionPanel().setLabelText(attName+"=");
					btnSetField.setEnabled(attName!=null);
				}


			});

		}


		return lstSourceField;
	}
	/**
	 * This method initializes jComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCbSourceLayer() {
		if (cbSourceLayer == null) {
			cbSourceLayer = new JComboBox();
			cbSourceLayer.setName("sourceLayerCombo");
			cbSourceLayer.setPreferredSize(new java.awt.Dimension(160,21));
			cbSourceLayer.addActionListener(
					new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//if (((JComboBox)e.getSource()).getSelectedItem()!=null)
					putToBlackboard(SELECTEDSOURCELAYER,((JComboBox)e.getSource()).getSelectedItem());
					updateFieldList(lstSourceField,((JComboBox)e.getSource()).getSelectedItem(),getFromBlackboard(ATTRIBUTE_ID_ON_SOURCE));
					//resetAll();
				}
			});

		}

		return cbSourceLayer;
	}
	private Object getFromBlackboard(String key)
	{
		try
		{
		ILayerViewPanel lyrView = localContext.getWorkbenchGuiComponent()
				.getActiveTaskComponent().getLayerViewPanel();
		Blackboard bk = lyrView.getBlackboard();
		return bk.get(key);
		} catch (NullPointerException e)
		{// ocurre cuando la ventana activa no es de GIS
		return null;
		}

	}
	private void putToBlackboard(String key, Object value)
	{
		try
		{
		Blackboard bk = localContext.getWorkbenchGuiComponent()
				.getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		bk.put(key, value);
		} catch (NullPointerException e)
		{// ocurre cuando la ventana activa no es de GIS
		return;
		}


	}
	/**
	 * This method initializes jComboBox3
	 *
	 * @return javax.swing.JComboBox


	 */
	private JList getLstTargetField() {
		if (lstTargetField == null) {
			lstTargetField = new JList();


			lstTargetField.setVisibleRowCount(3);

			lstTargetField.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstTargetField.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount()==2)
						{
						featureExpressionPanel.pasteText(((String)((JList)e.getSource()).getSelectedValue()).replace(' ','_'));
						featureExpressionPanel.requestFocus();
						}

				}
			});
			lstTargetField.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e)
				{
					String attName=(String) ((JList)e.getSource()).getSelectedValue();
					putToBlackboard(ATTRIBUTE_ID_ON_TARGET, attName);
					btnSetField.setEnabled(attName!=null);
				}
			});
		}
		return lstTargetField;
	}
	/**
	 * This method initializes jComboBox2
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCbTargetLayer() {
		if (cbTargetLayer == null) {
			cbTargetLayer = new JComboBox();
			cbTargetLayer.setPreferredSize(new java.awt.Dimension(160,21));
		}
		cbTargetLayer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				//if (((JComboBox)e.getSource()).getSelectedItem()!=null)
				Layer targetLayer=(Layer) ((JComboBox)e.getSource()).getSelectedItem();
				putToBlackboard(SELECTEDTARGETLAYER,targetLayer);

				updateFieldList(lstTargetField,targetLayer,getFromBlackboard(ATTRIBUTE_ID_ON_TARGET));
				if (targetLayer!=null)
				featureExpressionPanel.setFeatures(targetLayer.getFeatureCollectionWrapper().getFeatures());
				//resetAll();
			}
		});
		return cbTargetLayer;
	}
private Layer getSourceLayer()
{
	return (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
}
private Layer getTargetLayer()
{
	 return (Layer) getFromBlackboard(SELECTEDTARGETLAYER);
}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		//this.setTitle("EnlacePorCercanía");

		java.awt.GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints271 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		sourceLabel = new JLabel();
		java.awt.GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		lblTargetLayer = new JLabel();


		//sourceLabel.setText(aplicacion.getI18nString("SourceLayer"));
		//lblTargetLayer.setText(aplicacion.getI18nString("TargetLayer"));
		sourceLabel.setText(aplicacion.getI18nString("TargetLayer"));
		lblTargetLayer.setText(aplicacion.getI18nString("SourceLayer"));

		gridBagConstraints27.gridx = 0;
		gridBagConstraints27.gridy = 1;



		this.setLayout(new GridBagLayout());
		gridBagConstraints17.weightx = 0.5D;
		gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints17.insets = new java.awt.Insets(0,5,0,5);
		gridBagConstraints17.gridx = 0;
		gridBagConstraints17.gridy = 1;
		gridBagConstraints17.gridwidth = 1;
		gridBagConstraints18.insets = new java.awt.Insets(0,0,0,0);
		gridBagConstraints18.gridx = 1;
		gridBagConstraints18.gridy = 0;
		gridBagConstraints18.anchor = java.awt.GridBagConstraints.CENTER;
		gridBagConstraints23.gridx = 8;
		gridBagConstraints23.gridy = 0;
		gridBagConstraints23.insets = new java.awt.Insets(12,3,9,2);
		gridBagConstraints271.weightx = 0.5D;
		gridBagConstraints271.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints271.insets = new java.awt.Insets(0,5,0,5);
		gridBagConstraints271.gridx = 1;
		gridBagConstraints271.gridy = 1;
		gridBagConstraints271.gridwidth = 1;
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridy = 7;
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 2;
		gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints4.gridheight = 2;
		gridBagConstraints4.gridwidth = 1;
		gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints6.gridx = 1;
		gridBagConstraints6.gridy = 2;
		gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints6.gridheight = 3;
		gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints6.gridwidth = 1;
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.gridy = 0;
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.gridy = 7;
		gridBagConstraints5.gridwidth = 2;
		gridBagConstraints5.anchor = java.awt.GridBagConstraints.SOUTHWEST;
		gridBagConstraints42.gridx = 0;
		gridBagConstraints42.gridy = 6;
		gridBagConstraints42.gridwidth = 2;
		gridBagConstraints42.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.add(getSourceFieldScrollPane(), gridBagConstraints4);
		this.add(getTargetFieldScrollPane(), gridBagConstraints6);
		this.add(getCbTargetLayer(), gridBagConstraints271);
		this.add(getCbSourceLayer(), gridBagConstraints17);
		this.add(sourceLabel, gridBagConstraints41);

		getFeatureExpressionPanel().setLabelText("");
		this.add(getFeatureExpressionPanel(), gridBagConstraints42);
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(0,5,0,5);
			this.add(getJPanel(), gridBagConstraints5);
			this.add(lblTargetLayer, gridBagConstraints18);
	}

	private ArrayList nearestFeaturesBrute(Feature sourceFeature, List newWorkFeatures)
	{
		Geometry sourceFeatureGeometry = sourceFeature.getGeometry();

		ArrayList nearStreetNumbers = new ArrayList();
		// obtiene la lista de todas las features
		Feature candidate=null;
		boolean onlyclosest=true;
		double distance=Double.MAX_VALUE;
		Iterator workFeaturesIter = newWorkFeatures.iterator();
		try{
			while(workFeaturesIter.hasNext())
			{
				Feature currentFeature = (Feature) workFeaturesIter.next();
				double currDist= sourceFeatureGeometry.distance(currentFeature.getGeometry());
				if (onlyclosest && currDist<distance) // select only the closest
				{
					candidate=currentFeature;
					distance=currDist;
				}
				if (!onlyclosest && currDist<DistanceLinkingPanel.BUFFERDISTANCEINCREMENT) //select with threshold
				{
					nearStreetNumbers.add(candidate);
				}

			}
			if (onlyclosest)
				nearStreetNumbers.add(candidate);
				//ponemos la feature Buffer en la capa

		}catch(Exception e1){
	    	e1.printStackTrace();
	    }
		return nearStreetNumbers;
	}

    private ArrayList nearestFeatures(Feature sourceFeature, FeatureCollection collectionWorkFeatures)
    {

      Geometry geomSourceFeature = sourceFeature.getGeometry();



      int contador=1;
      boolean foundFeature=false;
      Geometry bufferGeometry=null;
      ArrayList nearStreetNumbers = new ArrayList();

      while(!foundFeature && contador<3)
      {
        bufferGeometry = geomSourceFeature.buffer(BUFFERDISTANCEINCREMENT*Math.pow(2,contador));
        List newWorkFeatures = collectionWorkFeatures.query(bufferGeometry.getEnvelopeInternal());

        Iterator workFeaturesIter = newWorkFeatures.iterator();
        while(workFeaturesIter.hasNext())
        {
          Feature actualFeature = (Feature) workFeaturesIter.next();
          Geometry actualGeometry = actualFeature.getGeometry();
          boolean overlapResult = bufferGeometry.intersects(actualGeometry);
          if(overlapResult)
            nearStreetNumbers.add(actualFeature);

        }

        if(nearStreetNumbers.isEmpty())
        {
          contador++;
        }
        else
        {
          foundFeature=true;
        }
      }



      //ponemos la feature Buffer en la capa
      Collection bufferGeometryArrayList = new ArrayList();
      bufferGeometryArrayList.add(bufferGeometry);
      bufferLayer.getFeatureCollectionWrapper().addAll(FeatureDatasetFactory.createFromGeometry(bufferGeometryArrayList).getFeatures());

      return nearStreetNumbers;
    }

	/**
	 *
	 */
	protected void resetAll()
	{
		Layer errorLayer=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().getLayer(ERRORLAYER_NAME);
		if (errorLayer!=null)
			{localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().remove(errorLayer);
		errorLayer.dispose();}
		Layer reportLayer=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().getLayer(REPORTING_LAYER_NAME);
		if (reportLayer!=null)
			{localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().remove(reportLayer);
		reportLayer.dispose();}
		Layer bufferLayer=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().getLayer(BUFFERLAYER_NAME);
		if (bufferLayer!=null)
			{localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().remove(bufferLayer);
		bufferLayer.dispose();}
		putToBlackboard(FINAL_LINKING_ARRAY,null);
	}
	/**
	 * Ejecuta el algoritmo en un hilo para evitar la sensación de bloqueo
	 *
	 */
	private void runTask(final ActionEvent e)
	{
		new TaskMonitorManager().execute(new ThreadedBasePlugIn() {
            public boolean execute(PlugInContext context) throws Exception {
                return true;
            }
            public void run(TaskMonitor monitor, PlugInContext context)
                throws Exception {

                automaticConnectBrute( monitor, e);
                context.getLayerViewPanel().getViewport().zoomToFullExtent();
                JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                        .getI18nString("Inforeferencia.GuardarCambios3"));        		

            }
        }, localContext.getWorkbenchContext().createPlugInContext());
	}
	// TODO: Implementar la funcionalidad REDO/UNDO en este plugin
//	private UndoableCommand createCommand()
//	{return null;}

	public void setContext(PlugInContext localContext)
	{
		this.localContext = localContext;
	}

	/**
	 * Cambia los atributos de las Features
	 */
	
	protected void setFields(TaskMonitor monitor, ActionEvent e)
	{
		//TODO: Sustituir la comprobación por la posibilidad de deshacer.
	    monitor.allowCancellationRequests();
	    monitor.report(aplicacion.getI18nString("TransfiriendoDatosEnlazados"));
		String attributeIDonTarget=(String)getFromBlackboard(ATTRIBUTE_ID_ON_TARGET);//cbTargetField.getSelectedItem();//(String)getFromBlackboard(ATTRIBUTE_ID_ON_TARGET);
		String attributeIDonSource=(String)getFromBlackboard(ATTRIBUTE_ID_ON_SOURCE);
		if (attributeIDonSource==null ) return;
		String expresion= (String) getFromBlackboard(EXPRESION_FORMULA);


		if (featureExpressionPanel.getExpParser().getErrorInfo()!=null)
		{
			JOptionPane.showMessageDialog(this, aplicacion.getI18nString("HayErrorEnEexpresión")+" Expression:"+expresion+" ErrorInfo:"+featureExpressionPanel.getExpParser().getErrorInfo());
			return;
		}
		if (JOptionPane.showConfirmDialog(this,MessageFormat.format("¿Aplicar cambios en el campo \"{0}\" de la capa \"{1}\"?\r\n Se copiarán los valores del campo \"{2}\" de las entidades enlazadas de la\r\n capa \"{3}\"",
				new Object[]{
				attributeIDonSource,
				((Layer)getFromBlackboard(SELECTEDSOURCELAYER)).getName(),
				attributeIDonTarget,
				((Layer)getFromBlackboard(SELECTEDTARGETLAYER)).getName(),
				}),null,JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION) return;

		TreeMap finalRelation=(TreeMap) getFromBlackboard(FINAL_LINKING_ARRAY);
		if (finalRelation==null) return;
	
		Iterator finalRelations = finalRelation.entrySet().iterator();
		FeatureExpresionParser expParser= featureExpressionPanel.getExpParser();
		int cont = 0;
		Layer sourceLayer = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
		boolean isFiringEvents = sourceLayer.getLayerManager().isFiringEvents();
		try
		{
	
	
		    sourceLayer.getLayerManager().setFiringEvents(false);
			while (finalRelations.hasNext())
			{
			    monitor.report(cont++,finalRelation.size(),aplicacion.getI18nString("TransfiriendoDatosEnlazados"));
				Map.Entry entry= (Entry) finalRelations.next();
				Feature target = (Feature)entry.getValue();
				Feature source = (Feature)entry.getKey();
				expParser.setFeature(target);
				expParser.parseExpression(expresion);
				Object value = expParser.getValueAsObject();
				// TODO: Revisar el ajuste de tipos
				source.setAttribute(attributeIDonSource,value);
	
			}
	
			 DataSource geopistaServerDataSource =  sourceLayer.getDataSourceQuery().getDataSource();
		
		
			 ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
			 String sridDefecto = ((GeopistaConnection)geopistaServerDataSource.getConnection()).getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
			 logger.info("Srid Defecto al insertar:"+sridDefecto);
		     if (geopistaServerDataSource.getProperties() != null) {
		         geopistaServerDataSource.getProperties().put("srid_destino", Integer.valueOf(sridDefecto));
		     } else {
		         Map properties = new HashMap();
		         properties.put("srid_destino", Integer.valueOf(sridDefecto));
		         geopistaServerDataSource.setProperties(properties);
		     }
		     if(geopistaServerDataSource!=null && geopistaServerDataSource instanceof GeopistaServerDataSource)
		     {
		    	 try
		    	 {
			    	 Iterator itFeature = sourceLayer.getFeatureCollectionWrapper().getUltimateWrappee().getFeatures().iterator();
			    	 FeatureCollection featureCollection = AddNewLayerPlugIn.createBlankFeatureCollection();
			    	 while (itFeature.hasNext()){
			    		 Feature feature = (Feature)itFeature.next();
			    		 GeopistaSchema schema = (GeopistaSchema)((GeopistaFeature)feature).getSchema();
			             String nombreAtributo = schema.getAttributeByColumn("id_municipio");
			             if (((GeopistaFeature)feature).getAttribute(nombreAtributo) instanceof String){
				             if (((String)((GeopistaFeature)feature).getAttribute(nombreAtributo)).equals(String.valueOf(AppContext.getIdMunicipio())))
				            	 featureCollection.add(feature);
			             }else{
				             if (((BigDecimal)((GeopistaFeature)feature).getAttribute(nombreAtributo)).intValue() == AppContext.getIdMunicipio())
				            	 featureCollection.add(feature);
			             }
			    	 }
			    	 geopistaServerDataSource.getConnection().executeUpdate(sourceLayer.getDataSourceQuery().getQuery(), featureCollection, monitor);
		    	 }catch(Exception e1)
		    	 {
		    		 logger.error(e1);
		    	 }
		     }
		}
		catch(Exception ex){
			logger.error(ex);
		}finally
		{
		    sourceLayer.getLayerManager().setFiringEvents(isFiringEvents);
		}
	}
	
	private void setup()
	{
		if (localContext!=null)
			setup(this.localContext);
		if(getFromBlackboard(EXPRESION_FORMULA)!=null)
		    featureExpressionPanel.setText((String)getFromBlackboard(EXPRESION_FORMULA));

	}
	public void setup(PlugInContext context)
	{
		if (context==null) return;
		setContext(context);
		try
		{
			
			if (localContext.getActiveTaskComponent()!=null){
			GeopistaFunctionUtils.initializeLayerComboBox(null, cbSourceLayer,
					(Layer) getFromBlackboard(
							SELECTEDSOURCELAYER), "to-do",
							localContext.getActiveTaskComponent().getLayerManager().getLayers());
	
			GeopistaFunctionUtils.initializeLayerComboBox(null, cbTargetLayer,
					(Layer) getFromBlackboard(
							SELECTEDTARGETLAYER), "to-do",
							localContext.getActiveTaskComponent().getLayerManager().getLayers());
			}

		} catch (Exception e)
		{
		logger.error("setup(PlugInContext)", e);
		}

	}
	/**
	 * @param cbTargetField2 combo destino de la actualizacion
	 * @param item valor del que obtener los valores nuevos (debe ser un Layer)
	 */
	protected void updateFieldList(JComponent cbTargetField2, Object item, Object selected)
	{
		if (item==null)return;

		FeatureSchema fsch=((Layer)item).getFeatureCollectionWrapper().getFeatureSchema();
		Vector vect=new Vector();
		for (int i =0;i<fsch.getAttributeCount();i++)
			{
			if (i!=fsch.getGeometryIndex())
				vect.addElement(fsch.getAttributeName(i));
			}
		if (cbTargetField2 instanceof JComboBox)
		{
		((JComboBox)cbTargetField2).setModel(new DefaultComboBoxModel(vect));
		((JComboBox)cbTargetField2).setSelectedItem(selected);
		}
		else if (cbTargetField2 instanceof JList)
		{
			((JList)cbTargetField2).setListData(vect);
			((JList)cbTargetField2).setSelectedValue(selected,true);
		}
	}
	/**
	 * This method initializes sourceFieldScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getSourceFieldScrollPane() {
		if (sourceFieldScrollPane == null) {
			sourceFieldScrollPane = new JScrollPane();
			sourceFieldScrollPane.setViewportView(getLstSourceField());
			sourceFieldScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			sourceFieldScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			sourceFieldScrollPane.setPreferredSize(new java.awt.Dimension(160,82));
		}
		return sourceFieldScrollPane;
	}
	/**
	 * This method initializes targetFieldScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getTargetFieldScrollPane() {
		if (targetFieldScrollPane == null) {
			targetFieldScrollPane = new JScrollPane();
			targetFieldScrollPane.setViewportView(getLstTargetField());
			targetFieldScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			targetFieldScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			targetFieldScrollPane.setPreferredSize(new java.awt.Dimension(160,82));
		}
		return targetFieldScrollPane;
	}
	/**
	 * This method initializes chkUpdateMap
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChkUpdateMap() {
		if (chkUpdateMap == null) {
			chkUpdateMap = new JCheckBox();
			chkUpdateMap.setText(aplicacion.getI18nString("updateMap"));
			chkUpdateMap.setSelected(true);
		}
		return chkUpdateMap;
	}
	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			java.awt.FlowLayout flowLayout6 = new FlowLayout();
			jPanel = new JPanel();
			jPanel.setLayout(flowLayout6);
			flowLayout6.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel.add(getBtnAutoLink(), null);
			jPanel.add(getBtnManualLink(), null);
			jPanel.add(getBtnReset(), null);
			jPanel.add(getBtnSetField(), null);
			jPanel.add(getChkUpdateMap(), null);

			//TODO Mientras se arregla la funcionalidad lo ponemos oculto. Arreglar
			getChkUpdateMap().setVisible(false);
		}
		return jPanel;
	}
	/**
	 * This method initializes featureExpressionPanel
	 *
	 * @return com.geopista.ui.FeatureExpressionPanel
	 */
	private FeatureExpressionPanel getFeatureExpressionPanel() {
		if (featureExpressionPanel == null) {
			featureExpressionPanel = new FeatureExpressionPanel();
			featureExpressionPanel.addPropertyChangeListener(new PropertyChangeListener()
			        {
			            public void propertyChange(PropertyChangeEvent evt)
			            {
			                putToBlackboard(EXPRESION_FORMULA,evt.getNewValue());
			            }
			        });
		}
		return featureExpressionPanel;
	}
  }  //  @jve:decl-index=0:visual-constraint="10,15"
