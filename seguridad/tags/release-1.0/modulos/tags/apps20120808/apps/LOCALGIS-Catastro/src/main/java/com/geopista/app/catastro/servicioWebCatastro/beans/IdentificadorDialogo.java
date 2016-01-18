package com.geopista.app.catastro.servicioWebCatastro.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.IdBienInmueble;

public class IdentificadorDialogo implements Serializable{

	private String identificadorDialogo;
	
	private ArrayList lstExpedientes;
	
	private Object fincaBien;
	
	private String codigoDelegacion;
    private String codigoMunicipioDGC;
    private String pc1;
    private String pc2;
    private boolean isFincaCatastro = false;
    private boolean isBienCatastro = false;
	private String BICE;

	private String claseBienInmueble;
	
	private IdBienInmueble idBienInmueble;

	public IdBienInmueble getIdBienInmueble() {
		return idBienInmueble;
	}

	public void setIdBienInmueble(IdBienInmueble idBienInmueble) {
		this.idBienInmueble = idBienInmueble;
	}

	public String getClaseBienInmueble() {
		return claseBienInmueble;
	}

	public void setClaseBienInmueble(String claseBienInmueble) {
		this.claseBienInmueble = claseBienInmueble;
	}

	public String getBICE() {
		return BICE;
	}

	public void setBICE(String bICE) {
		BICE = bICE;
	}

	public String getCodigoDelegacion() {
		return codigoDelegacion;
	}

	public void setCodigoDelegacion(String codigoDelegacion) {
		this.codigoDelegacion = codigoDelegacion;
	}

	public String getCodigoMunicipioDGC() {
		return codigoMunicipioDGC;
	}

	public void setCodigoMunicipioDGC(String codigoMunicipioDGC) {
		this.codigoMunicipioDGC = codigoMunicipioDGC;
	}

	public String getPc1() {
		return pc1;
	}

	public void setPc1(String pc1) {
		this.pc1 = pc1;
	}

	public String getPc2() {
		return pc2;
	}

	public void setPc2(String pc2) {
		this.pc2 = pc2;
	}

	public boolean isFincaCatastro() {
		
		if(fincaBien instanceof FincaCatastro){
			isFincaCatastro = true;
		}
		else if(fincaBien instanceof BienInmuebleCatastro){
			isFincaCatastro = false;
		}
		return isFincaCatastro;
	}

	public void setFincaCatastro(boolean isFincaCatastro) {
		this.isFincaCatastro = isFincaCatastro;
	}
	
	public boolean isBienCatastro() {
		if(fincaBien instanceof FincaCatastro){
			isBienCatastro = false;
		}
		else if(fincaBien instanceof BienInmuebleCatastro){
			isBienCatastro = true;
		}
		return isBienCatastro;
	}

	public void setBienCatastro(boolean isBienCatastro) {
		this.isBienCatastro = isBienCatastro;
	}


	public Object getFincaBien() {
		return fincaBien;
	}

	public void setFincaBien(Object fincaBien) {
		this.fincaBien = fincaBien;
	}

	public String getIdentificadorDialogo() {
		return identificadorDialogo;
	}

	public void setIdentificadorDialogo(String identificadorDialogo) {
		this.identificadorDialogo = identificadorDialogo;
	}

	public ArrayList getLstExpedientes() {
		return lstExpedientes;
	}

	public void setLstExpedientes(ArrayList lstExpedientes) {
		this.lstExpedientes = lstExpedientes;
	}

	
}
