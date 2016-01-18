package com.geopista.app.metadatos.xml;

import com.geopista.protocol.metadatos.DQ_DataQuality;
import com.geopista.protocol.metadatos.DQ_Element;
import com.geopista.protocol.metadatos.LI_Linage;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Vector;
import java.util.Enumeration;

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
 * Date: 07-sep-2004
 * Time: 16:41:41
 */
public class DQ_DataQualityXML implements IXMLElemento{
       private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DQ_DataQualityXML.class);
       DQ_DataQuality calidad;
       private String language;

        public DQ_DataQualityXML() {
        }

        public DQ_DataQualityXML(DQ_DataQuality calidad) {
            this.calidad = calidad;
        }

        public DQ_DataQualityXML(DQ_DataQuality calidad, String language) {
            this.calidad = calidad;
            this.language = language;
        }

        public String getTag()
        {
            return DQ_DATAQUALITY;
        }

        public static DQ_DataQuality load(Element nodoPadre)
                throws Exception
        {
             DQ_DataQuality result = new DQ_DataQuality();
             Element esteNodo = XMLTranslator_LCGIII.recuperarHijo(nodoPadre, DQ_DATAQUALITY);
             if(esteNodo == null) return null;
             NodeList listaElem = ((org.w3c.dom.Element)esteNodo).getElementsByTagName(REPORT);
             if(listaElem != null)
             {
                   Vector vec = new Vector();
                   DQ_Element si = null;
                   for(int i = 0; i < listaElem.getLength(); i++)
                   {
                       si = (DQ_Element)DQ_ElementXML.load((org.w3c.dom.Element)listaElem.item(i));
                       if(si != null) vec.addElement(si);
                   }
                   result.setElements(vec);
              }

              Element  xmlElement = XMLTranslator_LCGIII.recuperarHijo(esteNodo, LINEAGE);
              if(xmlElement == null)
              {
                    logger.warn("Lineage element not found");
              } else
              {
                   LI_Linage resultLinage= new LI_Linage();
                   xmlElement = XMLTranslator_LCGIII.recuperarHijo(xmlElement, LI_LINEAGE);
                   if(xmlElement != null)
                   {
                         resultLinage.setStatement(XMLTranslator_LCGIII.recuperarHoja(xmlElement,STATEMENT));
                         listaElem = ((org.w3c.dom.Element)xmlElement).getElementsByTagName(SOURCE);
                         if(listaElem != null)
                         {
                              Vector vec = new Vector();
                              for(int i = 0; i < listaElem.getLength(); i++)
                              {
                                    Element nodoSource = XMLTranslator_LCGIII.recuperarHijo( (org.w3c.dom.Element)listaElem.item(i), LI_SOURCE);
                                    if(nodoSource != null)
                                    {
                                            String sDescription = XMLTranslator_LCGIII.recuperarHoja(nodoSource, DESCRIPTION);
                                           if (sDescription!=null) vec.add(sDescription);
                                    }
                              }
                              resultLinage.setSources(vec);
                         }
                         listaElem =((org.w3c.dom.Element)xmlElement).getElementsByTagName(PROCESSSTEP);
                         if(listaElem != null)
                         {
                                Vector vec = new Vector();
                                for(int i = 0; i < listaElem.getLength(); i++)
                                {
                                   Element nodoStep = XMLTranslator_LCGIII.recuperarHijo( (org.w3c.dom.Element)listaElem.item(i), LI_PROCSTEP);
                                    if(nodoStep != null)
                                    {
                                            String sDescription = XMLTranslator_LCGIII.recuperarHoja(nodoStep, DESCRIPTION);
                                           if (sDescription!=null) vec.add(sDescription);
                                    }
                                }

                                resultLinage.setSteps(vec);
                          } else
                          {
                                logger.warn("processStep element not found");
                           }
                          result.setLinage(resultLinage);
                    }
                }
                logger.debug("Se ha recuperado correctamente la calidad");
                return result;
         }

         public void save(XMLTranslator traductor) throws Exception {
             if (calidad==null||traductor==null) return;
             if (calidad.getElements()!=null)
             {
                 for(Enumeration e=calidad.getElements().elements();e.hasMoreElements();)
                 {
                    traductor.save(new DQ_ElementXML((DQ_Element)e.nextElement(),language),REPORT);
                 }
             }
             if (calidad.getLinage()!=null)
             {
                 traductor.insertar_tag_begin(LINEAGE);
                 traductor.insertar_tag_begin(LI_LINEAGE);
                 traductor.insertar(STATEMENT,calidad.getLinage().getStatement());
                 if (calidad.getLinage().getSources()!=null)
                 {
                      for (Enumeration e=calidad.getLinage().getSources().elements();e.hasMoreElements();)
                      {
                          traductor.insertar_tag_begin(SOURCE);
                          traductor.insertar_tag_begin(LI_SOURCE);
                          traductor.insertar(DESCRIPTION, (String)e.nextElement());
                          traductor.insertar_tag_end(LI_SOURCE);
                          traductor.insertar_tag_end(SOURCE);
                      }
                 }

                 if (calidad.getLinage().getSteps()!=null)
                 {
                      for (Enumeration e= calidad.getLinage().getSteps().elements();e.hasMoreElements();)
                      {
                            traductor.insertar_tag_begin(PROCESSSTEP);
                            traductor.insertar_tag_begin(LI_PROCSTEP);
                            traductor.insertar(DESCRIPTION, (String)e.nextElement());
                            traductor.insertar_tag_end(LI_PROCSTEP);
                            traductor.insertar_tag_end(PROCESSSTEP);
                      }
                 }
                 traductor.insertar_tag_end(LI_LINEAGE);
                 traductor.insertar_tag_end(LINEAGE);
             }
         }

            public static final String DQ_DATAQUALITY = "DQ_DataQuality";
            public static final String LINEAGE = "lineage";
            public static final String LI_LINEAGE = "LI_Lineage";
            public static final String PROCESSSTEP = "processStep";
            public static final String REPORT = "report";
            public static final String SOURCE = "source";
            public static final String STATEMENT = "statement";
            public static final String DESCRIPTION = "description";
            public static final String LI_SOURCE = "LI_Source";
            public static final String LI_PROCSTEP = "LI_ProcessStep";

  }

