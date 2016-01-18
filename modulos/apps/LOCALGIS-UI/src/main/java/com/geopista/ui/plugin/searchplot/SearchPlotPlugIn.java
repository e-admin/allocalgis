/**
 * SearchPlotPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.searchplot;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import com.geopista.app.AppContext;
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
