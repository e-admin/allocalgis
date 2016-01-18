/**
 * MD_DataIdentificationXML.java
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
import com.geopista.protocol.metadatos.CI_Citation;
import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.metadatos.EX_Extent;
import com.geopista.protocol.metadatos.MD_DataIdentification;
import com.geopista.protocol.metadatos.MD_LegalConstraint;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-sep-2004
 * Time: 11:05:54
 */
public class MD_DataIdentificationXML implements IXMLElemento{

   private static org.apache.log4j.Logger  logger = org.apache.log4j.Logger.getLogger(MD_DataIdentificationXML.class);
    private String language=null;

    public MD_DataIdentificationXML() {
    }

    public MD_DataIdentificationXML(MD_DataIdentification dataIdentification) {
        this.dataIdentification = dataIdentification;
    }
    public MD_DataIdentificationXML(MD_DataIdentification dataIdentification, String language) {
        this.dataIdentification = dataIdentification;
        this.language=language;
    }
    public String getTag()
    {
        return MD_DATAID;
    }



   public static MD_DataIdentification load(Element nod)  throws Exception
   {
       MD_DataIdentification dataIdentification;
       Element esteNodo;
       Element nodo;
       //String inclass;
       //GeographicalCoordinates gc;

       dataIdentification = new MD_DataIdentification();
       esteNodo = XMLTranslator_LCGIII.recuperarHijo(nod, MD_DATAID);
       if(esteNodo == null)
           return dataIdentification;

       if((nodo = XMLTranslator_LCGIII.recuperarHijo(esteNodo, CITATION)) != null)
           dataIdentification.setCitacion((CI_Citation)CI_CitationXML.load(nodo));
       else
           logger.error("Posible error al cargar MD_DataIdentificacion--> no tiene citacion");
       logger.debug("Añadiendo el resumen");
       dataIdentification.setResumen(XMLTranslator_LCGIII.recuperarHoja(esteNodo, ABSTRACT));
       logger.debug("Añadiendo el proposito");
       dataIdentification.setPurpose(XMLTranslator_LCGIII.recuperarHoja(esteNodo,PURPOSE));
       logger.debug("Añadiendo los idiomas");
       Vector auxVector =  new Vector();
       NodeList listaElem = ((org.w3c.dom.Element)esteNodo).getElementsByTagName(LANGUAGE);
       if(listaElem != null)
       {
            for(int i = 0; i < listaElem.getLength(); i++)
            {
                  Element lan = (Element)listaElem.item(i);
                  DomainNode auxDomain=   Estructuras.getListaLanguage().getDomainNode((String)lan.getFirstChild().getNodeValue());
                  if (auxDomain!=null) auxVector.add(auxDomain.getIdNode());
           }
        }
        dataIdentification.setIdiomas(auxVector);
        dataIdentification.setCharacterset(XMLTranslator_LCGIII.recuperarHoja(esteNodo, CHARSET));
        nodo = XMLTranslator_LCGIII.recuperarHijo(esteNodo,PTCONTAC);
        if(nodo == null)
            logger.warn("pointOfContact incomplete");
        else
        {
            dataIdentification.setResponsibleParty((CI_ResponsibleParty)CI_ResponsiblePartyXML.load(nodo));
            dataIdentification.setRolecode_id(dataIdentification.getResponsibleParty()!=null?dataIdentification.getResponsibleParty().getIdRole():null);
        }
        //Añadimos el topic category
        Vector auxVectorTopic =  new Vector();
        NodeList listaElemTopic = ((org.w3c.dom.Element)esteNodo).getElementsByTagName(TOPICCAT);
        if(listaElemTopic != null)
       {
            for(int i = 0; i < listaElemTopic.getLength(); i++)
            {
                  org.w3c.dom.Element top = (org.w3c.dom.Element)listaElemTopic.item(i);
                  DomainNode auxDomain=   Estructuras.getListaTopicCategory().getDomainNode((String)top.getFirstChild().getNodeValue());
                  if (auxDomain!=null) auxVectorTopic.add(auxDomain.getIdNode());
                  else//preguntamos por la traducción
                  {
                      auxDomain = Estructuras.getListaTopicCategory().getDomainNodeByTraduccion((String)top.getFirstChild().getNodeValue());
                      if (auxDomain!=null) auxVectorTopic.add(auxDomain.getIdNode());
                  }

           }
        }
        dataIdentification.setCategorias(auxVectorTopic);

        //Añadimos la representación espacial
         Vector auxVectorSpa =  new Vector();
         NodeList listaElemSpa = ((org.w3c.dom.Element)esteNodo).getElementsByTagName(SPTATIALREPTYPE);
         if(listaElemSpa != null)
         {
            for(int i = 0; i < listaElemSpa.getLength(); i++)
            {
                  org.w3c.dom.Element spa = (org.w3c.dom.Element)listaElemSpa.item(i);
                  DomainNode auxDomain=   Estructuras.getListaSpatialRepresentation().getDomainNode((String)spa.getFirstChild().getNodeValue());
                  if (auxDomain!=null) auxVectorSpa.add(auxDomain.getIdNode());
                  else//preguntamos por la traducción
                  {
                      auxDomain = Estructuras.getListaSpatialRepresentation().getDomainNodeByTraduccion((String)spa.getFirstChild().getNodeValue());
                      if (auxDomain!=null) auxVectorSpa.add(auxDomain.getIdNode());
                  }

           }
         }
         dataIdentification.setrEspacial(auxVectorSpa);

         //añadimos la extension
         logger.debug("Añadiendo la extension");
         nodo = XMLTranslator_LCGIII.recuperarHijo(esteNodo, EXTENT);
         if(nodo != null)
              dataIdentification.setExtent((EX_Extent)EX_ExtentXML.load(nodo));
         //añadimos la escala
          nodo = XMLTranslator_LCGIII.recuperarHijo(esteNodo, SPTRESOLUTION);
          if(nodo != null)
          {
               nodo = XMLTranslator_LCGIII.recuperarHijo(nodo, EQUIVALENTSCALE);
               if(nodo != null)
               {
                   nodo = XMLTranslator_LCGIII.recuperarHijo(nodo, MD_REPFRACT);
                   if(nodo != null)
                       dataIdentification.setResolucion(new Long(EX_ExtentXML.quitaPuntos(XMLTranslator_LCGIII.recuperarHoja(nodo, DENOMINATOR))));
               }
           }
          //añadimos los gráficos
          listaElem = ((org.w3c.dom.Element)esteNodo).getElementsByTagName(OVERVIEW);
          if(listaElem != null)
          {
              Vector vGraficos=new Vector();
              for(int i = 0; i < listaElem.getLength(); i++)
              {
                  Element nodoMD_Browse = XMLTranslator_LCGIII.recuperarHijo((Element)listaElem.item(i), MD_BROWSE);
                  if(nodoMD_Browse != null)
                  {
                        String fileName=XMLTranslator_LCGIII.recuperarHoja(nodoMD_Browse, FILENAME);
                        if (fileName!=null) vGraficos.add(fileName);
                  }
              }
              dataIdentification.setGraficos(vGraficos);
        }
        //añadimos las restricciones
        nodo = XMLTranslator_LCGIII.recuperarHijo(esteNodo,METACONS);
        if(nodo!= null)
        {
            dataIdentification.setConstraint((MD_LegalConstraint)MD_LegalConstraintXML.load(nodo));
        }





    /*   idInfo.getDescription().setAdditionalInformation(recuperador.recuperarHoja(esteNodo, "supplementalInformation"));
       idInfo.setDatasetCredit(recuperador.recuperarHoja(esteNodo, "credit"));
       idInfo.setStatus(new Status());
       idInfo.getStatus().setProgress(recuperador.recuperarHoja(esteNodo, "status"));
       recuperador;
       nodo = XMLTranslator.recuperarHijo(esteNodo, "resourceMaintenance");
       if(nodo != null)
       {
           recuperador;
           nodo = XMLTranslator.recuperarHijo(nodo, "MD_MaintenanceInformation");
           if(nodo != null)
               idInfo.getStatus().setMaintenanceAndUpdateFrequency(recuperador.recuperarHoja(nodo, "maintenanceAndUpdateFrequency"));
       }
       recuperador;
       idInfo.setKeywords(new Keywords());
       listaElem = esteNodo.getChildrenByTagName("descriptiveKeywords");
       if(listaElem != null)
       {
           for(int i = 0; i < listaElem.getLength(); i++)
               KeywordsXML.load(recuperador, (XMLElement)listaElem.item(i), idInfo.getKeywords());

       }
       ++++++++++++++++++++++++++++++
       idInfo.setEnvironment(recuperador.recuperarHoja(esteNodo, "environmentDescription"));
       if(idInfo.getSpatialDomain().getGeographicalCoordinates() == null)
           idInfo.getSpatialDomain().setGeographicalCoordinates(new GeographicalCoordinates());
       gc = idInfo.getSpatialDomain().getGeographicalCoordinates();
       recuperador;
           if(idInfo.getTimePeriodOfContent() == null)
               idInfo.setTimePeriodOfContent(new TimePeriodOfContent());
           if(minf != null)
               idInfo.getTimePeriodOfContent().setEndDate(minf);
           if(maxf != null)
               idInfo.getTimePeriodOfContent().setStartDate(maxf);

       }     */
       return dataIdentification;
   }

