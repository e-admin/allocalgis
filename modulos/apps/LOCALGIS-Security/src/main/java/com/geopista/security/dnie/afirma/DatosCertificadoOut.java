/**
 * DatosCertificadoOut.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.dnie.afirma;

import java.util.Calendar;
import java.util.Date;

public class DatosCertificadoOut {

	private static final String NOMBRE_RESPONSABLE_FIELD = "nombreResponsable";
	private static final String ID_EMISOR_FIELD = "idEmisor";
	private static final String S_APELLIDO_FIELD = "segundoApellidoResponsable";
	private static final String TIPO_DERTIFICADO_FIELD = "tipoCertificado";
	private static final String P_APELLIDO_FIELD = "primerApellidoResponsable";
	private static final String RAZON_SOCIAL_FIELD = "razonSocial";
	private static final String NIF_RESPONSABLE_FIELD = "NIFResponsable";
	private static final String NIF_CIF_FIELD = "NIF-CIF";
	private static final String APELLIDOS_RESPONSABLE_FIELD = "ApellidosResponsable";
	private static final String USO_CERTIFICADO_FIELD = "usoCertificado";
	private static final String VALIDO_HASTA_FIELD = "validoHasta";
	private static final String POLITICA_FIELD = "politica";
	private static final String SUBJECT_FIELD = "subject";
	private static final String VERSION_POLITICA_FIELD = "versionPolitica";
	private static final String ORGANIZACION_EMISORA_FIELD = "OrganizacionEmisora";
	private static final String ID_POLITICA_FIELD = "idPolitica";
	private static final String NUMERO_SERIE_FIELD = "numeroSerie";
	private static final String EMAIL_FIELD = "email";
	private static final String CLASIFICACION_FIELD = "clasificacion";
	private static final String VALIDO_DESDE_FIELD = "validoDesde";
	private static final String NOMBRE_APELLIDOS_RESPONSABLE_FIELD = "NombreApellidosResponsable";
	private static final String RESPONSABLE_FIELD = "responsable";
	private static final String FECHA_NACIMIENTO_FIELD = "fechaNacimiento";

	private String Str_nombreResponsable_ = null;
	private String Str_idEmisor_ = null;
	private String Str_segundoApellidoResponsable_ = null;
	private String Str_tipoCertificado_ = null;
	private String Str_primerApellidoResponsable_ = null;
	private String Str_NIFResponsable_ = null;
	private String Str_apellidosResponsable_ = null;
	private String Str_usoCertificado_ = null;
	private Date validoHasta_ = null;
	private String Str_politica_ = null;
	private String Str_subject_ = null;
	private String Str_versionPolitica_ = null;
	private String Str_organizacionEmisora_ = null;
	private String Str_idPolitica_ = null;
	private String Str_numeroSerie_ = null;
	private String Str_email_ = null;
	private String Str_clasificacion_ = null;
	private Date validoDesde_ = null;
	private String Str_nombreApellidosResponsable_ = null;
	private String Str_responsable_ = null;
	private String Str_fechaNacimiento_ = null;

	/** Constructor */
	public DatosCertificadoOut() {}

	/**
	 * Devuelve los apellidos del responsable del certificado
	 *
	 * @return Apellidos del responsable del certificado
	 */
	public final String getApellidosResponsable() {
		return Str_apellidosResponsable_;
	}

	/**
	 * Inicializa el campo apellidos del responsble del certificado
	 *
	 * @param _str_apellidosResponsable Apellidos del responsable del certificado
	 */

	public final void setApellidosResponsable(String _str_apellidosResponsable) {
		Str_apellidosResponsable_ = _str_apellidosResponsable;
	}

	/**
	 * Devuelve la fecha de nacimiento del responsable del certificado
	 *
	 * @return Fecha de nacimiento del responsable del certificado
	 */
	public final String getFechaNacimiento() {
		return Str_fechaNacimiento_;
	}

	/**
	 * Inicializa el campo fecha de nacimiento del responsable del certificado
	 *
	 * @param _str_fechaNacimiento fecha de nacimiento del responsable del certificado
	 */
	public final void setFechaNacimiento(String _str_fechaNacimiento) {
		Str_fechaNacimiento_ = _str_fechaNacimiento;
	}

	/**
	 * Devuelve el id del emisor del certificado
	 *
	 * @return Id del emisor del certificado
	 */
	public final String getIdEmisor() {
		return Str_idEmisor_;
	}

	/**
	 * Inicializa el campo id del emisor del certificado
	 *
	 * @param _str_idEmisor id del emisor del certificado
	 */
	public final void setIdEmisor(String _str_idEmisor) {
		Str_idEmisor_ = _str_idEmisor;
	}

	/**
	 * Devuelve el NIF del responsable del certificado
	 *
	 * @return NIF del responsable del certificado
	 */
	public final String getNIFResponsable() {
		return Str_NIFResponsable_;
	}
	
	/**
	 * Inicializa el campo NIF del responsable del certificado
	 *
	 * @param _str_NIFResponsable NIF del responsable del certificado
	 */
	public final void setNIFResponsable(String _str_NIFResponsable) {
		if (null == Str_NIFResponsable_ || "".equals(Str_NIFResponsable_)) {
			Str_NIFResponsable_ = _str_NIFResponsable;
		}
	}

	/**
	 * Devuelve el nombre del responsable del certificado
	 *
	 * @return Nombre del responsable del certificado
	 */
	public final String getNombreResponsable() {
		return Str_nombreResponsable_;
	}
	
	/**
	 * Inicializa el campo nombre del responsable del certificado
	 *
	 * @param _str_nombreResponsable nombre del responsable del certificado
	 */
	public final void setNombreResponsable(String _str_nombreResponsable) {
		Str_nombreResponsable_ = _str_nombreResponsable;
	}

	/**
	 * Devuelve el primer apellido del responsable del certificado o la razon social
	 *
	 * @return Primer apellido del responsable del certificado o la razon social
	 */
	public final String getPrimerApellidoResponsableORazonSocial() {
		return Str_primerApellidoResponsable_;
	}

	/**
	 * Inicializa el campo primer apellido del responsable del certificado o la razon social
	 *
	 * @param _str_primerApellidoResponsable primer apellido del responsable del certificado o la razon social
	 */
	public final void setPrimerApellidoResponsableORazonSocial(String _str_primerApellidoResponsable) {
		if (null == Str_primerApellidoResponsable_ || "".equals(Str_primerApellidoResponsable_)) {
			Str_primerApellidoResponsable_ = _str_primerApellidoResponsable;
		}
	}

	/**
	 * Devuelve el nombre completo del responsable del certificado
	 *
	 * @return Nombre completo del responsable del certificado
	 */
	public final String getResponsable() {
		return Str_responsable_;
	}

	/**
	 * Inicializa el campo nombre completo del responsable del certificado
	 *
	 * @param _str_responsable nombre completo del responsable del certificado
	 */
	public final void setResponsable(String _str_responsable) {
		Str_responsable_ = _str_responsable;
	}

	/**
	 * Devuelve el segundo apellido del responsable del certificado
	 *
	 * @return Segundo apellido del responsable del certificado
	 */
	public final String getSegundoApellidoResponsable() {
		return Str_segundoApellidoResponsable_;
	}

	/**
	 * Inicializa el campo segundo apellido del responsable del certificado
	 *
	 * @param _str_segundoApellidoResponsable segundo apellido del responsable del certificado
	 */
	public final void setSegundoApellidoResponsable(String _str_segundoApellidoResponsable) {
		Str_segundoApellidoResponsable_ = _str_segundoApellidoResponsable;
	}

	/**
	 * Devuelve el tipo del certificado
	 *
	 * @return Tipo del certificado
	 */
	public final String getTipoCertificado() {
		return Str_tipoCertificado_;
	}

	/**
	 * Inicializa el campo tipo del certificado
	 *
	 * @param _str_tipoCertificado tipo del certificado
	 */
	public final void setTipoCertificado(String _str_tipoCertificado) {
		Str_tipoCertificado_ = _str_tipoCertificado;
	}

	/**
	 * Devuelve la clasificacion del certificado
	 *
	 * @return Clasificacion del certificado
	 */
	public final String getClasificacion() {
		return Str_clasificacion_;
	}

	/**
	 * Inicializa el campo clasificacion del certificado
	 *
	 * @param _str_clasificacion clasificacion del certificado
	 */
	public final void setClasificacion(String _str_clasificacion) {
		Str_clasificacion_ = _str_clasificacion;
	}

	/**
	 * Devuelve el email del responsable del certificado
	 *
	 * @return Email del responsable del certificado
	 */
	public final String getEmail() {
		return Str_email_;
	}

	/**
	 * Inicializa el campo email del responsable del certificado
	 *
	 * @param _str_email email del responsable del certificado
	 */
	public final void setEmail(String _str_email) {
		Str_email_ = _str_email;
	}

	/**
	 * Devuelve el id de politica del certificado
	 *
	 * @return Id de politica del certificado
	 */
	public final String getIdPolitica() {
		return Str_idPolitica_;
	}

	/**
	 * Inicializa el campo id de politica del certificado
	 *
	 * @param _str_idPolitica id de politica del certificado
	 */
	public final void setIdPolitica(String _str_idPolitica) {
		Str_idPolitica_ = _str_idPolitica;
	}

	/**
	 * Devuelve el nombre y los apellidos del responsable del certificado
	 *
	 * @return Nombre y apellidos del responsable del certificado
	 */
	public final String getNombreApellidosResponsable() {
		return Str_nombreApellidosResponsable_;
	}

	/**
	 * Inicializa el campo nombre y apellidos del responsable del certificado
	 *
	 * @param _str_nombreApellidosResponsable nombre y apellidos del responsable del certificado
	 */
	public final void setNombreApellidosResponsable(String _str_nombreApellidosResponsable) {
		Str_nombreApellidosResponsable_ = _str_nombreApellidosResponsable;
	}

	/**
	 * Devuelve el numero de serie del certificado
	 *
	 * @return Numero de serie del certificado
	 */
	public final String getNumeroSerie() {
		return Str_numeroSerie_;
	}

	/**
	 * Inicializa el campo numero de serie del certificado
	 *
	 * @param _str_numeroSerie numero de serie del certificado
	 */
	public final void setNumeroSerie(String _str_numeroSerie) {
		Str_numeroSerie_ = _str_numeroSerie;
	}

	/**
	 * Devuelve la organización emisora del certificado
	 *
	 * @return Organización emisora del certificado
	 */
	public final String getOrganizacionEmisora() {
		return Str_organizacionEmisora_;
	}

	/**
	 * Inicializa el campo organización emisora del certificado
	 *
	 * @param _str_organizacionEmisora organización emisora del certificado
	 */
	public final void setOrganizacionEmisora(String _str_organizacionEmisora) {
		Str_organizacionEmisora_ = _str_organizacionEmisora;
	}

	/**
	 * Devuelve la descricpion de la politica del certificado
	 *
	 * @return Politica del certificado
	 */
	public final String getPolitica() {
		return Str_politica_;
	}

	/**
	 * Inicializa el campo descricpion de la politica del certificado
	 *
	 * @param _str_politica descricpion de la politica del certificado
	 */
	public final void setPolitica(String _str_politica) {
		Str_politica_ = _str_politica;
	}

	/**
	 * Devuelve el subject del certificado
	 *
	 * @return Subject del certificado
	 */
	public final String getSubject() {
		return Str_subject_;
	}

	/**
	 * Inicializa el campo subject del certificado
	 *
	 * @param _str_subject subject del certificado
	 */
	public final void setSubject(String _str_subject) {
		Str_subject_ = _str_subject;
	}

	/**
	 * Devuelve el uso del certificado
	 *
	 * @return Uso del certificado
	 */
	public final String getUsoCertificado() {
		return Str_usoCertificado_;
	}

	/**
	 * Inicializa el campo uso del certificado
	 *
	 * @param _str_usoCertificado uso del certificado
	 */
	public final void setUsoCertificado(String _str_usoCertificado) {
		Str_usoCertificado_ = _str_usoCertificado;
	}

	/**
	 * Devuelve la fecha de inicio de validez del certificado
	 *
	 * @return Inicio de validez del certificado
	 */
	public final Date getValidoDesde() {
		return validoDesde_;
	}

	/**
	 * Inicializa el campo fecha de inicio de validez del certificado
	 *
	 * @param _validoDesde fecha de inicio de validez del certificado
	 */
	public final void setValidoDesde(Date _validoDesde) {
		validoDesde_ = _validoDesde;
	}

	/**
	 * Devuelve la fecha de fin de validez del certificado
	 *
	 * @return Fin de validez del certificado
	 */
	public final Date getValidoHasta() {
		return validoHasta_;
	}

	/**
	 * Inicializa el campo fecha de fin de validez del certificado
	 *
	 * @param _validoHasta fecha de fin de validez del certificado
	 */
	public final void setValidoHasta(Date _validoHasta) {
		validoHasta_ = _validoHasta;
	}

	/**
	 * Devuelve la version de la politica del certificado
	 *
	 * @return Version de la politica del certificado
	 */
	public final String getVersionPolitica() {
		return Str_versionPolitica_;
	}

	/**
	 * Inicializa el campo version de la politica del certificado
	 *
	 * @param _str_versionPolitica version de la politica del certificado
	 */
	public final void setVersionPolitica(String _str_versionPolitica) {
		Str_versionPolitica_ = _str_versionPolitica;
	}

	/**
	 * Inicializa uno de los valores de la clase en función del nombre del 
	 * campo a inicilizar. Los nombre de los campos se recogen directamente de la
	 * respuesta de la plataforma @Firma a una operacion de validación de un
	 * certificado
	 * 
	 * @param _Str_valueName Nombre del campo
	 * @param _Str_value Valor del campo
	 */
	public void setValue(String _Str_valueName, String _Str_value) {
		if (NOMBRE_RESPONSABLE_FIELD.equals(_Str_valueName)) {
			setNombreResponsable(_Str_value);
		} else if (ID_EMISOR_FIELD.equals(_Str_valueName)) {
			setIdEmisor(_Str_value);
		} else if (S_APELLIDO_FIELD.equals(_Str_valueName)) {
			setSegundoApellidoResponsable(_Str_value);
		} else if (TIPO_DERTIFICADO_FIELD.equals(_Str_valueName)) {
			setTipoCertificado(_Str_value);
		} else if (P_APELLIDO_FIELD.equals(_Str_valueName)) {
			setPrimerApellidoResponsableORazonSocial(_Str_value);
		} else if (RAZON_SOCIAL_FIELD.equals(_Str_valueName)) {
			setPrimerApellidoResponsableORazonSocial(_Str_value);
		} else if (RESPONSABLE_FIELD.equals(_Str_valueName)) {
			setResponsable(_Str_value);
		} else if (NIF_RESPONSABLE_FIELD.equals(_Str_valueName)) {
			setNIFResponsable(_Str_value);
		} else if (NIF_CIF_FIELD.equals(_Str_valueName)) {
			setNIFResponsable(_Str_value);
		} else if (APELLIDOS_RESPONSABLE_FIELD.equals(_Str_valueName)) {
			setApellidosResponsable(_Str_value);
		} else if (FECHA_NACIMIENTO_FIELD.equals(_Str_valueName)) {
			setFechaNacimiento(_Str_value);
		} else if (USO_CERTIFICADO_FIELD.equals(_Str_valueName)) {
			setUsoCertificado(_Str_value);
		} else if (VALIDO_HASTA_FIELD.equals(_Str_valueName)) {
			setValidoHasta(getDate(_Str_value));
		} else if (SUBJECT_FIELD.equals(_Str_valueName)) {
			setSubject(_Str_value);
		} else if (ID_POLITICA_FIELD.equals(_Str_valueName)) {
			setIdPolitica(_Str_value);
		} else if (NUMERO_SERIE_FIELD.equals(_Str_valueName)) {
			setNumeroSerie(_Str_value);
		} else if (EMAIL_FIELD.equals(_Str_valueName)) {
			setEmail(_Str_value);
		} else if (CLASIFICACION_FIELD.equals(_Str_valueName)) {
			setClasificacion(_Str_value);
		} else if (VALIDO_DESDE_FIELD.equals(_Str_valueName)) {
			setValidoDesde(getDate(_Str_value));
		} else if (NOMBRE_APELLIDOS_RESPONSABLE_FIELD.equals(_Str_valueName)) {
			setNombreApellidosResponsable(_Str_value);
		} else if (POLITICA_FIELD.equals(_Str_valueName)) {
			setPolitica(_Str_value);
		} else if (VERSION_POLITICA_FIELD.equals(_Str_valueName)) {
			setVersionPolitica(_Str_value);
		} else if (ORGANIZACION_EMISORA_FIELD.equals(_Str_valueName)) {
			setOrganizacionEmisora(_Str_value);
		}
	}

	/**
	 * Obtiene una fecha a partir de un String que representa a una fecha en el
	 * formato devuelto por la plataforma @Firma: 2008-05-06 mar 14:04:23 +0200 
	 * 
	 * @param _Str_date String representado a un fecha; formato 2008-05-06 mar 14:04:23 +0200
	 * @return Objecto Date 
	 */
	public static Date getDate(String _Str_date) {
		try {
			String Str_value = null;
			Calendar calendar = Calendar.getInstance();

			Str_value = _Str_date.substring(0, 4);
			calendar.set(Calendar.YEAR, Integer.parseInt(Str_value));
			Str_value = _Str_date.substring(5, 7);
			calendar.set(Calendar.MONTH, Integer.parseInt(Str_value) - 1);
			Str_value = _Str_date.substring(8, 10);
			calendar.set(Calendar.DATE, Integer.parseInt(Str_value));
			Str_value = _Str_date.substring(15, 17);
			calendar.set(Calendar.HOUR, Integer.parseInt(Str_value));
			Str_value = _Str_date.substring(18, 20);
			calendar.set(Calendar.MINUTE, Integer.parseInt(Str_value));
			Str_value = _Str_date.substring(21, 23);
			calendar.set(Calendar.SECOND, Integer.parseInt(Str_value));

			return calendar.getTime();
		}catch(Exception ex) {
			return null;
		}
	}

	/**
	 * Devuelve uan representacion del objecto en forma de String que recoge el valor
	 * de todos sus campos.
	 * 
	 * @return Representacion del objecto
	 */
	public String toString() {
		StringBuffer Str_return = new StringBuffer(0);

		Str_return.append(this.Str_apellidosResponsable_);
		Str_return.append(",");
		Str_return.append(this.Str_clasificacion_);
		Str_return.append(",");
		Str_return.append(this.Str_email_);
		Str_return.append(",");
		Str_return.append(this.Str_fechaNacimiento_);
		Str_return.append(",");
		Str_return.append(this.Str_idEmisor_);
		Str_return.append(",");
		Str_return.append(this.Str_idPolitica_);
		Str_return.append(",");
		Str_return.append(this.Str_NIFResponsable_);
		Str_return.append(",");
		Str_return.append(this.Str_nombreApellidosResponsable_);
		Str_return.append(",");
		Str_return.append(this.Str_nombreResponsable_);
		Str_return.append(",");
		Str_return.append(this.Str_numeroSerie_);
		Str_return.append(",");
		Str_return.append(this.Str_organizacionEmisora_);
		Str_return.append(",");
		Str_return.append(this.Str_politica_);
		Str_return.append(",");
		Str_return.append(this.Str_primerApellidoResponsable_);
		Str_return.append(",");
		Str_return.append(this.Str_responsable_);
		Str_return.append(",");
		Str_return.append(this.Str_segundoApellidoResponsable_);
		Str_return.append(",");
		Str_return.append(this.Str_subject_);
		Str_return.append(",");
		Str_return.append(this.Str_tipoCertificado_);
		Str_return.append(",");
		Str_return.append(this.Str_usoCertificado_);
		Str_return.append(",");
		Str_return.append(this.Str_versionPolitica_);

		return Str_return.toString();
	}
}
