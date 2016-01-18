package com.geopista.ui.dialogs.global;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL_LCGIII;

public class Constants {
	
	//tipos especiales
	public static final String LICENCIAS = "LICENCIAS";
	public static final String EIEL = "EIEL";
	
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
	
	//tipos EIEL
	public static final String[] TIPOS_EIEL = {
		ConstantesLocalGISEIEL_LCGIII.ABASTECIMIENTO_AUTONOMO_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CABILDO_CONSEJO_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CAPTACIONES_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CARRETERA_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CASAS_CONSISTORIALES_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CEMENTERIOS_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CENTROS_ASISTENCIALES_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CENTROS_CULTURALES_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CENTROS_ENSENIANZA_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CENTROS_SANITARIOS_LAYER,
		ConstantesLocalGISEIEL_LCGIII.COLECTOR_LAYER,
		//ConstantesLocalGISEIEL_LCGIII.COMARCA_LAYER,
		ConstantesLocalGISEIEL_LCGIII.DEPOSITOS_LAYER,
		ConstantesLocalGISEIEL_LCGIII.DEPURADORAS1_LAYER,
		ConstantesLocalGISEIEL_LCGIII.DEPURADORAS2_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.DISEMINADOS_LAYER,
		ConstantesLocalGISEIEL_LCGIII.EDIFICIOS_SIN_USO_LAYER,
		//REVISAR
//		ConstantesLocalGISEIEL_LCGIII.ELEMENTO_PUNTUAL_ABASTECIMIENTO_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.ELEM_PUNTUAL_LAYER,
		//FIN REVISAR
//		ConstantesLocalGISEIEL_LCGIII.EMISARIOS_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.ENCUESTADOS1_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.ENCUESTADOS2_LAYER,		
//		ConstantesLocalGISEIEL_LCGIII.ENTIDADES_SINGULARES_LAYER,
		ConstantesLocalGISEIEL_LCGIII.INCENDIOS_PROTECCION_LAYER,
		ConstantesLocalGISEIEL_LCGIII.INSTALACIONES_DEPORTIVAS_LAYER,
		ConstantesLocalGISEIEL_LCGIII.LONJAS_MERCADOS_LAYER,
		ConstantesLocalGISEIEL_LCGIII.MATADEROS_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.MUNICIPIO_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.NUCLEO_ENCT_7_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.NUCLEOS_ABANDONADOS_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.NUCLEOS_POBLACION_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.OTROS_SERVICIOS_MUNICIPALES_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.PADRON_MUNICIPIOS_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.PADRON_NUCLEOS_LAYER,
		ConstantesLocalGISEIEL_LCGIII.PARQUES_JARDINES_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.PLANEAMIENTO_URBANO_LAYER,
		ConstantesLocalGISEIEL_LCGIII.PUNTOS_VERTIDO_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.RECOGIDA_BASURAS_LAYER,
		ConstantesLocalGISEIEL_LCGIII.SANEAMIENTO_AUTONOMO_LAYER,
		ConstantesLocalGISEIEL_LCGIII.SERVICIOS_ABASTECIMIENTOS_LAYER,
//		ConstantesLocalGISEIEL_LCGIII.SERVICIOS_RECOGIDA_BASURA_LAYER,
		ConstantesLocalGISEIEL_LCGIII.SERVICIOS_SANEAMIENTO_LAYER,
		ConstantesLocalGISEIEL_LCGIII.TANATORIO_LAYER,
		ConstantesLocalGISEIEL_LCGIII.CARRETERA_LAYER,
		ConstantesLocalGISEIEL_LCGIII.TRAMOS_CONDUCCIONES_LAYER,
		ConstantesLocalGISEIEL_LCGIII.TRATAMIENTOS_POTABILIZACION_LAYER,
		ConstantesLocalGISEIEL_LCGIII.VERTEDERO_LAYER
	};	
	
