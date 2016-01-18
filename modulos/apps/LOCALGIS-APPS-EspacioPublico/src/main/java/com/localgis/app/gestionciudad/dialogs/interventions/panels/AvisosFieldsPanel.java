/**
 * AvisosFieldsPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.panels;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.naming.ConfigurationException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.gestionciudad.utilidades.EdicionUtils;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.ui.components.DateField;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.dialogs.interventions.dialogs.PeriodicidadAvisoDialog;
import com.localgis.app.gestionciudad.dialogs.interventions.images.IconLoader;
import com.localgis.app.gestionciudad.dialogs.interventions.renderers.AddToRoutesComboBoxRenderer;
import com.localgis.app.gestionciudad.dialogs.interventions.renderers.PriorityComboBoxRenderer;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.utils.GestionCiudadOperaciones;
import com.localgis.app.gestionciudad.utils.LocalGISWarning;
import com.toedter.calendar.JTextFieldDateEditor;
import com.vividsolutions.jump.I18N;


/**
 * @author javieraragon
 *
 */
public class AvisosFieldsPanel extends JPanel {

	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4617640604196019381L;

	private NotesInterventionsEditionTypes tipoEdicion = null;

	private ComboBoxEstructuras interventionTypesComboBoxEstructuras = null;
	private ComboBoxEstructuras actuationTypesComboBoxEstructuras = null;

	private JTextField descripcionTextField = null;
	private JTextField causaTextField = null;

	private DateField fechaAltaDateField = null;
	private DateField fechaProximoAvisodateField = null;
	private DateField fechaFinalizacionDateField = null;
	private JCheckBox fechaFinalizacionCheckBox = null;

	private JTextField presupuestoTextField = null;
	private JFormattedTextField porcentajeTextField = null;
	private JProgressBar porcentajeProgressBar = null;

	private JButton periodicidadButton = null;

	private JPanel panelFechas = null;
	private JPanel typesPanel = null;
	private JPanel numberFieldsPanel = null;

	private ButtonGroup recurrenceButtonGroup = new ButtonGroup();
	private JRadioButton fechaConcretaRadioButton = null;
	private JRadioButton fechaPeriodicaRadioButton = null;
	
	
	private JComboBox priorityComboBox = null;
	private JComboBox addRouteComboBox = null;

	private LocalGISIntervention aviso = null;
	private String pattern = null;
	
	private JCheckBox addIncidentToRoutesCheckBox = null;


	public AvisosFieldsPanel(NotesInterventionsEditionTypes edicion, LocalGISIntervention aviso){
		super(new GridBagLayout());
		this.tipoEdicion = edicion;
		this.aviso = aviso;

		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();

		this.initialize();

		loadAviso(aviso);
	}

	private void loadAviso(LocalGISIntervention aviso){
		if (aviso != null){
			if (aviso.getDescription() != null){
				this.getDescripcionTextField().setText(aviso.getDescription());
			}

			if (aviso.getCauses() != null){
				this.getCausaTextField().setText(aviso.getCauses());
			}

			if (aviso.getActuationType() != null){
				try{
				this.getActuationTypesComboBoxEstructuras().setSelectedPatron(aviso.getActuationType());
				}catch (Exception e) {
					e.printStackTrace();
					this.getActuationTypesComboBoxEstructuras().setSelectedIndex(0);
				}
			} else{
				this.getActuationTypesComboBoxEstructuras().setSelectedIndex(0);
			}
			
			if (aviso.getInterventionType() != null){
				try{
					this.getInterventionTypesComboBoxEstructuras().setSelectedPatron(aviso.getInterventionType());
				}catch (Exception e) {
					e.printStackTrace();
					this.getInterventionTypesComboBoxEstructuras().setSelectedIndex(0);
				}
			} else{
				this.getInterventionTypesComboBoxEstructuras().setSelectedIndex(0);
			}

			if (aviso.getStartWarning() != null){
				this.getFechaAltaDateField().setDate(aviso.getStartWarning().getTime());
			}

			if (aviso.getNextWarning() != null){
				this.getFechaProximoAvisoDateField().setDate(aviso.getNextWarning().getTime());
			}

			if (aviso.getPattern()!= null && !aviso.getPattern().equals("")){
				this.recurrencedateController(true);
			} else{
				this.recurrencedateController(false);
			}
			
			if (aviso.getWorkPercentage() != null && aviso.getWorkPercentage() > 0){
				int percentage = (int) (aviso.getWorkPercentage()*100);
				this.getPorcentajeTextField().setText(Integer.toString(percentage));
				onPercentageChangeDo();
			} else{
				this.getPorcentajeTextField().setText("0");
			}
			
			if (aviso.getForeseenBudget() != null && aviso.getForeseenBudget() > 0){
				this.getPresupuestoTextField().setText(Double.toString(aviso.getForeseenBudget()));
			} else{
				this.getPresupuestoTextField().setText("0");
			}
			
			if (aviso.getPriority() != null && aviso.getPriority() > 0){
				this.getPriorityComboBox().setSelectedItem(aviso.getPriority());
			} else{
				this.getPriorityComboBox().setSelectedIndex(0);
			}
			
			if (aviso.getExecutionDate()!=null){
				this.getFechaFinalizacionCheckBox().setSelected(true);
				this.getFechaFinalizacionDateField().setDate(aviso.getExecutionDate().getTime());
				this.setEnabledFechaFinalizacionDateField(true);
			}
			
			if (aviso.getAddToRoutes()!=null && aviso.getAddToRoutes()>=0 && 
					aviso.getAddToRoutes()<=2){
				this.getAddIncidentToRoutesCheckBox().setSelected(true);
				this.getAddRouteBox().setSelectedItem(aviso.getAddToRoutes());
				this.getAddRouteBox().setEnabled(true);
			} else{
				this.getAddIncidentToRoutesCheckBox().setSelected(false);
			}
			
		}
	}

