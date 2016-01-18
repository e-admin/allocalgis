/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.publicacion.modelo;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import es.dc.a21l.base.cu.impl.UtilidadesModelo;
import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;

@SuppressWarnings("serial")
@Entity
@Table(name="Tb_A21l_Configuracion_Sistema")
public class Publicacion extends EntidadBase implements Serializable {

	@Column(name="publicacionWebHabilitada",nullable=false)
    private Short publicacionWebHabilitada = UtilidadesModelo.convertBooleanToShort(false);

	public Short getPublicacionWebHabilitada() {
		return publicacionWebHabilitada;
	}

	public void setPublicacionWebHabilitada(Short publicacionWebHabilitada) {
		this.publicacionWebHabilitada = publicacionWebHabilitada;
	}	
}
