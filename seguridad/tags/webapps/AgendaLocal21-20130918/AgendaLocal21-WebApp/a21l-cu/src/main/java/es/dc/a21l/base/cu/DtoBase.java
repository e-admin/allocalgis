/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@SuppressWarnings("restriction")
@XmlRootElement(name="dtoBase")
public class DtoBase {


	private long id;
	private ResultadosOperaciones resultadoOperacion; 

    @XmlAttribute(name="id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public boolean isNuevo() {
		return id == 0;
		
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@XmlAttribute(name="resultadoOperacion")
	public ResultadosOperaciones getResultadoOperacion() {
		return resultadoOperacion;
	}
	public void setResultadoOperacion(ResultadosOperaciones resultadoOperacion) {
		this.resultadoOperacion = resultadoOperacion;
	}
	public static String arreglaAcentos(String input) {
	    // Cadena de caracteres original a sustituir.
	    String []original = {"á","é","í","ó","ú","Á","É","Í","Ó","Ú","ñ","Ñ"};
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String [] ascii = {"&aacute;","&eacute;","&iacute;","&oacute;","&uacute;","&Aacute;","&Eacute;","&Iacute;","&Oacute;","&Uacute;","&ntilde;","&Ntilde;"};
	    String output = input;
	    for (int i=0; i<original.length; i++) {
	        // Reemplazamos los caracteres especiales.
	       output = output.replace(original[i], ascii[i]);
	    }//for i
	    return output;
	}
}
