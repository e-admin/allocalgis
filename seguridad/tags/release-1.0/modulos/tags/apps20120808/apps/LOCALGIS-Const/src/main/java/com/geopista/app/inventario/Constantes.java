package com.geopista.app.inventario;

//import com.geopista.security.GeopistaPrincipal;

import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 11-jul-2006
 * Time: 10:29:45
 * To change this template use File | Settings | File Templates.
 */
public class Constantes {
	
	//LCGIII. Importado de InventarioManagerPlugIn
    public static final String INVENTARIO_SERVLET_NAME="/InventarioServlet";
    
    
    public static String OPERACION_ANNADIR= "ANNADIR";
    public static String OPERACION_MODIFICAR= "MODIFICAR";
    public static String OPERACION_BORRAR= "BORRAR";
    public static String OPERACION_ELIMINAR= "ELIMINAR";
    public static String OPERACION_PREALTA= "TRANSFORMAR_PREALTA";
    public static String OPERACION_ANEXAR= "ANEXAR";
    public static String OPERACION_CONSULTAR= "CONSULTAR";
    public static String OPERACION_FILTRAR= "FILTRAR";
    public static String OPERACION_INFORMES= "INFORMES";
    public static String OPERACION_BUSCAR= "BUSCAR";
    public static String OPERACION_CONSULTA_ANEXOS= "CONSULTA_ANEXOS";
    public static String OPERACION_RECUPERAR= "RECUPERAR";
    public static String OPERACION_ASOCIAR_FEATURES= "ASOCIAR_FEATURES";
    
    public static DateFormat df= new SimpleDateFormat("dd-MM-yyyy");

    public static int IdMunicipio= 34083;
    public static int IdEntidad= -1;
    public static String Provincia= null;
    public static String Municipio= null;
    public static String Entidad= null;

    public static String Locale= "es_ES";

    public static final String idApp= "Inventario";

    /** Usuario de login */
    //public static GeopistaPrincipal principal;

    /** Datos del mapa */
    public static int idMapaInventario= 40;

    public static String PATH_PLANTILLAS_INVENTARIO= "plantillas/inventario";

    public static int formatoPDF= 2;
    public static int formatoPREVIEW= 1;
    public static int formatoHTML= 3;
    public static int formatoPRINT= 4;
    public static int formatoXML= 5;

    public static String[] TIPOS_INVENTARIOS={"total",
    		"creditos_derechos",
    		"derechos_reales",
    		"inmuebles_rusticos",
			"inmuebles_urbanos",
			"historicos_artisticos",
			"otros_muebles",
			"semovientes",
			"valores_mobiliarios",
			"vehiculos",
			"vias_rusticas",
			"vias_urbanas",    			
			"lotes",    			
			"bienes_revertibles"};

    public static String ACTUAL_REMOTE_DIRECTORY="";
    
	
	public static boolean MOSTRAR_PAGINACION=false;
	public static boolean MOSTRAR_MAPA=true;

	public static String INVENTARIO_PARCELAS = "11006";
	public static String INVENTARIO_VIAS = "11007";

}
