/**
 * AFirmaException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.security.dnie.afirma;

public class AFirmaException extends Exception /*PPMAPException*/ {

	private static final long serialVersionUID = -2905834317693441138L;

	/**
	 * Crea una nueva AFirmaException con el mensaje especificado
	 *
	 * @param _Str_message Mensaje
	 */
	public AFirmaException(String _Str_message) 
	{ 
		super(_Str_message);
	}

//	/**
//	* Crea una nueva AFirmaException con el mensaje y codigo de error especificados
//	*
//	* @param _Str_message Mensaje
//	* @param _Str_errorCode Codigo de error
//	*/    
//	public AFirmaException(String _Str_message, 
//	String _Str_errorCode) 
//	{
//	super(_Str_message, _Str_errorCode);
//	}

//	/**
//	* Crea una nueva AFirmaException con el mensaje, codigo de error y descripcion detallada
//	* especificados
//	*
//	* @param _Str_message Mensaje
//	* @param Str_codigoError Codigo de error
//	* @param Str_detalles Descripcion detallada
//	*/      
//	public AFirmaException(String _Str_message, 
//	String _Str_errorCode,
//	String _Str_errorDetail) 
//	{
//	super(_Str_message, _Str_errorCode, _Str_errorDetail);
//	}
}    
