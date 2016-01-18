/**
 * Scale.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

public class Scale {

    private int numerator;

    private int denominator;

    public Scale(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Devuelve el campo numerator
     * 
     * @return El campo numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * Establece el valor del campo numerator
     * 
     * @param numerator
     *            El campo numerator a establecer
     */
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    /**
     * Devuelve el campo denominator
     * @return El campo denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Establece el valor del campo denominator
     * @param denominator El campo denominator a establecer
     */
    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }


}
