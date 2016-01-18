/**
 * CGeneracionInformes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * CGeneracionInformes.java
 *
 * Created on 16 de abril de 2004, 14:38
 */

package com.geopista.app.ocupaciones;

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
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import reso.jumpPlugIn.printLayoutPlugIn.DownloadFromServer;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.ocupaciones.panel.CCalendarJDialog;
import com.geopista.app.ocupaciones.panel.CPersonaJDialog;
import com.geopista.app.ocupaciones.panel.CSearchJDialog;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.app.reports.GenerarInformeExterno;
import com.geopista.app.reports.ReportsConstants;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.CConstantesPaths;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CPlantilla;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.util.config.UserPreferenceStore;

/**
 * @author charo
 */
public class CGeneracionInformes extends javax.swing.JInternalFrame implements IMultilingue{

	private Vector _vSolicitudes = null;
	private Vector _vExpedientes = null;

	private Hashtable _hWhere= null;

    private JFrame desktop;
    private String path = "";
	/**
	 * Modelo para el componente resultadosJTable
	 */
	CInformesTableModel _resultadosTableModel= new CInformesTableModel();

	Logger logger = Logger.getLogger(CGeneracionInformes.class);
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private String reportFile = "";
    /*
     * Almacenamos el listado con el num_expediente que utilizaremos en el ireport:
     * */
    private String lista_expedientes = "";
    
