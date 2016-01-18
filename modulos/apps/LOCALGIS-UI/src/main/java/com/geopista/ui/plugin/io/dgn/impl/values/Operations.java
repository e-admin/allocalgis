/**
 * Operations.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.dgn.impl.values;



/**
 * Interfaz que indica las operaciones soportadas por el SGBD
 *
 * @author Fernando González Cortés
 */
public interface Operations {
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value suma(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value suma(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value suma(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value suma(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value suma(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value suma(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value suma(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value suma(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value producto(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value producto(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value producto(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value producto(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value producto(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value producto(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value producto(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value producto(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value and(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value and(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value and(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value and(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value and(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value and(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value and(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value and(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value or(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value or(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value or(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value or(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value or(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value or(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value or(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value or(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value equals(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value equals(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value equals(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value equals(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value equals(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value equals(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value equals(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value equals(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value notEquals(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value notEquals(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value notEquals(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value notEquals(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value notEquals(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value notEquals(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value notEquals(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value notEquals(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greater(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greater(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greater(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greater(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greater(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greater(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greater(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greater(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value less(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value less(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value less(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value less(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value less(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value less(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value less(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value less(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greaterEqual(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greaterEqual(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greaterEqual(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greaterEqual(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greaterEqual(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greaterEqual(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greaterEqual(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value greaterEqual(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value lessEqual(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value lessEqual(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value lessEqual(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value lessEqual(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value lessEqual(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value lessEqual(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value lessEqual(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value lessEqual(Value value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value like(IntValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value like(LongValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value like(FloatValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value like(DoubleValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value like(StringValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value like(BooleanValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value like(DateValue value) throws IncompatibleTypesException;

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IncompatibleTypesException DOCUMENT ME!
     */
    Value like(Value value) throws IncompatibleTypesException;
}
