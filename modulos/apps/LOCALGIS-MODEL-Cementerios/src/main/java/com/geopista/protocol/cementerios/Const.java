/**
 * Const.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 14-jul-2006
 * Time: 15:22:26
 * To change this template use File | Settings | File Templates.
 */
public class Const {
    
//    /** determina los bienes que son revertibles */

    public static final String SUPERPATRON_GELEMENTOS= "GE";
		public static final String PATRON_BLOQUE= "GE-1";
		public static final String PATRON_UENTERRAMIENTO= "GE-2";
	
    public static final String SUPERPATRON_GPROPIEDAD= "GP";
	    public static final String PATRON_TITULAR= "GP-1";
	    public static final String PATRON_CONCESION= "GP-2";
	    public static final String PATRON_TARIFASPROP= "GP-4";
    
    public static final String SUPERPATRON_GHISTORICOS= "H";
	    public static final String PATRON_HISTORICO_DIFUNTO= "H-1";
	    public static final String PATRON_HISTORICO_PROPIEDAD= "H-2";

    public static final String SUPERPATRON_GDIFUNTOS= "GD";
		public static final String PATRON_DEFUNCION= "GD-1";
		public static final String PATRON_INHUMACION="GD-2";
		public static final String PATRON_EXHUMACION= "GD-3";
		public static final String PATRON_TARIFASDEF= "GD-4";
	
    public static final String SUPERPATRON_GINTERVENCIONES= "GI";
	    public static final String PATRON_INTERVENCION= "GI-1";
	    
//	 public static final String SUPERPATRON_GELEMENTOS= "5";
//		public static final String PATRON_BLOQUE= "1";
//		public static final String PATRON_UENTERRAMIENTO= "2";
//	
// public static final String SUPERPATRON_GPROPIEDAD= "4";
//	    public static final String PATRON_TITULAR= "1";
//	    public static final String PATRON_CONCESION= "2";
//	    public static final String PATRON_TARIFASPROP= "4";
// 
// public static final String SUPERPATRON_GHISTORICOS= "3";
//	    public static final String PATRON_HISTORICO_DIFUNTO= "1";
//	    public static final String PATRON_HISTORICO_PROPIEDAD= "2";
//
// public static final String SUPERPATRON_GDIFUNTOS= "2";
//		public static final String PATRON_DEFUNCION= "1";
//		public static final String PATRON_INHUMACION="2";
//		public static final String PATRON_EXHUMACION= "3";
//		public static final String PATRON_TARIFASDEF= "4";
//	
// public static final String SUPERPATRON_GINTERVENCIONES= "1";
//	    public static final String PATRON_INTERVENCION= "1";	
//	    public static final String PATRON_INTERVENCIONPERIODICA= "2";

    public static final int ACTION_GET_UNIDADES_ENTE= 0;
    public static final int ACTION_INSERT_UNIDADES_ENTE= 1;
    public static final int ACTION_UPDATE_UNIDADES_ENTE= 2;
    
    public static final int ACTION_GET_UNIDAD_ENTE= 3;
    

    //Generico..
    public static final int ACTION_INSERT= 4;
    public static final int ACTION_UPDATE= 5;
    public static final int ACTION_DELETE= 6;
    
    public static final int ACTION_GET_PLANTILLAS=7;
    public static final int ACTION_GET_BLOQUEADO=8;
    public static final int ACTION_BLOQUEAR_UNIDAD_ENTE=9;
    public static final int ACTION_ELIMINAR_UNIDAD_ENTE=10;
    
    public static final int ACTION_GET_CONCESIONES= 11;
    public static final int ACTION_GET_INVENCIONES_PUN= 12;
    
    public static final int ACTION_GET_ALL= 13;
    public static final int ACTION_GET_ELEM= 14;
    public static final int ACTION_GET_PLAZA= 15;
    
    public static final int ACTION_FILTER_ELEM= 16;
    public static final int ACTION_FIND_ELEM= 17;
    
    public static final int ACTION_GET_CAMPOS_ELEM= 18;
    public static final int ACTION_GET_HISTORICO= 19;
    public static final int ACTION_GET_CEMENTERIO= 20;
    
