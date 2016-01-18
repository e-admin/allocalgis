package com.geopista.ui.wizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.reports.parameters.editors.MapParameterEditor;
import com.geopista.app.reports.wizard.panels.ReportWizardPanel;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;

public class SeleccionParcelaReportWizardPanel extends ReportWizardPanel {
	
	private MapParameterEditor mapParameterEditor;
    
	private JLabel lblSeleccioneParcela;	
	private JPanel pnlRustico = new JPanel();
	private JPanel pnlUrbano = new JPanel();
	private JLabel lblPoligonoRustico = new JLabel();
	private JTextField txtPoligonoRustico = new JTextField();
	private JLabel lblParcelaRustico = new JLabel();
	private JTextField txtParcelaRustico = new JTextField();
	private JTextField txtParcelaUrbano = new JTextField();
	private JLabel lblParcelaUrbano = new JLabel();
	private JTextField txtPoligonoUrbano = new JTextField();
	private JLabel lblPoligonoUrbano = new JLabel();
	private GeopistaEditor geopistaEditor = null;
	
	private boolean mapLoaded = false;
	
	// Informacion parcela	
	private String referenciaCatastral;
	
	// Contexto de la aplicaicon geopista
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
	
	public SeleccionParcelaReportWizardPanel(MapParameterEditor mapParameterEditor){
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
		
		lblSeleccioneParcela = new JLabel();		
		Font fontSeleccioneParcela = lblSeleccioneParcela.getFont();
		fontSeleccioneParcela = fontSeleccioneParcela.deriveFont(
				fontSeleccioneParcela.getStyle() ^ Font.BOLD);
		lblSeleccioneParcela.setFont(fontSeleccioneParcela);
		panelTitle.add(lblSeleccioneParcela);
		String text = appContext.getI18nString("informes.wizard.seleccionparcela.seleccioneparcela");
		if (mapParameterEditor.getParameter().getDescription() != null &&
				mapParameterEditor.getParameter().getDescription().length() > 0) {
			text += " " + mapParameterEditor.getParameter().getDescription();
		}
		else {
			text += " " + mapParameterEditor.getParameter().getName();
		}
		lblSeleccioneParcela.setText(text);
		
		geopistaEditor = new GeopistaEditor(appContext.getString("fichero.vacio"));
		geopistaEditor.showLayerName(false);
		geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
		geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
		geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");		
		pnlGeneral.add(geopistaEditor, BorderLayout.CENTER);

		JPanel panelInfoParcela = new JPanel();
		GridLayout gridLayoutInfoParcela = new GridLayout(2,1);
		panelInfoParcela.setLayout(gridLayoutInfoParcela);
		pnlGeneral.add(panelInfoParcela, BorderLayout.SOUTH);
		
		GridBagLayout pnlRusticoGridBagLayout = new GridBagLayout();
		pnlRustico.setBorder(BorderFactory.createTitledBorder(appContext.getI18nString("parcelas.rustica")));		
		pnlRustico.setLayout(pnlRusticoGridBagLayout);		
		panelInfoParcela.add(pnlRustico);
		
		Insets insets = new Insets(0,5,5,0);
		GridBagConstraints gridBagConstraints;		
		lblParcelaRustico.setText(appContext.getI18nString("informe.patrimonio.inventario.parcela"));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = insets;
		pnlRustico.add(lblParcelaRustico, gridBagConstraints);
		
		txtParcelaRustico.setBounds(new Rectangle(360, 15, 185, 20));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 3.0;
		gridBagConstraints.insets = insets;
		pnlRustico.add(txtParcelaRustico, gridBagConstraints);

		lblPoligonoRustico.setText(appContext.getI18nString("texto.poligono"));
		lblPoligonoRustico.setBounds(new Rectangle(10, 15, 65, 20));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = insets;
		pnlRustico.add(lblPoligonoRustico, gridBagConstraints);
				
		txtPoligonoRustico.setHorizontalAlignment(JTextField.RIGHT);
		txtPoligonoRustico.setText("");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 3.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;		
		gridBagConstraints.insets = insets;
		pnlRustico.add(txtPoligonoRustico, gridBagConstraints);		
		
		GridBagLayout pnlUrbanoGridBagLayout = new GridBagLayout();
		pnlUrbano.setBorder(BorderFactory.createTitledBorder(appContext.getI18nString("parcelas.urabana")));
		pnlUrbano.setLayout(pnlUrbanoGridBagLayout);
		panelInfoParcela.add(pnlUrbano);
				
		lblParcelaUrbano.setText(appContext.getI18nString("informe.patrimonio.inventario.parcela"));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = insets;
		pnlUrbano.add(lblParcelaUrbano, gridBagConstraints);
		
		txtParcelaUrbano.setHorizontalAlignment(JTextField.RIGHT);
		txtParcelaUrbano.setText("");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 3.0;
		gridBagConstraints.insets = insets;
		pnlUrbano.add(txtParcelaUrbano, gridBagConstraints);
		
		lblPoligonoUrbano.setText(appContext.getI18nString("texto.manzana"));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = insets;
		pnlUrbano.add(lblPoligonoUrbano, gridBagConstraints);
		
		txtPoligonoUrbano.setHorizontalAlignment(JTextField.RIGHT);
		txtPoligonoUrbano.setText("");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 3.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = insets;
		pnlUrbano.add(txtPoligonoUrbano, gridBagConstraints);
		
		geopistaEditor.addGeopistaListener(new GeopistaListener(){

			public void selectionChanged(AbstractSelection abtractSelection) {
				
				//Capturamos la parcela y la manzana del gml
				try {
					ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems(geopistaEditor.getLayerManager().getLayer("parcelas"));
					Iterator featuresCollectionIter = featuresCollection.iterator();
					if(!featuresCollectionIter.hasNext()) return;
					Feature actualFeature = (Feature) featuresCollectionIter.next();

					//Tabla ambitos_gestion:					
					geopistaEditor.zoomTo(actualFeature);
					getWizardContext().inputChanged();

					//Recuperar el valor del tipo y colocarlo en su parcela corresondiente
					referenciaCatastral = actualFeature.getString(2) ; //La Referencia Catastral					
					String codigoParcela = actualFeature.getString(5);
					String codigoPoligono = actualFeature.getString(6);
					String idVia = actualFeature.getString(7);
					
					if (!idVia.equals("") &&
							(!codigoPoligono.equals("") || !codigoParcela.equals(""))){
						txtPoligonoUrbano.setText(referenciaCatastral.substring(0,5));
						txtParcelaUrbano.setText(referenciaCatastral.substring(5,7) );
						txtParcelaRustico.setText("");
						txtPoligonoRustico.setText("");
					}
					else if (!codigoPoligono.equals("") || !codigoParcela.equals("")) {						
						txtPoligonoUrbano.setText("");
						txtParcelaUrbano.setText("");
						txtParcelaRustico.setText(codigoParcela);
						txtPoligonoRustico.setText(codigoPoligono);
					}
					else if (!idVia.equals("")){
						txtPoligonoUrbano.setText(referenciaCatastral.substring(0,5));
						txtParcelaUrbano.setText(referenciaCatastral.substring(5,7) );
						txtParcelaRustico.setText("");
						txtPoligonoRustico.setText("");
					}
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

			public void featureActioned(AbstractSelection abtractSelection){
				// No hacer nada
			}
		});
	}
	
	protected void enteredPanelFromLeft(Map dataMap) {
		try {
			if (!mapLoaded){
				geopistaEditor.loadMap(appContext.getString("url.mapa.catastro"));
				GeopistaLayer layerParcelas = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("parcelas");
				layerParcelas.setActiva(true);
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
		mapParameterEditor.fillParameter(referenciaCatastral);
	}

	public boolean isInputValid() {
		Collection lista = null;
		lista = geopistaEditor.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(geopistaEditor.getLayerManager().getLayer("parcelas"));
		if (lista.size()==1){
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
