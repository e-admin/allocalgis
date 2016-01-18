/*
 * Created on 08.12.2004
 *
 * CVS header information:
 *  $RCSfile: ObjectComparator.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/ObjectComparator.java,v $
 */
package pirolPlugIns.utilities;

import java.math.BigDecimal;
import java.math.BigInteger;

import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class ObjectComparator {
    public static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    
	public static int compare( Object o1, Object o2 ){
		
		Double value1, value2;
		
		value1 = new Double(ObjectComparator.getDoubleValue(o1));
		value2 = new Double(ObjectComparator.getDoubleValue(o2));
		
		if (value1.doubleValue() ==Double.NaN || value2.doubleValue()==Double.NaN)
		    logger.printError("got NAN");
		
		return value1.compareTo(value2);		
	}

    /**
     * Method to generate a <code>double</code> value out of different number objects.
     *@param o
     *@return a double value representing to given object or <code>Double.NAN</code> if it can't be parsed
     */
	public static double getDoubleValue(Object o){
		double value = Double.NaN;
		
		if (o==null){
		    logger.printMinorError("got NULL value");
		} else {
			if (o.getClass() == Integer.class){
				value = ((Integer)o).doubleValue();
			} else if (o.getClass() == Double.class){
				value = ((Double)o).doubleValue();
            } else if (o.getClass() == Float.class){
                value = ((Float)o).doubleValue();
			} else if (o.getClass() == BigDecimal.class){
                value = ((BigDecimal)o).doubleValue();
            } else if (o.getClass() == BigInteger.class){
                value = ((BigInteger)o).doubleValue();
            } else if (o.getClass() == Long.class){
                value = ((Long)o).doubleValue();
            } else if (o.getClass() == Short.class){
                value = ((Short)o).doubleValue();
            } else if (o.getClass() == Byte.class){
                value = ((Byte)o).doubleValue();
            } else {
			    logger.printError(" can't get double value... - " + o.getClass().getName());
			}
		}
		return value;
	}
}
