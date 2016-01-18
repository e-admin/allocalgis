/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;

@SuppressWarnings("restriction")
@XmlRootElement(name="descripcionAtributo")
public class DescripcionAtributoDto {
	private String nombre;
	private TipoAtributoFD tipo;
	private String definicion;
	private String nombreAcentos;
	
	@XmlAttribute(name="nombre")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
		this.nombreAcentos=quitarAcentos(nombre);
	}
	
	@XmlAttribute(name="tipo")
	public TipoAtributoFD getTipo() {
		return tipo;
	}
	public void setTipo(TipoAtributoFD tipo) {
		this.tipo = tipo;
	}
	
	@XmlAttribute(name="definicion")
	public String getDefinicion() {
		return definicion;
	}
	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}
	
	public String getNombreAcentos() {
		return nombreAcentos;
	}
	public void setNombreAcentos(String nombreAcentos) {
		this.nombreAcentos = nombreAcentos;
	}
	public static String quitarAcentos(String input) {
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
