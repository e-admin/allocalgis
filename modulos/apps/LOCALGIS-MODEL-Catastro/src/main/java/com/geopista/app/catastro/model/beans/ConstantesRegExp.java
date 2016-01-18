/**
 * ConstantesRegExp.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.text.DecimalFormat;

import com.geopista.app.catastro.registroExpedientes.CatastroClient;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 08-ene-2007
 * Time: 11:58:46
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase utilizada por el cliente y el servidor que define las constantes de las acciones que el cliente desea hacer
 * en la parte servidora. En la parte servidora se comparara la accion obtenida y se realizara la accion deseada. El
 * resto de constantes son para permitir obtener los objetos de la hash params en el envio de la peticion al servidor
 * o utilzadas en el cliente y servidor.
 * */

public class ConstantesRegExp
{
    //Acciones sobre BD.
    public static final int ACTION_CREAR_EXPEDIENTE = 0;
    public static final int ACTION_GET_EXPEDIENTES_USUARIO = 1;
    public static final int ACTION_GET_USUARIOS_CON_EXPEDIENTE = 2;
    public static final int ACTION_GET_ENTIDADES_GENERADORAS = 3;
    public static final int ACTION_GET_CODIGOS_ESTADOS = 4;
    public static final int ACTION_GET_FINCA_CATASTRO_POR_REFERENCIA_CATASTRAL = 5;
    public static final int ACTION_GET_FINCA_CATASTRO_BUSCADAS = 6;
    public static final int ACTION_GET_FINCA_CATASTRO_BUSCADAS_POR_DIR = 7;
    public static final int ACTION_GET_ESTADO_SIGUIENTE = 9;
    public static final int ACTION_UPDATE_EXPEDIENTE = 10;
    public static final int ACTION_CONSULTA_HISTORICO_FICHEROS = 11;
    public static final int ACTION_GET_VIA_POR_NOMBRE = 12;
    public static final int ACTION_COMPROBAR_ACTUALIZACIONES_ENVIOS = 13;
    public static final int ACTION_GET_CODIGO_NOMBRE_PROVINCIA = 14;
    public static final int ACTION_GET_CODIGO_NOMBRE_MUNICIPIO = 15;
    public static final int ACTION_GUARDAR_CONFIGURACION = 16;
    public static final int ACTION_GET_CONFIGURACION = 17;
    public static final int ACTION_GET_BIEN_INMUEBLE_BUSCADAS = 18;
    public static final int ACTION_GET_BIEN_INMUEBLE_BUSCADAS_POR_DIR = 19;
    public static final int ACTION_GET_TIPOS_EXPEDIENTES = 20;
    public static final int ACTION_GET_BIEN_INMUEBLE_BUSCADOS_POR_TITULAR = 21;
    public static final int ACTION_MODIFICAR_EXPEDIENTE = 22;
    public static final int ACTION_CREAR_EXPEDIENTE_CATASTRO = 23;
    public static final int ACTION_CREAR_FICHERO = 24;
    public static final int ACTION_EXPORTACION_MASIVA = 25;
    public static final int ACTION_GET_ID_USUARIO = 26;
    public static final int ACTION_SINCRONIZA_EXPEDIENTE = 27;
    public static final int ACCTION_ELIMINAR_CATASTRO_TEMPORAL_EXP = 28;
    public static final int ACCTION_FECHA_INICIO_PERIODO_EXP = 29;
    public static final int ACTION_ESCRIBIR_CATASTRO_TEMPORAL= 30;
    public static final int ACCTION_GET_TODOS_LOS_USUARIOS = 31;
    public static final int ACTION_GET_FINCA_CATASTRO_RUSTICA_BUSCADAS_POR_POLIGONO = 32;
    public static final int ACTION_GET_BIEN_INMUEBLE_RUSTICO_BUSCADAS_POR_POLIGONO = 33;
    public static final int ACTION_GET_PARCELAS_EXPEDIENTE = 34;  
    public static final int ACTION_EXISTE_PARCELA_BD = 35;
    public static final int ACTION_EXISTE_BI_BD = 36;
    public static final int ACTION_EXPORTACION_MASIVA_VARPAD = 37;
    public static final int ACTION_EXPORTACION_MASIVA_FINENTRADA = 38;
    public static final int ACTION_INSERTAR_DATOS_SALIDA = 39;
    public static final int ACTION_GET_CODIGO_DGC_MUNICIPIO = 40;
    public static final int ACTION_GET_PONENCIA_ZONA_VALOR = 41;
    public static final int ACTION_GET_PONENCIA_URBANISTICA = 42;
    public static final int ACTION_GET_PONENCIA_POLIGONO = 43;
    public static final int ACTION_GET_PONENCIA_TRAMOS = 44;
    public static final int ACTION_BORRAR_EXPEDIENTE = 45;
    public static final int ACTION_GET_ID_EXPEDIENTE = 46;
    public static final int ACTION_GET_CODIGO_DIGITO_CONTROL_DNI = 47;
    public static final int ACTION_GET_EXPEDIENTES_USUARIO_FIN_ENTADA_MASIVO = 48;
    public static final int ACTION_GET_INFO_CATASTRAL = 49;
    public static final int ACTION_GET_VIA_POR_CODIGO_VIA = 50;
    public static final int ACTION_IS_PERMISOS_CONSULTA_INFO_CATASTRAL_FISICO_ECONOMICO = 51;
    public static final int ACTION_IS_PERMISOS_CONSULTA_INFO_CATASTRAL_TITULARIDAD = 52;
    public static final int ACTION_ESCRIBIR_CATASTRO_TEMPORAL_VARIACIONES= 53;
    public static final int ACTION_ELIMINAR_FINCA_CATASTRO_TEMPORAL = 54;
    public static final int ACTION_GET_LIST_ENTIDADES_GENERADORAS = 55;
    public static final int ACTION_UPDATE_IDENTIFICADORES_DIALOGO = 56;
    public static final int ACTION_GET_IDENTIFICADOR_DIALOGO_BIEN = 57;
    public static final int ACTION_GET_IDENTIFICADOR_DIALOGO_FINCA = 58;
    public static final int ACTION_ACTUALIZA_CATASTRO_TEMPORAL= 59;
    public static final int ACTION_CONSULTA_ESTADO_FINCA_OVC = 60;
    public static final int ACTION_CONSULTA_ESTADO_BIEN_OVC = 61;
    public static final int ACTION_GET_ID_FINCA_CATASTRO = 62;
    public static final int ACTION_EXISTE_REFERENCIA_CATASTRO_TEMPORAL = 63;
    public static final int ACTION_ACTUALIZAR_OVC_ACOPLADO = 64;
    public static final int ACTION_GET_VIA_POR_NOMBRE_Y_CODIGO = 65;

    
    public static CatastroClient clienteCatastro = null;
    

