package com.geopista.ui.plugin.clipboard;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/

import com.geopista.ui.plugin.GeopistaDeleteSelectedItemsPlugIn;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MacroPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.DeleteSelectedItemsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.clipboard.CopySelectedItemsPlugIn;


public class GeopistaCutSelectedItemsPlugIn  extends MacroPlugIn {
    public GeopistaCutSelectedItemsPlugIn () {
        super(new PlugIn[] {
                new CopySelectedItemsPlugIn(),
                new GeopistaDeleteSelectedItemsPlugIn()
            });
    }

    public String getName() {
        return AbstractPlugIn.createName(getClass());
    }
    
    public String getNameWithMnemonic() {
        return StringUtil.replace(getName(), "t", "&t", false);
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        return DeleteSelectedItemsPlugIn.createEnableCheck(workbenchContext);
    }
}
