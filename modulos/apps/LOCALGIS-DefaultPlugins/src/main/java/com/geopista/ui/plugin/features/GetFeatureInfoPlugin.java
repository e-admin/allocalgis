/**
 * GetFeatureInfoPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.features;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.ui.wms.FeatureInfoRequest;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.FeatureInfoDialog;
import com.vividsolutions.jump.workbench.ui.task.FormatFeatureInfoDialog;
import com.vividsolutions.wms.BoundingBox;

/**Plugin que muestra la opción de menú "Ver información Entidad Seleccionada", el cual aparece cuando
 * se pulsa con el botón derecho sobre un mapa.
 * Sólo estará disponible si se seleccionado una única entidad de una determinada capa del mapa.
 * @author sgrodriguez
 *
 */
public class GetFeatureInfoPlugin extends AbstractPlugIn{
	    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	    
	    
	    
	    
	    /**Método que crea un objeto que sirve para habilitar y deshabilitar la opción de menú.
	     */
	    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
	        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

	        return new MultiEnableCheck()
	            .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
	            .add (checkFactory.createWMSLayersMustHaveBeenRequested());

	    }//fin del método createEnableCheck
	       
	    
	    
	    
	    
	    
	    /**Inicializa el plugin.
	     */
	    public void initialize(PlugInContext context) throws Exception {
	        
	        JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();
	        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	    	featureInstaller.addPopupMenuItem(popupMenu, this,
	        GeopistaFunctionUtils.i18n_getname(this.getName()), false, null,
	        GetFeatureInfoPlugin.createEnableCheck(context.getWorkbenchContext()));
	        }//fin del método initialize
	    
	    
	   
	    
	    /**Retorna el nombre de la clave en el fichero de properties que se corresponde con el nombre
	     * de la opción de menú asciada a este plugin.
	     */
	    public String getName() {
	        return "GetFeatureInfo";
	    } //fin del método getName 
	    
	    
	    
	   /**Método que se ejecuta cuando seleccionamos la opción de menú "Ver información Entidad Seleccionada".
	    */
	    public boolean execute(PlugInContext context)
        throws Exception {
	    	
	    	//Obtener las capas WMS visibles en el mapa
	    	List WMSlayers=context.getWorkbenchContext().getLayerManager().getVisibleLayerables(WMSLayer.class, true);
	    	if(WMSlayers.size()<1){
	    		  JOptionPane.showMessageDialog(aplicacion.getMainFrame(), GeopistaFunctionUtils.i18n_getname("GetFeatureInfo.noCapas"),
	    				  GeopistaFunctionUtils.i18n_getname("GetFeatureInfo.title"),
                          JOptionPane.WARNING_MESSAGE);
	    	}//fin if
	    	else if(WMSlayers.size()>1){
	    		  JOptionPane.showMessageDialog(aplicacion.getMainFrame(), GeopistaFunctionUtils.i18n_getname("GetFeatureInfo.muchasCapas"), 
	    				  GeopistaFunctionUtils.i18n_getname("GetFeatureInfo.title"),
                          JOptionPane.WARNING_MESSAGE);
	    	}//fin if
	    	
	    	else{//hay una única capa WMS seleccionada o visible
	    		
	    		
	    		//Realizamos la petición con los parámetros necesarios
	    		WMSLayer selectedLayer=(WMSLayer) WMSlayers.get(0);
	    		String format=null;
	    		
	    		//Mostramos el cuadro de diálogo para recoger el formato
	    		List formatosDisponibles= selectedLayer.getService().getCapabilities().getGetFeatureInfoFormats();
	    		if(formatosDisponibles.size()>0){
	    		FormatFeatureInfoDialog formatFeatureInfoDialog=
	    			new	FormatFeatureInfoDialog(aplicacion.getMainFrame(),formatosDisponibles);
	    		GUIUtil.centreOnWindow(formatFeatureInfoDialog);
	    		formatFeatureInfoDialog.setVisible(true);
	    		format=FormatFeatureInfoDialog.getFormat();
	    		}//fin if
	    		
	    		
	    		FeatureInfoRequest request=new FeatureInfoRequest();
	    		request.setService(selectedLayer.getService());
	    		request.setFormat(format);
	    		String layerNames="";
	    		
	    		Iterator it=selectedLayer.getLayerNames().iterator();
	    		while(it.hasNext()){
	    			String layerName=(String) it.next();
	    			layerNames+=layerName;
	    			if(it.hasNext())
	    				layerNames+=",";
	    		}//fin del while
	    		
	    		request.setWMSLayers(layerNames);
	    		
	    		//obtenemos las coordenadas del punto en el que ha pinchado el usuario
	    		           
	            Point p=   context.getLayerViewPanel().getLastClickedPoint();
	    		request.setHeight(context.getWorkbenchContext().getLayerViewPanel().getHeight());
	    		request.setWidth(context.getWorkbenchContext().getLayerViewPanel().getWidth());
	    		String SRS=selectedLayer.getSRS();
	    		BoundingBox bBox=toBoundingBox(SRS, context.getWorkbenchContext().getLayerViewPanel().getViewport()
	    				.getEnvelopeInModelCoordinates());
	    		request.setX(p.getX());
	    		request.setY(p.getY());
	    		request.setBbox(bBox);
	    		
	    		
	    		String respuesta=request.callGetFeatureInfo();
	    		//String getFeatureInfoRequest=request.getFeatureInfoRequest();
	    	
	    		
	    		boolean error=false;
	    		
	    		/**Comprobamos si se ha producido algún error en la petición
	    		 */  		 
	    		if(respuesta.contains("ServiceExceptionReport")){
	    		    error=true;
	    		    FeatureInfoDialog featureInfoDialog=new FeatureInfoDialog(aplicacion.getMainFrame(),respuesta, format);
	    		    featureInfoDialog.setTitle( GeopistaFunctionUtils.i18n_getname("GetFeatureInfo.error")); 
	    		    GUIUtil.centreOnWindow(featureInfoDialog);
		    		featureInfoDialog.setVisible(true);	
	    		}//fin if (se han producido errores)
	    		
	    		if(!error){
	    		//Creamos el cuadro de diálogo en el que mostraremos los resultados
	    		FeatureInfoDialog featureInfoDialog=new FeatureInfoDialog(
	    				aplicacion.getMainFrame(),respuesta/*getFeatureInfoRequest*/,format);
	    		featureInfoDialog.setTitle(GeopistaFunctionUtils.i18n_getname("ui.MenuNames.LAYER")+": "+selectedLayer.getName());
	    		GUIUtil.centreOnWindow(featureInfoDialog);
	    		featureInfoDialog.setVisible(true);	    		
	    	}//fin del else
	    	}//fin if
	    	return true;
	    	
    }//fin del método execute
	    
	    
	    
	    
	    
	    private BoundingBox toBoundingBox(String srs, Envelope e) {
			return new BoundingBox(srs, e.getMinX(), e.getMinY(), e.getMaxX(), e
					.getMaxY());
		}
	
	
	

}//fin de la clase
