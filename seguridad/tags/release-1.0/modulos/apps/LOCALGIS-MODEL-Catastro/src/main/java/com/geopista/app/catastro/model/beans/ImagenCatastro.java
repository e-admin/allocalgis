package com.geopista.app.catastro.model.beans;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

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