	//atributos
	public static final String REFERENCIA_CATASTRAL = "ref_catastral";
	public static final String NUM_BIENES_ID = "id2";
	public static final String NUM_BIENES = "Número de Bienes";
	public static final String NUM_BIENES_2 = "Numero de Bienes";
	public static final String CLAVE_METAINFO = "catastral";
			
	//constantes de los xml skeleton
	public static final String ATT_I18NKEY = "i18nKey";
	public static final String ATT_REFLECTMETHOD = "rm";
	public static final String ATT_NAME = "name";
	public static final String ATT_CLASSID = "classId";
	public static final String ATT_LABEL = "label";
	public static final String TAG_ITEM = "item";
	public static final String TAG_ITEMLIST = "itemlist";
	public static final String TAG_SUBITEM = "subitem";

	public static int ObraMayor_Value = 0;
	public static int ObraMenor_Value = 1;
	public static int Actividades_Value = 2;
    public static int ActividadesNoCalificadas_Value = 3;
	
	//beans especificos de inventario
	public static final String CLASSID_EXPEDIENTE = "CExpedienteLicencia";
	public static final String CLASSID_SOLICITUD = "CSolicitudLicencia";
	public static final String CLASSID_ACTIVIDAD = "DatosActividad";
	public static final String CLASSID_INVENTARIO = "BienBean";
	public static final String CLASSID_INVENTARIO_INMUEBLE = "InmuebleBean";
	public static final String CLASSID_INVENTARIO_SEMOVIENTE = "SemovienteBean";
	public static final String CLASSID_INVENTARIO_VEHICULO = "VehiculoBean";
	public static final String CLASSID_INVENTARIO_VALORMOBILIARIO = "ValorMobiliarioBean";
	public static final String CLASSID_INVENTARIO_DERECHOREAL = "DerechoRealBean";
	public static final String CLASSID_INVENTARIO_CREDITODERECHO = "CreditoDerechoBean";
	public static final String CLASSID_INVENTARIO_MUEBLE = "MuebleBean";
	public static final String CLASSID_INVENTARIO_VIA = "ViaBean";

