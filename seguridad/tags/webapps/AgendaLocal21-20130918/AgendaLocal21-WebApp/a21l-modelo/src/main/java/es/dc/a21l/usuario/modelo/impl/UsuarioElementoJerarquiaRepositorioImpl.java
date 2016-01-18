/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.modelo.impl;

import java.util.ArrayList;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;
import es.dc.a21l.usuario.modelo.UsuarioElementoJerarquia;
import es.dc.a21l.usuario.modelo.UsuarioElementoJerarquiaRepositorio;

@SuppressWarnings("unchecked")
public class UsuarioElementoJerarquiaRepositorioImpl extends RepositorioBaseImpl<UsuarioElementoJerarquia> implements UsuarioElementoJerarquiaRepositorio {

	private static final String SELECT_BY_USUARIO = "Select e From UsuarioElementoJerarquia e Where e.usuario.id = :idUsuario";
	private static final String SELECT_IDS_EJS_BY_USUARIO="select ue.elementoJerarquia.id from UsuarioElementoJerarquia ue where ue.usuario.id= :idUsuario";
	private static final String DELETE_BY_IDS_EJS_Y_USUARIO="delete UsuarioElementoJerarquia ue where ue.usuario.id= :idUsuario and ue.elementoJerarquia.id in (:listaIdsEJs)";
	private static final String SELECT_IDS_ROLES_USUARIO="select ur.rol.id from UsuarioRol ur where ur.usuario.id= :idUsuario";
	
	private static final String SELECT_INDICADOR_PERMISO_PROPIO_USER="select i from Indicador i where i.id =:idIndicador and " +
																	"(" +
																	"(select count(ue) from UsuarioElementoJerarquia ue where ue.elementoJerarquia.id=i.id and ue.usuario.id = :idUsuario)>0 OR " +
																	"(select count(re) from RolElementoJerarquia re where re.rol.id in (:listaIdRoles) and re.elementoJerarquia.id=i.id )>0 ) " ;
	
	public List<UsuarioElementoJerarquia> cargarElementosJerarquiaPorUsuario(Long idUsuario){
		return new ArrayList<UsuarioElementoJerarquia>(getEntityManager().createQuery(SELECT_BY_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
	}
	
	public List<Long> cargarIdsEJsUsuario(Long idUsuario){
		return new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_EJS_BY_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
	}
	
	public void borrarListaEJsPorUsuario(Long idUsuario, List<Long> listaIdsEJs){
		 if(listaIdsEJs.isEmpty())
			 listaIdsEJs.add(0L);
		 getEntityManager().createQuery(DELETE_BY_IDS_EJS_Y_USUARIO).setParameter("idUsuario", idUsuario).setParameter("listaIdsEJs", listaIdsEJs).executeUpdate();
		 listaIdsEJs.remove(0L);
	}
	
	public Boolean cargaTienePermisosPropiosSobreIndicador(Long idIndicador, Long idUsuario){
		List<Long> listaIdRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdRoles.isEmpty())
			listaIdRoles.add(0L);
		List<Indicador> result= new ArrayList<Indicador>(getEntityManager().createQuery(SELECT_INDICADOR_PERMISO_PROPIO_USER).setParameter("idIndicador", idIndicador).setParameter("idUsuario", idUsuario).setParameter("listaIdRoles",listaIdRoles).getResultList());
		return !result.isEmpty();
	}
}
