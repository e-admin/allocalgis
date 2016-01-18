package com.gestorfip.ws.beans.tramite.ui;

import java.util.Arrays;



public class EntidadBean {
	
	private int id;
	private String codigo;
	private String clave;
	private String nombre;
	private String etiqueta;
	private boolean suspendida;
	private int base_entidadid;
	private int madre;
	private int tramite;
	
	private int idLayer = -1;
	private int idFeature = -1;
	
	private String codigoValRefCU;

	private EntidadBean[] lstEntidadessHijas ;
	private BaseEntidadBean baseBean;
	
	private DocumentoEntidadBean[] lstDocumetos;
	private DocumentoEntidadBean[] lstDocumetosAlta;
	private DocumentoEntidadBean[] lstDocumetosBaja;
	
	private OperacionEntidadBean[] lstOperacionEntidad;
	
	public OperacionEntidadBean[] getLstOperacionEntidad() {
		return lstOperacionEntidad;
	}

	public void setLstOperacionEntidad(OperacionEntidadBean[] lstOperacionEntidad) {
		this.lstOperacionEntidad = lstOperacionEntidad;
	}

	public EntidadBean[] getLstDeterminacionesHijas() {
		return lstEntidadessHijas;
	}
	
	public void setLstDeterminacionesHijas(
			EntidadBean[] lstDeterminacionesHijas) {
		this.lstEntidadessHijas = lstDeterminacionesHijas;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
	
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public boolean isSuspendida() {
		return suspendida;
	}
	
	public void setSuspendida(boolean suspendida) {
		this.suspendida = suspendida;
	}

	public int getBase_entidadid() {
		return base_entidadid;
	}
	
	public void setBase_entidadid(int baseEntidadid) {
		base_entidadid = baseEntidadid;
	}
	
	public int getMadre() {
		return madre;
	}
	
	public void setMadre(int madre) {
		this.madre = madre;
	}
	
	public int getTramite() {
		return tramite;
	}
	
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}
	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public DocumentoEntidadBean[] getLstDocumetos() {
		return lstDocumetos;
	}

	public void setLstDocumetos(DocumentoEntidadBean[] lstDocumetos) {
		this.lstDocumetos = lstDocumetos;
	}

	public DocumentoEntidadBean[] getLstDocumetosAlta() {
		return lstDocumetosAlta;
	}

	public void setLstDocumetosAlta(DocumentoEntidadBean[] lstDocumetosAlta) {
		this.lstDocumetosAlta = lstDocumetosAlta;
	}

	public DocumentoEntidadBean[] getLstDocumetosBaja() {
		return lstDocumetosBaja;
	}

	public void setLstDocumetosBaja(DocumentoEntidadBean[] lstDocumetosBaja) {
		this.lstDocumetosBaja = lstDocumetosBaja;
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
	
	public String getCodigoValRefCU() {
		return codigoValRefCU;
	}

	public void setCodigoValRefCU(String codigoValRefCU) {
		this.codigoValRefCU = codigoValRefCU;
	}
	public void addEntidadHija(EntidadBean entidadHija) {
		if(lstEntidadessHijas == null){
			
			lstEntidadessHijas = new EntidadBean[1];
			lstEntidadessHijas[0] = entidadHija;
		
		}
		else{
			lstEntidadessHijas = (EntidadBean[]) Arrays.copyOf(lstEntidadessHijas, 
																			lstEntidadessHijas.length+1);
		
			lstEntidadessHijas[lstEntidadessHijas.length-1] = entidadHija;
		}
	}
	
	public void addBaseDeterminacion(EntidadBean entidad) {
		
		baseBean = new BaseEntidadBean();
		baseBean.setEntidadid(entidad.getBase_entidadid());
		baseBean.setTramite(entidad.getTramite());
		baseBean.setId(entidad.getId());
		baseBean.setNombre(entidad.getNombre());

	}
}
