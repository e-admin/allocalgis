/**
 * CWorkflow.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.licencias.workflow;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.Resolucion;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.server.database.COperacionesDatabaseLicencias;
import com.geopista.server.database.CPoolDatabase;

/**
 * @author SATEC
 * @version $Revision: 1.2 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2009/07/30 10:25:41 $
 *          $Name:  $
 *          $RCSfile: CWorkflow.java,v $
 *          $Revision: 1.2 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CWorkflow {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CWorkflow.class);
    private static final boolean modoDebug=false; //Esta variable sirve para comprobar si la aplicación
    //funciona correctamente cuando esta variable esta a true en vez de que los tiempos esten en dias
    //se consideran que están en minutos.

    public static boolean checkExpedientes()
    {
    	try
        {
            //Expedientes sobre los que se va a realizar workflow
            Vector tiposLicencia= new Vector();
            tiposLicencia.add(new CTipoLicencia(CConstantesComando.TIPO_OBRA_MAYOR,"",""));
            tiposLicencia.add(new CTipoLicencia(CConstantesComando.TIPO_OBRA_MENOR,"",""));
            tiposLicencia.add(new CTipoLicencia(CConstantesComando.TIPO_ACTIVIDAD,"",""));
            tiposLicencia.add(new CTipoLicencia(CConstantesComando.TIPO_ACTIVIDAD_NO_CALIFICADA,"",""));


            logger.debug("WORKFLOW.1 Numero de conexiones: "+CPoolDatabase.getNumeroConexiones());
            //Interpretamos que todas las fechas de caducidad vienen en días.
        	long time=500;
            //Comprobamos que el expedientes están a punto de cumplirse
            /** Chequeamos que expedientes van a cambiar de estado por plazo de workflow dentro de 5 dias (CConstantesWorkflow.diasActivacionEvento).
             * Para aquellos que lo cumplan, se insertara un evento informando de la proximidad del cambio de estado
             * del expediente por expiracion de plazo de workflow (tabla workflow.plazo). */
			checkEventosPlazosExpiracion(tiposLicencia);
			logger.debug("WORKFLOW.2 Numero de conexiones: "+CPoolDatabase.getNumeroConexiones());
			Thread.sleep(time);

            //Comprobamos que expedientes deben ser cambiados de estado
            /** Cambiamos el estado (a estado tabla workflow.id_plazoestado) de aquellos expedientes cuyo estado actual
             * (estado_actual= (tabla workflow.id_estado=workflow.id_nextestado)) ha caducado por expiracion del plazo
             *  de workflow (tabla workflow.plazo).*/
			checkPlazosExpiracion(tiposLicencia);
			logger.debug("WORKFLOW.3 Numero de conexiones: "+CPoolDatabase.getNumeroConexiones());
			Thread.sleep(time);

            //Para aquellos expedientes que tengan un estado que no tiene un plazo de
            //expiración en la base de datos (tabla workflow) se coge la constante por defecto.
            //Aqui lo que se hace es avisar que el evento se va a producir en breve

            /** Chequeamos que expedientes van a cambiar a estado DURMIENTE por silencio administrativo en un plazo de
             * CConstantesWorkflow.diasActivacionEvento(= 5 dias). Cuando se cumple el plazo por silencio administrativo,
             * el estado del expediente pasa a estado DURMIENTE. Cumplen por silencio administrativo,
             * aquellos expedientes cuyo estado actual no tiene un plazo establecido en el workflow.
             * Para aquellos expedientes que expiren por silencio administrativo en el plazo de 5 dias (CConstantesWorkflow.diasActivacionEvento),
             * se insertara un evento de aviso de proximidad de fecha de cambio de estado. */
			checkEventosSilenciosAdministrativos(tiposLicencia);
			logger.debug("WORKFLOW.4 Numero de conexiones: "+CPoolDatabase.getNumeroConexiones());
			Thread.sleep(time);

            /** Para aquellos expedientes que tengan un estado que no tiene un plazo de
             expiración en la base de datos se coge la constante por defecto.
             Aqui se cambia de estado y se pone irremediablemente a silencio administrativo. */
			checkSilenciosAdministrativos(tiposLicencia);
			logger.debug("WORKFLOW.5 Numero de conexiones: "+CPoolDatabase.getNumeroConexiones());
			Thread.sleep(time);

			return true;

		} catch (Exception ex) {
			logger.error("Exception al ejecutar el workflow: ", ex);
			return false;
		}
	}


	public static boolean checkEventosSilenciosAdministrativos(Vector tiposLicencia)
    {
		try {
        	//Vector expedientes = COperacionesDatabaseLicencias.getSearchedExpedientes(new Hashtable(),null, tiposLicencia);
            Vector expedientes = COperacionesDatabaseLicencias.getSearchedExpedientesSilencioAdmin(new Hashtable(),null, tiposLicencia);
    		logger.info("expedientes.size(): " + expedientes.size());
    		for (int i = 0; i < expedientes.size(); i++)
            {
            	CExpedienteLicencia expedienteLicencia = (CExpedienteLicencia) expedientes.elementAt(i);
            	logger.debug("expedienteLicencia.getSilencioEvent(): " + expedienteLicencia.getSilencioEvent());
            	if (expedienteLicencia.getEstado().getIdEstado()==CConstantesComando.ESTADO_DURMIENTE) {
					logger.debug("Skipped. expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". expedienteLicencia.getEstado().getIdEstado(): " + expedienteLicencia.getEstado().getIdEstado());
					continue;
				}
            	if (expedienteLicencia.getSilencioEvent() != null || expedienteLicencia.getPlazoEvent()!=null) {
					logger.debug("Skipped. expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". expedienteLicencia.getSilencioEvent(): " + expedienteLicencia.getSilencioEvent());
					continue;
				}
                Date fechaEfectiva = getFechaEfectivaSilencioAdministrativo(expedienteLicencia, CConstantesWorkflow.diasActivacionEvento);
			    if (fechaEfectiva==null) continue;
				Date fechaActual = new Date();
				if (fechaEfectiva.before(fechaActual)) {
					String fechaExpiracionFormatted = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEfectiva);
					logger.info("fechaExpiracionFormatted: " + fechaExpiracionFormatted);
					logger.info("Evento silencio administrativo triggered. expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". expedienteLicencia.getFechaApertura(): " + expedienteLicencia.getFechaApertura());
					COperacionesDatabaseLicencias.insertEventoExpediente(expedienteLicencia, CWorkflowLiterales.EVENTOS_SILENCIO_ADMINISTRATIVO, false, true);
				}
			}
			return true;
		} catch (Exception ex) {
			logger.error("Exception: " , ex);
			return false;
		}
	}

    public static Date getFechaEfectivaSilencioAdministrativo(CExpedienteLicencia expedienteLicencia, long diasEvento)
    {
        //Transformamos los dias en milisegundos
        long CADUCIDAD = (CConstantesWorkflow.diasSilencioAdministrativo -diasEvento) *24 * 60 * 60 * 1000;
        if (modoDebug)
            CADUCIDAD = (CConstantesWorkflow.diasSilencioAdministrativo - diasEvento) * 60 * 1000;
        //Angeles piensa que es mejor utilizar la fecha de cambio de estado
        //Date fechaEfectiva = new Date(expedienteLicencia.getFechaCambioEstado()!=null?expedienteLicencia.getFechaCambioEstado().getTime():expedienteLicencia.getFechaApertura().getTime() + CADUCIDAD);
        /** A la fecha de cambio de estado hay que sumar la CADUCIDAD */
        Date fechaEfectiva = new Date(expedienteLicencia.getFechaCambioEstado()!=null?expedienteLicencia.getFechaCambioEstado().getTime() + CADUCIDAD:expedienteLicencia.getFechaApertura().getTime() + CADUCIDAD);
        logger.info("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". fechaEfectiva: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEfectiva));
        if (expedienteLicencia.getEstado().getIdEstado() ==CConstantesComando.ESTADO_EJECUCION)
        {
             if (expedienteLicencia.getSolicitud()!=null &&
                        expedienteLicencia.getSolicitud().getTipoLicencia()!=null &&
                            (expedienteLicencia.getSolicitud().getTipoLicencia().getIdTipolicencia()==
                                             CConstantesComando.TIPO_ACTIVIDAD ||
                             expedienteLicencia.getSolicitud().getTipoLicencia().getIdTipolicencia()==
                                             CConstantesComando.TIPO_ACTIVIDAD_NO_CALIFICADA))
                 {
                     return null;
                 }
                 if (expedienteLicencia.getSolicitud()!=null &&
                        expedienteLicencia.getSolicitud().getTipoLicencia()!=null &&
                            (expedienteLicencia.getSolicitud().getTipoLicencia().getIdTipolicencia()==
                                             CConstantesComando.TIPO_OBRA_MAYOR ||
                             expedienteLicencia.getSolicitud().getTipoLicencia().getIdTipolicencia()==
                                             CConstantesComando.TIPO_OBRA_MENOR))
                 {
                     /** Si un expediente esta en EJECUCION, este pasa a DURMIENTE en FechaLimiteObra */
                       if (expedienteLicencia.getSolicitud().getFechaLimiteObra()!=null)
                          fechaEfectiva=expedienteLicencia.getSolicitud().getFechaLimiteObra();
                 }
            }
       return fechaEfectiva;
    }
    /*******************************************************************************************
     * Esta función introduce un evento cuando queda poco tiempo para que acabe el
     * plazo de expiración de un evento.
     *
     * Coge todos los expedientes cuyo campo plazo_event sea distindo de null y
     * cuyo estado tenga plazo de expiracion
     * @return no devuelve nada
     */
	public static boolean checkEventosPlazosExpiracion(Vector tiposLicencia)
    {
	    try
        {
			Vector expedientes = COperacionesDatabaseLicencias.getSearchedExpedientesExpirables(false,null,tiposLicencia);
			logger.debug("expedientes.size(): " + expedientes.size());
			for (int i = 0; i < expedientes.size(); i++)
            {
            	CExpedienteLicencia expedienteLicencia = (CExpedienteLicencia) expedientes.elementAt(i);
            	
            	Connection connection = null;
        		connection = CPoolDatabase.getConnection();
        		
				CWorkflowLine workflowLine = COperacionesDatabaseLicencias.getWorkflowLine(expedienteLicencia.getEstado().getIdEstado(),
                        new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).intValue(), connection);
				
				if (connection!=null){
					connection.close();
					CPoolDatabase.releaseConexion();   
				}
				
                //la caducidad parece ser el plazo que haya en la base de datos
                //menos los minutos de activacion evento. Parece que viene en minutos
                //Transformamos los días en milisengundos
				long CADUCIDAD = (workflowLine.getPlazo() - CConstantesWorkflow.diasActivacionEvento) * 24 * 60 * 60 * 1000;
                if (modoDebug)
                   CADUCIDAD = (workflowLine.getPlazo() - CConstantesWorkflow.diasActivacionEvento) * 60 * 1000;

                //Date fechaEfectiva = new Date(expedienteLicencia.getFechaCambioEstado().getTime() + CADUCIDAD);
                Date fechaEfectiva = new Date(expedienteLicencia.getFechaCambioEstado()!=null?expedienteLicencia.getFechaCambioEstado().getTime() + CADUCIDAD:expedienteLicencia.getFechaApertura().getTime() + CADUCIDAD);
				logger.info("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". fechaEfectiva: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEfectiva));
                Date fechaActual = new Date();
				if (fechaEfectiva.before(fechaActual)) {
                	logger.info("fechaCambioEstadoFormatted: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(expedienteLicencia.getFechaCambioEstado()!=null?expedienteLicencia.getFechaCambioEstado():expedienteLicencia.getFechaApertura()));
					logger.info("fechaExpiracionFormatted: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEfectiva));
                    logger.info("Plazo:"+workflowLine.getPlazo());
					logger.info("Evento plazo expirado triggered. expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". expedienteLicencia.getFechaApertura(): " + expedienteLicencia.getFechaApertura());
                    //Si el plazo se ha cumplido se mete un evento señalando que un plazo se va a cumplir en breve
					COperacionesDatabaseLicencias.insertEventoExpediente(expedienteLicencia, CWorkflowLiterales.EVENTOS_PLAZO, false, false);
				}
			}
			return true;
		} catch (Exception ex) {
			logger.error("Exception al ejecutar los eventos de plazo de ejecución: ",ex);
			return false;
		}
	}


	public static boolean procesaSilencioAdministrativo(CExpedienteLicencia expedienteLicencia) {
        /*
        logger.info("...................................................................");
        logger.info("................EXPEDIENTE="+expedienteLicencia.getNumExpediente());
        logger.info("................ESTADO="+expedienteLicencia.getEstado().getIdEstado());
        logger.info("................FECHA CAMBIO ESTADO="+new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        logger.info("...................................................................");
		*/
		try {
			logger.info("NumExpediente:" + expedienteLicencia.getNumExpediente() + " IdEstado:"+expedienteLicencia.getEstado().getIdEstado());
            CEstado estado = expedienteLicencia.getEstado();
		    if (estado.getIdEstado()!=CConstantesComando.ESTADO_EJECUCION && expedienteLicencia.getSilencioAdministrativo()!=null &&expedienteLicencia.getSilencioAdministrativo().equals("1"))
            {
                logger.info("Silencio administrativo positivo. Num expediente:"+expedienteLicencia.getNumExpediente());
                estado.setIdEstado(CConstantesComando.ESTADO_EJECUCION);
                Resolucion res= new Resolucion();
                if (expedienteLicencia.getResolucion()!=null)
                    res=expedienteLicencia.getResolucion();
                res.setFavorable(true);
                res.setAsunto("Aprobado por silencio adminitrativo");
                expedienteLicencia.setResolucion(res);
                COperacionesDatabaseLicencias.actualizaResolucion(expedienteLicencia);

                /* actualizamos subparcelas */
                CSolicitudLicencia solicitud= expedienteLicencia.getSolicitud();
                if ((solicitud != null) &&  (solicitud.getTipoLicencia().getIdTipolicencia() == CConstantesComando.TIPO_OBRA_MAYOR) )
                {
                   if  (solicitud.getTipoObra().getIdTipoObra() == CConstantesComando.tipoObraDemolicion)
                        /** por silencio administrativo la resolucion es favorable */
                        COperacionesDatabaseLicencias.actualizarSubparcelas(solicitud.getReferenciasCatastrales(), solicitud.getIdMunicipio(),CConstantesComando.SOLAR);
                  if  (solicitud.getTipoObra().getIdTipoObra() == CConstantesComando.tipoObraNuevaPlanta)
                        /** por silencio administrativo la resolucion es favorable */
                        COperacionesDatabaseLicencias.actualizarSubparcelas(solicitud.getReferenciasCatastrales(), solicitud.getIdMunicipio(),CConstantesComando.NUEVA_PLANTA);

                }

            }
            else
			    estado.setIdEstado(CConstantesComando.ESTADO_DURMIENTE);

			expedienteLicencia.setEstado(estado);
			logger.info("expedienteLicencia.getEstado().getIdEstado(): "+expedienteLicencia.getEstado().getIdEstado());
            COperacionesDatabaseLicencias.updateEstadoExpediente(estado.getIdEstado(), expedienteLicencia.getNumExpediente(),
                    true, new Integer(CConstantesComando.TIPO_FINALIZACION_SILENCIO_ADMINISTRATIVO));
            
            Connection connection = null;
    		connection = CPoolDatabase.getConnection();
    		
    		COperacionesDatabaseLicencias.insertHistoricoExpediente(expedienteLicencia, "workflow", true, connection);
			COperacionesDatabaseLicencias.insertEventoExpediente(expedienteLicencia, new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).intValue(),
                    true, false, connection);

			if (connection!=null){
				connection.close();
				CPoolDatabase.releaseConexion();   
			}		

			//********************************
			//** VU
			//**************************************
			logger.info("expedienteLicencia.getVU(): "+expedienteLicencia.getVU());
			if (expedienteLicencia.getVU().equals("1")){
				COperacionesDatabaseLicencias.insertHitoTelematico(expedienteLicencia,new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).intValue());
			}

			return true;

		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}
	}


	public static boolean procesaPlazoExpiracion(CExpedienteLicencia expedienteLicencia, int idPlazoEstado)
    {
		try {
            /*
            logger.info("...................................................................");
            logger.info("................EXPEDIENTE="+expedienteLicencia.getNumExpediente());
            logger.info("................ESTADO="+expedienteLicencia.getEstado().getIdEstado());
            logger.info("................FECHA CAMBIO ESTADO="+new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            logger.info("...................................................................");
            */
			logger.info("expedienteLicencia.getIdSolicitud(): " + expedienteLicencia.getIdSolicitud());
			logger.info("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente());
			int estadoAntiguo = expedienteLicencia.getEstado().getIdEstado();
			logger.info("estadoAntiguo: " + estadoAntiguo);
			CResultadoOperacion resultadoOperacion = COperacionesDatabaseLicencias.getExpedienteLicencia(expedienteLicencia.getNumExpediente(), null, null, null);
			if (resultadoOperacion.getSolicitudes()==null){
				logger.warn("No se encontro la solicitud del expediente.");
				return false;
			}
			CSolicitudLicencia solicitudLicencia = (CSolicitudLicencia) resultadoOperacion.getSolicitudes().elementAt(0);
			logger.info("expedienteLicencia.getEstado().getIdEstado(): "+expedienteLicencia.getEstado().getIdEstado());
			CEstado estado = expedienteLicencia.getEstado();
			estado.setIdEstado(idPlazoEstado);
			expedienteLicencia.setEstado(estado);
            logger.info("expedienteLicencia.getEstado().getIdEstado(): "+expedienteLicencia.getEstado().getIdEstado());
            //COperacionesDatabaseLicencias.updateEstadoExpediente(idPlazoEstado, expedienteLicencia.getNumExpediente(), true, null);
            COperacionesDatabaseLicencias.updateEstadoExpediente(idPlazoEstado, expedienteLicencia.getNumExpediente(), false, null);
            
            Connection connection = null;
    		connection = CPoolDatabase.getConnection();
    		
    		COperacionesDatabaseLicencias.insertHistoricoExpediente(expedienteLicencia, "workflow", true, connection);
            COperacionesDatabaseLicencias.insertEventoExpediente(expedienteLicencia, new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).intValue(), true, false, connection);

			if (connection!=null){
				connection.close();
				CPoolDatabase.releaseConexion();   
			}	

            if (estadoAntiguo!=idPlazoEstado)
			switch (idPlazoEstado) {
				case CConstantesComando.ESTADO_MEJORA_DATOS:
				    COperacionesDatabaseLicencias.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                             solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionMejoraDatos, solicitudLicencia, true);
                    if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))){
					    COperacionesDatabaseLicencias.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                             solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionMejoraDatos, solicitudLicencia, false);
                    }
					break;
				case CConstantesComando.ESTADO_ESPERA_ALEGACIONES:
					COperacionesDatabaseLicencias.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                             solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionEsperaAlegacion, solicitudLicencia, true);
                    if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))){
					    COperacionesDatabaseLicencias.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                             solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionEsperaAlegacion, solicitudLicencia, false);
                    }
					break;
				case CConstantesComando.ESTADO_NOTIFICACION_DENEGACION:
                    COperacionesDatabaseLicencias.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                             solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionResolucionDenegacion, solicitudLicencia, true);
                    if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))){
                        COperacionesDatabaseLicencias.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                             solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionResolucionDenegacion, solicitudLicencia, false);
                    }
                    break;
                case CConstantesComando.ESTADO_NOTIFICACION_APROBACION:
                    COperacionesDatabaseLicencias.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                             solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionResolucionAprobacion, solicitudLicencia, true);
                    if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))){
                        COperacionesDatabaseLicencias.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                             solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionResolucionAprobacion, solicitudLicencia, false);
                    }
                    break;

				default:

					logger.warn("Operacion no encontrada. estadoAntiguo: " + estadoAntiguo);

			}


			//********************************
			//** VU
			//**************************************
			if (expedienteLicencia.getVU().equals("1")){
				COperacionesDatabaseLicencias.insertHitoTelematico(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia());
			}


			return true;

		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}
	}
    /***************************************************************************
     * Se recogen todos los expedientes que tengan un estado que tenga un plazo de expiración
    * si se ha cumplido el plazo se cambia de estado
    */

	public static boolean checkPlazosExpiracion(Vector tiposLicencia) {

		logger.info("Inicio.");
		try {

			Vector expedientes = COperacionesDatabaseLicencias.getSearchedExpedientesExpirables(true,null,tiposLicencia);
			logger.debug("expedientes.size(): " + expedientes.size());

			for (int i = 0; i < expedientes.size(); i++) {

				CExpedienteLicencia expedienteLicencia = (CExpedienteLicencia) expedientes.elementAt(i);
                if ((expedienteLicencia.getBloqueado() != null) && (expedienteLicencia.getBloqueado().equalsIgnoreCase("1"))){
                    continue;
                }
                
                Connection connection = null;
        		connection = CPoolDatabase.getConnection();
        		
				CWorkflowLine workflowLine = COperacionesDatabaseLicencias.getWorkflowLine(connection, expedienteLicencia.getEstado().getIdEstado(),
                        new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).intValue());

				if (connection!=null){
					connection.close();
					CPoolDatabase.releaseConexion();   
				}	

				if (connection!=null){
					connection.close();
					CPoolDatabase.releaseConexion();   
				}
				
				long plazoDias = workflowLine.getPlazo();
                //Transformamos los días en minutos
				long CADUCIDAD = plazoDias * 24 * 60 * 60 * 1000;
                if (modoDebug)
                   CADUCIDAD = plazoDias * 60 * 1000;
				//Date fechaEfectiva = new Date(expedienteLicencia.getFechaCambioEstado().getTime() + CADUCIDAD);
                Date fechaEfectiva = new Date(expedienteLicencia.getFechaCambioEstado()!=null?expedienteLicencia.getFechaCambioEstado().getTime() + CADUCIDAD:expedienteLicencia.getFechaApertura().getTime() + CADUCIDAD);
				logger.info("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". fechaEfectiva: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEfectiva));
				Date fechaActual = new Date();
				if (fechaEfectiva.before(fechaActual))
                {
                	String fechaCambioEstadoFormatted = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(expedienteLicencia.getFechaCambioEstado()!=null?expedienteLicencia.getFechaCambioEstado():expedienteLicencia.getFechaApertura());
					logger.info("fechaCambioEstadoFormatted: " + fechaCambioEstadoFormatted);
					String fechaExpiracionFormatted = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEfectiva);
					logger.info("fechaExpiracionFormatted: " + fechaExpiracionFormatted);
                	logger.info("Plazo expirado triggered. expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". expedienteLicencia.getFechaApertura(): " + expedienteLicencia.getFechaApertura());
					procesaPlazoExpiracion(expedienteLicencia, workflowLine.getIdPlazoEstado());
				}
			}
			return true;
		} catch (Exception ex) {
			logger.error("Exception al chequear los plazos de expiracion: " + ex);
			return false;
		}
	}
    // ASO añade
    // Normas para el silencio administrativo.
    //El silencio administrativo va a depender del tipo de licencia que se este ejecutando
    //cuando el estado sea igual al estado de resolución
    //Licencias de obra:
    //          - El plazo será si tienen fecha de fin hasta la fecha de fin
    //          - Si no el plazo será el reglamentario
    //Licencias de actividad:
    //          - No caduca

	public static boolean checkSilenciosAdministrativos(Vector tiposLicencia)
    {
    	try {
		    //Vector expedientes = COperacionesDatabaseLicencias.getSearchedExpedientes(new Hashtable(),null, tiposLicencia);
            Vector expedientes = COperacionesDatabaseLicencias.getSearchedExpedientesSilencioAdmin(new Hashtable(),null, tiposLicencia);
        	logger.debug("expedientes.size(): " + expedientes.size());
        	for (int i = 0; i < expedientes.size(); i++)
            {
            	CExpedienteLicencia expedienteLicencia = (CExpedienteLicencia) expedientes.elementAt(i);
                if ((expedienteLicencia.getBloqueado() != null) && (expedienteLicencia.getBloqueado().equalsIgnoreCase("1"))){
                    continue;
                }
            	if (expedienteLicencia.getEstado().getIdEstado() ==CConstantesComando.ESTADO_DURMIENTE)
					continue;
                if (expedienteLicencia.getSilencioEvent() == null || expedienteLicencia.getPlazoEvent()!=null)
                {
                    logger.info("Es una licencia con plazo o no se ha realizado el evento: "+expedienteLicencia.getNumExpediente());
                    continue;
                }

                Date fechaEfectiva = getFechaEfectivaSilencioAdministrativo(expedienteLicencia,0);
                if (fechaEfectiva==null) continue;
				logger.info("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". fechaEfectiva: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEfectiva));
				Date fechaActual = new Date();
                if (fechaEfectiva.before(fechaActual))
                {
					String fechaExpiracionFormatted = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEfectiva);
					logger.info("fechaExpiracionFormatted: " + fechaExpiracionFormatted);
					logger.info("Silencio administrativo triggered. expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente() + ". expedienteLicencia.getFechaApertura(): " + expedienteLicencia.getFechaApertura());
					procesaSilencioAdministrativo(expedienteLicencia);
				}
			}
			return true;
		} catch (Exception ex)
        {
        	logger.error("Exception al procesar el silencio administrativi: " + ex);
			return false;
		}
	}
    public static boolean getModoDebug()
    {
        return modoDebug;
    }

}
