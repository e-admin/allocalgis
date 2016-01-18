/**
 * ConstantesRegistroExpModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.geopista.security.GeopistaPrincipal;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 26-dic-2006
 * Time: 15:11:01
 * To change this template use File | Settings | File Templates.
 */
public class ConstantesRegistroExpModel
{
    /** Usuario de login */
    public static GeopistaPrincipal principal;

    public static String Locale= "es_ES";
    public static String LocalCastellano= "es_ES";

    /** Ayuda */
    public static String helpSetHomeID= "RegExpIntro";    

    public static int IdMunicipio= 34083;
    public static String Provincia = null;
	public static String Municipio = null;

    /**Modo de trabajo: Acoplado (A) o Desacoplado (D)*/
    public static String modoTrabajo = "A";
    
    /** Modo de generación del FXCC en el fin de entrada embebido o en un directorio **/
    public static String modoGeneracion = "F";

    /** Tipo de convenio: Ninguno (NINGU), Fisico economico (902) o titularidad (901)*/
    public static String tipoConvenio = "NINGU";

    /** Forma de convenio: Delegacion de competencias (D) o Enconmienda de Gestion (E)*/
    public static String formaConvenio = "D";

    public static final String idApp= "Catastro";
    
    public static boolean botonesGestExp[]= new boolean[9];

	public static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public static String calendarValue="";

    public static ArrayList tiposExpedientes;
    

	public static final String EXP_FINALIZADO = "F";
	public static final String EXP_NO_FINALIZADO = "R";
	

    /** Identificador de las variables del expedienteCatastral. */
    public final static String expedienteIdExpediente = "idExpediente";
    public final static String expedienteEntidadGeneradora = "entidadGeneradora";
    public final static String expedienteIdEstado = "idEstado";
    public final static String expedienteIdTecnicoCatastro = "idTecnicoCatastro";
    public final static String expedienteDireccionPresentador = "direccionPresentador";
    public final static String expedienteIdMunicipio = "idMunicipio";
    public final static String expedienteNumeroExpediente = "numeroExpediente";
    public final static String expedienteTipoExpediente = "tipoExpediente";
    public final static String expedienteFechaAlteracion = "fechaAlteracion";
    public final static String expedienteAnnoExpedienteGerencia = "annoExpedienteGerencia";
    public final static String expedienteReferenciaExpedienteGerencia = "referenciaExpedienteGerencia";
    public final static String expedienteCodigoEntidadRegistroDGCOrigenAlteracion = "codigoEntidadRegistroDGCOrigenAlteracion";
    public final static String expedienteAnnoExpedienteAdminOrigenAlteracion = "annoExpedienteAdminOrigenAlteracion";
    public final static String expedienteReferenciaExpedienteAdminOrigen = "referenciaExpedienteAdminOrigen";
    public final static String expedienteFechaRegistro = "fechaRegistro";
    public final static String expedienteFechaMovimiento = "fechaMovimiento";
    public final static String expedienteHoraMovimiento = "horaMovimiento";
    public final static String expedienteFechaDeCierre = "fechaDeCierre";
    public final static String expedienteTipoDocumentoOrigenAlteracion = "tipoDocumentoOrigenAlteracion";
    public final static String expedienteInfoDocumentoOrigenAlteracion = "infoDocumentoOrigenAlteracion";
    public final static String expedienteCodigoDescriptivoAlteracion = "codigoDescriptivoAlteracion";
    public final static String expedienteDescripcionAlteracion = "descripcionAlteracion";
    public final static String expedienteNifPresentador = "nifPresentador";
    public final static String expedienteNombreCompletoPresentador = "nombreCompletoPresentador";
    public final static String expedienteNumBienesInmueblesUrbanos = "numBienesInmueblesUrbanos";
    public final static String expedienteNumBienesInmueblesRusticos = "numBienesInmueblesRusticos";
    public final static String expedienteNumBienesInmueblesCaractEsp = "numBienesInmueblesCaractEsp";
    public final static String expedienteCodProvinciaNotaria = "codProvinciaNotaria";
    public final static String expedienteCodPoblacionNotaria = "codPoblacionNotaria";
    public final static String expedienteCodNotaria = "codNotaria";
    public final static String expedienteAnnoProtocoloNotarial = "annoProtocoloNotarial";
    public final static String expedienteProtocoloNotarial = "protocoloNotarial";
    public final static String expedienteReferenciasCatastrales = "referenciasCatastrales";


