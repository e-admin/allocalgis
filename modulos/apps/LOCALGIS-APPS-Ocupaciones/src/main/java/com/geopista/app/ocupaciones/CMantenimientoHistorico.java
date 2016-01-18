/**
 * CMantenimientoHistorico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * CConsultaEventos.java
 *
 * Created on 16 de abril de 2004, 14:38
 */

package com.geopista.app.ocupaciones;

import java.awt.Cursor;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.ocupaciones.panel.CCalendarJDialog;
import com.geopista.app.ocupaciones.panel.CPersonaJDialog;
import com.geopista.app.ocupaciones.panel.CSearchJDialog;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CHistorico;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CSolicitudLicencia;

/**
 *
 * @author  charo
 */
public class CMantenimientoHistorico extends javax.swing.JInternalFrame implements IMultilingue{

    private Vector _vHistorico= null;
    private Vector _vExpedientes= null;
    private Vector _vSolicitudes= null;
    private Vector aux= new Vector();

    private JFrame desktop;

    /** Modelo para el componente resultadosJTable */
    private DefaultTableModel _resultadosTableModel;
    TableSorted tableSorted= new TableSorted();
    int hiddenColumn= 8;


    /** Necesario para ir a Modificacion de expdiente */
    private CExpedienteLicencia _expedienteSelected= null;

    Logger logger = Logger.getLogger(CMantenimientoHistorico.class);

    /** Creates new form CConsultaEventos */
    public CMantenimientoHistorico(JFrame desktop) {
        this.desktop= desktop;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);


        initComponents();
        initComboBoxesEstructuras();
        String[] columnNames= {CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column1"),
                               CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column2"),
                               CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column3"),
                               CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column4"),
                               CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column5"),
                               CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column6"),
                               //CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column5"),
                               CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column7"),
                               CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column8"),
                               "HIDDEN"};

        CHistoricoTableModel.setColumnNames(columnNames);
        _resultadosTableModel = new CHistoricoTableModel();