	private void initialize(){

		this.setBorder(BorderFactory.createTitledBorder
				(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.bordertitle"), 
						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

		this.add(getTypesPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(5, 5, 5, 5), 0, 0));

		this.add(getTextFieldsPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(getPanelFechas(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(getNumberFieldsPanel(), 
				new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(getPriorityPanel(), 
				new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(getAddToRoutesPanel(), 
				new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		
	}

	private JPanel getNumberFieldsPanel() {
		numberFieldsPanel = new JPanel(new GridBagLayout());
		numberFieldsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JPanel panel1 = new JPanel(new GridBagLayout());	
		panel1.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.presupuesto")), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 5));
		panel1.add(getPresupuestoTextField(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		numberFieldsPanel.add(panel1, 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));


		JPanel panel2 = new JPanel(new GridBagLayout());
		panel2.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.porcentaje")), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.VERTICAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel2.add(getPorcentajeTextField(), 
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0 , 0));
		panel2.add(getPorcentajeProgressBar(), 
				new GridBagConstraints(0, 1, 2, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		numberFieldsPanel.add(panel2, 
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		return numberFieldsPanel;
	}
	
	private JPanel getAddToRoutesPanel(){
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		panel.add(this.getAddIncidentToRoutesCheckBox(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.BOTH, 
						new Insets(5, 10, 5, 0), 0 , 0));
		
		panel.add(this.getAddRouteBox(), 
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(5, 10, 5, 0), 20 , 0));
		
		panel.add(new JLabel(""), 
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.VERTICAL, 
						new Insets(5, 10, 5, 0), 200 , 0));
		
		return panel;
	}
	
	private JPanel getPriorityPanel(){
		
		JPanel panel3 = new JPanel(new GridBagLayout());
		
		panel3.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.priority")), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.BOTH, 
						new Insets(0, 10, 0, 0), 0 , 0));
		panel3.add(this.getPriorityComboBox(), 
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.VERTICAL, 
						new Insets(0, 0, 0, 5),  20, 0));
		panel3.add(new JLabel(), 
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(0, 0, 0, 5), 180, 0));

		return panel3;
	}

	private JPanel getTextFieldsPanel(){

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		panel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.descripcion")), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		panel.add(this.getDescripcionTextField(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		panel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.causa")), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel.add(this.getCausaTextField(), 
				new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

//		panel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.actuacion")), 
//				new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//						GridBagConstraints.HORIZONTAL, 
//						new Insets(0, 5, 0, 5), 0, 0));

//		panel.add(this.getActuacionTextField(), 
//				new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//						GridBagConstraints.HORIZONTAL, 
//						new Insets(0, 5, 0, 5), 0, 0));

		return panel;
	}

	private JPanel getTypesPanel(){
		if (typesPanel == null){
			typesPanel = new JPanel(new GridBagLayout());
			typesPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

			typesPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.actuationtype")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.SOUTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			typesPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.interventiontype")), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.SOUTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			typesPanel.add(getActuationTypesComboBoxEstructuras(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.SOUTH, 
							GridBagConstraints.BOTH, 
							new Insets(0, 5, 0, 5), 0, 0));

			typesPanel.add(getInterventionTypesComboBoxEstructuras(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.SOUTH, 
							GridBagConstraints.BOTH, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return typesPanel;
	}


	public ComboBoxEstructuras getInterventionTypesComboBoxEstructuras(){
		if (interventionTypesComboBoxEstructuras == null){

			ListaEstructuras lista = null;
			GestionCiudadOperaciones op2 = new GestionCiudadOperaciones();
			try {
				lista = op2.obtenerTiposDeIntervenciones("");
			} catch (DataException e2) {
				e2.printStackTrace();
				lista = new ListaEstructuras();
			}

			interventionTypesComboBoxEstructuras = new ComboBoxEstructuras(
					lista,
					new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {}
					},
					AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"),
					true);
			
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				interventionTypesComboBoxEstructuras.setEnabled(false);
			}
		}
		return interventionTypesComboBoxEstructuras;
	}
	public ComboBoxEstructuras getActuationTypesComboBoxEstructuras(){
		if(actuationTypesComboBoxEstructuras == null){
			
			ListaEstructuras lista = null;
			GestionCiudadOperaciones op2 = new GestionCiudadOperaciones();
			try {
				lista = op2.obtenerTiposDeActuaciones();
			} catch (DataException e2) {
				e2.printStackTrace();
				lista = new ListaEstructuras();
			}
			
			actuationTypesComboBoxEstructuras = new ComboBoxEstructuras(
					lista,
					new ActionListener(){ 
						@Override 
						public void actionPerformed(ActionEvent e){
							if (actuationTypesComboBoxEstructuras != null){
								if (actuationTypesComboBoxEstructuras.getSelectedItem() != null){
									loadActuationTypes(actuationTypesComboBoxEstructuras.getSelectedItem());
								}
							}
						}	
						},
					AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"),
					true);	
			
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				actuationTypesComboBoxEstructuras.setEnabled(false);
			}
		}
		return actuationTypesComboBoxEstructuras;
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



	private JPanel getPanelFechas(){
		if (panelFechas == null){
			panelFechas = new JPanel(new GridBagLayout());
			panelFechas.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

			JPanel panelCamposFechas = new JPanel(new GridBagLayout());
			JPanel panelBotonRecurrencia = new JPanel(new GridBagLayout());

			JPanel fechaAltaPanel = new JPanel(new GridBagLayout());
			fechaAltaPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.fechaalta")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			fechaAltaPanel.add(this.getFechaAltaDateField(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			fechaAltaPanel.add(this.getFechaFinalizacionCheckBox(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			fechaAltaPanel.add(this.getFechaFinalizacionDateField(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(5, 5, 0, 5), 0, 0));
			panelCamposFechas.add(fechaAltaPanel, 
					new GridBagConstraints(0, 0, 1, 2, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelCamposFechas.add(new JSeparator(SwingConstants.VERTICAL), 
					new GridBagConstraints(1, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, 
							GridBagConstraints.BOTH, 
							new Insets(0, 0, 0, 0), 0, 0));

//			
//			JPanel panelFechaFinalizacion = new JPanel(new GridBagLayout()); 
//			panelFechaFinalizacion.add(this.getFechaFinalizacionCheckBox(), 
//					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//							GridBagConstraints.NONE, 
//							new Insets(0, 5, 0, 5), 0, 0));
//			panelFechaFinalizacion.add(this.getFechaFinalizacionDateField(), 
//					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//							GridBagConstraints.NONE, 
//							new Insets(0, 5, 0, 5), 0, 0));
//			panelCamposFechas.add(panelFechaFinalizacion, 
//					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, 
//							GridBagConstraints.VERTICAL, 
//							new Insets(5, 5, 0, 5), 0, 0));

			JPanel fechaProximoAvisoPanel = new JPanel(new GridBagLayout());
			fechaProximoAvisoPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.proximoaviso")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			fechaProximoAvisoPanel.add(this.getFechaProximoAvisoDateField(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelCamposFechas.add(fechaProximoAvisoPanel, 
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			


			JPanel recurrenceButtonsPanel = new JPanel(new GridBagLayout());
			recurrenceButtonsPanel.add(this.getFechaConcretaRadioButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			recurrenceButtonsPanel.add(this.getFechaPeriodicaRadioButton(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			panelCamposFechas.add(recurrenceButtonsPanel, 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			panelBotonRecurrencia.add(this.getPeriodicidadButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.LAST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
//			JPanel panelFechaFinalizacion = new JPanel(new GridBagLayout()); 
//			panelFechaFinalizacion.add(this.getFechaFinalizacionCheckBox(), 
//					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//							GridBagConstraints.NONE, 
//							new Insets(0, 5, 0, 5), 0, 0));
//			panelFechaFinalizacion.add(this.getFechaFinalizacionDateField(), 
//					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//							GridBagConstraints.NONE, 
//							new Insets(0, 5, 0, 5), 0, 0));

			panelFechas.add(panelCamposFechas, 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelFechas.add(panelBotonRecurrencia, 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.SOUTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
//			panelFechas.add(panelFechaFinalizacion, 
//					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, 
//							GridBagConstraints.VERTICAL, 
//							new Insets(5, 5, 0, 5), 0, 0));
			
			
			

			this.fechaConcretaRadioButton.setSelected(true);
		}
		return panelFechas;
	}

	private JTextField getDescripcionTextField(){
		if (descripcionTextField == null){
			descripcionTextField = new JTextField();

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				descripcionTextField.setEditable(false);
			}
		}
		return this.descripcionTextField;
	}


	private JTextField getCausaTextField(){
		if (causaTextField == null){
			causaTextField = new JTextField();

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				causaTextField.setEditable(false);
			}
		}
		return this.causaTextField;
	}


//	private JTextField getActuacionTextField(){
//		if (actuacionTextField == null){
//			actuacionTextField = new JTextField();
//
//			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
//				actuacionTextField.setEditable(false);
//			}
//		}
//		return this.actuacionTextField;
//	}

	private DateField getFechaAltaDateField(){
		if (fechaAltaDateField == null){
			fechaAltaDateField = new DateField(new Date(), 0);

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				((JTextFieldDateEditor)fechaAltaDateField.getDateEditor().getUiComponent()).setEditable(false);
				fechaAltaDateField.getCalendarButton().setEnabled(false);
			}
		}
		return fechaAltaDateField;
	}
	
	private DateField getFechaFinalizacionDateField(){
		if (fechaFinalizacionDateField == null){
			fechaFinalizacionDateField = new DateField(new Date(), 0);
			fechaFinalizacionDateField.setCalendar(null);
			this.setEnabledFechaFinalizacionDateField(false);
		}
		
		return fechaFinalizacionDateField;
	}
	
	private void setEnabledFechaFinalizacionDateField(boolean flag){
		((JTextFieldDateEditor)getFechaFinalizacionDateField().getDateEditor().getUiComponent()).setEditable(flag);
		getFechaFinalizacionDateField().getCalendarButton().setEnabled(flag);
		
		getPorcentajeTextField().setEnabled(!flag);
		if(flag){
			if (getFechaFinalizacionDateField().getCalendar() == null){
				getFechaFinalizacionDateField().setCalendar(new GregorianCalendar());
			}
			getPorcentajeTextField().setText("100");
			onPercentageChangeDo();
		} else{
			getFechaFinalizacionDateField().setCalendar(null);
		}
		
		getFechaFinalizacionCheckBox().setSelected(flag);
	}
	
	private JCheckBox getFechaFinalizacionCheckBox(){
		if (fechaFinalizacionCheckBox == null){
			fechaFinalizacionCheckBox = new JCheckBox("Fecha Finalización:");
//					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.fechafinaliza"));
			
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				fechaFinalizacionCheckBox.setEnabled(false);
			}
			
			fechaFinalizacionCheckBox.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					onFechaFinalizacionCheckBoxDo();
				}
			});
		}
		return fechaFinalizacionCheckBox;
	}
	
	private void onFechaFinalizacionCheckBoxDo() {
		if (this.getFechaFinalizacionCheckBox().isSelected()){
			if (confirmarFechaFinalizacion()){
				this.setEnabledFechaFinalizacionDateField(true);
			} else{
				this.setEnabledFechaFinalizacionDateField(false);
			}
		} else{
			this.setEnabledFechaFinalizacionDateField(false);
		}
	}

	private boolean confirmarFechaFinalizacion() {
		
//		String mensaje = I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.message.executiondate.confirmdialog");
		String mensaje ="<html>¿Desea realmente activar la <b>'Fecha de Finalización'</b> de esta intervención? <br>Si pulsa aceptar el porcentaje de ejecución de la obra se pondrá a <b>100%</b> y no podrá modificarlo <br>hasta que vuelva a desactivar la fecha de finalización.</html>";
		
		
		int seleccion = JOptionPane.showOptionDialog(
				this,
				mensaje, 
				"",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,    // null para icono por defecto.
				new Object[]{
						I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deleteconfirmdialog.accept"), 
						I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deleteconfirmdialog.cancel")
				},   
				// null para YES, NO y CANCEL
				I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deleteconfirmdialog.cancel"));
		
		if (seleccion == 0)
			return true;
		
		return false;
	}

	private DateField getFechaProximoAvisoDateField(){
		if (fechaProximoAvisodateField == null){
			fechaProximoAvisodateField = new DateField(new Date(), 0);

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				((JTextFieldDateEditor)fechaProximoAvisodateField.getDateEditor().getUiComponent()).setEditable(false);
				fechaProximoAvisodateField.getCalendarButton().setEnabled(false);
			}
		}
		return fechaProximoAvisodateField;
	}

	private JButton getPeriodicidadButton(){
		if (periodicidadButton == null){
			periodicidadButton = new JButton(IconLoader.icon(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencebuttonicon")));
			periodicidadButton.setToolTipText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencebuttontip"));
			periodicidadButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onPeriodicidadButtonDo();
				}
			});

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				if (this.aviso.getPattern()==null||(this.aviso.getPattern()!=null)&&(this.aviso.getPattern().equals(""))){
					periodicidadButton.setEnabled(false);
				}
			}
		}
		return periodicidadButton;
	}


	private void onPeriodicidadButtonDo() {
		if (aviso != null){
			LocalGISWarning recursiveWarning = null;
			if (aviso.getPattern() != null && !aviso.getPattern().equals("")){
				try {
					recursiveWarning = new LocalGISWarning(aviso.getPattern());
				} catch (ConfigurationException e) {
					e.printStackTrace();
				} catch (StringIndexOutOfBoundsException e){
					e.printStackTrace();
				}
			} 
			// Se muestra el dialogo para periodicidad
			PeriodicidadAvisoDialog dialog = new PeriodicidadAvisoDialog((Dialog) this.getTopLevelAncestor(),
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.tittle") + ": " + aviso.getId() + "-" + aviso.getDescription(),
					recursiveWarning, this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW));
			if (dialog.wasOKPressed()){
				// Si se pulsó aceptar, se guarda la periodicidad
				if (!this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
					this.aviso.setPattern(dialog.getRecursiveIncident().toString());
					this.aviso.setNextWarning(dialog.getRecursiveIncident().getNextCalendarWarning(new GregorianCalendar()));
					this.pattern = dialog.getRecursiveIncident().toString();
					this.getFechaProximoAvisoDateField().setDate(this.aviso.getNextWarning().getTime());
				}
			}
			// se libera el dialogo
			dialog.dispose();
		}
	}

