package com.gestorfip.ws.servidor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Properties;


import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.gestorfip.ws.beans.configuracion.CRSGestor;
import com.gestorfip.ws.beans.configuracion.ConfiguracionGestor;
import com.gestorfip.ws.beans.configuracion.VersionesUER;
import com.gestorfip.ws.beans.tramite.ui.CaracteresDeterminacionBean;
import com.gestorfip.ws.beans.tramite.ui.CondicionUrbanisticaCasoBean;
import com.gestorfip.ws.beans.tramite.ui.CondicionUrbanisticaCasoRegimenRegimenesBean;
import com.gestorfip.ws.beans.tramite.ui.CondicionUrbanisticaCasoRegimenesBean;
import com.gestorfip.ws.beans.tramite.ui.DeterminacionBean;
import com.gestorfip.ws.beans.tramite.ui.EntidadBean;
import com.gestorfip.ws.beans.tramite.ui.FipBean;
import com.gestorfip.ws.beans.tramite.ui.TipoOperacionDeterminacionBean;
import com.gestorfip.ws.beans.tramite.ui.TipoOperacionEntidadBean;
import com.gestorfip.ws.beans.tramite.ui.TramiteBean;
import com.gestorfip.ws.utils.Constants;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerBean;
import com.gestorfip.ws.xml.beans.tramite.unidad.UnidadBean;
import com.gestorfip.ws.xml.parser.FIP1Handler;


public class Services {
	
	private final static String CARATERDETERMINACION_UNIDAD= "caracterDeterminacion_unidad";

	static Logger logger = Logger.getLogger(Services.class);
	

	public int obtenerValorSecuencia (String nombreSecuencia){
		
		return OperacionesBBDD.obtenerValorSecuenciaCliente(nombreSecuencia);
	}
	

 
	/***************************************************************************************
	 * 							FICHEROS
	 **************************************************************************************/
	/**
	 * 
	 * @param nombreFich, nombre utilizado como patron de busqueda
	 * @return FicherosFipBean objeto con el nombre y la fecha del fichero
	 * @throws Exception
	 */


	/***************************************************************************************
	 * 							FIN FICHEROS
	 **************************************************************************************/
	

	
		
	
	
	/**
	 * Obtiene el tramite que es el planeamiento encargado para trabajar con el
	 * @param idFip
	 * @return
	 * @throws Exception
	 */
	public static TramiteBean obtenerTramitesEncargadoAsociadosFip(int idFip) throws Exception{

		TramiteBean tramite = null;  
			
		tramite = OperacionesBBDD.obtenerTramitesEncargadoAsociadosFip(idFip);
			   
			return tramite;
		
	}
	 
	/** 
	 * 
	 * @param idTramite id del tramite
	 * @return TramiteBean objeto con los datos del tramite.
	 * @throws Exception
	 */
	public static TramiteBean obtenerDatosTramite(int idTramite) throws Exception{

		TramiteBean tramiteBean = null;
		
		tramiteBean = OperacionesBBDD.obtenerDatosTramite(idTramite);
		 
		return tramiteBean;

	}
	
	/** 
	 * 
	 * @param idTramite id del tramite
	 * @return TramiteBean objeto con los datos del tramite.
	 * @throws Exception
	 */
	public static FipBean obtenerDatosFip(int idFip) throws Exception{

		FipBean fipBean = null;
		
		fipBean = OperacionesBBDD.obtenerDatosFip(idFip);
		 
		return fipBean;

	}
	

	/** 
	 * 
	 * @param idTramite id del tramite
	 * @return TramiteBean objeto con los datos del tramite.
	 * @throws Exception
	 */
	public static FipBean obtenerDatosIdentificacionFip(int idFip) throws Exception{

		FipBean fipBean = null;
		
		fipBean = OperacionesBBDD.obtenerDatosIdentificacionFip(idFip);
		 
		return fipBean;
	}
	

	
	/***************************************************************************************
	 * 							DETERMINACIONES
	 **************************************************************************************/
	
