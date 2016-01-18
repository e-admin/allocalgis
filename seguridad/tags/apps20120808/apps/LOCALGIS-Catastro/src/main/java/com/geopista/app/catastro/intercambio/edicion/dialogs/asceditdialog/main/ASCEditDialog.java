package com.geopista.app.catastro.intercambio.edicion.dialogs.asceditdialog.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableCellRenderer;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.dialogs.ASCPanel;
import com.geopista.app.catastro.intercambio.edicion.dialogs.FileLoader;
import com.geopista.app.catastro.intercambio.edicion.dialogs.FxccPanel;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.edicion.dialogs.asceditdialog.utils.LimitadorCaracteres;
import com.geopista.app.catastro.intercambio.edicion.dialogs.asceditdialog.utils.LinderoEditDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.TableLinderosModel;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.model.beans.ASCCatastro;
import com.geopista.app.catastro.model.beans.LinderoCatastro;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.editor.GeopistaContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ASCEditDialog extends JDialog{

	/**
	 * Auto generated serial version Id.
	 */
	private static final long serialVersionUID = 8526628107698469987L;
	


	private JPanel jPanelIdentificacion = null;
	private JLabel jLabelCodDelegacion = null;
	private JLabel jLabelNombreGerencia = null;
	private JLabel jLabelCodMunicipio = null;
	private JLabel jLabelNombreMunicipio = null;
	private JTextField codDelegacionTextField = null;
	private JTextField nombreGerenciaTextField = null;
	private JTextField codMunicipioTextField = null;
	private JTextField nombreMunicipioTextField = null;
	private JLabel jLabelCodViaPublica = null;
	private JLabel jLabelSiglas = null;
	private JLabel jLabelNombreVia = null;
	private JLabel jLabelNumPolicia = null;
	private JLabel jLabelLetraDuplicado = null;
	private JLabel jLabelParcela1 = null;

	private JTextField codViaTextField = null;
	private ComboBoxEstructuras siglasComboBox = null;
	private JTextField nombreViaTextField = null;
	private JTextField numPoliciaTextField = null;
	private JTextField letraDuplicadoTextField = null;
	private JTextField parcelaCatastral1TextField = null;
	private JTextField parcelaCatastral2TextField = null;
	private JLabel jLabelParcelaCatastral2 = null;
	private JTable jTableLinderos = null;

	private TableLinderosModel tablelinderosmodel;
//	private TableUsosModel tableusosmodel;
//	private TablePlantasModel tableplantasmodel;

	private JLabel jLabelEscalaCaptura = null;
	private JTextField escalaCapturaTextField = null;
	private JLabel jLabelFechaCaptura = null;
	private JTextField fechaCapturaTextField = null;
	
	//botones de buscar
	private JButton buscarViaButton = null;


	private JButton acceptButton = null;
	private JButton cancelButton = null;

	private JButton modificarLinderoButton = null;
	private JButton anniadirLinderoButton = null;
	private JButton eliminarLinderoButton = null;

	private ASCCatastro asc = null;
	private FxccPanel fxccPanel = null;

	private static AppContext app =(AppContext) AppContext.getApplicationContext();


	public ASCEditDialog(){
		super(AppContext.getApplicationContext().getMainFrame());

		Locale loc=I18N.getLocaleAsObject();         
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("Expedientes",bundle);

		this.setLocationRelativeTo(null); 

		// poner tamaño, posicion y visible.
		this.setSize(800, 320);
		this.initializeDialogPosition();  
		this.setVisible(true);
		
		this.setResizable(false);

		this.initialize();
		
		this.setTitle("Datos ASC  (" + this.getClass().getSimpleName() + ").");
		this.getParent().setEnabled(false);
		
	}

	public ASCEditDialog(ASCCatastro asc){
		super(AppContext.getApplicationContext().getMainFrame());

		Locale loc=I18N.getLocaleAsObject();         
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("Expedientes",bundle);

		this.setLocationRelativeTo(null); 

		// poner tamaño, posicion y visible.
		this.setSize(800, 320);
		this.initializeDialogPosition();  
		this.setVisible(true);
		
		this.setResizable(false);

		this.initialize();

		this.setTitle("Datos ASC  (" + this.getClass().getSimpleName() + ").");
		
		this.asc = asc;
		loadData();
		this.getParent().setEnabled(false);
	}
	
	public ASCEditDialog(FxccPanel fxcc){
		super(AppContext.getApplicationContext().getMainFrame());

		Locale loc=I18N.getLocaleAsObject();         
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("Expedientes",bundle);

		this.setLocationRelativeTo(null); 

		// poner tamaño, posicion y visible.
		this.setSize(800, 320);
		this.initializeDialogPosition();  
		this.setVisible(true);
		
		this.setResizable(false);

		this.initialize();

		this.setTitle("Datos ASC  (" + this.getClass().getSimpleName() + ").");
		
		this.fxccPanel = fxcc;
		this.getParent().setEnabled(false);
	}
	
	public ASCEditDialog(ASCCatastro ascc, FxccPanel fxcc){
		super(AppContext.getApplicationContext().getMainFrame());

		Locale loc=I18N.getLocaleAsObject();         
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("Expedientes",bundle);

		

		// poner tamaño, posicion y visible.
		this.setVisible(true);
		
		this.setResizable(false);

		this.initialize();

		this.setTitle("Datos ASC  (" + this.getClass().getSimpleName() + ").");
		
		this.asc = ascc;
		loadData();
		
		this.fxccPanel = fxcc;
		this.getParent().setEnabled(false);
		
		this.setSize(800, 320);
		this.setLocationRelativeTo(null); 
	}


	/**
	 * 
	 */
	private void initializeDialogPosition() {
		//Get the screen size  
		Toolkit toolkit = Toolkit.getDefaultToolkit();  
		Dimension screenSize = toolkit.getScreenSize();  
		int x = (screenSize.width - this.getWidth()) / 2;  
		int y = (screenSize.height - this.getHeight()) / 2;
		//Set the new frame location  
		this.setLocation(x, y);
	}

	private void initialize(){

		this.addWindowListener(new java.awt.event.WindowAdapter(){
			public void windowClosing(WindowEvent e){
				getParent().setEnabled(true);
			}
		}
		);
	
		this.setLayout(new GridBagLayout());

		this.add(getJPanelIdentificacion(), new GridBagConstraints(
				0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 5, 0, 5), 0, 0));

		this.add(getButtonsPanel(), new GridBagConstraints(
				0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 5, 0, 5), 0, 0));
		//		this.add(getJPanelExpediente(), new GridBagConstraints(
		//				0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
		//				new Insets(0, 5, 0, 5), 0, 0));
		//		this.add(getJButtonGenerarFicheroASC(), new GridBagConstraints(
		//				0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.EAST, GridBagConstraints.NONE,
		//				new Insets(0, 5, 0, 5), 0, 0));
		//		this.add(jButtonCargarFicheroASC(), new GridBagConstraints(
		//				0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.NONE,
		//				new Insets(0, 5, 0, 5), 0, 0));
	}

	private void loadData() {
		// TODO Auto-generated method stub
		this.getJTextFieldCodDelegacion().setText(this.asc.getCodDelegacion());
		this.getJTextFieldNombreGerencia().setText(this.asc.getNombreGerencia());
		this.getJTextFieldCodMunicipio().setText(this.asc.getCodMunicipio());
		this.getJTextFieldNombreMunicipio().setText(this.asc.getNombreMunicipio());
		this.getJTextFieldCodVia().setText(this.asc.getCodVia());
		this.getComboBoxdSiglas().setSelectedPatron(this.asc.getSigla());
		this.getJTextFieldNombreVia().setText(this.asc.getNomVia());
		this.getJTextFieldNumPolicia().setText(this.asc.getNumPolicia());
		this.getJTextFieldLetraDuplicado().setText(this.asc.getLetraDuplicado());
		this.getJTextFieldParcelaCatastral1().setText(this.asc.getRefCatastral().getRefCatastral1());
		this.getJTextFieldParcelaCatastral2().setText(this.asc.getRefCatastral().getRefCatastral2());
		
		this.getJTextFieldEscalaCaptura().setText(this.asc.getFechaCaptura());
		this.getJTextFieldFechaCaptura().setText(this.asc.getFechaCaptura());
		
		((TableLinderosModel)((TableSorted)this.getJTableLinderos().getModel()).getTableModel()).setData(new ArrayList());
	}

	private JPanel getButtonsPanel() {
		// TODO Auto-generated method stub
		
		// panel Aparte para los botones...
		JPanel jPanel1 = new JPanel(new GridBagLayout());
		jPanel1.setSize(new Dimension(200, 50));
		jPanel1.setPreferredSize(new Dimension(200, 50));
		jPanel1.setMaximumSize(jPanel1.getPreferredSize());
		jPanel1.setMinimumSize(jPanel1.getPreferredSize());
		
		
		jPanel1.add(new JLabel(),
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 600, 0));
		
		jPanel1.add(getAcceptButton(),
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		jPanel1.add(new JLabel(),
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		jPanel1.add(getCancelButton(),
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		return jPanel1;
		
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
	
	private JPanel getJPanelIdentificacion() {
		if (jPanelIdentificacion == null) {

			jPanelIdentificacion = new JPanel(new GridBagLayout());
			jPanelIdentificacion.setSize(new Dimension(400, 250));
			jPanelIdentificacion.setPreferredSize(new Dimension(400, 250));
			jPanelIdentificacion.setMaximumSize(jPanelIdentificacion
					.getPreferredSize());
			jPanelIdentificacion.setMinimumSize(jPanelIdentificacion
					.getPreferredSize());

			jPanelIdentificacion.setBorder(BorderFactory.createTitledBorder(
					null, I18N.get("Expedientes",
					"asc.panel.datosidentificacion.titulo"),
					TitledBorder.LEADING, TitledBorder.TOP, new Font(null,
							Font.BOLD, 12)));

			// Panel expediente

			// primera linea con jPanel1			
			JPanel jPanel1 = getPrimeraLineaPanelExpediente();

			jPanelIdentificacion.add(jPanel1,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
					GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0))
					;
			// Fin primera linea

			// Segunda linea con jpanel 2
			JPanel jPanel2 = getSegundaLineaPanelExpediente();

			jPanelIdentificacion.add(jPanel2,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
					GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			// fin de la seguda linea
					
			// Terecera linea con jpanel 3
			JPanel jPanel3 = getTerceraLineaPanelExpediente();
			
			jPanelIdentificacion.add(jPanel3,
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
					GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			// fin de la tercera linea
			
			// cuarta linea (lista de linderos)
			JPanel jPanel4 = getCuartaLineaPanelExpediente();
		
			jPanelIdentificacion.add(jPanel4,
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
					GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 100));			
			// fin de la cuarta linea (lista de linderos)
			
			
			
			// quinta linea 
			JPanel jPanel5 = getQuintaLineaPanelExpediente();

			jPanelIdentificacion.add(jPanel5,
					new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
					GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			// fin de la quinta linea

			////////////////
			// FIN PANEL EXPEDIENTE
			/////////

		}
		return jPanelIdentificacion;
	}

	/**
	 * @param jPanel1
	 * @return
	 */
	private JPanel getCuartaLineaPanelExpediente() {
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setSize(new Dimension(400, 60));
		panel.setPreferredSize(new Dimension(400, 60));
		panel.setMaximumSize(panel.getPreferredSize());
		panel.setMinimumSize(panel.getPreferredSize());
		panel.setBorder(BorderFactory.createTitledBorder(I18N
				.get("Expedientes",
				"asc.panel.datosidentificacion.listalinderos")));
		
		
		JScrollPane jPanel4 = new JScrollPane();
		jPanel4.setSize(new Dimension(250,27));
		jPanel4.setPreferredSize(new Dimension(250, 27));
		jPanel4.setMaximumSize(jPanel4.getPreferredSize());
		jPanel4.setMinimumSize(jPanel4.getPreferredSize());
		jPanel4.setViewportView(getJTableLinderos());
		
		panel.add(jPanel4,
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 44));
		
		JPanel jPanel1 = new JPanel(new GridBagLayout());
		jPanel1.setSize(new Dimension(400, 25));
		jPanel1.setPreferredSize(new Dimension(400, 25));
		jPanel1.setMaximumSize(jPanel1.getPreferredSize());
		jPanel1.setMinimumSize(jPanel1.getPreferredSize());
		
		// PARA AJUSTAR LOS BOTONES ANNIADIR, BORRAR Y MODIFICAR A LA IZQUIERDA
		jPanel1.add(new JLabel(),
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
						GridBagConstraints.LAST_LINE_END,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 400, 0));
		
		jPanel1.add(getAnniadirLinderoButton(),
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
						GridBagConstraints.LAST_LINE_END,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		jPanel1.add(getEliminarLinderoButton(),
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.LAST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		jPanel1.add(getModificarLinderoButton(),
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.LAST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		
		panel.add(jPanel1,
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
						GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		((TableLinderosModel)((TableSorted)jTableLinderos.getModel()).getTableModel()).inicializarLinderos();
		return panel;
	}
	
	private JTable getJTableLinderos() {
		if (jTableLinderos == null) {

			jTableLinderos = new JTable();
			tablelinderosmodel = new TableLinderosModel();
			
			jTableLinderos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableLinderos.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent e) {
							ListSelectionModel l_lsm = (ListSelectionModel)e.getSource();
							if (!l_lsm.isSelectionEmpty()) {
								getModificarLinderoButton().setEnabled(true);
								getEliminarLinderoButton().setEnabled(true);
							} else {
								getModificarLinderoButton().setEnabled(false);
								getEliminarLinderoButton().setEnabled(false);
							}
						}
					}
			);
			
			TableSorted tblSorted = new TableSorted(tablelinderosmodel);
			jTableLinderos.getTableHeader().setReorderingAllowed(false);
			tblSorted.setTableHeader(jTableLinderos.getTableHeader());
			jTableLinderos.setModel(tblSorted);
			jTableLinderos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableLinderos.setCellSelectionEnabled(false);
			jTableLinderos.setColumnSelectionAllowed(false);
			jTableLinderos.setRowSelectionAllowed(true);
			

			((TableLinderosModel) ((TableSorted) jTableLinderos.getModel())
					.getTableModel()).addTableModelListener(jTableLinderos);
			

			jTableLinderos.getTableHeader().setReorderingAllowed(false);
			
			// poner propiedades de las celdas (centrado, negrita... )
			DefaultTableCellRenderer tcr1 = new DefaultTableCellRenderer();
			tcr1.setHorizontalAlignment(SwingConstants.LEFT);
			jTableLinderos.getColumnModel().getColumn(1).setCellRenderer(tcr1);
			
			DefaultTableCellRenderer tcr4 = new DefaultTableCellRenderer();
			tcr4.setHorizontalAlignment(SwingConstants.LEFT);
			tcr4.setSize(20, 10);
			jTableLinderos.getColumnModel().getColumn(4).setCellRenderer(tcr4);
			
		}
		return jTableLinderos;
	}

	/**
	 * @param jPanel1
	 * @return
	 */
	private JPanel getQuintaLineaPanelExpediente() {
		JPanel jPanel5 = new JPanel(new GridBagLayout());
		jPanel5.setSize(new Dimension(400, 50));
		jPanel5.setPreferredSize(new Dimension(400, 50));
		jPanel5.setMaximumSize(jPanel5.getPreferredSize());
		jPanel5.setMinimumSize(jPanel5.getPreferredSize());
		
		jLabelEscalaCaptura = new JLabel();
		jLabelEscalaCaptura.setText("Denominador de la Escala de captura: ");
		jPanel5.add(jLabelEscalaCaptura,
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel5.add(getJTextFieldEscalaCaptura(),
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 80, 0));
		

		
		jLabelFechaCaptura = new JLabel();
		jLabelFechaCaptura.setText("Fecha de captura: ");
		jPanel5.add(jLabelFechaCaptura,
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel5.add(getJTextFieldFechaCaptura(),
				new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 80, 0));
		
		jPanel5.add(new JLabel(),
				new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 250, 0));
		return jPanel5;
	}

	/**
	 * @param jPanel1
	 * @return
	 */
	private JPanel getTerceraLineaPanelExpediente() {
		
		JPanel jPanel3 = new JPanel(new GridBagLayout());
		jPanel3.setSize(new Dimension(400, 50));
		jPanel3.setPreferredSize(new Dimension(400, 50));
		jPanel3.setMaximumSize(jPanel3.getPreferredSize());
		jPanel3.setMinimumSize(jPanel3.getPreferredSize());
		
		jLabelLetraDuplicado = new JLabel();
		jLabelLetraDuplicado.setText("Letra duplicado: ");
		jPanel3.add(jLabelLetraDuplicado,
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel3.add(getJTextFieldLetraDuplicado(),
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 0));
		
		// numero policia
		jLabelNumPolicia = new JLabel();
		jLabelNumPolicia.setText("Num. Policía: ");
		jPanel3.add(jLabelNumPolicia,
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel3.add(getJTextFieldNumPolicia(),
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 35, 0));
		
		
		
		
		jLabelParcela1 = new JLabel();
		jLabelParcela1.setText("Parcela Catastral 1: ");
		jPanel3.add(jLabelParcela1,
				new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel3.add(getJTextFieldParcelaCatastral1(),
				new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 80, 0));
		
		jLabelParcelaCatastral2 = new JLabel();
		jLabelParcelaCatastral2.setText("Parcela Catastral 2: ");
		jPanel3.add(jLabelParcelaCatastral2,
				new GridBagConstraints(6, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel3.add(getJTextFieldParcelaCatastral2(),
				new GridBagConstraints(7, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 80, 0));
		

		// para ajustar textos izquierda
		jPanel3.add(new JLabel(),
				new GridBagConstraints(8, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 0));
		return jPanel3;
	}

	/**
	 * @param jPanel1
	 * @return
	 */
	private JPanel getSegundaLineaPanelExpediente() {
		
		JPanel jPanel2 = new JPanel(new GridBagLayout());
		jPanel2.setSize(new Dimension(400, 50));
		jPanel2.setPreferredSize(new Dimension(400, 50));
		jPanel2.setMaximumSize(jPanel2.getPreferredSize());
		jPanel2.setMinimumSize(jPanel2.getPreferredSize());

		// Código de la vía.
		jLabelCodViaPublica = new JLabel();
		jLabelCodViaPublica.setText("Código via DGC: ");
		jPanel2.add(jLabelCodViaPublica,
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel2.add(getJTextFieldCodVia(),
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 50, 0));


		// siglas de la via
		jLabelSiglas = new JLabel();
		jLabelSiglas.setText("Siglas: ");
		jPanel2.add(jLabelSiglas,
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel2.add(getComboBoxdSiglas(),
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 3));


		// Nombre de la vía
		jLabelNombreVia = new JLabel();
		jLabelNombreVia.setText("Nombre vía:");
		jPanel2.add(jLabelNombreVia,
				new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel2.add(getJTextFieldNombreVia(),
				new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 160, 0));
		jPanel2.add(getBuscarViaButton(),
				new GridBagConstraints(6, 0, 1, 1, 0.1, 0.1,
						GridBagConstraints.FIRST_LINE_START,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		

		
	
//		// para ajustar textos izquierda
		jPanel2.add(new JLabel(),
				new GridBagConstraints(11, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 70, 0));
		return jPanel2;
	}

	/**
	 * @return
	 */
	private JPanel getPrimeraLineaPanelExpediente() {
		

		JPanel jPanel1 = new JPanel(new GridBagLayout());
		jPanel1.setSize(new Dimension(400, 50));
		jPanel1.setPreferredSize(new Dimension(400, 50));
		jPanel1.setMaximumSize(jPanel1.getPreferredSize());
		jPanel1.setMinimumSize(jPanel1.getPreferredSize());

		//codigo delegación
		jLabelCodDelegacion = new JLabel();
		jLabelCodDelegacion.setText("Código delegación: ");
		jLabelCodDelegacion.setHorizontalAlignment(JLabel.CENTER);
		jPanel1.add(jLabelCodDelegacion,
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0,0), 0, 6));
		jPanel1.add(getJTextFieldCodDelegacion(),
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 0));

		//nombre gerencia
		jLabelNombreGerencia = new JLabel();
		jLabelNombreGerencia.setText("Nombre de la Gerencia: ");
		jPanel1.add(jLabelNombreGerencia,
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel1.add(getJTextFieldNombreGerencia(),
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 120, 0));

		// codigo municipio
		jLabelCodMunicipio = new JLabel();
		jLabelCodMunicipio.setText("Código municipio: ");
		jPanel1.add(jLabelCodMunicipio,
				new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));
		jPanel1.add(getJTextFieldCodMunicipio(),
				new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 0));

		// nombre municipio
		jLabelNombreMunicipio = new JLabel();
		jLabelNombreMunicipio.setText("Nombre del municipio: ");
		jPanel1.add(jLabelNombreMunicipio,
				new GridBagConstraints(6, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 6));

		jPanel1.add(getJTextFieldNombreMunicipio(),
				new GridBagConstraints(7, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 120, 0));
		return jPanel1;
	}


	/**
	 * @return the buscarViaButton
	 */
	public JButton getBuscarViaButton() {
		if(buscarViaButton == null){
			buscarViaButton = new JButton();
			buscarViaButton.setIcon(IconLoader.icon(GestionExpedientePanel.ICONO_BUSCAR));

			buscarViaButton.setMaximumSize(new Dimension(20,20));
			buscarViaButton.setSize(new Dimension(20,20));

			buscarViaButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
//					focoEn = "Parcela";
//					componenteConFoco = null;
//					focoAutomatico = true;
//					actualizaArbol(null);
					String tipoVia = "";
					if(siglasComboBox.getSelectedPatron()!=null){
						tipoVia = siglasComboBox.getSelectedPatron().toString();
					}
//					//ViasSistemaDialog dialog = new ViasSistemaDialog(jTextFieldVia.getText());
					ViasSistemaDialog dialog = new ViasSistemaDialog(nombreViaTextField.getText(),tipoVia);
					dialog.setVisible(true);

					nombreViaTextField.setText(dialog.getVia());
					siglasComboBox.setSelectedPatron(dialog.getTipoVia());
					codViaTextField.setText(String.valueOf(dialog.getCodigoVia()));
				}
			});

