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
