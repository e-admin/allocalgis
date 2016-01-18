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

package com.vividsolutions.jump.workbench.ui.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Date;

import javax.swing.Timer;

import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class TaskMonitorManager {
    public TaskMonitorManager() {
    }

    /**
     * Executes the task in a separate thread, reporting progress in a dialog.
     */
    public void execute(ThreadedPlugIn plugIn, PlugInContext context) {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
                context.getErrorHandler());
        progressDialog.setTitle(plugIn.getName());

        //Do not refer to context inside the anonymous class, otherwise it (and
        //the Task it refers to) will not be garbage collected. [Jon Aquino]
        //<<TODO>> Eliminate TaskWrapper. The Task no longer needs to be
        //garbage-collectable for the data to be garbage-collected. [Jon Aquino]
        final TaskWrapper taskWrapper = new TaskWrapper(plugIn, context,
                progressDialog);
        progressDialog.addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    //Wait for the dialog to appear before starting the task. Otherwise
                    //the task might possibly finish before the dialog appeared and the
                    //dialog would never close. [Jon Aquino]
                    new Thread(taskWrapper).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);

        Timer timer = timer(new Date(), plugIn, context.getWorkbenchGuiComponent());
        timer.start();

        try {
            progressDialog.setVisible(true);
        } finally {
            timer.stop();
        }
    }

    private Timer timer(final Date start, final ThreadedPlugIn plugIn,
        final WorkbenchGuiComponent workbenchFrame) {
        return new Timer(1000,
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String message = "";
                    message += StringUtil.toTimeString(new Date().getTime() -
                        start.getTime());
                    message += (" (" + plugIn.getName() + ")");
                    workbenchFrame.setTimeMessage(message);
                }
            });
    }

    private class TaskWrapper implements Runnable {
        private ThreadedPlugIn plugIn;
        private PlugInContext context;
        private TaskMonitorDialog dialog;

        public TaskWrapper(ThreadedPlugIn plugIn, PlugInContext context,
            TaskMonitorDialog dialog) {
            this.plugIn = plugIn;
            this.context = context;
            this.dialog = dialog;
        }

        public void run() {
            Throwable throwable = null;

            try {
                plugIn.run(dialog, context);
            } catch (Throwable t) {
                throwable = t;
            } finally {
                dialog.setVisible(false);

                if (throwable != null) {
                    context.getErrorHandler().handleThrowable(throwable);
                }

                //Releases references to the data, to facilitate garbage collection. [Jon Aquino]
                context = null;
            }
        }
    }
}
