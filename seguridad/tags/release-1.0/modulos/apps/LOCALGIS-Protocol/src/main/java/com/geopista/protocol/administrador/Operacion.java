package com.geopista.protocol.administrador;

import java.util.Date;

public class Operacion implements java.io.Serializable, Cloneable {
	
	private static final long serialVersionUID = -3880516422758986534L;

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Operacion.class);
	
	public static final String TIPO_TABLA = "TABLA";
	public static final String TIPO_CAPA = "CAPA";
	
	public static final String ACCION_INSERTAR = "INSERTAR";
	public static final String ACCION_ACTUALIZAR = "ACTUALIZAR";
	public static final String ACCION_ELIMINAR = "ELIMINAR";

	private String tipoOperacion;
	private String municipio;
	private Date fechaOperacion;
	private int accion;
	private String idFeature;
	private int revision;
	private String locale;	
	private String idConexion;
	private String idUsuario;
	private String capa;

	public Operacion(){}
	
	public Operacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
   
	public Operacion(String municipio, String capa, String idFeature, Date fecha, int accion) {
	        this.municipio = municipio;
	    	this.capa = capa;
	        this.fechaOperacion = fecha;
	        this.accion = accion;
	        this.idFeature = idFeature;
	    }
	    
	    public String getTipoOperacion() {
	        return tipoOperacion;
	    }

	    public void setTipoOperacion(String tipoOperacion) {
	        this.tipoOperacion = tipoOperacion;
	    }
	    
	    public String getMunicipio() {
	        return municipio;
	    }

	    public void setMunicipio(String municipio) {
	        this.municipio = municipio;
	    }

	    public String getCapaAfectada() {
	        return capa;
	    }

	    public void setCapaAfectada(String capa) {
	        this.capa = capa;
	    }

	    public Date getFechaOperacion() {
	        return fechaOperacion;
	    }

	    public void setFechaOperacion(Date fechaOperacion) {
	        this.fechaOperacion = fechaOperacion;
	    }

	    public String getIdFeature() {
	        return idFeature;
	    }

	    public void setIdFeature (String idFeature) {
	        this.idFeature = idFeature;
	    }

	    public int getAccion() {
	    	return accion;
	    }

	    public void setRevision(int rev) {
	        this.revision = rev;
	    }
	    
	    public int getRevision() {
	    	return revision;
	    }

	    public void setAccion(int accion) {
	        this.accion = accion;
	    }
	    
	    public String getIdConexion() {
	        return idConexion;
	    }

	    public void setIdConexion (String idConexion) {
	        this.idConexion = idConexion;
	    }
	    
	    public String getIdUsuario() {
	        return idUsuario;
	    }

	    public void setIdUsuario (String idUsuario) {
	        this.idUsuario = idUsuario;
	    }
	    public String getLocale() {
			return locale;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}
	    
	    public String getOperacionRealizada(){
	    	switch(accion) {
	    		case 1: return ACCION_INSERTAR;
	    		case 2: return ACCION_ACTUALIZAR;
	    		case 3: return ACCION_ELIMINAR;
		    	default: return "";
	    	}
	    	
	    }
	    
	    public void setOperacionRealizada(String nombreOperacion){
	    	if(nombreOperacion.equals(ACCION_INSERTAR)) 
	    		this.accion = 1;
	    	else if(nombreOperacion.equals(ACCION_ACTUALIZAR))	
	    		this.accion = 2;
	    	else if(nombreOperacion.equals(ACCION_ELIMINAR))
	    		this.accion = 3;
	    }	
	    
	    
	   
		
	    public Object clone() {
			Operacion obj = null;
			try {
				obj = (Operacion) super.clone();
				//obj.setFechaOperacion(new Date(this.getFechaOperacion().getTime()));
			} 
			catch (CloneNotSupportedException ex) {
				logger.error("Error al clonar el objeto Operacion: "+ex.toString());
			}
			return obj;
		}
}
