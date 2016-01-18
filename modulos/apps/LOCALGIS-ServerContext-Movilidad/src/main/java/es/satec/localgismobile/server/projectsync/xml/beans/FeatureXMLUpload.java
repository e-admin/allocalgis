/**
 * FeatureXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.beans;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.satec.localgismobile.server.projectsync.xml.ConstantsXMLUpload;

/**
 * <path d="M233.9375 63.609375 L236.97266 65.30078 241.48438 67.82031 241.0586 68.39844 238.60938 71.72656 
 * 234.47656 68.90625 230.11328 66.71875 232.1914 62.54297 233.9375 63.609375 z" v1="1745" v2="0668205UN9106N" 
 * v3="4083" v4="10000" v5="" v6="" v7="167" v8="6" v9="" v10="" v11="" v12="" v13="" v14="" v15="" v16="HO" 
 * v17="30.0855168380611" v18="46.7018938064575" v19="2004-12-03" v20="" id="30146205" changeType="2" 
 * changeTimestamp="1240384955527" imageURLs="http%3A%2F%2Fwww.spanien-bilder.com%2Fdata%2Fmedia%2F56%2Fwappen-real-sporting-de-gijon.gif " />
 * @author irodriguez
 *
 */
public class FeatureXMLUpload {

	private List<String> imageUrls;							//conjunto de imágenes asociadas a una feature
	private String changeType;								//tipo de cambio (insercción, borrado o modificacion)
	private String id;
	private String idFeature;
	private String idMunicipioFeature;
	private String name;
	private List<AttributeXMLUpload> attLayerList;			//atributos de capa
	private List<AttributeXMLUpload> attFeatList;			//v1="5211", v2="8793307UN8189S", points="21.316406 94.71875 34.121094
	private List<AttributeXMLUpload> dataBaseAttFeatList; 	//ID de parcela=5211, Referencia catastral=8793307UN8189S, ...
	private String geometry;
	
