/**
 * GeopistaMultiSelectionPatrimonioDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.patrimonio;


import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * GeopistaMultiSelectionPatrimonioDialog
 * Cuadro de Dialogo del Panel de Selección Multicriterio de Patrimonio
 * 
 */

public class GeopistaMultiSelectionPatrimonioDialog extends JDialog
{
  ApplicationContext appContext=AppContext.getApplicationContext();
  
  public GeopistaMultiSelectionPatrimonioDialog(PlugInContext context)
  {
	 
	     this.setTitle(appContext.getI18nString("GeopistaMultiSelectionPatrimonioPlugInDescription"));
	 
	     this.setResizable(false);
	     GeopistaMultiSelectionPatrimonioPanel multiPanel = new GeopistaMultiSelectionPatrimonioPanel(context);
	     this.setResizable(false);
	     this.getContentPane().add(multiPanel);
	     this.setSize(425, 325);
	     this.setModal(true);
	     this.setVisible(true);
    this.setLocation(150, 150);
  }
}