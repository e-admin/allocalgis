/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.rol.modelo.controladores;

import java.util.ArrayList;
import java.util.List;

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
import es.dc.a21l.usuario.cu.GestorCURol;
import es.dc.a21l.usuario.cu.RolDto;
import es.dc.a21l.usuario.cu.impl.RolDtoFormErrorsEmun;


@Controller
@RequestMapping(value="/roles")
public class RolControladorSW {

	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	private GestorCURol gestorCURol;

	@Autowired
	public void setGestorCURol(GestorCURol gestorCURol) {
		this.gestorCURol = gestorCURol;
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaTodos"})
	public ResponseEntity<EncapsuladorListSW<RolDto>> cargaTodos(){
		return new ResponseEntity<EncapsuladorListSW<RolDto>>(gestorCURol.cargaTodos(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value={"/guardarRol"})
	public ResponseEntity<EncapsuladorPOSTSW<RolDto>> guardarRol(@RequestBody RolDto rolDto){
		
		EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
		
		List<Long> listaIdsEJs=new ArrayList<Long>();
		listaIdsEJs.addAll(rolDto.getEltosJerarquia());
		
		RolDto rolDto2=null;
		if(rolDto.getId()>0)
			rolDto2=gestorCURol.cargarPorId(rolDto.getId());
			
		rolDto = gestorCURol.guardar(rolDto, errores);
		if (rolDto!=null)
			rolDto.setEltosJerarquia(listaIdsEJs);
		
		if(!errores.getHashErrors()){
			try {
				gestorCURol.guardaActualizacionPermisos(rolDto.getId(), listaIdsEJs);
				
			} catch (Throwable e) {
				if(rolDto2!=null){
					gestorCURol.guardar(rolDto2, errores);
				}else{
					gestorCURol.borrar(rolDto.getId());
				}
				errores.addError(RolDtoFormErrorsEmun.ERROR_PERMISOS);
				rolDto=null;
			}
		}
		return new ResponseEntity<EncapsuladorPOSTSW<RolDto>>(new EncapsuladorPOSTSW<RolDto>(rolDto, errores), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/rol/{id}" })
	public ResponseEntity<RolDto> cargarPorId(@PathVariable("id") Long id) {
		RolDto rolDto = gestorCURol.cargarPorId(id);
		return new ResponseEntity<RolDto>(rolDto, responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/eliminarRol/{id}"})
	public ResponseEntity<EncapsuladorIntegerSW> eliminarRol(@PathVariable("id") Long id){
		try {
			return new ResponseEntity<EncapsuladorIntegerSW>(gestorCURol.borrar(id)?new EncapsuladorIntegerSW(1):new EncapsuladorIntegerSW(0), responseHeaders, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<EncapsuladorIntegerSW>(new EncapsuladorIntegerSW(-1),responseHeaders,HttpStatus.OK);
		}
		
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaRolesPorUsuario/{idUsuario}"})
	public ResponseEntity<EncapsuladorListSW<RolDto>> cargaRolesPorUsuario(@PathVariable("idUsuario")Long idUsuario){
		return new ResponseEntity<EncapsuladorListSW<RolDto>>(gestorCURol.cargaRolesPorUsuario(idUsuario), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaRolesSinAsignarUsuario/{idUsuario}"})
	public ResponseEntity<EncapsuladorListSW<RolDto>> cargaRolesSinAsignarUsuario(@PathVariable("idUsuario") Long idUsuario){
		return new ResponseEntity<EncapsuladorListSW<RolDto>>(gestorCURol.cargaRolesSinAsignarUsuario(idUsuario), responseHeaders, HttpStatus.OK);
	}
	
}
