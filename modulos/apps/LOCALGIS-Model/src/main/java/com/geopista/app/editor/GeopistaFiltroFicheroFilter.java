/**
 * GeopistaFiltroFicheroFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.editor;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;
 
public class GeopistaFiltroFicheroFilter extends FileFilter {

    private static String TYPE_UNKNOWN = "Type Unknown";
    private static String HIDDEN_FILE = "Hidden File";

    private Hashtable filters = null;
    private String description = null;
    private String fullDescription = null;
    private String NombreExtension = null;
    private int tamano_extension;

    private boolean useExtensionsInDescription = true;

    public GeopistaFiltroFicheroFilter() {
	this.filters = new Hashtable();
    }

    public GeopistaFiltroFicheroFilter(String extension) {
	this(extension,null);
    }

    public GeopistaFiltroFicheroFilter(String extension, String description) {
	this();
	if(extension!=null) addExtension(extension);
 	if(description!=null) setDescription(description);
    }

    public GeopistaFiltroFicheroFilter(String[] filters) {
	this(filters, null);
    }

    public GeopistaFiltroFicheroFilter(String[] filters, String description) {
	this();
	for (int i = 0; i < filters.length; i++) {
	    addExtension(filters[i]);
	}
 	if(description!=null) setDescription(description);
    }

    public boolean accept(File f) {
	if(f != null) {
	    if(f.isDirectory()) {
		return true;
	    }
	    String extension = getExtension(f);
	    if(extension != null && filters.get(getExtension(f)) != null) {
		return true;
	    };
	}
	return false;
    }

    public String getExtension(File f) 
    {
      if(f != null) 
      {
        if(NombreExtension == null) 
        {
          NombreExtension = "";
          Enumeration extensions = filters.keys();
      		if(extensions != null) 
          {
            NombreExtension = (String) extensions.nextElement();
          
            while (extensions.hasMoreElements()) 
            {
              NombreExtension = (String) extensions.nextElement();
            }
          } 
        }    //if NombreExtension
    
        String filename = f.getName();
        
        if((NombreExtension.toLowerCase() == "elemtex.shp")||(NombreExtension.toLowerCase() == "elemlin.shp") || (NombreExtension.toLowerCase() == "ejes.shp")|| (NombreExtension.toLowerCase() == "carvia.dbf"))
        {
           return filename.toLowerCase();
        }
        else
        {
           int i = filename.lastIndexOf('.');
           if(i>0 && i<filename.length()-1) 
           {
              return filename.substring(i+1).toLowerCase();
           };
        }//else
  	   
    	} 
    	return null;
    }

    public void addExtension(String extension) {
	if(filters == null) {
	    filters = new Hashtable(5);
	}
	filters.put(extension.toLowerCase(), this);
	fullDescription = null;
    }

     public String getDescription() {
	if(fullDescription == null) {
	    if(description == null || isExtensionListInDescription()) {
 		fullDescription = description==null ? "(" : description + " (";
		Enumeration extensions = filters.keys();
		if(extensions != null) {
		    fullDescription += "." + (String) extensions.nextElement();
		    while (extensions.hasMoreElements()) {
			fullDescription += ", " + (String) extensions.nextElement();
		    }
		}
		fullDescription += ")";
	    } else {
		fullDescription = description;
	    }
	}
	return fullDescription;
    }
    public void setDescription(String description) {
	this.description = description;
	fullDescription = null;
    }

    public void setExtensionListInDescription(boolean b) {
  	useExtensionsInDescription = b;
    fullDescription = null;
    }

    public boolean isExtensionListInDescription() {
  	return useExtensionsInDescription;
    }
}
