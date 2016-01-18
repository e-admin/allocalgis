/**
 * IncompatibleTypesException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.dgn.impl.values;

/**
 * Lanzado cuando la operación especificada no está definida para los tipos de
 * los operandos sobre los que se quiso operar
 *
 * @author Fernando González Cortés
 */
public class IncompatibleTypesException extends SemanticException {
    /**
     * Creates a new IncompatibleTypesException object.
     */
    public IncompatibleTypesException() {
        super();
    }

    /**
     * Creates a new IncompatibleTypesException object.
     *
     * @param arg0
     */
    public IncompatibleTypesException(String arg0) {
        super(arg0);
    }

    /**
     * Creates a new IncompatibleTypesException object.
     *
     * @param arg0
     */
    public IncompatibleTypesException(Throwable arg0) {
        super(arg0);
    }

    /**
     * Creates a new IncompatibleTypesException object.
     *
     * @param arg0
     * @param arg1
     */
    public IncompatibleTypesException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
