/**
 * StringValue.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.dgn.impl.values;



/**
 * Wrapper sobre el tipo de datos String
 *
 * @author Fernando González Cortés
 */
public class StringValue extends Value {
    private String value;

    /**
     * Construye un objeto StringValue con el texto que se pasa como parametro
     *
     * @param text
     */
    public StringValue(String text) {
        this.value = text;
    }

    /**
     * Creates a new StringValue object.
     */
    public StringValue() {
    }

    /**
     * Establece el valor de este objeto
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Obtiene el valor de este objeto
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.Value)
     */
    public Value suma(Value v) throws IncompatibleTypesException {
        return v.suma((StringValue) this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.IntValue)
     */
    public Value suma(IntValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) + value.getValue());

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.LongValue)
     */
    public Value suma(LongValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) + value.getValue());

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.FloatValue)
     */
    public Value suma(FloatValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) + value.getValue());

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.DoubleValue)
     */
    public Value suma(DoubleValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) + value.getValue());

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#suma(com.hardcode.gdbms.engine.instruction.expression.StringValue)
     */
    public Value suma(StringValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) +
                Double.parseDouble(value.getValue()));

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#producto(com.hardcode.gdbms.engine.instruction.expression.Value)
     */
    public Value producto(Value v) throws IncompatibleTypesException {
        return v.producto((StringValue) this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#producto(com.hardcode.gdbms.engine.instruction.expression.IntValue)
     */
    public Value producto(IntValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) * value.getValue());

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#producto(com.hardcode.gdbms.engine.instruction.expression.LongValue)
     */
    public Value producto(LongValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) * value.getValue());

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#producto(com.hardcode.gdbms.engine.instruction.expression.FloatValue)
     */
    public Value producto(FloatValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) * value.getValue());

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#producto(com.hardcode.gdbms.engine.instruction.expression.DoubleValue)
     */
    public Value producto(DoubleValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) * value.getValue());

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.expression.Operations#producto(com.hardcode.gdbms.engine.instruction.expression.StringValue)
     */
    public Value producto(StringValue value) throws IncompatibleTypesException {
        try {
            DoubleValue ret = new DoubleValue();
            ret.setValue(Double.parseDouble(this.value) * Double.parseDouble(
                    value.getValue()));

            return ret;
        } catch (NumberFormatException e) {
            throw new IncompatibleTypesException(getValue() +
                " is not a number");
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "" + value;
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value equals(BooleanValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value equals(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value equals(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value equals(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value equals(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value equals(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.equals(value.value));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#equals(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value equals(Value value) throws IncompatibleTypesException {
        return value.equals(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value greater(BooleanValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) > 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value greater(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) > 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value greater(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) > 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value greater(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) > 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value greater(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) > 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value greater(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.getValue()) > 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greater(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value greater(Value value) throws IncompatibleTypesException {
        return value.less(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value greaterEqual(BooleanValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) >= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value greaterEqual(DoubleValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) >= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value greaterEqual(FloatValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) >= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value greaterEqual(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) >= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value greaterEqual(LongValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) >= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value greaterEqual(StringValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) >= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#greaterEqual(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value greaterEqual(Value value) throws IncompatibleTypesException {
        return value.lessEqual(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value less(BooleanValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) < 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value less(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) < 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value less(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) < 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value less(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) < 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value less(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) < 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value less(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.getValue()) < 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#less(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value less(Value value) throws IncompatibleTypesException {
        return value.greater(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value lessEqual(BooleanValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) <= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value lessEqual(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) <= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value lessEqual(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) <= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value lessEqual(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) <= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value lessEqual(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) <= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value lessEqual(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(this.value.compareTo(value.toString()) <= 0);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#lessEqual(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value lessEqual(Value value) throws IncompatibleTypesException {
        return value.greaterEqual(this);
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.BooleanValue)
     */
    public Value notEquals(BooleanValue value)
        throws IncompatibleTypesException {
        return new BooleanValue(!this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.DoubleValue)
     */
    public Value notEquals(DoubleValue value) throws IncompatibleTypesException {
        return new BooleanValue(!this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.FloatValue)
     */
    public Value notEquals(FloatValue value) throws IncompatibleTypesException {
        return new BooleanValue(!this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.IntValue)
     */
    public Value notEquals(IntValue value) throws IncompatibleTypesException {
        return new BooleanValue(!this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.LongValue)
     */
    public Value notEquals(LongValue value) throws IncompatibleTypesException {
        return new BooleanValue(!this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value notEquals(StringValue value) throws IncompatibleTypesException {
        return new BooleanValue(!this.value.equals(value.toString()));
    }

    /**
     * @see com.hardcode.gdbms.engine.instruction.Operations#notEquals(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value notEquals(Value value) throws IncompatibleTypesException {
        return value.notEquals(this);
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.StringValue)
     */
    public Value like(StringValue value) throws IncompatibleTypesException {
        String pattern = value.getValue().replaceAll("%", ".*");
        pattern = pattern.replaceAll("\\?", ".");

        return new BooleanValue(this.value.matches(pattern));
    }

    /**
     * @see com.geopista.ui.plugin.io.dgn.impl.values.Operations#like(com.geopista.ui.plugin.io.dgn.impl.values.Value)
     */
    public Value like(Value value) throws IncompatibleTypesException {
        return value.like(this);
    }
}
