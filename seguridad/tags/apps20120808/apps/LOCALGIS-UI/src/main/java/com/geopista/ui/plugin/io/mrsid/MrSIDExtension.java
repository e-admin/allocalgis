
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * JUMP is Copyright (C) 2003 Vivid Solutions
 *
 * This program implements extensions to JUMP and is
 * Copyright (C) 2004 Integrated Systems Analysts, Inc.
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

package com.geopista.ui.plugin.io.mrsid;


import java.io.File;

import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class MrSIDExtension extends Extension {
    
    public void configure(PlugInContext context) throws Exception 
    {
//        context.getFeatureInstaller().addMenuSeparator("Layer");
      System.out.println("Configurando MrSIDPlugin.....");
        AddSIDLayerPlugIn plug=new AddSIDLayerPlugIn();
        
        plug.initialize(context);
        File tmpdir=new File(plug.TMP_PATH);
        if (!tmpdir.exists())
        	tmpdir.mkdirs();
       
    }
} 

//
//
