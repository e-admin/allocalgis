package com.gestorfip.ws.utils;


public class Constants {

	public static int IS_MAP_GESTORFIP = 151;
	public static String NAME_MAP = "Map Gestor Planeamiento FIP";
	public static String NAME_FAMILY = "Familia Capas Gestor de Planeamiento - FIP";
	public static String NAME_DOMAIN = "Gestor Planeamiento FIP";
	
	public static int TAMANIO_COLUMNA = 50;
	
	// Editor FIP property file
	static public final String GESTORFIP_PROPERTIES_FILE = "gestorfip.properties";
	
	// Caracteres determination aplicaciones max value
	static public final String APLICACIONES_MAX_VALUE_FIP = "2147483648";
	static public final int APLICACIONES_MAX_VALUE = 2147483647;
	
	
	// FIP1-FIP2 processing codes
	static public final String SUCCESS_CODE = "000";
	static public final String DB_CONNEXION_ERROR_CODE = "-10";	
	static public final String DB_INSERTION_ERROR_CODE = "-11";	
	static public final String DB_EXTRACTION_ERROR_CODE = "-12";
	static public final String XML_VALIDATION_ERROR_CODE = "-21";
	static public final String XML_PARSING_ERROR_CODE = "-22";
	static public final String XML_GENERATION_ERROR_CODE = "-23";
	static public final String NON_FIP1_ERROR_CODE = "-31";
	static public final String PROPERTIES_FILE_ERROR_CODE = "-41";
	static public final String DIRECTORY_CREATION_ERROR_CODE = "-51";
	static public final String OTHER_ERROR_CODE = "-91";	
	
	
	// Codigos de los grupos de entidades.
	static public final String GRUPO_ENTIDADES = "7777777777"; 

