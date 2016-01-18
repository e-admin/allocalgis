/*
 * CConsultaEventos.java
 *
 * Created on 16 de abril de 2004, 14:38
 */

package com.geopista.app.ocupaciones.eventos;

import com.geopista.app.ocupaciones.*;
import com.geopista.app.ocupaciones.estructuras.Estructuras;
import com.geopista.app.ocupaciones.tableModels.CEventosTableModel;
import com.geopista.app.ocupaciones.utilidades.CheckBoxRenderer;
import com.geopista.app.ocupaciones.utilidades.CheckBoxTableEditor;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.licencias.*;
import com.geopista.protocol.administrador.dominios.DomainNode;


import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author charo
 */
public class CUltimosEventos extends javax.swing.JInternalFrame implements IMultilingue{

	private Vector _vEventosLast = null;
	private Vector _vExpedientesLast = null;
	private Vector _vSolicitudesLast = null;

	private Vector _vEventosSin = null;
	private Vector _vExpedientesSin = null;
	private Vector _vSolicitudesSin = null;

	private JFrame desktop;

	/**
	 * Modelo para el componente resultadosJTable
	 */
	private DefaultTableModel _ultimosTableModel;
	private DefaultTableModel _pendientesTableModel;
    TableSorted ultimosSorted= new TableSorted();
    TableSorted pendientesSorted= new TableSorted();
    int ultimosHiddenCol= 8;
    int pendientesHiddenCol= 8;

    /** Necesario para ir a Modificacion de expdiente */
    private CExpedienteLicencia _expedienteSelected= null;

	Logger logger = Logger.getLogger(CConsultaEventos.class);

	/**
	 * Creates new form CConsultaEventos
	 */
	public CUltimosEventos(JFrame desktop) {
		this.desktop = desktop;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);


		initComponents();
        initComboBoxesEstructuras();
        String[] columnNamesUltimos= {CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column1"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column2"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column3"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column3"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column5"),
                               //CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column5"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column6"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column7"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column8"),
                               "HIDDEN"};

        CEventosTableModel.setColumnNames(columnNamesUltimos);

        String[] columnNamesPendientes= {CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column1"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column2"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column3"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column4"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column5"),
                               //CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column5"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column6"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column7"),
                               CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column8"),
                               "HIDDEN"};

        CEventosTableModel.setColumnNames(columnNamesPendientes);

		buscarExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		fechaDesdeJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		fechaHastaJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		DNIJButton.setIcon(CUtilidadesComponentes.iconoZoom);

