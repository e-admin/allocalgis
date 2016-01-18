/*
 * CNotificaciones.java
 *
 * Created on 16 de abril de 2004, 14:38
 */

package com.geopista.app.licencias.notificaciones;


import com.geopista.app.licencias.*;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CNotificacionesTableModel;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.app.licencias.actividad.MainActividad;
import com.geopista.protocol.licencias.*;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.ListaEstructuras;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author charo
 */
public class CNotificaciones extends javax.swing.JInternalFrame implements IMultilingue {

	private Vector _vNotificaciones = new Vector();
    private ResourceBundle literales;
	/**
	 * Modelo para el componente resultadosJTable
	 */
	private DefaultTableModel _resultadosTableModel;
    TableSorted resultadosJTModelSorted= new TableSorted();
    int hiddenColumn= 12;


	Logger logger = Logger.getLogger(CNotificaciones.class);

	/**
	 * Creates new form CNotificaciones
	 */
	public CNotificaciones(JFrame desktop, ResourceBundle literales) {
		this.desktop = desktop;
        this.literales= literales;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

		initComponents();
        initComboBoxesEstructuras();
        try
        {
            /** Debido a que las filas de la tabla se pueden ordenar por cualquiera de las columnas en
             * modo ascendente o descendente, el valor rowSelected ya no nos sirve para acceder al
             * vector de notificaciones y recoger la notificacion seleccionada.
             * Es necesario por tanto almacenar las posiciones que en el vector ocupan las notificaciones en la tabla. Para ello,
             * creamos una columna hidden con los valores de la posicion de la notificacion en el vector.
             */
            String[] columnNames= {literales.getString("CNotificaciones.resultadosJTable.text.column11"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column1"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column2"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column3"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column4"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column12"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column5"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column6"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column7"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column8"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column9"),
                               literales.getString("CNotificaciones.resultadosJTable.text.column10"),
                               "HIDDEN" };
                CNotificacionesTableModel.setColumnNames(columnNames);

        }catch (Exception e){
            logger.error("Error al cargar las cabeceras: ",e);
        }
        _resultadosTableModel = new CNotificacionesTableModel();
        resultadosJTModelSorted= new TableSorted(_resultadosTableModel);
        resultadosJTModelSorted.setTableHeader(resultadosJTable.getTableHeader());
        resultadosJTable.setModel(resultadosJTModelSorted);
        resultadosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultadosJTable.getTableHeader().setReorderingAllowed(false);

        ((TableSorted)resultadosJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                resultadosJTableFocusGained();
            }
        });
        ((TableSorted)resultadosJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resultadosJTableMouseClicked();
            }
        });



        /** Tamanno de las columnas */
        /*
        for (int i= 0; i < _resultadosTableModel.getColumnCount(); i++) {
            TableColumn column= resultadosJTable.getColumnModel().getColumn(i);
            if ((i == 7) || (i == 8) || (i == 9)) {
                column.setPreferredWidth(10);
            }
        }
        */
        TableColumn col= resultadosJTable.getColumnModel().getColumn(hiddenColumn);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);

		buscarExpedienteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		fechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		fechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        borrarFechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
        borrarFechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);

        fechaDesdeJTField.setEnabled(false);
        fechaHastaJTField.setEnabled(false);

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
        fechaVencimientoJLabel = new javax.swing.JLabel();
        fechaDesdeJTField = new javax.swing.JTextField();
        fechaHastaJTField = new javax.swing.JTextField();
        tipoLicenciaJLabel = new javax.swing.JLabel();
        buscarJButton = new javax.swing.JButton();
        tipoNotificacionJLabel = new javax.swing.JLabel();
        enEstadoNotificacionJLabel = new javax.swing.JLabel();
        fechaDesdeJButton = new javax.swing.JButton();
        fechaHastaJButton = new javax.swing.JButton();
        buscarExpedienteJButton = new javax.swing.JButton();
        estadoExpedienteJLabel = new javax.swing.JLabel();
        borrarFechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();
        tipoObraJLabel = new javax.swing.JLabel();
        resultadosJScrollPane = new javax.swing.JScrollPane();
        resultadosJTable = new javax.swing.JTable();
        salirJButton = new javax.swing.JButton();
        datosNotificacionJPanel = new javax.swing.JPanel();
        datosNombreApellidosJLabel = new javax.swing.JLabel();
        datosDireccionJLabel = new javax.swing.JLabel();
        datosCPostalJLabel = new javax.swing.JLabel();
        datosNotificarPorJLabel = new javax.swing.JLabel();
        datosNombreApellidosJTField = new javax.swing.JTextField();
        datosCPostalJTField = new javax.swing.JTextField();
        datosDireccionJTField = new javax.swing.JTextField();
        datosNotificarPorJTField = new javax.swing.JTextField();
        entregadaAJLabel = new javax.swing.JLabel();
        entregadaAJTField = new javax.swing.JTextField();
        irExpedienteJButton = new javax.swing.JButton();

        setClosable(true);
        setTitle("Consulta de Notificaciones");
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
        datosBusquedaJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 220, 20));

        datosBusquedaJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 72, 310, 20));

        fechaVencimientoJLabel.setText("Fecha Vencimiento (desde/hasta):");
        datosBusquedaJPanel.add(fechaVencimientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 156, 220, 20));

        datosBusquedaJPanel.add(fechaDesdeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 156, 100, 20));

        datosBusquedaJPanel.add(fechaHastaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 156, 100, 20));

        tipoLicenciaJLabel.setText("Tipo Licencia:");
        datosBusquedaJPanel.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 220, 20));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 170, -1, -1));

        tipoNotificacionJLabel.setText("Tipo de Notificaci\u00f3n:");
        datosBusquedaJPanel.add(tipoNotificacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 220, 20));

        enEstadoNotificacionJLabel.setText("Estado de la Notificaci\u00f3n:");
        datosBusquedaJPanel.add(enEstadoNotificacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 220, 20));

        fechaDesdeJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaDesdeJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 156, 20, 20));

        fechaHastaJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaHastaJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 156, 20, 20));

        buscarExpedienteJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarExpedienteJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 72, 20, 20));

        estadoExpedienteJLabel.setText("Estado actual de Expediente:");
        datosBusquedaJPanel.add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 220, 20));

        borrarFechaDesdeJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarFechaDesdeJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 156, 20, 20));

        borrarFechaHastaJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarFechaHastaJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 156, 20, 20));

        tipoObraJLabel.setText("Tipo Obra:");
        datosBusquedaJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 220, 20));

        templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 980, 210));

        resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder("Resultado de la B\u00fasqueda"));
        resultadosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        resultadosJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Licencia", "Num. Expediente", "Fecha Vencimiento", "En Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        resultadosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultadosJTable.setFocusCycleRoot(true);
        resultadosJTable.setSurrendersFocusOnKeystroke(true);
        resultadosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                resultadosJTableFocusGained();
            }
        });
        resultadosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resultadosJTableMouseClicked();
            }
        });
        resultadosJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                resultadosJTableMouseDragged();
            }
        });
        resultadosJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                resultadosJTableKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                resultadosJTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                resultadosJTableKeyReleased(evt);
            }
        });


        resultadosJScrollPane.setViewportView(resultadosJTable);

        templateJPanel.add(resultadosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 220, 980, 210));

        salirJButton.setText("Cancelar");
        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirJButtonActionPerformed();
            }
        });

        templateJPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 600, 90, -1));

        datosNotificacionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder("Datos de Notificaci\u00f3n de la Notificaci\u00f3n Seleccionada"));
        datosNombreApellidosJLabel.setText("Nombre y Apellidos:");
        datosNotificacionJPanel.add(datosNombreApellidosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 230, 20));

        datosDireccionJLabel.setText("Direcci\u00f3n:");
        datosNotificacionJPanel.add(datosDireccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 230, 20));

        datosCPostalJLabel.setText("C.Postal/Municipio/Provincia:");
        datosNotificacionJPanel.add(datosCPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 230, 20));

        datosNotificarPorJLabel.setText("Notificar Por:");
        datosNotificacionJPanel.add(datosNotificarPorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 230, 20));

        datosNombreApellidosJTField.setEditable(false);
        datosNombreApellidosJTField.setBorder(null);
        datosNotificacionJPanel.add(datosNombreApellidosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, 700, 20));

        datosCPostalJTField.setEditable(false);
        datosCPostalJTField.setBorder(null);
        datosNotificacionJPanel.add(datosCPostalJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 700, 20));

        datosDireccionJTField.setEditable(false);
        datosDireccionJTField.setBorder(null);
        datosNotificacionJPanel.add(datosDireccionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 700, 20));

        datosNotificarPorJTField.setEditable(false);
        datosNotificarPorJTField.setBorder(null);
        datosNotificacionJPanel.add(datosNotificarPorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 700, 20));

        entregadaAJLabel.setText("Entregada A:");
        datosNotificacionJPanel.add(entregadaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 230, 20));

        entregadaAJTField.setEditable(false);
        entregadaAJTField.setBorder(null);
        datosNotificacionJPanel.add(entregadaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, 700, 20));

        templateJPanel.add(datosNotificacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 430, 980, 160));

        irExpedienteJButton.setText("Ir Expediente");
        irExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irExpedienteJButtonActionPerformed();
            }
        });

        templateJPanel.add(irExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 600, 130, -1));

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void resultadosJTableMouseDragged() {//GEN-FIRST:event_resultadosJTableMouseDragged
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_resultadosJTableMouseDragged

    private void resultadosJTableFocusGained() {//GEN-FIRST:event_resultadosJTableFocusGained
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void resultadosJTableKeyTyped(java.awt.event.KeyEvent evt) {
        mostrarNotificacionSeleccionada();
    }
    private void resultadosJTableKeyPressed(java.awt.event.KeyEvent evt){
        mostrarNotificacionSeleccionada();
    }
    private void resultadosJTableKeyReleased(java.awt.event.KeyEvent evt){
        mostrarNotificacionSeleccionada();
    }


    private void irExpedienteJButtonActionPerformed() {//GEN-FIRST:event_irExpedienteJButtonActionPerformed
        int row= resultadosJTable.getSelectedRow();
        if (row == -1){
            mostrarMensaje(literales.getString("CNotificaciones.mensaje4"));
            return;
        }

        CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(((Integer)resultadosJTModelSorted.getValueAt(row, hiddenColumn)).intValue());
        CExpedienteLicencia expediente= notificacion.getExpediente();
        CSolicitudLicencia solicitud= notificacion.getSolicitud();
        if ((notificacion == null) || (expediente == null) || (solicitud == null)) return;

        CConstantesLicencias.persona= null;
        CConstantesLicencias.representante= null;
        CConstantesLicencias.tecnico= null;
        CConstantesLicencias.promotor= null;

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
                com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias modificacionLicencias =
                    new com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias(desktop, expediente.getNumExpediente(),literales, false);
                mainActividad.mostrarJInternalFrame(modificacionLicencias);
         }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_irExpedienteJButtonActionPerformed

    private void borrarFechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaDesdeJButtonActionPerformed
        fechaDesdeJTField.setText("");
    }//GEN-LAST:event_borrarFechaDesdeJButtonActionPerformed

    private void borrarFechaHastaJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaHastaJButtonActionPerformed
        fechaHastaJTField.setText("");
    }//GEN-LAST:event_borrarFechaHastaJButtonActionPerformed

    private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
        CConstantesLicencias.helpSetHomeID = "geopistaIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);        
    }//GEN-LAST:event_formInternalFrameClosed

	private void resultadosJTableMouseClicked() {//GEN-FIRST:event_resultadosJTableMouseClicked
	    mostrarNotificacionSeleccionada();
	}//GEN-LAST:event_resultadosJTableMouseClicked

	private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed
		CUtilidadesComponentes.showSearchDialog(desktop,  tipoLicenciaEJCBox.getSelectedPatron(), tipoObraEJCBox.getSelectedPatron(), estadoExpedienteEJCBox.getSelectedPatron(), null, literales);
		numExpedienteJTField.setText(CConstantesLicencias.searchValue);
        if ((CConstantesLicencias.patronSelectedEstado != null) &&(CConstantesLicencias.patronSelectedTipoLicencia != null) && (CConstantesLicencias.patronSelectedTipoObra != null)){
            estadoExpedienteEJCBox.setSelectedPatron(CConstantesLicencias.patronSelectedEstado);
            tipoLicenciaEJCBox.setSelectedPatron(CConstantesLicencias.patronSelectedTipoLicencia);
            tipoObraEJCBox.setSelectedPatron(CConstantesLicencias.patronSelectedTipoObra);
        }


	}//GEN-LAST:event_buscarExpedienteJButtonActionPerformed

	private void fechaHastaJButtonActionPerformed() {//GEN-FIRST:event_fechaHastaJButtonActionPerformed
		CUtilidadesComponentes.showCalendarDialog(desktop);

		if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
			fechaHastaJTField.setText(CConstantesLicencias.calendarValue);
		}

	}//GEN-LAST:event_fechaHastaJButtonActionPerformed

	private void fechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_fechaDesdeJButtonActionPerformed
		CUtilidadesComponentes.showCalendarDialog(desktop);

		if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
			fechaDesdeJTField.setText(CConstantesLicencias.calendarValue);
		}

	}//GEN-LAST:event_fechaDesdeJButtonActionPerformed

	private void salirJButtonActionPerformed() {//GEN-FIRST:event_salirJButtonActionPerformed
	    CConstantesLicencias.helpSetHomeID= "licenciasIntro";
		this.dispose();
	}//GEN-LAST:event_salirJButtonActionPerformed

	private void buscarJButtonActionPerformed() {//GEN-FIRST:event_buscarJButtonActionPerformed

		Hashtable hash = new Hashtable();
		hash.put("E.NUM_EXPEDIENTE", numExpedienteJTField.getText());

        if (tipoLicenciaEJCBox.getSelectedIndex()!=0) {
					hash.put("S.ID_TIPO_LICENCIA", tipoLicenciaEJCBox.getSelectedPatron());
		    }
        // El patron -1 corresponde a la opcion por defecto (CUALQUIERA)
		if (tipoObraEJCBox.getSelectedIndex()!=0) {
			hash.put("S.ID_TIPO_OBRA", tipoObraEJCBox.getSelectedPatron());
		}
		if (estadoNotificacionEJCBox.getSelectedIndex()!=0) {
			hash.put("A.ID_ESTADO", estadoNotificacionEJCBox.getSelectedPatron());
		}
		if (estadoExpedienteEJCBox.getSelectedIndex()!=0) {
			hash.put("E.ID_ESTADO", estadoExpedienteEJCBox.getSelectedPatron());
		}
		if (tipoNotificacionEJCBox.getSelectedIndex()!=0) {
			hash.put("A.ID_TIPO_NOTIFICACION", tipoNotificacionEJCBox.getSelectedPatron());
		}
		/** comprobamos que la fecha tiene formato valido */
		if ((CUtilidades.parseFechaStringToString(fechaDesdeJTField.getText()) == null) || (CUtilidades.parseFechaStringToString(fechaHastaJTField.getText()) == null)) {
			mostrarMensaje(literales.getString("CNotificaciones.mensaje3"));
			return;
		} else {
			//Between entre fechas
			Date fechaDesde = CUtilidades.parseFechaStringToDate(fechaDesdeJTField.getText().trim());
			if (fechaDesdeJTField.getText().trim().equals("")) {
				fechaDesde = new Date(1);
			}
			Date fechaHasta = CUtilidades.parseFechaStringToDate(fechaHastaJTField.getText().trim());
			if (fechaHastaJTField.getText().trim().equals("")) {
                try{fechaHasta =  new SimpleDateFormat("dd/mm/yyyy").parse("1/1/3000");}catch (Exception e){};
			}

			if ((fechaDesde != null) && (fechaHasta != null)) {
                /** comprobamos que fechaDesde sea menor que fechaHasta */
                long millisDesde= fechaDesde.getTime();
                long millisHasta= fechaHasta.getTime();
                if (millisDesde > millisHasta){
                    mostrarMensaje(literales.getString("CNotificaciones.mensaje2"));
                    return;
                }

				String fechaDesdeFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaDesde);
				long millis = fechaHasta.getTime();
				/** annadimos un dia */
				millis += 24 * 60 * 60 * 1000;
				fechaHasta = new Date(millis);
				String fechaHastaFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaHasta);

				hash.put("BETWEEN*A.PLAZO_VENCIMIENTO", fechaDesdeFormatted + "*" + fechaHastaFormatted);
			}
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		/** Datos de la solicitud y del expediente seleccionado */
		CUtilidadesComponentes.clearTable(_resultadosTableModel);
		datosCPostalJTField.setText("");
		datosDireccionJTField.setText("");
		datosNombreApellidosJTField.setText("");
		datosNotificarPorJTField.setText("");

		_vNotificaciones = COperacionesLicencias.getNotificacionesMenu(hash, ((IMainLicencias)desktop).getTiposLicencia());
		if ((_vNotificaciones != null) && (_vNotificaciones.size() > 0)) {

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			CUtilidadesComponentes.clearTable(_resultadosTableModel);

            for (int i= 0; i < _vNotificaciones.size(); i++){
                CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(i);
                try{
                    String nombreSolicititante= CUtilidades.componerCampo(notificacion.getPersona().getApellido1(), notificacion.getPersona().getApellido2(), notificacion.getPersona().getNombre());

                    /** tipo licencia */
                    if (notificacion.getSolicitud().getTipoLicencia() != null){
                        int tipoLicencia= notificacion.getSolicitud().getTipoLicencia().getIdTipolicencia();
                        String sTipoObra= "";
                        String sTipoLicencia= "";
                        if (tipoLicencia == CConstantesLicencias.ObraMayor){
                             sTipoObra= ((DomainNode)Estructuras.getListaTiposObra().getDomainNode(new Integer(notificacion.getSolicitud().getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                             sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(notificacion.getSolicitud().getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        }else if(tipoLicencia == CConstantesLicencias.ObraMenor){
                             sTipoObra= ((DomainNode)Estructuras.getListaTiposObraMenor().getDomainNode(new Integer(notificacion.getSolicitud().getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                             sTipoLicencia= sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(notificacion.getSolicitud().getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        }else if(tipoLicencia == CConstantesLicencias.Actividades || tipoLicencia == CConstantesLicencias.ActividadesNoCalificadas){
                             sTipoObra= ((DomainNode)Estructuras.getListaTiposActividad().getDomainNode(new Integer(notificacion.getSolicitud().getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                             sTipoLicencia=((DomainNode)Estructuras.getListaTiposLicenciaActividad().getDomainNode(new Integer(notificacion.getSolicitud().getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        }

                        /** Intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                        /** Ahora tipo de Vía y nombre del emplazamiento son obligatorios, por lo
                         * que sacamos el emplazamiento de ahí y no de la lista de referencias catastrales. */
                        /*
                        String emplazamiento = "";
                        Vector vRef= notificacion.getSolicitud().getReferenciasCatastrales();
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
                        String emplazamiento= CUtilidades.componerCampo(notificacion.getSolicitud().getTipoViaAfecta(), notificacion.getSolicitud().getNombreViaAfecta(), notificacion.getSolicitud().getNumeroViaAfecta());
                        String entregadaA= "";
                        if (notificacion.getEntregadaA() != null)
                            entregadaA= notificacion.getEntregadaA();

                        Object[] rowData = {notificacion.getExpediente().getNumExpediente(),
                                            sTipoLicencia,
                                            sTipoObra,
                                            emplazamiento,
                                            ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(notificacion.getExpediente().getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                                            ((DomainNode)Estructuras.getListaTiposNotificacion().getDomainNode(new Integer(notificacion.getTipoNotificacion().getIdTipoNotificacion()).toString())).getTerm(literales.getLocale().toString()),
                                            ((DomainNode)Estructuras.getListaEstadosNotificacion().getDomainNode(new Integer(notificacion.getEstadoNotificacion().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                                            nombreSolicititante,
                                            entregadaA,
                                            CUtilidades.formatFecha(notificacion.getPlazoVencimiento()),
                                            CUtilidades.formatFecha(notificacion.getFechaNotificacion()),
                                            CUtilidades.formatFecha(notificacion.getFecha_reenvio()),
                                            new Integer(i)};

                        _resultadosTableModel.addRow(rowData);

                    }
                }catch (Exception e){
                    logger.error("Error al cargar la fila "+e);
                }
			}

		} else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			JOptionPane.showMessageDialog(desktop, literales.getString("CNotificaciones.mensaje1"));
			CUtilidadesComponentes.clearTable(_resultadosTableModel);
			return;
		}


	}//GEN-LAST:event_buscarJButtonActionPerformed

    public void mostrarNotificacionSeleccionada(){
        int row = resultadosJTable.getSelectedRow();
        if (row != -1) {

            CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(((Integer)resultadosJTModelSorted.getValueAt(row, hiddenColumn)).intValue());
            numExpedienteJTField.setText(notificacion.getExpediente().getNumExpediente());

            CPersonaJuridicoFisica persona = notificacion.getPersona();
            CDatosNotificacion datos = persona.getDatosNotificacion();
            String interesado= CUtilidades.componerCampo(persona.getApellido1(), persona.getApellido2(), persona.getNombre());
            datosNombreApellidosJTField.setText(interesado);
            datosDireccionJTField.setText(CUtilidades.componerCampo(((DomainNode)Estructuras.getListaTiposViaINE().getDomainNode(datos.getTipoVia())).getTerm(literales.getLocale().toString()),
                    datos.getNombreVia(),
                    datos.getNumeroVia()));
            datosCPostalJTField.setText(CUtilidades.componerCampo(datos.getCpostal(), datos.getMunicipio(), datos.getProvincia()));
            datosNotificarPorJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(datos.getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(literales.getLocale().toString()));
            entregadaAJTField.setText(notificacion.getEntregadaA());
        }else{
            clear();
        }
    }

    public void clear(){
        numExpedienteJTField.setText("");
        datosNombreApellidosJTField.setText("");
        datosDireccionJTField.setText("");
        datosCPostalJTField.setText("");
        datosNotificarPorJTField.setText("");
        entregadaAJTField.setText("");
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

	

	private JFrame desktop;

    private ComboBoxEstructuras tipoLicenciaEJCBox;
    private ComboBoxEstructuras estadoExpedienteEJCBox;
    private ComboBoxEstructuras tipoNotificacionEJCBox;
    private ComboBoxEstructuras estadoNotificacionEJCBox;
    private ComboBoxEstructuras tipoObraEJCBox;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton borrarFechaDesdeJButton;
    private javax.swing.JButton borrarFechaHastaJButton;
    private javax.swing.JButton buscarExpedienteJButton;
    private javax.swing.JButton buscarJButton;
    private javax.swing.JPanel datosBusquedaJPanel;
    private javax.swing.JLabel datosCPostalJLabel;
    private javax.swing.JTextField datosCPostalJTField;
    private javax.swing.JLabel datosDireccionJLabel;
    private javax.swing.JTextField datosDireccionJTField;
    private javax.swing.JLabel datosNombreApellidosJLabel;
    private javax.swing.JTextField datosNombreApellidosJTField;
    private javax.swing.JPanel datosNotificacionJPanel;
    private javax.swing.JLabel datosNotificarPorJLabel;
    private javax.swing.JTextField datosNotificarPorJTField;
    private javax.swing.JLabel enEstadoNotificacionJLabel;
    private javax.swing.JLabel entregadaAJLabel;
    private javax.swing.JTextField entregadaAJTField;
    private javax.swing.JLabel estadoExpedienteJLabel;
    private javax.swing.JButton fechaDesdeJButton;
    private javax.swing.JTextField fechaDesdeJTField;
    private javax.swing.JButton fechaHastaJButton;
    private javax.swing.JTextField fechaHastaJTField;
    private javax.swing.JLabel fechaVencimientoJLabel;
    private javax.swing.JButton irExpedienteJButton;
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JScrollPane resultadosJScrollPane;
    private javax.swing.JTable resultadosJTable;
    private javax.swing.JButton salirJButton;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoLicenciaJLabel;
    private javax.swing.JLabel tipoNotificacionJLabel;
    private javax.swing.JLabel tipoObraJLabel;
    //private javax.swing.JTextField jTextFieldTipoLicencia;

    // End of variables declaration//GEN-END:variables


	public void renombrarComponentes(ResourceBundle literales) {

        this.literales=literales;
		try {
			setTitle(literales.getString("CNotificaciones.JInternalFrame.title"));
			datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CNotificaciones.datosBusquedaJPanel.TitleBorder")));
			datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CNotificaciones.datosNotificacionJPanel.TitleBorder")));
			resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CNotificaciones.resultadosJScrollPane.TitleBorder")));

			tipoLicenciaJLabel.setText(literales.getString("CNotificaciones.tipoLicenciaJLabel.text"));
            tipoObraJLabel.setText(literales.getString("CNotificaciones.tipoObraJLabel.text"));
			numExpedienteJLabel.setText(literales.getString("CNotificaciones.numExpedienteJLabel.text"));
			estadoExpedienteJLabel.setText(literales.getString("CNotificaciones.estadoExpedienteJLabel.text"));
			tipoNotificacionJLabel.setText(literales.getString("CNotificaciones.tipoNotificacionJLabel.text"));
			enEstadoNotificacionJLabel.setText(literales.getString("CNotificaciones.enEstadoNotificacionJLabel.text"));
			fechaVencimientoJLabel.setText(literales.getString("CNotificaciones.fechaVencimientoJLabel.text"));


			datosNombreApellidosJLabel.setText(literales.getString("CNotificaciones.datosNombreApellidosJLabel.text"));
			datosDireccionJLabel.setText(literales.getString("CNotificaciones.datosDireccionJLabel.text"));
			datosCPostalJLabel.setText(literales.getString("CNotificaciones.datosCPostalJLabel.text"));
			datosNotificarPorJLabel.setText(literales.getString("CNotificaciones.datosNotificarPorJLabel.text"));
            entregadaAJLabel.setText(literales.getString("CNotificaciones.entregadaAJLabel.text"));

			buscarJButton.setText(literales.getString("CNotificaciones.buscarJButton.text"));
			salirJButton.setText(literales.getString("CNotificaciones.salirJButton.text"));
            irExpedienteJButton.setText(literales.getString("CNotificaciones.irExpedienteJButton.text"));

            buscarJButton.setToolTipText(literales.getString("CNotificaciones.buscarJButton.text"));
            salirJButton.setToolTipText(literales.getString("CNotificaciones.salirJButton.text"));
            irExpedienteJButton.setToolTipText(literales.getString("CNotificaciones.irExpedienteJButton.text"));

            /** Headers tabla notificaciones */
            TableColumn tableColumn= resultadosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column11"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column1"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column2"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column3"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column4"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column12"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column5"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column6"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column7"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(9);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column8"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(10);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column9"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(11);
            tableColumn.setHeaderValue(literales.getString("CNotificaciones.resultadosJTable.text.column10"));

            fechaDesdeJButton.setToolTipText(literales.getString("CNotificaciones.fechaDesdeJButton.setToolTip.text"));
            fechaHastaJButton.setToolTipText(literales.getString("CNotificaciones.fechaHastaJButton.setToolTip.text"));
            buscarExpedienteJButton.setToolTipText(literales.getString("CNotificaciones.buscarExpedienteJButton.setToolTip.text"));
            borrarFechaDesdeJButton.setToolTipText(literales.getString("CNotificaciones.borrarFechaDesdeJButton.setToolTip.text"));
            borrarFechaHastaJButton.setToolTipText(literales.getString("CNotificaciones.borrarFechaHastaJButton.setToolTip.text"));
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
            datosBusquedaJPanel.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 330, 20));
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
            datosBusquedaJPanel.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 330, 20));
            tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposActividad(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setEnabled(true);

        }

        tipoObraEJCBox.setSelectedIndex(0);
        datosBusquedaJPanel.add(tipoObraEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 51, 330, 20));

        estadoExpedienteEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstados(), null, literales.getLocale().toString(), true);
        estadoExpedienteEJCBox.setSelectedIndex(0);

        datosBusquedaJPanel.add(estadoExpedienteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 93, 330, 20));

        tipoNotificacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposNotificacion(), null, literales.getLocale().toString(), true);
        tipoNotificacionEJCBox.setSelectedIndex(0);
        datosBusquedaJPanel.add(tipoNotificacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 114, 330, 20));

        estadoNotificacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadosNotificacion(), null, literales.getLocale().toString(), true);
        estadoNotificacionEJCBox.setSelectedIndex(0);
        datosBusquedaJPanel.add(estadoNotificacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 135, 330, 20));

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
