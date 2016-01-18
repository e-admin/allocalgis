/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo;

import es.dc.a21l.base.modelo.RepositorioBase;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.DescripcionAtributoDto;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;

/**
 *
 * @author Balidea Consulting & Programming
 */
public interface OrigenMysqlRepositorio extends RepositorioBase<Fuente> {
    public EncapsuladorListSW<TablaFuenteDatosDto> listarTablasFuenteExterna(Fuente fuente);
    public EncapsuladorListSW<AtributoFuenteDatosDto> listarColumnasTablaFuenteExterna(Fuente fuente, String tabla);
    public EncapsuladorListSW<DescripcionAtributoDto> obtenerEsquemaTablaFuenteExterna(Fuente fuente, String tabla);
    public AtributosMapDto obtenerDatosTablaFuenteExterna(Fuente fuente, String tabla, boolean completos);
    public boolean probarFuente(Fuente fuente);
}