    public void save(XMLTranslator traductor) throws Exception
    {
          if(dataIdentification == null) dataIdentification = new MD_DataIdentification();
          traductor.save(new CI_CitationXML(dataIdentification.getCitacion(),language), CITATION);
          traductor.insertar(ABSTRACT, dataIdentification.getResumen(), XMLTranslator.UNKNOWN);
          traductor.insertar(PURPOSE, dataIdentification.getPurpose(), XMLTranslator.UNKNOWN);
          if (dataIdentification.getIdiomas()!=null)
          {
              for(Enumeration e=dataIdentification.getIdiomas().elements(); e.hasMoreElements();)
              {
                  DomainNode lan = Estructuras.getListaLanguage().getDomainNodeById((String)e.nextElement());
                  traductor.insertar(LANGUAGE, (language==null?lan.getPatron():lan.getTerm(language)),XMLTranslator.UNKNOWN);
              }
          }
          if (dataIdentification.getResponsibleParty()!=null) dataIdentification.getResponsibleParty().setIdRole(dataIdentification.getRolecode_id());
          traductor.insertar(CHARSET, dataIdentification.getCharacterset(), XMLTranslator.UNKNOWN);
          traductor.save(new CI_ResponsiblePartyXML(dataIdentification.getResponsibleParty(),language), PTCONTAC);
          if (dataIdentification.getCategorias()!=null)
          {
                for(Enumeration e=dataIdentification.getCategorias().elements();e.hasMoreElements();)
                {
                    DomainNode cat= Estructuras.getListaTopicCategory().getDomainNodeById((String)e.nextElement());
                    traductor.insertar(TOPICCAT, (language==null?cat.getPatron():cat.getTerm(language)), XMLTranslator.UNKNOWN);
                }
          }
          if (dataIdentification.getrEspacial()!=null)
          {
                for(Enumeration e=dataIdentification.getrEspacial().elements();e.hasMoreElements();)
                {
                    DomainNode res= Estructuras.getListaSpatialRepresentation().getDomainNodeById((String)e.nextElement());
                    traductor.insertar(SPTATIALREPTYPE, (language==null?res.getPatron():res.getTerm(language)), XMLTranslator.UNKNOWN);
                }
          }
          traductor.save(new EX_ExtentXML(dataIdentification.getExtent()),EXTENT);
          //salvamos la escala
          if (dataIdentification.getResolucion()!=null)
          {
                traductor.insertar_tag_begin(SPTRESOLUTION);
                traductor.insertar_tag_begin(EQUIVALENTSCALE);
                traductor.insertar_tag_begin(MD_REPFRACT);
                traductor.insertar(DENOMINATOR,dataIdentification.getResolucion().toString());
                traductor.insertar_tag_end(MD_REPFRACT);
                traductor.insertar_tag_end(EQUIVALENTSCALE);
                traductor.insertar_tag_end(SPTRESOLUTION);
         }

          if(dataIdentification.getGraficos() != null)
          {
              for(Enumeration e=dataIdentification.getGraficos().elements();e.hasMoreElements();)
              {
                  traductor.insertar_tag_begin(OVERVIEW);
                  traductor.insertar_tag_begin(MD_BROWSE);
                  traductor.insertar(FILENAME,(String)e.nextElement());
                  traductor.insertar_tag_end(MD_BROWSE);
                  traductor.insertar_tag_end(OVERVIEW);
              }

          }
          //añadimos las restricciones
          if (dataIdentification.getConstraint()!=null)
          {
             traductor.save(new MD_LegalConstraintXML(dataIdentification.getConstraint(),language),METACONS);
          }

       /*
          traductor.insertar("credit", _este.getDatasetCredit(), XMLTranslationHelper.UNKNOWN);
          if(_este.getStatus() != null)
              traductor.insertar("status", _este.getStatus().getProgress(), XMLTranslationHelper.UNKNOWN);
          saveConstraints(traductor);
          if(_este.getStatus() != null && _este.getStatus().getMaintenanceAndUpdateFrequency() != null)
          {
              traductor.insertar_tag_begin("resourceMaintenance");
              traductor.insertar_tag_begin("MD_MaintenanceInformation");
              traductor.insertar("maintenanceAndUpdateFrequency", _este.getStatus().getMaintenanceAndUpdateFrequency(), XMLTranslationHelper.UNKNOWN);
              traductor.insertar_tag_end("MD_MaintenanceInformation");
              traductor.insertar_tag_end("resourceMaintenance");
          }
          if(_este.getKeywords() != null)
          {
              KeywordsXML key = new KeywordsXML(_este.getKeywords());
              Enumeration theme = _este.getKeywords().getTheme();
              if(theme != null)
                  for(; theme.hasMoreElements(); traductor.insertar_tag_end("descriptiveKeywords"))
                  {
                      traductor.insertar_tag_begin("descriptiveKeywords");
                      key.saveTheme(traductor, (Theme)theme.nextElement());
                  }

              Enumeration place = _este.getKeywords().getPlace();
              if(place != null)
                  for(; place.hasMoreElements(); traductor.insertar_tag_end("descriptiveKeywords"))
                  {
                      traductor.insertar_tag_begin("descriptiveKeywords");
                      key.savePlace(traductor, (Place)place.nextElement());
                  }

              Enumeration stratum = _este.getKeywords().getStratum();
              if(stratum != null)
                  for(; stratum.hasMoreElements(); traductor.insertar_tag_end("descriptiveKeywords"))
                  {
                      traductor.insertar_tag_begin("descriptiveKeywords");
                      key.saveStratum(traductor, (Stratum)stratum.nextElement());
                  }

              Enumeration temporal = _este.getKeywords().getTemporal();
              if(temporal != null)
                  for(; temporal.hasMoreElements(); traductor.insertar_tag_end("descriptiveKeywords"))
                  {
                      traductor.insertar_tag_begin("descriptiveKeywords");
                      key.saveTemporal(traductor, (Temporal)temporal.nextElement());
                  }

          }

          if(_este.getSpatialDomain() != null)
          {
              traductor.insertar_tag_begin("spatialResolution");
              traductor.insertar_tag_begin("equivalentScale");
              traductor.insertar_tag_begin("MD_RepresentativeFraction");
              traductor.insertar("denominator", _este.getSpatialDomain().getScale(), XMLTranslationHelper.UNKNOWN);
              traductor.insertar_tag_end("MD_RepresentativeFraction");
              traductor.insertar_tag_end("equivalentScale");
              traductor.insertar_tag_end("spatialResolution");
          }

          traductor.insertar("environmentDescription", _este.getEnvironment(), XMLTranslationHelper.UNKNOWN);
          if(_este.getDescription() != null)
              traductor.insertar("supplementalInformation", _este.getDescription().getAdditionalInformation(), XMLTranslationHelper.UNKNOWN);
              */
      }







