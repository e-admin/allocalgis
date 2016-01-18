/**
 * GeopistaEditingPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.cursortool.editing;


import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import com.geopista.app.AppContext;
import com.geopista.ui.cursortool.GeopistaSelectFeaturesTool;
import com.geopista.ui.cursortool.GeopistaSelectLineStringsTool;
import com.geopista.ui.cursortool.GeopistaSelectPartsTool;
import com.geopista.ui.cursortool.editing.images.IconLoader;
import com.geopista.ui.cursortool.selectoneitem.SelectOneItemTool;
import com.geopista.ui.plugin.GeopistaOptionsPlugIn;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.cursortool.QuasimodeTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.DeleteVertexTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.DrawRectangleTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.InsertVertexTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.MoveSelectedItemsTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.MoveVertexTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.SnapVerticesToSelectedVertexTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.SnapVerticesTool;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;





public class GeopistaEditingPlugIn extends EditingPlugIn {
    AppContext appContext = (AppContext) AppContext.getApplicationContext();
     
    public String getName() { return "Editing Toolbox"; }

    public static ImageIcon ICON = IconLoader.icon("EditingToolbox.gif");

    public static final String KEY = EditingPlugIn.class.getName();

    WorkbenchContext workbenchContext = null;

	private JButton optionsButton = new JButton(appContext.getI18nString("Options"));
	
	
    public void initialize(PlugInContext context) throws Exception {
    	
    	///////////////////////////////////////
    	//Nuevo
    	 if(!isRegisteredPlugin(this,context))
    		 registerPlugin(this,context);
    	///////////////////////
    	
        context.getWorkbenchContext().getIWorkbench().getBlackboard().put(KEY, this);

        workbenchContext = context.getWorkbenchContext();

        final JToggleButton toggleButton = new JToggleButton();
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar().add(toggleButton,
            this.getName(), EditingPlugIn.ICON,
            AbstractPlugIn.toActionListener(this, context.getWorkbenchContext(),
                new TaskMonitorManager()), null);
        context.getWorkbenchContext().getIWorkbench().getGuiComponent();
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    //Can't #getToolbox before Workbench is thrown. Otherwise, get 
                    //IllegalComponentStateException. Thus, do it inside #componentShown. [Jon Aquino]
                    getToolbox(workbenchContext)
                                 .addComponentListener(new ComponentAdapter() {
                            //There are other ways to show/hide the toolbox. Track 'em. [Jon Aquino]
                            public void componentShown(ComponentEvent e) {
                                toggleButton.setSelected(true);
                            }

                            public void componentHidden(ComponentEvent e) {
                                toggleButton.setSelected(false);
                            }
                        });
                }
            });
    }

    protected void initializeToolbox(ToolboxDialog toolbox) {
        //The auto-generated title "Editing Toolbox" is too long to fit. [Jon Aquino]
        toolbox.setTitle(appContext.getI18nString("GeopistaEditingPlugIn.Editing"));
        EnableCheckFactory checkFactory = new EnableCheckFactory(toolbox.getContext());
        //Null out the quasimodes for [Ctrl] because the Select tools will handle that case. [Jon Aquino]
        toolbox.add(
            new QuasimodeTool(new GeopistaSelectFeaturesTool()).add(
                new QuasimodeTool.ModifierKeySpec(true, false, false),
                null));
        toolbox.add(
            new QuasimodeTool(new GeopistaSelectPartsTool()).add(
                new QuasimodeTool.ModifierKeySpec(true, false, false),
                null));
        toolbox.add(
            new QuasimodeTool(new GeopistaSelectLineStringsTool()).add(
                new QuasimodeTool.ModifierKeySpec(true, false, false),
                null));
        toolbox.add(new SelectOneItemTool());
        
		toolbox.addToolBar();
		toolbox.add(new InsertVertexTool(checkFactory));
		toolbox.add(new DeleteVertexTool(checkFactory));
		toolbox.add(new MoveVertexTool(checkFactory));
		toolbox.add(new MoveSelectedItemsTool(checkFactory));
		
		toolbox.addToolBar();						
        toolbox.add(new SnapVerticesTool(checkFactory));
        toolbox.add(new SnapVerticesToSelectedVertexTool(checkFactory));
        
              
        toolbox.addToolBar();    
        
        toolbox.add(DrawRectangleTool.create(toolbox.getContext())); 
        toolbox.add(DrawPolygonTool.create(toolbox.getContext()));        
        
        optionsButton.addActionListener(
            AbstractPlugIn.toActionListener(new GeopistaOptionsPlugIn(), toolbox.getContext(), null));
                
        for (Iterator plugInIter = aditionalPlugIns.iterator(); plugInIter.hasNext();) 
		{
			AbstractPlugIn plugIn = (AbstractPlugIn) plugInIter.next();
			plugIn.addButton(toolbox);
		 
		}
        
        toolbox.getCenterPanel().add(optionsButton, BorderLayout.CENTER);
        
        toolbox.setInitialLocation(new GUIUtil.Location(20, true, 20, false));
        toolbox.setResizable(false);
        
    }

	protected JButton getOptionsButton() {
		return optionsButton;
	}
	
	
	private Vector aditionalPlugIns=new Vector();
	
	public void addAditionalPlugIn(AbstractPlugIn plugIn)
    {
		aditionalPlugIns.add(plugIn);
    }
	
    
    
}
    