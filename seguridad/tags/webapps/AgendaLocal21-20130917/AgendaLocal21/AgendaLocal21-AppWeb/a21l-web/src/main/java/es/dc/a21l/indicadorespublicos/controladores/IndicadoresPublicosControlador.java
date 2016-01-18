/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.indicadorespublicos.controladores;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorFileSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.base.utils.enumerados.TipoFecha;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.base.utils.enumerados.TiposLetra;
import es.dc.a21l.base.utils.excepciones.PublicacionWebDeshabilitadaException;
import es.dc.a21l.comun.controladores.GenericAbstractController;
import es.dc.a21l.comun.utils.UrlConstructorSW;
import es.dc.a21l.comun.utils.UtilEncriptar;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.elementoJerarquia.cu.CapaBaseDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.elementoJerarquia.cu.ParDiagramaBarrasDto;
import es.dc.a21l.fuente.cu.AtributoDto;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributoHistoricoDto;
import es.dc.a21l.fuente.cu.AtributoMapDto;
import es.dc.a21l.fuente.cu.AtributoValoresDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.historico.cu.HistoricoDto;
import es.dc.a21l.indicador.controlador.Estilos;
import es.dc.a21l.publicacion.cu.PublicacionDto;
import es.dc.a21l.usuario.cu.UsuarioDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaBarrasDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaSectoresDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionMapaDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionTablaDto;
import es.dc.a21l.visualizacion.cu.RangosVisualizacionMapaDto;

@Controller
@RequestMapping(value = "/indicadorPublico.htm")
public class IndicadoresPublicosControlador extends GenericAbstractController {
	private static final Logger log = LoggerFactory.getLogger(IndicadoresPublicosControlador.class);
	private static final String TAMANHO_FUENTE_DEFECTO_EXPORTAR = "12";
	private static final String NO_PARAMETRO = "no";
	private static final String TAMANHO_FUENTE_DEFECTO = "12.0";
	private static final String TAMANHO_COLUMNA_DEFECTO = "40";
	private static final String COLOR_DEFECTO= "#00632E";
	private static final String COLORES_DEFECTO = "||rgb(241, 253, 201)||rgb(214, 235, 142)||rgb(156, 181, 69)||rgb(96, 108, 55)||rgb(59, 64, 43)";
	private static final String FUENTE_DEFECTO= "Arial";
	private static final String TIPO_FECHA_DEFECTO= TipoFecha.CORTA.getDescripcion();
	private static final String DIAMETRO_DEFECTO = "550";
	private static final String ERROR_NO_MAPA_SHAPEFILE = "_ERROR_NO_MAPA_SHAPEFILE_";

