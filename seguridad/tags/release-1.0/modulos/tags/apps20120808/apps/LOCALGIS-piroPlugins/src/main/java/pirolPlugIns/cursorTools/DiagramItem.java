/*
 * Created on 14.12.2004
 *
 * CVS header information:
 *  $RCSfile: DiagramItem.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:56 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/cursorTools/DiagramItem.java,v $s
 */
package pirolPlugIns.cursorTools;

import java.util.ArrayList;
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
public abstract class DiagramItem {
	protected List FIDs = new ArrayList();
	protected int index;
	protected String rangeString;
	
	public DiagramItem(String rangeString, int index) {
		this.rangeString = rangeString;
		this.index = index;
	}
	
	public String getRangeString() {
		return rangeString;
	}
	
	public boolean addFID(Integer FID){
		return this.FIDs.add(FID);
	}
    
    public boolean addFID(int FID){
        return this.addFID(new Integer(FID));
    }

	public List getFIDs() {
		return FIDs;
	}

	public int getNumItems(){
		return this.FIDs.size();
	}

	public int getIndex() {
		return index;
	}
}
