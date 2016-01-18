/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.encapsulacionSW;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;

@SuppressWarnings("restriction")
@XmlRootElement(name="encapsuladorStringSW")
public class EncapsuladorStringSW implements Comparable<EncapsuladorStringSW> {
	private String texto;
	
	public EncapsuladorStringSW(){
	}
	
	public EncapsuladorStringSW(String texto){
		this.texto = texto;
	}

	@XmlAttribute(name="texto")
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int compareTo(EncapsuladorStringSW o) {
		if ( o.getTexto().equals(this.texto))
			return 1;
		else return -1;
	}	
}
