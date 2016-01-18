package com.geopista.app.plantillas;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import com.geopista.security.GeopistaPrincipal;

/**
 * Clase utilizada por el cliente y el servidor que define las constantes de las
 * acciones que el cliente desea hacer en la parte servidora. En la parte
 * servidora se comparara la accion obtenida y se realizara la accion deseada.
 * El resto de constantes son para permitir obtener los objetos de la hash
 * params en el envio de la peticion al servidor o utilzadas en el cliente y
 * servidor.
 * */

public class ConstantesLocalGISPlantillas {
	/** Usuario de login */
	
	public static DateFormat df= new SimpleDateFormat("dd-MM-yyyy");
	public static GeopistaPrincipal principal;
	public static Hashtable permisos = new Hashtable();

	// Acciones sobre BD.

	public static LocalGISPlantillasClient clienteLocalGISPlantillas = null;

	public static String loginEIEL = "http://localhost:54321/eiel/";
	public static String servletEIELUrl = "http://localhost:54321/eiel/EIELServlet";

	public static final String idApp = "EIEL";
	//
	public static String Locale = "es_ES";
	public static String LocalCastellano = "es_ES";
	//
	public static int IdEntidad = -1;
	public static int IdMunicipio = 34083;
	public static String Provincia = null;
	public static String Municipio = null;

	public static String idProvincia = null;
	public static String idMunicipio = null;
	public static String idEntidad = null;

	public static String helpSetHomeID = "EIELIntro";

	public static final String NO_INICIALIZADO = "NoInicializado";

	public static final String FILTRO = "Filtro";
	public static final String PATRON = "Patron";
	public static final String PATRONES = "Patrones";
	public static final String MUNICIPIO_SELECCIONADO = "MunicipioSeleccionado";
	public static final String IDOPERACION = "IdOperacion";
	public static final String NOMBREMAPA = "NombreMapa";
	public static final String IDMAPA = "IdMapa";
	public static final String ID_DOMAIN = "IdDomain";
	public static final String OBJECT = "Objeto";
	public static final String KEY_VALOR_BLOQUEO_ELEMENTO = "lock";
	public static final String TIPO_ELEMENTO = "tipo";
	public static final String FEATURES = "IdFeatures";
	public static final String ID_LAYER = "IdLayer";
	public static final String ID_ENTIDAD = "IdEntidad";


	
	
	
	public static final String DOMAIN_NAME = "EIEL";

	public static DecimalFormat decimalFormatter = new DecimalFormat("00000.00");

	public static final int ACTION_GET_PLANTILLAS = 18;
	
	public static String OPERACION_ANNADIR = "ANNADIR";
	public static String OPERACION_MODIFICAR = "MODIFICAR";
	public static String OPERACION_ELIMINAR = "ELIMINAR";
	public static String OPERACION_FILTRAR = "FILTRAR";
	public static String OPERACION_LISTAR = "LISTAR";
	public static String OPERACION_DIGITALIZAR = "DIGITALIZAR";
	public static String OPERACION_DESACTIVAR = "DESACTIVAR";
	public static String OPERACION_VERSION = "VERSIONAR";
	
	public static String OPERACION_VERSIONADO_INSERT = "INSERTAR";
	public static String OPERACION_VERSIONADO_UPDATE = "ACTUALIZAR";
	public static String OPERACION_VERSIONADO_DELETE = "ELIMINAR";
	
	
	public static String OPERACION_FIJAR_VERSION = "FIJAR_VERSION";
	public static String OPERACION_VER_VERSION = "VER_VERSION ";
	

	
	public static String OPERACION_FILTRO_INFORMES = "FILTRO_INFORMES";
	public static String OPERACION_EXPORTAR = "EXPORTAR";
	public static final String OPERACION_FILTRAR_REFERENCIADOS = "FILTRO_REFERENCIADOS";
	public static final String OPERACION_FILTRAR_VERSION = "FILTRO_VERSION";
	public static final String OPERACION_PUBLICAR = "PUBLICAR";
	public static final String OPERACION_VALIDAR= "VALIDAR";
	public static final String OPERACION_VALIDAR_MPT= "VALIDAR_MPT";
	

	public static int MAPA_EIEL = 50;
	public static String NOMBRE_MAPA_EIEL = "Mapa EIEL";
	public static String NOMBRE_MAPA_EIEL_REDUCIDO = "Mapa EIEL Reducido";
	
	public static String PATRON_FICHA_MUNICIPAL= "EIEL_FichaMunicipal";
	
	public static String PATH_PLANTILLAS_CUADROS_EIEL="plantillas/eiel/cuadros";
	public static String PATH_PLANTILLAS_CUADROS_IMG_EIEL="plantillas/eiel/cuadros/img/";
    public static String PATH_PLANTILLAS_EIEL="plantillas/eiel";
    public static String SUREPORT_DIR="plantillas"+File.separator+"eiel"+File.separator+"subreports"+File.separator;
    public static String IMAGE_DIR="plantillas"+File.separator+"eiel"+File.separator+"img"+File.separator;
    
    public static String PATH_PLANTILLAS="plantillas";

    public static String PATH_ESPACIOPUBLICO="espaciopublico";
    public static String PATH_IMPRESION="impresion";
    public static String PATH_OCUPACION="ocupacion";
    public static String PATH_LICENCIAS="licencias";
    public static String PATH_INVENTARIO="inventario";
    public static String PATH_EIEL="eiel";
    public static String PATH_CONTAMINANTES="contaminantes";
    public static String PATH_ACTIVIDAD="actividad";
    public static String PATH_IMG="img";
    public static String PATH_ESCUDOS="escudos";
    public static String PATH_SUBREPORTS="subreports";
    public static final String EXTENSION_JMP = "jmp";
    public static final String EXTENSION_JRXML = "jrxml";
    
    public static int formatoPDF= 2;
    public static int formatoPREVIEW= 1;
    public static int formatoHTML= 3;
    public static int formatoPRINT= 4;
    public static int formatoXML= 5;
    
    public static final String KEY_NODO_EIEL= "nodo_eiel";
    public static final String KEY_PATH_PLANTILLAS= "pathPlantillas";
    public static final String KEY_ID_MUNICIPIO = "idMunicipio";
	public static final String ABASTECIMIENTO_AGUA = "AA";
	public static final String SANEAMIENTO = "SA";
	
	

	
	public static final String DOBLE_CLICK="DOBLE_CLICK";
	public static final String SORT="SORT";


}