	/**
	 * 
	 * @param idFip identificador del fip
	 * @return TipoOperacionesDeterminacionesBean objeto con los tipos de operaciones de determinaciones
	 * para el fip
	 * @throws Exception
	 */
	public static TipoOperacionDeterminacionBean[] 
	                   obtenerLstTiposOperacionesDeterminaciones(int idFip) throws Exception{

		TipoOperacionDeterminacionBean[] lstTipoOperacionesDeterminacionBean = null;
		
		lstTipoOperacionesDeterminacionBean = 
			OperacionesBBDD.obtenerLstTiposOperacionDeterminaciones(idFip, logger);

		return lstTipoOperacionesDeterminacionBean;

	} 

	
	/** 
	 * 
	 * @param idTramite id del tramite
	 * @return DeterminacionBean objeto con los datos de las determinaciones.
	 * @throws Exception
	 */
	public static DeterminacionBean[] obtenerDeterminacionesAsociadasFip(int idTramite) throws Exception{

		
		DeterminacionBean[] determinacionBean = null;
		
		determinacionBean = OperacionesBBDD.obtenerDeterminacionesAsociadasFip(idTramite);
		 
		return determinacionBean;

	} 
	
	
	/** 
	 * 
	 * @param idTramite id del tramite
	 * @return DeterminacionBean objeto con los datos de las determinaciones.
	 * @throws Exception
	 */
	public static DeterminacionBean[] obtenerArbolDeterminacionesAsociadasTramite(int idTramite) throws Exception{

		
		DeterminacionBean[] determinacionBean = null;
		
		determinacionBean = OperacionesBBDD.obtenerArbolDeterminacionesAsociadasTramite(idTramite);
		 
		return determinacionBean;

	} 

	
	/***************************************************************************************
	 * 							FIN DETERMINACIONES
	 **************************************************************************************/

	
	/** 
	 * 
	 * @param idTramite id del tramite
	 * @return EntidadBean objeto con los datos de las entidades.
	 * @throws Exception
	 */
	public static EntidadBean[] obtenerEntidadesAsociadasFip(int idTramite) throws Exception{

		EntidadBean[] entidadBean = null;
		
		//entidadBean = OperacionesBBDD.obtenerEntidadesAsociadasFip_GestorPlaneamiento(idTramite);
		 
		return entidadBean;

	}
	
	public static EntidadBean[] obtenerArbolEntidadesAsociadasTramite(int idTramite) throws Exception{

		EntidadBean[] entidadBean = null;
		
		entidadBean = OperacionesBBDD.obtenerArbolEntidadesAsociadasTramite(idTramite);
		 
		return entidadBean;

	}
	
	/**
	 * 
	 * @param idFip identificador del fip
	 * @return TipoOperacionesDeterminacionesBean objeto con los tipos de operaciones de determinaciones
	 * para el fip
	 * @throws Exception
	 */
	public static TipoOperacionEntidadBean[] 
	                   obtenerLstTiposOperacionesEntidades(int idFip) throws Exception{

		TipoOperacionEntidadBean[] lstTipoOperacionesEntidadBean = null;
		
		lstTipoOperacionesEntidadBean = 
			OperacionesBBDD.obtenerLstTiposOperacionEntidades(idFip, logger);

		return lstTipoOperacionesEntidadBean;

	} 
	
	
	
	/***************************************************************************************
	 * 							FIN ENTIDADES
	 **************************************************************************************/
	

	/***************************************************************************************
	 * 							DETERMINACIONES-ENTIDADES
	 **************************************************************************************/
	
	
	public static EntidadBean[] obtenerEntidadesAsociadasToDeterminacion(int idDeterminacion){
		EntidadBean[] lstEntidades = null;
		
		lstEntidades = OperacionesBBDD.obtenerEntidadesAsociadasToDeterminacion(idDeterminacion);
		
		return lstEntidades;
	}
	
	

	
	public static DeterminacionBean[] obtenerDetAplicablesEntidad(int idEntidad){
		
		DeterminacionBean[] lstDeterminaciones = null;
		
		lstDeterminaciones = OperacionesBBDD.obtenerDetAplicablesEntidad(idEntidad);
		
		return lstDeterminaciones;
	}
	
	public static DeterminacionBean[] obtenerDetByTipoCaracterCondUrban(
			int idFip, int idTramiteCatalogoSistematizado, int idTramitePlaneamientoVigente,
			int idTramitePlaneamientoEncargado,	String caracterDeterminacionProperty){
		DeterminacionBean[] lstDeterminaciones = null;
		
		lstDeterminaciones = OperacionesBBDD.obtenerDeterminacionesByTipoCaracterDeterminacionEnCondicionesUrbanisticas(
				idFip, idTramiteCatalogoSistematizado, idTramitePlaneamientoVigente,
				idTramitePlaneamientoEncargado, caracterDeterminacionProperty);
		
		return lstDeterminaciones;
	}

	public static DeterminacionBean[] obtenerDetValoresReferenciaCondicionUrbanistica(
			int idTramiteCatalogoSistematizado, int idTramitePlaneamientoVigente,
			int idTramitePlaneamientoEncargado,	DeterminacionBean[] lstDeterminaciones){
		
		DeterminacionBean[] lstDeterminacionesValRef = null;
		
		lstDeterminacionesValRef = OperacionesBBDD.obtenerDetValoresReferenciaCondicionUrbanistica(
				idTramiteCatalogoSistematizado,  idTramitePlaneamientoVigente,
				idTramitePlaneamientoEncargado, lstDeterminaciones);
		
		return lstDeterminacionesValRef;
	}
	
	
	/***************************************************************************************
	 * 							FIN ASOCIACION DETERMINACIONES-ENTIDADES
	 * @throws Exception 
	 **************************************************************************************/

