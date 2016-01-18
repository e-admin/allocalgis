/**
 * CModificacionLicencias.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * CModificacionLicencias.java
  *
 * Created on 16 de abril de 2004, 14:38
 */

package com.geopista.app.ocupaciones;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.ocupaciones.panel.CAddressJDialog;
import com.geopista.app.ocupaciones.panel.CCalendarJDialog;
import com.geopista.app.ocupaciones.panel.CDatosReferenciaCatastralJDialog;
import com.geopista.app.ocupaciones.panel.CHistoricoJDialog;
import com.geopista.app.ocupaciones.panel.CPersonaJDialog;
import com.geopista.app.ocupaciones.panel.CSearchJDialog;
import com.geopista.app.ocupaciones.panel.CSearchLicenciasObraJDialog;
import com.geopista.app.ocupaciones.panel.JPanelCallesAfectadas;
import com.geopista.app.printer.FichasDisponibles;
import com.geopista.app.printer.GeopistaPrintableOcupaciones;
import com.geopista.app.utilidades.GeoPistaFileFilter;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.CellRendererEstructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxRendererEstructuras;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.client.alfresco.servlet.AlfrescoDocumentClient;
import com.geopista.client.alfresco.ui.AlfrescoExplorer;
import com.geopista.client.alfresco.utils.implementations.LocalgisIntegrationManagerImpl;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.contaminantes.NumeroPolicia;
import com.geopista.protocol.licencias.CAnexo;
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
import com.geopista.protocol.licencias.tipos.CTipoAnexo;
import com.geopista.protocol.licencias.tipos.CTipoTramitacion;
import com.geopista.protocol.ocupacion.CDatosOcupacion;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;


/**
 * @author avivar
 */
public class CModificacionLicencias extends javax.swing.JInternalFrame implements IMultilingue{
    private final String layerName="ocupaciones";
    private Hashtable nuevasFeatures= new Hashtable();
    private boolean searchExpCanceled= false;

	/**
	 * Datos del Propietario
	 */
    private CPersonaJuridicoFisica propietario;
	private String DNI_CIF_Propietario = "";
    private boolean emailPropietarioObligatorio= false;
    /**
	 * Datos del Representante
	 */
    private CPersonaJuridicoFisica representante;
    private CPersonaJuridicoFisica representanteBuscado;
	private String DNI_CIF_Representante = "";
    private boolean emailRepresentanteObligatorio= false;


	/**
	 * Datos de la solicitud
	 */
	private int _tipoOcupacion = -1;
	/*private String _motivo = "";
	private String _asunto = "";*/
	private String observaciones = "";

	/**
	 * Datos de Expediente
	 */

	private Hashtable _hAnexosAnnadidosExpediente = new Hashtable();
	private Vector _vAnexosSolicitud = null;
	private Hashtable _hAnexosSolicitud = new Hashtable();
    private CSolicitudLicencia solicitud = null;
	private CExpedienteLicencia expediente = null;

	/**
	 * Modelo para el componente listaAnexosJTable
	 */
	private CListaAnexosTableModel _listaAnexosTableModel;
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
	private String _vu = "0";

    private boolean fromMenu= true;

	Logger logger = Logger.getLogger(CModificacionLicencias.class);

    private long maxSizeFilesUploaded= 0;

    /** Ordenacion de tablas */
    TableSorted notificacionesSorted= new TableSorted();
    TableSorted eventosSorted= new TableSorted();
    TableSorted historicoSorted= new TableSorted();
    int notificacionesHiddenCol= 5;
    int eventosHiddenCol= 5;
    int historicoHiddenCol= 4;



	/**
	 * Creates new form CModificacionLicencias
	 */
	public CModificacionLicencias(JFrame desktopFrame, String numExpediente, boolean calledFromMenu) {
		desktop = desktopFrame;
        fromMenu= calledFromMenu;
		CUtilidadesComponentes.menuLicenciasSetEnabled(false, desktop);
		initComponents();
		initComponentesEstructuras();
		configureComponents();
		String mostrarMapa=System.getProperty("mostrarMapa");
		if(mostrarMapa==null || !mostrarMapa.equals("false")){
			CUtilidadesComponentes.showGeopistaEditor(desktop,editorMapaJPanel, CMainOcupaciones.geopistaEditor, true);
		}
		CMainOcupaciones.geopistaEditor.removeAllGeopistaListener();
        CMainOcupaciones.geopistaEditor.addGeopistaListener(new GeopistaListener() {
			public void selectionChanged(IAbstractSelection abtractSelection) {
				logger.info("selectionChanged");
			}

			public void featureAdded(FeatureEvent e) {
                Collection features = e.getFeatures();
                GeopistaLayer layer= (GeopistaLayer)CMainOcupaciones.geopistaEditor.getLayerManager().getLayer(layerName);
                for (Iterator i = features.iterator(); i.hasNext();) {
                    Feature f = (Feature) i.next();
                    GeopistaFeature geoF = ShowMaps.saveFeature(f, layer, layerName,com.geopista.protocol.CConstantesComando.adminCartografiaUrl);
					if (geoF.getSystemId() == null || geoF.getSystemId().length() <= 0) {
						JOptionPane optionPane = new JOptionPane(CMainOcupaciones.literales.getString("CCreacionLicencias.insertError"), JOptionPane.ERROR_MESSAGE);
						JDialog dialog = optionPane.createDialog(CModificacionLicencias.desktop, "ERROR");
						dialog.show();
						CMainOcupaciones.geopistaEditor.deleteSelectedFeatures();
                    } else {
						((GeopistaFeature) f).setSystemId(geoF.getSystemId());
						f.setAttribute(0,new Integer(geoF.getSystemId()));
					    nuevasFeatures.put(geoF.getSystemId(),f);
                    	refreshFeatureSelection(layerName, 0,geoF.getSystemId());
                        NumeroPolicia numeroPolicia= new NumeroPolicia();
                        try
                        {
                            CResultadoOperacion resultado= COperacionesLicencias.getDireccionMasCercana((String)f.getAttribute(3).toString());
					        if (resultado.getResultado())
                            {
                                numeroPolicia= (NumeroPolicia)resultado.getVector().elementAt(0);
                                JOptionPane optionPane = new JOptionPane(CMainOcupaciones.literales.getString("direccionMasCercana.text")+" "+numeroPolicia.toString(), JOptionPane.INFORMATION_MESSAGE);
			                    JDialog dialog = optionPane.createDialog(desktop, "INFO");
			                    dialog.show();
                             }
                            else
                            {
                                logger.info("No se ha encontrado la direccion: "+resultado.getDescripcion());
                            }
                        }catch(Exception ex){}

                        referenciasCatastralesJTableModel.addRow(new Object[]{
                                                              geoF.getSystemId(),
                                                              new Long(((Number)geoF.getAttribute(2)).longValue()),
                                                              numeroPolicia.getTipovia()==null?"":numeroPolicia.getTipovia(),
                                                              numeroPolicia.getNombrevia()==null?"":numeroPolicia.getNombrevia(),
                                                              numeroPolicia.getRotulo()==null?"":numeroPolicia.getRotulo(),
                                                              "","","","",""});

                    }
                }
            	logger.info("featureAdded");
			}

			public void featureRemoved(FeatureEvent e) {
				logger.info("featureRemoved");
			}

			public void featureModified(FeatureEvent e) {
                Collection features = e.getFeatures();
                GeopistaLayer layer= (GeopistaLayer)CMainOcupaciones.geopistaEditor.getLayerManager().getLayer(layerName);
				for (Iterator i = features.iterator(); i.hasNext();) {
					Feature f = (Feature) i.next();
					ShowMaps.updateFeature(f, layer, layerName,com.geopista.protocol.CConstantesComando.adminCartografiaUrl);
                    nuevasFeatures.put(((GeopistaFeature)f).getSystemId(),f);
				}
				logger.info("featureModified");
			}

			public void featureActioned(IAbstractSelection abtractSelection) {
				logger.info("featureActioned");
			}

		});
		if (numExpediente != null) {
			numExpedienteJTField.setText(numExpediente);
            if (fromMenu){
			    consultar();
            }
		}
	}

