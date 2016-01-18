/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeconfig;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.uva.route.network.NetworkManager;
import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dialogs.PMRParametersJDialog;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dialogs.PMRParametersJPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;

/**
 * Dialog to configure algorithm and subNetwork for CalcularRutaPlugin
 * 
 * @author javieraragon
 *
 */
public class CalcRutaConfigDialog extends JDialog{

		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel panelConfigOptions = null;
	private JPanel panelBotonera = null;
	
	private JButton acceptButton = null;
	private JButton cancelButton = null;

	private JDialog calcRutaDialog= null;
	
	private JComboBox listaAlgoritmosComboBox = null;
	private JComboBox listaVehiculosComboBox = null;
	
	private JList listaSubredesList = null;

	private NetworkManager networkMgr;
	private Set<String> redesSet;
	private CalcRutaConfigFileReaderWriter configProperties = null;

	private boolean configuratioIsFinished = false;

	private JPanel redespanel;
	
	/**
	 * Constantes del configurador
	 * Subred, red y algoritmo	 * 
	 */
	private static String redName = " ";
	private static String subredName = " ";
	private static String algorithmName = " ";

	/**
	 * 
	 */
	public CalcRutaConfigDialog(WorkbenchFrame workbenchFrame, NetworkManager networkManager, JDialog dialog) {
		// TODO Auto-generated constructor stub
		
		
		super(AppContext.getApplicationContext().getMainFrame(),"Conficurador de Ruta",true);
		configProperties = new CalcRutaConfigFileReaderWriter();
		
		this.calcRutaDialog = dialog;
		this.networkMgr = networkManager;
		
		this.setSize(300, 275);
		this.setLocationRelativeTo(null);
				
		this.initializeCalcRutaConfiguratorDialog();
		this.loadCalcRutaProperties();
		
		if (calcRutaDialog != null){
			this.calcRutaDialog.setVisible(false);
			this.calcRutaDialog.setEnabled(false);
		}
		
	}
	
	public CalcRutaConfigDialog(WorkbenchFrame workbenchFrame, NetworkManager networkManager) {
		// TODO Auto-generated constructor stub
				
		super(AppContext.getApplicationContext().getMainFrame(),"Conficurador de Ruta",true);
		configProperties = new CalcRutaConfigFileReaderWriter();
		
		this.networkMgr = networkManager;
				
		this.setSize(300, 275);
		this.setLocationRelativeTo(null);
				
		this.initializeCalcRutaConfiguratorDialog();
		this.loadCalcRutaProperties();
		
		if (calcRutaDialog != null){
			this.calcRutaDialog.setVisible(false);
			this.calcRutaDialog.setEnabled(false);
		}
		
	}
	
	private void initializeDialogPosition() {
		//Get the screen size  
		Toolkit toolkit = Toolkit.getDefaultToolkit();  
		Dimension screenSize = toolkit.getScreenSize();  
		int x = (screenSize.width - this.getWidth()) / 2;  
		int y = (screenSize.height - this.getHeight()) / 2;    
		//Set the new frame location  
		this.setLocation(x, y);
		

	}

	private void initializeCalcRutaConfiguratorDialog() {
		// TODO Auto-generated method stub
		this.addWindowListener(new java.awt.event.WindowAdapter(){
	           public void windowClosing(WindowEvent e){
	        	   onWindowClosingDo();
	           }
	        }
	        );

		this.setLayout(new GridBagLayout());
				
		this.add(getRedesPanel(), new GridBagConstraints(
				0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(getJPanelConfigurationOptions(), new GridBagConstraints(
				0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 5, 0, 5), 0, 0));

		this.add(getButtonsPanel(), new GridBagConstraints(
				0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 5, 0, 5), 0, 0));
		
	}

	private void onWindowClosingDo(){
		this.setVisible(false);
		this.setEnabled(false);
		this.configuratioIsFinished = true;
		this.dispose();
	}
	
