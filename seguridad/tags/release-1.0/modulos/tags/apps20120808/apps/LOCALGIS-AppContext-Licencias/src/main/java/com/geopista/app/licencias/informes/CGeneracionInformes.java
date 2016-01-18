package com.geopista.app.licencias.informes;
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

import java.awt.Cursor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import reso.jumpPlugIn.printLayoutPlugIn.DownloadFromServer;

import com.geopista.app.AppContext;
import com.geopista.app.licencias.CConstantesLicencias;
import com.geopista.app.licencias.CMainLicencias;
import com.geopista.app.licencias.CUtilidades;
import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.app.licencias.IMainLicencias;
import com.geopista.app.licencias.IMultilingue;
import com.geopista.app.licencias.actividad.MainActividad;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CInformesTableModel;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.app.reports.GenerarInformeExterno;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.CConstantesPaths;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPlantilla;
import com.geopista.protocol.licencias.CSolicitudLicencia;

/**
 *
 * @author  charo
 */
public class CGeneracionInformes extends javax.swing.JInternalFrame implements IMultilingue {

    private Vector _vSolicitudes= null;
    private Vector _vExpedientes= null;

    private Hashtable _hWhere= null;

    private JFrame desktop;
    private String path = "";
    /** Modelo para el componente resultadosJTable */
    CInformesTableModel _resultadosTableModel = new CInformesTableModel();

    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private String reportFile = "";
    Logger logger = Logger.getLogger(CGeneracionInformes.class);
    private ResourceBundle literales;

    /*
     * Almacenamos el listado con el num_expediente que utilizaremos en el ireport:
     * */
    private String lista_expedientes = "";
    
    /** Creates new form CGeneracionInformes */
    public CGeneracionInformes(JFrame desktop, ResourceBundle literales) {
        this.desktop= desktop;
        this.literales=literales;

        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);
        initComponents();
        initComboBoxesEstructuras();
        logger.info("Intentamos cargar las plantillas");
        cargarPlantillas();
        logger.info("Plantillas cargadas");

        buscarExpedienteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        fechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        fechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);

        CInformesTableModel.setColumnNames( new String[]{literales.getString("CGeneracionInformesForm.resultadosJTable.text.column1"),
                                                           literales.getString("CGeneracionInformesForm.resultadosJTable.text.column6"),
                                                           literales.getString("CGeneracionInformesForm.resultadosJTable.text.column2"),
                                                           literales.getString("CGeneracionInformesForm.resultadosJTable.text.column3"),
                                                           literales.getString("CGeneracionInformesForm.resultadosJTable.text.column7"),
                                                           literales.getString("CGeneracionInformesForm.resultadosJTable.text.column4"),
                                                           literales.getString("CGeneracionInformesForm.resultadosJTable.text.column5")});

        _resultadosTableModel = new CInformesTableModel();
        com.geopista.app.utilidades.TableSorted tableSorted= new com.geopista.app.utilidades.TableSorted(_resultadosTableModel);
        tableSorted.setTableHeader(resultadosJTable.getTableHeader());
        resultadosJTable.setModel(tableSorted);

		renombrarComponentes(literales);
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
        titularJLabel = new javax.swing.JLabel();
        DNITitularJTField = new javax.swing.JTextField();
        fechaAperturaJLabel = new javax.swing.JLabel();
        fechaDesdeJTField = new javax.swing.JTextField();
        fechaHastaJTField = new javax.swing.JTextField();
        tipoLicenciaJLabel = new javax.swing.JLabel();
        buscarJButton = new javax.swing.JButton();
        estadoJLabel = new javax.swing.JLabel();
        buscarExpedienteJButton = new javax.swing.JButton();
        buscarDNIJButton = new javax.swing.JButton();
        fechaDesdeJButton = new javax.swing.JButton();
        fechaHastaJButton = new javax.swing.JButton();
        resultadosJScrollPane = new javax.swing.JScrollPane();
        resultadosJTable = new javax.swing.JTable();
        datosInformeJPanel = new javax.swing.JPanel();
        plantillaJLabel = new javax.swing.JLabel();
        //formatoJLabel = new javax.swing.JLabel();
        plantillaJCBox = new javax.swing.JComboBox();
        /*
        nombreInformeJLabel = new javax.swing.JLabel();
        nombreInformeJTField = new javax.swing.JTextField();
        */
        aceptarJButton = new javax.swing.JButton();
        salirJButton = new javax.swing.JButton();
        borrarFechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();
        tipoObraJLabel = new javax.swing.JLabel();


        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        setClosable(true);
        setTitle("Generaci\u00f3n de Informes");
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

        datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder("Datos de B\u00fasqueda de Licencia"));
        numExpedienteJLabel.setText("N\u00famero de Expediente:");
        datosBusquedaJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, -1, -1));

        datosBusquedaJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 72, 290, 20));

        titularJLabel.setText("DNI/CIF del Titular:");
        datosBusquedaJPanel.add(titularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, -1, -1));

        datosBusquedaJPanel.add(DNITitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 114, 290, 20));

        fechaAperturaJLabel.setText("Fecha Apertura (desde/hasta):");
        datosBusquedaJPanel.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, -1, -1));

        fechaDesdeJTField.setEnabled(false);
        datosBusquedaJPanel.add(fechaDesdeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 135, 100, 20));

        fechaHastaJTField.setEnabled(false);
        datosBusquedaJPanel.add(fechaHastaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 135, 100, 20));

        tipoLicenciaJLabel.setText("Tipo Licencia:");
        datosBusquedaJPanel.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        tipoObraJLabel.setText("Tipo Obra:");
        datosBusquedaJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, -1, -1));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _hWhere= buscarJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 156, -1, -1));

        estadoJLabel.setText("Estado:");
        datosBusquedaJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, -1, -1));

        buscarExpedienteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 72, 20, 20));

        buscarDNIJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        buscarDNIJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(buscarDNIJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 114, 20, 20));

        fechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        fechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaDesdeJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 135, 20, 20));

        fechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        fechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaHastaJButtonActionPerformed();
            }
        });

        datosBusquedaJPanel.add(fechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 135, 20, 20));

        borrarFechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
        borrarFechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed(evt);
            }
        });

        datosBusquedaJPanel.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 135, 20, 20));

        borrarFechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
        borrarFechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed(evt);
            }
        });

        datosBusquedaJPanel.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 135, 20, 20));


        //templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 830, 190));
        templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 1000, 190));

        resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder("Resultado de la B\u00fasqueda"));
        resultadosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        resultadosJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tipo Licencia", "Num. Expediente", "Estado", "DNI/CIF Titular", "Fecha Apertura"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        resultadosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultadosJTable.setFocusCycleRoot(true);
        resultadosJTable.setSurrendersFocusOnKeystroke(true);
        resultadosJTable.getTableHeader().setReorderingAllowed(false);
        resultadosJScrollPane.setViewportView(resultadosJTable);

        //templateJPanel.add(resultadosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 200, 830, 260));
        templateJPanel.add(resultadosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 200, 1000, 260));

        datosInformeJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosInformeJPanel.setBorder(new javax.swing.border.TitledBorder("Datos del Informe"));
        plantillaJLabel.setText("Plantilla para el Informe:");
        datosInformeJPanel.add(plantillaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        //formatoJLabel.setText("Formato de Salida:");
       //datosInformeJPanel.add(formatoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        datosInformeJPanel.add(plantillaJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 310, -1));

        /*
        nombreInformeJLabel.setText("Nombre del Informe:");
        datosInformeJPanel.add(nombreInformeJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        datosInformeJPanel.add(nombreInformeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 310, 20));
        */

        //templateJPanel.add(datosInformeJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 460, 830, 120));
        templateJPanel.add(datosInformeJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 460, 1000, 120));
        datosInformeJPanel.getAccessibleContext().setAccessibleName("Datos del informe");

        aceptarJButton.setText("Aceptar");
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });

        //templateJPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 600, 90, -1));
        templateJPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 600, 160, -1));

        salirJButton.setText("Cancelar");
        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirJButtonActionPerformed();
            }
        });

        templateJPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 600, 90, -1));

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane);

    }//GEN-END:initComponents

    private void borrarFechaHastaJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarFechaHastaJButtonActionPerformed
        // TODO add your handling code here:
        fechaHastaJTField.setText("");
    }//GEN-LAST:event_borrarFechaHastaJButtonActionPerformed

    private void borrarFechaDesdeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarFechaDesdeJButtonActionPerformed
        // TODO add your handling code here:
        fechaDesdeJTField.setText("");
    }//GEN-LAST:event_borrarFechaDesdeJButtonActionPerformed


    /** Los estados no pueden redefinirse como dominio, puesto que necesitamos el valor del campo step */
    public void initComboBoxesEstructuras(){
        while (!Estructuras.isCargada())
        {
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }

        /** Inicializamos los comboBox que llevan estructuras */
        if (desktop instanceof CMainLicencias){
            tipoLicenciaEJCBox= new ComboBoxEstructuras(Estructuras.getListaLicencias(), null, literales.getLocale().toString(), true);
            tipoLicenciaEJCBox.setSelectedIndex(0);
            datosBusquedaJPanel.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 310, 20));
            tipoLicenciaEJCBox.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoLicenciaEJCBoxActionPerformed();
                }
            });
            tipoObraEJCBox= new ComboBoxEstructuras(new ListaEstructuras(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setEnabled(false);
        }else if (desktop instanceof MainActividad){
            tipoLicenciaEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposLicenciaActividad(), null, literales.getLocale().toString(), true);
            tipoLicenciaEJCBox.setSelectedIndex(0);
            datosBusquedaJPanel.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 310, 20));
            tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposActividad(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setEnabled(true);
        }
        tipoObraEJCBox.setSelectedIndex(0);
        datosBusquedaJPanel.add(tipoObraEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 51, 310, 20));

        estadoEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstados(), null, literales.getLocale().toString(), true);
        estadoEJCBox.setSelectedIndex(0);
        datosBusquedaJPanel.add(estadoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 93, 310, 20));

       // formatoSalidaEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormatosSalida(), null,
          //      literales.getLocale().toString(), false);
       // datosInformeJPanel.add(formatoSalidaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 310, -1));
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



    private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
        CConstantesLicencias.helpSetHomeID= "geopistaIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);

    }//GEN-LAST:event_formInternalFrameClosed

    private void fechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_fechaDesdeJButtonActionPerformed
        CUtilidadesComponentes.showCalendarDialog(desktop);
        if ((CConstantesLicencias.calendarValue != null) &&(!CConstantesLicencias.calendarValue.trim().equals(""))) {
                fechaDesdeJTField.setText(CConstantesLicencias.calendarValue);
        }
    }//GEN-LAST:event_fechaDesdeJButtonActionPerformed

    private void fechaHastaJButtonActionPerformed() {//GEN-FIRST:event_fechaHastaJButtonActionPerformed
        CUtilidadesComponentes.showCalendarDialog(desktop);
        if ((CConstantesLicencias.calendarValue != null) &&(!CConstantesLicencias.calendarValue.trim().equals(""))) {
                fechaHastaJTField.setText(CConstantesLicencias.calendarValue);
        }
    }//GEN-LAST:event_fechaHastaJButtonActionPerformed

    private void buscarDNIJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIJButtonActionPerformed
        CUtilidadesComponentes.showPersonaDialog(desktop,literales);

        if (CConstantesLicencias.persona != null) {
            DNITitularJTField.setText(CConstantesLicencias.persona.getDniCif());
        }
    }//GEN-LAST:event_buscarDNIJButtonActionPerformed

    private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed
        CUtilidadesComponentes.showSearchDialog(desktop, literales);
        numExpedienteJTField.setText(CConstantesLicencias.searchValue);
   }//GEN-LAST:event_buscarExpedienteJButtonActionPerformed

    private void salirJButtonActionPerformed() {//GEN-FIRST:event_salirJButtonActionPerformed
        CConstantesLicencias.helpSetHomeID= "licenciasIntro";
        this.dispose();
        /** Borramos el fichero de datos csv */
        /** ya no es necesario, porque la fuente de datos es la BBDD */
        //borrarFicheroCSV();
    }//GEN-LAST:event_salirJButtonActionPerformed
   
	private void aceptarJButtonActionPerformed() {//GEN-FIRST:event_aceptarJButtonActionPerformed
		try{
			if ((_vExpedientes != null) && (_vExpedientes.size() > 0) && (_vSolicitudes != null) && (_vSolicitudes.size() > 0)){
				// Generamos el informe con la informacion

				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				
				String report = ((String)plantillaJCBox.getSelectedItem());
				

				if (report != null && !report.equals("")){          
					try {
						reportFile = path + report;
						Map parametros = obtenerParametros();
						
						// Le pasamos el nombre del informe (ruta absoluta): 
						GenerarInformeExterno giep=new GenerarInformeExterno(reportFile, parametros);
					}catch (Exception e) {

						mostrarMensaje(literales.getString("CGeneracionInformesForm.mensaje1"));
						e.printStackTrace();
						logger.error("exception thrown: " + e);
					}
				}
			}else{
				mostrarMensaje(literales.getString("CGeneracionInformesForm.mensaje3"));
			}
		}catch(Exception e){
			mostrarMensaje(literales.getString("CGeneracionInformesForm.mensaje4"));

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

    private Hashtable buscarJButtonActionPerformed() {//GEN-FIRST:event_buscarJButtonActionPerformed

        Hashtable hash=new Hashtable();
        hash.put("EXPEDIENTE_LICENCIA.NUM_EXPEDIENTE",numExpedienteJTField.getText());
        if (tipoLicenciaEJCBox.getSelectedIndex()!=0){
            hash.put("SOLICITUD_LICENCIA.ID_TIPO_LICENCIA", tipoLicenciaEJCBox.getSelectedPatron());
        }
        if (tipoObraEJCBox.getSelectedIndex()!=0){
            hash.put("SOLICITUD_LICENCIA.ID_TIPO_OBRA", tipoObraEJCBox.getSelectedPatron());
        }
        if (estadoEJCBox.getSelectedIndex()!=0){
            hash.put("ESTADO.ID_ESTADO", estadoEJCBox.getSelectedPatron());
        }
        hash.put("PERSONA_JURIDICO_FISICA.DNI_CIF",DNITitularJTField.getText());
        /** comprobamos que la fecha tiene formato valido */
         if ((CUtilidades.parseFechaStringToString(fechaDesdeJTField.getText()) == null) || (CUtilidades.parseFechaStringToString(fechaHastaJTField.getText()) == null)){
             mostrarMensaje("Formato de Fecha Erróneo.");
             return null;
         }else{
             //Between entre fechas
             Date fechaDesde=CUtilidades.parseFechaStringToDate(fechaDesdeJTField.getText().trim());
             if (fechaDesdeJTField.getText().trim().equals("")){
                 fechaDesde=new Date(1);
             }
             Date fechaHasta=CUtilidades.parseFechaStringToDate(fechaHastaJTField.getText().trim());
             if (fechaHastaJTField.getText().trim().equals("")){
                 fechaHasta=new Date();
             }

             if ((fechaDesde!=null)&&(fechaHasta!=null)) {
                 String fechaDesdeFormatted=new SimpleDateFormat("yyyy-MM-dd").format(fechaDesde);
                 long millis= fechaHasta.getTime();
                 /** annadimos un dia */
                 millis+= 24*60*60*1000;
                 fechaHasta= new Date(millis);
                 String fechaHastaFormatted=new SimpleDateFormat("yyyy-MM-dd").format(fechaHasta);

                 hash.put("BETWEEN*EXPEDIENTE_LICENCIA.FECHA_APERTURA", fechaDesdeFormatted+"*"+fechaHastaFormatted);
             }

         }

       this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

       // Resultado de la busqueda
       CResultadoOperacion ro= COperacionesLicencias.getSolicitudesExpedientesInforme(hash, ((IMainLicencias)desktop).getTiposLicencia());
       if (ro != null){
            _vSolicitudes= ro.getSolicitudes();
            _vExpedientes= ro.getExpedientes();

            CUtilidadesComponentes.clearTable(_resultadosTableModel);
            if ((_vSolicitudes == null) || (_vExpedientes == null) || (_vSolicitudes.size() == 0) || (_vExpedientes.size() == 0)) {
                JOptionPane.showMessageDialog(desktop, "Búsqueda realizada sin resultados.");
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return null;
            }
            lista_expedientes = "";
            for (int i = 0; i < _vSolicitudes.size(); i++) {
                CSolicitudLicencia solicitud= (CSolicitudLicencia)_vSolicitudes.elementAt(i);
                CExpedienteLicencia expediente= (CExpedienteLicencia) _vExpedientes.elementAt(i);

                logger.info("solicitud.getIdSolicitud()=" + solicitud.getIdSolicitud() + " expediente.getNumExpediente="+expediente.getNumExpediente());
                
                /*
                 * Rellenamos la lista con los numeros de expediente:*/
                lista_expedientes += "'"+expediente.getNumExpediente()+"'";
                if (i < _vSolicitudes.size()-1){
                	lista_expedientes += ", ";
                }
                String sTipoLicencia="";
                String sTipoObra= "";
                try{
                    if (desktop instanceof CMainLicencias){
                        sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        if (solicitud.getTipoLicencia().getIdTipolicencia() == CConstantesLicencias.ObraMayor){
                            sTipoObra= ((DomainNode)Estructuras.getListaTiposObra().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                        }else if (solicitud.getTipoLicencia().getIdTipolicencia() == CConstantesLicencias.ObraMenor){
                            sTipoObra= ((DomainNode)Estructuras.getListaTiposObraMenor().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                        }
                    }else if (desktop instanceof MainActividad){
                        sTipoLicencia=((DomainNode)Estructuras.getListaTiposLicenciaActividad().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
                        sTipoObra= ((DomainNode)Estructuras.getListaTiposActividad().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
                    }
                }catch(Exception e){logger.error("ERROR al cargar el tipo de Obra y/o Licencia "+e.toString());}

                String nombreSolicititante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());
                /** intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                String emplazamiento = CUtilidades.componerCampo(solicitud.getTipoViaAfecta(), solicitud.getNombreViaAfecta(), solicitud.getNumeroViaAfecta());
                //String emplazamiento = CUtilidades.getEmplazamiento(solicitud, literales);

                String descEstadoExpediente= ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(expediente.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString());
                _resultadosTableModel.addRow(new Object[]{sTipoLicencia,
                                                          sTipoObra,
                                                          expediente.getNumExpediente(),
                                                          descEstadoExpediente,
                                                          emplazamiento,
                                                          //solicitud.getPropietario().getDniCif(),
                                                          nombreSolicititante,
                                                          CUtilidades.formatFecha(expediente.getFechaApertura())});
            }
       }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return hash;

    }//GEN-LAST:event_buscarJButtonActionPerformed




     /*******************************************************************/
     /*                         Metodos propios                         */
    /********************************************************************/

    public void cargarPlantillas(){
        try {
        	String path = desktop instanceof CMainLicencias?ConstantesLocalGISPlantillas.PATH_LICENCIAS:ConstantesLocalGISPlantillas.PATH_ACTIVIDAD;
        	AppContext.getApplicationContext().getBlackboard().put(AppContext.idAppType, path);  
			DownloadFromServer dfs = new DownloadFromServer();
			dfs.getServerPlantillas(ConstantesLocalGISPlantillas.EXTENSION_JRXML);	
        	
			Vector plantillas = getPlantillas();
            if (plantillas != null) {
                Enumeration enumeration = plantillas.elements();
                while (enumeration.hasMoreElements()) {
                    CPlantilla plantilla = (CPlantilla) enumeration.nextElement();
                    logger.debug("plantilla.getFileName=" + plantilla.getFileName());
                    plantillaJCBox.addItem(plantilla.getFileName());
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }

	public Vector getPlantillas() {
		try {
			Vector vPlantillas = new Vector();

			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (name.endsWith(".jrxml"));
				}
			};
			
			path = AppContext.getApplicationContext().getUserPreference(
					AppContext.PREFERENCES_DATA_PATH_KEY,
					AppContext.DEFAULT_DATA_PATH, true);
			String idAppType = (String)AppContext.getApplicationContext().getBlackboard().get(AppContext.idAppType);
			if (!idAppType.equals("")){
				path = path+idAppType+File.separator;
			}
			
            File dir = new File(path);
			if (dir.isDirectory()) {
				File[] children = dir.listFiles(filter);
				if (children == null) {
					// Either dir does not exist or is not a directory
				} else {
					for (int i = 0; i < children.length; i++) {
						// Get filename of file or directory
						File file = children[i];
                        // FRAN
                        String fname = file.getName();
                        long ftam = file.length();
                        FileInputStream fis = new FileInputStream(file);
                        byte data[] = new byte[(int)ftam];
                        fis.read(data);
                        String sdata = new String(data);
                      //  String sdef = sdata.replaceAll(CConstantesComando.PATRON_SUSTITUIR_BBDD, bdContext);
                        BufferedWriter bw = new BufferedWriter(new FileWriter(path+fname));
                      //  bw.write(sdef);
                        bw.write(sdata);
                        //FileOutputStream fos = new FileOutputStream(_path+dname);
                        //fos.write(sdef.getBytes());
                        //fos.flush();
                        //fos.close();
                        bw.flush();
                        bw.close();
                        //int pos = sdata.indexOf();
                        // FRAN
						CPlantilla plantilla = new CPlantilla(fname);//file.getName());
						vPlantillas.addElement(plantilla);
					}
				}
			}
			return vPlantillas;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return new Vector();
		}
	}

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(datosBusquedaJPanel, mensaje);
    }
    
    /*********************************************************************/

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    


    private ComboBoxEstructuras tipoLicenciaEJCBox;
    private ComboBoxEstructuras estadoEJCBox;
    // private ComboBoxEstructuras formatoSalidaEJCBox;
    private ComboBoxEstructuras tipoObraEJCBox;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DNITitularJTField;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton buscarDNIJButton;
    private javax.swing.JButton buscarExpedienteJButton;
    private javax.swing.JButton buscarJButton;
    private javax.swing.JPanel datosBusquedaJPanel;
    private javax.swing.JPanel datosInformeJPanel;
    private javax.swing.JLabel estadoJLabel;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JButton fechaDesdeJButton;
    private javax.swing.JTextField fechaDesdeJTField;
    private javax.swing.JButton fechaHastaJButton;
    private javax.swing.JTextField fechaHastaJTField;
    // private javax.swing.JLabel formatoJLabel;
    /*
    private javax.swing.JLabel nombreInformeJLabel;
    private javax.swing.JTextField nombreInformeJTField;
    */
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JComboBox plantillaJCBox;
    private javax.swing.JLabel plantillaJLabel;
    private javax.swing.JScrollPane resultadosJScrollPane;
    private javax.swing.JTable resultadosJTable;
    private javax.swing.JButton salirJButton;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoLicenciaJLabel;
    private javax.swing.JLabel titularJLabel;
    private javax.swing.JButton borrarFechaDesdeJButton;
    private javax.swing.JButton borrarFechaHastaJButton;
    private javax.swing.JLabel tipoObraJLabel;
    //private javax.swing.JTextField jTextFieldTipoLicencia;
    // End of variables declaration//GEN-END:variables


	public void renombrarComponentes(ResourceBundle literales) {
        this.literales=literales;
		try {
			setTitle(literales.getString("CGeneracionInformesForm.JInternalFrame.title"));
			datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CGeneracionInformesForm.datosBusquedaJPanel.TitleBorder")));
			datosInformeJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CGeneracionInformesForm.datosInformeJPanel.TitleBorder")));
            resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CGeneracionInformesForm.resultadosJScrollPane.TitleBorder")));

			tipoLicenciaJLabel.setText(literales.getString("CGeneracionInformesForm.tipoLicenciaJLabel.text"));
            tipoObraJLabel.setText(literales.getString("CGeneracionInformesForm.tipoObraJLabel.text"));
			numExpedienteJLabel.setText(literales.getString("CGeneracionInformesForm.numExpedienteJLabel.text"));
			estadoJLabel.setText(literales.getString("CGeneracionInformesForm.estadoJLabel.text"));
			titularJLabel.setText(literales.getString("CGeneracionInformesForm.titularJLabel.text"));
			fechaAperturaJLabel.setText(literales.getString("CGeneracionInformesForm.fechaAperturaJLabel.text"));

			buscarJButton.setText(literales.getString("CGeneracionInformesForm.buscarJButton.text"));
			aceptarJButton.setText(literales.getString("CGeneracionInformesForm.aceptarJButton.text"));
			salirJButton.setText(literales.getString("CGeneracionInformesForm.salirJButton.text"));

            buscarJButton.setToolTipText(literales.getString("CGeneracionInformesForm.buscarJButton.text"));
            aceptarJButton.setToolTipText(literales.getString("CGeneracionInformesForm.aceptarJButton.toolTipText"));
            salirJButton.setToolTipText(literales.getString("CGeneracionInformesForm.salirJButton.text"));

			//nombreInformeJLabel.setText(literales.getString("CGeneracionInformesForm.nombreInformeJLabel.text"));
			plantillaJLabel.setText(literales.getString("CGeneracionInformesForm.plantillaJLabel.text"));
			//formatoJLabel.setText(literales.getString("CGeneracionInformesForm.formatoJLabel.text"));

            /** Headers tabla resultados */
            TableColumn tableColumn= resultadosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CGeneracionInformesForm.resultadosJTable.text.column1"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CGeneracionInformesForm.resultadosJTable.text.column6"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CGeneracionInformesForm.resultadosJTable.text.column2"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CGeneracionInformesForm.resultadosJTable.text.column3"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CGeneracionInformesForm.resultadosJTable.text.column7"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CGeneracionInformesForm.resultadosJTable.text.column4"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(literales.getString("CGeneracionInformesForm.resultadosJTable.text.column5"));

            datosBusquedaJPanel.setToolTipText(literales.getString("CGeneracionInformesForm.datosBusquedaJPanel.setToolTip.text"));
            buscarExpedienteJButton.setToolTipText(literales.getString("CGeneracionInformesForm.buscarExpedienteJButton.setToolTip.text"));
            buscarDNIJButton.setToolTipText(literales.getString("CGeneracionInformesForm.buscarDNIJButton.setToolTip.text"));
            fechaDesdeJButton.setToolTipText(literales.getString("CGeneracionInformesForm.fechaDesdeJButton.setToolTip.text"));
            fechaHastaJButton.setToolTipText(literales.getString("CGeneracionInformesForm.fechaHastaJButton.setToolTip.text"));
            borrarFechaDesdeJButton.setToolTipText(literales.getString("CGeneracionInformesForm.borrarFechaDesdeJButton.setToolTip.text"));
            borrarFechaHastaJButton.setToolTipText(literales.getString("CGeneracionInformesForm.borrarFechaHastaJButton.setToolTip.text"));
            datosInformeJPanel.setToolTipText(literales.getString("CGeneracionInformesForm.datosInformeJPanel.setToolTip.text"));
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

    private Map obtenerParametros(){
    
		Map parametros = new HashMap();
		parametros.put("lista_expedientes", lista_expedientes);
		parametros.put("locale", aplicacion.getI18NResource().getLocale().toString());
		parametros.put("id_municipio", aplicacion.getIdMunicipio());
		return parametros;
    }

}
