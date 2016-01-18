/**
 * IMultilingue.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.gestorcatastral;


/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 12-ene-2007
 * Time: 14:00:45
 * To change this template use File | Settings | File Templates.
 */

/**
 * Interfaz que implementan los elementos de la gui para cambiar sus etiquetas dinamicamente cuando el usuario cambie
 * el idioma de la aplicacion.
 */

public interface IMultilingue
{
    /**
     * Metodo que deben implementar para cambiar los label que poseen las interfaces.
     */
    public void renombrarComponentes();
}
