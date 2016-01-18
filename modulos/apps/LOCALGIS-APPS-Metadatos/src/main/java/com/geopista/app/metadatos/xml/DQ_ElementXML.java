/**
 * DQ_ElementXML.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.xml;

import org.w3c.dom.Element;

import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.CI_Citation;
import com.geopista.protocol.metadatos.DQ_Element;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 07-sep-2004
 * Time: 17:19:11
 */
public class DQ_ElementXML implements IXMLElemento{
           private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DQ_ElementXML.class);
           DQ_Element elemento;
           private String language=null;

            public DQ_ElementXML() {
            }

            public DQ_ElementXML(DQ_Element elemento) {
                this.elemento = elemento;
            }
            public DQ_ElementXML(DQ_Element elemento, String language) {
                this.elemento = elemento;
                this.language = language;
            }

            public String getTag()
            {
                if (elemento==null)
                     return DQ_ELEMENT;
                else
                {
                    DomainNode auxDominio = Estructuras.getListaTipoInforme().getDomainNodeById(elemento.getSubclass_id());
                    return auxDominio.getPatron();
                }

            }

            public static DQ_Element load(Element nodoPadre)
                    throws Exception
            {
                 DQ_Element result = new DQ_Element();
                 Element esteNodo = (Element)nodoPadre.getFirstChild();
                 if(esteNodo == null)return null;
                 //Vamos a itentar añadir el tipo
                 DomainNode auxDomain = Estructuras.getListaTipoInforme().getDomainNode(esteNodo.getTagName());
                 if (auxDomain!=null)
                    result.setSubclass_id(auxDomain.getIdNode());
                 else
                    logger.warn("No se ha encontrado el tipo de informe: "+esteNodo.getTagName());

                 Element node = XMLTranslator_LCGIII.recuperarHijo(esteNodo, RESULT);
                 if(node != null)
                 {
                       Element node2 = XMLTranslator_LCGIII.recuperarHijo(node, DQ_CONFRES);
                       if(node2 != null)
                       {
                              Element node3 = XMLTranslator_LCGIII.recuperarHijo(node2, SPECIFICATION);
                              if(node3 != null)
                              {
                                  result.setCitation((CI_Citation)CI_CitationXML.load(node3));
                              }
                              result.setExplanation(XMLTranslator_LCGIII.recuperarHoja(node2, EXPLANATION));
                              String pass=XMLTranslator_LCGIII.recuperarHoja(node2, PASS);
                              if (pass!=null&&pass.equals("1"))
                                      result.setPass(true);

                       }
                       node2 = XMLTranslator_LCGIII.recuperarHijo(node, DQ_QUANTRES);
                       if(node2 != null)
                       {
                                   result.setValue(XMLTranslator_LCGIII.recuperarHoja(node2, VALUE));
                                   result.setValueunit(XMLTranslator_LCGIII.recuperarHoja(node2, VALUEUNIT));
                       }


                  }
                  return result;

             }

            public void save(XMLTranslator traductor) throws Exception {
                if (elemento==null || traductor==null) return;
                traductor.insertar_tag_begin(RESULT);
                traductor.insertar_tag_begin(DQ_CONFRES);
                         traductor.insertar(EXPLANATION,elemento.getExplanation());
                         traductor.insertar(PASS,elemento.isPass()?"1":"0");
                         if (elemento.getCitation()!=null)
                         {
                             traductor.save(new CI_CitationXML(elemento.getCitation(),language),SPECIFICATION);
                         }
                traductor.insertar_tag_end(DQ_CONFRES);
                traductor.insertar_tag_begin(DQ_QUANTRES);
                    traductor.insertar(VALUE,elemento.getValue());
                    traductor.insertar(VALUEUNIT,elemento.getValueunit());
                traductor.insertar_tag_end(DQ_QUANTRES);
                traductor.insertar_tag_end(RESULT);


            }

            public static final String RESULT = "result";
            public static final String SPECIFICATION = "specification";
            public static final String DQ_CONFRES = "DQ_ConformanceResult";
            public static final String EXPLANATION = "explanation";
            public static final String PASS = "pass";
            public static final String DQ_QUANTRES = "DQ_QuantitativeResult";
            public static final String DQ_ELEMENT= "DQ_Element";
            public static final String VALUE = "value";
            public static final String VALUEUNIT = "valueUnit";
      }

