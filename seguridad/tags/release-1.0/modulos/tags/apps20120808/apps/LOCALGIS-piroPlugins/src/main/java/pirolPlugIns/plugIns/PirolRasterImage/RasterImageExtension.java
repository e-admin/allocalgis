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
