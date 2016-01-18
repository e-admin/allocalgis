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
