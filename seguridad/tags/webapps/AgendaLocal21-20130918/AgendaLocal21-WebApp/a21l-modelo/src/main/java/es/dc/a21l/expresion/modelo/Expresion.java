/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.expresion.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.expresion.cu.TipoOperacionEmun;
import es.dc.a21l.expresion.cu.TipoOperandoEmun;
import es.dc.a21l.fuente.modelo.AtributoFuenteDatos;

@Entity
@Table(name="Tb_A21l_Expresion")
public class Expresion extends EntidadBase {
	
	@Column(name="TipoOperandoIzq",nullable=false)
	private TipoOperandoEmun tipoOperandoIzq;
	
	@Column(name="TipoOperandoDch",nullable=false)
	private TipoOperandoEmun tipoOperandoDch;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Id_A21l_Expresion_Izq",nullable=true)
	private Expresion expresionIzq;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Id_A21l_Expresion_Dch",nullable=true)
	private Expresion expresionDch;
	
	@Column(name="LiteralIzq",nullable=true)
	private Double literalIzq;
	
	@Column(name="LiteralDch",nullable=true)
	private Double literalDch;
	
	@ManyToOne
	@JoinColumn(name="Id_A21l_AtributoFuenteDatos_Izq",nullable=true)
	private AtributoFuenteDatos atributoFuenteDatosIzq;
	
	@ManyToOne
	@JoinColumn(name="Id_A21l_AtributoFuenteDatos_Dch",nullable=true)
	private AtributoFuenteDatos atributoFuenteDatosDch;
	
	@Column(name="TipoOperacion",nullable=false)
	private TipoOperacionEmun tipoOperacion;
	
//	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="expresionIzq",orphanRemoval=true)
//	@OnDelete(action=OnDeleteAction.CASCADE)
//	private Set<Expresion> exprsionesIzq= new HashSet<Expresion>();
//	
//	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="expresionDch",orphanRemoval=true)
//	@OnDelete(action=OnDeleteAction.CASCADE)
//	private Set<Expresion> exprsionesDch= new HashSet<Expresion>();

	public TipoOperandoEmun getTipoOperandoIzq() {
		return tipoOperandoIzq;
	}

	public void setTipoOperandoIzq(TipoOperandoEmun tipoOperandoIzq) {
		this.tipoOperandoIzq = tipoOperandoIzq;
	}

	public TipoOperandoEmun getTipoOperandoDch() {
		return tipoOperandoDch;
	}

	public void setTipoOperandoDch(TipoOperandoEmun tipoOperandoDch) {
		this.tipoOperandoDch = tipoOperandoDch;
	}

	public Expresion getExpresionIzq() {
		return expresionIzq;
	}

	public void setExpresionIzq(Expresion expresionIzq) {
		this.expresionIzq = expresionIzq;
	}

	public Expresion getExpresionDch() {
		return expresionDch;
	}

	public void setExpresionDch(Expresion expresionDch) {
		this.expresionDch = expresionDch;
	}

	public Double getLiteralIzq() {
		return literalIzq;
	}

	public void setLiteralIzq(Double literalIzq) {
		this.literalIzq = literalIzq;
	}

	public Double getLiteralDch() {
		return literalDch;
	}

	public void setLiteralDch(Double literalDch) {
		this.literalDch = literalDch;
	}

	public AtributoFuenteDatos getAtributoFuenteDatosIzq() {
		return atributoFuenteDatosIzq;
	}

	public void setAtributoFuenteDatosIzq(AtributoFuenteDatos atributoFuenteDatosIzq) {
		this.atributoFuenteDatosIzq = atributoFuenteDatosIzq;
	}

	public AtributoFuenteDatos getAtributoFuenteDatosDch() {
		return atributoFuenteDatosDch;
	}

	public void setAtributoFuenteDatosDch(AtributoFuenteDatos atributoFuenteDatosDch) {
		this.atributoFuenteDatosDch = atributoFuenteDatosDch;
	}

	public TipoOperacionEmun getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(TipoOperacionEmun tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

//	public Set<Expresion> getExprsionesIzq() {
//		return exprsionesIzq;
//	}
//
//	public void setExprsionesIzq(Set<Expresion> exprsionesIzq) {
//		this.exprsionesIzq = exprsionesIzq;
//	}
//
//	public Set<Expresion> getExprsionesDch() {
//		return exprsionesDch;
//	}
//
//	public void setExprsionesDch(Set<Expresion> exprsionesDch) {
//		this.exprsionesDch = exprsionesDch;
//	}
	
}
