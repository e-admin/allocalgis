/**
 * CssParameter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

public class CssParameter {

    private String name = null;
    private String value = null;

    /**
     * constructor initializing the class with the <CssParameter>
     */
    CssParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name attribute's value of the CssParameter.
     * <p>
     * 
     * @return the value of the name attribute of the CssParameter
     */
    String getName() {
        return name;
    }

    /**
     * Sets the name attribute's value of the CssParameter.
     * <p>
     * 
     * @param name
     *            the value of the name attribute of the CssParameter
     */
    void setName( String name ) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

}
