package com.geopista.ui.dialogs.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExtractionProject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 192261541106400907L;
	private String idProyecto;
	private Date fechaExtraccion;
	private String  nombreProyecto;
	private double posEsquinaX;
	private double posEsquinaY;
	private double anchoCeldas;
	private double altoCeldas;
	private int celdasX;
	private int celdasY;
	private String idMapa;
	private List<Integer> idExtractLayersList;
	private String bloqueMetaInfo;
	private int srid;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public ExtractionProject() {
		super();
	}

	public ExtractionProject(String idProyecto, Date fechaExtraccion,
			String nombreProyecto, double posEsquinaX, double posEsquinaY,
			double anchoCeldas, double altoCeldas, int celdasX, int celdasY,
			String idMapa, int idEntidad, List<Integer> idExtractLayersList) {
		super();
		this.idProyecto = idProyecto;
		this.fechaExtraccion = fechaExtraccion;
		this.nombreProyecto = nombreProyecto;
		this.posEsquinaX = posEsquinaX;
		this.posEsquinaY = posEsquinaY;
		this.anchoCeldas = anchoCeldas;
		this.altoCeldas = altoCeldas;
		this.celdasX = celdasX;
		this.celdasY = celdasY;
		this.idMapa = idMapa;
		this.idExtractLayersList = idExtractLayersList;		
	}

	public int getSrid() {
		return srid;
	}

	public void setSrid(int srid) {
		this.srid = srid;
	}

	public String getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(String idProyecto) {
		this.idProyecto = idProyecto;
	}

	public Date getFechaExtraccion() {
		return fechaExtraccion;
	}

	public void setFechaExtraccion(Date fechaExtraccion) {
		this.fechaExtraccion = fechaExtraccion;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}

	public double getPosEsquinaX() {
		return posEsquinaX;
	}

	public void setPosEsquinaX(double posEsquinaX) {
		this.posEsquinaX = posEsquinaX;
	}

	public double getPosEsquinaY() {
		return posEsquinaY;
	}

	public void setPosEsquinaY(double posEsquinaY) {
		this.posEsquinaY = posEsquinaY;
	}

	public double getAnchoCeldas() {
		return anchoCeldas;
	}

	public void setAnchoCeldas(double anchoCeldas) {
		this.anchoCeldas = anchoCeldas;
	}

	public double getAltoCeldas() {
		return altoCeldas;
	}

	public void setAltoCeldas(double altoCeldas) {
		this.altoCeldas = altoCeldas;
	}

	public int getCeldasX() {
		return celdasX;
	}

	public void setCeldasX(int celdasX) {
		this.celdasX = celdasX;
	}

	public int getCeldasY() {
		return celdasY;
	}

	public void setCeldasY(int celdasY) {
		this.celdasY = celdasY;
	}
	
	public String getIdMapa() {
		return idMapa;
	}

	public void setIdMapa(String idMapa) {
		this.idMapa = idMapa;
	}

	public String getFechaExtraccionFormateada(){
		return sdf.format(fechaExtraccion);
	}
	
	public List<Integer> getIdExtractLayersList() {
		return idExtractLayersList;
	}

	public void setIdExtractLayersList(List<Integer> idExtractLayersList) {
		this.idExtractLayersList = idExtractLayersList;
	}

	public String getBloqueMetaInfo() {
		return bloqueMetaInfo;
	}

	public void setBloqueMetaInfo(String bloqueMetaInfo) {
		this.bloqueMetaInfo = bloqueMetaInfo;
	}

	@Override
	public String toString() {
		return nombreProyecto + " (" + getFechaExtraccionFormateada() + ")";
	}
	
}
