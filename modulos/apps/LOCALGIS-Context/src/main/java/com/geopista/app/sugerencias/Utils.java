/**
 * Utils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.sugerencias;

import java.awt.Frame;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;



public class Utils {
	
	public static byte[] readResource(String name) throws IOException {
		InputStream is = Utils.class.getClassLoader().getResourceAsStream(name);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		int read = -1;
		while ((read = is.read(buffer)) != -1) {
			baos.write(buffer, 0, read);
		}
		return baos.toByteArray();
	}
	
	public static byte[] readFile(File file) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int read = -1;
		while ((read = fis.read(buffer)) != -1) {
			baos.write(buffer, 0, read);
		}
		return baos.toByteArray();
	}
	
	public static String getMimeType(File file) {
		return new MimetypesFileTypeMap().getContentType(file);
	}
	
	public static String getMimeType(String string) {
		return new MimetypesFileTypeMap().getContentType(string);
	}
	
	/**
	 * El usuario debe seleccionar un proyecto
	 * @throws Exception 
	 */
	public static String seleccionarProyecto(Frame f, Sugerencia s) throws Exception{
		List<Proyecto> listProyectos=null;
		ProyectosDialog panelProyecto = null;
		String proyecto = null;
		try{
			    listProyectos=MantisConnectModeloClient.getProyectos(s.getUsuario(),s.getPassword());
				panelProyecto = new ProyectosDialog(f,listProyectos);	
				if (listProyectos!=null && listProyectos.size()>1){
					 panelProyecto.setVisible(true);					 
					 if (!panelProyecto.isCanceled())
						 proyecto=panelProyecto.getProyecto();
				}
				else if (listProyectos==null) return null;
				else proyecto=listProyectos.get(0).getId()+"-"+listProyectos.get(0).getNombre();
		        s.setProyecto(proyecto);
		        
		}catch(Exception e){
			//JOptionPane.showMessageDialog(f,e.getMessage());
			throw e;
		}
		return proyecto;
	}   	
	
}