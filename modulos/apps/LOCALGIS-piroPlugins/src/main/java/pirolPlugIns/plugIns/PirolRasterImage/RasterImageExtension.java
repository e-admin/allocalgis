/**
 * RasterImageExtension.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 09.01.2006 for PIROL
 *
 * CVS header information:
 *  $RCSfile: RasterImageExtension.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:31 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/RasterImageExtension.java,v $
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * TODO: comment class
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2006),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class RasterImageExtension extends Extension {

    /**
     *
     */
    public RasterImageExtension() {
        super();
    }

    /**
     *@param context
     *@throws Exception
     */
    public void configure(PlugInContext context) throws Exception {
        new RasterImagePlugIn().initialize(context);
    }

}