	/**
	 * Creates new form CGeneracionInformes
	 */
	public CGeneracionInformes(JFrame desktop) {
		this.desktop = desktop;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

		initComponents();
		initComboBoxesEstructuras();
		cargarPlantillas();

		buscarExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		fechaDesdeJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		fechaHastaJButton.setIcon(CUtilidadesComponentes.iconoZoom);

        CInformesTableModel.setColumnNames( new String[]{CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column1"),
                                                         CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column2"),
                                                         CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column3"),
                                                         CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column6"),
                                                         CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column4"),
                                                         CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column5")});

        _resultadosTableModel= new CInformesTableModel();
        resultadosJTable.setModel(_resultadosTableModel);

        fechaDesdeJTField.setEnabled(false);
        fechaHastaJTField.setEnabled(false);


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
		// formatoJLabel = new javax.swing.JLabel();
		plantillaJCBox = new javax.swing.JComboBox();
        /*
		nombreInformeJLabel = new javax.swing.JLabel();
		nombreInformeJTField = new javax.swing.JTextField();
        */
		aceptarJButton = new javax.swing.JButton();
		salirJButton = new javax.swing.JButton();
        borrarFechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();


		getContentPane().setLayout(new java.awt.GridLayout(1, 0));

		setClosable(true);
		setTitle("Generaci\u00f3n de Informes");
		addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
			public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
				formInternalFrameClosed(evt);
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
		//datosBusquedaJPanel.setToolTipText("");
		numExpedienteJLabel.setText("N\u00famero de Expediente:");
		datosBusquedaJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, -1, -1));

		datosBusquedaJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 51, 290, 20));

		titularJLabel.setText("DNI/CIF del Titular:");
		datosBusquedaJPanel.add(titularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, -1, -1));

		datosBusquedaJPanel.add(DNITitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 93, 290, 20));

		fechaAperturaJLabel.setText("Fecha Apertura (desde/hasta):");
		datosBusquedaJPanel.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, -1, -1));

		datosBusquedaJPanel.add(fechaDesdeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 114, 100, 20));

		datosBusquedaJPanel.add(fechaHastaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 114, 100, 20));

		tipoLicenciaJLabel.setText("Tipo Licencia:");
		datosBusquedaJPanel.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

		buscarJButton.setText("Buscar");
		buscarJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				_hWhere= buscarJButtonActionPerformed(evt);
			}
		});

		datosBusquedaJPanel.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 150, -1, -1));

		estadoJLabel.setText("Estado:");
		datosBusquedaJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, -1, -1));

		buscarExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
		buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
		buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buscarExpedienteJButtonActionPerformed(evt);
			}
		});

		datosBusquedaJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 51, 20, 20));

		buscarDNIJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		buscarDNIJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
		buscarDNIJButton.setPreferredSize(new java.awt.Dimension(30, 30));
		buscarDNIJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buscarDNIJButtonActionPerformed(evt);
			}
		});

		datosBusquedaJPanel.add(buscarDNIJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 93, 20, 20));

		fechaDesdeJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		fechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
		fechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
		fechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fechaDesdeJButtonActionPerformed(evt);
			}
		});

		datosBusquedaJPanel.add(fechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 114, 20, 20));

		fechaHastaJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		fechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
		fechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
		fechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fechaHastaJButtonActionPerformed(evt);
			}
		});

		datosBusquedaJPanel.add(fechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 114, 20, 20));

        borrarFechaDesdeJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
        borrarFechaDesdeJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaDesdeJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed(evt);
            }
        });

        datosBusquedaJPanel.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 114, 20, 20));

        borrarFechaHastaJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
        borrarFechaHastaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarFechaHastaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed(evt);
            }
        });

        datosBusquedaJPanel.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 115, 20, 20));


		//templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 830, 190));
        templateJPanel.add(datosBusquedaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 1000, 190));

		resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder("Resultado de la B\u00fasqueda"));
		resultadosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
		resultadosJTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{
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
				new String[]{
					"Tipo Licencia", "Num. Expediente", "Estado", "DNI/CIF Titular", "Fecha Apertura"
				}) {
			boolean[] canEdit = new boolean[]{
				false, true, false, false, true
			};

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
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
		//datosInformeJPanel.setToolTipText("");
		plantillaJLabel.setText("Plantilla para el Informe:");
		datosInformeJPanel.add(plantillaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

		// formatoJLabel.setText("Formato de Salida:");
		// datosInformeJPanel.add(formatoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

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
				aceptarJButtonActionPerformed(evt);
			}
		});

		templateJPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 600, 90, -1));

		salirJButton.setText("Cancelar");
		salirJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				salirJButtonActionPerformed(evt);
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



	/**
	 * Los estados no pueden redefinirse como dominio, puesto que necesitamos el valor del campo step
	 */
	public void initComboBoxesEstructuras() {
		while (!Estructuras.isCargada()) {
			if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		/** Inicializamos los comboBox que llevan estructuras */
		tipoOcupacionEJCBox = new ComboBoxEstructuras(Estructuras.getListaTipoOcupacion(), null, CMainOcupaciones.currentLocale.toString(), true);
        tipoOcupacionEJCBox.setSelectedIndex(0);
		datosBusquedaJPanel.add(tipoOcupacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 310, 20));

		estadoEJCBox = new ComboBoxEstructuras(Estructuras.getListaEstadosOcupacion(), null, CMainOcupaciones.currentLocale.toString(), true);
        estadoEJCBox.setSelectedIndex(0);
		datosBusquedaJPanel.add(estadoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 72, 310, 20));

		// formatoSalidaEJCBox = new ComboBoxEstructuras(Estructuras.getListaFormatosSalida(), null, CMainOcupaciones.currentLocale.toString(), false);
		// datosInformeJPanel.add(formatoSalidaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 72, 310, -1));

	}


	private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
		// TODO add your handling code here:
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);

	}//GEN-LAST:event_formInternalFrameClosed

	private void fechaDesdeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaDesdeJButtonActionPerformed
		// TODO add your handling code here:
        CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(desktop);
        if (dialog != null){
            String fecha= dialog.getFechaSelected();
            if ((fecha != null) && (!fecha.trim().equals(""))) {
                fechaDesdeJTField.setText(fecha);
            }
        }

	}//GEN-LAST:event_fechaDesdeJButtonActionPerformed

	private void fechaHastaJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaHastaJButtonActionPerformed
		// TODO add your handling code here:
        CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(desktop);
        if (dialog != null){
            String fecha= dialog.getFechaSelected();
            if ((fecha != null) && (!fecha.trim().equals(""))) {
                fechaHastaJTField.setText(fecha);
            }
        }

	}//GEN-LAST:event_fechaHastaJButtonActionPerformed

	private void buscarDNIJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarDNIJButtonActionPerformed
		// TODO add your handling code here:
        CPersonaJDialog dialog= CUtilidadesComponentes.showPersonaDialog(desktop);
        if (dialog != null){
            CPersonaJuridicoFisica persona = dialog.getPersona();
            if ((persona != null) && (persona.getDniCif() != null)) {
                DNITitularJTField.setText(persona.getDniCif());
            }
        }
	}//GEN-LAST:event_buscarDNIJButtonActionPerformed

	private void buscarExpedienteJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed
		// TODO add your handling code here:
        CSearchJDialog dialog= CUtilidadesComponentes.showSearchDialog(desktop, false);
        if ((dialog != null) && (dialog.getSelectedExpediente() != null)){
            numExpedienteJTField.setText(dialog.getSelectedExpediente());

        }

	}//GEN-LAST:event_buscarExpedienteJButtonActionPerformed

	private void salirJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirJButtonActionPerformed
		// TODO add your handling code here:
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
		this.dispose();
		/** Borramos el fichero de datos csv */
		/** ya no es necesario, porque la fuente de datos es la BBDD */
		//borrarFicheroCSV();
	}//GEN-LAST:event_salirJButtonActionPerformed
	
	private void aceptarJButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try{
			if ((_vExpedientes != null) && (_vExpedientes.size() > 0) && (_vSolicitudes != null) && (_vSolicitudes.size() > 0)) {
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
						mostrarMensaje(CMainOcupaciones.literales.getString("CGeneracionInformesForm.mensaje1"));
						e.printStackTrace();
						logger.error("exception thrown: " + e);
					}
				}
			}else{
				mostrarMensaje(CMainOcupaciones.literales.getString("CGeneracionInformesForm.mensaje3"));
			}
		}catch(Exception e){
			mostrarMensaje(CMainOcupaciones.literales.getString("CGeneracionInformesForm.mensaje4"));

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	private Hashtable buscarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarJButtonActionPerformed

		// TODO add your handling code here:
		Hashtable hash = new Hashtable();
		hash.put("EXPEDIENTE_LICENCIA.NUM_EXPEDIENTE", numExpedienteJTField.getText());
		if (tipoOcupacionEJCBox.getSelectedIndex() != 0) {
			hash.put("DATOS_OCUPACION.TIPO_OCUPACION", tipoOcupacionEJCBox.getSelectedPatron());
		}
		if (estadoEJCBox.getSelectedIndex() != 0) {
			hash.put("ESTADO_OCUPACION.ID_ESTADO", estadoEJCBox.getSelectedPatron());
		}
		hash.put("PERSONA_JURIDICO_FISICA.DNI_CIF", DNITitularJTField.getText());

        /** Fechas de Busqueda */
        if ((CUtilidades.parseFechaStringToString(fechaDesdeJTField.getText()) == null) || (CUtilidades.parseFechaStringToString(fechaHastaJTField.getText()) == null)) {
            mostrarMensaje(CMainOcupaciones.literales.getString("CSearchJDialog.mensaje2"));
            return null;
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
                    return null;
                }
                String fechaDesdeFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaDesde);
                long millis = fechaHasta.getTime();
                /** annadimos un dia */
                millis += 24 * 60 * 60 * 1000;
                fechaHasta = new Date(millis);
                String fechaHastaFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaHasta);

				hash.put("BETWEEN*EXPEDIENTE_LICENCIA.FECHA_APERTURA", fechaDesdeFormatted + "*" + fechaHastaFormatted);
			}

		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Datos de la solicitud y del expediente seleccionado
		CResultadoOperacion ro = COperacionesLicencias.getSolicitudesExpedientesInforme(hash, null);
		if (ro != null) {
			_vSolicitudes = ro.getSolicitudes();
			_vExpedientes = ro.getExpedientes();

			CUtilidadesComponentes.clearTable(_resultadosTableModel);
			if ((_vSolicitudes == null) || (_vExpedientes == null) || (_vSolicitudes.size() == 0) || (_vExpedientes.size() == 0)) {
				JOptionPane.showMessageDialog(desktop, "Búsqueda realizada sin resultados.");
				return null;
			}
			lista_expedientes = "";
			for (int i = 0; i < _vSolicitudes.size(); i++) {
				CSolicitudLicencia solicitud = (CSolicitudLicencia) _vSolicitudes.elementAt(i);
				CExpedienteLicencia expediente = (CExpedienteLicencia) _vExpedientes.elementAt(i);

				logger.info("solicitud.getIdSolicitud()=" + solicitud.getIdSolicitud() + " expediente.getNumExpediente=" + expediente.getNumExpediente());
                
				/*
                 * Rellenamos la lista con los numeros de expediente:*/
                lista_expedientes += "'"+expediente.getNumExpediente()+"'";
                if (i < _vSolicitudes.size()-1){
                	lista_expedientes += ", ";
                }
                
                String nombreSolicititante= CUtilidades.componerCampo(solicitud.getPropietario().getApellido1(), solicitud.getPropietario().getApellido2(), solicitud.getPropietario().getNombre());
                /** intentamos mostrar la referencia catastral con datos de emplazamiento(tipo de via, nombre de calle, ...) si la hay. */
                String emplazamiento = CUtilidades.componerCampo(solicitud.getTipoViaAfecta(), solicitud.getNombreViaAfecta(), solicitud.getNumeroViaAfecta());
                //String emplazamiento = CUtilidades.getEmplazamiento(solicitud);

				String descTipoOcupacion= ((DomainNode) Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(solicitud.getDatosOcupacion().getTipoOcupacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
				String descEstadoExpediente = ((DomainNode) Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(expediente.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
				_resultadosTableModel.addRow(new Object[]{descTipoOcupacion,
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
	/**
	 * ****************************************************************
	 */

	public void cargarPlantillas() {
		try {

        	AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.idAppType, ConstantesLocalGISPlantillas.PATH_OCUPACION);  
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
			
			path = UserPreferenceStore.getUserPreference(
					UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,
					UserPreferenceConstants.DEFAULT_DATA_PATH, true);
			String idAppType = (String)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.idAppType);
			if (!idAppType.equals("")){
				path = path+ReportsConstants.REPORTS_DIR+File.separator+idAppType+File.separator;
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

    public boolean bajarInformeFiles(String name){
        String path= "";
        String url= "";
        String pathDestino= "";


        try {

        	path = CConstantesPaths.PATH_PLANTILLAS_OCUPACION;
        	String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);
            pathDestino = localPath + UserPreferenceConstants.REPORT_DIR_NAME + File.separator + path;
            
            // Comprobamos que el path de las plantillas exista 
            if (!new File(pathDestino).exists()) {
                new File(pathDestino).mkdirs();
            }

            // bajamos la plantilla 
            url = CConstantesComando.plantillasOcupacionesUrl + name;
            pathDestino = pathDestino + name;
            reportFile = pathDestino;
            
            // Devolvemos verdadero si todo ha sido correcto:
            return CUtilidadesComponentes.GetURLFile(url, pathDestino, "", 0);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return false;
        }

    }
	
	/* Métodos antiguos, ya no se utilizan, se podrían borrar.
	 * 

	private void aceptarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarJButtonActionPerformed
		// TODO add your handling code here:
		try {
			boolean error = false;
			// De toda la informacion generada, mostraremos informe de los expedientes seleccionados (valido para fuente de datos charsep) 

			if ((_vExpedientes != null) && (_vExpedientes.size() > 0) && (_vSolicitudes != null) && (_vSolicitudes.size() > 0)) {
				// Fuente de datos charsep 

				// Generamos el informe con la informacion

				// bajamos del servidor la plantilla seleccionada por el usuario
				Hashtable h = bajarXMLFiles((String) plantillaJCBox.getSelectedItem());
				if ((h != null) & (h.size() > 0)) {
					Connection conn = null;
                    // Abrimos el dialogo showSaveDialog en el caso de que el formato no sea PREVIEW
                    boolean b= (new Integer(formatoSalidaEJCBox.getSelectedPatron()).intValue() == CConstantesOcupaciones.formatoPREVIEW)?false:true;
                    File file= showSaveFileDialog(b);
                    if ((file == null) && (b)) return;

                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    String nombreInforme= "";
                    String path= "";
                    if (file != null){
                        nombreInforme= file.getName();
                        path= file.getAbsolutePath();
                        int index= path.indexOf(nombreInforme);
                        if (index != -1){
                            path= path.substring(0, index);
                        }
                        // Si ya existe, borramos el fichero de datos
                        if (file.exists()) {
                          file.delete();
                        }
                    }

					try {
                        CPlantilla paramXML= new CPlantilla();
                        if (h.containsKey("PLANTILLA")){
                            CPlantilla XMLTemplate= (CPlantilla)h.get("PLANTILLA");
    						logger.debug("***** XMLTemplate=" + XMLTemplate.getPath());
    						Report r = new Report();
                            if ((CConstantesOcupaciones.DriverClassName != null) && (CConstantesOcupaciones.DriverClassName.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver"))){
                                r.setOracle(true);
                            }

                            // Fuente de datos BD
                            logger.debug("...................... Datos de conexion a BD ..................");
                            logger.debug("CConstantesOcupaciones.ConnectionInfo=" + CConstantesOcupaciones.ConnectionInfo);
                            logger.debug("CConstantesOcupaciones.DBUser=" + CConstantesOcupaciones.DBUser);
                            logger.debug("CConstantesOcupaciones.DBPassword=" + CConstantesOcupaciones.DBPassword);
                            logger.debug("CConstantesOcupaciones.DriverClassName=" + CConstantesOcupaciones.DriverClassName);

                            logger.debug("...................... DRIVERS registrados......................");
                            Enumeration e = DriverManager.getDrivers();
                            while (e.hasMoreElements()) {
                                Driver d = (Driver) e.nextElement();
                                logger.debug("Driver=" + d.toString());
                            }
                            logger.debug("................................................................");



                            // Eliminamos TODOS, por si cambia la urlGEOPISTA
                            e= DriverManager.getDrivers();
                            while (e.hasMoreElements()){
                              DriverManager.deregisterDriver((Driver)e.nextElement());
                            }

                            // La conexion desde el report se puede hacer:
                            // caso 1.-: pasando al report la conexion
                            // caso 2.-: pasando al report la password de conexion a BD.
                            // El resto de datos que necesita para hacer la conexion a BD los recoge de la plantilla en <source><database>


                            // caso 1 
                            Driver dPostgres= (Driver)Class.forName(CConstantesOcupaciones.DriverClassName).newInstance();
                            DriverManager.registerDriver(dPostgres);
                            // FRAN DriverManager.registerDriver(new org.postgresql.Driver());

                            logger.debug("...................... DRIVERS registrados......................");
                            e= DriverManager.getDrivers();
                            while (e.hasMoreElements()){
                                Driver d= (Driver)e.nextElement();
                                logger.debug("Driver="+d.toString());
                            }
                            logger.debug("................................................................");

                            conn = DriverManager.getConnection(CConstantesOcupaciones.ConnectionInfo, CConstantesOcupaciones.DBUser, CConstantesOcupaciones.DBPassword);
                            r.setDatabaseConnection(conn);


                            // caso 2 


                            // Read the report XML from a file or various stream types.
                            logger.debug("r.read(" + _pathPlantillas + XMLTemplate.getFileName() + ")");
                            r.read(_pathPlantillas + XMLTemplate.getFileName().replace('/', '\\'));

                            // If necessary, read parameter values.
                            if (r.hasParameterFields()) {
                                if (h.containsKey("PARAM")){
                                    paramXML= (CPlantilla)h.get("PARAM");
                                    logger.debug("***** paramXML="+paramXML.getPath());
                                    r.setParameterXMLInput(_pathPlantillas + paramXML.getFileName());
                                }else{
                                    // No existe el fichero param.xml
                                    error = true;
                                }
                            }
                             //Sustituido por el dialogo saveFileDialog
                            //String nombreInforme = nombreInformeJTField.getText();
                            //if ((nombreInforme.trim().length() == 0) || (nombreInforme.startsWith("."))) nombreInforme = "output";

                            if (!error){
                                if (new Integer(formatoSalidaEJCBox.getSelectedPatron()).intValue() == CConstantesOcupaciones.formatoPDF) {
                                    logger.debug("***** formato PDF *****");
                                    if (!nombreInforme.endsWith(".pdf"))
                                        nombreInforme = nombreInforme + ".pdf";
                                    //FileOutputStream fileOut = new FileOutputStream(_pathPlantillas + nombreInforme);
                                    FileOutputStream fileOut = new FileOutputStream(path + nombreInforme);
                                    r.setLayoutEngine(new PDFLE(fileOut));
                                } else if (new Integer(formatoSalidaEJCBox.getSelectedPatron()).intValue() == CConstantesOcupaciones.formatoTEXTO) {
                                    logger.debug("***** formato TXT *****");
                                    if (!nombreInforme.endsWith(".txt"))
                                        nombreInforme = nombreInforme + ".txt";
                                    //PrintWriter out = new PrintWriter(new FileWriter(_pathPlantillas + nombreInforme));
                                    PrintWriter out = new PrintWriter(new FileWriter(path + nombreInforme));
                                    r.setLayoutEngine(new CharSepLE(out, '\t'));
                                }
                                else if (new Integer(formatoSalidaEJCBox.getSelectedPatron()).intValue() == CConstantesOcupaciones.formatoPREVIEW) {
                                    logger.debug("***** formato PREVIEW *****");

                                    // Fuente de datos BD
                                    SwingLE le = new SwingLE(_pathPlantillas + paramXML.getFileName(), true) {
                                        public void close() {
                                            super.close();
                                            // Cierra la aplicacion
                                            //System.exit(0);
                                        }

                                    };

                                    r.setLayoutEngine(le);
                                }

                                // Inicio Fuente de datos BD 
                                r.setCaseSensitiveDatabaseNames(false);
                                // where clause para la fuente de datos BD
                                DataSource ds = r.getDataSource();
                                Query query = ds.getQuery();
                                String whereclause= dameWhereClause();

                                boolean hasDictionary= false;
                                query.findSelectablesUsed();
                                Iterator iterator= query.selectables();
                                while (iterator.hasNext() && !hasDictionary){
                                    Selectable selectable= (Selectable)iterator.next();
                                    // ORACLE:   DBUser.DICTIONARY
                                     // POSTGRES: public.dictionary                                    
                                    // VALIDO SOLO ORACLE if (selectable.getTable().getName().equalsIgnoreCase(CConstantesOcupaciones.DBUser+"."+"DICTIONARY")) hasDictionary= true;
                                    if (selectable.getTable().getName().toUpperCase().endsWith("."+"DICTIONARY")) hasDictionary= true;
                                }
                                if (hasDictionary){
                                    if (r.isOracle()){
                                        // Esto solo es necesario hacerlo si la BD es Oracle, ya que las tablas que aparezcan
                                        // en la clausula where han de aparecer en el from. Para la join con dictionary
                                        // necesitamos la tabla domainnodes, y ésta no está en el from de la query que genera
                                        // Datavision al no tener ninguna columna de esta tabla en la seccion Detail.
                                        Section section= (r.details()!=null?(r.details().hasNext()?(Section)r.details().next():null):null);
                                        if (section == null) throw new Exception("Plantilla sin seccion DETAIL");
                                        ColumnField cf= new ColumnField(null, r, section, CConstantesOcupaciones.DBUser.toUpperCase()+"."+"DOMAINNODES.ID_DESCRIPTION", false);
                                        section.addField(cf);
                                        cf= new ColumnField(null, r, section, CConstantesOcupaciones.DBUser.toUpperCase()+"."+"DATOS_OCUPACION.TIPO_OCUPACION", false);
                                        section.addField(cf);
                                    }
                                    // Annadimos al where la join de la tabla dictionary con la tabla domainnodes 
                                    whereclause+= addToWhereClause();
                                }

                                query.setWhereClause(whereclause);
                                logger.debug("QUERY=" + query.toString());

                                // Fin Fuente de datos BD 
                                r.runReport();
                            }else{
                                mostrarMensaje(CMainOcupaciones.literales.getString("CGeneracionInformesForm.mensaje4"));
                            }
                        }else{
                            mostrarMensaje(CMainOcupaciones.literales.getString("CGeneracionInformesForm.mensaje4"));
                        }

					} catch (Exception e) {
						error = true;
						mostrarMensaje(CMainOcupaciones.literales.getString("CGeneracionInformesForm.mensaje1"));
						e.printStackTrace();
						logger.error("exception thrown: " + e);
					} finally {
						if (conn != null) {
							try {
								conn.close();
							} catch (SQLException sqle) {
								logger.error("SQL exception thrown: " + sqle);
							}
						}
					}

					if ((!error) && (new Integer(formatoSalidaEJCBox.getSelectedPatron()).intValue() != CConstantesOcupaciones.formatoPREVIEW)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("CGeneracionInformesForm.mensaje2"));
					}
				}
			} else {
				mostrarMensaje(CMainOcupaciones.literales.getString("CGeneracionInformesForm.mensaje3"));
			}
		} catch (Exception e) {
			mostrarMensaje(CMainOcupaciones.literales.getString("CGeneracionInformesForm.mensaje4"));

			// Si ya existe, borramos el fichero de datos 
			// ya no es necesario, porque la fuente de datos es la BBDD 
			//borrarFicheroCSV();

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_aceptarJButtonActionPerformed

    private File showSaveFileDialog(boolean b){
        if (!b) return null;
        JFileChooser chooser = new JFileChooser();
        com.geopista.app.utilidades.GeoPistaFileFilter filter= new com.geopista.app.utilidades.GeoPistaFileFilter();

        if (new Integer(formatoSalidaEJCBox.getSelectedPatron()).intValue() == CConstantesOcupaciones.formatoPDF){
            filter.addExtension("pdf");
            filter.setDescription(CMainOcupaciones.literales.getString("JFileChooser.filter.setDescription") + " PDF");
            chooser.setFileFilter(filter);
        }else if (new Integer(formatoSalidaEJCBox.getSelectedPatron()).intValue() == CConstantesOcupaciones.formatoTEXTO){
            filter.addExtension("txt");
            filter.setDescription(CMainOcupaciones.literales.getString("JFileChooser.filter.setDescription") + " TXT");
            chooser.setFileFilter(filter);
        }

       // Permite multiples selecciones 
       chooser.setMultiSelectionEnabled(false);

       int returnVal = chooser.showSaveDialog(this);

       if (returnVal == JFileChooser.APPROVE_OPTION) {
           File selectedFile= chooser.getSelectedFile();
           return selectedFile;
       }
       return null;

    }
    
	public void borrarFicheroCSV() {
		// Borramos el fichero de datos csv
		File f = new File(CConstantesPaths.PLANTILLAS_PATH + "datos.csv");
		if (f.exists()) {
			f.delete();
		}
	}

    // Bajamos la plantilla seleccionada y el fichero de parámetros, que siempre sera el mismo
    // independientemente de la plantilla seleccionada.
    // Lo hacemos directamente con una conexion HTTP, por si acaso el fichero es muy grande.
    // NOTA: Plantilla tipo: puede tener un fichero de parametros y 2 imagenes (derecha e izquierda).
     public Hashtable bajarXMLFiles(String name){
         String path= "";
         String url= "";
         String pathDestino= "";

         try {
             Hashtable hPlantillas = new Hashtable();
             path= CConstantesPaths.PATH_PLANTILLAS_OCUPACION;

             // Comprobamos que el path de las plantillas y param.xml exista 
             if (!new File(path).exists()) {
                 new File(path).mkdirs();
             }

             // bajamos la plantilla 
             pathDestino= path + name;
             url= CConstantesComando.plantillasOcupacionesUrl + name;
             boolean resultado = CUtilidadesComponentes.GetURLFile(url, pathDestino, "", 0);
             if (resultado) {
                 CPlantilla plantilla = new CPlantilla(name);
                 plantilla.setPath(path);
                 hPlantillas.put("PLANTILLA", plantilla);
             }

             pathDestino= "";
             url= "";
             // bajamos el fichero param.xml 
             pathDestino= path + "param.xml";
             url= CConstantesComando.plantillasOcupacionesUrl + "param.xml";
             resultado = CUtilidadesComponentes.GetURLFile(url, pathDestino, "", 0);
             if (resultado){
                     CPlantilla param = new CPlantilla("param.xml");
                     param.setPath(path);
                     hPlantillas.put("PARAM",param);
             }

             // Comprobamos que el path de las imagenes exista 
             if (!new File(path + CConstantesPaths.IMAGE_PATH).exists()) {
                 new File(path + CConstantesPaths.IMAGE_PATH).mkdirs();
             }
             
             pathDestino= "";
             url= "";
             // bajamos la imagenes 
             pathDestino= path + CConstantesPaths.IMAGE_PATH + "derecha.gif";
             url=  CConstantesComando.plantillasOcupacionesUrl + CConstantesPaths.IMAGE_PATH + "derecha.gif";
             resultado = CUtilidadesComponentes.GetURLFile(url, pathDestino, "", 0);
             if (resultado){
                 CPlantilla imagen = new CPlantilla("derecha.gif");
                 imagen.setPath(path + CConstantesPaths.IMAGE_PATH);
                 hPlantillas.put("IMG1", imagen);
             }

             pathDestino= "";
             url= "";
             pathDestino= path + CConstantesPaths.IMAGE_PATH + "izquierda.gif";
             url=  CConstantesComando.plantillasOcupacionesUrl + CConstantesPaths.IMAGE_PATH + "izquierda.gif";
             resultado = CUtilidadesComponentes.GetURLFile(url, pathDestino, "", 0);
             if (resultado){
                 CPlantilla imagen = new CPlantilla("izquierda.gif");
                 imagen.setPath(path + CConstantesPaths.IMAGE_PATH);
                 hPlantillas.put("IMG2",imagen);
             }

             return hPlantillas;

         } catch (Exception ex) {
             StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw);
             ex.printStackTrace(pw);
             logger.error("Exception: " + sw.toString());
             return new Hashtable();
         }

     }

	public String dameWhereClause() {
		// Datavision concatena 'and (nuestra_cadena_where)'

        // AÑADIMOS: Sacamos la traduccion del tipo de ocupacion del DICTIONARY 
        String where = " EXPEDIENTE_LICENCIA.ID_SOLICITUD=SOLICITUD_LICENCIA.ID_SOLICITUD and " +
                "EXPEDIENTE_LICENCIA.APP_STRING='OCUPACIONES' and " +
                "SOLICITUD_LICENCIA.ID_SOLICITUD=DATOS_OCUPACION.ID_SOLICITUD and " +
                "SOLICITUD_LICENCIA.PROPIETARIO=PERSONA_JURIDICO_FISICA.ID_PERSONA " +
                " AND SOLICITUD_LICENCIA.ID_MUNICIPIO='" +  CConstantesOcupaciones.IdMunicipio + "' " +
                getConditions(_hWhere);

		logger.debug("dameWhereClause=" + where);
		return where;
	}

    public String addToWhereClause(){
        // NOTA: Sacamos la traduccion del tipo de obra/actividad de la tabla DICTIONARY 
        // domains.name='TIPO_OCUPACION' domains.id=68 
        String where= "AND DOMAINNODES.PATTERN=DATOS_OCUPACION.TIPO_OCUPACION AND " +
        "DICTIONARY.ID_VOCABLO=DOMAINNODES.ID_DESCRIPTION AND " +
        "DOMAINNODES.ID_DOMAIN=68 AND " +
        //"DICTIONARY.LOCALE='" + CMainOcupaciones.currentLocale.toString() + "' " +
        "DICTIONARY.LOCALE='es_ES'";
        return where;
    }


	private static String getConditions(Hashtable hash) {

		try {
			String conditions = "";

			if (hash == null) {
				return "";
			}

			Enumeration enumerationElement = hash.keys();

			while (enumerationElement.hasMoreElements()) {

				conditions += " and ";

				String field = (String) enumerationElement.nextElement();
				String value = (String) hash.get(field);

				if (field.startsWith("BETWEEN*")) {
					try {
						StringTokenizer st = new StringTokenizer(field);
						st.nextToken("*");
						String dateField = st.nextToken("*");

						st = new StringTokenizer(value);
						String desdeField = st.nextToken("*");

						String hastaField = st.nextToken("*");

						conditions += "" + dateField + " between date '" + desdeField + "' and date '" + hastaField + "'";
						continue;
					} catch (Exception ex) {
						continue;
					}
				}

				conditions += "upper(" + field + ") like upper('" + value + "%')";

			}

			return conditions;

		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return "";

		}

	}
*/
	/*********************************************************************/

	/**
	 * Exit the Application
	 */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		System.exit(0);
	}//GEN-LAST:event_exitForm

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		new CGeneracionInformes(null).show();
	}

	private ComboBoxEstructuras tipoOcupacionEJCBox;
	private ComboBoxEstructuras estadoEJCBox;
//	private ComboBoxEstructuras formatoSalidaEJCBox;

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
	//private javax.swing.JLabel formatoJLabel;
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

	// End of variables declaration//GEN-END:variables


	public void renombrarComponentes() {

		try {
			setTitle(CMainOcupaciones.literales.getString("CGeneracionInformesForm.JInternalFrame.title"));
			datosBusquedaJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CGeneracionInformesForm.datosBusquedaJPanel.TitleBorder")));
			datosInformeJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CGeneracionInformesForm.datosInformeJPanel.TitleBorder")));
			resultadosJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJScrollPane.TitleBorder")));

			tipoLicenciaJLabel.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.tipoLicenciaJLabel.text"));
			numExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.numExpedienteJLabel.text"));
			estadoJLabel.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.estadoJLabel.text"));
			titularJLabel.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.titularJLabel.text"));
			fechaAperturaJLabel.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.fechaAperturaJLabel.text"));

			buscarJButton.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.buscarJButton.text"));
			aceptarJButton.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.aceptarJButton.text"));
			salirJButton.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.salirJButton.text"));

			//nombreInformeJLabel.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.nombreInformeJLabel.text"));
			plantillaJLabel.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.plantillaJLabel.text"));
//			formatoJLabel.setText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.formatoJLabel.text"));

            /** Headers tabla resultados */
            TableColumn tableColumn= resultadosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column1"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column2"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column3"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column4"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column5"));
            tableColumn= resultadosJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CGeneracionInformesForm.resultadosJTable.text.column6"));

            buscarExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.buscarExpedienteJButton.setToolTip.text"));
            buscarDNIJButton.setToolTipText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.buscarDNIJButton.setToolTip.text"));
            fechaDesdeJButton.setToolTipText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.fechaDesdeJButton.setToolTip.text"));
            fechaHastaJButton.setToolTipText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.fechaHastaJButton.setToolTip.text"));
            borrarFechaDesdeJButton.setToolTipText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.borrarFechaDesdeJButton.setToolTip.text"));
            borrarFechaHastaJButton.setToolTipText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.borrarFechaHastaJButton.setToolTip.text"));

            buscarJButton.setToolTipText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.buscarJButton.text"));
            aceptarJButton.setToolTipText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.aceptarJButton.text"));
            salirJButton.setToolTipText(CMainOcupaciones.literales.getString("CGeneracionInformesForm.salirJButton.text"));

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
