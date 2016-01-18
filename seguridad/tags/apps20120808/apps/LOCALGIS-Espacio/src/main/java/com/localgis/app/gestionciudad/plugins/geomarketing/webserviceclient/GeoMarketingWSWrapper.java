package com.localgis.app.gestionciudad.plugins.geomarketing.webserviceclient;


import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import com.geopista.app.AppContext;
import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GeoMarketingOT2;
import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingData;
import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingDataResponse;
import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElementsResponse;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

/*
 *  GeoMarketingWSTest Junit test case
 */

public class GeoMarketingWSWrapper{

	private final static String geoMarketingEndPoint = "TOMCATURL/LocalGISGeoWebServices/services/GeoMarketingWS/";



	public static com.localgis.webservices.geomarketing.client.GeoMarketingWSStub geoStub = null;
	
	public static com.localgis.webservices.geomarketing.client.GeoMarketingWSStub getGeoMarktingStub(){
		if (geoStub == null){
			try {
				
				String baseDirectory = "";
				String localgisBaseDir = AppContext.getApplicationContext().getString("ruta.base.mapas");
				if (localgisBaseDir!=null && !localgisBaseDir.equals("")){
					baseDirectory = localgisBaseDir;
				}

				
				geoStub =
					new com.localgis.webservices.geomarketing.client.GeoMarketingWSStub(
							geoMarketingEndPoint.replaceFirst(
									"TOMCATURL", 
									AppContext.getApplicationContext().getUserPreference(AppContext.URL_TOMCAT, "", true)
							)
					);
			} catch (AxisFault e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return geoStub;
	}


	public static GeoMarketingOT2 getGeomarketingData(String geometryWKT, String srid, int idEntidad, int[] ageRanges, String userName) throws java.lang.Exception{

		try{



			com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingData getGeomarketingData12=
				(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingData)getTestObject(GetGeomarketingData.class);

			if (geometryWKT!= null && !geometryWKT.equals("")){
				getGeomarketingData12.setWktGeometry(geometryWKT);
			}
			if (srid!=null && !srid.equals("") && !srid.equals("0")){
				getGeomarketingData12.setSrid(srid);
			}
			if (idEntidad > 0){
				getGeomarketingData12.setIdEntidad(idEntidad);
			}
			if (ageRanges!=null && ageRanges.length>0){
				getGeomarketingData12.setRanges(ageRanges);
			}
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!=null && lastPassword!=null && !lastPassword.equals("") && !userName.equals("")){
				getGeomarketingData12.setUserName(userName);
				getGeomarketingData12.setPassword(lastPassword);
			}


			GetGeomarketingDataResponse response = getGeoMarktingStub().getGeomarketingData(getGeomarketingData12);

			return response.get_return();

		}catch (AxisFault e) {
			e.printStackTrace();
			if (e.getCause() instanceof java.net.ConnectException){
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"Conexión Rechazada", "Error de conexión con el servidor de WebServices: Conexión Rechazada.No hay conexión con el servidor.", StringUtil.stackTrace(e));
			} else if (e.getCause() instanceof java.net.SocketTimeoutException){
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			} else{
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"WebService Fault", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			}
			return null;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return null;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	//	public  void getGeomarketingData() throws java.lang.Exception{
	//		com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub stub = new com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub();
	//		com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub.GetGeomarketingData getGeomarketingData12=
	//			(com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub.GetGeomarketingData)getTestObject(com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub.GetGeomarketingData.class);
	//
	//
	//		stub.startgetGeomarketingData(
	//				getGeomarketingData12,
	//				new tempCallbackN65548()
	//		);
	//
	//
	//
	//	}

	//	private class tempCallbackN65548  extends com.localgis.webservicesclient.geomarketing.GeoMarketingWSCallbackHandler{
	//		public tempCallbackN65548(){ super(null);}
	//
	//		public void receiveResultgetGeomarketingData(
	//				com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub.GetGeomarketingDataResponse result
	//		) {
	//
	//		}
	//
	//		public void receiveErrorgetGeomarketingData(java.lang.Exception e) {
	//			fail();
	//		}
	//
	//	}

	public static GeoMarketingOT2 getGeomarketingAndElementsData(String geometryWKT, String srid, int idlayer, int idEntidad, int[] ageRanges, String userName, int idMunicipio) throws java.lang.Exception{
		try{

			com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsData getGeomarketingAndElementsData14=
				(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsData)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsData.class);

			if (geometryWKT!= null && !geometryWKT.equals("")){
				getGeomarketingAndElementsData14.setWktGeometry(geometryWKT);
			}
			if (srid!=null && !srid.equals("") && !srid.equals("0")){
				getGeomarketingAndElementsData14.setSrid(srid);
			}
			if (idlayer > 0){
				getGeomarketingAndElementsData14.setIdLayer(idlayer);
			}
			if (idEntidad > 0){
				getGeomarketingAndElementsData14.setIdEntidad(idEntidad);
			}
			if (ageRanges!=null && ageRanges.length>0){
				getGeomarketingAndElementsData14.setRanges(ageRanges);
			}
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!=null && lastPassword!=null && !lastPassword.equals("") && !userName.equals("")){
				getGeomarketingAndElementsData14.setUserName(userName);
				getGeomarketingAndElementsData14.setPassword(lastPassword);
			}

			if (idMunicipio>0){
				getGeomarketingAndElementsData14.setIdMunicipio(idMunicipio);
			}

			return getGeoMarktingStub().getGeomarketingAndElementsData(getGeomarketingAndElementsData14).get_return();

		}catch (AxisFault e) {
			e.printStackTrace();
			if (e.getCause() instanceof java.net.ConnectException){
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"Conexión Rechazada", "Error de conexión con el servidor de WebServices: Conexión Rechazada.No hay conexión con el servidor.", StringUtil.stackTrace(e));
			} else if (e.getCause() instanceof java.net.SocketTimeoutException){
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			} else{
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"WebService Fault", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			}
			return null;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return null;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static GeoMarketingOT2[] getGeomarketingAndElementsDataFromLayerGroupByAttribute(int idlayer, int idEntidad, int[] ageRanges, 
			String userName, int idMunicipio, String attributeName, 
			int idGeoLayer, String locale) throws java.lang.Exception{
		try{

			com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeName getGeomarketingAndElementsData14=
				(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeName)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeName.class);

			if (idlayer > 0){
				getGeomarketingAndElementsData14.setIdLayerElements(idlayer);
			}
			if (idEntidad > 0){
				getGeomarketingAndElementsData14.setIdEntidad(idEntidad);
			}
			if (ageRanges!=null && ageRanges.length>0){
				getGeomarketingAndElementsData14.setRanges(ageRanges);
			}
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!=null && lastPassword!=null && !lastPassword.equals("") && !userName.equals("")){
				getGeomarketingAndElementsData14.setUserName(userName);
				getGeomarketingAndElementsData14.setPassword(lastPassword);
			}
			
			if (attributeName!= null && !attributeName.equals("")){
				getGeomarketingAndElementsData14.setAttributeName(attributeName);
			}
			
			if (locale!=null && !locale.equals("")){
				getGeomarketingAndElementsData14.setLocale(locale);
			}
			if(idGeoLayer != 0)
				getGeomarketingAndElementsData14.setIdLayer(idGeoLayer);
//			if (idMunicipio>0){
//				getGeomarketingAndElementsData14.setIdMunicipio(idMunicipio);
//			}

			return getGeoMarktingStub().getGeomarketingAndElementsDataByIdLayerAndAttributeName(getGeomarketingAndElementsData14).get_return();

		}catch (AxisFault e) {
			e.printStackTrace();
			if (e.getCause() instanceof java.net.ConnectException){
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"Conexión Rechazada", "Error de conexión con el servidor de WebServices: Conexión Rechazada.No hay conexión con el servidor.", StringUtil.stackTrace(e));
			} else if (e.getCause() instanceof java.net.SocketTimeoutException){
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			} else{
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"WebService Fault", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			}
			return null;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return null;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//	public static GeoMarketingOT2 testgetGeomarketingAndElementsData(String geometryWKT, String srid, int idlayer, int idEntidad, int[] ageRanges, String userName, int idMunicipio) throws java.lang.Exception{
	//		try{
	//
	//			com.localgis.webservices.client.geomarketing.GeoMarketingWSStub stub =
	//				new com.localgis.webservices.client.geomarketing.GeoMarketingWSStub(geoMarketingEndPoint.replaceFirst(
	//						"TOMCATURL", 
	//						AppContext.getApplicationContext().getUserPreference(AppContext.URL_TOMCAT, "", true)
	//				));
	//
	//			com.localgis.webservices.client.geomarketing.GeoMarketingWSStub.GetGeomarketingAndElementsData getGeomarketingAndElementsData14=
	//				(com.localgis.webservices.client.geomarketing.GeoMarketingWSStub.GetGeomarketingAndElementsData)getTestObject(com.localgis.webservices.client.geomarketing.GeoMarketingWSStub.GetGeomarketingAndElementsData.class);
	//
	//			if (geometryWKT!= null && !geometryWKT.equals("")){
	//				getGeomarketingAndElementsData14.setWktGeometry(geometryWKT);
	//			}
	//			if (srid!=null && !srid.equals("") && !srid.equals("0")){
	//				getGeomarketingAndElementsData14.setSrid(srid);
	//			}
	//			if (idlayer > 0){
	//				getGeomarketingAndElementsData14.setIdLayer(idlayer);
	//			}
	//			if (idEntidad > 0){
	//				getGeomarketingAndElementsData14.setIdEntidad(idEntidad);
	//			}
	//			if (ageRanges!=null && ageRanges.length>0){
	//				getGeomarketingAndElementsData14.setRanges(ageRanges);
	//			}
	//			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
	//			if (userName!=null && lastPassword!=null && !lastPassword.equals("") && !userName.equals("")){
	//				getGeomarketingAndElementsData14.setUserName(userName);
	//				getGeomarketingAndElementsData14.setPassword(lastPassword); 
	//			}
	//
	//			if (idMunicipio>0){
	//				getGeomarketingAndElementsData14.setIdMunicipio(idMunicipio);
	//			}
	//
	//			return stub.getGeomarketingAndElementsData(getGeomarketingAndElementsData14).get_return();
	//
	//		}catch (AxisFault e) {
	//			e.printStackTrace();
	//			if (e.getCause() instanceof java.net.ConnectException){
	//				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
	//						"Conexión Rechazada", "Error de conexión con el servidor de WebServices: Conexión Rechazada.No hay conexión con el servidor.", StringUtil.stackTrace(e));
	//			} else if (e.getCause() instanceof java.net.SocketTimeoutException){
	//				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
	//						"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
	//			} else{
	//				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
	//						"WebService Fault", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
	//			}
	//			return null;
	//		}catch (java.net.SocketTimeoutException e) {
	//			e.printStackTrace();
	//			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
	//					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
	//			return null;
	//		} catch (java.lang.Exception e) {
	//			e.printStackTrace();
	//			return null;
	//		}
	//	}



