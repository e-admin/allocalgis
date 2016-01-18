/**
 * MD_MetadataXML.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.xml;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.CI_OnLineResource;
import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.metadatos.MD_Format;
import com.geopista.protocol.metadatos.MD_Metadata;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-sep-2004
 * Time: 10:37:06
 */
public class MD_MetadataXML {
     private Logger logger = Logger.getLogger(MD_MetadataXML.class);
     MD_Metadata metadato;
     private String language=null;

    public MD_MetadataXML()
    {
    }

    public MD_MetadataXML(MD_Metadata metadato)
    {
        this.metadato=metadato;
    }
     public MD_MetadataXML(MD_Metadata metadato, String language)
    {
        this.metadato=metadato;
        this.language=language;
    }

    public String getTag()
    {
        return MD_METADATA;
    }

    public MD_Metadata load(XMLTranslator recuperador) throws Exception
    {
        metadato= new MD_Metadata();
        logger.debug("Recuperando el metadato para el fichero: "+recuperador.get_infos()!=null?recuperador.get_infos().getURLasString():" NULO");
        org.w3c.dom.Element esteNodo;

        esteNodo = recuperador.getRoot();

        //Datas simples del metadato
        //  No meto el fileidentifier porque puede dar problemas
        //  metadato.setFileidentifier(XMLTranslator.recuperarHoja(esteNodo, MDFILEID));
        DomainNode auxDomain = Estructuras.getListaLanguage().getDomainNode(XMLTranslator_LCGIII.recuperarHoja(esteNodo, METALANG));
        if (auxDomain!=null)
            metadato.setLanguage_id(auxDomain.getIdNode());
        auxDomain=Estructuras.getListaScopeCode().getDomainNode(XMLTranslator_LCGIII.recuperarHoja(esteNodo, MDHRLV));
        if (auxDomain!=null)
               metadato.setScopecode_id(auxDomain.getIdNode());
        else
        {
            auxDomain=Estructuras.getListaScopeCode().getDomainNodeByTraduccion(XMLTranslator_LCGIII.recuperarHoja(esteNodo, MDHRLV));
            if (auxDomain!=null)
            {
                metadato.setScopecode_id(auxDomain.getIdNode());
            }
        }

        //Ponemos el identificador
        Element nodo = XMLTranslator_LCGIII.recuperarHijo(esteNodo, METAINFO);
        if(nodo == null)
            throw new Exception ("identificationInfo no encontrada");
        else
            metadato.setIdentificacion(MD_DataIdentificationXML.load(nodo));

        metadato.setDatestamp(XMLTranslator.recuperarHoja_Timestamp(esteNodo, METD));
        metadato.setMetadatastandardname(XMLTranslator_LCGIII.recuperarHoja(esteNodo, METSTDN));
        metadato.setMetadatastandardversion(XMLTranslator_LCGIII.recuperarHoja(esteNodo, METSTDV));
        Element nodoContacto = XMLTranslator_LCGIII.recuperarHijo(esteNodo, METC);
        if(nodoContacto == null)
            logger.warn("Contacto no encontrado en el metadato");
        else
        {
            metadato.setResponsibleParty((CI_ResponsibleParty)CI_ResponsiblePartyXML.load(nodoContacto));
            metadato.setRolecode_id(metadato.getResponsibleParty()!=null?metadato.getResponsibleParty().getIdRole():null);
        }

        //Añadimos la distribucion
         Element nodoDistribucion = XMLTranslator_LCGIII.recuperarHijo(esteNodo, DISTRIBINFO);
         if(nodoDistribucion != null)
         {
             Element nodeMD_Dis = XMLTranslator_LCGIII.recuperarHijo(nodoDistribucion, MD_DISTRIBUTION);
             if(nodeMD_Dis != null)
             {
                 NodeList listaElem3 = ((org.w3c.dom.Element)nodeMD_Dis).getElementsByTagName(DISTFORM);
                 if(listaElem3 != null)
                 {
                      Vector vecFormato = new Vector();
                      for(int i = 0; i < listaElem3.getLength(); i++)
                      {
                              vecFormato.addElement((MD_Format)MD_FormatXML.load( (org.w3c.dom.Element)listaElem3.item(i)));
                      }
                      metadato.setFormatos(vecFormato);
                 }
                    NodeList listaElem2 = ((org.w3c.dom.Element)nodeMD_Dis).getElementsByTagName(TRANSOPT);
                    if(listaElem2 != null)
                    {
                        Vector vec = new Vector();
                        for(int i = 0; i < listaElem2.getLength(); i++)
                        {
                            CI_OnLineResource auxOnline=(CI_OnLineResource)MD_DigitalTransferOptionsXML.load( (org.w3c.dom.Element)listaElem2.item(i));
                            if (auxOnline!=null)
                                vec.addElement(auxOnline);
                        }
                       metadato.setOnlineresources(vec);
                    }
             }

         }
         //Añadimos la calidad
        nodo = XMLTranslator_LCGIII.recuperarHijo(esteNodo, DATAQUALITY);
        if (nodo!=null)
        {
            metadato.setCalidad(DQ_DataQualityXML.load(nodo));
        }
        //Añadimos el sistema de referencia
        NodeList listaElem = ((org.w3c.dom.Element)esteNodo).getElementsByTagName(REFSYSINFO);
        if(listaElem != null)
        {
            Vector vec = new Vector();
            for(int i = 0; i < listaElem.getLength(); i++)
            {
                 String referenceSystem=(String)MD_ReferenceSystemXML.load( (org.w3c.dom.Element)listaElem.item(i));
                 if (referenceSystem!=null)
                     vec.addElement(referenceSystem);
            }
            metadato.setReference(vec);
        }

       /*   recuperador;
        nodo = XMLTranslator.recuperarHijo(esteNodo, "metadataMaintenance");
        if(nodo != null)
        {
            recuperador;
            nodo = XMLTranslator.recuperarHijo(nodo, "MD_MaintenanceInformation");
            if(nodo != null)
                _este.setFutureReview(recuperador.recuperarHoja_Timestamp(nodo, "dateOfNextUpdate"));
        }
        recuperador;
        nodo = XMLTranslator.recuperarHijo(esteNodo, "spatialRepresentationInfo");
        if(nodo != null)
        {
            String value = null;
            if(_este.getSpatialDataOrganizationInformation() != null)
                value = _este.getSpatialDataOrganizationInformation().getDirectSpatialReferenceType();
            _este.setSpatialDataOrganizationInformation((SpatialDataOrganizationInformation)SpatialDataOrganizationInformationXML.load(recuperador, nodo));
            if(value != null)
                _este.getSpatialDataOrganizationInformation().setDirectSpatialReferenceType(value);
        }
        recuperador;
        XMLElement refsys = XMLTranslator.recuperarHijo(esteNodo, "referenceSystemInfo");
        if(refsys != null)
            _este.setCoordinateReferenceSystem((MD_CRS)MD_CRSXML.load(recuperador, refsys));
        listaElem = esteNodo.getChildrenByTagName("contentInfo");
        if(listaElem == null) goto _L2; else goto _L1
_L1:
        int i = 0;
          goto _L3
_L4:
        XMLElement node;
        recuperador;
        node = XMLTranslator.recuperarHijo((XMLElement)listaElem.item(i), "MD_FeatureCatalogueDescription");
        if(node != null)
        {
            if(_este.getEntityAndAttributeInformation() == null)
                _este.setEntityAndAttributeInformation(new EntityAndAttributeInformation());
            if(_este.getEntityAndAttributeInformation().getDetailedDescriptionVector() == null)
                _este.getEntityAndAttributeInformation().setDetailedDescriptionVector(new Vector());
            NodeList listaElem2 = node.getChildrenByTagName("featureTypes");
            if(listaElem2 != null)
            {
                for(int j = 0; j < listaElem2.getLength(); j++)
                {
                    XMLElement ft = (XMLElement)listaElem2.item(j);
                    Text ftText = (Text)ft.getFirstChild();
                    String value2add = ftText.getNodeValue();
                    DetailedDescription dd = new DetailedDescription();
                    dd.setLabel(value2add);
                    dd.setEntityType(new EntityType());
                    dd.getEntityType().setLabel(value2add);
                    _este.getEntityAndAttributeInformation().getDetailedDescriptionVector().add(dd);
                }

            }
            break MISSING_BLOCK_LABEL_584;
        }
        recuperador;
        node = XMLTranslator.recuperarHijo((XMLElement)listaElem.item(i), "MD_ImageDescription");
        if(node == null)
            break MISSING_BLOCK_LABEL_583;
        if(_este.getDataQualityInformation() == null)
            _este.setDataQualityInformation(new DataQualityInformation());
        _este.getDataQualityInformation().setCloudCover(new Short(recuperador.recuperarHoja(node, "cloudCoverPercentage")));
        break MISSING_BLOCK_LABEL_582;
        Exception er;
        er;
        i++;
_L3:
        if(i < listaElem.getLength()) goto _L4; else goto _L2
_L2:
        recuperador;
        _este.setParentIdentifier(recuperador.recuperarHoja(esteNodo, "parentIdentifier"));
        recuperador;
           listaElem = esteNodo.getChildrenByTagName("metadataConstraints");
        if(listaElem != null)
        {
            Vector vec = new Vector();
            for(int i = 0; i < listaElem.getLength(); i++)
            {
                XMLElement tag = (XMLElement)listaElem.item(i);
                recuperador;
                XMLElement nodo2 = XMLTranslator.recuperarHijo(tag, "MD_LegalConstraints");
                if(nodo2 != null)
                {
                    _este.setAccessConstraints(recuperador.recuperarHoja(nodo2, "accessConstraints"));
                    _este.setUseConstraints(recuperador.recuperarHoja(nodo2, "useConstraints"));
                    _este.setOtherConstraints(recuperador.recuperarHoja(nodo2, "otherConstraints"));
                } else
                {
                    _este.setMetadataSecurityInformation((MetadataSecurityInformation)MetadataSecurityInformationXML.load(recuperador, tag));
                }
            }

        }*/
        return metadato;
    }

