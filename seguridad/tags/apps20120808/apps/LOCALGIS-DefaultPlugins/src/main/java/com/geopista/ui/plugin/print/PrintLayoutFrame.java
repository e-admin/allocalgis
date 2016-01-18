/*
 * 
 * Created on 29-jul-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import reso.jumpPlugIn.printLayoutPlugIn.OpenLegende;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class PrintLayoutFrame
		extends
			reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3760566394781054518L;

	/**
	 * @param context
	 */
	public PrintLayoutFrame(PlugInContext context)
	{
	super(context);
	
	// TODO Auto-generated constructor stub
	}
	public void forceOpen()
	{
		// Force to select a template
		OpenLegende ol=new OpenLegende(this);
		File dir=new File(AppContext.getApplicationContext().getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,null,false));
		ol.getFileChooser().setCurrentDirectory(dir);
		ol.getFileChooser().setFileFilter(new FileFilter()
		{
			public boolean accept(File arg0)
			{
			    if(arg0.getName().endsWith(".jmp") || arg0.isDirectory())
			    {
			        return true;
			    }
			    return false;
			}

			public String getDescription()
			{
			return I18N.get("PrintLayoutPlugin.PrintTemplates");
			}
		});
	ol.actionPerformed(null);
	setMapsExtents(); //refresh maps and labels
	}

}
