/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.modelo.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.usuario.modelo.RolElementoJerarquia;
import es.dc.a21l.usuario.modelo.RolElementoJerarquiaRepositorio;
@SuppressWarnings("unchecked")
public class RolElementoJerarquiaRepositorioImpl extends RepositorioBaseImpl<RolElementoJerarquia> implements RolElementoJerarquiaRepositorio {

	private static final String SELECT_BY_ROL = "Select e From RolElementoJerarquia e Where e.rol.id = :idRol";
	private static final String SELECT_ID_EJ_BY_ROL="select re.elementoJerarquia.id from RolElementoJerarquia re where re.rol.id = :idRol";
	private static final String DELETE_BY_LISTA_EJ_ID_ROL="delete RolElementoJerarquia re where re.elementoJerarquia.id in (:listaIdEJ) and re.rol.id = :idRol";
	
	public List<RolElementoJerarquia> cargarElementosJerarquiaPorRol(Long idRol){
		return new ArrayList<RolElementoJerarquia>(getEntityManager().createQuery(SELECT_BY_ROL).setParameter("idRol", idRol).getResultList());
	}
	
	public Set<RolElementoJerarquia> cargarSetElementosJerarquiaPorRol(Long idRol){
		return new HashSet<RolElementoJerarquia>(getEntityManager().createQuery(SELECT_BY_ROL).setParameter("idRol", idRol).getResultList());
	}
	
	public List<Long> cargaIdsElementosJerarquiaPorRol(Long idRol){
		return new ArrayList<Long>(getEntityManager().createQuery(SELECT_ID_EJ_BY_ROL).setParameter("idRol", idRol).getResultList());
	}
	
	public void borraPorListaEJyRol(List<Long> listaIdEJ,Long idRol){
		if(listaIdEJ.isEmpty())
			listaIdEJ.add(0L);
		getEntityManager().createQuery(DELETE_BY_LISTA_EJ_ID_ROL).setParameter("listaIdEJ", listaIdEJ).setParameter("idRol", idRol).executeUpdate();
		listaIdEJ.remove(0L);
	}
}
