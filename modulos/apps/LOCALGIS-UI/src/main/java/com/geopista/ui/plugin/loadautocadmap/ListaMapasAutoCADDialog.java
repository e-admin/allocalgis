/**
 * ListaMapasAutoCADDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.loadautocadmap;

import java.awt.Frame;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;

import com.geopista.model.GeopistaMap;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class ListaMapasAutoCADDialog extends JDialog
{

  ListaMapasAutoCADPanel listaMapasAutoCADPanel = null;
  
	/**
	 * This method initializes 
	 * 
	 */
	public ListaMapasAutoCADDialog() {
		super();
		
		Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.loadautocadmap.languages.LoadAutoCADMapPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("LoadAutoCADMapPlugIn",bundle2);
        
		initialize();
	}
	public ListaMapasAutoCADDialog(Frame owner)
	{
		super(owner);
		
		Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.loadautocadmap.languages.LoadAutoCADMapPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("LoadAutoCADMapPlugIn",bundle2);
        
		initialize();

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		this.setModal(true);
		this.setTitle(I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap"));

		listaMapasAutoCADPanel = new ListaMapasAutoCADPanel();

		this.getContentPane().add(listaMapasAutoCADPanel);

		this.setLocation(150, 90);	
		this.setSize(483, 447);

	}
	
	public void getMap(PlugInContext plugInContext )
	{
		listaMapasAutoCADPanel.getMapFiles(plugInContext, true, true);
		this.setModal(true);
		this.setVisible(true);
	}
  

  public GeopistaMap getMapAutoCAD()
  {
    return listaMapasAutoCADPanel.getMapGeopistaSelected();
  }
}  