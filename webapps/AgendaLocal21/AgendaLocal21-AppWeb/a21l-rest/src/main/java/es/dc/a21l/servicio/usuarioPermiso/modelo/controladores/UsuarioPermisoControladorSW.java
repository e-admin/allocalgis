/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.usuarioPermiso.modelo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.usuario.cu.GestorCUUsuarioPermiso;
import es.dc.a21l.usuario.cu.UsuarioElementoJerarquiaDto;


@Controller
@RequestMapping(value="/usuarioPermisos")
public class UsuarioPermisoControladorSW {

	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	private GestorCUUsuarioPermiso gestorCUUsuarioPermiso;

	@Autowired
	public void setGestorCUUsuarioPermiso(GestorCUUsuarioPermiso gestorCUUsuarioPermiso) {
		this.gestorCUUsuarioPermiso = gestorCUUsuarioPermiso;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargarPermisosPorUsuario/{idUsuario}" })
	public ResponseEntity<EncapsuladorListSW<UsuarioElementoJerarquiaDto>> cargarPermisosPorUsuario(@PathVariable("idUsuario") Long idUsuario){
		return new ResponseEntity<EncapsuladorListSW<UsuarioElementoJerarquiaDto>>(gestorCUUsuarioPermiso.cargarPermisosPorUsuario(idUsuario), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/actualizarListaPermisosUsuario/{idUsuario}"})
	public ResponseEntity<EncapsuladorBooleanSW> actualizarListaPermisosUsuario(@RequestBody EncapsuladorListSW<Long> listaIdsPermisos, @PathVariable("idUsuario") Long idUsuario){
		gestorCUUsuarioPermiso.actualizarListaPermisosUsuario(idUsuario, listaIdsPermisos);
		return new ResponseEntity<EncapsuladorBooleanSW>(new EncapsuladorBooleanSW(true), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/insertarPermisoUsuario/{idUsuario}/{idIndicador}"})
	public ResponseEntity<EncapsuladorBooleanSW> insertarPermisoUsuario(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idIndicador") Long idIndicador){
		Boolean resultado = gestorCUUsuarioPermiso.guardaPermisoUsuario(idUsuario, idIndicador);
		return new ResponseEntity<EncapsuladorBooleanSW>(new EncapsuladorBooleanSW(resultado), responseHeaders, HttpStatus.OK);
	}
}
