/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import java.util.LinkedHashMap;
import java.util.WeakHashMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.dc.a21l.base.modelo.encapsulacionSW.LinkedHashMapAdapter;
import es.dc.a21l.base.modelo.encapsulacionSW.WeakHashMapAdapter;

@SuppressWarnings("restriction")
@XmlRootElement(name="atributosMapDto")
public class AtributosMapDto {
	
	private LinkedHashMap<String,ValorFDDto> contenido = new LinkedHashMap<String,ValorFDDto>();
	private WeakHashMap<String,ValorFDDto> contenidoTemporal = new WeakHashMap<String,ValorFDDto>();
	
	public void setValor(String columna ,ValorFDDto valores) {
		this.contenido.put(columna, valores);
	}

	@XmlJavaTypeAdapter(LinkedHashMapAdapter.class)
	public LinkedHashMap<String, ValorFDDto> getContenido() {
		return contenido;
	}

	public void setContenido(LinkedHashMap<String, ValorFDDto> contenido) {
		this.contenido = contenido;
	}
	
	public void setValorTemporal(String columna ,ValorFDDto valores) {
		this.contenidoTemporal.put(columna, valores);
	}
	
	@XmlJavaTypeAdapter(WeakHashMapAdapter.class)
	public WeakHashMap<String, ValorFDDto> getContenidoTemporal() {
		return contenidoTemporal;
	}
		
	public void setContenidoTemporal(WeakHashMap<String, ValorFDDto> contenido) {
		this.contenidoTemporal = contenido;
	}	
}