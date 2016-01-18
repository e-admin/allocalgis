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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;




/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 21-jul-2004
 * Time: 13:42:04
 */
public class XMLTranslator_LCGIII {
    private static Logger logger = Logger.getLogger(XMLTranslator_LCGIII.class);

  //LCGIII.Desplazado del paquete LOCALGIS-Metadatos
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
    
    //LCGIII.Desplazado del paquete LOCALGIS-Metadatos
    public static NodeList recuperarHijos(Element esteNodo, String tag)
    {
        Element xmlElement = null;
        if(esteNodo != null)
        {
            NodeList listaElem = esteNodo.getElementsByTagName(tag);
            return listaElem;
        }
        return null;

    }    
  //LCGIII.Desplazado del paquete LOCALGIS-Metadatos
    public static Vector <Element>recuperarHijosAsVector(Element esteNodo, String tag)
    {
        NodeList listaElem = esteNodo.getElementsByTagName(tag);
        Vector <Element> vector = new Vector<Element>();
        for(int i = 0; i < listaElem.getLength(); i++)
        {
            Element xmlElement = (Element)listaElem.item(i);
            vector.addElement(xmlElement);
        }
        return vector;
    }
  //LCGIII.Desplazado del paquete LOCALGIS-Metadatos
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
  
  //LCGIII.Desplazado del paquete LOCALGIS-Metadatos
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
    
}
