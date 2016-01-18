/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.expresion.cu;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;

public class ExpresionDto extends DtoBase {

	private TipoOperandoEmun tipoOperandoIzq;
	private TipoOperandoEmun tipoOperandoDch;
	
	private Long idExpresionIzq;
	private Long idExpresionDch;
	private Double literalIzq;
	private Double literalDch;
	private AtributoFuenteDatosDto atributoFuenteDatosDtoIzq;
	private AtributoFuenteDatosDto atributoFuenteDatosDtoDch;
	
	private TipoOperacionEmun tipoOperacion;

	
	public TipoOperandoEmun getTipoOperandoIzq() {
		return tipoOperandoIzq;
	}

	public void setTipoOperandoIzq(TipoOperandoEmun tipoOperandoIzq) {
		this.tipoOperandoIzq = tipoOperandoIzq;
	}

	public TipoOperandoEmun getTipoOperandoDch() {
		return tipoOperandoDch;
	}

	public void setTipoOperandoDch(TipoOperandoEmun tipoOperandoDch) {
		this.tipoOperandoDch = tipoOperandoDch;
	}

	public Long getIdExpresionIzq() {
		return idExpresionIzq;
	}

	public void setIdExpresionIzq(Long idExpresionIzq) {
		this.idExpresionIzq = idExpresionIzq;
	}

	public Long getIdExpresionDch() {
		return idExpresionDch;
	}

	public void setIdExpresionDch(Long idExpresionDch) {
		this.idExpresionDch = idExpresionDch;
	}

	public Double getLiteralIzq() {
		return literalIzq;
	}

	public void setLiteralIzq(Double literalIzq) {
		this.literalIzq = literalIzq;
	}

	public Double getLiteralDch() {
		return literalDch;
	}

	public void setLiteralDch(Double literalDch) {
		this.literalDch = literalDch;
	}

	public AtributoFuenteDatosDto getAtributoFuenteDatosDtoIzq() {
		return atributoFuenteDatosDtoIzq;
	}

	public void setAtributoFuenteDatosDtoIzq(
			AtributoFuenteDatosDto atributoFuenteDatosDtoIzq) {
		this.atributoFuenteDatosDtoIzq = atributoFuenteDatosDtoIzq;
	}

	public AtributoFuenteDatosDto getAtributoFuenteDatosDtoDch() {
		return atributoFuenteDatosDtoDch;
	}

	public void setAtributoFuenteDatosDtoDch(
			AtributoFuenteDatosDto atributoFuenteDatosDtoDch) {
		this.atributoFuenteDatosDtoDch = atributoFuenteDatosDtoDch;
	}

	public TipoOperacionEmun getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(TipoOperacionEmun tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	

}
