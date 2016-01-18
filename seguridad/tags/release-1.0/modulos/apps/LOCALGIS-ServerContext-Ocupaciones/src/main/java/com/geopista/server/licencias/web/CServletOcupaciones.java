package com.geopista.server.licencias.web;

import admcarApp.PasarelaAdmcar;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CEnvioOperacion;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.CAnexo;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesDatabaseOcupaciones;
import com.localgis.server.SessionsContextShared;

import org.eclipse.jetty.plus.jaas.JAASPrincipal;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Principal;
import java.util.*;
import java.net.URLDecoder;


public class CServletOcupaciones extends LoggerHttpServlet {
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CServletOcupaciones.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.getWriter().append("Authentication Successful!");    
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

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		super.doPost(request);
		logger.debug("Inicio.");
		PrintWriter out = response.getWriter();

		try {

            CResultadoOperacion resultadoOperacion;

			JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
			PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
			Sesion userSession = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());
 
			Principal userPrincipal = userSession.getUserPrincipal();

			Enumeration userPerms = userSession.getRoleGroup().members();

            /*BUENO - antes de MultipartPostMethod
			String stream = request.getParameter("mensajeXML");
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
//                request.setCharacterEncoding("ISO-8859-1");
                List items = upload.parseRequest(request);
                // Process the uploaded items
                Iterator iter = items.iterator();

                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();

                    String fieldName = item.getFieldName();
                    if (item.isFormField()) {
                       if (fieldName.equalsIgnoreCase("mensajeXML")){
                           stream  = item.getString("ISO-8859-1");
                           logger.info("MENSAJE XML:"+item.getString("ISO-8859-1"));
                           System.out.println("CServletOcupaciones.doPost mensajeXML="+item.getString("ISO-8859-1"));
                       }
                       else if (fieldName.equalsIgnoreCase(com.geopista.protocol.net.EnviarSeguro.idMunicipio)){
                           String sMunicipio = item.getString("ISO-8859-1");
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

            /**/



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
			CEnvioOperacion envioOperacion = (CEnvioOperacion) Unmarshaller.unmarshal(CEnvioOperacion.class, sw);
			logger.debug("envioOperacion.getComando(): " + envioOperacion.getComando());


			//CResultadoOperacion resultadoOperacion;
			String numExpediente;
			CSolicitudLicencia solicitudLicencia;
			CExpedienteLicencia expedienteLicencia;
			Hashtable hash;
			Vector vector = new Vector();
            Vector vAnexosSol= new Vector();

