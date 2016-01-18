/**
 * EntidadesSupramunicipalesList.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * GeopistaMunicipiosList.java
 *
 * Created on 7 de octubre de 2007, 20:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.localgis.web.wm.struts.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adolfo
 */
public class EntidadesSupramunicipalesList {
    
    private List entidades;
   
    
    public EntidadesSupramunicipalesList() {
        entidades = new ArrayList();
    }

    public List getEntidades() {
        return entidades;
    }

    public void setEntidades(List municipios) {
        this.entidades = municipios;
    }
    
    public EntidadSupramunicipalBean getMunicipio(int index) {
        return (EntidadSupramunicipalBean) entidades.get(index);
    }
    
    public void setEntidad(EntidadSupramunicipalBean municipio){
        entidades.add(municipio);
    }
    

    
}
