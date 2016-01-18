/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo.impl;

import java.util.ArrayList;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuario;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuarioRepositorio;

public class IndicadorUsuarioRepositorioImpl extends RepositorioBaseImpl<IndicadorUsuario> implements IndicadorUsuarioRepositorio {

	private static final String CARGA_POR_USUARIO_INDICADOR="select count(iu) from IndicadorUsuario iu where iu.indicador.id =:idIndicador and iu.usuario.id =:idUsuario";
	public Boolean cargaEsUsuarioCreadorIndicador(Long idIndicador,Long idUsuario){
		List<IndicadorUsuario> lista= new ArrayList<IndicadorUsuario>(getEntityManager().createQuery(CARGA_POR_USUARIO_INDICADOR).setParameter("idIndicador", idIndicador).setParameter("idUsuario", idUsuario).getResultList());
		return !lista.isEmpty();
	}

	public void guardaLogModificacion(Long idIndicador, Long idUsuario) {
		// TODO Auto-generated method stub
		
	}
}
