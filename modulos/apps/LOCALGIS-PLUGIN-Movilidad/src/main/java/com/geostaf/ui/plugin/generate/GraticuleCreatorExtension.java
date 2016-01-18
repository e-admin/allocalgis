/**
 * GraticuleCreatorExtension.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*************************************************
 * created on   :     19.01.2006
 * last modified:     22.01.2006
 *
 * author:            Ruggero Valentinotti
 *               
 *   http://digilander.libero.it/valruggero/
 *   
 *   
 * LICENZE:	GPL 2.0 or (at your option) any later version
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 *   
 *************************************************/

package com.geostaf.ui.plugin.generate;

import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * @description
 *  - this class loads the GraticuleCreatorPlugIn into Jump <p>
 *  - class has to be called "Extension" on the end of classname
 *    to use the PlugIn in Jump
 *
 *  @author sstein
 */
public class GraticuleCreatorExtension extends Extension{

    /**
     * calls PlugIn using class method xplugin.initialize()
     */
    public void configure(PlugInContext context) throws Exception{
        new GraticuleCreatorPlugIn().initialize(context);
    }

}