        tableSorted= new TableSorted(_resultadosTableModel);
        tableSorted.setTableHeader(resultadosJTable.getTableHeader());
        resultadosJTable.setModel(tableSorted);
        resultadosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultadosJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)resultadosJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mostrarHistoricoSeleccionado();
            }
        });
        ((TableSorted)resultadosJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarHistoricoSeleccionado();
            }
        });
        TableColumn col= resultadosJTable.getColumnModel().getColumn(hiddenColumn);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);


        buscarExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);
        fechaDesdeJButton.setIcon(CUtilidadesComponentes.iconoZoom);
        fechaHastaJButton.setIcon(CUtilidadesComponentes.iconoZoom);
        DNIJButton.setIcon(CUtilidadesComponentes.iconoZoom);



        /** Tamanno de las columnas */
        /*
        TableColumn column = null;
        for (int i = 0; i < _resultadosTableModel.getColumnCount(); i++) {
            column = resultadosJTable.getColumnModel().getColumn(i);
            if ((i == 3) || (i == 4)) {
                column.setPreferredWidth(10);
            }
        }
        */

        fechaDesdeJTField.setEnabled(false);
        fechaHastaJTField.setEnabled(false);

		renombrarComponentes();
  }

    /** This method is called from within the constructor to
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
        usuarioJTField = new javax.swing.JTextField();
        usuarioJLabel = new javax.swing.JLabel();
        estadoJLabel = new javax.swing.JLabel();
        resultadosJScrollPane = new javax.swing.JScrollPane();
        resultadosJTable = new javax.swing.JTable();
        salirJButton = new javax.swing.JButton();
        apunteJScrollPane = new javax.swing.JScrollPane();
        apunteJTArea = new javax.swing.JTextArea();
        borrarFechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();
        irExpedienteJButton = new javax.swing.JButton();
        generarFichaJButton = new javax.swing.JButton();


        setClosable(true);
        setTitle("Consulta de Hist\u00f3rico");
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
        //datosBusquedaJPanel.setToolTipText("");
        numExpedienteJLabel.setText("N\u00famero de Expediente:");
        datosBusquedaJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 52, -1, -1));

        datosBusquedaJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 52, 290, 20));

        fechaJLabel.setText("Fecha (desde/hasta):");
        datosBusquedaJPanel.add(fechaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        datosBusquedaJPanel.add(fechaDesdeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 100, 20));

        datosBusquedaJPanel.add(fechaHastaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 140, 100, 20));

        tipoLicenciaJLabel.setText("Tipo Licencia:");
        datosBusquedaJPanel.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, -1, -1));

        datosBusquedaJPanel.add(DNIJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 96, 290, 20));

        DNIJLabel.setText("DNI/CIF del Propietario:");
        datosBusquedaJPanel.add(DNIJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 96, -1, -1));

        fechaDesdeJButton.setIcon(CUtilidadesComponentes.iconoZoom);
        fechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 140, 20, 20));

        fechaHastaJButton.setIcon(CUtilidadesComponentes.iconoZoom);
        fechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, 20, 20));


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


        DNIJButton.setIcon(CUtilidadesComponentes.iconoZoom);
        DNIJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        DNIJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        DNIJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DNIJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(DNIJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 96, 20, 20));

        buscarExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);
        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 52, 20, 20));

        datosBusquedaJPanel.add(usuarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 118, 310, -1));

        usuarioJLabel.setText("Usuario:");
        datosBusquedaJPanel.add(usuarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 118, 140, -1));

        estadoJLabel.setText("Estado actual de Expediente:");
        datosBusquedaJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 74, -1, -1));

        templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 980, 210));

        resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder("Resultado de la B\u00fasqueda"));
        resultadosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        resultadosJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Licencia", "Num. Expediente"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        resultadosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultadosJTable.setFocusCycleRoot(true);
        resultadosJTable.setSurrendersFocusOnKeystroke(true);
        resultadosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarHistoricoSeleccionado();
            }
        });
        resultadosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mostrarHistoricoSeleccionado();
            }
        });
        resultadosJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mostrarHistoricoSeleccionado();
            }
        });
        resultadosJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mostrarHistoricoSeleccionado();
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mostrarHistoricoSeleccionado();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mostrarHistoricoSeleccionado();
            }
        });



        resultadosJScrollPane.setViewportView(resultadosJTable);

        templateJPanel.add(resultadosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 220, 980, 300));

        salirJButton.setText("Cancelar");
        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirJButtonActionPerformed();
            }
        });

        templateJPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 610, 90, -1));

        irExpedienteJButton.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.irExpedienteJButton.text"));
        irExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irExpedienteJButtonActionPerformed();
            }
        });
        templateJPanel.add(irExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 610, 120, -1));

        generarFichaJButton.setIcon(new javax.swing.ImageIcon(""));
        generarFichaJButton.setToolTipText("");
        //generarFichaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        generarFichaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        generarFichaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarFichaJButtonActionPerformed();
            }
        });

        templateJPanel.add(generarFichaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 610, 110, 25));

        apunteJScrollPane.setBorder(new javax.swing.border.TitledBorder("Apunte del Hist\u00f3rico Seleccionado"));
        apunteJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        apunteJTArea.setEditable(false);
        apunteJTArea.setLineWrap(true);
        apunteJTArea.setRows(3);
        apunteJTArea.setTabSize(4);
        apunteJTArea.setWrapStyleWord(true);
        apunteJTArea.setBorder(null);
        apunteJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        apunteJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        apunteJScrollPane.setViewportView(apunteJTArea);

        templateJPanel.add(apunteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 520, 980, 70));

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void generarFichaJButtonActionPerformed() {//GEN-FIRST:event_generarFichaJButtonActionPerformed
    	this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try
        {
            if ((_vHistorico == null) || (_vExpedientes == null) || (_vSolicitudes == null)) return;
             JFileChooser chooser = new JFileChooser();
            com.geopista.app.utilidades.GeoPistaFileFilter filter= new com.geopista.app.utilidades.GeoPistaFileFilter();
            filter.addExtension("txt");
            filter.setDescription("Fichero TXT");
            chooser.setFileFilter(filter);
            /** Permite multiples selecciones */
            chooser.setMultiSelectionEnabled(false);

            int returnVal = chooser.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                  File selectedFile= chooser.getSelectedFile();
                CUtilidadesComponentes.generarFichaHistoricoMenu(this.desktop, _vHistorico, _vExpedientes, _vSolicitudes, selectedFile,CMainOcupaciones.literales);
            }
        }catch(Exception e)
        {
            logger.error("Error al generar la ficha: ",e);
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_generarFichaJButtonActionPerformed


    private void irExpedienteJButtonActionPerformed(){

        if (_expedienteSelected == null) {
            mostrarMensaje(CMainOcupaciones.literales.getString("CMantenimientoHistorico.mensaje1"));
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
        mainOcupaciones.mostrarJInternalFrame(new com.geopista.app.ocupaciones.CModificacionLicencias((JFrame)this.desktop, _expedienteSelected.getNumExpediente(), false));

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_modificarJButtonActionPerformed


    private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
        CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);        
    }//GEN-LAST:event_formInternalFrameClosed

    private void borrarFechaHastaJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaHastaJButtonActionPerformed
        fechaHastaJTField.setText("");
    }//GEN-LAST:event_borrarFechaHastaJButtonActionPerformed

    private void borrarFechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaDesdeJButtonActionPerformed
        fechaDesdeJTField.setText("");
    }//GEN-LAST:event_borrarFechaDesdeJButtonActionPerformed




    private void mostrarHistoricoSeleccionado(){
        int row= resultadosJTable.getSelectedRow();
        if (row != -1){
            apunteJTArea.setText("");

            CHistorico historico = (CHistorico) _vHistorico.get(((Integer)tableSorted.getValueAt(row, hiddenColumn)).intValue());
            apunteJTArea.setText(historico.getApunte());
            _expedienteSelected= (CExpedienteLicencia)_vExpedientes.get(((Integer)tableSorted.getValueAt(row, hiddenColumn)).intValue());
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
        Hashtable hash=new Hashtable();
        hash.put("E.NUM_EXPEDIENTE",numExpedienteJTField.getText());
        if (tipoLicenciaEJCBox.getSelectedIndex() != 0){
            hash.put("D.TIPO_OCUPACION", tipoLicenciaEJCBox.getSelectedPatron());
        }
        if (estadoEJCBox.getSelectedIndex() != 0){
            hash.put("E.ID_ESTADO", estadoEJCBox.getSelectedPatron());
        }
        hash.put("DNI_CIF",DNIJTField.getText());
        hash.put("H.USUARIO",usuarioJTField.getText());

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

                hash.put("BETWEEN*H.FECHA", fechaDesdeFormatted+"*"+fechaHastaFormatted);
            }
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));


        /** Datos de la solicitud y del expediente seleccionado */
        CResultadoOperacion ro= COperacionesLicencias.getHistorico(hash, null, CMainOcupaciones.literales.getLocale().toString());
        if (ro != null){
            aux= ro.getVector();
            _vHistorico= new Vector();
            _vExpedientes= ro.getExpedientes();
            _vSolicitudes= ro.getSolicitudes();

            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            CUtilidadesComponentes.clearTable(_resultadosTableModel);
            apunteJTArea.setText("");
            /** Redefinimos el tamanno de las celdas fecha */
            TableColumn column = resultadosJTable.getColumnModel().getColumn(6);
            CUtilidades.resizeColumn(column, 70);

            if ((aux == null) || (aux.size() == 0) || (_vExpedientes == null) || (_vExpedientes.size() == 0) || (_vSolicitudes == null) || (_vSolicitudes.size() == 0)) {
                JOptionPane.showMessageDialog(desktop, "Búsqueda realizada sin resultados.");
                return;
            }
            
            for (int i = 0; i < aux.size(); i++) {
                CHistorico historico= (CHistorico)aux.elementAt(i);
                CExpedienteLicencia expediente= (CExpedienteLicencia)_vExpedientes.elementAt(i);
                CSolicitudLicencia solicitud= (CSolicitudLicencia)_vSolicitudes.elementAt(i);

                String descTipoLicencia= ((DomainNode)Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(solicitud.getDatosOcupacion().getTipoOcupacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
                String descEstadoExpediente="";
                try{descEstadoExpediente=((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());}catch(Exception e){logger.error("Error al obtener el estado de la licencia: "+historico.getEstado().getIdEstado(),e);}

                String nombreSolicitante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());
                /** intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                String emplazamiento= CUtilidades.getEmplazamiento(solicitud);

                String apunte= historico.getApunte();
                /** Comprobamos que el historico no sea generico, en cuyo caso, no es necesario annadir
                 * al apunte el literal del estado al que ha cambiado.
                 */                
                if ((historico.getSistema().equalsIgnoreCase("1")) && (historico.getGenerico() == 0)){
                    /** Componemos el apunte, de forma multilingue */
                    apunte+= " " +
                            ((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.literales.getLocale().toString()) + ".";
                    historico.setApunte(apunte);
                }
                _vHistorico.add(i, historico);


                _resultadosTableModel.addRow(new Object[]{descTipoLicencia,
                                                          expediente.getNumExpediente(),
                                                          emplazamiento,
                                                          descEstadoExpediente,
                                                          nombreSolicitante,
                                                          historico.getUsuario(),
                                                          //new Long(historico.getIdHistorico()).toString(),
                                                          CUtilidades.formatFecha(historico.getFechaHistorico()),
                                                          historico.getApunte(),
                                                          new Integer(i)});
            }
       }

    }//GEN-LAST:event_buscarJButtonActionPerformed

    private void salirJButtonActionPerformed() {//GEN-FIRST:event_salirJButtonActionPerformed
            this.dispose();
    }//GEN-LAST:event_salirJButtonActionPerformed




     /*******************************************************************/
     /*                         Metodos propios                         */
    /********************************************************************/

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(datosBusquedaJPanel, mensaje);
    }

    /*********************************************************************/

    private ComboBoxEstructuras tipoLicenciaEJCBox;
    private ComboBoxEstructuras estadoEJCBox;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DNIJButton;
    private javax.swing.JLabel DNIJLabel;
    private javax.swing.JTextField DNIJTField;
    private javax.swing.JScrollPane apunteJScrollPane;
    private javax.swing.JTextArea apunteJTArea;
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
    private javax.swing.JScrollPane resultadosJScrollPane;
    private javax.swing.JTable resultadosJTable;
    private javax.swing.JButton salirJButton;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoLicenciaJLabel;
    private javax.swing.JLabel usuarioJLabel;
    private javax.swing.JTextField usuarioJTField;
    private javax.swing.JButton borrarFechaDesdeJButton;
    private javax.swing.JButton borrarFechaHastaJButton;
    private javax.swing.JButton irExpedienteJButton;
    private javax.swing.JButton generarFichaJButton;

    // End of variables declaration//GEN-END:variables

		public void renombrarComponentes() {

		try {
			setTitle(CMainOcupaciones.literales.getString("CMantenimientoHistorico.JInternalFrame.title"));
			datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CMantenimientoHistorico.datosBusquedaJPanel.TitleBorder")));
			apunteJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CMantenimientoHistorico.apunteJScrollPane.TitleBorder")));
            resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJScrollPane.TitleBorder")));

			tipoLicenciaJLabel.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.tipoLicenciaJLabel.text"));
			numExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.numExpedienteJLabel.text"));
			estadoJLabel.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.estadoJLabel.text"));
			DNIJLabel.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.DNIJLabel.text"));
			usuarioJLabel.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.usuarioJLabel.text"));
			fechaJLabel.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.fechaJLabel.text"));

			buscarJButton.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.buscarJButton.text"));
            irExpedienteJButton.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.irExpedienteJButton.text"));
			salirJButton.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.salirJButton.text"));

            borrarFechaHastaJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
            borrarFechaDesdeJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);

            generarFichaJButton.setText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.generarFichaJButton.text"));
            generarFichaJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.generarFichaHistoricoJButton.setToolTipText.text"));

            /** Headers tabla eventos */
            TableColumn tableColumn= resultadosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column1"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column2"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column3"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column4"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column5"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column6"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column7"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CMantenimientoHistorico.resultadosJTable.text.column8"));

            fechaDesdeJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.fechaDesdeJButton.setToolTip.text"));
            fechaHastaJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.fechaHastaJButton.setToolTip.text"));
            DNIJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.DNIJButton.setToolTip.text"));
            buscarExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.buscarExpedienteJButton.setToolTip.text"));
            borrarFechaDesdeJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.borrarFechaDesdeJButton.setToolTip.text"));
            borrarFechaHastaJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.borrarFechaHastaJButton.setToolTip.text"));

            buscarJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.buscarJButton.text"));
            irExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.irExpedienteJButton.text"));
            salirJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.salirJButton.text"));

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

        estadoEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadosOcupacion(), null, CMainOcupaciones.currentLocale.toString(), true);
        /** por defecto selected TODOS */
        estadoEJCBox.setSelectedIndex(0);        
        datosBusquedaJPanel.add(estadoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 74, 310, 20));
    }

    public void setEnabledIrExpedienteJButton(boolean b){
        irExpedienteJButton.setEnabled(b);
    }
    

}
