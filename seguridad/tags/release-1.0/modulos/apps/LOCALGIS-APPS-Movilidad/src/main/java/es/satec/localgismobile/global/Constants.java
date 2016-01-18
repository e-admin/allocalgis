package es.satec.localgismobile.global;

public class Constants {
	
	public static final String GRATICULE_LAYER_NAME="graticulemobile";
		
	public final static String DOMAINFIELD_ID = "id";	
	public final static String DOMAINFIELD_ID_MUNICIPIO = "IdMunicip";	
	
	//tipos de licencias
	public static final String LIC_OBRA_MENOR = "licencias_obra_menor";
	public static final String LIC_OBRA_MAYOR = "licencias_obra_mayor";
	public static final String LIC_ACTIVIDAD = "licencias_actividad";
	public static final String INV_PARCELAS = "inventario_parcelas";
	public static final String INV_VIAS = "inventario_vias";
	public static final String ACTIVIDADES_CONTAMINANTES = "actividades_contaminantes";
	public static final String VERTEDERO = "Vertedero";
	public static final String ARBOLEDA = "Arboleada";
	
	public static final String[] TIPOS_LICENCIAS = {LIC_OBRA_MENOR,LIC_OBRA_MAYOR,LIC_ACTIVIDAD};
	public static final String[] TIPOS_CONTAMINANTES = {ACTIVIDADES_CONTAMINANTES,VERTEDERO,ARBOLEDA};
	public static final String[] TIPOS_INVENTARIO = {INV_PARCELAS,INV_VIAS};
	
	//Capas EIEL
	public static final String CAPTACIONES_LAYER = "CA";
	public static final String DEPOSITOS_LAYER = "DE";
	public static final String DEPURADORAS_LAYER = "ED";
	public static final String NUCLEO_LAYER = "NP";
	public static final String TRAMOS_CONDUCCIONES_LAYER = "TCN";
	public static final String TRATAMIENTOS_POTABILIZACION_LAYER = "TP";
	public static final String EMISARIOS_LAYER = "TEM";
	public static final String COLECTOR_LAYER = "TCL";
	public static final String DEPURADORAS1_LAYER = "ED";
	public static final String DEPURADORAS2_LAYER = "ED";
	public static final String RED_RAMALES_LAYER = "RS";
	public static final String RED_DISTRIBUCION_LAYER = "RD";
	public static final String PUNTOS_VERTIDO_LAYER = "PV";
	public static final String ELEMENTO_PUNTUAL_ABASTECIMIENTO_LAYER = "AR";
	public static final String ELEM_PUNTUAL_LAYER = "PR";
	public static final String INSTALACIONES_DEPORTIVAS_LAYER = "ID";
	public static final String CENTROS_CULTURALES_LAYER = "CU";
	public static final String PARQUES_JARDINES_LAYER = "PJ";
	public static final String LONJAS_MERCADOS_LAYER = "LM";
	public static final String MATADEROS_LAYER = "MT";
	public static final String CEMENTERIOS_LAYER = "CE";
	public static final String TANATORIO_LAYER = "TN";
	public static final String CENTROS_SANITARIOS_LAYER = "SA";
	public static final String CENTROS_ASISTENCIALES_LAYER = "ASL";
	public static final String CENTROS_ENSENIANZA_LAYER = "EN";
	public static final String CASAS_CONSISTORIALES_LAYER = "CC";
	public static final String INCENDIOS_PROTECCION_LAYER = "IP";
	public static final String EDIFICIOS_SIN_USO_LAYER = "SU";
	public static final String INF_VIARIA_LAYER = "TU";
	public static final String CARRETERA_LAYER = "carreteras";
	public static final String CUADRO_MANDO_LAYER = "CMP";
	public static final String ESTABILIZADOR_LAYER = "eea";
	public static final String PUNTO_LUZ_LAYER = "PL";
	public static final String VERTEDERO_LAYER = "VT";
	public static final String PARROQUIA_LAYER = "parroquias";
	public static final String EDIFICIO_SING_LAYER = "edificiosing";
	public static final String COMARCA_LAYER = "CO";
	public static final String MUNICIPIO_LAYER = "TTMM";
	public static final String MUNICIPIOS_LAYER = "TTMM";
	public static final String NUCLEOS_POBLACION_LAYER = "NP";
	public static final String POBLAMIENTO_LAYER = "NP";
	public static final String ENCUESTADOS1_LAYER = "NP";
	public static final String ENCUESTADOS2_LAYER = "NP";
	public static final String RECOGIDA_BASURAS_LAYER = "NP";
	public static final String SANEAMIENTO_AUTONOMO_LAYER = "SN";
	public static final String SERVICIOS_RECOGIDA_BASURA_LAYER = "NP";
	public static final String NUCLEOS_ABANDONADOS_LAYER = "NP";
	public static final String NUCLEO_ENCT_7_LAYER = "N7";
	public static final String PROVINCIA_LAYER = "Provincia";		
	public static final String ABASTECIMIENTO_AUTONOMO_LAYER = "AB";
	public static final String DISEMINADOS_LAYER = "DI";
	public static final String ENTIDADES_SINGULARES_LAYER = "SI";
	public static final String INFO_TERMINOS_MUNICIPALES_LAYER = "TM";
	public static final String OTROS_SERVICIOS_MUNICIPALES_LAYER = "OS";
	public static final String PADRON_MUNICIPIOS_LAYER = "PM";
	public static final String PADRON_NUCLEOS_LAYER = "PN";
	public static final String PLANEAMIENTO_URBANO_LAYER = "PU";
	public static final String SERVICIOS_SANEAMIENTO_LAYER = "SS";
	public static final String SERVICIOS_ABASTECIMIENTOS_LAYER = "SA";
	public static final String CABILDO_CONSEJO_LAYER = "CI";
	
