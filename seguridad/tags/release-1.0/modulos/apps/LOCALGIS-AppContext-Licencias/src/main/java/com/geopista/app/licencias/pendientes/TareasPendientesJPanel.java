/*
 * TareasPendientesJPanel.java
 *
 * Created on 16 de noviembre de 2004, 12:34
 */

package com.geopista.app.licencias.pendientes;

import com.geopista.protocol.licencias.*;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.app.licencias.*;
import com.geopista.app.licencias.utilidades.CheckBoxTableEditor;
import com.geopista.app.licencias.utilidades.CheckBoxRenderer;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CEventosTableModel;
import com.geopista.app.licencias.tableModels.CNotificacionesTableModel;
import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.app.licencias.actividad.MainActividad;
import com.geopista.app.utilidades.TableSorted;


import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.awt.*;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 *
 * @author  charo
 */
public class TareasPendientesJPanel extends javax.swing.JPanel implements IMultilingue {

    private Vector _vEventos= null;
    private Vector _vSolicitudesEv= null;

    private Vector _vNotificaciones = null;

    private CExpedienteLicencia _expedienteSelected= null;
    private CSolicitudLicencia _solicitudSelected= null;

    private JFrame desktop;
    private JInternalFrame desktopInternal= null;
    private ResourceBundle literales;
    /**
     * Modelo para el componente resultadosJTable
     */
    private DefaultTableModel _eventosTableModel;
    private DefaultTableModel _notificacionesTableModel;

    TableSorted notificacionesSorted= new TableSorted();
    TableSorted eventosSorted= new TableSorted();
    int notificacionesHiddenCol= 8;
    int eventosHiddenCol= 9;


    Logger logger = Logger.getLogger(TareasPendientesJPanel.class);


    public TareasPendientesJPanel(JInternalFrame desktop, JFrame main, ResourceBundle literales) {
        this.desktopInternal= desktop;
        this.literales= literales;
        this.desktop= main;
        initComponents();
        init();
    }

