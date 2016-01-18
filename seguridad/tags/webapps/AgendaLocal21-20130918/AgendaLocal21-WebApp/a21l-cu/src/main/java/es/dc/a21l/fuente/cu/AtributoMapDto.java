/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.dc.a21l.base.modelo.encapsulacionSW.LinkedHashMapAdapter;

@SuppressWarnings("restriction")
@XmlRootElement(name="atributoMapDto")
public class AtributoMapDto {
	
	private LinkedHashMap<String,AtributoValoresDto> contenido = new LinkedHashMap<String,AtributoValoresDto>();	

	public void setValor(String columna ,AtributoValoresDto valores) {
		this.contenido.put(columna, valores);
	}

	@XmlJavaTypeAdapter(LinkedHashMapAdapter.class)
	public LinkedHashMap<String, AtributoValoresDto> getContenido() {
		return contenido;
	}

	public void setContenido(LinkedHashMap<String, AtributoValoresDto> contenido) {
		this.contenido = contenido;
	}
}