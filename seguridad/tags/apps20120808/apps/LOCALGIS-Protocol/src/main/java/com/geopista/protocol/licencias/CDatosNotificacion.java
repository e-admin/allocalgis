package com.geopista.protocol.licencias;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CDatosNotificacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CDatosNotificacion implements java.io.Serializable{

	private String dniCif;
	private int idSolicitud;
    private CViaNotificacion viaNotificacion;
	private String fax;
	private String telefono;
	private String movil;
	private String email;
	private String tipoVia;
	private String nombreVia;
	private String numeroVia;
	private String portal;
	private String planta;
	private String escalera;
	private String letra;
	private String cpostal;
	private String municipio;
	private String provincia;
	private String notificar;

	public CDatosNotificacion() {
	}


	public CDatosNotificacion(String dniCif, CViaNotificacion viaNotificacion, String fax, String telefono, String movil, String email, String tipoVia, String nombreVia, String numeroVia, String portal, String planta, String escalera, String letra, String cpostal, String municipio, String provincia, String notificar) {
		this.dniCif = dniCif;
		this.viaNotificacion = viaNotificacion;
		this.fax = fax;
		this.telefono = telefono;
		this.movil = movil;
		this.email = email;
		this.tipoVia = tipoVia;
		this.nombreVia = nombreVia;
		this.numeroVia = numeroVia;
		this.portal = portal;
		this.planta = planta;
		this.escalera = escalera;
		this.letra = letra;
		this.cpostal = cpostal;
		this.municipio = municipio;
		this.provincia = provincia;
		this.notificar = notificar;
	}

	public String getDniCif() {
		return dniCif;
	}

	public void setDniCif(String dniCif) {
		this.dniCif = dniCif;
	}

	public int getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public CViaNotificacion getViaNotificacion() {
		return viaNotificacion;
	}

	public void setViaNotificacion(CViaNotificacion viaNotificacion) {
		this.viaNotificacion = viaNotificacion;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTipoVia() {
		return tipoVia;
	}

	public void setTipoVia(String tipoVia) {
		this.tipoVia = tipoVia;
	}

	public String getNombreVia() {
		return nombreVia;
	}

	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
	}

	public String getNumeroVia() {
		return numeroVia;
	}

	public void setNumeroVia(String numeroVia) {
		this.numeroVia = numeroVia;
	}

	public String getPortal() {
		return portal;
	}

	public void setPortal(String portal) {
		this.portal = portal;
	}

	public String getPlanta() {
		return planta;
	}

	public void setPlanta(String planta) {
		this.planta = planta;
	}

	public String getEscalera() {
		return escalera;
	}

	public void setEscalera(String escalera) {
		this.escalera = escalera;
	}

	public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public String getCpostal() {
		return cpostal;
	}

	public void setCpostal(String cpostal) {
		this.cpostal = cpostal;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNotificar() {
		return notificar;
	}

	public void setNotificar(String notificar) {
		this.notificar = notificar;
	}

}
