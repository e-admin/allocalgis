/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.historico.modelo.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.historico.modelo.Historico;
import es.dc.a21l.historico.modelo.HistoricoRepositorio;

public class HistoricoRepositorioImpl extends RepositorioBaseImpl<Historico> implements HistoricoRepositorio{
	private static final String SELECT_HISTORICO_POR_INDICADOR = "select h from Historico h where h.indicador.id = :idindicador order by h.fecha asc";
	
	@SuppressWarnings("unchecked")
	public List<Historico> cargarTodosPorIndicador(Long idIndicador) {
		return new ArrayList<Historico>(getEntityManager().createQuery(SELECT_HISTORICO_POR_INDICADOR).setParameter("idindicador", idIndicador).getResultList());
	}
	
	@SuppressWarnings("unchecked")
	public boolean existeRepetido(Long idIndicador,String fecha) {
		ArrayList<Historico> lista =new ArrayList<Historico>(getEntityManager().createQuery(SELECT_HISTORICO_POR_INDICADOR).setParameter("idindicador", idIndicador).getResultList());
		for(Historico historico: lista){
			if(dateToString(historico.getFecha()).equals(fecha)){
				return true;
			}
		}
		return false;
	}
	public String dateToString(Date fecha){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			String strFecha;
			strFecha = sdf.format(fecha);
			return strFecha;
		}catch (Exception e) {
			return null;
		}
	}
}
