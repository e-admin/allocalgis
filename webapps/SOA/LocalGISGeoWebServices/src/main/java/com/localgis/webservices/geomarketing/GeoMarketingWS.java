/**
 * GeoMarketingWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.NamingException;




import com.geopista.server.administradorCartografia.ACException;
import com.localgis.webservices.geomarketing.ln.ElementsByGeometryLN;
import com.localgis.webservices.geomarketing.ln.UserValidationLN;
import com.localgis.webservices.geomarketing.model.ot.GeoMarketingOT;
import com.localgis.webservices.geomarketing.model.ot.PortalStepRelationOT;
import com.localgis.webservices.geomarketing.model.ot.PostalDataOT;
import com.localgis.webservices.geomarketing.model.ot.RangeData;
import com.localgis.webservices.geomarketing.model.ot.StudiesLevel;

public class GeoMarketingWS {
	/*private String getUserName(){
		MessageContext m2 =  MessageContext.getCurrentMessageContext();
		SOAPHeader header = m2.getEnvelope().getHeader();
		return header.getFirstElement().getFirstElement().getFirstElement().getText();
	}*/
	public Integer getNumElements(String userName,String password,String wktGeometry,String srid,Integer idLayer,Integer idEntidad,Integer idMunicipio) throws ACException, SQLException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		ElementsByGeometryLN geometryLN = new ElementsByGeometryLN();
		Integer result = geometryLN.getNumElementsByGeometry(wktGeometry, srid, idLayer, idEntidad, userId);
		return result;
	}
	public GeoMarketingOT2 getGeomarketingData(String userName,String password,String wktGeometry,String srid,Integer idEntidad,Integer[] ranges) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		ElementsByGeometryLN geometryLN = new ElementsByGeometryLN();
		GeoMarketingOT result = geometryLN.getGeomarketingDataByGeometry(new ArrayList<Integer>(Arrays.asList(ranges)), wktGeometry, srid,  idEntidad,userId);
		GeoMarketingOT2 result2 = new GeoMarketingOT2();
		parseGeomarketingOTToGeoMarketingOT2(result,result2);
		return result2;
	}
	public GeoMarketingOT2 getGeomarketingAndElementsData(String userName,String password,String wktGeometry,String srid,Integer idLayer,Integer idEntidad,Integer[] ranges, Integer idMunicipio) throws ACException, SQLException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		ElementsByGeometryLN geometryLN = new ElementsByGeometryLN();
		GeoMarketingOT result = geometryLN.getGeomarketingDataByGeometry(new ArrayList<Integer>(Arrays.asList(ranges)), wktGeometry, srid,  idEntidad,userId);
		result.setExternalValue(geometryLN.getNumElementsByGeometry(wktGeometry, srid, idLayer, idEntidad,userId));
		GeoMarketingOT2 result2 = new GeoMarketingOT2();
		parseGeomarketingOTToGeoMarketingOT2(result,result2);
		return result2;
	}
	public GeoMarketingOT2[] getGeomarketingByIdLayerAndAttributeName(String userName,String password,String attributeName,String locale,Integer idLayer,Integer idEntidad,Integer[] ranges) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		ElementsByGeometryLN geometryLN = new ElementsByGeometryLN();
		ArrayList<GeoMarketingOT> result = geometryLN.getGeomarketingDataFromIdLayer(idLayer, attributeName, locale, userId, idEntidad, new ArrayList<Integer>(Arrays.asList(ranges)));
		result = groupByAttributeName(result);
		ArrayList<GeoMarketingOT2> result2 = new ArrayList<GeoMarketingOT2>();
		Iterator<GeoMarketingOT> it = result.iterator();
		while(it.hasNext()){
			GeoMarketingOT2 geo2 = new GeoMarketingOT2();
			parseGeomarketingOTToGeoMarketingOT2(it.next(),geo2);
			result2.add(geo2);
		}
		return result2.toArray(new GeoMarketingOT2[result2.size()]);
	}
	public GeoMarketingOT2[] getGeomarketingAndElementsDataByIdLayerAndAttributeName(String userName,String password,String attributeName,String locale,Integer idLayer,Integer idLayerElements,Integer idEntidad,Integer[] ranges) throws ACException, NamingException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		ElementsByGeometryLN geometryLN = new ElementsByGeometryLN();
		ArrayList<GeoMarketingOT> result = geometryLN.getGeomarketingDataAndElementsFromIdLayer(idLayer, idLayerElements, attributeName, locale, userId, idEntidad, new ArrayList<Integer>(Arrays.asList(ranges)));
		result = groupByAttributeName(result);
		ArrayList<GeoMarketingOT2> result2 = new ArrayList<GeoMarketingOT2>();
		Iterator<GeoMarketingOT> it = result.iterator();
		while(it.hasNext()){
			GeoMarketingOT2 geo2 = new GeoMarketingOT2();
			parseGeomarketingOTToGeoMarketingOT2(it.next(),geo2);
			result2.add(geo2);
		}
		return result2.toArray(new GeoMarketingOT2[result2.size()]);
	}
	private ArrayList<GeoMarketingOT> groupByAttributeName(ArrayList<GeoMarketingOT> result) {
		Hashtable<String,GeoMarketingOT> groupedByAttName = new Hashtable<String,GeoMarketingOT>();
		ArrayList<GeoMarketingOT> accumResult = new ArrayList<GeoMarketingOT>();
		Iterator<GeoMarketingOT> it = result.iterator();
		ArrayList<String> attributeOrder = new ArrayList<String>();
		String nullAtt = "Sin detalle";
		while(it.hasNext()){
			GeoMarketingOT geo = it.next();
			if(geo.getAttributeName() == null)
				geo.setAttributeName(nullAtt);
			if(groupedByAttName.get(geo.getAttributeName()) != null){
				groupData(groupedByAttName.get(geo.getAttributeName()),geo);
			}else{
				groupedByAttName.put(geo.getAttributeName(),geo);
				attributeOrder.add(geo.getAttributeName());
			}
		}
		Iterator<String> it2 = attributeOrder.iterator();
		while(it2.hasNext()){
			accumResult.add(groupedByAttName.get(it2.next()));
		}
		return accumResult;
		
	}
	private void groupData(GeoMarketingOT geoMarketingOT, GeoMarketingOT geo) {
		if(geoMarketingOT.getExternalValue()!= null)
			geoMarketingOT.setExternalValue(geoMarketingOT.getExternalValue()+geo.getExternalValue());
		geoMarketingOT.setForeignHabitants(geoMarketingOT.getForeignHabitants()+geo.getForeignHabitants());
		geoMarketingOT.setIdFeature(appendIdFeatures(geoMarketingOT.getIdFeature(),geo.getIdFeature()));
		appendLevelStudies(geoMarketingOT.getLevelStudies(),geo.getLevelStudies());
		geoMarketingOT.setNumFemales(geoMarketingOT.getNumFemales()+geo.getNumFemales());
		geoMarketingOT.setNumHabitants(geoMarketingOT.getNumHabitants()+geo.getNumHabitants());
		geoMarketingOT.setNumMales(geoMarketingOT.getNumMales()+geo.getNumMales());
		geoMarketingOT.setSpanishHabitants(geoMarketingOT.getSpanishHabitants()+geo.getSpanishHabitants());

		
	}
	@SuppressWarnings("unchecked")
	private Integer[] appendIdFeatures(Integer[] idFeature, Integer[] idFeature2) {
		ArrayList<Integer> list1 = new ArrayList(Arrays.asList(idFeature));
		list1.addAll(Arrays.asList(idFeature2));	
		return list1.toArray(new Integer[list1.size()]);
	}
	private void appendLevelStudies(StudiesLevel levelStudies,
			StudiesLevel levelStudies2) {
		levelStudies.setI10NoSabeLeerNiEscribir(levelStudies.getI10NoSabeLeerNiEscribir()+levelStudies2.getI10NoSabeLeerNiEscribir());
		levelStudies.setI11NoSabeLeerNiEscribir(levelStudies.getI11NoSabeLeerNiEscribir()+levelStudies2.getI11NoSabeLeerNiEscribir());
		levelStudies.setI20TitulacioninferiorAlGradoDeEscolaridad(levelStudies.getI20TitulacioninferiorAlGradoDeEscolaridad()+levelStudies2.getI20TitulacioninferiorAlGradoDeEscolaridad());
		levelStudies.setI21SinEstudios(levelStudies.getI21SinEstudios()+levelStudies2.getI21SinEstudios());
		levelStudies.setI22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente(levelStudies.getI22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente()+levelStudies2.getI22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente());
		levelStudies.setI30GraduadoEscolarOEquivalente(levelStudies.getI30GraduadoEscolarOEquivalente()+levelStudies2.getI30GraduadoEscolarOEquivalente());
		levelStudies.setI31BachillerElemental(levelStudies.getI31BachillerElemental()+levelStudies2.getI31BachillerElemental());
		levelStudies.setI32FormacionProfesionalPrimerGradoOficialIndustrial(levelStudies.getI32FormacionProfesionalPrimerGradoOficialIndustrial()+levelStudies2.getI32FormacionProfesionalPrimerGradoOficialIndustrial());
		levelStudies.setI40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores(levelStudies.getI40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores()+levelStudies2.getI40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores());
		levelStudies.setI41FormacionProfesionalSegundoGradoMaestriaIndustrial(levelStudies.getI41FormacionProfesionalSegundoGradoMaestriaIndustrial()+levelStudies2.getI41FormacionProfesionalSegundoGradoMaestriaIndustrial());
		levelStudies.setI42BachillerSuperiorBup(levelStudies.getI42BachillerSuperiorBup()+levelStudies2.getI42BachillerSuperiorBup());
		levelStudies.setI43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc(levelStudies.getI43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc()+levelStudies2.getI43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc());
		levelStudies.setI44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares(levelStudies.getI44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares()+levelStudies2.getI44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares());
		levelStudies.setI45ArquitectoOIngenieroTecnico(levelStudies.getI45ArquitectoOIngenieroTecnico()+levelStudies2.getI45ArquitectoOIngenieroTecnico());
		levelStudies.setI46LicenciadoUniversitarioArquitectoOIngenieroSuperior(levelStudies.getI46LicenciadoUniversitarioArquitectoOIngenieroSuperior()+levelStudies2.getI46LicenciadoUniversitarioArquitectoOIngenieroSuperior());
		levelStudies.setI47TituladosDeEstudiosSuperioresNoUniversitarios(levelStudies.getI47TituladosDeEstudiosSuperioresNoUniversitarios()+levelStudies2.getI47TituladosDeEstudiosSuperioresNoUniversitarios());
		levelStudies.setI48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados(levelStudies.getI48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados()+levelStudies2.getI48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados());
	}
	
	public PostalDataOT[] getPostalDataFromIdTramosAndIdVia(String userName,String password,Integer idEntidad,Integer idFeatureStreet,Integer[] idFeatureStep) throws NamingException, ACException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		ElementsByGeometryLN geometryLN = new ElementsByGeometryLN();
		ArrayList<Integer> idFStep = new ArrayList<Integer>(Arrays.asList(idFeatureStep));
		ArrayList<PostalDataOT> result = geometryLN.getPostalDataFromTramo(userId, idEntidad, idFeatureStreet, idFStep);
		return result.toArray(new PostalDataOT[result.size()]);
	}
	public PortalStepRelationOT[] getPortalStepRelations(String userName,String password,Integer idEntidad,Integer idFeatureStreet,Integer[] idFeatureStep) throws NamingException, ACException{
		UserValidationLN userValidationLN = new UserValidationLN();
		userValidationLN.getActiveAndValidatedUserId(userName, password);
		Integer userId = userValidationLN.getUserId(userName);
		ElementsByGeometryLN geometryLN = new ElementsByGeometryLN();
		ArrayList<Integer> idFStep = new ArrayList<Integer>(Arrays.asList(idFeatureStep));
		ArrayList<PortalStepRelationOT> result = geometryLN.getRelatedPortalsByIdVia(userId, idEntidad, idFeatureStreet, idFStep);
		return result.toArray(new PortalStepRelationOT[result.size()]);
	}
	private void parseGeomarketingOTToGeoMarketingOT2(GeoMarketingOT result,
			GeoMarketingOT2 result2) {
		result2.setExternalValue(result.getExternalValue());
		result2.setForeignHabitants(result.getForeignHabitants());
		result2.setNumFemales(result.getNumFemales());
		result2.setNumHabitants(result.getNumHabitants());
		result2.setNumMales(result.getNumMales());
		result2.setRanges(transformRanges(result.getRanges()));
		result2.setS10(result.getLevelStudies().get10NoSabeLeerNiEscribir()+"");
		result2.setS20(result.getLevelStudies().get20TitulacionInferiorAlGradoDeEscolaridad()+"");
		result2.setS30(result.getLevelStudies().get30GraduadoEscolarOEquivalente()+"");
		result2.setS40(result.getLevelStudies().get40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores()+"");
		result2.setSpanishHabitants(result.getSpanishHabitants());
		result2.setAttName(result.getAttributeName());
		result2.setIdFeature(result.getIdFeature());
		result2.setMunicipio(result.getMunicipio());
		
	}
	private String transformRanges(RangeData[] ranges) {
		String result = "";//:fin:valor;inicio:fin:valor;inicio:fin:valor:inicio::valor 
		for (int i = 0;i<ranges.length;i++){
			RangeData range = ranges[i];
			if(range.getStartRange()!=null && range.getEndRange()!=null)
				result +=range.getStartRange()+":"+range.getEndRange()+":"+range.getValue();
			else if (range.getStartRange()!=null)
				result +=range.getStartRange()+"::"+range.getValue();
			else if (range.getEndRange()!=null)
				result +=":"+range.getEndRange()+":"+range.getValue();
			if(i+1<ranges.length)
				result+=";";
		}
		return result;
	}
	
}
