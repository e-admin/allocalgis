/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.metadatos.cu;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorFileSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;

/**
 *
 * @author Balidea Consulting & Programming
 */
public interface GestorCUMetadatos {
	    
    public MetadatosDto cargaPorIdIndicador(Long id);
    
    public EncapsuladorFileSW guardaFichero(EncapsuladorFileSW envioFich, String path, EncapsuladorErroresSW erros);
    public boolean borrarFichero(String nombre, Long idIndicador, String path, EncapsuladorErroresSW erros);
    public MetadatosDto guarda(MetadatosDto metadatosDto, EncapsuladorErroresSW erros);

    public Boolean borra(Long id);
    public MetadatosDto borraPorIdIndicador(Long id, EncapsuladorErroresSW erros);
}
