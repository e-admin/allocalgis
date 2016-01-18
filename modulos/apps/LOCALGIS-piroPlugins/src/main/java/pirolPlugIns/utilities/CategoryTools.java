/**
 * CategoryTools.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 10.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: CategoryTools.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/CategoryTools.java,v $
 */
package pirolPlugIns.utilities;

import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * 
 *
 * @author Ole Rahn
 * @author FH Osnabrück - University of Applied Sciences Osnabrück,
 * Project: PIROL (2005),
 * Subproject: Daten- und Wissensmanagement
 * 
 */
public class CategoryTools extends ToolToMakeYourLifeEasier  {
    
    /**
     * Get the (exatcly one) selected category from the LayerNamePanel. Returns the first selected category, if more than one is selected or WORKING if none is selected.
     *@param context the current PlugInContext
     *@return the currently selected cetagory or WORKING if none is selected
     */
    public static String getActiveCategory(PlugInContext context) {
		return context.getLayerNamePanel().getSelectedCategories().isEmpty() ? StandardCategoryNames.WORKING
				: context.getLayerNamePanel().getSelectedCategories()
						.iterator().next().toString();
	}
}
