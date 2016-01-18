/**
 * CConsultaEventos.java
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JCheckBox;
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
import com.geopista.protocol.licencias.CEvento;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CSolicitudLicencia;


/**
 * @author charo
 */
public class CConsultaEventos extends javax.swing.JInternalFrame implements IMultilingue{

	private Vector _vEventos = null;
	private Vector _vExpedientes = null;
	private Vector _vSolicitudes = null;
    /** Necesario para ir a Modificacion de expdiente */
    private CExpedienteLicencia _expedienteSelected= null;

	private JFrame desktop;

	/**
	 * Modelo para el componente resultadosJTable
	 */
	private DefaultTableModel _resultadosTableModel;
    TableSorted tableSorted= new TableSorted();
    int hiddenColumn= 8;


	Logger logger = Logger.getLogger(CConsultaEventos.class);

	/**
	 * Creates new form CConsultaEventos
	 */
	public CConsultaEventos(JFrame desktop) {
		this.desktop = desktop;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

		initComponents();
		initComboBoxesEstructuras();
		String[] columnNames = {CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column1"),
								CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column2"),
                                CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column3"),
								CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column4"),
								CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column5"),
								//CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column5"),
								CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column6"),
								CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column7"),
								CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column8"),
                                "HIDDEN"};

		CEventosTableModel.setColumnNames(columnNames);
		_resultadosTableModel = new CEventosTableModel();
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
        


		buscarExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		fechaDesdeJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		fechaHastaJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		DNIJButton.setIcon(CUtilidadesComponentes.iconoZoom);


        sinRevisarJRadioButton.setSelected(true);
        revisadosJRadioButton.setSelected(false);
        todosJRadioButton.setSelected(false);

		renombrarComponentes();
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
        //datosBusquedaJPanel.setToolTipText("");
        numExpedienteJLabel.setText("N\u00famero de Expediente:");
        datosBusquedaJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 52, -1, -1));

        datosBusquedaJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 52, 290, 20));

        fechaJLabel.setText("Fecha (desde/hasta):");
        datosBusquedaJPanel.add(fechaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 118, -1, -1));

        fechaDesdeJTField.setEnabled(false);
        datosBusquedaJPanel.add(fechaDesdeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 118, 100, 20));

        fechaHastaJTField.setEnabled(false);
        datosBusquedaJPanel.add(fechaHastaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 118, 100, 20));

        tipoLicenciaJLabel.setText("Tipo Licencia:");
        datosBusquedaJPanel.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 160, -1, -1));

        datosBusquedaJPanel.add(DNIJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 96, 290, 20));

        DNIJLabel.setText("DNI/CIF del Propietario:");
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

        DNIJButton.setIcon(new javax.swing.ImageIcon(""));
        DNIJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        DNIJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        DNIJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DNIJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(DNIJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 96, 20, 20));

        buscarExpedienteJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 52, 20, 20));

        estadoJLabel.setText("Estado actual de Expediente:");
        datosBusquedaJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 74, -1, -1));

        borrarFechaDesdeJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarFechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 118, 20, 20));

        borrarFechaHastaJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarFechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 118, 20, 20));

        sinRevisarJRadioButton.setText("Sin Revisar");
        sinRevisarJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sinRevisarJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(sinRevisarJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 170, -1));

        revisadosJRadioButton.setText("Revisados");
        revisadosJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revisadosJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(revisadosJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, 190, -1));

        todosJRadioButton.setText("Todos");
        todosJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todosJRadioButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(todosJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 160, 160, -1));

        templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 980, 200));

        resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder("Resultado de la B\u00fasqueda"));
        resultadosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        resultadosJTable.setModel(new javax.swing.table.DefaultTableModel(
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
        resultadosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultadosJTable.setFocusCycleRoot(true);
        resultadosJTable.setSurrendersFocusOnKeystroke(true);
        resultadosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                resultadosJTableFocusGained();
            }
        });
        resultadosJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                resultadosJTableKeyTyped();
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                resultadosJTableKeyPressed();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                resultadosJTableKeyReleased();
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

        resultadosJScrollPane.setViewportView(resultadosJTable);

        templateJPanel.add(resultadosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 980, 270));

        salirJButton.setText("Cancelar");
        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirJButtonActionPerformed();
            }
        });

        templateJPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 600, 90, -1));

        irExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irExpedienteJButtonActionPerformed();
            }
        });

        templateJPanel.add(irExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 600, 130, -1));


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

        templateJPanel.add(eventoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 980, 100));

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents


    private void resultadosJTableKeyReleased() {//GEN-FIRST:event_resultadosJTableKeyReleased
        eventoJTArea.setText("");
        mostrarDescripcionEventoSeleccionado();

    }//GEN-LAST:event_resultadosJTableKeyReleased

    private void resultadosJTableKeyPressed() {//GEN-FIRST:event_resultadosJTableKeyPressed
       eventoJTArea.setText("");
        mostrarDescripcionEventoSeleccionado();

    }//GEN-LAST:event_resultadosJTableKeyPressed

    private void resultadosJTableKeyTyped() {//GEN-FIRST:event_resultadosJTableKeyTyped
        eventoJTArea.setText("");
        mostrarDescripcionEventoSeleccionado();
   }//GEN-LAST:event_resultadosJTableKeyTyped


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
        mainOcupaciones.mostrarJInternalFrame(new com.geopista.app.ocupaciones.CModificacionLicencias((JFrame)this.desktop, _expedienteSelected.getNumExpediente(), false));

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }


    private void resultadosJTableFocusGained() {//GEN-FIRST:event_resultadosJTableFocusGained
        eventoJTArea.setText("");
        mostrarDescripcionEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void resultadosJTableMouseDragged() {//GEN-FIRST:event_resultadosJTableMouseDragged
        eventoJTArea.setText("");
        mostrarDescripcionEventoSeleccionado();
   }//GEN-LAST:event_resultadosJTableMouseDragged

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
         CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);        
    }//GEN-LAST:event_formInternalFrameClosed

	private void resultadosJTableMouseClicked() {//GEN-FIRST:event_resultadosJTableMouseClicked
	     eventoJTArea.setText("");
        mostrarDescripcionEventoSeleccionado();
	}//GEN-LAST:event_resultadosJTableMouseClicked

    private void mostrarDescripcionEventoSeleccionado(){
        int row = resultadosJTable.getSelectedRow();
        if (row != -1) {
            CEvento evento= (CEvento) _vEventos.get(((Integer)tableSorted.getValueAt(row, hiddenColumn)).intValue());
            eventoJTArea.setText(evento.getContent());

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

		// Annadimos a la tabla el editor CheckBox en la octava columna
		int vColIndexCheck = 6;
		TableColumn col6 = resultadosJTable.getColumnModel().getColumn(vColIndexCheck);
		col6.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
		col6.setCellRenderer(new CheckBoxRenderer());

		/** Redefinimos el tamanno de las celdas fecha */
        TableColumn column = resultadosJTable.getColumnModel().getColumn(5);
        CUtilidades.resizeColumn(column, 70);
        column = resultadosJTable.getColumnModel().getColumn(6);
        CUtilidades.resizeColumn(column, 70);

        /** Datos de la solicitud y del expediente seleccionado */
		CResultadoOperacion ro = COperacionesLicencias.getEventos(hash, null, CMainOcupaciones.literales.getLocale().toString());
		if (ro != null) {
			_vEventos = ro.getVector();
			_vExpedientes = ro.getExpedientes();
			_vSolicitudes = ro.getSolicitudes();

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			CUtilidadesComponentes.clearTable(_resultadosTableModel);
			eventoJTArea.setText("");

			if ((_vEventos == null) || (_vEventos.size() == 0) || (_vExpedientes == null) || (_vExpedientes.size() == 0) || (_vSolicitudes == null) || (_vSolicitudes.size() == 0)) {
				JOptionPane.showMessageDialog(desktop, CMainOcupaciones.literales.getString("Eventos.mensaje1"));
				return;
			}

			for (int i = 0; i < _vEventos.size(); i++) {
				CEvento evento = (CEvento) _vEventos.elementAt(i);
				CExpedienteLicencia expediente = (CExpedienteLicencia) _vExpedientes.elementAt(i);
				CSolicitudLicencia solicitud = (CSolicitudLicencia) _vSolicitudes.elementAt(i);

				//logger.info("evento.getIdEvento()=" + evento.getIdEvento() + " Revisado="+evento.getRevisado());
				/*
				JCheckBox check= (JCheckBox) ((CheckBoxTableEditor) resultadosJTable.getCellEditor(i, 5)).getComponent();
				if (evento.getRevisado().equalsIgnoreCase("1")) check.setSelected(true);
				else check.setSelected(false);
				*/

				CheckBoxRenderer renderCheck = (CheckBoxRenderer) resultadosJTable.getCellRenderer(i, vColIndexCheck);
				if (evento.getRevisado().equalsIgnoreCase("1"))
					renderCheck.setSelected(true);
				else
					renderCheck.setSelected(false);

                String nombreSolicitante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());
                /** intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                String emplazamiento= CUtilidades.getEmplazamiento(solicitud);
				_resultadosTableModel.addRow(new Object[]{
					((DomainNode) Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(solicitud.getDatosOcupacion().getTipoOcupacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
					expediente.getNumExpediente(),
                    emplazamiento,
					((DomainNode) Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(expediente.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
					//solicitud.getPropietario().getDniCif(),
                    nombreSolicitante,
                    //new Long(evento.getIdEvento()).toString(),
                    CUtilidades.formatFecha(evento.getFechaEvento()),
                    new Boolean(renderCheck.isSelected()),
                    evento.getContent(),
                    new Integer(i)});
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
    private javax.swing.JScrollPane eventoJScrollPane;
    private javax.swing.JTextArea eventoJTArea;
    private javax.swing.JButton fechaDesdeJButton;
    private javax.swing.JTextField fechaDesdeJTField;
    private javax.swing.JButton fechaHastaJButton;
    private javax.swing.JTextField fechaHastaJTField;
    private javax.swing.JLabel fechaJLabel;
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
    private javax.swing.JRadioButton todosJRadioButton;
    private javax.swing.JButton irExpedienteJButton;
    // End of variables declaration//GEN-END:variables

	public void renombrarComponentes() {

		try {
			setTitle(CMainOcupaciones.literales.getString("CConsultaEventos.JInternalFrame.title"));
			datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaEventos.datosBusquedaJPanel.TitleBorder")));
			eventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaEventos.eventoJScrollPane.TitleBorder")));
			resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJScrollPane.TitleBorder")));

			tipoLicenciaJLabel.setText(CMainOcupaciones.literales.getString("CConsultaEventos.tipoLicenciaJLabel.text"));
			numExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CConsultaEventos.numExpedienteJLabel.text"));
			estadoJLabel.setText(CMainOcupaciones.literales.getString("CConsultaEventos.estadoJLabel.text"));
			DNIJLabel.setText(CMainOcupaciones.literales.getString("CConsultaEventos.DNIJLabel.text"));
			fechaJLabel.setText(CMainOcupaciones.literales.getString("CConsultaEventos.fechaJLabel.text"));

			buscarJButton.setText(CMainOcupaciones.literales.getString("CConsultaEventos.buscarJButton.text"));
			salirJButton.setText(CMainOcupaciones.literales.getString("CConsultaEventos.salirJButton.text"));

            borrarFechaHastaJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
            borrarFechaDesdeJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);

            sinRevisarJRadioButton.setText(CMainOcupaciones.literales.getString("Eventos.sinRevisarJRadioButton.text"));
            revisadosJRadioButton.setText(CMainOcupaciones.literales.getString("Eventos.revisadosJRadioButton.text"));
            todosJRadioButton.setText(CMainOcupaciones.literales.getString("Eventos.todosJRadioButton.text"));

            irExpedienteJButton.setText(CMainOcupaciones.literales.getString("CConsultaEventos.irExpedienteJButton.text"));

            /** Headers tabla eventos */
            TableColumn tableColumn= resultadosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column1"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column2"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column3"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column4"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column5"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column6"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column7"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaEventos.resultadosJTable.text.column8"));

            buscarJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaEventos.buscarJButton.text"));
            salirJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaEventos.salirJButton.text"));
            irExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaEventos.irExpedienteJButton.text"));

            fechaDesdeJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaEventos.fechaDesdeJButton.setToolTip.text"));
            fechaHastaJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaEventos.fechaHastaJButton.setToolTip.text"));
            DNIJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaEventos.DNIJButton.setToolTip.text"));
            buscarExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaEventos.buscarExpedienteJButton.setToolTip.text"));
            borrarFechaDesdeJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaEventos.borrarFechaDesdeJButton.setToolTip.text"));
            borrarFechaHastaJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaEventos.borrarFechaHastaJButton.setToolTip.text"));


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


	public void initComboBoxesEstructuras() {
		while (!Estructuras.isCargada()) {
			if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}

		/** Inicializamos los comboBox que llevan estructuras */
		tipoLicenciaEJCBox = new ComboBoxEstructuras(Estructuras.getListaTipoOcupacion(), null, CMainOcupaciones.currentLocale.toString(), true);
        /** por defecto selected TODOS */
        tipoLicenciaEJCBox.setSelectedIndex(0);
		datosBusquedaJPanel.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 310, 20));

		estadoExpedienteEJCBox = new ComboBoxEstructuras(Estructuras.getListaEstadosOcupacion(), null, CMainOcupaciones.currentLocale.toString(), true);
        /** por defecto selected TODOS */
        estadoExpedienteEJCBox.setSelectedIndex(0);
		datosBusquedaJPanel.add(estadoExpedienteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 74, 310, 20));
	}

    public void setEnabledIrExpedienteJButton(boolean b){
        irExpedienteJButton.setEnabled(b);
    }



}