	//Variable global que contendrá los valores del ámbito con los que se realizarán los diagramas de barras y sectores 
	ArrayList<Integer> listaPosicionesAmbitoDiagrama=null;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ModelAttribute(value = "listaFuentes")
	public List<FuenteDto> getListaFuentes() {
		List<FuenteDto> items = new ArrayList<FuenteDto>();
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("listaFuentes");

		ResponseEntity<EncapsuladorListSW> lista = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class);
		EncapsuladorListSW<FuenteDto> listaFuentes = lista.getBody();
		for (FuenteDto fuente : listaFuentes)
			items.add(fuente);
		return items;
	}

	@RequestMapping(method = RequestMethod.GET)
	/*
	 * Método que se ejecuta al seleccionar el enlace de un Indicador Público (Parte Web)
	 * El identificador del indicador tiene que ser de tipo String.
	 * Internamente se transformará a Long
	 */
	public String visualizarIndicador(@RequestParam(value = "id") String id, 
			Model model,
			HttpServletRequest request, 			
			String error,
			HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, Exception {
		
		UrlConstructorSW urlCat = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		urlCat.setParametroCadena("publicacion").setParametroCadena("obtenerEstado");
		
		PublicacionDto publi = getRestTemplate().getForEntity(urlCat.getUrl(), PublicacionDto.class).getBody();
		
		//SI la publicacion no existe o no esta habilitada => no esta habilitada
		if ( publi == null || publi.getPublicacionWebHabilitada() == null || !publi.getPublicacionWebHabilitada()) 
		{
			throw new PublicacionWebDeshabilitadaException();
		}
		Long idIndicador;
		try {
			idIndicador = Long.valueOf(UtilEncriptar.desencripta(id));
		} catch (Exception e) {
			idIndicador = 0L;
		}
		
		return visualizarIndicadorTabla(idIndicador, model, request, null, error, response);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarIndicadorTabla" })
	public String visualizarIndicadorTabla(@RequestParam(value = "id") Long idIndicador, 
			Model model,
			HttpServletRequest request, 
			HistoricoDto historico, 
			String error, 
			HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, Exception {
		
		//Long idIndicador;
		//try {
		//	idIndicador = Long.valueOf(UtilEncriptar.desencripta(id));
		//} catch (Exception e) {
		//	idIndicador = 0L;
		//}
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(),IndicadorDto.class).getBody();

		if ( indicadorDto.getPteAprobacion() || !indicadorDto.getPublicadoEnWeb()) {
			return irSinPermisos(request, response);
		}
		
		model.addAttribute("indicadorDto", indicadorDto);
		model.addAttribute("idIndicador", idIndicador);
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("usuarios").setParametroCadena("cargaUsuarioPorLogin").setParametroCadena(indicadorDto.getLoginCreador());
		UsuarioDto usuarioDto = getRestTemplate().getForEntity(url.getUrl(),UsuarioDto.class).getBody();
		
		//Estilo de visualizacion
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaEstiloVisualizacionTabla");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(usuarioDto.getId());
		
		EstiloVisualizacionTablaDto estilo = getRestTemplate().getForEntity(url.getUrl(), EstiloVisualizacionTablaDto.class).getBody();
		
		if ( estilo!=null && estilo.getId()>0)
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);
		
		//Preparamos los datos para su visualizacion
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());
		EncapsuladorListSW<AtributoDto> listaColumnasMostrar = new EncapsuladorListSW<AtributoDto>();
		
		Object parametro;
		if ( estilo.getTipoFecha()==null)
			parametro = "no";
		else
			parametro = estilo.getTipoFecha().getDescripcion();
		
		int numFilas = 0;
		AtributosMapDto datos = new AtributosMapDto();
		AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
		if (historico != null && historico.getDatosDto() != null) {
			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));
		} else {
			datos = obtenerDatosIndicador(indicadorDto.getId(), "datosIndicador", parametro, listaColumnas);
		}
		
		if (historico != null && historico.getDatosDto() != null) {			
			
			EncapsuladorListSW<AtributoDto> listaColumnasHistorico = obtenerListaColumnasHistorico(datosHistoricosTemporal);
			
			//Se obtienen las columnas que se deben mostrar en el histórico de la estructura datosHistoricos
			Set<String> setAtributosHistorico = datosHistoricosTemporal.getContenido().keySet();
			Iterator<String> itAtributosHistorico = setAtributosHistorico.iterator();
			String strAtributoHistorico = null;
			AtributoValoresDto objAtributoValoresColumna = null;
			AtributoDto objAtributoMostrar = null;
			while (itAtributosHistorico.hasNext()) {
				strAtributoHistorico = (String)itAtributosHistorico.next();
				objAtributoValoresColumna = datosHistoricosTemporal.getContenido().get(strAtributoHistorico);
				if (objAtributoValoresColumna!=null && objAtributoValoresColumna.getAtributo()!=null) {
					objAtributoMostrar = convertirAtributoHistoricoEnDto(objAtributoValoresColumna.getAtributo());
					if (objAtributoMostrar!=null && objAtributoMostrar.getMostrar()) {
						listaColumnasMostrar.add(objAtributoMostrar);
					}
				}
			}
			//Finalmente se convierte la estructura temporal datosHistoricos en una estructura del tipo AtributosMapDto
			datos = convertirEnAtributosMapDto (datosHistoricosTemporal);
			
			/* Se obtienen las columnas obtenidas y se construye el resultado FINAL manteniendo el orden de las columnas
			 * y no visualizando la columna GEOM.
			 */
			LinkedHashMap<String,ValorFDDto> mapaColumnasInicial = datos.getContenido();
			LinkedHashMap<String,ValorFDDto> mapaColumnasRtdo = new LinkedHashMap<String,ValorFDDto>();
			for (AtributoDto atributoOrden : listaColumnasHistorico) {			
				if (mapaColumnasInicial.containsKey(atributoOrden.getNombre()) && (atributoOrden.getMostrar()) && !atributoOrden.getEsMapa()) {
					mapaColumnasRtdo.put(atributoOrden.getNombre(), mapaColumnasInicial.get(atributoOrden.getNombre()));
				}
			}
			
			datos.setContenido(mapaColumnasRtdo);
			mapaColumnasRtdo=null;
			
		} else {
			/* Se obtienen las columnas obtenidas y se construye el resultado FINAL manteniendo el orden de las columnas
			 * y no incluyendo la columna GEOM que fue necesaria incorporar para que participara del resultado de las relaciones.
			 */
			LinkedHashMap<String,ValorFDDto> mapaColumnasInicial = datos.getContenido();
			LinkedHashMap<String,ValorFDDto> mapaColumnasRtdo = new LinkedHashMap<String,ValorFDDto>();
			for (AtributoDto atributoOrden : listaColumnas) {			
				if (mapaColumnasInicial.containsKey(atributoOrden.getNombre()) && (atributoOrden.getMostrar()) && !atributoOrden.getEsMapa()) {
					mapaColumnasRtdo.put(atributoOrden.getNombre(), mapaColumnasInicial.get(atributoOrden.getNombre()));
				}
			}		
			datos.setContenido(mapaColumnasRtdo);
			mapaColumnasRtdo=null;
			
			for (AtributoDto attr : listaColumnas) {
				if (attr.getMostrar())
					listaColumnasMostrar.add(attr);
			}
		}
				
		//OBTENER NUMERO DE FILAS
		Set<String> claves = datos.getContenido().keySet();
		String primeraClave = ""; 	
		if (claves.iterator().hasNext()) {
			primeraClave = claves.iterator().next();
			if ( datos.getContenido().get(primeraClave)!=null )
				numFilas = datos.getContenido().get(primeraClave).getValores().size() - 1;
			else
				numFilas = 0;
		} else {
			numFilas = 0;
		}
		Set<Entry<String, ValorFDDto>> datosRepresentar=datos.getContenido().entrySet();
		for(Entry<String, ValorFDDto> columna:datosRepresentar){
			for(EncapsuladorStringSW valor:columna.getValue().getValores()){
				valor.setTexto(quitarAcentos(valor.getTexto()));
			}
		}
		model.addAttribute("datos",datosRepresentar);		
		model.addAttribute("numFilas",numFilas);
		model.addAttribute("listaColumnas",listaColumnasMostrar);
		
		
		mostrarHistorico(idIndicador,model);
		
		//Error fichero exportado
		if ( error!=null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}
		
		return "indicadorPublicadoTablaWebTile";
	}

	private static String quitarAcentos(String input) {
	    // Cadena de caracteres original a sustituir.
	    String []original = {"á","é","í","ó","ú","Á","É","Í","Ó","Ú","ñ","Ñ"};
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String [] ascii = {"&aacute;","&eacute;","&iacute;","&oacute;","&uacute;","&Aacute;","&Eacute;","&Iacute;","&Oacute;","&Uacute;","&ntilde;","&Ntilde;"};
	    String output = input;
	    for (int i=0; i<original.length; i++) {
	        // Reemplazamos los caracteres especiales.
	       output = output.replace(original[i], ascii[i]);
	    }//for i
	    return output;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarIndicadorMapa" })
	public String visualizarIndicadorMapa(@RequestParam(value = "id") Long idIndicador, 
			@RequestParam(value = "param", required = false) String parametro,
			@RequestParam(value = "idHistorico", required = false) String idHistorico,
			Model model, 
			HttpServletRequest request,
			HistoricoDto historico,
			String error, 
			HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, Exception {
		
		//Long idIndicador;		
		//try {
		//	idIndicador = Long.valueOf(UtilEncriptar.desencripta(id));
		//} catch (Exception e)  {
		//	idIndicador = 0L;
		//}
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(),IndicadorDto.class).getBody();

		if ( indicadorDto.getPteAprobacion() || !indicadorDto.getPublicadoEnWeb()) 
		{
			return irSinPermisos(request, response);
		}
		
		model.addAttribute("indicadorDto", indicadorDto);
		model.addAttribute("idIndicador", idIndicador);
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("usuarios").setParametroCadena("cargaUsuarioPorLogin").setParametroCadena(indicadorDto.getLoginCreador());
		UsuarioDto usuarioDto = getRestTemplate().getForEntity(url.getUrl(),UsuarioDto.class).getBody();
		
		//Estilo de visualizacion
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaEstiloVisualizacionMapa");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(usuarioDto.getId());
		
		EstiloVisualizacionMapaDto estilo = getRestTemplate().getForEntity(url.getUrl(), EstiloVisualizacionMapaDto.class).getBody();
		
		//Obtenemos las capas base
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaCapasBaseMapa");
		List<CapaBaseDto>listaCapasBase=new ArrayList<CapaBaseDto>();
		listaCapasBase.addAll(getRestTemplate().getForEntity(url.getUrl(),
				EncapsuladorListSW.class).getBody());
		model.addAttribute("listaCapasBase", listaCapasBase);
		
		//Rangos de Estilo de visualizacion de Mapa
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaRangosVisualizacionMapa");
		url.setParametroCadena(estilo.getId());

		EncapsuladorListSW<RangosVisualizacionMapaDto> listaRangos = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();	
				
		if (estilo!=null && estilo.getId()>0)
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);
		
		model.addAttribute("color_defecto", COLOR_DEFECTO);
		
		List<RangosVisualizacionMapaDto> rangos = listaRangos.getLista();
		
		model.addAttribute("rangos",rangos);
		model.addAttribute("numRangos",rangos.size());

		//Preparamos los datos para su visualizacion
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());

		// Comprobamos si el indicador tiene un campo mapa y un campo numerico
		// al menos para poder visualizar el mapa
		boolean tieneMapa = false;
		boolean tieneNumerico = false;
		AtributoFuenteDatosDto columnaAtributoMapa = null;
		for (AtributoDto attr : listaColumnas) {
			// Campo mapa
			if (attr.getEsMapa()) {
				tieneMapa = true;
				columnaAtributoMapa = attr.getColumna();
			}
			// Campo numerico
			if (attr.getColumna() != null && attr.getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO)
				tieneNumerico = true;
			// Formula nos vale como numerico
			if (attr.getColumna() == null && attr.getIndicadorExpresion() != null)
				tieneNumerico = true;
			if (tieneNumerico && tieneMapa)
				break;
		}

		if (!tieneMapa) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas.noMapa");
			return visualizarIndicadorTabla(idIndicador, model, request, historico,error, response);
		}

		if (!tieneNumerico) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error, response);
		}
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("atributos");
		url.setParametroCadena("datosIndicadorParametro");
		url.setParametroCadena(indicadorDto.getId());
		
		if ( parametro == null || parametro.equals("")) {
			parametro = NO_PARAMETRO;
		}
		url.setParametroCadena(parametro);
		
		AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
		AtributosMapDto datos = new AtributosMapDto();
		//CÓDIGO INCLUIDO EN ESTE CONTROLADOR, COPIADO DE INDICADOR CONTROLADOR
		if (idHistorico != null && !idHistorico.equals("0")) {
			if (historico.getDatosDto() == null) {
				url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("historico")
						.setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			}
			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));
			AtributosMapDto datosFromHistorico = convertirEnAtributosMapDto (datosHistoricosTemporal);//(AtributosMapDto) writeObject(decompress(historico.getDatosDto()));
			
			EncapsuladorListSW<AtributoDto> listaColumnasHistorico = obtenerListaColumnasHistorico(datosHistoricosTemporal);
			
			
			String ambito = "";
			datos = new AtributosMapDto();
			LinkedHashMap<String, ValorFDDto> eltosFromHistorico = new LinkedHashMap<String, ValorFDDto>();
			LinkedHashMap<String, ValorFDDto> mapaDatos = datosFromHistorico.getContenido();
			for (String key : mapaDatos.keySet()) {
				int i = 0;
				boolean campoCorrecto = false;
				while (!campoCorrecto) {
					if (listaColumnasHistorico.get(i).getEsMapa() || listaColumnasHistorico.get(i).getEsAmbito()) {
						if (listaColumnasHistorico.get(i).getEsAmbito()) {
							ambito = listaColumnasHistorico.get(i).getNombre();
							campoCorrecto = true;
						}
						i++;
						continue;
					}
				}
				if (parametro.equals(NO_PARAMETRO)) {
					campoCorrecto = false;
					while (!campoCorrecto) {
						if (((listaColumnasHistorico.get(i).getColumna() == null && listaColumnasHistorico.get(i).getIndicadorExpresion() != null) || (listaColumnasHistorico
								.get(i).getColumna() != null && listaColumnasHistorico.get(i).getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO))
								&& listaColumnasHistorico.get(i).getRelacion() == null) {
							parametro = listaColumnasHistorico.get(i).getNombre();
							campoCorrecto = true;
						} else {
							i++;
							continue;
						}
					}
				}

				if (key.equals(parametro)) {
					ValorFDDto datosParametro = mapaDatos.get(key);
					eltosFromHistorico.put(key, datosParametro);
				}
				if (key.equals(ambito)) {
					// añadir el ambito
					ValorFDDto datosParametro = mapaDatos.get(key);
					eltosFromHistorico.put(key, datosParametro);
				}
				datos.setContenido(eltosFromHistorico);
			}
			model.addAttribute("historico", historico);
		} else {
			datos = getRestTemplate().postForEntity(url.getUrl(),
					new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas), AtributosMapDto.class).getBody();
		}
				
		//datos = getRestTemplate().postForEntity(url.getUrl(),new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas),AtributosMapDto.class).getBody();
		
		//url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		//url.setParametroCadena("atributos");
		//url.setParametroCadena("mapaIndicador");
		//url.setParametroCadena(indicadorDto.getId());
		
		LinkedHashMap<String,ValorFDDto> columnaMapa = new LinkedHashMap<String,ValorFDDto>();
		AtributosMapDto datosMapa = new AtributosMapDto();
		if (historico != null && historico.getMapaDto() != null) {
			datosMapa = (AtributosMapDto) writeObject(decompress(historico.getMapaDto()));
		} else {
			columnaMapa.put(columnaAtributoMapa.getNombre(), datos.getContenido().get(columnaAtributoMapa.getNombre()));
			datosMapa.setContenido(columnaMapa);
		}

		//datosMapa = getRestTemplate().postForEntity(url.getUrl(),new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas),AtributosMapDto.class).getBody();
		
		datos.getContenido().remove(columnaAtributoMapa.getNombre());
		int numFilas = obtenerNumeroFilas(datos);
		
		List<AtributoDto> listaColumnasNoMapa = new ArrayList<AtributoDto>();
		if (historico != null && historico.getDatosDto() != null) {

			//Se obtienen las columnas que se deben mostrar en el histórico de la estructura datosHistoricos
			Set<String> setAtributosHistorico = datosHistoricosTemporal.getContenido().keySet();
			Iterator<String> itAtributosHistorico = setAtributosHistorico.iterator();
			String strAtributoHistorico = null;
			AtributoValoresDto objAtributoValoresColumna = null;
			AtributoDto objAtributoMostrar = null;
			while (itAtributosHistorico.hasNext()) {
				strAtributoHistorico = (String)itAtributosHistorico.next();
				objAtributoValoresColumna = datosHistoricosTemporal.getContenido().get(strAtributoHistorico);
				if (objAtributoValoresColumna!=null && objAtributoValoresColumna.getAtributo()!=null) {
					objAtributoMostrar = convertirAtributoHistoricoEnDto(objAtributoValoresColumna.getAtributo());
					if (objAtributoMostrar!=null && objAtributoMostrar.getMostrar() &&
							(!objAtributoMostrar.getEsMapa()) && (!objAtributoMostrar.getEsAmbito()) &&
							(((objAtributoMostrar.getColumna() == null && objAtributoMostrar.getIndicadorExpresion() != null) || 
							 (objAtributoMostrar.getColumna() != null && objAtributoMostrar.getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO)) && 
							  objAtributoMostrar.getRelacion() == null))  {
						listaColumnasNoMapa.add(objAtributoMostrar);
					}
					if(objAtributoMostrar.getEsAmbito()){
						model.addAttribute("columnaAmbito", datos.getContenido().get(objAtributoMostrar.getNombre()));
					}
				}
			}
			
		} else {
			for (AtributoDto col : listaColumnas) {
				if (col.getMostrar()
						&& !col.getEsMapa()
						&& !col.getEsAmbito()
						&& (((col.getColumna() == null && col.getIndicadorExpresion() != null) || (col.getColumna() != null && col
								.getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO)) && col.getRelacion() == null)){
					listaColumnasNoMapa.add(col);
				}
				if(col.getEsAmbito()){
					model.addAttribute("columnaAmbito", datos.getContenido().get(col.getNombre()));
				}
			}
		}
		
		if (listaColumnasNoMapa.size() <= 0) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error, response);
		}
		
		model.addAttribute("listaColumnas", listaColumnasNoMapa);
		
		model.addAttribute("parametro", parametro);
		model.addAttribute("numFilas",numFilas);
		model.addAttribute("datos",datos.getContenido().entrySet());
		model.addAttribute("mapa",datosMapa.getContenido().entrySet());
		//Mostrar histórico
		mostrarHistorico(idIndicador, model);
		
		//Error fichero exportado
		if ( error!=null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}
		
		return "indicadorPublicadoMapaWebTile";
	}
	
	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarIndicadorDiagramaBarras" })
	public String visualizarIndicadorDiagramaBarras(@RequestParam(value = "id") Long idIndicador, @RequestParam(value = "param", required = false) String parametro,
			@RequestParam(value = "idHistorico", required = false) String idHistorico, Model model, HttpServletRequest request, HistoricoDto historico, 
			String error, HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, Exception {
		
		//Long idIndicador;
		//try {
		//	idIndicador = Long.valueOf(UtilEncriptar.desencripta(id));
		//} catch (Exception e) {
		//	idIndicador = 0L;
		//}
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(),IndicadorDto.class).getBody();

		if ( indicadorDto.getPteAprobacion() || !indicadorDto.getPublicadoEnWeb()) {
			return irSinPermisos(request, response);
		}
		
		model.addAttribute("indicadorDto", indicadorDto);
		model.addAttribute("idIndicador", idIndicador);
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("usuarios").setParametroCadena("cargaUsuarioPorLogin").setParametroCadena(indicadorDto.getLoginCreador());
		UsuarioDto usuarioDto = getRestTemplate().getForEntity(url.getUrl(),UsuarioDto.class).getBody();
		
		//Estilo de visualizacion
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaEstiloVisualizacionDiagramaBarras");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(usuarioDto.getId());
		
		EstiloVisualizacionDiagramaBarrasDto estilo = getRestTemplate().getForEntity(url.getUrl(), EstiloVisualizacionDiagramaBarrasDto.class).getBody();
		
		if ( estilo!=null && estilo.getId()>0)			
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);
		
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());
		
		AtributoFuenteDatosDto columnaAtributoMapa = obtenerColumnaMapa(listaColumnas);
		
