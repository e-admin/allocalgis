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
import es.dc.a21l.elementoJerarquia.cu.IndicadorExpresionDto;
import es.dc.a21l.servicio.controladores.GenericAbstractController;

@Controller
@RequestMapping(value="/indicadorExpresiones")
public class IndicadorExpresionControladorSW extends GenericAbstractController {

	private GestorCUIndicadorExpresion gestorCUIndicadorExpresion;
	
	@Autowired
	public void setGestorCUIndicadorExpresion(
			GestorCUIndicadorExpresion gestorCUIndicadorExpresion) {
		this.gestorCUIndicadorExpresion = gestorCUIndicadorExpresion;
	}

	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}

	@RequestMapping(method=RequestMethod.POST,value="/guardaIndicadorExpresion")
	public ResponseEntity<EncapsuladorPOSTSW<IndicadorExpresionDto>> guardaIndicadorExpresion(@RequestBody IndicadorExpresionDto indicadorExpresionDto){
		
		String caracterSeparadorDecimales = getServicioConfiguracionGeneral().getCaracterSeparadorDecimales();
		
		EncapsuladorErroresSW erroresSW= new EncapsuladorErroresSW();
		IndicadorExpresionDto result= gestorCUIndicadorExpresion.guardar(indicadorExpresionDto, erroresSW, caracterSeparadorDecimales);
		return new ResponseEntity<EncapsuladorPOSTSW<IndicadorExpresionDto>>(new EncapsuladorPOSTSW<IndicadorExpresionDto>(result, erroresSW),responseHeaders, HttpStatus.OK);
	}
}
