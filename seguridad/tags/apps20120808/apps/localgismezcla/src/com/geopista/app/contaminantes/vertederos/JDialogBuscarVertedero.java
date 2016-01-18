/*
 * JDialogBuscarVertedero.java
 *
 * Created on 4 de mayo de 2005, 14:46
 */

package com.geopista.app.contaminantes.vertederos;

import com.geopista.protocol.contaminantes.OperacionesContaminantes;
import com.geopista.protocol.contaminantes.Vertedero;
import com.geopista.app.utilidades.TableSorted;


import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.Enumeration;

import org.apache.log4j.Logger;

/**
 *
 * @author  angeles
 */
public class JDialogBuscarVertedero extends javax.swing.JDialog {
    private ResourceBundle messages;
    VertederoTableModel modelVertedero;
    Vector listaVertederos = null;
    private JFrame parent;
	private TableSorted sorter;
	private Vertedero vertederoSelected = null;
    private boolean okSelected=false;
    protected Logger logger = Logger.getLogger(JDialogBuscarVertedero.class);



    /** Creates new form JDialogBuscarVertedero */
    public JDialogBuscarVertedero(java.awt.Frame parent,boolean modal,ResourceBundle messages) {
        super(parent, modal);
		this.messages = messages;
        this.parent = (JFrame)parent;
        initListas();
        initComponents();
    }
    private void initListas() {

		try {
			listaVertederos = (new OperacionesContaminantes(com.geopista.app.contaminantes.init.Constantes.url)).getVertederos();
			if (listaVertederos == null)
				logger.warn("No existen vertederos en el sistema");
			else
				logger.info("Numero de vertederos: " + listaVertederos.size());

		} catch (Exception e) {
			logger.error("Error al inicializa la lista de vertederos: " + e.toString());
			JOptionPane optionPane = new JOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(parent, "ERROR al inicializar la lista de vertederos");
			dialog.show();
		}
	}

    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPaneLista = new javax.swing.JScrollPane();
        jTableLista = new javax.swing.JTable();
        jPanelBotonera = new javax.swing.JPanel();
        jButtonSeleccionar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();

        setSize(new java.awt.Dimension(465, 465));

        jScrollPaneLista.setViewportView(jTableLista);
        ListSelectionModel rowSM = jTableLista.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				seleccionarVertedero(e);
			}
		});

        getContentPane().add(jScrollPaneLista, java.awt.BorderLayout.CENTER);

        jButtonSeleccionar.setMaximumSize(new java.awt.Dimension(95, 25));
        jButtonSeleccionar.setMinimumSize(new java.awt.Dimension(95, 25));
        jButtonSeleccionar.setPreferredSize(new java.awt.Dimension(95, 25));
        jButtonSeleccionar.setEnabled(false);
        jButtonSeleccionar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				seleccionar();
			}
		});
        jPanelBotonera.add(jButtonSeleccionar);

        jButtonSalir.setMaximumSize(new java.awt.Dimension(95, 25));
        jButtonSalir.setMinimumSize(new java.awt.Dimension(95, 25));
        jButtonSalir.setPreferredSize(new java.awt.Dimension(95, 25));
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				salir();
			}
		});
        jPanelBotonera.add(jButtonSalir);

        getContentPane().add(jPanelBotonera, java.awt.BorderLayout.SOUTH);

        changeScreenLang(messages);
        pack();
    }//GEN-END:initComponents


    private void actualizarModelo() {
		modelVertedero = new VertederoTableModel();
		modelVertedero.setModelData(listaVertederos);
		sorter = new TableSorted(modelVertedero);
		sorter.setTableHeader(jTableLista.getTableHeader());
		jTableLista.setModel(sorter);
		jTableLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	}
    public void changeScreenLang(ResourceBundle messages) {
        try
        {
            this.messages = messages;
            setTitle(messages.getString("JDialogBuscarVertedero.title"));//"Seleccionar vertedero");
            jButtonSeleccionar.setText(messages.getString("JDialogInspectores.jButtonSelect"));
            jButtonSalir.setText(messages.getString("JDialogBuscarVertedero.jButtonSalir"));

            VertederoTableModel.setColumnNames(new String[]{messages.getString("JPanelVertedero.VertederoTableModel.col0"), messages.getString("JPanelVertedero.VertederoTableModel.col1"), messages.getString("JPanelVertedero.VertederoTableModel.col2")});
            actualizarModelo();
        }catch(Exception e)
        {
            logger.error("Error al cargar las etiquetas: ",e);
        }
 	}
    private void salir()
    {
       okSelected=false;
       vertederoSelected=null;
       dispose();
    }

    private void seleccionar()
    {
       if (vertederoSelected!=null)
         okSelected=true;
       dispose();
    }

    private void seleccionarVertedero(ListSelectionEvent e) {
    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (!lsm.isSelectionEmpty())
        {
			int selectedRow = lsm.getMinSelectionIndex();
			String idVertedero = (String) sorter.getValueAt(selectedRow, VertederoTableModel.idIndex);
			vertederoSelected = getVertedero(idVertedero);
            jButtonSeleccionar.setEnabled(true);
		}
    }

    private Vertedero getVertedero(String idVertedero) {
		if (listaVertederos == null || idVertedero == null) return null;
		for (Enumeration e = listaVertederos.elements(); e.hasMoreElements();) {
			Vertedero aux = (Vertedero) e.nextElement();
			if (idVertedero.equalsIgnoreCase(aux.getId()))
				return aux;
		}
		return null;
	}

    public Vertedero getVertederoSelected() {
        return vertederoSelected;
    }

    public void setVertederoSelected(Vertedero vertederoSelected) {
        this.vertederoSelected = vertederoSelected;
    }
    public boolean isOkSelected()
    {
        return okSelected;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonSeleccionar;
    private javax.swing.JPanel jPanelBotonera;
    private javax.swing.JScrollPane jScrollPaneLista;
    private javax.swing.JTable jTableLista;
    // End of variables declaration//GEN-END:variables
    
}
