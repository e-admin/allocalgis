/**
 * CNotificacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

import java.util.Date;

import com.geopista.protocol.licencias.estados.CEstadoNotificacion;
import com.geopista.protocol.licencias.tipos.CTipoNotificacion;

/**
 * @author SATEC
 * @version $Revision: 1.2 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2012/11/15 15:32:28 $
 *          $Name:  $
 *          $RCSfile: CNotificacion.java,v $
 *          $Revision: 1.2 $
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
    private long plazo_dias;
    
    public long getPlazo_dias() {
		return plazo_dias;
	}


	public void setPlazo_dias(long plazo_dias) {
		this.plazo_dias = plazo_dias;
	}


	public String getPlantilla() {
		return plantilla;
	}


	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	private String entregadaA;
    private String plantilla;

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
	
	public CNotificacion(long idNotificacion, CTipoNotificacion tipoNotificacion, long idAlegacion, long idMejora, CPersonaJuridicoFisica persona, int notificadaPor, Date fechaCreacion, Date fechaNotificacion, String responsable, Date plazoVencimiento, String habiles, int numDias, String observaciones, CEstadoNotificacion estadoNotificacion, Date fecha_reenvio, String procedencia, String plantilla) {
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
		this.plantilla = plantilla;
	}
	
	public CNotificacion(long idNotificacion, CTipoNotificacion tipoNotificacion, long idAlegacion, long idMejora, CPersonaJuridicoFisica persona, int notificadaPor, Date fechaCreacion, Date fechaNotificacion, String responsable, Date plazoVencimiento, String habiles, int numDias, String observaciones, CEstadoNotificacion estadoNotificacion, Date fecha_reenvio, String procedencia, String plantilla, long plazoDias) {
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
		this.plantilla = plantilla;
		this.plazo_dias = plazoDias;
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
