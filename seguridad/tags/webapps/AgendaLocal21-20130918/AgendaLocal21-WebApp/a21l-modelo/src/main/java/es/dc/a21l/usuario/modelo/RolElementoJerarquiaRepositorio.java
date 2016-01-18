/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.modelo;

import java.util.List;
import java.util.Set;

import es.dc.a21l.base.modelo.RepositorioBase;

public interface RolElementoJerarquiaRepositorio extends RepositorioBase<RolElementoJerarquia> {

	public List<RolElementoJerarquia> cargarElementosJerarquiaPorRol(Long idRol);
	public Set<RolElementoJerarquia> cargarSetElementosJerarquiaPorRol(Long idRol);
	public List<Long> cargaIdsElementosJerarquiaPorRol(Long idRol);
	public void borraPorListaEJyRol(List<Long> listaIdEJ,Long idRol);
}
