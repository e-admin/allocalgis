/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.usuario.modelo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorIntegerSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.usuario.cu.GestorCUUsuario;
import es.dc.a21l.usuario.cu.UsuarioDto;

@Controller
@RequestMapping(value="/usuarios")
public class UsuarioControladorSW {
	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	private GestorCUUsuario gestorCUUsuario;
	
	
	@Autowired
	public void setGestorCUUsuario(GestorCUUsuario gestorCUUsuario) {
		this.gestorCUUsuario = gestorCUUsuario;
	}

	
	@RequestMapping(method = RequestMethod.GET, value = { "/listaTodosUsuarios" })
	public ResponseEntity<EncapsuladorListSW<UsuarioDto>> cargaTodos() {
		return new ResponseEntity<EncapsuladorListSW<UsuarioDto>>(gestorCUUsuario.cargaTodos(), responseHeaders, HttpStatus.OK);
	}


	@RequestMapping(method = RequestMethod.GET, value = { "/usuario/{id}" })
	public ResponseEntity<UsuarioDto> cargaPorId(@PathVariable("id") Long id) {
		UsuarioDto usuarioDto=gestorCUUsuario.carga(id);
		return new ResponseEntity<UsuarioDto>(usuarioDto, responseHeaders, HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = { "/guardaUsuario" })
	public ResponseEntity<EncapsuladorPOSTSW<UsuarioDto>> guarda(@RequestBody UsuarioDto usuarioDto) {
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		usuarioDto=gestorCUUsuario.guarda(usuarioDto, errores);
		EncapsuladorPOSTSW<UsuarioDto> encapsulador=new EncapsuladorPOSTSW<UsuarioDto>(usuarioDto, errores);
		ResponseEntity<EncapsuladorPOSTSW<UsuarioDto>> respuesta= new ResponseEntity<EncapsuladorPOSTSW<UsuarioDto>>(encapsulador,responseHeaders, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = { "/guardaUsuarioPass" })
	public ResponseEntity<EncapsuladorPOSTSW<UsuarioDto>> guardaPass(@RequestBody UsuarioDto usuarioDto) {
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		usuarioDto=gestorCUUsuario.guardaUsuarioPass(usuarioDto, errores);
		EncapsuladorPOSTSW<UsuarioDto> encapsulador=new EncapsuladorPOSTSW<UsuarioDto>(usuarioDto, errores);
		ResponseEntity<EncapsuladorPOSTSW<UsuarioDto>> respuesta= new ResponseEntity<EncapsuladorPOSTSW<UsuarioDto>>(encapsulador,responseHeaders, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaUsuarioPorLogin/{login}" })
	public ResponseEntity<UsuarioDto> cargaUsuarioDetalles(@PathVariable("login") String login){
		try{
			UsuarioDto usuarioDto=gestorCUUsuario.cargaUsuarioPorLogin(login);
			if(usuarioDto==null)
				return new ResponseEntity<UsuarioDto>(null,responseHeaders, HttpStatus.NOT_FOUND); 
			
			return new ResponseEntity<UsuarioDto>(usuarioDto,responseHeaders, HttpStatus.OK);
			
		}catch (Throwable e) {
			return new ResponseEntity<UsuarioDto>(null,responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Forma general de borrar
//	@RequestMapping(method=RequestMethod.DELETE,value={"/eliminarUsuario/{id}"})
//	public ResponseEntity eliminarUsuario(@PathVariable("id") Long id){
//		gestorCUUsuario.borra(id);
//		return new ResponseEntity(HttpStatus.OK);
//	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/eliminarUsuario/{id}/{idUsuarioBorra}"})
	public ResponseEntity<EncapsuladorIntegerSW> eliminarUsuario(@PathVariable("id") Long id,@PathVariable("idUsuarioBorra") Long idUsuarioBorra){
		try {
			return new ResponseEntity<EncapsuladorIntegerSW>(gestorCUUsuario.borra(id,idUsuarioBorra)?new EncapsuladorIntegerSW(1):new EncapsuladorIntegerSW(0),responseHeaders, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<EncapsuladorIntegerSW>(new EncapsuladorIntegerSW(-1),responseHeaders,HttpStatus.OK);
		}
		
	}
	
}
