/**
 * ShowMapPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;

import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.security.GeopistaPermission;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class ShowMapPanel extends JPanel
{

	private boolean acceso;
	private GeopistaMapPanel geopistaMapPanel = null;

	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private String fileProperties = null;
	private int idMap = 0;

	public ShowMapPanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ShowMapPanel(String fileProperties, int idMap)
	{
		this.fileProperties = fileProperties;
		this.idMap = idMap;
		
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public JPanel getGeopistaMapPanel(String fileProperties, int idMap){

		geopistaMapPanel = new GeopistaMapPanel(fileProperties, idMap);
		
		return geopistaMapPanel;
	}



	private void jbInit() throws Exception
	{

		GeopistaPermission geopistaPerm = new GeopistaPermission("LocalGIS.edicion.EIEL");
		acceso = aplicacion.checkPermission(geopistaPerm,"EIEL");

		this.setLayout(new GridBagLayout());

		this.add(getGeopistaMapPanel(fileProperties, idMap), 
				new GridBagConstraints(0, 0, 1, 1, 1, 1,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

		AppContext.getApplicationContext().getBlackboard().put("GeopistaEditor",geopistaMapPanel);

	}


	/**
	 * Tip: Delegate to an InputChangedFirer.
	 * @param listener a party to notify when the input changes (usually the
	 * WizardDialog, which needs to know when to update the enabled state of
	 * the buttons.
	 */
	public void add(InputChangedListener listener)
	{

	}

	public void remove(InputChangedListener listener)
	{

	}


	public String getTitle()
	{
		return "";
	}

	public String getID()
	{
		return "1";
	}

	public String getInstructions()
	{
		return "";
	}

	public boolean isInputValid()
	{
		Collection lista = null;
		lista = geopistaMapPanel.getEditor().getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(
				geopistaMapPanel.getEditor().getLayerManager().getLayer("parcelas"));
		if (lista.size()==1)
			if (acceso) {
				return true;
			}
			else{
				return false;
			}

		else

			return false;
	}
}

