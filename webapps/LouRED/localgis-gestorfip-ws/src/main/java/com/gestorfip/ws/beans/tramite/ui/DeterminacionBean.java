package com.gestorfip.ws.beans.tramite.ui;

import java.util.Arrays;



public class DeterminacionBean {
	
	private int id;
	private String codigo;
	private int caracter;
	private String apartado;
	private String nombre;
	private String etiqueta;
	private boolean suspendida;
	private String texto;
	private int unidad_determinacionid;
	private int base_determinacionid;
	private int madre;
	private int tramite;
	
	private DeterminacionBean[] lstDeterminacionesHijas ;
	private UnidadDeterminacionBean unidadBean;
	private BaseDeterminacionBean baseBean;
	
	private ValoresReferenciaBean[] lstValoresReferencia;
	private ValoresReferenciaBean[] lstValoresReferenciaAlta;
	private ValoresReferenciaBean[] lstValoresReferenciaBaja;
	
	private GrupoAplicacionBean[] lstGrupoAplicacion;
	private GrupoAplicacionBean[] lstGrupoAplicacionAlta;
	private GrupoAplicacionBean[] lstGrupoAplicacionBaja;
	
	private DeterminacionReguladoraBean[] lstDeterminacionReguladora;
	private DeterminacionReguladoraBean[] lstDeterminacionReguladoraAlta;
	private DeterminacionReguladoraBean[] lstDeterminacionReguladoraBaja;
	
	private RegulacionesEspecificasBean[] lstRegulacionesEspecificas;
	private RegulacionesEspecificasBean[] lstRegulacionesEspecificasAlta;
	private RegulacionesEspecificasBean[] lstRegulacionesEspecificasBaja;
	private RegulacionesEspecificasBean[] lstRegulacionesEspecificasModif;
	
	private DocumentoDeterminacionBean[] lstDocumetos;
	private DocumentoDeterminacionBean[] lstDocumetosAlta;
	private DocumentoDeterminacionBean[] lstDocumetosBaja;
	
	private OperacionDeterminacionBean[] lstOperacionDeterminacion;
	
	
	public OperacionDeterminacionBean[] getLstOperacionDeterminacion() {
		return lstOperacionDeterminacion;
	}

	public void setLstOperacionDeterminacion(
			OperacionDeterminacionBean[] lstOperacionDeterminacion) {
		this.lstOperacionDeterminacion = lstOperacionDeterminacion;
	}

	public DocumentoDeterminacionBean[] getLstDocumetos() {
		return lstDocumetos;
	}

	public void setLstDocumetos(DocumentoDeterminacionBean[] lstDocumetos) {
		this.lstDocumetos = lstDocumetos;
	}

	public DocumentoDeterminacionBean[] getLstDocumetosAlta() {
		return lstDocumetosAlta;
	}

	public void setLstDocumetosAlta(DocumentoDeterminacionBean[] lstDocumetosAlta) {
		this.lstDocumetosAlta = lstDocumetosAlta;
	}

	public DocumentoDeterminacionBean[] getLstDocumetosBaja() {
		return lstDocumetosBaja;
	}

	public void setLstDocumetosBaja(DocumentoDeterminacionBean[] lstDocumetosBaja) {
		this.lstDocumetosBaja = lstDocumetosBaja;
	}

	public RegulacionesEspecificasBean[] getLstRegulacionesEspecificas() {
		return lstRegulacionesEspecificas;
	}

	public void setLstRegulacionesEspecificas(
			RegulacionesEspecificasBean[] lstRegulacionesEspecificas) {
		this.lstRegulacionesEspecificas = lstRegulacionesEspecificas;
	}

	public GrupoAplicacionBean[] getLstGrupoAplicacion() {
		return lstGrupoAplicacion;
	}

	public void setLstGrupoAplicacion(GrupoAplicacionBean[] lstGrupoAplicacion) {
		this.lstGrupoAplicacion = lstGrupoAplicacion;
	}

	public DeterminacionReguladoraBean[] getLstDeterminacionReguladora() {
		return lstDeterminacionReguladora;
	}

	public void setLstDeterminacionReguladora(
			DeterminacionReguladoraBean[] lstDeterminacionReguladora) {
		this.lstDeterminacionReguladora = lstDeterminacionReguladora;
	}

	public ValoresReferenciaBean[] getLstValoresReferencia() {
		return lstValoresReferencia;
	}

	public void setLstValoresReferencia(ValoresReferenciaBean[] lstValoresReferencia) {
		this.lstValoresReferencia = lstValoresReferencia;
	}

	public BaseDeterminacionBean getBaseBean() {
		return baseBean;
	}

	public void setBaseBean(BaseDeterminacionBean baseBean) {
		this.baseBean = baseBean;
	}

	public UnidadDeterminacionBean getUnidadBean() {
		return unidadBean;
	}

	public void setUnidadBean(UnidadDeterminacionBean unidadBean) {
		this.unidadBean = unidadBean;
	}
	
	
	
	
	public DeterminacionBean[] getLstDeterminacionesHijas() {
		return lstDeterminacionesHijas;
	}
	
