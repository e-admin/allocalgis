/**
 * CServletLicenciasActividad.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.licencias.web;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CEnvioOperacion;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.licencias.Alegacion;
import com.geopista.protocol.licencias.CAnexo;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.Informe;
import com.geopista.protocol.licencias.Mejora;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesDatabaseLicencias;


public class CServletLicenciasActividad extends LoggerHttpServlet {
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CServletLicenciasActividad.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.debug("Inicio.");
		doPost(request, response);
	}

	private String buildResponse(CResultadoOperacion resultadoOperacion) {
		try {
			/*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(resultadoOperacion, sw);
			 */
			StringWriter sw = new StringWriter();
			Marshaller marshaller = new Marshaller(sw);
			marshaller.setEncoding("ISO-8859-1");
			marshaller.marshal(resultadoOperacion);

			String response = sw.toString();
			logger.debug("response: " + response);

			return response;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return "";
		}

	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		super.doPost(request);
		logger.debug("Inicio.");

		PrintWriter out = response.getWriter();

		try {

			CResultadoOperacion resultadoOperacion;

			JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
			Sesion userSession = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

			Principal userPrincipal = userSession.getUserPrincipal();
			Enumeration userPerms = userSession.getRoleGroup().members();

			/* - antes de MultipartPostMethod -
        	String stream = request.getParameter("mensajeXML");
            logger.info("stream="+stream);
			 */

			/* MultipartPostMethod */
			String stream=null;
			/** Recogemos los nuevos ficheros annadidos */
			Hashtable fileUploads=new Hashtable();
			// Create a new file upload handler
			DiskFileUpload upload = new DiskFileUpload();

			/** Set upload parameters */
			upload.setSizeThreshold(CConstantesComando.MaxMemorySize);
			upload.setSizeMax(CConstantesComando.MaxRequestSize);
			/*
            String yourTempDirectory= "anexos" + File.separator;
            upload.setRepositoryPath(yourTempDirectory);
			 */

			// Parse the request
			try{
				//               request.setCharacterEncoding("ISO-8859-1");
				List items = upload.parseRequest(request);

				// Process the uploaded items
				Iterator iter = items.iterator();
				String sMunicipio = "";
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();

					String fieldName = item.getFieldName();
					if (item.isFormField()) {
						if (fieldName.equalsIgnoreCase("mensajeXML")){
							stream  = item.getString("ISO-8859-1");
							logger.info("MENSAJE XML:"+item.getString("ISO-8859-1"));
							System.out.println("CServletLicencias.doPost mensajeXML="+item.getString("ISO-8859-1"));
						}
						else if (fieldName.equalsIgnoreCase(com.geopista.protocol.net.EnviarSeguro.idMunicipio)){
							sMunicipio = item.getString("ISO-8859-1");
							userSession.setIdMunicipio(sMunicipio);
						}
					} else {
						CAnexo anexo= new CAnexo();
						/** Debido a que el nombre del fichero puede contener acentos. */
						fieldName=URLDecoder.decode(fieldName,"ISO-8859-1");
						anexo.setFileName(fieldName);
						anexo.setContent(item.get());
						fileUploads.put(fieldName,anexo);
					}
				}

			}catch (FileUploadBase.SizeLimitExceededException ex){
				String respuesta = buildResponse(new CResultadoOperacion(false, "FileUploadBase.SizeLimitExceededException"));
				out.print(respuesta);
				out.flush();
				out.close();
				logger.warn("************************* FileUploadBase.SizeLimitExceededException");
				return;

			}

			//**********************************************************
			//** Chequeamos
			//******************************************************
			if ((stream == null) || (stream.trim().equals(""))) {
				String respuesta = buildResponse(new CResultadoOperacion(false, "stream es null"));
				out.print(respuesta);
				out.flush();
				out.close();
				logger.warn("stream null or empty. stream: " + stream);
				return;
			}

			StringReader sw = new StringReader(stream);
			logger.info("sw="+sw.toString());

			CEnvioOperacion envioOperacion = (com.geopista.protocol.CEnvioOperacion) Unmarshaller.unmarshal(com.geopista.protocol.CEnvioOperacion.class, sw);
			logger.debug("envioOperacion.getComando(): " + envioOperacion.getComando());

			//CResultadoOperacion resultadoOperacion;
			String numExpediente;
			CSolicitudLicencia solicitudLicencia;
			CExpedienteLicencia expedienteLicencia;
			Hashtable hash;
			Vector vector = new Vector();

			switch (envioOperacion.getComando()) {

			case CConstantesComando.CMD_LICENCIAS_CREAR_EXPEDIENTE:

				solicitudLicencia = envioOperacion.getSolicitudLicencia();
				expedienteLicencia = envioOperacion.getExpedienteLicencia();

				/** inicio */
				/*
				 * Cargamos en una hash los nombres de los anexos de la solicitud (marcados como annadidos, borrados, sin marcar).
				 * El contenido de los marcados como annadidos, so se encuentra en el xml, sino en la hash fileUploads.
				 */

				/* ANEXOS de solicitud. En la creacion no hay mejora de datos ni alegacion.
				 * No tienen contenido.
				 * Nos interesara recoger el estado (annadido, borrado) y el tipo de anexo.
				 */
				/* SOLICITUD **/
				solicitudLicencia.setAnexos(actualizarAnexosUploaded(solicitudLicencia.getAnexos(), fileUploads));

				resultadoOperacion = COperacionesDatabaseLicencias.crearExpedienteLicencias(solicitudLicencia, expedienteLicencia,userPrincipal,userSession.getIdMunicipio());
				break;

			case CConstantesComando.CMD_LICENCIAS_MODIFICAR_EXPEDIENTE:

				solicitudLicencia = envioOperacion.getSolicitudLicencia();
				expedienteLicencia = envioOperacion.getExpedienteLicencia();

				/** inicio */
				/*
				 * Cargamos en una hash los nombres de los anexos de la solicitud (marcados como annadidos, borrados, sin marcar).
				 * El contenido de los marcados como annadidos, so se encuentra en el xml, sino en la hash fileUploads.
				 */

				/*
				 * No tienen contenido.
				 * Nos interesara recoger el estado (annadido, borrado) y el tipo de anexo.
				 */
				/** ANEXOS SOLICITUD */
				solicitudLicencia.setAnexos(actualizarAnexosUploaded(solicitudLicencia.getAnexos(), fileUploads));

				/* ANEXOS MEJORA DE DATOS */
				Vector vMejoras= solicitudLicencia.getMejoras();
				Vector vM= new Vector();
				if (vMejoras != null){
					for (Enumeration e= vMejoras.elements(); e.hasMoreElements();){
						Mejora mejora= (Mejora)e.nextElement();
						mejora.setAnexos(actualizarAnexosUploaded(mejora.getAnexos(), fileUploads));
						vM.add(mejora);
					}
					solicitudLicencia.setMejoras(vM);
				}

				/* ANEXOS ALEGACION */
				Alegacion alegacion= expedienteLicencia.getAlegacion();
				if (alegacion != null){
					alegacion.setAnexos(actualizarAnexosUploaded(alegacion.getAnexos(), fileUploads));
					expedienteLicencia.setAlegacion(alegacion);
				}

				resultadoOperacion = COperacionesDatabaseLicencias.modificarExpedienteLicencias(solicitudLicencia, expedienteLicencia,userPrincipal, userSession.getIdMunicipio());
				break;


			case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_LICENCIA:
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getTiposLicencia());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_OBRA:
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getTiposObra());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_ANEXO:
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getTiposAnexo());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_DISPONIBLES:
				resultadoOperacion = new CResultadoOperacion(true, "");
				expedienteLicencia = (CExpedienteLicencia) envioOperacion.getParametro();
				int idTipoLicencia= new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).intValue();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getEstadosDisponibles(expedienteLicencia, idTipoLicencia));
				break;


			case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_PERMITIDOS:
				resultadoOperacion = new CResultadoOperacion(true, "");
				expedienteLicencia = (CExpedienteLicencia) envioOperacion.getParametro();
				idTipoLicencia= new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).intValue();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getEstadosPermitidos(expedienteLicencia,userPerms,idTipoLicencia));
				break;


			case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS:
				resultadoOperacion = new CResultadoOperacion(true, "");
				Vector tiposLicencia= (Vector)envioOperacion.getTiposLicencia();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getEstados(tiposLicencia));
				break;


			case CConstantesComando.CMD_LICENCIAS_GET_VIAS_NOTIFICACION:
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getViasNotificacion());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_SOLICITUD_LICENCIA:
				resultadoOperacion = new CResultadoOperacion(true, "");
				numExpediente = (String) envioOperacion.getParametro();
				String locale= (String)envioOperacion.getParametro2();
				tiposLicencia= (Vector)envioOperacion.getTiposLicencia();
				resultadoOperacion = COperacionesDatabaseLicencias.getExpedienteLicencia(numExpediente, userSession.getIdMunicipio(), locale, tiposLicencia);
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_EXPEDIENTES:
				resultadoOperacion = new CResultadoOperacion(true, "");
				hash = envioOperacion.getHashtable();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getSearchedExpedientes(hash, userPerms, userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia()));
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_EXPEDIENTES_PLANOS:
				resultadoOperacion = new CResultadoOperacion(true, "");
				hash = envioOperacion.getHashtable();
				resultadoOperacion.setExpedientes(COperacionesDatabaseLicencias.getSearchedExpedientesPlanos(hash, userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia()));
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getSearchedReferenciasPlanos(hash, userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia()));

				break;
			case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_REFERENCIAS_CATASTRALES:
				resultadoOperacion = new CResultadoOperacion(true, "");
				hash = envioOperacion.getHashtable();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getSearchedReferenciasCatastrales(hash,userSession.getIdMunicipio()));
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_ADDRESSES:
				resultadoOperacion = new CResultadoOperacion(true, "");
				hash = envioOperacion.getHashtable();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getSearchedAddresses(hash,userSession.getIdMunicipio()));
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_ADDRESSES_BY_NUMPOLICIA:
				resultadoOperacion = new CResultadoOperacion(true, "");
				hash = envioOperacion.getHashtable();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getSearchedAddressesByNumPolicia(hash,userSession.getIdMunicipio()));
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_PERSONAS:
				resultadoOperacion = new CResultadoOperacion(true, "");
				hash = envioOperacion.getHashtable();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getSearchedPersonas(hash, userSession.getIdMunicipio()));
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_FINALIZACION:
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getTiposFinalizacion());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_TRAMITACION:
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getTiposTramitacion());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_NOTIFICACION:
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getTiposNotificacion());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_NOTIFICACION:
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getEstadosNotificacion());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_RESOLUCION:
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getEstadosResolucion());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_NOTIFICACIONES:
				resultadoOperacion = new CResultadoOperacion(true, "");
				hash = envioOperacion.getHashtable();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getNotificaciones(hash));
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_PARCELARIO:
				resultadoOperacion = new CResultadoOperacion(true, "");
				hash = envioOperacion.getHashtable();
				resultadoOperacion.setVector(COperacionesDatabaseLicencias.getParcelario(hash, userSession.getIdMunicipio()));
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_SOLICITUDES_EXPEDIENTES_INFORME:
				hash = envioOperacion.getHashtable();
				resultadoOperacion = COperacionesDatabaseLicencias.getSolicitudesExpedientesInforme(hash,userSession.getIdMunicipio(), envioOperacion.getTiposLicencia());
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_PLANTILLAS:
				String aplicacion= (String)envioOperacion.getParametro();
				vector = COperacionesDatabaseLicencias.getPlantillas(aplicacion);
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(vector);
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_NOTIFICACIONES_MENU:
				hash = envioOperacion.getHashtable();
				vector = COperacionesDatabaseLicencias.getNotificacionesMenu(hash, userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia());
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(vector);
				break;

			case CConstantesComando.CMD_LICENCIAS_GET_EVENTOS:
				hash = envioOperacion.getHashtable();
				locale= (String)envioOperacion.getParametro();
				resultadoOperacion = COperacionesDatabaseLicencias.getEventos(hash, userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia(), locale);
				break;
			case CConstantesComando.CMD_LICENCIAS_GET_ULTIMOS_EVENTOS:
				hash = envioOperacion.getHashtable();
				locale= (String)envioOperacion.getParametro();
				resultadoOperacion = COperacionesDatabaseLicencias.getUltimosEventos(hash,userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia(), locale);
				break;
			case CConstantesComando.CMD_LICENCIAS_GET_EVENTOS_SIN_REVISAR:
				hash = envioOperacion.getHashtable();
				locale= (String)envioOperacion.getParametro();
				resultadoOperacion = COperacionesDatabaseLicencias.getEventosSinRevisar(hash, userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia(), locale);
				break;
			case CConstantesComando.CMD_LICENCIAS_GET_HISTORICO:
				hash = envioOperacion.getHashtable();
				locale= (String)envioOperacion.getParametro();
				resultadoOperacion = COperacionesDatabaseLicencias.getHistorico(hash, userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia(), locale);
				break;
			case CConstantesComando.CMD_LICENCIAS_GET_HISTORICO_EXPEDIENTE:
				hash = envioOperacion.getHashtable();
				locale= (String)envioOperacion.getParametro();
				resultadoOperacion = COperacionesDatabaseLicencias.getHistorico(hash, userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia(), locale);
				break;
			case CConstantesComando.CMD_LICENCIAS_GET_NOTIFICACIONES_PENDIENTES:
				hash = envioOperacion.getHashtable();
				vector = COperacionesDatabaseLicencias.getNotificacionesPendientes(userSession.getIdMunicipio(),
						envioOperacion.getTiposLicencia());
				resultadoOperacion = new CResultadoOperacion(true, "");
				resultadoOperacion.setVector(vector);
				break;
			case CConstantesComando.CMD_LICENCIAS_GET_EVENTOS_PENDIENTES:
				resultadoOperacion = COperacionesDatabaseLicencias.getEventosPendientes(userSession.getIdMunicipio(), userPerms,
						envioOperacion.getTiposLicencia(), (String)envioOperacion.getParametro());
				break;
			case CConstantesComando.CMD_LICENCIAS_BLOQUEAR_EXPEDIENTE:
				boolean bloquear= ((Boolean)envioOperacion.getParametro()).booleanValue();
				numExpediente= envioOperacion.getExpedienteLicencia().getNumExpediente();
				resultadoOperacion= COperacionesDatabaseLicencias.bloquearExpediente(numExpediente, bloquear);
				break;
			case CConstantesComando.CMD_INSERTAR_INFORME:
				resultadoOperacion = COperacionesDatabaseLicencias.insertarInforme((Informe)envioOperacion.getParametro(),fileUploads);
				break;
			case CConstantesComando.CMD_LICENCIAS_ACTUALIZAR_IDSIGEM:

				expedienteLicencia = envioOperacion.getExpedienteLicencia();                    
				resultadoOperacion = COperacionesDatabaseLicencias.actualizarIdSigem(expedienteLicencia);
				break;
			case CConstantesComando.CMD_LICENCIAS_OBTENER_IDSIGEM:

				expedienteLicencia = envioOperacion.getExpedienteLicencia();                    
				resultadoOperacion = COperacionesDatabaseLicencias.obtenerIdSigem(expedienteLicencia);
				break;
			case CConstantesComando.CMD_LICENCIAS_ACTUALIZAR_ESTADOSIGEM:

				expedienteLicencia = envioOperacion.getExpedienteLicencia();                    
				resultadoOperacion = COperacionesDatabaseLicencias.checkExpedientePublicado(expedienteLicencia, userSession.getIdMunicipio());
				break;
			case CConstantesComando.CMD_LICENCIAS_PUBLICAR_EXPEDEINTE_SIGEM:

				expedienteLicencia = envioOperacion.getExpedienteLicencia();
				solicitudLicencia = envioOperacion.getSolicitudLicencia();
				resultadoOperacion = COperacionesDatabaseLicencias.publicarExpedienteSigem(expedienteLicencia, solicitudLicencia, userSession.getIdMunicipio());
				break;

			default:
				logger.warn("Comand not found. envioOperacion.getComando(): " + envioOperacion.getComando());
				resultadoOperacion = new CResultadoOperacion(false, "Command not found");
			}

			String respuesta = buildResponse(resultadoOperacion);
			out.print(respuesta);
			out.flush();
			out.close();

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}

	}

	public Vector actualizarAnexosUploaded(Vector anexos, Hashtable fileUploaded){
		/*
		 * Cargamos en una hash los nombres de los anexos de la solicitud (marcados como annadidos, borrados, sin marcar).
		 * El contenido de los marcados como annadidos, no se encuentra en el xml, sino en la hash fileUploads.
		 */

		/*
		 * No tienen contenido.
		 * Nos interesara recoger el estado (annadido, borrado) y el tipo de anexo.
		 */

		if (anexos != null){
			Vector v= new Vector();

			for (Enumeration e= anexos.elements(); e.hasMoreElements();){
				CAnexo anexo= (CAnexo)e.nextElement();
				if (anexo.getEstado() == CConstantesComando.CMD_ANEXO_ADDED){
					String filename= anexo.getFileName();
					CAnexo uploaded= (CAnexo)fileUploaded.get(filename);
					if (uploaded != null){
						anexo.setContent(uploaded.getContent());
					}
				}
				v.add(anexo);
			}

			return v;
		}else{
			return null;
		}

	}


}
