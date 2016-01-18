/**
 * CConstantesPaths.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

import java.io.File;

import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;


/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CConstantesPaths.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CConstantesPaths {

	public static String IMAGE_PATH="img/";
    public static String IMAGE_MAPS_PATH="img/maps/";
	public static String ANEXOS_PATH="anexos/";
	public static String PLANTILLAS_PATH= ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"licencias"+File.separator;
    public static String PATH_PLANTILLAS_ACTIVIDAD= ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"actividad"+File.separator;
    public static String PATH_PLANTILLAS_OCUPACION= ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"ocupacion"+File.separator;

	// Formato del nombre de las plantillas para los informes
	public static String PLANTILLAS_STARTS_WITH= "plantilla";

}
