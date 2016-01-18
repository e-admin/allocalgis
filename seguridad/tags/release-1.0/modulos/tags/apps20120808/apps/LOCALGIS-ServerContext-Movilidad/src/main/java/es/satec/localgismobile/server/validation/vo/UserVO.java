package es.satec.localgismobile.server.validation.vo;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class UserVO implements Serializable {

	private static final long serialVersionUID = 1645L;

	private int id;
	private String login;
	private String password;
	private String completeName;
	private String mail;
	private HashMap permissionsByLayer;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public HashMap getPermissionsByLayer() {
		return permissionsByLayer;
	}

	public void setPermissions(HashMap permissionsByLayer) {
		this.permissionsByLayer = permissionsByLayer;
	}

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("UserVO");
		sb.append("{id='").append(id).append('\'');
		sb.append(", login='").append(login).append('\'');
		sb.append(", password='").append(password).append('\'');
		sb.append(", mail=").append(mail);
		sb.append(", name='").append(completeName).append('\'');
		sb.append(", permissions={");
		if (permissionsByLayer != null) {
			
			Iterator itKeys = permissionsByLayer.keySet().iterator();
			while (itKeys.hasNext()) {
				String layer = (String) itKeys.next();
				sb.append(layer + ": [");
				Collection perms = (Collection) permissionsByLayer.get(layer);
				if (perms != null) {
					Iterator itPerms = perms.iterator();
					while (itPerms.hasNext()) {
						Integer perm = (Integer) itPerms.next();
						sb.append(perm + " ");	
					}
				}
				sb.append("]");
			}
		}
		sb.append('}');
		return sb.toString();
	}
}