	private boolean configureComponents() {

		try {

			habilesJLabel.setVisible(false);
			habilesJCheckBox.setVisible(false);
			if (CMainOcupaciones.geopistaEditor ==null)CMainOcupaciones.geopistaEditor = new GeopistaEditor("workbench-properties-simple.xml");

			String[] columnNamesAnexos = {CMainOcupaciones.literales.getString("CModificacionLicenciasForm.anexosJTable.column1"),
										  CMainOcupaciones.literales.getString("CModificacionLicenciasForm.anexosJTable.column2"),
										  CMainOcupaciones.literales.getString("CModificacionLicenciasForm.anexosJTable.column3"),
										  CMainOcupaciones.literales.getString("CModificacionLicenciasForm.anexosJTable.column4")};

			CListaAnexosTableModel.setColumnNames(columnNamesAnexos);
			_listaAnexosTableModel = new CListaAnexosTableModel();

			String[] columnNamesNotificaciones = {CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum1"),
												  CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum2"),
												  CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum3"),
												  CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum4"),
												  CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum5"),
                                                  "HIDDEN"};
			CNotificacionesModificacionTableModel.setColumnNames(columnNamesNotificaciones);
			_notificacionesExpedienteTableModel = new CNotificacionesModificacionTableModel();

			String[] columnNamesEventos = {CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum1"),
										   CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum2"),
										   CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum3"),
										   CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum4"),
										   CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum5"),
                                           "HIDDEN"};
			CEventosModificacionTableModel.setColumnNames(columnNamesEventos);
			_eventosExpedienteTableModel = new CEventosModificacionTableModel();

			String[] columnNamesHistorico = {//CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJTable.colum1"),
											 CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJTable.colum2"),
											 CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJTable.colum3"),
											 CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJTable.colum4"),
											 CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJTable.colum5"),
                                             "HIDDEN"};
			CHistoricoModificacionTableModel.setColumnNames(columnNamesHistorico);
			_historicoExpedienteTableModel = new CHistoricoModificacionTableModel();


            referenciasCatastralesJTableModel = new CReferenciasCatastralesTableModel(new String[]{CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column1"),
                                                                                           CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column10"),
                                                                                           CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column2"),
                                                                                           CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column3"),
                                                                                           CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column4"),
                                                                                           CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column5"),
                                                                                           CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column6"),
                                                                                           CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column7"),
                                                                                           CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column8"),
                                                                                           CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column9")});
			referenciasCatastralesJTable.setModel(referenciasCatastralesJTableModel);
			referenciasCatastralesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			referenciasCatastralesJTable.setCellSelectionEnabled(false);
			referenciasCatastralesJTable.setColumnSelectionAllowed(false);
			referenciasCatastralesJTable.setRowSelectionAllowed(true);
            referenciasCatastralesJTable.getTableHeader().setReorderingAllowed(false);
			referenciasCatastralesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			for (int j = 0; j < referenciasCatastralesJTable.getColumnCount(); j++) {
				TableColumn column = referenciasCatastralesJTable.getColumnModel().getColumn(j);
                if (j==2){
                    column.setMinWidth(75);
                    column.setMaxWidth(150);
                    column.setWidth(75);
                    column.setPreferredWidth(75);
                }else if ((j>3) || (j == 1)){
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
            //****************************
            // Annadimos a la tabla el editor ComboBox en la segunda columna (tipoVia)
            TableColumn column= referenciasCatastralesJTable.getColumnModel().getColumn(2);
            ComboBoxEstructuras comboBoxEstructuras= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainOcupaciones.currentLocale.toString(), true);
            comboBoxEstructuras.setSelectedIndex(0);

            ComboBoxTableEditor comboBoxTableEditor= new ComboBoxTableEditor(comboBoxEstructuras);
            comboBoxTableEditor.setEnabled(true);
            column.setCellEditor(comboBoxTableEditor);

            CellRendererEstructuras renderer =
                new CellRendererEstructuras(CMainOcupaciones.literales.getLocale().toString(),com.geopista.app.ocupaciones.Estructuras.getListaTiposViaINE());
            column.setCellRenderer(renderer);


            // Annadimos a la tabla el editor TextField en el resto de columnas
            for (int col=3; col < referenciasCatastralesJTable.getColumnModel().getColumnCount(); col++){
                column= referenciasCatastralesJTable.getColumnModel().getColumn(col);
                TextFieldTableEditor textFieldTableEditor= null;
                if (col == 3){
                    // Nombre
                    textFieldTableEditor= new TextFieldTableEditor(68);
                }else if (col == 4){
                    // Numero
                    //textFieldTableEditor= new TextFieldTableEditor(true, 99999);
                    textFieldTableEditor= new TextFieldTableEditor(8);
                }else{
                    // resto de campos
                    textFieldTableEditor= new TextFieldTableEditor(5);
                }
                textFieldTableEditor.setEnabled(true);
                column.setCellEditor(textFieldTableEditor);
                column.setCellRenderer(new TextFieldRenderer());
            }


			/** Solicitud */
			municipioJTField.setText(CConstantesOcupaciones.Municipio);
			municipioJTField.setEnabled(false);
            municipioJTField.setEditable(true);
			provinciaJTField.setText(CConstantesOcupaciones.Provincia);
			provinciaJTField.setEnabled(false);
            provinciaJTField.setEditable(true);
			eliminarJButton.setEnabled(false);

			/** Anexos */
			listaAnexosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listaAnexosJTable.setModel(_listaAnexosTableModel);
			listaAnexosJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            listaAnexosJTable.getTableHeader().setReorderingAllowed(false);
			for (int j = 0; j < listaAnexosJTable.getColumnCount(); j++) {
				column = listaAnexosJTable.getColumnModel().getColumn(j);
				if (j == 2) {
					column.setMinWidth(250);
					column.setMaxWidth(300);
					column.setWidth(250);
					column.setPreferredWidth(250);
				} else {
					column.setMinWidth(150);
					column.setMaxWidth(300);
					column.setWidth(150);
					column.setPreferredWidth(150);
				}
				column.setResizable(true);
			}


            /** Definimos el componente notificacionesJTable */
            notificacionesSorted= new TableSorted(_notificacionesExpedienteTableModel);
            notificacionesSorted.setTableHeader(notificacionesJTable.getTableHeader());
            notificacionesJTable.setModel(notificacionesSorted);
			notificacionesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			notificacionesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            notificacionesJTable.getTableHeader().setReorderingAllowed(false);
			for (int j = 0; j < notificacionesJTable.getColumnCount(); j++) {
				column = notificacionesJTable.getColumnModel().getColumn(j);
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
			for (int j = 0; j < eventosJTable.getColumnCount(); j++) {
				column = eventosJTable.getColumnModel().getColumn(j);
				if (j == 2) {
					column.setMinWidth(75);
					column.setMaxWidth(75);
					column.setWidth(75);
					column.setPreferredWidth(75);
				} else {
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
			for (int j = 0; j < historicoJTable.getColumnCount(); j++) {
				column = historicoJTable.getColumnModel().getColumn(j);
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


            jButtonPlazoVen.setIcon(CUtilidadesComponentes.iconoZoom);

			buscarExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);
			nombreViaJButton.setIcon(CUtilidadesComponentes.iconoZoom);
			borrarRepresentanteJButton.setIcon(CUtilidadesComponentes.iconoDeleteParcela);
			
            tableToMapOneJButton.setIcon(CUtilidadesComponentes.iconoFlecha);
			tableToMapAllJButton.setIcon(CUtilidadesComponentes.iconoDobleFlecha);

			buscarDNIPropietarioJButton.setIcon(CUtilidadesComponentes.iconoZoom);
			buscarDNIRepresentanteJButton.setIcon(CUtilidadesComponentes.iconoZoom);
            buscarLicenciaObraJButton.setIcon(CUtilidadesComponentes.iconoZoom);

			/** Historico */
			if (expediente == null) {
				nuevoHistoricoJButton.setEnabled(false);
				borrarHistoricoJButton.setEnabled(false);
				modHistoricoJButton.setEnabled(false);
			}


            /** emplazamiento */
            nombreViaTField= new com.geopista.app.utilidades.TextField(68);
            nombreViaTField.setEditable(true);
            emplazamientoJPanel.add(nombreViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 190, -1));

            //numeroViaNumberTField=  new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            numeroViaNumberTField=  new com.geopista.app.utilidades.TextField(8);
            numeroViaNumberTField.setEditable(true);
            emplazamientoJPanel.add(numeroViaNumberTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 80, -1));

            portalViaTField= new com.geopista.app.utilidades.TextField(5);
            portalViaTField.setEditable(true);
            emplazamientoJPanel.add(portalViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 80, -1));

            plantaViaTField= new com.geopista.app.utilidades.TextField(5);
            plantaViaTField.setEditable(true);
            emplazamientoJPanel.add(plantaViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 70, -1));

            letraViaTField= new com.geopista.app.utilidades.TextField(5);
            letraViaTField.setEditable(true);
            emplazamientoJPanel.add(letraViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, 70, -1));

            cpostalViaTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
            cpostalViaTField.setEditable(true);
            emplazamientoJPanel.add(cpostalViaTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 80, -1));

            /** Borrar Geopista Feature */
            deleteGeopistaFeatureJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
            deleteGeopistaFeatureJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
            deleteGeopistaFeatureJButton.setPreferredSize(new java.awt.Dimension(30, 30));
            deleteGeopistaFeatureJButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    deleteGeopistaFeatureJButtonActionPerformed();
                }
            });
            emplazamientoJPanel.add(deleteGeopistaFeatureJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 180, 20, 20));

            /** Historico */
            modHistoricoJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoOK);
            borrarHistoricoJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoRemove);
            nuevoHistoricoJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoAdd);
            generarFichaHistoricoJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoGenerarFicha);

            /** Anexos */
            abrirJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoAbrir);
            annadirJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoAdd);
            eliminarJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoRemove);

            /** entregada a */
            entregadaATField= new com.geopista.app.utilidades.TextField(68);
            datosNotificacionJPanel.add(entregadaATField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 160, 260, 20));
            entregadaATField.setVisible(false);
            okJButton.setVisible(false);
            entregadaATextJLabel.setText("");
            datosNotificacionJPanel.add(entregadaATextJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 260, 20));

			renombrarComponentes(true);

			return true;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}

	}

    private void deleteGeopistaFeatureJButtonActionPerformed() {//GEN-FIRST:event_deleteRegistroCatastralJButtonActionPerformed
        int selectedRow = referenciasCatastralesJTable.getSelectedRow();
        logger.info("selectedRow: " + selectedRow);

        if (selectedRow != -1) {
            String sIdFeature= (String)referenciasCatastralesJTableModel.getValueAt(selectedRow,0);
            /** Borramos la feature del Mapa */
            try{
                refreshFeatureSelection(layerName,0,sIdFeature);
                ShowMaps.deleteFeature(CMainOcupaciones.geopistaEditor);
            }catch(Exception ex){
                logger.error("Exception al borrar el objeto: " ,ex);
            }
            referenciasCatastralesJTableModel.removeRow(selectedRow);
            nuevasFeatures.remove(sIdFeature);

            /** Borramos la feature de BD (geometria_ocupacion) */
            COperacionesLicencias.deleteGeometriaOcupacion(sIdFeature);
       }
   }//GEN-LAST:event_deleteRegistroCatastralJButtonActionPerformed


	/**
	 * Los estados no pueden redefinirse como dominio, puesto que necesitamos el valor del campo step
	 */
	public void initComponentesEstructuras() {
		while (!Estructuras.isCargada()) {
			if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}

		/** Inicializamos los comboBox que llevan estructuras */
		/** solicitud */
		tipoOcupacionEJCBox = new ComboBoxEstructuras(Estructuras.getListaTipoOcupacion(), null, CMainOcupaciones.currentLocale.toString(), false);
		datosSolicitudJPanel.add(tipoOcupacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 140, -1));

		horaInicioJTimeTField = new JNumberTextField(JNumberTextField.BASE_HM);
		datosSolicitudJPanel.add(horaInicioJTimeTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, 60, -1));
		horaFinJTimeTField = new JNumberTextField(JNumberTextField.BASE_HM);
		datosSolicitudJPanel.add(horaFinJTimeTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, 60, -1));

		numSillasJNTField = new JNumberTextField(JNumberTextField.NUMBER, new Integer(999999), true);
		datosSolicitudJPanel.add(numSillasJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, 150, -1));

		numMesasJNTField = new JNumberTextField(JNumberTextField.NUMBER, new Integer(999999), true);
		datosSolicitudJPanel.add(numMesasJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 140, -1));

		areaOcupacionJNTField = new JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
		datosSolicitudJPanel.add(areaOcupacionJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 120, -1));

		m2AceraJNTField = new JNumberTextField(JNumberTextField.REAL, new Integer(99999), true, 2);
		datosSolicitudJPanel.add(m2AceraJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, 110, -1));

		m2CalzadaJNTField = new JNumberTextField(JNumberTextField.REAL, new Integer(99999), true, 2);
		datosSolicitudJPanel.add(m2CalzadaJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 110, -1));

		m2AparcamientoJNTField = new JNumberTextField(JNumberTextField.REAL, new Integer(99999), true, 2);
		datosSolicitudJPanel.add(m2AparcamientoJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, 110, -1));

		/**expediente */
		finalizacionEJCBox = new ComboBoxEstructuras(Estructuras.getListaTiposFinalizacion(), null, CMainOcupaciones.currentLocale.toString(), true);
        finalizacionEJCBox.setEnabled(false);
        finalizacionEJCBox.setEditable(false);
		expedienteJPanel.add(finalizacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 390, 300, -1));

		tramitacionEJCBox = new ComboBoxEstructuras(Estructuras.getListaTiposTramitacion(), null, CMainOcupaciones.currentLocale.toString(), false);
		expedienteJPanel.add(tramitacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 152, 300, 20));

        estadoExpedienteEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadosOcupacion(), null, CMainOcupaciones.currentLocale.toString(), false);
        expedienteJPanel.add(estadoExpedienteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 300, 20));

		/** propietario */
		viaNotificacionPropietarioEJCBox = new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainOcupaciones.currentLocale.toString(), false);
        viaNotificacionPropietarioEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionPropietarioEJCBoxActionPerformed();}});

		datosNotificacionPropietarioJPanel.add(viaNotificacionPropietarioEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 300, 20));
		tipoViaNotificacionPropietarioEJCBox = new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainOcupaciones.currentLocale.toString(), false);
		datosNotificacionPropietarioJPanel.add(tipoViaNotificacionPropietarioEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 300, 20));

		/** representante */
		viaNotificacionRepresentanteEJCBox = new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainOcupaciones.currentLocale.toString(), false);
        viaNotificacionRepresentanteEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionRepresentanteEJCBoxActionPerformed();}});

		datosNotificacionRepresentanteJPanel.add(viaNotificacionRepresentanteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 300, 20));
		tipoViaNotificacionRepresentanteEJCBox = new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainOcupaciones.currentLocale.toString(), false);
		datosNotificacionRepresentanteJPanel.add(tipoViaNotificacionRepresentanteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 300, 20));

        tipoViaINEEJCBox= new ComboBoxEstructuras(com.geopista.app.ocupaciones.Estructuras.getListaTiposViaINE(), null, CMainOcupaciones.currentLocale.toString(), true);
        tipoViaINEEJCBox.setSelectedIndex(0);
        emplazamientoJPanel.add(tipoViaINEEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 110, -1));


	}

    private void viaNotificacionPropietarioEJCBoxActionPerformed() {
        String index= viaNotificacionPropietarioEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesOcupaciones.patronNotificacionEmail)){
            emailPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.emailPropietarioJLabel.text")));
            emailPropietarioObligatorio= true;
        }else{
            emailPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.emailPropietarioJLabel.text"));
            emailPropietarioObligatorio= false;
        }

    }

    private void viaNotificacionRepresentanteEJCBoxActionPerformed() {

        String index= viaNotificacionRepresentanteEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesOcupaciones.patronNotificacionEmail)){
            emailRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.emailRepresentanteJLabel.text")));
            emailRepresentanteObligatorio= true;
        }else{
            emailRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.emailRepresentanteJLabel.text"));
            emailRepresentanteObligatorio= false;
        }

    }

    private void fechaPlazoVen()
    {
        CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(desktop);
        if (dialog != null){
            String fecha= dialog.getFechaSelected();
            if ((fecha != null) && (!fecha.trim().equals(""))) {
                plazoVencimientoJTField.setText(fecha);
            }
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
        ocupacionJTabbedPane = new javax.swing.JTabbedPane();
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
        numFoliosJLabel = new javax.swing.JLabel();
        numFoliosJTField = new javax.swing.JTextField();
        responsableJLabel = new javax.swing.JLabel();
        responsableJTField = new javax.swing.JTextField();
        plazoVencimientoJLabel = new javax.swing.JLabel();
        numDiasJTField = new javax.swing.JTextField();
        numDiasJLabel = new javax.swing.JLabel();
        habilesJCheckBox = new javax.swing.JCheckBox();
        silencioJCheckBox = new javax.swing.JCheckBox();
        plazoVencimientoJTField = new javax.swing.JTextField();
        buscarExpedienteJButton = new javax.swing.JButton();
        consultarJButton = new javax.swing.JButton();
        silencioJLabel = new javax.swing.JLabel();
        habilesJLabel = new javax.swing.JLabel();
        notaJLabel = new javax.swing.JLabel();
        observacionesExpedienteJScrollPane = new javax.swing.JScrollPane();
        observacionesExpedienteJTArea = new javax.swing.JTextArea();
        ocupacionJPanel = new javax.swing.JPanel();
        datosSolicitudJPanel = new javax.swing.JPanel();
        tipoOcupacionJLabel = new javax.swing.JLabel();
        motivoJLabel = new javax.swing.JLabel();
        asuntoJLabel = new javax.swing.JLabel();
        fechaSolicitudJLabel = new javax.swing.JLabel();
        observacionesJLabel = new javax.swing.JLabel();
        motivoJTField = new javax.swing.JTextField();
        asuntoJTField = new javax.swing.JTextField();
        fechaSolicitudJTField = new javax.swing.JTextField();
        observacionesJScrollPane = new javax.swing.JScrollPane();
        observacionesJTArea = new javax.swing.JTextArea();
        numExpedienteObraJLabel = new javax.swing.JLabel();
        numExpedienteObraJTField = new javax.swing.JTextField();
        necesitaObraJLabel = new javax.swing.JLabel();
        necesitaObraJCheckBox = new javax.swing.JCheckBox();
        horaJLabel = new javax.swing.JLabel();
        aceraJCheckBox = new javax.swing.JCheckBox();
        aparcamientoJCheckBox = new javax.swing.JCheckBox();
        calzadaJCheckBox = new javax.swing.JCheckBox();
        mesasJLabel = new javax.swing.JLabel();
        metros2JLabel = new javax.swing.JLabel();
        afectaJLabel = new javax.swing.JLabel();
        numSillasJLabel = new javax.swing.JLabel();
        areaOcupacionJLabel = new javax.swing.JLabel();
        buscarLicenciaObraJButton = new javax.swing.JButton();
        emplazamientoJPanel = new javax.swing.JPanel();
        nombreViaJLabel = new javax.swing.JLabel();
        numeroViaJLabel = new javax.swing.JLabel();
        cPostalJLabel = new javax.swing.JLabel();
        provinciaJLabel = new javax.swing.JLabel();
        municipioJTField = new javax.swing.JTextField();
        provinciaJTField = new javax.swing.JTextField();
        nombreViaJButton = new javax.swing.JButton();
        referenciasCatastralesJScrollPane = new javax.swing.JScrollPane();
        referenciasCatastralesJTable = new javax.swing.JTable();
        tableToMapOneJButton = new javax.swing.JButton();
        tableToMapAllJButton = new javax.swing.JButton();
        anexosJPanel = new javax.swing.JPanel();
        annadirJButton = new javax.swing.JButton();
        eliminarJButton = new javax.swing.JButton();
        listaAnexosJScrollPane = new javax.swing.JScrollPane();
        listaAnexosJTable = new javax.swing.JTable();
        abrirJButton = new javax.swing.JButton();
        propietarioJPanel = new javax.swing.JPanel();
        datosPersonalesPropietarioJPanel = new javax.swing.JPanel();
        DNIPropietarioJLabel = new javax.swing.JLabel();
        DNIPropietarioJTField = new javax.swing.JTextField();
        nombrePropietarioJLabel = new javax.swing.JLabel();
        nombrePropietarioJTField = new javax.swing.JTextField();
        apellido1PropietarioJLabel = new javax.swing.JLabel();
        apellido2PropietarioJLabel = new javax.swing.JLabel();
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
        botoneraJPanel = new javax.swing.JPanel();
        aceptarJButton = new javax.swing.JButton();
        
        publicarJButton = new javax.swing.JButton();
        
        cancelarJButton = new javax.swing.JButton();
        editorMapaJPanel = new javax.swing.JPanel();
        jButtonGenerarFicha= new javax.swing.JButton();
        deleteGeopistaFeatureJButton= new javax.swing.JButton();
        entregadaAJLabel = new javax.swing.JLabel();
        okJButton = new javax.swing.JButton();
        generarFichaHistoricoJButton = new javax.swing.JButton();
        areaOcupacionJButton = new javax.swing.JButton();
        jPanelCallesAfectadas= new JPanelCallesAfectadas(desktop,CMainOcupaciones.literales);
        entregadaATextJLabel= new javax.swing.JLabel();
        alfrescoJButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        setClosable(true);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown();
            }
        });


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
        ocupacionJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        ocupacionJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));


        expedienteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expedienteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Expediente"));
        expedienteJPanel.setAutoscrolls(true);
        expedienteJPanel.add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 130, 20));
        expedienteJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));
        expedienteJPanel.add(servicioExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 131, 130, 20));
        expedienteJPanel.add(tramitacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 152, 130, 20));
        expedienteJPanel.add(finalizaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 130, 20));
        expedienteJPanel.add(asuntoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 173, 130, 20));
        expedienteJPanel.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 194, 130, 20));
        expedienteJPanel.add(observacionesExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 130, 20));

        expedienteJPanel.add(servicioExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 131, 300, -1));

        expedienteJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 280, -1));

        expedienteJPanel.add(asuntoExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 173, 300, -1));

        fechaAperturaJTField.setEnabled(false);
        expedienteJPanel.add(fechaAperturaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 194, 300, -1));

        expedienteJPanel.add(inicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 215, 130, 20));
        expedienteJPanel.add(inicioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 215, 300, -1));
        expedienteJPanel.add(numFoliosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 267, 130, 20));
        expedienteJPanel.add(numFoliosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 267, 300, -1));
        expedienteJPanel.add(responsableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 288, 130, 20));
        expedienteJPanel.add(responsableJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 288, 300, -1));
        expedienteJPanel.add(plazoVencimientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 309, 130, 20));

        numDiasJTField.setEnabled(true);
        expedienteJPanel.add(numDiasJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 330, 300, -1));
        expedienteJPanel.add(numDiasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 130, 20));

        habilesJCheckBox.setEnabled(false);
        expedienteJPanel.add(habilesJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 351, -1, -1));

        expedienteJPanel.add(silencioJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 236, 30, -1));

        jButtonGenerarFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarFicha();
            }
        });

        jButtonPlazoVen = new javax.swing.JButton();
        jButtonPlazoVen.setPreferredSize(new java.awt.Dimension(20,20));
        jButtonPlazoVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaPlazoVen();
            }
        });
        expedienteJPanel.add(jButtonPlazoVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 309, -1, -1));
        plazoVencimientoJTField.setEnabled(false);
        expedienteJPanel.add(plazoVencimientoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 309, 280, -1));
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
                consultar();
            }
        });

        expedienteJPanel.add(consultarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 130, -1));

        expedienteJPanel.add(silencioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 236, 160, 20));
        expedienteJPanel.add(habilesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 351, 130, 20));

        notaJLabel.setFont(new java.awt.Font("Arial", 0, 10));
        expedienteJPanel.add(notaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 236, 260, 20));

        observacionesExpedienteJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        observacionesExpedienteJTArea.setLineWrap(true);
        observacionesExpedienteJTArea.setRows(3);
        observacionesExpedienteJTArea.setTabSize(4);
        observacionesExpedienteJTArea.setWrapStyleWord(true);
        observacionesExpedienteJTArea.setBorder(null);
        observacionesExpedienteJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        observacionesExpedienteJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        observacionesExpedienteJScrollPane.setViewportView(observacionesExpedienteJTArea);

        expedienteJPanel.add(observacionesExpedienteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, 300, 90));

        ocupacionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder("Datos solicitud"));
        datosSolicitudJPanel.setAutoscrolls(true);
        datosSolicitudJPanel.add(tipoOcupacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 20, 90, 20));

        datosSolicitudJPanel.add(motivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 90, 20));

        asuntoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(asuntoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 90, 20));

        fechaSolicitudJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(fechaSolicitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 90, 20));
        datosSolicitudJPanel.add(observacionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 175, 90, 20));
        datosSolicitudJPanel.add(motivoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 140, -1));
        datosSolicitudJPanel.add(asuntoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 150, -1));

        fechaSolicitudJTField.setEditable(false);
        fechaSolicitudJTField.setEnabled(false);
        datosSolicitudJPanel.add(fechaSolicitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 150, -1));

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

        datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 175, 400, 40));

        numExpedienteObraJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(numExpedienteObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 90, 20));
        datosSolicitudJPanel.add(numExpedienteObraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, 130, -1));
        datosSolicitudJPanel.add(necesitaObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 90, 20));
        datosSolicitudJPanel.add(necesitaObraJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, -1, -1));

        horaJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(horaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 90, 20));
        datosSolicitudJPanel.add(aceraJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, -1, 20));
        datosSolicitudJPanel.add(aparcamientoJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 130, 110, 20));
        datosSolicitudJPanel.add(calzadaJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, -1, 20));
        datosSolicitudJPanel.add(mesasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 90, 20));
        datosSolicitudJPanel.add(metros2JLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 90, 20));
        datosSolicitudJPanel.add(afectaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 90, 20));

        numSillasJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(numSillasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 90, 20));

        datosSolicitudJPanel.add(areaOcupacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 90, 20));

        buscarLicenciaObraJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarLicenciaObraJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarLicenciaObraJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarLicenciaObraJButtonActionPerformed();
            }
        });

        datosSolicitudJPanel.add(buscarLicenciaObraJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 40, 20, 20));

        areaOcupacionJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoCalculadora);
        areaOcupacionJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        areaOcupacionJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        areaOcupacionJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcularArea();
           }
        });
        datosSolicitudJPanel.add(areaOcupacionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 20, 20));

        ocupacionJPanel.add(datosSolicitudJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 225));

        emplazamientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder("Emplazamiento"));
        emplazamientoJPanel.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, 20));
        emplazamientoJPanel.add(numeroViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 130, 20));
        emplazamientoJPanel.add(cPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 130, 20));
        emplazamientoJPanel.add(provinciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 130, 20));

        municipioJTField.setEditable(true);
        municipioJTField.setEnabled(false);
        emplazamientoJPanel.add(municipioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 240, -1));

        provinciaJTField.setEditable(true);
        provinciaJTField.setEnabled(false);
        emplazamientoJPanel.add(provinciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 330, -1));

        nombreViaJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        nombreViaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        nombreViaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreViaJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(nombreViaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 20, 20));

        referenciasCatastralesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        referenciasCatastralesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                referenciasCatastralesJTableMouseClicked();
            }
        });

        referenciasCatastralesJScrollPane.setViewportView(referenciasCatastralesJTable);

        emplazamientoJPanel.add(referenciasCatastralesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 470, 95));

        tableToMapOneJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableToMapOneJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        tableToMapOneJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableToMapOneJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(tableToMapOneJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 105, 20, 20));

        tableToMapAllJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableToMapAllJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        tableToMapAllJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableToMapAllJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(tableToMapAllJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 125, 20, 20));

        ocupacionJPanel.add(emplazamientoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 225, 520, 211));

        anexosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        annadirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annadirJButtonActionPerformed();
            }
        });

        anexosJPanel.add(annadirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, 20, 20));

        eliminarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarJButtonActionPerformed();
            }
        });

        anexosJPanel.add(eliminarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 20, 20));

        listaAnexosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                listaAnexosJTableFocusGained();
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                listaAnexosJTableFocusLost();
            }
        });
        listaAnexosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaAnexosJTableMouseClicked();
            }
        });

        listaAnexosJScrollPane.setViewportView(listaAnexosJTable);

        anexosJPanel.add(listaAnexosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 470, 80));

        abrirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirJButtonActionPerformed();
            }
        });

        anexosJPanel.add(abrirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 20, 20));

        ocupacionJPanel.add(anexosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 436, 520, 110));

        propietarioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPropietarioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesPropietarioJPanel.add(DNIPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));

       DNIPropietarioJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIPropietarioJTFieldKeyReleased();
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIPropietarioJTFieldKeyTyped();
            }
        });

        datosPersonalesPropietarioJPanel.add(DNIPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 280, -1));
        datosPersonalesPropietarioJPanel.add(nombrePropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, 20));

        datosPersonalesPropietarioJPanel.add(nombrePropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 300, -1));
        datosPersonalesPropietarioJPanel.add(apellido1PropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 81, 130, 20));
        datosPersonalesPropietarioJPanel.add(apellido2PropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 102, 130, 20));

        datosPersonalesPropietarioJPanel.add(apellido1PropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 81, 300, -1));

        datosPersonalesPropietarioJPanel.add(apellido2PropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 102, 300, -1));
        buscarDNIPropietarioJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIPropietarioJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIPropietarioJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIPropietarioJButtonActionPerformed();
            }
        });

        datosPersonalesPropietarioJPanel.add(buscarDNIPropietarioJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 20, 20));

        propietarioJPanel.add(datosPersonalesPropietarioJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 140));

        datosNotificacionPropietarioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        datosNotificacionPropietarioJPanel.add(viaNotificacionPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));

        datosNotificacionPropietarioJPanel.add(faxPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 130, 20));

        datosNotificacionPropietarioJPanel.add(telefonoPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 130, 20));

        datosNotificacionPropietarioJPanel.add(movilPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 130, 20));

        datosNotificacionPropietarioJPanel.add(emailPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 130, 20));

        datosNotificacionPropietarioJPanel.add(tipoViaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, 20));

        datosNotificacionPropietarioJPanel.add(nombreViaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 161, 130, 20));

        datosNotificacionPropietarioJPanel.add(numeroViaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 182, 130, 20));

        datosNotificacionPropietarioJPanel.add(portalPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 245, 130, 20));

        datosNotificacionPropietarioJPanel.add(plantaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 203, 130, 20));

        datosNotificacionPropietarioJPanel.add(escaleraPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 266, 130, 20));

        datosNotificacionPropietarioJPanel.add(letraPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 224, 130, 20));

        datosNotificacionPropietarioJPanel.add(cPostalPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 130, 20));

        datosNotificacionPropietarioJPanel.add(municipioPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 311, 130, 20));

        datosNotificacionPropietarioJPanel.add(provinciaPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 332, 130, 20));

        datosNotificacionPropietarioJPanel.add(faxPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 51, 300, -1));

        datosNotificacionPropietarioJPanel.add(telefonoPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 72, 300, -1));

        datosNotificacionPropietarioJPanel.add(movilPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 93, 300, -1));

        datosNotificacionPropietarioJPanel.add(emailPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 114, 300, -1));

       datosNotificacionPropietarioJPanel.add(nombreViaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 161, 300, -1));

        datosNotificacionPropietarioJPanel.add(numeroViaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 182, 150, -1));

        datosNotificacionPropietarioJPanel.add(plantaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 203, 150, -1));

        datosNotificacionPropietarioJPanel.add(portalPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 245, 150, -1));

        datosNotificacionPropietarioJPanel.add(escaleraPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 266, 150, -1));

        datosNotificacionPropietarioJPanel.add(letraPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 224, 150, -1));

        datosNotificacionPropietarioJPanel.add(cPostalPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 300, -1));

        datosNotificacionPropietarioJPanel.add(municipioPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 311, 300, -1));

        datosNotificacionPropietarioJPanel.add(provinciaPropietarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 332, 300, -1));

        notificarPropietarioJCheckBox.setSelected(true);
        notificarPropietarioJCheckBox.setEnabled(false);
        datosNotificacionPropietarioJPanel.add(notificarPropietarioJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 370, 70, -1));

        datosNotificacionPropietarioJPanel.add(notificarPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 150, 20));

        propietarioJPanel.add(datosNotificacionPropietarioJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 520, 406));

        representanteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentanteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesRepresentanteJPanel.add(DNIRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));

        DNIRepresentanteJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIRepresentanteJTFieldKeyReleased();
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIRepresentanteJTFieldKeyTyped();
            }
        });

        datosPersonalesRepresentanteJPanel.add(DNIRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 280, -1));

        datosPersonalesRepresentanteJPanel.add(nombreRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, 20));

        datosPersonalesRepresentanteJPanel.add(nombreRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 300, -1));

        datosPersonalesRepresentanteJPanel.add(apellido1RepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 81, 130, 20));

        datosPersonalesRepresentanteJPanel.add(apellido2RepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 102, 130, 20));

        datosPersonalesRepresentanteJPanel.add(apellido1RepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 81, 300, -1));

        datosPersonalesRepresentanteJPanel.add(apellido2RepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 102, 300, -1));

        buscarDNIRepresentanteJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIRepresentanteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIRepresentanteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIRepresentanteJButtonActionPerformed();
            }
        });

        datosPersonalesRepresentanteJPanel.add(buscarDNIRepresentanteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 20, 20));

        borrarRepresentanteJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarRepresentanteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarRepresentanteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarRepresentanteJButtonActionPerformed();
            }
        });

        datosPersonalesRepresentanteJPanel.add(borrarRepresentanteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 20, 20));

        representanteJPanel.add(datosPersonalesRepresentanteJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 140));

        datosNotificacionRepresentanteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        datosNotificacionRepresentanteJPanel.add(viaNotificacionRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));

        datosNotificacionRepresentanteJPanel.add(faxRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 130, 20));

        datosNotificacionRepresentanteJPanel.add(telefonoRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 130, 20));

        datosNotificacionRepresentanteJPanel.add(movilRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 130, 20));

        datosNotificacionRepresentanteJPanel.add(emailRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 130, 20));

        datosNotificacionRepresentanteJPanel.add(tipoViaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, 20));

        datosNotificacionRepresentanteJPanel.add(nombreViaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 161, 130, 20));

        datosNotificacionRepresentanteJPanel.add(numeroViaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 182, 130, 20));

        datosNotificacionRepresentanteJPanel.add(portalRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 245, 130, 20));

        datosNotificacionRepresentanteJPanel.add(plantaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 203, 130, 20));

        datosNotificacionRepresentanteJPanel.add(escaleraRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 266, 130, 20));

        datosNotificacionRepresentanteJPanel.add(letraRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 224, 130, 20));

        datosNotificacionRepresentanteJPanel.add(cPostalRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 130, 20));

        datosNotificacionRepresentanteJPanel.add(municipioRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 311, 130, 20));

        datosNotificacionRepresentanteJPanel.add(provinciaRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 332, 130, 20));

        datosNotificacionRepresentanteJPanel.add(faxRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 51, 300, -1));

        datosNotificacionRepresentanteJPanel.add(telefonoRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 72, 300, -1));

        datosNotificacionRepresentanteJPanel.add(movilRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 93, 300, -1));

        datosNotificacionRepresentanteJPanel.add(emailRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 114, 300, -1));

        datosNotificacionRepresentanteJPanel.add(nombreViaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 161, 300, -1));

        datosNotificacionRepresentanteJPanel.add(numeroViaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 182, 150, -1));

        datosNotificacionRepresentanteJPanel.add(plantaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 203, 150, -1));

        datosNotificacionRepresentanteJPanel.add(portalRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 245, 150, -1));

        datosNotificacionRepresentanteJPanel.add(escaleraRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 266, 150, -1));

        datosNotificacionRepresentanteJPanel.add(letraRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 224, 150, -1));

        datosNotificacionRepresentanteJPanel.add(cPostalRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 300, 20));


        datosNotificacionRepresentanteJPanel.add(municipioRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 311, 300, 20));

        datosNotificacionRepresentanteJPanel.add(provinciaRepresentanteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 332, 300, 20));

        datosNotificacionRepresentanteJPanel.add(notificarRepresentanteJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 370, 60, -1));

        datosNotificacionRepresentanteJPanel.add(notificarRepresentanteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 150, 20));

        representanteJPanel.add(datosNotificacionRepresentanteJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 520, 406));

        notificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder("Notificaciones"));
        notificacionesJScrollPane.setEnabled(false);
        notificacionesJTable.setFocusCycleRoot(true);
        notificacionesJTable.setSurrendersFocusOnKeystroke(true);
        notificacionesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                notificacionesJTableMouseClicked();
            }
        });
        notificacionesJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notificacionesJTableFocusGained();
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

        //datosNotificacionesJPanel.add(notificacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 500, 380));
        datosNotificacionesJPanel.add(notificacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 500, 340));

        datosNotificacionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder("Datos de Notificaci\u00f3n de la Notificaci\u00f3n Seleccionada"));
        datosNotificacionJPanel.add(datosNombreApellidosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 130, 20));

        datosNotificacionJPanel.add(datosDireccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, 20));

        datosNotificacionJPanel.add(datosCPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 20));

        datosNotificacionJPanel.add(datosNotificarPorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 130, 20));

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

        okJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoOK);
        okJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        okJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed();
            }
        });

        datosNotificacionJPanel.add(okJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 160, 20, 20));


        //CHAROdatosNotificacionesJPanel.add(datosNotificacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 500, 150));
        datosNotificacionesJPanel.add(datosNotificacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 500, 190));

        notificacionesJPanel.add(datosNotificacionesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 525, 578));

        eventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosEventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosEventosJPanel.setBorder(new javax.swing.border.TitledBorder("Eventos"));
        eventosJScrollPane.setBorder(null);
        eventosJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        eventosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        eventosJTable.setFocusCycleRoot(true);
        eventosJTable.setSurrendersFocusOnKeystroke(true);
        eventosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eventosJTableMouseClicked();
            }
        });
        eventosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eventosJTableFocusGained();
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

        eventosJPanel.add(datosEventosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 525, 578));

        historicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosHistoricoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosHistoricoJPanel.setBorder(new javax.swing.border.TitledBorder("Hist\u00f3rico"));
        historicoJScrollPane.setBorder(null);
        historicoJTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        historicoJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        historicoJTable.setFocusCycleRoot(true);
        historicoJTable.setSurrendersFocusOnKeystroke(true);
        historicoJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                historicoJTableMouseClicked();
            }
        });
        historicoJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                historicoJTableFocusGained();
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
        modHistoricoJButton.setText("");
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
        borrarHistoricoJButton.setText("");
        borrarHistoricoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarHistoricoJButtonActionPerformed();
            }
        });
        datosHistoricoJPanel.add(borrarHistoricoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, 20, 20));

        generarFichaHistoricoJButton.setFont(new java.awt.Font("MS Sans Serif", 1, 10));
        generarFichaHistoricoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarFichaHistoricoJButtonActionPerformed();
            }
        });
        datosHistoricoJPanel.add(generarFichaHistoricoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 210, 20, 20));

        historicoJPanel.add(datosHistoricoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 525, 578));

        templateJPanel.add(ocupacionJTabbedPane, BorderLayout.WEST);  //new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 590)


        botoneraJPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });
        
        publicarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publicarJButtonActionPerformed();
            }
        });

        jButtonGenerarFicha.setPreferredSize(new Dimension(120,30));
        aceptarJButton.setPreferredSize(new Dimension(120,30));
        
        publicarJButton.setPreferredSize(new Dimension(120,30));
        
        cancelarJButton.setPreferredSize(new Dimension (120,30));
        botoneraJPanel.add(aceptarJButton);//, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 130, -1));
        
        botoneraJPanel.add(publicarJButton);
        
        botoneraJPanel.add(jButtonGenerarFicha);
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        botoneraJPanel.add(cancelarJButton);//, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 130, -1));

        templateJPanel.add(botoneraJPanel, BorderLayout.SOUTH);//new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 560, 40));

        boolean activo=LocalgisIntegrationManagerImpl.verifyStatusAlfresco(UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL, "",false),
				String.valueOf(AppContext.getIdMunicipio()),AlfrescoConstants.APP_GENERAL);
        AlfrescoManagerUtils.setAlfrescoActive(activo);
        
        if(AlfrescoManagerUtils.isAlfrescoClientActive()){
        	alfrescoJButton.setEnabled(false); 
	        alfrescoJButton.setText(CMainOcupaciones.literales.getString("alfresco.button.documentManager"));
	        alfrescoJButton.setToolTipText(CMainOcupaciones.literales.getString("alfresco.button.documentManager"));
	        alfrescoJButton.setPreferredSize(new java.awt.Dimension(150, 25));
	        alfrescoJButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                docManager();
	            }
	        });
	        
	        ocupacionJPanel.add(alfrescoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 546, 150, 25));
        }
        
        editorMapaJPanel.setLayout(new java.awt.GridLayout(1, 0));

        templateJPanel.add(editorMapaJPanel, BorderLayout.CENTER);//new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 12, 480, 580));

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane);

    }//GEN-END:initComponents




    private void generarFichaHistoricoJButtonActionPerformed() {//GEN-FIRST:event_generarFichaHistoricoJButtonActionPerformed

        if ((_vHistorico == null) || (expediente == null) || (solicitud == null))  return;

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
            CUtilidadesComponentes.generarFichaHistorico(this.desktop, _vHistorico, expediente, solicitud,
                    selectedFile,CMainOcupaciones.literales);
        }
    }//GEN-LAST:event_generarFichaHistoricoJButtonActionPerformed


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


    private void buscarLicenciaObraJButtonActionPerformed() {//GEN-FIRST:event_buscarLicenciaObraJButtonActionPerformed
        CSearchLicenciasObraJDialog dialog= CUtilidadesComponentes.showSearchLicenciasObraDialog(desktop);
        if (dialog != null){
            if ((dialog.getNumExpedienteSeleccionado() != null) && (dialog.getNumExpedienteSeleccionado().trim().length() > 0)){
                numExpedienteObraJTField.setText(dialog.getNumExpedienteSeleccionado());
            }
        }

    }//GEN-LAST:event_buscarLicenciaObraJButtonActionPerformed

	private void borrarRepresentanteJButtonActionPerformed() {//GEN-FIRST:event_borrarRepresentanteJButtonActionPerformed
        if (CUtilidadesComponentes.hayDatosPersonaJuridicoFisica(DNIRepresentanteJTField.getText().trim(),
            nombreRepresentanteJTField.getText().trim(),
            nombreViaRepresentanteJTField.getText().trim(),
            numeroViaRepresentanteJTField.getText().trim(),
            cPostalRepresentanteJTField.getText().trim(),
            municipioRepresentanteJTField.getText().trim(),
            provinciaRepresentanteJTField.getText().trim())){

            /** mostramos ventana de confirmacion */
            int ok = JOptionPane.showConfirmDialog(ocupacionJPanel, CMainOcupaciones.literales.getString("CModificacionLicenciasForm.representanteJPanel.borrarRepresentanteJButton.Message"), CMainOcupaciones.literales.getString("CModificacionLicenciasForm.representanteJPanel.borrarRepresentanteJButton.tittle"), JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                borrarCamposRepresentante();
                if (solicitud.getRepresentante() != null) {
                    solicitud.setIdRepresentanteToDelete(solicitud.getRepresentante().getIdPersona());
                }
            }
        }else{
            borrarCamposRepresentante();
        }

	}//GEN-LAST:event_borrarRepresentanteJButtonActionPerformed


	private void referenciasCatastralesJTableMouseClicked() {//GEN-FIRST:event_referenciasCatastralesJTableMouseClicked
        try {

            int selectedRow = referenciasCatastralesJTable.getSelectedRow();
            logger.info("selectedRow: " + selectedRow);

            if (selectedRow != -1) {

                logger.info("referenciasCatastralesJTable.getValueAt(selectedRow, 0): " + referenciasCatastralesJTable.getValueAt(selectedRow, 0));

                /** tipoVia */
                String tipoVia= "";
                Object objeto=referenciasCatastralesJTableModel.getValueAt(selectedRow, 2);
                String patron=null;
                if ((objeto instanceof DomainNode) && objeto!=null){
                    tipoVia= (((DomainNode)objeto).getTerm(CMainOcupaciones.literales.getLocale().toString()));
                    patron=((DomainNode)objeto).getPatron();
                }
                if ((objeto instanceof String) && objeto!=null){
                    if (((String)objeto).length()>0){
                        tipoVia= Estructuras.getListaTiposViaINE().getDomainNode((String)objeto).getTerm(CMainOcupaciones.literales.getLocale().toString());
                        patron=((String)objeto);
                    }
                }

                CReferenciaCatastral referencia= new CReferenciaCatastral();
                referencia.setTipoVia(tipoVia);
                referencia.setNombreVia((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 3));
                referencia.setPrimerNumero((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 4));
                referencia.setPrimeraLetra((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 5));
                referencia.setBloque((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 6));
                referencia.setEscalera((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 7));
                referencia.setPlanta((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 8));
                referencia.setPuerta((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 9));
                CDatosReferenciaCatastralJDialog dialog= CUtilidadesComponentes.showDatosReferenciaCatastralDialog(desktop, referencia, true);

                if (dialog != null){
                    if (dialog.esDirEmplazamiento()){
                        tipoViaINEEJCBox.setSelectedPatron(patron);
                        nombreViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 3));
                        numeroViaNumberTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 4));
                        portalViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 6));
                        plantaViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 8));
                        letraViaTField.setText((String)referenciasCatastralesJTableModel.getValueAt(selectedRow, 5));
                        // no existe el dato en la referencia catastral
                        cpostalViaTField.setText("");
                    }
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
	}//GEN-LAST:event_referenciasCatastralesJTableMouseClicked


	private void tableToMapAllJButtonActionPerformed() {//GEN-FIRST:event_tableToMapAllJButtonActionPerformed

        Vector vBusqueda=new Vector();
		for (int i = 0; i < referenciasCatastralesJTableModel.getRowCount(); i++) {
			String value = (String) referenciasCatastralesJTableModel.getValueAt(i, 0);
			vBusqueda.add(value);
		}
		refreshFeatureSelection(layerName,0,vBusqueda);
	}//GEN-LAST:event_tableToMapAllJButtonActionPerformed

        public void borrarFeaturesSinGrabar()
    {
        try
        {
        GeopistaLayer layer= (GeopistaLayer)CMainOcupaciones.geopistaEditor.getLayerManager().getLayer(layerName);
             for (Enumeration e=nuevasFeatures.elements(); e.hasMoreElements();)
             {
                 Feature f = (Feature)e.nextElement();
                 if (f.getAttribute(1)==null)
                 {
                     ShowMaps.deleteFeature(f,layer,layerName,com.geopista.protocol.CConstantesComando.adminCartografiaUrl);
                     for (Iterator it=layer.getFeatureCollectionWrapper().getFeatures().iterator(); it.hasNext();)
                     {
                         GeopistaFeature fAux=(GeopistaFeature)it.next();
                         if (((GeopistaFeature)f).getSystemId()!=null &&((GeopistaFeature)f).getSystemId().equals(fAux.getSystemId()))
                             layer.getFeatureCollectionWrapper().remove(fAux);

                     }
                 }

             }
        }catch(Exception e){}
    }


    private void formComponentShown() {//GEN-FIRST:event_formComponentShown
         if (!fromMenu){
            consultar();
        }
    }


	private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
		borrarFeaturesSinGrabar();
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
        desbloquearExpediente();
		CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);

	}//GEN-LAST:event_formInternalFrameClosed

	private void notificacionesJTableMouseClicked() {//GEN-FIRST:event_notificacionesJTableMouseClicked
	    mostrarDatosNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableMouseClicked

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


    public void comboxMouseClicked() {
        notificacionesJTableMouseClicked();
    }

    public void comboxFocusGained() {
        notificacionesJTableFocusGained();
    }

    public void comboxMouseDragged() {
        notificacionesJTableMouseDragged();
    }


    private void mostrarDatosNotificacionSeleccionada(){
        int row = notificacionesJTable.getSelectedRow();
        if (row != -1) {
            row= ((Integer)notificacionesSorted.getValueAt(row, notificacionesHiddenCol)).intValue();
            CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(row);
            CPersonaJuridicoFisica persona = notificacion.getPersona();
            CDatosNotificacion datos = persona.getDatosNotificacion();
            String interesado= CUtilidades.componerCampo(persona.getApellido1(), persona.getApellido2(), persona.getNombre());
            datosNombreApellidosJTField.setText(interesado);
            datosDireccionJTField.setText(CUtilidades.componerCampo(((DomainNode) Estructuras.getListaTiposViaINE().getDomainNode(datos.getTipoVia())).getTerm(CMainOcupaciones.currentLocale.toString()),
                    datos.getNombreVia(),
                    datos.getNumeroVia()));
            datosCPostalJTField.setText(CUtilidades.componerCampo(datos.getCpostal(), datos.getMunicipio(), datos.getProvincia()));
            datosNotificarPorJTField.setText(((DomainNode) Estructuras.getListaViasNotificacion().getDomainNode(new Integer(datos.getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()));

            if (notificacion.getEntregadaA() != null){
                entregadaATField.setText(notificacion.getEntregadaA());
                entregadaATextJLabel.setText(notificacion.getEntregadaA());
            }else{
                entregadaATField.setText("");
                entregadaATextJLabel.setText("");
            }
            entregadaATField.setVisible(true);
            okJButton.setVisible(true);

        }

    }


	private void eventosJTableMouseClicked() {//GEN-FIRST:event_eventosJTableMouseClicked
	    mostrarEventoSeleccionado();
	}//GEN-LAST:event_eventosJTableMouseClicked

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


    private void mostrarEventoSeleccionado(){
        int row = eventosJTable.getSelectedRow();
        if (row != -1) {
            CEvento evento = (CEvento) _vEventos.get(((Integer)eventosSorted.getValueAt(row, eventosHiddenCol)).intValue());
            descEventoJTArea.setText(evento.getContent());
        }
    }

    private void borrarHistoricoJButtonActionPerformed() {//GEN-FIRST:event_borrarHistoricoJButtonActionPerformed
        int row = historicoJTable.getSelectedRow();
        if (row != -1) {

            int posDeleted= ((Integer)historicoSorted.getValueAt(row, historicoHiddenCol)).intValue();
            CHistorico historico= (CHistorico)vAuxiliar.get(posDeleted);

            if (historico != null){
                if ((historico.getIdHistorico() != -1) && (historico.getSistema().equals("1"))){
                    /** no se puede borrar un historico de sistema */
                    mostrarMensaje(CMainOcupaciones.literales.getString("borrarHistoricoJButton.mensaje1"));
                    return;
                }

                int ok= JOptionPane.showConfirmDialog(this, CMainOcupaciones.literales.getString("Licencias.confirmarBorrado"), CMainOcupaciones.literales.getString("Licencias.tittle"), JOptionPane.YES_NO_OPTION);
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
                        historico.setHasBeen(CConstantesOcupaciones.CMD_HISTORICO_DELETED);
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



    private void mostrarHistoricoSeleccionado(){
        int row = historicoJTable.getSelectedRow();
        if (row != -1) {
            CHistorico historico= (CHistorico)vAuxiliar.get(((Integer)historicoSorted.getValueAt(row, historicoHiddenCol)).intValue());
            if (historico != null){
                apunteJTArea.setText(historico.getApunte());
            }
        }
    }


    private void nuevoHistoricoJButtonActionPerformed() {//GEN-FIRST:event_nuevoHistoricoJButtonActionPerformed
        CHistorico nuevo= new CHistorico();
        nuevo.setIdHistorico(-1);
        nuevo.setSolicitud(solicitud);
        nuevo.setExpediente(expediente);
        nuevo.setEstado(expediente.getEstado());
        nuevo.setUsuario(CConstantesOcupaciones.principal.getName());
        nuevo.setFechaHistorico(CUtilidades.getDate(CUtilidades.showToday()));
        nuevo.setApunte("");
        nuevo.setSistema("0");
        nuevo.setHasBeen(CConstantesOcupaciones.CMD_HISTORICO_ADDED);

        CConstantesOcupaciones.OPERACION_HISTORICO= CConstantesOcupaciones.CMD_HISTORICO_ADDED;
        CHistoricoJDialog historicoJDialog= CUtilidadesComponentes.showHistoricoDialog(desktop, nuevo);

        CHistorico historico= historicoJDialog.getHistorico();
        /** comprobamos que operacion se ha llevado a cabo con el historico */
        if (historico.getHasBeen() != -1) {
            _historicoExpedienteTableModel.addRow(new Object[]{((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
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
                        mostrarMensaje(CMainOcupaciones.literales.getString("modificarHistoricoJButton.mensaje1"));
                        return;
                    }
                }

            }

            CConstantesOcupaciones.OPERACION_HISTORICO= CConstantesOcupaciones.CMD_HISTORICO_MODIFIED;
            CHistoricoJDialog historicoJDialog= CUtilidadesComponentes.showHistoricoDialog(desktop, historico);
            CHistorico h= historicoJDialog.getHistorico();
            _historicoExpedienteTableModel.setValueAt(h.getApunte(), row, 3);
            apunteJTArea.setText(h.getApunte());

            /** Annadimos el historico modificado */
            vAuxiliar.removeElementAt(row);
            historico.setApunte(h.getApunte());
            vAuxiliar.add(row, historico);
        }
    }//GEN-LAST:event_modHistoricoJButtonActionPerformed


	private void abrirJButtonActionPerformed() {//GEN-FIRST:event_abrirJButtonActionPerformed

		int row = listaAnexosJTable.getSelectedRow();
		logger.info("row: " + row);


		if (row == -1) {
			mostrarMensaje(CMainOcupaciones.literales.getString("abrirAnexoJButton.mensaje2"));
			return;
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		String fileName = (String) listaAnexosJTable.getValueAt(row, 0);
		logger.info("fileName: " + fileName);

        String tmpFile= "";
		File f = new File(fileName);
		if (!f.isAbsolute()) {
            /** Dialogo para seleccionar donde dejar el fichero */
            JFileChooser chooser = new JFileChooser();

            /** Permite multiples selecciones */
            chooser.setMultiSelectionEnabled(false);
            chooser.setSelectedFile(f);

            int returnVal = chooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile= chooser.getSelectedFile();
                if (selectedFile != null){
                    String tmpDir= "";
                    tmpFile= selectedFile.getAbsolutePath();
                    int index= tmpFile.lastIndexOf(selectedFile.getName());
                    if (index != -1){
                        tmpDir= tmpFile.substring(0, index);
                    }

                   /** Comprobamos si existe el directorio. */
                    try {
                        File dir = new File(tmpDir);
                        logger.info("dir: " + dir);

                        if (!dir.exists()) {
                            logger.warn("Directorio temporal creado.");
                            dir.mkdirs();
                        }
                    } catch (Exception ex) {
                        logger.error("Exception: " + ex.toString());
                    }

                    boolean resultado = false;
                    CAnexo anexo = (CAnexo)_hAnexosSolicitud.get(fileName);
                    if(!AlfrescoManagerUtils.isAlfrescoUuid(anexo.getIdAnexo(), String.valueOf(AppContext.getIdMunicipio()))){
                    	resultado = CUtilidadesComponentes.GetURLFile(CConstantesComando.anexosLicenciasUrl + solicitud.getIdSolicitud() + "/" + fileName, tmpFile, "", 0);
                    }
                    else {
                    	AlfrescoDocumentClient alfrescoDocumentClient = new AlfrescoDocumentClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.ALFRESCO_WEBAPP_NAME +
                                ServletConstants.ALFRESCO_DOCUMENT_SERVLET_NAME);
                    	try {
                    		String id = CUtilidadesComponentes.getAnexoId(selectedFile.getName(), solicitud.getIdSolicitud());
                    		if(id != null){
                    			resultado = alfrescoDocumentClient.downloadFile(new AlfrescoKey(id, AlfrescoKey.KEY_UUID), tmpDir, selectedFile.getName());
                    		}
                    	} catch (Exception e) {
    						logger.error(e);
    					}
                    }
                    if (!resultado) {
                        mostrarMensaje(CMainOcupaciones.literales.getString("abrirAnexoJButton.mensaje3"));
                        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        return;
                    }
                }else{
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    return;                    
                }
            }else{
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }
        }else{
            tmpFile= f.getAbsolutePath();
        }

        /** Visualizamos el fichero descargado si SO es Windows. */
        if (CUtilidadesComponentes.isWindows()){
            try {
                Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + tmpFile + "\"");

            } catch (Exception ex) {
                logger.warn("Exception: " + ex.toString());
                mostrarMensaje(CMainOcupaciones.literales.getString("abrirAnexoJButton.mensaje1")+ " " + tmpFile);
            }
        }

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_abrirJButtonActionPerformed

	private void tableToMapOneJButtonActionPerformed() {//GEN-FIRST:event_tableToMapOneJButtonActionPerformed


        if (referenciasCatastralesJTable.getSelectedRow() == -1) {
            logger.info("El usuario ha de seleccionar primero una fila");
            return;
        }
        String value = (String) referenciasCatastralesJTableModel.getValueAt(referenciasCatastralesJTable.getSelectedRow(), 0);
        refreshFeatureSelection(layerName,0,value);
	}//GEN-LAST:event_tableToMapOneJButtonActionPerformed

	private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed
        CSearchJDialog dialog= CUtilidadesComponentes.showSearchDialog(desktop, false);
        if ((dialog != null) && (dialog.getSelectedExpediente() != null)){
            if (!dialog.getOperacionCancelada()){
                if ((dialog.getSelectedExpediente() != null) && (dialog.getSelectedExpediente().trim().length() > 0)){
                    numExpedienteJTField.setText(dialog.getSelectedExpediente());
                }
            }else searchExpCanceled= true;
        }
        consultar();
	}//GEN-LAST:event_buscarExpedienteJButtonActionPerformed

	public void consultar() {//GEN-FIRST:event_consultarJButtonActionPerformed
		if ((numExpedienteJTField.getText() == null) || (numExpedienteJTField.getText().trim().equals(""))) {
			return;
		}

        /** cancela la busqueda desde el dialogo de busquedade expediente. */
        if (searchExpCanceled){
            searchExpCanceled= false;
            return;
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		/** Datos de la solicitud y del expediente seleccionado */
		CResultadoOperacion ro = COperacionesLicencias.getExpedienteLicencia(numExpedienteJTField.getText(), CMainOcupaciones.literales.getLocale().toString(), null);
		if (ro == null || ro.getSolicitudes()==null || ro.getExpedientes()==null)
        {
            	logger.warn("No existen resultados de operacion para el expediente " + numExpedienteJTField.getText());
                _consultaOK = false;
				mostrarMensaje(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.mensaje1"));
				clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
        }
    	CSolicitudLicencia auxSolicitud = (CSolicitudLicencia) ro.getSolicitudes().get(0);
		CExpedienteLicencia auxExpediente = (CExpedienteLicencia) ro.getExpedientes().get(0);
        if (auxSolicitud == null || auxExpediente== null)
        {
                logger.warn("No existe solicitud para el expediente " + numExpedienteJTField.getText());
                _consultaOK = false;
                mostrarMensaje(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.mensaje1"));
                clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
        }
        _consultaOK = true;
	    /** Comprobamos si el expediente esta bloqueado */
        if (auxExpediente.bloqueado()){
            /** Comprobamos si ya esta bloqueado por el usuario */
            if (expediente == null ||
                    ((expediente.getNumExpediente() != null) &&
                     (!expediente.getNumExpediente().equalsIgnoreCase(numExpedienteJTField.getText()))))
            {
                /** Expediente bloqueado por otro usuario */
                if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, fromMenu) != 0)
                {
                      if (expediente != null)
                          numExpedienteJTField.setText(expediente.getNumExpediente());
                      else
                      {
                          _consultaOK = false;
                          numExpedienteJTField.setText("");
                      }
                      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                      return;
                } else{
                      if ((expediente != null) && (expediente.getNumExpediente() != null) && (expediente.getNumExpediente().trim().length() > 0)){
                          /** Desbloqueo del expediente cargado anteriormente, si ha sido bloqueado por el usuario */
                          if (expediente.bloqueaUsuario())
                             COperacionesLicencias.bloquearExpediente(expediente.getNumExpediente(), false);
                      }
                }
            }else{
                /** Puede ocurrir:
                * 1.- Expediente bloqueado por propio usuario
                * 2.- El usuario ha vuelto a abrir el mismo expediente anterior bloqueado por otro usuario */
                if (expediente.bloqueaUsuario()){
                    auxExpediente.setBloqueaUsuario(true);
                }else{
                     /** caso 2 */
                     CUtilidadesComponentes.mostrarMensajeBloqueoAceptacion(this, CMainOcupaciones.literales.getString("Bloqueo.mensaje5"));
                }
             }
        }else{
              if ((expediente != null) && (expediente.getNumExpediente() != null) && (expediente.getNumExpediente().trim().length() > 0)){
                 /** Desbloqueo del expediente cargado anteriormente, si ha sido bloqueado por el propio usuario */
                 if (expediente.bloqueaUsuario()){
                     COperacionesLicencias.bloquearExpediente(expediente.getNumExpediente(), false);
                 }
              }
              /** Bloqueo de expediente */
              COperacionesLicencias.bloquearExpediente(auxExpediente.getNumExpediente(), true);
              auxExpediente.setBloqueaUsuario(true);
          }
          expediente=auxExpediente;
          solicitud=auxSolicitud;
          alfrescoJButton.setEnabled(true);
          /** Si seguimos buscando numExpediente, navegamos desde la propia pantalla */
          fromMenu= true;

          //*******************
    	  /** Creamos nuevos objetos para las Hashtable globales que manejan los anexos */
		   _hAnexosSolicitud = new Hashtable();
		   _hAnexosAnnadidosExpediente = new Hashtable();
			clearScreen();
			/** Expediente */
			rellenarExpediente();
			/** Solicitud */
			rellenarSolicitud();
			/** Titular */
			rellenarPropietario();
			/** Representado */
			rellenarRepresentante();
			/** Notificaciones */
			rellenarNotificaciones();
			clearDatosNotificacionSelected();
			/** Eventos */
			rellenarEventos();
			/** Historico */
			rellenarHistorico();
             //Calles afectadas
            jPanelCallesAfectadas.setCallesAfectadas(expediente.getCallesAfec(),expediente, solicitud,
                             CMainOcupaciones.geopistaEditor);

			com.geopista.app.ocupaciones.CUtilidadesComponentes.checkEstadosCombo(expediente, estadoExpedienteEJCBox);

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_consultarJButtonActionPerformed

	private void listaAnexosJTableMouseClicked() {//GEN-FIRST:event_listaAnexosJTableMouseClicked
		int selected = listaAnexosJTable.getSelectedRow();

		if (selected != -1) {
			eliminarJButton.setEnabled(true);
		} else {
			eliminarJButton.setEnabled(false);
		}
	}//GEN-LAST:event_listaAnexosJTableMouseClicked

	private void listaAnexosJTableFocusLost() {//GEN-FIRST:event_listaAnexosJTableFocusLost
		int selected = listaAnexosJTable.getSelectedRow();
		if (selected != -1) {
			eliminarJButton.setEnabled(true);
		} else {
			eliminarJButton.setEnabled(false);
		}
	}//GEN-LAST:event_listaAnexosJTableFocusLost

	private void eliminarJButtonActionPerformed() {//GEN-FIRST:event_eliminarJButtonActionPerformed
		if (eliminarJButton.isEnabled()) {
			int selected = listaAnexosJTable.getSelectedRow();
			if (selected != -1) {
                int ok= JOptionPane.showConfirmDialog(this, CMainOcupaciones.literales.getString("Licencias.confirmarBorrado"), CMainOcupaciones.literales.getString("Licencias.tittle"), JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.NO_OPTION) return;

				/** Marcamos el fichero como borrado */
				/*
				String nomFichero= (String)_listaAnexosTableModel.getValueAt(selected, 0);
				int i= 0;
				boolean encontrado= false;
				while ((!encontrado) && (i<_vAnexosSolicitud.size())){
					String nom= (String)_vAnexosSolicitud.get(i);
					if (nom.equalsIgnoreCase(nomFichero)){
						CAnexo anexo= (CAnexo)_vAnexosSolicitud.get(i);
						anexo.setEstado(CConstantesOcupaciones.CMD_ANEXO_DELETED);
						_vAnexosSolicitud.add(i, anexo);
						encontrado= true;
					}
					i++;
				}
				*/

                /** Actualizamos el tamanno total de los ficheros a enviar */
                String nombreAnexo = (String) _listaAnexosTableModel.getValueAt(selected, 0);
                File file = new File(nombreAnexo);
                if (file.isAbsolute()) {
                    long size= file.length();
                    if (getMaxSizeFilesUploaded() > size){
                       setMaxSizeFilesUploaded(getMaxSizeFilesUploaded() - size);
                    }else{
                        setMaxSizeFilesUploaded(0);
                    }
                }

				_listaAnexosTableModel.removeRow(selected);
				/** Comprobamos si algun item de la lista queda seleccionado.
				 *  Si es asi, habilitamos el boton Eliminar, si no, le deshabilitamos
				 */
				if (listaAnexosJTable.getModel().getRowCount() > 0) {
					if (listaAnexosJTable.getSelectedRow() != -1) {
						eliminarJButton.setEnabled(true);
					} else {
						eliminarJButton.setEnabled(false);
					}
				} else {
					eliminarJButton.setEnabled(false);
				}
			} else { // no ha seleccionado ningun item
				Toolkit.getDefaultToolkit().beep();
				eliminarJButton.setEnabled(false);
			}
		}
	}//GEN-LAST:event_eliminarJButtonActionPerformed

    public void setMaxSizeFilesUploaded(long size){
        this.maxSizeFilesUploaded= size;
    }

    public long getMaxSizeFilesUploaded(){
        return maxSizeFilesUploaded;
    }


	private void annadirJButtonActionPerformed() {//GEN-FIRST:event_annadirJButtonActionPerformed
		/** Permite annadir varios elementos a la lista */
        try{
            listaAnexosJTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            JFileChooser chooser = new JFileChooser();
            GeoPistaFileFilter filter = new GeoPistaFileFilter();
            filter.addExtension("doc");
            filter.addExtension("txt");
            filter.setDescription("Ficheros DOC & TXT");
            chooser.setFileFilter(filter);
            /** Permite multiples selecciones */
            chooser.setMultiSelectionEnabled(true);

            int returnVal = chooser.showOpenDialog(ocupacionJPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = chooser.getSelectedFiles();
                //System.out.println("Fichero(s) seleccionado " + selectedFiles.length);
                if (selectedFiles.length > 0) {
                    for (int i = 0; i < selectedFiles.length; i++) {
                        for (int j = 0; j < _listaAnexosTableModel.getRowCount(); j++) {
                            String nombreAnexo = (String) _listaAnexosTableModel.getValueAt(j, 0);
                            File file = new File(nombreAnexo);
                            if (file.isAbsolute()) {
                                nombreAnexo = file.getName();
                            }

                            if (nombreAnexo.equalsIgnoreCase(selectedFiles[i].getName())) {
                                /** Ya existe un anexo con ese nombre */
                                mostrarMensaje(CMainOcupaciones.literales.getString("AnexosJPanel.Message3"));
                                return;
                            }
                            /** comprobamos que no exceda el tamanno maximo permitido */
                            if (selectedFiles[i].isAbsolute()){
                                long sizeFilesUploaded= getMaxSizeFilesUploaded();
                                sizeFilesUploaded=+selectedFiles[i].length();
                                if (sizeFilesUploaded > CConstantesOcupaciones.totalMaxSizeFilesUploaded){
                                    JOptionPane optionPane= new JOptionPane(CMainOcupaciones.literales.getString("AnexosJPanel.Message2")+": " +CConstantesOcupaciones.totalMaxSizeFilesUploaded+" bytes", JOptionPane.ERROR_MESSAGE);
                                    JDialog dialog =optionPane.createDialog(desktop,"ERROR");
                                    dialog.show();
                                    return;
                                }else{
                                    setMaxSizeFilesUploaded(sizeFilesUploaded);
                                }
                            }

                        }                        
                        /** No se repite la entrada */
                        /** Por defecto selected el primer tipo */
                        JComboBox combox = (JComboBox) ((ComboBoxTableEditor) listaAnexosJTable.getCellEditor(_listaAnexosTableModel.getRowCount(), 1)).getComponent();
                        Object[] rowData = {selectedFiles[i].getAbsolutePath(), combox.getItemAt(0), ""};
                        _listaAnexosTableModel.addRow(rowData);

                        /** Marcamos el fichero como annadido y lo insertamos en un vector auxiliar de annadidos*/
                        if ((_hAnexosAnnadidosExpediente.get(selectedFiles[i].getName()) == null)) {
                            CAnexo anexo = new CAnexo(new CTipoAnexo(), selectedFiles[i].getName(), "");
                            anexo.setEstado(CConstantesOcupaciones.CMD_ANEXO_ADDED);
                            _hAnexosAnnadidosExpediente.put(selectedFiles[i].getName(), anexo);
                        }
                    }
                }
            }

            /** Solo se podra seleccionar un elemento de la lista */
            listaAnexosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }catch(Exception e){
        }
	}//GEN-LAST:event_annadirJButtonActionPerformed
    private void generarFicha()
    {
    	try
        {
            if (expediente == null)
            {
                mostrarMensaje(CMainOcupaciones.literales.getString("generarFicha.mensaje1"));
                return;
            }
            expediente.setEstructuraEstado(Estructuras.getListaEstadosOcupacion());
            expediente.setEstructuraTipoOcupacion(Estructuras.getListaTipoOcupacion());
            expediente.setLocale(CMainOcupaciones.currentLocale);
            expediente.setSolicitud(solicitud);
           	new GeopistaPrintableOcupaciones().printObjeto(FichasDisponibles.fichalicenciaocupacion, expediente , CExpedienteLicencia.class, CMainOcupaciones.geopistaEditor.getLayerViewPanel(), GeopistaPrintableOcupaciones.FICHA_OCUPACIONES_MODIFICACION);
		} catch (Exception ex) {
			logger.error("Exception al mostrar las features: " ,ex);
		}
    }
	private void cancelarJButtonActionPerformed() {//GEN-FIRST:event_cancelarJButtonActionPerformed
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
		borrarFeaturesSinGrabar();
		this.dispose();
	}//GEN-LAST:event_cancelarJButtonActionPerformed

	private void aceptarJButtonActionPerformed() {//GEN-FIRST:event_aceptarJButtonActionPerformed
		if (_consultaOK) {
            /** Volvemos a preguntar al usuario si quiere modificar un expediente bloqueado por otro usuario */
            if (!expediente.bloqueaUsuario()){
                if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, fromMenu) != 0){
                    return;
                }
            }

			if (rellenaCamposObligatorios()) {
				/** Comprobamos los datos de entrada */
				/** Datos del propietario */
				if (datosPropietarioOK() && datosRepresentanteOK()) {
					/** Datos de la solicitud */
					_tipoOcupacion = Integer.parseInt(tipoOcupacionEJCBox.getSelectedPatron());

                    Date _dHoraInicio= null;
                    Date _dHoraFin= null;

					String _horaInicio = horaInicioJTimeTField.getText().trim();
					try {
                        if (_horaInicio.length() > 0){
						    _dHoraInicio = new SimpleDateFormat("HH:mm").parse(_horaInicio);
                        }
					} catch (Exception e) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje1"));
						return;
					}

					String _horaFin = horaFinJTimeTField.getText().trim();
					try {
                        if (_horaFin.length() > 0){
						    _dHoraFin = new SimpleDateFormat("HH:mm").parse(_horaFin);
                        }
					} catch (Exception e) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje2"));
						return;
					}

					String motivo = motivoJTField.getText().trim();
					if (CUtilidades.excedeLongitud(motivo, CConstantesOcupaciones.MaxLengthMotivo)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje3"));
						return;
					}
					String asunto = asuntoJTField.getText().trim();
					if (CUtilidades.excedeLongitud(asunto, CConstantesOcupaciones.MaxLengthAsunto)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje4"));
						return;
					}

					String observaciones = observacionesJTArea.getText().trim();
					if (CUtilidades.excedeLongitud(observaciones, CConstantesOcupaciones.MaxLengthObservaciones)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje5"));
						return;
					}

					/** Expediente */
                    CEstado estado= new CEstado();
                    estado.setIdEstado(new Integer(estadoExpedienteEJCBox.getSelectedPatron()).intValue());
					String silencio = "0";
					if (silencioJCheckBox.isSelected()) silencio = "1";
					String habiles = "0";
					if (habilesJCheckBox.isSelected()) habiles = "1";

					String servicioEncargado = servicioExpedienteJTField.getText().trim();
					if (CUtilidades.excedeLongitud(servicioEncargado, CConstantesOcupaciones.MaxLengthServicioEncargado)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje6"));
						return;
					}

					String asuntoExpediente = asuntoExpedienteJTField.getText().trim();
					if (CUtilidades.excedeLongitud(asuntoExpediente, CConstantesOcupaciones.MaxLengthAsunto)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje7"));
						return;
					}

					/** No es editable */
					String fechaApertura = fechaAperturaJTField.getText();
					Date dateApertura = CUtilidades.parseFechaStringToDate(fechaApertura);

					String formaInicio = inicioJTField.getText().trim();
					if (CUtilidades.excedeLongitud(formaInicio, CConstantesOcupaciones.MaxLengthFormaInicio)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje8"));
						return;
					}

					String numFolios = numFoliosJTField.getText().trim();
					if (numFolios.length() > 0) {
						if (!CUtilidades.esNumerico(numFolios)) {
							mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje9"));
							return;
						} else {
							if (CUtilidades.excedeLongitud(numFolios, CConstantesOcupaciones.MaxLengthNumFolios)) {
								mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje10"));
								return;
							}
						}
					}
					String responsableExpediente = responsableJTField.getText().trim();
					if (CUtilidades.excedeLongitud(responsableExpediente, CConstantesOcupaciones.MaxLengthResponsableExpediente)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje11"));
						return;
					}

					/** No es editable */
					String plazoVencimiento = plazoVencimientoJTField.getText();
					Date dateVencimiento = CUtilidades.parseFechaStringToDate(plazoVencimiento);

					String numDias = numDiasJTField.getText().trim();
					if (numDias.length() > 0) {
						if (!CUtilidades.esNumerico(numDias)) {
							mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje12"));
							return;
						} else {
							if (CUtilidades.excedeLongitud(numDias, CConstantesOcupaciones.MaxLengthNumDias)) {
								mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje13"));
								return;
							}
						}
					}

					String observacionesExpediente = observacionesExpedienteJTArea.getText().trim();
					if (CUtilidades.excedeLongitud(observacionesExpediente, CConstantesOcupaciones.MaxLengthObservaciones)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje14"));
						return;
					}

					// Recogemos la lista de referencias catastrales de la solicitud
					Vector vRefCatastrales = new Vector();
					int numRowsRef = referenciasCatastralesJTable.getRowCount();
					for (int i = 0; i < numRowsRef; i++) {
						String referencia = (String) referenciasCatastralesJTable.getValueAt(i, 0);

						vRefCatastrales.addElement(referencia);
					}

					// Recogemos la lista de documentos anexados a la solicitud
					Vector vListaAnexos = new Vector();
					Hashtable hAnexosAux = new Hashtable();
					int numRows = listaAnexosJTable.getRowCount();

					for (int i = 0; i < numRows; i++) {
						String nomFichero = (String) listaAnexosJTable.getValueAt(i, 0);
						/*
						ComboBoxRenderer renderBox = (ComboBoxRenderer) listaAnexosJTable.getCellRenderer(i, 1);
						listaAnexosJTable.prepareRenderer(renderBox, i, 1);
						int index = renderBox.getSelectedIndex();
						*/
						ComboBoxRendererEstructuras renderBox = (ComboBoxRendererEstructuras) listaAnexosJTable.getCellRenderer(i, 1);
						listaAnexosJTable.prepareRenderer(renderBox, i, 1);
						int index = new Integer(renderBox.getSelectedPatron()).intValue();

						TextFieldRenderer renderText = (TextFieldRenderer) listaAnexosJTable.getCellRenderer(i, 2);
						listaAnexosJTable.prepareRenderer(renderText, i, 2);
						String descripcion = renderText.getText();

						CTipoAnexo tipoAnexo = new CTipoAnexo(index, "", "");
						CAnexo anexo = new CAnexo(tipoAnexo, nomFichero, descripcion);
						try {
							/** Lectura del contenido del anexo */
							File file = new File(nomFichero);
							if (file.isAbsolute()) {
								nomFichero = file.getName();
								anexo.setFileName(nomFichero);
                                /* MultipartPostMethod - en lugar de enviar el contenido, enviamos el path absoluto del fichero - */
                                anexo.setPath(file.getAbsolutePath());
                                /**/

							}

							/** Comprobamos si se ha marcado como annadido o como borrado */
							if ((_hAnexosSolicitud.get(anexo.getFileName()) != null) && (_hAnexosAnnadidosExpediente.get(anexo.getFileName()) != null)) {
								/** Ha sido borrado y posteriormente annadido (actualizado) */
								//System.out.println(anexo.getFileName() + " ** Ha sido borrado y posteriormente annadido (actualizado) *");
								anexo.setEstado(CConstantesOcupaciones.CMD_ANEXO_DELETED);
								/** marcado como borrado */
								vListaAnexos.add(anexo);
								/** Necesario, ya que te guarda una referencia al tratarse del mismo objeto*/
								CAnexo nuevo = new CAnexo(anexo.getTipoAnexo(), anexo.getFileName(), anexo.getObservacion());
								nuevo.setEstado(CConstantesOcupaciones.CMD_ANEXO_ADDED);
                                /** -- MultipartPostMethod: comentamos para no enviar el contenido. Enviamos el file directamente. */
                                /*
								byte[] content = getBytesFromFile(file);
								nuevo.setContent(content);
                                */
								/** marcado como annadido */
								vListaAnexos.add(nuevo);
								hAnexosAux.put(nuevo.getFileName(), nuevo);

							} else if ((_hAnexosSolicitud.get(anexo.getFileName()) == null) && (_hAnexosAnnadidosExpediente.get(anexo.getFileName()) != null)) {
								/** El anexo ha sido annadido */
								//System.out.println(anexo.getFileName() + " ** El anexo ha sido annadido *");
								anexo.setEstado(CConstantesOcupaciones.CMD_ANEXO_ADDED);
                                /** -- MultipartPostMethod: comentamos para no enviar el contenido. Enviamos el file directamente. */
                                /*
								byte[] content = getBytesFromFile(file);
								anexo.setContent(content);
                                */
								vListaAnexos.addElement(anexo);
								hAnexosAux.put(anexo.getFileName(), anexo);

							} else if ((_hAnexosSolicitud.get(anexo.getFileName()) != null) && (_hAnexosAnnadidosExpediente.get(anexo.getFileName()) == null)) {
								//System.out.println(anexo.getFileName() + " ** El anexo no ha sufrido cambios *");
								/** El anexo no ha sufrido cambios. Puede que solo haya cambiado el tipo y la observacion. */
                                CAnexo existente= (CAnexo)_hAnexosSolicitud.get(anexo.getFileName());
                                anexo.setIdAnexo(existente.getIdAnexo());
								vListaAnexos.addElement(anexo);
								hAnexosAux.put(anexo.getFileName(), anexo);
							} else if ((_hAnexosSolicitud.get(anexo.getFileName()) == null) && (_hAnexosAnnadidosExpediente.get(anexo.getFileName()) == null)) {
								/** Este caso no se puede dar */
							}
						} catch (Exception ex) {
							logger.error("Exception: " + ex.toString());
						}
					}

					/** Comprobamos aquellos que han sido marcados como borrados */
					Enumeration enumerationElement = _hAnexosSolicitud.elements();
					while (enumerationElement.hasMoreElements()) {
						CAnexo a = (CAnexo) enumerationElement.nextElement();
						if (hAnexosAux.get(a.getFileName()) == null) {
							a.setEstado(CConstantesOcupaciones.CMD_ANEXO_DELETED);
							vListaAnexos.addElement(a);
						}
					}

					/** Propietario */
					// @param CViaNotificacion int idViaNotifiacion, String observacion
					int viaNotificacionIndex = new Integer(viaNotificacionPropietarioEJCBox.getSelectedPatron()).intValue();
					CViaNotificacion viaNotificacion = new CViaNotificacion(viaNotificacionIndex, "");
                    propietario.getDatosNotificacion().setViaNotificacion(viaNotificacion);

					propietario.getDatosNotificacion().setTipoVia(tipoViaNotificacionPropietarioEJCBox.getSelectedPatron());

					// @param CDatosNotificacion String dniCif, CViaNotificacion viaNotificacion, String fax, String telefono, String movil, String email, String tipoVia, String nombreVia, String numeroVia, String portal, String planta, String escalera, String letra, String cpostal, String municipio, String provincia, String notificar
					/** si no se notifica ni al representante, ni al tecnico, ni al promotor, por defecto
					 * se notifica al propietario  */
                    if (representante.getDatosNotificacion().getNotificar().equals("0"))
                        propietario.getDatosNotificacion().setNotificar("1");

                    String fax= "";
                    try{fax= faxPropietarioJTField.getNumber().toString();}catch(Exception e){}
                    propietario.getDatosNotificacion().setFax(fax);
                    String telefono= "";
                    try{telefono= telefonoPropietarioJTField.getNumber().toString();}catch (Exception e){}
                    propietario.getDatosNotificacion().setTelefono(telefono);
                    String movil= "";
                    try{movil=  movilPropietarioJTField.getNumber().toString();}catch(Exception e){}
                    propietario.getDatosNotificacion().setMovil(movil);

                	// @param CPersonaJuridicoFisica String dniCif, String nombre, String apellido1, String apellido2, String colegio, String visado, String titulacion, CDatosNotificacion datosNotificacion
		            if (solicitud.getPropietario() != null){
					    propietario.setIdPersona(solicitud.getPropietario().getIdPersona());
                    }

					/** Representante */
					viaNotificacionIndex = new Integer(viaNotificacionRepresentanteEJCBox.getSelectedPatron()).intValue();
					viaNotificacion = new CViaNotificacion(viaNotificacionIndex, "");
                    representante.getDatosNotificacion().setViaNotificacion(viaNotificacion);

					representante.getDatosNotificacion().setTipoVia(tipoViaNotificacionRepresentanteEJCBox.getSelectedPatron());
                    fax= "";
                    try{fax= faxRepresentanteJTField.getNumber().toString();}catch(Exception e){}
                    representante.getDatosNotificacion().setFax(fax);
                    telefono= "";
                    try{telefono= telefonoRepresentanteJTField.getNumber().toString();}catch (Exception e){}
                    representante.getDatosNotificacion().setTelefono(telefono);
                    movil= "";
                    try{movil=  movilRepresentanteJTField.getNumber().toString();}catch(Exception e){}
                    representante.getDatosNotificacion().setMovil(movil);


        			if ((solicitud.getRepresentante() != null) && (solicitud.getIdRepresentanteToDelete() == -1)) {
						/** el expediente ya tiene representante. Puede ocurrir:
						 * 1.- se ha modificado alguno de sus datos
						 * 2.- no se ha borrado, pero se ha introducido a mano el DNI (por lo que suponemos que no existe en BD)
						 */
						if (solicitud.getRepresentante().getDniCif().equalsIgnoreCase(representante.getDniCif())) {
							/** caso 1 */
							System.out.println("************** 0.1- Se ha modificado algun dato de ID=" + solicitud.getRepresentante().getIdPersona());
							representante.setIdPersona(solicitud.getRepresentante().getIdPersona());
						} else {
							/** caso 2 */
							System.out.println("************** 0.2- no se ha borrado, pero se ha introducido a mano el DNI");
							representante.setIdPersona(-1);
						}
					} else if ((solicitud.getRepresentante() != null) && (solicitud.getIdRepresentanteToDelete() != -1)) {
						/** La solicitud tiene representante y se ha borrado. Puede ocurrir:
						 * 1.- se asigne un representante de BD
						 * 2.- se asigne uno nuevo que no existe en BD
						 * 3.- no se asigna representante
						 */
						if (representante.getDniCif().length() > 0) {
							if (representanteBuscado != null) {
								/** caso 1 */
								System.out.println("************** 1.- Se ha asignado un representante de BD ID=" + representanteBuscado.getIdPersona());
								representante.setIdPersona(representanteBuscado.getIdPersona());
							} else {
								/** caso 2 */
								System.out.println("************** 2.- Se ha asignado un representante que NO EXISTE EN BD");
								representante.setIdPersona(-1);
							}
						} else {
							/** caso 3 */
							System.out.println("************** 3.- NO Se ha asignado un representante");
							representante = null;
						}
					} else if (solicitud.getRepresentante() == null) {
						/** La solicitud no tiene representante. Puede ocurrir:
						 * 1.- se asigne un representante de BD
						 * 2.- se asigne uno nuevo que no existe en BD
						 * 3.- se quede como esta sin representante
						 */
						if ((representanteBuscado != null) || (representante.getDniCif().trim().length() > 0)) {
							if (representanteBuscado != null) {
								/** caso 1 */
								System.out.println("************** 4.- Se ha asignado un representante de BD ID=" + representanteBuscado.getIdPersona());
								representante.setIdPersona(representanteBuscado.getIdPersona());
							} else {
								/** caso 2 */
								System.out.println("************** 5.- Se ha asignado un representante que NO EXISTE EN BD");
								representante.setIdPersona(-1);
							}
						} else {
							/** caso 3 */
							System.out.println("************** 6.- NO Se ha asignado un representante");
							representante = null;
						}
					}

                    /** REFERENCIAS CATASTRALES */
					Vector referenciasCatastrales = new Vector();
					for (int i = 0; i < referenciasCatastralesJTable.getRowCount(); i++) {
                        /** tipoVia */
                        String tipoVia= null;
                        Object objeto=referenciasCatastralesJTableModel.getValueAt(i, 2);
                        if ((objeto instanceof DomainNode) && objeto!=null){
                            tipoVia=((DomainNode)objeto).getPatron();
                        }
                        if ((objeto instanceof String) && objeto!=null){
                            if (((String)objeto).length()>0){
                                tipoVia=((String)objeto);
                            }
                        }

                        String ref_Catastral = (String)referenciasCatastralesJTable.getValueAt(i, 0);

                        String nombre= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 3)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 3)).trim().equalsIgnoreCase(""))){
                           nombre= ((String)referenciasCatastralesJTable.getValueAt(i, 3)).trim();
                        }
                        String numero= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 4)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 4)).trim().equalsIgnoreCase(""))){
                           numero= ((String)referenciasCatastralesJTable.getValueAt(i, 4)).trim();
                        }
                        String letra= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 5)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 5)).trim().equalsIgnoreCase(""))){
                           letra= ((String)referenciasCatastralesJTable.getValueAt(i, 5)).trim();
                        }
                        String bloque= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 6)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 6)).trim().equalsIgnoreCase(""))){
                           bloque= ((String)referenciasCatastralesJTable.getValueAt(i, 6)).trim();
                        }
                        String escalera= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 7)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 7)).trim().equalsIgnoreCase(""))){
                           escalera= ((String)referenciasCatastralesJTable.getValueAt(i, 7)).trim();
                        }
                        String planta= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 8)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 8)).trim().equalsIgnoreCase(""))){
                           planta= ((String)referenciasCatastralesJTable.getValueAt(i, 8)).trim();
                        }
                        String puerta= null;
                        if ((((String)referenciasCatastralesJTable.getValueAt(i, 9)) != null) && (!((String)referenciasCatastralesJTable.getValueAt(i, 9)).trim().equalsIgnoreCase(""))){
                           puerta= ((String)referenciasCatastralesJTable.getValueAt(i, 9)).trim();
                        }

						CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral(ref_Catastral, tipoVia, nombre, numero, letra, bloque, escalera, planta, puerta);

						referenciasCatastrales.add(referenciaCatastral);

					}

                    /** Emplazamiento */
                    String tipoViaINE= null;
                    if (tipoViaINEEJCBox.getSelectedIndex() != 0){
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

					// @param CSolicitudLicencia CTipoLicencia tipoLicencia, CTipoObra tipoObra, CPersonaJuridicoFisica propietario, CPersonaJuridicoFisica representante, CPersonaJuridicoFisica tecnico, CPersonaJuridicoFisica promotor, String numAdministrativo, String codigoEntrada, String unidadTramitadora, String unidadDeRegistro, String motivo, String asunto, Date fecha, double tasa, String tipoViaAfecta, String nombreViaAfecta, String numeroViaAfecta, String portalAfecta, String plantaAfecta, String letraAfecta, String cpostalAfecta, String municipioAfecta, String provinciaAfecta, String observaciones, Vector anexos, Vector referenciasCatastrales
					CSolicitudLicencia solicitudLicencia = new CSolicitudLicencia(null,
							null,
							propietario,
							representante,
							null,
							null,
							"",
							"",
							"",
							"",
							motivo,
							asunto,
							solicitud.getFecha(),
							0,
                            tipoViaINE,
                            nombreViaEmplazamiento,
                            numeroViaEmplazamiento,
                            portalViaEmplazamiento,
                            plantaViaEmplazamiento,
                            letraViaEmplazamiento,
                            cPostalEmplazamiento,
                            municipioJTField.getText(),
                            provinciaJTField.getText(),
							observaciones,
							vListaAnexos,
							referenciasCatastrales);
					
					solicitudLicencia.setIdMunicipio(Integer.toString(AppContext.getIdMunicipio()));
					
					CDatosOcupacion datosOcupacion = new CDatosOcupacion();
					datosOcupacion.setTipoOcupacion(_tipoOcupacion);
					datosOcupacion.setNecesitaObra("0");
					if (necesitaObraJCheckBox.isSelected()) {
						datosOcupacion.setNecesitaObra("1");
					}
					datosOcupacion.setNumExpediente(numExpedienteObraJTField.getText());

					try {
						datosOcupacion.setHoraInicio(_dHoraInicio);
						datosOcupacion.setHoraFin(_dHoraFin);
						datosOcupacion.setNumMesas(numMesasJNTField.getNumber().intValue());
						datosOcupacion.setNumSillas(numSillasJNTField.getNumber().intValue());
						datosOcupacion.setAreaOcupacion(areaOcupacionJNTField.getNumber().doubleValue());
						datosOcupacion.setM2acera(m2AceraJNTField.getNumber().doubleValue());
						datosOcupacion.setM2calzada(m2CalzadaJNTField.getNumber().doubleValue());
						datosOcupacion.setM2aparcamiento(m2AparcamientoJNTField.getNumber().doubleValue());

					} catch (Exception e) {
					}

					datosOcupacion.setAfectaAcera("0");
					if (aceraJCheckBox.isSelected()) {
						datosOcupacion.setAfectaAcera("1");
					}

					datosOcupacion.setAfectaCalzada("0");
					if (calzadaJCheckBox.isSelected()) {
						datosOcupacion.setAfectaCalzada("1");
					}

					datosOcupacion.setAfectaAparcamiento("0");
					if (aparcamientoJCheckBox.isSelected()) {
						datosOcupacion.setAfectaAparcamiento("1");
					}

					solicitudLicencia.setDatosOcupacion(datosOcupacion);
					solicitudLicencia.setIdSolicitud(solicitud.getIdSolicitud());

					/** expediente */

					/** Tipo Tramitacion */
					CTipoTramitacion tramitacion = new CTipoTramitacion();
					tramitacion.setIdTramitacion(new Integer(tramitacionEJCBox.getSelectedPatron()).intValue());

					/** Tipo Finalizacion */

                    //ASO COMENTA EL MODO DE FINALIZACIÓN SE INTRODUCIRÁ AUTOMÁTICAMENTE CUANDO SE REALICE EL CIERRE DEL EXPEDIENTE
                    //if (finalizacionEJCBox.getSelectedIndex()!=0)
                    //{
                    //    CTipoFinalizacion finalizacion = new CTipoFinalizacion();
                    //	finalizacion.setIdFinalizacion(new Integer(finalizacionEJCBox.getSelectedIndex()).intValue());
                    //}

					/** Notificaciones */
                    Vector auxNotificaciones = new Vector();
                    if (_vNotificaciones != null) {
                        Enumeration e = _vNotificaciones.elements();
                        int row = 0;
                        while (e.hasMoreElements()) {
                            CNotificacion notificacion = (CNotificacion) e.nextElement();
                            // Solamente actualizamos los valores que se pueden modificar
                            ComboBoxRendererEstructuras renderBox = (ComboBoxRendererEstructuras) notificacionesJTable.getCellRenderer(row, 0);
                            notificacionesJTable.prepareRenderer(renderBox, row, 0);
                            int index = new Integer(renderBox.getSelectedPatron()).intValue();
                            CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion(index, "", "");
                            notificacion.setEstadoNotificacion(estadoNotificacion);
                            // si esta NOTIFICADA o PENDIENTE_ACUSE_REENVIO actualizamos la fecha
                            if (notificacion.getEstadoNotificacion().getIdEstado() == CConstantesOcupaciones.ID_ESTADO_NOTIFICADA) {
                                notificacion.setFechaNotificacion(new Timestamp(new Date().getTime()));
                            } else if (notificacion.getEstadoNotificacion().getIdEstado() == CConstantesOcupaciones.ID_ESTADO_PENDIENTE_REENVIO) {
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
								String revisadoPor = CConstantesOcupaciones.principal.getName();
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
					/*CExpedienteLicencia expedienteNuevo = new CExpedienteLicencia(
                            _numExpediente, expediente.getIdSolicitud(),
                            tramitacion, null, _servicioEncargado, _asuntoExpediente, silencio,
                            _formaInicio, new Integer(_numFolios).intValue(), dateApertura, _responsableExpediente,
                            dateVencimiento, habiles, new Integer(numDias).intValue(), observacionesExpediente, estado, auxNotificaciones);*/
                    expediente.setTipoTramitacion(tramitacion);
                    expediente.setServicioEncargado(servicioEncargado);
                    expediente.setAsunto(asuntoExpediente);
                    expediente.setSilencioAdministrativo(silencio);
                    expediente.setFormaInicio(formaInicio);
                    expediente.setNumFolios(new Integer(numFolios).intValue());
                    expediente.setFechaApertura(dateApertura);
                    expediente.setResponsable(responsableExpediente);
                    expediente.setPlazoVencimiento(dateVencimiento);
                    expediente.setHabiles(habiles);
                    expediente.setNumDias(new Integer(numDias).intValue());
                    expediente.setObservaciones(observacionesExpediente);
                    expediente.setEstado(estado);
                    expediente.setNotificaciones(auxNotificaciones);


					expediente.setVU(_vu);
					expediente.setEventos(auxEventos);
					expediente.setHistorico(_vHistorico);

                    //Calles afectadas
                    expediente.setCallesAfec(jPanelCallesAfectadas.getCallesAfectadas());

					CResultadoOperacion resultado = COperacionesLicencias.modificarExpediente(solicitudLicencia, expediente);

					/** si ha ido bien, los anexos han sido actualizados.
					 * Como no cerramos la pantalla, es necesario recargar los nuevos anexos del expediente */
					if (resultado.getResultado()) {
						_hAnexosSolicitud = new Hashtable();
						_vAnexosSolicitud = solicitud.getAnexos();
						if (_vAnexosSolicitud != null) {
							Enumeration en = _vAnexosSolicitud.elements();
							while (en.hasMoreElements()) {
								CAnexo anexo = (CAnexo) en.nextElement();
								/** Insertamos en _hAnexosSolicitud */
								/** Esta estructura nos servira para hacer las búsquedas de los anexos por nombre,
								 *  en la construccion del vector con los anexos marcados como borrados y annadidos
								 */
								_hAnexosSolicitud.put(anexo.getFileName(), anexo);
							}
						}

						_hAnexosAnnadidosExpediente = new Hashtable();
                        /** Expediente modificado OK */
						mostrarMensaje(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.Message1"));

						consultar();
                        GeopistaLayer geopistaLayer = (GeopistaLayer) CMainOcupaciones.geopistaEditor.getLayerManager().getLayer(layerName);
                        for (Enumeration en=nuevasFeatures.elements(); en.hasMoreElements();)
                        {
                            Feature f = (Feature)en.nextElement();
                            f.setAttribute(1,new Long(solicitudLicencia.getIdSolicitud()).toString());
                            ShowMaps.updateFeature(f,geopistaLayer,layerName,com.geopista.protocol.CConstantesComando.adminCartografiaUrl);
                       }
                       nuevasFeatures=new Hashtable();
                	} else{
                        /** Comprobamos que no se haya excedido el maximo FileUploadBase.SizeLimitExceededException */
                       if (resultado.getDescripcion().equalsIgnoreCase("FileUploadBase.SizeLimitExceededException")){
                           JOptionPane optionPane= new JOptionPane(CMainOcupaciones.literales.getString("AnexosJPanel.Message1"), JOptionPane.ERROR_MESSAGE);
                           JDialog dialog =optionPane.createDialog(desktop,CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje15"));
                           dialog.show();
                       }else{
                         /** ERROR al modificar el expediente */
                         JOptionPane optionPane= new JOptionPane(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.Message2")+" \n"+resultado.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                         JDialog dialog =optionPane.createDialog(desktop,CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.TabSolicitud.mensaje15"));
                         dialog.show();
                       }
                    }
				}
			} else {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.mensaje1"));
			}
			
			//Actualizar Estado de Expediente
			CResultadoOperacion resultado = COperacionesLicencias.actualizarEstadoExpediente(expediente);
			if (resultado.getResultado()){
				if (resultado.getDescripcion().equalsIgnoreCase("ExpedienteNoPublicado")){
					mostrarMensaje(CMainOcupaciones.literales.getString("CModificaionesLicencias.ExpedienteNoPublicado"));
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
			/////////////////////////////////////////////////////////////////

		} else {
			mostrarMensaje(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.mensaje2"));
		}


	}//GEN-LAST:event_aceptarJButtonActionPerformed


	private void publicarJButtonActionPerformed() {
		
		if(_consultaOK){
			
			if (expediente == null)
	        {
	            //mostrarMensaje("Es necesario consultar primero por un expediente.");
				mostrarMensaje(CMainOcupaciones.literales.getString("CActualizarIdSigemLicenciasForm.ConsultarExpediente"));
	            return;
	        }
			
			CResultadoOperacion primerResultado = COperacionesLicencias.obtenerIdSigem(expediente);
			if (primerResultado.getResultado()) {
				//No está publicado
				if (solicitud.getTipoLicencia() != null){
					
					expediente.setTipoLicenciaDescripcion(CUtilidadesComponentes.obtenerTipoLicencia(solicitud));

					if (expediente.getEstado() != null){
						expediente.getEstado().setDescripcion(CUtilidadesComponentes.obtenerDescripcionEstado(expediente, solicitud.getTipoLicencia().getIdTipolicencia()));
					}
				}
				
				CResultadoOperacion segundoResultado = COperacionesLicencias.publicarExpedienteSigem(expediente, solicitud);
				if (segundoResultado.getResultado()) {	
					
					CResultadoOperacion tercerResultado = COperacionesLicencias.actualizarIdSigem(expediente);
					if (tercerResultado.getResultado()) {
		                /* El expediente ha sido modificado */
						mostrarMensaje(CMainOcupaciones.literales.getString("CActualizarIdSigemLicenciasForm.Message1"));
					}
					else{
						mostrarMensaje(CMainOcupaciones.literales.getString("CActualizarIdSigemLicenciasForm.Message2"));
					}				
				}
				else{
					mostrarMensaje(CMainOcupaciones.literales.getString("CActualizarIdSigemLicenciasForm.Message2"));
				}			
			}
			else{
				//Está publicado
				mostrarMensaje(CMainOcupaciones.literales.getString("CActualizarIdSigemLicenciasForm.ExpedientePublicado"));
	            return;
			}			
		}
		else{
			mostrarMensaje(CMainOcupaciones.literales.getString("expedienteJPanel.Message1"));
		}
	}
	
    private boolean refreshFeatureSelection(String layerName, int attributeNumber, String sBuscado) {
        Vector vBusqueda= new Vector();
        vBusqueda.add(sBuscado);
        return refreshFeatureSelection(layerName, attributeNumber, vBusqueda);
    }
    private boolean refreshFeatureSelection(String layerName, int attributeNumber, Vector vBusqueda)
    {
        try
        {
           GeopistaLayer geopistaLayer = (GeopistaLayer) CMainOcupaciones.geopistaEditor.getLayerManager().getLayer(layerName);
           java.util.List featureList = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
           Feature feature = (Feature) featureList.get(attributeNumber);
           String attributeName = feature.getSchema().getAttributeName(attributeNumber);
           return refreshFeatureSelection(layerName, attributeName, vBusqueda);
        }catch(Exception e)
        {
           logger.error("Error al refrescar la feature:"+attributeNumber +" del layer "+layerName);
           return false;
        }
    }



	private boolean refreshFeatureSelection(String layerName, String attributeName, Vector vBusqueda) {

		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			CMainOcupaciones.geopistaEditor.getSelectionManager().clear();

			GeopistaLayer geopistaLayer = (GeopistaLayer) CMainOcupaciones.geopistaEditor.getLayerManager().getLayer(layerName);

			Enumeration enumerationElement = vBusqueda.elements();

			while (enumerationElement.hasMoreElements()) {
				String sbuscado = (String) enumerationElement.nextElement();
	    		Collection collection = CUtilidadesComponentes.searchByAttribute(geopistaLayer, attributeName, sbuscado);
				Iterator it = collection.iterator();
				if (it.hasNext()) {
					Feature feature = (Feature) it.next();
					CMainOcupaciones.geopistaEditor.select(geopistaLayer, feature);
				}
			}

			CMainOcupaciones.geopistaEditor.zoomToSelected();

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
        CAddressJDialog dialog= CUtilidadesComponentes.showAddressDialog(desktop);
        if (dialog != null){
            Hashtable h= dialog.getReferencias();
            try {
                Vector vBusqueda=new Vector();
                for (Enumeration e= h.elements(); e.hasMoreElements();)
                {
                      CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) e.nextElement();
                      vBusqueda.add(referenciaCatastral.getReferenciaCatastral());
                }
                refreshFeatureSelection("numeros_policia",1, vBusqueda);
            } catch (Exception ex) {
                logger.error("Exception: " + ex.toString());
            }
        }
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}//GEN-LAST:event_nombreViaJButtonActionPerformed

	private void buscarDNIRepresentanteJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIRepresentanteJButtonActionPerformed

        CPersonaJDialog dialog= CUtilidadesComponentes.showPersonaDialog(desktop);
        if (dialog != null){
            CPersonaJuridicoFisica persona = dialog.getPersona();
            if ((persona != null) && (persona.getDniCif() != null)) {
                representanteBuscado= persona;

                DNIRepresentanteJTField.setText(persona.getDniCif());
                nombreRepresentanteJTField.setText(persona.getNombre());
                apellido1RepresentanteJTField.setText(persona.getApellido1());
                apellido2RepresentanteJTField.setText(persona.getApellido2());

                /** Datos de Notificacion */
                faxRepresentanteJTField.setText(persona.getDatosNotificacion().getFax());
                telefonoRepresentanteJTField.setText(persona.getDatosNotificacion().getTelefono());
                movilRepresentanteJTField.setText(persona.getDatosNotificacion().getMovil());
                emailRepresentanteJTField.setText(persona.getDatosNotificacion().getEmail());
                nombreViaRepresentanteJTField.setText(persona.getDatosNotificacion().getNombreVia());
                numeroViaRepresentanteJTField.setText(persona.getDatosNotificacion().getNumeroVia());
                plantaRepresentanteJTField.setText(persona.getDatosNotificacion().getPlanta());
                portalRepresentanteJTField.setText(persona.getDatosNotificacion().getPortal());
                escaleraRepresentanteJTField.setText(persona.getDatosNotificacion().getEscalera());
                letraRepresentanteJTField.setText(persona.getDatosNotificacion().getLetra());
                cPostalRepresentanteJTField.setText(persona.getDatosNotificacion().getCpostal());
                municipioRepresentanteJTField.setText(persona.getDatosNotificacion().getMunicipio());
                provinciaRepresentanteJTField.setText(persona.getDatosNotificacion().getProvincia());
                if (persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                    notificarRepresentanteJCheckBox.setSelected(true);
                else notificarRepresentanteJCheckBox.setSelected(false);

                try{
                    tipoViaNotificacionRepresentanteEJCBox.setSelectedPatron(persona.getDatosNotificacion().getTipoVia());
                }catch (Exception e){
                    DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(persona.getDatosNotificacion().getTipoVia());
                    if (auxNode!=null)
                            tipoViaNotificacionRepresentanteEJCBox.setSelected(auxNode.getIdNode());
                }
                try{
                    viaNotificacionRepresentanteEJCBox.setSelectedPatron(new Integer(persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                }catch (Exception e){
                    DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                    if (auxNode!=null)
                            viaNotificacionRepresentanteEJCBox.setSelected(auxNode.getIdNode());
                }

            }
        }


	}//GEN-LAST:event_buscarDNIRepresentanteJButtonActionPerformed

	private void buscarDNIPropietarioJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIPropietarioJButtonActionPerformed

        CPersonaJDialog dialog= CUtilidadesComponentes.showPersonaDialog(desktop);
        CPersonaJuridicoFisica persona= dialog.getPersona();
        if ((persona != null) && (persona.getDniCif() != null)) {
            logger.info("persona.getDniCif(): " + persona.getDniCif());
            DNIPropietarioJTField.setText(persona.getDniCif());
            nombrePropietarioJTField.setText(persona.getNombre());
            apellido1PropietarioJTField.setText(persona.getApellido1());
            apellido2PropietarioJTField.setText(persona.getApellido2());

            /** Datos de Notificacion */
            faxPropietarioJTField.setText(persona.getDatosNotificacion().getFax());
            telefonoPropietarioJTField.setText(persona.getDatosNotificacion().getTelefono());
            movilPropietarioJTField.setText(persona.getDatosNotificacion().getMovil());
            emailPropietarioJTField.setText(persona.getDatosNotificacion().getEmail());
            nombreViaPropietarioJTField.setText(persona.getDatosNotificacion().getNombreVia());
            numeroViaPropietarioJTField.setText(persona.getDatosNotificacion().getNumeroVia());
            plantaPropietarioJTField.setText(persona.getDatosNotificacion().getPlanta());
            portalPropietarioJTField.setText(persona.getDatosNotificacion().getPortal());
            escaleraPropietarioJTField.setText(persona.getDatosNotificacion().getEscalera());
            letraPropietarioJTField.setText(persona.getDatosNotificacion().getLetra());
            cPostalPropietarioJTField.setText(persona.getDatosNotificacion().getCpostal());
            municipioPropietarioJTField.setText(persona.getDatosNotificacion().getMunicipio());
            provinciaPropietarioJTField.setText(persona.getDatosNotificacion().getProvincia());
            /*
            if (persona.getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))
                notificarPropietarioJCheckBox.setSelected(true);
            else notificarPropietarioJCheckBox.setSelected(false);
            */

            try{
                tipoViaNotificacionPropietarioEJCBox.setSelectedPatron(persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionPropietarioEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionPropietarioEJCBox.setSelectedPatron(new Integer(persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionPropietarioEJCBox.setSelected(auxNode.getIdNode());
            }
        }

	}//GEN-LAST:event_buscarDNIPropietarioJButtonActionPerformed



	private void DNIRepresentanteJTFieldKeyReleased() {//GEN-FIRST:event_DNIRepresentanteJTFieldKeyReleased

		if (DNIRepresentanteJTField.getDocument() != null) {
			if (DNIRepresentanteJTField.getText().trim().length() <= CConstantesOcupaciones.MaxLengthDNI) {
				DNI_CIF_Representante = DNIRepresentanteJTField.getText().trim();
			} else if (DNIRepresentanteJTField.getText().trim().length() > CConstantesOcupaciones.MaxLengthDNI) {
				DNIRepresentanteJTField.setText(DNI_CIF_Representante);
			}
		}
	}//GEN-LAST:event_DNIRepresentanteJTFieldKeyReleased

	private void DNIRepresentanteJTFieldKeyTyped() {//GEN-FIRST:event_DNIRepresentanteJTFieldKeyTyped

		if (DNIRepresentanteJTField.getDocument() != null) {
			if (DNIRepresentanteJTField.getText().length() <= CConstantesOcupaciones.MaxLengthDNI) {
				DNI_CIF_Representante = DNIRepresentanteJTField.getText().trim();
			} else if (DNIRepresentanteJTField.getText().trim().length() > CConstantesOcupaciones.MaxLengthDNI) {
				DNIRepresentanteJTField.setText(DNI_CIF_Representante);
			}
		}
	}//GEN-LAST:event_DNIRepresentanteJTFieldKeyTyped

	private void DNIPropietarioJTFieldKeyReleased() {//GEN-FIRST:event_DNIPropietarioJTFieldKeyReleased

		if (DNIPropietarioJTField.getDocument() != null) {
			if (DNIPropietarioJTField.getText().trim().length() <= CConstantesOcupaciones.MaxLengthDNI) {
				DNI_CIF_Propietario = DNIPropietarioJTField.getText().trim();
			} else if (DNIPropietarioJTField.getText().trim().length() > CConstantesOcupaciones.MaxLengthDNI) {
				DNIPropietarioJTField.setText(DNI_CIF_Propietario);
			}
		}
	}//GEN-LAST:event_DNIPropietarioJTFieldKeyReleased

	private void DNIPropietarioJTFieldKeyTyped() {//GEN-FIRST:event_DNIPropietarioJTFieldKeyTyped

		if (DNIPropietarioJTField.getDocument() != null) {
			if (DNIPropietarioJTField.getText().trim().length() <= CConstantesOcupaciones.MaxLengthDNI) {
				DNI_CIF_Propietario = DNIPropietarioJTField.getText().trim();
			} else if (DNIPropietarioJTField.getText().trim().length() > CConstantesOcupaciones.MaxLengthDNI) {
				DNIPropietarioJTField.setText(DNI_CIF_Propietario);
			}
		}
	}//GEN-LAST:event_DNIPropietarioJTFieldKeyTyped



	private void listaAnexosJTableFocusGained() {//GEN-FIRST:event_listaAnexosJTableFocusGained

		int selected = listaAnexosJTable.getSelectedRow();
		//System.out.println("Selected ROW= "+selected);
		if (selected != -1) {
			eliminarJButton.setEnabled(true);
		}
	}//GEN-LAST:event_listaAnexosJTableFocusGained

	private void observacionesJTAreaKeyReleased() {//GEN-FIRST:event_observacionesJTAreaKeyReleased

		if (observacionesJTArea.getDocument() != null) {
			if (observacionesJTArea.getText().trim().length() <= CConstantesOcupaciones.MaxLengthObservaciones) {
				observaciones = observacionesJTArea.getText().trim();
			} else if (observacionesJTArea.getText().trim().length() > CConstantesOcupaciones.MaxLengthObservaciones) {
				observacionesJTArea.setText(observaciones);
			}
		}
	}//GEN-LAST:event_observacionesJTAreaKeyReleased

	private void observacionesJTAreaKeyTyped() {//GEN-FIRST:event_observacionesJTAreaKeyTyped

		if (observacionesJTArea.getDocument() != null) {
			if (observacionesJTArea.getText().trim().length() <= CConstantesOcupaciones.MaxLengthObservaciones) {
				observaciones = observacionesJTArea.getText();
			} else if (observacionesJTArea.getText().trim().length() > CConstantesOcupaciones.MaxLengthObservaciones) {
				observacionesJTArea.setText(observaciones);
			}
		}
	}//GEN-LAST:event_observacionesJTAreaKeyTyped






	/*******************************************************************/
	/*                         Metodos propios                         */
	/**
	 * ***************************************************************
	 */
	public void cargarAnexosJTable() {
		try {
			// Annadimos a la tabla el editor ComboBox en la segunda columna
			int vColIndexCB = 1;
			TableColumn col2 = listaAnexosJTable.getColumnModel().getColumn(vColIndexCB);
			col2.setCellEditor(new ComboBoxTableEditor(new ComboBoxEstructuras(Estructuras.getListaTiposAnexo(), null, CMainOcupaciones.currentLocale.toString(), false)));
			col2.setCellRenderer(new ComboBoxRendererEstructuras(Estructuras.getListaTiposAnexo(), null, CMainOcupaciones.currentLocale.toString(), false));

			// Annadimos a la tabla el editor TextField en la tercera columna
			int vColIndexTF = 2;
			TableColumn col3 = listaAnexosJTable.getColumnModel().getColumn(vColIndexTF);
			col3.setCellEditor(new TextFieldTableEditor());
			col3.setCellRenderer(new TextFieldRenderer());

			_vAnexosSolicitud = solicitud.getAnexos();
			if (_vAnexosSolicitud != null) {
				logger.info("Anexos de la solicitud " + _vAnexosSolicitud.elements());
				Enumeration en = _vAnexosSolicitud.elements();
				int row = 0;
				while (en.hasMoreElements()) {
					CAnexo anexo = (CAnexo) en.nextElement();
					/** Insertamos en _hAnexosSolicitud */
					/** Esta estructura nos servira para hacer las búsquedas de los anexos por nombre,
					 *  en la construccion del vector con los anexos marcados como borrados y annadidos
					 */
					_hAnexosSolicitud.put(anexo.getFileName(), anexo);
					int tipo = ((CTipoAnexo) anexo.getTipoAnexo()).getIdTipoAnexo();
					ComboBoxEstructuras combox = (ComboBoxEstructuras) ((ComboBoxTableEditor) listaAnexosJTable.getCellEditor(row, 1)).getComponent();
					combox.setSelectedPatron(new Integer(tipo).toString());

                    String store = "LocalGIS";
                    if(AlfrescoManagerUtils.isAlfrescoUuid(anexo.getIdAnexo(), String.valueOf(AppContext.getIdMunicipio()))){
                    	store = "Alfresco";
                	}
					
					Object[] rowData = {anexo.getFileName(), combox.getSelectedItem(), anexo.getObservacion(), store};
					_listaAnexosTableModel.addRow(rowData);
					row++;
				}
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


	public boolean datosPropietarioOK() {
		try {
			if (CUtilidades.excedeLongitud(propietario.getDniCif(), CConstantesOcupaciones.MaxLengthDNI)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje1"));
				return false;
			}
			if (CUtilidades.excedeLongitud(propietario.getNombre(), CConstantesOcupaciones.MaxLengthNombre)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje2"));
				return false;
			}
			if (CUtilidades.excedeLongitud(propietario.getApellido1(), CConstantesOcupaciones.MaxLengthApellido) || CUtilidades.excedeLongitud(propietario.getApellido2(), CConstantesOcupaciones.MaxLengthApellido)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje3"));
				return false;
			}
            propietario.getDatosNotificacion().setEmail(emailPropietarioJTField.getText().trim());

			if (CUtilidades.excedeLongitud(propietario.getDatosNotificacion().getEmail(), CConstantesOcupaciones.MaxLengthEmail)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje6"));
				return false;
			}

					if (CUtilidades.excedeLongitud(propietario.getDatosNotificacion().getNombreVia(), CConstantesOcupaciones.MaxLengthNombreVia)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje8"));
				return false;
			}
            propietario.getDatosNotificacion().setPortal(portalPropietarioJTField.getText().trim());
            if (CUtilidades.excedeLongitud(propietario.getDatosNotificacion().getPortal(), CConstantesOcupaciones.MaxLengthPortal)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje11"));
				return false;
			}
            propietario.getDatosNotificacion().setPlanta(plantaPropietarioJTField.getText().trim());

			if (propietario.getDatosNotificacion().getPlanta().length() > 0) {
                if (CUtilidades.excedeLongitud(propietario.getDatosNotificacion().getPlanta(), CConstantesOcupaciones.MaxLengthPlanta)) {
                    mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje13"));
                    return false;
                }
			}
            propietario.getDatosNotificacion().setEscalera(escaleraPropietarioJTField.getText().trim());

			if (CUtilidades.excedeLongitud(propietario.getDatosNotificacion().getEscalera(), CConstantesOcupaciones.MaxLengthPlanta)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje14"));
				return false;
			}

            propietario.getDatosNotificacion().setLetra(letraPropietarioJTField.getText().trim());
       		if (CUtilidades.excedeLongitud(propietario.getDatosNotificacion().getLetra(), CConstantesOcupaciones.MaxLengthLetra)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje15"));
				return false;
			}

			if (CUtilidades.excedeLongitud(propietario.getDatosNotificacion().getMunicipio(), CConstantesOcupaciones.MaxLengthMunicipio)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje18"));
				return false;
			}

			if (CUtilidades.excedeLongitud(propietario.getDatosNotificacion().getProvincia(), CConstantesOcupaciones.MaxLengthProvincia)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.PropietarioTab.mensaje19"));
				return false;
			}
            propietario.getDatosNotificacion().setNotificar(notificarPropietarioJCheckBox.isSelected()?"1":"0");
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
			if (representante.getDniCif().length() > 0) {
				if (CUtilidades.excedeLongitud(representante.getDniCif(), CConstantesOcupaciones.MaxLengthDNI)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje1"));
					return false;
				}
				if (CUtilidades.excedeLongitud(representante.getNombre(), CConstantesOcupaciones.MaxLengthNombre)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje2"));
					return false;
				}
				if (CUtilidades.excedeLongitud(representante.getApellido1(), CConstantesOcupaciones.MaxLengthApellido) || CUtilidades.excedeLongitud(representante.getApellido2(), CConstantesOcupaciones.MaxLengthApellido)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje3"));
					return false;
				}

                representante.getDatosNotificacion().setEmail(emailRepresentanteJTField.getText().trim());
				if (CUtilidades.excedeLongitud(representante.getDatosNotificacion().getEmail(), CConstantesOcupaciones.MaxLengthEmail)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje6"));
					return false;
				}

				if (CUtilidades.excedeLongitud(representante.getDatosNotificacion().getNombreVia(), CConstantesOcupaciones.MaxLengthNombreVia)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje8"));
					return false;
				}
                representante.getDatosNotificacion().setPortal(portalRepresentanteJTField.getText().trim());
				if (representante.getDatosNotificacion().getPortal().length() > 0) {
					if (CUtilidades.excedeLongitud(representante.getDatosNotificacion().getPortal(), CConstantesOcupaciones.MaxLengthPortal)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje11"));
						return false;
					}
				}
                representante.getDatosNotificacion().setPlanta(plantaRepresentanteJTField.getText().trim());
				if (representante.getDatosNotificacion().getPlanta().length() > 0) {
                    if (CUtilidades.excedeLongitud(representante.getDatosNotificacion().getPlanta(), CConstantesOcupaciones.MaxLengthPlanta)) {
                        mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje13"));
                        return false;
                    }
				}
				representante.getDatosNotificacion().setEscalera(escaleraRepresentanteJTField.getText().trim());
				if (CUtilidades.excedeLongitud(representante.getDatosNotificacion().getEscalera(), CConstantesOcupaciones.MaxLengthPlanta)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje14"));
					return false;
				}
				representante.getDatosNotificacion().setLetra(letraRepresentanteJTField.getText().trim());
				if (CUtilidades.excedeLongitud(representante.getDatosNotificacion().getLetra(), CConstantesOcupaciones.MaxLengthLetra)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje15"));
					return false;
				}

				if (CUtilidades.excedeLongitud(representante.getDatosNotificacion().getMunicipio(), CConstantesOcupaciones.MaxLengthMunicipio)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje18"));
					return false;
				}

				if (CUtilidades.excedeLongitud(representante.getDatosNotificacion().getProvincia(), CConstantesOcupaciones.MaxLengthProvincia)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("ChequeoLicenciasOcupacion.RepresentanteTab.mensaje19"));
					return false;
				}
			} else {
				borrarCamposRepresentante();
			}
            representante.getDatosNotificacion().setNotificar(notificarRepresentanteJCheckBox.isSelected()?"1":"0");

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
			/** Estados posibles del expediente */
			logger.info("expediente.getSilencioTriggered(): " + expediente.getSilencioTriggered());

			Vector v = COperacionesLicencias.getEstadosDisponibles(expediente,CConstantesComando.TIPO_OCUPACION);

			if (v != null) {
				int i = 0;
				Enumeration e = v.elements();
				while (e.hasMoreElements()) {
					CEstado estado = (CEstado) e.nextElement();
					h.put(new Integer(i), estado);
					i++;
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

	/**
	 * Expediente
	 */
	public void rellenarExpediente() {
		try {
			/** Expediente */

            /** Estado del expediente */
            estadoExpedienteEJCBox.setSelectedPatron(new Integer(expediente.getEstado().getIdEstado()).toString());

			/** Tramitacion del expediente */
			tramitacionEJCBox.setSelectedPatron(new Integer(expediente.getTipoTramitacion().getIdTramitacion()).toString());

			/** Finalizacion del expediente */
            if (expediente.getTipoFinalizacion()!=null)
			    finalizacionEJCBox.setSelectedPatron(new Integer(expediente.getTipoFinalizacion().getIdFinalizacion()).toString());

			// Actualizamos campos
			numExpedienteJTField.setText(expediente.getNumExpediente());
			numExpedienteJTField.setEnabled(false);

			servicioExpedienteJTField.setText(expediente.getServicioEncargado());
			asuntoExpedienteJTField.setText(expediente.getAsunto());
			fechaAperturaJTField.setText(CUtilidades.formatFecha(expediente.getFechaApertura()));
			fechaAperturaJTField.setEnabled(false);
			inicioJTField.setText(expediente.getFormaInicio());
			if (expediente.getSilencioAdministrativo().equalsIgnoreCase("1")) silencioJCheckBox.setSelected(true);
			numFoliosJTField.setText(new Integer(expediente.getNumFolios()).toString());
			responsableJTField.setText(expediente.getResponsable());
			plazoVencimientoJTField.setText(CUtilidades.formatFecha(expediente.getPlazoVencimiento()));
			numDiasJTField.setText(new Integer(expediente.getNumDias()).toString());
			if (expediente.getHabiles().equalsIgnoreCase("1")) habilesJCheckBox.setSelected(true);
			observacionesExpedienteJTArea.setText(expediente.getObservaciones());
			_vu = expediente.getVU();

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
			/** Tipos de ocupacion */
			CDatosOcupacion datosOcupacion = expediente.getDatosOcupacion();
			tipoOcupacionEJCBox.setSelectedPatron(new Integer(datosOcupacion.getTipoOcupacion()).toString());

			/** Referencias Catastrales */
			cargarReferenciasCatastralesJTable();
			/** Anexos */
			cargarAnexosJTable();

			// Actualizamos campos
			motivoJTField.setText(solicitud.getMotivo());
			asuntoJTField.setText(solicitud.getAsunto());
			fechaSolicitudJTField.setText(CUtilidades.formatFecha(solicitud.getFecha()));
			fechaSolicitudJTField.setEnabled(false);
			observacionesJTArea.setText(solicitud.getObservaciones());

			horaInicioJTimeTField.setText(CUtilidades.formatHora(datosOcupacion.getHoraInicio()));
			horaFinJTimeTField.setText(CUtilidades.formatHora(datosOcupacion.getHoraFin()));
			numSillasJNTField.setNumber(new Integer(datosOcupacion.getNumSillas()));
			numMesasJNTField.setNumber(new Integer(datosOcupacion.getNumMesas()));
			areaOcupacionJNTField.setNumber(new Double(datosOcupacion.getAreaOcupacion()));
			m2AceraJNTField.setNumber(new Double(datosOcupacion.getM2acera()));
			m2CalzadaJNTField.setNumber(new Double(datosOcupacion.getM2calzada()));
			m2AparcamientoJNTField.setNumber(new Double(datosOcupacion.getM2aparcamiento()));

			if (datosOcupacion.getAfectaAcera().equalsIgnoreCase("1"))
				aceraJCheckBox.setSelected(true);
			else
				aceraJCheckBox.setSelected(false);

			if (datosOcupacion.getAfectaCalzada().equalsIgnoreCase("1"))
				calzadaJCheckBox.setSelected(true);
			else
				calzadaJCheckBox.setSelected(false);

			if (datosOcupacion.getAfectaAparcamiento().equalsIgnoreCase("1"))
				aparcamientoJCheckBox.setSelected(true);
			else
				aparcamientoJCheckBox.setSelected(false);

			if (datosOcupacion.getNecesitaObra().equalsIgnoreCase("1"))
				necesitaObraJCheckBox.setSelected(true);
			else
				necesitaObraJCheckBox.setSelected(false);

			numExpedienteObraJTField.setText(datosOcupacion.getNumExpediente());

            if ((solicitud.getTipoViaAfecta() != null) && (solicitud.getTipoViaAfecta().trim().length() > 0)){
			    tipoViaINEEJCBox.setSelectedPatron(solicitud.getTipoViaAfecta());
            }else{
                tipoViaINEEJCBox.setSelectedIndex(0);
            }
			nombreViaTField.setText(solicitud.getNombreViaAfecta());
			numeroViaNumberTField.setText(solicitud.getNumeroViaAfecta());
			portalViaTField.setText(solicitud.getPortalAfecta());
			plantaViaTField.setText(solicitud.getPlantaAfecta());
			letraViaTField.setText(solicitud.getLetraAfecta());
            //cpostalViaTField.setText(solicitud.getCpostalAfecta());
            try{
                cpostalViaTField.setNumber(new Integer(solicitud.getCpostalAfecta()));
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
			CPersonaJuridicoFisica propietario = solicitud.getPropietario();
			if (propietario != null) {
				viaNotificacionPropietarioEJCBox.setSelectedPatron(new Integer(solicitud.getPropietario().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
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
                //cPostalPropietarioJTField.setText(propietario.getDatosNotificacion().getCpostal());
                try{
                    cPostalPropietarioJTField.setNumber(new Integer(propietario.getDatosNotificacion().getCpostal()));
                }catch (Exception e){cPostalPropietarioJTField.setText("");}
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
			CPersonaJuridicoFisica representante = solicitud.getRepresentante();

			if (representante != null) {
				viaNotificacionRepresentanteEJCBox.setSelectedPatron(new Integer(solicitud.getRepresentante().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
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
				//cPostalRepresentanteJTField.setText(representante.getDatosNotificacion().getCpostal());
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

			// Annadimos a la tabla el editor ComboBox en la primera columna (estado)
			int vColIndexCB = 0;
			TableColumn col0 = notificacionesJTable.getColumnModel().getColumn(vColIndexCB);

			_vNotificaciones = expediente.getNotificaciones();
			if ((_vNotificaciones != null) && (_vNotificaciones.size() > 0)) {
				// Damos valores para la combo de la primera columna

				col0.setCellEditor(new ComboBoxTableEditor(new ComboBoxEstructuras(Estructuras.getListaEstadosNotificacion(), null, CMainOcupaciones.currentLocale.toString(), false)));
				col0.setCellRenderer(new ComboBoxRendererEstructuras(Estructuras.getListaEstadosNotificacion(), null, CMainOcupaciones.currentLocale.toString(), false));

				for (int i=0; i<_vNotificaciones.size(); i++) {
					CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(i);
					int tipoEstado = notificacion.getEstadoNotificacion().getIdEstado();
                    ComboBoxEstructuras combox = (ComboBoxEstructuras) ((ComboBoxTableEditor) notificacionesJTable.getCellEditor(i, 0)).getComponent();
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
                    /*
					ComboBoxEstructuras combox = (ComboBoxEstructuras) ((ComboBoxTableEditor) notificacionesJTable.getCellEditor(row, 0)).getComponent();
					combox.setSelectedPatron(new Integer(tipoEstado).toString());
					Object[] rowData = {combox.getSelectedItem(), CUtilidades.formatFechaH24(notificacion.getPlazoVencimiento()), notificacion.getPersona().getDniCif(), CUtilidades.formatFechaH24(notificacion.getFechaNotificacion()), CUtilidades.formatFechaH24(notificacion.getFecha_reenvio())};
                    */
                    Object[] rowData = {combox.getSelectedItem(),
                                        CUtilidades.formatFecha(notificacion.getPlazoVencimiento()),
                                        notificacion.getPersona().getDniCif(),
                                        CUtilidades.formatFecha(notificacion.getFechaNotificacion()),
                                        CUtilidades.formatFecha(notificacion.getFecha_reenvio()),
                                        new Integer(i)};
					_notificacionesExpedienteTableModel.addRow(rowData);
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

			_vEventos = expediente.getEventos();
			if ((_vEventos != null) && (_vEventos.size() > 0)) {

				for (int i=0; i<_vEventos.size(); i++) {
					CEvento evento = (CEvento) _vEventos.get(i);

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

			_vHistorico = expediente.getHistorico();
			if ((_vHistorico != null) && (_vHistorico.size() > 0)) {

                for (int i=0; i < _vHistorico.size(); i++){
                    CHistorico historico= (CHistorico)_vHistorico.get(i);
                    String estado= "";
                    try{
                        estado= ((DomainNode) Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
                    }catch(Exception e){logger.error("Error al cargar el estado del expediente para el historico "+historico.getIdHistorico() + e.toString());}

                    String apunte= historico.getApunte();
                    /** Comprobamos que el historico no sea generico, en cuyo caso, no es necesario annadir
                     * al apunte el literal del estado al que ha cambiado.
                     */                    
                    if ((historico.getSistema().equalsIgnoreCase("1")) && (historico.getGenerico() == 0)){
                        /** Componemos el apunte, de forma multilingue */
                        apunte+= " " +
                                ((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()) + ".";
                        historico.setApunte(apunte);
                    }

                    vAuxiliar.add(i, historico);


                    _historicoExpedienteTableModel.addRow(new Object[]{estado,
                                                                       historico.getUsuario(),
                                                                       CUtilidades.formatFecha(historico.getFechaHistorico()),
                                                                       historico.getApunte(),
                                                                       new Integer(i)});
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


	/**
	 * Referencias Catastrales
	 */
	public void cargarReferenciasCatastralesJTable() {
		try {

			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
			Vector referenciasCatastrales = solicitud.getReferenciasCatastrales();
			logger.info("referenciasCatastrales: " + referenciasCatastrales);

			if ((referenciasCatastrales == null) || (referenciasCatastrales.isEmpty())) {
				logger.info("No hay referenciasCatastrales para la licencia.");
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
 			}
            Vector vBusqueda=new Vector();

			for (int i = 0; i < referenciasCatastrales.size(); i++) {
				CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) referenciasCatastrales.elementAt(i);
                Double area= new Double(0);
                try{
                    area= referenciaCatastral.getArea()==null?new Double(0):new Double(referenciaCatastral.getArea().doubleValue());
                }catch (Exception e){}
				referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
                                                                      area,
                                                                      referenciaCatastral.getTipoVia()==null?"":referenciaCatastral.getTipoVia(),
																	  referenciaCatastral.getNombreVia(),
																	  referenciaCatastral.getPrimerNumero(),
                                                                      referenciaCatastral.getPrimeraLetra(),
                                                                      referenciaCatastral.getBloque(),
                                                                      referenciaCatastral.getEscalera(),
                                                                      referenciaCatastral.getPlanta(),
                                                                      referenciaCatastral.getPuerta() });
                vBusqueda.add(referenciaCatastral.getReferenciaCatastral());
             }
            refreshFeatureSelection(layerName,0,vBusqueda);
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
            propietario=new CPersonaJuridicoFisica();
			propietario.setDniCif(DNIPropietarioJTField.getText().trim());
			propietario.setNombre(nombrePropietarioJTField.getText().trim());
			propietario.setApellido1(apellido1PropietarioJTField.getText().trim());
			propietario.setApellido2(apellido2PropietarioJTField.getText().trim());

            if ((propietario.getDniCif().length() == 0) || (propietario.getNombre().length() == 0)) return false;

            CDatosNotificacion datosNotificacion= new CDatosNotificacion();
            datosNotificacion.setNombreVia(nombreViaPropietarioJTField.getText().trim());
            datosNotificacion.setNumeroVia(numeroViaPropietarioJTField.getText().trim());
            try{datosNotificacion.setCpostal(cPostalPropietarioJTField.getNumber().toString());}catch(Exception e){datosNotificacion.setCpostal("");};
			datosNotificacion.setMunicipio(municipioPropietarioJTField.getText().trim());
            datosNotificacion.setProvincia(provinciaPropietarioJTField.getText().trim());
			if ((datosNotificacion.getNombreVia().length() == 0) ||
					(datosNotificacion.getNumeroVia().length() == 0) || (datosNotificacion.getCpostal().length() == 0) ||
					(datosNotificacion.getMunicipio().length() == 0) || (datosNotificacion.getProvincia().length() == 0))
				return false;

            /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
            if (emailPropietarioObligatorio){
                if (emailPropietarioJTField.getText().trim().length() == 0) return false;
            }
            propietario.setDatosNotificacion(datosNotificacion);
			/** Comprobamos los datos del representante */
			// leemos los datos referentes al representante
            representante = new CPersonaJuridicoFisica();
			representante.setDniCif(DNIRepresentanteJTField.getText().trim());
			representante.setNombre(nombreRepresentanteJTField.getText().trim());
			representante.setApellido1(apellido1RepresentanteJTField.getText().trim());
			representante.setApellido2(apellido2RepresentanteJTField.getText().trim());

            CDatosNotificacion datosNoRe = new CDatosNotificacion();
			datosNoRe.setNombreVia(nombreViaRepresentanteJTField.getText().trim());
            datosNoRe.setNumeroVia(numeroViaRepresentanteJTField.getText().trim());
			try{datosNoRe.setCpostal(cPostalRepresentanteJTField.getNumber().toString());}catch(Exception e){datosNoRe.setCpostal("");};
			datosNoRe.setMunicipio(municipioRepresentanteJTField.getText().trim());
			datosNoRe.setProvincia(provinciaRepresentanteJTField.getText().trim());

			if ((solicitud.getRepresentante() != null) && (solicitud.getIdRepresentanteToDelete() == -1)) {
				/** solicitud con representante y este no se ha borrado ->
				 * si ALGUN campo obligatorio no esta rellenado, error */
				if ((representante.getDniCif().length() == 0) || (representante.getNombre().length() == 0)) return false;

				if ((datosNoRe.getNombreVia().length() == 0) || (datosNoRe.getNumeroVia().length() == 0) ||
						(datosNoRe.getCpostal().length() == 0) || (datosNoRe.getMunicipio().length() == 0) ||
						(datosNoRe.getProvincia().length() == 0))
					return false;
                /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
                if (emailRepresentanteObligatorio){
                    if (emailRepresentanteJTField.getText().trim().length() == 0) return false;
                }

			} else {
				 /* si TODOS los campos obligatorios estan sin rellenar, ok
				 * si TODOS los campos obligatorios estan rellenados, ok (se ha insertado uno nuevo)
				 * si solo ALGUNO esta rellenado, error */
				if (!(((representante.getDniCif().length() == 0) && (representante.getNombre().length() == 0) &&
						(datosNoRe.getNombreVia().length() == 0) && (datosNoRe.getNumeroVia().length() == 0) &&
						(datosNoRe.getCpostal().length() == 0) && (datosNoRe.getMunicipio().length() == 0) &&
						(datosNoRe.getProvincia().length() == 0) &&
                        (!emailRepresentanteObligatorio?true:emailRepresentanteJTField.getText().trim().length() == 0)) ||
						((representante.getDniCif().length() != 0) && (representante.getNombre().length() != 0) &&
						(datosNoRe.getNombreVia().length() != 0) && (datosNoRe.getNumeroVia().length() != 0) &&
						(datosNoRe.getCpostal().length() != 0) && (datosNoRe.getMunicipio().length() != 0) &&
						(datosNoRe.getProvincia().length() != 0) &&
                        (!emailRepresentanteObligatorio?true:emailRepresentanteJTField.getText().trim().length() != 0))))
					return false;

			}
            representante.setDatosNotificacion(datosNoRe);
            /** Otros campos obligatorios: fecha solictud, tipo ocupación, tipo y
             * nombre de via del emplazamiento principal. */
			if (fechaSolicitudJTField.getText().trim().length() == 0)
				return false;
			else if (tipoOcupacionEJCBox.getSelectedPatron() == null)
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
        JOptionPane.showMessageDialog(desktop, mensaje);
    }

	private boolean clearScreen() {
		/** Solicitud */
		CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
		CUtilidadesComponentes.clearTable(_listaAnexosTableModel);

		motivoJTField.setText("");
		asuntoJTField.setText("");
		fechaSolicitudJTField.setText("");
		fechaSolicitudJTField.setEnabled(false);
		observacionesJTArea.setText("");

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
		numFoliosJTField.setText("");
		responsableJTField.setText("");
		plazoVencimientoJTField.setText("");
		numDiasJTField.setText("");
		habilesJCheckBox.setSelected(false);
		observacionesExpedienteJTArea.setText("");

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

		/** Notificaciones */
		CUtilidadesComponentes.clearTable(_notificacionesExpedienteTableModel);

		/** Eventos */
		CUtilidadesComponentes.clearTable(_eventosExpedienteTableModel);

		/** Historico */
		CUtilidadesComponentes.clearTable(_historicoExpedienteTableModel);

		return true;
	}

    public void desbloquearExpediente(){
        if ((expediente != null) && (expediente.bloqueaUsuario())){
            COperacionesLicencias.bloquearExpediente(expediente.getNumExpediente(), false);
        }
    }

    public void renombrarComponentes() {
        renombrarComponentes(false);
    }


	public void renombrarComponentes(boolean construir) {

		try {
			setTitle(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.JInternalFrame.title"));
            if (construir) {
                /** Pestanas */
                try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/expediente.jpg"));
                    ocupacionJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.expedienteJPanel.TitleTab")), icon,expedienteJPanel);

                }catch(Exception e)
                {
                    ocupacionJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.expedienteJPanel.TitleTab")), expedienteJPanel);
                }
                JTabbedPane jTabbedSolicitud= new JTabbedPane();
                try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/solicitud.jpg"));
                    jTabbedSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), icon, ocupacionJPanel);

                }catch(Exception e)
                {
                     jTabbedSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), ocupacionJPanel);
                }

                try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/persona.jpg"));
                    jTabbedSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.propietarioJPanel.TitleTab")), icon, propietarioJPanel);


                }catch(Exception e)
                {
                     jTabbedSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.propietarioJPanel.TitleTab")), propietarioJPanel);

                }
                try
               {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/representante.jpg"));
                    jTabbedSolicitud.addTab(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.representanteJPanel.TitleTab"), icon,representanteJPanel);
              }catch(Exception e)
              {
                    jTabbedSolicitud.addTab(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.representanteJPanel.TitleTab"), representanteJPanel);
              }

                try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/solicitud.jpg"));
                    ocupacionJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), icon,jTabbedSolicitud);

                }catch(Exception e)
                {
                    ocupacionJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), jTabbedSolicitud);
                }

                try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/calle.jpg"));
                    ocupacionJTabbedPane.addTab(CMainOcupaciones.literales.getString("jPanelCallesAfectadas.title"),icon,jPanelCallesAfectadas);//CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), icon,jTabbedSolicitud);

                }catch(Exception e)
                {
                    ocupacionJTabbedPane.addTab(CMainOcupaciones.literales.getString("jPanelCallesAfectadas.title"),jPanelCallesAfectadas);//CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), jTabbedSolicitud);
                }
                try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/notificacion.jpg"));
                    ocupacionJTabbedPane.addTab(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJPanel.TitleTab"), icon,notificacionesJPanel);
                }catch(Exception e)
                {
                    ocupacionJTabbedPane.addTab(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJPanel.TitleTab"), notificacionesJPanel);
                }
                try
               {
                   ClassLoader cl =this.getClass().getClassLoader();
                   Icon icon= new javax.swing.ImageIcon(cl.getResource("img/evento.jpg"));
                   ocupacionJTabbedPane.addTab(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJPanel.TitleTab"), icon, eventosJPanel);
               }catch(Exception e)
               {
                   ocupacionJTabbedPane.addTab(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJPanel.TitleTab"),  eventosJPanel);
               }
                try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/historico.jpg"));
                    ocupacionJTabbedPane.addTab(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJPanel.TitleTab"), icon,historicoJPanel);
                }catch(Exception e)
                {
                    ocupacionJTabbedPane.addTab(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJPanel.TitleTab"), historicoJPanel);
                }
            }
            else {
                ocupacionJTabbedPane.setTitleAt(0, com.geopista.app.ocupaciones.CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.expedienteJPanel.TitleTab")));
                ocupacionJTabbedPane.setTitleAt(1, com.geopista.app.ocupaciones.CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")));
                JTabbedPane jTabbedSolicitud= new JTabbedPane();
                jTabbedSolicitud = (JTabbedPane)ocupacionJTabbedPane.getComponentAt(1);
                jTabbedSolicitud.setTitleAt(0, com.geopista.app.ocupaciones.CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")));
                jTabbedSolicitud.setTitleAt(1, CMainOcupaciones.literales.getString("CModificacionLicenciasForm.propietarioJPanel.TitleTab"));
                jTabbedSolicitud.setTitleAt(2, com.geopista.app.ocupaciones.CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.representanteJPanel.TitleTab")));
                ocupacionJTabbedPane.setTitleAt(2, CMainOcupaciones.literales.getString("jPanelCallesAfectadas.title"));
                ocupacionJTabbedPane.setTitleAt(3, CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJPanel.TitleTab"));
                ocupacionJTabbedPane.setTitleAt(4, CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJPanel.TitleTab"));
                ocupacionJTabbedPane.setTitleAt(5, CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJPanel.TitleTab"));
            }


			/** Expediente */
			expedienteJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.expedienteJPanel.TitleBorder")));
			estadoExpedienteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.estadoExpedienteJLabel.text")));
			numExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.numExpedienteJLabel.text"));
			servicioExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.servicioExpedienteJLabel.text"));
			tramitacionJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.tramitacionJLabel.text"));
			finalizaJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.finalizaJLable.text"));
			asuntoExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.asuntoExpedienteJLabel.text"));
			fechaAperturaJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.fechaAperturaJLabel.text"));
			observacionesExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.observacionesExpedienteJLabel.text"));
			inicioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.inicioJLabel.text"));
			numFoliosJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.numFoliosJLabel.text"));
			responsableJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.responsableJLabel.text"));
			plazoVencimientoJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.plazoVencimientoJLabel.text"));
			numDiasJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.numDiasJLabel.text"));
			consultarJButton.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.consultarJButton.text"));
			silencioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.silencioJLabel.text"));
			habilesJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.habilesJLabel.text"));
			notaJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notaJLabel.text"));

			/** Solicitud */
			datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosSolicitudJPanel.TitleBorder")));
            tipoOcupacionJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.tipoOcupacionJLabel.text")));
			motivoJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.motivoJLabel.text"));
			asuntoJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.asuntoJLabel.text"));
			fechaSolicitudJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.fechaSolicitudJLabel.text")));
			observacionesJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.observacionesJLabel.text"));
			emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.emplazamientoJPanel.TitleBorder")));
			nombreViaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.tipoViaJLabel.text"), CMainOcupaciones.literales.getString("CModificacionLicenciasForm.nombreViaJLabel.text")));
			numeroViaJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.numeroViaJLabel.text"));
			cPostalJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.cPostalJLabel.text"));
			provinciaJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.provinciaJLabel.text"));
			anexosJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.anexosJPanel.TitleBorder")));
			numSillasJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.numSillasJLabel.text"));
			mesasJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.numMesasJLabel.text"));
			areaOcupacionJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.areaOcupacionJLabel.text"));
			metros2JLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.metros2JLabel.text"));
			horaJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.horaJLabel.text"));
			afectaJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.afectaJLabel.text"));
            necesitaObraJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.necesitaObraJLabel.text"));
            numExpedienteObraJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.numExpedienteObraJLabel.text"));
            aceraJCheckBox.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.aceraJCheckBox.text"));
            aparcamientoJCheckBox.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.aparcamientoJCheckBox.text"));
            calzadaJCheckBox.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.calzadaJCheckBox.text"));

			numSillasJNTField.setNumber(new Integer(0));
			numMesasJNTField.setNumber(new Integer(0));
			areaOcupacionJNTField.setNumber(new Double(0));
			m2AceraJNTField.setNumber(new Double(0));
			m2CalzadaJNTField.setNumber(new Double(0));
			m2AparcamientoJNTField.setNumber(new Double(0));
			horaInicioJTimeTField.setText("00:00");
			horaFinJTimeTField.setText("00:00");
			aceraJCheckBox.setSelected(false);
			calzadaJCheckBox.setSelected(false);
			aparcamientoJCheckBox.setSelected(false);

			/** Propietario */
			datosPersonalesPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosPersonalesPropietarioJPanel.TitleBorder")));
			DNIPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.DNIPropietarioJLabel.text")));
			nombrePropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.nombrePropietarioJLabel.text")));
			apellido1PropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.apellido1PropietarioJLabel.text"));
			apellido2PropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.apellido2PropietarioJLabel.text"));
			datosNotificacionPropietarioJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosNotificacionPropietarioJPanel.TitleTab")));
			viaNotificacionPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.viaNotificacionPropietarioJLabel.text"));
			faxPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.faxPropietarioJLabel.text"));
			telefonoPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.telefonoPropietarioJLabel.text"));
			movilPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.movilPropietarioJLabel.text"));
			emailPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.emailPropietarioJLabel.text"));
			tipoViaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.tipoViaPropietarioJLabel.text")));
			nombreViaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.nombreViaPropietarioJLabel.text")));
			numeroViaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.numeroViaPropietarioJLabel.text")));
			portalPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.portalPropietarioJLabel.text"));
			plantaPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.plantaPropietarioJLabel.text"));
			escaleraPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.escaleraPropietarioJLabel.text"));
			letraPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.letraPropietarioJLabel.text"));
			cPostalPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.cPostalPropietarioJLabel.text")));
			municipioPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.municipioPropietarioJLabel.text")));
			provinciaPropietarioJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.provinciaPropietarioJLabel.text")));
			notificarPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificarPropietarioJLabel.text"));

			/** Representante */
			datosPersonalesRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosPersonalesRepresentanteJPanel.TitleBorder")));
			DNIRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.DNIRepresentanteJLabel.text")));
			nombreRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.nombreRepresentanteJLabel.text")));
			apellido1RepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.apellido1RepresentanteJLabel.text"));
			apellido2RepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.apellido2RepresentanteJLabel.text"));
			datosNotificacionRepresentanteJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosNotificacionRepresentanteJPanel.TitleTab")));
			viaNotificacionRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.viaNotificacionRepresentanteJLabel.text"));
			faxRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.faxRepresentanteJLabel.text"));
			telefonoRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.telefonoRepresentanteJLabel.text"));
			movilRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.movilRepresentanteJLabel.text"));
			emailRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.emailRepresentanteJLabel.text"));
			tipoViaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.tipoViaRepresentanteJLabel.text")));
			nombreViaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.nombreViaRepresentanteJLabel.text")));
			numeroViaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.numeroViaRepresentanteJLabel.text")));
			portalRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.portalRepresentanteJLabel.text"));
			plantaRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.plantaRepresentanteJLabel.text"));
			escaleraRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.escaleraRepresentanteJLabel.text"));
			letraRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.letraRepresentanteJLabel.text"));
			cPostalRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.cPostalRepresentanteJLabel.text")));
			municipioRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.municipioRepresentanteJLabel.text")));
			provinciaRepresentanteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.provinciaRepresentanteJLabel.text")));
			notificarRepresentanteJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificarRepresentanteJLabel.text"));

			/** notificaciones */
			datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosNotificacionesJPanel.TitleBorder")));
			datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosNotificacionJPanel.TitleBorder")));
			datosNombreApellidosJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosNombreApellidosJLabel.text"));
			datosDireccionJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosDireccionJLabel.text"));
			datosCPostalJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosCPostalJLabel.text"));
			datosNotificarPorJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosNotificarPorJLabel.text"));
            entregadaAJLabel.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosEntregadaAJLabel.text"));

			/** Eventos */
			datosEventosJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosEventosJPanel.TitleBorder")));
			descEventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.descEventoJScrollPane.TitleBorder")));

			/** Historico */
			datosHistoricoJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.datosHistoricoJPanel.TitleBorder")));
			apunteJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.apunteJScrollPane.TitleBorder")));

			cancelarJButton.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.salirJButton.text"));
			aceptarJButton.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.modificarJButton.text"));
			
			publicarJButton.setText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.publicarJButton.text"));
			
			editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.editorMapaJPanel.TitleBorder")));

            jButtonGenerarFicha.setText(CMainOcupaciones.literales.getString("CMainOcupaciones.jButtonGenerarFicha"));

            /** Historico */
            modHistoricoJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.modHistoricoJButton.setToolTipText.text"));
            borrarHistoricoJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.borrarHistoricoJButton.setToolTipText.text"));
            nuevoHistoricoJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.nuevoHistoricoJButton.setToolTipText.text"));
            generarFichaHistoricoJButton.setToolTipText(CMainOcupaciones.literales.getString("CMantenimientoHistorico.generarFichaHistoricoJButton.setToolTipText.text"));

            /** Anexos */
            annadirJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.annadirJButton.toolTipText.text"));
            eliminarJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.eliminarJButton.toolTipText.text"));
            abrirJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.abrirJButton.toolTipText.text"));
            okJButton.setToolTipText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.okJButtonToolTipText.text"));

            jPanelCallesAfectadas.renombrarComponentes();

            ((TitledBorder)datosEventosJPanel.getBorder()).setTitle(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJPanel.TitleTab"));
            ((TitledBorder)((JScrollPane)datosEventosJPanel.getComponent(1)).getBorder()).setTitle(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.descEventoJScrollPane.TitleBorder"));

            ((TitledBorder)datosHistoricoJPanel.getBorder()).setTitle(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJPanel.TitleTab"));
            ((TitledBorder)((JScrollPane)datosHistoricoJPanel.getComponent(1)).getBorder()).setTitle(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.apunteJScrollPane.TitleBorder"));

            consultarJButton.setToolTipText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.consultarJButton.text"));
            buscarExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.buscarExpedienteJButton.toolTip.text"));
            tableToMapAllJButton.setToolTipText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.tableToMapAllJButton.toolTip.text"));
            tableToMapOneJButton.setToolTipText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.tableToMapOneJButton.toolTip.text"));

            deleteGeopistaFeatureJButton.setToolTipText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.deleteGeopistaFeatureJButton.toolTip.text"));
            buscarExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.buscarExpedienteJButton.toolTip.text"));
            areaOcupacionJButton.setToolTipText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.areaOcupacionJButton.toolTip.text"));
            tableToMapOneJButton.setToolTipText(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.tableToMapOneJButton.toolTip.text"));
            buscarDNIPropietarioJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.buscarDNITitularJButton.text"));
            buscarExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.buscarNumExpedienteJButton.text"));
            nombreViaJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.nombreJButton.text"));
            buscarDNIRepresentanteJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.buscarDNIRepresentaAJButton.text"));
            borrarRepresentanteJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.borrarRepresentanteJButton.text"));
            eliminarJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.eliminarJButton.toolTipText.text"));
            annadirJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.annadirJButton.toolTipText.text"));
            abrirJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.abrirJButton.toolTipText.text"));
            aceptarJButton.setToolTipText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.aceptarJButton.text"));
            
            publicarJButton.setToolTipText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.publicarJButton.text"));
            
            cancelarJButton.setToolTipText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cancelarJButton.text"));
            buscarLicenciaObraJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.buscarLicenciaObraJButton.text"));
            jButtonPlazoVen.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.jButtonPlazoVen.text"));

            /** Headers de la tabla eventos */
            TableColumn tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column1"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column10"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column2"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column3"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column4"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column5"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column6"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column7"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column8"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(9);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.referenciasCatastralesJTable.column9"));

            tableColumn= listaAnexosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.anexosJTable.column1"));
            tableColumn= listaAnexosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.anexosJTable.column2"));
            tableColumn= listaAnexosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.anexosJTable.column3"));

            tableColumn= notificacionesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum1"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum2"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum3"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum4"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.notificacionesJTable.colum5"));

            tableColumn= eventosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum1"));
            tableColumn= eventosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum2"));
            tableColumn= eventosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum3"));
            tableColumn= eventosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum4"));
            tableColumn= eventosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.eventosJTable.colum5"));

            tableColumn= historicoJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJTable.colum2"));
            tableColumn= historicoJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJTable.colum3"));
            tableColumn= historicoJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJTable.colum4"));
            tableColumn= historicoJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CModificacionLicenciasForm.historicoJTable.colum5"));

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}
	
	  private void docManager() {    
	       	if(solicitud != null && expediente != null){
	   	    	String app = getAlfrescoAppName(solicitud.getTipoLicencia().getIdTipolicencia());	    	
	   	    	if(app != null){
	   	    		(new AlfrescoExplorer(AppContext.getApplicationContext().getMainFrame(),
	   						solicitud.getIdSolicitud(), getAnexosUuid(), String.valueOf(AppContext.getIdMunicipio()), app))
	   						.setVisible(true);
	   	    		recargarAnexos();	    		
	   	    	}
	       	}
	   	}
	       
	       /**
	        * Recarga los anexos en la tabla de anexos asociados a la solicitud
	        * @return Boolean: Resultado de la recarga
	        */
	       private Boolean recargarAnexos(){
		       	try{
		       		CResultadoOperacion resultadoOperacion = COperacionesLicencias.getExpedienteLicencia(expediente.getNumExpediente(), CMainOcupaciones.literales.getLocale().toString(), new Vector());
		       		if (resultadoOperacion.getResultado() && resultadoOperacion.getSolicitudes()!=null){
		       			CSolicitudLicencia solicitudLicencia = (CSolicitudLicencia) resultadoOperacion.getSolicitudes().get(0);
		   	    		if(solicitudLicencia.getAnexos()!=null && solicitudLicencia.getAnexos().size()>0){
		   	    			CUtilidadesComponentes.clearTable(_listaAnexosTableModel);
		   	    			cargarAnexosJTable(solicitudLicencia.getAnexos());
		   	    			return true;
		   	    		}
		       		}
		       	}
		       	catch(Exception ex){
		       		logger.error(ex);    		
		       	}
		       	return false;
	       }
	         
		   	private void cargarAnexosJTable(Vector anexos){
		   		if (anexos != null) {
		   			for (int i = 0; i < anexos.size(); i++) {
		   				CAnexo anexo = (CAnexo) anexos.elementAt(i);
		   				String descTipoAnexo = ((DomainNode) Estructuras.getListaTiposAnexo().getDomainNode(new Integer(anexo.getTipoAnexo().getIdTipoAnexo()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
		   				_listaAnexosTableModel.addRow(new String[]{anexo.getFileName(), descTipoAnexo, anexo.getObservacion()});
		   			}
		   		}
		   	}

	       
	       private static String getAlfrescoAppName(int idTipoLicencia){
		       	if(idTipoLicencia==CConstantesComando.TIPO_OBRA_MAYOR){						
		       		return AlfrescoConstants.APP_MAJORWORKLICENSE;
		   		}
		   		else if(idTipoLicencia==CConstantesComando.TIPO_OBRA_MENOR){						
		   			return AlfrescoConstants.APP_MINORWORKLICENSE;
		   		} 
		   		else if(idTipoLicencia==CConstantesComando.TIPO_ACTIVIDAD){						
		   			return AlfrescoConstants.APP_ACTIVITYLICENSE;
		   		} 	
		   		else if(idTipoLicencia==CConstantesComando.TIPO_ACTIVIDAD_NO_CALIFICADA){						
		   			return AlfrescoConstants.APP_NONQUALIFIEDACTIVITYLICENSE;
		   		} 	
		   		else if(idTipoLicencia==CConstantesComando.TIPO_OCUPACION){						
		   			return AlfrescoConstants.APP_OCUPATIONLICENSE;
		   		} 	
		       	return null;
	       }
	       
	       public ArrayList<String> getAnexosUuid(){
	       		ArrayList<String> anexosUuid = new ArrayList<String>();
	       		Vector<CAnexo> anexos = solicitud.getAnexos();
	       		Iterator<CAnexo> itAnexos = anexos.iterator();
	       		while(itAnexos.hasNext()){
	       			anexosUuid.add(String.valueOf(itAnexos.next().getIdAnexo()));    		
	       		}
	       		return anexosUuid;
	       }
	
    private void calcularArea() {
            //long total=0;
            double total=0;
            for (int i = 0; i < referenciasCatastralesJTable.getRowCount(); i++)
            {
                Object objeto=referenciasCatastralesJTableModel.getValueAt(i, 1);
                if (objeto!=null)
                   //try{total+=((Long)objeto).longValue();}catch(Exception e){}
                try{total+=((Double)objeto).doubleValue();}catch(Exception e){}
            }
            //areaOcupacionJNTField.setNumber(new Long(total));
            areaOcupacionJNTField.setNumber(new Double(total));
        }


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

	public void setNumExpediente(String numExpediente) {

		numExpedienteJTField.setText(numExpediente);
	}


	private DefaultTableModel referenciasCatastralesJTableModel;

	private static JFrame desktop;

	private ComboBoxEstructuras tipoOcupacionEJCBox;
	private ComboBoxEstructuras viaNotificacionPropietarioEJCBox;
	private ComboBoxEstructuras viaNotificacionRepresentanteEJCBox;
    private ComboBoxEstructuras estadoExpedienteEJCBox;

	private ComboBoxEstructuras tipoViaNotificacionPropietarioEJCBox;
	private ComboBoxEstructuras tipoViaNotificacionRepresentanteEJCBox;

	private ComboBoxEstructuras tramitacionEJCBox;
	private ComboBoxEstructuras finalizacionEJCBox;

	private JNumberTextField numSillasJNTField;
	private JNumberTextField numMesasJNTField;
	private JNumberTextField areaOcupacionJNTField;
	private JNumberTextField m2AceraJNTField;
	private JNumberTextField m2CalzadaJNTField;
	private JNumberTextField m2AparcamientoJNTField;

	private JNumberTextField horaInicioJTimeTField;
	private JNumberTextField horaFinJTimeTField;

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
    private javax.swing.JLabel DNIPropietarioJLabel;
    private javax.swing.JTextField DNIPropietarioJTField;
    private javax.swing.JLabel DNIRepresentanteJLabel;
    private javax.swing.JTextField DNIRepresentanteJTField;
    private javax.swing.JButton abrirJButton;
    private javax.swing.JButton aceptarJButton;
    
    private javax.swing.JButton publicarJButton;
    
    private javax.swing.JCheckBox aceraJCheckBox;
    private javax.swing.JLabel afectaJLabel;
    private javax.swing.JPanel anexosJPanel;
    private javax.swing.JButton annadirJButton;
    private javax.swing.JCheckBox aparcamientoJCheckBox;
    private javax.swing.JLabel apellido1PropietarioJLabel;
    private javax.swing.JTextField apellido1PropietarioJTField;
    private javax.swing.JLabel apellido1RepresentanteJLabel;
    private javax.swing.JTextField apellido1RepresentanteJTField;
    private javax.swing.JLabel apellido2PropietarioJLabel;
    private javax.swing.JTextField apellido2PropietarioJTField;
    private javax.swing.JLabel apellido2RepresentanteJLabel;
    private javax.swing.JTextField apellido2RepresentanteJTField;
    private javax.swing.JScrollPane apunteJScrollPane;
    private javax.swing.JTextArea apunteJTArea;
    private javax.swing.JLabel areaOcupacionJLabel;
    private javax.swing.JLabel asuntoExpedienteJLabel;
    private javax.swing.JTextField asuntoExpedienteJTField;
    private javax.swing.JLabel asuntoJLabel;
    private javax.swing.JTextField asuntoJTField;
    private javax.swing.JButton borrarHistoricoJButton;
    private javax.swing.JButton borrarRepresentanteJButton;
    private javax.swing.JPanel botoneraJPanel;
    private javax.swing.JButton buscarDNIPropietarioJButton;
    private javax.swing.JButton buscarDNIRepresentanteJButton;
    private javax.swing.JButton buscarExpedienteJButton;
    private javax.swing.JButton buscarLicenciaObraJButton;
    private javax.swing.JLabel cPostalJLabel;
    private javax.swing.JLabel cPostalPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalPropietarioJTField;
    private javax.swing.JLabel cPostalRepresentanteJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalRepresentanteJTField;
    private javax.swing.JCheckBox calzadaJCheckBox;
    private javax.swing.JButton cancelarJButton;
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
    private javax.swing.JPanel datosNotificacionPropietarioJPanel;
    private javax.swing.JPanel datosNotificacionRepresentanteJPanel;
    private javax.swing.JPanel datosNotificacionesJPanel;
    private javax.swing.JLabel datosNotificarPorJLabel;
    private javax.swing.JTextField datosNotificarPorJTField;
    private javax.swing.JPanel datosPersonalesPropietarioJPanel;
    private javax.swing.JPanel datosPersonalesRepresentanteJPanel;
    private javax.swing.JPanel datosSolicitudJPanel;
    private javax.swing.JScrollPane descEventoJScrollPane;
    private javax.swing.JTextArea descEventoJTArea;
    private javax.swing.JPanel editorMapaJPanel;
    private javax.swing.JButton eliminarJButton;
    private javax.swing.JLabel emailPropietarioJLabel;
    private javax.swing.JTextField emailPropietarioJTField;
    private javax.swing.JLabel emailRepresentanteJLabel;
    private javax.swing.JTextField emailRepresentanteJTField;
    private javax.swing.JPanel emplazamientoJPanel;
    private javax.swing.JLabel escaleraPropietarioJLabel;
    private javax.swing.JTextField escaleraPropietarioJTField;
    private javax.swing.JLabel escaleraRepresentanteJLabel;
    private javax.swing.JTextField escaleraRepresentanteJTField;
    private javax.swing.JLabel estadoExpedienteJLabel;
    private javax.swing.JPanel eventosJPanel;
    private javax.swing.JScrollPane eventosJScrollPane;
    private javax.swing.JTable eventosJTable;
    private javax.swing.JPanel expedienteJPanel;
    private javax.swing.JLabel faxPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxPropietarioJTField;
    private javax.swing.JLabel faxRepresentanteJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxRepresentanteJTField;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JTextField fechaAperturaJTField;
    private javax.swing.JLabel fechaSolicitudJLabel;
    private javax.swing.JTextField fechaSolicitudJTField;
    private javax.swing.JLabel finalizaJLabel;
    private javax.swing.JCheckBox habilesJCheckBox;
    private javax.swing.JLabel habilesJLabel;
    private javax.swing.JPanel historicoJPanel;
    private javax.swing.JScrollPane historicoJScrollPane;
    private javax.swing.JTable historicoJTable;
    private javax.swing.JLabel horaJLabel;
    private javax.swing.JLabel inicioJLabel;
    private javax.swing.JTextField inicioJTField;
    private javax.swing.JLabel letraPropietarioJLabel;
    private javax.swing.JTextField letraPropietarioJTField;
    private javax.swing.JLabel letraRepresentanteJLabel;
    private javax.swing.JTextField letraRepresentanteJTField;
    private javax.swing.JScrollPane listaAnexosJScrollPane;
    private javax.swing.JTable listaAnexosJTable;
    private javax.swing.JLabel mesasJLabel;
    private javax.swing.JLabel metros2JLabel;
    private javax.swing.JButton modHistoricoJButton;
    private javax.swing.JLabel motivoJLabel;
    private javax.swing.JTextField motivoJTField;
    private javax.swing.JLabel movilPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilPropietarioJTField;
    private javax.swing.JLabel movilRepresentanteJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilRepresentanteJTField;
    private javax.swing.JTextField municipioJTField;
    private javax.swing.JLabel municipioPropietarioJLabel;
    private javax.swing.JTextField municipioPropietarioJTField;
    private javax.swing.JLabel municipioRepresentanteJLabel;
    private javax.swing.JTextField municipioRepresentanteJTField;
    private javax.swing.JCheckBox necesitaObraJCheckBox;
    private javax.swing.JLabel necesitaObraJLabel;
    private javax.swing.JLabel nombrePropietarioJLabel;
    private javax.swing.JTextField nombrePropietarioJTField;
    private javax.swing.JLabel nombreRepresentanteJLabel;
    private javax.swing.JTextField nombreRepresentanteJTField;
    private javax.swing.JButton nombreViaJButton;
    private javax.swing.JLabel nombreViaJLabel;
    private javax.swing.JLabel nombreViaPropietarioJLabel;
    private javax.swing.JTextField nombreViaPropietarioJTField;
    private javax.swing.JLabel nombreViaRepresentanteJLabel;
    private javax.swing.JTextField nombreViaRepresentanteJTField;
    private javax.swing.JLabel notaJLabel;
    private javax.swing.JPanel notificacionesJPanel;
    private javax.swing.JScrollPane notificacionesJScrollPane;
    private javax.swing.JTable notificacionesJTable;
    private javax.swing.JCheckBox notificarPropietarioJCheckBox;
    private javax.swing.JLabel notificarPropietarioJLabel;
    private javax.swing.JCheckBox notificarRepresentanteJCheckBox;
    private javax.swing.JLabel notificarRepresentanteJLabel;
    private javax.swing.JButton nuevoHistoricoJButton;
    private javax.swing.JLabel numDiasJLabel;
    private javax.swing.JTextField numDiasJTField;
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JLabel numExpedienteObraJLabel;
    private javax.swing.JTextField numExpedienteObraJTField;
    private javax.swing.JLabel numFoliosJLabel;
    private javax.swing.JTextField numFoliosJTField;
    private javax.swing.JLabel numSillasJLabel;
    private javax.swing.JLabel numeroViaJLabel;
    private javax.swing.JLabel numeroViaPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaPropietarioJTField;
    private javax.swing.JLabel numeroViaRepresentanteJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaRepresentanteJTField;
    private javax.swing.JLabel observacionesExpedienteJLabel;
    private javax.swing.JScrollPane observacionesExpedienteJScrollPane;
    private javax.swing.JTextArea observacionesExpedienteJTArea;
    private javax.swing.JLabel observacionesJLabel;
    private javax.swing.JScrollPane observacionesJScrollPane;
    private javax.swing.JTextArea observacionesJTArea;
    private javax.swing.JPanel ocupacionJPanel;
    private javax.swing.JTabbedPane ocupacionJTabbedPane;
    private javax.swing.JLabel plantaPropietarioJLabel;
    private javax.swing.JTextField plantaPropietarioJTField;
    private javax.swing.JLabel plantaRepresentanteJLabel;
    private javax.swing.JTextField plantaRepresentanteJTField;
    private javax.swing.JLabel plazoVencimientoJLabel;
    private javax.swing.JTextField plazoVencimientoJTField;
    private javax.swing.JLabel portalPropietarioJLabel;
    private javax.swing.JTextField portalPropietarioJTField;
    private javax.swing.JLabel portalRepresentanteJLabel;
    private javax.swing.JTextField portalRepresentanteJTField;
    private javax.swing.JPanel propietarioJPanel;
    private javax.swing.JLabel provinciaJLabel;
    private javax.swing.JTextField provinciaJTField;
    private javax.swing.JLabel provinciaPropietarioJLabel;
    private javax.swing.JTextField provinciaPropietarioJTField;
    private javax.swing.JLabel provinciaRepresentanteJLabel;
    private javax.swing.JTextField provinciaRepresentanteJTField;
    private javax.swing.JScrollPane referenciasCatastralesJScrollPane;
    private javax.swing.JTable referenciasCatastralesJTable;
    private javax.swing.JPanel representanteJPanel;
    private javax.swing.JLabel responsableJLabel;
    private javax.swing.JTextField responsableJTField;
    private javax.swing.JLabel servicioExpedienteJLabel;
    private javax.swing.JTextField servicioExpedienteJTField;
    private javax.swing.JCheckBox silencioJCheckBox;
    private javax.swing.JLabel silencioJLabel;
    private javax.swing.JButton tableToMapAllJButton;
    private javax.swing.JButton tableToMapOneJButton;
    private javax.swing.JLabel telefonoPropietarioJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoPropietarioJTField;
    private javax.swing.JLabel telefonoRepresentanteJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoRepresentanteJTField;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoOcupacionJLabel;
    private javax.swing.JLabel tipoViaPropietarioJLabel;
    private javax.swing.JLabel tipoViaRepresentanteJLabel;
    private javax.swing.JLabel tramitacionJLabel;
    private javax.swing.JLabel viaNotificacionPropietarioJLabel;
    private javax.swing.JLabel viaNotificacionRepresentanteJLabel;
    private javax.swing.JButton jButtonPlazoVen;
    private javax.swing.JButton jButtonGenerarFicha;
    private javax.swing.JButton deleteGeopistaFeatureJButton;
    private javax.swing.JLabel entregadaAJLabel;
    /** notificacion entregada a */
    private javax.swing.JButton okJButton;
    private com.geopista.app.utilidades.TextField entregadaATField;
    private javax.swing.JButton generarFichaHistoricoJButton;
    private javax.swing.JButton areaOcupacionJButton;
    private JPanelCallesAfectadas jPanelCallesAfectadas;
    private javax.swing.JLabel entregadaATextJLabel;
    private javax.swing.JButton alfrescoJButton;
    
    // End of variables declaration//GEN-END:variables

}
