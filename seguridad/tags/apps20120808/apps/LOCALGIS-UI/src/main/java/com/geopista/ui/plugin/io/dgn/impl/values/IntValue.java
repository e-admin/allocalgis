package com.geopista.ui.plugin.io.dgn.impl.values;



/**
 * Wrapper sobre el tipo int
 *
 * @author Fernando González Cortés
 */
public class IntValue extends NumericValue {
    private int value;

    /**
     * Creates a new IntValue object.
     *
     * @param value DOCUMENT ME!
     */
    public IntValue(int value) {
        this.value = value;
    }

    /**
     * Creates a new IntValue object.
     */
    public IntValue() {
    }

    /**
     * Establece el valor de este objeto
     *
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Obtiene el valor de este objeto
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "" + value;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.Value)
     */
    public Value suma(Value v) throws IncompatibleTypesException {
        return v.suma((IntValue) this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.IntValue)
     */
    public Value suma(IntValue value) throws IncompatibleTypesException {
        IntValue ret = new IntValue();
        ret.setValue(this.value + value.getValue());

        return ret;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.LongValue)
     */
    public Value suma(LongValue value) throws IncompatibleTypesException {
        LongValue ret = new LongValue();
        ret.setValue(this.value + value.getValue());

        return ret;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.FloatValue)
     */
    public Value suma(FloatValue value) throws IncompatibleTypesException {
        FloatValue ret = new FloatValue();
        ret.setValue(this.value + value.getValue());

        return ret;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.DoubleValue)
     */
    public Value suma(DoubleValue value) throws IncompatibleTypesException {
        DoubleValue ret = new DoubleValue();
        ret.setValue(this.value + value.getValue());

        return ret;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.StringValue)
     */
    public Value suma(StringValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(this.value + Double.parseDouble(value.getValue()));

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(value.getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#producto(com.hardcode.gdbms.engine.instruction.expression.Value)
     */
    public Value producto(Value v) throws IncompatibleTypesException {
        return v.producto((IntValue) this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.IntValue)
     */
    public Value producto(IntValue value) throws IncompatibleTypesException {
        IntValue ret = new IntValue();
        ret.setValue(this.value * value.getValue());

        return ret;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.LongValue)
     */
    public Value producto(LongValue value) throws IncompatibleTypesException {
        LongValue ret = new LongValue();
        ret.setValue(this.value * value.getValue());

        return ret;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.FloatValue)
     */
    public Value producto(FloatValue value) throws IncompatibleTypesException {
        FloatValue ret = new FloatValue();
        ret.setValue(this.value * value.getValue());

        return ret;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.DoubleValue)
     */
    public Value producto(DoubleValue value) throws IncompatibleTypesException {
        DoubleValue ret = new DoubleValue();
        ret.setValue(this.value * value.getValue());

        return ret;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.StringValue)
     */
    public Value producto(StringValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(this.value * Double.parseDouble(value.getValue()));

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(value.getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value equals(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value == value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value equals(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value == value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value equals(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value == value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value equals(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value == value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value equals(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.toString().equals(value.getValue()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value equals(Value value) throws IncompatibleTypesException {
        return value.equals(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value greater(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value > value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value greater(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value > value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value greater(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value > value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value greater(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value > value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value greater(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.toString().compareTo(value.getValue()) > 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value greater(Value value) throws IncompatibleTypesException {
        return value.less(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value greaterEqual(DoubleValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value >= value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value greaterEqual(FloatValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value >= value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value greaterEqual(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value >= value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value greaterEqual(LongValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value >= value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value greaterEqual(StringValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.toString().compareTo(value.getValue()) >= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value greaterEqual(Value value) throws IncompatibleTypesException {
        return value.lessEqual(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value less(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value < value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value less(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value < value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value less(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value < value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value less(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value < value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value less(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.toString().compareTo(value.getValue()) < 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value less(Value value) throws IncompatibleTypesException {
        return value.greater(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value lessEqual(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value <= value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value lessEqual(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value <= value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value lessEqual(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value <= value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value lessEqual(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value <= value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value lessEqual(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.toString().compareTo(value.getValue()) <= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value lessEqual(Value value) throws IncompatibleTypesException {
        return value.greaterEqual(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value notEquals(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value != value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value notEquals(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value != value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value notEquals(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value != value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value notEquals(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value != value.getValue());
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value notEquals(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(!this.toString().equals(value.getValue()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value notEquals(Value value) throws IncompatibleTypesException {
        return value.notEquals(this);
    }

	/**
	 * @see com.geopista.ui.plugin.io.dgn.impl.values.NumericValue#intValue()
	 */
	public int intValue() {
		return (int) value;
	}

	/**
	 * @see com.geopista.ui.plugin.io.dgn.impl.values.NumericValue#longValue()
	 */
	public long longValue() {
		return (long) value;
	}

	/**
	 * @see com.geopista.ui.plugin.io.dgn.impl.values.NumericValue#floatValue()
	 */
	public float floatValue() {
		return (float) value;
	}

	/**
	 * @see com.geopista.ui.plugin.io.dgn.impl.values.NumericValue#doubleValue()
	 */
	public double doubleValue() {
		return (double) value;
	}
}
