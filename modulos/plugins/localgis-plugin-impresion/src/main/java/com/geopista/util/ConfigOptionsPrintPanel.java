/**
 * ConfigOptionsPrintPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.util.Locale;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.plugin.print.PrintLayoutFrame;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
/**

 */

public class ConfigOptionsPrintPanel extends javax.swing.JPanel implements WizardPanel{

	private static final long serialVersionUID = 1L;

	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard  = aplicacion.getBlackboard();
	private WizardContext wizardContext;
	private PlugInContext context;

	private String nextID = null;
	private String localId = null;

	private JTextField rutaDestino;
	private JButton rutaDestinoBtn;
	private JTextField prefijoImpresion;
	
	
	private JComboBox impresorasComboBox = null;
	private JButton configImpresoraBtn = null;
	private JCheckBox modificarConfiguracionCheck = null;
	private PrintLayoutFrame printFrame = null;

	public ConfigOptionsPrintPanel(String id, String nextId, PlugInContext context2) {
		this.context=context2;
		this.nextID = nextId;
		this.localId = id;

		initialize();
	}

	private  void initialize() {
		this.setName((UtilsPrintPlugin.getMessageI18N("ConfigPrintConfigPanel.Name"))); 
		this.setLayout(new GridBagLayout());
		this.setSize(365, 274);
		this.add(getPanelConfiguracionImpresion(),  new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(10, 10, 0, 10), 0, 0));
	}

	
	
	private JPanel getPanelConfiguracionImpresion () {
		int posFila = 0;

		int widthCol_1 = 229;
		
		//Combo con impresoras disponibles: solo informacion visual debe seleccionar desde configuracion impresora
		impresorasComboBox = new JComboBox(PrintServiceLookup.lookupPrintServices(null, null));
		impresorasComboBox.setPreferredSize(new Dimension(widthCol_1, 21));
		impresorasComboBox.setEnabled(false);
		
		//Muestra panel para establecer la configuracion de impresion
		configImpresoraBtn = new JButton(UtilsPrintPlugin.getMessageI18N("ConfigPrintConfigPanel.configurar"));
		configImpresoraBtn.setToolTipText("");
		//rutaDestinoBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
		configImpresoraBtn.setPreferredSize(new Dimension(30, 20));
		configImpresoraBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Actualizar serivicio impresion seleccionado en combo y mostrar panel configuracion
				establecerServicioImpresion(printFrame, ((PrintService)impresorasComboBox.getSelectedItem()).getName());
				printFrame.getPrinterJob().pageDialog(printFrame.getPageFormat());
				//Actualizar combo con impresora seleccionada
				impresorasComboBox.setSelectedItem(printFrame.getPrinterJob().getPrintService());
			}
		});
		//Modificar configuracion impresion: actualmente no permitir (pendiente ver que pasa en linux)!!!!
		modificarConfiguracionCheck = new JCheckBox(UtilsPrintPlugin.getMessageI18N("ConfigPrintConfigPanel.modificarConfiguracion"));
		modificarConfiguracionCheck.setEnabled(false);
		modificarConfiguracionCheck.setSelected(false);
		modificarConfiguracionCheck.addChangeListener(new ChangeListener() { 
			public void stateChanged(ChangeEvent e) {
				//Activar/Desactivar boton establecer opcfiones impresiones
				configImpresoraBtn.setEnabled(modificarConfiguracionCheck.isSelected());
			}
		});
				
		
		//Ruta destino
		rutaDestino = new JTextField();
		rutaDestino.setPreferredSize(new Dimension(widthCol_1, 21));
		rutaDestino.setSize(100, 20);
		
		rutaDestinoBtn = new JButton(UtilsPrintPlugin.getMessageI18N("ConfigPrintConfigPanel.seleccionar"));
		//TODO: icono????
		//rutaDestinoBtn.setIcon();
		rutaDestinoBtn.setToolTipText("");
		//rutaDestinoBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
		rutaDestinoBtn.setPreferredSize(new Dimension(30, 20));
		rutaDestinoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				establecerRutaDestino();
			}
		});

		//Prefijo impresion
		prefijoImpresion = new JTextField();
		prefijoImpresion.setPreferredSize(new Dimension(widthCol_1, 21));
		prefijoImpresion.setSize(100, 20);
		prefijoImpresion.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent evt) {
				wizardContext.inputChanged();
			}
		});
	
		JPanel panel = new JPanel(new GridBagLayout());
		
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("ConfigPrintConfigPanel.configuracionImpresora")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(0, 10, 0, 10), 0, 0));
		panel.add(impresorasComboBox,  new GridBagConstraints(0, posFila, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(configImpresoraBtn,  new GridBagConstraints(1, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 0, 0, 10), 0, 0));
		panel.add(modificarConfiguracionCheck,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(0, 10, 0, 10), 0, 0));
		
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("ConfigPrintConfigPanel.rutaDestino")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(0, 10, 0, 10), 0, 0));
		panel.add(rutaDestino,  new GridBagConstraints(0, posFila, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(rutaDestinoBtn,  new GridBagConstraints(1, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 0, 0, 10), 0, 0));
		
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("ConfigPrintConfigPanel.nombreSerie")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 10, 0, 10), 0, 0));
		panel.add(prefijoImpresion,  new GridBagConstraints(0, posFila++, 2, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));

		return panel;
	}


	public void enteredFromLeft(Map dataMap) {
		//Obtenemos configuracion y cargamos datos en pantalla
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		
		//Establecer seleccion por defecto PDFCreator
		establecerEstadoOpcionesImpresion(config);
		
		//Directorio base: ubicacion por defecto
		File dirBase = new File (UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,null,false)); 
		String ruta = dirBase.getPath() + dirBase.separator + "PluginImpresion";
		rutaDestino.setText((config != null && config.getRutaDestino() != null && !config.getRutaDestino().equals(""))? config.getRutaDestino() : ruta);
		prefijoImpresion.setText((config != null)? config.getPrefijoImpresion() : "");
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception {
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		config.setRutaDestino (rutaDestino.getText());
		config.setPrefijoImpresion(prefijoImpresion.getText());
		config.setServicioImpresion((PrintService)impresorasComboBox.getSelectedItem());
		config.setConfigPersonalizada(modificarConfiguracionCheck.isSelected());
		config.setPrintFrame(printFrame);
		
		//FIXME: ATENCION:
		//Imprimir: ACTUALMENTE OPCION FIJA UN FICHERO POR HOJA
		//Opciones implementadas:
		//Abrir y mostrar plantilla con todos los resultados
		//Generar fichero con resultados: imprime directamente resultados en un PDF sin permitir edicion plantilla 
		//			Generar un fichero con todos los resultados
		//			Generar multiples ficheros (un fichero por hoja)
		
		//Podemos establecer opciones visuales para configuracion manual por el usuario
		//Actualmente opcion fija: generacion un fichero por hoja si
		config.setResultadoEnFichero(true);
		config.setUnFicheroPorHoja(true);
	}

	/**
	 * Tip: Delegate to an InputChangedFirer.
	 * @param listener a party to notify when the input changes (usually the
	 * WizardDialog, which needs to know when to update the enabled state of
	 * the buttons.
	 */
	public void add(InputChangedListener listener) {
	}

	public void remove(InputChangedListener listener) {
	}
	public String getTitle() {
		return this.getName();
	}

	public String getInstructions() {
		return UtilsPrintPlugin.getMessageI18N("ConfigPrintConfigPanel.Instrucctions"); 
	}
	public boolean isInputValid() {
		boolean isValid = false;
		if ((rutaDestino.getText() != null && !rutaDestino.getText().equals("")) && (prefijoImpresion.getText() != null && !prefijoImpresion.getText().equals("")))
			isValid = true;
		return isValid;
	}
	public void setWizardContext(WizardContext wd) {
		wizardContext = wd;
	}
	public String getID() {
		return localId;
	}

	public void setNextID(String nextID) {
		this.nextID=nextID;
	}
	public String getNextID() {
		return nextID;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting() {
		// TODO Auto-generated method stub
	}


	public void establecerRutaDestino() 
	{
		//Directorio base
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File (rutaDestino.getText()));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int respuesta = fileChooser.showSaveDialog(this);
		//Comprobar si se ha pulsado Aceptar
		if (respuesta == JFileChooser.APPROVE_OPTION) {
			//Crear un objeto File con el archivo elegido
			File archivoElegido = fileChooser.getSelectedFile();
			//Mostrar el nombre del archvivo en un campo de texto
			rutaDestino.setText(archivoElegido.getPath());
		}
		wizardContext.inputChanged();
	}
	
	
	/**
	 * Obtiene servicio correspondiente al nombre indicado
	 * @return retorna servicio de impresion
	 */
	private PrintService obtenerServicioImpresion (PrintLayoutFrame frame, String printName) {
		//Obtebenmos servicio de impresion solicitido
		PrintServiceAttributeSet  aset = new HashPrintServiceAttributeSet ();
		aset.add(new PrinterName(printName, Locale.getDefault()));
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, aset);
		
		return ((services != null && services.length > 0)? services[0] : null);
	}
	
	/**
	 * Establece servicio impresion correspondiente al nombre
	 */
	private void establecerServicioImpresion (PrintLayoutFrame frame, String printName) {
		//Obtebenmos la impresora PDF creator con configuracion establecida y establecemos como servicio de impresion
		PrintService impresora = obtenerServicioImpresion(frame, printName);
		try {
			if (impresora != null) 
				frame.getPrinterJob().setPrintService(impresora);
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Establece estado de los campos de opciones de impresion segun corresponda
	 */
	private void establecerEstadoOpcionesImpresion (ConfigPrintPlugin config) 
	{
		//Obtener frame plantilla impresion
		printFrame = config.getPrintFrame();
		printFrame.setVisible(false);
		
		//Determinar si tenemos impresora PDFCreator
		String printNamePDF = "PDFCreator";
		//FIXME: OPCION FIJA ACTUALMENTE
		//SOLO se permite el CAMBIO de configuracion si NO tenemos PDFCreator
		boolean permitirModificar = (obtenerServicioImpresion(printFrame, printNamePDF) == null);
		
		//Obtener configuracion almacenada
		boolean hayConfiguracion = ((config != null)? config.isConfigPersonalizada() : false);
		PrintService servicioImpresion = ((config != null)? config.getServicioImpresion() : null);
		
		//Servicio de impresion
		String printName = ((servicioImpresion != null)? servicioImpresion.getName(): printNamePDF);
		establecerServicioImpresion(printFrame, printName);
		impresorasComboBox.setSelectedItem(printFrame.getPrinterJob().getPrintService());
		impresorasComboBox.setEnabled(permitirModificar);
		
		//Permitir cambio configuracion
		modificarConfiguracionCheck.setEnabled(permitirModificar);
		modificarConfiguracionCheck.setSelected(hayConfiguracion);
		//Activar/Desactivar btn opciones configuracion
		configImpresoraBtn.setEnabled(modificarConfiguracionCheck.isSelected());
	}
	
}
