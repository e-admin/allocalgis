/*
 * Created on 09.03.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ObjectInputDocument.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/ObjectInputDocument.java,v $
 */
package pirolPlugIns.dialogs;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class ObjectInputDocument extends PlainDocument {

    private static final long serialVersionUID = -8978874850916788716L;

    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        String clearedStr = str.substring(0);

        clearedStr = clearedStr.replaceAll("[^0-9a-zA-Z()-.]", "");

        if (clearedStr.length() > 0) {
            super.insertString(offs, clearedStr, a);
        }
    }
}
