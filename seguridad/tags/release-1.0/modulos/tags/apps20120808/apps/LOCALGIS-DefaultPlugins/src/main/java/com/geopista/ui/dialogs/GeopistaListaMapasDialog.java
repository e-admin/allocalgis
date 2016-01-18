/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/

package com.geopista.ui.dialogs;
import java.awt.Frame;

import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaMap;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class GeopistaListaMapasDialog extends JDialog
{

  GeopistaListaMapasPanel geopistaListaMapasPanel = null;
  
  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  
	/**
	 * This method initializes 
	 * 
	 */
	public GeopistaListaMapasDialog() {
		super();
		initialize();
	}
  public GeopistaListaMapasDialog(Frame owner)
  {
        super(owner);
	     initialize();

  }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setModal(true);
        this.setTitle(AppContext.getMessage("Abrir mapa"));
	     
	      geopistaListaMapasPanel = new GeopistaListaMapasPanel();
	     
	      this.getContentPane().add(geopistaListaMapasPanel);
	     
      this.setLocation(150, 90);	
      this.setSize(520, 447);
     
	}
	
  	public void getMap(PlugInContext plugInContext )
  	{
  		geopistaListaMapasPanel.getMapFiles(plugInContext, true, true);
  		/*if (aplicacion.isLogged()){
  			this.setModal(true);
  			this.setVisible(true);
  		}*/
  		
  		//Presentamos el dialogo solo si hay mapas
		this.setModal(true);
		this.setVisible(true);
  	}
  

  	public GeopistaMap getMapGeopista()
  	{
    	return geopistaListaMapasPanel.getMapGeopistaSelected();
  	}
  
	public GeopistaListaMapasPanel getGeopistaListaMapasPanel() {
		return geopistaListaMapasPanel;
	}
	
	public void setGeopistaListaMapasPanel(GeopistaListaMapasPanel geopistaListaMapasPanel) {
		this.geopistaListaMapasPanel = geopistaListaMapasPanel;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"