	//	public  void testStartgetGeomarketingAndElementsData() throws java.lang.Exception{
	//		com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub stub = new com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub();
	//		com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub.GetGeomarketingAndElementsData getGeomarketingAndElementsData14=
	//			(com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub.GetGeomarketingAndElementsData)getTestObject(com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub.GetGeomarketingAndElementsData.class);
	//
	//
	//		stub.startgetGeomarketingAndElementsData(
	//				getGeomarketingAndElementsData14,
	//				new tempCallbackN65618()
	//		);
	//
	//
	//
	//	}

	//	private class tempCallbackN65618  extends com.localgis.webservicesclient.geomarketing.GeoMarketingWSCallbackHandler{
	//		public tempCallbackN65618(){ super(null);}
	//
	//		public void receiveResultgetGeomarketingAndElementsData(
	//				com.localgis.webservicesclient.geomarketing.GeoMarketingWSStub.GetGeomarketingAndElementsDataResponse result
	//		) {
	//
	//		}
	//
	//		public void receiveErrorgetGeomarketingAndElementsData(java.lang.Exception e) {
	//			fail();
	//		}
	//
	//	}


	public static int getNumElements(String geometryWKT, String srid, int idlayer, int idEntidad, String userName, int idMunicipio) throws java.lang.Exception{
		try{
			
			//the default implementation should point to the right endpoint

			com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElements getNumElements16=
				(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElements)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElements.class);

			if (geometryWKT!= null && !geometryWKT.equals("")){
				getNumElements16.setWktGeometry(geometryWKT);
			}
			if (srid!=null && !srid.equals("") && !srid.equals("0")){
				getNumElements16.setSrid(srid);
			}
			if (idlayer > 0){
				getNumElements16.setIdLayer(idlayer);
			}
			if (idEntidad > 0){
				getNumElements16.setIdEntidad(idEntidad);
			}
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!=null && lastPassword!=null && !lastPassword.equals("") && !userName.equals("")){
				getNumElements16.setUserName(userName);
				getNumElements16.setPassword(lastPassword);
			}

			if (idMunicipio>0){
				getNumElements16.setIdMunicipio(idMunicipio);
			}

			GetNumElementsResponse a = getGeoMarktingStub().getNumElements(
					getNumElements16);

			return a.get_return();

		}catch (AxisFault e) {
			e.printStackTrace();
			if (e.getCause() instanceof java.net.ConnectException){
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"Conexión Rechazada", "Error de conexión con el servidor de WebServices: Conexión Rechazada.No hay conexión con el servidor.", StringUtil.stackTrace(e));
			} else if (e.getCause() instanceof java.net.SocketTimeoutException){
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			} else{
				ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
						"WebService Fault", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			}
			return 0;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return 0;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			return 0;
		}

	}




	//Create an ADBBean and provide it as the test object
	@SuppressWarnings("unchecked")
	public static org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
		return (org.apache.axis2.databinding.ADBBean) type.newInstance();
	}




}
