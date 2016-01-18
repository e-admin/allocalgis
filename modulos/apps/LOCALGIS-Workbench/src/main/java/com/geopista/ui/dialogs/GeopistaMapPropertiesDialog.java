/**
 * GeopistaMapPropertiesDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;

import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class GeopistaMapPropertiesDialog extends JDialog
{

    private GeopistaMapPropertiesPanel geopistaMapPropertiesPanel = null;

    private static ApplicationContext aplicacion = AppContext.getApplicationContext();

    public GeopistaMapPropertiesDialog(PlugInContext context)
        {
            super(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("GeopistaMapPropertiesPlugInDescription"), true);

            geopistaMapPropertiesPanel = new GeopistaMapPropertiesPanel(context);
            this.setResizable(false);
            this.getContentPane().add(geopistaMapPropertiesPanel);
            //this.setSize(400, 250);
            this.setSize(400, 300);
            this.setLocation(150, 90);

            this.setVisible(true);
        }
    
    public boolean wasOKPressed() {
        return geopistaMapPropertiesPanel.wasOKPressed();
    }

}