	public static final String[] TIPOS_EIEL = {
		ABASTECIMIENTO_AUTONOMO_LAYER,
		CABILDO_CONSEJO_LAYER,
		CAPTACIONES_LAYER,
		CARRETERA_LAYER,
		CASAS_CONSISTORIALES_LAYER,
		CEMENTERIOS_LAYER,
		CENTROS_ASISTENCIALES_LAYER,
		CENTROS_CULTURALES_LAYER,
		CENTROS_ENSENIANZA_LAYER,
		CENTROS_SANITARIOS_LAYER,
		COLECTOR_LAYER,
//		COMARCA_LAYER,
		DEPOSITOS_LAYER,
		DEPURADORAS1_LAYER,
		DEPURADORAS2_LAYER,
//		DISEMINADOS_LAYER,
		EDIFICIOS_SIN_USO_LAYER,
		//REVISAR
//		ELEMENTO_PUNTUAL_ABASTECIMIENTO_LAYER,
//		ELEM_PUNTUAL_LAYER,
		//FIN REVISAR
//		EMISARIOS_LAYER,
		ENCUESTADOS1_LAYER,
		ENCUESTADOS2_LAYER,
//		ENTIDADES_SINGULARES_LAYER,
		INCENDIOS_PROTECCION_LAYER,
		INSTALACIONES_DEPORTIVAS_LAYER,
		LONJAS_MERCADOS_LAYER,
		MATADEROS_LAYER,
//		MUNICIPIO_LAYER,
//		NUCLEO_ENCT_7_LAYER,
//		NUCLEOS_ABANDONADOS_LAYER,
//		NUCLEOS_POBLACION_LAYER,
//		OTROS_SERVICIOS_MUNICIPALES_LAYER,
//		PADRON_MUNICIPIOS_LAYER,
//		PADRON_NUCLEOS_LAYER,
		PARQUES_JARDINES_LAYER,
//		PLANEAMIENTO_URBANO_LAYER,
		PUNTOS_VERTIDO_LAYER,
		RECOGIDA_BASURAS_LAYER,
		SANEAMIENTO_AUTONOMO_LAYER,
		SERVICIOS_ABASTECIMIENTOS_LAYER,
		SERVICIOS_RECOGIDA_BASURA_LAYER,
		SERVICIOS_SANEAMIENTO_LAYER,
		TANATORIO_LAYER,
		CARRETERA_LAYER,
		TRAMOS_CONDUCCIONES_LAYER,
		TRATAMIENTOS_POTABILIZACION_LAYER,
		VERTEDERO_LAYER
	};	
			
//	public final static String DOMAINFIELD_CODPROVINCIA = "Código provincia";
//	public final static String DOMAINFIELD_CODMUNICIPIO = "Código Municipio";
//	public final static String DOMAINFIELD_CODENTIDAD = "Código INE Entidad";
//	public final static String DOMAINFIELD_CODNUCLEO = "Código INE Núcleo";
//	public final static String DOMAINFIELD_CLAVE = "Clave";
//	
//	public final static String FIELD_CLAVE = "clave";
//	public final static String FIELD_CODPROV = "codprov";
//	public final static String FIELD_CODMUNIC = "codmunic";
//	public final static String FIELD_CODENTIDAD = "codentidad";
//	public final static String FIELD_CODNUCLEO = "codpoblamiento";
//	public final static String FIELD_ORDEN = "orden_";
//	public final static String FIELD_TRAMO = "tramo_";
//	public final static String FIELD_PKI = "pki";
//	public final static String FIELD_PKF = "pkf";
	
	
	//public final static String FIELD_ORDEN_CA = "orden_ca";
	//public final static String FIELD_ORDEN_DE = "orden_de";
	
	
	//public static final String [] EIEL_NON_VISIBLE_DOMAINFIELDS = {DOMAINFIELDS_ID, DOMAINFIELDS_CODPROVINCIA, DOMAINFIELDS_CODMUNICIPIO};
	//public static final String [] EIEL_NON_VISIBLE_DOMAINFIELDS = {DOMAINFIELD_ID, DOMAINFIELD_ID_MUNICIPIO};
	//public static final String [] EIEL_PK_FIELDS = {FIELD_CLAVE};//,FIELDS_ORDEN};
	//public static final String [] EIEL_PK_FIELDS = {FIELD_CLAVE,FIELD_CODPROV,FIELD_CODMUNIC,FIELD_CODENTIDAD,FIELD_CODNUCLEO,FIELD_ORDEN,FIELD_TRAMO,FIELD_PKI,FIELD_PKF};
	//public static final String [] EIEL_PK_FIELDS_ORDEN = {FIELD_ORDEN_CA,FIELD_ORDEN_DE};
}
