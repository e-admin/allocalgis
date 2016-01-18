/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.comun.controladores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.AbstractClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.AbstractView;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErrorSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.comun.servicios.ServicioConfiguracionGeneral;
import es.dc.a21l.comun.servicios.ServicioSeguridad;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorExpresionDto;
import es.dc.a21l.fuente.cu.AtributoDto;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosHistoricoDto;
import es.dc.a21l.fuente.cu.AtributoHistoricoDto;
import es.dc.a21l.fuente.cu.AtributoMapDto;
import es.dc.a21l.fuente.cu.AtributoValoresDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.IndicadorExpresionHistoricoDto;
import es.dc.a21l.fuente.cu.ValorFDDto;

public class GenericAbstractController extends AbstractView {
	
	private static final Logger LOG = LoggerFactory.getLogger(GenericAbstractController.class);
	private MessageSource messageBundle;
	private ServicioSeguridad servicioSeguridad;
	private HttpHeaders headers;
	private RestTemplate restTemplate;
	private static final String VALIDATION_LIBRARY_PATH="org.springframework.validation.BindingResult.";
	private ServicioConfiguracionGeneral servicioConfiguracionGeneral;
	
	public MessageSource getMessageBundle() {
		return messageBundle;
	}
	
	@Autowired
	public void setMessageBundle(MessageSource messageBundle) {
		this.messageBundle = messageBundle;
	}
	
	@Autowired
	public void setServicioSeguridad(ServicioSeguridad servicioSeguridad) {
		this.servicioSeguridad = servicioSeguridad;
	}
	
	public ServicioSeguridad getServicioSeguridad() {
		return servicioSeguridad;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}
	
	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
	
	@Autowired
//	@Qualifier("es.dc.a21l.comun.servicios.ServicioConfiguracionGeneral")
	public void setServicioConfiguracionGeneral(
			ServicioConfiguracionGeneral servicioConfiguracionGeneral) {
		this.servicioConfiguracionGeneral = servicioConfiguracionGeneral;
	}
	
	public ServicioConfiguracionGeneral getServicioConfiguracionGeneral() {
		return servicioConfiguracionGeneral;
	}


	
//	{
//		 List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
//		  acceptableMediaTypes.add(MediaType.APPLICATION_XML);
//		  headers = new HttpHeaders();
//		  headers.setAccept(acceptableMediaTypes);
//	}


	

	public void guardaEnSesion(HttpServletRequest request, String key, Object value)
	  {
		request.getSession().setAttribute("" + key, value);
	  }
		
	public void borraEnSesion(HttpServletRequest request, String key) {
	    request.getSession().removeAttribute("" + key);
	  }
	
	  public Object cargaValorDeSesion(HttpServletRequest request, String key) {
		  return request.getSession().getAttribute("" + key);
	}
	  
	public Boolean hasErrorsSW(ResponseEntity entity){
		return !entity.getStatusCode().equals(HttpStatus.OK);
	}
	
	public BindingResult escribirErrores(BindingResult errors,EncapsuladorPOSTSW encapsuladorPOSTSW){
		if(encapsuladorPOSTSW.hashErrors()){
			List<EncapsuladorErrorSW> lista=encapsuladorPOSTSW.getListaDeErrores();
			for(EncapsuladorErrorSW temp:lista){
				errors.rejectValue(temp.getAtributo(), temp.getCadenaCodigoError());
			}
		}
		return errors;
	}
	
	public String irPaginaErrorSW(){
		return "errorTile";
	}
	  
	protected String getPathPaqueteValidacion(){
		return VALIDATION_LIBRARY_PATH;
	}
	
	
	@ExceptionHandler
	public String irSinPermisos(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.sendError(HttpStatus.FORBIDDEN.value());
		request.setAttribute("paginaActiva", null);
		return null;
	}
	
