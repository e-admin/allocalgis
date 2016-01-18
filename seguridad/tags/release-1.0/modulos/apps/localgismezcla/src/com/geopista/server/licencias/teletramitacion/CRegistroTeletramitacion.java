package com.geopista.server.licencias.teletramitacion;

import java.util.Vector;
import java.util.Date;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:56 $
 *          $Name:  $
 *          $RCSfile: CRegistroTeletramitacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CRegistroTeletramitacion {

	private String numreg;
	private String pdfFileName;
	private Vector anexos;
    private String nifSolicitante;
    private String nombreSolicitante;
	private String idMunicipio;
    private Date fechaEntradaRegistro;


	public CRegistroTeletramitacion(String numreg, String pdfFileName, Vector anexos, String nifSolicitante, String nombreSolicitante) {
		this.numreg = numreg;
		this.pdfFileName = pdfFileName;
		this.anexos = anexos;
        this.nifSolicitante = nifSolicitante;
        this.nombreSolicitante=nombreSolicitante;
	}

	public String getNumreg() {
		return numreg;
	}

	public void setNumreg(String numreg) {
		this.numreg = numreg;
	}

	public String getPdfFileName() {
		return pdfFileName;
	}

	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public Vector getAnexos() {
		return anexos;
	}

	public void setAnexos(Vector anexos) {
		this.anexos = anexos;
	}
    public String getNifSolicitante()
    {
        return nifSolicitante;
	}
    public String getNombreSolicitante()
    {
        return nombreSolicitante;
    }

    public void setFechaEntradaRegistro(Date fecha){
        this.fechaEntradaRegistro= fecha;
    }

    public Date getFechaEntradaRegistro(){
        return fechaEntradaRegistro;
    }





}
