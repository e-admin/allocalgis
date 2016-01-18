/*
 * Created on 07.12.2004
 *
 * CVS header information:
 *  $RCSfile: featureSelector.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:56 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/cursorTools/featureSelector.java,v $
 */
package pirolPlugIns.cursorTools;

import java.util.List;

/**
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public interface featureSelector {
	
	public void selectFeatures( List features, int kat );

}
