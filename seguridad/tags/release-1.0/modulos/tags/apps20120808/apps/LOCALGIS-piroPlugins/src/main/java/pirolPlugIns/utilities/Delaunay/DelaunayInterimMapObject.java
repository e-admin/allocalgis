/*
 * Created on 21.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: DelaunayInterimMapObject.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/Delaunay/DelaunayInterimMapObject.java,v $
 */
package pirolPlugIns.utilities.Delaunay;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Class that allows to store a map of Envelopes and DelaunayCalculators for a layer.  
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
public class DelaunayInterimMapObject {
    
    private Map Evelope2Delaunay;

    
    public DelaunayInterimMapObject() {
        super();
        this.Evelope2Delaunay = Collections.synchronizedMap(new HashMap());
    }
    
    

    public boolean containsKey(Envelope arg0) {
        return Evelope2Delaunay.containsKey(arg0);
    }
    public DelaunayCalculator get(Envelope arg0) {
        return (DelaunayCalculator)Evelope2Delaunay.get(arg0);
    }
    public Object put(Envelope arg0, DelaunayCalculator arg1) {
        return Evelope2Delaunay.put(arg0, arg1);
    }
    public Object remove(Envelope arg0) {
        return Evelope2Delaunay.remove(arg0);
    }
}
