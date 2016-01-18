package com.geopista.ui.plugin.grid;


import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.grid.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class GridOptionsPanelPlugIn extends AbstractPlugIn {
	
	private boolean selectGridButtonAdded = false;
	    
    public GridOptionsPanelPlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.grid.languages.GridOptionsPaneli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("GridOptionsPanel",bundle2);
    }

    public boolean execute(PlugInContext context) throws Exception {
                    	
        GridOptionsPanel gridOptionsPanel = new GridOptionsPanel(context);
    	
        if (context.getLayerViewPanel()!=null){
        	context.getLayerViewPanel().repaint();
        }
        
        return true;
    }

    public Icon getIcon() {
        return IconLoader.icon("Grid.gif");
    }
    
    public String getName(){
    	String name = I18N.get("GridOptionsPanel","GridOptionsPanel");
    	return name;
    }

    public MultiEnableCheck createEnableCheck(
        final WorkbenchContext workbenchContext) {
        
    	return null;
        
    }

    public void initialize(final PlugInContext context) throws Exception {    	

    	//Código necesario para incluir el PlugIn en la barra de herramientas
    	
    	/*context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar().addPlugIn(this.getIcon(),
            this,
            this.createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());  */ 

    	GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
    	geopistaEditingPlugIn.addAditionalPlugIn(this);

    }    
    
    public void addButton(final ToolboxDialog toolbox)
    {
        if (!selectGridButtonAdded)
        {
            toolbox.addToolBar();            
            GridOptionsPanelPlugIn grid = new GridOptionsPanelPlugIn();                 
            toolbox.addPlugIn(grid, null, grid.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectGridButtonAdded = true;
        }
    } 
    

}
