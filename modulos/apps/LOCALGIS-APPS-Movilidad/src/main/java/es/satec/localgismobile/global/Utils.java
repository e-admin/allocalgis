/**
 * Utils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.global;

import java.util.HashMap;

import org.eclipse.swt.widgets.Label;

import com.geopista.feature.GeopistaSchema;
import com.tinyline.svg.SVGNode;

public class Utils {

	public static boolean isInArray(Object[] objArray, Object obj) {
		for (int i = 0; i < objArray.length; i++) {
			if (obj.equals(objArray[i])) {
				return true;
			}
		}
		return false;
	}

	// BORRAR
	public static boolean containInArray(Object[] objArray, Object obj) {
		for (int i = 0; i < objArray.length; i++) {
			if (obj.toString().contains(objArray[i].toString())) {
				return true;
			}
		}
		return false;
	}

//	public static String contains(Object[] objArray, Object obj) {
//
//		return "";
//	}

	public static String getEIELValue(SVGNode node, String domainName) {
		for (int i = 0; i < node.nameAtts.size(); i++) {
			if (node.parent.getValueLayertAtt(i).equals(domainName))
				return node.getValueLayertAtt(i);
		}
		return null;
	}

//	public static String getIdMunicipio(SVGNode node) {
//		return getEIELValue(node, Constants.DOMAINFIELD_ID_MUNICIPIO);
//	}

//	public static String getCodProvincia(SVGNode node) {
//		return getCodProvincia(getIdMunicipio(node));
//	}

//	public static String getCodMunicipio(SVGNode node) {
//		return getCodMunicipio(getIdMunicipio(node));
//	}

	public static String getCodProvincia(String idMunicipio) {
		if (idMunicipio != null && idMunicipio.length() == 5)
			return idMunicipio.substring(0, 2);
		return null;
	}

	public static String getCodMunicipio(String idMunicipio) {
		if (idMunicipio != null && idMunicipio.length() == 5)
			return idMunicipio.substring(2, 5);
		return null;
	}

//	public static String getEIELClave(SVGNode node) {
//		return getEIELValue(node, Constants.DOMAINFIELD_CLAVE);
//	}

	public static void fillEielControlsINE(Label informacion, 
			HashMap controls, GeopistaSchema geoSche, String layerId, boolean enabled) {
		//fillEielControlsINE(informacion, Constants.DOMAINFIELD_ID_MUNICIPIO, controls, geoSche, layerId, enabled);	
	}
	
	public static void fillEielControlsINE(Label informacion, String attName,
			HashMap controls, GeopistaSchema geoSche, String layerId, boolean enabled) {
//		if (Utils.isInArray(Constants.TIPOS_EIEL, layerId) && attName.equals(Constants.DOMAINFIELD_ID_MUNICIPIO) && controls.get(Constants.DOMAINFIELD_ID_MUNICIPIO) != null) {
//			Combo idMunicip = ((Combo) controls
//					.get(Constants.DOMAINFIELD_ID_MUNICIPIO));
//			Domain domi = geoSche
//					.getAttributeDomain(Constants.DOMAINFIELD_ID_MUNICIPIO);
//			Iterator it = domi.getChildren().iterator();
//			while (it.hasNext()) {
//				CodedEntryDomain codedEd = (CodedEntryDomain) it.next();
//				if (codedEd.getDescription().equals(idMunicip.getText())) {
//					if (controls.get(Constants.DOMAINFIELD_CODPROVINCIA) != null) {
//						Text codprovText = (Text) controls
//								.get(Constants.DOMAINFIELD_CODPROVINCIA);
//						codprovText.setText(Utils.getCodProvincia(codedEd
//								.getName()));
//						ControlsDomainFactory
//								.datosDeleteErrorInformacion(informacion,
//										Constants.DOMAINFIELD_CODPROVINCIA);
//						codprovText.setEnabled(enabled);
//					}
//					if (controls.get(Constants.DOMAINFIELD_CODMUNICIPIO) != null) {
//						Text codmunicText = (Text) controls
//								.get(Constants.DOMAINFIELD_CODMUNICIPIO);
//						codmunicText.setText(Utils.getCodMunicipio(codedEd
//								.getName()));
//						ControlsDomainFactory
//								.datosDeleteErrorInformacion(informacion,
//										Constants.DOMAINFIELD_CODMUNICIPIO);
//						codmunicText.setEnabled(enabled);
//					}
//				}
//			}
//		}
	}
	
	public static void fillEielControlsClave(Label informacion,
			HashMap controls, GeopistaSchema geoSche, String layerId, boolean enabled) {
		//fillEielControlsClave(informacion, Constants.DOMAINFIELD_CLAVE, controls, geoSche, layerId, enabled);
	}
	
	public static void fillEielControlsClave(Label informacion, String attName,
			HashMap controls, GeopistaSchema geoSche, String layerId, boolean enabled) {
//		if (Utils.isInArray(Constants.TIPOS_EIEL, layerId) && attName.equals(Constants.DOMAINFIELD_CLAVE) && controls.get(Constants.DOMAINFIELD_CLAVE) != null) {
//			Text clave = (Text) controls
//					.get(Constants.DOMAINFIELD_CLAVE);
//			ControlsDomainFactory
//			.datosDeleteErrorInformacion(informacion,
//					Constants.DOMAINFIELD_CLAVE);		
//			clave.setEnabled(enabled);
//		}
	}
	
	  
	private static boolean isNumeric(String number){	  
	      try {	 
	    	  Integer.parseInt(number);	 
	    	  return true;	  
	      } catch (NumberFormatException e){	 
	    	  return false;	 
	      }
	 
	}

}
