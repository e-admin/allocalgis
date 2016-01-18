/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.utils;

import java.util.HashMap;

public abstract class MapaParesConTransformacionRepetidos<T, K> extends MapaPares<T, K> {

	private static final long serialVersionUID = 1L;
	private final HashMap<Par<T, K>, Integer> contadores = new HashMap<Par<T, K>, Integer>();

	@Override
	public void put(final T clave1, final T clave2, final K valor1, final K valor2) {
		super.put(clave1, clave2, this.valor1ConContador(clave1, valor1),
				this.valor2ConContador(clave2, valor2));
	}

	private K valor1ConContador(final T clave1, final K valor1) {
		return this.convertirValor1(valor1, this.actualizarContador(clave1, valor1));
	}

	private K valor2ConContador(final T clave1, final K valor1) {
		return this.convertirValor2(valor1, this.actualizarContador(clave1, valor1));
	}

	protected Integer actualizarContador(final T clave1, final K valor1) {
		Par<T, K> clave = new Par<T, K>(clave1, valor1);
		if (!this.contadores.containsKey(clave)) {
			this.contadores.put(clave, 0);
		}
		Integer contadorActual = this.contadores.get(clave);
		this.contadores.put(clave, contadorActual + 1);
		return contadorActual;
	}

	protected abstract K convertirValor1(K valor, Integer contador);

	protected abstract K convertirValor2(K valor, Integer contador);
}