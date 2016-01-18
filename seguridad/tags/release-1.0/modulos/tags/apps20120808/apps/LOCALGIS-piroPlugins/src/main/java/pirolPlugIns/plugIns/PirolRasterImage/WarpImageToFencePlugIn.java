/*
 * Created on 09.01.2006 for PIROL
 *
 * CVS header information:
 *  $RCSfile: WarpImageToFencePlugIn.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:31 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/WarpImageToFencePlugIn.java,v $
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

import pirolPlugIns.cursorTools.MyEnableCheckFactory;
import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.LayerTools;
import pirolPlugIns.utilities.PlugInContextTools;
import pirolPlugIns.utilities.SelectionTools;
import pirolPlugIns.utilities.RasterImageSupport.CurrentLayerIsRasterImageLayerCheck;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * PlugIn to warp a RasterImage to the bounding box of the Fence. 
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
public class WarpImageToFencePlugIn extends StandardPirolPlugIn {


    public WarpImageToFencePlugIn() {
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
            StandardPirolPlugIn.warnUser(context,PirolPlugInMessages.getString("no-layer-selected")); //$NON-NLS-1$
            return this.finishExecution(context, false);
        }
        
        Geometry fence = SelectionTools.getFenceGeometry(context);
        Envelope envWanted = fence.getEnvelopeInternal();
        
        rLayer.setEnvelope(envWanted);
        
        return this.finishExecution(context, true);
    }
    
    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        if (StandardPirolPlugIn.checkFactory == null){
            StandardPirolPlugIn.checkFactory = new MyEnableCheckFactory(workbenchContext);
        }
        MultiEnableCheck multiEnableCheck = new MultiEnableCheck();
        
        multiEnableCheck.add( StandardPirolPlugIn.checkFactory.createExactlyNLayerablesMustBeSelectedCheck(1, RasterImageLayer.class) );
        multiEnableCheck.add( StandardPirolPlugIn.checkFactory.createFenceMustBeDrawnCheck() );
        
        EnableCheck enableCheck = new CurrentLayerIsRasterImageLayerCheck(PlugInContextTools.getContext(workbenchContext));     
        multiEnableCheck.add(enableCheck);
        
        return multiEnableCheck;
    }

}
