/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.utils;

public class Par<T1, T2> {

	private T1 valor1;
	private T2 valor2;

	public Par(final T1 valor1, final T2 valor2) {
		super();
		this.valor1 = valor1;
		this.valor2 = valor2;
	}

	public T1 getValor1() {
		return this.valor1;
	}

	public void setValor1(final T1 valor1) {
		this.valor1 = valor1;
	}

	public T2 getValor2() {
		return this.valor2;
	}

	public void setValor2(final T2 valor2) {
		this.valor2 = valor2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.valor1 == null) ? 0 : this.valor1.hashCode());
		result = (prime * result) + ((this.valor2 == null) ? 0 : this.valor2.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Par other = (Par) obj;
		if (this.valor1 == null) {
			if (other.valor1 != null)
				return false;
		} else if (!this.valor1.equals(other.valor1))
			return false;
		if (this.valor2 == null) {
			if (other.valor2 != null)
				return false;
		} else if (!this.valor2.equals(other.valor2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + this.valor1.toString() + ", " + this.valor2.toString() + ")";
	}

}