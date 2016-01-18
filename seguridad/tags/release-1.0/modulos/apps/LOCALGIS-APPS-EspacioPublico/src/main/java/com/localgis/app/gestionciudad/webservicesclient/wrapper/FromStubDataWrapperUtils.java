package com.localgis.app.gestionciudad.webservicesclient.wrapper;

import java.util.ArrayList;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.beans.DocumentTypes;

public class FromStubDataWrapperUtils {

	public static LocalGISIntervention stubInterventionToLocalGisIntervention (com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISIntervention2 stubIntervention){
		LocalGISIntervention resultado = new LocalGISIntervention();

		resultado.setActuationType(stubIntervention.getActuationType());
		resultado.setAssignedUser(stubIntervention.getAssignedUser());
		resultado.setCauses(stubIntervention.getCauses());
		resultado.setDescription(stubIntervention.getDescription());
		resultado.setFeatureRelation(layerFeatureStubArrayTolayerFeatureArray(stubIntervention.getFeatureRelation()));
		resultado.setForeseenBudget(stubIntervention.getForeseenBudget());
		resultado.setId(stubIntervention.getId());		
		resultado.setIdMunicipio(stubIntervention.getIdMunicipio());
		resultado.setInterventionType(stubIntervention.getInterventionType());
		resultado.setListaDeDocumentos(documentStubArrayToDocumentBeanArray(stubIntervention.getListaDeDocumentos()));
		resultado.setNextWarning(stubIntervention.getNextWarning());
		resultado.setPattern(stubIntervention.getPattern());
		resultado.setStartWarning(stubIntervention.getStartWarning());
		resultado.setUserCreator(stubIntervention.getUserCreator());
		resultado.setWorkPercentage(stubIntervention.getWorkPercentage());
		resultado.setPriority(stubIntervention.getPriority());
		resultado.setExecutionDate(stubIntervention.getEndedWork());
		resultado.setAddToRoutes(stubIntervention.getSendToNetwork());

		return resultado;
	}


	public static LayerFeatureBean[] layerFeatureStubArrayTolayerFeatureArray(com.localgis.webservices.civilwork.client.CivilWorkWSStub.LayerFeatureBean[] listaFeatures){
		ArrayList<LayerFeatureBean> resultado = new ArrayList<LayerFeatureBean>();

		if (listaFeatures!=null && listaFeatures.length>0){
			for(int i = 0; i < listaFeatures.length; i++){
				if (listaFeatures[i]!=null && !Double.isNaN(listaFeatures[i].getIdFeature())
						&& !Double.isNaN(listaFeatures[i].getIdLayer())){
					resultado.add(stubLayerFeatureBeanToLayerFeatureBean(listaFeatures[i]));
				}
			}
		}

		return resultado.toArray(new LayerFeatureBean[resultado.size()]);
	}

	public static LayerFeatureBean stubLayerFeatureBeanToLayerFeatureBean (com.localgis.webservices.civilwork.client.CivilWorkWSStub.LayerFeatureBean layerFeature){
		LayerFeatureBean resultado = new LayerFeatureBean();
		if (layerFeature != null){
			if (Double.isNaN(layerFeature.getIdFeature()) || Double.isNaN(layerFeature.getIdLayer())){
				System.out.println("null");
			} else {
				resultado.setIdFeature(layerFeature.getIdFeature());
				resultado.setIdLayer(layerFeature.getIdLayer());
			}
		}

		return resultado;
	}


	public static Document[] documentStubArrayToDocumentBeanArray(com.localgis.webservices.civilwork.client.CivilWorkWSStub.Document[] documents){
		if (documents!=null && documents.length > 0){
			ArrayList<Document> resultado = new ArrayList<Document>();

			for(int i = 0; i < documents.length; i++){
				if (documents[i]!=null){
				resultado.add(documentStubToDocumentBean(documents[i]));
				}
			}
			return resultado.toArray(new Document[resultado.size()]);
		}
		return new Document[0];

	}

	public static Document documentStubToDocumentBean(com.localgis.webservices.civilwork.client.CivilWorkWSStub.Document document){
		
		
		Document resultado = new Document();

		if (document != null){
			if (document.getExtension()!=null){
				resultado.setExtension(document.getExtension());
			}
			//		stubResultado.setFichero(document.getFichero());


			resultado.setIdDocumento(document.getIdDocumento());

			if (document.getNombre()!=null){
				resultado.setNombre(document.getNombre());
			}

			String tipoDocumento = document.getTipo();
			if (tipoDocumento != null && !tipoDocumento.equals("")){
				if (tipoDocumento.equals(DocumentTypes.DOC.toString())){
					resultado.setTipo(DocumentTypes.DOC);
				} else if (tipoDocumento.equals(DocumentTypes.IMAGEN.toString())){
					resultado.setTipo(DocumentTypes.IMAGEN);
				} else if (tipoDocumento.equals(DocumentTypes.PDF.toString())){
					resultado.setTipo(DocumentTypes.PDF);
				} else{
					resultado.setTipo(DocumentTypes.TXT);
				}
			} else{
				resultado.setTipo(DocumentTypes.TXT);
			}
			if (document.getTipo().equals(DocumentTypes.DOC.toString())){
				;
			}
		}

		return resultado;
	}


	public static LocalGISNote noteStubToLocalGisNote (com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISNote note){
		LocalGISNote resultado = new LocalGISNote();

		resultado.setDescription(note.getDescription());
		resultado.setFeatureRelation(layerFeatureStubArrayTolayerFeatureArray(note.getFeatureRelation()));
		resultado.setId(note.getId());
		resultado.setIdMunicipio(note.getIdMunicipio());
		resultado.setListaDeDocumentos(documentStubArrayToDocumentBeanArray(note.getListaDeDocumentos()));
		resultado.setStartWarning(note.getStartWarning());
		resultado.setUserCreator(note.getUserCreator());		

		return resultado;

	}

	public static ArrayList<LocalGISNote> stubNoteListToLocalGisNoteArrayList(com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISNote[] notes){
		ArrayList<LocalGISNote> resultado = new ArrayList<LocalGISNote>();

		if (notes!= null && notes.length > 0){
			for(int i=0; i < notes.length; i++){
				resultado.add(noteStubToLocalGisNote(notes[i]));
			}
		}


		return resultado;
	}

	public static ArrayList<LocalGISIntervention> stubInterventionListToLocalGisInterventionArray(com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISIntervention2[] interventions){
		ArrayList<LocalGISIntervention> resultado = new ArrayList<LocalGISIntervention>();

		if (interventions != null && interventions.length > 0){
			for (int i=0; i < interventions.length; i++){
				resultado.add(stubInterventionToLocalGisIntervention(interventions[i]));
			}
		}

		return resultado;
	}

}
