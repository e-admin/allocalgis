package com.geopista.security.dnie.afirma;

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
