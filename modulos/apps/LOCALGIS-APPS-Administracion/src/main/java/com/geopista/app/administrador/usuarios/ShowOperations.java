/**
 * ShowOperations.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Hashtable;
import java.util.ResourceBundle;

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
import com.geopista.protocol.administrador.Operacion;
import com.geopista.protocol.administrador.OperacionesAdministrador;


public class ShowOperations extends javax.swing.JDialog  {
	
	private Logger logger = Logger.getLogger(ShowOperations.class);
	
	private ResourceBundle messages;
	private OperacionesTableModel modelOperaciones;
	private Hashtable listaOperaciones=null;
 
	private Operacion operacionSelected;    
    private String conexionIDSelected;
    private String idUsuarioSelected;
    
    private TableSorted sorter;
    private java.awt.Frame parentFrame;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonDetalles;
    private javax.swing.JPanel jPanelBotonera;
    private javax.swing.JPanel jPanelOperaciones;
    private javax.swing.JScrollPane jScrollPaneTablaOperaciones;
    private javax.swing.JTable jTableOperaciones;

	public ShowOperations(String idConexion, String sIdUsuario, ResourceBundle messages, javax.swing.JDialog parent) {
		super(parent, false); 
		
		this.messages=messages;
		this.idUsuarioSelected = sIdUsuario;
	    this.conexionIDSelected = idConexion;
	    
        initListas(idConexion, sIdUsuario);
		initComponents();
        changeScreenLang(messages);
	}
	
    private void initComponents() {
       
    	jButtonSalir = new JButton();
        jButtonDetalles = new JButton();
        jPanelOperaciones = new JPanel();
        jPanelBotonera = new JPanel();
        jScrollPaneTablaOperaciones = new javax.swing.JScrollPane();
        jTableOperaciones = new javax.swing.JTable();

        actualizarModelo();
        
        jScrollPaneTablaOperaciones.setViewportView(jTableOperaciones);

        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salir();
            }
        });
        
        jButtonDetalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultaDetalles();
            }
        });
        
        jPanelOperaciones.setLayout(new java.awt.BorderLayout());
        jPanelOperaciones.add(jScrollPaneTablaOperaciones,java.awt.BorderLayout.CENTER);
        jPanelBotonera.add(jButtonSalir);
        jPanelBotonera.add(jButtonDetalles);

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanelOperaciones, java.awt.BorderLayout.CENTER);
        getContentPane().add(jPanelBotonera, java.awt.BorderLayout.SOUTH);

     // Para seleccionar una fila
     	ListSelectionModel rowSM = jTableOperaciones.getSelectionModel();
     	rowSM.addListSelectionListener(new ListSelectionListener() {
     		public void valueChanged(ListSelectionEvent e) {
     			seleccionarOperacion(e);
     		}
     	});
     	
        pack();
    }
    
    private void actualizarModelo(){
    	modelOperaciones= new OperacionesTableModel(); 
    	modelOperaciones.setModelData(listaOperaciones);
        sorter = new TableSorted(modelOperaciones);
        sorter.setTableHeader(jTableOperaciones.getTableHeader());
        jTableOperaciones.setModel(sorter);
       
        sorter.setSortingStatus(0, 1);     
        jTableOperaciones.getColumn("").setMaxWidth(7);
        
        jTableOperaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  

    }
    
    private void initListas(String idConexion, String sIdUsuario) {
        try {
        	OperacionesAdministrador adminOperations = new OperacionesAdministrador(Constantes.url);
            listaOperaciones = adminOperations.getOperaciones(idConexion, sIdUsuario);
            
            if (listaOperaciones==null)
                 logger.error("Ha ocurrido un error al buscar las operaciones del "+sIdUsuario+" y idSession " +idConexion);
             else
                 logger.info("Numero de operaciones de la session "+idConexion+ ": "+listaOperaciones.size());

        }
        catch(Exception e) {
		       logger.error("Error al obtener las operaciones de la session: "+e.toString());
               JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.ERROR_MESSAGE);
               JDialog dialog =optionPane.createDialog(this,messages.getString("ShowOperaciones.userFriendlyError"));
               dialog.setVisible(true);
		 }
    }

    private void salir() {
        dispose();
    }
    
    public void changeScreenLang(ResourceBundle messages) {
         setTitle(messages.getString("ShowOperaciones.title"));
         jPanelOperaciones.setToolTipText(messages.getString("ShowOperaciones.title"));
         jButtonSalir.setText(messages.getString("ShowOperaciones.jButtonSalir"));
         jButtonSalir.setToolTipText(messages.getString("ShowOperaciones.jButtonSalir"));
         jButtonDetalles.setText(messages.getString("ShowOperaciones.jButtonDetalles"));
         jButtonDetalles.setToolTipText(messages.getString("ShowOperaciones.jButtonDetallesToolTip"));
        
         TableColumn tableColumn= jTableOperaciones.getColumnModel().getColumn(1);
         tableColumn.setHeaderValue(messages.getString("ShowOperaciones.column1"));
         tableColumn= jTableOperaciones.getColumnModel().getColumn(2);
         tableColumn.setHeaderValue(messages.getString("ShowOperaciones.column2"));
         tableColumn= jTableOperaciones.getColumnModel().getColumn(3);
         tableColumn.setHeaderValue(messages.getString("ShowOperaciones.column3"));
         tableColumn= jTableOperaciones.getColumnModel().getColumn(4);
         tableColumn.setHeaderValue(messages.getString("ShowOperaciones.column4"));

     }

     private void seleccionarOperacion(ListSelectionEvent e){  
     	ListSelectionModel lsm = (ListSelectionModel) e.getSource();
     	int selectedRow = lsm.getMinSelectionIndex();
     	
     	String opCount = (String)sorter.getValueAt(selectedRow, 0);
     	
     	Operacion op = (Operacion) listaOperaciones.get(opCount);	
     	op.setIdConexion(conexionIDSelected);
     	op.setIdUsuario(idUsuarioSelected);

     	operacionSelected = op;
    }
     
     private void consultaDetalles() {
    	 if (operacionSelected != null) {
    		 ShowDetalles detallesDialog = new ShowDetalles(operacionSelected, messages, this);

 			 Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
 			 detallesDialog.setLocation(d.width / 2 - 800 / 2, d.height / 2 - 300 / 2);
 			 detallesDialog.setSize(800, 300);
 			 detallesDialog.setVisible(true);
 			
 		}
 		 
 	}

}
