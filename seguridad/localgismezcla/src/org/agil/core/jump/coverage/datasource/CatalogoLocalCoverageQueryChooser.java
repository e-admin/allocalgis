package org.agil.core.jump.coverage.datasource;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import com.vividsolutions.jump.workbench.datasource.DataSourceQueryChooser;
import org.agil.core.coverage.Coverage;

/**
 *  Clase que permite mostrar un componente para la seleccion de un origen de
 *  datos (el componente muestra los datos contenidos en un catalogo) e
 *  incorporarlos al visor en forma de Layer. TODO Ahora mismo recibe un path
 *  XML de un catalogo Local. En realidad recibira una implementacion de
 *  CatalogoCoverageIF, obtenida mediante un componente selector de catalogos.
 *
 *@author    alvaro zabala 29-sep-2003
 */
public class CatalogoLocalCoverageQueryChooser implements DataSourceQueryChooser {
	/**
	 *  Catalogo que contiene los datos que se le van a dar a seleccionar al
	 *  usuario.
	 */
	private CatalogoCoverageIF catalogoCoverage;
	/**
	 *  Componente que nos permite seleccionar un origen de datos
	 */
	private CatalogoCoverageBrowser catalogoBrowser;


	/**
	 *  Constructor. Recibe la ruta del documento XML que contiene los origenes
	 *  raster, y la ventana padre del dialogo modal.
	 *
	 *@param  pathCatalogoLocal
	 *@param  dialogOwner
	 */
	public CatalogoLocalCoverageQueryChooser(String pathCatalogoLocal, Frame dialogOwner) {
		catalogoCoverage = new CatalogoCoverageLocal(pathCatalogoLocal);
		catalogoBrowser = new CatalogoCoverageBrowser(dialogOwner, true);
		catalogoBrowser.setCatalogo(catalogoCoverage);
	}


	/**
	 *  Devuelve el componente que permite seleccionar origenes de datos (no esta
	 *  visible. Su estado de visibilidad se debe controlar desde los plug-ins
	 *  que hagan uso de el)
	 *
	 *@return    The Component value
	 *@see       com.vividsolutions.jump.workbench.datasource.DataSourceQueryChooser#getComponent()
	 */
	public Component getComponent() {
		return catalogoBrowser;
	}


	/**
	 *  Devuelve una coleccion con todos los origenes de datos seleccionados
	 *  (como ahora mismo solo se puede seleccionar uno, pues solo tiene uno)
	 *
	 *@return    coleccion con los origenes seleccionados
	 *@see       com.vividsolutions.jump.workbench.datasource.DataSourceQueryChooser#getDataSourceQueries()
	 */
	public Collection getDataSourceQueries() {
		ArrayList solucion = new ArrayList();
		Object seleccion = catalogoBrowser.getSeleccion();
		//es una instancia de CoverageMetaData
		Coverage coverage = catalogoCoverage.getCoverage(seleccion.toString());
		solucion.add(coverage);
		return solucion;
	}


	/*
	 *  (non-Javadoc)
	 *  @see com.vividsolutions.jump.workbench.datasource.DataSourceQueryChooser#isInputValid()
	 */
	/**
	 *  Gets the InputValid attribute of the CatalogoLocalCoverageQueryChooser
	 *  object
	 *
	 *@return    The InputValid value
	 */
	public boolean isInputValid() {
		return true;
	}


	/**
	 *  Indica si el usuario pulso el boton aceptar, o por el contrario cerro la
	 *  ventana o pulso cancelar
	 *
	 *@return    booleano que indica si se pulso ok
	 */
	public boolean wasOkPressed() {
		return catalogoBrowser.getSeleccion() != null;
	}

}

/**
 *  Dialogo que nos permite seleccionar un origen de datos de los contenidos
 *  en un catalogo TODO El componente es muy parecido al selector para
 *  Catalogos de FeatureCollection. Por esta razon, si hacemos que ambos
 *  catalogos hereden de una clase comun, igual el componente puede ser el
 *  mismo (utilizado desde QueryChoosers distintos, cargando XMLs distintos)
 *
 *@author    Chris Seguin
 */
