/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.GestionErrores;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.modelo.GestionErrores.enumerados.FormErrorEmun;

@SuppressWarnings("restriction")
@XmlRootElement(name="encapsuladorErrorSW")
public class EncapsuladorErroresSW {

	private Boolean hashErrors;
	private List<EncapsuladorErrorSW> listaErrores;
	
	public EncapsuladorErroresSW(){
		listaErrores= new ArrayList<EncapsuladorErrorSW>();
		hashErrors=false;
	}
	
	@XmlAttribute(name="hashErrors")
	public Boolean getHashErrors() {
		return hashErrors;
	}
	
	public void setHashErrors(Boolean hashErrors) {
		this.hashErrors = hashErrors;
	}
	@XmlElement(name="encapsuladorErrorSW")
	public List<EncapsuladorErrorSW> getListaErrores() {
		return listaErrores;
	}
	public void setListaErrores(List<EncapsuladorErrorSW> listaErrores) {
		this.listaErrores = listaErrores;
	}
	
	
	public void addError(FormErrorEmun formErrorEmun){
		listaErrores.add(new EncapsuladorErrorSW(formErrorEmun));
		hashErrors=true;
	}
	
	
	
}
