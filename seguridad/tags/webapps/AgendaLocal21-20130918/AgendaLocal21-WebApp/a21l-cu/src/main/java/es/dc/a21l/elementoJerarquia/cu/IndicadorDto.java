/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name="indicadorDto")
public class IndicadorDto extends ElementoJerarquiaDto {
	
	private Boolean publico;
	private Boolean pteAprobacionPublico;
	private Boolean publicadoEnWeb;
	private Boolean pteAprobacion;
	private Long idCategoria;
	private String loginCreador;
	private Integer numUsuariosPublicacion;
	private String loginUltimaPeticion;
	
	//Campos jsp
	private String urlPublicacion;
	private String urlPublicacionCorta;
	
	public IndicadorDto(){
		publico=false;
		pteAprobacionPublico=false;
		idCategoria=null;
		publicadoEnWeb = false;
		pteAprobacion = false;
	}
	
	public IndicadorDto(Long idCategoria){
		publico=false;
		pteAprobacionPublico=false;
		this.idCategoria=idCategoria;
		publicadoEnWeb = false;
		pteAprobacion = false;
	}
	
	@XmlAttribute(name="loginCreador")
	public String getLoginCreador() {
		return loginCreador;
	}

	public void setLoginCreador(String loginCreador) {
		this.loginCreador = loginCreador;
	}

	@XmlAttribute(name="publico")
	public Boolean getPublico() {
		return publico;
	}
	public void setPublico(Boolean publico) {
		this.publico = publico;
	}
	
	@XmlAttribute(name="pteAprobacionPublico")
	public Boolean getPteAprobacionPublico() {
		return pteAprobacionPublico;
	}
	 
	public void setPteAprobacionPublico(Boolean pteAprobacionPublico) {
		this.pteAprobacionPublico = pteAprobacionPublico;
	}
	
	@XmlAttribute(name="idCategoria")
	public Long getIdCategoria() {
		return idCategoria;
	}
	
	public void setIdCategoria(Long idCategoria) {
		this.idCategoria = idCategoria;
	}

	@XmlAttribute(name="publicadoEnWeb")
	public Boolean getPublicadoEnWeb() {
		return publicadoEnWeb;
	}

	public void setPublicadoEnWeb(Boolean publicadoEnWeb) {
		this.publicadoEnWeb = publicadoEnWeb;
	}
	
	@XmlAttribute(name="pteAprobacion")
	public Boolean getPteAprobacion() {
		return pteAprobacion;
	}

	public void setPteAprobacion(Boolean pteAprobacion) {
		this.pteAprobacion = pteAprobacion;
	}

	@XmlAttribute(name="numUsuariosPublicacion")
	public Integer getNumUsuariosPublicacion() {
		return numUsuariosPublicacion;
	}

	public void setNumUsuariosPublicacion(Integer numUsuariosPublicacion) {
		this.numUsuariosPublicacion = numUsuariosPublicacion;
	}

	@XmlAttribute(name="loginUltimaPeticion")
	public String getLoginUltimaPeticion() {
		return loginUltimaPeticion;
	}

	public void setLoginUltimaPeticion(String loginUltimaPeticion) {
		this.loginUltimaPeticion = loginUltimaPeticion;
	}

	public String getUrlPublicacion() {
		return urlPublicacion;
	}

	public void setUrlPublicacion(String urlPublicacion) {
		this.urlPublicacion = urlPublicacion;
	}
	
	public String getUrlPublicacionCorta() {
		return urlPublicacionCorta;
	}

	public void setUrlPublicacionCorta(String urlPublicacionCorta) {
		this.urlPublicacionCorta = urlPublicacionCorta;
	}
}
