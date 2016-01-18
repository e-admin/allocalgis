/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.categoria.modelo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.cu.GestorCUCategoria;

@Controller
@RequestMapping(value="/categorias")
public class CategoriaControladorSW {

	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	private GestorCUCategoria gestorCUCategoria;

	@Autowired
	public void setGestorCUCategoria(GestorCUCategoria gestorCUCategoria) {
		this.gestorCUCategoria = gestorCUCategoria;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaCategoriasPadrePublicos" })
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPadrePublicos() {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPadrePublicos(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaCategoriasPadrePendientesDePublicos" })
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPadrePendientesDePublicos() {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPadrePendientesDePublicos(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaCategoriasPadrePendientesPublicacionWeb" })
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPadrePendientesPublicacionWeb() {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPadrePendientesPublicacionWeb(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaCategoriasPadre" })
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPadre() {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPadre(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaCategoriasPadrePublicados" })
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPadrePublicados() {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPadrePublicados(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaCategoriasPadreParaFuentes/{idFuente}" })
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPadreParaFuente(@PathVariable("idFuente") Long idFuente) {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPadreParaFuentes(idFuente), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaCategoriasPorPadrePublicos/{idPadre}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPorPadrePublicos(@PathVariable("idPadre") Long idPadre) {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPorPadrePublicos(idPadre), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaCategoriasPorPadrePublicados/{idPadre}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPorPadrePublicados(@PathVariable("idPadre") Long idPadre) {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPorPadrePublicados(idPadre), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaCategoriasPorPadrePendientesPublicacionWeb/{idPadre}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPorPadrePendientesPublicacionWeb(@PathVariable("idPadre") Long idPadre) {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPorPadrePendientesPublicacionWeb(idPadre), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaCategoriasPorPadrePendientesDePublicos/{idPadre}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPorPadrePendientesDePublicos(@PathVariable("idPadre") Long idPadre) {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPorPadrePendientesDePublicos(idPadre), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaCategoriasPorPadre/{idPadre}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPorPadre(@PathVariable("idPadre") Long idPadre) {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPorPadre(idPadre), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaCategoriasPorPadreParaFuentes/{idPadre}/{idFuente}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPorPadreParaFuente(@PathVariable("idPadre") Long idPadre,@PathVariable("idFuente") Long idFuente) {
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPorPadreParaFuentes(idPadre,idFuente), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaPorId/{id}"})
	public ResponseEntity<CategoriaDto> cargaPorId(@PathVariable("id") Long id){
		return new ResponseEntity<CategoriaDto>(gestorCUCategoria.cargaPorId(id), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/guarda/{idUsuario}"})
	public ResponseEntity<EncapsuladorPOSTSW<CategoriaDto>> guarda(@RequestBody CategoriaDto categoriaDto, @PathVariable("idUsuario")Long idUsuario){
		EncapsuladorErroresSW erroresSW = new EncapsuladorErroresSW();
		categoriaDto=gestorCUCategoria.guarda(categoriaDto, idUsuario, erroresSW);
		return new ResponseEntity<EncapsuladorPOSTSW<CategoriaDto>>(new EncapsuladorPOSTSW<CategoriaDto>(categoriaDto, erroresSW), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaCategoriasPrimerNivelUsuarioVisualizar/{idUsuario}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPrimerNivelUsuarioVisualizar(@PathVariable("idUsuario")Long idUsuario){
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPrimerNivelUsuarioVisualizar(idUsuario),responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaSubCategoriasUsuarioVisualizar/{idUsuario}/{idCategoriaPadre}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaSubCategoriasUsuarioVisualizar(@PathVariable("idUsuario")Long idUsuario,@PathVariable("idCategoriaPadre")Long idCategoriaPadre){
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaSubCategoriasUsuarioVisualizar(idUsuario, idCategoriaPadre), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaSubCategoriasUsuarioPermisosEnPadre/{idCategoriaPadre}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaSubCategoriasUsuarioPermisosEnPadre(@PathVariable("idCategoriaPadre")Long idCategoriaPadre){
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaSubCategoriasUsuarioPermisosEnPadre(idCategoriaPadre), responseHeaders, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET,value={"/borraCategoria/{idCategoria}"})
	public ResponseEntity<EncapsuladorBooleanSW> borraCategoria(@PathVariable("idCategoria") Long idCategoria){
		try {
			gestorCUCategoria.borra(idCategoria);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<EncapsuladorBooleanSW>(new EncapsuladorBooleanSW(false), responseHeaders, HttpStatus.OK);
		}
		return new ResponseEntity<EncapsuladorBooleanSW>(new EncapsuladorBooleanSW(true), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaTienePermisoUsuarioEdicionCategoria/{idCateogira}/{idUsuario}"})
	public ResponseEntity<EncapsuladorBooleanSW> cargaTienePermisoUsuarioEdicionCategoria(@PathVariable("idCateogira") Long idCategoria,@PathVariable("idUsuario") Long idUsuario){
		return new ResponseEntity<EncapsuladorBooleanSW>(new EncapsuladorBooleanSW(gestorCUCategoria.cargaTienePermisoUsuarioEdicionCategoria(idCategoria, idUsuario)), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaTienePermisoUsuarioEdicionIndicador/{idIndicador}/{idUsuario}"})
	public ResponseEntity<EncapsuladorBooleanSW> cargaTienePermisoUsuarioEdicionIndicador(@PathVariable("idIndicador") Long idIndicador,@PathVariable("idUsuario") Long idUsuario){
		return new ResponseEntity<EncapsuladorBooleanSW>(new EncapsuladorBooleanSW(gestorCUCategoria.cargaTienePermisoUsuarioEdicionIndicador(idIndicador, idUsuario)), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/cargaCategoriasPermisosEdicion/{idCateogira}/{idUsuario}"})
	public ResponseEntity<EncapsuladorListSW<CategoriaDto>> cargaCategoriasPermisosEdicion(@PathVariable("idCateogira") Long idCategoria,@PathVariable("idUsuario") Long idUsuario){
		return new ResponseEntity<EncapsuladorListSW<CategoriaDto>>(gestorCUCategoria.cargaCategoriasPermisosEdicion(idCategoria,idUsuario), responseHeaders, HttpStatus.OK);
	}
	
}