	//beans especificos de EIEL	
	public static final String CLASSID_ABASTECIMIENTO_AUTONOMO = "AbastecimientoAutonomoEIEL";	
	public static final String CLASSID_CABILDOCONSEJO = "CabildoConsejoEIEL";	
	public static final String CLASSID_CAPTACIONES = "CaptacionesEIEL";		
	public static final String CLASSID_CASASCONSISTORIALES  = "CasasConsistorialesEIEL"; 	
	public static final String CLASSID_CEMENTERIOS  = "CementeriosEIEL"; 
	public static final String CLASSID_CENTROSASISTENCIALES = "CentrosAsistencialesEIEL"; 
	public static final String CLASSID_CENTROSCULTURALES = "CentrosCulturalesEIEL"; 
	public static final String CLASSID_CENTROSENSENIANZA = "CentrosEnsenianzaEIEL";
	public static final String CLASSID_CENTROSSANITARIOS = "CentrosSanitariosEIEL";
	public static final String CLASSID_COLECTOR = "ColectorEIEL";
	public static final String CLASSID_COMARCA = "ComarcaEIEL";		
	public static final String CLASSID_DEPOSITOS = "DepositosEIEL";	
	public static final String CLASSID_DEPURADORAS1 = "Depuradora1EIEL";
	public static final String CLASSID_DEPURADORAS2 = "Depuradora2EIEL";
	public static final String CLASSID_DISEMINADOS	= "DiseminadosEIEL";
	public static final String CLASSID_EDIFICIOSSINUSO = "EdificiosSinUsoEIEL";
	//REVISAR
	public static final String CLASSID_ELEMEN_PUNTUALES_ABASTECIMIENTO = "ElementoPuntualAbastecimientoEIEL";
	public static final String CLASSID_ELEMEN_PUNTUALES_SANEAMIENTO = "ElementoPuntualSaneamientoEIEL";
	//FIN REVISAR
	public static final String CLASSID_EMISARIOS = "EmisariosEIEL";
	public static final String CLASSID_ENCUESTADOS1 = "Encuestados1EIEL";
	public static final String CLASSID_ENCUESTADOS2 = "Encuestados2EIEL";
	public static final String CLASSID_ENTIDADES = "EntidadEIEL";
	public static final String CLASSID_ENTIDADES_SINGULARES = "EntidadesSingularesEIEL";
	public static final String CLASSID_INCENDIOS_PROTECCION = "IncendiosProteccionEIEL";
	public static final String CLASSID_INSTALACIONES_DEPORTIVAS = "InstalacionesDeportivasEIEL";
	public static final String CLASSID_LIMPIEZA_CALLES = "LimpiezaCallesEIEL";
	public static final String CLASSID_LONJAS_MERCADOS = "LonjasMercadosEIEL";
	public static final String CLASSID_MATADEROS = "MataderosEIEL";
	public static final String CLASSID_MUNICIPIO = "MunicipioEIEL";
	public static final String CLASSID_NUCLEOENCUESTADO7 = "NucleoEncuestado7EIEL";
	public static final String CLASSID_NUCLEOS_ABANDONADOS = "NucleosAbandonadosEIEL";
	public static final String CLASSID_NUCLEOS_POBLACION = "NucleosPoblacionEIEL";
	public static final String CLASSID_OTROS_SERVICIOS_MUNICIPALES = "OtrosServMunicipalesEIEL";
	public static final String CLASSID_PADRON_MUNICIPIOS = "PadronMunicipiosEIEL";
	public static final String CLASSID_PADRON_NUCLEOS = "PadronNucleosEIEL";
	public static final String CLASSID_PARQUESJARDINES = "ParquesJardinesEIEL";
	public static final String CLASSID_PLANEAMIENTO_URBANO = "PlaneamientoUrbanoEIEL";
	public static final String CLASSID_PUNTOSDEVERTIDO = "PuntosVertidoEIEL";
	public static final String CLASSID_RECOGIDABASURA = "RecogidaBasurasEIEL";
	public static final String CLASSID_SANEAMIENTOAUTONOMO = "SaneamientoAutonomoEIEL";
	public static final String CLASSID_SERVICIOS_ABASTECIMIENTOS = "ServiciosAbastecimientosEIEL";
	public static final String CLASSID_SERVICIOS_RECOGIDA_BASURA = "ServiciosRecogidaBasuraEIEL";
	public static final String CLASSID_SERVICIOS_SANEAMIENTO = "ServiciosSaneamientoEIEL";	
	public static final String CLASSID_TANATORIOS = "TanatoriosEIEL";	
	public static final String CLASSID_TRAMOS_CARRETERAS = "TramosCarreterasEIEL";	
	public static final String CLASSID_TRAMOS_CONDUCCION = "TramosConduccionEIEL"; 
	public static final String CLASSID_TRATAMIENTOSPOTABILIZACION = "TratamientosPotabilizacionEIEL"; 
	public static final String CLASSID_VERTEDEROS = "VertederosEIEL";
	
	
	public static final String[] TIPOS_CLASSID_EIEL = {
		CLASSID_ABASTECIMIENTO_AUTONOMO,
		CLASSID_CABILDOCONSEJO,
		CLASSID_CAPTACIONES,
		CLASSID_CASASCONSISTORIALES,
		CLASSID_CEMENTERIOS,
		CLASSID_CENTROSASISTENCIALES,
		CLASSID_CENTROSCULTURALES,
		CLASSID_CENTROSENSENIANZA,
		CLASSID_CENTROSSANITARIOS,
		CLASSID_COLECTOR,
		CLASSID_COMARCA,
		CLASSID_DEPOSITOS,
		CLASSID_DEPURADORAS1,
		CLASSID_DEPURADORAS2, 
//		CLASSID_DISEMINADOS,
		CLASSID_EDIFICIOSSINUSO,
		//REVISAR
//		CLASSID_ELEMEN_PUNTUALES_ABASTECIMIENTO,
//		CLASSID_ELEMEN_PUNTUALES_SANEAMIENTO,
		//FIN REVISAR
//		CLASSID_EMISARIOS,
//		CLASSID_ENCUESTADOS1,
//		CLASSID_ENCUESTADOS2,		
//		CLASSID_ENTIDADES,
//		CLASSID_ENTIDADES_SINGULARES,
		CLASSID_INCENDIOS_PROTECCION,
		CLASSID_INSTALACIONES_DEPORTIVAS,
//		CLASSID_LIMPIEZA_CALLES,
		CLASSID_LONJAS_MERCADOS,
		CLASSID_MATADEROS,
		CLASSID_MUNICIPIO,
//		CLASSID_NUCLEOENCUESTADO7,
//		CLASSID_NUCLEOS_ABANDONADOS,
//		CLASSID_NUCLEOS_POBLACION,
//		CLASSID_OTROS_SERVICIOS_MUNICIPALES,
//		CLASSID_PADRON_MUNICIPIOS,
//		CLASSID_PADRON_NUCLEOS,
		CLASSID_PARQUESJARDINES,
//		CLASSID_PLANEAMIENTO_URBANO,
		CLASSID_PUNTOSDEVERTIDO,
//		CLASSID_RECOGIDABASURA,
		CLASSID_SANEAMIENTOAUTONOMO,
		CLASSID_SERVICIOS_ABASTECIMIENTOS,
//		CLASSID_SERVICIOS_RECOGIDA_BASURA,
		CLASSID_SERVICIOS_SANEAMIENTO,
		CLASSID_TANATORIOS,
		CLASSID_TRAMOS_CARRETERAS,
		CLASSID_TRAMOS_CONDUCCION,
		CLASSID_TRATAMIENTOSPOTABILIZACION,
		CLASSID_VERTEDEROS
	};
		
