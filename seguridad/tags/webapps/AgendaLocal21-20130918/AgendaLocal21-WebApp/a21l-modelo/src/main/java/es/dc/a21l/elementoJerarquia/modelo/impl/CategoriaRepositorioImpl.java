/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo.impl;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.elementoJerarquia.modelo.Categoria;
import es.dc.a21l.elementoJerarquia.modelo.CategoriaRepositorio;
import es.dc.a21l.fuente.modelo.Fuente;

@SuppressWarnings("unchecked")
public class CategoriaRepositorioImpl extends RepositorioBaseImpl<Categoria> implements CategoriaRepositorio {
	private static final String SELECT_CATEGORIA_BY_NOMBRE_Y_CAT_PADRE="select c from Categoria c where lower(c.nombre)= lower(:nombre) and c.categoriaPadre.id = :idCatPadre";
	private static final String SELECT_CATEGORIA_BY_NOMBRE_Y_CAT_PADRE_NULL="select c from Categoria c where lower(c.nombre)= lower(:nombre) and c.categoriaPadre.id is null";
	private static final String SELECT_CATEGORIA_BY_NOMBRE_Y_CAT_PADRE_CON_CAT="select c from Categoria c where lower(c.nombre)= lower(:nombre) and c.categoriaPadre.id = :idCatPadre and c.id!=:idCat";
	private static final String SELECT_CATEGORIA_BY_NOMBRE_Y_CAT_PADRE_NULL_CON_CAT="select c from Categoria c where lower(c.nombre)= lower(:nombre) and c.categoriaPadre.id is null and c.id!=:idCat";
	
	private static final String SELECT_CATEGORIAS_PADRE_PARA_FUENTE=
								//"select c from Categoria c where c.categoriaPadre is null and (select count(i.id) from Indicador i, TablaFuenteDatos t where i.categoria.id = c.id and t.fuente.id=:idFuente and t.indicador.id=i.id)>0 order by c.nombre";
								"select c from Categoria c where c.categoriaPadre is null and "+
								"((select count(i.id) from Indicador i, TablaFuenteDatos t where i.categoria.id = c.id and t.fuente.id=:idFuente and t.indicador.id=i.id)>0 "+
								"or ((select count(c2.id) from Categoria c2, Indicador i2, TablaFuenteDatos t2 where t2.fuente.id=:idFuente and t2.indicador.id=i2.id and c2.categoriaPadre.id = c.id and i2.categoria.id = c2.id and c2.id=i2.categoria.id)>0)) " +
								"order by c.nombre";
	private static final String SELECT_CATEGORIAS_BY_PADRE_PARA_FUENTE=
								//"select c from Categoria c where c.categoriaPadre.id = :idCategoriaPadre and ((select count(i.id) from Indicador i, TablaFuenteDatos t where i.categoria.id = c.id and t.fuente.id=:idFuente and t.indicador.id=i.id)>0 or (select count(c2.id) from Categoria c2 where c2.categoriaPadre.id = c.id)>0) order by c.nombre";
								"select c from Categoria c where c.categoriaPadre.id = :idCategoriaPadre and " +
								"((select count(i.id) from Indicador i, TablaFuenteDatos t where i.categoria = c.id and t.fuente.id=:idFuente and t.indicador.id=i.id)>0 ) order by c.nombre";
	
	private static final String SELECT_CATEGORIAS_PADRE="select c from Categoria c where c.categoriaPadre is null order by c.nombre";
	private static final String SELECT_CATEGORIAS_BY_PADRE="select c from Categoria c where c.categoriaPadre.id = :idCategoriaPadre order by c.nombre";
	
	private static final String SELECT_CATEGORIAS_PADRE_PUBLICADOS="select c from Categoria c where c.categoriaPadre is null and "+
									"((select count(i.id) from Indicador i where i.categoria.id = c.id and i.publicadoEnWeb=1 and i.pteAprobacion=0)>0 "+
									"or ((select count(c2.id) from Categoria c2, Indicador i2 where c2.categoriaPadre.id = c.id and i2.categoria.id = c2.id and i2.publicadoEnWeb=1 and i2.pteAprobacion=0 and c2.id=i2.categoria.id)>0)) " +
									"order by c.nombre";

	
	private static final String SELECT_CATEGORIAS_BY_PADRE_PUBLICADOS="select c from Categoria c where c.categoriaPadre.id = :idCategoriaPadre and ((select count(i.id) from Indicador i where i.categoria = c.id and i.publicadoEnWeb=1 and i.pteAprobacion=0)>0 ) order by c.nombre";
	
