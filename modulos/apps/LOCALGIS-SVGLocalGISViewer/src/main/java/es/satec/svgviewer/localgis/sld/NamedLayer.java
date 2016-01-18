/**
 * NamedLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

/**
 * A NamedLayer uses the "name" attribute to identify a layer known to the WMS and can contain zero
 * or more styles, either NamedStyles or UserStyles. In the absence of any styles the default style
 * for the layer is used.
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */
public class NamedLayer extends AbstractLayer {
    /**
     * constructor initializing the class with the <NamedLayer>
     */
    public NamedLayer( String name, AbstractStyle[] styles ) {
        super( name, styles );
    }

}