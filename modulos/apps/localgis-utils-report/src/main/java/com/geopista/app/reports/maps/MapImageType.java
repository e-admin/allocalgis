/**
 * MapImageType.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.maps;

/**
 * Clase que encapsula un tipo de imagen de mapa que se permite mostrar en los
 * informes, por ejemplo, parcelas, numeros de policia, etc.
 * 
 * @author albegarcia
 * 
 */
public class MapImageType {

    private String id;
    private String table;
    private String column;
    private String description;
    private String layers;
    private String style;
    
    /**
     * Constructor por defecto
     */
    public MapImageType() {
    }

    /**
     * Constructor a partir de los atributos
     * @param id Id del tipo de imagen de mapa
     * @param table Tabla
     * @param column Columna de la tabla
     * @param description Descripcion
     * @param layers Capas a mostrar
     * @param style Estilo para mostrar las capas
     */
    public MapImageType(String id, String table, String column, String description, String layers, String style) {
        this.id = id;
        this.table = table;
        this.column = column;
        this.description = description;
        this.layers = layers;
        this.style = style;
    }
    
    
    /**
     * Devuelve el campo id
     * @return El campo id
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el valor del campo id
     * @param id El campo id a establecer
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el campo table
     * @return El campo table
     */
    public String getTable() {
        return table;
    }

    /**
     * Establece el valor del campo table
     * @param table El campo table a establecer
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * Devuelve el campo column
     * @return El campo column
     */
    public String getColumn() {
        return column;
    }

    /**
     * Establece el valor del campo column
     * @param column El campo column a establecer
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * Devuelve el campo description
     * @return El campo description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece el valor del campo description
     * @param description El campo description a establecer
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Devuelve el campo layers
     * @return El campo layers
     */
    public String getLayers() {
        return layers;
    }

    /**
     * Establece el valor del campo layers
     * @param layers El campo layers a establecer
     */
    public void setLayers(String layers) {
        this.layers = layers;
    }

    /**
     * Devuelve el campo style
     * @return El campo style
     */
    public String getStyle() {
        return style;
    }

    /**
     * Establece el valor del campo style
     * @param style El campo style a establecer
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Devolvemos la descripcion del elemento como su representacion en String
     */
    public String toString() {
        return description;
    }
}
