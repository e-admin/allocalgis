/**
 * CModificacionLicencias.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.modificacion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.licencias.CConstantesLicencias;
import com.geopista.app.licencias.CConstantesLicencias_LCGIII;
import com.geopista.app.licencias.CHistoricoJDialog;
import com.geopista.app.licencias.CMainLicencias;
import com.geopista.app.licencias.CUtilidades;
import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.app.licencias.IMultilingue;
import com.geopista.app.licencias.documentacion.DocumentacionLicenciasJPanel;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CEventosModificacionTableModel;
import com.geopista.app.licencias.tableModels.CHistoricoModificacionTableModel;
import com.geopista.app.licencias.tableModels.CNotificacionesModificacionTableModel;
import com.geopista.app.licencias.tableModels.CReferenciasCatastralesTableModel;
import com.geopista.app.licencias.tecnicos.TecnicosJPanel;
import com.geopista.app.licencias.utilidades.CheckBoxRenderer;
import com.geopista.app.licencias.utilidades.CheckBoxTableEditor;
import com.geopista.app.licencias.utilidades.ComboBoxTableEditor;
import com.geopista.app.licencias.utilidades.JDialogWorkFlow;
import com.geopista.app.licencias.utilidades.JInternalFrameLicencias;
import com.geopista.app.licencias.utilidades.JPanelInformes;
import com.geopista.app.licencias.utilidades.JPanelResolucion;
import com.geopista.app.licencias.utilidades.TextFieldRenderer;
import com.geopista.app.licencias.utilidades.TextFieldTableEditor;
import com.geopista.app.printer.FichasDisponibles;
import com.geopista.app.printer.GeopistaPrintable;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.CellRendererEstructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxRendererEstructuras;
import com.geopista.editor.GeopistaEditor;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.Alegacion;
import com.geopista.protocol.licencias.CDatosNotificacion;
import com.geopista.protocol.licencias.CEvento;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CHistorico;
import com.geopista.protocol.licencias.CNotificacion;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.CViaNotificacion;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.estados.CEstadoNotificacion;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.protocol.licencias.tipos.CTipoObra;
import com.geopista.protocol.licencias.tipos.CTipoTramitacion;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
//import com.geopista.app.licencias.consulta.ISIGEMWSClient;
//import com.geopista.app.licencias.consulta.ISIGEMWSPortType;

/**
 * @author avivar
 */


public class CModificacionLicencias extends JInternalFrameLicencias implements IMultilingue {

	/**
	 * Datos del Propietario
	 */
	private String _DNI_CIF_Propietario = "";
	private String _nombrePropietario = "";
	private String _apellido1Propietario = "";
	private String _apellido2Propietario = "";
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
	private String _emailRepresentante = "";
    private String _tipoViaRepresentante = "";
	private String _nombreViaRepresentante = "";
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

	/**
	 * Comprueba si la consulta se ha realizado correctamente
	 */
	private boolean _consultaOK = false;
    /**
     * por defecto la solicitud es manual y no llega desde Ventanilla Unica
     */
	private String _vu = "0";
    private boolean fromMenu= true;
	Logger logger = Logger.getLogger(CModificacionLicencias.class);


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
	public CModificacionLicencias(final JFrame desktop,final String numExpediente, boolean calledFromMenu) {
		this.desktop = desktop;
        fromMenu= calledFromMenu;
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
                            annadirPestanas(CMainLicencias.literales);
                            renombrarComponentes(CMainLicencias.literales);
                            fechaSolicitudJTField.setEnabled(false);
                            fechaLimiteObraJTField.setEnabled(false);
                            progressDialog.report(CMainLicencias.literales.getString("Licencias.Tag1"));
                            loadMapa(/*"licencias_obra_mayor"*/276, "licencias_obra_mayor", "licencias_obra_menor");
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
			if (CMainLicencias.geopistaEditor == null) CMainLicencias.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");
            setGeopistaEditor(CMainLicencias.geopistaEditor);
			CConstantesLicencias.referenciasCatastrales= new Hashtable();

            /** Definimos el TableModel para el componente notificacionesJTable */
			String[] columnNamesNotificaciones = {CMainLicencias.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum6"),
                                                  CMainLicencias.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum1"),
												  CMainLicencias.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum2"),
												  CMainLicencias.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum3"),
												  CMainLicencias.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum4"),
												  CMainLicencias.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum5"),
                                                  "HIDDEN"};
			CNotificacionesModificacionTableModel.setColumnNames(columnNamesNotificaciones);
			_notificacionesExpedienteTableModel = new CNotificacionesModificacionTableModel();

            /** Definimos el TableModel para el componente eventosJTable */
			String[] columnNamesEventos = {CMainLicencias.literales.getString("CModificacionLicenciasForm.eventosJTable.colum1"),
										   CMainLicencias.literales.getString("CModificacionLicenciasForm.eventosJTable.colum2"),
										   CMainLicencias.literales.getString("CModificacionLicenciasForm.eventosJTable.colum3"),
										   CMainLicencias.literales.getString("CModificacionLicenciasForm.eventosJTable.colum4"),
										   CMainLicencias.literales.getString("CModificacionLicenciasForm.eventosJTable.colum5"),
                                           "HIDDEN"};
			CEventosModificacionTableModel.setColumnNames(columnNamesEventos);
			_eventosExpedienteTableModel = new CEventosModificacionTableModel();

            /** Definimos el TableModel para el componente historicoJTable */
			String[] columnNamesHistorico = {CMainLicencias.literales.getString("CModificacionLicenciasForm.historicoJTable.colum2"),
											 CMainLicencias.literales.getString("CModificacionLicenciasForm.historicoJTable.colum3"),
											 CMainLicencias.literales.getString("CModificacionLicenciasForm.historicoJTable.colum4"),
											 CMainLicencias.literales.getString("CModificacionLicenciasForm.historicoJTable.colum5"),
                                             "HIDDEN"};
			CHistoricoModificacionTableModel.setColumnNames(columnNamesHistorico);
			_historicoExpedienteTableModel= new CHistoricoModificacionTableModel();


            /** Definimos el TableModel para el componente referenciasCatastralesJTable */
			referenciasCatastralesJTableModel= new CReferenciasCatastralesTableModel(new String[]{CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column1"),
																				   CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column2"),
																				   CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column3"),
																				   CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column4"),
																				   CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column5"),
																				   CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column6"),
																				   CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column7"),
																				   CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column8"),
																				   CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column9"),
                                                                                   CMainLicencias.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column10"), ""});

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
                }else if (j == 10){
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
            TableColumn tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
            ComboBoxEstructuras comboBoxEstructuras= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), true);
            comboBoxEstructuras.setSelectedIndex(0);

            ComboBoxTableEditor comboBoxTableEditor= new ComboBoxTableEditor(comboBoxEstructuras);
            comboBoxTableEditor.setEnabled(true);
            tableColumn.setCellEditor(comboBoxTableEditor);

            CellRendererEstructuras renderer =
                            new CellRendererEstructuras(CMainLicencias.literales.getLocale().toString(),Estructuras.getListaTiposViaINE());
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
                tableColumn.setCellEditor(textFieldTableEditor);
                tableColumn.setCellRenderer(new TextFieldRenderer());
            }

			/** Solicitud */
			municipioJTField.setText(CConstantesLicencias.Municipio);
			municipioJTField.setEnabled(false);
			provinciaJTField.setText(CConstantesLicencias.Provincia);
			provinciaJTField.setEnabled(false);

