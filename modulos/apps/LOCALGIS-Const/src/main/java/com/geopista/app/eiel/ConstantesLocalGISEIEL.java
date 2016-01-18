/**
 * ConstantesLocalGISEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;

import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;


/**
 * Clase utilizada por el cliente y el servidor que define las constantes de las
 * acciones que el cliente desea hacer en la parte servidora. En la parte
 * servidora se comparara la accion obtenida y se realizara la accion deseada.
 * El resto de constantes son para permitir obtener los objetos de la hash
 * params en el envio de la peticion al servidor o utilzadas en el cliente y
 * servidor.
 * */

public class ConstantesLocalGISEIEL {
	/** Usuario de login */
	
	public static String loginEIEL = "http://localhost:54321/eiel/";
	public static String servletEIELUrl = "http://localhost:54321/eiel/EIELServlet";

	public static final String idApp = "EIEL";
	
	public static DateFormat df= new SimpleDateFormat("dd-MM-yyyy");
	//public static GeopistaPrincipal principal;
	public static Hashtable permisos = new Hashtable();

	// Acciones sobre BD.

	//
	public static String Locale = "es_ES";
	public static String LocalCastellano = "es_ES";
	//
	public static int IdEntidad = -1;
	//public static int IdMunicipio = 34083;
	public static String Provincia = null;
	public static String Municipio = null;

	public static String idProvincia = null;
	public static String idMunicipio = "34083";
	public static String idEntidad = null;

	public static String helpSetHomeID = "EIELIntro";

	public static final String NO_INICIALIZADO = "NoInicializado";

	public static final String FILTRO = "Filtro";
	public static final String PATRON = "Patron";
	public static final String PATRONES = "Patrones";
	public static final String MUNICIPIO_SELECCIONADO = "MunicipioSeleccionado";
	public static final String MUNICIPIO_BUSQUEDA= "MunicipioBusqueda";
	public static final String NUCLEO_SELECCIONADO= "NucleoSeleccionado";
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
	public static final String ID_ENTIDAD_ORIGINAL = "IdEntidadOriginal";
	
	public static final String AÑO_ENCUESTA= "AÑO_ENCUESTA";

	public static final String NOMBRE_TABLA = "NombreTabla";
	public static final String CAMPOS_ESPECIFICOS="CamposEspecificos";

	// Tablas
	public static final String CAPTACIONES = "CA";
	public static final String DEPURADORAS1 = "D1";
	public static final String DEPURADORAS2 = "D2";
	public static final String ABASTECIMIENTO_AUTONOMO = "AU";
	public static final String CASAS_CONSISTORIALES = "CC";
	public static final String CENTROS_CULTURALES = "CU";
	public static final String CENTROS_ENSENIANZA = "EN";
	public static final String INSTALACIONES_DEPORTIVAS = "ID";
	public static final String DEPOSITOS = "DE";
	public static final String PUNTOS_VERTIDO = "PV";
	// public static final String PUNTOS_VERTIDO_EMISOR = "PVE";
	public static final String DATOS_SERVICIOS_ABASTECIMIENTOS = "DSA";
	public static final String CENTROS_ASISTENCIALES = "AS";
	public static final String CABILDO = "CI";
	public static final String CEMENTERIOS = "CE";
	public static final String ENTIDADES_SINGULARES = "SI";
	public static final String NUCLEO_ENCT_7 = "N7";
	public static final String INCENDIOS_PROTECCION = "IP";
	public static final String LONJAS_MERCADOS = "LM"; //"LO"
	public static final String MATADEROS = "MT";
	public static final String TRATAMIENTOS_POTABILIZACION = "TP";
	public static final String DISEMINADOS = "DI";
	public static final String NUCLEOS_POBLACION = "NP";

	public static final String C_NUCLEOS_POBLACION = "CNP";

	public static final String OTROS_SERVICIOS_MUNICIPALES = "OS";
	public static final String PADRON_NUCLEOS = "PN";
	public static final String PADRON_MUNICIPIOS = "PM";
	public static final String PARQUES_JARDINES = "PJ";
	public static final String PLANEAMIENTO_URBANO = "PU";
	public static final String POBLAMIENTO = "PO";
	public static final String RECOGIDA_BASURAS = "RB";
	public static final String CENTROS_SANITARIOS = "CSAN";//"CS";
	public static final String SANEAMIENTO_AUTONOMO = "SN";
	public static final String DATOS_SERVICIOS_SANEAMIENTO = "DSS";
	public static final String SERVICIOS_RECOGIDA_BASURA = "SR";
	public static final String EDIFICIOS_SIN_USO = "SU";//"ES";
	public static final String TANATORIOS = "TA";
	public static final String DATOS_VERTEDEROS = "VT"; //"VE";
	// public static final String RED_CARRETERAS = "RC";
	public static final String TRAMOS_CARRETERAS = "TC";

	public static final String ENCUESTADOS1 = "P1";
	public static final String ENCUESTADOS2 = "P2";
	public static final String NUCLEOS_ABANDONADOS = "NA";

	public static final String EMISARIOS = "EM";
	public static final String TCONDUCCION = "CN";
	public static final String TCOLECTOR = "CL";	
	public static final String AGRUPACIONES6000 = "A6";		
	
	public static final String INFO_PADRON = "INFO_PADRON";
	
