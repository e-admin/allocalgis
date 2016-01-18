/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.indicador.controlador;

import org.springframework.web.client.RestTemplate;

import es.dc.a21l.comun.utils.UrlConstructorSW;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaBarrasDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaSectoresDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionMapaDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionTablaDto;

public final class Estilos {

	public static EstiloVisualizacionDiagramaBarrasDto cargarEstiloDiagramaBarras(
			long usuarioId, String urlBaseSW, RestTemplate restTemplate, Long idIndicador) {
		
		UrlConstructorSW url = new UrlConstructorSWImpl(urlBaseSW);
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaEstiloVisualizacionDiagramaBarras");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(usuarioId);
		
		EstiloVisualizacionDiagramaBarrasDto estilo = restTemplate.getForEntity(url.getUrl(), EstiloVisualizacionDiagramaBarrasDto.class).getBody();
		return estilo;
	}
	
	public static EstiloVisualizacionDiagramaSectoresDto cargarEstiloSectores(
			long usuarioId, String urlBaseSW, RestTemplate restTemplate, Long idIndicador) {
		
		UrlConstructorSW url = new UrlConstructorSWImpl(urlBaseSW);
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaEstiloVisualizacionDiagramaSectores");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(usuarioId);
		
		EstiloVisualizacionDiagramaSectoresDto estilo = restTemplate.getForEntity(url.getUrl(), EstiloVisualizacionDiagramaSectoresDto.class).getBody();
		return estilo;
	}
	
	public static EstiloVisualizacionMapaDto cargarEstiloMapa(long usuarioId, String urlBaseSW, RestTemplate restTemplate, Long idIndicador) {
		
		UrlConstructorSW url = new UrlConstructorSWImpl(urlBaseSW);
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaEstiloVisualizacionMapa");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(usuarioId);
		
		EstiloVisualizacionMapaDto estilo = restTemplate.getForEntity(url.getUrl(), EstiloVisualizacionMapaDto.class).getBody();
		return estilo;
	}
	
	public static EstiloVisualizacionTablaDto cargarEstiloVisualizacionTabla(
			long usuarioId, String urlBaseSW, RestTemplate restTemplate, Long idIndicador) {
		
		UrlConstructorSW url = new UrlConstructorSWImpl(urlBaseSW);
		url.setParametroCadena("indicadores");
		url.setParametroCadena("cargaEstiloVisualizacionTabla");
		url.setParametroCadena(idIndicador);
		url.setParametroCadena(usuarioId);
		
		EstiloVisualizacionTablaDto estilo = restTemplate.getForEntity(url.getUrl(), EstiloVisualizacionTablaDto.class).getBody();
		return estilo;
	}
}