package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.Documentable;



public class Lote implements Serializable, Documentable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id_lote;
	private Collection<BienBean> bienes;
	private String nombre_lote;
	private Date fecha_alta;
	private Date fecha_baja;
	private Date fecha_ultima_modificacion;
	//private String seguro;
	private String descripcion;
	private String destino;
	private int numeroBienes=0;
	
	private Seguro seguro;

	private Collection<DocumentBean> documentos;
	public Long getId_lote() {
		return id_lote;
	}
	public void setId_lote(long id_lote) {
		this.id_lote = new Long(id_lote);
	}
	public void setId_lote(Long id_lote) {
		this.id_lote = id_lote;
	}
	public Collection<BienBean> getBienes() {
		return bienes;
	}
	public void setBienes(Collection<BienBean> bienes) {
		this.bienes = bienes;
	}
	public String getNombre_lote() {
		return nombre_lote;
	}
	public void setNombre_lote(String nombre_lote) {
		this.nombre_lote = nombre_lote;
	}
	public Date getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public Date getFecha_baja() {
		return fecha_baja;
	}
	public void setFecha_baja(Date fecha_baja) {
		this.fecha_baja = fecha_baja;
	}
	public Date getFecha_ultima_modificacion() {
		return fecha_ultima_modificacion;
	}
	public void setFecha_ultima_modificacion(Date fecha_ultima_modificacion) {
		this.fecha_ultima_modificacion = fecha_ultima_modificacion;
	}
	/*public String getSeguro() {
		return seguro;
	}
	public void setSeguro(String seguro) {
		this.seguro = seguro;
	}*/
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Collection<DocumentBean> getDocumentos() {
		return documentos;
	}
	public void setDocumentos(Collection<DocumentBean> documentos) {
		this.documentos = documentos;
	}
	public int getNumeroBienes() {
		return numeroBienes;
	}
	public void setNumeroBienes(int numeroBienes) {
		this.numeroBienes = numeroBienes;
	}
	public void addBien(BienBean bien){
		if (bienes==null) bienes=new Vector<BienBean>();
		bienes.add(bien);
		this.numeroBienes=bienes.size();
		
	}
	public Seguro getSeguro() {
		return seguro;
	}
	public void setSeguro(Seguro seguro) {
		this.seguro = seguro;
	}
}