//		model.addAttribute("parametro", parametro);		
		model.addAttribute("elementosPagina", getServicioConfiguracionGeneral().getTamanhoPagina());
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("atributos");
		url.setParametroCadena("datosIndicadorParametro");
		url.setParametroCadena(indicadorDto.getId());
		
		parametro = calcularValorParametro(parametro, request, listaColumnas);		
		url.setParametroCadena(parametro);
		model.addAttribute("parametro", parametro);
				
//		if ( parametro == null || parametro.equals(""))
//			parametro = NO_PARAMETRO;
//		url.setParametroCadena(parametro);

		AtributosMapDto datos = new AtributosMapDto();
		AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
		if (idHistorico != null && !idHistorico.equals("0")) {
			
			if (historico.getDatosDto() == null) {
				url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("historico")
						.setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			}
			
			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));
			AtributosMapDto datosFromHistorico = convertirEnAtributosMapDto (datosHistoricosTemporal);//(AtributosMapDto) writeObject(decompress(historico.getDatosDto()));

			EncapsuladorListSW<AtributoDto> listaColumnasHistorico = obtenerListaColumnasHistorico(datosHistoricosTemporal);
			
			String ambito = "";
			datos = new AtributosMapDto();
			LinkedHashMap<String, ValorFDDto> eltosFromHistorico = new LinkedHashMap<String, ValorFDDto>();
			LinkedHashMap<String, ValorFDDto> mapaDatos = datosFromHistorico.getContenido();
			for (String key : mapaDatos.keySet()) {
				int i = 0;
				boolean campoCorrecto = false;
				while (!campoCorrecto) {
					if (listaColumnasHistorico.get(i).getEsMapa() || listaColumnasHistorico.get(i).getEsAmbito()) {
						if (listaColumnasHistorico.get(i).getEsAmbito()) {
							ambito = listaColumnasHistorico.get(i).getNombre();
							campoCorrecto = true;
						}
						i++;
						continue;
					}
				}
				if (!existeParametroEnListaColumnas(listaColumnasHistorico, parametro)) {
					parametro = NO_PARAMETRO;
				}
				if (parametro.equals(NO_PARAMETRO)) {
					campoCorrecto = false;
					while (!campoCorrecto) {
						if (((listaColumnasHistorico.get(i).getColumna() == null && listaColumnasHistorico.get(i).getIndicadorExpresion() != null) || (listaColumnasHistorico
								.get(i).getColumna() != null && listaColumnasHistorico.get(i).getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO))
								&& listaColumnasHistorico.get(i).getRelacion() == null) {
							parametro = listaColumnasHistorico.get(i).getNombre();
							campoCorrecto = true;
						} else {
							i++;
							continue;
						}
					}
				}

				if (key.equals(parametro)) {
					ValorFDDto datosParametro = mapaDatos.get(key);
					eltosFromHistorico.put(key, datosParametro);
				}
				if (key.equals(ambito)) {
					// añadir el ambito
					ValorFDDto datosParametro = mapaDatos.get(key);
					eltosFromHistorico.put(key, datosParametro);
				}
				datos.setContenido(eltosFromHistorico);
			}
			model.addAttribute("historico", historico);
		} else {
			datos = getRestTemplate().postForEntity(url.getUrl(),new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas),AtributosMapDto.class).getBody();
		}
		//datos = getRestTemplate().postForEntity(url.getUrl(),new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas),AtributosMapDto.class).getBody();
		
		if (columnaAtributoMapa!=null) {
			datos.getContenido().remove(columnaAtributoMapa.getNombre());
		}
		
		List<AtributoDto> listaColumnasNoMapa = obtenerListaColumnasNoMapa(
				historico, listaColumnas, datosHistoricosTemporal);

		if (listaColumnasNoMapa.size() <= 0) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error,response);
		}
		
		model.addAttribute("listaColumnas", listaColumnasNoMapa);
		
		AtributoFuenteDatosDto columnaAtributoAmbito=null;
		for(AtributoDto col:listaColumnas){
			if(col.getEsAmbito()){
				columnaAtributoAmbito=col.getColumna();
			}
		}
		//Realizo agrupacións cando o campo ámbito é o mesmo
		Set<Entry<String, ValorFDDto>> datosRepresentar=datos.getContenido().entrySet();
		List<EncapsuladorStringSW> listaValoresAmbito=null;
		List<EncapsuladorStringSW> listaValores=null;
		for(Entry<String, ValorFDDto> columna:datosRepresentar){
			if(columna.getKey().equals(columnaAtributoAmbito.getNombre())){
				listaValoresAmbito=columna.getValue().getValores();
			}else{
				listaValores=columna.getValue().getValores();
			}
		}
		
		List<ParDiagramaBarrasDto> listaOrdenada = obtenerListaOrdenada(listaValoresAmbito, listaValores);
		Collections.sort(listaOrdenada);
		
		List<EncapsuladorStringSW> listaValoresAmbitoFormateada=new ArrayList<EncapsuladorStringSW>();
		List<EncapsuladorStringSW> listaValoresFormateada=new ArrayList<EncapsuladorStringSW>();
		if(listaOrdenada!=null && listaOrdenada.size()>0){
			int i=0;
			while(i < listaOrdenada.size()){
				try{
					Float.parseFloat(listaOrdenada.get(i).getValor());
					break;
				}catch (Exception e) {
					i++;
				}
			}
			if(i < listaOrdenada.size()){
				String valorAmbito=listaOrdenada.get(i).getValorAmbito();
				Float acumulacion=Float.parseFloat(listaOrdenada.get(i).getValor());
				i++;
				while(i<listaOrdenada.size()){
					if(listaOrdenada.get(i).getValor()!=null  && listaOrdenada.get(i).getValorAmbito().equals(valorAmbito)){
						try{
							Float suma=Float.parseFloat(listaOrdenada.get(i).getValor());
							acumulacion+=suma;
							i++;
						}catch (Exception e) {
							i++;
						}
					}else{
						listaValoresAmbitoFormateada.add(new EncapsuladorStringSW(valorAmbito));
						listaValoresFormateada.add(new EncapsuladorStringSW(acumulacion.toString()));
						valorAmbito=listaOrdenada.get(i).getValorAmbito();
						try{
							acumulacion=Float.parseFloat(listaOrdenada.get(i).getValor());
							i++;
						}catch (Exception e) {
							acumulacion=0f;
							i++;
						}
					}
				}
				listaValoresAmbitoFormateada.add(new EncapsuladorStringSW(valorAmbito));
				listaValoresFormateada.add(new EncapsuladorStringSW(acumulacion.toString()));
			
			}
		}
		Entry<String, ValorFDDto> columnaAmbito=null;
		Entry<String, ValorFDDto> columnaNormal=null;
		for(Entry<String, ValorFDDto> columna:datosRepresentar){
			if(columna.getKey().equals(columnaAtributoAmbito.getNombre())){
				columna.getValue().setValores(listaValoresAmbitoFormateada);
				columnaAmbito=columna;
			}else{
				columna.getValue().setValores(listaValoresFormateada);
				columnaNormal=columna;
			}
		}
		
		datosRepresentar = new LinkedHashSet<Entry<String, ValorFDDto>>();
		datosRepresentar.add(columnaAmbito);
		datosRepresentar.add(columnaNormal);
		
