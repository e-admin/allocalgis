package com.geopista.protocol.licencias;

import com.geopista.protocol.licencias.estados.CEstadoNotificacion;
import com.geopista.protocol.licencias.tipos.CTipoNotificacion;

import java.util.Date;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CNotificacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CNotificacion implements java.io.Serializable{



	private long idNotificacion;
	private CTipoNotificacion tipoNotificacion;
	private long idAlegacion;
	private long idMejora;
	private CPersonaJuridicoFisica persona;
	private int notificadaPor;
	private Date fechaCreacion;
	private Date fechaNotificacion;
	private String responsable;
	private Date plazoVencimiento;
	private String habiles;
	private int numDias;
	private String observaciones;
	private CEstadoNotificacion estadoNotificacion;
	private Date fecha_reenvio;
	private String procedencia;
    private CExpedienteLicencia expediente;
    private CSolicitudLicencia solicitud;
    private String entregadaA;

	public CNotificacion() {
	}


	public CNotificacion(long idNotificacion, CTipoNotificacion tipoNotificacion, long idAlegacion, long idMejora, CPersonaJuridicoFisica persona, int notificadaPor, Date fechaCreacion, Date fechaNotificacion, String responsable, Date plazoVencimiento, String habiles, int numDias, String observaciones, CEstadoNotificacion estadoNotificacion, Date fecha_reenvio, String procedencia) {
		this.idNotificacion = idNotificacion;
		this.tipoNotificacion = tipoNotificacion;
		this.idAlegacion = idAlegacion;
		this.idMejora = idMejora;
		this.persona = persona;
		this.notificadaPor = notificadaPor;
		this.fechaCreacion = fechaCreacion;
		this.fechaNotificacion = fechaNotificacion;
		this.responsable = responsable;
		this.plazoVencimiento = plazoVencimiento;
		this.habiles = habiles;
		this.numDias = numDias;
		this.observaciones = observaciones;
		this.estadoNotificacion = estadoNotificacion;
		this.fecha_reenvio = fecha_reenvio;
		this.procedencia = procedencia;
	}


	public long getIdNotificacion() {
		return idNotificacion;
	}

	public void setIdNotificacion(long idNotificacion) {
		this.idNotificacion = idNotificacion;
	}

	public CTipoNotificacion getTipoNotificacion() {
		return tipoNotificacion;
	}

	public void setTipoNotificacion(CTipoNotificacion tipoNotificacion) {
		this.tipoNotificacion = tipoNotificacion;
	}

	public CExpedienteLicencia getExpediente() {
		return expediente;
	}

	public void setExpediente(CExpedienteLicencia expediente) {
		this.expediente = expediente;
	}

    public void setSolicitud(CSolicitudLicencia solicitud) {
        this.solicitud = solicitud;
    }

    public CSolicitudLicencia getSolicitud() {
        return solicitud;
    }

	public long getIdAlegacion() {
		return idAlegacion;
	}

	public void setIdAlegacion(long idAlegacion) {
		this.idAlegacion = idAlegacion;
	}

	public long getIdMejora() {
		return idMejora;
	}

	public void setIdMejora(long idMejora) {
		this.idMejora = idMejora;
	}

	public CPersonaJuridicoFisica getPersona() {
		return persona;
	}

	public void setPersona(CPersonaJuridicoFisica persona) {
		this.persona = persona;
	}

	public int getNotificadaPor() {
		return notificadaPor;
	}

	public void setNotificadaPor(int notificadaPor) {
		this.notificadaPor = notificadaPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaNotificacion() {
		return fechaNotificacion;
	}

	public void setFechaNotificacion(Date fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public Date getPlazoVencimiento() {
		return plazoVencimiento;
	}

	public void setPlazoVencimiento(Date plazoVencimiento) {
		this.plazoVencimiento = plazoVencimiento;
	}

	public String getHabiles() {
		return habiles;
	}

	public void setHabiles(String habiles) {
		this.habiles = habiles;
	}

	public int getNumDias() {
		return numDias;
	}

	public void setNumDias(int numDias) {
		this.numDias = numDias;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public CEstadoNotificacion getEstadoNotificacion() {
		return estadoNotificacion;
	}

	public void setEstadoNotificacion(CEstadoNotificacion estadoNotificacion) {
		this.estadoNotificacion = estadoNotificacion;
	}

	public Date getFecha_reenvio() {
		return fecha_reenvio;
	}

	public void setFecha_reenvio(Date fecha_reenvio) {
		this.fecha_reenvio = fecha_reenvio;
	}

	public String getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

    public String getEntregadaA() {
        return entregadaA;
    }

    public void setEntregadaA(String nombre) {
        this.entregadaA= nombre;
    }

}
