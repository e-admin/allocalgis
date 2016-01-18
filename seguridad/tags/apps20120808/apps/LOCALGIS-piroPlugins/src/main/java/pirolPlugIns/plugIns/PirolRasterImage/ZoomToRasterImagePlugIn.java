/*
 * Created on 04.01.2006 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ZoomToRasterImagePlugIn.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:31 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/ZoomToRasterImagePlugIn.java,v $
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import com.vividsolutions.jump.workbench.plugin.PlugInContext;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.LayerTools;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

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
public class ZoomToRasterImagePlugIn extends StandardPirolPlugIn {

    public ZoomToRasterImagePlugIn() {
        super(new PersonalLogger(DebugUserIds.USER_Ole));
    }

    /**
     *@inheritDoc
     */
    public String getIconString() {
        return null;
    }

    /**
     *@inheritDoc
     */
    public boolean execute(PlugInContext context) throws Exception {
        
        RasterImageLayer rLayer = (RasterImageLayer) LayerTools.getSelectedLayerable(context, RasterImageLayer.class);
        
        if (rLayer==null){
            StandardPirolPlugIn.warnUser(context,PirolPlugInMessages.getString("no-layer-selected"));
            return this.finishExecution(context, false);
        }
        
        context.getLayerViewPanel().getViewport().zoom(rLayer.getEnvelope());
        
        return this.finishExecution(context, true);
    }

}
