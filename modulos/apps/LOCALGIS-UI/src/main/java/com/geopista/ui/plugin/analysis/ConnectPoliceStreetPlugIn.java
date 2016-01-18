/**
 * ConnectPoliceStreetPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.plugin.analysis;


import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaSystemLayers;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureDatasetFactory;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.geom.InteriorPointFinder;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.ArrowLineStringEndpointStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.CircleLineStringEndpointStyle;



public class ConnectPoliceStreetPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    private static final double BUFFERDISTANCEINCREMENT = 10.0;
    private final String BUFFERLAYER =  aplicacion.getI18nString("lyrBufferLayer");
    private final String ERRORLAYER =  aplicacion.getI18nString("lyrErrorLayer");
    private final String POLICESTREET =  aplicacion.getI18nString("lyrPoliceStreetLayer");
    private final String IDASSOCIATEVIA = aplicacion.getString("IDASSOCIATEVIA");
    private final String IDSTREET = aplicacion.getString("IDSTREET");    
    private FeatureCollection initialFeature = null;
    private FeatureCollection initialErrorFeature=null;
    private FeatureCollection initialPoliceStreet=null;
    private Color color = Color.BLUE;
    private Color colorError = Color.RED;
    private String action = "";
    private PlugInContext localContext = null;
    private Layer bufferLayer = null;
    private HashMap arrowFeatureRelation = new HashMap();
    private JButton automaticButton =  new JButton();
    private JButton correctionButton =  new JButton(); 
    private JButton associateButton =  new JButton(); 
    private JDialog streetOptions = null;

    private JToolBar streetToolBar = new JToolBar();
    private HashMap finalRelation = new HashMap();

    private String toolBarCategory = "ConnectPoliceStreetPlugIn.category";
    
    public ConnectPoliceStreetPlugIn() {
    }

    
      public void initialize(PlugInContext context) throws Exception {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
            this,
            createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());
        streetOptions = new JDialog(aplicacion.getMainFrame(),false);
        automaticButton.setIcon(IconLoader.icon("numerospolicia2.gif"));
      automaticButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          automaticConnect(e);
        }
      });

      
      correctionButton.setIcon(IconLoader.icon("fusionar_capa.gif"));
      correctionButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          correctionConnect(e);
        }
      });

      associateButton.setIcon(IconLoader.icon("fusionar_capa.gif"));
      associateButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          associateConnect(e);
        }
      });

      streetOptions.getContentPane().setLayout(new FlowLayout());

      streetToolBar.setSize(150,50);
      streetToolBar.setLayout(new FlowLayout());
      streetToolBar.add(automaticButton);
      streetToolBar.add(correctionButton);  
      streetToolBar.add(associateButton);
      streetOptions.getContentPane().add(streetToolBar);
      streetOptions.setSize(180,90);

      }
    
    public boolean execute(PlugInContext context) throws Exception {
        this.localContext = context;
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        initialFeature = new FeatureDataset(featureSchema);
        initialErrorFeature = new FeatureDataset(featureSchema);
        initialPoliceStreet = new FeatureDataset(featureSchema);

        showOptions(context);
        
        return false;
    }

    

    public void run(TaskMonitor monitor, PlugInContext context)
        throws Exception {

        
        
    }

    private ArrayList nearestFeatures(Feature sourceFeature, FeatureCollection collectionWorkFeatures)
    {

      Geometry geomSourceFeature = sourceFeature.getGeometry();

      

      int contador=1;
      boolean foundFeature=false;
      Geometry bufferGeometry=null;
      ArrayList nearStreetNumbers = new ArrayList();

      while((!foundFeature)&&(contador<10))
      {
        bufferGeometry = geomSourceFeature.buffer(BUFFERDISTANCEINCREMENT*contador);
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
  

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
           .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
            .add(checkFactory.createLayerStreetBeExistCheck())
            .add(checkFactory.createLayerNumerosPoliciaBeExistCheck());

 
        
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("numerospolicia2.gif");
    }

    protected void applyStyles(Layer layer) {
        if (null == layer.getStyle(ArrowLineStringEndpointStyle.class)) {
            layer.addStyle(new ArrowLineStringEndpointStyle.SolidEnd());
        }
        if (null == layer.getStyle(CircleLineStringEndpointStyle.Start.class)) {
            layer.addStyle(new CircleLineStringEndpointStyle.Start());
        }
        layer.getBasicStyle().setLineColor(color);
        layer.getBasicStyle().setFillColor(color);
        layer.getBasicStyle().setRenderingFill(false);
        layer.setDrawingLast(true);
    }

    private void showOptions(PlugInContext context)
    {



            streetOptions.setVisible(true);
      
    }

    private void automaticConnect(ActionEvent e)
    {

      //Comprobamos primero que se cumplan las condiciones para realizar la union
      //automatica

      Layer capaNumerosPolicia = localContext.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);
      Layer capaCallejero = localContext.getLayerManager().getLayer(GeopistaSystemLayers.VIAS);

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

      bufferLayer = localContext.getLayerManager().getLayer(BUFFERLAYER);
      if(bufferLayer==null)
      {
       bufferLayer = localContext.addLayer(StandardCategoryNames.WORKING, BUFFERLAYER , initialFeature);
      }




        FeatureCollection featuresCollectionNumerosPolicia = capaNumerosPolicia.getFeatureCollectionWrapper().getUltimateWrappee();
        Collection featuresCallejero = capaCallejero.getFeatureCollectionWrapper().getFeatures();        
        

        Map streetPoliceNumber = new HashMap();
       
        Iterator featuresCallejeroIter = featuresCallejero.iterator();
        while(featuresCallejeroIter.hasNext())
        {
          Feature actualFeature = (Feature) featuresCallejeroIter.next();
          ArrayList actualNearestFeature = nearestFeatures(actualFeature, featuresCollectionNumerosPolicia);

          //Asociamos la calle con sus numeros de policia
          streetPoliceNumber.put(actualFeature,actualNearestFeature);          

        }

        featuresCallejeroIter = featuresCallejero.iterator();
        GeometryFactory geometryFactory = new GeometryFactory();
        streetLayer = localContext.getLayerManager().getLayer(POLICESTREET);
        if(streetLayer==null)
        {
          streetLayer = localContext.addLayer(StandardCategoryNames.WORKING,POLICESTREET,initialPoliceStreet);
          applyStyles(streetLayer);
          ((GeopistaLayer) streetLayer).setActiva(false);
        }
        else
        {
          streetLayer.getFeatureCollectionWrapper().removeAll(streetLayer.getFeatureCollectionWrapper().getFeatures());
        }

           errorLayer = localContext.getLayerManager().getLayer(ERRORLAYER);
           if(errorLayer==null)
           {
             errorLayer = localContext.addLayer(StandardCategoryNames.WORKING,ERRORLAYER,initialErrorFeature);
             errorLayer.getBasicStyle().setLineColor(colorError);
             errorLayer.getBasicStyle().setFillColor(colorError);
           }
           List allPoliceNumberFeatures = localContext.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA).getFeatureCollectionWrapper().getFeatures();
           
           errorLayer.getFeatureCollectionWrapper().addAll(allPoliceNumberFeatures);
        
        while(featuresCallejeroIter.hasNext())
        {
          GeopistaFeature actualStreetFeature = (GeopistaFeature) featuresCallejeroIter.next();
          ArrayList policeNumber = (ArrayList) streetPoliceNumber.get(actualStreetFeature);
          
          Iterator policeNumberIter = policeNumber.iterator();
          while(policeNumberIter.hasNext())
          {
            
            GeopistaFeature actualPoliceNumber = (GeopistaFeature) policeNumberIter.next();
            
            

            if(finalRelation.containsKey(actualPoliceNumber))
            {
              errorLayer.getFeatureCollectionWrapper().add(actualPoliceNumber);
            }
            else
            {
              Coordinate[] coordenadas = new Coordinate[2];
              coordenadas[0] = actualPoliceNumber.getGeometry().getCoordinate();
              coordenadas[1] = geometryMidPoint(actualStreetFeature.getGeometry(),actualPoliceNumber.getGeometry());
              LineString arrow = geometryFactory.createLineString(coordenadas);
              ArrayList arrowArray = new ArrayList();
              arrowArray.add(arrow);
              FeatureCollection arrowCollection = FeatureDatasetFactory.createFromGeometry(arrowArray);

              streetLayer.getFeatureCollectionWrapper().addAll(arrowCollection.getFeatures());
              errorLayer.getFeatureCollectionWrapper().remove(actualPoliceNumber);
              //Hastable que contiene las relaciones finales, cada numero de polica con la calle que
              //le corresponde
              finalRelation.put(actualPoliceNumber,actualStreetFeature);
              arrowFeatureRelation.put(actualPoliceNumber,arrowCollection.getFeatures().get(0));
            }
          }
           
        }
    }

    private void associateConnect(ActionEvent e)
    {
      Set finalRelationSet = finalRelation.keySet();

      Layer capaNumerosPolicia = localContext.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);
      List featuresPoliceNumber = capaNumerosPolicia.getFeatureCollectionWrapper().getFeatures();
      Iterator featuresPoliceNumberIter = featuresPoliceNumber.iterator();
      while(featuresPoliceNumberIter.hasNext())
      {
        GeopistaFeature actualFeature = (GeopistaFeature) featuresPoliceNumberIter.next();
        actualFeature.setAttribute(IDASSOCIATEVIA,new Integer(0));
      }

      Iterator finalRelationSetIter = finalRelationSet.iterator();
      while(finalRelationSetIter.hasNext())
      {
        GeopistaFeature actualPoliceNumberFeature = (GeopistaFeature) finalRelationSetIter.next();
        GeopistaFeature actualStreetFeature = (GeopistaFeature) finalRelation.get(actualPoliceNumberFeature);
        actualPoliceNumberFeature.setAttribute(IDASSOCIATEVIA,actualStreetFeature.getAttribute(IDSTREET));
      }
    }

    private void correctionConnect(ActionEvent e)
    {
      Layer layerStreets = localContext.getLayerManager().getLayer(GeopistaSystemLayers.VIAS);
      Layer layerPoliceNumber = localContext.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);

      if(layerPoliceNumber==null)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.CapaPoliciasNoCargada"));
        return;
      }
      if(layerStreets==null)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.CapaViasNoCargada"));
        return;
      }


      
      Collection selectedStreets = localContext.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(layerStreets);

      if(selectedStreets.size()==0||selectedStreets.size()>1)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.NumeroCallesErroneo"));
        return;
      }
      Collection selectedPoliceNumber = localContext.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(layerPoliceNumber);
      if(selectedPoliceNumber.size()==0)
      {
        JOptionPane.showMessageDialog((Component)localContext.getWorkbenchGuiComponent(),aplicacion.getI18nString("ConnectPoliceStreetPlugIn.NumeroPoliciasErroneo"));
        return;
      }
      

      Layer streetLayer = localContext.getLayerManager().getLayer(POLICESTREET);
      if(streetLayer==null)
      {
        streetLayer = localContext.addLayer(StandardCategoryNames.WORKING,POLICESTREET,initialPoliceStreet);
        applyStyles(streetLayer);
        ((GeopistaLayer) streetLayer).setActiva(false);
      }

      Layer errorLayer = localContext.getLayerManager().getLayer(ERRORLAYER);
      if(errorLayer==null)
      {
       errorLayer = localContext.addLayer(StandardCategoryNames.WORKING,ERRORLAYER,initialErrorFeature);
       errorLayer.getBasicStyle().setLineColor(colorError);
       errorLayer.getBasicStyle().setFillColor(colorError);
      }

      GeopistaFeature featureStreet = (GeopistaFeature) selectedStreets.iterator().next();

      Iterator featuresPoliceNumberIter = selectedPoliceNumber.iterator();

      
      
      
      while(featuresPoliceNumberIter.hasNext())
      {
          GeopistaFeature actualFeaturesPoliceNumber = (GeopistaFeature) featuresPoliceNumberIter.next();
          Coordinate[] coordenadas = new Coordinate[2];
          coordenadas[1] = geometryMidPoint(featureStreet.getGeometry(),actualFeaturesPoliceNumber.getGeometry());
          GeometryFactory geometryFactory = new GeometryFactory();
          coordenadas[0] = actualFeaturesPoliceNumber.getGeometry().getCoordinate();
          LineString arrow = geometryFactory.createLineString(coordenadas);
          ArrayList arrowArray = new ArrayList();
          arrowArray.add(arrow);

          FeatureCollection arrowCollection = FeatureDatasetFactory.createFromGeometry(arrowArray);

          GeopistaFeature featureArrow = (GeopistaFeature) arrowCollection.getFeatures().get(0);
          deleteArrow(actualFeaturesPoliceNumber);
          arrowFeatureRelation.put(actualFeaturesPoliceNumber,featureArrow);
          
          
          streetLayer.getFeatureCollectionWrapper().addAll(arrowCollection.getFeatures());
          errorLayer.getFeatureCollectionWrapper().remove(actualFeaturesPoliceNumber);
      }

      
    
    }

    public void deleteArrow(GeopistaFeature deleteFeature)
    {
      GeopistaFeature arrowFeature = (GeopistaFeature) arrowFeatureRelation.get(deleteFeature);
      if(arrowFeature!=null)
      {
        localContext.getLayerManager().getLayer(this.POLICESTREET).getFeatureCollectionWrapper().remove(arrowFeature);
      }
    }

    private Coordinate geometryMidPoint(Geometry workGeometry, Geometry policePoint)
    {
      double geometriesDistance = policePoint.distance(workGeometry);
      Geometry bufferedPoliceLocation = policePoint.buffer(geometriesDistance+1);

      Geometry intersection = workGeometry.intersection(bufferedPoliceLocation);

      InteriorPointFinder interiorPointFinder = new InteriorPointFinder();

           
      return interiorPointFinder.findPoint(intersection); 

  
    }

    
}
