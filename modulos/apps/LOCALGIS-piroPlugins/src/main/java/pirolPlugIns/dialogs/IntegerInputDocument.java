/**
 * IntegerInputDocument.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 23.05.2005 for PIROL
 * 
 * CVS header information:
 * $RCSfile: IntegerInputDocument.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:46 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/IntegerInputDocument.java,v $
 */
package pirolPlugIns.dialogs;

import javax.swing.JDialog;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A plain document for Integer values only. The default element structure for 
 * this document is a map of the lines in the text. The Element returned by 
 * getDefaultRootElement is a map of the lines, and each child element 
 * represents a line. This model does not maintain any character level 
 * attributes, but each line can be tagged with an arbitrary set of attributes. 
 * Line to offset, and offset to line translations can be quickly performed 
 * using the default root element. The structure information of the 
 * DocumentEvent's fired by edits will indicate the line structure changes. 
 * 
 * 
 * The default content storage management is performed by a gapped buffer 
 * implementation (GapContent). It supports editing reasonably large documents 
 * with good efficiency when the edits are contiguous or clustered, as is 
 * typical. 
 * @author Carsten Schulze
 * @author <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck
 * <br>Project: PIROL (2005)
 * <br>Subproject: Daten- und Wissensmanagement
 */
public class IntegerInputDocument extends PlainDocument {

    private static final long serialVersionUID = 3496661401696742340L;
    
    /**just to "beep"!*/
	private static JDialog dialog = new JDialog();
	/**
	 * Inserts some content into the document. Inserting content causes a write 
	 * lock to be held while the actual changes are taking place, followed by 
	 * notification to the observers on the thread that grabbed the write lock.
	 * @param offset the starting offset >= 0
	 * @param string the string to insert; does nothing with null/empty strings
	 * @param attributeSet the attributes for the inserted content
	 * @throws BadLocationException if the given insert position is not a valid 
	 * position within the document
	 */
	public void insertString(int offset, String string, AttributeSet attributeSet) throws BadLocationException{
		try{
			Integer.parseInt(string);
			super.insertString(offset,string,attributeSet);
		}
		catch(NumberFormatException e){
			dialog.getToolkit().beep();
		}
	}
}