    public void save(XMLTranslator traductor) throws Exception
    {
        if(metadato == null) return;
        if (metadato.getLanguage_id()!=null)
        {
            DomainNode auxDomain = Estructuras.getListaLanguage().getDomainNodeById(metadato.getLanguage_id());
            traductor.insertar(METALANG,(this.language==null?auxDomain.getPatron():auxDomain.getTerm(language)));
        }
        if (metadato.getScopecode_id()!=null)
        {
            DomainNode auxDomain = Estructuras.getListaScopeCode().getDomainNodeById(metadato.getScopecode_id());
            traductor.insertar(MDHRLV,this.language==null?auxDomain.getPatron():auxDomain.getTerm(language));
        }
        traductor.insertar(CHARACTERSET,metadato.getCharacterset());

        traductor.save(new MD_DataIdentificationXML(metadato.getIdentificacion(),language),METAINFO);
        traductor.insertar(METD, metadato.getDatestamp());
        if(metadato.getMetadatastandardname() == null || metadato.getMetadatastandardname().trim().equals(""))
          traductor.insertar(METSTDN, "ISO 19115 Geographic information - Metadata", null);
        else
          traductor.insertar(METSTDN,metadato.getMetadatastandardname());
        if(metadato.getMetadatastandardversion() != null && !metadato.getMetadatastandardversion().trim().equals(""))
          traductor.insertar(METSTDV, "ISO 19115:2003");
        else
           traductor.insertar(METSTDV, metadato.getMetadatastandardversion());

        if (metadato.getResponsibleParty()!=null) metadato.getResponsibleParty().setIdRole(metadato.getRolecode_id());
        traductor.save(new CI_ResponsiblePartyXML(metadato.getResponsibleParty(),language), METC);
        //salvamos la distribucion

        if (metadato.getFormatos()!=null || metadato.getOnlineresources()!=null)
        {
              traductor.insertar_tag_begin(DISTRIBINFO);
              traductor.insertar_tag_begin(MD_DISTRIBUTION);
              if (metadato.getFormatos()!=null)
              {
                  for (Enumeration e=metadato.getFormatos().elements();e.hasMoreElements();)
                  {
                      MD_FormatXML auxFormatXML = new MD_FormatXML((MD_Format)e.nextElement());
                      traductor.save(auxFormatXML,DISTFORM);
                  }
              }
              if (metadato.getOnlineresources()!=null)
              {
                  for(Enumeration e=metadato.getOnlineresources().elements();e.hasMoreElements();)
                  {
                      MD_DigitalTransferOptionsXML auxTransferXML=new MD_DigitalTransferOptionsXML((CI_OnLineResource)e.nextElement(),language);
                      traductor.save(auxTransferXML, TRANSOPT);
                   }
              }
              traductor.insertar_tag_end(MD_DISTRIBUTION);
              traductor.insertar_tag_end(DISTRIBINFO);
         }
         if (metadato.getCalidad()!=null)
         {
             traductor.save(new DQ_DataQualityXML(metadato.getCalidad(),language),DATAQUALITY);
         }
         if (metadato.getReference()!=null)
         {
             for (Enumeration e=metadato.getReference().elements();e.hasMoreElements();)
             {
                traductor.save(new MD_ReferenceSystemXML((String)e.nextElement(),language),REFSYSINFO);
             }
         }
   /*
        if(_este.getFutureReview() != null)
        {
            traductor.insertar_tag_begin("metadataMaintenance");
            traductor.insertar_tag_begin("MD_MaintenanceInformation");
            traductor.insertar("dateOfNextUpdate", _este.getFutureReview(), XMLTranslationHelper.UNKNOWN);
            traductor.insertar_tag_end("MD_MaintenanceInformation");
            traductor.insertar_tag_end("metadataMaintenance");
        }
        SpatialDataOrganizationInformation sdoi = _este.getSpatialDataOrganizationInformation();
        if(sdoi != null)
        {
            traductor.insertar_tag_begin("spatialRepresentationInfo");
            traductor.save(new SpatialDataOrganizationInformationXML(sdoi), null);
            traductor.insertar_tag_end("spatialRepresentationInfo");
        }
        if(_este.getCoordinateReferenceSystem() != null)
        {
            traductor.insertar_tag_begin("referenceSystemInfo");
            traductor.save(new MD_CRSXML(_este.getCoordinateReferenceSystem()), null);
            traductor.insertar_tag_end("referenceSystemInfo");
        }
        if(_este.getEntityAndAttributeInformation() != null)
        {
            Vector ddv = _este.getEntityAndAttributeInformation().getDetailedDescriptionVector();
            if(ddv != null && ddv.size() > 0)
            {
                traductor.insertar_tag_begin("contentInfo");
                traductor.insertar_tag_begin("MD_FeatureCatalogueDescription");
                for(int i = 0; i < ddv.size(); i++)
                {
                    DetailedDescription dd = (DetailedDescription)ddv.elementAt(i);
                    traductor.insertar("featureTypes", dd.getEntityType().getLabel(), null);
                }

                traductor.insertar_tag_end("MD_FeatureCatalogueDescription");
                traductor.insertar_tag_end("contentInfo");
            }
        }
        if(_este.getDataQualityInformation() != null && _este.getDataQualityInformation().getCloudCover() != null)
        {
            traductor.insertar_tag_begin("contentInfo");
            traductor.insertar_tag_begin("MD_ImageDescription");
            traductor.insertar("cloudCoverPercentage", _este.getDataQualityInformation().getCloudCover(), null);
            traductor.insertar_tag_end("MD_ImageDescription");
            traductor.insertar_tag_end("contentInfo");
        }
        DistributionInformation dis = _este.getDistributionInformationVector();
        if(dis != null)
        {
            traductor.insertar_tag_begin("distributionInfo");
            traductor.save(new DistributionInformationXML(dis), null);
            traductor.insertar_tag_end("distributionInfo");
        }
        if(_este.getMetadataReference() != null)
            saveMetadataInf(traductor);
        lodemas = null;
        try
        {
            if(_este.getLeftover() != null)
            {
                String leftover = _este.getLeftover().getText();
                if(leftover != null && leftover.length() > 0)
                {
                    traductor;
                    lodemas = XMLTranslator.xmlToDom(new StringReader(leftover), System.err, false);
                }
            }
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            traductor.addWarning("Leftover couldn't be loaded as DOM");
        }
        if(lodemas == null)
            break MISSING_BLOCK_LABEL_594;
        oracle.xml.parser.v2.XMLDocument interm = traductor.getXMLDocument();
        oracle.xml.parser.v2.XMLDocument finalDOM = LeftoverCSDGM.join(traductor, interm, lodemas);
        traductor.setXMLDocument(finalDOM);
        break MISSING_BLOCK_LABEL_593;
        interm;
        traductor.addWarning("Leftover couldn't be merged");*/

        return;
    }

