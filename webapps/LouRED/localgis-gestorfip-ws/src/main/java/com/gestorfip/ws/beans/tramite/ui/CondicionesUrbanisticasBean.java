package com.gestorfip.ws.beans.tramite.ui;

public class CondicionesUrbanisticasBean {
	
	  private int id; 
	  private int codigoentidad_entidadid; 
	  private int codigodeterminacion_determinacionid; 
	  private int tramite;
	  
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCodigoentidad_entidadid() {
		return codigoentidad_entidadid;
	}
	
	public void setCodigoentidad_entidadid(int codigoentidadEntidadid) {
		codigoentidad_entidadid = codigoentidadEntidadid;
	}
	
	public int getCodigodeterminacion_determinacionid() {
		return codigodeterminacion_determinacionid;
	}
	
	public void setCodigodeterminacion_determinacionid(
			int codigodeterminacionDeterminacionid) {
		codigodeterminacion_determinacionid = codigodeterminacionDeterminacionid;
	}
	
	public int getTramite() {
		return tramite;
	}
	
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}

}
