/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.dc.a21l.base.cu.impl.UtilidadesModelo;
import es.dc.a21l.fuente.modelo.TablaFuenteDatos;
import es.dc.a21l.historico.modelo.Historico;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaBarras;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaSectores;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionMapa;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionTabla;


@Entity
@Table(name="Tb_A21l_Indicador")
@PrimaryKeyJoinColumn(name="Id_A21l_ElementoJerarquia")
public class Indicador extends ElementoJerarquia {
	
	@Column(name="Publico",nullable=false)
	private Short publico = UtilidadesModelo.convertBooleanToShort(true);
	
	@Column(name="PteAprobacionPublico",nullable=false)
	private Short pteAprobacionPublico = UtilidadesModelo.convertBooleanToShort(true);
	
	@Column(name="PublicadoEnWeb",nullable=false)
	private Short publicadoEnWeb = UtilidadesModelo.convertBooleanToShort(true);
	
	@Column(name="PteAprobacion",nullable=false)
	private Short pteAprobacion = UtilidadesModelo.convertBooleanToShort(true);
	
	@Column(name="numUsuariosPublicacion")
	private Integer numUsuariosPublicacion;
	
	@Column(name="loginUltimaPeticion",nullable=true)
	private String loginUltimaPeticion;
	
	@ManyToOne
	@JoinColumn(name="Id_A21l_Categoria",nullable=true)
	private Categoria categoria;
	
	@Column(name="loginCreador",nullable=false)
	private String loginCreador;
	
	@Column(name="minx",nullable=true)
	private Double minx;
	
	@Column(name="miny",nullable=true)
	private Double miny;
	
	@Column(name="maxx",nullable=true)
	private Double maxx;
	
	@Column(name="maxy",nullable=true)
	private Double maxy;
		
