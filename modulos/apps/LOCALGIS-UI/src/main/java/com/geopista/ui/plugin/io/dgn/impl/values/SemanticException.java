/**
 * SemanticException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.dgn.impl.values;

/**
 * Clase base de las excepciones semánticas
 *
 * @author Fernando González Cortés
 */
public class SemanticException extends Exception {
    /**
     * Creates a new SemanticException object.
     */
    public SemanticException() {
        super();
    }

    /**
     * Creates a new SemanticException object.
     *
     * @param arg0
     */
    public SemanticException(String arg0) {
        super(arg0);
    }

    /**
     * Creates a new SemanticException object.
     *
     * @param arg0
     */
    public SemanticException(Throwable arg0) {
        super(arg0);
    }

    /**
     * Creates a new SemanticException object.
     *
     * @param arg0
     * @param arg1
     */
    public SemanticException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
