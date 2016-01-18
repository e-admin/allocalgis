/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.metadatos.modelo;

import es.dc.a21l.base.modelo.RepositorioBase;

public interface MetadatosRepositorio extends RepositorioBase<Metadatos>{
	public Metadatos cargaPorIdIndicador(Long id);	
	public Metadatos borrarPorIdIndicador(Long id);
}
