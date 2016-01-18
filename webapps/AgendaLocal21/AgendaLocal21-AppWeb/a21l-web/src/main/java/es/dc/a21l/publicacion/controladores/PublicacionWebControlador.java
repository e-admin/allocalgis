/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */

package es.dc.a21l.publicacion.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.comun.controladores.GenericAbstractController;
import es.dc.a21l.comun.utils.UrlConstructorSW;
import es.dc.a21l.comun.utils.UtilEncriptar;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.publicacion.cu.PublicacionDto;

/**
 * 
 * @author Balidea Consulting & Programming
 */
@Controller
@RequestMapping("/publicacionWeb.htm")
public class PublicacionWebControlador extends GenericAbstractController  {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(PublicacionWebControlador.class);

	private Boolean usuarioInvitado(){
		return getServicioSeguridad().getUserDetails().getInvitado();	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	public String mostrarPublicacionWeb(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		if(usuarioInvitado())
		{
			return irSinPermisos(request, response);
		}
		
		if(!getServicioSeguridad().getUserDetails().getEsAdmin())
		{
			return irSinPermisos(request, response);
		}
		
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadores = new HashMap<Long, List<IndicadorDto>>();
		
		// Se recuperan las categorías e indicadores raíz
		UrlConstructorSW urlCat = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPadrePublicados");
		UrlConstructorSW urlInd = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores").setParametroCadena("cargaSinCategoriaPublicados");
		
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

		urlCat.borraUltimoParametroCadena().setParametroCadena("cargaCategoriasPorPadrePublicados");
		urlInd.borraUltimoParametroCadena().setParametroCadena("cargaPorCategoriaPublicados");
	
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
				
		//Recorro los indicadores para calcular su Url, indicar su numero de peticiones y el login del ultimo en pedir.
		Iterator<Entry<Long, List<IndicadorDto>>> iteradorMapaInd = mapaIndicadores.entrySet().iterator();
		while (iteradorMapaInd.hasNext()) 
		{
			List<IndicadorDto> listaIndicadoresMapa = (List<IndicadorDto>)(iteradorMapaInd.next()).getValue();
			
			if (listaIndicadoresMapa != null && listaIndicadoresMapa.size() > 0)
			{
				for (IndicadorDto indicadorActual: listaIndicadoresMapa)
				{
					String urlTmp = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/"+"indicadorPublico.htm?id=" + UtilEncriptar.encripta(String.valueOf(indicadorActual.getId()));
					indicadorActual.setUrlPublicacion(urlTmp);
					indicadorActual.setUrlPublicacionCorta(UtilEncriptar.encripta(String.valueOf(indicadorActual.getId())));
				}
			}
		}
	
		model.addAttribute("mapaCategoriasTabla",mapaCategorias);
		model.addAttribute("mapaIndicadoresTabla",mapaIndicadores);
		
		//Comprobamos el estado de la publicacion!
		urlCat = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		urlCat.setParametroCadena("publicacion").setParametroCadena("obtenerEstado");
		
		PublicacionDto publi = getRestTemplate().getForEntity(urlCat.getUrl(), PublicacionDto.class).getBody();
		
		//SI la publicacion no existe o no esta habilitada => no esta habilitada
		if ( publi == null || publi.getPublicacionWebHabilitada() == null || !publi.getPublicacionWebHabilitada()) 
		{
			model.addAttribute("estadoPublicacion", false);
		} 
		else 
		{
			model.addAttribute("estadoPublicacion", publi.getPublicacionWebHabilitada());
		}
		
		return "publicacionWebTile";
	}	
	
	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(method=RequestMethod.GET,params={"accion=habilitarPublicacionWeb"})
	public String habilitarPublicacionWeb(Model model,HttpServletRequest request, HttpServletResponse response ) throws Exception {
		if(usuarioInvitado())
			return irSinPermisos(request, response);
		if(!getServicioSeguridad().getUserDetails().getEsAdmin())
			return irSinPermisos(request, response);
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("publicacion");
		url.setParametroCadena("guardaPublicacion");
		
		PublicacionDto publi = new PublicacionDto();
		publi.setPublicacionWebHabilitada(true);
		
		EncapsuladorPOSTSW<PublicacionDto> respuestaPublicacion = getRestTemplate().postForEntity(url.getUrl(),new HttpEntity<PublicacionDto>(publi),EncapsuladorPOSTSW.class).getBody();
		return mostrarPublicacionWeb(model, request, response);
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(method=RequestMethod.GET,params={"accion=deshabilitarPublicacionWeb"})
	public String deshabilitarPublicacionWeb(Model model,HttpServletRequest request, HttpServletResponse response ) throws Exception {
		if(usuarioInvitado())
			return irSinPermisos(request, response);
		if(!getServicioSeguridad().getUserDetails().getEsAdmin())
			return irSinPermisos(request, response);
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("publicacion");
		url.setParametroCadena("guardaPublicacion");
		
		PublicacionDto publi = new PublicacionDto();
		publi.setPublicacionWebHabilitada(false);
		
		EncapsuladorPOSTSW<PublicacionDto> respuestaPublicacion = getRestTemplate().postForEntity(url.getUrl(),new HttpEntity<PublicacionDto>(publi),EncapsuladorPOSTSW.class).getBody();
		return mostrarPublicacionWeb(model, request, response);	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET, params={"accion=listaIndicadoresPendientes"})
	public String mostrarListaIndicadoresPendientes(Model model,HttpServletRequest request, HttpServletResponse response ) throws Exception 
	{
		if(usuarioInvitado())
		{
			return irSinPermisos(request, response);
		}
		
		if(!getServicioSeguridad().getUserDetails().getEsAdmin()) 
		{
			return irSinPermisos(request, response);
		}
		
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadores = new HashMap<Long, List<IndicadorDto>>();

		// Se recuperan las categorías e indicadores raíz
		UrlConstructorSW urlCat = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPadrePendientesPublicacionWeb");
		UrlConstructorSW urlInd = new UrlConstructorSWImpl(getUrlBaseSw()).setParametroCadena("indicadores").setParametroCadena("cargaSinCategoriaPendientesPublicacionWeb");
		
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

		urlCat.borraUltimoParametroCadena().setParametroCadena("cargaCategoriasPorPadrePendientesPublicacionWeb");
		urlInd.borraUltimoParametroCadena().setParametroCadena("cargaPorCategoriaPendientesPublicacionWeb");
	
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
		
		return "listaIndicadoresPendientesWebTile";
	}
	
	@RequestMapping(method = RequestMethod.GET, params={"accion=autorizarIndicadorPublicacionWeb"})
	public String autorizarIndicador(@RequestParam(value="id") Long id, Model model,HttpServletRequest request, HttpServletResponse response ) throws Exception {
		if(usuarioInvitado())
			return irSinPermisos(request, response);
		if(!getServicioSeguridad().getUserDetails().getEsAdmin())
			return irSinPermisos(request, response);
		
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(id).getUrl(),IndicadorDto.class).getBody();
		
		String asuntoCorreo = obtenerMensajeLocal("correo.asunto.autorizar.publicacion.web",null);
		String textoCorreo = obtenerMensajeLocal("correo.texto.autorizar.publicacion.web",indicadorDto.getNombre());
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("autorizarIndicadorPublicacionWeb");
		url.setParametroCadena(id);
		url.setParametroCadena(asuntoCorreo);
		url.setParametroCadena(textoCorreo);
		url.setParametroCadena(getServicioSeguridad().getUserDetails().getId());
		
		EncapsuladorBooleanSW resultado= getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class).getBody();
		
		if ( resultado.getValorLogico())		
			model.addAttribute("resultadoOperacion", "jsp.publicacion.indicador.autorizado.web");
		else
			model.addAttribute("resultadoOperacionError", "jsp.publicacion.indicador.autorizado.web.error");
		
		return mostrarListaIndicadoresPendientes(model, request, response); 
	}
	
