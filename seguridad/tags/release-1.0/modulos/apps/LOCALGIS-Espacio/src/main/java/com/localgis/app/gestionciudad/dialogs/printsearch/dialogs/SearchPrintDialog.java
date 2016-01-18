/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.printsearch.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.app.gestionciudad.utilidades.EdicionUtils;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.ui.components.DateField;
import com.localgis.app.gestionciudad.dialogs.printsearch.utils.ConfigurationToggleButton;
import com.localgis.app.gestionciudad.dialogs.printsearch.utils.PrintSearhUtils;
import com.localgis.app.gestionciudad.utils.GestionCiudadOperaciones;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.FinderInterventionConditions;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.FinderNoteConditions;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class SearchPrintDialog extends JDialog implements ItemListener{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6433840953930364742L;
	
	private String notesName = "Notas";
	private String intervencionesName = "Actuaciones";
	private String allName = "Todas";
	
	private JComboBox swchitNotesInterventions = null;
	private JTextField descriptionTextField = null;
	private DateField fechaFromAltaDateField = null;
	private DateField fechaToAltaDateField = null;
	
	private ComboBoxEstructuras tiposDeActuacion = null;
	private ComboBoxEstructuras tiposDeIntervencion = null;
	private JTextField causaTextField = null;
	private DateField fechaFromProximaActuacionDateField = null;
	private DateField fechaToProximaActuacionDateField = null;
	private JTextField presupuestoFromTextField = null;
	private JTextField presupuestoToTextField = null;
	private JTextField porcentajeFromTextField = null;
	private JTextField porcentajeToTextField = null;
	
	
	private JPanel rootPanel = null;
	private OKCancelPanel okCancelPanel = null;
	 
	private JPanel panelCamposActuacion = null;
//	private JPanel panelTiposActuacion = null;
	private JPanel panelFechaProximaActuacion = null;
	private JPanel panelPresupuesto = null;
	private JPanel panelPorcentaje = null;
	private JPanel panelFechaAlta = null;
	
	private FinderNoteConditions noteConditions= null;
	private FinderInterventionConditions interventionConditions = null;
	
	private ConfigurationToggleButton mostrarPanelBusquedaAvanzadaButton = null;
	
	public static void main(String[] args){
		System.err.println("Prueba de Search Print Dialog");
//		AppContext.getApplicationContext().login();
		
		SearchPrintDialog dialog = new SearchPrintDialog(AppContext.getApplicationContext().getMainFrame());
		if (dialog.wasOKPressed()){
			
		}
//		if (dialog.wasOKPressed()){
//			ArrayList<LayerFeatureBean> layersAndFeatures = new ArrayList<LayerFeatureBean>();
////			Iterator it = editorGeopista.getSelectionManager().getFeatureSelection().getFeaturesWithSelectedItems().iterator();
//
////			while (it.hasNext()){
////				GeopistaFeature feature = (GeopistaFeature) it.next();
////				layersAndFeatures.add(new LayerFeatureBean(feature.getLayer().getId_LayerDataBase(),feature.getID()));
////			}
//
//
//			LocalGISIntervention nuevoAviso = dialog.getAviso();
//			nuevoAviso.setFeatureRelation(layersAndFeatures.toArray(new LayerFeatureBean[layersAndFeatures.size()]));
//			//				((AvisoSimpleTableModel)((TableSorted)(getJTableListaAvisos()).getModel()).getTableModel()).anniadirAviso(nuevoAviso);
//
//
//		}
		
	}
	

	
	public SearchPrintDialog(Frame parentComponent){
		super(parentComponent, "", true);
		PrintSearhUtils.inicializarIdiomaPrintSearchPanels();
		this.setTitle(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.title"));
		this.setSize(500, 500);
		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
		this.initialize();
		packDialog();
		this.setVisible(true);
		
	}
	
	private void packDialog(){
		this.setMinimumSize(new Dimension(420,1));
		this.pack();
	}
	
	private void initialize(){
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		
		this.setLayout(new GridBagLayout());
		
		
		this.add(this.getSearchPrintPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 10));


		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	public JPanel getSearchPrintPanel(){
		if (rootPanel == null){
			
		rootPanel = new JPanel(new GridBagLayout());
		
//		rootPanel.setBorder(BorderFactory.createTitledBorder
//				(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.bordertitle"), 
//						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
		
		rootPanel.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.findbytype")), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		rootPanel.add(this.getSwchitNotesInterventions(), 
				new GridBagConstraints(	1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		rootPanel.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.description")), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		rootPanel.add(getDescriptionTextField(), 
				new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 250, 0));
		
		
		rootPanel.add(getPanelFechaAlta(), 
				new GridBagConstraints(0, 2, 2, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));	
				
		rootPanel.add(getMostrarPanelBusquedaAvanzadaButton(), 
				new GridBagConstraints(0, 3, 2, 1, 0.1, 0.1, GridBagConstraints.EAST, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		rootPanel.add(getPanelCamposActuacion(), 
				new GridBagConstraints(0, 4, 2, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 0, 0, 0), 0, 0));
		
		this.getPanelCamposActuacion().setVisible(false);
		this.getSwchitNotesInterventions().setSelectedItem(allName);
		
		}
		return rootPanel;
		
	}
	
	
	private JPanel getPanelCamposActuacion(){
		if (panelCamposActuacion == null){
			panelCamposActuacion = new JPanel(new GridBagLayout());
			panelCamposActuacion.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.fields"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			
			panelCamposActuacion.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.typeact")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelCamposActuacion.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.typeint")), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelCamposActuacion.add(this.getActuationTypesComboBoxEstructuras(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelCamposActuacion.add(this.getInterventionTypesComboBoxEstructuras(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelCamposActuacion.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.cause")), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelCamposActuacion.add(this.getCausaTextField(), 
					new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelCamposActuacion.add(this.getPanelFechaProximaActuacion(), 
					new GridBagConstraints(0, 3, 2, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelCamposActuacion.add(this.getPanelPresupuesto(), 
					new GridBagConstraints(0, 4, 2, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelCamposActuacion.add(this.getPanelPorcentaje(), 
					new GridBagConstraints(0, 5, 2, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			
		}
		return panelCamposActuacion;
	}
	
	
//	private JPanel getPanelTiposActuacion(){
//		if (panelTiposActuacion == null){
//			panelTiposActuacion = new JPanel(new GridBagLayout());
//			panelTiposActuacion.setBorder(BorderFactory.createTitledBorder
//					(null," Tipo de Actuación e Intervención:   ", 
//							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
//			
//		}
//		return panelTiposActuacion;
//	}
	
	
	private JPanel getPanelFechaProximaActuacion(){
		if (panelFechaProximaActuacion == null){
			panelFechaProximaActuacion = new JPanel(new GridBagLayout());
			panelFechaProximaActuacion.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.nextdate.title"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			
			panelFechaProximaActuacion.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.nextdate.from")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelFechaProximaActuacion.add(getFechaFromProximaActuacionDateField(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelFechaProximaActuacion.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.nexdate.to")), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelFechaProximaActuacion.add(getFechaToProximaActuacionDateField(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
		}
		return panelFechaProximaActuacion;
	}
	
	
	private JPanel getPanelPresupuesto(){
		if (panelPresupuesto == null){
			panelPresupuesto = new JPanel(new GridBagLayout());
			panelPresupuesto.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.budget.title"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			
			panelPresupuesto.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.budget.from")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelPresupuesto.add(getPresupuestoFromTextField(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelPresupuesto.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.budget.to")), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelPresupuesto.add(getPresupuestoToTextField(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelPresupuesto.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.budget.currency")), 
					new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
		
			
		}
		return panelPresupuesto;
	}
	
	
	
	private JPanel getPanelPorcentaje(){
		if (panelPorcentaje == null){
			panelPorcentaje = new JPanel(new GridBagLayout());
			panelPorcentaje.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.percentage.title"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			
			panelPorcentaje.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.percentage.from")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelPorcentaje.add(getPorcentajeFromTextField(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelPorcentaje.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.percentage.to")), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelPorcentaje.add(getPorcentajeToTextField(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelPorcentaje.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.percentage.symbol")), 
					new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
		}
		return panelPorcentaje;
	}
	
	
	private JPanel getPanelFechaAlta(){
		if (panelFechaAlta == null){
			panelFechaAlta = new JPanel(new GridBagLayout());
			panelFechaAlta.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.initdate.title"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			
			panelFechaAlta.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.initdate.from")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelFechaAlta.add(getFechaAltaFromDateField(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelFechaAlta.add(new JLabel(I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.label.actuacion.initdate.to")), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelFechaAlta.add(getFechaAltaToDateField(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
		}
		return panelFechaAlta;
	}

	
	private JComboBox getSwchitNotesInterventions(){
		if (swchitNotesInterventions == null){	
			
			try{
				allName = I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.itemtype.combobox.fields.all");
			}catch (Exception e) { e.printStackTrace();	}
			
			try{
				notesName = I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.itemtype.combobox.fields.notes");
			}catch (Exception e) { e.printStackTrace();	}
			
			try{
				intervencionesName = I18N.get("printinterventionspanel","localgisgestionciudad.interfaces.printsearch.options.dialog.itemtype.combobox.fields.inteventions");
			}catch (Exception e) { e.printStackTrace();	}
			
			swchitNotesInterventions = new JComboBox();
			swchitNotesInterventions.addItem(allName);
			swchitNotesInterventions.addItem(notesName);
			swchitNotesInterventions.addItem(intervencionesName);
			
			swchitNotesInterventions.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (swchitNotesInterventions != null){
						if (swchitNotesInterventions.getSelectedItem() != null){
							onNotesInterventionsSelected(swchitNotesInterventions.getSelectedItem());
						}
					}
				}
			});		
			}
		return swchitNotesInterventions;
	}
	
	
	private JTextField getDescriptionTextField(){
		if (descriptionTextField == null){
			descriptionTextField = new JTextField();
		}
		return descriptionTextField;
	}
	
	
	private DateField getFechaAltaFromDateField(){
		if (fechaFromAltaDateField == null){
			fechaFromAltaDateField = new DateField(new GregorianCalendar(), 0);
			fechaFromAltaDateField.setDate(null);
		}
		return fechaFromAltaDateField;
	}
	private DateField getFechaAltaToDateField(){
		if (fechaToAltaDateField == null){
			fechaToAltaDateField = new DateField(new GregorianCalendar(),0);
			fechaToAltaDateField.setDate(null);
		}
		return fechaToAltaDateField;
	}
	
	
	
	private ComboBoxEstructuras getInterventionTypesComboBoxEstructuras(){
		if (tiposDeIntervencion == null){

			ListaEstructuras lista = null;
			GestionCiudadOperaciones op2 = new GestionCiudadOperaciones();
			try {
				lista = op2.obtenerTiposDeIntervenciones("");
			} catch (DataException e2) {
				e2.printStackTrace();
				lista = new ListaEstructuras();
			}

			tiposDeIntervencion = new ComboBoxEstructuras(
					lista,
					new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {}
					},
					AppContext.getApplicationContext().getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"),
					true);
			
			
		}
		return tiposDeIntervencion;
	}
	private ComboBoxEstructuras getActuationTypesComboBoxEstructuras(){
		if(tiposDeActuacion == null){
			
			ListaEstructuras lista = null;
			GestionCiudadOperaciones op2 = new GestionCiudadOperaciones();
			try {
				lista = op2.obtenerTiposDeActuaciones();
			} catch (DataException e2) {
				e2.printStackTrace();
				lista = new ListaEstructuras();
			}
			
			tiposDeActuacion = new ComboBoxEstructuras(
					lista,
					new ActionListener(){ 
						@Override 
						public void actionPerformed(ActionEvent e){
							if (tiposDeActuacion != null){
								if (tiposDeActuacion.getSelectedItem() != null){
									loadActuationTypes(tiposDeActuacion.getSelectedItem());
								}
							}
						}	
						},
					AppContext.getApplicationContext().getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"),
					true);	
		}
		return tiposDeActuacion;
	}

	private void loadActuationTypes(Object selectedItem) {
		if (selectedItem!=null){ 
			if (selectedItem.toString() != null ){

				ListaEstructuras lista = null;
				GestionCiudadOperaciones op2 = new GestionCiudadOperaciones();
				try {
					lista = op2.obtenerTiposDeIntervenciones(((DomainNode)selectedItem).getPatron());
				} catch (DataException e2) {
					e2.printStackTrace();
					lista = new ListaEstructuras();
				}
				
				this.cargarLista(this.getInterventionTypesComboBoxEstructuras(),
						lista);
			} else{
				this.cargarLista(this.getInterventionTypesComboBoxEstructuras(), new ListaEstructuras());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarLista (JComboBox jComboBox, ListaEstructuras lst)
	{
		jComboBox.removeAllItems();
		jComboBox.addItem(new DomainNode());
		
		Iterator it = lst.getLista().values().iterator();
		while (it.hasNext())
			jComboBox.addItem(it.next());
	}
	
	
	
	private JTextField getCausaTextField(){
		if (causaTextField == null){
			causaTextField = new JTextField();
		}
		return causaTextField;
	}
	
	private DateField getFechaFromProximaActuacionDateField(){
		if (fechaFromProximaActuacionDateField == null){
			fechaFromProximaActuacionDateField = new DateField(new GregorianCalendar(),0);
			fechaFromProximaActuacionDateField.setDate(null);
		}
		return fechaFromProximaActuacionDateField;
	}
	
	private DateField getFechaToProximaActuacionDateField(){
		if (fechaToProximaActuacionDateField == null){
			fechaToProximaActuacionDateField = new DateField(new GregorianCalendar(),0);
			fechaToProximaActuacionDateField.setDate(null);
		}
		return fechaToProximaActuacionDateField;
	}
	
	private JTextField getPresupuestoFromTextField(){
		if(presupuestoFromTextField == null){
			presupuestoFromTextField = new JTextField(10);
			presupuestoFromTextField.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYDecimalCampoEdit(getPresupuestoFromTextField(), 
    						10, 
    						AppContext.getApplicationContext().getMainFrame());
    			}
    		});
		}
		return presupuestoFromTextField;
	}
	
	private JTextField getPresupuestoToTextField(){
		if (presupuestoToTextField == null){
			presupuestoToTextField = new JTextField(10);
			presupuestoToTextField.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYDecimalCampoEdit(getPresupuestoToTextField(), 
    						10, 
    						AppContext.getApplicationContext().getMainFrame());
    			}
    		});
		}
		return presupuestoToTextField;
	}
	
	private JTextField getPorcentajeFromTextField(){
		if (porcentajeFromTextField == null){
			porcentajeFromTextField = new JTextField(5);
			porcentajeFromTextField.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYDecimalCampoEdit(getPorcentajeFromTextField(), 
    						5, 
    						AppContext.getApplicationContext().getMainFrame());
    			}
    		});
			
			porcentajeFromTextField.addFocusListener(new FocusListener(){

				@Override	public void focusGained(FocusEvent e) {	}

				@Override
				public void focusLost(FocusEvent e) {
					int valor = 0;
        			try{
        				valor = Integer.parseInt(getPorcentajeFromTextField().getText());
        			}catch(NumberFormatException ex){
        			}
        			if (valor > 100){
        				getPorcentajeFromTextField().setText("100");
        			}					
				}
				
			});
		}
		return porcentajeFromTextField;
	}
	
	private JTextField getPorcentajeToTextField(){
		if(porcentajeToTextField == null){
			porcentajeToTextField = new JTextField(5);
			porcentajeToTextField.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYDecimalCampoEdit(getPorcentajeToTextField(), 
    						5, 
    						AppContext.getApplicationContext().getMainFrame());
    			}
    		});
			porcentajeToTextField.addFocusListener(new FocusListener(){
				@Override public void focusGained(FocusEvent e) {}

				@Override
				public void focusLost(FocusEvent e) {
    				int valor = 0;
        			try{
        				valor = Integer.parseInt(getPorcentajeToTextField().getText());
        			}catch(NumberFormatException ex){
        			}
        			if (valor > 100){
        				getPorcentajeToTextField().setText("100");
        			}
				}
			});
		}
		return porcentajeToTextField;
	}
	
	private ConfigurationToggleButton getMostrarPanelBusquedaAvanzadaButton(){
		if (mostrarPanelBusquedaAvanzadaButton == null){
			mostrarPanelBusquedaAvanzadaButton = new ConfigurationToggleButton("Búsqueda Avanzada");
			
			mostrarPanelBusquedaAvanzadaButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onMostrarPanelBusquedaAvanzadaDo();
				}
			});
		}
		
		return mostrarPanelBusquedaAvanzadaButton;
	}
	
	
	private void onMostrarPanelBusquedaAvanzadaDo() {
			if (this.getPanelCamposActuacion().isVisible()){
				this.getPanelCamposActuacion().setVisible(false);
			}else{
				this.getPanelCamposActuacion().setVisible(true);
			}
			packDialog();
	}
	
	private void onNotesInterventionsSelected(Object selectedItem) {
		if (selectedItem !=null){
			if(selectedItem instanceof String){
				if (selectedItem.equals(intervencionesName)){
					this.enablePanelComponents(this.getPanelCamposActuacion(), true);
				} else{
					this.enablePanelComponents(this.getPanelCamposActuacion(), false);
				}
				this.packDialog();
			}
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void enablePanelComponents(JPanel panel,boolean flag){
		Component[] listaComponentes = panel.getComponents();
		if (listaComponentes!=null && listaComponentes.length>0){
			for(int i=0; i < listaComponentes.length; i++){
				if (listaComponentes[i] instanceof JPanel){
					enablePanelComponents((JPanel) listaComponentes[i], flag);
				} else {
					listaComponentes[i].setEnabled(flag);
				}
			}
		}
	}
	
	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();
			this.okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}
	
	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			setVisible(false);
			return;
		}
	}

	protected boolean isInputValid() {
		if (!allDataIscorrect()){
			return false;
		}
		return setDataToSearchConfiguration(); 
	}


	private boolean setDataToSearchConfiguration() {
		
		this.noteConditions = null;
		this.interventionConditions = null;
		
		
		Object selectedItem = this.getSwchitNotesInterventions().getSelectedItem();
		if (selectedItem != null){
			if (selectedItem.equals(allName)){
				this.noteConditions = new FinderNoteConditions();
				this.interventionConditions = new FinderInterventionConditions();		
			} else if (selectedItem.equals(intervencionesName)){
				this.noteConditions = null;
				this.interventionConditions = new FinderInterventionConditions();
			} else if (selectedItem.equals(notesName)){
				this.noteConditions = new FinderNoteConditions();
				this.interventionConditions = null;
			}
		}
		
		
		if (noteConditions!=null){
			String description = this.getDescriptionTextField().getText();
			if (description!=null && !description.equals("")){
				noteConditions.setDescription(this.getDescriptionTextField().getText());	
			}
			
			noteConditions.setTo(this.getFechaAltaToDateField().getCalendar());
			
			noteConditions.setFrom(this.getFechaAltaFromDateField().getCalendar());	
			
			noteConditions.setOrderByColumns("");
			noteConditions.setFeatures("");
		}
		
		if (interventionConditions!=null){
			String description = this.getDescriptionTextField().getText();
			if (description!=null && !description.equals("")){
				interventionConditions.setDescription(this.getDescriptionTextField().getText());	
			}
			
			interventionConditions.setToStart(this.getFechaAltaToDateField().getCalendar());
			interventionConditions.setFromStart(this.getFechaAltaFromDateField().getCalendar());
			
			interventionConditions.setActuationType(this.getActuationTypesComboBoxEstructuras().getSelectedPatron());
			interventionConditions.setInterventionType(this.getInterventionTypesComboBoxEstructuras().getSelectedPatron());
			interventionConditions.setCauses(this.getCausaTextField().getText());
			
			interventionConditions.setFromNext(this.getFechaFromProximaActuacionDateField().getCalendar());
			interventionConditions.setToNext(this.getFechaToProximaActuacionDateField().getCalendar());
			
			try{
				interventionConditions.setForeseenBudgetFrom(Double.parseDouble(this.getPresupuestoFromTextField().getText()));
			}catch (NumberFormatException e) {
			}
			try{
				interventionConditions.setForeseenBudgetTo(Double.parseDouble(this.getPresupuestoToTextField().getText()));
			} catch (NumberFormatException e) {
			}
			
			
			try{
				interventionConditions.setWorkPercentageFrom(Double.parseDouble(this.getPorcentajeFromTextField().getText()));
			 }catch (NumberFormatException e) {
			}
			 try{
				 interventionConditions.setWorkPercentageTo(Double.parseDouble(this.getPorcentajeToTextField().getText()));
			 } catch (NumberFormatException  e) {
			}
			 
			 interventionConditions.setOrderColumns("");
			 interventionConditions.setFeatures("");
			
		}
		
		
		return true;
	}


	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

	private boolean allDataIscorrect() {
		// TODO Auto-generated method stub
		return true;
	}

	
	public FinderNoteConditions getFinderNoteConditions(){
		return this.noteConditions;
	}
	public FinderInterventionConditions getFrinderInterventionConditions(){
		return this.interventionConditions;
	}
}
