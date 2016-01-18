/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.indicador.controlador;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.dc.a21l.base.cu.ResultadosOperaciones;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorFileSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorIntegerSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorMapSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.base.utils.enumerados.TipoFecha;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.base.utils.enumerados.TiposLetra;
import es.dc.a21l.comun.controladores.GenericAbstractController;
import es.dc.a21l.comun.utils.LeerXML;
import es.dc.a21l.comun.utils.UrlConstructorSW;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.criterio.cu.CriterioDto;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.cu.ElementoJerarquiaDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorBusquedaAvanzadaDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorExpresionDto;
import es.dc.a21l.elementoJerarquia.cu.ParDiagramaBarrasDto;
import es.dc.a21l.elementoJerarquia.cu.RelacionDto;
import es.dc.a21l.elementoJerarquia.cu.RelacionesJspDto;
import es.dc.a21l.elementoJerarquia.cu.impl.IndicadorDtoFormErrorsEmun;
import es.dc.a21l.expresion.cu.TipoOperacionEmun;
import es.dc.a21l.fuente.cu.AtributoDto;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributoHistoricoDto;
import es.dc.a21l.fuente.cu.AtributoMapDto;
import es.dc.a21l.fuente.cu.AtributoValoresDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.historico.cu.HistoricoDto;
import es.dc.a21l.metadatos.cu.MetadatosDto;
import es.dc.a21l.usuario.cu.UsuarioDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaBarrasDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaSectoresDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionMapaDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionTablaDto;
import es.dc.a21l.visualizacion.cu.RangosVisualizacionMapaDto;

@Controller
@RequestMapping(value = "/indicadores.htm")
public class IndicadorControlador extends GenericAbstractController {
	private static final Logger log = LoggerFactory.getLogger(IndicadorControlador.class);
	private static final String NO_PARAMETRO = "no";
	private static final String TAMANHO_FUENTE_DEFECTO = "12.0";
	private static final String TAMANHO_FUENTE_DEFECTO_EXPORTAR = "12";
	private static final String TAMANHO_COLUMNA_DEFECTO = "40";
	private static final String COLOR_DEFECTO = "#00632E";
	private static final String COLORES_DEFECTO = "||rgb(241, 253, 201)||rgb(214, 235, 142)||rgb(156, 181, 69)||rgb(96, 108, 55)||rgb(59, 64, 43)";
	private static final String FUENTE_DEFECTO = "Arial";
	private static final String TIPO_FECHA_DEFECTO = TipoFecha.CORTA.getDescripcion();
	private static final String DIAMETRO_DEFECTO = "550";
	private static final String ERROR_NO_MAPA_SHAPEFILE = "_ERROR_NO_MAPA_SHAPEFILE_";

	private boolean bolExitoEliminarIndicador = false;
	private boolean bolExitoEliminarCategoria = false;
	private boolean bolExitoEliminarVersion = false;
	
	private boolean bolSiAccionEliminarIndicador = false;
	private boolean bolSiAccionEliminarCategoria = false;
	private boolean bolSiAccionEliminarVersion = false;
	
