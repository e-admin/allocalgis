package com.vividsolutions.jump.workbench;

import com.vividsolutions.jump.task.TaskMonitor;

/**
 * Installs most of the menus and toolbar buttons. Called when the app starts up.
 */
public interface Setup {
    public void setup(WorkbenchContext workbenchContext, TaskMonitor monitor)
        throws Exception;
}
