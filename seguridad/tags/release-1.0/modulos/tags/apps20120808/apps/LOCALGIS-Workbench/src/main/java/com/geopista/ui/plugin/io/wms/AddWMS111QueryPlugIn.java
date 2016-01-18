/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/
package com.geopista.ui.plugin.io.wms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.io.datasource.wms.WMService;
import com.geopista.model.WMSLayerImpl;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.plugin.wms.MapLayerWizardPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.OneSRSWizardPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.SRSWizardPanel;
import com.vividsolutions.wms.MapLayer;




public class AddWMS111QueryPlugIn extends AbstractPlugIn {
    private String cachedURL = "http://";
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  

    public AddWMS111QueryPlugIn() {
    }

    private List toLayerNames(List mapLayers) {
        ArrayList names = new ArrayList();
        for (Iterator i = mapLayers.iterator(); i.hasNext();) {
            MapLayer layer = (MapLayer) i.next();
            names.add(layer.getName());
        }

        return names;
    }

    public boolean execute(final PlugInContext context)
        throws Exception {
        reportNothingToUndoYet(context);

        WizardDialog d = new WizardDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
                aplicacion.getI18nString("ConectarAlWebMapServer"), context.getErrorHandler());
        d.init(new com.geopista.ui.wizard.WizardPanel[] {
                new URLWizardPanel(cachedURL), new MapLayerWizardPanel(),
                new SRSWizardPanel(), new OneSRSWizardPanel()
            });

        //Set size after #init, because #init calls #pack. [Jon Aquino]
        d.setSize(500, 400);
        GUIUtil.centreOnWindow(d);
        d.setVisible(true);
        if (!d.wasFinishPressed()) {
            return false;
        }

        final WMSLayerImpl layer = new WMSLayerImpl(context.getLayerManager(),
                ((WMService) d.getData(URLWizardPanel.SERVICE_KEY)).getServerUrl(),
                (String) d.getData(SRSWizardPanel.SRS_KEY),
                toLayerNames((List) d.getData(MapLayerWizardPanel.LAYERS_KEY)),
                ((String) d.getData(URLWizardPanel.FORMAT_KEY)));
        execute(new UndoableCommand(getName()) {
                public void execute() {
                    Collection selectedCategories = context.getLayerNamePanel()
                                                           .getSelectedCategories();
                    context.getLayerManager().addLayerable(selectedCategories.isEmpty()
                        ? StandardCategoryNames.WORKING
                        : selectedCategories.iterator().next().toString(), layer);
                }

                public void unexecute() {
                    context.getLayerManager().remove(layer);
                }
            }, context);
        cachedURL = (String) d.getData(URLWizardPanel.URL_KEY);

        return true;
    }

    public void initialize(PlugInContext context) throws Exception {

      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
  
      featureInstaller.addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
                                                          .getGuiComponent()
                                                          .getCategoryPopupMenu(),
            this, I18N.get(this.getName()) + "...", false,
            null, null);

     featureInstaller.addLayerViewMenuItem(this,  
     		new String[]{MenuNames.LAYER,
    				"ImageCoverage"}, 
					I18N.get(this.getName()));            
    }

}
