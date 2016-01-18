/**
 * MapToolTipsPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;


public class MapToolTipsPlugIn extends AbstractPlugIn {
    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(
            checkFactory
                .createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(new EnableCheck() {
            public String check(JComponent component) {
                ((JCheckBoxMenuItem) component).setSelected(
                    workbenchContext.getLayerViewPanel().getToolTipWriter().isEnabled());
                return null;
            }
        });
    }
    public String getName() {
        //Can't use auto-naming, which produces "Map Tool Tips"; and Unix/Windows
        //CVS issues will occur if I rename MapToolTipsPlugIn to MapTooltipsPlugIn. [Jon Aquino]
		return "Map Tooltips";
	}
    public boolean execute(PlugInContext context) throws Exception {
        context.getLayerViewPanel().getToolTipWriter().setEnabled(
            !context.getLayerViewPanel().getToolTipWriter().isEnabled());
        return true;
    }
}