	private static FIP1Handler parseoXML (String xmlFichFIP, String encoding) throws Exception{
		FIP1Handler handler = null;
		InputStream inputStream = null;
		long before = System.currentTimeMillis();
	
		InputSource is;
		try {
			is = new InputSource( new ByteArrayInputStream(xmlFichFIP.getBytes(encoding)) );
			inputStream = Services.class.getClassLoader()
					.getResourceAsStream(Constants.GESTORFIP_PROPERTIES_FILE);
			
		} catch (UnsupportedEncodingException e1) {
			logger.error("Error al obterner el fichero de propiedades ");
			throw new Exception(Constants.OTHER_ERROR_CODE + "Configuration file not found");
		}
		
		try {

			if (inputStream == null) {
				inputStream = ClassLoader
						.getSystemResourceAsStream(Constants.GESTORFIP_PROPERTIES_FILE);
				if (inputStream == null) {
					logger.error("Configuration file not found.");
					throw new Exception(Constants.OTHER_ERROR_CODE + "Configuration file not found");
				}
			}
			
			Properties props = new Properties();
			props.load(inputStream);
			
			handler = new FIP1Handler();
			
			// Parsing del documento
			logger.info("Parsing file ...");
			Class c = Class.forName("org.apache.xerces.parsers.SAXParser");
			XMLReader reader = (XMLReader) c.newInstance();
			reader.setContentHandler(handler);
			reader.parse(is);

			long diff = System.currentTimeMillis() - before;
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(diff);
			logger.info("File successfully parsed [" + ca.get(Calendar.MINUTE) + "m " + ca.get(Calendar.SECOND) + "s " + ca.get(Calendar.MILLISECOND) + "ms]");
		}
		catch (SAXParseException e) {
			logger.error("Exception when parsing file at line "
					+ e.getLineNumber() + " , column " + e.getColumnNumber()
					+ " : ");
			logger.error(e.getMessage());
			throw new Exception(Constants.XML_PARSING_ERROR_CODE + "Exception when parsing "
					+ "file at line " + e.getLineNumber()
					+ " , column " + e.getColumnNumber() + " : "
					+ e.getMessage());
		}
		return handler;
	}
	
	
	
	/***************************************************************************************
	 * 							INICIO LOURED
	 * @return 
	 **************************************************************************************/

	
	public String obtenerFechaFipXML(String xmlFichFIP , String encoding) throws Exception{
		
		FIP1Handler handler = parseoXML(xmlFichFIP, encoding);
		
		return handler.getFip1().getFecha();
	}
	
	
	
	public static  ConfLayerBean[] obtenerDatosMigracionAsistida(String xmlFichFIP , String encoding) throws Exception{
		FIP1Handler handler = parseoXML(xmlFichFIP, encoding);
		ConfLayerBean[] lstConfLayerBean = handler.getFip1().initDataConfLayers();
		
		handler.getFip1().obtenerDatosDeterminacinoesMigracionAsistida(lstConfLayerBean);
		
		handler.getFip1().obtenerDatosUsosRegulacionesMigracionsAsistida(lstConfLayerBean);
		
		return lstConfLayerBean;
	}
	
	/**
	 * Valida y inserta el contenido del fichero FIP1 en la BD
	 * @param xmlFichFIP: Fichero Fip1
	 * @return String: response code with the associated error message
	 * @throws Exception
	 */
	public String insertarFIP1(String xmlFichFIP, int idAmbito, 
			String nameFileFip, int idMunicipioLG, ConfLayerBean[] lstConfLayerBean , String encoding, int idEntidad)throws Exception {
		logger.info("Inicio Insertar Fip ");
		try {
			FIP1Handler handler = parseoXML(xmlFichFIP, encoding);
			
			return OperacionesBBDD.insertarFIP1(idAmbito, handler, idMunicipioLG, lstConfLayerBean, idEntidad);
			
		} catch (Exception e) {
			logger.error("Exception when inserting FIP1 data into DB : ");
			logger.error(e.getMessage());
			return Constants.DB_CONNEXION_ERROR_CODE + e.getMessage();
		}

	}
	

	public String obtenerFechaConsolidacionFip(int idAmbito)throws Exception{
		
		String fechaConsolidacionFip = null;
		
		
		fechaConsolidacionFip = OperacionesBBDD.obtenerFechaConsolidacionFip(idAmbito);
		

		return fechaConsolidacionFip;
	}
	
	public FipBean[] obtenerLstFips(String codAmbito)throws Exception{
		
		FipBean[] fip = new FipBean[1];
		fip[0] =  OperacionesBBDD.obtenerUltimoFip(codAmbito);
		
		return fip;
	}
	
