 package org.eclipse.jetty.security.authentication;
 
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Principal;
import java.security.cert.CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.eclipse.jetty.security.ServerAuthException;
 import org.eclipse.jetty.security.UserAuthentication;
 import org.eclipse.jetty.server.Authentication;
 import org.eclipse.jetty.server.UserIdentity;
 import org.eclipse.jetty.util.security.CertificateUtils;
import org.eclipse.jetty.util.security.CertificateValidator;
import org.eclipse.jetty.util.security.Password;
 
 
 public class ClientCertDNIeFNMTAuthenticator extends ClientCertAuthenticator
 {  
	 
	 private transient Password _trustStorePassword;

	   public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory)
	     throws ServerAuthException
	   {
	     if (!mandatory) {
	       return this._deferred;
	     }
	     HttpServletRequest request = (HttpServletRequest)req;
	     HttpServletResponse response = (HttpServletResponse)res;
	     X509Certificate[] certs = (X509Certificate[])(X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
	     try
	     {
	       if ((certs != null) && (certs.length > 0))
	       {
	         if (this.isValidateCerts())
	         {
	           KeyStore trustStore = getKeyStore(null, getTrustStore(), getTrustStoreType(), getTrustStoreProvider(), _trustStorePassword == null ? null : _trustStorePassword.toString());
	 
	           Collection crls = loadCRL(getCrlPath());
	           CertificateValidator validator = new CertificateValidator(trustStore, crls);
	           validator.validate(certs);
	         }
	 
	         for (X509Certificate cert : certs)
	         {
	           if (cert == null) {
	             continue;
	           }
	           Principal principal = cert.getSubjectDN();
	           if (principal == null) principal = cert.getIssuerDN();
	           String username = principal == null ? "clientcert" : principal.getName();
	  
	           UserIdentity user = this._loginService.login(username, new X509Certificate[]{cert});
	           if (user == null)
	             continue;
	           renewSessionOnAuthentication(request, response);
	           return new UserAuthentication(getAuthMethod(), user);
	         }
	 
	       }
	 
	       if (!this._deferred.isDeferred(response))
	       {
	         response.sendError(403);
	         return Authentication.SEND_FAILURE;
	       }
	 
	       return Authentication.UNAUTHENTICATED;
	     }
	     catch (Exception e) {
	    
	     throw new ServerAuthException(e.getMessage());
	}
	   }
	 
	   protected KeyStore getKeyStore(InputStream storeStream, String storePath, String storeType, String storeProvider, String storePassword)
	     throws Exception
	   {
	     return CertificateUtils.getKeyStore(storeStream, storePath, storeType, storeProvider, storePassword);
	   }
	 
	   protected Collection<? extends CRL> loadCRL(String crlPath)
	     throws Exception
	   {
	     return CertificateUtils.loadCRL(crlPath);
	   }
	 
	   public void setTrustStorePassword(String password)
	   {
	     this._trustStorePassword = Password.getPassword("org.eclipse.jetty.ssl.password", password, null);
	   }
   
 }
