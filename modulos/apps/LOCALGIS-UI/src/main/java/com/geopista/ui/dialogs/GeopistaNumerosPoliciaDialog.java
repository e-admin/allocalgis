/**
 * GeopistaNumerosPoliciaDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;


import javax.swing.JDialog;
import javax.swing.JFrame;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;


public class GeopistaNumerosPoliciaDialog extends JDialog
{
  ApplicationContext appContext=AppContext.getApplicationContext();
  public GeopistaNumerosPoliciaDialog(PlugInContext context, JFrame owner)
  {     
      super(owner);
	 //super((JFrame)SwingUtilities.getAncestorOfClass(JFrame.class,(JFrame) context.getWorkbenchFrame()));
       this.setTitle(appContext.getI18nString("GeopistaNumerosPoliciaPlugInDescription"));

	     this.setResizable(false);
	     GeopistaNumerosPoliciaPanel numerosPoliciaPanel = new GeopistaNumerosPoliciaPanel(context);
	     this.setResizable(false);
	     this.getContentPane().add(numerosPoliciaPanel);
	     this.setSize(375, 150);
	     this.setModal(true);
	     this.setVisible(true);
    this.setLocation(150, 150);
  }
}