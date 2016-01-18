package com.geopista.app.licencias.obraMenor.creacion;
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
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.JDialogConfiguracion;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.CellRendererEstructuras;
import com.geopista.app.licencias.*;
import com.geopista.app.licencias.utilidades.ComboBoxTableEditor;
import com.geopista.app.licencias.utilidades.TextFieldTableEditor;
import com.geopista.app.licencias.utilidades.TextFieldRenderer;
import com.geopista.app.licencias.utilidades.JInternalFrameLicencias;
import com.geopista.app.licencias.tableModels.CReferenciasCatastralesTableModel;
import com.geopista.app.licencias.documentacion.DocumentacionLicenciasJPanel;
import com.geopista.app.licencias.consulta.CConsultaLicencias;
import com.geopista.editor.GeopistaEditor;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.*;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.protocol.licencias.tipos.CTipoObra;
import com.geopista.model.GeopistaListener;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.util.StringUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.*;

/**
 * @author avivar
 */
public class CCreacionLicencias extends JInternalFrameLicencias implements IMultilingue{


	Logger logger = Logger.getLogger(CConsultaLicencias.class);
	/**
	 * Datos del Titular
	 */
	private String _DNI_CIF_Titular = "";
	private String _nombreTitular = "";
	private String _apellido1Titular = "";
	private String _apellido2Titular = "";
	private String _faxTitular = "";
	private String _movilTitular = "";
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
	private String _faxRepresentaA = "";
	private String _movilRepresentaA = "";
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
	 * Datos del Tecnico
	 */
	private String _DNI_CIF_Tecnico = "";
	private String _nombreTecnico = "";
	private String _apellido1Tecnico = "";
	private String _apellido2Tecnico = "";
	private String _colegioTecnico = "";
	private String _visadoTecnico = "";
	private String _titulacionTecnico = "";
	private String _faxTecnico = "";
	private String _movilTecnico = "";
	private String _emailTecnico = "";
	private String _nombreViaTecnico = "";
	private String _numeroViaTecnico = "";
	private String _portalTecnico = "";
	private String _plantaTecnico = "";
	private String _escaleraTecnico = "";
	private String _letraTecnico = "";
	private String _cPostalTecnico = "";
	private String _municipioTecnico = "";
	private String _provinciaTecnico = "";
	private int _seNotificaTecnico = 0;
    private boolean _flagTecnico= false;
    private boolean emailTecnicoObligatorio= false;

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
	private String _faxPromotor = "";
	private String _movilPromotor = "";
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
    private boolean _flagPromotor= false;
    private boolean emailPromotorObligatorio= false;

	/**
	 * Datos de la solicitud
	 */
	// Tendra un valor u otro en funcion del TabbedPane seleccionado (CConstantesLicencias.ObraMayor)
	private String _unidadRegistro = "";
	private String _unidadTramitadora = "";
	private String _motivo = "";
	private String _asunto = "";
	private String _observaciones = "";

	/**
	 * por defecto la solicitud es manual y no llega desde Ventanilla Unica
	 */

	private Vector _vEstado = null;
	private Hashtable _hEstado = new Hashtable();


