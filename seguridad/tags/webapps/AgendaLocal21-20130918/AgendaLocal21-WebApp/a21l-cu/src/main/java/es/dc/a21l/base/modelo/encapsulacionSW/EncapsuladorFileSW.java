/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.encapsulacionSW;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;

@SuppressWarnings("restriction")
@XmlRootElement(name="encapsuladorFileSW")
public class EncapsuladorFileSW {
	private byte[] fich;
	private long idFuente;
	private long idIndicador;
	private String nombre;
	private EncapsuladorErroresSW errores;
	
	public EncapsuladorFileSW(){
	}
	
	public EncapsuladorFileSW(String nombre, byte[] fich, long idFuente){
		this.nombre = nombre;
		this.fich = fich;
		this.idFuente = idFuente;
	}
	
	public EncapsuladorFileSW(long idIndicador,String nombre, byte[] fich ){
		this.nombre = nombre;
		this.fich = fich;
		this.idIndicador = idIndicador;
	}

	@XmlElement(name="fich")
	public byte[] getFich() {
		return fich;
	}

	public void setFich(byte[] fich) {
		this.fich = fich;
	}

	@XmlAttribute(name="idIndicador")
	public long getIdIndicador() {
		return idFuente;
	}

	public void setIdIndicador(long idIndicador) {
		this.idIndicador = idIndicador;
	}
	
	@XmlAttribute(name="idFuente")
	public long getIdFuente() {
		return idFuente;
	}

	public void setIdFuente(long idFuente) {
		this.idFuente = idFuente;
	}
	
	@XmlAttribute(name="nombre")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}