	private static final String SELECT_CATEGORIAS_PADRE_PUBLICOS="select c from Categoria c where c.categoriaPadre is null and "+
									"((select count(i.id) from Indicador i where i.categoria.id = c.id and i.publico=1 and i.pteAprobacionPublico=0)>0 "+
									"or ((select count(c2.id) from Categoria c2, Indicador i2 where c2.categoriaPadre.id = c.id and i2.categoria.id = c2.id and i2.publico=1 and i2.pteAprobacionPublico=0 and c2.id=i2.categoria.id)>0)) " +
									"order by c.nombre";
	
	private static final String SELECT_CATEGORIAS_BY_PADRE_PUBLICOS="select c from Categoria c where c.categoriaPadre.id = :idCategoriaPadre and ((select count(i.id) from Indicador i where i.categoria = c.id and i.publico=1 and i.pteAprobacionPublico=0)>0 ) order by c.nombre";
	
	private static final String SELECT_CATEGORIAS_PADRE_PENDIENTES_DE_PUBLICOS="select c from Categoria c where c.categoriaPadre is null and "+
									"((select count(i.id) from Indicador i where i.categoria.id = c.id and i.publico=1 and i.pteAprobacionPublico=1)>0 "+
									"or ((select count(c2.id) from Categoria c2, Indicador i2 where c2.categoriaPadre.id = c.id and i2.categoria.id = c2.id and i2.publico=1 and i2.pteAprobacionPublico=1 and c2.id=i2.categoria.id)>0)) " +
									"order by c.nombre";
	
	private static final String SELECT_CATEGORIAS_BY_PADRE_PENDIENTES_DE_PUBLICOS="select c from Categoria c where c.categoriaPadre.id = :idCategoriaPadre and ((select count(i.id) from Indicador i where i.categoria = c.id and i.publico=1 and i.pteAprobacionPublico=1)>0 ) order by c.nombre";
	
	private static final String SELECT_CATEGORIAS_PADRE_PENDIENTES_PUBLICACION_WEB="select c from Categoria c where c.categoriaPadre is null and "+
									"((select count(i.id) from Indicador i where i.categoria.id = c.id and i.publicadoEnWeb=0 and i.pteAprobacion=1)>0 "+
									"or ((select count(c2.id) from Categoria c2, Indicador i2 where c2.categoriaPadre.id = c.id and i2.categoria.id = c2.id and i2.publicadoEnWeb=0 and i2.pteAprobacion=1 and c2.id=i2.categoria.id)>0)) " +
									"order by c.nombre";
	
	
	private static final String SELECT_CATEGORIAS_BY_PADRE_PENDIENTES_PUBLICACION_WEB="select c from Categoria c where c.categoriaPadre.id = :idCategoriaPadre and ((select count(i.id) from Indicador i where i.categoria = c.id and i.publicadoEnWeb=0 and i.pteAprobacion=1)>0 ) order by c.nombre";
	
	private static final String SELECT_CATEGORIAS_PRIMERNIVEL_IDS_WHERE_USER_TIENE_PERMISOS="select c.id from Categoria c " +
																							"where (c.categoriaPadre is null and " +
																							"( (select count(ue.id) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=c.id and ue.usuario.id =:idUsuario)>0 OR " +
																							"(select count(re.id) from RolElementoJerarquia re where re.elementoJerarquia.id=c.id and re.rol.id in (:listaIdRoles))>0 )" +
																							")";
	private static final String SELECT_IDS_ROLES_USUARIO="select ur.rol.id from UsuarioRol ur where ur.usuario.id= :idUsuario";
	
	private static final String SELECT_CATEGORIAS_PRIMER_NIVEL_USUARIO_NO_ADMIN_VISUALIZAR="select c from Categoria c " +
																							"where c.categoriaPadre is null and" +
																							"( c.id in (:listaIdCategoriasPrimerNivelPermiso) OR " +
																							"(select count(ip.id) from Indicador ip where (ip.categoria.id=c.id or ip.categoria.categoriaPadre.id=c.id) and ( (select count(ue1.id) from UsuarioElementoJerarquia ue1 where ue1.elementoJerarquia.id=ip.id and ue1.usuario.id= :idUsuario )>0 or (select count(re1.id) from RolElementoJerarquia re1 where re1.elementoJerarquia.id=ip.id and re1.rol.id in (:listaIdRoles))>0 ))>0 OR " +
																							"(select count(cs.id) from Categoria cs where cs.categoriaPadre.id=c.id and ( (select count(ue2.id) from UsuarioElementoJerarquia ue2 where ue2.elementoJerarquia.id=cs.id and ue2.usuario.id= :idUsuario )>0 or (select count(re2.id) from RolElementoJerarquia re2 where re2.elementoJerarquia.id=cs.id and re2.rol.id in (:listaIdRoles))>0 ))>0" +
																							" ) " +
																							"order by c.nombre";
	
