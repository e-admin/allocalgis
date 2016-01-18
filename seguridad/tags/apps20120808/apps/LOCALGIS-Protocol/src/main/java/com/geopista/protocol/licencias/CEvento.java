package com.geopista.protocol.licencias;

import com.geopista.protocol.licencias.estados.CEstado;

import java.util.Date;

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