	//Elementos sin datos alfanumericos
	public static final String INFRAESTRUCTURAS_VIARIAS="IV";
	
	
	// Models
	public static final String CAPTACIONES_MODEL = "com.geopista.app.eiel.models.CaptacionesEIELTableModel";
	public static final String CAPTACIONES_MODEL_COMPLETO = "com.geopista.app.eiel.models.CaptacionesCompletoEIELTableModel";
	public static final String ABASTECIMIENTO_AUTONOMO_MODEL = "com.geopista.app.eiel.models.AbastecimientoAutonomoEIELTableModel";
	public static final String ABASTECIMIENTO_AUTONOMO_MODEL_COMPLETO = "com.geopista.app.eiel.models.AbastecimientoAutonomoCompletoEIELTableModel";
	public static final String DEPURADORAS1_MODEL = "com.geopista.app.eiel.models.Depuradora1EIELTableModel";
	public static final String DEPURADORAS1_MODEL_COMPLETO = "com.geopista.app.eiel.models.Depuradora1CompletoEIELTableModel";
	public static final String DEPURADORAS2_MODEL = "com.geopista.app.eiel.models.Depuradora2EIELTableModel";
	public static final String DEPURADORAS2_MODEL_COMPLETO = "com.geopista.app.eiel.models.Depuradora2CompletoEIELTableModel";
	public static final String CASAS_CONSISTORIALES_MODEL = "com.geopista.app.eiel.models.CasasConsistorialesEIELTableModel";
	public static final String CASAS_CONSISTORIALES_MODEL_COMPLETO = "com.geopista.app.eiel.models.CasasConsistorialesCompletoEIELTableModel";
	public static final String CENTROS_CULTURALES_MODEL = "com.geopista.app.eiel.models.CentrosCulturalesEIELTableModel";
	public static final String CENTROS_CULTURALES_MODEL_COMPLETO = "com.geopista.app.eiel.models.CentrosCulturalesCompletoEIELTableModel";
	public static final String CENTROS_ENSENIANZA_MODEL = "com.geopista.app.eiel.models.CentrosEnsenianzaEIELTableModel";
	public static final String CENTROS_ENSENIANZA_MODEL_COMPLETO = "com.geopista.app.eiel.models.CentrosEnsenianzaCompletoEIELTableModel";
	public static final String INSTALACIONES_DEPORTIVAS_MODEL = "com.geopista.app.eiel.models.InstalacionesDeportivasEIELTableModel";
	public static final String INSTALACIONES_DEPORTIVAS_MODEL_COMPLETO = "com.geopista.app.eiel.models.InstalacionesDeportivasCompletoEIELTableModel";
	public static final String OTROS_SERVICIOS_MUNICIPALES_MODEL = "com.geopista.app.eiel.models.OtrosServMunicipalesEIELTableModel";
	public static final String OTROS_SERVICIOS_MUNICIPALES_MODEL_COMPLETO = "com.geopista.app.eiel.models.OtrosServMunicipalesCompletoEIELTableModel";
	public static final String PADRON_NUCLEOS_MODEL = "com.geopista.app.eiel.models.PadronNucleosEIELTableModel";
	public static final String PADRON_NUCLEOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.PadronNucleosCompletoEIELTableModel";
	public static final String PADRON_MUNICIPIOS_MODEL = "com.geopista.app.eiel.models.PadronMunicipiosEIELTableModel";
	public static final String PADRON_MUNICIPIOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.PadronMunicipiosCompletoEIELTableModel";
	public static final String PARQUES_JARDINES_MODEL = "com.geopista.app.eiel.models.ParquesJardinesEIELTableModel";
	public static final String PARQUES_JARDINES_MODEL_COMPLETO = "com.geopista.app.eiel.models.ParquesJardinesCompletoEIELTableModel";
	public static final String PLANEAMIENTO_URBANO_MODEL = "com.geopista.app.eiel.models.PlaneamientoUrbanoEIELTableModel";
	public static final String PLANEAMIENTO_URBANO_MODEL_COMPLETO = "com.geopista.app.eiel.models.PlaneamientoUrbanoCompletoEIELTableModel";
	public static final String POBLAMIENTO_MODEL = "com.geopista.app.eiel.models.PoblamientoEIELTableModel";
	public static final String POBLAMIENTO_MODEL_COMPLETO = "com.geopista.app.eiel.models.PoblamientoCompletoEIELTableModel";
	public static final String RECOGIDA_BASURAS_MODEL = "com.geopista.app.eiel.models.RecogidaBasurasEIELTableModel";
	public static final String RECOGIDA_BASURAS_MODEL_COMPLETO = "com.geopista.app.eiel.models.RecogidaBasurasCompletoEIELTableModel";
	public static final String CENTROS_SANITARIOS_MODEL = "com.geopista.app.eiel.models.CentrosSanitariosEIELTableModel";
	public static final String CENTROS_SANITARIOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.CentrosSanitariosCompletoEIELTableModel";
	public static final String SANEAMIENTO_AUTONOMO_MODEL = "com.geopista.app.eiel.models.SaneamientoAutonomoEIELTableModel";
	public static final String SANEAMIENTO_AUTONOMO_MODEL_COMPLETO = "com.geopista.app.eiel.models.SaneamientoAutonomoCompletoEIELTableModel";
	public static final String SERVICIOS_SANEAMIENTO_MODEL = "com.geopista.app.eiel.models.ServiciosSaneamientoEIELTableModel";
	public static final String SERVICIOS_SANEAMIENTO_MODEL_COMPLETO = "com.geopista.app.eiel.models.ServiciosSaneamientoCompletoEIELTableModel";
	public static final String SERVICIOS_RECOGIDA_BASURA_MODEL = "com.geopista.app.eiel.models.ServiciosRecogidaBasuraEIELTableModel";
	public static final String SERVICIOS_RECOGIDA_BASURA_MODEL_COMPLETO = "com.geopista.app.eiel.models.ServiciosRecogidaBasuraCompletoEIELTableModel";
	public static final String EDIFICIOS_SIN_USO_MODEL = "com.geopista.app.eiel.models.EdificiosSinUsoEIELTableModel";
	public static final String EDIFICIOS_SIN_USO_MODEL_COMPLETO = "com.geopista.app.eiel.models.EdificiosSinUsoCompletoEIELTableModel";
	public static final String TANATORIOS_MODEL = "com.geopista.app.eiel.models.TanatoriosEIELTableModel";
	public static final String TANATORIOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.TanatoriosCompletoEIELTableModel";
	public static final String VERTEDEROS_MODEL = "com.geopista.app.eiel.models.VertederosEIELTableModel";
	public static final String VERTEDEROS_MODEL_COMPLETO = "com.geopista.app.eiel.models.VertederosCompletoEIELTableModel";
	public static final String CARRETERAS_MODEL = "com.geopista.app.eiel.models.CarreterasEIELTableModel";
	public static final String CARRETERAS_MODEL_COMPLETO = "com.geopista.app.eiel.models.TramosCarreterasCompletoEIELTableModel";
	public static final String NUCLEOS_POBLACION_MODEL = "com.geopista.app.eiel.models.NucleosPoblacionEIELTableModel";
	public static final String NUCLEOS_POBLACION_MODEL_COMPLETO = "com.geopista.app.eiel.models.NucleosPoblacionCompletoEIELTableModel";
	public static final String DEPOSITOS_MODEL = "com.geopista.app.eiel.models.DepositosEIELTableModel";
	public static final String DEPOSITOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.DepositosCompletoEIELTableModel";
	public static final String PUNTOS_VERTIDO_MODEL = "com.geopista.app.eiel.models.PuntosVertidoEIELTableModel";
	public static final String PUNTOS_VERTIDO_MODEL_COMPLETO = "com.geopista.app.eiel.models.PuntosVertidoCompletoEIELTableModel";
	public static final String SERVICIOS_ABASTECIMIENTOS_MODEL = "com.geopista.app.eiel.models.ServiciosAbastecimientosEIELTableModel";
	public static final String SERVICIOS_ABASTECIMIENTOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.ServiciosAbastecimientosCompletoEIELTableModel";
	public static final String CENTROS_ASISTENCIALES_MODEL = "com.geopista.app.eiel.models.CentrosAsistencialesEIELTableModel";
	public static final String CENTROS_ASISTENCIALES_MODEL_COMPLETO = "com.geopista.app.eiel.models.CentrosAsistencialesCompletoEIELTableModel";
	public static final String CABILDO_CONSEJO_MODEL = "com.geopista.app.eiel.models.CabildoConsejoEIELTableModel";
	public static final String CABILDO_CONSEJO_MODEL_COMPLETO = "com.geopista.app.eiel.models.CabildoConsejoCompletoEIELTableModel";
	public static final String CEMENTERIOS_MODEL = "com.geopista.app.eiel.models.CementeriosEIELTableModel";
	public static final String CEMENTERIOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.CementeriosCompletoEIELTableModel";
	public static final String ENTIDADES_SINGULARES_MODEL = "com.geopista.app.eiel.models.EntidadesSingularesEIELTableModel";
	public static final String ENTIDADES_SINGULARES_MODEL_COMPLETO = "com.geopista.app.eiel.models.EntidadesSingularesCompletoEIELTableModel";
	public static final String INFO_TERMINOS_MUNICIPALES_MODEL = "com.geopista.app.eiel.models.InfoTerminosMunicipalesEIELTableModel";
	public static final String INFO_TERMINOS_MUNICIPALES_MODEL_COMPLETO = "com.geopista.app.eiel.models.InfoTerminosMunicipalesCompletoEIELTableModel";
	public static final String INCENDIOS_PROTECCION_MODEL = "com.geopista.app.eiel.models.IncendiosProteccionEIELTableModel";
	public static final String INCENDIOS_PROTECCION_MODEL_COMPLETO = "com.geopista.app.eiel.models.IncendiosProteccionCompletoEIELTableModel";
	public static final String LONJAS_MERCADOS_MODEL = "com.geopista.app.eiel.models.LonjasMercadosEIELTableModel";
	public static final String LONJAS_MERCADOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.LonjasMercadosCompletoEIELTableModel";
	public static final String MATADEROS_MODEL = "com.geopista.app.eiel.models.MataderosEIELTableModel";
	public static final String MATADEROS_MODEL_COMPLETO = "com.geopista.app.eiel.models.MataderosCompletoEIELTableModel";
	public static final String TRATAMIENTOS_POTABILIZACION_MODEL = "com.geopista.app.eiel.models.TratamientosPotabilizacionEIELTableModel";
	public static final String TRATAMIENTOS_POTABILIZACION_MODEL_COMPLETO = "com.geopista.app.eiel.models.TratamientosPotabilizacionCompletoEIELTableModel";
	public static final String DISEMINADOS_MODEL = "com.geopista.app.eiel.models.DiseminadosEIELTableModel";
	public static final String DISEMINADOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.DiseminadosCompletoEIELTableModel";
	public static final String ENCUESTADOS1_MODEL = "com.geopista.app.eiel.models.Encuestados1EIELTableModel";
	public static final String ENCUESTADOS1_MODEL_COMPLETO = "com.geopista.app.eiel.models.Encuestados1CompletoEIELTableModel";
	public static final String ENCUESTADOS2_MODEL = "com.geopista.app.eiel.models.Encuestados2EIELTableModel";
	public static final String ENCUESTADOS2_MODEL_COMPLETO = "com.geopista.app.eiel.models.Encuestados2CompletoEIELTableModel";
	public static final String NUCLEOS_ABANDONADOS_MODEL = "com.geopista.app.eiel.models.NucleosAbandonadosEIELTableModel";
	public static final String NUCLEOS_ABANDONADOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.NucleosAbandonadosCompletoEIELTableModel";
	
