/**
 * JoinPipePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.waternetwork.join;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.GeopistaAddNewLayerPlugIn;
import com.geopista.ui.plugin.waternetwork.images.IconLoader;
import com.geopista.ui.plugin.waternetwork.toolbox.WaterNetworkPlugIn;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class JoinPipePlugIn extends AbstractPlugIn{

    private boolean selectLineString = true;
    private boolean selectMultiLineString = true;
    private boolean pipesUnionPlugIn = false;
    public String getName() {return I18N.get("WaterNetworkPlugIn","JoinPipe");}
    private JoinTools joinTools = new JoinTools();
    private String distances = I18N.get("WaterNetworkPlugIn","JoinPipe.Distance");
    private String selectedLayer = I18N.get("WaterNetworkPlugIn","JoinPipe.SelectedLayer");

    @SuppressWarnings("unchecked")
	public void initialize(PlugInContext context) throws Exception{
    	Locale loc=Locale.getDefault();
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.waternetwork.languages.WaterNetworkPlugIni18n",loc,
				this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("WaterNetworkPlugIn",bundle2);
        String pathMenuNames[] =new String[] { MenuNames.EDIT };
		String name = I18N.get("WaterNetworkPlugIn",getName());
        context.getFeatureInstaller().addMainMenuItem(this, pathMenuNames, name,
    			false, null, createEnableCheck(context.getWorkbenchContext()));
        WaterNetworkPlugIn moduloAguasPI = (WaterNetworkPlugIn) (context.getWorkbenchContext().getBlackboard().get(WaterNetworkPlugIn.KEY));
        moduloAguasPI.addAditionalPlugIn(this);
    }

	@SuppressWarnings("unchecked")
	public boolean execute(final PlugInContext context) throws Exception{    	
		reportNothingToUndoYet(context);
		final Collection<Layer> layers = (Collection<Layer>) context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
        MultiInputDialog dialog = new MultiInputDialog(AppContext.getApplicationContext().getMainFrame(),
				"", true);
        setDialogValues(dialog,layers);
        dialog.setTitle(this.getName());
        dialog.setVisible(true);
        if (!dialog.wasOKPressed()){return false;}
        final LayerViewPanel layerViewPanel = (LayerViewPanel) context.getWorkbenchContext().getLayerViewPanel();
        final ArrayList<Feature> selectedFeatures = new ArrayList<Feature>();
        layerViewPanel.getSelectionManager().clear();
        final double distance = dialog.getDouble(distances);
        String layerName = dialog.getText(selectedLayer);
        for(final Layer layer : layers){
            selectedFeatures.clear();
            if (layer.getName().equals(layerName)){
                FeatureCollection featureCollection = layer.getFeatureCollectionWrapper();
                for (Iterator<Feature> i = featureCollection.iterator(); i.hasNext();){
                    Feature feature = i.next();
                    if (selectFeature(feature))
                        selectedFeatures.add(feature);
                }
            }
            if (selectedFeatures.size() > 0){ 
                final TaskMonitorDialog progressDialog= new TaskMonitorDialog(context.getWorkbenchFrame().getMainFrame(), null);
                progressDialog.setTitle(I18N.get("WaterNetworkPlugIn","JoinPipe.Loading"));
                progressDialog.addComponentListener(new ComponentAdapter(){
                    public void componentShown(ComponentEvent e){
                        new Thread(new Runnable(){
                            public void run(){
                                try{
                                	boolean copyLayer = false;
                                    boolean auxiliarLayer = false;
                                    for(Layer l : layers){
                                    	if(l.getName().equals(I18N.get("WaterNetworkPlugIn","JoinReservoir.PipesCopy")))copyLayer = true;
                                    	else if(l.getName().equals(I18N.get("WaterNetworkPlugIn","JoinSource.AuxLayer")))auxiliarLayer = true;
                                    }
                                    Layer newLayer,auxLayer = null;
                                	if(!copyLayer){
	                                    newLayer = layerViewPanel.getLayerManager().addLayer(StandardCategoryNames.WORKING,
	                                			I18N.get("WaterNetworkPlugIn","JoinReservoir.PipesCopy"),
	                            				GeopistaAddNewLayerPlugIn.createBlankFeatureCollection());
	                            		newLayer.setVisible(true);
	                            		newLayer.setEditable(true);
	                            		newLayer.getFeatureCollectionWrapper().addAll(selectedFeatures);
                                	}else newLayer = layerViewPanel.getLayerManager().getLayer(I18N.get("WaterNetworkPlugIn","JoinReservoir.PipesCopy"));
                                	if(!auxiliarLayer){
	                            		auxLayer = layerViewPanel.getLayerManager().addLayer(StandardCategoryNames.WORKING,
	                            				I18N.get("WaterNetworkPlugIn","JoinReservoir.AuxLayer"),
	                            				GeopistaAddNewLayerPlugIn.createBlankFeatureCollection());
	                            		auxLayer.setVisible(true);
	                            		auxLayer.setEditable(true);
                                	}else auxLayer = layerViewPanel.getLayerManager().getLayer(I18N.get("WaterNetworkPlugIn","JoinReservoir.AuxLayer"));
                                	progressDialog.report(I18N.get("WaterNetworkPlugIn","JoinPipe.Create"));
                                	joinTools.newPipes(layerViewPanel,newLayer,auxLayer,selectedFeatures, distance);
                                }catch(Exception e){
                                    ErrorDialog.show(context.getWorkbenchFrame().getMainFrame(), I18N.get("WaterNetworkPlugIn","JoinPipe.TitleError"), I18N.get("WaterNetworkPlugIn","JoinPipe.Error"), StringUtil.stackTrace(e));
                                    return;
                                }finally{
                                    progressDialog.setVisible(false);
                                }
                            }
                      }).start();
                  }
               });
               GUIUtil.centreOnWindow(progressDialog);
               progressDialog.setVisible(true);
			}
        }
        return true;
    }

    private boolean selectFeature(Feature feature){        
        if ((feature.getGeometry() instanceof LineString) && selectLineString) return true;
		if ((feature.getGeometry() instanceof MultiLineString) && selectMultiLineString) return true;
        return false;
    }

    private void setDialogValues(MultiInputDialog dialog,Collection<Layer> layers){
    	dialog.addComboBox(selectedLayer, null, layers, "");
        dialog.addTextField(distances,"",5,null,"");
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext){
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
        		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }

    public void addButton(final ToolboxDialog toolbox){
        if (!pipesUnionPlugIn){
        	toolbox.addToolBar();
        	JoinPipePlugIn select = new JoinPipePlugIn();
            toolbox.addPlugIn(select, null, select.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            pipesUnionPlugIn = true;
        }
    }

    public Icon getIcon(){
        return IconLoader.icon("join_pipe.png");
    }
}