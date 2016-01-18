/**
 * UserVO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
