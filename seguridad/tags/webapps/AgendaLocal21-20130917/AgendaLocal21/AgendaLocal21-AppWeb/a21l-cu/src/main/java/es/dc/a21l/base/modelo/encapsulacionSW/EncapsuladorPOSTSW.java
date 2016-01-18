/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.encapsulacionSW;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErrorSW;
import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.GestionErrores.enumerados.FormErrorEmun;

@SuppressWarnings("restriction")
@XmlRootElement(name="encapsuladorPOSTSW")
public class EncapsuladorPOSTSW<T> {
	
	private T objetoEncapsulado;
	private EncapsuladorErroresSW errores;
	
	public EncapsuladorPOSTSW(){
		
	}
	
	public EncapsuladorPOSTSW(T objetoEncapsulado,EncapsuladorErroresSW errores){
		this.objetoEncapsulado=objetoEncapsulado;
		this.errores= errores;
	}

	@XmlElement(name="objetoEncapsulado")
	public T getObjetoEncapsulado() {
		return objetoEncapsulado;
	}

	public void setObjetoEncapsulado(T objetoEncapsulado) {
		this.objetoEncapsulado = objetoEncapsulado;
	}

	@XmlElement(name="errores")
	public EncapsuladorErroresSW getErrores() {
		return errores;
	}

	public void setErrores(EncapsuladorErroresSW errores) {
		this.errores = errores;
	}
	
	public void addError(FormErrorEmun formErrorEmun){
		errores.addError(formErrorEmun);
	}
	
	public Boolean hashErrors (){
		return errores.getHashErrors();
	}
	
	public List<EncapsuladorErrorSW> getListaDeErrores(){
		return errores.getListaErrores();
	}

}
