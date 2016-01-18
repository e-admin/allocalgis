/**
 * CEvento.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

import java.util.Date;

import com.geopista.protocol.licencias.estados.CEstado;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CEvento.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CEvento implements java.io.Serializable{

	private long idEvento;
	private String tipoEvento;
	private Date fechaEvento;
	private String revisado;
	private String revisadoPor;
	private String content;
    private CEstado estado;
    private CExpedienteLicencia expediente;


	public CEvento() {
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(long idEvento) {
		this.idEvento = idEvento;
	}

	public CExpedienteLicencia getExpediente() {
		return expediente;
	}

	public void setExpediente(CExpedienteLicencia expediente) {
		this.expediente = expediente;
	}

    public void setEstado(CEstado estado) {
        this.estado= estado;
    }

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Date getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public String getRevisado() {
		return revisado;
	}

	public void setRevisado(String revisado) {
		this.revisado = revisado;
	}

	public String getRevisadoPor() {
		return revisadoPor;
	}

	public void setRevisadoPor(String revisadoPor) {
		this.revisadoPor = revisadoPor;
	}

    public CEstado getEstado(){
        return estado;
    }

}
