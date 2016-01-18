/**
 * PanelToSaveInDataStore.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.event.ActionListener;

import javax.swing.JTextField;

import com.vividsolutions.jump.workbench.plugin.PlugInContext;

@SuppressWarnings("serial")
public class PanelToSaveInDataStore extends PanelToSelectMemoryNetworks implements
		ActionListener {

    public PanelToSaveInDataStore(PlugInContext context)
    {
	super(context);
    }

    protected JTextField nombreenbaseField;

    @Override
    protected void initialize()
    {
	super.initialize();
	addRow("Nombre de SubRed", getFieldNombreenBase(), null, false);
    }

    protected JTextField getFieldNombreenBase()
    {
    	if (nombreenbaseField == null)
    		nombreenbaseField = new JTextField();
    	nombreenbaseField.setEditable(true);
    	return nombreenbaseField;
    }

    public String getNombreenBase()
    {
    	return nombreenbaseField.getText();
    }

}
