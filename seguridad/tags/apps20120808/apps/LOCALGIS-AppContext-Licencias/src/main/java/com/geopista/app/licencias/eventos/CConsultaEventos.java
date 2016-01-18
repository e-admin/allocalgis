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
public class CConsultaEventos extends javax.swing.JInternalFrame implements IMultilingue {

	private Vector _vEventos = null;
	private Vector _vExpedientes = null;
	private Vector _vSolicitudes = null;
    private ResourceBundle literales;

	private JFrame desktop;

	/**
	 * Modelo para el componente resultadosJTable
	 */
	private DefaultTableModel _resultadosTableModel;
    TableSorted tableSorted= new TableSorted();
    int hiddenColumn= 10;


	Logger logger = Logger.getLogger(CConsultaEventos.class);

	/**
	 * Creates new form CConsultaEventos
	 */
	public CConsultaEventos(JFrame desktop, ResourceBundle literales) {
        this.desktop = desktop;
        this.literales=literales;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

		initComponents();
        initComboBoxesEstructuras();
        try
        {
            String[] columnNames= {literales.getString("CConsultaEventos.resultadosJTable.text.column10"),
                           literales.getString("CConsultaEventos.resultadosJTable.text.column1"),
                           literales.getString("CConsultaEventos.resultadosJTable.text.column2"),
                           literales.getString("CConsultaEventos.resultadosJTable.text.column3"),
                           literales.getString("CConsultaEventos.resultadosJTable.text.column4"),
                           literales.getString("CConsultaEventos.resultadosJTable.text.column5"),
                           literales.getString("CConsultaEventos.resultadosJTable.text.column6"),
                           literales.getString("CConsultaEventos.resultadosJTable.text.column7"),
                           literales.getString("CConsultaEventos.resultadosJTable.text.column8"),
                           literales.getString("CConsultaEventos.resultadosJTable.text.column9"),
                           "HIDDEN"};

            CEventosTableModel.setColumnNames(columnNames);
        }catch(Exception e)
        {
            logger.error("Error al cargar las cabeceras:",e);
        }

        _resultadosTableModel = new CEventosTableModel();

		buscarExpedienteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		fechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		fechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        borrarFechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
        borrarFechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);

		DNIJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);

        tableSorted= new TableSorted(_resultadosTableModel);
        tableSorted.setTableHeader(resultadosJTable.getTableHeader());
		resultadosJTable.setModel(tableSorted);
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
        /** Debido a que las filas de la tabla se pueden ordenar por cualquiera de las columnas en
         * modo ascendente o descendente, el valor rowSelected ya no nos sirve para acceder al
         * vector de eventos y recoger el evento seleccionado.
         * Es necesario por tanto almacenar las posiciones que en el vector ocupan los eventos en la tabla. Para ello,
         * creamos una columna hidden con los valores de la posicion del evento en el vector.
         */
        TableColumn col= resultadosJTable.getColumnModel().getColumn(hiddenColumn);
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
        borrarFechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();
        sinRevisarJRadioButton = new javax.swing.JRadioButton();
        todosJRadioButton = new javax.swing.JRadioButton();
        revisadosJRadioButton = new javax.swing.JRadioButton();
        resultadosJScrollPane = new javax.swing.JScrollPane();
        resultadosJTable = new javax.swing.JTable();
        salirJButton = new javax.swing.JButton();
        eventoJScrollPane = new javax.swing.JScrollPane();
        eventoJTArea = new javax.swing.JTextArea();
        irExpedienteJButton = new javax.swing.JButton();

        setClosable(true);
        setTitle("Consulta de Eventos");
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

        datosBusquedaJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 72, 290, 20));

        fechaJLabel.setText("Fecha (desde/hasta):");
        datosBusquedaJPanel.add(fechaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 220, 20));

        datosBusquedaJPanel.add(fechaDesdeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 135, 100, 20));

        datosBusquedaJPanel.add(fechaHastaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 135, 100, 20));

        tipoLicenciaJLabel.setText("Tipo Licencia:");
        datosBusquedaJPanel.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 220, 20));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 160, -1, -1));

        datosBusquedaJPanel.add(DNIJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 114, 290, 20));

        DNIJLabel.setText("DNI/CIF del Propietario:");
        datosBusquedaJPanel.add(DNIJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 220, 20));

        fechaDesdeJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 135, 20, 20));

        fechaHastaJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 135, 20, 20));

        DNIJButton.setIcon(new javax.swing.ImageIcon(""));
        DNIJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        DNIJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        DNIJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DNIJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(DNIJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 114, 20, 20));

        buscarExpedienteJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 72, 20, 20));

        estadoJLabel.setText("Estado actual de Expediente:");
        datosBusquedaJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 220, 20));

        tipoObraJLabel.setText("Tipo Obra");
        datosBusquedaJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 220, 20));

        borrarFechaDesdeJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarFechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 135, 20, 20));

        borrarFechaHastaJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarFechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 135, 20, 20));

        sinRevisarJRadioButton.setText("Sin Revisar");
        sinRevisarJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sinRevisarJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(sinRevisarJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 170, -1));

        todosJRadioButton.setText("Todos");
        todosJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todosJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(todosJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 160, 160, -1));

        revisadosJRadioButton.setText("Revisados");
        revisadosJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revisadosJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(revisadosJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, 190, -1));

        templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 980, 210));

        resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder("Resultado de la B\u00fasqueda"));
        resultadosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));

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

        templateJPanel.add(resultadosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 220, 980, 300));

        salirJButton.setText("Cancelar");
        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirJButtonActionPerformed();
            }
        });

        templateJPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 610, 90, -1));

        eventoJScrollPane.setBorder(new javax.swing.border.TitledBorder("Descripci\u00f3n del Evento Seleccionado"));
        eventoJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        eventoJTArea.setEditable(false);
        eventoJTArea.setLineWrap(true);
        eventoJTArea.setRows(3);
        eventoJTArea.setTabSize(4);
        eventoJTArea.setWrapStyleWord(true);
        eventoJTArea.setBorder(null);
        eventoJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        eventoJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        eventoJScrollPane.setViewportView(eventoJTArea);

        templateJPanel.add(eventoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 520, 980, 70));

        irExpedienteJButton.setText("Ir Expediente");
        irExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irExpedienteJButtonActionPerformed();
            }
        });

        templateJPanel.add(irExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 610, 130, -1));

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void irExpedienteJButtonActionPerformed() {//GEN-FIRST:event_irExpedienteJButtonActionPerformed
        int row= resultadosJTable.getSelectedRow();
        if (row == -1){
            mostrarMensaje(literales.getString("CConsultaEventos.mensaje4"));
            return;
        }

        CEvento evento= (CEvento) _vEventos.get(((Integer)tableSorted.getValueAt(row, hiddenColumn)).intValue());
        CExpedienteLicencia expediente= (CExpedienteLicencia) _vExpedientes.get(((Integer)tableSorted.getValueAt(row, hiddenColumn)).intValue());
        CSolicitudLicencia solicitud= (CSolicitudLicencia) _vSolicitudes.get(((Integer)tableSorted.getValueAt(row, hiddenColumn)).intValue());

        if ((evento == null) || (expediente == null) || (solicitud == null)) return;

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
        if (desktop instanceof IMainLicencias){
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
                com.geopista.app.licencias.obraMenor.modificacion.CModificacionLicencias modificacionLicencias= new com.geopista.app.licencias.obraMenor.modificacion.CModificacionLicencias(desktop, expediente.getNumExpediente(), false);
                mainLicencias.mostrarJInternalFrame(modificacionLicencias);
            }
        }
        else{
        	IMainLicencias mainActividad = (IMainLicencias) desktop;
            com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias modificacionLicencias = new com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias(desktop, expediente.getNumExpediente(),literales, false);
            mainActividad.mostrarJInternalFrame(modificacionLicencias);
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_irExpedienteJButtonActionPerformed

    private void resultadosJTableMouseDragged() {//GEN-FIRST:event_resultadosJTableMouseDragged
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableMouseDragged

    private void resultadosJTableFocusGained() {//GEN-FIRST:event_resultadosJTableFocusGained
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void resultadosJTableKeyTyped(java.awt.event.KeyEvent evt) {
        mostrarEventoSeleccionado();
    }
    private void resultadosJTableKeyPressed(java.awt.event.KeyEvent evt){
        mostrarEventoSeleccionado();
    }
    private void resultadosJTableKeyReleased(java.awt.event.KeyEvent evt){
        mostrarEventoSeleccionado();
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

    private void borrarFechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaDesdeJButtonActionPerformed
        fechaDesdeJTField.setText("");
    }//GEN-LAST:event_borrarFechaDesdeJButtonActionPerformed

    private void borrarFechaHastaJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaHastaJButtonActionPerformed
        fechaHastaJTField.setText("");
    }//GEN-LAST:event_borrarFechaHastaJButtonActionPerformed

    private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
        CConstantesLicencias.helpSetHomeID = "licenciasIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);
    }//GEN-LAST:event_formInternalFrameClosed

	private void resultadosJTableMouseClicked() {//GEN-FIRST:event_resultadosJTableMouseClicked
        mostrarEventoSeleccionado();
	}//GEN-LAST:event_resultadosJTableMouseClicked

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

		if (CConstantesLicencias.persona != null) {
			DNIJTField.setText(CConstantesLicencias.persona.getDniCif());
		}
	}//GEN-LAST:event_DNIJButtonActionPerformed

	private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed
    	CUtilidadesComponentes.showSearchDialog(desktop, tipoLicenciaEJCBox.getSelectedPatron(), tipoObraEJCBox.getSelectedPatron(), estadoExpedienteEJCBox.getSelectedPatron(), DNIJTField.getText(), literales);
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
        // El patron -1 corresponde a la opcion por defecto (CUALQUIERA)
        if (tipoLicenciaEJCBox.getSelectedIndex()!=0) {
			    hash.put("S.ID_TIPO_LICENCIA", tipoLicenciaEJCBox.getSelectedPatron());
		    }

        // El patron -1 corresponde a la opcion por defecto (CUALQUIERA)
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
			mostrarMensaje(literales.getString("CConsultaEventos.mensaje3"));
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
                    mostrarMensaje(literales.getString("CConsultaEventos.mensaje2"));
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

		// Annadimos a la tabla el editor CheckBox en la novena columna
		int vColIndexCheck = 8;
		TableColumn col6 = resultadosJTable.getColumnModel().getColumn(vColIndexCheck);
		col6.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
		col6.setCellRenderer(new CheckBoxRenderer());

		/** Redefinimos el tamanno de las celdas */
		/*
		col4.sizeWidthToFit();
		int vColIndex = 0;
		TableColumn col0 = resultadosJTable.getColumnModel().getColumn(vColIndex);
		col0.sizeWidthToFit();

		vColIndex = 1;
		TableColumn col1 = resultadosJTable.getColumnModel().getColumn(vColIndex);
		col1.sizeWidthToFit();

		vColIndex = 2;
		TableColumn col2 = resultadosJTable.getColumnModel().getColumn(vColIndex);
		col2.sizeWidthToFit();

		vColIndex = 3;
		TableColumn col3 = resultadosJTable.getColumnModel().getColumn(vColIndex);
		col3.sizeWidthToFit();
		*/

		/** Datos de la solicitud y del expediente seleccionado */
		CResultadoOperacion ro = COperacionesLicencias.getEventos(hash, ((IMainLicencias)desktop).getTiposLicencia(), literales.getLocale().toString());
		if (ro != null) {
			_vEventos = ro.getVector();
			_vExpedientes = ro.getExpedientes();
			_vSolicitudes = ro.getSolicitudes();

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			CUtilidadesComponentes.clearTable(_resultadosTableModel);
			eventoJTArea.setText("");

			if ((_vEventos == null) || (_vEventos.size() == 0) || (_vExpedientes == null) || (_vExpedientes.size() == 0) || (_vSolicitudes == null) || (_vSolicitudes.size() == 0)) {
				JOptionPane.showMessageDialog(desktop, literales.getString("CConsultaEventos.mensaje1"));
				return;
			}

			for (int i = 0; i < _vEventos.size(); i++) {
				CEvento evento = (CEvento) _vEventos.elementAt(i);
				CExpedienteLicencia expediente = (CExpedienteLicencia) _vExpedientes.elementAt(i);
				CSolicitudLicencia solicitud = (CSolicitudLicencia) _vSolicitudes.elementAt(i);

                try{
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
                        }else if(tipoLicencia == CConstantesLicencias.Actividades || tipoLicencia == CConstantesLicencias.ActividadesNoCalificadas){
                             sTipoObra= ((DomainNode)Estructuras.getListaTiposActividad().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                             sTipoLicencia=((DomainNode)Estructuras.getListaTiposLicenciaActividad().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        }
                        /** Mostramos los datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                        String emplazamiento = CUtilidades.componerCampo(solicitud.getTipoViaAfecta(), solicitud.getNombreViaAfecta(), solicitud.getNumeroViaAfecta());
                        _resultadosTableModel.addRow(new Object[]{
                            expediente.getNumExpediente(),
                            sTipoLicencia,
                            sTipoObra,
                            emplazamiento,
                            ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(expediente.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                            nombreSolicititante,
                            ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(evento.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                            CUtilidades.formatFecha(evento.getFechaEvento()),
                            new Boolean(evento.getRevisado().equalsIgnoreCase("1")),
                            evento.getContent(),
                            new Integer(i)});
                    }
                }catch (Exception e){
                    logger.error("Error al crear la fila",e);
                }
			}

		}

	}//GEN-LAST:event_buscarJButtonActionPerformed

	private void salirJButtonActionPerformed() {//GEN-FIRST:event_salirJButtonActionPerformed
        CConstantesLicencias.helpSetHomeID= "licenciasIntro";
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

    public void mostrarEventoSeleccionado(){
        int row = resultadosJTable.getSelectedRow();

        if (row != -1) {
            CEvento evento= (CEvento) _vEventos.get(((Integer)tableSorted.getValueAt(row, hiddenColumn)).intValue());
            CExpedienteLicencia expediente= (CExpedienteLicencia)_vExpedientes.get(((Integer)tableSorted.getValueAt(row, hiddenColumn)).intValue());
            if (expediente != null){
                numExpedienteJTField.setText(expediente.getNumExpediente());
            }
            eventoJTArea.setText(evento.getContent());
        }else{
            clear();
        }
    }


    public void clear(){
        numExpedienteJTField.setText("");
        eventoJTArea.setText("");
    }

	/*********************************************************************/

	/**
	 * Exit the Application
	 */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		System.exit(0);
	}//GEN-LAST:event_exitForm



    private ComboBoxEstructuras tipoLicenciaEJCBox;
    private ComboBoxEstructuras estadoExpedienteEJCBox;
    private ComboBoxEstructuras tipoObraEJCBox;

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
    private javax.swing.JScrollPane eventoJScrollPane;
    private javax.swing.JTextArea eventoJTArea;
    private javax.swing.JButton fechaDesdeJButton;
    private javax.swing.JTextField fechaDesdeJTField;
    private javax.swing.JButton fechaHastaJButton;
    private javax.swing.JTextField fechaHastaJTField;
    private javax.swing.JLabel fechaJLabel;
    private javax.swing.JButton irExpedienteJButton;
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JScrollPane resultadosJScrollPane;
    private javax.swing.JTable resultadosJTable;
    private javax.swing.JRadioButton revisadosJRadioButton;
    private javax.swing.JButton salirJButton;
    private javax.swing.JRadioButton sinRevisarJRadioButton;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoLicenciaJLabel;
    private javax.swing.JLabel tipoObraJLabel;
    private javax.swing.JRadioButton todosJRadioButton;
    //private javax.swing.JTextField jTextFieldTipoLicencia;
    // End of variables declaration//GEN-END:variables

	public void renombrarComponentes(ResourceBundle literales) {
        this.literales=literales;

		try {

			setTitle(literales.getString("CConsultaEventos.JInternalFrame.title"));
			datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaEventos.datosBusquedaJPanel.TitleBorder")));
			eventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaEventos.eventoJScrollPane.TitleBorder")));
			resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaEventos.resultadosJScrollPane.TitleBorder")));

			tipoLicenciaJLabel.setText(literales.getString("CConsultaEventos.tipoLicenciaJLabel.text"));
            tipoObraJLabel.setText(literales.getString("CConsultaEventos.tipoObraJLabel.text"));
			numExpedienteJLabel.setText(literales.getString("CConsultaEventos.numExpedienteJLabel.text"));
			estadoJLabel.setText(literales.getString("CConsultaEventos.estadoJLabel.text"));
			DNIJLabel.setText(literales.getString("CConsultaEventos.DNIJLabel.text"));
			fechaJLabel.setText(literales.getString("CConsultaEventos.fechaJLabel.text"));

			buscarJButton.setText(literales.getString("CConsultaEventos.buscarJButton.text"));
			salirJButton.setText(literales.getString("CConsultaEventos.salirJButton.text"));
            irExpedienteJButton.setText(literales.getString("CConsultaEventos.irExpedienteJButton.text"));

            buscarJButton.setToolTipText(literales.getString("CConsultaEventos.buscarJButton.text"));
            salirJButton.setToolTipText(literales.getString("CConsultaEventos.salirJButton.text"));
            irExpedienteJButton.setToolTipText(literales.getString("CConsultaEventos.irExpedienteJButton.text"));

            sinRevisarJRadioButton.setText(literales.getString("CConsultaEventos.sinRevisarJRadioButton.text"));
            revisadosJRadioButton.setText(literales.getString("CConsultaEventos.revisadosJRadioButton.text"));
            todosJRadioButton.setText(literales.getString("CConsultaEventos.todosJRadioButton.text"));

            /** Headers tabla eventos */
            TableColumn tableColumn= resultadosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column10"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column1"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column2"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column3"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column4"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column5"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column6"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column7"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column8"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(9);
            tableColumn.setHeaderValue(literales.getString("CConsultaEventos.resultadosJTable.text.column9"));

            datosBusquedaJPanel.setToolTipText(literales.getString("CConsultaEventos.datosBusquedaJPanel.setToolTip.text"));
            fechaDesdeJButton.setToolTipText(literales.getString("CConsultaEventos.fechaDesdeJButton.setToolTip.text"));
            fechaHastaJButton.setToolTipText(literales.getString("CConsultaEventos.fechaHastaJButton.setToolTip.text"));
            DNIJButton.setToolTipText(literales.getString("CConsultaEventos.DNIJButton.setToolTip.text"));
            buscarExpedienteJButton.setToolTipText(literales.getString("CConsultaEventos.buscarExpedienteJButton.setToolTip.text"));
            borrarFechaDesdeJButton.setToolTipText(literales.getString("CConsultaEventos.borrarFechaDesdeJButton.setToolTip.text"));
            borrarFechaHastaJButton.setToolTipText(literales.getString("CConsultaEventos.borrarFechaHastaJButton.setToolTip.text"));

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
                tipoLicenciaEJCBoxActionPerformed();
                }
            });
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
        datosBusquedaJPanel.add(tipoObraEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 51, 310, 20));


        estadoExpedienteEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstados(), null, literales.getLocale().toString(), true);
        estadoExpedienteEJCBox.setSelectedIndex(0);
        datosBusquedaJPanel.add(estadoExpedienteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 93, 310, 20));
    }

    public void setEnabledIrExpedienteJButton(boolean b){
        irExpedienteJButton.setEnabled(b);
    }



}
