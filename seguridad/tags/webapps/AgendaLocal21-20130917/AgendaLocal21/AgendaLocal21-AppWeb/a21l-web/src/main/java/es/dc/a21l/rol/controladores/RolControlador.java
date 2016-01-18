/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.rol.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.dc.a21l.base.cu.ResultadosOperaciones;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorIntegerSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.comun.controladores.GenericAbstractController;
import es.dc.a21l.comun.utils.UrlConstructorSW;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.usuario.cu.RolDto;
import es.dc.a21l.usuario.cu.UsuarioDto;

@Controller
@RequestMapping(value="/roles.htm")
public class RolControlador extends GenericAbstractController {
	
	/** Visualización listado de roles
	 * @throws Exception */
	@RequestMapping(method = RequestMethod.GET)
	public String cargaListaRoles(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("roles").setParametroCadena("cargaTodos");
		model.addAttribute("listaRoles", getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody());
		return "rolesTile";
	}
	
	/** Visualización formulario alta rol*/
	@RequestMapping(method = RequestMethod.GET, params = {"accion=crearRol" })
	public String crearRol(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		cargaTablaElementosJerarquia(model, request);
		cargarPermisos(model);
		RolDto rolDto=new RolDto();
		model.addAttribute("rolDto",rolDto );
		model.addAttribute("listaEJRol","1");
		return "rolTile";
	}

	/** Almacenamiento datos rol en BD*/
	@RequestMapping(method=RequestMethod.POST,params={"accion=guardarRol"})
	public String guardarRol(@ModelAttribute RolDto rolDto, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String[] idsPermisosStrings = ((String[]) request.getParameterMap().get("eltosSeleccionados"));
		if (idsPermisosStrings != null) {
			for (String idString : idsPermisosStrings) {
				rolDto.getEltosJerarquia().add(new Long(idString.trim()));			
			}
		}
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		
		if(result.hasErrors()){
			model.addAttribute("listaEJRol",getListaEJRol(rolDto));
			model.addAttribute("rolDto", rolDto);
			return "rolTile";
		}
		
		RolDto rolResultDto = guardarRolSW(rolDto, result);
		if(result.hasErrors()){
			cargaTablaElementosJerarquia(model, request);
			cargarPermisos(model);
			model.addAttribute("listaEJRol",getListaEJRol(rolDto));
			model.addAttribute("rolDto", rolDto);
			return "rolTile";
		}
		if(rolDto.getId()==0L){
			rolResultDto.setResultadoOperacion(ResultadosOperaciones.EXITO_CREAR);
		}else{
			rolResultDto.setResultadoOperacion(ResultadosOperaciones.EXITO_GUARDAR);
		}
		cargarPermisos(model);
		model.addAttribute("listaEJRol",getListaEJRol(rolResultDto));
		model.addAttribute("rolDto", rolResultDto );
		return cargaListaRoles(model, request, response);
		//return "rolTile";
	}
	
	/** Visualización datos rol*/
	@RequestMapping(method = RequestMethod.GET, params = {"accion=mostrarDetallesRol" })
	public String mostrarDetallesRol(@RequestParam(value="id") Long id,Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		cargaTablaElementosJerarquia(model, request);
		cargarPermisos(model);
		RolDto rolDto=cargarRolPorId(id);
		model.addAttribute("rolDto",rolDto);
		model.addAttribute("listaEJRol",getListaEJRol(rolDto));
		return "rolDetalleTile";
	}
	public void cargaTablaElementosJerarquia(Model model, HttpServletRequest request) {
		
		
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadores = new HashMap<Long, List<IndicadorDto>>();
		// Se recuperan las categorías e indicadores raíz
		UrlConstructorSW urlCat = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPadre");
		UrlConstructorSW urlInd = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("indicadores").setParametroCadena("cargaSinCategoria");
		
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

		urlCat.borraUltimoParametroCadena().setParametroCadena("cargaCategoriasPorPadre");
		urlInd.borraUltimoParametroCadena().setParametroCadena("cargaPorCategoria");
	
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
		ordenarElementosJerarquiaCategorias(mapaCategorias);
		ordenarElementosJerarquiaIndicadores(mapaIndicadores);
		model.addAttribute("mapaCategoriasTabla", mapaCategorias);
		model.addAttribute("mapaIndicadoresTabla", mapaIndicadores);

	}
	
