/**
 * StandardPirolToolBox.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 20.10.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: StandardPirolToolBox.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:07 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/toolBoxes/StandardPirolToolBox.java,v $
 */
package pirolPlugIns.toolBoxes;

import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import pirolPlugIns.PirolPlugInSettings;
import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.PlugInContextTools;

import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelListener;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxPlugIn;

/**
 * Abstract base class for ToolBoxes in the PIROL context.
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
public abstract class StandardPirolToolBox extends ToolboxPlugIn implements LayerNamePanelListener, FocusListener {
    
    protected ImageIcon ICON = null;
    protected String imgName = "rasterToolBox.png";
    protected ToolboxDialog toolbox = null;
    
    protected String KEY = null;
    
    public StandardPirolToolBox(String imgName){
        this.KEY = this.getClass().getName();
        this.imgName = imgName;
        
    }
    
    /**
     * Name of the Tool to be shown in the menus or as a tooltip in JUMP.<br>
     * Looks for a key (the PlugIn's name with no path) in the i18n resources, if none is found
     * the standard jump name generation will be used.
     */
    public String getName() {
        try {
            return PirolPlugInMessages.getString(this.getShortClassName());
        } catch (RuntimeException e) {
            return super.getName();
        }
        
    }
    
    public String getShortClassName(){
        int pointPos = this.getClass().getName().lastIndexOf(".");
        if (pointPos > -1) {
            return this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1);
        }
        return this.getClass().getName();
    }
    
    public void initialize(PlugInContext context) throws Exception {
        if ( this.ICON == null ){
            InputStream in = getClass().getResourceAsStream(imgName);
            Image img = ImageIO.read(in).getScaledInstance(PirolPlugInSettings.StandardToolIconWidth, PirolPlugInSettings.StandardToolIconHeight, BufferedImage.SCALE_SMOOTH);
            this.ICON = new ImageIcon(img);
        }
        
        context.getWorkbenchContext().getWorkbench().getBlackboard().put(this.KEY, this);
        
        this.createMainMenuItem(new String[] { PirolPlugInSettings.getName_PirolMenu() },
                GUIUtil.toSmallIcon(this.ICON), context.getWorkbenchContext());

    }

    /**
     *@inheritDoc
     */
    protected abstract void initializeToolbox(ToolboxDialog toolbox);
    
    

    public void layerSelectionChanged() {
        this.toolbox.updateEnabledState();
    }

    public void focusGained(FocusEvent e) {
        PlugInContextTools.getContext(toolbox).getLayerNamePanel().addListener(this);
        PlugInContextTools.getContext(toolbox).getWorkbenchContext().getLayerNamePanel().addListener(this);

        toolbox.updateEnabledState();
        
    }

    public void focusLost(FocusEvent e) {
        PlugInContextTools.getContext(toolbox).getLayerNamePanel().removeListener(this);
        PlugInContextTools.getContext(toolbox).getWorkbenchContext().getLayerNamePanel().removeListener(this);

        toolbox.updateEnabledState();
    }

}
