/**
 * DxfReaderResourceBundle.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */


package org.agil.dxf.reader;

import java.util.ListResourceBundle;

/** 
 *  Default (english) resource bundle for DXF reader.
 *  
 *  @version 1.00beta0 (January 1999)
 */
public class DxfReaderResourceBundle extends ListResourceBundle {
  /**
   *  Get the contents of this bundle.
   *  @return  the contents
   */
   public Object[][] getContents() {
    return contents;
  }


  private static final Object[][] contents = {
    // LOCALIZE THIS
    // status messages during read
    { "msgReading",              "Reading %0..."         }, // %0 SECTION name
    { "msgPacked",	         "DXF file is packed."   },
    { "msgBinary",		 "This is binary DXF!"   },
    { "msgEOF",                  "Reached EOF."          },

    // warnings
    { "warnNoHeader",            "No HEADER section!"    },
    { "warnNoAcadver",           "No DXF file version number defined!" },
    { "warnUnknownLayer",        "Unknown LAYER %0 referenced!" },
    { "warnUnknownSection",      "Skipping unknown SECTION %0!" },

    // error messages
    { "err!Internal",            "Internal error"        },
    { "err!FileNotFound",        "File not found: \"%0\"" }, // %0 file name
    { "err!Read",                "%0\nin line %1"        },  
    { "err!GrpInTables",         "Unexpected group in TABLES section in line %0" },
    { "err!VertexAfterPolyline", "Non VERTEX/SEQEND following POLYLINE" },
    { "err!InHeader",            "Unexpected in HEADER in line %0" },
    { "err!DxfFile",             "This does not look like a DXF file..." },
    { "err!Group",               "Not a valid group number in line %0" },
    { "err!Syntax",              "Incorrect syntax in line %0" },
    { "err!Format",              "Wrong format in line %0"  },
    { "err!FpFormat",            "Wrong floating point format in line %0" },
    { "err!Nested",              "Nested BLOCKS not allowed." },
    { "err!IO",			 "Internal I/O error when opening \"%0\":\n%1"   },
    { "err!EOF",		 "Unexpected end of file in line %0" },
  };
    
}
