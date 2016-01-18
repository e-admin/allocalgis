/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.modelo.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.usuario.modelo.Usuario;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class UsuarioRepositorioImpl extends RepositorioBaseImpl<Usuario> implements UsuarioRepositorio{

	private static final String SELECT_USER_BY_LOGIN="select u from Usuario u where lower(u.login)= lower(:login)";
	private static final String SELECT_COUNT_USER_ADMIN="select count(u) from Usuario u where u.esAdmin=1";
	private static final String SELECT_USER_ADMIN = "select u from Usuario u where u.id= :idUsuario and u.esAdmin=1";
	private static final String SELECT_USUARIOS = "select u from Usuario u order by u.nombre";
	
	@SuppressWarnings("unchecked")
	public Usuario cargaUsuarioPorLogin(String login){
		 for(Usuario temp:new HashSet<Usuario>(getEntityManager().createQuery(SELECT_USER_BY_LOGIN).setParameter("login", login).getResultList()))
			 return temp;
		 return null;
	}
	
	public Boolean cargaExisteUnicoAdmin(){
		Long count=(Long)getEntityManager().createQuery(SELECT_COUNT_USER_ADMIN).getSingleResult();
		return(count.equals(1L));
	}
	
	@SuppressWarnings("unchecked")
	public Usuario cargarUsuarioAdmin(Long idUsuario){
		for(Usuario temp:new HashSet<Usuario>(getEntityManager().createQuery(SELECT_USER_ADMIN).setParameter("idUsuario", idUsuario).getResultList()))
			 return temp;
		return null;
	}
	
	/**Devuelve todos los usuarios almacenados en BD ordenados por el nombre de usuario*/
	@SuppressWarnings("unchecked")
	public List<Usuario> cargarTodosUsuarios(){
		List<Usuario> lista = new ArrayList<Usuario>(getEntityManager().createQuery(SELECT_USUARIOS).getResultList());
		return lista;
	}
}
