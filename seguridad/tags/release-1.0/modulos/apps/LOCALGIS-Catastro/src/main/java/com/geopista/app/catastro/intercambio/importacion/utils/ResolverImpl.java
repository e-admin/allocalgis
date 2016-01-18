package com.geopista.app.catastro.intercambio.importacion.utils;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.geopista.app.catastro.intercambio.importacion.utils.schemasxsd.FileLoader;

public class ResolverImpl implements EntityResolver {
    public InputSource resolveEntity(String publicID, String systemID)
        throws SAXException {
        if (systemID.endsWith("finretorno.xsd")) {
            // Return local copy of the copyright.xml file
        	return new InputSource(FileLoader.getFile("finretorno.xsd"));
        }
        else if (systemID.endsWith("ldc.xsd")){
        	return new InputSource(FileLoader.getFile("ldc.xsd"));
        }
        else if (systemID.endsWith("finentrada.xsd")){
        	return new InputSource(FileLoader.getFile("finentrada.xsd"));
        }
        else if (systemID.endsWith("finsalida.xsd")){
        	return new InputSource(FileLoader.getFile("finsalida.xsd"));
        }
        else if (systemID.endsWith("padron.xsd")){
        	return new InputSource(FileLoader.getFile("padron.xsd"));
        }
        // If no match, returning null makes process continue normally
        return null;
    }
}