	private boolean bolErrorBorrarPermisosIndicador = false;
	private boolean bolErrorBorrarPermisosCategoria = false;
	private boolean bolEntidadUtilizada = false;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	public String cargaTablaElementosJerarquia(Model model, HttpServletRequest request, 
			@RequestParam(required = false, value = "idIndicadorSenhalado") Long idIndicadorSenhalado) {
		
		model.addAttribute("idIndicadorSenhalado", idIndicadorSenhalado);
		
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadores = new HashMap<Long, List<IndicadorDto>>();

		if (getUserAdmin()) {
			// Se recuperan las categorías e indicadores raíz
			UrlConstructorSW urlCat = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPadre");
			UrlConstructorSW urlInd = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores").setParametroCadena("cargaSinCategoria");
			
			List<CategoriaDto> listaCategorias = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
			List<Long> categoriasPadre = new ArrayList<Long>();
			
			if (listaCategorias != null && listaCategorias.size() > 0) {
				mapaCategorias.put(0L, listaCategorias);
			}
			
			List<IndicadorDto> listaIndicadores = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
			
			if (listaIndicadores != null && listaIndicadores.size() > 0) {
				mapaIndicadores.put(0L, listaIndicadores);
			}

			urlCat.borraUltimoParametroCadena().setParametroCadena("cargaCategoriasPorPadre");
			urlInd.borraUltimoParametroCadena().setParametroCadena("cargaPorCategoria");
		
			int indiceMapaCategorias = 0;
			// Se recorren todas las categorías, para buscar las categorías/indicadores que de ellas dependen
			while (indiceMapaCategorias < mapaCategorias.size()) {
				Iterator<Entry<Long, List<CategoriaDto>>> it = mapaCategorias.entrySet().iterator();
				
				// Se recorre el mapa buscando el índice cuyo valor coincida con el id de la categoría padre
				while (it.hasNext()) {
					Map.Entry elementoActual = (Map.Entry)it.next();
					
					// Si las categorías son Hijo de la Categoría Padre buscada
					if (elementoActual != null && elementoActual.getKey() != null && !categoriasPadre.contains(((Long)elementoActual.getKey()).longValue())) {
						List<CategoriaDto> categorias = (List<CategoriaDto>)elementoActual.getValue();
						
						for(CategoriaDto categoriaMadre : categorias) {
							// Se buscan categorías hijo
							urlCat.setParametroCadena(categoriaMadre.getId());
							List<CategoriaDto> listaCategoriasHijo = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
							// Si se obtienen resultados
							if (listaCategoriasHijo != null && listaCategoriasHijo.size() > 0) {
								mapaCategorias.put(categoriaMadre.getId(), listaCategoriasHijo);
							}
							
							// Se buscan indicadores que cuelguen de esta categoría
							urlInd.setParametroCadena(categoriaMadre.getId());
							List<IndicadorDto> listaIndicadoresHijo = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
							// Si se obtienen resultados
							if (listaIndicadoresHijo != null && listaIndicadoresHijo.size() > 0) {
								mapaIndicadores.put(categoriaMadre.getId(), listaIndicadoresHijo);
							}
			
							// Se resetean los parámetros de los servicios Web
							urlCat.borraUltimoParametroCadena();
							urlInd.borraUltimoParametroCadena();
						}
						
						// Se actualiza el índice en el mapa de categorías
						indiceMapaCategorias++;
						// Se añade el IdCategoriaPadre actual a la lista de revisados
						categoriasPadre.add(((Long)elementoActual.getKey()).longValue());						
						break;
					}
				}
			}
		//SI USUARIO NO ES ADMINISTRADOR (INVITADO, USUARIO NORMAL)
		} else {
			
			//Se recuperan las categorías e indicadores raíz
			UrlConstructorSW urlCat = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPermisosEdicion");
			urlCat.setParametroCadena(0); // Id. Categoria
			urlCat.setParametroCadena(getUsuarioId()); // Id. Usuario
			List<CategoriaDto> listaCategorias = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
			List<Long> categoriasPadre = new ArrayList<Long>();
			
			//Se incluye en la carga de categorias, la categorias de indicadores públicos
			UrlConstructorSW urlCategoriasPadrePublicas = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPadrePublicos");
			List<CategoriaDto> listaCategoriasPublicas = getRestTemplate().getForEntity(urlCategoriasPadrePublicas.getUrl(), EncapsuladorListSW.class).getBody();
			if (listaCategoriasPublicas!=null && listaCategoriasPublicas.size() > 0) {
				
				for (CategoriaDto categoriaPublica: listaCategoriasPublicas) {
					if (!listaCategorias.contains(categoriaPublica)) {
						listaCategorias.add(categoriaPublica);
					}
				}				
			}
			
			if (listaCategorias != null && listaCategorias.size() > 0) {
				mapaCategorias.put(0L, listaCategorias);
			}
			
			UrlConstructorSW urlInd = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores").setParametroCadena("cargaIndicadoresSinCategoriaPublicosOPorPermisoUsuario");
			urlInd.setParametroCadena(getUsuarioId());
			List<IndicadorDto> listaIndicadores = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
			
			//List<IndicadorDto> listaIndicadoresFinal = new ArrayList<IndicadorDto>();			
			if (listaIndicadores != null && listaIndicadores.size() > 0) {
				
				Iterator<IndicadorDto> itListaIndicadores = listaIndicadores.iterator();
				while (itListaIndicadores.hasNext()) {
					IndicadorDto indicador = itListaIndicadores.next();
					if (indicador.getPublico() && indicador.getPteAprobacionPublico()) { 
						itListaIndicadores.remove();
					}
				}
				mapaIndicadores.put(0L, listaIndicadores);
			}
			
			int indiceMapaCategorias = 0;
			// Se recorren todas las categorías, para buscar las categorías/indicadores que de ellas dependen
			while (indiceMapaCategorias < mapaCategorias.size()) {
				Iterator<Entry<Long, List<CategoriaDto>>> it = mapaCategorias.entrySet().iterator();
				
				// Se recorre el mapa buscando el índice cuyo valor coincida con el id de la categoría padre
				while (it.hasNext()) {
					Map.Entry elementoActual = (Map.Entry)it.next();
					
					// Si las categorías son Hijo de la Categoría Padre buscada
					if (elementoActual != null && elementoActual.getKey() != null && !categoriasPadre.contains(((Long)elementoActual.getKey()).longValue())) {
						
						List<CategoriaDto> categorias = (List<CategoriaDto>)elementoActual.getValue();
						
						for(CategoriaDto categoriaMadre : categorias) {
							//Se buscan categorías hijo para las que el usuario tiene permisos
							urlCat = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPermisosEdicion");
							urlCat.setParametroCadena(categoriaMadre.getId());
							urlCat.setParametroCadena(getUsuarioId());
							List<CategoriaDto> listaCategoriasHijo = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
							
							//Se buscan las categorias hijo públicas
							UrlConstructorSW urlCategoriasHijaPublico = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPorPadrePublicos");
							urlCategoriasHijaPublico.setParametroCadena(categoriaMadre.getId());
							List<CategoriaDto> listaCategoriasHijoPublico = getRestTemplate().getForEntity(urlCategoriasHijaPublico.getUrl(), EncapsuladorListSW.class).getBody();

							if (listaCategoriasHijoPublico!=null && listaCategoriasHijoPublico.size() > 0) {
								
								for (CategoriaDto categoriaHijaPublica: listaCategoriasHijoPublico) {
									if (!listaCategoriasHijo.contains(categoriaHijaPublica)) {
										listaCategoriasHijo.add(categoriaHijaPublica);
									}
								}				
							}
														
							if (listaCategoriasHijo != null && listaCategoriasHijo.size() > 0) {
								mapaCategorias.put(categoriaMadre.getId(), listaCategoriasHijo);
							}
							
							// Se buscan indicadores que cuelguen de esta categoría, para los que el usuario tenga permisos
							urlInd = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores").setParametroCadena("cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario");
							urlInd.setParametroCadena(getUsuarioId());
							urlInd.setParametroCadena(categoriaMadre.getId());
							List<IndicadorDto> listaIndicadoresHijo = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
							
							
							if (listaIndicadoresHijo != null && listaIndicadoresHijo.size() > 0) {
								Iterator<IndicadorDto> itListaIndicadores = listaIndicadoresHijo.iterator();
								while (itListaIndicadores.hasNext()) {
									IndicadorDto indicador = itListaIndicadores.next();
									if (indicador.getPublico() && indicador.getPteAprobacionPublico()) { 
										itListaIndicadores.remove();
									}
								}
								mapaIndicadores.put(categoriaMadre.getId(), listaIndicadoresHijo);
							}
							
						}
						
						// Se actualiza el índice en el mapa de categorías
						indiceMapaCategorias++;
						// Se añade el IdCategoriaPadre actual a la lista de revisados
						categoriasPadre.add(((Long)elementoActual.getKey()).longValue());						
						break;
					}
				}
			}
		}
		
		model.addAttribute("mapaCategoriasTabla", mapaCategorias);
		model.addAttribute("mapaIndicadoresTabla", mapaIndicadores);
		
		//Gestionar los mensajes de Exito o Error de una eliminación para incorporar las variables en MODEL
		if (bolSiAccionEliminarIndicador) {
			
			if (bolErrorBorrarPermisosIndicador) {
				model.addAttribute(ResultadosOperaciones.ERROR_BORRAR_PERMISOS.toString() , true);
				bolSiAccionEliminarIndicador = false;
				bolSiAccionEliminarCategoria = false;
				return "indicadoresTile";
			}
			
			if (bolExitoEliminarIndicador) {
				model.addAttribute(ResultadosOperaciones.EXITO_BORRAR.toString(), true);
			} else {
				model.addAttribute(ResultadosOperaciones.ERROR_BORRAR + "Indicador", true);
			}
			bolSiAccionEliminarIndicador = false;
			bolSiAccionEliminarCategoria = false;
			return "indicadoresTile";
		}
		
		if (bolSiAccionEliminarVersion) {
			
			if (bolEntidadUtilizada) {
				model.addAttribute("resultadoOperacion", ResultadosOperaciones.ENTIDAD_UTILIZADA.getResultado());
				bolSiAccionEliminarIndicador = false;
				bolSiAccionEliminarCategoria = false;
				bolSiAccionEliminarVersion = false;
				return "indicadoresTile";
			}
			
			if (bolExitoEliminarVersion) {
				//model.addAttribute("resultadoOperacion", ResultadosOperaciones.EXITO_BORRAR.getResultado());
				model.addAttribute(ResultadosOperaciones.EXITO_BORRAR.toString(), true);
			} else {
				//model.addAttribute("resultadoOperacion", ResultadosOperaciones.ERROR_BORRAR.getResultado());
				model.addAttribute(ResultadosOperaciones.ERROR_BORRAR + "Versión", true);
			}
			bolSiAccionEliminarIndicador = false;
			bolSiAccionEliminarCategoria = false;
			bolSiAccionEliminarVersion = false;
			return "indicadoresTile";
		}
		
		if (bolSiAccionEliminarCategoria) {
			
			if (bolErrorBorrarPermisosCategoria) {
				model.addAttribute(ResultadosOperaciones.ERROR_BORRAR_PERMISOS.toString() , true);
				bolSiAccionEliminarIndicador = false;
				bolSiAccionEliminarCategoria = false;
				bolSiAccionEliminarVersion = false;
				return "indicadoresTile";
			}
			
			if (bolExitoEliminarCategoria) {
				model.addAttribute(ResultadosOperaciones.EXITO_BORRAR.toString(), true);
			} else {
				model.addAttribute(ResultadosOperaciones.ERROR_BORRAR + "Categoria", true);
			}
		}
		
		bolSiAccionEliminarIndicador = false;
		bolSiAccionEliminarCategoria = false;
		bolSiAccionEliminarVersion = false;
		return "indicadoresTile";
		
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, params = { "accion=editaCategoria" })
	public String editaCategoria(@RequestParam(value = "id") Long id, @RequestParam(value = "idCatSel", required = false) Long idCatSel, 
			Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (usuarioInvitado()) {
			return irSinPermisos(request, response);
		}
		
		// TODO hacer permisos usuario por categoria

		CategoriaDto categoriaDto = null;
		List<CategoriaDto> lista = null;
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		
		//ES UNA MODIFICACIÓN
		if (id > 0) {	
			
			if (!getUserAdmin()) {
				UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionCategoria");
				url.setParametroCadena(id);
				url.setParametroCadena(getUsuarioId());
				
				if (!(getRestTemplate().getForEntity(url.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico())) {
					return irSinPermisos(request, response);
				}
			}
			
			categoriaDto = getRestTemplate().getForEntity(new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaPorId").setParametroCadena(id).getUrl(), CategoriaDto.class).getBody();
			idCatSel = categoriaDto.getIdCategoriaPadre() != null ? categoriaDto.getIdCategoriaPadre() : 0L;
			
			//Almacena la categoría padre de la categoría seleccionada
			lista = new ArrayList<CategoriaDto>();			
			if (idCatSel != 0L) {
				lista.add(getRestTemplate().getForEntity(new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaPorId").setParametroCadena(idCatSel).getUrl(), CategoriaDto.class).getBody());
			}
		//ES UNA NUEVA CATEGORIA
		} else {		
			
			if (idCatSel==null) {
				return irSinPermisos(request, response);
			}
			
			//Solo obtenemos la categoriaPadreSeleccionada si lo que se a seleccionado no es la cagoria RAIZ
			CategoriaDto categoriaPadreSeleccionadaDto = null;
			if (idCatSel!=null && idCatSel!=0L) { 				
				UrlConstructorSW urlCategoriaPadreSeleccionada = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaPorId");
				urlCategoriaPadreSeleccionada.setParametroCadena(idCatSel);
				categoriaPadreSeleccionadaDto = getRestTemplate().getForEntity(urlCategoriaPadreSeleccionada.getUrl(),CategoriaDto.class).getBody();
			}
			
			//Solo comprobamos el permiso sobre categorias que no sean la RAIZ
			if (idCatSel!=null && idCatSel!=0L) {
				if (!getUserAdmin()) {
					Long idCategoriaSeleccionada = new Long(categoriaPadreSeleccionadaDto.getId());
					
					if(idCategoriaSeleccionada!=null) {
						UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionCategoria");
						url2.setParametroCadena(categoriaPadreSeleccionadaDto.getId());
						url2.setParametroCadena(getUsuarioId());
						
						if (!(getRestTemplate().getForEntity(url2.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico())) {
							return irSinPermisos(request, response);
						}
					}
				}
			}
			
			categoriaDto = new CategoriaDto();
			//Se recuperan las categorías que cuelgan de la categoría raíz
			UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPermisosEdicion");
			url.setParametroCadena(0); // Id. Categoria
			url.setParametroCadena(getUsuarioId()); // Id. Usuario
			
			lista = (List<CategoriaDto>)getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();

			if (lista != null && lista.size() > 0) {
				mapaCategorias.put(0L, lista);
			}
			
			Stack<Long> keycategoriasPorVer=new Stack<Long>();
			keycategoriasPorVer.push(0L);
			
			// Se recorren todas las categorías, para buscar las categorías que de ellas dependen
			List<CategoriaDto> listaTodasCategoriasHijo = new ArrayList<CategoriaDto>();
			while (!keycategoriasPorVer.empty()) {
				Long keyTratar=keycategoriasPorVer.pop();
				List<CategoriaDto> categorias = (List<CategoriaDto>)mapaCategorias.get(keyTratar);
				
				for(CategoriaDto categoriaMadre : categorias) {
					//Se buscan categorías hijo
					url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPermisosEdicion");
					url.setParametroCadena(categoriaMadre.getId());
					url.setParametroCadena(getUsuarioId());
					List<CategoriaDto> listaCategoriasHijo = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();
					
					listaTodasCategoriasHijo.addAll(listaCategoriasHijo);
					// Si se obtienen resultados
					if (listaCategoriasHijo != null && listaCategoriasHijo.size() > 0) {
						listaTodasCategoriasHijo.addAll(listaCategoriasHijo);
						mapaCategorias.put(categoriaMadre.getId(), listaCategoriasHijo);
						keycategoriasPorVer.push(categoriaMadre.getId());
					}
				}
			}
				
			// Si hai categorías para las que el usuario tiene permisos, se procede a su formateo
			if (mapaCategorias != null && mapaCategorias.size() > 0) {
				lista = formateaCategoriasParaSelectPermisos(mapaCategorias);
			}
		}

		model.addAttribute("listaCategoriasPadre", lista);
		model.addAttribute("categoriaDto", categoriaDto);
		model.addAttribute("idCategoriaSeleccionada", idCatSel);
		
		return "categoriaTile";
	}

	@RequestMapping(method = RequestMethod.POST, params = { "accion=guardaCategoria" })
	public String guardaCategoria(@ModelAttribute CategoriaDto categoriaDto, BindingResult result, Model model,
			@RequestParam(value = "idCatSel", required = false) Long idCatSel, HttpServletRequest request,
			HttpServletResponse response) throws Exception {


		if (usuarioInvitado())
			return irSinPermisos(request, response);

		//Se obtiene los datos de la categoría original si es una modificación. Cuando se crea una categoria ID = 0
		if (categoriaDto!=null && categoriaDto.getId() != 0L ) {
			UrlConstructorSW urlCategoriaOrigen = new UrlConstructorSWImpl(getUrlBaseSw());
			urlCategoriaOrigen.setParametroCadena("categorias").setParametroCadena("cargaPorId").setParametroCadena(categoriaDto.getId());
			CategoriaDto categoriaOrigenDto = getRestTemplate().getForEntity(urlCategoriaOrigen.getUrl(), CategoriaDto.class).getBody();
			
			model.addAttribute("categoriaOrigenDto", categoriaOrigenDto);
		}

		if (!getUserAdmin()) {
			Long idCategoriaPadre = new Long(categoriaDto.getIdCategoriaPadre());
			if(idCategoriaPadre!=null) {
				UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionCategoria");
				url2.setParametroCadena(idCategoriaPadre);
				url2.setParametroCadena(getUsuarioId());
				
				if (!(getRestTemplate().getForEntity(url2.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico())) {
					model.addAttribute("mostrarNombreCategoriaOriginal",true);
					editaCategoria(categoriaDto.getId(), idCatSel, model, request, response);
					model.addAttribute("categoriaDto", categoriaDto);
					model.addAttribute(getPathPaqueteValidacion() + "categoriaDto", result);
					return "categoriaTile";
				}
			}
		}
		
		
		if (result.hasErrors()) {
			
			model.addAttribute("mostrarNombreCategoriaOriginal",true);
			editaCategoria(categoriaDto.getId(), idCatSel, model, request, response);
			model.addAttribute("categoriaDto", categoriaDto);
			model.addAttribute(getPathPaqueteValidacion() + "categoriaDto", result);
			return "categoriaTile";
		}
		
		CategoriaDto categoriaDto2 = guardaCategoria(categoriaDto, result);

		if (result.hasErrors()) {
			model.addAttribute("mostrarNombreCategoriaOriginal",true);
			editaCategoria(categoriaDto.getId(), idCatSel, model, request, response);
			model.addAttribute("categoriaDto", categoriaDto);
			model.addAttribute(getPathPaqueteValidacion() + "categoriaDto", result);
			return "categoriaTile";
		}
		
		if (categoriaDto.getId() == 0L) {
			categoriaDto2.setResultadoOperacion(ResultadosOperaciones.EXITO_CREAR);
		} else {
			categoriaDto2.setResultadoOperacion(ResultadosOperaciones.EXITO_GUARDAR);
		}
		model.addAttribute("mostrarNombreCategoriaOriginal",false);	
		model.addAttribute("categoriaDto", categoriaDto2);
		return cargaTablaElementosJerarquia(model, request, null);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET, params = { "accion=editaIndicador" })
	public String editaIndicador(@RequestParam(value = "id") Long id,
			@RequestParam(value = "idCategoria", required = false) Long idCategoria, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer numCampos = 0;
		Integer numFormulas = 0;
		Integer numRelaciones = 0;
		Integer numCriterios = 0;

		if (usuarioInvitado())
			return irSinPermisos(request, response);
		IndicadorDto indicadorDto = id < 1 
			? new IndicadorDto(idCategoria > 0 ? idCategoria : null) 
			: getRestTemplate().getForEntity(new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(id).getUrl(), IndicadorDto.class)
				.getBody();

		model.addAttribute("indicadorDto", indicadorDto);	
		if (!usuarioInvitado()&& !getUserAdmin())
		{
			UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionIndicador");
			url.setParametroCadena(id);
			url.setParametroCadena(getUsuarioId());
			
			if (!(getRestTemplate().getForEntity(url.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico()))
			{
				if(idCategoria!=null){
					UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionCategoria");
					url2.setParametroCadena(idCategoria);
					url2.setParametroCadena(getUsuarioId());
					
					if (!(getRestTemplate().getForEntity(url2.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico()))
					{
						
						model.addAttribute(ResultadosOperaciones.ERROR_EDITAR_PERMISOS.toString() , true);
						return cargaTablaElementosJerarquia(model, request, null);
					}
				}else{
					model.addAttribute(ResultadosOperaciones.ERROR_EDITAR_PERMISOS.toString() , true);
					return cargaTablaElementosJerarquia(model, request, null);
				}
			}
		}

		// Necesito los campos / tablas / columnas / etc.
		// Preparamos los datos para su visualizacion
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("atributos");
		url.setParametroCadena("atributos");
		url.setParametroCadena(id);

		// Cargo los criterios para este indicador
		UrlConstructorSW urlCrit = new UrlConstructorSWImpl(getUrlBaseSw());
		urlCrit.setParametroCadena("criterios");
		urlCrit.setParametroCadena("cargaCriterios");
		urlCrit.setParametroCadena(id);

		EncapsuladorListSW<CriterioDto> listaCriterios = getRestTemplate().getForEntity(urlCrit.getUrl(), EncapsuladorListSW.class).getBody();
		EncapsuladorListSW<AtributoDto> listaColumnas = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();
		EncapsuladorListSW<AtributoDto> listaColumnasCopia = new EncapsuladorListSW<AtributoDto>();
		EncapsuladorListSW<AtributoDto> listaColumnasJsp = new EncapsuladorListSW<AtributoDto>();

		numCriterios = listaCriterios.size();

		List<TablaFuenteDatosDto> listaTablas = new ArrayList<TablaFuenteDatosDto>();
		Map<String, EncapsuladorListSW<AtributoFuenteDatosDto>> columnasTabla = new HashMap<String, EncapsuladorListSW<AtributoFuenteDatosDto>>();
		boolean encontrado = false;
		for (AtributoDto col : listaColumnas) {
			// Si es un atributo que corresponde a una formula o una relacion, nos saltamos esta iteración.A
			// if ( col.getIndicadorExpresion()!=null ||
			// col.getRelacion()!=null)
			// continue;
			// Solo sumamos 1 al numero de campos si es un campo normal que no sea formula ni relacion
			if (!(col.getIndicadorExpresion() != null || col.getRelacion() != null))
				numCampos++;
			else {
				listaColumnasCopia.add(col);
				continue;
			}

			for (TablaFuenteDatosDto tabla : listaTablas) {
				if (tabla.getId() == col.getColumna().getTabla().getId()) {
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				listaTablas.add(col.getColumna().getTabla());
			}
			encontrado = false;

			UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw());
			url2.setParametroCadena("fuentes");
			url2.setParametroCadena("columnasFuente");
			url2.setParametroCadena(col.getColumna().getTabla().getNombre());
			url2.setParametroCadena(col.getColumna().getTabla().getFuente().getId());
			url2.setParametroCadena(col.getColumna().getTabla().getFuente().getTipo().getId());
			ResponseEntity<EncapsuladorListSW> listaColumnasTabla = getRestTemplate().getForEntity(url2.getUrl(), EncapsuladorListSW.class);
			columnasTabla.put(col.getColumna().getTabla().getNombre(), listaColumnasTabla.getBody());

			if (col.getEsAmbito()) {
				model.addAttribute("tabla_ambito", col.getColumna().getTabla().getNombre());
				model.addAttribute("columna_ambito", col.getColumna().getNombre());
			}
			if (col.getEsMapa()) {
				model.addAttribute("tabla_mapa", col.getColumna().getTabla().getNombre());
				model.addAttribute("columna_mapa", col.getColumna().getNombre());
			}

			// CRITERIOS columna normal
			for (CriterioDto criterio : listaCriterios) {
				// Si vienen vacios los valores, es porque se añadió un campo y luego se se le dio a quitar.
				UrlConstructorSW urlCol = new UrlConstructorSWImpl(getUrlBaseSw());
				urlCol.setParametroCadena("atributos");
				urlCol.setParametroCadena("atributo");
				urlCol.setParametroCadena(criterio.getIdAtributo());
				AtributoDto colCriterio = getRestTemplate().getForEntity(urlCol.getUrl(), AtributoDto.class).getBody();
				
				// Si es un criterio de un campo de una tabla
				if (colCriterio.getIndicadorExpresion() == null && colCriterio.getColumna() != null && colCriterio.getRelacion() == null) {
					// Si el nombre de la columna y el nombre de la tabla son iguales, esta columna tiene un criterio
					if (col.getNombre().equals(colCriterio.getColumna().getNombre()) && col.getColumna().getTabla().getNombre().equals(colCriterio.getColumna().getTabla().getNombre())) {
						CriterioDto critTmp = new CriterioDto();
						critTmp.setCadenaCriterio(criterio.getCadenaCriterio());
						AtributoDto attrTmp = new AtributoDto();
						AtributoFuenteDatosDto attrFDTmp = new AtributoFuenteDatosDto();
						TablaFuenteDatosDto tablaTmp = new TablaFuenteDatosDto();
						FuenteDto fuenteTmp = new FuenteDto();
						tablaTmp.setNombre(colCriterio.getColumna().getTabla().getNombre());
						fuenteTmp.setId(colCriterio.getColumna().getTabla().getFuente().getId());
						tablaTmp.setFuente(fuenteTmp);
						attrFDTmp.setNombre(colCriterio.getColumna().getNombre());
						attrFDTmp.setTabla(tablaTmp);
						attrTmp.setColumna(attrFDTmp);
						critTmp.setIdAtributo(colCriterio.getId());
						critTmp.setAtributo(attrTmp);
						col.setCriterio(critTmp);
					}
				}
			}
			
			listaColumnasCopia.add(col);
		}

		// FORMULAS
		for (AtributoDto col : listaColumnasCopia) {
			// Si es un atributo que corresponde a una columna o a una relacion, nos saltamos esta iteración.
			// Si es un campo normal, lo añadimos a la lista q va a la jsp
			if (col.getColumna() != null || col.getRelacion() != null) {
				if (col.getRelacion() == null) {
					listaColumnasJsp.add(col);
				}
				else if (col.getColumna() != null) {
					listaColumnasJsp.add(col);
				}
				
				continue;
			}

			numFormulas++;

			AtributoDto attr = new AtributoDto();

			attr.setIndicadorExpresion(col.getIndicadorExpresion());
			attr.setEsAmbito(false);
			attr.setEsMapa(false);
			attr.setMostrar(col.getMostrar());
			attr.setNombre(col.getNombre());
			attr.setOrdenVisualizacion(col.getOrdenVisualizacion());
			AtributoFuenteDatosDto att = new AtributoFuenteDatosDto();
			TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
			tabla.setNombre(col.getIndicadorExpresion().getExpresionLiteral());
			att.setTabla(tabla);
			attr.setColumna(att);

			// CRITERIOS columna formula
			for (CriterioDto criterio : listaCriterios) {
				UrlConstructorSW urlCol = new UrlConstructorSWImpl(getUrlBaseSw());
				urlCol.setParametroCadena("atributos");
				urlCol.setParametroCadena("atributo");
				urlCol.setParametroCadena(criterio.getIdAtributo());
				AtributoDto colCriterio = getRestTemplate().getForEntity(urlCol.getUrl(), AtributoDto.class).getBody();
				
				// Si es una fórmula
				if (colCriterio.getIndicadorExpresion() != null && colCriterio.getRelacion() == null) {
					if (col.getNombre().equals(colCriterio.getNombre()) && col.getIndicadorExpresion().getExpresionLiteral().equals(colCriterio.getIndicadorExpresion().getExpresionLiteral())) {				
						CriterioDto critTmp = new CriterioDto();
						critTmp.setCadenaCriterio(criterio.getCadenaCriterio());
						AtributoDto attrTmp = new AtributoDto();
						AtributoFuenteDatosDto attrFDTmp = new AtributoFuenteDatosDto();
						TablaFuenteDatosDto tablaTmp = new TablaFuenteDatosDto();
						tablaTmp.setNombre(attr.getIndicadorExpresion().getExpresionLiteral());
						attrFDTmp.setNombre(attr.getNombre());
						attrFDTmp.setTabla(tablaTmp);
						attrTmp.setColumna(attrFDTmp);
						critTmp.setIdAtributo(colCriterio.getId());
						critTmp.setAtributo(attrTmp);
						attr.setCriterio(critTmp);
					}
				}
			}
			
			listaColumnasJsp.add(attr);
		}

		// RELACIONES
		List<RelacionesJspDto> listaRelaciones = new ArrayList<RelacionesJspDto>();
		for (AtributoDto col : listaColumnasCopia) {
			// Si es un atributo que corresponde a una columna o a una formula, nos saltamos esta iteración.
			if (col.getColumna() != null || col.getIndicadorExpresion() != null) {
				continue;
			}

			TablaFuenteDatosDto tablaFuenteDatosDto = new TablaFuenteDatosDto();
			FuenteDto fuenteDto = new FuenteDto();

			// Tabla relacionada
			tablaFuenteDatosDto = new TablaFuenteDatosDto();
			fuenteDto = new FuenteDto();
			
			String idFuenteRelacionada = col.getRelacion().getIdFuenteRelacionada();
			String strTablaRelacionada = col.getRelacion().getNombreTablaRelacionada();
			
			String idFuente = col.getRelacion().getIdFuenteRelacionada();
			String tabla = col.getRelacion().getNombreTablaRelacionada();

			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("tablas");
			url.setParametroCadena("tabla");
			url.setParametroCadena(indicadorDto.getId());
			url.setParametroCadena(tabla);
			url.setParametroCadena(idFuente);

			tablaFuenteDatosDto = getRestTemplate().getForEntity(url.getUrl(), TablaFuenteDatosDto.class).getBody();

			if (!contiene(listaTablas, tablaFuenteDatosDto)) {
				listaTablas.add(tablaFuenteDatosDto);
				// Para cada tabla, obtengo sus columnas para mostrarlas
				url = new UrlConstructorSWImpl(getUrlBaseSw());
				url.setParametroCadena("fuentes");
				url.setParametroCadena("fuente");
				url.setParametroCadena(idFuente);

				fuenteDto = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();

				UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw());
				url2.setParametroCadena("fuentes");
				url2.setParametroCadena("columnasFuente");
				url2.setParametroCadena(tabla);
				url2.setParametroCadena(idFuente);
				url2.setParametroCadena(fuenteDto.getTipo().getId());
				ResponseEntity<EncapsuladorListSW> listaColumnasTabla = getRestTemplate().getForEntity(url2.getUrl(),
						EncapsuladorListSW.class);
				columnasTabla.put(tabla, listaColumnasTabla.getBody());
			}

			// Tabla relacion
			idFuente = col.getRelacion().getIdFuenteRelacion();
			tabla = col.getRelacion().getNombreTablaRelacion();

			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("tablas");
			url.setParametroCadena("tabla");
			url.setParametroCadena(indicadorDto.getId());
			url.setParametroCadena(tabla);
			url.setParametroCadena(idFuente);

			tablaFuenteDatosDto = getRestTemplate().getForEntity(url.getUrl(), TablaFuenteDatosDto.class).getBody();

			ResponseEntity<EncapsuladorListSW> listaColumnasTabla = null;
			
			if (!contiene(listaTablas, tablaFuenteDatosDto)) {
				listaTablas.add(tablaFuenteDatosDto);
				// Para cada tabla, obtengo sus columnas para mostrarlas
				url = new UrlConstructorSWImpl(getUrlBaseSw());
				url.setParametroCadena("fuentes");
				url.setParametroCadena("fuente");
				url.setParametroCadena(idFuente);

				fuenteDto = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();

				UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw());
				url2.setParametroCadena("fuentes");
				url2.setParametroCadena("columnasFuente");
				url2.setParametroCadena(tabla);
				url2.setParametroCadena(idFuente);
				url2.setParametroCadena(fuenteDto.getTipo().getId());
				listaColumnasTabla = getRestTemplate().getForEntity(url2.getUrl(), EncapsuladorListSW.class);
			} 
			else {
				listaColumnasTabla = new ResponseEntity<EncapsuladorListSW>(columnasTabla.get(tabla), HttpStatus.OK);
			}

			List<AtributoFuenteDatosDto> lista = new ArrayList<AtributoFuenteDatosDto>();
			lista.addAll(listaColumnasTabla.getBody().getLista());
						
			//-----------------------------------------------------------------------------------------------------
			//SE CREA EL ATRIBUTO RELACION. ESTE DEBE CREARSE COMO SI FUERA INICIAL, ES DECIR, IGUAL QUE EN GUARDAR
			AtributoFuenteDatosDto nuevaColumna = new AtributoFuenteDatosDto();
			TablaFuenteDatosDto tablaColumna = new TablaFuenteDatosDto();
			FuenteDto objFuenteRelacion = new FuenteDto();
			
			nuevaColumna.setNombre(col.getRelacion().getNombreColumnaRelacion());			
			
			nuevaColumna.setEsFormula(false);
			nuevaColumna.setEsRelacion(true);
			nuevaColumna.setTipoAtributo(TipoAtributoFD.VALORFDRELACION);
			 
			objFuenteRelacion.setId(Long.valueOf(idFuente));
			tablaColumna.setNombre(tabla);
			tablaColumna.setFuente(objFuenteRelacion);
			nuevaColumna.setTabla(tablaColumna);
			
			EncapsuladorListSW<AtributoFuenteDatosDto> objAtributosTablaRelacion = columnasTabla.get(strTablaRelacionada);
			List<AtributoFuenteDatosDto> listaAtributosRelacion = new ArrayList<AtributoFuenteDatosDto>();
			listaAtributosRelacion = objAtributosTablaRelacion.getLista();
			TipoAtributoFD tipoDatoRelacionado = null;
			//Se recorre la Lista de Atributos de la Tabla Relacionada para encontrar el tipo de DATO del atributo relación
			for (AtributoFuenteDatosDto objAtributoBuscar : listaAtributosRelacion) {
				if (objAtributoBuscar!=null && objAtributoBuscar.getNombre().equals(col.getRelacion().getNombreColumnaRelacionada())) {
					tipoDatoRelacionado = objAtributoBuscar.getTipoAtributo();
				}
			}
			nuevaColumna.setStrTipoDatoRelacion(tipoDatoRelacionado.toString());			
			nuevaColumna.setStrFuenteRelacion(idFuenteRelacionada);							
			nuevaColumna.setStrTablaRelacion(col.getRelacion().getNombreTablaRelacionada());
			nuevaColumna.setStrColumnaRelacion(col.getRelacion().getNombreColumnaRelacionada());
		 
			//----------------------------------------------------------------------------------------------------------
			
			lista.add(nuevaColumna);
			listaColumnasTabla.getBody().setLista(lista);
			columnasTabla.put(tabla, listaColumnasTabla.getBody());

			// Se va montando una lista con las relaciones
			RelacionesJspDto rel = new RelacionesJspDto();
			rel.setMostrar(col.getMostrar());
			rel.setTablaRelacion(col.getRelacion().getNombreTablaRelacion());
			rel.setColumnaRelacion(col.getRelacion().getNombreColumnaRelacion());
			rel.setIdFuenteRelacion(col.getRelacion().getIdFuenteRelacion());
			rel.setTablaRelacionada(col.getRelacion().getNombreTablaRelacionada());
			rel.setColumnaRelacionada(col.getRelacion().getNombreColumnaRelacionada());
			rel.setIdFuenteRelacionada(col.getRelacion().getIdFuenteRelacionada());
			rel.setOrdenVisualizacion("-1");

			/*
			 * SE COMENTA ESTA PARTE DE CÓDIGO PARA QUE AL VISUALIZAR EL DETALLE DEL INDICADOR
			 * NO SE VISUALICEN LAS RELACIONES EN LA TABLA
			AtributoDto attrRelacion = new AtributoDto();
			 
			if (col.getOrdenVisualizacion() != null) {
				rel.setOrdenVisualizacion(col.getOrdenVisualizacion().toString());
			}
			else {
				rel.setOrdenVisualizacion("0");
			}
			
			attrRelacion.setOrdenVisualizacion(col.getOrdenVisualizacion());

			AtributoFuenteDatosDto atributoFuenteDatos = new AtributoFuenteDatosDto();
			TablaFuenteDatosDto tablaFuenteDatos = new TablaFuenteDatosDto();
			attrRelacion.setNombre(col.getRelacion().getNombreColumnaRelacion() + "-"
					+ col.getRelacion().getNombreTablaRelacionada() + "."
					+ col.getRelacion().getNombreColumnaRelacionada());
			tablaFuenteDatos.setNombre(col.getRelacion().getNombreTablaRelacion());
			atributoFuenteDatos.setTabla(tablaFuenteDatos);
			attrRelacion.setColumna(atributoFuenteDatos);
			attrRelacion.setMostrar(col.getMostrar());
			// Se añade una relacion vacia, para que relacion sea != de null y se considere este cmapo una relacion
			attrRelacion.setRelacion(new RelacionDto());
			listaColumnasJsp.add(attrRelacion);
			*/
			listaRelaciones.add(rel);
			numRelaciones++;
		}

		model.addAttribute("listaTablas", listaTablas);
		model.addAttribute("listaColumnasTabla", columnasTabla);
		model.addAttribute("listaColumnasIndicador", listaColumnasJsp);
		model.addAttribute("listaRelaciones", listaRelaciones);
		model.addAttribute("listaCriterios", listaCriterios);

		model.addAttribute("numCampos", numCampos);
		model.addAttribute("numFormulas", numFormulas);
		model.addAttribute("numRelaciones", numRelaciones);
		model.addAttribute("numCriterios", numCriterios);

		// Tipos de operacion para las formulas
		List<TipoOperacionEmun> items = new ArrayList<TipoOperacionEmun>();
		
		for (TipoOperacionEmun tipo : TipoOperacionEmun.values()) {
			items.add(tipo);
		}
		
		model.addAttribute("tipoOperacion", items);

		return "indicadorTile";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, params = { "accion=guardaIndicador" })
	public String guardaIndicador(@ModelAttribute IndicadorDto indicadorDto, BindingResult result, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (usuarioInvitado())
			return irSinPermisos(request, response);

		indicadorDto.setLoginCreador(getServicioSeguridad().getUserDetails().getUsername());

		String columnaAmbito = request.getParameter("columna_campo_ambito");
		String tablaAmbito = request.getParameter("tabla_campo_ambito");
		String columnaMapa = request.getParameter("columna_campo_mapa");
		String tablaMapa = request.getParameter("tabla_campo_mapa");

		Integer numCampos = 0;
		
		if (request.getParameter("num_campos_envio") != null && !request.getParameter("num_campos_envio").equals("")) {
			numCampos = Integer.valueOf(request.getParameter("num_campos_envio"));
		}
		Integer numFormulas = 0;
		
		if (request.getParameter("num_formulas_envio") != null && !request.getParameter("num_formulas_envio").equals("")) {
			numFormulas = Integer.valueOf(request.getParameter("num_formulas_envio"));
		}
		Integer numRelaciones = 0;
		
		if (request.getParameter("num_relaciones_envio") != null && !request.getParameter("num_relaciones_envio").equals("")) {
			numRelaciones = Integer.valueOf(request.getParameter("num_relaciones_envio"));
		}
		Integer numCriterios = 0;
		
		if (request.getParameter("num_criterios_envio") != null && !request.getParameter("num_criterios_envio").equals("")) {
			numCriterios = Integer.valueOf(request.getParameter("num_criterios_envio"));
		}

		boolean erroresValidacion = false;
		List<String> listaErrores = new ArrayList<String>();

		// Validar datos del indicador
		if (columnaAmbito == null || columnaAmbito.equals("")) {
			erroresValidacion = true;
			listaErrores.add(IndicadorDtoFormErrorsEmun.SIN_AMBITO.getCadenaCodigoError());
		}

		if (numCampos == null || numCampos <= 0) {
			erroresValidacion = true;
			listaErrores.add(IndicadorDtoFormErrorsEmun.SIN_CAMPOS.getCadenaCodigoError());
		}

		boolean campoMostrar = false;
		//Recorre todos los campos para comprobar si al menos alguno se muestra, sino se devuelve ERROR
		for (int i = 1; i <= numCampos; i++) {
			if (request.getParameter("tabla_campo_" + i) == null && request.getParameter("columna_campo_" + i) == null
					&& request.getParameter("mostrar_campo_" + i) == null
					&& request.getParameter("idfuente_campo_" + i) == null)
				continue;
			if (request.getParameter("mostrar_campo_" + i) != null
					&& request.getParameter("mostrar_campo_" + i).equals("true"))
				campoMostrar = true;
		}

		if (!campoMostrar) {
			erroresValidacion = true;
			listaErrores.add(IndicadorDtoFormErrorsEmun.SIN_CAMPOS_MOSTRAR.getCadenaCodigoError());
		}

		if (erroresValidacion) {
			model.addAttribute("erroresValidacion", "true");
			model.addAttribute("listaErrores", listaErrores);
			model = rellenarDatosIndicadorPostValidacion(model, request);
			return "indicadorTile";
		}

		//comienza el guardado
		IndicadorDto indicadorDto2 = guardaIndicador(indicadorDto, result);

		if (result.hasErrors()) {
			model.addAttribute("indicadorDto", indicadorDto);
			model = rellenarDatosIndicadorPostValidacion(model, request);
			return "indicadorTile";
		}

		if (indicadorDto.getId() == 0) {
			indicadorDto2.setResultadoOperacion(ResultadosOperaciones.EXITO_CREAR);
		} 
		else {
			indicadorDto2.setResultadoOperacion(ResultadosOperaciones.EXITO_GUARDAR);
		}
		
		//--------------------------------------------------------------------------------------------------------------------------
		
		/* SE INCLUYE ESTE CÓDIGO AQUI PARA EJECUTARLO EN EL CASO DE NO ENTRAR POR EL MÉTODO "rellenarDatosIndicadorPostValidacion"
		 * EN ESTE CASO ES NECESARIO EJECUTARLO PARA ACTUALIZAR LA LISTA DE TABLAS, COLUMNAS Y RELACIONES QUE SERÁN LEIDAS EN LA VISTA
		 */
		String strFuentesTablas = null;
		if (request.getParameter("num_fuentes_tablas_envio")!=null 
				&& !request.getParameter("num_fuentes_tablas_envio").equals(""))
			strFuentesTablas = request.getParameter("num_fuentes_tablas_envio");

		String strRelacionesTablas = null;
		if (request.getParameter("num_relacionestablas_envio")!=null 
				&& !request.getParameter("num_relacionestablas_envio").equals(""))
			strRelacionesTablas = request.getParameter("num_relacionestablas_envio");
		
		Integer numRelacionesReal = 0;
		List<TablaFuenteDatosDto> listaTablas = new ArrayList<TablaFuenteDatosDto>();
		Map<String, EncapsuladorListSW<String>> columnasTabla = new HashMap<String, EncapsuladorListSW<String>>();
		List<RelacionesJspDto> listaRelaciones = new ArrayList<RelacionesJspDto>();
		List<RelacionesJspDto> listaRelacionesInterno = new ArrayList<RelacionesJspDto>();

		//FUENTES Y TABLAS (ZONA TABLAS) (si existen)
		if (strFuentesTablas!=null) {
			String[] listaFuentesTablasCargadas = strFuentesTablas.split(",");
	        for (int i = 0; i < listaFuentesTablasCargadas.length; i++) {
	        	String[] strFuenteTablaSeleccionada = listaFuentesTablasCargadas[i].split("\\|");
	        	//Por cada Fuente - Tabla cargarla en el formulario	        
	        	//Se obtiene el código de la fuente de datos asociado a la tabla que se va a cargar
	        	Long idFuente = Long.valueOf(strFuenteTablaSeleccionada[0]);
	        	//Se obtiene el nombre de la tabla que se va a cargar
	        	String tabla = 	strFuenteTablaSeleccionada[1];
	        	
	        	TablaFuenteDatosDto tablaFuenteDatosDto = new TablaFuenteDatosDto();
				FuenteDto fuenteDto = new FuenteDto();
				tablaFuenteDatosDto.setNombre(tabla);
				fuenteDto.setId(idFuente);
				tablaFuenteDatosDto.setFuente(fuenteDto);
	        	
				//Para cada tabla, obtengo sus columnas para mostrarlas
				UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
				url.setParametroCadena("fuentes");
				url.setParametroCadena("fuente");
				url.setParametroCadena(idFuente);

				fuenteDto = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();
				
				UrlConstructorSW url2 = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
				url2.setParametroCadena("fuentes");
				url2.setParametroCadena("columnasFuente");
				url2.setParametroCadena(tabla);
				url2.setParametroCadena(idFuente);
				url2.setParametroCadena(fuenteDto.getTipo().getId());
				ResponseEntity<EncapsuladorListSW> listaColumnasTabla = getRestTemplate().getForEntity(url2.getUrl(), EncapsuladorListSW.class);
								
				if ( !contiene(listaTablas,tablaFuenteDatosDto)) {
					listaTablas.add(tablaFuenteDatosDto);
										
					//RELACIONES ENTRE LAS TABLAS (ZONA TABLAS) (si existen)
					if (strRelacionesTablas!=null) {						
						String[] listaRelacionesTablas = strRelacionesTablas.split(",");			

						for (int j = 0; j < listaRelacionesTablas.length; j++) {
							
							//Por cada relación se obtienen sus valores
							 String[] strRelacionesTabla = listaRelacionesTablas[j].split("\\|");
							 AtributoFuenteDatosDto objAtributoFuenteDatosDto = new AtributoFuenteDatosDto();
							 TablaFuenteDatosDto objTablaFuenteDatosDto = new TablaFuenteDatosDto();
							 FuenteDto objFuenteOrigenDTO = new FuenteDto();
							 
							 Long strFuenteOrigen = Long.valueOf(strRelacionesTabla[0]);
							 String strTablaOrigen = strRelacionesTabla[1].substring("tabla_".length());
							 
							 if (strTablaOrigen!=null && !strTablaOrigen.equals(tablaFuenteDatosDto.getNombre()))
								 continue;
							 							
							 objAtributoFuenteDatosDto.setEsFormula(false);
							 objAtributoFuenteDatosDto.setEsRelacion(true);
							 objAtributoFuenteDatosDto.setTipoAtributo(TipoAtributoFD.VALORFDRELACION);
							 
							 objFuenteOrigenDTO.setId(strFuenteOrigen);							 
							 objTablaFuenteDatosDto.setNombre(strTablaOrigen);
							 objTablaFuenteDatosDto.setFuente(objFuenteOrigenDTO);
							 objAtributoFuenteDatosDto.setTabla(objTablaFuenteDatosDto);
							
							 objAtributoFuenteDatosDto.setNombre(strRelacionesTabla[2]);
							 objAtributoFuenteDatosDto.setStrTipoDatoRelacion(strRelacionesTabla[3]);
							 objAtributoFuenteDatosDto.setStrFuenteRelacion(strRelacionesTabla[4]);							
							 objAtributoFuenteDatosDto.setStrTablaRelacion(strRelacionesTabla[5]);
							 objAtributoFuenteDatosDto.setStrColumnaRelacion(strRelacionesTabla[6]);
							 							 
							 listaColumnasTabla.getBody().add(objAtributoFuenteDatosDto);
							 
							 RelacionesJspDto relaciones = new RelacionesJspDto();
							 relaciones.setMostrar(false);
							 relaciones.setIdFuenteRelacion(strRelacionesTabla[0]);
							 relaciones.setTablaRelacion(strTablaOrigen);
							 relaciones.setColumnaRelacion(strRelacionesTabla[2]);
							 relaciones.setIdFuenteRelacionada(strRelacionesTabla[4]);
							 relaciones.setTablaRelacionada(strRelacionesTabla[5]);
							 relaciones.setColumnaRelacionada(strRelacionesTabla[6]);
							 relaciones.setOrdenVisualizacion("-1");
							
							 listaRelaciones.add(relaciones);
							 listaRelacionesInterno.add(relaciones);
							 numRelacionesReal++;
							 
						}
					}					
					
					columnasTabla.put(tabla, listaColumnasTabla.getBody());
				}
	        }
		}
		
		//-----------------------------------------------------------------------------------------------------------------------------------------
		
		model.addAttribute("listaTablas", listaTablas);
		model.addAttribute("listaColumnasTabla", columnasTabla);
		model.addAttribute("listaRelaciones", listaRelaciones);
		model.addAttribute("numRelaciones", numRelacionesReal);
		
		// Primero borramos la información que teníamos del indicador para
		// guardar la nueva
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("atributos");
		url.setParametroCadena("borrarListaAtributos");
		url.setParametroCadena(indicadorDto.getId());

		AtributoDto attrDto = getRestTemplate().getForEntity(url.getUrl(), AtributoDto.class).getBody();
		
		if (attrDto != null)
		{
			attrDto = null;
		}

		// Guardamos las tablas/columnas/formulas/criterios de este indicador
		
		// CAMPOS
		for (int i = 1; i <= numCampos; i++) {
			// Si vienen vacios los valores, es pq se añadio un campo y luego se le dio a quitar.
			if (request.getParameter("tabla_campo_" + i) == null && request.getParameter("columna_campo_" + i) == null
					&& request.getParameter("mostrar_campo_" + i) == null
					&& request.getParameter("idfuente_campo_" + i) == null)
				continue;

			Long idFuente = Long.valueOf(request.getParameter("idfuente_campo_" + i));

			// Veo si la tabla existe en BD, sino la introduzco
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("tablas");
			url.setParametroCadena("tabla");
			url.setParametroCadena(indicadorDto2.getId());
			url.setParametroCadena(request.getParameter("tabla_campo_" + i));
			url.setParametroCadena(idFuente);

			TablaFuenteDatosDto tablaDto = getRestTemplate().getForEntity(url.getUrl(), TablaFuenteDatosDto.class).getBody();
			
			if (tablaDto == null || tablaDto.getId() <= 0) {
				url = new UrlConstructorSWImpl(getUrlBaseSw());
				url.setParametroCadena("tablas");
				url.setParametroCadena("tabla");

				TablaFuenteDatosDto tablaFuenteDatosDto = new TablaFuenteDatosDto();
				tablaFuenteDatosDto.setFuente(cargarFuente(idFuente));
				tablaFuenteDatosDto.setIndicador(indicadorDto2);
				tablaFuenteDatosDto.setNombre(request.getParameter("tabla_campo_" + i));

				tablaDto = (TablaFuenteDatosDto) getRestTemplate()
						.postForEntity(url.getUrl(), new HttpEntity<TablaFuenteDatosDto>(tablaFuenteDatosDto),
								EncapsuladorPOSTSW.class).getBody().getObjetoEncapsulado();
			}

			//Ademas introduzco el atributo relacionado con esta tabla
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("atributosFuente");
			url.setParametroCadena("atributo");

			// Obtengo las columnas de esta fuente, para obtener la definicion, el tipo, etc.
			// Para ello primero obtengo la fuente
			UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw());
			url2.setParametroCadena("fuentes");
			url2.setParametroCadena("fuente");
			url2.setParametroCadena(idFuente);
			FuenteDto fuenteDto = getRestTemplate().getForEntity(url2.getUrl(), FuenteDto.class).getBody();

			// Obtengo las columnas
			url2 = new UrlConstructorSWImpl(getUrlBaseSw());
			url2.setParametroCadena("fuentes");
			url2.setParametroCadena("columnasFuente");
			url2.setParametroCadena(request.getParameter("tabla_campo_" + i));
			url2.setParametroCadena(idFuente);
			url2.setParametroCadena(fuenteDto.getTipo().getId());

			EncapsuladorListSW<AtributoFuenteDatosDto> listaColumnas = getRestTemplate().getForEntity(url2.getUrl(),
					EncapsuladorListSW.class).getBody();
			
			AtributoFuenteDatosDto atributoFuenteDatosDto = new AtributoFuenteDatosDto();
			for (AtributoFuenteDatosDto attr : listaColumnas) {
				if (attr.getNombre().equals(request.getParameter("columna_campo_" + i))) {
					atributoFuenteDatosDto.setDefinicion(attr.getDefinicion());
					atributoFuenteDatosDto.setNombre(attr.getNombre());
					atributoFuenteDatosDto.setTipoAtributo(attr.getTipoAtributo());
					atributoFuenteDatosDto.setTabla(tablaDto);
					break;
				}
			}
			// Guardo la columna despues de obtener sus caracteristicas
			EncapsuladorPOSTSW<AtributoFuenteDatosDto> respuestaPostColumna = getRestTemplate().postForEntity(
					url.getUrl(), new HttpEntity<AtributoFuenteDatosDto>(atributoFuenteDatosDto),
					EncapsuladorPOSTSW.class).getBody();

			// Para cada campo, introduzco la entrada correspondiente en
			// Atributo
			url2 = new UrlConstructorSWImpl(getUrlBaseSw());
			url2.setParametroCadena("atributos");
			url2.setParametroCadena("atributo");
			AtributoDto atributoDto = new AtributoDto();
			atributoDto.setColumna(respuestaPostColumna.getObjetoEncapsulado());
			atributoDto.setIndicadorExpresion(null);
			atributoDto.setRelacion(null);
			boolean esAmbito = false;
			boolean esMapa = false;
			if (atributoDto.getColumna().getNombre().equals(columnaAmbito)
					&& atributoDto.getColumna().getTabla().getNombre().equals(tablaAmbito))
				esAmbito = true;
			if (atributoDto.getColumna().getNombre().equals(columnaMapa)
					&& atributoDto.getColumna().getTabla().getNombre().equals(tablaMapa))
				esMapa = true;

			atributoDto.setEsAmbito(esAmbito);
			atributoDto.setEsMapa(esMapa);

			Boolean mostrar = false;
			if (request.getParameter("mostrar_campo_" + i).equals("true"))
				mostrar = true;
			atributoDto.setMostrar(mostrar);
			atributoDto.setNombre(request.getParameter("columna_campo_" + i));
			if (request.getParameter("orden_campo_" + i) != null
					&& !request.getParameter("orden_campo_" + i).equals(""))
				atributoDto.setOrdenVisualizacion(Integer.valueOf(request.getParameter("orden_campo_" + i)));

			EncapsuladorPOSTSW<AtributoDto> respuestaAtributo = getRestTemplate().postForEntity(url2.getUrl(),
					new HttpEntity<AtributoDto>(atributoDto), EncapsuladorPOSTSW.class).getBody();

			// GUARDAR CRITERIOS DE CAMPOS NORMALES
			for (int j = 1; j <= numCriterios; j++) {
				// Si vienen vacios los valores, es pq se añadio un campo y luego se se le dio a quitar.
				if (request.getParameter("tabla_criterio_" + j) == null
						&& request.getParameter("columna_criterio_" + j) == null
						&& request.getParameter("valor_criterio_" + j) == null)
					continue;

				String nombreTablaCriterio = request.getParameter("tabla_criterio_" + j);
				String nombreColumnaCriterio = request.getParameter("columna_criterio_" + j);
				String valorCriterio = request.getParameter("valor_criterio_" + j);

				// Si el nombre de la columna y el nombre de la tabla son iguales, esta columna tiene un criterio
				if (atributoDto.getNombre().equals(nombreColumnaCriterio) && atributoDto.getColumna().getTabla().getNombre().equals(nombreTablaCriterio)) {
					// Guardar criterio
					url2 = new UrlConstructorSWImpl(getUrlBaseSw());
					url2.setParametroCadena("criterios");
					url2.setParametroCadena("guardaNuevoCriterio");
					url2.setParametroCadena(respuestaAtributo.getObjetoEncapsulado().getId());
					url2.setParametroCadena(atributoDto.getColumna().getTipoAtributo().getDescripcion());

					EncapsuladorStringSW cadenaCriterio = new EncapsuladorStringSW();
					cadenaCriterio.setTexto(valorCriterio);

					EncapsuladorPOSTSW<CriterioDto> respuestaCriterio = getRestTemplate().postForEntity(url2.getUrl(),
							new HttpEntity<EncapsuladorStringSW>(cadenaCriterio), EncapsuladorPOSTSW.class).getBody();

					if (respuestaCriterio == null || respuestaCriterio.getObjetoEncapsulado() == null) {
						// Error eliminamos el indicador
						if (indicadorDto.getId() == 0L) {
							url = new UrlConstructorSWImpl(getUrlBaseSw());
							url.setParametroCadena("indicadores").setParametroCadena("borraIndicador")
									.setParametroCadena(indicadorDto2.getId());
							EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(),
									EncapsuladorBooleanSW.class).getBody();
						}
						model.addAttribute("erroresValidacion", "true");
						listaErrores.add(IndicadorDtoFormErrorsEmun.ERROR_CRITERIO.getCadenaCodigoError());
						if(respuestaCriterio.getErrores().getListaErrores()!=null && !respuestaCriterio.getErrores().getListaErrores().isEmpty()){
							listaErrores.add(IndicadorDtoFormErrorsEmun.ERROR_CRITERIO_NUMERICO.getCadenaCodigoError());
						}
						model.addAttribute("listaErrores", listaErrores);
						model = rellenarDatosIndicadorPostValidacion(model, request);
						return "indicadorTile";
					}
				}
			}
		}

		// FORMULAS
		for (int i = 1; i <= numFormulas; i++) {
			// Si vienen vacios los valores, es pq se añadio un campo y luego se se le dio a quitar.
			if (request.getParameter("nombre_formula_" + i) == null
					&& request.getParameter("valor_formula_" + i) == null
					&& request.getParameter("orden_formula_" + i) == null
					&& request.getParameter("mostrar_formula_" + i) == null)
				continue;

			// Guarda la formula en expresion
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("indicadorExpresiones");
			url.setParametroCadena("guardaIndicadorExpresion");

			IndicadorExpresionDto indiExpre = new IndicadorExpresionDto();
			indiExpre.setExpresionLiteral(request.getParameter("valor_formula_" + i));
			indiExpre.setExpresionTransformada(request.getParameter("valor_formula_" + i));
			indiExpre.setIdIndicador(indicadorDto2.getId());

			EncapsuladorPOSTSW<IndicadorExpresionDto> respuestaExpresion = getRestTemplate().postForEntity(
					url.getUrl(), new HttpEntity<IndicadorExpresionDto>(indiExpre), EncapsuladorPOSTSW.class).getBody();

			if (respuestaExpresion == null || respuestaExpresion.getObjetoEncapsulado() == null) {
				// Error eliminamos el indicador
				if (indicadorDto.getId() == 0L) {
					url = new UrlConstructorSWImpl(getUrlBaseSw());
					url.setParametroCadena("indicadores").setParametroCadena("borraIndicador")
							.setParametroCadena(indicadorDto2.getId());
					EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(),
							EncapsuladorBooleanSW.class).getBody();
				}
				model.addAttribute("erroresValidacion", "true");
				listaErrores.add(IndicadorDtoFormErrorsEmun.ERROR_FORMULA.getCadenaCodigoError());
				model.addAttribute("listaErrores", listaErrores);
				model = rellenarDatosIndicadorPostValidacion(model, request);
				return "indicadorTile";
			}

			// Para cada campo, introduzco la entrada correspondiente en
			// Atributo
			UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw());
			url2.setParametroCadena("atributos");
			url2.setParametroCadena("atributo");
			AtributoDto atributoDto = new AtributoDto();
			atributoDto.setColumna(null);
			atributoDto.setRelacion(null);
			boolean esAmbito = false;
			boolean esMapa = false;

			atributoDto.setEsAmbito(esAmbito);
			atributoDto.setEsMapa(esMapa);

			Boolean mostrar = false;
			if (request.getParameter("mostrar_formula_" + i).equals("true"))
				mostrar = true;
			atributoDto.setMostrar(mostrar);
			atributoDto.setNombre(request.getParameter("nombre_formula_" + i));
			if (request.getParameter("orden_formula_" + i) != null
					&& !request.getParameter("orden_formula_" + i).equals(""))
				atributoDto.setOrdenVisualizacion(Integer.valueOf(request.getParameter("orden_formula_" + i)));

			IndicadorExpresionDto indiExpreDto = new IndicadorExpresionDto();
			indiExpreDto.setIdExpresion(respuestaExpresion.getObjetoEncapsulado().getIdExpresion());
			indiExpreDto.setIdIndicador(respuestaExpresion.getObjetoEncapsulado().getIdIndicador());
			indiExpreDto.setId(respuestaExpresion.getObjetoEncapsulado().getId());

			atributoDto.setIndicadorExpresion(indiExpreDto);

			EncapsuladorPOSTSW<AtributoDto> respuestaAtributo = getRestTemplate().postForEntity(url2.getUrl(),
					new HttpEntity<AtributoDto>(atributoDto), EncapsuladorPOSTSW.class).getBody();

			// GUARDAR CRITERIOS DE FORMULAS
			for (int j = 1; j <= numCriterios; j++) {
				// Si vienen vacios los valores, es pq se añadio un campo y luego se se le dio a quitar.
				if (request.getParameter("tabla_criterio_" + j) == null
						&& request.getParameter("columna_criterio_" + j) == null
						&& request.getParameter("valor_criterio_" + j) == null)
					continue;

				String nombreTablaCriterio = request.getParameter("tabla_criterio_" + j);
				String nombreColumnaCriterio = request.getParameter("columna_criterio_" + j);
				String valorCriterio = request.getParameter("valor_criterio_" + j);

				// Si el nombre de la columna y el nombre de la tabla son iguales, esta columna tiene un criterio
				if (atributoDto.getNombre().equals(nombreColumnaCriterio) && indiExpre.getExpresionLiteral().equals(nombreTablaCriterio)) {
					// Guardar criterio
					url2 = new UrlConstructorSWImpl(getUrlBaseSw());
					url2.setParametroCadena("criterios");
					url2.setParametroCadena("guardaNuevoCriterio");
					url2.setParametroCadena(respuestaAtributo.getObjetoEncapsulado().getId());
					url2.setParametroCadena(TipoAtributoFD.VALORFDNUMERICO.getDescripcion());

					EncapsuladorStringSW cadenaCriterio = new EncapsuladorStringSW();
					cadenaCriterio.setTexto(valorCriterio);

					EncapsuladorPOSTSW<CriterioDto> respuestaCriterio = getRestTemplate().postForEntity(url2.getUrl(),
							new HttpEntity<EncapsuladorStringSW>(cadenaCriterio), EncapsuladorPOSTSW.class).getBody();

					if (respuestaCriterio == null || respuestaCriterio.getObjetoEncapsulado() == null) {
						// Error eliminamos el indicador
						if (indicadorDto.getId() == 0L) {
							url = new UrlConstructorSWImpl(getUrlBaseSw());
							url.setParametroCadena("indicadores").setParametroCadena("borraIndicador")
									.setParametroCadena(indicadorDto2.getId());
							EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(),
									EncapsuladorBooleanSW.class).getBody();
						}
						model.addAttribute("erroresValidacion", "true");
						listaErrores.add(IndicadorDtoFormErrorsEmun.ERROR_CRITERIO.getCadenaCodigoError());
						model.addAttribute("listaErrores", listaErrores);
						model = rellenarDatosIndicadorPostValidacion(model, request);
						return "indicadorTile";
					}
				}
			}
		}

		//RELACIONES. AHORA LAS RELACIONES SE RECUPERAN DE LA LISTA CARGADA AL RECUPERARLAS
		//AL ACCEDER A OTRO FORMULARIO NO SE DISPONE DE VALORES EN LA REQUEST (SE COMENTÓ LA OBTENCIÓN DE LA INFORMACIÓN DESDE ESTE ORIGEN)
		//----------------------------------------------------------------------------------
		for (RelacionesJspDto objRelacionVista : listaRelacionesInterno) {
		//----------------------------------------------------------------------------------
			// Si vienen vacios los valores, es pq se añadio un campo y luego se
			// se le dio a quitar.
			/*if (request.getParameter("tabla_relacion_" + i) == null
					&& request.getParameter("columna_relacion_" + i) == null
					&& request.getParameter("orden_relacion_" + i) == null
					&& request.getParameter("mostrar_relacion_" + i) == null
					&& request.getParameter("tabla_relacionada_" + i) == null
					&& request.getParameter("columna_relacionada_" + i) == null)
				continue;
			*/
			// Para cada relacion, hay q meter una entrada en Atributo donde ira
			// el nombre, el idrelacion q indica q es una relacion
			// el mostrar y el orden de visualizacion
			// Para cada relacion ademas hacemos la entrada correspondiente en
			// Relacion
			// A la hora de leer estos datos, se leen las columnas del indicador
			// y si alguna es relacion se obtienen sus datos de la tabla
			// Relacion.
			// Guardamos la relacion en la tabla relacion
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("relaciones");
			url.setParametroCadena("guardaRelacion");

			RelacionDto relacion = new RelacionDto();
			relacion.setIdFuenteRelacion(objRelacionVista.getIdFuenteRelacion());
			relacion.setIdFuenteRelacionada(objRelacionVista.getIdFuenteRelacionada());				
			relacion.setNombreColumnaRelacion(objRelacionVista.getColumnaRelacion());
			relacion.setNombreColumnaRelacionada(objRelacionVista.getColumnaRelacionada());
			relacion.setNombreTablaRelacion(objRelacionVista.getTablaRelacion());
			relacion.setNombreTablaRelacionada(objRelacionVista.getTablaRelacionada());
			relacion.setIndicador(indicadorDto2);

			/*
			RelacionDto relacion = new RelacionDto();
			relacion.setIdFuenteRelacion(request.getParameter("idfuente_relacion_" + i));
			relacion.setIdFuenteRelacionada(request.getParameter("idfuente_relacionada_" + i));
			relacion.setNombreColumnaRelacion(request.getParameter("columna_relacion_" + i));
			relacion.setNombreColumnaRelacionada(request.getParameter("columna_relacionada_" + i));
			relacion.setNombreTablaRelacion(request.getParameter("tabla_relacion_" + i));
			relacion.setNombreTablaRelacionada(request.getParameter("tabla_relacionada_" + i));
			relacion.setIndicador(indicadorDto2);
			 */
			EncapsuladorPOSTSW<RelacionDto> respuestaRelacion = getRestTemplate().postForEntity(url.getUrl(),
					new HttpEntity<RelacionDto>(relacion), EncapsuladorPOSTSW.class).getBody();

			AtributoDto atributoDto = new AtributoDto();
			atributoDto.setColumna(null);
			atributoDto.setIndicadorExpresion(null);
			atributoDto.setEsAmbito(false);
			atributoDto.setEsMapa(false);

			Boolean mostrar = false;
			/*if (request.getParameter("mostrar_relacion_" + i).equals("true"))
				mostrar = true;*/
			atributoDto.setMostrar(mostrar);
			/*String nombre = request.getParameter("columna_relacion_" + i) + "-"
					+ request.getParameter("tabla_relacionada_" + i) + "."
					+ request.getParameter("columna_relacionada_" + i); */
			String nombre = objRelacionVista.getColumnaRelacion() + "-" 
					+ objRelacionVista.getTablaRelacionada() + "." 
					+ objRelacionVista.getColumnaRelacionada();
			atributoDto.setNombre(nombre);
			/*if (request.getParameter("orden_relacion_" + i) != null
					&& !request.getParameter("orden_relacion_" + i).equals(""))
				atributoDto.setOrdenVisualizacion(Integer.valueOf(request.getParameter("orden_relacion_" + i)));*/
			atributoDto.setOrdenVisualizacion(-1);
			atributoDto.setRelacion(respuestaRelacion.getObjetoEncapsulado());

			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("atributos");
			url.setParametroCadena("atributo");

			EncapsuladorPOSTSW<AtributoDto> respuestaAtributo = getRestTemplate().postForEntity(url.getUrl(),
					new HttpEntity<AtributoDto>(atributoDto), EncapsuladorPOSTSW.class).getBody();

			// Introduzco las tabla relacionada que utiliza la relacion
			// Veo si la tabla relacionada existe en BD, sino la introduzco
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("tablas");
			url.setParametroCadena("tabla");
			url.setParametroCadena(indicadorDto2.getId());
			/*url.setParametroCadena(request.getParameter("tabla_relacionada_" + i));
			url.setParametroCadena(request.getParameter("idfuente_relacionada_" + i));*/
			url.setParametroCadena(objRelacionVista.getTablaRelacionada());
			url.setParametroCadena(objRelacionVista.getIdFuenteRelacionada());
			
			TablaFuenteDatosDto tablaDto = getRestTemplate().getForEntity(url.getUrl(), TablaFuenteDatosDto.class)
					.getBody();
			if (tablaDto == null || tablaDto.getId() <= 0) {
				url = new UrlConstructorSWImpl(getUrlBaseSw());
				url.setParametroCadena("tablas");
				url.setParametroCadena("tabla");

				TablaFuenteDatosDto tablaFuenteDatosDto = new TablaFuenteDatosDto();
				tablaFuenteDatosDto.setFuente(cargarFuente(Long.valueOf(objRelacionVista.getIdFuenteRelacionada())));
				tablaFuenteDatosDto.setIndicador(indicadorDto2);
				tablaFuenteDatosDto.setNombre(objRelacionVista.getTablaRelacionada());

				tablaDto = (TablaFuenteDatosDto) getRestTemplate()
						.postForEntity(url.getUrl(), new HttpEntity<TablaFuenteDatosDto>(tablaFuenteDatosDto),
								EncapsuladorPOSTSW.class).getBody().getObjetoEncapsulado();
			}

			// Introduzco las tabla relacion que utiliza la relacion
			// Veo si la tabla relacion existe en BD, sino la introduzco
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("tablas");
			url.setParametroCadena("tabla");
			url.setParametroCadena(indicadorDto2.getId());
			/*url.setParametroCadena(request.getParameter("tabla_relacion_" + i));
			url.setParametroCadena(request.getParameter("idfuente_relacion_" + i));*/
			url.setParametroCadena(objRelacionVista.getTablaRelacion());
			url.setParametroCadena(objRelacionVista.getIdFuenteRelacion());
			
			

			tablaDto = getRestTemplate().getForEntity(url.getUrl(), TablaFuenteDatosDto.class).getBody();
			if (tablaDto == null || tablaDto.getId() <= 0) {
				url = new UrlConstructorSWImpl(getUrlBaseSw());
				url.setParametroCadena("tablas");
				url.setParametroCadena("tabla");

				TablaFuenteDatosDto tablaFuenteDatosDto = new TablaFuenteDatosDto();
				tablaFuenteDatosDto
						.setFuente(cargarFuente(Long.valueOf(objRelacionVista.getIdFuenteRelacion())));
				tablaFuenteDatosDto.setIndicador(indicadorDto2);
				tablaFuenteDatosDto.setNombre(objRelacionVista.getTablaRelacion());

				tablaDto = (TablaFuenteDatosDto) getRestTemplate()
						.postForEntity(url.getUrl(), new HttpEntity<TablaFuenteDatosDto>(tablaFuenteDatosDto),
								EncapsuladorPOSTSW.class).getBody().getObjetoEncapsulado();
			}
		}

		model.addAttribute("indicadorDto", indicadorDto2);

		// Estilo de visualizacion
		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaEstiloVisualizacionTabla");
		url.setParametroCadena(indicadorDto2.getId());
		url.setParametroCadena(getUsuarioId());

		EstiloVisualizacionTablaDto estilo = getRestTemplate().getForEntity(url.getUrl(),
				EstiloVisualizacionTablaDto.class).getBody();

		if (estilo != null && estilo.getId() > 0)
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);

		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto2.getId());
		EncapsuladorListSW<AtributoDto> listaColumnasMostrar = new EncapsuladorListSW<AtributoDto>();

		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("atributos");
		url.setParametroCadena("datosIndicador");
		url.setParametroCadena(indicadorDto2.getId());
		url.setParametroCadena("no");

