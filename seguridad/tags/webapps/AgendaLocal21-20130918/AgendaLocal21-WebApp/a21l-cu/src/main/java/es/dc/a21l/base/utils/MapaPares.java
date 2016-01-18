/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MapaPares<T, K> extends LinkedHashMap<Par<T, T>, List<Par<K, K>>> {

	private static final long serialVersionUID = 1L;

	public void put(final T clave1, final T clave2, final K valor1, final K valor2) {
		Par<T, T> clave = new Par<T, T>(clave1, clave2);
		if (!this.containsKey(clave)) {
			this.put(clave, new ArrayList<Par<K, K>>());
		}
		this.get(clave).add(new Par<K, K>(valor1, valor2));
	}

	@Override
	public String toString() {
		String resultado = "";
		for (Par<T, T> clave : this.keySet()) {
			resultado += clave + ":\n\t";
			List<Par<K, K>> valores = this.get(clave);
			for (Par<K, K> valor : valores) {
				resultado += " " + valor;
			}
			resultado += "\n";
		}
		return resultado;
	}

	public static void main(final String[] args) {
		MapaPares<String, String> mapaRelaciones = new MapaPares<String, String>();
		mapaRelaciones.put("tabla1", "tabla2", "at1", "at2");
		mapaRelaciones.put("tabla1", "tabla2", "at4", "at5");
		mapaRelaciones.put("tabla1", "tabla2", "at4", "at6");
		mapaRelaciones.put("tabla2", "tabla3", "at1", "at2");
		mapaRelaciones.put("tabla4", "tabla5", "at1", "at2");
		mapaRelaciones.put("tabla4", "tabla5", "at1", "at6");
		System.out.println(mapaRelaciones);
	}
}