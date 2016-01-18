package com.geopista.protocol.inventario;

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
    public static final String PATRON_INMUEBLES_URBANOS= "2";
    public static final String PATRON_INMUEBLES_RUSTICOS= "3";
    public static final String PATRON_MUEBLES_HISTORICOART= "7";
    public static final String PATRON_DERECHOS_REALES= "6";
    public static final String PATRON_VALOR_MOBILIARIO= "9";
    public static final String PATRON_BIENES_MUEBLES= "13";
    public static final String PATRON_SEMOVIENTES= "12";
    public static final String PATRON_VEHICULOS= "11";
    public static final String PATRON_CREDITOS_DERECHOS_PERSONALES= "10";
    public static final String PATRON_VIAS_PUBLICAS_URBANAS= "4";
    public static final String PATRON_VIAS_PUBLICAS_RUSTICAS= "5";
    /** determina los bienes que son revertibles */
    public static final String PATRON_CEDIDO= "3";

    public static final String SUPERPATRON_BIENES= "1";
    public static final String SUPERPATRON_REVERTIBLES= "2";
    public static final String SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO= "3";
    public static final String SUPERPATRON_LOTES= "4";


    public static final int ACTION_GET_INMUEBLES= 0;
    public static final int ACTION_INSERT_INVENTARIO= 1;
    public static final int ACTION_UPDATE_INVENTARIO= 2;
    public static final int ACTION_GET_CUENTAS_CONTABLES= 3;
    public static final int ACTION_GET_CUENTAS_AMORTIZACION= 4;
    public static final int ACTION_GET_COMPANNIAS_SEGUROS= 5;
    public static final int ACTION_BORRAR_INVENTARIO= 6;
    public static final int ACTION_ELIMINAR_INVENTARIO= 7;
    public static final int ACTION_ANEXAR_INVENTARIO= 8;
    public static final int ACTION_RECUPERAR_INVENTARIO= 28;
    public static final int ACTION_GET_MUEBLES= 9;
    public static final int ACTION_GET_DERECHOS_REALES=10;
    public static final int ACTION_BLOQUEAR_INVENTARIO=11;
    public static final int ACTION_GET_VALORES_MOBILIARIOS=12;
    public static final int ACTION_GET_BLOQUEADO_INVENTARIO=13;
    public static final int ACTION_GET_CREDITOS_DERECHOS=14;
    public static final int ACTION_GET_SEMOVIENTES=15;
    public static final int ACTION_GET_VIAS=16;
    public static final int ACTION_GET_VEHICULOS=17;
    public static final int ACTION_GET_PLANTILLAS=18;
    
    public static final int ACTION_GET_INMUEBLE= 19;
    public static final int ACTION_GET_MUEBLE= 20;
    public static final int ACTION_GET_DERECHO_REAL=21;
    public static final int ACTION_GET_VALOR_MOBILIARIO=22;
    public static final int ACTION_GET_CREDITO_DERECHO=23;
    public static final int ACTION_GET_SEMOVIENTE=24;
    public static final int ACTION_GET_VIA=25;
    public static final int ACTION_GET_VEHICULO=26;
    public static final int ACTION_GET_REFERENCIAS_CATASTRALES=27;
    public static final int ACTION_GET_HORA=29;
    public static final int ACTION_GET_BIENES_REVERTIBLES=30;
    public static final int ACTION_UPDATE_BIEN_REVERTIBLE=31;
    public static final int ACTION_GET_LOTES=32;
    public static final int ACTION_UPDATE_LOTES=33;
    public static final int ACTION_GET_BIENES=34;
    public static final int ACTION_ELIMINAR_TODO_INVENTARIO= 35;
	    
	public static final int ACTION_INSERT_INVENTARIO_SICALWIN = 36;
    
    public static final int ACTION_UPDATE_LAYER_FEATURE_BIENES=37;
    public static final int ACTION_GET_DATE=38;
	public static final int ACTION_GET_BIENES_PREALTA = 39;
	public static final int ACTION_INSERT_BIEN_PREALTA = 40;
	public static final int ACTION_UPDATE_DATOS_VALORACION = 41;
	public static final int ACTION_GET_VALOR_METRO= 42;
	public static final int ACTION_GET_EIEL_SIN_ASOCIAR=43;
	public static final int ACTION_GET_NUMEROS_INVENTARIO=44;
	public static final int ACTION_INSERT_INTEG_EIEL_INVENTARIO = 45;	
    //Eliminacion total sin posibilidad de poder recuperar posteriormente
    //el historico.
    public static final int ACTION_ELIMINAR_NORECOVER_INVENTARIO= 46;
	public static final int ACTION_GET_EIEL_ASOCIADOS = 47;	
	public static final int ACTION_GET_COMPROBAR_INTEG_EIEL = 48;
	public static final int ACTION_GET_ELEMENTOS_COMPROBAR_INTEG_EIEL = 49;
	public static final int ACTION_GET_BIENES_AMORTIZABLES=50;
	public static final int ACTION_INSERT_BIENES_AMORTIZABLES =51;
	public static final int ACTION_GET_BIENES_AMORTIZADOS = 52;
	
    public static final String KEY_PATRON= "patron";
    public static final String KEY_ARRAYLIST_IDFEATURES= "arrayList.idFeatures";
    public static final String KEY_ARRAYLIST_IDLAYERS= "arrayList.idLayers";
    public static final String KEY_INVENTARIO= "inventario";
    public static final String KEY_VALOR_BLOQUEO_INVENTARIO= "bloqueo";
    public static final String KEY_SUPERPATRON= "superpatron";
    public static final String KEY_CADENA_BUSQUEDA= "cadenaBusqueda";
    public static final String KEY_FILTRO_BUSQUEDA= "filtroBusqueda";
    public static final String KEY_PATH_PLANTILLAS= "pathPlantillas";
    public static final String KEY_IDINMUEBLE= "idInmueble";
    public static final String KEY_IDREVISION= "idRevision";
    public static final String KEY_IDREVISIONEXPIRADA= "idRevisionExpirada";
       public static final String KEY_FECHA_VERSION = "fechaVersion";
    public static final String KEY_ID_MUNICIPIO = "idMunicipio";
    public static final String KEY_ID_ENTIDAD = "idEntidad";
    public static final String KEY_EPIG_INVENTARIO = "epigInventario";
    public static final String ASSOCIATE_FEATURES_BIENES = "associategeometrybien";
	public static final String KEY_TOLERANCIA = "tolerancia";
	public static final String KEY_SUPERF = "superficie";
	public static final String KEY_ID_LAYER = "idLayer";
	public static final Object KEY_PREALTA = "bienprealta";
	public static final Object KEY_INVENTARIO_EIEL = "inventarioEIEL";
	public static final Object KEY_IDBIEN = "idBien";
	public static final Object KEY_BIEN = "bien";

	
    public static String pattern= "dd-MM-yyyy";
    public static String fechaVersion="";


	//constantes parar relacionar inventario de patrimonio
	public static List<String> SUPERPATRONES;
	public static HashMap<String, Integer> MULTIACCIONES_PATRONES; //para obtener varios
	public static HashMap<String, Integer> ACCION_PATRONES; //para obtener uno
	public static HashMap<String, Class> CLASE_PATRONES; //obtener la clase del bien
	public static HashMap<String, String> SUPERPATRONES_NOMBRE; //obtener la clase del bien
	
	static {
		SUPERPATRONES = new ArrayList<String>();
		SUPERPATRONES.add(Const.SUPERPATRON_BIENES);
		SUPERPATRONES.add(Const.SUPERPATRON_REVERTIBLES);
		SUPERPATRONES.add(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO);

		MULTIACCIONES_PATRONES = new HashMap<String, Integer>();
		MULTIACCIONES_PATRONES.put(Const.PATRON_INMUEBLES_URBANOS, Const.ACTION_GET_INMUEBLES);
		MULTIACCIONES_PATRONES.put(Const.PATRON_INMUEBLES_RUSTICOS, Const.ACTION_GET_INMUEBLES);
		MULTIACCIONES_PATRONES.put(Const.PATRON_MUEBLES_HISTORICOART, Const.ACTION_GET_MUEBLES);
		MULTIACCIONES_PATRONES.put(Const.PATRON_BIENES_MUEBLES, Const.ACTION_GET_MUEBLES);
		MULTIACCIONES_PATRONES.put(Const.PATRON_DERECHOS_REALES, Const.ACTION_GET_DERECHOS_REALES);
		MULTIACCIONES_PATRONES.put(Const.PATRON_VALOR_MOBILIARIO, Const.ACTION_GET_VALORES_MOBILIARIOS);
		MULTIACCIONES_PATRONES.put(Const.PATRON_CREDITOS_DERECHOS_PERSONALES, Const.ACTION_GET_CREDITOS_DERECHOS);
		MULTIACCIONES_PATRONES.put(Const.PATRON_SEMOVIENTES, Const.ACTION_GET_SEMOVIENTES);
		MULTIACCIONES_PATRONES.put(Const.PATRON_VIAS_PUBLICAS_URBANAS, Const.ACTION_GET_VIAS);
		MULTIACCIONES_PATRONES.put(Const.PATRON_VIAS_PUBLICAS_RUSTICAS, Const.ACTION_GET_VIAS);
		MULTIACCIONES_PATRONES.put(Const.PATRON_VEHICULOS, Const.ACTION_GET_VEHICULOS);
		
		ACCION_PATRONES = new HashMap<String, Integer>();
		ACCION_PATRONES.put(Const.PATRON_INMUEBLES_URBANOS, Const.ACTION_GET_INMUEBLE);
		ACCION_PATRONES.put(Const.PATRON_INMUEBLES_RUSTICOS, Const.ACTION_GET_INMUEBLE);
		ACCION_PATRONES.put(Const.PATRON_MUEBLES_HISTORICOART, Const.ACTION_GET_MUEBLE);
		ACCION_PATRONES.put(Const.PATRON_BIENES_MUEBLES, Const.ACTION_GET_MUEBLE);
		ACCION_PATRONES.put(Const.PATRON_DERECHOS_REALES, Const.ACTION_GET_DERECHO_REAL);
		ACCION_PATRONES.put(Const.PATRON_VALOR_MOBILIARIO, Const.ACTION_GET_VALOR_MOBILIARIO);
		ACCION_PATRONES.put(Const.PATRON_CREDITOS_DERECHOS_PERSONALES, Const.ACTION_GET_CREDITO_DERECHO);
		ACCION_PATRONES.put(Const.PATRON_SEMOVIENTES, Const.ACTION_GET_SEMOVIENTE);
		ACCION_PATRONES.put(Const.PATRON_VIAS_PUBLICAS_URBANAS, Const.ACTION_GET_VIA);
		ACCION_PATRONES.put(Const.PATRON_VIAS_PUBLICAS_RUSTICAS, Const.ACTION_GET_VIA);
		ACCION_PATRONES.put(Const.PATRON_VEHICULOS, Const.ACTION_GET_VEHICULO);
		
		CLASE_PATRONES = new HashMap<String, Class>();
		CLASE_PATRONES.put(Const.PATRON_INMUEBLES_URBANOS, InmuebleBean.class);
		CLASE_PATRONES.put(Const.PATRON_INMUEBLES_RUSTICOS, InmuebleBean.class);
		CLASE_PATRONES.put(Const.PATRON_MUEBLES_HISTORICOART, MuebleBean.class);
		CLASE_PATRONES.put(Const.PATRON_BIENES_MUEBLES, MuebleBean.class);
		CLASE_PATRONES.put(Const.PATRON_DERECHOS_REALES, DerechoRealBean.class);
		CLASE_PATRONES.put(Const.PATRON_VALOR_MOBILIARIO, ValorMobiliarioBean.class);
		CLASE_PATRONES.put(Const.PATRON_CREDITOS_DERECHOS_PERSONALES, CreditoDerechoBean.class);
		CLASE_PATRONES.put(Const.PATRON_SEMOVIENTES, SemovienteBean.class);
		CLASE_PATRONES.put(Const.PATRON_VIAS_PUBLICAS_URBANAS, ViaBean.class);
		CLASE_PATRONES.put(Const.PATRON_VIAS_PUBLICAS_RUSTICAS, ViaBean.class);
		CLASE_PATRONES.put(Const.PATRON_VEHICULOS, VehiculoBean.class);
		
		SUPERPATRONES_NOMBRE = new HashMap<String, String>();
		SUPERPATRONES_NOMBRE.put(Const.SUPERPATRON_BIENES, "Bienes");
		SUPERPATRONES_NOMBRE.put(Const.SUPERPATRON_REVERTIBLES, "Bienes revertibles");
		SUPERPATRONES_NOMBRE.put(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO, "Patrimonio municipal");
	}
	
	public static final int POSICION_INMUEBLES_URBANOS= 1;
    public static final int POSICION_INMUEBLES_RUSTICOS= 2;
    public static final int POSICION_VIAS_PUBLICAS_URBANAS= 3;
    public static final int POSICION_VIAS_PUBLICAS_RUSTICAS= 4;
    public static final int POSICION_DERECHOS_REALES= 5;
    public static final int POSICION_MUEBLES_HISTORICOART= 6;
    public static final int POSICION_VALOR_MOBILIARIO= 7;
    public static final int POSICION_VEHICULOS= 8;
    public static final int POSICION_SEMOVIENTES= 9;
    public static final int POSICION_PATRON_MOBILIARIO= 10;
    public static final int POSICION_BIENES_MUEBLES= 11;
    public static final int POSICION_CREDITOS_DERECHOS_PERSONALES= 12;
    
	public static final String KEY_VALOR_URBANO = "valor_urbano";
	public static final String KEY_VALOR_RUSTICO = "valor_rustico";
	public static final String KEY_ANIO_AMORTIZACION = "anioAmortizacion";
	public static final String INMUEBLES_URBANOS="inmuebles_urbanos";
	public static final String INMUEBLES_RUSTICOS="inmuebles_rusticos";
	public static final String MUEBLES="muebles";
	public static final String VEHICULOS="vehiculo";
	public static final String LAST_IMPORT_DIRECTORY_AMORT = null;
	
}
