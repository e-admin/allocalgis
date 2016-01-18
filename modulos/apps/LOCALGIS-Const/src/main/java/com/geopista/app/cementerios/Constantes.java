/**
 * Constantes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;


public class Constantes {
    public static String OPERACION_ANNADIR= "ANNADIR";
    public static String OPERACION_MODIFICAR= "MODIFICAR";
    public static String OPERACION_BORRAR= "BORRAR";
    public static String OPERACION_ELIMINAR= "ELIMINAR";
    public static String OPERACION_ANEXAR= "ANEXAR";
    public static String OPERACION_CONSULTAR= "CONSULTAR";
    public static String OPERACION_FILTRAR= "FILTRAR";
    public static String OPERACION_INFORMES= "INFORMES";
    public static String OPERACION_BUSCAR= "BUSCAR";
    public static String OPERACION_CONSULTA_ANEXOS= "CONSULTA_ANEXOS";
    public static String OPERACION_RECUPERAR= "RECUPERAR";
    
    public static String OPERACION_ANNADIR_DIFUNTO= "ANNDIR_DIFUNTO";
    
    public static int TRUE=0;
    public static int FALSE=1;
    
    public static int TARIFA_GPROPIEDAD=0;
    public static int TARIFA_GDIFUNTOS=1;
    
    
    public static final String ESTADO_CIVIL_SOLTERO= "soltero";
    public static final String ESTADO_CIVIL_CASADO= "casado";
    public static final String SEXO_MUJER= "mujer";
    public static final String SEXO_HOMBRE= "hombre";
    public static final String COMBO_CEMENT = "comboCementerios";
    public static final String ID_CEMENTERIO = "idCementerio";
    
    
    public static int NUM_MAX_PLAZAS = 6;
    
    public static DateFormat df= new SimpleDateFormat("dd-MM-yyyy");

    public static int IdMunicipio= 34083;
    public static int IdEntidad= -1;
    public static String Provincia= null;
    public static String Municipio= null;
    public static String Entidad= null;

    public static String Locale= "es_ES";

    public static final String idApp= "Cementerios";

    /** Datos del mapa */
    public static int idMapaCementerios= 5;

    public static String PATH_PLANTILLAS_INVENTARIO= ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"cementerios";
    public static int formatoPDF= 2;
    public static int formatoPREVIEW= 1;
    public static int formatoHTML= 3;
    public static int formatoPRINT= 4;
    public static int formatoXML= 5;

    
}