        _ultimosTableModel = new CEventosTableModel();
        ultimosSorted= new TableSorted(_ultimosTableModel);
        ultimosSorted.setTableHeader(ultimosJTable.getTableHeader());
		ultimosJTable.setModel(ultimosSorted);
        ultimosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ultimosJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)ultimosJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ultimos();
            }
        });
        ((TableSorted)ultimosJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ultimos();
            }
        });
        TableColumn col= ultimosJTable.getColumnModel().getColumn(ultimosHiddenCol);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);

        _pendientesTableModel = new CEventosTableModel();
        pendientesSorted= new TableSorted(_pendientesTableModel);
        pendientesSorted.setTableHeader(pendientesJTable.getTableHeader());
		pendientesJTable.setModel(pendientesSorted);
        pendientesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pendientesJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)pendientesJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pendientes();
            }
        });
        ((TableSorted)pendientesJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendientes();
            }
        });
        col= pendientesJTable.getColumnModel().getColumn(pendientesHiddenCol);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);


        sinRevisarJRadioButton.setSelected(true);
        revisadosJRadioButton.setSelected(false);
        todosJRadioButton.setSelected(false);

		renombrarComponentes();
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
        borrarFechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();
        sinRevisarJRadioButton = new javax.swing.JRadioButton();
        revisadosJRadioButton = new javax.swing.JRadioButton();
        todosJRadioButton = new javax.swing.JRadioButton();
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
        datosBusquedaJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 52, -1, -1));

        datosBusquedaJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 52, 290, 20));
        datosBusquedaJPanel.add(fechaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 118, -1, -1));

        fechaDesdeJTField.setEnabled(false);
        datosBusquedaJPanel.add(fechaDesdeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 118, 100, 20));

        fechaHastaJTField.setEnabled(false);
        datosBusquedaJPanel.add(fechaHastaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 118, 100, 20));

        datosBusquedaJPanel.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 150, -1, -1));
        datosBusquedaJPanel.add(DNIJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 96, 290, 20));
        datosBusquedaJPanel.add(DNIJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 96, -1, -1));

        fechaDesdeJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 118, 20, 20));

        fechaHastaJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 118, 20, 20));

        DNIJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        DNIJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        DNIJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DNIJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(DNIJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 96, 20, 20));

        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 52, 20, 20));
        datosBusquedaJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 74, -1, -1));
        borrarFechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 118, 20, 20));

        borrarFechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 118, 20, 20));
        sinRevisarJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sinRevisarJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(sinRevisarJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 170, -1));
        revisadosJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revisadosJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(revisadosJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 190, -1));
        todosJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todosJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(todosJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, 160, -1));

        templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 980, 180));

        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirJButtonActionPerformed();
            }
        });

        templateJPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 630, 90, -1));

        irExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irExpedienteJButtonActionPerformed();
            }
        });

        templateJPanel.add(irExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 630, 130, -1));


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
                ultimos();
            }
        });
        ultimosJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ultimos();
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ultimos();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ultimos();
            }
        });
        ultimosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ultimos();
            }
        });
        ultimosJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                ultimos();
            }
        });

        ultimosJScrollPane.setViewportView(ultimosJTable);

        ultimosEventosJPanel.add(ultimosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 960, 110));

        ultimoJScrollPane.setBorder(new javax.swing.border.TitledBorder("Descripci\u00f3n del Evento Seleccionado"));
        ultimoJTArea.setLineWrap(true);
        ultimoJTArea.setWrapStyleWord(true);
        ultimoJScrollPane.setViewportView(ultimoJTArea);

        ultimosEventosJPanel.add(ultimoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 960, 80));

        templateJPanel.add(ultimosEventosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 980, 220));

        pendientesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pendientesJPanel.setBorder(new javax.swing.border.TitledBorder("Eventos Pendientes de Revisar"));
        pendienteJScrollPane.setBorder(new javax.swing.border.TitledBorder("Descripci\u00f3n del Evento Seleccionado"));
        pendienteJTArea.setLineWrap(true);
        pendienteJTArea.setWrapStyleWord(true);
        pendienteJScrollPane.setViewportView(pendienteJTArea);

        pendientesJPanel.add(pendienteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 960, 80));

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
                pendientes();
            }
        });
        pendientesJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pendientes();
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pendientes();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pendientes();
            }
        });
        pendientesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendientes();
            }
        });
        pendientesJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pendientes();
            }
        });

        pendientesJScrollPane.setViewportView(pendientesJTable);

        pendientesJPanel.add(pendientesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 960, 100));

        templateJPanel.add(pendientesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 980, 210));

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void pendientes() {//GEN-FIRST:event_pendientesJTableKeyTyped
        pendienteJTArea.setText("");
        mostrarEventoPendienteSeleccionado();
    }//GEN-LAST:event_pendientesJTableKeyTyped

    private void ultimos() {//GEN-FIRST:event_ultimosJTableKeyReleased
        ultimoJTArea.setText("");
        mostrarUltimoEventoSeleccionado();
    }//GEN-LAST:event_ultimosJTableKeyReleased


    private void irExpedienteJButtonActionPerformed(){

        if (_expedienteSelected == null) {
            mostrarMensaje(CMainOcupaciones.literales.getString("Eventos.mensaje2"));
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesOcupaciones.helpSetHomeID = "ocupacionesModificacion";

        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

        /** Comprobamos si el expediente esta bloqueado */
        if (CUtilidadesComponentes.expedienteBloqueado(_expedienteSelected.getNumExpediente(), CMainOcupaciones.currentLocale)){
            if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, true) != 0){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }
        }
        dispose();

        CMainOcupaciones mainOcupaciones = (CMainOcupaciones)desktop;
        mainOcupaciones.mostrarJInternalFrame(new com.geopista.app.ocupaciones.modificacion.CModificacionLicencias((JFrame)this.desktop, _expedienteSelected.getNumExpediente(), false));

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }

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

    private void borrarFechaHastaJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaHastaJButtonActionPerformed
        fechaHastaJTField.setText("");
    }//GEN-LAST:event_borrarFechaHastaJButtonActionPerformed

    private void borrarFechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaDesdeJButtonActionPerformed
        fechaDesdeJTField.setText("");
    }//GEN-LAST:event_borrarFechaDesdeJButtonActionPerformed

    private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
        CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);        
    }//GEN-LAST:event_formInternalFrameClosed


    private void mostrarEventoPendienteSeleccionado(){
        int row = pendientesJTable.getSelectedRow();

        if (row != -1) {

            if (ultimosJTable.getSelectedRow() != -1){
                ultimosJTable.clearSelection();
                ultimoJTArea.setText("");
            }

            CEvento evento = (CEvento) _vEventosSin.get(((Integer)pendientesSorted.getValueAt(row, pendientesHiddenCol)).intValue());
            pendienteJTArea.setText(evento.getContent());

            _expedienteSelected= (CExpedienteLicencia)_vExpedientesSin.get(((Integer)pendientesSorted.getValueAt(row, pendientesHiddenCol)).intValue());
        }else _expedienteSelected= null;
    }


    private void mostrarUltimoEventoSeleccionado(){
        int row = ultimosJTable.getSelectedRow();

        if (row != -1) {
            if (pendientesJTable.getSelectedRow() != -1){
                pendientesJTable.clearSelection();
                pendienteJTArea.setText("");
            }

            CEvento evento = (CEvento) _vEventosLast.get(((Integer)ultimosSorted.getValueAt(row, ultimosHiddenCol)).intValue());
            ultimoJTArea.setText(evento.getContent());

            _expedienteSelected= (CExpedienteLicencia)_vExpedientesLast.get(((Integer)ultimosSorted.getValueAt(row, ultimosHiddenCol)).intValue());
        }else _expedienteSelected= null;
    }

	private void fechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_fechaDesdeJButtonActionPerformed
	    CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(desktop);
        if (dialog != null){
            String fecha= dialog.getFechaSelected();
            if ((fecha != null) && (!fecha.trim().equals(""))) {
                fechaDesdeJTField.setText(fecha);
            }
        }

	}//GEN-LAST:event_fechaDesdeJButtonActionPerformed

	private void fechaHastaJButtonActionPerformed() {//GEN-FIRST:event_fechaHastaJButtonActionPerformed
	    CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(desktop);
        if (dialog != null){
            String fecha= dialog.getFechaSelected();
            if ((fecha != null) && (!fecha.trim().equals(""))) {
                fechaHastaJTField.setText(fecha);
            }
        }

	}//GEN-LAST:event_fechaHastaJButtonActionPerformed

	private void DNIJButtonActionPerformed() {//GEN-FIRST:event_DNIJButtonActionPerformed
	    CPersonaJDialog dialog= CUtilidadesComponentes.showPersonaDialog(desktop);
        if (dialog != null){
            CPersonaJuridicoFisica persona = dialog.getPersona();
            if ((persona != null) && (persona.getDniCif() != null)) {
                DNIJTField.setText(persona.getDniCif());
            }
        }

	}//GEN-LAST:event_DNIJButtonActionPerformed

	private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed
		 CSearchJDialog dialog= CUtilidadesComponentes.showSearchDialog(desktop, false);
        if ((dialog != null) && (dialog.getSelectedExpediente() != null)){
            numExpedienteJTField.setText(dialog.getSelectedExpediente());
        }

	}//GEN-LAST:event_buscarExpedienteJButtonActionPerformed

	private void buscarJButtonActionPerformed() {//GEN-FIRST:event_buscarJButtonActionPerformed
		Hashtable hash = new Hashtable();
		hash.put("E.NUM_EXPEDIENTE", numExpedienteJTField.getText());
		if (tipoLicenciaEJCBox.getSelectedIndex() != 0) {
			hash.put("D.TIPO_OCUPACION", tipoLicenciaEJCBox.getSelectedPatron());
		}
		if (estadoExpedienteEJCBox.getSelectedIndex() != 0) {
			hash.put("E.ID_ESTADO", estadoExpedienteEJCBox.getSelectedPatron());
		}
		hash.put("DNI_CIF", DNIJTField.getText());
        /** comprobamos que radioButton ha sido seleccionado */
        if (revisadosJRadioButton.isSelected()){
            hash.put("V.REVISADO", "1");
        }else if (sinRevisarJRadioButton.isSelected()){
            hash.put("V.REVISADO", "0");
        }

        /** Fechas de Busqueda */
        if ((CUtilidades.parseFechaStringToString(fechaDesdeJTField.getText()) == null) || (CUtilidades.parseFechaStringToString(fechaHastaJTField.getText()) == null)) {
            mostrarMensaje(CMainOcupaciones.literales.getString("CSearchJDialog.mensaje2"));
            return;
        } else {

            //Between entre fechas
            Date fechaDesde = CUtilidades.parseFechaStringToDate(fechaDesdeJTField.getText());
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
                    mostrarMensaje(CMainOcupaciones.literales.getString("CSearchJDialog.mensaje1"));
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

		// Annadimos el editor CheckBox en la octava columna de las tablas ultimosJTable y pendientesJTable
		int vColIndexCheck = 6;
		TableColumn col6 = ultimosJTable.getColumnModel().getColumn(vColIndexCheck);
		col6.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
		col6.setCellRenderer(new CheckBoxRenderer());

		col6 = pendientesJTable.getColumnModel().getColumn(vColIndexCheck);
		col6.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
		col6.setCellRenderer(new CheckBoxRenderer());

		/** Datos de los eventos correspondientes a una solicitud y expediente */
		CResultadoOperacion roLast = COperacionesLicencias.getUltimosEventos(hash, null, CMainOcupaciones.literales.getLocale().toString());
        // Eliminamos la condicion porque siempre son los no revisados
        hash.remove("V.REVISADO");        
		CResultadoOperacion roSin = COperacionesLicencias.getEventosSinRevisar(hash, null, CMainOcupaciones.literales.getLocale().toString());

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

        /** Redefinimos el tamanno de las celdas fecha */
        TableColumn column = ultimosJTable.getColumnModel().getColumn(5);
        CUtilidades.resizeColumn(column, 70);
        column = ultimosJTable.getColumnModel().getColumn(6);
        CUtilidades.resizeColumn(column, 70);
        column = pendientesJTable.getColumnModel().getColumn(5);
        CUtilidades.resizeColumn(column, 70);
        column = pendientesJTable.getColumnModel().getColumn(6);
        CUtilidades.resizeColumn(column, 70);


		if (((_vEventosLast == null) || (_vEventosLast.size() == 0) || (_vExpedientesLast == null) || (_vExpedientesLast.size() == 0) || (_vSolicitudesLast == null) || (_vSolicitudesLast.size() == 0)) &&
				((_vEventosSin == null) || (_vEventosSin.size() == 0) || (_vExpedientesSin == null) || (_vExpedientesSin.size() == 0) || (_vSolicitudesSin == null) || (_vSolicitudesSin.size() == 0))) {
			JOptionPane.showMessageDialog(desktop, CMainOcupaciones.literales.getString("Eventos.mensaje1"));
			return;
		}

		if ((_vEventosLast != null) && (_vEventosLast.size() > 0) && (_vExpedientesLast != null) && (_vExpedientesLast.size() > 0) && (_vSolicitudesLast != null) && (_vSolicitudesLast.size() > 0)) {
			int cont = 0;
			String numExpAux = "";
			boolean annadimos = false;

			System.out.println("CConstantesOcupaciones.N_UltimosEventos=" + CConstantesOcupaciones.N_UltimosEventos);
			if (CConstantesOcupaciones.N_UltimosEventos > 0) {
				for (int i = 0; i < _vEventosLast.size(); i++) {
					/** solo mostraremos los n ultimos eventos, siendo este parametro configurable */
					CEvento evento = (CEvento) _vEventosLast.elementAt(i);
					CExpedienteLicencia expediente = (CExpedienteLicencia) _vExpedientesLast.elementAt(i);
					CSolicitudLicencia solicitud = (CSolicitudLicencia) _vSolicitudesLast.elementAt(i);

					if (cont == 0) {
						// es el primer evento del resultset
						numExpAux = expediente.getNumExpediente();
						cont++;
						annadimos = true;
					} else if ((cont < CConstantesOcupaciones.N_UltimosEventos) && (numExpAux.equalsIgnoreCase(expediente.getNumExpediente()))) {
						cont++;
						annadimos = true;
					} else if ((cont < CConstantesOcupaciones.N_UltimosEventos) && (!numExpAux.equalsIgnoreCase(expediente.getNumExpediente()))) {
						// cambiamos de expediente (no hay tantos eventos de expediente como CConstantesOcupaciones.N_UltimosEventos)
						numExpAux = expediente.getNumExpediente();
						cont = 1;
						annadimos = true;
					} else if ((cont >= CConstantesOcupaciones.N_UltimosEventos) && (numExpAux.equalsIgnoreCase(expediente.getNumExpediente()))) {
						/** No hacemos nada, hasta que sea fin del resultset o cambiemos de expediente */
						annadimos = false;
					} else if ((cont >= CConstantesOcupaciones.N_UltimosEventos) && (!numExpAux.equalsIgnoreCase(expediente.getNumExpediente()))) {
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

                            String nombreSolicitante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());
                            /** intentamos mostrar el emplazamiento, y si no, la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                            String emplazamiento= CUtilidades.getEmplazamiento(solicitud);

                        _ultimosTableModel.addRow(new Object[]{
                            ((DomainNode)Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(solicitud.getDatosOcupacion().getTipoOcupacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
                            expediente.getNumExpediente(),
                            emplazamiento,
                            ((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(expediente.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
                            //solicitud.getPropietario().getDniCif(),
                            nombreSolicitante,
                            //new Long(evento.getIdEvento()).toString(),
                            CUtilidades.formatFecha(evento.getFechaEvento()), new Boolean(renderCheck.isSelected()), evento.getContent(), new Integer(i)});

						annadimos = false;
					}
				}
			}
		}


		if ((_vEventosSin != null) && (_vEventosSin.size() > 0) && (_vExpedientesSin != null) && (_vExpedientesSin.size() > 0) && (_vSolicitudesSin != null) && (_vSolicitudesSin.size() > 0)) {

			for (int i = 0; i < _vEventosSin.size(); i++) {
				CEvento evento = (CEvento) _vEventosSin.elementAt(i);
				CExpedienteLicencia expediente = (CExpedienteLicencia) _vExpedientesSin.elementAt(i);
				CSolicitudLicencia solicitud = (CSolicitudLicencia) _vSolicitudesSin.elementAt(i);

				//logger.info("evento.getIdEvento()=" + evento.getIdEvento() + " Revisado="+evento.getRevisado());
				CheckBoxRenderer renderCheck = (CheckBoxRenderer) pendientesJTable.getCellRenderer(i, vColIndexCheck);
				if (evento.getRevisado().equalsIgnoreCase("1"))
					renderCheck.setSelected(true);
				else
					renderCheck.setSelected(false);
				/*
				JCheckBox check= (JCheckBox) ((CheckBoxTableEditor) pendientesJTable.getCellEditor(i, 5)).getComponent();
				if (evento.getRevisado().equalsIgnoreCase("1")) check.setSelected(true);
				else check.setSelected(false);
				*/

                String nombreSolicitante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());
                /** intentamos mostrar el emplazamiento, y si no, la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                String emplazamiento= CUtilidades.getEmplazamiento(solicitud);

                _pendientesTableModel.addRow(new Object[]{
                    ((DomainNode)Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(solicitud.getDatosOcupacion().getTipoOcupacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
                    expediente.getNumExpediente(),
                    emplazamiento,
                    ((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(expediente.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
                    //solicitud.getPropietario().getDniCif(),
                    nombreSolicitante,
                    //new Long(evento.getIdEvento()).toString(),
                    CUtilidades.formatFecha(evento.getFechaEvento()), new Boolean(renderCheck.isSelected()), evento.getContent(), new Integer(i)});

			}
		}


	}//GEN-LAST:event_buscarJButtonActionPerformed

	private void salirJButtonActionPerformed() {//GEN-FIRST:event_salirJButtonActionPerformed
		this.dispose();
	}//GEN-LAST:event_salirJButtonActionPerformed




	/*******************************************************************/
	/*                         Metodos propios                         */
	/**
	 * ****************************************************************
	 */

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(datosBusquedaJPanel, mensaje);
	}

	/*********************************************************************/

    private ComboBoxEstructuras tipoLicenciaEJCBox;
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
    private javax.swing.JRadioButton todosJRadioButton;
    private javax.swing.JScrollPane ultimoJScrollPane;
    private javax.swing.JTextArea ultimoJTArea;
    private javax.swing.JPanel ultimosEventosJPanel;
    private javax.swing.JScrollPane ultimosJScrollPane;
    private javax.swing.JTable ultimosJTable;
    private javax.swing.JButton irExpedienteJButton;
    // End of variables declaration//GEN-END:variables

	public void renombrarComponentes() {

		try {
			setTitle(CMainOcupaciones.literales.getString("CUltimosEventos.JInternalFrame.title"));
			datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CUltimosEventos.datosBusquedaJPanel.TitleBorder")));
			ultimosEventosJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CUltimosEventos.ultimosEventosJPanel.TitleBorder")));
			pendientesJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CUltimosEventos.pendientesJPanel.TitleBorder")));

			ultimoJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CUltimosEventos.ultimoJScrollPane.TitleBorder")));
			pendienteJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CUltimosEventos.pendienteJScrollPane.TitleBorder")));

			tipoLicenciaJLabel.setText(CMainOcupaciones.literales.getString("CUltimosEventos.tipoLicenciaJLabel.text"));
			numExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CUltimosEventos.numExpedienteJLabel.text"));
			estadoJLabel.setText(CMainOcupaciones.literales.getString("CUltimosEventos.estadoJLabel.text"));
			DNIJLabel.setText(CMainOcupaciones.literales.getString("CUltimosEventos.DNIJLabel.text"));
			fechaJLabel.setText(CMainOcupaciones.literales.getString("CUltimosEventos.fechaJLabel.text"));

			buscarJButton.setText(CMainOcupaciones.literales.getString("CUltimosEventos.buscarJButton.text"));
			salirJButton.setText(CMainOcupaciones.literales.getString("CUltimosEventos.salirJButton.text"));

            borrarFechaHastaJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
            borrarFechaDesdeJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);

            sinRevisarJRadioButton.setText(CMainOcupaciones.literales.getString("Eventos.sinRevisarJRadioButton.text"));
            revisadosJRadioButton.setText(CMainOcupaciones.literales.getString("Eventos.revisadosJRadioButton.text"));
            todosJRadioButton.setText(CMainOcupaciones.literales.getString("Eventos.todosJRadioButton.text"));
            irExpedienteJButton.setText(CMainOcupaciones.literales.getString("CUltimosEventos.irExpedienteJButton.text"));

            /** Headers tabla eventos */
            TableColumn tableColumn= ultimosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column1"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column2"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column3"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column4"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column5"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column6"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column7"));
            tableColumn= ultimosJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.ultimosTableModel.text.column8"));

            tableColumn= pendientesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column1"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column2"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column3"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column4"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column5"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column6"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column7"));
            tableColumn= pendientesJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CUltimosEventos.pendientesTableModel.text.column8"));

            buscarJButton.setToolTipText(CMainOcupaciones.literales.getString("CUltimosEventos.buscarJButton.text"));
            salirJButton.setToolTipText(CMainOcupaciones.literales.getString("CUltimosEventos.salirJButton.text"));
            irExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CUltimosEventos.irExpedienteJButton.text"));

            buscarExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CUltimosEventos.buscarExpedienteJButton.setToolTip.text"));
            DNIJButton.setToolTipText(CMainOcupaciones.literales.getString("CUltimosEventos.DNIJButton.setToolTip.text"));
            fechaDesdeJButton.setToolTipText(CMainOcupaciones.literales.getString("CUltimosEventos.fechaDesdeJButton.setToolTip.text"));
            fechaHastaJButton.setToolTipText(CMainOcupaciones.literales.getString("CUltimosEventos.fechaHastaJButton.setToolTip.text"));
            borrarFechaDesdeJButton.setToolTipText(CMainOcupaciones.literales.getString("CUltimosEventos.borrarFechaDesdeJButton.setToolTip.text"));
            borrarFechaHastaJButton.setToolTipText(CMainOcupaciones.literales.getString("CUltimosEventos.borrarFechaHastaJButton.setToolTip.text"));

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
        tipoLicenciaEJCBox= new ComboBoxEstructuras(Estructuras.getListaTipoOcupacion(), null, CMainOcupaciones.currentLocale.toString(), true);
        /** por defecto selected TODOS */
        tipoLicenciaEJCBox.setSelectedIndex(0);
        datosBusquedaJPanel.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 310, 20));

        estadoExpedienteEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadosOcupacion(), null, CMainOcupaciones.currentLocale.toString(), true);
        /** por defecto selected TODOS */
        estadoExpedienteEJCBox.setSelectedIndex(0);        
        datosBusquedaJPanel.add(estadoExpedienteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 74, 310, 20));
    }

    public void setEnabledIrExpedienteJButton(boolean b){
        irExpedienteJButton.setEnabled(b);
    }




}
