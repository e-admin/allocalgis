/*
 * Created on 04.01.2006 for PIROL
 *
 * CVS header information:
 *  $RCSfile: RasterImageLayerRendererFactory.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/RasterImageSupport/RasterImageLayerRendererFactory.java,v $
 */
package pirolPlugIns.utilities.RasterImageSupport;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;

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
public class RasterImageLayerRendererFactory implements Renderer.ContentDependendFactory {

    protected WorkbenchContext wbContext = null;
    
    public RasterImageLayerRendererFactory(WorkbenchContext wbContext) {
        super();
        this.wbContext = wbContext;
    }

    /**
     *@inheritDoc
     */
    public Renderer create(Object contentID) {
        return new RasterImageRenderer(contentID, this.wbContext.getLayerViewPanel());
    }

}