	/** Modificación datos de un rol*/
	@RequestMapping(method = RequestMethod.POST, params ={"accion=modificarRol"})
	public String modificarRol(@ModelAttribute RolDto rolDto, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String[] idsPermisosStrings = ((String[]) request.getParameterMap().get("eltosSeleccionados"));
		if (idsPermisosStrings != null) {
			for (String idString : idsPermisosStrings) {
				rolDto.getEltosJerarquia().add(new Long(idString.trim()));			
			}
		}
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		
		if(result.hasErrors()){
			model.addAttribute("listaEJRol",getListaEJRol(rolDto));
			return "rolDetalleTile";
		}
		
		 RolDto rolResultDto = cargarRolPorId(rolDto.getId());
		
		rolResultDto.setNombre(rolDto.getNombre());
		rolResultDto.setDescripcion(rolDto.getDescripcion());
		rolResultDto.setEltosJerarquia(rolDto.getEltosJerarquia());
		
		rolDto = guardarRolSW(rolResultDto, result); 
		
		if(result.hasErrors()){
			model.addAttribute("rolDto", rolResultDto);
			cargaTablaElementosJerarquia(model, request);
			cargarPermisos(model);
			model.addAttribute("listaEJRol",getListaEJRol(rolResultDto));
			model.addAttribute(getPathPaqueteValidacion()+"rolDto", result);
			
			return "rolDetalleTile";
		}
		
		cargarPermisos(model);
		rolDto.setResultadoOperacion(ResultadosOperaciones.EXITO_GUARDAR);
		model.addAttribute("rolDto", rolDto);
		model.addAttribute("listaEJRol",getListaEJRol(rolDto));
		return cargaListaRoles(model, request, response);
		//return "rolDetalleTile";
	}
	
	/** Borrado de un rol*/
	@RequestMapping(method=RequestMethod.GET,params={"accion=eliminarRol"})
	public String eliminarRol(@RequestParam(value="id") Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("roles").setParametroCadena("eliminarRol").setParametroCadena(id);
		ResponseEntity<EncapsuladorIntegerSW> respuesta = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorIntegerSW.class);
		if(hasErrorsSW(respuesta))
			irPaginaErrorSW();
		
