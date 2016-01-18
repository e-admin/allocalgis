/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.expresion.cu;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorIntegerSW;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.fuente.cu.AtributosMapDto;

public interface GestorCUExpresion {

	public ExpresionDto cargaPorId(Long id);
	public ExpresionDto guarda(ExpresionDto expresionDto, EncapsuladorErroresSW errores);
	public void borra(Long id);
	public ExpresionDto guardaExpresion(String expresion,Long idIndicador, String caracterSeparadorDecimales);
	//devuelve -1 si no existen fuentes en la expresion
	public EncapsuladorIntegerSW cargaNumeroIteracionesExpresion(ExpresionDto expresionDto, String caracterSeparador, Map<TiposFuente, String> mapaPath);
	public List<String> cargaEvaluacionExpresion(ExpresionDto expresionDto,String caracterSeparador, Map<TiposFuente, String> mapaPath,Integer tamanhoColumnaAmbito);
	public List<String> cargaEvaluacionExpresionDeEstructura(ExpresionDto expresionDto, LinkedHashMap<String, AtributosMapDto> mapaTablas, Integer tamanhoColumnaAmbito);
	public LinkedHashMap<String, AtributosMapDto> cargaInicialValoresDeFuentesExpresionEnEstructura(ExpresionDto expresionDto, LinkedHashMap<String, AtributosMapDto> mapaTablas, 
			String caracterSeparador, Map<TiposFuente, String> mapaPath);
}