	private JPanel getRedesPanel(){
		if (redespanel == null){
			redespanel = new JPanel();
			
			redespanel = new JPanel(new GridBagLayout());
			
			JScrollPane scpanel = new JScrollPane();
			scpanel.setViewportView(getListaSubRedesList());
			
			Iterator it = this.networkMgr.getNetworks().keySet().iterator();
			System.out.println(it);
			while(it.hasNext()){
				String nombreRed = (String)it.next();
				Iterator it2 = this.networkMgr.getNetwork(nombreRed).getSubnetworks().keySet().iterator();
				
				if (this.networkMgr.getNetwork(nombreRed).getSubnetworks().isEmpty()){
					((DefaultListModel)getListaSubRedesList().getModel()).addElement(nombreRed);
				} else {
					while (it2.hasNext()){
						((DefaultListModel)getListaSubRedesList().getModel()).addElement(((String)it2.next()));
					}
				}
			}
			
		
			redespanel.add(new JLabel("Seleccione las redes: "),
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
					GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
			redespanel.add(scpanel,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
					GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 100));
			
		}
		
		return redespanel;
	}
	
	private JPanel getButtonsPanel() {
		// TODO Auto-generated method stub
		if (panelBotonera == null){
			
			panelBotonera = new JPanel(new GridBagLayout());
						
			panelBotonera.add(new JLabel(),
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
					GridBagConstraints.FIRST_LINE_END,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5),0, 0));
			
