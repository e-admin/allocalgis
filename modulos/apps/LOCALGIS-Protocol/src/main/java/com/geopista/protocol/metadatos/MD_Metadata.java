/**
 * MD_Metadata.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.metadatos;

import java.util.Date;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 20-ago-2004
 * Time: 10:04:02
 */
public class MD_Metadata {
    String fileidentifier;
    String language_id;
    String characterset;
    Date datestamp;
    String metadatastandardname;
    String metadatastandardversion;
    CI_ResponsibleParty responsibleParty;
    String rolecode_id;
    String scopecode_id;
    MD_DataIdentification identificacion;
    Vector formatos;
    Vector onlineresources;
    Vector reference;
    DQ_DataQuality calidad;
    String id_capa;
    String id_acl_capa;

    public MD_Metadata() {
    }

    public Date getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(Date datestamp) {
        this.datestamp = datestamp;
    }

    public String getFileidentifier() {
        return fileidentifier;
    }

    public void setFileidentifier(String fileidentifier) {
        this.fileidentifier = fileidentifier;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getMetadatastandardname() {
        return metadatastandardname;
    }

    public void setMetadatastandardname(String metadatastandardname) {
        this.metadatastandardname = metadatastandardname;
    }

    public String getMetadatastandardversion() {
        return metadatastandardversion;
    }

    public void setMetadatastandardversion(String metadatastandardversion) {
        this.metadatastandardversion = metadatastandardversion;
    }

    public String getCharacterset() {
        return characterset;
    }

    public void setCharacterset(String characterset) {
        this.characterset = characterset;
    }

     public String getRolecode_id() {
        return rolecode_id;
    }

    public void setRolecode_id(String rolecode_id) {
        this.rolecode_id = rolecode_id;
    }

    public MD_DataIdentification getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(MD_DataIdentification identificacion) {
        this.identificacion = identificacion;
    }

    public Vector getFormatos() {
        return formatos;
    }

    public void setFormatos(Vector formatos) {
        this.formatos = formatos;
    }

    public Vector getOnlineresources() {
        return onlineresources;
    }

    public void setOnlineresources(Vector onlineresources) {
        this.onlineresources = onlineresources;
    }

    public DQ_DataQuality getCalidad() {
        return calidad;
    }

    public void setCalidad(DQ_DataQuality calidad) {
        this.calidad = calidad;
    }

    public CI_ResponsibleParty getResponsibleParty() {
        return responsibleParty;
    }

    public void setResponsibleParty(CI_ResponsibleParty responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    public String getScopecode_id() {
        return scopecode_id;
    }

    public void setScopecode_id(String scopecode_id) {
        this.scopecode_id = scopecode_id;
    }

    public Vector getReference() {
        return reference;
    }

    public void setReference(Vector reference) {
        this.reference = reference;
    }

    public String getId_capa() {
        return id_capa;
    }

    public void setId_capa(String id_capa) {
        this.id_capa = id_capa;
    }

    public String getId_acl_capa() {
        return id_acl_capa;
    }

    public void setId_acl_capa(String id_acl_capa) {
        this.id_acl_capa = id_acl_capa;
    }
}
