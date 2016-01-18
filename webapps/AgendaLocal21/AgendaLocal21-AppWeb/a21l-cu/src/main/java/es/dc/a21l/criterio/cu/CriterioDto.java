/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.criterio.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.fuente.cu.AtributoDto;

@SuppressWarnings("restriction")
@XmlRootElement(name="CriterioDto")
public class CriterioDto extends DtoBase {
	
	private TipoOperandoCriterioEmun tipoOperandoIzq;
	private TipoOperandoCriterioEmun tipoOperandoDch;
	
	private Long idCriterioIzq;
	private Long idCriterioDch;
	private Double literalIzq;
	private Double literalDch;
	private String strLiteralIzq;
	private String strLiteralDch;
	
	private TipoOperacionCriterioEmun tipoOperacion;

	private Long idAtributo;
	private String cadenaCriterio;
	
	//ESte campo es SOLO para manejar los datos de la jsp
	private AtributoDto atributo;
	
	@XmlElement(name="idAtributo")
	public Long getIdAtributo() {
		return idAtributo;
	}
	public void setIdAtributo(Long idAtributo) {
		this.idAtributo = idAtributo;
	}
	
	@XmlAttribute(name="tipoOperandoIzq")
	public TipoOperandoCriterioEmun getTipoOperandoIzq() {
		return tipoOperandoIzq;
	}
	
	public void setTipoOperandoIzq(TipoOperandoCriterioEmun tipoOperandoIzq) {
		this.tipoOperandoIzq = tipoOperandoIzq;
	}
	
	@XmlAttribute(name="tipoOperandoDch")
	public TipoOperandoCriterioEmun getTipoOperandoDch() {
		return tipoOperandoDch;
	}
	
	public void setTipoOperandoDch(TipoOperandoCriterioEmun tipoOperandoDch) {
		this.tipoOperandoDch = tipoOperandoDch;
	}
	
	@XmlAttribute(name="idCriterioIzq")
	public Long getIdCriterioIzq() {
		return idCriterioIzq;
	}
	
	public void setIdCriterioIzq(Long idCriterioIzq) {
		this.idCriterioIzq = idCriterioIzq;
	}
	
	@XmlAttribute(name="idCriterioDch")
	public Long getIdCriterioDch() {
		return idCriterioDch;
	}
	public void setIdCriterioDch(Long idCriterioDch) {
		this.idCriterioDch = idCriterioDch;
	}
	
	@XmlAttribute(name="literalIzq")
	public Double getLiteralIzq() {
		return literalIzq;
	}
	
	public void setLiteralIzq(Double literalIzq) {
		this.literalIzq = literalIzq;
	}
	
	@XmlAttribute(name="literalDch")
	public Double getLiteralDch() {
		return literalDch;
	}
	
	public void setLiteralDch(Double literalDch) {
		this.literalDch = literalDch;
	}
	
	@XmlAttribute(name="tipoOperacion")
	public TipoOperacionCriterioEmun getTipoOperacion() {
		return tipoOperacion;
	}
	
	public void setTipoOperacion(TipoOperacionCriterioEmun tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	
	@XmlAttribute(name="cadenaCriterio")
	public String getCadenaCriterio() {
		return cadenaCriterio;
	}

	public void setCadenaCriterio(String cadenaCriterio) {
		this.cadenaCriterio = cadenaCriterio;
	}
	public AtributoDto getAtributo() {
		return atributo;
	}
	public void setAtributo(AtributoDto atributo) {
		this.atributo = atributo;
	}
	
	@XmlAttribute(name="strLiteralIzq")
	public String getStrLiteralIzq() {
		return strLiteralIzq;
	}
	
	public void setStrLiteralIzq(String strLiteralIzq) {
		this.strLiteralIzq = strLiteralIzq;
	}
	
	@XmlAttribute(name="strLiteralDch")
	public String getStrLiteralDch() {
		return strLiteralDch;
	}
	
	public void setStrLiteralDch(String strLiteralDch) {
		this.strLiteralDch = strLiteralDch;
	}
}