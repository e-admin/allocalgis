/**
 * EntidadLocalGis.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import com.localgis.web.core.ConstantesSQL;

public class EntidadLocalGis {

  
  
    private Integer mapidentidad;
    
    public EntidadLocalGis() {
    }
    
    public EntidadLocalGis(Integer mapidentidad) {
        super();
        this.mapidentidad = mapidentidad;
    }



    /**
     * Devuelve el campo mapidgeopista
     * 
     * @return El campo mapidgeopista
     */
    public Integer getMapidentidad() {
        return mapidentidad;
    }

    /**
     * Establece el valor del campo mapidgeopista
     * 
     * @param mapidgeopista
     *            El campo mapidgeopista a establecer
     */
    public void setMapidentidad(Integer mapidentidad) {
        this.mapidentidad = mapidentidad;
    }

    

}