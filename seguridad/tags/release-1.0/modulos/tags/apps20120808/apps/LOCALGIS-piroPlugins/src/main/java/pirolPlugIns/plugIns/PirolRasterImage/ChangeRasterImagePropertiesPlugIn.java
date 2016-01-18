/*
 * Created on 03.07.2005
 *
 * CVS information:
 *  $Author: miriamperez $
 *  $Date: 2009/07/03 12:31:31 $
 *  $ID$
 *  $Revision: 1.1 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/ChangeRasterImagePropertiesPlugIn.java,v $
 *  $Log: ChangeRasterImagePropertiesPlugIn.java,v $
 *  Revision 1.1  2009/07/03 12:31:31  miriamperez
 *  Rama única LocalGISDOS
 *
 *  Revision 1.1  2006/05/18 08:23:26  jpcastro
 *  Pirol RasterImage
 *
 *  Revision 1.3  2006/01/11 12:40:12  orahn
 *  soll sich nicht mehr selbst initialisieren, sondern wird vom RasterImagePlugIn initialisiert
 *
 *  Revision 1.2  2006/01/05 14:55:42  orahn
 *  Einige grundlegende Klassen nach utilities/RasterImageSupport verschoben
 *
 *  Revision 1.1  2006/01/04 18:11:04  orahn
 *  neues Model für RasterImage-Support - erfordert u.U. noch Kern-Patch
 *
 *  Revision 1.3  2005/07/27 11:06:57  orahn
 *  +speed regler eingebaut (s. bug #68)
 *  +kleinere optimierungen
 *
 *  Revision 1.2  2005/07/05 16:31:33  orahn
 *  + labels für transparenz slider
 *
 *  Revision 1.1  2005/07/05 15:23:48  orahn
 *  Steuerung der RasterImage Transparenzen durch Kontext-Menü
 *
 *  Revision 1.1  2005/07/04 08:42:32  orahn
 *  Vorbereitung: Dialog zum Einstellen der Transparenzen bei RasterImages
 *
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import pirolPlugIns.cursorTools.MyEnableCheckFactory;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.LayerTools;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * 
 * PlugIn that adds a menu item to the layer context menu, that enables changing RasterImage layer
 * transparencies add any time.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class ChangeRasterImagePropertiesPlugIn extends
        StandardPirolPlugIn {

    public ChangeRasterImagePropertiesPlugIn(){
        super(new PersonalLogger(DebugUserIds.USER_Ole));
    }
    
    /**
     * @inheritDoc
     */
    public String getIconString() {
        return null;
    }

    /**
     *@inheritDoc
     */
    public boolean execute(PlugInContext context) throws Exception {
        RasterImageLayer rlayer = (RasterImageLayer)LayerTools.getSelectedLayerable(context, RasterImageLayer.class);
        
        if (rlayer==null){
            StandardPirolPlugIn.warnUser(context,"no layer selected - canceling");
            return this.finishExecution(context, false);
        } 
        
        ChangeRasterImageStyleDialog dialog = new ChangeRasterImageStyleDialog(rlayer, context.getWorkbenchFrame(), this.getName(), true);
        
        dialog.setVisible(true);
        
        if (!dialog.wasOkClicked())
            return this.finishExecution(context, false);

        rlayer.fireAppearanceChanged();
        
        return this.finishExecution(context, true);
    }
    
    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        if (StandardPirolPlugIn.checkFactory == null){
            StandardPirolPlugIn.checkFactory = new MyEnableCheckFactory(workbenchContext);
        }
        MultiEnableCheck multiEnableCheck = new MultiEnableCheck();
        
        multiEnableCheck.add( StandardPirolPlugIn.checkFactory.createExactlyNLayerablesMustBeSelectedCheck(1, RasterImageLayer.class) );
        
        
        return multiEnableCheck;
	}
    
    public void initialize(PlugInContext context) throws Exception {}

}
