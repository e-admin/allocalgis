/**
 * UtilidadesAvisosPanels.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.interventions.utils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.geopista.feature.GeopistaFeature;
import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 *
 */
public class UtilidadesAvisosPanels {
	
	@SuppressWarnings("unchecked")
	public static void inicializarIdiomaAvisosPanels(){
		if (I18N.plugInsResourceBundle.get("avisospanels") == null){
			Locale loc=I18N.getLocaleAsObject();    
			ResourceBundle bundle = ResourceBundle.getBundle("com.localgis.app.gestionciudad.dialogs.interventions.language.GestionCiudad_InterventionPaneli18n",loc,UtilidadesAvisosPanels.class.getClassLoader());
			I18N.plugInsResourceBundle.put("avisospanels",bundle);
		}
	}	
	
	public static void packColumns(JTable table, int margin) {
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int c=0; c<table.getColumnCount(); c++) {
			if (c!=1)
				packColumn(table, c, 2);
		}
	}
	
	public static void packColumn(JTable table, int vColIndex, int margin) {
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.
		getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0;    

		// obtiene la anchura de la cabecera de la columna
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent(
				table, col.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;    

		// Obtine la anchura maxima de la coluna de
		for (int r=0; r<table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			comp = renderer.getTableCellRendererComponent(
					table, table.getValueAt(r, vColIndex), false, false, r,
					vColIndex);
			width = Math.max(width, comp.getPreferredSize().width);
		}    

		width += 2*margin;    

		// Configura el ancho
		col.setPreferredWidth(width);
	}
	
	public static ArrayList<Document> documentListToArrayList(Document[] list){
		if (list!=null && list.length > 0){
			ArrayList<Document> resultado = new ArrayList<Document>();
//			Iterator<Document> it = list.iterator();
//			int cont = 0;
//			while(it.hasNext()){
//				resultado.add[cont] = it.next();
//				cont++;
//			}
			for (int i = 0; i < list.length; i++){
				resultado.add(list[i]);
			}
			return resultado;
		}else{
			return new ArrayList<Document>();
		}
	}
	
	
	
	// TODO Eliminar esta funcion si no es necesaria, usada para log de pruebas
	public String listaDocumentosToString(Document[] listaDeDocumentos){
		String resultado = "";
		if(listaDeDocumentos!=null && listaDeDocumentos.length != 0){
			ArrayList<Document> lista = new ArrayList<Document>(Arrays.asList(listaDeDocumentos));
			Iterator<Document> it = lista.iterator();
			Document doc = it.next(); 
			resultado = "Documentos Asociados: " + doc.toString();
			int i = 1;
			while(it.hasNext()){
				doc = it.next();
				if (i == 3){
					resultado = resultado + "/n     ";
					i = 0;
				}
				i++;
				resultado = resultado + ", " + doc.toString();
			}
		}
		return resultado;
	}
	
	public static String DocumentListToParsedString(Document[] listaDeDocumentos){
		String resultado = "";
		if(listaDeDocumentos!=null && listaDeDocumentos.length != 0){
			ArrayList<Document> lista = new ArrayList<Document>(Arrays.asList(listaDeDocumentos));
			Iterator<Document> it = lista.iterator();
			Document doc = it.next(); 
			resultado = doc.toString();
			while(it.hasNext()){
				doc = it.next();
				resultado = resultado + "; " + doc.toString();
			}
		}
		return resultado;
	}
	
	
	public static int getFeatureSystemId(GeopistaFeature feat){
		int featureSystemId = -1;
		try{
			featureSystemId = Integer.parseInt(feat.getSystemId());
		}catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return featureSystemId;
	}

	public static String buildLayerFeaturesString(LayerFeatureBean[] layersFeatures) {
		if (layersFeatures!=null && layersFeatures.length>0){
			String resultado = "";
			for(int i=0; i < layersFeatures.length; i++){
				resultado = resultado + 
					layersFeatures[i].getIdLayer() + ":" + layersFeatures[i].getIdFeature() + ";";
			}
			return resultado;
		}	
		return null;
	}

	public static com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.LayerFeatureBean[] buildLayerFeaturesToFeatureLayerStubBean(
			LayerFeatureBean[] features) {
		if (features!=null && features.length>0){
			ArrayList<com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.LayerFeatureBean> resultado = new ArrayList<com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.LayerFeatureBean>();
			for(int i = 0; i < features.length; i ++){
				try{
					com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.LayerFeatureBean newLayerFeatureBean = new com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.LayerFeatureBean();
					newLayerFeatureBean.setIdLayer(features[i].getIdLayer());
					newLayerFeatureBean.setIdFeature(features[i].getIdFeature());
					resultado.add(newLayerFeatureBean);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			return resultado.toArray(new com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.LayerFeatureBean[resultado.size()]);
		}
		
		
		return null;
	}

}
