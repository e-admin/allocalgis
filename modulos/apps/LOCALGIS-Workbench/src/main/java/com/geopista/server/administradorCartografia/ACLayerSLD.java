/**
 * ACLayerSLD.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deegree.graphics.sld.Rule;
import org.deegree_impl.graphics.sld.FeatureTypeStyle_Impl;

import com.geopista.model.IGeopistaLayer;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;

public class ACLayerSLD {
	
    
    private static final Log	logger	= LogFactory.getLog(ACLayerFamily.class);
    
	public  static void actualizarReglaPintado(IGeopistaLayer layer){
		
		try {
			//Si no tiene atributo "Revision Expirada" salta una excepcion y no actualizar el estilo
			if (layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("Revision Expirada")==-1)
				return;
			
			String estiloPintado="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><NamedLayer><Name>GENERIC_LAYER</Name><UserStyle><Name>GENERIC_LAYER</Name><Title>GENERIC_LAYER</Title><Abstract>GENERIC_LAYER</Abstract><FeatureTypeStyle><Name>GENERIC_LAYER</Name><!--<Rule><Name>Revision Expirada Lineas</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9899999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name='stroke'>#6666ff</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter><CssParameter name='stroke-dasharray'>10.0,10.0,2.0,10.0</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>7.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule>--><Rule><Name>Revision expirada Temporal Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9899999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><!--<Label><ogc:PropertyName>Revision Expirada</ogc:PropertyName></Label>--><Label>Elemento Temporal</Label><Font><CssParameter name='font-style'>normal</CssParameter><CssParameter name='font-size'>18.0</CssParameter><CssParameter name='font-color'>#6666ff</CssParameter><CssParameter name='font-family'>Agency FB</CssParameter><CssParameter name='font-weight'>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Revision expirada Publicable Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9799999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><!--<Label><ogc:PropertyName>Revision Expirada</ogc:PropertyName></Label>--><Label>Elemento Publicable</Label><Font><CssParameter name='font-style'>normal</CssParameter><CssParameter name='font-size'>18.0</CssParameter><CssParameter name='font-color'>#6666ff</CssParameter><CssParameter name='font-family'>Agency FB</CssParameter><CssParameter name='font-weight'>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Revision expirada Borrable Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9599999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><!--<Label><ogc:PropertyName>Revision Expirada</ogc:PropertyName></Label>--><Label>Elemento Borrable</Label><Font><CssParameter name='font-style'>normal</CssParameter><CssParameter name='font-size'>18.0</CssParameter><CssParameter name='font-color'>#ff0000</CssParameter><CssParameter name='font-family'>Agency FB</CssParameter><CssParameter name='font-weight'>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
			SLDStyleImpl sldStyle = (SLDStyleImpl)layer.getStyle(SLDStyleImpl.class);
			FeatureTypeStyle_Impl featureTypeStyle = (FeatureTypeStyle_Impl)sldStyle.getDefaultStyle().getFeatureTypeStyles()[0];
			Rule [] listaReglas=featureTypeStyle.getRules();
			//Symbolizer_Impl symbolizer = (Symbolizer_Impl)featureTypeStyle.getRules()[0].getSymbolizers()[0];
			
			
			//SLDStyle styleRevisionExpiradaLineas=SLDFactory.createSLDStyle("estilo_lineas_revision_expirada.xml", "Estilo Lineas Revision Expirada", "GENERIC_LAYER");
			SLDStyle styleRevisionExpirada=SLDFactory.createSLDStyle(estiloPintado, "GENERIC_LAYER");
			if (styleRevisionExpirada==null){
				logger.info("Estilo de pintado erroneo");
			}
			else{	
				//logger.info("Estilo de pintado correcto");
				
				FeatureTypeStyle_Impl featureTypeStyleRevisionExpirada = (FeatureTypeStyle_Impl)styleRevisionExpirada.getUserStyle("GENERIC_LAYER").getFeatureTypeStyles()[0];
				Rule [] listaReglasRevisionExpirada=featureTypeStyleRevisionExpirada.getRules();
					    		
				for (int i=0;i<listaReglasRevisionExpirada.length;i++){
					Rule ruleRevision=listaReglasRevisionExpirada[i];
					
					//Verificamos si ya estuviera aplicada
					for (int j=0;j<listaReglas.length;j++){
						Rule ruleActual=listaReglas[j];
						if (ruleRevision.getName().equals(ruleActual.getName())){
							featureTypeStyle.removeRule(ruleActual);
							break;
						}
					}
				featureTypeStyle.addRule(listaReglasRevisionExpirada[i]);
				}
				
			}
		} catch (Exception e) {
			//e.printStackTrace();
			
		}
    	
    }
}
