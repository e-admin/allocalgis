/*
 * 
 * Created on 26-sep-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.io.dgn;

import java.io.File;

import com.geopista.ui.plugin.io.mrsid.AddSIDLayerPlugIn;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MenuNames;

public class DGNExtension extends Extension
{

	
	   public void configure(PlugInContext context) throws Exception 
	    {
	       
	        DGNDataSourcePlugIn plug=new DGNDataSourcePlugIn();
	        
	        plug.initialize(context);
	       
	       
	    }

}
