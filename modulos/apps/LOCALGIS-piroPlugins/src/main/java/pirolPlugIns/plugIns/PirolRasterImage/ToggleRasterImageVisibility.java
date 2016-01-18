/**
 * ToggleRasterImageVisibility.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 04.01.2006 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ToggleRasterImageVisibility.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:31 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/ToggleRasterImageVisibility.java,v $
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.LayerTools;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jump.workbench.WorkbenchContext;
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
public class ToggleRasterImageVisibility extends StandardPirolPlugIn implements PopupMenuListener {

    protected WorkbenchContext wbContext = null;
    
    protected JCheckBoxMenuItem menuItem = null;
    
    public ToggleRasterImageVisibility(WorkbenchContext wbContext) {
        super(new PersonalLogger(DebugUserIds.USER_Ole));
        this.wbContext = wbContext;
    }

    /**
     *@inheritDoc
     */
    public String getIconString() {
        return "visibility.png";
    }

    /**
     *@inheritDoc
     */
    public boolean execute(PlugInContext context) throws Exception {
        
        RasterImageLayer rLayer = (RasterImageLayer) LayerTools.getSelectedLayerable(context, RasterImageLayer.class);
        
        if (rLayer==null){
            StandardPirolPlugIn.warnUser(context,PirolPlugInMessages.getString("no-layer-selected")); //$NON-NLS-1$
            return this.finishExecution(context, false);
        }
        
        rLayer.setVisible(!rLayer.isVisible());
        
        rLayer.fireAppearanceChanged();
        
        return this.finishExecution(context, true);
    }

    /**
     *@param arg0
     */
    public void popupMenuCanceled(PopupMenuEvent arg0) {
    }

    /**
     *@param arg0
     */
    public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
    }

    /**
     *@param arg0
     */
    public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
        RasterImageLayer rLayer = (RasterImageLayer) LayerTools.getSelectedLayerable(this.wbContext.createPlugInContext(), RasterImageLayer.class);
        
        if (rLayer==null) return;
        
        if (this.menuItem!=null)
            menuItem.setSelected(rLayer.isVisible());
        
        
    }

    public JCheckBoxMenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(JCheckBoxMenuItem menuItem) {
        this.menuItem = menuItem;
    }

    
}
