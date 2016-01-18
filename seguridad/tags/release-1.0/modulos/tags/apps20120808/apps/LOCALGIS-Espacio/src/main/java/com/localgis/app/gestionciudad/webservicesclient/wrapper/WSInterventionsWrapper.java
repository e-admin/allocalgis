package com.localgis.app.gestionciudad.webservicesclient.wrapper;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.axis2.AxisFault;

import com.geopista.app.AppContext;
import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.plugins.geomarketing.webserviceclient.GeoMarketingWSWrapper;
import com.localgis.app.gestionciudad.utils.GestionCiudadOperaciones;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.CancelIntervention;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.FinderInterventionConditions;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.FinderNoteConditions;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredInterventionListResponse;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteInterventionListResponse;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListByConditionsResponse;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListByContidionsResponse;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteInterventionListResponse;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumInterventionListByConditionsResponse;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumInterventionsResponse;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.StatisticalDataOT;
import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub;
import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdViaResponse;
import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.PostalDataOT;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;


public class WSInterventionsWrapper{

	public static final String orderToInterventions = "priority:asc;next_warning:asc";
	public static final String endPoint =  "TOMCATURL/LocalGISGeoWebServices/services/CivilWorkWS/";
	private static com.localgis.webservices.civilwork.client.CivilWorkWSStub staticStub = null;
	
	
	public static com.localgis.webservices.civilwork.client.CivilWorkWSStub getCivilworkStub(){
		if (staticStub == null){
			try {
//				String baseDirectory = "";
//				String localgisBaseDir = AppContext.getApplicationContext().getString("ruta.base.mapas");
//				if (localgisBaseDir!=null && !localgisBaseDir.equals("")){
//					baseDirectory = localgisBaseDir;
//				}
				
				staticStub = new com.localgis.webservices.civilwork.client.CivilWorkWSStub(
						endPoint.replaceFirst(
								"TOMCATURL", 
								AppContext.getApplicationContext().getUserPreference(AppContext.URL_TOMCAT, "", true)
								)
						);
			} catch (AxisFault e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return staticStub;
	}

	public  static ArrayList<LocalGISNote> getNoteListByContidions(String userName,int idMunicipio, FinderNoteConditions conditions) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListByContidions getNoteListByContidions104=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListByContidions)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListByContidions.class);

			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				
				getNoteListByContidions104.setUserName(userName);
				getNoteListByContidions104.setPassword(lastPassword);

