/**
 * LayerOpenlayers.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.openlayers;


public class LayerOpenlayers implements Comparable {

    public static final int ID_LAYER_ORTOFOTO = -1;
    public static final int ID_LAYER_PROVINCIAS = -2;
    
    public static String FORMAT_PNG = "image/png";
    public static String FORMAT_JPEG = "image/jpeg";

    /**
     * Identificador de la capa
     */
    private Integer idLayer;
    /**
     * URL para hacer las peticiones GetMap de la capa
     */
    private String urlGetMapRequests;
    /**
     * URL para hacer las peticiones GetLegendGraphics de la capa
     */
    private String urlGetLegendGraphicsRequests;
    /**
     * URL para hacer las peticiones GetFeatureInfo de la capa
     */
    private String urlGetFeatureInfoRequests;

	/**
     * Nombre de la capa
     */
    private String name;
    /**
     * Nombre interno de la capa (utilizado en las peticiones GetMap)
     */
    private String internalName;
    /**
     *  Nombre con el que se exportará la capa en formato wmc (puede ser distinto del nombre interno de la capa)
     */
    private String wmcName;
    /**
     * Posicion de la layerfamily a la que pertence la capa. 
     */
    private Integer positionLayerFamily;
    /**
     * Posicion absoluta de la capa dentro del mapa sin contar con las capas
     * wms. Las capas se ordenan por posicion: primero por posicion de
     * layerfamily y luego por posicion dentro de la layerfamily
     */
    private Integer position;
    /**
     * Proyeccion de la capa
     */
    private String projection;
    /**
     * Formato en el que se pide la capa
     */
    private String format;
    /**
     * Version para pedir la capa
     */
    private String version;
    /**
     * Si se trata de una capa externa
     */
    private Boolean externalLayer;
    /**
     * Nombre interno de la capa en el map server
     */
    private String internalNameMapServer;
    
    /**Variable booleana que indicará si la capa será visible
     */
    private boolean visible=true;
    
    /**
     * Constructor por defecto
     */
    public LayerOpenlayers() {
    }
    
    /**
     * Devuelve el campo name
     * @return El campo name
     */
    public String getName() {
        if (this.name == null || this.name.equals("")) {
            return internalName;
        } else {
            return name;
        }
    }
    /**
     * Establece el valor del campo name
     * @param name El campo name a establecer
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Devuelve el campo internalName
     * @return El campo internalName
     */
    public String getInternalName() {
        return internalName;
    }
    /**
     * Establece el valor del campo internalName
     * @param internalName El campo internalName a establecer
     */
    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }
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
     * Devuelve el campo projection
     * @return El campo projection
     */
    public String getProjection() {
        return projection;
    }

    /**
     * Establece el valor del campo projection
     * @param projection El campo projection a establecer
     */
    public void setProjection(String projection) {
        this.projection = projection;
    }

    /**
     * Devuelve el campo format
     * @return El campo format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Establece el valor del campo format
     * @param format El campo format a establecer
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Devuelve el campo version
     * @return El campo version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Establece el valor del campo version
     * @param version El campo version a establecer
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Devuelve el campo externalLayer
     * @return El campo externalLayer
     */
    public Boolean getExternalLayer() {
        return externalLayer;
    }

    /**
     * Establece el valor del campo externalLayer
     * @param externalLayer El campo externalLayer a establecer
     */
    public void setExternalLayer(Boolean externalLayer) {
        this.externalLayer = externalLayer;
    }

    /**
     * Devuelve el campo internalNameMapServer (el nombre interno de la capa en el map server para el caso de capas WMS externas)
     * @return El campo internalNameMapServer
     */
    public String getInternalNameMapServer() {
        return internalNameMapServer;
    }

    /**
     * Devuelve el campo idLayer
     * @return El campo idLayer
     */
    public Integer getIdLayer() {
        return idLayer;
    }

    /**
     * Establece el valor del campo idLayer
     * @param idLayer El campo idLayer a establecer
     */
    public void setIdLayer(Integer idLayer) {
        this.idLayer = idLayer;
    }

    /**
     * Establece el valor del campo internalNameMapServer (el nombre interno de la capa en el map server para el caso de capas WMS externas)
     * @param internalNameMapServer El campo internalNameMapServer a establecer
     */
    public void setInternalNameMapServer(String internalNameMapServer) {
        this.internalNameMapServer = internalNameMapServer;
    }
    
    /**
     * Devuelve el campo wmcName
     * @return El campo wmcName
     */
    public String getWmcName() {
        return wmcName;
    }

    /**
     * Establece el valor del campo wmcName
     * @param wmcName El campo wmcName a establecer
     */
    public void setWmcName(String wmcName) {
        this.wmcName = wmcName;
    }

    /**
     * Devuelve el campo urlGetMapRequests
     * @return El campo urlGetMapRequests
     */
    public String getUrlGetMapRequests() {
        return urlGetMapRequests;
    }

    /**
     * Establece el valor del campo urlGetMapRequests
     * @param urlGetMapRequests El campo urlGetMapRequests a establecer
     */
    public void setUrlGetMapRequests(String urlGetMapRequests) {
        this.urlGetMapRequests = urlGetMapRequests;
    }

    /**
     * Devuelve el campo urlGetLegendGraphicsRequests
     * @return El campo urlGetLegendGraphicsRequests
     */
    public String getUrlGetLegendGraphicsRequests() {
        return urlGetLegendGraphicsRequests;
    }

    /**
     * Establece el valor del campo urlGetLegendGraphicsRequests
     * @param urlGetLegendGraphicsRequests El campo urlGetLegendGraphicsRequests a establecer
     */
    public void setUrlGetLegendGraphicsRequests(String urlGetLegendGraphicsRequests) {
        this.urlGetLegendGraphicsRequests = urlGetLegendGraphicsRequests;
    }

    /**
     * Devuelve el campo urlGetFeatureInfoRequests
     * @return El campo urlGetFeatureInfoRequests
     */
    public String getUrlGetFeatureInfoRequests() {
        return urlGetFeatureInfoRequests;
    }

    /**
     * Establece el valor del campo urlGetFeatureInfoRequests
     * @param urlGetFeatureInfoRequests El campo urlGetFeatureInfoRequests a establecer
     */
    public void setUrlGetFeatureInfoRequests(String urlGetFeatureInfoRequests) {
        this.urlGetFeatureInfoRequests = urlGetFeatureInfoRequests;
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

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof LayerOpenlayers)) {
            return false;
        }
        /*
         * Dos capas serán iguales si ambas son externas o internas y ademas tienen el mismo id
         */
        if (this.externalLayer.equals(((LayerOpenlayers)obj).externalLayer) && this.idLayer.equals(((LayerOpenlayers)obj).idLayer)) {
            return true;
        } else {
            return false;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(T)
     */
    public int compareTo(Object another) {
        LayerOpenlayers anotherLayer = (LayerOpenlayers)another;
        /*
         * TODO: Esto esta mal, pero para mantener el comportamiento del editor
         * lo hacemos asi. Las capas se ordenaran primero por layerfamily y
         * dentro de la misma layerfamily por posicion absoluta de la capa
         * dentro del mapa. Esto provoca que si una capa se mete en medio de dos
         * capas que pertenencen a la misma layerfamily, dicha capa quedará
         * despues de la layerfamily en la que se ha insertado.
         */
        int res = anotherLayer.positionLayerFamily.compareTo(positionLayerFamily);
        /*
         * Si ambas capas tienen la misma posicion de layerfamily se ordena por posicion absoluta
         */ 
        if (res == 0) {
            return anotherLayer.position.compareTo(position);
        } else {
            return res;
        }
    }
    
    public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public String toString() {
		return "LayerOpenlayers [name=" + name + ", internalName="
				+ internalName + ", position=" + position + "]";
	}

}
