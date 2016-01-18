
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

package com.vividsolutions.jump.workbench.ui.cursortool;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkBench;
import com.geopista.editor.WorkbenchGuiComponent;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Con este plugin el usuario dibuja un recuadro sobre una capa dinámica. En el interior de este recuadro se permitirá al usuario
 * visualizar las features de esa capa que están almacenadas en la base de datos.
 * @author miriamperez
 *
 */
public class UpdateDynamicFenceTool extends RectangleTool{
    public final static Color COLOR = Color.black;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    TaskMonitorDialog progressDialog;
    WorkBench workbench;

    public UpdateDynamicFenceTool() {
        setColor(COLOR);
    }

    public Icon getIcon() {
        return IconLoader.icon("dinamico_fence.gif");
    }

    public String getName() {
        return aplicacion.getI18nString("VisualizarFeatures");
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("dinamico_cursor.gif").getImage());
    }

    public void activate(ILayerViewPanel layerViewPanel) {                        
        super.activate(layerViewPanel);
    }

    public void deactivate() {                       
        super.deactivate();
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();
    	workbench =((WorkbenchGuiComponent) SwingUtilities.getAncestorOfClass(WorkbenchGuiComponent.class,
                getPanel())).getContext().getIWorkbench();

    	progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(),
    			workbench.getContext().getErrorHandler());
        progressDialog.setTitle(I18N.get("LoadSystemLayers.Cargando"));
        progressDialog.report(I18N.get("LoadSystemLayers.Cargando"));
        progressDialog.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                //Wait for the dialog to appear before starting the task. Otherwise
                //the task might possibly finish before the dialog appeared and the
                //dialog would never close. [Jon Aquino]
                new Thread(new Runnable(){
                    public void run()
                    {
                        try
                        {
                        	UpdateDynamic updateDynamic = new UpdateDynamic(getPanel());
                        	updateDynamic.getFeatures(getRectangle());
                        }catch(Exception e)
                        {
                        }finally
                        {
                            progressDialog.setVisible(false);
                        } 
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        ((WorkbenchGuiComponent) SwingUtilities.getAncestorOfClass(WorkbenchGuiComponent.class,
                getPanel())).getContext().getLayerViewPanel().getRenderingManager().renderAll(true);
    }
    
    
    public EnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
	    EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
	    return new MultiEnableCheck()
	        .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
	        .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

}