	@RequestMapping(method = RequestMethod.GET, params={"accion=descartarIndicadorPublicacionWeb"})
	public String descartarIndicador(@RequestParam(value="id") Long id, Model model,HttpServletRequest request, HttpServletResponse response ) throws Exception {
		if(usuarioInvitado())
			return irSinPermisos(request, response);
		if(!getServicioSeguridad().getUserDetails().getEsAdmin())
			return irSinPermisos(request, response);
		
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(id).getUrl(),IndicadorDto.class).getBody();
		
		String asuntoCorreo = obtenerMensajeLocal("correo.asunto.descartar.publicacion.web",null);
		String textoCorreo = obtenerMensajeLocal("correo.texto.descartar.publicacion.web",indicadorDto.getNombre());
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("descartarIndicadorPublicacionWeb");
		url.setParametroCadena(id);
		url.setParametroCadena(asuntoCorreo);
		url.setParametroCadena(textoCorreo);
		url.setParametroCadena(getServicioSeguridad().getUserDetails().getId());
		
		
		EncapsuladorBooleanSW resultado= getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class).getBody();
		
		if ( resultado.getValorLogico())		
			model.addAttribute("resultadoOperacion", "jsp.publicacion.indicador.descartado.web");
		else
			model.addAttribute("resultadoOperacionError", "jsp.publicacion.indicador.descartado.web.error");
		
		return mostrarListaIndicadoresPendientes(model, request, response); 
	}
	
	@RequestMapping(method = RequestMethod.GET, params={"accion=retirarIndicadorPublicacionWeb"})
	public String retirarIndicador(@RequestParam(value="id") Long id, Model model,HttpServletRequest request, HttpServletResponse response ) throws Exception {
		if(usuarioInvitado())
			return irSinPermisos(request, response);
		
		IndicadorDto indicadorDto = getRestTemplate().getForEntity(new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("indicadores").setParametroCadena("cargaPorId").setParametroCadena(id).getUrl(),IndicadorDto.class).getBody();
		
		String asuntoCorreo = obtenerMensajeLocal("correo.asunto.retirar.publicacion.web",null);
		String textoCorreo = obtenerMensajeLocal("correo.texto.retirar.publicacion.web",indicadorDto.getNombre());
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("indicadores");
		url.setParametroCadena("retirarIndicadorPublicacionWeb");
		url.setParametroCadena(id);
		url.setParametroCadena(asuntoCorreo);
		url.setParametroCadena(textoCorreo);
		url.setParametroCadena(getServicioSeguridad().getUserDetails().getId());
		
		EncapsuladorBooleanSW resultado= getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class).getBody();
		
		if ( resultado.getValorLogico())		
			model.addAttribute("resultadoOperacion", "jsp.publicacion.indicador.retirado.web");
		else
			model.addAttribute("resultadoOperacionError", "jsp.publicacion.indicador.retirado.web.error");
		
		return mostrarPublicacionWeb(model, request, response); 
	}
	
	protected String getUrlBaseSw() {
		return getServicioConfiguracionGeneral().getUrlBaseSW();
	}
}