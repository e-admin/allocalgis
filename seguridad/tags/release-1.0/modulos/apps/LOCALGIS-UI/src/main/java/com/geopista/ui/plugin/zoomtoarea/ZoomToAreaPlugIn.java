package com.geopista.ui.plugin.zoomtoarea;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.zoomtoarea.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class ZoomToAreaPlugIn extends AbstractPlugIn{

	private boolean selectZoomToAreaAdded = false;
	
	public ZoomToAreaPlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.zoomtoarea.languages.ZoomToAreaPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("ZoomToAreaPlugIn",bundle2);
    }
	
	public Icon getIcon() {
        return IconLoader.icon("ZoomToArea.gif");
    }
    
    public String getName(){
    	String name = I18N.get("ZoomToAreaPlugIn","ZoomToAreaPlugIn");
    	return name;
    }
        
    public void initialize(final PlugInContext context) throws Exception {    	

    	//Código necesario para incluir el PlugIn en la barra de herramientas
    	
    	context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar().addPlugIn(this.getIcon(),
            this,
            this.createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());   

    	////////////////////////////////////////////////////////////////
    	
//    	GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
//    	geopistaEditingPlugIn.addAditionalPlugIn(this);

    }  
    
    public void addButton(final ToolboxDialog toolbox)
    {
        if (!selectZoomToAreaAdded)
        {
            toolbox.addToolBar();            
            ZoomToAreaPlugIn zoomToArea = new ZoomToAreaPlugIn();                 
            toolbox.addPlugIn(zoomToArea, null, zoomToArea.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectZoomToAreaAdded = true;
        }
    } 
    
    public boolean execute(PlugInContext context) throws Exception {
    	
        ZoomToAreaDialog zoomToAreaPanel = new ZoomToAreaDialog(context);
    	
        if (context.getLayerViewPanel()!=null){
        	context.getLayerViewPanel().repaint();
        }
        
        return true;
    }
    

	public MultiEnableCheck createEnableCheck(
	        final WorkbenchContext workbenchContext) {
	        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

	        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
	                                     .add(checkFactory.createAtLeastNLayersMustExistCheck(
	                1));
	    }


}
