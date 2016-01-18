/**
 * TareasPendientesJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * TareasPendientesJPanel.java
 *
 * Created on 16 de noviembre de 2004, 12:34
 */

package com.geopista.app.ocupaciones.panel;

import java.awt.Cursor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.ocupaciones.CConstantesOcupaciones;
import com.geopista.app.ocupaciones.CEventosTableModel;
import com.geopista.app.ocupaciones.CMainOcupaciones;
import com.geopista.app.ocupaciones.CNotificacionesTableModel;
import com.geopista.app.ocupaciones.CUtilidades;
import com.geopista.app.ocupaciones.CUtilidadesComponentes;
import com.geopista.app.ocupaciones.CheckBoxRenderer;
import com.geopista.app.ocupaciones.CheckBoxTableEditor;
import com.geopista.app.ocupaciones.Estructuras;
import com.geopista.app.ocupaciones.IMultilingue;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.CDatosNotificacion;
import com.geopista.protocol.licencias.CEvento;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CNotificacion;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CSolicitudLicencia;

/**
 *
 * @author  charo
 */
public class TareasPendientesJPanel extends javax.swing.JPanel implements IMultilingue {

    private Vector _vEventos= null;
    private Vector _vExpedientesEv= null;
    private Vector _vSolicitudesEv= null;

    private Vector _vNotificaciones = null;
    /** Necesario para ir a Modificacion de expdiente */
    private CExpedienteLicencia _expedienteSelected= null;

    private java.awt.Window desktop;
    private JFrame desktopMain;
    private JInternalFrame desktopInternal= null;

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

    /** Creates new form TareasPendientesJPanel */
    public TareasPendientesJPanel(java.awt.Window w, JFrame main) {

		this.desktop= w;
        this.desktopMain= main;
        initComponents();
        init();
    }

    public TareasPendientesJPanel(JInternalFrame desktop, JFrame main) {
        this.desktopInternal= desktop;
        this.desktopMain= main;
        initComponents();
        init();
    }

