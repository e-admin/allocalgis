/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.workbench.ui.plugin.wms;

import java.awt.Dimension;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.wms.MapLayer;
import com.vividsolutions.wms.WMService;


public class EditWMSQueryPlugIn extends AbstractPlugIn {
    public MultiEnableCheck createEnableCheck(
        final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                     .add(checkFactory.createExactlyNLayerablesMustBeSelectedCheck(
                1, Layerable.class));
    }

    public boolean execute(PlugInContext context) throws Exception {
        WMSLayer layer = (WMSLayer) context.getLayerNamePanel()
                                           .selectedNodes(WMSLayer.class)
                                           .iterator().next();
        MultiInputDialog dialog = new MultiInputDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
                "Edit WMS Query", true);
        dialog.setInset(0);
        dialog.setSideBarImage(IconLoader.icon("EditWMSLayer.jpg"));
        dialog.setSideBarDescription(
            "This dialog enables you to change the layers being retrieved from a Web Map Server.");
WMService service=layer.getService();
     /**   WMService service = new WMService(layer.getServerURL());
       try{
       service.initialize();
       }catch(Exception e)
       {
       e.printStackTrace();
       }
*/ 		
		
        EditWMSQueryPanel panel = new EditWMSQueryPanel(service,
                layer.getLayerNames(), layer.getSRS(), layer.getAlpha(),layer.getFormat(),
                layer.getSelectedStyles());
        panel.setPreferredSize(new Dimension(700, 500));

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

            
            layer.setSRS(panel.getEPSG());
            //layer.setSRS(panel.getSRS());
            layer.setAlpha(panel.getAlpha());
            layer.setFormat(panel.getFormat());
            panel.updateStyles();
            layer.fireAppearanceChanged();
            
                 return true;
        }

        return false;
    }//fin del método execute
    
    public void initialize(PlugInContext context) throws Exception {

    FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

    JPopupMenu wmsLayerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
                                                         .getGuiComponent()
                                                         .getWMSLayerNamePopupMenu();

          featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
          this, I18N.get(this.getName()) + "...", false,
          null, this.createEnableCheck(context.getWorkbenchContext()));
  }
}
