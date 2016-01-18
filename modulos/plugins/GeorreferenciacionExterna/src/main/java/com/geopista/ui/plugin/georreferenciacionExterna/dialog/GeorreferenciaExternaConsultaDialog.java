/**
 * GeorreferenciaExternaConsultaDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.georreferenciacionExterna.dialog;

import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.georreferenciacionExterna.paneles.GeorreferenciacionExternaPanelConsultas;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class GeorreferenciaExternaConsultaDialog extends JDialog{

	
	private static ApplicationContext aplicacion = AppContext.getApplicationContext();
	private GeorreferenciacionExternaPanelConsultas  georreferenciacionExternaPanelConsultas= null;
	
	public GeorreferenciaExternaConsultaDialog(PlugInContext context, String user){
		
		super(aplicacion.getMainFrame(), aplicacion
                .getI18nString("GeopistaMapPropertiesPlugInDescription"), true);
		georreferenciacionExternaPanelConsultas = new GeorreferenciacionExternaPanelConsultas(context, user ,this);
		 this.setResizable(false);
         this.getContentPane().add(georreferenciacionExternaPanelConsultas);
         this.setSize(700, 600);
         this.setLocation(200, 120);
 
         this.setVisible(true);
	} 
	
	public boolean wasOKPressed() {
        return georreferenciacionExternaPanelConsultas.wasOKPressed();
    }
}
