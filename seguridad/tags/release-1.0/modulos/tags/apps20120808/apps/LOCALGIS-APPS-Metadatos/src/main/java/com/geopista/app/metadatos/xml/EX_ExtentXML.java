package com.geopista.app.metadatos.xml;

import com.geopista.protocol.metadatos.EX_Extent;
import com.geopista.protocol.metadatos.EX_GeographicBoundingBox;
import com.geopista.protocol.metadatos.EX_VerticalExtent;
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
 * Date: 07-sep-2004
 * Time: 11:15:40
 */
public class EX_ExtentXML implements IXMLElemento{
            private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EX_ExtentXML.class);
            EX_Extent extent;
            public EX_ExtentXML(EX_Extent extent)
            {
                this.extent=extent;
            }

            public EX_ExtentXML()
            {
            }

            public String getTag()
            {
                return "EX_Extent";
            }

            public EX_Extent getExtent()
            {
                return extent;
            }

            public static Object load(Element nodoPadre)
            {
                EX_Extent result = new EX_Extent();
                Element nodoextent = XMLTranslator_LCGIII.recuperarHijo((Element)nodoPadre, EX_EXTENT);
                if(nodoextent == null)  return null;
                Element nodoGeographic = XMLTranslator_LCGIII.recuperarHijo(nodoextent, GEOELEM);
                if (nodoGeographic!=null)
                {
                    Element nodoBox=XMLTranslator_LCGIII.recuperarHijo(nodoGeographic, EX_GEOBOUNDING);
                    if (nodoBox!=null)
                    {
                        EX_GeographicBoundingBox box= new EX_GeographicBoundingBox();
                        if (XMLTranslator_LCGIII.recuperarHoja(nodoBox, NORTHBL)!=null)
                        {
                            box.setNorth(new Float(XMLTranslator_LCGIII.recuperarHoja(nodoBox, NORTHBL).trim()).floatValue());
                        }
                        if (XMLTranslator_LCGIII.recuperarHoja(nodoBox, SOUTHBL)!=null)
                        {
                            box.setSouth(new Float(XMLTranslator_LCGIII.recuperarHoja(nodoBox, SOUTHBL).trim()).floatValue());
                        }
                        if (XMLTranslator_LCGIII.recuperarHoja(nodoBox, EASTBL)!=null)
                        {
                            box.setEast(new Float(XMLTranslator_LCGIII.recuperarHoja(nodoBox, EASTBL).trim()).floatValue());
                        }
                        if (XMLTranslator_LCGIII.recuperarHoja(nodoBox, WESTBL)!=null)
                        {
                            box.setWest(new Float(XMLTranslator_LCGIII.recuperarHoja(nodoBox, WESTBL).trim()).floatValue());
                        }
                        String typeCode=XMLTranslator_LCGIII.recuperarHoja(nodoBox,EXTENTTYPECODE);
                        if (typeCode!=null&&typeCode.equals("1")) box.setExtenttypecode(true);
                        else
                        box.setExtenttypecode(false);

                        result.setBox(box);
                     }
                }
                Element nodoVertical = XMLTranslator_LCGIII.recuperarHijo(nodoextent,VERTELEM) ;
                if (nodoVertical!=null)
                {
                    Element nodoEXVer=XMLTranslator_LCGIII.recuperarHijo(nodoVertical, EX_VERT);
                    if (nodoEXVer!=null)
                    {
                        EX_VerticalExtent vertical = new EX_VerticalExtent();
                        try{vertical.setMin(new Long(quitaPuntos(XMLTranslator_LCGIII.recuperarHoja(nodoEXVer, MINVAL))).longValue());}catch(Exception e){};
                        try{vertical.setMax(new Long(quitaPuntos(XMLTranslator_LCGIII.recuperarHoja(nodoEXVer, MAXVAL))).longValue());}catch(Exception e){};
                        try{vertical.setUnit(XMLTranslator_LCGIII.recuperarHoja(nodoEXVer, UNITMEASURE));}catch(Exception e){};
                        result.setVertical(vertical);
                    }
                }
                result.setDescription(XMLTranslator_LCGIII.recuperarHoja(nodoextent,DESCRIPTION));
                return result;
            }
            public static String quitaPuntos(String cadena)
            {
                if (cadena==null)return null;
                while(cadena.indexOf('.')>=0)
                {
                    if (cadena.indexOf('.')==0)
                        cadena=cadena.substring(1);
                    else if (cadena.indexOf('.')==cadena.length()-1)
                    {
                         cadena=cadena.substring(0,cadena.length()-1);
                    }
                    else
                    {
                        cadena=cadena.substring(0,cadena.indexOf('.'))+cadena.substring(cadena.indexOf('.')+1);
                    }
                    logger.debug("Cadena: "+cadena);
                }
                return cadena;
            }
           public void save(XMLTranslator traductor)  throws Exception
           {
              if (extent==null) return;
              traductor.insertar_tag_begin(GEOELEM);
              traductor.insertar_tag_begin(EX_GEOBOUNDING);
              if(extent.getBox() != null )
              {
                  traductor.insertar(WESTBL,new Float( extent.getBox().getWest()), null);
                  traductor.insertar(EASTBL, new Float(extent.getBox().getEast()), null);
                  traductor.insertar(SOUTHBL, new Float(extent.getBox().getSouth()), null);
                  traductor.insertar(NORTHBL, new Float(extent.getBox().getNorth()), null);
                  traductor.insertar(EXTENTTYPECODE, extent.getBox().isExtenttypecode()?"1":"0");
              }
              traductor.insertar_tag_end(EX_GEOBOUNDING);
              traductor.insertar_tag_end(GEOELEM);

              traductor.insertar_tag_begin(VERTELEM);
              if (extent.getVertical()!=null)
              {
                  traductor.insertar_tag_begin(EX_VERT);
                  traductor.insertar(MINVAL,new Long(extent.getVertical().getMin()),null);
                  traductor.insertar(MAXVAL,new Long(extent.getVertical().getMax()),null);
                  traductor.insertar(UNITMEASURE, extent.getVertical().getUnit(),null);
                  traductor.insertar_tag_end(EX_VERT);
              }
              traductor.insertar_tag_end(VERTELEM);
              traductor.insertar(DESCRIPTION,extent.getDescription());
        }
            public static final String EX_EXTENT = "EX_Extent";
            public static final String GEOELEM = "geographicElement";
            public static final String EX_GEOBOUNDING = "EX_GeographicBoundingBox";
            public static final String NORTHBL = "northBoundLatitude";
            public static final String SOUTHBL = "southBoundLatitude";
            public static final String EASTBL = "eastBoundLongitude";
            public static final String WESTBL = "westBoundLongitude";
            public static final String VERTELEM = "verticalElement";
            public static final String DESCRIPTION = "description";
            public static final String EX_VERT = "EX_VerticalExtent";
            public static final String MAXVAL = "maximumValue";
            public static final String MINVAL = "minimumValue";
            public static final String UNITMEASURE = "unitOfMeasure";
            public static final String EXTENTTYPECODE = "extentTypeCode";






}
