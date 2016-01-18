/**
 * CCreacionLicencias.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.creacion;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.licencias.CConstantesLicencias;
import com.geopista.app.licencias.CConstantesLicencias_LCGIII;
import com.geopista.app.licencias.CMainLicencias;
import com.geopista.app.licencias.CUtilidades;
import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.app.licencias.IMultilingue;
import com.geopista.app.licencias.consulta.CConsultaLicencias;
import com.geopista.app.licencias.documentacion.DocumentacionLicenciasJPanel;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CReferenciasCatastralesTableModel;
import com.geopista.app.licencias.tecnicos.TecnicosJPanel;
import com.geopista.app.licencias.utilidades.ComboBoxTableEditor;
import com.geopista.app.licencias.utilidades.TextFieldRenderer;
import com.geopista.app.licencias.utilidades.TextFieldTableEditor;
import com.geopista.app.utilidades.JDialogConfiguracion;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.CellRendererEstructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.CDatosNotificacion;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.CViaNotificacion;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.protocol.licencias.tipos.CTipoObra;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * @author avivar
 */
public class CCreacionLicencias extends javax.swing.JInternalFrame implements IMultilingue{


	Logger logger = Logger.getLogger(CConsultaLicencias.class);
	/**
	 * Datos del Titular
	 */
    private String _DNI_CIF_Titular = "";
	private String _nombreTitular = "";
	private String _apellido1Titular = "";
	private String _apellido2Titular = "";
	private String _emailTitular = "";
	private String _nombreViaTitular = "";
	private String _numeroViaTitular = "";
	private String _portalTitular = "";
	private String _plantaTitular = "";
	private String _escaleraTitular = "";
	private String _letraTitular = "";
	private String _cPostalTitular = "";
	private String _municipioTitular = "";
	private String _provinciaTitular = "";
	private int _seNotificaTitular = 0;
    private boolean emailTitularObligatorio= false;

	/**
	 * Datos del Representado
	 */
	private String _DNI_CIF_RepresentaA = "";
	private String _nombreRepresentaA = "";
	private String _apellido1RepresentaA = "";
	private String _apellido2RepresentaA = "";
	private String _emailRepresentaA = "";
	private String _nombreViaRepresentaA = "";
	private String _numeroViaRepresentaA = "";
	private String _portalRepresentaA = "";
	private String _plantaRepresentaA = "";
	private String _escaleraRepresentaA = "";
	private String _letraRepresentaA = "";
	private String _cPostalRepresentaA = "";
	private String _municipioRepresentaA = "";
	private String _provinciaRepresentaA = "";
	private int _seNotificaRepresentaA = 0;
    private boolean _flagRepresentante= false;
    private boolean emailRepresentanteObligatorio= false;

	/**
	 * Datos del Promotor
	 */
	private String _DNI_CIF_Promotor = "";
	private String _nombrePromotor = "";
	private String _apellido1Promotor = "";
	private String _apellido2Promotor = "";
	private String _colegioPromotor = "";
	private String _visadoPromotor = "";
	private String _titulacionPromotor = "";
	private String _emailPromotor = "";
	private String _nombreViaPromotor = "";
	private String _numeroViaPromotor = "";
	private String _portalPromotor = "";
	private String _plantaPromotor = "";
	private String _escaleraPromotor = "";
	private String _letraPromotor = "";
	private String _cPostalPromotor = "";
	private String _municipioPromotor = "";
	private String _provinciaPromotor = "";
	private int _seNotificaPromotor = 0;
    private boolean emailPromotorObligatorio= false;

	/**
	 * Datos de la solicitud
	 */
	// Tendra un valor u otro en funcion del TabbedPane seleccionado (CConstantesLicencias.ObraMayor)
	private String _unidadRegistro = "";
	private String _unidadTramitadora = "";



	private Vector _vEstado = null;
	private Hashtable _hEstado = new Hashtable();

	/**
	 * Creates new form CCreacionLicencias
	 */
	public CCreacionLicencias(final JFrame desktop) {
		this.desktop = desktop;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);


