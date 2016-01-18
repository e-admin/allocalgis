/**
 * DeleteProjectAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.actions;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
