package com.geopista.protocol.metadatos;

import java.util.Vector;

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
 * Date: 20-jul-2004
 * Time: 10:29:40
 */
public class CI_ResponsibleParty {
    String id;
    String individualName;
    String organisationName;
    String positionName;
    String addressCity;
    String addressAdministrativeArea;
    String addressPostalCode;
    String addressCountry;
    CI_OnLineResource onLineResource;
    Vector electronicMailAdress;
    Vector deliveryPoint;
    Vector ci_telephone_facsimile;
    Vector ci_telephone_voice;
    String idRole;
    String role;
    String hoursOfService;
    String contactInstructions;


    public String getIdRole() {
        return idRole;
    }

    public String getAddressAdministrativeArea() {
        return addressAdministrativeArea;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public Vector getCi_telephone_facsimile() {
        return ci_telephone_facsimile;
    }

    public Vector getCi_telephone_voice() {
        return ci_telephone_voice;
    }

    public Vector getDeliveryPoint() {
        return deliveryPoint;
    }

    public Vector getElectronicMailAdress() {
        return electronicMailAdress;
    }

    public String getId() {
        return id;
    }

    public String getIndividualName() {
        return individualName;
    }

    public CI_OnLineResource getOnLineResource() {
        return onLineResource;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setAddressAdministrativeArea(String addressAdministrativeArea) {
        this.addressAdministrativeArea = addressAdministrativeArea;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    public void setCi_telephone_facsimile(Vector ci_telephone_facsimile) {
        this.ci_telephone_facsimile = ci_telephone_facsimile;
    }

    public void setCi_telephone_voice(Vector ci_telephone_voice) {
        this.ci_telephone_voice = ci_telephone_voice;
    }

    public void setDeliveryPoint(Vector deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

    public void setElectronicMailAdress(Vector electronicMailAdress) {
        this.electronicMailAdress = electronicMailAdress;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIndividualName(String individualName) {
        this.individualName = individualName;
    }

    public void setOnLineResource(CI_OnLineResource onLineResource) {
        this.onLineResource = onLineResource;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getContactInstructions() {
        return contactInstructions;
    }

    public void setContactInstructions(String contactInstructions) {
        this.contactInstructions = contactInstructions;
    }

    public String getHoursOfService() {
        return hoursOfService;
    }

    public void setHoursOfService(String hoursOfService) {
        this.hoursOfService = hoursOfService;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static CI_ResponsibleParty getDefaultContact()
    {
        CI_ResponsibleParty contact = new CI_ResponsibleParty();
        contact.setIndividualName("");
        contact.setOrganisationName("");
        contact.setPositionName("");
        contact.setAddressCity("");
        contact.setAddressAdministrativeArea("");
        contact.setAddressPostalCode("");
        contact.setAddressCountry("");
        contact.setIdRole("");
        contact.setRole("");
        contact.setHoursOfService("");
        contact.setContactInstructions("");
        return contact;
    }

    public String toString()
    {
       return getIndividualName()+(getOrganisationName()!=null&&getOrganisationName().length()>0?" ["+getOrganisationName()+"] ":"")+
                                  (getPositionName()!=null&&getPositionName().length()>0?" ["+getPositionName()+"] ":"");
    }
}


