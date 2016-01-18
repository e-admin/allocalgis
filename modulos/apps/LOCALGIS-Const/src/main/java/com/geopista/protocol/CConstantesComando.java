/**
 * CConstantesComando.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol;

/**
 * @author SATEC
 * @version $Revision: 1.2 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2011/12/29 18:56:30 $
 *          $Name:  $
 *          $RCSfile: CConstantesComando.java,v $
 *          $Revision: 1.2 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CConstantesComando {

    public static final int TIPO_OBRA_MAYOR=0;
    public static final int TIPO_OBRA_MENOR=1;
    public static final int TIPO_ACTIVIDAD=2;
    public static final int TIPO_ACTIVIDAD_NO_CALIFICADA=3;
    public static final int TIPO_OCUPACION=-1;

    public static String servletOrtofotoUrl = "http://localhost:8081/Ortofoto/CServletOrtofoto";
	public static String servletLicenciasUrl = "http://localhost:54321/Licencias/CServletLicencias";
	public static String anexosLicenciasUrl = "http://localhost:54321/Anexos/";
	public static String loginLicenciasUrl = "http://localhost:54321/Licencias/";
	public static String loginAlpUrl = "http://localhost:54321/Alptolocalgis/";
	public static String planeamiento = "http://localhost:54321/Planeamiento/";
	public static String adminCartografiaUrl = "http://localhost:5432/Geopista/";
    public static String plantillasLicenciasUrl = "http://localhost:8081/Plantillas/licencias/";
    public static String plantillasActividadUrl = "http://localhost:8081/Plantillas/actividad/";
    public static String plantillasOcupacionesUrl = "http://localhost:8081/Plantillas/ocupacion/";
    public static String servidorUrl = "http://localhost:8081/";
    public static String loginGeopistaInicio = "http://localhost:8081/Geopistainicio";

	public static int timeout = 15000;

	//Identificadores de aplicación
	public static final String APP_LICENCIAS = "LICENCIAS";
	public static final String APP_OCUPACIONES = "OCUPACIONES";

	//**********************************************
	//** Comandos relacionados con las licencias
	//***********************************************
	public static final int CMD_LICENCIAS_CREAR_EXPEDIENTE = 0;
	public static final int CMD_LICENCIAS_MODIFICAR_EXPEDIENTE = 1;
	public static final int CMD_LICENCIAS_GET_TIPOS_OBRA = 2;
	public static final int CMD_LICENCIAS_GET_TIPOS_LICENCIA = 3;
	public static final int CMD_LICENCIAS_GET_TIPOS_ANEXO = 4;
	public static final int CMD_LICENCIAS_GET_ESTADOS = 5;
	public static final int CMD_LICENCIAS_GET_VIAS_NOTIFICACION = 6;
	public static final int CMD_LICENCIAS_GET_SOLICITUD_LICENCIA = 7;
	public static final int CMD_LICENCIAS_GET_SEARCHED_EXPEDIENTES = 8;
	public static final int CMD_LICENCIAS_GET_TIPOS_FINALIZACION = 9;
	public static final int CMD_LICENCIAS_GET_TIPOS_TRAMITACION = 10;
	public static final int CMD_LICENCIAS_GET_TIPOS_NOTIFICACION = 11;
	public static final int CMD_LICENCIAS_GET_ESTADOS_NOTIFICACION = 12;
	public static final int CMD_LICENCIAS_GET_ESTADOS_RESOLUCION = 13;
	public static final int CMD_LICENCIAS_GET_NOTIFICACIONES = 14;
	public static final int CMD_LICENCIAS_GET_SEARCHED_PERSONAS = 15;
	public static final int CMD_LICENCIAS_GET_SEARCHED_ADDRESSES = 16;
	public static final int CMD_LICENCIAS_GET_SOLICITUDES_EXPEDIENTES_INFORME = 17;
	public static final int CMD_LICENCIAS_GET_PARCELARIO = 18;
	public static final int CMD_LICENCIAS_GET_PLANTILLAS = 19;
	//public static final int CMD_LICENCIAS_GET_PLANTILLA = 20;
	public static final int CMD_LICENCIAS_GET_ESTADOS_DISPONIBLES = 21;
	public static final int CMD_LICENCIAS_GET_NOTIFICACIONES_MENU = 22;
	public static final int CMD_LICENCIAS_GET_EVENTOS = 23;
	public static final int CMD_LICENCIAS_GET_ULTIMOS_EVENTOS = 24;
	public static final int CMD_LICENCIAS_GET_EVENTOS_SIN_REVISAR = 25;
	public static final int CMD_LICENCIAS_GET_HISTORICO = 26;
	public static final int CMD_LICENCIAS_GET_HISTORICO_EXPEDIENTE = 27;
	public static final int CMD_LICENCIAS_GET_SEARCHED_REFERENCIAS_CATASTRALES = 28;
	public static final int CMD_LICENCIAS_GET_ESTADOS_PERMITIDOS = 29;
    public static final int CMD_LICENCIAS_GET_NOTIFICACIONES_PENDIENTES= 30;
    public static final int CMD_LICENCIAS_GET_EVENTOS_PENDIENTES= 31;
	public static final int CMD_LICENCIAS_GET_SEARCHED_ADDRESSES_BY_NUMPOLICIA = 32;
    public static final int CMD_LICENCIAS_GET_DIRECCION_MAS_CERCANA = 33;
    public static final int CMD_LICENCIAS_GET_SEARCHED_LICENCIAS_OBRA= 34;
    public static final int CMD_LICENCIAS_BLOQUEAR_EXPEDIENTE= 35;
    public static final int CMD_INSERTAR_INFORME= 36;
    public static final int CMD_LICENCIAS_GET_SEARCHED_EXPEDIENTES_PLANOS=37;
    public static final int CMD_LICENCIAS_DELETE_GEOMETRIA_OCUPACION= 38;

    public static final int CMD_LICENCIAS_ACTUALIZAR_IDSIGEM = 39;
    public static final int CMD_LICENCIAS_ACTUALIZAR_ESTADOSIGEM = 40;
    public static final int CMD_LICENCIAS_PUBLICAR_EXPEDEINTE_SIGEM = 41;
    public static final int CMD_LICENCIAS_OBTENER_IDSIGEM = 42;
    
    public static final int CMD_LICENCIAS_ANEXO_ALFRESCO = 43;
    public static final int CMD_LICENCIAS_ANEXO_GETID = 44;
    
    //Comandos relacionados con las licencias de geocuenca: a partir del 400
    
    /**
     * Ordenantes de un pago (Licencias Geocuenca)
     */
    public static final int CMD_LICENCIAS_GET_SEARCHED_ORDENANTES = 401;
    
    /**
     * Obtener tasas para los distintos tipos de licencias (Licencias Geocuenca)
     */
    public static final int CMD_LICENCIAS_GET_TASAS = 402;

    /**
     * Modificar las tasas para los distintos tipos de licencias (Licencias Geocuenca)
     */
    public static final int CMD_LICENCIAS_MODIFICAR_TASAS = 403;

    /**
     * Reinicia la numeración de los expedientes (Licencias Geocuenca)
     */
    public static final int CMD_LICENCIAS_RESET_NUMERATION = 404;
    
    /**
     * Recoge el año en curso de los expedientes (Licencias Geocuenca)
     */
    public static final int CMD_LICENCIAS_GET_ANIO_EXPEDIENTE = 405;
    
    /**
     * Recoge el siguiente número de expediente (Licencias Geocuenca)
     */
	public static final int CMD_LICENCIAS_GET_NEXT_EXP_NUMBER = 406;
    
	/**
	 * Obtiene el siguiente valor de una secuencia para nombrar los ficheros subidos al servidor
	 */
	public static final int CMD_LICENCIAS_GET_NEXT_FILE_SEQUENCE = 407;
	
	
	/**
	 * Actualizar el año en curso de numeración de expediente
	 */
	public static final int CMD_LICENCIAS_UPDATE_ANIO_EXPEDIENTE = 408;
	
	/**
	 * Obtener los documentos asociados a una licencia hasta una fecha dada
	 */
	public static final int CMD_LICENCIAS_GET_DOCUMENTOS = 409;
	
	
	 public static final int CMD_LICENCIAS_GET_SOLICITUDES_LICENCIA = 45;
	
	
	//**********************************************
	//** Comandos relacionados con el control
	//***********************************************
	public static final int CMD_CONTROL_LOGIN = 100;


	//******************************************
	//** Operaciones sobre los anexos
	//****************************************
	public static final int CMD_ANEXO_ADDED = 201;
	public static final int CMD_ANEXO_DELETED = 202;
	//public static final int CMD_ANEXO_UPDATED = 203;

	//******************************************
	//** Operaciones sobre el historico
	//******************************************
	public static final int CMD_HISTORICO_ADDED = 201;
	public static final int CMD_HISTORICO_MODIFIED = 202;
	public static final int CMD_HISTORICO_DELETED = 203;
	
	//******************************************
	//** Operaciones sobre las ortofotos
	//******************************************
	public static final int CMD_ORTHOPHOTO_IMPORT = 300;
	public static final int CMD_ORTHOPHOTO_SEND = 301;
	
    //******************************************
    //** Eventos y Notificaciones pendientes
    //******************************************
    public static final int CMD_NOTIFICACION_NOTIFICADA=3;
    public static final int CMD_EVENTO_REVISADO=1;

    //******************************************
    //** Configuracion de UploadFile
    //******************************************
    public static int MaxMemorySize=1024; // 100Kb
    public static int MaxRequestSize=1024*1000*1; //1M

    //******************************************
    //** Configuracion de Proxy para Informes
    //******************************************
    public static int REPORTS_PORT=8083;

     //******************************************
    //** Tipos de Notificacion.
    //******************************************
    public static int notificacionMejoraDatos= 0;
    public static int notificacionEsperaAlegacion= 1;
    public static int notificacionResolucionAprobacion= 2;
    public static int notificacionResolucionDenegacion= 3;

    //******************************************
    //** Estados de las licencias
    //******************************************
    public static final int ESTADO_INICIAL= 0;
    public static final int ESTADO_APERTURA_EXPEDIENTE = 1;
    public static final int ESTADO_MEJORA_DATOS = 2;
    public static final int ESTADO_SOLICITUD_INFORMES = 3;
    public static final int ESTADO_ESPERA_INFORMES = 4;
    public static final int ESTADO_EMISION_INFORME_RESOLUCION = 5;
    public static final int ESTADO_ESPERA_ALEGACIONES = 6;
    public static final int ESTADO_ACTUALIZACION_INFORME_RESOLUCION = 7;
    public static final int ESTADO_EMISION_PROPUESTA_RESOLUCION = 8;
    public static final int ESTADO_NOTIFICACION_DENEGACION = 9;
    public static final int ESTADO_FORMALIZACION_LICENCIA = 10;
    public static final int ESTADO_NOTIFICACION_APROBACION = 11;
    public static final int ESTADO_EJECUCION = 12;
    public static final int ESTADO_DURMIENTE = 13;
    public static final int ESTADO_PUBLICAR_BOP = 14;
    public static final int ESTADO_REMISION_CP = 15;
    public static final int ESTADO_REMISION_DG = 16;
    public static final int ESTADO_SOLICITUD_ACTA = 17;
    public static final int ESTADO_LICENCIA_FUNCIONAMIENTO = 18;
    public static final int ESTADO_PAGO = 20;
    public static final int ESTADO_REMISION_ORGANISMOS = 21;
    public static final int ESTADO_DEFICIENCIAS_ADMINISTRATIVAS = 22;
    public static final int ESTADO_DEFICIENCIAS_ORGANISMOS = 23;
    public static final int ESTADO_PENDIENTE_INFORME_DEF_ORGANISMOS = 24;
    public static final int ESTADO_COMPROBACION_DATOS = 25;
    public static final int ESTADO_CORRECION_DATOS = 30;
    
    //public static final int ESTADO_SOLICITUD_INFORMES = 40;
    public static final int ESTADO_DEFICIENCIAS = 41;
    public static final int ESTADO_INFORMES_FAVORABLES = 42;
    public static final int ESTADO_INFORMES_DESFAVORABLES = 43;
    public static final int ESTADO_DESISTIDO = 44;
    public static final int ESTADO_CADUCADO = 45;
    public static final int ESTADO_COMISION_LICENCIA = 50;
    public static final int ESTADO_JUNTA_GOBIERNO = 55;
    public static final int ESTADO_EXPEDIENTE_CONCEDIDO = 56;
    public static final int ESTADO_EXPEDIENTE_DENEGADO = 57;
    public static final int ESTADO_EXPEDIENTE_DESISTIDO = 58;
    public static final int ESTADO_EXPEDIENTE_CADUCADO = 59;
    public static final int ESTADO_SOLICITUD_INICIO_OBRAS = 60;
    public static final int ESTADO_COMISION_INICIO_OBRAS = 70;
    public static final int ESTADO_ARCHIVO_GESTION = 80;
    public static final int ESTADO_SOLICITUD_PRORROGA = 90;
    public static final int ESTADO_COMISION_PRORROGA = 100;
    public static final int ESTADO_SOLICITUD_FINAL_OBRA = 110;
    public static final int ESTADO_COMPROBACION_DATOS_II = 120;
    public static final int ESTADO_CORRECCION_DATOS_II = 130;
    public static final int ESTADO_COMISION_FINAL_OBRA = 140;
    public static final int ESTADO_ARCHIVO_TEMPORAL = 150;
    public static final int ESTADO_ENMIENDA_DEFICIENCIAS = 160;
    
    
    


    public static final int ESTADO_DURMIENTE_OCUPACION = 3;
    public static final int TIPO_FINALIZACION_EXPRESA = 0;
    public static final int TIPO_FINALIZACION_SILENCIO_ADMINISTRATIVO = 1;


    public static final String PATRON_SUSTITUIR_BBDD = "\\%\\%ENTORNO\\%\\%";

    public static String documentosPath= "";
	public static int guardarBD=0;

    public static final int tipoObraDemolicion= 2;
    public static final int tipoObraNuevaPlanta=0;

    public static final String SOLAR="SOLAR";
    public static final String NUEVA_PLANTA="P";
		

}
