package com.geopista.ui.plugin.georreferenciacionExterna;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class GeorreferenciacionExternaData {

		private String tipoDatos; 
	    private String tabla;
	    private String bbdd;	
	    private String nombreConsultaCapa;
	    private String descripcion;
		private String tipoGeometria;
	    private String tablaGeometria;
	    private String etiquetaSLD;
	    private String portal;
		private Hashtable tipo_elegido=new Hashtable();
	    private Hashtable campos_tipos=new Hashtable();
	    
	    private String usuario; 
	    private int id;
	 
	    public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getUsuario() {
			return usuario;
		}

		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}


		public String getTipoDatos() {

	        return tipoDatos;

	    }
	 
	    public void setTipoDatos(String tipoDatos) {

	        this.tipoDatos = tipoDatos;

	    }
	 
	    public String getTabla() {

	        return tabla;

	    }
	 
	    public void setTabla(String tabla) {

	        this.tabla = tabla;

	    }
	 
	    public String getBbdd() {

	        return bbdd;

	    }
	 
	    public void setBbdd(String bbdd) {

	        this.bbdd = bbdd;

	    }
	    
	    public Hashtable getTipoElegido() {

	        return tipo_elegido;

	    }
	    
	    public String getNombreConsultaCapa() {
			return nombreConsultaCapa;
		}

		public void setNombreConsultaCapa(String nombreConsultaCapa) {
			this.nombreConsultaCapa = nombreConsultaCapa;
		}
	 
	    public void setTipoElegido(Hashtable tipo_elegido) {

	    	Set valores = tipo_elegido.keySet();
			Iterator listaCampos = valores.iterator();

			while (listaCampos.hasNext()){
				String key = listaCampos.next().toString();
				this.tipo_elegido.put(key,tipo_elegido.get(key));
			}

	    }	    
	    
	    public Hashtable getCamposTipo() {

	        return campos_tipos;

	    }
	 
	    public void setCamposTipo(Hashtable campos_tipos) {

	    	Set valores = campos_tipos.keySet();
			Iterator listaCampos = valores.iterator();

			while (listaCampos.hasNext()){
				String key = listaCampos.next().toString();
				this.campos_tipos.put(key,campos_tipos.get(key));
			}
		}	    
	    
	    public String getTipoGeometria() {
			return tipoGeometria;
		}

		public void setTipoGeometria(String tipoGeometria) {
			this.tipoGeometria = tipoGeometria;
		}
		


		public String getTablaGeometria() {
			return tablaGeometria;
		}

		public void setTablaGeometria(String tablaGeometria) {
			this.tablaGeometria = tablaGeometria;
		}
		
		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public String getEtiquetaSLD() {
			return etiquetaSLD;
		}

		public void setEtiquetaSLD(String etiquetaSLD) {
			this.etiquetaSLD = etiquetaSLD;
		}
		
		public String getPortal() {
			return portal;
		}

		public void setPortal(String portal) {
			this.portal = portal;
		}


}
