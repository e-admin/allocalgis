/**
 * CEnvioOperacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.ortofoto.CSolicitudEnvioOrtofoto;
import com.geopista.protocol.ortofoto.CSolicitudImportacionOrtofoto;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:56 $
 *          $Name:  $
 *          $RCSfile: CEnvioOperacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CEnvioOperacion implements Serializable {

	private int comando;
	private CSolicitudLicencia solicitudLicencia;
	private CExpedienteLicencia expedienteLicencia;
	private CSolicitudImportacionOrtofoto solicitudImportacionOrtofoto;
	private CSolicitudEnvioOrtofoto solicitudEnvioOrtofoto;

	private Object parametro;
	private Hashtable hash;
    private Vector tiposLicencia;
    private Object parametro2;

	public CEnvioOperacion() {

	}

	public CEnvioOperacion(int comando) {
		this.comando = comando;
	}

	public int getComando() {
		return comando;
	}

	public void setComando(int comando) {
		this.comando = comando;
	}

	public Object getParametro() {
		return parametro;
	}

	public void setParametro(Object parametro) {
		this.parametro = parametro;
	}

	public CSolicitudLicencia getSolicitudLicencia() {
		return solicitudLicencia;
	}

	public void setSolicitudLicencia(CSolicitudLicencia solicitudLicencia) {
		this.solicitudLicencia = solicitudLicencia;
	}

	public CExpedienteLicencia getExpedienteLicencia() {
		return expedienteLicencia;
	}

	public void setExpedienteLicencia(CExpedienteLicencia expedienteLicencia) {
		this.expedienteLicencia = expedienteLicencia;
	}

	public Hashtable getHashtable() {
		return hash;
	}

	public void setHashtable(Hashtable hash) {
		this.hash = hash;
	}

    public Vector getTiposLicencia() {
        return tiposLicencia;
    }

    public void setTiposLicencia(Vector tiposLicencia) {
        this.tiposLicencia = tiposLicencia;
    }

    public Object getParametro2() {
        return parametro2;
    }

    public void setParametro2(Object parametro) {
        this.parametro2 = parametro;
    }

	public CSolicitudImportacionOrtofoto getSolicitudImportacionOrtofoto() {
		
		return solicitudImportacionOrtofoto;
	}

	public void setSolicitudImportacionOrtofoto(
			CSolicitudImportacionOrtofoto solicitudImportacionOrtofoto) {
		
		this.solicitudImportacionOrtofoto = solicitudImportacionOrtofoto;
	}

	public CSolicitudEnvioOrtofoto getSolicitudEnvioOrtofoto() {
		
		return solicitudEnvioOrtofoto;
	}

	public void setSolicitudEnvioOrtofoto(
			CSolicitudEnvioOrtofoto solicitudEnvioOrtofoto) {
		
		this.solicitudEnvioOrtofoto = solicitudEnvioOrtofoto;
	}


}