class CatalogoCoverageBrowser extends javax.swing.JDialog {


	/**
	 *  * CatalogoLocal cuyas entradas son mostradas por el componente
	 */
	private CatalogoCoverageIF catalogo;
	/**
	 *  Entrada seleccionada por el usuario de las mostradas
	 */
	private Object entradaSeleccionada;
	/*
	 *  Componentes del DIALOGO
	 */
	private javax.swing.JPanel jPanel1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JList jList1;
	private javax.swing.JLabel jLabel1;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();


	/**
	 *  Constructor
	 *
	 *@param  parent
	 *@param  modal
	 */
	public CatalogoCoverageBrowser(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}


	/**
	 *  Inicializa el componente para que muestre todas las entradas del catalogo
	 *
	 *@param  catalogo  The new Catalogo value
	 */
	public void setCatalogo(CatalogoCoverageIF catalogo) {
		this.catalogo = catalogo;
		initializeListModel();
	}


	/**
	 *  Devuelve la entrada seleccionada
	 *
	 *@return    The Seleccion value
	 */
	public Object getSeleccion() {
		return entradaSeleccionada;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  e  Description of Parameter
	 */
	void jList1_valueChanged(ListSelectionEvent e) {
		int donde = jList1.getSelectedIndex();
		jList1.ensureIndexIsVisible(donde);
	}


	/**
	 *  Rellena la lista a partir de las entradas del catalogo
	 */
	private void initializeListModel() {
		DefaultListModel m = new DefaultListModel();
		Iterator it = catalogo.getEntrys().iterator();
		while (it.hasNext()) {
			m.addElement(it.next());
		}
		//while
		jList1.setModel(m);
	}


	/**
	 *  Inicializa la parte visual del componente
	 */
	private void initComponents() {
		jLabel1 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jList1 = new javax.swing.JList();
		jPanel1 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();

		setTitle("Catalogo Coberturas");
		this.getContentPane().setLayout(gridBagLayout1);
		addWindowListener(
					new java.awt.event.WindowAdapter() {
						public void windowClosing(java.awt.event.WindowEvent evt) {
							closeDialog(evt);
						}
					});

		jLabel1.setText("Selecione una Cobertura...");
		jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList1.addListSelectionListener(
					new javax.swing.event.ListSelectionListener() {
						public void valueChanged(ListSelectionEvent e) {
							jList1_valueChanged(e);
						}
					});

		jPanel1.setLayout(gridBagLayout2);
		jPanel1.setMinimumSize(new Dimension(1, 1));
		jPanel1.setPreferredSize(new Dimension(1, 1));
		getContentPane().add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jScrollPane1.setViewportView(jList1);

		getContentPane().add(jScrollPane1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
				, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jButton1.setText("OK");
		jButton1.addActionListener(
					new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							okSelected(evt);
						}
					});

		jButton2.setText("Cancel");
		jButton2.addActionListener(
					new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							cancelSelected(evt);
						}
					});

		jPanel1.add(jButton2, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel1.add(jButton1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		getContentPane().add(jPanel1, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 25));

		setSize(300, 400);
	}


	/**
	 *  Procesa el evento de pulsar el boton cancel
	 *
	 *@param  evt  Description of Parameter
	 */
	private void cancelSelected(java.awt.event.ActionEvent evt) {
		System.out.println("operacion cancelada");
		entradaSeleccionada = null;
	}


	/**
	 *  Procesa el evento de pulsar el boton ok
	 *
	 *@param  evt  Description of Parameter
	 */
	private void okSelected(java.awt.event.ActionEvent evt) {
		entradaSeleccionada = jList1.getSelectedValue();
		setVisible(false);
	}


	/**
	 *  Cierra el dialogo
	 *
	 *@param  evt  evento de ventana
	 */
	private void closeDialog(java.awt.event.WindowEvent evt) {
		setVisible(false);
		entradaSeleccionada = jList1.getSelectedValue();
	}

}

