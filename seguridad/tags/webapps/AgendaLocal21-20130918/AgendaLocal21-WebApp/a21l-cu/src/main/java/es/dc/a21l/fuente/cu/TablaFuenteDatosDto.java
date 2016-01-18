/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;

@SuppressWarnings("restriction")
@XmlRootElement(name="tablaFuenteDatosDto")
public class TablaFuenteDatosDto extends DtoBase {
	private String nombre;
	private String esquema;
	private IndicadorDto indicador;
	private FuenteDto fuente;

	@XmlAttribute(name="nombre")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@XmlAttribute(name="esquema")
	public String getEsquema() {
		return esquema;
	}

	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}
	
	@XmlElement(name="indicador")
	public IndicadorDto getIndicador() {
		return indicador;
	}

	public void setIndicador(IndicadorDto indicador) {
		this.indicador = indicador;
	}

	@XmlElement(name="fuente")
	public FuenteDto getFuente() {
		return fuente;
	}

	public void setFuente(FuenteDto fuente) {
		this.fuente = fuente;
	}
	public String getNombreAcentos(){
		return arreglaAcentos(nombre);
	}
}
