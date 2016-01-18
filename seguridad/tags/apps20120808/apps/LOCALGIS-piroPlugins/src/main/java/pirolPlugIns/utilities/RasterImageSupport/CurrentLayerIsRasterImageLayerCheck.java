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
