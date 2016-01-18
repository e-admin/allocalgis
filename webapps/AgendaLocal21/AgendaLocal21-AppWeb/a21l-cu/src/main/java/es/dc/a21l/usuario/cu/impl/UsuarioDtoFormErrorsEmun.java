/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import es.dc.a21l.base.modelo.GestionErrores.enumerados.FormErrorEmun;

public enum UsuarioDtoFormErrorsEmun implements FormErrorEmun {
	
	NOMBREVACIO("nombre","validacion.usuario.nombre.vacio"),
	ERROR_TAMANHO_DESC("descripcion","validacion.categorias.error.descripcion"),
	CONTRASENHAVACIA("password","validacion.usuario.contrasenha.vacia"),
	CONTRASENHASDISITNTAS("password","validacion.usuario.contrasenhas.distintas"),
	IDENTIFICADORCONESPACIOS("login","validacion.usuario.login.conEspacios"),
	IDENTIFICADOR_VACIO("login","validacion.usuario.login.vacio"),
	EMAIL_NO_VALIDO("email","validacion.usuario.email.noValido"),
	LOGIN_EN_USO("login","validacion.usuario.login.enUso"),
	CONTRASENHA_ANTIGUA_DISTINTA("passwordOld","validacion.usuario.contrasenhas.AntiguaNoCoindice"),
	NOMBRE_RESERVADO("login","validacion.usuario.login.revervado"),
	UNICO_ADMIN("esAdmin","validacion.usuario.admin.unico");
	
	private String atributo;
	private String cadenaCodigoError;

	UsuarioDtoFormErrorsEmun(String atributo,String cadenaCodigoError){
		this.atributo=atributo;
		this.cadenaCodigoError=cadenaCodigoError;
	}
	
	public String getAtributo() {
		return atributo;
	}

	public String getCadenaCodigoError() {
		return cadenaCodigoError;
	}

}
