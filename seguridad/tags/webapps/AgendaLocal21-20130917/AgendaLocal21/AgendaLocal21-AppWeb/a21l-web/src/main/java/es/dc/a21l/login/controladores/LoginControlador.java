/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.login.controladores;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import es.dc.a21l.comun.controladores.GenericAbstractController;
import es.dc.a21l.comun.servicios.ServicioConfiguracionGeneral;
import es.dc.a21l.comun.utils.UrlConstructorSW;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.publicacion.cu.PublicacionDto;

@Controller
public class LoginControlador extends GenericAbstractController {
	private static final Logger log = LoggerFactory.getLogger(LoginControlador.class);
	private ServicioConfiguracionGeneral servicioConfiguracionGeneral;
	private RestTemplate restTemplate;
	
	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Autowired
	public void setServicioConfiguracionGeneral(
			ServicioConfiguracionGeneral servicioConfiguracionGeneral) {
		this.servicioConfiguracionGeneral = servicioConfiguracionGeneral;
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/login.htm")
	public String login(Model model) {
		UrlConstructorSW url= new UrlConstructorSWImpl(servicioConfiguracionGeneral.getUrlBaseSW());
		url.setParametroCadena("publicacion");
		url.setParametroCadena("obtenerEstado");
		String urlDelServicio = url.getUrl();
		PublicacionDto publi = restTemplate.getForEntity(urlDelServicio, PublicacionDto.class).getBody();
		//SI la publicacion no existe o no esta habilitada => no esta habilitada
		if ( publi == null || publi.getPublicacionWebHabilitada()==null || !publi.getPublicacionWebHabilitada()) {
			model.addAttribute("estadoPublicacion", false);
		} else {
			model.addAttribute("estadoPublicacion", publi.getPublicacionWebHabilitada());
		}
		
		model.addAttribute("errorAutentificacion",false);
		return "loginTile";		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/loginfailed.htm")
	public String loginerror(Model model) {
		UrlConstructorSW url= new UrlConstructorSWImpl(servicioConfiguracionGeneral.getUrlBaseSW());
		url.setParametroCadena("publicacion");
		url.setParametroCadena("obtenerEstado");
		
		PublicacionDto publi = restTemplate.getForEntity(url.getUrl(), PublicacionDto.class).getBody();
		//SI la publicacion no existe o no esta habilitada => no esta habilitada
		if ( publi == null || publi.getPublicacionWebHabilitada()==null || !publi.getPublicacionWebHabilitada()) {
			model.addAttribute("estadoPublicacion", false);
		} else {
			model.addAttribute("estadoPublicacion", publi.getPublicacionWebHabilitada());
		}
		
		model.addAttribute("errorAutentificacion",true);
		return "loginTile";
 
	}
}