/**
 * CI_CitationXML.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.xml;


import java.util.Enumeration;
import java.util.Vector;

import org.w3c.dom.Element;

import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.CI_Citation;
import com.geopista.protocol.metadatos.CI_Date;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-sep-2004
 * Time: 11:13:48
 */
public class CI_CitationXML implements IXMLElemento{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CI_CitationXML.class);
    CI_Citation citacion;
    private String language=null;

    public CI_CitationXML() {
    }

    public CI_CitationXML(CI_Citation citacion) {
        this.citacion = citacion;
    }
    public CI_CitationXML(CI_Citation citacion, String language) {
        this.citacion = citacion;
        this.language=language;
    }

    public String getTag() {
        return CI_CITATION;
    }

    public static Object load(Element citeinfoDOM) throws Exception
        {
             Element esteNodo = XMLTranslator_LCGIII.recuperarHijo(citeinfoDOM, CI_CITATION);
            if(esteNodo == null)
                return null;
            CI_Citation citation = new CI_Citation();
            citation.setTitle(XMLTranslator_LCGIII.recuperarHoja(esteNodo, TITLE));
            Element nodo = XMLTranslator_LCGIII.recuperarHijo(esteNodo, DATE);
            if(nodo != null)
            {
                nodo = XMLTranslator_LCGIII.recuperarHijo(nodo, CI_DATE);
                if(nodo != null)//Aquui hay que meter un  for
                {
                    Vector auxVector= new Vector();
                    DomainNode auxDomain= Estructuras.getListaDateType().getDomainNode(XMLTranslator_LCGIII.recuperarHoja(nodo, DATETYPE));
                    CI_Date auxDate= new CI_Date(XMLTranslator.recuperarHoja_Date(nodo, DATE),auxDomain);
                    auxVector.add(auxDate);
                    citation.setCI_Dates(auxVector);
                }


            }
           logger.debug("Se ha recuperado correctamente la citacion");
           return citation;
         }

        public void save(XMLTranslator traductor) throws Exception
        {
                if( citacion == null) return;
                traductor.insertar(TITLE, citacion.getTitle(), XMLTranslator.UNKNOWN);
                if(citacion.getCI_Dates() != null)
                {
                    traductor.insertar_tag_begin(DATE);
                    for (Enumeration e=citacion.getCI_Dates().elements();e.hasMoreElements();)
                    {
                        CI_Date auxDate=(CI_Date)e.nextElement();
                        traductor.insertar_tag_begin(CI_DATE);
                        traductor.insertar(DATE, auxDate.getDate());
                        if (language==null)
                            traductor.insertar(DATETYPE, auxDate.getTipo()!=null?auxDate.getTipo().getPatron():XMLTranslator.UNKNOWN, XMLTranslator.UNKNOWN);
                        else
                            traductor.insertar(DATETYPE, auxDate.getTipo()!=null?auxDate.getTipo().getTerm(language):XMLTranslator.UNKNOWN, XMLTranslator.UNKNOWN);
                        traductor.insertar_tag_end(CI_DATE);
                    }
                    traductor.insertar_tag_end(DATE);
                }
        }
       public static final String CI_CITATION = "CI_Citation";
       public static final String CI_DATE = "CI_Date";
       public static final String DATE = "date";
       public static final String DATETYPE = "dateType";
       public static final String TITLE = "title";

}
