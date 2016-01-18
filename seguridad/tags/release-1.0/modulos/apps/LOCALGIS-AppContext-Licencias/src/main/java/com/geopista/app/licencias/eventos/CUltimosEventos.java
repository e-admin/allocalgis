package com.geopista.app.licencias.eventos;
/**
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
import com.geopista.app.licencias.*;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CEventosTableModel;
import com.geopista.app.licencias.utilidades.CheckBoxRenderer;
import com.geopista.app.licencias.utilidades.CheckBoxTableEditor;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.app.licencias.actividad.MainActividad;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author charo
 */
public class CUltimosEventos extends javax.swing.JInternalFrame implements IMultilingue {

	private Vector _vEventosLast = null;
	private Vector _vExpedientesLast = null;
	private Vector _vSolicitudesLast = null;

	private Vector _vEventosSin = null;
	private Vector _vExpedientesSin = null;
	private Vector _vSolicitudesSin = null;
    private ResourceBundle literales;

	private JFrame desktop;

	/**
	 * Modelo para el componente resultadosJTable
	 */
	private DefaultTableModel _ultimosTableModel;
	private DefaultTableModel _pendientesTableModel;

    TableSorted ultimosSorted= new TableSorted();
    TableSorted pendientesSorted= new TableSorted();
    int ultimosHiddenCol= 10;
    int pendientesHiddenCol= 10;


	Logger logger = Logger.getLogger(CConsultaEventos.class);

	/**
	 * Creates new form CConsultaEventos
	 */
	public CUltimosEventos(JFrame desktop, ResourceBundle literales) {
		this.desktop = desktop;
        this.literales =  literales;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

		initComponents();
        initComboBoxesEstructuras();
        try
        {
            /** Debido a que las filas de la tabla se pueden ordenar por cualquiera de las columnas en
             * modo ascendente o descendente, el valor rowSelected ya no nos sirve para acceder al
             * vector de eventos y recoger el evento seleccionado.
             * Es necesario por tanto almacenar las posiciones que en el vector ocupan los eventos en la tabla. Para ello,
             * creamos una columna hidden con los valores de la posicion del evento en el vector.
             */
            String[] columnNamesUltimos= {literales.getString("CUltimosEventos.ultimosTableModel.text.column10"),
                               literales.getString("CUltimosEventos.ultimosTableModel.text.column1"),
                               literales.getString("CUltimosEventos.ultimosTableModel.text.column2"),
                               literales.getString("CUltimosEventos.ultimosTableModel.text.column3"),
                               literales.getString("CUltimosEventos.ultimosTableModel.text.column4"),
                               literales.getString("CUltimosEventos.ultimosTableModel.text.column5"),
                               literales.getString("CUltimosEventos.ultimosTableModel.text.column6"),
                               literales.getString("CUltimosEventos.ultimosTableModel.text.column7"),
                               literales.getString("CUltimosEventos.ultimosTableModel.text.column8"),
                               literales.getString("CUltimosEventos.ultimosTableModel.text.column9"),
                               "HIDDEN"};

            CEventosTableModel.setColumnNames(columnNamesUltimos);
        }catch(Exception e)
        {
            logger.error("Error al crear las cabeceras:",e);
        }
        _ultimosTableModel = new CEventosTableModel();

        try
        {
            String[] columnNamesPendientes= {literales.getString("CUltimosEventos.pendientesTableModel.text.column10"),
                               literales.getString("CUltimosEventos.pendientesTableModel.text.column1"),
                               literales.getString("CUltimosEventos.pendientesTableModel.text.column2"),
                               literales.getString("CUltimosEventos.pendientesTableModel.text.column3"),
                               literales.getString("CUltimosEventos.pendientesTableModel.text.column4"),
                               literales.getString("CUltimosEventos.pendientesTableModel.text.column5"),
                               literales.getString("CUltimosEventos.pendientesTableModel.text.column6"),
                               literales.getString("CUltimosEventos.pendientesTableModel.text.column7"),
                               literales.getString("CUltimosEventos.pendientesTableModel.text.column8"),
                               literales.getString("CUltimosEventos.pendientesTableModel.text.column9"),
                               "HIDDEN"};

           CEventosTableModel.setColumnNames(columnNamesPendientes);
        }catch(Exception e)
        {
            logger.error("Error al crear las cabeceras:",e);
        }

        _pendientesTableModel = new CEventosTableModel();

		buscarExpedienteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		fechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		fechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        borrarFechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
        borrarFechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);

		DNIJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);

        ultimosSorted= new TableSorted(_ultimosTableModel);
        ultimosSorted.setTableHeader(ultimosJTable.getTableHeader());
		ultimosJTable.setModel(ultimosSorted);
        ultimosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ultimosJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)ultimosJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ultimosJTableFocusGained();
            }
        });
        ((TableSorted)ultimosJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ultimosJTableMouseClicked();
            }
        });
        TableColumn col= ultimosJTable.getColumnModel().getColumn(ultimosHiddenCol);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);



        pendientesSorted= new TableSorted(_pendientesTableModel);
        pendientesSorted.setTableHeader(pendientesJTable.getTableHeader());               
		pendientesJTable.setModel(pendientesSorted);
        pendientesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pendientesJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)pendientesJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pendientesJTableFocusGained();
            }
        });
        ((TableSorted)pendientesJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendientesJTableMouseClicked();
            }
        });
        col= pendientesJTable.getColumnModel().getColumn(pendientesHiddenCol);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);


        fechaDesdeJTField.setEnabled(false);
        fechaHastaJTField.setEnabled(false);

        sinRevisarJRadioButton.setSelected(false);
        revisadosJRadioButton.setSelected(false);
        todosJRadioButton.setSelected(true);

		renombrarComponentes(literales);
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {//GEN-BEGIN:initComponents
        templateJScrollPane = new javax.swing.JScrollPane();
        templateJPanel = new javax.swing.JPanel();
        datosBusquedaJPanel = new javax.swing.JPanel();
        numExpedienteJLabel = new javax.swing.JLabel();
        numExpedienteJTField = new javax.swing.JTextField();
        fechaJLabel = new javax.swing.JLabel();
        fechaDesdeJTField = new javax.swing.JTextField();
        fechaHastaJTField = new javax.swing.JTextField();
        tipoLicenciaJLabel = new javax.swing.JLabel();
        buscarJButton = new javax.swing.JButton();
        DNIJTField = new javax.swing.JTextField();
        DNIJLabel = new javax.swing.JLabel();
        fechaDesdeJButton = new javax.swing.JButton();
        fechaHastaJButton = new javax.swing.JButton();
        DNIJButton = new javax.swing.JButton();
        buscarExpedienteJButton = new javax.swing.JButton();
        estadoJLabel = new javax.swing.JLabel();
        tipoObraJLabel = new javax.swing.JLabel();
        sinRevisarJRadioButton = new javax.swing.JRadioButton();
        revisadosJRadioButton = new javax.swing.JRadioButton();
        todosJRadioButton = new javax.swing.JRadioButton();
        borrarFechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();
        salirJButton = new javax.swing.JButton();
        ultimosEventosJPanel = new javax.swing.JPanel();
        ultimosJScrollPane = new javax.swing.JScrollPane();
        ultimosJTable = new javax.swing.JTable();
        ultimoJScrollPane = new javax.swing.JScrollPane();
        ultimoJTArea = new javax.swing.JTextArea();
        pendientesJPanel = new javax.swing.JPanel();
        pendienteJScrollPane = new javax.swing.JScrollPane();
        pendienteJTArea = new javax.swing.JTextArea();
        pendientesJScrollPane = new javax.swing.JScrollPane();
        pendientesJTable = new javax.swing.JTable();
        irExpedienteJButton = new javax.swing.JButton();

        setClosable(true);
        setTitle("Ultimos Eventos");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed();
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        templateJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosBusquedaJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder("Datos de B\u00fasqueda"));
        numExpedienteJLabel.setText("N\u00famero de Expediente:");
        datosBusquedaJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 74, 220, 20));

        datosBusquedaJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 74, 290, 20));

        fechaJLabel.setText("Fecha (desde/hasta):");
        datosBusquedaJPanel.add(fechaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 220, 20));

        datosBusquedaJPanel.add(fechaDesdeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 100, 20));

        datosBusquedaJPanel.add(fechaHastaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 140, 100, 20));

        tipoLicenciaJLabel.setText("Tipo Licencia:");
        datosBusquedaJPanel.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 220, 20));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, -1, -1));

        datosBusquedaJPanel.add(DNIJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 118, 290, 20));

        DNIJLabel.setText("DNI/CIF del Propietario:");
        datosBusquedaJPanel.add(DNIJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 118, 220, 20));

        fechaDesdeJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 140, 20, 20));

        fechaHastaJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, 20, 20));

        DNIJButton.setIcon(new javax.swing.ImageIcon(""));
        DNIJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        DNIJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        DNIJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DNIJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(DNIJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 118, 20, 20));

        buscarExpedienteJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 74, 20, 20));

        estadoJLabel.setText("Estado actual de Expediente:");
        datosBusquedaJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 96, 220, 20));

        tipoObraJLabel.setText("Tipo Obra");
        datosBusquedaJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 52, 220, 20));

        sinRevisarJRadioButton.setText("Sin Revisar");
        sinRevisarJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sinRevisarJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(sinRevisarJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 170, -1));

        revisadosJRadioButton.setText("Revisados");
        revisadosJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revisadosJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(revisadosJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 190, -1));

        todosJRadioButton.setText("Todos");
        todosJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todosJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(todosJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, 160, -1));

        borrarFechaDesdeJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarFechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 20, 20));

        borrarFechaHastaJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarFechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 20, 20));

        templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 980, 200));

        salirJButton.setText("Cancelar");
        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirJButtonActionPerformed();
            }
        });

        templateJPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 620, 90, -1));

        ultimosEventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ultimosEventosJPanel.setBorder(new javax.swing.border.TitledBorder("\u00daltimos Eventos"));
        ultimosJScrollPane.setBorder(new javax.swing.border.EtchedBorder());
        ultimosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        ultimosJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Licencia", "Num. Expediente", "Tipo Evento", "Fecha", "Revisado", "Revisado Por"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ultimosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        ultimosJTable.setFocusCycleRoot(true);
        ultimosJTable.setSurrendersFocusOnKeystroke(true);
        ultimosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ultimosJTableFocusGained();
            }
        });
        ultimosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ultimosJTableMouseClicked();
            }
        });
        ultimosJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                ultimosJTableMouseDragged();
            }
        });
        ultimosJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ultimosJTableKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ultimosJTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ultimosJTableKeyReleased(evt);
            }
        });


        ultimosJScrollPane.setViewportView(ultimosJTable);

        ultimosEventosJPanel.add(ultimosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 960, 110));

        ultimoJScrollPane.setBorder(new javax.swing.border.TitledBorder("Descripci\u00f3n del Evento Seleccionado"));
        ultimoJScrollPane.setViewportView(ultimoJTArea);

        ultimosEventosJPanel.add(ultimoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 960, 60));

        templateJPanel.add(ultimosEventosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 980, 200));

        pendientesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pendientesJPanel.setBorder(new javax.swing.border.TitledBorder("Eventos Pendientes de Revisar"));
        pendienteJScrollPane.setBorder(new javax.swing.border.TitledBorder("Descripci\u00f3n del Evento Seleccionado"));
        pendienteJScrollPane.setViewportView(pendienteJTArea);

        pendientesJPanel.add(pendienteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 960, 60));

        pendientesJScrollPane.setBorder(new javax.swing.border.EtchedBorder());
        pendientesJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        pendientesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Licencia", "Num. Expediente", "Tipo Evento", "Fecha", "Revisado", "Revisado Por"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pendientesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        pendientesJTable.setFocusCycleRoot(true);
        pendientesJTable.setSurrendersFocusOnKeystroke(true);
        pendientesJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pendientesJTableFocusGained();
            }
        });
        pendientesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendientesJTableMouseClicked();
            }
        });
        pendientesJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pendientesJTableMouseDragged();
            }
        });
        pendientesJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pendientesJTableKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pendientesJTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pendientesJTableKeyReleased(evt);
            }
        });


        pendientesJScrollPane.setViewportView(pendientesJTable);

        pendientesJPanel.add(pendientesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 960, 100));

        templateJPanel.add(pendientesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 980, 200));

        irExpedienteJButton.setText("Ir Expediente");
        irExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irExpedienteJButtonActionPerformed();
            }
        });

        templateJPanel.add(irExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 620, 130, -1));

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void irExpedienteJButtonActionPerformed() {//GEN-FIRST:event_irExpedienteJButtonActionPerformed
        int rowP= pendientesJTable.getSelectedRow();
        int rowU= ultimosJTable.getSelectedRow();
        if ((rowP == -1) && (rowU == -1)){
            mostrarMensaje(literales.getString("CUltimosEventos.mensaje4"));
            return;
        }

        Vector vEventos= new Vector();
        Vector vExpedientes= new Vector();
        Vector vSolicitudes= new Vector();
        TableSorted tableSorted= new TableSorted();
        int hiddenCol= -1;

        /** Recogemos la fila seleccionada */
        int row= rowP;
        if (row == -1){
            row= rowU;
            vEventos= _vEventosLast;
            vExpedientes= _vExpedientesLast;
            vSolicitudes= _vSolicitudesLast;
            tableSorted= ultimosSorted;
            hiddenCol= ultimosHiddenCol;
        }else{
            vEventos= _vEventosSin;
            vExpedientes= _vExpedientesSin;
            vSolicitudes= _vSolicitudesSin;
            tableSorted= pendientesSorted;
            hiddenCol= pendientesHiddenCol;
        }

        CEvento evento= (CEvento) vEventos.get(((Integer)tableSorted.getValueAt(row, hiddenCol)).intValue());
        CExpedienteLicencia expediente= (CExpedienteLicencia) vExpedientes.get(((Integer)tableSorted.getValueAt(row, hiddenCol)).intValue());
        CSolicitudLicencia solicitud= (CSolicitudLicencia) vSolicitudes.get(((Integer)tableSorted.getValueAt(row, hiddenCol)).intValue());

        if ((evento == null) || (expediente == null) || (solicitud == null)) return;

        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;

        /** Comprobamos si el expediente esta bloqueado */
        if (CUtilidadesComponentes.expedienteBloqueado(expediente.getNumExpediente(), literales.getLocale().toString())){
            if (CUtilidadesComponentes.mostrarMensajeBloqueo(this,literales, true) != 0) return;
        }

        /** Vamos a Modificacion de expediente */
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        dispose();
        if (desktop instanceof CMainLicencias){
            if (solicitud.getTipoLicencia().getIdTipolicencia() == CConstantesLicencias.ObraMayor){
                CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
                CConstantesLicencias.helpSetHomeID = "licenciasModificacionObraMayor";
                CMainLicencias mainLicencias = (CMainLicencias) desktop;
                mainLicencias.setLang("licenciasObraMayor",CConstantesLicencias.Locale);
                com.geopista.app.licencias.modificacion.CModificacionLicencias modificacionLicencias = new com.geopista.app.licencias.modificacion.CModificacionLicencias(desktop, expediente.getNumExpediente(), false);
                mainLicencias.mostrarJInternalFrame(modificacionLicencias);
            }else if (solicitud.getTipoLicencia().getIdTipolicencia() == CConstantesLicencias.ObraMenor){
                CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMenor;
                CConstantesLicencias.helpSetHomeID = "licenciasModificacionObraMenor";
                CMainLicencias mainLicencias = (CMainLicencias) desktop;
                mainLicencias.setLang("licenciasObraMenor",CConstantesLicencias.Locale);
                com.geopista.app.licencias.obraMenor.modificacion.CModificacionLicencias modificacionLicencias = new com.geopista.app.licencias.obraMenor.modificacion.CModificacionLicencias(desktop, expediente.getNumExpediente(), false);
                mainLicencias.mostrarJInternalFrame(modificacionLicencias);
            }
        }else{
            MainActividad mainActividad = (MainActividad) desktop;
            com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias modificacionLicencias = new com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias(desktop, expediente.getNumExpediente(),literales, false);
            mainActividad.mostrarJInternalFrame(modificacionLicencias);
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_irExpedienteJButtonActionPerformed

    private void pendientesJTableFocusGained() {//GEN-FIRST:event_pendientesJTableFocusGained
        ultimosJTable.clearSelection();
        ultimoJTArea.setText("");
        mostrarEventoPendienteSeleccionado();
    }//GEN-LAST:event_pendientesJTableFocusGained

    private void pendientesJTableMouseDragged() {//GEN-FIRST:event_pendientesJTableMouseDragged
        ultimosJTable.clearSelection();
        ultimoJTArea.setText("");
        mostrarEventoPendienteSeleccionado();
    }//GEN-LAST:event_pendientesJTableMouseDragged

    private void pendientesJTableKeyTyped(java.awt.event.KeyEvent evt) {
        ultimosJTable.clearSelection();
        ultimoJTArea.setText("");
        mostrarEventoPendienteSeleccionado();
    }
    private void pendientesJTableKeyPressed(java.awt.event.KeyEvent evt){
        ultimosJTable.clearSelection();
        ultimoJTArea.setText("");
        mostrarEventoPendienteSeleccionado();
    }
    private void pendientesJTableKeyReleased(java.awt.event.KeyEvent evt){
        ultimosJTable.clearSelection();
        ultimoJTArea.setText("");
        mostrarEventoPendienteSeleccionado();
    }



    private void ultimosJTableMouseDragged() {//GEN-FIRST:event_ultimosJTableMouseDragged
        pendientesJTable.clearSelection();
        pendienteJTArea.setText("");
        mostrarUltimoEventoSeleccionado();
    }//GEN-LAST:event_ultimosJTableMouseDragged

    private void ultimosJTableFocusGained() {//GEN-FIRST:event_ultimosJTableFocusGained
        pendientesJTable.clearSelection();
        pendienteJTArea.setText("");
        mostrarUltimoEventoSeleccionado();
    }//GEN-LAST:event_ultimosJTableFocusGained

    private void ultimosJTableKeyTyped(java.awt.event.KeyEvent evt) {
        pendientesJTable.clearSelection();
        pendienteJTArea.setText("");
        mostrarUltimoEventoSeleccionado();
    }
    private void ultimosJTableKeyPressed(java.awt.event.KeyEvent evt){
        pendientesJTable.clearSelection();
        pendienteJTArea.setText("");
        mostrarUltimoEventoSeleccionado();
    }
    private void ultimosJTableKeyReleased(java.awt.event.KeyEvent evt){
        pendientesJTable.clearSelection();
        pendienteJTArea.setText("");
        mostrarUltimoEventoSeleccionado();
    }

    private void borrarFechaHastaJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaHastaJButtonActionPerformed
        fechaHastaJTField.setText("");

    }//GEN-LAST:event_borrarFechaHastaJButtonActionPerformed

    private void borrarFechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaDesdeJButtonActionPerformed
        fechaDesdeJTField.setText("");
    }//GEN-LAST:event_borrarFechaDesdeJButtonActionPerformed

    private void todosJRadioButtonActionPerformed() {//GEN-FIRST:event_todosJRadioButtonActionPerformed
        if (todosJRadioButton.isSelected()){
            sinRevisarJRadioButton.setSelected(false);
            revisadosJRadioButton.setSelected(false);
        }
    }//GEN-LAST:event_todosJRadioButtonActionPerformed

    private void revisadosJRadioButtonActionPerformed() {//GEN-FIRST:event_revisadosJRadioButtonActionPerformed
        if (revisadosJRadioButton.isSelected()){
            sinRevisarJRadioButton.setSelected(false);
            todosJRadioButton.setSelected(false);
        }
    }//GEN-LAST:event_revisadosJRadioButtonActionPerformed

    private void sinRevisarJRadioButtonActionPerformed() {//GEN-FIRST:event_sinRevisarJRadioButtonActionPerformed
        if (sinRevisarJRadioButton.isSelected()){
            revisadosJRadioButton.setSelected(false);
            todosJRadioButton.setSelected(false);
        }
    }//GEN-LAST:event_sinRevisarJRadioButtonActionPerformed

    private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
        CConstantesLicencias.helpSetHomeID = "licenciasIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);
    }//GEN-LAST:event_formInternalFrameClosed

	private void pendientesJTableMouseClicked() {//GEN-FIRST:event_pendientesJTableMouseClicked
		ultimosJTable.clearSelection();
        ultimoJTArea.setText("");
        mostrarEventoPendienteSeleccionado();
	}//GEN-LAST:event_pendientesJTableMouseClicked

	private void ultimosJTableMouseClicked() {//GEN-FIRST:event_ultimosJTableMouseClicked
		pendientesJTable.clearSelection();
        pendienteJTArea.setText("");
        mostrarUltimoEventoSeleccionado();
	}//GEN-LAST:event_ultimosJTableMouseClicked

	private void fechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_fechaDesdeJButtonActionPerformed
		CUtilidadesComponentes.showCalendarDialog(desktop);

		if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
			fechaDesdeJTField.setText(CConstantesLicencias.calendarValue);
		}
	}//GEN-LAST:event_fechaDesdeJButtonActionPerformed

	private void fechaHastaJButtonActionPerformed() {//GEN-FIRST:event_fechaHastaJButtonActionPerformed
		CUtilidadesComponentes.showCalendarDialog(desktop);

		if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
			fechaHastaJTField.setText(CConstantesLicencias.calendarValue);
		}
	}//GEN-LAST:event_fechaHastaJButtonActionPerformed

	private void DNIJButtonActionPerformed() {//GEN-FIRST:event_DNIJButtonActionPerformed
		CUtilidadesComponentes.showPersonaDialog(desktop, literales);

		if (CConstantesLicencias_LCGIII.persona != null) {
			DNIJTField.setText(CConstantesLicencias_LCGIII.persona.getDniCif());
		}
	}//GEN-LAST:event_DNIJButtonActionPerformed

	private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed
		CUtilidadesComponentes.showSearchDialog(desktop, tipoLicenciaEJCBox.getSelectedPatron(), tipoObraEJCBox.getSelectedPatron(), estadoExpedienteEJCBox.getSelectedPatron(), DNIJTField.getText(),literales);
		numExpedienteJTField.setText(CConstantesLicencias.searchValue);
        if ((CConstantesLicencias.patronSelectedEstado != null) &&(CConstantesLicencias.patronSelectedTipoLicencia != null) && (CConstantesLicencias.patronSelectedTipoObra != null) && (CConstantesLicencias.selectedDniSolicitante != null)){
            estadoExpedienteEJCBox.setSelectedPatron(CConstantesLicencias.patronSelectedEstado);
            tipoLicenciaEJCBox.setSelectedPatron(CConstantesLicencias.patronSelectedTipoLicencia);
            tipoObraEJCBox.setSelectedPatron(CConstantesLicencias.patronSelectedTipoObra);
            if (CConstantesLicencias.selectedDniSolicitante.trim().length() > 0)
                DNIJTField.setText(CConstantesLicencias.selectedDniSolicitante);
        }
	}//GEN-LAST:event_buscarExpedienteJButtonActionPerformed

	private void buscarJButtonActionPerformed() {//GEN-FIRST:event_buscarJButtonActionPerformed
		Hashtable hash = new Hashtable();
		hash.put("E.NUM_EXPEDIENTE", numExpedienteJTField.getText());
        if (tipoLicenciaEJCBox.getSelectedIndex()!=0) {
			    hash.put("S.ID_TIPO_LICENCIA", tipoLicenciaEJCBox.getSelectedPatron());
		    }
        if (tipoObraEJCBox.getSelectedIndex()!=0) {
			hash.put("S.ID_TIPO_OBRA", tipoObraEJCBox.getSelectedPatron());
		}
		if (estadoExpedienteEJCBox.getSelectedIndex()!=0) {
			hash.put("E.ID_ESTADO", estadoExpedienteEJCBox.getSelectedPatron());
		}
		hash.put("DNI_CIF", DNIJTField.getText());
        /** comprobamos que radioButton ha sido seleccionado */
        if (revisadosJRadioButton.isSelected()){
            hash.put("V.REVISADO", "1");
        }else if (sinRevisarJRadioButton.isSelected()){
            hash.put("V.REVISADO", "0");
        }
		/** comprobamos que la fecha tiene formato valido */
		if ((CUtilidades.parseFechaStringToString(fechaDesdeJTField.getText()) == null) || (CUtilidades.parseFechaStringToString(fechaHastaJTField.getText()) == null)) {
			mostrarMensaje(literales.getString("CUltimosEventos.mensaje3"));
			return;
		} else {
			//Between entre fechas
			Date fechaDesde = CUtilidades.parseFechaStringToDate(fechaDesdeJTField.getText().trim());
			if (fechaDesdeJTField.getText().trim().equals("")) {
				fechaDesde = new Date(1);
			}
			Date fechaHasta = CUtilidades.parseFechaStringToDate(fechaHastaJTField.getText().trim());
			if (fechaHastaJTField.getText().trim().equals("")) {
				fechaHasta = new Date();
			}

            if ((fechaDesde != null) && (fechaHasta != null)) {
                /** comprobamos que fechaDesde sea menor que fechaHasta */
                long millisDesde= fechaDesde.getTime();
                long millisHasta= fechaHasta.getTime();
                if (millisDesde > millisHasta){
                    mostrarMensaje(literales.getString("CUltimosEventos.mensaje2"));
                    return;
                }
                String fechaDesdeFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaDesde);
                long millis = fechaHasta.getTime();
                /** annadimos un dia */
                millis += 24 * 60 * 60 * 1000;
                fechaHasta = new Date(millis);
                String fechaHastaFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaHasta);

                hash.put("BETWEEN*V.FECHA", fechaDesdeFormatted + "*" + fechaHastaFormatted);
            }
		}


		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Annadimos el editor CheckBox en la novena columna de las tablas ultimosJTable y pendientesJTable
		int vColIndexCheck = 8;
		TableColumn col6 = ultimosJTable.getColumnModel().getColumn(vColIndexCheck);
		col6.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
		col6.setCellRenderer(new CheckBoxRenderer());

		col6 = pendientesJTable.getColumnModel().getColumn(vColIndexCheck);
		col6.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
		col6.setCellRenderer(new CheckBoxRenderer());

		/** Datos de los eventos correspondientes a una solicitud y expediente */
		CResultadoOperacion roLast = COperacionesLicencias.getUltimosEventos(hash, ((IMainLicencias)desktop).getTiposLicencia(), literales.getLocale().toString());
        // Eliminamos la condicion porque siempre son los no revisados
        hash.remove("V.REVISADO");
		CResultadoOperacion roSin = COperacionesLicencias.getEventosSinRevisar(hash, ((IMainLicencias)desktop).getTiposLicencia(), literales.getLocale().toString());


		if (roLast != null) {
			_vEventosLast = roLast.getVector();
			_vExpedientesLast = roLast.getExpedientes();
			_vSolicitudesLast = roLast.getSolicitudes();
		}

		if (roSin != null) {
			_vEventosSin = roSin.getVector();
			_vExpedientesSin = roSin.getExpedientes();
			_vSolicitudesSin = roSin.getSolicitudes();
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		CUtilidadesComponentes.clearTable(_ultimosTableModel);
		ultimoJTArea.setText("");
		CUtilidadesComponentes.clearTable(_pendientesTableModel);
		pendienteJTArea.setText("");

		if (((_vEventosLast == null) || (_vEventosLast.size() == 0) || (_vExpedientesLast == null) || (_vExpedientesLast.size() == 0) || (_vSolicitudesLast == null) || (_vSolicitudesLast.size() == 0)) &&
				((_vEventosSin == null) || (_vEventosSin.size() == 0) || (_vExpedientesSin == null) || (_vExpedientesSin.size() == 0) || (_vSolicitudesSin == null) || (_vSolicitudesSin.size() == 0))) {
			JOptionPane.showMessageDialog(desktop, literales.getString("CUltimosEventos.mensaje1"));
			return;
		}

		if ((_vEventosLast != null) && (_vEventosLast.size() > 0) && (_vExpedientesLast != null) && (_vExpedientesLast.size() > 0) && (_vSolicitudesLast != null) && (_vSolicitudesLast.size() > 0)) {
			int cont = 0;
			String numExpAux = "";
			boolean annadimos = false;
			logger.info("CConstantesLicencias.N_UltimosEventos=" + CConstantesLicencias.N_UltimosEventos);

			if (CConstantesLicencias.N_UltimosEventos > 0) {
				for (int i = 0; i < _vEventosLast.size(); i++) {
					/** solo mostraremos los n ultimos eventos, siendo este parametro configurable */
					CEvento evento = (CEvento) _vEventosLast.elementAt(i);
					CExpedienteLicencia expediente = (CExpedienteLicencia) _vExpedientesLast.elementAt(i);
					CSolicitudLicencia solicitud = (CSolicitudLicencia) _vSolicitudesLast.elementAt(i);
                    try{
                        if (cont == 0) {
                            // es el primer evento del resultset
                            numExpAux = expediente.getNumExpediente();
                            cont++;
                            annadimos = true;
                        } else if ((cont < CConstantesLicencias.N_UltimosEventos) && (numExpAux.equalsIgnoreCase(expediente.getNumExpediente()))) {
                            cont++;
                            annadimos = true;
                        } else if ((cont < CConstantesLicencias.N_UltimosEventos) && (!numExpAux.equalsIgnoreCase(expediente.getNumExpediente()))) {
                            // cambiamos de expediente (no hay tantos eventos de expediente como CConstantesLicencias.N_UltimosEventos)
                            numExpAux = expediente.getNumExpediente();
                            cont = 1;
                            annadimos = true;
                        } else if ((cont >= CConstantesLicencias.N_UltimosEventos) && (numExpAux.equalsIgnoreCase(expediente.getNumExpediente()))) {
                            /** No hacemos nada, hasta que sea fin del resultset o cambiemos de expediente */
                            annadimos = false;
                        } else if ((cont >= CConstantesLicencias.N_UltimosEventos) && (!numExpAux.equalsIgnoreCase(expediente.getNumExpediente()))) {
                            /** hemos cambiado de expediente */
                            cont = 1;
                            numExpAux = expediente.getNumExpediente();
                            annadimos = true;
                        }

                        if (annadimos) {
                            CheckBoxRenderer renderCheck = (CheckBoxRenderer) ultimosJTable.getCellRenderer(i, vColIndexCheck);
                            if (evento.getRevisado().equalsIgnoreCase("1"))
                                renderCheck.setSelected(true);
                            else
                                renderCheck.setSelected(false);

                            String nombreSolicititante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());

                            /** tipo licencia */
                            if (solicitud.getTipoLicencia() != null){
                                int tipoLicencia= solicitud.getTipoLicencia().getIdTipolicencia();
                                String sTipoObra= "";
                                String sTipoLicencia="";
                                if (tipoLicencia == CConstantesLicencias.ObraMayor){
                                     sTipoObra= ((DomainNode)Estructuras.getListaTiposObra().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                                     sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                                }else if(tipoLicencia == CConstantesLicencias.ObraMenor){
                                     sTipoObra= ((DomainNode)Estructuras.getListaTiposObraMenor().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                                     sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                                }else if(tipoLicencia == CConstantesLicencias.Actividades || tipoLicencia==CConstantesLicencias.ActividadesNoCalificadas){
                                     sTipoObra= ((DomainNode)Estructuras.getListaTiposActividad().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                                     sTipoLicencia=((DomainNode)Estructuras.getListaTiposLicenciaActividad().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                                 }

                                /** Intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                                /** Ahora tipo de Vía y nombre del emplazamiento son obligatorios, por lo
                                 * que sacamos el emplazamiento de ahí y no de la lista de referencias catastrales. */
                                /*
                                String emplazamiento = "";
                                Vector vRef= solicitud.getReferenciasCatastrales();
                                if (vRef != null){
                                    boolean encontrado= false;
                                    Enumeration e = vRef.elements();
                                    while ((e.hasMoreElements()) && (!encontrado)){
                                        CReferenciaCatastral refCatastral= (CReferenciaCatastral)e.nextElement();
                                        if (refCatastral != null){
                                            emplazamiento= CUtilidades.componerCampo(refCatastral.getTipoVia(), refCatastral.getNombreVia(), refCatastral.getPrimerNumero());
                                            if (emplazamiento.trim().length() > 0) encontrado= true;
                                        }
                                    };
                                }
                                */

                                String emplazamiento= CUtilidades.componerCampo(solicitud.getTipoViaAfecta(), solicitud.getNombreViaAfecta(), solicitud.getNumeroViaAfecta());

                                _ultimosTableModel.addRow(new Object[]{
                                    expediente.getNumExpediente(),
                                    sTipoLicencia,
                                    sTipoObra,
                                    emplazamiento,
                                    ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(expediente.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                                    nombreSolicititante,
                                    ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(evento.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                                    CUtilidades.formatFecha(evento.getFechaEvento()),
                                    new Boolean(renderCheck.isSelected()),
                                    evento.getContent(),
                                    new Integer(i)});

                               annadimos = false;

                            }
    					}
                    }catch(Exception e){
                        logger.error("Error al crear la fila",e);
                    }
				}
			}
		}

		if ((_vEventosSin != null) && (_vEventosSin.size() > 0) && (_vExpedientesSin != null) && (_vExpedientesSin.size() > 0) && (_vSolicitudesSin != null) && (_vSolicitudesSin.size() > 0)) {

			for (int i = 0; i < _vEventosSin.size(); i++) {
				CEvento evento = (CEvento) _vEventosSin.elementAt(i);
				CExpedienteLicencia expediente = (CExpedienteLicencia) _vExpedientesSin.elementAt(i);
				CSolicitudLicencia solicitud = (CSolicitudLicencia) _vSolicitudesSin.elementAt(i);
                try{
                    //logger.info("evento.getIdEvento()=" + evento.getIdEvento() + " Revisado="+evento.getRevisado());
                    CheckBoxRenderer renderCheck = (CheckBoxRenderer) pendientesJTable.getCellRenderer(i, vColIndexCheck);
                    if (evento.getRevisado().equalsIgnoreCase("1"))
                        renderCheck.setSelected(true);
                    else
                        renderCheck.setSelected(false);

                    String nombreSolicititante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());

                    /** tipo licencia */
                    if (solicitud.getTipoLicencia() != null){
                        int tipoLicencia= solicitud.getTipoLicencia().getIdTipolicencia();
                        String sTipoObra= "";
                        String sTipoLicencia="";
                        if (tipoLicencia == CConstantesLicencias.ObraMayor){
                             sTipoObra= ((DomainNode)Estructuras.getListaTiposObra().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                             sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        }else if(tipoLicencia == CConstantesLicencias.ObraMenor){
                             sTipoObra= ((DomainNode)Estructuras.getListaTiposObraMenor().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                             sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        }else if(tipoLicencia == CConstantesLicencias.Actividades){
                             sTipoObra= ((DomainNode)Estructuras.getListaTiposActividad().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                             sTipoLicencia=((DomainNode)Estructuras.getListaTiposLicenciaActividad().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        }


                        /** Intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                        /** Ahora tipo de Vía y nombre del emplazamiento son obligatorios, por lo
                         * que sacamos el emplazamiento de ahí y no de la lista de referencias catastrales. */
                        /*
                        String emplazamiento = "";
                        Vector vRef= solicitud.getReferenciasCatastrales();
                        if (vRef != null){
                            boolean encontrado= false;
                            Enumeration e = vRef.elements();
                            while ((e.hasMoreElements()) && (!encontrado)){
                                CReferenciaCatastral refCatastral= (CReferenciaCatastral)e.nextElement();
                                if (refCatastral != null){
                                    emplazamiento= CUtilidades.componerCampo(refCatastral.getTipoVia(), refCatastral.getNombreVia(), refCatastral.getPrimerNumero());
                                    if (emplazamiento.trim().length() > 0) encontrado= true;
                                }
                            };
                        }
                        */
                        String emplazamiento= CUtilidades.componerCampo(solicitud.getTipoViaAfecta(), solicitud.getNombreViaAfecta(), solicitud.getNumeroViaAfecta());
                        _pendientesTableModel.addRow(new Object[]{
                            expediente.getNumExpediente(),
                            sTipoLicencia,
                            sTipoObra,
                            emplazamiento,
                            ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(expediente.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                            nombreSolicititante,
                            ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(evento.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                            CUtilidades.formatFecha(evento.getFechaEvento()),
                            new Boolean(renderCheck.isSelected()),
                            evento.getContent(),
                            new Integer(i)});

                    }
                }catch(Exception e){
                    logger.error("Error al crear la fila",e);
                }
			}
		}


	}//GEN-LAST:event_buscarJButtonActionPerformed

	private void salirJButtonActionPerformed() {//GEN-FIRST:event_salirJButtonActionPerformed
	    CConstantesLicencias.helpSetHomeID= "licenciasIntro";
		this.dispose();
	}//GEN-LAST:event_salirJButtonActionPerformed

    public void mostrarUltimoEventoSeleccionado(){
        int row = ultimosJTable.getSelectedRow();

        if (row != -1) {
            CEvento evento = (CEvento) _vEventosLast.get(((Integer)ultimosSorted.getValueAt(row, ultimosHiddenCol)).intValue());
            CExpedienteLicencia expediente= (CExpedienteLicencia)_vExpedientesLast.get(((Integer)ultimosSorted.getValueAt(row, ultimosHiddenCol)).intValue());
            if (expediente != null){
                numExpedienteJTField.setText(expediente.getNumExpediente());
            }

            ultimoJTArea.setText(evento.getContent());
        }else{
            clearUltimoEvento();
        }
    }

    public void clearUltimoEvento(){
        numExpedienteJTField.setText("");
        ultimoJTArea.setText("");

    }


    public void mostrarEventoPendienteSeleccionado(){
        int row = pendientesJTable.getSelectedRow();

        if (row != -1) {
            CEvento evento = (CEvento) _vEventosSin.get(((Integer)pendientesSorted.getValueAt(row, pendientesHiddenCol)).intValue());
            CExpedienteLicencia expediente= (CExpedienteLicencia)_vExpedientesSin.get(((Integer)pendientesSorted.getValueAt(row, pendientesHiddenCol)).intValue());
            if (expediente != null){
                numExpedienteJTField.setText(expediente.getNumExpediente());
            }

            pendienteJTArea.setText(evento.getContent());
        }else{
           clearPendientes();
        }
    }

    public void clearPendientes(){
        numExpedienteJTField.setText("");
        pendienteJTArea.setText("");
    }


	/*******************************************************************/
	/*                         Metodos propios                         */
	/**
	 * ****************************************************************
	 */

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(datosBusquedaJPanel, mensaje);
	}

	/*********************************************************************/

	/**
	 * Exit the Application
	 */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		System.exit(0);
	}//GEN-LAST:event_exitForm

	private ComboBoxEstructuras tipoLicenciaEJCBox;
    private ComboBoxEstructuras tipoObraEJCBox;
    private ComboBoxEstructuras estadoExpedienteEJCBox;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DNIJButton;
    private javax.swing.JLabel DNIJLabel;
    private javax.swing.JTextField DNIJTField;
    private javax.swing.JButton borrarFechaDesdeJButton;
    private javax.swing.JButton borrarFechaHastaJButton;
    private javax.swing.JButton buscarExpedienteJButton;
    private javax.swing.JButton buscarJButton;
    private javax.swing.JPanel datosBusquedaJPanel;
    private javax.swing.JLabel estadoJLabel;
    private javax.swing.JButton fechaDesdeJButton;
    private javax.swing.JTextField fechaDesdeJTField;
    private javax.swing.JButton fechaHastaJButton;
    private javax.swing.JTextField fechaHastaJTField;
    private javax.swing.JLabel fechaJLabel;
    private javax.swing.JButton irExpedienteJButton;
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JScrollPane pendienteJScrollPane;
    private javax.swing.JTextArea pendienteJTArea;
    private javax.swing.JPanel pendientesJPanel;
    private javax.swing.JScrollPane pendientesJScrollPane;
    private javax.swing.JTable pendientesJTable;
    private javax.swing.JRadioButton revisadosJRadioButton;
    private javax.swing.JButton salirJButton;
    private javax.swing.JRadioButton sinRevisarJRadioButton;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoLicenciaJLabel;
    private javax.swing.JLabel tipoObraJLabel;
    private javax.swing.JRadioButton todosJRadioButton;
    private javax.swing.JScrollPane ultimoJScrollPane;
    private javax.swing.JTextArea ultimoJTArea;
    private javax.swing.JPanel ultimosEventosJPanel;
    private javax.swing.JScrollPane ultimosJScrollPane;
    private javax.swing.JTable ultimosJTable;
    //private javax.swing.JTextField jTextFieldTipoLicencia;
    // End of variables declaration//GEN-END:variables

	public void renombrarComponentes(ResourceBundle literales) {
        this.literales=literales;

		try {
            fechaDesdeJTField.setEnabled(false);
            fechaHastaJTField.setEnabled(false);

			setTitle(literales.getString("CUltimosEventos.JInternalFrame.title"));
			datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CUltimosEventos.datosBusquedaJPanel.TitleBorder")));
			ultimosEventosJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CUltimosEventos.ultimosEventosJPanel.TitleBorder")));
			pendientesJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CUltimosEventos.pendientesJPanel.TitleBorder")));

			ultimoJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CUltimosEventos.ultimoJScrollPane.TitleBorder")));
			pendienteJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CUltimosEventos.pendienteJScrollPane.TitleBorder")));

			tipoLicenciaJLabel.setText(literales.getString("CUltimosEventos.tipoLicenciaJLabel.text"));
            tipoObraJLabel.setText(literales.getString("CUltimosEventos.tipoObraJLabel.text"));
			numExpedienteJLabel.setText(literales.getString("CUltimosEventos.numExpedienteJLabel.text"));
			estadoJLabel.setText(literales.getString("CUltimosEventos.estadoJLabel.text"));
			DNIJLabel.setText(literales.getString("CUltimosEventos.DNIJLabel.text"));
			fechaJLabel.setText(literales.getString("CUltimosEventos.fechaJLabel.text"));

			buscarJButton.setText(literales.getString("CUltimosEventos.buscarJButton.text"));
			salirJButton.setText(literales.getString("CUltimosEventos.salirJButton.text"));
            irExpedienteJButton.setText(literales.getString("CUltimosEventos.irExpedienteJButton.text"));

            buscarJButton.setToolTipText(literales.getString("CUltimosEventos.buscarJButton.text"));
            salirJButton.setToolTipText(literales.getString("CUltimosEventos.salirJButton.text"));
            irExpedienteJButton.setToolTipText(literales.getString("CUltimosEventos.irExpedienteJButton.text"));

            sinRevisarJRadioButton.setText(literales.getString("CUltimosEventos.sinRevisarJRadioButton.text"));
            revisadosJRadioButton.setText(literales.getString("CUltimosEventos.revisadosJRadioButton.text"));
            todosJRadioButton.setText(literales.getString("CUltimosEventos.todosJRadioButton.text"));

            /** Headers tabla ultimos eventos */
            TableColumn tableColumn= ultimosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column10"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column1"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column2"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column3"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column4"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column5"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column6"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column7"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column8"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(9);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.ultimosTableModel.text.column9"));

            /** Headers tabla eventos pendientes */
            tableColumn= pendientesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column10"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column1"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column2"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column3"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column4"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column5"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column6"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column7"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column8"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(9);
            tableColumn.setHeaderValue(literales.getString("CUltimosEventos.pendientesTableModel.text.column9"));

            fechaDesdeJButton.setToolTipText(literales.getString("CUltimosEventos.fechaDesdeJButton.setToolTip.text"));
            fechaHastaJButton.setToolTipText(literales.getString("CUltimosEventos.fechaHastaJButton.setToolTip.text"));
            DNIJButton.setToolTipText(literales.getString("CUltimosEventos.DNIJButton.setToolTip.text"));
            buscarExpedienteJButton.setToolTipText(literales.getString("CUltimosEventos.buscarExpedienteJButton.setToolTip.text"));
            borrarFechaDesdeJButton.setToolTipText(literales.getString("CUltimosEventos.borrarFechaDesdeJButton.setToolTip.text"));
            borrarFechaHastaJButton.setToolTipText(literales.getString("CUltimosEventos.borrarFechaHastaJButton.setToolTip.text"));

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

    public void initComboBoxesEstructuras(){
        while (!Estructuras.isCargada())
        {
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }

        /** Inicializamos los comboBox que llevan estructuras */
        tipoLicenciaEJCBox= new ComboBoxEstructuras(Estructuras.getListaLicencias(), null, literales.getLocale().toString(), true);
        tipoLicenciaEJCBox.setSelectedIndex(0);
        if (desktop instanceof CMainLicencias)
        {
            datosBusquedaJPanel.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 310, 20));
            tipoLicenciaEJCBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoLicenciaEJCBoxActionPerformed();}});
            tipoObraEJCBox= new ComboBoxEstructuras(new ListaEstructuras(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setEnabled(false);
        }
         if (desktop instanceof MainActividad)
        {
            tipoLicenciaEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposLicenciaActividad(), null, literales.getLocale().toString(), true);
            tipoLicenciaEJCBox.setSelectedIndex(0);
            datosBusquedaJPanel.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 310, 20));
            tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposActividad(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setEnabled(true); 
        }
        tipoObraEJCBox.setSelectedIndex(0);
        datosBusquedaJPanel.add(tipoObraEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 52, 310, 20));
        estadoExpedienteEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstados(), null, literales.getLocale().toString(), true);
        estadoExpedienteEJCBox.setSelectedIndex(0);
        datosBusquedaJPanel.add(estadoExpedienteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 96, 310, 20));
    }


    private void tipoLicenciaEJCBoxActionPerformed() {
       if (tipoLicenciaEJCBox.getSelectedIndex()==0) {
            tipoObraEJCBox.removeAllItems();
            tipoObraEJCBox.setEstructuras(new ListaEstructuras(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(false);
        }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesLicencias.ObraMayor).toString())){
            tipoObraEJCBox.removeAllItems();
            tipoObraEJCBox.setEstructuras(Estructuras.getListaTiposObra(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(true);
        }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesLicencias.ObraMenor).toString())){
            tipoObraEJCBox.removeAllItems();
            tipoObraEJCBox.setEstructuras(Estructuras.getListaTiposObraMenor(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(true);
        }

    }

    public void setEnabledIrExpedienteJButton(boolean b){
        irExpedienteJButton.setEnabled(b);
    }



}
