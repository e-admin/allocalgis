/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.publicacion.controladores;

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
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.publicacion.cu.GestorCUPublicacion;
import es.dc.a21l.publicacion.cu.PublicacionDto;
import es.dc.a21l.usuario.cu.GestorCUUsuario;
import es.dc.a21l.usuario.cu.UsuarioDto;

@Controller
@RequestMapping(value="/publicacion")
public class PublicacionControladorSW {
	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	private GestorCUPublicacion gestorCUPublicacion;
	
	@Autowired
	public void setGestorCUPublicacion(GestorCUPublicacion gestorCUPublicacion) {
		this.gestorCUPublicacion = gestorCUPublicacion;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/obtenerEstado" })
	public ResponseEntity<PublicacionDto> obtenerEstado() {
		ResponseEntity<PublicacionDto> publiEntity =  new ResponseEntity<PublicacionDto>(gestorCUPublicacion.obtenerEstado(), responseHeaders, HttpStatus.OK);
		return publiEntity;		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = { "/guardaPublicacion" })
	public ResponseEntity<EncapsuladorPOSTSW<PublicacionDto>> guardaPublicacion(@RequestBody PublicacionDto publicacionDto) {
		EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
		PublicacionDto publicacionDto2 = gestorCUPublicacion.guardar(publicacionDto, errores);
		return new ResponseEntity<EncapsuladorPOSTSW<PublicacionDto>>(new EncapsuladorPOSTSW<PublicacionDto>(publicacionDto2,errores), responseHeaders, HttpStatus.OK);
	}
}