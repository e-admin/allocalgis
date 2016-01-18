package com.geopista.ui.plugin.buscadorcallejero;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.buscadorcallejero.dialog.BuscadorCallejeroDialog;
import com.geopista.ui.plugin.buscadorcallejero.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class BuscadorCallejeroPlugIn extends ThreadedBasePlugIn{

	
	 private javax.swing.JDesktopPane desktopPane;
	 private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	 private String toolBarCategory = "GeopistaLoadMapPlugIn.category";
	
	  public void initialize(PlugInContext context) throws Exception
	    {
	    	Locale loc=I18N.getLocaleAsObject();      
	    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.buscadorcallejero.languages.BuscadorCallejeroi18n",loc,this.getClass().getClassLoader());
	    	I18N.plugInsResourceBundle.put("BuscadorCallejero",bundle);
	        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	       /* featureInstaller.addMainMenuItem(this,
	                new String[]{"Tools", AppContext.getApplicationContext().getI18nString("ui.MenuNames.TOOLS.ANALYSIS"), I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.labelGeoreference")},
	                I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.menu.title"), 
	                false,
	                null,
	                GeorreferenciacionExternaPlugIn.createEnableCheck(context.getWorkbenchContext()));
	        */
	        
	       /* context.getFeatureInstaller().addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
	                .getGuiComponent()
	                .getCategoryPopupMenu(),
	    			this,
	        		  I18N.get("BuscadorCallejero","buscadorCallejero.popup.buscador") ,
	        		  false,null,BuscadorCallejeroPlugIn.createEnableCheck(context.getWorkbenchContext()));
	        */
	        String pluginCategory = aplicacion.getString(toolBarCategory);
	        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
	                this,BuscadorCallejeroPlugIn.createEnableCheck(context.getWorkbenchContext()),context.getWorkbenchContext());
	        
	    }
	
	 private boolean mostrarJInternalFrame(JInternalFrame internalFrame) {

		try {

			int numInternalFrames=desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(internalFrame)).length;
			//logger.info("numInternalFrames: "+numInternalFrames);

			if (numInternalFrames==0){
				internalFrame.setFrameIcon(new javax.swing.ImageIcon("img"+File.separator+"geopista.gif"));

				desktopPane.add(internalFrame);
				internalFrame.setMaximum(true);
				internalFrame.show();
			}else{
				//logger.info("cannot open another JInternalFrame");
			}

		} catch (Exception ex) {
			//logger.warn("Exception: " + ex.toString());
		}

		return true;
	}

	public boolean execute(PlugInContext context) throws Exception
    {
    	
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.buscadorcallejero.languages.BuscadorCallejeroi18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("BuscadorCallejero",bundle);    	

    	desktopPane = new javax.swing.JDesktopPane();

        String user = AppContext.getApplicationContext().getString(AppContext.USER_LOGIN);
    	JDialog d = new BuscadorCallejeroDialog(context, user);
    	
        return true;
    }
	
	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
		        .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
				.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
				.add(checkFactory.createAtLeastNLayersMustExistCheck(1));    
        
    }
	
	public ImageIcon getIcon()
    {
        return IconLoader.icon("buscadorCallejero.png");
    }
	
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
