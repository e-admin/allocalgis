/**
 * CPersonaJuridicoFisica.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CPersonaJuridicoFisica.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CPersonaJuridicoFisica implements java.io.Serializable{

	private long idPersona;
	private String dniCif;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String colegio;
	private String visado;
	private String titulacion;
	private CDatosNotificacion datosNotificacion;
    private String patronPerfil;

	public CPersonaJuridicoFisica() {
	}

	public CPersonaJuridicoFisica(String dniCif, String nombre, String apellido1, String apellido2, String colegio, String visado, String titulacion, CDatosNotificacion datosNotificacion) {
		this.dniCif = dniCif;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.colegio = colegio;
		this.visado = visado;
		this.titulacion = titulacion;
		this.datosNotificacion = datosNotificacion;
	}

	public long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public String getDniCif() {
		return dniCif;
	}

	public void setDniCif(String dniCif) {
		this.dniCif = dniCif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getColegio() {
		return colegio;
	}

	public void setColegio(String colegio) {
		this.colegio = colegio;
	}

	public String getVisado() {
		return visado;
	}

	public void setVisado(String visado) {
		this.visado = visado;
	}

	public String getTitulacion() {
		return titulacion;
	}

	public void setTitulacion(String titulacion) {
		this.titulacion = titulacion;
	}

	public CDatosNotificacion getDatosNotificacion() {
		return datosNotificacion;
	}

	public void setDatosNotificacion(CDatosNotificacion datosNotificacion) {
		this.datosNotificacion = datosNotificacion;
	}

    public String getPatronPerfil() {
        return patronPerfil;
    }

    public void setPatronPerfil(String patron) {
        this.patronPerfil= patron;
    }
      public boolean copy(Object o) {
        if (this == o) return true;
        if (!(o instanceof CPersonaJuridicoFisica)) return false;

        final CPersonaJuridicoFisica persona = (CPersonaJuridicoFisica) o;
        idPersona=persona.getIdPersona();
        dniCif=persona.getDniCif();
        nombre=persona.getNombre();
        apellido1=persona.getApellido1();
        apellido2=persona.getApellido2();
        colegio=persona.getColegio();
        titulacion=persona.getTitulacion();
        visado=persona.getVisado();
        return true;
    }



}
