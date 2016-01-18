package com.geopista.protocol.ocupacion;

import java.util.Date;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:06 $
 *          $Name:  $
 *          $RCSfile: CDatosOcupacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CDatosOcupacion {
	private long idOcupacion;
	private int tipoOcupacion;
	private String necesitaObra;
	private String numExpediente;
	private Date horaInicio;
	private Date horaFin;
	private int numMesas= -1;
	private int numSillas= -1;
	private double areaOcupacion= -1;
	private String afectaAcera;
    private String afectaCalzada;
	private String afectaAparcamiento;
	public double m2acera= -1;
	public double m2calzada= -1;
	public double m2aparcamiento= -1;
    private Date fechaInicio;
    private Date fechaFin;

	public CDatosOcupacion() {
	}

	public long getIdOcupacion() {
		return idOcupacion;
	}

	public void setIdOcupacion(long idOcupacion) {
		this.idOcupacion = idOcupacion;
	}

	public int getTipoOcupacion() {
		return tipoOcupacion;
	}

	public void setTipoOcupacion(int tipoOcupacion) {
		this.tipoOcupacion = tipoOcupacion;
	}

	public String getNecesitaObra() {
		return necesitaObra;
	}

	public void setNecesitaObra(String necesitaObra) {
		this.necesitaObra = necesitaObra;
	}

	public String getNumExpediente() {
		return numExpediente;
	}

	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Date getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(Date horaFin) {
		this.horaFin = horaFin;
	}

	public int getNumMesas() {
		return numMesas;
	}

	public void setNumMesas(int numMesas) {
		this.numMesas = numMesas;
	}

	public int getNumSillas() {
		return numSillas;
	}

	public void setNumSillas(int numSillas) {
		this.numSillas = numSillas;
	}

	public double getAreaOcupacion() {
		return areaOcupacion;
	}

	public void setAreaOcupacion(double areaOcupacion) {
		this.areaOcupacion = areaOcupacion;
	}

	public String getAfectaAcera() {
		return afectaAcera;
	}

	public void setAfectaAcera(String afectaAcera) {
		this.afectaAcera = afectaAcera;
	}

	public String getAfectaCalzada() {
		return afectaCalzada;
	}

	public void setAfectaCalzada(String afectaCalzada) {
		this.afectaCalzada = afectaCalzada;
	}

	public String getAfectaAparcamiento() {
		return afectaAparcamiento;
	}

	public void setAfectaAparcamiento(String afectaAparcamiento) {
		this.afectaAparcamiento = afectaAparcamiento;
	}

	public double getM2acera() {
		return m2acera;
	}

	public void setM2acera(double m2acera) {
		this.m2acera = m2acera;
	}

	public double getM2calzada() {
		return m2calzada;
	}

	public void setM2calzada(double m2calzada) {
		this.m2calzada = m2calzada;
	}

	public double getM2aparcamiento() {
		return m2aparcamiento;
	}

	public void setM2aparcamiento(double m2aparcamiento) {
		this.m2aparcamiento = m2aparcamiento;
	}

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
