package com.localgis.app.gestionciudad.beans;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.localgis.app.gestionciudad.beans.types.DocumentTypes;
/**
 * @author javieraragon
 *
 */
public class Document implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1335350382955947490L;
	
	private String tipo = null;
	private byte[] fichero = null;
	private byte[] thumbnail = null;
	private String extension = "";
	private String nombre = "";
	private int idDocumento;
	
	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Document(){
	}
	
	public Document(String nombreFichero, DocumentTypes tipo, String extension, byte[] fichero){
		this.tipo = tipo.toString();
		this.setNombre(nombreFichero);
		this.fichero = fichero;
		this.extension = extension;
	}
	
	
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(DocumentTypes tipo) {
		this.tipo = tipo.toString();
	}
	/**
	 * @return the fichero
	 */
	public byte[] getFichero() {
		return fichero;
	}
	/**
	 * @param fichero the fichero to set
	 */
	public void setFichero(byte[] fichero) {
		this.fichero = fichero;
	}
	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}
	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	
	
	public String toString() {
		String resultado = "";
		if (this.nombre != null && !this.nombre.equals("")){
			resultado = resultado + "'" + this.nombre + "'";
		} else{
			resultado = resultado + "'Sin Nombre'";
		}
		
		if (this.fichero!=null){
			resultado = resultado + " (" + tamanioDocumentoToString() + ")";
		}
		
		if(this.tipo!=null){
			resultado = resultado + " " + this.tipo;
		}
		
		return resultado;
	}
	
	private String tamanioDocumentoToString(){
		String resultado = "";
		if (this.fichero != null){
			double tamanio  = this.fichero.length;
			DecimalFormat twoDigits = new DecimalFormat("##############.###");
			String unidades = "byte";

			if (this.fichero.length > 1073741824){
				unidades = "GB";
				tamanio = this.fichero.length / 1024 /1024 /1024;
			}else if(this.fichero.length > 1048576){
				unidades = "MB";
				tamanio = this.fichero.length / 1024 /1024;			
			} else if (this.fichero.length > 1024){
				unidades = "KB";
				tamanio = this.fichero.length / 1024;
			} 
			resultado = twoDigits.format(tamanio) + " " + unidades;
		} else{
			resultado = resultado + "NULL";
		}
		
		return resultado;
	}

}
