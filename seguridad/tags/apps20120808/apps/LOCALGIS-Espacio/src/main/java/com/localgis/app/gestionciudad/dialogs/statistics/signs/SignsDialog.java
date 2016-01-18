package com.localgis.app.gestionciudad.dialogs.statistics.signs;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.geopista.app.layerutil.exception.DataException;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.utils.GestionCiudadOperaciones;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.StatisticalDataOT;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class SignsDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8838279439039379335L;
	
	private JPanel rootPanel = null;
	private OKCancelPanel okCancelPanel = null;
//	private StatisticalDataOT[] statisticsData = null;
	
	private TextArea textAreaInformation = null;
	
	
	public SignsDialog(Frame parentFrame, String tittle, boolean modal, StatisticalDataOT[] statisticsData){
		super(parentFrame, tittle, modal);
//		this.statisticsData = statisticsData;
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.initialize();
		
		loadStatisticsData(statisticsData);
		
		this.pack();
		this.setLocationRelativeTo(parentFrame);
		this.setVisible(true);
	}
	
	
	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		
		this.setLayout(new GridBagLayout());
		
		
		this.add(getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	
	private JPanel getRootPanel() {
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());
			
			this.add(getTextAreaInformation(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return rootPanel;
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
		//TODO implementar si hiciese falta validar datos al salir del formulario
		return true;
	}

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

	
	public void loadStatisticsData (StatisticalDataOT[] statisticsData){
		for(int i=0; i < statisticsData.length; i++){
			
			GestionCiudadOperaciones operacionesBaseDatos = new GestionCiudadOperaciones();
			
			String tipoActuacion = "";
			if (statisticsData[i] != null && statisticsData[i].getActuationType()!=null){
				try {
					String traduccion = operacionesBaseDatos.obtenerTraduccionDeActuacion(statisticsData[i].getActuationType());
					if (traduccion!=null && !traduccion.equals("")){
						tipoActuacion = traduccion;
					} else{
						tipoActuacion = statisticsData[i].getActuationType();
					}
				} catch (DataException e) {
					tipoActuacion = statisticsData[i].getActuationType();
					e.printStackTrace();
				}
			}
			getTextAreaInformation().append(
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.actuationtype") + ": " +
					tipoActuacion +"\n");
			
			
			String tipoIntervencion = "";
			if (statisticsData[i] != null && statisticsData[i].getInterventionType()!=null){
				try {
					String traduccion = operacionesBaseDatos.obtenerTraduccionDeIntervencion(statisticsData[i].getInterventionType());
					if (traduccion!=null && !traduccion.equals("")){
						tipoIntervencion = traduccion;
					} else{
						tipoIntervencion = statisticsData[i].getInterventionType();
					}
				} catch (DataException e) {
					tipoActuacion = statisticsData[i].getInterventionType();
					e.printStackTrace();
				}
			}
			getTextAreaInformation().append(
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.interventiontype") + ": " + 
					tipoIntervencion +"\n");
			
			getTextAreaInformation().append(
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.numinterventions") + ": " +
					Integer.toString(statisticsData[i].getNumInterventions()) +"\n");
			
			getTextAreaInformation().append(
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.mediumtime") + ": " +
					Double.toString(statisticsData[i].getMedia()) + " " +
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.mediumtime.days") + "\n");
			
			getTextAreaInformation().append(
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.standarddeviation") + ": " +
					Double.toString(statisticsData[i].getStdDeviation()) +"\n");
			
			getTextAreaInformation().append(
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.idMunicipio") + ": " +
					Integer.toString(statisticsData[i].getIdMunicipio()) +"\n");
			
			getTextAreaInformation().append("---------------------------------------------\n\n");
		}	
	}
	
	private TextArea getTextAreaInformation(){
		if (textAreaInformation == null){
			textAreaInformation =  new TextArea("",20,57,
					TextArea.SCROLLBARS_VERTICAL_ONLY );
			textAreaInformation.setEditable(false);
			textAreaInformation.setEnabled(true);
		}
		
		return textAreaInformation;
	}
}
