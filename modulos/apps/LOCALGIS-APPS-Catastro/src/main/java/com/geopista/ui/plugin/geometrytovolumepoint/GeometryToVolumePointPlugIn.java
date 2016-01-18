/**
 * 
 */
package com.geopista.ui.plugin.geometrytovolumepoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.app.AppContext;
import  com.geopista.ui.plugin.geometrytovolumepoint.images.IconLoader;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;


/**
 * @author seilagamo
 *
 */
public class GeometryToVolumePointPlugIn extends AbstractPlugIn implements ThreadedPlugIn {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
//    private String toolBarCategory = "GeometryToVolumePointPlugIn.category";
    
    public GeometryToVolumePointPlugIn() {
        Locale loc = Locale.getDefault();
        ResourceBundle bundle2 = ResourceBundle.getBundle(
                "com.geopista.ui.plugin.geometrytovolumepoint.languages.GeometryToVolumePointi18n", loc,
                this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("GeometryToVolumePoint", bundle2);
    }    
    
    public void initialize(PlugInContext context) throws Exception {
        
        String pluginCategory = aplicacion.getString("toolBarAnalisys");
        //FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(pluginCategory)
                .addPlugIn(this.getIcon(), this, createEnableCheck(context.getWorkbenchContext()), 
                        context.getWorkbenchContext());
    }
    
    /**
     * Método que establece las condiciones para que se vea el plugin
     * 
     * @param workbenchContext
     * @return
     */
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {

        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck()).add(
                checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

    public Icon getIcon() {
        return IconLoader.icon("GeometryToVolumePoint.png");
    }

    public String getName() {
        String name = I18N.get("GeometryToVolumePoint", "geometryToVolumePointPlugIn");
        return name;
    }
    
    public boolean execute(PlugInContext context) throws Exception {
        
        GeometryToVolumePointDialog geometryToVolumePointDialog = new GeometryToVolumePointDialog(context);
        if (context.getLayerViewPanel() != null) {
            context.getLayerViewPanel().repaint();
        }
        return geometryToVolumePointDialog.wasOKPressed();
    }
    
    /**
     * Realiza el procesamiento que consume tiempo
     */
    public void run(final TaskMonitor monitor, PlugInContext context) throws Exception {
    	final PlugInContext localPluginContext = context;
        final Hashtable layersFeatures = new Hashtable();

        final boolean todasEntidadesSeleccionadas = localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToVolumePointVolumePanel.CHECKTODOSELEMENTOS, true);
        final Layer layerOrigen = (Layer) localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToVolumePointLayerPanel.SELECTEDSOURCELAYER);
        final Layer layerDestino = (Layer) localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToVolumePointLayerPanel.SELECTEDTARGETLAYER);
        final String atributoDestino = (String) localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToVolumePointLayerPanel.SELECTEDCAMPODESTINO);
        final String codigoAtributo = (String) localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToVolumePointVolumePanel.TEXTCODIGOATRIBUTO, "");

        execute(new UndoableCommand(getName()) {

            public void execute() {
                // Averiguamos si había elementos seleccionados en la capa
                boolean tieneEntidadesSeleccionadas = false;
                Collection featuresSeleccionadas = localPluginContext.getWorkbenchContext()
                        .getLayerViewPanel().getSelectionManager()
                        .getFeaturesWithSelectedItems(layerOrigen);

                Iterator featuresSeleccionadasIter = featuresSeleccionadas.iterator();
                if (featuresSeleccionadasIter.hasNext()) { // Esto es que tiene al
                                                           // menos una feature seleccionada
                    tieneEntidadesSeleccionadas = true;
                }
                Hashtable newGeometries = null;
                if (tieneEntidadesSeleccionadas) { // Si tiene entidades seleccionadas
                    newGeometries = convertGeometryToPoint(monitor, new ArrayList(
                            featuresSeleccionadas), layerOrigen);
                } else if (!tieneEntidadesSeleccionadas) { // Si no tiene ninguna entonces
                                                           // se cogen todas las de la capa
                    List allLayerFeatures = layerOrigen.getFeatureCollectionWrapper().getFeatures();
                    newGeometries = convertGeometryToPoint(monitor, allLayerFeatures, layerOrigen);
                }

                FeatureSchema featureSchemaTarget = layerDestino.getFeatureCollectionWrapper()
                        .getFeatureSchema();
                Enumeration enumeration = newGeometries.keys();
                FeatureDataset fc = new FeatureDataset(featureSchemaTarget);
                String codigoAtributoLocal = codigoAtributo;
                while (enumeration.hasMoreElements()) {
                    Feature f = (Feature) enumeration.nextElement();
                    Geometry geom = (Geometry) newGeometries.get(f);
                    if (!todasEntidadesSeleccionadas) {
                        codigoAtributoLocal = (String) localPluginContext
                                .getWorkbenchGuiComponent().getActiveTaskComponent()
                                .getLayerViewPanel().getBlackboard().get(
                                        Integer.toString(f.getID()), "");
                    }
                    if (geom != null) {
                        Feature fNew = new BasicFeature(fc.getFeatureSchema());
                        fNew.setGeometry(geom);
                        fNew.setAttribute(atributoDestino, codigoAtributoLocal);
                        fc.add(fNew);
                    }
                }
                layersFeatures.put(layerDestino, fc.getFeatures());
                layerDestino.getFeatureCollectionWrapper().addAll(fc.getFeatures());
                try {
                    localPluginContext.getLayerViewPanel().getViewport().update();
                } catch (Exception e) {

                }
            }

            public void unexecute() {
                layerDestino.getFeatureCollectionWrapper().removeAll(
                        (List) layersFeatures.get(layerDestino));
                try {
                    localPluginContext.getLayerViewPanel().getViewport().update();
                } catch (Exception e) {

                }
            }
        }, context);
    }
    
    /**
     * Calcula todos los centroides de las features y los devuelve en un Hashtable
     * @param monitor
     * @param allLayerFeatures
     * @param currentLayer
     * @return Un HashTable que contiene los centroides
     */
    private Hashtable convertGeometryToPoint(TaskMonitor monitor, List allLayerFeatures,
            Layer currentLayer) {

        monitor.allowCancellationRequests();
        monitor.report(I18N.get("GeometryToVolumePoint",
                "geometryToVolumePointPlugIn.CalculandoCentroides"));

        Hashtable newGeometries = new Hashtable();

        int size = allLayerFeatures.size();
        int count = 1;

        for (Iterator i = allLayerFeatures.iterator(); i.hasNext();) {
            monitor.report(count++, size, I18N.get("GeometryToVolumePoint",
                    "geometryToVolumePointPlugIn.features"));

            Feature f = (Feature) i.next();
            Geometry geom = f.getGeometry();
            if (geom.getCentroid() != null) {
                newGeometries.put(f, geom.getCentroid());
            }
        }

        return newGeometries;
    }

}
