/*
 * Created on 19.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ColoringChangedListener.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:05 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/ColoringChangedListener.java,v $
 */
package pirolPlugIns.utilities.colors;

/**
 * Interface for Object listening to changes in a ColorsChooserPanel
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
public interface ColoringChangedListener {
    
    public void coloringChanged(ColorGenerator colorGen);
}
