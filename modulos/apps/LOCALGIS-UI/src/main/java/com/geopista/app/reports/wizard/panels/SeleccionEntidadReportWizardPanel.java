/**
 * SeleccionEntidadReportWizardPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.wizard.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JRImage;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.reports.ReportsManager;
import com.geopista.app.reports.maps.MapImageExpressionManager;
import com.geopista.app.reports.maps.MapImageSettings;
import com.geopista.app.reports.parameters.editors.MapParameterEditor;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaListener;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.cursortool.FeatureInfoTool;

/*
 * Esta clase sustituye a la SeleccionParcelaReportWizardPanel, que ya no se usa.
 */

public class SeleccionEntidadReportWizardPanel extends ReportWizardPanel {
	
	 
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SeleccionEntidadReportWizardPanel.class);
    
	private MapParameterEditor mapParameterEditor;
    
	private JLabel lblSeleccioneEntidad;	

	private GeopistaEditor geopistaEditor = null;
	
	private boolean mapLoaded = false;
	
	private String valor;
	
	private MapImageSettings mapImageSettings;
	
	// Contexto de la aplicacion geopista
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
	
	public SeleccionEntidadReportWizardPanel(MapParameterEditor mapParameterEditor){
		super();
		this.mapParameterEditor = mapParameterEditor;
		try {
			initForm();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	protected void initForm() throws Exception {
		BorderLayout borderLayout = new BorderLayout();
		pnlGeneral.setLayout(borderLayout);
		
		JPanel panelTitle = new JPanel();
		FlowLayout flowLayoutTitle = new FlowLayout();
		flowLayoutTitle.setAlignment(FlowLayout.LEFT);
		panelTitle.setLayout(flowLayoutTitle);
		pnlGeneral.add(panelTitle, BorderLayout.NORTH);
		
		lblSeleccioneEntidad = new JLabel();		
		Font fontSeleccioneEntidad = lblSeleccioneEntidad.getFont();
		fontSeleccioneEntidad = fontSeleccioneEntidad.deriveFont(
				fontSeleccioneEntidad.getStyle() ^ Font.BOLD);
		lblSeleccioneEntidad.setFont(fontSeleccioneEntidad);
		panelTitle.add(lblSeleccioneEntidad);
		String text = appContext.getI18nString("informes.wizard.seleccionentidad.seleccioneentidad");
		if (mapParameterEditor.getParameter().getDescription() != null &&
				mapParameterEditor.getParameter().getDescription().length() > 0) {
			text += " " + mapParameterEditor.getParameter().getDescription();
		}
		else {
			text += " " + mapParameterEditor.getParameter().getName();
		}
		lblSeleccioneEntidad.setText(text);
		
		//geopistaEditor = new GeopistaEditor(appContext.getString("fichero.vacio"));
		geopistaEditor = new GeopistaEditor("workbench-properties-localgis-simple.xml");
		geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
		geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
		geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");
		geopistaEditor.getToolBar().addCursorTool("FeatureInfoTool", new FeatureInfoTool());
		geopistaEditor.addCursorTool("Select One Item tool", "com.geopista.ui.cursortool.selectoneitem.SelectOneItemTool");

		pnlGeneral.add(geopistaEditor, BorderLayout.CENTER);
		
		geopistaEditor.setVisible(true);
		geopistaEditor.showLayerName(true);
		

		JPanel panelInfoEntidad = new JPanel();
		GridLayout gridLayoutInfoEntidad = new GridLayout(2,1);
		panelInfoEntidad.setLayout(gridLayoutInfoEntidad);
		pnlGeneral.add(panelInfoEntidad, BorderLayout.SOUTH);

		geopistaEditor.addGeopistaListener(new GeopistaListener(){

			public void selectionChanged(IAbstractSelection abtractSelection) {
				
				try {
									        
					Layer layer = geopistaEditor.getLayerManager().getLayer(mapImageSettings.getCapa());
					ArrayList featuresCollection = (ArrayList) abtractSelection.getFeaturesWithSelectedItems(layer);
					abtractSelection.unselectItems();
					
					Iterator featuresCollectionIter = featuresCollection.iterator();

					if(!featuresCollectionIter.hasNext()){
						//System.out.println("Features seleccionadas:"+geopistaEditor.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems().size());
						logger.error("No existen elementos en la capa seleccionada del informe seleccionado. Capa:"+layer.getName());
						getWizardContext().inputChanged();
						return;
					}

					// Quedarse solo con la primera y deseleccionar las demás
					Feature actualFeature = (Feature) featuresCollectionIter.next();
					abtractSelection.selectItems(layer, actualFeature);
					
//					actualFeature.getSchema()
					
					geopistaEditor.zoomTo(actualFeature);
					getWizardContext().inputChanged();

					//Recuperar el dato:
					//String parametro = mapParameterEditor.getParameter().getName();
					String parametro = mapImageSettings.getColumna();
					String traduccion = ((GeopistaSchema)actualFeature.getSchema()).getAttributeByColumn(parametro);
					//com.geopista.model.GeopistaLayer columna = ((GeopistaSchema)actualFeature.getSchema()).getGeopistalayer();

					valor = actualFeature.getString(traduccion);
					System.out.println("Identificador encontrado:"+parametro+" ("+traduccion+"): "+valor);
					logger.info("Identificador encontrado:"+parametro+" ("+traduccion+"): "+valor);

			/*
					ArrayList layersCollection = (ArrayList) abtractSelection.getLayersWithSelectedItems();

					Iterator layersCollectionIter = layersCollection.iterator();
					
					
					
					if(!layersCollectionIter.hasNext())
						return;
					
					Layer actualLayer = (Layer) layersCollectionIter.next();
				*/	
				} catch (Exception e){
					e.printStackTrace();
				}
			}

			public void featureAdded(FeatureEvent e){
				// No hacer nada
			}

			public void featureRemoved(FeatureEvent e){
				// No hacer nada
			}

			public void featureModified(FeatureEvent e){
				// No hacer nada
			}

			public void featureActioned(IAbstractSelection abtractSelection){
				// No hacer nada
			}
		});
	}
	
	protected void enteredPanelFromLeft(Map dataMap) {
		try {
			String mapa;
			if (!mapLoaded){
				// Obtener el mapa a mostrar
				List interactiveMaps = ReportsManager.getInstance().findInteractiveMapImagesReferencingParameter(
					reportWizard.getReport(), mapParameterEditor.getParameter(), null);
				if (interactiveMaps == null || interactiveMaps.isEmpty()) {
					mapa = appContext.getString("url.mapa.catastro");
				}
				else {
					JRImage image = (JRImage) interactiveMaps.get(0);
					mapImageSettings = MapImageExpressionManager.parseExpression(image.getExpression().getText());
					mapa = appContext.getString("url.mapa") + mapImageSettings.getMapId();
				}

				geopistaEditor.loadMap(mapa);
				mapLoaded = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception {		
		
		HashMap defaultParameters=new HashMap();
		defaultParameters.put("id_municipio",String.valueOf(AppContext.getIdMunicipio()));
		defaultParameters.put("locale",AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES"));
		reportWizard.fillParameters(defaultParameters);		
		
		
		mapParameterEditor.fillParameter(valor);
	}

	public boolean isInputValid() {
		Collection lista = null;
		lista = geopistaEditor.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
		if (lista.size()>=1){
			if (reportWizard.acceso()) {
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

}
