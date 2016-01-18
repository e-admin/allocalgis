/**
 * CustomFillPatternExamplePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.renderer.style;

import java.util.ArrayList;
import java.util.Collection;

import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;


public class CustomFillPatternExamplePlugIn extends AbstractPlugIn {
    public void initialize(PlugInContext context) throws Exception {
        Collection customFillPatterns = (Collection) context.getWorkbenchContext()
                                                            .getIWorkbench()
                                                            .getBlackboard()
                                                            .get(FillPatternFactory.CUSTOM_FILL_PATTERNS_KEY,
                new ArrayList());
        customFillPatterns.add(new WKTFillPattern(1, 10, "LINESTRING(3 3, 3 -3, -3 -3, 3 3)"));
        customFillPatterns.add(new ImageFillPattern(IconLoader.class,
                "Favorite.gif"));
    }
}