		AtributosMapDto datos = getRestTemplate().postForEntity(url.getUrl(),
				new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas), AtributosMapDto.class).getBody();

		
		//Se obtiene la columna MAPA para eliminarla (se incorporó para la visualización del mapa
		AtributoFuenteDatosDto columnaAtributoMapa = null;			
		for (AtributoDto attr : listaColumnas) {
			// Campo mapa
			if (attr.getEsMapa()) { 
				columnaAtributoMapa = attr.getColumna();	
				break;
			}
		}
		if (columnaAtributoMapa!=null) {
			datos.getContenido().remove(columnaAtributoMapa.getNombre());
		}
		int numFilas = obtenerNumeroFilas(datos);

		for (AtributoDto attr : listaColumnas) {
			if (attr.getMostrar())
				listaColumnasMostrar.add(attr);
		}

		model.addAttribute("numFilas", numFilas);
		model.addAttribute("datos", datos.getContenido().entrySet());
		model.addAttribute("listaColumnas", listaColumnasMostrar);

		model = rellenarDatosFormularioEstilo(model);

		mostrarHistorico(indicadorDto2.getId(), model);
		comprobarUsuario(model, indicadorDto2);

		return "visualizarIndicadorTablaTile";
	}

	/*
	 * Método que elimina una categoria. Redirecciona la salida a indicadores.html
	 * Es necesario gestionar los errores a través de variables para mostrar los errores en cargaTablaElementosJerarquia
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=borraCategoria" })
	public String borraCategoria(@RequestParam(value = "idCategoria") Long idCategoria, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (usuarioInvitado())
			return irSinPermisos(request, response);
		
		bolSiAccionEliminarCategoria = true;
		
		// Si el usuario no es administrador sólo puede eliminar la CATEGORIA si es su creador o TIENE PERMISO SOBRE ELLA
		if (!getUserAdmin()) {
			UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionCategoria");
			url.setParametroCadena(idCategoria);
			url.setParametroCadena(getUsuarioId());
			
			if (!(getRestTemplate().getForEntity(url.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico())) {
				bolErrorBorrarPermisosCategoria = true;
				return "redirect:/indicadores.htm";
			} else {
				bolErrorBorrarPermisosCategoria = false;
			}
		}
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("categorias").setParametroCadena("borraCategoria").setParametroCadena(idCategoria);
		EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class)
				.getBody();

		if (resultado.getValorLogico()) {
			bolExitoEliminarCategoria = true;
		} else {
			bolExitoEliminarCategoria = false;
		}
		
		if (model.containsAttribute("idioma")) {
			model.addAttribute("idioma", null);
		}
		if (model.containsAttribute("usuarioAdmin")) {
			model.addAttribute("usuarioAdmin", null);
		}
		
		return "redirect:/indicadores.htm";
	}
	
	/*
	 * Método que elimina un indicador. Redirecciona la salida a indicadores.html
	 * Es necesario gestionar los errores a través de variables para mostrar los errores en cargaTablaElementosJerarquia
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=borraIndicador" })
	public String borraIndicador(@RequestParam(value = "idIndicador") Long idIndicador, 
			Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (usuarioInvitado()) {			
				irSinPermisos(request, response);
		}
		
		bolSiAccionEliminarIndicador = true;
		
		// Si el usuario no es administrador sólo puede eliminar el indicador si es su creador
		if (!getUserAdmin()) {
			IndicadorDto indicadorDto = getRestTemplate().getForEntity(new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador).getUrl(), IndicadorDto.class).getBody();
			if (indicadorDto != null && !indicadorDto.getLoginCreador().equals(getServicioSeguridad().getUserDetails().getUsername())) {
				bolErrorBorrarPermisosIndicador = true;
				return "redirect:/indicadores.htm";
			} else {
				bolErrorBorrarPermisosIndicador = false;
			}
		}
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("borraIndicador").setParametroCadena(idIndicador);
		EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class).getBody();

		if (resultado.getValorLogico()) {
			bolExitoEliminarIndicador = true;
		} else {
			bolExitoEliminarIndicador = false;
		}
		
		if (model.containsAttribute("idioma")) {
			model.addAttribute("idioma", null);
		}
		if (model.containsAttribute("usuarioAdmin")) {
			model.addAttribute("usuarioAdmin", null);
		}
		
		return "redirect:/indicadores.htm";
	}
	
	@SuppressWarnings("rawtypes")
	private List<CategoriaDto> formateaCategoriasParaSelectPermisos(Map<Long, List<CategoriaDto>> mapaCategorias)
	{
		List<CategoriaDto> listaCategorias = new ArrayList<CategoriaDto>();
		
		String cambioNivel = "--- ";
		String nivelActual = cambioNivel;
		
		// Si hai Categorías
		if (mapaCategorias != null && mapaCategorias.size() > 0)
		{
			// Se realiza la chamada al método recursivo que formatea las categorías pasando, como parámetro, el id de la categoría Raíz, 0
			listaCategorias = formateaCategoriasParaSelectPermisosHijo(mapaCategorias, 0L, cambioNivel, nivelActual);
		}
		
		return listaCategorias;
	}
	
	@SuppressWarnings("rawtypes")
	private List<CategoriaDto> formateaCategoriasParaSelectPermisosHijo(Map<Long, List<CategoriaDto>> mapaCategorias, Long idCategoriaPadre, String cambioNivel, String nivelActual)
	{
		List<CategoriaDto> listaCategoriasHijoFormateadas = new ArrayList<CategoriaDto>();
		
		// Si todos los parámetros son, a priori, correctos
		if (mapaCategorias != null && mapaCategorias.size() > 0 && idCategoriaPadre != null && idCategoriaPadre >= 0L)
		{
			Iterator<Entry<Long, List<CategoriaDto>>> it = mapaCategorias.entrySet().iterator();
			
			// Se recorre el mapa buscando el índice cuyo valor coincida con el id de la categoría padre
			while (it.hasNext()) 
			{
				Map.Entry elementoActual = (Map.Entry)it.next();
				
				// Si las categorías son Hijo de la Categoría Padre buscada
				if (elementoActual != null && elementoActual.getKey() != null && ((Long)elementoActual.getKey()).longValue() == idCategoriaPadre)
				{
					List<CategoriaDto> listaActual = (List<CategoriaDto>)elementoActual.getValue();
					
					if (listaActual != null && listaActual.size() > 0)
					{
						for(CategoriaDto categoriaActual : listaActual)
						{
							if (categoriaActual != null && categoriaActual.getId() > 0L)
							{
								// Se formatea el nombre de la categoría para su visualización
								categoriaActual.setNombre(nivelActual + categoriaActual.getNombre());
								// Se añade a la lista
								listaCategoriasHijoFormateadas.add(categoriaActual);
								
								// Se obtiene, formateada, la lista de sus categorías Hijo
								List<CategoriaDto> listaCategoriasHijo = formateaCategoriasParaSelectPermisosHijo(mapaCategorias, categoriaActual.getId(), cambioNivel, nivelActual + cambioNivel);
								
								if (listaCategoriasHijo != null && listaCategoriasHijo.size() > 0)
								{
									listaCategoriasHijoFormateadas.addAll(listaCategoriasHijo);
								}
							}
						}
					}
				}
			}
		}
		
		return listaCategoriasHijoFormateadas;
	}

	@SuppressWarnings("unchecked")
	private CategoriaDto guardaCategoria(CategoriaDto categoriaDto, BindingResult result) {
		EncapsuladorPOSTSW<CategoriaDto> respuestaPost = getRestTemplate().postForEntity(
				new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("guarda")
						.setParametroCadena(getUsuarioId()).getUrl(), new HttpEntity<CategoriaDto>(categoriaDto),
				EncapsuladorPOSTSW.class).getBody();

		if (respuestaPost.hashErrors())
			escribirErrores(result, respuestaPost);

		return respuestaPost.getObjetoEncapsulado();
	}

	@SuppressWarnings({ "rawtypes" })
	private IndicadorDto guardaIndicador(IndicadorDto indicadorDto, BindingResult result) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("guardaIndicador");
		url.setParametroCadena(getUsuarioId());

		if (indicadorDto.getPublico()) {
			indicadorDto.setPteAprobacionPublico(true);
		} else {
			indicadorDto.setPteAprobacionPublico(false);
		}

		HttpEntity<IndicadorDto> entity = new HttpEntity<IndicadorDto>(indicadorDto, getHeaders());
		ResponseEntity<EncapsuladorPOSTSW> respuestaPost = getRestTemplate().postForEntity(url.getUrl(), entity,
				EncapsuladorPOSTSW.class);

		if (respuestaPost.getBody().hashErrors())
			escribirErrores(result, respuestaPost.getBody());

		IndicadorDto indicadorRespuesta = (IndicadorDto) respuestaPost.getBody().getObjetoEncapsulado();

		return indicadorRespuesta;
	}

	@ModelAttribute("usuarioAdmin")
	public Boolean getUserAdmin() {
		return getServicioSeguridad().getUserDetails().getEsAdmin();
	}

	private Boolean usuarioInvitado() {
		return getServicioSeguridad().getUserDetails().getInvitado();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ModelAttribute(value = "listaFuentes")
	public List<FuenteDto> getListaFuentes() {
		List<FuenteDto> items = new ArrayList<FuenteDto>();
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("listaFuentes");

		ResponseEntity<EncapsuladorListSW> lista = getRestTemplate().getForEntity(url.getUrl(),
				EncapsuladorListSW.class);
		EncapsuladorListSW<FuenteDto> listaFuentes = lista.getBody();
		for (FuenteDto fuente : listaFuentes)
			items.add(fuente);
		return items;
	}
	
	public boolean esUsuarioConPermisos(Long idIndicador){
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("permisosEdicionUsuarioIndicador").setParametroCadena(idIndicador).setParametroCadena(getUsuarioId());
		EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class).getBody();

		if (resultado.getValorLogico()) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarIndicadorTabla" })
	public String visualizarIndicadorTabla(@RequestParam(value = "id") Long idIndicador, Model model,
			HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {
		
		if(!esUsuarioConPermisos(idIndicador)){
			throw new HttpMessageNotReadableException(error);
		}

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", indicadorDto);

		EstiloVisualizacionTablaDto estilo = Estilos.cargarEstiloVisualizacionTabla(getUsuarioId(), getUrlBaseSw(),
				getRestTemplate(), idIndicador);

		if (estilo != null && estilo.getId() > 0)
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);

		// Preparamos los datos para su visualizacion
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());
		EncapsuladorListSW<AtributoDto> listaColumnasMostrar = new EncapsuladorListSW<AtributoDto>();

		Object parametro;
		if (estilo.getTipoFecha() == null)
			parametro = "no";
		else
			parametro = estilo.getTipoFecha().getDescripcion();

		//----------------------------------CAMBIO EN LA GESTIÓN DE LAS COLUMNAS HISTÓRICO -----------------------------------------------
		
		/* Si el resultado a mostrar en la tabla de Indicadores es un Histórico, los datos ni las columnas se pueden obtener de la 
		 * misma forma que en la vista de la versión actual. El histórico se obtiene: writeObject(decompress(historico.getDatosDto()))
		 * Por tanto las columnas deberemos obtenerlas de este dato y no de las columnas
		 */
		
		AtributosMapDto datos = new AtributosMapDto();
		AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
		if (historico != null && historico.getDatosDto() != null) {
			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));
			model.addAttribute("historico", historico);
		} else {
			datos = obtenerDatosIndicador(indicadorDto.getId(), "datosIndicador", parametro, listaColumnas);
		}


		if (historico != null && historico.getDatosDto() != null) {
			
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
					if (objAtributoMostrar!=null && objAtributoMostrar.getMostrar() && (!objAtributoMostrar.getEsMapa())) {
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
			 * y no visualizando la columna GEOM.
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
				
		int numFilas = obtenerNumeroFilas(datos);
		model.addAttribute("datos", datos.getContenido().entrySet());
		model.addAttribute("numFilas", numFilas);
		model.addAttribute("listaColumnas", listaColumnasMostrar);
		
		// Mostrar histórico
		mostrarHistorico(idIndicador, model);
		comprobarUsuario(model, indicadorDto);
		
		model = rellenarDatosFormularioEstilo(model);
		
		if (!usuarioInvitado()&& !getUserAdmin()) {
			UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionIndicador");
			url2.setParametroCadena(idIndicador);
			url2.setParametroCadena(getUsuarioId());
			
			if (!(getRestTemplate().getForEntity(url2.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico())) {
				model.addAttribute("tienePermisos" , false);
			} else {
				model.addAttribute("tienePermisos" , true);
			}
		}

		// Error fichero exportado
		if (error != null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}
		
		if (model.containsAttribute("accion")) {
			model.addAttribute("accion", null);
		}
		if (model.containsAttribute("id")) {
			model.addAttribute("accion", null);
		}
		
		if (bolSiAccionEliminarVersion) {
			
			if (bolEntidadUtilizada) {
				model.addAttribute("resultadoOperacion", ResultadosOperaciones.ENTIDAD_UTILIZADA.getResultado());
				bolSiAccionEliminarVersion = false;
				return "visualizarIndicadorTablaTile";
			}
			
			if (bolExitoEliminarVersion) {
				model.addAttribute(ResultadosOperaciones.EXITO_BORRAR.toString(), true);
			} else {
				model.addAttribute(ResultadosOperaciones.ERROR_BORRAR + "Version", true);
			}
			bolSiAccionEliminarVersion = false;
			return "visualizarIndicadorTablaTile";
		}
		
		return "visualizarIndicadorTablaTile";
	}
		
	protected String getUrlBaseSw() {
		return getServicioConfiguracionGeneral().getUrlBaseSW();
	}

	protected long getUsuarioId() {
		return getServicioSeguridad().getUserDetails().getId();
	}

	protected void comprobarUsuario(Model model, IndicadorDto indicadorDto) {
		if (!usuarioInvitado()) {
			UsuarioDto usuarioLogueado = cargaUsuarioPorIdSW(getUsuarioId());
			if (getUserAdmin()) {
				model.addAttribute("propietario", true);
			} else if (indicadorDto.getLoginCreador().equals(usuarioLogueado.getLogin())) {
				model.addAttribute("propietario", true);
			} else {
				model.addAttribute("propietario", false);
			}
		} else {
			model.addAttribute("propietario", false);
		}
	}
	
	/*
	 * Método que a partir de un indicador comprueba si el usuario logeado es propietario de
	 * un indicador
	 */
	private boolean comprobarPropiedadIndicador(IndicadorDto indicadorDto) {
		
		boolean bolEsPropietario = false;
		if (!usuarioInvitado()) {
			UsuarioDto usuarioLogueado = cargaUsuarioPorIdSW(getUsuarioId());
			if (getUserAdmin()) {
				bolEsPropietario=true;
			} else if (indicadorDto.getLoginCreador().equals(usuarioLogueado.getLogin())) {
				bolEsPropietario=true;
			} else {
				bolEsPropietario=false;
			}
		} else {
			bolEsPropietario=false;
		}
		return bolEsPropietario;
	}

	/*
	 * Método para visualizar el diagrama Mapa Temático
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarIndicadorMapa" })
	public String visualizarIndicadorMapa(@RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "param", required = false) String parametro,
			@RequestParam(value = "idHistorico", required = false) String idHistorico, Model model,
			HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {
		
		if(!esUsuarioConPermisos(idIndicador)){
			throw new HttpMessageNotReadableException(error);
		}
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", indicadorDto);

		EstiloVisualizacionMapaDto estilo = Estilos.cargarEstiloMapa(getUsuarioId(), getUrlBaseSw(), getRestTemplate(),
				idIndicador);

		// Rangos de Estilo de visualizacion de Mapa
		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaRangosVisualizacionMapa");
		url.setParametroCadena(estilo.getId());

		EncapsuladorListSW<RangosVisualizacionMapaDto> listaRangos = getRestTemplate().getForEntity(url.getUrl(),
				EncapsuladorListSW.class).getBody();

		if (estilo != null && estilo.getId() > 0)
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);

		model.addAttribute("color_defecto", COLOR_DEFECTO);

		List<RangosVisualizacionMapaDto> rangos = listaRangos.getLista();

		model.addAttribute("rangos", rangos);
		model.addAttribute("numRangos", rangos.size());

		// Preparamos los datos para su visualizacion
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());

		// Comprobamos si el indicador tiene un campo mapa y un campo numerico
		// al menos para poder visualizar el mapa
		boolean tieneMapa = false;
		boolean tieneNumerico = false;
		AtributoFuenteDatosDto columnaAtributoMapa = null;
		
		for (AtributoDto attr : listaColumnas) {
			
			//Campo mapa
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
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error);
		}

		if (!tieneNumerico) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error);
		}

		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("atributos");
		url.setParametroCadena("datosIndicadorParametro");
		url.setParametroCadena(indicadorDto.getId());
		if (parametro == null || parametro.equals(""))
			parametro = NO_PARAMETRO;
		url.setParametroCadena(parametro);

		AtributosMapDto datos = new AtributosMapDto();
		
		AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
		if (idHistorico != null && !idHistorico.equals("0")) {
			
			if (historico.getDatosDto() == null) {
				url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("historico")
						.setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			}
			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));
			AtributosMapDto datosFromHistorico = convertirEnAtributosMapDto (datosHistoricosTemporal);//(AtributosMapDto) writeObject(decompress(historico.getDatosDto()));
			
			//Si trabajamos sobre una versión listaColumnas tenemos que obtenerla directamente del Histórico Temporal
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
			
			
			String ambito = "";
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

		//url = new UrlConstructorSWImpl(getUrlBaseSw());
		//url.setParametroCadena("atributos");
		//url.setParametroCadena("mapaIndicador");
		//url.setParametroCadena(indicadorDto.getId());

		//Se obtiene la columna mapa
		LinkedHashMap<String,ValorFDDto> columnaMapa = new LinkedHashMap<String,ValorFDDto>();
		AtributosMapDto datosMapa = new AtributosMapDto();
		if (historico != null && historico.getMapaDto() != null) {
			datosMapa = (AtributosMapDto) writeObject(decompress(historico.getMapaDto()));
		} else {
			//datosMapa = getRestTemplate().postForEntity(url.getUrl(),
			//		new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas), AtributosMapDto.class).getBody();
			
			columnaMapa.put(columnaAtributoMapa.getNombre(), datos.getContenido().get(columnaAtributoMapa.getNombre()));
			datosMapa.setContenido(columnaMapa);
		}
		
		datos.getContenido().remove(columnaAtributoMapa.getNombre());
		int numFilas = obtenerNumeroFilas(datos);

		//Se obtienen las columnas que se deben mostrar
		List<AtributoDto> listaColumnasNoMapa = new ArrayList<AtributoDto>();

		//Si estamos visualizando el Mapa Temático de un Histórico
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

		if (listaColumnasNoMapa.size() <= 0) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error);
		}
		
		model.addAttribute("listaColumnas", listaColumnasNoMapa);

		model.addAttribute("parametro", parametro);
		model.addAttribute("numFilas", numFilas);
		model.addAttribute("datos", datos.getContenido().entrySet());
		model.addAttribute("mapa", datosMapa.getContenido().entrySet());
		
		// Mostrar histórico
		mostrarHistorico(idIndicador, model);
		comprobarUsuario(model, indicadorDto);
		
		if (!usuarioInvitado()&& !getUserAdmin()) {
			UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionIndicador");
			url2.setParametroCadena(idIndicador);
			url2.setParametroCadena(getUsuarioId());
			
			if (!(getRestTemplate().getForEntity(url2.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico())) {
				model.addAttribute("tienePermisos" , false);
			} else {
				model.addAttribute("tienePermisos" , true);
			}
		}

		// Error fichero exportado
		if (error != null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}

		return "visualizarIndicadorMapaTile";
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarIndicadorDiagramaBarras" })
	public String visualizarIndicadorDiagramaBarras(@RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "param", required = false) String parametro,
			@RequestParam(value = "idHistorico", required = false) String idHistorico, Model model,
			HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {
		
		if(!esUsuarioConPermisos(idIndicador)){
			throw new HttpMessageNotReadableException(error);
		}
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", indicadorDto);

		EstiloVisualizacionDiagramaBarrasDto estilo = Estilos.cargarEstiloDiagramaBarras(getUsuarioId(),
				getUrlBaseSw(), getRestTemplate(), idIndicador);

		if (estilo != null && estilo.getId() > 0)
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);

		//Sobre lista columnas se trabaja sino trabajamos sobre el INDICADOR ACTUAL
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());
		
		//Se obtiene la columna MAPA para eliminarla
		AtributoFuenteDatosDto columnaAtributoMapa = null;			
		for (AtributoDto attr : listaColumnas) {
			// Campo mapa
			if (attr.getEsMapa()) { 
				columnaAtributoMapa = attr.getColumna();	
				break;
			}
		}
		
		model.addAttribute("parametro", parametro);
		model.addAttribute("idindicador", idIndicador);
		model.addAttribute("elementosPagina", getServicioConfiguracionGeneral().getTamanhoPagina());

		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("atributos");
		url.setParametroCadena("datosIndicadorParametro");
		url.setParametroCadena(indicadorDto.getId());
		if (parametro == null || parametro.equals(""))
			parametro = NO_PARAMETRO;
		url.setParametroCadena(parametro);
		
		AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
		AtributosMapDto datos = new AtributosMapDto();
		if (idHistorico != null && !idHistorico.equals("0")) {
			
			if (historico.getDatosDto() == null) {
				url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("historico")
						.setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			}
			
			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));
			AtributosMapDto datosFromHistorico = convertirEnAtributosMapDto (datosHistoricosTemporal);
						
			//Si trabajamos sobre una versión listaColumnas tenemos que obtenerla directamente del Histórico Temporal
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

		if (columnaAtributoMapa!=null) {
			datos.getContenido().remove(columnaAtributoMapa.getNombre());
		}
		
		/*
		 * Trabajo con columnas
		 */
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

		if (listaColumnasNoMapa.size() <= 0) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error);
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
		
		List<ParDiagramaBarrasDto>listaOrdenada=new ArrayList<ParDiagramaBarrasDto>();
		int tamanhoListaMenor=0;
		if(listaValoresAmbito.size()>listaValores.size()){
			tamanhoListaMenor=listaValores.size();
		}else{
			tamanhoListaMenor=listaValoresAmbito.size();
		}
		for(int i=0; i<tamanhoListaMenor;i++){
			if(listaValoresAmbito.get(i).getTexto()!=null && listaValores.get(i).getTexto()!=null){
				listaOrdenada.add(new ParDiagramaBarrasDto(listaValoresAmbito.get(i).getTexto(),listaValores.get(i).getTexto()));
			}
		}
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
		
		int numFilas = obtenerNumeroFilas(datos);

		model.addAttribute("numFilas", numFilas);
		model.addAttribute("numPaginasDiagramas",
				Math.ceil(numFilas / getServicioConfiguracionGeneral().getTamanhoPagina()));

		model.addAttribute("datos", datosRepresentar);

		model = rellenarDatosFormularioEstiloDiagramaBarras(model);
		// Mostrar histórico
		mostrarHistorico(idIndicador, model);
		comprobarUsuario(model, indicadorDto);
		
		if (!usuarioInvitado()&& !getUserAdmin()) {
			UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionIndicador");
			url2.setParametroCadena(idIndicador);
			url2.setParametroCadena(getUsuarioId());
			
			if (!(getRestTemplate().getForEntity(url2.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico())){
				model.addAttribute("tienePermisos" , false);
			} else {
				model.addAttribute("tienePermisos" , true);
			}
		}

		// Error fichero exportado
		if (error != null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}

		return "visualizarIndicadorDiagramaBarrasTile";
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarIndicadorDiagramaSectores" })
	public String visualizarIndicadorDiagramaSectores(@RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "param", required = false) String parametro,
			@RequestParam(value = "idHistorico", required = false) String idHistorico, Model model,
			HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {
		
		if(!esUsuarioConPermisos(idIndicador)){
			throw new HttpMessageNotReadableException(error);
		}
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", indicadorDto);

		EstiloVisualizacionDiagramaSectoresDto estilo = Estilos.cargarEstiloSectores(getUsuarioId(), getUrlBaseSw(),
				getRestTemplate(), idIndicador);

		if (estilo != null && estilo.getId() > 0)
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);

		// Preparamos los datos para su visualizacion
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());

		//Se obtiene la columna MAPA para eliminarla (se incorporó para la visualización del mapa
		AtributoFuenteDatosDto columnaAtributoMapa = null;			
		for (AtributoDto attr : listaColumnas) {
			// Campo mapa
			if (attr.getEsMapa()) { 
				columnaAtributoMapa = attr.getColumna();	
				break;
			}
		}
		
		model.addAttribute("parametro", parametro);
		model.addAttribute("idindicador", idIndicador);
		model.addAttribute("elementosPagina", getServicioConfiguracionGeneral().getTamanhoPagina());

		if (parametro == null || parametro.equals(""))
			parametro = NO_PARAMETRO;

		AtributosMapDto datos = new AtributosMapDto();
		AtributoMapDto datosHistoricosTemporal = new AtributoMapDto();
		if (idHistorico != null && !idHistorico.equals("0")) {
			if (historico.getDatosDto() == null) {
				url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("historico")
						.setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			}
			
			datosHistoricosTemporal = (AtributoMapDto) writeObjectOtro(decompress(historico.getDatosDto()));
			AtributosMapDto datosFromHistorico = convertirEnAtributosMapDto (datosHistoricosTemporal);//(AtributosMapDto) writeObject(decompress(historico.getDatosDto()));
			
			//Si trabajamos sobre una versión listaColumnas tenemos que obtenerla directamente del Histórico Temporal
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
			datos = obtenerDatosIndicador(indicadorDto.getId(), "datosIndicadorParametro", parametro, listaColumnas);
		}
		
		if (columnaAtributoMapa!=null) {
			datos.getContenido().remove(columnaAtributoMapa.getNombre());
		}
		
		/*
		 * Trabajo con columnas
		 */
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

		if (listaColumnasNoMapa.size() <= 0) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error);
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
		
		List<ParDiagramaBarrasDto>listaOrdenada=new ArrayList<ParDiagramaBarrasDto>();
		int tamanhoListaMenor=0;
		if(listaValoresAmbito.size()>listaValores.size()){
			tamanhoListaMenor=listaValores.size();
		}else{
			tamanhoListaMenor=listaValoresAmbito.size();
		}
		for(int i=0; i<tamanhoListaMenor;i++){
			if(listaValoresAmbito.get(i).getTexto()!=null && listaValores.get(i).getTexto()!=null){
				listaOrdenada.add(new ParDiagramaBarrasDto(listaValoresAmbito.get(i).getTexto(),listaValores.get(i).getTexto()));
			}
		}
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
		
		int numFilas = obtenerNumeroFilas(datos);
		model.addAttribute("numFilas", numFilas);
		model.addAttribute("numPaginasDiagramas",
				Math.ceil(numFilas / getServicioConfiguracionGeneral().getTamanhoPagina()));

		model.addAttribute("datos", datosRepresentar);

		model = rellenarDatosFormularioEstiloDiagramaSectores(model);
		// Mostrar histórico
		mostrarHistorico(idIndicador, model);
		comprobarUsuario(model, indicadorDto);
		
		if (!usuarioInvitado()&& !getUserAdmin()) {
			UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaTienePermisoUsuarioEdicionIndicador");
			url2.setParametroCadena(idIndicador);
			url2.setParametroCadena(getUsuarioId());
			
			if (!(getRestTemplate().getForEntity(url2.getUrl(),EncapsuladorBooleanSW.class).getBody().getValorLogico())) {
				model.addAttribute("tienePermisos" , false);
			} else {
				model.addAttribute("tienePermisos" , true);
			}
		}

		// Error fichero exportado
		if (error != null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}

		return "visualizarIndicadorDiagramaSectoresTile";
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=verDetallesIndicador" })
	public String verDetallesIndicador(@RequestParam(value = "id") Long idIndicador, Model model,
			HttpServletRequest request) {
		
		if(!esUsuarioConPermisos(idIndicador)){
			throw new HttpMessageNotReadableException("error");
		}
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto resultado = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", resultado);

		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("metadatos").setParametroCadena("metadato").setParametroCadena(idIndicador);
		MetadatosDto resultado2 = getRestTemplate().getForEntity(url.getUrl(), MetadatosDto.class).getBody();

		// Si no tiene metadatos, no tiene fichero
		if (resultado2.getId() > 0 && resultado2.getMetadatos() != null && !resultado2.getMetadatos().equals("")) {
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("ficheros");
			url.setParametroCadena("descargarMetadatos");
			url.setParametroCadena(idIndicador);

			EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class)
					.getBody();
			String ficheroMetadatosFormateado = "";
			try {
				ficheroMetadatosFormateado = LeerXML.mostrarFormateado(fichero.getFich());
			} catch (Exception ex) {
				ficheroMetadatosFormateado = new String(fichero.getFich());
			}
			model.addAttribute("metadatos", ficheroMetadatosFormateado);
			model.addAttribute("ficheroMetadatos", fichero.getNombre());
		} else {
			model.addAttribute("metadatos", "");
			model.addAttribute("ficheroMetadatos", "");
		}

		return "verDetallesIndicadorTile";
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=modificarDetallesIndicador" })
	public String modificarDetallesIndicador(@RequestParam(value = "id") Long idIndicador, Model model,
			HttpServletRequest request) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto resultado = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", resultado);

		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("metadatos").setParametroCadena("metadato").setParametroCadena(idIndicador);
		MetadatosDto resultado2 = getRestTemplate().getForEntity(url.getUrl(), MetadatosDto.class).getBody();

		if (resultado2.getId() > 0)
			model.addAttribute("ficheroMetadatos", resultado2.getMetadatos());
		else
			model.addAttribute("ficheroMetadatos", "");

		return "modificarDetallesIndicadorTile";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST, params = { "accion=modificarDetallesIndicador" })
	public String guardarDetallesIndicador(Model model, @ModelAttribute IndicadorDto indicadorDto,
			BindingResult result, @RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "fich_metadatos", required = false) MultipartFile fich, HttpServletRequest request) {
		
		//Se obtiene el Indicador, que existe en la actualidad en la BBDD (para no perder el NOMBRE DEL MISMO en caso de error en el formulario
		UrlConstructorSW urlIndicadorOriginal = new UrlConstructorSWImpl(getUrlBaseSw());
		urlIndicadorOriginal.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorOriginal = getRestTemplate().getForEntity(urlIndicadorOriginal.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorOriginalDto", indicadorOriginal);
		
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("guardaIndicador").setParametroCadena(getUsuarioId());

		HttpEntity<IndicadorDto> entity = new HttpEntity<IndicadorDto>(indicadorDto, getHeaders());
		ResponseEntity<EncapsuladorPOSTSW> respuesta = getRestTemplate().postForEntity(url.getUrl(), entity,
				EncapsuladorPOSTSW.class);
		String nombreFicheroMetadatos = "";
		// Se evaluan errores en el servicio web
		if (hasErrorsSW(respuesta))
			return irPaginaErrorSW();

		// Se evaluan errores en el formulario
		if (respuesta.getBody().hashErrors()) {
			escribirErrores(result, respuesta.getBody());
			model.addAttribute("mostrarNombreOriginal",true);
			return "modificarDetallesIndicadorTile";
		}

		model.addAttribute("mostrarNombreOriginal",false);
		IndicadorDto indicadorDto2 = (IndicadorDto) respuesta.getBody().getObjetoEncapsulado();
		model.addAttribute("indicadorDto", indicadorDto2);

		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;
		ResponseEntity<EncapsuladorPOSTSW> respuestaBorrarFichero = null;
		// Enviamos el fichero al servidor SOLO si hay fichero
		if (fich != null
				&& fich.getSize() > 0
				&& (request.getParameter("sin_metadatos") == null || !request.getParameter("sin_metadatos")
						.equals("on"))) {
			respuestaFichero = guardarFicheroMetadatos(fich, indicadorDto2);
			nombreFicheroMetadatos = fich.getOriginalFilename();
		}

		if (respuestaFichero != null) {
			// Despues de guardar el fichero
			// Se evaluan errores en el servicio web
			if (hasErrorsSW(respuestaFichero))
				return irPaginaErrorSW();

			// Se evaluan errores en el formulario
			if (respuestaFichero.getBody().hashErrors()) {
				escribirErrores(result, respuestaFichero.getBody());
				model.addAttribute(getPathPaqueteValidacion() + "indicadorDto", result);
				return "modificarDetallesIndicadorTile";
			}
		} else if (fich != null
				&& fich.getSize() > 0
				&& (request.getParameter("sin_metadatos") == null || !request.getParameter("sin_metadatos")
						.equals("on"))) {
			log.error("Error. Error al leer el fichero");
			model.addAttribute("resultado", "fileError");
			model.addAttribute("fileError", "validacion.fuente.fichero.error");
			return "modificarDetallesIndicadorTile";
		}

		// Obtengo el nombre del fichero sólo si no hay fichero nuevo
		if (fich == null || fich.getSize() <= 0) {
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("metadatos").setParametroCadena("metadato").setParametroCadena(idIndicador);
			MetadatosDto resultado2 = getRestTemplate().getForEntity(url.getUrl(), MetadatosDto.class).getBody();
			nombreFicheroMetadatos = resultado2.getMetadatos();
			model.addAttribute("ficheroMetadatos", nombreFicheroMetadatos);
		}

		if (request.getParameter("sin_metadatos") != null && request.getParameter("sin_metadatos").equals("on")) {
			respuestaBorrarFichero = borrarFicheroMetadatos(indicadorDto2, result);
			nombreFicheroMetadatos = "";
			if (hasErrorsSW(respuestaBorrarFichero))
				return irPaginaErrorSW();

			// Se evaluan errores en el formulario
			if (respuestaBorrarFichero.getBody().hashErrors()) {
				escribirErrores(result, respuestaBorrarFichero.getBody());
				model.addAttribute(getPathPaqueteValidacion() + "indicadorDto", result);
				return "modificarDetallesIndicadorTile";
			}
		}

		model.addAttribute("ficheroMetadatos", nombreFicheroMetadatos);

		// Guardar la entrada en metadatos
		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("metadatos").setParametroCadena("metadato");

		MetadatosDto metas = new MetadatosDto();
		metas.setMetadatos(nombreFicheroMetadatos);
		metas.setIndicador(indicadorDto2);
		HttpEntity<MetadatosDto> entityMetas = new HttpEntity<MetadatosDto>(metas, getHeaders());
		ResponseEntity<EncapsuladorPOSTSW> respuestaMetas = getRestTemplate().postForEntity(url.getUrl(), entityMetas,
				EncapsuladorPOSTSW.class);

		// Se evaluan errores en el servicio web
		if (hasErrorsSW(respuestaMetas))
			return irPaginaErrorSW();

		// Se evaluan errores en el formulario
		if (respuestaMetas.getBody().hashErrors()) {
			escribirErrores(result, respuestaMetas.getBody());
			return "modificarDetallesIndicadorTile";
		}

		model.addAttribute("resultado", "exitoGuardar");
		return "modificarDetallesIndicadorTile";
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=descargarMetadatos" })
	public void descargarMetadatos(Model model, @RequestParam(value = "id") Long idIndicador,
			HttpServletRequest request, HttpServletResponse response) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaPorId");
		url.setParametroCadena(idIndicador);
		IndicadorDto resultado = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", resultado);

		// Llamar al SW para requerir el fichero!
		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("ficheros");
		url.setParametroCadena("descargarMetadatos");
		url.setParametroCadena(idIndicador);

		EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class).getBody();
		model.addAttribute("metadatos", fichero.getNombre());

		ServletContext sc = request.getSession().getServletContext();
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("name", fichero.getNombre());
		mapa.put("type", sc.getMimeType(fichero.getNombre()));
		mapa.put("inputStream", new ByteArrayInputStream(fichero.getFich()));
		try {
			renderMergedOutputModel(mapa, request, response);
		} catch (Exception e) {
			log.debug("Error al leer el fichero de metadatos para descarga");
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, params = { "accion=busquedaDirecta" })
	public String BusquedaDirecta(@RequestParam(value = "criterio") String criterio, HttpServletRequest request, Model model) 
	{
		if (StringUtils.isBlank(criterio))
		{
			return cargaTablaElementosJerarquia(model, request, null);
		}
		else
		{
			UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("indicadores").setParametroCadena("cargaBusquedaDirecta").setParametroCadena(getUsuarioId()).setParametroCadena(criterio);
			EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> result = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorMapSW.class).getBody();
			
			model.addAttribute("mapaIndicadores", writeJson(result.get("Indicadores")));
			model.addAttribute("mapaCategorias", writeJson(result.get("Categorias")));
			model.addAttribute("criterio", criterio);
			
			return "indicadoresBusquedaTile";
		}
	}

	@RequestMapping(method = RequestMethod.POST, params = { "accion=nuevoEstiloVisualizacionTabla" })
	public String nuevoEstiloVisualizacionTabla(@RequestParam(value = "id") Long idIndicador, Model model,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		Double tamanhoFuente = (request.getParameter("tamanho_fuente") != null && !request.getParameter(
				"tamanho_fuente").equals("")) ? Double.valueOf(request.getParameter("tamanho_fuente")) : null;
		String tipoLetra = request.getParameter("tipo_letra");
		Integer numDecimales = (request.getParameter("decimales") != null && !request.getParameter("decimales").equals(
				"")) ? Integer.valueOf(request.getParameter("decimales")) : null;
		String tipoFecha = request.getParameter("tipo_fecha");

		long idUsuario = getUsuarioId();
		System.out.println("id usuario logueado: " + getUsuarioId());

		// Validar nuevos datos de estilo
		if ((tamanhoFuente == null || tamanhoFuente < 0.4) || (tipoLetra == null || tipoLetra.equals(""))
				|| (numDecimales == null || numDecimales < 0) || (tipoFecha == null || tipoFecha.equals(""))) {
			model.addAttribute("errorEstiloVisualizacion", "jsp.visualizacion.error.validacion.estilo.tabla");
			return visualizarIndicadorTabla(idIndicador, model, request, null, null);
		}

		EstiloVisualizacionTablaDto estilo = new EstiloVisualizacionTablaDto();
		estilo.setDecimales(numDecimales);
		estilo.setTamanhoFuente(tamanhoFuente);
		if (tipoFecha.equals(TipoFecha.CORTA.getDescripcion()))
			estilo.setTipoFecha(TipoFecha.CORTA);
		else
			estilo.setTipoFecha(TipoFecha.LARGA);
		estilo.setTipoFuente(tipoLetra);

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("nuevoEstiloVisualizacionTabla");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(idUsuario);

		ResponseEntity<EncapsuladorPOSTSW> respuestaPost = getRestTemplate().postForEntity(url.getUrl(),
				new HttpEntity<EstiloVisualizacionTablaDto>(estilo, getHeaders()), EncapsuladorPOSTSW.class);

		model.addAttribute("exitoCrear", "jsp.visualizacion.exito.crear");
		return visualizarIndicadorTabla(idIndicador, model, request, null, null);
	}

	@RequestMapping(method = RequestMethod.POST, params = { "accion=nuevoEstiloVisualizacionDiagramaBarras" })
	public String nuevoEstiloVisualizacionDiagramaBarras(@RequestParam(value = "id") Long idIndicador, Model model,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		Double tamanhoFuente = (request.getParameter("tamanho_fuente") != null && !request.getParameter(
				"tamanho_fuente").equals("")) ? Double.valueOf(request.getParameter("tamanho_fuente")) : null;
		String tipoLetra = request.getParameter("tipo_letra");
		Integer tamanho = (request.getParameter("tamanho") != null && !request.getParameter("tamanho").equals("")) ? Integer
				.valueOf(request.getParameter("tamanho")) : null;
		String color = (request.getParameter("color") != null && !request.getParameter("color").equals("")) ? request
				.getParameter("color") : "";

		long idUsuario = getUsuarioId();
		System.out.println("id usuario logueado: " + getUsuarioId());

		// Validar nuevos datos de estilo
		if ((tamanhoFuente == null || tamanhoFuente < 0.4) || (tipoLetra == null || tipoLetra.equals(""))
				|| (tamanho == null || tamanho < 0) || (color == null || color.equals(""))) {
			model.addAttribute("errorEstiloVisualizacion", "jsp.visualizacion.error.validacion.estilo.diagrama.barras");
			return visualizarIndicadorDiagramaBarras(idIndicador, "", null, model, request, null, null);
		}

		EstiloVisualizacionDiagramaBarrasDto estilo = new EstiloVisualizacionDiagramaBarrasDto();
		estilo.setTamanho(tamanho);
		estilo.setTamanhoFuente(tamanhoFuente);
		estilo.setTipoFuente(tipoLetra);
		estilo.setColor(color);

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("nuevoEstiloVisualizacionDiagramaBarras");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(idUsuario);

		ResponseEntity<EncapsuladorPOSTSW> respuestaPost = getRestTemplate().postForEntity(url.getUrl(),
				new HttpEntity<EstiloVisualizacionDiagramaBarrasDto>(estilo, getHeaders()), EncapsuladorPOSTSW.class);

		model.addAttribute("exitoCrear", "jsp.visualizacion.exito.crear");
		return visualizarIndicadorDiagramaBarras(idIndicador, null, null, model, request, null, null);
	}

	@RequestMapping(method = RequestMethod.POST, params = { "accion=nuevoEstiloVisualizacionDiagramaSectores" })
	public String nuevoEstiloVisualizacionDiagramaSectores(@RequestParam(value = "id") Long idIndicador, Model model,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		Double tamanhoFuente = (request.getParameter("tamanho_fuente") != null && !request.getParameter(
				"tamanho_fuente").equals("")) ? Double.valueOf(request.getParameter("tamanho_fuente")) : null;
		String tipoLetra = request.getParameter("tipo_letra");
		Integer diametro = (request.getParameter("diametro") != null && !request.getParameter("diametro").equals("")) ? Integer
				.valueOf(request.getParameter("diametro")) : null;
		String colores = (request.getParameter("colores") != null && !request.getParameter("colores").equals("")) ? request
				.getParameter("colores") : "";

		long idUsuario = getUsuarioId();

		// Validar nuevos datos de estilo
		if ((tamanhoFuente == null || tamanhoFuente < 0.4) || (tipoLetra == null || tipoLetra.equals(""))
				|| (diametro == null || diametro < 0) || (colores == null || colores.equals(""))) {
			model.addAttribute("errorEstiloVisualizacion",
					"jsp.visualizacion.error.validacion.estilo.diagrama.sectores");
			return visualizarIndicadorDiagramaSectores(idIndicador, "", null, model, request, null, null);
		}
		
		if(colores.length()>305){
			model.addAttribute("errorEstiloVisualizacion",
					"jsp.visualizacion.error.validacion.estilo.diagrama.maxcolores");
			return visualizarIndicadorDiagramaSectores(idIndicador, "", null, model, request, null, null);
		}

		EstiloVisualizacionDiagramaSectoresDto estilo = new EstiloVisualizacionDiagramaSectoresDto();
		estilo.setDiametro(diametro);
		estilo.setTamanhoFuente(tamanhoFuente);
		estilo.setTipoFuente(tipoLetra);
		estilo.setColores(colores);

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("nuevoEstiloVisualizacionDiagramaSectores");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(idUsuario);

		ResponseEntity<EncapsuladorPOSTSW> respuestaPost = getRestTemplate().postForEntity(url.getUrl(),
				new HttpEntity<EstiloVisualizacionDiagramaSectoresDto>(estilo, getHeaders()), EncapsuladorPOSTSW.class);

		model.addAttribute("exitoCrear", "jsp.visualizacion.exito.crear");
		return visualizarIndicadorDiagramaSectores(idIndicador, null, null, model, request, null, null);
	}

	@RequestMapping(method = RequestMethod.POST, params = { "accion=nuevoEstiloVisualizacionMapa" })
	public String nuevoEstiloVisualizacionMapa(@RequestParam(value = "id") Long idIndicador, Model model,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		Integer numRangos = (request.getParameter("num_rangos") != null && !request.getParameter("num_rangos").equals(
				"")) ? Integer.valueOf(request.getParameter("num_rangos")) : 0;
		long idUsuario = getUsuarioId();
		for (int i = 1; i <= numRangos; i++) {
			if (request.getParameter("inicio_rango_" + i) == null && request.getParameter("fin_rango_" + i) == null
					&& request.getParameter("color_rango_" + i) == null)
				continue;

			String color = request.getParameter("color_rango_" + i);
			String inicio = request.getParameter("inicio_rango_" + i);
			String fin = request.getParameter("fin_rango_" + i);
			// Validar nuevos datos de estilo
			if ((inicio == null || inicio.equals("")) || (fin == null || fin.equals(""))
					|| (color == null || color.equals(""))) {
				model.addAttribute("errorEstiloVisualizacion", "jsp.visualizacion.error.validacion.estilo.mapa");
				return visualizarIndicadorDiagramaBarras(idIndicador, "", null, model, request, null, null);
			}
		}

		RangosVisualizacionMapaDto rango = null;
		List<RangosVisualizacionMapaDto> listaRangos = new ArrayList<RangosVisualizacionMapaDto>();
		for (int i = 1; i <= numRangos; i++) {
			if (request.getParameter("inicio_rango_" + i) == null && request.getParameter("fin_rango_" + i) == null
					&& request.getParameter("color_rango_" + i) == null)
				continue;

			try {
				// Tenemos un rango, asi que lo añadimos a la lista
				rango = new RangosVisualizacionMapaDto();
				rango.setColor(request.getParameter("color_rango_" + i));
				Double inicioRango = Double.valueOf(request.getParameter("inicio_rango_" + i));
				rango.setInicio(inicioRango);
				Double finRango = Double.valueOf(request.getParameter("fin_rango_" + i));
				rango.setFin(finRango);
				listaRangos.add(rango);
			} catch (Exception ex) {
				// Si alguno de lo numeros casca al convertirse o cualquier otro
				// error. Mostramos error.
				model.addAttribute("errorEstiloVisualizacion", "jsp.visualizacion.error.validacion.estilo.mapa");
				return visualizarIndicadorMapa(idIndicador, "", null, model, request, null, null);
			}
		}

		EstiloVisualizacionMapaDto estiloMapa = new EstiloVisualizacionMapaDto();
		// Si no ha cascado, tenemos los valores correctos para el estilo
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("nuevoEstiloVisualizacionMapa");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(idUsuario);

		ResponseEntity<EncapsuladorPOSTSW> respuestaPost = getRestTemplate().postForEntity(url.getUrl(),
				new HttpEntity<EstiloVisualizacionMapaDto>(estiloMapa, getHeaders()), EncapsuladorPOSTSW.class);

		for (RangosVisualizacionMapaDto rangoTmp : listaRangos) {
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("indicadores");
			url.setParametroCadena("insertarRangosVisualizacionMapa");
			url.setParametroCadena(((EstiloVisualizacionMapaDto) respuestaPost.getBody().getObjetoEncapsulado())
					.getId());

			ResponseEntity<EncapsuladorPOSTSW> respuestaPost2 = getRestTemplate().postForEntity(url.getUrl(),
					new HttpEntity<RangosVisualizacionMapaDto>(rangoTmp, getHeaders()), EncapsuladorPOSTSW.class);
		}

		model.addAttribute("exitoCrear", "jsp.visualizacion.exito.crear");
		return visualizarIndicadorMapa(idIndicador, null, null, model, request, null, null);
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=exportar" })
	public String exportarIndicador(@RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "tipoGrafico") int tipoGrafico, @RequestParam(value = "tipo") String tipo,
			Model model, HttpServletRequest request, HttpServletResponse response) throws JsonParseException,
			JsonMappingException, IOException {
		// Exportar datos del indicador
		EncapsuladorFileSW fichero = new EncapsuladorFileSW();
		if (tipo.equals(TiposFuente.CSV.getId()))
			fichero = exportarIndicadorCSV(idIndicador);
		else if (tipo.equals(TiposFuente.BDESPACIAL.getId())) {
			fichero = exportarIndicadorBDEspacial(idIndicador);
		} else if (tipo.equals(TiposFuente.GML.getId())) {
			fichero = exportarIndicadorGML(idIndicador);
		} else if (tipo.equals(TiposFuente.SHAPEFILE.getId())) {
			fichero = exportarIndicadorShapeFile(idIndicador);
		}
		if (fichero == null || fichero.getFich() == null || fichero.getFich().length <= 0) {
			if (fichero.getNombre() != null && fichero.getNombre().equals(ERROR_NO_MAPA_SHAPEFILE))
				return visualizarGrafico(idIndicador, model, request, tipoGrafico, null,
						"jsp.indicador.exportar.shapefile.error");
			else
				return visualizarGrafico(idIndicador, model, request, tipoGrafico, null,
						"jsp.indicador.exportar.fichero.error");
		} else {
			model.addAttribute("fichero", fichero.getNombre());

			ServletContext sc = request.getSession().getServletContext();
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("name", fichero.getNombre());
			mapa.put("type", sc.getMimeType(fichero.getNombre()));
			mapa.put("inputStream", new ByteArrayInputStream(fichero.getFich()));
			try {
				renderMergedOutputModel(mapa, request, response);
			} catch (Exception e) {
				log.debug("Error al leer el fichero de exportacion para descarga");
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private ResponseEntity<EncapsuladorPOSTSW> guardarFicheroMetadatos(MultipartFile file, IndicadorDto indicador) {
		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;
		if (file != null && file.getSize() > 0) {
			UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("ficheros");
			url.setParametroCadena("ficheroMetadatos");

			EncapsuladorFileSW envioFichero = null;
			try {
				envioFichero = new EncapsuladorFileSW(file.getOriginalFilename(), file.getBytes(), indicador.getId());
			} catch (IOException ex) {
				return null;
			}
			HttpEntity<EncapsuladorFileSW> fichero = new HttpEntity<EncapsuladorFileSW>(envioFichero, getHeaders());
			respuestaFichero = getRestTemplate().postForEntity(url.getUrl(), fichero, EncapsuladorPOSTSW.class);
		} else
			return null;
		return respuestaFichero;
	}

	private ResponseEntity<EncapsuladorPOSTSW> borrarFicheroMetadatos(IndicadorDto indicador, BindingResult result) {
		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("ficheros");
		url.setParametroCadena("borrarFicheroMetadatos");

		HttpEntity<IndicadorDto> entity = new HttpEntity<IndicadorDto>(indicador, getHeaders());
		ResponseEntity<EncapsuladorPOSTSW> respuesta = getRestTemplate().postForEntity(url.getUrl(), entity,
				EncapsuladorPOSTSW.class);

		return respuesta;
	}

	private FuenteDto cargarFuente(Long idFuente) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("fuente");
		url.setParametroCadena(idFuente);

		FuenteDto respuesta = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();

		return respuesta;
	}

	protected Model rellenarDatosFormularioEstilo(Model model) {
		// Datos estilos
		// Tamanho fuente
		List<Double> tamanhosFuente = new ArrayList<Double>();
		for (Double i = 8D; i <= 40; i++) {
			tamanhosFuente.add(i);
		}

		// Valores tamanho fuente
		List<Integer> valoresTamanhosFuente = new ArrayList<Integer>();
		for (Integer i = 8; i <= 40; i++) {
			valoresTamanhosFuente.add(i);
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

		model.addAttribute("tamanhosFuente", tamanhosFuente);
		model.addAttribute("valoresTamanhosFuente", valoresTamanhosFuente);
		model.addAttribute("tiposLetra", tiposLetra);
		model.addAttribute("tiposFecha", tiposFecha);
		model.addAttribute("decimales", decimales);
		model.addAttribute("color_defecto", COLOR_DEFECTO);
		model.addAttribute("tamanho_defecto", TAMANHO_FUENTE_DEFECTO);
		model.addAttribute("fuente_defecto", FUENTE_DEFECTO);
		model.addAttribute("tipo_fecha_defecto", TIPO_FECHA_DEFECTO);
		return model;
	}

	private Model rellenarDatosFormularioEstiloDiagramaBarras(Model model) {
		// Datos estilos
		// Tamanho fuente
		List<Double> tamanhosFuente = new ArrayList<Double>();
		for (Double i = 8D; i <= 40; i++) {
			tamanhosFuente.add(i);
		}

		// Valores tamanho fuente
		List<Integer> valoresTamanhosFuente = new ArrayList<Integer>();
		for (Integer i = 8; i <= 40; i++) {
			valoresTamanhosFuente.add(i);
		}

		// Tipos Letra
		List<TiposLetra> tiposLetra = new ArrayList<TiposLetra>();
		for (TiposLetra tipo : TiposLetra.values())
			tiposLetra.add(tipo);

		// Tamanhos de columna
		List<Integer> tamanhosColumna = new ArrayList<Integer>();
		for (Integer i = 20; i <= 100; i += 5) {
			tamanhosColumna.add(i);
		}

		model.addAttribute("tamanhosFuente", tamanhosFuente);
		model.addAttribute("valoresTamanhosFuente", valoresTamanhosFuente);
		model.addAttribute("tiposLetra", tiposLetra);
		model.addAttribute("tamanhosColumna", tamanhosColumna);
		model.addAttribute("color_defecto", COLOR_DEFECTO);
		model.addAttribute("tamanho_defecto", TAMANHO_FUENTE_DEFECTO);
		model.addAttribute("tamanho_columna_defecto", TAMANHO_COLUMNA_DEFECTO);
		model.addAttribute("fuente_defecto", FUENTE_DEFECTO);
		return model;
	}

	private Model rellenarDatosFormularioEstiloDiagramaSectores(Model model) {
		// Datos estilos
		// Tamanho fuente
		List<Double> tamanhosFuente = new ArrayList<Double>();
		for (Double i = 8D; i <= 40; i++) {
			tamanhosFuente.add(i);
		}

		// Valores tamanho fuente
		List<Integer> valoresTamanhosFuente = new ArrayList<Integer>();
		for (Integer i = 8; i <= 40; i++) {
			valoresTamanhosFuente.add(i);
		}

		// Tipos Letra
		List<TiposLetra> tiposLetra = new ArrayList<TiposLetra>();
		for (TiposLetra tipo : TiposLetra.values())
			tiposLetra.add(tipo);

		// Diametros
		List<Integer> diametros = new ArrayList<Integer>();
		for (Integer i = 300; i <= 550; i += 25) {
			diametros.add(i);
		}

		model.addAttribute("tamanhosFuente", tamanhosFuente);
		model.addAttribute("valoresTamanhosFuente", valoresTamanhosFuente);
		model.addAttribute("tiposLetra", tiposLetra);
		model.addAttribute("diametros", diametros);
		model.addAttribute("colores_defecto", COLORES_DEFECTO);
		model.addAttribute("tamanho_defecto", TAMANHO_FUENTE_DEFECTO);
		model.addAttribute("fuente_defecto", FUENTE_DEFECTO);
		model.addAttribute("diametro_defecto", DIAMETRO_DEFECTO);
		return model;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Model rellenarDatosIndicadorPostValidacion(Model model, HttpServletRequest request) {
		String columnaAmbito = request.getParameter("columna_campo_ambito");
		String tablaAmbito = request.getParameter("tabla_campo_ambito");
		String columnaMapa = request.getParameter("columna_campo_mapa");
		String tablaMapa = request.getParameter("tabla_campo_mapa");

		model.addAttribute("tabla_ambito", tablaAmbito);
		model.addAttribute("columna_ambito", columnaAmbito);

		model.addAttribute("tabla_mapa", tablaMapa);
		model.addAttribute("columna_mapa", columnaMapa);

		Integer numCampos = 0;
		Integer numFormulas = 0;
		Integer numRelaciones = 0;
		Integer numCriterios = 0;

		Integer numCamposReal = 0;
		Integer numFormulasReal = 0;
		Integer numRelacionesReal = 0;
		Integer numCriteriosReal = 0;
		
		//Campos definidos para gestionar el contenido de la ZONA TABLAS (tablas, relaciones) cuando existen errores en el formulario
		//---------------------------------------------------------------------------------------------------------------------------
		String  strFuentesTablas = null;
		String strRelacionesTablas = null;
		
		if (request.getParameter("num_fuentes_tablas_envio")!=null 
				&& !request.getParameter("num_fuentes_tablas_envio").equals(""))
			strFuentesTablas = request.getParameter("num_fuentes_tablas_envio");

		if (request.getParameter("num_relacionestablas_envio")!=null 
				&& !request.getParameter("num_relacionestablas_envio").equals(""))
			strRelacionesTablas = request.getParameter("num_relacionestablas_envio");

		//---------------------------------------------------------------------------------------------------------------------------
		
		if (request.getParameter("num_campos_envio") != null 
				&& !request.getParameter("num_campos_envio").equals(""))
			numCampos = Integer.valueOf(request.getParameter("num_campos_envio"));

		if (request.getParameter("num_formulas_envio") != null
				&& !request.getParameter("num_formulas_envio").equals(""))
			numFormulas = Integer.valueOf(request.getParameter("num_formulas_envio"));

		if (request.getParameter("num_relaciones_envio") != null
				&& !request.getParameter("num_relaciones_envio").equals(""))
			numRelaciones = Integer.valueOf(request.getParameter("num_relaciones_envio"));

		if (request.getParameter("num_criterios_envio") != null
				&& !request.getParameter("num_criterios_envio").equals(""))
			numCriterios = Integer.valueOf(request.getParameter("num_criterios_envio"));

		EncapsuladorListSW<AtributoDto> listaColumnas = new EncapsuladorListSW<AtributoDto>();
		EncapsuladorListSW<CriterioDto> listaCriterios = new EncapsuladorListSW<CriterioDto>();
		List<TablaFuenteDatosDto> listaTablas = new ArrayList<TablaFuenteDatosDto>();
		Map<String, EncapsuladorListSW<String>> columnasTabla = new HashMap<String, EncapsuladorListSW<String>>();
		List<RelacionesJspDto> listaRelaciones = new ArrayList<RelacionesJspDto>();

		//FUENTES Y TABLAS (ZONA TABLAS) (si existen)
		//---------------------------------------------------------------------------------------------------------------------------
		if (strFuentesTablas!=null) {
			String[] listaFuentesTablasCargadas = strFuentesTablas.split(",");
	        for (int i = 0; i < listaFuentesTablasCargadas.length; i++) {
	        	String[] strFuenteTablaSeleccionada = listaFuentesTablasCargadas[i].split("\\|");
	        	//Por cada Fuente - Tabla cargarla en el formulario	        
	        	//Se obtiene el código de la fuente de datos asociado a la tabla que se va a cargar
	        	Long idFuente = Long.valueOf(strFuenteTablaSeleccionada[0]);
	        	//Se obtiene el nombre de la tabla que se va a cargar
	        	String tabla = 	strFuenteTablaSeleccionada[1];
	        	
	        	TablaFuenteDatosDto tablaFuenteDatosDto = new TablaFuenteDatosDto();
				FuenteDto fuenteDto = new FuenteDto();
				tablaFuenteDatosDto.setNombre(tabla);
				fuenteDto.setId(idFuente);
				tablaFuenteDatosDto.setFuente(fuenteDto);
	        	
				//Para cada tabla, obtengo sus columnas para mostrarlas
				UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
				url.setParametroCadena("fuentes");
				url.setParametroCadena("fuente");
				url.setParametroCadena(idFuente);

				fuenteDto = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();
				
				UrlConstructorSW url2 = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
				url2.setParametroCadena("fuentes");
				url2.setParametroCadena("columnasFuente");
				url2.setParametroCadena(tabla);
				url2.setParametroCadena(idFuente);
				url2.setParametroCadena(fuenteDto.getTipo().getId());
				ResponseEntity<EncapsuladorListSW> listaColumnasTabla = getRestTemplate().getForEntity(url2.getUrl(), EncapsuladorListSW.class);
								
				if ( !contiene(listaTablas,tablaFuenteDatosDto)) {
					listaTablas.add(tablaFuenteDatosDto);
										
					//RELACIONES ENTRE LAS TABLAS (ZONA TABLAS) (si existen)
					if (strRelacionesTablas!=null) {						
						String[] listaRelacionesTablas = strRelacionesTablas.split(",");			

						for (int j = 0; j < listaRelacionesTablas.length; j++) {
							
							//Por cada relación se obtienen sus valores
							 String[] strRelacionesTabla = listaRelacionesTablas[j].split("\\|");
							 AtributoFuenteDatosDto objAtributoFuenteDatosDto = new AtributoFuenteDatosDto();
							 TablaFuenteDatosDto objTablaFuenteDatosDto = new TablaFuenteDatosDto();
							 FuenteDto objFuenteOrigenDTO = new FuenteDto();
							 
							 Long strFuenteOrigen = Long.valueOf(strRelacionesTabla[0]);
							 String strTablaOrigen = strRelacionesTabla[1].substring("tabla_".length());
							 
							 if (strTablaOrigen!=null && !strTablaOrigen.equals(tablaFuenteDatosDto.getNombre()))
								 continue;
							 							
							 objAtributoFuenteDatosDto.setEsFormula(false);
							 objAtributoFuenteDatosDto.setEsRelacion(true);
							 objAtributoFuenteDatosDto.setTipoAtributo(TipoAtributoFD.VALORFDRELACION);
							 
							 objFuenteOrigenDTO.setId(strFuenteOrigen);							 
							 objTablaFuenteDatosDto.setNombre(strTablaOrigen);
							 objTablaFuenteDatosDto.setFuente(objFuenteOrigenDTO);
							 objAtributoFuenteDatosDto.setTabla(objTablaFuenteDatosDto);
							 							 							 
							 objAtributoFuenteDatosDto.setNombre(strRelacionesTabla[2]);
							 objAtributoFuenteDatosDto.setStrTipoDatoRelacion(strRelacionesTabla[3]);
							 objAtributoFuenteDatosDto.setStrFuenteRelacion(strRelacionesTabla[4]);
							 
							 objAtributoFuenteDatosDto.setStrTablaRelacion(strRelacionesTabla[5]);
							 objAtributoFuenteDatosDto.setStrColumnaRelacion(strRelacionesTabla[6]);
							 
							 listaColumnasTabla.getBody().add(objAtributoFuenteDatosDto);
							 
							 //SE MONTA LISTARELACIONES, cada relación leida se incluye en la lista
							 RelacionesJspDto relaciones = new RelacionesJspDto();
							 relaciones.setMostrar(false);
							 relaciones.setIdFuenteRelacion(strRelacionesTabla[0]);
							 relaciones.setTablaRelacion(strTablaOrigen);
							 relaciones.setColumnaRelacion(strRelacionesTabla[2]);
							 relaciones.setIdFuenteRelacionada(strRelacionesTabla[4]);
							 relaciones.setTablaRelacionada(strRelacionesTabla[5]);
							 relaciones.setColumnaRelacionada(strRelacionesTabla[6]);
							 relaciones.setOrdenVisualizacion("-1");
							
							 listaRelaciones.add(relaciones);
							 numRelacionesReal++;
						}
					}					
					
					columnasTabla.put(tabla, listaColumnasTabla.getBody());
				}
	        }
		}
		//---------------------------------------------------------------------------------------------------------------------------

		
		// CAMPOS NORMALES
		for (int i = 1; i <= numCampos; i++) {
			// Si vienen vacios los valores, es pq se añadio un campo y luego se
			// se le dio a quitar.
			if (request.getParameter("tabla_campo_" + i) == null && request.getParameter("columna_campo_" + i) == null
					&& request.getParameter("mostrar_campo_" + i) == null
					&& request.getParameter("idfuente_campo_" + i) == null)
				continue;

			Long idFuente = Long.valueOf(request.getParameter("idfuente_campo_" + i));
			String tabla = request.getParameter("tabla_campo_" + i);
			TablaFuenteDatosDto tablaFuenteDatosDto = new TablaFuenteDatosDto();
			FuenteDto fuenteDto = new FuenteDto();
			tablaFuenteDatosDto.setNombre(tabla);
			fuenteDto.setId(Long.valueOf(request.getParameter("idfuente_campo_" + i)));
			tablaFuenteDatosDto.setFuente(fuenteDto);
			List<AtributoFuenteDatosDto> listaColumnasTablaTiposDatos = new ArrayList<AtributoFuenteDatosDto>();

			// Para cada tabla, obtengo sus columnas para mostrarlas
			UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("fuentes");
			url.setParametroCadena("fuente");
			url.setParametroCadena(idFuente);

			fuenteDto = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();

			UrlConstructorSW url2 = new UrlConstructorSWImpl(getUrlBaseSw());
			url2.setParametroCadena("fuentes");
			url2.setParametroCadena("columnasFuente");
			url2.setParametroCadena(tabla);
			url2.setParametroCadena(idFuente);
			url2.setParametroCadena(fuenteDto.getTipo().getId());
			ResponseEntity<EncapsuladorListSW> listaColumnasTabla = getRestTemplate().getForEntity(url2.getUrl(),
					EncapsuladorListSW.class);

			// Siempre obtengo las columnas para poder obtener el tipo de dato
			// de cada columna
			if (!contiene(listaTablas, tablaFuenteDatosDto)) {
				listaTablas.add(tablaFuenteDatosDto);
				columnasTabla.put(tabla, listaColumnasTabla.getBody());
			}
			listaColumnasTablaTiposDatos = listaColumnasTabla.getBody().getLista();

			Boolean mostrar = false;
			if (request.getParameter("mostrar_campo_" + i).equals("true"))
				mostrar = true;
			// Campos que vienen en el formulario
			AtributoDto attr = new AtributoDto();
			AtributoFuenteDatosDto columna = new AtributoFuenteDatosDto();
			attr.setNombre(request.getParameter("columna_campo_" + i));
			for (AtributoFuenteDatosDto atributo : listaColumnasTablaTiposDatos) {
				if (atributo.getNombre().equals(attr.getNombre())) {
					columna.setTipoAtributo(atributo.getTipoAtributo());
				}
			}

			columna.setTabla(tablaFuenteDatosDto);
			attr.setMostrar(mostrar);

			attr.setColumna(columna);
			if (request.getParameter("orden_campo_" + i) != null
					&& !request.getParameter("orden_campo_" + i).equals(""))
				attr.setOrdenVisualizacion(Integer.valueOf(request.getParameter("orden_campo_" + i)));
			attr.setCriterio(null);
			numCamposReal++;

			// CRITERIOS
			for (int j = 1; j <= numCriterios; j++) {
				// Si vienen vacios los valores, es pq se añadio un campo y
				// luego se se le dio a quitar.
				if (request.getParameter("tabla_criterio_" + j) == null
						&& request.getParameter("columna_criterio_" + j) == null
						&& request.getParameter("valor_criterio_" + j) == null)
					continue;

				String nombreTablaCriterio = request.getParameter("tabla_criterio_" + j);
				String nombreColumnaCriterio = request.getParameter("columna_criterio_" + j);
				String valorCriterio = request.getParameter("valor_criterio_" + j);

				// Si el nombre de la columna y el nombre de la tabla son
				// iguales, esta columna tiene un criterio
				if (attr.getNombre().equals(nombreColumnaCriterio)
						&& attr.getColumna().getTabla().getNombre().equals(nombreTablaCriterio)) {
					CriterioDto criterio = new CriterioDto();
					criterio.setCadenaCriterio(valorCriterio);
					AtributoDto attrTmp = new AtributoDto();
					AtributoFuenteDatosDto attrFDTmp = new AtributoFuenteDatosDto();
					TablaFuenteDatosDto tablaTmp = new TablaFuenteDatosDto();
					FuenteDto fuenteTmp = new FuenteDto();
					tablaTmp.setNombre(nombreTablaCriterio);
					fuenteTmp.setId(idFuente);
					tablaTmp.setFuente(fuenteTmp);
					attrFDTmp.setNombre(nombreColumnaCriterio);
					attrFDTmp.setTabla(tablaTmp);
					attrTmp.setColumna(attrFDTmp);
					criterio.setIdAtributo(attrTmp.getId());
					attr.setCriterio(criterio);
				}
			}
			listaColumnas.add(attr);
		}

		// FORMULAS
		for (int i = 1; i <= numFormulas; i++) {
			// Si vienen vacios los valores, es pq se añadio un campo y luego se
			// se le dio a quitar.
			if (request.getParameter("nombre_formula_" + i) == null
					&& request.getParameter("valor_formula_" + i) == null
					&& request.getParameter("mostrar_formula_" + i) == null)
				continue;

			Boolean mostrar = false;
			if (request.getParameter("mostrar_formula_" + i).equals("true"))
				mostrar = true;
			// Campos que vienen en el formulario
			AtributoDto attr = new AtributoDto();
			attr.setMostrar(mostrar);
			attr.setNombre(request.getParameter("nombre_formula_" + i));

			AtributoFuenteDatosDto columna = new AtributoFuenteDatosDto();
			TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
			tabla.setNombre(request.getParameter("valor_formula_" + i));
			columna.setTabla(tabla);
			attr.setColumna(columna);

			IndicadorExpresionDto indiExpre = new IndicadorExpresionDto();
			indiExpre.setExpresionLiteral(request.getParameter("valor_formula_" + i));
			indiExpre.setExpresionTransformada(request.getParameter("valor_formula_" + i));
			attr.setIndicadorExpresion(indiExpre);

			if (request.getParameter("orden_formula_" + i) != null
					&& !request.getParameter("orden_formula_" + i).equals(""))
				attr.setOrdenVisualizacion(Integer.valueOf(request.getParameter("orden_formula_" + i)));
			attr.setCriterio(null);
			numFormulasReal++;

			// CRITERIOS
			for (int j = 1; j <= numCriterios; j++) {
				// Si vienen vacios los valores, es pq se añadio un campo y
				// luego se se le dio a quitar.
				if (request.getParameter("tabla_criterio_" + j) == null
						&& request.getParameter("columna_criterio_" + j) == null
						&& request.getParameter("valor_criterio_" + j) == null)
					continue;

				String nombreTablaCriterio = request.getParameter("tabla_criterio_" + j);
				String nombreColumnaCriterio = request.getParameter("columna_criterio_" + j);
				String valorCriterio = request.getParameter("valor_criterio_" + j);

				// Si el nombre de la columna y el nombre de la tabla son
				// iguales, esta columna tiene un criterio
				if (attr.getNombre().equals(nombreColumnaCriterio)
						&& attr.getColumna().getTabla().getNombre().equals(nombreTablaCriterio)) {
					CriterioDto criterio = new CriterioDto();
					criterio.setCadenaCriterio(valorCriterio);
					AtributoDto attrTmp = new AtributoDto();
					AtributoFuenteDatosDto attrFDTmp = new AtributoFuenteDatosDto();
					TablaFuenteDatosDto tablaTmp = new TablaFuenteDatosDto();
					tablaTmp.setNombre(nombreTablaCriterio);
					attrFDTmp.setNombre(nombreColumnaCriterio);
					attrFDTmp.setTabla(tablaTmp);
					attrTmp.setColumna(attrFDTmp);
					attr.setCriterio(criterio);
				}
			}
			listaColumnas.add(attr);
		}


		// CRITERIOS
		for (int i = 1; i <= numCriterios; i++) {
			// Si vienen vacios los valores, es pq se añadio un campo y luego se
			// se le dio a quitar.
			if (request.getParameter("tabla_criterio_" + i) == null
					&& request.getParameter("columna_criterio_" + i) == null
					&& request.getParameter("valor_criterio_" + i) == null)
				continue;

			String nombreTablaCriterio = request.getParameter("tabla_criterio_" + i);
			String nombreColumnaCriterio = request.getParameter("columna_criterio_" + i);
			String valorCriterio = request.getParameter("valor_criterio_" + i);

			// Monto lo necesario para obtener la lista de criterios
			CriterioDto criterio = new CriterioDto();
			AtributoDto attr = new AtributoDto();
			AtributoFuenteDatosDto columna = new AtributoFuenteDatosDto();
			TablaFuenteDatosDto tablaFuenteDatosDto = new TablaFuenteDatosDto();
			tablaFuenteDatosDto.setNombre(nombreTablaCriterio);
			columna.setTabla(tablaFuenteDatosDto);
			columna.setNombre(nombreColumnaCriterio);
			attr.setColumna(columna);
			criterio.setIdAtributo(attr.getId());
			criterio.setCadenaCriterio(valorCriterio);
			criterio.setAtributo(attr);
			listaCriterios.add(criterio);

			numCriteriosReal++;
		}

		model.addAttribute("listaTablas", listaTablas);
		model.addAttribute("listaColumnasTabla", columnasTabla);
		model.addAttribute("listaColumnasIndicador", listaColumnas);
		model.addAttribute("listaRelaciones", listaRelaciones);
		model.addAttribute("listaCriterios", listaCriterios);		

		model.addAttribute("numCampos", numCamposReal);
		model.addAttribute("numFormulas", numFormulasReal);
		model.addAttribute("numRelaciones", numRelacionesReal);
		model.addAttribute("numCriterios", numCriteriosReal);

		return model;
	}

	/**
	 * Añade un instante concreto a la lista de históricos del indicador
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@RequestMapping(method = RequestMethod.POST, params = { "accion=guardarVersion" })
	public String guardarVersion(@RequestParam(value = "id") Long idIndicador, Model model, HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {
		
		
		String strFecha = request.getParameter("historico_fecha");
		String idHistorico = request.getParameter("historicoId");
		
		int tipoGrafico = 0;
		try {
			tipoGrafico = Integer.parseInt(request.getParameter("tipoGrafico"));
		} catch (NumberFormatException ex) {
			tipoGrafico = 0;
		}
		
		HistoricoDto historico;
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());

		//Validar fecha de la versión
		if (strFecha == null || strFecha.isEmpty()) {
			model.addAttribute("errorGuardarVersion", "jsp.indicador.version.validacion.fecha.vacia");
			if (idHistorico == null || idHistorico.isEmpty() || idHistorico.equals("0")) {
				return visualizarGrafico(idIndicador, model, request, tipoGrafico, null, null);
			} else {
				url.setParametroCadena("historico").setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				return visualizarVersion(Long.parseLong(idHistorico), idIndicador, tipoGrafico, model, request);
			}
		}
		if(strFecha.length()!=10){
			model.addAttribute("errorGuardarVersion", "jsp.indicador.version.error.validacion.fecha.incorrecta");
			if (idHistorico == null || idHistorico.isEmpty() || idHistorico.equals("0")) {
				return visualizarGrafico(idIndicador, model, request, tipoGrafico, null, null);
			} else {
				url.setParametroCadena("historico").setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				return visualizarVersion(Long.parseLong(idHistorico), idIndicador, tipoGrafico, model, request);
			}
		}

		// Nueva versión
		if (idHistorico == null || idHistorico.isEmpty() || idHistorico.equals("0")) {
			
			historico = new HistoricoDto();
			
			/*
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("indicadores");
			url.setParametroCadena("obtenerDatos");
			url.setParametroCadena(idIndicador);
			//Obtener los datos del indicador
			AtributosMapDto datos = getRestTemplate().getForEntity(url.getUrl(), AtributosMapDto.class).getBody();
			*/
			
			AtributoMapDto datosConAtributos = new AtributoMapDto();
			
			//Se obtiene la lista de columnas (atributos) del indicador con el tipo AtributoDTO (contiene toda la información del atributo) 
			EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(idIndicador);

			/*Cuando existe una relación, los datos no se almacenan en la BBDD, por tal motivo lo necesario será calcular los datos de la misma
			 * forma que se calculan cuando se visualiza el indicador y de esta forma disponer de todas las columnas provincia, provincia1, provincia2 .. etc 
			 */
			AtributosMapDto datos = obtenerDatosIndicador(idIndicador, "datosIndicador", "no", listaColumnas);
			
			/*
			 * Se recorre la lista de columnas buscando los atributos que contiene el indicador.
			 * Se genera una nueva estructura datosConAtributos para almacener la versión con toda
			 * la información del atributo y la lista de valores
			 */
			Set<String> setlistaColumnas = datos.getContenido().keySet();
			Iterator<String> itlistaColumnas = setlistaColumnas.iterator();
			String strlistaColumna = null;
			AtributoDto atributoListaColumnas = null;
			AtributoHistoricoDto atributoListaColumnasHist = null;
			while (itlistaColumnas.hasNext()) {
				AtributoValoresDto atributoValores = null;
				//Por cada columna recuperada del histórico  
				strlistaColumna = itlistaColumnas.next();
				atributoListaColumnas = siExisteAtributoEnListaColumnas(strlistaColumna,listaColumnas);
				if (atributoListaColumnas!=null) {
					atributoListaColumnasHist = convertirAtributoEnHistorico(atributoListaColumnas);
					atributoValores = new AtributoValoresDto();
					atributoValores.setAtributo(atributoListaColumnasHist);
					atributoValores.setListaValores(datos.getContenido().get(strlistaColumna));					
					datosConAtributos.setValor(atributoListaColumnas.getNombre(),atributoValores);
				}
			}
			historico.setDatosDto(compress(writeArrayJson(datosConAtributos)));
			//historico.setDatosDto(compress(writeArrayJson(datos)));

			//Obtener mapa del indicador
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("indicadores");
			url.setParametroCadena("obtenerDatosMapa");
			url.setParametroCadena(idIndicador);
			AtributosMapDto datosMapa = getRestTemplate().getForEntity(url.getUrl(), AtributosMapDto.class).getBody();
			historico.setMapaDto(compress(writeArrayJson(datosMapa)));
			
		} else { // Modificar fecha versión
			url = new UrlConstructorSWImpl(getUrlBaseSw());
			url.setParametroCadena("historico").setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
			historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		Date fecha;
		try {
			cal.setTime(sdf.parse(strFecha));
			fecha = sdf.parse(strFecha);
		} catch (ParseException e) {
			model.addAttribute("errorGuardarVersion", "jsp.indicador.version.error.validacion.fecha.incorrecta");
			if (idHistorico == null || idHistorico.isEmpty() || idHistorico.equals("0")) {
				return visualizarGrafico(idIndicador, model, request, tipoGrafico, null, null);
			} else {
				url.setParametroCadena("historico").setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				return visualizarVersion(Long.parseLong(idHistorico), idIndicador, tipoGrafico, model, request);
			}
		}
//		if(cal.get(Calendar.YEAR)<1920){
//			model.addAttribute("errorGuardarVersion", "jsp.indicador.version.error.validacion.fecha1900.incorrecta");
//			if (idHistorico == null || idHistorico.isEmpty() || idHistorico.equals("0")) {
//				return visualizarGrafico(idIndicador, model, request, tipoGrafico, null, null);
//			} else {
//				url.setParametroCadena("historico").setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
//				return visualizarVersion(Long.parseLong(idHistorico), idIndicador, tipoGrafico, model, request);
//			}
//		}
		historico.setFecha(fecha);
		historico.setStrFecha(strFecha);

		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("historico");
		url.setParametroCadena("guardarHistorico");
		url.setParametroCadena(idIndicador);

		ResponseEntity<EncapsuladorPOSTSW> respuestaPost = getRestTemplate().postForEntity(url.getUrl(),
				new HttpEntity<HistoricoDto>(historico, getHeaders()), EncapsuladorPOSTSW.class);
		// Se evaluan errores en el formulario
		if (respuestaPost.getBody().hashErrors()) {
			model.addAttribute("errorGuardarVersion", "validacion.historico.fecha.repetida");
		}else{
			model.addAttribute("exitoCrear", "jsp.indicador.version.exito.crear");
		}

		if (idHistorico == null || idHistorico.isEmpty() || idHistorico.equals("0")) {
			return visualizarGrafico(idIndicador, model, request, tipoGrafico, null, null);
		} else {
			url.setParametroCadena("historico").setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
			return visualizarVersion(Long.parseLong(idHistorico), idIndicador, tipoGrafico, model, request);
		}
	}

	/** Borrado de una version */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=eliminarVersion" })
	public String eliminarVersion(@RequestParam(value = "id") Long id, @RequestParam(value = "idInd") Long idIndicador, 
			@RequestParam(value = "tipoGrafico") int tipoGrafico, 
			Model model, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("historico").setParametroCadena("eliminarHistorico").setParametroCadena(id);
		ResponseEntity<EncapsuladorIntegerSW> respuesta = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorIntegerSW.class);
		
		if (hasErrorsSW(respuesta)) {
			irPaginaErrorSW();
		}
		
		if (respuesta.getBody().getValor().equals(1)) {
			bolExitoEliminarVersion = true;
		} else if (respuesta.getBody().getValor().equals(0)) {
			bolExitoEliminarVersion = false;
		} else if (respuesta.getBody().getValor().equals(-1)) {
			bolExitoEliminarVersion = false;
			bolEntidadUtilizada = true;
		} else {
			bolExitoEliminarVersion = false;
			bolEntidadUtilizada = false;
		}
		bolSiAccionEliminarVersion = true;

		if (model.containsAttribute("idioma")) {
			model.addAttribute("idioma", null);
		}
		if (model.containsAttribute("usuarioAdmin")) {
			model.addAttribute("usuarioAdmin", null);
		}
		
		
		return "redirect:/indicadores.htm?accion=visualizarIndicadorTabla&id="+idIndicador;
		
		//return visualizarGrafico(idIndicador, model, request, tipoGrafico, null, null);
	}

	/**
	 * Visualización de una version
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=visualizarVersion" })
	public String visualizarVersion(@RequestParam(value = "id") Long idHistorico,
			@RequestParam(value = "idInd") Long idIndicador, @RequestParam(value = "tipoGrafico") int tipoGrafico,
			Model model, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		if(!esUsuarioConPermisos(idIndicador)){
			throw new HttpMessageNotReadableException("error");
		}
		if (idHistorico != 0) {
			UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("historico")
					.setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
			HistoricoDto historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			model.addAttribute("historico", historico);

			return visualizarGrafico(idIndicador, model, request, tipoGrafico, historico, null);
		} else {
			return visualizarGrafico(idIndicador, model, request, tipoGrafico, null, null);
		}
	}

	/**
	 * Visualiza los parámetros a escoger sobre los que se visualizará la
	 * evolución de un indicador
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "accion=mostrarOpcionesEvolucion" })
	public String mostrarOpcionesEvolucion(@RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "param", required = false) String parametro, Model model, HttpServletRequest request) {
		// Datos indicador
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicador = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();
		model.addAttribute("indicadorDto", indicador);
		model.addAttribute("idindicador", idIndicador);
		model.addAttribute("parametro", parametro);

		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicador.getId());
		AtributosMapDto datosAmbito = obtenerDatosAmbito(indicador, listaColumnas, model);
		datosAmbito=ordenarDatos(datosAmbito);
		model.addAttribute("datosAmbito", datosAmbito.getContenido().entrySet());

		int numFilas = obtenerNumeroFilas(datosAmbito);
		model.addAttribute("numFilas", numFilas);

		return "mostrarEvolucionTile";
	}

	private AtributosMapDto obtenerDatosAmbito(IndicadorDto indicador, EncapsuladorListSW<AtributoDto> listaColumnas, Model model) {
		
		UrlConstructorSW url;
		AtributoDto ambito = new AtributoDto();
		List<AtributoDto> listaColumnasNoMapa = new ArrayList<AtributoDto>();
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
		url = new UrlConstructorSWImpl(getUrlBaseSw());
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
	
	protected int obtenerNumeroFilas(AtributosMapDto datosAmbito) {
		Set<String> claves = datosAmbito.getContenido().keySet();
		String primeraClave = "";
		int numFilas = 0;
		if (claves.iterator().hasNext()) {
			primeraClave = claves.iterator().next();
			if (datosAmbito.getContenido().get(primeraClave) != null)
				// numFilas =
				// datosAmbito.getContenido().get(primeraClave).getValores().size()
				// - 1;
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
	 * 
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
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
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

		Set<Entry<String, ValorFDDto>> ambito = datosAmbito.getContenido().entrySet();
		ArrayList<String> leyenda = new ArrayList<String>();
		for (int i = 0; i < campos.size(); i++) {
			for (Map.Entry<String, ValorFDDto> valoresFila : ambito) {
				leyenda.add(valoresFila.getValue().getValores().get(campos.get(i)).getTexto());
			}
		}
		Collections.sort(leyenda);	
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
		url = new UrlConstructorSWImpl(getUrlBaseSw());
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
			if ((dato!=null) && (dato.length() > 0)) {
				dato = dato.substring(1);
			}
			dato = "[" + dato + "]";
			datos.add(dato);
			dato = "";
		}

		model.addAttribute("datos", datos);
		model.addAttribute("numCampos", numCampos);
		model.addAttribute("camposChk", campos);
		return mostrarOpcionesEvolucion(Long.parseLong(idIndicador), parametro, model, request);
	}

	/**
	 * Mantiene la visualización del gráfico desde el que se realizó la acción
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private String visualizarGrafico(Long idIndicador, Model model, HttpServletRequest request, int tipoGrafico,
			HistoricoDto historico, String error) throws JsonParseException, JsonMappingException, IOException {
		switch (tipoGrafico) {
		case 1:
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error);
		case 2:
			if (historico != null)
				return visualizarIndicadorDiagramaBarras(idIndicador, null, String.valueOf(historico.getId()), model,
						request, historico, error);
			else
				return visualizarIndicadorDiagramaBarras(idIndicador, null, null, model, request, historico, error);
		case 3:
			if (historico != null)
				return visualizarIndicadorDiagramaSectores(idIndicador, null, String.valueOf(historico.getId()), model,
						request, historico, error);
			else
				return visualizarIndicadorDiagramaSectores(idIndicador, null, null, model, request, historico, error);
		case 4:
			if (historico != null)
				return visualizarIndicadorMapa(idIndicador, null, String.valueOf(historico.getId()), model, request,
						historico, error);
			else
				return visualizarIndicadorMapa(idIndicador, null, null, model, request, historico, error);
		default:
			return visualizarIndicadorTabla(idIndicador, model, request, historico, error);
		}
	}

	@SuppressWarnings("unchecked")
	protected EncapsuladorListSW<AtributoDto> obtenerListaColumnas(Long idIndicador) {
		UrlConstructorSW url;
		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("atributos");
		url.setParametroCadena("atributos");
		url.setParametroCadena(idIndicador);

		EncapsuladorListSW<AtributoDto> listaColumnas = getRestTemplate().getForEntity(url.getUrl(),
				EncapsuladorListSW.class).getBody();

		// Repasamos la lista de columnas por si alguna tiene el mismo nombre q
		// otra, se le calcula otro nombre.
		// Que ademas coincidira con el nombre usado en el repositorio
		EncapsuladorListSW<AtributoDto> listaColumnas2 = new EncapsuladorListSW<AtributoDto>();
		for (AtributoDto attr : listaColumnas) {
			if (!attr.getMostrar() && !attr.getEsMapa())
				if (attr.getRelacion()==null) continue;
			String nombreColumna = attr.getNombre();
			if (contieneColumna(listaColumnas2, attr)) {
				Integer i = 1;
				while (contieneColumna(listaColumnas2, attr)) {
					attr.setNombre(nombreColumna + i.toString());
					i++;
				}
				listaColumnas2.add(attr);
			} else {
				listaColumnas2.add(attr);
			}
		}
		return listaColumnas2;
	}

	protected AtributosMapDto obtenerDatosIndicador(Long idIndicador, String metodo, Object parametro,
			EncapsuladorListSW<AtributoDto> listaColumnas) {
		UrlConstructorSW url;
		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("atributos");
		url.setParametroCadena(metodo);
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(parametro);

		AtributosMapDto datos = getRestTemplate().postForEntity(url.getUrl(),
				new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas), AtributosMapDto.class).getBody();
		return datos;
	}

	@RequestMapping(method = RequestMethod.GET, params = "accion=editaBusquedaAvanzada")
	public String editaBusquedaAvanzada(Model model) {
		model.addAttribute("indiBusqAvanzadaDto", new IndicadorBusquedaAvanzadaDto());
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("NEM").setParametroCadena("cargaTodos");
		model.addAttribute("listaAtributosNEM", getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class)
				.getBody());

		return "indicadoresBusquedaAvanzadaTile";
	}

	@RequestMapping(method = RequestMethod.POST, params = "accion=realizaBusquedaAvanzada")
	public String realizaBusquedaAvanzada(@ModelAttribute IndicadorBusquedaAvanzadaDto indicadorBusquedaAvanzadaDto,
			BindingResult result, Model model, HttpServletRequest request) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaBusquedaAvanzada")
				.setParametroCadena(getUsuarioId());
		EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> resultado = getRestTemplate()
				.postForEntity(url.getUrl(),
						new HttpEntity<IndicadorBusquedaAvanzadaDto>(indicadorBusquedaAvanzadaDto),
						EncapsuladorMapSW.class).getBody();

		if (indicadorBusquedaAvanzadaDto.getIdsFiltroMD() != null
				&& !indicadorBusquedaAvanzadaDto.getIdsFiltroMD().isEmpty()) {
			url.borrarParametrosCadena();
			url.setParametroCadena("NEM").setParametroCadena("cargaPorListaIds");
			model.addAttribute(
					"listaNEMUtilizados",
					getRestTemplate().postForEntity(
							url.getUrl(),
							new HttpEntity<EncapsuladorListSW<Long>>(new EncapsuladorListSW<Long>(
									indicadorBusquedaAvanzadaDto.getIdsFiltroMD())), EncapsuladorListSW.class)
							.getBody());

		} else {
			model.addAttribute("listaNEMUtilizados", null);
		}
		
		model.addAttribute("indicadorBusquedaAvanzadaDto", indicadorBusquedaAvanzadaDto);
		model.addAttribute("mapaIndicadores", writeJson(resultado.get("Indicadores")));
		model.addAttribute("mapaCategorias", writeJson(resultado.get("Categorias")));
		return "indicadoresBusquedaAvanzadaResultadoTile";
	}

	private boolean contiene(List<TablaFuenteDatosDto> lista, TablaFuenteDatosDto tablaDto) {
		boolean encontrado = false;
		for (TablaFuenteDatosDto tabla : lista) {
			if (tabla.getNombre().equals(tablaDto.getNombre()) && tabla.getId() == tablaDto.getId()) {
				encontrado = true;
				return encontrado;
			}
		}
		return encontrado;
	}

	private EncapsuladorFileSW exportarIndicadorCSV(Long idIndicador) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("exportarCSV");
		url.setParametroCadena(idIndicador);

		EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class).getBody();
		return fichero;
	}

	private EncapsuladorFileSW exportarIndicadorBDEspacial(Long idIndicador) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("exportarBDEspacial");
		url.setParametroCadena(idIndicador);

		EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class).getBody();
		return fichero;
	}

	/** Visualiza las versiones guardadas del indicador */
	@SuppressWarnings("unchecked")
	protected void mostrarHistorico(Long idIndicador, Model model) {
		UrlConstructorSW url;
		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("historico").setParametroCadena("cargarHistoricos").setParametroCadena(idIndicador);

		HistoricoDto historico = new HistoricoDto();
		historico.setId(0);
		List<HistoricoDto> historicos = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class)
				.getBody();
		historicos.add(historico);
		model.addAttribute("historicos", historicos);
		if (!model.containsAttribute("historico")) {
			model.addAttribute("historico", historico);
		}
	}

	private UsuarioDto cargaUsuarioPorIdSW(Long id) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("usuarios")
				.setParametroCadena("usuario").setParametroCadena(id);
		ResponseEntity<UsuarioDto> respuesta = getRestTemplate().getForEntity(url.getUrl(), UsuarioDto.class);
		return respuesta.getBody();
	}

	private EncapsuladorFileSW exportarIndicadorGML(Long idIndicador) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("exportarGML");
		url.setParametroCadena(idIndicador);

		EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class).getBody();
		return fichero;
	}

	private EncapsuladorFileSW exportarIndicadorShapeFile(Long idIndicador) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("exportarShapefile");
		url.setParametroCadena(idIndicador);

		EncapsuladorFileSW fichero = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorFileSW.class).getBody();
		return fichero;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, params = { "accion=listaIndicadoresPublicos" })
	public String mostrarListaIndicadoresPublicos(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		if (usuarioInvitado())
		{
			return irSinPermisos(request, response);
		}

		//if (!getServicioSeguridad().getUserDetails().getEsAdmin())
		//{
		//	return irSinPermisos(request, response);
		//}
		
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadores = new HashMap<Long, List<IndicadorDto>>();

		// Se recuperan las categorías e indicadores raíz
		UrlConstructorSW urlCat = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPadrePublicos");
		UrlConstructorSW urlInd = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores").setParametroCadena("cargaSinCategoriaPublicos");
		
		List<CategoriaDto> listaCategorias = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
		List<Long> categoriasPadre = new ArrayList<Long>();
		
		if (listaCategorias != null && listaCategorias.size() > 0)
		{
			mapaCategorias.put(0L, listaCategorias);
		}
		
		List<IndicadorDto> listaIndicadores = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
		
		if (listaIndicadores != null && listaIndicadores.size() > 0)
		{
			mapaIndicadores.put(0L, listaIndicadores);
		}

		urlCat.borraUltimoParametroCadena().setParametroCadena("cargaCategoriasPorPadrePublicos");
		urlInd.borraUltimoParametroCadena().setParametroCadena("cargaPorCategoriaPublicos");
	
		int indiceMapaCategorias = 0;
		// Se recorren todas las categorías, para buscar las categorías/indicadores que de ellas dependen
		while (indiceMapaCategorias < mapaCategorias.size())
		{
			Iterator<Entry<Long, List<CategoriaDto>>> it = mapaCategorias.entrySet().iterator();
			
			// Se recorre el mapa buscando el índice cuyo valor coincida con el id de la categoría padre
			while (it.hasNext()) 
			{
				Map.Entry elementoActual = (Map.Entry)it.next();
				
				// Si las categorías son Hijo de la Categoría Padre buscada
				if (elementoActual != null && elementoActual.getKey() != null && !categoriasPadre.contains(((Long)elementoActual.getKey()).longValue()))
				{
					List<CategoriaDto> categorias = (List<CategoriaDto>)elementoActual.getValue();
					
					for(CategoriaDto categoriaMadre : categorias)
					{
						// Se buscan categorías hijo
						urlCat.setParametroCadena(categoriaMadre.getId());
						List<CategoriaDto> listaCategoriasHijo = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
						// Si se obtienen resultados
						if (listaCategoriasHijo != null && listaCategoriasHijo.size() > 0)
						{
							mapaCategorias.put(categoriaMadre.getId(), listaCategoriasHijo);
						}
						
						// Se buscan indicadores que cuelguen de esta categoría
						urlInd.setParametroCadena(categoriaMadre.getId());
						List<IndicadorDto> listaIndicadoresHijo = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
						// Si se obtienen resultados
						if (listaIndicadoresHijo != null && listaIndicadoresHijo.size() > 0)
						{
							mapaIndicadores.put(categoriaMadre.getId(), listaIndicadoresHijo);
						}
		
						// Se resetean los parámetros de los servicios Web
						urlCat.borraUltimoParametroCadena();
						urlInd.borraUltimoParametroCadena();
					}
					
					// Se actualiza el índice en el mapa de categorías
					indiceMapaCategorias++;
					// Se añade el IdCategoriaPadre actual a la lista de revisados
					categoriasPadre.add(((Long)elementoActual.getKey()).longValue());
					
					break;
				}
			}
		}
		
		model.addAttribute("mapaCategoriasTabla", mapaCategorias);
		model.addAttribute("mapaIndicadoresTabla", mapaIndicadores);

		return "listaIndicadoresPublicosTile";
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=retirarIndicador" })
	public String retirarIndicador(@RequestParam(value = "id") Long id, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (usuarioInvitado())
			return irSinPermisos(request, response);

		IndicadorDto indicadorDto = getRestTemplate().getForEntity(
				new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores")
						.setParametroCadena("cargaPorId").setParametroCadena(id).getUrl(), IndicadorDto.class)
				.getBody();

		String asuntoCorreo = obtenerMensajeLocal("correo.asunto.retirar", null);
		String textoCorreo = obtenerMensajeLocal("correo.texto.retirar", indicadorDto.getNombre());

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("retirarIndicador");
		url.setParametroCadena(id);
		url.setParametroCadena(asuntoCorreo);
		url.setParametroCadena(textoCorreo);
		url.setParametroCadena(getUsuarioId());

		EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class)
				.getBody();

		if (resultado.getValorLogico())
			model.addAttribute("resultadoOperacion", "jsp.publicacion.indicador.retirado");
		else
			model.addAttribute("resultadoOperacionError", "jsp.publicacion.indicador.retirado.error");

		return mostrarListaIndicadoresPublicos(model, request, response);
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=publicarEnWeb" })
	public String publicarIndicadorEnWeb(@RequestParam(value = "id") Long id, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		if (usuarioInvitado())
			return irSinPermisos(request, response);

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("publicarEnWeb");
		url.setParametroCadena(id);
		url.setParametroCadena(getServicioSeguridad().getUserDetails().getUsername());
		url.setParametroCadena(getUsuarioId());

		EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class)
				.getBody();

		if (resultado.getValorLogico())
			model.addAttribute("resultadoOperacion", "jsp.publicacion.indicador.publicado.web");
		else
			model.addAttribute("resultadoOperacionError", "jsp.publicacion.indicador.publicado.web.error");

		return cargaTablaElementosJerarquia(model, request, null);
	}

	private Boolean contieneColumna(EncapsuladorListSW<AtributoDto> lista, AtributoDto col) {
		for (AtributoDto attr : lista) {
			if (attr.getNombre().equals(col.getNombre()))
				return true;
		}
		return false;
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=exportarPDF" })
	public String paginaExportarIndicadorPdf(@RequestParam(value = "id") Long id,
			@RequestParam(value = "tipo") String tipo, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(id);
		IndicadorDto resultado = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", resultado);

		model.addAttribute("tipo", tipo);

		model = rellenarDatosFormularioEstiloExportarPDF(model);

		EstiloVisualizacionTablaDto estiloTabla = Estilos.cargarEstiloVisualizacionTabla(getUsuarioId(),
				getUrlBaseSw(), getRestTemplate(), id);

		if (estiloTabla != null && estiloTabla.getId() > 0)
			model.addAttribute("estiloTabla", estiloTabla);
		else
			model.addAttribute("estiloTabla", null);

		EstiloVisualizacionDiagramaBarrasDto estiloBarras = Estilos.cargarEstiloDiagramaBarras(getUsuarioId(),
				getUrlBaseSw(), getRestTemplate(), id);

		if (estiloBarras != null && estiloBarras.getId() > 0)
			model.addAttribute("estiloBarras", estiloBarras);
		else
			model.addAttribute("estiloBarras", null);

		EstiloVisualizacionDiagramaSectoresDto estiloSectores = Estilos.cargarEstiloSectores(getUsuarioId(),
				getUrlBaseSw(), getRestTemplate(), id);

		if (estiloSectores != null && estiloSectores.getId() > 0)
			model.addAttribute("estiloSectores", estiloSectores);
		else
			model.addAttribute("estiloSectores", null);

		EstiloVisualizacionMapaDto estiloMapa = Estilos.cargarEstiloMapa(getUsuarioId(), getUrlBaseSw(),
				getRestTemplate(), id);

		// Rangos de Estilo de visualizacion de Mapa
		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaRangosVisualizacionMapa");
		url.setParametroCadena(estiloMapa.getId());

		EncapsuladorListSW<RangosVisualizacionMapaDto> listaRangos = getRestTemplate().getForEntity(url.getUrl(),
				EncapsuladorListSW.class).getBody();

		if (estiloMapa != null && estiloMapa.getId() > 0)
			model.addAttribute("estiloMapa", estiloMapa);
		else
			model.addAttribute("estiloMapa", null);

		List<RangosVisualizacionMapaDto> rangos = listaRangos.getLista();

		model.addAttribute("rangos", rangos);
		model.addAttribute("numRangos", rangos.size());

		// Obtenemos las columnas para su representación		
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(resultado.getId());
		
		//CAMBIO: SELECCIONAMOS UNICAMENTE LOS ATRIBUTOS QUE NO SON RELACION
		EncapsuladorListSW<AtributoDto> listaColumnasAux = new EncapsuladorListSW<AtributoDto>();
		for (AtributoDto atributoColumnas : listaColumnas) {			
			if (atributoColumnas.getRelacion()==null) {
				listaColumnasAux.add(atributoColumnas);
			}
		}
		
		model.addAttribute("listaColumnas", listaColumnasAux);

		return "paginaExportarIndicadorPdfTile";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET, params = { "accion=listaIndicadoresPendientes" })
	public String mostrarListaIndicadoresPendientes(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		if (usuarioInvitado())
		{
			return irSinPermisos(request, response);
		}

		if (!getServicioSeguridad().getUserDetails().getEsAdmin()) 
		{
			return irSinPermisos(request, response);
		}
		
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadores = new HashMap<Long, List<IndicadorDto>>();

		// Se recuperan las categorías e indicadores raíz
		UrlConstructorSW urlCat = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPadrePendientesDePublicos");
		UrlConstructorSW urlInd = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores").setParametroCadena("cargaSinCategoriaPendientesDePublicos");
		
		List<CategoriaDto> listaCategorias = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
		List<Long> categoriasPadre = new ArrayList<Long>();
		
		if (listaCategorias != null && listaCategorias.size() > 0)
		{
			mapaCategorias.put(0L, listaCategorias);
		}
		
		List<IndicadorDto> listaIndicadores = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
		
		if (listaIndicadores != null && listaIndicadores.size() > 0)
		{
			mapaIndicadores.put(0L, listaIndicadores);
		}

		urlCat.borraUltimoParametroCadena().setParametroCadena("cargaCategoriasPorPadrePendientesDePublicos");
		urlInd.borraUltimoParametroCadena().setParametroCadena("cargaPorCategoriaPendientesDePublicos");
	
		int indiceMapaCategorias = 0;
		// Se recorren todas las categorías, para buscar las categorías/indicadores que de ellas dependen
		while (indiceMapaCategorias < mapaCategorias.size())
		{
			Iterator<Entry<Long, List<CategoriaDto>>> it = mapaCategorias.entrySet().iterator();
			
			// Se recorre el mapa buscando el índice cuyo valor coincida con el id de la categoría padre
			while (it.hasNext()) 
			{
				Map.Entry elementoActual = (Map.Entry)it.next();
				
				// Si las categorías son Hijo de la Categoría Padre buscada
				if (elementoActual != null && elementoActual.getKey() != null && !categoriasPadre.contains(((Long)elementoActual.getKey()).longValue()))
				{
					List<CategoriaDto> categorias = (List<CategoriaDto>)elementoActual.getValue();
					
					for(CategoriaDto categoriaMadre : categorias)
					{
						// Se buscan categorías hijo
						urlCat.setParametroCadena(categoriaMadre.getId());
						List<CategoriaDto> listaCategoriasHijo = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
						// Si se obtienen resultados
						if (listaCategoriasHijo != null && listaCategoriasHijo.size() > 0)
						{
							mapaCategorias.put(categoriaMadre.getId(), listaCategoriasHijo);
						}
						
						// Se buscan indicadores que cuelguen de esta categoría
						urlInd.setParametroCadena(categoriaMadre.getId());
						List<IndicadorDto> listaIndicadoresHijo = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
						// Si se obtienen resultados
						if (listaIndicadoresHijo != null && listaIndicadoresHijo.size() > 0)
						{
							mapaIndicadores.put(categoriaMadre.getId(), listaIndicadoresHijo);
						}
		
						// Se resetean los parámetros de los servicios Web
						urlCat.borraUltimoParametroCadena();
						urlInd.borraUltimoParametroCadena();
					}
					
					// Se actualiza el índice en el mapa de categorías
					indiceMapaCategorias++;
					// Se añade el IdCategoriaPadre actual a la lista de revisados
					categoriasPadre.add(((Long)elementoActual.getKey()).longValue());
					
					break;
				}
			}
		}
		
		model.addAttribute("mapaCategoriasTabla", mapaCategorias);
		model.addAttribute("mapaIndicadoresTabla", mapaIndicadores);
		
		return "listaIndicadoresPendientesTile";
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=autorizarIndicador" })
	public String autorizarIndicador(@RequestParam(value = "id") Long id, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (usuarioInvitado())
			return irSinPermisos(request, response);
		if (!getServicioSeguridad().getUserDetails().getEsAdmin())
			return irSinPermisos(request, response);

		IndicadorDto indicadorDto = getRestTemplate().getForEntity(
				new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores")
						.setParametroCadena("cargaPorId").setParametroCadena(id).getUrl(), IndicadorDto.class)
				.getBody();

		String asuntoCorreo = obtenerMensajeLocal("correo.asunto.autorizar", null);
		String textoCorreo = obtenerMensajeLocal("correo.texto.autorizar", indicadorDto.getNombre());

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("autorizarIndicador");
		url.setParametroCadena(id);
		url.setParametroCadena(asuntoCorreo);
		url.setParametroCadena(textoCorreo);
		url.setParametroCadena(getUsuarioId());

		EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class)
				.getBody();

		if (resultado.getValorLogico())
			model.addAttribute("resultadoOperacion", "jsp.publicacion.indicador.autorizado");
		else
			model.addAttribute("resultadoOperacionError", "jsp.publicacion.indicador.autorizado.error");

		return mostrarListaIndicadoresPendientes(model, request, response);
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=descartarIndicador" })
	public String descartarIndicador(@RequestParam(value = "id") Long id, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (usuarioInvitado())
			return irSinPermisos(request, response);
		if (!getServicioSeguridad().getUserDetails().getEsAdmin())
			return irSinPermisos(request, response);

		IndicadorDto indicadorDto = getRestTemplate().getForEntity(
				new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores")
						.setParametroCadena("cargaPorId").setParametroCadena(id).getUrl(), IndicadorDto.class)
				.getBody();

		String asuntoCorreo = obtenerMensajeLocal("correo.asunto.descartar", null);
		String textoCorreo = obtenerMensajeLocal("correo.texto.descartar", indicadorDto.getNombre());
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("descartarIndicador");
		url.setParametroCadena(id);
		url.setParametroCadena(asuntoCorreo);
		url.setParametroCadena(textoCorreo);
		url.setParametroCadena(getUsuarioId());

		EncapsuladorBooleanSW resultado = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class)
				.getBody();

		if (resultado.getValorLogico())
			model.addAttribute("resultadoOperacion", "jsp.publicacion.indicador.descartado");
		else
			model.addAttribute("resultadoOperacionError", "jsp.publicacion.indicador.descartado.error");

		return mostrarListaIndicadoresPendientes(model, request, response);
	}

	// GENERACION DE INFORME PDF
	// ---------------------------------------------------------------------------

	@RequestMapping(method = RequestMethod.POST, params = { "accion=generarPdf" })
	public String generarPdf(@RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "param", required = false) String parametro,
			@RequestParam(value = "idHistorico", required = false) String idHistorico, Model model,
	 		HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {

		generarInformeTabla(idIndicador, model, request, historico, error);
		generarInformesDeSectores(idIndicador, idHistorico, model, request, historico, error);
		generarInformesDeBarras(idIndicador, idHistorico, model, request, historico, error);
		generarInformesDeMapas(idIndicador, idHistorico, model, request, historico, error);
		
		String formatoImpresion = request.getParameter("formato_impresion");
		model.addAttribute("formato_impresion", formatoImpresion.toLowerCase());
		return "generarPdfTile";
	}
	
	@SuppressWarnings("rawtypes")
	private void generarInformesDeMapas(Long idIndicador, String idHistorico, Model model,
			HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {
		
		Map parametros = request.getParameterMap();
		String prefijoTabla = "mapa_";
		List<String> parametrosMapasAMostrar = new ArrayList<String>();
		List<DatosDiagramaMapa> datosMapas = new ArrayList<DatosDiagramaMapa>();
		for (Object parametroAMostrar : parametros.keySet()) {
			String nombreParametro = parametroAMostrar.toString();
			if (nombreParametro.startsWith(prefijoTabla)) {
				nombreParametro = nombreParametro.substring(prefijoTabla.length());
				parametrosMapasAMostrar.add(nombreParametro);
				datosMapas.add(generarInformeDeMapa(idIndicador, nombreParametro, idHistorico, model, request,
						historico, error));
			}
		}
		model.addAttribute("datos_mapas", datosMapas);
		model.addAttribute("numero_mapas", datosMapas.size());
		model.addAttribute("parametrosMapasAMostrar", parametrosMapasAMostrar);
	}
	
	public DatosDiagramaMapa generarInformeDeMapa(@RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "param", required = false) String parametro,
			@RequestParam(value = "idHistorico", required = false) String idHistorico, Model model,
			HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", indicadorDto);

//		EstiloVisualizacionMapaDto estilo = Estilos.cargarEstiloMapa(getUsuarioId(), getUrlBaseSw(), getRestTemplate(),
//				idIndicador);
//
//		// Rangos de Estilo de visualizacion de Mapa
//		url = new UrlConstructorSWImpl(getUrlBaseSw());
//		url.setParametroCadena("indicadores");
//		url.setParametroCadena("cargaRangosVisualizacionMapa");
//		url.setParametroCadena(estilo.getId());
//
//		EncapsuladorListSW<RangosVisualizacionMapaDto> listaRangos = estiloInformeMapa(request);
//
//		if (estilo != null && estilo.getId() > 0)
//			model.addAttribute("estilo_mapa", estilo);
//		else
//			model.addAttribute("estilo_mapa", null);

		model.addAttribute("color_defecto", COLOR_DEFECTO);

		List<RangosVisualizacionMapaDto> rangos = estiloInformeMapa(request);

		DatosDiagramaMapa resultado = new DatosDiagramaMapa();

		resultado.setRangos(rangos);
		resultado.setNumRangos(rangos.size());

		// Preparamos los datos para su visualizacion
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());

		//Se obtiene la columna MAPA para eliminarla (se incorporó para la visualización del mapa
		AtributoFuenteDatosDto columnaAtributoMapa = null;			

		// Comprobamos si el indicador tiene un campo mapa y un campo numerico
		// al menos para poder visualizar el mapa
		boolean tieneMapa = false;
		boolean tieneNumerico = false;
		for (AtributoDto attr : listaColumnas) {
			// Campo mapa
			if (attr.getEsMapa()) {
				columnaAtributoMapa = attr.getColumna();
				tieneMapa = true;
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
			return null;
		}

		if (!tieneNumerico) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return null;
		}

		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("atributos");
		url.setParametroCadena("datosIndicadorParametro");
		url.setParametroCadena(indicadorDto.getId());
		if (parametro == null || parametro.equals(""))
			parametro = NO_PARAMETRO;
		url.setParametroCadena(parametro);

		AtributosMapDto datos = new AtributosMapDto();
		if (idHistorico != null && !idHistorico.equals("0")) {
			if (historico.getDatosDto() == null) {
				url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("historico")
						.setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			}
			AtributosMapDto datosFromHistorico = (AtributosMapDto) writeObject(decompress(historico.getDatosDto()));
			String ambito = "";
			datos = new AtributosMapDto();
			LinkedHashMap<String, ValorFDDto> eltosFromHistorico = new LinkedHashMap<String, ValorFDDto>();
			LinkedHashMap<String, ValorFDDto> mapaDatos = datosFromHistorico.getContenido();
			for (String key : mapaDatos.keySet()) {
				int i = 0;
				boolean campoCorrecto = false;
				while (!campoCorrecto) {
					if (listaColumnas.get(i).getEsMapa() || listaColumnas.get(i).getEsAmbito()) {
						if (listaColumnas.get(i).getEsAmbito()) {
							ambito = listaColumnas.get(i).getNombre();
							campoCorrecto = true;
						}
						i++;
						continue;
					}
				}
				if (parametro.equals(NO_PARAMETRO)) {
					campoCorrecto = false;
					while (!campoCorrecto) {
						if (((listaColumnas.get(i).getColumna() == null && listaColumnas.get(i).getIndicadorExpresion() != null) || (listaColumnas
								.get(i).getColumna() != null && listaColumnas.get(i).getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO))
								&& listaColumnas.get(i).getRelacion() == null) {
							parametro = listaColumnas.get(i).getNombre();
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
			resultado.setHistorico(historico);
		} else {
			datos = getRestTemplate().postForEntity(url.getUrl(),
					new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas), AtributosMapDto.class).getBody();
		}

		//url = new UrlConstructorSWImpl(getUrlBaseSw());
		//url.setParametroCadena("atributos");
		//url.setParametroCadena("mapaIndicador");
		//url.setParametroCadena(indicadorDto.getId());
		
		LinkedHashMap<String,ValorFDDto> columnaMapa = new LinkedHashMap<String,ValorFDDto>();
		AtributosMapDto datosMapa = new AtributosMapDto();
		if (historico != null && historico.getMapaDto() != null) {
			datosMapa = (AtributosMapDto) writeObject(decompress(historico.getMapaDto()));
		} else {
			//datosMapa = getRestTemplate().postForEntity(url.getUrl(),
			//		new HttpEntity<EncapsuladorListSW<AtributoDto>>(listaColumnas), AtributosMapDto.class).getBody();
			columnaMapa.put(columnaAtributoMapa.getNombre(), datos.getContenido().get(columnaAtributoMapa.getNombre()));
			datosMapa.setContenido(columnaMapa);
		}
		
		datos.getContenido().remove(columnaAtributoMapa.getNombre());
		int numFilas = obtenerNumeroFilas(datos);

		List<AtributoDto> listaColumnasNoMapa = new ArrayList<AtributoDto>();
		for (AtributoDto col : listaColumnas) {
			if (col.getMostrar()
					&& !col.getEsMapa()
					&& !col.getEsAmbito()
					&& (((col.getColumna() == null && col.getIndicadorExpresion() != null) || (col.getColumna() != null && col
							.getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO)) && col.getRelacion() == null))
				listaColumnasNoMapa.add(col);
		}

		resultado.setListaColumnas(listaColumnasNoMapa);
		resultado.setParametro(parametro);
		resultado.setNumFilas(numFilas);
		resultado.setDatos(datos.getContenido().entrySet());
		resultado.setMapa(datosMapa.getContenido().entrySet());
		// Mostrar histórico
		mostrarHistorico(idIndicador, model);
		comprobarUsuario(model, indicadorDto);

		// Error fichero exportado
		if (error != null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}

		return resultado;
	}	
	
	@SuppressWarnings("rawtypes")
	private void generarInformesDeBarras(Long idIndicador, String idHistorico, Model model,
			HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {
		
		Map parametros = request.getParameterMap();
		String prefijoTabla = "barras_";
		List<String> parametrosSectoresAMostrar = new ArrayList<String>();
		List<DatosDiagramaBarras> datosSectores = new ArrayList<DatosDiagramaBarras>();
		int numeroInformes = 0;
		for (Object parametroAMostrar : parametros.keySet()) {
			String nombreParametro = parametroAMostrar.toString();
			if (nombreParametro.startsWith(prefijoTabla)) {
				nombreParametro = nombreParametro.substring(prefijoTabla.length());
				parametrosSectoresAMostrar.add(nombreParametro);
				DatosDiagramaBarras informeDeBarras = generarInformeDeBarras(idIndicador, nombreParametro, idHistorico, model, request,
						historico, error);
				datosSectores.add(informeDeBarras);
				numeroInformes += (int) informeDeBarras.getNumPaginasDiagramas();
			}
		}
		model.addAttribute("datos_barras", datosSectores);
		model.addAttribute("numero_barras", numeroInformes);
		model.addAttribute("parametrosBarrasAMostrar", parametrosSectoresAMostrar);
	}
	
	public DatosDiagramaBarras generarInformeDeBarras(@RequestParam(value = "id") Long idIndicador,
			@RequestParam(value = "param", required = false) String parametro,
			@RequestParam(value = "idHistorico", required = false) String idHistorico, Model model,
			HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", indicadorDto);
		EstiloVisualizacionDiagramaBarrasDto estiloDefecto=Estilos.cargarEstiloDiagramaBarras(getUsuarioId(),
				getUrlBaseSw(), getRestTemplate(), idIndicador);
		EstiloVisualizacionDiagramaBarrasDto estilo = estiloInformeDiagramaBarras(request,estiloDefecto);

		if (estilo != null)
			model.addAttribute("estilo_barras", estilo);
		else
			model.addAttribute("estilo_barras", null);

		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());

		//Se obtiene la columna MAPA para eliminarla (se incorporó para la visualización del mapa
		AtributoFuenteDatosDto columnaAtributoMapa = null;			
		for (AtributoDto attr : listaColumnas) {
			// Campo mapa
			if (attr.getEsMapa()) { 
				columnaAtributoMapa = attr.getColumna();	
				break;
			}
		}
		
		List<AtributoDto> listaColumnasNoMapa = new ArrayList<AtributoDto>();
		for (AtributoDto col : listaColumnas) {
			if (col.getMostrar()
					&& !col.getEsMapa()
					&& !col.getEsAmbito()
					&& (((col.getColumna() == null && col.getIndicadorExpresion() != null) || (col.getColumna() != null && col
							.getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO)) && col.getRelacion() == null))
				listaColumnasNoMapa.add(col);
		}

		if (listaColumnasNoMapa.size() <= 0) {
			model.addAttribute("errorColumnas", "jsp.visualizacion.error.columnas");
			return null;
		}

		model.addAttribute("listaColumnas", listaColumnasNoMapa);
		model.addAttribute("parametro", parametro);
		model.addAttribute("idindicador", idIndicador);
		model.addAttribute("elementosPagina", getServicioConfiguracionGeneral().getTamanhoPagina());

		url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("atributos");
		url.setParametroCadena("datosIndicadorParametro");
		url.setParametroCadena(indicadorDto.getId());
		if (parametro == null || parametro.equals(""))
			parametro = NO_PARAMETRO;
		url.setParametroCadena(parametro);

		AtributosMapDto datos = new AtributosMapDto();
		if (idHistorico != null && !idHistorico.equals("0")) {
			if (historico.getDatosDto() == null) {
				url = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("historico")
						.setParametroCadena("cargarPorId").setParametroCadena(idHistorico);
				historico = getRestTemplate().getForEntity(url.getUrl(), HistoricoDto.class).getBody();
			}
			AtributosMapDto datosFromHistorico = (AtributosMapDto) writeObject(decompress(historico.getDatosDto()));
			String ambito = "";
			datos = new AtributosMapDto();
			LinkedHashMap<String, ValorFDDto> eltosFromHistorico = new LinkedHashMap<String, ValorFDDto>();
			LinkedHashMap<String, ValorFDDto> mapaDatos = datosFromHistorico.getContenido();
			for (String key : mapaDatos.keySet()) {
				int i = 0;
				boolean campoCorrecto = false;
				while (!campoCorrecto) {
					if (listaColumnas.get(i).getEsMapa() || listaColumnas.get(i).getEsAmbito()) {
						if (listaColumnas.get(i).getEsAmbito()) {
							ambito = listaColumnas.get(i).getNombre();
							campoCorrecto = true;
						}
						i++;
						continue;
					}
				}
				if (parametro.equals(NO_PARAMETRO)) {
					campoCorrecto = false;
					while (!campoCorrecto) {
						if (((listaColumnas.get(i).getColumna() == null && listaColumnas.get(i).getIndicadorExpresion() != null) || (listaColumnas
								.get(i).getColumna() != null && listaColumnas.get(i).getColumna().getTipoAtributo() == TipoAtributoFD.VALORFDNUMERICO))
								&& listaColumnas.get(i).getRelacion() == null) {
							parametro = listaColumnas.get(i).getNombre();
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
		
		if (columnaAtributoMapa!=null) {
			datos.getContenido().remove(columnaAtributoMapa.getNombre());
		}
		
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
		
		
		List<ParDiagramaBarrasDto>listaOrdenada=new ArrayList<ParDiagramaBarrasDto>();
		int tamanhoListaMenor=0;
		if(listaValoresAmbito.size()>listaValores.size()){
			tamanhoListaMenor=listaValores.size();
		}else{
			tamanhoListaMenor=listaValoresAmbito.size();
		}
		for(int i=0; i<tamanhoListaMenor;i++){
			if(listaValoresAmbito.get(i).getTexto()!=null && listaValores.get(i).getTexto()!=null){
				listaOrdenada.add(new ParDiagramaBarrasDto(listaValoresAmbito.get(i).getTexto(),listaValores.get(i).getTexto()));
			}
		}
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
		int numFilas = obtenerNumeroFilas(datos);

		model = rellenarDatosFormularioEstiloDiagramaBarras(model);
		// Mostrar histórico
		mostrarHistorico(idIndicador, model);
		comprobarUsuario(model, indicadorDto);

		// Error fichero exportado
		if (error != null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}

		DatosDiagramaBarras datosDiagramaBarras = new DatosDiagramaBarras();
		datosDiagramaBarras.setNumPaginasDiagramas(Math.ceil(numFilas / getServicioConfiguracionGeneral().getTamanhoPagina()));
		datosDiagramaBarras.setNumFilas(numFilas);
		datosDiagramaBarras.setDatos(datosRepresentar);
		datosDiagramaBarras.setParametro(parametro);
		datosDiagramaBarras.setElementosPagina(getServicioConfiguracionGeneral().getTamanhoPagina());
		return datosDiagramaBarras;
	}	

	@SuppressWarnings("rawtypes")
	private void generarInformesDeSectores(Long idIndicador, String idHistorico, Model model,
			HttpServletRequest request, HistoricoDto historico, String error) throws JsonParseException,
			JsonMappingException, IOException {
		Map parametros = request.getParameterMap();
		String prefijoTabla = "sectores_";
		List<String> parametrosSectoresAMostrar = new ArrayList<String>();
		List<DatosDiagramaSectores> datosSectores = new ArrayList<DatosDiagramaSectores>();
		for (Object parametroAMostrar : parametros.keySet()) {
			String nombreParametro = parametroAMostrar.toString();
			if (nombreParametro.startsWith(prefijoTabla)) {
				nombreParametro = nombreParametro.substring(prefijoTabla.length());
				parametrosSectoresAMostrar.add(nombreParametro);
				datosSectores.add(generarInformeSectores(idIndicador, nombreParametro, idHistorico, model, request,
						historico, error));
			}
		}
		model.addAttribute("datos_sectores", datosSectores);
		model.addAttribute("numero_sectores", datosSectores.size());
		if(datosSectores.size()>0 && datosSectores.get(0)!=null){
			Iterator<Map.Entry<String,ValorFDDto> >iterador=datosSectores.get(0).getDatos().iterator();
			if(iterador.hasNext()){
				if(iterador.next().getValue().getValores().size()>20){
					model.addAttribute("tiene_anexo",true);
				}else{
					model.addAttribute("tiene_anexo", false);
				}
			}else{
				model.addAttribute("tiene_anexo", false);
			}
		}else{
			model.addAttribute("tiene_anexo", false);
		}
		model.addAttribute("parametrosSectoresAMostrar", parametrosSectoresAMostrar);
	}

	public DatosDiagramaSectores generarInformeSectores(Long idIndicador, String parametro,
			String idHistorico, Model model, HttpServletRequest request, HistoricoDto historico, String error)
			throws JsonParseException, JsonMappingException, IOException {

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(idIndicador);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", indicadorDto);
		EstiloVisualizacionDiagramaSectoresDto estiloDefecto = Estilos.cargarEstiloSectores(getUsuarioId(), getUrlBaseSw(),
				getRestTemplate(), idIndicador);
		EstiloVisualizacionDiagramaSectoresDto estilo = estiloInformeDiagramaSectores(request,estiloDefecto);

		if (estilo != null)
			model.addAttribute("estilo_sectores", estilo);
		else
			model.addAttribute("estilo_sectores", null);

		// Preparamos los datos para su visualizacion
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());

		//Se obtiene la columna MAPA para eliminarla (se incorporó para la visualización del mapa
		AtributoFuenteDatosDto columnaAtributoMapa = null;			
		for (AtributoDto attr : listaColumnas) {
			// Campo mapa
			if (attr.getEsMapa()) { 
				columnaAtributoMapa = attr.getColumna();	
				break;
			}
		}
		
		model.addAttribute("parametro", parametro);
		model.addAttribute("idindicador_sectores", idIndicador);
		model.addAttribute("elementosPagina", getServicioConfiguracionGeneral().getTamanhoPagina());

		if (parametro == null || parametro.equals(""))
			parametro = NO_PARAMETRO;

		AtributosMapDto datos = obtenerDatosIndicador(indicadorDto.getId(), "datosIndicadorParametro", parametro,
				listaColumnas);
		
		if (columnaAtributoMapa!=null) {
			datos.getContenido().remove(columnaAtributoMapa.getNombre());
		}
		
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
		
		List<ParDiagramaBarrasDto>listaOrdenada=new ArrayList<ParDiagramaBarrasDto>();
		int tamanhoListaMenor=0;
		if(listaValoresAmbito.size()>listaValores.size()){
			tamanhoListaMenor=listaValores.size();
		}else{
			tamanhoListaMenor=listaValoresAmbito.size();
		}
		for(int i=0; i<tamanhoListaMenor;i++){
			if(listaValoresAmbito.get(i).getTexto()!=null && listaValores.get(i).getTexto()!=null){
				listaOrdenada.add(new ParDiagramaBarrasDto(listaValoresAmbito.get(i).getTexto(),listaValores.get(i).getTexto()));
			}
		}
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
		
		int numFilas = obtenerNumeroFilas(datos);

		model.addAttribute("numFilas_sectores", numFilas);
		model.addAttribute("numPaginasDiagramas",
				Math.ceil(numFilas / getServicioConfiguracionGeneral().getTamanhoPagina()));

		model = rellenarDatosFormularioEstiloDiagramaSectores(model);
		// Mostrar histórico
		mostrarHistorico(idIndicador, model);
		comprobarUsuario(model, indicadorDto);

		// Error fichero exportado
		if (error != null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}

		DatosDiagramaSectores resultado = new DatosDiagramaSectores();
		resultado.setDatos(datosRepresentar);
		resultado.setNumPaginasDiagramas(Math.ceil(numFilas / getServicioConfiguracionGeneral().getTamanhoPagina()));
		resultado.setParametro(parametro);
		return resultado;
	}

	@SuppressWarnings("rawtypes")
	private void generarInformeTabla(Long id, Model model, HttpServletRequest request, HistoricoDto historico,
			String error) throws JsonParseException, JsonMappingException, IOException {
		Map parametros = request.getParameterMap();
		Set<String> columnasAMostrar = new HashSet<String>();
		String prefijoTabla = "tabla_";
		for (Object parametro : parametros.keySet()) {
			String nombreParametro = parametro.toString();
			if (nombreParametro.startsWith(prefijoTabla)) {
				columnasAMostrar.add(nombreParametro.substring(prefijoTabla.length()));
			}
		}

		UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBaseSw());
		url.setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(id);
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(url.getUrl(), IndicadorDto.class).getBody();

		model.addAttribute("indicadorDto", indicadorDto);
		EstiloVisualizacionTablaDto estiloDefecto = Estilos.cargarEstiloVisualizacionTabla(getUsuarioId(), getUrlBaseSw(),
				getRestTemplate(), id);
		EstiloVisualizacionTablaDto estilo = estiloInformeTabla(request,estiloDefecto);

		if (estilo != null)
			model.addAttribute("estilo", estilo);
		else
			model.addAttribute("estilo", null);

		// Preparamos los datos para su visualizacion
		EncapsuladorListSW<AtributoDto> listaColumnas = obtenerListaColumnas(indicadorDto.getId());
		EncapsuladorListSW<AtributoDto> listaColumnasMostrar = new EncapsuladorListSW<AtributoDto>();

		Object parametro;
		if (estilo.getTipoFecha() == null)
			parametro = "no";
		else
			parametro = estilo.getTipoFecha().getDescripcion();

		AtributosMapDto datos = new AtributosMapDto();
		if (historico != null && historico.getDatosDto() != null) {
			datos = (AtributosMapDto) writeObject(decompress(historico.getDatosDto()));
			model.addAttribute("historico", historico);
		} else {
			datos = obtenerDatosIndicador(indicadorDto.getId(), "datosIndicador", parametro, listaColumnas);
		}

		int numFilas = obtenerNumeroFilas(datos);
		model.addAttribute("datos", datos.getContenido().entrySet());
		model.addAttribute("numFilas", numFilas);

		// Mostrar histórico
		mostrarHistorico(id, model);
		comprobarUsuario(model, indicadorDto);

		for (AtributoDto attr : listaColumnas) {
			if (attr.getMostrar() && columnasAMostrar.contains(attr.getNombre()))
				listaColumnasMostrar.add(attr);
		}
		model.addAttribute("listaColumnasTabla", listaColumnasMostrar);

		model = rellenarDatosFormularioEstilo(model);

		// Error fichero exportado
		if (error != null && !error.equals("")) {
			model.addAttribute("errorFichero", error);
		}
	}
	
	public EstiloVisualizacionTablaDto estiloInformeTabla(HttpServletRequest request,EstiloVisualizacionTablaDto estiloDefecto) throws JsonParseException, JsonMappingException, IOException {
		Double tamanhoFuente = (request.getParameter("tamanho_fuente_tabla") != null && !request.getParameter(
				"tamanho_fuente_tabla").equals("")) ? Double.valueOf(request.getParameter("tamanho_fuente_tabla")) : null;
		String tipoLetra = request.getParameter("tipo_letra_tabla");
		Integer numDecimales = (request.getParameter("decimales_tabla") != null && !request.getParameter("decimales_tabla").equals(
				"")) ? Integer.valueOf(request.getParameter("decimales_tabla")) : null;
		String tipoFecha = request.getParameter("tipo_fecha_tabla");
		
		if(tamanhoFuente == null || tamanhoFuente < 0.4){
			tamanhoFuente=estiloDefecto.getTamanhoFuente();
		}
		if(tipoLetra == null || tipoLetra.equals("")){
			tipoLetra=estiloDefecto.getTipoFuente();
		}
		if(numDecimales == null || numDecimales < 0){
			numDecimales=estiloDefecto.getDecimales();
		}
		
		// Validar nuevos datos de estilo
		if ((tamanhoFuente == null || tamanhoFuente < 0.4) || (tipoLetra == null || tipoLetra.equals(""))
				|| (numDecimales == null || numDecimales < 0) || (tipoFecha == null || tipoFecha.equals(""))) {
			return null;
		}

		EstiloVisualizacionTablaDto estilo = new EstiloVisualizacionTablaDto();
		estilo.setDecimales(numDecimales);
		estilo.setTamanhoFuente(tamanhoFuente);
		if (tipoFecha.equals(TipoFecha.CORTA.getDescripcion()))
			estilo.setTipoFecha(TipoFecha.CORTA);
		else
			estilo.setTipoFecha(TipoFecha.LARGA);
		estilo.setTipoFuente(tipoLetra);

		return estilo;
	}
	
	public EstiloVisualizacionDiagramaBarrasDto estiloInformeDiagramaBarras(HttpServletRequest request,EstiloVisualizacionDiagramaBarrasDto estiloDefecto) throws JsonParseException, JsonMappingException, IOException {
		Double tamanhoFuente = (request.getParameter("tamanho_fuente_barras") != null && !request.getParameter(
				"tamanho_fuente_barras").equals("")) ? Double.valueOf(request.getParameter("tamanho_fuente_barras")) : null;
		String tipoLetra = request.getParameter("tipo_letra_barras");
		Integer tamanho = (request.getParameter("tamanho_barras") != null && !request.getParameter("tamanho_barras").equals("")) ? Integer
				.valueOf(request.getParameter("tamanho_barras")) : null;
		String color = (request.getParameter("color") != null && !request.getParameter("color").equals("")) ? request
				.getParameter("color") : "";

		long idUsuario = getUsuarioId();
		System.out.println("id usuario logueado: " + getUsuarioId());
		if(tamanhoFuente==null || tamanhoFuente < 0.4){
			tamanhoFuente=estiloDefecto.getTamanhoFuente();
		}
		if(tipoLetra == null || tipoLetra.equals("")){
			tipoLetra=estiloDefecto.getTipoFuente();
		}
		if(tamanho == null || tamanho < 0){
			tamanho=estiloDefecto.getTamanho();
		}
		if(color == null || color.equals("")||color.equals("#fff")){
			color=estiloDefecto.getColor();
		}

		// Validar nuevos datos de estilo
		if ((tamanhoFuente == null || tamanhoFuente < 0.4) || (tipoLetra == null || tipoLetra.equals(""))
				|| (tamanho == null || tamanho < 0) || (color == null || color.equals(""))) {
			return null;
		}

		EstiloVisualizacionDiagramaBarrasDto estilo = new EstiloVisualizacionDiagramaBarrasDto();
		estilo.setTamanho(tamanho);
		estilo.setTamanhoFuente(tamanhoFuente);
		estilo.setTipoFuente(tipoLetra);
		estilo.setColor(color);
		return estilo;
	}
	
	public EstiloVisualizacionDiagramaSectoresDto estiloInformeDiagramaSectores(HttpServletRequest request,EstiloVisualizacionDiagramaSectoresDto estiloDefecto) throws JsonParseException, JsonMappingException, IOException {
		Double tamanhoFuente = (request.getParameter("tamanho_fuente_sectores") != null && !request.getParameter(
				"tamanho_fuente_sectores").equals("")) ? Double.valueOf(request.getParameter("tamanho_fuente_sectores")) : null;
		String tipoLetra = request.getParameter("tipo_letra_sectores");
		Integer diametro = (request.getParameter("diametro_sectores") != null && !request.getParameter("diametro_sectores").equals("")) ? Integer
				.valueOf(request.getParameter("diametro_sectores")) : null;
		String colores = (request.getParameter("colores_sectores") != null && !request.getParameter("colores_sectores").equals("")) ? request
				.getParameter("colores_sectores") : "";

		if(tamanhoFuente == null || tamanhoFuente < 0.4){
			tamanhoFuente=estiloDefecto.getTamanhoFuente();
		}
		if(tipoLetra == null || tipoLetra.equals("")){
			tipoLetra=estiloDefecto.getTipoFuente();
		}
		if(diametro == null || diametro < 0){
			diametro=estiloDefecto.getDiametro();
		}
		if(colores == null || colores.equals("")){
			colores=estiloDefecto.getColores();
		}

		// Validar nuevos datos de estilo
		if ((tamanhoFuente == null || tamanhoFuente < 0.4) || (tipoLetra == null || tipoLetra.equals(""))
				|| (diametro == null || diametro < 0) || (colores == null || colores.equals(""))) {
			return null;
		}

		EstiloVisualizacionDiagramaSectoresDto estilo = new EstiloVisualizacionDiagramaSectoresDto();
		estilo.setDiametro(diametro);
		estilo.setTamanhoFuente(tamanhoFuente);
		estilo.setTipoFuente(tipoLetra);
		estilo.setColores(colores);

		return estilo;
	}
	
	public List<RangosVisualizacionMapaDto> estiloInformeMapa(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		Integer numRangos = (request.getParameter("num_rangos") != null && !request.getParameter("num_rangos").equals(
				"")) ? Integer.valueOf(request.getParameter("num_rangos")) : 0;
		long idUsuario = getUsuarioId();
		for (int i = 1; i <= numRangos; i++) {
			if (request.getParameter("inicio_rango_" + i) == null && request.getParameter("fin_rango_" + i) == null
					&& request.getParameter("color_rango_" + i) == null)
				continue;

			String color = request.getParameter("color_rango_" + i);
			String inicio = request.getParameter("inicio_rango_" + i);
			String fin = request.getParameter("fin_rango_" + i);
			// Validar nuevos datos de estilo
			if ((inicio == null || inicio.equals("")) || (fin == null || fin.equals(""))
					|| (color == null || color.equals(""))) {
				return null;
			}
		}

		RangosVisualizacionMapaDto rango = null;
		List<RangosVisualizacionMapaDto> listaRangos = new ArrayList<RangosVisualizacionMapaDto>();
		for (int i = 1; i <= numRangos; i++) {
			if (request.getParameter("inicio_rango_" + i) == null && request.getParameter("fin_rango_" + i) == null
					&& request.getParameter("color_rango_" + i) == null)
				continue;

			try {
				// Tenemos un rango, asi que lo añadimos a la lista
				rango = new RangosVisualizacionMapaDto();
				rango.setColor(request.getParameter("color_rango_" + i));
				Double inicioRango = Double.valueOf(request.getParameter("inicio_rango_" + i));
				rango.setInicio(inicioRango);
				Double finRango = Double.valueOf(request.getParameter("fin_rango_" + i));
				rango.setFin(finRango);
				listaRangos.add(rango);
			} catch (Exception ex) {
				// Si alguno de lo numeros casca al convertirse o cualquier otro
				// error. Mostramos error.
				return null;
			}
		}

		return listaRangos;
	}
}