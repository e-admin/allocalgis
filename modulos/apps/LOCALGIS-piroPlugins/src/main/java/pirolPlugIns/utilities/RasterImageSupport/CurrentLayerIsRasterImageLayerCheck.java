/**
 * CurrentLayerIsRasterImageLayerCheck.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 05.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: CurrentLayerIsRasterImageLayerCheck.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/RasterImageSupport/CurrentLayerIsRasterImageLayerCheck.java,v $
 */
package pirolPlugIns.utilities.RasterImageSupport;

import javax.swing.JComponent;

import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * Enable check (to controll menu items) that checks, if the selected layer is a RasterImage Layer<br>
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class CurrentLayerIsRasterImageLayerCheck implements EnableCheck {

    protected PlugInContext context = null;
    
    protected EnableCheck checker = null;

    public CurrentLayerIsRasterImageLayerCheck(PlugInContext context) {
        super();
        this.context = context;
        this.checker = new EnableCheckFactory(context.getWorkbenchContext()).createExactlyNLayerablesMustBeSelectedCheck(1, RasterImageLayer.class);
    }
    
    /**
     *@inheritDoc
     */
    public String check(JComponent component) {
        return this.checker.check(component);        
    }

}