	static public final String GRUPO_APLICACION_AMBITO = "7000000002";
	static public final String GRUPO_APLICACION_CLASE_SUELO = "7000000003";
	static public final String GRUPO_APLICACION_CAT_SUELO_URBANO = "7000000004";
	static public final String GRUPO_APLICACION_CAT_SUELO_URBANIZABLE = "7000000005";
	static public final String GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE = "7000000006";
	static public final String GRUPO_APLICACION_AREA_HOMOGENEA = "7000000007";
	static public final String GRUPO_APLICACION_SISTEMA_POLIGONAL = "7000000008";
	static public final String GRUPO_APLICACION_ZONA = "7000000011";
	static public final String GRUPO_APLICACION_PROTECCION_POLIGONAL = "7000000013";
	static public final String GRUPO_APLICACION_UNIDAD_ACTUACION = "7000000016";
	static public final String GRUPO_APLICACION_SECTOR = "7000000017";
	static public final String GRUPO_APLICACION_AREA_REPARTO = "7000000018";
	static public final String GRUPO_APLICACION_UNIDAD_EJECUCION = "7000000021";
	static public final String GRUPO_APLICACION_AREA_USO_GLOBAL = "7000000025";
	static public final String GRUPO_APLICACION_RESERVA = "7000000030";
	static public final String GRUPO_APLICACION_AFECCION_POLIGONAL = "7000000031";
	static public final String GRUPO_APLICACION_SUBCAT_SUELO_URBANO = "7000000040";
	static public final String GRUPO_APLICACION_SUBCAT_SUELO_URBANIZABLE = "7000000041";
	static public final String GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO = "7000000042";
	static public final String GRUPO_APLICACION_ACCION = "7000000043";
	static public final String GRUPO_APLICACION_SECTORIZACION = "7000000044";
	static public final String GRUPO_APLICACION_ZONA_ORDENACION_URBANISTICA = "7000000049";
	static public final String GRUPO_APLICACION_SISTEMA_LINEAL = "7000000050";
	static public final String GRUPO_APLICACION_ALINEACION = "7000000053";
	static public final String GRUPO_APLICACION_RASANTE = "7000000055";
	static public final String GRUPO_APLICACION_PROTECCION_LINEAL = "7000000057";
	static public final String GRUPO_APLICACION_PROTECCION_PUNTUAL = "7000000059";
	static public final String GRUPO_APLICACION_ESPACIOS_GESTION_INTEGRADA = "7000000061";
	static public final String GRUPO_APLICACION_AMBITO_ACTUACION = "7000000064";
	static public final String GRUPO_APLICACION_AFECCION_LINEAL = "7000000066";
	static public final String GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PUNTUAL = "7000000068";
	static public final String GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_LINEAL = "7000000069";
	static public final String GRUPO_APLICACION_AFECCION_PUNTUAL = "7000000070";
	static public final String GRUPO_APLICACION_AMBITO_DESARROLLO = "7000000071";
	static public final String GRUPO_APLICACION_AREA_PLANEAMIENTO_ASUMIDO = "7000000072";
	static public final String GRUPO_APLICACION_AMBITO_PLANEAMIENTO_DESARROLLO = "7000000073";
	static public final String GRUPO_APLICACION_UNIDAD_NORMALIZACION = "7000000074";
	static public final String GRUPO_APLICACION_ZONA_SUELO_NO_URBANIZABLE = "7000000079";
	static public final String GRUPO_APLICACION_ZONA_SUELO_URBANIZABLE = "7000000080";
	static public final String GRUPO_APLICACION_ZONA_SUELO_URBANO = "7000000081";
	static public final String GRUPO_APLICACION_ZONA_ALTURAS = "7000000083";
	static public final String GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PROTEGIDO = "7000000091";
	static public final String GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_NO_PROTEGIDO = "7000000092";
	static public final String GRUPO_APLICACION_UNIDAD_URBANA = "7000000093";
	static public final String GRUPO_APLICACION_OTROS_AMBITOS = "7000000094";
	static public final String GRUPO_APLICACION_COTA = "7000000095";
	static public final String GRUPO_APLICACION_PAUTA_ORDENACION = "7000000098";
	static public final String GRUPO_APLICACION_ACTUACION_AISLADA = "7000000100";
	static public final String GRUPO_APLICACION_ZONAS_PLTO_INCORPORADO = "7000000101";
	static public final String GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL = "7000000102";
	static public final String GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL = "7000000103";
	static public final String GRUPO_APLICACION_ZONAS_PLAN_PARCIAL = "7000000109";
	static public final String GRUPO_APLICACION_ZONAS_ESTUDIO_DETALLE = "7000000110";
	static public final String GRUPO_APLICACION_PARCELA = "7000000111";
	static public final String GRUPO_APLICACION_PATRIMONIO_MUNICIPAL_SUELO = "7000000112";
	static public final String GRUPO_APLICACION_SISTEMAS_ESTUDIO_DETALLE = "7000000113";
	static public final String GRUPO_APLICACION_ACCIONES = "7000000114";
	static public final String GRUPO_APLICACION_AFECCIONES_ESTUDIO_DETALLE = "7000000115";
	static public final String GRUPO_APLICACION_UNIDADES_NORMALIZACION_FINCAS = "7000000116";
	static public final String GRUPO_APLICACION_ACTUACIONES_URBANAS_CONCERTADAS = "7000000117";
	static public final String GRUPO_APLICACION_PROYECTO_ACTUACION_PRIORITARIA = "7000000118";
	static public final String GRUPO_APLICACION_INSTRUMENTO_GESTION = "7000000119";
	static public final String GRUPO_APLICACION_ALTURAS = "7000000120";
	static public final String GRUPO_APLICACION_FONDOS_LINEAL = "7000000121";
	static public final String GRUPO_APLICACION_ZONA_FONFO = "7000000124";
	static public final String GRUPO_APLICACION_ZONA_EDIFICABILIDADES = "7000000125";	
	static public final String GRUPO_APLICACION_ZONA_USO = "7000000155";
	static public final String GRUPO_APLICACION_AREA_INTERVENCION = "7000000156";
	static public final String GRUPO_APLICACION_INTERVENCION_PUNTUAL = "7000000158";
	static public final String GRUPO_APLICACION_ZONA_ALTURA = "7000000159";
	static public final String GRUPO_APLICACION_ZONA_EDIFICABILIDAD = "7000000161";
	static public final String GRUPO_APLICACION_PARCELA_SUSPENDIDA = "8000000010";
	
	

