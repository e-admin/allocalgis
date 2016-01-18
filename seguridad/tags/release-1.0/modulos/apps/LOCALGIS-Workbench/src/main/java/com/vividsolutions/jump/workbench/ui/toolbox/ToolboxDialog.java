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

package com.vividsolutions.jump.workbench.ui.toolbox;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;

/**
 * An always-on-top modeless dialog with a WorkbenchToolBar (for CursorTools,
 * PlugIns, and other buttons). Takes care of unpressing the CursorTools (if necessary)
 * when the dialog is closed (and pressing the first CursorTool on the main toolbar).
 */

//Must use modeless dialog rather than JInternalFrame; otherwise, if we implement
//it as an internal frame, when we click it, the original TaskFrame will deactivate,
//thus deactivating the current CursorTool. [Jon Aquino]

public class ToolboxDialog extends JDialog {
    public AbstractButton getButton(Class cursorToolClass) {
        for (Iterator i = toolBars.iterator(); i.hasNext();) {
            WorkbenchToolBar toolBar = (WorkbenchToolBar) i.next();
            AbstractButton button = toolBar.getButton(cursorToolClass);
            if (button != null) {
                return button;
            }
        }
        return null;
    }

    public WorkbenchToolBar getToolBar() {
        if (toolBars.isEmpty()) {
            addToolBar();
        }
        return (WorkbenchToolBar) toolBars.get(toolBars.size() - 1);
    }

    public WorkbenchContext getContext() {
        return context;
    }

    public WorkbenchToolBar.ToolConfig add(CursorTool tool) {
        return add(tool, null);
    }    

    /**
	 * @param enableCheck
	 *                  null to leave unspecified
	 */
    public WorkbenchToolBar.ToolConfig add(CursorTool tool,
            EnableCheck enableCheck) {
        WorkbenchToolBar.ToolConfig config = getToolBar().addCursorTool(tool);
        JToggleButton button = config.getButton();
        getToolBar()
                .setEnableCheck(button, enableCheck != null
                        ? enableCheck
                        : new MultiEnableCheck())        ;
        registerButton(button, enableCheck);
        return config;
    }
    
    public void addPlugIn(PlugIn plugIn, EnableCheck enableCheck, Icon icon) {
        registerButton(getToolBar().addPlugIn(icon, plugIn, enableCheck, context), enableCheck);
    }
    
    private void registerButton(AbstractButton button, EnableCheck enableCheck) {
        buttons.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {            
                activateWorkbenchFrame(context);
        }
        });
    }
    private ArrayList buttons = new ArrayList();

    private ArrayList toolBars = new ArrayList();

    public void addToolBar() {
        toolBars.add(
            new WorkbenchToolBar(
                context,
                context.getIWorkbench().getGuiComponent().getToolBar().getButtonGroup()));
        getToolBar().setBorder(null);
        getToolBar().setFloatable(false);
        gridLayout1.setRows(toolBars.size());
        toolbarsPanel.add(getToolBar());
    }

    private void activateWorkbenchFrame(final WorkbenchContext context) {
        //Re-activate WorkbenchFrame. Otherwise, user may try entering
        //a quasi-mode by pressing a modifier key -- nothing will happen because the
        //WorkbenchFrame does not have focus. [Jon Aquino]
        //JavaDoc for #toFront says some platforms will not activate the window.
        //So use #requestFocus instead. [Jon Aquino 12/9/2003]
       //TODO: modificar el interfaz WorkBenchGUIComponent para manejar requestFocus()
    	 //context.getWorkbench().getFrame().requestFocus();
    }          
    
    public ToolboxDialog(final WorkbenchContext context) {
        super(GeopistaFunctionUtils.getFrame(context.getIWorkbench().getGuiComponent()), "", false);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context = context;
        setResizable(true);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                activateWorkbenchFrame(context);
            }

        });  
        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                if (buttons
                    .contains(
                        context
                            .getIWorkbench()
                            .getGuiComponent()
                            .getToolBar()
                            .getSelectedCursorToolButton())) {
                    ((AbstractButton) context
                        .getIWorkbench()
                        .getGuiComponent()
                        .getToolBar()
                        .getButtonGroup()
                        .getElements()
                        .nextElement())
                        .doClick();
                }
            }
        });
    }

    /**
     * Call this method after all the CursorTools have been added.
     */
    public void finishAddingComponents() {
        pack();
        GUIUtil.setLocation(this, initialLocation, context.getIWorkbench().getGuiComponent().getDesktopPane());
    }
    
    private GUIUtil.Location initialLocation = new GUIUtil.Location(0, false, 0, false);

    private WorkbenchContext context;

    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel centerPanel = new JPanel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel toolbarsPanel = new JPanel();
    private GridLayout gridLayout1 = new GridLayout();

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        centerPanel.setLayout(borderLayout2);
        toolbarsPanel.setLayout(gridLayout1);
        gridLayout1.setColumns(1);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(toolbarsPanel, BorderLayout.NORTH);        
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public void updateEnabledState() {
        for (Iterator i = toolBars.iterator(); i.hasNext();) {
            WorkbenchToolBar toolBar = (WorkbenchToolBar) i.next();
            toolBar.updateEnabledState();
        }
    }

    public void setInitialLocation(GUIUtil.Location location) {
        initialLocation = location;
    }

}
