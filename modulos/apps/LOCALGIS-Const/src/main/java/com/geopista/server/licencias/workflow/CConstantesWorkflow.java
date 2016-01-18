/**
 * CConstantesWorkflow.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.licencias.workflow;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:02 $
 *          $Name:  $
 *          $RCSfile: CConstantesWorkflow.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CConstantesWorkflow {

    /**Dias por defecto para el silencio administrativo.
     * Lo normal es que este valor sea igual a 30.
     */
	public static long diasSilencioAdministrativo= 30;

	/** Dias para crear un evento indicando que se va a producir
     * un cambio de estado lo normal es que sea 5.
     */
	public static long diasActivacionEvento= 5;

    /** Tiempo en segundos en que se va a revisar si se ha
     * producido un cambio de evento lo normal son 300 segundos.
     */
	public static int segundosWorkflowThread=300;


}
