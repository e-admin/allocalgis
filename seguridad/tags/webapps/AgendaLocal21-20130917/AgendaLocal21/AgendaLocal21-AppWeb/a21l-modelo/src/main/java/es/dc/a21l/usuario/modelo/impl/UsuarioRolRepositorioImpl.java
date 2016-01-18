/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.modelo.impl;

import java.util.ArrayList;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.usuario.modelo.UsuarioRol;
import es.dc.a21l.usuario.modelo.UsuarioRolRepositorio;

public class UsuarioRolRepositorioImpl extends RepositorioBaseImpl<UsuarioRol> implements UsuarioRolRepositorio {

	private static final String CARGA_BY_USUARIO="select ur from UsuarioRol ur where ur.usuario.id= :idUsuario";
	private static final String CARGA_IDS_ROLES_BY_USUARIO="select ur.rol.id from UsuarioRol ur where ur.usuario.id= :idUsuario";
	private static final String DELETE_BY_IDS_ROLES_Y_USUARIO="delete UsuarioRol ur where ur.usuario.id= :idUsuario and ur.rol.id in (:listaIdsRoles)";
	
	public List<UsuarioRol> cargaPorUsuario(Long idUsuario){
		return new ArrayList<UsuarioRol>(getEntityManager().createQuery(CARGA_BY_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
	}
	
	public List<Long> cargaIdsRolesUsuario(Long idUsuario){
		return new ArrayList<Long>(getEntityManager().createQuery(CARGA_IDS_ROLES_BY_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
	}
	
	public void borraPorUsuarioYListaRoles(Long idUsuario, List<Long> listaIdsRoles){
		 if(listaIdsRoles.isEmpty())
			 listaIdsRoles.add(0L);
		 getEntityManager().createQuery(DELETE_BY_IDS_ROLES_Y_USUARIO).setParameter("idUsuario", idUsuario).setParameter("listaIdsRoles", listaIdsRoles).executeUpdate();
		 listaIdsRoles.remove(0L);
	}
	
	
	
}
