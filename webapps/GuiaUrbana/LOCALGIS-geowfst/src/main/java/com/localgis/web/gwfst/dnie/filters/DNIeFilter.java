/**
 * DNIeFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.dnie.filters;

import java.io.IOException;
//import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

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

import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.security.dnie.CertificateAuthManager;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;

//import com.localgis.web.core.utils.Base64Helper;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Filtro de autenticación mediante DNIe/FNMT
 */
public class DNIeFilter implements Filter {

	/**
	 * Logger
	 */
	private static Log log = LogFactory.getLog(DNIeFilter.class);

	/**
	 * Constantes
	 */
	private static final String REQUEST_CERTIFICATE_ATTRIBUTE = "javax.servlet.request.X509Certificate";
	private static final String LOGIN_ATTRIBUTE = "LoginAttribute";
	private static final String LOGIN_ROL = "Geopista.Visualizador.Login";

	/**
	 * Filtro de autenticación mediante DNIe/FNMT
	 * @param request: Objeto petición del filtro
	 * @param response: Objeto respuesta del filtro
	 * @param chain: Cadena de filtros
	 * @throws OException
	 * @throws ServletException
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session = httpServletRequest.getSession();
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		Integer httpPort = (Integer) session
				.getAttribute(CertificateAuthManager.HTTP_CONNECTION_PORT);
		String baseURL = "http://" + request.getServerName() + ":" + httpPort;
		// String finalURL = baseURL + "/localgis-geowfst/features/";
		String finalURL = (String) session
				.getAttribute(CertificateAuthManager.HTTP_CONNECTION_URL);

		if (httpServletRequest.getSession().getAttribute(LOGIN_ATTRIBUTE) == null) {
			// log.info("El usuario no está autenticado");
			X509Certificate[] certificados = (X509Certificate[]) request
					.getAttribute(REQUEST_CERTIFICATE_ATTRIBUTE);

			// String certificadoBase64;
			try {
				// certificadoBase64 =
				// Base64Helper.getInstance().encodeBytes(certificados[0].getEncoded());
				// DatosValidacionCertificadoOut datosCert =
				// aFirmaManager.validarCertificado(certificadoBase64);
				// log.debug("Datos de validación: " + datosCert);

				// if (datosCert.certificadoCorrecto()) {
				String certNIF = CertificateAuthManager
						.getNIFfromSubjectDN(certificados[0]
								.getSubjectX500Principal().getName());
				// log.error(certNIF);

				LocalgisUtilsManager localgisUtilsManager = LocalgisManagerBuilderSingleton
						.getInstance().getLocalgisUtilsManager();
				Integer idEntidad = localgisUtilsManager
						.isValidUserEntityNotPass(certNIF, LOGIN_ROL);
				if (idEntidad > -1) {
					// httpServletRequest.getSession().setAttribute(LOGIN_ATTRIBUTE,datosCert.getCertificado().getNIFResponsable());
					session.setAttribute(LOGIN_ATTRIBUTE, certNIF);
					// request.getRequestDispatcher(loginAction).forward(request,
					// response);

					// finalURL = (String)
					// session.getAttribute(CertificateAuthManager.HTTP_CONNECTION_URL);

					// String fullRequestURL =
					// httpServletRequest.getRequestURL().toString();
					// if(httpServletRequest.getParameter("requesturi")==null){
					// Enumeration e = httpServletRequest.getParameterNames();
					// String requesturi = "";
					// if(e.hasMoreElements()) fullRequestURL += "?";
					// while(e.hasMoreElements()){
					// String attribute = (String) e.nextElement();
					// fullRequestURL += attribute + "=" +
					// httpServletRequest.getParameter(attribute);
					// if(e.hasMoreElements()) fullRequestURL += "&";
					// }
					// }else fullRequestURL =
					// httpServletRequest.getParameter("requesturi");
					// request.setAttribute("fullRequestURL", fullRequestURL);
					// request.getRequestDispatcher(loginAction).forward(request,
					// response);
					// //
					// //request.getRequestDispatcher("/private/selectEntidad.do").forward(request,
					// response);
					// finalURL = baseURL +
					// "/localgis-geowfst/features/showFeatureMap.do";
					// //adquirida al pulsar el botón DNIe o al entra en
					// /private (filtro previo)
					// if(idEntidad>0){
					// httpServletRequest.getSession().setAttribute("idEntidad",
					// idEntidad);
					// //return mapping.findForward(SUCCESS_PAGE_ENTITY);
					// //
					// request.getRequestDispatcher("/private/selectMap.do").forward(request,
					// response);
					// finalURL = baseURL +
					// "/localgis-geowfst/features/selectMap.do?idEntidad=" +
					// idEntidad;
					// }

				}
				// }
				// else {
				// String resultado = datosCert.getDescripcionResultado();
				// log.warn("El certificado no es correcto: " + resultado);

				// request.setAttribute("resultado", resultado);
				// //Redirigir a pantalla de error por certificado invalido
				// request.getRequestDispatcher("/fail_login.jsp").forward(request,
				// response);
				// }
				// } catch (CertificateEncodingException e) {
				// log.error(e);
				// } catch (AFirmaException e) {
				// log.error(e);
				// request.getRequestDispatcher("/error_afirma.jsp").forward(request,
				// response);
			} catch (Exception e) {
				log.error(e);
				finalURL = baseURL + "/localgis-geowfst/errorFeature.jsp";
				httpServletResponse.sendRedirect(finalURL);
			}
		}
		httpServletResponse.sendRedirect(finalURL);
	}

	/**
	 * Sobrescritura del destructor
	 */
	public void destroy() {
	}

	/**
	 * Sobrescritura del inicializador
	 * @param config: Configuración del filtro
	 * @throws ServletException
	 */
	public void init(FilterConfig config) throws ServletException {
	}
	
}
