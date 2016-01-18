/**
 * Constants.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.global;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.global.WebAppConstants;

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
		ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_LAYER,
		ConstantesLocalGISEIEL.CABILDO_CONSEJO_LAYER,
		ConstantesLocalGISEIEL.CAPTACIONES_LAYER,
		ConstantesLocalGISEIEL.CARRETERA_LAYER,
		ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER,
		ConstantesLocalGISEIEL.CEMENTERIOS_LAYER,
		ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER,
		ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER,
		ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER,
		ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER,
		ConstantesLocalGISEIEL.COLECTOR_LAYER,
		//ConstantesLocalGISEIEL.COMARCA_LAYER,
		ConstantesLocalGISEIEL.DEPOSITOS_LAYER,
		ConstantesLocalGISEIEL.DEPURADORAS1_LAYER,
		ConstantesLocalGISEIEL.DEPURADORAS2_LAYER,
//		ConstantesLocalGISEIEL.DISEMINADOS_LAYER,
		ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER,
		//REVISAR
//		ConstantesLocalGISEIEL.ELEMENTO_PUNTUAL_ABASTECIMIENTO_LAYER,
//		ConstantesLocalGISEIEL.ELEM_PUNTUAL_LAYER,
		//FIN REVISAR
//		ConstantesLocalGISEIEL.EMISARIOS_LAYER,
//		ConstantesLocalGISEIEL.ENCUESTADOS1_LAYER,
//		ConstantesLocalGISEIEL.ENCUESTADOS2_LAYER,		
//		ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_LAYER,
		ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER,
		ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER,
		ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER,
		ConstantesLocalGISEIEL.MATADEROS_LAYER,
//		ConstantesLocalGISEIEL.MUNICIPIO_LAYER,
//		ConstantesLocalGISEIEL.NUCLEO_ENCT_7_LAYER,
//		ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_LAYER,
//		ConstantesLocalGISEIEL.NUCLEOS_POBLACION_LAYER,
//		ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES_LAYER,
//		ConstantesLocalGISEIEL.PADRON_MUNICIPIOS_LAYER,
//		ConstantesLocalGISEIEL.PADRON_NUCLEOS_LAYER,
		ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER,
//		ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO_LAYER,
		ConstantesLocalGISEIEL.PUNTOS_VERTIDO_LAYER,
//		ConstantesLocalGISEIEL.RECOGIDA_BASURAS_LAYER,
		ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO_LAYER,
		ConstantesLocalGISEIEL.SERVICIOS_ABASTECIMIENTOS_LAYER,
//		ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_LAYER,
		ConstantesLocalGISEIEL.SERVICIOS_SANEAMIENTO_LAYER,
		ConstantesLocalGISEIEL.TANATORIO_LAYER,
		ConstantesLocalGISEIEL.CARRETERA_LAYER,
		ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER,
		ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER,
		ConstantesLocalGISEIEL.VERTEDERO_LAYER
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
	private static final String _ADD_URL_SERVER = WebAppConstants.MOVILIDAD_WEBAPP_NAME + "/UploadProject.do";
	private static final String _DELETE_URL_TOMCAT = WebAppConstants.MOVILIDAD_WEBAPP_NAME + "/DeleteProject.do";

	public static String URL_SERVER = APLICACION.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + _ADD_URL_SERVER;
	public static String URL_DELETE_SERVER = APLICACION.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + _DELETE_URL_TOMCAT;

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