    /** pestanna de documentacion de una solicitud (documentacion requerida, anexos...) */
    private DocumentacionLicenciasJPanel documentacionJPanel;


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
                        /* añadimos el documento a la lista */
                        try
                        {
                            progressDialog.report(CMainLicencias.literales.getString("Licencias.Tag2"));
                            initComponents();
                            initComboBoxesEstructuras();
                            configureComponents();
                            renombrarComponentes(CMainLicencias.literales);

                            fechaSolicitudJTField.setEnabled(false);
                            fechaSolicitudJTField.setText(showToday());

                            fechaLimiteObraJTField.setEnabled(false);
                            fechaLimiteObraJTField.setText("");

                            estadoJCBox.setEnabled(false);
                            progressDialog.report(CMainLicencias.literales.getString("Licencias.Tag1"));
                            loadMapa(/*"licencias_obra_menor"*/276, "licencias_obra_menor", "licencias_obra_mayor");
                        }
                            catch(Exception e)
                        {
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

        geopistaEditor.removeAllGeopistaListener();
		geopistaEditor.addGeopistaListener(new GeopistaListener() {

			public void selectionChanged(AbstractSelection abtractSelection) {
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

			public void featureActioned(AbstractSelection abtractSelection) {
				logger.info("featureActioned");
			}

		});

		municipioJTField.setText(CConstantesLicencias.Municipio);
		provinciaJTField.setText(CConstantesLicencias.Provincia);
		rellenarEstadoJCBox();
	}


	private void configureComponents() {

		long tiempoInicial=new Date().getTime();
		if (CMainLicencias.geopistaEditor == null) CMainLicencias.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");
        setGeopistaEditor(CMainLicencias.geopistaEditor);
		logger.info("TIME (new GeopistaEditor()): "+(new Date().getTime()-tiempoInicial));


		CConstantesLicencias.referenciasCatastrales = new Hashtable();

		referenciasCatastralesJTableModel = new CReferenciasCatastralesTableModel(new String[]{CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column1"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column2"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column3"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column4"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column5"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column6"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column7"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column8"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column9"),
                                                                               CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column10"), ""});

		referenciasCatastralesJTable.setModel(referenciasCatastralesJTableModel);
		referenciasCatastralesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		referenciasCatastralesJTable.setCellSelectionEnabled(false);
		referenciasCatastralesJTable.setColumnSelectionAllowed(false);
		referenciasCatastralesJTable.setRowSelectionAllowed(true);
        referenciasCatastralesJTable.getTableHeader().setReorderingAllowed(false);

        referenciasCatastralesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int j=0; j< referenciasCatastralesJTable.getColumnCount(); j++){
            TableColumn column = referenciasCatastralesJTable.getColumnModel().getColumn(j);

            if (j==1){
                column.setMinWidth(75);
                column.setMaxWidth(150);
                column.setWidth(75);
                column.setPreferredWidth(75);
            }else if (j>2){
                column.setMinWidth(50);
                column.setMaxWidth(100);
                column.setWidth(50);
                column.setPreferredWidth(50);
            }else{
                column.setMinWidth(150);
                column.setMaxWidth(300);
                column.setWidth(150);
                column.setPreferredWidth(150);
            }
            column.setResizable(true);
        }

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


        /** tasa */
        tasaTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
        datosSolicitudJPanel.add(tasaTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 330, -1));
        /** impuesto */
        impuestoTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
        datosSolicitudJPanel.add(impuestoTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 330, -1));

        fechaSolicitudJTField.setEnabled(false);
        fechaLimiteObraJTField.setEnabled(false);

		fechaSolicitudJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
        fechaLimiteObraJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		nombreJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		referenciaCatastralJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		MapToTableJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoFlechaIzquierda);
		tableToMapJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoFlechaDerecha);

		buscarDNITitularJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		buscarDNIRepresentaAJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		buscarDNITecnicoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		buscarDNIPromotorJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
		deleteRegistroCatastralJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);

        /** emplazamiento */
        nombreViaTField= new com.geopista.app.utilidades.TextField(68);
        nombreViaTField.setEditable(true);
        emplazamientoJPanel.add(nombreViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 38, 190, -1));

        //numeroViaNumberTField=  new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        numeroViaNumberTField=  new com.geopista.app.utilidades.TextField(8);
        numeroViaNumberTField.setEditable(true);
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
        emplazamientoJPanel.add(cpostalViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 84, 80, -1));


        /** Pestanas */
        jTabbedPaneSolicitud= new JTabbedPane();
        jTabbedPaneSolicitud.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPaneSolicitud.setFont(new java.awt.Font("Arial", 0, 10));
        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.obraMayorJPanel.SubTitleTab")), CUtilidadesComponentes.iconoSolicitud, obraMayorJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.obraMayorJPanel.SubTitleTab")), obraMayorJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.titularJPanel.TitleTab")), CUtilidadesComponentes.iconoPersona, titularJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.titularJPanel.TitleTab")), titularJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.representaAJPanel.TitleTab"), CUtilidadesComponentes.iconoRepresentante, representaAJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.representaAJPanel.TitleTab"), representaAJPanel);
        }

        try{
            jTabbedPaneSolicitud.addTab(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.TitleTab"), CUtilidadesComponentes.iconoPersona, tecnicoJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.TitleTab"), tecnicoJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.promotorJPanel.TitleTab"), CUtilidadesComponentes.iconoPersona, promotorJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.promotorJPanel.TitleTab"), promotorJPanel);
        }
        try{
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.obraMayorJPanel.TitleTab")), CUtilidadesComponentes.iconoSolicitud, jTabbedPaneSolicitud);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.obraMayorJPanel.TitleTab")), jTabbedPaneSolicitud);
        }

        /** Annadimos la pestanna de documentacion para la solicitud */
        documentacionJPanel= new DocumentacionLicenciasJPanel(CMainLicencias.literales,CConstantesLicencias.LicenciasObraMenor);
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
        motivoJTField = new javax.swing.JTextField();
        asuntoJTField = new javax.swing.JTextField();
        fechaSolicitudJTField = new javax.swing.JTextField();
        observacionesJScrollPane = new javax.swing.JScrollPane();
        observacionesJTArea = new javax.swing.JTextArea();
        fechaSolicitudJButton = new javax.swing.JButton();
        tasaJLabel = new javax.swing.JLabel();
        impuestoJLabel = new javax.swing.JLabel();
        fechaLimiteObraJLabel = new javax.swing.JLabel();
        fechaLimiteObraJTField = new javax.swing.JTextField();
        fechaLimiteObraJButton = new javax.swing.JButton();
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
        DNITitularJTField = new javax.swing.JTextField();
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
        faxTitularJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        telefonoTitularJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        movilTitularJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        emailTitularJTField = new javax.swing.JTextField();
        nombreViaTitularJTField = new javax.swing.JTextField();
        numeroViaTitularJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        plantaTitularJTField = new javax.swing.JTextField();
        portalTitularJTField = new javax.swing.JTextField();
        escaleraTitularJTField = new javax.swing.JTextField();
        letraTitularJTField = new javax.swing.JTextField();
        cPostalTitularJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioTitularJTField = new javax.swing.JTextField();
        provinciaTitularJTField = new javax.swing.JTextField();
        notificarTitularJCheckBox = new javax.swing.JCheckBox();
        notificarTitularJLabel = new javax.swing.JLabel();
        representaAJPanel = new javax.swing.JPanel();
        datosPersonalesRepresentaAJPanel = new javax.swing.JPanel();
        DNIRepresenaAJLabel = new javax.swing.JLabel();
        DNIRepresentaAJTField = new javax.swing.JTextField();
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
        faxRepresentaAJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        telefonoRepresentaAJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        movilRepresentaAJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        emailRepresentaAJTField = new javax.swing.JTextField();
        nombreViaRepresentaAJTField = new javax.swing.JTextField();
        numeroViaRepresentaAJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        plantaRepresentaAJTField = new javax.swing.JTextField();
        portalRepresentaAJTField = new javax.swing.JTextField();
        escaleraRepresentaAJTField = new javax.swing.JTextField();
        letraRepresentaAJTField = new javax.swing.JTextField();
        cPostalRepresentaAJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioRepresentaAJTField = new javax.swing.JTextField();
        provinciaRepresentaAJTField = new javax.swing.JTextField();
        notificarRepresentaAJCheckBox = new javax.swing.JCheckBox();
        notificarRepresentaAJLabel = new javax.swing.JLabel();
        tecnicoJPanel = new javax.swing.JPanel();
        datosPersonalesTecnicoJPanel = new javax.swing.JPanel();
        DNITecnicoJLabel = new javax.swing.JLabel();
        DNITecnicoJTField = new javax.swing.JTextField();
        nombreTecnicoJLabel = new javax.swing.JLabel();
        nombreTecnicoJTField = new javax.swing.JTextField();
        apellido1TecnicoJLabel = new javax.swing.JLabel();
        apellido2TecnicoJLabel = new javax.swing.JLabel();
        apellido1TecnicoJTField = new javax.swing.JTextField();
        apellido2TecnicoJTField = new javax.swing.JTextField();
        buscarDNITecnicoJButton = new javax.swing.JButton();
        colegioTecnicoJLabel = new javax.swing.JLabel();
        visadoTecnicoJLabel = new javax.swing.JLabel();
        titulacionTecnicoJLabel = new javax.swing.JLabel();
        colegioTecnicoJTField = new javax.swing.JTextField();
        visadoTecnicoJTField = new javax.swing.JTextField();
        titulacionTecnicoJTField = new javax.swing.JTextField();
        datosNotificacionTecnicoJPanel = new javax.swing.JPanel();
        viaNotificacionTecnicoJLabel = new javax.swing.JLabel();
        faxTecnicoJLabel = new javax.swing.JLabel();
        telefonoTecnicoJLabel = new javax.swing.JLabel();
        movilTecnicoJLabel = new javax.swing.JLabel();
        emailTecnicoJLabel = new javax.swing.JLabel();
        tipoViaTecnicoJLabel = new javax.swing.JLabel();
        nombreViaTecnicoJLabel = new javax.swing.JLabel();
        numeroViaTecnicoJLabel = new javax.swing.JLabel();
        portalTecnicoJLabel = new javax.swing.JLabel();
        plantaTecnicoJLabel = new javax.swing.JLabel();
        escaleraTecnicoJLabel = new javax.swing.JLabel();
        letraTecnicoJLabel = new javax.swing.JLabel();
        cPostalTecnicoJLabel = new javax.swing.JLabel();
        municipioTecnicoJLabel = new javax.swing.JLabel();
        provinciaTecnicoJLabel = new javax.swing.JLabel();
        faxTecnicoJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        telefonoTecnicoJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        movilTecnicoJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        emailTecnicoJTField = new javax.swing.JTextField();
        nombreViaTecnicoJTField = new javax.swing.JTextField();
        numeroViaTecnicoJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        plantaTecnicoJTField = new javax.swing.JTextField();
        portalTecnicoJTField = new javax.swing.JTextField();
        escaleraTecnicoJTField = new javax.swing.JTextField();
        letraTecnicoJTField = new javax.swing.JTextField();
        cPostalTecnicoJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioTecnicoJTField = new javax.swing.JTextField();
        provinciaTecnicoJTField = new javax.swing.JTextField();
        notificarTecnicoJCheckBox = new javax.swing.JCheckBox();
        notificarTecnicoJLabel = new javax.swing.JLabel();
        promotorJPanel = new javax.swing.JPanel();
        datosPersonalesPromotorJPanel = new javax.swing.JPanel();
        DNIPromotorJLabel = new javax.swing.JLabel();
        DNIPromotorJTField = new javax.swing.JTextField();
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
        faxPromotorJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        telefonoPromotorJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        movilPromotorJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        emailPromotorJTField = new javax.swing.JTextField();
        nombreViaPromotorJTField = new javax.swing.JTextField();
        numeroViaPromotorJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        plantaPromotorJTField = new javax.swing.JTextField();
        portalPromotorJTField = new javax.swing.JTextField();
        escaleraPromotorJTField = new javax.swing.JTextField();
        letraPromotorJTField = new javax.swing.JTextField();
        cPostalPromotorJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
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

        templateJPanel.setLayout(new BorderLayout());

        obraMayorJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));
        obraMayorJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        obraMayorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder("Datos solicitud"));
        datosSolicitudJPanel.setAutoscrolls(true);
        estadoJLabel.setText("(*) Estado:");
        datosSolicitudJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 150, 20));

        tipoObraJLabel.setText("(*) Tipo Obra:");
        datosSolicitudJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 20));

        unidadTJLabel.setText("Unidad Tramitadora:");
        datosSolicitudJPanel.add(unidadTJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 20));

        unidadRJLabel.setText("Unidad de Registro:");
        datosSolicitudJPanel.add(unidadRJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 150, 20));

        motivoJLabel.setText("Motivo:");
        datosSolicitudJPanel.add(motivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 150, 20));

        asuntoJLabel.setText("Asunto:");
        datosSolicitudJPanel.add(asuntoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 150, 20));

        fechaSolicitudJLabel.setText("(*) Fecha Solicitud:");
        datosSolicitudJPanel.add(fechaSolicitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 150, 20));

        observacionesJLabel.setText("Observaciones:");
        datosSolicitudJPanel.add(observacionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 150, 20));

        datosSolicitudJPanel.add(estadoJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 330, 20));

        datosSolicitudJPanel.add(unidadTJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 330, -1));

        datosSolicitudJPanel.add(unidadRJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 330, -1));

        datosSolicitudJPanel.add(motivoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 330, -1));

        datosSolicitudJPanel.add(asuntoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 330, -1));

        datosSolicitudJPanel.add(fechaSolicitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 70, -1));

        observacionesJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        observacionesJTArea.setLineWrap(true);
        observacionesJTArea.setRows(3);
        observacionesJTArea.setTabSize(4);
        observacionesJTArea.setWrapStyleWord(true);
        observacionesJTArea.setBorder(null);
        observacionesJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        observacionesJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        observacionesJTArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                observacionesJTAreaKeyTyped();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                observacionesJTAreaKeyReleased();
            }
        });

        observacionesJScrollPane.setViewportView(observacionesJTArea);

        datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 330, 70));

        fechaSolicitudJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaSolicitudJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaSolicitudJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaSolicitudJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaSolicitudJButtonActionPerformed();
            }
        });

        datosSolicitudJPanel.add(fechaSolicitudJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 20, 20));

        tasaJLabel.setText("Tasa:");
        datosSolicitudJPanel.add(tasaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 150, 20));

        impuestoJLabel.setText("Impuesto:");
        datosSolicitudJPanel.add(impuestoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 150, 20));

        fechaLimiteObraJLabel.setText("Fecha L\u00edmite Obra:");
        datosSolicitudJPanel.add(fechaLimiteObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 130, 20));

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

        obraMayorJPanel.add(datosSolicitudJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 310));

        emplazamientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder("Emplazamiento"));
        nombreViaJLabel.setText("Tipo v\u00eda / Nombre V\u00eda:");
        emplazamientoJPanel.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 38, 150, 20));

        numeroViaJLabel.setText("N\u00ba / Portal / Planta /Letra:");
        emplazamientoJPanel.add(numeroViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 61, 150, 20));

        cPostalJLabel.setText("C.P. / Municipio: ");
        emplazamientoJPanel.add(cPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 84, 150, 20));

        provinciaJLabel.setText("Provincia:");
        emplazamientoJPanel.add(provinciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 107, 150, 20));

        municipioJTField.setEnabled(false);
        emplazamientoJPanel.add(municipioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 84, 240, -1));

        provinciaJTField.setEnabled(false);
        emplazamientoJPanel.add(provinciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 107, 330, -1));

        referenciasCatastralesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Referencia catastral"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
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
                nombreViaJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(nombreJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 38, 20, 20));

        referenciaCatastralJLabel.setText("Referencia catastral:");
        emplazamientoJPanel.add(referenciaCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 150, 20));

        referenciaJTextField.setEnabled(false);
        emplazamientoJPanel.add(referenciaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 15, 310, -1));

        referenciaCatastralJButton.setIcon(new javax.swing.ImageIcon(""));
        referenciaCatastralJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        referenciaCatastralJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        referenciaCatastralJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refCatastralJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(referenciaCatastralJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 15, 20, 20));

        MapToTableJButton.setIcon(new javax.swing.ImageIcon(""));
        MapToTableJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MapToTableJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        MapToTableJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapToTableJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(MapToTableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 135, 20, 20));

        deleteRegistroCatastralJButton.setIcon(new javax.swing.ImageIcon(""));
        deleteRegistroCatastralJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        deleteRegistroCatastralJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        deleteRegistroCatastralJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    deleteParcelaJButtonActionPerformed();
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

        DNITitularJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNITitularJTFieldKeyTyped();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNITitularJTFieldKeyReleased();
            }
        });

        datosPersonalesTitularJPanel.add(DNITitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 340, -1));

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

        datosNotificacionTitularJPanel.add(faxTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 42, 360, -1));

        datosNotificacionTitularJPanel.add(telefonoTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 64, 360, -1));

        datosNotificacionTitularJPanel.add(movilTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 86, 360, -1));

        datosNotificacionTitularJPanel.add(emailTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 108, 360, -1));

        datosNotificacionTitularJPanel.add(nombreViaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 162, 360, -1));

        datosNotificacionTitularJPanel.add(numeroViaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 184, 150, -1));

        datosNotificacionTitularJPanel.add(plantaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 206, 150, -1));

        datosNotificacionTitularJPanel.add(portalTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 150, -1));

        datosNotificacionTitularJPanel.add(escaleraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 272, 150, -1));

        datosNotificacionTitularJPanel.add(letraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 228, 150, -1));

        datosNotificacionTitularJPanel.add(cPostalTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, 360, -1));

        datosNotificacionTitularJPanel.add(municipioTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 332, 360, -1));

        datosNotificacionTitularJPanel.add(provinciaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 354, 360, -1));

        notificarTitularJCheckBox.setEnabled(false);
        notificarTitularJCheckBox.setSelected(true);
        datosNotificacionTitularJPanel.add(notificarTitularJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 376, 30, -1));

        notificarTitularJLabel.setText("Notificar propietario:");
        datosNotificacionTitularJPanel.add(notificarTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 376, 120, 20));

        titularJPanel.add(datosNotificacionTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 135, 520, 412));

        representaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNIRepresenaAJLabel.setText("(*) DNI/CIF:");
        datosPersonalesRepresentaAJPanel.add(DNIRepresenaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        DNIRepresentaAJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIRepresentaAJTFieldKeyTyped();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIRepresentaAJTFieldKeyReleased();
            }
        });

        datosPersonalesRepresentaAJPanel.add(DNIRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 340, -1));

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

        datosNotificacionRepresentaAJPanel.add(faxRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 42, 360, -1));

        datosNotificacionRepresentaAJPanel.add(telefonoRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 64, 360, -1));

        datosNotificacionRepresentaAJPanel.add(movilRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 86, 360, -1));

        datosNotificacionRepresentaAJPanel.add(emailRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 108, 360, -1));

        datosNotificacionRepresentaAJPanel.add(nombreViaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 162, 360, -1));

        datosNotificacionRepresentaAJPanel.add(numeroViaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 184, 150, -1));

        datosNotificacionRepresentaAJPanel.add(plantaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 206, 150, -1));

        datosNotificacionRepresentaAJPanel.add(portalRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 150, -1));

        datosNotificacionRepresentaAJPanel.add(escaleraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 272, 150, -1));

        datosNotificacionRepresentaAJPanel.add(letraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 228, 150, -1));

        datosNotificacionRepresentaAJPanel.add(cPostalRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, 360, -1));

        datosNotificacionRepresentaAJPanel.add(municipioRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 332, 360, -1));

        datosNotificacionRepresentaAJPanel.add(provinciaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 354, 360, -1));

        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 376, 30, -1));

        notificarRepresentaAJLabel.setText("Notificar representante:");
        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 376, 120, 20));

        representaAJPanel.add(datosNotificacionRepresentaAJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 135, 520, 412));

        tecnicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTecnicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNITecnicoJLabel.setText("(*) DNI/CIF:");
        datosPersonalesTecnicoJPanel.add(DNITecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        DNITecnicoJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNITecnicoJTFieldKeyTyped();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNITecnicoJTFieldKeyReleased();
            }
        });

        datosPersonalesTecnicoJPanel.add(DNITecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 340, -1));

        nombreTecnicoJLabel.setText("(*) Nombre:");
        datosPersonalesTecnicoJPanel.add(nombreTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 47, 130, 20));

        datosPersonalesTecnicoJPanel.add(nombreTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 47, 360, -1));

        apellido1TecnicoJLabel.setText("Apellido1:");
        datosPersonalesTecnicoJPanel.add(apellido1TecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 68, 130, 20));

        apellido2TecnicoJLabel.setText("Apellido2:");
        datosPersonalesTecnicoJPanel.add(apellido2TecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 89, 130, 20));

        datosPersonalesTecnicoJPanel.add(apellido1TecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 68, 360, -1));

        datosPersonalesTecnicoJPanel.add(apellido2TecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 89, 360, -1));

        buscarDNITecnicoJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNITecnicoJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNITecnicoJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNITecnicoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNITecnicoJButtonActionPerformed();
            }
        });

        datosPersonalesTecnicoJPanel.add(buscarDNITecnicoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 20, 20));

        colegioTecnicoJLabel.setText("(*) Colegio:");
        datosPersonalesTecnicoJPanel.add(colegioTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 130, 20));

        visadoTecnicoJLabel.setText("(*) Visado:");
        datosPersonalesTecnicoJPanel.add(visadoTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 131, 130, 20));

        titulacionTecnicoJLabel.setText("Titulaci\u00f3n:");
        datosPersonalesTecnicoJPanel.add(titulacionTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 152, 130, 20));

        datosPersonalesTecnicoJPanel.add(colegioTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 360, -1));

        datosPersonalesTecnicoJPanel.add(visadoTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 131, 360, -1));

        datosPersonalesTecnicoJPanel.add(titulacionTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 152, 360, -1));

        tecnicoJPanel.add(datosPersonalesTecnicoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 182));

        datosNotificacionTecnicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionTecnicoJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionTecnicoJPanel.add(viaNotificacionTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        faxTecnicoJLabel.setText("Fax:");
        datosNotificacionTecnicoJPanel.add(faxTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 130, 20));

        telefonoTecnicoJLabel.setText("Tel\u00e9fono:");
        datosNotificacionTecnicoJPanel.add(telefonoTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 130, 20));

        movilTecnicoJLabel.setText("M\u00f3vil:");
        datosNotificacionTecnicoJPanel.add(movilTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 130, 20));

        emailTecnicoJLabel.setText("Email:");
        datosNotificacionTecnicoJPanel.add(emailTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 130, 20));

        tipoViaTecnicoJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionTecnicoJPanel.add(tipoViaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 130, 20));

        nombreViaTecnicoJLabel.setText("(*) Nombre:");
        datosNotificacionTecnicoJPanel.add(nombreViaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 151, 130, 20));

        numeroViaTecnicoJLabel.setText("(*) N\u00famero:");
        datosNotificacionTecnicoJPanel.add(numeroViaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 172, 70, 20));

        portalTecnicoJLabel.setText("Portal:");
        datosNotificacionTecnicoJPanel.add(portalTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 235, 70, 20));

        plantaTecnicoJLabel.setText("Planta:");
        datosNotificacionTecnicoJPanel.add(plantaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 193, 70, 20));

        escaleraTecnicoJLabel.setText("Escalera:");
        datosNotificacionTecnicoJPanel.add(escaleraTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 256, 70, 20));

        letraTecnicoJLabel.setText("Letra:");
        datosNotificacionTecnicoJPanel.add(letraTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 214, 70, 20));

        cPostalTecnicoJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionTecnicoJPanel.add(cPostalTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 277, 130, 20));

        municipioTecnicoJLabel.setText("(*) Municipio:");
        datosNotificacionTecnicoJPanel.add(municipioTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 298, 130, 20));

        provinciaTecnicoJLabel.setText("(*) Provincia:");
        datosNotificacionTecnicoJPanel.add(provinciaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 319, 130, 20));

        datosNotificacionTecnicoJPanel.add(faxTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 41, 360, -1));

        datosNotificacionTecnicoJPanel.add(telefonoTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 62, 360, -1));

        datosNotificacionTecnicoJPanel.add(movilTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 83, 360, -1));

        datosNotificacionTecnicoJPanel.add(emailTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 104, 360, -1));

        datosNotificacionTecnicoJPanel.add(nombreViaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 151, 360, -1));

        datosNotificacionTecnicoJPanel.add(numeroViaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 172, 150, -1));

        datosNotificacionTecnicoJPanel.add(plantaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 193, 150, -1));

        datosNotificacionTecnicoJPanel.add(portalTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 235, 150, -1));

        datosNotificacionTecnicoJPanel.add(escaleraTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 256, 150, -1));

        datosNotificacionTecnicoJPanel.add(letraTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 214, 150, -1));

        datosNotificacionTecnicoJPanel.add(cPostalTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 277, 360, -1));

        datosNotificacionTecnicoJPanel.add(municipioTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 298, 360, -1));

        datosNotificacionTecnicoJPanel.add(provinciaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 319, 360, -1));

        notificarTecnicoJCheckBox.setVisible(false);
        datosNotificacionTecnicoJPanel.add(notificarTecnicoJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 340, 200, -1));

        notificarTecnicoJLabel.setVisible(false);
        notificarTecnicoJLabel.setText("Notificar t\u00e9cnico:");
        datosNotificacionTecnicoJPanel.add(notificarTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 120, 20));

        tecnicoJPanel.add(datosNotificacionTecnicoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 182, 520, 365));

        promotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNIPromotorJLabel.setText("(*) DNI/CIF:");
        datosPersonalesPromotorJPanel.add(DNIPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        DNIPromotorJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIPromotorJTFieldKeyTyped();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIPromotorJTFieldKeyReleased();
            }
        });

        datosPersonalesPromotorJPanel.add(DNIPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 340, -1));

        nombrePromotorJLabel.setText("(*) Nombre:");
        datosPersonalesPromotorJPanel.add(nombrePromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 47, 130, 20));

        datosPersonalesPromotorJPanel.add(nombrePromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 47, 360, -1));

        apellido1PromotorJLabel.setText("Apellido1:");
        datosPersonalesPromotorJPanel.add(apellido1PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 68, 130, 20));

        apellido2PromotorJLabel.setText("Apellido2:");
        datosPersonalesPromotorJPanel.add(apellido2PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 89, 130, 20));

        datosPersonalesPromotorJPanel.add(apellido1PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 68, 360, -1));

        datosPersonalesPromotorJPanel.add(apellido2PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 89, 360, -1));

        buscarDNIPromotorJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNIPromotorJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIPromotorJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIPromotorJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIPromotorJButtonActionPerformed();
            }
        });

        datosPersonalesPromotorJPanel.add(buscarDNIPromotorJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 20, 20));

        colegioPromotorJLabel.setText("(*) Colegio:");
        datosPersonalesPromotorJPanel.add(colegioPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 130, 20));

        visadoPromotorJLabel.setText("(*) Visado:");
        datosPersonalesPromotorJPanel.add(visadoPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 131, 130, 20));

        titulacionPromotorJLabel.setText("Titulaci\u00f3n:");
        datosPersonalesPromotorJPanel.add(titulacionPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 152, 130, 20));

        datosPersonalesPromotorJPanel.add(colegioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 360, -1));

        datosPersonalesPromotorJPanel.add(visadoPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 131, 360, -1));

        datosPersonalesPromotorJPanel.add(titulacionPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 152, 360, -1));

        promotorJPanel.add(datosPersonalesPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 182));

        datosNotificacionPromotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionPromotorJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionPromotorJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionPromotorJPanel.add(viaNotificacionPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        faxPromotorJLabel.setText("Fax:");
        datosNotificacionPromotorJPanel.add(faxPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 130, 20));

        telefonoPromotorJLabel.setText("Tel\u00e9fono:");
        datosNotificacionPromotorJPanel.add(telefonoPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 130, 20));

        movilPromotorJLabel.setText("M\u00f3vil:");
        datosNotificacionPromotorJPanel.add(movilPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 130, 20));

        emailPromotorJLabel.setText("Email:");
        datosNotificacionPromotorJPanel.add(emailPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 130, 20));

        tipoViaPromotorJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionPromotorJPanel.add(tipoViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 130, 20));

        nombreViaPromotorJLabel.setText("(*) Nombre:");
        datosNotificacionPromotorJPanel.add(nombreViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 151, 130, 20));

        numeroViaPromotorJLabel.setText("(*) N\u00famero:");
        datosNotificacionPromotorJPanel.add(numeroViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 172, 80, 20));

        portalPromotorJLabel.setText("Portal:");
        datosNotificacionPromotorJPanel.add(portalPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 235, 50, 20));

        plantaPromotorJLabel.setText("Planta:");
        datosNotificacionPromotorJPanel.add(plantaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 193, 80, 20));

        escaleraPromotorJLabel.setText("Escalera:");
        datosNotificacionPromotorJPanel.add(escaleraPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 256, 60, 20));

        letraPromotorJLabel.setText("Letra:");
        datosNotificacionPromotorJPanel.add(letraPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 214, 80, 20));

        cPostalPromotorJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionPromotorJPanel.add(cPostalPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 277, 130, 20));

        municipioPromotorJLabel.setText("(*) Municipio:");
        datosNotificacionPromotorJPanel.add(municipioPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 298, 130, 20));

        provinciaPromotorJLabel.setText("(*) Provincia:");
        datosNotificacionPromotorJPanel.add(provinciaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 319, 130, 20));

        datosNotificacionPromotorJPanel.add(faxPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 41, 360, -1));

        datosNotificacionPromotorJPanel.add(telefonoPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 62, 360, -1));

        datosNotificacionPromotorJPanel.add(movilPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 83, 360, -1));

        datosNotificacionPromotorJPanel.add(emailPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 104, 360, -1));

        datosNotificacionPromotorJPanel.add(nombreViaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 151, 360, -1));

        datosNotificacionPromotorJPanel.add(numeroViaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 172, 150, -1));

        datosNotificacionPromotorJPanel.add(plantaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 193, 150, -1));

        datosNotificacionPromotorJPanel.add(portalPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 235, 150, -1));

        datosNotificacionPromotorJPanel.add(escaleraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 256, 150, -1));

        datosNotificacionPromotorJPanel.add(letraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 214, 150, -1));

        datosNotificacionPromotorJPanel.add(cPostalPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 277, 360, -1));

        datosNotificacionPromotorJPanel.add(municipioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 298, 360, -1));

        datosNotificacionPromotorJPanel.add(provinciaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 319, 360, -1));

        notificarPromotorJCheckBox.setVisible(false);
        datosNotificacionPromotorJPanel.add(notificarPromotorJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 340, 80, 20));

        notificarPromotorJLabel.setVisible(false);
        notificarPromotorJLabel.setText("Notificar promotor:");
        datosNotificacionPromotorJPanel.add(notificarPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 120, 20));

        promotorJPanel.add(datosNotificacionPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 182, 520, 365));

        templateJPanel.add(obraMayorJTabbedPane, BorderLayout.WEST);

        botoneraJPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        aceptarJButton.setText("Crear");
        aceptarJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aceptarJButtonMouseClicked();
            }
        });

        aceptarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(aceptarJButton);

        cancelarJButton.setText("Salir");
        cancelarJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelarJButtonMouseClicked();
            }
        });

        cancelarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(cancelarJButton);

        templateJPanel.add(botoneraJPanel, BorderLayout.SOUTH);

        editorMapaJPanel.setLayout(new java.awt.GridLayout(1, 0));

        editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder("Mapa"));
        templateJPanel.add(editorMapaJPanel, BorderLayout.CENTER);

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane);

    }//GEN-END:initComponents

    private void fechaLimiteObraJButtonActionPerformed() {//GEN-FIRST:event_fechaLimiteObraJButtonActionPerformed
        CUtilidadesComponentes.showCalendarDialog(desktop);

        if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
            fechaLimiteObraJTField.setText(CConstantesLicencias.calendarValue);
        }

    }//GEN-LAST:event_fechaLimiteObraJButtonActionPerformed

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
        tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposObraMenor(), null, CMainLicencias.currentLocale.toString(), false);
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

        viaNotificacionTecnicoEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainLicencias.currentLocale.toString(), false);
        viaNotificacionTecnicoEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionTecnicoEJCBoxActionPerformed();}});

        datosNotificacionTecnicoJPanel.add(viaNotificacionTecnicoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 360, 20));
        tipoViaNotificacionTecnicoEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), false);
        datosNotificacionTecnicoJPanel.add(tipoViaNotificacionTecnicoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 360, 20));

        viaNotificacionPromotorEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainLicencias.currentLocale.toString(), false);
        viaNotificacionPromotorEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionPromotorEJCBoxActionPerformed();}});

        datosNotificacionPromotorJPanel.add(viaNotificacionPromotorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 360, 20));
        tipoViaNotificacionPromotorEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), false);
        datosNotificacionPromotorJPanel.add(tipoViaNotificacionPromotorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 360, 20));

        tipoViaINEEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), true);
        tipoViaINEEJCBox.setSelectedIndex(0);
        emplazamientoJPanel.add(tipoViaINEEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 37, 110, 20));
    }

    private void viaNotificacionTitularEJCBoxActionPerformed() {
        String index= viaNotificacionTitularEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailTitularJLabel.text")));
            emailTitularObligatorio= true;
        }else{
            emailTitularJLabel.setText(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailTitularJLabel.text"));
            emailTitularObligatorio= false;
        }

    }

    private void viaNotificacionTecnicoEJCBoxActionPerformed() {
        String index= viaNotificacionTecnicoEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailTecnicoJLabel.text")));
            emailTecnicoObligatorio= true;
        }else{
            emailTecnicoJLabel.setText(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailTecnicoJLabel.text"));
            emailTecnicoObligatorio= false;
        }

    }


    private void viaNotificacionRepresentaAEJCBoxActionPerformed() {

        String index= viaNotificacionRepresentaAEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailRepresentaAJLabel.text")));
            emailRepresentanteObligatorio= true;
        }else{
            emailRepresentaAJLabel.setText(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailRepresentaAJLabel.text"));
            emailRepresentanteObligatorio= false;
        }

    }

    private void viaNotificacionPromotorEJCBoxActionPerformed() {

        String index= viaNotificacionPromotorEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailPromotorJLabel.text")));
            emailPromotorObligatorio= true;
        }else{
            emailPromotorJLabel.setText(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailPromotorJLabel.text"));
            emailPromotorObligatorio= false;
        }

    }



    private void referenciasCatastralesJTableMouseClicked() {//GEN-FIRST:event_referenciasCatastralesJTableMouseClicked

        try {

            int selectedRow = referenciasCatastralesJTable.getSelectedRow();
            logger.info("selectedRow: " + selectedRow);

            if (selectedRow != -1) {

                logger.info("referenciasCatastralesJTable.getValueAt(selectedRow, 0): " + referenciasCatastralesJTable.getValueAt(selectedRow, 0));

                CConstantesLicencias.referencia.setReferenciaCatastral((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 0));
                Object objeto=referenciasCatastralesJTableModel.getValueAt(selectedRow, 1);
                String patron=null;
                CConstantesLicencias.referencia.setTipoVia("");
                if ((objeto instanceof DomainNode) && objeto!=null)
                {
                    CConstantesLicencias.referencia.setTipoVia(((DomainNode)objeto).getTerm(CMainLicencias.literales.getLocale().toString()));
                    patron=((DomainNode)objeto).getPatron();
                }
                if ((objeto instanceof String) && objeto!=null)
                {
                    if (((String)objeto).length()>0)
                    {
                        CConstantesLicencias.referencia.setTipoVia(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto).getTerm(CMainLicencias.literales.getLocale().toString()));
                        patron=((String)objeto);
                    }
                }
                CConstantesLicencias.referencia.setNombreVia((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 2));
                CConstantesLicencias.referencia.setPrimerNumero((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 3));
                CConstantesLicencias.referencia.setPrimeraLetra((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 4));
                CConstantesLicencias.referencia.setBloque((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 5));
                CConstantesLicencias.referencia.setEscalera((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 6));
                CConstantesLicencias.referencia.setPlanta((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 7));
                CConstantesLicencias.referencia.setPuerta((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 8));
                CConstantesLicencias.referencia.setCPostal((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 9));
                CUtilidadesComponentes.showDatosReferenciaCatastralDialog(desktop, true,CMainLicencias.literales);

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







	private void buscarDNIPromotorJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIPromotorJButtonActionPerformed

		logger.info("Inicio.");

		CUtilidadesComponentes.showPersonaDialog(desktop,CMainLicencias.literales);

		if ((CConstantesLicencias.persona != null) && (CConstantesLicencias.persona.getDniCif() != null)) {
            DNIPromotorJTField.setText(CConstantesLicencias.persona.getDniCif());
            nombrePromotorJTField.setText(CConstantesLicencias.persona.getNombre());
            apellido1PromotorJTField.setText(CConstantesLicencias.persona.getApellido1());
            apellido2PromotorJTField.setText(CConstantesLicencias.persona.getApellido2());
            colegioPromotorJTField.setText(CConstantesLicencias.persona.getColegio());
            visadoPromotorJTField.setText(CConstantesLicencias.persona.getVisado());
            titulacionPromotorJTField.setText(CConstantesLicencias.persona.getTitulacion());
            faxPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getFax());
            telefonoPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getTelefono());
            movilPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getMovil());
            emailPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionPromotorEJCBox.setSelectedPatron(CConstantesLicencias.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicencias.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionPromotorEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionPromotorEJCBox.setSelectedPatron(new Integer(CConstantesLicencias.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicencias.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionPromotorEJCBox.setSelected(auxNode.getIdNode());                
            }


            nombreViaPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getNombreVia());
            numeroViaPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getNumeroVia());
            plantaPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getPlanta());
            portalPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getPortal());
            escaleraPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getEscalera());
            letraPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getLetra());
            cPostalPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getCpostal());
            municipioPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getMunicipio());
            provinciaPromotorJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getProvincia());
            /*
            if (CConstantesLicencias.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarPromotorJCheckBox.setSelected(true);
            else notificarPromotorJCheckBox.setSelected(false);
            */
		}


	}//GEN-LAST:event_buscarDNIPromotorJButtonActionPerformed

	private void buscarDNITecnicoJButtonActionPerformed() {//GEN-FIRST:event_buscarDNITecnicoJButtonActionPerformed

		logger.info("Inicio.");

		CUtilidadesComponentes.showPersonaDialog(desktop,CMainLicencias.literales);

		if ((CConstantesLicencias.persona != null) && (CConstantesLicencias.persona.getDniCif() != null)) {
            DNITecnicoJTField.setText(CConstantesLicencias.persona.getDniCif());
            nombreTecnicoJTField.setText(CConstantesLicencias.persona.getNombre());
            apellido1TecnicoJTField.setText(CConstantesLicencias.persona.getApellido1());
            apellido2TecnicoJTField.setText(CConstantesLicencias.persona.getApellido2());
            colegioTecnicoJTField.setText(CConstantesLicencias.persona.getColegio());
            visadoTecnicoJTField.setText(CConstantesLicencias.persona.getVisado());
            titulacionTecnicoJTField.setText(CConstantesLicencias.persona.getTitulacion());
            faxTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getFax());
            telefonoTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getTelefono());
            movilTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getMovil());
            emailTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionTecnicoEJCBox.setSelectedPatron(CConstantesLicencias.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicencias.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionTecnicoEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionTecnicoEJCBox.setSelectedPatron(new Integer(CConstantesLicencias.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicencias.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionTecnicoEJCBox.setSelected(auxNode.getIdNode());
            }

            nombreViaTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getNombreVia());
            numeroViaTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getNumeroVia());
            plantaTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getPlanta());
            portalTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getPortal());
            escaleraTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getEscalera());
            letraTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getLetra());
            cPostalTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getCpostal());
            municipioTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getMunicipio());
            provinciaTecnicoJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getProvincia());
            if (CConstantesLicencias.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarTecnicoJCheckBox.setSelected(true);
            else notificarTecnicoJCheckBox.setSelected(false);

		}


	}//GEN-LAST:event_buscarDNITecnicoJButtonActionPerformed

	private void buscarDNIRepresentaAJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIRepresentaAJButtonActionPerformed

		logger.info("Inicio.");

		CUtilidadesComponentes.showPersonaDialog(desktop, CMainLicencias.literales);

		if ((CConstantesLicencias.persona != null) && (CConstantesLicencias.persona.getDniCif() != null)) {
            DNIRepresentaAJTField.setText(CConstantesLicencias.persona.getDniCif());
            nombreRepresentaAJTField.setText(CConstantesLicencias.persona.getNombre());
            apellido1RepresentaAJTField.setText(CConstantesLicencias.persona.getApellido1());
            apellido2RepresentaAJTField.setText(CConstantesLicencias.persona.getApellido2());
            faxRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getFax());
            telefonoRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getTelefono());
            movilRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getMovil());
            emailRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionRepresentaAEJCBox.setSelectedPatron(CConstantesLicencias.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicencias.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionRepresentaAEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionRepresentaAEJCBox.setSelectedPatron(new Integer(CConstantesLicencias.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicencias.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionRepresentaAEJCBox.setSelected(auxNode.getIdNode());
            }

            nombreViaRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getNombreVia());
            numeroViaRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getNumeroVia());
            plantaRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getPlanta());
            portalRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getPortal());
            escaleraRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getEscalera());
            letraRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getLetra());
            cPostalRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getCpostal());
            municipioRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getMunicipio());
            provinciaRepresentaAJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getProvincia());
            if (CConstantesLicencias.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarRepresentaAJCheckBox.setSelected(true);
            else notificarRepresentaAJCheckBox.setSelected(false);

		}


	}//GEN-LAST:event_buscarDNIRepresentaAJButtonActionPerformed

	private void buscarDNITitularJButtonActionPerformed() {//GEN-FIRST:event_buscarDNITitularJButtonActionPerformed

		logger.info("Inicio.");

		CUtilidadesComponentes.showPersonaDialog(desktop,CMainLicencias.literales);

		if ((CConstantesLicencias.persona != null) && (CConstantesLicencias.persona.getDniCif() != null)) {
            DNITitularJTField.setText(CConstantesLicencias.persona.getDniCif());
            nombreTitularJTField.setText(CConstantesLicencias.persona.getNombre());
            apellido1TitularJTField.setText(CConstantesLicencias.persona.getApellido1());
            apellido2TitularJTField.setText(CConstantesLicencias.persona.getApellido2());
            faxTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getFax());
            telefonoTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getTelefono());
            movilTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getMovil());
            emailTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionTitularEJCBox.setSelectedPatron(CConstantesLicencias.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicencias.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionTitularEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionTitularEJCBox.setSelectedPatron(new Integer(CConstantesLicencias.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicencias.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionTitularEJCBox.setSelected(auxNode.getIdNode());
            }

            nombreViaTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getNombreVia());
            numeroViaTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getNumeroVia());
            plantaTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getPlanta());
            portalTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getPortal());
            escaleraTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getEscalera());
            letraTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getLetra());
            cPostalTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getCpostal());
            municipioTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getMunicipio());
            provinciaTitularJTField.setText(CConstantesLicencias.persona.getDatosNotificacion().getProvincia());
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




	private void DNIPromotorJTFieldKeyReleased() {//GEN-FIRST:event_DNIPromotorJTFieldKeyReleased
	    if (DNIPromotorJTField.getDocument() != null) {
			if (DNIPromotorJTField.getText().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Promotor = DNIPromotorJTField.getText();
			} else if (DNIPromotorJTField.getText().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIPromotorJTField.setText(_DNI_CIF_Promotor);
			}
		}
	}//GEN-LAST:event_DNIPromotorJTFieldKeyReleased

	private void DNIPromotorJTFieldKeyTyped() {//GEN-FIRST:event_DNIPromotorJTFieldKeyTyped
		if (DNIPromotorJTField.getDocument() != null) {
			if (DNIPromotorJTField.getText().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Promotor = DNIPromotorJTField.getText();
			} else if (DNIPromotorJTField.getText().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIPromotorJTField.setText(_DNI_CIF_Promotor);
			}
		}
	}//GEN-LAST:event_DNIPromotorJTFieldKeyTyped

	private void DNITecnicoJTFieldKeyReleased() {//GEN-FIRST:event_DNITecnicoJTFieldKeyReleased
		if (DNITecnicoJTField.getDocument() != null) {
			if (DNITecnicoJTField.getText().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Tecnico = DNITecnicoJTField.getText();
			} else if (DNITecnicoJTField.getText().length() > CConstantesLicencias.MaxLengthDNI) {
				DNITecnicoJTField.setText(_DNI_CIF_Tecnico);
			}
		}
	}//GEN-LAST:event_DNITecnicoJTFieldKeyReleased

	private void DNITecnicoJTFieldKeyTyped() {//GEN-FIRST:event_DNITecnicoJTFieldKeyTyped
		if (DNITecnicoJTField.getDocument() != null) {
			if (DNITecnicoJTField.getText().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Tecnico = DNITecnicoJTField.getText();
			} else if (DNITecnicoJTField.getText().length() > CConstantesLicencias.MaxLengthDNI) {
				DNITecnicoJTField.setText(_DNI_CIF_Tecnico);
			}
		}
	}//GEN-LAST:event_DNITecnicoJTFieldKeyTyped

	private void DNIRepresentaAJTFieldKeyReleased() {//GEN-FIRST:event_DNIRepresentaAJTFieldKeyReleased
		if (DNIRepresentaAJTField.getDocument() != null) {
			if (DNIRepresentaAJTField.getText().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_RepresentaA = DNIRepresentaAJTField.getText();
			} else if (DNIRepresentaAJTField.getText().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIRepresentaAJTField.setText(_DNI_CIF_RepresentaA);
			}
		}
	}//GEN-LAST:event_DNIRepresentaAJTFieldKeyReleased

	private void DNIRepresentaAJTFieldKeyTyped() {//GEN-FIRST:event_DNIRepresentaAJTFieldKeyTyped
		if (DNIRepresentaAJTField.getDocument() != null) {
			if (DNIRepresentaAJTField.getText().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_RepresentaA = DNIRepresentaAJTField.getText();
			} else if (DNIRepresentaAJTField.getText().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIRepresentaAJTField.setText(_DNI_CIF_RepresentaA);
			}
		}
	}//GEN-LAST:event_DNIRepresentaAJTFieldKeyTyped

	private void DNITitularJTFieldKeyReleased() {//GEN-FIRST:event_DNITitularJTFieldKeyReleased
		if (DNITitularJTField.getDocument() != null) {
			if (DNITitularJTField.getText().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Titular = DNITitularJTField.getText();
			} else if (DNITitularJTField.getText().length() > CConstantesLicencias.MaxLengthDNI) {
				DNITitularJTField.setText(_DNI_CIF_Titular);
			}
		}
	}//GEN-LAST:event_DNITitularJTFieldKeyReleased

	private void DNITitularJTFieldKeyTyped() {//GEN-FIRST:event_DNITitularJTFieldKeyTyped
		if (DNITitularJTField.getDocument() != null) {
			if (DNITitularJTField.getText().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Titular = DNITitularJTField.getText();
			} else if (DNITitularJTField.getText().length() > CConstantesLicencias.MaxLengthDNI) {
				DNITitularJTField.setText(_DNI_CIF_Titular);
			}
		}
	}//GEN-LAST:event_DNITitularJTFieldKeyTyped


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
            if (rellenaCamposObligatorios()) {
                /** Comprobamos los datos de entrada */

                /** Datos del propietario, representante, tecnico y promotor */
                if (datosTitularOK() && datosRepresentaAOK() && datosTecnicoOK() && datosPromotorOK()) {
                    /** Datos de la solicitud */
                    int index = estadoJCBox.getSelectedIndex();
                    CEstado estado = (CEstado) _hEstado.get(new Integer(index));

                    CTipoObra tipoObra = new CTipoObra((new Integer(tipoObraEJCBox.getSelectedPatron())).intValue(), "", "");

                    _unidadRegistro = unidadRJTField.getText();
                    _unidadTramitadora = unidadTJTField.getText();
                    if (excedeLongitud(_unidadRegistro, CConstantesLicencias.MaxLengthUnidad) || excedeLongitud(_unidadTramitadora, CConstantesLicencias.MaxLengthUnidad)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje1"));
                        return;
                    }

                    _motivo = motivoJTField.getText();
                    if (excedeLongitud(_motivo, CConstantesLicencias.MaxLengthMotivo)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje2"));
                        return;
                    }
                    _asunto = asuntoJTField.getText();
                    if (excedeLongitud(_asunto, CConstantesLicencias.MaxLengthAsunto)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje3"));
                        return;
                    }

                    _observaciones = observacionesJTArea.getText();
                    if (excedeLongitud(_observaciones, CConstantesLicencias.MaxLengthObservaciones)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje5"));
                        return;
                    }

    //***********************************************
    //** Datos del titular
    //*****************************************

                    int viaNotificacionIndex = new Integer(viaNotificacionTitularEJCBox.getSelectedPatron()).intValue();
                    CViaNotificacion viaNotificacion= new CViaNotificacion(viaNotificacionIndex, "");

                    if ((_seNotificaRepresentaA == 0) && (_seNotificaTecnico == 0) && (_seNotificaPromotor == 0))
                        _seNotificaTitular= 1;

                    String fax= "";
                    try{fax= faxTitularJTField.getNumber().toString();}catch(Exception e){}
                    String telefono= "";
                    try{telefono= telefonoTitularJTField.getNumber().toString();}catch (Exception e){}
                    String movil= "";
                    try{movil=  movilTitularJTField.getNumber().toString();}catch(Exception e){}
                    String numero= "";
                    try{numero= numeroViaTitularJTField.getNumber().toString();}catch(Exception e){}
                    String cPostal= "";
                    try{cPostal= cPostalTitularJTField.getNumber().toString();}catch(Exception e){}
                    CDatosNotificacion datosNotificacion = new CDatosNotificacion(DNITitularJTField.getText().trim(),
                            viaNotificacion,
                            fax,
                            telefono,
                            movil,
                            emailTitularJTField.getText().trim(),
                            tipoViaNotificacionTitularEJCBox.getSelectedPatron(),
                            nombreViaTitularJTField.getText().trim(),
                            numero,
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

                    CPersonaJuridicoFisica propietario = new CPersonaJuridicoFisica(DNITitularJTField.getText().trim(),
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
                        viaNotificacionIndex = new Integer(viaNotificacionRepresentaAEJCBox.getSelectedPatron()).intValue();
                        viaNotificacion= new CViaNotificacion(viaNotificacionIndex, "");

                        fax= "";
                        try{fax= faxRepresentaAJTField.getNumber().toString();}catch(Exception e){}
                        telefono= "";
                        try{telefono= telefonoRepresentaAJTField.getNumber().toString();}catch (Exception e){}
                        movil= "";
                        try{movil=  movilRepresentaAJTField.getNumber().toString();}catch(Exception e){}
                        numero= "";
                        try{numero= numeroViaRepresentaAJTField.getNumber().toString();}catch(Exception e){}
                        cPostal= "";
                        try{cPostal= cPostalRepresentaAJTField.getNumber().toString();}catch(Exception e){}
                        datosNotificacion = new CDatosNotificacion(DNIRepresentaAJTField.getText().trim(),
                                viaNotificacion,
                                fax,
                                telefono,
                                movil,
                                emailRepresentaAJTField.getText().trim(),
                                tipoViaNotificacionRepresentaAEJCBox.getSelectedPatron(),
                                nombreViaRepresentaAJTField.getText().trim(),
                                numero,
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

                        representante = new CPersonaJuridicoFisica(DNIRepresentaAJTField.getText().trim(),
                                nombreRepresentaAJTField.getText().trim(),
                                apellido1RepresentaAJTField.getText().trim(),
                                apellido2RepresentaAJTField.getText().trim(),
                                "",
                                "",
                                "",
                                datosNotificacion);
                    }else representante= null;


    //***********************************************
    //** Datos del Tecnico
    //*****************************************
                    Vector vTecnicos= new Vector();
                    if (_flagTecnico){
                        CPersonaJuridicoFisica tecnico= new CPersonaJuridicoFisica();
                        viaNotificacionIndex = new Integer(viaNotificacionTecnicoEJCBox.getSelectedPatron()).intValue();
                        viaNotificacion= new CViaNotificacion(viaNotificacionIndex, "");

                        fax= "";
                        try{fax= faxTecnicoJTField.getNumber().toString();}catch(Exception e){}
                        telefono= "";
                        try{telefono= telefonoTecnicoJTField.getNumber().toString();}catch (Exception e){}
                        movil= "";
                        try{movil=  movilTecnicoJTField.getNumber().toString();}catch(Exception e){}
                        numero= "";
                        try{numero= numeroViaTecnicoJTField.getNumber().toString();}catch(Exception e){}
                        cPostal= "";
                        try{cPostal= cPostalTecnicoJTField.getNumber().toString();}catch(Exception e){}
                        datosNotificacion = new CDatosNotificacion(DNITecnicoJTField.getText().trim(),
                                viaNotificacion,
                                fax,
                                telefono,
                                movil,
                                emailTecnicoJTField.getText().trim(),
                                tipoViaNotificacionTecnicoEJCBox.getSelectedPatron(),
                                nombreViaTecnicoJTField.getText().trim(),
                                numero,
                                portalTecnicoJTField.getText().trim(),
                                plantaTecnicoJTField.getText().trim(),
                                escaleraTecnicoJTField.getText().trim(),
                                letraTecnicoJTField.getText().trim(),
                                cPostal,
                                municipioTecnicoJTField.getText().trim(),
                                provinciaTecnicoJTField.getText().trim(),
                                (notificarTecnicoJCheckBox.isSelected() ? "1" : "0"));

                        logger.info("viaNotificacion.getIdViaNotificacion(): " + viaNotificacion.getIdViaNotificacion());
                        logger.info("datosNotificacion.getNotificar(): " + datosNotificacion.getNotificar());


                        tecnico= new CPersonaJuridicoFisica(DNITecnicoJTField.getText().trim(),
                                nombreTecnicoJTField.getText().trim(),
                                apellido1TecnicoJTField.getText().trim(),
                                apellido2TecnicoJTField.getText().trim(),
                                colegioTecnicoJTField.getText().trim(),
                                visadoTecnicoJTField.getText().trim(),
                                titulacionTecnicoJTField.getText().trim(),
                                datosNotificacion);
                        vTecnicos.add(tecnico);
                    }







    //***********************************************
    //** Datos del Promotor
    //*****************************************
                    CPersonaJuridicoFisica promotor= new CPersonaJuridicoFisica();

                    if (_flagPromotor){
                        viaNotificacionIndex = new Integer(viaNotificacionPromotorEJCBox.getSelectedPatron()).intValue();
                        viaNotificacion= new CViaNotificacion(viaNotificacionIndex, "");

                        fax= "";
                        try{fax= faxPromotorJTField.getNumber().toString();}catch(Exception e){}
                        telefono= "";
                        try{telefono= telefonoPromotorJTField.getNumber().toString();}catch (Exception e){}
                        movil= "";
                        try{movil=  movilPromotorJTField.getNumber().toString();}catch(Exception e){}
                        numero= "";
                        try{numero= numeroViaPromotorJTField.getNumber().toString();}catch(Exception e){}
                        cPostal= "";
                        try{cPostal= cPostalPromotorJTField.getNumber().toString();}catch(Exception e){}
                        datosNotificacion = new CDatosNotificacion(DNIPromotorJTField.getText().trim(),
                                viaNotificacion,
                                fax,
                                telefono,
                                movil,
                                emailPromotorJTField.getText().trim(),
                                tipoViaNotificacionPromotorEJCBox.getSelectedPatron(),
                                nombreViaPromotorJTField.getText().trim(),
                                numero,
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

                        promotor = new CPersonaJuridicoFisica(DNIPromotorJTField.getText().trim(),
                                nombrePromotorJTField.getText().trim(),
                                apellido1PromotorJTField.getText().trim(),
                                apellido2PromotorJTField.getText().trim(),
                                colegioPromotorJTField.getText().trim(),
                                visadoPromotorJTField.getText().trim(),
                                titulacionPromotorJTField.getText().trim(),
                                datosNotificacion);
                    }else promotor= null;


                    CTipoLicencia tipoLicencia = new CTipoLicencia(CConstantesLicencias.ObraMenor, "", "");
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
                            CConstantesLicencias.referencia.setTipoVia(((DomainNode)objeto).getTerm(CMainLicencias.literales.getLocale().toString()));
                            tipoVia=((DomainNode)objeto).getPatron();
                        }
                        if ((objeto instanceof String) && objeto!=null)
                        {
                            if (((String)objeto).length()>0)
                            {
                                CConstantesLicencias.referencia.setTipoVia(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto).getTerm(CMainLicencias.literales.getLocale().toString()));
                                tipoVia=((String)objeto);
                            }
                        }

                        String ref_Catastral = (String) referenciasCatastralesJTable.getValueAt(i, 0);

                        String nombre= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 2)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 2)).trim().equalsIgnoreCase(""))){
                           nombre= ((String)referenciasCatastralesJTable.getValueAt(i, 2)).trim();
                        }
                        numero= null;
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

                    double tasa= 0.00;
                    try {
                        tasa= tasaTextField.getNumber().doubleValue();
                    } catch (Exception ex) {
                        logger.warn("Tasa no válida. tasaTextField.getText(): " + tasaTextField.getText());
                    }

                    double impuesto= 0.00;
                    try {
                        impuesto= impuestoTextField.getNumber().doubleValue();
                    } catch (Exception ex) {
                        logger.warn("Impuesto no válida. impuestoTextField.getText(): " + impuestoTextField.getText());
                    }

                    /** EMPLAZAMIENTO */
                    String tipoViaINE= null;
                    if (tipoViaINEEJCBox.getSelectedIndex()!=0){
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
                            motivoJTField.getText(),
                            asuntoJTField.getText(),
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
                            observacionesJTArea.getText(),
                            documentacionJPanel.getAnexos(),
                            referenciasCatastrales);

                    /** Documentacion entregada de caracter obligatorio */
                    solicitudLicencia.setDocumentacionEntregada(documentacionJPanel.getDocumentacionObligatoriaSeleccionada());
                    solicitudLicencia.setObservacionesDocumentacionEntregada(documentacionJPanel.getObservacionesGenerales());

                    /** Tecnicos */
                    solicitudLicencia.setTecnicos(vTecnicos);
                    /** Impuesto */
                    solicitudLicencia.setImpuesto(impuesto);
                    /** Fecha resolucion */
                    solicitudLicencia.setFechaResolucion(null);
                    /** fecha limite de obra */
                    Date dateFechaLimite= CUtilidades.parseFechaStringToDate(fechaLimiteObraJTField.getText());
                    solicitudLicencia.setFechaLimiteObra(dateFechaLimite);

                    CResultadoOperacion resultado = COperacionesLicencias.crearExpediente(solicitudLicencia, expedienteLicencia);
                    logger.info("resultado.getResultado(): " + resultado.getResultado());
                    logger.info("resultado.getDescripcion(): " + resultado.getDescripcion());

                    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    if (resultado.getResultado()){
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje16")+ " " + resultado.getDescripcion());
                        CUtilidadesComponentes.addFeatureCapa(geopistaEditor, "licencias_obra_menor", referenciasCatastralesJTable, referenciasCatastralesJTableModel);
                        refreshFeatureSelection();
                        clearAll();
                    }
                    else{
                        /** Comprobamos que no se haya excedido el maximo FileUploadBase.SizeLimitExceededException */
                       if (resultado.getDescripcion().equalsIgnoreCase("FileUploadBase.SizeLimitExceededException")){
                           JOptionPane optionPane= new JOptionPane(CMainLicencias.literales.getString("AnexosJPanel.Message1"), JOptionPane.ERROR_MESSAGE);
                           JDialog dialog =optionPane.createDialog(this,"ERROR");
                           dialog.show();
                       }else{                        
                         JOptionPane optionPane= new JOptionPane(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje19") + "\n"+resultado.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                         JDialog dialog =optionPane.createDialog(this,"ERROR");
                         dialog.show();
                       }
                    }
                }
            } else {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje17"));
            }
        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }

	}//GEN-LAST:event_aceptarJButtonMouseClicked


	private void observacionesJTAreaKeyReleased() {//GEN-FIRST:event_observacionesJTAreaKeyReleased
		if (observacionesJTArea.getDocument() != null) {
			if (observacionesJTArea.getText().length() <= CConstantesLicencias.MaxLengthObservaciones) {
				_observaciones = observacionesJTArea.getText();
			} else if (observacionesJTArea.getText().length() > CConstantesLicencias.MaxLengthObservaciones) {
				observacionesJTArea.setText(_observaciones);
			}
		}
	}//GEN-LAST:event_observacionesJTAreaKeyReleased

	private void observacionesJTAreaKeyTyped() {//GEN-FIRST:event_observacionesJTAreaKeyTyped
		if (observacionesJTArea.getDocument() != null) {
			if (observacionesJTArea.getText().length() <= CConstantesLicencias.MaxLengthObservaciones) {
				_observaciones = observacionesJTArea.getText();
			} else if (observacionesJTArea.getText().length() > CConstantesLicencias.MaxLengthObservaciones) {
				observacionesJTArea.setText(_observaciones);
			}
		}
	}//GEN-LAST:event_observacionesJTAreaKeyTyped





	/*******************************************************************/
