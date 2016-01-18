/**
 * JoinTableExtension.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	27 oct. 2004
 * 
 */
package reso.jump.joinTable;

import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	27 oct. 2004
 * 
 */
public class JoinTableExtension extends Extension {

	public void configure(PlugInContext context) throws Exception {
		JoinTablePlugIn joinTablePlugIn = new JoinTablePlugIn();
		joinTablePlugIn.initialize(context);
	}
}