	public void setLstDeterminacionesHijas(
			DeterminacionBean[] lstDeterminacionesHijas) {
		this.lstDeterminacionesHijas = lstDeterminacionesHijas;
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
	
	public int getCaracter() {
		return caracter;
	}
	
	public void setCaracter(int caracter) {
		this.caracter = caracter;
	}
	
	public String getApartado() {
		return apartado;
	}
	
	public void setApartado(String apartado) {
		this.apartado = apartado;
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
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getUnidad_determinacionid() {
		return unidad_determinacionid;
	}
	
	public void setUnidad_determinacionid(int unidadDeterminacionid) {
		unidad_determinacionid = unidadDeterminacionid;
	}

	public int getBase_determinacionid() {
		return base_determinacionid;
	}
	
	public void setBase_determinacionid(int baseDeterminacionid) {
		base_determinacionid = baseDeterminacionid;
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
	
	public ValoresReferenciaBean[] getLstValoresReferenciaAlta() {
		return lstValoresReferenciaAlta;
	}

	public void setLstValoresReferenciaAlta(
			ValoresReferenciaBean[] lstValoresReferenciaAlta) {
		this.lstValoresReferenciaAlta = lstValoresReferenciaAlta;
	}

	public ValoresReferenciaBean[] getLstValoresReferenciaBaja() {
		return lstValoresReferenciaBaja;
	}

	public void setLstValoresReferenciaBaja(
			ValoresReferenciaBean[] lstValoresReferenciaBaja) {
		this.lstValoresReferenciaBaja = lstValoresReferenciaBaja;
	}

	public GrupoAplicacionBean[] getLstGrupoAplicacionAlta() {
		return lstGrupoAplicacionAlta;
	}

	public void setLstGrupoAplicacionAlta(
			GrupoAplicacionBean[] lstGrupoAplicacionAlta) {
		this.lstGrupoAplicacionAlta = lstGrupoAplicacionAlta;
	}

	public GrupoAplicacionBean[] getLstGrupoAplicacionBaja() {
		return lstGrupoAplicacionBaja;
	}

	public void setLstGrupoAplicacionBaja(
			GrupoAplicacionBean[] lstGrupoAplicacionBaja) {
		this.lstGrupoAplicacionBaja = lstGrupoAplicacionBaja;
	}

	public DeterminacionReguladoraBean[] getLstDeterminacionReguladoraAlta() {
		return lstDeterminacionReguladoraAlta;
	}

	public void setLstDeterminacionReguladoraAlta(
			DeterminacionReguladoraBean[] lstDeterminacionReguladoraAlta) {
		this.lstDeterminacionReguladoraAlta = lstDeterminacionReguladoraAlta;
	}

	public DeterminacionReguladoraBean[] getLstDeterminacionReguladoraBaja() {
		return lstDeterminacionReguladoraBaja;
	}

	public void setLstDeterminacionReguladoraBaja(
			DeterminacionReguladoraBean[] lstDeterminacionReguladoraBaja) {
		this.lstDeterminacionReguladoraBaja = lstDeterminacionReguladoraBaja;
	}

	public RegulacionesEspecificasBean[] getLstRegulacionesEspecificasAlta() {
		return lstRegulacionesEspecificasAlta;
	}

	public void setLstRegulacionesEspecificasAlta(
			RegulacionesEspecificasBean[] lstRegulacionesEspecificasAlta) {
		this.lstRegulacionesEspecificasAlta = lstRegulacionesEspecificasAlta;
	}

	public RegulacionesEspecificasBean[] getLstRegulacionesEspecificasBaja() {
		return lstRegulacionesEspecificasBaja;
	}

	public void setLstRegulacionesEspecificasBaja(
			RegulacionesEspecificasBean[] lstRegulacionesEspecificasBaja) {
		this.lstRegulacionesEspecificasBaja = lstRegulacionesEspecificasBaja;
	}

	public RegulacionesEspecificasBean[] getLstRegulacionesEspecificasModif() {
		return lstRegulacionesEspecificasModif;
	}

	public void setLstRegulacionesEspecificasModif(
			RegulacionesEspecificasBean[] lstRegulacionesEspecificasModif) {
		this.lstRegulacionesEspecificasModif = lstRegulacionesEspecificasModif;
	}


	public void addDeterminacionHija(DeterminacionBean determinacionHija) {
		if(lstDeterminacionesHijas == null){
			
			lstDeterminacionesHijas = new DeterminacionBean[1];
			lstDeterminacionesHijas[0] = determinacionHija;
		
		}
		else{
			lstDeterminacionesHijas = (DeterminacionBean[]) Arrays.copyOf(lstDeterminacionesHijas, 
																			lstDeterminacionesHijas.length+1);
		
			lstDeterminacionesHijas[lstDeterminacionesHijas.length-1] = determinacionHija;
		}
	}
	
	public void addUnidadDeterminacion(DeterminacionBean determinacion) {
			
		unidadBean = new UnidadDeterminacionBean();
		unidadBean.setDeterminacionid(determinacion.getUnidad_determinacionid());
		unidadBean.setTramite(determinacion.getTramite());
		unidadBean.setId(determinacion.getId());
		unidadBean.setNombre(determinacion.getNombre());
	
	}
	
	public void addBaseDeterminacion(DeterminacionBean determinacion) {
		
		baseBean = new BaseDeterminacionBean();
		baseBean.setDeterminacionid(determinacion.getBase_determinacionid());
		baseBean.setTramite(determinacion.getTramite());
		baseBean.setId(determinacion.getId());
		baseBean.setNombre(determinacion.getNombre());

	}
}
