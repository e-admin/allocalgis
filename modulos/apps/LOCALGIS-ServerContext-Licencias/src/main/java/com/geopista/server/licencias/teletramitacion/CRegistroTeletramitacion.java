/**
 * CRegistroTeletramitacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.licencias.teletramitacion;

import java.util.Date;
import java.util.Vector;

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
