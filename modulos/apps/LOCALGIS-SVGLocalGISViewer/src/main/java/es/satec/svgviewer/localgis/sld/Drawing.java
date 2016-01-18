/**
 * Drawing.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

import java.util.Hashtable;

/**
 * This is the top level interface of <tt>Fill</tt> and <tt>Stroke</tt> defining the methods
 * <tt>getGraphicFill()</tt> and <tt>getCssParameters()</tt> that are common to both.
 * <p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public class Drawing {

    protected Hashtable cssParams = null;

    /**
     * Constructs a new instance of <tt>Drawing</tt>.
     * 
     * @param cssParams
     */
    Drawing(Hashtable cssParams) {
        this.cssParams = cssParams;
    }

    /**
     * A simple SVG/CSS2 styling parameters are given with the CssParameter element. <br>
     * This method is for technical use. The user should access the specialized methods of the
     * derived classes.
     * 
     * @return the CssParameters
     */
    public Hashtable getCssParameters() {
        return cssParams;
    }

    /**
     * A simple SVG/CSS2 styling parameters are given with the CssParameter element. <br>
     * This method sets CssParameters.
     * 
     * @param cssParameters
     *            the CssParameters
     */
    void setCssParameters(Hashtable cssParameters) {
        this.cssParams = cssParameters;
    }

    /**
     * Simple SVG/CSS2 styling parameters are given with the CssParameter element. This method adds
     * a CssParameter to a given set of CssParameters.
     * <p>
     * 
     * @param key
     *            the key of the object to insert
     * @param value
     *            the value of the object to insert
     */
    void addCssParameter( String key, Object value ) {
        cssParams.put( key, value );
    }

    /**
     * Simple SVG/CSS2 styling parameters are given with the CssParameter element.
     * <p>
     * This method adds a CssParameter to a given set of CssParameters.
     * 
     * @param key
     *            the key of the object to remove
     */
    void removeCssParameter( Object key ) {
        cssParams.remove( key );
    }

}