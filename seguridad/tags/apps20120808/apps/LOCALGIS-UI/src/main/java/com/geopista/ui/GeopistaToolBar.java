package com.geopista.ui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.ui.EnableableToolBar;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.QuasimodeTool;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;



public class GeopistaToolBar extends EnableableToolBar {
    private HashMap cursorToolClassToButtonMap = new HashMap();


    private LayerViewPanelProxy layerViewPanelProxy;
    private TaskMonitorManager taskMonitorManager = null;

    public AbstractButton getButton(Class cursorToolClass) {
        Assert.isTrue(CursorTool.class.isAssignableFrom(cursorToolClass));
        return (AbstractButton) cursorToolClassToButtonMap.get(cursorToolClass);
    }

    // By default, CursorTool buttons are always enabled. [Jon Aquino]
    private EnableCheck cursorToolEnableCheck = new EnableCheck() {
            public String check(JComponent component) {
                return null;
            }
        };

    private ButtonGroup cursorToolButtonGroup;

    public GeopistaToolBar(LayerViewPanelProxy layerViewPanelProxy) {
        this(layerViewPanelProxy, new ButtonGroup());
    }

    public GeopistaToolBar(LayerViewPanelProxy layerViewPanelProxy, ButtonGroup cursorToolButtonGroup) {
        this.cursorToolButtonGroup = cursorToolButtonGroup;
        this.layerViewPanelProxy = layerViewPanelProxy;
    }

    public void setCursorToolEnableCheck(EnableCheck cursorToolEnableCheck) {
        this.cursorToolEnableCheck = cursorToolEnableCheck;
    }

    public void setTaskMonitorManager(TaskMonitorManager taskMonitorManager) {
        this.taskMonitorManager = taskMonitorManager;
    }

    public ToolConfig addCursorTool(CursorTool cursorTool) {
        return addCursorTool(cursorTool.getName(), cursorTool);
    }
    
    public static class ToolConfig {
        private JToggleButton button;
        private QuasimodeTool quasimodeTool;
        public ToolConfig(JToggleButton button, QuasimodeTool quasimodeTool) {
            this.button = button;
            this.quasimodeTool = quasimodeTool;
        }
        public JToggleButton getButton() {
            return button;
        }

        public QuasimodeTool getQuasimodeTool() {
            return quasimodeTool;
        }
        public void remove(){
        	if (quasimodeTool!=null){
	        	quasimodeTool.clean();
	        	quasimodeTool=null;
        	}
        }

    }

    public ToolConfig addCursorTool(String tooltip, final CursorTool cursorTool) {
        JToggleButton button = new JToggleButton();
        cursorToolButtonGroup.add(button);
        cursorToolClassToButtonMap.put(cursorTool.getClass(), button);
        final QuasimodeTool quasimodeTool = QuasimodeTool.addStandardQuasimodes(cursorTool);        
        add(button, tooltip, cursorTool.getIcon(),
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //It's null when the Workbench starts up. [Jon Aquino]
                    //Or the active frame may not have a LayerViewPanel. [Jon Aquino]
                    if (layerViewPanelProxy.getLayerViewPanel() != null) {
                        layerViewPanelProxy.getLayerViewPanel().setCurrentCursorTool(quasimodeTool);
                    }

                    //<<TODO:DESIGN>> We really shouldn't create a new LeftClickFilter on each
                    //click of the tool button. Not a big deal though. [Jon Aquino]
                }
            }, cursorToolEnableCheck);
        if (cursorToolButtonGroup.getButtonCount() == 1) {
            cursorToolButtonGroup.setSelected(button.getModel(), true);
            reClickSelectedCursorToolButton();            
        }            
        return new ToolConfig(button, quasimodeTool);
    }
    
    public ButtonGroup getButtonGroup() { return cursorToolButtonGroup; }

    public JToggleButton getSelectedCursorToolButton() {
        for (Enumeration e = cursorToolButtonGroup.getElements();
                e.hasMoreElements();) {
            JToggleButton button = (JToggleButton) e.nextElement();

            if (button.getModel() == cursorToolButtonGroup.getSelection()) {
                return button;
            }
        }

        Assert.shouldNeverReachHere();

        return null;
    }

    public void reClickSelectedCursorToolButton() {
        if (cursorToolButtonGroup.getButtonCount() == 0) {
            return;
        }
        getSelectedCursorToolButton().doClick(); 
    }

    //<<TODO:REFACTOR>> This method duplicates code in FeatureInstaller, with the
    //result that when the latter was updated (to handle ThreadedPlugIns), the
    //changes were left out from the former. [Jon Aquino]
    public JButton addPlugIn(Icon icon, final PlugIn plugIn,
        EnableCheck enableCheck, WorkbenchContext workbenchContext) {
        JButton button = new JButton();
        add(button, plugIn.getName(), icon,
            AbstractPlugIn.toActionListener(plugIn, workbenchContext,
                taskMonitorManager), enableCheck);

        return button;
    }
}