   /*

    public void saveMetadataInf(XMLTranslator traductor)
        throws XMLTranslationException
    {
        traductor.insertar("fileIdentifier", _este.getFileIdentifier(), XMLTranslationHelper.UNKNOWN);
        traductor.insertar("characterSet", _este.getCharacterSet(), XMLTranslationHelper.UNKNOWN);
         traductor.insertar("parentIdentifier", _este.getParentIdentifier(), XMLTranslationHelper.UNKNOWN);

        i   Vector vec = _este.getMetadataExtensionVector();
        if(vec != null)
        {
            MetadataExtensionXML mx = new MetadataExtensionXML(null);
            for(int i = 0; i < vec.size(); i++)
            {
                mx.setTranslatable((MetadataExtension)vec.elementAt(i));
                traductor.save(mx, null);
            }

        }
        traductor.insertar("status", _este.getStatus(), XMLTranslationHelper.UNKNOWN);
        traductor.insertar("dropDate", _este.getDropDate(), XMLTranslationHelper.UNKNOWN);
    }

    public void setTranslatable(Translatable value)
    {
        _este = (MetadataReference)value;
    }
     */
    public static final String CHARACTERSET = "characterSet";
    public static final String CLOUDCOVER = "cloudCoverPercentage";
    public static final String CONTENTINFO = "contentInfo";
    public static final String DATAQUALITY = "dataQualityInfo";
    public static final String DATNUP = "dateOfNextUpdate";
    public static final String DISTRIBINFO = "distributionInfo";
    public static final String DROPDATE = "dropDate";
    public static final String FEATURETYPE = "featureTypes";
    public static final String MDFILEID = "fileIdentifier";
    public static final String MDHRLV = "hierarchyLevel";
    public static final String MDPARENTID = "parentIdentifier";
    public static final String MD_FEATURECD = "MD_FeatureCatalogueDescription";
    public static final String MD_IMAGEDESCR = "MD_ImageDescription";
    public static final String MD_LEGALCONS = "MD_LegalConstraints";
    public static final String MD_MAINT = "MD_MaintenanceInformation";
    public static final String MD_METADATA = "MD_Metadata";
    public static final String METAC = "accessConstraints";
    public static final String METACONS = "metadataConstraints";
    public static final String METAINFO = "identificationInfo";
    public static final String METALANG = "language";
    public static final String METC = "contact";
    public static final String METD = "dateStamp";
    public static final String METFRD = "metfrd";
    public static final String METMAINT = "metadataMaintenance";
    public static final String METOC = "otherConstraints";
    public static final String METRD = "metrd";
    public static final String METSI = "metsi";
    public static final String METSTDN = "metadataStandardName";
    public static final String METSTDV = "metadataStandardVersion";
    public static final String METTC = "mettc";
    public static final String METUC = "useConstraints";
    public static final String REFSYSINFO = "referenceSystemInfo";
    public static final String SPATIALINFO = "spatialRepresentationInfo";
    public static final String STATUS = "status";
    public static final String MD_DISTRIBUTION = "MD_Distribution";
    public static final String DISTFORM = "distributionFormat";
    public static final String TRANSOPT = "transferOptions";


}
