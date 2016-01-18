/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo.impl;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;

@SuppressWarnings("unchecked")
public class IndicadorRepositorioImpl extends RepositorioBaseImpl<Indicador> implements IndicadorRepositorio {
	
	private static final String SELECT_SIN_CATEGORIA="select i from Indicador i where i.categoria is null order by i.nombre";
	private static final String SELECT_SIN_CATEGORIA_POR_FUENTE="select i from Indicador i, TablaFuenteDatos t where i.categoria is null and t.indicador.id=i.id and t.fuente.id=:idFuente order by i.nombre";
	private static final String SELECT_CON_CATEGORIA_POR_FUENTE="select i from Indicador i, TablaFuenteDatos t where i.categoria.id > 0 and t.indicador.id=i.id and t.fuente.id=:idFuente order by i.nombre";
	private static final String SELECT_BY_CATEGORIA="select i from Indicador i where i.categoria.id = :idCategoria order by i.nombre";
	private static final String SELECT_BY_CATEGORIA_POR_FUENTE="select i from Indicador i, TablaFuenteDatos t where i.categoria.id = :idCategoria and t.indicador.id=i.id and t.fuente.id=:idFuente order by i.nombre";
	
	//Indicadores con publico = 1 y pteAprobacionPublico = 1
	private static final String SELECT_SIN_CATEGORIA_PENDIENTES_DE_PUBLICO="select i from Indicador i where i.categoria is null and (publico=1 and pteAprobacionPublico=1) order by i.nombre";
	private static final String SELECT_CON_CATEGORIA_PENDIENTES_DE_PUBLICO="select i from Indicador i where i.categoria.id > 0 and (publico=1 and pteAprobacionPublico=1) order by i.nombre";
	private static final String SELECT_BY_CATEGORIA_PENDIENTES_DE_PUBLICO="select i from Indicador i where i.categoria.id = :idCategoria and (publico=1 and pteAprobacionPublico=1)  order by i.nombre";
	
	//Indicadores con publico = 1 y pteAprobacionPublico = 0
	private static final String SELECT_SIN_CATEGORIA_PUBLICOS="select i from Indicador i where i.categoria is null and (publico=1 and pteAprobacionPublico=0) order by i.nombre";
	private static final String SELECT_CON_CATEGORIA_PUBLICOS="select i from Indicador i where i.categoria.id > 0 and (publico=1 and pteAprobacionPublico=0) order by i.nombre";
	private static final String SELECT_BY_CATEGORIA_PUBLICOS="select i from Indicador i where i.categoria.id = :idCategoria and (publico=1 and pteAprobacionPublico=0)  order by i.nombre";
	
	//Indicadores con publicadoEnWeb = 1 y pteAprobacion = 0
	private static final String SELECT_SIN_CATEGORIA_PUBLICADOS="select i from Indicador i where i.categoria is null and (publicadoEnWeb=1 and pteAprobacion=0) order by i.nombre";
	private static final String SELECT_CON_CATEGORIA_PUBLICADOS="select i from Indicador i where i.categoria.id > 0 and (publicadoEnWeb=1 and pteAprobacion=0) order by i.nombre";
	private static final String SELECT_BY_CATEGORIA_PUBLICADOS="select i from Indicador i where i.categoria.id = :idCategoria and (publicadoEnWeb=1 and pteAprobacion=0)  order by i.nombre";
	
	//Indicadores con publicadoEnWeb = 0 y pteAprobacion = 1
	private static final String SELECT_SIN_CATEGORIA_PENDIENTES_PUBLICACION_WEB="select i from Indicador i where i.categoria is null and (publicadoEnWeb=0 and pteAprobacion=1) order by i.nombre";
	private static final String SELECT_CON_CATEGORIA_PENDIENTES_PUBLICACION_WEB="select i from Indicador i where i.categoria.id > 0 and (publicadoEnWeb=0 and pteAprobacion=1) order by i.nombre";
	private static final String SELECT_BY_CATEGORIA_PENDIENTES_PUBLICACION_WEB="select i from Indicador i where i.categoria.id = :idCategoria and (publicadoEnWeb=0 and pteAprobacion=1)  order by i.nombre";
	
