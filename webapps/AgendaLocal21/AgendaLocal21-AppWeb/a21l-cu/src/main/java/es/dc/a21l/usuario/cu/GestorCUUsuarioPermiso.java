/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu;

import java.util.List;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;


public interface GestorCUUsuarioPermiso {
	public EncapsuladorListSW<UsuarioElementoJerarquiaDto> cargarPermisosPorUsuario(Long idUsuario);
	public void actualizarListaPermisosUsuario(Long idUsuario, List<Long> listaEltosJerarquia);
	public Boolean cargaEsUsuarioConPermisosVerCategoria(Long idCategoria, UsuarioDto usuarioDto);
	public Boolean cargaEsUsuarioConPermisosEdicionIndicador(Long idCategoria, UsuarioDto usuarioDto);
	public Boolean cargaUsuarioConPermisosVerIndicador(Long idIndicador,UsuarioDto usuarioDto);
	public Boolean guardaPermisoUsuario(Long idUsuario, Long idIndicador);
	public Boolean esUsuarioConPermisosSobreIndicador(Long idIndicador,UsuarioDto usuarioDto);
}
