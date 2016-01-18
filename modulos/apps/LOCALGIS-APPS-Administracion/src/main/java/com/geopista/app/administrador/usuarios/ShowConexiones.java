/**
 * ShowConexiones.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.comparator.DateComparator;
import com.geopista.protocol.administrador.OperacionesAdministrador;


public class ShowConexiones extends javax.swing.JDialog  {
	
	Logger logger = Logger.getLogger(ShowConexiones.class);
    ResourceBundle messages;
    ConexionesTableModel modelConexiones;
    Vector listaConexiones=null;
    private TableSorted sorter;
    private String conexionIDSelected;
    private String idUsuarioSelected;
    
    private java.awt.Frame parentFrame;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonOperaciones;
    private javax.swing.JPanel jPanelBotonera;
    private javax.swing.JPanel jPanelConexiones;
    private javax.swing.JScrollPane jScrollPaneTablaConexiones;
    private javax.swing.JTable jTableConexiones;

	
	public ShowConexiones(String sIdUsuario, java.awt.Frame parent, boolean modal, ResourceBundle messages) {
		super(parent, modal);
        this.messages=messages;
        this.idUsuarioSelected = sIdUsuario;
        this.parentFrame = parent;
        initListas(sIdUsuario);
		initComponents();
        changeScreenLang(messages);
	}
	
	public ShowConexiones(String sIdUsuario, javax.swing.JInternalFrame parent, boolean modal, ResourceBundle messages) {
		
        this.messages=messages;
        this.idUsuarioSelected = sIdUsuario;

        initListas(sIdUsuario);
		initComponents();
        changeScreenLang(messages);
	}


    private void initComponents() {
        jButtonSalir = new JButton();
        jButtonOperaciones = new JButton();
        jPanelConexiones = new JPanel();
        jPanelBotonera = new JPanel();
        jScrollPaneTablaConexiones = new javax.swing.JScrollPane();
        jTableConexiones = new javax.swing.JTable();
        actualizarModelo();
        jScrollPaneTablaConexiones.setViewportView(jTableConexiones);

        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salir();
            }
        });
        
        jButtonOperaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultaOperaciones();
            }
        });
        
        jPanelConexiones.setLayout(new java.awt.BorderLayout());
        jPanelConexiones.add(jScrollPaneTablaConexiones,java.awt.BorderLayout.CENTER);
        jPanelBotonera.add(jButtonSalir);
        jPanelBotonera.add(jButtonOperaciones);

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanelConexiones, java.awt.BorderLayout.CENTER);
        getContentPane().add(jPanelBotonera, java.awt.BorderLayout.SOUTH);

     // Para seleccionar una fila
		ListSelectionModel rowSM = jTableConexiones.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				seleccionarConexion(e);
			}
		});
		
		this.setModal(true);
        pack();
    }
    
    private void initListas(String sIdUsuario) {
        try {
             listaConexiones= (new OperacionesAdministrador(Constantes.url)).getConexiones(sIdUsuario);
             if (listaConexiones==null)
                 logger.warn("El usuario "+sIdUsuario +" no tiene conexiones");
             else
                 logger.info("Numero de conexiones para el usuario "+sIdUsuario+ ": "+listaConexiones.size());

        }
        catch(Exception e) {
		       logger.error("Error al inicializa la lista de conexiones: "+e.toString());
               JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.ERROR_MESSAGE);
               JDialog dialog =optionPane.createDialog(this, messages.getString("ShowConexiones.userFriendlyError"));
               dialog.setVisible(true);
		 }
    }

    private void salir() {
        dispose();
    }
    
    public void changeScreenLang(ResourceBundle messages) {
         setTitle(messages.getString("ShowConexiones.title"));
         jPanelConexiones.setToolTipText(messages.getString("ShowConexiones.title"));
         jButtonSalir.setText(messages.getString("ShowConexiones.jButtonSalir"));
         jButtonSalir.setToolTipText(messages.getString("ShowConexiones.jButtonSalir"));
         jButtonOperaciones.setText(messages.getString("ShowConexiones.jButtonOperaciones"));
         jButtonOperaciones.setToolTipText(messages.getString("ShowConexiones.jButtonOperacionesToolTip"));

         TableColumn tableColumn= jTableConexiones.getColumnModel().getColumn(0);
         tableColumn.setHeaderValue(messages.getString("ShowConexiones.column0"));
         tableColumn= jTableConexiones.getColumnModel().getColumn(1);
         tableColumn.setHeaderValue(messages.getString("ShowConexiones.column1"));
         tableColumn= jTableConexiones.getColumnModel().getColumn(2);
         tableColumn.setHeaderValue(messages.getString("ShowConexiones.column2"));
         tableColumn= jTableConexiones.getColumnModel().getColumn(3);
         tableColumn.setHeaderValue(messages.getString("ShowConexiones.column3"));
     }

     private void actualizarModelo(){
        modelConexiones= new ConexionesTableModel();
        modelConexiones.setModelData(listaConexiones);
        sorter = new TableSorted(modelConexiones);
        sorter.setTableHeader(jTableConexiones.getTableHeader());
        
        //La columna 2 es una fecha
        sorter.setColumnComparatorByColumn(2, DateComparator.DATE_COMPARATOR);
        sorter.setColumnComparatorByColumn(3, DateComparator.DATE_COMPARATOR);

        
        jTableConexiones.setModel(sorter);
        jTableConexiones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

	private void consultaOperaciones() {
		if (conexionIDSelected != null) {

			ShowOperations operationsDialog = new ShowOperations(
					conexionIDSelected, idUsuarioSelected, messages, this);

			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(d.width / 2 - 1200 / 2, d.height / 2 - 400 / 2);
			operationsDialog.setLocation(d.width / 2 , d.height / 2 - 400 / 2);
			operationsDialog.setSize(600, 400);
			operationsDialog.show();
		}

	}
     
   private void seleccionarConexion(ListSelectionEvent e){  
    	ListSelectionModel lsm = (ListSelectionModel) e.getSource();
    	int selectedRow = lsm.getMinSelectionIndex();
    	conexionIDSelected = (String) sorter.getValueAt(selectedRow, ConexionesTableModel.idIndex);
  
   }


}
