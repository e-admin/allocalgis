/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu;

public class ParDiagramaBarrasDto implements Comparable<ParDiagramaBarrasDto>{
	private String valorAmbito;
	private String valor;
	
	public ParDiagramaBarrasDto(String valorAmbito, String valor) {
		super();
		this.valorAmbito = valorAmbito;
		this.valor = valor;
	}

	public String getValorAmbito() {
		return valorAmbito;
	}

	public void setValorAmbito(String valorAmbito) {
		this.valorAmbito = valorAmbito;
	}

	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}

	public int compareTo(ParDiagramaBarrasDto o) {
		return valorAmbito.compareTo(o.valorAmbito);
	}

}
