/**
 * ErrorHandlerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