	/** 
	 * 
	 * @param idFip id del fip
	 * @return DeterminacionBean objeto con los datos de las determinaciones.
	 * @throws Exception
	 */
	public static DeterminacionBean[] obtenerDeterminacionesAsociadasFip_GestorPlaneamiento(int idFip, int idTramite) throws Exception{

		
		DeterminacionBean[] determinacionBean = null;
		
		determinacionBean = OperacionesBBDD.obtenerDeterminacionesAsociadasFip_GestorPlaneamiento(idFip, idTramite);
		 
		return determinacionBean;

	} 
	
	/** 
	 * 
	 * @param idFip id del fip
	 * @return EntidadBean objeto con los datos de las entidades.
	 * @throws Exception
	 */
	public static EntidadBean[] obtenerEntidadesAsociadasFip_GestorPlaneamiento(int idFip, int idTramite) throws Exception{

		EntidadBean[] entidadBean = null;
	
		entidadBean = OperacionesBBDD.obtenerEntidadesAsociadasFip_GestorPlaneamiento(idFip, idTramite);
	
		return entidadBean;

	}
	
	
	public static DeterminacionBean[] obtenerDeterminacionesAsociadasToEntidad(int idEntidad){
		DeterminacionBean[] lstDeterminaciones = null;
		
		lstDeterminaciones = OperacionesBBDD.obtenerDeterminacionesAsociadasToEntidad(idEntidad);
		
		return lstDeterminaciones;
	}
	
	public static CondicionUrbanisticaCasoBean[] obtenerCasosCondicionUrbanistica(int idDeterminacion, int idEntidad){
		CondicionUrbanisticaCasoBean[] lstcuc = null;
		
		lstcuc = OperacionesBBDD.obtenerCasosCondicionUrbanistica(idDeterminacion, idEntidad);
		
		return lstcuc;
	}
	

	public static CondicionUrbanisticaCasoRegimenesBean[] obtenerRegimenesCasoCondicionUrbanistica(int idCaso){
		CondicionUrbanisticaCasoRegimenesBean[] lstCUCR = null;
		
		lstCUCR = OperacionesBBDD.obtenerRegimenesCasoCondicionUrbanistica(idCaso);
		
		return lstCUCR;
	}

	public static CondicionUrbanisticaCasoRegimenRegimenesBean obtenerRegimenEspecificoRegimenCasoCondicionUrbanistica(int idRegimen){
		CondicionUrbanisticaCasoRegimenRegimenesBean cucrr = null;
		
		cucrr = OperacionesBBDD.obtenerRegimenEspecificoRegimenCasoCondicionUrbanistica(idRegimen);
		
		return cucrr;
	}
	
	public static DeterminacionBean obtenerDatosDeterminacion(int idDet){
		DeterminacionBean det = null;
		
		det = OperacionesBBDD.obtenerDatosDeterminacion(idDet);
		
		return det;
	}

	
	/** 
	 * 
	 * @return CaracteresDeterminacionBean[], lista de caracteres de deteminaciones.
	 * @throws Exception
	 */
	public static CaracteresDeterminacionBean[] obtenerCaracteresDeterminaciones() throws Exception{

		CaracteresDeterminacionBean[] lstCaracteresDeterminacion = null;
		
		lstCaracteresDeterminacion = OperacionesBBDD.obtenerCaracteresDeterminaciones();
		 
		return lstCaracteresDeterminacion;

	}
	
	public static String obtenerAmbitoTrabajo(int idMunicipio) throws Exception{
		String ambitosGestorFip = null;
		
		ambitosGestorFip = OperacionesBBDD.obtenerAmbitoTrabajo(idMunicipio);
		
		return ambitosGestorFip;
	}
	
	public VersionesUER[] obtenerVersionesConsolaUER()throws Exception{
		VersionesUER[] lstVersionesUER = OperacionesBBDD.obtenerVersionesConsolaUER();
		return lstVersionesUER;	
	}
	
	public CRSGestor[] obtenerCRS()throws Exception{
		CRSGestor[] lstCrs = OperacionesBBDD.obtenerCRS();
		return lstCrs;	
	}
	
	public ConfiguracionGestor obtenerConfigVersionConsolaUER()throws Exception{
		ConfiguracionGestor configGestor = OperacionesBBDD.obtenerConfigVersionConsolaUER();
		return configGestor;	
	}
	
	public boolean guardarConfiguracionGestorFip(ConfiguracionGestor configGestor) throws Exception{
		
		OperacionesBBDD.guardarConfiguracionGestorFip(configGestor);
		
		return true;
	}
	
	public UnidadBean obtenerUnidadDeterminacion(int idDet) throws Exception{
		
		return OperacionesBBDD.obtenerUnidadDeterminacion(idDet);
		
	}

}

