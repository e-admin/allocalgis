/**
 * GEOPISTAWorkbenchContext.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/*

 */

package com.geopista.ui;

import javax.swing.JInternalFrame;

import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkBench;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.JUMPWorkbench;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.driver.DriverManager;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.TaskFrame;


/**
 * Implementation of {@link WorkbenchContext} for the {@link
 * JUMPWorkbench}.
 */
public class GEOPISTAWorkbenchContext extends WorkbenchContext {
    private WorkBench workbench;

    public GEOPISTAWorkbenchContext(WorkBench workbench) {
        this.workbench = workbench;
    }

    public WorkBench getIWorkbench() {
        return workbench;
    }
    public JUMPWorkbench getWorkbench() {
    return (JUMPWorkbench) workbench;
    }
    public Blackboard getBlackboard() {
		return workbench.getBlackboard();
	}

    public DriverManager getDriverManager() {
        return workbench.getDriverManager();
    }

    public ErrorHandler getErrorHandler() {
        return workbench.getGuiComponent();
    }

    public ITask getTask() {
        if (!(activeInternalFrame() instanceof TaskFrame)) {
            return null;
        }

        return ((TaskComponent) activeInternalFrame()).getTask();
    }

    public LayerNamePanel getLayerNamePanel() {
        if (!(activeInternalFrame() instanceof LayerNamePanelProxy)) {
            return null;
        }

        return ((LayerNamePanelProxy) activeInternalFrame()).getLayerNamePanel();
    }

    public ILayerManager getLayerManager() {
        if (!(activeInternalFrame() instanceof LayerManagerProxy)) {
            //WarpingPanel assumes that this method returns null if the active frame is not
            //a LayerManagerProxy. [Jon Aquino]            
            return null;
        }

        return ((LayerManagerProxy) activeInternalFrame()).getLayerManager();
    }

    public ILayerViewPanel getLayerViewPanel() {
        if (!(activeInternalFrame() instanceof LayerViewPanelProxy)) {
            return null;
        }

        return ((LayerViewPanelProxy) activeInternalFrame()).getLayerViewPanel();
    }

    private JInternalFrame activeInternalFrame() {
        return workbench.getGuiComponent().getActiveInternalFrame();
    }
}