	private static final String SELECT_SIN_CATEGORIA_PUBLICOS_PERMISOS_USER="select i from Indicador i where i.categoria is null and " +
																			"((i.publico=1 and i.pteAprobacionPublico=0) OR " +
																			"(select count(ue) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=i.id and ue.usuario.id = :idUsuario)>0 OR " +
																			"(select count(re) from RolElementoJerarquia re where re.rol.id in (:listaIdRoles) and re.elementoJerarquia.id=i.id )>0 ) " +
																			"order by i.nombre";
	
	private static final String SELECT_SIN_CATEGORIA_PERMISOS_USER_POR_FUENTE="select i from Indicador i, TablaFuenteDatos t where i.categoria is null and t.fuente.id=:idFuente and t.indicador.id=i.id and " +
			"(select count(ue) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=i.id and ue.usuario.id = :idUsuario)>0 OR " +
			"(select count(re) from RolElementoJerarquia re where re.rol.id in (:listaIdRoles) and re.elementoJerarquia.id=i.id )>0 ) AND " +
			"(select count(i2.id) from Indicador i2 where i2.categoria is null)>0 " +
			"order by i.nombre";
																	
	private static final String SELECT_IDS_ROLES_USUARIO="select ur.rol.id from UsuarioRol ur where ur.usuario.id= :idUsuario";
	
	private static final String SELECT_CON_CATEGORIA_PUBLICOS_PERMISOS_USER="select i from Indicador i where i.categoria.id =:idCategoria and " +
																			"(i.publico=1 OR " +
																			"(select count(ue) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=i.id and ue.usuario.id = :idUsuario)>0 OR " +
																			"(select count(re) from RolElementoJerarquia re where re.rol.id in (:listaIdRoles) and re.elementoJerarquia.id=i.id )>0 ) " +
																			"order by i.nombre";
	
	private static final String SELECT_CON_CATEGORIA_PERMISOS_USER="select i from Indicador i where i.categoria.id =:idCategoria and " +
																	"( " +
																	"(select count(ue) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=i.id and ue.usuario.id = :idUsuario)>0 OR " +
																	"(select count(re) from RolElementoJerarquia re where re.rol.id in (:listaIdRoles) and re.elementoJerarquia.id=i.id )>0 ) " +
																	"order by i.nombre";
	
	private static final String SELECT_INDICADORES_PUBLICOS_POR_CATEGORIA="select i from Indicador i where i.categoria.id =:idCategoria and i.publico=1 and i.pteAprobacionPublico=0 order by i.nombre";
	private static final String SELECT_INDICADORES_PUBLICOS_SIN_CATEGORIA="select i from Indicador i where i.categoria is null and i.publico=1 and i.pteAprobacionPublico=0 order by i.nombre";
	
	private static final String SELECT_INDICADORES_POR_PERMISOS_USUARIO="select i from Indicador i where " +
																		"(select count(ue) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=i.id and ue.usuario.id = :idUsuario)>0 OR " +
																		"(select count(re) from RolElementoJerarquia re where re.rol.id in (:listaIdRoles) and re.elementoJerarquia.id=i.id )>0 ) " +
																		"order by i.nombre";
	
	private static final String SELECT_BY_CATEGORIA_Y_IDS_EXCLUIDOS="select i from Indicador i where i.categoria.id =:idCategoria and i.id not in(:idsIndicadoresExcluidos) order by i.nombre";
	
	private static final String SELECT_LIKE_NOMBRE="select i from Indicador i where lower(TRANSLATE(i.nombre,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) like lower(:nombre) and i.id not in (:idsEscluidos) order by i.nombre";
	private static final String SELECT_LIKE_DESCRIPCION="select i from Indicador i where lower(TRANSLATE(i.descripcion,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) like lower(:descripcion) and i.id not in (:idsEscluidos) order by i.nombre";
	private static final String SELECT_BY_LOGINCREADOR="select i from Indicador i where i.loginCreador like :loginCreador and i.id not in (:idsEscluidos) order by i.nombre";
	
