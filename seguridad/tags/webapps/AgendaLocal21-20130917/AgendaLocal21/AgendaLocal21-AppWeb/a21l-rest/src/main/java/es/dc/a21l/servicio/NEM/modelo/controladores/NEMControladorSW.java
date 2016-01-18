/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.NEM.modelo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.dc.a21l.NEM.cu.AtributoNEMDto;
import es.dc.a21l.NEM.cu.GestorCUAtributoNEM;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;

@Controller
@RequestMapping(value="/NEM")
public class NEMControladorSW {
	
	private GestorCUAtributoNEM gestorCUAtributoNEM;
	
	@Autowired
	public void setGestorCUAtributoNEM(GestorCUAtributoNEM gestorCUAtributoNEM) {
		this.gestorCUAtributoNEM = gestorCUAtributoNEM;
	}

	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	
	@RequestMapping(method=RequestMethod.GET,value="/cargaTodos")
	public ResponseEntity<EncapsuladorListSW<AtributoNEMDto>> cargaTodos(){
		return new ResponseEntity<EncapsuladorListSW<AtributoNEMDto>>(gestorCUAtributoNEM.cargaTodos(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/cargaPorListaIds")
	public ResponseEntity<EncapsuladorListSW<AtributoNEMDto>> cargaListaIds(@RequestBody EncapsuladorListSW<Long> ids){
		return new ResponseEntity<EncapsuladorListSW<AtributoNEMDto>>(gestorCUAtributoNEM.cargaPorListaIds(ids),responseHeaders, HttpStatus.OK);
	}
}