//			jButtonBuscarVia.setName("_buscarvia");
		}
		return buscarViaButton;
	}

	private JTextField getJTextFieldFechaCaptura() {
		// TODO Auto-generated method stub
		if (fechaCapturaTextField == null){
			fechaCapturaTextField = new JTextField();
			fechaCapturaTextField.setDocument(new LimitadorCaracteres(fechaCapturaTextField,8));
			fechaCapturaTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					fechaCapturaTextField.selectAll();
				}
			}
			);
		}
		return fechaCapturaTextField;
	}

	private JTextField getJTextFieldEscalaCaptura() {
		// TODO Auto-generated method stub
		if (escalaCapturaTextField == null){
			escalaCapturaTextField = new JTextField();
			escalaCapturaTextField.setDocument(new LimitadorCaracteres(escalaCapturaTextField,4));
			escalaCapturaTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					escalaCapturaTextField.selectAll();
				}
			}
			);
			
		}
		return escalaCapturaTextField;
	}

	private JTextField getJTextFieldParcelaCatastral1() {
		// TODO Auto-generated method stub
		if (parcelaCatastral1TextField == null){
			parcelaCatastral1TextField = new JTextField();
			parcelaCatastral1TextField.setEditable(false);
			parcelaCatastral1TextField.setDocument(new LimitadorCaracteres(parcelaCatastral1TextField,7));
			parcelaCatastral1TextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					parcelaCatastral1TextField.selectAll();
				}
			}
			);
		}
		return parcelaCatastral1TextField;
	}
	
	private JTextField getJTextFieldParcelaCatastral2() {
		// TODO Auto-generated method stub
		if (parcelaCatastral2TextField == null){
			parcelaCatastral2TextField = new JTextField();
			parcelaCatastral2TextField.setEditable(false);
			parcelaCatastral2TextField.setDocument(new LimitadorCaracteres(parcelaCatastral2TextField,7));
			parcelaCatastral2TextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					parcelaCatastral2TextField.selectAll();
				}
			}
			);
		}
		return parcelaCatastral2TextField;
	}

	private JTextField getJTextFieldLetraDuplicado() {
		// TODO Auto-generated method stub
		if (letraDuplicadoTextField == null){
			letraDuplicadoTextField = new JTextField();
			letraDuplicadoTextField.setDocument(new LimitadorCaracteres(letraDuplicadoTextField,1));
			letraDuplicadoTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					letraDuplicadoTextField.selectAll();
				}
			}
			);
		}
		return letraDuplicadoTextField;
	}

	private JTextField getJTextFieldNumPolicia() {
		// TODO Auto-generated method stub
		if (numPoliciaTextField == null){
			numPoliciaTextField = new JTextField();
			numPoliciaTextField.setDocument(new LimitadorCaracteres(numPoliciaTextField,4));
			numPoliciaTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					numPoliciaTextField.selectAll();
				}
			}
			);
		}
		return numPoliciaTextField;
	}

	private JTextField getJTextFieldNombreVia() {
		// TODO Auto-generated method stub
		if (nombreViaTextField == null){
			nombreViaTextField = new JTextField();
			nombreViaTextField.setDocument(new LimitadorCaracteres(nombreViaTextField,25));
			nombreViaTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					nombreViaTextField.selectAll();
				}
			}
			);
		}
		return nombreViaTextField;
	}

	private ComboBoxEstructuras getComboBoxdSiglas() {
		// TODO Auto-generated method stub
		if (siglasComboBox == null){
        	Estructuras.cargarEstructura("Tipo de vía");
            siglasComboBox = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
		}
		return siglasComboBox;
	}

	private JTextField getJTextFieldCodVia() {
		// TODO Auto-genTextFieldethod stub
		if (codViaTextField == null){
			codViaTextField = new JTextField();
			codViaTextField.setDocument(new LimitadorCaracteres(codViaTextField,5));
			codViaTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					codViaTextField.selectAll();
				}
			}
			);
		}
		return codViaTextField;
	}

	private JTextField getJTextFieldNombreMunicipio() {
		if (nombreMunicipioTextField == null){
			nombreMunicipioTextField = new JTextField();
			nombreMunicipioTextField.setDocument(new LimitadorCaracteres(nombreMunicipioTextField,30));
			nombreMunicipioTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					nombreMunicipioTextField.selectAll();
				}
			}
			);
		}
		return nombreMunicipioTextField;
	}

	private JTextField getJTextFieldCodMunicipio() {
		// TODO Auto-generated method stub
		if (codMunicipioTextField == null){
			codMunicipioTextField = new JTextField();
			codMunicipioTextField.setDocument(new LimitadorCaracteres(codMunicipioTextField,3));
			codMunicipioTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					codMunicipioTextField.selectAll();
				}
			}
			);
		}
		return codMunicipioTextField;
	}

	private JTextField getJTextFieldNombreGerencia() {
		// TODO Auto-generated method stub
		if (nombreGerenciaTextField == null) {
			nombreGerenciaTextField = new JTextField();
			nombreGerenciaTextField.setDocument(new LimitadorCaracteres(nombreGerenciaTextField,20));
			nombreGerenciaTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					nombreGerenciaTextField.selectAll();
				}
			}
			);
		}
		return nombreGerenciaTextField;
	}

	private JTextField getJTextFieldCodDelegacion() {
		if (codDelegacionTextField == null) {
			codDelegacionTextField = new JTextField();
			codDelegacionTextField.setHorizontalAlignment((int) TextField.CENTER_ALIGNMENT);
			codDelegacionTextField.setDocument(new LimitadorCaracteres(codDelegacionTextField,2));
			codDelegacionTextField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					codDelegacionTextField.selectAll();
				}
			}
			);
		}
		return codDelegacionTextField;
	}

	public JButton getModificarLinderoButton() {
		if(modificarLinderoButton == null){
			modificarLinderoButton = new JButton("Modificar Lindero");
			modificarLinderoButton.setEnabled(false);
			
			modificarLinderoButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					lanzarModificarLinderoDialog();
				}
			}
			);
			
		}
		return modificarLinderoButton;
	}
	
	public JButton getEliminarLinderoButton() {
		if( eliminarLinderoButton== null){
			eliminarLinderoButton = new JButton("Borrar Lindero");
			eliminarLinderoButton.setEnabled(false);

			eliminarLinderoButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					lanzarBorrarLinderoDialog();
				}
			}
			);
		}
		return eliminarLinderoButton;
	}
	
	
	public JButton getAnniadirLinderoButton() {
		if( anniadirLinderoButton == null){
			anniadirLinderoButton = new JButton("Añadir Lindero");
			anniadirLinderoButton.setEnabled(true);
			
			anniadirLinderoButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					lanzarAnninadirLinderoDialog();
				}
			}
			);
			
		}
		return anniadirLinderoButton;
	}
	
	private boolean lanzarBorrarLinderoDialog() {
		// TODO Auto-generated method stub
		if (confirmarEliminar()){
			((TableLinderosModel)((TableSorted)jTableLinderos.getModel()).getTableModel()).borrarFila(jTableLinderos.getSelectedRow());
		}
		return false;
		
	}
	
	private boolean confirmarEliminar() {
		// TODO Auto-generated method stub
		
		LinderoCatastro lc = ((TableLinderosModel)((TableSorted)jTableLinderos.getModel()).getTableModel()).getValueAt(jTableLinderos.getSelectedRow()); 
		
		// mensaje del dialogo.
		String mensaje = "Intenta eliminar el lindero ";
		if(lc.getTipoLindero().equals(LinderoCatastro.IZ)){
			mensaje = mensaje + "'IZQUIERDO'. ";
		} else if(lc.getTipoLindero().equals(LinderoCatastro.DR)){
			mensaje = mensaje + "'DERECHO'. ";
		} else if(lc.getTipoLindero().equals(LinderoCatastro.FD)){
			mensaje = mensaje + "'FONDO'. ";
		}
		mensaje = mensaje + "¿Desea realmente eliminarlo?";
		
		int seleccion = JOptionPane.showOptionDialog(
				   this,
				   mensaje, 
				   "Seleccciona 1 opción",
				   JOptionPane.YES_NO_OPTION,
				   JOptionPane.QUESTION_MESSAGE,
				   null,    // null para icono por defecto.
				   new Object[] { "Aceptar", "cancelar"},   // null para YES, NO y CANCEL
				   "cancelar");

		if (seleccion == 0)
			return true;

		return false;
	}
	
	public boolean lanzarModificarLinderoDialog(){
		try{
		// Comprobar que hay un lindero de la lista elegido
		TableLinderosModel model =  (TableLinderosModel)((TableSorted)jTableLinderos.getModel()).getTableModel();
		if (getJTableLinderos().getSelectedRow() < 0){
			JOptionPane.showMessageDialog(this, "Seleccione un lindero de la lista.");
			return false;
		}
		
		// Cogemos el lindero seleccionado de la lista.
		LinderoCatastro lindero = model.getValueAt(getJTableLinderos().getSelectedRow());
		LinderoEditDialog ascEdit = new LinderoEditDialog(this, lindero);
		return true;
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "Seleccione un lindero de la lista.");
			return false;
		}
	}
	
	public boolean lanzarAnninadirLinderoDialog(){
		LinderoEditDialog ascEdit = new LinderoEditDialog(this, null);
		return true;
	}

	public boolean modificarLindero(LinderoCatastro linderoCatastro) {
		// TODO Auto-generated method stub
		return ((TableLinderosModel)((TableSorted)this.jTableLinderos.getModel()).getTableModel()).updateTableLindero(linderoCatastro);
	}
	
	public boolean existeLindero (LinderoCatastro linderoCatastro){
		if (((TableLinderosModel)((TableSorted)this.jTableLinderos.getModel()).getTableModel()).getData().isEmpty()){
			return false;
		}
		if( ((TableLinderosModel)((TableSorted)this.jTableLinderos.getModel()).getTableModel()).buscarLinderoPorTipo(linderoCatastro.getTipoLindero()) == null ){
			return false;
		} else {
			return true;
		}
	}
	
	public boolean existeLinderoTipo (String tipoLinderoCatastro){
		if (((TableLinderosModel)((TableSorted)this.jTableLinderos.getModel()).getTableModel()).getData().isEmpty()){
			return false;
		}
		if( ((TableLinderosModel)((TableSorted)this.jTableLinderos.getModel()).getTableModel()).buscarLinderoPorTipo(tipoLinderoCatastro) == null ){
			return false;
		} else {
			return true;
		}
	}



	private boolean onCancelButtonDo(){
		this.getParent().setEnabled(true);
		this.dispose();
		((TableLinderosModel)((TableSorted)this.getJTableLinderos().getModel()).getTableModel()).borrarTodasFilas();
		return true;
	}
	
	private boolean onAcceptButtonDo(){
		
		if (!comprobarCampos()){
			return false;
		}
				
		// La comprobacion de datos del panel de edicion asc ha sido correcta
		// Cargamos los datos en el panel ASC.
		this.cargarDatosEnAscPanel();
		ASCPanel.get_instance().setAsc(this.asc);

		///
		
		this.getParent().setEnabled(true);
		this.dispose();
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
		//progressDialog.setTitle(I18N.get("Expedientes","fxcc.panel.CargandoFicheroDXF"));
		progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.report(I18N.get("Expedientes","fxcc.panel.CargandoPlantillaDXF"));
		progressDialog.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{   
				new Thread(new Runnable()
				{
					public void run()
					{
						try
						{   
							//InputStream resultado = new FileInputStream("classes" + File.separator + "DXFCATASTRO.txt");
//							InputStream resultado = FileLoader.getFile("DXFCATASTRO.txt");
							InputStream resultado = FileLoader.getFile("PG_DXF.txt");
							if(resultado!=null){
								ImportarUtils_LCGIII operations = new ImportarUtils_LCGIII();
								final String file = operations.parseISToString(resultado); 
								final PlugInContext context = new PlugInContext(new GeopistaContext(fxccPanel.getGeopistaEditor()),null,null,null,null);

								try {
//									ArrayList namesLayerFamilies = new ArrayList();
//									namesLayerFamilies.add("parcelario");
//									namesLayerFamilies.add("construcciones");
//									namesLayerFamilies.add("cultivos");
//									ImportarUtils.cargarCapas(namesLayerFamilies, geopistaEditor);
									fxccPanel.loadLayers();
									fxccPanel.loadDXF(progressDialog,context,file);
									fxccPanel.getJButtonNuevoDXF().setEnabled(false);

								} catch (Exception e) {

									e.printStackTrace();
								}        
							}

						} 
						catch (Exception e)
						{
							e.printStackTrace();
						} 
						finally
						{
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);
		return true;
	}

	private boolean cargarDatosEnAscPanel() {
		// TODO Auto-generated method stub
		this.asc.setCodDelegacion(this.getJTextFieldCodDelegacion().getText());
		this.asc.setNombreGerencia(this.getJTextFieldNombreGerencia().getText());
		
		this.asc.setCodMunicipio(this.getJTextFieldCodMunicipio().getText());
		this.asc.setNombreMunicipio(this.getJTextFieldNombreMunicipio().getText());
		
		this.asc.setCodVia(this.getJTextFieldCodVia().getText());
		this.asc.setSigla(this.getComboBoxdSiglas().getSelectedPatron());
		this.asc.setNomVia(this.getJTextFieldNombreVia().getText());
		this.asc.setNumPolicia(this.getJTextFieldNumPolicia().getText());
		this.asc.setLetraDuplicado(this.getJTextFieldLetraDuplicado().getText());
		
		this.asc.setEscalaCaptura(this.getJTextFieldEscalaCaptura().getText());
		this.asc.setFechaCaptura(this.getJTextFieldFechaCaptura().getText());
		
		this.asc.setLstLinderos(((TableLinderosModel)((TableSorted)this.getJTableLinderos().getModel()).getTableModel()).getData());
		
		ASCPanel.get_instance().setAsc(this.asc);
		ASCPanel.get_instance().loadData(this.asc);
		return true;
		
	}

	private boolean comprobarCampos() {
		// TODO Auto-generated method stub
		
		// Comprobar el código de la delegación.
		try{
			if ( Integer.parseInt(this.codDelegacionTextField.getText()) < 0  || Integer.parseInt(this.codDelegacionTextField.getText()) > 99 ){
				JOptionPane.showMessageDialog(this, "Código de delegación '" + codDelegacionTextField.getText()  + "' erroneo. Debería ser un valor numérico entre 0 y 99.");
				return false;
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "Código de delegación '" + codDelegacionTextField.getText()  + "' erroneo. Debería ser un valor numérico entre 0 y 99999.");
			return false;
		}
		
		
		// Comprobar el nombre de la gerencia.
		if ( this.nombreGerenciaTextField.getText().equals("")){
			JOptionPane.showMessageDialog(this, "Introduzca el nombre de la gerencia.");
			return false;
		}
		
		// Comprobar el codigo del municipio.
		try{
			if ( Integer.parseInt(this.codMunicipioTextField.getText()) < 0  || Integer.parseInt(this.codMunicipioTextField.getText()) > 9999 ){
				JOptionPane.showMessageDialog(this, "Codigo del municipio '" + codMunicipioTextField.getText()  + "' erróneo. Debería ser un valor numérico entre 0 y 9999.");
				return false;
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "Codigo del municipio '" + codMunicipioTextField.getText()  + "' erróneo. Debería ser un valor numérico entre 0 y 9999.");
			return false;
		}
		
		// Comprobar el nombre del municipio.
		if( this.nombreMunicipioTextField.getText().equals("")){
			JOptionPane.showMessageDialog(this, "Introduzca el nombre del municipio.");
			return false;
		}
		
		
		// Comprobar las referencias catastrales
		if (this.parcelaCatastral1TextField.equals("") || this.parcelaCatastral2TextField.equals("")){
			JOptionPane.showMessageDialog(this, "Las referencias catastrales no son correctas. Compruebelo o introduzca los datos de los linderos.");
			parcelaCatastral1TextField.setEditable(true);
			parcelaCatastral2TextField.setEditable(true);
			return false;
		}
		
		//Comprobar la escala de la captura.
		try{
			if ( Integer.parseInt(this.escalaCapturaTextField.getText()) < 0  || Integer.parseInt(this.escalaCapturaTextField.getText()) > 9999 ){
				JOptionPane.showMessageDialog(this, "Escala captura '" + escalaCapturaTextField.getText()  + "' erróneo. Debería ser un valor numérico mayor que cero.");
				return false;
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "Escala de captura '" + escalaCapturaTextField.getText()  + "' erróneo. Debería ser un valor numérico.");
			return false;
		}
		
		// Compobar la fecha de la captura.
		if ( this.fechaCapturaTextField.getText().equals("")){
			JOptionPane.showMessageDialog(this, "Introduzca la fecha de la captura.");
			return false;
		}
		
		return true;
	}
	
	
	
}
