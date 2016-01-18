/**
 * GpxReaderExtension.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.surveyos.sourceforge.openjump.plugins.gpx;

import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * Loads the plug-ins that implement the GPX support for OpenJUMP created
 * as part of the SurveyOS Project.
 */
public class GpxReaderExtension extends Extension 
{

	@Override
	public void configure(PlugInContext argPlugInContext) throws Exception 
	{
		// Create and initialize the plug-ins installed by this Extension.
		GpxReaderPlugIn plugIn1 = new 	
		GpxReaderPlugIn();
			
		plugIn1.initialize(argPlugInContext);
			
		// Add the plug-in to the main menu bar in OpenJUMP.
		com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller installer = argPlugInContext.getFeatureInstaller();
			
		String[] menuPath1 = new String[2];
			
		menuPath1[0] = "SurveyOS";
		menuPath1[1] = "GPS";
			
		installer.addMainMenuItem(plugIn1, menuPath1, 
		"Import GPX File", false, null, null);
	}
}
