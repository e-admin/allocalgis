package com.geopista.app.metadatos.xml;


import org.w3c.dom.Element;

import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.app.metadatos.estructuras.Estructuras;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 21-dic-2004
 * Time: 16:26:35
 */
public class MD_ReferenceSystemXML implements IXMLElemento{
       private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MD_ReferenceSystemXML.class);
       String referenceSystem;
       private String language;

        public MD_ReferenceSystemXML(){
        }

        public MD_ReferenceSystemXML(String referenceSystem) {
            this.referenceSystem = referenceSystem;
        }

        public MD_ReferenceSystemXML(String referenceSystem, String language) {
            this.referenceSystem = referenceSystem;
            this.language = language;
        }

        public String getTag()
        {
            return MD_REFERENCESYSTEM;
        }

        public static Object load(Element nodoPadre)
                throws Exception
        {
            Element nodoMD = XMLTranslator_LCGIII.recuperarHijo((Element)nodoPadre, MD_REFERENCESYSTEM);
            if(nodoMD == null) return null;
            Element nodoIdentifier = XMLTranslator_LCGIII.recuperarHijo(nodoMD, SYSTEMIDENTIFIER);
            if(nodoIdentifier == null) return null;
            DomainNode auxDomain = Estructuras.getListaReferenceSystem().getDomainNode(XMLTranslator_LCGIII.recuperarHoja(nodoIdentifier, CODE));
            return (auxDomain==null?null:auxDomain.getIdNode());
         }

         public void save(XMLTranslator traductor) throws Exception {
             if (referenceSystem==null||traductor==null) return;
             traductor.insertar_tag_begin(SYSTEMIDENTIFIER);
             DomainNode auxDomain = Estructuras.getListaReferenceSystem().getDomainNodeById(referenceSystem);
             traductor.insertar(CODE,(language==null?auxDomain.getPatron():auxDomain.getTerm(language)));
             traductor.insertar_tag_end(SYSTEMIDENTIFIER);
         }

            public static final String MD_REFERENCESYSTEM = "MD_ReferenceSystem";
            public static final String SYSTEMIDENTIFIER = "referenceSystemIdentifier";
            public static final String CODE = "code";

  }

