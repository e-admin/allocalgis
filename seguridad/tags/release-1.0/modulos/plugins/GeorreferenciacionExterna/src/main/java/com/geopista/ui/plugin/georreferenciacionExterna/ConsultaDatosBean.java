package com.geopista.ui.plugin.georreferenciacionExterna;

public class ConsultaDatosBean {

	private int id; 
	private String nombreConsulta;
	private String descripcion ;
	private String usuario;
	private String nombre_bbdd_ext;
	private String nombre_tabla_ext;
	private String metodo_georeferencia;
	private String tipo_geometria;
	private String tabla_cruce;
	private String campo_georeferencia;
	private String campos_mostrar;
	private String campo_etiqueta;
	private String filtro_operador;
	private String filtro_valor;
	private String portal;
	
	
	public String getPortal() {
		return portal;
	}
	public void setPortal(String portal) {
		this.portal = portal;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombreConsulta() {
		return nombreConsulta;
	}
	public void setNombreConsulta(String nombreConsulta) {
		this.nombreConsulta = nombreConsulta;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getNombre_bbdd_ext() {
		return nombre_bbdd_ext;
	}
	public void setNombre_bbdd_ext(String nombreBbddExt) {
		nombre_bbdd_ext = nombreBbddExt;
	}
	public String getNombre_tabla_ext() {
		return nombre_tabla_ext;
	}
	public void setNombre_tabla_ext(String nombreTablaExt) {
		nombre_tabla_ext = nombreTablaExt;
	}
	public String getMetodo_georeferencia() {
		return metodo_georeferencia;
	}
	public void setMetodo_georeferencia(String metodoGeoreferencia) {
		metodo_georeferencia = metodoGeoreferencia;
	}
	public String getTipo_geometria() {
		return tipo_geometria;
	}
	public void setTipo_geometria(String tipoGeometria) {
		tipo_geometria = tipoGeometria;
	}
	public String getTabla_cruce() {
		return tabla_cruce;
	}
	public void setTabla_cruce(String tablaCruce) {
		tabla_cruce = tablaCruce;
	}
	public String getCampo_georeferencia() {
		return campo_georeferencia;
	}
	public void setCampo_georeferencia(String campoGeoreferencia) {
		campo_georeferencia = campoGeoreferencia;
	}
	public String getCampos_mostrar() {
		return campos_mostrar;
	}
	public void setCampos_mostrar(String camposMostrar) {
		campos_mostrar = camposMostrar;
	}
	public String getCampo_etiqueta() {
		return campo_etiqueta;
	}
	public void setCampo_etiqueta(String campoEtiqueta) {
		campo_etiqueta = campoEtiqueta;
	}
	public String getFiltro_operador() {
		return filtro_operador;
	}
	public void setFiltro_operador(String filtroOperador) {
		filtro_operador = filtroOperador;
	}
	public String getFiltro_valor() {
		return filtro_valor;
	}
	public void setFiltro_valor(String filtroValor) {
		filtro_valor = filtroValor;
	}

	
	
	

}
