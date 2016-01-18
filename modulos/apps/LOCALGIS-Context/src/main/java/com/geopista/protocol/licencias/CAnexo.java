/**
 * CAnexo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

import com.geopista.protocol.licencias.tipos.CTipoAnexo;

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
