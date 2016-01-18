/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.metadatos.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;

/**
 *
 * @author Balidea Consulting & Programming
 */

@SuppressWarnings("restriction")
@XmlRootElement(name="metadatosDto")
public class MetadatosDto extends DtoBase{

	private String metadatos;
	private IndicadorDto indicador;

	@XmlAttribute(name="metadatos")
	public String getMetadatos() {
		return metadatos;
	}

	public void setMetadatos(String metadatos) {
		this.metadatos = metadatos;
	}

	@XmlElement(name="indicadorDto")
	public IndicadorDto getIndicador() {
		return indicador;
	}

	public void setIndicador(IndicadorDto indicador) {
		this.indicador = indicador;
	}
}
