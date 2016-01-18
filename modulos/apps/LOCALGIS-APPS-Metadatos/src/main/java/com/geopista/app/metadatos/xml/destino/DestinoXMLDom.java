/**
 * DestinoXMLDom.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.xml.destino;

import java.io.OutputStream;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.Method;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
//import oracle.xml.parser.v2.XMLDocument;
//import org.jdom.Document;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 21-jul-2004
 * Time: 16:33:49
 */
public class DestinoXMLDom
 {
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(DestinoXMLDom.class);
    public DestinoXMLDom(Document doc)
     {
         _outputstream = null;
         _doc = doc;
         _node = _doc;
         _tipo =Dom;
     }

     public DestinoXMLDom(OutputStream outputstream)
     {
         _outputstream = outputstream;

         try
         {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            _doc = docBuilder.newDocument();
         }catch(Exception e){}
         _node = _doc;
         _tipo = Stream;
     }




    public Document getDom()
    {
        return _doc;
    }

    public int getNbLapsIndentacion()
    {
        return -1;
    }

    public int getNbLapsSubclass()
    {
        return -1;
    }

    public Node getNode()
    {
        return _node;
    }

    public long getTiempoIndentacion()
    {
        return -1L;
    }

    public long getTiempoSubclass()
    {
        return -1L;
    }

    public String getURL()
    {
        return null;
    }

    public void insertar(String cadena)
    {
        org.w3c.dom.Text hijo = _doc.createTextNode(cadena);
        _node.appendChild(hijo);
    }

    public void insertar_tag_begin(String cadena)
    {
        org.w3c.dom.Element hijo = _doc.createElement(cadena);
        _node.appendChild(hijo);
        _node = hijo;
    }

    public void insertar_tag_end(String cadena)
    {
        if(!_node.getNodeName().equals(cadena))
        {
            logger.warn("WARNING!!!-- Se esta intentando cerrar un nodo que no corresponde:"+ cadena+" ::: " +_node.getNodeName());
            return;
        } else
        {
            _node = (Node)_node.getParentNode();
            return;
        }
    }

    public void insertar_tag_header_doctype(String doc_tag, String dtd)
    {
       //_doc.setDoctype(doc_tag, dtd, null);
    }

    public void insertar_tag_header_xml(String version, String encoding) throws Exception
    {
       // _doc.setEncoding(encoding);
        _encoding=encoding;
       // _doc.setVersion(version);
        _version=version;
    }

    public void setDom(Document value)
    {
        _doc = value;
    }

    public void close() throws Exception
    {
        if (_tipo==Dom) return;
        //_doc.print(_outputstream);
        print();
        _outputstream.close();
        return;
    }
    public void print()
    {
        try
        {
            if (_doc==null || _outputstream==null)  return;
            PrintWriter pw=new PrintWriter(_outputstream);
            XMLSerializer serial=new XMLSerializer(pw,
               new OutputFormat(Method.XML,"ISO-8859-1",false));
            serial.serialize(_doc);
        }catch(Exception e)
        {}
    }
    private Document _doc;
    private OutputStream _outputstream;
    private Node _node;
     private static final int Dom = 2;
    private static final int Stream = 1;
    private int _tipo;
    private String _encoding="ISO-8859-1";
    private String _version="1.0";
}