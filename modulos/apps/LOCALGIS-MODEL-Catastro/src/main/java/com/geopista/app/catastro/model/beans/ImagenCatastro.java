/**
 * ImagenCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class ImagenCatastro implements Serializable{

	private String nombre = null;
	private String extension = null;
	private String tipoDocumento = null;
	private String foto = null;
		
	public void setFoto(byte[] foto){
		this.foto = new sun.misc.BASE64Encoder().encode(foto);
	}
	
	public Image getImagen() throws Exception{
		
		Image imagen = null;
		
		if (foto != null){
						
	        byte[] bytes = null;
	        try{
	        	
	            bytes = new sun.misc.BASE64Decoder().decodeBuffer(foto);	        	
				imagen = ImageIO.read(new ByteArrayInputStream(bytes));
	            
	        } catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	        
		}
		return imagen;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
}
