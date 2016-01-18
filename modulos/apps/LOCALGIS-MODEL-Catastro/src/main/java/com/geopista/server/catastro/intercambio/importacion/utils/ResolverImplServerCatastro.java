/**
 * ResolverImplServerCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.intercambio.importacion.utils;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.geopista.server.catastro.intercambio.importacion.utils.schemasxsd.FileLoaderServerCatastro;

public class ResolverImplServerCatastro implements EntityResolver {
    public InputSource resolveEntity(String publicID, String systemID)
        throws SAXException {
        if (systemID.endsWith("finretorno.xsd")) {
            // Return local copy of the copyright.xml file
        	return new InputSource(FileLoaderServerCatastro.getFile("finretorno.xsd"));
        }
        else if (systemID.endsWith("ldc.xsd")){
        	return new InputSource(FileLoaderServerCatastro.getFile("ldc.xsd"));
        }
        else if (systemID.endsWith("finentrada.xsd")){
        	return new InputSource(FileLoaderServerCatastro.getFile("finentrada.xsd"));
        }
        else if (systemID.endsWith("finsalida.xsd")){
        	return new InputSource(FileLoaderServerCatastro.getFile("finsalida.xsd"));
        }
        else if (systemID.endsWith("padron.xsd")){
        	return new InputSource(FileLoaderServerCatastro.getFile("padron.xsd"));
        }
        // If no match, returning null makes process continue normally
        return null;
    }
}
