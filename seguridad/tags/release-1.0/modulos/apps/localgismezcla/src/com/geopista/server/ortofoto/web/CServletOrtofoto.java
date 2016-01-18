package com.geopista.server.ortofoto.web;
/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

import admcarApp.PasarelaAdmcar;

import com.geopista.app.AppContext;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.ortofoto.ImportadorOrtofotos;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CEnvioOperacion;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.ortofoto.CSolicitudImportacionOrtofoto;
import com.geopista.util.ApplicationContext;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.mortbay.jaas.JAASUserPrincipal;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.*;
import java.net.URLDecoder;


public class CServletOrtofoto extends LoggerHttpServlet {

	private static final long serialVersionUID = 1L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CServletOrtofoto.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		logger.debug("Inicio CServletOrtofoto.doGet...");
		doPost(request, response);
	}

	private String buildResponse(CResultadoOperacion resultadoOperacion) {
		
		try {
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
		logger.debug("Inicio CServletOrtofoto.doPost...");
		PrintWriter out = response.getWriter();

		try {
			
            CResultadoOperacion resultadoOperacion = new CResultadoOperacion();

			JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
			Sesion userSession = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

			Principal userPrincipal = userSession.getUserPrincipal();
        	Enumeration userPerms = userSession.getRoleGroup().members();

        	String idMunicipio = null;//(String)request.getParameter("idMunicipio");
        	String idEntidad= null;//(String)request.getParameter("idMunicipio");
//        	
//            /* MultipartPostMethod */
//            String stream=request.getParameter("mensajeXML");
//            
//            System.out.println("CServletOrtofotos.doPost mensajeXML="+stream);
        	
        	/* MultipartPostMethod */
            String stream=null;
            /** Recogemos los nuevos ficheros annadidos */
            Hashtable fileUploads=new Hashtable();
            // Create a new file upload handler
            DiskFileUpload upload = new DiskFileUpload();

            /** Set upload parameters */
            upload.setSizeThreshold(CConstantesComando.MaxMemorySize);

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
                          System.out.println("CServletOrtofoto.doPost mensajeXML="+item.getString("ISO-8859-1"));
                       } else {
	                       if (fieldName.equalsIgnoreCase("idMunicipio")){
	                           idMunicipio = item.getString("ISO-8859-1");
	                        }
	                       else{
	                    	   if (fieldName.equalsIgnoreCase("idEntidad")){
		                           idEntidad = item.getString("ISO-8859-1");
		                        } 
	                       }
                       }
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
			
			ApplicationContext aplicacion = AppContext.getApplicationContext();
			StringReader sw = new StringReader(stream);
            logger.info("sw="+sw.toString());

			CEnvioOperacion envioOperacion = (com.geopista.protocol.CEnvioOperacion) Unmarshaller.unmarshal(com.geopista.protocol.CEnvioOperacion.class, sw);
			logger.debug("envioOperacion.getComando(): " + envioOperacion.getComando());

			CSolicitudImportacionOrtofoto solicitudImportacionOrtofoto;

			if (envioOperacion.getComando() == CConstantesComando.CMD_ORTHOPHOTO_IMPORT) {

				solicitudImportacionOrtofoto = envioOperacion.getSolicitudImportacionOrtofoto();
				String imageName = solicitudImportacionOrtofoto.getImageName();
				boolean worlfileAttached = solicitudImportacionOrtofoto.isWorldfileAttached();
				String worldfileName = solicitudImportacionOrtofoto.getWorldfileName();
                String epsg = solicitudImportacionOrtofoto.getEpsg();
                String extension = solicitudImportacionOrtofoto.getExtension();
				
				try {
					ImportadorOrtofotos.importar(imageName, worlfileAttached, worldfileName, epsg, idEntidad, extension);
					resultadoOperacion.setDescripcion(aplicacion
                            .getI18nString("importador.importacion.exito"));
					resultadoOperacion.setResultado(true);
				} catch (Exception e) {
					logger.error("Exception: " + e.getMessage());
					resultadoOperacion.setDescripcion(aplicacion
                            .getI18nString("importador.importacion.error"));
					resultadoOperacion.setResultado(false);
				}	
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
}
