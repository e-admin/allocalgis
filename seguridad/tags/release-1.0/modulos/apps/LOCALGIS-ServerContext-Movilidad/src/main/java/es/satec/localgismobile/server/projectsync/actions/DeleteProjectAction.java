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
public class DeleteProjectAction extends Action {
	
	private static Logger log = Logger.getLogger(DeleteProjectAction.class);
	
	public DeleteProjectAction(){
		
	}


	/**
	 * Realiza el borrado de un directorio dentro del servidor
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//url de acceso => http://localhost:8082/localgismobile/DeleteProject.do
		
		log.info("LocalGISMobileManagerAction - deleteProject");

        
        String projectId=request.getParameter("PROJECT_ID");
        String projectName=request.getParameter("PROJECT_NAME");
		//creación de la salida devuelta
        response.setContentType("text/html");
       	
        projectName=projectName.replaceAll(" ", "+");
        String dirMapName = projectName + "." + projectId+".zip";
        String fullPathFile = Global.UPLOAD_PATH + dirMapName;
        
		
        File dirBaseMake = new File(fullPathFile);
        if (dirBaseMake.exists()){
        	log.info("Borrando Fichero:"+fullPathFile);
        	deleteDir(dirBaseMake);        	
        }
		return null;
	}

	//Borra un directorio completo
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            
            //Borra todos los ficheros del directorio
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        //Ahora que el directorio está vacío, se puede borrar
        return dir.delete();
    }
    
    public static void main(String args[]){
    	
    	String name="Javi1_Mapa+de+Catastro+de+Urbana.4.1268592768390.zip";
    	String path="C:\\Documents and Settings\\fjgarcia\\.LOCALGIS\\uploadFiles\\"+name;
    	 File dirBaseMake = new File(path);
         if (dirBaseMake.exists()){
        	 System.out.println("Existe");
         }
         else
        	 System.out.println("No existe");
    }

}
