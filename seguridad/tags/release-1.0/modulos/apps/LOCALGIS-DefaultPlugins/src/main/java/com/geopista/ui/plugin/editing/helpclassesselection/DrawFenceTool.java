package com.geopista.ui.plugin.editing.helpclassesselection;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.Icon;
import com.geopista.ui.cursortool.PolygonTool;
import com.geopista.ui.plugin.editing.helpclassesselection.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;


public class DrawFenceTool extends PolygonTool {
	private FenceDrawingUtil myfDrawingUtil;
	private PlugInContext context;
	
	private WorkbenchContext contextW;

	//sst: change: PlugInContext added
	protected DrawFenceTool(FenceDrawingUtil featureDrawingUtil, PlugInContext context) {
		this.myfDrawingUtil = featureDrawingUtil;
		this.context = context;
	}
	
	protected DrawFenceTool(FenceDrawingUtil featureDrawingUtil, WorkbenchContext context) {
		this.myfDrawingUtil = featureDrawingUtil;
		this.contextW = context;
	}
	
	//static method to use class (sst: change: PlugInContext added)
	public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy, PlugInContext context) {
		FenceDrawingUtil fDUtil = new FenceDrawingUtil(layerNamePanelProxy);
		
		return fDUtil.prepare(new DrawFenceTool(fDUtil, context),false); // no snap!
	}
	
	public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy, WorkbenchContext context) {
		FenceDrawingUtil fDUtil = new FenceDrawingUtil(layerNamePanelProxy);
		
		return fDUtil.prepare(new DrawFenceTool(fDUtil, context),false); // no snap!
	}

	/*public Icon getIcon() {
		//return IconLoader.icon("DrawPolygon.gif");
		return IconLoader.icon("");
	}*/
	
	public String getName(){
    	String name = I18N.get("SelectItemsByFenceFromSelectedLayersPlugIn","SelectItemsByFenceFromSelectedLayersPlugIn");
    	return name;
    }
    
    public Icon getIcon() {
        return IconLoader.icon("selecteditemsbyfence.gif");
    }

	protected void gestureFinished() throws Exception {
		reportNothingToUndoYet();

		//context.getLayerViewPanel().setCurrentCursorTool(this);
		
		if (!checkPolygon()){
			return;
			}
		
		int count = 0;
		
		if (!wasShiftPressed()) {
            getPanel().getSelectionManager().clear();
        }
		
		//Layer[] selectedLayers = context.getLayerNamePanel().getSelectedLayers();
		Layer[] selectedLayers = contextW.getLayerNamePanel().getSelectedLayers();
		for (int i = 0; i < selectedLayers.length; i++) {
			Layer actualLayer = selectedLayers[i]; 		
			//FeatureCollection fc = context.getSelectedLayer(i).getFeatureCollectionWrapper().getWrappee();
			FeatureCollection fc = actualLayer.getFeatureCollectionWrapper().getWrappee();
			Collection features = new ArrayList();
			for (Iterator iter = fc.iterator(); iter.hasNext();) {
				Feature element = (Feature) iter.next();
				if(!this.getPolygon().disjoint(element.getGeometry())){
					features.add(element);
					count++;
				}
			}
			//context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(actualLayer, features);
			contextW.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(actualLayer, features);
		}			    
		
	}

}
