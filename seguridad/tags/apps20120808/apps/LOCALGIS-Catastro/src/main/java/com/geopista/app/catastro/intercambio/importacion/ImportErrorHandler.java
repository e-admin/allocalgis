package com.geopista.app.catastro.intercambio.importacion;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class ImportErrorHandler implements ErrorHandler 
{    
    //  This method is called in the event of a recoverable error
    public void error(SAXParseException e) {
        log(Level.SEVERE, "Error", e);
    }
    
    //  This method is called in the event of a non-recoverable error
    public void fatalError(SAXParseException e) {
        log(Level.SEVERE, "Fatal Error", e);
    }
    
    //  This method is called in the event of a warning
    public void warning(SAXParseException e) {
        log(Level.WARNING, "Warning", e);
    }
    
    // Get logger to log errors
    private Logger logger = Logger.getLogger("com.geopista");
    
    
    // Dump a log record to a logger
    private void log(Level level, String message, SAXParseException e) {
        // Get details
        int line = e.getLineNumber();
        int col = e.getColumnNumber();
        String publicId = e.getPublicId();
        String systemId = e.getSystemId();
        
        // Append details to message
        message = message + ": " + e.getMessage() + ": line="
        + line + ", col=" + col + ", PUBLIC="
        + publicId + ", SYSTEM=" + systemId;
        
        // Log the message
        logger.log(level, message);
    }
}