	//constantes generales
	public static AppContext APLICACION = (AppContext) AppContext.getApplicationContext();
	private static final String _ADD_URL_TOMCAT = "/localgismobile/UploadProject.do";
	private static final String _DELETE_URL_TOMCAT = "/localgismobile/DeleteProject.do";

	public static String URL_TOMCAT =APLICACION.getUserPreference(AppContext.URL_TOMCAT,"",true) + _ADD_URL_TOMCAT;
	public static String URL_DELETE_TOMCAT =APLICACION.getUserPreference(AppContext.URL_TOMCAT,"",true) + _DELETE_URL_TOMCAT;

	//Campos
	public static final String FIELD_ID = "id";	
	
	//Campos EIEL
	public static final String FIELD_ID_MUNICIPIO = "id_municipio";	
	public static final String FIELD_CLAVE = "clave";	
	public static final String FIELD_CODPROV = "codprov";	
	public static final String FIELD_CODMUNIC = "codmunic";	
	
//	public static final String FIELD_ORDEN_CA = "orden_ca";	
//	
//	public static final String FIELD_ORDEN_DE = "orden_de";	
//	
//	public static final String FIELD_COD_CARRETERA = "cod_carrt";
//	public static final String FIELD_PKI = "pki";
//	public static final String FIELD_PKF = "pkf";
//	public static final String FIELD_GESTOR = "gestor";
	
		
//	public static final String [] FIELDS_CA = {FIELD_CLAVE, FIELD_CODPROV, FIELD_CODMUNIC, FIELD_ORDEN_CA};
//	public static final String [] FIELDS_DE = {FIELD_CLAVE, FIELD_CODPROV, FIELD_CODMUNIC, FIELD_ORDEN_DE};
//	public static final String [] FIELDS_TC = {FIELD_CODPROV, FIELD_COD_CARRETERA, FIELD_PKI, FIELD_PKF, FIELD_GESTOR};
	
	//public static final String [] EIEL_ONLY_READ_FIELDS = {FIELD_CLAVE, FIELD_CODPROV, FIELD_CODMUNIC};
	//public static final String [] EIEL_ONLY_READ_FIELDS = {};                           
}
