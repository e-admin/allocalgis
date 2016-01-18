package com.geopista.ui.plugin.selectbytype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.app.AppContext;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.selectbytype.images.IconLoader;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;


public class SelectByTypePlugIn extends AbstractPlugIn
{
      
    private boolean selectPoint = false;
    private boolean selectMultiPoint = false;
    private boolean selectLineString = false;
    private boolean selectLinearRing = false;
    private boolean selectMultiLineString = false;
    private boolean selectPolygon = false;
    private boolean selectMultiPolygon = false;
    private boolean selectGeometryCollection = false;
    protected IAbstractSelection selection;    
	
    private boolean selectByTypePlugIn = false;
    
    public static final String toolBarCategory = "SelectByType";
    
    public void initialize(PlugInContext context) throws Exception
    {     
    	Locale loc=Locale.getDefault();
   	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.selectbytype.languages.SelectByTypePlugIni18n",loc,this.getClass().getClassLoader());
    	
        I18N.plugInsResourceBundle.put("SelectByTypePlugIn",bundle2);
    	
        String pathMenuNames[] =new String[] { MenuNames.EDIT };
        
		String name = I18N.get("SelectByTypePlugIn",getName().replaceAll(" ", ""));
        context.getFeatureInstaller().addMainMenuItem(this, pathMenuNames, name,
    			false, null,
    			createEnableCheck(context.getWorkbenchContext()));
    				
        GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
    	geopistaEditingPlugIn.addAditionalPlugIn(this);
             
      
    }
    
    public boolean execute(final PlugInContext context) throws Exception
    {    	
    	reportNothingToUndoYet(context);
    	
    	String nameDialog = I18N.get("SelectByTypePlugIn","SelectByType");
    	
        MultiInputDialog dialog = new MultiInputDialog(
        //context.getWorkbenchFrame(), "", true);
        AppContext.getApplicationContext().getMainFrame(), "", true);
        setDialogValues(dialog, context);        
        dialog.setTitle(nameDialog);
        dialog.setVisible(true);
        
        if (! dialog.wasOKPressed()) {return false;}
        
        getDialogValues(dialog);        
        ILayerViewPanel layerViewPanel = context.getWorkbenchContext().getLayerViewPanel();
        ArrayList selectedFeatures = new ArrayList();
        
        layerViewPanel.getSelectionManager().clear();
        Collection layers;
                
        layers = (Collection) context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
            
        for (Iterator j = layers.iterator(); j.hasNext();) 
        {
            Layer layer = (Layer) j.next();
            selectedFeatures.clear();
            
            if (layer.isVisible())
            {
                FeatureCollection featureCollection = layer.getFeatureCollectionWrapper();
                
                for (Iterator i = featureCollection.iterator(); i.hasNext();)
                {
                    Feature feature = (Feature) i.next();
                    if (selectFeature(feature))
                    {

                        selectedFeatures.add(feature);
                    }
                }
            }
            if (selectedFeatures.size() > 0)
                layerViewPanel.getSelectionManager().getFeatureSelection().selectItems(layer, selectedFeatures);
        }        
        return true;
    }
      
    private boolean selectFeature(Feature feature)
    {
       
        if ((feature.getGeometry() instanceof Point) && selectPoint) return true;
        if ((feature.getGeometry() instanceof MultiPoint) && selectMultiPoint) return true;
        if ((feature.getGeometry() instanceof LineString) && selectLineString) return true;
        if ((feature.getGeometry() instanceof LinearRing) && selectLinearRing) return true;
        if ((feature.getGeometry() instanceof MultiLineString) && selectMultiLineString) return true;
        if ((feature.getGeometry() instanceof Polygon) && selectPolygon) return true;
        if ((feature.getGeometry() instanceof MultiPolygon) && selectMultiPolygon) return true;
        if ((feature.getGeometry() instanceof GeometryCollection) && selectGeometryCollection) return true;
        return false;
    }
    
    private void setDialogValues(MultiInputDialog dialog, PlugInContext context)
    {    	
    	String sSelectOnlyTheseTypes = I18N.get("SelectByTypePlugIn","SelectByType.Select");
        dialog.addLabel(sSelectOnlyTheseTypes);       
        dialog.addCheckBox(I18N.get("SelectByTypePlugIn","Point"), selectPoint);
        dialog.addCheckBox(I18N.get("SelectByTypePlugIn","MultiPoint"), selectMultiPoint);
        dialog.addCheckBox(I18N.get("SelectByTypePlugIn","LineString"), selectLineString);
        dialog.addCheckBox(I18N.get("SelectByTypePlugIn","LinearRing"), selectLinearRing);
        dialog.addCheckBox(I18N.get("SelectByTypePlugIn","MultiLineString"), selectMultiLineString);
        dialog.addCheckBox(I18N.get("SelectByTypePlugIn","Polygon"), selectPolygon);
        dialog.addCheckBox(I18N.get("SelectByTypePlugIn","MultiPolygon"), selectMultiPolygon);
        dialog.addCheckBox(I18N.get("SelectByTypePlugIn","GeometryCollection"), selectGeometryCollection);
        
    }
    
    private void getDialogValues(MultiInputDialog dialog)
    {
        
        selectPoint = dialog.getCheckBox(I18N.get("SelectByTypePlugIn","Point")).isSelected();
        selectMultiPoint = dialog.getCheckBox(I18N.get("SelectByTypePlugIn","MultiPoint")).isSelected();
        selectLineString = dialog.getCheckBox(I18N.get("SelectByTypePlugIn","LineString")).isSelected();
        selectLinearRing = dialog.getCheckBox(I18N.get("SelectByTypePlugIn","LinearRing")).isSelected();
        selectMultiLineString = dialog.getCheckBox(I18N.get("SelectByTypePlugIn","MultiLineString")).isSelected();
        selectPolygon = dialog.getCheckBox(I18N.get("SelectByTypePlugIn","Polygon")).isSelected();
        selectMultiPolygon = dialog.getCheckBox(I18N.get("SelectByTypePlugIn","MultiPolygon")).isSelected();
        selectGeometryCollection = dialog.getCheckBox(I18N.get("SelectByTypePlugIn","GeometryCollection")).isSelected();
        
    }
       
    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) 
    {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
        		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }   
   
    public void addButton(final ToolboxDialog toolbox)
    {
        if (!selectByTypePlugIn)
        {            
        	toolbox.addToolBar();
            SelectByTypePlugIn select = new SelectByTypePlugIn();                 
            toolbox.addPlugIn(select, null, select.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectByTypePlugIn = true;
        }
    }
    
    public Icon getIcon() {
        return IconLoader.icon("Select.gif");
    }
}

