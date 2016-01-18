/**
 * EditWMS111QueryPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.wms;

import java.awt.Dimension;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.io.datasource.wms.WMService;
import com.geopista.model.WMSLayerImpl;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.wms.MapLayer;


public class EditWMS111QueryPlugIn extends AbstractPlugIn {
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    
    public MultiEnableCheck createEnableCheck(
        final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                     .add(checkFactory.createExactlyNLayerablesMustBeSelectedCheck(
                1, WMSLayerImpl.class));
    }

    public boolean execute(PlugInContext context) throws Exception {
        WMSLayerImpl layer = (WMSLayerImpl) context.getLayerNamePanel()
                                           .selectedNodes(WMSLayerImpl.class)
                                           .iterator().next();
        MultiInputDialog dialog = new MultiInputDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
                "Edit WMS Query", true);
        dialog.setInset(0);
        dialog.setSideBarImage(IconLoader.icon("EditWMSLayer.jpg"));
        dialog.setSideBarDescription(
            "This dialog enables you to change the layers being retrieved from a Web Map Server.");

        WMService service = new WMService(layer.getServerURL());
        service.initialize();

        EditWMSQueryPanel panel = new EditWMSQueryPanel(service,
                layer.getLayerNames(), layer.getSRS(), layer.getAlpha());
        panel.setPreferredSize(new Dimension(600, 450));

        //The field name "Chosen Layers" will appear on validation error messages
        //e.g. if the user doesn't pick any layers. [Jon Aquino]
        dialog.addRow("Chosen Layers", new JLabel(""), panel, panel.getEnableChecks(), null);
        dialog.setVisible(true);

        if (dialog.wasOKPressed()) {
            layer.removeAllLayerNames();

            for (Iterator i = panel.getChosenMapLayers().iterator();
                    i.hasNext();) {
                MapLayer mapLayer = (MapLayer) i.next();
                layer.addLayerName(mapLayer.getName());
            }

            layer.setSRS(panel.getSRS());
            layer.setAlpha(panel.getAlpha());
            layer.fireAppearanceChanged();

            return true;
        }

        return false;
    }

    public void initialize(PlugInContext context) throws Exception {

      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

      JPopupMenu wmsLayerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
                                                           .getGuiComponent()
                                                           .getWMSLayerNamePopupMenu();

            featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
            this, aplicacion.getI18nString(this.getName()) + "...", false,
            null, this.createEnableCheck(context.getWorkbenchContext()));
    }

}
