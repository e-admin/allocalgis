/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.indicador.modelo.controladores;

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
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.DescripcionAtributoDto;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.cu.GestorCUFuente;
import es.dc.a21l.fuente.cu.GestorCUTablaFuenteDatos;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.servicio.controladores.GenericAbstractController;

/*
 * SERVICIO WEB REST
 */

@Controller
@RequestMapping(value = "/tablas")
public class TablaFuenteDatosControladorSW extends GenericAbstractController {
	private static final Logger log = LoggerFactory.getLogger(TablaFuenteDatosControladorSW.class);
	
	private GestorCUTablaFuenteDatos gestorCUTablaFuenteDatos;
	
	@Autowired
	public void setGestorCUTablaFuenteDatos(GestorCUTablaFuenteDatos gestorCUTablaFuenteDatos) {
		this.gestorCUTablaFuenteDatos = gestorCUTablaFuenteDatos;
	}
	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	@RequestMapping(method = RequestMethod.GET, value = "/tabla/{id}")
	public ResponseEntity<TablaFuenteDatosDto> cargaPorId(@PathVariable("id") Long id) {
		return new ResponseEntity<TablaFuenteDatosDto>(gestorCUTablaFuenteDatos.carga(id), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/tabla")
	public ResponseEntity<EncapsuladorPOSTSW> guardar(@RequestBody TablaFuenteDatosDto tablaFuenteDatosDto,HttpServletRequest request) {
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		tablaFuenteDatosDto = gestorCUTablaFuenteDatos.guarda(tablaFuenteDatosDto, errores);
		EncapsuladorPOSTSW<TablaFuenteDatosDto> encapsulador=new EncapsuladorPOSTSW<TablaFuenteDatosDto>(tablaFuenteDatosDto, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/tabla/{idIndicador}/{nombreTabla}/{idFuente}")
	public ResponseEntity<TablaFuenteDatosDto> cargarTablaPorIndicadorFuenteNombre(@PathVariable("idIndicador") Long idIndicador,@PathVariable("idFuente") Long idFuente,@PathVariable("nombreTabla") String nombreTabla, HttpServletRequest request) {
		return new ResponseEntity<TablaFuenteDatosDto>(gestorCUTablaFuenteDatos.cargaPorIndicadorYFuenteYTabla(idIndicador, idFuente, nombreTabla), responseHeaders, HttpStatus.OK);
	}
}