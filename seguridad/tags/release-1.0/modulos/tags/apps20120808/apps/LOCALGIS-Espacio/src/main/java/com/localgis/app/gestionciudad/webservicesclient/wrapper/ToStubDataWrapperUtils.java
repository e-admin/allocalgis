package com.localgis.app.gestionciudad.webservicesclient.wrapper;


import com.localgis.webservices.civilwork.client.CivilWorkWSStub.Document;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.LayerFeatureBean;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISIntervention2;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISNote;

public class ToStubDataWrapperUtils {

	public static LocalGISIntervention2 localgisInterventionToStubIntervention(com.localgis.app.gestionciudad.beans.LocalGISIntervention intervention){
		LocalGISIntervention2 stubResultado = new LocalGISIntervention2();

		stubResultado.setActuationType(intervention.getActuationType());
		stubResultado.setAssignedUser(intervention.getAssignedUser());
		stubResultado.setCauses(intervention.getCauses());
		stubResultado.setDescription(intervention.getDescription());
		stubResultado.setFeatureRelation(layerFeatureArrayToLayerFeatureStubArray(intervention.getFeatureRelation()));
		stubResultado.setForeseenBudget(intervention.getForeseenBudget());
		stubResultado.setPriority(intervention.getPriority());
		stubResultado.setEndedWork(intervention.getExecutionDate());
		if (intervention.getId()!=null && intervention.getId()>0){
			stubResultado.setId(intervention.getId());
		}
		stubResultado.setIdMunicipio(intervention.getIdMunicipio());
		stubResultado.setInterventionType(intervention.getInterventionType());
		stubResultado.setListaDeDocumentos(documentArrayToStubDocumentArray(intervention.getListaDeDocumentos()));
		stubResultado.setNextWarning(intervention.getNextWarning());
		stubResultado.setPattern(intervention.getPattern());
		stubResultado.setStartWarning(intervention.getStartWarning());
		stubResultado.setUserCreator(intervention.getUserCreator());
		stubResultado.setWorkPercentage(intervention.getWorkPercentage());
		if (intervention.getAddToRoutes()!=null){
			stubResultado.setSendToNetwork(intervention.getAddToRoutes());	
		}
		

		return stubResultado;
	}

	public static LayerFeatureBean[] layerFeatureArrayToLayerFeatureStubArray(com.localgis.app.gestionciudad.beans.LayerFeatureBean[] listaFeatures){
		if (listaFeatures!=null && listaFeatures.length>0){
			LayerFeatureBean[] resultado = new LayerFeatureBean[listaFeatures.length];

			for(int i = 0; i < listaFeatures.length; i++){
				resultado[i] = layerFeatureToLayerFeatureStub(listaFeatures[i]);
			}

			return resultado;
		}else{
			return new LayerFeatureBean[0];
		}
	}


	public static LayerFeatureBean layerFeatureToLayerFeatureStub(com.localgis.app.gestionciudad.beans.LayerFeatureBean layerFeatureBean){
		LayerFeatureBean stubResultado = new LayerFeatureBean();

		stubResultado.setIdFeature(layerFeatureBean.getIdFeature());
		stubResultado.setIdLayer(layerFeatureBean.getIdLayer());

		return stubResultado;
	}

	public static Document[] documentArrayToStubDocumentArray(com.localgis.app.gestionciudad.beans.Document[] documents){
		if (documents!=null && documents.length>0){
			Document[] restultado = new Document[documents.length];

			for(int i = 0; i < documents.length; i++){
				restultado[i] = documentToStubDocument(documents[i]);
			}

			return restultado;
		} else{
			return new Document[0];
		}
	}

	public static Document documentToStubDocument(com.localgis.app.gestionciudad.beans.Document document){
		Document stubResultado = new Document();

		stubResultado.setExtension(document.getExtension());
		//		stubResultado.setFichero(document.getFichero());
		stubResultado.setIdDocumento(document.getIdDocumento());
		stubResultado.setNombre(document.getNombre());
		stubResultado.setTipo(document.getTipo());

		return stubResultado;
	}


	public static LocalGISNote localgisNoteToStubNote (com.localgis.app.gestionciudad.beans.LocalGISNote note){
		LocalGISNote resultado = new LocalGISNote();

		resultado.setDescription(note.getDescription());
		resultado.setFeatureRelation(layerFeatureArrayToLayerFeatureStubArray(note.getFeatureRelation()));
		if (note.getId()!=null && note.getId()>0){
			resultado.setId(note.getId());	
		}
		resultado.setIdMunicipio(note.getIdMunicipio());
		resultado.setListaDeDocumentos(documentArrayToStubDocumentArray(note.getListaDeDocumentos()));
		resultado.setStartWarning(note.getStartWarning());
		resultado.setUserCreator(note.getUserCreator());		

		return resultado;
	}



}