	static public final String AMBITOS_GESTORFIP = "ambitosgestorfip";
	
	
	public final static String CARACTERDETERMINACION_ENUNCIADO= "caracterDeterminacion_enunciado";
	public final static String CARACTERDETERMINACION_NORMAGENERALLITERAL= "caracterDeterminacion_normageneralliteral";
	public final static String CARACTERDETERMINACION_NORMAGENERALGRAFICA= "caracterDeterminacion_normageneralgrafica";
	public final static String CARACTERDETERMINACION_NORMADEUSOS= "caracterDeterminacion_normadeusos";
	public final static String CARACTERDETERMINACION_NORMADEACTOS= "caracterDeterminacion_normadeactos";
	public final static String CARACTERDETERMINACION_NORMADEADSCRIPCIONES= "caracterDeterminacion_normadeadscripciones";
	public final static String CARACTERDETERMINACION_REGIMENDEUSO= "caracterDeterminacion_regimendeuso";
	public final static String CARACTERDETERMINACION_REGIMENDEACTOS= "caracterDeterminacion_regimendeactos";
	public final static String CARACTERDETERMINACION_USO= "caracterDeterminacion_uso";
	public final static String CARACTERDETERMINACION_ACTODEEJECUCION= "caracterDeterminacion_actodeejecucion";
	public final static String CARACTERDETERMINACION_ENUNCIADOCOMPLEMENTARIO= "caracterDeterminacion_enunciadocomplementario";
	public final static String CARACTERDETERMINACION_VALORDEREFERENCIA= "caracterDeterminacion_valorreferencia";
	public final static String CARACTERDETERMINACION_REGULACION= "caracterDeterminacion_regulacion";
	public final static String CARACTERDETERMINACION_GRUPODEUSOS= "caracterDeterminacion_grupodeusos";
	public final static String CARACTERDETERMINACION_GRUPODEACTOS= "caracterDeterminacion_grupodeactos";
	public final static String CARACTERDETERMINACION_VIRTUAL= "caracterDeterminacion_virtual";
	public final static String CARACTERDETERMINACION_OPERADORA= "caracterDeterminacion_operadora";
	public final static String CARACTERDETERMINACION_UNIDAD= "caracterDeterminacion_unidad";
	public final static String CARACTERDETERMINACION_ADSCRIPCION= "caracterDeterminacion_adscripcion";
	public final static String CARACTERDETERMINACION_GRUPOENTIDADES= "caracterDeterminacion_grupoentidades";
	
	public final static String DETERMINACION_CARPETA= "determinacion_carpeta";
	
	
	public final static String IDENTIFICADOR_TABLE_GENERICO = "_gen";
	// Titulos de las layers
	static public final String LAYER_TITLE_PLANEAMIENTO_ACCIONES = "Acciones";
	static public final String LAYER_TITLE_PLANEAMIENTO_AFECCIONES= "Afecciones";
	static public final String LAYER_TITLE_PLANEAMIENTO_ALINEACION = "Alineacion";
	static public final String LAYER_TITLE_PLANEAMIENTO_AMBITO = "Ambito";
	static public final String LAYER_TITLE_PLANEAMIENTO_CATEGORIA = "Categoria";
	static public final String LAYER_TITLE_PLANEAMIENTO_CLASIFICACION = "Clasificacion";
	static public final String LAYER_TITLE_PLANEAMIENTO_DESARROLLO = "Desarrollo";
	static public final String LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION = "Equidistribucion";
	static public final String LAYER_TITLE_PLANEAMIENTO_GESTION = "Gestion";	
	static public final String LAYER_TITLE_PLANEAMIENTO_PROTECCION = "Proteccion";
	static public final String LAYER_TITLE_PLANEAMIENTO_SISTEMAS = "Sistemas";
	static public final String LAYER_TITLE_PLANEAMIENTO_ZONA = "Zona";
	static public final String LAYER_TITLE_PLANEAMIENTO_OTRAS_INDICACIONES = "OtrasIndicaciones";
	
	// nombre de las layers
	public final static String LAYER_ACCION = "layer_accion";
	public final static String LAYER_AFECCION = "layer_afeccion";
	public final static String LAYER_ALINEACION = "layer_alineacion";
	public final static String LAYER_AMBITO =  "layer_ambito";
	public final static String LAYER_CATEGORIA = "layer_categoria";
	public final static String LAYER_CLASIFICACION = "layer_clasificacion";
	public final static String LAYER_DESARROLLO = "layer_desarrollo";
	public final static String LAYER_EQUIDISTRIBUCION = "layer_equidistribucion";
	public final static String LAYER_GESTION = "layer_gestion";
	public final static String LAYER_PROTECCION = "layer_proteccion";
	public final static String LAYER_SISTEMAS = "layer_sistemas";
	public final static String LAYER_ZONA = "layer_zona";
	public final static String LAYER_OTRAS_INDICACIONES = "layer_otras_indicaciones";
	
