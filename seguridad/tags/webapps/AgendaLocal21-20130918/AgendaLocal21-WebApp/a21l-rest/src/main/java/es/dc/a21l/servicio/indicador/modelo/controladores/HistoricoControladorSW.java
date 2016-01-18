/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.indicador.modelo.controladores;

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
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicador;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.historico.cu.GestorCUHistorico;
import es.dc.a21l.historico.cu.HistoricoDto;
import es.dc.a21l.servicio.controladores.GenericAbstractController;


@Controller
@RequestMapping(value="/historico")
public class HistoricoControladorSW extends GenericAbstractController{

	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	private GestorCUHistorico gestorCUHistorico;
	private GestorCUIndicador gestorCUIndicador;

	@Autowired
	public void setGestorCUHistorico(GestorCUHistorico gestorCUHistorico) {
		this.gestorCUHistorico = gestorCUHistorico;
	}
	
	@Autowired
	public void setGestorCUIndicador(GestorCUIndicador gestorCUIndicador) {
		this.gestorCUIndicador = gestorCUIndicador;
	}
	
	
	/** Busca todas las versiones almacenadas para un indicador concreto*/
	@RequestMapping(method=RequestMethod.GET,value={"/cargarHistoricos/{idIndicador}"})
	public ResponseEntity<EncapsuladorListSW<HistoricoDto>> cargaHistoricosPorIndicador(@PathVariable("idIndicador")Long idIndicador){
		return new ResponseEntity<EncapsuladorListSW<HistoricoDto>>(gestorCUHistorico.cargarTodosPorIndicador(idIndicador), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargarPorId/{id}"})
	public ResponseEntity<HistoricoDto> cargarPorId(@PathVariable("id") Long id){
		return new ResponseEntity<HistoricoDto>(gestorCUHistorico.cargarPorId(id), responseHeaders, HttpStatus.OK);
	}
	
	/** Almacena una nueva versión para un indicador */
	@RequestMapping(method=RequestMethod.POST,value={"/guardarHistorico/{idIndicador}"})
	public ResponseEntity<EncapsuladorPOSTSW<HistoricoDto>> guardarHistorico(@PathVariable("idIndicador")Long idIndicador, @RequestBody HistoricoDto historicoDto){
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(idIndicador);
		historicoDto.setIndicador(indicadorDto);
		
		historicoDto = gestorCUHistorico.guardar(historicoDto, errores);
		return new ResponseEntity<EncapsuladorPOSTSW<HistoricoDto>>(new EncapsuladorPOSTSW<HistoricoDto>(historicoDto, errores), responseHeaders, HttpStatus.OK);
	}
	
	/** Elimina una versión de un indicador */
	@RequestMapping(method=RequestMethod.GET,value={"/eliminarHistorico/{id}"})
	public ResponseEntity<EncapsuladorIntegerSW> eliminarHistorico(@PathVariable("id") Long id){
		try {
			return new ResponseEntity<EncapsuladorIntegerSW>(gestorCUHistorico.borrar(id)?new EncapsuladorIntegerSW(1):new EncapsuladorIntegerSW(0), responseHeaders, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<EncapsuladorIntegerSW>(new EncapsuladorIntegerSW(-1),responseHeaders,HttpStatus.OK);
		}
	}
}
