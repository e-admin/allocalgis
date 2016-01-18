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
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.undo.UndoManager;

import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
public class UndoPlugIn extends AbstractPlugIn {
    public UndoPlugIn() {}
    public void initialize(PlugInContext context) throws Exception {
       context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar().addPlugIn(this.getIcon(), this,
            this.createEnableCheck(context.getWorkbenchContext()), context.getWorkbenchContext());
       FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
       featureInstaller.addMainMenuItem(this, GeopistaFunctionUtils.i18n_getname("Edit"),
    		   GeopistaFunctionUtils.i18n_getname(this.getName()), GUIUtil.toSmallIcon(this.getIcon()),
            createEnableCheck(context.getWorkbenchContext()));
    }
    
    public boolean execute(PlugInContext context) throws Exception {
        
    	LayerManager layerManager = (LayerManager)context.getWorkbenchContext()
			.getIWorkbench()
			.getGuiComponent()
			.getContext()
			.getLayerManager();
    	/*LayerManager layerManager = ((LayerManagerProxy) context.getWorkbenchContext()
                    .getIWorkbench()
                    .getGuiComponent()
                    .getActiveInternalFrame())
                    .getLayerManager();*/
        layerManager.getUndoableEditReceiver().getUndoManager().undo();
        //Exclude the plug-in's activity from the undo history [Jon Aquino]
        reportNothingToUndoYet(context);
        // sila operacion se ha abortado hay que rehacer el comando
        
       if (layerManager.getUndoableEditReceiver()
                .isAborted())
        {
           boolean firingEvents = layerManager.isFiringEvents();
           layerManager.setFiringEvents(false);
            layerManager
                    .getUndoableEditReceiver().getUndoManager().redo();
            layerManager.setFiringEvents(firingEvents);
            context.getWorkbenchContext()
                    .getIWorkbench()
                    .getGuiComponent()
                    .getActiveTaskComponent().getLayerViewPanel().repaint();
        }
        context.getWorkbenchGuiComponent().getToolBar().updateEnabledState();
        return true;
    }
    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(
            checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(new EnableCheck() {
            public String check(JComponent component) {
            	
            	UndoManager undoManager = workbenchContext
            		.getIWorkbench()
            		.getGuiComponent()
            		.getContext()
            		.getLayerManager()
            		.getUndoableEditReceiver()
            		.getUndoManager();
            	
                /*UndoManager undoManager =
                    ((LayerManagerProxy) workbenchContext
                        .getIWorkbench()
                        .getGuiComponent()
                        .getActiveInternalFrame())
                        .getLayerManager()
                        .getUndoableEditReceiver()
                        .getUndoManager();*/
                component.setToolTipText(undoManager.getUndoPresentationName());
                return (!undoManager.canUndo()) ? "X" : null;
            }
        });
    }
    public ImageIcon getIcon() {
        return IconLoader.icon("Undo.gif");
    }
}
