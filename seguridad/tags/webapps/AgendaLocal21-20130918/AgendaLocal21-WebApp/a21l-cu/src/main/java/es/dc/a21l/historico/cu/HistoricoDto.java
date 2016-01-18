/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.historico.cu;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;

@SuppressWarnings("restriction")
@XmlRootElement(name="historicoDto")
public class HistoricoDto extends DtoBase {
	private Date fecha;
	private String strFecha;
	private IndicadorDto indicador;
	private byte[] datosDto;
	private byte[] mapaDto;

	public HistoricoDto() {
		this.indicador = new IndicadorDto();
	}
	
	@XmlAttribute(name="fecha")
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	@XmlElement(name="indicador")
	public IndicadorDto getIndicador() {
		return indicador;
	}

	public void setIndicador(IndicadorDto indicador) {
		this.indicador = indicador;
	}

	@XmlAttribute(name="datosDto")
	public byte[] getDatosDto() {
		return datosDto;
	}

	public void setDatosDto(byte[] datos) {
		this.datosDto = datos;
	}
	
	@XmlAttribute(name="mapaDto")
	public byte[] getMapaDto() {
		return mapaDto;
	}

	public void setMapaDto(byte[] mapa) {
		this.mapaDto = mapa;
	}

	public String getStrFecha() {
		return strFecha;
	}

	public void setStrFecha(String strFecha) {
		this.strFecha = strFecha;
	}
	
}
