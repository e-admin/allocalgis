/**
 * AFirmaManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.dnie.afirma;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.soap.SOAPFaultException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class AFirmaManager {

	private static final String AFIRMA_PROPERTIES = "/geopista.properties";

	protected final String ID_APLICACION_LABEL_NAME = "#idAplicacion";
	protected final String DATOS_FIRMADOS_LABEL_NAME = "#datosFirmados";
	protected final String DATOS_NO_FIRMADOS_LABEL_NAME = "#datosNoFirmados";
	protected final String DATOS_HASH_LABEL_NAME = "#datosHash";
	protected final String FORMATO_FIRMA_LABEL_NAME = "#formatoFirma";
	protected final String ALGORITMO_HASH_LABEL_NAME = "#algoritmoHash";
	protected final String CERTIFICADO_LABEL_NAME = "#certificado";
	protected final String MODO_VALIDACION_LABEL_NAME = "#modoValidacion";
	protected final String OBTENER_INFO_LABEL_NAME = "#obtenerInfo";

	protected final String AFIRMA_MANAGER_APLICACION = "Aplicacion";
	protected final String AFIRMA_MANAGER_TRUSTSTORE = "TrustStore";

	// Plantilla validacion certificado
	protected final String AFIRMA_MANAGER_END_POINT_CERT_PROPERTY = "ValidarCertificadoEndPoint";
	protected final String AFIRMA_MANAGER_NAME_CERT_PROPERTY = "ValidarCertificadoOperationName";
	protected final String AFIRMA_MANAGER_METHOD_CERT_PROPERTY = "ValidarCertificadoOperation";
	protected final String AFIRMA_MANAGER_PET_CERTIFICADO = "PlantillaValidarCertificado";
	protected final String AFIRMA_MANAGER_MODO_VALIDACION = "ValidarCertificadoModo";
	protected final String AFIRMA_MANAGER_OBTENER_INFO = "ValidarCertificadoObtenerInfo";

	private Properties properties;

	
	/**
	 * Logger
	 */
	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AFirmaManager.class);
    	
	public AFirmaManager() {
		properties = null;
	}
	
	/**
	 * Validar un certificado
	 * 
	 * @param strCertificado Certificado a validar formateado en Base 64
	 * @return Objeto con los datos de validación del certificado
	 * @exception AFirmaException cuando se produce algun error durante la comunicación con la plataforma @Firma
	 */
	public DatosValidacionCertificadoOut validarCertificado(String strCertificado) throws AFirmaException {
		String strPeticion = null;
		String strXmlResult = null;
		Service service = null;
		Call call = null;
		QName qName = null;
		DatosValidacionCertificadoOut returnDatos = null;

		log.info("Validando el certificado");
		log.debug("Certificado: " + strCertificado);

		try {
			strPeticion = prepararPeticionCertificado(strCertificado);

			log.debug("PETICION=" + strPeticion);

			initSecurity();
			service = new Service();            
			call = (Call) service.createCall();

			log.debug("End point de validacion:"+ getPropertyValue(AFIRMA_MANAGER_END_POINT_CERT_PROPERTY));
			call.setTargetEndpointAddress(getPropertyValue(AFIRMA_MANAGER_END_POINT_CERT_PROPERTY));
			
			log.debug("QName:"+ getPropertyValue(AFIRMA_MANAGER_NAME_CERT_PROPERTY) + " " + getPropertyValue(AFIRMA_MANAGER_METHOD_CERT_PROPERTY));
			qName = new QName(getPropertyValue(AFIRMA_MANAGER_NAME_CERT_PROPERTY),     
					getPropertyValue(AFIRMA_MANAGER_METHOD_CERT_PROPERTY));
			call.setOperationName(qName);

			strXmlResult =(String)call.invoke(new Object[] {strPeticion});
			log.debug("Satec: Resultado:" + strXmlResult);
			
			returnDatos = getDatosFromRespuestaCertificado(strXmlResult);

		} catch(ServiceException se){
			log.error(se);
			throw new AFirmaException("Error al invocar el servicio de validación @firma: " + se.getMessage());
		} catch(RemoteException re){
			log.error(re);
			throw new AFirmaException("Error al invocar el servicio de validación @firma: " + re.getMessage());
		} catch(JAXRPCException jaxrpce){
			log.error(jaxrpce);
			throw new AFirmaException("Error al invocar el servicio de validación @firma: " + jaxrpce.getMessage());
		} catch(SOAPFaultException sfe){
			log.error(sfe);
			throw new AFirmaException("Error al invocar el servicio de validación @firma: " + sfe.getMessage());
		}

		return returnDatos;
    }    

	/*
	 * Prepara la peticion de validación de certificado a enviar a la plataforma @Firma
	 * 
	 * @param _str_cert_validar parte publica del certificado en Base 64
	 * @return String con la peticion.
	 * @throws AFirmaException 
	 */
	private String prepararPeticionCertificado(String strCertificado) throws AFirmaException {
		String strPeticion = null;
        
		strPeticion = getPlantillaPeticion(AFIRMA_MANAGER_PET_CERTIFICADO);
		strPeticion = strPeticion.replaceFirst(ID_APLICACION_LABEL_NAME, getPropertyValue(AFIRMA_MANAGER_APLICACION));
		strPeticion = strPeticion.replaceFirst(CERTIFICADO_LABEL_NAME, strCertificado);
		strPeticion = strPeticion.replaceFirst(MODO_VALIDACION_LABEL_NAME, getPropertyValue(AFIRMA_MANAGER_MODO_VALIDACION));
		strPeticion = strPeticion.replaceFirst(OBTENER_INFO_LABEL_NAME, getPropertyValue(AFIRMA_MANAGER_OBTENER_INFO));

        return strPeticion;
    }

	private String getPlantillaPeticion(String _Str_propertyName) throws AFirmaException {
		String Str_ruta = null;
		String Str_peticion = null;

		log.info("Obteniendo la plantilla de la petición");
		try {
			Str_ruta = getPropertyValue(_Str_propertyName);
			log.debug("Ruta de la plantilla: " + Str_ruta);
			Str_peticion = getResourceAsString(Str_ruta);
			log.debug("Plantilla obtenida: " + Str_peticion);
		}
		catch (IOException e) {
			log.error("Error al cargar la plantilla de petición", e);
			throw new AFirmaException("Error al cargar la plantilla de petición");
		}

		return Str_peticion;
	}    

	private static String getResourceAsString(String uri) throws IOException {
		InputStream is = AFirmaManager.class.getResourceAsStream(uri);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int count;
		byte[] bytes = new byte[0x100]; 
		while((count=is.read(bytes))!=-1) {
			stream.write(bytes, 0, count);
		}
		is.close();
		stream.close();
		return new String(stream.toByteArray()); 
	}

	public void initSecurity() throws AFirmaException {
    	System.setProperty("javax.net.ssl.trustStore", getPropertyValue(AFIRMA_MANAGER_TRUSTSTORE));
//        System.setProperty("javax.net.ssl.trustStorePassword", "satec08");
//        System.setProperty("javax.net.ssl.trustStoreType", "jks");
        
//        System.setProperty("javax.net.ssl.keyStore", "C:\\Archivos de programa\\LocalGIS\\apache-tomcat-6.0.14\\keystore");
//        System.setProperty("javax.net.ssl.keyStorePassword", "satec08");
//        System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
	}

	/**
	 * Devuelve el valor de una propiedad
	 * 
	 * @param _Str_propertyName Nombre de la propiedad
	 * @return Valor de la propiedad
	 * @exception AFirmaException cuando la propiedad no puede ser leida
	 */
	protected String getPropertyValue(String _Str_propertyName) throws AFirmaException {
		String Str_return = null;

		if (properties == null) {
			InputStream is = AFirmaManager.class.getResourceAsStream(AFIRMA_PROPERTIES);
			properties = new Properties();
			try {
				properties.load(is);
			} catch (IOException e) {
				log.error("No se ha podido cargar el fichero de propiedades");
				throw new AFirmaException("No se ha podido cargar el fichero de propiedades");
			}
		}
		Str_return = (String)properties.get(_Str_propertyName);
		if (null == Str_return) {
			log.error("No se encontró la propiedad " + _Str_propertyName);
			throw new AFirmaException("No se encontró la propiedad " + _Str_propertyName);
		}

		return Str_return;
	}

	private DatosValidacionCertificadoOut getDatosFromRespuestaCertificado(String _Str_xml) {
		final String USO_CERTIFICADO = "usoCertificado";
		final String APELLIDOS_RESPONSABLE = "ApellidosResponsable";
		final String VALIDO_HASTA = "validoHasta";
		final String POLITICA = "politica";
		final String SUBJECT = "subject";
		final String TIPO_CERTIFICADO = "tipoCertificado";
		final String VERSION_POLITICA = "versionPolitica";
		final String ORGANIZACION_EMISORA = "OrganizacionEmisora";
		final String ID_POLITICA = "idPolitica";
		final String NIF_RESPONSABLE = "NIFResponsable";
		final String NIF_CIF = "NIF-CIF";
		final String NUMERO_SERIE = "numeroSerie";
		final String NOMBRE_RESPONSABLE = "nombreResponsable";
		final String ID_EMISOR = "idEmisor";
		final String EMAIL = "email";
		final String CLASIFICACION = "clasificacion";
		final String VALIDO_DESDE = "validoDesde";
		final String NOMBRE_APELLIDOS_RESPONSABLE = "NombreApellidosResponsable";
		final String SEGUNDO_APELLIDO_RESPONSABLE = "segundoApellidoResponsable";
		final String PRIMER_APELLIDO_RESPONSABLE = "primerApellidoResponsable";
		final String RAZON_SOCIAL_FIELD = "razonSocial";
		final String RESULTADO = "resultado";
		final String DESCRIPCION = "descripcion";
		final String CODIGO_RESULTADO = "codigoResultado";
		final String DESC_RESULTADO = "descResultado";
		final String EXCEPCION = "excepcion";
		
		DatosValidacionCertificadoOut returnDatos = null;
		DatosCertificadoOut certificado = null; 

		certificado = new DatosCertificadoOut();
		certificado.setApellidosResponsable(getCertificadoNodeValue(_Str_xml, APELLIDOS_RESPONSABLE));
		certificado.setClasificacion(getCertificadoNodeValue(_Str_xml, CLASIFICACION));
		certificado.setEmail(getCertificadoNodeValue(_Str_xml, EMAIL));
		certificado.setIdEmisor(getCertificadoNodeValue(_Str_xml, ID_EMISOR));
		certificado.setIdPolitica(getCertificadoNodeValue(_Str_xml, ID_POLITICA));
		certificado.setNIFResponsable(getCertificadoNodeValue(_Str_xml, NIF_RESPONSABLE));
		certificado.setNIFResponsable(getCertificadoNodeValue(_Str_xml, NIF_CIF));
		certificado.setNombreApellidosResponsable(getCertificadoNodeValue(_Str_xml, NOMBRE_APELLIDOS_RESPONSABLE));
		certificado.setNombreResponsable(getCertificadoNodeValue(_Str_xml, NOMBRE_RESPONSABLE));
		certificado.setNumeroSerie(getCertificadoNodeValue(_Str_xml, NUMERO_SERIE));
		certificado.setOrganizacionEmisora(	getCertificadoNodeValue(_Str_xml, ORGANIZACION_EMISORA));
		certificado.setPolitica(getCertificadoNodeValue(_Str_xml, POLITICA));
		certificado.setPrimerApellidoResponsableORazonSocial(getCertificadoNodeValue(_Str_xml, PRIMER_APELLIDO_RESPONSABLE));
		certificado.setPrimerApellidoResponsableORazonSocial(getCertificadoNodeValue(_Str_xml, RAZON_SOCIAL_FIELD));
		certificado.setSegundoApellidoResponsable(getCertificadoNodeValue(_Str_xml, SEGUNDO_APELLIDO_RESPONSABLE));
		certificado.setSubject(getCertificadoNodeValue(_Str_xml, SUBJECT));
		certificado.setTipoCertificado(getCertificadoNodeValue(_Str_xml, TIPO_CERTIFICADO));
		certificado.setUsoCertificado(getCertificadoNodeValue(_Str_xml, USO_CERTIFICADO));
		certificado.setValidoDesde(DatosCertificadoOut.getDate(getCertificadoNodeValue(_Str_xml, VALIDO_DESDE)));
		certificado.setValidoHasta(DatosCertificadoOut.getDate(getCertificadoNodeValue(_Str_xml, VALIDO_HASTA)));
		certificado.setVersionPolitica(getCertificadoNodeValue(_Str_xml, VERSION_POLITICA));

		returnDatos = new DatosValidacionCertificadoOut();
		returnDatos.setCertificado(certificado);

		returnDatos.setResultado(getNodeValue(_Str_xml, RESULTADO));
		returnDatos.setDescripcionResultado(getNodeValue(_Str_xml, DESCRIPCION));

		returnDatos.setCodigoResultado(getNodeValue(_Str_xml, CODIGO_RESULTADO));
		/** SATEC: no se actualiza correctamente el campo <descripcion> en el objeto de tipo DefaultDatosValidacionCertificadoOut */
//		returnDatos.setDescripcion(getNodeValue(_Str_xml, DESC_RESULTADO));
		returnDatos.setDescripcion(getNodeValue(_Str_xml, DESCRIPCION));
		returnDatos.setExceptionResultado(getNodeValue(_Str_xml, EXCEPCION));

		return returnDatos;
	}     

	private String getNodeValue(String _Str_xml, String _Str_nodeName) {
		final String NODE_INIT = "<";
		final String NODE_END = "</";
		final String NODE_LAST = ">";
		int int_init = 0;
		int int_end = 0;
		String Str_return = "";
		String Str_nodeInit = null;
		String Str_nodeEnd = null;

		Str_nodeInit = NODE_INIT + _Str_nodeName + NODE_LAST;
		Str_nodeEnd = NODE_END + _Str_nodeName + NODE_LAST;

		int_init = _Str_xml.indexOf(Str_nodeInit);// busca <nombre>
		int_end = _Str_xml.indexOf(Str_nodeEnd);// busca </nombre>
		if (-1 != int_init && -1 != int_end) {
			Str_return = _Str_xml.substring(int_init + Str_nodeInit.length(),
					int_end);
		}

		return Str_return;
	}

	private String getCertificadoNodeValue(String _Str_xml, 
			String _Str_nodeName)
	{
		final String NODE_INIT = "<idCampo>";
		final String NODE_END = "</idCampo>";
		final String NODE_VALOR_INIT = "<valorCampo>";
		final String NODE_VALOR_END = "</valorCampo>";
		int int_init = 0;
		int int_end_init = 0;
		int int_end_end = 0;        
		String Str_return = "";
		String Str_nodeInit = null;

//		<idCampo>politica</idCampo><valorCampo>1.3.6.1.4.1.5734.3.5</valorCampo>        
		Str_nodeInit = NODE_INIT + _Str_nodeName + NODE_END;

		int_init = _Str_xml.indexOf(Str_nodeInit);// busca <idCampo>nombre_campo</idCampo>
		if (-1 != int_init) {
			int_end_init = _Str_xml.indexOf(NODE_VALOR_INIT, int_init);// busca <valorCampo>
			if (-1 != int_end_init) {
				int_end_end = _Str_xml.indexOf(NODE_VALOR_END, int_end_init);// busca </valorCampo>

				Str_return = _Str_xml.substring(
						int_end_init + NODE_VALOR_INIT.length(), 
						int_end_end);
			}
		}

		return Str_return;
	}    
}
