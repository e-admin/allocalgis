/**
 * InstallDatastoreLayerRendererHintsPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.datastore;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerNameRenderer;
import com.vividsolutions.jump.workbench.ui.renderer.LayerRenderer;
import com.vividsolutions.jump.workbench.ui.renderer.RenderingManager;

public class InstallDatastoreLayerRendererHintsPlugIn extends AbstractPlugIn {
    public void initialize(PlugInContext context) throws Exception {
        for (Iterator i = Arrays.asList(
                ((JDesktopPane) context.getWorkbenchFrame().getDesktopPane()).getAllFrames())
                .iterator(); i.hasNext();) {
            JInternalFrame internalFrame = (JInternalFrame) i.next();
            installDatastoreLayerRendererHintsIfNecessary(internalFrame);
        }
        GUIUtil.addInternalFrameListener((JDesktopPane) context.getWorkbenchFrame()
                .getDesktopPane(), new InternalFrameAdapter() {
            public void internalFrameActivated(InternalFrameEvent e) {
                installDatastoreLayerRendererHintsIfNecessary(e
                        .getInternalFrame());
            }
        });
    }

    private Collection installedLayerManagers = new HashSet();

    private static final String INSTALLED_KEY = InstallDatastoreLayerRendererHintsPlugIn.class
            .getName()
            + " - INSTALLED";

    private void installDatastoreLayerRendererHintsIfNecessary(
            JInternalFrame internalFrame) {
        if (!(internalFrame instanceof LayerManagerProxy)) {
            return;
        }
        installDatastoreLayerRendererHintsIfNecessary((LayerManager)((LayerManagerProxy) internalFrame)
                .getLayerManager());
    }

    private void installDatastoreLayerRendererHintsIfNecessary(
            LayerManager layerManager) {
        if (installedLayerManagers.contains(layerManager)) {
            return;
        }
        layerManager.addLayerListener(new LayerListener() {

            public void featuresChanged(FeatureEvent e) {
            }

            public void layerChanged(LayerEvent e) {
                if (!(e.getLayerable() instanceof Layer)) {
                    return;
                }
                if (((Layer) e.getLayerable()).getDataSourceQuery() == null) {
                    return;
                }
                if (!(((Layer) e.getLayerable()).getDataSourceQuery()
                        .getDataSource() instanceof DataStoreDataSource)) {
                    return;
                }
                if (((Layer) e.getLayerable()).getBlackboard().get(
                        INSTALLED_KEY, false)) {
                    return;
                }
                // CachedDynamicFeatureCollection#size is not up to
                // date (it is cached), so force image caching to always
                // be used, as its use depends on #size.
                // [Jon Aquino 2005-03-16]
                ((Layer) e.getLayerable()).getBlackboard().put(
                        LayerRenderer.ALWAYS_USE_IMAGE_CACHING_KEY, true);
                ((Layer) e.getLayerable()).getBlackboard().put(
                        RenderingManager.USE_MULTI_RENDERING_THREAD_QUEUE_KEY,
                        true);
                ((Layer) e.getLayerable()).getBlackboard().put(
                        LayerNameRenderer.USE_CLOCK_ANIMATION_KEY, true);
                ((Layer) e.getLayerable()).getBlackboard().put(INSTALLED_KEY,
                        true);
                // Get here *after* the first paint, so repaint.
                // [Jon Aquino 2005-03-16]
                ((Layer) e.getLayerable()).fireAppearanceChanged();
            }

            public void categoryChanged(CategoryEvent e) {
            }
        });
        installedLayerManagers.add(layerManager);
    }
}