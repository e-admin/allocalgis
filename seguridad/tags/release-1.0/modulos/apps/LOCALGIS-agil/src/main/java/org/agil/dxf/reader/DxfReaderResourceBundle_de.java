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
 *  German resource bundle for DXF reader.
 *  
 *  @version 1.00beta0
 */
public class DxfReaderResourceBundle_de extends ListResourceBundle {
  /**
   *  Get the contents of this bundle.
   *  @return  the contents
   */
   public Object[][] getContents() {
    return contents;
  }

  static final Object[][] contents = {
    // LOCALIZE THIS
    // status messages during read
    { "msgReading",              "Lese %0...   "         },
    { "msgPacked",	         "DXF-Datei ist gepackt."},
    { "msgBinary",		 "Dies ist binäres DXF!" },
    { "msgEOF",                  "Dateiende erreicht."   },

    // warnings
    { "warnNoHeader",            "Keine HEADER-Sektion!"    },
    { "warnNoAcadver",           "Keine DXF-Versionsnummer definiert!" },
    { "warnUnknownLayer",        "Unbekannter LAYER %0 referenziert!" },
    { "warnUnknownSection",      "Überspringe unbekannte SEKTION %0!" },

    // error messages
    { "err!Internal",            "Interner Fehler"       },
    { "err!FileNotFound",        "Datei nicht gefunden: \"%0\"" }, 
    { "err!Read",                "%0\nin Zeile %1"       },
    { "err!GrpInTables",         "Unerwartete Gruppennummer für TABLES-Sektion in Zeile %0" },
    { "err!VertexAfterPolyline", "POLYLINE nicht gefolgt von VERTEX/SEQEND" },
    { "err!InHeader",            "Unerwarteter HEADER-Eintrag in Zeile %0" },
    { "err!DxfFile",             "Dies scheint keine DXF-Datei zu sein..." },
    { "err!Group",               "Keine erlaubte Gruppennummer in Zeile %0" },
    { "err!Syntax",              "Falsche Syntax in Zeile %0" },
    { "err!Format",              "Falsches Format in Zeile %0"  },
    { "err!FpFormat",            "Falsches Fließkommaformat in Zeile %0" },
    { "err!Nested",              "Verschachtelte BLOCKS sind nicht erlaubt." },
    { "err!IO",		         "Interner Eingabe-/Ausgabefehler beim Öffnen von \"%0\":\n%1" },
    { "err!EOF",		 "Unerwartetes Dateiende in Zeile %0" },
  };
}    
