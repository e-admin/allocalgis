/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;

/**
 *
 * @author Balidea Consulting & Programming
 */
public interface GestorCUTablaFuenteDatos {
	
	public TablaFuenteDatosDto guarda(TablaFuenteDatosDto tablaFuenteDatosDto, EncapsuladorErroresSW erros);
	public EncapsuladorListSW<TablaFuenteDatosDto> cargaTodosPorIndicadorYFuente(Long idIndicador, Long idFuente);
	public TablaFuenteDatosDto cargaPorIndicadorYFuenteYTabla(Long idIndicador, Long idFuente, String nombreTabla);
	public TablaFuenteDatosDto carga(Long idTabla);
    public TablaFuenteDatosDto borra(Long id, EncapsuladorErroresSW erros);
    public TablaFuenteDatosDto cargaFuentePorIndicadorYNombreYFuente(Long idIndicador, Long idFuente, String nombre);
}