	//Prueba de concepto Elementos sin informacion alfanumerica.
	//ALFANUMERICOS
	//- EMISARIOS
	public static final String EMISARIOS_MODEL = "com.geopista.app.eiel.models.EmisariosEIELTableModel";
	public static final String EMISARIOS_MODEL_COMPLETO = "com.geopista.app.eiel.models.EmisariosCompletoEIELTableModel";
	public static final String TCONDUCCION_MODEL = "com.geopista.app.eiel.models.TramosConduccionEIELTableModel";
	public static final String TCONDUCCION_MODEL_COMPLETO = "com.geopista.app.eiel.models.TramosConduccionCompletoEIELTableModel";
	public static final String TCOLECTOR_MODEL = "com.geopista.app.eiel.models.ColectoresEIELTableModel";
	public static final String TCOLECTOR_MODEL_COMPLETO = "com.geopista.app.eiel.models.ColectoresCompletoEIELTableModel";
	public static final String AGRUPACIONES6000_MODEL_COMPLETO = "com.geopista.app.eiel.models.EntidadesAgrupadasCompletoEIELTableModel";

	public static final String GENERIC_MODEL = "com.geopista.app.eiel.models.GenericEIELTableModel";

	
	
	public static final String CAPTACIONES_MODEL_COMPLETO_VERSIONADO = "com.geopista.app.eiel.models.versionados.CaptacionesVersionadoCompletoEIELTableModel";
	public static final String ABASTECIMIENTO_AUTONOMO_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.AbastecimientoAutonomoVersionadoCompletoEIELTableModel";
	public static final String DEPURADORAS1_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.Depuradora1VersionadoCompletoEIELTableModel";
	public static final String DEPURADORAS2_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.Depuradora2VersionadoCompletoEIELTableModel";
	public static final String CASAS_CONSISTORIALES_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.CasasConsistorialesVersionadoCompletoEIELTableModel";
	public static final String CENTROS_CULTURALES_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.CentrosCulturalesVersionadoCompletoEIELTableModel";
	public static final String CENTROS_ENSENIANZA_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.CentrosEnsenianzaVersionadoCompletoEIELTableModel";
	public static final String INSTALACIONES_DEPORTIVAS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.InstalacionesDeportivasVersionadoCompletoEIELTableModel";
	public static final String OTROS_SERVICIOS_MUNICIPALES_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.OtrosServMunicipalesVersionadoCompletoEIELTableModel";
	public static final String PADRON_NUCLEOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.PadronNucleosVersionadoCompletoEIELTableModel";
	public static final String PADRON_MUNICIPIOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.PadronMunicipiosVersionadoCompletoEIELTableModel";
	public static final String PARQUES_JARDINES_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.ParquesJardinesVersionadoCompletoEIELTableModel";
	public static final String PLANEAMIENTO_URBANO_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.PlaneamientoUrbanoVersionadoCompletoEIELTableModel";
	public static final String POBLAMIENTO_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.PoblamientoVersionadoCompletoEIELTableModel";
	public static final String RECOGIDA_BASURAS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.RecogidaBasurasVersionadoCompletoEIELTableModel";
	public static final String CENTROS_SANITARIOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.CentrosSanitariosVersionadoCompletoEIELTableModel";
	public static final String SANEAMIENTO_AUTONOMO_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.SaneamientoAutonomoVersionadoCompletoEIELTableModel";
	public static final String SERVICIOS_SANEAMIENTO_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.ServiciosSaneamientoVersionadoCompletoEIELTableModel";
	public static final String SERVICIOS_RECOGIDA_BASURA_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.ServiciosRecogidaBasuraVersionadoCompletoEIELTableModel";
	public static final String EDIFICIOS_SIN_USO_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.EdificiosSinUsoVersionadoCompletoEIELTableModel";
	public static final String TANATORIOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.TanatoriosVersionadoCompletoEIELTableModel";
	public static final String VERTEDEROS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.VertederosVersionadoCompletoEIELTableModel";
	public static final String CARRETERAS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.TramosCarreterasVersionadoCompletoEIELTableModel";
	public static final String NUCLEOS_POBLACION_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.NucleosPoblacionVersionadoCompletoEIELTableModel";
	public static final String DEPOSITOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.DepositosVersionadoCompletoEIELTableModel";
	public static final String PUNTOS_VERTIDO_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.PuntosVertidoVersionadoCompletoEIELTableModel";
	public static final String SERVICIOS_ABASTECIMIENTOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.ServiciosAbastecimientosVersionadoCompletoEIELTableModel";
	public static final String CENTROS_ASISTENCIALES_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.CentrosAsistencialesVersionadoCompletoEIELTableModel";
	public static final String CABILDO_CONSEJO_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.CabildoConsejoVersionadoCompletoEIELTableModel";
	public static final String CEMENTERIOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.CementeriosVersionadoCompletoEIELTableModel";
	public static final String ENTIDADES_SINGULARES_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.EntidadesSingularesVersionadoCompletoEIELTableModel";
	public static final String INFO_TERMINOS_MUNICIPALES_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.InfoTerminosMunicipalesVersionadoCompletoEIELTableModel";
	public static final String INCENDIOS_PROTECCION_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.IncendiosProteccionVersionadoCompletoEIELTableModel";
	public static final String LONJAS_MERCADOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.LonjasMercadosVersionadoCompletoEIELTableModel";
	public static final String MATADEROS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.MataderosVersionadoCompletoEIELTableModel";
	public static final String TRATAMIENTOS_POTABILIZACION_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.TratamientosPotabilizacionVersionadoCompletoEIELTableModel";
	public static final String DISEMINADOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.DiseminadosVersionadoCompletoEIELTableModel";
	public static final String ENCUESTADOS1_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.Encuestados1VersionadoCompletoEIELTableModel";
	public static final String ENCUESTADOS2_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.Encuestados2VersionadoCompletoEIELTableModel";
	public static final String NUCLEOS_ABANDONADOS_MODEL_COMPLETO_VERSIONADO  = "com.geopista.app.eiel.models.versionados.NucleosAbandonadosVersionadoCompletoEIELTableModel";
	
