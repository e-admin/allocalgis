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
