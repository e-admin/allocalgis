/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 * 
 *
 * $Id: AddArcIMSQueryExtension.java,v 0.1 20041110 
 *
 * Copyright (C) 2004 Jan Ruzicka jan.ruzicka@vsb.cz
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

/**
 *
 * <p>Title: AddArcIMSQueryExtension</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Jan Ruzicka jan.ruzicka@vsb.cz
 * @version 0.1
 */

package com.geopista.ui.plugin.io.arcIMS;

import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;


public class AddGeopistaArcIMSQueryExtension extends Extension {

	public String getName()
	{
	return "Geopista ArcIMS query extension";
	}
	public String getVersion()
	{
	
	return "0.1";
	}
    public void configure(PlugInContext context) throws Exception {
        new AddArcIMSQueryPlugIn().initialize(context);
    }
}


