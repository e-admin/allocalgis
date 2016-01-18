/**
 * ImportErrorHandlerServerCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.intercambio.importacion.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class ImportErrorHandlerServerCatastro implements ErrorHandler 
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
