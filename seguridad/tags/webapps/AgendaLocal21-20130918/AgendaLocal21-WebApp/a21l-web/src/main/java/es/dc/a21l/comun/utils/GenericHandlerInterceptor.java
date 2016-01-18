/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.comun.utils;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import es.dc.a21l.comun.servicios.ServicioConfiguracionGeneral;
import es.dc.a21l.comun.servicios.ServicioSeguridad;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.modelo.web.base.UsuarioDetalles;
import es.dc.a21l.publicacion.cu.PublicacionDto;

public class GenericHandlerInterceptor extends HandlerInterceptorAdapter {
	
	private Map<String, String> mapaElementoMenu;
	private ServicioSeguridad servicioSeguridad;
	private List<String> patronesExcluidos;
	private List<String> patronesInvitado;
	private RestTemplate restTemplate;
	private ServicioConfiguracionGeneral servicioConfiguracionGeneral;
	
	
	@Autowired
	public void setServicioConfiguracionGeneral(
			ServicioConfiguracionGeneral servicioConfiguracionGeneral) {
		this.servicioConfiguracionGeneral = servicioConfiguracionGeneral;
	}


	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}


	@Autowired
	public void setServicioSeguridad(ServicioSeguridad servicioSeguridad) {
		this.servicioSeguridad = servicioSeguridad;
	}


	public void setMapaElementoMenu(Map<String, String> mapaElementoMenu) {
		this.mapaElementoMenu = mapaElementoMenu;
	}
	
	public void setPatronesExcluidos(List<String> patronesExcluidos) {
		this.patronesExcluidos = patronesExcluidos;
	}


	public void setPatronesInvitado(List<String> patronesInvitado) {
		this.patronesInvitado = patronesInvitado;
	}


	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		super.preHandle(request, response, handler);
		
		if(pathEstaExcluido(request, patronesExcluidos))
			return true;
		
		
		UsuarioDetalles usuarioDetalles= servicioSeguridad.getUserDetails();
		if(usuarioDetalles==null || StringUtils.isBlank(usuarioDetalles.getUsername())){
			response.sendRedirect(urlPaginaLogin(request));
			return false;
		}
		
		if(usuarioDetalles.getInvitado()){
			UrlConstructorSW url= new UrlConstructorSWImpl(servicioConfiguracionGeneral.getUrlBaseSW());
			url.setParametroCadena("publicacion").setParametroCadena("obtenerEstado");
			PublicacionDto publi = restTemplate.getForEntity(url.getUrl(), PublicacionDto.class).getBody();
			if(publi==null || publi.getPublicacionWebHabilitada()==null || !publi.getPublicacionWebHabilitada()){
				response.sendRedirect(urlLogOut(request));
				return false;
			}
			
		}
		
		request.setAttribute("usuarioInvitado",usuarioDetalles.getInvitado());
		request.setAttribute("usuarioAdministrador",usuarioDetalles.getEsAdmin());
		request.setAttribute("loginUsuarioActual", usuarioDetalles.getUsername());
		
		if(usuarioDetalles.getInvitado() && !pathEstaExcluido(request, patronesInvitado)){
			response.sendRedirect(urlPaginaIndicadores(request));
			return false;
		}
		
		request.setAttribute("paginaActiva", mapaElementoMenu.get(request.getServletPath()));

		//si ha cambiado la pass, enviar variable para mostrar mensaje de exito
		if ( request.getSession().getAttribute("pass_cambiada")!=null && request.getSession().getAttribute("pass_cambiada").equals("correcto") ) {
			request.getSession().removeAttribute("pass_cambiada");
			request.setAttribute("passwordCambiadaExito", "jsp.usuario.pass.cambiada.exito");
		}
		
		return true;
	}
	
	
	private boolean pathEstaExcluido(HttpServletRequest request, List<String> patrons) {
	    AntPathMatcher matcher = new AntPathMatcher();
	    if (patrons != null) {
		    for (String patron: patrons) {
		    	if (matcher.match(patron, request.getRequestURI())) {
		    		return true;
		    	}
		    }
	    }
	    return false;
	  }
	
	private String urlPaginaLogin(HttpServletRequest request){
		String result=contruirUrlBase(request);
		result=result+"/login.htm";
		return result;
	}
	
	private String urlPaginaIndicadores(HttpServletRequest request){
		String result=contruirUrlBase(request);
		result=result+"/indicadores.htm";
		return result;
	}
	
	private String urlLogOut(HttpServletRequest request){
		String result=contruirUrlBase(request);
		result=result+"/j_spring_security_logout";
		return result;
	}
	
	private String contruirUrlBase(HttpServletRequest request){
		String result="";
		result=result+request.getScheme()+"://";
		result=result+request.getServerName();
		result=result+":"+request.getServerPort();
		result=result+request.getContextPath();
		return result;
	}
	
}
