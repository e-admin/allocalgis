/**
 * ConnectionManagerToolboxPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.datastore;

import java.awt.BorderLayout;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.datastore.ConnectionManager;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxPlugIn;

public class ConnectionManagerToolboxPlugIn extends ToolboxPlugIn {

    private static final String INSTANCE_KEY = ConnectionManagerToolboxPlugIn.class
            .getName()
            + " - INSTANCE";

    private ConnectionManagerToolboxPlugIn() {
    }

    public String getName() {
        // Specify name explicitly, as auto-name-generator
        // says "Connection Manager Toolbox" [Jon Aquino 2005-03-14]
        return I18N.get("jump.workbench.ui.plugin.datastore.ConnectionManagerToolboxPlugIn.Connection-Manager");
    }

    public static final ConnectionManagerToolboxPlugIn instance(
            Blackboard blackboard) {
        if (blackboard.get(INSTANCE_KEY) == null) {
            blackboard.put(INSTANCE_KEY, new ConnectionManagerToolboxPlugIn());
        }
        return (ConnectionManagerToolboxPlugIn) blackboard.get(INSTANCE_KEY);
    }

    public void initialize(final PlugInContext context) throws Exception {
        if (1 == 1) {
            throw new UnsupportedOperationException(
                    "To do: fix: ConnectionManagerToolbox does not stay in sync with ConnectionManager object. Implement eventing. [Jon Aquino 2005-03-24]");
        }
        new FeatureInstaller(context.getWorkbenchContext()).addMainMenuItem(
                this, (new String[] { MenuNames.VIEW }), getName() + "...{pos:1}",
                true, null, new EnableCheck() {
                    public String check(JComponent component) {
                        ((JCheckBoxMenuItem) component).setSelected(getToolbox(
                                context.getWorkbenchContext()).isVisible());
                        return null;
                    }
                });
    }

    protected void initializeToolbox(ToolboxDialog toolbox) {
        ConnectionManagerPanel connectionManagerPanel = new ConnectionManagerPanel(
                ConnectionManager
                        .instance(toolbox.getContext()),
                toolbox.getContext().getRegistry(), toolbox.getContext()
                        .getErrorHandler(),toolbox.getContext());
        toolbox.getCenterPanel().add(connectionManagerPanel,
                BorderLayout.CENTER);
        toolbox.setInitialLocation(new GUIUtil.Location(20, true, 20, false));
    }

}