package es.satec.localgismobile.server.validation.vo;

import java.io.Serializable;

public class PermissionVO implements Serializable {

	private static final long serialVersionUID = -4003617322946337242L;

	private String layer;
	private int idPerm;
	
	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		this.layer = layer;
	}
	public int getIdPerm() {
		return idPerm;
	}
	public void setIdPerm(int idPerm) {
		this.idPerm = idPerm;
	}
	
	public String toString() {
		return "(" + layer + ", " + idPerm + ")";
	}
	public boolean equals(Object o) {
		if (o instanceof PermissionVO) {
			PermissionVO p = (PermissionVO) o;
			if (p.layer == null) {
				return layer == null && p.idPerm == idPerm;
			}
			else {
				return p.layer.equals(layer) && p.idPerm == idPerm;
			}
		}
		return false;
	}
}
