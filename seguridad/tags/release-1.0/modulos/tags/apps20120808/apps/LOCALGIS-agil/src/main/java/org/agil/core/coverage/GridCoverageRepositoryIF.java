package org.agil.core.coverage;

/**
 *  Interfaz que deben implementar todas aquellas clases que sean repositorio
 *  de GridCoverages, es decir, que guarden y permitan acceder a coberturas.
 *
 *@author    alvaro zabala 26-sep-2003
 */
public interface GridCoverageRepositoryIF {
	/**
	 *  Metodo que anyade una GridCoverage al repositorio
	 *
	 *@param  gridCoverage  The feature to be added to the Coverage attribute
	 */
	public void addCoverage(GridCoverage gridCoverage);


	/**
	 *  Permite recuperar un GridCoverage a partir de su identificador
	 *
	 *@param  idCoverage  Description of Parameter
	 *@return             The Coverage value
	 */
	public GridCoverage getCoverage(long idCoverage);
}
