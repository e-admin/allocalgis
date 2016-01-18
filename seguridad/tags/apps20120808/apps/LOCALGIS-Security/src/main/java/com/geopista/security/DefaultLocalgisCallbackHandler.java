package com.geopista.security;

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.eclipse.jetty.plus.jaas.callback.ObjectCallback;
import org.eclipse.jetty.util.security.Password;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

/**
 * Created by IntelliJ IDEA. User: angeles Date: 11-jun-2004 Time: 10:29:09
 */
public class DefaultLocalgisCallbackHandler extends
		AbstractLocalgisCallbackHandler {

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				((NameCallback) callbacks[i]).setName(getUserName());
			} else if (callbacks[i] instanceof ObjectCallback) {
				((ObjectCallback) callbacks[i]).setObject(getIdEntidad());
			} else if (callbacks[i] instanceof PasswordCallback) {
				if (getCredential() instanceof Password)
					((PasswordCallback) callbacks[i])
							.setPassword(((Password) getCredential())
									.toString().toCharArray());
				else if (getCredential() instanceof String) {
					((PasswordCallback) callbacks[i])
							.setPassword(((String) getCredential())
									.toCharArray());
				} else
					throw new UnsupportedCallbackException(
							callbacks[i],
							"User supplied credentials cannot be converted to char[] for PasswordCallback: try using an ObjectCallback instead");
			//-----NUEVO----->
			} else if (callbacks[i] instanceof CertificateCallback) {
				if (getCredential() instanceof X509Certificate)
					((CertificateCallback) callbacks[i])
							.setCertificate(((X509Certificate) getCredential()));				
				else
					throw new UnsupportedCallbackException(
							callbacks[i],
							"User supplied credentials cannot be converted to X509Certificate for CertificateCallback: try using an ObjectCallback instead");
			//---FIN NUEVO--->
			} else
				throw new UnsupportedCallbackException(callbacks[i]);
		}

	}

}