			/** Definimos el componente notificacionesJTable */
            notificacionesSorted= new TableSorted(_notificacionesExpedienteTableModel);
            notificacionesSorted.setTableHeader(notificacionesJTable.getTableHeader());
			notificacionesJTable.setModel(notificacionesSorted);
            notificacionesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
            eventosSorted= new TableSorted(_eventosExpedienteTableModel);
            eventosSorted.setTableHeader(eventosJTable.getTableHeader());
			eventosJTable.setModel(eventosSorted);
            eventosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
            historicoSorted= new TableSorted(_historicoExpedienteTableModel);
            historicoSorted.setTableHeader(historicoJTable.getTableHeader());
			historicoJTable.setModel(historicoSorted);
            historicoJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
           	nombreViaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
			refCatastralJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);


			mapToTableJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoFlechaIzquierda);
			tableToMapJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoFlechaDerecha);

			buscarDNIPropietarioJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
			buscarDNIRepresentanteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
			buscarDNIPromotorJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);

            borrarRepresentanteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
            deleteParcelaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);

            /** Historico */
            if (_expediente == null){
                nuevoHistoricoJButton.setEnabled(false);
                borrarHistoricoJButton.setEnabled(false);
                modHistoricoJButton.setEnabled(false);
            }
            modHistoricoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoOK);
            borrarHistoricoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoRemove);
            nuevoHistoricoJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoAdd);
            generarFichaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoGenerarFicha);

            modHistoricoJButton.setToolTipText(CMainLicencias.literales.getString("CMantenimientoHistorico.modHistoricoJButton.setToolTipText.text"));
            borrarHistoricoJButton.setToolTipText(CMainLicencias.literales.getString("CMantenimientoHistorico.borrarHistoricoJButton.setToolTipText.text"));
            nuevoHistoricoJButton.setToolTipText(CMainLicencias.literales.getString("CMantenimientoHistorico.nuevoHistoricoJButton.setToolTipText.text"));
            generarFichaJButton.setToolTipText(CMainLicencias.literales.getString("CMantenimientoHistorico.generarFichaJButton.setToolTipText.text"));

            /** Observaciones Solicitud */
            observacionesSolicitudTPane= new com.geopista.app.utilidades.TextPane(254);
            observacionesJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            observacionesJScrollPane.setViewportView(observacionesSolicitudTPane);
            datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 330, 90));


            /** Observaciones Expediente */
            observacionesExpedienteTPane= new com.geopista.app.utilidades.TextPane(254);
            observacionesExpedienteJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            observacionesExpedienteJScrollPane.setViewportView(observacionesExpedienteTPane);
            expedienteJPanel.add(observacionesExpedienteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 290, 300, 90));


            /** Motivo */
            motivoTField= new com.geopista.app.utilidades.TextField(254);
            datosSolicitudJPanel.add(motivoTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 330, -1));

            /** Resolucion */
            jLabelEstadoActual.setForeground(new Color(255,0,102));
            expedienteJPanel.add(jLabelEstadoActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 300, 20));
            expedienteJPanel.add(jPanelResolucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 380,500, 175));

            /** Asunto */
            asuntoSolicitudTField= new com.geopista.app.utilidades.TextField(254);
            datosSolicitudJPanel.add(asuntoSolicitudTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 330, -1));
            asuntoExpedienteTField= new com.geopista.app.utilidades.TextField(128);
            expedienteJPanel.add(asuntoExpedienteTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 300, -1));

            /** dni */
            DNIPromotorTField= new com.geopista.app.utilidades.TextField(10);
            datosPersonalesPromotorJPanel.add(DNIPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 280, -1));

            DNIPropietarioTField= new com.geopista.app.utilidades.TextField(10) ;
            datosPersonalesPropietarioJPanel.add(DNIPropietarioTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 280, -1));

            DNIRepresentanteTField= new com.geopista.app.utilidades.TextField(10);
            datosPersonalesRepresentanteJPanel.add(DNIRepresentanteTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 280, -1));

            /** codigos postales */
            cPostalPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            cPostalPromotorTField.setSignAllowed(false);
            datosNotificacionPromotorJPanel.add(cPostalPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 277, 300, -1));

            cPostalRepresentanteTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            cPostalRepresentanteTField.setSignAllowed(false);
            datosNotificacionRepresentanteJPanel.add(cPostalRepresentanteTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 294, 300, -1));

            cPostalPropietarioTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            cPostalPropietarioTField.setSignAllowed(false);
            datosNotificacionPropietarioJPanel.add(cPostalPropietarioTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 294, 300, -1));

            /** Numeros de via para la notificacion */
            numeroViaPropietarioTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            numeroViaPropietarioTField.setSignAllowed(false);
            datosNotificacionPropietarioJPanel.add(numeroViaPropietarioTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 184, 150, -1));

            numeroViaRepresentanteTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            numeroViaRepresentanteTField.setSignAllowed(false);
            datosNotificacionRepresentanteJPanel.add(numeroViaRepresentanteTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 184, 150, -1));

            numeroViaPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            numeroViaPromotorTField.setSignAllowed(false);
            datosNotificacionPromotorJPanel.add(numeroViaPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 172, 150, -1));

            /** Telefono para la notificacion */
            telefonoPropietarioTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
            telefonoPropietarioTField.setSignAllowed(false);
            datosNotificacionPropietarioJPanel.add(telefonoPropietarioTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 64, 300, -1));

            telefonoRepresentanteTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
            telefonoRepresentanteTField.setSignAllowed(false);
            datosNotificacionRepresentanteJPanel.add(telefonoRepresentanteTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 64, 300, -1));

            telefonoPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
            telefonoPromotorTField.setSignAllowed(false);
            datosNotificacionPromotorJPanel.add(telefonoPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 62, 300, -1));

            /** Movil para la notificacion */
            movilPropietarioTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
            movilPropietarioTField.setSignAllowed(false);
            datosNotificacionPropietarioJPanel.add(movilPropietarioTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 86, 300, -1));

            movilRepresentanteTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
            movilRepresentanteTField.setSignAllowed(false);
            datosNotificacionRepresentanteJPanel.add(movilRepresentanteTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 86, 300, -1));

            movilPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
            movilPromotorTField.setSignAllowed(false);
            datosNotificacionPromotorJPanel.add(movilPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 83, 300, -1));


            /** Fax para la notificacion */
            faxPropietarioTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
            faxPropietarioTField.setSignAllowed(false);
            datosNotificacionPropietarioJPanel.add(faxPropietarioTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 42, 300, -1));

            faxRepresentanteTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
            faxRepresentanteTField.setSignAllowed(false);
            datosNotificacionRepresentanteJPanel.add(faxRepresentanteTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 42, 300, -1));

            faxPromotorTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999999), true);
            faxPromotorTField.setSignAllowed(false);
            datosNotificacionPromotorJPanel.add(faxPromotorTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 41, 300, -1));

            /** tasa e impuesto */
            impuestoTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
            impuestoTextField.setSignAllowed(false);
            datosSolicitudJPanel.add(impuestoTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 185, 330, -1));

            tasaTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
            tasaTextField.setSignAllowed(false);
            datosSolicitudJPanel.add(tasaTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 165, 330, -1));

            /** entregada a */
            entregadaATField= new com.geopista.app.utilidades.TextField(68);
            datosNotificacionJPanel.add(entregadaATField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 160, 260, 20));
            entregadaATField.setVisible(false);
            okJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoOK);
            okJButton.setVisible(false);
            okJButton.setToolTipText(CMainLicencias.literales.getString("CModificacionLicenciasForm.okJButtonToolTipText.text"));
            entregadaATextJLabel= new javax.swing.JLabel();
            entregadaATextJLabel.setText("");
            datosNotificacionJPanel.add(entregadaATextJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 260, 20));


            /** fecha Limite de obra */
            fechaLimiteObraJLabel= new javax.swing.JLabel();
            fechaLimiteObraJLabel.setText(CMainLicencias.literales.getString("CModificacionLicenciasForm.fechaLimiteObraJLabel.text"));
            fechaLimiteObraJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            datosSolicitudJPanel.add(fechaLimiteObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 140, 130, 20));

            fechaLimiteObraJTField= new javax.swing.JTextField();
            fechaLimiteObraJTField.setEnabled(false);
            datosSolicitudJPanel.add(fechaLimiteObraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 80, -1));

            fechaLimiteObraJButton=  new javax.swing.JButton();
            fechaLimiteObraJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
            fechaLimiteObraJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
            fechaLimiteObraJButton.setPreferredSize(new java.awt.Dimension(30, 30));
            fechaLimiteObraJButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    fechaLimiteObraJButtonActionPerformed();
                }
            });
            datosSolicitudJPanel.add(fechaLimiteObraJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, 20, 20));

            responsableTField= new com.geopista.app.utilidades.TextField(68);
            expedienteJPanel.add(responsableTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 260, 300, -1));

            /** emplazamiento */
            nombreViaTField= new com.geopista.app.utilidades.TextField(68);
            nombreViaTField.setEditable(true);
            emplazamientoJPanel.add(nombreViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 190, -1));

            //numeroViaNumberTField=  new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            numeroViaNumberTField=  new com.geopista.app.utilidades.TextField(8);
            numeroViaNumberTField.setEditable(true);
            //numeroViaNumberTField.setSignAllowed(false);
            emplazamientoJPanel.add(numeroViaNumberTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 80, -1));

            portalViaTField= new com.geopista.app.utilidades.TextField(5);
            portalViaTField.setEditable(true);
            emplazamientoJPanel.add(portalViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 80, -1));

            plantaViaTField= new com.geopista.app.utilidades.TextField(5);
            plantaViaTField.setEditable(true);
            emplazamientoJPanel.add(plantaViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 70, 70, -1));

            letraViaTField= new com.geopista.app.utilidades.TextField(5);
            letraViaTField.setEditable(true);
            emplazamientoJPanel.add(letraViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 70, -1));

            cpostalViaTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            cpostalViaTField.setEditable(true);
            cpostalViaTField.setSignAllowed(false);
            emplazamientoJPanel.add(cpostalViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 80, -1));

			return true;
		} catch (Exception ex) {
			logger.error("Exception al crear los tab: ",ex);
			return false;
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
        tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposObra(), null, CMainLicencias.currentLocale.toString(), false);
        datosSolicitudJPanel.add(tipoObraEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 330, 20));

        /**expediente */
        finalizacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposFinalizacion(), null, CMainLicencias.currentLocale.toString(), true);
        finalizacionEJCBox.setEnabled(false);
        finalizacionEJCBox.setEditable(false);
        expedienteJPanel.add(finalizacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 555, 300, -1));

        tramitacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposTramitacion(), null, CMainLicencias.currentLocale.toString(), false);
        expedienteJPanel.add(tramitacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 300, 20));

        /** propietario */
        viaNotificacionPropietarioEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainLicencias.currentLocale.toString(), false);
        viaNotificacionPropietarioEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionPropietarioEJCBoxActionPerformed();}});

        datosNotificacionPropietarioJPanel.add(viaNotificacionPropietarioEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 300, 20));
        tipoViaNotificacionPropietarioEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), false);
        datosNotificacionPropietarioJPanel.add(tipoViaNotificacionPropietarioEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 300, 20));

        /** representante */
        viaNotificacionRepresentanteEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainLicencias.currentLocale.toString(), false);
        viaNotificacionRepresentanteEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionRepresentanteEJCBoxActionPerformed();}});

        datosNotificacionRepresentanteJPanel.add(viaNotificacionRepresentanteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 300, 20));
        tipoViaNotificacionRepresentanteEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), false);
        datosNotificacionRepresentanteJPanel.add(tipoViaNotificacionRepresentanteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 300, 20));

        /** promotor */
        viaNotificacionPromotorEJCBox= new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainLicencias.currentLocale.toString(), false);
        viaNotificacionPromotorEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionPromotorEJCBoxActionPerformed();}});

        datosNotificacionPromotorJPanel.add(viaNotificacionPromotorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 300, 20));
        tipoViaNotificacionPromotorEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), false);
        datosNotificacionPromotorJPanel.add(tipoViaNotificacionPromotorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 300, 20));

        /** emplazamiento (tipoVia) */
        tipoViaINEEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainLicencias.currentLocale.toString(), true);
        tipoViaINEEJCBox.setSelectedIndex(0);
        emplazamientoJPanel.add(tipoViaINEEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 110, -1));


    }

    private void viaNotificacionPropietarioEJCBoxActionPerformed() {
        String index= viaNotificacionPropietarioEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CModificacionLicenciasForm.emailPropietarioJLabel.text")));
            emailPropietarioObligatorio= true;
        }else{
            emailPropietarioJLabel.setText(CMainLicencias.literales.getString("CModificacionLicenciasForm.emailPropietarioJLabel.text"));
            emailPropietarioObligatorio= false;
        }

    }

    private void viaNotificacionRepresentanteEJCBoxActionPerformed() {

        String index= viaNotificacionRepresentanteEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CModificacionLicenciasForm.emailRepresentanteJLabel.text")));
            emailRepresentanteObligatorio= true;
        }else{
            emailRepresentanteJLabel.setText(CMainLicencias.literales.getString("CModificacionLicenciasForm.emailRepresentanteJLabel.text"));
            emailRepresentanteObligatorio= false;
        }

    }

    private void viaNotificacionPromotorEJCBoxActionPerformed() {
        String index= viaNotificacionPromotorEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
            emailPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainLicencias.literales.getString("CModificacionLicenciasForm.emailPromotorJLabel.text")));
            emailPromotorObligatorio= true;
        }else{
            emailPromotorJLabel.setText(CMainLicencias.literales.getString("CModificacionLicenciasForm.emailPromotorJLabel.text"));
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
        numExpedienteJLabel = new javax.swing.JLabel();
        servicioExpedienteJLabel = new javax.swing.JLabel();
        tramitacionJLabel = new javax.swing.JLabel();
        finalizaJLabel = new javax.swing.JLabel();
        asuntoExpedienteJLabel = new javax.swing.JLabel();
        fechaAperturaJLabel = new javax.swing.JLabel();
        observacionesExpedienteJLabel = new javax.swing.JLabel();
        servicioExpedienteJTField = new javax.swing.JTextField();
        numExpedienteJTField = new javax.swing.JTextField();
        asuntoExpedienteJTField = new javax.swing.JTextField();
        fechaAperturaJTField = new javax.swing.JTextField();
        inicioJLabel = new javax.swing.JLabel();
        inicioJTField = new javax.swing.JTextField();
        responsableJLabel = new javax.swing.JLabel();
        silencioJCheckBox = new javax.swing.JCheckBox();
        buscarExpedienteJButton = new javax.swing.JButton();
        consultarJButton = new javax.swing.JButton();
        silencioJLabel = new javax.swing.JLabel();
        notaJLabel = new javax.swing.JLabel();
        observacionesExpedienteJScrollPane = new javax.swing.JScrollPane();
        estadoExpedienteJCBox = new javax.swing.JComboBox();

        obraMayorJPanel = new javax.swing.JPanel();
        datosSolicitudJPanel = new javax.swing.JPanel();
        tipoObraJLabel = new javax.swing.JLabel();
        unidadTJLabel = new javax.swing.JLabel();
        motivoJLabel = new javax.swing.JLabel();
        asuntoJLabel = new javax.swing.JLabel();
        fechaSolicitudJLabel = new javax.swing.JLabel();
        observacionesJLabel = new javax.swing.JLabel();
        unidadTJTField = new javax.swing.JTextField();
        fechaSolicitudJTField = new javax.swing.JTextField();
        observacionesJScrollPane = new javax.swing.JScrollPane();
        numRegistroJTField = new javax.swing.JTextField();
        numRegistroJLabel = new javax.swing.JLabel();
        unidadRJLabel = new javax.swing.JLabel();
        unidadRJTField = new javax.swing.JTextField();
        tasaJLabel = new javax.swing.JLabel();
        impuestoJLabel = new javax.swing.JLabel();
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
        emailPropietarioJTField = new javax.swing.JTextField();
        nombreViaPropietarioJTField = new javax.swing.JTextField();
        plantaPropietarioJTField = new javax.swing.JTextField();
        portalPropietarioJTField = new javax.swing.JTextField();
        escaleraPropietarioJTField = new javax.swing.JTextField();
        letraPropietarioJTField = new javax.swing.JTextField();
        municipioPropietarioJTField = new javax.swing.JTextField();
        provinciaPropietarioJTField = new javax.swing.JTextField();
        notificarPropietarioJCheckBox = new javax.swing.JCheckBox();
        notificarPropietarioJLabel = new javax.swing.JLabel();
        representanteJPanel = new javax.swing.JPanel();
        datosPersonalesRepresentanteJPanel = new javax.swing.JPanel();
        DNIRepresentanteJLabel = new javax.swing.JLabel();
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
        emailRepresentanteJTField = new javax.swing.JTextField();
        nombreViaRepresentanteJTField = new javax.swing.JTextField();
        plantaRepresentanteJTField = new javax.swing.JTextField();
        portalRepresentanteJTField = new javax.swing.JTextField();
        escaleraRepresentanteJTField = new javax.swing.JTextField();
        letraRepresentanteJTField = new javax.swing.JTextField();
        municipioRepresentanteJTField = new javax.swing.JTextField();
        provinciaRepresentanteJTField = new javax.swing.JTextField();
        notificarRepresentanteJCheckBox = new javax.swing.JCheckBox();
        notificarRepresentanteJLabel = new javax.swing.JLabel();
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
        generarFichaJButton = new javax.swing.JButton();
        botoneraJPanel = new javax.swing.JPanel();
        aceptarJButton = new javax.swing.JButton();
        publicarJButton = new javax.swing.JButton();
        cancelarJButton = new javax.swing.JButton();
        jButtonGenerarFicha= new javax.swing.JButton();
        jButtonWorkFlow= new javax.swing.JButton();
        editorMapaJPanel = new javax.swing.JPanel();

        /** documentacion */
        documentacionJPanel= new DocumentacionLicenciasJPanel(CMainLicencias.literales,CConstantesLicencias.LicenciasObraMayor);
        documentacionJPanel.setModificacion();
        jLabelEstadoActual=new javax.swing.JLabel();
        /** Informes **/
        jPanelInformes= new JPanelInformes(desktop, CMainLicencias.literales);
        /** tecnicos */
        tecnicosJPanel= new TecnicosJPanel(this.desktop, CMainLicencias.literales);
        tecnicosJPanel.setModificacion();
        /** Resolucion **/
        jPanelResolucion = new JPanelResolucion(desktop,CMainLicencias.literales);



        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        setClosable(true);
        setTitle("Modificaci\u00f3n de Licencia de Obra Mayor");
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



        obraMayorJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        obraMayorJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));
        jTabbedPaneSolicitud.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);


        expedienteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expedienteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Expediente"));
        expedienteJPanel.setAutoscrolls(true);
        estadoExpedienteJLabel.setText("Estado:");
        expedienteJPanel.add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 130, 20));

        numExpedienteJLabel.setText("Num. Expediente:");
        expedienteJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));

        servicioExpedienteJLabel.setText("Servicio Encargado:");
        expedienteJPanel.add(servicioExpedienteJLabel, new org. netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 130, 20));

        tramitacionJLabel.setText("Tipo de Tramitaci\u00f3n:");
        expedienteJPanel.add(tramitacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 130, 20));

        finalizaJLabel.setText("Finaliza por:");
        expedienteJPanel.add(finalizaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 555, -1, -1));

        asuntoExpedienteJLabel.setText("Asunto:");
        expedienteJPanel.add(asuntoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 130, 20));

        fechaAperturaJLabel.setText("Fecha Apertura:");
        expedienteJPanel.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 130, 20));

        observacionesExpedienteJLabel.setText("Observaciones:");
        expedienteJPanel.add(observacionesExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 170, 20));

        expedienteJPanel.add(servicioExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 300, -1));

        expedienteJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 280, -1));

        expedienteJPanel.add(asuntoExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 300, -1));

        fechaAperturaJTField.setEnabled(false);
        expedienteJPanel.add(fechaAperturaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 300, -1));

        inicioJLabel.setText("Forma de inicio:");
        expedienteJPanel.add(inicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 130, 20));

        expedienteJPanel.add(inicioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 210, 300, -1));

        responsableJLabel.setText("Responsable:");
        expedienteJPanel.add(responsableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 170, 20));

        expedienteJPanel.add(silencioJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 30, -1));

        buscarExpedienteJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        expedienteJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 20, 20));

        consultarJButton.setText("Consultar");
        consultarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarJButtonActionPerformed();
            }
        });

        expedienteJPanel.add(consultarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 130, -1));

        silencioJLabel.setText("Silencio administrativo:");
        expedienteJPanel.add(silencioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 160, 20));

        notaJLabel.setFont(new java.awt.Font("Arial", 0, 10));
        notaJLabel.setText("*Nota: Chequee para silencio administrativo positivo.");
        expedienteJPanel.add(notaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, 275, 20));

        observacionesExpedienteJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        expedienteJPanel.add(observacionesExpedienteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 290, 300, 90));

        estadoExpedienteJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comprobarResolucion();
            }
        });
        expedienteJPanel.add(estadoExpedienteJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 300, 20));

         obraMayorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder("Datos solicitud"));
        datosSolicitudJPanel.setAutoscrolls(true);
        tipoObraJLabel.setText("(*) Tipo Obra:");
        datosSolicitudJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 130, 20));

        unidadTJLabel.setText("Unidad Tramitadora:");
        datosSolicitudJPanel.add(unidadTJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 130, 20));

        motivoJLabel.setText("Motivo:");
        datosSolicitudJPanel.add(motivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 130, 20));

        asuntoJLabel.setText("Asunto:");
        datosSolicitudJPanel.add(asuntoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 130, 20));

        fechaSolicitudJLabel.setText("Fecha de Solicitud:");
        datosSolicitudJPanel.add(fechaSolicitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, 20));

        observacionesJLabel.setText("Observaciones:");
        datosSolicitudJPanel.add(observacionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 130, 20));

        datosSolicitudJPanel.add(unidadTJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 330, -1));

        fechaSolicitudJTField.setEnabled(false);
        datosSolicitudJPanel.add(fechaSolicitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 80, -1));

        observacionesJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 330, 90));

        numRegistroJTField.setEnabled(false);
        datosSolicitudJPanel.add(numRegistroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 330, -1));

        numRegistroJLabel.setText("N\u00famero de Registro:");
        datosSolicitudJPanel.add(numRegistroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        unidadRJLabel.setText("Unidad de Registro:");
        datosSolicitudJPanel.add(unidadRJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, 20));

        datosSolicitudJPanel.add(unidadRJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 330, -1));

        tasaJLabel.setText("Tasa:");
        datosSolicitudJPanel.add(tasaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 165, 130, 20));

        impuestoJLabel.setText("Impuesto:");
        datosSolicitudJPanel.add(impuestoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 185, 130, 20));

        obraMayorJPanel.add(datosSolicitudJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 310));

        emplazamientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder("Emplazamiento"));
        nombreViaJLabel.setText("Tipo v\u00eda / Nombre V\u00eda:");
        emplazamientoJPanel.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 20));

        numeroViaJLabel.setText("N\u00ba/Portal/Planta/Letra:");
        emplazamientoJPanel.add(numeroViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 130, 20));

        cPostalJLabel.setText("C.P. / Municipio: ");
        emplazamientoJPanel.add(cPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 130, 20));

        provinciaJLabel.setText("Provincia:");
        emplazamientoJPanel.add(provinciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 130, 20));

        municipioJTField.setEnabled(false);
        emplazamientoJPanel.add(municipioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 240, -1));

        provinciaJTField.setEnabled(false);
        emplazamientoJPanel.add(provinciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 330, -1));

        nombreViaJButton.setIcon(new javax.swing.ImageIcon(""));
        nombreViaJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        nombreViaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        nombreViaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreViaJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(nombreViaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 20, 20));

        refCatastralJLabel.setText("Referencia catastral:");
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

        referenciasCatastralesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        referenciasCatastralesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                referenciasCatastralesJTableMouseClicked();
            }
        });

        referenciasCatastralesJScrollPane.setViewportView(referenciasCatastralesJTable);

        emplazamientoJPanel.add(referenciasCatastralesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 135, 470, 90));

        mapToTableJButton.setIcon(new javax.swing.ImageIcon(""));
        mapToTableJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mapToTableJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        mapToTableJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapToTableJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(mapToTableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 135, 20, 20));

        tableToMapJButton.setIcon(new javax.swing.ImageIcon(""));
        tableToMapJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableToMapJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        tableToMapJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableToMapJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(tableToMapJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 155, 20, 20));

        deleteParcelaJButton.setIcon(new javax.swing.ImageIcon(""));
        deleteParcelaJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        deleteParcelaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        deleteParcelaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteParcelaJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(deleteParcelaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 205, 20, 20));

        obraMayorJPanel.add(emplazamientoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 520, 237));

        propietarioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPropietarioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNIPropietarioJLabel.setText("(*) DNI/CIF:");
        datosPersonalesPropietarioJPanel.add(DNIPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        nombrePropietarioJLabel.setText("(*) Nombre:");
        datosPersonalesPropietarioJPanel.add(nombrePropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 20));

        datosPersonalesPropietarioJPanel.add(nombrePropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 300, -1));

        apellido1PropietarioJLabel.setText("Apellido1:");
        datosPersonalesPropietarioJPanel.add(apellido1PropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 130, 20));

        apellido2PropietarioJLabel2.setText("Apellido2:");
        datosPersonalesPropietarioJPanel.add(apellido2PropietarioJLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 94, 130, 20));

        datosPersonalesPropietarioJPanel.add(apellido1PropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 72, 300, -1));

        datosPersonalesPropietarioJPanel.add(apellido2PropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 94, 300, -1));

        buscarDNIPropietarioJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNIPropietarioJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIPropietarioJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIPropietarioJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIPropietarioJButtonActionPerformed();
            }
        });


        datosPersonalesPropietarioJPanel.add(buscarDNIPropietarioJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 20, 20));

        propietarioJPanel.add(datosPersonalesPropietarioJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 132));

        datosNotificacionPropietarioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionPropietarioJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionPropietarioJPanel.add(viaNotificacionPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        faxPropietarioJLabel.setText("Fax:");
        datosNotificacionPropietarioJPanel.add(faxPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, 130, 20));

        telefonoPropietarioJLabel.setText("Tel\u00e9fono:");
        datosNotificacionPropietarioJPanel.add(telefonoPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 130, 20));

        movilPropietarioJLabel.setText("M\u00f3vil:");
        datosNotificacionPropietarioJPanel.add(movilPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 86, 130, 20));

        emailPropietarioJLabel.setText("Email:");
        datosNotificacionPropietarioJPanel.add(emailPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 108, 130, 20));

        tipoViaPropietarioJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionPropietarioJPanel.add(tipoViaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, 20));

        nombreViaPropietarioJLabel.setText("(*) Nombre:");
        datosNotificacionPropietarioJPanel.add(nombreViaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 162, 130, 20));

        numeroViaPropietarioJLabel.setText("(*) N\u00famero:");
        datosNotificacionPropietarioJPanel.add(numeroViaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 184, 90, 20));

        portalPropietarioJLabel.setText("Portal:");
        datosNotificacionPropietarioJPanel.add(portalPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 50, 20));

        plantaPropietarioJLabel.setText("Planta:");
        datosNotificacionPropietarioJPanel.add(plantaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 206, 70, 20));

        escaleraPropietarioJLabel.setText("Escalera:");
        datosNotificacionPropietarioJPanel.add(escaleraPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 272, 60, 20));

        letraPropietarioJLabel.setText("Letra:");
        datosNotificacionPropietarioJPanel.add(letraPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 228, 40, 20));

        cPostalPropietarioJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionPropietarioJPanel.add(cPostalPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 294, 130, 20));

        municipioPropietarioJLabel.setText("(*) Municipio:");
        datosNotificacionPropietarioJPanel.add(municipioPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 316, 130, 20));

        provinciaPropietarioJLabel.setText("(*) Provincia:");
        datosNotificacionPropietarioJPanel.add(provinciaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 338, 130, 20));

        datosNotificacionPropietarioJPanel.add(emailPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 108, 300, -1));

        datosNotificacionPropietarioJPanel.add(nombreViaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 162, 300, -1));

        datosNotificacionPropietarioJPanel.add(plantaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 206, 150, -1));

        datosNotificacionPropietarioJPanel.add(portalPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 150, -1));

        datosNotificacionPropietarioJPanel.add(escaleraPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 272, 150, -1));

        datosNotificacionPropietarioJPanel.add(letraPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 228, 150, -1));

        datosNotificacionPropietarioJPanel.add(municipioPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 316, 300, -1));

        datosNotificacionPropietarioJPanel.add(provinciaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 338, 300, -1));

        notificarPropietarioJCheckBox.setSelected(true);
        notificarPropietarioJCheckBox.setEnabled(false);
        datosNotificacionPropietarioJPanel.add(notificarPropietarioJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 370, 70, -1));

        notificarPropietarioJLabel.setText("Notificar propietario:");
        datosNotificacionPropietarioJPanel.add(notificarPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 150, 20));

        propietarioJPanel.add(datosNotificacionPropietarioJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 132, 520, 415));

        representanteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentanteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNIRepresentanteJLabel.setText("(*) DNI/CIF:");
        datosPersonalesRepresentanteJPanel.add(DNIRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        nombreRepresentanteJLabel.setText("(*) Nombre:");
        datosPersonalesRepresentanteJPanel.add(nombreRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 20));

        datosPersonalesRepresentanteJPanel.add(nombreRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 300, -1));

        apellido1RepresentanteJLabel.setText("Apellido1:");
        datosPersonalesRepresentanteJPanel.add(apellido1RepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 130, 20));

        apellido2RepresentanteJLabel.setText("Apellido2:");
        datosPersonalesRepresentanteJPanel.add(apellido2RepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 94, 130, 20));

        datosPersonalesRepresentanteJPanel.add(apellido1RepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 72, 300, -1));

        datosPersonalesRepresentanteJPanel.add(apellido2RepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 94, 300, -1));

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

        representanteJPanel.add(datosPersonalesRepresentanteJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 132));

        datosNotificacionRepresentanteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionRepresentanteJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionRepresentanteJPanel.add(viaNotificacionRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        faxRepresentanteJLabel.setText("Fax:");
        datosNotificacionRepresentanteJPanel.add(faxRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, 130, 20));

        telefonoRepresentanteJLabel.setText("Tel\u00e9fono:");
        datosNotificacionRepresentanteJPanel.add(telefonoRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 130, 20));

        movilRepresentanteJLabel.setText("M\u00f3vil:");
        datosNotificacionRepresentanteJPanel.add(movilRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 86, 130, 20));

        emailRepresentanteJLabel.setText("Email:");
        datosNotificacionRepresentanteJPanel.add(emailRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 108, 130, 20));

        tipoViaRepresentanteJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionRepresentanteJPanel.add(tipoViaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, 20));

        nombreViaRepresentanteJLabel.setText("(*) Nombre:");
        datosNotificacionRepresentanteJPanel.add(nombreViaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 162, 100, 20));

        numeroViaRepresentanteJLabel.setText("(*) N\u00famero:");
        datosNotificacionRepresentanteJPanel.add(numeroViaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 184, 90, 20));

        portalRepresentanteJLabel.setText("Portal:");
        datosNotificacionRepresentanteJPanel.add(portalRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 50, 20));

        plantaRepresentanteJLabel.setText("Planta:");
        datosNotificacionRepresentanteJPanel.add(plantaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 206, 70, 20));

        escaleraRepresentanteJLabel.setText("Escalera:");
        datosNotificacionRepresentanteJPanel.add(escaleraRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 272, 60, 20));

        letraRepresentanteJLabel.setText("Letra:");
        datosNotificacionRepresentanteJPanel.add(letraRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 228, 40, 20));

        cPostalRepresentanteJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionRepresentanteJPanel.add(cPostalRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 294, 130, 20));

        municipioRepresentanteJLabel.setText("(*) Municipio:");
        datosNotificacionRepresentanteJPanel.add(municipioRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 316, 130, 20));

        provinciaRepresentanteJLabel.setText("(*) Provincia:");
        datosNotificacionRepresentanteJPanel.add(provinciaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 338, 130, 20));

        datosNotificacionRepresentanteJPanel.add(emailRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 108, 300, -1));

        datosNotificacionRepresentanteJPanel.add(nombreViaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 162, 300, -1));

        datosNotificacionRepresentanteJPanel.add(plantaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 206, 150, -1));

        datosNotificacionRepresentanteJPanel.add(portalRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 150, -1));

        datosNotificacionRepresentanteJPanel.add(escaleraRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 272, 150, -1));

        datosNotificacionRepresentanteJPanel.add(letraRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 228, 150, -1));


        datosNotificacionRepresentanteJPanel.add(municipioRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 316, 300, 20));

        datosNotificacionRepresentanteJPanel.add(provinciaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 338, 300, 20));

        datosNotificacionRepresentanteJPanel.add(notificarRepresentanteJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 370, 60, -1));

        notificarRepresentanteJLabel.setText("Notificar representante:");
        datosNotificacionRepresentanteJPanel.add(notificarRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 150, 20));

        representanteJPanel.add(datosNotificacionRepresentanteJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 132, 520, 415));

        promotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNIPromotorJLabel.setText("(*) DNI/CIF:");
        datosPersonalesPromotorJPanel.add(DNIPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 20));

        nombrePromotorJLabel.setText("(*) Nombre:");
        datosPersonalesPromotorJPanel.add(nombrePromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 20));

        datosPersonalesPromotorJPanel.add(nombrePromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 300, -1));

        apellido1PromotorJLabel.setText("Apellido1:");
        datosPersonalesPromotorJPanel.add(apellido1PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 71, 130, 20));

        apellido2PromotorJLabel.setText("Apellido2:");
        datosPersonalesPromotorJPanel.add(apellido2PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 92, 130, 20));

        datosPersonalesPromotorJPanel.add(apellido1PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 71, 300, -1));

        datosPersonalesPromotorJPanel.add(apellido2PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 92, 300, -1));

        buscarDNIPromotorJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarDNIPromotorJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIPromotorJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIPromotorJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIPromotorJButtonActionPerformed();
            }
        });

        datosPersonalesPromotorJPanel.add(buscarDNIPromotorJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 20, 20));

        colegioPromotorJLabel.setText("(*) Colegio:");
        datosPersonalesPromotorJPanel.add(colegioPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 108, 130, 20));

        visadoPromotorJLabel.setText("(*) Visado:");
        datosPersonalesPromotorJPanel.add(visadoPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 129, 130, 20));

        titulacionPromotorJLabel.setText("Titulaci\u00f3n:");
        datosPersonalesPromotorJPanel.add(titulacionPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 130, 20));

        datosPersonalesPromotorJPanel.add(colegioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 108, 300, -1));

        datosPersonalesPromotorJPanel.add(visadoPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 129, 300, -1));

        datosPersonalesPromotorJPanel.add(titulacionPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, 300, -1));

        promotorJPanel.add(datosPersonalesPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 180));

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

        datosNotificacionPromotorJPanel.add(emailPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 104, 300, -1));

        datosNotificacionPromotorJPanel.add(nombreViaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 151, 300, -1));

        datosNotificacionPromotorJPanel.add(plantaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 193, 150, -1));

        datosNotificacionPromotorJPanel.add(portalPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 235, 150, -1));

        datosNotificacionPromotorJPanel.add(escaleraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 256, 150, -1));

        datosNotificacionPromotorJPanel.add(letraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 214, 150, -1));

        datosNotificacionPromotorJPanel.add(municipioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 298, 300, -1));

        datosNotificacionPromotorJPanel.add(provinciaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 319, 300, -1));

        notificarPromotorJCheckBox.setVisible(false);
        datosNotificacionPromotorJPanel.add(notificarPromotorJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 340, 80, 20));

        notificarPromotorJLabel.setText("Notificar promotor:");
        notificarPromotorJLabel.setVisible(false);
        datosNotificacionPromotorJPanel.add(notificarPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 150, 20));

        promotorJPanel.add(datosNotificacionPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 520, 367));

        notificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder("Notificaciones"));
        notificacionesJScrollPane.setEnabled(false);
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
        datosNombreApellidosJLabel.setText("Nombre y Apellidos:");
        datosNotificacionJPanel.add(datosNombreApellidosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 180, 20));

        datosDireccionJLabel.setText("Direcci\u00f3n:");
        datosNotificacionJPanel.add(datosDireccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 180, 20));

        datosCPostalJLabel.setText("C.Postal/Municipio/Provincia:");
        datosNotificacionJPanel.add(datosCPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 180, 20));

        datosNotificarPorJLabel.setText("Notificar Por:");
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

        entregadaAJLabel.setText("Entregada A:");
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

        datosNotificacionesJPanel.add(datosNotificacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 500, 200));

        notificacionesJPanel.add(datosNotificacionesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 580));

        eventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosEventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosEventosJPanel.setBorder(new javax.swing.border.TitledBorder("Eventos"));
        eventosJScrollPane.setBorder(null);
        eventosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
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

        datosEventosJPanel.add(descEventoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 500, 100));

        eventosJPanel.add(datosEventosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 580));

        historicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosHistoricoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosHistoricoJPanel.setBorder(new javax.swing.border.TitledBorder("Hist\u00f3rico"));
        historicoJScrollPane.setBorder(null);
        historicoJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
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

        datosHistoricoJPanel.add(apunteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 460, 90));

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

        generarFichaJButton.setIcon(new javax.swing.ImageIcon(""));
        generarFichaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        generarFichaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        generarFichaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarFichaJButtonActionPerformed();
            }
        });

        datosHistoricoJPanel.add(generarFichaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 210, 20, 20));

        historicoJPanel.add(datosHistoricoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 580));

        templateJPanel.setLayout(new BorderLayout());
        botoneraJPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));

        //templateJPanel.add(obraMayorJTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 590));
        templateJPanel.add(obraMayorJTabbedPane, BorderLayout.WEST);

        aceptarJButton.setText("Modificar");
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });
        aceptarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 130, -1));

        publicarJButton.setText("Publicar");
        publicarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publicarJButtonActionPerformed();
            }
        });
        publicarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(publicarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 130, -1));

        jButtonGenerarFicha.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                       generarFicha();
                   }
        });
        jButtonGenerarFicha.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(jButtonGenerarFicha, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 90, -1));

        jButtonWorkFlow.addActionListener(new java.awt.event.ActionListener() {
                   public void actionPerformed(java.awt.event.ActionEvent evt) {
                               verWorkFlow();
                           }
         });
         jButtonWorkFlow.setPreferredSize(new Dimension(120,30));
         botoneraJPanel.add(jButtonWorkFlow, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 90, -1));

        cancelarJButton.setPreferredSize(new Dimension(120,30));
        cancelarJButton.setText("Cancelar");
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        botoneraJPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 130, -1));

        //templateJPanel.add(botoneraJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 560, 40));
        templateJPanel.add(botoneraJPanel, BorderLayout.SOUTH);

        editorMapaJPanel.setLayout(new java.awt.GridLayout(1, 0));

        editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder("Mapa"));
        //templateJPanel.add(editorMapaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 12, 480, 580));
        templateJPanel.add(editorMapaJPanel, BorderLayout.CENTER);
        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane);

    }//GEN-END:initComponents

    private void formComponentShown() {//GEN-FIRST:event_formComponentShown
        if (!fromMenu){
            consultarJButtonActionPerformed();
        }

    }//GEN-LAST:event_formComponentShown


    private void generarFichaJButtonActionPerformed() {//GEN-FIRST:event_generarFichaJButtonActionPerformed
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
                    selectedFile,CMainLicencias.literales);
        }

    }//GEN-LAST:event_generarFichaJButtonActionPerformed

    private void historicoJTableMouseDragged() {//GEN-FIRST:event_historicoJTableMouseDragged
        mostrarHistoricoSeleccionado();
    }//GEN-LAST:event_historicoJTableMouseDragged

    private void historicoJTableFocusGained() {//GEN-FIRST:event_historicoJTableFocusGained
        mostrarHistoricoSeleccionado();
    }//GEN-LAST:event_historicoJTableFocusGained
    private void historicoJTableKeyTyped() {
        mostrarHistoricoSeleccionado();
    }
    private void historicoJTableKeyPressed(){
        mostrarHistoricoSeleccionado();
    }
    private void historicoJTableKeyReleased(){
        mostrarHistoricoSeleccionado();
    }
    
    private void eventosJTableFocusGained() {//GEN-FIRST:event_eventosJTableFocusGained
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_eventosJTableFocusGained

    private void eventosJTableMouseDragged() {//GEN-FIRST:event_eventosJTableMouseDragged
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_eventosJTableMouseDragged

    private void eventosJTableKeyTyped() {
        mostrarEventoSeleccionado();
    }
    private void eventosJTableKeyPressed(){
        mostrarEventoSeleccionado();
    }
    private void eventosJTableKeyReleased(){
        mostrarEventoSeleccionado();
    }


    private void notificacionesJTableMouseDragged() {//GEN-FIRST:event_notificacionesJTableMouseDragged
        mostrarDatosNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableMouseDragged

    private void notificacionesJTableFocusGained() {//GEN-FIRST:event_notificacionesJTableFocusGained
        mostrarDatosNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableFocusGained

    private void notificacionesJTableKeyTyped(){
       mostrarDatosNotificacionSeleccionada();
    }
    public void notificacionesJTableKeyPressed() {
        mostrarDatosNotificacionSeleccionada();
    }
    public void notificacionesJTableKeyReleased() {
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


    private void buscarDNIRepresentanteJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIRepresentanteJButtonActionPerformed
        CUtilidadesComponentes.showPersonaDialog(desktop,CMainLicencias.literales);
         if ((CConstantesLicencias_LCGIII.persona != null) && (CConstantesLicencias_LCGIII.persona.getDniCif() != null)) {
            if ((CConstantesLicencias_LCGIII.persona != null) && (CConstantesLicencias_LCGIII.persona.getDniCif() != null)) {

                DNIRepresentanteTField.setText(CConstantesLicencias_LCGIII.persona.getDniCif());
                nombreRepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getNombre());
                apellido1RepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido1());
                apellido2RepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido2());
                faxRepresentanteTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getFax());
                telefonoRepresentanteTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTelefono());
                movilRepresentanteTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMovil());
                emailRepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEmail());
                nombreViaRepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNombreVia());
                numeroViaRepresentanteTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNumeroVia());
                plantaRepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPlanta());
                portalRepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPortal());
                escaleraRepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEscalera());
                letraRepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getLetra());
                try{
                    tipoViaNotificacionRepresentanteEJCBox.setSelectedPatron(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
                }catch (Exception e){
                    DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
                    if (auxNode!=null)
                            tipoViaNotificacionRepresentanteEJCBox.setSelected(auxNode.getIdNode());
                }
                try{
                    viaNotificacionRepresentanteEJCBox.setSelectedPatron(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                }catch (Exception e){
                    DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                    if (auxNode!=null)
                            viaNotificacionRepresentanteEJCBox.setSelected(auxNode.getIdNode());
                }

                try{
                    cPostalRepresentanteTField.setNumber(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalRepresentanteTField.setText("");}
                municipioRepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMunicipio());
                provinciaRepresentanteJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getProvincia());
                if (CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                    notificarRepresentanteJCheckBox.setSelected(true);
                else notificarRepresentanteJCheckBox.setSelected(false);

            }

        }

        CConstantesLicencias_LCGIII.persona= null;


    }//GEN-LAST:event_buscarDNIRepresentanteJButtonActionPerformed

    private void borrarRepresentanteJButtonActionPerformed() {//GEN-FIRST:event_borrarRepresentanteJButtonActionPerformed
        if (CUtilidadesComponentes.hayDatosPersonaJuridicoFisica(DNIRepresentanteTField.getText().trim(),
                nombreRepresentanteJTField.getText().trim(),
                nombreViaRepresentanteJTField.getText().trim(),
                numeroViaRepresentanteTField.getText().trim(),
                cPostalRepresentanteTField.getText().trim(),
                municipioRepresentanteJTField.getText().trim(),
                provinciaRepresentanteJTField.getText().trim())){
            /** mostramos ventana de confirmacion */
            int ok= JOptionPane.showConfirmDialog(obraMayorJPanel, CMainLicencias.literales.getString("CModificacionLicenciasForm.representanteJPanel.borrarRepresentanteJButton.Message"), CMainLicencias.literales.getString("CModificacionLicenciasForm.representanteJPanel.borrarRepresentanteJButton.tittle"), JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION){
                borrarCamposRepresentante();
                if (_solicitud.getRepresentante() != null){
                    _solicitud.setIdRepresentanteToDelete(_solicitud.getRepresentante().getIdPersona());
                }
                CConstantesLicencias_LCGIII.representante= null;
            }
        }else{
            borrarCamposRepresentante();
            CConstantesLicencias_LCGIII.representante= null;
        }
    }//GEN-LAST:event_borrarRepresentanteJButtonActionPerformed

    private void fechaLimiteObraJButtonActionPerformed() {//GEN-FIRST:event_fechaLimiteObraJButtonActionPerformed
        CUtilidadesComponentes.showCalendarDialog(desktop);
        if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
            fechaLimiteObraJTField.setText(CConstantesLicencias.calendarValue);
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
                    CConstantesLicencias_LCGIII.referencia.setTipoVia(((DomainNode)objeto).getTerm(literales.getLocale().toString()));
                    patron=((DomainNode)objeto).getPatron();
                }
                if ((objeto instanceof String) && objeto!=null)
                {
                    if (((String)objeto).length()>0)
                    {
                        CConstantesLicencias_LCGIII.referencia.setTipoVia(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto).getTerm(literales.getLocale().toString()));
                        patron=((String)objeto);
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
                    mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.historicoTab.mensaje1"));
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
        nuevo.setUsuario(CConstantesLicencias_LCGIII.principal.getName());
        nuevo.setFechaHistorico(CUtilidades.getDate(CUtilidades.showToday()));
        nuevo.setApunte("");
        nuevo.setSistema("0");
        nuevo.setHasBeen(CConstantesLicencias.CMD_HISTORICO_ADDED);

		CConstantesLicencias.OPERACION_HISTORICO= CConstantesLicencias.CMD_HISTORICO_ADDED;
		CHistoricoJDialog historicoJDialog= CUtilidadesComponentes.showHistoricoDialog(desktop, nuevo, CMainLicencias.literales);

        CHistorico historico= historicoJDialog.getHistorico();
		/** comprobamos que operacion se ha llevado a cabo con el historico */
		if (historico.getHasBeen() != -1) {
			_historicoExpedienteTableModel.addRow(new Object[]{((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(CMainLicencias.currentLocale.toString()),
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
            row = ((Integer)historicoSorted.getValueAt(row, historicoHiddenCol)).intValue();
            CHistorico historico= (CHistorico)vAuxiliar.get(row);
            if (historico != null){
                long idHistorico= historico.getIdHistorico();
                if (idHistorico == -1){
                    /** se modifica un historico nuevo */
                }else{
                    if (!historico.getSistema().equals("1")) {
                    } else {
                        mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.historicoTab.mensaje2"));
                        return;
                    }
                }

            }

            CConstantesLicencias.OPERACION_HISTORICO= CConstantesLicencias.CMD_HISTORICO_MODIFIED;
            CHistoricoJDialog historicoJDialog= CUtilidadesComponentes.showHistoricoDialog(desktop, historico, CMainLicencias.literales);
            CHistorico h= historicoJDialog.getHistorico();
            _historicoExpedienteTableModel.setValueAt(h.getApunte(), row, 3);
            apunteJTArea.setText(h.getApunte());

            /** Annadimos el historico modificado */
            vAuxiliar.removeElementAt(row);
            historico.setApunte(h.getApunte());
            vAuxiliar.add(row, historico);
        }
    }//GEN-LAST:event_modHistoricoJButtonActionPerformed


	private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed

        CUtilidadesComponentes.showSearchDialog(desktop, new Integer(CConstantesLicencias.ObraMayor).toString(), "-1", "-1", "", false, CMainLicencias.literales);

        if ((numExpedienteJTField.getText() != null) && (numExpedienteJTField.getText().trim().length() > 0)){
            if ((CConstantesLicencias.searchValue != null) && (CConstantesLicencias.searchValue.trim().length() > 0)){
                /** se ha aceptado en el dialogo de busqueda */
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
        CTipoLicencia tipoLicencia= new CTipoLicencia(new Integer(CConstantesLicencias.LicenciasObraMayor).intValue(), "", "");
        tiposLicencia.add(tipoLicencia);
		CResultadoOperacion ro= COperacionesLicencias.getExpedienteLicencia(_numExpediente, CMainLicencias.currentLocale, tiposLicencia);

		if (ro != null) {
			if ((ro.getSolicitudes() != null) && (ro.getExpedientes() != null)) {
				_consultaOK = true;
				_solicitud = (CSolicitudLicencia) ro.getSolicitudes().get(0);
				_expediente = (CExpedienteLicencia) ro.getExpedientes().get(0);

                CConstantesLicencias_LCGIII.persona= null;
                CConstantesLicencias_LCGIII.representante= null;
                CConstantesLicencias_LCGIII.tecnico= null;
                CConstantesLicencias_LCGIII.promotor= null;

				if ((_solicitud != null) && (_expediente != null)) {

                    if (_expediente.bloqueado()){
                        /** Comprobamos si ya esta bloqueado por el usuario */
                        if ((expedienteAnterior == null) || ((expedienteAnterior != null) && (expedienteAnterior.getNumExpediente() != null) && (!expedienteAnterior.getNumExpediente().equalsIgnoreCase(_numExpediente)))){
                            /** Expediente bloqueado por otro usuario */
                            if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, CMainLicencias.literales, fromMenu) != 0){
                                _expediente= expedienteAnterior;
                                _solicitud= solicitudAnterior;

                                if (_expediente != null){
                                    numExpedienteJTField.setText(_expediente.getNumExpediente());
                                }else numExpedienteJTField.setText("");

                                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                return;
                            }
                            else{
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
                                CUtilidadesComponentes.mostrarMensajeBloqueoAceptacion(this, CMainLicencias.literales.getString("Licencias.mensaje12"), CMainLicencias.literales);
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
                    jLabelEstadoActual.setText(((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(_expediente.getEstado().getIdEstado()).toString())).getTerm(CMainLicencias.currentLocale));

                    /** Titular */
					rellenarPropietario();
					/** Representado */
					rellenarRepresentante();
					/** Tecnicos */
                    tecnicosJPanel.load(_expediente,_solicitud);
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
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.solicitudJPanel.mensaje1"));
					clearScreen();
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}
			} else {
				_consultaOK = false;
				logger.warn("No existen resultados de operacion para el expediente " + _numExpediente);
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.solicitudJPanel.mensaje1"));
				clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}
		} else {
			_consultaOK = false;
			logger.warn("No existen resultados de operacion para el expediente " + _numExpediente);
			mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.solicitudJPanel.mensaje1"));
			clearScreen();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return;
		}

	}//GEN-LAST:event_consultarJButtonActionPerformed

	private void cancelarJButtonActionPerformed() {//GEN-FIRST:event_cancelarJButtonActionPerformed
		CConstantesLicencias.helpSetHomeID = "licenciasIntro";
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
        /** Desbloqueo de expediente */
        desbloquearExpediente();
        this.dispose();
	}//GEN-LAST:event_cancelarJButtonActionPerformed

	private void aceptarJButtonActionPerformed() {//GEN-FIRST:event_aceptarJButtonActionPerformed
		if (_consultaOK) {
            /** Volvemos a preguntar al usuario si quiere modificar un expediente bloqueado por otro usuario */
            if (!_expediente.bloqueaUsuario()){
                if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, CMainLicencias.literales, fromMenu) != 0){
                    return;
                }
            }

            // El chequeo de los tecnicos se realiza en TecnicosJPanel. Comprobamos que en la modificacion
            // no se hayan borrado los tecnicos
			if (rellenaCamposObligatorios() && tecnicosJPanel.hayTecnicos()) {
				/** Comprobamos los datos de entrada */

				/** Datos de las personas juridico fisicas */
				if (datosPropietarioOK() && datosRepresentanteOK() && datosPromotorOK()) {
					/** Datos de la solicitud */
					CTipoObra tipoObra = new CTipoObra(new Integer(tipoObraEJCBox.getSelectedPatron()).intValue(), "", "");

                    double tasa = 0.00;
                    try {
                        tasa= tasaTextField.getNumber().doubleValue();
                    } catch (Exception ex) {
                        logger.warn("Tasa no válida. tasaTextField.getText(): " + tasaTextField.getText());
                    }

                    double impuesto = 0.00;
                    try {
                        impuesto= impuestoTextField.getNumber().doubleValue();
                    } catch (Exception ex) {
                        logger.warn("Impuesto no válida. impuestoTextField.getText(): " + impuestoTextField.getText());
                    }

					_unidadRegistro = unidadRJTField.getText();
					_unidadTramitadora = unidadTJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_unidadRegistro, CConstantesLicencias.MaxLengthUnidad) || CUtilidades.excedeLongitud(_unidadTramitadora, CConstantesLicencias.MaxLengthUnidad)) {
						mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.SolicitudTab.mensaje1"));
						return;
					}

					_motivo = motivoTField.getText().trim();
					if (CUtilidades.excedeLongitud(_motivo, CConstantesLicencias.MaxLengthMotivo)) {
						mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.SolicitudTab.mensaje2"));
						return;
					}
					_asunto = asuntoSolicitudTField.getText().trim();
					if (CUtilidades.excedeLongitud(_asunto, CConstantesLicencias.MaxLengthAsunto)) {
						mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.SolicitudTab.mensaje3"));
						return;
					}

					_observaciones = observacionesSolicitudTPane.getText().trim();


					/** Expediente */
					CEstado estado = (CEstado) _hEstados.get(new Integer(estadoExpedienteJCBox.getSelectedIndex()));
					String silencio = "0";
					if (silencioJCheckBox.isSelected()) silencio = "1";

					_servicioEncargado = servicioExpedienteJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_servicioEncargado, CConstantesLicencias.MaxLengthServicioEncargado)) {
						mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.ExpedienteTab.mensaje1"));
						return;
					}

					_asuntoExpediente = asuntoExpedienteJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_asuntoExpediente, CConstantesLicencias.MaxLengthAsuntoExpediente)) {
						mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.ExpedienteTab.mensaje2"));
						return;
					}

					/** No es editable */
					_fechaApertura = fechaAperturaJTField.getText();
					Date dateApertura = CUtilidades.parseFechaStringToDate(_fechaApertura);


					_formaInicio = inicioJTField.getText().trim();
					if (CUtilidades.excedeLongitud(_formaInicio, CConstantesLicencias.MaxLengthFormaInicio)) {
						mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.ExpedienteTab.mensaje3"));
						return;
					}

					_responsableExpediente = responsableTField.getText().trim();

					_observacionesExpediente= observacionesExpedienteTPane.getText().trim();

					// Recogemos la lista de referencias catastrales de la solicitud
					final Vector vRefCatastrales = new Vector();
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
					if ((_seNotificaRepresentante == 0) && (_seNotificaPromotor == 0))
						_seNotificaPropietario = 1;

                    String fax= "";
                    try{fax= faxPropietarioTField.getNumber().toString();}catch(Exception e){}
                    String telefono= "";
                    try{telefono= telefonoPropietarioTField.getNumber().toString();}catch (Exception e){}
                    String movil= "";
                    try{movil=  movilPropietarioTField.getNumber().toString();}catch(Exception e){}
                    String numero= "";
                    try{numero= numeroViaPropietarioTField.getNumber().toString();}catch(Exception e){}
                    String cPostal= "";
                    try{cPostal= cPostalPropietarioTField.getNumber().toString();}catch(Exception e){}
					CDatosNotificacion datosNotificacion = new CDatosNotificacion(_DNI_CIF_Propietario, viaNotificacion, fax, telefono, movil, _emailPropietario, _tipoViaPropietario, _nombreViaPropietario, numero, _portalPropietario, _plantaPropietario, _escaleraPropietario, _letraPropietario, cPostal, _municipioPropietario, _provinciaPropietario, new Integer(_seNotificaPropietario).toString());
					CPersonaJuridicoFisica propietario = new CPersonaJuridicoFisica(_DNI_CIF_Propietario, _nombrePropietario, _apellido1Propietario, _apellido2Propietario, "", "", "", datosNotificacion);
                    if (_solicitud.getPropietario() != null){
					    propietario.setIdPersona(_solicitud.getPropietario().getIdPersona());
                    }

					/** Representante */
					CPersonaJuridicoFisica representante = new CPersonaJuridicoFisica();
                    viaNotificacionIndex = new Integer(viaNotificacionRepresentanteEJCBox.getSelectedPatron()).intValue();
                    viaNotificacion = new CViaNotificacion(viaNotificacionIndex, "");
                    _tipoViaRepresentante= tipoViaNotificacionRepresentanteEJCBox.getSelectedPatron();

                    fax= "";
                    try{fax= faxRepresentanteTField.getNumber().toString();}catch(Exception e){}
                    telefono= "";
                    try{telefono= telefonoRepresentanteTField.getNumber().toString();}catch (Exception e){}
                    movil= "";
                    try{movil=  movilRepresentanteTField.getNumber().toString();}catch(Exception e){}
                    numero= "";
                    try{numero= numeroViaRepresentanteTField.getNumber().toString();}catch(Exception e){}
                    cPostal= "";
                    try{cPostal= cPostalRepresentanteTField.getNumber().toString();}catch(Exception e){}
					datosNotificacion = new CDatosNotificacion(_DNI_CIF_Representante, viaNotificacion, fax, telefono, movil, _emailRepresentante, _tipoViaRepresentante, _nombreViaRepresentante, numero, _portalRepresentante, _plantaRepresentante, _escaleraRepresentante, _letraRepresentante, cPostal, _municipioRepresentante, _provinciaRepresentante, new Integer(_seNotificaRepresentante).toString());
					representante = new CPersonaJuridicoFisica(_DNI_CIF_Representante, _nombreRepresentante, _apellido1Representante, _apellido2Representante, "", "", "", datosNotificacion);

					if ((_solicitud.getRepresentante() != null) && (_solicitud.getIdRepresentanteToDelete() == -1)) {
                        /** el expediente ya tiene representante. Puede ocurrir:
                         * 1.- se ha modificado alguno de sus datos
                         * 2.- no se ha borrado, pero se ha introducido a mano el DNI (por lo que suponemos que no existe en BD)
                         */
                        if (_solicitud.getRepresentante().getDniCif().equalsIgnoreCase(_DNI_CIF_Representante)){
                            /** caso 1 */
                            //System.out.println("************** 0.1- Se ha modificado algun dato de ID="+_solicitud.getRepresentante().getIdPersona());
                            representante.setIdPersona(_solicitud.getRepresentante().getIdPersona());
                        }else{
                            /** caso 2 */
                            //System.out.println("************** 0.2- no se ha borrado, pero se ha introducido a mano el DNI");
                            representante.setIdPersona(-1);
                        }
					}else if ((_solicitud.getRepresentante() != null) && (_solicitud.getIdRepresentanteToDelete() != -1)){
                        /** La solicitud tiene representante y se ha borrado. Puede ocurrir:
                        * 1.- se asigne un representante de BD
                        * 2.- se asigne uno nuevo que no existe en BD
                        * 3.- no se asigna representante
                        */
                        if ((CConstantesLicencias_LCGIII.representante != null) || (_DNI_CIF_Representante.trim().length() > 0)){
                            if (CConstantesLicencias_LCGIII.representante != null){
                                /** caso 1 */
                               //System.out.println("************** 1.- Se ha asignado un representante de BD ID="+CConstantesLicencias_LCGIII.representante.getIdPersona());
                               representante.setIdPersona(CConstantesLicencias_LCGIII.representante.getIdPersona());
                            }else{
                                /** caso 2 */
                               //System.out.println("************** 2.- Se ha asignado un representante que NO EXISTE EN BD");
                               representante.setIdPersona(-1);
                            }
                        }else{
                            /** caso 3 */
                            //System.out.println("************** 3.- NO Se ha asignado un representante");
                            representante= null;
                        }
                    }else if (_solicitud.getRepresentante() == null){
                        /** La solicitud no tiene representante. Puede ocurrir:
                         * 1.- se asigne un representante de BD
                         * 2.- se asigne uno nuevo que no existe en BD
                         * 3.- se quede como esta sin representante
                         */
                        if ((CConstantesLicencias_LCGIII.representante != null) || (_DNI_CIF_Representante.trim().length() > 0)){
                            if (CConstantesLicencias_LCGIII.representante != null){
                                /** caso 1 */
                                //System.out.println("************** 4.- Se ha asignado un representante de BD ID="+CConstantesLicencias_LCGIII.representante.getIdPersona());
                               representante.setIdPersona(CConstantesLicencias_LCGIII.representante.getIdPersona());
                            }else{
                                /** caso 2 */
                                //System.out.println("************** 5.- Se ha asignado un representante que NO EXISTE EN BD");
                               representante.setIdPersona(-1);
                            }
                        }else{
                            /** caso 3 */
                            //System.out.println("************** 6.- NO Se ha asignado un representante");
                            representante= null;
                        }
                    }

					/** Promotor */
                    viaNotificacionIndex = new Integer(viaNotificacionPromotorEJCBox.getSelectedPatron()).intValue();
					viaNotificacion = new CViaNotificacion(viaNotificacionIndex, "");
                    _tipoViaPromotor= tipoViaNotificacionPromotorEJCBox.getSelectedPatron();
					// @param CDatosNotificacion String dniCif, CViaNotificacion viaNotificacion, String fax, String telefono, String movil, String email, String tipoVia, String nombreVia, String numeroVia, String portal, String planta, String escalera, String letra, String cpostal, String municipio, String provincia, String notificar
                    fax= "";
                    try{fax= faxPromotorTField.getNumber().toString();}catch(Exception e){}
                    telefono= "";
                    try{telefono= telefonoPromotorTField.getNumber().toString();}catch (Exception e){}
                    movil= "";
                    try{movil=  movilPromotorTField.getNumber().toString();}catch(Exception e){}
                    numero= "";
                    try{numero= numeroViaPromotorTField.getNumber().toString();}catch(Exception e){}
                    cPostal= "";
                    try{cPostal= cPostalPromotorTField.getNumber().toString();}catch(Exception e){}
					datosNotificacion = new CDatosNotificacion(_DNI_CIF_Promotor, viaNotificacion, fax, telefono, movil, _emailPromotor, _tipoViaPromotor, _nombreViaPromotor, numero, _portalPromotor, _plantaPromotor, _escaleraPromotor, _letraPromotor, cPostal, _municipioPromotor, _provinciaPromotor, new Integer(_seNotificaPromotor).toString());
					CPersonaJuridicoFisica promotor = new CPersonaJuridicoFisica(_DNI_CIF_Promotor, _nombrePromotor, _apellido1Promotor, _apellido2Promotor, _colegioPromotor, _visadoPromotor, _titulacionPromotor, datosNotificacion);
                    if (_solicitud.getPromotor() != null){
					    promotor.setIdPersona(_solicitud.getPromotor().getIdPersona());
                    }

					/** SOLICITUD */

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

					CTipoLicencia licencia = _solicitud.getTipoLicencia();

                    /** REFERENCIAS CATASTRALES */
					Vector referenciasCatastrales = new Vector();
					for (int i = 0; i < referenciasCatastralesJTable.getRowCount(); i++) {

                        /** tipoVia */
                        String tipoVia= null;
                        Object objeto=referenciasCatastralesJTableModel.getValueAt(i, 1);
                        if ((objeto instanceof DomainNode) && objeto!=null)
                        {
                            CConstantesLicencias_LCGIII.referencia.setTipoVia(((DomainNode)objeto).getTerm(literales.getLocale().toString()));
                            tipoVia=((DomainNode)objeto).getPatron();
                        }
                        if ((objeto instanceof String) && objeto!=null)
                        {
                            if (((String)objeto).length()>0)
                            {
                                CConstantesLicencias_LCGIII.referencia.setTipoVia(Estructuras.getListaTiposViaINE().getDomainNode((String)objeto).getTerm(literales.getLocale().toString()));
                                tipoVia=((String)objeto);
                            }
                        }

                        String ref_Catastral = (String)referenciasCatastralesJTable.getValueAt(i, 0);

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

                    // Recogemos la lista de documentos anexados a la solicitud
                    Vector vListaAnexos= documentacionJPanel.getAnexos();

					CSolicitudLicencia solicitudLicencia = new CSolicitudLicencia(licencia, tipoObra, propietario, representante, null, promotor, null, null, _unidadTramitadora, _unidadRegistro, _motivo, _asunto, _solicitud.getFecha(), tasa, tipoViaINE, nombreViaEmplazamiento, numeroViaEmplazamiento, portalViaEmplazamiento, plantaViaEmplazamiento, letraViaEmplazamiento, cPostalEmplazamiento, municipioJTField.getText(), provinciaJTField.getText(), _observaciones, vListaAnexos, referenciasCatastrales);
					solicitudLicencia.setIdSolicitud(_solicitud.getIdSolicitud());
                    solicitudLicencia.setNumAdministrativo(_solicitud.getNumAdministrativo());
                    solicitudLicencia.setImpuesto(impuesto);
                    solicitudLicencia.setTecnicos(tecnicosJPanel.getListaTecnicos());
                    solicitudLicencia.setObservacionesDocumentacionEntregada(documentacionJPanel.getObservacionesGenerales());
                    solicitudLicencia.setDocumentacionEntregada(documentacionJPanel.getDocumentacionObligatoriaSeleccionada());
                    /** fecha limite de obra */
                    Date dateFechaLimite= CUtilidades.parseFechaStringToDate(fechaLimiteObraJTField.getText());
                    solicitudLicencia.setFechaLimiteObra(dateFechaLimite);

                    /** Mejoras */
                    Vector vMejoras= documentacionJPanel.getMejoras();
                    solicitudLicencia.setMejoras(vMejoras);

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
								String revisadoPor = CConstantesLicencias_LCGIII.principal.getName();
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

					// @param CExpedienteLicencia String numExpediente, int idSolicitud, CTipoTramitacion tipoTramitacion, CTipoFinalizacion tipoFinalizacion, String servicioEncargado, String asunto, String silencioAdministrativo, String formaInicio, int numFolios, Date fechaApertura, String responsable, Date plazoVencimiento, String habiles, int numDias, String observaciones,CEstado estado, Vector notificaciones
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
					CResultadoOperacion resultado = COperacionesLicencias.modificarExpediente(solicitudLicencia, expediente);

					/** si ha ido bien, los anexos han sido actualizados.
					 * Como no cerramos la pantalla, es necesario recargar los nuevos anexos del expediente */
					if (resultado.getResultado()) {
                        /* El expediente ha sido modificado */
						mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.Message1"));
                        final int idObra=solicitudLicencia.getTipoObra().getIdTipoObra();
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
                                                    CUtilidadesComponentes.addFeatureCapa(geopistaEditor, "licencias_obra_mayor", referenciasCatastralesJTable, referenciasCatastralesJTableModel);
                                                    compruebaSolar(vRefCatastrales,idObra);
                                                    CConstantesLicencias_LCGIII.persona= null;
                                                    CConstantesLicencias_LCGIII.representante= null;
                                                    CConstantesLicencias_LCGIII.tecnico= null;
                                                    CConstantesLicencias_LCGIII.promotor= null;
						                            consultarJButtonActionPerformed();
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

					} else{
                        /** Comprobamos que no se haya excedido el maximo FileUploadBase.SizeLimitExceededException */
                       if (resultado.getDescripcion().equalsIgnoreCase("FileUploadBase.SizeLimitExceededException")){
                           JOptionPane optionPane= new JOptionPane(CMainLicencias.literales.getString("DocumentacionLicenciasJPanel.mensaje4"), JOptionPane.ERROR_MESSAGE);
                           JDialog dialog =optionPane.createDialog(this,"ERROR");
                           dialog.show();
                       }else{
                           JOptionPane optionPane= new JOptionPane("Error al modificar el expediente.\n"+resultado.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                           JDialog dialog =optionPane.createDialog(this,"ERROR");
                           dialog.show();
                       }
                    }
				}
			} else {
                if (!rellenaCamposObligatorios()){
                    mostrarMensaje(CMainLicencias.literales.getString("datosSolicitudJPanel.Message1"));
                }else if (!tecnicosJPanel.hayTecnicos()){
                    mostrarMensaje(CMainLicencias.literales.getString("TecnicosJPanel.mensaje2"));
                }
			}
			
			//Actualizar Estado de Expediente
			CResultadoOperacion resultado = COperacionesLicencias.actualizarEstadoExpediente(_expediente);
			if (resultado.getResultado()){
				if (resultado.getDescripcion().equalsIgnoreCase("ExpedienteNoPublicado")){
					mostrarMensaje(CMainLicencias.literales.getString("CModificaionesLicencias.ExpedienteNoPublicado"));
				}
				//else{
				//	mostrarMensaje(resultado.getDescripcion());
				//}
			}
			else{

				JOptionPane optionPane= new JOptionPane("Error al actualizar el estado del expediente en el SIGEM.\n"+resultado.getDescripcion(),JOptionPane.ERROR_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"ERROR");
				dialog.show();

			}
			/////////////////////////////////////////////////////////////////


		} else {
			mostrarMensaje(CMainLicencias.literales.getString("expedienteJPanel.Message1"));
		}


	}//GEN-LAST:event_aceptarJButtonActionPerformed

	private void publicarJButtonActionPerformed(){
		
		if(_consultaOK){
			
			if (_expediente == null)
	        {
	            //mostrarMensaje("Es necesario consultar primero por un expediente.");
				mostrarMensaje(CMainLicencias.literales.getString("CActualizarIdSigemLicenciasForm.ConsultarExpediente"));
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
						mostrarMensaje(CMainLicencias.literales.getString("CActualizarIdSigemLicenciasForm.Message1"));
					}
					else{
						mostrarMensaje(CMainLicencias.literales.getString("CActualizarIdSigemLicenciasForm.Message2"));
					}				
				}
				else{
					mostrarMensaje(CMainLicencias.literales.getString("CActualizarIdSigemLicenciasForm.Message2"));
				}			
			}
			else{
				//Está publicado
				mostrarMensaje(CMainLicencias.literales.getString("CActualizarIdSigemLicenciasForm.ExpedientePublicado"));
	            return;
			}		
		}
		else{
			mostrarMensaje(CMainLicencias.literales.getString("expedienteJPanel.Message1"));
		}

	}

	private void buscarDNIPromotorJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIPromotorJButtonActionPerformed
    	CUtilidadesComponentes.showPersonaDialog(desktop,CMainLicencias.literales);

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
            nombreViaPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNombreVia());
            numeroViaPromotorTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNumeroVia());
            plantaPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPlanta());
            portalPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPortal());
            escaleraPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEscalera());
            letraPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getLetra());
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
            try{
                cPostalPromotorTField.setNumber(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getCpostal()));
            }catch(Exception e){cPostalPromotorTField.setText("");}

            municipioPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMunicipio());
            provinciaPromotorJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getProvincia());
            if (CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarPromotorJCheckBox.setSelected(true);
            else notificarPromotorJCheckBox.setSelected(false);

		}

        CConstantesLicencias_LCGIII.persona= null;

	}//GEN-LAST:event_buscarDNIPromotorJButtonActionPerformed


	private void buscarDNIPropietarioJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIPropietarioJButtonActionPerformed
		CUtilidadesComponentes.showPersonaDialog(desktop,CMainLicencias.literales);

		if ((CConstantesLicencias_LCGIII.persona != null) && (CConstantesLicencias_LCGIII.persona.getDniCif() != null)) {
                DNIPropietarioTField.setText(CConstantesLicencias_LCGIII.persona.getDniCif());
                nombrePropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getNombre());
                apellido1PropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido1());
                apellido2PropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getApellido2());
                faxPropietarioTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getFax());
                telefonoPropietarioTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTelefono());
                movilPropietarioTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMovil());
                emailPropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEmail());
                nombreViaPropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNombreVia());
                numeroViaPropietarioTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNumeroVia());
                plantaPropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPlanta());
                portalPropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getPortal());
                escaleraPropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getEscalera());
                letraPropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getLetra());
                try{
                    tipoViaNotificacionPropietarioEJCBox.setSelectedPatron(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
                }catch (Exception e){
                    DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getTipoVia());
                    if (auxNode!=null)
				            tipoViaNotificacionPropietarioEJCBox.setSelected(auxNode.getIdNode());
                }
                try{
                    viaNotificacionPropietarioEJCBox.setSelectedPatron(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                }catch (Exception e){
                    DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                    if (auxNode!=null)
                            viaNotificacionPropietarioEJCBox.setSelected(auxNode.getIdNode());
                }

                try{
                    cPostalPropietarioTField.setNumber(new Integer(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalPropietarioTField.setText("");}
                municipioPropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getMunicipio());
                provinciaPropietarioJTField.setText(CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getProvincia());
                /*
                if (CConstantesLicencias_LCGIII.persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                    notificarPropietarioJCheckBox.setSelected(true);
                else notificarPropietarioJCheckBox.setSelected(false);
                */

		}

        CConstantesLicencias_LCGIII.persona= null;

	}//GEN-LAST:event_buscarDNIPropietarioJButtonActionPerformed

	/*******************************************************************/
	/*                         Metodos propios                         */
	/**
	 * ***************************************************************
	 */
	public boolean datosPropietarioOK() {
		try {
			if (CUtilidades.excedeLongitud(_DNI_CIF_Propietario, CConstantesLicencias.MaxLengthDNI)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje1"));
				return false;
			}
			if (CUtilidades.excedeLongitud(_nombrePropietario, CConstantesLicencias.MaxLengthNombre)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje2"));
				return false;
			}
			if (CUtilidades.excedeLongitud(_apellido1Propietario, CConstantesLicencias.MaxLengthApellido) || CUtilidades.excedeLongitud(_apellido2Propietario, CConstantesLicencias.MaxLengthApellido)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje3"));
				return false;
			}
			_emailPropietario = emailPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_emailPropietario, CConstantesLicencias.MaxLengthEmail)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje6"));
				return false;
			}
			_nombreViaPropietario = nombreViaPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_nombreViaPropietario, CConstantesLicencias.MaxLengthNombreVia)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje8"));
				return false;
			}
			_portalPropietario = portalPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_portalPropietario, CConstantesLicencias.MaxLengthPortal)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje11"));
				return false;
			}
			_plantaPropietario = plantaPropietarioJTField.getText().trim();
			if (_plantaPropietario.length() > 0) {
                if (CUtilidades.excedeLongitud(_plantaPropietario, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje13"));
                    return false;
                }
			}
			_escaleraPropietario = escaleraPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_escaleraPropietario, CConstantesLicencias.MaxLengthPlanta)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje14"));
				return false;
			}
			_letraPropietario = letraPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_letraPropietario, CConstantesLicencias.MaxLengthLetra)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje15"));
				return false;
			}
			_municipioPropietario = municipioPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_municipioPropietario, CConstantesLicencias.MaxLengthMunicipio)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje18"));
				return false;
			}

			_provinciaPropietario = provinciaPropietarioJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_provinciaPropietario, CConstantesLicencias.MaxLengthProvincia)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PropietarioTab.mensaje19"));
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
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje1"));
					return false;
				}
				if (CUtilidades.excedeLongitud(_nombreRepresentante, CConstantesLicencias.MaxLengthNombre)) {
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje2"));
					return false;
				}
				if (CUtilidades.excedeLongitud(_apellido1Representante, CConstantesLicencias.MaxLengthApellido) || CUtilidades.excedeLongitud(_apellido2Representante, CConstantesLicencias.MaxLengthApellido)) {
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje3"));
					return false;
				}

				_emailRepresentante = emailRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_emailRepresentante, CConstantesLicencias.MaxLengthEmail)) {
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje6"));
					return false;
				}
				_nombreViaRepresentante = nombreViaRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_nombreViaRepresentante, CConstantesLicencias.MaxLengthNombreVia)) {
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje8"));
					return false;
				}
				_portalRepresentante = portalRepresentanteJTField.getText().trim();
				if (_portalRepresentante.length() > 0) {
					if (CUtilidades.excedeLongitud(_portalRepresentante, CConstantesLicencias.MaxLengthPortal)) {
						mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje11"));
						return false;
					}
				}
				_plantaRepresentante = plantaRepresentanteJTField.getText().trim();
				if (_plantaRepresentante.length() > 0) {
                    if (CUtilidades.excedeLongitud(_plantaRepresentante, CConstantesLicencias.MaxLengthPlanta)) {
                        mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje13"));
                        return false;
                    }
				}
				_escaleraRepresentante = escaleraRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_escaleraRepresentante, CConstantesLicencias.MaxLengthPlanta)) {
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje14"));
					return false;
				}
				_letraRepresentante = letraRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_letraRepresentante, CConstantesLicencias.MaxLengthLetra)) {
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje15"));
					return false;
				}
				_municipioRepresentante = municipioRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_municipioRepresentante, CConstantesLicencias.MaxLengthMunicipio)) {
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje18"));
					return false;
				}

				_provinciaRepresentante = provinciaRepresentanteJTField.getText().trim();
				if (CUtilidades.excedeLongitud(_provinciaRepresentante, CConstantesLicencias.MaxLengthProvincia)) {
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.RepresentanteTab.mensaje19"));
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


	public boolean datosPromotorOK() {

		try {
			if (CUtilidades.excedeLongitud(_DNI_CIF_Promotor, CConstantesLicencias.MaxLengthDNI)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje1"));
				return false;
			}
			if (CUtilidades.excedeLongitud(_nombrePromotor, CConstantesLicencias.MaxLengthNombre)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje2"));
				return false;
			}
			if (CUtilidades.excedeLongitud(_apellido1Promotor, CConstantesLicencias.MaxLengthApellido) || CUtilidades.excedeLongitud(_apellido2Promotor, CConstantesLicencias.MaxLengthApellido)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje3"));
				return false;
			}
			if (CUtilidades.excedeLongitud(_colegioPromotor, CConstantesLicencias.MaxLengthColegio)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje4"));
				return false;
			}
			if (CUtilidades.excedeLongitud(_visadoPromotor, CConstantesLicencias.MaxLengthVisado)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje5"));
				return false;
			}
			if (CUtilidades.excedeLongitud(_titulacionPromotor, CConstantesLicencias.MaxLengthTitulacion)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje6"));
				return false;
			}

			_emailPromotor = emailPromotorJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_emailPromotor, CConstantesLicencias.MaxLengthEmail)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje9"));
				return false;
			}
			_nombreViaPromotor = nombreViaPromotorJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_nombreViaPromotor, CConstantesLicencias.MaxLengthNombreVia)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje11"));
				return false;
			}
			_portalPromotor = portalPromotorJTField.getText().trim();
			if (_portalPromotor.length() > 0) {
				if (CUtilidades.excedeLongitud(_portalPromotor, CConstantesLicencias.MaxLengthPortal)) {
					mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje14"));
					return false;
				}
			}
			_plantaPromotor = plantaPromotorJTField.getText().trim();
			if (_plantaPromotor.length() > 0) {
                if (CUtilidades.excedeLongitud(_plantaPromotor, CConstantesLicencias.MaxLengthPlanta)) {
                    mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje16"));
                    return false;
                }
			}
			_escaleraPromotor = escaleraPromotorJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_escaleraPromotor, CConstantesLicencias.MaxLengthPlanta)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje17"));
				return false;
			}
			_letraPromotor = letraPromotorJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_letraPromotor, CConstantesLicencias.MaxLengthLetra)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje18"));
				return false;
			}
			_municipioPromotor = municipioPromotorJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_municipioPromotor, CConstantesLicencias.MaxLengthMunicipio)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje21"));
				return false;
			}

			_provinciaPromotor = provinciaPromotorJTField.getText().trim();
			if (CUtilidades.excedeLongitud(_provinciaPromotor, CConstantesLicencias.MaxLengthProvincia)) {
				mostrarMensaje(CMainLicencias.literales.getString("CModificacionLicenciasForm.PromotorTab.mensaje22"));
				return false;
			}
			boolean notificar = notificarPromotorJCheckBox.isSelected();
			if (notificar)
				_seNotificaPromotor = 1;
			else
				_seNotificaPromotor = 0;

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
            estadoExpedienteJCBox.removeAllItems();
            if (_hEstados.size() > 0) {
                for (int i = 0; i < _hEstados.size(); i++) {
                    CEstado estado = (CEstado) _hEstados.get(new Integer(i));
                    DomainNode dominio= Estructuras.getListaEstados().getDomainNode(new Integer(estado.getIdEstado()).toString());
                    estadoExpedienteJCBox.addItem(dominio.getTerm(CMainLicencias.currentLocale.toString()));
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
			responsableTField.setText(_expediente.getResponsable());
			observacionesExpedienteTPane.setText(_expediente.getObservaciones());
			_vu = _expediente.getVU();

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
			/** Referencias Catastrales */
			cargarReferenciasCatastralesJTable(_solicitud);

			// Actualizamos campos
			numRegistroJTField.setText(_solicitud.getNumAdministrativo());
			numRegistroJTField.setEnabled(false);
			unidadTJTField.setText(_solicitud.getUnidadTramitadora());
			unidadRJTField.setText(_solicitud.getUnidadDeRegistro());
			motivoTField.setText(_solicitud.getMotivo());
			asuntoSolicitudTField.setText(_solicitud.getAsunto());
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
			observacionesSolicitudTPane.setText(_solicitud.getObservaciones());

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
				DNIPropietarioTField.setText(propietario.getDniCif());
				nombrePropietarioJTField.setText(propietario.getNombre());
				apellido1PropietarioJTField.setText(propietario.getApellido1());
				apellido2PropietarioJTField.setText(propietario.getApellido2());
				faxPropietarioTField.setText(propietario.getDatosNotificacion().getFax());
				telefonoPropietarioTField.setText(propietario.getDatosNotificacion().getTelefono());
				movilPropietarioTField.setText(propietario.getDatosNotificacion().getMovil());
				emailPropietarioJTField.setText(propietario.getDatosNotificacion().getEmail());
                try
                {
				    tipoViaNotificacionPropietarioEJCBox.setSelectedPatron(propietario.getDatosNotificacion().getTipoVia());
                }catch(Exception e)
                {
                    DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(propietario.getDatosNotificacion().getTipoVia());
                    if (auxNode!=null)
				            tipoViaNotificacionPropietarioEJCBox.setSelected(auxNode.getIdNode());
                }
				nombreViaPropietarioJTField.setText(propietario.getDatosNotificacion().getNombreVia());
				numeroViaPropietarioTField.setText(propietario.getDatosNotificacion().getNumeroVia());
				plantaPropietarioJTField.setText(propietario.getDatosNotificacion().getPlanta());
				letraPropietarioJTField.setText(propietario.getDatosNotificacion().getLetra());
				portalPropietarioJTField.setText(propietario.getDatosNotificacion().getPortal());
				escaleraPropietarioJTField.setText(propietario.getDatosNotificacion().getEscalera());
                try{
				    cPostalPropietarioTField.setNumber(new Integer(propietario.getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalPropietarioTField.setText("");}
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
				DNIRepresentanteTField.setText(representante.getDniCif());
				nombreRepresentanteJTField.setText(representante.getNombre());
				apellido1RepresentanteJTField.setText(representante.getApellido1());
				apellido2RepresentanteJTField.setText(representante.getApellido2());
				faxRepresentanteTField.setText(representante.getDatosNotificacion().getFax());
				telefonoRepresentanteTField.setText(representante.getDatosNotificacion().getTelefono());
				movilRepresentanteTField.setText(representante.getDatosNotificacion().getMovil());
				emailRepresentanteJTField.setText(representante.getDatosNotificacion().getEmail());
                try
                {
                    tipoViaNotificacionRepresentanteEJCBox.setSelectedPatron(representante.getDatosNotificacion().getTipoVia());

                }catch(Exception e)
                {
                    DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(representante.getDatosNotificacion().getTipoVia());
                    if (auxNode!=null)
                            tipoViaNotificacionRepresentanteEJCBox.setSelected(auxNode.getIdNode());
                }


				nombreViaRepresentanteJTField.setText(representante.getDatosNotificacion().getNombreVia());
				numeroViaRepresentanteTField.setText(representante.getDatosNotificacion().getNumeroVia());
				plantaRepresentanteJTField.setText(representante.getDatosNotificacion().getPlanta());
				letraRepresentanteJTField.setText(representante.getDatosNotificacion().getLetra());
				portalRepresentanteJTField.setText(representante.getDatosNotificacion().getPortal());
				escaleraRepresentanteJTField.setText(representante.getDatosNotificacion().getEscalera());
                try{
				    cPostalRepresentanteTField.setNumber(new Integer(representante.getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalRepresentanteTField.setText("");}
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
            DNIRepresentanteTField.setText("");
            nombreRepresentanteJTField.setText("");
            apellido1RepresentanteJTField.setText("");
            apellido2RepresentanteJTField.setText("");
            faxRepresentanteTField.setText("");
            telefonoRepresentanteTField.setText("");
            movilRepresentanteTField.setText("");
            emailRepresentanteJTField.setText("");
            nombreViaRepresentanteJTField.setText("");
            numeroViaRepresentanteTField.setText("");
            plantaRepresentanteJTField.setText("");
            letraRepresentanteJTField.setText("");
            portalRepresentanteJTField.setText("");
            escaleraRepresentanteJTField.setText("");
            cPostalRepresentanteTField.setText("");
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

	/**
	 * Promotor
	 */
	public void rellenarPromotor() {
		try {
			// Actualizamos campos
			CPersonaJuridicoFisica promotor = _solicitud.getPromotor();
			if (promotor != null) {
                viaNotificacionPromotorEJCBox.setSelectedPatron(new Integer(_solicitud.getPromotor().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
				DNIPromotorTField.setText(promotor.getDniCif());
				nombrePromotorJTField.setText(promotor.getNombre());
				apellido1PromotorJTField.setText(promotor.getApellido1());
				apellido2PromotorJTField.setText(promotor.getApellido2());
				colegioPromotorJTField.setText(promotor.getColegio());
				visadoPromotorJTField.setText(promotor.getVisado());
				titulacionPromotorJTField.setText(promotor.getTitulacion());
				faxPromotorTField.setText(promotor.getDatosNotificacion().getFax());
				telefonoPromotorTField.setText(promotor.getDatosNotificacion().getTelefono());
				movilPromotorTField.setText(promotor.getDatosNotificacion().getMovil());
				emailPromotorJTField.setText(promotor.getDatosNotificacion().getEmail());
                try
               {
                   tipoViaNotificacionPromotorEJCBox.setSelectedPatron(promotor.getDatosNotificacion().getTipoVia());
               }catch(Exception e)
               {
                   DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(promotor.getDatosNotificacion().getTipoVia());
                   if (auxNode!=null)
                          tipoViaNotificacionPromotorEJCBox.setSelected(auxNode.getIdNode());
               }

				nombreViaPromotorJTField.setText(promotor.getDatosNotificacion().getNombreVia());
				numeroViaPromotorTField.setText(promotor.getDatosNotificacion().getNumeroVia());
				plantaPromotorJTField.setText(promotor.getDatosNotificacion().getPlanta());
				letraPromotorJTField.setText(promotor.getDatosNotificacion().getLetra());
				portalPromotorJTField.setText(promotor.getDatosNotificacion().getPortal());
				escaleraPromotorJTField.setText(promotor.getDatosNotificacion().getEscalera());
                try{
				    cPostalPromotorTField.setNumber(new Integer(promotor.getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalPromotorTField.setText("");}
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
                col0.setCellEditor(new ComboBoxTableEditor(new ComboBoxEstructuras(Estructuras.getListaEstadosNotificacion(), null, CMainLicencias.currentLocale.toString(), false)));
                col0.setCellRenderer(new ComboBoxRendererEstructuras(Estructuras.getListaEstadosNotificacion(), null, CMainLicencias.currentLocale.toString(), false));
				for (int i=0; i<_vNotificaciones.size(); i++){
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

                        Object[] rowData = {((DomainNode)Estructuras.getListaTiposNotificacion().getDomainNode(new Integer(notificacion.getTipoNotificacion().getIdTipoNotificacion()).toString())).getTerm(CMainLicencias.currentLocale.toString()),
                            combox.getSelectedItem(),
                            CUtilidades.formatFechaH24(notificacion.getPlazoVencimiento()),
                            notificacion.getPersona().getDniCif(),
                            CUtilidades.formatFechaH24(notificacion.getFechaNotificacion()),
                            CUtilidades.formatFechaH24(notificacion.getFecha_reenvio()),
                            new Integer(i)};
                        _notificacionesExpedienteTableModel.addRow(rowData);

                    }catch(Exception e){
                        logger.error("ERROR al cargar la fila de NOTIFICACIONES "+e);
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
				for (int i=0; i< _vEventos.size(); i++) {
					CEvento evento = (CEvento) _vEventos.get(i);
                    try{
                        JCheckBox check = (JCheckBox) ((CheckBoxTableEditor) eventosJTable.getCellEditor(i, 2)).getComponent();

                        if (evento.getRevisado().equalsIgnoreCase("1"))
                            check.setSelected(true);
                        else
                            check.setSelected(false);
                        _eventosExpedienteTableModel.addRow(new Object[]{new Long(evento.getIdEvento()).toString(),
                                                                         CUtilidades.formatFechaH24(evento.getFechaEvento()),
                                                                         new Boolean(check.isSelected()),
                                                                         evento.getRevisadoPor(),
                                                                         evento.getContent(),
                                                                         new Integer(i)});
                    }catch(Exception e){
                        logger.error("ERROR al carga la fila EVENTOS "+e);
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
     private void generarFicha()
    {
    	try
        {
            if (_expediente == null)
            {
               mostrarMensaje(CMainLicencias.literales.getString("expedienteJPanel.Message1"));
                return;
            }
            _expediente.setEstructuraEstado(Estructuras.getListaEstados());
            _expediente.setEstructuraTipoObra(Estructuras.getListaTiposObra());
            _expediente.setLocale(CMainLicencias.currentLocale);
            _expediente.setSolicitud(_solicitud);
           	new GeopistaPrintable().printObjeto(FichasDisponibles.fichalicenciaobramayor, _expediente , CExpedienteLicencia.class, geopistaEditor.getLayerViewPanel(), GeopistaPrintable.FICHA_LICENCIAS_MODIFICACION);
		} catch (Exception ex) {
			logger.error("Exception al crear la ficha: " ,ex);
		}
    }
    private void verWorkFlow()
    {
    	try
        {
            if (_expediente == null)
            {
                mostrarMensaje("Es necesario consultar primero por un expediente.");
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
                        /** Comprobamos que el historico no sea generico, en cuyo caso, no es necesario annadir
                         * al apunte el literal del estado al que ha cambiado.
                         */
                        if ((historico.getSistema().equalsIgnoreCase("1")) && (historico.getGenerico() == 0)){
                            /** Componemos el apunte, de forma multilingue */
                            apunte+= " " +
                                    ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(CMainLicencias.currentLocale.toString()) + ".";
                            historico.setApunte(apunte);
                        }

                        vAuxiliar.add(i, historico);

                        _historicoExpedienteTableModel.addRow(new Object[]{((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(CMainLicencias.currentLocale.toString()),
                                                                           historico.getUsuario(),
                                                                           CUtilidades.formatFechaH24(historico.getFechaHistorico()),
                                                                           apunte,
                                                                           new Integer(i)});

                    }catch(Exception e){
                        logger.error("ERROR al carla fila HISTORICO "+e);
                    }
                }
			}
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

	public boolean rellenaCamposObligatorios() {
		try {
			// Chequeamos que el usuario haya rellenado los campos obligatorios
			/** Comprobamos los datos del Propietario */
			// leemos los datos referentes al Propietario
			_DNI_CIF_Propietario = DNIPropietarioTField.getText().trim();
			_nombrePropietario = nombrePropietarioJTField.getText().trim();
			_apellido1Propietario = apellido1PropietarioJTField.getText().trim();
			_apellido2Propietario = apellido2PropietarioJTField.getText().trim();

			_nombreViaPropietario = nombreViaPropietarioJTField.getText().trim();
            try{
			    _numeroViaPropietario= numeroViaPropietarioTField.getNumber().toString().trim();
            }catch (Exception e){
                _numeroViaPropietario= "";
            }
            try{
			    _cPostalPropietario= cPostalPropietarioTField.getNumber().toString().trim();
            }catch(Exception e){
                _cPostalPropietario= "";
            }
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
            _DNI_CIF_Representante = DNIRepresentanteTField.getText().trim();
            _nombreRepresentante = nombreRepresentanteJTField.getText().trim();
            _apellido1Representante = apellido1RepresentanteJTField.getText().trim();
            _apellido2Representante = apellido2RepresentanteJTField.getText().trim();
            _nombreViaRepresentante = nombreViaRepresentanteJTField.getText().trim();

            try{
                _cPostalRepresentante= cPostalRepresentanteTField.getNumber().toString().trim();
            }catch(Exception e){
                _cPostalRepresentante= "";
            }
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
                /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
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

			/** COMPROBAR Comprobamos los datos obligatorios del tecnico */

			/** Comprobamos los datos del promotor */
			// leemos los datos referentes al tecnico
			_DNI_CIF_Promotor = DNIPromotorTField.getText();
			_nombrePromotor = nombrePromotorJTField.getText();
			_apellido1Promotor = apellido1PromotorJTField.getText();
			_apellido2Promotor = apellido2PromotorJTField.getText();
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
            if ((_DNI_CIF_Promotor.trim().length() == 0) || (_nombrePromotor.trim().length() == 0))
                return false;

			if (    (_nombreViaPromotor.trim().length() == 0) ||
					(_numeroViaPromotor.trim().length() == 0) || (_cPostalPromotor.trim().length() == 0) ||
					(_municipioPromotor.trim().length() == 0) || (_provinciaPromotor.trim().length() == 0))
				return false;

            if (emailPromotorObligatorio){
                if (emailPromotorJTField.getText().trim().length() == 0) return false;
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

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(obraMayorJPanel, mensaje);
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


	private boolean clearScreen() {
		/** Solicitud */
		CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);

		numRegistroJTField.setText("");
		numRegistroJTField.setEnabled(false);
		unidadTJTField.setText("");
		unidadRJTField.setText("");
		motivoTField.setText("");
		asuntoSolicitudTField.setText("");
		fechaSolicitudJTField.setText("");
		fechaSolicitudJTField.setEnabled(false);
        fechaLimiteObraJTField.setText("");
        fechaLimiteObraJTField.setEnabled(false);
		tasaTextField.setText("€0.00");
        impuestoTextField.setText("€0.00");
		observacionesSolicitudTPane.setText("");

        /** Emplazamiento */
		tipoViaINEEJCBox.setSelectedIndex(0);
		nombreViaTField.setText("");
		numeroViaNumberTField.setText("");
		portalViaTField.setText("");
		plantaViaTField.setText("");
		letraViaTField.setText("");
        cpostalViaTField.setText("");

		/** Expediente */
		numExpedienteJTField.setText("");
		numExpedienteJTField.setEnabled(true);
		servicioExpedienteJTField.setText("");
		asuntoExpedienteJTField.setText("");
		fechaAperturaJTField.setText("");
		fechaAperturaJTField.setEnabled(false);
		inicioJTField.setText("");
		silencioJCheckBox.setSelected(false);
		responsableTField.setText("");
		observacionesExpedienteTPane.setText("");

		/** Propietario */
		DNIPropietarioTField.setText("");
		nombrePropietarioJTField.setText("");
		apellido1PropietarioJTField.setText("");
		apellido2PropietarioJTField.setText("");
		faxPropietarioTField.setText("");
		telefonoPropietarioTField.setText("");
		movilPropietarioTField.setText("");
		emailPropietarioJTField.setText("");
		nombreViaPropietarioJTField.setText("");
		numeroViaPropietarioTField.setText("");
		plantaPropietarioJTField.setText("");
		letraPropietarioJTField.setText("");
		portalPropietarioJTField.setText("");
		escaleraPropietarioJTField.setText("");
		cPostalPropietarioTField.setText("");
		municipioPropietarioJTField.setText("");
		provinciaPropietarioJTField.setText("");
		notificarPropietarioJCheckBox.setSelected(false);

		/** Representante */
		DNIRepresentanteTField.setText("");
		nombreRepresentanteJTField.setText("");
		apellido1RepresentanteJTField.setText("");
		apellido2RepresentanteJTField.setText("");
		faxRepresentanteTField.setText("");
		telefonoRepresentanteTField.setText("");
		movilRepresentanteTField.setText("");
		emailRepresentanteJTField.setText("");
		nombreViaRepresentanteJTField.setText("");
		numeroViaRepresentanteTField.setText("");
		plantaRepresentanteJTField.setText("");
		letraRepresentanteJTField.setText("");
		portalRepresentanteJTField.setText("");
		escaleraRepresentanteJTField.setText("");
		cPostalRepresentanteTField.setText("");
		municipioRepresentanteJTField.setText("");
		provinciaRepresentanteJTField.setText("");
		notificarRepresentanteJCheckBox.setSelected(false);

		/** Tecnicos */
        tecnicosJPanel.clearTecnicosJPanel(true);

		/** Promotor */
		DNIPromotorTField.setText("");
		nombrePromotorJTField.setText("");
		apellido1PromotorJTField.setText("");
		apellido2PromotorJTField.setText("");
		colegioPromotorJTField.setText("");
		visadoPromotorJTField.setText("");
		titulacionPromotorJTField.setText("");
		faxPromotorTField.setText("");
		telefonoPromotorTField.setText("");
		movilPromotorTField.setText("");
		emailPromotorJTField.setText("");
		nombreViaPromotorJTField.setText("");
		numeroViaPromotorTField.setText("");
		plantaPromotorJTField.setText("");
		letraPromotorJTField.setText("");
		portalPromotorJTField.setText("");
		escaleraPromotorJTField.setText("");
		cPostalPromotorTField.setText("");
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
           datosDireccionJTField.setText(CUtilidades.componerCampo(((DomainNode)Estructuras.getListaTiposViaINE().getDomainNode(datos.getTipoVia())).getTerm(CMainLicencias.currentLocale.toString()),
                   datos.getNombreVia(),
                   datos.getNumeroVia()));
           datosCPostalJTField.setText(CUtilidades.componerCampo(datos.getCpostal(), datos.getMunicipio(), datos.getProvincia()));
           datosNotificarPorJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(datos.getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(CMainLicencias.currentLocale.toString()));

           if (notificacion.getEntregadaA() != null){
               entregadaATField.setText(notificacion.getEntregadaA());
               entregadaATextJLabel.setText(notificacion.getEntregadaA());
           }else{
               entregadaATField.setText("");
               entregadaATextJLabel.setText("");
           }
           entregadaATField.setVisible(true);
           okJButton.setVisible(true);

       }else{
           clearDatosNotificacionSelected();
       }

   }

    private void annadirPestanas(ResourceBundle literales){
        try{
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.expedienteJPanel.TitleTab")), CUtilidadesComponentes.iconoExpediente, expedienteJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.expedienteJPanel.TitleTab")), expedienteJPanel);
        }

        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.solicitudJPanel.SubTitleTab")), CUtilidadesComponentes.iconoSolicitud, obraMayorJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.solicitudJPanel.SubTitleTab")), obraMayorJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.propietarioJPanel.TitleTab")), CUtilidadesComponentes.iconoPersona, propietarioJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.propietarioJPanel.TitleTab")), propietarioJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(literales.getString("CModificacionLicenciasForm.representanteJPanel.TitleTab"), CUtilidadesComponentes.iconoRepresentante, representanteJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(literales.getString("CModificacionLicenciasForm.representanteJPanel.TitleTab"), representanteJPanel);
        }


        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("TecnicosJPanel.TitleTab")), CUtilidadesComponentes.iconoPersona, tecnicosJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("TecnicosJPanel.TitleTab")), tecnicosJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.promotorJPanel.TitleTab")), CUtilidadesComponentes.iconoPersona, promotorJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.promotorJPanel.TitleTab")), promotorJPanel);
        }
        try{
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), CUtilidadesComponentes.iconoSolicitud, jTabbedPaneSolicitud);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), jTabbedPaneSolicitud);
        }
         try{
            obraMayorJTabbedPane.addTab(literales.getString("DocumentacionLicenciasJPanel.title"), CUtilidadesComponentes.iconoDocumentacion, documentacionJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(literales.getString("DocumentacionLicenciasJPanel.title"), documentacionJPanel);
        }

        try{
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.notificacionesJPanel.TitleTab"), CUtilidadesComponentes.iconoNotificacion, notificacionesJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.notificacionesJPanel.TitleTab"), notificacionesJPanel);
        }

        try{
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.eventosJPanel.TitleTab"), CUtilidadesComponentes.iconoEvento, eventosJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.eventosJPanel.TitleTab"), eventosJPanel);
        }

        try{
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.historicoJPanel.TitleTab"), CUtilidadesComponentes.iconoHistorico, historicoJPanel);
        }catch(Exception e){
            obraMayorJTabbedPane.addTab(literales.getString("CModificacionLicenciasForm.historicoJPanel.TitleTab"), historicoJPanel);
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
            if(CConstantesLicencias.selectedSubApp.equals(CConstantesLicencias.LicenciasObraMayor)){
                setTitle(literales.getString("CModificacionLicenciasForm.JInternalFrame.title"));
                /** Pestanas */
                obraMayorJTabbedPane.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.expedienteJPanel.TitleTab")));
                jTabbedPaneSolicitud.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.solicitudJPanel.SubTitleTab")));
                jTabbedPaneSolicitud.setTitleAt(1, CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.propietarioJPanel.TitleTab")));
                jTabbedPaneSolicitud.setTitleAt(2, literales.getString("CModificacionLicenciasForm.representanteJPanel.TitleTab"));
                jTabbedPaneSolicitud.setTitleAt(3, CUtilidadesComponentes.annadirAsterisco(literales.getString("TecnicosJPanel.TitleTab")));
                jTabbedPaneSolicitud.setTitleAt(4, CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.promotorJPanel.TitleTab")));
                obraMayorJTabbedPane.setTitleAt(1, CUtilidadesComponentes.annadirAsterisco(literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")));
                obraMayorJTabbedPane.setTitleAt(2, literales.getString("DocumentacionLicenciasJPanel.title"));
                obraMayorJTabbedPane.setTitleAt(3, literales.getString("CModificacionLicenciasForm.notificacionesJPanel.TitleTab"));
                obraMayorJTabbedPane.setTitleAt(4, literales.getString("CModificacionLicenciasForm.eventosJPanel.TitleTab"));
                obraMayorJTabbedPane.setTitleAt(5, literales.getString("CModificacionLicenciasForm.historicoJPanel.TitleTab"));
                obraMayorJTabbedPane.setTitleAt(6, literales.getString("JPanelInformes.jPanelInforme"));

                /** Expediente */
                expedienteJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.expedienteJPanel.TitleBorder")));
                estadoExpedienteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.estadoExpedienteJLabel.text")));
                numExpedienteJLabel.setText(literales.getString("CModificacionLicenciasForm.numExpedienteJLabel.text"));
                servicioExpedienteJLabel.setText(literales.getString("CModificacionLicenciasForm.servicioExpedienteJLabel.text"));
                tramitacionJLabel.setText(literales.getString("CModificacionLicenciasForm.tramitacionJLabel.text"));
                finalizaJLabel.setText(literales.getString("CModificacionLicenciasForm.finalizaJLable.text"));
                asuntoExpedienteJLabel.setText(literales.getString("CModificacionLicenciasForm.asuntoExpedienteJLabel.text"));
                fechaAperturaJLabel.setText(literales.getString("CModificacionLicenciasForm.fechaAperturaJLabel.text"));
                observacionesExpedienteJLabel.setText(literales.getString("CModificacionLicenciasForm.observacionesExpedienteJLabel.text"));
                inicioJLabel.setText(literales.getString("CModificacionLicenciasForm.inicioJLabel.text"));
                responsableJLabel.setText(literales.getString("CModificacionLicenciasForm.responsableJLabel.text"));
                consultarJButton.setText(literales.getString("CModificacionLicenciasForm.consultarJButton.text"));
                silencioJLabel.setText(literales.getString("CModificacionLicenciasForm.silencioJLabel.text"));
                notaJLabel.setText(literales.getString("CModificacionLicenciasForm.notaJLabel.text"));

                /** Solicitud */
                datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosSolicitudJPanel.TitleBorder")));
                tipoObraJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.tipoObraJLabel.text")));
                unidadTJLabel.setText(literales.getString("CModificacionLicenciasForm.unidadTJLabel.text"));
                motivoJLabel.setText(literales.getString("CModificacionLicenciasForm.motivoJLabel.text"));
                asuntoJLabel.setText(literales.getString("CModificacionLicenciasForm.asuntoJLabel.text"));
                fechaSolicitudJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.fechaSolicitudJLabel.text")));
                fechaLimiteObraJLabel.setText(CMainLicencias.literales.getString("CModificacionLicenciasForm.fechaLimiteObraJLabel.text"));
                observacionesJLabel.setText(literales.getString("CModificacionLicenciasForm.observacionesJLabel.text"));
                numRegistroJLabel.setText(literales.getString("CModificacionLicenciasForm.numRegistroJLabel.text"));
                unidadRJLabel.setText(literales.getString("CModificacionLicenciasForm.unidadRJLabel.text"));
                tasaJLabel.setText(literales.getString("CModificacionLicenciasForm.tasaJLabel.text"));
                impuestoJLabel.setText(literales.getString("CModificacionLicenciasForm.impuestoJLabel.text"));
                emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.emplazamientoJPanel.TitleBorder")));
                nombreViaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.tipoViaJLabel.text"), literales.getString("CModificacionLicenciasForm.nombreViaJLabel.text")));
                numeroViaJLabel.setText(literales.getString("CModificacionLicenciasForm.numeroViaJLabel.text"));
                cPostalJLabel.setText(literales.getString("CModificacionLicenciasForm.cPostalJLabel.text"));
                provinciaJLabel.setText(literales.getString("CModificacionLicenciasForm.provinciaJLabel.text"));
                refCatastralJLabel.setText(literales.getString("CModificacionLicenciasForm.refCatastralJLabel.text"));

                /** Propietario */
                datosPersonalesPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosPersonalesPropietarioJPanel.TitleBorder")));
                DNIPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.DNIPropietarioJLabel.text")));
                nombrePropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.nombrePropietarioJLabel.text")));
                apellido1PropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.apellido1PropietarioJLabel.text"));
                apellido2PropietarioJLabel2.setText(literales.getString("CModificacionLicenciasForm.apellido2PropietarioJLabel.text"));
                datosNotificacionPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosNotificacionPropietarioJPanel.TitleTab")));
                viaNotificacionPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.viaNotificacionPropietarioJLabel.text"));
                faxPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.faxPropietarioJLabel.text"));
                telefonoPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.telefonoPropietarioJLabel.text"));
                movilPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.movilPropietarioJLabel.text"));
                emailPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.emailPropietarioJLabel.text"));
                tipoViaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.tipoViaPropietarioJLabel.text")));
                nombreViaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.nombreViaPropietarioJLabel.text")));
                numeroViaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.numeroViaPropietarioJLabel.text")));
                portalPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.portalPropietarioJLabel.text"));
                plantaPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.plantaPropietarioJLabel.text"));
                escaleraPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.escaleraPropietarioJLabel.text"));
                letraPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.letraPropietarioJLabel.text"));
                cPostalPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.cPostalPropietarioJLabel.text")));
                municipioPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.municipioPropietarioJLabel.text")));
                provinciaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.provinciaPropietarioJLabel.text")));
                notificarPropietarioJLabel.setText(literales.getString("CModificacionLicenciasForm.notificarPropietarioJLabel.text"));

                /** Representante */
                datosPersonalesRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosPersonalesRepresentanteJPanel.TitleBorder")));
                DNIRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.DNIRepresentanteJLabel.text")));
                nombreRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.nombreRepresentanteJLabel.text")));
                apellido1RepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.apellido1RepresentanteJLabel.text"));
                apellido2RepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.apellido2RepresentanteJLabel.text"));
                datosNotificacionRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosNotificacionRepresentanteJPanel.TitleTab")));
                viaNotificacionRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.viaNotificacionRepresentanteJLabel.text"));
                faxRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.faxRepresentanteJLabel.text"));
                telefonoRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.telefonoRepresentanteJLabel.text"));
                movilRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.movilRepresentanteJLabel.text"));
                emailRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.emailRepresentanteJLabel.text"));
                tipoViaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.tipoViaRepresentanteJLabel.text")));
                nombreViaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.nombreViaRepresentanteJLabel.text")));
                numeroViaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.numeroViaRepresentanteJLabel.text")));
                portalRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.portalRepresentanteJLabel.text"));
                plantaRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.plantaRepresentanteJLabel.text"));
                escaleraRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.escaleraRepresentanteJLabel.text"));
                letraRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.letraRepresentanteJLabel.text"));
                cPostalRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.cPostalRepresentanteJLabel.text")));
                municipioRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.municipioRepresentanteJLabel.text")));
                provinciaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.provinciaRepresentanteJLabel.text")));
                notificarRepresentanteJLabel.setText(literales.getString("CModificacionLicenciasForm.notificarRepresentanteJLabel.text"));

                /** Tecnicos */
                tecnicosJPanel.renombrarComponentes(literales);

                /** Promotor */
                datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosPersonalesPromotorJPanel.TitleBorder")));
                DNIPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.DNIPromotorJLabel.text")));
                nombrePromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.nombrePromotorJLabel.text")));
                apellido1PromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.apellido1PromotorJLabel.text"));
                apellido2PromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.apellido2PromotorJLabel.text"));
                colegioPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.colegioPromotorJLabel.text"));
                visadoPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.visadoPromotorJLabel.text"));
                titulacionPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.titulacionPromotorJLabel.text"));
                datosNotificacionPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosNotificacionPromotorJPanel.TitleTab")));
                viaNotificacionPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.viaNotificacionPromotorJLabel.text"));
                faxPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.faxPromotorJLabel.text"));
                telefonoPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.telefonoPromotorJLabel.text"));
                movilPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.movilPromotorJLabel.text"));
                emailPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.emailPromotorJLabel.text"));
                tipoViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.tipoViaPromotorJLabel.text")));
                nombreViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.nombreViaPromotorJLabel.text")));
                numeroViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.numeroViaPromotorJLabel.text")));
                portalPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.portalPromotorJLabel.text"));
                plantaPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.plantaPromotorJLabel.text"));
                escaleraPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.escaleraPromotorJLabel.text"));
                letraPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.letraPromotorJLabel.text"));
                cPostalPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.cPostalPromotorJLabel.text")));
                municipioPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.municipioPromotorJLabel.text")));
                provinciaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CModificacionLicenciasForm.provinciaPromotorJLabel.text")));
                notificarPromotorJLabel.setText(literales.getString("CModificacionLicenciasForm.notificarPromotorJLabel.text"));

                /** Documentacion */
                documentacionJPanel.renombrarComponentes(literales);

                /** notificaciones */
                datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosNotificacionesJPanel.TitleBorder")));
                datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosNotificacionJPanel.TitleBorder")));
                datosNombreApellidosJLabel.setText(literales.getString("CModificacionLicenciasForm.datosNombreApellidosJLabel.text"));
                datosDireccionJLabel.setText(literales.getString("CModificacionLicenciasForm.datosDireccionJLabel.text"));
                datosCPostalJLabel.setText(literales.getString("CModificacionLicenciasForm.datosCPostalJLabel.text"));
                datosNotificarPorJLabel.setText(literales.getString("CModificacionLicenciasForm.datosNotificarPorJLabel.text"));
                entregadaAJLabel.setText(literales.getString("CModificacionLicenciasForm.entregadaAJLabel.text"));

                /** Eventos */
                datosEventosJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosEventosJPanel.TitleBorder")));
                descEventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.descEventoJScrollPane.TitleBorder")));

                /** Historico */
                datosHistoricoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.datosHistoricoJPanel.TitleBorder")));
                apunteJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.apunteJScrollPane.TitleBorder")));

                cancelarJButton.setText(literales.getString("CModificacionLicenciasForm.salirJButton.text"));
                aceptarJButton.setText(literales.getString("CModificacionLicenciasForm.modificarJButton.text"));
                publicarJButton.setText(literales.getString("CModificacionLicenciasForm.publicarJButton.text"));
                editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CModificacionLicenciasForm.editorMapaJPanel.TitleBorder")));

                jButtonGenerarFicha.setText(literales.getString("CMainLicencias.jButtonGenerarFicha"));
                jButtonWorkFlow.setText(literales.getString("CMainLicencias.jButtonWorkFlow"));
                jPanelInformes.changeScreenLang(literales);
                jPanelResolucion.changeScreenLang(literales);

                consultarJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.consultarJButton.text"));
                cancelarJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.salirJButton.text"));
                aceptarJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.modificarJButton.toolTipText"));
                publicarJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.publicarJButton.toolTipText"));
                jButtonGenerarFicha.setToolTipText(literales.getString("CMainLicenciasForm.generarFichaJButton.setToolTipText"));
                jButtonWorkFlow.setToolTipText(literales.getString("CMainLicenciasForm.verWorkFlowJButton.setToolTipText"));

                /** Headers de la tabla eventos */
                TableColumn tableColumn= eventosJTable.getColumnModel().getColumn(0);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.eventosJTable.colum1"));
                tableColumn= eventosJTable.getColumnModel().getColumn(1);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.eventosJTable.colum2"));
                tableColumn= eventosJTable.getColumnModel().getColumn(2);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.eventosJTable.colum3"));
                tableColumn= eventosJTable.getColumnModel().getColumn(3);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.eventosJTable.colum4"));
                tableColumn= eventosJTable.getColumnModel().getColumn(4);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.eventosJTable.colum5"));

                /** Headers tabla Notificaciones */
                tableColumn= notificacionesJTable.getColumnModel().getColumn(0);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum6"));
                tableColumn= notificacionesJTable.getColumnModel().getColumn(1);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum1"));
                tableColumn= notificacionesJTable.getColumnModel().getColumn(2);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum2"));
                tableColumn= notificacionesJTable.getColumnModel().getColumn(3);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum3"));
                tableColumn= notificacionesJTable.getColumnModel().getColumn(4);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum4"));
                tableColumn= notificacionesJTable.getColumnModel().getColumn(5);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum5"));

                /** Headers tabla Historico */
                tableColumn= historicoJTable.getColumnModel().getColumn(0);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.historicoJTable.colum2"));
                tableColumn= historicoJTable.getColumnModel().getColumn(1);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.historicoJTable.colum3"));
                tableColumn= historicoJTable.getColumnModel().getColumn(2);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.historicoJTable.colum4"));
                tableColumn= historicoJTable.getColumnModel().getColumn(3);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.historicoJTable.colum5"));

                /** Headers tabla referencias catastrales */
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(0);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column1"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column2"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(2);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column3"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(3);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column4"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(4);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column5"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(5);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column6"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(6);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column7"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(7);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column8"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(8);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column9"));
                tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(9);
                tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column10"));

            modHistoricoJButton.setToolTipText(literales.getString("CMantenimientoHistorico.modHistoricoJButton.setToolTipText.text"));
            borrarHistoricoJButton.setToolTipText(literales.getString("CMantenimientoHistorico.borrarHistoricoJButton.setToolTipText.text"));
            nuevoHistoricoJButton.setToolTipText(literales.getString("CMantenimientoHistorico.nuevoHistoricoJButton.setToolTipText.text"));
            generarFichaJButton.setToolTipText(literales.getString("CMantenimientoHistorico.generarFichaJButton.setToolTipText.text"));
            okJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.okJButtonToolTipText.text"));
            fechaLimiteObraJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.fechaLimiteObraJButton.text"));
            buscarExpedienteJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.buscarExpedienteJButton.text"));
            nombreViaJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.nombreViaJButton.toolTipText"));
            refCatastralJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.refCatastralJButton.toolTipText"));
            mapToTableJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.mapToTableJButton.text"));
            tableToMapJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.tableToMapJButton.text"));
            deleteParcelaJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.deleteParcelaJButton.text"));
            buscarDNIPropietarioJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.buscarDNIPropietarioJButton.text"));
            buscarDNIRepresentanteJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.buscarDNIRepresentanteJButton.text"));
            borrarRepresentanteJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.borrarRepresentanteJButton.text"));
            buscarDNIPromotorJButton.setToolTipText(literales.getString("CModificacionLicenciasForm.buscarDNIPromotorJButton.text"));

            }

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


		public void setNumExpediente(String numExpediente) {

		numExpedienteJTField.setText(numExpediente);
	}
    private void compruebaSolar(Vector vRefCatastrales,int tipoObra)
    {

        CEstado newEstado = (CEstado) _hEstados.get(new Integer(estadoExpedienteJCBox.getSelectedIndex()));
          if ((_expediente.getEstado().getIdEstado()==CConstantesComando.ESTADO_EMISION_PROPUESTA_RESOLUCION) &&
            (newEstado.getIdEstado()==CConstantesComando.ESTADO_FORMALIZACION_LICENCIA))
        {
            if (tipoObra==CConstantesComando.tipoObraDemolicion)
                CUtilidadesComponentes.ponSolar(geopistaEditor, vRefCatastrales,CConstantesComando.SOLAR);
            if (tipoObra==CConstantesComando.tipoObraNuevaPlanta)
                CUtilidadesComponentes.ponSolar(geopistaEditor, vRefCatastrales,CConstantesComando.NUEVA_PLANTA);
        }
    }
    private void comprobarResolucion()
    {
       // if (jPanelResolucion.isEnabled())
       // {
              if (estadoExpedienteJCBox.getSelectedIndex()<0) return;
              CEstado estado = (CEstado) _hEstados.get(new Integer(estadoExpedienteJCBox.getSelectedIndex()));
			  if (estado.getIdEstado()==CConstantesComando.ESTADO_NOTIFICACION_DENEGACION)
                      jPanelResolucion.setFavorable(false);
              if ((estado.getIdEstado()==CConstantesComando.ESTADO_NOTIFICACION_APROBACION)||
                   (estado.getIdEstado()==CConstantesComando.ESTADO_FORMALIZACION_LICENCIA))
                      jPanelResolucion.setFavorable(true);
       // }
    }

    private ComboBoxEstructuras tipoObraEJCBox;
    private ComboBoxEstructuras viaNotificacionPropietarioEJCBox;
    private ComboBoxEstructuras viaNotificacionRepresentanteEJCBox;
    private ComboBoxEstructuras viaNotificacionPromotorEJCBox;

    private ComboBoxEstructuras tipoViaNotificacionPropietarioEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionRepresentanteEJCBox;
    private ComboBoxEstructuras tipoViaNotificacionPromotorEJCBox;

    private ComboBoxEstructuras tramitacionEJCBox;
    private ComboBoxEstructuras finalizacionEJCBox;

    /** pestanna de documentacion de una solicitud (documentacion requerida, anexos...) */
    private DocumentacionLicenciasJPanel documentacionJPanel;

    /** informes **/
    private JPanelInformes jPanelInformes;

    /** pestanna del tecnico */
    private com.geopista.app.licencias.tecnicos.TecnicosJPanel tecnicosJPanel;

    /** notificacion entregada a */
    private com.geopista.app.utilidades.TextField entregadaATField;


    /** Variables propias */
    private com.geopista.app.utilidades.TextField asuntoSolicitudTField;
    private com.geopista.app.utilidades.TextField asuntoExpedienteTField;

    /** responsable */
    private com.geopista.app.utilidades.TextField responsableTField;

    private com.geopista.app.utilidades.TextField motivoTField;
    private com.geopista.app.utilidades.TextPane observacionesSolicitudTPane;
    private com.geopista.app.utilidades.TextPane observacionesExpedienteTPane;

    private com.geopista.app.utilidades.TextField DNIPromotorTField;
    private com.geopista.app.utilidades.TextField DNIPropietarioTField;
    private com.geopista.app.utilidades.TextField DNIRepresentanteTField;

    private com.geopista.app.utilidades.JNumberTextField cPostalPromotorTField;
    private com.geopista.app.utilidades.JNumberTextField cPostalRepresentanteTField;
    private com.geopista.app.utilidades.JNumberTextField cPostalPropietarioTField;

    private com.geopista.app.utilidades.JNumberTextField numeroViaPropietarioTField;
    private com.geopista.app.utilidades.JNumberTextField numeroViaRepresentanteTField;
    private com.geopista.app.utilidades.JNumberTextField numeroViaPromotorTField;

    private com.geopista.app.utilidades.JNumberTextField telefonoPropietarioTField;
    private com.geopista.app.utilidades.JNumberTextField telefonoRepresentanteTField;
    private com.geopista.app.utilidades.JNumberTextField telefonoPromotorTField;

    /** Movil para la notificacion */
    private com.geopista.app.utilidades.JNumberTextField movilPropietarioTField;
    private com.geopista.app.utilidades.JNumberTextField movilRepresentanteTField;
    private com.geopista.app.utilidades.JNumberTextField movilPromotorTField;

    /** Fax para la notificacion */
    private com.geopista.app.utilidades.JNumberTextField faxPropietarioTField;
    private com.geopista.app.utilidades.JNumberTextField faxRepresentanteTField;
    private com.geopista.app.utilidades.JNumberTextField faxPromotorTField;

    /** tasa e impuesto */
    private com.geopista.app.utilidades.JNumberTextField tasaTextField;
    private com.geopista.app.utilidades.JNumberTextField impuestoTextField;

    /** fecha limite de obra */
    private javax.swing.JLabel fechaLimiteObraJLabel;
    private javax.swing.JTextField fechaLimiteObraJTField;
    private javax.swing.JButton fechaLimiteObraJButton;

    /** emplazamiento */
    private ComboBoxEstructuras tipoViaINEEJCBox;
    private com.geopista.app.utilidades.TextField nombreViaTField;
    //private com.geopista.app.utilidades.JNumberTextField numeroViaNumberTField;
    private com.geopista.app.utilidades.TextField numeroViaNumberTField;
    private com.geopista.app.utilidades.TextField portalViaTField;
    private com.geopista.app.utilidades.TextField plantaViaTField;
    private com.geopista.app.utilidades.TextField letraViaTField;
    private com.geopista.app.utilidades.JNumberTextField cpostalViaTField;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DNIPromotorJLabel;
    private javax.swing.JLabel DNIPropietarioJLabel;
    private javax.swing.JLabel DNIRepresentanteJLabel;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton publicarJButton;
    private javax.swing.JLabel apellido1PromotorJLabel;
    private javax.swing.JTextField apellido1PromotorJTField;
    private javax.swing.JLabel apellido1PropietarioJLabel;
    private javax.swing.JTextField apellido1PropietarioJTField;
    private javax.swing.JLabel apellido1RepresentanteJLabel;
    private javax.swing.JTextField apellido1RepresentanteJTField;
    private javax.swing.JLabel apellido2PromotorJLabel;
    private javax.swing.JTextField apellido2PromotorJTField;
    private javax.swing.JLabel apellido2PropietarioJLabel2;
    private javax.swing.JTextField apellido2PropietarioJTField;
    private javax.swing.JLabel apellido2RepresentanteJLabel;
    private javax.swing.JTextField apellido2RepresentanteJTField;
    private javax.swing.JScrollPane apunteJScrollPane;
    private javax.swing.JTextArea apunteJTArea;
    private javax.swing.JLabel asuntoExpedienteJLabel;
    private javax.swing.JTextField asuntoExpedienteJTField;
    private javax.swing.JLabel asuntoJLabel;
    private javax.swing.JButton borrarHistoricoJButton;
    private javax.swing.JButton borrarRepresentanteJButton;
    private javax.swing.JPanel botoneraJPanel;
    private javax.swing.JButton buscarDNIPromotorJButton;
    private javax.swing.JButton buscarDNIPropietarioJButton;
    private javax.swing.JButton buscarDNIRepresentanteJButton;
    private javax.swing.JButton buscarExpedienteJButton;
    private javax.swing.JLabel cPostalJLabel;
    private javax.swing.JLabel cPostalPromotorJLabel;
    private javax.swing.JLabel cPostalPropietarioJLabel;
    private javax.swing.JLabel cPostalRepresentanteJLabel;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JLabel colegioPromotorJLabel;
    private javax.swing.JTextField colegioPromotorJTField;
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
    private javax.swing.JPanel datosNotificacionesJPanel;
    private javax.swing.JLabel datosNotificarPorJLabel;
    private javax.swing.JTextField datosNotificarPorJTField;
    private javax.swing.JPanel datosPersonalesPromotorJPanel;
    private javax.swing.JPanel datosPersonalesPropietarioJPanel;
    private javax.swing.JPanel datosPersonalesRepresentanteJPanel;
    private javax.swing.JPanel datosSolicitudJPanel;
    private javax.swing.JButton deleteParcelaJButton;
    private javax.swing.JScrollPane descEventoJScrollPane;
    private javax.swing.JTextArea descEventoJTArea;
    private javax.swing.JLabel emailPromotorJLabel;
    private javax.swing.JTextField emailPromotorJTField;
    private javax.swing.JLabel emailPropietarioJLabel;
    private javax.swing.JTextField emailPropietarioJTField;
    private javax.swing.JLabel emailRepresentanteJLabel;
    private javax.swing.JTextField emailRepresentanteJTField;
    private javax.swing.JPanel emplazamientoJPanel;
    private javax.swing.JLabel entregadaAJLabel;
    private javax.swing.JLabel escaleraPromotorJLabel;
    private javax.swing.JTextField escaleraPromotorJTField;
    private javax.swing.JLabel escaleraPropietarioJLabel;
    private javax.swing.JTextField escaleraPropietarioJTField;
    private javax.swing.JLabel escaleraRepresentanteJLabel;
    private javax.swing.JTextField escaleraRepresentanteJTField;
    private javax.swing.JComboBox estadoExpedienteJCBox;
    private javax.swing.JLabel estadoExpedienteJLabel;
    private javax.swing.JPanel eventosJPanel;
    private javax.swing.JScrollPane eventosJScrollPane;
    private javax.swing.JTable eventosJTable;
    private javax.swing.JPanel expedienteJPanel;
    private javax.swing.JLabel faxPromotorJLabel;
    private javax.swing.JLabel faxPropietarioJLabel;
    private javax.swing.JLabel faxRepresentanteJLabel;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JTextField fechaAperturaJTField;
    private javax.swing.JLabel fechaSolicitudJLabel;
    private javax.swing.JTextField fechaSolicitudJTField;
    private javax.swing.JLabel finalizaJLabel;
    private javax.swing.JButton generarFichaJButton;
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
    private javax.swing.JButton mapToTableJButton;
    private javax.swing.JButton modHistoricoJButton;
    private javax.swing.JLabel motivoJLabel;
    private javax.swing.JLabel movilPromotorJLabel;
    private javax.swing.JLabel movilPropietarioJLabel;
    private javax.swing.JLabel movilRepresentanteJLabel;
    private javax.swing.JTextField municipioJTField;
    private javax.swing.JLabel municipioPromotorJLabel;
    private javax.swing.JTextField municipioPromotorJTField;
    private javax.swing.JLabel municipioPropietarioJLabel;
    private javax.swing.JTextField municipioPropietarioJTField;
    private javax.swing.JLabel municipioRepresentanteJLabel;
    private javax.swing.JTextField municipioRepresentanteJTField;
    private javax.swing.JLabel nombrePromotorJLabel;
    private javax.swing.JTextField nombrePromotorJTField;
    private javax.swing.JLabel nombrePropietarioJLabel;
    private javax.swing.JTextField nombrePropietarioJTField;
    private javax.swing.JLabel nombreRepresentanteJLabel;
    private javax.swing.JTextField nombreRepresentanteJTField;
    private javax.swing.JButton nombreViaJButton;
    private javax.swing.JLabel nombreViaJLabel;
    private javax.swing.JLabel nombreViaPromotorJLabel;
    private javax.swing.JTextField nombreViaPromotorJTField;
    private javax.swing.JLabel nombreViaPropietarioJLabel;
    private javax.swing.JTextField nombreViaPropietarioJTField;
    private javax.swing.JLabel nombreViaRepresentanteJLabel;
    private javax.swing.JTextField nombreViaRepresentanteJTField;
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
    private javax.swing.JButton nuevoHistoricoJButton;
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JLabel numRegistroJLabel;
    private javax.swing.JTextField numRegistroJTField;
    private javax.swing.JLabel numeroViaJLabel;
    private javax.swing.JLabel numeroViaPromotorJLabel;
    private javax.swing.JLabel numeroViaPropietarioJLabel;
    private javax.swing.JLabel numeroViaRepresentanteJLabel;
    private javax.swing.JPanel obraMayorJPanel;
    private javax.swing.JTabbedPane obraMayorJTabbedPane;
    private javax.swing.JTabbedPane jTabbedPaneSolicitud;
    private javax.swing.JLabel observacionesExpedienteJLabel;
    private javax.swing.JScrollPane observacionesExpedienteJScrollPane;
    private javax.swing.JLabel observacionesJLabel;
    private javax.swing.JScrollPane observacionesJScrollPane;
    private javax.swing.JButton okJButton;
    private javax.swing.JLabel plantaPromotorJLabel;
    private javax.swing.JTextField plantaPromotorJTField;
    private javax.swing.JLabel plantaPropietarioJLabel;
    private javax.swing.JTextField plantaPropietarioJTField;
    private javax.swing.JLabel plantaRepresentanteJLabel;
    private javax.swing.JTextField plantaRepresentanteJTField;
    private javax.swing.JLabel portalPromotorJLabel;
    private javax.swing.JTextField portalPromotorJTField;
    private javax.swing.JLabel portalPropietarioJLabel;
    private javax.swing.JTextField portalPropietarioJTField;
    private javax.swing.JLabel portalRepresentanteJLabel;
    private javax.swing.JTextField portalRepresentanteJTField;
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
    private javax.swing.JButton refCatastralJButton;
    private javax.swing.JLabel refCatastralJLabel;
    private javax.swing.JTextField refCatastralJTextField;
    private javax.swing.JScrollPane referenciasCatastralesJScrollPane;
    private javax.swing.JPanel representanteJPanel;
    private javax.swing.JLabel responsableJLabel;
    private javax.swing.JLabel servicioExpedienteJLabel;
    private javax.swing.JTextField servicioExpedienteJTField;
    private javax.swing.JCheckBox silencioJCheckBox;
    private javax.swing.JLabel silencioJLabel;
    private javax.swing.JButton tableToMapJButton;
    private javax.swing.JLabel tasaJLabel;
    private javax.swing.JLabel telefonoPromotorJLabel;
    private javax.swing.JLabel telefonoPropietarioJLabel;
    private javax.swing.JLabel telefonoRepresentanteJLabel;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoObraJLabel;
    private javax.swing.JLabel tipoViaPromotorJLabel;
    private javax.swing.JLabel tipoViaPropietarioJLabel;
    private javax.swing.JLabel tipoViaRepresentanteJLabel;
    private javax.swing.JLabel titulacionPromotorJLabel;
    private javax.swing.JTextField titulacionPromotorJTField;
    private javax.swing.JLabel tramitacionJLabel;
    private javax.swing.JLabel unidadRJLabel;
    private javax.swing.JTextField unidadRJTField;
    private javax.swing.JLabel unidadTJLabel;
    private javax.swing.JTextField unidadTJTField;
    private javax.swing.JLabel viaNotificacionPromotorJLabel;
    private javax.swing.JLabel viaNotificacionPropietarioJLabel;
    private javax.swing.JLabel viaNotificacionRepresentanteJLabel;
    private javax.swing.JLabel visadoPromotorJLabel;
    private javax.swing.JTextField visadoPromotorJTField;
    private javax.swing.JButton jButtonGenerarFicha;
    private javax.swing.JButton jButtonWorkFlow;
    private JPanelResolucion jPanelResolucion;
    private javax.swing.JLabel jLabelEstadoActual;
    private javax.swing.JLabel entregadaATextJLabel;
    // End of variables declaration//GEN-END:variables

}
