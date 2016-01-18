package com.geopista.ui.plugin.io.dgn.impl.values;

import java.util.Date;


/**
 * Clase padre de todos los wrappers sobre tipos del sistema
 *
 * @author Fernando González Cortés
 */
public abstract class Value implements Operations {
	 /** Campo de tipo alfanumerico */
    public static final int STRING = 0;

    /** Campo de tipo numérico entero */
    public static final int INTEGER = 1;

    /** Campo de tipo numérico decimal */
    public static final int DECIMAL = 2;

    /** Campo de tipo fecha */
    public static final int DATE = 3;

    /** Campo de tipo booleano */
    public static final int BOOLEAN = 4;
    
    /** Campo de tipo double */
    public static final int DOUBLE = 5;
    
    /** Campo de tipo float */
    public static final int FLOAT = 6;
    
    /** Campo de tipo long */
    public static final int LONG = 7;
    
    /** Campo de tipo Null */
    public static final int NULL = 8;
    
    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.values.Booleanvalue);
     */
    public Value and(BooleanValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.values.Doublevalue);
     */
    public Value and(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.values.Floatvalue);
     */
    public Value and(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.values.Intvalue);
     */
    public Value and(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.values.Longvalue);
     */
    public Value and(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.values.Stringvalue);
     */
    public Value and(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.values.value);
     */
    public Value and(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#or(com.hardcode.gdbms.engine.values.Booleanvalue);
     */
    public Value or(BooleanValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#or(com.hardcode.gdbms.engine.values.Doublevalue);
     */
    public Value or(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#or(com.hardcode.gdbms.engine.values.Floatvalue);
     */
    public Value or(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#or(com.hardcode.gdbms.engine.values.Intvalue);
     */
    public Value or(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#or(com.hardcode.gdbms.engine.values.Longvalue);
     */
    public Value or(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#or(com.hardcode.gdbms.engine.values.Stringvalue);
     */
    public Value or(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#or(com.hardcode.gdbms.engine.values.value);
     */
    public Value or(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#producto(com.hardcode.gdbms.engine.values.Booleanvalue);
     */
    public Value producto(BooleanValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#producto(com.hardcode.gdbms.engine.values.Doublevalue);
     */
    public Value producto(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#producto(com.hardcode.gdbms.engine.values.Floatvalue);
     */
    public Value producto(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#producto(com.hardcode.gdbms.engine.values.Intvalue);
     */
    public Value producto(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#producto(com.hardcode.gdbms.engine.values.Longvalue);
     */
    public Value producto(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#producto(com.hardcode.gdbms.engine.values.Stringvalue);
     */
    public Value producto(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#producto(com.hardcode.gdbms.engine.values.value);
     */
    public Value producto(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#suma(com.hardcode.gdbms.engine.values.Booleanvalue);
     */
    public Value suma(BooleanValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#suma(com.hardcode.gdbms.engine.values.Doublevalue);
     */
    public Value suma(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#suma(com.hardcode.gdbms.engine.values.Floatvalue);
     */
    public Value suma(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#suma(com.hardcode.gdbms.engine.values.Intvalue);
     */
    public Value suma(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#suma(com.hardcode.gdbms.engine.values.Longvalue);
     */
    public Value suma(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#suma(com.hardcode.gdbms.engine.values.Stringvalue);
     */
    public Value suma(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#suma(com.hardcode.gdbms.engine.values.value);
     */
    public Value suma(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value equals(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value equals(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value equals(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value equals(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value equals(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value equals(BooleanValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value equals(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value notEquals(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value notEquals(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value notEquals(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value notEquals(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value notEquals(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value notEquals(BooleanValue value)
        throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value notEquals(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value greater(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value greater(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value greater(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value greater(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value greater(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value greater(BooleanValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value greater(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value less(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value less(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value less(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value less(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value less(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value less(BooleanValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value less(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value greaterEqual(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value greaterEqual(LongValue value)
        throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value greaterEqual(FloatValue value)
        throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value greaterEqual(DoubleValue value)
        throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value greaterEqual(StringValue value)
        throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value greaterEqual(BooleanValue value)
        throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value greaterEqual(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value lessEqual(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value lessEqual(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value lessEqual(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value lessEqual(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value lessEqual(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value lessEqual(BooleanValue value)
        throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value lessEqual(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value like(BooleanValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value like(DoubleValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value like(FloatValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value like(IntValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value like(LongValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value like(StringValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value like(Value value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#suma(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value suma(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#producto(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value producto(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#and(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value and(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#or(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value or(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value equals(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value notEquals(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value greater(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value less(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value greaterEqual(DateValue value)
        throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value lessEqual(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.DateValue)
     */
    public Value like(DateValue value) throws IncompatibleTypesException {
        throw new IncompatibleTypesException("Cannot operate with " + value +
            " and " + this);
    }
    
}