        //***para sacar la ventana de espera**
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(this.desktop, null);
        progressDialog.setTitle(CMainLicencias.literales.getString("Licencias.Tag1"));
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                         try
                        {
                            progressDialog.report(CMainLicencias.literales.getString("Licencias.Tag2"));
                            initComponents();
                            initComboBoxesEstructuras();
                            configureComponents();
                            renombrarComponentes(CMainLicencias.literales);

                            // mejora
                            documentacionJPanel.setVisibleMejoraAnexosJPanel(false);
                            // alegacion
                            documentacionJPanel.setVisibleAnexosAlegacionJPanel(false);

                            fechaSolicitudJTField.setText(showToday());
                            fechaLimiteObraJTField.setText("");
                            estadoJCBox.setEnabled(false);
                            progressDialog.report(CMainLicencias.literales.getString("Licencias.Tag1"));

                            if (CUtilidadesComponentes.showGeopistaMap(desktop,editorMapaJPanel, CMainLicencias.geopistaEditor,/*"licencias_obra_mayor"*/276, false, "licencias_obra_mayor", "licencias_obra_menor",progressDialog)){
                                GeopistaLayer layer=(GeopistaLayer)CMainLicencias.geopistaEditor.getLayerManager().getLayer("parcelas");
                                if (layer!=null){
                                    layer.setEditable(true);
                                    layer.setActiva(true);
                                }
                            }else{
                                    new JOptionPane("No existe el mapa licencias obra mayor en el sistema. \nContacte con el administrador."
                                        , JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();
                                }
                        }
                        catch(Exception e){
                            logger.error("Error ", e);
                            ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                            return;
                        }
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
              }).start();
          }
       });
       GUIUtil.centreOnWindow(progressDialog);
       progressDialog.setVisible(true);

       CMainLicencias.geopistaEditor.removeAllGeopistaListener();
	   CMainLicencias.geopistaEditor.addGeopistaListener(new GeopistaListener() {

			public void selectionChanged(IAbstractSelection abtractSelection) {
				logger.info("selectionChanged");
			}

			public void featureAdded(FeatureEvent e) {
				logger.info("featureAdded");
			}

			public void featureRemoved(FeatureEvent e) {
				logger.info("featureRemoved");
			}

			public void featureModified(FeatureEvent e) {
				logger.info("featureModified");
			}

			public void featureActioned(IAbstractSelection abtractSelection) {
				logger.info("featureActioned");
			}


		});
	    municipioJTField.setText(CConstantesLicencias.Municipio);
		provinciaJTField.setText(CConstantesLicencias.Provincia);

		rellenarEstadoJCBox();

	}


	private void configureComponents() {

        fechaSolicitudJTField.setEnabled(false);
        fechaLimiteObraJTField.setEnabled(false);

		long tiempoInicial=new Date().getTime();
		if (CMainLicencias.geopistaEditor == null) CMainLicencias.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");

		logger.info("TIME (new GeopistaEditor()): "+(new Date().getTime()-tiempoInicial));

        referenciasCatastralesJTableModel= new CReferenciasCatastralesTableModel(new String[]{CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column1"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column2"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column3"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column4"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column5"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column6"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column7"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column8"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column9"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column10"),""});


		referenciasCatastralesJTable.setModel(referenciasCatastralesJTableModel);
		referenciasCatastralesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		referenciasCatastralesJTable.setCellSelectionEnabled(false);
		referenciasCatastralesJTable.setColumnSelectionAllowed(false);
		referenciasCatastralesJTable.setRowSelectionAllowed(true);

        referenciasCatastralesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        referenciasCatastralesJTable.getTableHeader().setReorderingAllowed(false);
        for (int j=0; j< referenciasCatastralesJTable.getColumnCount(); j++){
            TableColumn column = referenciasCatastralesJTable.getColumnModel().getColumn(j);

            if (j==1){
                column.setMinWidth(75);
                column.setMaxWidth(150);
                column.setWidth(75);
                column.setPreferredWidth(75);
            }else if (j>2 && j!=10){
                column.setMinWidth(50);
                column.setMaxWidth(100);
                column.setWidth(50);
                column.setPreferredWidth(50);
            }else if (j==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
                column.setWidth(0);
                column.setPreferredWidth(0);
            }else{
                column.setMinWidth(150);
                column.setMaxWidth(300);
                column.setWidth(150);
                column.setPreferredWidth(150);
            }
            column.setResizable(true);
        }

        //****************************
        // Annadimos a la tabla el editor ComboBox en la segunda columna (tipoVia)
        TableColumn column= referenciasCatastralesJTable.getColumnModel().getColumn(1);
        ComboBoxEstructuras comboBoxEstructuras= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), true);
        comboBoxEstructuras.setSelectedIndex(0);

        ComboBoxTableEditor comboBoxTableEditor= new ComboBoxTableEditor(comboBoxEstructuras);
        comboBoxTableEditor.setEnabled(true);
        column.setCellEditor(comboBoxTableEditor);

        CellRendererEstructuras renderer =
                       new CellRendererEstructuras(CMainLicencias.literales.getLocale().toString(),Estructuras.getListaTiposViaINE());
               column.setCellRenderer(renderer);

        // Annadimos a la tabla el editor TextField en el resto de columnas
        for (int col=2; col < referenciasCatastralesJTable.getColumnModel().getColumnCount(); col++){
            column= referenciasCatastralesJTable.getColumnModel().getColumn(col);
            TextFieldTableEditor textFieldTableEditor= null;
            if (col == 2){
                // Nombre
                textFieldTableEditor= new TextFieldTableEditor(68);
            }else if (col == 3){
                // Numero
                //textFieldTableEditor= new TextFieldTableEditor(true, 99999);
                textFieldTableEditor= new TextFieldTableEditor(8);
            }else if (col == 9){
                // CPostal
                textFieldTableEditor= new TextFieldTableEditor(true, 99999);
            }else{
                // resto de campos
                textFieldTableEditor= new TextFieldTableEditor(5);
            }
            textFieldTableEditor.setEnabled(true);
            column.setCellEditor(textFieldTableEditor);
            column.setCellRenderer(new TextFieldRenderer());
        }

        //******************************
		fechaSolicitudJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        fechaLimiteObraJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		nombreJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		referenciaCatastralJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		MapToTableJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoFlechaIzquierda);
		tableToMapJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoFlechaDerecha);

		buscarDNITitularJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		buscarDNIRepresentaAJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		buscarDNIPromotorJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		deleteRegistroCatastralJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);

        /** Observaciones */
        observacionesTPane= new com.geopista.app.utilidades.TextPane(254);
        observacionesJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        observacionesJScrollPane.setViewportView(observacionesTPane);
        datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 330, 70));

        /** Motivo */
        motivoTField= new com.geopista.app.utilidades.TextField(254);
        datosSolicitudJPanel.add(motivoTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 330, -1));


        /** Asunto */
        asuntoTField= new com.geopista.app.utilidades.TextField(254);
        datosSolicitudJPanel.add(asuntoTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 330, -1));

        /** dni */
        DNIPromotorTField= new com.geopista.app.utilidades.TextField(10);
        datosPersonalesPromotorJPanel.add(DNIPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 15, 340, -1));

        DNITitularTField= new com.geopista.app.utilidades.TextField(10) ;
        datosPersonalesTitularJPanel.add(DNITitularTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 340, -1));

        DNIRepresentaATField= new com.geopista.app.utilidades.TextField(10);
        datosPersonalesRepresentaAJPanel.add(DNIRepresentaATField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 340, -1));

        /** codigos postales */
        cPostalPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        cPostalPromotorTField.setSignAllowed(false);
        datosNotificacionPromotorJPanel.add(cPostalPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 360, -1));

        cPostalRepresentaATField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        cPostalRepresentaATField.setSignAllowed(false);
        datosNotificacionRepresentaAJPanel.add(cPostalRepresentaATField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, 360, -1));

        cPostalTitularTField = new TextField(5, true); 
      //  	new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
   //     cPostalTitularTField.setSignAllowed(false);
        datosNotificacionTitularJPanel.add(cPostalTitularTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, 360, -1));

        /** Numeros de via para la notificacion */
        numeroViaTitularTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        numeroViaTitularTField.setSignAllowed(false);
        datosNotificacionTitularJPanel.add(numeroViaTitularTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 184, 150, -1));

        numeroViaRepresentaATField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        numeroViaRepresentaATField.setSignAllowed(false);
        datosNotificacionRepresentaAJPanel.add(numeroViaRepresentaATField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 184, 150, -1));

        numeroViaPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        datosNotificacionPromotorJPanel.add(numeroViaPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 150, -1));

        /** Telefono para la notificacion */
        telefonoTitularTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        telefonoTitularTField.setSignAllowed(false);
        datosNotificacionTitularJPanel.add(telefonoTitularTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 64, 360, -1));

        telefonoRepresentaATField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        telefonoRepresentaATField.setSignAllowed(false);
        datosNotificacionRepresentaAJPanel.add(telefonoRepresentaATField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 64, 360, -1));

        telefonoPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        telefonoPromotorTField.setSignAllowed(false);
        datosNotificacionPromotorJPanel.add(telefonoPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 59, 360, -1));

        /** Movil para la notificacion */
        movilTitularTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        movilTitularTField.setSignAllowed(false);
        datosNotificacionTitularJPanel.add(movilTitularTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 86, 360, -1));

        movilRepresentaATField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        movilRepresentaATField.setSignAllowed(false);
        datosNotificacionRepresentaAJPanel.add(movilRepresentaATField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 86, 360, -1));

        movilPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        movilPromotorTField.setSignAllowed(false);
        datosNotificacionPromotorJPanel.add(movilPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 81, 360, -1));


        /** Fax para la notificacion */
        faxTitularTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        faxTitularTField.setSignAllowed(false);
        datosNotificacionTitularJPanel.add(faxTitularTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 42, 360, -1));

        faxRepresentaATField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        faxRepresentaATField.setSignAllowed(false);
        datosNotificacionRepresentaAJPanel.add(faxRepresentaATField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 42, 360, -1));

        faxPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        faxPromotorTField.setSignAllowed(false);
        datosNotificacionPromotorJPanel.add(faxPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 37, 360, -1));

        /** tasa e impuesto */
        impuestoTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
        impuestoTextField.setSignAllowed(false);
        datosSolicitudJPanel.add(impuestoTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 330, -1));

        tasaTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
        tasaTextField.setSignAllowed(false);
        datosSolicitudJPanel.add(tasaTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 330, -1));

        /** emplazamiento */
        nombreViaTField= new com.geopista.app.utilidades.TextField(68);
        nombreViaTField.setEditable(true);
        emplazamientoJPanel.add(nombreViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 38, 190, -1));

        //numeroViaNumberTField=  new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        numeroViaNumberTField=  new com.geopista.app.utilidades.TextField(8);
        numeroViaNumberTField.setEditable(true);
        //numeroViaNumberTField.setSignAllowed(false);
        emplazamientoJPanel.add(numeroViaNumberTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 61, 80, -1));

        portalViaTField= new com.geopista.app.utilidades.TextField(5);
        portalViaTField.setEditable(true);
        emplazamientoJPanel.add(portalViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 61, 80, -1));

        plantaViaTField= new com.geopista.app.utilidades.TextField(5);
        plantaViaTField.setEditable(true);
        emplazamientoJPanel.add(plantaViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 61, 70, -1));

        letraViaTField= new com.geopista.app.utilidades.TextField(5);
        letraViaTField.setEditable(true);
        emplazamientoJPanel.add(letraViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 61, 70, -1));

        cpostalViaTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        cpostalViaTField.setEditable(true);
        cpostalViaTField.setSignAllowed(false);
        emplazamientoJPanel.add(cpostalViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 84, 80, -1));


        /** Pestanas */
        jTabbedPaneSolicitud= new JTabbedPane();
        jTabbedPaneSolicitud.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPaneSolicitud.setFont(new java.awt.Font("Arial", 0, 10));
        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.obraMayorJPanel.SubTitleTab")), CUtilidadesComponentes.iconoSolicitud, obraMayorJPanel);

        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.obraMayorJPanel.SubTitleTab")), obraMayorJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.titularJPanel.TitleTab")), CUtilidadesComponentes.iconoPersona, titularJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.titularJPanel.TitleTab")), titularJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.representaAJPanel.TitleTab"), CUtilidadesComponentes.iconoRepresentante, representaAJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.representaAJPanel.TitleTab"), representaAJPanel);
        }

        /** Annadimos la pestanna del tecnico para la solicitud */
        tecnicosJPanel= new TecnicosJPanel(this.desktop, CMainLicencias.literales);
        tecnicosJPanel.setOperacion("CREACION");
        tecnicosJPanel.setEnabledEliminarJButton(false);
        tecnicosJPanel.setEnabledEditarJButton(false);
        tecnicosJPanel.setEnabledModificarJButton(false);

        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("TecnicosJPanel.TitleTab")), CUtilidadesComponentes.iconoPersona, tecnicosJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("TecnicosJPanel.TitleTab")), tecnicosJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.promotorJPanel.TitleTab")), CUtilidadesComponentes.iconoPersona, promotorJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.promotorJPanel.TitleTab")), promotorJPanel);
        }
        try{
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.obraMayorJPanel.TitleTab")), CUtilidadesComponentes.iconoSolicitud, jTabbedPaneSolicitud);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.obraMayorJPanel.TitleTab")), jTabbedPaneSolicitud);
        }
        /** Annadimos la pestanna de documentacion para la solicitud */
        documentacionJPanel= new DocumentacionLicenciasJPanel(CMainLicencias.literales,CConstantesLicencias.LicenciasObraMayor);
        documentacionJPanel.setCreacion();

        try{
            obraMayorJTabbedPane.addTab(CMainLicencias.literales.getString("DocumentacionLicenciasJPanel.title"), CUtilidadesComponentes.iconoDocumentacion, documentacionJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(CMainLicencias.literales.getString("DocumentacionLicenciasJPanel.title"), documentacionJPanel);
        }

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
        obraMayorJTabbedPane = new javax.swing.JTabbedPane();
        obraMayorJPanel = new javax.swing.JPanel();
        datosSolicitudJPanel = new javax.swing.JPanel();
        estadoJLabel = new javax.swing.JLabel();
        tipoObraJLabel = new javax.swing.JLabel();
        unidadTJLabel = new javax.swing.JLabel();
        unidadRJLabel = new javax.swing.JLabel();
        motivoJLabel = new javax.swing.JLabel();
        asuntoJLabel = new javax.swing.JLabel();
        fechaSolicitudJLabel = new javax.swing.JLabel();
        observacionesJLabel = new javax.swing.JLabel();
        estadoJCBox = new javax.swing.JComboBox();
        unidadTJTField = new javax.swing.JTextField();
        unidadRJTField = new javax.swing.JTextField();
        fechaSolicitudJTField = new javax.swing.JTextField();
        fechaSolicitudJButton = new javax.swing.JButton();
        tasaJLabel = new javax.swing.JLabel();
        observacionesJScrollPane = new javax.swing.JScrollPane();
        fechaLimiteObraJLabel = new javax.swing.JLabel();
        fechaLimiteObraJTField = new javax.swing.JTextField();
        fechaLimiteObraJButton = new javax.swing.JButton();
        impuestoJLabel = new javax.swing.JLabel();
        emplazamientoJPanel = new javax.swing.JPanel();
        nombreViaJLabel = new javax.swing.JLabel();
        numeroViaJLabel = new javax.swing.JLabel();
        cPostalJLabel = new javax.swing.JLabel();
        provinciaJLabel = new javax.swing.JLabel();
        municipioJTField = new javax.swing.JTextField();
        provinciaJTField = new javax.swing.JTextField();
        referenciasCatastralesJScrollPane = new javax.swing.JScrollPane();
        referenciasCatastralesJTable = new javax.swing.JTable();
        nombreJButton = new javax.swing.JButton();
        referenciaCatastralJLabel = new javax.swing.JLabel();
        referenciaJTextField = new javax.swing.JTextField();
        referenciaCatastralJButton = new javax.swing.JButton();
        MapToTableJButton = new javax.swing.JButton();
        deleteRegistroCatastralJButton = new javax.swing.JButton();
        tableToMapJButton = new javax.swing.JButton();
        titularJPanel = new javax.swing.JPanel();
        datosPersonalesTitularJPanel = new javax.swing.JPanel();
        DNITitularJLabel = new javax.swing.JLabel();
        nombreTitularJLabel = new javax.swing.JLabel();
        nombreTitularJTField = new javax.swing.JTextField();
        apellido1TitularJLabel = new javax.swing.JLabel();
        apellido2TitularJLabel2 = new javax.swing.JLabel();
        apellido1TitularJTField = new javax.swing.JTextField();
        apellido2TitularJTField = new javax.swing.JTextField();
        buscarDNITitularJButton = new javax.swing.JButton();
        datosNotificacionTitularJPanel = new javax.swing.JPanel();
        viaNotificacionTitularJLabel = new javax.swing.JLabel();
        faxTitularJLabel = new javax.swing.JLabel();
        telefonoTitularJLabel = new javax.swing.JLabel();
        movilTitularJLabel = new javax.swing.JLabel();
        emailTitularJLabel = new javax.swing.JLabel();
        tipoViaTitularJLabel = new javax.swing.JLabel();
        nombreViaTitularJLabel = new javax.swing.JLabel();
        numeroViaTitularJLabel = new javax.swing.JLabel();
        portalTitularJLabel = new javax.swing.JLabel();
        plantaTitularJLabel = new javax.swing.JLabel();
        escaleraTitularJLabel = new javax.swing.JLabel();
        letraTitularJLabel = new javax.swing.JLabel();
        cPostalTitularJLabel = new javax.swing.JLabel();
        municipioTitularJLabel = new javax.swing.JLabel();
        provinciaTitularJLabel = new javax.swing.JLabel();
        emailTitularJTField = new javax.swing.JTextField();
        nombreViaTitularJTField = new javax.swing.JTextField();
        plantaTitularJTField = new javax.swing.JTextField();
        portalTitularJTField = new javax.swing.JTextField();
        escaleraTitularJTField = new javax.swing.JTextField();
        letraTitularJTField = new javax.swing.JTextField();
        municipioTitularJTField = new javax.swing.JTextField();
        provinciaTitularJTField = new javax.swing.JTextField();
        notificarTitularJCheckBox = new javax.swing.JCheckBox();
        notificarTitularJLabel = new javax.swing.JLabel();
        representaAJPanel = new javax.swing.JPanel();
        datosPersonalesRepresentaAJPanel = new javax.swing.JPanel();
        DNIRepresenaAJLabel = new javax.swing.JLabel();
        nombreRepresentaAJLabel = new javax.swing.JLabel();
        nombreRepresentaAJTField = new javax.swing.JTextField();
        apellido1RepresentaAJLabel = new javax.swing.JLabel();
        apellido2RepresentaAJLabel = new javax.swing.JLabel();
        apellido1RepresentaAJTField = new javax.swing.JTextField();
        apellido2RepresentaAJTField = new javax.swing.JTextField();
        buscarDNIRepresentaAJButton = new javax.swing.JButton();
        datosNotificacionRepresentaAJPanel = new javax.swing.JPanel();
        viaNotificacionRepresentaAJLabel = new javax.swing.JLabel();
        faxRepresentaAJLabel = new javax.swing.JLabel();
        telefonoRepresentaAJLabel = new javax.swing.JLabel();
        movilRepresentaAJLabel = new javax.swing.JLabel();
        emailRepresentaAJLabel = new javax.swing.JLabel();
        tipoViaRepresentaAJLabel = new javax.swing.JLabel();
        nombreViaRepresentaAJLabel = new javax.swing.JLabel();
        numeroViaRepresentaAJLabel = new javax.swing.JLabel();
        portalRepresentaAJLabel = new javax.swing.JLabel();
        plantaRepresentaAJLabel = new javax.swing.JLabel();
        escaleraRepresentaAJLabel = new javax.swing.JLabel();
        letraRepresentaAJLabel = new javax.swing.JLabel();
        cPostalRepresentaAJLabel = new javax.swing.JLabel();
        municipioRepresentaAJLabel = new javax.swing.JLabel();
        provinciaRepresentaAJLabel = new javax.swing.JLabel();
        emailRepresentaAJTField = new javax.swing.JTextField();
        nombreViaRepresentaAJTField = new javax.swing.JTextField();
        plantaRepresentaAJTField = new javax.swing.JTextField();
        portalRepresentaAJTField = new javax.swing.JTextField();
        escaleraRepresentaAJTField = new javax.swing.JTextField();
        letraRepresentaAJTField = new javax.swing.JTextField();
        municipioRepresentaAJTField = new javax.swing.JTextField();
        provinciaRepresentaAJTField = new javax.swing.JTextField();
        notificarRepresentaAJCheckBox = new javax.swing.JCheckBox();
        notificarRepresentaAJLabel = new javax.swing.JLabel();
        promotorJPanel = new javax.swing.JPanel();
        datosPersonalesPromotorJPanel = new javax.swing.JPanel();
        DNIPromotorJLabel = new javax.swing.JLabel();
        nombrePromotorJLabel = new javax.swing.JLabel();
        nombrePromotorJTField = new javax.swing.JTextField();
        apellido1PromotorJLabel = new javax.swing.JLabel();
        apellido2PromotorJLabel = new javax.swing.JLabel();
        apellido1PromotorJTField = new javax.swing.JTextField();
        apellido2PromotorJTField = new javax.swing.JTextField();
        buscarDNIPromotorJButton = new javax.swing.JButton();
        colegioPromotorJLabel = new javax.swing.JLabel();
        visadoPromotorJLabel = new javax.swing.JLabel();
        titulacionPromotorJLabel = new javax.swing.JLabel();
        colegioPromotorJTField = new javax.swing.JTextField();
        visadoPromotorJTField = new javax.swing.JTextField();
        titulacionPromotorJTField = new javax.swing.JTextField();
        datosNotificacionPromotorJPanel = new javax.swing.JPanel();
        viaNotificacionPromotorJLabel = new javax.swing.JLabel();
        faxPromotorJLabel = new javax.swing.JLabel();
        telefonoPromotorJLabel = new javax.swing.JLabel();
        movilPromotorJLabel = new javax.swing.JLabel();
        emailPromotorJLabel = new javax.swing.JLabel();
        tipoViaPromotorJLabel = new javax.swing.JLabel();
        nombreViaPromotorJLabel = new javax.swing.JLabel();
        numeroViaPromotorJLabel = new javax.swing.JLabel();
        portalPromotorJLabel = new javax.swing.JLabel();
        plantaPromotorJLabel = new javax.swing.JLabel();
        escaleraPromotorJLabel = new javax.swing.JLabel();
        letraPromotorJLabel = new javax.swing.JLabel();
        cPostalPromotorJLabel = new javax.swing.JLabel();
        municipioPromotorJLabel = new javax.swing.JLabel();
        provinciaPromotorJLabel = new javax.swing.JLabel();
        emailPromotorJTField = new javax.swing.JTextField();
        nombreViaPromotorJTField = new javax.swing.JTextField();
        plantaPromotorJTField = new javax.swing.JTextField();
        portalPromotorJTField = new javax.swing.JTextField();
        escaleraPromotorJTField = new javax.swing.JTextField();
        letraPromotorJTField = new javax.swing.JTextField();
        municipioPromotorJTField = new javax.swing.JTextField();
        provinciaPromotorJTField = new javax.swing.JTextField();
        notificarPromotorJCheckBox = new javax.swing.JCheckBox();
        notificarPromotorJLabel = new javax.swing.JLabel();
        botoneraJPanel = new javax.swing.JPanel();
        aceptarJButton = new javax.swing.JButton();
        cancelarJButton = new javax.swing.JButton();
        editorMapaJPanel = new javax.swing.JPanel();

        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        setClosable(true);
        setTitle("Creaci\u00f3n de Licencia de Obra Mayor");
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

        obraMayorJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        obraMayorJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));


        obraMayorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder("Datos solicitud"));
        datosSolicitudJPanel.setAutoscrolls(true);
        datosSolicitudJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 150, 20));
        datosSolicitudJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 20));
        datosSolicitudJPanel.add(unidadTJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 20));
        datosSolicitudJPanel.add(unidadRJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 150, 20));
        datosSolicitudJPanel.add(motivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 150, 20));

        datosSolicitudJPanel.add(asuntoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 150, 20));

        datosSolicitudJPanel.add(fechaSolicitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 150, 20));

        datosSolicitudJPanel.add(observacionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 150, 20));

        datosSolicitudJPanel.add(estadoJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 330, 20));

        datosSolicitudJPanel.add(unidadTJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 330, -1));

        datosSolicitudJPanel.add(unidadRJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 330, -1));

        datosSolicitudJPanel.add(fechaSolicitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 70, -1));

        fechaSolicitudJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaSolicitudJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaSolicitudJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaSolicitudJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaSolicitudJButtonActionPerformed();
            }
        });

        datosSolicitudJPanel.add(fechaSolicitudJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 20, 20));

       datosSolicitudJPanel.add(tasaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 150, 20));

        datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 330, 70));

        datosSolicitudJPanel.add(fechaLimiteObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 150, 20));

        datosSolicitudJPanel.add(fechaLimiteObraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 70, -1));

        fechaLimiteObraJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaLimiteObraJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaLimiteObraJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaLimiteObraJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaLimiteObraJButtonActionPerformed();
            }
        });

        datosSolicitudJPanel.add(fechaLimiteObraJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 160, 20, 20));

        datosSolicitudJPanel.add(impuestoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 150, 20));

        obraMayorJPanel.add(datosSolicitudJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 310));

        emplazamientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder("Emplazamiento"));
        emplazamientoJPanel.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 38, 150, 20));

        emplazamientoJPanel.add(numeroViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 61, 150, 20));
        emplazamientoJPanel.add(cPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 84, 150, 20));

        emplazamientoJPanel.add(provinciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 107, 150, 20));

        municipioJTField.setEnabled(false);
        emplazamientoJPanel.add(municipioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 84, 240, -1));

        provinciaJTField.setEnabled(false);
        emplazamientoJPanel.add(provinciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 107, 330, -1));

        referenciasCatastralesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                referenciasCatastralesJTableMouseClicked();
            }
        });

        referenciasCatastralesJScrollPane.setViewportView(referenciasCatastralesJTable);

        emplazamientoJPanel.add(referenciasCatastralesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 135, 470, 90));

        nombreJButton.setIcon(new javax.swing.ImageIcon(""));
        nombreJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        nombreJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        nombreJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(nombreJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 38, 20, 20));

        emplazamientoJPanel.add(referenciaCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 150, 20));

        referenciaJTextField.setEnabled(false);
        emplazamientoJPanel.add(referenciaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 15, 310, -1));

        referenciaCatastralJButton.setIcon(new javax.swing.ImageIcon(""));
        referenciaCatastralJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        referenciaCatastralJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        referenciaCatastralJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referenciaCatastralJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(referenciaCatastralJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 15, 20, 20));

        MapToTableJButton.setIcon(new javax.swing.ImageIcon(""));
        MapToTableJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MapToTableJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        MapToTableJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MapToTableJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(MapToTableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 135, 20, 20));

        deleteRegistroCatastralJButton.setIcon(new javax.swing.ImageIcon(""));
        deleteRegistroCatastralJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        deleteRegistroCatastralJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        deleteRegistroCatastralJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRegistroCatastralJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(deleteRegistroCatastralJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 205, 20, 20));

        tableToMapJButton.setIcon(new javax.swing.ImageIcon(""));
        tableToMapJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableToMapJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        tableToMapJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableToMapJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(tableToMapJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 155, 20, 20));

        obraMayorJPanel.add(emplazamientoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 520, 237));

        titularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTitularJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNITitularJLabel.setText("(*) DNI/CIF:");
        datosPersonalesTitularJPanel.add(DNITitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        nombreTitularJLabel.setText("(*) Nombre:");
        datosPersonalesTitularJPanel.add(nombreTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 20));

        datosPersonalesTitularJPanel.add(nombreTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 360, -1));

        apellido1TitularJLabel.setText("Apellido1:");
        datosPersonalesTitularJPanel.add(apellido1TitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 75, 130, 20));

        apellido2TitularJLabel2.setText("Apellido2:");
        datosPersonalesTitularJPanel.add(apellido2TitularJLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 130, 20));

        datosPersonalesTitularJPanel.add(apellido1TitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 75, 360, -1));

        datosPersonalesTitularJPanel.add(apellido2TitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 360, -1));

        buscarDNITitularJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNITitularJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNITitularJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNITitularJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNITitularJButtonActionPerformed();
            }
        });


        datosPersonalesTitularJPanel.add(buscarDNITitularJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 20, 20));

        titularJPanel.add(datosPersonalesTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 135));

        datosNotificacionTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionTitularJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionTitularJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionTitularJPanel.add(viaNotificacionTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        faxTitularJLabel.setText("Fax:");
        datosNotificacionTitularJPanel.add(faxTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, 130, 20));

        telefonoTitularJLabel.setText("Tel\u00e9fono:");
        datosNotificacionTitularJPanel.add(telefonoTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 130, 20));

        movilTitularJLabel.setText("M\u00f3vil:");
        datosNotificacionTitularJPanel.add(movilTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 86, 130, 20));

        emailTitularJLabel.setText("Email:");
        datosNotificacionTitularJPanel.add(emailTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 108, 130, 20));

        tipoViaTitularJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionTitularJPanel.add(tipoViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, 20));

        nombreViaTitularJLabel.setText("(*) Nombre:");
        datosNotificacionTitularJPanel.add(nombreViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 162, 130, 20));

        numeroViaTitularJLabel.setText("(*) N\u00famero:");
        datosNotificacionTitularJPanel.add(numeroViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 184, 90, 20));

        portalTitularJLabel.setText("Portal:");
        datosNotificacionTitularJPanel.add(portalTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 50, 20));

        plantaTitularJLabel.setText("Planta:");
        datosNotificacionTitularJPanel.add(plantaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 206, 70, 20));

        escaleraTitularJLabel.setText("Escalera:");
        datosNotificacionTitularJPanel.add(escaleraTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 272, 60, 20));

        letraTitularJLabel.setText("Letra:");
        datosNotificacionTitularJPanel.add(letraTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 228, 40, 20));

        cPostalTitularJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionTitularJPanel.add(cPostalTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 130, 20));

        municipioTitularJLabel.setText("(*) Municipio:");
        datosNotificacionTitularJPanel.add(municipioTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 332, 130, 20));

        provinciaTitularJLabel.setText("(*) Provincia:");
        datosNotificacionTitularJPanel.add(provinciaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 354, 130, 20));

        datosNotificacionTitularJPanel.add(emailTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 108, 360, -1));

        datosNotificacionTitularJPanel.add(nombreViaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 162, 360, -1));

        datosNotificacionTitularJPanel.add(plantaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 206, 150, -1));

        datosNotificacionTitularJPanel.add(portalTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 150, -1));

        datosNotificacionTitularJPanel.add(escaleraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 272, 150, -1));

        datosNotificacionTitularJPanel.add(letraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 228, 150, -1));

        datosNotificacionTitularJPanel.add(municipioTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 332, 360, -1));

        datosNotificacionTitularJPanel.add(provinciaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 354, 360, -1));

        datosNotificacionTitularJPanel.add(notificarTitularJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 376, 30, -1));
        notificarTitularJCheckBox.setEnabled(false);
        notificarTitularJCheckBox.setSelected(true);

        notificarTitularJLabel.setText("Notificar propietario:");
        datosNotificacionTitularJPanel.add(notificarTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 376, 120, 20));

        titularJPanel.add(datosNotificacionTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 135, 520, 412));

        representaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNIRepresenaAJLabel.setText("(*) DNI/CIF:");
        datosPersonalesRepresentaAJPanel.add(DNIRepresenaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        nombreRepresentaAJLabel.setText("(*) Nombre:");
        datosPersonalesRepresentaAJPanel.add(nombreRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 20));

        datosPersonalesRepresentaAJPanel.add(nombreRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 360, -1));

        apellido1RepresentaAJLabel.setText("Apellido1:");
        datosPersonalesRepresentaAJPanel.add(apellido1RepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 75, 130, 20));

        apellido2RepresentaAJLabel.setText("Apellido2:");
        datosPersonalesRepresentaAJPanel.add(apellido2RepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 130, 20));

        datosPersonalesRepresentaAJPanel.add(apellido1RepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 75, 360, -1));

        datosPersonalesRepresentaAJPanel.add(apellido2RepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 360, -1));

        buscarDNIRepresentaAJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNIRepresentaAJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIRepresentaAJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIRepresentaAJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIRepresentaAJButtonActionPerformed();
            }
        });

        datosPersonalesRepresentaAJPanel.add(buscarDNIRepresentaAJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 20, 20));

        representaAJPanel.add(datosPersonalesRepresentaAJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 135));

        datosNotificacionRepresentaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionRepresentaAJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionRepresentaAJPanel.add(viaNotificacionRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        faxRepresentaAJLabel.setText("Fax:");
        datosNotificacionRepresentaAJPanel.add(faxRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, 130, 20));

        telefonoRepresentaAJLabel.setText("Tel\u00e9fono:");
        datosNotificacionRepresentaAJPanel.add(telefonoRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 130, 20));

        movilRepresentaAJLabel.setText("M\u00f3vil:");
        datosNotificacionRepresentaAJPanel.add(movilRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 86, 130, 20));

        emailRepresentaAJLabel.setText("Email:");
        datosNotificacionRepresentaAJPanel.add(emailRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 108, 130, 20));

        tipoViaRepresentaAJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionRepresentaAJPanel.add(tipoViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, 20));

        nombreViaRepresentaAJLabel.setText("(*) Nombre:");
        datosNotificacionRepresentaAJPanel.add(nombreViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 162, 100, 20));

        numeroViaRepresentaAJLabel.setText("(*) N\u00famero:");
        datosNotificacionRepresentaAJPanel.add(numeroViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 184, 90, 20));

        portalRepresentaAJLabel.setText("Portal:");
        datosNotificacionRepresentaAJPanel.add(portalRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 50, 20));

        plantaRepresentaAJLabel.setText("Planta:");
        datosNotificacionRepresentaAJPanel.add(plantaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 206, 70, 20));

        escaleraRepresentaAJLabel.setText("Escalera:");
        datosNotificacionRepresentaAJPanel.add(escaleraRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 272, 60, 20));

        letraRepresentaAJLabel.setText("Letra:");
        datosNotificacionRepresentaAJPanel.add(letraRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 228, 40, 20));

        cPostalRepresentaAJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionRepresentaAJPanel.add(cPostalRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 130, 20));

        municipioRepresentaAJLabel.setText("(*) Municipio:");
        datosNotificacionRepresentaAJPanel.add(municipioRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 332, 130, 20));

        provinciaRepresentaAJLabel.setText("(*) Provincia:");
        datosNotificacionRepresentaAJPanel.add(provinciaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 354, 130, 20));

        datosNotificacionRepresentaAJPanel.add(emailRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 108, 360, -1));

        datosNotificacionRepresentaAJPanel.add(nombreViaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 162, 360, -1));

        datosNotificacionRepresentaAJPanel.add(plantaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 206, 150, -1));

        datosNotificacionRepresentaAJPanel.add(portalRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 150, -1));

        datosNotificacionRepresentaAJPanel.add(escaleraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 272, 150, -1));

        datosNotificacionRepresentaAJPanel.add(letraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 228, 150, -1));

        datosNotificacionRepresentaAJPanel.add(municipioRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 332, 360, -1));

        datosNotificacionRepresentaAJPanel.add(provinciaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 354, 360, -1));

        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 376, 30, -1));

        notificarRepresentaAJLabel.setText("Notificar representante:");
        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 376, 120, 20));

        representaAJPanel.add(datosNotificacionRepresentaAJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 135, 520, 412));

        promotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNIPromotorJLabel.setText("(*) DNI/CIF:");
        datosPersonalesPromotorJPanel.add(DNIPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 130, 20));

        nombrePromotorJLabel.setText("(*) Nombre:");
        datosPersonalesPromotorJPanel.add(nombrePromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 45, 130, 20));

        datosPersonalesPromotorJPanel.add(nombrePromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 45, 360, -1));

        apellido1PromotorJLabel.setText("Apellido1:");
        datosPersonalesPromotorJPanel.add(apellido1PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 67, 130, 20));

        apellido2PromotorJLabel.setText("Apellido2:");
        datosPersonalesPromotorJPanel.add(apellido2PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 89, 130, 20));

        datosPersonalesPromotorJPanel.add(apellido1PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 67, 360, -1));

        datosPersonalesPromotorJPanel.add(apellido2PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 89, 360, -1));

        buscarDNIPromotorJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNIPromotorJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIPromotorJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIPromotorJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIPromotorJButtonActionPerformed();
            }
        });

        datosPersonalesPromotorJPanel.add(buscarDNIPromotorJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 15, 20, 20));

        colegioPromotorJLabel.setText("(*) Colegio:");
        datosPersonalesPromotorJPanel.add(colegioPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 111, 130, 20));

        visadoPromotorJLabel.setText("(*) Visado:");
        datosPersonalesPromotorJPanel.add(visadoPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 133, 130, 20));

        titulacionPromotorJLabel.setText("Titulaci\u00f3n:");
        datosPersonalesPromotorJPanel.add(titulacionPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 155, 130, 20));

        datosPersonalesPromotorJPanel.add(colegioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 111, 360, -1));

        datosPersonalesPromotorJPanel.add(visadoPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 133, 360, -1));

        datosPersonalesPromotorJPanel.add(titulacionPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 155, 360, -1));

        promotorJPanel.add(datosPersonalesPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 185));

        datosNotificacionPromotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionPromotorJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionPromotorJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionPromotorJPanel.add(viaNotificacionPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 130, 20));

        faxPromotorJLabel.setText("Fax:");
        datosNotificacionPromotorJPanel.add(faxPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 37, 130, 20));

        telefonoPromotorJLabel.setText("Tel\u00e9fono:");
        datosNotificacionPromotorJPanel.add(telefonoPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 59, 130, 20));

        movilPromotorJLabel.setText("M\u00f3vil:");
        datosNotificacionPromotorJPanel.add(movilPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 81, 130, 20));

        emailPromotorJLabel.setText("Email:");
        datosNotificacionPromotorJPanel.add(emailPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 103, 130, 20));

        tipoViaPromotorJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionPromotorJPanel.add(tipoViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 130, 20));

        nombreViaPromotorJLabel.setText("(*) Nombre:");
        datosNotificacionPromotorJPanel.add(nombreViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 130, 20));

        numeroViaPromotorJLabel.setText("(*) N\u00famero:");
        datosNotificacionPromotorJPanel.add(numeroViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 80, 20));

        portalPromotorJLabel.setText("Portal:");
        datosNotificacionPromotorJPanel.add(portalPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 50, 20));

        plantaPromotorJLabel.setText("Planta:");
        datosNotificacionPromotorJPanel.add(plantaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 80, 20));

        escaleraPromotorJLabel.setText("Escalera:");
        datosNotificacionPromotorJPanel.add(escaleraPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 60, 20));

        letraPromotorJLabel.setText("Letra:");
        datosNotificacionPromotorJPanel.add(letraPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 80, 20));

        cPostalPromotorJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionPromotorJPanel.add(cPostalPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 130, 20));

        municipioPromotorJLabel.setText("(*) Municipio:");
        datosNotificacionPromotorJPanel.add(municipioPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 130, 20));

        provinciaPromotorJLabel.setText("(*) Provincia:");
        datosNotificacionPromotorJPanel.add(provinciaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 130, 20));

        datosNotificacionPromotorJPanel.add(emailPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 103, 360, -1));

        datosNotificacionPromotorJPanel.add(nombreViaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 360, -1));

        datosNotificacionPromotorJPanel.add(plantaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 150, -1));

        datosNotificacionPromotorJPanel.add(portalPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 150, -1));

        datosNotificacionPromotorJPanel.add(escaleraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 150, -1));

        datosNotificacionPromotorJPanel.add(letraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 150, -1));

        datosNotificacionPromotorJPanel.add(municipioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 360, -1));

        datosNotificacionPromotorJPanel.add(provinciaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, 360, -1));

        datosNotificacionPromotorJPanel.add(notificarPromotorJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 340, 80, 20));
        notificarPromotorJCheckBox.setVisible(false);

        notificarPromotorJLabel.setText("Notificar promotor:");
        datosNotificacionPromotorJPanel.add(notificarPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 120, 20));
        notificarPromotorJLabel.setVisible(false);

        promotorJPanel.add(datosNotificacionPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 185, 520, 361));

        templateJPanel.setLayout(new BorderLayout());
        templateJPanel.add(obraMayorJTabbedPane, BorderLayout.WEST);


        botoneraJPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        aceptarJButton.setText("Crear");
        aceptarJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aceptarJButtonMouseClicked();
            }
        });
        aceptarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 90, -1));

        cancelarJButton.setText("Salir");
        cancelarJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelarJButtonMouseClicked();
            }
        });

        cancelarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 90, -1));

        templateJPanel.add(botoneraJPanel, BorderLayout.SOUTH);

        editorMapaJPanel.setLayout(new java.awt.GridLayout(1, 0));

        editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder("Mapa"));
        templateJPanel.add(editorMapaJPanel, BorderLayout.CENTER);

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane);

    }//GEN-END:initComponents

    private void fechaLimiteObraJButtonActionPerformed() {//GEN-FIRST:event_fechaResolucionJButtonActionPerformed
        CUtilidadesComponentes.showCalendarDialog(desktop);
        if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
            fechaLimiteObraJTField.setText(CConstantesLicencias.calendarValue);
        }

    }//GEN-LAST:event_fechaResolucionJButtonActionPerformed

    private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
        CConstantesLicencias.helpSetHomeID = "geopistaIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);
    }//GEN-LAST:event_formInternalFrameClosed

    /** Los estados no pueden redefinirse como dominio, puesto que necesitamos el valor del campo step */
    public void initComboBoxesEstructuras(){

        while (!Estructuras.isCargada())
        {
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }

        /** Inicializamos los comboBox que llevan estructuras */
        tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposObra(), null, CMainLicencias.currentLocale.toString(), false);
        datosSolicitudJPanel.add(tipoObraEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 330, 20));

        viaNotificacionTitularEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainLicencias.currentLocale.toString(), false);
        viaNotificacionTitularEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionTitularEJCBoxActionPerformed();}});

        datosNotificacionTitularJPanel.add(viaNotificacionTitularEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 360, 20));
        tipoViaNotificacionTitularEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), false);
        datosNotificacionTitularJPanel.add(tipoViaNotificacionTitularEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 360, 20));

        viaNotificacionRepresentaAEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainLicencias.currentLocale.toString(), false);
        viaNotificacionRepresentaAEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionRepresentaAEJCBoxActionPerformed();}});

        datosNotificacionRepresentaAJPanel.add(viaNotificacionRepresentaAEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 360, 20));
        tipoViaNotificacionRepresentaAEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), false);
        datosNotificacionRepresentaAJPanel.add(tipoViaNotificacionRepresentaAEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 360, 20));

        viaNotificacionPromotorEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainLicencias.currentLocale.toString(), false);
        viaNotificacionPromotorEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionPromotorEJCBoxActionPerformed();}});

        datosNotificacionPromotorJPanel.add(viaNotificacionPromotorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 15, 360, 20));
        tipoViaNotificacionPromotorEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), false);
        datosNotificacionPromotorJPanel.add(tipoViaNotificacionPromotorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 360, 20));

        tipoViaINEEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), false);
        DomainNode dn= new DomainNode();
        dn.setPatron("-1");
        dn.setTerm(CConstantesLicencias.LocalCastellano, " ");
        dn.setTerm(CConstantesLicencias.LocalValenciano, " ");
        dn.setTerm(CConstantesLicencias.LocalCatalan, " ");
        dn.setTerm(CConstantesLicencias.LocalEuskera, " ");
        dn.setTerm(CConstantesLicencias.LocalGallego, " ");
        tipoViaINEEJCBox.addItem(dn);
        tipoViaINEEJCBox.setSelectedPatron("-1");
        emplazamientoJPanel.add(tipoViaINEEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 38, 110, 20));

    }

    private void viaNotificacionTitularEJCBoxActionPerformed() {
        String index= viaNotificacionTitularEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailTitularJLabel.text")));
            emailTitularObligatorio= true;
        }else{
            emailTitularJLabel.setText(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailTitularJLabel.text"));
            emailTitularObligatorio= false;
        }

    }

    private void viaNotificacionRepresentaAEJCBoxActionPerformed() {

        String index= viaNotificacionRepresentaAEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailRepresentaAJLabel.text")));
            emailRepresentanteObligatorio= true;
        }else{
            emailRepresentaAJLabel.setText(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailRepresentaAJLabel.text"));
            emailRepresentanteObligatorio= false;
        }

    }

    private void viaNotificacionPromotorEJCBoxActionPerformed() {

        String index= viaNotificacionPromotorEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailPromotorJLabel.text")));
            emailPromotorObligatorio= true;
        }else{
            emailPromotorJLabel.setText(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailPromotorJLabel.text"));
            emailPromotorObligatorio= false;
        }

    }


	private void referenciasCatastralesJTableMouseClicked() {//GEN-FIRST:event_referenciasCatastralesJTableMouseClicked

		try {

			int selectedRow = referenciasCatastralesJTable.getSelectedRow();
			logger.info("selectedRow: " + selectedRow);

			if (selectedRow != -1) {

				logger.info("referenciasCatastralesJTable.getValueAt(selectedRow, 0): " + referenciasCatastralesJTable.getValueAt(selectedRow, 0));

                CConstantesLicencias_LCGIII.referencia.setReferenciaCatastral((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 0));

                Object objeto=referenciasCatastralesJTableModel.getValueAt(selectedRow, 1);
                String patron=null;
                CConstantesLicencias_LCGIII.referencia.setTipoVia("");
                if ((objeto instanceof DomainNode) && objeto!=null)
                {
                    CConstantesLicencias_LCGIII.referencia.setTipoVia(((DomainNode)objeto).getTerm(CMainLicencias.literales.getLocale().toString()));
                    patron=((DomainNode)objeto).getPatron();
                }
                if ((objeto instanceof String) && objeto!=null)
                {
                    if (((String)objeto).length()>0)
                    {
                    	

                    	if(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto)==null){
                    		CConstantesLicencias_LCGIII.referencia.setTipoVia(CMainLicencias.literales.getString("CModificacionLicenciasForm.tipovianoespecificado.text"));
                    		patron="NE";
                    	}
                    	
                    	
                    	else {    
                        CConstantesLicencias_LCGIII.referencia.setTipoVia(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto).getTerm(CMainLicencias.literales.getLocale().toString()));
                        patron=((String)objeto);
                    	}
                    }
                }

                CConstantesLicencias_LCGIII.referencia.setNombreVia((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 2));
                CConstantesLicencias_LCGIII.referencia.setPrimerNumero((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 3));
                CConstantesLicencias_LCGIII.referencia.setPrimeraLetra((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 4));
                CConstantesLicencias_LCGIII.referencia.setBloque((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 5));
                CConstantesLicencias_LCGIII.referencia.setEscalera((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 6));
                CConstantesLicencias_LCGIII.referencia.setPlanta((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 7));
                CConstantesLicencias_LCGIII.referencia.setPuerta((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 8));
                CConstantesLicencias_LCGIII.referencia.setCPostal((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 9));
                CUtilidadesComponentes.showDatosReferenciaCatastralDialog(desktop, true, CMainLicencias.literales);

                if (CConstantesLicencias.esDireccionEmplazamiento){
                    referenciaJTextField.setText((String)referenciasCatastralesJTable.getValueAt(selectedRow, 0));

                    tipoViaINEEJCBox.setSelectedPatron(patron);

                    nombreViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 2));
                    numeroViaNumberTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 3));
                    portalViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 5));
                    plantaViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 7));
                    letraViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 4));
                    // no existe el dato en la referencia catastral
                    cpostalViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 9));
                }
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}

	}//GEN-LAST:event_referenciasCatastralesJTableMouseClicked


	private void tableToMapJButtonActionPerformed() {//GEN-FIRST:event_tableToMapJButtonActionPerformed

		refreshFeatureSelection();

	}//GEN-LAST:event_tableToMapJButtonActionPerformed

    private void deleteRegistroCatastralJButtonActionPerformed() {//GEN-FIRST:event_deleteRegistroCatastralJButtonActionPerformed


        int selectedRow = referenciasCatastralesJTable.getSelectedRow();
        logger.info("selectedRow: " + selectedRow);

        if (selectedRow != -1) {
            referenciasCatastralesJTableModel.removeRow(selectedRow);
            refreshFeatureSelection();
        }


    }//GEN-LAST:event_deleteRegistroCatastralJButtonActionPerformed


	private void MapToTableJButtonActionPerformed() {//GEN-FIRST:event_MapToTableJButtonActionPerformed

		Object[] options = {CMainLicencias.literales.getString("Licencias.mensaje3"), CMainLicencias.literales.getString("Licencias.mensaje4")};
		if (JOptionPane.showOptionDialog(this,
				CMainLicencias.literales.getString("Licencias.mensaje1"),
				CMainLicencias.literales.getString("Licencias.mensaje2"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, //don't use a custom Icon
				options, //the titles of buttons
				options[1])!=JOptionPane.OK_OPTION) return;

		CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
		Collection collection = CMainLicencias.geopistaEditor.getSelection();
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			Feature feature = (Feature) it.next();
			if (feature == null) {
				logger.error("feature: " + feature);
				continue;
			}
            CReferenciaCatastral referenciaCatastral = CUtilidadesComponentes.getReferenciaCatastral(feature);
            Object[]features= new Object[1];
            features[0]=feature;
			referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
                                                                  referenciaCatastral.getTipoVia(),
																  referenciaCatastral.getNombreVia(),
																  referenciaCatastral.getPrimerNumero(),
																  referenciaCatastral.getPrimeraLetra(),
																  referenciaCatastral.getBloque(),
																  referenciaCatastral.getEscalera(),
																  referenciaCatastral.getPlanta(),
																  referenciaCatastral.getPuerta(),
                                                                  referenciaCatastral.getCPostal(),features});
		}


	}//GEN-LAST:event_MapToTableJButtonActionPerformed


	private void referenciaCatastralJButtonActionPerformed() {//GEN-FIRST:event_referenciaCatastralJButtonActionPerformed

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		CUtilidadesComponentes.showReferenciaCatastralDialog(desktop,CMainLicencias.literales);

		try {

         	Enumeration enumerationElement = CConstantesLicencias.referenciasCatastrales.elements();
			while (enumerationElement.hasMoreElements()) {

                CReferenciaCatastral referenciaCatastral= (CReferenciaCatastral)enumerationElement.nextElement();
            	referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
                                                                      referenciaCatastral.getTipoVia(),
																	  referenciaCatastral.getNombreVia(),
																	  referenciaCatastral.getPrimerNumero(),
																	  referenciaCatastral.getPrimeraLetra(),
																	  referenciaCatastral.getBloque(),
																	  referenciaCatastral.getEscalera(),
																	  referenciaCatastral.getPlanta(),
																	  referenciaCatastral.getPuerta(),
                                                                      referenciaCatastral.getCPostal(), CUtilidadesComponentes.getFeatureSearched(CMainLicencias.geopistaEditor, referenciaCatastral.getReferenciaCatastral())});
  			}
			refreshFeatureSelection();
		} catch (Exception ex) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
		}


		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


	}//GEN-LAST:event_referenciaCatastralJButtonActionPerformed


	private boolean refreshFeatureSelection() {

		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			CMainLicencias.geopistaEditor.getSelectionManager().clear();

			GeopistaLayer geopistaLayer = (GeopistaLayer) CMainLicencias.geopistaEditor.getLayerManager().getLayer("parcelas");

            for (int i=0; i < referenciasCatastralesJTable.getModel().getRowCount(); i++){
                String refCatastral= (String) referenciasCatastralesJTable.getValueAt(i,0);
                Collection collection = CUtilidadesComponentes.searchByAttribute(geopistaLayer, "Referencia catastral", refCatastral);
				Iterator it = collection.iterator();
				if (it.hasNext()) {
					Feature feature = (Feature) it.next();
					CMainLicencias.geopistaEditor.select(geopistaLayer, feature);
				}
			}

			CMainLicencias.geopistaEditor.zoomToSelected();

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return true;
		} catch (Exception ex) {

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}

	}


	private void nombreJButtonActionPerformed() {//GEN-FIRST:event_nombreJButtonActionPerformed


		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		CUtilidadesComponentes.showAddressDialog(desktop, CMainLicencias.literales);

		try {
             Enumeration enumerationElement = CConstantesLicencias.referenciasCatastrales.elements();
            while (enumerationElement.hasMoreElements()){
                CReferenciaCatastral referenciaCatastral= (CReferenciaCatastral)enumerationElement.nextElement();
       			referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
                                                                      referenciaCatastral.getTipoVia(),
																	  referenciaCatastral.getNombreVia(),
																	  referenciaCatastral.getPrimerNumero(),
																	  referenciaCatastral.getPrimeraLetra(),
																	  referenciaCatastral.getBloque(),
																	  referenciaCatastral.getEscalera(),
																	  referenciaCatastral.getPlanta(),
																	  referenciaCatastral.getPuerta(),
                                                                      referenciaCatastral.getCPostal(), CUtilidadesComponentes.getFeatureSearched(CMainLicencias.geopistaEditor, referenciaCatastral.getReferenciaCatastral())});
      		}
			refreshFeatureSelection();

		} catch (Exception ex) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
		}
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}//GEN-LAST:event_nombreJButtonActionPerformed

	private void buscarDNIPromotorJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIPromotorJButtonActionPerformed

		logger.info("Inicio.");

		CUtilidadesComponentes.showPersonaDialog(desktop, CMainLicencias.literales);

		if ((CConstantesLicencias_LCGIII.persona != null) && (CConstantesLicencias_LCGIII.persona.getDniCif() != null)) {
            DNIPromotorTField.setText(CConstantesLicencias_LCGIII.persona.getDniCif());
            nombrePromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getNombre());
            apellido1PromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido1());
            apellido2PromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido2());
            colegioPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getColegio());
            visadoPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getVisado());
            titulacionPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getTitulacion());
            faxPromotorTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getFax());
            telefonoPromotorTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTelefono());
            movilPromotorTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMovil());
            emailPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionPromotorEJCBox.setSelectedPatron(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionPromotorEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionPromotorEJCBox.setSelectedPatron(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionPromotorEJCBox.setSelected(auxNode.getIdNode());
            }

            nombreViaPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNombreVia());
            numeroViaPromotorTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNumeroVia());
            plantaPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPlanta());
            portalPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPortal());
            escaleraPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEscalera());
            letraPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getLetra());
            cPostalPromotorTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getCpostal());
            municipioPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMunicipio());
            provinciaPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getProvincia());
            /*
            if (CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarPromotorJCheckBox.setSelected(true);
            else notificarPromotorJCheckBox.setSelected(false);
            */
		}


	}//GEN-LAST:event_buscarDNIPromotorJButtonActionPerformed

	private void buscarDNIRepresentaAJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIRepresentaAJButtonActionPerformed

		logger.info("Inicio.");

		CUtilidadesComponentes.showPersonaDialog(desktop, CMainLicencias.literales);

		if ((CConstantesLicencias_LCGIII.persona != null) && (CConstantesLicencias_LCGIII.persona.getDniCif() != null)) {
            DNIRepresentaATField.setText(CConstantesLicencias_LCGIII.persona.getDniCif());
            nombreRepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getNombre());
            apellido1RepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido1());
            apellido2RepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido2());
            faxRepresentaATField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getFax());
            telefonoRepresentaATField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTelefono());
            movilRepresentaATField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMovil());
            emailRepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionRepresentaAEJCBox.setSelectedPatron(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionRepresentaAEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionRepresentaAEJCBox.setSelectedPatron(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionRepresentaAEJCBox.setSelected(auxNode.getIdNode());
            }

            nombreViaRepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNombreVia());
            numeroViaRepresentaATField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNumeroVia());
            plantaRepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPlanta());
            portalRepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPortal());
            escaleraRepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEscalera());
            letraRepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getLetra());
            cPostalRepresentaATField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getCpostal());
            municipioRepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMunicipio());
            provinciaRepresentaAJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getProvincia());
            /*
            if (CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarRepresentaAJCheckBox.setSelected(true);
            else notificarRepresentaAJCheckBox.setSelected(false);
            */
		}


	}//GEN-LAST:event_buscarDNIRepresentaAJButtonActionPerformed

	private void buscarDNITitularJButtonActionPerformed() {//GEN-FIRST:event_buscarDNITitularJButtonActionPerformed

		logger.info("Inicio.");

		CUtilidadesComponentes.showPersonaDialog(desktop, CMainLicencias.literales);

		if ((CConstantesLicencias_LCGIII.persona != null) && (CConstantesLicencias_LCGIII.persona.getDniCif() != null)) {

            DNITitularTField.setText(CConstantesLicencias_LCGIII.persona.getDniCif());
            nombreTitularJTField.setText(CConstantesLicencias_LCGIII.persona.getNombre());
            apellido1TitularJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido1());
            apellido2TitularJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido2());
            faxTitularTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getFax());
            telefonoTitularTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTelefono());
            movilTitularTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMovil());
            emailTitularJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionTitularEJCBox.setSelectedPatron(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionTitularEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionTitularEJCBox.setSelectedPatron(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionTitularEJCBox.setSelected(auxNode.getIdNode());
            }
            
            nombreViaTitularJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNombreVia());
            numeroViaTitularTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNumeroVia());
            plantaTitularJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPlanta());
            portalTitularJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPortal());
            escaleraTitularJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEscalera());
            letraTitularJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getLetra());
            cPostalTitularTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getCpostal());
            municipioTitularJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMunicipio());
            provinciaTitularJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getProvincia());
            /*
            if (CConstantesLicencias.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarTitularJCheckBox.setSelected(true);
            else notificarTitularJCheckBox.setSelected(false);
            */

		}

	}//GEN-LAST:event_buscarDNITitularJButtonActionPerformed


	private void fechaSolicitudJButtonActionPerformed() {//GEN-FIRST:event_fechaSolicitudJButtonActionPerformed


		logger.info("Inicio.");
		CUtilidadesComponentes.showCalendarDialog(desktop);

		if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
			fechaSolicitudJTField.setText(CConstantesLicencias.calendarValue);
		}

	}//GEN-LAST:event_fechaSolicitudJButtonActionPerformed


	private void cancelarJButtonMouseClicked() {//GEN-FIRST:event_cancelarJButtonMouseClicked
		CConstantesLicencias.helpSetHomeID= "licenciasIntro";
		this.dispose();
	}//GEN-LAST:event_cancelarJButtonMouseClicked


	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

