/**
 * DatosValidacionCertificadoOut.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.dnie.afirma;

public class DatosValidacionCertificadoOut {
	private final int RESULTADO_CORRECTO = 0;
    
	private String Str_resultado = null;
	private String Str_descripcion = null;
    
	private String Str_codigoResultado = null;
	private String Str_descripcionResultado = null;
	private String Str_exceptionResultado = null;

	private DatosCertificadoOut certificado = null;
    
	/** Constructor */
	public DatosValidacionCertificadoOut() {}
    
	/**
	 * Devuelve los datos del certificado validado
	 *
	 * @return Objecto que implementa la interfaz DatosCertificadoOut
	 */
	public final DatosCertificadoOut getCertificado() {
		return certificado;
	}
	
	/**
	 * Inicializa el campo datos del certificado validado
	 *
	 * @param _certificado Objecto que implementa la interfaz DatosCertificadoOut
	 */
	public final void setCertificado(DatosCertificadoOut _certificado) {
		certificado = _certificado;
	}

	/**
	 * Devuelve el codigo de resultado de la operacion de validación
	 *
	 * @return Codigo de resultado
	 */
	public final String getCodigoResultado() {
		return Str_codigoResultado;
	}

	/**
	 * Inicializa el campo codigo de resultado de la operacion de validación
	 *
	 * @param _str_codigoResultado codigo de resultado de la operacion de validación
	 */
	public final void setCodigoResultado(String _str_codigoResultado) {
		Str_codigoResultado = _str_codigoResultado;
	}

	/**
	 * Devuelve la descripcion de la  operacion de validación
	 *
	 * @return Descripcion
	 */
	public final String getDescripcion() {
		return Str_descripcion;
	}

	/**
	 * Inicializa el campo descripcion de la  operacion de validación
	 *
	 * @param _str_descripcion descripcion de la  operacion de validación
	 */
	public final void setDescripcion(String _str_descripcion) {
		Str_descripcion = _str_descripcion;
	}

	/**
	 * Devuelve la descripcion del resultado de la operacion de validación
	 *
	 * @return Descripcion del resultado
	 */
	public final String getDescripcionResultado() {
		return Str_descripcionResultado;
	}

	/**
	 * Inicializa el campo descripcion del resultado de la operacion de validación
	 *
	 * @param _str_descripcionResultado descripcion del resultado de la operacion de validación
	 */
	public final void setDescripcionResultado(String _str_descripcionResultado) {
		Str_descripcionResultado = _str_descripcionResultado;
	}

	/**
	 * Devuelve la posible exception que se produjo durante la operación de validacion
	 *
	 * @return Exception del resultado
	 */
	public final String getExceptionResultado() {
		return Str_exceptionResultado;
	}

	/**
	 * Inicializa el campo exception que se produjo durante la operación de validacion
	 *
	 * @param _str_exceptionResultado exception que se produjo durante la operación de validacion
	 */
	public final void setExceptionResultado(String _str_exceptionResultado) {
		Str_exceptionResultado = _str_exceptionResultado;
	}

	/**
	 * Devuelve el resultado de la operacion de validación
	 *
	 * @return Resultado
	 */
	public final String getResultado() {
		return Str_resultado;
	}

	/**
	 * Inicializa el campo resultado de la operacion de validación
	 *
	 * @param _str_resultado resultado de la operacion de validación
	 */
	public final void setResultado(String _str_resultado) {
		Str_resultado = _str_resultado;
	}

	/**
	 * Devuelve si el certificado es correcto o no.
	 * 
	 * @return true si el certificado es correcto; false si no.
	 */
	public boolean certificadoCorrecto() {
		boolean boo_return = true;

		try {
			boo_return = RESULTADO_CORRECTO == Integer.parseInt(getResultado());
		} catch (NumberFormatException nfe) {
			boo_return = false;
		} catch (NullPointerException npe) {
			boo_return = false;
		}

		return boo_return;
	}
    
	/**
	 * Devuelve uan representacion del objecto en forma de String que recoge el valor
	 * de todos sus campos.
	 * 
	 * @return Representacion del objecto
	 */
	public String toString() {
		StringBuffer Str_return = new StringBuffer(0);
     
		Str_return.append("[");
		Str_return.append(this.certificado.toString());
		Str_return.append("],");
		Str_return.append(this.Str_codigoResultado);
		Str_return.append(",");
		Str_return.append(this.Str_descripcion);
		Str_return.append(",");
		Str_return.append(this.Str_descripcionResultado);
		Str_return.append(",");
		Str_return.append(this.Str_exceptionResultado);
		Str_return.append(",");
		Str_return.append(this.Str_resultado);

		return Str_return.toString(); 
	}
}