	public static final String EMISARIOS_MODEL_COMPLETO_VERSIONADO = "com.geopista.app.eiel.models.versionados.EmisariosVersionadoCompletoEIELTableModel";
	public static final String REDDISTRIBUCION_MODEL_COMPLETO_VERSIONADO = "com.geopista.app.eiel.models.versionados.RedDistribucionVersionadoCompletoEIELTableModel";
	public static final String RAMALSANEAMIENTO_MODEL_COMPLETO_VERSIONADO = "com.geopista.app.eiel.models.versionados.RamalSaneamientoVersionadoCompletoEIELTableModel";
	public static final String TCONDUCCION_MODEL_COMPLETO_VERSIONADO = "com.geopista.app.eiel.models.versionados.TramoConduccionVersionadoCompletoEIELTableModel";
	public static final String TCOLECTOR_MODEL_COMPLETO_VERSIONADO = "com.geopista.app.eiel.models.versionados.ColectoresVersionadoCompletoEIELTableModel";
	public static final String AGRUPACIONES6000_MODEL_COMPLETO_VERSIONADO="com.geopista.app.eiel.models.versionados.EntidadesAgrupadasVersionadoCompletoEIELTableModel";
	
	
	
	public static final String DOMAIN_NAME = "EIEL";

	public static DecimalFormat decimalFormatter = new DecimalFormat("00000.00");

	public static final int ACTION_GET_ID_USUARIO = 2;
	public static final int ACTION_OBTENER_DOMINIO = 3;
	public static final int ACTION_OBTENER_IDMAPA = 4;
	public static final int ACTION_OBTENER_ELEMENTO = 5;
	public static final int ACTION_GET_FEATURES = 6;
	public static final int ACTION_BLOQUEAR_ELEMENTO = 7;
	public static final int ACTION_GET_BLOQUEADO_ELEMENTO = 8;
	public static final int ACTION_ELIMINAR_ELEMENTO = 9;
	public static final int ACTION_INSERTAR_ELEMENTO = 10;
	public static final int ACTION_ACTUALIZAR_ELEMENTO = 11;
	public static final int ACTION_OBTENER_NOMBREMAPA = 12;
	public static final int ACTION_CALCULAR_INDICES_MUNICIPALES = 13;
	public static final int ACTION_CALCULAR_INDICES_OBRAS = 14;
	public static final int ACTION_SET_FIELDS = 15;
	public static final int ACTION_VALIDACION_MPT = 16;
	
	public static final int ACTION_EXPORT_MPT = 17;
//	public static final int ACTION_GET_PLANTILLAS = 18;
	public static final int ACTION_GET_NODOS_EIEL= 19;
	public static final int ACTION_GET_CAMPOS_CAPA_EIEL= 20;
	public static final int ACTION_GET_NUCLEOS_MUNICIPIO= 21;
	public static final int ACTION_GET_CUADROS_MPT= 22;
	public static final int ACTION_GET_POBLAMIENTOS_MPT= 23;
	public static final int ACTION_OBTENER_ELEMENTOS_VERSIONADOS = 24;
	public static final int ACTION_OBTENER_USUARIOS_ENTIDAD = 25;
	public static final int ACTION_GET_INDICADORES_EIEL= 26;
	public static final int ACTION_GET_MUNICIPIOS_EIEL= 27;
	//Temporal mientras autor sube valor actual
	public static final int ACTION_GET_PLANTILLAS_CUADROS = 28;
	public static final int ACTION_GET_VALIDACIONES_MPT= 29;

	//Acciones sobre el nuevo modelo de indicadores
	
