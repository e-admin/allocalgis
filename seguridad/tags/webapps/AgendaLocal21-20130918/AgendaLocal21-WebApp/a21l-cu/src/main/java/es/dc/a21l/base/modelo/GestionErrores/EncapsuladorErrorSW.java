/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.GestionErrores;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.modelo.GestionErrores.enumerados.FormErrorEmun;

@SuppressWarnings("restriction")
@XmlRootElement(name="encapsuladorErrorSW")
public class EncapsuladorErrorSW {
	
	private String atributo;
	private String cadenaCodigoError;
	
	public EncapsuladorErrorSW(){
		
	}
	public EncapsuladorErrorSW(FormErrorEmun errorEmun){
		this.atributo=errorEmun.getAtributo();
		this.cadenaCodigoError=errorEmun.getCadenaCodigoError();
	}
	
	
    @XmlAttribute(name="atributo")
	public String getAtributo() {
		return atributo;
	}
    
    @XmlAttribute(name="cadenaCodigoError")
	public String getCadenaCodigoError() {
		return cadenaCodigoError;
	}
	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
	public void setCadenaCodigoError(String cadenaCodigoError) {
		this.cadenaCodigoError = cadenaCodigoError;
	}	

}