    public static final int ACTION_GET_HORA=29;

    
    public static final String KEY_PATRON= "patron";
    public static final String KEY_ARRAYLIST_IDFEATURES= "arrayList.idFeatures";
    public static final String KEY_ARRAYLIST_IDLAYERS= "arrayList.idLayers";
    public static final String KEY_CEMENTERIO= "CEMENTERIO";
    public static final String KEY_VALOR_BLOQUEO_CEMENTERIO= "bloqueo";
    public static final String KEY_SUPERPATRON= "superpatron";
    public static final String KEY_CADENA_BUSQUEDA= "cadenaBusqueda";
    public static final String KEY_FILTRO_BUSQUEDA= "filtroBusqueda";
    public static final String KEY_PATH_PLANTILLAS= "pathPlantillas";
    
    public static final String KEY_IDELEMENTOCEMENTERIO= "idElementoCementerio";
    public static final String KEY_IDCEMENTERIO= "idCEMENTERIO";
    public static final String KEY_ID_DIFUNTO= "idDifunto";
    
    public static final String KEY_FECHA_VERSION = "fechaVersion";
    public static final String KEY_ID_MUNICIPIO = "idMunicipio";

    public static String pattern= "dd-MM-yyyy";
    public static DateFormat df= new SimpleDateFormat(Const.pattern);
    public static String fechaVersion="";

	//constantes parar relacionar CEMENTERIO 
	public static List<String> SUPERPATRONES;
	public static HashMap<String, Integer> MULTIACCIONES_PATRONES; //para obtener varios
	public static HashMap<String, Integer> ACCION_PATRONES; //para obtener uno
	public static HashMap<String, Class> CLASE_PATRONES; //obtener la clase del bien
	public static HashMap<String, String> SUPERPATRONES_NOMBRE; //obtener la clase del bien
	
	static {
		SUPERPATRONES = new ArrayList<String>();
		SUPERPATRONES.add(Const.SUPERPATRON_GELEMENTOS);
		SUPERPATRONES.add(Const.SUPERPATRON_GDIFUNTOS);
		SUPERPATRONES.add(Const.SUPERPATRON_GPROPIEDAD);
		SUPERPATRONES.add(Const.SUPERPATRON_GHISTORICOS);
		SUPERPATRONES.add(Const.SUPERPATRON_GINTERVENCIONES);
		
		MULTIACCIONES_PATRONES = new HashMap<String, Integer>();
		MULTIACCIONES_PATRONES.put(Const.PATRON_UENTERRAMIENTO, Const.ACTION_GET_UNIDADES_ENTE);
		
		ACCION_PATRONES = new HashMap<String, Integer>();
		ACCION_PATRONES.put(Const.PATRON_UENTERRAMIENTO, Const.ACTION_GET_UNIDAD_ENTE);
		
		
		CLASE_PATRONES = new HashMap<String, Class>();
		CLASE_PATRONES.put(Const.PATRON_UENTERRAMIENTO, UnidadEnterramientoBean.class);
		
		
		SUPERPATRONES_NOMBRE = new HashMap<String, String>();
		SUPERPATRONES_NOMBRE.put(Const.SUPERPATRON_GELEMENTOS, "Gestion Elementos");
		SUPERPATRONES_NOMBRE.put(Const.SUPERPATRON_GDIFUNTOS, "Gestion Difuntos");
		SUPERPATRONES_NOMBRE.put(Const.SUPERPATRON_GPROPIEDAD, "Gestion Propiedad");
		SUPERPATRONES_NOMBRE.put(Const.SUPERPATRON_GHISTORICOS, "Gestion Historicos");
		SUPERPATRONES_NOMBRE.put(Const.SUPERPATRON_GINTERVENCIONES, "Gestion Intervenciones");
	}
	
	/*Estados posibles de la ocupacion de una plaza*/
	 public static final int Estado_libre = 0;
	 public static final int Estado_Completa = 1;
	 public static final String EstlibreStr = "Libre";
	 public static final String EstcompletaStr = "Completa";
	 
	 public static final int Estado_Activa = 0;
	 public static final int Estado_Caducada = 1;
	 public static final String EstActivaStr = "Activa";
	 public static final String EstCaducadaStr = "Caducada";
	 
	    
	public static final int unidadEnterramiento=0;
    public static final int bloque=1;
    public static final int concesion=2;
    public static final int difunto=3;
	public static final int contenedor=4;
	public static final int inhumacion=5;
	public static final int exhumacion=6;
	public static final int servicios=7;

    public static String TARIFA_GPROPIEDAD="0";
    public static String TARIFA_GDIFUNTOS="1";
    
}

