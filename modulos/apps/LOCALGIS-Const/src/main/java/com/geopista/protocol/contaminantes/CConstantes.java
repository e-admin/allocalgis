/**
 * CConstantes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.contaminantes;

import java.io.File;

import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;



/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 19-oct-2004
 * Time: 10:22:08
 * To change this template use File | Settings | File Templates.
 */
public class CConstantes {
    public static String PLANTILLAS_PATH_ARBOLADO=ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"contaminantes"+File.separator+"arbolado"+File.separator;
    public static String PLANTILLAS_PATH_VERTEDEROS=ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"contaminantes"+File.separator+"vertederos"+File.separator;
    public static String PLANTILLAS_PATH_ACTIVIDADES=ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"contaminantes"+File.separator+"actividades"+File.separator;
    public static String IMAGE_PATH="img/";

    // Formato del nombre de las plantillas para los informes
    public static String PLANTILLAS_STARTS_WITH= "plantilla";

    //** Anexos */
    public static String anexosActividadesContaminantesUrl = "http://localhost:54321/anexos/contaminantes/";
    
   //Constantes comando BdD
  	public static final int CMD_GET_SEARCHED_ADDRESSES_BY_NUMPOLICIA = 201;
  	
    //******************************************
    //** Operaciones sobre los anexos
    //****************************************
    public static final int CMD_ANEXO_ADDED = 201;
    public static final int CMD_ANEXO_DELETED = 202;
    //******************************************
    //** Operaciones sobre las inspecciones
    //****************************************
    public static final int CMD_INSPECCION_ADDED = 201;
    public static final int CMD_INSPECCION_DELETED = 202;
    public static final int CMD_INSPECCION_MODIFIED = 203;

}
