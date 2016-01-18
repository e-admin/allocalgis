/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.indicador.controlador;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.dc.a21l.fuente.cu.AtributoDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.historico.cu.HistoricoDto;
import es.dc.a21l.visualizacion.cu.RangosVisualizacionMapaDto;

public class DatosDiagramaMapa {

	private List<RangosVisualizacionMapaDto> rangos;
	private int numRangos;
	private HistoricoDto historico;
	private List<AtributoDto> listaColumnas;
	private String parametro;
	private int numFilas;
	private Set<Entry<String, ValorFDDto>> datos;
	private Set<Entry<String, ValorFDDto>> mapa;

	public void setRangos(List<RangosVisualizacionMapaDto> rangos) {
		this.rangos = rangos;
	}

	public List<RangosVisualizacionMapaDto> getRangos() {
		return rangos;
	}

	public void setNumRangos(int numRangos) {
		this.numRangos = numRangos;
	}

	public int getNumRangos() {
		return numRangos;
	}

	public void setHistorico(HistoricoDto historico) {
		this.historico = historico;
	}

	public HistoricoDto getHistorico() {
		return historico;
	}

	public void setListaColumnas(List<AtributoDto> listaColumnas) {
		this.listaColumnas = listaColumnas;
	}

	public List<AtributoDto> getListaColumnas() {
		return listaColumnas;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;

	}

	public void setNumFilas(int numFilas) {
		this.numFilas = numFilas;

	}

	public void setDatos(Set<Entry<String, ValorFDDto>> datos) {
		this.datos = datos;

	}

	public void setMapa(Set<Entry<String, ValorFDDto>> mapa) {
		this.mapa = mapa;
	}

	public Set<Entry<String, ValorFDDto>> getMapa() {
		return mapa;
	}

	public String getParametro() {
		return parametro;
	}

	public int getNumFilas() {
		return numFilas;
	}

	public Set<Entry<String, ValorFDDto>> getDatos() {
		return datos;
	}
}