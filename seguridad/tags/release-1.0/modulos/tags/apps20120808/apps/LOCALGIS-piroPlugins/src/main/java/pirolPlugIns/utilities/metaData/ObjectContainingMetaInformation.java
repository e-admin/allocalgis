/*
 * Created on 05.01.2006 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ObjectContainingMetaInformation.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:01 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/metaData/ObjectContainingMetaInformation.java,v $
 */
package pirolPlugIns.utilities.metaData;

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
public interface ObjectContainingMetaInformation {
    
    public MetaDataMap getMetaInformation();

    public void setMetaInformation(MetaDataMap metaInformation);

}