	//Nombres de las tablas donde se insertan las geometrias de las entidades
	public final static String TABLE_NAME_ACCIONES = "planeamiento_acciones";
	public final static String TABLE_NAME_AFECCIONES = "planeamiento_afecciones";
	public final static String TABLE_NAME_ALINEACION = "planeamiento_alineacion";
	public final static String TABLE_NAME_AMBITO = "planeamiento_ambito";
	public final static String TABLE_NAME_CATEGORIA = "planeamiento_categoria";
	public final static String TABLE_NAME_CLASIFICACION = "planeamiento_clasificacion";
	public final static String TABLE_NAME_DESARROLLO = "planeamiento_desarrollo";
	public final static String TABLE_NAME_EQUIDISTRIBUCION = "planeamiento_equidistribucion";
	public final static String TABLE_NAME_GESTION = "planeamiento_gestion";
	public final static String TABLE_NAME_PROTECCION = "planeamiento_proteccion";
	public final static String TABLE_NAME_SISTEMAS = "planeamiento_sistemas";
	public final static String TABLE_NAME_ZONA = "planeamiento_zona";
	public final static String TABLE_NAME_OTRAS_INDICACIONES = "planeamiento_otras_indicaciones";
	
	public final static String SEQ_ACCIONES = "seq_planeamiento_acciones_gen";
	public final static String SEQ_AFECCIONES = "seq_planeamiento_afecciones_gen";
	public final static String SEQ_ALINEACION = "seq_planeamiento_alineacion_gen";
	public final static String SEQ_AMBITO = "seq_planeamiento_ambito_gen";
	public final static String SEQ_CATEGORIA = "seq_planeamiento_categoria_gen";
	public final static String SEQ_CLASIFICACION = "seq_planeamiento_clasificacion_gen";
	public final static String SEQ_DESARROLLO = "seq_planeamiento_desarrollo_gen";
	public final static String SEQ_EQUIDISTRIBUCION = "seq_planeamiento_equidistribucion_gen";
	public final static String SEQ_GESTION = "seq_planeamiento_gestion_gen";
	public final static String SEQ_PROTECCION = "seq_planeamiento_proteccion_gen";
	public final static String SEQ_SISTEMAS = "seq_planeamiento_sistemas_gen";
	public final static String SEQ_ZONA = "seq_planeamiento_zona_gen";
	public final static String SEQ_OTRAS_INDICACIONES = "seq_planeamiento_otras_indicaciones_gen";
	
	
	// Attributes Layers
	public final static String ATT_GEOMETRIA = "GEOMETRY";
	public final static String ATT_ID = "Id";
	public final static String ATT_ID_MUNICIPIO = "id_municipio";
	public final static String ATT_CLAVE = "Clave";
	public final static String ATT_CODIGO = "Codigo";
	public final static String ATT_ETIQUETA = "Etiqueta";
	public final static String ATT_NOMBRE = "Nombre";
	public final static String ATT_GRUPOAPLICACION = "GrupoAplicacion";
	
	public final static String CAT = "[cat]";
	public final static String GL = "[gl]";
	public final static String VA = "[va]";
	public final static String EU = "[eu]";
	
	public final static String ES_ES = "es_ES";
	public final static String CA_ES = "ca_ES";
	public final static String GL_ES = "gl_ES";
	public final static String VA_ES = "va_ES";
	public final static String EU_ES = "eu_ES";

	public final static String ID_VALOR = "idvalores";
	public final static String ID_VALOR_REFERENCIA = "idvalorreferencia";
	public final static String ID_REGIMEN_ESPECIFICO = "idregimenespecifico";
	
	public final static String KEY_LST_VALORES_DETERMINACIONES_APLICADA = "lstvaloresdetaplicada";
	public final static String KEY_LST_VALORES_USOS_REGULACION = "lstvaloresusoregulacion"; 
}
