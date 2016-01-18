/**
 * CI_ResponsiblePartyXML.java
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
import com.geopista.protocol.metadatos.CI_OnLineResource;
import com.geopista.protocol.metadatos.CI_ResponsibleParty;




public class CI_ResponsiblePartyXML implements IXMLElemento
{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CI_ResponsiblePartyXML.class);
    CI_ResponsibleParty responsibleParty;
    private String language=null;

    public CI_ResponsiblePartyXML(CI_ResponsibleParty este)
    {
        responsibleParty=este;
    }

    public CI_ResponsiblePartyXML(CI_ResponsibleParty este,String language)
    {
        responsibleParty=este;
        this.language=language;
    }
    public CI_ResponsiblePartyXML()
    {
        this(null);
    }


    public String getTag()
    {
        return "CI_ResponsibleParty";
    }

    public CI_ResponsibleParty getResponsibleParty()
    {
        return responsibleParty;
    }

    public static CI_ResponsibleParty load(Element nodoPadre)
    {
        CI_ResponsibleParty result = new CI_ResponsibleParty();
        Element esteNodo = XMLTranslator_LCGIII.recuperarHijo((Element)nodoPadre, CI_RESPPARTY);
        if(esteNodo == null) return null;
        result.setIndividualName(XMLTranslator_LCGIII.recuperarHoja(esteNodo,INDNAME));
        result.setOrganisationName(XMLTranslator_LCGIII.recuperarHoja(esteNodo,ORGNAME));
        result.setPositionName(XMLTranslator_LCGIII.recuperarHoja(esteNodo, POSNAME));
        DomainNode auxDomain=Estructuras.getListaRoles().getDomainNode(XMLTranslator_LCGIII.recuperarHoja(esteNodo, ROLE));
        if (auxDomain!=null)
            result.setIdRole(auxDomain.getIdNode());

        Element nodoex = XMLTranslator_LCGIII.recuperarHijo(esteNodo,CNTINFO);
        if(nodoex == null)
        {
            logger.warn("contactInf incomplete");
        } else
        {
            Element nodo = XMLTranslator_LCGIII.recuperarHijo(nodoex,CI_CONTACT);
            if(nodo == null)
            {
                logger.warn("CI_Contact incomplete");
            } else
            {
                Element elnodo = XMLTranslator_LCGIII.recuperarHijo(nodo, PHONE);
                if(elnodo != null)
                {
                    elnodo = XMLTranslator_LCGIII.recuperarHijo(elnodo, CI_TELEPHONE);
                    if(elnodo != null)
                    {
                        result.setCi_telephone_voice(XMLTranslator_LCGIII.recuperarHojasAsVector(elnodo, VOICE));
                        result.setCi_telephone_facsimile(XMLTranslator_LCGIII.recuperarHojasAsVector(elnodo,FAX));
                    }
                }
                Element nodoaddex = XMLTranslator_LCGIII.recuperarHijo(nodo, ADDRESS);
                if(nodoaddex == null)
                {
                   logger.warn("address incomplete");
                } else
                {
                    Element nodoadd = XMLTranslator_LCGIII.recuperarHijo(nodoaddex, CI_ADDRESS);
                    if(nodoadd == null)
                    {
                        logger.warn("address incomplete");
                    } else
                    {
                        result.setDeliveryPoint(XMLTranslator_LCGIII.recuperarHojasAsVector(nodoadd,DELPOINT));
                        result.setAddressCity(XMLTranslator_LCGIII.recuperarHoja(nodoadd,CITY ));
                        result.setAddressAdministrativeArea(XMLTranslator_LCGIII.recuperarHoja(nodoadd, ADMINAREA));
                        result.setAddressPostalCode(XMLTranslator_LCGIII.recuperarHoja(nodoadd, POSTALCODE));
                        result.setAddressCountry(XMLTranslator_LCGIII.recuperarHoja(nodoadd, COUNTRY));
                        result.setElectronicMailAdress(XMLTranslator_LCGIII.recuperarHojasAsVector(nodoadd, EMAIL));
                    }
                }

                Element nodoonline = XMLTranslator_LCGIII.recuperarHijo(nodo, ONLINERES);
                result.setOnLineResource((CI_OnLineResource)CI_OnLineResourceXML.load(nodoonline));
                result.setHoursOfService(XMLTranslator_LCGIII.recuperarHoja(nodo, HOURS));
                result.setContactInstructions(XMLTranslator_LCGIII.recuperarHoja(nodo, CNTINST));
            }
        }
        return result;
    }


    public void save(XMLTranslator traductor)  throws Exception
    {
        if(responsibleParty == null)
        {
            responsibleParty = CI_ResponsibleParty.getDefaultContact();
            logger.warn("We use the default contact");
        }
        traductor.insertar(INDNAME, responsibleParty.getIndividualName(), null);
        traductor.insertar(ORGNAME, responsibleParty.getOrganisationName(), null);
        traductor.insertar(POSNAME, responsibleParty.getPositionName(), null);
        DomainNode auxDomain=Estructuras.getListaRoles().getDomainNodeById(responsibleParty.getIdRole());
        if (auxDomain!=null)
            traductor.insertar(ROLE,(language==null?auxDomain.getPatron():auxDomain.getTerm(language)));

        traductor.insertar_tag_begin(CNTINFO);
        traductor.insertar_tag_begin(CI_CONTACT);
        traductor.insertar_tag_begin(PHONE);
        traductor.insertar_tag_begin(CI_TELEPHONE);
        traductor.insertar_v(VOICE, responsibleParty.getCi_telephone_voice());
        traductor.insertar_v(FAX, responsibleParty.getCi_telephone_facsimile());
        traductor.insertar_tag_end(CI_TELEPHONE);
        traductor.insertar_tag_end(PHONE);
        traductor.insertar_tag_begin(ADDRESS);
        traductor.insertar_tag_begin(CI_ADDRESS);
        traductor.insertar_v(DELPOINT, responsibleParty.getDeliveryPoint());
        traductor.insertar(CITY, responsibleParty.getAddressCity(), null);
        traductor.insertar(ADMINAREA, responsibleParty.getAddressAdministrativeArea(), null);
        traductor.insertar(POSTALCODE, responsibleParty.getAddressPostalCode(), null);
        traductor.insertar(COUNTRY, responsibleParty.getAddressCountry(), null);
        traductor.insertar_v(EMAIL, responsibleParty.getElectronicMailAdress());
        traductor.insertar_tag_end(CI_ADDRESS);
        traductor.insertar_tag_end(ADDRESS);
        traductor.save(new CI_OnLineResourceXML(responsibleParty.getOnLineResource(),language),ONLINERES);

        traductor.insertar(HOURS, responsibleParty.getHoursOfService(), null);
        traductor.insertar(CNTINST, responsibleParty.getContactInstructions(), null);
        traductor.insertar_tag_end(CI_CONTACT);
        traductor.insertar_tag_end(CNTINFO);
    }

    public static final String ADDRESS = "address";
    public static final String ADMINAREA = "administrativeArea";
    public static final String CITY = "city";
    public static final String CI_ADDRESS = "CI_Address";
    public static final String CI_CONTACT = "CI_Contact";
    public static final String CI_RESPPARTY = "CI_ResponsibleParty";
    public static final String CI_TELEPHONE = "CI_Telephone";
    public static final String CNTINFO = "contactInfo";
    public static final String CNTINST = "contactInstructions";
    public static final String COUNTRY = "country";
    public static final String DELPOINT = "deliveryPoint";
    public static final String EMAIL = "electronicMailAddress";
    public static final String FAX = "facsimile";
    public static final String HOURS = "hoursOfService";
    public static final String INDNAME = "individualName";
    public static final String ONLINERES = "onlineResource";
    public static final String ORGNAME = "organisationName";
    public static final String PHONE = "phone";
    public static final String POSNAME = "positionName";
    public static final String POSTALCODE = "postalCode";
    public static final String ROLE = "role";
    public static final String VOICE = "voice";
}
