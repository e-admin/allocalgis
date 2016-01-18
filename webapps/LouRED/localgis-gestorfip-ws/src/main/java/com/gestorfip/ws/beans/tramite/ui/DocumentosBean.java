package com.gestorfip.ws.beans.tramite.ui;

import java.util.Arrays;




public class DocumentosBean {
	
	private int id;
	private Long escala;
	private String archivo;
	private String nombre;
	private String codigo;
	private String comentario;
	private int grupo;
	private int tipo;
	private int tramite;
	private boolean isDirectory;
	private boolean isHoja;
	private DocumentosBean[] lstHojas ;
	private int idLayer = -1;
	private int idFeature = -1;
	
	public int getTramite() {
		return tramite;
	}

	public void setTramite(int tramite) {
		this.tramite = tramite;
	}

	public int getGrupo() {
		return grupo;
	}
	
	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}
	
	public int getTipo() {
		return tipo;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	
	public Long getEscala() {
		return escala;
	}
	
	public void setEscala(Long escala) {
		this.escala = escala;
	}
	
	public String getArchivo() {
		return archivo;
	}
	
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	
	

	public boolean isHoja() {
		return isHoja;
	}

	public void setHoja(boolean isHoja) {
		this.isHoja = isHoja;
	}

	public DocumentosBean[] getLstHojas() {
		return lstHojas;
	}

	public void setLstHojas(DocumentosBean[] lstHojas) {
		this.lstHojas = lstHojas;
	}

	public void addHoja(DocumentosBean hoja) {
		if(lstHojas == null){
			
			lstHojas = new DocumentosBean[1];
			lstHojas[0] = hoja;
		
		}
		else{
			lstHojas = (DocumentosBean[]) Arrays.copyOf(lstHojas, 
					lstHojas.length+1);
		
			lstHojas[lstHojas.length-1] = hoja;
		}
	}

	public int getIdLayer() {
		return idLayer;
	}

	public void setIdLayer(int idLayer) {
		this.idLayer = idLayer;
	}

	public int getIdFeature() {
		return idFeature;
	}

	public void setIdFeature(int idFeature) {
		this.idFeature = idFeature;
	}
	
	
}