    /** Identificador de las variables de Direccion.*/
    public final static String direccionProvinciaINE = "provinciaINE";
    public final static String direccionMunicipioINE = "municipioINE";
    public final static String direccionCodigoMunicipioDGC = "codigoMunicipioDGC";
    public final static String direccionNombreEntidadMenor = "nombreEntidadMenor";
    public final static String direccionCodigoVia = "codigoVia";
    public final static String direccionPrimerNumero = "primerNumero";
    public final static String direccionPrimeraLetra = "primeraLetra";
    public final static String direccionSegundoNumero = "segundoNumero";
    public final static String direccionSegundaLetra = "segundaLetra";
    public final static String direccionKilometro = "kilometro";
    public final static String direccionBloque = "bloque";
    public final static String direccionPuerta = "puerta";
    public final static String direccionEscalera = "escalera";
    public final static String direccionPlanta = "planta";
    public final static String direccionDireccionNoEstructurada = "direccionNoEstructurada";
    public final static String direccionCodigoPostal = "codigoPostal";
    public final static String direccionNombreVia = "nombreVia";
    public final static String direccionApartadoCorreos = "apartadoCorreos";
    public final static String direccionTipoVia = "tipoVia";
    public final static String direccionNombreProvincia = "nombreProvincia";
    public final static String direccionNombreMunicipio = "nombreMunicipio";

    /** Atributos entidad generadora */
    public final static String entidadGeneradoraCodigo = "codigo";

    /** Tipos de documentos */
    public final static String TIPO_DOCUMENTO_PUBLICO = "P";
    public final static String TIPO_DOCUMENTO_PRIVADO = "R";

    /** Estados del expediente */
    public final static int ESTADO_REGISTRADO = 1;
    public final static int ESTADO_ASOCIADO = 2;
    public final static int ESTADO_RELLENADO = 3;
    public final static int ESTADO_SINCRONIZADO = 4;
    public final static int ESTADO_MODIFICADO = 5;
    public final static int ESTADO_FINALIZADO = 6;
    public final static int ESTADO_GENERADO = 7;
    public final static int ESTADO_ENVIADO = 8;
    public final static int ESTADO_CERRADO = 9;

    /** Interfaces origen del programa y la accion para el flujo de la gui */
    public final static int FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR = 1;
    public final static int FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR = 2;
    public final static int FRAME_ORIGEN_CREAR_EXP_ACCION_GUARDAR = 3;
    public final static int FRAME_ORIGEN_CREAR_EXP_ACCION_SIGUIENTE = 4;
    public final static int FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR = 5;
    public final static int FRAME_ORIGEN_BUSQUEDA_ACCION_NUEVO = 6;
    public final static int FRAME_ORIGEN_GESTION_EXP_VER_ESTADO = 7;
    public final static int FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE = 8;
    public final static int FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR = 9;
    public final static int FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR = 10;
    public final static int FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR = 11;
    public final static int FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR = 12;
    public final static int FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP = 13;
    public final static int FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL = 14;
    public final static int FRAME_ORIGEN_GESTION_EXP_ACCION_EXPORTAR_FICHERO = 15;
    public final static int FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR = 16;
    public final static int FRAME_ORIGEN_GESTION_EXP_ACCION_ASOCIAR_DATOS_GRAFICOS = 17;
    public final static int FRAME_ORIGEN_GESTION_EXP_ACCION_CERRAR = 18;
    public final static int FRAME_ORIGEN_ASOCIADO_EXP = 19;
    public final static int FRAME_ORIGEN_NO_ASOCIADO_EXP = 20;
    public final static int FRAME_ORIGEN_OBTENER_INF_CATASTRO = 21;
    public final static int FRAME_ORIGEN_ACTUALIZAR_CATASTRO = 22;
    public final static int FRAME_ORIGEN_GESTION_EXP_CONSULTA_ESTADO = 23;
    public final static int FRAME_ORIGEN_BUSQUEDA_CONSULTA_ESTADO = 24;
    

