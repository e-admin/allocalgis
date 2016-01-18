
/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

package com.geopista.ui.dialogs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.workbench.ui.OptionsPanel;
/**
 * GeopistaPreferencesPanel
 * Panel que permite modificar preferencias de usuario respecto a fuente, tamaño, color de la selección, ... etc
 */

public class GeopistaPreferencesPanel  extends JPanel implements OptionsPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
	.getLog(GeopistaPreferencesPanel.class);


	private JPanel seleccionStylePanel = new JPanel();
	private JPanel menuPanel = new JPanel();
	private JLabel lblFuente = new JLabel();
	private JLabel lblTamanno = new JLabel();
	private JPanel pnlColor = new JPanel();
	private JTextField pintadoEscalableTextField  = new JTextField(8);

	private Double pintadoEscalableValor = null;
	private Double anchoMonitorValor = null;


	AppContext appContext=(AppContext) AppContext.getApplicationContext();

	private String temp = appContext.getI18nString("colores");
	private String[] coloresArray = temp.split("\\,");

	private String temp2 = appContext.getI18nString("coloresRGB");
	private String[] coloresRGBArray = temp2.split("\\;");



	private int colorSeleccion = Integer.parseInt(appContext.getUserPreference("color.feature.seleccion","3",true));
	private double anchoMonitor = Double.parseDouble(appContext.getUserPreference("ancho.monitor","30.5",true));

	
	private JComboBox cmbColorSeleccion = new JComboBox(coloresArray);
	private JFormattedTextField txtTamanno = null;

	private String temp3 = appContext.getI18nString("fuentes");
	private String[] fuentesArray = temp3.split("\\,");
	private JComboBox cmbFuente = new JComboBox(fuentesArray);
	private JLabel lblEstilo = new JLabel();
	private String temp4 = appContext.getI18nString("estilos");
	private String[] estilosArray = temp4.split("\\,");
	private JComboBox cmbEstilo = new JComboBox(estilosArray);
	private int fuente = Integer.parseInt(appContext.getUserPreference("menu.fuente","0",true));
	private String tamanno = appContext.getUserPreference("menu.tamanno","12",true);
	private int estilo = Integer.parseInt(appContext.getUserPreference("menu.estilo","0",true));
	private JPanel appIconStylePanel = new JPanel();
	private JButton btnFile = new JButton();


	private File file = null;

	private JLabel lblFile = null;
	private String appIcon;

	private JCheckBox pintadoEscalableCheckBox = null;
	private JPanel jPanel = null;
	private JTextField unitTextField = null;
	private JFormattedTextField equivalenceTextField1 = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private DecimalFormat decimalFormat = new DecimalFormat();
	private JPanel visorDataJPanel;
	private JLabel anchoMonitorJLabel = null;
	private JTextField anchoMonitorTextField = null;
	
	/**
	 * This method initializes pintadoEscalableCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getPintadoEscalableCheckBox() {
		if (pintadoEscalableCheckBox == null) {
			pintadoEscalableCheckBox = new JCheckBox();
			pintadoEscalableCheckBox.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			pintadoEscalableCheckBox.setText("escalado");
			pintadoEscalableCheckBox.addItemListener(new java.awt.event.ItemListener() {
				String escala="";
				public void itemStateChanged(java.awt.event.ItemEvent e) {    
					if (getPintadoEscalableCheckBox().isSelected())
					{
						pintadoEscalableTextField.setText(escala);
						pintadoEscalableTextField.setEnabled(true);
					}
					else
					{
						escala=pintadoEscalableTextField.getText();
						pintadoEscalableTextField.setText("");
						pintadoEscalableTextField.setEnabled(false);
					}

				}
			});
		}
		return pintadoEscalableCheckBox;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel2 = new JLabel();
			jLabel1 = new JLabel();
			jLabel = new JLabel();
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints221 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints241 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints251 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints261 = new GridBagConstraints();
			jPanel = new JPanel();
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Conversión Unidades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints221.gridx = 0;
			gridBagConstraints221.gridy = 1;
			gridBagConstraints221.weightx = 1.0;
			gridBagConstraints221.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints221.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints23.gridx = 1;
			gridBagConstraints23.gridy = 1;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints241.gridx = 2;
			gridBagConstraints241.gridy = 1;
			gridBagConstraints241.insets = new java.awt.Insets(5,5,0,5);
			jLabel.setText("m2 por Unidad");
			gridBagConstraints251.gridx = 0;
			gridBagConstraints251.gridy = 0;
			jLabel1.setText("Unidad");
			gridBagConstraints261.gridx = 1;
			gridBagConstraints261.gridy = 0;
			jLabel2.setText("Equivalencia");
			jPanel.add(getUnitTextField(), gridBagConstraints221);
			jPanel.add(getEquivalenceTextField1(), gridBagConstraints23);
			jPanel.add(jLabel, gridBagConstraints241);
			jPanel.add(jLabel1, gridBagConstraints251);
			jPanel.add(jLabel2, gridBagConstraints261);
		}
		return jPanel;
	}
	/**
	 * This method initializes unitTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getUnitTextField() {
		if (unitTextField == null) {
			unitTextField = new JTextField();
		}
		return unitTextField;
	}
	/**
	 * This method initializes equivalenceTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getEquivalenceTextField1() {
		if (equivalenceTextField1 == null) {
			equivalenceTextField1 = new JFormattedTextField(decimalFormat);
		}
		return equivalenceTextField1;
	}
	public static void main(String[] args)
	{
		JFrame fr = new JFrame("Test"); //$NON-NLS-1$
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setSize(500,500);
		fr.getContentPane().add(new GeopistaPreferencesPanel());
		fr.setVisible(true);

	}
	public GeopistaPreferencesPanel()
	{
		decimalFormat.setGroupingUsed(false);
		txtTamanno = new JFormattedTextField(decimalFormat);
		jbInit();


	}
	private void setTexts()
	{
		((TitledBorder)seleccionStylePanel.getBorder()).setTitle(appContext.getI18nString("lblColorSeleccion"));
		((TitledBorder)menuPanel.getBorder()).setTitle(appContext.getI18nString("lblMenu"));
		((TitledBorder)appIconStylePanel.getBorder()).setTitle(appContext.getI18nString("lblIconFile"));
		lblFuente.setText(appContext.getI18nString("lblFuente"));
		lblTamanno.setText(appContext.getI18nString("lblTamanno"));
		txtTamanno.setText(tamanno); 
		lblEstilo.setText(appContext.getI18nString("lblEstilo"));
		pintadoEscalableCheckBox.setText(appContext.getI18nString("lblPintadoEscalable"));
		btnFile.setText(appContext.getI18nString("btnIconFile"));
		
		anchoMonitorJLabel.setText(appContext.getI18nString("anchoMonitorJLabel"));
		

	}
	private void jbInit()
	{

		java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();

		lblFile = new JLabel();
		java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setSize(new java.awt.Dimension(274,330));
		seleccionStylePanel.setLayout(new GridBagLayout());
		seleccionStylePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SelectionStyle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		menuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MenusStyle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		menuPanel.setLayout(new GridBagLayout());
		cmbColorSeleccion.setSelectedIndex(colorSeleccion);

		String tempSeleccion = coloresRGBArray[colorSeleccion];
		String[] rgb = tempSeleccion.split("\\,");
		pnlColor.setBackground(new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2])));

		cmbColorSeleccion.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cmbColorSeleccion_actionPerformed(e);
			}
		});

		cmbFuente.setSelectedIndex(fuente);
		cmbEstilo.setSelectedIndex(estilo);
		appIconStylePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "logotipoStyle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		appIconStylePanel.setLayout(new GridBagLayout());
		pnlColor.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
		lblFile.setText("Default");
		lblFile.setPreferredSize(new java.awt.Dimension(190,15));
		lblFile.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		btnFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				btnFile_actionPerformed(e);
			}
		});

		pnlColor.setOpaque(true);

		pnlColor.setMinimumSize(new java.awt.Dimension(32,32));
		pnlColor.setPreferredSize(new java.awt.Dimension(32,32));
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.gridy = 1;
		gridBagConstraints21.gridwidth = 1;
		gridBagConstraints21.ipady = 0;
		gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints21.insets = new java.awt.Insets(0,20,0,0);
		gridBagConstraints22.gridx = 1;
		gridBagConstraints22.gridy = 1;
		gridBagConstraints22.weightx = 1.0;
		gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints22.ipadx = 0;
		gridBagConstraints22.ipady = 0;
		gridBagConstraints22.insets = new java.awt.Insets(0,5,0,20);
		cmbColorSeleccion.setPreferredSize(new java.awt.Dimension(32,20));
		gridBagConstraints24.gridx = 2;
		gridBagConstraints24.gridy = 3;
		gridBagConstraints24.weightx = 1.0;
		gridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints24.ipadx = 104;
		gridBagConstraints24.ipady = -1;
		gridBagConstraints24.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints25.gridx = 0;
		gridBagConstraints25.gridy = 3;
		gridBagConstraints25.ipadx = 50;
		gridBagConstraints25.ipady = 20;
		gridBagConstraints25.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints25.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints25.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints26.gridx = 2;
		gridBagConstraints26.gridy = 1;
		gridBagConstraints26.weightx = 1.0;
		gridBagConstraints26.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints26.ipadx = 104;
		gridBagConstraints26.ipady = -1;
		gridBagConstraints26.insets = new java.awt.Insets(5,5,0,5);
		gridBagConstraints27.gridx = 2;
		gridBagConstraints27.gridy = 2;
		gridBagConstraints27.weightx = 1.0;
		gridBagConstraints27.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints27.ipadx = 39;
		gridBagConstraints27.ipady = -1;
		gridBagConstraints27.insets = new java.awt.Insets(5,5,0,5);
		gridBagConstraints27.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints28.gridx = 0;
		gridBagConstraints28.gridy = 2;
		gridBagConstraints28.gridwidth = 1;
		gridBagConstraints28.ipadx = 60;
		gridBagConstraints28.ipady = 20;
		gridBagConstraints28.insets = new java.awt.Insets(5,5,0,5);
		gridBagConstraints28.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints28.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints29.gridx = 0;
		gridBagConstraints29.gridy = 1;
		gridBagConstraints29.ipadx = 45;
		gridBagConstraints29.ipady = 20;
		gridBagConstraints29.insets = new java.awt.Insets(5,5,0,5);
		gridBagConstraints29.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints29.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints31.gridx = 2;
		gridBagConstraints31.gridy = 0;
		gridBagConstraints31.ipadx = 0;
		gridBagConstraints31.ipady = 0;
		gridBagConstraints31.insets = new java.awt.Insets(5,5,5,5);
		pintadoEscalableTextField.setColumns(8);
		txtTamanno.setColumns(3);
		gridBagConstraints11.gridx = 2;
		gridBagConstraints11.gridy = 0;
		gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints11.insets = new java.awt.Insets(0,5,0,0);


		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridy = 0;

		this.add(seleccionStylePanel, null);
		seleccionStylePanel.add(pnlColor, gridBagConstraints21);
		appIconStylePanel.add(btnFile, gridBagConstraints31);
		menuPanel.add(cmbEstilo, gridBagConstraints24);
		this.add(appIconStylePanel, null);
		seleccionStylePanel.add(cmbColorSeleccion, gridBagConstraints22);
		appIconStylePanel.add(lblFile, gridBagConstraints1);
		menuPanel.add(lblEstilo, gridBagConstraints25);
		this.add(menuPanel, null);
		menuPanel.add(cmbFuente, gridBagConstraints26);
		menuPanel.add(txtTamanno, gridBagConstraints27);
		
		this.add (getVisorDataJPanel(), null);
		getAnchoMonitorTextField().setText(String.valueOf(anchoMonitor));
		
		this.add(getJPanel(), null);
		menuPanel.add(lblTamanno, gridBagConstraints28);
		menuPanel.add(lblFuente, gridBagConstraints29);

		menuPanel.add(pintadoEscalableTextField, gridBagConstraints11);
		menuPanel.add(getPintadoEscalableCheckBox(), gridBagConstraints3);

		
		
		
		setTexts();
	}

	private JPanel getVisorDataJPanel() {
		if (visorDataJPanel == null) {
			visorDataJPanel = new JPanel();
			visorDataJPanel.setLayout(new GridBagLayout());
			visorDataJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos para la visualización a escala",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			
			anchoMonitorJLabel = new JLabel();
			anchoMonitorJLabel.setText("Ancho del monitor (cm)");

			visorDataJPanel.add (anchoMonitorJLabel, 
					new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0,5,0,0),0,0));
			visorDataJPanel.add (getAnchoMonitorTextField(), 
					new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));

		}
		return visorDataJPanel;
	}
	
	private JTextField getAnchoMonitorTextField() {
		if (anchoMonitorTextField == null) {
			anchoMonitorTextField = new JFormattedTextField(decimalFormat);
		}
		return anchoMonitorTextField;
	}
	public String validateInput() {
		//Comprobamos que el numero introducido sea un Double
		pintadoEscalableValor = null;
		String localEscalableValor = pintadoEscalableTextField.getText();  
		if (!"".equals(localEscalableValor))
		{
			try
			{
				pintadoEscalableValor = new Double(localEscalableValor);
			}
			catch(NumberFormatException e)
			{
				logger.error("validateInput()", e);

				return appContext.getI18nString("lblEscalaErronea");
			}
		}

		anchoMonitorValor = null;
		String localAnchoMonitorValor = anchoMonitorTextField.getText();  

		if ("".equals(localAnchoMonitorValor))
			return null;

		try
		{
			anchoMonitorValor = new Double(localAnchoMonitorValor.replaceFirst(",", "."));
		}
		catch(NumberFormatException e)
		{
			logger.error("validateInput()", e);

			return appContext.getI18nString("lblAnchoErroneo");


		}
		return null;
	}

	/**
	 * okPressed()
	 * Método que se activa cuando se presiona el botón de Aceptar. Escribe las
	 * propiedades modificadas en el Geopista18n.properties. Es necesario reiniciar
	 * la aplicación para que los cambios surtan efecto.
	 */
	public void okPressed() {
		validateInput();// asegura que se evaluan los campos locales
		appContext.setUserPreference("color.feature.seleccion",String.valueOf(cmbColorSeleccion.getSelectedIndex()));
		appContext.setUserPreference("menu.fuente",String.valueOf(cmbFuente.getSelectedIndex()));
		appContext.setUserPreference("menu.estilo",String.valueOf(cmbEstilo.getSelectedIndex()));
		appContext.setUserPreference("menu.tamanno",txtTamanno.getText());

		appContext.setUserPreference("unidad.nombre",unitTextField.getText());
		appContext.setUserPreference("unidad.equivalence",equivalenceTextField1.getText());

		appContext.setUserPreference(SLDStyleImpl.SYMBOLIZER_SIZES_REFERENCE_SCALE,String.valueOf(pintadoEscalableValor));            
		appContext.getBlackboard().put(SLDStyleImpl.SYMBOLIZER_SIZES_REFERENCE_SCALE,pintadoEscalableValor);
		appContext.setUserPreference("ancho.monitor",String.valueOf(anchoMonitorValor));    
		
		if (file!=null)// el usuario ha elegido un fichero específico
		{
			String path = appContext.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH,false)+file.getName();
			GeopistaFunctionUtils.copyFile(new File(path),file);
			appContext.setUserPreference(AppContext.PREFERENCES_APPICON_KEY,file.getName());
			//appContext.getMainFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath()));
		}
	}
	public void init() {
		appIcon=appContext.getUserPreference(AppContext.PREFERENCES_APPICON_KEY,null,false);
		if (appIcon==null)//icono por defecto
			lblFile.setText(AppContext.getMessage("GeopistaPreferencesPanel.DefaultIcon"));
		lblFile.setText(appContext.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH,false)+
				appContext.getUserPreference(AppContext.PREFERENCES_APPICON_KEY,"",false));
		String escala=appContext.getUserPreference(SLDStyleImpl.SYMBOLIZER_SIZES_REFERENCE_SCALE,"null",false);            
		if ("null".equals(escala))
		{
			getPintadoEscalableCheckBox().setSelected(false);
			pintadoEscalableTextField.setEnabled(false);
			pintadoEscalableTextField.setText("");
		}
		else
		{
			getPintadoEscalableCheckBox().setSelected(true);
			pintadoEscalableTextField.setEnabled(true);
			pintadoEscalableTextField.setText(escala);
		}

		String unitName = appContext.getUserPreference("unidad.nombre","",true);
		String unitEquivalence = appContext.getUserPreference("unidad.equivalence","",true);
		equivalenceTextField1.setText(unitEquivalence);
		unitTextField.setText(unitName);


	}

	private void cmbColorSeleccion_actionPerformed(ActionEvent e)
	{
		JComboBox cb = (JComboBox)e.getSource();
		int index = cb.getSelectedIndex();
		updateColor(index);
	}
	/**
	 * updateColor(int index)
	 * Método que actualiza el cuadro de color
	 */
	protected void updateColor(int index) {

		String temp = coloresRGBArray[index];
		String[] rgb = temp.split("\\,");
		pnlColor.setBackground(new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2])));

	}

	private void btnFile_actionPerformed(ActionEvent e)
	{
		boolean status = false;
		status = openFile();
	}

	/**
	 * openFile()
	 * Método que abre el cuadro de dialogo para cargar ficheros
	 */
	boolean openFile()
	{

		JFileChooser fc = new JFileChooser();

		fc.setDialogTitle("Open File");

		// Choose only files, not directories
		fc.setFileSelectionMode( JFileChooser.FILES_ONLY);

		// Start in current directory
		fc.setCurrentDirectory(new File(appContext.getUserPreference(AppContext.PREFERENCES_LAST_IMAGE_FOLDER_KEY,".",false)));

		// Set filter for Java source files.
		//fc.setFileFilter(javaFilter);

		// Now open chooser
		int result = fc.showOpenDialog(this);

		if( result == JFileChooser.CANCEL_OPTION)
		{
			return true;
		}else if( result == JFileChooser.APPROVE_OPTION)
		{
			file = fc.getSelectedFile();
			lblFile.setText(file.getAbsolutePath());
			appContext.setUserPreference(AppContext.PREFERENCES_LAST_IMAGE_FOLDER_KEY,file.getPath());
		}else
		{
			return false;
		}
		return true;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"