package com.geopista.protocol.licencias;

import com.geopista.protocol.licencias.tipos.CTipoAnexo;

import java.io.File;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CAnexo.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CAnexo implements java.io.Serializable {

	private String idAnexo = null;

	private CTipoAnexo tipoAnexo;
	private String observacion;
	private String fileName;
	private byte[] content;
    private int estado;
    private String path;
    private String comesFrom = null;

	public CAnexo() {
	}

	public CAnexo(CTipoAnexo tipoAnexo, String fileName, String observacion) {
		this.tipoAnexo = tipoAnexo;
		this.fileName = fileName;
		this.observacion = observacion;
	}


	public String getIdAnexo() {
		return idAnexo;
	}

	public void setIdAnexo(String idAnexo) {
		this.idAnexo = idAnexo;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path= path;
    }

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public CTipoAnexo getTipoAnexo() {
		return tipoAnexo;
	}

	public void setTipoAnexo(CTipoAnexo tipoAnexo) {
		this.tipoAnexo = tipoAnexo;
	}


	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

    public String getComesFrom() {
        return comesFrom;
    }

    public void setComesFrom(String from) {
        this.comesFrom= from;
    }

}
