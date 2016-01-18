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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorExpresion;
import es.dc.a21l.elementoJerarquia.cu.GestorCURelacion;
import es.dc.a21l.elementoJerarquia.cu.IndicadorExpresionDto;
import es.dc.a21l.elementoJerarquia.cu.RelacionDto;
import es.dc.a21l.servicio.controladores.GenericAbstractController;

@Controller
@RequestMapping(value="/relaciones")
public class RelacionControladorSW extends GenericAbstractController {

	private GestorCURelacion gestorCURelacion;
	
	@Autowired
	public void setGestorCURelacion(GestorCURelacion gestorCURelacion) {
		this.gestorCURelacion = gestorCURelacion;
	}

	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}

	@RequestMapping(method=RequestMethod.POST,value="/guardaRelacion")
	public ResponseEntity<EncapsuladorPOSTSW<RelacionDto>> guardaRelacion(@RequestBody RelacionDto relacionDto){
		EncapsuladorErroresSW erroresSW= new EncapsuladorErroresSW();
		RelacionDto result= gestorCURelacion.guardar(relacionDto, erroresSW);
		return new ResponseEntity<EncapsuladorPOSTSW<RelacionDto>>(new EncapsuladorPOSTSW<RelacionDto>(result, erroresSW),responseHeaders, HttpStatus.OK);
	}
}