			panelBotonera.add(getAcceptButton(),
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
					GridBagConstraints.FIRST_LINE_END,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
			panelBotonera.add(new JLabel(),
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
					GridBagConstraints.FIRST_LINE_END,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
			panelBotonera.add(getCancelButton(),
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
					GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		}

		return panelBotonera;
	}

	private JPanel getJPanelConfigurationOptions() {
		// TODO Auto-generated method stub
		if (panelConfigOptions == null){
			panelConfigOptions = new JPanel(new GridBagLayout());
									
			panelConfigOptions.add(getListaAlgoritmosComboBox(),
					new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
					GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			getListaAlgoritmosComboBox().setVisible(false);
			
			panelConfigOptions.add(new JLabel("Seleccione la forma de desplazarse: "),
					new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
					GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
			panelConfigOptions.add(getListaVehiculosComboBox(),
					new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
					GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
		}
		return panelConfigOptions;
	}
	
	public JList getListaSubRedesList() {
		if (this.listaSubredesList == null){
			listaSubredesList= new JList();
			listaSubredesList.setModel(new DefaultListModel());
			listaSubredesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
			
		}
		
		return listaSubredesList;
	}

	public JComboBox getListaAlgoritmosComboBox() {
		if (listaAlgoritmosComboBox == null){
			Vector<String> listaAlg = new Vector<String>();
			listaAlg.add(I18N.get("calcruta","routeengine.calcularruta.dijkstraalgorithm"));
			listaAlg.add(I18N.get("calcruta","routeengine.calcularruta.astaralgorithm"));
			
			listaAlgoritmosComboBox = new JComboBox(listaAlg);
		}
		
		return listaAlgoritmosComboBox;
	}
	
	public JComboBox getListaVehiculosComboBox() {
		if (listaVehiculosComboBox == null){
			Vector<String> listaAlg = new Vector<String>();
			listaAlg.add("Caminando");
			listaAlg.add("En Coche");
			
			listaVehiculosComboBox = new JComboBox(listaAlg);
		}
		
		return listaVehiculosComboBox;
	}
	
	
	public JButton getAcceptButton() {
		if(acceptButton == null){
			acceptButton = new JButton("Aceptar");
			acceptButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					onAcceptButtonDo();
				}
			}
			);
		}
		return acceptButton;
	}

	public JButton getCancelButton() {
		if(cancelButton == null){
			cancelButton = new JButton("Cancelar");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					onCancelButtonDo();
				}
			}
			);
		}
		return cancelButton;
	}
	
	private boolean onAcceptButtonDo() {
		// TODO Auto-generated method stub
		
		if (this.listaVehiculosComboBox.getSelectedItem().toString().equals("Caminando")){

			PMRParametersJDialog dialog = new PMRParametersJDialog();
			PMRParametersJPanel prmParametersJPanel = (PMRParametersJPanel)((PMRParametersJDialog)dialog).getPanel();

			CalcRutaConfigFileReaderWriter configProperties =new CalcRutaConfigFileReaderWriter();
			ArrayList parametersList = new ArrayList();
			parametersList.add("pavementWidth" + "=" + prmParametersJPanel.getjTextAnchuraMinAcera().getText() );
			parametersList.add("transversalSlope" + "=" + prmParametersJPanel.getjTextMaxPendTransAcera().getText() );
			parametersList.add("longitudinalSlope" + "=" + prmParametersJPanel.getjTextMaxPendLongAcera().getText() );
			parametersList.add("disabilityType" + "=" + prmParametersJPanel.getComboTiposMinusvalia().getSelectedItem() );
			if (prmParametersJPanel.getComboTiposMinusvalia().getSelectedItem() != null && !prmParametersJPanel.getComboTiposMinusvalia().getSelectedItem().toString().equals("Ninguna"))
				configProperties.writeParametersIntoConfigFile(parametersList);
			else
				configProperties.writeParametersIntoConfigFile(new ArrayList());
		}else{
			configProperties.writeParametersIntoConfigFile(new ArrayList());
		}
		Object[] listaRedes = getListaSubRedesList().getSelectedValues();
		ArrayList<String> redesString = new ArrayList<String>();
		if(listaRedes!=null && listaRedes.length>0){
			for(int i=0; i < listaRedes.length; i++){
				if (listaRedes[i]!=null && listaRedes[i] instanceof String){
					redesString.add((String) listaRedes[i]);
				}
			}
		}
		
		configProperties.writeParametersIntoConfigFile(
				redesString.toArray(new String[redesString.size()]),
				(String) this.listaAlgoritmosComboBox.getSelectedItem(),
				(String) this.listaVehiculosComboBox.getSelectedItem()
				);
		
		this.setEnabled(false);
		this.setVisible(false);	
		this.configuratioIsFinished = true;
		
		this.dispose();
		
		return true;
	}
	
	private boolean onCancelButtonDo() {
		// TODO Auto-generated method stub
		
		this.setEnabled(false);
		this.setVisible(false);
		this.configuratioIsFinished = true;
		this.dispose();
	
		return true;
	}
	
	
	public String getRedName(){
		return this.redName;
	}
	
	public String getSubRedName(){
		return this.subredName;
	}
	
	public String getAlgorithmName(){
		return this.algorithmName;
	}
	
	private void loadCalcRutaProperties() {
		// TODO Auto-generated method stub	
				
		if (listaVehiculosComboBox != null){
			String algorithm = configProperties.getTipoVehiculo();
			if (algorithm != null && !algorithm.equals("")){
				listaVehiculosComboBox.setSelectedItem(algorithm);
				if (listaVehiculosComboBox.getItemCount() > 0 && listaVehiculosComboBox.getSelectedIndex()==-1){
					listaVehiculosComboBox.setSelectedIndex(0);
				}
			}
		}
		
		if (listaSubredesList != null && listaSubredesList.getModel().getSize() > 0){
			for (int m = 0; m < listaSubredesList.getModel().getSize(); m++){
				String redFromList = (String) listaSubredesList.getModel().getElementAt(m);
				String[] redes = configProperties.getRedesNames();
				if (redes != null && redes.length>0){
					for (int i = 0; i < redes.length; i++){
						String red = redes[i];
						if (red != null && !red.equals("")){
							if (redFromList.equals(red)){
								((DefaultListSelectionModel)listaSubredesList.getSelectionModel()).addSelectionInterval(m,m);
							}
						}
					}
				}
			}
		}

	}

	public boolean finished() {
		// TODO Auto-generated method stub
		return this.configuratioIsFinished ;
	}
}