	public FeatureXMLUpload(String name, List<AttributeXMLUpload> attLayerList, List<AttributeXMLUpload> attFeatList) {
		super();
		this.name = name;
		this.attLayerList = attLayerList;
		this.attFeatList = attFeatList;
		updateInternalFields();
		updateDataBaseFeatList();
		updateFeatureGeometry();
	}
	public String getChangeType() {
		return changeType;
	}
	public String getId() {
		return id;
	}
	public String getIdFeature() {
		return idFeature;
	}
	public String getIdMunicipioFeature() {
		return idMunicipioFeature;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AttributeXMLUpload> getAttFeatList() {
		return attFeatList;
	}
	public String getGeometry() {
		return geometry;
	}
	public void setAttFeatList(List<AttributeXMLUpload> attributeList) {
		this.attFeatList = attributeList;
		updateInternalFields();
		updateDataBaseFeatList();
		updateFeatureGeometry();
	}
	public List<AttributeXMLUpload> getAttLayerList() {
		return attLayerList;
	}
	public void setAttLayerList(List<AttributeXMLUpload> attLayerList) {
		this.attLayerList = attLayerList;
		updateInternalFields();
		updateDataBaseFeatList();
		updateFeatureGeometry();
	}
	public List<AttributeXMLUpload> getDataBaseAttFeatList() {
		return dataBaseAttFeatList;
	}
	public List<String> getImageUrls() {
		return imageUrls;
	}
	/**
	 * Imprime la feature en formato XML
	 * @return
	 */
	public String toXml(){
		StringBuffer str = new StringBuffer();
		str.append("<" + name + " ");
		for (int i = 0; i < attFeatList.size(); i++) {
			str.append(attFeatList.get(i).getKey() + "=\"" + attFeatList.get(i).getValue() + "\" ");
		}
		str.append(">");
		return str.toString();
	}
	/**
	 * Actualiza los atributos internos (atajo para id, v1 y changeType)
	 */
	private void updateInternalFields(){
		String key = null;
		AttributeXMLUpload attributeXMLUpload = null;
		imageUrls = new ArrayList<String>();
		String valueAtr = null;
		for (int i = 0; i < attFeatList.size(); i++) {
			attributeXMLUpload = attFeatList.get(i);
			key = attributeXMLUpload.getKey();
			valueAtr = attributeXMLUpload.getValue();
			if(key.equals(ConstantsXMLUpload.ATT_ID)){
				id = valueAtr;
			}
			else if(key.equals(ConstantsXMLUpload.ATT_ID_FEATURE)){
				idFeature = valueAtr;
			}	
			else if(key.equals(ConstantsXMLUpload.ATT_ID_MUNICIPIO_FEATURE)){
				idMunicipioFeature = valueAtr;
			}	
			else if(key.equals(ConstantsXMLUpload.ATT_CHANGE_TYPE)){
				changeType = valueAtr;
			}
			else if(key.equals(ConstantsXMLUpload.ATT_IMAGE_URLS)){
				String[] splitValues = valueAtr.split(ConstantsXMLUpload.SPLIT_CHAR_ATT_URL);
				for (int j = 0; j < splitValues.length; j++) {
					if(splitValues[j]!=null && splitValues[j].length()>0){
						try {
							imageUrls.add(URLDecoder.decode(splitValues[j], "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}				
			}
		}
	}
	/**
	 * Actualiza los atributos de base de datos la feature
	 * Es decir, saca el valor de los l1, l2, l3... y de los v1, v2, v3... y enlaza todo en una nueva lista
	 * para tener disponible rápidamente NombreAtributo1=ValorAtributo1...
	 */
	private void updateDataBaseFeatList() {
		Pattern pattern = Pattern.compile("l(\\d{1,3})"); //l1, l2, l3,...,l999
		Matcher matcher = null;
		AttributeXMLUpload attributeLayer = null;
		AttributeXMLUpload attributeFeat = null;
		dataBaseAttFeatList = new ArrayList<AttributeXMLUpload>();
		for (int i = 0; i < attLayerList.size(); i++) {
			attributeLayer = attLayerList.get(i);
			matcher = pattern.matcher(attributeLayer.getKey());
			//si es del tipo l1, l2, l3,...,l999
			if(matcher.matches()){
				for (int j = 0; j < attFeatList.size(); j++) {
					attributeFeat = attFeatList.get(j);
					//si encontramos un vi que coincida con el li
					if(attributeFeat.getKey().equals("v"+matcher.group(1))){
						dataBaseAttFeatList.add(new AttributeXMLUpload(attributeLayer.getValue(), attributeFeat.getValue()));
						break;
					}
				}
			}
		}
	}
	/**
	 * Actualiza la información sobre la geometría de la feature
	 * @return
	 */
	private void updateFeatureGeometry(){
		String str = "";
		String key = null;
		Pattern pattern = Pattern.compile("v(\\d{1,3})"); //v1, v2, v3,...,v999
		Matcher matcher = null;
		for (int i = 0; i < attFeatList.size(); i++) {
			key = attFeatList.get(i).getKey();
			if(key.equals(ConstantsXMLUpload.ATT_CHANGE_TYPE) || key.equals(ConstantsXMLUpload.ATT_ID) 
					|| key.equals(ConstantsXMLUpload.ATT_SYSTEM_ID_LAYER) || key.equals(ConstantsXMLUpload.ATT_CHANGE_TIMESTAMP)
					|| key.equals(ConstantsXMLUpload.ATT_OPACITY)){
				continue;
			}
			else {
				matcher = pattern.matcher(key);
				//si es del tipo l1, l2, l3,...,l999
				if(matcher.matches()){
					continue;
				}
			}
			
			str += key + "=\"" + attFeatList.get(i).getValue() + "\" ";
		}	
		
		geometry = str;
	}
	
	/**
	 * Imprime el error que se produce en una operación sobre una feature
	 * en el formato aceptado por el cliente PDA
	 * @param errorMsg
	 * @return
	 */
	public String printFeatureError(String errorMsg){
		return "<" + name + " id=" + "\"" + id + "\" v1=\"" + errorMsg + "\" "+geometry+"/>";
	}
	
}
