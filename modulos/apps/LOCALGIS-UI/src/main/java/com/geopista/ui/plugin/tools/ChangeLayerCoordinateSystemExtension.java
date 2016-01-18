/**
 * ChangeLayerCoordinateSystemExtension.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 10-oct-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.tools;

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
