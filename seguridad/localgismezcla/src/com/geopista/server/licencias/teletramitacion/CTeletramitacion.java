package com.geopista.server.licencias.teletramitacion;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.licencias.*;
import com.geopista.protocol.licencias.actividad.DatosActividad;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.tipos.CTipoAnexo;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.protocol.licencias.tipos.CTipoObra;
import com.geopista.server.database.COperacionesDatabase;
import com.geopista.server.database.CPoolDatabase;
import org.pdfbox.cos.COSDictionary;
import org.pdfbox.cos.COSDocument;
import org.pdfbox.cos.COSName;
import org.pdfbox.cos.COSString;
//import org.pdfbox.encryption.DecryptDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.interactive.form.*;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:56 $
 *          $Name:  $
 *          $RCSfile: CTeletramitacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CTeletramitacion {
    /** campos obligatorios propietario */
	private static String _idMunicipio = "";
	private static String _DNIPropietario = "";
	private static String _nombrePropietario = "";
	private static String _tipoViaPropietario = "";
	private static String _nombreViaPropietario = "";
	private static String _numeroViaPropietario = "";
	private static String _cPostalPropietario = "";
	private static String _municipioPropietario = "";
	private static String _provinciaPropietario = "";

    /** campos obligatorios representante */
	private static String _DNIRepresentante = "";
	private static String _nombreRepresentante = "";
	private static String _tipoViaRepresentante = "";
	private static String _nombreViaRepresentante = "";
	private static String _numeroViaRepresentante = "";
	private static String _cPostalRepresentante = "";
	private static String _municipioRepresentante = "";
	private static String _provinciaRepresentante = "";

	private static String _isRepresentedBy = "0";
	private static String _tieneTecnico = "0";
	private static String _tienePromotor = "0";

    /** campos obligatorios tecnico */
	private static String _DNITecnico = "";
	private static String _nombreTecnico = "";
	private static String _tipoViaTecnico = "";
	private static String _nombreViaTecnico = "";
	private static String _numeroViaTecnico = "";
	private static String _cPostalTecnico = "";
	private static String _municipioTecnico = "";
	private static String _provinciaTecnico = "";

    /** campos obligatorios promotor */
	private static String _DNIPromotor = "";
	private static String _nombrePromotor = "";
	private static String _tipoViaPromotor = "";
	private static String _nombreViaPromotor = "";
	private static String _numeroViaPromotor = "";
	private static String _cPostalPromotor = "";
	private static String _municipioPromotor = "";
	private static String _provinciaPromotor = "";

	private static String _fechaSolicitud = "";
	private static String _tipoObra = "";
	private static String _tipoLicencia = "";

    private static boolean campoEmailPropietarioObligatorio= false;
    private static boolean campoEmailRepresentanteObligatorio= false;

	private static Hashtable _hPDFields = null;
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CTeletramitacion.class);

	public static boolean checkRegistroTelematico() {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			try {
				connection = CPoolDatabase.getConnnection("teletramitacion");
			} catch (Exception e) {
				logger.error("No se puede obtener la conexión, seguramente no este habilitada la teletramitación");
			}

			if (connection == null) {
				logger.warn("No se puede obtener la conexión, seguramente no este habilitada la teletramitación");
				return false;

			}

			logger.info("connection: " + connection);

			//String sql = "select a.CNUMERO,c.CRUTAACC,c.CNOM,a.CNIFREM, a.CNOMREM, a.CFECHAHORA from PVURTINFOREG a,PVURTFICHREG b,PVUAFINFOFICH c where a.CESTADO=0 and a.CNUMERO=b.CNUMREG and b.CROL='Rol_Principal' and b.CGUID=c.CGUID";
            /** Si en el campo CCODDEST el valor es GEOPISTA: forma de identificar que registros tiene que leer GeoPISTA. */
            String sql = "select a.CNUMERO,c.CRUTAACC,c.CNOM,a.CNIFREM, a.CNOMREM, a.CFECHAHORA from PVURTINFOREG a,PVURTFICHREG b,PVUAFINFOFICH c where a.CESTADO=0 and a.CCODDEST='GEOPISTA' and a.CNUMERO=b.CNUMREG and b.CROL='Rol_Principal' and b.CGUID=c.CGUID";

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				String numreg = rs.getString("CNUMERO");
				String nifSolicitante = rs.getString("CNIFREM");
				String nombreSolicitante = rs.getString("CNOMREM");
				String ruta = rs.getString("CRUTAACC");
				String nombre = rs.getString("CNOM");
				Date fechaSolicitud = rs.getTimestamp("CFECHAHORA");

				String pdfFileName = ruta + File.separator + nombre;
				logger.info("pdfFileName: " + pdfFileName);

                Hashtable hCampos= getDatosDocumento(pdfFileName);
                Vector anexos = getAnexosRegistroTelematico(numreg);

                /** primero comprobamos si el registro es de aportación de documentos. */
                int retorno = isAportacionDocumentos(nifSolicitante, hCampos, anexos);
                if (retorno == 0) {
                    /** El registro no es de aportacion de documentos. Comprobamos que el formulario sea correcto. */
                    if (formularioSolicitudOk(hCampos)){
                        CRegistroTeletramitacion registroTeletramitacion = new CRegistroTeletramitacion(numreg, pdfFileName, anexos, nifSolicitante, nombreSolicitante);
                        registroTeletramitacion.setFechaEntradaRegistro(fechaSolicitud);

                        if (!procesaRegistroTelematico(registroTeletramitacion)) {
                            logger.info("Error en el procesamiento del registro telematico");
                            String cnom = crearFichNotif("Solicitud Rechazada", CConstantesTeletramitacion.NOTIF_MESSAGE_CAMPOS_OBLIGATORIOS, CConstantesTeletramitacion.VU_NOTIF_CRUTAACC);
                            /** Insertamos una notificacion en el sistema de teletramitacion */
                            COperacionesDatabase.insertarNotifTelematico(null, registroTeletramitacion.getNifSolicitante(), cnom, "Solicitud Rechazada", "Acuse Recibo", "Solicitud Rechazada", CConstantesTeletramitacion.VU_NOTIF_CRUTAACC, CConstantesTeletramitacion.HITOESTEXP_MESSAGE_CAMPOS_OBLIGATORIOS, "GeoPISTA");
                        } else
                            logger.info("Registro telematico procesado");
                    }else{
                        /** Formulario erroneo, insertamos notificacion. */
                        String mensaje= "";
                        try{
                            int idTipoLicencia= new Integer((String) hCampos.get(CConstantesTeletramitacion.CSolicitudLicencia_CTipoLicencia_idTipoLicencia)).intValue();
                            if (idTipoLicencia == CConstantesComando.TIPO_OBRA_MAYOR) mensaje= CConstantesTeletramitacion.NOTIF_MESSAGE_FORMULARIO_ERRONEO_1 + " Obra Mayor " + CConstantesTeletramitacion.NOTIF_MESSAGE_FORMULARIO_ERRONEO_2;
                            else if (idTipoLicencia == CConstantesComando.TIPO_OBRA_MENOR) mensaje= CConstantesTeletramitacion.NOTIF_MESSAGE_FORMULARIO_ERRONEO_1 + " Obra Menor " + CConstantesTeletramitacion.NOTIF_MESSAGE_FORMULARIO_ERRONEO_2;
                            else if (idTipoLicencia == CConstantesComando.TIPO_ACTIVIDAD) mensaje= CConstantesTeletramitacion.NOTIF_MESSAGE_FORMULARIO_ERRONEO_1 + " Actividad " + CConstantesTeletramitacion.NOTIF_MESSAGE_FORMULARIO_ERRONEO_2;
                            else mensaje= CConstantesTeletramitacion.NOTIF_MESSAGE_FORMULARIO_ERRONEO;
                        }catch (Exception e){
                            mensaje= CConstantesTeletramitacion.NOTIF_MESSAGE_FORMULARIO_ERRONEO;
                        }
                        /** creamos fichero de notificacion */
                        String cnom = crearFichNotif("Solicitud Rechazada", mensaje, CConstantesTeletramitacion.VU_NOTIF_CRUTAACC);
                        /** Insertamos una notificacion en el sistema de teletramitacion */
                        COperacionesDatabase.insertarNotifTelematico(null, nifSolicitante, cnom, "Solicitud Rechazada", "Acuse Recibo", "Solicitud Rechazada", CConstantesTeletramitacion.VU_NOTIF_CRUTAACC, CConstantesTeletramitacion.HITOESTEXP_MESSAGE_FORMULARIO_ERRONEO, "GeoPISTA");
                    }
                }
                /** Cambiamos de estado a procesado, si el expediente no esta bloqueado. */
                if (retorno != 2) {
                    CSolicitudLicencia solicitudLicencia = new CSolicitudLicencia();
                    solicitudLicencia.setNumAdministrativo(numreg);
                    COperacionesDatabase.changeEstadoTelematico(solicitudLicencia, 1);
                }
			}

			/** cerramos la conexion */
			safeClose(rs, statement, connection);

			return true;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}
	}

