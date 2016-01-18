package com.localgis.webservices.civilwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

import javax.naming.NamingException;

import com.geopista.server.administradorCartografia.ACException;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.webservices.civilwork.bean.LocalGISIntervention2;
import com.localgis.webservices.civilwork.ln.LocalGISInterventionsLN;
import com.localgis.webservices.civilwork.ln.LocalGISNotesLN;
import com.localgis.webservices.civilwork.model.ot.StatisticalDataOT;
import com.localgis.webservices.civilwork.util.OrderByColumn;
import com.localgis.webservices.geomarketing.ln.UserValidationLN;

public class CivilWorkWS {
	/*private String getUserName(){
		MessageContext m2 =  MessageContext.getCurrentMessageContext();
		SOAPHeader header = m2.getEnvelope().getHeader();
		return header.getFirstElement().getFirstElement().getFirstElement().getText();
	}*/
	public String addNote(String userName,String password,LocalGISNote note, Integer idMunicipio) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISNotesLN notesLN = new LocalGISNotesLN();
		notesLN.addNote(note, userId);
		return null;
	}

	public String deleteNote(String userName,String password,Integer idNote, Integer idMunicipio) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISNotesLN notesLN = new LocalGISNotesLN();
		notesLN.removeNote(idNote, userId);
		return null;
	}

	public String modifyNote(String userName,String password,LocalGISNote note,Integer idMunicipio) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISNotesLN notesLN = new LocalGISNotesLN();
		notesLN.setNote(note, userId);
		return null;
	}

	public LocalGISNote getNote(String userName,String password,Integer idNote,Integer idMunicipio) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISNotesLN notesLN = new LocalGISNotesLN();
		return notesLN.getNote(idNote, true, idMunicipio, userId);
	}


	public LocalGISNote[] getNoteList(String userName,String password,String description,Calendar from,Calendar to,Integer startElement,Integer range,Integer idMunicipio,OrderByColumn[] orderByColumns,LayerFeatureBean[] features) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISNotesLN notesLN = new LocalGISNotesLN();
		ArrayList<LayerFeatureBean> layers = new ArrayList<LayerFeatureBean>(Arrays.asList(features));
		ArrayList<OrderByColumn> columns = new ArrayList<OrderByColumn>(Arrays.asList(orderByColumns));
		ArrayList<LocalGISNote> notes = notesLN.getLocalGISNotesList(description, from, to, startElement, range, idMunicipio, userId, columns, layers);
		return notes.toArray(new LocalGISNote[(notes.size())]);
	}
	public LocalGISNote[] getCompleteNoteList(String userName,String password,Integer idMunicipio) throws ACException, NamingException{
		return getNoteList(userName,password,null, null, null, null, null, idMunicipio, new OrderByColumn[0], new LayerFeatureBean[0]);

	}

	public LocalGISNote[] getCompleteFilteredNoteList(String userName,String password,Integer startElement,Integer range,Integer idMunicipio) throws ACException, NamingException{
		return getNoteList(userName,password,null, null, null, startElement, range, idMunicipio, new OrderByColumn[0], new LayerFeatureBean[0]);

	}
	
	public int getNumNotes(String userName,String password,String description,Calendar from,Calendar to,Integer idMunicipio,LayerFeatureBean[] features) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISNotesLN notesLN = new LocalGISNotesLN();
		ArrayList<LayerFeatureBean> layers = new ArrayList<LayerFeatureBean>(Arrays.asList(features));
		return notesLN.getNumNotes(description, from, to, idMunicipio, userId, layers);
	}
	public int getNumCompleteNoteList(String userName,String password,Integer idMunicipio) throws ACException, NamingException{

		return getNumNotes(userName,password,null, null, null, idMunicipio, new LayerFeatureBean[0]);
	}
	public String addIntervention(String userName,String password,LocalGISIntervention2 intervention2, Integer idMunicipio) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		
		LocalGISInterventionsLN interventionLN = new LocalGISInterventionsLN();
		LocalGISIntervention intervention = new LocalGISIntervention();
		parseToLocalGISIntervention(intervention,intervention2);
		interventionLN.addIntervention(intervention, userId,intervention2.getSendToNetwork());
		return null;
	}
		
	public LocalGISNote[] getNoteListByContidions(String userName,String password,FinderNoteConditions conditions) throws ACException, NamingException{
		
		return getNoteList(userName,password,conditions.getDescription(),conditions.getFrom(),conditions.getTo(),conditions.getStartElement(),conditions.getRange(),conditions.getIdMunicipio(),getOrderByColumn(conditions.getOrderByColumns()),getLayerFeatureBean(conditions.getFeatures()));
	}
	public int getNumNoteListByConditions(String username,String password,FinderNoteConditions conditions) throws ACException, NamingException{
		return getNumNotes(username, password, conditions.getDescription(), conditions.getFrom(), conditions.getTo(), conditions.getIdMunicipio(), getLayerFeatureBean(conditions.getFeatures()));
	}
	public int getNumInterventionListByConditions(String userName,String password,FinderInterventionConditions conditions) throws ACException, NamingException{		
		return getNumInterventions(userName, password, conditions.getDescription(), conditions.getAssociatedAction(), conditions.getFromStart(), conditions.getToStart(), conditions.getFromNext(), conditions.getToNext(), conditions.getStartElement(), conditions.getRange(), conditions.getIdMunicipio(), conditions.getActuationType(), conditions.getInterventionType(), conditions.getForeseenBudgetFrom(), conditions.getForeseenBudgetTo(), conditions.getWorkPercentageFrom(), conditions.getWorkPercentageTo(), conditions.getCauses(), getLayerFeatureBean(conditions.getFeatures()));
	}
	private OrderByColumn[] getOrderByColumn(String orderByColumns) {
		if(orderByColumns == null || orderByColumns.equals(""))
			return new OrderByColumn[0];
		String[] columns = orderByColumns.split(";");
		OrderByColumn[] obc = new OrderByColumn[columns.length];
		for(int i = 0;i<columns.length;i++){
			obc[i] = buildOrderByColumn(columns[i]);
		}
		return obc;
	}

	private OrderByColumn buildOrderByColumn(String column) {
		String columnName = column.split(":")[0].toUpperCase();
		String order = column.split(":")[1].toUpperCase();
		boolean isAsc = true;
		if(order.equals("DESC"))
			isAsc = false;
		return new OrderByColumn(columnName,isAsc);
	}

	private LayerFeatureBean[] getLayerFeatureBean(String features) {
		if(features == null || features.equals(""))
			return new LayerFeatureBean[0];
		String[] layersFeatures = features.split(";");
		LayerFeatureBean[] lfb = new LayerFeatureBean[layersFeatures.length];
		for(int i = 0;i<layersFeatures.length;i++){
			lfb[i] = buildLayerFeatureBeanFromString(layersFeatures[i]);
		}
		return lfb;
	}

	private LayerFeatureBean buildLayerFeatureBeanFromString(String layerFeature) {
		String layer = layerFeature.split(":")[0];
		String feature = layerFeature.split(":")[1];
		return new LayerFeatureBean(new Integer(layer), new Integer(feature));
	}

	private void parseToLocalGISIntervention2(
			LocalGISIntervention intervention,
			LocalGISIntervention2 intervention2) {
		intervention2.setActuationType(intervention.getActuationType());
		intervention2.setAssignedUser(intervention.getAssignedUser());
		intervention2.setCauses(intervention.getCauses());
		intervention2.setDescription(intervention.getDescription());
		intervention2.setFeatureRelation(intervention.getFeatureRelation());
		intervention2.setForeseenBudget(intervention.getForeseenBudget());
		intervention2.setId(intervention.getId());
		intervention2.setIdMunicipio(intervention.getIdMunicipio());
		intervention2.setInterventionType(intervention.getInterventionType());
		intervention2.setListaDeDocumentos(intervention.getListaDeDocumentos());
		intervention2.setNextWarning(intervention.getNextWarning());
		intervention2.setPattern(intervention.getPattern());
		intervention2.setStartWarning(intervention.getStartWarning());
		intervention2.setUserCreator(intervention.getUserCreator());
		intervention2.setWorkPercentage(intervention.getWorkPercentage());
		intervention2.setEndedWork(intervention.getEndedWork());
		intervention2.setPriority(intervention.getPriority());
		intervention2.setSendToNetwork(intervention.getIncidentNetworkType());
	}

	private void parseToLocalGISIntervention(LocalGISIntervention intervention,
			LocalGISIntervention2 intervention2) {
		intervention.setActuationType(intervention2.getActuationType());
		intervention.setAssignedUser(intervention2.getAssignedUser());
		intervention.setCauses(intervention2.getCauses());
		intervention.setDescription(intervention2.getDescription());
		intervention.setFeatureRelation(intervention2.getFeatureRelation());
		intervention.setForeseenBudget(intervention2.getForeseenBudget());
		intervention.setId(intervention2.getId());
		intervention.setIdMunicipio(intervention2.getIdMunicipio());
		intervention.setInterventionType(intervention2.getInterventionType());
		intervention.setListaDeDocumentos(intervention2.getListaDeDocumentos());
		intervention.setNextWarning(intervention2.getNextWarning());
		intervention.setPattern(intervention2.getPattern());
		intervention.setStartWarning(intervention2.getStartWarning());
		intervention.setUserCreator(intervention2.getUserCreator());
		intervention.setWorkPercentage(intervention2.getWorkPercentage());
		intervention.setEndedWork(intervention2.getEndedWork());
		intervention.setPriority(intervention2.getPriority());
		
	}

	public String deleteIntervention(String userName,String password,Integer idIntervention) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISInterventionsLN interventionLN = new LocalGISInterventionsLN();
		interventionLN.removeWarning(idIntervention, userId);
		return null;
	}

	public String modifyIntervention(String userName,String password,LocalGISIntervention2 intervention2, Integer idMunicipio) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISInterventionsLN interventionLN = new LocalGISInterventionsLN();
		LocalGISIntervention intervention = new LocalGISIntervention();
		parseToLocalGISIntervention(intervention, intervention2);
		interventionLN.setWarning(intervention, userId,intervention2.getSendToNetwork());
		return null;
	}

	public LocalGISIntervention2 getIntervention(String userName,String password,Integer idIntervention,Integer idMunicipio) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISInterventionsLN interventionLN = new LocalGISInterventionsLN();
		LocalGISIntervention intervention = interventionLN.getWarning(idIntervention, true, idMunicipio, userId);
		LocalGISIntervention2 intervention2 = new LocalGISIntervention2();
		parseToLocalGISIntervention2(intervention, intervention2);
		return intervention2;
	}


	public LocalGISIntervention2[] getInterventionList(String userName,String password,String description,String associatedAction,Calendar fromStart,Calendar toStart,Calendar fromNext,Calendar toNext,Integer startElement,Integer range,Integer idMunicipio,String actuationType,String interventionType,Double foreseenBudgetFrom,Double foreseenBudgetTo,Double workPercentageFrom,Double workPercentageTo,String causes,OrderByColumn[] orderByColumns,LayerFeatureBean[] features) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISInterventionsLN interventionLN = new LocalGISInterventionsLN();
		ArrayList<LayerFeatureBean> layers = new ArrayList<LayerFeatureBean>(Arrays.asList(features));
		ArrayList<OrderByColumn> columns = new ArrayList<OrderByColumn>(Arrays.asList(orderByColumns));
		ArrayList<LocalGISIntervention> interventions = interventionLN.getInterventionList(description, associatedAction, fromStart, toStart, fromNext, toNext, startElement, range, idMunicipio, userId, actuationType, interventionType, foreseenBudgetFrom, foreseenBudgetTo, workPercentageFrom, workPercentageTo, causes, layers, columns);
		Iterator<LocalGISIntervention> it = interventions.iterator();
		LocalGISIntervention2[] interventionList = new LocalGISIntervention2[interventions.size()];
		int i = 0;
		while(it.hasNext()){
			LocalGISIntervention intervention = it.next();
			LocalGISIntervention2 intervention2 = new LocalGISIntervention2();
			parseToLocalGISIntervention2(intervention, intervention2);
			interventionList[i] = intervention2;
			i++;
		}
		return interventionList;
	}
	public LocalGISIntervention2[] getInterventionListByConditions(String userName,String password,FinderInterventionConditions conditions) throws ACException, NamingException{		
		return getInterventionList(userName,password,conditions.getDescription(), conditions.getAssociatedAction(), conditions.getFromStart(), conditions.getToStart(), conditions.getFromNext(), conditions.getToNext(), conditions.getStartElement(), conditions.getRange(), conditions.getIdMunicipio(), conditions.getActuationType(), conditions.getInterventionType(), conditions.getForeseenBudgetFrom(), conditions.getForeseenBudgetTo(), conditions.getWorkPercentageFrom(), conditions.getWorkPercentageTo(), conditions.getCauses(), getOrderByColumn(conditions.getOrderColumns()), getLayerFeatureBean(conditions.getFeatures()));
	}
	public LocalGISIntervention2[] getCompleteInterventionList(String userName,String password,Integer idMunicipio) throws ACException, NamingException{
		return getInterventionList(userName,password,null,		null,				null,		null,		null,		null,	null,			null,	idMunicipio,	null,			null,				null,				null,				null,				null,				null,	new OrderByColumn[0],	new LayerFeatureBean[0]);
		}

	public LocalGISIntervention2[] getCompleteFilteredInterventionList(String userName,String password,Integer startElement,Integer range,Integer idMunicipio) throws ACException, NamingException{
		return getInterventionList(userName,password,null,		null,				null,		null,		null,		null,	startElement,	range,	idMunicipio,	null,			null,				null,				null,				null,				null,				null,	new OrderByColumn[0],	new LayerFeatureBean[0]);
	}
	
	public LocalGISIntervention2[] getCompleteNexDateInterventionList(String userName,String password,Calendar toNext,Integer idMunicipio) throws ACException, NamingException{
		return getInterventionList(userName,password,null,null,null,null,null,toNext,null,null,idMunicipio,null,null,null,null,null,null,null,new OrderByColumn[0],new LayerFeatureBean[0]);
	}
	
	public LocalGISIntervention2[] getCompleteFilteredNextDateInterventionList(String userName,String password,Calendar toNext,Integer startElement,Integer range,Integer idMunicipio) throws ACException, NamingException{
		return getInterventionList(userName,password,null,null,null,null,null,toNext,startElement,range,idMunicipio,null,null,null,null,null,null,null,new OrderByColumn[0],new LayerFeatureBean[0]);
	}
	
	
	public int getNumInterventions(String userName,String password,String description,String associatedAction,Calendar fromStart,Calendar toStart,Calendar fromNext,Calendar toNext,Integer startElement,Integer range,Integer idMunicipio,String actuationType,String interventionType,Double foreseenBudgetFrom,Double foreseenBudgetTo,Double workPercentageFrom,Double workPercentageTo,String causes,LayerFeatureBean[] features) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISInterventionsLN interventionLN = new LocalGISInterventionsLN();
		ArrayList<LayerFeatureBean> layers = new ArrayList<LayerFeatureBean>(Arrays.asList(features));
		return interventionLN.getNumInterventions(description, associatedAction, fromStart, toStart, fromNext, toNext, startElement, range, idMunicipio, userId, actuationType, interventionType, foreseenBudgetFrom, foreseenBudgetTo, workPercentageFrom, workPercentageTo, causes, layers);
	}
	
	public int getNumCompleteInterventionList(String userName,String password,Integer idMunicipio) throws ACException, NamingException{
		return getNumInterventions(userName,password,null, null, null, null, null, null, null, null, idMunicipio, null, null, null, null, null, null, null, new LayerFeatureBean[0]);
	}

	public int getNumCompleteNexDateInterventionList(String userName,String password,Calendar toNext,Integer idMunicipio) throws ACException, NamingException{
		return getNumInterventions(userName,password,null, null, null, null, null, toNext, null, null, idMunicipio, null, null, null, null, null, null, null, new LayerFeatureBean[0]);
	}	

	public String changeInterventionDate(String userName,String password,Integer idWarning,Calendar newDate) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISInterventionsLN interventionLN = new LocalGISInterventionsLN();
		interventionLN.changeWarningDate(idWarning, newDate, userId);
		return null;
	}
	public String cancelIntervention(String userName,String password,Integer idWarning) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISInterventionsLN interventionLN = new LocalGISInterventionsLN();
		interventionLN.cancelWarning(idWarning, userId);
		return null;
	}
	public StatisticalDataOT[] getStatistics(String userName,String password,Integer idEntidad) throws NamingException, ACException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		LocalGISInterventionsLN interventionLN = new LocalGISInterventionsLN();
		ArrayList<StatisticalDataOT> statistics = interventionLN.getStatistics(userId, idEntidad);
		return statistics.toArray(new StatisticalDataOT[statistics.size()]);
	}
}
