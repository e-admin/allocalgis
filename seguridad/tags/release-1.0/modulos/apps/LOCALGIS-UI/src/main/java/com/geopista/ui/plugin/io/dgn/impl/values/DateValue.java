package com.geopista.ui.plugin.io.dgn.impl.values;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Wrapper sobre el tipo Date
 *
 * @author Fernando González Cortés
 */
public class DateValue extends Value {
    private Date value;

    /**
     * Creates a new DateValue object.
     *
     * @param d DOCUMENT ME!
     */
    public DateValue(Date d) {
        value = d;
    }

    /**
     * Creates a new DateValue object.
     */
    public DateValue() {
    }

    /**
     * Establece el valor
     *
     * @param d valor
     */
    public void setValue(java.util.Date d) {
        value = new Date(d.getTime());
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value equals(DateValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.equals(value.value));
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value greater(DateValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.value) > 0);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value greaterEqual(DateValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.value) >= 0);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value less(DateValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.value) < 0);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value lessEqual(DateValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.value) <= 0);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value notEquals(DateValue value) throws IncompatibleTypesException {
        return new BooleanValue(!this.value.equals(value.value));
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value equals(Value value) throws IncompatibleTypesException {
        return value.equals(this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value greater(Value value) throws IncompatibleTypesException {
        return value.less(this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value greaterEqual(Value value) throws IncompatibleTypesException {
        return value.lessEqual(this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value less(Value value) throws IncompatibleTypesException {
        return value.greater(this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value lessEqual(Value value) throws IncompatibleTypesException {
        return value.greaterEqual(this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value notEquals(Value value) throws IncompatibleTypesException {
        return value.notEquals(this);
    }
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		return sdf.format(value);
	}
}