	private static final String SELET_CON_METADATOS="select m.indicador from Metadatos m where m.indicador.id not in (:idsEscluidos) order by m.indicador.nombre";
	
	
	private static final String SELECT_PUBLICOS_LIKE_NOMBRE="select i from Indicador i where i.publico=1 and i.pteAprobacionPublico=0 and lower(TRANSLATE(i.nombre,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) like lower(:nombre) and i.id not in (:idsEscluidos) order by i.nombre";
	private static final String SELECT_PUBLICOS_LIKE_DESCRIPCION="select i from Indicador i where i.publico=1 and i.pteAprobacionPublico=0 and lower(TRANSLATE(i.descripcion,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) like lower(:descripcion) and i.id not in (:idsEscluidos) order by i.nombre";
	private static final String SELECT_PUBLICOS_BY_LOGINCREADOR="select i from Indicador i where i.publico=1 and i.pteAprobacionPublico=0 and i.loginCreador like :loginCreador and i.id not in (:idsEscluidos) order by i.nombre";
	
	private static final String SELECT_PUBLICADOS_LIKE_NOMBRE="select i from Indicador i where i.publicadoEnWeb=1 and i.pteAprobacion=0 and lower(TRANSLATE(i.nombre,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) like lower(:nombre) and i.id not in (:idsEscluidos) order by i.nombre";
	private static final String SELECT_PUBLICADOS_LIKE_DESCRIPCION="select i from Indicador i where i.publicadoEnWeb=1 and i.pteAprobacion=0 and lower(TRANSLATE(i.descripcion,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) like lower(:descripcion) and i.id not in (:idsEscluidos) order by i.nombre";
	private static final String SELECT_PUBLICADOS_BY_LOGINCREADOR="select i from Indicador i where i.publicadoEnWeb=1 and i.pteAprobacion=0 and i.loginCreador like :loginCreador and i.id not in (:idsEscluidos) order by i.nombre";
	
	private static final String SELECT_PUBLICOS_CON_METADATOS="select m.indicador from Metadatos m where m.indicador.publico=1 and m.indicador.pteAprobacionPublico=0 and m.indicador.id not in (:idsEscluidos) order by m.indicador.nombre";
	
	private static final String SELECT_INDICADORES_POR_PERMISOS_USUARIO_CON_METADATOS="select m.indicador from Metadatos m where m.indicador.id not in (:idsEscluidos) and " +
																						"(select count(ue) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=m.indicador.id and ue.usuario.id = :idUsuario)>0 OR " +
																						"(select count(re) from RolElementoJerarquia re where re.rol.id in (:listaIdRoles) and re.elementoJerarquia.id=m.indicador.id )>0 ) " +
																						"order by m.indicador.nombre";
	
	
	private static final String SELECT_INDICADORES_POR_FUENTE="select t.indicador from  TablaFuenteDatos t where t.fuente.id=:idFuente";
	
	private static final String SELECT_INDICADOR_BY_NOMBRE="select i from Indicador i where lower(i.nombre)= lower(:nombre)";
	private static final String SELECT_INDICADOR_BY_NOMBRE_CON_IDINDICADOR="select i from Indicador i where lower(i.nombre)= lower(:nombre) and i.id!=:idIndicador";
	private static final String SELECT_INDICADOR_BY_NOMBRE_Y_CATEGORIA="select i from Indicador i where lower(i.nombre)= lower(:nombre) and i.categoria.id=:idCategoria";
	private static final String SELECT_INDICADOR_BY_NOMBRE_INDICADOR_Y_CATEGORIA="select i from Indicador i where lower(i.nombre)= lower(:nombre) and i.id!=:idIndicador and i.categoria.id=:idCategoria";
	
	private static final String SELECT_IDS_INDICADORES_WHERE_USER_TIENE_PERMISOS="select i.id from Indicador i where ("+
			"((select count(ue.id) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=i.id and ue.usuario.id =:idUsuario)>0 OR " +
			"(select count(re.id) from RolElementoJerarquia re where re.elementoJerarquia.id=i.id and re.rol.id in (:listaIdRoles))>0 )" +
			")"; 
	
