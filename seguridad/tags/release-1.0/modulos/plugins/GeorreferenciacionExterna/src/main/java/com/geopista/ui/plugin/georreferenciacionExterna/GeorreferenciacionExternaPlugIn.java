package com.geopista.ui.plugin.georreferenciacionExterna;

import java.awt.Frame;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;

import reso.jump.joinTable.JoinTable;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.georreferenciacionExterna.dialog.GeorreferenciaExternaConsultaDialog;
import com.geopista.ui.plugin.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class GeorreferenciacionExternaPlugIn extends ThreadedBasePlugIn 
{
	private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();
    private Blackboard blackboard = aplicacion.getBlackboard();   
    
    private String toolBarCategory = "GeopistaLoadMapPlugIn.category";
    public static final String CAPAPLANTILLA = "SeriePrintPlugIn.CapaPlantilla";

    //VARIABLES NECESARIAS QUE SE PASAN DESDE LOS PANELES
    GeopistaLayer capa=null;
    String select=null;
    JoinTable jt=null;    
    //FIN
    Frame owner = null;  
    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
                .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck());
    	
    }
    public void initialize(PlugInContext context) throws Exception
    {
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georreferenciacionExterna.languages.GeorreferenciacionExternai18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("GeorreferenciacionExterna",bundle);
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(this,
                new String[]{"Tools", AppContext.getApplicationContext().getI18nString("ui.MenuNames.TOOLS.ANALYSIS"), I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.labelGeoreference")},
                I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.menu.title"), 
                false,
                null,
                GeorreferenciacionExternaPlugIn.createEnableCheck(context.getWorkbenchContext()));
        
        
        context.getFeatureInstaller().addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
                .getGuiComponent()
                .getCategoryPopupMenu(),
    			this,
        		  I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.popup.anadircapa") ,
        		  false,null,GeorreferenciacionExternaPlugIn.createEnableCheck(context.getWorkbenchContext()));
        
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
                this,GeorreferenciacionExternaPlugIn.createEnableCheck(context.getWorkbenchContext()),context.getWorkbenchContext());
        
    }
    
    public boolean execute(PlugInContext context) throws Exception
    {
    	
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georreferenciacionExterna.languages.GeorreferenciacionExternai18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("GeorreferenciacionExterna",bundle);    	

    	desktopPane = new javax.swing.JDesktopPane();

        String user = AppContext.getApplicationContext().getString(AppContext.USER_LOGIN);
    	JDialog d = new GeorreferenciaExternaConsultaDialog(context, user);
    	
        return true;
    }
    
    private javax.swing.JDesktopPane desktopPane;
    
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
    
    
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public ImageIcon getIcon()
    {
        return IconLoader.icon("btn_georeferenciaExt.png");
    }
 
}