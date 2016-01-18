/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu;

import java.util.List;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;

public interface GestorCUCategoria {
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadre();
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadrePublicados();
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadreParaFuentes(Long idFuente);
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadrePublicos();
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadrePendientesDePublicos();
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadrePendientesPublicacionWeb();
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadre(Long idCategoriaPadre);
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadreParaFuentes(Long idCategoriaPadre, Long idFuente);
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadrePublicos(Long idCategoriaPadre);
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadrePublicados(Long idCategoriaPadre);
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadrePendientesPublicacionWeb(Long idCategoriaPadre);
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadrePendientesDePublicos(Long idCategoriaPadre);
	public CategoriaDto cargaPorId(Long id);
	public CategoriaDto guarda(CategoriaDto categoriaDto, Long idUsuario, EncapsuladorErroresSW erroresSW);
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPrimerNivelUsuarioVisualizar(Long idUsuario);
	public EncapsuladorListSW<CategoriaDto> cargaSubCategoriasUsuarioVisualizar(Long idUsuario,Long idCategoriaPadre);
	public EncapsuladorListSW<CategoriaDto> cargaSubCategoriasUsuarioPermisosEnPadre(Long idCategoriaPadre);
	public void borra(Long idCategoria);
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPermisosUsuario(Long idUsuario);
	public List<CategoriaDto> cargaPorNombreLike(String nombreCategoria);
	public List<Long> cargaIdsCategoriasUsuarioPermisos(Long idUsuario);
	public Boolean cargaTienePermisoUsuarioEdicionCategoria(Long idCategoria, Long idUsuario);
	public Boolean cargaTienePermisoUsuarioEdicionIndicador(Long idIndicador, Long idUsuario);
	public EncapsuladorListSW<CategoriaDto> cargaTodas();
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPermisosEdicion(Long idCategoriaPadre, Long idUsuario);
	public EncapsuladorListSW<CategoriaDto> cargaTodas2PrimerosNiveles();
}
