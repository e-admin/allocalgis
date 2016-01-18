/**
 * GetLegendActionForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.actionsforms;

import org.apache.struts.action.ActionForm;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Formulario de recuperación de la leyenda asociada
 */
public class GetLegendActionForm extends ActionForm {

	/**
	 * Variables
	 */
    private Integer idEntidad;
    private Integer idLayerGeopista;

    /**
     * Devuelve el campo idEntidad
     * @return El campo idEntidad
     */
    public Integer getIdEntidad() {
        return idEntidad;
    }

    /**
     * Establece el valor del campo idEntidad
     * @param idEntidad El campo idEntidad a establecer
     */
    public void setIdEntidad(Integer idEntidad) {
        this.idEntidad = idEntidad;
    }

    /**
     * Devuelve el campo idLayerGeopista
     * @return El campo idLayerGeopista
     */
    public Integer getIdLayerGeopista() {
        return idLayerGeopista;
    }

    /**
     * Establece el valor del campo idLayerGeopista
     * @param idLayerGeopista El campo idLayerGeopista a establecer
     */
    public void setIdLayerGeopista(Integer idLayerGeopista) {
        this.idLayerGeopista = idLayerGeopista;
    }

}
