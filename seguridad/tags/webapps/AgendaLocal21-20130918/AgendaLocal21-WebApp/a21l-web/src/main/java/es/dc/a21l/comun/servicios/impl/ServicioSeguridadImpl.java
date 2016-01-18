/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.comun.servicios.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.dc.a21l.comun.servicios.ServicioConfiguracionGeneral;
import es.dc.a21l.comun.servicios.ServicioSeguridad;
import es.dc.a21l.comun.utils.UrlConstructorSW;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.modelo.web.base.UsuarioDetalles;
import es.dc.a21l.usuario.cu.UsuarioDto;


public class ServicioSeguridadImpl implements ServicioSeguridad {
	
	private ServicioConfiguracionGeneral servicioConfiguracionGeneral;
	private RestTemplate restTemplate;
	private static final String USUARIO_INVITADO_LOGIN="Invitado";
	private static final String USUARIO_INVITADO_PASS="a6ae8a143d440ab8c006d799f682d48d";
	private static final Logger LOG = LoggerFactory.getLogger(ServicioConfiguracionGeneral.class);
	
	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}


	@Autowired
	public void setServicioConfiguracionGeneral(
			ServicioConfiguracionGeneral servicioConfiguracionGeneral) {
		this.servicioConfiguracionGeneral = servicioConfiguracionGeneral;
	}


	public UserDetails loadUserByUsername(String paramString)throws UsernameNotFoundException, DataAccessException {
		
		UsuarioDetalles user;
		//usuario Invitado
		if(StringUtils.equalsIgnoreCase(paramString, USUARIO_INVITADO_LOGIN)){
			 Set<String> rolesI=new HashSet<String>();
			 rolesI.add("ROLE_ANONIMO");
			 
			 user=new UsuarioDetalles(0, USUARIO_INVITADO_LOGIN, USUARIO_INVITADO_PASS, false, true, rolesI);
			 user.setEsAdmin(false);
			 user.setInvitado(true);
			 return user;
		}
		
		UrlConstructorSW urlConstructorSW= new UrlConstructorSWImpl(servicioConfiguracionGeneral.getUrlBaseSW());
		urlConstructorSW.setParametroCadena("usuarios");
		urlConstructorSW.setParametroCadena("cargaUsuarioPorLogin");
		urlConstructorSW.setParametroCadena(paramString);
		
//		EncapsuladorMapSW<String, String> map = new EncapsuladorMapSW<String, String>();
//        map.put("j_username", "anonimo");
//        map.put("j_password","anonimo");
        //String response = restTemplate.postForObject(servicioConfiguracionGeneral.getUrlBaseSW()+"/j_spring_security_check",  map, String.class);
		
		ResponseEntity<UsuarioDto> respuesta=null;
        try{
        	respuesta= restTemplate.getForEntity(urlConstructorSW.getUrl(), UsuarioDto.class);
        }catch (HttpClientErrorException e) {
        	LOG.error("Error inicianso sesion"+e);
        	if(e.getStatusCode().equals(HttpStatus.NOT_FOUND))
        		throw new UsernameNotFoundException("No se ha encontrado el usuario "+paramString);
        	
        	throw new DataAccessException("Problemas conectando con el servicio web") {
			};	
        }
        catch (RuntimeException e) {
        	LOG.error("Error inicianso sesion"+e);
        	throw new DataAccessException("Problemas conectando con el servicio web") {
			};
		}
		
		UsuarioDto usuarioDto=respuesta.getBody();
		 Set<String> roles=new HashSet<String>();
		 roles.add("ROLE_ANONIMO");
		 if(usuarioDto.getEsAdmin()){
			 roles.add("ROL_ADMIN");
		 }
		 user=new UsuarioDetalles(usuarioDto.getId(), usuarioDto.getLogin(), usuarioDto.getPassword(), !usuarioDto.getActivo(), true, roles);
		 user.setEsAdmin(usuarioDto.getEsAdmin());
		 user.setInvitado(false);
		return user;
	}
	
	
	 public UsuarioDetalles getUserDetails() {
		    Authentication authentication = getAuthentication();
		    if (!(authentication.getPrincipal() instanceof UserDetails)) {
		      return null;
		    }
		    return (UsuarioDetalles)authentication.getPrincipal();
		  }
	 
	 public Authentication getAuthentication()
	  {
	    SecurityContext sc = SecurityContextHolder.getContext();
	    if (sc == null) throw new IllegalStateException("No esta configurado el contexto de seguridad");

	    Authentication au = sc.getAuthentication();
	    if (au == null) throw new IllegalStateException("No existe el objeto Authentication en el contexto de seguridad");

	    return au;
	  }
	 

}