	//borrado en cascada
	
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="indicador",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<TablaFuenteDatos> tablaFuenteDatos= new HashSet<TablaFuenteDatos>();
    
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="indicador",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<EstiloVisualizacionTabla> estiloVisualizacionTablas= new HashSet<EstiloVisualizacionTabla>();
    
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="indicador",orphanRemoval=true)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<EstiloVisualizacionDiagramaBarras> estiloVisualizacionDiagramaBarras= new HashSet<EstiloVisualizacionDiagramaBarras>();
    
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="indicador",orphanRemoval=true)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<EstiloVisualizacionDiagramaSectores> estiloVisualizacionDiagramaSectores = new HashSet<EstiloVisualizacionDiagramaSectores>();
    
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="indicador",orphanRemoval=true)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<EstiloVisualizacionMapa> estiloVisualizacionMapa = new HashSet<EstiloVisualizacionMapa>();
    
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="indicador",orphanRemoval=true)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<IndicadorUsuario> indicadorUsuarios= new HashSet<IndicadorUsuario>();
    
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="indicador",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<Historico> historicos= new HashSet<Historico>();

	public Set<IndicadorUsuario> getIndicadorUsuarios() {
		return indicadorUsuarios;
	}

	public void setIndicadorUsuarios(Set<IndicadorUsuario> indicadorUsuarios) {
		this.indicadorUsuarios = indicadorUsuarios;
	}

	public Set<EstiloVisualizacionTabla> getEstiloVisualizacionTablas() {
		return estiloVisualizacionTablas;
	}

	public void setEstiloVisualizacionTablas(Set<EstiloVisualizacionTabla> estiloVisualizacionTablas) {
		this.estiloVisualizacionTablas = estiloVisualizacionTablas;
	}

	public Set<EstiloVisualizacionDiagramaBarras> getEstiloVisualizacionDiagramaBarras() {
		return estiloVisualizacionDiagramaBarras;
	}

	public void setEstiloVisualizacionDiagramaBarras(Set<EstiloVisualizacionDiagramaBarras> estiloVisualizacionDiagramaBarras) {
		this.estiloVisualizacionDiagramaBarras = estiloVisualizacionDiagramaBarras;
	}
	
	public Set<EstiloVisualizacionDiagramaSectores> getEstiloVisualizacionDiagramaSectores() {
		return estiloVisualizacionDiagramaSectores;
	}

	public void setEstiloVisualizacionDiagramaSectores(
			Set<EstiloVisualizacionDiagramaSectores> estiloVisualizacionDiagramaSectores) {
		this.estiloVisualizacionDiagramaSectores = estiloVisualizacionDiagramaSectores;
	}

	public Set<EstiloVisualizacionMapa> getEstiloVisualizacionMapa() {
		return estiloVisualizacionMapa;
	}

	public void setEstiloVisualizacionMapa(Set<EstiloVisualizacionMapa> estiloVisualizacionMapa) {
		this.estiloVisualizacionMapa = estiloVisualizacionMapa;
	}	
	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Boolean getPublico() {
		return UtilidadesModelo.convertShortToBoolean(publico);
	}

	public void setPublico(Boolean publico) {
		this.publico = UtilidadesModelo.convertBooleanToShort(publico);
	}

	public Boolean isPteAprobacionPublico() {
		return UtilidadesModelo.convertShortToBoolean(pteAprobacionPublico);
	}

	public Boolean getPteAprobacionPublico() {
		return UtilidadesModelo.convertShortToBoolean(pteAprobacionPublico);
	}
	
	public void setPteAprobacionPublico(Boolean pteAprobacionPublico) {
		this.pteAprobacionPublico = UtilidadesModelo.convertBooleanToShort(pteAprobacionPublico);
	}
	
	public Boolean getPublicadoEnWeb() {
		return UtilidadesModelo.convertShortToBoolean(publicadoEnWeb);
	}

	public void setPublicadoEnWeb(Boolean publicadoEnWeb) {
		this.publicadoEnWeb = UtilidadesModelo.convertBooleanToShort(publicadoEnWeb);
	}

	public Boolean isPteAprobacion() {
		return UtilidadesModelo.convertShortToBoolean(pteAprobacion);
	}

	public Boolean getPteAprobacion() {
		return UtilidadesModelo.convertShortToBoolean(pteAprobacion);
	}
	
	public void setPteAprobacion(Boolean pteAprobacion) {
		this.pteAprobacion = UtilidadesModelo.convertBooleanToShort(pteAprobacion);
	}

	public Set<TablaFuenteDatos> getTablaFuenteDatos() {
		return tablaFuenteDatos;
	}

	public void setTablaFuenteDatos(Set<TablaFuenteDatos> tablaFuenteDatos) {
		this.tablaFuenteDatos = tablaFuenteDatos;
	}

	public String getLoginCreador() {
		return loginCreador;
	}

	public void setLoginCreador(String loginCreador) {
		this.loginCreador = loginCreador;
	}

	public Set<Historico> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(Set<Historico> historicos) {
		this.historicos = historicos;
	}
	
	public Integer getNumUsuariosPublicacion() {
		return numUsuariosPublicacion;
	}

	public void setNumUsuariosPublicacion(Integer numUsuariosPublicacion) {
		this.numUsuariosPublicacion = numUsuariosPublicacion;
	}

	public String getLoginUltimaPeticion() {
		return loginUltimaPeticion;
	}

	public void setLoginUltimaPeticion(String loginUltimaPeticion) {
		this.loginUltimaPeticion = loginUltimaPeticion;
	}
		
	public Double getMinx() {
		return minx;
	}

	public void setMinx(Double minx) {
		this.minx = minx;
	}

	public Double getMiny() {
		return miny;
	}

	public void setMiny(Double miny) {
		this.miny = miny;
	}

	public Double getMaxx() {
		return maxx;
	}

	public void setMaxx(Double maxx) {
		this.maxx = maxx;
	}

	public Double getMaxy() {
		return maxy;
	}

	public void setMaxy(Double maxy) {
		this.maxy = maxy;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Indicador)
			return this.getId()==((Indicador)obj).getId();
		return super.equals(obj);
	}		
}