	  public String writeJson(Object dto){
	    try {
	      MappingJacksonHttpMessageConverter messageRender = new MappingJacksonHttpMessageConverter();
	      HttpOutputMessage mockResponse = new ServletServerHttpResponseMock();

	      messageRender.write(dto, MediaType.APPLICATION_JSON, mockResponse);

	      String JsonUTF = ((ByteArrayOutputStream)mockResponse.getBody()).toString("ISO-8859-1");
	      return new String(JsonUTF.getBytes());
	      
	    } catch (IOException ex) {
	    	throw new RuntimeException(ex.getMessage()); 
	    } catch (HttpMessageNotWritableException ex) {
	    	throw new RuntimeException(ex.getMessage());
	    }
	  }
	  
	public byte[] writeArrayJson(Object dto) {
		try {
			MappingJacksonHttpMessageConverter messageRender = new MappingJacksonHttpMessageConverter();
			HttpOutputMessage mockResponse = new ServletServerHttpResponseMock();

			messageRender.write(dto, MediaType.APPLICATION_JSON, mockResponse);

			String JsonUTF = ((ByteArrayOutputStream) mockResponse.getBody()).toString("UTF-8");
			return JsonUTF.getBytes();

		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage());
		} catch (HttpMessageNotWritableException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	  
	public AtributosMapDto writeObject(byte[] json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		AtributosMapDto map = new AtributosMapDto();
		String datos = new String(json);
		map = mapper.readValue(datos, AtributosMapDto.class);
		return map;
	}
	
	/*
	 * Método incorporado para comprimir un objeto AtributoMapDto
	 */
	public AtributoMapDto writeObjectOtro(byte[] json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		AtributoMapDto map = new AtributoMapDto();
		String datos = new String(json);
		map = mapper.readValue(datos, AtributoMapDto.class);
		return map;
	}

	public static byte[] compress(byte[] content){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(content);
            gzipOutputStream.close();
        } catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decompress(byte[] contentBytes){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out);
        } catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
        return out.toByteArray();
    }

	  
	  private class ServletServerHttpResponseMock extends AbstractClientHttpRequest {
		  private ServletServerHttpResponseMock() {
		  	}
		  protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
			  return null;
		  }
		  public HttpMethod getMethod() { 
			  return null; 
		  } 
		  public URI getURI() { 
			  return null;
		  }
	  }
	  
	  protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	        response.setContentType((String)model.get("type"));
	        response.setHeader("Content-disposition", "attachment; filename=\"" + model.get("name")+"\"");
	        InputStream in = (InputStream)model.get("inputStream");
	        ByteArrayOutputStream baos = createTemporaryOutputStream();
	        IOUtils.copy(in, baos);
	        in.close();
	        ServletOutputStream out = response.getOutputStream();
	        baos.writeTo(out);
	        out.flush();
	    }

		@ModelAttribute("errorTamanhoArchivo")
		public Boolean errorTamanoFichero(@RequestParam(value="errorTamanhoArchivo",required=false) Boolean errorTamanhoArchivo){
			return errorTamanhoArchivo;
		}
		
		public String obtenerMensajeLocal(String prop, String var) throws IOException {
			String idioma = obtenerLocale();
			Resource resource = null;
			if ( idioma == null || idioma.equals(""))
				resource = new ClassPathResource("/mensajes.properties");
			else
				resource = new ClassPathResource("/mensajes_"+idioma+".properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			String propiedad = props.getProperty(prop);
			Object[] argumentos = {var};
			MessageFormat form = new MessageFormat(propiedad);
			return form.format(argumentos);
		}
		
		@ModelAttribute("idioma")
		public String obtenerLocale(){
			Locale locale = LocaleContextHolder.getLocale();
			return locale.getLanguage();
		}
		
		/*
		 * Método que se encarga de comprobar si en la lista de columnas obtenidas del indicador existe una atributo
		 * concreto. Si es así lo recupera y lo devuelve 
		 */
		public AtributoDto siExisteAtributoEnListaColumnas (String strNombreColumna, EncapsuladorListSW<AtributoDto> listaColumnas) {
			
			AtributoDto columnaConDatos = null;
			
			for (AtributoDto atributo : listaColumnas) {
				if (atributo!=null && atributo.getNombre().equals(strNombreColumna)) {
					columnaConDatos = atributo;
				}
			}
			return columnaConDatos;
		}
		
		/*
		 * Método que convierte un objeto AtributoDto en AtributoHistoricoDto 
		 */
		public AtributoHistoricoDto convertirAtributoEnHistorico(AtributoDto objAtributoListaColumnas) {
			
			AtributoHistoricoDto objAtributoHistoricoDto = new AtributoHistoricoDto();
			
			objAtributoHistoricoDto.setId(objAtributoListaColumnas.getId());
			objAtributoHistoricoDto.setResultadoOperacion(objAtributoListaColumnas.getResultadoOperacion());
			objAtributoHistoricoDto.setNombre(objAtributoListaColumnas.getNombre());
			objAtributoHistoricoDto.setOrdenVisualizacion(objAtributoListaColumnas.getOrdenVisualizacion());
			objAtributoHistoricoDto.setMostrar(objAtributoListaColumnas.getMostrar());
			objAtributoHistoricoDto.setEsAmbito(objAtributoListaColumnas.getEsAmbito());
			objAtributoHistoricoDto.setEsMapa(objAtributoListaColumnas.getEsMapa());

			/* Si el atributo tiene campo COLUMNA */
			AtributoFuenteDatosHistoricoDto objColumnaAtributo = null;
			if (objAtributoListaColumnas.getColumna()!=null) {
				objColumnaAtributo = new AtributoFuenteDatosHistoricoDto(); 
				objColumnaAtributo.setId(objAtributoListaColumnas.getColumna().getId());
				objColumnaAtributo.setResultadoOperacion(objAtributoListaColumnas.getColumna().getResultadoOperacion());
				objColumnaAtributo.setNombre(objAtributoListaColumnas.getColumna().getNombre());
				objColumnaAtributo.setTipoAtributo(objAtributoListaColumnas.getColumna().getTipoAtributo());
				objColumnaAtributo.setDefinicion(objAtributoListaColumnas.getColumna().getDefinicion());
				objColumnaAtributo.setEsFormula(objAtributoListaColumnas.getColumna().getEsFormula());
				objColumnaAtributo.setEsRelacion(objAtributoListaColumnas.getColumna().getEsRelacion());
				objColumnaAtributo.setStrTipoDatoRelacion(objAtributoListaColumnas.getColumna().getStrTipoDatoRelacion());
				objColumnaAtributo.setStrColumnaRelacion(objAtributoListaColumnas.getColumna().getStrColumnaRelacion());
				objColumnaAtributo.setStrTablaRelacion(objAtributoListaColumnas.getColumna().getStrTablaRelacion());
				objColumnaAtributo.setStrFuenteRelacion(objAtributoListaColumnas.getColumna().getStrFuenteRelacion());
			}
			objAtributoHistoricoDto.setColumna(objColumnaAtributo);
			
			/* Si el atributo tiene campo INDICADOR EXPRESIÓN */
			IndicadorExpresionHistoricoDto objIndicadorExpresionColumna = null;
			if (objAtributoListaColumnas.getIndicadorExpresion()!=null) {
				objIndicadorExpresionColumna = new IndicadorExpresionHistoricoDto();
				objIndicadorExpresionColumna.setId(objAtributoListaColumnas.getIndicadorExpresion().getId());
				objIndicadorExpresionColumna.setResultadoOperacion(objAtributoListaColumnas.getIndicadorExpresion().getResultadoOperacion());
				objIndicadorExpresionColumna.setIdIndicador(objAtributoListaColumnas.getIndicadorExpresion().getIdIndicador());
				objIndicadorExpresionColumna.setIdExpresion(objAtributoListaColumnas.getIndicadorExpresion().getIdExpresion());
				objIndicadorExpresionColumna.setExpresionLiteral(objAtributoListaColumnas.getIndicadorExpresion().getExpresionLiteral());
				objIndicadorExpresionColumna.setExpresionTransformada(objAtributoListaColumnas.getIndicadorExpresion().getExpresionTransformada());
			}
			objAtributoHistoricoDto.setIndicadorExpresion(objIndicadorExpresionColumna);
			
			
			return objAtributoHistoricoDto;
		}
		
		/*
		 * Método que convierte un objeto AtributoHistoricoDto en AtributoDto
		 */
		public AtributoDto convertirAtributoHistoricoEnDto(AtributoHistoricoDto objAtributoDatosHistorico) {
			
			AtributoDto objAtributoDto = new AtributoDto();
			
			objAtributoDto.setId(objAtributoDatosHistorico.getId());
			objAtributoDto.setResultadoOperacion(objAtributoDatosHistorico.getResultadoOperacion());
			objAtributoDto.setNombre(objAtributoDatosHistorico.getNombre());
			objAtributoDto.setOrdenVisualizacion(objAtributoDatosHistorico.getOrdenVisualizacion());
			objAtributoDto.setMostrar(objAtributoDatosHistorico.getMostrar());
			objAtributoDto.setEsAmbito(objAtributoDatosHistorico.getEsAmbito());
			objAtributoDto.setEsMapa(objAtributoDatosHistorico.getEsMapa());

			/* Si el atributo tiene campo COLUMNA */
			AtributoFuenteDatosDto objColumnaAtributo = null;
			if (objAtributoDatosHistorico.getColumna()!=null) {
				objColumnaAtributo = new AtributoFuenteDatosDto(); 
				objColumnaAtributo.setId(objAtributoDatosHistorico.getColumna().getId());
				objColumnaAtributo.setResultadoOperacion(objAtributoDatosHistorico.getColumna().getResultadoOperacion());
				objColumnaAtributo.setNombre(objAtributoDatosHistorico.getColumna().getNombre());
				objColumnaAtributo.setTipoAtributo(objAtributoDatosHistorico.getColumna().getTipoAtributo());
				objColumnaAtributo.setDefinicion(objAtributoDatosHistorico.getColumna().getDefinicion());
				objColumnaAtributo.setEsFormula(objAtributoDatosHistorico.getColumna().getEsFormula());
				objColumnaAtributo.setEsRelacion(objAtributoDatosHistorico.getColumna().getEsRelacion());
				objColumnaAtributo.setStrTipoDatoRelacion(objAtributoDatosHistorico.getColumna().getStrTipoDatoRelacion());
				objColumnaAtributo.setStrColumnaRelacion(objAtributoDatosHistorico.getColumna().getStrColumnaRelacion());
				objColumnaAtributo.setStrTablaRelacion(objAtributoDatosHistorico.getColumna().getStrTablaRelacion());
				objColumnaAtributo.setStrFuenteRelacion(objAtributoDatosHistorico.getColumna().getStrFuenteRelacion());
				objColumnaAtributo.setTabla(null);
			}
			objAtributoDto.setColumna(objColumnaAtributo);
			
			/* Si el atributo tiene campo INDICADOR EXPRESIÓN */
			IndicadorExpresionDto objIndicadorExpresionColumna = null;
			if (objAtributoDatosHistorico.getIndicadorExpresion()!=null) {
				objIndicadorExpresionColumna = new IndicadorExpresionDto();
				objIndicadorExpresionColumna.setId(objAtributoDatosHistorico.getIndicadorExpresion().getId());
				objIndicadorExpresionColumna.setResultadoOperacion(objAtributoDatosHistorico.getIndicadorExpresion().getResultadoOperacion());
				objIndicadorExpresionColumna.setIdIndicador(objAtributoDatosHistorico.getIndicadorExpresion().getIdIndicador());
				objIndicadorExpresionColumna.setIdExpresion(objAtributoDatosHistorico.getIndicadorExpresion().getIdExpresion());
				objIndicadorExpresionColumna.setExpresionLiteral(objAtributoDatosHistorico.getIndicadorExpresion().getExpresionLiteral());
				objIndicadorExpresionColumna.setExpresionTransformada(objAtributoDatosHistorico.getIndicadorExpresion().getExpresionTransformada());
			}
			objAtributoDto.setIndicadorExpresion(objIndicadorExpresionColumna);
			
			objAtributoDto.setCriterio(null);
			objAtributoDto.setRelacion(null);
			
			return objAtributoDto;
		}
		
		/*
		 * Método que convierte una estructura AtributoMapDto en AtributosMapDato
		 */
		public AtributosMapDto convertirEnAtributosMapDto (AtributoMapDto objAtributoMapDto) {
			
			AtributosMapDto objAtributosMapDto = new AtributosMapDto();
			LinkedHashMap<String,ValorFDDto> contenidoAtributosMapDto = new LinkedHashMap<String,ValorFDDto>();
			
			//Se obtienen las columnas que se deben mostrar en el histórico de la estructura datosHistoricos
			Set<String> setAtributosHistorico = objAtributoMapDto.getContenido().keySet();
			Iterator<String> itAtributosHistorico = setAtributosHistorico.iterator();
			String strAtributoHistorico = null;
			AtributoValoresDto objAtributoValoresColumna = null;
			while (itAtributosHistorico.hasNext()) {
				strAtributoHistorico = (String)itAtributosHistorico.next();			
				objAtributoValoresColumna = objAtributoMapDto.getContenido().get(strAtributoHistorico);
				contenidoAtributosMapDto.put(strAtributoHistorico, objAtributoValoresColumna.getListaValores());
			}
			objAtributosMapDto.setContenido(contenidoAtributosMapDto);
			return objAtributosMapDto;
		}
		
		/*
		 * Método que comprueba en un mapa elementos (categorias o indicadores) 
		 * si existe una valor concreto
		 */
		public boolean existeElementoEnMapa(Long idElementoEntrada, boolean esCategoria, 
				Map<Long,List<CategoriaDto>> mapaElementosCategoria,
				Map<Long, List<IndicadorDto>> mapaElementosIndicadores) {
			
			boolean encontradoElemento = false;
			
			Set<Long> elementos = null;
			if (esCategoria) {
				elementos = mapaElementosCategoria.keySet();
			} else {
				elementos = mapaElementosIndicadores.keySet();
			}
			
			Iterator<Long> itIdElementos = elementos.iterator();
			Long idElemento = null;
			while (itIdElementos.hasNext()) {
				idElemento = itIdElementos.next();
				List<CategoriaDto> elementosHijosCategoria = null;
				List<IndicadorDto> elementosHijosIndicador = null;
				List<Long> elementosHijosLong = null;
				if (esCategoria) {
					elementosHijosCategoria = mapaElementosCategoria.get(idElemento);
					elementosHijosLong = obtenerListaCategoriasLong(elementosHijosCategoria);
				} else {
					elementosHijosIndicador = mapaElementosIndicadores.get(idElemento);
					elementosHijosLong = obtenerListaIndicadoresLong(elementosHijosIndicador);
				}
				
				if (elementosHijosLong.contains(idElementoEntrada)) {
					encontradoElemento=true;
					break;
				}
			}		
			return encontradoElemento;
		}
		
		/*
		 * Método que convierte una lista de objetos IndicadorDto a una lista de Longs
		 */
		public List<Long> obtenerListaIndicadoresLong(List<IndicadorDto> listaIndicadoresHijo) {
			List<Long> listaIndicadoresHijoL = new ArrayList<Long>();
			for (IndicadorDto indicador : listaIndicadoresHijo) {
				listaIndicadoresHijoL.add(indicador.getId());
			}
			return listaIndicadoresHijoL;
		}
		
		/*
		 * Método que convierte una lista de objetos CategoriaDto a una lista de Longs
		 */
		public List<Long> obtenerListaCategoriasLong(List<CategoriaDto> listaCategoriasHijo) {
			List<Long> listaCategoriasHijoL = new ArrayList<Long>();
			for (CategoriaDto categoria : listaCategoriasHijo) {
				listaCategoriasHijoL.add(categoria.getId());
			}
			return listaCategoriasHijoL;
		}
}
