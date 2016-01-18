package es.satec.localgismobile.server.projectsync.actions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.satec.localgismobile.server.Global;

/**
 * Servlet con operaciones sobre el servidor de LocalGIS Mobile
 * @author irodriguez
 *
 */
public class UploadProjectAction extends Action {
	
	private static final String ZIP_REFER_HEADER = "zipReference";

	private static final String FILE_TYPE_HEADER = "fileType";

	private static final String PROP_TYPE = "PROPERTIES";

	private static final String ZIP_TYPE = "ZIP";

	private static Logger log = Logger.getLogger(UploadProjectAction.class);
	
	private ServletOutputStream out; //escritura por HTML
	
	public UploadProjectAction(){
		
	}

	/* 
	 * 
	 * ************************** ACCIONES DEL SERVLET ************************** 
	 * 
	 */
	/**
	 * Realiza la subida de un fichero por HTTP
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//url de acceso => http://localhost:8082/localgismobile/UploadProject.do
		
		log.info("LocalGISMobileManagerAction - uploadProject");
        out = initWriter(response);
        
		//creación de la salida devuelta
        response.setContentType("text/html");
       
		//Chequeamos si se trata de un archivo multipart
//		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//		if(!isMultipart){
//			printLogAndHTML("El fichero no tiene contenido multipart, no será procesado.");
//			closeWriter();
//			return;
//		}
		
		//Creamos una factoria para los ficheros
		FileItemFactory factory = new DiskFileItemFactory();

		//Creamos un manejador del fichero de subida
		ServletFileUpload upload = new ServletFileUpload(factory);

		//Obtenemos el tipo de fichero subido (zip o properties)
		String fileType = request.getHeader(FILE_TYPE_HEADER);
		if(fileType==null || !(fileType.equals(ZIP_TYPE) || fileType.equals(PROP_TYPE))){
			printLogAndHTML("El fichero "+fileType+" no es del tipo deseado (ZIP o PROPERTIES).");
			closeWriter();
			return null;
		}
		String zipReference = request.getHeader(ZIP_REFER_HEADER);
		if(fileType.equals(PROP_TYPE) && zipReference==null){
			printLogAndHTML("El fichero de properties con la asignacion de usuarios " +
					"debe tener un encabezado que indique el ZIP al que hace referencia.");
			closeWriter();
			return null;
		}
		
		//Parseamos la petición
		try {
			List<FileItem> items = upload.parseRequest(request);
			
			//Procesamos los ficheros subidos
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();
			    processUploadedFile(item, fileType, zipReference);
			}
			
		} catch (FileUploadException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			errorLogAndHTML("Error parseando la peticion de subida del fichero. " + e);
		} finally {
			closeWriter();
		}	
		
		return null;
	}

	/* 
	 * 
	 * ************************** MÉTODOS AUXILIARES **************************
	 * 
	 */
	/**
	 * Procesa cada uno de los ficheros subidos
	 * @param item
	 * @param zipReference 
	 * @param fileType 
	 * @param request 
	 * @param out 
	 */
	private void processUploadedFile(FileItem item, String fileType, String zipReference) {
		//subimos el fichero
		String fileName = "";
		try {
			fileName = URLEncoder.encode(item.getName(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			errorLogAndHTML("No se ha podido codificar el nombre del fichero " + fileName);
			return;
		}
		printLogAndHTML("Procesando fichero " + fileName);
		String fullPathFile = Global.UPLOAD_PATH + fileName;
		
		//si es el de properties lo borraremos tras zipearlo, de ahi que alteremos su nombre para que no haya colisiones
		if(fileType.equals(PROP_TYPE)){
			fullPathFile += System.currentTimeMillis(); 
		}
		
		//escritura del fichero en el servidor
		File uploadedFile = new File(fullPathFile);
		try {
			item.write(uploadedFile);
		} catch (Exception e) {
			errorLogAndHTML("No se ha podido escribir el archivo en el servidor: " + fullPathFile);
			return;
		}
		printLogAndHTML("Se ha subido correctamente el fichero a " + fullPathFile);
		
		//si es de properties tenemos que meterlo en el ZIP correspondiente
		if(fileType.equals(PROP_TYPE)){
			if(zipReference==null){return;}
			try {
				zipReference = URLEncoder.encode(zipReference, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				errorLogAndHTML("No se ha podido codificar el nombre del fichero " + zipReference);
				return;
			}
			String fullPathZip = Global.UPLOAD_PATH + zipReference;
			File fZipRefer = new File(fullPathZip);
			if(!fZipRefer.exists()){
				errorLogAndHTML("No se ha encontrado el siguiente zip en el servidor: " + fullPathZip);
				return;
			}
			addToZipFile(fZipRefer, uploadedFile, fileName);
			uploadedFile.delete();
		}
	}	
	
	
	/**
	 * Añade el fichero properties al fichero zip correspondiente. 
	 * Utilizamos esta librería por el problema con los ficheros cuyo nombre contiene acentos
	 * 
	 * @param zipFile
	 * @param propFile
	 */
	private void addToZipFile(File zipFile, File propFile, String propEntry) {
		try {
			de.schlichtherle.io.File zipFileTrue = new de.schlichtherle.io.File(zipFile + File.separator + propEntry);
			zipFileTrue.archiveCopyFrom(propFile);
			de.schlichtherle.io.File.umount();
		} catch (de.schlichtherle.io.ArchiveException e) {
			errorLogAndHTML("No se ha podido añadir el properties al ZIP. " + e);
		}
	}

	/**
	 * Crea una página web de test para comprobar el funcionamiento del upload
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 */
	public void testUpload(ActionMapping map, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html");
	    PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<TITLE></TITLE>");
			out.println("</HEAD>");
			out.println("<BODY>");
			out.println("<center>");
			out.println("<form method=\"POST\" enctype='multipart/form-data' action=\"/localgismobile/SVGManager.do?method=upload\">");
			out.println("Por favor, seleccione el trayecto del fichero a cargar");
			out.println("<br><input type=\"file\" name=\"fichero\">");
			out.println("<input type=\"submit\" value=\"Upload\">");
			out.println("</form>");
			out.println("</center>");
			out.println("</BODY>");
			out.println("</HTML>");
		} catch (IOException e) {
			log.error(e,e);
		} finally {
			try {out.close();}catch (Exception e) {}
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
