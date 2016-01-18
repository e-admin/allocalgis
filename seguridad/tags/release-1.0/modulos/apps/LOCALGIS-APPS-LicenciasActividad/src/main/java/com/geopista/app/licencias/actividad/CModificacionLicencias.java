package com.geopista.app.licencias.actividad;
/**
 *
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
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.licencias.CMainLicencias;
import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.CellRendererEstructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxRendererEstructuras;
import com.geopista.app.licencias.*;
import com.geopista.app.licencias.actividad.datosActividad.DatosActividadJPanel;
import com.geopista.app.licencias.actividad.MainActividadLicencias;
import com.geopista.app.licencias.documentacion.DocumentacionLicenciasJPanel;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CEventosModificacionTableModel;
import com.geopista.app.licencias.tableModels.CHistoricoModificacionTableModel;
import com.geopista.app.licencias.tableModels.CNotificacionesModificacionTableModel;
import com.geopista.app.licencias.tableModels.CReferenciasCatastralesTableModel;
import com.geopista.app.licencias.utilidades.*;
import com.geopista.app.printer.GeopistaPrintable;
import com.geopista.app.printer.FichasDisponibles;
import com.geopista.editor.GeopistaEditor;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.*;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.estados.CEstadoNotificacion;
import com.geopista.protocol.licencias.tipos.*;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.util.StringUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author avivar
 */
public class CModificacionLicencias extends javax.swing.JInternalFrame implements IMultilingue{


	/**
	 * Datos del Propietario
	 */
	private String _DNI_CIF_Propietario = "";
	private String _nombrePropietario = "";
	private String _apellido1Propietario = "";
	private String _apellido2Propietario = "";
	private String _faxPropietario = "";
	private String _telefonoPropietario = "";
	private String _movilPropietario = "";
	private String _emailPropietario = "";
    private String _tipoViaPropietario = "";
	private String _nombreViaPropietario = "";
	private String _numeroViaPropietario = "";
	private String _portalPropietario = "";
	private String _plantaPropietario = "";
	private String _escaleraPropietario = "";
	private String _letraPropietario = "";
	private String _cPostalPropietario = "";
	private String _municipioPropietario = "";
	private String _provinciaPropietario = "";
	private int _seNotificaPropietario = 1;
    private boolean emailPropietarioObligatorio= false;

	/**
	 * Datos del Representante
	 */
	private String _DNI_CIF_Representante = "";
	private String _nombreRepresentante = "";
	private String _apellido1Representante = "";
	private String _apellido2Representante = "";
	private String _faxRepresentante = "";
	private String _telefonoRepresentante = "";
	private String _movilRepresentante = "";
	private String _emailRepresentante = "";
    private String _tipoViaRepresentante = "";
	private String _nombreViaRepresentante = "";
	private String _numeroViaRepresentante = "";
	private String _portalRepresentante = "";
	private String _plantaRepresentante = "";
	private String _escaleraRepresentante = "";
	private String _letraRepresentante = "";
	private String _cPostalRepresentante = "";
	private String _municipioRepresentante = "";
	private String _provinciaRepresentante = "";
	private int _seNotificaRepresentante = 0;
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
	private String _telefonoTecnico = "";
	private String _movilTecnico = "";
	private String _emailTecnico = "";
    private String _tipoViaTecnico = "";
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
	private String _telefonoPromotor = "";
	private String _movilPromotor = "";
	private String _emailPromotor = "";
    private String _tipoViaPromotor = "";
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
	private String _motivo = "";
	private String _asunto = "";
	private String _observaciones = "";

	/**
	 * Datos de Expediente
	 */
	private String _servicioEncargado = "";
	private String _asuntoExpediente = "";
	private String _fechaApertura = "";
	private String _formaInicio = "";
	private String _responsableExpediente = "";
	private String _observacionesExpediente = "";

	private String _numExpediente= "";
	private CSolicitudLicencia _solicitud = null;
	private CExpedienteLicencia _expediente = null;

	/**
	 * Modelo para el componente notificacionesJTable
	 */
	private CNotificacionesModificacionTableModel _notificacionesExpedienteTableModel;

	/**
	 * Modelo para el componente eventosJTable
	 */
	private CEventosModificacionTableModel _eventosExpedienteTableModel;

	/**
	 * Modelo para el componente historicoJTable
	 */
	private CHistoricoModificacionTableModel _historicoExpedienteTableModel;

	private Hashtable _hEstados = new Hashtable();

	/**
	 * Notificaciones del expediente
     */

	private Vector _vNotificaciones = new Vector();

	/**
	 * Eventos del expediente
	 */
	private Vector _vEventos = new Vector();

	/**
	 * Historico del expediente
	 */
	private Vector _vHistorico = new Vector();
    private Vector vAuxiliar= new Vector();
    private Vector vAuxiliarDeleted= new Vector();

    /** Referencias Catastrales */
    //Vector _vReferenciasCatastrales= new Vector();


	/**
	 * Comprueba si la consulta se ha realizado correctamente
	 */
	private boolean _consultaOK = false;
	private String _vu = "0";
    private boolean fromMenu= true;
	Logger logger = Logger.getLogger(CModificacionLicencias.class);
    private ResourceBundle literales;

    /** Ordenacion de tablas */
    TableSorted notificacionesSorted= new TableSorted();
    TableSorted eventosSorted= new TableSorted();
    TableSorted historicoSorted= new TableSorted();
    int notificacionesHiddenCol= 6;
    int eventosHiddenCol= 5;
    int historicoHiddenCol= 4;