				conditions.setIdMunicipio(idMunicipio);
				getNoteListByContidions104.setConditions(conditions);
			}

			GetNoteListByContidionsResponse response = getCivilworkStub().getNoteListByContidions(getNoteListByContidions104);

			return FromStubDataWrapperUtils.stubNoteListToLocalGisNoteArrayList(response.get_return());

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
			return new ArrayList<LocalGISNote>();
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISNote>();
		} catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISNote>();
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return new ArrayList<LocalGISNote>();
		}
		
	}


	public  static ArrayList<LocalGISIntervention> getInterventionListByConditions(String userName, int idMunicipio, FinderInterventionConditions conditions) throws java.lang.Exception{
		try{

			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListByConditions getInterventionListByConditions110=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListByConditions)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListByConditions.class);

			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				getInterventionListByConditions110.setUserName(userName);
				getInterventionListByConditions110.setPassword(lastPassword);

				conditions.setIdMunicipio(idMunicipio);
				getInterventionListByConditions110.setConditions(conditions);
			}


			return FromStubDataWrapperUtils.stubInterventionListToLocalGisInterventionArray(getCivilworkStub().getInterventionListByConditions(
					getInterventionListByConditions110).get_return());

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
			return new ArrayList<LocalGISIntervention>();
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISIntervention>();
		} catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISIntervention>();
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return new ArrayList<LocalGISIntervention>();
		}
	}
	
	
	
	
	public static ArrayList<LocalGISIntervention> getRangeOrderInterventions(String userName, int idMunicipio,int page, int range, LayerFeatureBean[] layersFeatures) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListByConditions getCompleteFilteredInterventionList =
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListByConditions)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListByConditions.class);
			
			FinderInterventionConditions conditions = new FinderInterventionConditions();
			
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				getCompleteFilteredInterventionList.setUserName(userName);
				getCompleteFilteredInterventionList.setPassword(lastPassword);

				if (layersFeatures!=null && layersFeatures.length>0){
					conditions.setFeatures(UtilidadesAvisosPanels.buildLayerFeaturesString(layersFeatures));
				}
				
				conditions.setIdMunicipio(idMunicipio);
				conditions.setRange(range);
				conditions.setStartElement(range * page);
				conditions.setOrderColumns(orderToInterventions);
				
				getCompleteFilteredInterventionList.setConditions(conditions);
			}
			
			GetInterventionListByConditionsResponse response = getCivilworkStub().getInterventionListByConditions(
					getCompleteFilteredInterventionList);
			
			return FromStubDataWrapperUtils.stubInterventionListToLocalGisInterventionArray(response.get_return());
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
			return new ArrayList<LocalGISIntervention>();
		}catch (java.net.SocketTimeoutException e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISIntervention>();
		} catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISIntervention>();
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return new ArrayList<LocalGISIntervention>();
		}
	}
	
	public static ArrayList<LocalGISNote> getRangeOrderNotes(String userName, int idMunicipio,int page, int range, LayerFeatureBean[] layersFeatures) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListByContidions getCompleteFilteredInterventionList =
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListByContidions)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListByContidions.class);
			
			FinderNoteConditions conditions = new FinderNoteConditions();
			
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				getCompleteFilteredInterventionList.setUserName(userName);
				getCompleteFilteredInterventionList.setPassword(lastPassword);

				if (layersFeatures!=null && layersFeatures.length>0){
					conditions.setFeatures(UtilidadesAvisosPanels.buildLayerFeaturesString(layersFeatures));
				}
				
				conditions.setIdMunicipio(idMunicipio);
				conditions.setRange(range);
				conditions.setStartElement(range * page);
				
				getCompleteFilteredInterventionList.setConditions(conditions);
			}
			
			GetNoteListByContidionsResponse response = getCivilworkStub().getNoteListByContidions(
					getCompleteFilteredInterventionList);
			
			return FromStubDataWrapperUtils.stubNoteListToLocalGisNoteArrayList(response.get_return());
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
			return new ArrayList<LocalGISNote>();
		}catch (java.net.SocketTimeoutException e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISNote>();
		} catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISNote>();
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return new ArrayList<LocalGISNote>();
		}
	}
	

	public static com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISIntervention2[] getRangeInterventions(String userName, int idMunicipio,int page, int range) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredInterventionList getCompleteFilteredInterventionList94=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredInterventionList)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredInterventionList.class);


			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				getCompleteFilteredInterventionList94.setUserName(userName);
				getCompleteFilteredInterventionList94.setPassword(lastPassword);
				getCompleteFilteredInterventionList94.setIdMunicipio(idMunicipio);

				getCompleteFilteredInterventionList94.setRange(range);
				getCompleteFilteredInterventionList94.setStartElement((page * range));
			}

			GetCompleteFilteredInterventionListResponse response = getCivilworkStub().getCompleteFilteredInterventionList(
					getCompleteFilteredInterventionList94);


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
			return new com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISIntervention2[0];
		}catch (java.net.SocketTimeoutException e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return new com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISIntervention2[0];
		} catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return new com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISIntervention2[0];
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return new com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISIntervention2[0];
		}

	}

	public static ArrayList<LocalGISIntervention> getAllInterventionList(String userName, int idMunicipio) throws java.lang.Exception{

		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteInterventionList getCompleteInterventionList98=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteInterventionList)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteInterventionList.class);

			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				getCompleteInterventionList98.setUserName(userName);
				getCompleteInterventionList98.setPassword(lastPassword);
				getCompleteInterventionList98.setIdMunicipio(idMunicipio);


			}
			GetCompleteInterventionListResponse response = getCivilworkStub().getCompleteInterventionList(getCompleteInterventionList98);

			return FromStubDataWrapperUtils.stubInterventionListToLocalGisInterventionArray(response.get_return());
		
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
			return new ArrayList<LocalGISIntervention>();
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISIntervention>();
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISIntervention>();
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return new ArrayList<LocalGISIntervention>();
		}
	}


	//	public static ArrayList<LocalGISIntervention> getAllInterventionList(String userName, int idMunicipio) throws java.lang.Exception{
	//		try{
	//			com.localgis.webservices.client.civilwork.CivilWorkWSStub stub =
	//				new com.localgis.webservices.client.civilwork.CivilWorkWSStub();//the default implementation should point to the right endpoint
	//
	//			GetInterventionList getInterventionList53=
	//				(GetInterventionList)getTestObject(GetInterventionList.class);
	//
	//			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
	//			if (userName!= null && lastPassword!=null &&
	//					!userName.equals("") && !lastPassword.equals("")){
	//				getInterventionList53.setPassword(lastPassword);
	//				getInterventionList53.setUserName(userName);
	//				getInterventionList53.setIdMunicipio(idMunicipio);
	//			}
	//
	//			GetInterventionListResponse response = stub.getInterventionList(getInterventionList53);
	//
	//			return FromStubDataWrapperUtils.stubInterventionListToLocalGisInterventionArray(response.get_return());
	//		}catch (java.lang.Exception e) {
	//			e.printStackTrace();
	//			return new ArrayList<LocalGISIntervention>();
	//		}
	//	}


	//	public static ArrayList<LocalGISIntervention> getRangeInterventions(String userName, int idMunicipio, int page, int range) throws java.lang.Exception{
	//		try{
	//			com.localgis.webservices.client.civilwork.CivilWorkWSStub stub =
	//				new com.localgis.webservices.client.civilwork.CivilWorkWSStub();//the default implementation should point to the right endpoint
	//
	//			com.localgis.webservices.client.civilwork.CivilWorkWSStub.GetInterventionList getInterventionList53=
	//				(com.localgis.webservices.client.civilwork.CivilWorkWSStub.GetInterventionList)getTestObject(com.localgis.webservices.client.civilwork.CivilWorkWSStub.GetInterventionList.class);
	//
	//			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
	//			if (userName!= null && lastPassword!=null &&
	//					!userName.equals("") && !lastPassword.equals("")){
	//				getInterventionList53.setPassword(lastPassword);
	//				getInterventionList53.setUserName(userName);
	//				getInterventionList53.setIdMunicipio(idMunicipio);
	//
	//				getInterventionList53.setRange(range);
	//				getInterventionList53.setStartElement((page * range) + 1);
	//			}
	//
	//			GetInterventionListResponse response = stub.getInterventionList(getInterventionList53);
	//
	//			return FromStubDataWrapperUtils.stubInterventionListToLocalGisInterventionArray(response.get_return());
	//		}catch (java.lang.Exception e) {
	//			e.printStackTrace();
	//			return new ArrayList<LocalGISIntervention>();
	//		}
	//	}




	public static ArrayList<LocalGISIntervention> getExpiredInterventions(String userName, int idMunicipio, Calendar expireDate) throws java.lang.Exception{
		try{

			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNexDateInterventionList getCompleteNexDateInterventionList130=				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNexDateInterventionList)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNexDateInterventionList.class);

			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				getCompleteNexDateInterventionList130.setUserName(userName);
				getCompleteNexDateInterventionList130.setPassword(lastPassword);
				getCompleteNexDateInterventionList130.setIdMunicipio(idMunicipio);
				getCompleteNexDateInterventionList130.setToNext(new GregorianCalendar());
			}
			return FromStubDataWrapperUtils.stubInterventionListToLocalGisInterventionArray((getCivilworkStub().getCompleteNexDateInterventionList(getCompleteNexDateInterventionList130).get_return()));
			
