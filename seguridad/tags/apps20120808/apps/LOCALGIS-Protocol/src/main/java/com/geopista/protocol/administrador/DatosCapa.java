package com.geopista.protocol.administrador;

public class DatosCapa implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 506106939850148891L;
	private Integer idTabla;
	private String nombreTabla;
	private Integer idCapa;
	private String nombreCapa;
	private String traduccionCapa;
	private int versionable;
	
	
	public DatosCapa() {}
	
	
	public DatosCapa(String nombreCapa) {
		super();
		this.nombreCapa = nombreCapa;
	}



	public Integer getIdTabla() {
		return idTabla;
	}
	public void setIdTabla(Integer idTabla) {
		this.idTabla = idTabla;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
	public Integer getIdCapa() {
		return idCapa;
	}
	public void setIdCapa(Integer idCapa) {
		this.idCapa = idCapa;
	}
	public String getNombreCapa() {
		return nombreCapa;
	}
	public void setNombreCapa(String nombreCapa) {
		this.nombreCapa = nombreCapa;
	}
	public String getTraduccionCapa() {
		return traduccionCapa;
	}
	public void setTraduccionCapa(String traduccionCapa) {
		this.traduccionCapa = traduccionCapa;
	}
	public int getVersionable() {
		return versionable;
	}
	public void setVersionable(int versionable) {
		this.versionable = versionable;
	}
	
	public Object clone() {
		DatosCapa obj = null;
		try {
			obj = (DatosCapa) super.clone();
		} 
		catch (CloneNotSupportedException ex) {
			System.out.println("Error al clonar el objeto DatosCapa: "+ex.toString());
		}
		return obj;
	}
	
}