	private JRadioButton getFechaConcretaRadioButton(){
		if (fechaConcretaRadioButton == null){
			fechaConcretaRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.fechaconcretalabel"));
			recurrenceButtonGroup.add(fechaConcretaRadioButton);
			fechaConcretaRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					onRecurrenceItemListenerDo(e);
				}
			});

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				fechaConcretaRadioButton.setEnabled(false);
			}
		}
		return fechaConcretaRadioButton;
	}

	private JRadioButton getFechaPeriodicaRadioButton(){
		if (fechaPeriodicaRadioButton == null){
			fechaPeriodicaRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencelabel"));
			recurrenceButtonGroup.add(fechaPeriodicaRadioButton);
			fechaPeriodicaRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					onRecurrenceItemListenerDo(e);
				}
			});

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				fechaPeriodicaRadioButton.setEnabled(false);
			}
		}
		return fechaPeriodicaRadioButton;
	}


	private void onRecurrenceItemListenerDo(ItemEvent event) {
		if (event != null && event.getItem()!=null){

			if(event.getItem().equals(fechaPeriodicaRadioButton) ||
					event.getItem().equals(fechaConcretaRadioButton) ){
				if (fechaPeriodicaRadioButton.isSelected()){
					recurrencedateController(true);
				} else if (fechaConcretaRadioButton.isSelected()){
					recurrencedateController(false);
				} else{
					recurrencedateController(false);
				}
			}
		}
	}

	private void recurrencedateController(boolean flag){
		if (!this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
			this.getFechaProximoAvisoDateField().setEditable(!flag);
			this.getFechaProximoAvisoDateField().setEnabled(!flag);

			this.getPeriodicidadButton().setEnabled(flag);
		}
	}

	public LocalGISIntervention getAvisoData(){
		LocalGISIntervention resultado = null;
		if (this.aviso != null){
			resultado = aviso;
		} else{
			resultado = new LocalGISIntervention();
		}

		String causa = this.getCausaTextField().getText();
		if (causa != null){
			resultado.setCauses(causa);
		}else{
			resultado.setCauses("");
		}

		DomainNode actuacion = (DomainNode) this.getActuationTypesComboBoxEstructuras().getSelectedItem();
		if (actuacion != null){
			resultado.setActuationType(actuacion.getPatron());
		}else{
			resultado.setActuationType("");
		}
		
		DomainNode intervencion = (DomainNode) this.getInterventionTypesComboBoxEstructuras().getSelectedItem();
		if (intervencion != null){
			resultado.setInterventionType(intervencion.getPatron());
		} else{
			resultado.setInterventionType("");
		}

		String descripcion = this.getDescripcionTextField().getText();
		if (descripcion != null){
			resultado.setDescription(descripcion);
		} else{
			resultado.setDescription("");	
		}

		if (this.getFechaAltaDateField().getDate()!=null && this.getFechaAltaDateField().getText()!=null 
				&& this.getFechaAltaDateField().getText() != ""){
			GregorianCalendar fehcaAlta = new GregorianCalendar();
			fehcaAlta.setTime(this.getFechaAltaDateField().getDate());
			resultado.setStartWarning(fehcaAlta);
		} else{
			resultado.setStartWarning(new GregorianCalendar());
		}


		if (this.getFechaProximoAvisoDateField().getDate()!=null && this.getFechaProximoAvisoDateField().getText()!=null 
				&& this.getFechaProximoAvisoDateField().getText() != ""){
			GregorianCalendar nextWariningDate = new GregorianCalendar();
			nextWariningDate.setTime(this.getFechaProximoAvisoDateField().getDate());
			resultado.setNextWarning(nextWariningDate);
		} else{
			resultado.setNextWarning(new GregorianCalendar());
		}

		if (this.getFechaPeriodicaRadioButton().isSelected()){
			resultado.setPattern(this.pattern);
		}		
		
		if (this.getPorcentajeProgressBar().getPercentComplete()>0 ){
			resultado.setWorkPercentage(this.getPorcentajeProgressBar().getPercentComplete());
		} else{
			resultado.setWorkPercentage(0.0);
		}
		
		if (this.getPresupuestoTextField().getText() != null && !this.getPresupuestoTextField().getText().equals("")){
			resultado.setForeseenBudget(Double.parseDouble(this.getPresupuestoTextField().getText()));
		} else{
			resultado.setForeseenBudget(0.0);
		}
		
		if (this.getPriorityComboBox() != null && this.getPriorityComboBox().getSelectedItem() != null &&
				this.getPriorityComboBox().getSelectedItem() instanceof Integer){
			resultado.setPriority((Integer) this.getPriorityComboBox().getSelectedItem());
		} else{
			// asginar prioridad minima: 10
			resultado.setPriority(10);
		}
		
		if (this.getFechaFinalizacionCheckBox()!=null && this.getFechaFinalizacionCheckBox().isSelected()){
			// Si la fecha de finalización está activa se pone el porcentaje de la obra a 100%
			if (this.getFechaFinalizacionDateField().getDate()!=null && this.getFechaFinalizacionDateField().getText()!=null 
					&& this.getFechaFinalizacionDateField().getText() != ""){
				GregorianCalendar executionDate = new GregorianCalendar();
				executionDate.setTime(this.getFechaFinalizacionDateField().getDate());
				resultado.setExecutionDate(executionDate);
			}else{
				resultado.setExecutionDate(new GregorianCalendar());
			}
		}
		
		if (this.getAddIncidentToRoutesCheckBox()!=null &&
				this.getAddIncidentToRoutesCheckBox().isSelected()){
			resultado.setAddToRoutes(1);
		} else{
			resultado.setAddToRoutes(0);
		}
		
		return resultado;
	}

	public LocalGISIntervention loadDataToAviso(LocalGISIntervention aviso){
		if (aviso == null){
			aviso = new LocalGISIntervention();
		}

		String causa = this.getCausaTextField().getText();
		if (causa != null){
			aviso.setCauses(causa);
		}else{
			aviso.setCauses("");
		}

//		String actuacion = this.getActuacionTextField().getText();
//		if (actuacion != null){
//			aviso.setActuationType(actuacion);
//		}else{
//			aviso.setActuationType("");
//		}

		String descripcion = this.getDescripcionTextField().getText();
		if (descripcion != null){
			aviso.setDescription(descripcion);
		} else{
			aviso.setDescription("");	
		}

		if (this.getFechaAltaDateField().getDate()!=null && this.getFechaAltaDateField().getText()!=null 
				&& this.getFechaAltaDateField().getText() != ""){
			GregorianCalendar fehcaAlta = new GregorianCalendar();
			fehcaAlta.setTime(this.getFechaAltaDateField().getDate());
			aviso.setStartWarning(fehcaAlta);
		} else{
			aviso.setStartWarning(new GregorianCalendar());
		}

		if (this.getFechaConcretaRadioButton().isSelected()){
			if (this.getFechaProximoAvisoDateField().getDate()!=null && this.getFechaProximoAvisoDateField().getText()!=null 
					&& this.getFechaProximoAvisoDateField().getText() != ""){
				GregorianCalendar nextWarningDate = new GregorianCalendar();
				nextWarningDate.setTime(this.getFechaProximoAvisoDateField().getDate());
				aviso.setNextWarning(nextWarningDate);
			} else{
				aviso.setNextWarning(new GregorianCalendar());
			}
		}
		
		if (this.getPriorityComboBox() != null && this.getPriorityComboBox().getSelectedItem() != null &&
				this.getPriorityComboBox().getSelectedItem() instanceof Integer){
			aviso.setPriority((Integer) this.getPriorityComboBox().getSelectedItem());
		} else{
			// asginar prioridad minima: 10
			aviso.setPriority(10);
		}
		
		if (this.getFechaFinalizacionCheckBox()!=null && this.getFechaFinalizacionCheckBox().isSelected()){
			// Si la fecha de finalización está activa se pone el porcentaje de la obra a 100%
			aviso.setForeseenBudget(new Double(100));
			if (this.getFechaFinalizacionDateField().getDate()!=null && this.getFechaFinalizacionDateField().getText()!=null 
					&& this.getFechaFinalizacionDateField().getText() != ""){
				GregorianCalendar executionDate = new GregorianCalendar();
				executionDate.setTime(this.getFechaFinalizacionDateField().getDate());
				aviso.setExecutionDate(executionDate);
			}else{
				aviso.setExecutionDate(new GregorianCalendar());
			}
		}
		
		return aviso;
	}

	public void setAviso(LocalGISIntervention aviso){
		this.aviso = aviso;
	}

	public LocalGISIntervention getAviso(){
		return this.aviso;
	}



	private JTextField getPresupuestoTextField(){
		if (presupuestoTextField == null){
			
			presupuestoTextField = new TextField(10);
			presupuestoTextField.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYDecimalCampoEdit(getPresupuestoTextField(), 
    						10, 
    						AppContext.getApplicationContext().getMainFrame());
    			}
    		});
			
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				presupuestoTextField.setEditable(false);
			}
			
