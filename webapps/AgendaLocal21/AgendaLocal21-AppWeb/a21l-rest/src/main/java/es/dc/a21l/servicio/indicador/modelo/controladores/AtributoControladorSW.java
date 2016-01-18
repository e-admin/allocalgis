/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.indicador.modelo.controladores;

import java.util.HashSet;
import java.util.Set;

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
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.criterio.cu.GestorCUCriterio;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorExpresion;
import es.dc.a21l.elementoJerarquia.cu.GestorCURelacion;
import es.dc.a21l.fuente.cu.AtributoDto;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.GestorCUAtributo;
import es.dc.a21l.fuente.cu.GestorCUAtributoFuenteDatos;
import es.dc.a21l.fuente.cu.GestorCUTablaFuenteDatos;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.servicio.controladores.GenericAbstractController;


/*
 * SERVICIO WEB REST
 */

@Controller
@RequestMapping(value = "/atributos")
public class AtributoControladorSW extends GenericAbstractController {
	private static final Logger log = LoggerFactory.getLogger(AtributoControladorSW.class);
	private static final String NO_PARAMETRO = "no";
	private GestorCUAtributo gestorCUAtributo;
	private GestorCUAtributoFuenteDatos gestorCUAtributoFuenteDatos;
	private GestorCUTablaFuenteDatos gestorCUTablaFuenteDatos;
	private GestorCUIndicadorExpresion gestorCUIndicadorExpresion;
	private GestorCURelacion gestorCURelacion;
	private GestorCUCriterio gestorCUCriterio;
	
	@Autowired
	public void setGestorCUIndicadorExpresion(GestorCUIndicadorExpresion gestorCUIndicadorExpresion) {
		this.gestorCUIndicadorExpresion = gestorCUIndicadorExpresion;
	}
	
	@Autowired
	public void setGestorCUAtributofuenteDatos(GestorCUAtributoFuenteDatos gestorCUAtributoFuenteDatos) {
		this.gestorCUAtributoFuenteDatos = gestorCUAtributoFuenteDatos;
	}
	
	@Autowired
	public void setGestorCUTablaFuenteDatos(GestorCUTablaFuenteDatos gestorCUTablaFuenteDatos) {
		this.gestorCUTablaFuenteDatos = gestorCUTablaFuenteDatos;
	}
	
	@Autowired
	public void setGestorCUAtributo(GestorCUAtributo gestorCUAtributo) {
		this.gestorCUAtributo = gestorCUAtributo;
	}
	
	@Autowired
	public void setGestorCURelacion(GestorCURelacion gestorCURelacion) {
		this.gestorCURelacion = gestorCURelacion;
	}
	
