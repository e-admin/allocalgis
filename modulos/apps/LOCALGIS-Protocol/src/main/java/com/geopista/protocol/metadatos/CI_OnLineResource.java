/**
 * CI_OnLineResource.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.metadatos;

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 20-jul-2004
 * Time: 15:03:21
 */
public class CI_OnLineResource {
    String id;
    String linkage;
    String protocol;
    String aplicationProfile;
    String name;
    String description;
    String idOnLineFunctionCode;

    public CI_OnLineResource() {
    }

    public CI_OnLineResource(String linkage, String idOnLineFunctionCode) {
        this.linkage = linkage;
        this.idOnLineFunctionCode = idOnLineFunctionCode;
    }

    public CI_OnLineResource(String aplicationProfile, String description, String id, String linkage, String name, String idOnLineFunctionCode, String protocol) {
        this.aplicationProfile = aplicationProfile;
        this.description = description;
        this.id = id;
        this.linkage = linkage;
        this.name = name;
        this.idOnLineFunctionCode = idOnLineFunctionCode;
        this.protocol = protocol;
    }

    public String getAplicationProfile() {
        return aplicationProfile;
    }

    public void setAplicationProfile(String aplicationProfile) {
        this.aplicationProfile = aplicationProfile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkage() {
        return linkage;
    }

    public void setLinkage(String linkage) {
        this.linkage = linkage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdOnLineFunctionCode() {
        return this.idOnLineFunctionCode;
    }

    public void setIdOnLineFunctionCode(String idOnLineFunctionCode) {
        this.idOnLineFunctionCode = idOnLineFunctionCode;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public String toString()
    {
        return linkage;
    }
}
