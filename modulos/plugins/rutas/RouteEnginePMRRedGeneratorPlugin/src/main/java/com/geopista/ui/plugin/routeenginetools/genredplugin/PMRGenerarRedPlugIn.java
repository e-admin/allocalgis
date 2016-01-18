/**
 * PMRGenerarRedPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.genredplugin;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.geopista.ui.plugin.routeenginetools.genredplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.PMRBasicNetworkFactoryDialog;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * @description: This plugin creates a list of nets and they are saved in
 *               NetworkMgr. Besides ConvexHull is created of each one of the
 *               generated nets
 **/

public class PMRGenerarRedPlugIn extends GenerarRedPlugIn {



	private static Logger LOGGER = Logger.getLogger(PMRGenerarRedPlugIn.class);


	public boolean execute(PlugInContext context)  {
		if(context.getLayerViewPanel() == null)
			return false;
		this.context = context;

		if (basicDialog == null){
			basicDialog = new PMRBasicNetworkFactoryDialog(context.getWorkbenchFrame(), 
					I18N.get("genred","routeengine.genred.plugintitle"), 
					context);
		}
		return super.execute(context);
	}

	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("genred","routeengine.genred.iconfile"));
	}



}
