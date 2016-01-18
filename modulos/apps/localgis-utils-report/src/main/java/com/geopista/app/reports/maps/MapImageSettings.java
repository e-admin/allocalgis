/**
 * MapImageSettings.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.maps;

public class MapImageSettings {

	public static final String DEFAULT_SCALE = MapImageConstants.SCALE_TYPE_AUTOMATIC;

	protected String scale;
	protected int mapSelectionType;
	protected int mapSelectionIdType;
	protected String mapSelectionIdName;
    protected String imageKey;
    protected int mapId;
    protected String capa;
    protected String atributo;
    protected String tabla;
    protected String columna;
    
    protected String layers;
    protected String idUnicoImagen;
    
    private int width;
    private int height;
    
    /**
     * Id del tipo de imagen de mapa
     */
    protected String idMapImageType;

	public MapImageSettings() {
		super();
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;			
	}

	public String getMapSelectionIdName() {
		return mapSelectionIdName;
	}

	public void setMapSelectionIdName(String mapSelectionIdName) {	
		this.mapSelectionIdName = mapSelectionIdName;	
	}

	public int getMapSelectionIdType() {
		return mapSelectionIdType;
	}

	public void setMapSelectionIdType(int mapSelectionIdType) {
		this.mapSelectionIdType = mapSelectionIdType;
	}

	public int getMapSelectionType() {
		return mapSelectionType;
	}

	public void setMapSelectionType(int mapSelectionType) {
		this.mapSelectionType = mapSelectionType;
	}
	
	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

    /**
     * Devuelve el campo idMapImageType
     * @return El campo idMapImageType
     */
    public String getIdMapImageType() {
        return idMapImageType;
    }

    /**
     * Establece el valor del campo idMapImageType
     * @param idMapImageType El campo idMapImageType a establecer
     */
    public void setIdMapImageType(String idMapImageType) {
        this.idMapImageType = idMapImageType;
    }

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getColumna() {
		return columna;
	}

	public void setColumna(String columna) {
		this.columna = columna;
	}

	public String getCapa() {
		return capa;
	}

	public void setCapa(String capa) {
		this.capa = capa;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getAtributo() {
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}

	public String getLayers() {
		return layers;
	}

	public void setLayers(String layers) {
		this.layers = layers;
	}

	public String getIdUnicoImagen() {
		return idUnicoImagen;
	}

	public void setIdUnicoImagen(String idUnicoImagen) {
		this.idUnicoImagen = idUnicoImagen;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}