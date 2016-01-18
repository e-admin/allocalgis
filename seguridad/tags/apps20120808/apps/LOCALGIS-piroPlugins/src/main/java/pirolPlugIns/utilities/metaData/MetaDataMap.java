/*
 * Created on 17.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: MetaDataMap.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:01 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/metaData/MetaDataMap.java,v $
 */
package pirolPlugIns.utilities.metaData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class to store meta information of various kinds in a map.
 * By default an object of this class will be added to the properties of a 
 * DataSource (and is hopefully saved, when e.g. the task is saved). 
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
public class MetaDataMap {
    
    protected HashMap metaData = new HashMap(); 
    
    /**
     * constructor (needs to be parameterless in order for java2xml to be able to load it)
     */
    public MetaDataMap() {
        super();
    }
    
    /**
     * Adds a new meta information to the map
     *@param key the kind of information
     *@param value the information itself
     */
    public void addMetaInformation(Object key, Object value){
        this.metaData.put(key, value);
    }


    /**
     * Gets all meta information in one map object
     *@return all stored meta information
     */
    public HashMap getMetaData() {
        return metaData;
    }
    /**
     * Sets (overwrites) the stored meta information
     *@param metaData
     */
    public void setMetaData(HashMap metaData) {
        this.metaData = metaData;
    }

    public void clear() {
        metaData.clear();
    }

    public boolean containsKey(Object key) {
        return metaData.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return metaData.containsValue(value);
    }

    public Set keySet() {
        return metaData.keySet();
    }

    public Object remove(Object key) {
        return metaData.remove(key);
    }

    public Object get(String key) {
        return metaData.get(key);
    }

    public void putAll(Map arg0) {
        metaData.putAll(arg0);
    }
    
}
