/**
 * WizardPanelImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.wizard;

import javax.swing.JPanel;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public abstract class WizardPanelImpl extends JPanel implements WizardPanel
{
private String nextID=null;
private String ID=null;
	public WizardPanelImpl(String ID, String nextID)
	{
	this.ID=ID;
	setNextID(nextID);
	}
	public void setNextID(String nextID)
	{
		this.nextID=nextID;

	}
	public String getID()
	{
		
		return ID==null?this.getClass().toString():ID;
	}
	public String getNextID()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
