/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package com.vividsolutions.jump.workbench.datasource;

import java.awt.Component;
import java.util.Collection;

/**
 * UI for picking datasets for a given format. Ideally allows a user to select
 * multiple datasets. Produces 
 * {@link com.vividsolutions.jump.io.datasource.DataSourceQuery DataSourceQueries}
 * each of which encapsulates a
 * query string and the DataSource to run it against.
 * 
 *
 */
public interface DataSourceQueryChooser {
    public Component getComponent();

    /**
     * Puesto que un DataSource permite construir una FeatureCollection a partir
     * de una cadena de texto con su metodo
     *  <i>public FeatureCollection executeQuery(String query) throws Exception;<i/>
     *
     * este metodo devuelve en una collection todos los DataSource asociados a un fichero.
     * ¿Cómo?
     *
     * FileDataSourceQueryChooser, que es el principal, muestra un dialogo FileChooser.
     * El usuario elige una serie de ficheros -shp, p.ej- y pulsa OK. Internamente, el metodo
     * getDataSourceQueries() para cada fichero seleccionado, crea un DataSource de la clase asociada
     * (FileReaderWriterDataSource) y le pasa como propiedad el fichero seleccionado.
     * Seguidamente, envuelve este dataSource con la clase DataSourceQuery
     *

     * Si no creamos un dialogo (getComponent()) que sea capaz de mostrar otros origenes
     * de datos -además del sistema local de ficheros-, podremos personalizar las cadenas
     * 'QUERY' de manera parecida a esta:
     * <ol>
     * <li>oracle:IP|USER|PASSWORD|SCHEMA|TABLA_ATRIBUTOS|SPATIAL_INDEX</li>
     * <li>remoteServer:IP|TIPO_LAYER|ID_LAYER</li>
     * <li>servlet:IP|TIPO:LAYER|ID_LAYER</li>
     * <li>file:basePath</li>
     * </ol>
     *
     * Seguidamente, habría que crear una DataSourceFactory que a partir de esta
     * query cree DataSource personalizados (o que devuelva DataSourceQuery que contengan
     * el DataSource y la Query). Algo asi:
     *
     * DataSource createDataSource(String query){
     *
     *    DataSource solucion = new BDatosDataSource();
     *    solucion.setSchema(SCHEMA);//esquema o usuario de la base de datos
     *    solucion.setTableName(TABLA_ATRIBUTOS);//tabla alfanumerica, que tiene asociada una tabla G_
     *
     *
     *
     * }
     *
     *
     * */
    public Collection getDataSourceQueries();
    /**
     * @return a brief description of the dataset type, suitable for display in a combo box.
     */
    public String toString();
    /**
     * The user has pressed the OK button.
     */
    public boolean isInputValid();
}
