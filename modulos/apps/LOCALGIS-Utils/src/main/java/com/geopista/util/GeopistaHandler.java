/**
 * GeopistaHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;


import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
 
public class GeopistaHandler extends URLStreamHandler { 
   //String cryptype; 
 
    /*protected void parseURL(URL u, String spec, int start, int end) { 
        int slash = spec.indexOf('/'); 
        cryptype = spec.substring(start, slash); 
        start=slash; 
        super.parseURL(u, spec, start, end); 
    } */
 
    protected URLConnection openConnection(URL url) 
       throws IOException {
 
        return null; 
    } 
} 
