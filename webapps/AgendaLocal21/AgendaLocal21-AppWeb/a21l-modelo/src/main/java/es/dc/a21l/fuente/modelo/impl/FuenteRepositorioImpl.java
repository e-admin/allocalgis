/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.fuente.modelo.Fuente;
import es.dc.a21l.fuente.modelo.FuenteRepositorio;
import es.dc.a21l.fuente.modelo.TablaFuenteDatos;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class FuenteRepositorioImpl extends RepositorioBaseImpl<Fuente> implements FuenteRepositorio{
	private static final String SELECT_FUENTE_BY_NOMBRE="select f from Fuente f where lower(f.nombre)= lower(:nombre)";
	private static final String SELECT_INDICADORES_ASOCIADOS_FUENTE="select t from TablaFuenteDatos t where t.fuente.id= :idFuente";
	private static final String SELECT_FUENTE_INTERNA = "select f from Fuente f where lower(f.nombre)= lower(:nombre) and lower(f.usuario)= lower(:user) and lower(f.password)= lower(:pass) and lower(f.infoConexion)= lower(:url) and f.fechaRegistro= :data and f.escatalogointerno = 1";

	public Fuente cargaFuentePorNombre(String nombre){
		 for(Fuente  temp:new HashSet<Fuente>(getEntityManager().createQuery(SELECT_FUENTE_BY_NOMBRE).setParameter("nombre", nombre).getResultList()))
			 return temp;
		 return null;
	}
	

	public Integer cargaIndicadoresAsociadosFuente(Long idFuente) 
	{
		List<TablaFuenteDatos> listaIndicadoresUsandoFuente = getEntityManager().createQuery(SELECT_INDICADORES_ASOCIADOS_FUENTE).setParameter("idFuente", idFuente).getResultList();
		return listaIndicadoresUsandoFuente.size();
	}


	public Fuente existeFuenteInterna(String nombre, String url, String user,String pass,Date data) 
	{
		List<Fuente> listFuentes = getEntityManager().createQuery(SELECT_FUENTE_INTERNA).setParameter("nombre", nombre).setParameter("user", user).setParameter("pass", pass).setParameter("url", url).setParameter("data", data).getResultList();
		
		if (listFuentes == null || listFuentes.isEmpty()) {
			return new Fuente();
		} else {
			return listFuentes.get(0);
		}

	}
	
}
