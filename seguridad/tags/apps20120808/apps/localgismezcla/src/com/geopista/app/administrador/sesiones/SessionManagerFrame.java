package com.geopista.app.administrador.sesiones;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.administrador.usuarios.ShowConexiones;
import com.geopista.app.administrador.usuarios.UsuariosTableModel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.administrador.ListaUsuarios;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.Usuario;

public class SessionManagerFrame extends javax.swing.JInternalFrame {
	private Logger logger = Logger.getLogger(SessionManagerFrame.class);
	 
	private UsuariosTableModel modelUsuarios;
	private ListaUsuarios listaUsuarios=null;    
	private ResourceBundle messages;
	
	    private TableSorted sorter;
	    private javax.swing.JButton jButtonSalir;
	    private javax.swing.JButton jButtonConexiones;
	    private javax.swing.JPanel jPanel1;
	    private javax.swing.JPanel jPanelSalir;
	    private javax.swing.JScrollPane jScrollPaneTablaUsuarios;
	    private javax.swing.JTable jTableUsuarios;
	    private Usuario usuarioSelected = null;

		private boolean checkPermission;
	    
	    
	    public SessionManagerFrame(ResourceBundle messages, boolean checkPermission) {
	    	this.checkPermission=checkPermission;
	        this.messages = messages;
	    	initListas();
	        initComponents();
	        changeScreenLang(messages);
	    }
	 
	    private void initListas() {
	        try {
	        	if (Constantes.idEntidad == 0)
	        		listaUsuarios = new OperacionesAdministrador(Constantes.url).getListaUsuarios();
	        	else
	        		listaUsuarios = new OperacionesAdministrador(Constantes.url).getUsuarios(Constantes.idEntidad);
	        }
	        catch(Exception e) {
			       logger.error("Error al inicializa la lista de usuarios: "+e.toString());
	               JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.ERROR_MESSAGE);
	               JDialog dialog =optionPane.createDialog(this,"ERROR al inicializar los usuarios");
	               dialog.setVisible(true);
			 }
	    }
	  
	    private void initComponents() {
	        jPanelSalir = new javax.swing.JPanel();
	        jButtonSalir = new javax.swing.JButton();
	        jPanel1 = new javax.swing.JPanel();
	        jScrollPaneTablaUsuarios = new javax.swing.JScrollPane();
	        jTableUsuarios = new javax.swing.JTable();

	        jButtonSalir.setText("Salir");
	        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButtonSalirActionPerformed(evt);
	            }
	        });

	        jPanelSalir.add(jButtonSalir);
	        
	        jButtonConexiones = new javax.swing.JButton();

			jButtonConexiones.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							mostrarConexiones();
						}
					});

			if (!checkPermission)
				jButtonConexiones.setEnabled(false);
			jPanelSalir.add(jButtonConexiones);
			

	        getContentPane().add(jPanelSalir, java.awt.BorderLayout.SOUTH);
	        jPanel1.setLayout(new java.awt.BorderLayout());

	        actualizarModelo();
	        jScrollPaneTablaUsuarios.setViewportView(jTableUsuarios);

	        jPanel1.add(jScrollPaneTablaUsuarios, java.awt.BorderLayout.CENTER);
	        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
	        
	        // Para seleccionar una fila
	     	ListSelectionModel rowSM = jTableUsuarios.getSelectionModel();
	     	rowSM.addListSelectionListener(new ListSelectionListener() {
	     		public void valueChanged(ListSelectionEvent e) {
	     			seleccionarUsuario(e);
	     		}
	     	});
	     	

	        pack();
	    }

	    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {
	         dispose();
	    }
	    
	    private void actualizarModelo() {
	        modelUsuarios= new UsuariosTableModel();
	        modelUsuarios.setModelData(listaUsuarios);
	        sorter = new TableSorted(modelUsuarios);
	        sorter.setTableHeader(jTableUsuarios.getTableHeader());
	        jTableUsuarios.setModel(sorter);
	        jTableUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    }

	    public void changeScreenLang(ResourceBundle messages) {
	        jButtonSalir.setText(messages.getString("CSesionesFrame.jButtonSalir"));
	        jButtonSalir.setToolTipText(messages.getString("CSesionesFrame.jButtonSalir"));
	    	jButtonConexiones.setText(messages.getString("CUsuariosFrame.jButtonConexiones"));
	        jButtonConexiones.setToolTipText(messages.getString("CUsuariosFrame.jButtonConexiones"));

	        TableColumn tableColumn = jTableUsuarios.getColumnModel().getColumn(0);
			tableColumn.setHeaderValue(messages.getString("CUsuariosFrame.col0"));
			tableColumn = jTableUsuarios.getColumnModel().getColumn(1);
			tableColumn.setHeaderValue(messages.getString("CUsuariosFrame.col1"));
			tableColumn = jTableUsuarios.getColumnModel().getColumn(2);
			tableColumn.setHeaderValue(messages.getString("CUsuariosFrame.col2"));

	    }
	    
	    private void seleccionarUsuario(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if (!lsm.isSelectionEmpty()) {
				int selectedRow = lsm.getMinSelectionIndex();
				String idUsuario = (String) sorter.getValueAt(selectedRow, UsuariosTableModel.idIndex);
				usuarioSelected = listaUsuarios.get(idUsuario);
				if (usuarioSelected == null) {
					JOptionPane optionPane = new JOptionPane(messages.getString("JPanelUsuarios.mensaje.usunoencontrado"), JOptionPane.INFORMATION_MESSAGE);
					JDialog dialog = optionPane.createDialog(this, "");
					dialog.setVisible(true);
				}
			}
		}
	    
	    private void mostrarConexiones() {
			if (usuarioSelected != null) {
				ShowConexiones conexionesDialog = new ShowConexiones(usuarioSelected.getId(), this, true, messages);
			
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				conexionesDialog.setLocation(d.width / 2 - 400 / 2, d.height / 2 - 400 / 2);
				conexionesDialog.setSize(600, 400);
				conexionesDialog.setVisible(true);
			} 
		}

	    
	}

