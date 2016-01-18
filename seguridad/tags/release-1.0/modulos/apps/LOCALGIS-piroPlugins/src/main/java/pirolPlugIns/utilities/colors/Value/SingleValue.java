/*
 * Created on 14.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: SingleValue.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:04 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/Value/SingleValue.java,v $
 */
package pirolPlugIns.utilities.colors.Value;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Represents a single value, that can have a color.
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
public class SingleValue extends ColoredValue {

    protected Object value = null;
    protected boolean numeric = true;
    
    public SingleValue(Object value, int index) {
        super(index);
        this.value = value;
        
        this.numeric = Number.class.isInstance(value);
    }
    
    /**
     * for storing/reading to/from a file
     *@param index index
     */
    protected SingleValue(int index) {
        super(index);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Comparable value) {
        this.value = value;
        this.numeric = Number.class.isInstance(value);
        this.fireValueChangeEvent();
    }

    /**
     *@inheritDoc
     */
    public boolean isResponsibleForValue(Object value) {
        return this.value.equals(value);
    }

    /**
     *@inheritDoc
     */
    public boolean isResponsibleForValue(double value) {
        return this.isResponsibleForValue(new Double(value));
    }

    /**
     *@inheritDoc
     */
    public Comparable getCompareValue() {
        return (Comparable)this.value;
    }

    /**
     *@inheritDoc
     */
    public String toString() {
        if (String.class.isInstance(this.value)){
            return "\"" + ((String)this.value).trim() + "\"";
        }
        return this.value.toString();
    }

    /**
     *@inheritDoc
     */
    protected String getParseableValueString() {
        return this.value.getClass().getName() + ColoredValue.valueSeparatorString + this.value.toString();
    }

    /**
     *@inheritDoc
     */
    protected void readParseableValueString(String valueString) {
        String[] fields = valueString.split(ColoredValue.valueSeparatorString);
        
        if (fields[0].equals(String.class.getName())){
            this.value = fields[1];
        } else if (fields[0].equals(Double.class.getName())) {
            this.value = new Double(fields[1]);
        } else if (fields[0].equals(Integer.class.getName())) {
            this.value = new Integer(fields[1]);
        } else if (fields[0].equals(Float.class.getName())) {
            this.value = new Float(fields[1]);
        } else if (fields[0].equals(Long.class.getName())) {
            this.value = new Long(fields[1]);
        } else if (fields[0].equals(BigDecimal.class.getName())) {
            this.value = new BigDecimal(fields[1]);
        } else if (fields[0].equals(BigInteger.class.getName())) {
            this.value = new BigInteger(fields[1]);
        } else {
            throw new IllegalArgumentException("unknown value format: " + fields[0] + ", " + fields[1]);
        }
        this.numeric = Number.class.isInstance(this.value);
    }

    /**
     *@inheritDoc
     */
    public boolean isNumeric() {
        return this.numeric;
    }

}