	/**
	 * Creates new form CModificacionLicencias
	 */
	public CModificacionLicencias(final JFrame desktop, final String numExpediente, final ResourceBundle literales, final boolean calledFromMenu) {
        this.literales= literales;
		this.desktop = desktop;
        fromMenu= calledFromMenu;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

        //***para sacar la ventana de espera**
         final TaskMonitorDialog progressDialog = new TaskMonitorDialog(this.desktop, null);
         progressDialog.setTitle(literales.getString("Licencias.Tag1"));
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
                            progressDialog.report(literales.getString("Licencias.Tag2"));
                            initComponents();
                            initComboBoxesEstructuras();
                            configureComponents();
                            annadirPestanas(literales);
                            renombrarComponentes(literales);

                            fechaSolicitudJTField.setEnabled(false);
                            fechaLimiteObraJTField.setEnabled(false);
                            progressDialog.report(literales.getString("Licencias.Tag1"));
                             if (CUtilidadesComponentes.showGeopistaMap(desktop,editorMapaJPanel, MainActividadLicencias.geopistaEditor, 273, false))
                            {
                                GeopistaLayer layer=(GeopistaLayer)MainActividadLicencias.geopistaEditor.getLayerManager().getLayer("parcelas");
                                if (layer!=null)
                                {
                                    layer.setEditable(true);
                                    layer.setActiva(true);
                                }
                            } else
                            {
                                new JOptionPane("No existe el mapa licencias actividad en el sistema. \nContacte con el administrador."
                                        , JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();
                            }
                            if (numExpediente != null) {
                                numExpedienteJTField.setText(numExpediente);

                               if (fromMenu){
                                   consultarJButtonActionPerformed();
                               }
                            }
                         }catch(Exception e)
                         {
                             logger.error("Error ", e);
                             ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                             return;
                         }finally
                         {
                             progressDialog.setVisible(false);
                         }
                     }
               }).start();
           }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);

	}

	private boolean configureComponents() {

		try {
			if (MainActividadLicencias.geopistaEditor == null) MainActividadLicencias.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");

			CConstantesLicencias.referenciasCatastrales = new Hashtable();

            /** Definimos el TableModel para el componente notificacionesJTable */
			String[] columnNamesNotificaciones = {literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum6"),
                                                  literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum1"),
												  literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum2"),
												  literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum3"),
												  literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum4"),
												  literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum5"),
                                                  "HIDDEN"};
			CNotificacionesModificacionTableModel.setColumnNames(columnNamesNotificaciones);
			_notificacionesExpedienteTableModel = new CNotificacionesModificacionTableModel();

            /** Definimos el TableModel para el componente eventosJTable */
			String[] columnNamesEventos = {literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum1"),
										   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum2"),
										   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum3"),
										   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum4"),
										   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum5"),
                                           "HIDDEN"};
			CEventosModificacionTableModel.setColumnNames(columnNamesEventos);
			_eventosExpedienteTableModel = new CEventosModificacionTableModel();

            /** Definimos el TableModel para el componente historicoJTable */
			String[] columnNamesHistorico = {literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum2"),
											 literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum3"),
											 literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum4"),
											 literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum5"),
                                             "HIDDEN"};
			CHistoricoModificacionTableModel.setColumnNames(columnNamesHistorico);
			_historicoExpedienteTableModel = new CHistoricoModificacionTableModel();

            /** Definimos el TableModel para el componente referenciasCatastralesJTable */
			referenciasCatastralesJTableModel = new CReferenciasCatastralesTableModel(new String[]{literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column1"),
																				   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column2"),
																				   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column3"),
																				   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column4"),
																				   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column5"),
																				   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column6"),
																				   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column7"),
																				   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column8"),
																				   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column9"),
                                                                                   literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column10"), ""});

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

            // Annadimos a la tabla el editor ComboBox en la segunda columna (tipoVia)
            TableColumn tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
            ComboBoxEstructuras comboBoxEstructuras= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, literales.getLocale().toString(), true);
            comboBoxEstructuras.setSelectedIndex(0);

            ComboBoxTableEditor comboBoxTableEditor= new ComboBoxTableEditor(comboBoxEstructuras);
            comboBoxTableEditor.setEnabled(true);
            tableColumn.setCellEditor(comboBoxTableEditor);

            CellRendererEstructuras renderer =
                new CellRendererEstructuras(literales.getLocale().toString(),Estructuras.getListaTiposViaINE());
            tableColumn.setCellRenderer(renderer);

            // Annadimos a la tabla el editor TextField en el resto de columnas
            for (int col=2; col < referenciasCatastralesJTable.getColumnModel().getColumnCount(); col++){
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(col);
                TextFieldTableEditor textFieldTableEditor= null;
                if (col == 2){
                    // Nombre
                    textFieldTableEditor= new TextFieldTableEditor(68);
                }else if (col == 3){
                    // Numero
                    textFieldTableEditor= new TextFieldTableEditor(8);
                }else if (col == 9){
                    // CPostal
                    textFieldTableEditor= new TextFieldTableEditor(true, 99999);
                }else{
                    // resto de campos
                    textFieldTableEditor= new TextFieldTableEditor(5);
                }
                textFieldTableEditor.setEnabled(true);
                tableColumn.setCellEditor(textFieldTableEditor);
                tableColumn.setCellRenderer(new TextFieldRenderer());
            }


			/** Solicitud */
			municipioJTField.setText(CConstantesLicencias.Municipio);
			municipioJTField.setEnabled(false);
			provinciaJTField.setText(CConstantesLicencias.Provincia);
			provinciaJTField.setEnabled(false);

            /** cnae */
            cnaeTField= new com.geopista.app.utilidades.TextField(16);
            datosSolicitudJPanel.add(cnaeTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 167, 330, -1));

            /** tasa */
            tasaTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
            datosSolicitudJPanel.add(tasaTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 188, 100, -1));

            /** impuesto */
            impuestoTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
            datosSolicitudJPanel.add(impuestoTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 188, 100, -1));

            /** entregada a */
            entregadaATField= new com.geopista.app.utilidades.TextField(68);
            datosNotificacionJPanel.add(entregadaATField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 160, 260, 20));
            entregadaATField.setVisible(false);
            okJButton.setVisible(false);
            entregadaATextJLabel= new javax.swing.JLabel();
            entregadaATextJLabel.setText("");
            datosNotificacionJPanel.add(entregadaATextJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 260, 20));


            /**  responsable */
            responsableTField= new com.geopista.app.utilidades.TextField(68);
            expedienteJPanel.add(responsableTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 250, 300, -1));

			/** Definimos el componente notificacionesJTable */
			notificacionesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            notificacionesSorted= new TableSorted(_notificacionesExpedienteTableModel);
            notificacionesSorted.setTableHeader(notificacionesJTable.getTableHeader());
			notificacionesJTable.setModel(notificacionesSorted);
            notificacionesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            notificacionesJTable.getTableHeader().setReorderingAllowed(false);
            for (int j=0; j< notificacionesJTable.getColumnCount(); j++){
                TableColumn column = notificacionesJTable.getColumnModel().getColumn(j);
                column.setMinWidth(150);
                column.setMaxWidth(300);
                column.setWidth(150);
                column.setPreferredWidth(150);
                column.setResizable(true);
            }
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
            TableColumn col= notificacionesJTable.getColumnModel().getColumn(notificacionesHiddenCol);
            col.setResizable(false);
            col.setWidth(0);
            col.setMaxWidth(0);
            col.setMinWidth(0);
            col.setPreferredWidth(0);




			/** Definimos el componente eventosJTable */
			eventosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            eventosSorted= new TableSorted(_eventosExpedienteTableModel);
            eventosSorted.setTableHeader(eventosJTable.getTableHeader());
			eventosJTable.setModel(eventosSorted);
            eventosJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            eventosJTable.getTableHeader().setReorderingAllowed(false);
            for (int j=0; j< eventosJTable.getColumnCount(); j++){
                TableColumn column = eventosJTable.getColumnModel().getColumn(j);
                if (j==2){
                    column.setMinWidth(75);
                    column.setMaxWidth(75);
                    column.setWidth(75);
                    column.setPreferredWidth(75);
                }else{
                    column.setMinWidth(150);
                    column.setMaxWidth(300);
                    column.setWidth(150);
                    column.setPreferredWidth(150);
                }
                column.setResizable(true);
            }
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
            col= eventosJTable.getColumnModel().getColumn(eventosHiddenCol);
            col.setResizable(false);
            col.setWidth(0);
            col.setMaxWidth(0);
            col.setMinWidth(0);
            col.setPreferredWidth(0);



			/** Definimos el componente historicoJTable */
			historicoJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            historicoSorted= new TableSorted(_historicoExpedienteTableModel);
            historicoSorted.setTableHeader(historicoJTable.getTableHeader());
			historicoJTable.setModel(historicoSorted);
            historicoJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            historicoJTable.getTableHeader().setReorderingAllowed(false);
            for (int j=0; j< historicoJTable.getColumnCount(); j++){
                TableColumn column = historicoJTable.getColumnModel().getColumn(j);
                column.setMinWidth(150);
                column.setMaxWidth(300);
                column.setWidth(150);
                column.setPreferredWidth(150);
                column.setResizable(true);
            }
            ((TableSorted)historicoJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    historicoJTableFocusGained();
                }
            });
            ((TableSorted)historicoJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    historicoJTableMouseClicked();
                }
            });
            col= historicoJTable.getColumnModel().getColumn(historicoHiddenCol);
            col.setResizable(false);
            col.setWidth(0);
            col.setMaxWidth(0);
            col.setMinWidth(0);
            col.setPreferredWidth(0);




			buscarExpedienteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
            fechaLimiteObraJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
			nombreViaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
			refCatastralJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
            deleteParcelaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);

			mapToTableJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoFlechaIzquierda);
			tableToMapJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoFlechaDerecha);

			buscarDNIPropietarioJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
			buscarDNIRepresentanteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
			buscarDNITecnicoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
			buscarDNIPromotorJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);

            borrarRepresentanteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
            borrarTecnicoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
            borrarPromotorJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
            okJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoOK);

            /** Historico */
            if (_expediente == null){
                nuevoHistoricoJButton.setEnabled(false);
                borrarHistoricoJButton.setEnabled(false);
                modHistoricoJButton.setEnabled(false);
            }

            modHistoricoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoOK);
            borrarHistoricoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoRemove);
            nuevoHistoricoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoAdd);
            generarFichaHistoricoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoGenerarFicha);

            /** emplazamiento */
            nombreViaTField= new com.geopista.app.utilidades.TextField(68);
            nombreViaTField.setEditable(true);
            emplazamientoJPanel.add(nombreViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 46, 190, -1));

            //numeroViaNumberTField=  new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            numeroViaNumberTField=  new com.geopista.app.utilidades.TextField(8);
            numeroViaNumberTField.setEditable(true);
            emplazamientoJPanel.add(numeroViaNumberTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 71, 80, -1));

            portalViaTField= new com.geopista.app.utilidades.TextField(5);
            portalViaTField.setEditable(true);
            emplazamientoJPanel.add(portalViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 71, 80, -1));

            plantaViaTField= new com.geopista.app.utilidades.TextField(5);
            plantaViaTField.setEditable(true);
            emplazamientoJPanel.add(plantaViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 71, 70, -1));

            letraViaTField= new com.geopista.app.utilidades.TextField(5);
            letraViaTField.setEditable(true);
            emplazamientoJPanel.add(letraViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 71, 70, -1));

            cpostalViaTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            cpostalViaTField.setEditable(true);
            emplazamientoJPanel.add(cpostalViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 92, 80, -1));

			return true;
		} catch (Exception ex) {
			logger.error("Exception al inicializar los datos: " , ex);
			return false;
		}

	}

    private void formComponentShown() {
        if (!fromMenu){
            consultarJButtonActionPerformed();
        }
   }



    /** Los estados no pueden redefinirse como dominio, puesto que necesitamos el valor del campo step */
    public void initComboBoxesEstructuras(){
        while (!Estructuras.isCargada())
        {
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }

        /** Inicializamos los comboBox que llevan estructuras */
        /** solicitud */
        tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposActividad(), null, literales.getLocale().toString(), false);
        datosSolicitudJPanel.add(tipoObraEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 41,180, 20));
        datosSolicitudJPanel.add(jCheckBoxActividadNoCalificada, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 41,-1, 20));
        jCheckBoxActividadNoCalificada.setEnabled(false);

        /**expediente */

        finalizacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposFinalizacion(), null, literales.getLocale().toString(), true);
        finalizacionEJCBox.setEnabled(false);
        finalizacionEJCBox.setEditable(false);

        expedienteJPanel.add(finalizacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 550, 300, -1));
        expedienteJPanel.add(finalizaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, -1, -1));
        tramitacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposTramitacion(), null, literales.getLocale().toString(), false);
        expedienteJPanel.add(tramitacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 142, 300, 20));

        /** propietario */
        viaNotificacionPropietarioEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, literales.getLocale().toString(), false);
        viaNotificacionPropietarioEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionPropietarioEJCBoxActionPerformed();}});

        datosNotificacionPropietarioJPanel.add(viaNotificacionPropietarioEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170,20, 300, 20));
        tipoViaNotificacionPropietarioEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, literales.getLocale().toString(), false);
        datosNotificacionPropietarioJPanel.add(tipoViaNotificacionPropietarioEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 125, 300, 20));

        /** representante */
        viaNotificacionRepresentanteEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, literales.getLocale().toString(), false);
        viaNotificacionRepresentanteEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionRepresentanteEJCBoxActionPerformed();}});

        datosNotificacionRepresentanteJPanel.add(viaNotificacionRepresentanteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 300, 20));
        tipoViaNotificacionRepresentanteEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, literales.getLocale().toString(), false);
        datosNotificacionRepresentanteJPanel.add(tipoViaNotificacionRepresentanteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 125, 300, 20));

        /** tecnico */
        viaNotificacionTecnicoEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, literales.getLocale().toString(), false);
        viaNotificacionTecnicoEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionTecnicoEJCBoxActionPerformed();}});

        datosNotificacionTecnicoJPanel.add(viaNotificacionTecnicoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 300, 20));
        tipoViaNotificacionTecnicoEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, literales.getLocale().toString(), false);
        datosNotificacionTecnicoJPanel.add(tipoViaNotificacionTecnicoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 125, 300, 20));

        /** promotor */
        viaNotificacionPromotorEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, literales.getLocale().toString(), false);
        viaNotificacionPromotorEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionPromotorEJCBoxActionPerformed();}});

        datosNotificacionPromotorJPanel.add(viaNotificacionPromotorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 300, 20));
        tipoViaNotificacionPromotorEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, literales.getLocale().toString(), false);
        datosNotificacionPromotorJPanel.add(tipoViaNotificacionPromotorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 125, 300, 20));

        /** emplazamiento (tipoVia) */
        tipoViaINEEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, literales.getLocale().toString(), true);
        tipoViaINEEJCBox.setSelectedIndex(0);
        emplazamientoJPanel.add(tipoViaINEEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 46, 110, 20));


    }

    private void viaNotificacionPropietarioEJCBoxActionPerformed() {
        String index= viaNotificacionPropietarioEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailPropietarioJLabel.text")));
            emailPropietarioObligatorio= true;
        }else{
            emailPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailPropietarioJLabel.text"));
            emailPropietarioObligatorio= false;
        }

    }

    private void viaNotificacionRepresentanteEJCBoxActionPerformed() {

        String index= viaNotificacionRepresentanteEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailRepresentanteJLabel.text")));
            emailRepresentanteObligatorio= true;
        }else{
            emailRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailRepresentanteJLabel.text"));
            emailRepresentanteObligatorio= false;
        }

    }

    private void viaNotificacionTecnicoEJCBoxActionPerformed() {
        String index= viaNotificacionTecnicoEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailTecnicoJLabel.text")));
            emailTecnicoObligatorio= true;
        }else{
            emailTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailTecnicoJLabel.text"));
            emailTecnicoObligatorio= false;
        }

    }


    private void viaNotificacionPromotorEJCBoxActionPerformed() {
        String index= viaNotificacionPromotorEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailPromotorJLabel.text")));
            emailPromotorObligatorio= true;
        }else{
            emailPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailPromotorJLabel.text"));
            emailPromotorObligatorio= false;
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
        jTabbedPaneSolicitud= new JTabbedPane();
        expedienteJPanel = new javax.swing.JPanel();
        estadoExpedienteJLabel = new javax.swing.JLabel();
        finalizaJLabel= new javax.swing.JLabel();
        numExpedienteJLabel = new javax.swing.JLabel();
        servicioExpedienteJLabel = new javax.swing.JLabel();
        tramitacionJLabel = new javax.swing.JLabel();
        asuntoExpedienteJLabel = new javax.swing.JLabel();
        fechaAperturaJLabel = new javax.swing.JLabel();
        observacionesExpedienteJLabel = new javax.swing.JLabel();
        servicioExpedienteJTField = new javax.swing.JTextField();
        numExpedienteJTField = new javax.swing.JTextField();
        asuntoExpedienteJTField = new javax.swing.JTextField();
        fechaAperturaJTField = new javax.swing.JTextField();
        inicioJLabel = new javax.swing.JLabel();
        inicioJTField = new javax.swing.JTextField();
        silencioJCheckBox = new javax.swing.JCheckBox();
        buscarExpedienteJButton = new javax.swing.JButton();
        consultarJButton = new javax.swing.JButton();
        silencioJLabel = new javax.swing.JLabel();
        notaJLabel = new javax.swing.JLabel();
        observacionesExpedienteJScrollPane = new javax.swing.JScrollPane();
        observacionesExpedienteJTArea = new javax.swing.JTextArea();
        estadoExpedienteJCBox = new javax.swing.JComboBox();
        responsableJLabel = new javax.swing.JLabel();
        obraMayorJPanel = new javax.swing.JPanel();
        datosSolicitudJPanel = new javax.swing.JPanel();
        tipoObraJLabel = new javax.swing.JLabel();
        jCheckBoxActividadNoCalificada= new javax.swing.JCheckBox();

        unidadTJLabel = new javax.swing.JLabel();
        motivoJLabel = new javax.swing.JLabel();
        asuntoJLabel = new javax.swing.JLabel();
        fechaSolicitudJLabel = new javax.swing.JLabel();
        observacionesJLabel = new javax.swing.JLabel();
        unidadTJTField = new javax.swing.JTextField();
        motivoJTField = new javax.swing.JTextField();
        asuntoJTField = new javax.swing.JTextField();
        fechaSolicitudJTField = new javax.swing.JTextField();
        observacionesJScrollPane = new javax.swing.JScrollPane();
        observacionesJTArea = new javax.swing.JTextArea();
        numRegistroJTField = new javax.swing.JTextField();
        numRegistroJLabel = new javax.swing.JLabel();
        unidadRJLabel = new javax.swing.JLabel();
        unidadRJTField = new javax.swing.JTextField();
        tasaJLabel = new javax.swing.JLabel();
        cnaeJLabel= new javax.swing.JLabel();
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
        nombreViaJButton = new javax.swing.JButton();
        refCatastralJLabel = new javax.swing.JLabel();
        refCatastralJTextField = new javax.swing.JTextField();
        refCatastralJButton = new javax.swing.JButton();
        referenciasCatastralesJScrollPane = new javax.swing.JScrollPane();
        referenciasCatastralesJTable = new javax.swing.JTable();
        mapToTableJButton = new javax.swing.JButton();
        tableToMapJButton = new javax.swing.JButton();
        deleteParcelaJButton = new javax.swing.JButton();
        propietarioJPanel = new javax.swing.JPanel();
        datosPersonalesPropietarioJPanel = new javax.swing.JPanel();
        DNIPropietarioJLabel = new javax.swing.JLabel();
        DNIPropietarioJTField = new javax.swing.JTextField();
        nombrePropietarioJLabel = new javax.swing.JLabel();
        nombrePropietarioJTField = new javax.swing.JTextField();
        apellido1PropietarioJLabel = new javax.swing.JLabel();
        apellido2PropietarioJLabel2 = new javax.swing.JLabel();
        apellido1PropietarioJTField = new javax.swing.JTextField();
        apellido2PropietarioJTField = new javax.swing.JTextField();
        buscarDNIPropietarioJButton = new javax.swing.JButton();
        datosNotificacionPropietarioJPanel = new javax.swing.JPanel();
        viaNotificacionPropietarioJLabel = new javax.swing.JLabel();
        faxPropietarioJLabel = new javax.swing.JLabel();
        telefonoPropietarioJLabel = new javax.swing.JLabel();
        movilPropietarioJLabel = new javax.swing.JLabel();
        emailPropietarioJLabel = new javax.swing.JLabel();
        tipoViaPropietarioJLabel = new javax.swing.JLabel();
        nombreViaPropietarioJLabel = new javax.swing.JLabel();
        numeroViaPropietarioJLabel = new javax.swing.JLabel();
        portalPropietarioJLabel = new javax.swing.JLabel();
        plantaPropietarioJLabel = new javax.swing.JLabel();
        escaleraPropietarioJLabel = new javax.swing.JLabel();
        letraPropietarioJLabel = new javax.swing.JLabel();
        cPostalPropietarioJLabel = new javax.swing.JLabel();
        municipioPropietarioJLabel = new javax.swing.JLabel();
        provinciaPropietarioJLabel = new javax.swing.JLabel();
        faxPropietarioJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        telefonoPropietarioJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        movilPropietarioJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        emailPropietarioJTField = new javax.swing.JTextField();
        nombreViaPropietarioJTField = new javax.swing.JTextField();
        numeroViaPropietarioJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        plantaPropietarioJTField = new javax.swing.JTextField();
        portalPropietarioJTField = new javax.swing.JTextField();
        escaleraPropietarioJTField = new javax.swing.JTextField();
        letraPropietarioJTField = new javax.swing.JTextField();
        cPostalPropietarioJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioPropietarioJTField = new javax.swing.JTextField();
        provinciaPropietarioJTField = new javax.swing.JTextField();
        notificarPropietarioJCheckBox = new javax.swing.JCheckBox();
        notificarPropietarioJLabel = new javax.swing.JLabel();
        representanteJPanel = new javax.swing.JPanel();
        datosPersonalesRepresentanteJPanel = new javax.swing.JPanel();
        DNIRepresentanteJLabel = new javax.swing.JLabel();
        DNIRepresentanteJTField = new javax.swing.JTextField();
        nombreRepresentanteJLabel = new javax.swing.JLabel();
        nombreRepresentanteJTField = new javax.swing.JTextField();
        apellido1RepresentanteJLabel = new javax.swing.JLabel();
        apellido2RepresentanteJLabel = new javax.swing.JLabel();
        apellido1RepresentanteJTField = new javax.swing.JTextField();
        apellido2RepresentanteJTField = new javax.swing.JTextField();
        buscarDNIRepresentanteJButton = new javax.swing.JButton();
        borrarRepresentanteJButton = new javax.swing.JButton();
        datosNotificacionRepresentanteJPanel = new javax.swing.JPanel();
        viaNotificacionRepresentanteJLabel = new javax.swing.JLabel();
        faxRepresentanteJLabel = new javax.swing.JLabel();
        telefonoRepresentanteJLabel = new javax.swing.JLabel();
        movilRepresentanteJLabel = new javax.swing.JLabel();
        emailRepresentanteJLabel = new javax.swing.JLabel();
        tipoViaRepresentanteJLabel = new javax.swing.JLabel();
        nombreViaRepresentanteJLabel = new javax.swing.JLabel();
        numeroViaRepresentanteJLabel = new javax.swing.JLabel();
        portalRepresentanteJLabel = new javax.swing.JLabel();
        plantaRepresentanteJLabel = new javax.swing.JLabel();
        escaleraRepresentanteJLabel = new javax.swing.JLabel();
        letraRepresentanteJLabel = new javax.swing.JLabel();
        cPostalRepresentanteJLabel = new javax.swing.JLabel();
        municipioRepresentanteJLabel = new javax.swing.JLabel();
        provinciaRepresentanteJLabel = new javax.swing.JLabel();
        faxRepresentanteJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        telefonoRepresentanteJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        movilRepresentanteJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
        emailRepresentanteJTField = new javax.swing.JTextField();
        nombreViaRepresentanteJTField = new javax.swing.JTextField();
        numeroViaRepresentanteJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        plantaRepresentanteJTField = new javax.swing.JTextField();
        portalRepresentanteJTField = new javax.swing.JTextField();
        escaleraRepresentanteJTField = new javax.swing.JTextField();
        letraRepresentanteJTField = new javax.swing.JTextField();
        cPostalRepresentanteJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioRepresentanteJTField = new javax.swing.JTextField();
        provinciaRepresentanteJTField = new javax.swing.JTextField();
        notificarRepresentanteJCheckBox = new javax.swing.JCheckBox();
        notificarRepresentanteJLabel = new javax.swing.JLabel();
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
        borrarTecnicoJButton = new javax.swing.JButton();
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
        borrarPromotorJButton = new javax.swing.JButton();
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
        notificacionesJPanel = new javax.swing.JPanel();
        datosNotificacionesJPanel = new javax.swing.JPanel();
        notificacionesJScrollPane = new javax.swing.JScrollPane();
        notificacionesJTable = new javax.swing.JTable();
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
        okJButton = new javax.swing.JButton();
        eventosJPanel = new javax.swing.JPanel();
        datosEventosJPanel = new javax.swing.JPanel();
        eventosJScrollPane = new javax.swing.JScrollPane();
        eventosJTable = new javax.swing.JTable();
        descEventoJScrollPane = new javax.swing.JScrollPane();
        descEventoJTArea = new javax.swing.JTextArea();
        historicoJPanel = new javax.swing.JPanel();
        datosHistoricoJPanel = new javax.swing.JPanel();
        historicoJScrollPane = new javax.swing.JScrollPane();
        historicoJTable = new javax.swing.JTable();
        apunteJScrollPane = new javax.swing.JScrollPane();
        apunteJTArea = new javax.swing.JTextArea();
        modHistoricoJButton = new javax.swing.JButton();
        nuevoHistoricoJButton = new javax.swing.JButton();
        borrarHistoricoJButton = new javax.swing.JButton();
        generarFichaHistoricoJButton = new javax.swing.JButton();
        botoneraJPanel = new javax.swing.JPanel();
        aceptarJButton = new javax.swing.JButton();
        publicarJButton = new javax.swing.JButton();
        cancelarJButton = new javax.swing.JButton();
        jButtonGenerarFicha= new javax.swing.JButton();
        jButtonWorkFlow= new javax.swing.JButton();
        editorMapaJPanel = new javax.swing.JPanel();
        jLabelEstadoActual=new javax.swing.JLabel();
        /** documentacion */
        documentacionJPanel= new DocumentacionLicenciasJPanel(literales);
        documentacionJPanel.setModificacion();
        /** Informes **/
        jPanelInformes= new JPanelInformes(desktop, literales);
        /** Resolucion **/
        jPanelResolucion = new JPanelResolucion(desktop,literales);

        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        setClosable(true);
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
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown();
            }
        });


        templateJPanel.setLayout(new BorderLayout());

        obraMayorJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        obraMayorJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));
        jTabbedPaneSolicitud.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPaneSolicitud.setFont(new java.awt.Font("Arial", 0, 10));

        expedienteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expedienteJPanel.setAutoscrolls(true);
        expedienteJPanel.add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 170, 20));
        expedienteJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 170, 20));
        jLabelEstadoActual.setForeground(new Color(255,0,102));
        expedienteJPanel.add(jLabelEstadoActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 300, 20));
        expedienteJPanel.add(servicioExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 121, 170, 20));
        expedienteJPanel.add(tramitacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 142, 170, 20));
        expedienteJPanel.add(asuntoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 163, 170, 20));
        expedienteJPanel.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 184, 170, 20));
        expedienteJPanel.add(observacionesExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 170, 20));

        expedienteJPanel.add(jPanelResolucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 370,500, 175));
        expedienteJPanel.add(servicioExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 121, 300, -1));
        expedienteJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 280, -1));
        expedienteJPanel.add(asuntoExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 163, 300, -1));
        fechaAperturaJTField.setEnabled(false);
        expedienteJPanel.add(fechaAperturaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 184, 300, -1));
        expedienteJPanel.add(inicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 205, 170, 20));
        expedienteJPanel.add(inicioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 205, 300, -1));
        expedienteJPanel.add(silencioJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 226, 30, -1));
        buscarExpedienteJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        expedienteJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 20, 20));
        consultarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarJButtonActionPerformed();
            }
        });

        expedienteJPanel.add(consultarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 130, -1));
        expedienteJPanel.add(silencioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 226, 170, 20));
        notaJLabel.setFont(new java.awt.Font("Arial", 0, 10));
        expedienteJPanel.add(notaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 226, 275, 20));
        observacionesExpedienteJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        observacionesExpedienteJTArea.setLineWrap(true);
        observacionesExpedienteJTArea.setRows(3);
        observacionesExpedienteJTArea.setTabSize(4);
        observacionesExpedienteJTArea.setWrapStyleWord(true);
        observacionesExpedienteJTArea.setBorder(null);
        observacionesExpedienteJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        observacionesExpedienteJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        observacionesExpedienteJScrollPane.setViewportView(observacionesExpedienteJTArea);
        expedienteJPanel.add(observacionesExpedienteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, 300, 90));

        expedienteJPanel.add(estadoExpedienteJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 300, 20));
        expedienteJPanel.add(responsableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 170, 20));
        obraMayorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder("Datos solicitud"));
        datosSolicitudJPanel.setAutoscrolls(true);
        datosSolicitudJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 140, 20));

        datosSolicitudJPanel.add(unidadTJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 140, 20));
        datosSolicitudJPanel.add(motivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 140, 20));

        datosSolicitudJPanel.add(asuntoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 140, 20));

        datosSolicitudJPanel.add(fechaSolicitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 140, 20));

        datosSolicitudJPanel.add(observacionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 140, 20));

        datosSolicitudJPanel.add(unidadTJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 83, 330, -1));

        datosSolicitudJPanel.add(motivoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 104, 330, -1));

        datosSolicitudJPanel.add(asuntoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 125, 330, -1));

        fechaSolicitudJTField.setEnabled(false);
        datosSolicitudJPanel.add(fechaSolicitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 146, 80, -1));

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

        datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 330, 70));

        numRegistroJTField.setEnabled(false);
        datosSolicitudJPanel.add(numRegistroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 330, -1));

        datosSolicitudJPanel.add(numRegistroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 140, 20));

        datosSolicitudJPanel.add(unidadRJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 140, 20));

        datosSolicitudJPanel.add(unidadRJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 62, 330, -1));

        /** cnae */
        datosSolicitudJPanel.add(cnaeJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 167, 140, 20));

        tasaJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(tasaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 188, 100, 20));

        datosSolicitudJPanel.add(impuestoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 188, 140, 20));

        fechaLimiteObraJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(fechaLimiteObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 146, 130, 20));

        fechaLimiteObraJTField.setEnabled(false);
        datosSolicitudJPanel.add(fechaLimiteObraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 146, 80, -1));
        fechaLimiteObraJButton.setIcon(new javax.swing.ImageIcon(""));
        fechaLimiteObraJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaLimiteObraJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaLimiteObraJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaLimiteObraJButtonActionPerformed();
            }
        });

        datosSolicitudJPanel.add(fechaLimiteObraJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 146, 20, 20));

        obraMayorJPanel.add(datosSolicitudJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 290));

        emplazamientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder("Emplazamiento"));
        emplazamientoJPanel.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 46, 130, 20));

        emplazamientoJPanel.add(numeroViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 71, 130, 20));

        emplazamientoJPanel.add(cPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 92, 130, 20));

        emplazamientoJPanel.add(provinciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 113, 130, 20));

        municipioJTField.setEnabled(false);
        emplazamientoJPanel.add(municipioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 92, 240, -1));

        provinciaJTField.setEnabled(false);
        emplazamientoJPanel.add(provinciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 113, 330, -1));

        nombreViaJButton.setIcon(new javax.swing.ImageIcon(""));
        nombreViaJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        nombreViaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        nombreViaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreViaJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(nombreViaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 46, 20, 20));

        emplazamientoJPanel.add(refCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 20));

        refCatastralJTextField.setEnabled(false);
        emplazamientoJPanel.add(refCatastralJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 310, -1));

        refCatastralJButton.setIcon(new javax.swing.ImageIcon(""));
        refCatastralJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        refCatastralJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        refCatastralJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refCatastralJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(refCatastralJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 20, 20));

        referenciasCatastralesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Referencia catastral"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        referenciasCatastralesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        referenciasCatastralesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // mostramos ventana con los datos de la referencia catastral
                referenciasCatastralesJTableMouseClicked();
        }});

        referenciasCatastralesJScrollPane.setViewportView(referenciasCatastralesJTable);

        emplazamientoJPanel.add(referenciasCatastralesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 470, 105));

        mapToTableJButton.setIcon(new javax.swing.ImageIcon(""));
        mapToTableJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mapToTableJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        mapToTableJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapToTableJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(mapToTableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, 20, 20));

        tableToMapJButton.setIcon(new javax.swing.ImageIcon(""));
        tableToMapJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableToMapJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        tableToMapJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableToMapJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(tableToMapJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, 20, 20));

        deleteParcelaJButton.setIcon(new javax.swing.ImageIcon(""));
        deleteParcelaJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        deleteParcelaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        deleteParcelaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteParcelaJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(deleteParcelaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 225, 20, 20));

        obraMayorJPanel.add(emplazamientoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 518, 257));

        propietarioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPropietarioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesPropietarioJPanel.add(DNIPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

         DNIPropietarioJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIPropietarioJTFieldKeyReleased();
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIPropietarioJTFieldKeyTyped();
            }
        });

        datosPersonalesPropietarioJPanel.add(DNIPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 280, -1));

        datosPersonalesPropietarioJPanel.add(nombrePropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 20));

        datosPersonalesPropietarioJPanel.add(nombrePropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 300, -1));

        datosPersonalesPropietarioJPanel.add(apellido1PropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 71, 130, 20));

        datosPersonalesPropietarioJPanel.add(apellido2PropietarioJLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 92, 130, 20));

        datosPersonalesPropietarioJPanel.add(apellido1PropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 71, 300, -1));

        datosPersonalesPropietarioJPanel.add(apellido2PropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 92, 300, -1));

        buscarDNIPropietarioJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNIPropietarioJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIPropietarioJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIPropietarioJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIPropietarioJButtonActionPerformed();
            }
        });


        datosPersonalesPropietarioJPanel.add(buscarDNIPropietarioJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 20, 20));

        propietarioJPanel.add(datosPersonalesPropietarioJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 132));

        datosNotificacionPropietarioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionPropietarioJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionPropietarioJPanel.add(viaNotificacionPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        datosNotificacionPropietarioJPanel.add(faxPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 130, 20));

        datosNotificacionPropietarioJPanel.add(telefonoPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 130, 20));

        datosNotificacionPropietarioJPanel.add(movilPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 130, 20));

        datosNotificacionPropietarioJPanel.add(emailPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 130, 20));

        datosNotificacionPropietarioJPanel.add(tipoViaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 130, 20));

        datosNotificacionPropietarioJPanel.add(nombreViaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 130, 20));

        datosNotificacionPropietarioJPanel.add(numeroViaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 175, 90, 20));

        datosNotificacionPropietarioJPanel.add(portalPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 238, 50, 20));

        datosNotificacionPropietarioJPanel.add(plantaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 196, 70, 20));

        datosNotificacionPropietarioJPanel.add(escaleraPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 259, 60, 20));

        datosNotificacionPropietarioJPanel.add(letraPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 217, 40, 20));

        datosNotificacionPropietarioJPanel.add(cPostalPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 285, 130, 20));

        datosNotificacionPropietarioJPanel.add(municipioPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 306, 130, 20));

        datosNotificacionPropietarioJPanel.add(provinciaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 327, 130, 20));

        datosNotificacionPropietarioJPanel.add(faxPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 41, 300, -1));

        datosNotificacionPropietarioJPanel.add(telefonoPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 62, 300, -1));

        datosNotificacionPropietarioJPanel.add(movilPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 83, 300, -1));

        datosNotificacionPropietarioJPanel.add(emailPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 104, 300, -1));

        datosNotificacionPropietarioJPanel.add(nombreViaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 146, 300, -1));

        datosNotificacionPropietarioJPanel.add(numeroViaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 175, 150, -1));

        datosNotificacionPropietarioJPanel.add(plantaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 196, 150, -1));

        datosNotificacionPropietarioJPanel.add(portalPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 238, 150, -1));

        datosNotificacionPropietarioJPanel.add(escaleraPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 259, 150, -1));

        datosNotificacionPropietarioJPanel.add(letraPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 217, 150, -1));

        datosNotificacionPropietarioJPanel.add(cPostalPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 285, 300, -1));

        datosNotificacionPropietarioJPanel.add(municipioPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 306, 300, -1));

        datosNotificacionPropietarioJPanel.add(provinciaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 327, 300, -1));

        notificarPropietarioJCheckBox.setSelected(true);
        notificarPropietarioJCheckBox.setEnabled(false);
        datosNotificacionPropietarioJPanel.add(notificarPropietarioJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 70, -1));

        datosNotificacionPropietarioJPanel.add(notificarPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 150, 20));

        propietarioJPanel.add(datosNotificacionPropietarioJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 132, 518, 415));

        representanteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentanteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesRepresentanteJPanel.add(DNIRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        DNIRepresentanteJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIRepresentanteJTFieldKeyReleased();
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIRepresentanteJTFieldKeyTyped();
            }
        });

        datosPersonalesRepresentanteJPanel.add(DNIRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 280, -1));

        datosPersonalesRepresentanteJPanel.add(nombreRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 20));

        datosPersonalesRepresentanteJPanel.add(nombreRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 300, -1));

        datosPersonalesRepresentanteJPanel.add(apellido1RepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 71, 130, 20));

        datosPersonalesRepresentanteJPanel.add(apellido2RepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 92, 130, 20));

        datosPersonalesRepresentanteJPanel.add(apellido1RepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 71, 300, -1));

        datosPersonalesRepresentanteJPanel.add(apellido2RepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 92, 300, -1));

        buscarDNIRepresentanteJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNIRepresentanteJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIRepresentanteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIRepresentanteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIRepresentanteJButtonActionPerformed();
            }
        });

        datosPersonalesRepresentanteJPanel.add(buscarDNIRepresentanteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 20, 20));

        borrarRepresentanteJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarRepresentanteJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarRepresentanteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarRepresentanteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarRepresentanteJButtonActionPerformed();
            }
        });

        datosPersonalesRepresentanteJPanel.add(borrarRepresentanteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 20, 20));

        representanteJPanel.add(datosPersonalesRepresentanteJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 132));

        datosNotificacionRepresentanteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        datosNotificacionRepresentanteJPanel.add(viaNotificacionRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        datosNotificacionRepresentanteJPanel.add(faxRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 130, 20));

        datosNotificacionRepresentanteJPanel.add(telefonoRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 130, 20));

        datosNotificacionRepresentanteJPanel.add(movilRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 130, 20));

        datosNotificacionRepresentanteJPanel.add(emailRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 130, 20));

        datosNotificacionRepresentanteJPanel.add(tipoViaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 130, 20));

        datosNotificacionRepresentanteJPanel.add(nombreViaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 100, 20));

        datosNotificacionRepresentanteJPanel.add(numeroViaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 175, 90, 20));

        datosNotificacionRepresentanteJPanel.add(portalRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 238, 50, 20));

        datosNotificacionRepresentanteJPanel.add(plantaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 196, 70, 20));

        datosNotificacionRepresentanteJPanel.add(escaleraRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 259, 60, 20));

        datosNotificacionRepresentanteJPanel.add(letraRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 217, 40, 20));

        datosNotificacionRepresentanteJPanel.add(cPostalRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 285, 130, 20));

        datosNotificacionRepresentanteJPanel.add(municipioRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 306, 130, 20));

        datosNotificacionRepresentanteJPanel.add(provinciaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 327, 130, 20));

        datosNotificacionRepresentanteJPanel.add(faxRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 41, 300, -1));

        datosNotificacionRepresentanteJPanel.add(telefonoRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 62, 300, -1));

        datosNotificacionRepresentanteJPanel.add(movilRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 83, 300, -1));

        datosNotificacionRepresentanteJPanel.add(emailRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 104, 300, -1));

        datosNotificacionRepresentanteJPanel.add(nombreViaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 146, 300, -1));

        datosNotificacionRepresentanteJPanel.add(numeroViaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 175, 150, -1));

        datosNotificacionRepresentanteJPanel.add(plantaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 196, 150, -1));

        datosNotificacionRepresentanteJPanel.add(portalRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 238, 150, -1));

        datosNotificacionRepresentanteJPanel.add(escaleraRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 259, 150, -1));

        datosNotificacionRepresentanteJPanel.add(letraRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 217, 150, -1));

        datosNotificacionRepresentanteJPanel.add(cPostalRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 285, 300, 20));

        datosNotificacionRepresentanteJPanel.add(municipioRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 306, 300, 20));

        datosNotificacionRepresentanteJPanel.add(provinciaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 327, 300, 20));

        datosNotificacionRepresentanteJPanel.add(notificarRepresentanteJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 60, -1));

        datosNotificacionRepresentanteJPanel.add(notificarRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 150, 20));

        representanteJPanel.add(datosNotificacionRepresentanteJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 132, 518, 415));

        tecnicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTecnicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesTecnicoJPanel.add(DNITecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        DNITecnicoJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNITecnicoJTFieldKeyReleased();
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNITecnicoJTFieldKeyTyped();
            }
        });

        datosPersonalesTecnicoJPanel.add(DNITecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 280, -1));

        datosPersonalesTecnicoJPanel.add(nombreTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 46, 130, 20));

        datosPersonalesTecnicoJPanel.add(nombreTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 46, 300, -1));

        datosPersonalesTecnicoJPanel.add(apellido1TecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 67, 130, 20));

        datosPersonalesTecnicoJPanel.add(apellido2TecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 88, 130, 20));

        datosPersonalesTecnicoJPanel.add(apellido1TecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 67, 300, -1));

        datosPersonalesTecnicoJPanel.add(apellido2TecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 88, 300, -1));

        buscarDNITecnicoJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNITecnicoJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNITecnicoJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNITecnicoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNITecnicoJButtonActionPerformed();
            }
        });

        datosPersonalesTecnicoJPanel.add(buscarDNITecnicoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 20, 20));

        datosPersonalesTecnicoJPanel.add(colegioTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 109, 130, 20));

        datosPersonalesTecnicoJPanel.add(visadoTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 130, 20));

        datosPersonalesTecnicoJPanel.add(titulacionTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 151, 130, 20));

        datosPersonalesTecnicoJPanel.add(colegioTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 109, 300, -1));

        datosPersonalesTecnicoJPanel.add(visadoTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 300, -1));

        datosPersonalesTecnicoJPanel.add(titulacionTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 151, 300, -1));

        borrarTecnicoJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarTecnicoJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarTecnicoJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarTecnicoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarTecnicoJButtonActionPerformed();
            }
        });

        datosPersonalesTecnicoJPanel.add(borrarTecnicoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 20, 20));

        tecnicoJPanel.add(datosPersonalesTecnicoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 180));

        datosNotificacionTecnicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        datosNotificacionTecnicoJPanel.add(viaNotificacionTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        datosNotificacionTecnicoJPanel.add(faxTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 130, 20));

        datosNotificacionTecnicoJPanel.add(telefonoTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 130, 20));

        datosNotificacionTecnicoJPanel.add(movilTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 130, 20));

        datosNotificacionTecnicoJPanel.add(emailTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 130, 20));

        datosNotificacionTecnicoJPanel.add(tipoViaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 130, 20));

        datosNotificacionTecnicoJPanel.add(nombreViaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 130, 20));

        datosNotificacionTecnicoJPanel.add(numeroViaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 70, 20));

        datosNotificacionTecnicoJPanel.add(portalTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 233, 70, 20));

        datosNotificacionTecnicoJPanel.add(plantaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 191, 70, 20));

        datosNotificacionTecnicoJPanel.add(escaleraTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 254, 70, 20));

        datosNotificacionTecnicoJPanel.add(letraTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 212, 70, 20));

        datosNotificacionTecnicoJPanel.add(cPostalTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 278, 130, 20));

        datosNotificacionTecnicoJPanel.add(municipioTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 299, 130, 20));

        datosNotificacionTecnicoJPanel.add(provinciaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 130, 20));

        datosNotificacionTecnicoJPanel.add(faxTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 41, 300, -1));

        datosNotificacionTecnicoJPanel.add(telefonoTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 62, 300, -1));

        datosNotificacionTecnicoJPanel.add(movilTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 83, 300, -1));

        datosNotificacionTecnicoJPanel.add(emailTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 104, 300, -1));

        datosNotificacionTecnicoJPanel.add(nombreViaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 146, 300, -1));

        datosNotificacionTecnicoJPanel.add(numeroViaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 150, -1));

        datosNotificacionTecnicoJPanel.add(plantaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 191, 150, -1));

        datosNotificacionTecnicoJPanel.add(portalTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 233, 150, -1));

        datosNotificacionTecnicoJPanel.add(escaleraTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 254, 150, -1));

        datosNotificacionTecnicoJPanel.add(letraTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 212, 150, -1));

        datosNotificacionTecnicoJPanel.add(cPostalTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 278, 300, -1));

        datosNotificacionTecnicoJPanel.add(municipioTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 299, 300, -1));

        datosNotificacionTecnicoJPanel.add(provinciaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 300, -1));

        notificarTecnicoJCheckBox.setVisible(false);
        datosNotificacionTecnicoJPanel.add(notificarTecnicoJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 342, 40, -1));
        notificarTecnicoJLabel.setVisible(false);
        datosNotificacionTecnicoJPanel.add(notificarTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 342, 150, 20));

        tecnicoJPanel.add(datosNotificacionTecnicoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 518, 367));

        promotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesPromotorJPanel.add(DNIPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        DNIPromotorJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIPromotorJTFieldKeyReleased();
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIPromotorJTFieldKeyTyped();
            }
        });

        datosPersonalesPromotorJPanel.add(DNIPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 280, -1));

        datosPersonalesPromotorJPanel.add(nombrePromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 46, 130, 20));

        datosPersonalesPromotorJPanel.add(nombrePromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 46, 300, -1));

        datosPersonalesPromotorJPanel.add(apellido1PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 67, 130, 20));

        datosPersonalesPromotorJPanel.add(apellido2PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 88, 130, 20));

        datosPersonalesPromotorJPanel.add(apellido1PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 67, 300, -1));

        datosPersonalesPromotorJPanel.add(apellido2PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 88, 300, -1));

        buscarDNIPromotorJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNIPromotorJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIPromotorJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIPromotorJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIPromotorJButtonActionPerformed();
            }
        });

        datosPersonalesPromotorJPanel.add(buscarDNIPromotorJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 20, 20));

        datosPersonalesPromotorJPanel.add(colegioPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 109, 130, 20));

        datosPersonalesPromotorJPanel.add(visadoPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 130, 20));

        datosPersonalesPromotorJPanel.add(titulacionPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 151, 130, 20));

        datosPersonalesPromotorJPanel.add(colegioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 109, 300, -1));

        datosPersonalesPromotorJPanel.add(visadoPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 300, -1));

        datosPersonalesPromotorJPanel.add(titulacionPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 151, 300, -1));

        borrarPromotorJButton.setIcon(new javax.swing.ImageIcon(""));
        borrarPromotorJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarPromotorJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarPromotorJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarPromotorJButtonActionPerformed();
            }
        });

        datosPersonalesPromotorJPanel.add(borrarPromotorJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 20, 20));

        promotorJPanel.add(datosPersonalesPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 180));

        datosNotificacionPromotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionPromotorJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        datosNotificacionPromotorJPanel.add(viaNotificacionPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        datosNotificacionPromotorJPanel.add(faxPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 130, 20));

        datosNotificacionPromotorJPanel.add(telefonoPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 130, 20));

        datosNotificacionPromotorJPanel.add(movilPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 130, 20));

        datosNotificacionPromotorJPanel.add(emailPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 130, 20));

        datosNotificacionPromotorJPanel.add(tipoViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 130, 20));

        datosNotificacionPromotorJPanel.add(nombreViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 130, 20));

        datosNotificacionPromotorJPanel.add(numeroViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 80, 20));

        datosNotificacionPromotorJPanel.add(portalPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 233, 50, 20));

        datosNotificacionPromotorJPanel.add(plantaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 191, 80, 20));

        datosNotificacionPromotorJPanel.add(escaleraPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 254, 60, 20));

        datosNotificacionPromotorJPanel.add(letraPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 212, 80, 20));

        datosNotificacionPromotorJPanel.add(cPostalPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 278, 130, 20));

        datosNotificacionPromotorJPanel.add(municipioPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 299, 130, 20));

        datosNotificacionPromotorJPanel.add(provinciaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 130, 20));

        datosNotificacionPromotorJPanel.add(faxPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 41, 300, -1));

        datosNotificacionPromotorJPanel.add(telefonoPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 62, 300, -1));

        datosNotificacionPromotorJPanel.add(movilPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 83, 300, -1));

        datosNotificacionPromotorJPanel.add(emailPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 104, 300, -1));

        datosNotificacionPromotorJPanel.add(nombreViaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 146, 300, -1));

        datosNotificacionPromotorJPanel.add(numeroViaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 150, -1));

        datosNotificacionPromotorJPanel.add(plantaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 191, 150, -1));

        datosNotificacionPromotorJPanel.add(portalPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 233, 150, -1));

        datosNotificacionPromotorJPanel.add(escaleraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 254, 150, -1));

        datosNotificacionPromotorJPanel.add(letraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 212, 150, -1));

        datosNotificacionPromotorJPanel.add(cPostalPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 278, 300, -1));

        datosNotificacionPromotorJPanel.add(municipioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 299, 300, -1));

        datosNotificacionPromotorJPanel.add(provinciaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 300, -1));

        notificarPromotorJCheckBox.setVisible(false);
        datosNotificacionPromotorJPanel.add(notificarPromotorJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 342, 40, -1));

        notificarPromotorJLabel.setVisible(false);
        datosNotificacionPromotorJPanel.add(notificarPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 342, 150, 20));

        promotorJPanel.add(datosNotificacionPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 518, 367));

        notificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder("Notificaciones"));
        notificacionesJScrollPane.setEnabled(false);
        notificacionesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
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
                notificacionesJTableKeyTyped();
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notificacionesJTableKeyPressed();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                notificacionesJTableKeyReleased();
            }
        });


        notificacionesJScrollPane.setViewportView(notificacionesJTable);

        datosNotificacionesJPanel.add(notificacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 500, 340));

        datosNotificacionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder("Datos de Notificaci\u00f3n de la Notificaci\u00f3n Seleccionada"));
        datosNotificacionJPanel.add(datosNombreApellidosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 180, 20));

        datosNotificacionJPanel.add(datosDireccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 180, 20));

        datosNotificacionJPanel.add(datosCPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 180, 20));

        datosNotificacionJPanel.add(datosNotificarPorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 180, 20));

        datosNombreApellidosJTField.setEditable(false);
        datosNombreApellidosJTField.setBorder(null);
        datosNotificacionJPanel.add(datosNombreApellidosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 280, 20));

        datosCPostalJTField.setEditable(false);
        datosCPostalJTField.setBorder(null);
        datosNotificacionJPanel.add(datosCPostalJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 280, 20));

        datosDireccionJTField.setEditable(false);
        datosDireccionJTField.setBorder(null);
        datosNotificacionJPanel.add(datosDireccionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 280, 20));

        datosNotificarPorJTField.setEditable(false);
        datosNotificarPorJTField.setBorder(null);
        datosNotificacionJPanel.add(datosNotificarPorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 280, 20));

        datosNotificacionJPanel.add(entregadaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 180, 20));

        okJButton.setIcon(new javax.swing.ImageIcon(""));
        okJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        okJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed();
            }
        });

        datosNotificacionJPanel.add(okJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 160, 20, 20));

        datosNotificacionesJPanel.add(datosNotificacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 500, 190));

        notificacionesJPanel.add(datosNotificacionesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 523, 579));

        eventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosEventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosEventosJPanel.setBorder(new javax.swing.border.TitledBorder("Eventos"));
        eventosJScrollPane.setBorder(null);
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
            public void focusGained(java.awt.event.FocusEvent evt) {
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
                eventosJTableKeyTyped();
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                eventosJTableKeyPressed();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                eventosJTableKeyReleased();
            }
        });


        eventosJScrollPane.setViewportView(eventosJTable);

        datosEventosJPanel.add(eventosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 500, 440));

        descEventoJScrollPane.setBorder(new javax.swing.border.TitledBorder("Descripci\u00f3n  del Evento Seleccionado"));
        descEventoJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        descEventoJTArea.setEditable(false);
        descEventoJTArea.setLineWrap(true);
        descEventoJTArea.setRows(3);
        descEventoJTArea.setTabSize(4);
        descEventoJTArea.setWrapStyleWord(true);
        descEventoJTArea.setBorder(null);
        descEventoJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        descEventoJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        descEventoJScrollPane.setViewportView(descEventoJTArea);

        datosEventosJPanel.add(descEventoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 500, 90));

        eventosJPanel.add(datosEventosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 523, 579));

        historicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosHistoricoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosHistoricoJPanel.setBorder(new javax.swing.border.TitledBorder("Hist\u00f3rico"));
        historicoJScrollPane.setBorder(null);
        historicoJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        historicoJTable.setModel(new javax.swing.table.DefaultTableModel(
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
        historicoJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        historicoJTable.setFocusCycleRoot(true);
        historicoJTable.setSurrendersFocusOnKeystroke(true);
        historicoJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                historicoJTableFocusGained();
            }
        });
        historicoJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                historicoJTableMouseClicked();
            }
        });
        historicoJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                historicoJTableMouseDragged();
            }
        });
        historicoJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                historicoJTableKeyTyped();
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                historicoJTableKeyPressed();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                historicoJTableKeyReleased();
            }
        });


        historicoJScrollPane.setViewportView(historicoJTable);

        datosHistoricoJPanel.add(historicoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 460, 430));

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

        datosHistoricoJPanel.add(apunteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 460, 90));

        modHistoricoJButton.setFont(new java.awt.Font("MS Sans Serif", 1, 10));
        modHistoricoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modHistoricoJButtonActionPerformed();
            }
        });

        datosHistoricoJPanel.add(modHistoricoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 100, 20, 20));

        nuevoHistoricoJButton.setFont(new java.awt.Font("MS Sans Serif", 1, 10));
        nuevoHistoricoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoHistoricoJButtonActionPerformed();
            }
        });

        datosHistoricoJPanel.add(nuevoHistoricoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, 20, 20));

        borrarHistoricoJButton.setFont(new java.awt.Font("MS Sans Serif", 1, 10));
        borrarHistoricoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarHistoricoJButtonActionPerformed();
            }
        });

        datosHistoricoJPanel.add(borrarHistoricoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 170, 20, 20));

        generarFichaHistoricoJButton.setFont(new java.awt.Font("MS Sans Serif", 1, 10));
        generarFichaHistoricoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarFichaHistoricoJButtonActionPerformed();
            }
        });

        datosHistoricoJPanel.add(generarFichaHistoricoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 210, 20, 20));

        historicoJPanel.add(datosHistoricoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 523, 579));

        templateJPanel.add(obraMayorJTabbedPane, BorderLayout.WEST);
        botoneraJPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });
        aceptarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(aceptarJButton);
        
        publicarJButton.setText("Publicar");
        publicarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publicarJButtonActionPerformed();
            }
        });
        publicarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(publicarJButton);

        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        jButtonGenerarFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                        generarFicha();
                    }
        });
        botoneraJPanel.add(jButtonGenerarFicha);
        jButtonGenerarFicha.setPreferredSize(new Dimension(120,30));

        jButtonWorkFlow.addActionListener(new java.awt.event.ActionListener() {
                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                              verWorkFlow();
                          }
        });
        jButtonWorkFlow.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(jButtonWorkFlow);

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

    private void generarFichaHistoricoJButtonActionPerformed() {//GEN-FIRST:event_generarFichaHistoricoJButtonActionPerformed

        if ((_vHistorico == null) || (_expediente == null) || (_solicitud == null))  return;

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
            CUtilidadesComponentes.generarFichaHistorico(this.desktop, _vHistorico, _expediente, _solicitud,
                    selectedFile,literales);
        }
    }//GEN-LAST:event_generarFichaHistoricoJButtonActionPerformed

    private void historicoJTableFocusGained() {//GEN-FIRST:event_historicoJTableFocusGained

        mostrarHistoricoSeleccionado();
    }//GEN-LAST:event_historicoJTableFocusGained

    private void historicoJTableMouseDragged() {//GEN-FIRST:event_historicoJTableMouseDragged

        mostrarHistoricoSeleccionado();
    }//GEN-LAST:event_historicoJTableMouseDragged
    private void historicoJTableKeyTyped(){
        mostrarHistoricoSeleccionado();
    }
    private void historicoJTableKeyPressed() {
        mostrarHistoricoSeleccionado();
    }
    private void historicoJTableKeyReleased() {
        mostrarHistoricoSeleccionado();
    }


    private void eventosJTableMouseDragged() {//GEN-FIRST:event_eventosJTableMouseDragged

        mostrarEventoSeleccionado();
    }//GEN-LAST:event_eventosJTableMouseDragged

    private void eventosJTableFocusGained() {//GEN-FIRST:event_eventosJTableFocusGained

        mostrarEventoSeleccionado();
    }//GEN-LAST:event_eventosJTableFocusGained
    private void eventosJTableKeyTyped(){
        mostrarEventoSeleccionado();
    }
    private void eventosJTableKeyPressed() {
        mostrarEventoSeleccionado();
    }
    private void eventosJTableKeyReleased() {
        mostrarEventoSeleccionado();
    }


    private void notificacionesJTableFocusGained() {//GEN-FIRST:event_notificacionesJTableFocusGained

        mostrarDatosNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableFocusGained

    private void notificacionesJTableMouseDragged() {//GEN-FIRST:event_notificacionesJTableMouseDragged

        mostrarDatosNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableMouseDragged
    private void notificacionesJTableKeyTyped(){
        mostrarDatosNotificacionSeleccionada();
    }
    private void notificacionesJTableKeyPressed() {
        mostrarDatosNotificacionSeleccionada();
    }
    private void notificacionesJTableKeyReleased() {
        mostrarDatosNotificacionSeleccionada();
    }


    private void okJButtonActionPerformed() {//GEN-FIRST:event_okJButtonActionPerformed

        int row = notificacionesJTable.getSelectedRow();
        if (row != -1) {
            row= ((Integer)notificacionesSorted.getValueAt(row, notificacionesHiddenCol)).intValue();
            CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(row);
            if (entregadaATField.getText().trim().equalsIgnoreCase("")){
                notificacion.setEntregadaA(null);
                entregadaATextJLabel.setText("");
            }else {
                notificacion.setEntregadaA(entregadaATField.getText().trim());
                entregadaATextJLabel.setText(entregadaATField.getText().trim());
            }
            _vNotificaciones.set(row, notificacion);
        }

    }//GEN-LAST:event_okJButtonActionPerformed


    private void borrarPromotorJButtonActionPerformed() {//GEN-FIRST:event_borrarPromotorJButtonActionPerformed

        if (CUtilidadesComponentes.hayDatosPersonaJuridicoFisica(DNIPromotorJTField.getText().trim(),
                nombrePromotorJTField.getText().trim(),
                nombreViaPromotorJTField.getText().trim(),
                numeroViaPromotorJTField.getText().trim(),
                cPostalPromotorJTField.getText().trim(),
                municipioPromotorJTField.getText().trim(),
                provinciaPromotorJTField.getText().trim())){
            /** mostramos ventana de confirmacion */
            int ok= JOptionPane.showConfirmDialog(obraMayorJPanel, literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.promotorJPanel.borrarPromotorJButton.Message"), literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.promotorJPanel.borrarPromotorJButton.tittle"), JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION){
                borrarCamposPromotor();
                if (_solicitud.getPromotor() != null){
                    _solicitud.setIdPromotorToDelete(_solicitud.getPromotor().getIdPersona());
                }
                CConstantesLicenciasActividad.promotor= null;
            }
        }else{
            borrarCamposPromotor();
            CConstantesLicenciasActividad.promotor= null;
        }
    }//GEN-LAST:event_borrarPromotorJButtonActionPerformed

    private void borrarTecnicoJButtonActionPerformed() {//GEN-FIRST:event_borrarTecnicoJButtonActionPerformed

        if (CUtilidadesComponentes.hayDatosPersonaJuridicoFisica(DNITecnicoJTField.getText().trim(),
                nombreTecnicoJTField.getText().trim(),
                nombreViaTecnicoJTField.getText().trim(),
                numeroViaTecnicoJTField.getText().trim(),
                cPostalTecnicoJTField.getText().trim(),
                municipioTecnicoJTField.getText().trim(),
                provinciaTecnicoJTField.getText().trim())){
            /** mostramos ventana de confirmacion */
            int ok= JOptionPane.showConfirmDialog(obraMayorJPanel, literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.borrarTecnicoJButton.Message"), literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.borrarTecnicoJButton.tittle"), JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION){
                borrarCamposTecnico();
                if ((_solicitud.getTecnicos() != null) && (_solicitud.getTecnicos().size() > 0)){
                    _solicitud.setIdTecnicoToDelete(((CPersonaJuridicoFisica)_solicitud.getTecnicos().get(0)).getIdPersona());
                }
                CConstantesLicenciasActividad.tecnico =  null;
            }
        }else{
            borrarCamposTecnico();
            CConstantesLicenciasActividad.tecnico =  null;
        }
    }//GEN-LAST:event_borrarTecnicoJButtonActionPerformed

    private void borrarRepresentanteJButtonActionPerformed() {//GEN-FIRST:event_borrarRepresentanteJButtonActionPerformed

        if (CUtilidadesComponentes.hayDatosPersonaJuridicoFisica(DNIRepresentanteJTField.getText().trim(),
                nombreRepresentanteJTField.getText().trim(),
                nombreViaRepresentanteJTField.getText().trim(),
                numeroViaRepresentanteJTField.getText().trim(),
                cPostalRepresentanteJTField.getText().trim(),
                municipioRepresentanteJTField.getText().trim(),
                provinciaRepresentanteJTField.getText().trim())){
            /** mostramos ventana de confirmacion */
            int ok= JOptionPane.showConfirmDialog(obraMayorJPanel, literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.representanteJPanel.borrarRepresentanteJButton.Message"), literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.representanteJPanel.borrarRepresentanteJButton.tittle"), JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION){
                borrarCamposRepresentante();
                if (_solicitud.getRepresentante() != null){
                    _solicitud.setIdRepresentanteToDelete(_solicitud.getRepresentante().getIdPersona());
                }
                CConstantesLicenciasActividad.representante= null;
            }
        }else{
            borrarCamposRepresentante();
            CConstantesLicenciasActividad.representante= null;
        }
    }//GEN-LAST:event_borrarRepresentanteJButtonActionPerformed


    private void referenciasCatastralesJTableMouseClicked() {//GEN-FIRST:event_referenciasCatastralesJTableMouseClicked

        try {

            int selectedRow = referenciasCatastralesJTable.getSelectedRow();
            logger.info("selectedRow: " + selectedRow);

            if (selectedRow != -1) {
                logger.info("referenciasCatastralesJTable.getValueAt(selectedRow, 0): " + referenciasCatastralesJTable.getValueAt(selectedRow, 0));

                CConstantesLicenciasActividad.referencia.setReferenciaCatastral((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 0));

               Object objeto=referenciasCatastralesJTableModel.getValueAt(selectedRow, 1);
                String patron=null;
                CConstantesLicenciasActividad.referencia.setTipoVia("");
                if ((objeto instanceof DomainNode) && objeto!=null)
                {
                    CConstantesLicenciasActividad.referencia.setTipoVia(((DomainNode)objeto).getTerm(literales.getLocale().toString()));
                    patron=((DomainNode)objeto).getPatron();
                }
                if ((objeto instanceof String) && objeto!=null)
                {
                    if (((String)objeto).length()>0)
                    {
                        CConstantesLicenciasActividad.referencia.setTipoVia(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto).getTerm(literales.getLocale().toString()));
                        patron=((String)objeto);
                    }
                }
                CConstantesLicenciasActividad.referencia.setNombreVia((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 2));
                CConstantesLicenciasActividad.referencia.setPrimerNumero((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 3));
                CConstantesLicenciasActividad.referencia.setPrimeraLetra((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 4));
                CConstantesLicenciasActividad.referencia.setBloque((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 5));
                CConstantesLicenciasActividad.referencia.setEscalera((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 6));
                CConstantesLicenciasActividad.referencia.setPlanta((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 7));
                CConstantesLicenciasActividad.referencia.setPuerta((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 8));
                CConstantesLicenciasActividad.referencia.setCPostal((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 9));
                CUtilidadesComponentes.showDatosReferenciaCatastralDialog(desktop, true, literales);

                if (CConstantesLicencias.esDireccionEmplazamiento){
                    /** No es necesario debido a que luego no se hace nada con el valor de refCatastralJTextField */
                    //refCatastralJTextField.setText((String)referenciasCatastralesJTable.getValueAt(selectedRow, 0));

                    // Si ha sido chequeado actualizamos direccion emplazamiento
                    tipoViaINEEJCBox.setSelectedPatron(patron);

                    nombreViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 2));
                    numeroViaNumberTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 3));
                    portalViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 5));
                    plantaViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 7));
                    letraViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 4));
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



	private void deleteParcelaJButtonActionPerformed() {//GEN-FIRST:event_deleteParcelaJButtonActionPerformed

		int selectedRow = referenciasCatastralesJTable.getSelectedRow();
		logger.info("selectedRow: " + selectedRow);

		if (selectedRow != -1) {

            referenciasCatastralesJTableModel.removeRow(selectedRow);
			refreshFeatureSelection();
		}
	}//GEN-LAST:event_deleteParcelaJButtonActionPerformed

	private void tableToMapJButtonActionPerformed() {//GEN-FIRST:event_tableToMapJButtonActionPerformed

		refreshFeatureSelection();
	}//GEN-LAST:event_tableToMapJButtonActionPerformed

	private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
		CConstantesLicencias.helpSetHomeID = "licenciasIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);
        desbloquearExpediente();

	}//GEN-LAST:event_formInternalFrameClosed
    public void desbloquearExpediente(){
        if ((_expediente != null) && (_expediente.bloqueaUsuario())){
            //COperacionesLicencias.bloquearExpediente(_numExpediente, false);
            COperacionesLicencias.bloquearExpediente(_expediente.getNumExpediente(), false);
        }
    }
	private void notificacionesJTableMouseClicked() {//GEN-FIRST:event_notificacionesJTableMouseClicked
	       mostrarDatosNotificacionSeleccionada();
	}//GEN-LAST:event_notificacionesJTableMouseClicked

	private void eventosJTableMouseClicked() {//GEN-FIRST:event_eventosJTableMouseClicked
	       mostrarEventoSeleccionado();
	}//GEN-LAST:event_eventosJTableMouseClicked

    private void borrarHistoricoJButtonActionPerformed() {//GEN-FIRST:event_borrarHistoricoJButtonActionPerformed
        int row = historicoJTable.getSelectedRow();
        if (row != -1) {

            int posDeleted= ((Integer)historicoSorted.getValueAt(row, historicoHiddenCol)).intValue();
            CHistorico historico= (CHistorico)vAuxiliar.get(posDeleted);

            if (historico != null){
                if ((historico.getIdHistorico() != -1) && (historico.getSistema().equals("1"))){
                    /** no se puede borrar un historico de sistema */
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoTab.mensaje1"));
                    return;
                }

                int ok= JOptionPane.showConfirmDialog(this, literales.getString("Licencias.confirmarBorrado"), literales.getString("Licencias.tittle"), JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.NO_OPTION) return;

                /** Eliminamos del vAuxiliar la entrada, respetando el orden */
                Vector v= new Vector();
                for (int i=0; i < vAuxiliar.size(); i++){
                    if (i != posDeleted){
                        v.add((CHistorico)vAuxiliar.get(i));
                    }
                }
                /** actualizamos las posiciones de la columna hidden */
                for (int i=0 ; i<historicoSorted.getRowCount(); i++){
                    int pos= ((Integer)historicoSorted.getValueAt(i, historicoHiddenCol)).intValue();
                    if (pos > posDeleted){
                        historicoSorted.setValueAt(new Integer(pos-1), i, historicoHiddenCol);
                    }
                }

                vAuxiliar= new Vector();
                vAuxiliar= v;

                if (historico.getIdHistorico() == -1){
                    /** se borra un historico que ha sido insertado -> no hacemos nada */

                }else if (!historico.getSistema().equals("1")) {
                        /** se borra un historico que ya existia -> marcamos como borrado. */
                        /** Eliminamos del vAuxiliar la entrada, respetando el orden */
                        /** Annadimos a un vector auxiliar de historicos marcados como borrados */
                        historico.setHasBeen(CConstantesLicencias.CMD_HISTORICO_DELETED);
                        vAuxiliarDeleted.add(historico);
                }
            }
            _historicoExpedienteTableModel.removeRow(posDeleted);

            apunteJTArea.setText("");
        }
    }//GEN-LAST:event_borrarHistoricoJButtonActionPerformed



	private void historicoJTableMouseClicked() {//GEN-FIRST:event_historicoJTableMouseClicked
		   mostrarHistoricoSeleccionado();
	}//GEN-LAST:event_historicoJTableMouseClicked

	private void nuevoHistoricoJButtonActionPerformed() {//GEN-FIRST:event_nuevoHistoricoJButtonActionPerformed
	    CHistorico nuevo= new CHistorico();
        nuevo.setIdHistorico(-1);
        nuevo.setSolicitud(_solicitud);
        nuevo.setExpediente(_expediente);
        nuevo.setEstado(_expediente.getEstado());
        nuevo.setUsuario(CConstantesLicenciasActividad.principal.getName());
        nuevo.setFechaHistorico(CUtilidades.getDate(CUtilidades.showToday()));
        nuevo.setApunte("");
        nuevo.setSistema("0");
        nuevo.setHasBeen(CConstantesLicencias.CMD_HISTORICO_ADDED);

		CConstantesLicencias.OPERACION_HISTORICO= CConstantesLicencias.CMD_HISTORICO_ADDED;
		CHistoricoJDialog historicoJDialog= CUtilidadesComponentes.showHistoricoDialog(desktop, nuevo, literales);

        CHistorico historico= historicoJDialog.getHistorico();
		/** comprobamos que operacion se ha llevado a cabo con el historico */
		if (historico.getHasBeen() != -1) {
			_historicoExpedienteTableModel.addRow(new Object[]{((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                                                               historico.getUsuario(),
                                                               CUtilidades.formatFecha(historico.getFechaHistorico()),
                                                               historico.getApunte(),
                                                               new Integer(vAuxiliar.size())});
			// seleccionamos el nuevo historico insertado
			historicoJTable.addRowSelectionInterval(_historicoExpedienteTableModel.getRowCount() - 1, _historicoExpedienteTableModel.getRowCount() - 1);
			apunteJTArea.setText(historico.getApunte());

            /** Annadimos en el vector auxiliar */
            nuevo.setApunte(historico.getApunte());
            vAuxiliar.add(vAuxiliar.size(), nuevo);
		}
	}//GEN-LAST:event_nuevoHistoricoJButtonActionPerformed

	private void modHistoricoJButtonActionPerformed() {//GEN-FIRST:event_modHistoricoJButtonActionPerformed
		int row = historicoJTable.getSelectedRow();
        if (row != -1) {
            row= ((Integer)historicoSorted.getValueAt(row, historicoHiddenCol)).intValue();
            CHistorico historico= (CHistorico)vAuxiliar.get(row);
            if (historico != null){
                long idHistorico= historico.getIdHistorico();
                if (idHistorico == -1){
                    /** se modifica un historico nuevo */
                }else{
                    if (!historico.getSistema().equals("1")) {
                    } else {
                        mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoTab.mensaje2"));
                        return;
                    }
                }

            }

            CConstantesLicencias.OPERACION_HISTORICO= CConstantesLicencias.CMD_HISTORICO_MODIFIED;
            CHistoricoJDialog historicoJDialog= CUtilidadesComponentes.showHistoricoDialog(desktop, historico, literales);
            CHistorico h= historicoJDialog.getHistorico();
            _historicoExpedienteTableModel.setValueAt(h.getApunte(), row, 3);
            apunteJTArea.setText(h.getApunte());

            /** Annadimos el historico modificado */
            vAuxiliar.removeElementAt(row);
            historico.setApunte(h.getApunte());
            vAuxiliar.add(row, historico);
        }
	}//GEN-LAST:event_modHistoricoJButtonActionPerformed

	private void mapToTableJButtonActionPerformed() {
      	Object[] options = {literales.getString("Licencias.mensaje3"), literales.getString("Licencias.mensaje4")};
	    if (JOptionPane.showOptionDialog(this,
				literales.getString("Licencias.mensaje1"),
				literales.getString("Licencias.mensaje2"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, //don't use a custom Icon
				options, //the titles of buttons
				options[1])!=JOptionPane.OK_OPTION) return;

		CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
		Collection collection = MainActividadLicencias.geopistaEditor.getSelection();
		Iterator it = collection.iterator();
		CConstantesLicencias.referenciasCatastrales.clear();
		while (it.hasNext()) {
			Feature feature = (Feature) it.next();
			if (feature == null) {
				logger.error("feature: " + feature);
				continue;
			}

            CReferenciaCatastral referenciaCatastral = CUtilidadesComponentes.getReferenciaCatastral(feature);
            Object[] features= new Object[1];
            features[0]= feature;
			referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
																  referenciaCatastral.getTipoVia(),
																  referenciaCatastral.getNombreVia(),
																  referenciaCatastral.getPrimerNumero(),
																  referenciaCatastral.getPrimeraLetra(),
																  referenciaCatastral.getBloque(),
																  referenciaCatastral.getEscalera(),
																  referenciaCatastral.getPlanta(),
																  referenciaCatastral.getPuerta(), referenciaCatastral.getCPostal(), features});
		}


	}//GEN-LAST:event_mapToTableJButtonActionPerformed


	private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed
        CUtilidadesComponentes.showSearchDialog(desktop, new Integer(CConstantesLicencias.ObraMenor).toString(), "-1", "-1", "", true, literales);

        if ((numExpedienteJTField.getText() != null) && (numExpedienteJTField.getText().trim().length() > 0)){
            if ((CConstantesLicencias.searchValue != null) && (CConstantesLicencias.searchValue.trim().length() > 0)){
                // se ha aceptado en el dialogo de busqueda
                numExpedienteJTField.setText(CConstantesLicencias.searchValue);
            }
        }else{
            numExpedienteJTField.setText(CConstantesLicencias.searchValue);
        }

		consultarJButtonActionPerformed();

	}//GEN-LAST:event_buscarExpedienteJButtonActionPerformed

	public void consultarJButtonActionPerformed() {//GEN-FIRST:event_consultarJButtonActionPerformed

		if ((numExpedienteJTField.getText() == null) || (numExpedienteJTField.getText().trim().equals(""))) {
			return;
		}

        if (CConstantesLicencias.searchCanceled){
            CConstantesLicencias.searchCanceled= false;
            return;
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		_numExpediente= numExpedienteJTField.getText().trim();

        CExpedienteLicencia expedienteAnterior= _expediente;
        CSolicitudLicencia solicitudAnterior= _solicitud;

		/** Datos de la solicitud y del expediente seleccionado */
        Vector tiposLicencia= new Vector();
        CTipoLicencia tipoLicencia= new CTipoLicencia(new Integer(CConstantesLicencias.LicenciasActividad).intValue(), "", "");
        tiposLicencia.add(tipoLicencia);
        tipoLicencia= new CTipoLicencia(new Integer(CConstantesLicencias.LicenciasActividadNoCalificada).intValue(), "", "");
        tiposLicencia.add(tipoLicencia);
        
		CResultadoOperacion ro = COperacionesLicencias.getExpedienteLicencia(_numExpediente, literales.getLocale().toString(), tiposLicencia);

		if (ro != null) {
			if ((ro.getSolicitudes() != null) && (ro.getExpedientes() != null)) {
				_consultaOK = true;
				_solicitud = (CSolicitudLicencia) ro.getSolicitudes().get(0);
				_expediente = (CExpedienteLicencia) ro.getExpedientes().get(0);

                CConstantesLicenciasActividad.persona= null;
                CConstantesLicenciasActividad.representante= null;
                CConstantesLicenciasActividad.tecnico= null;
                CConstantesLicenciasActividad.promotor= null;


				if ((_solicitud != null) && (_expediente != null)) {
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    if (_expediente.bloqueado()){
                        /** Comprobamos si ya esta bloqueado por el usuario */
                        if ((expedienteAnterior == null) || ((expedienteAnterior != null) && (expedienteAnterior.getNumExpediente() != null) && (!expedienteAnterior.getNumExpediente().equalsIgnoreCase(_numExpediente)))){
                            /** Expediente bloqueado por otro usuario */
                            if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, literales, fromMenu) != 0){

                                _expediente= expedienteAnterior;
                                _solicitud= solicitudAnterior;

                                if (_expediente != null){
                                    numExpedienteJTField.setText(_expediente.getNumExpediente());
                                }else numExpedienteJTField.setText("");

                                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                return;
                            }else{
                                if ((expedienteAnterior != null) && (expedienteAnterior.getNumExpediente() != null) && (expedienteAnterior.getNumExpediente().trim().length() > 0)){
                                    /** Desbloqueo del expediente cargado anteriormente, si ha sido bloqueado por el usuario */
                                    if (expedienteAnterior.bloqueaUsuario()){
                                        COperacionesLicencias.bloquearExpediente(expedienteAnterior.getNumExpediente(), false);
                                    }
                                }
                            }
                        }else{
                            /** Puede ocurrir:
                             * 1.- Expediente bloqueado por propio usuario
                             * 2.- El usuario ha vuelto a abrir el mismo expediente anterior bloqueado por otro usuario */
                            if (expedienteAnterior.bloqueaUsuario()){
                                /** caso 1*/
                                /** expedienteAnterior seguira bloqueado por el usuario, cuando le asignemos _expediente */
                                _expediente.setBloqueaUsuario(true);
                            }else{
                                /** caso 2 */
                                CUtilidadesComponentes.mostrarMensajeBloqueoAceptacion(this, literales.getString("Licencias.mensaje12"), literales);
                            }
                        }
                    }else{
                        if ((expedienteAnterior != null) && (expedienteAnterior.getNumExpediente() != null) && (expedienteAnterior.getNumExpediente().trim().length() > 0)){
                            /** Desbloqueo del expediente cargado anteriormente*/
                            if (expedienteAnterior.bloqueaUsuario()){
                                COperacionesLicencias.bloquearExpediente(expedienteAnterior.getNumExpediente(), false);
                            }
                        }
                        /** Bloqueo de expediente */
                        COperacionesLicencias.bloquearExpediente(_numExpediente, true);
                        _expediente.setBloqueaUsuario(true);
                    }

                    /** Si seguimos buscando numExpediente, navegamos desde la propia pantalla */
                    fromMenu= true;

					/** Estados de la Solicitud */
					_hEstados = cargarEstadosHash(_expediente.getEstado().getIdEstado());

					/** Borramos la pantalla */
					clearScreen();
					/** Expediente */
					rellenarExpediente();
					/** Solicitud */
					rellenarSolicitud();
                    /** Documentacion */
                    documentacionJPanel.load(_expediente, _solicitud, true);
                    /** Informes */
                    jPanelInformes.setInformes(_expediente.getInformes(),_solicitud.getIdSolicitud());
                    /** Resolucion */
                    jPanelResolucion.load(_expediente.getResolucion());
                    jPanelResolucion.setEnabled(_expediente.getEstado().getIdEstado()==com.geopista.protocol.CConstantesComando.ESTADO_EMISION_PROPUESTA_RESOLUCION);
                    jLabelEstadoActual.setText(((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(_expediente.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()));

                    /** Titular */
					rellenarPropietario();
					/** Representado */
					rellenarRepresentante();
					/** Tecnico */
					rellenarTecnico();
					/** Promotor */
					rellenarPromotor();
					/** Notificaciones */
					rellenarNotificaciones();
					clearDatosNotificacionSelected();
					/** Eventos */
					rellenarEventos();
					/** Historico */
					rellenarHistorico();

					com.geopista.app.licencias.CUtilidadesComponentes.checkEstadosCombo(_expediente,estadoExpedienteJCBox,
                                _solicitud.getTipoLicencia().getIdTipolicencia());

					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

				} else {
					logger.warn("No existe solicitud para el expediente " + _numExpediente);
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.mensaje1"));
					clearScreen();
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}
			} else {
				_consultaOK = false;
				logger.warn("No existen resultados de operacion para el expediente " + _numExpediente);
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.mensaje1"));
				clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}
		} else {
			_consultaOK = false;
			logger.warn("No existen resultados de operacion para el expediente " + _numExpediente);
			mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.mensaje1"));
			clearScreen();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return;
		}

	}//GEN-LAST:event_consultarJButtonActionPerformed

	private void cancelarJButtonActionPerformed() {//GEN-FIRST:event_cancelarJButtonActionPerformed

		CConstantesLicencias.helpSetHomeID = "licenciasIntro";
        CConstantesLicenciasActividad.persona= null;
        CConstantesLicenciasActividad.representante= null;
        CConstantesLicenciasActividad.tecnico= null;
        CConstantesLicenciasActividad.promotor= null;
        /** Desbloqueo de expediente */
        desbloquearExpediente();
		this.dispose();
	}//GEN-LAST:event_cancelarJButtonActionPerformed

	private void aceptarJButtonActionPerformed() {//GEN-FIRST:event_aceptarJButtonActionPerformed
		if (_consultaOK) {
            /** Volvemos a preguntar al usuario si quiere modificar un expediente bloqueado por otro usuario */
            if (!_expediente.bloqueaUsuario()){
                if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, literales, fromMenu) != 0){
                    return;
                }
            }

			if (rellenaCamposObligatorios()) {
				/** Comprobamos los datos de entrada */

				/** Datos del propietario */
				if (datosPropietarioOK() && datosRepresentanteOK() && datosTecnicoOK() && datosPromotorOK()) {
					/** Datos de la solicitud */
					CTipoObra tipoObra = new CTipoObra(new Integer(tipoObraEJCBox.getSelectedPatron()).intValue(), "", "");

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
                        logger.warn("Tasa no válida. impuestoTextField.getText(): " + impuestoTextField.getText());
                    }



					_unidadRegistro = unidadRJTField.getText();
					_unidadTramitadora = unidadTJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_unidadRegistro, CConstantesLicencias.MaxLengthUnidad) || CUtilidades.excedeLongitud(_unidadTramitadora, CConstantesLicencias.MaxLengthUnidad)) {
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje1"));
						return;
					}

					_motivo = motivoJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_motivo, CConstantesLicencias.MaxLengthMotivo)) {
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje2"));
						return;
					}
					_asunto = asuntoJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_asunto, CConstantesLicencias.MaxLengthAsunto)) {
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje3"));
						return;
					}

					_observaciones = observacionesJTArea.getText().trim();
					if (CUtilidades.excedeLongitud(_observaciones, CConstantesLicencias.MaxLengthObservaciones)) {
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje5"));
						return;
					}

					/** Expediente */
					CEstado estado = (CEstado) _hEstados.get(new Integer(estadoExpedienteJCBox.getSelectedIndex()));
					String silencio = "0";
					if (silencioJCheckBox.isSelected()) silencio = "1";

					_servicioEncargado = servicioExpedienteJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_servicioEncargado, CConstantesLicencias.MaxLengthServicioEncargado)) {
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.ExpedienteTab.mensaje1"));
						return;
					}

					_asuntoExpediente = asuntoExpedienteJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_asuntoExpediente, CConstantesLicencias.MaxLengthAsuntoExpediente)) {
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.ExpedienteTab.mensaje2"));
						return;
					}

					/** No es editable */
					_fechaApertura = fechaAperturaJTField.getText();
					Date dateApertura = CUtilidades.parseFechaStringToDate(_fechaApertura);


					_formaInicio = inicioJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_formaInicio, CConstantesLicencias.MaxLengthFormaInicio)) {
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.ExpedienteTab.mensaje3"));
						return;
					}

					_responsableExpediente = responsableTField.getText().trim();

					_observacionesExpediente = observacionesExpedienteJTArea.getText().trim();
					if (CUtilidades.excedeLongitud(_observacionesExpediente, CConstantesLicencias.MaxLengthObservaciones)) {
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.ExpedienteTab.mensaje9"));
						return;
					}

					// Recogemos la lista de referencias catastrales de la solicitud
					Vector vRefCatastrales = new Vector();
					int numRowsRef = referenciasCatastralesJTable.getRowCount();
					for (int i = 0; i < numRowsRef; i++) {
						String referencia = (String) referenciasCatastralesJTable.getValueAt(i, 0);
						vRefCatastrales.addElement(referencia);
					}

					/** Propietario */
					// @param CViaNotificacion int idViaNotifiacion, String observacion
                    int viaNotificacionIndex = new Integer(viaNotificacionPropietarioEJCBox.getSelectedPatron()).intValue();
                    CViaNotificacion viaNotificacion= new CViaNotificacion(viaNotificacionIndex, "");
                    _tipoViaPropietario= tipoViaNotificacionPropietarioEJCBox.getSelectedPatron();

                    // @param CDatosNotificacion String dniCif, CViaNotificacion viaNotificacion, String fax, String telefono, String movil, String email, String tipoVia, String nombreVia, String numeroVia, String portal, String planta, String escalera, String letra, String cpostal, String municipio, String provincia, String notificar
					/** si no se notifica ni al representante, ni al tecnico, ni al promotor, por defecto
					 * se notifica al propietario  */
					if ((_seNotificaRepresentante == 0) && (_seNotificaTecnico == 0) && (_seNotificaPromotor == 0))
						_seNotificaPropietario = 1;
					CDatosNotificacion datosNotificacion = new CDatosNotificacion(_DNI_CIF_Propietario, viaNotificacion, _faxPropietario, _telefonoPropietario, _movilPropietario, _emailPropietario, _tipoViaPropietario, _nombreViaPropietario, _numeroViaPropietario, _portalPropietario, _plantaPropietario, _escaleraPropietario, _letraPropietario, _cPostalPropietario, _municipioPropietario, _provinciaPropietario, new Integer(_seNotificaPropietario).toString());
					// @param CPersonaJuridicoFisica String dniCif, String nombre, String apellido1, String apellido2, String colegio, String visado, String titulacion, CDatosNotificacion datosNotificacion
					CPersonaJuridicoFisica propietario = new CPersonaJuridicoFisica(_DNI_CIF_Propietario, _nombrePropietario, _apellido1Propietario, _apellido2Propietario, "", "", "", datosNotificacion);
                    if (_solicitud.getPropietario() != null){
					    propietario.setIdPersona(_solicitud.getPropietario().getIdPersona());
                    }

					/** Representante */
					CPersonaJuridicoFisica representante = new CPersonaJuridicoFisica();
                    viaNotificacionIndex = new Integer(viaNotificacionRepresentanteEJCBox.getSelectedPatron()).intValue();
                    viaNotificacion = new CViaNotificacion(viaNotificacionIndex, "");
                    _tipoViaRepresentante= tipoViaNotificacionRepresentanteEJCBox.getSelectedPatron();
                    datosNotificacion = new CDatosNotificacion(_DNI_CIF_Representante, viaNotificacion, _faxRepresentante, _telefonoRepresentante, _movilRepresentante, _emailRepresentante, _tipoViaRepresentante, _nombreViaRepresentante, _numeroViaRepresentante, _portalRepresentante, _plantaRepresentante, _escaleraRepresentante, _letraRepresentante, _cPostalRepresentante, _municipioRepresentante, _provinciaRepresentante, new Integer(_seNotificaRepresentante).toString());
                    representante = new CPersonaJuridicoFisica(_DNI_CIF_Representante, _nombreRepresentante, _apellido1Representante, _apellido2Representante, "", "", "", datosNotificacion);

                    representante= modificarRepresentante(representante);

					/** Tecnico */
                    viaNotificacionIndex = new Integer(viaNotificacionTecnicoEJCBox.getSelectedPatron()).intValue();
					viaNotificacion = new CViaNotificacion(viaNotificacionIndex, "");
                    _tipoViaTecnico= tipoViaNotificacionTecnicoEJCBox.getSelectedPatron();
					// @param CDatosNotificacion String dniCif, CViaNotificacion viaNotificacion, String fax, String telefono, String movil, String email, String tipoVia, String nombreVia, String numeroVia, String portal, String planta, String escalera, String letra, String cpostal, String municipio, String provincia, String notificar
					datosNotificacion = new CDatosNotificacion(_DNI_CIF_Tecnico, viaNotificacion, _faxTecnico, _telefonoTecnico, _movilTecnico, _emailTecnico, _tipoViaTecnico, _nombreViaTecnico, _numeroViaTecnico, _portalTecnico, _plantaTecnico, _escaleraTecnico, _letraTecnico, _cPostalTecnico, _municipioTecnico, _provinciaTecnico, new Integer(_seNotificaTecnico).toString());
					// @param CPersonaJuridicoFisica String dniCif, String nombre, String apellido1, String apellido2, String colegio, String visado, String titulacion, CDatosNotificacion datosNotificacion
					CPersonaJuridicoFisica tecnico = new CPersonaJuridicoFisica(_DNI_CIF_Tecnico, _nombreTecnico, _apellido1Tecnico, _apellido2Tecnico, _colegioTecnico, _visadoTecnico, _titulacionTecnico, datosNotificacion);
                    tecnico= modificarTecnico(tecnico);
                    Vector vTecnicos= new Vector();
                    if (tecnico != null){
                        vTecnicos.add(tecnico);
                    }

					/** Promotor */
                    viaNotificacionIndex = new Integer(viaNotificacionPromotorEJCBox.getSelectedPatron()).intValue();
					viaNotificacion = new CViaNotificacion(viaNotificacionIndex, "");
                    _tipoViaPromotor= tipoViaNotificacionPromotorEJCBox.getSelectedPatron();
					// @param CDatosNotificacion String dniCif, CViaNotificacion viaNotificacion, String fax, String telefono, String movil, String email, String tipoVia, String nombreVia, String numeroVia, String portal, String planta, String escalera, String letra, String cpostal, String municipio, String provincia, String notificar
					datosNotificacion = new CDatosNotificacion(_DNI_CIF_Promotor, viaNotificacion, _faxPromotor, _telefonoPromotor, _movilPromotor, _emailPromotor, _tipoViaPromotor, _nombreViaPromotor, _numeroViaPromotor, _portalPromotor, _plantaPromotor, _escaleraPromotor, _letraPromotor, _cPostalPromotor, _municipioPromotor, _provinciaPromotor, new Integer(_seNotificaPromotor).toString());
					// @param CPersonaJuridicoFisica String dniCif, String nombre, String apellido1, String apellido2, String colegio, String visado, String titulacion, CDatosNotificacion datosNotificacion
					CPersonaJuridicoFisica promotor = new CPersonaJuridicoFisica(_DNI_CIF_Promotor, _nombrePromotor, _apellido1Promotor, _apellido2Promotor, _colegioPromotor, _visadoPromotor, _titulacionPromotor, datosNotificacion);
					promotor= modificarPromotor(promotor);

                    /** Datos del emplazamiento */
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


					/** licencia */
					CTipoLicencia licencia = _solicitud.getTipoLicencia();

                    /** REFERENCIAS CATASTRALES */
					Vector referenciasCatastrales = new Vector();
					for (int i = 0; i < referenciasCatastralesJTable.getRowCount(); i++) {
                        /** tipoVia */
                        String tipoVia= null;
                        Object objeto=referenciasCatastralesJTableModel.getValueAt(i, 1);
                        if ((objeto instanceof DomainNode) && objeto!=null)
                        {
                            CConstantesLicenciasActividad.referencia.setTipoVia(((DomainNode)objeto).getTerm(literales.getLocale().toString()));
                            tipoVia=((DomainNode)objeto).getPatron();
                        }
                        if ((objeto instanceof String) && objeto!=null)
                        {
                            if (((String)objeto).length()>0)
                            {
                                CConstantesLicenciasActividad.referencia.setTipoVia(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto).getTerm(literales.getLocale().toString()));
                                tipoVia=((String)objeto);
                            }
                        }
						String ref_Catastral = (String) referenciasCatastralesJTable.getValueAt(i, 0);

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
                    // Recogemos la lista de documentos anexados a la solicitud
                    Vector vListaAnexos= documentacionJPanel.getAnexos();

					// @param CSolicitudLicencia CTipoLicencia tipoLicencia, CTipoObra tipoObra, CPersonaJuridicoFisica propietario, CPersonaJuridicoFisica representante, CPersonaJuridicoFisica tecnico, CPersonaJuridicoFisica promotor, String numAdministrativo, String codigoEntrada, String unidadTramitadora, String unidadDeRegistro, String motivo, String asunto, Date fecha, double tasa, String tipoViaAfecta, String nombreViaAfecta, String numeroViaAfecta, String portalAfecta, String plantaAfecta, String letraAfecta, String cpostalAfecta, String municipioAfecta, String provinciaAfecta, String observaciones, Vector anexos, Vector referenciasCatastrales
					CSolicitudLicencia solicitudLicencia = new CSolicitudLicencia(licencia, tipoObra, propietario, representante, null, promotor, null, null, _unidadTramitadora, _unidadRegistro, _motivo, _asunto, _solicitud.getFecha(), tasa, tipoViaINE, nombreViaEmplazamiento, numeroViaEmplazamiento, portalViaEmplazamiento, plantaViaEmplazamiento, letraViaEmplazamiento, cPostalEmplazamiento, municipioJTField.getText(), provinciaJTField.getText(), _observaciones, vListaAnexos, referenciasCatastrales);
					solicitudLicencia.setIdSolicitud(_solicitud.getIdSolicitud());
                    solicitudLicencia.setNumAdministrativo(_solicitud.getNumAdministrativo());
                    /** fecha limite de obra */
                    Date dateFechaLimite= CUtilidades.parseFechaStringToDate(fechaLimiteObraJTField.getText());
                    solicitudLicencia.setFechaLimiteObra(dateFechaLimite);
                    /** Tecnicos */
                    solicitudLicencia.setTecnicos(vTecnicos);
                    /** impuesto */
                    solicitudLicencia.setImpuesto(impuesto);
                    /** documentacion entregada y observaciones de  la misma */
                    solicitudLicencia.setObservacionesDocumentacionEntregada(documentacionJPanel.getObservacionesGenerales());
                    solicitudLicencia.setDocumentacionEntregada(documentacionJPanel.getDocumentacionObligatoriaSeleccionada());

                    /** Mejoras */
                    Vector vMejoras= documentacionJPanel.getMejoras();
                    solicitudLicencia.setMejoras(vMejoras);

                    /* datos de actividad */
                    solicitudLicencia.setDatosActividad(datosActividadJPanel.getDatosActividad());


					/** expediente */

					/** Tipo Tramitacion */
					CTipoTramitacion tramitacion = new CTipoTramitacion();
                    tramitacion.setIdTramitacion(new Integer(tramitacionEJCBox.getSelectedPatron()).intValue());

					/** Notificaciones */
					Vector auxNotificaciones = new Vector();
					if (_vNotificaciones != null) {
						Enumeration e = _vNotificaciones.elements();
						int row = 0;
						while (e.hasMoreElements()) {
							CNotificacion notificacion = (CNotificacion) e.nextElement();
							// Solamente actualizamos los valores que se pueden modificar
							ComboBoxRendererEstructuras renderBox = (ComboBoxRendererEstructuras) notificacionesJTable.getCellRenderer(row, 1);
							notificacionesJTable.prepareRenderer(renderBox, row, 1);
                            int index = new Integer(renderBox.getSelectedPatron()).intValue();
							CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion(index, "", "");
							notificacion.setEstadoNotificacion(estadoNotificacion);
							// si esta NOTIFICADA o PENDIENTE_ACUSE_REENVIO actualizamos la fecha
							if (notificacion.getEstadoNotificacion().getIdEstado() == CConstantesLicencias.ID_ESTADO_NOTIFICADA) {
								notificacion.setFechaNotificacion(new Timestamp(new Date().getTime()));
							} else if (notificacion.getEstadoNotificacion().getIdEstado() == CConstantesLicencias.ID_ESTADO_PENDIENTE_REENVIO) {
								notificacion.setFecha_reenvio(new Timestamp(new Date().getTime()));
							}

							auxNotificaciones.add(notificacion);
							row++;
						}
					}

					/** Eventos */
					Vector auxEventos = new Vector();
					if (_vEventos != null) {
						Enumeration e = _vEventos.elements();
						int row = 0;
						while (e.hasMoreElements()) {
							CEvento evento = (CEvento) e.nextElement();
							CheckBoxRenderer renderCheck = (CheckBoxRenderer) eventosJTable.getCellRenderer(row, 2);
							eventosJTable.prepareRenderer(renderCheck, row, 2);
							if (renderCheck.isSelected()) {
								// actualizamos el campo revisado
								evento.setRevisado("1");

								// actualizamos el campo revisado_por
								/*
								TextFieldRenderer renderText = (TextFieldRenderer) eventosJTable.getCellRenderer(row, 3);
								eventosJTable.prepareRenderer(renderText, row, 3);
								String revisadoPor= renderText.getText();
								*/
								String revisadoPor = CConstantesLicenciasActividad.principal.getName();
								evento.setRevisadoPor(revisadoPor);
							} else {
								evento.setRevisado("0");
								evento.setRevisadoPor("");
							}

							auxEventos.add(evento);
							row++;
						}
					}

                    /** Historico */
                    /* regeneramos el vector _vHistorico con los nuevos, los marcados como borrados y los marcados como modificados */
                    _vHistorico = new Vector();
                    if ((vAuxiliarDeleted != null) && (vAuxiliarDeleted.size() > 0)){
                         _vHistorico= vAuxiliarDeleted;
                    }
                    for (Enumeration e= vAuxiliar.elements(); e.hasMoreElements();){
                        CHistorico historico= (CHistorico)e.nextElement();
                        _vHistorico.add(historico);
                    }

					CExpedienteLicencia expediente = new CExpedienteLicencia(_numExpediente, _expediente.getIdSolicitud(), tramitacion, null, _servicioEncargado, _asuntoExpediente, silencio, _formaInicio, -1, dateApertura, _responsableExpediente, null, "-1", -1, _observacionesExpediente, estado, auxNotificaciones);
					expediente.setVU(_vu);
					expediente.setEventos(auxEventos);
					expediente.setHistorico(_vHistorico);

                    /* Alegacion */
                    Alegacion alegacion= _expediente.getAlegacion();
                    if (alegacion != null){
                        Vector vListaAnexosAlegacion= documentacionJPanel.getAnexosAlegacion();
                        alegacion.setAnexos(vListaAnexosAlegacion);
                        expediente.setAlegacion(alegacion);
                    }
                    //informes.
                    expediente.setInformes(jPanelInformes.getInformes());
                    //resolucion
                    expediente.setResolucion(jPanelResolucion.save());

                    /** cnae */
                    expediente.setCNAE(cnaeTField.getText().trim());
                    
					CResultadoOperacion resultado = COperacionesLicencias.modificarExpediente(solicitudLicencia, expediente);

					/** si ha ido bien, los anexos han sido actualizados.
					 * Como no cerramos la pantalla, es necesario recargar los nuevos anexos del expediente */
					if (resultado.getResultado()) {
                        /* El expediente ha sido modificado */
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.Message1"));
                        CUtilidadesComponentes.addFeatureCapa(MainActividadLicencias.geopistaEditor, "licencias_actividad", referenciasCatastralesJTable, referenciasCatastralesJTableModel);
                        CConstantesLicenciasActividad.persona= null;
                        CConstantesLicenciasActividad.representante= null;
                        CConstantesLicenciasActividad.tecnico= null;
                        CConstantesLicenciasActividad.promotor= null;
						consultarJButtonActionPerformed();
					} else{
                        /** Comprobamos que no se haya excedido el maximo FileUploadBase.SizeLimitExceededException */
                       if (resultado.getDescripcion().equalsIgnoreCase("FileUploadBase.SizeLimitExceededException")){
                           JOptionPane optionPane= new JOptionPane(literales.getString("AnexosJPanel.Message1"), JOptionPane.ERROR_MESSAGE);
                           JDialog dialog =optionPane.createDialog(this,"ERROR");
                           dialog.show();
                       }else{
                         JOptionPane optionPane= new JOptionPane(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.SolicitudTab.mensaje21") +"\n"+resultado.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                         JDialog dialog =optionPane.createDialog(this,"ERROR");
                         dialog.show();
                       }
                    }
				}
			} else {
				mostrarMensaje(literales.getString("datosSolicitudJPanel.Message1"));
			}
			
			//Actualizar Estado de Expediente
			CResultadoOperacion resultado = COperacionesLicencias.actualizarEstadoExpediente(_expediente);
			if (resultado.getResultado()){
				if (resultado.getDescripcion().equalsIgnoreCase("ExpedienteNoPublicado")){
					mostrarMensaje(CMainLicencias.literales.getString("CModificaionesLicencias.ExpedienteNoPublicado"));
				}
				else{
					mostrarMensaje(resultado.getDescripcion());
				}
			}
			else{

				JOptionPane optionPane= new JOptionPane("Error al actualizar el estado del expediente en el SIGEM.\n"+resultado.getDescripcion(),JOptionPane.ERROR_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"ERROR");
				dialog.show();

			}
			
		} else {
			mostrarMensaje(literales.getString("expedienteJPanel.Message1"));
		}


	}//GEN-LAST:event_aceptarJButtonActionPerformed

	private void publicarJButtonActionPerformed() {
		
		if(_consultaOK){
			
			if (_expediente == null)
	        {
	            //mostrarMensaje("Es necesario consultar primero por un expediente.");
				mostrarMensaje(literales.getString("CActualizarIdSigemLicenciasForm.ConsultarExpediente"));
	            return;
	        }
			
			CResultadoOperacion primerResultado = COperacionesLicencias.obtenerIdSigem(_expediente);
			if (primerResultado.getResultado()) {
				//No está publicado
				if (_solicitud.getTipoLicencia() != null){
					
					_expediente.setTipoLicenciaDescripcion(CUtilidadesComponentes.obtenerTipoLicencia(_solicitud));

					if (_expediente.getEstado() != null){
						_expediente.getEstado().setDescripcion(CUtilidadesComponentes.obtenerDescripcionEstado(_expediente, _solicitud.getTipoLicencia().getIdTipolicencia()));
					}
				}
				
				CResultadoOperacion segundoResultado = COperacionesLicencias.publicarExpedienteSigem(_expediente, _solicitud);
				if (segundoResultado.getResultado()) {	
					
					CResultadoOperacion tercerResultado = COperacionesLicencias.actualizarIdSigem(_expediente);
					if (tercerResultado.getResultado()) {
		                /* El expediente ha sido modificado */
						mostrarMensaje(literales.getString("CActualizarIdSigemLicenciasForm.Message1"));
					}
					else{
						mostrarMensaje(literales.getString("CActualizarIdSigemLicenciasForm.Message2"));
					}				
				}
				else{
					mostrarMensaje(literales.getString("CActualizarIdSigemLicenciasForm.Message2"));
				}			
			}
			else{
				//Está publicado
				mostrarMensaje(literales.getString("CActualizarIdSigemLicenciasForm.ExpedientePublicado"));
	            return;
			}		
		}
		else{
			mostrarMensaje(literales.getString("expedienteJPanel.Message1"));
		}
	}
	
	private void refCatastralJButtonActionPerformed() {//GEN-FIRST:event_refCatastralJButtonActionPerformed

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		CUtilidadesComponentes.showReferenciaCatastralDialog(desktop,literales);

		try {
           	Enumeration enumerationElement = CConstantesLicencias.referenciasCatastrales.elements();
			while (enumerationElement.hasMoreElements()) {
				CReferenciaCatastral referenciaCatastral= (CReferenciaCatastral) enumerationElement.nextElement();
            	referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
																	  referenciaCatastral.getTipoVia(),
																	  referenciaCatastral.getNombreVia(),
																	  referenciaCatastral.getPrimerNumero(),
																	  referenciaCatastral.getPrimeraLetra(),
																	  referenciaCatastral.getBloque(),
																	  referenciaCatastral.getEscalera(),
																	  referenciaCatastral.getPlanta(),
																	  referenciaCatastral.getPuerta(),
                                                                      referenciaCatastral.getCPostal(), CUtilidadesComponentes.getFeatureSearched(MainActividadLicencias.geopistaEditor, referenciaCatastral.getReferenciaCatastral())});
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

	}//GEN-LAST:event_refCatastralJButtonActionPerformed


	private boolean refreshFeatureSelection() {

		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			MainActividadLicencias.geopistaEditor.getSelectionManager().clear();

			GeopistaLayer geopistaLayer = (GeopistaLayer) MainActividadLicencias.geopistaEditor.getLayerManager().getLayer("parcelas");

            for (int i=0; i<referenciasCatastralesJTable.getModel().getRowCount(); i++){
                String refCatastral= (String) referenciasCatastralesJTable.getValueAt(i,0);
         	   	Collection collection = CUtilidadesComponentes.searchByAttribute(geopistaLayer, "Referencia catastral", refCatastral);
				Iterator it = collection.iterator();
				if (it.hasNext()) {
					Feature feature = (Feature) it.next();
					MainActividadLicencias.geopistaEditor.select(geopistaLayer, feature);
				}
			}

			MainActividadLicencias.geopistaEditor.zoomToSelected();

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


	private void nombreViaJButtonActionPerformed() {//GEN-FIRST:event_nombreViaJButtonActionPerformed

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		CUtilidadesComponentes.showAddressDialog(desktop,literales);

		try {
          	Enumeration enumerationElement = CConstantesLicencias.referenciasCatastrales.elements();
			while (enumerationElement.hasMoreElements()) {
				CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) enumerationElement.nextElement();
        		referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
																	  referenciaCatastral.getTipoVia(),
																	  referenciaCatastral.getNombreVia(),
																	  referenciaCatastral.getPrimerNumero(),
																	  referenciaCatastral.getPrimeraLetra(),
																	  referenciaCatastral.getBloque(),
																	  referenciaCatastral.getEscalera(),
																	  referenciaCatastral.getPlanta(),
																	  referenciaCatastral.getPuerta(),
                                                                      referenciaCatastral.getCPostal(), CUtilidadesComponentes.getFeatureSearched(MainActividadLicencias.geopistaEditor, referenciaCatastral.getReferenciaCatastral())});
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

	}//GEN-LAST:event_nombreViaJButtonActionPerformed

	private void buscarDNIPromotorJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIPromotorJButtonActionPerformed

		CUtilidadesComponentes.showPersonaDialog(desktop,literales);
		if ((CConstantesLicenciasActividad.persona != null) && (CConstantesLicenciasActividad.persona.getDniCif() != null)) {
            DNIPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDniCif());
            nombrePromotorJTField.setText(CConstantesLicenciasActividad.persona.getNombre());
            apellido1PromotorJTField.setText(CConstantesLicenciasActividad.persona.getApellido1());
            apellido2PromotorJTField.setText(CConstantesLicenciasActividad.persona.getApellido2());
            colegioPromotorJTField.setText(CConstantesLicenciasActividad.persona.getColegio());
            visadoPromotorJTField.setText(CConstantesLicenciasActividad.persona.getVisado());
            titulacionPromotorJTField.setText(CConstantesLicenciasActividad.persona.getTitulacion());
            faxPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getFax());
            telefonoPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTelefono());
            movilPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getMovil());
            emailPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionPromotorEJCBox.setSelectedPatron(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionPromotorEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionPromotorEJCBox.setSelectedPatron(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionPromotorEJCBox.setSelected(auxNode.getIdNode());
            }


            nombreViaPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getNombreVia());
            numeroViaPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getNumeroVia());
            plantaPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getPlanta());
            portalPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getPortal());
            escaleraPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getEscalera());
            letraPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getLetra());
            try{
                cPostalPromotorJTField.setNumber(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getCpostal()));
            }catch(Exception e){cPostalPromotorJTField.setText("");}
            municipioPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getMunicipio());
            provinciaPromotorJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getProvincia());
            if (CConstantesLicenciasActividad.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarPromotorJCheckBox.setSelected(true);
            else notificarPromotorJCheckBox.setSelected(false);

            CConstantesLicenciasActividad.promotor= CConstantesLicenciasActividad.persona;

		}
        CConstantesLicenciasActividad.persona= null;

	}//GEN-LAST:event_buscarDNIPromotorJButtonActionPerformed

	private void buscarDNITecnicoJButtonActionPerformed() {//GEN-FIRST:event_buscarDNITecnicoJButtonActionPerformed

		CUtilidadesComponentes.showPersonaDialog(desktop,literales);

		if ((CConstantesLicenciasActividad.persona != null) && (CConstantesLicenciasActividad.persona.getDniCif() != null)) {
            DNITecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDniCif());
            nombreTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getNombre());
            apellido1TecnicoJTField.setText(CConstantesLicenciasActividad.persona.getApellido1());
            apellido2TecnicoJTField.setText(CConstantesLicenciasActividad.persona.getApellido2());
            colegioTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getColegio());
            visadoTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getVisado());
            titulacionTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getTitulacion());
            faxTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getFax());
            telefonoTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTelefono());
            movilTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getMovil());
            emailTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionTecnicoEJCBox.setSelectedPatron(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionTecnicoEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionTecnicoEJCBox.setSelectedPatron(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionTecnicoEJCBox.setSelected(auxNode.getIdNode());
            }
            nombreViaTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getNombreVia());
            numeroViaTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getNumeroVia());
            plantaTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getPlanta());
            portalTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getPortal());
            escaleraTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getEscalera());
            letraTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getLetra());
            try{
                cPostalTecnicoJTField.setNumber(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getCpostal()));
            }catch(Exception e){cPostalTecnicoJTField.setText("");}
            municipioTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getMunicipio());
            provinciaTecnicoJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getProvincia());
            if (CConstantesLicenciasActividad.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarTecnicoJCheckBox.setSelected(true);
            else notificarTecnicoJCheckBox.setSelected(false);

            CConstantesLicenciasActividad.tecnico= CConstantesLicenciasActividad.persona;

		}

        CConstantesLicenciasActividad.persona= null;

	}//GEN-LAST:event_buscarDNITecnicoJButtonActionPerformed

	private void buscarDNIRepresentanteJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIRepresentanteJButtonActionPerformed

		logger.info("Inicio.");
		CUtilidadesComponentes.showPersonaDialog(desktop,literales);

		if ((CConstantesLicenciasActividad.persona != null) && (CConstantesLicenciasActividad.persona.getDniCif() != null)) {

            DNIRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDniCif());
            nombreRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getNombre());
            apellido1RepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getApellido1());
            apellido2RepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getApellido2());
            faxRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getFax());
            telefonoRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTelefono());
            movilRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getMovil());
            emailRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionRepresentanteEJCBox.setSelectedPatron(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionRepresentanteEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionRepresentanteEJCBox.setSelectedPatron(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionRepresentanteEJCBox.setSelected(auxNode.getIdNode());
            }

            nombreViaRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getNombreVia());
            numeroViaRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getNumeroVia());
            plantaRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getPlanta());
            portalRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getPortal());
            escaleraRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getEscalera());
            letraRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getLetra());
            try{
                cPostalRepresentanteJTField.setNumber(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getCpostal()));
            }catch(Exception e){cPostalRepresentanteJTField.setText("");}
            municipioRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getMunicipio());
            provinciaRepresentanteJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getProvincia());
            if (CConstantesLicenciasActividad.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarRepresentanteJCheckBox.setSelected(true);
            else notificarRepresentanteJCheckBox.setSelected(false);

            CConstantesLicenciasActividad.representante= CConstantesLicenciasActividad.persona;

		}

        CConstantesLicenciasActividad.persona= null;

	}//GEN-LAST:event_buscarDNIRepresentanteJButtonActionPerformed

	private void buscarDNIPropietarioJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIPropietarioJButtonActionPerformed

		logger.info("Inicio.");

		CUtilidadesComponentes.showPersonaDialog(desktop,literales);

		if ((CConstantesLicenciasActividad.persona != null) && (CConstantesLicenciasActividad.persona.getDniCif() != null)) {
            DNIPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDniCif());
            nombrePropietarioJTField.setText(CConstantesLicenciasActividad.persona.getNombre());
            apellido1PropietarioJTField.setText(CConstantesLicenciasActividad.persona.getApellido1());
            apellido2PropietarioJTField.setText(CConstantesLicenciasActividad.persona.getApellido2());
            faxPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getFax());
            telefonoPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTelefono());
            movilPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getMovil());
            emailPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getEmail());
            try{
                tipoViaNotificacionPropietarioEJCBox.setSelectedPatron(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicenciasActividad.persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionPropietarioEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionPropietarioEJCBox.setSelectedPatron(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionPropietarioEJCBox.setSelected(auxNode.getIdNode());                
            }


            nombreViaPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getNombreVia());
            numeroViaPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getNumeroVia());
            plantaPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getPlanta());
            portalPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getPortal());
            escaleraPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getEscalera());
            letraPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getLetra());
            try{
                cPostalPropietarioJTField.setNumber(new Integer(CConstantesLicenciasActividad.persona.getDatosNotificacion().getCpostal()));
            }catch(Exception e){cPostalPropietarioJTField.setText("");}
            municipioPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getMunicipio());
            provinciaPropietarioJTField.setText(CConstantesLicenciasActividad.persona.getDatosNotificacion().getProvincia());
            /*
            if (CConstantesLicencias.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarPropietarioJCheckBox.setSelected(true);
            else notificarPropietarioJCheckBox.setSelected(false);
            */
		}

        CConstantesLicenciasActividad.persona= null;

	}//GEN-LAST:event_buscarDNIPropietarioJButtonActionPerformed



	private void DNIPromotorJTFieldKeyReleased() {//GEN-FIRST:event_DNIPromotorJTFieldKeyReleased
		if (DNIPromotorJTField.getDocument() != null) {
			if (DNIPromotorJTField.getText().trim().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Promotor = DNIPromotorJTField.getText().trim();
			} else if (DNIPromotorJTField.getText().trim().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIPromotorJTField.setText(_DNI_CIF_Promotor);
			}
		}
	}//GEN-LAST:event_DNIPromotorJTFieldKeyReleased

	private void DNIPromotorJTFieldKeyTyped() {//GEN-FIRST:event_DNIPromotorJTFieldKeyTyped
		if (DNIPromotorJTField.getDocument() != null) {
			if (DNIPromotorJTField.getText().trim().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Promotor = DNIPromotorJTField.getText().trim();
			} else if (DNIPromotorJTField.getText().trim().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIPromotorJTField.setText(_DNI_CIF_Promotor);
			}
		}
	}//GEN-LAST:event_DNIPromotorJTFieldKeyTyped

	private void DNITecnicoJTFieldKeyReleased() {//GEN-FIRST:event_DNITecnicoJTFieldKeyReleased
		if (DNITecnicoJTField.getDocument() != null) {
			if (DNITecnicoJTField.getText().trim().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Tecnico = DNITecnicoJTField.getText().trim();
			} else if (DNITecnicoJTField.getText().trim().length() > CConstantesLicencias.MaxLengthDNI) {
				DNITecnicoJTField.setText(_DNI_CIF_Tecnico);
			}
		}
	}//GEN-LAST:event_DNITecnicoJTFieldKeyReleased

	private void DNITecnicoJTFieldKeyTyped() {//GEN-FIRST:event_DNITecnicoJTFieldKeyTyped
		if (DNITecnicoJTField.getDocument() != null) {
			if (DNITecnicoJTField.getText().trim().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Tecnico = DNITecnicoJTField.getText().trim();
			} else if (DNITecnicoJTField.getText().trim().length() > CConstantesLicencias.MaxLengthDNI) {
				DNITecnicoJTField.setText(_DNI_CIF_Tecnico);
			}
		}
	}//GEN-LAST:event_DNITecnicoJTFieldKeyTyped

	private void DNIRepresentanteJTFieldKeyReleased() {//GEN-FIRST:event_DNIRepresentanteJTFieldKeyReleased
		if (DNIRepresentanteJTField.getDocument() != null) {
			if (DNIRepresentanteJTField.getText().trim().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Representante = DNIRepresentanteJTField.getText().trim();
			} else if (DNIRepresentanteJTField.getText().trim().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIRepresentanteJTField.setText(_DNI_CIF_Representante);
			}
		}
	}//GEN-LAST:event_DNIRepresentanteJTFieldKeyReleased

	private void DNIRepresentanteJTFieldKeyTyped() {//GEN-FIRST:event_DNIRepresentanteJTFieldKeyTyped

		if (DNIRepresentanteJTField.getDocument() != null) {
			if (DNIRepresentanteJTField.getText().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Representante = DNIRepresentanteJTField.getText().trim();
			} else if (DNIRepresentanteJTField.getText().trim().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIRepresentanteJTField.setText(_DNI_CIF_Representante);
			}
		}
	}//GEN-LAST:event_DNIRepresentanteJTFieldKeyTyped

	private void DNIPropietarioJTFieldKeyReleased() {//GEN-FIRST:event_DNIPropietarioJTFieldKeyReleased

		if (DNIPropietarioJTField.getDocument() != null) {
			if (DNIPropietarioJTField.getText().trim().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Propietario = DNIPropietarioJTField.getText().trim();
			} else if (DNIPropietarioJTField.getText().trim().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIPropietarioJTField.setText(_DNI_CIF_Propietario);
			}
		}
	}//GEN-LAST:event_DNIPropietarioJTFieldKeyReleased

	private void DNIPropietarioJTFieldKeyTyped() {//GEN-FIRST:event_DNIPropietarioJTFieldKeyTyped

		if (DNIPropietarioJTField.getDocument() != null) {
			if (DNIPropietarioJTField.getText().trim().length() <= CConstantesLicencias.MaxLengthDNI) {
				_DNI_CIF_Propietario = DNIPropietarioJTField.getText().trim();
			} else if (DNIPropietarioJTField.getText().trim().length() > CConstantesLicencias.MaxLengthDNI) {
				DNIPropietarioJTField.setText(_DNI_CIF_Propietario);
			}
		}
	}//GEN-LAST:event_DNIPropietarioJTFieldKeyTyped


	private void observacionesJTAreaKeyReleased() {//GEN-FIRST:event_observacionesJTAreaKeyReleased

		if (observacionesJTArea.getDocument() != null) {
			if (observacionesJTArea.getText().trim().length() <= CConstantesLicencias.MaxLengthObservaciones) {
				_observaciones = observacionesJTArea.getText().trim();
			} else if (observacionesJTArea.getText().trim().length() > CConstantesLicencias.MaxLengthObservaciones) {
				observacionesJTArea.setText(_observaciones);
			}
		}
	}//GEN-LAST:event_observacionesJTAreaKeyReleased

	private void observacionesJTAreaKeyTyped() {//GEN-FIRST:event_observacionesJTAreaKeyTyped
		if (observacionesJTArea.getDocument() != null) {
			if (observacionesJTArea.getText().trim().length() <= CConstantesLicencias.MaxLengthObservaciones) {
				_observaciones = observacionesJTArea.getText();
			} else if (observacionesJTArea.getText().trim().length() > CConstantesLicencias.MaxLengthObservaciones) {
				observacionesJTArea.setText(_observaciones);
			}
		}
	}//GEN-LAST:event_observacionesJTAreaKeyTyped


	/*******************************************************************/
	/*                         Metodos propios                         */
	/**
	 * ***************************************************************
	 */

	public boolean datosPropietarioOK() {
		try {
			if (CUtilidades.excedeLongitud(_DNI_CIF_Propietario, CConstantesLicencias.MaxLengthDNI)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje1"));
				return false;
			}
			if (CUtilidades.excedeLongitud(_nombrePropietario, CConstantesLicencias.MaxLengthNombre)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje2"));
				return false;
			}
			if (CUtilidades.excedeLongitud(_apellido1Propietario, CConstantesLicencias.MaxLengthApellido) || CUtilidades.excedeLongitud(_apellido2Propietario, CConstantesLicencias.MaxLengthApellido)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje3"));
				return false;
			}
            _faxPropietario= "";
            try{
			    _faxPropietario = faxPropietarioJTField.getNumber().toString();
            }catch(Exception e){}
            _movilPropietario= "";
            try{
			    _movilPropietario = movilPropietarioJTField.getNumber().toString();
            }catch(Exception e){}
            _telefonoPropietario= "";
            try{
    	        _telefonoPropietario = telefonoPropietarioJTField.getNumber().toString();
            }catch(Exception e){}
			_emailPropietario = emailPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_emailPropietario, CConstantesLicencias.MaxLengthEmail)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje6"));
				return false;
			}
			_nombreViaPropietario = nombreViaPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_nombreViaPropietario, CConstantesLicencias.MaxLengthNombreVia)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje8"));
				return false;
			}
            _numeroViaPropietario= "";
            try{
			    _numeroViaPropietario = numeroViaPropietarioJTField.getNumber().toString();
            }catch(Exception e){}
			_portalPropietario = portalPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_portalPropietario, CConstantesLicencias.MaxLengthPortal)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje11"));
				return false;
			}
			_plantaPropietario = plantaPropietarioJTField.getText().trim();
			if (_plantaPropietario.length() > 0) {
                if (CUtilidades.excedeLongitud(_plantaPropietario, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje13"));
                    return false;
                }
			}
			_escaleraPropietario = escaleraPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_escaleraPropietario, CConstantesLicencias.MaxLengthPlanta)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje14"));
				return false;
			}
			_letraPropietario = letraPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_letraPropietario, CConstantesLicencias.MaxLengthLetra)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje15"));
				return false;
			}
            _cPostalPropietario= "";
            try{
			    _cPostalPropietario = cPostalPropietarioJTField.getNumber().toString();
            }catch(Exception e){}
			_municipioPropietario = municipioPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_municipioPropietario, CConstantesLicencias.MaxLengthMunicipio)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje18"));
				return false;
			}

			_provinciaPropietario = provinciaPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_provinciaPropietario, CConstantesLicencias.MaxLengthProvincia)) {
				mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PropietarioTab.mensaje19"));
				return false;
			}

			if (notificarPropietarioJCheckBox.isSelected())
				_seNotificaPropietario = 1;
			else
				_seNotificaPropietario = 0;

			return true;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}


	public boolean datosRepresentanteOK() {

		try {
            /** La solicitud puede tener asignado un representante o se le puede asignar posteriormente */
			if (_DNI_CIF_Representante.trim().length() > 0) {
				if (CUtilidades.excedeLongitud(_DNI_CIF_Representante, CConstantesLicencias.MaxLengthDNI)) {
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje1"));
					return false;
				}
				if (CUtilidades.excedeLongitud(_nombreRepresentante, CConstantesLicencias.MaxLengthNombre)) {
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje2"));
					return false;
				}
				if (CUtilidades.excedeLongitud(_apellido1Representante, CConstantesLicencias.MaxLengthApellido) || CUtilidades.excedeLongitud(_apellido2Representante, CConstantesLicencias.MaxLengthApellido)) {
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje3"));
					return false;
				}
                _faxRepresentante= "";
                try{
				    _faxRepresentante = faxRepresentanteJTField.getNumber().toString();
                }catch(Exception e){}
                _movilRepresentante= "";
                try{
				    _movilRepresentante = movilRepresentanteJTField.getNumber().toString();
                }catch(Exception e){}
                _telefonoRepresentante= "";
                try{
				    _telefonoRepresentante = telefonoRepresentanteJTField.getNumber().toString();
                }catch(Exception e){}
				_emailRepresentante = emailRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_emailRepresentante, CConstantesLicencias.MaxLengthEmail)) {
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje6"));
					return false;
				}
				_nombreViaRepresentante = nombreViaRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_nombreViaRepresentante, CConstantesLicencias.MaxLengthNombreVia)) {
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje8"));
					return false;
				}
                _numeroViaRepresentante= "";
                try{
				    _numeroViaRepresentante = numeroViaRepresentanteJTField.getNumber().toString();
                }catch(Exception e){}
				_portalRepresentante = portalRepresentanteJTField.getText().trim();
				if (_portalRepresentante.length() > 0) {
					if (CUtilidades.excedeLongitud(_portalRepresentante, CConstantesLicencias.MaxLengthPortal)) {
						mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje11"));
						return false;
					}
				}
				_plantaRepresentante = plantaRepresentanteJTField.getText().trim();
				if (_plantaRepresentante.length() > 0) {
                    if (CUtilidades.excedeLongitud(_plantaRepresentante, CConstantesLicencias.MaxLengthPlanta)) {
                        mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje13"));
                        return false;
                    }
				}
				_escaleraRepresentante = escaleraRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_escaleraRepresentante, CConstantesLicencias.MaxLengthPlanta)) {
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje14"));
					return false;
				}
				_letraRepresentante = letraRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_letraRepresentante, CConstantesLicencias.MaxLengthLetra)) {
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje15"));
					return false;
				}
                _cPostalRepresentante= "";
                try{
				    _cPostalRepresentante = cPostalRepresentanteJTField.getNumber().toString();
                }catch(Exception e){}
				_municipioRepresentante = municipioRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_municipioRepresentante, CConstantesLicencias.MaxLengthMunicipio)) {
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje18"));
					return false;
				}

				_provinciaRepresentante = provinciaRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_provinciaRepresentante, CConstantesLicencias.MaxLengthProvincia)) {
					mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.RepresentanteTab.mensaje19"));
					return false;
				}

                if (notificarRepresentanteJCheckBox.isSelected())
                    _seNotificaRepresentante = 1;
                else
                    _seNotificaRepresentante = 0;

            }else{
                borrarCamposRepresentante();
            }

			return true;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}


	public boolean datosTecnicoOK() {
		try {
            /** La solicitud puede tener asignado un tecnico o se le puede asignar posteriormente */
			if (_DNI_CIF_Tecnico.trim().length() > 0) {
                if (CUtilidades.excedeLongitud(_DNI_CIF_Tecnico, CConstantesLicencias.MaxLengthDNI)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje1"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_nombreTecnico, CConstantesLicencias.MaxLengthNombre)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje2"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_apellido1Tecnico, CConstantesLicencias.MaxLengthApellido) || CUtilidades.excedeLongitud(_apellido2Tecnico, CConstantesLicencias.MaxLengthApellido)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje3"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_colegioTecnico, CConstantesLicencias.MaxLengthColegio)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje4"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_visadoTecnico, CConstantesLicencias.MaxLengthVisado)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje5"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_titulacionTecnico, CConstantesLicencias.MaxLengthTitulacion)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje6"));
                    return false;
                }
                _faxTecnico= "";
                try{
                    _faxTecnico = faxTecnicoJTField.getNumber().toString();
                }catch(Exception e){}
                _movilTecnico= "";
                try{
                    _movilTecnico = movilTecnicoJTField.getNumber().toString();
                }catch(Exception e){}
                _telefonoTecnico= "";
                try{
                    _telefonoTecnico = telefonoTecnicoJTField.getNumber().toString();
                }catch(Exception e){}
                _emailTecnico = emailTecnicoJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_emailTecnico, CConstantesLicencias.MaxLengthEmail)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje9"));
                    return false;
                }
                _nombreViaTecnico = nombreViaTecnicoJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_nombreViaTecnico, CConstantesLicencias.MaxLengthNombreVia)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje11"));
                    return false;
                }
                _numeroViaTecnico= "";
                try{
                    _numeroViaTecnico = numeroViaTecnicoJTField.getNumber().toString();
                }catch(Exception e){}
                _portalTecnico = portalTecnicoJTField.getText().trim();
                if (_portalTecnico.length() > 0) {
                    if (CUtilidades.excedeLongitud(_portalTecnico, CConstantesLicencias.MaxLengthPortal)) {
                        mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje14"));
                        return false;
                    }
                }
                _plantaTecnico = plantaTecnicoJTField.getText().trim();
                if (_plantaTecnico.length() > 0) {
                    if (CUtilidades.excedeLongitud(_plantaTecnico, CConstantesLicencias.MaxLengthPlanta)) {
                        mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje16"));
                        return false;
                    }
                }
                _escaleraTecnico = escaleraTecnicoJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_escaleraTecnico, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje17"));
                    return false;
                }
                _letraTecnico = letraTecnicoJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_letraTecnico, CConstantesLicencias.MaxLengthLetra)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje18"));
                    return false;
                }
                _cPostalTecnico= "";
                try{
                    _cPostalTecnico = cPostalTecnicoJTField.getNumber().toString();
                }catch(Exception e){}
                _municipioTecnico = municipioTecnicoJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_municipioTecnico, CConstantesLicencias.MaxLengthMunicipio)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje21"));
                    return false;
                }

                _provinciaTecnico = provinciaTecnicoJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_provinciaTecnico, CConstantesLicencias.MaxLengthProvincia)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.TecnicoTab.mensaje22"));
                    return false;
                }

                boolean notificar = notificarTecnicoJCheckBox.isSelected();
                if (notificar)
                    _seNotificaTecnico = 1;
                else
                    _seNotificaTecnico = 0;

            }else borrarCamposTecnico();

			return true;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}


	public boolean datosPromotorOK() {

		try {
            /** La solicitud puede tener asignado un promotor o se le puede asignar posteriormente */
			if (_DNI_CIF_Promotor.trim().length() > 0) {
                if (CUtilidades.excedeLongitud(_DNI_CIF_Promotor, CConstantesLicencias.MaxLengthDNI)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje1"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_nombrePromotor, CConstantesLicencias.MaxLengthNombre)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje2"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_apellido1Promotor, CConstantesLicencias.MaxLengthApellido) || CUtilidades.excedeLongitud(_apellido2Promotor, CConstantesLicencias.MaxLengthApellido)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje3"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_colegioPromotor, CConstantesLicencias.MaxLengthColegio)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje4"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_visadoPromotor, CConstantesLicencias.MaxLengthVisado)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje5"));
                    return false;
                }
                if (CUtilidades.excedeLongitud(_titulacionPromotor, CConstantesLicencias.MaxLengthTitulacion)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje6"));
                    return false;
                }
                _faxPromotor= "";
                try{
                    _faxPromotor = faxPromotorJTField.getNumber().toString();
                }catch(Exception e){}
                _movilPromotor= "";
                try{
                    _movilPromotor = movilPromotorJTField.getNumber().toString();
                }catch(Exception e){}
                _telefonoPromotor= "";
                try{
                    _telefonoPromotor = telefonoPromotorJTField.getNumber().toString();
                }catch(Exception e){}
                _emailPromotor = emailPromotorJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_emailPromotor, CConstantesLicencias.MaxLengthEmail)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje9"));
                    return false;
                }
                _nombreViaPromotor = nombreViaPromotorJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_nombreViaPromotor, CConstantesLicencias.MaxLengthNombreVia)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje11"));
                    return false;
                }
                _numeroViaPromotor= "";
                try{
                    _numeroViaPromotor = numeroViaPromotorJTField.getNumber().toString();
                }catch(Exception e){}
                _portalPromotor = portalPromotorJTField.getText().trim();
                if (_portalPromotor.length() > 0) {
                    if (CUtilidades.excedeLongitud(_portalPromotor, CConstantesLicencias.MaxLengthPortal)) {
                        mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje14"));
                        return false;
                    }
                }
                _plantaPromotor = plantaPromotorJTField.getText().trim();
                if (_plantaPromotor.length() > 0) {
                    if (CUtilidades.excedeLongitud(_plantaPromotor, CConstantesLicencias.MaxLengthPlanta)) {
                        mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje16"));
                        return false;
                    }
                }
                _escaleraPromotor = escaleraPromotorJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_escaleraPromotor, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje17"));
                    return false;
                }
                _letraPromotor = letraPromotorJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_letraPromotor, CConstantesLicencias.MaxLengthLetra)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje18"));
                    return false;
                }
                _cPostalPromotor= "";
                try{
                    _cPostalPromotor = cPostalPromotorJTField.getNumber().toString();
                }catch(Exception e){}
                _municipioPromotor = municipioPromotorJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_municipioPromotor, CConstantesLicencias.MaxLengthMunicipio)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje21"));
                    return false;
                }

                _provinciaPromotor = provinciaPromotorJTField.getText().trim();
                if (CUtilidades.excedeLongitud(_provinciaPromotor, CConstantesLicencias.MaxLengthProvincia)) {
                    mostrarMensaje(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.PromotorTab.mensaje22"));
                    return false;
                }

                boolean notificar = notificarPromotorJCheckBox.isSelected();
                if (notificar)
                    _seNotificaPromotor = 1;
                else
                    _seNotificaPromotor = 0;

            }else borrarCamposPromotor();

			return true;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}

	public Hashtable cargarEstadosHash(int estadoActual) {
		Hashtable h = new Hashtable();
		try {
            logger.debug("_expediente.getSilencioTriggered(): " + _expediente.getSilencioTriggered());
            
            /** Estados visibles al usuario:
            * -> estados permitidos para el usuario +
            * -> estado actual del expediente (aunque el usuario no tenga permiso, en cuyo caso no se podría modificar.)
            * */
            Vector permitidos= COperacionesLicencias.getEstadosPermitidos(_expediente, _solicitud.getTipoLicencia().getIdTipolicencia());
            if (permitidos != null) {
                Hashtable estadosPermitidos= new Hashtable();
                if (permitidos != null){
                    for (int i=0; i < permitidos.size(); i++){
                        CEstado permitido= (CEstado)permitidos.get(i);
                        estadosPermitidos.put(new Integer(permitido.getIdEstado()), "");
                    }
                }

                int i= 0;
                Enumeration e = permitidos.elements();
                while (e.hasMoreElements()) {
                    CEstado estado = (CEstado) e.nextElement();
                    h.put(new Integer(i), estado);
                    i++;
                }
                if (estadosPermitidos.get(new Integer(_expediente.getEstado().getIdEstado())) == null){
                    h.put(new Integer(i), _expediente.getEstado());
                }
            }
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
		return h;
	}

    public void rellenarEstadoExpedienteJCBox() {
        try {
            /** borramos los anteriores estados posibles*/
            estadoExpedienteJCBox.removeAllItems();
            if (_hEstados.size() > 0) {
                for (int i = 0; i < _hEstados.size(); i++) {
                    CEstado estado = (CEstado) _hEstados.get(new Integer(i));
                    DomainNode dominio= Estructuras.getListaEstados().getDomainNode(new Integer(estado.getIdEstado()).toString());
                    estadoExpedienteJCBox.addItem(dominio.getTerm(literales.getLocale().toString()));
                    if (estado.getIdEstado() == _expediente.getEstado().getIdEstado())
                        estadoExpedienteJCBox.setSelectedIndex(i);
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }




	/**
	 * Expediente
	 */
	public void rellenarExpediente() {
		try {
			/** Expediente */
			/** Estados del expediente */
            rellenarEstadoExpedienteJCBox();

			/** Tramitacion del expediente */
            tramitacionEJCBox.setSelectedPatron(new Integer(_expediente.getTipoTramitacion().getIdTramitacion()).toString());

			/** Finalizacion del expediente */
            if (_expediente.getTipoFinalizacion()!=null)
                finalizacionEJCBox.setSelectedPatron(new Integer(_expediente.getTipoFinalizacion().getIdFinalizacion()).toString());
            else
                finalizacionEJCBox.setSelectedIndex(0);

			// Actualizamos campos
			numExpedienteJTField.setText(_expediente.getNumExpediente());
			numExpedienteJTField.setEnabled(false);

			servicioExpedienteJTField.setText(_expediente.getServicioEncargado());
			asuntoExpedienteJTField.setText(_expediente.getAsunto());
			fechaAperturaJTField.setText(CUtilidades.formatFecha(_expediente.getFechaApertura()));
			fechaAperturaJTField.setEnabled(false);
			inicioJTField.setText(_expediente.getFormaInicio());
			if (_expediente.getSilencioAdministrativo().equalsIgnoreCase("1")) silencioJCheckBox.setSelected(true);
			observacionesExpedienteJTArea.setText(_expediente.getObservaciones());
			_vu = _expediente.getVU();

            if (_expediente.getResponsable() != null){
                responsableTField.setText(_expediente.getResponsable());
            }else{
                responsableTField.setText("");
            }

            /** cnae */
            if (_expediente.getCNAE() != null){
                cnaeTField.setText(_expediente.getCNAE());
            }else cnaeTField.setText("");

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


	/**
	 * Solicitud
	 */
	public void rellenarSolicitud() {
		try {
			/** Tipos de obra */
            tipoObraEJCBox.setSelectedPatron(new Integer(_solicitud.getTipoObra().getIdTipoObra()).toString());
            jCheckBoxActividadNoCalificada.setSelected(_solicitud.getTipoLicencia().getIdTipolicencia()==CConstantesLicencias.ActividadesNoCalificadas);
			/** Referencias Catastrales */
			cargarReferenciasCatastralesJTable();

			// Actualizamos campos
			numRegistroJTField.setText(_solicitud.getNumAdministrativo());
			numRegistroJTField.setEnabled(false);
			unidadTJTField.setText(_solicitud.getUnidadTramitadora());
			unidadRJTField.setText(_solicitud.getUnidadDeRegistro());
			motivoJTField.setText(_solicitud.getMotivo());
			asuntoJTField.setText(_solicitud.getAsunto());
			fechaSolicitudJTField.setText(CUtilidades.formatFecha(_solicitud.getFecha()));
			fechaSolicitudJTField.setEnabled(false);
            if (_solicitud.getFechaLimiteObra() != null){
                fechaLimiteObraJTField.setText(CUtilidades.formatFecha(_solicitud.getFechaLimiteObra()));
            }else{
                fechaLimiteObraJTField.setText("");
            }
            fechaLimiteObraJTField.setEnabled(false);

            tasaTextField.setNumber(new Double(_solicitud.getTasa()));
            impuestoTextField.setNumber(new Double(_solicitud.getImpuesto()));
			observacionesJTArea.setText(_solicitud.getObservaciones());

            /** Emplazamiento */
            if ((_solicitud.getTipoViaAfecta() != null) && (_solicitud.getTipoViaAfecta().trim().length() > 0)){
			    tipoViaINEEJCBox.setSelectedPatron(_solicitud.getTipoViaAfecta());
            }else{
                tipoViaINEEJCBox.setSelectedIndex(0);
            }
			nombreViaTField.setText(_solicitud.getNombreViaAfecta());
			numeroViaNumberTField.setText(_solicitud.getNumeroViaAfecta());
			portalViaTField.setText(_solicitud.getPortalAfecta());
			plantaViaTField.setText(_solicitud.getPlantaAfecta());
			letraViaTField.setText(_solicitud.getLetraAfecta());
            try{
                cpostalViaTField.setNumber(new Integer(_solicitud.getCpostalAfecta()));
            }catch(Exception e){cpostalViaTField.setText("");}

            /** datos de actividad */
            datosActividadJPanel.load(_solicitud.getDatosActividad());

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

	/**
	 * Propietario
	 */
	public void rellenarPropietario() {
		try {
			CPersonaJuridicoFisica propietario = _solicitud.getPropietario();
			if (propietario != null) {
                viaNotificacionPropietarioEJCBox.setSelectedPatron(new Integer(_solicitud.getPropietario().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
				DNIPropietarioJTField.setText(propietario.getDniCif());
				nombrePropietarioJTField.setText(propietario.getNombre());
				apellido1PropietarioJTField.setText(propietario.getApellido1());
				apellido2PropietarioJTField.setText(propietario.getApellido2());
				faxPropietarioJTField.setText(propietario.getDatosNotificacion().getFax());
				telefonoPropietarioJTField.setText(propietario.getDatosNotificacion().getTelefono());
				movilPropietarioJTField.setText(propietario.getDatosNotificacion().getMovil());
				emailPropietarioJTField.setText(propietario.getDatosNotificacion().getEmail());
				tipoViaNotificacionPropietarioEJCBox.setSelectedPatron(propietario.getDatosNotificacion().getTipoVia());
				nombreViaPropietarioJTField.setText(propietario.getDatosNotificacion().getNombreVia());
				numeroViaPropietarioJTField.setText(propietario.getDatosNotificacion().getNumeroVia());
				plantaPropietarioJTField.setText(propietario.getDatosNotificacion().getPlanta());
				letraPropietarioJTField.setText(propietario.getDatosNotificacion().getLetra());
				portalPropietarioJTField.setText(propietario.getDatosNotificacion().getPortal());
				escaleraPropietarioJTField.setText(propietario.getDatosNotificacion().getEscalera());
                try{
				    cPostalPropietarioJTField.setNumber(new Integer(propietario.getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalPropietarioJTField.setText("");}
				municipioPropietarioJTField.setText(propietario.getDatosNotificacion().getMunicipio());
				provinciaPropietarioJTField.setText(propietario.getDatosNotificacion().getProvincia());
				if (propietario.getDatosNotificacion().getNotificar().equals("1"))
					notificarPropietarioJCheckBox.setSelected(true);
				else
					notificarPropietarioJCheckBox.setSelected(false);
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

	/**
	 * Representante
	 */
	public void rellenarRepresentante() {
		try {
			// Actualizamos campos
			CPersonaJuridicoFisica representante = _solicitud.getRepresentante();

			if (representante != null) {
                viaNotificacionRepresentanteEJCBox.setSelectedPatron(new Integer(_solicitud.getRepresentante().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
				DNIRepresentanteJTField.setText(representante.getDniCif());
				nombreRepresentanteJTField.setText(representante.getNombre());
				apellido1RepresentanteJTField.setText(representante.getApellido1());
				apellido2RepresentanteJTField.setText(representante.getApellido2());
				faxRepresentanteJTField.setText(representante.getDatosNotificacion().getFax());
				telefonoRepresentanteJTField.setText(representante.getDatosNotificacion().getTelefono());
				movilRepresentanteJTField.setText(representante.getDatosNotificacion().getMovil());
				emailRepresentanteJTField.setText(representante.getDatosNotificacion().getEmail());
				tipoViaNotificacionRepresentanteEJCBox.setSelectedPatron(representante.getDatosNotificacion().getTipoVia());
				nombreViaRepresentanteJTField.setText(representante.getDatosNotificacion().getNombreVia());
				numeroViaRepresentanteJTField.setText(representante.getDatosNotificacion().getNumeroVia());
				plantaRepresentanteJTField.setText(representante.getDatosNotificacion().getPlanta());
				letraRepresentanteJTField.setText(representante.getDatosNotificacion().getLetra());
				portalRepresentanteJTField.setText(representante.getDatosNotificacion().getPortal());
				escaleraRepresentanteJTField.setText(representante.getDatosNotificacion().getEscalera());
                try{
				    cPostalRepresentanteJTField.setNumber(new Integer(representante.getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalRepresentanteJTField.setText("");}
				municipioRepresentanteJTField.setText(representante.getDatosNotificacion().getMunicipio());
				provinciaRepresentanteJTField.setText(representante.getDatosNotificacion().getProvincia());
				if (representante.getDatosNotificacion().getNotificar().equals("1"))
					notificarRepresentanteJCheckBox.setSelected(true);
				else
					notificarRepresentanteJCheckBox.setSelected(false);
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


    public void borrarCamposRepresentante() {
        try {
            // Actualizamos campos
            DNIRepresentanteJTField.setText("");
            nombreRepresentanteJTField.setText("");
            apellido1RepresentanteJTField.setText("");
            apellido2RepresentanteJTField.setText("");
            faxRepresentanteJTField.setText("");
            telefonoRepresentanteJTField.setText("");
            movilRepresentanteJTField.setText("");
            emailRepresentanteJTField.setText("");
            nombreViaRepresentanteJTField.setText("");
            numeroViaRepresentanteJTField.setText("");
            plantaRepresentanteJTField.setText("");
            letraRepresentanteJTField.setText("");
            portalRepresentanteJTField.setText("");
            escaleraRepresentanteJTField.setText("");
            cPostalRepresentanteJTField.setText("");
            municipioRepresentanteJTField.setText("");
            provinciaRepresentanteJTField.setText("");
            notificarRepresentanteJCheckBox.setSelected(false);
            emailRepresentanteObligatorio= false;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }

    public CPersonaJuridicoFisica modificarRepresentante(CPersonaJuridicoFisica persona){

        CPersonaJuridicoFisica representante= persona;

        if ((_solicitud.getRepresentante() != null) && (_solicitud.getIdRepresentanteToDelete() == -1)) {
            /** el expediente ya tiene representante. Puede ocurrir:
             * 1.- se ha modificado alguno de sus datos
             * 2.- no se ha borrado, pero se ha introducido a mano el DNI (por lo que suponemos que no existe en BD)
             */
            if (_solicitud.getRepresentante().getDniCif().equalsIgnoreCase(_DNI_CIF_Representante)){
                /** caso 1 */
                logger.debug("************** 0.1- Se ha modificado algun dato de ID="+_solicitud.getRepresentante().getIdPersona());
                representante.setIdPersona(_solicitud.getRepresentante().getIdPersona());
            }else{
                /** caso 2 */
                logger.debug("************** 0.2- no se ha borrado, pero se ha introducido a mano el DNI");
                representante.setIdPersona(-1);
            }
        }else if ((_solicitud.getRepresentante() != null) && (_solicitud.getIdRepresentanteToDelete() != -1)){
            /** La solicitud tiene representante y se ha borrado. Puede ocurrir:
            * 1.- se asigne un representante de BD
            * 2.- se asigne uno nuevo que no existe en BD
            * 3.- no se asigna representante
            */
            if ((CConstantesLicenciasActividad.representante != null) || (_DNI_CIF_Representante.trim().length() > 0)){
                if (CConstantesLicenciasActividad.representante != null){
                    /** caso 1 */
                   logger.debug("************** 1.- Se ha asignado un representante de BD ID="+CConstantesLicenciasActividad.representante.getIdPersona());
                   representante.setIdPersona(CConstantesLicenciasActividad.representante.getIdPersona());
                }else{
                    /** caso 2 */
                   logger.debug("************** 2.- Se ha asignado un representante que NO EXISTE EN BD");
                   representante.setIdPersona(-1);
                }
            }else{
                /** caso 3 */
                logger.debug("************** 3.- NO Se ha asignado un representante");
                representante= null;
            }
        }else if (_solicitud.getRepresentante() == null){
            /** La solicitud no tiene representante. Puede ocurrir:
             * 1.- se asigne un representante de BD
             * 2.- se asigne uno nuevo que no existe en BD
             * 3.- se quede como esta sin representante
             */
            if ((CConstantesLicenciasActividad.representante != null) || (_DNI_CIF_Representante.trim().length() > 0)){
                if (CConstantesLicenciasActividad.representante != null){
                    /** caso 1 */
                    logger.debug("************** 4.- Se ha asignado un representante de BD ID="+CConstantesLicenciasActividad.representante.getIdPersona());
                   representante.setIdPersona(CConstantesLicenciasActividad.representante.getIdPersona());
                }else{
                    /** caso 2 */
                    logger.debug("************** 5.- Se ha asignado un representante que NO EXISTE EN BD");
                   representante.setIdPersona(-1);
                }
            }else{
                /** caso 3 */
                logger.debug("************** 6.- NO Se ha asignado un representante");
                representante= null;
            }
        }

        return representante;

    }


	/**
	 * Tecnico
	 */
	public void rellenarTecnico() {
		try {
			// Actualizamos campos
            Vector tecnicos= _solicitud.getTecnicos();
			if (tecnicos != null) {
                // En obra menor solo hay un tecnico
                CPersonaJuridicoFisica tecnico= (CPersonaJuridicoFisica)tecnicos.get(0);
                if (tecnico != null){
                    viaNotificacionTecnicoEJCBox.setSelectedPatron(new Integer(tecnico.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                    DNITecnicoJTField.setText(tecnico.getDniCif());
                    nombreTecnicoJTField.setText(tecnico.getNombre());
                    apellido1TecnicoJTField.setText(tecnico.getApellido1());
                    apellido2TecnicoJTField.setText(tecnico.getApellido2());
                    colegioTecnicoJTField.setText(tecnico.getColegio());
                    visadoTecnicoJTField.setText(tecnico.getVisado());
                    titulacionTecnicoJTField.setText(tecnico.getTitulacion());
                    faxTecnicoJTField.setText(tecnico.getDatosNotificacion().getFax());
                    telefonoTecnicoJTField.setText(tecnico.getDatosNotificacion().getTelefono());
                    movilTecnicoJTField.setText(tecnico.getDatosNotificacion().getMovil());
                    emailTecnicoJTField.setText(tecnico.getDatosNotificacion().getEmail());
                    tipoViaNotificacionTecnicoEJCBox.setSelectedPatron(tecnico.getDatosNotificacion().getTipoVia());
                    nombreViaTecnicoJTField.setText(tecnico.getDatosNotificacion().getNombreVia());
                    numeroViaTecnicoJTField.setText(tecnico.getDatosNotificacion().getNumeroVia());
                    plantaTecnicoJTField.setText(tecnico.getDatosNotificacion().getPlanta());
                    letraTecnicoJTField.setText(tecnico.getDatosNotificacion().getLetra());
                    portalTecnicoJTField.setText(tecnico.getDatosNotificacion().getPortal());
                    escaleraTecnicoJTField.setText(tecnico.getDatosNotificacion().getEscalera());
                    try{
                        cPostalTecnicoJTField.setNumber(new Integer(tecnico.getDatosNotificacion().getCpostal()));
                    }catch(Exception e){cPostalTecnicoJTField.setText("");}
                    municipioTecnicoJTField.setText(tecnico.getDatosNotificacion().getMunicipio());
                    provinciaTecnicoJTField.setText(tecnico.getDatosNotificacion().getProvincia());
                    if (tecnico.getDatosNotificacion().getNotificar().equals("1"))
                        notificarTecnicoJCheckBox.setSelected(true);
                    else
                        notificarTecnicoJCheckBox.setSelected(false);
                }
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

    public void borrarCamposTecnico() {
        try {
            // Actualizamos campos
            DNITecnicoJTField.setText("");
            nombreTecnicoJTField.setText("");
            apellido1TecnicoJTField.setText("");
            apellido2TecnicoJTField.setText("");
            visadoTecnicoJTField.setText("");
            colegioTecnicoJTField.setText("");
            titulacionTecnicoJTField.setText("");
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
            emailTecnicoObligatorio= false;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }

    public CPersonaJuridicoFisica modificarTecnico(CPersonaJuridicoFisica persona){

        CPersonaJuridicoFisica tecnico= persona;

        if ((_solicitud.getTecnicos() != null) && (_solicitud.getTecnicos().size() > 0) && (_solicitud.getIdTecnicoToDelete() == -1)) {
            /** el expediente ya tiene tecnico. Puede ocurrir:
             * 1.- se ha modificado alguno de sus datos
             * 2.- no se ha borrado, pero se ha introducido a mano el DNI (por lo que suponemos que no existe en BD)
             */
            if (((CPersonaJuridicoFisica)_solicitud.getTecnicos().get(0)).getDniCif().equalsIgnoreCase(_DNI_CIF_Tecnico)){
                /** caso 1 */
                logger.debug("************** 0.1- Se ha modificado algun dato de ID="+((CPersonaJuridicoFisica)_solicitud.getTecnicos().get(0)).getIdPersona());
                tecnico.setIdPersona(((CPersonaJuridicoFisica)_solicitud.getTecnicos().get(0)).getIdPersona());
            }else{
                /** caso 2 */
                logger.debug("************** 0.2- no se ha borrado, pero se ha introducido a mano el DNI");
                tecnico.setIdPersona(-1);
            }
        }else if ((_solicitud.getTecnicos() != null) && (_solicitud.getTecnicos().size() > 0) && (_solicitud.getIdTecnicoToDelete() != -1)){
            /** La solicitud tiene tecnico y se ha borrado. Puede ocurrir:
            * 1.- se asigne un tecnico de BD
            * 2.- se asigne uno nuevo que no existe en BD
            * 3.- no se asigna tecnico
            */
            if ((CConstantesLicenciasActividad.tecnico != null) || (_DNI_CIF_Tecnico.trim().length() > 0)){
                if (CConstantesLicenciasActividad.tecnico != null){
                    /** caso 1 */
                   logger.debug("************** 1.- Se ha asignado un tecnico de BD ID="+CConstantesLicenciasActividad.tecnico.getIdPersona());
                   tecnico.setIdPersona(CConstantesLicenciasActividad.tecnico.getIdPersona());
                }else{
                    /** caso 2 */
                   logger.debug("************** 2.- Se ha asignado un tecnico que NO EXISTE EN BD");
                   tecnico.setIdPersona(-1);
                }
            }else{
                /** caso 3 */
                logger.debug("************** 3.- NO Se ha asignado un tecnico");
                tecnico= null;
            }
        }else if ((_solicitud.getTecnicos() == null) || (_solicitud.getTecnicos().size() == 0)){
            /** La solicitud no tiene tecnico. Puede ocurrir:
             * 1.- se asigne un tecnico de BD
             * 2.- se asigne uno nuevo que no existe en BD
             * 3.- se quede como esta sin tecnico
             */
            if ((CConstantesLicenciasActividad.tecnico != null) || (_DNI_CIF_Tecnico.trim().length() > 0)){
                if (CConstantesLicenciasActividad.tecnico != null){
                    /** caso 1 */
                    logger.debug("************** 4.- Se ha asignado un tecnico de BD ID="+CConstantesLicenciasActividad.tecnico.getIdPersona());
                    tecnico.setIdPersona(CConstantesLicenciasActividad.tecnico.getIdPersona());
                }else{
                    /** caso 2 */
                    logger.debug("************** 5.- Se ha asignado un tecnico que NO EXISTE EN BD");
                    tecnico.setIdPersona(-1);
                }
            }else{
                /** caso 3 */
                logger.debug("************** 6.- NO Se ha asignado un tecnico");
                tecnico= null;
            }
        }

        return tecnico;

    }




	/**
	 * Promotor
	 */
	public void rellenarPromotor() {
		try {
			// Actualizamos campos
			CPersonaJuridicoFisica promotor = _solicitud.getPromotor();
			if (promotor != null) {
                viaNotificacionPromotorEJCBox.setSelectedPatron(new Integer(_solicitud.getPromotor().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
				DNIPromotorJTField.setText(promotor.getDniCif());
				nombrePromotorJTField.setText(promotor.getNombre());
				apellido1PromotorJTField.setText(promotor.getApellido1());
				apellido2PromotorJTField.setText(promotor.getApellido2());
				colegioPromotorJTField.setText(promotor.getColegio());
				visadoPromotorJTField.setText(promotor.getVisado());
				titulacionPromotorJTField.setText(promotor.getTitulacion());
				faxPromotorJTField.setText(promotor.getDatosNotificacion().getFax());
				telefonoPromotorJTField.setText(promotor.getDatosNotificacion().getTelefono());
				movilPromotorJTField.setText(promotor.getDatosNotificacion().getMovil());
				emailPromotorJTField.setText(promotor.getDatosNotificacion().getEmail());
				tipoViaNotificacionPromotorEJCBox.setSelectedPatron(promotor.getDatosNotificacion().getTipoVia());
				nombreViaPromotorJTField.setText(promotor.getDatosNotificacion().getNombreVia());
				numeroViaPromotorJTField.setText(promotor.getDatosNotificacion().getNumeroVia());
				plantaPromotorJTField.setText(promotor.getDatosNotificacion().getPlanta());
				letraPromotorJTField.setText(promotor.getDatosNotificacion().getLetra());
				portalPromotorJTField.setText(promotor.getDatosNotificacion().getPortal());
				escaleraPromotorJTField.setText(promotor.getDatosNotificacion().getEscalera());
                try{
				    cPostalPromotorJTField.setNumber(new Integer(promotor.getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalPromotorJTField.setText("");}
				municipioPromotorJTField.setText(promotor.getDatosNotificacion().getMunicipio());
				provinciaPromotorJTField.setText(promotor.getDatosNotificacion().getProvincia());
				if (promotor.getDatosNotificacion().getNotificar().equals("1"))
					notificarPromotorJCheckBox.setSelected(true);
				else
					notificarPromotorJCheckBox.setSelected(false);
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


    public void borrarCamposPromotor() {
        try {
            // Actualizamos campos
            DNIPromotorJTField.setText("");
            nombrePromotorJTField.setText("");
            apellido1PromotorJTField.setText("");
            apellido2PromotorJTField.setText("");
            visadoPromotorJTField.setText("");
            colegioPromotorJTField.setText("");
            titulacionPromotorJTField.setText("");
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
            emailPromotorObligatorio= false;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }

    public void comboxMouseClicked() {
        notificacionesJTableMouseClicked();
    }

    public void comboxFocusGained() {
        notificacionesJTableFocusGained();
    }

    public void comboxMouseDragged() {
        notificacionesJTableMouseDragged();
    }

    public void mostrarDatosNotificacionSeleccionada(){
        int row = notificacionesJTable.getSelectedRow();
        if (row != -1) {
            row= ((Integer)notificacionesSorted.getValueAt(row, notificacionesHiddenCol)).intValue();
            CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(row);
            CPersonaJuridicoFisica persona = notificacion.getPersona();
            CDatosNotificacion datos = persona.getDatosNotificacion();
            String interesado= CUtilidades.componerCampo(persona.getApellido1(), persona.getApellido2(), persona.getNombre());
            datosNombreApellidosJTField.setText(interesado);
            datosDireccionJTField.setText(CUtilidades.componerCampo(((DomainNode)Estructuras.getListaTiposViaINE().getDomainNode(datos.getTipoVia())).getTerm(literales.getLocale().toString()),
                    datos.getNombreVia(),
                    datos.getNumeroVia()));
            datosCPostalJTField.setText(CUtilidades.componerCampo(datos.getCpostal(), datos.getMunicipio(), datos.getProvincia()));
            datosNotificarPorJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(datos.getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(literales.getLocale().toString()));

            if (notificacion.getEntregadaA() != null){
                entregadaATField.setText(notificacion.getEntregadaA());
                entregadaATextJLabel.setText(notificacion.getEntregadaA());
            }else {
                entregadaATField.setText("");
                entregadaATextJLabel.setText("");
            }
            entregadaATField.setVisible(true);
            okJButton.setVisible(true);
        }else{
            clearDatosNotificacion();
        }

    }

    public void clearDatosNotificacion(){
        datosNombreApellidosJTField.setText("");
        datosDireccionJTField.setText("");
        datosCPostalJTField.setText("");
        datosNotificarPorJTField.setText("");
        entregadaATField.setText("");
        entregadaATextJLabel.setText("");
        entregadaATField.setVisible(false);
        okJButton.setVisible(false);
    }

    public CPersonaJuridicoFisica modificarPromotor(CPersonaJuridicoFisica persona){

        CPersonaJuridicoFisica promotor= persona;

        if ((_solicitud.getPromotor() != null) && (_solicitud.getIdPromotorToDelete() == -1)) {
            /** el expediente ya tiene promotor. Puede ocurrir:
             * 1.- se ha modificado alguno de sus datos
             * 2.- no se ha borrado, pero se ha introducido a mano el DNI (por lo que suponemos que no existe en BD)
             */
            if (_solicitud.getPromotor().getDniCif().equalsIgnoreCase(_DNI_CIF_Promotor)){
                /** caso 1 */
                logger.debug("************** 0.1- Se ha modificado algun dato de ID="+_solicitud.getPromotor().getIdPersona());
                promotor.setIdPersona(_solicitud.getPromotor().getIdPersona());
            }else{
                /** caso 2 */
                logger.debug("************** 0.2- no se ha borrado, pero se ha introducido a mano el DNI");
                promotor.setIdPersona(-1);
            }
        }else if ((_solicitud.getPromotor() != null) && (_solicitud.getIdPromotorToDelete() != -1)){
            /** La solicitud tiene promotor y se ha borrado. Puede ocurrir:
            * 1.- se asigne un promotor de BD
            * 2.- se asigne uno nuevo que no existe en BD
            * 3.- no se asigna promotor
            */
            if ((CConstantesLicenciasActividad.promotor != null) || (_DNI_CIF_Promotor.trim().length() > 0)){
                if (CConstantesLicenciasActividad.promotor != null){
                    /** caso 1 */
                   logger.debug("************** 1.- Se ha asignado un tecnico de BD ID="+CConstantesLicenciasActividad.promotor.getIdPersona());
                   promotor.setIdPersona(CConstantesLicenciasActividad.promotor.getIdPersona());
                }else{
                    /** caso 2 */
                   logger.debug("************** 2.- Se ha asignado un promotor que NO EXISTE EN BD");
                   promotor.setIdPersona(-1);
                }
            }else{
                /** caso 3 */
                logger.debug("************** 3.- NO Se ha asignado un promotor");
                promotor= null;
            }
        }else if (_solicitud.getPromotor() == null){
            /** La solicitud no tiene promotor. Puede ocurrir:
             * 1.- se asigne un promotor de BD
             * 2.- se asigne uno nuevo que no existe en BD
             * 3.- se quede como esta sin promotor
             */
            if ((CConstantesLicenciasActividad.promotor != null) || (_DNI_CIF_Promotor.trim().length() > 0)){
                if (CConstantesLicenciasActividad.promotor != null){
                    /** caso 1 */
                    logger.debug("************** 4.- Se ha asignado un promotor de BD ID="+CConstantesLicenciasActividad.promotor.getIdPersona());
                    promotor.setIdPersona(CConstantesLicenciasActividad.promotor.getIdPersona());
                }else{
                    /** caso 2 */
                    logger.debug("************** 5.- Se ha asignado un promotor que NO EXISTE EN BD");
                    promotor.setIdPersona(-1);
                }
            }else{
                /** caso 3 */
                logger.debug("************** 6.- NO Se ha asignado un promotor");
                promotor= null;
            }
        }

        return promotor;

    }

	/**
	 * Notificaciones
	 */
	public void rellenarNotificaciones() {
		try {
			/*
			// Para usar el sorter JTableRoles es una JTable y model es un DefaultTableModel
			TableSorted sorter = new TableSorted(_notificacionesExpedienteTableModel); //ADDED THIS
			notificacionesJTable.setModel(sorter);
			sorter.setTableHeader(notificacionesJTable.getTableHeader()); //ADDED THIS
			*/

			// Annadimos a la tabla el editor ComboBox en la segunda columna (estado)
			int vColIndexCB = 1;
			TableColumn col0 = notificacionesJTable.getColumnModel().getColumn(vColIndexCB);

			_vNotificaciones = _expediente.getNotificaciones();
			if ((_vNotificaciones != null) && (_vNotificaciones.size() > 0)) {
				// Damos valores para la combo de la primera columna
                col0.setCellEditor(new ComboBoxTableEditor(new ComboBoxEstructuras(Estructuras.getListaEstadosNotificacion(), null, literales.getLocale().toString(), false)));
                col0.setCellRenderer(new ComboBoxRendererEstructuras(Estructuras.getListaEstadosNotificacion(), null, literales.getLocale().toString(), false));

				for (int i=0; i < _vNotificaciones.size(); i++) {
					CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(i);
                    try{
                        int tipoEstado = notificacion.getEstadoNotificacion().getIdEstado();
                        ComboBoxEstructuras combox = (ComboBoxEstructuras) ((ComboBoxTableEditor) notificacionesJTable.getCellEditor(i, 1)).getComponent();
                        combox.setSelectedPatron(new Integer(tipoEstado).toString());
                        combox.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                comboxMouseClicked();
                            }
                        });
                        combox.addFocusListener(new java.awt.event.FocusAdapter() {
                            public void focusGained(java.awt.event.FocusEvent evt) {
                                comboxFocusGained();
                            }
                        });
                        combox.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                            public void mouseDragged(java.awt.event.MouseEvent evt) {
                                comboxMouseDragged();
                            }
                        });

                        Object[] rowData = {((DomainNode)Estructuras.getListaTiposNotificacion().getDomainNode(new Integer(notificacion.getTipoNotificacion().getIdTipoNotificacion()).toString())).getTerm(literales.getLocale().toString()),
                            combox.getSelectedItem(),
                            CUtilidades.formatFechaH24(notificacion.getPlazoVencimiento()),
                            notificacion.getPersona().getDniCif(),
                            CUtilidades.formatFechaH24(notificacion.getFechaNotificacion()),
                            CUtilidades.formatFechaH24(notificacion.getFecha_reenvio()),
                            new Integer(i)};
                        _notificacionesExpedienteTableModel.addRow(rowData);

                    }catch(Exception e){
                        logger.error("ERROR al cargar la fila " +e);
                    }
				}
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


	public void clearDatosNotificacionSelected() {
		try {
			datosNombreApellidosJTField.setText("");
			datosDireccionJTField.setText("");
			datosCPostalJTField.setText("");
			datosNotificarPorJTField.setText("");
            entregadaATField.setText("");
            entregadaATField.setVisible(false);
            okJButton.setVisible(false);
            entregadaATextJLabel.setText("");
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


	/**
	 * Eventos
	 */
	public void rellenarEventos() {
		try {
			// Annadimos a la tabla el editor CheckBox en la tercera columna
			int vColIndexCheck = 2;
			TableColumn col2 = eventosJTable.getColumnModel().getColumn(vColIndexCheck);
			col2.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
			col2.setCellRenderer(new CheckBoxRenderer());

			// Annadimos a la tabla el editor Text en la cuarta columna
			int vColIndexTF = 3;
			TableColumn col3 = eventosJTable.getColumnModel().getColumn(vColIndexTF);
			col3.setCellEditor(new TextFieldTableEditor());
			col3.setCellRenderer(new TextFieldRenderer());

			_vEventos = _expediente.getEventos();
			if ((_vEventos != null) && (_vEventos.size() > 0)) {
				for (int i=0; i < _vEventos.size(); i++) {
					CEvento evento = (CEvento) _vEventos.get(i);
                    try{
                        JCheckBox check = (JCheckBox) ((CheckBoxTableEditor) eventosJTable.getCellEditor(i, 2)).getComponent();

                        if (evento.getRevisado().equalsIgnoreCase("1"))
                            check.setSelected(true);
                        else
                            check.setSelected(false);
                        _eventosExpedienteTableModel.addRow(new Object[]{new Long(evento.getIdEvento()).toString(), CUtilidades.formatFechaH24(evento.getFechaEvento()), new Boolean(check.isSelected()), evento.getRevisadoPor(), evento.getContent(), new Integer(i)});

                    }catch(Exception e){
                        logger.error("ERROR al cargar la fila "+e);
                    }
				}
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

	/**
	 * Historico
	 */
	public void rellenarHistorico() {
		try {
			// Annadimos a la tabla el editor TextField en la segunda y cuarta (usuario, apunte)
			int vCol1IndexTF = 1;
			TableColumn col2 = historicoJTable.getColumnModel().getColumn(vCol1IndexTF);
			col2.setCellEditor(new TextFieldTableEditor());
			col2.setCellRenderer(new TextFieldRenderer());

			vCol1IndexTF = 3;
			TableColumn col4 = historicoJTable.getColumnModel().getColumn(vCol1IndexTF);
			col4.setCellEditor(new TextFieldTableEditor());
			col4.setCellRenderer(new TextFieldRenderer());

            vAuxiliar= new Vector();
            vAuxiliarDeleted= new Vector();

			_vHistorico = _expediente.getHistorico();
			if ((_vHistorico != null) && (_vHistorico.size() > 0)) {

                for (int i=0; i < _vHistorico.size(); i++){
                    CHistorico historico= (CHistorico)_vHistorico.get(i);
                    try{
                        String apunte= historico.getApunte();
                        if ((historico.getSistema().equalsIgnoreCase("1")) && (historico.getGenerico() == 0)){
                            /** Componemos el apunte, de forma multilingue */
                            apunte+= " " +
                                    ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()) + ".";
                            historico.setApunte(apunte);
                        }
                        vAuxiliar.add(i, historico);

                        _historicoExpedienteTableModel.addRow(new Object[]{((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()), historico.getUsuario(), CUtilidades.formatFechaH24(historico.getFechaHistorico()), apunte, new Integer(i)});

                    }catch(Exception e){
                        logger.error("ERROR al cargar la fila "+e);
                    }
                }
			}

            /** Historico */
            nuevoHistoricoJButton.setEnabled(true);
            borrarHistoricoJButton.setEnabled(true);
            modHistoricoJButton.setEnabled(true);

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

	/**
	 * Referencias Catastrales
	 */
	public void cargarReferenciasCatastralesJTable() {
		try {

			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
			Vector referenciasCatastrales = _solicitud.getReferenciasCatastrales();
			logger.info("referenciasCatastrales: " + referenciasCatastrales);

			if ((referenciasCatastrales == null) || (referenciasCatastrales.isEmpty())) {
				logger.info("No hay referenciasCatastrales para la licencia.");
				return;
			}

			for (int i = 0; i < referenciasCatastrales.size(); i++) {
				CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) referenciasCatastrales.elementAt(i);
               	referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
																	  referenciaCatastral.getTipoVia()==null?"":referenciaCatastral.getTipoVia(),
																	  referenciaCatastral.getNombreVia(),
																	  referenciaCatastral.getPrimerNumero(),
																	  referenciaCatastral.getPrimeraLetra(),
																	  referenciaCatastral.getBloque(),
																	  referenciaCatastral.getEscalera(),
																	  referenciaCatastral.getPlanta(),
																	  referenciaCatastral.getPuerta(), referenciaCatastral.getCPostal()});


			}


			refreshFeatureSelection();
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


		} catch (Exception ex) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}

	}

	public boolean rellenaCamposObligatorios() {
		try {
			// Chequeamos que el usuario haya rellenado los campos obligatorios
			/** Comprobamos los datos del Propietario */
			// leemos los datos referentes al Propietario
			_DNI_CIF_Propietario = DNIPropietarioJTField.getText().trim();
			_nombrePropietario = nombrePropietarioJTField.getText().trim();
			_apellido1Propietario = apellido1PropietarioJTField.getText().trim();
			_apellido2Propietario = apellido2PropietarioJTField.getText().trim();

			_nombreViaPropietario = nombreViaPropietarioJTField.getText().trim();
			_numeroViaPropietario = numeroViaPropietarioJTField.getText().trim();
     	    _cPostalPropietario = cPostalPropietarioJTField.getText().trim();
			_municipioPropietario = municipioPropietarioJTField.getText().trim();
			_provinciaPropietario = provinciaPropietarioJTField.getText().trim();
			if ((_DNI_CIF_Propietario.length() == 0) || (_nombrePropietario.length() == 0)) return false;

			if ((_nombreViaPropietario.length() == 0) ||
					(_numeroViaPropietario.length() == 0) || (_cPostalPropietario.length() == 0) ||
					(_municipioPropietario.length() == 0) || (_provinciaPropietario.length() == 0))
				return false;
            /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
            if (emailPropietarioObligatorio){
                if (emailPropietarioJTField.getText().trim().length() == 0) return false;
            }


            /** Comprobamos los datos del representante */
            // leemos los datos referentes al representante
            _DNI_CIF_Representante = DNIRepresentanteJTField.getText().trim();
            _nombreRepresentante = nombreRepresentanteJTField.getText().trim();
            _apellido1Representante = apellido1RepresentanteJTField.getText().trim();
            _apellido2Representante = apellido2RepresentanteJTField.getText().trim();
            _nombreViaRepresentante = nombreViaRepresentanteJTField.getText().trim();
            _numeroViaRepresentante = numeroViaRepresentanteJTField.getText().trim();
            _cPostalRepresentante = cPostalRepresentanteJTField.getText().trim();
            _municipioRepresentante = municipioRepresentanteJTField.getText().trim();
            _provinciaRepresentante = provinciaRepresentanteJTField.getText().trim().trim();

			if ((_solicitud.getRepresentante() != null) && (_solicitud.getIdRepresentanteToDelete() == -1)){
                /** solicitud con representante y este no se ha borrado ->
                 * si ALGUN campo obligatorio no esta rellenado, error */
				if ((_DNI_CIF_Representante.length() == 0) || (_nombreRepresentante.length() == 0)) return false;
				if (    (_nombreViaRepresentante.length() == 0) ||
						(_cPostalRepresentante.length() == 0) || (_municipioRepresentante.length() == 0) ||
						(_provinciaRepresentante.length() == 0))
			    return false;
                if (emailRepresentanteObligatorio){
                    if (emailRepresentanteJTField.getText().trim().length() == 0) return false;
                }

			}else if ((_solicitud.getRepresentante() != null) && (_solicitud.getIdRepresentanteToDelete() != -1)){
                /** solicitud con representante y este se ha borrado ->
                 * si TODOS los campos obligatorios estan sin rellenar, ok
                 * si TODOS los campos obligatorios estan rellenados, ok (se ha insertado uno nuevo)
                 * si solo ALGUNO esta rellenado, error */
                if (!(((_DNI_CIF_Representante.length() == 0) && (_nombreRepresentante.length() == 0) &&
                    (_nombreViaRepresentante.length() == 0) &&
                    (_cPostalRepresentante.length() == 0) && (_municipioRepresentante.length() == 0) &&
                    (_provinciaRepresentante.length() == 0) &&
                    (!emailRepresentanteObligatorio?true:emailRepresentanteJTField.getText().trim().length() == 0)) ||
                    ((_DNI_CIF_Representante.length() != 0) && (_nombreRepresentante.length() != 0) &&
                    (_nombreViaRepresentante.length() != 0) &&
                    (_cPostalRepresentante.length() != 0) && (_municipioRepresentante.length() != 0) &&
                    (_provinciaRepresentante.length() != 0) &&
                    (!emailRepresentanteObligatorio?true:emailRepresentanteJTField.getText().trim().length() != 0)))) return false;

            }else if (_solicitud.getRepresentante() == null){
                /** solicitud sin representante ->
                 * si TODOS los campos obligatorios estan sin rellenar, ok
                 * si TODOS los campos obligatorios estan rellenados, ok (se ha insertado uno nuevo)
                 * si solo ALGUNO esta rellenado, error */
                if (!(((_DNI_CIF_Representante.length() == 0) && (_nombreRepresentante.length() == 0) &&
                    (_nombreViaRepresentante.length() == 0) &&
                    (_cPostalRepresentante.length() == 0) && (_municipioRepresentante.length() == 0) &&
                    (_provinciaRepresentante.length() == 0) &&
                    (!emailRepresentanteObligatorio?true:emailRepresentanteJTField.getText().trim().length() == 0)) ||
                    ((_DNI_CIF_Representante.length() != 0) && (_nombreRepresentante.length() != 0) &&
                    (_nombreViaRepresentante.length() != 0) &&
                    (_cPostalRepresentante.length() != 0) && (_municipioRepresentante.length() != 0) &&
                    (_provinciaRepresentante.length() != 0) &&
                    (!emailRepresentanteObligatorio?true:emailRepresentanteJTField.getText().trim().length() != 0)))) return false;
            }

			/** Comprobamos los datos del tecnico */
			// leemos los datos referentes al tecnico
			_DNI_CIF_Tecnico = DNITecnicoJTField.getText();
			_nombreTecnico = nombreTecnicoJTField.getText();
			_apellido1Tecnico = apellido1TecnicoJTField.getText();
			_apellido2Tecnico = apellido2TecnicoJTField.getText();
			_colegioTecnico = colegioTecnicoJTField.getText();
			_visadoTecnico = visadoTecnicoJTField.getText();
			_titulacionTecnico= titulacionTecnicoJTField.getText();
			_nombreViaTecnico = nombreViaTecnicoJTField.getText();
			_numeroViaTecnico = numeroViaTecnicoJTField.getText();
			_cPostalTecnico = cPostalTecnicoJTField.getText();
			_municipioTecnico = municipioTecnicoJTField.getText();
			_provinciaTecnico = provinciaTecnicoJTField.getText();

            if ((_solicitud.getTecnicos() != null) && (_solicitud.getTecnicos().size() > 0) && (_solicitud.getIdTecnicoToDelete() == -1)){
                /** solicitud con tecnico y este no se ha borrado ->
                 * si ALGUN campo obligatorio no esta rellenado, error */
                if ((_DNI_CIF_Tecnico.length() == 0) || (_nombreTecnico.length() == 0)) return false;
                if (    (_nombreViaTecnico.length() == 0) ||
                        (_numeroViaTecnico.length() == 0) ||
                        (_cPostalTecnico.length() == 0) || (_municipioTecnico.length() == 0) ||
                        (_provinciaTecnico.length() == 0))
                return false;
                if (emailTecnicoObligatorio){
                    if (emailTecnicoJTField.getText().trim().length() == 0) return false;
                }

            }else if ((_solicitud.getTecnicos() != null) && (_solicitud.getTecnicos().size() > 0) && (_solicitud.getIdTecnicoToDelete() != -1)){
                /** solicitud con tecnico y este se ha borrado ->
                 * si TODOS los campos obligatorios estan sin rellenar, ok
                 * si TODOS los campos obligatorios estan rellenados, ok (se ha insertado uno nuevo)
                 * si solo ALGUNO esta rellenado, error */
                if (!(((_DNI_CIF_Tecnico.length() == 0) && (_nombreTecnico.length() == 0) &&
                    (_nombreViaTecnico.length() == 0) &&
                    (_numeroViaTecnico.length() == 0) &&
                    (_cPostalTecnico.length() == 0) && (_municipioTecnico.length() == 0) &&
                    (_provinciaTecnico.length() == 0) &&
                    (!emailTecnicoObligatorio?true:emailTecnicoJTField.getText().trim().length() == 0)) ||
                    ((_DNI_CIF_Tecnico.length() != 0) && (_nombreTecnico.length() != 0) &&
                    (_nombreViaTecnico.length() != 0) &&
                    (_numeroViaTecnico.length() != 0) &&
                    (_cPostalTecnico.length() != 0) && (_municipioTecnico.length() != 0) &&
                    (_provinciaTecnico.length() != 0) &&
                    (!emailTecnicoObligatorio?true:emailTecnicoJTField.getText().trim().length() != 0)))) return false;

            }else if ((_solicitud.getTecnicos() == null) || (_solicitud.getTecnicos().size() == 0)){
                /** solicitud sin tecnico ->
                 * si TODOS los campos obligatorios estan sin rellenar, ok
                 * si TODOS los campos obligatorios estan rellenados, ok (se ha insertado uno nuevo)
                 * si solo ALGUNO esta rellenado, error */
                if (!(((_DNI_CIF_Tecnico.length() == 0) && (_nombreTecnico.length() == 0) &&
                    (_nombreViaTecnico.length() == 0) &&
                    (_numeroViaTecnico.length() == 0) &&
                    (_cPostalTecnico.length() == 0) && (_municipioTecnico.length() == 0) &&
                    (_provinciaTecnico.length() == 0) &&
                    (!emailTecnicoObligatorio?true:emailTecnicoJTField.getText().trim().length() == 0)) ||
                    ((_DNI_CIF_Tecnico.length() != 0) && (_nombreTecnico.length() != 0) &&
                    (_nombreViaTecnico.length() != 0) &&
                    (_numeroViaTecnico.length() != 0) &&
                    (_cPostalTecnico.length() != 0) && (_municipioTecnico.length() != 0) &&
                    (_provinciaTecnico.length() != 0) &&
                    (!emailTecnicoObligatorio?true:emailTecnicoJTField.getText().trim().length() != 0)))) return false;
            }

			/** Comprobamos los datos del promotor */
			// leemos los datos referentes al promotor
			_DNI_CIF_Promotor = DNIPromotorJTField.getText();
			_nombrePromotor = nombrePromotorJTField.getText();
			_apellido1Promotor = apellido1PromotorJTField.getText();
			_apellido2Promotor = apellido2PromotorJTField.getText();
			_colegioPromotor = colegioPromotorJTField.getText();
			_visadoPromotor = visadoPromotorJTField.getText();
			_titulacionPromotor = titulacionPromotorJTField.getText();
			_nombreViaPromotor = nombreViaPromotorJTField.getText();
			_numeroViaPromotor = numeroViaPromotorJTField.getText();
			_cPostalPromotor = cPostalPromotorJTField.getText();
			_municipioPromotor = municipioPromotorJTField.getText();
			_provinciaPromotor = provinciaPromotorJTField.getText();

            if ((_solicitud.getPromotor() != null) && (_solicitud.getIdPromotorToDelete() == -1)){
                /** solicitud con promotor y este no se ha borrado ->
                 * si ALGUN campo obligatorio no esta rellenado, error */
                if ((_DNI_CIF_Promotor.length() == 0) || (_nombrePromotor.length() == 0)) return false;
                if (    (_nombreViaPromotor.length() == 0) ||
                        (_cPostalPromotor.length() == 0) || (_municipioPromotor.length() == 0) ||
                        (_provinciaPromotor.length() == 0))
                return false;
                if (emailPromotorObligatorio){
                    if (emailPromotorJTField.getText().trim().length() == 0) return false;
                }

            }else if ((_solicitud.getPromotor() != null) && (_solicitud.getIdPromotorToDelete() != -1)){
                /** solicitud con promotor y este se ha borrado ->
                 * si TODOS los campos obligatorios estan sin rellenar, ok
                 * si TODOS los campos obligatorios estan rellenados, ok (se ha insertado uno nuevo)
                 * si solo ALGUNO esta rellenado, error */
                if (!(((_DNI_CIF_Promotor.length() == 0) && (_nombrePromotor.length() == 0) &&
                    (_nombreViaPromotor.length() == 0) &&
                    (_numeroViaPromotor.length() == 0) &&
                    (_cPostalPromotor.length() == 0) && (_municipioPromotor.length() == 0) &&
                    (_provinciaPromotor.length() == 0) &&
                    (!emailPromotorObligatorio?true:emailPromotorJTField.getText().trim().length() == 0)) ||
                    ((_DNI_CIF_Promotor.length() != 0) && (_nombrePromotor.length() != 0) &&
                    (_nombreViaPromotor.length() != 0) &&
                    (_numeroViaPromotor.length() != 0) &&
                    (_cPostalPromotor.length() != 0) && (_municipioPromotor.length() != 0) &&
                    (_provinciaPromotor.length() != 0) &&
                    (!emailPromotorObligatorio?true:emailPromotorJTField.getText().trim().length() != 0)))) return false;

            }else if (_solicitud.getPromotor() == null){
                /** solicitud sin promotor ->
                 * si TODOS los campos obligatorios estan sin rellenar, ok
                 * si TODOS los campos obligatorios estan rellenados, ok (se ha insertado uno nuevo)
                 * si solo ALGUNO esta rellenado, error */
                if (!(((_DNI_CIF_Promotor.length() == 0) && (_nombrePromotor.length() == 0) &&
                    (_nombreViaPromotor.length() == 0) &&
                    (_numeroViaPromotor.length() == 0) &&
                    (_cPostalPromotor.length() == 0) && (_municipioPromotor.length() == 0) &&
                    (_provinciaPromotor.length() == 0) &&
                    (!emailPromotorObligatorio?true:emailPromotorJTField.getText().trim().length() == 0)) ||
                    ((_DNI_CIF_Promotor.length() != 0) && (_nombrePromotor.length() != 0) &&
                    (_nombreViaPromotor.length() != 0) &&
                    (_numeroViaPromotor.length() != 0) &&
                    (_cPostalPromotor.length() != 0) && (_municipioPromotor.length() != 0) &&
                    (_provinciaPromotor.length() != 0) &&
                    (!emailPromotorObligatorio?true:emailPromotorJTField.getText().trim().length() != 0)))) return false;
            }

			if (fechaSolicitudJTField.getText().trim().length() == 0)
				return false;
			else if (tipoObraEJCBox.getSelectedPatron() == null)
				return false;
            else if (tipoViaINEEJCBox.getSelectedPatron() == null)
                return false;
            else if (nombreViaTField.getText().trim().length() == 0)
                return false;
			else
				return true;
                        
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}




	public void buscarDatosPersonaConDNI(String dni) {
		// Falta por hacer

	}

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(obraMayorJPanel, mensaje);
	}



	private boolean clearScreen() {
		/** Solicitud */
		CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);

		numRegistroJTField.setText("");
		numRegistroJTField.setEnabled(false);
		unidadTJTField.setText("");
		unidadRJTField.setText("");
		motivoJTField.setText("");
		asuntoJTField.setText("");
		fechaSolicitudJTField.setText("");
		fechaSolicitudJTField.setEnabled(false);
        fechaLimiteObraJTField.setText("");
        fechaLimiteObraJTField.setEnabled(false);

        tasaTextField.setText("€0.00");
        impuestoTextField.setText("€0.00");
		observacionesJTArea.setText("");

        /** Emplazamiento */
		tipoViaINEEJCBox.setSelectedIndex(0);
		nombreViaTField.setText("");
		numeroViaNumberTField.setText("");
		portalViaTField.setText("");
		plantaViaTField.setText("");
		letraViaTField.setText("");
        cpostalViaTField.setText("");

        /** datos actividad */
        datosActividadJPanel.load(null);

		/** Expediente */
		numExpedienteJTField.setText("");
		numExpedienteJTField.setEnabled(true);
		servicioExpedienteJTField.setText("");
		asuntoExpedienteJTField.setText("");
		fechaAperturaJTField.setText("");
		fechaAperturaJTField.setEnabled(false);
		inicioJTField.setText("");
		silencioJCheckBox.setSelected(false);
		observacionesExpedienteJTArea.setText("");
        cnaeTField.setText("");

		/** Propietario */
		DNIPropietarioJTField.setText("");
		nombrePropietarioJTField.setText("");
		apellido1PropietarioJTField.setText("");
		apellido2PropietarioJTField.setText("");
		faxPropietarioJTField.setText("");
		telefonoPropietarioJTField.setText("");
		movilPropietarioJTField.setText("");
		emailPropietarioJTField.setText("");
		nombreViaPropietarioJTField.setText("");
		numeroViaPropietarioJTField.setText("");
		plantaPropietarioJTField.setText("");
		letraPropietarioJTField.setText("");
		portalPropietarioJTField.setText("");
		escaleraPropietarioJTField.setText("");
		cPostalPropietarioJTField.setText("");
		municipioPropietarioJTField.setText("");
		provinciaPropietarioJTField.setText("");
		notificarPropietarioJCheckBox.setSelected(true);

		/** Representante */
		DNIRepresentanteJTField.setText("");
		nombreRepresentanteJTField.setText("");
		apellido1RepresentanteJTField.setText("");
		apellido2RepresentanteJTField.setText("");
		faxRepresentanteJTField.setText("");
		telefonoRepresentanteJTField.setText("");
		movilRepresentanteJTField.setText("");
		emailRepresentanteJTField.setText("");
		nombreViaRepresentanteJTField.setText("");
		numeroViaRepresentanteJTField.setText("");
		plantaRepresentanteJTField.setText("");
		letraRepresentanteJTField.setText("");
		portalRepresentanteJTField.setText("");
		escaleraRepresentanteJTField.setText("");
		cPostalRepresentanteJTField.setText("");
		municipioRepresentanteJTField.setText("");
		provinciaRepresentanteJTField.setText("");
		notificarRepresentanteJCheckBox.setSelected(false);

		/** Tecnico */
		DNITecnicoJTField.setText("");
		nombreTecnicoJTField.setText("");
		apellido1TecnicoJTField.setText("");
		apellido2TecnicoJTField.setText("");
		colegioTecnicoJTField.setText("");
		visadoTecnicoJTField.setText("");
		titulacionTecnicoJTField.setText("");
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

		/** Promotor */
		DNIPromotorJTField.setText("");
		nombrePromotorJTField.setText("");
		apellido1PromotorJTField.setText("");
		apellido2PromotorJTField.setText("");
		colegioPromotorJTField.setText("");
		visadoPromotorJTField.setText("");
		titulacionPromotorJTField.setText("");
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

		/** Notificaciones */
		CUtilidadesComponentes.clearTable(_notificacionesExpedienteTableModel);

		/** Eventos */
		CUtilidadesComponentes.clearTable(_eventosExpedienteTableModel);

		/** Historico */
		CUtilidadesComponentes.clearTable(_historicoExpedienteTableModel);

		return true;
	}

    private void annadirPestanas(ResourceBundle literales){

        /** Pestanas */
        try{
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.expedienteJPanel.TitleTab")), CUtilidadesComponentes.iconoExpediente, expedienteJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.expedienteJPanel.TitleTab")), expedienteJPanel);
        }

        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.SubTitleTab")), CUtilidadesComponentes.iconoSolicitud, obraMayorJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.SubTitleTab")), obraMayorJPanel);
        }
        /** anndimos la pestanna con los datos de la actividad */
        datosActividadJPanel= new DatosActividadJPanel(this.desktop, literales);
        datosActividadJPanel.load(null);
        try{
            jTabbedPaneSolicitud.addTab(literales.getString("DatosActividadJPanel.SubTitleTab"), CUtilidadesComponentes.iconoSolicitud, datosActividadJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(literales.getString("DatosActividadJPanel.SubTitleTab"), datosActividadJPanel);
        }

        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.propietarioJPanel.TitleTab")), CUtilidadesComponentes.iconoPersona, propietarioJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.propietarioJPanel.TitleTab")), propietarioJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.representanteJPanel.TitleTab"), CUtilidadesComponentes.iconoRepresentante, representanteJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.representanteJPanel.TitleTab"), representanteJPanel);
        }

        try{
            jTabbedPaneSolicitud.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.TitleTab"), CUtilidadesComponentes.iconoPersona, tecnicoJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.TitleTab"), tecnicoJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.promotorJPanel.TitleTab"), CUtilidadesComponentes.iconoPersona, promotorJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.promotorJPanel.TitleTab"), promotorJPanel);
        }
        try{
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.TitleTab")), CUtilidadesComponentes.iconoSolicitud, jTabbedPaneSolicitud);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.TitleTab")), jTabbedPaneSolicitud);
        }

        try{
            obraMayorJTabbedPane.addTab(literales.getString("DocumentacionLicenciasJPanel.title"), CUtilidadesComponentes.iconoDocumentacion, documentacionJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(literales.getString("DocumentacionLicenciasJPanel.title"), documentacionJPanel);
        }

        try{
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJPanel.TitleTab"), CUtilidadesComponentes.iconoNotificacion, notificacionesJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJPanel.TitleTab"), notificacionesJPanel);
        }

        try{
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJPanel.TitleTab"), CUtilidadesComponentes.iconoEvento, eventosJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJPanel.TitleTab"), eventosJPanel);
        }

        try{
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJPanel.TitleTab"), CUtilidadesComponentes.iconoHistorico, historicoJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJPanel.TitleTab"), historicoJPanel);
        }
        try{
            obraMayorJTabbedPane.addTab(literales.getString("JPanelInformes.jPanelInforme"),CUtilidadesComponentes.iconoInformes  , jPanelInformes);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(literales.getString("JPanelInformes.jPanelInforme"), jPanelInformes);
         }
    }


	public void renombrarComponentes(ResourceBundle literales) {
        this.literales=literales;

		try {
                setTitle(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.JInternalFrame.title"));

                /** Pestanas */
                obraMayorJTabbedPane.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.expedienteJPanel.TitleTab")));
                jTabbedPaneSolicitud.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.SubTitleTab")));
                jTabbedPaneSolicitud.setTitleAt(1, literales.getString("DatosActividadJPanel.SubTitleTab"));
                jTabbedPaneSolicitud.setTitleAt(2, CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.propietarioJPanel.TitleTab")));
                jTabbedPaneSolicitud.setTitleAt(3, literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.representanteJPanel.TitleTab"));
                jTabbedPaneSolicitud.setTitleAt(4, literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.TitleTab"));
                jTabbedPaneSolicitud.setTitleAt(5, literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.promotorJPanel.TitleTab"));
                obraMayorJTabbedPane.setTitleAt(1, CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.TitleTab")));
                obraMayorJTabbedPane.setTitleAt(2, literales.getString("DocumentacionLicenciasJPanel.title"));
                obraMayorJTabbedPane.setTitleAt(3, literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJPanel.TitleTab"));
                obraMayorJTabbedPane.setTitleAt(4, literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJPanel.TitleTab"));
                obraMayorJTabbedPane.setTitleAt(5, literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJPanel.TitleTab"));
                obraMayorJTabbedPane.setTitleAt(6, literales.getString("JPanelInformes.jPanelInforme"));

                /** Expediente */
                expedienteJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.expedienteJPanel.TitleBorder")));
                estadoExpedienteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.estadoExpedienteJLabel.text")));
                numExpedienteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.numExpedienteJLabel.text"));
                servicioExpedienteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.servicioExpedienteJLabel.text"));
                tramitacionJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tramitacionJLabel.text"));
                asuntoExpedienteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.asuntoExpedienteJLabel.text"));
                fechaAperturaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.fechaAperturaJLabel.text")));
                observacionesExpedienteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.observacionesExpedienteJLabel.text"));
                inicioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.inicioJLabel.text"));
                responsableJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.responsableJLabel.text"));
                consultarJButton.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.consultarJButton.text"));
                silencioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.silencioJLabel.text"));
                notaJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notaJLabel.text"));
                finalizaJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.finalizaJLable.text"));

                /** Solicitud */
                datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosSolicitudJPanel.TitleBorder")));
                tipoObraJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tipoObraJLabel.text")));
                jCheckBoxActividadNoCalificada.setText(literales.getString("jCheckBoxActividadNoCalificada"));
                unidadTJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.unidadTJLabel.text"));
                motivoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.motivoJLabel.text"));
                asuntoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.asuntoJLabel.text"));
                fechaSolicitudJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.fechaSolicitudJLabel.text")));
                fechaLimiteObraJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.fechaLimiteObraJLabel.text"));
                observacionesJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.observacionesJLabel.text"));
                numRegistroJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.numRegistroJLabel.text"));
                unidadRJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.unidadRJLabel.text"));
                tasaJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tasaJLabel.text"));
                impuestoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.impuestoJLabel.text"));
                emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emplazamientoJPanel.TitleBorder")));
                nombreViaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tipoViaJLabel.text"), literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombreViaJLabel.text")));
                numeroViaJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.numeroViaJLabel.text"));
                cPostalJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.cPostalJLabel.text"));
                provinciaJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.provinciaJLabel.text"));
                refCatastralJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.refCatastralJLabel.text"));
                cnaeJLabel.setText(literales.getString("cnaeJLabel.text"));

                /** Datos Actividad */
                datosActividadJPanel.renameComponents(literales);


                /** Propietario */
                datosPersonalesPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosPersonalesPropietarioJPanel.TitleBorder")));
                DNIPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.DNIPropietarioJLabel.text")));
                nombrePropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombrePropietarioJLabel.text")));
                apellido1PropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.apellido1PropietarioJLabel.text"));
                apellido2PropietarioJLabel2.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.apellido2PropietarioJLabel.text"));
                datosNotificacionPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionPropietarioJPanel.TitleTab")));
                viaNotificacionPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.viaNotificacionPropietarioJLabel.text"));
                faxPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.faxPropietarioJLabel.text"));
                telefonoPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.telefonoPropietarioJLabel.text"));
                movilPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.movilPropietarioJLabel.text"));
                emailPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailPropietarioJLabel.text"));
                tipoViaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tipoViaPropietarioJLabel.text")));
                nombreViaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombreViaPropietarioJLabel.text")));
                numeroViaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.numeroViaPropietarioJLabel.text")));
                portalPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.portalPropietarioJLabel.text"));
                plantaPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.plantaPropietarioJLabel.text"));
                escaleraPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.escaleraPropietarioJLabel.text"));
                letraPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.letraPropietarioJLabel.text"));
                cPostalPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.cPostalPropietarioJLabel.text")));
                municipioPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.municipioPropietarioJLabel.text")));
                provinciaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.provinciaPropietarioJLabel.text")));
                notificarPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificarPropietarioJLabel.text"));

                /** Representante */
                datosPersonalesRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosPersonalesRepresentanteJPanel.TitleBorder")));
                DNIRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.DNIRepresentanteJLabel.text")));
                nombreRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombreRepresentanteJLabel.text")));
                apellido1RepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.apellido1RepresentanteJLabel.text"));
                apellido2RepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.apellido2RepresentanteJLabel.text"));
                datosNotificacionRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionRepresentanteJPanel.TitleTab")));
                viaNotificacionRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.viaNotificacionRepresentanteJLabel.text"));
                faxRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.faxRepresentanteJLabel.text"));
                telefonoRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.telefonoRepresentanteJLabel.text"));
                movilRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.movilRepresentanteJLabel.text"));
                emailRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailRepresentanteJLabel.text"));
                tipoViaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tipoViaRepresentanteJLabel.text")));
                nombreViaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombreViaRepresentanteJLabel.text")));
                numeroViaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.numeroViaRepresentanteJLabel.text")));
                portalRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.portalRepresentanteJLabel.text"));
                plantaRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.plantaRepresentanteJLabel.text"));
                escaleraRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.escaleraRepresentanteJLabel.text"));
                letraRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.letraRepresentanteJLabel.text"));
                cPostalRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.cPostalRepresentanteJLabel.text")));
                municipioRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.municipioRepresentanteJLabel.text")));
                provinciaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.provinciaRepresentanteJLabel.text")));
                notificarRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificarRepresentanteJLabel.text"));

                /** Tecnico */
                datosPersonalesTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosPersonalesTecnicoJPanel.TitleBorder")));
                DNITecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.DNITecnicoJLabel.text")));
                nombreTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombreTecnicoJLabel.text")));
                apellido1TecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.apellido1TecnicoJLabel.text"));
                apellido2TecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.apellido2TecnicoJLabel.text"));
                colegioTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.colegioTecnicoJLabel.text"));
                visadoTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.visadoTecnicoJLabel.text"));
                titulacionTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.titulacionTecnicoJLabel.text"));
                datosNotificacionTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionTecnicoJPanel.TitleTab")));
                viaNotificacionTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.viaNotificacionTecnicoJLabel.text"));
                faxTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.faxTecnicoJLabel.text"));
                telefonoTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.telefonoTecnicoJLabel.text"));
                movilTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.movilTecnicoJLabel.text"));
                emailTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailTecnicoJLabel.text"));
                tipoViaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tipoViaTecnicoJLabel.text")));
                nombreViaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombreViaTecnicoJLabel.text")));
                numeroViaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.numeroViaTecnicoJLabel.text")));
                portalTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.portalTecnicoJLabel.text"));
                plantaTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.plantaTecnicoJLabel.text"));
                escaleraTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.escaleraTecnicoJLabel.text"));
                letraTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.letraTecnicoJLabel.text"));
                cPostalTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.cPostalTecnicoJLabel.text")));
                municipioTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.municipioTecnicoJLabel.text")));
                provinciaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.provinciaTecnicoJLabel.text")));
                notificarTecnicoJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificarTecnicoJLabel.text"));

                /** Promotor */
                datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosPersonalesPromotorJPanel.TitleBorder")));
                DNIPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.DNIPromotorJLabel.text")));
                nombrePromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombrePromotorJLabel.text")));
                apellido1PromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.apellido1PromotorJLabel.text"));
                apellido2PromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.apellido2PromotorJLabel.text"));
                colegioPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.colegioPromotorJLabel.text"));
                visadoPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.visadoPromotorJLabel.text"));
                titulacionPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.titulacionPromotorJLabel.text"));
                datosNotificacionPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionPromotorJPanel.TitleTab")));
                viaNotificacionPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.viaNotificacionPromotorJLabel.text"));
                faxPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.faxPromotorJLabel.text"));
                telefonoPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.telefonoPromotorJLabel.text"));
                movilPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.movilPromotorJLabel.text"));
                emailPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.emailPromotorJLabel.text"));
                tipoViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tipoViaPromotorJLabel.text")));
                nombreViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombreViaPromotorJLabel.text")));
                numeroViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.numeroViaPromotorJLabel.text")));
                portalPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.portalPromotorJLabel.text"));
                plantaPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.plantaPromotorJLabel.text"));
                escaleraPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.escaleraPromotorJLabel.text"));
                letraPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.letraPromotorJLabel.text"));
                cPostalPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.cPostalPromotorJLabel.text")));
                municipioPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.municipioPromotorJLabel.text")));
                provinciaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.provinciaPromotorJLabel.text")));
                notificarPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificarPromotorJLabel.text"));

                /** Documentacion */
                documentacionJPanel.renombrarComponentes(literales);


                /** notificaciones */
                datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionesJPanel.TitleBorder")));
                datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosNotificacionJPanel.TitleBorder")));
                datosNombreApellidosJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosNombreApellidosJLabel.text"));
                datosDireccionJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosDireccionJLabel.text"));
                datosCPostalJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosCPostalJLabel.text"));
                datosNotificarPorJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosNotificarPorJLabel.text"));
                entregadaAJLabel.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.entregadaAJLabel.text"));

                /** Eventos */
                datosEventosJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosEventosJPanel.TitleBorder")));
                descEventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.descEventoJScrollPane.TitleBorder")));

                /** Historico */
                datosHistoricoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.datosHistoricoJPanel.TitleBorder")));
                apunteJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.apunteJScrollPane.TitleBorder")));

                cancelarJButton.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.salirJButton.text"));
                aceptarJButton.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.modificarJButton.text"));
                publicarJButton.setText(literales.getString("CModificacionLicenciasForm.publicarJButton.text"));
                aceptarJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.modificarJButton.toolTipText"));
                publicarJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.publicarJButton.toolTipText"));
                jButtonGenerarFicha.setToolTipText(literales.getString("CMainLicenciasForm.generarFichaJButton.setToolTipText"));
                jButtonWorkFlow.setToolTipText(literales.getString("CMainLicenciasForm.verWorkFlowJButton.setToolTipText"));
                editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.editorMapaJPanel.TitleBorder")));
                jButtonGenerarFicha.setText(literales.getString("CMainLicencias.jButtonGenerarFicha"));
                jButtonWorkFlow.setText(literales.getString("CMainLicencias.jButtonWorkFlow"));
                modHistoricoJButton.setToolTipText(literales.getString("CMantenimientoHistorico.modHistoricoJButton.setToolTipText.text"));
                borrarHistoricoJButton.setToolTipText(literales.getString("CMantenimientoHistorico.borrarHistoricoJButton.setToolTipText.text"));
                nuevoHistoricoJButton.setToolTipText(literales.getString("CMantenimientoHistorico.nuevoHistoricoJButton.setToolTipText.text"));
                generarFichaHistoricoJButton.setToolTipText(literales.getString("CMantenimientoHistorico.generarFichaJButton.setToolTipText.text"));
                okJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.okJButtonToolTipText.text"));
                buscarExpedienteJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.buscarExpedienteJButtonToolTipText.text"));
                fechaLimiteObraJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.fechaLimiteObraJButtonToolTipText.text"));
                nombreViaJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.nombreViaJButtonToolTipText.text"));
                refCatastralJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.refCatastralJButtonToolTipText.text"));
                mapToTableJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.mapToTableJButtonToolTipText.text"));
                tableToMapJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.tableToMapJButtonToolTipText.text"));
                deleteParcelaJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.deleteParcelaJButtonToolTipText.text"));
                buscarDNIPropietarioJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.buscarDNIPropietarioJButtonToolTipText.text"));
                buscarDNIRepresentanteJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.buscarDNIRepresentanteJButtonToolTipText.text"));
                borrarRepresentanteJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.borrarRepresentanteJButtonToolTipText.text"));
                buscarDNITecnicoJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.buscarDNITecnicoJButtonToolTipText.text"));
                borrarTecnicoJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.borrarTecnicoJButtonToolTipText.text"));
                buscarDNIPromotorJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.buscarDNIPromotorJButtonToolTipText.text"));
                borrarPromotorJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.borrarPromotorJButtonToolTipText.text"));
                consultarJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.consultarJButton.text"));

                jPanelInformes.changeScreenLang(literales);
                jPanelResolucion.changeScreenLang(literales);

            /** Headers de la tabla eventos */
            TableColumn tableColumn= eventosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum1"));
            tableColumn= eventosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum2"));
            tableColumn= eventosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum3"));
            tableColumn= eventosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum4"));
            tableColumn= eventosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum5"));

            /** Headers tabla Notificaciones */
            tableColumn= notificacionesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum6"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum1"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum2"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum3"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum4"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum5"));

            /** Headers tabla Historico */
            tableColumn= historicoJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum2"));
            tableColumn= historicoJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum3"));
            tableColumn= historicoJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum4"));
            tableColumn= historicoJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum5"));

            /** Headers tabla referencias catastrales */
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column1"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column2"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column3"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column4"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column5"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column6"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column7"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column8"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column9"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(9);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.column10"));

	  } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al pintar las etiquetas: " , ex);
	  }
	}
    private void generarFicha()
       {
           try
           {
               if (_expediente == null)
               {
                   mostrarMensaje(literales.getString("CConsultaLicenciasForm.mensaje1"));
                   return;
               }
               _expediente.setEstructuraTipoLicencia(Estructuras.getListaTiposLicenciaActividad());
               _expediente.setEstructuraEstado(Estructuras.getListaEstados());
               _expediente.setEstructuraTipoObra(Estructuras.getListaTiposActividad());
               _expediente.setLocale(literales.getLocale().toString());
               _expediente.setSolicitud(_solicitud);
                  new GeopistaPrintable().printObjeto(FichasDisponibles.fichalicenciaactividad, _expediente , CExpedienteLicencia.class, MainActividadLicencias.geopistaEditor.getLayerViewPanel(), GeopistaPrintable.FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION);
           } catch (Exception ex) {
               logger.error("Exception al mostrar las features: " ,ex);
           }
       }
    private void verWorkFlow()
    {
    	try
        {
            if (_expediente == null)
            {
                mostrarMensaje(literales.getString("CConsultaLicenciasForm.mensaje1"));
                return;
            }

           logger.info("Mostrando workflow para el estado: "+_expediente.getEstado().getIdEstado());
           JDialogWorkFlow dialogImagen = new JDialogWorkFlow(desktop, _expediente.getEstado().getIdEstado(), _solicitud.getTipoLicencia().getIdTipolicencia());
           Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
           dialogImagen.setSize(590,720);
           dialogImagen.setLocation(d.width/2 - dialogImagen.getSize().width/2, d.height/2 - dialogImagen.getSize().height/2);
           dialogImagen.setResizable(false);
           dialogImagen.show();
           dialogImagen.setEstado(_expediente.getEstado().getIdEstado()) ;
           dialogImagen=null;
    	} catch (Exception ex) {
			logger.error("Exception al mostrar las features: " ,ex);
		}
    }



    public void mostrarEventoSeleccionado(){
        int row = eventosJTable.getSelectedRow();
        if (row != -1) {
            CEvento evento = (CEvento) _vEventos.get(((Integer)eventosSorted.getValueAt(row, eventosHiddenCol)).intValue());
            descEventoJTArea.setText(evento.getContent());
        }else{
            descEventoJTArea.setText("");
        }
    }

    public void mostrarHistoricoSeleccionado(){
        int row = historicoJTable.getSelectedRow();
        if (row != -1) {
            CHistorico historico= (CHistorico)vAuxiliar.get(((Integer)historicoSorted.getValueAt(row, historicoHiddenCol)).intValue());
            if (historico != null){
                apunteJTArea.setText(historico.getApunte());
            }
        }else{
            apunteJTArea.setText("");
        }
    }

	public void setNumExpediente(String numExpediente) {

		numExpedienteJTField.setText(numExpediente);
	}


	private DefaultTableModel referenciasCatastralesJTableModel;
	private JFrame desktop;

    private ComboBoxEstructuras tipoObraEJCBox;
    private ComboBoxEstructuras viaNotificacionPropietarioEJCBox;
    private ComboBoxEstructuras viaNotificacionRepresentanteEJCBox;
    private ComboBoxEstructuras viaNotificacionTecnicoEJCBox;
    private ComboBoxEstructuras viaNotificacionPromotorEJCBox;

    private ComboBoxEstructuras tipoViaNotificacionPropietarioEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionRepresentanteEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionTecnicoEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionPromotorEJCBox;

    private ComboBoxEstructuras tramitacionEJCBox;
    private ComboBoxEstructuras finalizacionEJCBox;

    /** tasa */
    private com.geopista.app.utilidades.JNumberTextField tasaTextField;
    /** Impuesto */
    private com.geopista.app.utilidades.JNumberTextField impuestoTextField;

    /** pestanna de documentacion de una solicitud (documentacion requerida, anexos...) */
    private DocumentacionLicenciasJPanel documentacionJPanel;

    /** notificacion entregada a */
    private com.geopista.app.utilidades.TextField entregadaATField;
    private com.geopista.app.utilidades.TextField responsableTField;

    /** emplazamiento */
    private ComboBoxEstructuras tipoViaINEEJCBox;
    private com.geopista.app.utilidades.TextField nombreViaTField;
    //private com.geopista.app.utilidades.JNumberTextField numeroViaNumberTField;
    private com.geopista.app.utilidades.TextField numeroViaNumberTField;
    private com.geopista.app.utilidades.TextField portalViaTField;
    private com.geopista.app.utilidades.TextField plantaViaTField;
    private com.geopista.app.utilidades.TextField letraViaTField;
    private com.geopista.app.utilidades.JNumberTextField cpostalViaTField;

    /** cnae */
    private com.geopista.app.utilidades.TextField cnaeTField;


    /** pestanna de los datos de actividad */
    private com.geopista.app.licencias.actividad.datosActividad.DatosActividadJPanel datosActividadJPanel;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DNIPromotorJLabel;
    private javax.swing.JTextField DNIPromotorJTField;
    private javax.swing.JLabel DNIPropietarioJLabel;
    private javax.swing.JTextField DNIPropietarioJTField;
    private javax.swing.JLabel DNIRepresentanteJLabel;
    private javax.swing.JTextField DNIRepresentanteJTField;
    private javax.swing.JLabel DNITecnicoJLabel;
    private javax.swing.JTextField DNITecnicoJTField;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton publicarJButton;
    private javax.swing.JLabel apellido1PromotorJLabel;
    private javax.swing.JTextField apellido1PromotorJTField;
    private javax.swing.JLabel apellido1PropietarioJLabel;
    private javax.swing.JTextField apellido1PropietarioJTField;
    private javax.swing.JLabel apellido1RepresentanteJLabel;
    private javax.swing.JTextField apellido1RepresentanteJTField;
    private javax.swing.JLabel apellido1TecnicoJLabel;
    private javax.swing.JTextField apellido1TecnicoJTField;
    private javax.swing.JLabel apellido2PromotorJLabel;
    private javax.swing.JTextField apellido2PromotorJTField;
    private javax.swing.JLabel apellido2PropietarioJLabel2;
    private javax.swing.JTextField apellido2PropietarioJTField;
    private javax.swing.JLabel apellido2RepresentanteJLabel;
    private javax.swing.JTextField apellido2RepresentanteJTField;
    private javax.swing.JLabel apellido2TecnicoJLabel;
    private javax.swing.JTextField apellido2TecnicoJTField;
    private javax.swing.JScrollPane apunteJScrollPane;
    private javax.swing.JTextArea apunteJTArea;
    private javax.swing.JLabel asuntoExpedienteJLabel;
    private javax.swing.JTextField asuntoExpedienteJTField;
    private javax.swing.JLabel asuntoJLabel;
    private javax.swing.JTextField asuntoJTField;
    private javax.swing.JButton borrarHistoricoJButton;
    private javax.swing.JButton borrarPromotorJButton;
    private javax.swing.JButton borrarRepresentanteJButton;
    private javax.swing.JButton borrarTecnicoJButton;
    private javax.swing.JPanel botoneraJPanel;
    private javax.swing.JButton buscarDNIPromotorJButton;
    private javax.swing.JButton buscarDNIPropietarioJButton;
    private javax.swing.JButton buscarDNIRepresentanteJButton;
    private javax.swing.JButton buscarDNITecnicoJButton;
    private javax.swing.JButton buscarExpedienteJButton;
    private javax.swing.JLabel cPostalJLabel;
    private javax.swing.JLabel cPostalPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalPromotorJTField;
    private javax.swing.JLabel cPostalPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalPropietarioJTField;
    private javax.swing.JLabel cPostalRepresentanteJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalRepresentanteJTField;
    private javax.swing.JLabel cPostalTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalTecnicoJTField;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JLabel colegioPromotorJLabel;
    private javax.swing.JTextField colegioPromotorJTField;
    private javax.swing.JLabel colegioTecnicoJLabel;
    private javax.swing.JTextField colegioTecnicoJTField;
    private javax.swing.JButton consultarJButton;
    private javax.swing.JLabel datosCPostalJLabel;
    private javax.swing.JTextField datosCPostalJTField;
    private javax.swing.JLabel datosDireccionJLabel;
    private javax.swing.JTextField datosDireccionJTField;
    private javax.swing.JPanel datosEventosJPanel;
    private javax.swing.JPanel datosHistoricoJPanel;
    private javax.swing.JLabel datosNombreApellidosJLabel;
    private javax.swing.JTextField datosNombreApellidosJTField;
    private javax.swing.JPanel datosNotificacionJPanel;
    private javax.swing.JPanel datosNotificacionPromotorJPanel;
    private javax.swing.JPanel datosNotificacionPropietarioJPanel;
    private javax.swing.JPanel datosNotificacionRepresentanteJPanel;
    private javax.swing.JPanel datosNotificacionTecnicoJPanel;
    private javax.swing.JPanel datosNotificacionesJPanel;
    private javax.swing.JLabel datosNotificarPorJLabel;
    private javax.swing.JTextField datosNotificarPorJTField;
    private javax.swing.JPanel datosPersonalesPromotorJPanel;
    private javax.swing.JPanel datosPersonalesPropietarioJPanel;
    private javax.swing.JPanel datosPersonalesRepresentanteJPanel;
    private javax.swing.JPanel datosPersonalesTecnicoJPanel;
    private javax.swing.JPanel datosSolicitudJPanel;
    private javax.swing.JButton deleteParcelaJButton;
    private javax.swing.JScrollPane descEventoJScrollPane;
    private javax.swing.JTextArea descEventoJTArea;
    private javax.swing.JPanel editorMapaJPanel;
    private javax.swing.JLabel emailPromotorJLabel;
    private javax.swing.JTextField emailPromotorJTField;
    private javax.swing.JLabel emailPropietarioJLabel;
    private javax.swing.JTextField emailPropietarioJTField;
    private javax.swing.JLabel emailRepresentanteJLabel;
    private javax.swing.JTextField emailRepresentanteJTField;
    private javax.swing.JLabel emailTecnicoJLabel;
    private javax.swing.JTextField emailTecnicoJTField;
    private javax.swing.JPanel emplazamientoJPanel;
    private javax.swing.JLabel entregadaAJLabel;
    private javax.swing.JLabel escaleraPromotorJLabel;
    private javax.swing.JTextField escaleraPromotorJTField;
    private javax.swing.JLabel escaleraPropietarioJLabel;
    private javax.swing.JTextField escaleraPropietarioJTField;
    private javax.swing.JLabel escaleraRepresentanteJLabel;
    private javax.swing.JTextField escaleraRepresentanteJTField;
    private javax.swing.JLabel escaleraTecnicoJLabel;
    private javax.swing.JTextField escaleraTecnicoJTField;
    private javax.swing.JComboBox estadoExpedienteJCBox;
    private javax.swing.JLabel estadoExpedienteJLabel;
    private javax.swing.JPanel eventosJPanel;
    private javax.swing.JScrollPane eventosJScrollPane;
    private javax.swing.JTable eventosJTable;
    private javax.swing.JPanel expedienteJPanel;
    private javax.swing.JLabel faxPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxPromotorJTField;
    private javax.swing.JLabel faxPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxPropietarioJTField;
    private javax.swing.JLabel faxRepresentanteJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxRepresentanteJTField;
    private javax.swing.JLabel faxTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxTecnicoJTField;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JTextField fechaAperturaJTField;
    private javax.swing.JButton fechaLimiteObraJButton;
    private javax.swing.JLabel fechaLimiteObraJLabel;
    private javax.swing.JTextField fechaLimiteObraJTField;
    private javax.swing.JLabel fechaSolicitudJLabel;
    private javax.swing.JTextField fechaSolicitudJTField;
    private javax.swing.JButton generarFichaHistoricoJButton;
    private javax.swing.JPanel historicoJPanel;
    private javax.swing.JScrollPane historicoJScrollPane;
    private javax.swing.JTable historicoJTable;
    private javax.swing.JLabel impuestoJLabel;
    private javax.swing.JLabel inicioJLabel;
    private javax.swing.JTextField inicioJTField;
    private javax.swing.JLabel letraPromotorJLabel;
    private javax.swing.JTextField letraPromotorJTField;
    private javax.swing.JLabel letraPropietarioJLabel;
    private javax.swing.JTextField letraPropietarioJTField;
    private javax.swing.JLabel letraRepresentanteJLabel;
    private javax.swing.JTextField letraRepresentanteJTField;
    private javax.swing.JLabel letraTecnicoJLabel;
    private javax.swing.JTextField letraTecnicoJTField;
    private javax.swing.JButton mapToTableJButton;
    private javax.swing.JButton modHistoricoJButton;
    private javax.swing.JLabel motivoJLabel;
    private javax.swing.JTextField motivoJTField;
    private javax.swing.JLabel movilPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilPromotorJTField;
    private javax.swing.JLabel movilPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilPropietarioJTField;
    private javax.swing.JLabel movilRepresentanteJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilRepresentanteJTField;
    private javax.swing.JLabel movilTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilTecnicoJTField;
    private javax.swing.JTextField municipioJTField;
    private javax.swing.JLabel municipioPromotorJLabel;
    private javax.swing.JTextField municipioPromotorJTField;
    private javax.swing.JLabel municipioPropietarioJLabel;
    private javax.swing.JTextField municipioPropietarioJTField;
    private javax.swing.JLabel municipioRepresentanteJLabel;
    private javax.swing.JTextField municipioRepresentanteJTField;
    private javax.swing.JLabel municipioTecnicoJLabel;
    private javax.swing.JTextField municipioTecnicoJTField;
    private javax.swing.JLabel nombrePromotorJLabel;
    private javax.swing.JTextField nombrePromotorJTField;
    private javax.swing.JLabel nombrePropietarioJLabel;
    private javax.swing.JTextField nombrePropietarioJTField;
    private javax.swing.JLabel nombreRepresentanteJLabel;
    private javax.swing.JTextField nombreRepresentanteJTField;
    private javax.swing.JLabel nombreTecnicoJLabel;
    private javax.swing.JTextField nombreTecnicoJTField;
    private javax.swing.JButton nombreViaJButton;
    private javax.swing.JLabel nombreViaJLabel;
    private javax.swing.JLabel nombreViaPromotorJLabel;
    private javax.swing.JTextField nombreViaPromotorJTField;
    private javax.swing.JLabel nombreViaPropietarioJLabel;
    private javax.swing.JTextField nombreViaPropietarioJTField;
    private javax.swing.JLabel nombreViaRepresentanteJLabel;
    private javax.swing.JTextField nombreViaRepresentanteJTField;
    private javax.swing.JLabel nombreViaTecnicoJLabel;
    private javax.swing.JTextField nombreViaTecnicoJTField;
    private javax.swing.JLabel notaJLabel;
    private javax.swing.JPanel notificacionesJPanel;
    private javax.swing.JScrollPane notificacionesJScrollPane;
    private javax.swing.JTable notificacionesJTable;
    private javax.swing.JCheckBox notificarPromotorJCheckBox;
    private javax.swing.JLabel notificarPromotorJLabel;
    private javax.swing.JCheckBox notificarPropietarioJCheckBox;
    private javax.swing.JLabel notificarPropietarioJLabel;
    private javax.swing.JCheckBox notificarRepresentanteJCheckBox;
    private javax.swing.JLabel notificarRepresentanteJLabel;
    private javax.swing.JCheckBox notificarTecnicoJCheckBox;
    private javax.swing.JLabel notificarTecnicoJLabel;
    private javax.swing.JButton nuevoHistoricoJButton;
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JLabel numRegistroJLabel;
    private javax.swing.JTextField numRegistroJTField;
    private javax.swing.JLabel numeroViaJLabel;
    private javax.swing.JLabel numeroViaPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaPromotorJTField;
    private javax.swing.JLabel numeroViaPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaPropietarioJTField;
    private javax.swing.JLabel numeroViaRepresentanteJLabel;
    private javax.swing.JLabel finalizaJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaRepresentanteJTField;
    private javax.swing.JLabel numeroViaTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaTecnicoJTField;
    private javax.swing.JPanel obraMayorJPanel;
    private javax.swing.JTabbedPane obraMayorJTabbedPane;
    private javax.swing.JTabbedPane jTabbedPaneSolicitud;
    private javax.swing.JLabel observacionesExpedienteJLabel;
    private javax.swing.JScrollPane observacionesExpedienteJScrollPane;
    private javax.swing.JTextArea observacionesExpedienteJTArea;
    private javax.swing.JLabel observacionesJLabel;
    private javax.swing.JScrollPane observacionesJScrollPane;
    private javax.swing.JTextArea observacionesJTArea;
    private javax.swing.JButton okJButton;
    private javax.swing.JLabel plantaPromotorJLabel;
    private javax.swing.JTextField plantaPromotorJTField;
    private javax.swing.JLabel plantaPropietarioJLabel;
    private javax.swing.JTextField plantaPropietarioJTField;
    private javax.swing.JLabel plantaRepresentanteJLabel;
    private javax.swing.JTextField plantaRepresentanteJTField;
    private javax.swing.JLabel plantaTecnicoJLabel;
    private javax.swing.JTextField plantaTecnicoJTField;
    private javax.swing.JLabel portalPromotorJLabel;
    private javax.swing.JTextField portalPromotorJTField;
    private javax.swing.JLabel portalPropietarioJLabel;
    private javax.swing.JTextField portalPropietarioJTField;
    private javax.swing.JLabel portalRepresentanteJLabel;
    private javax.swing.JTextField portalRepresentanteJTField;
    private javax.swing.JLabel portalTecnicoJLabel;
    private javax.swing.JTextField portalTecnicoJTField;
    private javax.swing.JPanel promotorJPanel;
    private javax.swing.JPanel propietarioJPanel;
    private javax.swing.JLabel provinciaJLabel;
    private javax.swing.JTextField provinciaJTField;
    private javax.swing.JLabel provinciaPromotorJLabel;
    private javax.swing.JTextField provinciaPromotorJTField;
    private javax.swing.JLabel provinciaPropietarioJLabel;
    private javax.swing.JTextField provinciaPropietarioJTField;
    private javax.swing.JLabel provinciaRepresentanteJLabel;
    private javax.swing.JTextField provinciaRepresentanteJTField;
    private javax.swing.JLabel provinciaTecnicoJLabel;
    private javax.swing.JTextField provinciaTecnicoJTField;
    private javax.swing.JButton refCatastralJButton;
    private javax.swing.JLabel refCatastralJLabel;
    private javax.swing.JTextField refCatastralJTextField;
    private javax.swing.JScrollPane referenciasCatastralesJScrollPane;
    private javax.swing.JTable referenciasCatastralesJTable;
    private javax.swing.JPanel representanteJPanel;
    private javax.swing.JLabel responsableJLabel;
    private javax.swing.JLabel servicioExpedienteJLabel;
    private javax.swing.JTextField servicioExpedienteJTField;
    private javax.swing.JCheckBox silencioJCheckBox;
    private javax.swing.JLabel silencioJLabel;
    private javax.swing.JButton tableToMapJButton;
    private javax.swing.JLabel tasaJLabel;
    private javax.swing.JPanel tecnicoJPanel;
    private javax.swing.JLabel telefonoPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoPromotorJTField;
    private javax.swing.JLabel telefonoPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoPropietarioJTField;
    private javax.swing.JLabel telefonoRepresentanteJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoRepresentanteJTField;
    private javax.swing.JLabel telefonoTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoTecnicoJTField;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoObraJLabel;
    private javax.swing.JCheckBox jCheckBoxActividadNoCalificada;
    private javax.swing.JLabel tipoViaPromotorJLabel;
    private javax.swing.JLabel tipoViaPropietarioJLabel;
    private javax.swing.JLabel tipoViaRepresentanteJLabel;
    private javax.swing.JLabel tipoViaTecnicoJLabel;
    private javax.swing.JLabel titulacionPromotorJLabel;
    private javax.swing.JTextField titulacionPromotorJTField;
    private javax.swing.JLabel titulacionTecnicoJLabel;
    private javax.swing.JTextField titulacionTecnicoJTField;
    private javax.swing.JLabel tramitacionJLabel;
    private javax.swing.JLabel unidadRJLabel;
    private javax.swing.JTextField unidadRJTField;
    private javax.swing.JLabel unidadTJLabel;
    private javax.swing.JTextField unidadTJTField;
    private javax.swing.JLabel viaNotificacionPromotorJLabel;
    private javax.swing.JLabel viaNotificacionPropietarioJLabel;
    private javax.swing.JLabel viaNotificacionRepresentanteJLabel;
    private javax.swing.JLabel viaNotificacionTecnicoJLabel;
    private javax.swing.JLabel visadoPromotorJLabel;
    private javax.swing.JTextField visadoPromotorJTField;
    private javax.swing.JLabel visadoTecnicoJLabel;
    private javax.swing.JTextField visadoTecnicoJTField;
    private javax.swing.JButton jButtonGenerarFicha;
    private javax.swing.JButton jButtonWorkFlow;
    private JPanelInformes jPanelInformes;
    private JPanelResolucion jPanelResolucion;
    private javax.swing.JLabel jLabelEstadoActual;
    private javax.swing.JLabel entregadaATextJLabel;
    private javax.swing.JLabel cnaeJLabel;
    // End of variables declaration//GEN-END:variables

}
