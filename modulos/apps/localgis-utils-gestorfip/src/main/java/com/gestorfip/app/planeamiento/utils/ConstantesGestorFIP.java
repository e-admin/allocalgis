/**
 * ConstantesGestorFIP.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.utils;

import java.util.Hashtable;

import com.geopista.security.GeopistaPrincipal;


public class ConstantesGestorFIP {

	 public static GeopistaPrincipal principal;

	 public static Hashtable permisos= new Hashtable();
	
	 public static final String idApp= "Fip";
	 
	 
	 public static String loginFIPUrl = "http://localhost:54321/fip";
	 
	 public static int IdMunicipio= 34083;
	 
	 // key para almacenar el hahsMap de propiedades del gestorFip en el Blackboard
	 public static String PROPERTIES_GESTORFIP = "propertiesGestorFip";
	 
	 
	 // key para almacenar el fip de trabajo en el Blackboard
	 public static String FIP_TRABAJO = "fipTrabajo";
	 
	 // Indica la busqueda de la listas de Entidades asociadas a una Determinacion
	 public static int  CONDICION_URBANISTICA_DETERMINACION_SELECTED = 1;
	 
	 // Indica la busqueda de la lista de Determinaciones asociadas a una Entidad
	 public static int  CONDICION_URBANISTICA_ENTIDAD_SELECTED = -1;
	 
	 // key para almacenar el fip de trabajo en el Blackboard
	 public static String OPEN_GESTORFIP = "openGestorFip";
	 
	 public static String VERSION_CONSOLA_UER = "verConsolaUER";
	 
	 
	 
	// key para almacenar el ArrayList de Comunidades Autonomas del gestyorfip en el Blackboard
	 public static String LISTA_CCAA = "listaCCAA";
	 
	// key para almacenar la Comunidad Autonoma de trabajo del gestyorfip en el Blackboard
	 public static String CCAA_SELECCIONADA = "seleccionCCAA";
	 
	// key para almacenar el ambito de trabajo del gestorFip en el Blackboard
	 public static String AMBITO_TRABAJO = "ambitoTrabajo";
	 
	// key para almacenar el nombre del municpio de trabajo del gestorFip en el Blackboard
	public static String MUNICIPIO_TRABAJO = "municipioTrabajo";
	 
	// key para almacenar el path de los documentos de la Comunidad Autonoma de trabajo del gestyorfip en el Blackboard
	 public static String PATH_DOCUMENTOS_CCAA_SELECCIONADA = "path_documentos_ccaa";
	 
	// key para almacenar id del fip de trabajo  en el Blackboard
	 public static String ID_FIP_TRABAJO = "idFipTrabajo";
	 
	// key para almacenar id del tramtite de trabajo  en el Blackboard
	 public static String ID_TRAMITE_TRABAJO = "idTramiteTrabajo";
	 
	// key para almacenar si el mapa de entidades graficas tiene que recargarse
	 public static String IS_MAP_RELOAD_ENT_GRAF = "isMapReloadEntGraf";
	 
	// key para almacenar si el mapa de asociacion entidades y determinaciones tiene que recargarse
	 public static String IS_MAP_RELOAD_ASOC_DET_ENT = "isMapReloadAsocDetEnt";
	 
	// key para almacenar sie l mapa de los documentos tienen que recargarse
	 public static String IS_MAP_RELOAD_DOCUMENTOS = "isMapReloadDoc"; 	 
	 
	 //key que indica donde se encuentran los fichero de tipo FIP1
	 public static final String PATH_FICHEROS_FIP1 = "gestorFip.path.ficherosFip1";
	 
	//key que indica donde se encuentran los documentos
	 public static final String PATH_DOCUMENTOS = "gestorFip.path.documentos";
	 
	//key que guarda el el Geopista Editor en la asociacion de entidades graficas
	 public static final String GEOPISTA_EDITOR_ASOCIACION_ENTIDADES_GRAFICAS = "geopistaeditor_asociacionentidadesgraficas";

	//key que guarda el el Geopista Editor en la asociacion de documentos graficos
	 public static final String GEOPISTA_EDITOR_ASOCIACION_DOCUMENTOS_GRAFICOS = "geopistaeditor_asociaciondocumentosgraficos";
	
	 //key que guarda el el Geopista Editor en la asociacion de determinaciones y entidades
	 public static final String GEOPISTA_EDITOR_ASOCIACION_DETERMINACIONES_ENTIDADES = "geopistaeditor_asociaciondeterminacionesentidades";

	 
	 
	 //ASD REVISAR
	 public static final String GESTORFIP_GET_DATA_LAYER_IMPORT = "gestorfip_getdatalayer_import";
	 public static final String GESTORFIP_DATA_MIGRACION_ASISTIDA = "gestorfip_migasist_data";
	 public static final String GESTORFIP_ADD_DATA_LAYER_IMPORT = "gestorfip_adddatalayer_import";
	 
	 
	 
	 
	 
	// key para almacenar la configuracion de la consola de UER a la que concectarse
	 public static String CONFIG_VERSION_CONSOLE_UER = "configVersionConsolaUeR";
	 public static String CONFIG_CRS_CONSOLE_UER = "configCrsConsolaUeR";
	 
	// key para almacenar las versiones disponibles de la consola de UER a la que concectarse
    public static String VERSIONES_CONSOLE_UER = "versionesConsoleUeR";
    public static String LST_CRS_GESTOR= "crsGestor";
	 


	 public static int MAPA_GESTORFIP = 151;  
	 
	 // constantes para identifiar si los datos de los paneles de la determinacion
	 //corresponden a un alta o a una modificacion
	 public static int ESTADO_ACCION_DETERMINACIONES_NINGUNA = 0;
     public static int ESTADO_ACCION_DETERMINACIONES_ALTA = 1;
	 public static int ESTADO_ACCION_DETERMINACIONES_MODIF = 2;
	 
	 // constantes para identifiar si los datos de los paneles de la determinacion
	 //corresponden a un alta o a una modificacion
	 public static int ESTADO_ACCION_ENTIDADES_NINGUNA = 0;
     public static int ESTADO_ACCION_ENTIDADES_ALTA = 1;
	 public static int ESTADO_ACCION_ENTIDADES_MODIF = 2;
	 
	// constantes para identifiar si los datos de los paneles de la Regulacion especifica
	 //corresponden a un alta o a una modificacion
	 public static int ESTADO_ACCION_REGULACION_ESPECIFICA_NINGUNA = 0;
     public static int ESTADO_ACCION_REGULACION_ESPECIFICA_ALTA = 1;
	 public static int ESTADO_ACCION_REGULACION_ESPECIFICA_MODIF = 2;
	 
	// constantes para identifiar si los datos de los paneles de documentos
	 //corresponden a un alta o a una modificacion
	 public static int ESTADO_ACCION_DOCUMENTOS_NINGUNA = 0;
     public static int ESTADO_ACCION_DOCUMENTOS_ALTA = 1;
	 public static int ESTADO_ACCION_DOCUMENTOS_MODIF = 2;
	 
	// constantes para identifiar si los datos de los paneles de documentos
	 //corresponden a un alta o a una modificacion
	 public static int ESTADO_ACCION_ADSCRIPCION_NINGUNA = 0;
     public static int ESTADO_ACCION_ADSCRIPCION_ALTA = 1;
	 public static int ESTADO_ACCION_ADSCRIPCION_MODIF = 2;
	 
	 public static String calendarValue="";
	 
	 // Constantes para identificar el nombre de las secuencias de la BBDD
	 public static String SEQUENCE_TRAMITE_DETERMINACIONES = "gestorfip.seq_tramite_determinaciones";
	 public static String SEQUENCE_TRAMITE_DETERMINACIONES_VALOR_REFERENCIA =  "gestorfip.seq_tramite_determinacion_valoresreferencia";
	 public static String SEQUENCE_TRAMITE_DETERMINACIONES_GRUPO_APLICACION =  "gestorfip.seq_tramite_determinacion_gruposaplicacion";
	 public static String SEQUENCE_TRAMITE_DETERMINACIONES_DETERMINACION_REGULADORA =  "gestorfip.seq_tramite_determinacion_determinacionesreguladoras";
	 public static String SEQUENCE_TRAMITE_DETERMINACIONES_DOCUMENTOS =  "gestorfip.seq_tramite_determinacion_documentos";
	 public static String SEQUENCE_TRAMITE_REGULACIONES_ESPECIFICAS =  "gestorfip.seq_tramite_determinacion_regulacionesespecificas";
	 public static String SEQUENCE_TRAMITE_DETERMINACIONES_OPERACIONES =  "gestorfip.seq_tramite_operacionesdeterminaciones";
	
	 
	 public static String SEQUENCE_TRAMITE_ENTIDADES = "gestorfip.seq_tramite_entidades";
	 public static String SEQUENCE_TRAMITE_ENTIDADES_DOCUMENTOS =  "gestorfip.seq_tramite_entidad_documentos";
	 public static String SEQUENCE_TRAMITE_ENTIDADES_OPERACIONES =  "gestorfip.seq_tramite_operacionesentidades";
	 
	 public static String SEQUENCE_TRAMITE_DOCUMENTOS = "gestorfip.seq_tramite_documentos";
	 public static String SEQUENCE_TRAMITE_DOCUMENTOS_HOJAS = "gestorfip.seq_tramite_documento_hojas";	 
	 
	 public static String SEQUENCE_TRAMITE_ADSCRIPCIONES = "gestorfip.seq_tramite_adscripciones";

	// GestorFIP WebService returns code
	static public final String SUCCESS_CODE = "000";
	static public final String DB_CONNEXION_ERROR_CODE = "-10";
	static public final String DB_INSERTION_ERROR_CODE = "-11";	
	static public final String DB_EXTRACTION_ERROR_CODE = "-12";
	static public final String DB_ROLLBACK_ERROR_CODE = "-13";
	static public final String XML_VALIDATION_ERROR_CODE = "-21";
	static public final String XML_PARSING_ERROR_CODE = "-22";
	static public final String XML_GENERATION_ERROR_CODE = "-23";
	static public final String NON_FIP1_ERROR_CODE = "-31";
	static public final String PROPERTIES_FILE_ERROR_CODE = "-41";
	static public final String OTHER_ERROR_CODE = "-91";
	
	
	public static final String LISTA_TIPOS_CARACTER_DETERMINACION= "lstTiposCaracterDeterminacio";
	
	// Tipos Caracter Determinaciones
	public static final String CARATERDETERMINACION_UNIDAD= "caracterDeterminacion_unidad";
	public static final String CARATERDETERMINACION_VALORREFERENCIA= "caracterDeterminacion_valorreferencia";
	public static final String CARATERDETERMINACION_NORMADEUSOS= "caracterDeterminacion_normadeusos";	
	public static final String CARATERDETERMINACION_REGIMENDEUSO= "caracterDeterminacion_regimendeuso";
	public static final String CARATERDETERMINACION_REGIMENDEACTOS= "caracterDeterminacion_regimendeactos";
	
	public static final String CARATERDETERMINACION_ENUNCIADO_VALOR = "000001";
	public static final String CARATERDETERMINACION_NORMAGENERALLITERAL_VALOR = "000002";
	public static final String CARATERDETERMINACION_NORMAGENERALGRAFICA_VALOR = "000003";
	public static final String CARATERDETERMINACION_NORMADEUSOS_VALOR = "000004";	
	public static final String CARATERDETERMINACION_NORMADEACTOS_VALOR = "000005";
	public static final String CARATERDETERMINACION_NORMADEADSCIPCIONES_VALOR = "000006";
	public static final String CARATERDETERMINACION_REGIMENDEUSOS_VALOR = "000007";
	public static final String CARATERDETERMINACION_REGIMENDEACTOS_VALOR = "000008";
	public static final String CARATERDETERMINACION_USO_VALOR = "000009";
	public static final String CARATERDETERMINACION_ACTOEJECUCION_VALOR = "000010";
	public static final String CARATERDETERMINACION_ENUNCIADOCOMPLEMENTARIO_VALOR = "000011";
	public static final String CARATERDETERMINACION_VALORREFERENCIA_VALOR = "000012";
	public static final String CARATERDETERMINACION_REGULACION_VALOR = "000013";
	public static final String CARATERDETERMINACION_GRUPOUSOS_VALOR = "000014";
	public static final String CARATERDETERMINACION_GRUPOACTOS_VALOR = "000015";
	public static final String CARATERDETERMINACION_VITUAL_VALOR = "000016";
	public static final String CARATERDETERMINACION_OPERADORA_VALOR = "000017";
	public static final String CARATERDETERMINACION_UNIDAD_VALOR = "000018";
	public static final String CARATERDETERMINACION_ADSCRIPCION_VALOR = "000019";
	public static final String CARATERDETERMINACION_GRUPODEENTIDADES_VALOR = "000020";

	
	//NAME ARBOLES TRAMITES
	public static final String TRAMITE_PLANEAMIENTO_ENCARGADO_DETERMINACIONES = "PlaneamientoEncargadoDeterminaciones";
	public static final String TRAMITE_PLANEAMIENTO_VIGENTE_DETERMINACIONES = "PlaneamientoVigenteDeterminaciones";
	public static final String TRAMITE_CATALOGO_SISTEMATIZADO_DETERMINACIONES = "CatalogoSistematizadoDeterminaciones";
	
	public static final String TRAMITE_PLANEAMIENTO_ENCARGADO_ENTIDADES = "PlaneamientoEncargadoEntidades";
	public static final String TRAMITE_PLANEAMIENTO_VIGENTE_ENTIDADES = "PlaneamientoVigenteEntidades";
	public static final String TRAMITE_CATALOGO_SISTEMATIZADO_ENTIDADES = "CatalogoSistematizadoEntidades";
	
	public final static String LAYER_ACCION = "layer_accion";
	public final static String LAYER_AFECCION = "layer_afeccion";
	public final static String LAYER_ALINEACION = "layer_alineacion";
	public final static String LAYER_AMBITO =  "layer_ambito";
	public final static String LAYER_CATEGORIA = "layer_categoria";
	public final static String LAYER_CLASIFICACION= "layer_clasificacion";
	public final static String LAYER_GESTION = "layer_gestion";
	public final static String LAYER_PROTECCION = "layer_proteccion";
	public final static String LAYER_SISTEMAS = "layer_sistemas";
	public final static String LAYER_ZONA = "layer_zona";
	
	
	//Renderer Arboles
	public static final int TIPO_NOESPECIFICADO = 0;
    public static final int TIPO_TRAMITE = 1;
    public static final int TIPO_DETERMINACION = 2;
    public static final int TIPO_REGULACION_ESPECIFICA = 3;
    public static final int TIPO_ENTIDAD = 4;
    public static final int TIPO_DOCUMENTO = 5;
    public static final int TIPO_HOJA = 6;
    
    public static final int TIPO_DETERMINACION_ACTO = 101;
    public static final int TIPO_DETERMINACION_ENUNCIADO = 102;
    public static final int TIPO_DETERMINACION_ENUM_COMPL = 103;
    public static final int TIPO_DETERMINACION_GRUPO_ACTOS = 104;
    public static final int TIPO_DETERMINACION_GRUPO_ENTIDADES = 105;
    public static final int TIPO_DETERMINACION_GRUPO_USOS = 106;
    public static final int TIPO_DETERMINACION_NORMA_GENERICA_GRAFICA = 107;
    public static final int TIPO_DETERMINACION_NORMA_USO = 108;
    public static final int TIPO_DETERMINACION_NORMA_GENERAL_LITERAL = 109;
    public static final int TIPO_DETERMINACION_REGIMEN_USO = 110;
    //public static final int TIPO_DETERMINACION_REGULACION = 111;
    public static final int TIPO_DETERMINACION_UNIDAD = 112;
    public static final int TIPO_DETERMINACION_USO  = 113;
    public static final int TIPO_DETERMINACION_VALOR_REFERENCIA = 114;
    public static final int TIPO_DETERMINACION_ADSCRIPCION = 115;
    public static final int TIPO_DETERMINACION_REGIMEN_ACTO = 116;
    static public final int TIPO_GRUPO_APLICACION_GRUPO = 120;
    static public final int TIPO_GRUPO_APLICACION_AMBITO = 121;
    static public final int TIPO_GRUPO_APLICACION_CLASE_SUELO = 122;
    static public final int TIPO_GRUPO_APLICACION_CAT_SUELO_URBANO = 123;
    static public final int TIPO_GRUPO_APLICACION_CAT_SUELO_URBANIZABLE = 124;
	static public final int TIPO_GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE = 125;
	static public final int TIPO_GRUPO_APLICACION_SISTEMA_POLIGONAL = 126;
    static public final int TIPO_GRUPO_APLICACION_ZONA = 127;
    static public final int TIPO_GRUPO_APLICACION_PROTECCION_POLIGONAL = 128;
    static public final int TIPO_GRUPO_APLICACION_UNIDAD_ACTUACION = 129;
    static public final int TIPO_GRUPO_APLICACION_SECTOR = 130;
    static public final int TIPO_GRUPO_APLICACION_AREA_REPARTO = 131;
    static public final int TIPO_GRUPO_APLICACION_UNIDAD_EJECUCION = 132;
    static public final int TIPO_GRUPO_APLICACION_AFECCION_POLIGONAL = 133;
    static public final int TIPO_GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO = 134;
    static public final int TIPO_GRUPO_APLICACION_ACCION = 135;
    static public final int TIPO_GRUPO_APLICACION_SISTEMA_LINEAL = 136;
	static public final int TIPO_GRUPO_APLICACION_ALINEACION = 137;
	static public final int TIPO_GRUPO_APLICACION_PROTECCION_LINEAL = 138;
	static public final int TIPO_GRUPO_APLICACION_PROTECCION_PUNTUAL = 139;
	static public final int TIPO_GRUPO_APLICACION_AFECCION_LINEAL = 140;
	static public final int TIPO_GRUPO_APLICACION_AFECCION_PUNTUAL= 141;
	static public final int TIPO_GRUPO_APLICACION_UNIDAD_NORMALIZACION = 142;
	static public final int TIPO_GRUPO_APLICACION_UNIDAD_URBANA = 143;
	static public final int TIPO_GRUPO_APLICACION_OTROS_AMBITOS = 144;
	static public final int TIPO_GRUPO_APLICACION_AREA_MOVIMIENTO = 145;
	static public final int TIPO_GRUPO_APLICACION_PAUTA_ORDENACION = 146;
	static public final int TIPO_GRUPO_APLICACION_AJUSTE_CARTOGRAFICO = 147;
	static public final int TIPO_GRUPO_APLICACION_ACTUACION_AISLADA = 148;
	static public final int TIPO_GRUPO_APLICACION_AMB_GESTION = 149;
	static public final int TIPO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL = 150;
	static public final int TIPO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL = 151;
    
	
	 // Grupos de aplicacion de entidades
    static public final String GRUPO_APLICACION_GRUPO = "7000000001";
    static public final String GRUPO_APLICACION_AMBITO = "7000000002";
    static public final String GRUPO_APLICACION_CLASE_SUELO = "7000000003";
    static public final String GRUPO_APLICACION_CAT_SUELO_URBANO = "7000000004";
    static public final String GRUPO_APLICACION_CAT_SUELO_URBANIZABLE = "7000000005";
	static public final String GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE = "7000000006";
	static public final String GRUPO_APLICACION_SISTEMA_POLIGONAL = "7000000008";
    static public final String GRUPO_APLICACION_ZONA = "7000000011";
    static public final String GRUPO_APLICACION_PROTECCION_POLIGONAL = "7000000013";
    static public final String GRUPO_APLICACION_UNIDAD_ACTUACION = "7000000016";
    static public final String GRUPO_APLICACION_SECTOR = "7000000017";
    static public final String GRUPO_APLICACION_AREA_REPARTO = "7000000018";
    static public final String GRUPO_APLICACION_UNIDAD_EJECUCION = "7000000021";
    static public final String GRUPO_APLICACION_AFECCION_POLIGONAL = "7000000031";
    static public final String GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO = "7000000042";
    static public final String GRUPO_APLICACION_ACCION = "7000000043";
    static public final String GRUPO_APLICACION_SISTEMA_LINEAL = "7000000050";
	static public final String GRUPO_APLICACION_ALINEACION = "7000000053";
	static public final String GRUPO_APLICACION_PROTECCION_LINEAL = "7000000057";
	static public final String GRUPO_APLICACION_PROTECCION_PUNTUAL = "7000000059";
	static public final String GRUPO_APLICACION_AFECCION_LINEAL = "7000000066";
	static public final String GRUPO_APLICACION_AFECCION_PUNTUAL= "7000000070";
	static public final String GRUPO_APLICACION_UNIDAD_NORMALIZACION = "7000000074";
	static public final String GRUPO_APLICACION_UNIDAD_URBANA = "7000000093";
	static public final String GRUPO_APLICACION_OTROS_AMBITOS = "7000000094";
	static public final String GRUPO_APLICACION_AREA_MOVIMIENTO = "7000000097";
	static public final String GRUPO_APLICACION_PAUTA_ORDENACION = "7000000098";
	static public final String GRUPO_APLICACION_AJUSTE_CARTOGRAGICO = "7000000099";
	static public final String GRUPO_APLICACION_ACTUACION_AISLADA = "7000000100";
	static public final String GRUPO_APLICACION_AMB_GESTION = "7000000101";
	static public final String GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL = "7000000102";
	static public final String GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL = "7000000103";
    
    
    public static final String ICONO_NOESPECIFICADO = "noespecificado.gif";
    public static final String ICONO_TRAMITE = "tramite.gif";
    public static final String ICONO_DETERMINACION = "determinacion.gif";
    public static final String ICONO_REGULACION_ESPECIFICA = "regulacionespecifica.gif";
    public static final String ICONO_ENTIDAD = "entidad.gif";
    public static final String ICONO_DOCUMENTO = "documento.gif";
    public static final String ICONO_HOJA = "notas.png";
    

    //Iconos arboles determinaciones
    public static final String ICONO_DETERMINACION_ACTOS= "acto.png";
    public static final String ICONO_DETERMINACION_ENUNCIADO = "enunciado.png";
    public static final String ICONO_DETERMINACION_ENUM_COMPL = "enunciado.png";
    public static final String ICONO_DETERMINACION_GRUPO_ACTOS = "grupoactos.png";
    public static final String ICONO_DETERMINACION_GRUPO_ENTIDADES = "grupoentidad.png";
    public static final String ICONO_DETERMINACION_GRUPO_USOS = "grupousos.png";
    public static final String ICONO_DETERMINACION_NORMA_GENERICA_GRAFICA = "normageneral.png";
    public static final String ICONO_DETERMINACION_NORMA_USO = "normauso.gif";
    public static final String ICONO_DETERMINACION_NORMA_GENERAL_LITERAL = "normageneral.png";
    public static final String ICONO_DETERMINACION_REGIMEN_USO = "regimenuso.png";
    public static final String ICONO_DETERMINACION_REGIMEN_ACTO = "regimenacto.png";
    //public static final String ICONO_DETERMINACION_REGULACION = "regulacion.gif";
    public static final String ICONO_DETERMINACION_UNIDAD = "unidad.png";
    public static final String ICONO_DETERMINACION_USO = "uso.png";
    public static final String ICONO_DETERMINACION_VALOR_REFERENCIA = "valorreferencia.png";
    public static final String ICONO_DETERMINACION_ADSCRIPCION = "adscripcion.png";
    
	//Iconos arboles entidades
	static public final String ICONO_GRUPO_APLICACION_GRUPO = "7000000001.png";
    static public final String ICONO_GRUPO_APLICACION_AMBITO = "7000000002.png";
    static public final String ICONO_GRUPO_APLICACION_CLASE_SUELO = "7000000003.png";
    static public final String ICONO_GRUPO_APLICACION_CAT_SUELO_URBANO = "7000000004.png";
    static public final String ICONO_GRUPO_APLICACION_CAT_SUELO_URBANIZABLE = "7000000005.png";
	static public final String ICONO_GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE = "7000000006.png";
	static public final String ICONO_GRUPO_APLICACION_SISTEMA_POLIGONAL = "7000000008.png";
    static public final String ICONO_GRUPO_APLICACION_ZONA = "7000000011.png";
    static public final String ICONO_GRUPO_APLICACION_PROTECCION_POLIGONAL = "7000000013.png";
    static public final String ICONO_GRUPO_APLICACION_UNIDAD_ACTUACION = "7000000016.png";
    static public final String ICONO_GRUPO_APLICACION_SECTOR = "7000000017.png";
    static public final String ICONO_GRUPO_APLICACION_AREA_REPARTO = "7000000018.png";
    static public final String ICONO_GRUPO_APLICACION_UNIDAD_EJECUCION = "7000000021.png";
    static public final String ICONO_GRUPO_APLICACION_AFECCION_POLIGONAL = "7000000031.png";
    static public final String ICONO_GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO = "7000000042.png";
    static public final String ICONO_GRUPO_APLICACION_ACCION = "7000000043.png";
    static public final String ICONO_GRUPO_APLICACION_SISTEMA_LINEAL = "7000000050.png";
	static public final String ICONO_GRUPO_APLICACION_ALINEACION = "7000000053.png";
	static public final String ICONO_GRUPO_APLICACION_PROTECCION_LINEAL = "7000000057.png";
	static public final String ICONO_GRUPO_APLICACION_PROTECCION_PUNTUAL = "7000000059.png";
	static public final String ICONO_GRUPO_APLICACION_AFECCION_LINEAL = "7000000066.png";
	static public final String ICONO_GRUPO_APLICACION_AFECCION_PUNTUAL= "7000000070.png";
	static public final String ICONO_GRUPO_APLICACION_UNIDAD_NORMALIZACION = "7000000074.png";
	static public final String ICONO_GRUPO_APLICACION_UNIDAD_URBANA = "7000000093.png";
	static public final String ICONO_GRUPO_APLICACION_OTROS_AMBITOS = "7000000094.png";
	static public final String ICONO_GRUPO_APLICACION_AREA_MOVIMIENTO = "7000000097.png";
	static public final String ICONO_GRUPO_APLICACION_PAUTA_ORDENACION = "7000000098.png";
	static public final String ICONO_GRUPO_APLICACION_AJUSTE_CARTOGRAFICO = "7000000099.png";
	static public final String ICONO_GRUPO_APLICACION_ACTUACION_AISLADA = "7000000100.png";
	static public final String ICONO_GRUPO_APLICACION_AMB_GESTION = "7000000101.png";
	static public final String ICONO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL = "7000000102.png";
	static public final String ICONO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL = "7000000103.png";
	
	// versiones consola UER
	static public final double VERSIONCONSOLA_UER_1_86 = 1.86;
	static public final double VERSIONCONSOLA_UER_2_00 = 2.00;

}
