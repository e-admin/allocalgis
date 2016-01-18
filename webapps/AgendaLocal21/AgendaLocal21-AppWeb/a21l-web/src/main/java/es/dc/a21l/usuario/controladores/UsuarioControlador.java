/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.controladores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.dc.a21l.base.cu.ResultadosOperaciones;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorIntegerSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.comun.controladores.GenericAbstractController;
import es.dc.a21l.comun.utils.UrlConstructorSW;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.modelo.web.base.UsuarioDetalles;
import es.dc.a21l.usuario.cu.RolDto;
import es.dc.a21l.usuario.cu.UsuarioDto;
import es.dc.a21l.usuario.cu.UsuarioElementoJerarquiaDto;
import es.dc.a21l.usuario.cu.UsuarioRolDto;
import es.dc.a21l.usuario.cu.impl.UsuarioDtoFormErrorsEmun;




@Controller
@RequestMapping(value="/usuarios.htm")
public class UsuarioControlador extends GenericAbstractController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        // true passed to CustomDateEditor constructor means convert empty String to null
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	public String cargaListaUsuarios(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		model.addAttribute("lateralUsuario", true);
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		
		request.setAttribute("atributo1", "nombre atributo");
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("usuarios");
		
		EncapsuladorListSW<UsuarioDto> listaUsuarios=new EncapsuladorListSW<UsuarioDto>();
		if(getUserAdmin()){
			url.setParametroCadena("listaTodosUsuarios");
			ResponseEntity<EncapsuladorListSW> respuestaListaUsuarios=getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class);
			listaUsuarios.addAll(listaUsuarios=respuestaListaUsuarios.getBody());
		}else{
			url.setParametroCadena("usuario").setParametroCadena(getServicioSeguridad().getUserDetails().getId());
			listaUsuarios.add(getRestTemplate().getForEntity(url.getUrl(), UsuarioDto.class).getBody());
		}
		
		url.borrarParametrosCadena().setParametroCadena("usuarioRoles").setParametroCadena("cargaPorUsuario");
		Map<Long, String> mapaRolesUsuario= new HashMap<Long, String>();
		EncapsuladorListSW<UsuarioRolDto> listaUsuarioRol;
		String cadenaListaRol;
		for(UsuarioDto temp:listaUsuarios){
			cadenaListaRol="";
			url.setParametroCadena(temp.getId());
			ResponseEntity<EncapsuladorListSW> respuesta= getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class);
			listaUsuarioRol=respuesta.getBody();
			url.borraUltimoParametroCadena();
			for(UsuarioRolDto tempUr:listaUsuarioRol)
				cadenaListaRol=cadenaListaRol+tempUr.getRolDto().getNombre()+", ";
			if(!cadenaListaRol.isEmpty())
				cadenaListaRol=cadenaListaRol.substring(0, cadenaListaRol.length()-2);
			mapaRolesUsuario.put(temp.getId(), cadenaListaRol);
		}
		
		model.addAttribute("listaUsuarios", listaUsuarios);
		model.addAttribute("mapaRolesUsuario", mapaRolesUsuario);
		return "usuariosTile";		
	}
	
	@RequestMapping(method = RequestMethod.GET, params = {"accion=modificarUsuario" })
	public String modificarUsuario(@RequestParam(value="id") Long id,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		model.addAttribute("lateralUsuario", true);
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		model.addAttribute("idUsuarioActual",getServicioSeguridad().getUserDetails().getId());
		model.addAttribute("usuarioDto",cargaUsuarioPorIdSW(id));
		return "usuarioMoficicarTile";
	}
	
	
	@RequestMapping(method = RequestMethod.POST, params ={"accion=guardarModificacionUsuario"})
	public String guardarModificacionUsuario(@ModelAttribute UsuarioDto usuarioDto, BindingResult result, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception{
		model.addAttribute("lateralUsuario", true);
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		
		model.addAttribute("idUsuarioActual",getServicioSeguridad().getUserDetails().getId());
		if(result.hasErrors())
			return "usuarioMoficicarTile";
		
		UsuarioDto usuarioDto2;
		usuarioDto2=cargaUsuarioPorIdSW(usuarioDto.getId());
		
		usuarioDto2.setNombre(usuarioDto.getNombre());
		usuarioDto2.setDescripcion(usuarioDto.getDescripcion());
		usuarioDto2.setEmail(usuarioDto.getEmail());
		usuarioDto2.setEsAdmin(usuarioDto.getEsAdmin());
		
		usuarioDto = guardarUsuarioSW(usuarioDto2, result);
		
		if(result.hasErrors()){
			model.addAttribute("usuarioDto", usuarioDto2);
			model.addAttribute(getPathPaqueteValidacion()+"usuarioDto", result);
			return "usuarioMoficicarTile";
		}
		
		usuarioDto.setResultadoOperacion(ResultadosOperaciones.EXITO_GUARDAR);
		
		model.addAttribute("usuarioDto", usuarioDto);
		return cargaListaUsuarios(model, request, response);
		//return "usuarioMoficicarTile";
	}
	
	
	@RequestMapping(method = RequestMethod.GET, params = {"accion=restablecerContrasenha" })
	public String restablecerContrasenha(@RequestParam(value="id") Long id,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		model.addAttribute("lateralUsuario", true);
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		if(id.equals(getServicioSeguridad().getUserDetails().getId())){
			model.addAttribute("msgRestPropiaPass", true);
			return cargaListaUsuarios(model, request, response);
		}
		model.addAttribute("usuarioDto",cargaUsuarioPorIdSW(id));
		
		return "usuarioRestablecerContrasenhaTile";
	}
	
	@RequestMapping(method = RequestMethod.POST, params ={"accion=guardarRestContrasenhaUsuario"})
	public String guardarRestContrasenhaUsuario(@ModelAttribute UsuarioDto usuarioDto, BindingResult result, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception{
		model.addAttribute("lateralUsuario", true);
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		if(usuarioDto.getId()==getServicioSeguridad().getUserDetails().getId()){
			model.addAttribute("msgRestPropiaPass", true);
			return cargaListaUsuarios(model, request, response);
		}
		if(result.hasErrors())
			return "usuarioRestablecerContrasenhaTile";
		
		if(!usuarioDto.getPassword().equals(usuarioDto.getPasswordConfirm())){
			result.rejectValue(UsuarioDtoFormErrorsEmun.CONTRASENHASDISITNTAS.getAtributo(), UsuarioDtoFormErrorsEmun.CONTRASENHASDISITNTAS.getCadenaCodigoError());
			return "usuarioRestablecerContrasenhaTile";
		}
		
		UsuarioDto usuarioDto2;
		usuarioDto2=cargaUsuarioPorIdSW(usuarioDto.getId());
		
		usuarioDto2.setPassword(usuarioDto.getPassword());
		usuarioDto2.setPasswordConfirm(usuarioDto.getPasswordConfirm());
		
		usuarioDto=guardarUsuarioSW(usuarioDto2, result);
		
		model.addAttribute("usuarioDto", usuarioDto2);
		model.addAttribute(getPathPaqueteValidacion()+"usuarioDto", result);
		model.addAttribute("passRestablecida", true);
		return cargaListaUsuarios(model, request, response);
		//return "usuarioRestablecerContrasenhaTile";
	}
	
	@RequestMapping(method = RequestMethod.GET, params = {"accion=crearUsuario" })
	public String crearUsuario(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		model.addAttribute("lateralUsuario", true);
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		model.addAttribute("usuarioDto", new UsuarioDto());
		return "usuarioTile";
	}
	
	@RequestMapping(method=RequestMethod.POST,params={"accion=guardarUsuario"})
	public String guardarUsuario(@ModelAttribute UsuarioDto usuarioDto,BindingResult result, Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		model.addAttribute("lateralUsuario", true);
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		
		if(result.hasErrors()){
			model.addAttribute("usuarioDto", usuarioDto);
			return "usuarioTile";
		}
		
		UsuarioDto usuarioDto2;
		usuarioDto2=guardarUsuarioSW(usuarioDto, result);
		
		if(result.hasErrors()){
			model.addAttribute("usuarioDto", usuarioDto);
			return "usuarioTile";
		}
		usuarioDto2.setResultadoOperacion(ResultadosOperaciones.EXITO_CREAR);
		model.addAttribute("usuarioDto", usuarioDto2);
		return cargaListaUsuarios(model, request, response);
		//return "usuarioTile";
	}
	
	@RequestMapping(method=RequestMethod.GET,params={"accion=eliminarUsuario"})
	public String eliminarUsuario(@RequestParam(value="id") Long id, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception{
		model.addAttribute("lateralUsuario", true);
		if(!getUserAdmin())
			return irSinPermisos(request, response);
		if(id.equals(getServicioSeguridad().getUserDetails().getId())){
			model.addAttribute("msgDelPropioUser", true);
			return cargaListaUsuarios(model, request, response);
		}
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("usuarios").setParametroCadena("eliminarUsuario").setParametroCadena(id);
		//Forma general de borrar
//		getRestTemplate().delete(url.getUrl());
		ResponseEntity<EncapsuladorIntegerSW> respuesta=getRestTemplate().getForEntity(url.getUrl(),EncapsuladorIntegerSW.class);
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
		return cargaListaUsuarios(model, request, response);
	}
	
	@RequestMapping(method=RequestMethod.GET,params={"accion=cambiarContrasenha"})
	public String cambiarContrasenha(Model model,HttpServletRequest request){
		model.addAttribute("usuarioDto", cargaUsuarioPorIdSW(getServicioSeguridad().getUserDetails().getId()));
		String referer = request.getHeader("referer");
		model.addAttribute("urlOrigen", referer);
		return "usuarioCambiarContrasenhaTile";
	}
	
	@RequestMapping(method=RequestMethod.POST,params={"accion=guardarContrasenhaNueva"})
	public String guardarContrasenhaNueva(@ModelAttribute UsuarioDto usuarioDto,BindingResult result,HttpServletRequest request,Model model){
		String referer = usuarioDto.getReferer();
		
		if(result.hasErrors()){
			model.addAttribute("usuarioDto", usuarioDto);
			model.addAttribute("urlOrigen", referer);
			return "usuarioCambiarContrasenhaTile";
		}
		UsuarioDetalles usuarioDetalles=getServicioSeguridad().getUserDetails();
		
		if(StringUtils.isBlank(usuarioDto.getPasswordOld()) || !usuarioDetalles.getPassword().equals(usuarioDto.getPasswordOld())){
			result.rejectValue(UsuarioDtoFormErrorsEmun.CONTRASENHA_ANTIGUA_DISTINTA.getAtributo(), UsuarioDtoFormErrorsEmun.CONTRASENHA_ANTIGUA_DISTINTA.getCadenaCodigoError());
		}
		if(!usuarioDto.getPassword().equals(usuarioDto.getPasswordConfirm())){
			result.rejectValue(UsuarioDtoFormErrorsEmun.CONTRASENHASDISITNTAS.getAtributo(), UsuarioDtoFormErrorsEmun.CONTRASENHASDISITNTAS.getCadenaCodigoError());
		}
		
		if(result.hasErrors()){
			model.addAttribute("usuarioDto", usuarioDto);
			model.addAttribute("urlOrigen", referer);
			return "usuarioCambiarContrasenhaTile";
		}
		
		UsuarioDto usuarioDto2=cargaUsuarioPorIdSW(usuarioDto.getId());
		usuarioDto2.setPassword(usuarioDto.getPassword());
		usuarioDto2.setPasswordConfirm(usuarioDto.getPasswordConfirm());
		
		usuarioDto= guardarUsuarioSWParaCambioPass(usuarioDto2, result);
		
		if(result.hasErrors()){
			model.addAttribute("usuarioDto", usuarioDto2);
			model.addAttribute(getPathPaqueteValidacion()+"usuarioDto", result);
			model.addAttribute("urlOrigen", referer);
			return "usuarioCambiarContrasenhaTile";
		}
		
		cambiarContrasenhaContextoSeguridad(request, usuarioDto.getPassword());
		usuarioDto.setResultadoOperacion(ResultadosOperaciones.EXITO_GUARDAR);
		model.addAttribute("usuarioDto", usuarioDto);
		guardaEnSesion(request, "pass_cambiada", "correcto");
		return "redirect:"+referer;
		//return "usuarioCambiarContrasenhaTile";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,params={"accion=asignaRoles"})
	public String asignaRoles(@RequestParam(value="id") Long id,Model model,HttpServletRequest request){
		model.addAttribute("lateralUsuario", true);
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("roles").setParametroCadena("cargaRolesSinAsignarUsuario").setParametroCadena(id);
		List<RolDto> listaRolesNoUsuario= getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();
		
		url.borraUltimoParametroCadena().borraUltimoParametroCadena();
		url.setParametroCadena("cargaRolesPorUsuario").setParametroCadena(id);
		List<RolDto> listaRolesUsuario= getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();
		
		url.borrarParametrosCadena();
		url.setParametroCadena("usuarios").setParametroCadena("usuario").setParametroCadena(id);
		UsuarioDto usuarioDto= getRestTemplate().getForEntity(url.getUrl(), UsuarioDto.class).getBody();

		
		model.addAttribute("listaRolesUsuario", listaRolesUsuario);
		model.addAttribute("listaRolesNoUsuario", listaRolesNoUsuario);
		model.addAttribute("usuarioDto", usuarioDto);
	
		return "usuarioAsignarRolesTile";
		
	}
	
	@RequestMapping(method=RequestMethod.POST,params={"accion=guardaRoles"})
	public String guardaRoles (@ModelAttribute UsuarioDto usuarioDto,BindingResult result, HttpServletRequest request,Model model, HttpServletResponse response) throws Exception{
		model.addAttribute("lateralUsuario", false);
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("usuarioRoles").setParametroCadena("actualizaListaRolesUsuario").setParametroCadena(usuarioDto.getId());
		EncapsuladorListSW<Long> encapsuladorListSW= new EncapsuladorListSW<Long>();
		encapsuladorListSW.setLista(usuarioDto.getRoles());
		getRestTemplate().postForEntity(url.getUrl(), new HttpEntity<EncapsuladorListSW<Long>>(encapsuladorListSW), EncapsuladorBooleanSW.class);
		model.addAttribute(ResultadosOperaciones.EXITO_ASIGNAR_ROLES.toString(), true);
		return cargaListaUsuarios(model, request, response);
	}
	
	/**Muestra la pantalla de gestión de permisos sobre categorías e indicadores que tiene un usuario */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method=RequestMethod.GET,params={"accion=gestionarPermisos"})
	public String gestionarPermisos(@RequestParam(value="id") Long id,Model model,HttpServletRequest request) {
		model.addAttribute("lateralUsuario", true);
		UsuarioDto usuario = cargaUsuarioPorIdSW(id);
				 
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("roles").setParametroCadena("cargaRolesPorUsuario").setParametroCadena(id);
		List<RolDto> listaRolesUsuario= getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();
		Map<Long, List<String>> mapaEltosRol = new HashMap<Long, List<String>>();
		for (RolDto rolDto : listaRolesUsuario) {
			url.borraUltimoParametroCadena().borraUltimoParametroCadena();
			url.setParametroCadena("rol").setParametroCadena(rolDto.getId());
			ResponseEntity<RolDto> respuesta= getRestTemplate().getForEntity(url.getUrl(), RolDto.class);
			RolDto rolCompleto = respuesta.getBody();
			for (Long i : rolCompleto.getEltosJerarquia()) {
				if (mapaEltosRol.containsKey(i)){
					List<String> eltos = mapaEltosRol.get(i);
					eltos.add(", "+rolCompleto.getNombre());
					mapaEltosRol.put(i, eltos);
				}
				else{
					List<String> eltos = new ArrayList<String>();
					eltos.add(rolCompleto.getNombre());
					mapaEltosRol.put(i, eltos);
				}
			}
		}
		usuario.setRoles(new ArrayList(mapaEltosRol.keySet()));
		
		url.borrarParametrosCadena();
		url.setParametroCadena("usuarioPermisos").setParametroCadena("cargarPermisosPorUsuario").setParametroCadena(id);
		List<UsuarioElementoJerarquiaDto> listaPermisosUsuario = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();
		for (UsuarioElementoJerarquiaDto usuarioElementoJerarquiaDto : listaPermisosUsuario) {
			usuario.getEltosJerarquia().add(usuarioElementoJerarquiaDto.getElementoJerarquiaDto().getId());
		}
		cargaTablaElementosJerarquia(model, request);
		cargarPermisos(model);
		model.addAttribute("usuarioDto", usuario);
		model.addAttribute("mapaEltosRol", mapaEltosRol);
		return "usuarioAsignarPermisosTile";
	}
	
	public void cargaTablaElementosJerarquia(Model model, HttpServletRequest request) {
		
		
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadores = new HashMap<Long, List<IndicadorDto>>();
		obtenerMapasIndicadoresCategorias(mapaCategorias, mapaIndicadores);
		model.addAttribute("mapaCategoriasTabla", mapaCategorias);
		model.addAttribute("mapaIndicadoresTabla", mapaIndicadores);

	}
	
	
	/** Almacenamiento elementos sobre los que un usuario tiene permiso en BD*/
	@RequestMapping(method=RequestMethod.POST,params={"accion=guardarPermisos"})
	public String guardarPermisos(@ModelAttribute UsuarioDto usuarioDto, BindingResult result, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		model.addAttribute("lateralUsuario", false);
		
		
		//Se recupera toda la jerarquia de Categorias e Indicadores
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadores = new HashMap<Long, List<IndicadorDto>>();
		obtenerMapasIndicadoresCategorias(mapaCategorias, mapaIndicadores);
		
		String[] idsPermisosStrings = ((String[]) request.getParameterMap().get("eltosSeleccionados"));
		
		/* En este punto se dispone de todos los identificadores que se han seleccionado para un usuario concreto.
		 * Hasta el momento unicamente se asignaban los checks seleccionados en la vista a la lista de Long que tiene el objeto UsuarioDTO
		 * A partir de ahora si lo que se selecciona es una categoría, se le dará permiso a TODOS los elementos que cuelguen de ella (indicadores y categorias)
		 * y se incorporarán a la lista de Long del usuario
		 */
		if (idsPermisosStrings != null) {
			for (String idString : idsPermisosStrings) {
				List<Long> listaDescendientesCategorias = new ArrayList<Long>();
				List<Long> listaDescendientesIndicadores = new ArrayList<Long>();
				Long idElemento = Long.valueOf(idString);
				//Comprobar si el elemento es una categoria o un INDICADOR
				if (idElemento!=null && idElemento!=0L) {
					
					
					boolean esCategoria = existeElementoEnMapa(idElemento, true, mapaCategorias, null);
					if (esCategoria) {
						//Obtener Categoria
						UrlConstructorSW urlCategoria = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("categorias").setParametroCadena("cargaPorId");
						urlCategoria.setParametroCadena(idElemento);
						CategoriaDto categoria = getRestTemplate().getForEntity(urlCategoria.getUrl(),CategoriaDto.class).getBody();
						
						obtenerListaDescendientesCategorias(listaDescendientesCategorias,categoria);
						obtenerListaDescendientesIndicadores(listaDescendientesIndicadores,categoria);
						cargarElementosJerarquiaUsuario(listaDescendientesCategorias,usuarioDto.getEltosJerarquia());
						cargarElementosJerarquiaUsuario(listaDescendientesIndicadores,usuarioDto.getEltosJerarquia());
					//ES UN INDICADOR
					}
					boolean esIndicador = existeElementoEnMapa(idElemento, false, null, mapaIndicadores);
					if (esIndicador) {
						UrlConstructorSW urlIndicador = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("indicadores").setParametroCadena("cargaPorId");
						urlIndicador.setParametroCadena(idElemento);
						IndicadorDto indicadorDto = getRestTemplate().getForEntity(urlIndicador.getUrl(), IndicadorDto.class).getBody();
						cargarElementoJerarquiaUsuario(usuarioDto.getEltosJerarquia(), indicadorDto);
					}
				}
				//usuarioDto.getEltosJerarquia().add(new Long(idString.trim()));
			}
		}
		
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("usuarioPermisos").setParametroCadena("actualizarListaPermisosUsuario").setParametroCadena(usuarioDto.getId());
		EncapsuladorListSW<Long> encapsuladorListSW= new EncapsuladorListSW<Long>();
		encapsuladorListSW.setLista(usuarioDto.getEltosJerarquia());
		getRestTemplate().postForEntity(url.getUrl(), new HttpEntity<EncapsuladorListSW<Long>>(encapsuladorListSW), EncapsuladorBooleanSW.class);
		model.addAttribute(ResultadosOperaciones.EXITO_ASIGNAR_PERMISOS.toString(), true);
		return cargaListaUsuarios(model, request, response);
	}

	/*
	 * Método que carga toda la jerarquia de categorias e indicadores y los mantiene en memoria para poder trabajar la lista de EltosJerarquia
	 * del usuario (no se puede acceder a la BBDD si la categoria o el indicador no existe, lanza exception
	 */
	private void obtenerMapasIndicadoresCategorias(Map<Long, List<CategoriaDto>> mapaCategorias,Map<Long, List<IndicadorDto>> mapaIndicadores) {
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
	}

	private void cargarElementoJerarquiaUsuario(List<Long> listaElementosUsuario, IndicadorDto indicadorDto) {
		if (!listaElementosUsuario.contains(indicadorDto.getId())) {
			listaElementosUsuario.add(indicadorDto.getId());
		}
	}

	private void cargarElementosJerarquiaUsuario(List<Long> listaDescendientesCategorias, List<Long> listaElementosUsuario) {
		for (Long elemento : listaDescendientesCategorias) {							
			if (!listaElementosUsuario.contains(elemento)) {
				listaElementosUsuario.add(elemento);
			}
		}
	}
	
	
	/*
	 * Método encargado de obtener de una CATEGORIA todas sus categorias descendientes
	 */
	private void obtenerListaDescendientesCategorias (List<Long> listaDescendientesCategorias, CategoriaDto categoria) {
		
		Long identificador = categoria.getId();
		
		//Incorporo la categoría padre (de la que se quiere comprobar si tiene hijos)
		listaDescendientesCategorias.add(identificador);
		
		@SuppressWarnings("unchecked")
		List<CategoriaDto> listaCategoriasHijo = obtenerCategoriasHijo(identificador);
		for (CategoriaDto objCategoriaHija : listaCategoriasHijo) {			
			obtenerListaDescendientesCategorias (listaDescendientesCategorias,objCategoriaHija);
		}
	}
	
	/*
	 * Método encargado de obtener de una CATEGORIA todas sus categorias descendientes
	 */
	private void obtenerListaDescendientesIndicadores (List<Long> listaDescendientesIndicadores, CategoriaDto categoria) {
		
		Long idCategoriaPadre = categoria.getId();
		
		List<CategoriaDto> listaCategoriasHijo = obtenerCategoriasHijo(idCategoriaPadre);
		List<IndicadorDto> listaIndicadoresHijo = obtenerIndicadoresHijo(idCategoriaPadre);
		
		List<Long> listaIndicadoresHijoL = obtenerListaIndicadoresLong(listaIndicadoresHijo);
		listaDescendientesIndicadores.addAll(listaIndicadoresHijoL);
		
		for (CategoriaDto objCategoriaHija : listaCategoriasHijo) {			
			obtenerListaDescendientesIndicadores (listaDescendientesIndicadores,objCategoriaHija);
		}
	}

	/*
	 * Método que obtiene la lista de indicadores hijo del identificador de categoria
	 * pasado como argumento 
	 */
	private List<IndicadorDto> obtenerIndicadoresHijo(Long idCategoriaPadre) {
		UrlConstructorSW urlIndicadoresHijos = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("indicadores").setParametroCadena("cargaPorCategoria");
		urlIndicadoresHijos.setParametroCadena(idCategoriaPadre);		
		@SuppressWarnings("unchecked")
		List<IndicadorDto> listaIndicadoresHijo = getRestTemplate().getForEntity(urlIndicadoresHijos.getUrl(), EncapsuladorListSW.class).getBody();
		return listaIndicadoresHijo;
	}

	/*
	 * Método que obtiene la lista de categorias hija del identificador de categoria
	 * pasado como argumento
	 */
	private List<CategoriaDto> obtenerCategoriasHijo(Long idCategoriaPadre) {
		UrlConstructorSW urlCategoriasHijas = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPorPadre");
		urlCategoriasHijas.setParametroCadena(idCategoriaPadre);
		@SuppressWarnings("unchecked")
		List<CategoriaDto> listaCategoriasHijo = getRestTemplate().getForEntity(urlCategoriasHijas.getUrl(), EncapsuladorListSW.class).getBody();
		return listaCategoriasHijo;
	}

	@SuppressWarnings("rawtypes")
	private UsuarioDto guardarUsuarioSWParaCambioPass(UsuarioDto usuarioDto,BindingResult result){
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("usuarios").setParametroCadena("guardaUsuarioPass");
		ResponseEntity<EncapsuladorPOSTSW> respuestaPost = getRestTemplate().postForEntity(url.getUrl(),  new HttpEntity<UsuarioDto>(usuarioDto, getHeaders()), EncapsuladorPOSTSW.class);
		
		//Se evaluan errores en el formulario
		if(respuestaPost.getBody().hashErrors()){
			escribirErrores(result, respuestaPost.getBody());
		}
		return (UsuarioDto)respuestaPost.getBody().getObjetoEncapsulado();
	}
	
	@SuppressWarnings("rawtypes")
	private UsuarioDto guardarUsuarioSW(UsuarioDto usuarioDto,BindingResult result){
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("usuarios").setParametroCadena("guardaUsuario");
		ResponseEntity<EncapsuladorPOSTSW> respuestaPost = getRestTemplate().postForEntity(url.getUrl(),  new HttpEntity<UsuarioDto>(usuarioDto, getHeaders()), EncapsuladorPOSTSW.class);
		
		//Se evaluan errores en el formulario
		if(respuestaPost.getBody().hashErrors()){
			escribirErrores(result, respuestaPost.getBody());
		}
		return (UsuarioDto)respuestaPost.getBody().getObjetoEncapsulado();
	}
	
	private UsuarioDto cargaUsuarioPorIdSW(Long id){
		UrlConstructorSW url= new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("usuarios").setParametroCadena("usuario").setParametroCadena(id);
		ResponseEntity<UsuarioDto> respuesta=getRestTemplate().getForEntity(url.getUrl(), UsuarioDto.class);
		return respuesta.getBody();
	}
	
	

	
	private void cambiarContrasenhaContextoSeguridad(HttpServletRequest request,String password){
		SecurityContext contextoSeguridad=SecurityContextHolder.getContext();
		Authentication authentication = contextoSeguridad.getAuthentication();
		if(authentication instanceof UsernamePasswordAuthenticationToken){
			UsuarioDetalles usuarioDetalles=(UsuarioDetalles)authentication.getPrincipal();
			usuarioDetalles.setPassword(password);
		}
	}
	
	@ModelAttribute("usuarioAdmin")
	public Boolean getUserAdmin(){
		return getServicioSeguridad().getUserDetails().getEsAdmin();
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
}
