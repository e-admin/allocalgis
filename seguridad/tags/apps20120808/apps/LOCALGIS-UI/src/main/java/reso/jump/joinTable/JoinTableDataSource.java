/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	26 oct. 2004
 * 
 */
package reso.jump.joinTable;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	26 oct. 2004
 * 
 */
public interface JoinTableDataSource {
	
	public ArrayList getFieldNames();
	
	public ArrayList getFieldTypes();
	
	public Hashtable buildTable (int keyIndex);
    
    public Hashtable buildTableMultipleKey (int keyIndex); 
	
}
