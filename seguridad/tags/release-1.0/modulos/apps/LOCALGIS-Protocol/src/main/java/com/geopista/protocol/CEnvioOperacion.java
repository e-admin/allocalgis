package com.geopista.protocol;

import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.ortofoto.CSolicitudEnvioOrtofoto;
import com.geopista.protocol.ortofoto.CSolicitudImportacionOrtofoto;

import java.io.Serializable;
import java.util.Properties;
import java.util.Hashtable;
import java.util.Vector;

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