    public void init(){

        initEstructuras();
        try
        {
            String[] columnNamesEventos= {literales.getString("CTareasPendientes.eventosTableModel.text.column1"),
                               literales.getString("CTareasPendientes.eventosTableModel.text.column2"),
                               literales.getString("CTareasPendientes.eventosTableModel.text.column3"),
                               literales.getString("CTareasPendientes.eventosTableModel.text.column4"),
                               literales.getString("CTareasPendientes.eventosTableModel.text.column5"),
                               literales.getString("CTareasPendientes.eventosTableModel.text.column6"),
                               literales.getString("CTareasPendientes.eventosTableModel.text.column7"),
                               literales.getString("CTareasPendientes.eventosTableModel.text.column8"),
                               literales.getString("CTareasPendientes.eventosTableModel.text.column9"),
                               "HIDDEN"};

            CEventosTableModel.setColumnNames(columnNamesEventos);
        }catch(Exception e)
        {
            logger.error("Error al poner el titulo de las tablas.",e);
        }
        _eventosTableModel = new CEventosTableModel();

        String[] columnNamesNotificaciones= {literales.getString("CTareasPendientes.notificacionesTableModel.text.column1"),
                               literales.getString("CTareasPendientes.notificacionesTableModel.text.column2"),
                               literales.getString("CTareasPendientes.notificacionesTableModel.text.column9"),
                               literales.getString("CTareasPendientes.notificacionesTableModel.text.column3"),
                               literales.getString("CTareasPendientes.notificacionesTableModel.text.column4"),
                               literales.getString("CTareasPendientes.notificacionesTableModel.text.column5"),
                               literales.getString("CTareasPendientes.notificacionesTableModel.text.column6"),
                               //literales.getString("CTareasPendientes.notificacionesTableModel.text.column7"),
                               literales.getString("CTareasPendientes.notificacionesTableModel.text.column8"),
                               "HIDDEN"};

        CNotificacionesTableModel.setColumnNames(columnNamesNotificaciones);
        _notificacionesTableModel = new CNotificacionesTableModel();

        eventosSorted= new TableSorted(_eventosTableModel);
        eventosSorted.setTableHeader(eventosJTable.getTableHeader());
		eventosJTable.setModel(eventosSorted);
        eventosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventosJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)eventosJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eventosJTableFocusGained();
            }
        });
        ((TableSorted)eventosJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eventosJTableMouseClicked();
            }
        });
        TableColumn col= eventosJTable.getColumnModel().getColumn(eventosHiddenCol);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);



        notificacionesSorted= new TableSorted(_notificacionesTableModel);
        notificacionesSorted.setTableHeader(notificacionesJTable.getTableHeader());
		notificacionesJTable.setModel(notificacionesSorted);
        notificacionesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificacionesJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)notificacionesJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notificacionesJTableFocusGained();
            }
        });
        ((TableSorted)notificacionesJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                notificacionesJTableMouseClicked();
            }
        });
        col= notificacionesJTable.getColumnModel().getColumn(notificacionesHiddenCol);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);

		renombrarComponentes(literales);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        eventosJPanel = new javax.swing.JPanel();
        eventosJScrollPane = new javax.swing.JScrollPane();
        eventosJTable = new javax.swing.JTable();
        eventoJScrollPane = new javax.swing.JScrollPane();
        eventoJTArea = new javax.swing.JTextArea();
        notificacionesJPanel = new javax.swing.JPanel();
        notificacionesJScrollPane = new javax.swing.JScrollPane();
        notificacionesJTable = new javax.swing.JTable();
        datosNotificacionJPanel = new javax.swing.JPanel();
        datosNombreApellidosJLabel = new javax.swing.JLabel();
        datosDireccionJLabel = new javax.swing.JLabel();
        datosCPostalJLabel = new javax.swing.JLabel();
        datosNotificarPorJLabel = new javax.swing.JLabel();
        datosNombreApellidosJTField = new javax.swing.JTextField();
        datosDireccionJTField = new javax.swing.JTextField();
        datosNotificarPorJTField = new javax.swing.JTextField();
        datosCPostalJTField = new javax.swing.JTextField();
        entregadaAJTField = new javax.swing.JTextField();
        entregadaAJLabel = new javax.swing.JLabel();
        aceptarJButton = new javax.swing.JButton();
        salirJButton = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        eventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        eventosJPanel.setBorder(new javax.swing.border.TitledBorder("Eventos Pendientes"));
        eventosJScrollPane.setBorder(new javax.swing.border.EtchedBorder());
        eventosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        eventosJTable.setModel(new javax.swing.table.DefaultTableModel(
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
        eventosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        eventosJTable.setFocusCycleRoot(true);
        eventosJTable.setSurrendersFocusOnKeystroke(true);
        eventosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained() {
                eventosJTableFocusGained();
            }
        });
        eventosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eventosJTableMouseClicked();
            }
        });
        eventosJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                eventosJTableMouseDragged();
            }
        });
        eventosJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                eventosJTableKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                eventosJTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                eventosJTableKeyReleased(evt);
            }
        });


        eventosJScrollPane.setViewportView(eventosJTable);

        eventosJPanel.add(eventosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 990, 160));

        eventoJScrollPane.setBorder(new javax.swing.border.TitledBorder("Descripci\u00f3n del Evento Seleccionado"));
        eventoJTArea.setEditable(false);
        eventoJTArea.setLineWrap(true);
        eventoJTArea.setWrapStyleWord(true);
        eventoJScrollPane.setViewportView(eventoJTArea);

        eventosJPanel.add(eventoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 990, 100));

        add(eventosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 300));

        notificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        notificacionesJPanel.setBorder(new javax.swing.border.TitledBorder("Notificaciones Pendientes"));
        notificacionesJScrollPane.setBorder(new javax.swing.border.EtchedBorder());
        notificacionesJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        notificacionesJTable.setModel(new javax.swing.table.DefaultTableModel(
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
        notificacionesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        notificacionesJTable.setFocusCycleRoot(true);
        notificacionesJTable.setSurrendersFocusOnKeystroke(true);
        notificacionesJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notificacionesJTableFocusGained();
            }
        });
        notificacionesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                notificacionesJTableMouseClicked();
            }
        });
        notificacionesJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                notificacionesJTableMouseDragged();
            }
        });
        notificacionesJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                notificacionesJTableKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notificacionesJTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                notificacionesJTableKeyReleased(evt);
            }
        });


        notificacionesJScrollPane.setViewportView(notificacionesJTable);

        notificacionesJPanel.add(notificacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 990, 140));

        datosNotificacionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder("Datos de Notificaci\u00f3n"));
        datosNombreApellidosJLabel.setText("Nombre y Apellidos:");
        datosNotificacionJPanel.add(datosNombreApellidosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 220, 20));

        datosDireccionJLabel.setText("Direcci\u00f3n:");
        datosNotificacionJPanel.add(datosDireccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 220, 20));

        datosCPostalJLabel.setText("C.Postal/Municipio/Provincia:");
        datosNotificacionJPanel.add(datosCPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 220, 20));

        datosNotificarPorJLabel.setText("Notificar Por:");
        datosNotificacionJPanel.add(datosNotificarPorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 220, 20));

        datosNombreApellidosJTField.setEditable(false);
        datosNombreApellidosJTField.setBorder(null);
        datosNotificacionJPanel.add(datosNombreApellidosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 700, 20));

        datosDireccionJTField.setEditable(false);
        datosDireccionJTField.setBorder(null);
        datosNotificacionJPanel.add(datosDireccionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 700, 20));

        datosNotificarPorJTField.setEditable(false);
        datosNotificarPorJTField.setBorder(null);
        datosNotificacionJPanel.add(datosNotificarPorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 700, 20));

        datosCPostalJTField.setEditable(false);
        datosCPostalJTField.setBorder(null);
        datosNotificacionJPanel.add(datosCPostalJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 700, 20));

        entregadaAJTField.setEditable(false);
        entregadaAJTField.setBorder(null);
        datosNotificacionJPanel.add(entregadaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, 700, 20));

        entregadaAJLabel.setText("Entregada A:");
        datosNotificacionJPanel.add(entregadaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 220, 20));

        notificacionesJPanel.add(datosNotificacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 990, 140));

        add(notificacionesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 1010, 310));

        aceptarJButton.setText("Aceptar");
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed(evt);
            }
        });        

        add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 620, 140, -1));

        salirJButton.setText("Cancelar");
        salirJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salirJButtonMouseClicked();
            }
        });

        add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 620, 120, -1));

    }//GEN-END:initComponents

    private void notificacionesJTableMouseDragged() {//GEN-FIRST:event_notificacionesJTableMouseDragged
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableMouseDragged

    private void notificacionesJTableFocusGained() {//GEN-FIRST:event_notificacionesJTableFocusGained
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableFocusGained

    private void notificacionesJTableKeyTyped(java.awt.event.KeyEvent evt) {
        mostrarNotificacionSeleccionada();
    }
    private void notificacionesJTableKeyPressed(java.awt.event.KeyEvent evt){
        mostrarNotificacionSeleccionada();
    }
    private void notificacionesJTableKeyReleased(java.awt.event.KeyEvent evt){
        mostrarNotificacionSeleccionada();
    }


    private void eventosJTableFocusGained() {//GEN-FIRST:event_eventosJTableFocusGained
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_eventosJTableFocusGained

    private void eventosJTableMouseDragged() {//GEN-FIRST:event_eventosJTableMouseDragged
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_eventosJTableMouseDragged

    private void eventosJTableKeyTyped(java.awt.event.KeyEvent evt) {
        mostrarEventoSeleccionado();
    }
    private void eventosJTableKeyPressed(java.awt.event.KeyEvent evt){
        mostrarEventoSeleccionado();
    }
    private void eventosJTableKeyReleased(java.awt.event.KeyEvent evt){
        mostrarEventoSeleccionado();
    }


    private void salirJButtonMouseClicked() {//GEN-FIRST:event_salirJButtonMouseClicked
        CConstantesLicencias.helpSetHomeID= "licenciasIntro";
        if (this.desktopInternal != null) this.desktopInternal.dispose();
        else this.desktop.dispose();
    }//GEN-LAST:event_salirJButtonMouseClicked

    private void aceptarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarJButtonMouseClicked
         if ((_expedienteSelected == null) || (_solicitudSelected == null)) {
            mostrarMensaje(literales.getString("CTareasPendientes.mensaje2"));
            return;
        }

        CMainLicencias mainLicencias = null;
        MainActividad mainActividad= null;
        if (desktop instanceof CMainLicencias)
            mainLicencias=(CMainLicencias) desktop;
        if (desktop instanceof MainActividad)
            mainActividad=(MainActividad) desktop;

        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;

        /** Comprobamos si el expediente esta bloqueado */
        if (CUtilidadesComponentes.expedienteBloqueado(_expedienteSelected.getNumExpediente(), literales.getLocale().toString())){
            if (CUtilidadesComponentes.mostrarMensajeBloqueo(this,literales, true) != 0) return;
        }

        /** Vamos a Modificacion de expediente */
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (this.desktopInternal != null) this.desktopInternal.dispose();
        else this.desktop.dispose();

        if (_solicitudSelected.getTipoLicencia().getIdTipolicencia() == new Integer(CConstantesLicencias.LicenciasObraMayor).intValue()){
            CConstantesLicencias.helpSetHomeID = "licenciasModificacionObraMayor";
            CConstantesLicencias.selectedSubApp= CConstantesLicencias.LicenciasObraMayor;
            mainLicencias.setLang("licenciasObraMayor",CConstantesLicencias.Locale);
            com.geopista.app.licencias.modificacion.CModificacionLicencias modificacionLicencias = new com.geopista.app.licencias.modificacion.CModificacionLicencias((JFrame)this.desktop, _expedienteSelected.getNumExpediente(), false);
            mainLicencias.mostrarJInternalFrame(modificacionLicencias);

        }else if (_solicitudSelected.getTipoLicencia().getIdTipolicencia() == new Integer(CConstantesLicencias.LicenciasObraMenor).intValue()){
            CConstantesLicencias.helpSetHomeID = "licenciasModificacionObraMenor";
            CConstantesLicencias.selectedSubApp= CConstantesLicencias.LicenciasObraMenor;
            mainLicencias.setLang("licenciasObraMenor",CConstantesLicencias.Locale);
            com.geopista.app.licencias.obraMenor.modificacion.CModificacionLicencias modificacionLicencias = new com.geopista.app.licencias.obraMenor.modificacion.CModificacionLicencias((JFrame)this.desktop, _expedienteSelected.getNumExpediente(), false);
            mainLicencias.mostrarJInternalFrame(modificacionLicencias);
        }else if(mainActividad!=null){
            com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias modificacionLicencias = new com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias((JFrame)this.desktop, _expedienteSelected.getNumExpediente(), literales, false);
            mainActividad.mostrarJInternalFrame(modificacionLicencias);
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_aceptarJButtonMouseClicked

    private void eventosJTableMouseClicked() {//GEN-FIRST:event_eventosJTableMouseClicked
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_eventosJTableMouseClicked

    private void notificacionesJTableMouseClicked() {//GEN-FIRST:event_notificacionesJTableMouseClicked
          mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableMouseClicked


    public void mostrarEventoSeleccionado(){
        if (notificacionesJTable.getSelectedRow() != -1){
            notificacionesJTable.clearSelection();
        }

		int row = eventosJTable.getSelectedRow();

		if (row != -1) {
			CEvento evento = (CEvento) _vEventos.get(((Integer)eventosSorted.getValueAt(row, eventosHiddenCol)).intValue());
			eventoJTArea.setText(evento.getContent());

            _expedienteSelected= evento.getExpediente();
            _solicitudSelected= (CSolicitudLicencia)_vSolicitudesEv.get(((Integer)eventosSorted.getValueAt(row, eventosHiddenCol)).intValue());

		}else{
            _expedienteSelected= null;
            _solicitudSelected= null;
            clearDatosEvento();
        }
    }

    public void clearDatosEvento(){
        eventoJTArea.setText("");
    }

    public void mostrarNotificacionSeleccionada(){
        if (eventosJTable.getSelectedRow() != -1){
            eventosJTable.clearSelection();
        }

        int row = notificacionesJTable.getSelectedRow();
        if (row != -1) {
            CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(((Integer)notificacionesSorted.getValueAt(row, notificacionesHiddenCol)).intValue());
            CPersonaJuridicoFisica persona = notificacion.getPersona();
            CDatosNotificacion datos = persona.getDatosNotificacion();
            String interesado= CUtilidades.componerCampo(persona.getApellido2(), persona.getApellido1(), persona.getNombre());
            datosNombreApellidosJTField.setText(interesado);
            datosDireccionJTField.setText(CUtilidades.componerCampo(((DomainNode)Estructuras.getListaTiposViaINE().getDomainNode(datos.getTipoVia())).getTerm(literales.getLocale().toString()),
                    datos.getNombreVia(),
                    datos.getNumeroVia()));
            datosCPostalJTField.setText(CUtilidades.componerCampo(datos.getCpostal(), datos.getMunicipio(), datos.getProvincia()));
            datosNotificarPorJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(datos.getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(literales.getLocale().toString()));

            _expedienteSelected= notificacion.getExpediente();
            _solicitudSelected= notificacion.getSolicitud();
        }else{
            _expedienteSelected= null;
            _solicitudSelected= null;
            clearDatosNotificacion();
        }
    }

    public void clearDatosNotificacion(){
        datosNombreApellidosJTField.setText("");
        datosDireccionJTField.setText("");
        datosCPostalJTField.setText("");
        datosNotificarPorJTField.setText("");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JLabel datosCPostalJLabel;
    private javax.swing.JTextField datosCPostalJTField;
    private javax.swing.JLabel datosDireccionJLabel;
    private javax.swing.JTextField datosDireccionJTField;
    private javax.swing.JLabel datosNombreApellidosJLabel;
    private javax.swing.JTextField datosNombreApellidosJTField;
    private javax.swing.JPanel datosNotificacionJPanel;
    private javax.swing.JLabel datosNotificarPorJLabel;
    private javax.swing.JTextField datosNotificarPorJTField;
    private javax.swing.JLabel entregadaAJLabel;
    private javax.swing.JTextField entregadaAJTField;
    private javax.swing.JScrollPane eventoJScrollPane;
    private javax.swing.JTextArea eventoJTArea;
    private javax.swing.JPanel eventosJPanel;
    private javax.swing.JScrollPane eventosJScrollPane;
    private javax.swing.JTable eventosJTable;
    private javax.swing.JPanel notificacionesJPanel;
    private javax.swing.JScrollPane notificacionesJScrollPane;
    private javax.swing.JTable notificacionesJTable;
    private javax.swing.JButton salirJButton;
    // End of variables declaration//GEN-END:variables

    /*******************************************************************/
    /*                         Metodos propios                         */
    /**
     * ****************************************************************
     */

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(eventosJPanel, mensaje);
    }

    public void renombrarComponentes(ResourceBundle literales) {

        this.literales=literales;
        try {
            eventosJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CTareasPendientes.eventosJPanel.TitleBorder")));
            notificacionesJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CTareasPendientes.notificacionesJPanel.TitleBorder")));

            eventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CTareasPendientes.eventoJScrollPane.TitleBorder")));
            datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CTareasPendientes.datosNotificacionJPanel.TitleBorder")));

            datosCPostalJLabel.setText(literales.getString("CTareasPendientes.datosCPostalJLabel.text"));
            datosDireccionJLabel.setText(literales.getString("CTareasPendientes.datosDireccionJLabel.text"));
            datosNotificarPorJLabel.setText(literales.getString("CTareasPendientes.datosNotificarPorJLabel.text"));
            datosNombreApellidosJLabel.setText(literales.getString("CTareasPendientes.datosNombreApellidosJLabel.text"));
            entregadaAJLabel.setText(literales.getString("CTareasPendientes.entregadaAJLabel.text"));

            salirJButton.setText(literales.getString("CTareasPendientes.salirJButton.text"));
            aceptarJButton.setText(literales.getString("CTareasPendientes.aceptarJButton.text"));

            salirJButton.setToolTipText(literales.getString("CTareasPendientes.salirJButton.text"));
            aceptarJButton.setToolTipText(literales.getString("CTareasPendientes.aceptarJButton.text"));

            /** Headers tabla eventos */
            TableColumn tableColumn= eventosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.eventosTableModel.text.column1"));
            tableColumn= eventosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.eventosTableModel.text.column2"));
            tableColumn= eventosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.eventosTableModel.text.column3"));
            tableColumn= eventosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.eventosTableModel.text.column4"));
            tableColumn= eventosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.eventosTableModel.text.column5"));
            tableColumn= eventosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.eventosTableModel.text.column6"));
            tableColumn= eventosJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.eventosTableModel.text.column7"));
            tableColumn= eventosJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.eventosTableModel.text.column8"));
            tableColumn= eventosJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.eventosTableModel.text.column9"));

            /** Headers tabla notificaciones */
            tableColumn= notificacionesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.notificacionesTableModel.text.column1"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.notificacionesTableModel.text.column2"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.notificacionesTableModel.text.column9"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.notificacionesTableModel.text.column3"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.notificacionesTableModel.text.column4"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.notificacionesTableModel.text.column5"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.notificacionesTableModel.text.column6"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(literales.getString("CTareasPendientes.notificacionesTableModel.text.column8"));

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }

    public void initEstructuras(){
        while (!Estructuras.isCargada())
        {
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }

    }


    public boolean rellenarEventosYNotificaciones(){
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean sinNotificaciones= true;
        boolean sinEventos= true;

        /** Notificaciones */
        CResultadoOperacion ro= COperacionesLicencias.getNotificacionesPendientes( ((IMainLicencias)desktop).getTiposLicencia());
        if (ro != null){
            _vNotificaciones= ro.getVector();

            if ((_vNotificaciones != null) && (_vNotificaciones.size() > 0)) {
                sinNotificaciones= false;

                CUtilidadesComponentes.clearTable(_notificacionesTableModel);

                for (int i=0; i < _vNotificaciones.size(); i++){
                    CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(i);
                    try{
                        String sTipoLicencia="";
                        if (desktop instanceof CMainLicencias)
                            sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(notificacion.getSolicitud().getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        if (desktop instanceof MainActividad)
                            sTipoLicencia=((DomainNode)Estructuras.getListaTiposLicenciaActividad().getDomainNode(new Integer(notificacion.getSolicitud().getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());

                        String interesado= CUtilidades.componerCampo(notificacion.getPersona().getApellido1(), notificacion.getPersona().getApellido2(), notificacion.getPersona().getNombre());
                        /** intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                        String emplazamiento= CUtilidades.getEmplazamiento(notificacion.getSolicitud(), CMainLicencias.literales);
                        Object[] rowData = {sTipoLicencia,
                                        notificacion.getExpediente().getNumExpediente(),
                                        emplazamiento,
                                        ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(notificacion.getExpediente().getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                                        //notificacion.getPersona().getDniCif(),
                                        interesado,
                                        ((DomainNode)Estructuras.getListaEstadosNotificacion().getDomainNode(new Integer(notificacion.getEstadoNotificacion().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                                        CUtilidades.formatFecha(notificacion.getPlazoVencimiento()),
                                        //CUtilidades.formatFecha(notificacion.getFechaNotificacion()),
                                        CUtilidades.formatFecha(notificacion.getFecha_reenvio()),
                                        new Integer(i)};

                        _notificacionesTableModel.addRow(rowData);

                    }catch(Exception e){
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        logger.error("Error al cargar las tareas pendientes: " + sw.toString());
                    }
                 }
            }
        }

        /** Eventos */
        ro = COperacionesLicencias.getEventosPendientes( ((IMainLicencias)desktop).getTiposLicencia(), literales.getLocale().toString());
        if (ro != null) {
            _vEventos = ro.getVector();
            _vSolicitudesEv= ro.getSolicitudes();

            CUtilidadesComponentes.clearTable(_eventosTableModel);
            eventoJTArea.setText("");

            if ((_vEventos != null) && (_vEventos.size() != 0) && (_vSolicitudesEv != null) && (_vSolicitudesEv.size() != 0)) {
                sinEventos= false;

                // Annadimos el editor CheckBox en la septima columna de las tablas eventosJTable
                int vColIndexCheck = 7;
                TableColumn col6 = eventosJTable.getColumnModel().getColumn(vColIndexCheck);
                col6.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
                col6.setCellRenderer(new CheckBoxRenderer());

                for (int i = 0; i < _vEventos.size(); i++) {
                    CEvento evento = (CEvento) _vEventos.get(i);
                    CSolicitudLicencia solicitud = (CSolicitudLicencia) _vSolicitudesEv.get(i);
                    CheckBoxRenderer renderCheck = (CheckBoxRenderer) eventosJTable.getCellRenderer(i, vColIndexCheck);
                    if (evento.getRevisado().equalsIgnoreCase("1"))
                        renderCheck.setSelected(true);
                    else
                        renderCheck.setSelected(false);
                    try
                    {
                        String sTipoLicencia="";
                        if (desktop instanceof CMainLicencias)
                            sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        if (desktop instanceof MainActividad)
                            sTipoLicencia=((DomainNode)Estructuras.getListaTiposLicenciaActividad().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());

                        String nombreSolicitante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());
                        /** intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                        String emplazamiento= CUtilidades.getEmplazamiento(solicitud, CMainLicencias.literales);

                         _eventosTableModel.addRow(new Object[]{
                         sTipoLicencia,
                         evento.getExpediente().getNumExpediente(),
                         emplazamiento,
                        ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(evento.getExpediente().getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                        //solicitud.getPropietario().getDniCif(),
                        nombreSolicitante,
                        ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(evento.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                        CUtilidades.formatFechaH24(evento.getFechaEvento()),
                        new Boolean(renderCheck.isSelected()),
                        evento.getContent(),
                        new Integer(i)});

                    }catch(Exception e){
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        logger.error("Error al cargar las tareas pendientes: " + sw.toString());
                    }
                }
            }
        }

        if ((sinEventos) && (sinNotificaciones)) {
            JOptionPane.showMessageDialog(desktop, literales.getString("CTareasPendientes.mensaje1"));
            CUtilidadesComponentes.clearTable(_notificacionesTableModel);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return false;
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return true;
    }

    public void aceptarJButtonSetEnabled(boolean b){        
        aceptarJButton.setEnabled(b);
    }



}
