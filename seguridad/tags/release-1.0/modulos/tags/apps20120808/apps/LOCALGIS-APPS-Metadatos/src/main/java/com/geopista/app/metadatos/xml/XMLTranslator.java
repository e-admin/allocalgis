package com.geopista.app.metadatos.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import java.util.Vector;
import java.util.Date;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.geopista.app.metadatos.xml.destino.DestinoXMLDom;
//import oracle.xml.parser.v2.XMLElement;
//import oracle.xml.parser.v2.XMLDocument;
//import oracle.xml.parser.v2.DOMParser;

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
public class XMLTranslator {
    private static Logger logger = Logger.getLogger(XMLTranslator.class);

     protected static final String UNKNOWN = null;
    DestinoXMLDom _destinoXML;

    public XMLTranslator(XMLTranslationInfo infoTraXML)
    {
         if(infoTraXML.getDestinoXML() != null)
                _destinoXML = infoTraXML.getDestinoXML();
            else
            {
                 try
                 {
                    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                    Document doc = docBuilder.newDocument();
                    _destinoXML = new DestinoXMLDom(doc);
                 }catch(Exception e){}
            }

          _infos = infoTraXML;
    }

    //LCGIII.Desplazado al paquete LOCALGIS-Utils
    /*public static Element recuperarHijo(Element esteNodo, String tag)
       {
           Element xmlElement = null;
           if(esteNodo != null)
           {
               NodeList listaElem = esteNodo.getElementsByTagName(tag);
               if(listaElem != null)
                   xmlElement = (Element)listaElem.item(0);
           }
           return xmlElement;
       }*/
    
    //LCGIII.Desplazado al paquete LOCALGIS-Utils
    /*public static NodeList recuperarHijos(Element esteNodo, String tag)
    {
        Element xmlElement = null;
        if(esteNodo != null)
        {
            NodeList listaElem = esteNodo.getElementsByTagName(tag);
            return listaElem;
        }
        return null;

    }*/
    //LCGIII.Desplazado al paquete LOCALGIS-Utils
    /*public static String recuperarHoja(Element esteNodo, String tag)
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
     }*/
    
    //LCGIII.Desplazado al paquete LOCALGIS-Utils
    /*public static Vector recuperarHojasAsVector(Element esteNodo, String tag)
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
    }*/
    
    //LCGIII.Desplazado al paquete LOCALGIS-Utils
    /*public static Vector <Element>recuperarHijosAsVector(Element esteNodo, String tag)
    {
        NodeList listaElem = esteNodo.getElementsByTagName(tag);
        Vector <Element> vector = new Vector<Element>();
        for(int i = 0; i < listaElem.getLength(); i++)
        {
            Element xmlElement = (Element)listaElem.item(i);
            vector.addElement(xmlElement);
        }
        return vector;
    }*/
    public boolean insertar(String tag, Object o, String alternativa)
    {
            String os;
            if(o != null)
                os = o.toString();
            else
                os = alternativa;
            return insertar(tag, os);
    }

        public boolean insertar(String tag, String texto)
        {
            if(texto == null)
              return false;
            insertar_tag_begin(tag);
            insertar(texto);
            insertar_tag_end(tag);
            return true;
        }

        private void insertar(String cadena)
        {
            _destinoXML.insertar(cadena);
        }

    public boolean insertar(String tag, java.util.Date date)
     {
        if (date ==null) return false;
        String cad_date = dateToFgdc(date);
        return insertar(tag, cad_date);
    }



        public void insertar_tag_begin(String cadena)
        {
            if(cadena != null)
                _destinoXML.insertar_tag_begin(cadena);
        }

        public void insertar_tag_end(String cadena)
        {
            if(cadena != null)
                _destinoXML.insertar_tag_end(cadena);
        }

        public int insertar_v(String tag, Vector vector)
        {
            if(vector == null)
                return 0;
            for(int j = 0; j < vector.size(); j++)
                insertar(tag, (String)vector.elementAt(j), null);

            return vector.size();
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
        public DestinoXMLDom domToXml(OutputStream writer)
        {
            return new DestinoXMLDom(writer);
        }
        public void cargar() throws Exception
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
        }
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
            return fgdcToDate(XMLTranslator_LCGIII.recuperarHoja(esteNodo, tag));
        }

        public static Timestamp recuperarHoja_Timestamp(Element esteNodo, String tag)
            throws Exception
        {
            return fgdcToTimestamp(XMLTranslator_LCGIII.recuperarHoja(esteNodo, tag));
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
      public static Timestamp fgdcToTimestamp(String cad) throws Exception
      {
          if(cad == null)  return null;

          Date d = fgdcToDate(cad);
          if(d == null)
            throw new Exception("Error, convertion to Timestamp from String: <<" + cad + ">>");
        return new Timestamp(d.getTime());
     }
    public static String dateToFgdc(Date d)
    {
    	if(d==null) return null;
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(d);
    }

    public XMLTranslationInfo get_infos() {
        return _infos;
    }

    public void set_infos(XMLTranslationInfo _infos) {
        this._infos = _infos;
    }

    public void close()  throws Exception
    {
            _destinoXML.close();
    }

    public void commit()throws Exception
    {
        close();
    }



       public void save(MD_MetadataXML metadato) throws Exception
       {
           if (_destinoXML==null) throw new Exception("Fichero destino no especificado");
           _destinoXML.insertar_tag_header_xml(_infos.getXmlVersion(), _infos.getDataEncoding());
           if(_infos.getDTD() != null)
               _destinoXML.insertar_tag_header_doctype(metadato.getTag(), _infos.getDTD());
           insertar_tag_begin(metadato.getTag());
           metadato.save(this);
           insertar_tag_end(metadato.getTag());
           _destinoXML.close();
           return;
       }
         public void save(IXMLElemento elemento) throws Exception
       {
           if (_destinoXML==null) throw new Exception("Fichero destino no especificado");
           _destinoXML.insertar_tag_header_xml(_infos.getXmlVersion(), _infos.getDataEncoding());
           if(_infos.getDTD() != null)
               _destinoXML.insertar_tag_header_doctype(elemento.getTag(), _infos.getDTD());
           insertar_tag_begin(elemento.getTag());
           elemento.save(this);
           insertar_tag_end(elemento.getTag());
           _destinoXML.close();
           return;
       }
       public void save(IXMLElemento elemento, String tag_plus) throws Exception
       {
            if (elemento==null) return;
            insertar_tag_begin(tag_plus);
            insertar_tag_begin(elemento.getTag());
            elemento.save(this);
            insertar_tag_end(elemento.getTag());
            insertar_tag_end(tag_plus);
       }

       private XMLTranslationInfo _infos;
       private org.w3c.dom.Element _raiz;
}
