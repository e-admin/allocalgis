/**
 * AbstractStyle.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: satec $
 * 
 * @version. $Revision: 1.1 $, $Date: 2011/09/19 13:48:12 $
 */
public abstract class AbstractStyle {

    protected String name = null;

    /**
     * Creates a new AbstractStyle object.
     * 
     * @param name
     */
    AbstractStyle( String name ) {
        this.name = name;
    }

    /**
     * The given Name is equivalent to the name of a WMS named style and is used to reference the
     * style externally when an SLD is used in library mode and identifies the named style to
     * redefine when an SLD is inserted into a WMS.
     * 
     * @return the name
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name attribute's value of the AbstractStyle.
     * 
     * @param name
     *            the name of the style
     *            <p>
     * 
     */
    public void setName( String name ) {
        this.name = name;
    }

}