	private static final String SELECT_SUBCATEGORIAS_IDS_WHERE_USER_TIENE_PERMISOS="select c.id from Categoria c " +
																					"where (c.categoriaPadre.id= :idCategoriaPadre and " +
																					"( (select count(ue.id) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=c.id and ue.usuario.id =:idUsuario)>0 OR " +
																					"(select count(re.id) from RolElementoJerarquia re where re.elementoJerarquia.id=c.id and re.rol.id in (:listaIdRoles))>0 )" +
																					")";
	
	private static final String SELECT_SUBCATEGORIAS_USUARIO_NO_ADMIN_VISUALIZAR="select c from Categoria c " +
																				"where c.categoriaPadre.id = :idCategoriaPadre and" +
																				"( c.id in (:listaIdsSubCategoriasPermiso) OR " +
																				"(select count(ip.id) from Indicador ip where (ip.categoria.id=c.id) and ( (select count(ue1.id) from UsuarioElementoJerarquia ue1 where ue1.elementoJerarquia.id=ip.id and ue1.usuario.id= :idUsuario )>0 or (select count(re1.id) from RolElementoJerarquia re1 where re1.elementoJerarquia.id=ip.id and re1.rol.id in (:listaIdRoles))>0 ))>0 " +
																				" ) " +
																				"order by c.nombre";
	
	private static final String SELECT_CATEGORIAS_USUARIO_PERMISOS="select c from Categoria c where ( " +
																	"(select count(ue.id) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=c.id and ue.usuario.id =:idUsuario)>0 OR " +
																	"(select count(re.id) from RolElementoJerarquia re where re.elementoJerarquia.id=c.id and re.rol.id in (:listaIdRoles))>0" +
																	") order by c.nombre";
	
	private static final String SELECT_CATEGORIA_POR_NOMBRE="select c from Categoria c where lower(TRANSLATE(c.nombre,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) like lower(:nombreCategoria)";
	
	
	private static final String SELECT_CATEGORIAS_IDS_WHERE_USER_TIENE_PERMISOS="select c.id from Categoria c " +
																				"where (" +
																				"((select count(ue.id) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=c.id and ue.usuario.id =:idUsuario)>0 OR " +
																				"(select count(re.id) from RolElementoJerarquia re where re.elementoJerarquia.id=c.id and re.rol.id in (:listaIdRoles))>0 )" +
																				")";
	
