/**
 * Symbolizer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

/**
 * This is the basis of all symbolizers. It defines the method <tt>getGeometry</tt> that's common
 * to all symbolizers.
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public interface Symbolizer {

    /**
     * The ScaleDenominator-information is optional and determines whether a rule (and thus a
     * Symbolizer) is a to be applied at a certain scale.
     * 
     * @return the MinScaleDenominator
     */
    double getMinScaleDenominator();

    /**
     * Sets the MinScaleDenominator
     * 
     * @param minScaleDenominator
     *            the MinScaleDenominator
     */
    void setMinScaleDenominator( double minScaleDenominator );

    /**
     * The ScaleDenominator-information is optional and determines whether a rule (and thus a
     * Symbolizer) is a to be applied at a certain scale.
     * 
     * @return the MaxScaleDenominator
     */
    double getMaxScaleDenominator();

    /**
     * Sets the MaxScaleDenominator
     * 
     * @param maxScaleDenominator
     *            the MaxScaleDenominator
     */
    void setMaxScaleDenominator( double maxScaleDenominator );

	void dispose();

}
