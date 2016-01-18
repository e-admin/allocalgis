/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.criterio.modelo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.criterio.cu.TipoOperacionCriterioEmun;
import es.dc.a21l.criterio.cu.TipoOperandoCriterioEmun;
import es.dc.a21l.fuente.modelo.Atributo;
import es.dc.a21l.usuario.modelo.UsuarioRol;

@Entity
@Table(name="Tb_A21l_Criterio",uniqueConstraints=@UniqueConstraint(columnNames={"Id_A21l_Atributo"}))
public class Criterio extends EntidadBase {
	
	@Column(name="TipoOperandoIzq",nullable=false)
	private TipoOperandoCriterioEmun tipoOperandoIzq;
	
	@Column(name="TipoOperandoDch",nullable=false)
	private TipoOperandoCriterioEmun tipoOperandoDch;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Id_A21l_Criterio_Izq",nullable=true)
	private Criterio criterioIzq;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Id_A21l_Criterio_Dch",nullable=true)
	private Criterio criterioDch;
	
	
	@Column(name="LiteralIzq",nullable=true)
	private Double literalIzq;
	
	@Column(name="LiteralDch",nullable=true)
	private Double literalDch;
	
	@Column(name="TxtLiteralIzq",nullable=true)
	private String strLiteralIzq;
	
	@Column(name="TxtLiteralDch",nullable=true)
	private String strLiteralDch;
	
	@Column(name="TipoOperacion",nullable=false)
	private TipoOperacionCriterioEmun tipoOperacion;
	
	@OneToOne
	@JoinColumn(name="Id_A21l_Atributo",nullable=true)
	private Atributo atributo;

	@Column(name="CadenaCriterio",nullable=true)
	private String cadenaCriterio;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="criterioIzq",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<Criterio> criteriosIzq= new HashSet<Criterio>();
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="criterioDch",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<Criterio> criteriosDch= new HashSet<Criterio>();
	
	
	public TipoOperandoCriterioEmun getTipoOperandoIzq() {
		return tipoOperandoIzq;
	}

	public void setTipoOperandoIzq(TipoOperandoCriterioEmun tipoOperandoIzq) {
		this.tipoOperandoIzq = tipoOperandoIzq;
	}

	public TipoOperandoCriterioEmun getTipoOperandoDch() {
		return tipoOperandoDch;
	}

	public void setTipoOperandoDch(TipoOperandoCriterioEmun tipoOperandoDch) {
		this.tipoOperandoDch = tipoOperandoDch;
	}

	public Criterio getCriterioIzq() {
		return criterioIzq;
	}

	public void setCriterioIzq(Criterio criterioIzq) {
		this.criterioIzq = criterioIzq;
	}

	public Criterio getCriterioDch() {
		return criterioDch;
	}

	public void setCriterioDch(Criterio criterioDch) {
		this.criterioDch = criterioDch;
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
	
	public String getStrLiteralIzq() {
		return strLiteralIzq;
	}

	public void setStrLiteralIzq(String strLiteralIzq) {
		this.strLiteralIzq = strLiteralIzq;
	}

	public String getStrLiteralDch() {
		return strLiteralDch;
	}

	public void setStrLiteralDch(String strLiteralDch) {
		this.strLiteralDch = strLiteralDch;
	}
	
	public TipoOperacionCriterioEmun getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(TipoOperacionCriterioEmun tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Atributo getAtributo() {
		return atributo;
	}

	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}

	public String getCadenaCriterio() {
		return cadenaCriterio;
	}

	public void setCadenaCriterio(String cadenaCriterio) {
		this.cadenaCriterio = cadenaCriterio;
	}

	public Set<Criterio> getCriteriosIzq() {
		return criteriosIzq;
	}

	public void setCriteriosIzq(Set<Criterio> criteriosIzq) {
		this.criteriosIzq = criteriosIzq;
	}

	public Set<Criterio> getCriteriosDch() {
		return criteriosDch;
	}

	public void setCriteriosDch(Set<Criterio> criteriosDch) {
		this.criteriosDch = criteriosDch;
	}

}
