package com.geopista.app.inventario;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;

//import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.utilidades.EdicionUtils;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.Usuario;
import com.geopista.protocol.inventario.InventarioEIELBean;
import com.geopista.protocol.inventario.Inventario;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.ListaEIEL;

import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;


public class IntegracionEIELJDialog extends JDialog {
	/**
 * Logger for this class
 */
private static final Logger logger = Logger.getLogger(IntegracionEIELJDialog.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IntegracionEIELTableModel modelIntegracionEIEL;
	
	private TableSorted sorter;
	
	private InventarioClient inventarioClient = null;
	
	private javax.swing.JTable jTableIntegracionEIEL; 

	private javax.swing.JPanel jPanelListaEIEL;
	
	private InventarioEIELBean eielSelected = null;
	
	private ListaEIEL listaEIEL = null;
	
	private javax.swing.JButton jButtonAsociar;
	private javax.swing.JButton jButtonCancelar;
	
	private javax.swing.JScrollPane listaEIELJScrollPane;
	
	private ResourceBundle messages;

	private Municipio municipio;

	private ComboBoxEstructuras jComboBoxEpigInventario = null;
	private JComboBox jComboBoxNumInventario = null;
	private JLabel jLabelEpigInventario = null;
	private JLabel jLabelNumInventario = null;
	
	protected javax.swing.JPanel jPanelIntegracionEIEL;
	private AppContext aplicacion;


	

	

	/**
	 * Constructor de la clase
	 * 
	 * @param parent
	 *            ventana padre
	 * @param modal
	 *            indica si es modal o no
	 * @param messages
	 *            textos de la aplicación
	 */
	public IntegracionEIELJDialog(java.awt.Frame parent, boolean modal,
			ResourceBundle messages,Municipio municipio) {
		super(parent, modal);
		this.messages = messages;
		this.municipio = municipio;
		this.aplicacion = (AppContext) AppContext.getApplicationContext();
		inventarioClient = new InventarioClient(
				aplicacion
						.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
						+ Constantes.INVENTARIO_SERVLET_NAME);
				
		initComponents();
		actualizarModelo();
		renombrarComponentes();

	}

	
	private void inicializaListaEIEL() {
		listaEIEL = new ListaEIEL(); 
		try {
			listaEIEL.sethEIEL(inventarioClient.getDatosEIELSinAsociar());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


	/**
	 * Inicializa los componentes de la ventana
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		
		inicializaListaEIEL();
		
		jPanelListaEIEL = new javax.swing.JPanel();
		jButtonAsociar = new javax.swing.JButton();
		jButtonCancelar = new javax.swing.JButton();
		
		jPanelListaEIEL.setLayout(null);
		jTableIntegracionEIEL = new javax.swing.JTable();
		listaEIELJScrollPane = new javax.swing.JScrollPane();
		
		listaEIELJScrollPane.setBounds(new Rectangle(10, 20, 560, 200));
		
		jLabelEpigInventario  = new JLabel(); 
        jLabelEpigInventario.setBounds(new Rectangle(20, 250, 150, 20));
        
        jLabelNumInventario  = new JLabel(); 
        jLabelNumInventario.setBounds(new Rectangle(20, 280, 150, 20));
                           
		
		jButtonAsociar.setBounds(new Rectangle(200, 320, 85, 20));
		jButtonAsociar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				asociarActionPerformed();
			}
		});
		jButtonAsociar.setEnabled(false);

		jButtonCancelar.setBounds(new Rectangle(315, 320, 85, 20));
		jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelarActionPerformed();
			}
		});

		listaEIELJScrollPane.setViewportView(jTableIntegracionEIEL);

		jPanelListaEIEL.add(listaEIELJScrollPane, null);		
		jPanelListaEIEL.add(jButtonAsociar,	null);
		jPanelListaEIEL.add(jButtonCancelar, null);
		jPanelListaEIEL.add(jLabelEpigInventario, null);
		jPanelListaEIEL.add(jLabelNumInventario, null);		
        jPanelListaEIEL.add(getJComboBoxEpigInventario(), null);
        jPanelListaEIEL.add(getJComboBoxNumInventario(), null);

		getContentPane().setLayout(new java.awt.BorderLayout());
		getContentPane().add(jPanelListaEIEL, java.awt.BorderLayout.CENTER);

		pack();
		
		// Para seleccionar una fila
		ListSelectionModel rowSM = jTableIntegracionEIEL.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				seleccionarEIEL(e);
			}
		});
	}
	
	
	private void actualizarModelo() {
		modelIntegracionEIEL = new IntegracionEIELTableModel(messages.getLocale().toString());
		modelIntegracionEIEL.setModelData(listaEIEL);
		sorter = new TableSorted(modelIntegracionEIEL);
		sorter.setTableHeader(jTableIntegracionEIEL.getTableHeader());
		jTableIntegracionEIEL.setModel(sorter);
		TableColumn column = jTableIntegracionEIEL.getColumnModel().getColumn(
				IntegracionEIELTableModel.idIndex);
		column.setPreferredWidth(5);
		column = jTableIntegracionEIEL.getColumnModel().getColumn(
				IntegracionEIELTableModel.idNombre);
		column.setPreferredWidth(15);
		jTableIntegracionEIEL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
    private ComboBoxEstructuras getJComboBoxEpigInventario()
    { 
        if (jComboBoxEpigInventario == null)
        {
            Estructuras.cargarEstructura("Tipo de bien patrimonial EIEL");
            jComboBoxEpigInventario = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEpigInventario.setBounds(new Rectangle(200, 250, 300, 20));

            jComboBoxEpigInventario.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   
            		jComboBoxNumInventario.removeAllItems();
            		if (jComboBoxEpigInventario.getSelectedIndex()==0)
            		{            			
            			jComboBoxNumInventario.setEnabled(false);
            		}
            		else
            		{

        				jComboBoxNumInventario.setEnabled(true);
        				try {
							EdicionUtils.cargarLista(getJComboBoxNumInventario(), inventarioClient.getNumInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron())));
						} catch (NumberFormatException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
        			}
            		comprobarSeleccionados();
            	}
            });
        }
        return jComboBoxEpigInventario;        
    }    
    
    /* Rellenar con los Inventarios del epígrafe seleccionado:*/
    private JComboBox getJComboBoxNumInventario()
    {
        if (jComboBoxNumInventario == null)
        {
            jComboBoxNumInventario  = new JComboBox();
            jComboBoxNumInventario.setEnabled(false);
            jComboBoxNumInventario.setBounds(new Rectangle(200, 280, 300, 20));
        }
        return jComboBoxNumInventario;
    }
	
    private void renombrarComponentes(){
    	jPanelListaEIEL.setBorder(new javax.swing.border.TitledBorder(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.title", messages)));
    	jButtonAsociar.setText(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.asociar", messages));
    	jButtonCancelar.setText(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.cancelar", messages));
    	
    	jLabelEpigInventario.setText(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.label.epigInventario", messages));
    	jLabelNumInventario.setText(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.label.numInventario", messages));
    	
		TableColumn tableColumn = jTableIntegracionEIEL.getColumnModel().getColumn(0);
		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento1", messages));
		tableColumn = jTableIntegracionEIEL.getColumnModel().getColumn(1);
		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento2", messages));
		tableColumn = jTableIntegracionEIEL.getColumnModel().getColumn(2);
		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento3", messages));
		tableColumn = jTableIntegracionEIEL.getColumnModel().getColumn(3);
		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento4", messages));
		tableColumn = jTableIntegracionEIEL.getColumnModel().getColumn(4);
		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento5", messages));		
    }
	
	private void asociarActionPerformed() {
		
		eielSelected.setEpigInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron()));
		eielSelected.setIdBien(((Inventario) jComboBoxNumInventario.getSelectedItem()).getId());
		try {
			inventarioClient.returnInsertIntegEIELInventario(eielSelected);
			jComboBoxEpigInventario.setSelectedIndex(0);
			inicializaListaEIEL();
			actualizarModelo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Función que se ejecuta al salir
	 */
	protected void cancelarActionPerformed() {
		dispose();
	}
	
	private void seleccionarEIEL(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
			eielSelected = null;
		} 
		else {
			int selectedRow = lsm.getMinSelectionIndex();
			String unionClaveEIEL = (String) sorter.getValueAt(selectedRow, IntegracionEIELTableModel.idIndex);
			
			eielSelected = listaEIEL.get(unionClaveEIEL);
		}
		comprobarSeleccionados();
	}
	
	
private void comprobarSeleccionados() {

		if ((eielSelected != null) && (jComboBoxEpigInventario.getSelectedIndex()!=0) && (((Inventario) jComboBoxNumInventario.getSelectedItem()) != null)){
			jButtonAsociar.setEnabled(true);
		}
		else{
			jButtonAsociar.setEnabled(false);
		}
		
	}

/**
	 * Devuelve un String del resourceBundle con parametros
	 * 
	 * @param key
	 * @param valores
	 * @return
	 */
	protected String getStringWithParameters(ResourceBundle messages,
			String key, Object[] valores) {
		try {
			MessageFormat messageForm = new MessageFormat("");
			messageForm.setLocale(messages.getLocale());
			String pattern = messages.getString(key);
			messageForm.applyPattern(pattern);
			return messageForm.format(valores, new StringBuffer(), null)
					.toString();
		} catch (Exception ex) {
			logger.error("Excepción al recoger el recurso:" + key, ex);
			return "undefined";
		}
	}
	

}