	public static final int ACTION_EIEL_INDICADORES_CLEANDATA= 30;
	public static final int ACTION_EIEL_INDICADORES_LOAD_POBLACION= 31;
	public static final int ACTION_EIEL_INDICADORES_LOAD_CICLOAGUA= 32;
	public static final int ACTION_EIEL_INDICADORES_LOAD_INFRAESTRUCTURAS= 33;
	public static final int ACTION_EIEL_INDICADORES_LOAD_RESIDUOSURBANOS= 34;
	public static final int ACTION_EIEL_INDICADORES_LOAD_EDUCACIONCULTURA= 35;
	public static final int ACTION_EIEL_INDICADORES_LOAD_SANITARIOASISTENCIAL= 36;
	public static final int ACTION_EIEL_INDICADORES_LOAD_OTROS= 37;
	
	public static final int ACTION_PUBLICAR_ELEMENTO= 38;
	public static final int ACTION_VALIDAR_ELEMENTO= 39;
	public static final int ACTION_GET_NUM_ELEMENTOS_PENDIENTES=40;
	
	public static final int ACTION_OBTENER_MAPAS = 41;
	
	public static final int ACTION_GET_NUCLEOS_MUNICIPIO_ENCUESTABLES= 42;
	public static final int ACTION_UPDATE_CONFIGURACION_PADRON= 43;

	public static final int  ACTION_GET_MUNICIPIOS_OTRA_PROVINCIA =44;
	
	// Capas
	public static final String CAPTACIONES_LAYER = "CA";
	public static final String DEPOSITOS_LAYER = "DE";
	public static final String DEPURADORAS_LAYER = "ED";
	public static final String NUCLEO_LAYER = "NP";
	public static final String TRAMOS_CONDUCCIONES_LAYER = "TCN";
	public static final String TRATAMIENTOS_POTABILIZACION_LAYER = "TP";
	public static final String ELEMENTO_PUNTUAL_ABASTECIMIENTO_LAYER = "AR";
	public static final String EMISARIOS_LAYER = "TEM";
	public static final String COLECTOR_LAYER = "TCL";
	public static final String DEPURADORAS1_LAYER = "ED";
	public static final String DEPURADORAS2_LAYER = "ED";
	public static final String RED_RAMALES_LAYER = "RS";
	public static final String RED_DISTRIBUCION_LAYER = "RD";
	public static final String PUNTOS_VERTIDO_LAYER = "PV";
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
	public static final String AGRUPACIONES6000_LAYER = "TTMM";
	public static final String SANEAMIENTO_AUTONOMO_LAYER = "SN";
	public static final String SERVICIOS_RECOGIDA_BASURA_LAYER = "NP";
	public static final String NUCLEOS_ABANDONADOS_LAYER = "NP";
	public static final String NUCLEO_ENCT_7_LAYER = "N7";
	public static final String DATOS_SERVICIO_ABASTECIMIENTO_LAYER = "NP";
	public static final String DATOS_SERVICIO_SANEAMIENTO_LAYER = "NP";
	
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
	



	
	

	public static String OPERACION_ANNADIR = "ANNADIR";
	public static String OPERACION_MODIFICAR = "MODIFICAR";
	public static String OPERACION_ELIMINAR = "ELIMINAR";
	public static String OPERACION_FILTRAR = "FILTRAR";
	public static String OPERACION_LISTAR = "LISTAR";
	public static String OPERACION_DIGITALIZAR = "DIGITALIZAR";
	public static String OPERACION_DESACTIVAR = "DESACTIVAR";
	public static String OPERACION_VERSION = "VERSIONAR";
	public static String OPERACION_CONSULTAR = "CONSULTAR";

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
	public static int MAPA_EIEL_GLOBAL = 50;
	public static int MAPA_EIEL_COMPLETO = 50;
	
	public static int MAPA_EIEL_CARGA = 50;
	public static String NOMBRE_MAPA_EIEL = "Mapa EIEL";
	public static String NOMBRE_MAPA_EIEL_GLOBAL = "Mapa EIEL Global";
	public static String NOMBRE_MAPA_EIEL_COMPLETO = "Mapa EIEL Completo";
	public static String NOMBRE_MAPA_EIEL_REDUCIDO = "Mapa EIEL Reducido";
	public static String NOMBRE_MAPA_EIEL_INFORMES = "Mapa EIEL Informes";
	
	public static int MAPA_PROVINCIAL = 407;
	public static int MAPA_COMARCAL = 408;
	public static int MAPA_MUNICIPAL = 409;
	public static int MAPA_INFRAESTRUCTURAS = 344;
	public static int MAPA_TEMATICO_VIVIENDAS = 348;
	public static int MAPA_TEMATICO_PLANEAMIENTO = 349;
	public static int MAPA_TEMATICO_DEPOSITOS1 = 352;
	public static int MAPA_TEMATICO_DEPOSITOS2 = 354;
	public static int MAPA_TEMATICO_PAVIMENTACION1 = 355;
	public static int MAPA_TEMATICO_PAVIMENTACION2 = 356;
	public static int MAPA_TEMATICO_ALUMBRADO = 357;
	public static int MAPA_TEMATICO_CENTROS_SANITARIOS1 = 358;
	public static int MAPA_TEMATICO_CENTROS_SANITARIOS2 = 359;
	public static int MAPA_TEMATICO_CAPTACIONES = 360;
	public static int MAPA_TEMATICO_POTABILIZACION = 374;
	public static int MAPA_TEMATICO_DEPOSITOS_SINT = 361;
	public static int MAPA_TEMATICO_DISTRIBUCION = 362;
	public static int MAPA_TEMATICO_SANEAMIENTO = 363;
	public static int MAPA_TEMATICO_DEPURACION = 364;
	public static int MAPA_TEMATICO_PAVIMENTACION_SINT = 365;
	public static int MAPA_TEMATICO_ALUMBRADO_SINT = 366;
	public static int MAPA_TEMATICO_BASURAS = 367;
	public static int MAPA_TEMATICO_RESIDUOS = 368;
	public static int MAPA_TEMATICO_CULTURA = 369;
	public static int MAPA_TEMATICO_DEPORTES = 370;
	public static int MAPA_TEMATICO_ZONASVERDES = 371;
	public static int MAPA_TEMATICO_ADMINISTRATIVO = 372;
	public static int MAPA_TEMATICO_GLOBAL = 373;

