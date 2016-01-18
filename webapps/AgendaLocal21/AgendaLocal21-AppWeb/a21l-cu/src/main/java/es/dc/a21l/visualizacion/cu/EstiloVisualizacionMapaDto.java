/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.cu;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.usuario.cu.UsuarioDto;

/**
 *
 * @author Balidea Consulting & Programming
 */

@SuppressWarnings("restriction")
@XmlRootElement(name="estiloVisualizacionMapaDto")
public class EstiloVisualizacionMapaDto extends DtoBase {
	
	private UsuarioDto usuario;
	private IndicadorDto indicador;

	@XmlElement(name="usuario")
	public UsuarioDto getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDto usuario) {
		this.usuario = usuario;
	}
	
	@XmlElement(name="indicador")
	public IndicadorDto getIndicador() {
		return indicador;
	}

	public void setIndicador(IndicadorDto indicador) {
		this.indicador = indicador;
	}	
}