	private static final String SELECT_TODAS="select c from Categoria c order by c.nombre";
	
	
	public List<Categoria> cargaCategoriasPadre(){
		return  new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_PADRE).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPadrePublicados(){
		return  new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_PADRE_PUBLICADOS).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPorPadre(Long idCategoriaPadre){
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_BY_PADRE).setParameter("idCategoriaPadre", idCategoriaPadre).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPadreParaFuentes(Long idFuente){
		return  new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_PADRE_PARA_FUENTE).setParameter("idFuente", idFuente).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPorPadreParaFuentes(Long idCategoriaPadre, Long idFuente){
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_BY_PADRE_PARA_FUENTE).setParameter("idCategoriaPadre", idCategoriaPadre).setParameter("idFuente", idFuente).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPadrePublicos(){
		return  new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_PADRE_PUBLICOS).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPorPadrePublicos(Long idCategoriaPadre){
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_BY_PADRE_PUBLICOS).setParameter("idCategoriaPadre", idCategoriaPadre).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPorPadrePublicados(Long idCategoriaPadre){
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_BY_PADRE_PUBLICADOS).setParameter("idCategoriaPadre", idCategoriaPadre).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPadrePendientesDePublicos(){
		return  new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_PADRE_PENDIENTES_DE_PUBLICOS).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPadrePendientesPublicacionWeb(){
		return  new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_PADRE_PENDIENTES_PUBLICACION_WEB).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPorPadrePendientesDePublicos(Long idCategoriaPadre){
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_BY_PADRE_PENDIENTES_DE_PUBLICOS).setParameter("idCategoriaPadre", idCategoriaPadre).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPorPadrePendientesPublicacionWeb(Long idCategoriaPadre){
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_BY_PADRE_PENDIENTES_PUBLICACION_WEB).setParameter("idCategoriaPadre", idCategoriaPadre).getResultList());
	}
	
	public List<Long> cargaIdsCategoriasPrimerNivelUsuarioPermisos(Long idUsuario){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Long>(getEntityManager().createQuery(SELECT_CATEGORIAS_PRIMERNIVEL_IDS_WHERE_USER_TIENE_PERMISOS).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles", listaIdRoles).getResultList());
	}
	
	public List<Categoria> cargaCategoriasPrimerNivelUsuarioVisualizar(List<Long> listaIdCategoriasPrimerNivelPermiso,Long idUsuario){
		if(listaIdCategoriasPrimerNivelPermiso.isEmpty())
			listaIdCategoriasPrimerNivelPermiso.add(0L);
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_PRIMER_NIVEL_USUARIO_NO_ADMIN_VISUALIZAR).setParameter("listaIdCategoriasPrimerNivelPermiso", listaIdCategoriasPrimerNivelPermiso).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles", listaIdRoles).getResultList());
		
	}
	
	public List<Long> cargaIdsSubCategoriasUsuarioPermisos(Long idUsuario,Long idCategoriaPadre){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Long>(getEntityManager().createQuery(SELECT_SUBCATEGORIAS_IDS_WHERE_USER_TIENE_PERMISOS).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles", listaIdRoles).setParameter("idCategoriaPadre", idCategoriaPadre).getResultList());
	}
	
	public List<Categoria> cargaSubCategoriasUsuarioVisualizar(List<Long> listaIdsSubCategoriasPermiso,Long idUsuario,Long idCategoriaPadre){
		if(listaIdsSubCategoriasPermiso.isEmpty())
			listaIdsSubCategoriasPermiso.add(0L);
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		List<Categoria> result= new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_SUBCATEGORIAS_USUARIO_NO_ADMIN_VISUALIZAR).setParameter("idCategoriaPadre", idCategoriaPadre).setParameter("listaIdsSubCategoriasPermiso", listaIdsSubCategoriasPermiso).setParameter("listaIdRoles", listaIdRoles).setParameter("idUsuario",idUsuario).getResultList());
		listaIdsSubCategoriasPermiso.remove(0L);
		return result;
	}
	
	public List<Categoria> cargaCategoriasPermisosUsuario(Long idUsuario){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIAS_USUARIO_PERMISOS).setParameter("listaIdRoles", listaIdRoles).setParameter("idUsuario", idUsuario).getResultList());
	}
	
	public List<Categoria> cargaPorNombreLike(String nombreCategoria){
		String param= Normalizer.normalize(nombreCategoria, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_CATEGORIA_POR_NOMBRE).setParameter("nombreCategoria", "%"+param+"%").getResultList());
	}
	
	
	public List<Long> cargaIdsCategoriasUsuarioPermisos(Long idUsuario){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		return new ArrayList<Long>(getEntityManager().createQuery(SELECT_CATEGORIAS_IDS_WHERE_USER_TIENE_PERMISOS).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles", listaIdRoles).getResultList());
	}
	
	public Categoria cargaCategoriaPorNombre(String nombre, Long idCatPadre, Long idCategoria){
		String consulta = SELECT_CATEGORIA_BY_NOMBRE_Y_CAT_PADRE;
		
		if ( idCatPadre == 0 || idCatPadre == null ) {
			if ( idCategoria!=null && idCategoria>0 ) {
				consulta = SELECT_CATEGORIA_BY_NOMBRE_Y_CAT_PADRE_NULL_CON_CAT;
				for(Categoria  temp:new HashSet<Categoria>(getEntityManager().createQuery(consulta).setParameter("nombre", nombre).setParameter("idCat", idCategoria).getResultList()))
					 return temp;
				 return null;
			} else {
				consulta = SELECT_CATEGORIA_BY_NOMBRE_Y_CAT_PADRE_NULL;
				for(Categoria  temp:new HashSet<Categoria>(getEntityManager().createQuery(consulta).setParameter("nombre", nombre).getResultList()))
					 return temp;
				 return null;
			}
		}
		
		if ( idCategoria!=null && idCategoria>0 ) {
			consulta = SELECT_CATEGORIA_BY_NOMBRE_Y_CAT_PADRE_CON_CAT;
			for(Categoria  temp:new HashSet<Categoria>(getEntityManager().createQuery(consulta).setParameter("nombre", nombre).setParameter("idCat", idCategoria).setParameter("idCatPadre", idCatPadre).getResultList()))
				 return temp;
			 return null;
		} else {
			for(Categoria  temp:new HashSet<Categoria>(getEntityManager().createQuery(consulta).setParameter("nombre", nombre).setParameter("idCatPadre", idCatPadre).getResultList()))
				 return temp;
			 return null;
			
		}
	}
	
	public List<Categoria> cargaTodas(){
		return new ArrayList<Categoria>(getEntityManager().createQuery(SELECT_TODAS).getResultList());
	}
	
	
}