	public static int MAPA_COMARCAL_VIVIENDAS = 375;
	public static int MAPA_COMARCAL_PLANEAMIENTO = 376;
	public static int MAPA_COMARCAL_DEPOSITOS1 = 377;
	public static int MAPA_COMARCAL_DEPOSITOS2 = 378;
	public static int MAPA_COMARCAL_PAVIMENTACION1 = 379;
	public static int MAPA_COMARCAL_PAVIMENTACION2 = 380;
	public static int MAPA_COMARCAL_ALUMBRADO = 381;
	public static int MAPA_COMARCAL_CENTROS_SANITARIOS1 = 382;
	public static int MAPA_COMARCAL_CENTROS_SANITARIOS2 = 383;
	public static int MAPA_COMARCAL_CAPTACIONES = 384;
	public static int MAPA_COMARCAL_POTABILIZACION = 385;
	public static int MAPA_COMARCAL_DEPOSITOS_SINT = 386;
	public static int MAPA_COMARCAL_DISTRIBUCION = 387;
	public static int MAPA_COMARCAL_SANEAMIENTO = 388;
	public static int MAPA_COMARCAL_DEPURACION = 389;
	public static int MAPA_COMARCAL_PAVIMENTACION_SINT = 390;
	public static int MAPA_COMARCAL_ALUMBRADO_SINT = 391;
	public static int MAPA_COMARCAL_BASURAS = 392;
	public static int MAPA_COMARCAL_RESIDUOS = 393;
	public static int MAPA_COMARCAL_CULTURA = 394;
	public static int MAPA_COMARCAL_DEPORTES = 395;
	public static int MAPA_COMARCAL_ZONASVERDES = 396;
	public static int MAPA_COMARCAL_ADMINISTRATIVO = 397;
	public static int MAPA_COMARCAL_GLOBAL = 398;

	public static int MAPA_OBRAS_INVERSION = 401;
	public static int MAPA_OBRAS_HABITANTE = 400;
	public static int MAPA_OBRAS_TOTAL = 3;

	public static int MAPA_COMARCAL_INVERSION = 404;
	public static int MAPA_COMARCAL_HABITANTE = 403;
	public static int MAPA_COMARCAL_TOTAL = 405;

	public static final int ACL_INDICADORES = 12;

	public static final String PERM_LAYER_READ = "Geopista.Layer.Leer";
	public static final String PERM_VERSION_READ = "Geopista.EIEL.versionado.visualizacion";
	public static final String PERM_VERSION_UPDATE = "Geopista.EIEL.versionado.modificacion";
	public static final String PERM_LOGIN_EIEL="LocalGis.EIEL.Login";
	//public static final String PERM_EDICION_EIEL="LocalGis.edicion.EIEL";
	public static final String PERM_MODIFICA_EIEL="LocalGis.EIEL.Modifica";
	public static final String PERM_CONSULTA_EIEL="LocalGis.EIEL.Consulta";
	public static final String PERM_GENERA_MPT_EIEL="LocalGis.EIEL.GeneraMPT";
	public static final String PERM_VALIDA_MPT_EIEL="LocalGis.EIEL.ValidarMPT";
	public static final String PERM_CUADRO_MPT_EIEL="LocalGis.EIEL.CuadrosResumenMPT";
	public static final String PERM_EXPORTAR_SHP_MPT_EIEL="LocalGis.EIEL.ExportarSHPMPT";
	public static final String PERM_ACTUALIZAR_PADRON_EIEL="LocalGis.EIEL.ActualizarPadron";

	
	public static final String PERM_PUBLICADOR_EIEL = "LocalGis.EIEL.Publicador";
	public static final String PERM_VALIDADOR_EIEL = "LocalGis.EIEL.Validador";


	public static final String ABASTECIMIENTO_AUTONOMO_CLAVE = "AU";
	public static final String INSTALACIONES_DEPORTIVAS_CLAVE = "ID";
	public static final String DEPURADORAS1_CLAVE = "ED";
	public static final String DEPURADORAS2_CLAVE = "ED";
	public static final String CAPTACIONES_ClAVE = "CA";
	public static final String CABILDOCONSEJO_CLAVE = "CI";
	public static final String CASASCONSISTORIALES_CLAVE = "CC";
	public static final String CEMENTERIOS_CLAVE = "CE";
	public static final String CENTROCULTURAL_CLAVE = "CU";
	public static final String CENTROENSENIANZA_CLAVE = "EN";
	public static final String CENTROSASISTENCIALES_CLAVE = "AS";
	public static final String CENTROSSANITARIOS_CLAVE = "SA";
	public static final String DEPOSITOS_CLAVE = "DE";
	public static final String EDIFICIOSSINUSO_CLAVE = "SU";
	public static final String INCENDIOSPROTECCION_CLAVE = "IP";
	public static final String LONJASMERCADOS_CLAVE = "LM";
	public static final String MATADEROS_CLAVE = "MT";
	public static final String PARQUESJARDINES_CLAVE = "PJ";
	public static final String PUNTOSDEVERTIDO_CLAVE = "PV";
	public static final String RECOGIDABASURA_CLAVE = "RB";
	public static final String SANEAMIENTOAUTONOMO_CLAVE = "AU";
	public static final String TANATORIOS_CLAVE = "TA";
	public static final String TRATAMIENTOSPOTABILIZACION_CLAVE = "TP";
	public static final String VERTEDEROS_CLAVE = "VT";
	public static final String EMISARIOS_CLAVE = "EM";
	public static final String CONDUCCION_CLAVE = "CN";
	public static final String COLECTOR_CLAVE = "CL";
	public static final String ALUMBRADO_CLAVE = "AL";
	public static final String TRAMOSCARRETERAS_CLAVE = "TC";	
	public static final String COMARCA_CLAVE = "CO";
	public static final String DISEMINADOS_CLAVE = "DI";
	public static final String ENCUESTADOS1_CLAVE = "NP";
	public static final String ENCUESTADOS2_CLAVE = "NP";
	public static final String ENTIDADES_SINGULARES_CLAVE = "SI";
	public static final String INCENDIOS_PROTECCION_CLAVE = "IP";
	public static final String LONJAS_MERCADOS_CLAVE = "LM";
	public static final String MUNICIPIO_CLAVE = "TM";
	public static final String NUCLEO_ENCT_7_CLAVE = "N7";
	public static final String NUCLEOS_ABANDONADOS_CLAVE = "NP";
	public static final String NUCLEOS_POBLACION_CLAVE = "NP";
	public static final String OTROS_SERVICIOS_MUNICIPALES_CLAVE = "OS";
	public static final String PADRON_MUNICIPIOS_CLAVE = "PM";
	public static final String PADRON_NUCLEOS_CLAVE = "PN";
	public static final String PLANEAMIENTO_URBANO_CLAVE = "PU";
	public static final String SERVICIOS_ABASTECIMIENTOS_CLAVE = "SA";
	public static final String SERVICIOS_RECOGIDA_BASURA_CLAVE = "NP";
	public static final String SERVICIOS_SANEAMIENTO_CLAVE = "SS";
	
	public static String PATRON_FICHA_MUNICIPAL= "EIEL_FichaMunicipal";
	
