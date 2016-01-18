/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.utils;



public class MapaRelaciones<T, K> extends MapaParesConTransformacionRepetidos<String, String> {

	private static final long serialVersionUID = 1L;

	@Override
	protected String convertirValor1(final String valor, final Integer contador) {
		//if (contador == 0)
			return valor + "_relacion";
		//return valor + "_relacion" + contador.toString();
	}

	@Override
	protected String convertirValor2(final String valor, final Integer contador) {
		//if (contador == 0)
			return valor + "_relacionada";
		//return valor + "_relacionada" + contador.toString();
	}

	public static void main(final String[] args) {
		MapaRelaciones mapaRelaciones = new MapaRelaciones();
		mapaRelaciones.put("municipio", "poblacion2010", "municipio", "municipio");
		mapaRelaciones.put("municipio", "poblacion2011", "municipio", "municipio");
		mapaRelaciones.put("municipio", "poblacion2011", "municipioVecino", "municipio");
		System.out.println(mapaRelaciones);
	}
}