//			CivilWorkWSTest wsTest = new CivilWorkWSTest();
//			
//			wsTest.testgetCompleteNexDateInterventionList();
//			
//			return new ArrayList<LocalGISIntervention>();
		
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
			return new ArrayList<LocalGISIntervention>();
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISIntervention>();
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISIntervention>();
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return new ArrayList<LocalGISIntervention>();
		}

	}



	
	public static ArrayList<LocalGISNote> getAllNoteList(String userName, int idMunicipio) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNoteList getCompleteNoteList118=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNoteList)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNoteList.class);

			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				getCompleteNoteList118.setUserName(userName);
				getCompleteNoteList118.setPassword(lastPassword);
				getCompleteNoteList118.setIdMunicipio(idMunicipio);
			}

			return FromStubDataWrapperUtils.stubNoteListToLocalGisNoteArrayList(getCivilworkStub().getCompleteNoteList(getCompleteNoteList118).get_return());

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
			return new ArrayList<LocalGISNote>();
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISNote>();
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISNote>();
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return new ArrayList<LocalGISNote>();
		}


	}	

	
	public static ArrayList<LocalGISNote> getRangeNoteList(String userName, int idMunicipio, int page, int range) throws java.lang.Exception{

		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredNoteList getCompleteFilteredNoteList120=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredNoteList)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredNoteList.class);


			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				getCompleteFilteredNoteList120.setUserName(userName);
				getCompleteFilteredNoteList120.setPassword(lastPassword);
				getCompleteFilteredNoteList120.setIdMunicipio(idMunicipio);

				getCompleteFilteredNoteList120.setRange(range);
				getCompleteFilteredNoteList120.setStartElement((page * range));	

			}

			return FromStubDataWrapperUtils.stubNoteListToLocalGisNoteArrayList(getCivilworkStub().getCompleteFilteredNoteList(
					getCompleteFilteredNoteList120).get_return());


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
			return new ArrayList<LocalGISNote>();
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISNote>();
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return new ArrayList<LocalGISNote>();
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return new ArrayList<LocalGISNote>();
		}
	}

	

	public static boolean cancelIntervention(LocalGISIntervention intervention, String userName, int idMunicpio) throws java.lang.Exception{
		try{
			
			CancelIntervention cancelIntervention40=(CancelIntervention)getTestObject(CancelIntervention.class);

			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (intervention != null){
				if (intervention.getId()!=null && intervention.getId() > 0){
					if (userName!= null && lastPassword!=null &&
							!userName.equals("") && !lastPassword.equals("")){
						cancelIntervention40.setIdWarning(intervention.getId());
						cancelIntervention40.setUserName(userName);
						cancelIntervention40.setPassword(lastPassword);
					}
				}

			}

			getCivilworkStub().cancelIntervention(cancelIntervention40);

			return true;
		
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
			return false;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return false;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return false;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return false;
		}
	}



	public static boolean changeInterventionDate(String userName, int idMunicipio, LocalGISIntervention intervention, Calendar nextWarningDate) throws java.lang.Exception{
		try{

			com.localgis.webservices.civilwork.client.CivilWorkWSStub.ChangeInterventionDate changeInterventionDate41=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.ChangeInterventionDate)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.ChangeInterventionDate.class);


			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (intervention != null){
				if (intervention.getId()!=null && intervention.getId() > 0){
					if (userName!= null && lastPassword!=null &&
							!userName.equals("") && !lastPassword.equals("")){
						changeInterventionDate41.setIdWarning(intervention.getId());
						changeInterventionDate41.setUserName(userName);
						changeInterventionDate41.setPassword(lastPassword);;
						changeInterventionDate41.setNewDate(nextWarningDate);
					}
				}
			}

			//There is no output to be tested!
			getCivilworkStub().changeInterventionDate(changeInterventionDate41);
			
			return true;
		
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
			return false;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return false;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return false;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return false;
		}


	}


	public  void getNote() throws java.lang.Exception{

		com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNote getNote42=
			(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNote)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNote.class);

		getCivilworkStub().getNote(
				getNote42);




	}






	public static boolean modifyNote(LocalGISNote note, String userName, int idMunicipio) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyNote modifyNote48=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyNote)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyNote.class);

			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (note != null){
				if (note.getId()!=null && note.getId() > 0){
					if (userName!= null && lastPassword!=null &&
							!userName.equals("") && !lastPassword.equals("")){
						//Pasar del StubIntervention a LocalGiSIntervention
						modifyNote48.setNote(ToStubDataWrapperUtils.localgisNoteToStubNote(note));
						modifyNote48.setUserName(userName);
						modifyNote48.setPassword(lastPassword);
						modifyNote48.setIdMunicipio(idMunicipio);
					}
				}
			}

			//There is no output to be tested!
			getCivilworkStub().modifyNote(
					modifyNote48);
			
			return true;
		
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
			return false ;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return false;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return false;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return false;
		}

	}


	public static int getNumInterventions(String userName, int idMunicipio) throws java.lang.Exception{
		try{

			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteInterventionList getNumCompleteInterventionList110=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteInterventionList)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteInterventionList.class);
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);


			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("") &&
					idMunicipio > 0){
				getNumCompleteInterventionList110.setUserName(userName);
				getNumCompleteInterventionList110.setPassword(lastPassword);
				getNumCompleteInterventionList110.setIdMunicipio(idMunicipio);

				//			, 	, 	, 	, 	, startElement, 	range, 	idMunicipio, 	userId, actuationType, 	interventionType, foreseenBudgetFrom, 	foreseenBudgetTo, 	workPercentageFrom, workPercentageTo, 	causes, features);
				// 			, 	, 	, 	, 	, 	null, 			null, 	idMunicipio, 	null, 			null, 			  null, 				null, 				null, 				null, 				null, 	null);
				//			return warEvents.getNumInterventions(null, 		 null, 			null, 				null, 		null, 		null, 		null, 	null, 			null, 	idMunicipio, 	userID,	null, 			null, 			  null, 				null, 				null, 				null, 				null, 	null);
			}

			GetNumCompleteInterventionListResponse response = getCivilworkStub().getNumCompleteInterventionList(getNumCompleteInterventionList110);

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
			return 0;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return 0;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return 0;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getNumInterventionsByConditions(String userName, int idMunicipio, LayerFeatureBean[] features) throws java.lang.Exception{
		try{

			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumInterventionListByConditions getNumCompleteInterventionList110=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumInterventionListByConditions)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumInterventionListByConditions.class);
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);


			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("") &&
					idMunicipio > 0){
				getNumCompleteInterventionList110.setUserName(userName);
				getNumCompleteInterventionList110.setPassword(lastPassword);
				
				FinderInterventionConditions conditions = new FinderInterventionConditions();
				
				conditions.setIdMunicipio(idMunicipio);
				if (features!=null && features.length>0){
					conditions.setFeatures(UtilidadesAvisosPanels.buildLayerFeaturesString(features));
				}
				getNumCompleteInterventionList110.setConditions(conditions);

			}

			GetNumInterventionListByConditionsResponse response = getCivilworkStub().getNumInterventionListByConditions(getNumCompleteInterventionList110);

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
			return 0;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return 0;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return 0;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	//	public static int getNumInterventions(String userName, int idMunicipio) throws java.lang.Exception{
	//		try{
	//			CivilWorkWSStub stub = new CivilWorkWSStub();//the default implementation should point to the right endpoint
	//
	//			GetNumInterventions getNumInterventions49=	(GetNumInterventions)getTestObject(GetNumInterventions.class);
	//
	//			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
	//
	//
	//			if (userName!= null && lastPassword!=null &&
	//					!userName.equals("") && !lastPassword.equals("") &&
	//					idMunicipio > 0){
	//				getNumInterventions49.setPassword(lastPassword);
	//				getNumInterventions49.setUserName(userName);
	//
	//				getNumInterventions49.setDescription(null);
	//				getNumInterventions49.setAssociatedAction(null);
	//
	//				getNumInterventions49.setFromStart(null);
	//				getNumInterventions49.setToStart(null);
	//				getNumInterventions49.setFromNext(null);
	//				getNumInterventions49.setToNext(null);
	//
	//				getNumInterventions49.setIdMunicipio(idMunicipio);
	//
	//				//			, 	, 	, 	, 	, startElement, 	range, 	idMunicipio, 	userId, actuationType, 	interventionType, foreseenBudgetFrom, 	foreseenBudgetTo, 	workPercentageFrom, workPercentageTo, 	causes, features);
	//				// 			, 	, 	, 	, 	, 	null, 			null, 	idMunicipio, 	null, 			null, 			  null, 				null, 				null, 				null, 				null, 	null);
	//				//			return warEvents.getNumInterventions(null, 		 null, 			null, 				null, 		null, 		null, 		null, 	null, 			null, 	idMunicipio, 	userID,	null, 			null, 			  null, 				null, 				null, 				null, 				null, 	null);
	//			}
	//
	//
	//			GetNumInterventionsResponse a = stub.getNumInterventions(
	//					getNumInterventions49);
	//
	//			System.out.println(a.get_return());
	//
	//
	//			return a.get_return();
	//
	//		}catch (java.lang.Exception e) {
	//			e.printStackTrace();
	//			return 0;
	//		}
	//
	//	}

	public static int getNumNotes(String userName, int idMunicipio) throws java.lang.Exception{
		try{

			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteNoteList getNumCompleteNoteList132=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteNoteList)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteNoteList.class);
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);


			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("") &&
					idMunicipio > 0){
				getNumCompleteNoteList132.setUserName(userName);
				getNumCompleteNoteList132.setPassword(lastPassword);
				getNumCompleteNoteList132.setIdMunicipio(idMunicipio);
			}

			return getCivilworkStub().getNumCompleteNoteList(getNumCompleteNoteList132).get_return();

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
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return 0;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getNumNotesByConditions(String userName, int idMunicipio, LayerFeatureBean[] features) throws java.lang.Exception{
		try{

			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumNoteListByConditions getNumCompleteNoteList132=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumNoteListByConditions)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumNoteListByConditions.class);
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);

			
			
			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("") &&
					idMunicipio > 0){
				getNumCompleteNoteList132.setUsername(userName);
				getNumCompleteNoteList132.setPassword(lastPassword);
				
				FinderNoteConditions conditions = new FinderNoteConditions();
				conditions.setIdMunicipio(idMunicipio);
				if (features!=null && features.length>0){
					conditions.setFeatures(UtilidadesAvisosPanels.buildLayerFeaturesString(features));
				}
				getNumCompleteNoteList132.setConditions(conditions);
			}

			return getCivilworkStub().getNumNoteListByConditions(getNumCompleteNoteList132).get_return();

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
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return 0;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return 0;
		}


	}


	public static boolean deleteNote(LocalGISNote note, String userName, int idMunicipio) throws java.lang.Exception{
		try{

			com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteNote deleteNote51=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteNote)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteNote.class);
			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (note != null){
				if (note.getId()!=null && note.getId() > 0){
					if (userName!= null && lastPassword!=null &&
							!userName.equals("") && !lastPassword.equals("")){
						//Pasar del StubIntervention a LocalGiSIntervention
						deleteNote51.setIdNote(note.getId());
						deleteNote51.setUserName(userName);
						deleteNote51.setPassword(lastPassword);
						deleteNote51.setIdMunicipio(idMunicipio);
					}
				}
			}

			//There is no output to be tested!
			getCivilworkStub().deleteNote(deleteNote51);
			
			return true;
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
			return false;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return false;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return false;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public static boolean modifyIntervention(LocalGISIntervention intervention, String userName, int idMunicipio) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyIntervention modifyIntervention52=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyIntervention)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyIntervention.class);

			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (intervention != null){
				if (intervention.getId()!=null && intervention.getId() > 0){
					if (userName!= null && lastPassword!=null &&
							!userName.equals("") && !lastPassword.equals("")){
						// Pasar de StubIntervention a LocalGiSIntervention
						modifyIntervention52.setIntervention2(ToStubDataWrapperUtils.localgisInterventionToStubIntervention(intervention));
						modifyIntervention52.setIdMunicipio(intervention.getId());
						modifyIntervention52.setUserName(userName);
						modifyIntervention52.setPassword(lastPassword);
					}
				}
			}

			//There is no output to be tested!
			getCivilworkStub().modifyIntervention(
					modifyIntervention52);

			return true;
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
			return false;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return false;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return false;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return false;
		}


	}





	public static boolean addNote(LocalGISNote note, String userName, int idMunicipio) throws java.lang.Exception{
		try{

			com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddNote addNote55=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddNote)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddNote.class);


			GestionCiudadOperaciones operaciones = new GestionCiudadOperaciones();
			
			if (note.getUserCreator() == null || note.getUserCreator().equals("")){
				note.setUserCreator(operaciones.getUserIdByUserNameAndIdMunipio(userName, idMunicipio));
			}

			if (note.getIdMunicipio() == null || note.getIdMunicipio() < 0){
				note.setIdMunicipio(idMunicipio);
			}


			if (note.getListaDeDocumentos() == null){
				note.setListaDeDocumentos(new Document[0]);
			}


			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (note != null){
				if (userName!= null && lastPassword!=null &&
						!userName.equals("") && !lastPassword.equals("")){
					//Pasar del StubIntervention a LocalGiSIntervention
					addNote55.setNote(ToStubDataWrapperUtils.localgisNoteToStubNote(note));
					addNote55.setUserName(userName);
					addNote55.setPassword(lastPassword);
					addNote55.setIdMunicipio(idMunicipio);
				}
			}

			//There is no output to be tested!
			getCivilworkStub().addNote(addNote55);

			
			return true;
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
			return false;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return false;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return false;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return false;
		}

	}


	public static boolean addIntervention(LocalGISIntervention intervention, String userName, int idMunicipio) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddIntervention addIntervention56=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddIntervention)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddIntervention.class);

			GestionCiudadOperaciones operaciones = new GestionCiudadOperaciones();

			intervention.setId(1);

			if (intervention.getUserCreator() == null || intervention.getUserCreator().equals("")){
				intervention.setUserCreator(operaciones.getUserIdByUserNameAndIdMunipio(userName, idMunicipio));
			}

			if (intervention.getIdMunicipio() == null || intervention.getIdMunicipio() < 0){
				intervention.setIdMunicipio(idMunicipio);
			}

			if (intervention.getAssignedUser() == null || intervention.getAssignedUser().equals("")){
				intervention.setAssignedUser(operaciones.getUserIdByUserNameAndIdMunipio(userName, idMunicipio));
			}

			if (intervention.getListaDeDocumentos() == null){
				intervention.setListaDeDocumentos(new Document[0]);
			}
			if (intervention.getPattern() == null){
				intervention.setPattern("");
			}

			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (intervention != null){
				if (userName!= null && lastPassword!=null &&
						!userName.equals("") && !lastPassword.equals("")){
					//Pasar del StubIntervention a LocalGiSIntervention
					addIntervention56.setIntervention2(ToStubDataWrapperUtils.localgisInterventionToStubIntervention(intervention));
					addIntervention56.setUserName(userName);
					addIntervention56.setPassword(lastPassword);
					addIntervention56.setIdMunicipio(idMunicipio);
				}

			}

			getCivilworkStub().addIntervention(addIntervention56);
		
			return true;
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
			return false;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return false;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return false;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public static void deleteIntervention(LocalGISIntervention intervention, String userName, int idMunicipio) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteIntervention deleteIntervention59=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteIntervention)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteIntervention.class);


			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
			if (intervention != null){
				if (intervention.getId()!=null && intervention.getId() > 0){
					if (userName!= null && lastPassword!=null &&
							!userName.equals("") && !lastPassword.equals("")){
						deleteIntervention59.setIdIntervention(intervention.getId());
						deleteIntervention59.setUserName(userName);
						deleteIntervention59.setPassword(lastPassword);
					}
				}

			}

			//There is no output to be tested!
			getCivilworkStub().deleteIntervention(
					deleteIntervention59);
		
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
			return ;
		}catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"Socket Time Out Exception", "Error de conexión con el servidor de WebServices", StringUtil.stackTrace(e));
			return ;
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return ;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return ;
		}

	}

	//Create an ADBBean and provide it as the test object
	@SuppressWarnings("unchecked")
	public static org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
		return (org.apache.axis2.databinding.ADBBean) type.newInstance();
	}

	
	public static StatisticalDataOT[] getStatistics(String userName, int idEntidad) throws java.lang.Exception{
		try{
			
			com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetStatistics getStatistics=
				(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetStatistics)getTestObject(com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetStatistics.class);


			String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);

			if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
				getStatistics.setUserName(userName);
				getStatistics.setPassword(lastPassword);
			}
			
			if (idEntidad >= 0){
				getStatistics.setIdEntidad(idEntidad);
			}

			//There is no output to be tested!
			return getCivilworkStub().getStatistics(
					getStatistics).get_return();
		
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
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return null;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
    public  static PostalDataOT[] getPostalDataFromIdTramosAndIdVia(
    		String userName, int idEntidad, Integer[] tramos, int calle) throws java.lang.Exception{
    	
    	try{
    		com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub =GeoMarketingWSWrapper.getGeoMarktingStub();
    		com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdVia getPostalDataFromIdTramosAndIdVia34=
    			(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdVia)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdVia.class);
    		
    		if (idEntidad >= 0){
    			getPostalDataFromIdTramosAndIdVia34.setIdEntidad(idEntidad);
    		}
            
    		if (tramos !=null && tramos.length>0){
    			int[] tramosInt = new int[tramos.length];
    			for (int i=0; i < tramos.length; i++){
    				tramosInt[i] = tramos[i];
    			}
    			getPostalDataFromIdTramosAndIdVia34.setIdFeatureStep(tramosInt);
    		}
    		
    		if (calle > 0){
    			getPostalDataFromIdTramosAndIdVia34.setIdFeatureStreet(calle);
    		}
            
            String lastPassword = AppContext.getApplicationContext().getUserPreference("LAST_PASS","",false);
            if (userName!= null && lastPassword!=null &&
					!userName.equals("") && !lastPassword.equals("")){
            	getPostalDataFromIdTramosAndIdVia34.setUserName(userName);
                getPostalDataFromIdTramosAndIdVia34.setPassword(lastPassword);
			}
            


    		return stub.getPostalDataFromIdTramosAndIdVia(getPostalDataFromIdTramosAndIdVia34).get_return();
    		
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
		}catch (NoSuchMethodError e) {
			ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
					"No Such Method Error", "No se encuentra la operación.", StringUtil.stackTrace(e));
			return null;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			return null;
		}


    }



}
