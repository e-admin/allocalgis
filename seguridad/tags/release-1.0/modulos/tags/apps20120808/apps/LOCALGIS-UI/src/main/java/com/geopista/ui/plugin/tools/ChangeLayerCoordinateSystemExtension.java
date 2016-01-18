/*
 * 
 * Created on 10-oct-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.tools;

import com.geopista.ui.plugin.io.arcIMS.AddArcIMSQueryPlugIn;
import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ChangeLayerCoordinateSystemExtension extends Extension
{

public String getName()
{
return "Geopista Change Layer Projection Tool";
}
public String getVersion()
{

return "0.1";
}
public void configure(PlugInContext context) throws Exception {
    new ChangeLayerCoordinateSystem().initialize(context);
}

}
