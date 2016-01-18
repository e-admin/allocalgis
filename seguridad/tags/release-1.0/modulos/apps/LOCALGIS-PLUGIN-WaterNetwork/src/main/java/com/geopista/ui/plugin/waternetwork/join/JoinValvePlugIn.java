package com.geopista.ui.plugin.waternetwork.join;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.geopista.app.AppContext;
import com.geopista.ui.plugin.waternetwork.images.IconLoader;
import com.geopista.ui.plugin.waternetwork.toolbox.WaterNetworkPlugIn;

public class JoinValvePlugIn extends AbstractPlugIn{
	
    private boolean joinValvePlugIn = false;
    public String getName() {return I18N.get("WaterNetworkPlugIn","JoinValve");}
    private JoinTools joinTools = new JoinTools();
    private String selectedLayer = I18N.get("WaterNetworkPlugIn","JoinValve.SelectedLayer");

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
        final LayerViewPanel layerViewPanel = (LayerViewPanel) context.getWorkbenchContext().getLayerViewPanel();
        final ArrayList<Feature> tuberias = new ArrayList<Feature>();
        final ArrayList<Feature> valvulas = new ArrayList<Feature>();
        layerViewPanel.getSelectionManager().clear();
        Collection<Layer> layers;
        layers = (Collection<Layer>) context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
        MultiInputDialog dialog = new MultiInputDialog(AppContext.getApplicationContext().getMainFrame(),
				"", true);
        setDialogValues(dialog,layers);
        dialog.setTitle(this.getName());
        dialog.setVisible(true);
        if (!dialog.wasOKPressed()){return false;}
        String layerName = dialog.getText(selectedLayer);
        for(Layer layer : layers){
	        if (layer.getName().equals(I18N.get("WaterNetworkPlugIn","JoinValve.PipesCopy"))
	        		||layer.getName().equals(I18N.get("WaterNetworkPlugIn","JoinValve.AuxLayer"))){
	            FeatureCollection featureCollection = layer.getFeatureCollectionWrapper();
	            for (Iterator<Feature> i = featureCollection.iterator(); i.hasNext();){
	                Feature feature = i.next();
	                tuberias.add(feature);
	            }
	        }
	        if(layer.getName().equals(layerName)){
	        	FeatureCollection featureCollection = layer.getFeatureCollectionWrapper();
	            for (Iterator<Feature> i = featureCollection.iterator(); i.hasNext();){
	                Feature feature = i.next();
	                valvulas.add(feature);
	            }
	        }
        }
        if (tuberias.size() > 0){ 
	        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(context.getWorkbenchFrame().getMainFrame(), null);
	        progressDialog.setTitle(I18N.get("WaterNetworkPlugIn","JoinValve.Loading"));
	        progressDialog.addComponentListener(new ComponentAdapter(){
	            public void componentShown(ComponentEvent e){
	                new Thread(new Runnable(){
	                    public void run(){
	                        try{
	                        	progressDialog.report(I18N.get("WaterNetworkPlugIn","JoinValve.Fusion"));
	                        	Layer layerAux = joinTools.fusionLayer(layerViewPanel, tuberias);
	                        	progressDialog.report(I18N.get("WaterNetworkPlugIn","JoinValve.Create"));
	                        	joinTools.newElements(layerViewPanel,layerAux, tuberias, valvulas, "valve");
	                        }catch(Exception e){
	                            ErrorDialog.show(context.getWorkbenchFrame().getMainFrame(), I18N.get("WaterNetworkPlugIn","JoinValve.TitleError"), I18N.get("WaterNetworkPlugIn","JoinValve.Error"), StringUtil.stackTrace(e));
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
	   return true;
    }

	private void setDialogValues(MultiInputDialog dialog,Collection<Layer> layers){
    	dialog.addComboBox(selectedLayer, null, layers, "");
    }

	public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext){
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
        		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }

    public Icon getIcon(){
        return IconLoader.icon("join_valve.png");
    }

    public void addButton(final ToolboxDialog toolbox){
        if (!joinValvePlugIn){
        	JoinValvePlugIn select = new JoinValvePlugIn();
            toolbox.addPlugIn(select, null, select.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            joinValvePlugIn = true;
        }
    }
}