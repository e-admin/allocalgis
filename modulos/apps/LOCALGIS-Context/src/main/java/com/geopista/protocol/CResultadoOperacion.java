/**
 * CResultadoOperacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Vector;

import org.exolab.castor.xml.Marshaller;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:56 $
 *          $Name:  $
 *          $RCSfile: CResultadoOperacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */


public class CResultadoOperacion implements Serializable {
	private boolean resultado;
	private String descripcion;
	private Vector vector;
/*
	private CSolicitudLicencia solicitudLicencia;
	private CExpedienteLicencia expedienteLicencia;
*/
    private Vector Solicitudes;
    private Vector Expedientes;

	public CResultadoOperacion() {
	}

	public CResultadoOperacion(boolean resultado, String descripcion) {
		this.resultado = resultado;
		this.descripcion = descripcion;
	}

    public Vector getSolicitudes() {
        return Solicitudes;
    }

    public void setSolicitudes(Vector vSolicitudes) {
        this.Solicitudes = vSolicitudes;
    }

    public Vector getExpedientes() {
        return Expedientes;
    }

    public void setExpedientes(Vector vExpedientes) {
        this.Expedientes = vExpedientes;
    }

	public boolean getResultado() {
		return resultado;
	}

	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Vector getVector() {
		return vector;
	}

	public void setVector(Vector vector) {
		this.vector = vector;
	}
/*
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

*/
	public String buildResponse() {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(this, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(this);

			String response = sw.toString();
			return response;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return "";
		}

	}
}
