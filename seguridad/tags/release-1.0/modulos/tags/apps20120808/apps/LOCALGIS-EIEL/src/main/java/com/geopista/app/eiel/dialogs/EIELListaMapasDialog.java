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

package com.geopista.app.eiel.dialogs;
import java.awt.Frame;

import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaMap;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class EIELListaMapasDialog extends JDialog
{

  EIELListaMapasPanel geopistaListaMapasPanel = null;
  
	/**
	 * This method initializes 
	 * 
	 */
	public EIELListaMapasDialog() {
		super();
		initialize();
	}
  public EIELListaMapasDialog(Frame owner)
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
	     
	      geopistaListaMapasPanel = new EIELListaMapasPanel();
	     
	      this.getContentPane().add(geopistaListaMapasPanel);
	     
      this.setLocation(150, 90);	
      this.setSize(483, 447);
      
	}	
	
  public void getMap(PlugInContext plugInContext )
  {
  geopistaListaMapasPanel.getMapFiles(plugInContext, true, true);
  this.setModal(true);
  this.setVisible(true);
  } 
  
  public GeopistaMap getMapEIEL()
  {
    return geopistaListaMapasPanel.getMapGeopistaSelected();
  }
}