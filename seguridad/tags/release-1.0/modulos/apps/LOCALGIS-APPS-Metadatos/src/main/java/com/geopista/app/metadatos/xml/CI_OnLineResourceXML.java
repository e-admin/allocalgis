package com.geopista.app.metadatos.xml;

import com.geopista.protocol.metadatos.CI_OnLineResource;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.app.metadatos.estructuras.Estructuras;
import org.w3c.dom.Element;



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
 * Date: 21-jul-2004
 * Time: 15:06:02
 */
public class CI_OnLineResourceXML implements IXMLElemento{
        CI_OnLineResource onLineResource;
        private String language=null;

        public CI_OnLineResourceXML(CI_OnLineResource este)
        {
            onLineResource=este;
        }
        public CI_OnLineResourceXML(CI_OnLineResource este, String language)
        {
            onLineResource=este;
            this.language=language;
        }

        public CI_OnLineResourceXML()
        {
            this(null);
        }


        public String getTag()
        {
            return CI_ONLINE;
        }

        public CI_OnLineResource getOnLineResource()
        {
            return onLineResource;
        }

        public static Object load(Element nodoPadre)
        {
            CI_OnLineResource result = new CI_OnLineResource();
            Element nodoonline = XMLTranslator_LCGIII.recuperarHijo((Element)nodoPadre, CI_ONLINE);
            if(nodoonline == null)
                return null;
            result.setLinkage(XMLTranslator_LCGIII.recuperarHoja(nodoonline, LINKAGE));
            DomainNode auxDomain = Estructuras.getListaFunctionCode().getDomainNode(XMLTranslator_LCGIII.recuperarHoja(nodoonline, FUNCODE));
            if (auxDomain!=null)
                  result.setIdOnLineFunctionCode(auxDomain.getIdNode());
            return result;


        }
        public void save(XMLTranslator traductor) throws Exception {
            if (onLineResource==null) return;
            traductor.insertar(LINKAGE, onLineResource.getLinkage());
            if (onLineResource.getIdOnLineFunctionCode()!=null)
            {
                DomainNode auxDomain = Estructuras.getListaFunctionCode().getDomainNodeById(onLineResource.getIdOnLineFunctionCode());
                traductor.insertar(FUNCODE,(language==null?auxDomain.getPatron():auxDomain.getTerm(language)));
            }
        }


         public static final String LINKAGE = "linkage";
         public static final String CI_ONLINE = "CI_OnlineResource";
         public static final String FUNCODE = "functionCode";
}