    public void init(){

        initEstructuras();
        try
        {
            String[] columnNamesEventos= {CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column1"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column2"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column10"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column3"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column4"),
                               //CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column5"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column9"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column6"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column7"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column8"),
                               "HIDDEN"};

            CEventosTableModel.setColumnNames(columnNamesEventos);
        }catch(Exception e)
        {
            logger.error(e);
        }

        try
        {
            String[] columnNamesNotificaciones= {CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column1"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column2"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column9"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column3"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column5"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column8"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column4"),
                               //CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column6"),
                               CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column7"),
                               "HIDDEN"};

            CNotificacionesTableModel.setColumnNames(columnNamesNotificaciones);
        }catch(Exception e)
        {
            logger.error(e);
        }

        _notificacionesTableModel = new CNotificacionesTableModel();
        notificacionesSorted= new TableSorted(_notificacionesTableModel);
        notificacionesSorted.setTableHeader(notificacionesJTable.getTableHeader());
		notificacionesJTable.setModel(notificacionesSorted);
        notificacionesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificacionesJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)notificacionesJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notificacionesJTableFocusGained(evt);
            }
        });
        ((TableSorted)notificacionesJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                notificacionesJTableMouseClicked(evt);
            }
        });
        TableColumn col= notificacionesJTable.getColumnModel().getColumn(notificacionesHiddenCol);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);



        _eventosTableModel = new CEventosTableModel();
        eventosSorted= new TableSorted(_eventosTableModel);
        eventosSorted.setTableHeader(eventosJTable.getTableHeader());
		eventosJTable.setModel(eventosSorted);
        eventosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventosJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)eventosJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eventosJTableFocusGained(evt);
            }
        });
        ((TableSorted)eventosJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eventosJTableMouseClicked(evt);
            }
        });
        col= eventosJTable.getColumnModel().getColumn(eventosHiddenCol);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);

		renombrarComponentes();

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
        eventosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eventosJTableMouseClicked(evt);
            }
        });
        eventosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eventosJTableFocusGained(evt);
            }
        });
        eventosJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                eventosJTableMouseDragged(evt);
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

        eventosJPanel.add(eventosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 990, 220));

        eventoJScrollPane.setBorder(new javax.swing.border.TitledBorder("Descripci\u00f3n del Evento Seleccionado"));
        eventoJTArea.setEditable(false);
        eventoJTArea.setLineWrap(true);
        eventoJTArea.setWrapStyleWord(true);
        eventoJScrollPane.setViewportView(eventoJTArea);

        eventosJPanel.add(eventoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 990, 100));

        add(eventosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 350));

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
        notificacionesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                notificacionesJTableMouseClicked(evt);
            }
        });
        notificacionesJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notificacionesJTableFocusGained(evt);
            }
        });
        notificacionesJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                notificacionesJTableMouseDragged(evt);
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

        notificacionesJPanel.add(notificacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 990, 130));

        datosNotificacionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder("Datos de Notificaci\u00f3n"));
        datosNombreApellidosJLabel.setText("Nombre y Apellidos:");
        datosNotificacionJPanel.add(datosNombreApellidosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        datosDireccionJLabel.setText("Direcci\u00f3n:");
        datosNotificacionJPanel.add(datosDireccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 20));

        datosCPostalJLabel.setText("C.Postal/Municipio/Provincia:");
        datosNotificacionJPanel.add(datosCPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, 20));

        datosNotificarPorJLabel.setText("Notificar Por:");
        datosNotificacionJPanel.add(datosNotificarPorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 130, 20));

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

        notificacionesJPanel.add(datosNotificacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 990, 110));

        add(notificacionesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 1010, 270));

        aceptarJButton.setText("Aceptar");
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed(evt);
            }
        });


        add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 620, 120, -1));

        salirJButton.setText("Cancelar");
        salirJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salirJButtonMouseClicked(evt);
            }
        });

        add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 620, 120, -1));

    }//GEN-END:initComponents

    private void salirJButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirJButtonMouseClicked
        // TODO add your handling code here:
        CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
        if (this.desktopInternal != null) this.desktopInternal.dispose();
        else this.desktop.dispose();
    }//GEN-LAST:event_salirJButtonMouseClicked

    private void aceptarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarJButtonMouseClicked
        // TODO add your handling code here:
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        if (_expedienteSelected == null) {
            mostrarMensaje(CMainOcupaciones.literales.getString("CTareasPendientes.mensaje2"));
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        CConstantesOcupaciones.helpSetHomeID = "ocupacionesModificacion";
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktopMain);

        /** Comprobamos si el expediente esta bloqueado */
        if (CUtilidadesComponentes.expedienteBloqueado(_expedienteSelected.getNumExpediente(), CMainOcupaciones.currentLocale)){
            if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, true) != 0){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }
        }

        if (this.desktopInternal != null) this.desktopInternal.dispose();
        else this.desktop.dispose();

        CMainOcupaciones mainOcupaciones = (CMainOcupaciones)desktopMain;
        mainOcupaciones.mostrarJInternalFrame(new com.geopista.app.ocupaciones.CModificacionLicencias((JFrame)this.desktopMain, _expedienteSelected.getNumExpediente(), false));

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_aceptarJButtonMouseClicked

    private void eventosJTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eventosJTableMouseClicked
        // TODO add your handling code here:
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_eventosJTableMouseClicked

    private void eventosJTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resultadosJTableFocusGained
        // TODO add your handling code here:
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void eventosJTableMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resultadosJTableMouseDragged
        // TODO add your handling code here:
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableMouseDragged

    private void eventosJTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resultadosJTableKeyReleased
        // TODO add your handling code here:
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableKeyReleased

    private void eventosJTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resultadosJTableKeyPressed
        // TODO add your handling code here:
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableKeyPressed

    private void eventosJTableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resultadosJTableKeyTyped
        // TODO add your handling code here:
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableKeyTyped



    private void mostrarEventoSeleccionado(){

        if (notificacionesJTable.getSelectedRow() != -1){
            notificacionesJTable.clearSelection();
        }
        

		int row = eventosJTable.getSelectedRow();

		if (row != -1) {
            eventoJTArea.setText("");
            CEvento evento = (CEvento) _vEventos.get(((Integer)eventosSorted.getValueAt(row, eventosHiddenCol)).intValue());
			eventoJTArea.setText(evento.getContent());

            _expedienteSelected= evento.getExpediente();
		}else _expedienteSelected= null;
    }

    private void notificacionesJTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_notificacionesJTableMouseClicked
        // TODO add your handling code here:
        mostrarNotificacionSeleccionada();

    }//GEN-LAST:event_notificacionesJTableMouseClicked

    private void notificacionesJTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resultadosJTableFocusGained
        // TODO add your handling code here:
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void notificacionesJTableMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resultadosJTableMouseDragged
        // TODO add your handling code here:
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_resultadosJTableMouseDragged

    private void notificacionesJTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ultimosJTableKeyReleased
        // TODO add your handling code here:
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_ultimosJTableKeyReleased

    private void notificacionesJTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ultimosJTableKeyPressed
        // TODO add your handling code here:
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_ultimosJTableKeyPressed

    private void notificacionesJTableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ultimosJTableKeyTyped
        // TODO add your handling code here:
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_ultimosJTableKeyTyped



    private void mostrarNotificacionSeleccionada(){

        if (eventosJTable.getSelectedRow() != -1){
            eventosJTable.clearSelection();
        }


        int row = notificacionesJTable.getSelectedRow();
        if (row != -1) {
            datosNombreApellidosJTField.setText("");
            datosDireccionJTField.setText("");
            datosCPostalJTField.setText("");
            datosNotificarPorJTField.setText("");

            CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(((Integer)notificacionesSorted.getValueAt(row, notificacionesHiddenCol)).intValue());
            CPersonaJuridicoFisica persona = notificacion.getPersona();
            CDatosNotificacion datos = persona.getDatosNotificacion();
            String interesado= CUtilidades.componerCampo(persona.getApellido2(), persona.getApellido1(), persona.getNombre());
            datosNombreApellidosJTField.setText(interesado);
            datosDireccionJTField.setText(CUtilidades.componerCampo(((DomainNode)Estructuras.getListaTiposViaINE().getDomainNode(datos.getTipoVia())).getTerm(CMainOcupaciones.currentLocale.toString()),
                    datos.getNombreVia(),
                    datos.getNumeroVia()));
            datosCPostalJTField.setText(CUtilidades.componerCampo(datos.getCpostal(), datos.getMunicipio(), datos.getProvincia()));
            datosNotificarPorJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(datos.getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()));

            _expedienteSelected= notificacion.getExpediente();
        }else _expedienteSelected= null;

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

    public void renombrarComponentes() {

        try {
            eventosJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CTareasPendientes.eventosJPanel.TitleBorder")));
            notificacionesJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesJPanel.TitleBorder")));

            eventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CTareasPendientes.eventoJScrollPane.TitleBorder")));
            datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CTareasPendientes.datosNotificacionJPanel.TitleBorder")));

            salirJButton.setText(CMainOcupaciones.literales.getString("CTareasPendientes.salirJButton.text"));
            aceptarJButton.setText(CMainOcupaciones.literales.getString("CTareasPendientes.aceptarJButton.text"));

            datosCPostalJLabel.setText(CMainOcupaciones.literales.getString("CTareasPendientes.datosCPostalJLabel.text"));
            datosDireccionJLabel.setText(CMainOcupaciones.literales.getString("CTareasPendientes.datosDireccionJLabel.text"));
            datosNombreApellidosJLabel.setText(CMainOcupaciones.literales.getString("CTareasPendientes.datosNombreApellidosJLabel.text"));
            datosNotificarPorJLabel.setText(CMainOcupaciones.literales.getString("CTareasPendientes.datosNotificarPorJLabel.text"));

            salirJButton.setToolTipText(CMainOcupaciones.literales.getString("CTareasPendientes.salirJButton.text"));
            aceptarJButton.setToolTipText(CMainOcupaciones.literales.getString("CTareasPendientes.aceptarJButton.text"));

            /** Headers tabla eventos */
            TableColumn tableColumn= eventosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column1"));
            tableColumn= eventosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column2"));
            tableColumn= eventosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column10"));
            tableColumn= eventosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column3"));
            tableColumn= eventosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column4"));
            tableColumn= eventosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column9"));
            tableColumn= eventosJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column6"));
            tableColumn= eventosJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column7"));
            tableColumn= eventosJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.eventosTableModel.text.column8"));

            tableColumn= notificacionesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column1"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column2"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column9"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column3"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column5"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column8"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column4"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CTareasPendientes.notificacionesTableModel.text.column7"));

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

        /** Redefinimos el tamanno de las celdas fecha */
        TableColumn column = notificacionesJTable.getColumnModel().getColumn(6);
        CUtilidades.resizeColumn(column, 70);
        column = notificacionesJTable.getColumnModel().getColumn(7);
        CUtilidades.resizeColumn(column, 70);
        /*
        column = notificacionesJTable.getColumnModel().getColumn(8);
        CUtilidades.resizeColumn(column, 70);
        */
        column = eventosJTable.getColumnModel().getColumn(6);
        CUtilidades.resizeColumn(column, 70);
        /** Redefinimos el tamanno de la cedla revisado */
        column = eventosJTable.getColumnModel().getColumn(7);
        CUtilidades.resizeColumn(column, 70);

        boolean sinNotificaciones= true;
        boolean sinEventos= true;

        /** Notificaciones */
        CResultadoOperacion ro= COperacionesLicencias.getNotificacionesPendientes(null);
        if (ro != null){
            _vNotificaciones= ro.getVector();

            if ((_vNotificaciones != null) && (_vNotificaciones.size() > 0)) {
                sinNotificaciones= false;

                CUtilidadesComponentes.clearTable(_notificacionesTableModel);

                for (int i=0; i<_vNotificaciones.size(); i++) {
                    CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(i);
                    try{
                        String sEstado="";
                        try{sEstado=((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(notificacion.getExpediente().getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());}catch(Exception e){logger.error("Error al cargar el estado:"+notificacion.getExpediente().getEstado().getIdEstado(),e);}
                        String estadoNotificacion= "";
                        try{estadoNotificacion= ((DomainNode)Estructuras.getListaEstadosNotificacion().getDomainNode(new Integer(notificacion.getEstadoNotificacion().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());}catch(Exception e){logger.error("Error al cargar el estado de Notificacion:"+new Integer(notificacion.getEstadoNotificacion().getIdEstado()).toString(),e);}

                        String nombreInteresado= CUtilidades.componerCampo(notificacion.getPersona().getApellido1(), notificacion.getPersona().getApellido2(), notificacion.getPersona().getNombre());
                        /** intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                        String emplazamiento= CUtilidades.getEmplazamiento(notificacion.getSolicitud());

                        Object[] rowData = {((DomainNode)Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(notificacion.getSolicitud().getDatosOcupacion().getTipoOcupacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
                                        notificacion.getExpediente().getNumExpediente(),
                                        emplazamiento,
                                        sEstado,
                                        //notificacion.getPersona().getDniCif(),
                                        nombreInteresado,
                                        estadoNotificacion,
                                        CUtilidades.formatFecha(notificacion.getPlazoVencimiento()),
                                        //CUtilidades.formatFecha(notificacion.getFechaNotificacion()),
                                        CUtilidades.formatFecha(notificacion.getFecha_reenvio()),
                                        new Integer(i)};
                        _notificacionesTableModel.addRow(rowData);
                    }catch(Exception ex){
                        logger.error("Error al cargar las notificaciones",ex);
                    }
                }
            }
        }

        /** Eventos */
        ro = COperacionesLicencias.getEventosPendientes(null, CMainOcupaciones.literales.getLocale().toString());
        if (ro != null) {
            _vEventos = ro.getVector();
            _vExpedientesEv= ro.getExpedientes();
            _vSolicitudesEv= ro.getSolicitudes();

            CUtilidadesComponentes.clearTable(_eventosTableModel);
            eventoJTArea.setText("");

            if ((_vEventos != null) && (_vEventos.size() != 0) && (_vExpedientesEv != null) && (_vExpedientesEv.size() != 0) && (_vSolicitudesEv != null) && (_vSolicitudesEv.size() != 0)) {
                sinEventos= false;

                // Annadimos el editor CheckBox en la octava columna de las tablas eventosJTable
                int vColIndexCheck = 7;
                TableColumn col6 = eventosJTable.getColumnModel().getColumn(vColIndexCheck);
                col6.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
                col6.setCellRenderer(new CheckBoxRenderer());

                for (int i = 0; i < _vEventos.size(); i++) {
                    CEvento evento = (CEvento) _vEventos.elementAt(i);
                    CSolicitudLicencia solicitud = (CSolicitudLicencia) _vSolicitudesEv.elementAt(i);

                    CheckBoxRenderer renderCheck = (CheckBoxRenderer) eventosJTable.getCellRenderer(i, vColIndexCheck);
                    if (evento.getRevisado().equalsIgnoreCase("1"))
                        renderCheck.setSelected(true);
                    else
                        renderCheck.setSelected(false);

                    /* TRAZAS                    
                    logger.info("**************solicitud.getTipoLicencia().getIdTipolicencia()="+new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString());
                    Enumeration e= Estructuras.getListaTipoOcupacion().getLista().elements();
                    while(e.hasMoreElements()){
                          DomainNode node= (DomainNode)e.nextElement();
                        logger.info("PATRON=" + node.getPatron()+ " TERM="+ node.getTerm(CMainOcupaciones.currentLocale.toString()));

                    }
                    */

                    String nombreSolicitante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());
                    /** intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                    String emplazamiento= CUtilidades.getEmplazamiento(solicitud);

                    _eventosTableModel.addRow(new Object[]{
                        ((DomainNode)Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(evento.getExpediente().getDatosOcupacion().getTipoOcupacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
                        evento.getExpediente().getNumExpediente(),
                        emplazamiento,
                        ((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(evento.getExpediente().getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
                        //solicitud.getPropietario().getDniCif(),
                        nombreSolicitante,
                        //new Long(evento.getIdEvento()).toString(),
                        ((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(evento.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
                        CUtilidades.formatFecha(evento.getFechaEvento()),
                        new Boolean(renderCheck.isSelected()),
                        evento.getContent(),
                        new Integer(i)});
                }
            }
        }

        if ((sinEventos) && (sinNotificaciones)) {
            JOptionPane.showMessageDialog(desktopMain,
                    CMainOcupaciones.literales.getString("CTareasPendientes.mensaje1"));
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