/*
	//metodo correspondiente al api PDFBOX v0.6.6
	public static Hashtable getDatosDocumento(String pdfFileName) {
		Hashtable hCampos = new Hashtable();

		try {
			PDFParser parser = new PDFParser(new FileInputStream(pdfFileName));
			parser.parse();
			PDDocument pdDoc = new PDDocument(parser.getDocument());
			if (pdDoc.isEncrypted()) {
                new DecryptDocument(pdDoc).decryptDocument("");
			}
			PDAcroForm pdForm = pdDoc.getDocumentCatalog().getAcroForm();
			try {
				List lFields = pdForm.getFields();
				for (Iterator i = lFields.iterator(); i.hasNext();) {
					PDField field = (PDField) i.next();


                    String sName = field.getName();
//					String sName = field.getFullyQualifiedName();
					String sValue = "";
					COSDictionary dict = field.getDictionary();
					try {
						COSString aux = (COSString) dict.getItem(COSName.getPDFName("V"));

						if (aux != null) sValue = aux.getString();
					} catch (Exception ex) {
						logger.warn("Exception: " + ex.toString());
					}
					logger.info(sName + ": -" + sValue + "-");
					hCampos.put(sName, sValue);
				}
				pdDoc.close();
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				logger.error("Exception: " + sw.toString());
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
		return hCampos;
	}



	//metodo correspondiente al api PDFBOX v0.6.6
	public static boolean procesaRegistroTelematico(CRegistroTeletramitacion registroTeletramitacion) {

		logger.debug("Inicio.");


		try {

			String numreg = registroTeletramitacion.getNumreg();
			String pdfFileName = registroTeletramitacion.getPdfFileName();
			Vector anexos = registroTeletramitacion.getAnexos();

			COSDocument document = null;
			InputStream input = new FileInputStream(pdfFileName);
			PDFParser parser = new PDFParser(input);
			parser.parse();
			document = parser.getDocument();
			PDDocument pdDoc = new PDDocument(document);
			if (pdDoc.isEncrypted()) {
				new DecryptDocument(pdDoc).decryptDocument("");
			}
			PDAcroForm pdForm = pdDoc.getDocumentCatalog().getAcroForm();
			logger.debug("pdForm: " + pdForm);
			try {
				List lFields = pdForm.getFields();
				PDField field = null;
				String sName = "";
				String sValue = "";
				_hPDFields = new Hashtable();
				for (Iterator i = lFields.iterator(); i.hasNext();) {
					field = (PDField) i.next();
					sName = field.getName();
					COSDictionary dict = field.getDictionary();

					try {
						COSString aux = (COSString) dict.getItem(COSName.getPDFName("V"));
						System.out.println("*** aux: " + aux);
						if (aux != null) {
							sValue = aux.getString();
						} else
							sValue = "";

					} catch (Exception ex) {
						logger.warn("Exception: " + ex.toString());
					}
					logger.info(sName + ": -" + sValue + "-");
					setCampoPDF(sName, sValue);
				}


				pdDoc.close();

				// Comprobamos si el tecnico y el promotor son obligatorios.
				int idTipoLicencia = new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CTipoLicencia_idTipoLicencia)).intValue();
				boolean obligatorioTecnico = false;
				boolean obligatorioPromotor = false;
				if (idTipoLicencia == CConstantesComando.TIPO_OBRA_MAYOR) {
					obligatorioTecnico = true;
					obligatorioPromotor = true;
				}

				// Validamos los valores del registro. Si falta por rellenar alguno
				 // de los campo obligatorios devolvemos el expediente
				if (!okCamposObligatorios(registroTeletramitacion, obligatorioTecnico, obligatorioPromotor)) {
					return false;
				}
				// solicitud que nos llega en el PDF
				CSolicitudLicencia solicitud = getSolicitudPDFile(anexos);
				solicitud.setNumAdministrativo(registroTeletramitacion.getNumreg());
				solicitud.setFecha(registroTeletramitacion.getFechaEntradaRegistro());
				// expediente
				CEstado estado = new CEstado();
				estado.setIdEstado(CConstantesTeletramitacion.ID_ESTADO_INICIAL_VU);
				CExpedienteLicencia expediente = new CExpedienteLicencia(estado);

				expediente.setCNAE((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_cnae));
				// el expediente nos llega desde Ventanilla Unica
				expediente.setVU("1");
				CResultadoOperacion resultado = COperacionesDatabase.crearExpedienteLicencias(solicitud, expediente, null, solicitud.getIdMunicipio());

				return true;


			} catch (Exception ex) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				logger.error("Exception: " + sw.toString());

			}
			input.close();
			document.close();

			return false;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}

*/

	//metodo correspondiente al api PDFBOX v0.7.2
	public static Hashtable getDatosDocumento(String pdfFileName) {
		Hashtable hCampos = new Hashtable();

		try {
			PDFParser parser = new PDFParser(new FileInputStream(pdfFileName));
			parser.parse();
			PDDocument pdDoc = new PDDocument(parser.getDocument());
			if (pdDoc.isEncrypted()) {
				logger.warn("PDF encriptado.");
				try {
					pdDoc.decrypt(""); //password?
				} catch (Exception ex) {
					logger.error("decrypt error. Exception: " + ex.toString());
				}
			}
			PDAcroForm pdForm = pdDoc.getDocumentCatalog().getAcroForm();

			List lFields = pdForm.getFields();
			System.out.println("pdForm.getFields().size(): " + pdForm.getFields().size());

			Iterator iterator = lFields.iterator();
			while (iterator.hasNext()) {
				PDField field = (PDField) iterator.next();
				if (field == null) {
					continue;
				}


				String key = field.getFullyQualifiedName();


				String value = "";
				if (field instanceof PDTextbox) {

					value = (field.getValue() == null) ? "" : field.getValue();


				} else if (field instanceof PDChoiceField) {

					COSDictionary dict = field.getDictionary();
					value = ((COSString) dict.getItem(COSName.getPDFName("V"))).getString();

				} else if (field instanceof PDCheckbox) {

					PDCheckbox checkbox = (PDCheckbox) field;
					value = (checkbox.isChecked()) ? "1" : "0";

				} else {

					logger.warn("Campo no parseado, clase no tratada: " + field.getClass());
				}


				logger.info(key + ": -" + value + "-");

				hCampos.put(key, value);

			}

			pdDoc.close();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println("Exception: " + sw.toString());
			logger.error("Exception: " + sw.toString());
		}
		return hCampos;
	}


	//metodo correspondiente al api PDFBOX v0.7.2
	public static boolean procesaRegistroTelematico(CRegistroTeletramitacion registroTeletramitacion) {

		logger.debug("Inicio.");

		try {

			String numreg = registroTeletramitacion.getNumreg();
			String pdfFileName = registroTeletramitacion.getPdfFileName();
			Vector anexos = registroTeletramitacion.getAnexos();

			COSDocument document = null;
			InputStream input = new FileInputStream(pdfFileName);
			PDFParser parser = new PDFParser(input);
			parser.parse();
			document = parser.getDocument();
			PDDocument pdDoc = new PDDocument(document);

			if (pdDoc.isEncrypted()) {
				logger.warn("PDF encriptado.");
				try {
					pdDoc.decrypt(""); //password?
				} catch (Exception ex) {
					logger.error("decrypt error. Exception: " + ex.toString());
				}
			}

			PDAcroForm pdForm = pdDoc.getDocumentCatalog().getAcroForm();
			logger.debug("pdForm: " + pdForm);
			try {
				List lFields = pdForm.getFields();
				_hPDFields = new Hashtable();

				Iterator iterator = lFields.iterator();
				while (iterator.hasNext()) {
					PDField field = (PDField) iterator.next();


					if (field == null) {
						continue;
					}


					String key = field.getFullyQualifiedName();


					String value = "";
					if (field instanceof PDTextbox) {

						value = (field.getValue() == null) ? "" : field.getValue();


					} else if (field instanceof PDChoiceField) {

						COSDictionary dict = field.getDictionary();
						value = ((COSString) dict.getItem(COSName.getPDFName("V"))).getString();

					} else if (field instanceof PDCheckbox) {

						PDCheckbox checkbox = (PDCheckbox) field;
						value = (checkbox.isChecked()) ? "1" : "0";

					} else {

						logger.warn("Campo no parseado, clase no tratada: " + field.getClass());
					}


					logger.info(key + ": -" + value + "-");
					setCampoPDF(key, value);

				}

				pdDoc.close();

				// Comprobamos si el tecnico y el promotor son obligatorios.
				int idTipoLicencia = new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CTipoLicencia_idTipoLicencia)).intValue();
				boolean obligatorioTecnico = false;
				boolean obligatorioPromotor = false;
				if (idTipoLicencia == CConstantesComando.TIPO_OBRA_MAYOR) {
					obligatorioTecnico = true;
					obligatorioPromotor = true;
				}

				// Validamos los valores del registro. Si falta por rellenar alguno
				// de los campo obligatorios devolvemos el expediente
				if (!okCamposObligatorios(registroTeletramitacion, obligatorioTecnico, obligatorioPromotor)) {
					return false;
				}
				// solicitud que nos llega en el PDF
				CSolicitudLicencia solicitud = getSolicitudPDFile(anexos);
				solicitud.setNumAdministrativo(registroTeletramitacion.getNumreg());
				solicitud.setFecha(registroTeletramitacion.getFechaEntradaRegistro());
				// expediente
				CEstado estado = new CEstado();
				estado.setIdEstado(CConstantesTeletramitacion.ID_ESTADO_INICIAL_VU);
				CExpedienteLicencia expediente = new CExpedienteLicencia(estado);

				expediente.setCNAE((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_cnae));
				// el expediente nos llega desde Ventanilla Unica
				expediente.setVU("1");
				CResultadoOperacion resultado = COperacionesDatabase.crearExpedienteLicencias(solicitud, expediente, null, solicitud.getIdMunicipio());

				return true;


			} catch (Exception ex) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				logger.error("Exception: " + sw.toString());

			}
			input.close();
			document.close();

			return false;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}


	//Se comprueba y ejecuta que el registro sea de aportación de documentos.
	/* Valores de retorno:
		0 -> El registro no es de aportacion de documentos.
		1 -> El registro es de aporatcion de documentos y no esta bloaqueado.
		2 -> El registro es de aportacion de documentos y esta bloqueado.
		*/
    private static int isAportacionDocumentos(String nifSolicitante, Hashtable hCampos, Vector anexos) {
		int isAportacion = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
            if (!formularioAportacionDocumentosOk(hCampos)) return 0;
			isAportacion = 1;
			try {
				//****************************************
				//** Obtener una conexion de la base de datos
				//****************************************************
				connection = CPoolDatabase.getConnection();
				if (connection == null) {
					logger.warn("No se puede obtener la conexión");
					return isAportacion;
				}
				String sql = "select expediente_licencia.id_solicitud, num_expediente, id_estado,  propietario, representante, id_tipo_licencia, bloqueado " +
						"  from expediente_licencia, solicitud_licencia where num_expediente=? and " +
						" solicitud_licencia.id_solicitud= expediente_licencia.id_solicitud";
				statement = connection.prepareStatement(sql);
				String num_expediente = (String) hCampos.get(CConstantesTeletramitacion.CAMPO_NUMERO_EXPEDIENTE);
				statement.setString(1, num_expediente);
				rs = statement.executeQuery();
				/** notificamos que el numero de expediente aportado no existe */
				if (!rs.next()) {
					safeClose(rs, statement, connection);
					logger.info("No existe el numero de expediente: " + num_expediente + " no se puede realizar la aportación de documentación");
					String cnom = crearFichNotif("Expediente inexistente", "No se puede efectuar la aportación de documentación porque el numero de expediente " + num_expediente + " no existe en el sistema.", CConstantesTeletramitacion.VU_NOTIF_CRUTAACC);
					if (num_expediente == null || num_expediente.length() <= 0) {
						COperacionesDatabase.insertarNotifTelematico(null, nifSolicitante, cnom, "Aportación de documentación denegada", "Acuse Recibo", "Expediente inexistente", CConstantesTeletramitacion.VU_NOTIF_CRUTAACC, "No se puede efectuar la aportación de documentación porque el número de expediente " + num_expediente + " no existe en el sistema.", "GeoPISTA");
					} else {
						long secuencia = COperacionesDatabase.insertarNotifTelematico(num_expediente, nifSolicitante, cnom, "Aportación de documentación denegada", "Acuse Recibo", "Expediente inexistente", CConstantesTeletramitacion.VU_NOTIF_CRUTAACC, null, "GeoPISTA");
					}
					return isAportacion;
				}

				/** Generamos el campo cproc */
				/** NOTA: Ventanilla Unica, no es multilenguaje. */
				int idTipoLicencia = rs.getInt("id_tipo_licencia");
				String cproc = "GeoPISTA";
				if (idTipoLicencia == CConstantesComando.TIPO_OBRA_MAYOR) {
					cproc += ". Licencia de Obra Mayor.";
				} else if (idTipoLicencia == CConstantesComando.TIPO_OBRA_MENOR) {
					cproc += ". Licencia de Obra Menor.";
				} else if (idTipoLicencia == CConstantesComando.TIPO_ACTIVIDAD) {
					cproc += ". Licencia de Actividad Calificada.";
				} else if (idTipoLicencia == CConstantesComando.TIPO_ACTIVIDAD_NO_CALIFICADA) {
					cproc += ". Licencia de Actividad No Calificada.";
				}

                /** Comprobamos que el expediente no este bloqueado.
                 * Solo se comprueba el bloqueo del expediente en la aportacion de documentos, ya
                 * que las 2 operaciones que se pueden realizar desde ventanilla unica son:
                 * 1.- Creación de expediente
                 * 2.- aportacion de documentacion de un expediente ya existente (necesita comprobacion)*/

                String bloqueado= rs.getString("bloqueado");
                if ((bloqueado != null) && (bloqueado.equalsIgnoreCase("1"))) {
                    return 2;
                }
                
				//comprobamos si el estado es de mejora o de aportación de alegaciones
				int iIdEstado = rs.getInt("id_estado");
				if (iIdEstado != CConstantesComando.ESTADO_MEJORA_DATOS && iIdEstado != CConstantesComando.ESTADO_ESPERA_ALEGACIONES) {
					safeClose(rs, statement, connection);
					logger.info("El expediente: " + num_expediente + " no se encuntra en nigún estado que permita aportar documentacion. Id Estado: " + iIdEstado);
					String cnom = crearFichNotif("Aportación de documentación denegada", "<p>No se puede efectuar la aportación de documentación porque</p><p>el estado actual del expediente <b>" + num_expediente + "</b> no lo permite.", CConstantesTeletramitacion.VU_NOTIF_CRUTAACC);
					long secuencia = COperacionesDatabase.insertarNotifTelematico(num_expediente, nifSolicitante, cnom, "Aportación de documentación denegada", "Acuse Recibo", "Estado del expediente incorrecto", CConstantesTeletramitacion.VU_NOTIF_CRUTAACC, null, cproc);
					return isAportacion;
				}

				/** comprobamos si el usuario tiene permiso para aporta la documentación.
				 * Si es el propietario o el representante. */
				String sIdPropietario = rs.getString("propietario");
				String sIdRepresentante = rs.getString("representante");
				long lIdSolicitud = rs.getLong("id_solicitud");
				sql = "select dni_cif from persona_juridico_fisica where id_persona = ? or id_persona=?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, sIdPropietario);
				statement.setString(2, sIdRepresentante);
				rs = statement.executeQuery();
				boolean isPersonaAutorizada = false;
				while (rs.next()) {
					/** el nifSolicitante, corresponde a la persona que ha enviado por teletramitacion la
					 * aportación de documentacion. (Puede ser el titular o el representante).*/
					if (nifSolicitante.equalsIgnoreCase(rs.getString("dni_cif")))
						isPersonaAutorizada = true;
				}
				if (!isPersonaAutorizada) {
					safeClose(rs, statement, connection);
					logger.info("El solicitante de la aportación de documentación con dni: " + nifSolicitante + " no tiene permiso para solicitar esta información");
					String cnom = crearFichNotif("Aportación de documentación denegada", "<p>No se puede efectuar la aportación de documentación porque</p><p>el solicitante con dni: <b>" + nifSolicitante + "</b> no esta autorizado para modificar los datos </p><p>del expediente con numero<b>" + num_expediente + "</b>", CConstantesTeletramitacion.VU_NOTIF_CRUTAACC);
					long secuencia = COperacionesDatabase.insertarNotifTelematico(num_expediente, nifSolicitante, cnom, "Aportación de documentación denegada", "Acuse Recibo", "Estado del expediente incorrecto", CConstantesTeletramitacion.VU_NOTIF_CRUTAACC, null, cproc);
					return isAportacion;
				}


				/** Si ha ido bien, añadimos la mejora o la alegacion.  */
				if (iIdEstado == CConstantesComando.ESTADO_MEJORA_DATOS) {
					/*
					Mejora mejora = new Mejora(lIdSolicitud,num_expediente, "Mejora realizada a través de Teletramitación por "+nifSolicitante);
					mejora.setAnexos(anexos);
					COperacionesDatabase.insertarMejora(connection,mejora);
					*/
					/** Solo hay una mejora, pero en una version anterior podia haber varias mejoras. */
					Vector mejoras = COperacionesDatabase.getMejorasSolicitud(connection, lIdSolicitud);
					if ((mejoras != null) && (mejoras.size() > 0)) {
						/** cogemos la mejora actual */
						Mejora mejora = (Mejora) mejoras.get(0);
						Vector totalAnexos = mejora.getAnexos();
						/** Annadimos a la mejora los anexos adjuntados por teletramitacion. */
						for (int i = 0; i < anexos.size(); i++) {
							CAnexo anexo = (CAnexo) anexos.get(i);
							anexo.setObservacion("Teletramitación");
							/** por defecto */
							CTipoAnexo tipoAnexo = new CTipoAnexo();
							tipoAnexo.setIdTipoAnexo(0);
							anexo.setTipoAnexo(tipoAnexo);
							totalAnexos.add(totalAnexos.size(), anexo);
						}
						mejora.setAnexos(totalAnexos);
						//mejora.setAnexos(anexos);
						COperacionesDatabase.modificaAnexosSolicitud(lIdSolicitud, mejora.getAnexos(), mejora, null);
						CExpedienteLicencia expe = new CExpedienteLicencia();
						expe.setNumExpediente(num_expediente);
						expe.setIdSolicitud(lIdSolicitud);
						expe.setEstado(new CEstado(iIdEstado));
						COperacionesDatabase.insertEventoExpediente(expe, "Mejora de datos insertada por teletramitación.", true, false);
						logger.info("Datos de mejora insertados con exito por el nif: " + nifSolicitante);
					}
				}

				if (iIdEstado == CConstantesComando.ESTADO_ESPERA_ALEGACIONES) {
					/** Suponemos que solo existe una alegacion */
					Alegacion alegacion = COperacionesDatabase.getAlegacion(connection, num_expediente);
					if (alegacion != null) {
						Vector totalAnexos = alegacion.getAnexos();
						/** Annadimos a la alegacion los anexos adjuntados por teletramitacion. */
						for (int i = 0; i < anexos.size(); i++) {
							CAnexo anexo = (CAnexo) anexos.get(i);
							anexo.setObservacion("Teletramitación");
							/** por defecto */
							CTipoAnexo tipoAnexo = new CTipoAnexo();
							tipoAnexo.setIdTipoAnexo(0);
							anexo.setTipoAnexo(tipoAnexo);
							totalAnexos.add(totalAnexos.size(), anexo);
						}
						alegacion.setAnexos(totalAnexos);
						//alegacion.setAnexos(anexos);
						COperacionesDatabase.modificaAnexosSolicitud(lIdSolicitud, alegacion.getAnexos(), null, alegacion);
						CExpedienteLicencia expe = new CExpedienteLicencia();
						expe.setNumExpediente(num_expediente);
						expe.setIdSolicitud(lIdSolicitud);
						expe.setEstado(new CEstado(iIdEstado));
						COperacionesDatabase.insertEventoExpediente(expe, "Alegación insertada por teletramitación.", true, false);
						logger.info("Datos de alegación insertados con éxito por el nif: " + nifSolicitante);
					}
				}
				safeClose(rs, statement, connection);
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				safeClose(rs, statement, connection);
				logger.error("Exception: " + sw.toString());
			}
		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
		return isAportacion;
	}

	private static Vector getAnexosRegistroTelematico(String numreg) {

		logger.debug("Inicio.");
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;


		try {

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnnection("teletramitacion");
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();

			}

			logger.info("connection: " + connection);

			String sql = "select a.CNUMERO,c.CRUTAACC,c.CNOM from PVURTINFOREG a,PVURTFICHREG b,PVUAFINFOFICH c where a.CESTADO=0 and a.CNUMERO='" + numreg + "' and a.CNUMERO=b.CNUMREG and b.CROL='Rol_Anexo' and b.CGUID=c.CGUID";

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector anexos = new Vector();
			while (rs.next()) {
				String ruta = rs.getString("CRUTAACC");
				String nombre = rs.getString("CNOM");

				String nombreFichero = ruta + File.separator + nombre;
				logger.info("nombreFichero: " + nombreFichero);

				CAnexo anexo = new CAnexo();
				anexo.setFileName(nombreFichero);

				File dir = new File(ruta + File.separator);
				if (dir.isDirectory()) {
					File[] children = dir.listFiles();
					if (children == null) {
						// Either dir does not exist or is not a directory
					} else {
						for (int i = 0; i < children.length; i++) {
							// Get filename of file or directory
							File file = children[i];
							if (file.isFile()) {
								if (file.getName().equalsIgnoreCase(nombre)) {
									anexo.setFileName(nombre);
									byte[] content = COperacionesDatabase.getBytesFromFile(file);
									anexo.setContent(content);
									anexo.setEstado(CConstantesComando.CMD_ANEXO_ADDED);
									anexos.addElement(anexo);
								}
							}
						}
					}
				}

			}

			safeClose(rs, statement, connection);
			return anexos;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static void setCampoPDF(String nombre, String valor) {
		logger.debug("KEY=" + nombre + " VALOR=" + valor);
		if (!_hPDFields.containsKey(nombre)) {
			_hPDFields.put(nombre, valor);
		}
	}

	public static boolean okCamposObligatorios(CRegistroTeletramitacion registroTeletramitacion, boolean esObligatorioTecnico, boolean esObligatorioPromotor) {
        try{
            // Chequeamos que el usuario haya rellenado los campos obligatorios
            /** Comprobamos los datos obligatorios del propietario */
            _idMunicipio = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_idMunicipio);
            _DNIPropietario = registroTeletramitacion.getNifSolicitante();// (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_dniCif);
            _nombrePropietario = registroTeletramitacion.getNombreSolicitante();//(String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_nombre);
            _tipoViaPropietario = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_tipoVia);
            _nombreViaPropietario = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_nombreVia);
            _numeroViaPropietario = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_numeroVia);
            _cPostalPropietario = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_cpostal);
            _municipioPropietario = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_municipio);
            _provinciaPropietario = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_provincia);

            /** comprobamos via de notificacion para el propietario: siempre se le notifica. Si por email, campo email obligatorio.*/
            int tipoViaNotificacion= new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_CViaNotificacion_idViaNotificacion)).intValue();
            if (tipoViaNotificacion == CConstantesTeletramitacion.patronNotificacionEmail){
                String email= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_email);
                if ((email == null) || (email.trim().length() == 0)) return false;
            }

            if ((_DNIPropietario != null) && (_nombrePropietario != null) && (_tipoViaPropietario != null) &&
                    (_numeroViaPropietario != null) && (_cPostalPropietario != null) &&
                    (_municipioPropietario != null) && (_provinciaPropietario != null)) {
                if ((_DNIPropietario.trim().length() == 0) || (_nombrePropietario.trim().length() == 0)) return false;

                if ((_tipoViaPropietario.trim().length() == 0) || (_numeroViaPropietario.trim().length() == 0) ||
                        (_numeroViaPropietario.trim().length() == 0) || (_cPostalPropietario.trim().length() == 0) ||
                        (_municipioPropietario.trim().length() == 0) || (_provinciaPropietario.trim().length() == 0))
                    return false;
            } else
                return false;

            /** Consideramos que tiene representante si se ha insertado el DNI en el documento. */
            if (tieneRepresentante()) {
                _nombreRepresentante = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_nombre);
                _tipoViaRepresentante = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_tipoVia);
                _nombreViaRepresentante = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_nombreVia);
                _numeroViaRepresentante = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_numeroVia);
                _cPostalRepresentante = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_cpostal);
                _municipioRepresentante = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_municipio);
                _provinciaRepresentante = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_provincia);

                /** comprobamos via de notificacion para el representante. Si por email, campo email obligatorio. */
                String notificar= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_notificar);
                if (notificar.equalsIgnoreCase("1")){
                    tipoViaNotificacion= new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_CViaNotificacion_idViaNotificacion)).intValue();
                    if (tipoViaNotificacion == CConstantesTeletramitacion.patronNotificacionEmail){
                        String email= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_email);
                        if ((email == null) || (email.trim().length() == 0)) return false;
                    }
                }
                /** Comprobamos los datos obligatorios del representante */
                if ((_DNIRepresentante != null) && (_nombreRepresentante != null) && (_tipoViaRepresentante != null) &&
                        (_numeroViaRepresentante != null) && (_cPostalRepresentante != null) &&
                        (_municipioRepresentante != null) && (_provinciaRepresentante != null)) {

                    if ((_DNIRepresentante.trim().length() == 0) || (_nombreRepresentante.trim().length() == 0)) return false;

                    if ((_tipoViaRepresentante.trim().length() == 0) || (_nombreViaRepresentante.trim().length() == 0) ||
                            (_cPostalRepresentante.trim().length() == 0) || (_municipioRepresentante.trim().length() == 0) ||
                            (_provinciaRepresentante.trim().length() == 0))
                        return false;
                } else
                    return false;
            }

            /** Comprobamos los datos obligatorios del tecnico */
            /** Consideramos que tiene tecnico si se ha insertado el DNI en el documento. */
            if ((esObligatorioTecnico) && (!tieneTecnico())) return false;

            if (tieneTecnico()) {
                if (!leerTecnico()) return false;
            }

            /** Comprobamos los datos obligatorios del promotor */
            /** Consideramos que tiene tecnico si se ha insertado el DNI en el documento. */
            if ((esObligatorioPromotor) && (!tienePromotor())) return false;

            if (tienePromotor()) {
                if (!leerPromotor()) return false;
            }

            /** los checkBox, siempre tendran un valor por defecto. No es necesario comprobar */
            _tipoLicencia = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CTipoLicencia_idTipoLicencia);
            _tipoObra = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CTipoObra_idTipoObra);

            /** Emplazamiento de la obra */
            String tipoViaEmplazamiento = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_tipoViaAfecta);
            String nombreViaEmplazamiento = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_nombreViaAfecta);
            if ((tipoViaEmplazamiento.trim().length() == 0) || (nombreViaEmplazamiento.trim().length() == 0)) return false;

            return true;
        }catch (Exception e){
            return false;
        }
	}


	public static boolean leerTecnico() {

		_DNITecnico = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_dniCif);
		_nombreTecnico = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_nombre);
		_tipoViaTecnico = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_tipoVia);
		_nombreViaTecnico = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_nombreVia);
		_numeroViaTecnico = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_numeroVia);
		_cPostalTecnico = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_cpostal);
		_municipioTecnico = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_municipio);
		_provinciaTecnico = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_provincia);
		if ((_DNITecnico != null) && (_nombreTecnico != null) && (_tipoViaTecnico != null) &&
				(_numeroViaTecnico != null) && (_cPostalTecnico != null) &&
				(_municipioTecnico != null) && (_provinciaTecnico != null)) {
			if ((_DNITecnico.trim().length() == 0) || (_nombreTecnico.trim().length() == 0))
				return false;

			if ((_tipoViaTecnico.trim().length() == 0) || (_nombreViaTecnico.trim().length() == 0) ||
					(_numeroViaTecnico.trim().length() == 0) || (_cPostalTecnico.trim().length() == 0) ||
					(_municipioTecnico.trim().length() == 0) || (_provinciaTecnico.trim().length() == 0))
				return false;
		} else
			return false;

		return true;
	}


	public static boolean leerPromotor() {

		_DNIPromotor = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_dniCif);
		_nombrePromotor = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_nombre);
		_tipoViaPromotor = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_tipoVia);
		_nombreViaPromotor = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_nombreVia);
		_numeroViaPromotor = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_numeroVia);
		_cPostalPromotor = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_cpostal);
		_municipioPromotor = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_municipio);
		_provinciaPromotor = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_provincia);
		if ((_DNIPromotor != null) && (_nombrePromotor != null) && (_tipoViaPromotor != null) &&
				(_numeroViaPromotor != null) && (_cPostalPromotor != null) &&
				(_municipioPromotor != null) && (_provinciaPromotor != null)) {

			if ((_DNIPromotor.trim().length() == 0) || (_nombrePromotor.trim().length() == 0))
				return false;

			if ((_tipoViaPromotor.trim().length() == 0) || (_nombreViaPromotor.trim().length() == 0) ||
					(_numeroViaPromotor.trim().length() == 0) || (_cPostalPromotor.trim().length() == 0) ||
					(_municipioPromotor.trim().length() == 0) || (_provinciaPromotor.trim().length() == 0))
				return false;
		} else
			return false;

		return true;

	}

	public static CSolicitudLicencia getSolicitudPDFile(Vector anexos) {

		CSolicitudLicencia solicitud = new CSolicitudLicencia();
		CTipoLicencia tipoLicencia = new CTipoLicencia();
		int idTipoLicencia = new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CTipoLicencia_idTipoLicencia)).intValue();

        /** Emplazamiento de la obra */
        String tipoViaEmplazamiento= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_tipoViaAfecta);        
        String nombreViaEmplazamiento= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_nombreViaAfecta);
        String numeroViaAfecta= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_numeroViaAfecta);
        String portalViaAfecta= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_portalAfecta);
        String plantaViaAfecta= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_plantaAfecta);
        //String letraViaAfecta= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_letraAfecta);
        String cpostalViaAfecta= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_cpostalAfecta);
        solicitud.setTipoViaAfecta(tipoViaEmplazamiento);
        solicitud.setNombreViaAfecta(nombreViaEmplazamiento);
        solicitud.setNumeroViaAfecta(numeroViaAfecta);
        solicitud.setPortalAfecta(portalViaAfecta);
        solicitud.setPortalAfecta(plantaViaAfecta);
        //solicitud.setLetraAfecta(letraViaAfecta);
        solicitud.setLetraAfecta("");
        solicitud.setCpostalAfecta(cpostalViaAfecta);

		/** Si es una licencia de actividad, comprobamos si es ademas NO calificada. */
		if (idTipoLicencia == CConstantesComando.TIPO_ACTIVIDAD) {
			try {
                String noCalificada = (String) _hPDFields.get(CConstantesTeletramitacion.ActividadNoCalificadaCheckBox);
                if (noCalificada != null) {
                    if (noCalificada.equalsIgnoreCase("1")) idTipoLicencia = CConstantesComando.TIPO_ACTIVIDAD_NO_CALIFICADA;
                }

				DatosActividad datosActividad = new DatosActividad();
				datosActividad.setNumExpedienteObra((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_expediente_obra));
				datosActividad.setAlquiler(new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_expediente_obra_alquiler)).intValue());
                try{
                    datosActividad.setNumeroOperarios(new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_num_operarios)).intValue());
                }catch (Exception e){datosActividad.setNumeroOperarios(-1);}
                try{
                    datosActividad.setAforo(new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_aforo)).intValue());
                }catch(Exception e){datosActividad.setAforo(-1);}
                try{
                    datosActividad.setAlturaTechos(new Double((String)_hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_altura_techos)).doubleValue());
                }catch(Exception e){datosActividad.setAlturaTechos(-1);}

				datosActividad.setDescripcionVentilacion((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_ventilacion));
				datosActividad.setDescripcionAlmacenaje((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_almacenaje));

				solicitud.setDatosActividad(datosActividad);
			} catch (Exception ex) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				logger.error("Exception: " + sw.toString());
			}

		}
		tipoLicencia.setIdTipolicencia(idTipoLicencia);
		solicitud.setTipoLicencia(tipoLicencia);
		CTipoObra tipoObra = new CTipoObra();
		tipoObra.setIdTipoObra(new Integer(_tipoObra).intValue());
		solicitud.setTipoObra(tipoObra);
		solicitud.setAnexos(anexos);
		solicitud.setAsunto((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_asunto));
		solicitud.setMotivo((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_motivo));
		solicitud.setNombreComercial((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_motivo));
		//solicitud.setNumAdministrativo((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_numAdministrativo));
		//solicitud.setCodigoEntrada((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_codigoEntrada));
		solicitud.setUnidadTramitadora((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_unidadTramitadora));
		solicitud.setUnidadDeRegistro((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_unidadDeRegistro));
		//solicitud.setFechaEntrada(new Date());//getDate((String)_hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_fecha)));
        Vector referenciasCatastrales= new Vector();
        CReferenciaCatastral referenciaCatastral= new CReferenciaCatastral();
        String ref= (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_referenciaCatastral);
        if ((ref != null) && (!ref.equalsIgnoreCase(""))){
            referenciaCatastral.setReferenciaCatastral(ref);
            referenciasCatastrales.add(referenciaCatastral);
        }
        solicitud.setReferenciasCatastrales(referenciasCatastrales);
		double tasa = 0.00;
		try {
			tasa = new Double((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_tasa)).doubleValue();
		} catch (Exception ex) {
		}
		solicitud.setTasa(tasa);
		solicitud.setTipoViaAfecta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_tipoViaAfecta));
		solicitud.setNombreViaAfecta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_nombreViaAfecta));
		solicitud.setNumeroViaAfecta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_numeroViaAfecta));
		solicitud.setPortalAfecta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_portalAfecta));
		solicitud.setPlantaAfecta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_plantaAfecta));
		//solicitud.setLetraAfecta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_letraAfecta));
        solicitud.setLetraAfecta("");
		solicitud.setCpostalAfecta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_cpostalAfecta));
		solicitud.setMunicipioAfecta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_municipioAfecta));
		solicitud.setProvinciaAfecta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_provinciaAfecta));
		solicitud.setObservaciones((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_observaciones));

		CPersonaJuridicoFisica propietario = new CPersonaJuridicoFisica();
		propietario.setDniCif(_DNIPropietario);
		propietario.setNombre(_nombrePropietario);
		/*
		propietario.setApellido1((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_apellido1));
		propietario.setApellido2((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_apellido2));
		*/
		CDatosNotificacion datosNotificacion = new CDatosNotificacion();
		CViaNotificacion viaNotificacion = new CViaNotificacion();
		if (_hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_CViaNotificacion_idViaNotificacion) != null) {
			viaNotificacion.setIdViaNotificacion(new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_CViaNotificacion_idViaNotificacion)).intValue());
		}
		datosNotificacion.setViaNotificacion(viaNotificacion);
		datosNotificacion.setDniCif(_DNIPropietario);
		datosNotificacion.setFax((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_fax));
		datosNotificacion.setTelefono((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_telefono));
		datosNotificacion.setMovil((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_movil));
		datosNotificacion.setEmail((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_email));
		datosNotificacion.setTipoVia(_tipoViaPropietario);
		datosNotificacion.setNombreVia(_nombreViaPropietario);
		datosNotificacion.setNumeroVia(_numeroViaPropietario);
		datosNotificacion.setPortal((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_portal));
		datosNotificacion.setPlanta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_planta));
		datosNotificacion.setEscalera((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_escalera));
		//datosNotificacion.setLetra((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_letra));
        datosNotificacion.setLetra("");
		datosNotificacion.setCpostal(_cPostalPropietario);
		datosNotificacion.setMunicipio(_municipioPropietario);
		datosNotificacion.setProvincia(_provinciaPropietario);
		datosNotificacion.setNotificar((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_notificar));
		propietario.setDatosNotificacion(datosNotificacion);
		solicitud.setPropietario(propietario);

		CPersonaJuridicoFisica representante = new CPersonaJuridicoFisica();
		if (_isRepresentedBy.equalsIgnoreCase("1")) {
			representante.setDniCif(_DNIRepresentante);
			representante.setNombre(_nombreRepresentante);
			representante.setApellido1((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_apellido1));
			representante.setApellido2((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_apellido2));
			datosNotificacion = new CDatosNotificacion();
			viaNotificacion = new CViaNotificacion();
			if (_hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_CViaNotificacion_idViaNotificacion) != null) {
				viaNotificacion.setIdViaNotificacion(new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_CViaNotificacion_idViaNotificacion)).intValue());
			}
			datosNotificacion.setViaNotificacion(viaNotificacion);
			datosNotificacion.setDniCif(_DNIRepresentante);
			datosNotificacion.setFax((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_fax));
			datosNotificacion.setTelefono((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_telefono));
			datosNotificacion.setMovil((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_movil));
			datosNotificacion.setEmail((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_email));
			datosNotificacion.setTipoVia(_tipoViaRepresentante);
			datosNotificacion.setNombreVia(_nombreViaRepresentante);
			datosNotificacion.setNumeroVia(_numeroViaRepresentante);
			datosNotificacion.setPortal((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_portal));
			datosNotificacion.setPlanta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_planta));
			datosNotificacion.setEscalera((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_escalera));
			//datosNotificacion.setLetra((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_letra));
            datosNotificacion.setLetra("");
			datosNotificacion.setCpostal(_cPostalRepresentante);
			datosNotificacion.setMunicipio(_municipioRepresentante);
			datosNotificacion.setProvincia(_provinciaRepresentante);
			datosNotificacion.setNotificar((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_notificar));
			representante.setDatosNotificacion(datosNotificacion);
			solicitud.setRepresentante(representante);
		}

		CPersonaJuridicoFisica tecnico = new CPersonaJuridicoFisica();
        if (_tieneTecnico.equalsIgnoreCase("1")){
            tecnico.setDniCif(_DNITecnico);
            tecnico.setNombre(_nombreTecnico);
            tecnico.setApellido1((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_apellido1));
            tecnico.setApellido2((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_apellido2));
            /*
            tecnico.setColegio((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_colegio));
            tecnico.setVisado((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_visado));
            tecnico.setTitulacion((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_titulacion));
            */
            tecnico.setColegio("");
            tecnico.setVisado("");
            tecnico.setTitulacion("");
            datosNotificacion = new CDatosNotificacion();
            viaNotificacion = new CViaNotificacion();
            if (_hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_CViaNotificacion_idViaNotificacion) != null) {
                viaNotificacion.setIdViaNotificacion(new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_CViaNotificacion_idViaNotificacion)).intValue());
            }

            datosNotificacion.setViaNotificacion(viaNotificacion);
            datosNotificacion.setDniCif(_DNITecnico);
            datosNotificacion.setFax((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_fax));
            datosNotificacion.setTelefono((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_telefono));
            datosNotificacion.setMovil((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_movil));
            datosNotificacion.setEmail((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_email));
            datosNotificacion.setTipoVia(_tipoViaTecnico);
            datosNotificacion.setNombreVia(_nombreViaTecnico);
            datosNotificacion.setNumeroVia(_numeroViaTecnico);
            datosNotificacion.setPortal((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_portal));
            datosNotificacion.setPlanta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_planta));
            datosNotificacion.setEscalera((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_escalera));
            //datosNotificacion.setLetra((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_letra));
            datosNotificacion.setLetra("");
            datosNotificacion.setCpostal(_cPostalTecnico);
            datosNotificacion.setMunicipio(_municipioTecnico);
            datosNotificacion.setProvincia(_provinciaTecnico);
            datosNotificacion.setNotificar((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_notificar));
            tecnico.setDatosNotificacion(datosNotificacion);
            solicitud.ponTecnico(tecnico);
        }

		CPersonaJuridicoFisica promotor = new CPersonaJuridicoFisica();
        if (_tienePromotor.equalsIgnoreCase("1")){
            promotor.setDniCif(_DNIPromotor);
            promotor.setNombre(_nombrePromotor);
            promotor.setApellido1((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_apellido1));
            promotor.setApellido2((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_apellido2));
            /*
            promotor.setColegio((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_colegio));
            promotor.setVisado((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_visado));
            promotor.setTitulacion((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_titulacion));
            */
            promotor.setColegio("");
            promotor.setVisado("");
            promotor.setTitulacion("");

            datosNotificacion = new CDatosNotificacion();
            viaNotificacion = new CViaNotificacion();
            if (_hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_CViaNotificacion_idViaNotificacion) != null) {
                viaNotificacion.setIdViaNotificacion(new Integer((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_CViaNotificacion_idViaNotificacion)).intValue());
            }
            datosNotificacion.setViaNotificacion(viaNotificacion);
            datosNotificacion.setDniCif(_DNIPromotor);
            datosNotificacion.setFax((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_fax));
            datosNotificacion.setTelefono((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_telefono));
            datosNotificacion.setMovil((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_movil));
            datosNotificacion.setEmail((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_email));
            datosNotificacion.setTipoVia((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_tipoVia));
            datosNotificacion.setNombreVia((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_nombreVia));
            datosNotificacion.setNumeroVia(_numeroViaPromotor);
            datosNotificacion.setPortal((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_portal));
            datosNotificacion.setPlanta((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_planta));
            datosNotificacion.setEscalera((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_escalera));
            //datosNotificacion.setLetra((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_letra));
            datosNotificacion.setLetra("");
            datosNotificacion.setCpostal(_cPostalPromotor);
            datosNotificacion.setMunicipio(_municipioPromotor);
            datosNotificacion.setProvincia(_provinciaPromotor);
            datosNotificacion.setNotificar((String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_notificar));
            promotor.setDatosNotificacion(datosNotificacion);
            solicitud.setPromotor(promotor);
        }
		solicitud.setIdMunicipio(_idMunicipio);
		return solicitud;
	}

	public static boolean tieneRepresentante() {
		/** Leemos los campos que hacen referencia al representante */
		/** Comprobamos que los campos obligatorios estan rellenados. Si ninguno de ellos lo esta,
		 *  el propietario no tiene representante. Si falta alguno de ellos, consideramos que el
		 * propietario tiene representante y que por tanto faltan por rellenar datos.
		 */
		_DNIRepresentante = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_dniCif);

		/** si se inserta el DNI consideramos que el propietario tiene representante. */
		if (_DNIRepresentante.trim().length() == 0)
			return false;
		else {
			_isRepresentedBy = "1";
			return true;
		}
	}


	public static boolean tieneTecnico(){
		_DNITecnico = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_dniCif);
        /** si se inserta el DNI consideramos que el propietario tiene tecnico. */
        if (_DNITecnico.trim().length() == 0)
            return false;
        else {
            _tieneTecnico = "1";
            return true;
        }
	}

	public static boolean tienePromotor() {
		_DNIPromotor = (String) _hPDFields.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_dniCif);
		/** si se inserta el DNI consideramos que el propietario tiene promotor. */
		if (_DNIPromotor.trim().length() == 0)
			return false;
		else {
			_tienePromotor = "1";
			return true;
		}
	}


	public static String crearFichNotif(String sTitle, String sTexto, String ruta) {
		// creamos el fichero de notificacion
		try {
			/** comprobamos que exista la ruta. */
			File f = new File(ruta);
			if (!f.exists()) {
				f.mkdirs();
			}

			String cnom = getNomFichNotif();
			String fichNotif = ruta + File.separator + cnom;
			Writer stream = new FileWriter(fichNotif, true);
			String datos = "<HTML>\n" +
					"<head>\n" +
					"\t<title>" + sTitle + "</title>\n" +
					"</head>\n" +
					"<body>\n" +
					"<div style=\"position:absolute;left:15px;top:10px;font-family:Verdana;font-size:10pt;\">\n" +
					sTexto +
					"</div>\n" +
					"</body>\n" +
					"</HTML>";
			stream.write(datos);
			stream.close();

			return cnom;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

			return null;
		}

	}

	public static boolean esDate(String fecha) {
		try {
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public static Date getDate(String fecha) {
		try {
			if (esDate(fecha))
				return new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getNomFichNotif() {
		try {
			Thread.sleep(5);
		} catch (Exception ex) {
		}

		long sequence = new Date().getTime();
		return "NOTIF_" + sequence + ".html";
	}


	private static boolean safeClose(ResultSet rs, Statement statement, Connection connection) {
		try {
			connection.rollback();
		} catch (Exception ex2) {
		}
		try {
			rs.close();
		} catch (Exception ex2) {
		}
		try {
			statement.close();
		} catch (Exception ex2) {
		}
		try {
			connection.close();
			CPoolDatabase.releaseConexion();
		} catch (Exception ex2) {
		}

		return true;
	}


    public static boolean formularioSolicitudOk(Hashtable hCampos){
        /** Comprobamos para cada tipo de licencias que su formulario correspondiente ES el formulario,
         y que el cuidadano no ha manipulado el formulario o ha enviado otro formulario. */
        if (hCampos != null){
            int idTipoLicencia= -1;
            try{
                idTipoLicencia= new Integer((String) hCampos.get(CConstantesTeletramitacion.CSolicitudLicencia_CTipoLicencia_idTipoLicencia)).intValue();
            }catch (Exception e){
                return false;
            }

            if ((idTipoLicencia == CConstantesComando.TIPO_OBRA_MAYOR) || (idTipoLicencia == CConstantesComando.TIPO_OBRA_MENOR)){
                if ((hCampos.size() == CConstantesTeletramitacion.NUM_CAMPOS_FORMULARIO_LICENCIAS_OBRA) &&
                    existenCamposSolicitud(hCampos) && existenCamposPersonasJuridicas(hCampos)){
                    return true;
                }else return false;
            }else if (idTipoLicencia == CConstantesComando.TIPO_ACTIVIDAD){
                if ((hCampos.size() == CConstantesTeletramitacion.NUM_CAMPOS_FORMULARIO_LICENCIAS_ACTIVIDAD) &&
                    existenCamposSolicitud(hCampos) && existenCamposPersonasJuridicas(hCampos) &&
                    existenCamposActividad(hCampos)){
                    return true;
                }else return false;
            }else return false;
        }else return false;
    }


    public static boolean existenCamposSolicitud(Hashtable hCampos){
        if (hCampos.containsKey(CConstantesTeletramitacion.RegistroTelematico_GEOPISTA) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencias_Nota) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CTipoLicencia_idTipoLicencia) &&
	        hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CTipoObra_idTipoObra) &&
	        hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_idMunicipio) &&
            //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_numAdministrativo) &&
            //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_codigoEntrada) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_unidadTramitadora) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_unidadDeRegistro) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_referenciaCatastral) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_motivo) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_asunto) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_fecha) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_fechaEntrada) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_tasa) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_tipoViaAfecta) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_nombreViaAfecta) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_numeroViaAfecta) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_portalAfecta) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_plantaAfecta) &&
            //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_letraAfecta) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_cpostalAfecta) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_municipioAfecta) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_provinciaAfecta) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_observaciones)) return true;
        else return false;
    }

    public static boolean existenCamposPersonasJuridicas(Hashtable hCampos){
        /** Comprobamos el tipo de notificacion. Que los valores no sean erroneos. */
        try{
            int tipoViaNotificacion= new Integer((String) hCampos.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_CViaNotificacion_idViaNotificacion)).intValue();
        }catch(Exception e){return false;}
        try{
            int tipoViaNotificacion= new Integer((String) hCampos.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_CViaNotificacion_idViaNotificacion)).intValue();
            String notificar= (String) hCampos.get(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_notificar);
        }catch(Exception e){return false;}

       if( hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_dniCif) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_nombre) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_apellido1) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_apellido2) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_CViaNotificacion_idViaNotificacion) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_fax) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_telefono) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_movil) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_email) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_tipoVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_nombreVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_numeroVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_portal) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_planta) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_escalera) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_letra) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_cpostal) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_municipio) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_provincia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_propietario_CDatosNotificacion_notificar) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_dniCif) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_nombre) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_apellido1) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_apellido2) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_CViaNotificacion_idViaNotificacion) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_fax) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_telefono) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_movil) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_email) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_tipoVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_nombreVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_numeroVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_portal) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_planta) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_escalera) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_letra) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_cpostal) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_municipio) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_representante_CDatosNotificacion_provincia) &&

           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_dniCif) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_nombre) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_apellido1) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_apellido2) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_colegio) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_visado) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_titulacion) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_CViaNotificacion_idViaNotificacion) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_fax) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_telefono) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_movil) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_email) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_tipoVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_nombreVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_numeroVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_portal) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_planta) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_escalera) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_letra) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_cpostal) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_municipio) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_provincia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_tecnico_CDatosNotificacion_notificar) &&

           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_dniCif) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_nombre) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_apellido1) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_apellido2) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_colegio) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_visado) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_titulacion) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_CViaNotificacion_idViaNotificacion) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_fax) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_telefono) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_movil) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_email) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_tipoVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_nombreVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_numeroVia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_portal) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_planta) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_escalera) &&
           //hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_letra) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_cpostal) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_municipio) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_provincia) &&
           hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_CPersonaJuridicoFisica_promotor_CDatosNotificacion_notificar)) return true;
        else return false;
    }

    public static boolean existenCamposActividad(Hashtable hCampos){
        if (hCampos.containsKey(CConstantesTeletramitacion.Actividad2NoCalificadaLabel) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_cnae) &&
            hCampos.containsKey(CConstantesTeletramitacion.ActividadNoCalificadaCheckBox) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_expediente_obra) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_expediente_obra_alquiler) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_num_operarios) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_aforo) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_altura_techos) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_ventilacion) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_almacenaje_label) &&
            hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_almacenaje)) return true;
        else return false;
    }


    public static boolean formularioAportacionDocumentosOk(Hashtable hCampos){
        if (hCampos != null){
            if (hCampos.containsKey(CConstantesTeletramitacion.CAMPO_NUMERO_EXPEDIENTE) &&
                hCampos.containsKey(CConstantesTeletramitacion.RegistroTelematico_GEOPISTA) &&
                hCampos.containsKey(CConstantesTeletramitacion.CSolicitudLicencia_idMunicipio)) return true;
            else return false;
        }else return false;
    }
    

}
