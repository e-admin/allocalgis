package com.geopista.ui.plugin.routeenginetools.pavementfactory;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToLoadFromDataStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerNameRenderer;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class GeneratePavementDialog extends JDialog  implements ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5224329718344856621L;
	private JComboBox tipoPasos = null;
	private JTextField nombreRedTextField = null;
	private static String RED = "RedPMR";
	private JPanel rootPanel = null; 
	private JPanel featuresSelectPanel = null;
	private OKCancelPanel okCancelPanel = null;
	private PanelToLoadFromDataStore redesPanel = null;
	private boolean bPasosCebra = false;

	protected PlugInContext context;

	public GeneratePavementDialog(Frame frame, String nombreRed, PlugInContext context){
		this(frame, nombreRed, context, false);
	}
	public GeneratePavementDialog(Frame frame, String nombreRed, PlugInContext context, boolean pasosZebra){
		super(frame, "", true);
		this.context = context;

		this.setSize(440, 175);
		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
		this.bPasosCebra = pasosZebra;
		this.initialize();
		Blackboard blackboard = AppContext.getApplicationContext().getBlackboard();
		if (blackboard != null && blackboard.get(RED) != null)
			this.nombreRedTextField.setText(blackboard.get(RED).toString());
		this.setResizable(false);
		this.setVisible(true);

	}


	private void initialize() {
		// TODO Auto-generated method stub
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});

		this.setLayout(new GridBagLayout());

		this.add(this.getPanelPrincipal(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		//CUIDADO! Se deja un hueco en medio para poder insertar el panel con las opciones de
		// StreetNetwotrk... si se quieren extender para mas dialogos. modificar.

		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}



	private JPanel getPanelPrincipal(){
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());
			redesPanel = new PanelToLoadFromDataStore(context.getWorkbenchContext());
			
			rootPanel.add(getFeaturesSelectPanel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

		}
		return rootPanel;
	}


	public JTextField getNombreRedTextField() {
		// TODO Auto-generated method stub
		if (nombreRedTextField == null){
			nombreRedTextField = new JTextField(20);
		}
		return nombreRedTextField;
	}



	public JComboBox getTipoPasos() {
		// TODO Auto-generated method stub
		if (tipoPasos == null){
			tipoPasos = new JComboBox(new String[]{"CON REBAJE","SIN REBAJE"});
		}
		return tipoPasos;
	}


	public JPanel getFeaturesSelectPanel() {
		// TODO Auto-generated method stub
		if (featuresSelectPanel == null){
			featuresSelectPanel = new JPanel(new GridBagLayout());

			featuresSelectPanel.setBorder(BorderFactory.createTitledBorder
					(null, "Datos de la red", 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));


			JPanel auxPanel = new JPanel(new GridBagLayout());

			auxPanel.add(new JLabel("Nombre de la red:") , 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			auxPanel.add(getNombreRedTextField() , 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 100, 0));
			
			if (bPasosCebra){
				auxPanel.add(new JLabel("Tipo de pasos de cebra:") , 
						new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
								GridBagConstraints.HORIZONTAL, 
								new Insets(0, 5, 0, 5), 0, 0));
	
				auxPanel.add(getTipoPasos() , 
						new GridBagConstraints(1, 2, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
								GridBagConstraints.HORIZONTAL, 
								new Insets(0, 5, 0, 5), 100, 0));
			}
			featuresSelectPanel.add( auxPanel, 
					new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 20));

		}
		return featuresSelectPanel;
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

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

	public void itemStateChanged(ItemEvent e) {
	}

	
	public void showDialog(boolean visible){
		this.setVisible(visible);
	}

	protected boolean isInputValid() {

		boolean insertedNetworkName = !this.nombreRedTextField.getText().equals("");
		if (!insertedNetworkName){
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "Inserte el nombre de la red");
			return false;
		}
		Blackboard blackboard = AppContext.getApplicationContext().getBlackboard();
		blackboard.put(RED, this.nombreRedTextField.getText());
		return insertedNetworkName; 
	}
}