//		int numFilas = obtenerNumeroFilas(datos);
		int numFilas = calcularNumFilasDatosRepresentar(datosRepresentar);

		model.addAttribute("numFilas",numFilas);
		model.addAttribute("numPaginasDiagramas", Math.ceil(numFilas/getServicioConfiguracionGeneral().getTamanhoPagina()));
		
		model.addAttribute("datos",datosRepresentar);
		
		//Mostrar histórico
		mostrarHistorico(idIndicador, model);
		
		//Error fichero exportado
		if ( error!=null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}
		
		return "indicadorPublicadoBarrasWebTile";
	}
	
	/**
	 * Método que visualiza el diagrama de sectores de un indicador
	 * @param idIndicador identificador del indicador del que se desea ver el diagrama
	 * @param parametro valor del parámetro del cual se quiere hacer la representación
	 * @param idHistorico identificador del histórico en caso de que se esté consultando una versión histórica
	 * @param model
	 * @param request
	 * @param historico
	 * @param error
	 * @return mapping "visualizarIndicadorDiagramaSectoresTile"
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarIndicadorDiagramaSectores" })
	public String visualizarIndicadorDiagramaSectores(@RequestParam(value = "id") Long idIndicador, 
			@RequestParam(value = "param", required = false) String parametro, @RequestParam(value = "idHistorico", required = false) String idHistorico,
			Model model, HttpServletRequest request, HistoricoDto historico, String error, HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, Exception {
		
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(),IndicadorDto.class).getBody();

		if ( indicadorDto.getPteAprobacion() || !indicadorDto.getPublicadoEnWeb()) {
			return irSinPermisos(request, response);
		}		
		model.addAttribute("indicadorDto", indicadorDto);
		model.addAttribute("idIndicador", idIndicador);
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("usuarios").setParametroCadena("cargaUsuarioPorLogin").setParametroCadena(indicadorDto.getLoginCreador());
		UsuarioDto usuarioDto = getRestTemplate().getForEntity(url.getUrl(),UsuarioDto.class).getBody();
		
		//Estilo de visualizacion
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaEstiloVisualizacionDiagramaSectores");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(usuarioDto.getId());
		
		EstiloVisualizacionDiagramaSectoresDto estilo = getRestTemplate().getForEntity(url.getUrl(), EstiloVisualizacionDiagramaSectoresDto.class).getBody();
		
		if ( estilo!=null && estilo.getId()>0) 
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);
		
		//Preparamos los datos para su visualizacion
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());
		
		AtributoFuenteDatosDto columnaAtributoMapa = obtenerColumnaMapa(listaColumnas);
				
		model.addAttribute("elementosPagina", getServicioConfiguracionGeneral().getTamanhoPagina());
		
		
		parametro = calcularValorParametro(parametro, request, listaColumnas);
		model.addAttribute("parametro", parametro);
		
		AtributosMapDto datos = new AtributosMapDto();
		AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
		
		if (idHistorico != null && !idHistorico.equals("0")) {
			
			if (historico.getDatosDto() == null) {
				url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("historico").setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			}

			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));
			AtributosMapDto datosFromHistorico = convertirEnAtributosMapDto (datosHistoricosTemporal);
			EncapsuladorListSW<AtributoDto> listaColumnasHistorico = obtenerListaColumnasHistorico(datosHistoricosTemporal);
			
			String ambito = "";
			datos = new AtributosMapDto();
			LinkedHashMap<String, ValorFDDto> eltosFromHistorico = new LinkedHashMap<String, ValorFDDto>();
			LinkedHashMap<String, ValorFDDto> mapaDatos = datosFromHistorico.getContenido();
			for (String key : mapaDatos.keySet()) {
				int i = 0;
				boolean campoCorrecto = false;
				while (!campoCorrecto) {
					if (listaColumnasHistorico.get(i).getEsMapa() || listaColumnasHistorico.get(i).getEsAmbito()) {
						if (listaColumnasHistorico.get(i).getEsAmbito()) {
							ambito = listaColumnasHistorico.get(i).getNombre();
							campoCorrecto = true;
						}
						i++;
						continue;
					}
				}
				if (!existeParametroEnListaColumnas(listaColumnasHistorico, parametro)) {
					parametro = NO_PARAMETRO;
				}
				if (parametro.equals(NO_PARAMETRO)) {
					campoCorrecto = false;
					while (!campoCorrecto) {
						if (((listaColumnasHistorico.get(i).getColumna() == null && listaColumnasHistorico.get(i).getIndicadorExpresion() != null) || (listaColumnasHistorico
								.get(i).getColumna() != null && listaColumnasHistorico.get(i).getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO))
								&& listaColumnasHistorico.get(i).getRelacion() == null) {
							parametro = listaColumnasHistorico.get(i).getNombre();
							campoCorrecto = true;
						} else {
							i++;
							continue;
						}
					}
				}

				if (key.equals(parametro)) {
					ValorFDDto datosParametro = mapaDatos.get(key);
					eltosFromHistorico.put(key, datosParametro);
				}
				if (key.equals(ambito)) {
					// añadir el ambito
					ValorFDDto datosParametro = mapaDatos.get(key);
					eltosFromHistorico.put(key, datosParametro);
				}
				datos.setContenido(eltosFromHistorico);
			}
			model.addAttribute("historico", historico);
		} else {
			datos = obtenerDatosIndicador(indicadorDto.getId(), "datosIndicadorParametro", parametro, listaColumnas);
		}
		
		
		if (columnaAtributoMapa!=null) {
			datos.getContenido().remove(columnaAtributoMapa.getNombre());
		}
		
		List<AtributoDto> listaColumnasNoMapa = obtenerListaColumnasNoMapa(historico, listaColumnas, datosHistoricosTemporal);
		if (listaColumnasNoMapa.size() <= 0) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error,response);
		}		
		model.addAttribute("listaColumnas", listaColumnasNoMapa);
		
		AtributoFuenteDatosDto columnaAtributoAmbito=null;
		for(AtributoDto col:listaColumnas){
			if(col.getEsAmbito()){
				columnaAtributoAmbito=col.getColumna();
			}
		}
		//Realizo agrupacións cando o campo ámbito é o mesmo
		Set<Entry<String, ValorFDDto>> datosRepresentar=datos.getContenido().entrySet();
		List<EncapsuladorStringSW> listaValoresAmbito=null;
		List<EncapsuladorStringSW> listaValores=null;
		for(Entry<String, ValorFDDto> columna:datosRepresentar){
			if(columna.getKey().equals(columnaAtributoAmbito.getNombre())){
				listaValoresAmbito=columna.getValue().getValores();
			}else{
				listaValores=columna.getValue().getValores();
			}
		}
		
		List<ParDiagramaBarrasDto> listaOrdenada = obtenerListaOrdenada(listaValoresAmbito, listaValores);
		Collections.sort(listaOrdenada);
		
		List<EncapsuladorStringSW> listaValoresAmbitoFormateada=new ArrayList<EncapsuladorStringSW>();
		List<EncapsuladorStringSW> listaValoresFormateada=new ArrayList<EncapsuladorStringSW>();
		if(listaOrdenada!=null && listaOrdenada.size()>0){
			int i=0;
			while(i < listaOrdenada.size()){
				try{
					Float.parseFloat(listaOrdenada.get(i).getValor());
					break;
				}catch (Exception e) {
					i++;
				}
			}
			if(i < listaOrdenada.size()){
				String valorAmbito=listaOrdenada.get(i).getValorAmbito();
				Float acumulacion=Float.parseFloat(listaOrdenada.get(i).getValor());
				i++;
				while(i<listaOrdenada.size()){
					if(listaOrdenada.get(i).getValor()!=null  && listaOrdenada.get(i).getValorAmbito().equals(valorAmbito)){
						try{
							Float suma=Float.parseFloat(listaOrdenada.get(i).getValor());
							acumulacion+=suma;
							i++;
						}catch (Exception e) {
							i++;
						}
					}else{
						listaValoresAmbitoFormateada.add(new EncapsuladorStringSW(valorAmbito));
						listaValoresFormateada.add(new EncapsuladorStringSW(acumulacion.toString()));
						valorAmbito=listaOrdenada.get(i).getValorAmbito();
						try{
							acumulacion=Float.parseFloat(listaOrdenada.get(i).getValor());
							i++;
						}catch (Exception e) {
							acumulacion=0f;
							i++;
						}
					}
				}
				listaValoresAmbitoFormateada.add(new EncapsuladorStringSW(valorAmbito));
				listaValoresFormateada.add(new EncapsuladorStringSW(acumulacion.toString()));
			
			}
		}
		Entry<String, ValorFDDto> columnaAmbito=null;
		Entry<String, ValorFDDto> columnaNormal=null;
		for(Entry<String, ValorFDDto> columna:datosRepresentar){
			if(columna.getKey().equals(columnaAtributoAmbito.getNombre())){
				columna.getValue().setValores(listaValoresAmbitoFormateada);
				columnaAmbito=columna;
			}else{
				columna.getValue().setValores(listaValoresFormateada);
				columnaNormal=columna;
			}
		}
		
		datosRepresentar = new LinkedHashSet<Entry<String, ValorFDDto>>();
		datosRepresentar.add(columnaAmbito);
		datosRepresentar.add(columnaNormal);
		
//		int numFilas = obtenerNumeroFilas(datos);
		int numFilas = calcularNumFilasDatosRepresentar(datosRepresentar);

		model.addAttribute("numFilas",numFilas);
		model.addAttribute("numPaginasDiagramas", Math.ceil(numFilas/getServicioConfiguracionGeneral().getTamanhoPagina()));
		
		model.addAttribute("datos",datosRepresentar);
		
		//Mostrar histórico
		mostrarHistorico(idIndicador, model);
		
		//Error fichero exportado
		if ( error!=null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}
		
		return "indicadorPublicadoSectoresWebTile";
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=exportar" })
	public void exportarIndicador(@RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "tipoGrafico") int tipoGrafico, 
			@RequestParam(value = "tipo") String tipo, 
			Model model,
			HttpServletRequest request, 
			HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, Exception {
		
		//Long idIndicador;
		//try {
		//	idIndicador = Long.valueOf(UtilEncriptar.desencripta(id));
		//} catch (Exception e) {
		//	idIndicador = 0L;
		//}
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(),IndicadorDto.class).getBody();
		
		if ( indicadorDto.getPteAprobacion() || !indicadorDto.getPublicadoEnWeb()) {
			visualizarGrafico(idIndicador, model, request,tipoGrafico, null, "jsp.indicador.exportar.fichero.error", response);
		}
		
		//Exportar datos del indicador
		EncapsuladorFileSW fichero = new EncapsuladorFileSW();
		if ( tipo.equals(TiposFuente.CSV.getId()))
			fichero = exportarIndicadorCSV(idIndicador);
		else if (tipo.equals(TiposFuente.BDESPACIAL.getId())) {
			fichero = exportarIndicadorBDEspacial(idIndicador);
		} else if (tipo.equals(TiposFuente.GML.getId())) {
			fichero = exportarIndicadorGML(idIndicador);
		} else if (tipo.equals(TiposFuente.SHAPEFILE.getId())) {
			fichero = exportarIndicadorShapeFile(idIndicador);
		}
		
		if ( fichero == null || fichero.getFich()==null || fichero.getFich().length<=0 ) {
			if (fichero.getNombre() != null && fichero.getNombre().equals(ERROR_NO_MAPA_SHAPEFILE))
				visualizarGrafico(idIndicador, model, request, tipoGrafico, null,
						"jsp.indicador.exportar.shapefile.error",response);
			else
				visualizarGrafico(idIndicador, model, request, tipoGrafico, null,
						"jsp.indicador.exportar.fichero.error",response);
		} else {
			model.addAttribute("fichero",fichero.getNombre());
			
			ServletContext sc = request.getSession().getServletContext();
			Map<String,Object> mapa = new HashMap<String,Object>();
			mapa.put("name", fichero.getNombre());
			mapa.put("type",sc.getMimeType(fichero.getNombre()));
			mapa.put("inputStream", new ByteArrayInputStream(fichero.getFich()));
			try {
				renderMergedOutputModel(mapa, request, response);
			} catch (Exception e) {
				log.debug("Error al leer el fichero de exportacion para descarga");
			}
		}
	}
	
	private Model rellenarDatosFormularioEstiloExportarPDF(Model model) {
		// Datos estilos
		// Tamanho fuente
		List<Integer> tamanhosFuente = new ArrayList<Integer>();
		for (Integer i = 8; i <= 40; i++) {
			tamanhosFuente.add(i);
		}

		// Valores tamanho fuente
		List<String> valoresTamanhosFuente = new ArrayList<String>();
		for (Double i = 0.3; i <= 2; i = i + 0.1) {
			DecimalFormat df = new DecimalFormat("#.#");
			String val = df.format(i);
			val = val.replace(",", ".");
			valoresTamanhosFuente.add(val);
		}
		
		List<Double> tamanhosFuenteSectores = new ArrayList<Double>();
		for (Double i = 8D; i <= 40; i++) {
			tamanhosFuenteSectores.add(i);
		}

		// Valores tamanho fuente
		List<Integer> valoresTamanhosFuenteSectores = new ArrayList<Integer>();
		for (Integer i = 8; i <= 40; i++) {
			valoresTamanhosFuenteSectores.add(i);
		}
		
		// Tipos letra
		List<TiposLetra> tiposLetra = new ArrayList<TiposLetra>();
		for (TiposLetra tipo : TiposLetra.values())
			tiposLetra.add(tipo);
		// Tipos fecha
		List<TipoFecha> tiposFecha = new ArrayList<TipoFecha>();
		for (TipoFecha tipo : TipoFecha.values())
			tiposFecha.add(tipo);
		// Decimales
		List<Integer> decimales = new ArrayList<Integer>();
		for (Integer i = 0; i <= 10; i++) {
			decimales.add(i);
		}

		// Diametros
		List<Integer> diametros = new ArrayList<Integer>();
		for (Integer i = 300; i <= 550; i += 25) {
			diametros.add(i);
		}

		// Tamanhos de columna
		List<Integer> tamanhosColumna = new ArrayList<Integer>();
		for (Integer i = 20; i <= 100; i += 5) {
			tamanhosColumna.add(i);
		}

		model.addAttribute("tamanhosColumna", tamanhosColumna);
		model.addAttribute("diametros", diametros);
		model.addAttribute("tamanhosFuente", tamanhosFuente);
		model.addAttribute("valoresTamanhosFuente", valoresTamanhosFuente);
		model.addAttribute("tamanhosFuenteSectores", tamanhosFuenteSectores);
		model.addAttribute("valoresTamanhosFuenteSectores", valoresTamanhosFuenteSectores);
		model.addAttribute("tiposLetra", tiposLetra);
		model.addAttribute("tiposFecha", tiposFecha);
		model.addAttribute("decimales", decimales);
		model.addAttribute("color_defecto", COLOR_DEFECTO);

		model.addAttribute("tamanho_fuente_defecto_tabla", TAMANHO_FUENTE_DEFECTO_EXPORTAR);
		model.addAttribute("fuente_defecto_tabla", FUENTE_DEFECTO);
		model.addAttribute("tipo_fecha_defecto_tabla", TIPO_FECHA_DEFECTO);

		model.addAttribute("tamanho_fuente_defecto_barras", TAMANHO_FUENTE_DEFECTO_EXPORTAR);
		model.addAttribute("fuente_defecto_barras", FUENTE_DEFECTO);
		model.addAttribute("tamanho_defecto_barras", TAMANHO_COLUMNA_DEFECTO);
		model.addAttribute("color_defecto_barras", COLOR_DEFECTO);

		model.addAttribute("colores_defecto_sectores", COLORES_DEFECTO);
		model.addAttribute("tamanho_defecto_sectores", TAMANHO_FUENTE_DEFECTO);
		model.addAttribute("fuente_defecto_sectores", FUENTE_DEFECTO);
		model.addAttribute("diametro_defecto_sectores", DIAMETRO_DEFECTO);
		model.addAttribute("diametros_sectores", diametros);

		return model;
	}
		
	/**
	 * Visualización de una version
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarVersion" })
	public String visualizarVersion(@RequestParam(value = "id") Long idHistorico,
			@RequestParam(value = "idInd") Long idIndicador, 
			@RequestParam(value = "tipoGrafico") int tipoGrafico,
			Model model, 
			HttpServletRequest request,
			HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, Exception {
		if (idHistorico != 0) {
			UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("historico")
					.setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
			HistoricoDto historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			model.addAttribute("historico", historico);

			return visualizarGrafico(idIndicador, model, request, tipoGrafico, historico, null, response);
		} else {
			return visualizarGrafico(idIndicador, model, request, tipoGrafico, null, null, response);
		}
	}

	
	/** Visualiza los parámetros a escoger sobre los que se visualizará la evolución de un indicador */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=mostrarOpcionesEvolucion" })
	public String mostrarOpcionesEvolucion(@RequestParam(value = "id") Long idIndicador, 
			@RequestParam(value = "param", required = false) String parametro, 
			Model model, HttpServletRequest request) {
		//Datos indicador
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicador = getRestTemplate().getForEntity(url.getUrl(),IndicadorDto.class).getBody();
		model.addAttribute("indicadorDto", indicador);
		
		model.addAttribute("parametro", parametro);		
		model.addAttribute("idindicador", idIndicador);
		
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicador.getId());
		
		AtributosMapDto datosAmbito = obtenerDatosAmbito(indicador, listaColumnas, model);
		datosAmbito=ordenarDatos(datosAmbito);
		model.addAttribute("datosAmbito",datosAmbito.getContenido().entrySet());
		
		int numFilas = obtenerNumeroFilas(datosAmbito);
		model.addAttribute("numFilas",numFilas);
		
		return "indicadorPublicoMostrarEvolucionTile";
	}

	private AtributosMapDto obtenerDatosAmbito(IndicadorDto indicador, EncapsuladorListSW<AtributoDto> listaColumnas, Model model) {
		
		UrlConstructorSW url;
		AtributoDto ambito = new AtributoDto();
		List<AtributoDto> listaColumnasNoMapa = new ArrayList<AtributoDto>();
		
		//Por cada columna que se encuentre en listaColumnas
		for (AtributoDto col : listaColumnas) {
			
			if (!col.getEsMapa()
					&& !col.getEsAmbito()
					&& ((col.getColumna() == null && col.getIndicadorExpresion() != null) || col.getColumna() != null
							&& col.getColumna().getTipoAtributo() != null
							&& col.getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO))
				listaColumnasNoMapa.add(col);
			if (col.getEsAmbito())
				ambito = col;
		}

		model.addAttribute("listaColumnas", listaColumnasNoMapa);
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("atributos");
		url.setParametroCadena("datosIndicadorParametro");
		url.setParametroCadena(indicador.getId());
		url.setParametroCadena(NO_PARAMETRO);

		int numCampos = 0;
		model.addAttribute("numCampos", numCampos);

		AtributosMapDto datosAmbito = getRestTemplate().postForEntity(url.getUrl(),
				new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas), AtributosMapDto.class).getBody();

		LinkedHashMap<String, ValorFDDto> mapaAmbito = datosAmbito.getContenido();
		LinkedHashMap<String, ValorFDDto> mapaAmbitoResult = new LinkedHashMap<String, ValorFDDto>();
		
		for (String key : mapaAmbito.keySet()) {
			if (key.equals(ambito.getNombre())) {
				mapaAmbitoResult.put(key, mapaAmbito.get(key));
			}
		}

		AtributosMapDto datosAmbitoResult = new AtributosMapDto();
		datosAmbitoResult.setContenido(mapaAmbitoResult);
		
		return ordenarDatos(datosAmbitoResult);
	}

	
	private int obtenerNumeroFilas(AtributosMapDto datosAmbito) {
		Set<String> claves = datosAmbito.getContenido().keySet();
		String primeraClave = ""; 
		int numFilas = 0;
		if (claves.iterator().hasNext()) {
			primeraClave = claves.iterator().next();
			if (datosAmbito.getContenido().get(primeraClave) != null)
				//numFilas = datosAmbito.getContenido().get(primeraClave).getValores().size() - 1;
				numFilas = datosAmbito.getContenido().get(primeraClave).getValores().size();
			else
				numFilas = 0;
		} else {
			numFilas = 0;
		}
		return numFilas;
	}

	/**
	 * Muestra la evolución de un indicador
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(method = RequestMethod.POST, params = { "accion=visualizarEvolucion" })
	public String visualizarEvolucion(Model model, HttpServletRequest request) throws JsonParseException,
			JsonMappingException, IOException {
		
		//Datos indicador
		String idIndicador = request.getParameter("id");
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();
		model.addAttribute("indicadorDto", indicadorDto);

		String parametro = request.getParameter("parametro");
		if (parametro == null || parametro.equals("")) {
			// Error: debe seleccionar un parámetro
			model.addAttribute("errorSeleccionarParametro", "jsp.indicador.evolucion.error.seleccion.parametro");
		}

		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());
		AtributosMapDto datosAmbito = obtenerDatosAmbito(indicadorDto, listaColumnas, model);

		int numFilas = obtenerNumeroFilas(datosAmbito);
		model.addAttribute("numFilas", numFilas);

		int numCampos = 0;
		ArrayList<Integer> campos = new ArrayList<Integer>();
		if (request.getParameter("numCampos") != null && !request.getParameter("numCampos").equals("")
				&& !request.getParameter("numCampos").equals("0")) {
			numCampos = Integer.valueOf(request.getParameter("numCampos"));
		} else {
			// Error: debe seleccionar al menos un campo
			model.addAttribute("errorSeleccionarCampo", "jsp.indicador.evolucion.error.seleccion.campo");
		}

		for (int i = 0; i <= numFilas; i++) {
			if (request.getParameter("campo_" + i) != null && !request.getParameter("campo_" + i).equals("")) {
				campos.add(Integer.valueOf(request.getParameter("campo_" + i)));
			}
		}
		
		if(request.getParameter("campo_chk_todos")!=null){
			model.addAttribute("seleccionado", true);
		}

		Set<Entry<String, ValorFDDto>> ambito = datosAmbito.getContenido().entrySet();
		ArrayList<String> leyenda = new ArrayList<String>();
		for (int i = 0; i < campos.size(); i++) {
			for (Map.Entry<String, ValorFDDto> valoresFila : ambito) {
				leyenda.add(valoresFila.getValue().getValores().get(campos.get(i)).getTexto());
			}
		}

		// Leyenda de la grafica de evolucion
		String strLeyenda = "";
		if (leyenda.size() != 0) {
			for (int i = 0; i < leyenda.size(); i++) {
				strLeyenda = strLeyenda + ",{label:'" + leyenda.get(i) + "'}";
			}
			strLeyenda = strLeyenda.substring(1);
		}
		model.addAttribute("leyenda", strLeyenda);

		// Historicos del indicador sin los datos del mapa
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("historico").setParametroCadena("cargarHistoricos")
				.setParametroCadena(indicadorDto.getId());
		List<HistoricoDto> historicos = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class)
				.getBody();

		AtributosMapDto evolucion = new AtributosMapDto();
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdfParse = new java.text.SimpleDateFormat("dd/MM/yyyy");
		sdfParse.setLenient(false);
		Long maxDia = 0L;
		Long maxDiaAux = 0L;

		// Buscar dentro de los historicos aquellos que tengan el parámetro
		// seleccionado
		for (HistoricoDto historico : historicos) {
			try{
				historico.setFecha(sdfParse.parse(historico.getStrFecha()));
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			//Los datos del histórico es necesario obtenerlos en una estructura del tipo AtributoMapDto
			AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));			
			AtributosMapDto atributos = convertirEnAtributosMapDto (datosHistoricosTemporal); //(AtributosMapDto) writeObject(decompress(historico.getDatosDto()));
			LinkedHashMap<String, ValorFDDto> mapaDatos = atributos.getContenido();
			for (String key : mapaDatos.keySet()) {
				if (key.equals(parametro)) {
					evolucion.getContenido().put(sdf.format(historico.getFecha()), mapaDatos.get(key));
					maxDiaAux = historico.getFecha().getTime();
					if (maxDiaAux > maxDia) {
						maxDia = maxDiaAux;
					}
				}
			}
		}
		//incluimos datos versión actual
		AtributosMapDto datosVersionActual = new AtributosMapDto();
		datosVersionActual = obtenerDatosIndicador(indicadorDto.getId(), "datosIndicador", parametro, listaColumnas);
		LinkedHashMap<String, ValorFDDto> mapaDatosVersionActual = datosVersionActual.getContenido();
		Date fechaActual=new Date();
		for (String key : mapaDatosVersionActual.keySet()) {
			if (key.equals(parametro)) {
				evolucion.getContenido().put(sdf.format(fechaActual), mapaDatosVersionActual.get(key));
				maxDiaAux = fechaActual.getTime();
				if (maxDiaAux > maxDia) {
					maxDia = maxDiaAux;
				}
			}
		}
		// Limite maximo del eje X
		model.addAttribute("maxDia", maxDia + 86400000);

		// Seleccionamos los datos de los campos a visualizar
		LinkedHashMap<String, ValorFDDto> datosEvolucionOrigen = evolucion.getContenido();
		String dato = "";
		ArrayList<String> datos = new ArrayList<String>();
		for (int i = 0; i < campos.size(); i++) {
			for (String key : datosEvolucionOrigen.keySet()) {
				ValorFDDto datosHistorico = datosEvolucionOrigen.get(key);
				try {
					if (datosHistorico.getValores().get(campos.get(i)).getTexto() != null) {
						dato = dato + ",['" + key + "', " + datosHistorico.getValores().get(campos.get(i)).getTexto()
								+ "]";
					} else {
						dato = dato + ",['" + key + "', 0]";
					}
				} catch (Exception e) {
					dato = dato + ",['" + key + "', 0]";
				}
			}
			dato = dato.substring(1);
			dato = "[" + dato + "]";
			datos.add(dato);
			dato = "";
		}

		model.addAttribute("datos", datos);
		model.addAttribute("numCampos", numCampos);
		model.addAttribute("camposChk", campos);
		return mostrarOpcionesEvolucion(Long.parseLong(idIndicador), parametro, model, request);
	}

	/** Mantiene la visualización del gráfico desde el que se realizó la acción 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException */
	private String visualizarGrafico(Long id, 
			Model model,
			HttpServletRequest request, 
			int tipoGrafico,
			HistoricoDto historico,
			String error, 
			HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, Exception {
		switch(tipoGrafico) {
		case 1:
			return visualizarIndicadorTabla(id, model, request, historico, error, response);
		case 2:
			String parametroBarras = request.getParameter("parametro");
			if (historico != null) {
				return visualizarIndicadorDiagramaBarras(id, parametroBarras, String.valueOf(historico.getId()), model, request, historico, error, response);
			} else {
				return visualizarIndicadorDiagramaBarras(id, parametroBarras, null, model, request, historico, error, response);
			}
		case 3:
			String parametroSectores = request.getParameter("parametro");
			if (historico != null) {
				return visualizarIndicadorDiagramaSectores(id, parametroSectores, String.valueOf(historico.getId()), model, request, historico, error, response);
			} else {
				return visualizarIndicadorDiagramaSectores(id, parametroSectores, null, model, request, historico, error, response);
			}
		case 4:
			if (historico != null) {
				return visualizarIndicadorMapa(id, null, String.valueOf(historico.getId()), model, request, historico, error, response);
			} else {
				return visualizarIndicadorMapa(id, null, null, model, request, historico, error, response);
			}
		default:
			return visualizarIndicadorTabla(id, model, request, historico, error, response);
		}
	}

	@SuppressWarnings("unchecked")
	private EncapsuladorListSW<AtributoDto> obtenerListaColumnas(Long idIndicador) {
		UrlConstructorSW url;
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("atributos");
		url.setParametroCadena("atributos");
		url.setParametroCadena(idIndicador);
		
		EncapsuladorListSW<AtributoDto> listaColumnas = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();
		
		//Repasamos la lista de columnas por si alguna tiene el mismo nombre q otra, se le calcula otro nombre.
		//Que ademas coincidira con el nombre usado en el repositorio
		EncapsuladorListSW<AtributoDto> listaColumnas2 = new EncapsuladorListSW<AtributoDto>();
		for ( AtributoDto attr : listaColumnas ) {
			String nombreColumna = attr.getNombre();
			if ( contieneColumna(listaColumnas2,attr)) {
				Integer i = 1; 
	    		while ( contieneColumna(listaColumnas2,attr) ) {
	    			attr.setNombre(nombreColumna+i.toString());
	    			i++;
	    		}
	    		listaColumnas2.add(attr);
			} else {
				listaColumnas2.add(attr);
			}
		}
		return listaColumnas2;
	}
	
	private AtributosMapDto obtenerDatosIndicador(Long idIndicador, String metodo, Object parametro, EncapsuladorListSW<AtributoDto> listaColumnas) {
		UrlConstructorSW url;
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("atributos");
		url.setParametroCadena(metodo);
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(parametro);
		
		AtributosMapDto datos = getRestTemplate().postForEntity(url.getUrl(),new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas),AtributosMapDto.class).getBody();
		return datos;
	}

	private EncapsuladorFileSW exportarIndicadorCSV(Long idIndicador) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("exportarCSV");
		url.setParametroCadena(idIndicador);
		
		EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class).getBody();
		return fichero;
	}
	
	private EncapsuladorFileSW exportarIndicadorBDEspacial(Long idIndicador) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("exportarBDEspacial");
		url.setParametroCadena(idIndicador);
		
		EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class).getBody();
		return fichero;
	}

	/** Visualiza las versiones guardadas del indicador*/
	@SuppressWarnings("unchecked")
	private void mostrarHistorico(Long idIndicador, Model model) {
		UrlConstructorSW url;
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("historico").setParametroCadena("cargarHistoricos").setParametroCadena(idIndicador);

		HistoricoDto historico = new HistoricoDto();
		historico.setId(0);
		List<HistoricoDto> historicos = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();
		historicos.add(historico);
		model.addAttribute("historicos", historicos);
		if (!model.containsAttribute("historico")){
			model.addAttribute("historico", historico);
		}
	}
	
	private EncapsuladorFileSW exportarIndicadorGML(Long idIndicador) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("exportarGML");
		url.setParametroCadena(idIndicador); 
		
		EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class).getBody();
		return fichero;
	}
	
	private EncapsuladorFileSW exportarIndicadorShapeFile(Long idIndicador) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("exportarShapefile");
		url.setParametroCadena(idIndicador);
		
		EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class).getBody();
		return fichero;
	}
	
	@RequestMapping(method = RequestMethod.GET, params={"accion=exportarPDF"})
	public String paginaExportarIndicadorPdf(@RequestParam(value="id") Long id, @RequestParam(value="tipo") String tipo, @RequestParam(value = "idHistorico") String idHistorico,
			@RequestParam(value = "desde") String origenExportacion,
			Model model,HttpServletRequest request, HttpServletResponse response ) throws Exception {		
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(id);
		IndicadorDto resultado = getRestTemplate().getForEntity(url.getUrl(),IndicadorDto.class).getBody();
		model.addAttribute("indicadorDto", resultado);
		
		model.addAttribute("tipo",tipo);
		model.addAttribute("idHistorico", idHistorico);
		
		model = rellenarDatosFormularioEstiloExportarPDF(model);
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("usuarios").setParametroCadena("cargaUsuarioPorLogin").setParametroCadena(resultado.getLoginCreador());
		UsuarioDto usuarioDto = getRestTemplate().getForEntity(url.getUrl(),UsuarioDto.class).getBody();
		
		//Estilo de visualizacion Tabla
		//url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		//url.setParametroCadena("indicadores");
		//url.setParametroCadena("cargaEstiloVisualizacionTabla");
		//url.setParametroCadena(id);
		//url.setParametroCadena(usuarioDto.getId());
		
		EstiloVisualizacionTablaDto estiloTabla = Estilos.cargarEstiloVisualizacionTabla(usuarioDto.getId(),
				getServicioConfiguracionGeneral().getUrlBaseSW(), getRestTemplate(), id);
		
		//EstiloVisualizacionTablaDto estiloTabla = getRestTemplate().getForEntity(url.getUrl(), EstiloVisualizacionTablaDto.class).getBody();
		
		if ( estiloTabla!=null && estiloTabla.getId()>0)
			model.addAttribute("estiloTabla", estiloTabla);
		else
			model.addAttribute("estiloTabla", null);
		
		//Estilo de visualizacion Diagrama de Barras
		//url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		//url.setParametroCadena("indicadores");
		//url.setParametroCadena("cargaEstiloVisualizacionDiagramaBarras");
		//url.setParametroCadena(id);
		//url.setParametroCadena(usuarioDto.getId());
		
		EstiloVisualizacionDiagramaBarrasDto estiloBarras = Estilos.cargarEstiloDiagramaBarras(usuarioDto.getId(),
				getServicioConfiguracionGeneral().getUrlBaseSW(), getRestTemplate(), id);
		
		//EstiloVisualizacionDiagramaBarrasDto estiloBarras = getRestTemplate().getForEntity(url.getUrl(), EstiloVisualizacionDiagramaBarrasDto.class).getBody();
		
		if ( estiloBarras!=null && estiloBarras.getId()>0)
			model.addAttribute("estiloBarras", estiloBarras);
		else
			model.addAttribute("estiloBarras", null);
		
		//Estilo de visualizacion Diagrama de Sectores
		//url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		//url.setParametroCadena("indicadores");
		//url.setParametroCadena("cargaEstiloVisualizacionDiagramaSectores");
		//url.setParametroCadena(id);
		//url.setParametroCadena(usuarioDto.getId());
		
		EstiloVisualizacionDiagramaSectoresDto estiloSectores = Estilos.cargarEstiloSectores(usuarioDto.getId(),
				getServicioConfiguracionGeneral().getUrlBaseSW(), getRestTemplate(), id);
		
		//EstiloVisualizacionDiagramaSectoresDto estiloSectores = getRestTemplate().getForEntity(url.getUrl(), EstiloVisualizacionDiagramaSectoresDto.class).getBody();
		
		if ( estiloSectores!=null && estiloSectores.getId()>0)
			model.addAttribute("estiloSectores", estiloSectores);
		else
			model.addAttribute("estiloSectores", null);

		//Estilo de visualizacion mapa
		//url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		//url.setParametroCadena("indicadores");
		//url.setParametroCadena("cargaEstiloVisualizacionMapa");
		//url.setParametroCadena(id);
		//url.setParametroCadena(usuarioDto.getId());

		EstiloVisualizacionMapaDto estiloMapa = Estilos.cargarEstiloMapa(usuarioDto.getId(), 
				getServicioConfiguracionGeneral().getUrlBaseSW(), getRestTemplate(), id);
		
		//EstiloVisualizacionMapaDto estiloMapa = getRestTemplate().getForEntity(url.getUrl(), EstiloVisualizacionMapaDto.class).getBody();
		
		//Rangos de Estilo de visualizacion de Mapa
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaRangosVisualizacionMapa");
		url.setParametroCadena(estiloMapa.getId());

		EncapsuladorListSW<RangosVisualizacionMapaDto> listaRangos = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();	
				
		if ( estiloMapa!=null && estiloMapa.getId()>0)
			model.addAttribute("estiloMapa", estiloMapa);
		else
			model.addAttribute("estiloMapa", null);
		
		List<RangosVisualizacionMapaDto> rangos = listaRangos.getLista();
		
		model.addAttribute("rangos",rangos);
		model.addAttribute("numRangos",rangos.size());
		
		obtenerListaColumnasSeleccionPDF(idHistorico, model, resultado);
		//La generación del PDF se solicita desde la Tabla de Valores, desde este punto la lista de posiciones deberá estar vacia y obtener todos los valores del ámbito
		if ((origenExportacion!=null) && origenExportacion.equals("0")) {
			this.listaPosicionesAmbitoDiagrama = null;
		}
		model.addAttribute("origenExportacionPublico", origenExportacion);
		model.addAttribute("camposChkDiagrama", this.listaPosicionesAmbitoDiagrama);
		
		return "paginaExportarIndicadorPublicoPdfTile";
	}
		
	protected AtributosMapDto ordenarDatos(AtributosMapDto datosAmbito) {
		Set<String> claves = datosAmbito.getContenido().keySet();
		String primeraClave = "";
		if (claves.iterator().hasNext()) {
			primeraClave = claves.iterator().next();
			if (datosAmbito.getContenido().get(primeraClave) != null){
				List<EncapsuladorStringSW> lista=datosAmbito.getContenido().get(primeraClave).getValores(); 
				Collections.sort(lista, new Comparator<EncapsuladorStringSW>() {

					public int compare(EncapsuladorStringSW o1, EncapsuladorStringSW o2) {
						return o1.getTexto().compareTo(o2.getTexto());
					}
				});
				datosAmbito.getContenido().get(primeraClave).setValores(lista);
			}
		}

		return datosAmbito;
	}
	
	/**
	 * Método que visualiza los parámetros a escoger sobre los que se visualizará los diagramas de barras y de sectores de un indicador
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=mostrarOpcionesDiagramasPublicos" })
	public String mostrarOpcionesDiagramas(@RequestParam(value = "id") Long idIndicador, @RequestParam(value = "tipo") String strTipoDiagrama,
			@RequestParam(value = "param", required = false) String parametro, Model model, HttpServletRequest request) {

		//Se eliminan del model la información de la columna ámbito relevante en la generación del diagrama
		model.addAttribute("datosAmbitoDiagrama", null);
		model.addAttribute("numFilasDiagrama", null);
		model.addAttribute("numCamposDiagrama", null);
		model.addAttribute("camposChkDiagrama", null);
		
		//Se indentifica el tipo de diagrama (barras o sectores) que se quiere lanzar. Esta información nos la aporta strTipoDiagrama
		if (strTipoDiagrama!=null && strTipoDiagrama.equals("barras")) {
			model.addAttribute("tipoDiagrama", "barras");
		} else if  (strTipoDiagrama!=null && strTipoDiagrama.equals("sectores")) {
			model.addAttribute("tipoDiagrama", "sectores");
		}
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicador = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();
		model.addAttribute("indicadorDto", indicador);
		model.addAttribute("idindicador", idIndicador);		

		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(idIndicador);
		AtributosMapDto datosAmbito = obtenerDatosAmbito(indicador, listaColumnas, model);
		datosAmbito=ordenarDatos(datosAmbito);
		model.addAttribute("datosAmbitoDiagrama", datosAmbito.getContenido().entrySet());
		int numFilas = obtenerNumeroFilas(datosAmbito);
		model.addAttribute("numFilasDiagrama", numFilas);
		
		return "mostrarDiagramasPublicoTile";
	}
	
	/**
	 * Método que visualiza los diagramas de barras o sectores dependiendo de donde se ha llamado aportando los valores del campo ámbito
	 * seleccionado por el usuario en le formulario de entrada de datos
	 * @param model
	 * @param request
	 * @param response
	 * @return visualización del gráfico
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(method = RequestMethod.POST, params = { "accion=visualizarDiagramaPublico" })
	public String visualizarDiagrama(Model model, HttpServletRequest request, HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, Exception {
																									
		String strTipoDiagrama = request.getParameter("tipo");
		//Se obtiene el indicador
		String idIndicador = request.getParameter("id");
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();
		model.addAttribute("indicadorDto", indicadorDto);
		
		String parametro = request.getParameter("parametro");
		if (parametro == null || parametro.equals("")) {
			//No existe un parámetro seleccionado. Es necesario informar
			model.addAttribute("errorSeleccionarParametro", "jsp.indicador.evolucion.error.seleccion.parametro");
			return mostrarOpcionesDiagramas(Long.parseLong(idIndicador), strTipoDiagrama, parametro, model, request);
		}
		
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());
		AtributosMapDto datosAmbito = obtenerDatosAmbito(indicadorDto, listaColumnas, model);
		datosAmbito=ordenarDatos(datosAmbito);
		model.addAttribute("datosAmbitoDiagrama", datosAmbito.getContenido().entrySet());
		int numFilas = obtenerNumeroFilas(datosAmbito);
		model.addAttribute("numFilasDiagrama", numFilas);
		
		int numCamposDiagrama = 0;
		ArrayList<Integer> camposDiagrama = new ArrayList<Integer>();
		if (request.getParameter("numCamposDiagrama") != null && !request.getParameter("numCamposDiagrama").equals("") && !request.getParameter("numCamposDiagrama").equals("0")) {
			numCamposDiagrama = Integer.valueOf(request.getParameter("numCamposDiagrama"));
		} else {
			//El número de campos seleccionados en la lista de valores ámbito es CERO
			model.addAttribute("errorSeleccionarCampo", "jsp.indicador.evolucion.error.seleccion.campo");
			return mostrarOpcionesDiagramas(Long.parseLong(idIndicador), strTipoDiagrama, parametro, model, request);
		}
		
		//Se rellena en el array campos todos los valores seleccionados
		for (int i = 0; i <= numFilas; i++) {
			if (request.getParameter("campo_" + i) != null && !request.getParameter("campo_" + i).equals("")) {
				camposDiagrama.add(Integer.valueOf(request.getParameter("campo_" + i)));
			}
		}
		
		if(request.getParameter("campo_chk_todos")!=null){
			model.addAttribute("seleccionado", true);
		}
		
		this.listaPosicionesAmbitoDiagrama = camposDiagrama;
		
		model.addAttribute("numCamposDiagrama", numCamposDiagrama);
		model.addAttribute("camposChkDiagrama", camposDiagrama);
		model.addAttribute("parametro", parametro);
		
		String identificadorHistorico = request.getParameter("listaHistoricos");
		if (identificadorHistorico!=null && !(identificadorHistorico.equals("0"))) {
			String strTipoGrafico = request.getParameter("tipoGrafico");
			return visualizarVersion(Long.parseLong(identificadorHistorico), Long.parseLong(idIndicador), Integer.valueOf(strTipoGrafico).intValue(), model, request, response);
		} else {
			if (strTipoDiagrama!=null && strTipoDiagrama.equals("barras")) {
				return visualizarIndicadorDiagramaBarras(indicadorDto.getId(), parametro, null, model, request, null, null, response);
			} else if (strTipoDiagrama!=null && strTipoDiagrama.equals("sectores")) {
				return visualizarIndicadorDiagramaSectores(indicadorDto.getId(), parametro, null, model, request, null, null, response);
			}
		}
				
		return mostrarOpcionesDiagramas(Long.parseLong(idIndicador), strTipoDiagrama, parametro, model, request);
	}
	
	/**
	 * Método que a partir de los valores totales del ámbito y del parámetro seleccionado para la visualización del diagrama de barras y/o sectores
	 * obtiene: 
	 * 			1º: Una lista de longitud la longitud de la lista de ámbito que contiene los pares valor ámbito - valor parametro. En la nueva lista
	 * 			se rellenan con NULL los valores del parámetro si la lista de los valores del parámetro es menor en longitud a la lista de los valores 
	 * 			del ámbito = listaValoresDiagrama
	 * 			2º: La lista obtenida en el punto 1 se ordena para que los valores de las posiciones del campo ámbito coincidan con los valores de las
	 * 			posiciones obtenidas en this.listaPosicionesAmbitoDiagrama (lista de posiciones obtenida de la selección manual del usuario)
	 * 			3º: A partir de la lista ordenada obtenida en el punto 2) y con la lista de posiciones this.listaPosicionesAmbitoDiagrama, se obtiene
	 * 			una nueva lista con los valores que realmente se deberán tener en cuenta para la visualización del diagrama, es decir, los pares 
	 * 			valor ámbito - valor parametro correspondientes a los valores de ámbito seleccionados por el usuario = listaIntermediaOrdenada
	 * 			4º: A partir de la lista obtenida en el punto 3º se eliminan los pares valor ámbito - valor parámetro que tengan valor NULL en el valor
	 * 			del parámetro
	 * @param listaValoresAmbito
	 * @param listaValores
	 * @return List<ParDiagramaBarrasDto> listaOrdenada lista con los valores definitivos a tener en cuenta en la visualización del diagrama
	 */
	private List<ParDiagramaBarrasDto> obtenerListaOrdenada(List<EncapsuladorStringSW> listaValoresAmbito, List<EncapsuladorStringSW> listaValores) {
		
		Collections.sort(listaValoresAmbito);
		List<ParDiagramaBarrasDto>listaValoresDiagrama=new ArrayList<ParDiagramaBarrasDto>();		
		int intTamanhoListaAmbito=listaValoresAmbito.size();
		int intTamanhoListaValores=listaValores.size();		
		for(int i=0; i<intTamanhoListaAmbito;i++) {
			if (existePosicionEnListaValores(i, intTamanhoListaValores)) {
				listaValoresDiagrama.add(new ParDiagramaBarrasDto(listaValoresAmbito.get(i).getTexto(),listaValores.get(i).getTexto()));
			} else {
				listaValoresDiagrama.add(new ParDiagramaBarrasDto(listaValoresAmbito.get(i).getTexto(),null));
			}
		}
		Collections.sort(listaValoresDiagrama);
		
		List<ParDiagramaBarrasDto>listaIntermediaOrdenada=new ArrayList<ParDiagramaBarrasDto>();
		int tamanhoListaPosiciones = 0;
		if (listaPosicionesNoEsVacia(this.listaPosicionesAmbitoDiagrama)) {
			tamanhoListaPosiciones = this.listaPosicionesAmbitoDiagrama.size();
		} else {
			//Se obtiene la lista ordenada a partir de todos los pares ámbito-valor
			List<ParDiagramaBarrasDto> listaOrdenada = obtenerListaOrdenadaCompleta(listaValoresDiagrama);
			return listaOrdenada;
		}
		
		for (int i = 0; i < tamanhoListaPosiciones; i++) {			
			Integer intValorPosicion= this.listaPosicionesAmbitoDiagrama.get(i);
			ParDiagramaBarrasDto objParDiagramaBarrasDtoEnPosicion = listaValoresDiagrama.get(intValorPosicion);
			listaIntermediaOrdenada.add(objParDiagramaBarrasDtoEnPosicion);
		}
		
		List<ParDiagramaBarrasDto>listaOrdenada=new ArrayList<ParDiagramaBarrasDto>();
		int tamanhoListaIntermediaOrdenada = listaIntermediaOrdenada.size();
		for(int i=0; i<tamanhoListaIntermediaOrdenada;i++){
			ParDiagramaBarrasDto objParDiagramaBarrasDtoEnPosicion = listaIntermediaOrdenada.get(i);
			if((objParDiagramaBarrasDtoEnPosicion!=null && objParDiagramaBarrasDtoEnPosicion.getValorAmbito()!=null) && 
					(objParDiagramaBarrasDtoEnPosicion!=null && objParDiagramaBarrasDtoEnPosicion.getValor()!=null)) {
				listaOrdenada.add(objParDiagramaBarrasDtoEnPosicion);
			}
		}
		return listaOrdenada;
	}

	/**
	 * Método que comprueba si existe la posición i dentro del tamaño de una lista
	 * @param int posicion valor de la posición que se desea comprobar si existe en la lista
	 * @param int tamanhoLista tamaño de lista donde se comprobará si existe la posición solicitada
	 * @return TRUE si la posición está dentro de la longitud de la lista, FALSE en caso contrario
	 */
	private boolean existePosicionEnListaValores(int posicion, int tamanhoLista) {		
		return (posicion <= (tamanhoLista-1));
	}
	
	/**
	 * Método que comprueba si en la lista de columnas del indicador existe el parámetro. Este comprobación es 
	 * necesaria porque es posible que el indicador y alguno de sus históricos no compartan la misma lista de
	 * parámetros. Si este es el caso y se cambia para la visualización de un diagrama al histórico en un parámetro
	 * que no contiene el histórico la aplicación falla
	 * @param listaColumnas
	 * @param parametro
	 * @return TRUE si existe el parámetro, FALSE en caso contrario
	 */
	private boolean existeParametroEnListaColumnas(EncapsuladorListSW<AtributoDto> listaColumnas, String parametro) {
		
		for (AtributoDto attr : listaColumnas) {
			AtributoFuenteDatosDto columna = attr.getColumna();
			if (columna!=null) { 
				String nombreColumna = columna.getNombre();
				if (nombreColumna!=null && nombreColumna.equals(parametro)) {
					return true;	
				}
			}
		}
		return false;
	}

	/**
	 * Método interno que comprueba si el atributo parámetro tiene valor
	 * @param parametro
	 * @return TRUE si tiene valor, FALSE en caso contrario
	 */
	private boolean parametroNoTieneValor(String parametro) {
		return ((parametro==null) || (parametro.equals("")));
	}

	/**
	 * Método interno que obtiene el valor del campo parámetro de la request
	 * @param request
	 * @return String valor del parámetro o NULL en caso contrario
	 */
	private String obtenerValorParametro(HttpServletRequest request) {
		return request.getParameter("param");
	}
	
	/**
	 * Método que comprueba si exise una columna en una lista dada
	 * @param lista lista de columnas
	 * @param col columnas a localizar
	 * @return TRUE si existe, FALSE en caso contrario
	 */
	private Boolean contieneColumna(EncapsuladorListSW<AtributoDto> lista, AtributoDto col) {
		for ( AtributoDto attr : lista ){
			if ( attr.getNombre().equals(col.getNombre()))
				return true;
		}
		return false;
	}
	
	/**
	 * Método que comprueba si la lista de posiciones es vacia
	 * @param listaPosicionesAmbitoDiagrama
	 * @return
	 */
	private boolean listaPosicionesNoEsVacia(ArrayList<Integer> listaPosicionesAmbitoDiagrama) {
		return (listaPosicionesAmbitoDiagrama != null && listaPosicionesAmbitoDiagrama.size()>0);
	}
	
	/**
	 * Método que obtiene la lista ordenada a partir de una lista dada
	 * @param listaValoresDiagrama lista de valores dada
	 * @return lista ordenada
	 */
	private List<ParDiagramaBarrasDto> obtenerListaOrdenadaCompleta(List<ParDiagramaBarrasDto> listaValoresDiagrama) {
		
		List<ParDiagramaBarrasDto>listaOrdenada=new ArrayList<ParDiagramaBarrasDto>();
		int tamanhoListaValoresDiagrama = listaValoresDiagrama.size();
		for(int i=0; i<tamanhoListaValoresDiagrama;i++){
			ParDiagramaBarrasDto objParDiagramaBarras = listaValoresDiagrama.get(i);
			if((objParDiagramaBarras!=null && objParDiagramaBarras.getValorAmbito()!=null) && 
					(objParDiagramaBarras!=null && objParDiagramaBarras.getValor()!=null)) {
				listaOrdenada.add(objParDiagramaBarras);
			}
		}
		return listaOrdenada;
	}
	
	/**
	 * Método que obtiene la listas de columnas a visualizar en el formulario previo a la generación del informe PDF. La lista de columnas dependerá
	 * de si lo que se está consultando es un histórico o no
	 * @param idHistorico
	 * @param model
	 * @param resultado
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private void obtenerListaColumnasSeleccionPDF(String idHistorico, Model model, IndicadorDto resultado) throws JsonParseException, JsonMappingException, IOException {
		
		UrlConstructorSW url;
		if (idHistorico != null && !idHistorico.equals("0")) {
			AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
			HistoricoDto historico = null;
			url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("historico").setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
			historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));
			//Si trabajamos sobre una versión histórica, listaColumnas tenemos que obtenerla directamente del Histórico Temporal
			EncapsuladorListSW<AtributoDto> listaColumnasHistorico = obtenerListaColumnasHistorico(datosHistoricosTemporal);
			model.addAttribute("listaColumnas", listaColumnasHistorico);
		} else {
			//Obtenemos las columnas para su representación		
			EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(resultado.getId());
			
			//CAMBIO: SELECCIONAMOS UNICAMENTE LOS ATRIBUTOS QUE NO SON RELACION
			EncapsuladorListSW<AtributoDto> listaColumnasAux = new EncapsuladorListSW<AtributoDto>();
			for (AtributoDto atributoColumnas : listaColumnas) {			
				if (atributoColumnas.getRelacion()==null) {
					listaColumnasAux.add(atributoColumnas);
				}
			}
			model.addAttribute("listaColumnas", listaColumnasAux);
		}
	}
	
	/**
	 * Método que obtiene una lista con las columnas leidas de los datos históricos
	 * @param datosHistoricosTemporal datos históricos
	 * @return lista de atributos 
	 */
	private EncapsuladorListSW<AtributoDto> obtenerListaColumnasHistorico(AtributoMapDto datosHistoricosTemporal) {
		//En el caso de trabajar con históricos no podremos usar listaColumnas, tendremos que calcular listaColumnasHistorico
		EncapsuladorListSW<AtributoDto> listaColumnasHistorico = new EncapsuladorListSW<AtributoDto>();
		
		Set<String> setlistaColumnas = datosHistoricosTemporal.getContenido().keySet();
		Iterator<String> itlistaColumnas = setlistaColumnas.iterator();
		String strlistaColumna = null;
		AtributoDto atributoListaColumnas = null;
		AtributoHistoricoDto atributoListaColumnasHist = null;
		AtributoValoresDto objAtributoValoresDto = null;
		while (itlistaColumnas.hasNext()) {
			//Por cada columna recuperada del histórico  
			strlistaColumna = itlistaColumnas.next();
			objAtributoValoresDto = datosHistoricosTemporal.getContenido().get(strlistaColumna);
			if (objAtributoValoresDto!=null && objAtributoValoresDto.getAtributo()!=null) {
				atributoListaColumnasHist = objAtributoValoresDto.getAtributo();
				atributoListaColumnas = convertirAtributoHistoricoEnDto(atributoListaColumnasHist);
				listaColumnasHistorico.add(atributoListaColumnas);
			}
			objAtributoValoresDto=null;
			atributoListaColumnasHist=null;
			atributoListaColumnas=null;
		}
		return listaColumnasHistorico;
	}
	
	/**
	 * Método que calcula el número de elementos que poseen la lista de datos que contiene
	 * la estructura datosRepresentar (para el diagrama de sectores y para el diagrama de barras)
	 * @param datosRepresentar
	 * @return int número de elementos de la lista
	 */
	private int calcularNumFilasDatosRepresentar(Set<Entry<String, ValorFDDto>> datosRepresentar) {
		int numFilas = 0;
		Iterator<Map.Entry<String,ValorFDDto>> itDatosRepresentar = datosRepresentar.iterator();
		while (itDatosRepresentar.hasNext()) {
			Entry<String,ValorFDDto> elemento = itDatosRepresentar.next();
			numFilas = elemento.getValue().getValores().size();
			return numFilas;
		}
		return numFilas;
	}
	
	/**
	 * Método que obtiene una referencia a la columna mapa, si existe en la lista de columnas(atributos) de un indicador
	 * @param listaColumnas
	 * @return AtributoFuenteDatosDto referencia a la columna mapa si existe, NULL si no existe
	 */
	private AtributoFuenteDatosDto obtenerColumnaMapa(EncapsuladorListSW<AtributoDto> listaColumnas) {
		AtributoFuenteDatosDto columnaAtributoMapa = null;			
		for (AtributoDto attr : listaColumnas) {			
			if (attr.getEsMapa()) { 
				columnaAtributoMapa = attr.getColumna();	
				break;
			}
		}
		return columnaAtributoMapa;
	}
	
	/**
	 * Método que recalcula el valor del parámetro. El parámetro puede venir como argumento del método o directamente de la request. Se realiza esta comprobación.
	 * También es posible que el parámetro no esté en la lista de columnas si existe diferencia entre la versión actual y alguna del histórico. Se comprueba para
	 * evitar errores
	 * @param parametro
	 * @param request
	 * @param listaColumnas
	 * @return valor del parámetro o NO_PARAMETRO si no existe valor
	 */
	private String calcularValorParametro(String parametro, HttpServletRequest request, EncapsuladorListSW<AtributoDto> listaColumnas) {
		
		if (parametroNoTieneValor(parametro)) {
			String strParametroRequest = obtenerValorParametro(request);
			if (parametroNoTieneValor(strParametroRequest)) {
				parametro = NO_PARAMETRO;
			} else {
				if (existeParametroEnListaColumnas(listaColumnas, strParametroRequest)) {
					parametro = strParametroRequest;
				} else {
					parametro = NO_PARAMETRO;
				}
			}
		} else {
			if (!existeParametroEnListaColumnas(listaColumnas, parametro)) {
				parametro = NO_PARAMETRO;
			}
		}
		return parametro;
	}
	
	/**
	 * Método que elimina las columnas que no deben estar en listaColumnas para las distintas operaciones (diagramas, visualización datos)
	 * @param historico referencia al objeto histórico por si es necesario recuperar la información de ahi, se está consultando una versión histórica
	 * @param listaColumnas lista de columnas de la que se parte
	 * @param datosHistoricosTemporal referencia a los datos del histórico (necesario obtenerlos despues de un decompress
	 * @return devuelve la lista de atributos necesarios
	 */
	private List<AtributoDto> obtenerListaColumnasNoMapa(HistoricoDto historico, EncapsuladorListSW<AtributoDto> listaColumnas, AtributoMapDto datosHistoricosTemporal) {
		List<AtributoDto> listaColumnasNoMapa = new ArrayList<AtributoDto>();
		if (historico != null && historico.getDatosDto() != null) {

			//Se obtienen las columnas que se deben mostrar en el histórico de la estructura datosHistoricos
			Set<String> setAtributosHistorico = datosHistoricosTemporal.getContenido().keySet();
			Iterator<String> itAtributosHistorico = setAtributosHistorico.iterator();
			String strAtributoHistorico = null;
			AtributoValoresDto objAtributoValoresColumna = null;
			AtributoDto objAtributoMostrar = null;
			while (itAtributosHistorico.hasNext()) {
				strAtributoHistorico = (String)itAtributosHistorico.next();
				objAtributoValoresColumna = datosHistoricosTemporal.getContenido().get(strAtributoHistorico);
				if (objAtributoValoresColumna!=null && objAtributoValoresColumna.getAtributo()!=null) {
					objAtributoMostrar = convertirAtributoHistoricoEnDto(objAtributoValoresColumna.getAtributo());
					if (objAtributoMostrar!=null && objAtributoMostrar.getMostrar() &&
							(!objAtributoMostrar.getEsMapa()) && (!objAtributoMostrar.getEsAmbito()) &&
							(((objAtributoMostrar.getColumna() == null && objAtributoMostrar.getIndicadorExpresion() != null) || 
							 (objAtributoMostrar.getColumna() != null && objAtributoMostrar.getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO)) && 
							  objAtributoMostrar.getRelacion() == null))  {
						listaColumnasNoMapa.add(objAtributoMostrar);
					}
				}
			}
			
		} else {
			for (AtributoDto col : listaColumnas) {
				if (col.getMostrar()
						&& !col.getEsMapa()
						&& !col.getEsAmbito()
						&& (((col.getColumna() == null && col.getIndicadorExpresion() != null) || (col.getColumna() != null && col
								.getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO)) && col.getRelacion() == null))
					listaColumnasNoMapa.add(col);
			}
		}
		return listaColumnasNoMapa;
	}
}