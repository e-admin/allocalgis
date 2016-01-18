/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.dc.a21l.base.cu.impl.UtilidadesModelo;
import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;

@Entity
@Table(name = "Tb_A21l_AtributoFuenteDatos")
public class AtributoFuenteDatos extends EntidadBase{
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="tipoAtributo")
	private TipoAtributoFD tipoAtributo;
	
	@Column(name="definicion")
	private String definicion;
	
	@Column(name="esFormula")
	private Short esFormula = UtilidadesModelo.convertBooleanToShort(false);
	
	@Column(name="esRelacion")
	private Short esRelacion = UtilidadesModelo.convertBooleanToShort(false);
	
	@JoinColumn(name = "idtablafuentedatos", referencedColumnName = "Id", nullable = false)
    @ManyToOne
	private TablaFuenteDatos tabla;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="columna",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<Atributo> atributos= new HashSet<Atributo>();
		
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public TipoAtributoFD getTipoAtributo() {
		return tipoAtributo;
	}
	public void setTipoAtributo(TipoAtributoFD tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}
	
	public String getDefinicion() {
		return definicion;
	}
	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}
	
	public TablaFuenteDatos getTabla() {
		return tabla;
	}
	public void setTabla(TablaFuenteDatos tabla) {
		this.tabla = tabla;
	}
	
	public Boolean getEsRelacion() {
		return UtilidadesModelo.convertShortToBoolean(this.esFormula);
	}

	public void setEsRelacion(Boolean esRelacion) {
		this.esRelacion = UtilidadesModelo.convertBooleanToShort(esRelacion);
	}
	
	public Boolean getEsFormula() {
		return UtilidadesModelo.convertShortToBoolean(this.esFormula);
	}

	public void setEsFormula(Boolean esFormula) {
		this.esFormula = UtilidadesModelo.convertBooleanToShort(esFormula);
	}
	
	public Set<Atributo> getAtributos() {
		return atributos;
	}
	public void setAtributos(Set<Atributo> atributos) {
		this.atributos = atributos;
	}
}
