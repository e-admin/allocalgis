/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo;

import java.util.List;
import java.util.Set;

import es.dc.a21l.base.modelo.RepositorioBase;

public interface IndicadorRepositorio extends RepositorioBase<Indicador> {
	public List<Indicador> cargaIndicadoresSinCategoriaPendientesDePublico();
	public List<Indicador> cargaIndicadoresSinCategoriaPendientesPublicacionWeb();
	public List<Indicador> cargaIndicadoresConCategoriaPendientesPublicacionWeb();
	public List<Indicador> cargaIndicadoresPorCategoriaPendientesDePublico(Long idCategoria);
	public List<Indicador> cargaIndicadoresPorCategoriaPendientesPublicacionWeb(Long idCategoria);
	public List<Indicador> cargaIndicadoresConCategoriaPublicos();
	public List<Indicador> cargaIndicadoresSinCategoriaPublicos();
	public List<Indicador> cargaIndicadoresPorCategoriaPublicos(Long idCategoria);
	public List<Indicador> cargaIndicadoresConCategoriaPublicados();
	public List<Indicador> cargaIndicadoresSinCategoriaPublicados();
	public List<Indicador> cargaIndicadoresPorCategoriaPublicados(Long idCategoria);
	public List<Indicador> cargaIndicadoresSinCategoria();
	public List<Indicador> cargaIndicadoresSinCategoriaPorFuente(Long idFuente);
	public List<Indicador> cargaIndicadoresConCategoriaPorFuente(Long idFuente);
	public List<Indicador> cargaIndicadoresPorCategoria(Long idCategoria);
	public List<Indicador> cargaIndicadoresPorCategoriaPorFuente(Long idCategoria, Long idFuente);
	public List<Indicador> cargaIndicadoresSinCategoriaPublicosOPorPermisoUsuario(Long idUsuario);
	public List<Indicador> cargaIndicadoresSinCategoriaPorPermisoUsuarioPorFuente(Long idUsuario, Long idFuente);
	public List<Indicador> cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario(Long idCategoria,Long idUsuario);
	public List<Indicador> cargaIndicadoresConCategoriaNoPublicosPorPermisoUsuario(Long idUsuario,Long idCategoria);
	public List<Indicador> cargaIndicadorePublicosPorCategoria(Long idCategoria);
	public List<Indicador> cargaIndicadorePorPermisoUsuario(Long idUsuario);
	public List<Indicador> cargaIndicadoresPorCategoriaYExcluidos(Long idCategoria, List<Long> idsIndicadoresExcluidos );
	public List<Indicador> cargaIndicadoresConCategoriaPendientesDePublico();
	public Set<Indicador> cargaPorNombreOdescripcionOUsuarioCreador(String nombre, String descripcion, String loginCreador, List<Long> idsEscluidos);
	public List<Indicador> cargaConMetadosYExcluidos(List<Long> idsEscluidos);
	public Set<Indicador> cargaPublicosPorNombreOdescripcionOUsuarioCreador(String nombre, String descripcion, String loginCreador, List<Long> idsEscluidos);
	public Set<Indicador> cargaPublicadosPorNombreOdescripcionOUsuarioCreador(String nombre, String descripcion, String loginCreador, List<Long> idsEscluidos);
	public List<Indicador> cargaPublicosConMetadosYExcluidos(List<Long> idsEscluidos);
	public List<Indicador> cargaPermisosUsuarioConMetadatosYExcluidos(List<Long> idsEscluidos,Long idUsuario);
	public List<Indicador> cargaIndicadoresPorFuente(Long idFuente);
	public Indicador cargarIndicadorPorNombre(String nombre, Long idCategoria, Long idIndicador);
	public List<Long> cargarIdsIndicadoresUsuarioPermisos(Long idUsuario);
}
