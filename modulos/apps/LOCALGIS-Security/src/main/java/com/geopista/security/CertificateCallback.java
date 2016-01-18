/**
 * CertificateCallback.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security;

import java.security.cert.X509Certificate;

import javax.security.auth.callback.Callback;

public class CertificateCallback  implements Callback, java.io.Serializable{

	private static final long serialVersionUID = 7677026007207974609L;
	
	private X509Certificate _certificate;
	
	public CertificateCallback(){
		
	}
	
	public CertificateCallback(X509Certificate _certificate){
		this._certificate=_certificate;
	}
	
	public void setCertificate(X509Certificate _certificate){
		this._certificate=_certificate;
	}
	
	public X509Certificate getCertificate(){
		return this._certificate;
	}
	
}
