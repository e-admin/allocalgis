/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.indicador.modelo.controladores;

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
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.criterio.cu.CriterioDto;
import es.dc.a21l.criterio.cu.GestorCUCriterio;
import es.dc.a21l.servicio.controladores.GenericAbstractController;


@Controller
@RequestMapping(value = "/criterios")
public class CriterioControladorSW extends GenericAbstractController {
	
	private GestorCUCriterio gestorCUCriterio;

	@Autowired
	public void setGestorCUCriterio(GestorCUCriterio gestorCUCriterio) {
		this.gestorCUCriterio = gestorCUCriterio;
	}
	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	
	@RequestMapping(method=RequestMethod.POST,value="/guardaNuevoCriterio/{idAtributo}/{tipoColumna}")
	public ResponseEntity<EncapsuladorPOSTSW<CriterioDto>> guardaNuevoCriterio(@RequestBody EncapsuladorStringSW cadenaCriterio, @PathVariable("idAtributo") Long idAtributo,@PathVariable("tipoColumna") String tipoColumna){
		
		String caracterSeparadorDecimales = getServicioConfiguracionGeneral().getCaracterSeparadorDecimales();
		
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		CriterioDto result=gestorCUCriterio.guardaNuevoCriterio(idAtributo, cadenaCriterio.getTexto(),tipoColumna, errores, caracterSeparadorDecimales);
		return new ResponseEntity<EncapsuladorPOSTSW<CriterioDto>>(new EncapsuladorPOSTSW<CriterioDto>(result,errores), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method=RequestMethod.GET,value="/cargaCriterios/{idIndicador}")
	public ResponseEntity<EncapsuladorListSW<CriterioDto>> cargaCriterioPorIdIndicador(@PathVariable("idIndicador") Long idIndicador){
		EncapsuladorListSW<CriterioDto> result=gestorCUCriterio.cargarPorIdIndicador(idIndicador);
		return new ResponseEntity<EncapsuladorListSW<CriterioDto>>(new EncapsuladorListSW<CriterioDto>(result), responseHeaders, HttpStatus.OK);
	}
}
