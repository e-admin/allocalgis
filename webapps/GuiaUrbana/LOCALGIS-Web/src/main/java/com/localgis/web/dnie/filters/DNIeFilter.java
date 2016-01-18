/**
 * DNIeFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.dnie.filters;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.security.dnie.CertificateAuthManager;
import com.localgis.web.core.security.dnie.afirma.AFirmaException;
import com.localgis.web.core.security.dnie.afirma.AFirmaManager;
import com.localgis.web.core.security.dnie.afirma.DatosValidacionCertificadoOut;
import com.localgis.web.core.utils.Base64Helper;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;

public class DNIeFilter implements Filter {

	private static final String REQUEST_CERTIFICATE_ATTRIBUTE = "javax.servlet.request.X509Certificate";
	private static final String LOGIN_ATTRIBUTE = "LoginAttribute";
	private static final String LOGIN_ROL = "Geopista.Visualizador.Login";
	private static final String PROP_AFIRMA_ACTIVE = "afirma.active";   
	
	private static Log log = LogFactory.getLog(DNIeFilter.class);

	private String finalURL;
	
	public void destroy() {
		
	}
	
	//private String loginAction;

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		// loginAction = config.getInitParameter("loginAction");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 HttpServletRequest httpServletRequest = (HttpServletRequest)request;
	     HttpSession session = httpServletRequest.getSession();
	     HttpServletResponse httpServletResponse = (HttpServletResponse) response;
	     
	     Integer httpPort = (Integer) session.getAttribute(CertificateAuthManager.HTTP_CONNECTION_PORT);
     	 String baseURL =  "http://" + request.getServerName() + ":" + httpPort;
     	 finalURL = baseURL + "/localgis-guiaurbana/private/";

     	log.info("Final URL:"+finalURL);
		if (httpServletRequest.getSession().getAttribute(LOGIN_ATTRIBUTE)==null) {
			log.info("El usuario no está autenticado");
			X509Certificate[] certificados = (X509Certificate[]) request.getAttribute(REQUEST_CERTIFICATE_ATTRIBUTE);
			try {
				if (isValidNonRevoke(certificados, request, response)) {
					String certNIF = CertificateAuthManager.getNIFfromSubjectDN(certificados[0].getSubjectX500Principal().getName());
					System.out.println("Certificado NIF:"+certNIF);
					//log.error(certNIF);
				
					LocalgisUtilsManager localgisUtilsManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisUtilsManager();
			        Integer idEntidad = localgisUtilsManager.isValidUserEntityNotPass(certNIF, LOGIN_ROL);
			        System.out.println("Identificador de Entidad para el usuario:"+certNIF+":"+idEntidad);
			        if (idEntidad>-1) {
//						httpServletRequest.getSession().setAttribute(LOGIN_ATTRIBUTE,datosCert.getCertificado().getNIFResponsable());
			        	session.setAttribute(LOGIN_ATTRIBUTE,certNIF);
			        	//request.getRequestDispatcher(loginAction).forward(request, response);
			        	
			        	//request.getRequestDispatcher("/private/selectEntidad.do").forward(request, response);
			        	finalURL = baseURL + "/localgis-guiaurbana/private/selectEntidad.do"; //adquirida al pulsar el botón DNIe o al entra en /private (filtro previo)
			        	if(idEntidad>0){
			        		httpServletRequest.getSession().setAttribute("idEntidad", idEntidad);
		                	//return mapping.findForward(SUCCESS_PAGE_ENTITY);			        		
			        	//	request.getRequestDispatcher("/private/selectMap.do").forward(request, response);
			        		finalURL = baseURL + "/localgis-guiaurbana/private/selectMap.do?idEntidad=" + idEntidad;
		                }
			        }
				}		
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
				finalURL = baseURL + "/localgis-guiaurbana/error500.jsp";
			 	httpServletResponse.sendRedirect(finalURL);
			 	return;
			}
		}
		System.out.println("Final URL:"+finalURL);
		httpServletResponse.sendRedirect(finalURL);
	}
	
	public boolean isValidNonRevoke(X509Certificate[] certificados, ServletRequest request, ServletResponse response) throws ServletException, IOException, LocalgisConfigurationException {
		if(isAfirmaActive()){			
			AFirmaManager aFirmaManager = new AFirmaManager();
			String certificadoBase64;
			try {
				certificadoBase64 = Base64Helper.getInstance().encodeBytes(
						certificados[0].getEncoded());
				DatosValidacionCertificadoOut datosCert = aFirmaManager
						.validarCertificado(certificadoBase64);
				if (datosCert.certificadoCorrecto()) {
					return true;
				}
				else {
					String resultado = datosCert.getDescripcionResultado(); 
					log.warn("El certificado no es correcto: " + resultado);
					
					request.setAttribute("resultado", resultado);
					request.getRequestDispatcher("/fail_login.jsp").forward(request, response);					
				}
			} catch (CertificateEncodingException e) {
				log.error(e);				
			} catch (AFirmaException e) {
				log.error(e);
				request.getRequestDispatcher("/error_afirma.jsp").forward(request, response);
			} catch (IOException e) {
				log.error(e);
			}
			catch (Exception e) {
				//Si da algun error distinto dejamos pasar
				log.error(e);
				return true;
				
			}
			return false;
		}
		return true;		
	}

	private static Boolean isAfirmaActive() throws LocalgisConfigurationException{
		String afirmaActive = Configuration.getPropertyString(PROP_AFIRMA_ACTIVE);
		if(afirmaActive != null){		
			return Boolean.valueOf(afirmaActive);
		}
		return true;
	}

}
