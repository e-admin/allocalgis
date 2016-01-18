/**
 * LocalgisLayerExt.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import com.localgis.web.core.ConstantesSQL;

public class LocalgisLayerExt extends LocalgisLayer {
    
    private Integer position;

    private Integer positionLayerFamily;

    private String nametranslated;

    private Short customlegend;
    
    private Boolean visible;
    
    /**
     * Devuelve el campo position
     * @return El campo position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Establece el valor del campo position
     * @param position El campo position a establecer
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * Devuelve el campo nametranslated
     * @return El campo nametranslated
     */
    public String getNametranslated() {
        return nametranslated;
    }

    /**
     * Establece el valor del campo nametranslated
     * @param nametranslated El campo nametranslated a establecer
     */
    public void setNametranslated(String nametranslated) {
        this.nametranslated = nametranslated;
    }

    /**
     * Devuelve el campo customlegend
     * @return El campo customlegend
     */
    public Short getCustomlegend() {
        return customlegend;
    }

    /**
     * Establece el valor del campo customlegend
     * @param customlegend El campo customlegend a establecer
     */
    public void setCustomlegend(Short customlegend) {
        this.customlegend = customlegend;
    }
    
    /**
     * Devuelve el campo customlegend
     * @return El campo customlegend
     */
    public Boolean isCustomlegend() {
        return new Boolean(customlegend != null && customlegend.equals(ConstantesSQL.TRUE));
    }

    /**
     * Devuelve el campo positionLayerFamily
     * @return El campo positionLayerFamily
     */
    public Integer getPositionLayerFamily() {
        return positionLayerFamily;
    }

    /**
     * Establece el valor del campo positionLayerFamily
     * @param positionLayerFamily El campo positionLayerFamily a establecer
     */
    public void setPositionLayerFamily(Integer positionLayerFamily) {
        this.positionLayerFamily = positionLayerFamily;
    }

    public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
}
