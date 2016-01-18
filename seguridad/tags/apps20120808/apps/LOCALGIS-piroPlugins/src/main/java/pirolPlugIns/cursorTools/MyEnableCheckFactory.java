/*
 * Created on 01.12.2004
 *
 * CVS header information:
 *  $RCSfile: MyEnableCheckFactory.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:56 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/cursorTools/MyEnableCheckFactory.java,v $s
 */
package pirolPlugIns.cursorTools;


import javax.swing.JComponent;

import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;

/**
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class MyEnableCheckFactory extends com.vividsolutions.jump.workbench.plugin.EnableCheckFactory {

	    protected WorkbenchContext workbenchContext;

	    public MyEnableCheckFactory(WorkbenchContext workbenchContext) {
	    	super(workbenchContext);
	        this.workbenchContext = workbenchContext;
	    }

	    public EnableCheck createExactlyNLayerablesMustBeSelectedCheck(
	        final int n,
	        final Class layerableClass) {
	        return new EnableCheck() {
	            public String check(JComponent component) {
	            	if (workbenchContext==null || workbenchContext.getLayerManager()==null){
	            		return "There are no layers loaded/created, yet"; 
	            	}
	                return (
	                    n != (workbenchContext.getLayerNamePanel())
	                            .selectedNodes(layerableClass)
	                            .size())
	                    ? ("Exactly " + n + " layer" + StringUtil.s(n) + " must be selected")
	                    : null;
	            }
	        };
	    }


	    public EnableCheck createAtLeastNLayersMustExistCheck(final int n) {
	        return new EnableCheck() {
	            public String check(JComponent component) {
	            	if (workbenchContext==null || workbenchContext.getLayerManager()==null){
	            		return "There are no layers loaded/created, yet"; 
	            	}
	                return (n > workbenchContext.getLayerManager().size())
	                    ? ("At least " + n + " layer" + StringUtil.s(n) + " must exist")
	                    : null;
	            }
	        };
	    }
	    
	    public EnableCheck createFenceMustBeDrawnCheck() {
	        return new EnableCheck() {
	            public String check(JComponent component) {
	            	if (null == workbenchContext)
	            		return "no workbenchContext!";
	            	else if (null == workbenchContext.getLayerViewPanel())
	            		return "no LayerViewPanel!";
	                else if (null == workbenchContext.getLayerViewPanel().getFence())
	                    return "A fence must be drawn";

	            	return null;
	            }
	        };
	    }


}
