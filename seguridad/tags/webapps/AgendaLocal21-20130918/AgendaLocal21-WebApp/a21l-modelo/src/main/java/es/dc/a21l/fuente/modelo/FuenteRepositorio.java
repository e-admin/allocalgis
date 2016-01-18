/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo;

import java.util.Date;
import es.dc.a21l.base.modelo.RepositorioBase;

public interface FuenteRepositorio extends RepositorioBase<Fuente>{
	public Fuente cargaFuentePorNombre(String nombre);
	public Integer cargaIndicadoresAsociadosFuente(Long idFuente);
	public Fuente existeFuenteInterna(String nombre, String url, String user,String pass, Date data);
}
