package es.satec.localgismobile.server.projectsync.actions;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.server.administradorCartografia.Const;


import es.satec.localgismobile.server.Global;
import es.satec.localgismobile.server.projectsync.beans.ExtractionProjectList;
import es.satec.localgismobile.server.projectsync.connectors.ProyectosExtraccionConnector;
import es.satec.localgismobile.server.projectsync.xml.beans.ServletXMLUpload;
import es.satec.localgismobile.server.web.filters.AuthenticationFilter;

/**
 * Servlet con operaciones sobre el servidor de LocalGIS Mobile
 * @author irodriguez
 *
 */
public class SyncProjectAction extends DispatchAction{
	
	private static final String USER_ID_PARAM = "userId";
	private static final String PROJECT_ID_PARAM = "projectId";

	private static Logger log = Logger.getLogger(SyncProjectAction.class);

	private ServletOutputStream out; //escritura por HTML
	
	public SyncProjectAction(){
		
	}
	
	/**
	 * Devuelve un XML con el listado de proyectos
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 */
	public void listProjects(ActionMapping map, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//url de acceso => http://localhost:8082/localgismobile/sync/SyncProjectAction.do?method=listProjects&userId=203
		
		log.info("LocalGISMobileManagerAction - listProjects");
		out = initWriter(response);
		String userId = request.getParameter(USER_ID_PARAM);
		
		if(userId==null){
			errorLogAndHTML("Error: Falta parametro " + USER_ID_PARAM);
			closeWriter();
			return;
		}
		
		ProyectosExtraccionConnector conectorBBDD = null;
		ExtractionProjectList extractionProjectList = null;
		try {
			conectorBBDD = new ProyectosExtraccionConnector();
			extractionProjectList = conectorBBDD.getExtractionProjectList(Integer.parseInt(userId));
			if(extractionProjectList==null){
				errorLogAndHTML("Error: No se ha encontrado ningun proyecto de extraccion para el usuario " + USER_ID_PARAM);
				closeWriter();
				return;
			}
			response.setContentType("text/xml");
			response.setHeader("Content-Disposition", "attachment; filename=project_list_"+userId+".xml;");
			out.print(extractionProjectList.toXMLFormat());
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			errorLogAndHTML("Error al escribir el XML con la lista de proyectos de extraccion. " + e);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			errorLogAndHTML("Error: el identificador de usuario debe ser numerico. " + e);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			errorLogAndHTML("Error al obtener los proyectos de extraccion del usuario " + userId + ". "+ e);
		} finally {
			closeWriter();
		}	
	}
	
	/**
	 * Descarga un proyecto de extracción
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 */
	public void downloadProject(ActionMapping map, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//url de acceso => http://localhost:8082/localgismobile/sync/SyncProjectAction.do?method=downloadProject&projectId=1241535782624
		
		log.info("LocalGISMobileManagerAction - downloadProject");
		out = initWriter(response);
		String projectId = request.getParameter(PROJECT_ID_PARAM);
		
		if(projectId==null){
			errorLogAndHTML("Error: Falta parametro " + PROJECT_ID_PARAM);
			closeWriter();
			return;
		}
		
		File dir = new File(Global.UPLOAD_PATH);
		File ficheroZip = null;
		FileInputStream fIn = null;
		String fileName = null;
		boolean isProjectFound = false;
		try {
	        if (dir.isDirectory()) {
	            File[] myFiles = dir.listFiles();
	            //buscamos el proyecto de desconexión indicado
	            for (int i = 0; i < myFiles.length && isProjectFound==false; i++) {
	            	ficheroZip = myFiles[i];
	            	fileName = ficheroZip.getName();
	            	//una vez encontrado escribimos en el flujo de salida
	            	if(fileName.endsWith(projectId + ".zip")){
	            		fIn = new FileInputStream(ficheroZip);
		    			response.setContentType("application/zip");
		    			response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\";");
		    			out = response.getOutputStream();
		    			byte[] buffer = new byte[18024];
		    			int len;
		    		    while ((len = fIn.read(buffer)) > 0){
		    		    	out.write(buffer, 0, len);
		    		    }
		    		    isProjectFound = true;
	            	} 
	            }
	            if(isProjectFound==false){
	            	errorLogAndHTML("Error: No se ha encontrado ningun proyecto con id " + projectId);
	    			closeWriter();
	    			return;
	            }
	    		
	    		log.info("Fichero encontrado: " + fileName);
	        }
		}catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			errorLogAndHTML("Error: No se ha podido devolver el zip para el proyecto " + projectId);
		} finally {
			try {out.close();}catch (Exception e) {}
			try {fIn.close();}catch (Exception e) {}
			closeWriter();
		}
	}
	
	/**
	 * Subida de ficheros XML para la reconexión de features
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 */
	public void reconnectProject(ActionMapping map, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//url de acceso => http://localhost:8082/localgismobile/sync/SyncProjectAction.do?method=reconnectProject
		
		log.info("LocalGISMobileManagerAction - reconnectProject");
		out = initWriter(response);
		InputStream is = null;
		InputStream byteInput = null;
		try {
			//obtenemos el documento XML a parsear
			is = request.getInputStream();
			byte[] byteIs = Global.convertStreamToByte(is);
			
			if(log.isDebugEnabled()){				
				log.debug("InputStream recibido: " +  new String(byteIs));
			}
			
			//inputstream para modificar
			byteInput = new ByteArrayInputStream(byteIs);			
			com.geopista.security.SecurityManager sm = (com.geopista.security.SecurityManager)request.getSession().getAttribute(AuthenticationFilter.SM_ATTRIBUTE);

			ServletXMLUpload servletXmlUp = new ServletXMLUpload(sm);			
			String xmlResponse = servletXmlUp.execute(byteInput);
            
            response.setStatus(HttpServletResponse.SC_OK);
            printLogAndHTML(xmlResponse); //respuesta en XML en este caso
            
            log.info("Proyecto correctamente reconectado.");
            
		} catch (Exception e) {
			log.error("Se ha producido un error en la reconexion: ",e);
			response.setStatus(HttpServletResponse.SC_OK);
			errorLogAndHTML(e.getMessage()); //impresión de una traza en XML
		} finally {
			try{is.close();}catch(Exception ex){};
			try{byteInput.close();}catch(Exception ex){};
			closeWriter();
			System.gc(); //llamamos al recolector de basura
		}
	}
	
	/** FUNCIONES AUXILIARES DE ESCRITURA EN LOG Y HTML **/
	/**
	 * Inicializa la salida html
	 * @param response
	 * @return
	 */
	private ServletOutputStream initWriter(HttpServletResponse response) {
		try {
			out = response.getOutputStream();
			//Descomentar
			//response.setCharacterEncoding("UTF-8");
		} catch (IOException e1) {
			log.error(e1,e1);
		}
		return out;   
	}
	
	/**
	 * Imprime una traza en log y en html
	 * @param s
	 */
	private void printLogAndHTML(String s){
		log.debug(s);
		try {
			out.println(s);
		} catch (Exception e) {
			log.error("Output stream no inicializado " + e, e);
		}
	}
	
	/**
	 * Imprime un error en log y en html
	 * @param s
	 */
	private void errorLogAndHTML(String s){
		log.error(s);
		try {
			out.println(s);
		} catch (Exception e) {
			log.error("Output stream no inicializado " + e, e);
		}
	}

	/**
	 * Cierra la salida html
	 * @param out
	 */
	private void closeWriter() {
		try {
	        out.flush();
	        out.close();
		} catch (Exception e1) {}
	}
}

