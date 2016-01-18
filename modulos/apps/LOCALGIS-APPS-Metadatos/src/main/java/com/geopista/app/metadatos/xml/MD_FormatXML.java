/**
 * MD_FormatXML.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.xml;

import org.w3c.dom.Element;

import com.geopista.protocol.metadatos.MD_Format;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 07-sep-2004
 * Time: 15:49:47
 */
public class MD_FormatXML implements IXMLElemento {
        MD_Format formato;
        public MD_FormatXML(MD_Format este)
        {
            formato=este;
        }

        public MD_FormatXML()
        {
            this(null);
        }


        public String getTag()
        {
            return MD_FORMAT;
        }

        public MD_Format getFormato()
        {
            return formato;
        }

        public static Object load(Element nodoPadre)
        {
            MD_Format result = new MD_Format();
            Element esteNodo = XMLTranslator_LCGIII.recuperarHijo(nodoPadre,MD_FORMAT );
            if(esteNodo == null) return result;
            result.setName(XMLTranslator_LCGIII.recuperarHoja(esteNodo, FORMNAME));
            result.setVersion(XMLTranslator_LCGIII.recuperarHoja(esteNodo, FORMCONT)==null?"":XMLTranslator_LCGIII.recuperarHoja(esteNodo, FORMCONT));
            return result;
        }

        public void save(XMLTranslator traductor) throws Exception {
            if (formato==null||traductor==null) return;
            traductor.insertar(FORMNAME,formato.getName(),XMLTranslator.UNKNOWN);
            traductor.insertar(FORMCONT,formato.getVersion(),XMLTranslator.UNKNOWN);
        }

       public static final String FORMNAME = "name";
       public static final String MD_FORMAT = "MD_Format";
       public static final String FORMCONT = "specification";

}

