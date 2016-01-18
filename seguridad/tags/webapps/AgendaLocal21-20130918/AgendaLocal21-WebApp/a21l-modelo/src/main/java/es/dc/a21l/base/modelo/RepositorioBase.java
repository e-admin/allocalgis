/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo;

import java.util.List;

public abstract interface RepositorioBase<T extends EntidadBase> {

	public abstract T carga(long id);
	public abstract T guarda(T entidade);
	public abstract void borra(long id);
	public abstract boolean existe(long id);
	public abstract List<T> cargaTodos();
}
