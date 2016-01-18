package com.geopista.app.utilidades.xml;

import org.w3c.dom.*;
import org.apache.log4j.Logger;


import java.util.Vector;
import java.util.Date;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

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
 * Time: 13:42:04
 */
public class GeopistaTranslator {

    private static Logger logger = Logger.getLogger(GeopistaTranslator.class);
    protected static final String UNKNOWN = null;
    public GeopistaTranslator(GeopistaTranslationInfo infoTraXML)
    {
          _infos = infoTraXML;
    }

    public static Element recuperarHijo(Element esteNodo, String tag)
       {
           Element xmlElement = null;
           if(esteNodo != null)
           {
               NodeList listaElem = esteNodo.getElementsByTagName(tag);
               if(listaElem != null)
                   xmlElement = (Element)listaElem.item(0);
           }
           return xmlElement;
       }
    public  String devuelveValor(Node nodo)
      {
         if(nodo == null)
         {
             return null;
         }
         Node xmlHoja = nodo.getChildNodes().item(0);
         if(xmlHoja == null)
             return null;
         else
             return xmlHoja.getNodeValue();
     }
    public static String recuperarHoja(Element esteNodo, String tag)
      {
         if(esteNodo == null)
         {
             return null;
         }
         Element xmlElement = recuperarHijo(esteNodo, tag);
         if(xmlElement == null)
             return null;
         Node xmlHoja = (Node)xmlElement.getChildNodes().item(0);
         if(xmlHoja == null)
             return null;
         else
             return xmlHoja.getNodeValue();
     }
    public static Vector recuperarHojasAsVector(Element esteNodo, String tag)
    {
        NodeList listaElem = esteNodo.getElementsByTagName(tag);
        Vector vector = new Vector();
        for(int i = 0; i < listaElem.getLength(); i++)
        {
            Element xmlElement = (Element)listaElem.item(i);
            Node xmlHoja = (Node)xmlElement.getChildNodes().item(0);
            vector.addElement(xmlHoja.getNodeValue());
        }
        return vector;
    }

    public Vector getNodes(Element padre)
    {
        Vector vectorNode = new Vector();
        NodeList nodeList= padre.getChildNodes();
        for (int i=0; i<nodeList.getLength();i++)
        {
            Node node= nodeList.item(i);
            if ( node.getNodeType()==Node.ELEMENT_NODE)
                vectorNode.add(node);
        }
        return vectorNode;
    }


      public Element getRoot()
       {
           return _raiz;
       }

       public void parsear() throws Exception
        {
            Document xml;
            try
            {
                if(_infos.getInputReader() != null)
                {
                     xml = xmlToDom(_infos.getInputReader(), System.out, _infos.getValidatingMode());
                } else
                {
                    xml = xmlToDom(new InputStreamReader(_infos.getURL().openStream()), System.out, _infos.getValidatingMode());
                }
            }
            catch(Exception ex)
            {
                throw ex;
            }
            _raiz = (Element)xml.getDocumentElement();
            //_infos.setDataEncoding(doc.getEncoding());
            //_infos.setXmlVersion(xml.getVersion());
        }
    /*   public void cargar() throws Exception
        {
            try
            {
                if(_infos.getOutputWriter() != null)
                {
                     _destinoXML = domToXml(_infos.getOutputWriter());
                } else
                {
                    _destinoXML = domToXml(new FileOutputStream(_infos.getURLasString()));
                }
            }
            catch(Exception ex)
            {
                throw ex;
            }
        } */
        public static org.w3c.dom.Document xmlToDom(Reader procedencia, OutputStream salida, boolean valid_mode)
            throws Exception
        {
            try
            {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                StringBuffer cadena= new StringBuffer("");
                char[] b = new char[255];
                int bytesRead = 0;
                while ((bytesRead = procedencia.read(b)) != -1) {
                           cadena.append(b,0,bytesRead);
                }
                System.out.println(cadena.toString());
                ByteArrayInputStream entradaParser= new ByteArrayInputStream(cadena.toString().getBytes());
   	 		    org.w3c.dom.Document doc = docBuilder.parse ((InputStream) entradaParser);
                return doc;
                /*DOMParser parser = new DOMParser();
                parser.setErrorStream(salida);
                parser.showWarnings(valid_mode);
                if(valid_mode)
                    parser.setValidationMode(2);
                else
                    parser.setValidationMode(0);
                parser.parse(procedencia);
                XMLDocument xml = parser.getDocument();
                return xml;*/
            }catch (Exception e)
            {
                throw e;
            }
        }
        public static java.util.Date recuperarHoja_Date(Element esteNodo, String tag)
            throws Exception
        {
            logger.debug("Intentando recuperar una fecha");
            return fgdcToDate(recuperarHoja(esteNodo, tag));
        }

        public static Timestamp recuperarHoja_Timestamp(Element esteNodo, String tag)
            throws Exception
        {
            return fgdcToTimestamp(recuperarHoja(esteNodo, tag));
        }

        public static Date fgdcToDate(String cad) throws Exception
        {
           if(cad == null)
                return null;
          if(cad.equals("Unknown"))
          {
                return null;
          } else
          {
                String cadena = cad.trim();
                try
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return dateFormat.parse(cadena);
                }catch(Exception e)
                {
                    try
                    {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        logger.debug("parseando la fecha "+cadena);
                        return dateFormat.parse(cadena);
                    }catch(Exception ex)
                    {
                         logger.error("Excepcion al convertir la fecha "+cad+"e.toString()");
                         throw (ex);
                    }
                }
          }
      }

      public String getAtributo (Node nodo, String nombreAtributo)
      {
            NamedNodeMap atts;
            atts = nodo.getAttributes();
            if (atts==null) return null;
            for (int i=0; i< atts.getLength();i++)
            {
                 Node atributo= atts.item(i);
                 if (atributo.getNodeName().equalsIgnoreCase(nombreAtributo))
                    return atributo.getNodeValue();
            }
            return null;
      }
      public static Timestamp fgdcToTimestamp(String cad) throws Exception
      {
          if(cad == null)  return null;

          Date d = fgdcToDate(cad);
          if(d == null)
            throw new Exception("Error, convertion to Timestamp from String: <<" + cad + ">>");
        return new Timestamp(d.getTime());
     }
    static String dateToFgdc(Date d)
    {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
           return sdf.format(d);
    }


    public GeopistaTranslationInfo get_infos() {
        return _infos;
    }

    public void set_infos(GeopistaTranslationInfo _infos) {
        this._infos = _infos;
    }


       private GeopistaTranslationInfo _infos;
       private org.w3c.dom.Element _raiz;
}
