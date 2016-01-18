/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo;

import java.util.List;

import es.dc.a21l.base.modelo.RepositorioBase;
import es.dc.a21l.fuente.modelo.Fuente;

public interface CategoriaRepositorio extends RepositorioBase<Categoria> {
	
	public List<Categoria> cargaCategoriasPadre();
	public List<Categoria> cargaCategoriasPadrePublicados();
	public List<Categoria> cargaCategoriasPadreParaFuentes(Long idFuente);
	public List<Categoria> cargaCategoriasPorPadre(Long idCategoriaPadre);
	public List<Categoria> cargaCategoriasPorPadreParaFuentes(Long idCategoriaPadre, Long idFuente);
	public List<Categoria> cargaCategoriasPadrePublicos();
	public List<Categoria> cargaCategoriasPorPadrePublicos(Long idCategoriaPadre);
	public List<Categoria> cargaCategoriasPorPadrePublicados(Long idCategoriaPadre);
	public List<Categoria> cargaCategoriasPadrePendientesDePublicos();
	public List<Categoria> cargaCategoriasPadrePendientesPublicacionWeb();
	public List<Categoria> cargaCategoriasPorPadrePendientesDePublicos(Long idCategoriaPadre);
	public List<Categoria> cargaCategoriasPorPadrePendientesPublicacionWeb(Long idCategoriaPadre);
	public List<Long> cargaIdsCategoriasPrimerNivelUsuarioPermisos(Long idUsuario);
	public List<Categoria> cargaCategoriasPrimerNivelUsuarioVisualizar(List<Long> listaIdCategoriasPrimerNivelPermiso,Long idUsuario);
	public List<Long> cargaIdsSubCategoriasUsuarioPermisos(Long idUsuario,Long idCategoriaPadre);
	public List<Categoria> cargaSubCategoriasUsuarioVisualizar(List<Long> listaIdsSubCategoriasPermiso,Long idUsuario,Long idCategoriaPadre);
	public List<Categoria> cargaCategoriasPermisosUsuario(Long idUsuario);
	public List<Categoria> cargaPorNombreLike(String nombreCategoria);
	public List<Long> cargaIdsCategoriasUsuarioPermisos(Long idUsuario);
	public Categoria cargaCategoriaPorNombre(String nombre, Long idCatPadre, Long idCategoria);
	public List<Categoria> cargaTodas();
}
