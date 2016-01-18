/**
 * ZoomToFullExtentPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 10-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui.plugin.zoom;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


public class ZoomToFullExtentPlugIn extends AbstractPlugIn {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    public ZoomToFullExtentPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        context.getLayerViewPanel().getViewport().zoomToFullExtent();

        return true;
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("World.gif");
    }

    public MultiEnableCheck createEnableCheck(
        final WorkbenchContext workbenchContext) {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                                     .add(checkFactory.createAtLeastNLayersMustExistCheck(
                1));
    }

    public void initialize(PlugInContext context) throws Exception {

      context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar().addPlugIn(this.getIcon(),
            this,
            this.createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());
      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
      featureInstaller.addMainMenuItem(this, aplicacion.getI18nString("View"),
              GeopistaUtil.i18n_getname(this.getName()),
              GUIUtil.toSmallIcon(this.getIcon()),
              this.createEnableCheck(context.getWorkbenchContext()));

    }
}