			switch (envioOperacion.getComando()) {

				case CConstantesComando.CMD_LICENCIAS_CREAR_EXPEDIENTE:

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
                    vAnexosSol= solicitudLicencia.getAnexos();
                    if ((vAnexosSol != null) && (vAnexosSol.size() > 0)){
                        solicitudLicencia.setAnexos(actualizarAnexosUploaded(solicitudLicencia, fileUploads));
                    }
                    /**/


					resultadoOperacion = COperacionesDatabaseOcupaciones.crearExpedienteLicencias(solicitudLicencia, expedienteLicencia,userPrincipal,userSession.getIdMunicipio());
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
                    vAnexosSol= solicitudLicencia.getAnexos();
                    if ((vAnexosSol != null) && (vAnexosSol.size() > 0)){
                        solicitudLicencia.setAnexos(actualizarAnexosUploaded(solicitudLicencia, fileUploads));
                    }
                    /**/


					resultadoOperacion = COperacionesDatabaseOcupaciones.modificarExpedienteLicencias(solicitudLicencia, expedienteLicencia,userPrincipal);
					break;


				case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_LICENCIA:
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getTiposLicencia());
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_OBRA:
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getTiposObra());
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_ANEXO:
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getTiposAnexo());
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_DISPONIBLES:
					resultadoOperacion = new CResultadoOperacion(true, "");
					expedienteLicencia = (CExpedienteLicencia) envioOperacion.getParametro();
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getEstadosDisponibles(expedienteLicencia));
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_PERMITIDOS:
					resultadoOperacion = new CResultadoOperacion(true, "");
					expedienteLicencia = (CExpedienteLicencia) envioOperacion.getParametro();
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getEstadosPermitidos(expedienteLicencia,userPerms));
					break;



				case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS:
					resultadoOperacion = new CResultadoOperacion(true, "");

					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getEstados());
					break;


				case CConstantesComando.CMD_LICENCIAS_GET_VIAS_NOTIFICACION:
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getViasNotificacion());
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_SOLICITUD_LICENCIA:
					resultadoOperacion = new CResultadoOperacion(true, "");
					numExpediente = (String) envioOperacion.getParametro();
                    String locale= (String)envioOperacion.getParametro2();
					resultadoOperacion = COperacionesDatabaseOcupaciones.getExpedienteLicencia(numExpediente, userSession.getIdMunicipio(), locale);
					break;


				case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_EXPEDIENTES:
					resultadoOperacion = new CResultadoOperacion(true, "");
					hash = envioOperacion.getHashtable();
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getSearchedExpedientes(hash, userPerms,userSession));
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_REFERENCIAS_CATASTRALES:
					resultadoOperacion = new CResultadoOperacion(true, "");
					hash = envioOperacion.getHashtable();
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getSearchedReferenciasCatastrales(hash));
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_ADDRESSES:
					resultadoOperacion = new CResultadoOperacion(true, "");
					hash = envioOperacion.getHashtable();
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getSearchedAddresses(hash,userSession.getIdMunicipio()));
					break;


				case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_PERSONAS:
					resultadoOperacion = new CResultadoOperacion(true, "");
					hash = envioOperacion.getHashtable();
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getSearchedPersonas(hash, userSession.getIdMunicipio()));
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_FINALIZACION:
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getTiposFinalizacion());
					break;
				case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_TRAMITACION:
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getTiposTramitacion());
					break;
				case CConstantesComando.CMD_LICENCIAS_GET_TIPOS_NOTIFICACION:
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getTiposNotificacion());
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_NOTIFICACION:
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getEstadosNotificacion());
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_RESOLUCION:
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getEstadosResolucion());
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_NOTIFICACIONES:
					resultadoOperacion = new CResultadoOperacion(true, "");
					hash = envioOperacion.getHashtable();
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getNotificaciones(hash));
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_PARCELARIO:
					resultadoOperacion = new CResultadoOperacion(true, "");
					hash = envioOperacion.getHashtable();
					resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getParcelario(hash));
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_SOLICITUDES_EXPEDIENTES_INFORME:
					hash = envioOperacion.getHashtable();
					resultadoOperacion = COperacionesDatabaseOcupaciones.getSolicitudesExpedientesInforme(hash);
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_PLANTILLAS:
					vector = COperacionesDatabaseOcupaciones.getPlantillas();
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(vector);
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_NOTIFICACIONES_MENU:
					hash = envioOperacion.getHashtable();
					vector = COperacionesDatabaseOcupaciones.getNotificacionesMenu(hash);
					resultadoOperacion = new CResultadoOperacion(true, "");
					resultadoOperacion.setVector(vector);
					break;

				case CConstantesComando.CMD_LICENCIAS_GET_EVENTOS:
					hash = envioOperacion.getHashtable();
                    locale= (String)envioOperacion.getParametro();
                    //resultadoOperacion = COperacionesDatabaseOcupaciones.getEventos(hash, locale);
					resultadoOperacion = COperacionesDatabaseOcupaciones.getEventos(hash,userSession.getIdMunicipio(), locale);
					break;
				case CConstantesComando.CMD_LICENCIAS_GET_ULTIMOS_EVENTOS:
					hash = envioOperacion.getHashtable();
                    locale= (String)envioOperacion.getParametro();
                    //resultadoOperacion = COperacionesDatabaseOcupaciones.getUltimosEventos(hash, locale);
					resultadoOperacion = COperacionesDatabaseOcupaciones.getUltimosEventos(hash, userSession.getIdMunicipio(), locale);
					break;
				case CConstantesComando.CMD_LICENCIAS_GET_EVENTOS_SIN_REVISAR:
					hash = envioOperacion.getHashtable();
                    locale= (String)envioOperacion.getParametro();
                    //resultadoOperacion = COperacionesDatabaseOcupaciones.getEventosSinRevisar(hash, locale);
					resultadoOperacion = COperacionesDatabaseOcupaciones.getEventosSinRevisar(hash, userSession.getIdMunicipio(), locale);
					break;
                case CConstantesComando.CMD_LICENCIAS_GET_HISTORICO:
                    hash = envioOperacion.getHashtable();
                    locale= (String)envioOperacion.getParametro();
                    resultadoOperacion = COperacionesDatabaseOcupaciones.getHistorico(hash,userSession.getIdMunicipio(), locale);
                    break;
                case CConstantesComando.CMD_LICENCIAS_GET_HISTORICO_EXPEDIENTE:
                    hash = envioOperacion.getHashtable();
                    locale= (String)envioOperacion.getParametro();
                    resultadoOperacion = COperacionesDatabaseOcupaciones.getHistorico(hash,userSession.getIdMunicipio(), locale);
                    break;
                case CConstantesComando.CMD_LICENCIAS_GET_NOTIFICACIONES_PENDIENTES:
                    hash = envioOperacion.getHashtable();
                    vector = COperacionesDatabaseOcupaciones.getNotificacionesPendientes(userSession.getIdMunicipio());
                    resultadoOperacion = new CResultadoOperacion(true, "");
                    resultadoOperacion.setVector(vector);
                    break;
                case CConstantesComando.CMD_LICENCIAS_GET_EVENTOS_PENDIENTES:
                    resultadoOperacion = COperacionesDatabaseOcupaciones.getEventosPendientes(userSession.getIdMunicipio(), userPerms, (String)envioOperacion.getParametro());
                    break;
                case CConstantesComando.CMD_LICENCIAS_GET_DIRECCION_MAS_CERCANA:
                    String geometria = (String) envioOperacion.getParametro();
	                resultadoOperacion = COperacionesDatabaseOcupaciones.getDireccionMasCercana(geometria, userSession.getIdMunicipio());
                    break;
                case CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_LICENCIAS_OBRA:
                    resultadoOperacion = new CResultadoOperacion(true, "");
                    hash = envioOperacion.getHashtable();
                    resultadoOperacion.setVector(COperacionesDatabaseOcupaciones.getSearchedLicenciasObra(hash, userPerms, userSession.getIdMunicipio()));
                    break;
                case CConstantesComando.CMD_LICENCIAS_BLOQUEAR_EXPEDIENTE:
                    boolean bloquear= ((Boolean)envioOperacion.getParametro()).booleanValue();
                    numExpediente= envioOperacion.getExpedienteLicencia().getNumExpediente();
                    resultadoOperacion= COperacionesDatabaseOcupaciones.bloquearExpediente(numExpediente, bloquear);
                    break;
                case CConstantesComando.CMD_LICENCIAS_DELETE_GEOMETRIA_OCUPACION:
                    String idFeature= (String)envioOperacion.getParametro();
                    resultadoOperacion= COperacionesDatabaseOcupaciones.deleteGeometriaOcupacion(idFeature);
                    break;
                case CConstantesComando.CMD_LICENCIAS_ACTUALIZAR_IDSIGEM:
                	
					expedienteLicencia = envioOperacion.getExpedienteLicencia();                    
                    resultadoOperacion = COperacionesDatabaseOcupaciones.actualizarIdSigem(expedienteLicencia);
                    break;
                case CConstantesComando.CMD_LICENCIAS_OBTENER_IDSIGEM:
                	
					expedienteLicencia = envioOperacion.getExpedienteLicencia();                    
                    resultadoOperacion = COperacionesDatabaseOcupaciones.obtenerIdSigem(expedienteLicencia);
                    break;
                case CConstantesComando.CMD_LICENCIAS_ACTUALIZAR_ESTADOSIGEM:
                	
					expedienteLicencia = envioOperacion.getExpedienteLicencia();                    
                    resultadoOperacion = COperacionesDatabaseOcupaciones.checkExpedientePublicado(expedienteLicencia, userSession.getIdMunicipio());
                    break;
                case CConstantesComando.CMD_LICENCIAS_PUBLICAR_EXPEDEINTE_SIGEM:

                	expedienteLicencia = envioOperacion.getExpedienteLicencia();  
                	solicitudLicencia = envioOperacion.getSolicitudLicencia();
                	resultadoOperacion = COperacionesDatabaseOcupaciones.publicarExpedienteSigem(expedienteLicencia, solicitudLicencia, userSession.getIdMunicipio());
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

    public Vector actualizarAnexosUploaded(CSolicitudLicencia solicitud, Hashtable fileUploaded){
        /*
        * Cargamos en una hash los nombres de los anexos de la solicitud (marcados como annadidos, borrados, sin marcar).
        * El contenido de los marcados como annadidos, so se encuentra en el xml, sino en la hash fileUploads.
        */

        /*
        * No tienen contenido.
        * Nos interesara recoger el estado (annadido, borrado) y el tipo de anexo.
        */
        Vector vAnexos= new Vector();
        Hashtable hAux= new Hashtable();

        Vector vAnexosSol= solicitud.getAnexos();
        if ((vAnexosSol != null) && (vAnexosSol.size() > 0)){
            for (Enumeration e= vAnexosSol.elements(); e.hasMoreElements();){
                CAnexo anexo= (CAnexo)e.nextElement();
                String name= anexo.getFileName();
                hAux.put(name, anexo);
            }

            for (Enumeration e=hAux.elements(); e.hasMoreElements();){
                CAnexo aux= (CAnexo)e.nextElement();
                String filename= aux.getFileName();
                CAnexo uploaded= (CAnexo)fileUploaded.get(filename);
                if (uploaded != null){
                    uploaded.setEstado(aux.getEstado());
                    uploaded.setTipoAnexo(aux.getTipoAnexo());
                    uploaded.setObservacion(aux.getObservacion());
                    vAnexos.add(uploaded);
                }else{
                    vAnexos.add(aux);
                }
            }
            return vAnexos;
        }else{
            return null;
        }
    }
    
}
