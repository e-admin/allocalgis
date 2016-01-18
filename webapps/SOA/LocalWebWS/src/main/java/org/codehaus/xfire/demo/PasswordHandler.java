/**
 * PasswordHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.codehaus.xfire.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

/**
 * <a href="mailto:tsztelak@gmail.com">Tomasz Sztelak</a>
 *
 */
public class PasswordHandler implements CallbackHandler {

	private Map passwords = new HashMap();

	public PasswordHandler() {
		passwords.put("serveralias", "aliaspass");
        passwords.put("client-344-839","client344Password");

	}

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		String id = pc.getIdentifer();
		pc.setPassword((String) passwords.get(id));

	}

}