    /** Interface destino del pragoma para el flujo de la gui */
    public final static int FRAME_DESTINO_ASOCIAR_PARCELAS= 1;
    public final static int FRAME_DESTINO_ASOCIAR_PARCELAS_CREAR_EXP= 2;
    public final static int FRAME_DESTINO_GESTION_EXP_CREA_EXP = 3;
    public final static int FRAME_DESTINO_GESTION_EXP_UPDATE_EXP = 4;
    public final static int FRAME_DESTINO_GESTION_EXP = 6;
    public final static int FRAME_DESTINO_VER_ESTADO = 7;
    public final static int FRAME_DESTINO_CREAR_EXP = 8;
    public final static int FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE_CREAR_EXP = 9;
    public final static int FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE=10;
    public final static int FRAME_DESTINO_MODIFICAR_DATOS_EXP = 11;
    public final static int FRAME_DESTINO_MODIFICAR_INF_CATASTRAL = 12;
    public final static int FRAME_DESTINO_GESTION_EXP_EXPORTAR = 13;
    public final static int FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_SINCRONIZAR = 14;
    public final static int FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL= 15;
    public final static int FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_ESCRIBIR_CATASTRO_TEMPORAL=16;
    public final static int FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_SINCRONIZAR = 17;
    public final static int FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_ESCRIBIR_CATASTRO_TEMPORAL = 18;
    public final static int FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS = 19;
    public final static int FRAME_DESTINO_BUSCAR_EXPEDIENTES = 20;
    public final static int FRAME_DESTINO_GESTION_EXP_CONSULTA_CATASTRO = 21;
    public final static int FRAME_DESTINO_GESTION_EXP_ACTUALIZA_CATASTRO = 22;
    public final static int FRAME_DESTINO_GESTION_EXP_CONSULTA_ESTADO_CATASTRO = 23;
    public final static int FRAME_DESTINO_BUSQUEDA_CONSULTA_ESTADO = 24;
    public final static int FRAME_ACCION_DEFAULT = -1;

    
    public static String  TAG_CREACION_EXPEDIENTE_REQUEST = 		"CreacionExpedientesRequest";
    public static String  TAG_CONSULTA_CATASTRO_REQUEST = 			"ConsultaCatastroRequest";
    public static String  TAG_ACTUALIZA_CATASTRO_REQUEST = 			"ActualizaCatastroRequest";
    public static String  TAG_CONSULTA_ESTADO_EXPEDIENTE_REQUEST =	"ConsultaEstadoExpedienteRequest";
    
    public static String  TAG_CREACION_EXPEDIENTE_IN = 			"Expedientes_In";
    public static String  TAG_CONSULTA_CATASTRO_IN = 			"ConsultaCatastro_In";
    public static String  TAG_ACTUALIZA_CATASTRO_IN = 			"ActualizaCatastro_In";
    public static String  TAG_CONSULTA_ESTADO_EXPEDIENTE_IN =	"ConsultaExpediente_In";
    
    public static String  CERTIFICADO_NOMBRE_SOLICITANTE =	"certificadoNombreSolicitante";
    public static String  CERTIFICADO_DNI_SOLICITANTE =	"certificadoDniSolicitante";
    public static String  CERTIFICADO_KEYSTOREFILE =	"certificadoFichero";
    public static String  CERTIFICADO_PASSWORD =	"certificadoPassword";
    
    public static String  CERTIFICADO_DATOS =	"certificadoDatos";
    
    
}
