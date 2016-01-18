/**
 * TemplatesServlet.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.server.resources;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.server.administradorCartografia.ACException;


/**
 * Esta clase es la primera que se llama al entrar en una aplicación
 * su función consiste en crear la sesión del usuario dentro del sistema
 * Para ello regoge del user principal el nombre del usuario y obtiene
 * los datos de este de la base de datos.
 * También obtiene el listado de entidades y municipios.
 * 
 * @author angeles
 *
 */
public class TemplatesServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
  
	/**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TemplatesServlet.class);
    
    @Override
    public void doGet (HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	doPost(request, response);
    }
    
    @Override
    public void doPost (HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	try{
	    	byte [] zip = ZIPCreatorUtils.createZip(ZIPConstants.PLANTILLAS_ZIPNAME,
	    				new File(this.getClass().getResource(ZIPConstants.PLANTILLAS_DIR).getPath()),false);
	    	
	    	//OutputStream outputStream = new FileOutputStream ("/tmp/plantillas.zip"); 
	    	//outputStream.write(zip);
	    	
	        ServletOutputStream sos = response.getOutputStream();
	        response.setContentType("application/zip");      
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + ZIPConstants.PLANTILLAS_ZIPNAME + "\"");
	        sos.write(zip);
	        sos.flush();
	    }
	    catch(Exception ex){
	    	logger.error(ex);
	    	System.out.println(ex);
	    	response.setHeader("Transfer-encoding","chunked");
	        response.setHeader("Content-Encoding", "zip");
	        ObjectOutputStream oos = new ObjectOutputStream(new ZipOutputStream(response.getOutputStream()));
	    	try{oos.writeObject(new ACException(ex));}catch(Exception ex2){};
	    }
    }

    /* ------------------------------------------------ */
    /** Create user for test
     * @exception javax.servlet.ServletException
     */
    public void init ()
        throws ServletException
    {

    }

    /* ------------------------------------------------ */
    /** Destroy servlet, drop tables.
     */
    public void destroy ()
    {
    }

}
