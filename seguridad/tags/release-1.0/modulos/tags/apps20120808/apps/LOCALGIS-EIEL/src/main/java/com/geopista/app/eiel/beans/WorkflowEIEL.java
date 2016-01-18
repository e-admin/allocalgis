package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.geopista.app.eiel.ConstantesLocalGISEIEL;

public class WorkflowEIEL extends InventarioEIEL implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3607365627234226280L;
	
	private static final String TEMPORAL="T";
	private static final String PUBLICABLE="P";
	private static final String AUTO_PUBLICABLE="E";
	private static final String VALIDO="V";
	
	private int estadoValidacion;
	private int estadoValidacionAnterior;
	
	/*private boolean estadoTemporal;
	private boolean estadoPublicable;
	private boolean estadoValido=true;*/
	
	private long revisionActual;
	private long revisionExpirada;
	
	private String bloqueado = null;
	protected String codINEProvincia = null;
	protected String codINEMunicipio = null;
	
	protected long idelemento;

	protected LinkedHashMap elementos;

	
	public void setEstadoValidacion(int estadoValidacion){
		this.estadoValidacion=estadoValidacion;
	}
	
	public void setEstadoValidacionAnterior(int estadoValidacionAnterior){
		this.estadoValidacionAnterior=estadoValidacionAnterior;
	}
	
	public boolean isEstadoTemporal() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_TEMPORAL);
	}
	
	public boolean isEstadoValido() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_VALIDO);
	}

	public boolean isEstadoAutoPublicable() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_PUBLICABLE_MOVILIDAD);
	}
	
	public boolean isEstadoPublicable() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_PUBLICABLE);
	}

	public long getRevisionActual() {
		return revisionActual;
	}

	public void setRevisionActual(long revisionActual) {
		this.revisionActual = revisionActual;
	}

	public long getRevisionExpirada() {
		return revisionExpirada;
	}

	public void setRevisionExpirada(long revisionExpirada) {
		this.revisionExpirada = revisionExpirada;
	}
	
	public String getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	public String getCodINEProvincia() {
		return codINEProvincia;
	}
	public void setCodINEProvincia(String codINEProvincia) {
		this.codINEProvincia = codINEProvincia;
	}
	
	public String getCodINEMunicipio() {
		return codINEMunicipio;
	}

	
	public void setCodINEMunicipio(String codINEMunicipio) {
		this.codINEMunicipio = codINEMunicipio;
	}
		
	
	public String getEstadoValidacion(){
		if (isEstadoTemporal())
			return TEMPORAL;
		else if (isEstadoPublicable())
			return PUBLICABLE;
		else if (isEstadoAutoPublicable())
			return AUTO_PUBLICABLE;
		else
			return VALIDO;			
	}
	
	public int getEstadoValidacionAnterior(){
		return estadoValidacionAnterior;
	}

	public void setDatosAdicionales(LinkedHashMap elementos) {
		this.elementos=elementos;
		
	}
	
	public LinkedHashMap getDatosAdicionales(){
		return this.elementos;
	}

	public long getIdElemento() {
		return idelemento;
	}

	public void setIdElemento(long idelemento) {
		this.idelemento = idelemento;
	}
	
	

}
