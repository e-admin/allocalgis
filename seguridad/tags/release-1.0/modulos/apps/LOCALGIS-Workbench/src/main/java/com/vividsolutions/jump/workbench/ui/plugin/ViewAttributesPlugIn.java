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
package com.vividsolutions.jump.workbench.ui.plugin;

import com.vividsolutions.jts.util.Assert;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.*;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.*;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.vividsolutions.jump.workbench.ui.*;




public class ViewAttributesPlugIn extends AbstractPlugIn {
    public ViewAttributesPlugIn() {
    }

    public String getName() {
        return "View / Edit Attributes";
    }

    public boolean execute(final PlugInContext context)
        throws Exception {
        reportNothingToUndoYet(context);

        //Don't add GeometryInfoFrame because the HTML will probably be too much for the
        //editor pane (too many features). [Jon Aquino]
        final ViewAttributesFrame frame = new ViewAttributesFrame(context);
        // REVISAR: Cast to WorkbenchFrame for the MDI model
        ((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);

        return true;
    }

    public MultiEnableCheck createEnableCheck(
        final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()//.add(checkFactory.createTaskWindowMustBeActiveCheck())
                                     .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(
                1));
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("Row.gif");
    }

    private class ViewAttributesFrame extends JInternalFrame
        implements LayerManagerProxy, SelectionManagerProxy,
            LayerNamePanelProxy, TaskFrameProxy, LayerViewPanelProxy {
        private LayerManager layerManager;
        private OneLayerAttributeTab attributeTab;

        public ViewAttributesFrame(PlugInContext context) {
            this.layerManager = (LayerManager)context.getLayerManager();
            addInternalFrameListener(new InternalFrameAdapter() {
                    public void internalFrameClosed(InternalFrameEvent e) {
                        //Assume that there are no other views on the model [Jon Aquino]
                        attributeTab.getModel().dispose();
                    }
                });
            setResizable(true);
            setClosable(true);
            setMaximizable(true);
            setIconifiable(true);
            getContentPane().setLayout(new BorderLayout());
            attributeTab = new OneLayerAttributeTab(context.getWorkbenchContext(),
                    //(TaskComponent) context.getActiveInternalFrame(), this).setLayer(context.getSelectedLayer(
            		(TaskComponent) context.getActiveTaskComponent(), this).setLayer((Layer)context.getSelectedLayer(
                        0));
            addInternalFrameListener(new InternalFrameAdapter() {
                    public void internalFrameOpened(InternalFrameEvent e) {
                        attributeTab.getToolBar().updateEnabledState();
                    }
                });
            getContentPane().add(attributeTab, BorderLayout.CENTER);
            setSize(500, 300);
            updateTitle(attributeTab.getLayer());
            context.getLayerManager().addLayerListener(new LayerListener() {
                    public void layerChanged(LayerEvent e) {
                        if (attributeTab.getLayer() != null) {
                            updateTitle(attributeTab.getLayer());
                        }
                    }

                    public void categoryChanged(CategoryEvent e) {
                    }

                    public void featuresChanged(FeatureEvent e) {
                    }
                });
            Assert.isTrue(!(this instanceof CloneableInternalFrame),
                "There can be no other views on the InfoModel");
        }

        public LayerViewPanel getLayerViewPanel() {
            return (LayerViewPanel)getTaskComponent().getLayerViewPanel();
        }

        public LayerManager getLayerManager() {
            return layerManager;
        }

        private void updateTitle(Layer layer) {
            setTitle((layer.isEditable() ? "Edit" : "View") + " Attributes: " +
                layer.getName());
        }

        public TaskComponent getTaskComponent() {
            return attributeTab.getTaskFrame();
        }

        public SelectionManager getSelectionManager() {
            return attributeTab.getPanel().getSelectionManager();
        }

        public LayerNamePanel getLayerNamePanel() {
            return attributeTab;
        }
     
        

    }
    
    public void initialize(PlugInContext context) throws Exception {

        JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
        .getGuiComponent()
        .getLayerNamePopupMenu();

        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                this, AppContext.getApplicationContext().getI18nString(this.getName()), false, null,
                this.createEnableCheck(context.getWorkbenchContext()));
                //null);
         

      }
}
