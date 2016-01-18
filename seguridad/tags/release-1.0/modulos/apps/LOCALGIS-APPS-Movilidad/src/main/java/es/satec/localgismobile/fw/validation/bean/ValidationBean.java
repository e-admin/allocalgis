package es.satec.localgismobile.fw.validation.bean;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Clase que representa los datos de validación a guardar para un usuario. El bean tiene que ser serializable.
 * @author cgarciar
 *
 */
public class ValidationBean implements Serializable{
	
	// Constantes asociadas a los identificadores de permisos para capas
	public static final int PERM_LAYER_READ = 871;
	public static final int PERM_LAYER_WRITE = 872;
	public static final int PERM_LAYER_ADD = 873;
	public static final int PERM_LAYER_EDIT_SLD = 874;
	
	private static final long serialVersionUID = -2259630890896592946L;

	//Estructura del fichero que se almacenará en disco
	/**
	 * login del usuario
	 */
	private String usuario;
	
	/**
	 * password usuario
	 */
	private String password;
	
	/**
	 * Identificador interno del usuario
	 */
	private String idUsuario;
	
	/**
	 * permisos por capa que posee el usuario
	 */
	private Hashtable permisos;
	
	/**
	 * id del dispositivo
	 */
	private String dispositivo;
	
	/**
	 * nombre del usuario validado
	 */
	private String nombre;
	
	/**
	 * apellidos del usuario validado
	 */
	private String apellidos;
	
	/**
	 * Booleano que indica si la última validación empleada ha sido con certificado o login y password
	 */
	private boolean certValidation;
	
	/**
	 * Campo que guardará un texto encriptado usando la clave privada de un DigitalCertificate
	 * y que se usará para comprobar una posible validación offline usando certificados
	 */
	private byte[] signedText;	
	
	public String getId_dispositivo() {
		return dispositivo;
	}
	public void setId_dispositivo(String id_dispositivo) {
		this.dispositivo = id_dispositivo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public Hashtable getPermisos() {
		return permisos;
	}
	
	public int[] getPermisosBySystemId(String systemId){
		//blindo un poco el temita ...
		int vacio [] = {};
		if ( (systemId == null) 
			 || (permisos.get(systemId) == null)) return vacio;
		return (int []) permisos.get(systemId);
	}
	
	public void setPermisos(Hashtable permisos) {
		this.permisos = permisos;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public byte[] getSignedText() {
		return signedText;
	}
	public void setSignedText(byte[] signedText) {
		this.signedText = signedText;
	}
	public boolean isCertValidation() {
		return certValidation;
	}
	public void setCertValidation(boolean certValidation) {
		this.certValidation = certValidation;
	}
	
	public String getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Usuario=" + usuario + ", id=" + idUsuario);
		if (permisos != null) {
			sb.append(", permisos=");
			Enumeration e = permisos.keys();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				sb.append(key+"=[");
				int[] permisosLayer = (int[]) permisos.get(key);
				for (int i=0; i<permisosLayer.length; i++) {
					sb.append(permisosLayer[i] + ",");
				}
				sb.append("]");
			}
		}
		return sb.toString();
	}
}