	public static String PATH_PLANTILLAS_CUADROS_EIEL=ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"eiel"+File.separator+"cuadros";
	public static String PATH_PLANTILLAS_CUADROS_IMG_EIEL=ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"eiel"+File.separator+"cuadros"+File.separator+"img"+File.separator;
    public static String PATH_PLANTILLAS_EIEL=ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"eiel";
    public static String SUREPORT_DIR=ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"eiel"+File.separator+"subreports"+File.separator;
    public static String IMAGE_DIR=ConstantesLocalGISPlantillas.RESOURCES_PATH+File.separator+"plantillas"+File.separator+"eiel"+File.separator+"img"+File.separator;

    
    public static int formatoPDF= 2;
    public static int formatoPREVIEW= 1;
    public static int formatoHTML= 3;
    public static int formatoPRINT= 4;
    public static int formatoXML= 5;
    public static int formatoPDFRAW= 6;
    public static int formatoXMLRAW= 7;
    
    
    public static final String KEY_NODO_EIEL= "nodo_eiel";
    public static final String KEY_PATH_PLANTILLAS= "pathPlantillas";
    public static final String KEY_ID_MUNICIPIO = "idMunicipio";
	public static final String ABASTECIMIENTO_AGUA = "AA";
	public static final String SANEAMIENTO = "SA";
	
	
	//Ficheros MPT
	public static final String FASE_MPT = "fase_mpt";
	public static final String CUADRO_VALIDACION_MPT = "cuadro_validacion_mpt";
	public static final String CUADRO_VALIDACIONES_CUADROS_MPT = "cuadro_validaciones_cuadros_mpt";
	public static final String FILENAME_VALIDACION_MPT = "ValidacionMPT";
	public static final String FILENAME_EXPORT_MPT_ZIP = "MPT";
	public static final String RUTA_EXPORT_MPT = "C:\\LocalGIS\\Datos";
	public static final String FILENAMEALUMBRADO="ALUMBRADO.txt";
	public static final String FILENAMECABILDO_CONSEJO="CABILDO_CONSEJO.txt";
	public static final String FILENAMECAP_AGUA_NUCLEO="CAP_AGUA_NUCLEO.txt";
	public static final String FILENAMECAPTACION_AGUA="CAPTACION_AGUA.txt";
	public static final String FILENAMECAPTACION_ENC="CAPTACION_ENC.txt";
	public static final String FILENAMECAPTACION_ENC_M50="CAPTACION_ENC_M50.txt";
	public static final String FILENAMECARRETERA="CARRETERA.txt";
	public static final String FILENAMECASA_CON_USO="CASA_CON_USO.txt";
	public static final String FILENAMECASA_CONSISTORIAL="CASA_CONSISTORIAL.txt";
	public static final String FILENAMECEMENTERIO="CEMENTERIO.txt";
	public static final String FILENAMECENT_CULTURAL="CENT_CULTURAL.txt";
	public static final String FILENAMECENT_CULTURAL_USOS="CENT_CULTURAL_USOS.txt";
	public static final String FILENAMECENTRO_ASISTENCIAL="CENTRO_ASISTENCIAL.txt";
	public static final String FILENAMECENTRO_ENSENANZA="CENTRO_ENSENANZA.txt";
	public static final String FILENAMECENTRO_SANITARIO="CENTRO_SANITARIO.txt";
	public static final String FILENAMECOLECTOR="COLECTOR.txt";
	public static final String FILENAMECOLECTOR_ENC="COLECTOR_ENC.txt";
	public static final String FILENAMECOLECTOR_ENC_M50="COLECTOR_ENC_M50.txt";
	public static final String FILENAMECOLECTOR_NUCLEO="COLECTOR_NUCLEO.txt";
	public static final String FILENAMECOND_AGUA_NUCLEO="COND_AGUA_NUCLEO.txt";
	public static final String FILENAMECONDUCCION="CONDUCCION.txt";
	public static final String FILENAMECONDUCCION_ENC="CONDUCCION_ENC.txt";
	public static final String FILENAMECONDUCCION_ENC_M50="CONDUCCION_ENC_M50.txt";
	public static final String FILENAMEDEP_AGUA_NUCLEO="DEP_AGUA_NUCLEO.txt";
	public static final String FILENAMEDEPOSITO="DEPOSITO.txt";
	public static final String FILENAMEDEPOSITO_AGUA_NUCLEO="DEPOSITO_AGUA_NUCLEO.txt";
	public static final String FILENAMEDEPOSITO_ENC="DEPOSITO_ENC.txt";
	public static final String FILENAMEDEPOSITO_ENC_M50="DEPOSITO_ENC_M50.txt";
	public static final String FILENAMEDEPURADORA="DEPURADORA.txt";
	public static final String FILENAMEDEPURADORA_ENC="DEPURADORA_ENC.txt";
	public static final String FILENAMEDEPURADORA_ENC_2="DEPURADORA_ENC_2.txt";
	public static final String FILENAMEDEPURADORA_ENC_2_M50="DEPURADORA_ENC_2_M50.txt";
	public static final String FILENAMEDEPURADORA_ENC_M50="DEPURADORA_ENC_M50.txt";
	public static final String FILENAMEEDIFIC_PUB_SIN_USO="EDIFIC_PUB_SIN_USO.txt";
	public static final String FILENAMEEMISARIO="EMISARIO.txt";
	public static final String FILENAMEEMISARIO_ENC="EMISARIO_ENC.txt";
	public static final String FILENAMEEMISARIO_ENC_M50="EMISARIO_ENC_M50.txt";
	public static final String FILENAMEEMISARIO_NUCLEO="EMISARIO_NUCLEO.txt";
	public static final String FILENAMEENTIDAD_SINGULAR="ENTIDAD_SINGULAR.txt";
	public static final String FILENAMEINFRAESTR_VIARIA="INFRAESTR_VIARIA.txt";
	public static final String FILENAMEINST_DEPOR_DEPORTE="INST_DEPOR_DEPORTE.txt";
	public static final String FILENAMEINSTAL_DEPORTIVA="INSTAL_DEPORTIVA.txt";
	public static final String FILENAMELONJA_MERC_FERIA="LONJA_MERC_FERIA.txt";
	public static final String FILENAMEMATADERO="MATADERO.txt";
	public static final String FILENAMEMUN_ENC_DIS="MUN_ENC_DIS.txt";
	public static final String FILENAMEMUNICIPIO="MUNICIPIO.txt";
	public static final String FILENAMENIVEL_ENSENANZA="NIVEL_ENSENANZA.txt";
	public static final String FILENAMENUC_ABANDONADO="NUC_ABANDONADO.txt";
	public static final String FILENAMENUCL_ENCUESTADO_1="NUCL_ENCUESTADO_1.txt";
	public static final String FILENAMENUCL_ENCUESTADO_2="NUCL_ENCUESTADO_2.txt";
	public static final String FILENAMENUCL_ENCUESTADO_3="NUCL_ENCUESTADO_3.txt";
	public static final String FILENAMENUCL_ENCUESTADO_4="NUCL_ENCUESTADO_4.txt";
	public static final String FILENAMENUCL_ENCUESTADO_5="NUCL_ENCUESTADO_5.txt";
	public static final String FILENAMENUCL_ENCUESTADO_6="NUCL_ENCUESTADO_6.txt";
	public static final String FILENAMENUCL_ENCUESTADO_7="NUCL_ENCUESTADO_7.txt";
	public static final String FILENAMENUCLEO_POBLACION="NUCLEO_POBLACION.txt";
	public static final String FILENAMEOT_SERV_MUNICIPAL="OT_SERV_MUNICIPAL.txt";
	public static final String FILENAMEPADRON="PADRON.txt";
	public static final String FILENAMEPARQUE="PARQUE.txt";
	public static final String FILENAMEPLAN_URBANISTICO="PLAN_URBANISTICO.txt";
	public static final String FILENAMEPOBLAMIENTO="POBLAMIENTO.txt";
	public static final String FILENAMEPOTABILIZACION_ENC="POTABILIZACION_ENC.txt";
	public static final String FILENAMEPOTABILIZACION_ENC_M50="POTABILIZACION_ENC_M50.txt";
	public static final String FILENAMEPROTECCION_CIVIL="PROTECCION_CIVIL.txt";
	public static final String FILENAMEPROVINCIA="PROVINCIA.txt";
	public static final String FILENAMERAMAL_SANEAMIENTO="RAMAL_SANEAMIENTO.txt";
	public static final String FILENAMERECOGIDA_BASURA="RECOGIDA_BASURA.txt";
	public static final String FILENAMERED_DISTRIBUCION="RED_DISTRIBUCION.txt";
	public static final String FILENAMESANEA_AUTONOMO="SANEA_AUTONOMO.txt";
	public static final String FILENAMETANATORIO="TANATORIO.txt";
	public static final String FILENAMETRA_POTABILIZACION="TRA_POTABILIZACION.txt";
	public static final String FILENAMETRAMO_CARRETERA="TRAMO_CARRETERA.txt";
	public static final String FILENAMETRAMO_COLECTOR="TRAMO_COLECTOR.txt";
	public static final String FILENAMETRAMO_COLECTOR_M50="TRAMO_COLECTOR_M50.txt";
	public static final String FILENAMETRAMO_CONDUCCION="TRAMO_CONDUCCION.txt";
	public static final String FILENAMETRAMO_CONDUCCION_M50="TRAMO_CONDUCCION_M50.txt";
	public static final String FILENAMETRAMO_EMISARIO="TRAMO_EMISARIO.txt";
	public static final String FILENAMETRAMO_EMISARIO_M50="TRAMO_EMISARIO_M50.txt";
	public static final String FILENAMETRAT_POTA_NUCLEO="TRAT_POTA_NUCLEO.txt";
	public static final String FILENAMEVERT_ENCUESTADO="VERT_ENCUESTADO.txt";
	public static final String FILENAMEVERT_ENCUESTADO_M50="VERT_ENCUESTADO_M50.txt";
	public static final String FILENAMEVERTEDERO="VERTEDERO.txt";
	public static final String FILENAMEVERTEDERO_NUCLEO="VERTEDERO_NUCLEO.txt";
	
