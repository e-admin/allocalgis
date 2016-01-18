/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.usuarioRol.modelo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.usuario.cu.GestorCUUsuarioRol;
import es.dc.a21l.usuario.cu.UsuarioRolDto;


@Controller
@RequestMapping(value="/usuarioRoles")
public class UsuarioRolControladorSW {

	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	private GestorCUUsuarioRol gestorCUUsuarioRol;

	@Autowired
	public void setGestorCUUsuarioRol(GestorCUUsuarioRol gestorCUUsuarioRol) {
		this.gestorCUUsuarioRol = gestorCUUsuarioRol;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaPorUsuario/{idUsuario}" })
	public ResponseEntity<EncapsuladorListSW<UsuarioRolDto>> cargaPorUsuario(@PathVariable("idUsuario") Long idUsuario){
		return new ResponseEntity<EncapsuladorListSW<UsuarioRolDto>>(gestorCUUsuarioRol.cargaPorUsuario(idUsuario), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/actualizaListaRolesUsuario/{idUsuario}"})
	public ResponseEntity<EncapsuladorBooleanSW> actualizaListaRolesUsuario(@RequestBody EncapsuladorListSW<Long> listaIdsRoles,@PathVariable("idUsuario") Long idUsuario){
		gestorCUUsuarioRol.actualizaListaRolesUsuario(idUsuario, listaIdsRoles);
		return new ResponseEntity<EncapsuladorBooleanSW>(new EncapsuladorBooleanSW(true), responseHeaders, HttpStatus.OK);
	}
	
	
}
