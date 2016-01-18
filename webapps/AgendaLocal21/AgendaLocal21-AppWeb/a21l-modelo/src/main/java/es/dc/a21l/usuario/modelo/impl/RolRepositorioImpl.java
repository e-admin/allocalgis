/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.modelo.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.usuario.modelo.Rol;
import es.dc.a21l.usuario.modelo.RolRepositorio;

@SuppressWarnings("unchecked")
public class RolRepositorioImpl extends RepositorioBaseImpl<Rol> implements RolRepositorio {

	private static final String SELECT_ROL_BY_NOMBRE="select r from Rol r where lower(r.nombre)= lower(:nombre) and r.id != :idRol";
	private static final String SELECT_ROLES_BY_USUARIO="select ur.rol from UsuarioRol ur where ur.usuario.id = :idUsuario order by ur.rol.id";
	private static final String SELECT_IDS_ROLES_BY_USUAIRO="select ur.rol.id from UsuarioRol ur where ur.usuario.id = :idUsuario order by ur.rol.id";
	private static final String SELECT_ROLES_NO_INCLIDOS_EN_LISTA="select r from Rol r where r.id not in (:listaIdsRoles)";
	private static final String SELECT_ROLES = "select r from Rol r order by r.nombre";
	
	public Rol cargaRolPorNombreYIdDistinto(String nombre,Long idRol){
		 for(Rol temp: new HashSet<Rol>(getEntityManager().createQuery(SELECT_ROL_BY_NOMBRE).setParameter("nombre", nombre).setParameter("idRol", idRol).getResultList()))
			 return temp;
		 return null;
	}
	
	public List<Rol> cargaRolesPorUsuario(Long idUsuario){
		return new ArrayList<Rol>(getEntityManager().createQuery(SELECT_ROLES_BY_USUARIO).setParameter("idUsuario", idUsuario).getResultList());
	}
	
	public List<Rol> cargaRolesSinAsignarUsuario(Long idUsuario){
		List<Long> listaIdsRoles= new ArrayList<Long>(getEntityManager().createQuery(SELECT_IDS_ROLES_BY_USUAIRO).setParameter("idUsuario", idUsuario).getResultList());
		if(listaIdsRoles.isEmpty())
			listaIdsRoles.add(0L);
		return new ArrayList<Rol>(getEntityManager().createQuery(SELECT_ROLES_NO_INCLIDOS_EN_LISTA).setParameter("listaIdsRoles", listaIdsRoles).getResultList());
	}
	
	/**Devuelve todos los roles almacenados en BD ordenados por el nombre de rol*/
	@SuppressWarnings("unchecked")
	public List<Rol> cargarTodosRoles(){
		List<Rol> lista = new ArrayList<Rol>(getEntityManager().createQuery(SELECT_ROLES).getResultList());
		return lista;
	}
}