//			try {
//				MaskFormatter mascara = new MaskFormatter("*.##");
//				presupuestoTextField = new JFormattedTextField (mascara);
//			} catch (ParseException e) {
//				porcentajeTextField = new JFormattedTextField(new Double(14));
//				e.printStackTrace();
//			}
		}
		return presupuestoTextField;
	}
	private JFormattedTextField getPorcentajeTextField(){
		if (porcentajeTextField == null){

			try {
				MaskFormatter mascara = new MaskFormatter("###");
				porcentajeTextField = new JFormattedTextField (mascara);
			} catch (ParseException e1) {
				porcentajeTextField = new JFormattedTextField (new Integer(000));
				e1.printStackTrace();
			}

			porcentajeTextField.setToolTipText("Sólo acepta números (porcentaje entre 1 y 100.");
			
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				porcentajeTextField.setEditable(false);
			}

			porcentajeTextField.addFocusListener(new FocusListener(){
				@Override public void focusGained(FocusEvent e) {
				}
				@Override public void focusLost(FocusEvent e) {
					if (e.getComponent().equals(getPorcentajeTextField())){
						onPercentageChangeDo();
					}
				}
			});

			porcentajeTextField.addKeyListener(new KeyListener(){
				@Override public void keyPressed(KeyEvent e) {
					if (e.getComponent().equals(getPorcentajeTextField())){
						if (e.getKeyCode() == KeyEvent.VK_ENTER){
							onPercentageChangeDo();
						}
					}
				}
				@Override public void keyReleased(KeyEvent e) {}
				@Override public void keyTyped(KeyEvent e) {}
			});
		}
		return porcentajeTextField;
	}

	private void onPercentageChangeDo() {
		DecimalFormat decimalFormat = new DecimalFormat("000");
		int value = 0;
		try{
			value = decimalFormat.parse(porcentajeTextField.getText()).intValue();
		}catch (Exception e2) {
			e2.printStackTrace();
		}
		getPorcentajeProgressBar().setValue(value);
		getPorcentajeTextField().setText(decimalFormat.format(getPorcentajeProgressBar().getPercentComplete()*100));
	}

	private JProgressBar getPorcentajeProgressBar(){
		if (porcentajeProgressBar == null){
			porcentajeProgressBar = new JProgressBar();
			porcentajeProgressBar.setSize(0, 1);
			porcentajeProgressBar.setStringPainted(true);
			porcentajeProgressBar.setValue(0);
			porcentajeProgressBar.setMinimum(0);
			porcentajeProgressBar.setMaximum(100);


			porcentajeProgressBar.addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					int percentage = (int) (getPorcentajeProgressBar().getPercentComplete()*100); 
					getPorcentajeProgressBar().setString(percentage + "%");
				}
			});
			

		}
		return porcentajeProgressBar;
	}
	
	private JComboBox getPriorityComboBox(){
		if (priorityComboBox == null){
			priorityComboBox = new JComboBox();
			priorityComboBox.setRenderer(new PriorityComboBoxRenderer());
			for (int i=10; i > 0; i--){
				priorityComboBox.addItem(i);
			}
			
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				priorityComboBox.setEnabled(false);
			}
		}
		return priorityComboBox;
	}
	
	private JCheckBox getAddIncidentToRoutesCheckBox(){
		if (addIncidentToRoutesCheckBox == null){
			addIncidentToRoutesCheckBox = new JCheckBox(
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.addtoroutes.label")
					);
			
			addIncidentToRoutesCheckBox.setSelected(true);
			
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				addIncidentToRoutesCheckBox.setEnabled(false);
			}
			
			addIncidentToRoutesCheckBox.addItemListener(new ItemListener() {				
				@Override public void itemStateChanged(ItemEvent e) {
					onAddIncidentToRoutesCheckBoxStateChanged(e);
				}
			});
			
		}
		
		return addIncidentToRoutesCheckBox;
	}
	
	private boolean isIncidentToRoutesModified = false;
	public boolean getIsIncidentToRoutesModified(){
		return isIncidentToRoutesModified;
	}
	
	private void onAddIncidentToRoutesCheckBoxStateChanged(ItemEvent e) {
		if(!this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW) 
				&& e.getItem().equals(this.getAddIncidentToRoutesCheckBox())){
			if (this.getAddIncidentToRoutesCheckBox().isSelected()){
				this.getAddRouteBox().setEnabled(true);
			} else {
				this.getAddRouteBox().setEnabled(false);
			}
			
			isIncidentToRoutesModified = true;
		}
	}
	
	private JComboBox getAddRouteBox(){
		if (addRouteComboBox == null){
			addRouteComboBox = new JComboBox();
			addRouteComboBox.setRenderer(new AddToRoutesComboBoxRenderer());
			for (int i=0; i < 3; i++){
				addRouteComboBox.addItem(i);
			}
			addRouteComboBox.setEnabled(false);
			
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				addRouteComboBox.setEnabled(false);
			}
			
			addRouteComboBox.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					isIncidentToRoutesModified = true;
				}
			});
		}
		return addRouteComboBox;
	}
}
