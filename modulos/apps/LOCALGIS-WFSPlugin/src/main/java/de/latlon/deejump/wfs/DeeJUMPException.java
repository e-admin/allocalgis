/**
 * DeeJUMPException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * (c) 2007 by lat/lon GmbH
 *
 * @author Ugo Taddei (taddei@latlon.de)
 *
 * This program is free software under the GPL (v2.0)
 * Read the file LICENSE.txt coming with the sources for details.
 */
package de.latlon.deejump.wfs;

/**
 * ...
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: satec $
 * 
 * @version 2.0, $Revision: 1.1 $, $Date: 2012/09/12 10:52:04 $
 * 
 * @since 2.0
 */

public class DeeJUMPException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param message
     * @param cause
     */
    public DeeJUMPException( String message, Throwable cause ) {
        super( message, cause );
    }

    /**
     * @param cause
     */
    public DeeJUMPException( Throwable cause ) {
        super( cause );
    }

    /**
     * @param msg
     */
    public DeeJUMPException( String msg ) {
        super( msg );
    }

}
