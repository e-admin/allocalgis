/**
 * LocalgisEIELConectividadPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.conectividad;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaUtilEIEL;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.FenceLayerFinder;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;



public class LocalgisEIELConectividadPlugIn extends AbstractPlugIn{
      private static final Log logger = LogFactory.getLog(LocalgisEIELConectividadPlugIn.class);
      private static AppContext aplicacion = (AppContext) AppContext
              .getApplicationContext();

      private String toolBarCategory = "GeopistaFeatureSchemaPlugIn.category";
      private Feature localFeature = null;

      static public final ImageIcon ICON = IconLoader.icon("gota.gif");

      public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
      {
          EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

          return new MultiEnableCheck()
          .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
          .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
          .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
          .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1));
     }

      public void initialize(PlugInContext context) throws Exception
      {
    	  /*context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar("Conectividad").addPlugIn(
    	            getIcon(), this,
    	            createEnableCheck(context.getWorkbenchContext()),
    	        context.getWorkbenchContext());*/


        JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

       
        featureInstaller.addPopupMenuItem(popupMenu, this,
        		GeopistaUtilEIEL.i18n_getname(getName()), false,
				GUIUtil.toSmallIcon(ICON),
				createEnableCheck(context.getWorkbenchContext()));
      }

      public boolean execute(PlugInContext context) throws Exception
      {
          if (!aplicacion.isOnline())
          {
              JOptionPane.showMessageDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
              return false;
          }
          
          String redAsociada=null;
          int numElementosEncontrados=0;
          
          Feature featureSeleccionada=(Feature)context.getLayerViewPanel().getSelectionManager().
          		getFeaturesWithSelectedItems().iterator() .next();
          if (featureSeleccionada instanceof GeopistaFeature){
        	  GeopistaFeature geopistaFeatureSeleccionada=(GeopistaFeature)featureSeleccionada;;        	
        	  try {
				redAsociada=(String)geopistaFeatureSeleccionada.getAttribute("red_asociada");
        	  } catch (Exception e) { }
        	  //System.out.println("Red Asociada:"+redAsociada);
          }
          
          if (redAsociada==null){
        	  JOptionPane.showMessageDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
                      aplicacion.getI18nString("mensaje.no.redasociada"));
        	  return false;
          }
          
          List capasVisibles = context.getWorkbenchContext().getLayerNamePanel()
                  .getLayerManager().getVisibleLayers(true);
          Iterator capasVisiblesIter = capasVisibles.iterator();
                  
          
          HashMap featuresPorCapa=new HashMap();
          while (capasVisiblesIter.hasNext())
          {
              Layer layer = (Layer) capasVisiblesIter.next();
              
              if (layer.getName().equals(FenceLayerFinder.LAYER_NAME)) {
                  continue;
              }

              if (layer instanceof DynamicLayer){
  	            if (!((DynamicLayer)layer).isActiva()) {
  	                continue;
  	            }
              }else{
  	            if (!((GeopistaLayer)layer).isActiva()) {
  	                continue;
  	            }
              }
              
            
              ArrayList arrayFeaturesSeleccionadas=new ArrayList();
              if (layer instanceof GeopistaLayer){
              	String idLayer=((GeopistaLayer)layer).getSystemId();
              	//System.out.println("ID LAYER:"+idLayer);
              	Iterator featureIterator=layer.getFeatureCollectionWrapper().getFeatures().iterator();
              	while (featureIterator.hasNext()){
              		Feature feature=(Feature)featureIterator.next();
              		if (feature instanceof GeopistaFeature){
              			GeopistaFeature geopistaFeatureEncontrada=(GeopistaFeature)feature;
              			try {
            				String redElemento=(String)geopistaFeatureEncontrada.getAttribute("red_asociada");
            				if (redAsociada.equals(redElemento)){
            					//System.out.println("ENCONTRADO:"+geopistaFeatureEncontrada.getID());
            					numElementosEncontrados++;
            					context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(layer, feature);
            					arrayFeaturesSeleccionadas.add(feature);
            					 
            				}
                    	  } catch (Exception e) { }

              		}
              		
              	}
              }
              featuresPorCapa.put(layer, arrayFeaturesSeleccionadas);
          }
          
          
          
          String mensaje="";
          if (numElementosEncontrados==0)
        	  mensaje="No se han encontrado elementos conectados en la red";
          else if (numElementosEncontrados==1)
        	  mensaje="Se han encontrado "+numElementosEncontrados+" elemento conectado en la red";
          else
        	  mensaje="Se han encontrado "+numElementosEncontrados+" elementos conectados en la red";

          JOptionPane.showMessageDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), mensaje);
          
          Iterator it= featuresPorCapa.keySet().iterator();
          while (it.hasNext()){
        	  Layer layer=(Layer)it.next();
        	  ArrayList listaFeatures=(ArrayList)featuresPorCapa.get(layer);
        	  
        	  for (int i=0;i<listaFeatures.size();i++){
        		  Feature feature=(Feature)listaFeatures.get(i);
        		     FeatureDialog featureDialog = new FeatureDialog(GeopistaFunctionUtils
        	                  .getFrame(context.getWorkbenchGuiComponent()), "Atributos", true,
        	                  feature, context.getWorkbenchContext().getLayerViewPanel(),layer);
        			 featureDialog.buildDialog();
        	          featureDialog.setVisible(true);
        		  
        	  }
          }
     
          
          
    	 
       	  return true;
          
          
          /*

          final LockManager lockManager = (LockManager) context.getActiveTaskComponent()
                  .getLayerViewPanel().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);

          Vector features= new Vector();
          final ArrayList lockResultaArrayList = new ArrayList();
          boolean bloquear = false; //Si alguna de las features esta en una capa
                                    // capa de sistema. La feature debe bloquearse
          while (capasVisiblesIter.hasNext())
          {
              Layer capaActual = (Layer) capasVisiblesIter.next();
              Collection featuresSeleccionadas = context.getWorkbenchContext()
                      .getLayerViewPanel().getSelectionManager()
                      .getFeaturesWithSelectedItems(capaActual);
              // Almacenamos en este ArrayList el resultado de la operacion de
              // bloqueo
              Iterator featuresSeleccionadasIter = featuresSeleccionadas.iterator();
              //no editable  bloquemaos la pantalla
              while (featuresSeleccionadasIter.hasNext())
              {
                  localFeature = (Feature) featuresSeleccionadasIter.next();
                  String systemId = ((GeopistaFeature)localFeature).getSystemId();
                  if (!((GeopistaLayer)capaActual).isLocal() && !((GeopistaLayer)capaActual).isExtracted())
                      features.add(localFeature);
                  if( capaActual instanceof GeopistaLayer   &&
                          !((GeopistaLayer)capaActual).isLocal() &&
                          !((GeopistaLayer)capaActual).isExtracted() &&
                          capaActual.isEditable()&& systemId!=null &&
                          !((GeopistaFeature)localFeature).isTempID() &&
                          !systemId.equals(""))
                  {

                         final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                          .getMainFrame(), context.getErrorHandler());
                         progressDialog.setTitle(aplicacion.getI18nString("LockFeatures"));
                         progressDialog.addComponentListener(new ComponentAdapter()
                         {
                              public void componentShown(ComponentEvent e)
                              {
                                     new Thread(new Runnable(){
                                          public void run(){
                                              try{
                                                  Integer lockID = lockManager.lockFeature(localFeature, progressDialog);
                                                  lockResultaArrayList.add(lockID);
                                              } catch (Exception e){}
                                              finally{
                                                  progressDialog.setVisible(false);
                                              }
                                          }
                                  }).start();
                          }});
                          GUIUtil.centreOnWindow(progressDialog);
                          progressDialog.setVisible(true);
                  }else
                      bloquear=true;

              }
          }
          if (features.size()==0)
         {
             JOptionPane.showMessageDialog(GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()),
                     aplicacion.getI18nString("mensaje.no.capasistema"));
             return false;
         }

          ImageDialog imageDialog = new ImageDialog(GeopistaUtil
                          .getFrame(context.getWorkbenchGuiComponent()), "Atributos", true,
                          features, context.getWorkbenchContext().getLayerViewPanel());

          try{
                  ImageIcon icon = IconLoader.icon("logo_geopista.png");
                  imageDialog.setSideBarImage(icon);
          } catch (NullPointerException e){
                  logger.error("Error el icono logo_geopista.png", e);
          }
          imageDialog.buildDialog();
          if (bloquear) imageDialog.setLock();
          imageDialog.setVisible(true);
           // Hay que desbloquear los bloqueados
        for (Iterator it=lockResultaArrayList.iterator();it.hasNext();)
        {
              final Integer lockID=(Integer)it.next();
              final TaskMonitorDialog progressDialogFinal = new TaskMonitorDialog(
                          aplicacion.getMainFrame(), context.getErrorHandler());
              progressDialogFinal.setTitle(aplicacion
                          .getI18nString("UnlockFeatures"));
              progressDialogFinal.addComponentListener(new ComponentAdapter(){
                  public void componentShown(ComponentEvent e)
                  {
                      new Thread(new Runnable() {
                                      public void run()
                                      {
                                          try
                                          {
                                                  lockManager.unlockFeaturesByLockId(
                                                      lockID, progressDialogFinal);
                                          } catch (Exception e){
                                         } finally
                                          {
                                              progressDialogFinal.setVisible(false);
                                          }
                      }
                        }).start();
                    }
                });
                GUIUtil.centreOnWindow(progressDialogFinal);
                progressDialogFinal.setVisible(true);
           }
           */

      }

      public ImageIcon getIcon()
      {
          return ICON;
      }


      public String getName() {
          return "LocalgisEIELConectividadPlugIn";
      }
  }


