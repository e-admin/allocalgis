/**
 * SegregatePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.plugin.analysis;


import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.plugin.analysis.images.IconLoader;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;


public class SegregatePlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
        
    private Layer capa = null;
    private Feature sourceFeature = null;
    private ArrayList addedFeatures = new ArrayList();
    private String toolBarCategory = "SegregatePlugIn.category";
    
    private boolean selectSegregateButtonAdded = false;

    public SegregatePlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.analysis.languages.SegregatePlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("SegregatePlugIn",bundle2);
    }

    
      public void initialize(PlugInContext context) throws Exception {
          String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
            this,
            createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());
        
        //Líneas necesarias para añadir el PlugIn a la caja de Edición
        /*GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
      	geopistaEditingPlugIn.addAditionalPlugIn(this);*/
      	
      }
    
    public boolean execute(PlugInContext context) throws Exception {
        return true;
    }

    

    public void run(TaskMonitor monitor, PlugInContext context)
    throws Exception {
    	addedFeatures.clear();

    	Layer[] selectedLayers = context.getSelectedLayers();
    	capa = selectedLayers[0];

    	Collection featuresSource = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(capa);
    	Collection features = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();

    	if(featuresSource.size()<1)
    	{
    		JOptionPane.showMessageDialog((Component)context.getWorkbenchGuiComponent(),I18N.get("SegregatePlugIn","DebeSeleccionarunaFeature"));
    		return;
    	}

    	if(featuresSource.size()>1)
    	{
    		JOptionPane.showMessageDialog((Component)context.getWorkbenchGuiComponent(),I18N.get("SegregatePlugIn","DebeSeleccionarSolounaFeature"));
    		return;
    	}

    	Iterator featuresSourceIter = featuresSource.iterator();
    	sourceFeature = (Feature) featuresSourceIter.next();

    	//Eliminamos la parcelas de las features seleccionadas
    	features.remove(sourceFeature);

    	//Debe seleccionar al menos 2 features para hacer la division
    	if(features.size()<2)
    	{
    		JOptionPane.showMessageDialog((Component)context.getWorkbenchGuiComponent(),I18N.get("SegregatePlugIn","DebeSeleccionaralmenosdosfeaturesparahacerlasegregacion"));
    		return;
    	}

    	//No se permite que se superpongan las Features utilizadas para hacer la segregacion
    	if(comprobarSuperposicion(monitor,features)==true)
    	{
    		JOptionPane.showMessageDialog((Component)context.getWorkbenchGuiComponent(),I18N.get("SegregatePlugIn","LasFeaturesSeleccionadasNoPuedenSuperponerse"));
    		return;
    	}

    	ArrayList sourceSegregateGeometries = new ArrayList();
    	//Geometria de la Feature que queremos segregar
    	Geometry sourceGeometry = sourceFeature.getGeometry();
    	GeopistaFeature sourceGeopistaFeature = (GeopistaFeature)sourceFeature;
    	Iterator featuresIter = features.iterator();
    	while(featuresIter.hasNext())
    	{
    		Feature actualFeature = (Feature) featuresIter.next();
    		Geometry actualGeometry = actualFeature.getGeometry();
    		sourceSegregateGeometries.add(actualGeometry);
    	}

    	if(!comprobarSumaTotalIgualOriginal(monitor,sourceSegregateGeometries,sourceGeometry))
    	{
    		JOptionPane.showMessageDialog((Component)context.getWorkbenchGuiComponent(),I18N.get("SegregatePlugIn","LasFeaturesDebenCubrirTodaSuperficieOriginal"));
    		return;
    	}

    	FeatureSchema sourceSchema = sourceFeature.getSchema();

    	Iterator sourceSegregateGeometriesIter = sourceSegregateGeometries.iterator();
    	while(sourceSegregateGeometriesIter.hasNext())
    	{
    		Geometry actualGeometry = (Geometry) sourceSegregateGeometriesIter.next();
    		Geometry totalGeometry = actualGeometry.intersection(sourceGeometry);
    		GeopistaFeature feature = new GeopistaFeature(sourceSchema);
    		feature.setAttributes(sourceGeopistaFeature.getAttributes());
    		feature.setGeometry(totalGeometry);  
    		setSystemIDNoInitialize(feature);
    		feature.isNew();

    		if(capa instanceof GeopistaLayer)
    			feature.setLayer((GeopistaLayer) capa);
    		addedFeatures.add(feature);

    	}

    	boolean doExecute = false;
    	int cursedfeatures=0;

    	for (Iterator j = addedFeatures.iterator(); j.hasNext();) {
    		final GeopistaFeature feature = (GeopistaFeature) j.next();
    		cursedfeatures++;
    		if(!((feature.getAttributes().length ==1)&&(feature.getSchema().getAttributeType(0) == AttributeType.GEOMETRY))){
    			boolean resultAttributes = GeopistaValidatePlugin.showFeatureDialog(
    					feature, feature.getLayer());
    			if (resultAttributes == true)
    			{
    				doExecute=true;	
    			}
    			else{
    				doExecute = false;
    				if((addedFeatures.size()-cursedfeatures)>0){
    					//Mostrar diálogo de cancelar actual o cancelar todas
    					if (JOptionPane.showConfirmDialog(
    							(Component)context.getWorkbenchGuiComponent(),
    							I18N.get("SegregatePlugIn","CancellAll"),
    							I18N.get("SegregatePlugIn","Cancell"),
    							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)

    						break; 	
    				}
    			}
    		}
    		else{
    			doExecute=true;	
    		}
    	}

    	if(doExecute){

    		execute(new UndoableCommand(getName()) {

    			private ArrayList addedFeaturesLocal = new ArrayList(addedFeatures);
    			private Feature sourceFeatureLocal = sourceFeature;

    			public void execute() {

    				for (Iterator j = addedFeatures.iterator(); j.hasNext();) {
    					GeopistaFeature feature = (GeopistaFeature) j.next();
    					feature.getLayer().getFeatureCollectionWrapper().add(feature);

    				}
    				capa.getFeatureCollectionWrapper().remove(sourceFeatureLocal); 
    			}

    			public void unexecute() {

    				capa.getFeatureCollectionWrapper().removeAll(addedFeaturesLocal);                   
    				capa.getFeatureCollectionWrapper().add(sourceFeatureLocal);
    			}
    		}, context);

    	}

    }

    private boolean comprobarSuperposicion(TaskMonitor monitor, Collection features) {
        monitor.allowCancellationRequests();
        monitor.report(I18N.get("SegregatePlugIn","RealizandoSegregacion"));

        ArrayList featuresArrayList = new ArrayList(features);
        for (int n=0;n<featuresArrayList.size()-1;n++)
        {
          for(int p=n;p<featuresArrayList.size()-1;p++)
          {
            Feature temp1 = (Feature)featuresArrayList.get(p);
            Feature temp2 = (Feature)featuresArrayList.get(p+1);            
            Geometry geom1 = temp1.getGeometry();
            Geometry geom2 = temp2.getGeometry();
            boolean interseccion = geom1.overlaps(geom2);
            if(interseccion==true) return true;
          }
        }
       
        return false;
    }
    
    private boolean comprobarSumaTotalIgualOriginal(TaskMonitor monitor, Collection sourceSegregateGeometries,Geometry sourceGeometry) {

        monitor.allowCancellationRequests();
        monitor.report(I18N.get("SegregatePlugIn","RealizandoSegregacion"));

        Geometry currUnion = null;

        Iterator sourceSegregateGeometriesIter = sourceSegregateGeometries.iterator();
        int size = sourceSegregateGeometries.size();
        int count = 1;
        while(sourceSegregateGeometriesIter.hasNext())
        {
          Geometry actualGeometry = (Geometry) sourceSegregateGeometriesIter.next();
         
          if (currUnion == null) {
               currUnion = actualGeometry;
          } else {
            try
            {
                currUnion = currUnion.union(actualGeometry);
            }catch(IllegalArgumentException e)
            {
              
            }
          }
          monitor.report(count++, size, "features");
          
        }
        Geometry intersect = currUnion.intersection(sourceGeometry);

        if(intersect.isEmpty()) return false;
        
        return sourceGeometry.equals(intersect);

    }


    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);
// Debe haber features seleccionadas en dos capas y solo una de las capas debe estar seleccionada
// en la capa seleccionada está el origen y destino de las operaciones. Las otras geometrías definen
// el recorte.
//TODO: Se necesita un enablecheck para este caso concreto: ExactlyNFeaturesMustBeSelectedInSelectedLayer(int n)
        return new MultiEnableCheck()
           .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())          
			.add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1))
			.add(checkFactory.createAtLeastNFeaturesMustBeSelectedCheck(3));
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("Segregate.gif");
    }
    
    public void addButton(final ToolboxDialog toolbox)
    {
        if (!selectSegregateButtonAdded)
        {
        	SegregatePlugIn segregate = new SegregatePlugIn();                 
            toolbox.addPlugIn(segregate, null, segregate.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectSegregateButtonAdded = true;
        }
    }
    
    private void setSystemIDNoInitialize(GeopistaFeature feature){
    	
    	GeopistaFeature featuretemp = new GeopistaFeature(feature.getSchema());
    	feature.setSystemId(featuretemp.getSystemId());	         
                   	
    }
}