	public static final String ESTRUCTURA_NUCLEOS="nucleosEIEL";
	public static final String ESTRUCTURA_INDICADORES="indicadoresEIEL";
	public static final String ESTRUCTURA_MUNICIPIOS="municipiosEIEL";
	public static final String ESTRUCTURA_ENTIDADES_NUCLEOS="entidadesNucleosEIEL";

	//Exportacion cuadros
	public static final String LST_ID_MUNICIPIO_CUADROS_MPT = "lst_id_municipio_cuadros_mpt";
	public static final String PROVINCIA_MPT = "provincia_mpt";
	
	//public static final String DOBLE_CLICK="DOBLE_CLICK";
	public static final String SORT="SORT";
	public static final String CAPA_MUNICIPIOS = "municipios";

	public static String REVISION_VALIDA="9999999999";
	public static String REVISION_TEMPORAL="9899999999";
	public static String REVISION_PUBLICABLE="9799999999";
	public static String REVISION_PUBLICABLE_MOVILIDAD="9699999999";
	public static String REVISION_BORRABLE="9599999999";

	public static final String TEMPORAL="T";
	public static final String PUBLICABLE="P";
	public static final String AUTO_PUBLICABLE="E";
	public static final String VALIDO="V";
	public static final String BORRABLE="B";
	public static final String URL_EIEL_DATA_SERVICE = null;
	
	public static int ESTADO_VALIDO=0;
	public static int ESTADO_TEMPORAL=1;
	public static int ESTADO_PUBLICABLE=2;
	public static int ESTADO_PUBLICABLE_MOVILIDAD=3;
	//Para indicar que se desea publicar un elemento
	public static int ESTADO_A_PUBLICAR=4;
	public static int ESTADO_BORRABLE=5;
	public static int ESTADO_A_BORRAR=6;

	public static String EVENT_DOUBLE_CLICK="EIEL_DOUBLE_CLICK";
	public static String EVENT_EIEL_MAP_SELECTION_CHANGED="EIEL_MAP_SELECTION_CHANGED";
	public static String EVENT_EIEL_MAP_FEATURE_CHANGED="EIEL_MAP_FEATURE_CHANGED";
	
	
	public static boolean GLOBAL_MAP=false;
	public static int MULTIPLO_CONSUMO_INVIERNO = 150;
	public static int MULTIPLO_CONSUMO_VERANO = 180;
	
	//La inicializacion se ha movido a la clase InitEIEL
	
	public static HashMap<String,String> capasEspeciales;
	
	static{
		
		//Capas con tratamiento especial
		capasEspeciales=new HashMap<String,String>();
		capasEspeciales.put("IV","IV"); //Red de Carreteras->Infraestructura viaria
		capasEspeciales.put("PL","PL"); //Alumbrado->Puntos de Luz
		capasEspeciales.put("CMP","CMP"); //Alumbrado->Cuadro de mandos
		capasEspeciales.put("eea","eea"); //Alumbrado->Estabilizador
		capasEspeciales.put("PR","PR"); //Saneamiento->Elementos puntuales
		capasEspeciales.put("AR","AR"); //Abastecimiento->Elementos puntuales
		capasEspeciales.put("RD","RD"); //Abastecimiento->Red de distribución domiciliaria
		capasEspeciales.put("RS","RS"); //Saneamiento->Red de ramales
		capasEspeciales.put("CBS","CBS"); //Saneamiento->Caseta de Bombeo
	}
}

