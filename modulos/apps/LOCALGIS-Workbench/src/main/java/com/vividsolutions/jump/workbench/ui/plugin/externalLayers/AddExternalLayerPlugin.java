/**
 * AddExternalLayerPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.externalLayers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.app.loadEIELData.service.LoadEIELDataProxy;
import com.geopista.app.loadEIELData.vo.CompleteEIELLayer;
import com.geopista.app.loadEIELData.vo.EIELLayer;
import com.geopista.model.ExternalLayer;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.renderer.style.EIELVertexStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.Style;
import com.vividsolutions.jump.workbench.ui.renderer.style.VertexStyle;


//nos guiamos por la clase LoadSystemLayersPlugin
/**
 * Plugin que muestra la opción de menú "Añadir capa externa...", el cual aparece cuando
 * se pulsa con el botón derecho sobre el panel de capas de un mapa en el Editor GIS.
 * Esta opción también se mostrará en el menú Archio del Editor GIS.
 * 
 * @author Silvia García
 */
public class AddExternalLayerPlugin extends ThreadedBasePlugIn{
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	  private Blackboard blackboard  = aplicacion.getBlackboard();//en blackboard almacenaremos la lista de capas elegidas por el usuario
	 
	 	
	
	
	/**Inicializa el plugin
	 */
	 public void initialize(PlugInContext context) throws Exception {

		    FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

		    //añadimos la opción al menú popup que aparece cuando pulsamos el botón derecho sobre el panel de capas
		    featureInstaller.addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
		                                                        .getGuiComponent()
		                                                        .getCategoryPopupMenu(),
		          this, StringUtil.insertSpaces(this.getName()) + "...", false,
		          null, AddExternalLayerPlugin.createEnableCheck(context.getWorkbenchContext()));

		   		   
		   //añadimos la opción al menú Archivo
		   featureInstaller.addMainMenuItem(this, aplicacion.getI18nString("File"),
		            this.getName() + "...", null, AddExternalLayerPlugin.createEnableCheck(context.getWorkbenchContext()));
		   
		  }//fin del método initialize
	 
	 
	 
	  /**Retorna el nombre de la opción de menú que nos mostrará el plugin
	     */
	    public String getName() {
	    	return GeopistaFunctionUtils.i18n_getname("AddExternalLayerPlugin.name");
	    }//fin método getName
	    
	    
	    
	    
	    /**Método que se ejecuta cuando seleccionamos la opción de menú "Añadir capa externa...".
		    */
		    public boolean execute(PlugInContext context)throws Exception {
		    	   
		        WizardDialog d = new WizardDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
		        		   aplicacion.getI18nString("AddExternalLayerPlugin.name")
		          , context.getErrorHandler());
		          d.init(new WizardPanel[] {new LoadExternalLayersPanel("LoadExternalLayersPanel",null,context)});

		          //Set size after #init, because #init calls #pack. [Jon Aquino]
		          d.setSize(550,450);
		          GUIUtil.centreOnWindow(d);
		          d.setVisible(true);
		          if (!d.wasFinishPressed()) {
		              return false;//el usuario ha cancelado la operación
		          }

		          return true;//el usuario ha seleccionado las capas que desea cargar y pulsa el botón finalizar
		    }//fin del método execute
		    
		    
		    
		    
		    /**Método que se ejecuta una vez que el usuario ha seleccionado las capas que desea cargar y pulsa el botón finalizar
		     */
		    public void run(TaskMonitor monitor, final PlugInContext context)
	        throws Exception {
		    	
		    	
		    	 LoadEIELDataProxy eielProxy=new LoadEIELDataProxy();
		    	 int idMunicipio=aplicacion.getIdMunicipio();
		    	 
		    	 Locale locale=aplicacion.getI18NResource().getLocale();
		    	 String lang="es";
		    	 
		    	 if(locale.toString().equalsIgnoreCase("gl_ES"))
         			lang="gl";
		    	 
		    	 ArrayList externalLayers = (ArrayList) blackboard.get("SelectedExternalLayers");
		    	 Iterator it=externalLayers.iterator();
		    	 
		    	 while (it.hasNext()){
		    		 final EIELLayer eielLayer=(EIELLayer) it.next();
		    		 
		    		 
		    		 monitor.report(aplicacion.getI18nString("LoadSystemLayers.Cargando")+" "+eielLayer.getId());
		    		 		    		 
		    		
		    		 /**Bloque comprueba si la capa que deseamos cargar ya está cargada
		    		  */
		    		 boolean layerRepeated = false;
		              List currentLayers =  context.getLayerManager().getLayerables(ExternalLayer.class);
		              Iterator currentLayersIterator = currentLayers.iterator();
		              while (currentLayersIterator.hasNext()){
		                  Layer currentLayer = (Layer) currentLayersIterator.next();
		                  if(currentLayer instanceof ExternalLayer){
		                	  ExternalLayer extLayer=(ExternalLayer) currentLayer;
		                	  if(extLayer.getExternalId().equalsIgnoreCase(eielLayer.getId())){
		                          layerRepeated = true;
		                          break;
		                      }//fin if
		                  }//fin if
		              }//fin del while que recorre las capas que ya hemos añadido al gesto de capas     
		              
		              
		              if(layerRepeated) 
		            	  continue;
		              
		    		 /**Fin del bloque que comprueba las repeticiones
		    		  */
		             
		              
		    		 
		    		 CompleteEIELLayer completeEIELLayer= eielProxy.loadEIELLayer(String.valueOf(idMunicipio), eielLayer.getId(), lang, eielLayer.getTable(),
		    				 eielLayer.getIdField(), eielLayer.getGeometryField());
		    		 
		    		 if(completeEIELLayer!=null){
		    			 final ExternalLayer externalLayer=new ExternalLayer(
		    					 	eielLayer.getId(),
		    		                context.getLayerManager().generateLayerFillColor(),
		    		                completeEIELLayer.createFeatureCollection(),
		    		                context.getLayerManager(),
		    		                eielLayer.getId(),
		    		                eielLayer.getTable(),
		    		                eielLayer.getIdField(),
		    		                eielLayer.getGeometryField(),
		    		                eielProxy.getEndpoint()
		    		                );
		    			 externalLayer.setVisible(true);
		    			 
		    			 final List styles=completeEIELLayer.getStyle();
		 
		    		        SwingUtilities.invokeLater(new Runnable() {

		    		            public void run() {
		    		            	//añadimos la capa al gestos de capas
		    		                  context.getLayerManager()
		    		                        .addLayerable(StandardCategoryNames.WORKING, externalLayer);
		    		                  
		    		                  //establecemos el estilo correspondiente a la capa
		    		                  if(styles!=null){
		    		                		externalLayer.removeStyle(externalLayer.getBasicStyle());
		    		                		
		    		                		for(int i=0;i<styles.size();i++){
		    		                			
		    		                			if((Style) styles.get(i) instanceof EIELVertexStyle)
		    		                				externalLayer.removeStyle(externalLayer.getStyle(VertexStyle.class));
		    		                			
		    		                			if((Style) styles.get(i) instanceof LabelStyle)
		    		                				externalLayer.removeStyle(externalLayer.getLabelStyle());
		    		                			
		    		                			externalLayer.addStyle((Style) styles.get(i)); 
		    		     					}//fin del for
		    		                	  
		    			    			 }//fin if: tratamiento de estilos	    
		    		            }
		    		        });
		    		        
		    		        
		    		 }//fin if que comprueba si la capa que hemos cargado no es nula
 
		    	 }//fin while que recorre la lista de capas externas seleccionadas
		    }//fin método run
		    
		    
		    
		    
		    public static MultiEnableCheck createEnableCheck(
		            final WorkbenchContext workbenchContext) {
		            GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

		            MultiEnableCheck m = new MultiEnableCheck();
		            m.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck());
		            return m;
		        }
	
	
	
	
}//fin del plugin AddExternalLayerPlugin