    public static final String ABSTRACT = "abstract";
    public static final String ACCCONST = "accconst";
    public static final String CHARSET = "characterSet";
    public static final String CITATION = "citation";
    public static final String CREDIT = "credit";
    public static final String DATACRED = "datacred";
    public static final String DENOMINATOR = "denominator";
     public static final String ENVIRONMENT = "environmentDescription";
    public static final String EQUIVALENTSCALE = "equivalentScale";
    public static final String EXTENT = "extent";
    public static final String EXTENTT = "extent";

    public static final String EX_TEMPEXT = "EX_TemporalExtent";

    public static final String KEYWORDS = "descriptiveKeywords";
    public static final String LANGUAGE = "language";
    public static final String MAINUPFREC = "maintenanceAndUpdateFrequency";
    public static final String MD_DATAID = "MD_DataIdentification";
    public static final String MD_MAINT = "MD_MaintenanceInformation";
    public static final String MD_REPFRACT = "MD_RepresentativeFraction";
    public static final String METAC = "accessConstraints";
    public static final String METACONS = "resourceConstraints";
    public static final String METMAINT = "resourceMaintenance";
    public static final String NATIVE = "native";
    public static final String OTHCONST = "othconst";
    public static final String OVERVIEW = "graphicOverview";
    public static final String PTCONTAC = "pointOfContact";
    public static final String PURPOSE = "purpose";
    public static final String SECINFO = "secinfo";
    public static final String SPTATIALREPTYPE = "spatialRepresentationType";
    public static final String SPTRESOLUTION = "spatialResolution";
    public static final String STATUS = "status";
    public static final String SUPINFO = "supplementalInformation";
    public static final String TEMPELEM = "temporalElement";
    public static final String TOPICCAT = "topicCategory";
    public static final String USECONST = "useconst";
    public static final String MD_BROWSE = "MD_BrowseGraphic";
    public static final String FILENAME = "fileName";
    private MD_DataIdentification dataIdentification;
}
