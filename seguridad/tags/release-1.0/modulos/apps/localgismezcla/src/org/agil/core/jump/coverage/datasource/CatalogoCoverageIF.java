package org.agil.core.jump.coverage.datasource;

import java.util.Collection;
import org.agil.core.coverage.Coverage;

/**
 *  Interfaz que deben implementar todas las clases que sirvan de catalogo de
 *  Coverages (origenes de datos de cobertura)
 *
 *@author    alvaro zabala 29-sep-2003
 */
public interface CatalogoCoverageIF {
	/**
	 *  Devuelve una lista con todas las entradas contenidas en el catalogo
	 *
	 *@return    The Entrys value
	 */
	public Collection getEntrys();


	/**
	 *  A partir del nombre de una entrada del catalogo, devuelve un
	 *  FeatureCollection -que representa la entrada solicitada-
	 *
	 *@param  entry  Description of Parameter
	 *@return        The Coverage value
	 */
	public Coverage getCoverage(String entry);

}