	@Autowired
	public void setGestorCUCriterio(GestorCUCriterio gestorCUCriterio) {
		this.gestorCUCriterio = gestorCUCriterio;
	}
	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	@RequestMapping(method = RequestMethod.GET, value = "/atributo/{id}")
	public ResponseEntity<AtributoDto> cargaPorId(@PathVariable("id") Long id) {
		return new ResponseEntity<AtributoDto>(gestorCUAtributo.carga(id), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/atributo")
	public ResponseEntity<EncapsuladorPOSTSW> guardar(@RequestBody AtributoDto atributoDto,HttpServletRequest request) {
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		atributoDto = gestorCUAtributo.guarda(atributoDto, errores);
		EncapsuladorPOSTSW<AtributoDto> encapsulador=new EncapsuladorPOSTSW<AtributoDto>(atributoDto, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/borrarListaAtributos/{id}")
	public ResponseEntity<AtributoDto> borrarListaAtributosIndicador(@PathVariable("id") Long id) {
		EncapsuladorListSW<AtributoDto> listaAtributos = gestorCUAtributo.cargaPorIndicador(id);
		AtributoDto atributoDto = new AtributoDto();
		Set<Long> listaAtributosFuenteDatosBorrar = new HashSet<Long>();
		Set<Long> listaIndicadoresExpresionBorrar = new HashSet<Long>();
		Set<Long> listaRelacionBorrar = new HashSet<Long>();
		Set<Long> listaTablasBorrar = new HashSet<Long>();
		Set<Long> listaAtributosBorrar = new HashSet<Long>();
		
		//Primero borramos los criterios
		gestorCUCriterio.borraPorIdIndicador(id);
		
		for ( AtributoDto attr : listaAtributos ) {
			listaAtributosBorrar.add(attr.getId());
			if ( attr.getColumna()!=null) {
				listaAtributosFuenteDatosBorrar.add(attr.getColumna().getId());
				listaTablasBorrar.add(attr.getColumna().getTabla().getId());
			}
			if ( attr.getIndicadorExpresion()!=null)
				listaIndicadoresExpresionBorrar.add(attr.getIndicadorExpresion().getId());
			
			if ( attr.getRelacion()!=null)
				listaRelacionBorrar.add(attr.getRelacion().getId());
		}
		for ( Long idAtributo : listaAtributosBorrar ) {
			gestorCUAtributo.borra(idAtributo,null);
		}
		for ( Long idRel : listaRelacionBorrar ) {
			gestorCURelacion.borra(idRel);
		}
		for ( Long idIndiExpre : listaIndicadoresExpresionBorrar ) {
			gestorCUIndicadorExpresion.borra(idIndiExpre);
		}
		for ( Long idAttr : listaAtributosFuenteDatosBorrar ) {
			gestorCUAtributoFuenteDatos.borra(idAttr);
		}
		for ( Long idTabla : listaTablasBorrar ) {
			gestorCUTablaFuenteDatos.borra(idTabla, null);
		}
		
		ResponseEntity<AtributoDto> respuesta = new ResponseEntity<AtributoDto>(atributoDto, responseHeaders, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/atributos/{idindicador}")
	public ResponseEntity<EncapsuladorListSW<AtributoDto>> cargaPorIdIndicador(@PathVariable("idindicador") Long id) {
		return new ResponseEntity<EncapsuladorListSW<AtributoDto>>(gestorCUAtributo.cargaPorIndicador(id), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/datosIndicador/{idindicador}/{tipoFecha}")
	public ResponseEntity<AtributosMapDto> cargaDatosIndicador(@RequestBody EncapsuladorListSW<AtributoDto> listaAtributos,@PathVariable("idindicador") Long id,@PathVariable("tipoFecha") String tipoFecha, HttpServletRequest request) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		
		EncapsuladorListSW<AtributoDto> listaAtributos2 = new EncapsuladorListSW<AtributoDto>(); 
		for ( AtributoDto attr : listaAtributos ) {
			AtributoFuenteDatosDto columna = attr.getColumna();
			TiposFuente tipo = null;
			if ( columna != null ) {
    			TablaFuenteDatosDto tabla = columna.getTabla();
    			tipo = tabla.getFuente().getTipo();
    		} else {
    			TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
    			//Un tipo de fuente que no sea csv ni gml ni shapefile
    			tipo = TiposFuente.ODBC;
    		}
			String path;
			if ( tipo.equals(TiposFuente.CSV))
				path = getServicioConfiguracionGeneral().getPathCsv();
			else if ( tipo.equals(TiposFuente.GML))
				path = getServicioConfiguracionGeneral().getPathGml();
			else if ( tipo.equals(TiposFuente.SHAPEFILE))
				path = getServicioConfiguracionGeneral().getPathShapefile();
			else
				path = null;
			
			String realPath = getPathRealAplicacion(request, path);
			AtributoDto attrCopia = new AtributoDto();
			attrCopia.setColumna(attr.getColumna());
			attrCopia.setIndicadorExpresion(attr.getIndicadorExpresion());
			attrCopia.setRelacion(attr.getRelacion());
			attrCopia.setEsAmbito(attr.getEsAmbito());
			attrCopia.setEsMapa(attr.getEsMapa());
			attrCopia.setId(attr.getId());
			attrCopia.setMostrar(attr.getMostrar());
			attrCopia.setNombre(attr.getNombre());
			attrCopia.setOrdenVisualizacion(attr.getOrdenVisualizacion());
			attrCopia.setPath(realPath);
			listaAtributos2.add(attrCopia);
		}
		
		ResponseEntity<AtributosMapDto> resp = new ResponseEntity<AtributosMapDto>(gestorCUAtributo.cargarDatosIndicador(listaAtributos2, caracterSeparador, tipoFecha, getServicioConfiguracionGeneral().getMapaPathTiposFuente()), responseHeaders, HttpStatus.OK);
		return resp; 
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/datosIndicadorParametro/{idindicador}/{parametro}")
	public ResponseEntity<AtributosMapDto> cargaDatosIndicadorConParametro(@RequestBody EncapsuladorListSW<AtributoDto> listaAtributos,@PathVariable("idindicador") Long id, @PathVariable("parametro") String parametro, HttpServletRequest request) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		
		EncapsuladorListSW<AtributoDto> listaAtributos2 = new EncapsuladorListSW<AtributoDto>();
		int i = 0;
		boolean campoCorrecto = false;
		//Si no hay parametro seleccionado, busco cual es
		
		if ( parametro.equals(NO_PARAMETRO) ) {
			
			//Si la longitud de listaAtributos se alcanza sin obtener un parámetro no se puede seguir evaluando
			
			while  (!campoCorrecto && (!esLongituMaximaAtributos(i,listaAtributos))) {
				if ( listaAtributos.get(i).getEsMapa() || listaAtributos.get(i).getEsAmbito() ) {
					i++;
					continue;
				}
				if (((listaAtributos.get(i).getColumna()==null && listaAtributos.get(i).getIndicadorExpresion()!=null) || (listaAtributos.get(i).getColumna()!=null && listaAtributos.get(i).getColumna().getTipoAtributo()==TipoAtributoFD.VALORFDNUMERICO)) && listaAtributos.get(i).getRelacion()==null) {
					if ( listaAtributos.get(i).getMostrar() )
						parametro = listaAtributos.get(i).getNombre();
					else {
						i++;
						continue;
					}
				} else {
					i++;
					continue;
				}
				campoCorrecto = true;
			}
		}
		
		//Obtengo la lista de columnas y sus datos completos y filtrados por los criterios
		for ( AtributoDto attr : listaAtributos ) {
			AtributoFuenteDatosDto columna = attr.getColumna();
			String path;
			if ( columna == null )
    			path = null;
    		else {
				TablaFuenteDatosDto tabla = columna.getTabla();
				TiposFuente tipo = tabla.getFuente().getTipo();
				if ( tipo.equals(TiposFuente.CSV))
					path = getServicioConfiguracionGeneral().getPathCsv();
				else if ( tipo.equals(TiposFuente.GML))
					path = getServicioConfiguracionGeneral().getPathGml();
				else if ( tipo.equals(TiposFuente.SHAPEFILE))
					path = getServicioConfiguracionGeneral().getPathShapefile();
				else
					path = null;
    		}
			
			String realPath = getPathRealAplicacion(request, path);
			AtributoDto attrCopia = new AtributoDto();
			attrCopia.setColumna(attr.getColumna());
			attrCopia.setIndicadorExpresion(attr.getIndicadorExpresion());
			attrCopia.setRelacion(attr.getRelacion());
			attrCopia.setEsAmbito(attr.getEsAmbito());
			attrCopia.setEsMapa(attr.getEsMapa());
			attrCopia.setId(attr.getId());
			attrCopia.setMostrar(attr.getMostrar());
			attrCopia.setNombre(attr.getNombre());
			attrCopia.setOrdenVisualizacion(attr.getOrdenVisualizacion());
			attrCopia.setPath(realPath);
			listaAtributos2.add(attrCopia);
		}
		
		ResponseEntity<AtributosMapDto> resp = new ResponseEntity<AtributosMapDto>(gestorCUAtributo.cargarDatosIndicador(listaAtributos2, caracterSeparador,null,getServicioConfiguracionGeneral().getMapaPathTiposFuente()), responseHeaders, HttpStatus.OK);
		
		//Una vez tengo todos los datos, me quedo con la columna parametro y la columna ambito.
		//Incorporación: necesitamos hacer llegar la columna mapa al controlador para poder obtener los datos.
		AtributosMapDto datosFiltrados = new AtributosMapDto();
		for ( AtributoDto attr : listaAtributos ) {
			if ( attr.getNombre().equals(parametro) || attr.getEsAmbito() || attr.getEsMapa()) {
				datosFiltrados.setValor(attr.getNombre(), resp.getBody().getContenido().get(attr.getNombre()));
			}
		}
		ResponseEntity<AtributosMapDto> respuesta = new ResponseEntity<AtributosMapDto>(datosFiltrados, responseHeaders, HttpStatus.OK);
		return respuesta; 
	}
	
	/*
	 * Método que comprueba si hemos alcanzado la longitud máxima en una lista de AtributosDto
	 */
	private boolean esLongituMaximaAtributos(int posicion, EncapsuladorListSW<AtributoDto> listaAtributos) {
		
		boolean bolEsMaxima=false;
		
		if (posicion > (listaAtributos.size()-1)) {
			bolEsMaxima=true;
		}
		
		return bolEsMaxima;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/datosMapaIndicador/{idindicador}")
	public ResponseEntity<AtributosMapDto> cargaDatosMapaIndicador(@RequestBody EncapsuladorListSW<AtributoDto> listaAtributos,@PathVariable("idindicador") Long id) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		
		EncapsuladorListSW<AtributoDto> listaAtributos2 = new EncapsuladorListSW<AtributoDto>(); 
		for ( AtributoDto attr : listaAtributos ) {
			AtributoFuenteDatosDto columna = attr.getColumna();
			TiposFuente tipo;
			if ( columna != null ) {
    			TablaFuenteDatosDto tabla = columna.getTabla();
    			tipo = tabla.getFuente().getTipo();
    		} else {
    			TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
    			//Un tipo de fuente que no sea csv ni gml ni shapefile
    			tipo = TiposFuente.ODBC;
    		}
			
			String path;
			if ( tipo.equals(TiposFuente.CSV))
				path = getServicioConfiguracionGeneral().getPathCsv();
			else if ( tipo.equals(TiposFuente.GML))
				path = getServicioConfiguracionGeneral().getPathGml();
			else if ( tipo.equals(TiposFuente.SHAPEFILE))
				path = getServicioConfiguracionGeneral().getPathShapefile();
			else
				path = null;
			AtributoDto attrCopia = new AtributoDto();
			attrCopia.setColumna(attr.getColumna());
			attrCopia.setIndicadorExpresion(attr.getIndicadorExpresion());
			attrCopia.setEsAmbito(attr.getEsAmbito());
			attrCopia.setEsMapa(attr.getEsMapa());
			attrCopia.setId(attr.getId());
			attrCopia.setMostrar(attr.getMostrar());
			attrCopia.setNombre(attr.getNombre());
			attrCopia.setOrdenVisualizacion(attr.getOrdenVisualizacion());
			attrCopia.setPath(path);
			listaAtributos2.add(attrCopia);
		}
		
		ResponseEntity<AtributosMapDto> resp = new ResponseEntity<AtributosMapDto>(gestorCUAtributo.cargarDatosMapaIndicador(listaAtributos2, caracterSeparador, getServicioConfiguracionGeneral().getMapaPathTiposFuente()), responseHeaders, HttpStatus.OK);
		return resp; 
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/mapaIndicador/{idindicador}")
	public ResponseEntity<AtributosMapDto> cargaMapaIndicador(@RequestBody EncapsuladorListSW<AtributoDto> listaAtributos,@PathVariable("idindicador") Long id) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		
		EncapsuladorListSW<AtributoDto> listaAtributos2 = new EncapsuladorListSW<AtributoDto>(); 
		for ( AtributoDto attr : listaAtributos ) {
			AtributoFuenteDatosDto columna = attr.getColumna();
			TiposFuente tipo;
			if ( columna != null ) {
    			TablaFuenteDatosDto tabla = columna.getTabla();
    			tipo = tabla.getFuente().getTipo();
    		} else {
    			TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
    			//Un tipo de fuente que no sea csv ni gml ni shapefile
    			tipo = TiposFuente.ODBC;
    		}
			String path;
			if ( tipo.equals(TiposFuente.CSV))
				path = getServicioConfiguracionGeneral().getPathCsv();
			else if ( tipo.equals(TiposFuente.GML))
				path = getServicioConfiguracionGeneral().getPathGml();
			else if ( tipo.equals(TiposFuente.SHAPEFILE))
				path = getServicioConfiguracionGeneral().getPathShapefile();
			else
				path = null;
			AtributoDto attrCopia = new AtributoDto();
			attrCopia.setColumna(attr.getColumna());
			attrCopia.setEsAmbito(attr.getEsAmbito());
			attrCopia.setEsMapa(attr.getEsMapa());
			attrCopia.setId(attr.getId());
			attrCopia.setMostrar(attr.getMostrar());
			attrCopia.setNombre(attr.getNombre());
			attrCopia.setOrdenVisualizacion(attr.getOrdenVisualizacion());
			attrCopia.setPath(path);
			listaAtributos2.add(attrCopia);
		}
		
		ResponseEntity<AtributosMapDto> resp = new ResponseEntity<AtributosMapDto>(gestorCUAtributo.cargarMapaIndicador(listaAtributos2, caracterSeparador), responseHeaders, HttpStatus.OK);
		return resp; 
	}
}
