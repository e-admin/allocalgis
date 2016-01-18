package es.dc.a21l.base.utils.enumerados;

public enum TipoModificacionEnum {
	CREACION("CREAR"),
	MODIFICACION("MODIFICAR"),
	BORRADO("BORRAR");
	
	private String tipoModificacion;

	private TipoModificacionEnum(String tipoModificacion) {
		this.tipoModificacion = tipoModificacion;
	}

	public String getTipoModificacion() {
		return tipoModificacion;
	}

	public void setTipoModificacion(String tipoModificacion) {
		this.tipoModificacion = tipoModificacion;
	}
	

}
