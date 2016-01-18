/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import es.dc.a21l.base.modelo.EntidadBase;

@Entity
@Table(name = "tb_a21l_caracter_separador")
public class CaracterSeparador extends EntidadBase {
	@Column(name = "caracter")
	private String caracter;

	public String getCaracter() {
		return caracter;
	}

	public void setCaracter(String caracter) {
		this.caracter = caracter;
	}
	
	
}
