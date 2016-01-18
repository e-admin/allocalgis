/**
 * ConversionIgnClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestrofip.ui.plugin.ign;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.HttpStatus;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ConversionIgnClient {
	
	private final static String ENCODE_SPACE = "%20";
	private final static String STRING_COMA = ",";
	private static AppContext appContext = (AppContext) AppContext.getApplicationContext();
	
	public static Geometry transformIGN_IDEE(Coordinate coord, String sourceCRS, String targetCRS) throws Exception{
		String urlIgnRWS = appContext.getString(UserPreferenceConstants.URL_IGN_WS);
		Geometry geometry = null;
		StringBuffer xml = new StringBuffer();
		xml.append("<Transform").append(ENCODE_SPACE).append("xmlns=\"http://www.opengeospatial.net/wcts\"").append(ENCODE_SPACE).append("xmlns:gml=\"http://www.opengis.net/gml\"").append(ENCODE_SPACE).append("service=\"WCTS\"").append(ENCODE_SPACE).append("version=\"1.0.0\">")
			.append("<SourceCRS>urn:ogc:def:crs:").append(sourceCRS).append("</SourceCRS>")
			.append("<TargetCRS>urn:ogc:def:crs:").append(targetCRS).append("</TargetCRS>")
			.append("<InputData>")
			.append("<gml:FeatureCollection>")
			.append("<gml:featureMember>")
			.append("<Point>")
			.append("<gml:pointProperty>")
			.append("<gml:Point").append(ENCODE_SPACE).append("srsName=\"epsg:").append(sourceCRS).append("\">")
			.append("<gml:pos").append(ENCODE_SPACE).append("srsDimension=\"2\">").append(coord.x).append(ENCODE_SPACE).append(coord.y).append("</gml:pos>")
			.append("</gml:Point>")
			.append("</gml:pointProperty>")
			.append("</Point>")
			.append("</gml:featureMember>")
			.append("</gml:FeatureCollection>")
			.append("</InputData>")
			.append("<OutputFormat>text/xml;").append(ENCODE_SPACE).append("gmlVersion=3.1.0</OutputFormat>")
			.append("</Transform>");
		try {
	
			URL obj = new URL(urlIgnRWS+"?"+xml.toString());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			int responseCode = con.getResponseCode();
			if(responseCode==  HttpStatus.SC_OK){
				geometry = getGeometrias( con.getInputStream(), "");
				
			}
			else{
				JOptionPane.showMessageDialog(appContext.getMainFrame(),
						"No se ha podido establecer conectar con el Conversor de IGN.\nPor favor revise el valor \"Url WS conversor IGN.\"", "Error",
					JOptionPane.ERROR_MESSAGE);
			}
		
		} catch (Exception e) {		
			e.printStackTrace();
			JOptionPane.showMessageDialog(appContext.getMainFrame(),
					"No se ha podido establecer la conectar con el Conversor de IGN.\nPor favor revise el valor \"Url WS conversor IGN.\"", "Error",
				JOptionPane.ERROR_MESSAGE);
			throw e;
		}
		return geometry;
	}
	
	private static Geometry getGeometrias(InputStream inputStream, String typeName) throws IOException {

		Geometry geometry = null;
		String result = "";
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document doc = null;
		try {
			doc = builder.build(inputStream);
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		if (null != doc) {
			Element element = doc.getRootElement();

			for (Object object : element.getChildren()) {

				for(Object objectFc: ((Element)object).getChildren()){
					if ("featureMember".equals(((Element) objectFc).getName())) {
						
						for(Object objectMember: ((Element)objectFc).getChildren()){
							if ("Point".equals(((Element) objectMember).getName())) {
								
								for(Object objectPoint: ((Element)objectMember).getChildren()){
									if ("pointProperty".equals(((Element) objectPoint).getName())) {
										
										for(Object objectPointPro: ((Element)objectPoint).getChildren()){
											if ("Point".equals(((Element) objectPointPro).getName())) {								
												
												for(Object objectCoordinates: ((Element)objectPointPro).getChildren()){
													if ("coordinates".equals(((Element) objectCoordinates).getName())) {
														result = ((Text) ((Element)objectCoordinates).getContent().get(0)).getValue();
														
														StringTokenizer stringTokenizer = new StringTokenizer(result,STRING_COMA);
														Coordinate coordinates = new Coordinate();
														coordinates = new Coordinate(Double.parseDouble(stringTokenizer.nextToken(STRING_COMA)),
																							Double.parseDouble(stringTokenizer.nextToken(STRING_COMA)));
							
														GeometryFactory gf = new GeometryFactory();
														geometry = gf.createPoint(coordinates);				
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
							
		return geometry;
	}
}