/*                         Metodos propios                         */
	/**
	 * ****************************************************************
	 */
    /**
     * Datos Titular
     */


	public boolean datosTitularOK() {

        try{
            if (excedeLongitud(_DNI_CIF_Titular, CConstantesLicencias.MaxLengthDNI)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje1"));
                return false;
            }
            if (excedeLongitud(_nombreTitular, CConstantesLicencias.MaxLengthNombre)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje2"));
                return false;
            }
            if (excedeLongitud(_apellido1Titular, CConstantesLicencias.MaxLengthApellido) || excedeLongitud(_apellido2Titular, CConstantesLicencias.MaxLengthApellido)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje3"));
                return false;
            }
            _emailTitular = emailTitularJTField.getText();
            if (excedeLongitud(_emailTitular, CConstantesLicencias.MaxLengthEmail)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje6"));
                return false;
            }
            _nombreViaTitular = nombreViaTitularJTField.getText();
            if (excedeLongitud(_nombreViaTitular, CConstantesLicencias.MaxLengthNombreVia)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje8"));
                return false;
            }
            _portalTitular = portalTitularJTField.getText();
            if (_portalTitular.length() > 0) {
                if (excedeLongitud(_portalTitular, CConstantesLicencias.MaxLengthPortal)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje11"));
                    return false;
                }
            }
            _plantaTitular = plantaTitularJTField.getText();
            if (_plantaTitular.length() > 0) {
                if (excedeLongitud(_plantaTitular, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje13"));
                    return false;
                }
            }
            _escaleraTitular = escaleraTitularJTField.getText();
            if (excedeLongitud(_escaleraTitular, CConstantesLicencias.MaxLengthPlanta)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje14"));
                return false;
            }
            _letraTitular = letraTitularJTField.getText();
            if (excedeLongitud(_letraTitular, CConstantesLicencias.MaxLengthLetra)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje15"));
                return false;
            }
            _municipioTitular = municipioTitularJTField.getText();
            if (excedeLongitud(_municipioTitular, CConstantesLicencias.MaxLengthMunicipio)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje18"));
                return false;
            }

            _provinciaTitular = provinciaTitularJTField.getText();
            if (excedeLongitud(_provinciaTitular, CConstantesLicencias.MaxLengthProvincia)) {
                mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje19"));
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
                if (excedeLongitud(_DNI_CIF_RepresentaA, CConstantesLicencias.MaxLengthDNI)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje1"));
                    return false;
                }
                if (excedeLongitud(_nombreRepresentaA, CConstantesLicencias.MaxLengthNombre)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje2"));
                    return false;
                }
                if (excedeLongitud(_apellido1RepresentaA, CConstantesLicencias.MaxLengthApellido) || excedeLongitud(_apellido2RepresentaA, CConstantesLicencias.MaxLengthApellido)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje3"));
                    return false;
                }
                _emailRepresentaA = emailRepresentaAJTField.getText();
                if (excedeLongitud(_emailRepresentaA, CConstantesLicencias.MaxLengthEmail)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje6"));
                    return false;
                }
                _nombreViaRepresentaA = nombreViaRepresentaAJTField.getText();
                if (excedeLongitud(_nombreViaRepresentaA, CConstantesLicencias.MaxLengthNombreVia)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje8"));
                    return false;
                }
                _portalRepresentaA = portalRepresentaAJTField.getText();
                if (_portalRepresentaA.length() > 0) {
                    if (excedeLongitud(_portalRepresentaA, CConstantesLicencias.MaxLengthPortal)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje11"));
                        return false;
                    }
                }
                _plantaRepresentaA = plantaRepresentaAJTField.getText();
                if (_plantaRepresentaA.length() > 0) {
                    if (excedeLongitud(_plantaRepresentaA, CConstantesLicencias.MaxLengthPlanta)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje13"));
                        return false;
                    }
                }
                _escaleraRepresentaA = escaleraRepresentaAJTField.getText();
                if (excedeLongitud(_escaleraRepresentaA, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje14"));
                    return false;
                }
                _letraRepresentaA = letraRepresentaAJTField.getText();
                if (excedeLongitud(_letraRepresentaA, CConstantesLicencias.MaxLengthLetra)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje15"));
                    return false;
                }
                _municipioRepresentaA = municipioRepresentaAJTField.getText();
                if (excedeLongitud(_municipioRepresentaA, CConstantesLicencias.MaxLengthMunicipio)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje18"));
                    return false;
                }

                _provinciaRepresentaA = provinciaRepresentaAJTField.getText();
                if (excedeLongitud(_provinciaRepresentaA, CConstantesLicencias.MaxLengthProvincia)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje19"));
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
            DNIRepresentaAJTField.setText("");
            nombreRepresentaAJTField.setText("");
            apellido1RepresentaAJTField.setText("");
            apellido2RepresentaAJTField.setText("");
            faxRepresentaAJTField.setText("");
            telefonoRepresentaAJTField.setText("");
            movilRepresentaAJTField.setText("");
            emailRepresentaAJTField.setText("");
            nombreViaRepresentaAJTField.setText("");
            numeroViaRepresentaAJTField.setText("");
            plantaRepresentaAJTField.setText("");
            letraRepresentaAJTField.setText("");
            portalRepresentaAJTField.setText("");
            escaleraRepresentaAJTField.setText("");
            cPostalRepresentaAJTField.setText("");
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
     * Datos Tecnico
     */

	public boolean datosTecnicoOK() {

        try{
            if (_flagTecnico){
                if (excedeLongitud(_DNI_CIF_Tecnico, CConstantesLicencias.MaxLengthDNI)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje1"));
                    return false;
                }
                if (excedeLongitud(_nombreTecnico, CConstantesLicencias.MaxLengthNombre)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje2"));
                    return false;
                }
                if (excedeLongitud(_apellido1Tecnico, CConstantesLicencias.MaxLengthApellido) || excedeLongitud(_apellido2Tecnico, CConstantesLicencias.MaxLengthApellido)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje3"));
                    return false;
                }
                if (excedeLongitud(_colegioTecnico, CConstantesLicencias.MaxLengthColegio)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje4"));
                    return false;
                }
                if (excedeLongitud(_visadoTecnico, CConstantesLicencias.MaxLengthVisado)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje5"));
                    return false;
                }
                if (excedeLongitud(_titulacionTecnico, CConstantesLicencias.MaxLengthTitulacion)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje6"));
                    return false;
                }
                _emailTecnico = emailTecnicoJTField.getText();
                if (excedeLongitud(_emailTecnico, CConstantesLicencias.MaxLengthEmail)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje9"));
                    return false;
                }
                _nombreViaTecnico = nombreViaTecnicoJTField.getText();
                if (excedeLongitud(_nombreViaTecnico, CConstantesLicencias.MaxLengthNombreVia)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje11"));
                    return false;
                }
                _portalTecnico = portalTecnicoJTField.getText();
                if (_portalTecnico.length() > 0) {
                    if (excedeLongitud(_portalTecnico, CConstantesLicencias.MaxLengthPortal)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje14"));
                        return false;
                    }
                }
                _plantaTecnico = plantaTecnicoJTField.getText();
                if (_plantaTecnico.length() > 0) {
                    if (excedeLongitud(_plantaTecnico, CConstantesLicencias.MaxLengthPlanta)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje16"));
                        return false;
                    }
                }
                _escaleraTecnico = escaleraTecnicoJTField.getText();
                if (excedeLongitud(_escaleraTecnico, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje17"));
                    return false;
                }
                _letraTecnico = letraTecnicoJTField.getText();
                if (excedeLongitud(_letraTecnico, CConstantesLicencias.MaxLengthLetra)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje18"));
                    return false;
                }
                _municipioTecnico = municipioTecnicoJTField.getText();
                if (excedeLongitud(_municipioTecnico, CConstantesLicencias.MaxLengthMunicipio)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje21"));
                    return false;
                }
                _provinciaTecnico = provinciaTecnicoJTField.getText();
                if (excedeLongitud(_provinciaTecnico, CConstantesLicencias.MaxLengthProvincia)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje22"));
                    return false;
                }
            }else{
                borrarCamposTecnico();
            }

            boolean notificar = notificarTecnicoJCheckBox.isSelected();
            if (notificar)
                _seNotificaTecnico = 1;
            else
                _seNotificaTecnico = 0;

        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }

		return true;
	}


    public void borrarCamposTecnico() {
        try {
            // Actualizamos campos
            DNITecnicoJTField.setText("");
            nombreTecnicoJTField.setText("");
            apellido1TecnicoJTField.setText("");
            apellido2TecnicoJTField.setText("");
            faxTecnicoJTField.setText("");
            telefonoTecnicoJTField.setText("");
            movilTecnicoJTField.setText("");
            emailTecnicoJTField.setText("");
            nombreViaTecnicoJTField.setText("");
            numeroViaTecnicoJTField.setText("");
            plantaTecnicoJTField.setText("");
            letraTecnicoJTField.setText("");
            portalTecnicoJTField.setText("");
            escaleraTecnicoJTField.setText("");
            cPostalTecnicoJTField.setText("");
            municipioTecnicoJTField.setText("");
            provinciaTecnicoJTField.setText("");
            notificarTecnicoJCheckBox.setSelected(false);

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
            if (_flagPromotor){
                if (excedeLongitud(_DNI_CIF_Promotor, CConstantesLicencias.MaxLengthDNI)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje1"));
                    return false;
                }
                if (excedeLongitud(_nombrePromotor, CConstantesLicencias.MaxLengthNombre)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje2"));
                    return false;
                }
                if (excedeLongitud(_apellido1Promotor, CConstantesLicencias.MaxLengthApellido) || excedeLongitud(_apellido2Promotor, CConstantesLicencias.MaxLengthApellido)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje3"));
                    return false;
                }
                if (excedeLongitud(_colegioPromotor, CConstantesLicencias.MaxLengthColegio)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje4"));
                    return false;
                }
                if (excedeLongitud(_visadoPromotor, CConstantesLicencias.MaxLengthVisado)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje5"));
                    return false;
                }
                if (excedeLongitud(_titulacionPromotor, CConstantesLicencias.MaxLengthTitulacion)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje6"));
                    return false;
                }
                _emailPromotor = emailPromotorJTField.getText();
                if (excedeLongitud(_emailPromotor, CConstantesLicencias.MaxLengthEmail)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje9"));
                    return false;
                }
                _nombreViaPromotor = nombreViaPromotorJTField.getText();
                if (excedeLongitud(_nombreViaPromotor, CConstantesLicencias.MaxLengthNombreVia)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje11"));
                    return false;
                }
                _portalPromotor = portalPromotorJTField.getText();
                if (_portalPromotor.length() > 0) {
                    if (excedeLongitud(_portalPromotor, CConstantesLicencias.MaxLengthPortal)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje14"));
                        return false;
                    }
                }
                _plantaPromotor = plantaPromotorJTField.getText();
                if (_plantaPromotor.length() > 0) {
                    if (excedeLongitud(_plantaPromotor, CConstantesLicencias.MaxLengthPlanta)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje16"));
                        return false;
                    }
                }
                _escaleraPromotor = escaleraPromotorJTField.getText();
                if (excedeLongitud(_escaleraPromotor, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje17"));
                    return false;
                }
                _letraPromotor = letraPromotorJTField.getText();
                if (excedeLongitud(_letraPromotor, CConstantesLicencias.MaxLengthLetra)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje18"));
                    return false;
                }
                _municipioPromotor = municipioPromotorJTField.getText();
                if (excedeLongitud(_municipioPromotor, CConstantesLicencias.MaxLengthMunicipio)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje21"));
                    return false;
                }

                _provinciaPromotor = provinciaPromotorJTField.getText();
                if (excedeLongitud(_provinciaPromotor, CConstantesLicencias.MaxLengthProvincia)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje22"));
                    return false;
                }
            }else{
                borrarCamposPromotor();
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

    public void borrarCamposPromotor() {
        try {
            // Actualizamos campos
            DNIPromotorJTField.setText("");
            nombrePromotorJTField.setText("");
            apellido1PromotorJTField.setText("");
            apellido2PromotorJTField.setText("");
            faxPromotorJTField.setText("");
            telefonoPromotorJTField.setText("");
            movilPromotorJTField.setText("");
            emailPromotorJTField.setText("");
            nombreViaPromotorJTField.setText("");
            numeroViaPromotorJTField.setText("");
            plantaPromotorJTField.setText("");
            letraPromotorJTField.setText("");
            portalPromotorJTField.setText("");
            escaleraPromotorJTField.setText("");
            cPostalPromotorJTField.setText("");
            municipioPromotorJTField.setText("");
            provinciaPromotorJTField.setText("");
            notificarPromotorJCheckBox.setSelected(false);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }




	public void rellenarEstadoJCBox() {

		_vEstado = COperacionesLicencias.getEstadosDisponibles(null,CConstantesLicencias.ObraMenor);
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
		_DNI_CIF_Titular = DNITitularJTField.getText();
		_nombreTitular = nombreTitularJTField.getText();
		/** Comprobamos si el titular representa a otra peprsona juridico-fisica */

		_nombreViaTitular = nombreViaTitularJTField.getText();
		_numeroViaTitular = numeroViaTitularJTField.getText();
		_cPostalTitular = cPostalTitularJTField.getText();
		_municipioTitular = municipioTitularJTField.getText();
		_provinciaTitular = provinciaTitularJTField.getText();

         
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
 		if (tipoViaINEEJCBox.getSelectedPatron() == "-1")
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
    


         
         
         
         
         
         
        if (_DNI_CIF_Titular.trim().length() == 0)
        {
        	jTabbedPaneSolicitud.setSelectedIndex(1); 
        	DNITitularJTField.requestFocus();
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
			numeroViaTitularJTField.requestFocus();
			return false;
		}
		if(_cPostalTitular.trim().length() == 0)
		{
			jTabbedPaneSolicitud.setSelectedIndex(1); 
			cPostalTitularJTField.requestFocus();
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
        _DNI_CIF_RepresentaA = DNIRepresentaAJTField.getText();
        /** si se inserta el DNI consideramos que el propietario tiene representante */
        
        if (_DNI_CIF_RepresentaA.trim().length() > 0) _flagRepresentante= true;
        else _flagRepresentante= false;
        if (_flagRepresentante){
            _nombreRepresentaA = nombreRepresentaAJTField.getText();
            _nombreViaRepresentaA = nombreViaRepresentaAJTField.getText();
            _numeroViaRepresentaA = numeroViaRepresentaAJTField.getText();
            _cPostalRepresentaA = cPostalRepresentaAJTField.getText();
            _municipioRepresentaA = municipioRepresentaAJTField.getText();
            _provinciaRepresentaA = provinciaRepresentaAJTField.getText();
            
            
            if (_DNI_CIF_RepresentaA.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(2);
            	DNIRepresentaAJTField.requestFocus();
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
            	cPostalRepresentaAJTField.requestFocus();
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

		/** Comprobamos los datos del tecnico */
        // leemos los datos referentes al tecnico
        _DNI_CIF_Tecnico = DNITecnicoJTField.getText();
        /** si se inserta el DNI consideramos que el propietario tiene tecnico */
        if (_DNI_CIF_Tecnico.trim().length() > 0) _flagTecnico= true;
        else _flagTecnico=false;
        if (_flagTecnico){
            _nombreTecnico = nombreTecnicoJTField.getText();
            _colegioTecnico = colegioTecnicoJTField.getText();
            _visadoTecnico = visadoTecnicoJTField.getText();
            _nombreViaTecnico = nombreViaTecnicoJTField.getText();
            _numeroViaTecnico = numeroViaTecnicoJTField.getText();
            _cPostalTecnico = cPostalTecnicoJTField.getText();
            _municipioTecnico = municipioTecnicoJTField.getText();
            _provinciaTecnico = provinciaTecnicoJTField.getText();

            if(_DNI_CIF_Tecnico.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(3);
            	DNITecnicoJTField.requestFocus();
            	return false;
            }
            if(_nombreTecnico.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(3);
            	nombreTecnicoJTField.requestFocus();
            	return false;
            }
            if (_nombreViaTecnico.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(3);
            	nombreViaTecnicoJTField.requestFocus();
            	return false;
            }
            if(_numeroViaTecnico.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(3);
            	numeroViaTecnicoJTField.requestFocus();
            	return false;
            }
            if(_cPostalTecnico.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(3);
            	cPostalTecnicoJTField.requestFocus();
            	return false;
            }
            if(_municipioTecnico.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(3);
            	municipioTecnicoJTField.requestFocus();
            	return false;
            }
            if(_provinciaTecnico.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(3);
            	provinciaTecnicoJTField.requestFocus();
            	return false;
            }
            /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
            if (emailTecnicoObligatorio){
                if (emailTecnicoJTField.getText().trim().length() == 0)
                	{
	                	jTabbedPaneSolicitud.setSelectedIndex(3);
	                	emailTecnicoJTField.requestFocus();
	                	return false;
                	}
            }

        }

		/** Comprobamos los datos del promotor */
        // leemos los datos referentes al tecnico
		_DNI_CIF_Promotor = DNIPromotorJTField.getText();
        /** si se inserta el DNI consideramos que el propietario tiene tecnico */
        if (_DNI_CIF_Promotor.trim().length() > 0) _flagPromotor= true;
        else _flagPromotor=false;
        if (_flagPromotor){
            _nombrePromotor = nombrePromotorJTField.getText();
            _colegioPromotor = colegioPromotorJTField.getText();
            _visadoPromotor = visadoPromotorJTField.getText();
            _nombreViaPromotor = nombreViaPromotorJTField.getText();
            _numeroViaPromotor = numeroViaPromotorJTField.getText();
            _cPostalPromotor = cPostalPromotorJTField.getText();
            _municipioPromotor = municipioPromotorJTField.getText();
            _provinciaPromotor = provinciaPromotorJTField.getText();

            if(_DNI_CIF_Promotor.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(4);
            	DNIPromotorJTField.requestFocus();
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
            	numeroViaPromotorJTField.requestFocus();
            	return false;
            }
            if(_cPostalPromotor.trim().length() == 0)
            {
            	jTabbedPaneSolicitud.setSelectedIndex(4);
            	cPostalPromotorJTField.requestFocus();
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
        this.literales=literales;

        try{
            if(CConstantesLicencias.selectedSubApp.equals(CConstantesLicencias.LicenciasObraMenor)){

                /** Pestannas */
                jTabbedPaneSolicitud.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.obraMayorJPanel.SubTitleTab")));
                jTabbedPaneSolicitud.setTitleAt(1, CUtilidadesComponentes.annadirAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.titularJPanel.TitleTab")));
                jTabbedPaneSolicitud.setTitleAt(2, literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.representaAJPanel.TitleTab"));
                jTabbedPaneSolicitud.setTitleAt(3, literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.TitleTab"));
                jTabbedPaneSolicitud.setTitleAt(4, literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.promotorJPanel.TitleTab"));
                obraMayorJTabbedPane.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.obraMayorJPanel.TitleTab")));
                obraMayorJTabbedPane.setTitleAt(1, literales.getString("DocumentacionLicenciasJPanel.title"));

                /** Solicitud */
                setTitle(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.JInternalFrame.title"));
                datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.datosSolicitudJPanel.TitleBorder")));
                estadoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.estadoJLabel.text")));
                tipoObraJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tipoObraJLabel.text")));
                unidadTJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.unidadTJLabel.text"));
                unidadRJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.unidadRJLabel.text"));
                motivoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.motivoJLabel.text"));
                asuntoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.asuntoJLabel.text"));
                fechaSolicitudJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.fechaSolicitudJLabel.text")));
                fechaLimiteObraJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.fechaLimiteObraJLabel.text"));
                observacionesJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.observacionesJLabel.text"));
                fechaSolicitudJTField.setText(showToday());
                unidadTJTField.setText(JDialogConfiguracion.getUnidadTramitadora());
                unidadRJTField.setText(JDialogConfiguracion.getUnidadRegistro());
                tasaJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tasaJLabel.text"));
                emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emplazamientoJPanel.TitleBorder")));
                nombreViaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tipoViaJLabel.text"), literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombreViaJLabel.text")));
                numeroViaJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.numeroViaJLabel.text"));
                cPostalJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.cPostalJLabel.text"));
                provinciaJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.provinciaJLabel.text"));
                referenciaCatastralJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciaCatastralJLabel.text"));

                /** Propietario */
                datosPersonalesTitularJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.datosPersonalesTitularJPanel.TitleBorder")));
                DNITitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.DNITitularJLabel.text")));
                nombreTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombreTitularJLabel.text")));
                apellido1TitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.apellido1TitularJLabel.text"));
                apellido2TitularJLabel2.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.apellido2TitularJLabel.text"));
                datosNotificacionTitularJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionTitularJPanel.TitleBorder")));
                viaNotificacionTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.viaNotificacionTitularJLabel.text"));
                faxTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.faxTitularJLabel.text"));
                telefonoTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.telefonoTitularJLabel.text"));
                movilTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.movilTitularJLabel.text"));
                emailTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailTitularJLabel.text"));
                tipoViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tipoViaTitularJLabel.text")));
                nombreViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombreViaTitularJLabel.text")));
                numeroViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.numeroViaTiularJLabel.text")));
                portalTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.portalTitularJLabel.text"));
                plantaTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.plantaTitularJLabel.text"));
                escaleraTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.escaleraTitularJLabel.text"));
                letraTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.letraTitularJLabel.text"));
                cPostalTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.cPostalTitularJLabel.text")));
                municipioTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.municipioTitularJLabel.text")));
                provinciaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.provinciaTitularJLabel.text")));
                notificarTitularJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.notificarTitularJLabel.text"));

                /** Representante */
                datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.datosPersonalesRepresentaAJPanel.TitleBorder")));
                DNIRepresenaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.DNIRepresentaAJLabel.text")));
                nombreRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombreRepresentaAJLabel.text")));
                apellido1RepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.apellido1RepresentaAJLabel.text"));
                apellido2RepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.apellido2RepresentaAJLabel.text"));
                datosNotificacionRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionRepresentaAJPanel.TitleBorder")));
                viaNotificacionRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.viaNotificacionRepresentaAJLabel.text"));
                faxRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.faxRepresentaAJLabel.text"));
                telefonoRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.telefonoRepresentaAJLabel.text"));
                movilRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.movilRepresentaAJLabel.text"));
                emailRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailRepresentaAJLabel.text"));
                tipoViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tipoViaRepresentaAJLabel.text")));
                nombreViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombreViaRepresentaAJLabel.text")));
                numeroViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.numeroViaTiularJLabel.text")));
                portalRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.portalRepresentaAJLabel.text"));
                plantaRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.plantaRepresentaAJLabel.text"));
                escaleraRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.escaleraRepresentaAJLabel.text"));
                letraRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.letraRepresentaAJLabel.text"));
                cPostalRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.cPostalRepresentaAJLabel.text")));
                municipioRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.municipioRepresentaAJLabel.text")));
                provinciaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.provinciaRepresentaAJLabel.text")));
                notificarRepresentaAJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.notificarRepresentaAJLabel.text"));

                /** Tecnico */
                datosPersonalesTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.datosPersonalesTecnicoJPanel.TitleBorder")));
                DNITecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.DNITecnicoJLabel.text")));
                nombreTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombreTecnicoJLabel.text")));
                apellido1TecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.apellido1TecnicoJLabel.text"));
                apellido2TecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.apellido2TecnicoJLabel.text"));
                colegioTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.colegioTecnicoJLabel.text"));
                visadoTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.visadoTecnicoJLabel.text"));
                titulacionTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.titulacionTecnicoJLabel.text"));
                datosNotificacionTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionTecnicoJPanel.TitleBorder")));
                viaNotificacionTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.viaNotificacionTecnicoJLabel.text"));
                faxTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.faxTecnicoJLabel.text"));
                telefonoTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.telefonoTecnicoJLabel.text"));
                movilTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.movilTecnicoJLabel.text"));
                emailTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailTecnicoJLabel.text"));
                tipoViaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tipoViaTecnicoJLabel.text")));
                nombreViaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombreViaTecnicoJLabel.text")));
                numeroViaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.numeroViaTiularJLabel.text")));
                portalTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.portalTecnicoJLabel.text"));
                plantaTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.plantaTecnicoJLabel.text"));
                escaleraTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.escaleraTecnicoJLabel.text"));
                letraTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.letraTecnicoJLabel.text"));
                cPostalTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.cPostalTecnicoJLabel.text")));
                municipioTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.municipioTecnicoJLabel.text")));
                provinciaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.provinciaTecnicoJLabel.text")));
                notificarTecnicoJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.notificarTecnicoJLabel.text"));

                /** Promotor */
                datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.datosPersonalesPromotorJPanel.TitleBorder")));
                DNIPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.DNIPromotorJLabel.text")));
                nombrePromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombrePromotorJLabel.text")));
                apellido1PromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.apellido1PromotorJLabel.text"));
                apellido2PromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.apellido2PromotorJLabel.text"));
                colegioPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.colegioPromotorJLabel.text"));
                visadoPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.visadoPromotorJLabel.text"));
                titulacionPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.titulacionPromotorJLabel.text"));
                datosNotificacionPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionPromotorJPanel.TitleBorder")));
                viaNotificacionPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.viaNotificacionPromotorJLabel.text"));
                faxPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.faxPromotorJLabel.text"));
                telefonoPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.telefonoPromotorJLabel.text"));
                movilPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.movilPromotorJLabel.text"));
                emailPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.emailPromotorJLabel.text"));
                tipoViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tipoViaPromotorJLabel.text")));
                nombreViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombreViaPromotorJLabel.text")));
                numeroViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.numeroViaTiularJLabel.text")));
                portalPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.portalPromotorJLabel.text"));
                plantaPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.plantaPromotorJLabel.text"));
                escaleraPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.escaleraPromotorJLabel.text"));
                letraPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.letraPromotorJLabel.text"));
                cPostalPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.cPostalPromotorJLabel.text")));
                municipioPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.municipioPromotorJLabel.text")));
                provinciaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.provinciaPromotorJLabel.text")));
                notificarPromotorJLabel.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.notificarPromotorJLabel.text"));

                /** Documentacion */
                documentacionJPanel.renombrarComponentes(literales);

                aceptarJButton.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.aceptarJButton.text"));
                cancelarJButton.setText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.cancelarJButton.text"));
                editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.editorMapaJPanel.TitleBorder")));

                aceptarJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.aceptarJButton.toolTipText"));
                cancelarJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.cancelarJButton.text"));
                fechaSolicitudJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.fechaSolicitudJButton.setToolTip.text"));
                fechaLimiteObraJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.fechaLimiteObraJButton.setToolTip.text"));
                nombreJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.nombreJButton.setToolTip.text"));
                referenciaCatastralJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciaCatastralJButton.setToolTip.text"));
                MapToTableJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.MapToTableJButton.setToolTip.text"));
                deleteRegistroCatastralJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.deleteRegistroCatastralJButton.setToolTip.text"));
                tableToMapJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.tableToMapJButton.setToolTip.text"));
                buscarDNITitularJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.buscarDNITitularJButton.setToolTip.text"));
                buscarDNIRepresentaAJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.buscarDNIRepresentaAJButton.setToolTip.text"));
                buscarDNITecnicoJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.buscarDNITecnicoJButton.setToolTip.text"));
                buscarDNIPromotorJButton.setToolTipText(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.buscarDNIPromotorJButton.setToolTip.text"));

                /** Header tabla referenciasCatastrales */
                TableColumn tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(0);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column1"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column2"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(2);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column3"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(3);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column4"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(4);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column5"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(5);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column6"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(6);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column7"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(7);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column8"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(8);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column9"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(9);
                tableColumn.setHeaderValue(literales.getString("CCreacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column10"));
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
            motivoJTField.setText("");
            asuntoJTField.setText("");
            fechaLimiteObraJTField.setText("");
            fechaSolicitudJTField.setText(showToday());            
            observacionesJTArea.setText("");
            tasaTextField.setText("€0.00");
            impuestoTextField.setText("€0.00");
            CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
            CConstantesLicencias.referenciasCatastrales.clear();
            geopistaEditor.getSelectionManager().clear();

            /** Emplazamiento */
            tipoViaINEEJCBox.setSelectedIndex(0);
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
            DNITitularJTField.setText("");
            nombreTitularJTField.setText("");
            apellido1TitularJTField.setText("");
            apellido2TitularJTField.setText("");
            buscarDNITitularJButton.setToolTipText("");
            faxTitularJTField.setText("");
            telefonoTitularJTField.setText("");
            movilTitularJTField.setText("");
            emailTitularJTField.setText("");
            nombreViaTitularJTField.setText("");
            numeroViaTitularJTField.setText("");
            plantaTitularJTField.setText("");
            portalTitularJTField.setText("");
            escaleraTitularJTField.setText("");
            letraTitularJTField.setText("");
            cPostalTitularJTField.setText("");
            municipioTitularJTField.setText("");
            provinciaTitularJTField.setText("");
            notificarTitularJCheckBox.setSelected(true);

            /** Representante */
            DNIRepresentaAJTField.setText("");
            nombreRepresentaAJTField.setText("");
            apellido1RepresentaAJTField.setText("");
            apellido2RepresentaAJTField.setText("");
            buscarDNIRepresentaAJButton.setToolTipText("");
            faxRepresentaAJTField.setText("");
            telefonoRepresentaAJTField.setText("");
            movilRepresentaAJTField.setText("");
            emailRepresentaAJTField.setText("");
            nombreViaRepresentaAJTField.setText("");
            numeroViaRepresentaAJTField.setText("");
            plantaRepresentaAJTField.setText("");
            portalRepresentaAJTField.setText("");
            escaleraRepresentaAJTField.setText("");
            letraRepresentaAJTField.setText("");
            cPostalRepresentaAJTField.setText("");
            municipioRepresentaAJTField.setText("");
            provinciaRepresentaAJTField.setText("");
            notificarRepresentaAJCheckBox.setSelected(false);
            _seNotificaRepresentaA= 0;
            _flagRepresentante= false;


            /** Tecnico */
            DNITecnicoJTField.setText("");
            nombreTecnicoJTField.setText("");
            apellido1TecnicoJTField.setText("");
            apellido2TecnicoJTField.setText("");
            colegioTecnicoJTField.setText("");
            visadoTecnicoJTField.setText("");
            titulacionTecnicoJTField.setText("");
            buscarDNITecnicoJButton.setToolTipText("");
            faxTecnicoJTField.setText("");
            telefonoTecnicoJTField.setText("");
            movilTecnicoJTField.setText("");
            emailTecnicoJTField.setText("");
            nombreViaTecnicoJTField.setText("");
            numeroViaTecnicoJTField.setText("");
            plantaTecnicoJTField.setText("");
            portalTecnicoJTField.setText("");
            escaleraTecnicoJTField.setText("");
            letraTecnicoJTField.setText("");
            cPostalTecnicoJTField.setText("");
            municipioTecnicoJTField.setText("");
            provinciaTecnicoJTField.setText("");
            notificarTecnicoJCheckBox.setSelected(false);
            _seNotificaTecnico= 0;


            /** Promotor */
            DNIPromotorJTField.setText("");
            nombrePromotorJTField.setText("");
            apellido1PromotorJTField.setText("");
            apellido2PromotorJTField.setText("");
            colegioPromotorJTField.setText("");
            visadoPromotorJTField.setText("");
            titulacionPromotorJTField.setText("");
            buscarDNIPromotorJButton.setToolTipText("");
            faxPromotorJTField.setText("");
            telefonoPromotorJTField.setText("");
            movilPromotorJTField.setText("");
            emailPromotorJTField.setText("");
            nombreViaPromotorJTField.setText("");
            numeroViaPromotorJTField.setText("");
            plantaPromotorJTField.setText("");
            portalPromotorJTField.setText("");
            escaleraPromotorJTField.setText("");
            letraPromotorJTField.setText("");
            cPostalPromotorJTField.setText("");
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




    private ComboBoxEstructuras tipoObraEJCBox;
    private ComboBoxEstructuras viaNotificacionTitularEJCBox;
    private ComboBoxEstructuras viaNotificacionRepresentaAEJCBox;
    private ComboBoxEstructuras viaNotificacionTecnicoEJCBox;
    private ComboBoxEstructuras viaNotificacionPromotorEJCBox;

    private ComboBoxEstructuras tipoViaNotificacionTitularEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionRepresentaAEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionTecnicoEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionPromotorEJCBox;

    /** tasa */
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
    private ComboBoxEstructuras tipoViaINEEJCBox;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DNIPromotorJLabel;
    private javax.swing.JTextField DNIPromotorJTField;
    private javax.swing.JLabel DNIRepresenaAJLabel;
    private javax.swing.JTextField DNIRepresentaAJTField;
    private javax.swing.JLabel DNITecnicoJLabel;
    private javax.swing.JTextField DNITecnicoJTField;
    private javax.swing.JLabel DNITitularJLabel;
    private javax.swing.JTextField DNITitularJTField;
    private javax.swing.JButton MapToTableJButton;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JLabel apellido1PromotorJLabel;
    private javax.swing.JTextField apellido1PromotorJTField;
    private javax.swing.JLabel apellido1RepresentaAJLabel;
    private javax.swing.JTextField apellido1RepresentaAJTField;
    private javax.swing.JLabel apellido1TecnicoJLabel;
    private javax.swing.JTextField apellido1TecnicoJTField;
    private javax.swing.JLabel apellido1TitularJLabel;
    private javax.swing.JTextField apellido1TitularJTField;
    private javax.swing.JLabel apellido2PromotorJLabel;
    private javax.swing.JTextField apellido2PromotorJTField;
    private javax.swing.JLabel apellido2RepresentaAJLabel;
    private javax.swing.JTextField apellido2RepresentaAJTField;
    private javax.swing.JLabel apellido2TecnicoJLabel;
    private javax.swing.JTextField apellido2TecnicoJTField;
    private javax.swing.JLabel apellido2TitularJLabel2;
    private javax.swing.JTextField apellido2TitularJTField;
    private javax.swing.JLabel asuntoJLabel;
    private javax.swing.JTextField asuntoJTField;
    private javax.swing.JPanel botoneraJPanel;
    private javax.swing.JButton buscarDNIPromotorJButton;
    private javax.swing.JButton buscarDNIRepresentaAJButton;
    private javax.swing.JButton buscarDNITecnicoJButton;
    private javax.swing.JButton buscarDNITitularJButton;
    private javax.swing.JLabel cPostalJLabel;
    private javax.swing.JLabel cPostalPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalPromotorJTField;
    private javax.swing.JLabel cPostalRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalRepresentaAJTField;
    private javax.swing.JLabel cPostalTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalTecnicoJTField;
    private javax.swing.JLabel cPostalTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalTitularJTField;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JLabel colegioPromotorJLabel;
    private javax.swing.JTextField colegioPromotorJTField;
    private javax.swing.JLabel colegioTecnicoJLabel;
    private javax.swing.JTextField colegioTecnicoJTField;
    private javax.swing.JPanel datosNotificacionPromotorJPanel;
    private javax.swing.JPanel datosNotificacionRepresentaAJPanel;
    private javax.swing.JPanel datosNotificacionTecnicoJPanel;
    private javax.swing.JPanel datosNotificacionTitularJPanel;
    private javax.swing.JPanel datosPersonalesPromotorJPanel;
    private javax.swing.JPanel datosPersonalesRepresentaAJPanel;
    private javax.swing.JPanel datosPersonalesTecnicoJPanel;
    private javax.swing.JPanel datosPersonalesTitularJPanel;
    private javax.swing.JPanel datosSolicitudJPanel;
    private javax.swing.JButton deleteRegistroCatastralJButton;
    private javax.swing.JLabel emailPromotorJLabel;
    private javax.swing.JTextField emailPromotorJTField;
    private javax.swing.JLabel emailRepresentaAJLabel;
    private javax.swing.JTextField emailRepresentaAJTField;
    private javax.swing.JLabel emailTecnicoJLabel;
    private javax.swing.JTextField emailTecnicoJTField;
    private javax.swing.JLabel emailTitularJLabel;
    private javax.swing.JTextField emailTitularJTField;
    private javax.swing.JPanel emplazamientoJPanel;
    private javax.swing.JLabel escaleraPromotorJLabel;
    private javax.swing.JTextField escaleraPromotorJTField;
    private javax.swing.JLabel escaleraRepresentaAJLabel;
    private javax.swing.JTextField escaleraRepresentaAJTField;
    private javax.swing.JLabel escaleraTecnicoJLabel;
    private javax.swing.JTextField escaleraTecnicoJTField;
    private javax.swing.JLabel escaleraTitularJLabel;
    private javax.swing.JTextField escaleraTitularJTField;
    private javax.swing.JComboBox estadoJCBox;
    private javax.swing.JLabel estadoJLabel;
    private javax.swing.JLabel faxPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxPromotorJTField;
    private javax.swing.JLabel faxRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxRepresentaAJTField;
    private javax.swing.JLabel faxTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxTecnicoJTField;
    private javax.swing.JLabel faxTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxTitularJTField;
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
    private javax.swing.JLabel letraTecnicoJLabel;
    private javax.swing.JTextField letraTecnicoJTField;
    private javax.swing.JLabel letraTitularJLabel;
    private javax.swing.JTextField letraTitularJTField;
    private javax.swing.JLabel motivoJLabel;
    private javax.swing.JTextField motivoJTField;
    private javax.swing.JLabel movilPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilPromotorJTField;
    private javax.swing.JLabel movilRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilRepresentaAJTField;
    private javax.swing.JLabel movilTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilTecnicoJTField;
    private javax.swing.JLabel movilTitularJLabel;
   private com.geopista.app.utilidades.JNumberTextField movilTitularJTField;
    private javax.swing.JTextField municipioJTField;
    private javax.swing.JLabel municipioPromotorJLabel;
    private javax.swing.JTextField municipioPromotorJTField;
    private javax.swing.JLabel municipioRepresentaAJLabel;
    private javax.swing.JTextField municipioRepresentaAJTField;
    private javax.swing.JLabel municipioTecnicoJLabel;
    private javax.swing.JTextField municipioTecnicoJTField;
    private javax.swing.JLabel municipioTitularJLabel;
    private javax.swing.JTextField municipioTitularJTField;
    private javax.swing.JButton nombreJButton;
    private javax.swing.JLabel nombrePromotorJLabel;
    private javax.swing.JTextField nombrePromotorJTField;
    private javax.swing.JLabel nombreRepresentaAJLabel;
    private javax.swing.JTextField nombreRepresentaAJTField;
    private javax.swing.JLabel nombreTecnicoJLabel;
    private javax.swing.JTextField nombreTecnicoJTField;
    private javax.swing.JLabel nombreTitularJLabel;
    private javax.swing.JTextField nombreTitularJTField;
    private javax.swing.JLabel nombreViaJLabel;
    private javax.swing.JLabel nombreViaPromotorJLabel;
    private javax.swing.JTextField nombreViaPromotorJTField;
    private javax.swing.JLabel nombreViaRepresentaAJLabel;
    private javax.swing.JTextField nombreViaRepresentaAJTField;
    private javax.swing.JLabel nombreViaTecnicoJLabel;
    private javax.swing.JTextField nombreViaTecnicoJTField;
    private javax.swing.JLabel nombreViaTitularJLabel;
    private javax.swing.JTextField nombreViaTitularJTField;
    private javax.swing.JCheckBox notificarPromotorJCheckBox;
    private javax.swing.JLabel notificarPromotorJLabel;
    private javax.swing.JCheckBox notificarRepresentaAJCheckBox;
    private javax.swing.JLabel notificarRepresentaAJLabel;
    private javax.swing.JCheckBox notificarTecnicoJCheckBox;
    private javax.swing.JLabel notificarTecnicoJLabel;
    private javax.swing.JCheckBox notificarTitularJCheckBox;
    private javax.swing.JLabel notificarTitularJLabel;
    private javax.swing.JLabel numeroViaJLabel;
    private javax.swing.JLabel numeroViaPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaPromotorJTField;
    private javax.swing.JLabel numeroViaRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaRepresentaAJTField;
    private javax.swing.JLabel numeroViaTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaTecnicoJTField;
    private javax.swing.JLabel numeroViaTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaTitularJTField;
    private javax.swing.JPanel obraMayorJPanel;
    private javax.swing.JTabbedPane obraMayorJTabbedPane;
    private javax.swing.JLabel observacionesJLabel;
    private javax.swing.JScrollPane observacionesJScrollPane;
    private javax.swing.JTextArea observacionesJTArea;
    private javax.swing.JLabel plantaPromotorJLabel;
    private javax.swing.JTextField plantaPromotorJTField;
    private javax.swing.JLabel plantaRepresentaAJLabel;
    private javax.swing.JTextField plantaRepresentaAJTField;
    private javax.swing.JLabel plantaTecnicoJLabel;
    private javax.swing.JTextField plantaTecnicoJTField;
    private javax.swing.JLabel plantaTitularJLabel;
    private javax.swing.JTextField plantaTitularJTField;
    private javax.swing.JLabel portalPromotorJLabel;
    private javax.swing.JTextField portalPromotorJTField;
    private javax.swing.JLabel portalRepresentaAJLabel;
    private javax.swing.JTextField portalRepresentaAJTField;
    private javax.swing.JLabel portalTecnicoJLabel;
    private javax.swing.JTextField portalTecnicoJTField;
    private javax.swing.JLabel portalTitularJLabel;
    private javax.swing.JTextField portalTitularJTField;
    private javax.swing.JPanel promotorJPanel;
    private javax.swing.JLabel provinciaJLabel;
    private javax.swing.JTextField provinciaJTField;
    private javax.swing.JLabel provinciaPromotorJLabel;
    private javax.swing.JTextField provinciaPromotorJTField;
    private javax.swing.JLabel provinciaRepresentaAJLabel;
    private javax.swing.JTextField provinciaRepresentaAJTField;
    private javax.swing.JLabel provinciaTecnicoJLabel;
    private javax.swing.JTextField provinciaTecnicoJTField;
    private javax.swing.JLabel provinciaTitularJLabel;
    private javax.swing.JTextField provinciaTitularJTField;
    private javax.swing.JButton referenciaCatastralJButton;
    private javax.swing.JLabel referenciaCatastralJLabel;
    private javax.swing.JTextField referenciaJTextField;
    private javax.swing.JScrollPane referenciasCatastralesJScrollPane;
    private javax.swing.JPanel representaAJPanel;
    private javax.swing.JButton tableToMapJButton;
    private javax.swing.JLabel tasaJLabel;
    private javax.swing.JPanel tecnicoJPanel;
    private javax.swing.JLabel telefonoPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoPromotorJTField;
    private javax.swing.JLabel telefonoRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoRepresentaAJTField;
    private javax.swing.JLabel telefonoTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoTecnicoJTField;
    private javax.swing.JLabel telefonoTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoTitularJTField;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoObraJLabel;
    private javax.swing.JLabel tipoViaPromotorJLabel;
    private javax.swing.JLabel tipoViaRepresentaAJLabel;
    private javax.swing.JLabel tipoViaTecnicoJLabel;
    private javax.swing.JLabel tipoViaTitularJLabel;
    private javax.swing.JLabel titulacionPromotorJLabel;
    private javax.swing.JTextField titulacionPromotorJTField;
    private javax.swing.JLabel titulacionTecnicoJLabel;
    private javax.swing.JTextField titulacionTecnicoJTField;
    private javax.swing.JPanel titularJPanel;
    private javax.swing.JLabel unidadRJLabel;
    private javax.swing.JTextField unidadRJTField;
    private javax.swing.JLabel unidadTJLabel;
    private javax.swing.JTextField unidadTJTField;
    private javax.swing.JLabel viaNotificacionPromotorJLabel;
    private javax.swing.JLabel viaNotificacionRepresentaAJLabel;
    private javax.swing.JLabel viaNotificacionTecnicoJLabel;
    private javax.swing.JLabel viaNotificacionTitularJLabel;
    private javax.swing.JLabel visadoPromotorJLabel;
    private javax.swing.JTextField visadoPromotorJTField;
    private javax.swing.JLabel visadoTecnicoJLabel;
    private javax.swing.JTextField visadoTecnicoJTField;
    private JTabbedPane jTabbedPaneSolicitud;
    // End of variables declaration//GEN-END:variables

}
