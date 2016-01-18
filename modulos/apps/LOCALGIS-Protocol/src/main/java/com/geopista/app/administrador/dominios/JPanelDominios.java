/**
 * JPanelDominios.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.dominios;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.administrador.usuarios.RolesTableModel;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.dominios.Category;
import com.geopista.protocol.administrador.dominios.Domain;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.administrador.dominios.ListaCategories;
import com.geopista.protocol.administrador.dominios.ListaDomain;
import com.geopista.protocol.administrador.dominios.ListaDomainNode;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 29-sep-2004
 * Time: 19:11:38
 */

public class JPanelDominios extends JPanel implements TreeSelectionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(JPanelDominios.class);
	private ResourceBundle messages;
	public static TreeModel model;
	private ListaDomain listaDomain;
	private ListaDomain listaDomainParticular;
	private ListaCategories listaCategorias;
	private DefaultMutableTreeNode nodeArbolSelected=null;
	private boolean nodoParticular=false;
	private DomainNode auxDomainNode=null;
	private DomainNode auxDomainCodeEntry=null;
	private boolean modoNuevo=false;
	private boolean modoEdicion=false;
	private CodeBookTableModel  modelCodeBook;
	private TableSorted sorter;
	private String idCodeEntrySelected=null;
	private static final String AUTOAREA= "AREA";
	private static final String AUTOLENGHT = "LENGTH";
	private static final String AUTOFORMULA = "FORMULA";
	private static final String AUTOENV_VAR = "ENV_VAR";
	private static final String AUTOID  =   "ID";
	private JFrame frame;
	private String idCatAMostrar=null;
	private boolean editable=true;
	private final String optativo="?";

	public JPanelDominios(ResourceBundle messages, JFrame frame) {

		this (messages, frame, null);
	}
	/**
	 * Este constructor sirve para mostrar solo una categoria
	 * @param messages
	 * @param frame
	 *
	 */
	public JPanelDominios(ResourceBundle messages, JFrame frame,String idCatAMostrar) {
		try
		{
			this.frame=frame;

			/*Municipio municipio =
				(new OperacionesAdministrador(com.geopista.app.administrador.init.Constantes.url)).
				getMunicipio(new Integer(com.geopista.app.administrador.init.Constantes.idMunicipio).toString());*/
			Entidad entidad =             
				(new OperacionesAdministrador(com.geopista.app.administrador.init.Constantes.url))
				.getEntidad(new Integer(com.geopista.app.administrador.init.Constantes.idEntidad).toString());

			if (entidad!=null)
			{
				com.geopista.app.administrador.init.Constantes.Entidad = entidad.getNombre();
				//com.geopista.app.administrador.init.Constantes.Provincia= municipio.getProvincia();
			}
			this.idCatAMostrar=idCatAMostrar;

			initListas(idCatAMostrar);
			initComponents(messages);
		}catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			JOptionPane.showMessageDialog(frame, "Excepcion al cargar la pantalla:\n"+sw.toString());
			logger.error("Exception: " + sw.toString());
		}
	}


	public boolean isModoNuevo() {
		return modoNuevo;
	}

	public void setModoNuevo(boolean modoNuevo) {
		this.modoNuevo = modoNuevo;
	}

	private void initComponents(ResourceBundle messages)
	{

		jPanelArbolDominios = new javax.swing.JPanel();
		jScrollPaneArbol = new javax.swing.JScrollPane();
		jScrollPaneArbolParticular = new javax.swing.JScrollPane();
		jPanelNuevoBorrar = new javax.swing.JPanel();
		jButtonBorrarDominio = new javax.swing.JButton();
		jButtonEditarDominio = new javax.swing.JButton();
		jButtonNewDominio = new javax.swing.JButton();
		jPanelDominio = new javax.swing.JPanel();
		jPanelLeyenda = new javax.swing.JPanel();
		jPanelAceptarCancelar = new javax.swing.JPanel();
		jButtonCancelar = new javax.swing.JButton();
		jButtonAceptar = new javax.swing.JButton();
		jPanelStringDomain = new javax.swing.JPanel();
		jLabelStringLongMax = new javax.swing.JLabel();
		jTextFieldStringLongMaxima = new JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999));
		jPanelCabecera = new javax.swing.JPanel();
		jLabelNombreDominio = new javax.swing.JLabel();
		jTextFieldDominioPadre= new com.geopista.app.utilidades.TextField();
		jComboBoxTipos = new javax.swing.JComboBox();
		jLabelTipo = new javax.swing.JLabel();
		jTextFieldDescripcion = new com.geopista.app.utilidades.TextField();
		jTextFieldID = new com.geopista.app.utilidades.TextField();
		jLabelID = new javax.swing.JLabel();
		jCheckBoxObligatorio = new javax.swing.JCheckBox();
		jLabelDescripcion = new javax.swing.JLabel();
		jButtonDesIdiomas = new javax.swing.JButton();
		jPanelBooleanDomain = new javax.swing.JPanel();
		jPanelNumberDomain = new javax.swing.JPanel();
		jPanelDateDomain = new javax.swing.JPanel();
		jPanelNada = new javax.swing.JPanel();
		jTextFieldValorMinimo = new JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999));
		jLabelValorMinimo = new javax.swing.JLabel();
		jTextFieldValorMaximo = new JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999)); //999999999999999999
		jLabelValorMaximo = new javax.swing.JLabel();
		jLabelPatron= new javax.swing.JLabel();
		jTextFieldPatron=new com.geopista.app.utilidades.TextField();
		jLabelFormato = new javax.swing.JLabel();
		jComboBoxFormato = new javax.swing.JComboBox();
		jPanelCodeBook = new javax.swing.JPanel();
		jLabelPatronAnadir= new javax.swing.JLabel();
		jTextFieldPatronAnadir=new com.geopista.app.utilidades.TextField();
		jLabelDescripcionAnadir=new javax.swing.JLabel();
		jTextFieldAnadir = new com.geopista.app.utilidades.TextField();
		jButtonAnadir = new javax.swing.JButton();
		jButtonBorrar = new javax.swing.JButton();
		jScrollPaneNodos = new javax.swing.JScrollPane();
		jTableCodeNodes = new javax.swing.JTable();
		jButtonNodosIdiomas = new javax.swing.JButton();
		jLabelFechaInicial = new javax.swing.JLabel();
		jLabelFechaFinal= new javax.swing.JLabel();
		jLabelFormatoFecha = new javax.swing.JLabel();
		jComboBoxFormatoFecha = new javax.swing.JComboBox();
		jTextFieldFechaInicial= new javax.swing.JFormattedTextField(new SimpleDateFormat("dd-MM-yyyy"));
		jTextFieldFechaFinal= new javax.swing.JFormattedTextField(new SimpleDateFormat("dd-MM-yyyy"));
		jCalendarButtonFechaInicial= new CalendarButton(jTextFieldFechaInicial);
		jCalendarButtonFechaFinal= new CalendarButton(jTextFieldFechaFinal);
		jButtonLimpiarFechaFinal= new javax.swing.JButton();
		jButtonLimpiarFechaInicial= new javax.swing.JButton();

		jPanelAutoDomain = new javax.swing.JPanel();
		buttonGroupTipoFormula = new javax.swing.ButtonGroup();
		jComboBoxTipoAuto = new javax.swing.JComboBox();
		jLabelTipoAuto = new javax.swing.JLabel();
		//jPanelFormula = new javax.swing.JPanel();

		jPanelAuxiliar = new javax.swing.JPanel();
		jLabelAuxiliar = new javax.swing.JLabel();
		jTextFieldAuxiliar = new JTextField();

		jRadioButtonPor = new javax.swing.JRadioButton();
		jRadioButtonDividir = new javax.swing.JRadioButton();
		jLabelTipoAtributo1 = new javax.swing.JLabel();
		jTextFieldAtributo1 = new JNumberTextField(JNumberTextField.REAL);
		jLabelTipoAtributo2 = new javax.swing.JLabel();
		jTextFieldAtributo2 = new JNumberTextField(JNumberTextField.REAL);
		jPopupMenuDominios = new JPopupMenu();
		while (!com.geopista.app.utilidades.estructuras.Estructuras.isCargada())
		{
			if (!com.geopista.app.utilidades.estructuras.Estructuras.isIniciada()) com.geopista.app.utilidades.estructuras.Estructuras.cargarEstructuras();
			logger.debug("Esperando a que se terminen de cargar las estructuras");
			try{Thread.sleep(1000);}catch(Exception e){}
		}

		setLayout(new java.awt.BorderLayout());
		jMenuItemNewDominioRoot = new JMenuItem();
		jMenuItemNewDominioRoot.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				crearNuevoDominioPadre();
			}
		});
		jMenuItemNewDominio = new JMenuItem();
		jMenuItemNewDominio.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				anadirDomainNode();
			}
		});

		jMenuItemDeleteDominio = new JMenuItem();
		jMenuItemDeleteDominio.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarDominio();
			}
		});
		jMenuItemDeleteDominioRoot = new JMenuItem();
		jMenuItemDeleteDominioRoot.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarDominioPadre();
			}
		});
		jPopupMenuDominios.add(jMenuItemNewDominioRoot);
		jPopupMenuDominios.add(jMenuItemDeleteDominioRoot);
		jPopupMenuDominios.add(jMenuItemNewDominio);
		jPopupMenuDominios.add(jMenuItemDeleteDominio);
		jMenuItemDeleteDominioRoot.setEnabled(false);
		jMenuItemNewDominioRoot.setEnabled(false);
		jMenuItemDeleteDominio.setEnabled(false);
		jMenuItemNewDominio.setEnabled(false);


		DefaultMutableTreeNode top =
			new DefaultMutableTreeNode(messages.getString("CDominiosFrame.jTreeArbol"));
		initArbol(listaDomain,top);
		jTreeArbol=new JTree(top);
		jTreeArbol.setName("General");
		try
		{
			jTreeArbol.getSelectionModel().setSelectionMode
			(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			JOptionPane.showMessageDialog(frame, "Excepcion al cargar el Renderer:\n"+sw.toString());
			logger.error("Exception: " + sw.toString());
		}
		jTreeArbol.setCellRenderer(new TreeRendererDominios());
		jTreeArbol.addTreeSelectionListener(this);
		jTreeArbol.addMouseListener (
				new MouseAdapter () {
					public void mouseReleased( MouseEvent e ) {
						if ( e.isPopupTrigger()) {
							jPopupMenuDominios.show( (JComponent)e.getSource(), e.getX(), e.getY() );
						}
					}
				}
		);
		DefaultMutableTreeNode topParticular =
			new DefaultMutableTreeNode(messages.getString("CDominiosFrame.jTreeArbolParticular")+" "+Constantes.Entidad);


		initArbol(listaDomainParticular,topParticular);
		jTreeArbolParticular=new JTree(topParticular);
		jTreeArbolParticular.setName("Particular");
		jTreeArbolParticular.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);
		try
		{
			jTreeArbolParticular.setCellRenderer(new TreeRendererDominios());
		}catch(Exception ex)
		{

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			JOptionPane.showMessageDialog(frame, "Excepcion al cargar el Renderer:\n"+sw.toString());
			logger.error("Exception: " + sw.toString());

		}
		jTreeArbolParticular.addTreeSelectionListener(this);
		jTreeArbolParticular.addMouseListener (
				new MouseAdapter () {
					public void mouseReleased( MouseEvent e ) {
						if ( e.isPopupTrigger()) {
							jPopupMenuDominios.show( (JComponent)e.getSource(), e.getX(), e.getY() );
						}
					}
				}
		);
		jPanelArbolDominios.setLayout(new java.awt.BorderLayout());

		jPanelArbolDominios.setMinimumSize(new java.awt.Dimension(300, 51));
		jPanelArbolDominios.setPreferredSize(new java.awt.Dimension(300, 351));
		jScrollPaneArbol.setViewportView(jTreeArbol);
		jScrollPaneArbolParticular.setViewportView(jTreeArbolParticular);
		jPanelArbolDominios.add(jScrollPaneArbol, java.awt.BorderLayout.NORTH);
		jPanelArbolDominios.add(jScrollPaneArbolParticular, java.awt.BorderLayout.CENTER);

		//jPanelNuevoBorrar.setMinimumSize(new java.awt.Dimension(200, 35));
		jPanelNuevoBorrar.setLayout(new java.awt.FlowLayout());
		jButtonBorrarDominio.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrar();
			}
		});
		jButtonEditarDominio.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editarDominioActionPerformed();
			}

		});
		jPanelNuevoBorrar.add(jButtonBorrarDominio);
		jButtonNewDominio.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				crear();
			}
		});        

		jPanelDominio.setLayout(new java.awt.BorderLayout());
		jPanelLeyenda.setLayout(new java.awt.GridLayout(15,1));

		jButtonCancelar.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelarActionPerformed();
			}
		});

		jPanelAceptarCancelar.add(jButtonCancelar);

		jButtonAceptar.addActionListener(new java.awt.event.ActionListener()
		{  public void actionPerformed(java.awt.event.ActionEvent evt)
		{  aceptarActionPerformed(); }
		});
		jPanelAceptarCancelar.add(jButtonAceptar);

		jPanelDominio.add(jPanelAceptarCancelar, java.awt.BorderLayout.SOUTH);

		jPanelStringDomain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanelStringDomain.setAutoscrolls(true);

		jPanelStringDomain.add(jLabelStringLongMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 110, -1));

		jPanelStringDomain.add(jTextFieldStringLongMaxima, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 60, -1));

		jPanelStringDomain.add(jLabelPatron, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 110, -1));

		jPanelStringDomain.add(jTextFieldPatron, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 200, -1));

		jPanelDominio.add(jPanelStringDomain, java.awt.BorderLayout.CENTER);

		jPanelCabecera.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanelCabecera.setPreferredSize(new java.awt.Dimension(287, 140));
		jPanelCabecera.add(jLabelNombreDominio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, -1, -1));
		jTextFieldDominioPadre.setEditable(false);
		jPanelCabecera.add(jTextFieldDominioPadre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 180, -1));

		jPanelCabecera.add(jComboBoxTipos, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 180, -1));
		jComboBoxTipos.setRenderer(new ComboBoxRendererTipos());
		jComboBoxTipos.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				jComboBoxTiposActionPerformed();
			}
		});


		jTextFieldID.setEditable(false);

		jPanelCabecera.add(jLabelTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

		jPanelCabecera.add(jLabelDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));
		jPanelCabecera.add(jTextFieldDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 240, -1));

		jPanelCabecera.add(jLabelID, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));
		jPanelCabecera.add(jTextFieldID, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 60, -1));

		jPanelCabecera.add(jCheckBoxObligatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 260, -1));

		try
		{
			ClassLoader cl =(new TreeRendererDominios()).getClass().getClassLoader();
			Icon banderasIcon= new javax.swing.ImageIcon(cl.getResource("img/banderas.gif"));
			jButtonDesIdiomas.setIcon(banderasIcon);
			jButtonNodosIdiomas.setIcon(banderasIcon);
		}catch(Exception e){}


		jButtonDesIdiomas.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				if(modoNuevo)
				{
					if (jTextFieldDescripcion.getText().length()>0)   auxDomainNode.gethDict().put(Constantes.Locale,jTextFieldDescripcion.getText());
					JDialogDiccionario jDiccionario= showIdiomasDescripcion(auxDomainNode);
					Hashtable hDict=jDiccionario.getDiccionario();
					if (hDict!=null)auxDomainNode.sethDict(hDict);
					jDiccionario=null;
					jTextFieldDescripcion.setText((String)auxDomainNode.gethDict().get(Constantes.Locale));
				}
				else
				{
					if (nodeArbolSelected==null) return;
					Object nodeInfo=nodeArbolSelected.getUserObject();
					if (nodeInfo instanceof DomainNode)
					{
						showIdiomasDescripcion((DomainNode)nodeInfo);
					}

				}
			}
		});
		jPanelCabecera.add(jButtonDesIdiomas, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, 20, 20));

		jPanelDominio.add(jPanelCabecera, java.awt.BorderLayout.NORTH);

		jPanelDominio.add(jPanelBooleanDomain, java.awt.BorderLayout.CENTER);

		jPanelNumberDomain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());



		jPanelNumberDomain.add(jTextFieldValorMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 100, -1));

		jPanelNumberDomain.add(jLabelValorMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 240, -1));

		jPanelNumberDomain.add(jTextFieldValorMaximo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 100, -1));

		jPanelNumberDomain.add(jLabelValorMaximo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 240, -1));

		jPanelNumberDomain.add(jLabelFormato, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 130, -1));

		jComboBoxFormato.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "#", "##", "###", "####", "#####", "#.#", "##.#", "##.##", "###.###" }));

		jPanelNumberDomain.add(jComboBoxFormato, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 120, -1));

		jPanelDominio.add(jPanelNumberDomain, java.awt.BorderLayout.CENTER);

		jPanelDateDomain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanelDateDomain.add(jTextFieldFechaInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 100, -1));
		jPanelDateDomain.add(jCalendarButtonFechaInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 20, 20));
		jPanelDateDomain.add(jButtonLimpiarFechaInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 20, 20));

		jPanelDateDomain.add(jLabelFechaInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 240, -1));
		jButtonLimpiarFechaInicial.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				limpiarFechaInicial();
			}
		});


		jPanelDateDomain.add(jTextFieldFechaFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 100, -1));
		jButtonLimpiarFechaFinal.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				limpiarFechaFinal();
			}
		});
		jTextFieldFechaInicial.setEditable(false);
		jTextFieldFechaFinal.setEditable(false);


		jPanelDateDomain.add(jButtonLimpiarFechaFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 20, 20));

		jPanelDateDomain.add(jCalendarButtonFechaFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 20, 20));


		jPanelDateDomain.add(jLabelFechaFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 240, -1));

		jPanelDateDomain.add(jLabelFormatoFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 130, -1));


		jComboBoxFormatoFecha.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dd-MM-yyyy", "dd/MM/yyyy", "MM-dd-yyyy", "MM/dd/yyyy"}));
		jComboBoxFormatoFecha.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				jComboBoxFormatoFechaActionPerformed();
			}
		});
		jPanelDateDomain.add(jComboBoxFormatoFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 120, -1));

		jPanelCodeBook.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());


		jPanelCodeBook.add(jLabelPatronAnadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 110, -1));

		jPanelCodeBook.add(jTextFieldPatronAnadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 110, -1));
		jPanelCodeBook.add(jLabelDescripcionAnadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, 110, -1));


		jPanelCodeBook.add(jTextFieldAnadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, 110, -1));

		jTextFieldAnadir.setEditable(false);
		jButtonAnadir.setEnabled(false);
		jButtonBorrar.setEnabled(false);
		jButtonBorrar.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				deleteCodeEntry();
			}
		});

		jButtonAnadir.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				addCodeEntry();
			}
		});
		jPanelCodeBook.add(jButtonAnadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, 80, -1));


		jPanelCodeBook.add(jButtonBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 80, -1));


		//Para seleccionar una fila

		jScrollPaneNodos.setViewportView(jTableCodeNodes);

		jPanelCodeBook.add(jScrollPaneNodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 250, 300));

		jButtonNodosIdiomas.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				if(modoNuevo)
				{
					if (auxDomainCodeEntry==null) auxDomainCodeEntry=new DomainNode();
					if (jTextFieldAnadir.getText().length()>0)
					{
						if (jTextFieldPatronAnadir.getText().length() > Constantes.PATRON_MAX_SIZE) {
							JOptionPane.showMessageDialog(frame, "Patron demasiado largo.");
							//JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.nododominionoselected"),JOptionPane.INFORMATION_MESSAGE);
							//JDialog dialog =optionPane.createDialog(null, "");
							//dialog.show();
							return;
						}
						auxDomainCodeEntry.setPatron(jTextFieldPatronAnadir.getText());
						auxDomainCodeEntry.gethDict().put(Constantes.Locale,jTextFieldAnadir.getText());
					}
					JDialogDiccionario jDiccionario= showIdiomasDescripcion(auxDomainCodeEntry);
					Hashtable hDict=jDiccionario.getDiccionario();
					if (hDict!=null)auxDomainCodeEntry.sethDict(hDict);
					jDiccionario=null;
					jTextFieldAnadir.setText((String)auxDomainCodeEntry.gethDict().get(Constantes.Locale));
					//jTextFieldPatronAnadir.setText(auxDomainCodeEntry.getPatron());
				}
				else
				{
					if ((nodeArbolSelected==null)||(idCodeEntrySelected==null)) return;
					Object nodeInfo=nodeArbolSelected.getUserObject();
					if (nodeInfo instanceof DomainNode)
					{
						DomainNode dnHijo=(DomainNode)((DomainNode)nodeInfo).getlHijos().gethDom().get(idCodeEntrySelected);
						if (dnHijo!=null) showIdiomasDescripcion(dnHijo);
					}

				}
			}
		});

		jPanelCodeBook.add(jButtonNodosIdiomas, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 90, 20, 20));

		jPanelDominio.add(jPanelCodeBook, java.awt.BorderLayout.CENTER);

		add(jPanelDominio, java.awt.BorderLayout.CENTER);
		add(jPanelLeyenda, java.awt.BorderLayout.EAST);




		ListSelectionModel rowCodeNode = jTableCodeNodes.getSelectionModel();
		rowCodeNode.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				seleccionarCodeEntry(e);
			}
		});

		jPanelAutoDomain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanelAutoDomain.setAutoscrolls(true);

		jComboBoxTipoAuto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { 
				AUTOAREA, AUTOLENGHT, AUTOFORMULA, AUTOID, AUTOENV_VAR 
		}));
		jComboBoxTipoAuto.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				seleccionTiposAuto();
			}
		});
		jPanelAutoDomain.add(jComboBoxTipoAuto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 130, -1));
		jPanelAutoDomain.add(jLabelTipoAuto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 80, -1));
		//jPanelFormula.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		//jPanelFormula.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));

		jPanelAuxiliar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanelAuxiliar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));

		jRadioButtonPor.setFont(new java.awt.Font("MS Sans Serif", 1, 18));
		jRadioButtonPor.setSelected(true);
		jRadioButtonPor.setText("*");
		buttonGroupTipoFormula.add(jRadioButtonPor);
		//jPanelFormula.add(jRadioButtonPor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
		jRadioButtonDividir.setFont(new java.awt.Font("MS Sans Serif", 1, 18));
		jRadioButtonDividir.setText("/");
		buttonGroupTipoFormula.add(jRadioButtonDividir);
		//jPanelFormula.add(jRadioButtonDividir, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));
		//jPanelFormula.add(jLabelTipoAtributo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 80, -1));
		//jPanelFormula.add(jTextFieldAtributo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 150, 20));
		//jPanelFormula.add(jLabelTipoAtributo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 80, -1));
		//jPanelFormula.add(jTextFieldAtributo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 150, 20));
		//jPanelAutoDomain.add(jPanelFormula, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 240, 160));

		jPanelAuxiliar.add(jLabelAuxiliar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 200, 20));
		jPanelAuxiliar.add(jTextFieldAuxiliar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 200, 20));
		jPanelAutoDomain.add(jPanelAuxiliar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 240, 100));

		jPanelDominio.add(jPanelAutoDomain, java.awt.BorderLayout.CENTER);


		jButtonNewDominio.setPreferredSize(new java.awt.Dimension(75, 25));
		jPanelNuevoBorrar.add(jButtonNewDominio);
		jPanelNuevoBorrar.add(jButtonEditarDominio);
		jPanelArbolDominios.add(jPanelNuevoBorrar, java.awt.BorderLayout.SOUTH);
		add(jPanelArbolDominios, java.awt.BorderLayout.WEST);
		initDomainPanel(0);
		enabledComponents(false);
		pintarTipos(0);
		pintaLeyenda();

	}
	private void initListas()
	{
		initListas(null);
	}

	private void initListas(String idCategoria)
	{
		try
		{
			//logger.info("Obteniendo datos: ");
			listaDomain =new ListaDomain();
			listaDomainParticular =new ListaDomain();
			(new OperacionesAdministrador(Constantes.url)).getDominios(idCategoria,listaDomain,listaDomainParticular);
			//logger.info("Obteniendo categorias");
			listaCategorias= (new OperacionesAdministrador(Constantes.url)).getListaCategorias();
		}catch(Exception e)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error al inicializar la lista de dominios: "+sw.toString());
			JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.ERROR_MESSAGE);
			JDialog dialog =optionPane.createDialog(this,"ERROR al inicializar Dominios");
			dialog.show();
		}
	}
	public void valueChanged(TreeSelectionEvent e) {
		if (e==null || !(e.getSource() instanceof JTree)) return;
		JTree arbol= (JTree)e.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)arbol.getLastSelectedPathComponent();
		if (node == null) return;
		if (arbol.getName().indexOf("Particular")>=0)
		{
			nodoParticular=true;
			jTreeArbol.clearSelection();
		}
		else
		{
			nodoParticular=false;
			jTreeArbolParticular.clearSelection();

		}
		pintarTipos(0);
		Object nodeInfo = node.getUserObject();
		if (nodeInfo instanceof DomainNode) {
			if (editable)
			{
				jMenuItemDeleteDominioRoot.setEnabled(false);
				jMenuItemNewDominioRoot.setEnabled(false);
				jMenuItemDeleteDominio.setEnabled(true);
				jMenuItemNewDominio.setEnabled(true);
			}else editable(editable);
			jButtonDesIdiomas.setEnabled(true);
			nodeArbolSelected=node;
			jPanelCabecera.setVisible(true);
			mostrarDomainNode((DomainNode)nodeInfo);
		}else if (nodeInfo instanceof Domain)
		{
			if (editable)
			{
				jMenuItemDeleteDominioRoot.setEnabled(true);
				jMenuItemNewDominioRoot.setEnabled(false);
				jMenuItemDeleteDominio.setEnabled(false);
				jMenuItemNewDominio.setEnabled(true);
			}else editable(editable);
			jPanelCabecera.setVisible(true);
			if (((Domain)nodeInfo).getListaNodes().getFirst() != null)
				mostrarDomainNode(((Domain)nodeInfo).getListaNodes().getFirst());
			Domain auxDomain= (Domain)nodeInfo;
			jTextFieldDominioPadre.setText("");
			jTextFieldDescripcion.setText(auxDomain.getName());
			jTextFieldID.setText(auxDomain.getIdDomain());
			jLabelID.setText(messages.getString("CDominiosFrame.jLabelID_Domain"));
			jComboBoxTipos.setSelectedIndex(0);
			jButtonDesIdiomas.setEnabled(false);
			nodeArbolSelected=node;
		}else if (nodeInfo instanceof Category)
		{
			if (editable)
			{
				jMenuItemDeleteDominioRoot.setEnabled(false);
				jMenuItemNewDominioRoot.setEnabled(true);
				jMenuItemDeleteDominio.setEnabled(false);
				jMenuItemNewDominio.setEnabled(false);
			}else editable(editable);
			jPanelCabecera.setVisible(false);
			jComboBoxTipos.setSelectedIndex(0);
			nodeArbolSelected=node;
		}
		else
		{
			if (editable)
			{
				jMenuItemDeleteDominioRoot.setEnabled(false);
				jMenuItemNewDominioRoot.setEnabled(false);
				jMenuItemDeleteDominio.setEnabled(false);
				jMenuItemNewDominio.setEnabled(false);
			}else editable(editable);

			nodeArbolSelected=null;
			jPanelCabecera.setVisible(false);
		}


		this.invalidate();
		this.validate();
	}
	public DefaultMutableTreeNode buscarHojaCategoria(Domain domain, DefaultMutableTreeNode top)
	{
		DefaultMutableTreeNode hojaCategoria=null;
		for (int i=0;i<top.getChildCount()&&hojaCategoria==null;i++)
		{
			DefaultMutableTreeNode aux =(DefaultMutableTreeNode)top.getChildAt(i);
			Object nodeInfo = aux.getUserObject();
			if (nodeInfo instanceof Category)
			{
				if (((Category)nodeInfo).getId()==null)
				{
					if(domain.getIdCategory()==null)
						hojaCategoria=aux;
				}else
				{
					if(((Category)nodeInfo).getId().equals(domain.getIdCategory()))
						hojaCategoria=aux;
				}
			}
		}

		if (hojaCategoria==null)
		{
			if (idCatAMostrar==null || idCatAMostrar.equals(domain.getIdCategory()))
			{
				if (domain.getIdCategory()==null|| listaCategorias.get(domain.getIdCategory())==null)
				{
					Category auxNula=new Category(domain.getIdCategory(),"0");
					auxNula.addTerm(Constantes.Locale,"Sin Categoria");
					hojaCategoria = new DefaultMutableTreeNode(auxNula);
				}
				else
					hojaCategoria = new DefaultMutableTreeNode(listaCategorias.get(domain.getIdCategory()));
				top.add(hojaCategoria);
			}
		}
		return hojaCategoria;
	}
	private void initArbol(ListaDomain lista,DefaultMutableTreeNode top) {
		if (lista!=null)
		{
			for(Enumeration e=lista.keys();e.hasMoreElements();)
			{
				Domain domain = (Domain)lista.get((String)e.nextElement());
				DefaultMutableTreeNode hojaCategoria= buscarHojaCategoria(domain,top);
				if (hojaCategoria!=null)
				{
					DefaultMutableTreeNode hojaDominio = new DefaultMutableTreeNode(domain);
					hojaCategoria.add(hojaDominio);
					initArbol(hojaDominio,domain.getListaNodes(),domain.getIdDomain());
				}
			}
		}

	}
	private void initArbol(DefaultMutableTreeNode hojaPadre, ListaDomainNode listaDomainNode, String sIdDomain)
	{
		if (listaDomainNode==null) return;
		for(Enumeration e=listaDomainNode.gethDom().elements();e.hasMoreElements();)
		{
			DomainNode domainNode = (DomainNode)e.nextElement();
			if (domainNode.getIdDomain()==null) domainNode.setIdDomain(sIdDomain);
			DefaultMutableTreeNode hojaHijo = new DefaultMutableTreeNode(domainNode);
			hojaPadre.add(hojaHijo);
			initArbol(hojaHijo, domainNode.getlHijos(),sIdDomain);
		}
	}

	private void pintarTipos(int iTipoPadre)
	{
		jComboBoxTipos.removeAllItems();
		switch (iTipoPadre)
		{
		case com.geopista.feature.Domain.CODEBOOK:
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.CODEDENTRY,"CodeEntryDomain"));
			break;
		case com.geopista.feature.Domain.PATTERN:
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.PATTERN,"StringDomain"));
			break;
		case com.geopista.feature.Domain.NUMBER:
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.NUMBER,"NumberDomain"));
			break;
		case com.geopista.feature.Domain.BOOLEAN:
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.BOOLEAN,"BooleanDomain"));
			break;
		case com.geopista.feature.Domain.DATE:
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.DATE,"DateDomain"));
			break;
		case com.geopista.feature.Domain.CODEDENTRY:
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.CODEDENTRY,"CodeEntryDomain"));
			break;
		case com.geopista.feature.Domain.AUTO:
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.AUTO,"AutoDomain"));
			break;
		case com.geopista.feature.Domain.TREE:
			jComboBoxTipos.addItem(new TipoDominio(0,"           "));
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.PATTERN,"StringDomain"));
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.NUMBER,"NumberDomain"));
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.BOOLEAN,"BooleanDomain"));
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.DATE,"DateDomain"));
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.CODEDENTRY,"CodeEntryDomain"));
			jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.AUTO,"AutoDomain"));
			break;
		default:
			jComboBoxTipos.removeAllItems();
		jComboBoxTipos.addItem(new TipoDominio(0,"           "));
		jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.PATTERN,"StringDomain"));
		jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.TREE,"TreeDomain"));
		jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.NUMBER,"NumberDomain"));
		jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.CODEBOOK,"CodeBookDomain"));
		jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.BOOLEAN,"BooleanDomain"));
		jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.DATE,"DateDomain"));
		jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.CODEDENTRY,"CodeEntryDomain"));
		jComboBoxTipos.addItem(new TipoDominio(com.geopista.feature.Domain.AUTO,"AutoDomain"));
		break;
		}
	}
	private void jComboBoxTiposActionPerformed()
	{

		TipoDominio tipo=(TipoDominio)jComboBoxTipos.getSelectedItem();
		if (tipo!=null)
			initDomainPanel(tipo.getId());
		else
			initDomainPanel(0);

	}
	private void seleccionTiposAuto()
	{
		if (jComboBoxTipoAuto.getSelectedIndex()==2)
		{    
			jLabelAuxiliar.setText(messages.getString("CDominiosFrame.jLabelAuxFormula"));
			jPanelAuxiliar.setVisible(true);
		}
		else if (jComboBoxTipoAuto.getSelectedIndex()==4)
		{    
			jLabelAuxiliar.setText(messages.getString("CDominiosFrame.jLabelAuxEntorno"));
			jPanelAuxiliar.setVisible(true);

		}        
		else         
		{
			jPanelAuxiliar.setVisible(false);
		}

		invalidate();
		validate();

	}
	private void jComboBoxFormatoFechaActionPerformed()
	{
		String  sFormatoFecha=(String)jComboBoxFormatoFecha.getSelectedItem();
		jPanelDateDomain.remove(jTextFieldFechaInicial);
		jPanelDateDomain.remove(jTextFieldFechaFinal);
		jTextFieldFechaInicial= new javax.swing.JFormattedTextField(new SimpleDateFormat(sFormatoFecha));
		jTextFieldFechaFinal= new javax.swing.JFormattedTextField(new SimpleDateFormat(sFormatoFecha));
		jTextFieldFechaInicial.setEditable(false);
		jTextFieldFechaFinal.setEditable(false);
		jCalendarButtonFechaInicial.setCampoTexto(jTextFieldFechaInicial);
		jCalendarButtonFechaFinal.setCampoTexto(jTextFieldFechaFinal);
		jPanelDateDomain.add(jTextFieldFechaInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 100, -1));
		jPanelDateDomain.add(jTextFieldFechaFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 100, -1));
		jPanelDateDomain.invalidate();
		jPanelDateDomain.validate();
	}

	private void initDomainPanel(int iIndexSelected)
	{
		JPanel jPanelAux=null;
		switch (iIndexSelected)
		{
		case com.geopista.feature.Domain.PATTERN: jPanelAux=jPanelStringDomain;
		break;
		case com.geopista.feature.Domain.NUMBER: jPanelAux=jPanelNumberDomain;
		break;
		case com.geopista.feature.Domain.CODEBOOK: jPanelAux=jPanelCodeBook;
		break;
		case com.geopista.feature.Domain.BOOLEAN: jPanelAux=jPanelBooleanDomain;
		break;
		case com.geopista.feature.Domain.DATE: jPanelAux=jPanelDateDomain;
		break;
		case com.geopista.feature.Domain.AUTO: jPanelAux=jPanelAutoDomain;
		break;
		case com.geopista.feature.Domain.CODEDENTRY:jPanelAux=jPanelStringDomain;
		break;

		default:jPanelAux=jPanelNada;
		break;
		}
		jPanelNada.setVisible((iIndexSelected==0));
		jPanelStringDomain.setVisible(((iIndexSelected==com.geopista.feature.Domain.PATTERN)||(iIndexSelected==com.geopista.feature.Domain.CODEDENTRY)));
		jPanelNumberDomain.setVisible((iIndexSelected==com.geopista.feature.Domain.NUMBER));
		jPanelCodeBook.setVisible((iIndexSelected==com.geopista.feature.Domain.CODEBOOK));
		jPanelBooleanDomain.setVisible((iIndexSelected==com.geopista.feature.Domain.BOOLEAN));
		jPanelDateDomain.setVisible((iIndexSelected==com.geopista.feature.Domain.DATE));
		jPanelAutoDomain.setVisible((iIndexSelected==com.geopista.feature.Domain.AUTO));
		if (jPanelAux!=null)
			jPanelDominio.add(jPanelAux, java.awt.BorderLayout.CENTER);

		jPanelCabecera.repaint();
		jPanelCabecera.invalidate();
		jPanelCabecera.validate();

		invalidate();
		validate();

	}
	private void crearNuevoDominioPadre()
	{
		try
		{
			Category categoria=null;
			if (nodeArbolSelected!=null)
			{
				Object nodeInfo=nodeArbolSelected.getUserObject();
				if (nodeInfo instanceof Category)
				{
					categoria=(Category)nodeInfo;
				}
			}

			String inputValue=(String)JOptionPane.showInputDialog(this, messages.getString("CDominiosFrame.jLabelNombreDominio"),
					(categoria==null?(messages.getString("CDominiosFrame.jMenuNewDominioRoot")):
						(messages.getString("CDominiosFrame.jMenuNewDominioRootCategory")+" "+categoria.getTerm(com.geopista.app.administrador.init.Constantes.Locale))) , -1,
						null, null, null);
			if (inputValue!=null && inputValue.length()>0)
			{
				Domain newDomain= new Domain("",inputValue);
				if (categoria!=null)
					newDomain.setIdCategory(categoria.getId());
				if (inputValue.length() > Constantes.NODO_PADRE_SIZE) {
					JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.nodopadresize"),JOptionPane.INFORMATION_MESSAGE);
					JDialog dialog =optionPane.createDialog(this, "");
					dialog.show();
					return;
				}

				CResultadoOperacion resultado=(new OperacionesAdministrador(Constantes.url)).nuevoDomain(newDomain);
				logger.debug("Identificador del dominio:"+newDomain.getIdDomain());
				try
				{newDomain=(Domain)resultado.getVector().get(0);}catch(Exception e)
				{newDomain.setIdDomain(resultado.getDescripcion());}
				listaDomain.add(newDomain);
				listaDomainParticular.add(newDomain);

				if (resultado.getResultado())
				{
					DefaultMutableTreeNode hojaCategoria= buscarHojaCategoria(newDomain,(DefaultMutableTreeNode)jTreeArbol.getModel().getRoot());
					if (hojaCategoria!=null)
					{
						DefaultTreeModel treeModel=(DefaultTreeModel)jTreeArbol.getModel();
						DefaultMutableTreeNode hojaDominio = new DefaultMutableTreeNode(newDomain);
						treeModel.insertNodeInto(hojaDominio, (MutableTreeNode)hojaCategoria, ((MutableTreeNode)hojaCategoria).getChildCount());
						jTreeArbol.scrollPathToVisible(new TreePath(hojaDominio.getPath()));
					}
					DefaultMutableTreeNode hojaCategoriaParticular= buscarHojaCategoria(newDomain,(DefaultMutableTreeNode)jTreeArbolParticular.getModel().getRoot());
					if (hojaCategoriaParticular!=null)
					{
						DefaultTreeModel treeModel=(DefaultTreeModel)jTreeArbolParticular.getModel();
						//Domain domainParticular=new Domain(newDomain.getIdDomain(), newDomain.getName());
						DefaultMutableTreeNode hojaDominio = new DefaultMutableTreeNode(newDomain);
						treeModel.insertNodeInto(hojaDominio, (MutableTreeNode)hojaCategoriaParticular, ((MutableTreeNode)hojaCategoriaParticular).getChildCount());
						jTreeArbolParticular.scrollPathToVisible(new TreePath(hojaDominio.getPath()));
					}
					/*DefaultTreeModel treeModel=(DefaultTreeModel)jTreeArbol.getModel();
                     DefaultMutableTreeNode hojaDominio = new DefaultMutableTreeNode(newDomain);
                     treeModel.insertNodeInto(hojaDominio, (MutableTreeNode)treeModel.getRoot(),((MutableTreeNode)treeModel.getRoot()).getChildCount());
                     jTreeArbol.scrollPathToVisible(new TreePath(hojaDominio.getPath()));
                     //Lo meto tambien en la lista particular del municipio
                      Domain domainParticular=new Domain(newDomain.getIdDomain(), newDomain.getName());
                      DefaultTreeModel treeModelParticular=(DefaultTreeModel)jTreeArbolParticular.getModel();
                      DefaultMutableTreeNode hojaDominioParticular = new DefaultMutableTreeNode(domainParticular);
                      treeModelParticular.insertNodeInto(hojaDominioParticular, (MutableTreeNode)treeModelParticular.getRoot(),((MutableTreeNode)treeModelParticular.getRoot()).getChildCount());
                      jTreeArbolParticular.scrollPathToVisible(new TreePath(hojaDominioParticular.getPath()));*/

				}
				else
				{
					JOptionPane optionPane= new JOptionPane(resultado.getDescripcion(),JOptionPane.ERROR_MESSAGE);
					JDialog dialog =optionPane.createDialog(this,"");
					dialog.show();
				}
			}
		}catch(Exception ex)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Excepcion al crear un dominio padre: "+sw.toString());
			JOptionPane optionPane= new JOptionPane(ex.getMessage(),JOptionPane.ERROR_MESSAGE);
			JDialog dialog =optionPane.createDialog(this,"");
			dialog.show();
		}
	}
	private void crear()
	{
		if (nodeArbolSelected!=null)
		{
			Object nodeInfo=nodeArbolSelected.getUserObject();
			if (nodeInfo instanceof Category)
			{
				crearNuevoDominioPadre();
				return;
			}
		}
		anadirDomainNode();
	}

	private void borrar()
	{
		if (nodeArbolSelected!=null)
		{
			Object nodeInfo=nodeArbolSelected.getUserObject();
			if (nodeInfo instanceof Domain)
			{
				borrarDominioPadre();
				return;
			}
		}
		borrarDominio();
	}
	private void borrarDominio()
	{
		ListaDomain lista=null;
		if (nodoParticular)
			lista=listaDomainParticular;
		else
			lista=listaDomain;
		try
		{
			if (nodeArbolSelected==null)
			{
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.nododominionoselected"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
				return;
			}

			Object nodeInfo=nodeArbolSelected.getUserObject();
			if (!(nodeInfo instanceof DomainNode))
			{
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.nodonoselected"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
				return;

			}
			DomainNode nodoBorrado=(DomainNode) nodeInfo;
			if (nodoBorrado.getlHijos().gethDom().size()>0)
			{
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.tienehijos"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
				return;
			}

			int n = JOptionPane.showOptionDialog(this,
					messages.getString("CDominiosFrame.mensaje.borrarnodo")+" "+nodoBorrado.getTerm(Constantes.Locale)+"?",
					"",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,null,null,null);
			if (n==JOptionPane.NO_OPTION)  return;
			CResultadoOperacion result=null;
			result=(new OperacionesAdministrador(Constantes.url)).eliminarDomainNode(nodoBorrado);
			if (result.getResultado())
			{
				enabledComponents(false);
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.nodeborrado"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();

				mostrarDomainNode(null);
				removeCurrentNode();
				nodeArbolSelected=null;
				if (nodoBorrado.getIdParent()!=null)
				{
					DomainNode nodePadre=lista.getNode(nodoBorrado.getIdDomain(),nodoBorrado.getIdParent());
					nodePadre.removeHijo(nodoBorrado);
				}
				else
				{
					Domain domainPadre=lista.get(nodoBorrado.getIdDomain());
					domainPadre.removeNode(nodoBorrado);
				}
				nodoBorrado=null;
			}
			else
			{
				JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
			}
		}catch(Exception ex)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Excepcion al añadir usuario: "+sw.toString());
			JOptionPane optionPane= new JOptionPane(ex.getMessage(),JOptionPane.ERROR_MESSAGE);
			JDialog dialog =optionPane.createDialog(this,"");
			dialog.show();
		}
	}
	private void anadirDomainNode()
	{
		ListaDomain lista;
		if (nodoParticular)
			lista=listaDomainParticular;
		else
			lista=listaDomain;
		if (nodeArbolSelected==null)
		{
			JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.nodonoselected"),JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog =optionPane.createDialog(this,"");
			dialog.show();
			return;
		}
		Object nodeInfo = nodeArbolSelected.getUserObject();
		if (nodeInfo instanceof DomainNode)
		{
			DomainNode auxNode= (DomainNode)nodeInfo;
			if ((auxNode.getType()==com.geopista.feature.Domain.TREE)||lista.hasFamily(auxNode,com.geopista.feature.Domain.TREE))
			{
				if (auxNode.getlHijos().gethDom().size()>0)
					pintarTipos(auxNode.getlHijos().getFirst().getType());
				else
					pintarTipos(com.geopista.feature.Domain.TREE);
			}
			else
			{
				if ((auxNode.getType()!=com.geopista.feature.Domain.CODEBOOK))
				{
					JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.notreemodel"),JOptionPane.INFORMATION_MESSAGE);
					JDialog dialog =optionPane.createDialog(this,"");
					dialog.show();
					return;
				}
				pintarTipos(com.geopista.feature.Domain.CODEBOOK);
			}
			mostrarDomainNode(null);
			jTextFieldDominioPadre.setText(auxNode.getTerm(Constantes.Locale));
			auxDomainNode=new DomainNode();
			auxDomainNode.setIdDomain(auxNode.getIdDomain());
			auxDomainNode.setIdParent(auxNode.getIdNode());
		}
		if (nodeInfo instanceof Domain)
		{
			Domain auxDomain= (Domain)nodeInfo;
			if (auxDomain.getListaNodes().gethDom().size()>0)
			{
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.masdeunnodo"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
				return;
			}
			pintarTipos(0);
			mostrarDomainNode(null);
			jTextFieldDominioPadre.setText(auxDomain.getName());
			auxDomainNode=new DomainNode();
			auxDomainNode.setIdDomain(auxDomain.getIdDomain());
		}
		modoNuevo=true;
		initDomainPanel(0);
		jButtonDesIdiomas.setEnabled(true);
		enabledComponents(true);

		if (nodoParticular)
			auxDomainNode.setIdMuni(com.geopista.security.SecurityManager.getIdEntidad());
		jComboBoxTipos.setSelectedIndex(0);
		this.invalidate();
		this.validate();
	}
	private boolean borrarDominiodeArbol(JTree arbol, Domain dominio)
	{
		if (dominio.getIdDomain()==null) return false;
		DefaultMutableTreeNode hojaCategoria=buscarHojaCategoria(dominio,(DefaultMutableTreeNode)arbol.getModel().getRoot());
		for (int i=0;i<hojaCategoria.getChildCount();i++)
		{
			DefaultMutableTreeNode aux =(DefaultMutableTreeNode)hojaCategoria.getChildAt(i);
			Object nodeInfo = aux.getUserObject();
			if (nodeInfo instanceof Domain)
			{
				if( dominio.getIdDomain().equals(((Domain)nodeInfo).getIdDomain()))
				{
					((DefaultTreeModel)arbol.getModel()).removeNodeFromParent(aux);
					//hojaCategoria.remove(i);
					return true;
				}
			}
		}
		return false;
	}

	private void borrarDominioPadre()
	{
		try
		{
			if (nodeArbolSelected==null)
			{
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.dominionoselected"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.setVisible(true);
				return;
			}

			Object nodeInfo=nodeArbolSelected.getUserObject();
			if (!(nodeInfo instanceof Domain))
			{
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.dominionoselected"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.setVisible(true);
				return;

			}
			Domain nodoBorrado=(Domain) nodeInfo;
			if (nodoBorrado.getListaNodes().gethDom().size()>0)
			{
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.tienehijos"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
				return;
			}

			int n = JOptionPane.showOptionDialog(this,
					messages.getString("CDominiosFrame.mensaje.borrarnodo")+" "+nodoBorrado.getName()+"?",
					"",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,null,null,null);
			if (n==JOptionPane.NO_OPTION)  return;
			CResultadoOperacion result=null;
			result=(new OperacionesAdministrador(Constantes.url)).eliminarDomain(nodoBorrado);
			if (result.getResultado())
			{
				enabledComponents(false);
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.dominioborrado"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();

				mostrarDomainNode(null);
				removeCurrentNode();
				//Borramos el nodo del arbol que no esta seleccionado
				if (nodoParticular)
					borrarDominiodeArbol(jTreeArbol,nodoBorrado);
				else
					borrarDominiodeArbol(jTreeArbolParticular,nodoBorrado);
				nodeArbolSelected=null;
				listaDomainParticular.remove(nodoBorrado.getIdDomain());
				listaDomain.remove(nodoBorrado.getIdDomain());
				nodoBorrado=null;
			}
			else
			{
				JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
			}
		}catch(Exception ex)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Excepcion al borrar un dominio: "+sw.toString());
			JOptionPane optionPane= new JOptionPane(ex,JOptionPane.ERROR_MESSAGE);
			JDialog dialog =optionPane.createDialog(this,"");
			dialog.show();
		}
	}

	private void editarDominioActionPerformed()
	{
		if (nodeArbolSelected==null)
		{
			JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.nodonoselected"),JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog =optionPane.createDialog(this,"");
			dialog.show();
			return;
		}
		Object nodeInfo = nodeArbolSelected.getUserObject();
		if (nodeInfo instanceof Domain)
		{
			JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.noeditdomain"),JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog =optionPane.createDialog(this,"");
			dialog.show();
			return;

		}
		if (nodeInfo instanceof DomainNode)
		{

			auxDomainNode=(DomainNode)((DomainNode)nodeInfo).clone();
			modoNuevo=true;
			modoEdicion=true;
			enabledComponents(true);
			jComboBoxTipos.setEnabled(false);
			jTextFieldAnadir.setEditable(false);
			jTextFieldPatronAnadir.setEditable(false);
			jButtonAnadir.setEnabled(false);
			jButtonBorrar.setEnabled(false);

		}

	}
	private void enabledComponents(boolean bEnabled)
	{

		jTextFieldDescripcion.setEditable(bEnabled);
		jCheckBoxObligatorio.setEnabled(bEnabled);
		jTextFieldPatronAnadir.setEditable(bEnabled);
		jTextFieldStringLongMaxima.setEditable(bEnabled);
		jTextFieldPatron.setEditable(bEnabled);
		jTextFieldValorMaximo.setEditable(bEnabled);
		jTextFieldValorMinimo.setEditable(bEnabled);

		jButtonLimpiarFechaFinal.setEnabled(bEnabled);
		jButtonLimpiarFechaInicial.setEnabled(bEnabled);
		jTextFieldAnadir.setEditable(bEnabled);
		jButtonAnadir.setEnabled(bEnabled);
		jButtonBorrar.setEnabled(bEnabled);


		jComboBoxFormato.setEnabled(bEnabled);
		jComboBoxFormatoFecha.setEnabled(bEnabled);
		jComboBoxTipos.setEnabled(bEnabled);

		jCalendarButtonFechaInicial.setEnabled(bEnabled);
		jCalendarButtonFechaFinal.setEnabled(bEnabled);

		jComboBoxTipoAuto.setEnabled(bEnabled);
		jRadioButtonDividir.setEnabled(bEnabled);
		jRadioButtonPor.setEnabled(bEnabled);
		jTextFieldAtributo1.setEditable(bEnabled);
		jTextFieldAtributo2.setEditable(bEnabled);
		jTextFieldAuxiliar.setEditable(bEnabled);


		jButtonAceptar.setEnabled(bEnabled);
		jButtonCancelar.setEnabled(bEnabled);
		jButtonBorrarDominio.setEnabled(!bEnabled);
		jButtonEditarDominio.setEnabled(!bEnabled);
		jButtonNewDominio.setEnabled(!bEnabled);
		jTreeArbol.setEnabled(!bEnabled);
		jTreeArbolParticular.setEnabled(!bEnabled);


	}
	private void mostrarDomainNode(DomainNode auxNode)
	{
		ListaDomain lista;
		if (nodoParticular)
			lista=listaDomainParticular;
		else
			lista=listaDomain;
		jTextFieldDescripcion.setText(auxNode==null?"":auxNode.getTerm(Constantes.Locale));

		if (auxNode!=null && auxNode.getIdNode()!=null)
			jTextFieldID.setText(auxNode.getIdNode());        
		else
			jTextFieldID.setText("");

		jLabelID.setText(messages.getString("CDominiosFrame.jLabelID_DomainNode"));
		jComboBoxTipos.setSelectedIndex(auxNode==null?0:auxNode.getType());
		jComboBoxTipoAuto.setSelectedIndex(0);

		jTextFieldAnadir.setText("");
		jTextFieldPatronAnadir.setText("");
		idCodeEntrySelected=null;
		auxDomainCodeEntry=null;
		jTextFieldAtributo1.setText("");
		jTextFieldAtributo2.setText("");
		jTextFieldAuxiliar.setText("");
		if (auxNode==null)
		{
			jTextFieldDominioPadre.setText("");
			jTextFieldFechaFinal.setText("");
			jTextFieldFechaInicial.setText("");
			jTextFieldStringLongMaxima.setText("");
			jTextFieldPatron.setText("");
			jTextFieldValorMinimo.setText("");
			jTextFieldValorMaximo.setText("");
			actualizarModelo(null);

			return;
		}
		else if (auxNode.getIdParent()==null)
		{
			Domain auxDomain = lista.get(auxNode.getIdDomain());
			jTextFieldDominioPadre.setText(auxDomain.getName());

		}else
		{
			DomainNode auxNodePadre = lista.getNode(auxNode.getIdDomain(), auxNode.getIdParent());
			if (auxNodePadre!=null)
				jTextFieldDominioPadre.setText(auxNodePadre.getTerm(Constantes.Locale));
		}
		mostrarPatron(auxNode.getType(),auxNode.getPatron());
	}

	private void guardarCambios()
	{
		if (auxDomainNode==null) return;
		TipoDominio tipo=(TipoDominio)jComboBoxTipos.getSelectedItem();
		auxDomainNode.setType(tipo.getId());
		auxDomainNode.setTerm(Constantes.Locale, jTextFieldDescripcion.getText());
		auxDomainNode.setPatron(obtenerPatron(tipo.getId()));
	}
	private String obtenerPatron(int iTipo)
	{
		switch (iTipo)
		{
		case com.geopista.feature.Domain.PATTERN: return obtenerPatronString();
		case com.geopista.feature.Domain.CODEDENTRY: return obtenerPatronString();
		case com.geopista.feature.Domain.NUMBER: return obtenerPatronNumber();
		case com.geopista.feature.Domain.DATE: return obtenerPatronDate();
		case com.geopista.feature.Domain.AUTO: return obtenerPatronAuto();
		case com.geopista.feature.Domain.BOOLEAN: return obtenerPatronOptativo();
		case com.geopista.feature.Domain.CODEBOOK: return obtenerPatronOptativo();
		default: return null;
		}
	}
	private String obtenerPatronOptativo()
	{
		return (jCheckBoxObligatorio.isSelected()?"":"?");
	}
	private String obtenerPatronDate()
	{
		String sFechaInicial="";
		if (jTextFieldFechaInicial.getText()!=null&&jTextFieldFechaInicial.getText().length()>0)
			sFechaInicial=jTextFieldFechaInicial.getText();
		else
			sFechaInicial="NOW";
		String sFechaFinal="";
		if (jTextFieldFechaFinal.getText()!=null&&jTextFieldFechaFinal.getText().length()>0)
			sFechaFinal=jTextFieldFechaFinal.getText();
		else
			sFechaFinal="NOW";
		String  sFormatoFecha=(String)jComboBoxFormatoFecha.getSelectedItem();
		return (jCheckBoxObligatorio.isSelected()?"":"?")+"["+sFechaInicial+":"+sFechaFinal+"]"+sFormatoFecha;
	}
	private String obtenerPatronNumber()
	{
		String sValorMinimo="";
		if (jTextFieldValorMinimo.getText()!=null&&jTextFieldValorMinimo.getText().length()>0)
			try {sValorMinimo=jTextFieldValorMinimo.getNumber().toString();}catch(Exception e){sValorMinimo="";}
			else
				sValorMinimo="-INF";
		String sValorMaximo="";
		if (jTextFieldValorMaximo.getText()!=null&&jTextFieldValorMaximo.getText().length()>0)
			try {sValorMaximo=jTextFieldValorMaximo.getNumber().toString();}catch(Exception e){sValorMaximo="";}
			else
				sValorMaximo="INF";
		String  sFormato=(String)jComboBoxFormato.getSelectedItem();
		return (jCheckBoxObligatorio.isSelected()?"":"?")+"["+sValorMinimo+":"+sValorMaximo+"]"+sFormato;
	}

	private String obtenerPatronString()
	{
		String sAuxPatron;
		Integer iLongitud=null;
		if (jTextFieldPatron.getText().length()>0)
			return jTextFieldPatron.getText();
		try
		{
			iLongitud=new Integer(jTextFieldStringLongMaxima.getNumber().intValue());
		}catch(Exception e)
		{}
		if (iLongitud!=null&&iLongitud.intValue()>0)
			sAuxPatron=iLongitud+"[.*]";
		else
			sAuxPatron="[.*]";
		return (jCheckBoxObligatorio.isSelected()?"":"?")+sAuxPatron;
	}
	private String obtenerPatronAuto()
	{
		try
		{
			switch (jComboBoxTipoAuto.getSelectedIndex())
			{
			case 0: return (jCheckBoxObligatorio.isSelected()?"":"?")+(String)jComboBoxTipoAuto.getSelectedItem();
			case 1: return (jCheckBoxObligatorio.isSelected()?"":"?")+AUTOLENGHT;
			case 2: 
				//String sPatron=((String)jComboBoxTipoAuto.getSelectedItem())+":"+
				//( (jTextFieldAtributo1.getText().length()==0)?"0":""+jTextFieldAtributo1.getNumber().doubleValue())
				//+ (jRadioButtonDividir.isSelected()?"/":"*")+
				//((jTextFieldAtributo1.getText().length()==0)?"0":""+jTextFieldAtributo2.getNumber().doubleValue());
				//return (jCheckBoxObligatorio.isSelected()?"":"?")+sPatron;
				return (jCheckBoxObligatorio.isSelected()?"":"?")+(String)jComboBoxTipoAuto.getSelectedItem()+":"+jTextFieldAuxiliar.getText();

			case 3: return (jCheckBoxObligatorio.isSelected()?"":"?")+(String)jComboBoxTipoAuto.getSelectedItem();
			case 4: return (jCheckBoxObligatorio.isSelected()?"":"?")+(String)jComboBoxTipoAuto.getSelectedItem()+":"+jTextFieldAuxiliar.getText();

			}
			return null;
		}catch(Exception e)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error al obtener el formato auto: Excepcion:"+sw.toString());
			return null;
		}
	}

	private void mostrarPatron(int iTipo,String sPatron)
	{
		try
		{
			if (sPatron!=null && sPatron.length()>0&& optativo.equals(sPatron.substring(0,1)))
			{
				jCheckBoxObligatorio.setSelected(false);
				sPatron=sPatron.substring(1);
			}
			else
				jCheckBoxObligatorio.setSelected(true);
			switch (iTipo)
			{
			case com.geopista.feature.Domain.DATE: mostrarPatronDate(sPatron);
			return;
			case com.geopista.feature.Domain.PATTERN:mostrarPatronString(sPatron);
			return;
			case com.geopista.feature.Domain.CODEDENTRY:mostrarPatronString(sPatron);
			return;
			case com.geopista.feature.Domain.NUMBER:mostrarPatronNumber(sPatron);
			return;
			case com.geopista.feature.Domain.CODEBOOK:mostrarPatronCodeBook();
			return;
			case com.geopista.feature.Domain.AUTO:mostrarPatronAuto(sPatron);
			return;
			default: return ;
			}
		}catch (Exception e)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error la mostrar el patro tipo: "+iTipo+" Patron: "+sPatron+" Excepcion:"+sw.toString());

		}
	}
	private void mostrarPatronDate(String sPatron)
	{
		try
		{
			if (sPatron==null) return;
			String sFechaInicial=sPatron.substring(sPatron.indexOf("[")+1,sPatron.indexOf(":"));
			System.out.println("FECHA INICIAL:"+sFechaInicial);
			String sFechaFinal=sPatron.substring(sPatron.indexOf(":")+1,sPatron.indexOf("]"));
			System.out.println("FECHA INICIAL:"+sFechaFinal);
			String sFormato=sPatron.substring(sPatron.indexOf("]")+1);
			System.out.println("FORMATO:"+sFormato);

			jComboBoxFormatoFecha.setSelectedItem(sFormato);

			if (!sFechaInicial.equals("NOW"))
			{
				SimpleDateFormat formatter= new SimpleDateFormat (sFormato);
				Date dateInicial = formatter.parse(sFechaInicial);
				Calendar calendarInicial= Calendar.getInstance();
				calendarInicial.setTime(dateInicial);
				jCalendarButtonFechaInicial.setFecha(calendarInicial);
			}
			if (!sFechaFinal.equals("NOW"))
			{
				SimpleDateFormat formatter= new SimpleDateFormat (sFormato);
				Date dateFinal = formatter.parse(sFechaFinal);
				Calendar calendarFinal= Calendar.getInstance();
				calendarFinal.setTime(dateFinal);
				jCalendarButtonFechaFinal.setFecha(calendarFinal);
			}


		}catch(Exception e)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error la mostrar el patron fecha: "+sPatron+" Excepcion:"+sw.toString());
		}
	}
	private void mostrarPatronString(String sPatron)
	{
		try
		{
			if (sPatron==null) return;
			Integer iLongMaxima;
			try
			{
				if (sPatron.indexOf("[")>=0)
				{
					String sFormato=sPatron.substring(0,sPatron.indexOf("["));
					//System.out.println("Formato String:"+sFormato);
					iLongMaxima= new Integer(sFormato);
					jTextFieldStringLongMaxima.setText(iLongMaxima.toString());
					jTextFieldPatron.setText("");
				}
				else
				{
					jTextFieldPatron.setText(sPatron);
					jTextFieldStringLongMaxima.setText("");
				}
				return;
			}catch(Exception e){}
			jTextFieldStringLongMaxima.setText("");
			jTextFieldPatron.setText("");
		}catch(Exception e)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error la mostrar el patron fecha: "+sPatron+" Excepcion:"+sw.toString());
		}
	}
	private void mostrarPatronNumber(String sPatron)
	{
		try
		{
			if (sPatron==null) return;
			try
			{
				String sValorMinimo=sPatron.substring(sPatron.indexOf("[")+1,sPatron.indexOf(":"));
				//System.out.println("ValorMinimo:"+sValorMinimo);
				jTextFieldValorMinimo.setText(sValorMinimo);
			}catch(Exception e){
				jTextFieldValorMinimo.setText("");
			}
			try
			{
				String sValorMaximo=sPatron.substring(sPatron.indexOf(":")+1,sPatron.indexOf("]"));
				//System.out.println("ValorMinimo:"+sValorMaximo);
				jTextFieldValorMaximo.setText(sValorMaximo);
			}catch(Exception e){
				jTextFieldValorMaximo.setText("");
			}
		}catch(Exception e)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error la mostrar el patron fecha: "+sPatron+" Excepcion:"+sw.toString());
		}
	}
	private void mostrarPatronAuto(String sPatron)
	{
		try
		{
			if (sPatron==null) return;
			if (sPatron.indexOf(AUTOAREA)>=0)  jComboBoxTipoAuto.setSelectedIndex(0);
			if (sPatron.indexOf(AUTOLENGHT)>=0) jComboBoxTipoAuto.setSelectedIndex(1);
			if (sPatron.indexOf(AUTOFORMULA)>=0)
			{
				jPanelAuxiliar.setVisible(true);

				int posicionComillas=sPatron.indexOf(":");
				if (posicionComillas>0)
					jTextFieldAuxiliar.setText(sPatron.substring(posicionComillas+1));

				jComboBoxTipoAuto.setSelectedIndex(2);
				/*
                 jPanelFormula.setVisible(true);
                 jComboBoxTipoAuto.setSelectedIndex(2);
                 if (sPatron.indexOf("*")>=0)
                 {
                 jRadioButtonPor.setSelected(true);
                 String sPrimer=sPatron.substring(sPatron.indexOf(AUTOFORMULA)+AUTOFORMULA.length()+1,sPatron.indexOf("*"));
                 jTextFieldAtributo1.setText(new Double(sPrimer).toString());
                 String sSegun=sPatron.substring(sPatron.indexOf("*")+1);
                 jTextFieldAtributo2.setText(new Double(sSegun).toString());
                 }
                 if (sPatron.indexOf("/")>=0)
                 {
                 jRadioButtonDividir.setSelected(true);
                 String sPrimer=sPatron.substring(sPatron.indexOf(AUTOFORMULA)+AUTOFORMULA.length()+1,sPatron.indexOf("/"));
                 jTextFieldAtributo1.setText(new Double(sPrimer).toString());
                 String sSegun=sPatron.substring(sPatron.indexOf("/")+1);
                 jTextFieldAtributo2.setText(new Double(sSegun).toString());
                 }
				 */
			}

			if (sPatron.indexOf(AUTOID)>=0)  jComboBoxTipoAuto.setSelectedIndex(3);
			if (sPatron.indexOf(AUTOENV_VAR)>=0) 
			{
				jPanelAuxiliar.setVisible(true);
				int posicionComillas=sPatron.indexOf(":");
				if (posicionComillas>0)
					jTextFieldAuxiliar.setText(sPatron.substring(posicionComillas+1));

				jComboBoxTipoAuto.setSelectedIndex(4);
			}

			/*Integer iLongMaxima;
             try
             {
             String sFormato=sPatron.substring(1,sPatron.indexOf("[")-1);
             //System.out.println("Formato String:"+sFormato);
             iLongMaxima= new Integer(sFormato);
             jTextFieldStringLongMaxima.setText(iLongMaxima.toString());
             return;
             }catch(Exception e){}
             jTextFieldStringLongMaxima.setText("");
			 */}
		catch(Exception e)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error la mostrar el patron fecha: "+sPatron+" Excepcion:"+sw.toString());
		}
	}
	private void aceptarActionPerformed()
	{
		try
		{
			ListaDomain lista;
			JTree arbol;
			if (nodoParticular)
			{
				lista=listaDomainParticular;
				arbol=jTreeArbolParticular;
			}
			else
			{
				lista=listaDomain;
				arbol=jTreeArbol;
			}

			guardarCambios();
			if (auxDomainNode!=null && auxDomainNode.getPatron()!=null && auxDomainNode.getPatron().length() > Constantes.PATRON_MAX_SIZE) {
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.nodopatronsize"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this, "");
				dialog.show();
				return;
			}
			if (auxDomainNode!=null && auxDomainNode.gethDict()!=null)
			{
				for (Enumeration e=auxDomainNode.gethDict().elements(); e.hasMoreElements();)
				{
					if (((String)e.nextElement()).length()>Constantes.NODE_DESCRIPTION_SIZE)
					{
						JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.highnodescripcion"),JOptionPane.INFORMATION_MESSAGE);
						JDialog dialog =optionPane.createDialog(this,"");
						dialog.show();
						return;
					}
				}
			}

			if (auxDomainNode.gethDict().size()==0)
			{
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.nodescripcion"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
				return;
			}
			if (auxDomainNode.getType()==0)
			{
				JOptionPane optionPane= new JOptionPane(messages.getString("CDominiosFrame.mensaje.notype"),JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
				return;
			}
			CResultadoOperacion result=null;
			String sMensaje="";

			if (modoEdicion)
			{
				result=(new OperacionesAdministrador(Constantes.url)).actualizarDomainNode(auxDomainNode);
				sMensaje=messages.getString("CDominiosFrame.mensaje.nodeeditok");
				nodeArbolSelected.setUserObject(auxDomainNode);
			}
			else
			{
				result=(new OperacionesAdministrador(Constantes.url)).nuevoDomainNode(auxDomainNode);
				sMensaje=messages.getString("CDominiosFrame.mensaje.nodenuevook");

			}
			if (result.getResultado())
			{
				enabledComponents(false);
				JOptionPane optionPane= new JOptionPane(sMensaje,JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
				Object nodeInfo = nodeArbolSelected.getUserObject();
				if(!modoEdicion)
				{
					auxDomainNode=(DomainNode)result.getVector().get(0);
					if (nodeInfo instanceof DomainNode) {
						DomainNode nodoPadre=(DomainNode)nodeInfo;
						nodoPadre.addHijo(auxDomainNode);

					} else if (nodeInfo instanceof Domain)
					{
						Domain domainPadre=(Domain)nodeInfo;
						domainPadre.addNode(auxDomainNode);
						int a = 0;
					}
					DefaultMutableTreeNode hojaDominio = new DefaultMutableTreeNode(auxDomainNode);
					DefaultTreeModel treeModel=(DefaultTreeModel)arbol.getModel();
					treeModel.insertNodeInto(hojaDominio, nodeArbolSelected,nodeArbolSelected.getChildCount());
					ListaDomainNode nuevaLista=new ListaDomainNode();
					for (Enumeration e=auxDomainNode.getlHijos().gethDom().elements();e.hasMoreElements();)
					{
						DomainNode nodoHijo= (DomainNode) e.nextElement();
						nodoHijo.setIdDomain(auxDomainNode.getIdDomain());
						nodoHijo.setIdParent(auxDomainNode.getIdNode());
						DefaultMutableTreeNode hojaDominioHijo = new DefaultMutableTreeNode(nodoHijo);
						treeModel.insertNodeInto(hojaDominioHijo,hojaDominio ,hojaDominio.getChildCount());
						nuevaLista.add(nodoHijo);
					}
					auxDomainNode.setlHijos(nuevaLista);
					arbol.scrollPathToVisible(new TreePath(hojaDominio.getPath()));
				}
				else
				{

					DomainNode auxNode=lista.getNode(auxDomainNode.getIdDomain(),auxDomainNode.getIdNode());
					if (auxNode!=null)
						auxNode.copy(auxDomainNode);
					//nodeArbolSelected.setUserObject(auxDomainNode);
					nodeArbolSelected.setUserObject(auxNode);

				}
				modoNuevo=false;
				modoEdicion=false;

			}
			else
			{
				JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();
			}

		}catch(Exception ex)
		{
			java.io.StringWriter sw=new java.io.StringWriter();
			java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Excepcion al añadir usuario: "+sw.toString());
			JOptionPane optionPane= new JOptionPane(ex.getMessage()+" "+ex.toString(),JOptionPane.ERROR_MESSAGE);
			JDialog dialog =optionPane.createDialog(this,"");
			dialog.show();
		}
	}
	private void pintaLeyenda()
	{
		try
		{
			jPanelLeyenda.setBackground(Color.white);
			jPanelLeyenda.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			Font font = new Font("Serif", Font.PLAIN, 30);
			jPanelLeyenda.setFont(font);
			jPanelLeyenda.add(new JLabel("StringDomain",TreeRendererDominios.stringIcon,JLabel.LEFT));
			jPanelLeyenda.add(new JLabel("TreeDomain",TreeRendererDominios.TreeIcon,JLabel.LEFT));
			jPanelLeyenda.add(new JLabel("NumberDomain",TreeRendererDominios.numberIcon,JLabel.LEFT));
			jPanelLeyenda.add(new JLabel("CodeBookDomain",TreeRendererDominios.CodeBookIcon,JLabel.LEFT));
			jPanelLeyenda.add(new JLabel("BooleanDomain",TreeRendererDominios.booleanIcon,JLabel.LEFT));
			jPanelLeyenda.add(new JLabel("DateDomain",TreeRendererDominios.DateIcon,JLabel.LEFT));
			jPanelLeyenda.add(new JLabel("CodeEntryDomain",TreeRendererDominios.CodeEntryIcon,JLabel.LEFT));
			jPanelLeyenda.add(new JLabel("AutoDomain",TreeRendererDominios.AutoIcon,JLabel.LEFT));
		}catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			JOptionPane.showMessageDialog(frame, "Excepcion al cargar la leyenda:\n"+sw.toString());
			logger.error("Exception: " + sw.toString());
		}
	}
	private void limpiarFechaFinal()
	{
		jTextFieldFechaFinal.setText("");
		jCalendarButtonFechaFinal.setFecha(null);
	}

	private void limpiarFechaInicial()
	{
		jTextFieldFechaInicial.setText("");
		jCalendarButtonFechaInicial.setFecha(null);
	}
	private void removeCurrentNode()
	{
		JTree arbol;
		if (nodoParticular)
			arbol=jTreeArbolParticular;
		else
			arbol=jTreeArbol;
		TreePath currentSelection = arbol.getSelectionPath();
		if (currentSelection != null) {
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
			(currentSelection.getLastPathComponent());
			MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
			if (parent != null) {
				((DefaultTreeModel)arbol.getModel()).removeNodeFromParent(currentNode);
				return;
			}
		}
	}

	private void mostrarPatronCodeBook()
	{
		ListaDomain lista;
		if (nodoParticular)
			lista=listaDomainParticular;
		else
			lista=listaDomain;

		Object nodeInfo = nodeArbolSelected.getUserObject();
		if (!(nodeInfo instanceof DomainNode)) return;
		DomainNode auxDomainNode=(DomainNode) nodeInfo;
		if (auxDomainNode.getType()!=com.geopista.feature.Domain.CODEBOOK) return;
		auxDomainNode=lista.getNode(auxDomainNode.getIdDomain(), auxDomainNode.getIdNode());
		actualizarModelo(auxDomainNode.getlHijos());
	}
	private void actualizarModelo(ListaDomainNode lista)
	{      modelCodeBook= new CodeBookTableModel();
	modelCodeBook.setModelData(lista);
	sorter = new TableSorted(modelCodeBook);
	sorter.setTableHeader(jTableCodeNodes.getTableHeader());
	jTableCodeNodes.setModel(sorter);
	TableColumn column = jTableCodeNodes.getColumnModel().getColumn(RolesTableModel.idIndex);
	column.setPreferredWidth(5);
	column=jTableCodeNodes.getColumnModel().getColumn(RolesTableModel.idNombre);
	column.setPreferredWidth(15);
	jTableCodeNodes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.invalidate();
	this.validate();

	}
	private void  addCodeEntry()
	{
		if ((jTextFieldAnadir.getText()==null)||(jTextFieldAnadir.getText().length()==0)) return;
		if ((jTextFieldPatronAnadir.getText()==null)||(jTextFieldPatronAnadir.getText().length()==0)) return;
		DomainNode nodeHijo=new DomainNode(jTextFieldAnadir.getText(), null, com.geopista.feature.Domain.CODEDENTRY, null,(nodoParticular?com.geopista.security.SecurityManager.getIdEntidad():null), auxDomainNode.getIdDomain(),
				jTextFieldPatronAnadir.getText());
		if (auxDomainCodeEntry!=null)
		{
			nodeHijo.sethDict(auxDomainCodeEntry.gethDict());
		}
		nodeHijo.setTerm(Constantes.Locale,jTextFieldAnadir.getText());
		
		//Verificamos si el patron ya existe en el dominio
		if (auxDomainNode.getlHijos().gethDom()!=null){
			for (Enumeration e = auxDomainNode.getlHijos().gethDom().elements() ; e.hasMoreElements() ;) {
				DomainNode domain=(DomainNode)e.nextElement();
				//logger.info("Domain:"+domain.getPatron());
				if (domain.getPatron().equals(nodeHijo.getPatron())){
					 JOptionPane.showMessageDialog(frame, messages.getString("CDominiosFrame.jPanelDominio.duplicatepatron"));
						return;
				}
	     }
		}

		auxDomainNode.getlHijos().add(nodeHijo);
		auxDomainNode.setPatron(nodeHijo.getPatron());
		actualizarModelo(auxDomainNode.getlHijos());
		jTextFieldAnadir.setText("");
		jTextFieldPatronAnadir.setText("");
		jTextFieldAnadir.setFocusable(true);
		auxDomainCodeEntry=null;
	}
	private void  deleteCodeEntry()
	{
		actualizarModelo(auxDomainNode.getlHijos());
		if ((idCodeEntrySelected==null) || (auxDomainNode==null)) return;
		auxDomainNode.getlHijos().gethDom().remove(idCodeEntrySelected);
		actualizarModelo(auxDomainNode.getlHijos());
		idCodeEntrySelected=null;
	}
	private void seleccionarCodeEntry(ListSelectionEvent e)
	{
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		if (lsm.isSelectionEmpty()) { }
		else
		{
			int selectedRow = lsm.getMinSelectionIndex();
			idCodeEntrySelected=(String)sorter.getValueAt(selectedRow,CodeBookTableModel.idIndex);
		}
	}

	private JDialogDiccionario  showIdiomasDescripcion(DomainNode auxDomain)
	{
		JDialogDiccionario jDiccionario = new JDialogDiccionario(frame, true, auxDomain.gethDict(),modoNuevo,messages);
		jDiccionario.setSize(600,500);
		//jDiccionario.setResizable(false);
		//jDiccionario.pack();
		jDiccionario.setLocationRelativeTo(this);
		jDiccionario.show();
		return jDiccionario;

	}
	private void  cancelarActionPerformed()
	{
		modoNuevo=false;
		modoEdicion=false;
		enabledComponents(false);
		valueChanged(null);
	}

	public void showLeyenda()
	{
		jPanelLeyenda.setVisible(true);
		this.repaint();
		this.invalidate();
		this.validate();
	}
	public void hideLeyenda()
	{
		jPanelLeyenda.setVisible(false);
		this.repaint();
		this.invalidate();
		this.validate();
	}

	public void editable(boolean b)
	{
		editable=b;
		jButtonNewDominio.setEnabled(b);
		jButtonBorrarDominio.setEnabled(b);
		jButtonEditarDominio.setEnabled(b);
		jMenuItemDeleteDominioRoot.setEnabled(b);
		jMenuItemNewDominioRoot.setEnabled(b);
		jMenuItemDeleteDominio.setEnabled(b);
		jMenuItemNewDominio.setEnabled(b);
	}

	public void changeScreenLang(ResourceBundle messages) {
		try
		{
			this.messages=messages;
			jPanelArbolDominios.setBorder(new javax.swing.border.TitledBorder(messages.getString("CDominiosFrame.jPanelArbolDominios.title")));
			jPanelDominio.setBorder(new javax.swing.border.TitledBorder(messages.getString("CDominiosFrame.jPanelDominio.title")));
			jPanelLeyenda.setBorder(new javax.swing.border.TitledBorder(messages.getString("CDominiosFrame.jPanelLeyenda.title")));
			jButtonBorrarDominio.setText(messages.getString("CDominiosFrame.jButtonBorrarDominio"));
			jButtonEditarDominio.setText(messages.getString("CDominiosFrame.jButtonEditarDominio"));
			jButtonNewDominio.setText(messages.getString("CDominiosFrame.jButtonNewDominio"));
			jButtonCancelar.setText(messages.getString("CDominiosFrame.jButtonCancelar"));
			jButtonAceptar.setText(messages.getString("CDominiosFrame.jButtonAceptar"));
			jLabelStringLongMax.setText(messages.getString("CDominiosFrame.jLabelStringLongMax"));
			jLabelPatron.setText(messages.getString("CDominiosFrame.jLabelPatron"));
			jLabelNombreDominio.setText(messages.getString("CDominiosFrame.jLabelNombreDominio"));
			jLabelTipo.setText(messages.getString("CDominiosFrame.jLabelTipo"));
			jLabelDescripcion.setText(messages.getString("CDominiosFrame.jLabelDescripcion"));
			jLabelID.setText(messages.getString("CDominiosFrame.jLabelID_Domain"));
			jLabelValorMinimo.setText(messages.getString("CDominiosFrame.jLabelValorMinimo"));
			jLabelValorMaximo.setText(messages.getString("CDominiosFrame.jLabelValorMaximo"));
			jLabelFormato.setText(messages.getString("CDominiosFrame.jLabelFormato"));
			jLabelFechaFinal.setText(messages.getString("CDominiosFrame.jLabelFechaFinal"));
			jLabelFechaInicial.setText(messages.getString("CDominiosFrame.jLabelFechaInicial"));
			jButtonAnadir.setText(messages.getString("CDominiosFrame.jButtonAñadir"));
			jButtonBorrar.setText(messages.getString("CDominiosFrame.jButtonBorrar"));
			jLabelTipoAuto.setText(messages.getString("CDominiosFrame.jLabelTipoAuto"));
			jLabelTipoAtributo1.setText(messages.getString("CDominiosFrame.jLabelAtributo1"));
			jLabelTipoAtributo2.setText(messages.getString("CDominiosFrame.jLabelAtributo2"));
			jLabelDescripcionAnadir.setText(messages.getString("CDominiosFrame.jLabelDescripcionAnadir"));
			jLabelPatronAnadir.setText(messages.getString("CDominiosFrame.jLabelPatronAnadir"));
			jMenuItemNewDominioRoot.setText(messages.getString("CDominiosFrame.jMenuNewDominioRoot"));
			jMenuItemDeleteDominioRoot.setText(messages.getString("CDominiosFrame.jMenuDeleteDominioRoot"));
			jMenuItemNewDominio.setText(messages.getString("CDominiosFrame.jMenuNewDominio"));
			jMenuItemDeleteDominio.setText(messages.getString("CDominiosFrame.jMenuDeleteDominio"));
			jCheckBoxObligatorio.setText(messages.getString("CDominiosFrame.jCheckBoxObligatorio"));

			jButtonAceptar.setToolTipText(messages.getString("CDominiosFrame.jButtonAceptar"));
			jButtonAnadir.setToolTipText(messages.getString("CDominiosFrame.jButtonAñadir"));
			jButtonBorrar.setToolTipText(messages.getString("CDominiosFrame.jButtonBorrar"));
			jButtonBorrarDominio.setToolTipText(messages.getString("CDominiosFrame.jButtonBorrarDominio"));
			jButtonEditarDominio.setToolTipText(messages.getString("CDominiosFrame.jButtonEditarDominio"));
			jButtonCancelar.setToolTipText(messages.getString("CDominiosFrame.jButtonCancelar"));
			jButtonDesIdiomas.setToolTipText(messages.getString("CDominiosFrame.jButtonDesIdiomas"));
			jButtonNewDominio.setToolTipText(messages.getString("CDominiosFrame.jButtonNewDominio"));
			jButtonNodosIdiomas.setToolTipText(messages.getString("CDominiosFrame.jButtonNodosIdiomas"));
			jButtonLimpiarFechaInicial.setToolTipText(messages.getString("CDominiosFrame.jButtonLimpiarFechaInicial"));
			jButtonLimpiarFechaFinal.setToolTipText(messages.getString("CDominiosFrame.jButtonLimpiarFechaFinal"));
		}catch(Exception e)
		{
			logger.error("Falta alguna etiqueta:",e);
		}
	}


	private javax.swing.JButton jButtonAceptar;
	private javax.swing.JButton jButtonAnadir;
	private javax.swing.JButton jButtonBorrar;
	private javax.swing.JButton jButtonBorrarDominio;
	private javax.swing.JButton jButtonEditarDominio;
	private javax.swing.JButton jButtonCancelar;
	private javax.swing.JButton jButtonDesIdiomas;
	private javax.swing.JButton jButtonNewDominio;
	private javax.swing.JButton jButtonNodosIdiomas;

	private javax.swing.JComboBox jComboBoxTipos;
	private javax.swing.JComboBox jComboBoxFormato;
	private javax.swing.JComboBox jComboBoxFormatoFecha;
	private javax.swing.JLabel jLabelStringLongMax;
	private javax.swing.JLabel jLabelPatron;
	private com.geopista.app.utilidades.TextField jTextFieldPatron;
	private javax.swing.JLabel jLabelDescripcion;
	private javax.swing.JLabel jLabelFormato;
	private javax.swing.JLabel jLabelID;
	private javax.swing.JLabel jLabelNombreDominio;
	private com.geopista.app.utilidades.TextField jTextFieldDominioPadre;
	private javax.swing.JLabel jLabelTipo;
	private javax.swing.JLabel jLabelValorMaximo;
	private javax.swing.JLabel jLabelValorMinimo;
	private javax.swing.JLabel jLabelFechaInicial;
	private javax.swing.JLabel jLabelFechaFinal;
	private javax.swing.JLabel jLabelFormatoFecha;
	private javax.swing.JPanel jPanelAceptarCancelar;
	private javax.swing.JPanel jPanelArbolDominios;
	private javax.swing.JPanel jPanelBooleanDomain;
	private javax.swing.JPanel jPanelCabecera;
	private javax.swing.JPanel jPanelCodeBook;
	private javax.swing.JPanel jPanelNada;
	private javax.swing.JPanel jPanelDominio;
	private javax.swing.JPanel jPanelLeyenda;
	private javax.swing.JPanel jPanelNuevoBorrar;
	private javax.swing.JPanel jPanelNumberDomain;
	private javax.swing.JPanel jPanelDateDomain;
	private javax.swing.JPanel jPanelStringDomain;
	private javax.swing.JScrollPane jScrollPaneArbol;
	private javax.swing.JScrollPane jScrollPaneArbolParticular;
	private javax.swing.JScrollPane jScrollPaneNodos;
	private javax.swing.JTable jTableCodeNodes;
	private JNumberTextField jTextFieldValorMinimo;
	private com.geopista.app.utilidades.TextField jTextFieldAnadir;
	private com.geopista.app.utilidades.TextField jTextFieldPatronAnadir;
	private javax.swing.JLabel jLabelDescripcionAnadir;
	private javax.swing.JLabel jLabelPatronAnadir;
	private com.geopista.app.utilidades.TextField jTextFieldDescripcion;
	private com.geopista.app.utilidades.TextField jTextFieldID;
	private com.geopista.app.utilidades.JNumberTextField jTextFieldStringLongMaxima;
	private com.geopista.app.utilidades.JNumberTextField jTextFieldValorMaximo;
	private javax.swing.JFormattedTextField jTextFieldFechaInicial;
	private javax.swing.JFormattedTextField jTextFieldFechaFinal;
	private com.geopista.app.utilidades.CalendarButton jCalendarButtonFechaInicial;
	private CalendarButton jCalendarButtonFechaFinal;
	private javax.swing.JButton jButtonLimpiarFechaInicial;
	private javax.swing.JButton jButtonLimpiarFechaFinal;
	private javax.swing.JTree jTreeArbol;
	private javax.swing.JTree jTreeArbolParticular;
	private javax.swing.JPanel jPanelAutoDomain;
	private javax.swing.ButtonGroup buttonGroupTipoFormula;
	private javax.swing.JComboBox jComboBoxTipoAuto;
	private javax.swing.JLabel jLabelTipoAtributo1;
	private javax.swing.JLabel jLabelTipoAtributo2;
	private javax.swing.JLabel jLabelTipoAuto;

	private javax.swing.JPanel jPanelAuxiliar;
	private javax.swing.JLabel jLabelAuxiliar;
	private JTextField jTextFieldAuxiliar;

	//private javax.swing.JPanel jPanelFormula;   
	private javax.swing.JRadioButton jRadioButtonDividir;
	private javax.swing.JRadioButton jRadioButtonPor;
	private JNumberTextField jTextFieldAtributo1;
	private JNumberTextField jTextFieldAtributo2;
	private JPopupMenu jPopupMenuDominios;
	private JMenuItem jMenuItemNewDominioRoot;
	private JMenuItem jMenuItemDeleteDominioRoot;
	private JMenuItem jMenuItemNewDominio;
	private JMenuItem jMenuItemDeleteDominio;
	private JCheckBox jCheckBoxObligatorio;



	/**
	 * @return Returns the jTreeArbol.
	 */
	public javax.swing.JTree getJTreeArbol()
	{
		return jTreeArbol;
	}
	/**
	 * @param treeArbol The jTreeArbol to set.
	 */
	public void setJTreeArbol(javax.swing.JTree treeArbol)
	{
		jTreeArbol = treeArbol;
	}
	/**
	 * @return Returns the jTreeArbolParticular.
	 */
	public javax.swing.JTree getJTreeArbolParticular()
	{
		return jTreeArbolParticular;
	}
	/**
	 * @param treeArbolParticular The jTreeArbolParticular to set.
	 */
	public void setJTreeArbolParticular(javax.swing.JTree treeArbolParticular)
	{
		jTreeArbolParticular = treeArbolParticular;
	}
	
	/**
	 * 
	 */
	public void dispose(){
		try{
			removeListaDomain(listaDomain);
			removeListaDomain(listaDomainParticular);
		}catch (Exception e) {
		}
		
	}
	private void removeListaDomain(ListaDomain lista){
		if (lista==null) return;
	    for (Enumeration e=lista.keys();e.hasMoreElements();){
			String idDomain=(String)e.nextElement();
			Domain domain = (Domain)lista.get(idDomain);
			for (Enumeration e2=domain.getListaNodes().gethDom().keys();e.hasMoreElements();){
				String idDomainNode=(String)e2.nextElement();
				DomainNode domainNode=(DomainNode)domain.getListaNodes().gethDom().get(idDomainNode);
				removeDomainNode(domainNode);
				domain.getListaNodes().gethDom().remove(idDomainNode);
			}
			domain=null; 
			lista.remove(idDomain);
		}
	//    lista.remove();
	}
	private void removeDomainNode(DomainNode domainNode){
		if (domainNode==null)return;
		if (domainNode.getlHijos()==null) return;
		for (Enumeration e=domainNode.getlHijos().gethDom().keys();e.hasMoreElements();){
			String idDomainNode=(String)e.nextElement();
			DomainNode hijo=(DomainNode)domainNode.getlHijos().gethDom().get(idDomainNode);
			domainNode.getlHijos().gethDom().remove(idDomainNode);
			removeDomainNode(hijo);
		}
		domainNode=null;
	}
}