// Get the size of the file
		long length = file.length();

// You cannot create an array using a long type.
// It needs to be an int type.
// Before converting to an int type, check
// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
// File is too large
		}

// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

// Close the input stream and return bytes
		is.close();
		return bytes;
	}


	private void aceptarJButtonMouseClicked() {//GEN-FIRST:event_aceptarJButtonMouseClicked
        try{
            if (rellenaCamposObligatorios()){ 
            	if(tecnicosJPanel.hayTecnicos())
            	{
            		
                /** Comprobamos los datos de entrada */

                /** Datos del propietario, representante, tecnico y promotor */
                if (datosTitularOK() && datosRepresentaAOK() && datosPromotorOK()) {
                    /** Datos de la solicitud */
                    int index = estadoJCBox.getSelectedIndex();
                    CEstado estado = (CEstado) _hEstado.get(new Integer(index));

    // TRAZAS-----------------------------------------
    /*
    System.out.println("indexSelected="+_estado + " valorSelected="+estadoJCBox.getSelectedItem());
    for (int t=0; t<_vEstado.size(); t++){
         CEstado e= (CEstado)_vEstado.get(t);
         System.out.println("t="+t+ " valor="+e.getDescripcion());
    }
    */
    //-------------------------------------------------
                    CTipoObra tipoObra = new CTipoObra((new Integer(tipoObraEJCBox.getSelectedPatron())).intValue(), "", "");

                    _unidadRegistro = unidadRJTField.getText();
                    _unidadTramitadora = unidadTJTField.getText();
                    if (excedeLongitud(_unidadRegistro, CConstantesLicencias.MaxLengthUnidad) || excedeLongitud(_unidadTramitadora, CConstantesLicencias.MaxLengthUnidad)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje1"));
                        return;
                    }

                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    //***********************************************
    //** Datos del titular
    //*****************************************

                    /* Recogemos los datos JNumberTextField */
                    String telefono= "";
                    String movil= "";
                    String fax= "";
                    String numeroVia= "";
                    String cPostal= "";

                     try{
                         telefono= telefonoTitularTField.getNumber().toString();
                     }catch (Exception e){}
                     try{
                         movil= movilTitularTField.getNumber().toString();
                     }catch (Exception e){}
                     try{
                         fax= faxTitularTField.getNumber().toString();
                     }catch (Exception e){}
                     try{
                         numeroVia= numeroViaTitularTField.getNumber().toString();
                     }catch (Exception e){}
                     try{
                         cPostal= cPostalTitularTField.getText().toString();
                     }catch (Exception e){}


                    int viaNotificacionIndex = new Integer(viaNotificacionTitularEJCBox.getSelectedPatron()).intValue();
                    CViaNotificacion viaNotificacion= new CViaNotificacion(viaNotificacionIndex, "");
                    if ((_seNotificaRepresentaA == 0) && (_seNotificaPromotor == 0))
                        _seNotificaTitular= 1;

                    CDatosNotificacion datosNotificacion = new CDatosNotificacion(DNITitularTField.getText(),
                            viaNotificacion,
                            fax,
                            telefono,
                            movil,
                            emailTitularJTField.getText().trim(),
                            tipoViaNotificacionTitularEJCBox.getSelectedPatron(),
                            nombreViaTitularJTField.getText().trim(),
                            numeroVia,
                            portalTitularJTField.getText().trim(),
                            plantaTitularJTField.getText().trim(),
                            escaleraTitularJTField.getText().trim(),
                            letraTitularJTField.getText().trim(),
                            cPostal,
                            municipioTitularJTField.getText().trim(),
                            provinciaTitularJTField.getText().trim(),
                            new Integer(_seNotificaTitular).toString());

                    logger.info("viaNotificacion.getIdViaNotificacion(): " + viaNotificacion.getIdViaNotificacion());
                    logger.info("datosNotificacion.getNotificar(): " + datosNotificacion.getNotificar());

                    CPersonaJuridicoFisica propietario = new CPersonaJuridicoFisica(DNITitularTField.getText(),
                            nombreTitularJTField.getText().trim(),
                            apellido1TitularJTField.getText().trim(),
                            apellido2TitularJTField.getText().trim(),
                            "",
                            "",
                            "",
                            datosNotificacion);



    //***********************************************
    //** Datos del Representante
    //*****************************************

                    CPersonaJuridicoFisica representante= new CPersonaJuridicoFisica();

                    if (_flagRepresentante){

                        /* Recogemos los datos JNumberTextField */
                        telefono= "";
                        movil= "";
                        fax= "";
                        numeroVia= "";
                        cPostal= "";

                         try{
                             telefono= telefonoRepresentaATField.getNumber().toString();
                         }catch (Exception e){}
                         try{
                             movil= movilRepresentaATField.getNumber().toString();
                         }catch (Exception e){}
                         try{
                             fax= faxRepresentaATField.getNumber().toString();
                         }catch (Exception e){}
                         try{
                             numeroVia= numeroViaRepresentaATField.getNumber().toString();
                         }catch (Exception e){}
                         try{
                             cPostal= cPostalRepresentaATField.getNumber().toString();
                         }catch (Exception e){}

                        viaNotificacionIndex = new Integer(viaNotificacionRepresentaAEJCBox.getSelectedPatron()).intValue();
                        viaNotificacion= new CViaNotificacion(viaNotificacionIndex, "");

                        datosNotificacion = new CDatosNotificacion(DNIRepresentaATField.getText().trim(),
                                viaNotificacion,
                                fax,
                                telefono,
                                movil,
                                emailRepresentaAJTField.getText().trim(),
                                tipoViaNotificacionRepresentaAEJCBox.getSelectedPatron(),
                                nombreViaRepresentaAJTField.getText().trim(),
                                numeroVia,
                                portalRepresentaAJTField.getText().trim(),
                                plantaRepresentaAJTField.getText().trim(),
                                escaleraRepresentaAJTField.getText().trim(),
                                letraRepresentaAJTField.getText().trim(),
                                cPostal,
                                municipioRepresentaAJTField.getText().trim(),
                                provinciaRepresentaAJTField.getText().trim(),
                                (notificarRepresentaAJCheckBox.isSelected() ? "1" : "0"));

                        logger.info("viaNotificacion.getIdViaNotificacion(): " + viaNotificacion.getIdViaNotificacion());
                        logger.info("datosNotificacion.getNotificar(): " + datosNotificacion.getNotificar());

                        representante = new CPersonaJuridicoFisica(DNIRepresentaATField.getText().trim(),
                                nombreRepresentaAJTField.getText().trim(),
                                apellido1RepresentaAJTField.getText().trim(),
                                apellido2RepresentaAJTField.getText().trim(),
                                "",
                                "",
                                "",
                                datosNotificacion);
                    }else representante= null;


    //***********************************************
    //** Datos del Promotor
    //*****************************************

                    /* Recogemos los datos JNumberTextField */
                    telefono= "";
                    movil= "";
                    fax= "";
                    numeroVia= "";
                    cPostal= "";

                     try{
                         telefono= telefonoPromotorTField.getNumber().toString();
                     }catch (Exception e){}
                     try{
                         movil= movilPromotorTField.getNumber().toString();
                     }catch (Exception e){}
                     try{
                         fax= faxPromotorTField.getNumber().toString();
                     }catch (Exception e){}
                     try{
                         numeroVia= numeroViaPromotorTField.getNumber().toString();
                     }catch (Exception e){}
                     try{
                         cPostal= cPostalPromotorTField.getNumber().toString();
                     }catch (Exception e){}

                    viaNotificacionIndex = new Integer(viaNotificacionPromotorEJCBox.getSelectedPatron()).intValue();
                    viaNotificacion= new CViaNotificacion(viaNotificacionIndex, "");

                    datosNotificacion = new CDatosNotificacion(DNIPromotorTField.getText().trim(),
                            viaNotificacion,
                            fax,
                            telefono,
                            movil,
                            emailPromotorJTField.getText().trim(),
                            tipoViaNotificacionPromotorEJCBox.getSelectedPatron(),
                            nombreViaPromotorJTField.getText().trim(),
                            numeroVia,
                            portalPromotorJTField.getText().trim(),
                            plantaPromotorJTField.getText().trim(),
                            escaleraPromotorJTField.getText().trim(),
                            letraPromotorJTField.getText().trim(),
                            cPostal,
                            municipioPromotorJTField.getText().trim(),
                            provinciaPromotorJTField.getText().trim(),
                            (notificarPromotorJCheckBox.isSelected() ? "1" : "0"));


                    logger.info("viaNotificacion.getIdViaNotificacion(): " + viaNotificacion.getIdViaNotificacion());
                    logger.info("datosNotificacion.getNotificar(): " + datosNotificacion.getNotificar());

                    CPersonaJuridicoFisica promotor = new CPersonaJuridicoFisica(DNIPromotorTField.getText().trim(),
                            nombrePromotorJTField.getText().trim(),
                            apellido1PromotorJTField.getText().trim(),
                            apellido2PromotorJTField.getText().trim(),
                            colegioPromotorJTField.getText().trim(),
                            visadoPromotorJTField.getText().trim(),
                            titulacionPromotorJTField.getText().trim(),
                            datosNotificacion);


                    CTipoLicencia tipoLicencia = new CTipoLicencia(CConstantesLicencias.ObraMayor, "", "");
                    CExpedienteLicencia expedienteLicencia = new CExpedienteLicencia(estado);

                    //Añadimos los parámetros de la configuracion
                     expedienteLicencia.setServicioEncargado(JDialogConfiguracion.getServicio());
                     expedienteLicencia.setFormaInicio(JDialogConfiguracion.getInicio());
                     expedienteLicencia.setResponsable(JDialogConfiguracion.getResponsable());

                    /** REFERENCIAS CATASTRALES */
                    Vector referenciasCatastrales = new Vector();
                    for (int i = 0; i < referenciasCatastralesJTable.getRowCount(); i++) {

                        /** tipoVia */
                        String tipoVia= null;
                        Object objeto=referenciasCatastralesJTableModel.getValueAt(i, 1);
                        if ((objeto instanceof DomainNode) && objeto!=null)
                        {
                        	                       	
                            CConstantesLicencias_LCGIII.referencia.setTipoVia(((DomainNode)objeto).getTerm(CMainLicencias.literales.getLocale().toString()));
                            tipoVia=((DomainNode)objeto).getPatron();
                        }
                        if ((objeto instanceof String) && objeto!=null)
                        {
                            if (((String)objeto).length()>0)
                            {
                            	
                            	if(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto)==null){
                            		CConstantesLicencias_LCGIII.referencia.setTipoVia("No Especificado");
                            		tipoVia="NE";
                            	}
                            	
                            	
                            	else {                             	
                                CConstantesLicencias_LCGIII.referencia.setTipoVia(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto).getTerm(CMainLicencias.literales.getLocale().toString()));
                                tipoVia=((String)objeto);
                            	}
                            }
                        }

                        String ref_Catastral = (String)referenciasCatastralesJTable.getValueAt(i, 0);

                        String nombre= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 2)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 2)).trim().equalsIgnoreCase(""))){
                           nombre= ((String)referenciasCatastralesJTable.getValueAt(i, 2)).trim();
                        }
                        String numero= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 3)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 3)).trim().equalsIgnoreCase(""))){
                           numero= ((String)referenciasCatastralesJTable.getValueAt(i, 3)).trim();
                        }
                        String letra= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 4)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 4)).trim().equalsIgnoreCase(""))){
                           letra= ((String)referenciasCatastralesJTable.getValueAt(i, 4)).trim();
                        }
                        String bloque= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 5)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 5)).trim().equalsIgnoreCase(""))){
                           bloque= ((String)referenciasCatastralesJTable.getValueAt(i, 5)).trim();
                        }
                        String escalera= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 6)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 6)).trim().equalsIgnoreCase(""))){
                           escalera= ((String)referenciasCatastralesJTable.getValueAt(i, 6)).trim();
                        }
                        String planta= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 7)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 7)).trim().equalsIgnoreCase(""))){
                           planta= ((String)referenciasCatastralesJTable.getValueAt(i, 7)).trim();
                        }
                        String puerta= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 8)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 8)).trim().equalsIgnoreCase(""))){
                           puerta= ((String)referenciasCatastralesJTable.getValueAt(i, 8)).trim();
                        }
                        String cpostal= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 9)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 9)).trim().equalsIgnoreCase(""))){
                           cpostal= ((String)referenciasCatastralesJTable.getValueAt(i, 9)).trim();
                        }


                        CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral(ref_Catastral, tipoVia, nombre, numero, letra, bloque, escalera, planta, puerta);
                        referenciaCatastral.setCPostal(cpostal);

                        referenciasCatastrales.add(referenciaCatastral);

                    }

                    double tasa = 0.00;
                    try {
                        tasa= tasaTextField.getNumber().doubleValue();
                    } catch (Exception ex) {
                        logger.warn("Tasa no válida. tasaJTextField.getText(): " + tasaTextField.getText());
                    }

                    double impuesto = 0.00;
                    try {
                        impuesto= impuestoTextField.getNumber().doubleValue();
                    } catch (Exception ex) {
                        logger.warn("Tasa no válida. impuestoTextField.getText(): " + impuestoTextField.getText());
                    }

                    /** Emplazamiento */
                    String tipoViaINE= null;
                    if (!tipoViaINEEJCBox.getSelectedPatron().equalsIgnoreCase("-1")){
                          tipoViaINE= tipoViaINEEJCBox.getSelectedPatron();
                    }
                    String nombreViaEmplazamiento= null;
                    if (nombreViaTField.getText().trim().length() > 0){
                        nombreViaEmplazamiento= nombreViaTField.getText().trim();
                    }                    
                    String numeroViaEmplazamiento= null;
                    try{
                        //numeroViaEmplazamiento= numeroViaNumberTField.getNumber().toString();
                        numeroViaEmplazamiento= numeroViaNumberTField.getText().trim();
                    }catch(Exception e){}
                    String plantaViaEmplazamiento= null;
                    if (plantaViaTField.getText().trim().length() > 0){
                        plantaViaEmplazamiento= plantaViaTField.getText().trim();
                    }
                    String portalViaEmplazamiento= null;
                    if (portalViaTField.getText().trim().length() > 0){
                        portalViaEmplazamiento= portalViaTField.getText().trim();
                    }
                    String letraViaEmplazamiento= null;
                    if (letraViaTField.getText().trim().length() > 0){
                        letraViaEmplazamiento= letraViaTField.getText().trim();
                    }
                    String cPostalEmplazamiento= null;
                    try{
                        cPostalEmplazamiento= cpostalViaTField.getNumber().toString();
                    }catch(Exception e){}

                    CSolicitudLicencia solicitudLicencia = new CSolicitudLicencia(tipoLicencia,
                            tipoObra,
                            propietario,
                            representante,
                            null,
                            promotor,
                            "",
                            "",
                            unidadTJTField.getText(),
                            unidadRJTField.getText(),
                            motivoTField.getText(),
                            asuntoTField.getText(),
                            CUtilidades.getDate(fechaSolicitudJTField.getText()),
                            tasa,
                            tipoViaINE,
                            nombreViaEmplazamiento,
                            numeroViaEmplazamiento,
                            portalViaEmplazamiento,
                            plantaViaEmplazamiento,
                            letraViaEmplazamiento,
                            cPostalEmplazamiento,
                            municipioJTField.getText(),
                            provinciaJTField.getText(),
                            observacionesTPane.getText(),
                            documentacionJPanel.getAnexos(),
                            referenciasCatastrales);

                    /** Documentacion entregada de caracter obligatorio */
                    solicitudLicencia.setDocumentacionEntregada(documentacionJPanel.getDocumentacionObligatoriaSeleccionada());
                    solicitudLicencia.setObservacionesDocumentacionEntregada(documentacionJPanel.getObservacionesGenerales());

                    /* impuesto */
                    solicitudLicencia.setImpuesto(impuesto);

                    /* tecnicos */
                    solicitudLicencia.setTecnicos(tecnicosJPanel.getListaTecnicos());

                    /* fecha resolucion */
                    solicitudLicencia.setFechaResolucion(null);

                    /** fecha limite de obra */
                    Date dateFechaLimite= CUtilidades.parseFechaStringToDate(fechaLimiteObraJTField.getText());
                    solicitudLicencia.setFechaLimiteObra(dateFechaLimite);

                    CResultadoOperacion resultado = COperacionesLicencias.crearExpediente(solicitudLicencia, expedienteLicencia);
                    logger.info("resultado.getResultado(): " + resultado.getResultado());
                    logger.info("resultado.getDescripcion(): " + resultado.getDescripcion());

                    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    if (resultado.getResultado()){
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje16")+ " " + resultado.getDescripcion());
                        CUtilidadesComponentes.addFeatureCapa(CMainLicencias.geopistaEditor, "licencias_obra_mayor", referenciasCatastralesJTable, referenciasCatastralesJTableModel);
                        refreshFeatureSelection();
                        clearAll();
                        tecnicosJPanel.clearTecnicosJPanel(true);
                        tecnicosJPanel.inicializarBotonera();
                    }
                    else{
                        /** Comprobamos que no se haya excedido el maximo FileUploadBase.SizeLimitExceededException */
                       if (resultado.getDescripcion().equalsIgnoreCase("FileUploadBase.SizeLimitExceededException")){
                           JOptionPane optionPane= new JOptionPane(CMainLicencias.literales.getString("DocumentacionLicenciasJPanel.mensaje4"), JOptionPane.ERROR_MESSAGE);
                           JDialog dialog =optionPane.createDialog(this,"ERROR");
                           dialog.show();
                       }else{
                         JOptionPane optionPane= new JOptionPane("Error al crear la licencia.\n"+resultado.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                         JDialog dialog =optionPane.createDialog(this,"ERROR");
                         dialog.show();
                       }
                    }
                  }
            	}
            	else
            	{
            		obraMayorJTabbedPane.setSelectedIndex(0);
            		jTabbedPaneSolicitud.setSelectedIndex(3);
            		mostrarMensaje(CMainLicencias.literales.getString("TecnicosJPanel.mensaje2"));
            	}
            } else {
                if (!rellenaCamposObligatorios()){
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje17"));
                
                    
                }
            }
            	
        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }

	}//GEN-LAST:event_aceptarJButtonMouseClicked

	/*******************************************************************/
    /*                         Metodos propios                         */
	/*******************************************************************/
	public boolean datosTitularOK() {

        try{
            if (excedeLongitud(_nombreTitular, CConstantesLicencias.MaxLengthNombre)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje2"));
                return false;
            }
            if (excedeLongitud(_apellido1Titular, CConstantesLicencias.MaxLengthApellido) || excedeLongitud(_apellido2Titular, CConstantesLicencias.MaxLengthApellido)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje3"));
                return false;
            }
            _emailTitular = emailTitularJTField.getText();
            if (excedeLongitud(_emailTitular, CConstantesLicencias.MaxLengthEmail)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje6"));
                return false;
            }
            _nombreViaTitular = nombreViaTitularJTField.getText();
            if (excedeLongitud(_nombreViaTitular, CConstantesLicencias.MaxLengthNombreVia)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje8"));
                return false;
            }
            _portalTitular = portalTitularJTField.getText();
            if (_portalTitular.length() > 0) {
                if (excedeLongitud(_portalTitular, CConstantesLicencias.MaxLengthPortal)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje11"));
                    return false;
                }
            }
            _plantaTitular = plantaTitularJTField.getText();
            if (_plantaTitular.length() > 0) {
                if (excedeLongitud(_plantaTitular, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje13"));
                    return false;
                }
            }
            _escaleraTitular = escaleraTitularJTField.getText();
            if (excedeLongitud(_escaleraTitular, CConstantesLicencias.MaxLengthPlanta)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje14"));
                return false;
            }
            _letraTitular = letraTitularJTField.getText();
            if (excedeLongitud(_letraTitular, CConstantesLicencias.MaxLengthLetra)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje15"));
                return false;
            }

            _municipioTitular = municipioTitularJTField.getText();
            if (excedeLongitud(_municipioTitular, CConstantesLicencias.MaxLengthMunicipio)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje18"));
                return false;
            }

            _provinciaTitular = provinciaTitularJTField.getText();
            if (excedeLongitud(_provinciaTitular, CConstantesLicencias.MaxLengthProvincia)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje19"));
                return false;
            }

            boolean notificar = notificarTitularJCheckBox.isSelected();
            if (notificar)
                _seNotificaTitular = 1;
            else
                _seNotificaTitular = 0;

        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }

		return true;
	}

    /**
     * Datos Representante
     */

	public boolean datosRepresentaAOK() {

        try{
            /** Comprobamos que el propietario tenga representante */
            if (_flagRepresentante){
                if (excedeLongitud(_nombreRepresentaA, CConstantesLicencias.MaxLengthNombre)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje2"));
                    return false;
                }
                if (excedeLongitud(_apellido1RepresentaA, CConstantesLicencias.MaxLengthApellido) || excedeLongitud(_apellido2RepresentaA, CConstantesLicencias.MaxLengthApellido)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje3"));
                    return false;
                }
                _emailRepresentaA = emailRepresentaAJTField.getText();
                if (excedeLongitud(_emailRepresentaA, CConstantesLicencias.MaxLengthEmail)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje6"));
                    return false;
                }
                _nombreViaRepresentaA = nombreViaRepresentaAJTField.getText();
                if (excedeLongitud(_nombreViaRepresentaA, CConstantesLicencias.MaxLengthNombreVia)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje8"));
                    return false;
                }
                _portalRepresentaA = portalRepresentaAJTField.getText();
                if (_portalRepresentaA.length() > 0) {
                    if (excedeLongitud(_portalRepresentaA, CConstantesLicencias.MaxLengthPortal)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje11"));
                        return false;
                    }
                }
                _plantaRepresentaA = plantaRepresentaAJTField.getText();
                if (_plantaRepresentaA.length() > 0) {
                    if (excedeLongitud(_plantaRepresentaA, CConstantesLicencias.MaxLengthPlanta)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje13"));
                        return false;
                    }
                }
                _escaleraRepresentaA = escaleraRepresentaAJTField.getText();
                if (excedeLongitud(_escaleraRepresentaA, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje14"));
                    return false;
                }
                _letraRepresentaA = letraRepresentaAJTField.getText();
                if (excedeLongitud(_letraRepresentaA, CConstantesLicencias.MaxLengthLetra)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje15"));
                    return false;
                }

                _municipioRepresentaA = municipioRepresentaAJTField.getText();
                if (excedeLongitud(_municipioRepresentaA, CConstantesLicencias.MaxLengthMunicipio)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje18"));
                    return false;
                }

                _provinciaRepresentaA = provinciaRepresentaAJTField.getText();
                if (excedeLongitud(_provinciaRepresentaA, CConstantesLicencias.MaxLengthProvincia)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje19"));
                    return false;
                }
            }else{
                borrarCamposRepresentante();
            }

            boolean notificar = notificarRepresentaAJCheckBox.isSelected();
            if (notificar)
                _seNotificaRepresentaA = 1;
            else
                _seNotificaRepresentaA = 0;

        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }

		return true;
	}

    public void borrarCamposRepresentante() {
        try {
            // Actualizamos campos
            DNIRepresentaATField.setText("");
            nombreRepresentaAJTField.setText("");
            apellido1RepresentaAJTField.setText("");
            apellido2RepresentaAJTField.setText("");
            faxRepresentaATField.setText("");
            telefonoRepresentaATField.setText("");
            movilRepresentaATField.setText("");
            emailRepresentaAJTField.setText("");
            nombreViaRepresentaAJTField.setText("");
            numeroViaRepresentaATField.setText("");
            plantaRepresentaAJTField.setText("");
            letraRepresentaAJTField.setText("");
            portalRepresentaAJTField.setText("");
            escaleraRepresentaAJTField.setText("");
            cPostalRepresentaATField.setText("");
            municipioRepresentaAJTField.setText("");
            provinciaRepresentaAJTField.setText("");
            notificarRepresentaAJCheckBox.setSelected(false);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }


    /**
     * Datos Promotor
     */

	public boolean datosPromotorOK() {

        try{
            if (excedeLongitud(_nombrePromotor, CConstantesLicencias.MaxLengthNombre)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje2"));
                return false;
            }
            if (excedeLongitud(_apellido1Promotor, CConstantesLicencias.MaxLengthApellido) || excedeLongitud(_apellido2Promotor, CConstantesLicencias.MaxLengthApellido)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje3"));
                return false;
            }
            if (excedeLongitud(_colegioPromotor, CConstantesLicencias.MaxLengthColegio)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje4"));
                return false;
            }
            if (excedeLongitud(_visadoPromotor, CConstantesLicencias.MaxLengthVisado)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje5"));
                return false;
            }
            if (excedeLongitud(_titulacionPromotor, CConstantesLicencias.MaxLengthTitulacion)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje6"));
                return false;
            }

            _emailPromotor = emailPromotorJTField.getText();
            if (excedeLongitud(_emailPromotor, CConstantesLicencias.MaxLengthEmail)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje9"));
                return false;
            }
            _nombreViaPromotor = nombreViaPromotorJTField.getText();
            if (excedeLongitud(_nombreViaPromotor, CConstantesLicencias.MaxLengthNombreVia)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje11"));
                return false;
            }
            _portalPromotor = portalPromotorJTField.getText();
            if (_portalPromotor.length() > 0) {
                if (excedeLongitud(_portalPromotor, CConstantesLicencias.MaxLengthPortal)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje14"));
                    return false;
                }
            }

            _plantaPromotor = plantaPromotorJTField.getText();
            if (_plantaPromotor.length() > 0) {
                if (excedeLongitud(_plantaPromotor, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje16"));
                    return false;
                }
            }
            _escaleraPromotor = escaleraPromotorJTField.getText();
            if (excedeLongitud(_escaleraPromotor, CConstantesLicencias.MaxLengthPlanta)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje17"));
                return false;
            }
            _letraPromotor = letraPromotorJTField.getText();
            if (excedeLongitud(_letraPromotor, CConstantesLicencias.MaxLengthLetra)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje18"));
                return false;
            }
            _municipioPromotor = municipioPromotorJTField.getText();
            if (excedeLongitud(_municipioPromotor, CConstantesLicencias.MaxLengthMunicipio)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje21"));
                return false;
            }

            _provinciaPromotor = provinciaPromotorJTField.getText();
            if (excedeLongitud(_provinciaPromotor, CConstantesLicencias.MaxLengthProvincia)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PromotorTab.mensaje22"));
                return false;
            }
            boolean notificar = notificarPromotorJCheckBox.isSelected();
            if (notificar)
                _seNotificaPromotor = 1;
            else
                _seNotificaPromotor = 0;

        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }

		return true;
	}


	public void rellenarEstadoJCBox() {

		_vEstado = COperacionesLicencias.getEstadosDisponibles(null,CConstantesLicencias.ObraMayor);
		if (_vEstado != null) {
			Enumeration e = _vEstado.elements();
			int i = 0;
			while (e.hasMoreElements()) {
				CEstado estado = (CEstado) e.nextElement();
                DomainNode dominio= Estructuras.getListaEstados().getDomainNode(new Integer(estado.getIdEstado()).toString());
				estadoJCBox.addItem(dominio.getTerm(CMainLicencias.currentLocale.toString()));
				_hEstado.put(new Integer(i), estado);
				i++;
			}
		}
	}


	public boolean rellenaCamposObligatorios() {
		// Chequeamos que el usuario haya rellenado los campos obligatorios
		/** Comprobamos los datos del titular */
		// leemos los datos referentes al titular
        try{
       

            /** Presupuesto **/
            //presupuesto

            obraMayorJTabbedPane.setSelectedIndex(0);
            if (fechaSolicitudJTField.getText().trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(0); 
            	fechaSolicitudJTField.requestFocus();
             	return false;
            }
     		if (estadoJCBox.getSelectedIndex() == -1)
     		{
     			jTabbedPaneSolicitud.setSelectedIndex(0); 
     			estadoJCBox.requestFocus();
             	return false;
     		}
     		if (tipoObraEJCBox.getSelectedPatron() == null)
     		{
     			jTabbedPaneSolicitud.setSelectedIndex(0); 
     			tipoObraEJCBox.requestFocus();
             	return false;
     		}
     		if (tipoViaINEEJCBox.getSelectedPatron()=="-1")
     		{
     			jTabbedPaneSolicitud.setSelectedIndex(0); 
     			tipoViaINEEJCBox.requestFocus();
             	return false;
     		}
            if (nombreViaTField.getText().trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(0); 
            	nombreViaTField.requestFocus();
             	return false;
            }
           
            
            
            
            
            
            
            /** Comprobamos si el titular representa a otra peprsona juridico-fisica */
            _DNI_CIF_Titular = DNITitularTField.getText();
            _nombreTitular = nombreTitularJTField.getText();
            _nombreViaTitular = nombreViaTitularJTField.getText();
          
           
            try{
                _numeroViaTitular= numeroViaTitularTField.getNumber().toString();
            }catch(Exception e){
                _numeroViaTitular= "";
            }
            try{
                _cPostalTitular= cPostalTitularTField.toString();
            }catch(Exception e){
                _cPostalTitular= "";
            }
            _municipioTitular = municipioTitularJTField.getText();
            _provinciaTitular = provinciaTitularJTField.getText();
            if (_DNI_CIF_Titular.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(1); 
            	DNITitularTField.requestFocus();
            	return false;
            }
            if(_nombreTitular.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(1); 
            	nombreTitularJTField.requestFocus();
            	return false;
            }
        	if (_nombreViaTitular.trim().length() == 0) 
        	{
        		jTabbedPaneSolicitud.setSelectedIndex(1); 
        		nombreViaTitularJTField.requestFocus();
        		return false;
        	}
    		if(_numeroViaTitular.trim().length() == 0)
    		{
    			jTabbedPaneSolicitud.setSelectedIndex(1); 
    			numeroViaTitularTField.requestFocus();
    			return false;
    		}
    		if(_cPostalTitular.trim().length() == 0)
    		{
    			jTabbedPaneSolicitud.setSelectedIndex(1); 
    			cPostalTitularTField.requestFocus();
    			return false;
    		}
    		if(_municipioTitular.trim().length() == 0)
    		{
    			jTabbedPaneSolicitud.setSelectedIndex(1); 
    			municipioTitularJTField.requestFocus();
    			return false;
    		}
    		if(_provinciaTitular.trim().length() == 0)
    		{
    			jTabbedPaneSolicitud.setSelectedIndex(1); 
    			provinciaTitularJTField.requestFocus();
    			return false;
    		}
            /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
            if (emailTitularObligatorio)
            {
                if (emailTitularJTField.getText().trim().length() == 0)
                {
                	jTabbedPaneSolicitud.setSelectedIndex(1);
                	emailTitularJTField.requestFocus();
                	return false;
                }
            }
            /** Comprobamos los datos del representado */
            // leemos los datos referentes al representado
            _DNI_CIF_RepresentaA = DNIRepresentaATField.getText();
            /** si se inserta el DNI consideramos que el propietario tiene representante */
            if (_DNI_CIF_RepresentaA.trim().length() > 0) _flagRepresentante= true;
            else _flagRepresentante= false;
            if (_flagRepresentante){
                _nombreRepresentaA = nombreRepresentaAJTField.getText();
                _nombreViaRepresentaA = nombreViaRepresentaAJTField.getText();
                try{
                    _numeroViaRepresentaA= numeroViaRepresentaATField.getNumber().toString();
                }catch(Exception e){
                   _numeroViaRepresentaA= "";
                }
                try{
                    _cPostalRepresentaA= cPostalRepresentaATField.getNumber().toString();
                }catch(Exception e){
                    _cPostalRepresentaA= "";
                }
                _municipioRepresentaA = municipioRepresentaAJTField.getText();
                _provinciaRepresentaA = provinciaRepresentaAJTField.getText();
                if (_DNI_CIF_RepresentaA.trim().length() == 0)
                {
                	jTabbedPaneSolicitud.setSelectedIndex(2);
                	DNIRepresentaATField.requestFocus();
                	return false;
                }
                if(_nombreRepresentaA.trim().length() == 0)
                {
                	jTabbedPaneSolicitud.setSelectedIndex(2);
                	nombreRepresentaAJTField.requestFocus();
                	return false;
                }
                if(_nombreViaRepresentaA.trim().length() == 0)
                {
                	jTabbedPaneSolicitud.setSelectedIndex(2);
                	nombreViaRepresentaAJTField.requestFocus();
                	return false;
                }
                if(_cPostalRepresentaA.trim().length() == 0)
                {
                	jTabbedPaneSolicitud.setSelectedIndex(2);
                	cPostalRepresentaATField.requestFocus();
                	return false;
                }
                if(_municipioRepresentaA.trim().length() == 0)
                {
                	jTabbedPaneSolicitud.setSelectedIndex(2);
                	municipioRepresentaAJTField.requestFocus();
                	return false;
                }
                if(_provinciaRepresentaA.trim().length() == 0)
                {
                	jTabbedPaneSolicitud.setSelectedIndex(2);
                	provinciaRepresentaAJTField.requestFocus();
                	return false;
                }
                /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
                if (emailRepresentanteObligatorio){
                    if (emailRepresentaAJTField.getText().trim().length() == 0)
                    {
                    	jTabbedPaneSolicitud.setSelectedIndex(2);
                    	emailRepresentaAJTField.requestFocus();
                    	return false;
                    }
                }

            }

            /** Comprobamos los datos del promotor */
            // leemos los datos referentes al tecnico
            _DNI_CIF_Promotor = DNIPromotorTField.getText();
            _nombrePromotor = nombrePromotorJTField.getText();
            _colegioPromotor = colegioPromotorJTField.getText();
            _visadoPromotor = visadoPromotorJTField.getText();
            _titulacionPromotor = titulacionPromotorJTField.getText();
            _nombreViaPromotor = nombreViaPromotorJTField.getText();
            try{
                _numeroViaPromotor= numeroViaPromotorTField.getNumber().toString();
            }catch(Exception e){
                _numeroViaPromotor= "";
            }
            try{
                _cPostalPromotor= cPostalPromotorTField.getNumber().toString();
            }catch(Exception e){
                _cPostalPromotor= "";
            }
            _municipioPromotor = municipioPromotorJTField.getText();
            _provinciaPromotor = provinciaPromotorJTField.getText();

            /*
            if ((_DNI_CIF_Promotor.trim().length() == 0) || (_nombrePromotor.trim().length() == 0) ||
                    (_colegioPromotor.trim().length() == 0) || (_visadoPromotor.trim().length() == 0))
                return false;
            */

            if(_DNI_CIF_Promotor.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(4);
            	DNIPromotorTField.requestFocus();
            	return false;
            }
            if(_nombrePromotor.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(4);
            	nombrePromotorJTField.requestFocus();
            	return false;
            }
            if (_nombreViaPromotor.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(4);
            	nombreViaPromotorJTField.requestFocus();
            	return false;
            }
            if(_numeroViaPromotor.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(4);
            	numeroViaPromotorTField.requestFocus();
            	return false;
            }
            if(_cPostalPromotor.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(4);
            	cPostalPromotorTField.requestFocus();
            	return false;
            }
            if(_municipioPromotor.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(4);
            	municipioPromotorJTField.requestFocus();
            	return false;
            }
            if(_provinciaPromotor.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(4);
            	provinciaPromotorJTField.requestFocus();
            	return false;
            }
  
            /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
            if (emailPromotorObligatorio){
                if (emailPromotorJTField.getText().trim().length() == 0) 
                {
                	jTabbedPaneSolicitud.setSelectedIndex(4);
                	emailPromotorJTField.requestFocus();
                	return false;
                }
            }

            

        }catch (Exception e){
            return false;
        }

        return true;
	}

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(obraMayorJPanel, mensaje);
	}

	public boolean esNumerico(String s) {
		try {
			new Integer(s).intValue();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean esDate(String fecha) {
		try {
			Date date = CConstantesLicencias.df.parse(fecha);
			logger.info("Fecha Original: " + fecha);
			logger.info("Parsed date: " + date.toString());

		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public boolean excedeLongitud(String campo, int maxLengthPermitida) {
		if (campo.length() > maxLengthPermitida)
			return true;
		else
			return false;
	}

	public String showToday() {
		Calendar cal = new GregorianCalendar();
		Locale locale = Locale.getDefault();
		cal = Calendar.getInstance(TimeZone.getTimeZone(locale.toString()));
/*
System.out.println("locale.toString()="+locale.toString());
System.out.println("locale.getCountry()="+locale.getCountry());
System.out.println("YEAR="+cal.get(Calendar.YEAR));
System.out.println("MONTH="+cal.get(Calendar.MONTH));
System.out.println("DAY="+cal.get(Calendar.DAY_OF_MONTH));
*/
		int anno = cal.get(Calendar.YEAR);
		int mes = cal.get(Calendar.MONTH) + 1; // 0 == Enero
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		String sMes = "";
		String sDia = "";
		if (mes < 10)
			sMes = "0" + mes;
		else
			sMes = "" + mes;
		if (dia < 10)
			sDia = "0" + dia;
		else
			sDia = "" + dia;

		return sDia + "/" + sMes + "/" + anno;
	}


    public void renombrarComponentes(ResourceBundle literales) {

        try{

            if (CConstantesLicencias.selectedSubApp.equals(CConstantesLicencias.LicenciasObraMayor)){

                /** Pestannas */
                jTabbedPaneSolicitud.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.obraMayorJPanel.SubTitleTab")));
                jTabbedPaneSolicitud.setTitleAt(1, CUtilidadesComponentes.annadirAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.titularJPanel.TitleTab")));
                jTabbedPaneSolicitud.setTitleAt(2, literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.representaAJPanel.TitleTab"));
                jTabbedPaneSolicitud.setTitleAt(3, CUtilidadesComponentes.annadirAsterisco(literales.getString("TecnicosJPanel.TitleTab")));
                jTabbedPaneSolicitud.setTitleAt(4, CUtilidadesComponentes.annadirAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.promotorJPanel.TitleTab")));
                obraMayorJTabbedPane.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.obraMayorJPanel.TitleTab")));
                obraMayorJTabbedPane.setTitleAt(1, literales.getString("DocumentacionLicenciasJPanel.title"));

                /** Solicitud */
                setTitle(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.JInternalFrame.title"));
                datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosSolicitudJPanel.TitleBorder")));
                estadoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.estadoJLabel.text")));
                tipoObraJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tipoObraJLabel.text")));
                unidadTJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.unidadTJLabel.text"));
                unidadRJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.unidadRJLabel.text"));
                motivoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.motivoJLabel.text"));
                asuntoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.asuntoJLabel.text"));
                fechaSolicitudJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.fechaSolicitudJLabel.text")));
                fechaLimiteObraJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.fechaLimiteObraJLabel.text"));
                observacionesJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.observacionesJLabel.text"));
                unidadTJTField.setText(JDialogConfiguracion.getUnidadTramitadora());
                unidadRJTField.setText(JDialogConfiguracion.getUnidadRegistro());
                fechaSolicitudJTField.setText(showToday());
                tasaJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tasaJLabel.text"));
                impuestoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.impuestoJLabel.text"));
                emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emplazamientoJPanel.TitleBorder")));
                nombreViaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tipoViaJLabel.text"), literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreViaJLabel.text")));
                numeroViaJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.numeroViaJLabel.text"));
                cPostalJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cPostalJLabel.text"));
                provinciaJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.provinciaJLabel.text"));
                referenciaCatastralJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciaCatastralJLabel.text"));

                /** Propietario */
                datosPersonalesTitularJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosPersonalesTitularJPanel.TitleBorder")));
                DNITitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.DNITitularJLabel.text")));
                nombreTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreTitularJLabel.text")));
                apellido1TitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido1TitularJLabel.text"));
                apellido2TitularJLabel2.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido2TitularJLabel.text"));
                datosNotificacionTitularJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosNotificacionTitularJPanel.TitleBorder")));
                viaNotificacionTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.viaNotificacionTitularJLabel.text"));
                faxTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.faxTitularJLabel.text"));
                telefonoTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.telefonoTitularJLabel.text"));
                movilTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.movilTitularJLabel.text"));
                emailTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailTitularJLabel.text"));
                tipoViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tipoViaTitularJLabel.text")));
                nombreViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreViaTitularJLabel.text")));
                numeroViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.numeroViaTiularJLabel.text")));
                portalTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.portalTitularJLabel.text"));
                plantaTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.plantaTitularJLabel.text"));
                escaleraTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.escaleraTitularJLabel.text"));
                letraTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.letraTitularJLabel.text"));
                cPostalTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cPostalTitularJLabel.text")));
                municipioTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.municipioTitularJLabel.text")));
                provinciaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.provinciaTitularJLabel.text")));
                notificarTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.notificarTitularJLabel.text"));

                /** Representante */
                datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosPersonalesRepresentaAJPanel.TitleBorder")));
                DNIRepresenaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.DNIRepresentaAJLabel.text")));
                nombreRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreRepresentaAJLabel.text")));
                apellido1RepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido1RepresentaAJLabel.text"));
                apellido2RepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido2RepresentaAJLabel.text"));
                datosNotificacionRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosNotificacionRepresentaAJPanel.TitleBorder")));
                viaNotificacionRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.viaNotificacionRepresentaAJLabel.text"));
                faxRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.faxRepresentaAJLabel.text"));
                telefonoRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.telefonoRepresentaAJLabel.text"));
                movilRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.movilRepresentaAJLabel.text"));
                emailRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailRepresentaAJLabel.text"));
                tipoViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tipoViaRepresentaAJLabel.text")));
                nombreViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreViaRepresentaAJLabel.text")));
                numeroViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.numeroViaTiularJLabel.text")));
                portalRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.portalRepresentaAJLabel.text"));
                plantaRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.plantaRepresentaAJLabel.text"));
                escaleraRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.escaleraRepresentaAJLabel.text"));
                letraRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.letraRepresentaAJLabel.text"));
                cPostalRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cPostalRepresentaAJLabel.text")));
                municipioRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.municipioRepresentaAJLabel.text")));
                provinciaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.provinciaRepresentaAJLabel.text")));
                notificarRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.notificarRepresentaAJLabel.text"));

                /** Tecnicos */
                tecnicosJPanel.renombrarComponentes(literales);

                /** Promotor */
                datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosPersonalesPromotorJPanel.TitleBorder")));
                DNIPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.DNIPromotorJLabel.text")));
                nombrePromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombrePromotorJLabel.text")));
                apellido1PromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido1PromotorJLabel.text"));
                apellido2PromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido2PromotorJLabel.text"));
                colegioPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.colegioPromotorJLabel.text"));
                visadoPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.visadoPromotorJLabel.text"));
                titulacionPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.titulacionPromotorJLabel.text"));
                datosNotificacionPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosNotificacionPromotorJPanel.TitleBorder")));
                viaNotificacionPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.viaNotificacionPromotorJLabel.text"));
                faxPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.faxPromotorJLabel.text"));
                telefonoPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.telefonoPromotorJLabel.text"));
                movilPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.movilPromotorJLabel.text"));
                emailPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailPromotorJLabel.text"));
                tipoViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tipoViaPromotorJLabel.text")));
                nombreViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreViaPromotorJLabel.text")));
                numeroViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.numeroViaTiularJLabel.text")));
                portalPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.portalPromotorJLabel.text"));
                plantaPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.plantaPromotorJLabel.text"));
                escaleraPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.escaleraPromotorJLabel.text"));
                letraPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.letraPromotorJLabel.text"));
                cPostalPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cPostalPromotorJLabel.text")));
                municipioPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.municipioPromotorJLabel.text")));
                provinciaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.provinciaPromotorJLabel.text")));
                notificarPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.notificarPromotorJLabel.text"));

                /** Documentacion */
                documentacionJPanel.renombrarComponentes(literales);

                aceptarJButton.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.aceptarJButton.text"));
                cancelarJButton.setText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cancelarJButton.text"));
                editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.editorMapaJPanel.TitleBorder")));

                aceptarJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.aceptarJButton.toolTipText"));
                cancelarJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cancelarJButton.text"));

                /** Header tabla referenciasCatastrales */
                TableColumn tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(0);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column1"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column2"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(2);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column3"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(3);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column4"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(4);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column5"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(5);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column6"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(6);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column7"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(7);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column8"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(8);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column9"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(9);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column10"));

                fechaSolicitudJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.fechaSolicitudJButton.toolTipText"));
                nombreJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreJButton.toolTipText"));
                referenciaCatastralJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciaCatastralJButton.toolTipText"));
                MapToTableJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.MapToTableJButton.toolTipText"));
                deleteRegistroCatastralJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.deleteRegistroCatastralJButton.toolTipText"));
                tableToMapJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tableToMapJButton.toolTipText"));
                buscarDNITitularJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.buscarDNITitularJButton.toolTipText"));
                buscarDNIRepresentaAJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.buscarDNIRepresentaAJButton.toolTipText"));
                buscarDNIPromotorJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.buscarDNIPromotorJButton.toolTipText"));
                fechaLimiteObraJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.fechaLimiteObraJButton.toolTipText"));

            }

        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }




    public void clearAll() {

        try{
            /** Solicitud */
            unidadTJTField.setText(JDialogConfiguracion.getUnidadTramitadora());
            unidadRJTField.setText(JDialogConfiguracion.getUnidadRegistro());
            motivoTField.setText("");
            asuntoTField.setText("");
            fechaLimiteObraJTField.setText("");
            fechaSolicitudJTField.setText(showToday());
            observacionesTPane.setText("");
            tasaTextField.setText("€0.00");
            impuestoTextField.setText("€0.00");
            CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
            CMainLicencias.geopistaEditor.getSelectionManager().clear();

            /** Emplazamiento */
            tipoViaINEEJCBox.setSelectedPatron("-1");
            referenciaJTextField.setText("");
            nombreViaTField.setText("");
            numeroViaNumberTField.setText("");
            portalViaTField.setText("");
            plantaViaTField.setText("");
            letraViaTField.setText("");
            cpostalViaTField.setText("");
            municipioJTField.setText(CConstantesLicencias.Municipio);
            provinciaJTField.setText(CConstantesLicencias.Provincia);

            /** Documentacion */
            documentacionJPanel.clearDocumentacionJPanel();

            /** Propietario */
            DNITitularTField.setText("");
            nombreTitularJTField.setText("");
            apellido1TitularJTField.setText("");
            apellido2TitularJTField.setText("");
            buscarDNITitularJButton.setToolTipText("");
            faxTitularTField.setText("");
            telefonoTitularTField.setText("");
            movilTitularTField.setText("");
            emailTitularJTField.setText("");
            nombreViaTitularJTField.setText("");
            numeroViaTitularTField.setText("");
            plantaTitularJTField.setText("");
            portalTitularJTField.setText("");
            escaleraTitularJTField.setText("");
            letraTitularJTField.setText("");
            cPostalTitularTField.setText("");
            municipioTitularJTField.setText("");
            provinciaTitularJTField.setText("");
            notificarTitularJCheckBox.setSelected(true);

            /** Representante */
            DNIRepresentaATField.setText("");
            nombreRepresentaAJTField.setText("");
            apellido1RepresentaAJTField.setText("");
            apellido2RepresentaAJTField.setText("");
            buscarDNIRepresentaAJButton.setToolTipText("");
            faxRepresentaATField.setText("");
            telefonoRepresentaATField.setText("");
            movilRepresentaATField.setText("");
            emailRepresentaAJTField.setText("");
            nombreViaRepresentaAJTField.setText("");
            numeroViaRepresentaATField.setText("");
            plantaRepresentaAJTField.setText("");
            portalRepresentaAJTField.setText("");
            escaleraRepresentaAJTField.setText("");
            letraRepresentaAJTField.setText("");
            cPostalRepresentaATField.setText("");
            municipioRepresentaAJTField.setText("");
            provinciaRepresentaAJTField.setText("");
            notificarRepresentaAJCheckBox.setSelected(false);
            _seNotificaRepresentaA= 0;
            _flagRepresentante= false;


            /** Promotor */
            DNIPromotorTField.setText("");
            nombrePromotorJTField.setText("");
            apellido1PromotorJTField.setText("");
            apellido2PromotorJTField.setText("");
            colegioPromotorJTField.setText("");
            visadoPromotorJTField.setText("");
            titulacionPromotorJTField.setText("");
            buscarDNIPromotorJButton.setToolTipText("");
            faxPromotorTField.setText("");
            telefonoPromotorTField.setText("");
            movilPromotorTField.setText("");
            emailPromotorJTField.setText("");
            nombreViaPromotorJTField.setText("");
            numeroViaPromotorTField.setText("");
            plantaPromotorJTField.setText("");
            portalPromotorJTField.setText("");
            escaleraPromotorJTField.setText("");
            letraPromotorJTField.setText("");
            cPostalPromotorTField.setText("");
            municipioPromotorJTField.setText("");
            provinciaPromotorJTField.setText("");
            notificarPromotorJCheckBox.setSelected(false);
            _seNotificaPromotor= 0;


        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }




	private CReferenciasCatastralesTableModel referenciasCatastralesJTableModel;
	private final JFrame desktop;
    private ComboBoxEstructuras tipoObraEJCBox;
    private ComboBoxEstructuras viaNotificacionTitularEJCBox;
    private ComboBoxEstructuras viaNotificacionRepresentaAEJCBox;
    private ComboBoxEstructuras viaNotificacionPromotorEJCBox;

    private ComboBoxEstructuras tipoViaNotificacionTitularEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionRepresentaAEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionPromotorEJCBox;

    private ComboBoxEstructuras tipoViaINEEJCBox;

    /** pestanna de documentacion de una solicitud (documentacion requerida, anexos...) */
    private DocumentacionLicenciasJPanel documentacionJPanel;

    /** pestanna del tecnico */
    private com.geopista.app.licencias.tecnicos.TecnicosJPanel tecnicosJPanel;


    /** Variables propias */
    private com.geopista.app.utilidades.TextField asuntoTField;
    private com.geopista.app.utilidades.TextField motivoTField;
    private com.geopista.app.utilidades.TextPane observacionesTPane;

    private com.geopista.app.utilidades.TextField DNIPromotorTField;
    private com.geopista.app.utilidades.TextField DNITitularTField;
    private com.geopista.app.utilidades.TextField DNIRepresentaATField;

    private com.geopista.app.utilidades.JNumberTextField cPostalPromotorTField;
    private com.geopista.app.utilidades.JNumberTextField cPostalRepresentaATField;
    private TextField  cPostalTitularTField;

    private com.geopista.app.utilidades.JNumberTextField numeroViaTitularTField;
    private com.geopista.app.utilidades.JNumberTextField numeroViaRepresentaATField;
    private com.geopista.app.utilidades.JNumberTextField numeroViaPromotorTField;

    private com.geopista.app.utilidades.JNumberTextField telefonoTitularTField;
    private com.geopista.app.utilidades.JNumberTextField telefonoRepresentaATField;
    private com.geopista.app.utilidades.JNumberTextField telefonoPromotorTField;

    /** Movil para la notificacion */
    private com.geopista.app.utilidades.JNumberTextField movilTitularTField;
    private com.geopista.app.utilidades.JNumberTextField movilRepresentaATField;
    private com.geopista.app.utilidades.JNumberTextField movilPromotorTField;

    /** Fax para la notificacion */
    private com.geopista.app.utilidades.JNumberTextField faxTitularTField;
    private com.geopista.app.utilidades.JNumberTextField faxRepresentaATField;
    private com.geopista.app.utilidades.JNumberTextField faxPromotorTField;

    /** tasa e impuesto */
    private com.geopista.app.utilidades.JNumberTextField tasaTextField;
    private com.geopista.app.utilidades.JNumberTextField impuestoTextField;

    /** emplazamiento */
    private com.geopista.app.utilidades.TextField nombreViaTField;
    //private com.geopista.app.utilidades.JNumberTextField numeroViaNumberTField;
    private com.geopista.app.utilidades.TextField numeroViaNumberTField;
    private com.geopista.app.utilidades.TextField portalViaTField;
    private com.geopista.app.utilidades.TextField plantaViaTField;
    private com.geopista.app.utilidades.TextField letraViaTField;
    private com.geopista.app.utilidades.JNumberTextField cpostalViaTField;



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DNIPromotorJLabel;
    private javax.swing.JLabel DNIRepresenaAJLabel;
    private javax.swing.JLabel DNITitularJLabel;
    private javax.swing.JButton MapToTableJButton;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JLabel apellido1PromotorJLabel;
    private javax.swing.JTextField apellido1PromotorJTField;
    private javax.swing.JLabel apellido1RepresentaAJLabel;
    private javax.swing.JTextField apellido1RepresentaAJTField;
    private javax.swing.JLabel apellido1TitularJLabel;
    private javax.swing.JTextField apellido1TitularJTField;
    private javax.swing.JLabel apellido2PromotorJLabel;
    private javax.swing.JTextField apellido2PromotorJTField;
    private javax.swing.JLabel apellido2RepresentaAJLabel;
    private javax.swing.JTextField apellido2RepresentaAJTField;
    private javax.swing.JLabel apellido2TitularJLabel2;
    private javax.swing.JTextField apellido2TitularJTField;
    private javax.swing.JLabel asuntoJLabel;
    private javax.swing.JPanel botoneraJPanel;
    private javax.swing.JButton buscarDNIPromotorJButton;
    private javax.swing.JButton buscarDNIRepresentaAJButton;
    private javax.swing.JButton buscarDNITitularJButton;
    private javax.swing.JLabel cPostalJLabel;
    private javax.swing.JLabel cPostalPromotorJLabel;
    private javax.swing.JLabel cPostalRepresentaAJLabel;
    private javax.swing.JLabel cPostalTitularJLabel;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JLabel colegioPromotorJLabel;
    private javax.swing.JTextField colegioPromotorJTField;
    private javax.swing.JPanel datosNotificacionPromotorJPanel;
    private javax.swing.JPanel datosNotificacionRepresentaAJPanel;
    private javax.swing.JPanel datosNotificacionTitularJPanel;
    private javax.swing.JPanel datosPersonalesPromotorJPanel;
    private javax.swing.JPanel datosPersonalesRepresentaAJPanel;
    private javax.swing.JPanel datosPersonalesTitularJPanel;
    private javax.swing.JPanel datosSolicitudJPanel;
    private javax.swing.JButton deleteRegistroCatastralJButton;
    private javax.swing.JPanel editorMapaJPanel;
    private javax.swing.JLabel emailPromotorJLabel;
    private javax.swing.JTextField emailPromotorJTField;
    private javax.swing.JLabel emailRepresentaAJLabel;
    private javax.swing.JTextField emailRepresentaAJTField;
    private javax.swing.JLabel emailTitularJLabel;
    private javax.swing.JTextField emailTitularJTField;
    private javax.swing.JPanel emplazamientoJPanel;
    private javax.swing.JLabel escaleraPromotorJLabel;
    private javax.swing.JTextField escaleraPromotorJTField;
    private javax.swing.JLabel escaleraRepresentaAJLabel;
    private javax.swing.JTextField escaleraRepresentaAJTField;
    private javax.swing.JLabel escaleraTitularJLabel;
    private javax.swing.JTextField escaleraTitularJTField;
    private javax.swing.JComboBox estadoJCBox;
    private javax.swing.JLabel estadoJLabel;
    private javax.swing.JLabel faxPromotorJLabel;
    private javax.swing.JLabel faxRepresentaAJLabel;
    private javax.swing.JLabel faxTitularJLabel;
    private javax.swing.JButton fechaLimiteObraJButton;
    private javax.swing.JLabel fechaLimiteObraJLabel;
    private javax.swing.JTextField fechaLimiteObraJTField;
    private javax.swing.JButton fechaSolicitudJButton;
    private javax.swing.JLabel fechaSolicitudJLabel;
    private javax.swing.JTextField fechaSolicitudJTField;
    private javax.swing.JLabel impuestoJLabel;
    private javax.swing.JLabel letraPromotorJLabel;
    private javax.swing.JTextField letraPromotorJTField;
    private javax.swing.JLabel letraRepresentaAJLabel;
    private javax.swing.JTextField letraRepresentaAJTField;
    private javax.swing.JLabel letraTitularJLabel;
    private javax.swing.JTextField letraTitularJTField;
    private javax.swing.JLabel motivoJLabel;
    private javax.swing.JLabel movilPromotorJLabel;
    private javax.swing.JLabel movilRepresentaAJLabel;
    private javax.swing.JLabel movilTitularJLabel;
    private javax.swing.JTextField municipioJTField;
    private javax.swing.JLabel municipioPromotorJLabel;
    private javax.swing.JTextField municipioPromotorJTField;
    private javax.swing.JLabel municipioRepresentaAJLabel;
    private javax.swing.JTextField municipioRepresentaAJTField;
    private javax.swing.JLabel municipioTitularJLabel;
    private javax.swing.JTextField municipioTitularJTField;
    private javax.swing.JButton nombreJButton;
    private javax.swing.JLabel nombrePromotorJLabel;
    private javax.swing.JTextField nombrePromotorJTField;
    private javax.swing.JLabel nombreRepresentaAJLabel;
    private javax.swing.JTextField nombreRepresentaAJTField;
    private javax.swing.JLabel nombreTitularJLabel;
    private javax.swing.JTextField nombreTitularJTField;
    private javax.swing.JLabel nombreViaJLabel;
    private javax.swing.JLabel nombreViaPromotorJLabel;
    private javax.swing.JTextField nombreViaPromotorJTField;
    private javax.swing.JLabel nombreViaRepresentaAJLabel;
    private javax.swing.JTextField nombreViaRepresentaAJTField;
    private javax.swing.JLabel nombreViaTitularJLabel;
    private javax.swing.JTextField nombreViaTitularJTField;
    private javax.swing.JCheckBox notificarPromotorJCheckBox;
    private javax.swing.JLabel notificarPromotorJLabel;
    private javax.swing.JCheckBox notificarRepresentaAJCheckBox;
    private javax.swing.JLabel notificarRepresentaAJLabel;
    private javax.swing.JCheckBox notificarTitularJCheckBox;
    private javax.swing.JLabel notificarTitularJLabel;
    private javax.swing.JLabel numeroViaJLabel;
    private javax.swing.JLabel numeroViaPromotorJLabel;
    private javax.swing.JLabel numeroViaRepresentaAJLabel;
    private javax.swing.JLabel numeroViaTitularJLabel;
    private javax.swing.JPanel obraMayorJPanel;
    private javax.swing.JTabbedPane obraMayorJTabbedPane;
    private javax.swing.JLabel observacionesJLabel;
    private javax.swing.JScrollPane observacionesJScrollPane;
    private javax.swing.JLabel plantaPromotorJLabel;
    private javax.swing.JTextField plantaPromotorJTField;
    private javax.swing.JLabel plantaRepresentaAJLabel;
    private javax.swing.JTextField plantaRepresentaAJTField;
    private javax.swing.JLabel plantaTitularJLabel;
    private javax.swing.JTextField plantaTitularJTField;
    private javax.swing.JLabel portalPromotorJLabel;
    private javax.swing.JTextField portalPromotorJTField;
    private javax.swing.JLabel portalRepresentaAJLabel;
    private javax.swing.JTextField portalRepresentaAJTField;
    private javax.swing.JLabel portalTitularJLabel;
    private javax.swing.JTextField portalTitularJTField;
    private javax.swing.JPanel promotorJPanel;
    private javax.swing.JLabel provinciaJLabel;
    private javax.swing.JTextField provinciaJTField;
    private javax.swing.JLabel provinciaPromotorJLabel;
    private javax.swing.JTextField provinciaPromotorJTField;
    private javax.swing.JLabel provinciaRepresentaAJLabel;
    private javax.swing.JTextField provinciaRepresentaAJTField;
    private javax.swing.JLabel provinciaTitularJLabel;
    private javax.swing.JTextField provinciaTitularJTField;
    private javax.swing.JButton referenciaCatastralJButton;
    private javax.swing.JLabel referenciaCatastralJLabel;
    private javax.swing.JTextField referenciaJTextField;
    private javax.swing.JScrollPane referenciasCatastralesJScrollPane;
    private javax.swing.JTable referenciasCatastralesJTable;
    private javax.swing.JPanel representaAJPanel;
    private javax.swing.JButton tableToMapJButton;
    private javax.swing.JLabel tasaJLabel;
    private javax.swing.JLabel telefonoPromotorJLabel;
    private javax.swing.JLabel telefonoRepresentaAJLabel;
    private javax.swing.JLabel telefonoTitularJLabel;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoObraJLabel;
    private javax.swing.JLabel tipoViaPromotorJLabel;
    private javax.swing.JLabel tipoViaRepresentaAJLabel;
    private javax.swing.JLabel tipoViaTitularJLabel;
    private javax.swing.JLabel titulacionPromotorJLabel;
    private javax.swing.JTextField titulacionPromotorJTField;
    private javax.swing.JPanel titularJPanel;
    private javax.swing.JLabel unidadRJLabel;
    private javax.swing.JTextField unidadRJTField;
    private javax.swing.JLabel unidadTJLabel;
    private javax.swing.JTextField unidadTJTField;
    private javax.swing.JLabel viaNotificacionPromotorJLabel;
    private javax.swing.JLabel viaNotificacionRepresentaAJLabel;
    private javax.swing.JLabel viaNotificacionTitularJLabel;
    private javax.swing.JLabel visadoPromotorJLabel;
    private javax.swing.JTextField visadoPromotorJTField;
    private JTabbedPane jTabbedPaneSolicitud;
    // End of variables declaration//GEN-END:variables

}
