/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.metadatos.modelo.controladores;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.metadatos.cu.GestorCUMetadatos;
import es.dc.a21l.metadatos.cu.MetadatosDto;
import es.dc.a21l.servicio.controladores.GenericAbstractController;

/*
 * SERVICIO WEB REST
 */

@Controller
@RequestMapping(value = "/metadatos")
public class MetadatosControladorSW extends GenericAbstractController {
	private static final Logger log = LoggerFactory.getLogger(MetadatosControladorSW.class);
	
	private GestorCUMetadatos gestorCUMetadatos;
	
	@Autowired
	public void setGestorCUMetadatos(GestorCUMetadatos gestorCUMetadatos) {
		this.gestorCUMetadatos = gestorCUMetadatos;
	}
	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
		
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/metadato")
	public ResponseEntity<EncapsuladorPOSTSW> guardar(@RequestBody MetadatosDto metadatosDto,HttpServletRequest request) {
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		MetadatosDto tmp = gestorCUMetadatos.cargaPorIdIndicador(metadatosDto.getIndicador().getId());
		tmp.setIndicador(metadatosDto.getIndicador());
		tmp.setMetadatos(metadatosDto.getMetadatos());
		metadatosDto=gestorCUMetadatos.guarda(tmp, errores);
		EncapsuladorPOSTSW<MetadatosDto> encapsulador=new EncapsuladorPOSTSW<MetadatosDto>(metadatosDto, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/metadato/{id}")
	public ResponseEntity<MetadatosDto> cargarPorIdIndicador(@PathVariable("id") Long id,HttpServletRequest request) {
		MetadatosDto tmp = gestorCUMetadatos.cargaPorIdIndicador(id);
		ResponseEntity<MetadatosDto> respuesta= new ResponseEntity<MetadatosDto>(tmp, HttpStatus.OK);
		return respuesta;
	}
}