/**
 * SeleccionEscalaReportWizardPanel.java
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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JRImage;

import com.geopista.app.AppContext;
import com.geopista.app.reports.ReportsManager;
import com.geopista.app.reports.ScaleUtils;
import com.geopista.app.reports.maps.MapImageExpressionManager;
import com.geopista.app.reports.maps.MapImageFactory;
import com.geopista.app.reports.maps.MapImageSettings;
import com.geopista.app.reports.parameters.editors.MapParameterEditor;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.reports.scale.InstallPrintSectionPlugin;
import com.geopista.ui.reports.scale.PrintSectionPlugin;
import com.geopista.ui.reports.scale.PrintSectionRenderer;
import com.geopista.ui.reports.zoom.GeopistaEditorZoomBar;
import com.geopista.ui.reports.zoom.InstallGeopistaEditorZoomBarPlugIn;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class SeleccionEscalaReportWizardPanel extends ReportWizardPanel {
	
	// Contexto de la aplicaicon geopista
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
	
	private MapParameterEditor mapParameterEditor;
	private JRImage image;
	private String idFeature;
	private boolean mapLoaded = false;
	
	private GeopistaEditor geopistaEditor;
	private JLabel lblSeleccioneEscala;
 
	private MapImageSettings mapImageSettings;

	public SeleccionEscalaReportWizardPanel(MapParameterEditor mapParameterEditor, JRImage jrImage){
		super();
		this.mapParameterEditor = mapParameterEditor;
		this.image = jrImage;
		try {
			initForm();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void enteredPanelFromLeft(Map dataMap) {
		try {
			String mapa;
			if (!mapLoaded){
				// Obtener el mapa a mostrar
				List images = ReportsManager.getInstance().findImagesReferencingParameter(
					reportWizard.getReport(), mapParameterEditor.getParameter());
				if (images == null || images.isEmpty()) {
					mapa = appContext.getString("url.mapa.catastro");
				}
				else {
					JRImage image = (JRImage) images.get(0);
					mapImageSettings = MapImageExpressionManager.parseExpression(image.getExpression().getText());
					mapa = appContext.getString("url.mapa") + mapImageSettings.getMapId();
				}

				geopistaEditor.loadMap(mapa);
				mapLoaded = true;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		this.idFeature = mapParameterEditor.getFilledParameterValue();
	}
	
	protected void initForm() throws Exception {
		BorderLayout borderLayout = new BorderLayout();
		pnlGeneral.setLayout(borderLayout);
		
		JPanel panelTitle = new JPanel();
		FlowLayout flowLayoutTitle = new FlowLayout();
		flowLayoutTitle.setAlignment(FlowLayout.LEFT);
		panelTitle.setLayout(flowLayoutTitle);
		pnlGeneral.add(panelTitle, BorderLayout.NORTH);
		
		lblSeleccioneEscala = new JLabel();		
		Font fontSeleccioneEscala = lblSeleccioneEscala.getFont();
		fontSeleccioneEscala = fontSeleccioneEscala.deriveFont(
				fontSeleccioneEscala.getStyle() ^ Font.BOLD);
		lblSeleccioneEscala.setFont(fontSeleccioneEscala);
		panelTitle.add(lblSeleccioneEscala);
		String text = appContext.getI18nString("informes.wizard.seleccionescala.seleccionescala");	
		text += " " + image.getKey();
		lblSeleccioneEscala.setText(text);
		panelTitle.add(lblSeleccioneEscala);
				
		geopistaEditor = new GeopistaEditor(appContext.getString("fichero.vacio"));
		geopistaEditor.showLayerName(false);
		pnlGeneral.add(geopistaEditor, BorderLayout.CENTER);
		
		InstallGeopistaEditorZoomBarPlugIn installZoomBarPlugIn = new InstallGeopistaEditorZoomBarPlugIn();
		GeopistaEditorZoomBar zoomBar = installZoomBarPlugIn.getInitializedPlugin(new PlugInContext(geopistaEditor.getContext(), null, null,
                null, null));		
		zoomBar.setPrintAreaDimensions(image.getWidth(), image.getHeight());
		
		InstallPrintSectionPlugin installPrintSectionPlugin = 
			new InstallPrintSectionPlugin(geopistaEditor.getLayerViewPanel(), image.getWidth(), image.getHeight());
		PlugInContext pluginContext = new PlugInContext(geopistaEditor.getContext(),
				geopistaEditor.getTask(), geopistaEditor, null, geopistaEditor.getLayerViewPanel());
		installPrintSectionPlugin.initialize(pluginContext);		
		PrintSectionPlugin printSectionPlugin = new PrintSectionPlugin();		
		printSectionPlugin.initialize(pluginContext);
		
		PrintSectionRenderer.setEnabled(true, geopistaEditor.getLayerViewPanel());
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {					
				super.componentShown(e);
				try {
					selectEntidadById();
				} catch (Exception e1){
					e1.printStackTrace();
				}
			}				
		});
	}

	public void exitingToRight() throws Exception {
		String scale = "1:" + new Double(ScaleUtils.getReportPrintEnvelopeScale(
				geopistaEditor.getLayerViewPanel(), image.getHeight(), image.getWidth())).intValue();
		
		MapImageFactory.setInteractiveMapImageScale(image.getKey(), scale);
	}

	public boolean isInputValid() {
		return true;
	}
	
	private void selectEntidadById() {
		GeopistaLayer layer = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(mapImageSettings.getCapa());		
		layer.setActiva(true);
		
		Feature featureToSelect = null;
		Iterator featureIterator = layer.getFeatureCollectionWrapper().iterator();
		while (featureIterator.hasNext()){
			Feature currentFeature = (Feature) featureIterator.next();
			String id = currentFeature.getString(mapImageSettings.getAtributo());
			if (id != null && idFeature.equals(id)){
				featureToSelect = currentFeature;
				break;
			}			
		}
		
		if (featureToSelect == null){
			return;
		}
		
		geopistaEditor.getSelectionManager().getFeatureSelection().selectItems(layer, featureToSelect);
		try {
			geopistaEditor.zoomTo(featureToSelect);
		} catch (NoninvertibleTransformException e) {			
			e.printStackTrace();
		}
	}
}
