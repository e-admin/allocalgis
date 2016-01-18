package com.geopista.ui.plugin.searchplot;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.GeopistaListaMapasDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.searchplot.dialogs.SearchPlotDialog;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


public class SearchPlotPlugIn extends AbstractPlugIn 
{
	private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();


    public SearchPlotPlugIn()
        {
        }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
                .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
                .add(checkFactory.createLayerParcelasBeExistCheck());
    }

    public String getName()
    {
        return "SearchPlot";
    }

    public void initialize(PlugInContext context) throws Exception
    {
//        String pluginCategory = aplicacion.getString(toolBarCategory);
//        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent())
//                .getToolBar(pluginCategory).setTaskMonitorManager(
//                        new TaskMonitorManager());
//        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent())
//                .getToolBar(pluginCategory).addPlugIn(this.getIcon(), this, null,
//                        context.getWorkbenchContext());
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

        featureInstaller.addMainMenuItem(this,
               new String[]{"Tools", AppContext.getApplicationContext().getI18nString("SimpleQuery")},
               AppContext.getApplicationContext().getI18nString(this.getName()), 
               false,
               null,
               SearchPlotPlugIn.createEnableCheck(context.getWorkbenchContext()));

//        if (context.getWorkbenchGuiComponent() instanceof Frame)
//        {
//            if (owner == null)
//                owner = (Frame) context.getWorkbenchGuiComponent();
//        } else
//        {
//            owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
//                    (Component) context.getWorkbenchContext().getIWorkbench().getGuiComponent());
//        }
//        ListaMapasDialog = new GeopistaListaMapasDialog(owner);
    }

    public ImageIcon getIcon()
    {
        return IconLoader.icon("Open.gif");
    }

    public boolean execute(PlugInContext context) throws Exception {

        JDialog d = new SearchPlotDialog(context,aplicacion.getMainFrame());
        return true;
     }

}
