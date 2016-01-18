/**
 * MD_LegalConstraintXML.java
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
import org.w3c.dom.NodeList;

import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.MD_LegalConstraint;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 07-sep-2004
 * Time: 14:46:51
 */
public class MD_LegalConstraintXML implements IXMLElemento{
        private static org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(MD_LegalConstraintXML.class);
        MD_LegalConstraint constraint;
        private String language=null;
        public MD_LegalConstraintXML(MD_LegalConstraint constraint)
        {
            this.constraint=constraint;
        }
        public MD_LegalConstraintXML(MD_LegalConstraint constraint, String language)
        {
            this.constraint=constraint;
            this.language=language;
        }

        public MD_LegalConstraintXML()
        {
            this(null);
        }


        public String getTag()
        {
            return MD_LEGALCONS;
        }

        public MD_LegalConstraint getConstraint()
        {
            return constraint;
        }

        public static Object load(Element nodoPadre)
        {
            MD_LegalConstraint result = new MD_LegalConstraint();
            Element nodoconstraint = XMLTranslator_LCGIII.recuperarHijo((Element)nodoPadre, MD_LEGALCONS);
            if(nodoconstraint == null)  return null;
            result.setUselimitation(XMLTranslator_LCGIII.recuperarHoja(nodoconstraint,USELIMITATION ));
            result.setOtherconstraint(XMLTranslator_LCGIII.recuperarHoja(nodoconstraint,METOC ));
            NodeList listaElemUse = ((org.w3c.dom.Element)nodoconstraint).getElementsByTagName(METUC);
            if(listaElemUse != null)
            {
               Vector vUsos=new Vector();
               for(int i = 0; i < listaElemUse.getLength(); i++)
               {
                   org.w3c.dom.Element res = (org.w3c.dom.Element)listaElemUse.item(i);
                   DomainNode auxDomain = Estructuras.getListaRestriction().getDomainNode((String)res.getFirstChild().getNodeValue());
                   if (auxDomain!=null) vUsos.add(auxDomain.getIdNode());
                   else//preguntamos por la traducción
                   {
                      auxDomain = Estructuras.getListaRestriction().getDomainNodeByTraduccion((String)res.getFirstChild().getNodeValue());
                      if (auxDomain!=null) vUsos.add(auxDomain.getIdNode());
                      else
                          logger.warn("USES: No se ha encontrado el identificador ni la traducción de :"+(String)res.getFirstChild().getNodeValue());
                   }

               }
               result.setUse(vUsos);
           }
           else
                logger.warn("El metadato no tiene "+METUC);
           NodeList listaElemAcc = ((org.w3c.dom.Element)nodoconstraint).getElementsByTagName(METAC);
           if(listaElemAcc != null)
           {
                Vector vAcc=new Vector();
                for(int i = 0; i < listaElemAcc.getLength(); i++)
                {
                    org.w3c.dom.Element res = (org.w3c.dom.Element)listaElemAcc.item(i);
                    DomainNode auxDomain = Estructuras.getListaRestriction().getDomainNode((String)res.getFirstChild().getNodeValue());
                    if (auxDomain!=null) vAcc.add(auxDomain.getIdNode());
                    else//preguntamos por la traducción
                    {
                       auxDomain = Estructuras.getListaRestriction().getDomainNodeByTraduccion((String)res.getFirstChild().getNodeValue());
                       if (auxDomain!=null) vAcc.add(auxDomain.getIdNode());
                       else
                          logger.warn("ACCESS: No se ha encontrado el identificador ni la traducción de :"+(String)res.getFirstChild().getNodeValue());
                    }

                }
                result.setAccess(vAcc);
           }
           else
            logger.warn("El metadato no tiene "+METAC);
         return result;
        }

        public void save(XMLTranslator traductor) throws Exception
      {
          if(constraint!= null)
          {
              traductor.insertar(USELIMITATION, constraint.getUselimitation());
              traductor.insertar(METOC,constraint.getOtherconstraint());
              if (constraint.getUse()!=null)
              {
                   for (Enumeration e=constraint.getUse().elements(); e.hasMoreElements();)
                   {
                       DomainNode auxDomain =Estructuras.getListaRestriction().getDomainNodeById((String)e.nextElement());
                       if (auxDomain!=null)
                            traductor.insertar(METUC,(language==null?auxDomain.getPatron():auxDomain.getTerm(language)));
                   }
              }
              if (constraint.getAccess()!=null)
              {
                   for (Enumeration e=constraint.getAccess().elements(); e.hasMoreElements();)
                   {
                       DomainNode auxDomain =Estructuras.getListaRestriction().getDomainNodeById((String)e.nextElement());
                       if (auxDomain!=null)
                            traductor.insertar(METAC,(language==null?auxDomain.getPatron():auxDomain.getTerm(language)));
                   }
              }
          }

      }

        public static final String MD_LEGALCONS = "MD_LegalConstraints";
        public static final String USELIMITATION = "useLimitation";
        public static final String METOC = "otherConstraints";
        public static final String METUC = "useConstraints";
        public static final String METAC = "accessConstraints";


}

