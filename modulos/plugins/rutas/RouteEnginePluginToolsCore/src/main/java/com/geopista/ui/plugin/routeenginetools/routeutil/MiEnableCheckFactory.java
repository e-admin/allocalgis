/**
 * MiEnableCheckFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.util.Set;

import javax.swing.JComponent;

import org.uva.route.network.NetworkManager;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;

public class MiEnableCheckFactory extends EnableCheckFactory {
	WorkbenchContext workbenchContext;

	public MiEnableCheckFactory(WorkbenchContext workbenchContext) {
		super(workbenchContext);
		this.workbenchContext = workbenchContext;

	}

    public EnableCheck createNetworksMustBeLoadedCheck() {
		return new EnableCheck() {
			public String check(JComponent component) 
			{
			NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(workbenchContext);
			Set<String> redesSet = networkMgr.getNetworks().keySet();// obtener list de redes
		    return redesSet.isEmpty() ? "No hay ninguna red cargada." : null;
			}
		};
	}
	public EnableCheck createBlackBoardMustBeElementsCheck() {
		return new EnableCheck() {
			public String check(JComponent component) {
				return ((workbenchContext.getWorkbench().getBlackboard().get(
						"RedesDefinidas") == null)) ? "No hay ninguna red en el blackboard"
						: null;
			}
		};
	}

	public EnableCheck checkIsOnline()
	{return new EnableCheck() {
		public String check(JComponent component) 
		{
		boolean isOnline = AppContext.getApplicationContext().isOnline();
		return !isOnline ? "No se ha detectado un servidor de LocalGIS." : null;
		}
	};
}

}
