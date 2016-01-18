/**
 * JDialogBuscarArbolado.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * JDialogBuscarVertedero.java
 *
 * Created on 4 de mayo de 2005, 14:46
 */

package com.geopista.app.contaminantes.panel;

import com.geopista.protocol.contaminantes.OperacionesContaminantes;
import com.geopista.protocol.contaminantes.Arbolado;
import com.geopista.app.contaminantes.ArboladoTableModel;
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
public class JDialogBuscarArbolado extends javax.swing.JDialog {
    private ResourceBundle messages;
    ArboladoTableModel model;
    Vector lista = null;
    private JFrame parent;
	private TableSorted sorter;
	private Arbolado arboladoSelected = null;
    private boolean okSelected=false;
    protected Logger logger = Logger.getLogger(JDialogBuscarArbolado.class);



    /** Creates new form JDialogBuscarVertedero */
    public JDialogBuscarArbolado(java.awt.Frame parent,boolean modal,ResourceBundle messages) {
        super(parent, modal);
		this.messages = messages;
        this.parent = (JFrame)parent;
        initListas();
        initComponents();
    }
    private void initListas() {

		try {
			lista = (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).getArbolados();
			if (lista == null)
				logger.warn("No existen zonas arboladas en el sistema");
			else
				logger.info("Numero de zonas arboladas: " + lista.size());

		} catch (Exception e) {
			logger.error("Error al inicializa la lista de zonas arboladas: " + e.toString());
			JOptionPane optionPane = new JOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(parent, "ERROR al inicializar la lista de zonas arboladas");
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
		model = new ArboladoTableModel();
		model.setModelData(lista);
		sorter = new TableSorted(model);
		sorter.setTableHeader(jTableLista.getTableHeader());
		jTableLista.setModel(sorter);
		jTableLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	}
    public void changeScreenLang(ResourceBundle messages) {
        try
        {
            this.messages = messages;
            setTitle(messages.getString("JDialogBuscarArbolado.title"));//"Seleccionar vertedero");
            jButtonSeleccionar.setText(messages.getString("JDialogInspectores.jButtonSelect"));
            jButtonSalir.setText(messages.getString("JDialogBuscarArbolado.jButtonSalir"));

            ArboladoTableModel.setColumnNames(new String[]{messages.getString("JFrameArbolado.modelArbolado.col0.text"), messages.getString("JFrameArbolado.modelArbolado.col1.text"), messages.getString("JFrameArbolado.modelArbolado.col2.text")});
		    actualizarModelo();
        }catch(Exception e)
        {
            logger.error("Error al cargar las etiquetas: ",e);
        }
 	}
    private void salir()
    {
       okSelected=false;
       arboladoSelected=null;
       dispose();
    }

    private void seleccionar()
    {
       if (arboladoSelected!=null)
         okSelected=true;
       dispose();
    }

    private void seleccionarVertedero(ListSelectionEvent e) {
    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (!lsm.isSelectionEmpty())
        {
			int selectedRow = lsm.getMinSelectionIndex();
			String id = (String) sorter.getValueAt(selectedRow, ArboladoTableModel.idIndex);
			arboladoSelected = getArbolado(id);
            jButtonSeleccionar.setEnabled(true);
		}
    }

    private Arbolado getArbolado(String id) {
		if (lista == null || id == null) return null;
		for (Enumeration e = lista.elements(); e.hasMoreElements();) {
			Arbolado aux = (Arbolado) e.nextElement();
			if (id.equalsIgnoreCase(aux.getId()))
				return aux;
		}
		return null;
	}

    public Arbolado getArboladoSelected() {
        return arboladoSelected;
    }

    public void setArboladoSelected(Arbolado arboladoSelected) {
        this.arboladoSelected = arboladoSelected;
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

