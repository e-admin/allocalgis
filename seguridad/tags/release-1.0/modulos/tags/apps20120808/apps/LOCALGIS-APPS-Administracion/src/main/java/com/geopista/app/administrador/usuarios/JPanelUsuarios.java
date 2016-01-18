package com.geopista.app.administrador.usuarios;

/** * 
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

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-may-2004
 * Time: 19:14:20
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.acl.AclNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.entidades.ComboBoxRendererEntidad;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Acl;
import com.geopista.protocol.administrador.App;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.ListaAcl;
import com.geopista.protocol.administrador.ListaApp;
import com.geopista.protocol.administrador.ListaEntidades;
import com.geopista.protocol.administrador.ListaPermisos;
import com.geopista.protocol.administrador.ListaRoles;
import com.geopista.protocol.administrador.ListaUsuarios;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.Permiso;
import com.geopista.protocol.administrador.Rol;
import com.geopista.protocol.administrador.Usuario;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPrincipal;

public class JPanelUsuarios extends javax.swing.JPanel {
	Logger logger = Logger.getLogger(JPanelUsuarios.class);
	private ListaUsuarios listaUsuarios = null;
	private ListaRoles listaRoles = null;
	private ListaAcl listaAcl = null;
	private ListaApp listaApp = null;	
	private ListaEntidades listaEntidades = null;
	private Acl aclSelected = null;
	private App appSelected = null;
	private Entidad entidadSelected = null;
	private TableSorted sorter;
	private UsuariosTableModel modelUsuarios;
	private boolean modoEdicion = false;
	private boolean modoNuevo = false;
	private Usuario usuarioSelected = null;
	private Usuario auxUsuario = null;
	private ResourceBundle mensajesUsuario;
	private static final String IDPERM_CODE = "idPerm";
	private static final String IDROL_CODE = "idRol";
	private ListaGroupPermisos listaGruposPermisos;
	private JFrame framePadre;
	private boolean passwordCambiado = false;
	private boolean userCambiado = false;
	private final int TAMANIO_MINIMO_PASSWORD = 8;
	private final int TAMANIO_MINIMO_USER = 8;
	/**
	 * Id del rol que deben tener todos los usuarios por defecto cuando se crean
	 */
	private final static String ID_ROL_EDICION_DATOS_PERSONALES = "111";
	private boolean permisoEdicionCompleta = false;
	private boolean permisoEdicionParcial = false;
	
	public JPanelUsuarios(ResourceBundle messages, JFrame framePadre) {
		this.framePadre = framePadre;
		initComponents(messages);
	}

	private void initComponents(ResourceBundle messages) {// GEN-BEGIN:initComponents

		mensajesUsuario = messages;
		// setLayout(new java.awt.BorderLayout());
		jPanelEditarUsuario = new javax.swing.JPanel();
		jPanelListaUsuarios = new javax.swing.JPanel();
		jTableUsuarios = new javax.swing.JTable();
		jButtonUsuAnadir = new javax.swing.JButton();
		jButtonUsuEliminar = new javax.swing.JButton();
		jButtonUsuEliminar.setEnabled(false);
		jButtonVerPermisos = new javax.swing.JButton();
		jButtonVerPermisos.setEnabled(false);
		jButtonEditar = new javax.swing.JButton();
		jButtonEditar.setEnabled(false);
		jLabelNombreUsuario = new javax.swing.JLabel();
		jLabelUsuDes = new javax.swing.JLabel();
		jTextFieldUsuNombre = new com.geopista.app.utilidades.TextField(32);
		jTextFieldUsuDes = new com.geopista.app.utilidades.TextField(254);
		jTextFieldUsuNif = new com.geopista.app.utilidades.TextField(64);
		jTextFieldUsuMail = new com.geopista.app.utilidades.TextField(64);
		jTextFieldBusquedaUsuario = new com.geopista.app.utilidades.TextField(64);
		jLabelContrasena = new javax.swing.JLabel();
		jTextFieldPass = new javax.swing.JPasswordField();		
		jLabelBusquedaUsuario = new javax.swing.JLabel();
		jLabelRolUsuario = new javax.swing.JLabel();
		jLabelACLUsuario = new javax.swing.JLabel();
		jLabelAppUsuario = new javax.swing.JLabel();
		jLabelConfirContra = new javax.swing.JLabel();
		jTextFieldRePass = new javax.swing.JPasswordField();
		jComboBoxACLs = new javax.swing.JComboBox();
		jComboBoxApps = new javax.swing.JComboBox();
		jComboBoxEntidades = new javax.swing.JComboBox();
		jButtonPermitir = new javax.swing.JButton();
		jButtonDenegar = new javax.swing.JButton();
		jButtonLimpiar = new javax.swing.JButton();
		jButtonCancelar = new javax.swing.JButton();
		jButtonUsuAceptar = new javax.swing.JButton();
		jTextFieldNombreCompleto = new com.geopista.app.utilidades.TextField(254);
		jLabelNombreCompleto = new javax.swing.JLabel();
		jLabelBloqueado = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jLabelOrganizacion = new javax.swing.JLabel();
		jLabelMail = new javax.swing.JLabel();
		jLabelNif = new javax.swing.JLabel();
		jLabelEntidad = new javax.swing.JLabel();
		jComboBoxDepartamento = new javax.swing.JComboBox();
		jComboBoxOrganizacion = new javax.swing.JComboBox();
		jLabelDepartamento = new javax.swing.JLabel();
		jLabelPermisos = new javax.swing.JLabel();
		jScrollPaneUsuPer = new javax.swing.JScrollPane();
		jPanelPermisosUsuario = new javax.swing.JPanel();
		jPanelPermisosTotal = new javax.swing.JPanel();
		bloqueadoJCheckBox = new JCheckBox();
        
		jScrollPaneUsuRoles = new javax.swing.JScrollPane();
		jPanelRolesUsuario = new javax.swing.JPanel();
		jButtonConexiones = new javax.swing.JButton();
		jButtonUsuarioTodos = new javax.swing.JButton();

		jButtonConexiones
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						mostrarConexiones();
					}
				});

		listaUsuariosJScrollPane = new javax.swing.JScrollPane();

		setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanelListaUsuarios
				.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jButtonUsuEliminar
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						eliminarUsuarioActionListener();
					}
				});
		jPanelListaUsuarios.add(jButtonUsuEliminar,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 520, 75,
						-1));

		jButtonEditar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editarUsuario();
			}
		});
		jPanelListaUsuarios.add(jButtonEditar,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 520, 75,
						-1));

		jButtonVerPermisos.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						verPermisos();
					}
				});
		jPanelListaUsuarios.add(jButtonVerPermisos,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 520, -1,
						-1));

		listaUsuariosJScrollPane.setViewportView(jTableUsuarios);

		jPanelListaUsuarios.add(listaUsuariosJScrollPane,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 350,
						450));

		jButtonUsuAnadir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				anadirUsuarioActionPerformed();
			}
		});

		jPanelListaUsuarios.add(jButtonUsuAnadir,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 75,
						-1));

		add(jPanelListaUsuarios,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 370,
						555));
		jPanelEditarUsuario
				.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanelEditarUsuario.add(jLabelNombreUsuario,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 15, -1,
						-1));
		jTextFieldUsuNombre.setEditable(false);
		jTextFieldUsuNombre.addKeyListener(new KeyListener(){

			
			public void keyTyped(KeyEvent e) {
				userCambiado = true;
			}

			
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});
		
		jPanelEditarUsuario.add(jTextFieldUsuNombre,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 35, 120,
						-1));

		jPanelEditarUsuario.add(jLabelContrasena,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 15, -1,
						-1));
		
		jPanelListaUsuarios.add(jLabelBusquedaUsuario,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 28, 200,
						-1));
		
		jTextFieldBusquedaUsuario.addKeyListener(new KeyListener(){

			
			public void keyTyped(KeyEvent e) {
			}

			
			public void keyPressed(KeyEvent e) {				
			}

			
			public void keyReleased(KeyEvent e) {			
				actualizarModelo(jTextFieldBusquedaUsuario.getText());
			}
		});		
		
		jPanelListaUsuarios.add(jTextFieldBusquedaUsuario,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 25, 200,
						-1));
		
		jButtonUsuarioTodos.addMouseListener(new MouseListener(){
			
			public void mouseClicked(MouseEvent e) {
				jTextFieldBusquedaUsuario.setText("");
				actualizarModelo("");
			}

			
			public void mousePressed(MouseEvent e) {
			}

			
			public void mouseReleased(MouseEvent e) {
			}

			
			public void mouseEntered(MouseEvent e) {
			}

			
			public void mouseExited(MouseEvent e) {
			}
			
		});
		
		jPanelListaUsuarios.add(jButtonUsuarioTodos,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 24, 80,
						-1));
		
		jTextFieldPass.setEditable(false);
		jTextFieldPass.addKeyListener(new KeyListener(){

			
			public void keyTyped(KeyEvent e) {
				passwordCambiado = true;
			}

			
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});
		jPanelEditarUsuario.add(jTextFieldPass,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 35, 130,
						-1));
		jPanelEditarUsuario.add(jLabelRolUsuario,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 180,
						110));
		jPanelEditarUsuario.add(jLabelAppUsuario,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 258, 190,
						-1));
		jPanelEditarUsuario.add(jLabelACLUsuario,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 288, 190,
						-1));
		jPanelEditarUsuario.add(jLabelConfirContra,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 15, -1,
						-1));
		jTextFieldRePass.setEditable(false);
		jTextFieldRePass.addKeyListener(new KeyListener(){

			
			public void keyTyped(KeyEvent e) {
				passwordCambiado = true;
			}

			
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});
		jPanelEditarUsuario.add(jTextFieldRePass,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 35, 130,
						-1));

		jPanelEditarUsuario.add(jComboBoxApps,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 255, 200,
						-1));
		// jComboBoxACLs.setEnabled(false);
		jPanelEditarUsuario.add(jComboBoxACLs,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 285, 200,
						-1));
		jPanelEditarUsuario.add(jButtonConexiones,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 253,
						180, -1));

		bloqueadoJCheckBox.setSelected(false);
		bloqueadoJCheckBox.setEnabled(false);
		jPanelEditarUsuario.add(bloqueadoJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 65, -1,
				-1));
        
		jButtonPermitir.setEnabled(false);
		jButtonPermitir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				marcar("Permitir");
			}
		});
		jButtonDenegar.setEnabled(false);
		jPanelEditarUsuario.add(jButtonPermitir,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 310, 125,
						-1));

		jButtonDenegar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				marcar("Denegar");
			}
		});
		jPanelEditarUsuario.add(jButtonDenegar,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 310,
						125, -1));

		jButtonLimpiar.setEnabled(false);
		jButtonLimpiar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				marcar("Rol");
			}
		});
		jPanelEditarUsuario.add(jButtonLimpiar,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 310,
						120, -1));

		jButtonCancelar.setEnabled(false);
		jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelarActionPerformed();
			}
		});

		jPanelEditarUsuario.add(jButtonCancelar,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 525,
						110, -1));
		jButtonUsuAceptar.setEnabled(false);
		jButtonUsuAceptar
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						aceptarActionPerformed();
					}
				});

		jPanelEditarUsuario.add(jButtonUsuAceptar,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 525,
						110, -1));

		jTextFieldNombreCompleto.setEditable(false);
		jPanelEditarUsuario.add(jTextFieldNombreCompleto,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 65, 150,
						-1));
		jPanelEditarUsuario.add(jLabelNombreCompleto,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 65, -1,
						-1));
		jTextFieldUsuNombre.setEditable(false);
		jPanelEditarUsuario.add(jLabelUsuDes,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 95, -1,
						-1));
		jPanelEditarUsuario.add(jLabelBloqueado,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 65, -1,
						-1));
		jTextFieldUsuNombre.setEditable(false);
		jTextFieldUsuDes.setEditable(false);
		jTextFieldUsuNif.setEditable(false);
		jTextFieldUsuMail.setEditable(false);
		jComboBoxEntidades.setEnabled(false);
		jPanelEditarUsuario.add(jTextFieldUsuDes,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 95, 320,
						-1));

		jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		// jPanel1.setBorder(new javax.swing.border.EtchedBorder());
		jLabelMail.setText("Email:");
		jLabelNif.setText("Nif:");
		jPanel1.add(jLabelMail,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 150,
						-1));
		jPanel1.add(jLabelNif,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 150,
						-1));
		jPanel1.add(jLabelEntidad,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 95, 150,
						-1));
		// jComboBoxDepartamento.setEnabled(false);
		// jPanel1.add(jComboBoxDepartamento, new
		// org.netbeans.lib.awtextra.AbsoluteConstraints(10, 75, 150, -1));
		jPanel1.add(jTextFieldUsuNif,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 150,
						-1));
		jPanel1.add(jTextFieldUsuMail,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 150,
						-1));
		jPanel1.add(jComboBoxEntidades,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 115, 150,
						-1));
		// jComboBoxOrganizacion.setEnabled(false);
		// jPanel1.add(jComboBoxOrganizacion, new
		// org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 150, -1));
		// jPanel1.add(jLabelDepartamento, new
		// org.netbeans.lib.awtextra.AbsoluteConstraints(10, 55, -1, -1));
		jPanelEditarUsuario.add(jPanel1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1,
						-1));
		jPanelEditarUsuario.add(jLabelPermisos,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 325, -1,
						-1));
		jScrollPaneUsuPer.setBackground(new java.awt.Color(255, 255, 255));
		jScrollPaneUsuPer.setBorder(new javax.swing.border.LineBorder(
				new java.awt.Color(0, 0, 0)));
		jPanelPermisosUsuario
				.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanelPermisosTotal.setLayout(new BorderLayout());
		jPanelPermisosTotal.setBackground(new java.awt.Color(255, 255, 255));
		jPanelPermisosTotal.add(jPanelPermisosUsuario, BorderLayout.EAST);
		jPanelPermisosUsuario.setBackground(new java.awt.Color(255, 255, 255));
		jScrollPaneUsuPer.setViewportView(jPanelPermisosTotal);
		jPanelEditarUsuario.add(jScrollPaneUsuPer,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 450,
						180));

		jScrollPaneUsuRoles.setBackground(new java.awt.Color(255, 255, 255));
		jScrollPaneUsuRoles.setBorder(new javax.swing.border.LineBorder(
				new java.awt.Color(0, 0, 0)));
		jPanelRolesUsuario
				.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanelRolesUsuario.setBackground(new java.awt.Color(255, 255, 255));
		jScrollPaneUsuRoles.setViewportView(jPanelRolesUsuario);

		jPanelEditarUsuario.add(jScrollPaneUsuRoles,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 135,
						200, 110));

		jPanelDescripcion.setBackground(Color.WHITE);
		jPanelDescripcion
				.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanelPermisosTotal.add(jPanelDescripcion, BorderLayout.WEST);
		add(jPanelEditarUsuario,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, 500,
						555));

		// Para seleccionar una fila
		ListSelectionModel rowSM = jTableUsuarios.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				seleccionarUsuario(e);
			}
		});
	}

	public void editable(boolean b) {
		jButtonUsuAnadir.setEnabled(b);
		// jButtonUsuEliminar.setEnabled(b);
		// jButtonVerPermisos.setEnabled(b);

		/**
		 * Incidencia [308]: si un usuario tiene el permiso
		 * Geopista.Administracion.View, independientemente de que tenga permiso
		 * de edicion (Geopista.Administracion.Edit), el boton Ver Permisos debe
		 * estar habilitado.
		 */

		/**
		 * Si mostramos el panel de usuarios es porque tenemos el permiso de
		 * Geopista.Administracion.View (opcion de menu Gestion de Usuarios
		 * habilitada). Aun asi, hacemos la comprobacion.
		 */
		try {
			com.geopista.security.GeopistaAcl aclAdministracion = com.geopista.security.SecurityManager
					.getPerfil("Administracion");
			jButtonVerPermisos.setEnabled(aclAdministracion
							.checkPermission(new com.geopista.security.GeopistaPermission(
									com.geopista.security.GeopistaPermission.VER_ADMINITRACION)));
		} catch (Exception e) {
			jButtonVerPermisos.setEnabled(true);
		}
	}

	public void changeScreenLang(ResourceBundle messages) {
		mensajesUsuario = messages;
		jPanelListaUsuarios.setBorder(new javax.swing.border.TitledBorder(
				messages.getString("CUsuariosFrame.jPanelListaUsuarios")));
		jPanelEditarUsuario.setBorder(new javax.swing.border.TitledBorder(
				messages.getString("CUsuariosFrame.jPanelEditarUsuario")));
		jButtonUsuAnadir.setText(messages
				.getString("CUsuariosFrame.jButtonUsuAnadir"));
		jButtonUsuEliminar.setText(messages
				.getString("CUsuariosFrame.jButtonUsuEliminar"));
		jButtonEditar.setText(messages
				.getString("CUsuariosFrame.jButtonEditar"));
		jButtonVerPermisos.setText(messages
				.getString("CUsuariosFrame.jButtonVerPermisos"));
		jLabelNombreUsuario.setText(messages
				.getString("CUsuariosFrame.jLabelNombreUsuario"));
		jLabelContrasena.setText(messages
				.getString("CUsuariosFrame.jLabelContrasena"));
		jLabelRolUsuario.setText(messages
				.getString("CUsuariosFrame.jLabelRolUsuario"));
		jLabelAppUsuario.setText(messages
				.getString("CUsuariosFrame.jLabelAppUsuario"));
		jLabelBusquedaUsuario.setText(messages
				.getString("CUsuariosFrame.jLabelBusquedaUsuario"));
		jLabelACLUsuario.setText(messages
				.getString("CUsuariosFrame.jLabelACLUsuario"));
		jLabelConfirContra.setText(messages
				.getString("CUsuariosFrame.jLabelConfirContra"));
		jLabelEntidad.setText(messages
				.getString("CUsuariosFrame.jLabelEntidad"));
		jButtonPermitir.setText(messages
				.getString("CUsuariosFrame.jButtonPermitir"));// Permitir
																// todos");
		jButtonUsuarioTodos.setText(messages
				.getString("CUsuariosFrame.jButtonUsuarioTodos"));
		jButtonDenegar.setText(messages
				.getString("CUsuariosFrame.jButtonDenegar"));// "Denegar todos");
		jButtonLimpiar.setText(messages
				.getString("CUsuariosFrame.jButtonLimpiar"));// "Limpiar");

		jButtonCancelar.setText(messages
				.getString("CUsuariosFrame.jButtonCancelar"));
		jButtonUsuAceptar.setText(messages
				.getString("CUsuariosFrame.jButtonUsuAceptar"));
		jLabelNombreCompleto.setText(messages
				.getString("CUsuariosFrame.jLabelNombreCompleto"));
		jLabelBloqueado.setText(messages
				.getString("CUsuariosFrame.jLabelBloqueado"));
		jLabelOrganizacion.setText(messages
				.getString("CUsuariosFrame.jLabelOrganizacion"));
		jLabelDepartamento.setText(messages
				.getString("CUsuariosFrame.jLabelDepartamento"));
		jLabelPermisos.setText(messages
				.getString("CUsuariosFrame.jLabelPermisos"));
		jLabelUsuDes.setText(messages.getString("CUsuariosFrame.jLabelUsuDes"));
		jButtonConexiones.setText(messages
				.getString("CUsuariosFrame.jButtonConexiones"));

		jButtonCancelar.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonCancelar"));
		jButtonEditar.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonEditar"));
		jButtonPermitir.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonPermitir"));
		jButtonDenegar.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonDenegar"));
		jButtonLimpiar.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonLimpiar"));
		jButtonUsuAceptar.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonUsuAceptar"));
		jButtonUsuAnadir.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonUsuAnadir"));
		jButtonUsuEliminar.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonUsuEliminar"));
		jButtonVerPermisos.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonVerPermisos"));
		jButtonConexiones.setToolTipText(messages
				.getString("CUsuariosFrame.jButtonConexiones"));

		TableColumn tableColumn = jTableUsuarios.getColumnModel().getColumn(0);
		tableColumn.setHeaderValue(messages.getString("CUsuariosFrame.col0"));
		tableColumn = jTableUsuarios.getColumnModel().getColumn(1);
		tableColumn.setHeaderValue(messages.getString("CUsuariosFrame.col1"));
		tableColumn = jTableUsuarios.getColumnModel().getColumn(2);
		tableColumn.setHeaderValue(messages.getString("CUsuariosFrame.col2"));

	}

	private void marcar(String titulo) {
		int iTotal = jPanelPermisosUsuario.getComponentCount();
		for (int i = 0; i < iTotal; i++) {
			Component auxComponent = jPanelPermisosUsuario.getComponent(i);
			if (auxComponent instanceof JRadioButton) {
				JRadioButton jRadioButton = (JRadioButton) auxComponent;
				if ((jRadioButton.getText() != null)
						&& jRadioButton.getText().equals(titulo))
					jRadioButton.setSelected(true);
			}
		}
	}

	//NUEVO
	private void jComboBoxAppsActionPerformed() {
		guardarCambios();
		appSelected = (App) jComboBoxApps.getSelectedItem();
		pintarAcls(appSelected.getAcls());
		inicializarPermisosUsuario(auxUsuario);
	}
	//FIN NUEVO
	
	private void jComboBoxACLsActionPerformed() {
		guardarCambios();
		aclSelected = (Acl) jComboBoxACLs.getSelectedItem();
		if(aclSelected!=null){
			pintarPermisosAcls(aclSelected.getPermisos());
			inicializarPermisosUsuario(auxUsuario);
		}
	}
	
	private void jComboBoxEntidadesActionPerformed() {
		if(aclSelected!=null)
			guardarCambios();
		entidadSelected = (Entidad) jComboBoxEntidades.getSelectedItem();
	}

	private void guardarCambios() {
		if (!modoEdicion)
			return;
		try {

			auxUsuario.setName(jTextFieldUsuNombre.getText());
			auxUsuario.setDescripcion(jTextFieldUsuDes.getText());
			auxUsuario.setNif(jTextFieldUsuNif.getText());
			auxUsuario.setEmail(jTextFieldUsuMail.getText());
			if (entidadSelected != null) {
				if (entidadSelected.getId().equals(""))
					auxUsuario.setId_entidad("0");
				else
					auxUsuario.setId_entidad(entidadSelected.getId());
			}
			auxUsuario.setNombreCompleto(jTextFieldNombreCompleto.getText());
			auxUsuario.setPassword(new String(jTextFieldPass.getPassword()));
			auxUsuario.setBloqueado(bloqueadoJCheckBox.isSelected());
			auxUsuario.setPasswordCambiado(passwordCambiado);
			auxUsuario.setUserCambiado(userCambiado);
			auxUsuario.setPermisoEdicionCompleta(permisoEdicionCompleta);

			Vector auxVectorGrupos = new Vector();
			int iTotal = jPanelRolesUsuario.getComponentCount();
			for (int i = 0; i < iTotal; i++) {
				Component auxComponent = jPanelRolesUsuario.getComponent(i);
				if (auxComponent instanceof JCheckBox) {
					JCheckBox jCheckBoxPermitir = (JCheckBox) auxComponent;
					if (jCheckBoxPermitir.isSelected()) {
						String sIdRol = (String) jCheckBoxPermitir
								.getClientProperty(IDROL_CODE);
						auxVectorGrupos.add(sIdRol);
					}
				}
			}
			auxUsuario.setGrupos(auxVectorGrupos);
			if (listaGruposPermisos == null)
				return;
			for (Enumeration e = listaGruposPermisos.gethGroupPermisos()
					.elements(); e.hasMoreElements();) {
				GroupPermisos auxGrupo = (GroupPermisos) e.nextElement();
				if (auxGrupo.getBotonRol().isSelected()) {
					// auxUsuario.getPermisos().remove(auxGrupo.getIdPerm());
					/** Incidencia [308] - acl distintos con los mismos idperm */
					auxUsuario.getPermisos().remove(auxGrupo.getIdPerm(),
							aclSelected.getId());
				} else {
					Permiso auxPermiso = new Permiso(auxGrupo.getIdPerm(),
							aclSelected.getId(), auxGrupo.getBotonPermitir()
									.isSelected());
					auxUsuario.getPermisos().add(auxPermiso);
				}
			}
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error al guardar los cambios:" + sw.toString());
		}

	}

	public void pintarUsuarios(ListaUsuarios listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
		actualizarModelo();
	}

	public void pintarRolesUsuario(ListaRoles listaRoles) {
		try {
			this.listaRoles = listaRoles;
			jPanelRolesUsuario.removeAll();
			if (listaRoles == null) {
				jPanelRolesUsuario.repaint();
				jPanelRolesUsuario.invalidate();
				jPanelRolesUsuario.validate();
				return;
			}
			
			RolComparator myComparator = new RolComparator();
			boolean sortRoles=true;
			Object[] myArray = listaRoles.gethRoles().entrySet().toArray();
			if (sortRoles){			
				//Object[] myArray = listaRoles.gethRoles().entrySet().toArray();
		        Arrays.sort(myArray,(Comparator)myComparator);
		 
			}
			
			
			int i = 0;
			//for (Enumeration e = listaRoles.gethRoles().elements(); e.hasMoreElements();) {
			for(int j=0;j<myArray.length;j++) {
				//Rol rol = (Rol) e.nextElement();
				Rol rol = (Rol)((Entry)myArray[j]).getValue();
				JCheckBox jCheckBoxRol = new JCheckBox();
				jCheckBoxRol.setBackground(new java.awt.Color(255, 255, 255));
				jCheckBoxRol.setEnabled(modoEdicion);
				jCheckBoxRol.putClientProperty(IDROL_CODE, rol.getId());
				jCheckBoxRol
						.addActionListener(new java.awt.event.ActionListener() {
							public void actionPerformed(
									java.awt.event.ActionEvent evt) {
								inicializaComboAcl();
							}
						});
				jPanelRolesUsuario.add(jCheckBoxRol,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(160,
								5 + i, -1, -1));
				JLabel jLabelRol = new JLabel();
				jLabelRol.setText(rol.getNombre());
				jPanelRolesUsuario.add(jLabelRol,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(10,
								5 + i, -1, -1));
				i += 20;
			}
			jPanelRolesUsuario.repaint();
			jPanelRolesUsuario.invalidate();
			jPanelRolesUsuario.validate();
			inicializarRolesUsuario(usuarioSelected);
		} catch (Exception e) {
			logger.error("Error al pedir los permisos:" + e.toString());
		}
	}
	
	//NUEVO
	public void pintarApps(ListaApp listaApps) {
		try {
			AppComparator myComparator = new AppComparator();
			this.listaApp = listaApps;
			Hashtable hApps = listaApps.gethApps();
			jComboBoxApps.addItem(new App("", "              "));

			boolean sortApp=true;
			if (sortApp){			
				Object[] myArray = hApps.entrySet().toArray();
		        Arrays.sort(myArray,(Comparator)myComparator);
		 
		        for(int i=0;i<myArray.length;++i) {
		        	App auxApp = (App)((Entry)myArray[i]).getValue();
		        	jComboBoxApps.addItem(auxApp);
		        }
			}
			else{			
				for (Enumeration e = hApps.elements(); e.hasMoreElements();) {
					App auxApp = (App) e.nextElement();
					jComboBoxApps.addItem(auxApp);
				}
			}
			
			jComboBoxApps.setRenderer(new ComboBoxRendererApp());
			jComboBoxApps
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jComboBoxAppsActionPerformed();
						}
					});

		} catch (Exception e) {
			logger.error("Error al pintar los acls: " + e.toString());
		}
	}
	//FIN NUEVO

	public void pintarAcls(ListaAcl listaAcls) {
		try {
			jComboBoxACLs.removeAllItems();
			AclComparator myComparator = new AclComparator();
			this.listaAcl = listaAcls;
			Hashtable hRoles = listaAcls.gethAcls();
			jComboBoxACLs.addItem(new Acl("", "              ",""));

			boolean sortAcl=true;
			if (sortAcl){			
				Object[] myArray = hRoles.entrySet().toArray();
		        Arrays.sort(myArray,(Comparator)myComparator);
		 
		        for(int i=0;i<myArray.length;++i) {
		        	Acl auxAcl = (Acl)((Entry)myArray[i]).getValue();
					jComboBoxACLs.addItem(auxAcl);
		        }
			}
			else{			
				for (Enumeration e = hRoles.elements(); e.hasMoreElements();) {
					Acl auxAcl = (Acl) e.nextElement();
					jComboBoxACLs.addItem(auxAcl);
				}
			}
			
			jComboBoxACLs.setRenderer(new ComboBoxRendererAcl());
			jComboBoxACLs
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jComboBoxACLsActionPerformed();
						}
					});

		} catch (Exception e) {
			logger.error("Error al pintar los permisos: " + e.toString());
		}
	}

	public void pintarEntidades(ListaEntidades listaEntidades) {
		try {
			this.listaEntidades = listaEntidades;
			Hashtable hEntidades = listaEntidades.gethEntidades();
			jComboBoxEntidades
					.addItem(new Entidad("", "                  ", ""));
			jComboBoxEntidades = listaEntidades
					.cargarJComboBox(jComboBoxEntidades);
			jComboBoxEntidades.setRenderer(new ComboBoxRendererEntidad());
			jComboBoxEntidades
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jComboBoxEntidadesActionPerformed();
						}
					});
		} catch (Exception e) {
			logger.error("Error al pintar las entidades: " + e.toString());
		}
	}

	private void inicializarPermisosUsuario(Usuario usuario) {
		int iTotal = jPanelPermisosUsuario.getComponentCount();
		for (int i = 0; i < iTotal; i++) {
			Component auxComponent = jPanelPermisosUsuario.getComponent(i);
			if (auxComponent instanceof JCheckBox) {
				JCheckBox jCheckBoxRol = (JCheckBox) auxComponent;
				if ((usuario != null)
						&& (usuario.tienePermisoRol((String) jCheckBoxRol
								.getClientProperty(IDPERM_CODE), aclSelected
								.getId(), listaRoles)))
					jCheckBoxRol.setSelected(true);
				else
					jCheckBoxRol.setSelected(false);
			}
		}
		if ((listaGruposPermisos == null) || (usuario == null))
			return;
		else
			listaGruposPermisos.inicializaGroupPermisos(usuario.getPermisos());

	}

	public void pintarPermisosAcls(ListaPermisos listaPermisos) {
		try {
			listaGruposPermisos = new ListaGroupPermisos(aclSelected.getId());
			jPanelPermisosUsuario.removeAll();
			jPanelDescripcion.removeAll();

			if (listaPermisos == null) {
				jPanelPermisosTotal.repaint();
				jPanelPermisosTotal.invalidate();
				jPanelPermisosTotal.validate();
				return;
			}
			int i = 0;

			for (Enumeration e = listaPermisos.gethPermisos().elements(); e
					.hasMoreElements();) {
				Permiso permiso = (Permiso) e.nextElement();

				JLabel jLabelPermiso = new JLabel();
				jLabelPermiso.setText(permiso.getDef());
				jPanelDescripcion.add(jLabelPermiso,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(10,
								3 + i, -1, -1));

				JCheckBox jCheckBoxPermitir = new JCheckBox();
				jCheckBoxPermitir.setEnabled(false);
				jCheckBoxPermitir.setBackground(new java.awt.Color(255, 255,
						255));
				jCheckBoxPermitir.putClientProperty(IDPERM_CODE, permiso
						.getIdPerm());
				jCheckBoxPermitir
						.addActionListener(new java.awt.event.ActionListener() {
							public void actionPerformed(
									java.awt.event.ActionEvent evt) {
								inicializarPermisosUsuario(auxUsuario);
							}
						});
				jPanelPermisosUsuario.add(jCheckBoxPermitir,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(0, i,
								-1, -1));
				GroupPermisos grupoPermisos = new GroupPermisos(permiso
						.getIdPerm(), modoEdicion);
				jPanelPermisosUsuario.add(grupoPermisos.getBotonPermitir(),
						new org.netbeans.lib.awtextra.AbsoluteConstraints(50,
								i, -1, -1));
				jPanelPermisosUsuario.add(grupoPermisos.getBotonDenegar(),
						new org.netbeans.lib.awtextra.AbsoluteConstraints(130,
								i, -1, -1));
				jPanelPermisosUsuario.add(grupoPermisos.getBotonRol(),
						new org.netbeans.lib.awtextra.AbsoluteConstraints(130,
								i, -1, -1));
				listaGruposPermisos.add(grupoPermisos);
				i += 20;
			}
			jPanelPermisosTotal.repaint();
			jPanelPermisosTotal.invalidate();
			jPanelPermisosTotal.validate();
			jScrollPaneUsuPer.repaint();
			jScrollPaneUsuPer.invalidate();
			jScrollPaneUsuPer.validate();
		} catch (Exception e) {
			logger.error("Error al pedir los permisos:" + e.toString());
		}
	}

	public void inicializarPermisosRolUsuario(Vector vIdPermisos) {
		int iTotal = jPanelPermisosUsuario.getComponentCount();
		for (int i = 0; i < iTotal; i++) {
			Component auxComponent = jPanelPermisosUsuario.getComponent(i);
			if (auxComponent instanceof JCheckBox) {
				JCheckBox jCheckBoxPermitir = (JCheckBox) auxComponent;
				if ((vIdPermisos != null)
						&& (vIdPermisos.indexOf(jCheckBoxPermitir
								.getClientProperty("Permitir")) >= 0))
					jCheckBoxPermitir.setSelected(true);
				else
					jCheckBoxPermitir.setSelected(false);
			}
		}
	}

	private void seleccionarUsuario(ListSelectionEvent e) {
		if (modoEdicion) {
			int n = JOptionPane.showOptionDialog(this, mensajesUsuario
					.getString("JPanelUsuarios.mensaje.desechar"), "",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, null, null);
			if (n == JOptionPane.NO_OPTION)
				return;
			else
				enabledComponents(false);
		}
		// if (e.getValueIsAdjusting()) return;
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
		} else {
			int selectedRow = lsm.getMinSelectionIndex();
			String idUsuario = (String) sorter.getValueAt(selectedRow,
					UsuariosTableModel.idIndex);
			usuarioSelected = listaUsuarios.get(idUsuario);
			if (usuarioSelected == null) {
				JOptionPane optionPane = new JOptionPane(mensajesUsuario
						.getString("JPanelUsuarios.mensaje.usunoencontrado"),
						JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(this, "");
				dialog.show();
			}

			else {
				// miramos si el usuario que está en sesión tiene los permisos
				// de editar sus propios datos
				try {
					GeopistaAcl aclAdministracion = com.geopista.security.SecurityManager
							.getPerfil("Administracion");
					boolean permisoEdicionParcial = aclAdministracion
							.checkPermission(new com.geopista.security.GeopistaPermission(
									com.geopista.security.GeopistaPermission.EDIT_USER_DATA_ADMINITRACION));
					boolean permisoEdicionCompleto = aclAdministracion
							.checkPermission(new com.geopista.security.GeopistaPermission(
									com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));

					GeopistaPrincipal principal = com.geopista.security.SecurityManager
							.getPrincipal();
					String usuarioLogueado = principal.getName();

					if (permisoEdicionCompleto) {
						this.jButtonUsuEliminar.setEnabled(true);
						this.jComboBoxACLs.setEnabled(true);
					} else {
						this.jButtonUsuEliminar.setEnabled(false);
						this.jComboBoxACLs.setEnabled(false);
					}

					if ((permisoEdicionParcial && usuarioSelected.getName()
							.equalsIgnoreCase(usuarioLogueado))
							|| permisoEdicionCompleto)
						this.jButtonEditar.setEnabled(true);

					else
						this.jButtonEditar.setEnabled(false);
					
					this.jButtonVerPermisos.setEnabled(true);

				} catch (AclNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

			mostrarUsuario(usuarioSelected);
		}
	}// fin método seleccionarUsuario

	private void inicializaComboAcl() {
		jComboBoxACLs.setSelectedIndex(0);
	}

	private void inicializaComboEntidades() {
		jComboBoxEntidades.setSelectedIndex(0);
	}

	/**
	 * Habilitamos los componentes.
	 * 
	 * @param bEnabled
	 */
	private void enabledComponents(boolean bEnabled) {

		com.geopista.security.GeopistaAcl aclAdministracion;
		try {
			aclAdministracion = com.geopista.security.SecurityManager
					.getPerfil("Administracion");
			permisoEdicionCompleta = aclAdministracion
					.checkPermission(new com.geopista.security.GeopistaPermission(
							com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
			permisoEdicionParcial = aclAdministracion
					.checkPermission(new com.geopista.security.GeopistaPermission(
							"Geopista.Administracion.Edit"));

			modoEdicion = bEnabled;
			jButtonUsuAceptar.setEnabled(bEnabled);
			jButtonCancelar.setEnabled(bEnabled);
			jTextFieldUsuNombre.setEditable(bEnabled);
			jTextFieldNombreCompleto.setEditable(bEnabled);
			bloqueadoJCheckBox.setEnabled(bEnabled);
			jTextFieldPass.setEditable(bEnabled);
			jTextFieldRePass.setEditable(bEnabled);
			jTextFieldUsuDes.setEditable(bEnabled);
			jTextFieldUsuNif.setEditable(bEnabled);
			jTextFieldUsuMail.setEditable(bEnabled);

			if (permisoEdicionCompleta) {// Tenemos el permiso de edición completa
				jButtonUsuAnadir.setEnabled(true);
				jButtonUsuEliminar.setEnabled(false);
				jButtonEditar.setEnabled(false);
				
				
				jButtonPermitir.setEnabled(bEnabled);
				jButtonDenegar.setEnabled(bEnabled);
				jButtonLimpiar.setEnabled(bEnabled);
				jComboBoxDepartamento.setEnabled(bEnabled);
				jComboBoxOrganizacion.setEnabled(bEnabled);
				jComboBoxEntidades.setEnabled(bEnabled);
				int iTotal = jPanelRolesUsuario.getComponentCount();
				for (int i = 0; i < iTotal; i++) {
					Component auxComponent = jPanelRolesUsuario.getComponent(i);
					if (auxComponent instanceof JCheckBox) {
						JCheckBox jCheckBoxPermitir = (JCheckBox) auxComponent;
						jCheckBoxPermitir.setEnabled(bEnabled);
					}
				}
				iTotal = jPanelPermisosUsuario.getComponentCount();
				for (int i = 0; i < iTotal; i++) {
					Component auxComponent = jPanelPermisosUsuario
							.getComponent(i);
					if (auxComponent instanceof JRadioButton) {
						JRadioButton jRadioButton = (JRadioButton) auxComponent;
						jRadioButton.setEnabled(bEnabled);
					}
				}
			}// fin if

			else if (permisoEdicionParcial) {// tengo el permiso de edición
												// parcial: EditUserData
				// deshabilito los componentes que no se pueden modificar
				jComboBoxACLs.setEnabled(false);
				jLabelRolUsuario.setEnabled(false);
				jLabelAppUsuario.setEnabled(false);
				jLabelACLUsuario.setEnabled(false);
				jLabelPermisos.setEnabled(false);
				jButtonConexiones.setEnabled(false);
				jScrollPaneUsuRoles.setEnabled(false);
				jTextFieldUsuNombre.setEnabled(false);
			}

			this.invalidate();
			this.validate();
		} catch (AclNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void actualizarModelo() {
		actualizarModelo("");
	}

	private void actualizarModelo(String filtro) {
		modelUsuarios = new UsuariosTableModel();
		//modelUsuarios.setModelData(listaUsuarios);
		modelUsuarios.setModelData(listaUsuarios, filtro);
		sorter = new TableSorted(modelUsuarios);
		sorter.setTableHeader(jTableUsuarios.getTableHeader());
		jTableUsuarios.setModel(sorter);
		TableColumn column = jTableUsuarios.getColumnModel().getColumn(
				UsuariosTableModel.idIndex);
		column.setPreferredWidth(5);
		column = jTableUsuarios.getColumnModel().getColumn(
				UsuariosTableModel.idNombre);
		column.setPreferredWidth(15);
		jTableUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
        //Ordenamos por nombre de Rol que es la columna 1
    	sorter.setSortingStatus(1, 1);
	}

	private int getEntidadSelectedIndex(String id_entidad) {
		int index = 0;
		for (int i = 0; i < jComboBoxEntidades.getItemCount(); i++) {
			if (((Entidad) jComboBoxEntidades.getItemAt(i)).getId().equals(
					id_entidad)) {
				index = i;
				break;
			}

		}
		return index;
	}

	public void mostrarUsuario(Usuario miUsuario) {
		if (miUsuario != null) {
			auxUsuario = (Usuario) miUsuario.clone();
			jTextFieldUsuNombre.setText(auxUsuario.getName());
			jTextFieldNombreCompleto.setText(auxUsuario.getNombreCompleto());
			jTextFieldPass.setText(auxUsuario.getPassword());
			jTextFieldRePass.setText(auxUsuario.getPassword());
			jTextFieldUsuDes.setText(auxUsuario.getDescripcion());
			jTextFieldUsuNif.setText(auxUsuario.getNif());
			jTextFieldUsuMail.setText(auxUsuario.getEmail());
			bloqueadoJCheckBox.setSelected(auxUsuario.isBloqueado());
			passwordCambiado = false;
			userCambiado = false;
			if (auxUsuario.getId_entidad() != null) {
				jComboBoxEntidades
						.setSelectedIndex(getEntidadSelectedIndex(auxUsuario
								.getId_entidad()));
			} else {
				jComboBoxEntidades.setSelectedIndex(0);
			}
			inicializarRolesUsuario(auxUsuario);
			inicializarPermisosUsuario(auxUsuario);
		} else// Limpio los datos
		{
			jTextFieldUsuNombre.setText("");
			jTextFieldNombreCompleto.setText("");
			bloqueadoJCheckBox.setSelected(false);
			jTextFieldPass.setText("");
			jTextFieldRePass.setText("");
			jTextFieldUsuDes.setText("");
			jTextFieldUsuNif.setText("");
			jTextFieldUsuMail.setText("");
			jComboBoxACLs.setSelectedIndex(0);
			inicializaComboEntidades();
			inicializarRolesUsuario(null);
			passwordCambiado = false;
			userCambiado = false;
		}
	}

	private void editarUsuario() {
		if (usuarioSelected == null) {
			JOptionPane optionPane = new JOptionPane(mensajesUsuario
					.getString("JPanelUsuarios.mensaje.nousuario"),
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "INFO");
			dialog.show();
			return;
		}
		enabledComponents(true);
	}

	public void inicializarRolesUsuario(Usuario auxUsuario) {
		int iTotal = jPanelRolesUsuario.getComponentCount();
		for (int i = 0; i < iTotal; i++) {
			Component auxComponent = jPanelRolesUsuario.getComponent(i);
			if (auxComponent instanceof JCheckBox) {
				JCheckBox jCheckBoxGrupo = (JCheckBox) auxComponent;
				String idRol = (String) jCheckBoxGrupo
						.getClientProperty(IDROL_CODE);
				if (((auxUsuario != null) && (auxUsuario
						.containsGrupo((String) jCheckBoxGrupo
								.getClientProperty(IDROL_CODE))))
						|| modoNuevo
						& idRol
								.equalsIgnoreCase(ID_ROL_EDICION_DATOS_PERSONALES))
					jCheckBoxGrupo.setSelected(true);
				else
					jCheckBoxGrupo.setSelected(false);
			}
		}
	}

	private void cancelarActionPerformed() {
		modoNuevo = false;
		enabledComponents(false);
		mostrarUsuario(usuarioSelected);
	}

	private boolean validarPassword(){
		JOptionPane optionPane = null;
		boolean valido = true;
		String password = new String(jTextFieldPass.getPassword());
	    
		if (password == null || (!new String(jTextFieldPass.getPassword()).equals(new String(jTextFieldRePass.getPassword())))){
			optionPane = new JOptionPane(mensajesUsuario.getString("JPanelUsuarios.mensajes.nopassword"), JOptionPane.INFORMATION_MESSAGE);
			valido = false;
		}
		else{
			if (password.length() < TAMANIO_MINIMO_PASSWORD){
				optionPane = new JOptionPane(mensajesUsuario.getString("JPanelUsuarios.mensajes.minimocaracterespass"), JOptionPane.INFORMATION_MESSAGE);
				valido = false;
			}
			else{
				Pattern p = Pattern.compile("[A-Z]+");				
				Matcher m = p.matcher(password);
			    StringBuffer sb = new StringBuffer();
			    boolean resultado = m.find();
			    if (!resultado){
			    	optionPane = new JOptionPane(mensajesUsuario.getString("JPanelUsuarios.mensajes.caracteresenpassword"), JOptionPane.INFORMATION_MESSAGE);
			    	valido = false;
			    }
			    else{
			    	p = Pattern.compile("[a-z]+");
			    	m = p.matcher(password);
			    	sb = new StringBuffer();
				    resultado = m.find();
				    if (!resultado){
				    	optionPane = new JOptionPane(mensajesUsuario.getString("JPanelUsuarios.mensajes.caracteresenpassword"), JOptionPane.INFORMATION_MESSAGE);
				    	valido = false;
				    }
				    else{
				    	p = Pattern.compile("[0-9]+");
				    	m = p.matcher(password);
				    	sb = new StringBuffer();
				    	resultado = m.find();
				    	if (!resultado){
				    		optionPane = new JOptionPane(mensajesUsuario.getString("JPanelUsuarios.mensajes.caracteresenpassword"), JOptionPane.INFORMATION_MESSAGE);
				    		valido = false;
				    	}
				    	else{
				    		p = Pattern.compile("[-_@?¿¡*+.,:;<>\\!\"#%&/()={}]+");
					    	m = p.matcher(password);
					    	sb = new StringBuffer();
					    	resultado = m.find();
					    	if (!resultado){
					    		optionPane = new JOptionPane(mensajesUsuario.getString("JPanelUsuarios.mensajes.caracteresenpassword"), JOptionPane.INFORMATION_MESSAGE);
					    		valido = false;
					    	}
					    	else{
					    		if ((usuarioSelected!=null) && (password.equals(usuarioSelected.getPassword()))){
					    			optionPane = new JOptionPane(mensajesUsuario.getString("JPanelUsuarios.mensajes.passworddistintaalaultima"), JOptionPane.INFORMATION_MESSAGE);
						    		valido = false;
					    		}
					    	}
				    	}
				    }
			    }

			}
		}
		if (!valido){
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
		}
		return valido;
	}
	
	private boolean validarUsuario(){
		JOptionPane optionPane = null;
		boolean valido = true;
		if (new String(jTextFieldUsuNombre.getText()).length() < TAMANIO_MINIMO_USER){
			optionPane = new JOptionPane(mensajesUsuario.getString("JPanelUsuarios.mensajes.minimocaracteresuser"), JOptionPane.INFORMATION_MESSAGE);
			valido = false;
		}

		if (!valido){
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
		}
		return valido;
	}
	
	// --Comienza el manejo de evento cuando el usuario pulsa aceptar---
	public void aceptarActionPerformed() {
		try {
			if (userCambiado)
				if (!validarUsuario())
					return;
			if (passwordCambiado)
				if (!validarPassword())
					return;
			
			if (this.jTextFieldUsuNombre.getText().toUpperCase().equals("SYSSUPERUSER")){
				if (!((Entidad) this.jComboBoxEntidades.getSelectedItem()).getId()
					.equals("")) {
					JOptionPane optionPane = new JOptionPane(mensajesUsuario
							.getString("JPanelUsuarios.mensajes.entidad.syssuperuser"),
							JOptionPane.INFORMATION_MESSAGE);
					JDialog dialog = optionPane.createDialog(this, "");
					dialog.show();
					return;
				}
			}else{
				//Si el usuario tiene el identificador de entidad=0 le dejamos que se pueda seleccionar
				//la entidad 0
				if (!auxUsuario.getId_entidad().equals("0"))
					if (((Entidad) this.jComboBoxEntidades.getSelectedItem()).getId()
							.equals("")) {
							JOptionPane optionPane = new JOptionPane(mensajesUsuario
									.getString("JPanelUsuarios.mensajes.noentidad"),
									JOptionPane.INFORMATION_MESSAGE);
							JDialog dialog = optionPane.createDialog(this, "");
							dialog.show();
							return;
					}
			}
			guardarCambios();
			CResultadoOperacion result = null;
			String sMensaje = "";
			try {
				if (modoNuevo) {
					result = (new OperacionesAdministrador(Constantes.url)).nuevoUsuario(auxUsuario);
					sMensaje = mensajesUsuario.getString("JPanelUsuarios.mensaje.usuarioinsertado");
				} else {
					logger.debug("Actualizando usuario: " + auxUsuario
							+ " Grupos: \n" + auxUsuario.printGrupos());
					result = (new OperacionesAdministrador(Constantes.url)).actualizarUsuario(auxUsuario);
					sMensaje = mensajesUsuario.getString("JPanelUsuarios.mensaje.usuarioactualizado");
				}
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				logger.error("Exception al grabar en base de datos un usuario: "
								+ sw.toString());
				result = new CResultadoOperacion(false, e.getMessage());
			}
			if (result.getResultado()) {
				usuarioSelected = auxUsuario;
				listaUsuarios.add(usuarioSelected);
				JOptionPane optionPane = new JOptionPane(sMensaje,
						JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(this, "");
				dialog.show();
				enabledComponents(false);
				modoNuevo = false;
				mostrarUsuario(usuarioSelected);
				actualizarModelo();
			} else {
				JOptionPane optionPane = new JOptionPane(result
						.getDescripcion(), JOptionPane.ERROR_MESSAGE);
				JDialog dialog = optionPane.createDialog(this, "ERROR");
				dialog.show();
			}
		} catch (Exception ex) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Excepcion al añadir usuario: " + sw.toString());
			JOptionPane optionPane = new JOptionPane(ex.getMessage(),
					JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
		}
	}// fin del manejo de evento cuando el usuario pulsa
		// aceptar-----------------------

	private void eliminarUsuarioActionListener() {
		if (usuarioSelected == null) {
			JOptionPane optionPane = new JOptionPane(mensajesUsuario
					.getString("JPanelUsuarios.mensaje.usunoencontrado"),
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "INFO");
			dialog.show();
			return;
		}
		int n = JOptionPane.showOptionDialog(this, mensajesUsuario
				.getString("JPanelUsuario.mensaje.eliminarusuario")
				+ " " + usuarioSelected.getName() + "?", "",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
		if (n == JOptionPane.NO_OPTION)
			return;
		else
			eliminarUsuario(usuarioSelected);
	}

	public void eliminarUsuario(Usuario usuarioEliminado) {
		CResultadoOperacion result;
		try {
			result = (new OperacionesAdministrador(Constantes.url))
					.eliminarUsuario(usuarioEliminado);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception al eliminar usuario en base de datos: "
					+ sw.toString());
			result = new CResultadoOperacion(false, e.getMessage());
		}
		if (result.getResultado()) {
			listaUsuarios.remove(usuarioEliminado);
			usuarioSelected = null;
			JOptionPane optionPane = new JOptionPane(mensajesUsuario
					.getString("JPanelUsuarios.mensaje.usuarioeliminado"),
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
			enabledComponents(false);
			mostrarUsuario(usuarioSelected);
			actualizarModelo();
		} else {
			JOptionPane optionPane = new JOptionPane(result.getDescripcion(),
					JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
		}
	}

	public void anadirUsuarioActionPerformed() {
		modoNuevo = true;
		auxUsuario = new Usuario();
		mostrarUsuario(null);
		enabledComponents(true);
	}// fin anadirUsuarioActionPerformed

	private void mostrarConexiones() {
		if (usuarioSelected != null) {
			ShowConexiones conexionesDialog = new ShowConexiones(
					usuarioSelected.getId(), framePadre, true, mensajesUsuario);
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			conexionesDialog.setLocation(d.width / 2 - 400 / 2,
					d.height / 2 - 400 / 2);
			conexionesDialog.setSize(600, 400);
			conexionesDialog.show();
		}
	}

	private void verPermisos() {
		if (usuarioSelected != null) {
			ShowPermisos permisosDialog = new ShowPermisos(usuarioSelected
					.getId(), framePadre, true, mensajesUsuario);
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			permisosDialog.setLocation(d.width / 2 - 400 / 2,
					d.height / 2 - 400 / 2);
			permisosDialog.setSize(400, 400);
			permisosDialog.show();
		}
	}

	private javax.swing.JButton jButtonCancelar;
	private javax.swing.JButton jButtonEditar;
	private javax.swing.JButton jButtonPermitir;
	private javax.swing.JButton jButtonDenegar;
	private javax.swing.JButton jButtonLimpiar;
	private javax.swing.JButton jButtonUsuAceptar;
	private javax.swing.JButton jButtonUsuAnadir;
	private javax.swing.JButton jButtonUsuEliminar;
	private javax.swing.JButton jButtonVerPermisos;
	private javax.swing.JComboBox jComboBoxACLs;
	private javax.swing.JComboBox jComboBoxApps;
	private JCheckBox bloqueadoJCheckBox;
	private javax.swing.JComboBox jComboBoxEntidades;
	private javax.swing.JComboBox jComboBoxOrganizacion;
	private javax.swing.JComboBox jComboBoxDepartamento;
	private javax.swing.JLabel jLabelNombreUsuario;
	private javax.swing.JLabel jLabelBusquedaUsuario;
	private javax.swing.JLabel jLabelUsuDes;
	private javax.swing.JLabel jLabelContrasena;
	private javax.swing.JLabel jLabelRolUsuario;
	private javax.swing.JLabel jLabelAppUsuario;
	private javax.swing.JLabel jLabelACLUsuario;
	private javax.swing.JLabel jLabelConfirContra;
	private javax.swing.JLabel jLabelDepartamento;
	private javax.swing.JLabel jLabelOrganizacion;
	private javax.swing.JLabel jLabelMail;
	private javax.swing.JLabel jLabelNif;
	private javax.swing.JLabel jLabelNombreCompleto;
	private javax.swing.JLabel jLabelBloqueado;
	private javax.swing.JLabel jLabelEntidad;
	private javax.swing.JLabel jLabelPermisos;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanelListaUsuarios;
	private javax.swing.JPanel jPanelPermisosUsuario;
	private javax.swing.JPanel jPanelPermisosTotal;
	private com.geopista.app.utilidades.TextField jTextFieldBusquedaUsuario;
	private com.geopista.app.utilidades.TextField jTextFieldUsuNombre;
	private com.geopista.app.utilidades.TextField jTextFieldUsuDes;
	private com.geopista.app.utilidades.TextField jTextFieldUsuNif;
	private com.geopista.app.utilidades.TextField jTextFieldUsuMail;
	private javax.swing.JPasswordField jTextFieldPass;
	private javax.swing.JPasswordField jTextFieldRePass;
	private com.geopista.app.utilidades.TextField jTextFieldNombreCompleto;
	private javax.swing.JScrollPane jScrollPaneUsuPer;
	private javax.swing.JScrollPane listaUsuariosJScrollPane;
	private javax.swing.JTable jTableUsuarios;
	private javax.swing.JPanel jPanelEditarUsuario;
	private javax.swing.JPanel jPanelRolesUsuario;
	private javax.swing.JScrollPane jScrollPaneUsuRoles;
	private javax.swing.JPanel jPanelDescripcion = new JPanel();
	private javax.swing.JButton jButtonUsuarioTodos;
	private javax.swing.JButton jButtonConexiones;

}