		if(respuesta.getBody().getValor().equals(1)){
			model.addAttribute("resultadoOperacion", ResultadosOperaciones.EXITO_BORRAR.getResultado());
		}
		if(respuesta.getBody().getValor().equals(0)){
			model.addAttribute("resultadoOperacion", ResultadosOperaciones.ERROR_BORRAR.getResultado());
		}
		if(respuesta.getBody().getValor().equals(-1)){
			model.addAttribute("resultadoOperacion", ResultadosOperaciones.ENTIDAD_UTILIZADA.getResultado());
		}
		return cargaListaRoles(model, request,response);
	}
	
	/** SW Almacenamiento datos rol en BD*/
	@SuppressWarnings("unchecked")
	private RolDto guardarRolSW(RolDto rolDto, BindingResult result){
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("roles").setParametroCadena("guardarRol");
		EncapsuladorPOSTSW<RolDto> respuestaPost= getRestTemplate().postForEntity(url.getUrl(), new HttpEntity<RolDto>(rolDto),EncapsuladorPOSTSW.class).getBody();
		
		if(respuestaPost.hashErrors())
			escribirErrores(result, respuestaPost);
		return respuestaPost.getObjetoEncapsulado();
	}

	/** SW Devuelve los datos asociados a un rol a partir de su identificador*/
	private RolDto cargarRolPorId(Long id){
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("roles").setParametroCadena("rol").setParametroCadena(id);
		ResponseEntity<RolDto> respuesta = getRestTemplate().getForEntity(url.getUrl(), RolDto.class);
		return respuesta.getBody();
	}
	
	/**Carga en el modelo las listas que contienen la jerarquía de elementos (categorías e indicadores) */
	@SuppressWarnings("unchecked")
	private void cargarPermisos(Model model) {
		UrlConstructorSW urlCategorias= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPadre");
		UrlConstructorSW urlIndicadores= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("indicadores").setParametroCadena("cargaSinCategoria");
		
		List<CategoriaDto> listaCategoriasRaiz=getRestTemplate().getForEntity(urlCategorias.getUrl(), EncapsuladorListSW.class).getBody();
		List<IndicadorDto> listaIndicadoresRaiz= getRestTemplate().getForEntity(urlIndicadores.getUrl(), EncapsuladorListSW.class).getBody();
		
		Map<Long, List<CategoriaDto>> mapaCategoriasSegundoNivel=new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadoresSegundoNivel= new HashMap<Long, List<IndicadorDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadoresTercerNivel= new HashMap<Long, List<IndicadorDto>>();
		
		urlCategorias.borraUltimoParametroCadena().setParametroCadena("cargaCategoriasPorPadre");
		urlIndicadores.borraUltimoParametroCadena().setParametroCadena("cargaPorCategoria");
		
		List<CategoriaDto> listaCategoriasSegundonivel;
		for(CategoriaDto temp:listaCategoriasRaiz){
			urlCategorias.setParametroCadena(temp.getId());
			urlIndicadores.setParametroCadena(temp.getId());
			
			listaCategoriasSegundonivel=getRestTemplate().getForEntity(urlCategorias.getUrl(), EncapsuladorListSW.class).getBody();
			mapaCategoriasSegundoNivel.put(temp.getId(),listaCategoriasSegundonivel);
			
			mapaIndicadoresSegundoNivel.put(temp.getId(), getRestTemplate().getForEntity(urlIndicadores.getUrl(), EncapsuladorListSW.class).getBody());
		
			urlCategorias.borraUltimoParametroCadena();
			urlIndicadores.borraUltimoParametroCadena();
			
			for(CategoriaDto temp2:listaCategoriasSegundonivel){
				urlIndicadores.setParametroCadena(temp2.getId());
				mapaIndicadoresTercerNivel.put(temp2.getId(), getRestTemplate().getForEntity(urlIndicadores.getUrl(), EncapsuladorListSW.class).getBody());
				urlIndicadores.borraUltimoParametroCadena();
			}
		}
		
		model.addAttribute("listaCategoriasRaiz", listaCategoriasRaiz);
		model.addAttribute("listaIndicadoresRaiz", listaIndicadoresRaiz);
		model.addAttribute("mapaCategoriasSegundoNivel", mapaCategoriasSegundoNivel);
		model.addAttribute("mapaIndicadoresSegundoNivel", mapaIndicadoresSegundoNivel);
		model.addAttribute("mapaIndicadoresTercerNivel", mapaIndicadoresTercerNivel);
	}
	
	@ModelAttribute("usuarioAdmin")
	public Boolean getUserAdmin() {
		Long id=getServicioSeguridad().getUserDetails().getId();
		UsuarioDto usuario=cargaUsuarioPorIdSW(id);
		if(usuario.getEsAdmin()!=getServicioSeguridad().getUserDetails().getEsAdmin()){
			getServicioSeguridad().getUserDetails().setEsAdmin(usuario.getEsAdmin());
		}
		return usuario.getEsAdmin();
	}
	
	private String getListaEJRol(RolDto rolDto){
		if(rolDto==null || rolDto.getEltosJerarquia()==null)
			return "";
		String result="";
		for(Long temp:rolDto.getEltosJerarquia())
			result+=temp+",";
		
		if(!result.isEmpty())
			result=result.substring(0, result.length()-1);
		return result;
	}
	
}