	public List<Indicador> cargaIndicadoresSinCategoria(){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_SIN_CATEGORIA).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresSinCategoriaPorFuente(Long idFuente){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_SIN_CATEGORIA_POR_FUENTE).setParameter("idFuente", idFuente).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresConCategoriaPorFuente(Long idFuente){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_CON_CATEGORIA_POR_FUENTE).setParameter("idFuente", idFuente).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresPorCategoria(Long idCategoria){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_BY_CATEGORIA).setParameter("idCategoria", idCategoria).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresPorCategoriaPorFuente(Long idCategoria, Long idFuente){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_BY_CATEGORIA_POR_FUENTE).setParameter("idCategoria", idCategoria).setParameter("idFuente", idFuente).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresSinCategoriaPendientesDePublico(){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_SIN_CATEGORIA_PENDIENTES_DE_PUBLICO).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresSinCategoriaPendientesPublicacionWeb(){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_SIN_CATEGORIA_PENDIENTES_PUBLICACION_WEB).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresConCategoriaPendientesPublicacionWeb(){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_CON_CATEGORIA_PENDIENTES_PUBLICACION_WEB).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresConCategoriaPendientesDePublico(){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_CON_CATEGORIA_PENDIENTES_DE_PUBLICO).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresPorCategoriaPendientesDePublico(Long idCategoria){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_BY_CATEGORIA_PENDIENTES_DE_PUBLICO).setParameter("idCategoria", idCategoria).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresPorCategoriaPendientesPublicacionWeb(Long idCategoria){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_BY_CATEGORIA_PENDIENTES_PUBLICACION_WEB).setParameter("idCategoria", idCategoria).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresSinCategoriaPublicos(){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_SIN_CATEGORIA_PUBLICOS).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresConCategoriaPublicos(){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_CON_CATEGORIA_PUBLICOS).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresPorCategoriaPublicos(Long idCategoria){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_BY_CATEGORIA_PUBLICOS).setParameter("idCategoria", idCategoria).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresConCategoriaPublicados(){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_CON_CATEGORIA_PUBLICADOS).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresSinCategoriaPublicados(){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_SIN_CATEGORIA_PUBLICADOS).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresPorCategoriaPublicados(Long idCategoria){
		List<Indicador> lista = new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_BY_CATEGORIA_PUBLICADOS).setParameter("idCategoria", idCategoria).getResultList());
		return lista;
	}
	
	public List<Indicador> cargaIndicadoresSinCategoriaPublicosOPorPermisoUsuario(Long idUsuario){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_SIN_CATEGORIA_PUBLICOS_PERMISOS_USER).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles", listaIdRoles).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresSinCategoriaPorPermisoUsuarioPorFuente(Long idUsuario, Long idFuente){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_SIN_CATEGORIA_PERMISOS_USER_POR_FUENTE).setParameter("idUsuario", idUsuario).setParameter("idFuente", idFuente).setParameter("listaIdRoles", listaIdRoles).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario(Long idCategoria,Long idUsuario){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_CON_CATEGORIA_PUBLICOS_PERMISOS_USER).setParameter("listaIdRoles", listaIdRoles).setParameter("idCategoria",idCategoria).setParameter("idUsuario", idUsuario).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresConCategoriaNoPublicosPorPermisoUsuario(Long idUsuario,Long idCategoria){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_CON_CATEGORIA_PERMISOS_USER).setParameter("listaIdRoles", listaIdRoles).setParameter("idCategoria",idCategoria).setParameter("idUsuario", idUsuario).getResultList());
	}
	
	public List<Indicador> cargaIndicadorePublicosPorCategoria(Long idCategoria){
		if(idCategoria.equals(0L))
			return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_SIN_CATEGORIA_PUBLICADOS).getResultList());
		
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_BY_CATEGORIA_PUBLICADOS).setParameter("idCategoria", idCategoria).getResultList());
	}
	
	public List<Indicador> cargaIndicadorePorPermisoUsuario(Long idUsuario){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_INDICADORES_POR_PERMISOS_USUARIO).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles", listaIdRoles).getResultList());
	}
	
	public List<Indicador> cargaIndicadoresPorCategoriaYExcluidos(Long idCategoria, List<Long> idsIndicadoresExcluidos ){
		if(idsIndicadoresExcluidos.isEmpty()){
			idsIndicadoresExcluidos.add(0L);
			List<Indicador> result= new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_BY_CATEGORIA_Y_IDS_EXCLUIDOS).setParameter("idCategoria", idCategoria).setParameter("idsIndicadoresExcluidos", idsIndicadoresExcluidos).getResultList());
			idsIndicadoresExcluidos.remove(0L);
			return result;
		}
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_BY_CATEGORIA_Y_IDS_EXCLUIDOS).setParameter("idCategoria", idCategoria).setParameter("idsIndicadoresExcluidos", idsIndicadoresExcluidos).getResultList());
	}
	
	public Set<Indicador> cargaPorNombreOdescripcionOUsuarioCreador(String nombre, String descripcion, String loginCreador, List<Long> idsEscluidos){
		Set<Indicador> result= new HashSet<Indicador>();
		Boolean listaVacia;
		
		if(listaVacia=idsEscluidos.isEmpty())
			idsEscluidos.add(0L);
		
		if(!StringUtils.isBlank(nombre)){
			String param= Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
			result.addAll(new HashSet<Indicador>(getEntityManager().createQuery(SELECT_LIKE_NOMBRE).setParameter("nombre", "%"+param+"%").setParameter("idsEscluidos", idsEscluidos).getResultList()));
		}
		
		if(!StringUtils.isBlank(descripcion)){
			String param= Normalizer.normalize(descripcion, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
			result.addAll(new HashSet<Indicador>(getEntityManager().createQuery(SELECT_LIKE_DESCRIPCION).setParameter("descripcion", "%"+param+"%").setParameter("idsEscluidos", idsEscluidos).getResultList()));
		}
		
		if(!StringUtils.isBlank(loginCreador)){
			String param= Normalizer.normalize(loginCreador, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
			result.addAll(new HashSet<Indicador>(getEntityManager().createQuery(SELECT_BY_LOGINCREADOR).setParameter("loginCreador", "%"+param+"%").setParameter("idsEscluidos", idsEscluidos).getResultList()));
		}
			
		
		if(listaVacia)
			idsEscluidos.remove(0L);
		
		return result;
	}
	
	public List<Indicador> cargaConMetadosYExcluidos(List<Long> idsEscluidos){
		if (idsEscluidos.isEmpty()){
			idsEscluidos.add(0L);
			List<Indicador> result= new ArrayList<Indicador>(getEntityManager().createQuery(SELET_CON_METADATOS).setParameter("idsEscluidos", idsEscluidos).getResultList());
			idsEscluidos.remove(0L);
			return result;
		}
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELET_CON_METADATOS).setParameter("idsEscluidos", idsEscluidos).getResultList());
	}
	
	public Set<Indicador> cargaPublicosPorNombreOdescripcionOUsuarioCreador(String nombre, String descripcion, String loginCreador, List<Long> idsEscluidos){
		Set<Indicador> result= new HashSet<Indicador>();
		Boolean listaVacia;
		
		if(listaVacia=idsEscluidos.isEmpty())
			idsEscluidos.add(0L);
		
		if(!StringUtils.isBlank(nombre)){
			String param= Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
			result.addAll(new HashSet<Indicador>(getEntityManager().createQuery(SELECT_PUBLICOS_LIKE_NOMBRE).setParameter("nombre", "%"+param+"%").setParameter("idsEscluidos", idsEscluidos).getResultList()));
		}
		
		if(!StringUtils.isBlank(descripcion)){
			String param= Normalizer.normalize(descripcion, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
			result.addAll(new HashSet<Indicador>(getEntityManager().createQuery(SELECT_PUBLICOS_LIKE_DESCRIPCION).setParameter("descripcion", "%"+param+"%").setParameter("idsEscluidos", idsEscluidos).getResultList()));
		}
		
		if(!StringUtils.isBlank(loginCreador)){
			String param= Normalizer.normalize(loginCreador, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
			result.addAll(new HashSet<Indicador>(getEntityManager().createQuery(SELECT_PUBLICOS_BY_LOGINCREADOR).setParameter("loginCreador", "%"+param+"%").setParameter("idsEscluidos", idsEscluidos).getResultList()));
		}
		if(listaVacia)
			idsEscluidos.remove(0L);
		
		return result;
	}
	
	public Set<Indicador> cargaPublicadosPorNombreOdescripcionOUsuarioCreador(String nombre, String descripcion, String loginCreador, List<Long> idsEscluidos){
		Set<Indicador> result= new HashSet<Indicador>();
		Boolean listaVacia;
		
		if(listaVacia=idsEscluidos.isEmpty())
			idsEscluidos.add(0L);
		
		if(!StringUtils.isBlank(nombre)){
			String param= Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
			result.addAll(new HashSet<Indicador>(getEntityManager().createQuery(SELECT_PUBLICADOS_LIKE_NOMBRE).setParameter("nombre", "%"+param+"%").setParameter("idsEscluidos", idsEscluidos).getResultList()));
		}
		if(!StringUtils.isBlank(descripcion)){
			String param= Normalizer.normalize(descripcion, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
			result.addAll(new HashSet<Indicador>(getEntityManager().createQuery(SELECT_PUBLICADOS_LIKE_DESCRIPCION).setParameter("descripcion", "%"+param+"%").setParameter("idsEscluidos", idsEscluidos).getResultList()));
		}
		
		if(!StringUtils.isBlank(loginCreador)){
			String param= Normalizer.normalize(loginCreador, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
			result.addAll(new HashSet<Indicador>(getEntityManager().createQuery(SELECT_PUBLICADOS_BY_LOGINCREADOR).setParameter("loginCreador", "%"+param+"%").setParameter("idsEscluidos", idsEscluidos).getResultList()));
		}
		if(listaVacia)
			idsEscluidos.remove(0L);
		
		return result;
	}
	
	public List<Indicador> cargaPublicosConMetadosYExcluidos(List<Long> idsEscluidos){
		if (idsEscluidos.isEmpty()){
			idsEscluidos.add(0L);
			List<Indicador> result= new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_PUBLICOS_CON_METADATOS).setParameter("idsEscluidos", idsEscluidos).getResultList());
			idsEscluidos.remove(0L);
			return result;
		}
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_PUBLICOS_CON_METADATOS).setParameter("idsEscluidos", idsEscluidos).getResultList());
	}
	
	public List<Indicador> cargaPermisosUsuarioConMetadatosYExcluidos(List<Long> idsEscluidos,Long idUsuario){
		
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		
		if (idsEscluidos.isEmpty()){
			idsEscluidos.add(0L);
			List<Indicador> result= new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_INDICADORES_POR_PERMISOS_USUARIO_CON_METADATOS).setParameter("idsEscluidos", idsEscluidos).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles", listaIdRoles).getResultList());
			idsEscluidos.remove(0L);
			return result;
		}
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_INDICADORES_POR_PERMISOS_USUARIO_CON_METADATOS).setParameter("idsEscluidos", idsEscluidos).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles", listaIdRoles).getResultList());
		
	}
	
	public List<Indicador> cargaIndicadoresPorFuente(Long idFuente){
		return new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_INDICADORES_POR_FUENTE).setParameter("idFuente", idFuente).getResultList());
	}
	
	/**Devuelve un indicador por nombre si existe en BD*/
	public Indicador cargarIndicadorPorNombre(String nombre, Long idCategoria, Long idIndicador){
		String consulta = "";
		if (idCategoria == null || idCategoria == 0){
			if (idIndicador != null && idIndicador > 0){
				consulta = SELECT_INDICADOR_BY_NOMBRE_CON_IDINDICADOR;
				for (Indicador temp:new HashSet<Indicador>(getEntityManager().createQuery(consulta).setParameter("nombre", nombre).setParameter("idIndicador", idIndicador).getResultList()))
					return temp;
				return null;
				
			}
			else{
				consulta= SELECT_INDICADOR_BY_NOMBRE;
				for (Indicador temp:new HashSet<Indicador>(getEntityManager().createQuery(consulta).setParameter("nombre", nombre).getResultList()))
					return temp;
				return null;
			}
		}
		else{
			if (idIndicador != null && idIndicador >0){
				consulta = SELECT_INDICADOR_BY_NOMBRE_INDICADOR_Y_CATEGORIA;
				for (Indicador temp:new HashSet<Indicador>(getEntityManager().createQuery(consulta).setParameter("nombre", nombre).setParameter("idIndicador", idIndicador).setParameter("idCategoria", idCategoria).getResultList()))
					return temp;
				return null;
			}
			else{
				consulta = SELECT_INDICADOR_BY_NOMBRE_Y_CATEGORIA;
				for (Indicador temp:new HashSet<Indicador>(getEntityManager().createQuery(consulta).setParameter("nombre", nombre).setParameter("idCategoria", idCategoria).getResultList()))
					return temp;
				return null;
			}
		}
	}
	
	/**Devuelve los ids de los indicadores sobre los que el usuario tiene permisos*/
	public List<Long> cargarIdsIndicadoresUsuarioPermisos(Long idUsuario){
		List<Long> listaIdRoles = new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if (listaIdRoles.isEmpty()){
			listaIdRoles.add(0L);
		}
		return new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_INDICADORES_WHERE_USER_TIENE_PERMISOS).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles", listaIdRoles).getResultList());
	}
}
