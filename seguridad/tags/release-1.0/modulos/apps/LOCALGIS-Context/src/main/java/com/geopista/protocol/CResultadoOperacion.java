package com.geopista.protocol;

import org.exolab.castor.xml.Marshaller;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Vector;

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
