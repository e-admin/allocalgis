/**
 * IAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.action;

import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CSolicitudLicencia;

/**
 * Interfaz que deben implementar todas las Acciones a ejecutar.
 *
 */
public interface IAction {

    /**
     * Ejecuta la acción.
     * @param rctx Contexto de ejecución de la regla
     * @param attContext Atributos con información extra, utilizados dentro de la ejecución de la regla.
     * @return true si la ejecución termina correctamente, false en caso contrario.
     * @throws ActionException
     */
    public boolean execute(String attContext,  CExpedienteLicencia expLic, int tipoLicencia, CSolicitudLicencia solicitud);
    
    /**
     * Obtiene la información causante de que la ejecución de la acción haya 
     * dado resultado negativo.
     * @return Cadena con información.
     */
    public String getInfo();
    

    /**
     * Obtiene el código de error. Este código es un entero que indentifica al 
     * causante de que la ejecución de la acción de resultado negativo.
     * @return Código de error.
     */
    public int getErrorCode();
}

