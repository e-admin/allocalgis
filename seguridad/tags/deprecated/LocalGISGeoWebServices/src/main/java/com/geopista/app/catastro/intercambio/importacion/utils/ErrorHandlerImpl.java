package com.geopista.app.catastro.intercambio.importacion.utils;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;



public class ErrorHandlerImpl implements ErrorHandler
{

    public void error(SAXParseException exception) throws SAXException
    {
    	exception.printStackTrace();
    	System.out.println(exception.getLineNumber());
    	throw new SAXException(exception.getMessage() + "Line error: " + exception.getLineNumber());
        
    }

    public void fatalError(SAXParseException exception) throws SAXException
    {
    	exception.printStackTrace();
    	System.out.println(exception.getLineNumber());
    	throw new SAXException(exception.getMessage() + "Line error: " + exception.getLineNumber());
        
    }

    public void warning(SAXParseException exception) throws SAXException
    {
    	exception.printStackTrace();
    	System.out.println(exception.getLineNumber() + "Line error: " + exception.getLineNumber());
        
    }

}