    //Constantes para seleccion de elem en la llamadas a servlet de BD.
    public static final String OBJETO_EXPEDIENTE = "Expediente";
    public static final String OBJETO_LISTA_EXP = "ListaExp";
    public static final String OBJETO_LISTA_USER = "ListaUser";
    public static final String OBJETO_LISTA_REFERENCIAS_CATASTRALES = "ListaRefCatastrales";
    public static final String BOOLEAN_EXP_CERRADO = "ExpCerrado";
    public static final String STRING_PATRON_REF_CATASTRAL = "PatronRefCatastral";
    public static final String STRING_PATRON_NOMBRE_VIA = "PatronNombreVia";
    public static final String STRING_PATRON_TIPO_VIA = "PatronTipoVia";
    public static final String STRING_TIPO_VIA = "TipoVia";
    public static final String STRING_CODIGO_VIA = "CodigoVia";
    public static final String STRING_NOMBRE_VIA = "NombreVia";
    public static final String STRING_NUMERO_EXP = "NumeroExp";
    public static final String CODIGO_MUNICIPIO_INE = "CodMunicipioINE";
    public static final String CODIGO_PROVINCIA = "CodProvincia";
    public static final String PARAM_CONFIGURACION = "param_configuracion";
    public static final String STRING_CONVENIO = "convenio";
    public static final String STRING_PATRON_NIF = "PatronNif";
    public static final String OBJETO_FICHERO = "Fichero";
    public static final String ARRAY_LISTA_NUM_EXP = "listaNumExp";
    public static final String STRING_MODO_TRABAJO = "modoTrabajo";
    public static final String STRING_DIRECTORIO_EXPORTACION = "directorioExportacion";
    public static final String STRING_NOMBRE_FICHERO_FIN_ENTRADA_EXPORTACION = "nombreFicheroFinEntradaExportacion";
    public static final String STRING_NOMBRE_FICHERO_VARPAD_EXPORTACION = "nombreFicheroVARPADExportacion";
    public static final String STRING_NOMBRE_FICHERO = "nombreFicheroExportacion";
    public static final String STRING_PATRON_POLIGONO = "PatronPoligono";
    public static final String STRING_PATRON_PARCELA = "PatronParcela";
    public static final String STRING_REF_CATASTRAL = "ReferenciaCatastral";
    public static final String STRING_ID_BI = "IDBI";
    public static final String OBJETO_UNIDAD_DATOS_INTERCAMBIO = "udsa";
    public static final String BOOLEAN_INSERTAR_EXPEDIENTE = "insertarExpediente";
    public static final String STRING_PONENCIA_ZONA_VALOR = "PonenciaZonaValor";
    public static final String STRING_PONENCIA_URBANISTICA = "PonenciaUrbanistica";
    public static final String STRING_PONENCIA_POLIGONO = "PonenciaPoligono";
    public static final String STRING_PONENCIA_TRAMOS = "PonenciaTramos";
    public static final String STRING_PERMISO_BORRAR_EXP = "Geopista.Catastro.BorrarExpedientes";
    public static final String STRING_PERMISO_FISICO_ECONOMICO = "Geopista.Catastro.Consulta Parcela Catastral";
    public static final String STRING_PERMISO_TITULARIDAD = "Geopista.Catastro.Consulta Cargos";
    public static final String OBJETO_LISTA_ESTADOS_EXP = "ListaEstadosExp";
    public static final String BOOLEAN_EXPORTAR_TODOS_EXPEDIENTE = "exportarTodosExpediente";
    public static final String STRING_IDPARCELA = "IdParcela";
    public static final String STRING_ESCRIBE_CATASTRO_TEMPORAL = "isCatastroTemporal";
    public static final String STRING_IS_EXP_SITUACION_FINAL = "isexpSitFinales";
    public static final String OBJETO_LST_IDENTIFICADORES_DIALOGO = "lstIdentificadoresDialogo";
    public static final String OBJETO_IDENTIFICADOS_DIALOGO = "identificadorDialogo";

    //Estados del expediente
    public final static int ESTADO_REGISTRADO = 1;
    public final static int ESTADO_ASOCIADO = 2;
    public final static int ESTADO_RELLENADO = 3;
    public final static int ESTADO_SINCRONIZADO = 4;
    public final static int ESTADO_MODIFICADO = 5;
    public final static int ESTADO_FINALIZADO = 6;
    public final static int ESTADO_GENERADO = 7;
    public final static int ESTADO_ENVIADO = 8;
    public final static int ESTADO_CERRADO = 9;
    
    public final static int ACL_CATASTRO = 5;

    public static DecimalFormat decimalFormatter = new DecimalFormat("00000.00");




}
