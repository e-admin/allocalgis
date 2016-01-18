package com.geopista.app.catastro.servicioWebCatastro.beans;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.servicioWebCatastro.IdentificadorDerecho;

public class OrigenWSBean {

	private Expediente expediente;
	
	private FincaCatastro finca;
	
	private SueloCatastro suelo;
	
	private UnidadConstructivaCatastro unidadConstructiva;
	
	private BienInmuebleCatastro bienInmueble;
	
	private ConstruccionCatastro construccion;
	
	private Cultivo cultivo;

	private RepartoCatastro reparto;
	
	private IdentificadorDerecho derecho;
	
	//falta protocolo notarial
	
	//falta finca registral
	
	private int identificador;

	
	
	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public FincaCatastro getFinca() {
		return finca;
	}

	public void setFinca(FincaCatastro finca) {
		this.finca = finca;
	}

	public SueloCatastro getSuelo() {
		return suelo;
	}

	public void setSuelo(SueloCatastro suelo) {
		this.suelo = suelo;
	}

	public UnidadConstructivaCatastro getUnidadConstructiva() {
		return unidadConstructiva;
	}

	public void setUnidadConstructiva(UnidadConstructivaCatastro unidadConstructiva) {
		this.unidadConstructiva = unidadConstructiva;
	}

	public BienInmuebleCatastro getBienInmueble() {
		return bienInmueble;
	}

	public void setBienInmueble(BienInmuebleCatastro bienInmueble) {
		this.bienInmueble = bienInmueble;
	}

	public ConstruccionCatastro getConstruccion() {
		return construccion;
	}

	public void setConstruccion(ConstruccionCatastro construccion) {
		this.construccion = construccion;
	}

	public RepartoCatastro getReparto() {
		return reparto;
	}

	public void setReparto(RepartoCatastro reparto) {
		this.reparto = reparto;
	}

	public IdentificadorDerecho getDerecho() {
		return derecho;
	}

	public void setDerecho(IdentificadorDerecho derecho) {
		this.derecho = derecho;
	}

	public int getIdentificador() {
		return identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}
	
	
	public Cultivo getCultivo() {
		return cultivo;
	}

	public void setCultivo(Cultivo cultivo) {
		this.cultivo = cultivo;
	}

}
