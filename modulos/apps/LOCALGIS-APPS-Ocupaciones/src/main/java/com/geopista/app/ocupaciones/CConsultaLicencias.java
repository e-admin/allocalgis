/**
 * CConsultaLicencias.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.ocupaciones;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JCheckBox;
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
import com.geopista.app.ocupaciones.panel.CSearchJDialog;
import com.geopista.app.ocupaciones.panel.JPanelCallesAfectadas;
import com.geopista.app.printer.FichasDisponibles;
import com.geopista.app.printer.GeopistaPrintableOcupaciones;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.client.alfresco.servlet.AlfrescoDocumentClient;
import com.geopista.client.alfresco.ui.AlfrescoExplorer;
import com.geopista.client.alfresco.utils.implementations.LocalgisIntegrationManagerImpl;
import com.geopista.editor.GeopistaEditor;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
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
import com.geopista.protocol.ocupacion.CDatosOcupacion;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;
import com.vividsolutions.jump.feature.Feature;

/**
 * @author charo
 */
public class CConsultaLicencias extends javax.swing.JInternalFrame implements IMultilingue{
    private final String layerName="ocupaciones";
	private Vector _vNotificaciones = new Vector();
	private Vector _vEventos = new Vector();
	private Vector _vHistorico = new Vector();
    private Vector _vReferenciasCatastrales= new Vector();
    private boolean searchExpCanceled= false;
    
    private JFrame desktop;

	Logger logger = Logger.getLogger(CConsultaLicencias.class);

    /** Ordenacion de tablas */
    TableSorted notificacionesSorted= new TableSorted();
    TableSorted eventosSorted= new TableSorted();
    TableSorted historicoSorted= new TableSorted();
    int notificacionesHiddenCol= 5;
    int eventosHiddenCol= 5;
    int historicoHiddenCol= 4;
	private Hashtable _hAnexosSolicitud = new Hashtable();


	/**
	 * Creates new form CCreacionLicencias
	 */

	public CConsultaLicencias(JFrame desktop) {

		this.desktop = desktop;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);
		initComponents();
		configureComponents();
        setEnabledDatosExpediente(false);
        setEnabledDatosSolicitud(false);
        setEnabledDatosTitular(false);
        setEnabledDatosRepresentante(false);
        String mostrarMapa=System.getProperty("mostrarMapa");
		if(mostrarMapa==null || !mostrarMapa.equals("false")){
			CUtilidadesComponentes.showGeopistaEditor(desktop,editorMapaJPanel, CMainOcupaciones.geopistaEditor, false);
		}
	}
	
	private boolean configureComponents() {

		if (CMainOcupaciones.geopistaEditor==null) CMainOcupaciones.geopistaEditor = new GeopistaEditor("workbench-properties-simple.xml");

		String[] columnNamesAnexos = {CMainOcupaciones.literales.getString("CConsultaLicenciasForm.anexosJTable.column1"),
									  CMainOcupaciones.literales.getString("CConsultaLicenciasForm.anexosJTable.column2"),
									  CMainOcupaciones.literales.getString("CConsultaLicenciasForm.anexosJTable.column3"),
									  CMainOcupaciones.literales.getString("CConsultaLicenciasForm.anexosJTable.column4")};

		anexosJTableModel = new CSearchDialogTableModel(columnNamesAnexos);
		anexosJTable.setModel(anexosJTableModel);
		anexosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		anexosJTable.setCellSelectionEnabled(false);
		anexosJTable.setColumnSelectionAllowed(false);
		anexosJTable.setRowSelectionAllowed(true);
        anexosJTable.getTableHeader().setReorderingAllowed(false);

		anexosJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < anexosJTable.getColumnCount(); i++) {
			TableColumn column = anexosJTable.getColumnModel().getColumn(i);
			if (i == 2) {
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


		String[] columnNamesNotificaciones = {CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum1"),
											  CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum2"),
											  CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum3"),
											  CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum4"),
											  CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum5"),
                                              "HIDDEN"};

		notificacionesJTableModel = new CSearchDialogTableModel(columnNamesNotificaciones);
        notificacionesSorted= new TableSorted(notificacionesJTableModel);
        notificacionesSorted.setTableHeader(notificacionesJTable.getTableHeader());
		notificacionesJTable.setModel(notificacionesSorted);
		notificacionesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		notificacionesJTable.setCellSelectionEnabled(false);
		notificacionesJTable.setColumnSelectionAllowed(false);
		notificacionesJTable.setRowSelectionAllowed(true);
        notificacionesJTable.getTableHeader().setReorderingAllowed(false);

		notificacionesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < notificacionesJTable.getColumnCount(); i++) {
			TableColumn column = notificacionesJTable.getColumnModel().getColumn(i);
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



		String[] columnNamesEventos = {CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum1"),
									   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum2"),
									   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum3"),
									   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum4"),
									   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum5"),
                                       "HIDDEN"};

		_eventosTableModel = new CSearchDialogTableModel(columnNamesEventos);
        eventosSorted= new TableSorted(_eventosTableModel);
        eventosSorted.setTableHeader(eventosJTable.getTableHeader());
		eventosJTable.setModel(eventosSorted);
		eventosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		eventosJTable.setCellSelectionEnabled(false);
		eventosJTable.setColumnSelectionAllowed(false);
		eventosJTable.setRowSelectionAllowed(true);
        eventosJTable.getTableHeader().setReorderingAllowed(false);

		eventosJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < eventosJTable.getColumnCount(); i++) {
			TableColumn column = eventosJTable.getColumnModel().getColumn(i);
			if (i == 2) {
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



		String[] columnNamesHistorico = {//CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJTable.colum1"),
										 CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJTable.colum2"),
										 CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJTable.colum3"),
										 CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJTable.colum4"),
										 CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJTable.colum5"),
                                         "HIDDEN"};

		_historicoTableModel = new CSearchDialogTableModel(columnNamesHistorico);
        historicoSorted= new TableSorted(_historicoTableModel);
        historicoSorted.setTableHeader(historicoJTable.getTableHeader());
		historicoJTable.setModel(historicoSorted);
		historicoJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		historicoJTable.setCellSelectionEnabled(false);
		historicoJTable.setColumnSelectionAllowed(false);
		historicoJTable.setRowSelectionAllowed(true);
        historicoJTable.getTableHeader().setReorderingAllowed(false);

		historicoJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < historicoJTable.getColumnCount(); i++) {
			TableColumn column = historicoJTable.getColumnModel().getColumn(i);
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


		String[] columnNamesRef = {CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum1"),
                                   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum10"),
								   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum2"),
								   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum3"),
								   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum4"),
                                   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum5"),
                                   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum6"),
                                   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum7"),
                                   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum8"),
                                   CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum9")};

    	referenciasCatastralesJTableModel = new CSearchDialogTableModel(columnNamesRef);
    	referenciasCatastralesJTable.setModel(referenciasCatastralesJTableModel);
		referenciasCatastralesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		referenciasCatastralesJTable.setCellSelectionEnabled(false);
		referenciasCatastralesJTable.setColumnSelectionAllowed(false);
		referenciasCatastralesJTable.setRowSelectionAllowed(true);
        referenciasCatastralesJTable.getTableHeader().setReorderingAllowed(false);

		referenciasCatastralesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		for (int i = 0; i < referenciasCatastralesJTable.getColumnCount(); i++) {
			TableColumn column = referenciasCatastralesJTable.getColumnModel().getColumn(i);

            if (i==2){
                column.setMinWidth(75);
                column.setMaxWidth(150);
                column.setWidth(75);
                column.setPreferredWidth(75);
            }else if ((i>3) || (i==1)){
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

        buscarExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);

		habilesJCheckBox.setVisible(false);
		habilesJLabel.setVisible(false);

		renombrarComponentes(true);

		return true;
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
        botoneraJPanel = new javax.swing.JPanel();
        salirJButton = new javax.swing.JButton();
        jButtonGenerarFicha= new javax.swing.JButton();
        modificarJButton = new javax.swing.JButton();
        
        publicarJButton = new javax.swing.JButton();
        
        datosSolicitudJTabbedPane = new javax.swing.JTabbedPane();
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
        estadoExpedienteJTextField = new javax.swing.JTextField();
        silencioJLabel = new javax.swing.JLabel();
        habilesJLabel = new javax.swing.JLabel();
        notaJLabel = new javax.swing.JLabel();
        observacionesExpedienteJScrollPane = new javax.swing.JScrollPane();
        observacionesExpedienteJTArea = new javax.swing.JTextArea();
        tramitacionJTextField = new javax.swing.JTextField();
        finalizaPorJTextField = new javax.swing.JTextField();
        solicitudJPanel = new javax.swing.JPanel();
        datosSolicitudJPanel = new javax.swing.JPanel();
        estadoJLabel = new javax.swing.JLabel();
        tipoObraJLabel = new javax.swing.JLabel();
        necesitaObraJLabel = new javax.swing.JLabel();
        expedienteObraJLabel = new javax.swing.JLabel();
        motivoJLabel = new javax.swing.JLabel();
        asuntoJLabel = new javax.swing.JLabel();
        fechaSolicitudJLabel = new javax.swing.JLabel();
        observacionesJLabel = new javax.swing.JLabel();
        numExpedienteJTextField = new javax.swing.JTextField();
        motivoJTField = new javax.swing.JTextField();
        asuntoJTField = new javax.swing.JTextField();
        fechaSolicitudJTField = new javax.swing.JTextField();
        observacionesJScrollPane = new javax.swing.JScrollPane();
        observacionesJTArea = new javax.swing.JTextArea();
        estadoJTextField = new javax.swing.JTextField();
        tipoObraJTextField = new javax.swing.JTextField();
        necesitaObraJCheckBox = new javax.swing.JCheckBox();
        areaOcupacionJLabel = new javax.swing.JLabel();
        areaOcupacionJTextField = new javax.swing.JTextField();
        numMesasJLabel = new javax.swing.JLabel();
        numMesasJTextField = new javax.swing.JTextField();
        numSillasJLabel = new javax.swing.JLabel();
        horaInicioJTextField = new javax.swing.JTextField();
        horaJLabel = new javax.swing.JLabel();
        horaFinJTextField = new javax.swing.JTextField();
        numSillasJTextField = new javax.swing.JTextField();
        afectaAparcamientoJCheckBox = new javax.swing.JCheckBox();
        afectaCalzadaJCheckBox = new javax.swing.JCheckBox();
        afectaAceraJCheckBox = new javax.swing.JCheckBox();
        afectaJLabel = new javax.swing.JLabel();
        metros2JLabel = new javax.swing.JLabel();
        metros2aceraJTextField = new javax.swing.JTextField();
        metros2calzadaJTextField = new javax.swing.JTextField();
        metros2aparcamientoJTextField = new javax.swing.JTextField();
        emplazamientoJPanel = new javax.swing.JPanel();
        nombreViaJLabel = new javax.swing.JLabel();
        tipoViaJTextField = new javax.swing.JTextField();
        nombreViaJTextField = new javax.swing.JTextField();
        numeroViaJLabel = new javax.swing.JLabel();
        numeroViaJTextField = new javax.swing.JTextField();
        portalJTextField = new javax.swing.JTextField();
        plantaJTextField = new javax.swing.JTextField();
        letraJTextField = new javax.swing.JTextField();
        cPostalJLabel = new javax.swing.JLabel();
        cPostalJTextField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioJTextField = new javax.swing.JTextField();
        provinciaJLabel = new javax.swing.JLabel();
        provinciaJTextField = new javax.swing.JTextField();
        referenciasCatastralesJScrollPane = new javax.swing.JScrollPane();
        referenciasCatastralesJTable = new javax.swing.JTable();
        anexosJPanel = new javax.swing.JPanel();
        abrirJButton = new javax.swing.JButton();
        anexosJScrollPane = new javax.swing.JScrollPane();
        anexosJTable = new javax.swing.JTable();
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
        faxTitularJTField = new javax.swing.JTextField();
        telefonoTitularJTField = new javax.swing.JTextField();
        movilTitularJTField = new javax.swing.JTextField();
        emailTitularJTField = new javax.swing.JTextField();
        nombreViaTitularJTField = new javax.swing.JTextField();
        numeroViaTitularJTField = new javax.swing.JTextField();
        plantaTitularJTField = new javax.swing.JTextField();
        portalTitularJTField = new javax.swing.JTextField();
        escaleraTitularJTField = new javax.swing.JTextField();
        letraTitularJTField = new javax.swing.JTextField();
        cPostalTitularJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioTitularJTField = new javax.swing.JTextField();
        provinciaTitularJTField = new javax.swing.JTextField();
        notificarTitularJCheckBox = new javax.swing.JCheckBox();
        notificarTitularJLabel = new javax.swing.JLabel();
        viaNotificacionTitularJTField = new javax.swing.JTextField();
        tipoViaNotificacionTitularJTField = new javax.swing.JTextField();
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
        faxRepresentaAJTField = new javax.swing.JTextField();
        telefonoRepresentaAJTField = new javax.swing.JTextField();
        movilRepresentaAJTField = new javax.swing.JTextField();
        emailRepresentaAJTField = new javax.swing.JTextField();
        nombreViaRepresentaAJTField = new javax.swing.JTextField();
        numeroViaRepresentaAJTField = new javax.swing.JTextField();
        plantaRepresentaAJTField = new javax.swing.JTextField();
        portalRepresentaAJTField = new javax.swing.JTextField();
        escaleraRepresentaAJTField = new javax.swing.JTextField();
        letraRepresentaAJTField = new javax.swing.JTextField();
        cPostalRepresentaAJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioRepresentaAJTField = new javax.swing.JTextField();
        provinciaRepresentaAJTField = new javax.swing.JTextField();
        notificarRepresentaAJCheckBox = new javax.swing.JCheckBox();
        notificarRepresentaAJLabel = new javax.swing.JLabel();
        viaNotificacionRepresentaAJTField = new javax.swing.JTextField();
        tipoViaNotificacionRepresentaAJTField = new javax.swing.JTextField();
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
        editorMapaJPanel = new javax.swing.JPanel();
        tableToMapOneJButton = new javax.swing.JButton();
        tableToMapAllJButton = new javax.swing.JButton();
        entregadaAJLabel = new javax.swing.JLabel();
        entregadaAJTField = new javax.swing.JTextField();
        generarFichaJButton = new javax.swing.JButton();
        jPanelCallesAfectadas= new JPanelCallesAfectadas(desktop,CMainOcupaciones.literales);
        alfrescoJButton = new javax.swing.JButton();
        
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        setClosable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed();
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        //templateJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
       //botoneraJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        templateJPanel.setLayout(new BorderLayout());
        botoneraJPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirJButtonActionPerformed();
            }
        });

         modificarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarJButtonActionPerformed();
            }
        });

        publicarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(publicarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 90, -1));

        publicarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publicarJButtonActionPerformed();
            }
        });

        publicarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(publicarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 90, -1));

        
        jButtonGenerarFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarFicha();
            }
        });
        jButtonGenerarFicha.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(jButtonGenerarFicha, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 90, -1));
        salirJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 90, -1));

        //templateJPanel.add(botoneraJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 990, 40));
        templateJPanel.add(botoneraJPanel, BorderLayout.SOUTH);

        datosSolicitudJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        datosSolicitudJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));
        expedienteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expedienteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Expediente"));
        expedienteJPanel.setAutoscrolls(true);
        expedienteJPanel.add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 130, 20));

        expedienteJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));

        expedienteJPanel.add(servicioExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 130, 20));

        expedienteJPanel.add(tramitacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 130, 20));

        expedienteJPanel.add(finalizaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 130, 20));

        expedienteJPanel.add(asuntoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 185, 130, 20));

        expedienteJPanel.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 130, 20));

        expedienteJPanel.add(observacionesExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 130, 20));

        servicioExpedienteJTField.setEditable(true);
        expedienteJPanel.add(servicioExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 135, 300, -1));

        expedienteJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 280, -1));

        asuntoExpedienteJTField.setEditable(true);

        expedienteJPanel.add(asuntoExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 185, 300, -1));

        fechaAperturaJTField.setEditable(true);
        expedienteJPanel.add(fechaAperturaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 210, 300, -1));

        expedienteJPanel.add(inicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 235, 130, 20));

        inicioJTField.setEditable(true);
        expedienteJPanel.add(inicioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 235, 300, -1));

        expedienteJPanel.add(numFoliosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 285, 130, 20));

        numFoliosJTField.setEditable(true);
        expedienteJPanel.add(numFoliosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 285, 300, -1));

        expedienteJPanel.add(responsableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 130, 20));

        responsableJTField.setEditable(true);
        expedienteJPanel.add(responsableJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 310, 300, -1));

        expedienteJPanel.add(plazoVencimientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 335, 130, 20));

        numDiasJTField.setEditable(true);
        expedienteJPanel.add(numDiasJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 360, 300, -1));

        expedienteJPanel.add(numDiasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 130, 20));

        habilesJCheckBox.setEnabled(false);
        expedienteJPanel.add(habilesJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 340, -1, -1));

        silencioJCheckBox.setEnabled(false);
        expedienteJPanel.add(silencioJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 260, -1, -1));

        plazoVencimientoJTField.setEditable(true);
        expedienteJPanel.add(plazoVencimientoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 335, 300, -1));

        buscarExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);
        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        tableToMapAllJButton.setIcon(CUtilidadesComponentes.iconoDobleFlecha);
        tableToMapAllJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableToMapAllJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        tableToMapAllJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableToMapAllJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(tableToMapAllJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 135, 20, 20));

        expedienteJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 20, 20));

        consultarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarJButtonActionPerformed();
            }
        });

        expedienteJPanel.add(consultarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 90, -1));

        estadoExpedienteJTextField.setEditable(true);
        expedienteJPanel.add(estadoExpedienteJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 300, -1));

        expedienteJPanel.add(silencioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 160, 20));

        expedienteJPanel.add(habilesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 130, 20));

        notaJLabel.setFont(new java.awt.Font("Arial", 0, 10));
        expedienteJPanel.add(notaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 260, 270, 20));

        observacionesExpedienteJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        observacionesExpedienteJTArea.setEditable(true);
        observacionesExpedienteJTArea.setLineWrap(true);
        observacionesExpedienteJTArea.setRows(3);
        observacionesExpedienteJTArea.setTabSize(4);
        observacionesExpedienteJTArea.setWrapStyleWord(true);
        observacionesExpedienteJTArea.setBorder(null);
        observacionesExpedienteJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        observacionesExpedienteJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        observacionesExpedienteJScrollPane.setViewportView(observacionesExpedienteJTArea);

        expedienteJPanel.add(observacionesExpedienteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 440, 300, 90));

        tramitacionJTextField.setEditable(true);
        expedienteJPanel.add(tramitacionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 300, -1));

        finalizaPorJTextField.setEditable(true);
        expedienteJPanel.add(finalizaPorJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 410, 300, -1));


        solicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder("Datos solicitud"));
        datosSolicitudJPanel.setAutoscrolls(true);
        datosSolicitudJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 90, 20));
        datosSolicitudJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, 20));
        datosSolicitudJPanel.add(necesitaObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 90, 20));

        expedienteObraJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(expedienteObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 100, 20));

        datosSolicitudJPanel.add(motivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 150, 20));

        asuntoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(asuntoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 60, 20));

        fechaSolicitudJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(fechaSolicitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 80, 20));

        datosSolicitudJPanel.add(observacionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 150, 20));

        numExpedienteJTextField.setEditable(true);
        datosSolicitudJPanel.add(numExpedienteJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 120, -1));

        motivoJTField.setEditable(true);
        datosSolicitudJPanel.add(motivoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 150, -1));

        asuntoJTField.setEditable(true);
        datosSolicitudJPanel.add(asuntoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 80, 120, -1));

        fechaSolicitudJTField.setEditable(true);
        datosSolicitudJPanel.add(fechaSolicitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 120, -1));

        observacionesJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        observacionesJTArea.setEditable(true);
        observacionesJTArea.setLineWrap(true);
        observacionesJTArea.setRows(3);
        observacionesJTArea.setTabSize(4);
        observacionesJTArea.setWrapStyleWord(true);
        observacionesJTArea.setBorder(null);
        observacionesJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        observacionesJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        observacionesJScrollPane.setViewportView(observacionesJTArea);

        datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 185, 380, 40));

        estadoJTextField.setEditable(true);
        datosSolicitudJPanel.add(estadoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 150, -1));

        tipoObraJTextField.setEditable(true);
        datosSolicitudJPanel.add(tipoObraJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 380, -1));

        necesitaObraJCheckBox.setEnabled(false);
        datosSolicitudJPanel.add(necesitaObraJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 30, 20));

        datosSolicitudJPanel.add(areaOcupacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 100, 20));

        areaOcupacionJTextField.setEditable(true);
        datosSolicitudJPanel.add(areaOcupacionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 150, -1));

        datosSolicitudJPanel.add(numMesasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 20));

        numMesasJTextField.setEditable(true);
        datosSolicitudJPanel.add(numMesasJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 150, -1));

        numSillasJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(numSillasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, 90, 20));

        horaInicioJTextField.setEditable(true);
        datosSolicitudJPanel.add(horaInicioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 50, -1));

        horaJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(horaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, 90, 20));

        horaFinJTextField.setEditable(true);
        datosSolicitudJPanel.add(horaFinJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 100, 50, -1));

        numSillasJTextField.setEditable(true);
        datosSolicitudJPanel.add(numSillasJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 120, 120, -1));

        afectaAparcamientoJCheckBox.setSelected(false);
        afectaAparcamientoJCheckBox.setEnabled(false);
        datosSolicitudJPanel.add(afectaAparcamientoJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 140, 100, 20));

        afectaCalzadaJCheckBox.setSelected(false);
        afectaCalzadaJCheckBox.setEnabled(false);
        datosSolicitudJPanel.add(afectaCalzadaJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 140, 100, 20));

        afectaAceraJCheckBox.setSelected(false);
        afectaAceraJCheckBox.setEnabled(false);
        datosSolicitudJPanel.add(afectaAceraJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 120, 20));

        datosSolicitudJPanel.add(afectaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 100, 20));

        datosSolicitudJPanel.add(metros2JLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 100, 20));

        metros2aceraJTextField.setEditable(true);
        datosSolicitudJPanel.add(metros2aceraJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 100, -1));

        metros2calzadaJTextField.setEditable(true);
        datosSolicitudJPanel.add(metros2calzadaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 100, -1));

        metros2aparcamientoJTextField.setEditable(true);
        datosSolicitudJPanel.add(metros2aparcamientoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 160, 100, -1));

        solicitudJPanel.add(datosSolicitudJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 235));
        emplazamientoJPanel.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, 20));

        tipoViaJTextField.setEditable(true);
        emplazamientoJPanel.add(tipoViaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 110, -1));

        nombreViaJTextField.setEditable(true);
        emplazamientoJPanel.add(nombreViaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 190, -1));
        emplazamientoJPanel.add(numeroViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 130, 20));

        numeroViaJTextField.setEditable(true);
        emplazamientoJPanel.add(numeroViaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 70, -1));

        portalJTextField.setEditable(true);
        emplazamientoJPanel.add(portalJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 70, -1));

        plantaJTextField.setEditable(true);
        emplazamientoJPanel.add(plantaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, 70, -1));

        letraJTextField.setEditable(true);
        emplazamientoJPanel.add(letraJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 70, -1));
        emplazamientoJPanel.add(cPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 130, 20));

        cPostalJTextField.setEditable(true);
        emplazamientoJPanel.add(cPostalJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 70, -1));

        municipioJTextField.setEditable(true);
        emplazamientoJPanel.add(municipioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 230, -1));
        emplazamientoJPanel.add(provinciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 130, 20));

        provinciaJTextField.setEditable(true);
        emplazamientoJPanel.add(provinciaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 310, -1));
        referenciasCatastralesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        referenciasCatastralesJScrollPane.setViewportView(referenciasCatastralesJTable);

        emplazamientoJPanel.add(referenciasCatastralesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 450, 70));

        tableToMapOneJButton.setIcon(CUtilidadesComponentes.iconoFlecha);

        tableToMapOneJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableToMapOneJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        tableToMapOneJButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        tableToMapOneJButtonActionPerformed();
                    }
                });

        emplazamientoJPanel.add(tableToMapOneJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, 20, 20));

        solicitudJPanel.add(emplazamientoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 235, 500, 190));

        anexosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        abrirJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoAbrir);
        abrirJButton.setText("");
        abrirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirJButtonActionPerformed();
            }
        });

        anexosJPanel.add(abrirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 20, 20));
        anexosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        anexosJScrollPane.setViewportView(anexosJTable);

        anexosJPanel.add(anexosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 450, 90));

        solicitudJPanel.add(anexosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 425, 500, 120));

        titularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTitularJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesTitularJPanel.add(DNITitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));

        DNITitularJTField.setEditable(true);
        datosPersonalesTitularJPanel.add(DNITitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 300, -1));

        datosPersonalesTitularJPanel.add(nombreTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, 20));

        nombreTitularJTField.setEditable(true);
        datosPersonalesTitularJPanel.add(nombreTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 300, -1));

        datosPersonalesTitularJPanel.add(apellido1TitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 81, 130, 20));
        datosPersonalesTitularJPanel.add(apellido2TitularJLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 102, 130, 20));

        apellido1TitularJTField.setEditable(true);
        datosPersonalesTitularJPanel.add(apellido1TitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 81, 300, -1));

        apellido2TitularJTField.setEditable(true);
        datosPersonalesTitularJPanel.add(apellido2TitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 102, 300, -1));

        titularJPanel.add(datosPersonalesTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 140));

        datosNotificacionTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionTitularJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        datosNotificacionTitularJPanel.add(viaNotificacionTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));

        datosNotificacionTitularJPanel.add(faxTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 130, 20));

        datosNotificacionTitularJPanel.add(telefonoTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 130, 20));

        datosNotificacionTitularJPanel.add(movilTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 130, 20));

        datosNotificacionTitularJPanel.add(emailTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 130, 20));

        datosNotificacionTitularJPanel.add(tipoViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 130, 20));

        datosNotificacionTitularJPanel.add(nombreViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 156, 130, 20));

        datosNotificacionTitularJPanel.add(numeroViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 90, 20));

        datosNotificacionTitularJPanel.add(portalTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 243, 50, 20));

        datosNotificacionTitularJPanel.add(plantaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 201, 70, 20));

        datosNotificacionTitularJPanel.add(escaleraTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 264, 60, 20));

        datosNotificacionTitularJPanel.add(letraTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 222, 40, 20));

        datosNotificacionTitularJPanel.add(cPostalTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 130, 20));

        datosNotificacionTitularJPanel.add(municipioTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 311, 130, 20));

        datosNotificacionTitularJPanel.add(provinciaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 332, 130, 20));

        faxTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(faxTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 51, 300, -1));

        telefonoTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(telefonoTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 72, 300, -1));

        movilTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(movilTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 93, 300, -1));

        emailTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(emailTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 114, 300, -1));

        nombreViaTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(nombreViaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 156, 300, -1));

        numeroViaTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(numeroViaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 150, -1));

        plantaTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(plantaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 201, 150, -1));

        portalTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(portalTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 243, 150, -1));

        escaleraTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(escaleraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 264, 150, -1));

        letraTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(letraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 222, 150, -1));

        cPostalTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(cPostalTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 300, -1));

        municipioTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(municipioTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 311, 300, -1));

        provinciaTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(provinciaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 332, 300, -1));

        notificarTitularJCheckBox.setEnabled(false);
        datosNotificacionTitularJPanel.add(notificarTitularJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 360, -1, -1));

        datosNotificacionTitularJPanel.add(notificarTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 120, 20));

        viaNotificacionTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(viaNotificacionTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 300, -1));

        tipoViaNotificacionTitularJTField.setEditable(true);
        datosNotificacionTitularJPanel.add(tipoViaNotificacionTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 135, 300, -1));

        titularJPanel.add(datosNotificacionTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 145, 500, 400));

        representaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesRepresentaAJPanel.add(DNIRepresenaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));

        DNIRepresentaAJTField.setEditable(true);
        datosPersonalesRepresentaAJPanel.add(DNIRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 300, -1));

        datosPersonalesRepresentaAJPanel.add(nombreRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, 20));

        nombreRepresentaAJTField.setEditable(true);
        datosPersonalesRepresentaAJPanel.add(nombreRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 300, -1));

        datosPersonalesRepresentaAJPanel.add(apellido1RepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 81, 130, 20));

        datosPersonalesRepresentaAJPanel.add(apellido2RepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 102, 130, 20));

        apellido1RepresentaAJTField.setEditable(true);
        datosPersonalesRepresentaAJPanel.add(apellido1RepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 81, 300, -1));

        apellido2RepresentaAJTField.setEditable(true);
        datosPersonalesRepresentaAJPanel.add(apellido2RepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 102, 300, -1));

        representaAJPanel.add(datosPersonalesRepresentaAJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 140));

        datosNotificacionRepresentaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionRepresentaAJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionRepresentaAJPanel.add(viaNotificacionRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));
        datosNotificacionRepresentaAJPanel.add(faxRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 130, 20));
        datosNotificacionRepresentaAJPanel.add(telefonoRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 130, 20));
        datosNotificacionRepresentaAJPanel.add(movilRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 130, 20));
        datosNotificacionRepresentaAJPanel.add(emailRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 130, 20));
        datosNotificacionRepresentaAJPanel.add(tipoViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 130, 20));
        datosNotificacionRepresentaAJPanel.add(nombreViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 156, 130, 20));
        datosNotificacionRepresentaAJPanel.add(numeroViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 90, 20));
        datosNotificacionRepresentaAJPanel.add(portalRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 243, 50, 20));
        datosNotificacionRepresentaAJPanel.add(plantaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 201, 70, 20));
        datosNotificacionRepresentaAJPanel.add(escaleraRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 264, 60, 20));
        datosNotificacionRepresentaAJPanel.add(letraRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 222, 40, 20));
        datosNotificacionRepresentaAJPanel.add(cPostalRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 130, 20));
        datosNotificacionRepresentaAJPanel.add(municipioRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 311, 130, 20));
        datosNotificacionRepresentaAJPanel.add(provinciaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 332, 130, 20));

        faxRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(faxRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 51, 300, -1));

        telefonoRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(telefonoRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 72, 300, -1));

        movilRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(movilRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 93, 300, -1));

        emailRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(emailRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 114, 300, -1));

        nombreViaRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(nombreViaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 156, 300, -1));

        numeroViaRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(numeroViaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 150, -1));

        plantaRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(plantaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 201, 150, -1));

        portalRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(portalRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 243, 150, -1));

        escaleraRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(escaleraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 264, 150, -1));

        letraRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(letraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 222, 150, -1));

        cPostalRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(cPostalRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 300, -1));

        municipioRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(municipioRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 311, 300, -1));

        provinciaRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(provinciaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 332, 300, -1));

        notificarRepresentaAJCheckBox.setEnabled(false);
        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 360, -1, -1));

        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 130, 20));

        viaNotificacionRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(viaNotificacionRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 300, -1));

        tipoViaNotificacionRepresentaAJTField.setEditable(true);
        datosNotificacionRepresentaAJPanel.add(tipoViaNotificacionRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 135, 300, -1));

        representaAJPanel.add(datosNotificacionRepresentaAJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 145, 500, 400));

        notificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder("Notificaciones"));
        notificacionesJScrollPane.setEnabled(false);

        notificacionesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
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

        datosNotificacionesJPanel.add(notificacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 480, 320));


        datosNotificacionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder("Datos de Notificaci\u00f3n de la Notificaci\u00f3n Seleccionada"));
        datosNotificacionJPanel.add(datosNombreApellidosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 130, 20));

        datosNotificacionJPanel.add(datosDireccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, 20));

        datosNotificacionJPanel.add(datosCPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 20));

        datosNotificacionJPanel.add(datosNotificarPorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 130, 20));

        datosNombreApellidosJTField.setEditable(false);
        datosNombreApellidosJTField.setBorder(null);
        datosNotificacionJPanel.add(datosNombreApellidosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 250, 20));

        datosCPostalJTField.setEditable(false);
        datosCPostalJTField.setBorder(null);
        datosNotificacionJPanel.add(datosCPostalJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 250, 20));

        datosDireccionJTField.setEditable(false);
        datosDireccionJTField.setBorder(null);
        datosNotificacionJPanel.add(datosDireccionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 250, 20));

        datosNotificarPorJTField.setEditable(false);
        datosNotificarPorJTField.setBorder(null);
        datosNotificacionJPanel.add(datosNotificarPorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 250, 20));

        datosNotificacionJPanel.add(entregadaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 180, 20));

        entregadaAJTField.setEditable(false);
        entregadaAJTField.setBorder(null);
        datosNotificacionJPanel.add(entregadaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 250, 20));

        datosNotificacionesJPanel.add(datosNotificacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 480, 190));
        notificacionesJPanel.add(datosNotificacionesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 578));

        try
        {
            ClassLoader cl =this.getClass().getClassLoader();
            Icon icon= new javax.swing.ImageIcon(cl.getResource("img/notificacion.jpg"));
            datosSolicitudJTabbedPane.addTab("Notificaciones",icon, notificacionesJPanel);
        }catch(Exception e)
        {
            datosSolicitudJTabbedPane.addTab("Notificaciones", notificacionesJPanel);
        }

        eventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosEventosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosEventosJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJPanel.TitleTab")));
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

        datosEventosJPanel.add(eventosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 480, 430));

        descEventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.descEventoJScrollPane.TitleBorder")));//"Descripci\u00f3n  del Evento Seleccionado"));
        descEventoJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        descEventoJTArea.setEditable(true);
        descEventoJTArea.setLineWrap(true);
        descEventoJTArea.setRows(3);
        descEventoJTArea.setTabSize(4);
        descEventoJTArea.setWrapStyleWord(true);
        descEventoJTArea.setBorder(null);
        descEventoJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        descEventoJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        descEventoJScrollPane.setViewportView(descEventoJTArea);

        datosEventosJPanel.add(descEventoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 480, 90));

        eventosJPanel.add(datosEventosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 578));

        datosSolicitudJTabbedPane.addTab("Eventos", eventosJPanel);

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

        apunteJScrollPane.setBorder(new javax.swing.border.TitledBorder("Apunte del Hist\u00f3rico Seleccionado"));
        apunteJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        apunteJTArea.setEditable(true);
        apunteJTArea.setLineWrap(true);
        apunteJTArea.setRows(3);
        apunteJTArea.setTabSize(4);
        apunteJTArea.setWrapStyleWord(true);
        apunteJTArea.setBorder(null);
        apunteJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        apunteJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        apunteJScrollPane.setViewportView(apunteJTArea);

        //datosHistoricoJPanel.add(apunteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 480, 90));
        datosHistoricoJPanel.add(apunteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 460, 90));

        //historicoJPanel.add(datosHistoricoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 550));
        historicoJPanel.add(datosHistoricoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 578));

        generarFichaJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoGenerarFicha);
        generarFichaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        generarFichaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        generarFichaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarFichaJButtonActionPerformed();
            }
        });

        historicoJPanel.add(generarFichaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 20, 20));

        datosSolicitudJTabbedPane.addTab("Hist\u00f3rico", historicoJPanel);

        
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
	        
	        solicitudJPanel.add(alfrescoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 546, 150, 25));
        }
        
        templateJPanel.add(datosSolicitudJTabbedPane, BorderLayout.WEST);

        editorMapaJPanel.setLayout(new java.awt.GridLayout(1, 0));

        editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder("Mapa"));
        //templateJPanel.add(editorMapaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, 480, 560));
        templateJPanel.add(editorMapaJPanel, BorderLayout.CENTER);
        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane);

    }//GEN-END:initComponents

    private void generarFichaJButtonActionPerformed() {//GEN-FIRST:event_generarFichaJButtonActionPerformed
        if ((_vHistorico == null) || (expedienteLicencia == null) || (solicitudLicencia == null))  return;

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
            CUtilidadesComponentes.generarFichaHistorico(this.desktop, _vHistorico, expedienteLicencia, solicitudLicencia, selectedFile,CMainOcupaciones.literales);
        }

    }//GEN-LAST:event_generarFichaJButtonActionPerformed



	private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);
	}//GEN-LAST:event_formInternalFrameClosed

	private void historicoJTableMouseClicked() {//GEN-FIRST:event_historicoJTableMouseClicked
        mostrarHistoricoSeleccionado();
    }//GEN-LAST:event_historicoJTableMouseClicked

    private void historicoJTableFocusGained() {//GEN-FIRST:event_resultadosJTableFocusGained
        mostrarHistoricoSeleccionado();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void historicoJTableMouseDragged() {//GEN-FIRST:event_resultadosJTableMouseDragged
        mostrarHistoricoSeleccionado();
    }//GEN-LAST:event_resultadosJTableMouseDragged

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
            CHistorico historico = (CHistorico) _vHistorico.get(((Integer)historicoSorted.getValueAt(row, historicoHiddenCol)).intValue());
            apunteJTArea.setText(historico.getApunte());
        }
    }


	private void eventosJTableMouseClicked() {//GEN-FIRST:event_eventosJTableMouseClicked
		mostrarEventoSeleccionado();
    }//GEN-LAST:event_eventosJTableMouseClicked

    private void eventosJTableFocusGained() {//GEN-FIRST:event_resultadosJTableFocusGained
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void eventosJTableMouseDragged() {//GEN-FIRST:event_resultadosJTableMouseDragged
        mostrarEventoSeleccionado();
    }//GEN-LAST:event_resultadosJTableMouseDragged

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


	private void notificacionesJTableMouseClicked() {//GEN-FIRST:event_notificacionesJTableMouseClicked
	    mostrarNotificacionSeleccionada();
	}//GEN-LAST:event_notificacionesJTableMouseClicked

    private void notificacionesJTableFocusGained() {//GEN-FIRST:event_resultadosJTableFocusGained
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void notificacionesJTableMouseDragged() {//GEN-FIRST:event_resultadosJTableMouseDragged
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_resultadosJTableMouseDragged

    private void notificacionesJTableKeyTyped(){
       mostrarNotificacionSeleccionada();
    }
    public void notificacionesJTableKeyPressed() {
        mostrarNotificacionSeleccionada();
    }
    public void notificacionesJTableKeyReleased() {
        mostrarNotificacionSeleccionada();
    }

    private void mostrarNotificacionSeleccionada(){
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
            datosNotificarPorJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(datos.getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()));

            if (notificacion.getEntregadaA() != null)
                entregadaAJTField.setText(notificacion.getEntregadaA());
            else entregadaAJTField.setText("");


        }
    }


    private void generarFicha()
    {
    	try
        {
            if (expedienteLicencia == null)
            {
                mostrarMensaje(CMainOcupaciones.literales.getString("generarFicha.mensaje1"));
                return;
            }
            expedienteLicencia.setEstructuraEstado(Estructuras.getListaEstadosOcupacion());
            expedienteLicencia.setEstructuraTipoOcupacion(Estructuras.getListaTipoOcupacion());
            expedienteLicencia.setLocale(CMainOcupaciones.currentLocale);
            expedienteLicencia.setSolicitud(solicitudLicencia);
           	new GeopistaPrintableOcupaciones().printObjeto(FichasDisponibles.fichalicenciaocupacion, expedienteLicencia , CExpedienteLicencia.class, CMainOcupaciones.geopistaEditor.getLayerViewPanel(), GeopistaPrintableOcupaciones.FICHA_OCUPACIONES_CONSULTA);
		} catch (Exception ex) {
			logger.error("Exception al mostrar las features: " ,ex);
		}
    }
	private void modificarJButtonActionPerformed()
    {
        if ((numExpedienteJTField.getText() == null) || (numExpedienteJTField.getText().trim().equals("")))
        {
			mostrarMensaje(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.mensaje1"));
			return;
		}

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         CConstantesOcupaciones.helpSetHomeID = "ocupacionesModificacion";

        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

        /** Comprobamos si el expediente esta bloqueado */
        if (CUtilidadesComponentes.expedienteBloqueado(numExpedienteJTField.getText().trim(), CMainOcupaciones.currentLocale)){
            if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, true) != 0){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }
        }
        dispose();

        CMainOcupaciones mainOcupaciones = (CMainOcupaciones)desktop;
        mainOcupaciones.mostrarJInternalFrame(new com.geopista.app.ocupaciones.CModificacionLicencias((JFrame)this.desktop, numExpedienteJTField.getText().trim(), false));

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_modificarJButtonActionPerformed

	private void publicarJButtonActionPerformed(){
		
		if (expedienteLicencia == null)
        {
            //mostrarMensaje("Es necesario consultar primero por un expediente.");
			mostrarMensaje(CMainOcupaciones.literales.getString("CActualizarIdSigemLicenciasForm.ConsultarExpediente"));
            return;
        }
		
		CResultadoOperacion primerResultado = COperacionesLicencias.obtenerIdSigem(expedienteLicencia);
		if (primerResultado.getResultado()) {
			//No está publicado
			if (solicitudLicencia.getTipoLicencia() != null){
				
				expedienteLicencia.setTipoLicenciaDescripcion(CUtilidadesComponentes.obtenerTipoLicencia(solicitudLicencia));

				if (expedienteLicencia.getEstado() != null){
					expedienteLicencia.getEstado().setDescripcion(CUtilidadesComponentes.obtenerDescripcionEstado(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia()));
				}
			}
			
			CResultadoOperacion segundoResultado = COperacionesLicencias.publicarExpedienteSigem(expedienteLicencia, solicitudLicencia);
			if (segundoResultado.getResultado()) {	
				
				CResultadoOperacion tercerResultado = COperacionesLicencias.actualizarIdSigem(expedienteLicencia);
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
	
	private void abrirJButtonActionPerformed() {//GEN-FIRST:event_abrirJButtonActionPerformed

		int row = anexosJTable.getSelectedRow();
		logger.info("row: " + row);


		if (solicitudLicencia == null) {
			logger.warn("Hay que consultar primero un expediente.");
			return;
		}

		if (row == -1) {
			mostrarMensaje(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.mensaje2"));
			return;
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		String fileName = (String) anexosJTable.getValueAt(row, 0);

        String tmpFile= "";
        File f= new File(fileName);

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
                	resultado = CUtilidadesComponentes.GetURLFile(CConstantesComando.anexosLicenciasUrl + solicitudLicencia.getIdSolicitud() + "/" + fileName, tmpFile, "", 0);
                }
                else {
                	AlfrescoDocumentClient alfrescoDocumentClient = new AlfrescoDocumentClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.ALFRESCO_WEBAPP_NAME +
                            ServletConstants.ALFRESCO_DOCUMENT_SERVLET_NAME);
                	try {
                		String id = CUtilidadesComponentes.getAnexoId(selectedFile.getName(), solicitudLicencia.getIdSolicitud());
                		if(id != null){
                			resultado = alfrescoDocumentClient.downloadFile(new AlfrescoKey(id, AlfrescoKey.KEY_UUID), tmpDir, selectedFile.getName());
                		}
                	} catch (Exception e) {
						logger.error(e);
					}
                }
                if (!resultado) {
                    mostrarMensaje(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.mensaje3"));
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

        /** Visualizamos el fichero descargado si SO es Windows. */
        if (CUtilidadesComponentes.isWindows()){
            try {
                Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + tmpFile + "\"");

            } catch (Exception ex) {
                logger.warn("Exception: " + ex.toString());
                mostrarMensaje(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.mensaje4") + " " + tmpFile);
            }
        }

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_abrirJButtonActionPerformed


	public void cargarReferenciasCatastralesJTable(CSolicitudLicencia solicitudLicencia) {
		try {

			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
			Vector referenciasCatastrales = solicitudLicencia.getReferenciasCatastrales();
			logger.info("referenciasCatastrales: " + referenciasCatastrales);

			if ((referenciasCatastrales == null) || (referenciasCatastrales.isEmpty())) {
				logger.info("No hay referenciasCatastrales para la licencia.");
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}

			CMainOcupaciones.geopistaEditor.getSelectionManager().clear();
            _vReferenciasCatastrales= new Vector();
            Vector vBusqueda=new Vector();

			for (int i = 0; i < referenciasCatastrales.size(); i++) {
				CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) referenciasCatastrales.elementAt(i);
                _vReferenciasCatastrales.add(i, referenciaCatastral);

                String tipoVia= "";
                if ((referenciaCatastral.getTipoVia() != null) && (referenciaCatastral.getTipoVia().trim().length() > 0)){
                    tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(referenciaCatastral.getTipoVia()).getTerm(CMainOcupaciones.currentLocale.toString());
                }

				referenciasCatastralesJTableModel.addRow(new String[]{referenciaCatastral.getReferenciaCatastral(),
                                                                      (referenciaCatastral.getArea()==null?"":referenciaCatastral.getArea().toString()),
																	  tipoVia,
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


	private void consultarJButtonActionPerformed() {//GEN-FIRST:event_consultarJButtonActionPerformed


		try {

			if ((numExpedienteJTField.getText() == null) || (numExpedienteJTField.getText().trim().equals(""))) {
				return;
			}

            /** cancela la busqueda desde el dialogo de busquedade expediente. */
            if (searchExpCanceled){
                searchExpCanceled= false;
                return;
            }
            

            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			CResultadoOperacion resultadoOperacion = COperacionesLicencias.getExpedienteLicencia(numExpedienteJTField.getText(), CMainOcupaciones.literales.getLocale().toString(), null);
            if (!resultadoOperacion.getResultado()){
                new JOptionPane(resultadoOperacion.getDescripcion(),JOptionPane.INFORMATION_MESSAGE).createDialog(desktop,"INFORMACION").show();
                clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            if ((resultadoOperacion.getSolicitudes() != null) && (resultadoOperacion.getExpedientes() != null)){
                solicitudLicencia = (CSolicitudLicencia) resultadoOperacion.getSolicitudes().get(0);
                expedienteLicencia = (CExpedienteLicencia) resultadoOperacion.getExpedientes().get(0);
                alfrescoJButton.setEnabled(true);
            }else{
                mostrarMensaje(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.mensaje5"));
                clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            if ((solicitudLicencia == null) || (expedienteLicencia == null)){
                mostrarMensaje(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.mensaje5"));
                clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }

			CDatosOcupacion datosOcupacion = expedienteLicencia.getDatosOcupacion();

			//***********************************************
			//** Datos de expediente.
			//*******************************************************
			estadoExpedienteJTextField.setText(((DomainNode) Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(expedienteLicencia.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()));
			servicioExpedienteJTField.setText(expedienteLicencia.getServicioEncargado());

			tramitacionJTextField.setText(((DomainNode) Estructuras.getListaTiposTramitacion().getDomainNode(new Integer(expedienteLicencia.getTipoTramitacion().getIdTramitacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()));
			asuntoExpedienteJTField.setText(expedienteLicencia.getAsunto());
			fechaAperturaJTField.setText(CUtilidades.formatFecha(expedienteLicencia.getFechaApertura()));
			inicioJTField.setText(expedienteLicencia.getFormaInicio());
			if (expedienteLicencia.getSilencioAdministrativo().equals("1")) {
				silencioJCheckBox.setSelected(true);
			}
			numFoliosJTField.setText("" + expedienteLicencia.getNumFolios());
			responsableJTField.setText(expedienteLicencia.getResponsable());
			plazoVencimientoJTField.setText(CUtilidades.formatFecha(expedienteLicencia.getPlazoVencimiento()));
			numDiasJTField.setText("" + expedienteLicencia.getNumDias());
			if (expedienteLicencia.getHabiles().equals("1")) {
				habilesJCheckBox.setSelected(true);
			}
            if (expedienteLicencia.getTipoFinalizacion()!=null)
			    finalizaPorJTextField.setText(((DomainNode) Estructuras.getListaTiposFinalizacion().getDomainNode(new Integer(expedienteLicencia.getTipoFinalizacion().getIdFinalizacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()));
			observacionesExpedienteJTArea.setText(expedienteLicencia.getObservaciones());

			//***********************************************
			//** Datos de solicitud.
			//*******************************************************
			tipoObraJTextField.setText(((DomainNode) Estructuras.getListaTipoOcupacion().getDomainNode(""+datosOcupacion.getTipoOcupacion())).getTerm(CMainOcupaciones.currentLocale.toString()));
			estadoJTextField.setText(((DomainNode) Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(expedienteLicencia.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()));

			necesitaObraJCheckBox.setSelected(false);
			if ((datosOcupacion.getNecesitaObra() != null) && (datosOcupacion.getNecesitaObra().equals("1"))) {
				necesitaObraJCheckBox.setSelected(true);
			}

			numExpedienteJTextField.setText(datosOcupacion.getNumExpediente());

 			horaInicioJTextField.setText(CUtilidades.formatHora(datosOcupacion.getHoraInicio()));
			horaFinJTextField.setText(CUtilidades.formatHora(datosOcupacion.getHoraFin()));

			numMesasJTextField.setText(""+datosOcupacion.getNumMesas());
			numSillasJTextField.setText(""+datosOcupacion.getNumSillas());

			afectaAceraJCheckBox.setSelected(false);
			if ((datosOcupacion.getAfectaAcera() != null) && (datosOcupacion.getAfectaAcera().equals("1"))) {
				afectaAceraJCheckBox.setSelected(true);
			}
			afectaCalzadaJCheckBox.setSelected(false);
			if ((datosOcupacion.getAfectaCalzada() != null) && (datosOcupacion.getAfectaCalzada().equals("1"))) {
				afectaCalzadaJCheckBox.setSelected(true);
			}
			afectaAparcamientoJCheckBox.setSelected(false);
			if ((datosOcupacion.getAfectaAparcamiento() != null) && (datosOcupacion.getAfectaAparcamiento().equals("1"))) {
				afectaAparcamientoJCheckBox.setSelected(true);
			}
            areaOcupacionJTextField.setText(""+datosOcupacion.getAreaOcupacion());

			metros2aceraJTextField.setText(""+datosOcupacion.getM2acera());
			metros2calzadaJTextField.setText(""+datosOcupacion.getM2calzada());
			metros2aparcamientoJTextField.setText(""+datosOcupacion.getM2aparcamiento());

			motivoJTField.setText(solicitudLicencia.getMotivo());
			asuntoJTField.setText(solicitudLicencia.getAsunto());
			fechaSolicitudJTField.setText(CUtilidades.formatFecha(solicitudLicencia.getFecha()));
            String tipoVia= "";
            if ((solicitudLicencia.getTipoViaAfecta() != null) && (solicitudLicencia.getTipoViaAfecta().trim().length() > 0)){
                tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(solicitudLicencia.getTipoViaAfecta()).getTerm(CMainOcupaciones.currentLocale.toString());
            }
            tipoViaJTextField.setText(tipoVia);

			observacionesJTArea.setText(solicitudLicencia.getObservaciones());
			nombreViaJTextField.setText(solicitudLicencia.getNombreViaAfecta());
			numeroViaJTextField.setText(solicitudLicencia.getNumeroViaAfecta());
			portalJTextField.setText(solicitudLicencia.getPortalAfecta());
			plantaJTextField.setText(solicitudLicencia.getPlantaAfecta());
			//cPostalJTextField.setText(solicitudLicencia.getCpostalAfecta());
            try{
                cPostalJTextField.setNumber(new Integer(solicitudLicencia.getCpostalAfecta()));
            }catch(Exception e){cPostalJTextField.setText("");}
			municipioJTextField.setText(solicitudLicencia.getMunicipioAfecta());
			provinciaJTextField.setText(solicitudLicencia.getProvinciaAfecta());

			cargarReferenciasCatastralesJTable(solicitudLicencia);
			//refreshFeatureSelection();

			/** Anexos */
			CUtilidadesComponentes.clearTable(anexosJTableModel);
			Vector anexos = solicitudLicencia.getAnexos();
			
			cargarAnexosJTable(anexos);

			//***********************************************
			//** Datos titular.
			//***********************************************
			DNITitularJTField.setText(solicitudLicencia.getPropietario().getDniCif());
			nombreTitularJTField.setText(solicitudLicencia.getPropietario().getNombre());
			apellido1TitularJTField.setText(solicitudLicencia.getPropietario().getApellido1());
			apellido2TitularJTField.setText(solicitudLicencia.getPropietario().getApellido2());

			viaNotificacionTitularJTField.setText(((DomainNode) Estructuras.getListaViasNotificacion().getDomainNode(new Integer(solicitudLicencia.getPropietario().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()));
            if (new Integer(solicitudLicencia.getPropietario().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString().equalsIgnoreCase(CConstantesOcupaciones.patronNotificacionEmail)){
                emailTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.emailTitularJLabel.text")));
            }else{
                emailTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.emailTitularJLabel.text"));
            }

			faxTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getFax());
			telefonoTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getTelefono());
			movilTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getMovil());
			emailTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getEmail());

			tipoViaNotificacionTitularJTField.setText(((DomainNode) Estructuras.getListaTiposViaINE().getDomainNode(solicitudLicencia.getPropietario().getDatosNotificacion().getTipoVia())).getTerm(CMainOcupaciones.currentLocale.toString()));
			nombreViaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getNombreVia());
			numeroViaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getNumeroVia());
			plantaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getPlanta());
			plantaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getPlanta());
			letraTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getLetra());
			portalTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getPortal());
			escaleraTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getEscalera());
			//cPostalTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getCpostal());
            try{
               cPostalTitularJTField.setNumber(new Integer(solicitudLicencia.getPropietario().getDatosNotificacion().getCpostal()));
            }catch(Exception e){cPostalTitularJTField.setText("");}
			municipioTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getMunicipio());
			provinciaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getProvincia());
			if (solicitudLicencia.getPropietario().getDatosNotificacion().getNotificar().equals("1")) {
				notificarTitularJCheckBox.setSelected(true);
			}



			//***********************************************
			//** Datos representado.
			//*******************************************************
			if (solicitudLicencia.getRepresentante() != null) {

				DNIRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDniCif());
				nombreRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getNombre());
				apellido1RepresentaAJTField.setText(solicitudLicencia.getRepresentante().getApellido1());
				apellido2RepresentaAJTField.setText(solicitudLicencia.getRepresentante().getApellido2());

				viaNotificacionRepresentaAJTField.setText(((DomainNode) Estructuras.getListaViasNotificacion().getDomainNode(new Integer(solicitudLicencia.getRepresentante().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()));
                if (new Integer(solicitudLicencia.getRepresentante().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString().equalsIgnoreCase(CConstantesOcupaciones.patronNotificacionEmail)){
                    emailRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.emailRepresentaAJLabel.text")));
                }else{
                    emailRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.emailRepresentaAJLabel.text"));
                }

				faxRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getFax());
				telefonoRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getTelefono());
				movilRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getMovil());
				emailRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getEmail());

				tipoViaNotificacionRepresentaAJTField.setText(((DomainNode) Estructuras.getListaTiposViaINE().getDomainNode(solicitudLicencia.getRepresentante().getDatosNotificacion().getTipoVia())).getTerm(CMainOcupaciones.currentLocale.toString()));
				nombreViaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getNombreVia());
				numeroViaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getNumeroVia());
				plantaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getPlanta());
				plantaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getPlanta());
				letraRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getLetra());
				portalRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getPortal());
				escaleraRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getEscalera());
				//cPostalRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getCpostal());
                try{
                   cPostalRepresentaAJTField.setNumber(new Integer(solicitudLicencia.getRepresentante().getDatosNotificacion().getCpostal()));
                }catch (Exception e){cPostalRepresentaAJTField.setText("");}
				municipioRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getMunicipio());
				provinciaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getProvincia());
				if (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equals("1")) {
					notificarRepresentaAJCheckBox.setSelected(true);
				}
			}




			//***********************************************
			//** Datos notificaciones
			//***********************************************
			CUtilidadesComponentes.clearTable(notificacionesJTableModel);
			// Annadimos a la tabla el editor ComboBox en la primera columna (estado)
			_vNotificaciones = expedienteLicencia.getNotificaciones();
			if ((_vNotificaciones != null) && (_vNotificaciones.size() > 0)) {
                for (int i=0; i <_vNotificaciones.size(); i++){
					CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(i);
					int tipoEstado = notificacion.getEstadoNotificacion().getIdEstado();
					String descEstadoNotif = ((DomainNode) Estructuras.getListaEstadosNotificacion().getDomainNode(new Integer(tipoEstado).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
					Object[] rowData = {descEstadoNotif,
                                        CUtilidades.formatFechaH24(notificacion.getPlazoVencimiento()),
                                        notificacion.getPersona().getDniCif(),
                                        CUtilidades.formatFechaH24(notificacion.getFechaNotificacion()),
                                        CUtilidades.formatFechaH24(notificacion.getFecha_reenvio()),
                                        new Integer(i)};
					notificacionesJTableModel.addRow(rowData);
				}
			}

			//***********************************************
			//** Datos eventos
			//***********************************************
			// Annadimos a la tabla el editor CheckBox en la tercera columna
			int vColIndexCheck = 2;
			TableColumn col2 = eventosJTable.getColumnModel().getColumn(vColIndexCheck);
			col2.setCellEditor(new CheckBoxTableEditor(new JCheckBox()));
			col2.setCellRenderer(new CheckBoxRenderer());

			_vEventos = expedienteLicencia.getEventos();
			if ((_vEventos != null) && (_vEventos.size() > 0)) {
                for(int i=0; i < _vEventos.size(); i++){
				//int row = 0;
					CEvento evento = (CEvento) _vEventos.get(i);
					JCheckBox check = (JCheckBox) ((CheckBoxTableEditor) eventosJTable.getCellEditor(i, 2)).getComponent();

					if (evento.getRevisado().equalsIgnoreCase("1"))
						check.setSelected(true);
					else
						check.setSelected(false);
					_eventosTableModel.addRow(new Object[]{new Long(evento.getIdEvento()).toString(),
                                                           CUtilidades.formatFechaH24(evento.getFechaEvento()),
                                                           new Boolean(check.isSelected()),
                                                           evento.getRevisadoPor(),
                                                           evento.getContent(),
                                                           new Integer(i)});

					//row++;
				}
			}

			//***********************************************
			//** Datos historico
			//***********************************************
            _vHistorico= new Vector();
            Vector aux= expedienteLicencia.getHistorico();
            for (int i=0; i<aux.size(); i++){
                CHistorico histAux= (CHistorico)aux.get(i);
                String apunte= histAux.getApunte();
                /** Comprobamos que el historico no sea generico, en cuyo caso, no es necesario annadir
                 * al apunte el literal del estado al que ha cambiado.
                 */                
                if ((histAux.getSistema().equalsIgnoreCase("1")) && (histAux.getGenerico() == 0)){
                    /** Componemos el apunte, de forma multilingue */
                    apunte+= " " +
                            ((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(histAux.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString() + ".");
                   histAux.setApunte(apunte);
                }
                _vHistorico.add(histAux);

            }

            for (int i=0; i < _vHistorico.size(); i++){
                CHistorico historico = (CHistorico)_vHistorico.get(i);
                String estado= "";
                try{
                    estado= ((DomainNode) Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
                }catch(Exception ex){
                    logger.error("ERROR al cargar un historico con estado de espedinte "+historico.getEstado().getIdEstado());
                }
                _historicoTableModel.addRow(new Object[]{//new Long(historico.getIdHistorico()).toString(),
                                                         estado,
                                                         historico.getUsuario(),
                                                         CUtilidades.formatFechaH24(historico.getFechaHistorico()),
                                                         historico.getApunte(),
                                                         new Integer(i)});
            }

            jPanelCallesAfectadas.setCallesAfectadas(expedienteLicencia.getCallesAfec(),expedienteLicencia, solicitudLicencia,
                                         CMainOcupaciones.geopistaEditor,false);

            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


		} catch (Exception ex) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

		}


	}//GEN-LAST:event_consultarJButtonActionPerformed

	
	private void cargarAnexosJTable(Vector anexos){
		if (anexos != null) {
			for (int i = 0; i < anexos.size(); i++) {
				CAnexo anexo = (CAnexo) anexos.elementAt(i);
				String store = "LocalGIS";
                if(AlfrescoManagerUtils.isAlfrescoUuid(anexo.getIdAnexo(), String.valueOf(AppContext.getIdMunicipio()))){
                	store = "Alfresco";
            	}
				String descTipoAnexo = ((DomainNode) Estructuras.getListaTiposAnexo().getDomainNode(new Integer(anexo.getTipoAnexo().getIdTipoAnexo()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
				anexosJTableModel.addRow(new String[]{anexo.getFileName(), descTipoAnexo, anexo.getObservacion(),store});
				_hAnexosSolicitud.put(anexo.getFileName(), anexo);
			}
		}
	}

	private boolean clearScreen() {

		//***********************************************
		//** Datos de expediente.
		//*******************************************************
		estadoExpedienteJTextField.setText("");
		servicioExpedienteJTField.setText("");
		asuntoExpedienteJTField.setText("");
		fechaAperturaJTField.setText("");
		inicioJTField.setText("");
		silencioJCheckBox.setSelected(false);

		tramitacionJTextField.setText("");
		finalizaPorJTextField.setText("");
		numFoliosJTField.setText("");
		responsableJTField.setText("");
		plazoVencimientoJTField.setText("");
		numDiasJTField.setText("");
		habilesJCheckBox.setSelected(false);

		observacionesExpedienteJTArea.setText("");

		//***********************************************
		//** Datos de solicitud.
		//*******************************************************
		estadoJTextField.setText("");
		motivoJTField.setText("");
		asuntoJTField.setText("");
		fechaSolicitudJTField.setText("");
		observacionesJTArea.setText("");
		tipoViaJTextField.setText("");
		nombreViaJTextField.setText("");
		numeroViaJTextField.setText("");
		portalJTextField.setText("");
		plantaJTextField.setText("");
		cPostalJTextField.setText("");
		municipioJTextField.setText("");
		provinciaJTextField.setText("");

		CUtilidadesComponentes.clearTable(anexosJTableModel);
		CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
		CMainOcupaciones.geopistaEditor.getSelectionManager().clear();


		//***********************************************
		//** Datos titular.
		//*******************************************************
		DNITitularJTField.setText("");
		nombreTitularJTField.setText("");
		apellido1TitularJTField.setText("");
		apellido2TitularJTField.setText("");

		viaNotificacionTitularJTField.setText("");
		faxTitularJTField.setText("");
		telefonoTitularJTField.setText("");
		movilTitularJTField.setText("");
		emailTitularJTField.setText("");

		tipoViaNotificacionTitularJTField.setText("");
		nombreViaTitularJTField.setText("");
		numeroViaTitularJTField.setText("");
		plantaTitularJTField.setText("");
		plantaTitularJTField.setText("");
		letraTitularJTField.setText("");
		portalTitularJTField.setText("");
		escaleraTitularJTField.setText("");
		cPostalTitularJTField.setText("");
		municipioTitularJTField.setText("");
		provinciaTitularJTField.setText("");



		//***********************************************
		//** Datos representado.
		//*******************************************************
		DNIRepresentaAJTField.setText("");
		nombreRepresentaAJTField.setText("");
		apellido1RepresentaAJTField.setText("");
		apellido2RepresentaAJTField.setText("");

		viaNotificacionRepresentaAJTField.setText("");
		faxRepresentaAJTField.setText("");
		telefonoRepresentaAJTField.setText("");
		movilRepresentaAJTField.setText("");
		emailRepresentaAJTField.setText("");

		tipoViaNotificacionRepresentaAJTField.setText("");
		nombreViaRepresentaAJTField.setText("");
		numeroViaRepresentaAJTField.setText("");
		plantaRepresentaAJTField.setText("");
		plantaRepresentaAJTField.setText("");
		letraRepresentaAJTField.setText("");
		portalRepresentaAJTField.setText("");
		escaleraRepresentaAJTField.setText("");
		cPostalRepresentaAJTField.setText("");
		municipioRepresentaAJTField.setText("");
		provinciaRepresentaAJTField.setText("");
		notificarRepresentaAJCheckBox.setSelected(false);




		//***********************************************
		//** Datos notificaciones
		//***********************************************
		CUtilidadesComponentes.clearTable(notificacionesJTableModel);
		datosNombreApellidosJTField.setText("");
		datosDireccionJTField.setText("");
		datosCPostalJTField.setText("");
		datosNotificarPorJTField.setText("");
        entregadaAJTField.setText("");

		//***********************************************
		//** Datos eventos
		//***********************************************
		CUtilidadesComponentes.clearTable(_eventosTableModel);
		descEventoJTArea.setText("");

		//***********************************************
		//** Datos historico
		//***********************************************
		CUtilidadesComponentes.clearTable(_historicoTableModel);
		apunteJTArea.setText("");


		return true;

	}

	private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed

        CSearchJDialog dialog= CUtilidadesComponentes.showSearchDialog(desktop, false);
        if ((dialog != null) && (dialog.getSelectedExpediente() != null)){
            if (!dialog.getOperacionCancelada()){
                if ((dialog.getSelectedExpediente() != null) && (dialog.getSelectedExpediente().trim().length() > 0)){
                    numExpedienteJTField.setText(dialog.getSelectedExpediente());
                }
            }else searchExpCanceled= true;
        }
        consultarJButtonActionPerformed();

	}//GEN-LAST:event_buscarExpedienteJButtonActionPerformed

	private void salirJButtonActionPerformed() {//GEN-FIRST:event_salirJButtonActionPerformed
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
		this.dispose();
	}//GEN-LAST:event_salirJButtonActionPerformed


	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(desktop, mensaje);
	}

    private void tableToMapAllJButtonActionPerformed() {//GEN-FIRST:event_tableToMapAllJButtonActionPerformed

        Vector vBusqueda= new Vector();
        for (int i = 0; i < referenciasCatastralesJTableModel.getRowCount(); i++) {
            String value = (String) referenciasCatastralesJTableModel.getValueAt(i, 0);
            vBusqueda.add(value);
        }
        refreshFeatureSelection(layerName,0,vBusqueda);
    }//GEN-LAST:event_tableToMapAllJButtonActionPerformed

    private void tableToMapOneJButtonActionPerformed() {//GEN-FIRST:event_tableToMapOneJButtonActionPerformed

            if (referenciasCatastralesJTable.getSelectedRow() == -1) {
                logger.info("El usuario ha de seleccionar primero una fila");
                return;
            }
            String value = (String) referenciasCatastralesJTableModel.getValueAt(referenciasCatastralesJTable.getSelectedRow(), 0);
            refreshFeatureSelection(layerName,0,value);
        }//GEN-LAST:event_tableToMapOneJButtonActionPerformed


    public void renombrarComponentes() {
        renombrarComponentes(false);
    }

	public void renombrarComponentes(boolean construir) {

		try {
            JTabbedPane jTabbedSolicitud = null;

			setTitle(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.JInternalFrame.title"));

            /** Pestanas */
            if (construir) {
                try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/expediente.jpg"));
                    datosSolicitudJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.expedienteJPanel.TitleTab")), icon,expedienteJPanel);
                }catch(Exception e)
                {
                    datosSolicitudJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.expedienteJPanel.TitleTab")), expedienteJPanel);
                }
                expedienteJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.expedienteJPanel.TitleBorder")));

                /** Solicitud */
                /*JTabbedPane*/ jTabbedSolicitud= new JTabbedPane();
                try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/solicitud.jpg"));
                    jTabbedSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.solicitudJPanel.TitleTab")), icon, solicitudJPanel);

                }catch(Exception e)
                {
                     jTabbedSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.solicitudJPanel.TitleTab")), solicitudJPanel);
                }
                 try
                {
                    ClassLoader cl =this.getClass().getClassLoader();
                    Icon icon= new javax.swing.ImageIcon(cl.getResource("img/solicitud.jpg"));
                    datosSolicitudJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.solicitudJPanel.TitleTab")), icon, jTabbedSolicitud);

                }catch(Exception e)
                {
                     datosSolicitudJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.solicitudJPanel.TitleTab")), jTabbedSolicitud);
                }
            }
            else {
                datosSolicitudJTabbedPane.setTitleAt(0, com.geopista.app.ocupaciones.CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.expedienteJPanel.TitleTab")));
                datosSolicitudJTabbedPane.setTitleAt(1, com.geopista.app.ocupaciones.CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.solicitudJPanel.TitleTab")));
                jTabbedSolicitud = (JTabbedPane)datosSolicitudJTabbedPane.getComponentAt(1);
                jTabbedSolicitud.setTitleAt(0, com.geopista.app.ocupaciones.CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.solicitudJPanel.TitleTab")));
                jTabbedSolicitud.setTitleAt(1, CMainOcupaciones.literales.getString("CConsultaLicenciasForm.titularJPanel.TitleTab"));
                jTabbedSolicitud.setTitleAt(2, com.geopista.app.ocupaciones.CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.representaAJPanel.TitleTab")));
                datosSolicitudJTabbedPane.setTitleAt(2, CMainOcupaciones.literales.getString("jPanelCallesAfectadas.title"));
                datosSolicitudJTabbedPane.setTitleAt(3, CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJPanel.TitleTab"));
                datosSolicitudJTabbedPane.setTitleAt(4, CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJPanel.TitleTab"));
                datosSolicitudJTabbedPane.setTitleAt(5, CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJPanel.TitleTab"));
            }

			/** Expediente */
			inicioJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.inicioJLabel.text"));
			numFoliosJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.numFoliosJLabel.text"));
			responsableJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.responsableJLabel.text"));
			plazoVencimientoJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.plazoVencimientoJLabel.text"));
			numDiasJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.numDiasJLabel.text"));
			consultarJButton.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.consultarJButton.text"));
			silencioJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.silencioJLabel.text"));
			habilesJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.habilesJLabel.text"));
			notaJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notaJLabel.text"));
            estadoExpedienteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.estadoExpedienteJLabel.text")));

            numExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.numExpedienteJLabel.text"));
            servicioExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.servicioExpedienteJLabel.text"));
            tramitacionJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.tramitacionJLabel.text"));
            finalizaJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.finalizaJLable.text"));
            asuntoExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.asuntoExpedienteJLabel.text"));
            fechaAperturaJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.fechaAperturaJLabel.text"));
            observacionesExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.observacionesExpedienteJLabel.text"));
            inicioJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.inicioJLabel.text"));



       		datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosSolicitudJPanel.TitleBorder")));
			estadoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.estadoJLabel.text")));
			tipoObraJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.tipoObraJLabel.text")));
			necesitaObraJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.necesitaObraJLabel.text"));
			expedienteObraJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.expedienteObraJLabel.text"));
			motivoJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.motivoJLabel.text"));
			asuntoJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.asuntoJLabel.text"));
			fechaSolicitudJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.fechaSolicitudJLabel.text")));
			observacionesJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.observacionesJLabel.text"));
			emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.emplazamientoJPanel.TitleBorder")));
			nombreViaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.tipoViaJLabel.text"), CMainOcupaciones.literales.getString("CConsultaLicenciasForm.nombreViaJLabel.text")));
			numeroViaJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.numeroViaJLabel.text"));
			cPostalJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.cPostalJLabel.text"));
			provinciaJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.provinciaJLabel.text"));
			anexosJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.anexosJPanel.TitleBorder")));
            abrirJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.abrirJButton.toolTipText.text"));
            numSillasJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.numSillasJLabel.text"));
            numMesasJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.numMesasJLabel.text"));
            areaOcupacionJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.areaOcupacionJLabel.text"));
            metros2JLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.metros2JLabel.text"));
            afectaJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.afectaJLabel.text"));
            afectaAceraJCheckBox.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.aceraJCheckBox.text"));
            afectaAparcamientoJCheckBox.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.aparcamientoJCheckBox.text"));
            afectaCalzadaJCheckBox.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.calzadaJCheckBox.text"));
            horaJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.horaJLabel.text"));


			/** Propietario */
			datosPersonalesTitularJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosPersonalesTitularJPanel.TitleBorder")));
			DNITitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.DNITitularJLabel.text")));
			nombreTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.nombreTitularJLabel.text")));
			apellido1TitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.apellido1TitularJLabel.text"));
			apellido2TitularJLabel2.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.apellido2TitularJLabel.text"));
			datosNotificacionTitularJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosNotificacionTitularJPanel.TitleTab")));
			viaNotificacionTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.viaNotificacionTitularJLabel.text"));
			faxTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.faxTitularJLabel.text"));
			telefonoTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.telefonoTitularJLabel.text"));
			movilTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.movilTitularJLabel.text"));
			emailTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.emailTitularJLabel.text"));
			tipoViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.tipoViaTitularJLabel.text")));
			nombreViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.nombreViaTitularJLabel.text")));
			numeroViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.numeroViaTiularJLabel.text")));
			portalTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.portalTitularJLabel.text"));
			plantaTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.plantaTitularJLabel.text"));
			escaleraTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.escaleraTitularJLabel.text"));
			letraTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.letraTitularJLabel.text"));
			cPostalTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.cPostalTitularJLabel.text")));
			municipioTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.municipioTitularJLabel.text")));
			provinciaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.provinciaTitularJLabel.text")));
			notificarTitularJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificarTitularJLabel.text"));
            if (construir) {
            try
            {
                 ClassLoader cl =this.getClass().getClassLoader();
                 Icon icon= new javax.swing.ImageIcon(cl.getResource("img/persona.jpg"));
                 jTabbedSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.titularJPanel.TitleTab")), icon,titularJPanel);
            }catch(Exception e)
            {
                 jTabbedSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.titularJPanel.TitleTab")), titularJPanel);
            }
            }

			/** Representante */
			datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosPersonalesRepresentaAJPanel.TitleBorder")));
			DNIRepresenaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.DNIRepresentaAJLabel.text")));
			nombreRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.nombreRepresentaAJLabel.text")));
			apellido1RepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.apellido1RepresentaAJLabel.text"));
			apellido2RepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.apellido2RepresentaAJLabel.text"));
			datosNotificacionRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosNotificacionRepresentaAJPanel.TitleTab")));
			viaNotificacionRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.viaNotificacionRepresentaAJLabel.text"));
			faxRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.faxRepresentaAJLabel.text"));
			telefonoRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.telefonoRepresentaAJLabel.text"));
			movilRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.movilRepresentaAJLabel.text"));
			emailRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.emailRepresentaAJLabel.text"));
			tipoViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.tipoViaRepresentaAJLabel.text")));
			nombreViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.nombreViaRepresentaAJLabel.text")));
			numeroViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.numeroViaTiularJLabel.text")));
			portalRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.portalRepresentaAJLabel.text"));
			plantaRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.plantaRepresentaAJLabel.text"));
			escaleraRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.escaleraRepresentaAJLabel.text"));
			letraRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.letraRepresentaAJLabel.text"));
			cPostalRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.cPostalRepresentaAJLabel.text")));
			municipioRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.municipioRepresentaAJLabel.text")));
			provinciaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.provinciaRepresentaAJLabel.text")));
			notificarRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificarRepresentaAJLabel.text"));
            if (construir) {
             try
            {
                ClassLoader cl =this.getClass().getClassLoader();
                Icon icon= new javax.swing.ImageIcon(cl.getResource("img/representante.jpg"));
                jTabbedSolicitud.addTab(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.representaAJPanel.TitleTab"), icon,representaAJPanel);

            }catch(Exception e)
            {
                jTabbedSolicitud.addTab(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.representaAJPanel.TitleTab"), representaAJPanel);
            }
            }

			//datosSolicitudJTabbedPane.addTab(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.promotorJPanel.TitleTab"), promotorJPanel);

			/** notificaciones */
			datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosNotificacionesJPanel.TitleBorder")));
			datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosNotificacionJPanel.TitleBorder")));
			datosNombreApellidosJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosNombreApellidosJLabel.text"));
			datosDireccionJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosDireccionJLabel.text"));
			datosCPostalJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosCPostalJLabel.text"));
			datosNotificarPorJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosNotificarPorJLabel.text"));
            if (construir) {
            try
           {
               ClassLoader cl =this.getClass().getClassLoader();
               Icon icon= new javax.swing.ImageIcon(cl.getResource("img/calle.jpg"));
               datosSolicitudJTabbedPane.addTab(CMainOcupaciones.literales.getString("jPanelCallesAfectadas.title"),icon,jPanelCallesAfectadas);//CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), icon,jTabbedSolicitud);

           }catch(Exception e)
           {
               datosSolicitudJTabbedPane.addTab(CMainOcupaciones.literales.getString("jPanelCallesAfectadas.title"),jPanelCallesAfectadas);//CMainOcupaciones.literales.getString("CModificacionLicenciasForm.solicitudJPanel.TitleTab")), jTabbedSolicitud);
           }

             try
            {
                ClassLoader cl =this.getClass().getClassLoader();
                Icon icon= new javax.swing.ImageIcon(cl.getResource("img/notificacion.jpg"));
                 datosSolicitudJTabbedPane.addTab(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJPanel.TitleTab"), icon,notificacionesJPanel);
            }catch(Exception e)
            {
                 datosSolicitudJTabbedPane.addTab(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJPanel.TitleTab"), notificacionesJPanel);
            }
            }

			if (construir) {
            /** Eventos */
			datosEventosJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosEventosJPanel.TitleBorder")));
			descEventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.descEventoJScrollPane.TitleBorder")));
             try
            {
                ClassLoader cl =this.getClass().getClassLoader();
                Icon icon= new javax.swing.ImageIcon(cl.getResource("img/evento.jpg"));
                datosSolicitudJTabbedPane.addTab(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJPanel.TitleTab"),icon, eventosJPanel);
            }catch(Exception e)
            {
                datosSolicitudJTabbedPane.addTab(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJPanel.TitleTab"), eventosJPanel);
            }

			/** Historico */
			datosHistoricoJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosHistoricoJPanel.TitleBorder")));
			apunteJScrollPane.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.apunteJScrollPane.TitleBorder")));
            try
            {
                ClassLoader cl =this.getClass().getClassLoader();
                Icon icon= new javax.swing.ImageIcon(cl.getResource("img/historico.jpg"));
                datosSolicitudJTabbedPane.addTab(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJPanel.TitleTab"), icon,historicoJPanel);
            }catch(Exception e)
            {
                datosSolicitudJTabbedPane.addTab(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJPanel.TitleTab"), historicoJPanel);
            }
            }

			salirJButton.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.salirJButton.text"));
			modificarJButton.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.modificarJButton.text"));
			
			publicarJButton.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.publicarJButton.text"));
			
			editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.editorMapaJPanel.TitleBorder")));

            jButtonGenerarFicha.setText(CMainOcupaciones.literales.getString("CMainOcupaciones.jButtonGenerarFicha"));//"Generar Ficha");
            entregadaAJLabel.setText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.datosEntregadaAJLabel.text"));

            jPanelCallesAfectadas.renombrarComponentes();

            ((TitledBorder)datosEventosJPanel.getBorder()).setTitle(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJPanel.TitleTab"));
            ((TitledBorder)((JScrollPane)datosEventosJPanel.getComponent(1)).getBorder()).setTitle(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.descEventoJScrollPane.TitleBorder"));

            ((TitledBorder)datosHistoricoJPanel.getBorder()).setTitle(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJPanel.TitleTab"));
            ((TitledBorder)((JScrollPane)datosHistoricoJPanel.getComponent(1)).getBorder()).setTitle(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.apunteJScrollPane.TitleBorder"));

            consultarJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.consultarJButton.text"));
            salirJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.salirJButton.text"));
            modificarJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.modificarJButton.text"));
            
            publicarJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.publicarJButton.text"));
            
            buscarExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.buscarExpedienteJButton.toolTip.text"));
            tableToMapAllJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.tableToMapAllJButton.toolTip.text"));
            tableToMapOneJButton.setToolTipText(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.tableToMapOneJButton.toolTip.text"));
            jButtonGenerarFicha.setToolTipText(CMainOcupaciones.literales.getString("CMainOcupaciones.jButtonGenerarFicha"));//"Generar Ficha");

            /** Headers de la tabla eventos */
            TableColumn tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum1"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum10"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum2"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum3"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum4"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum5"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum6"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum7"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum8"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(9);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.referenciasCatastralesJTable.colum9"));

            tableColumn= anexosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.anexosJTable.column1"));
            tableColumn= anexosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.anexosJTable.column2"));
            tableColumn= anexosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.anexosJTable.column3"));

            tableColumn= notificacionesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum1"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum2"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum3"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum4"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.notificacionesJTable.colum5"));

            tableColumn= eventosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum1"));
            tableColumn= eventosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum2"));
            tableColumn= eventosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum3"));
            tableColumn= eventosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum4"));
            tableColumn= eventosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.eventosJTable.colum5"));

            tableColumn= historicoJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJTable.colum2"));
            tableColumn= historicoJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJTable.colum3"));
            tableColumn= historicoJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJTable.colum4"));
            tableColumn= historicoJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CConsultaLicenciasForm.historicoJTable.colum5"));

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
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

       private boolean refreshFeatureSelection(String layerName, String attributeName, String sBuscado) {
           Vector vBusqueda= new Vector();
           vBusqueda.add(sBuscado);
           return refreshFeatureSelection(layerName, attributeName, vBusqueda);
       }

       private void docManager() {    
       	if(solicitudLicencia != null && expedienteLicencia != null){
   	    	String app = getAlfrescoAppName(solicitudLicencia.getTipoLicencia().getIdTipolicencia());	    	
   	    	if(app != null){
   	    		(new AlfrescoExplorer(AppContext.getApplicationContext().getMainFrame(),
   						solicitudLicencia.getIdSolicitud(), getAnexosUuid(), String.valueOf(AppContext.getIdMunicipio()), app))
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
	       		CResultadoOperacion resultadoOperacion = COperacionesLicencias.getExpedienteLicencia(expedienteLicencia.getNumExpediente(), CMainOcupaciones.literales.getLocale().toString(), new Vector());
	       		if (resultadoOperacion.getResultado() && resultadoOperacion.getSolicitudes()!=null){
	       			CSolicitudLicencia solicitudLicencia = (CSolicitudLicencia) resultadoOperacion.getSolicitudes().get(0);
	   	    		if(solicitudLicencia.getAnexos()!=null && solicitudLicencia.getAnexos().size()>0){
	   	    			CUtilidadesComponentes.clearTable(anexosJTableModel);
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
       		Vector<CAnexo> anexos = solicitudLicencia.getAnexos();
       		Iterator<CAnexo> itAnexos = anexos.iterator();
       		while(itAnexos.hasNext()){
       			anexosUuid.add(String.valueOf(itAnexos.next().getIdAnexo()));    		
       		}
       		return anexosUuid;
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

    public void setEnabledDatosSolicitud(boolean b){
        estadoJTextField.setEnabled(false);
        tipoObraJTextField.setEnabled(b);
        motivoJTField.setEnabled(b);
        asuntoJTField.setEnabled(b);
        metros2aceraJTextField.setEnabled(b);
        metros2aparcamientoJTextField.setEnabled(b);
        metros2calzadaJTextField.setEnabled(b);
        numMesasJTextField.setEnabled(b);
        numSillasJTextField.setEnabled(b);
        areaOcupacionJTextField.setEnabled(b);
        horaFinJTextField.setEnabled(b);
        horaInicioJTextField.setEnabled(b);
        numExpedienteJTextField.setEnabled(b);
        fechaSolicitudJTField.setEnabled(b);
        observacionesJTArea.setEnabled(b);
        tipoViaJTextField.setEnabled(b);
        nombreViaJTextField.setEnabled(b);
        numeroViaJTextField.setEnabled(b);
        portalJTextField.setEnabled(b);
        plantaJTextField.setEnabled(b);
        cPostalJTextField.setEnabled(b);
        municipioJTextField.setEnabled(b);
        provinciaJTextField.setEnabled(b);
    }

    public void setEnabledDatosExpediente(boolean b){
        estadoExpedienteJTextField.setEnabled(b);
        servicioExpedienteJTField.setEnabled(b);
        asuntoExpedienteJTField.setEnabled(b);
        fechaAperturaJTField.setEnabled(b);
        inicioJTField.setEnabled(b);
        silencioJCheckBox.setEnabled(b);
        //numExpedienteJTField.setEnabled(b);
        tramitacionJTextField.setEnabled(b);
        finalizaPorJTextField.setEnabled(b);
        numFoliosJTField.setEnabled(b);
        responsableJTField.setEnabled(b);
        plazoVencimientoJTField.setEnabled(b);
        numDiasJTField.setEnabled(b);
        habilesJCheckBox.setEnabled(b);
        silencioJCheckBox.setEnabled(b);

        observacionesExpedienteJTArea.setEnabled(b);
        /** descripcion del evento */
        descEventoJTArea.setEnabled(b);
        /** apunte del historico */
        apunteJTArea.setEnabled(b);
    }

    public void setEnabledDatosTitular(boolean b){
        DNITitularJTField. setEnabled(b);
        nombreTitularJTField.setEnabled(b);
        apellido1TitularJTField.setEnabled(b);
        apellido2TitularJTField.setEnabled(b);

        viaNotificacionTitularJTField.setEnabled(b);
        faxTitularJTField.setEnabled(b);
        telefonoTitularJTField.setEnabled(b);
        movilTitularJTField.setEnabled(b);
        emailTitularJTField.setEnabled(b);

        tipoViaNotificacionTitularJTField.setEnabled(b);
        nombreViaTitularJTField.setEnabled(b);
        numeroViaTitularJTField.setEnabled(b);
        plantaTitularJTField.setEnabled(b);
        plantaTitularJTField.setEnabled(b);
        letraTitularJTField.setEnabled(b);
        portalTitularJTField.setEnabled(b);
        escaleraTitularJTField.setEnabled(b);
        cPostalTitularJTField.setEnabled(b);
        municipioTitularJTField.setEnabled(b);
        provinciaTitularJTField.setEnabled(b);
        notificarTitularJCheckBox.setEnabled(b);
    }


     public void setEnabledDatosRepresentante(boolean b){
        DNIRepresentaAJTField.setEnabled(b);
        nombreRepresentaAJTField.setEnabled(b);
        apellido1RepresentaAJTField.setEnabled(b);
        apellido2RepresentaAJTField.setEnabled(b);

        viaNotificacionRepresentaAJTField.setEnabled(b);
        faxRepresentaAJTField.setEnabled(b);
        telefonoRepresentaAJTField.setEnabled(b);
        movilRepresentaAJTField.setEnabled(b);
        emailRepresentaAJTField.setEnabled(b);

        tipoViaNotificacionRepresentaAJTField.setEnabled(b);
        nombreViaRepresentaAJTField.setEnabled(b);
        numeroViaRepresentaAJTField.setEnabled(b);
        plantaRepresentaAJTField.setEnabled(b);
        plantaRepresentaAJTField.setEnabled(b);
        letraRepresentaAJTField.setEnabled(b);
        portalRepresentaAJTField.setEnabled(b);
        escaleraRepresentaAJTField.setEnabled(b);
        cPostalRepresentaAJTField.setEnabled(b);
        municipioRepresentaAJTField.setEnabled(b);
        provinciaRepresentaAJTField.setEnabled(b);
        notificarRepresentaAJCheckBox.setEnabled(b);

    }

    public void setEnabledModificarJButton(boolean b){
        modificarJButton.setEnabled(b);
    }
    
    public void setEnabledPublicarJButton(boolean b){
        publicarJButton.setEnabled(b);
    }


	private CSolicitudLicencia solicitudLicencia;
    private CExpedienteLicencia expedienteLicencia;
	private DefaultTableModel notificacionesJTableModel;
	private DefaultTableModel anexosJTableModel;
	private DefaultTableModel referenciasCatastralesJTableModel;
	private DefaultTableModel _eventosTableModel;
	private DefaultTableModel _historicoTableModel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DNIRepresenaAJLabel;
    private javax.swing.JTextField DNIRepresentaAJTField;
    private javax.swing.JLabel DNITitularJLabel;
    private javax.swing.JTextField DNITitularJTField;
    private javax.swing.JButton abrirJButton;
    private javax.swing.JCheckBox afectaAceraJCheckBox;
    private javax.swing.JCheckBox afectaAparcamientoJCheckBox;
    private javax.swing.JCheckBox afectaCalzadaJCheckBox;
    private javax.swing.JPanel anexosJPanel;
    private javax.swing.JScrollPane anexosJScrollPane;
    private javax.swing.JTable anexosJTable;
    private javax.swing.JLabel apellido1RepresentaAJLabel;
    private javax.swing.JTextField apellido1RepresentaAJTField;
    private javax.swing.JLabel apellido1TitularJLabel;
    private javax.swing.JTextField apellido1TitularJTField;
    private javax.swing.JLabel apellido2RepresentaAJLabel;
    private javax.swing.JTextField apellido2RepresentaAJTField;
    private javax.swing.JLabel apellido2TitularJLabel2;
    private javax.swing.JTextField apellido2TitularJTField;
    private javax.swing.JScrollPane apunteJScrollPane;
    private javax.swing.JTextArea apunteJTArea;
    private javax.swing.JLabel areaOcupacionJLabel;
    private javax.swing.JTextField areaOcupacionJTextField;
    private javax.swing.JLabel asuntoExpedienteJLabel;
    private javax.swing.JTextField asuntoExpedienteJTField;
    private javax.swing.JLabel asuntoJLabel;
    private javax.swing.JTextField asuntoJTField;
    private javax.swing.JPanel botoneraJPanel;
    private javax.swing.JButton buscarExpedienteJButton;
    private javax.swing.JLabel cPostalJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalJTextField;
    private javax.swing.JLabel cPostalRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalRepresentaAJTField;
    private javax.swing.JLabel cPostalTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalTitularJTField;
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
    private javax.swing.JPanel datosNotificacionRepresentaAJPanel;
    private javax.swing.JPanel datosNotificacionTitularJPanel;
    private javax.swing.JPanel datosNotificacionesJPanel;
    private javax.swing.JLabel datosNotificarPorJLabel;
    private javax.swing.JTextField datosNotificarPorJTField;
    private javax.swing.JPanel datosPersonalesRepresentaAJPanel;
    private javax.swing.JPanel datosPersonalesTitularJPanel;
    private javax.swing.JPanel datosSolicitudJPanel;
    private javax.swing.JTabbedPane datosSolicitudJTabbedPane;
    private javax.swing.JScrollPane descEventoJScrollPane;
    private javax.swing.JTextArea descEventoJTArea;
    private javax.swing.JPanel editorMapaJPanel;
    private javax.swing.JLabel emailRepresentaAJLabel;
    private javax.swing.JTextField emailRepresentaAJTField;
    private javax.swing.JLabel emailTitularJLabel;
    private javax.swing.JTextField emailTitularJTField;
    private javax.swing.JPanel emplazamientoJPanel;
    private javax.swing.JLabel escaleraRepresentaAJLabel;
    private javax.swing.JTextField escaleraRepresentaAJTField;
    private javax.swing.JLabel escaleraTitularJLabel;
    private javax.swing.JTextField escaleraTitularJTField;
    private javax.swing.JLabel estadoExpedienteJLabel;
    private javax.swing.JTextField estadoExpedienteJTextField;
    private javax.swing.JLabel estadoJLabel;
    private javax.swing.JTextField estadoJTextField;
    private javax.swing.JPanel eventosJPanel;
    private javax.swing.JScrollPane eventosJScrollPane;
    private javax.swing.JTable eventosJTable;
    private javax.swing.JPanel expedienteJPanel;
    private javax.swing.JLabel faxRepresentaAJLabel;
    private javax.swing.JTextField faxRepresentaAJTField;
    private javax.swing.JLabel faxTitularJLabel;
    private javax.swing.JTextField faxTitularJTField;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JTextField fechaAperturaJTField;
    private javax.swing.JLabel fechaSolicitudJLabel;
    private javax.swing.JTextField fechaSolicitudJTField;
    private javax.swing.JLabel finalizaJLabel;
    private javax.swing.JTextField finalizaPorJTextField;
    private javax.swing.JCheckBox habilesJCheckBox;
    private javax.swing.JLabel habilesJLabel;
    private javax.swing.JPanel historicoJPanel;
    private javax.swing.JScrollPane historicoJScrollPane;
    private javax.swing.JTable historicoJTable;
    private javax.swing.JTextField horaFinJTextField;
    private javax.swing.JTextField horaInicioJTextField;
    private javax.swing.JLabel inicioJLabel;
    private javax.swing.JTextField inicioJTField;
    private javax.swing.JTextField letraJTextField;
    private javax.swing.JLabel letraRepresentaAJLabel;
    private javax.swing.JTextField letraRepresentaAJTField;
    private javax.swing.JLabel letraTitularJLabel;
    private javax.swing.JTextField letraTitularJTField;
    private javax.swing.JLabel metros2JLabel;
    private javax.swing.JTextField metros2aceraJTextField;
    private javax.swing.JTextField metros2aparcamientoJTextField;
    private javax.swing.JTextField metros2calzadaJTextField;
    private javax.swing.JButton modificarJButton;
    
    private javax.swing.JButton publicarJButton;
    
    private javax.swing.JLabel motivoJLabel;
    private javax.swing.JTextField motivoJTField;
    private javax.swing.JLabel movilRepresentaAJLabel;
    private javax.swing.JTextField movilRepresentaAJTField;
    private javax.swing.JLabel movilTitularJLabel;
    private javax.swing.JTextField movilTitularJTField;
    private javax.swing.JTextField municipioJTextField;
    private javax.swing.JLabel municipioRepresentaAJLabel;
    private javax.swing.JTextField municipioRepresentaAJTField;
    private javax.swing.JLabel municipioTitularJLabel;
    private javax.swing.JTextField municipioTitularJTField;
    private javax.swing.JCheckBox necesitaObraJCheckBox;
    private javax.swing.JLabel nombreRepresentaAJLabel;
    private javax.swing.JTextField nombreRepresentaAJTField;
    private javax.swing.JLabel nombreTitularJLabel;
    private javax.swing.JTextField nombreTitularJTField;
    private javax.swing.JLabel nombreViaJLabel;
    private javax.swing.JTextField nombreViaJTextField;
    private javax.swing.JLabel nombreViaRepresentaAJLabel;
    private javax.swing.JTextField nombreViaRepresentaAJTField;
    private javax.swing.JLabel nombreViaTitularJLabel;
    private javax.swing.JTextField nombreViaTitularJTField;
    private javax.swing.JLabel notaJLabel;
    private javax.swing.JPanel notificacionesJPanel;
    private javax.swing.JScrollPane notificacionesJScrollPane;
    private javax.swing.JTable notificacionesJTable;
    private javax.swing.JCheckBox notificarRepresentaAJCheckBox;
    private javax.swing.JLabel notificarRepresentaAJLabel;
    private javax.swing.JCheckBox notificarTitularJCheckBox;
    private javax.swing.JLabel notificarTitularJLabel;
    private javax.swing.JLabel numDiasJLabel;
    private javax.swing.JTextField numDiasJTField;
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JTextField numExpedienteJTextField;
    private javax.swing.JLabel numFoliosJLabel;
    private javax.swing.JTextField numFoliosJTField;
    private javax.swing.JLabel numMesasJLabel;
    private javax.swing.JTextField numMesasJTextField;
    private javax.swing.JLabel numSillasJLabel;
    private javax.swing.JTextField numSillasJTextField;
    private javax.swing.JLabel numeroViaJLabel;
    private javax.swing.JTextField numeroViaJTextField;
    private javax.swing.JLabel numeroViaRepresentaAJLabel;
    private javax.swing.JTextField numeroViaRepresentaAJTField;
    private javax.swing.JLabel numeroViaTitularJLabel;
    private javax.swing.JTextField numeroViaTitularJTField;
    private javax.swing.JLabel observacionesExpedienteJLabel;
    private javax.swing.JScrollPane observacionesExpedienteJScrollPane;
    private javax.swing.JTextArea observacionesExpedienteJTArea;
    private javax.swing.JLabel observacionesJLabel;
    private javax.swing.JScrollPane observacionesJScrollPane;
    private javax.swing.JTextArea observacionesJTArea;
    private javax.swing.JTextField plantaJTextField;
    private javax.swing.JLabel plantaRepresentaAJLabel;
    private javax.swing.JTextField plantaRepresentaAJTField;
    private javax.swing.JLabel plantaTitularJLabel;
    private javax.swing.JTextField plantaTitularJTField;
    private javax.swing.JLabel plazoVencimientoJLabel;
    private javax.swing.JTextField plazoVencimientoJTField;
    private javax.swing.JTextField portalJTextField;
    private javax.swing.JLabel portalRepresentaAJLabel;
    private javax.swing.JTextField portalRepresentaAJTField;
    private javax.swing.JLabel portalTitularJLabel;
    private javax.swing.JTextField portalTitularJTField;
    private javax.swing.JLabel provinciaJLabel;
    private javax.swing.JTextField provinciaJTextField;
    private javax.swing.JLabel provinciaRepresentaAJLabel;
    private javax.swing.JTextField provinciaRepresentaAJTField;
    private javax.swing.JLabel provinciaTitularJLabel;
    private javax.swing.JTextField provinciaTitularJTField;
    private javax.swing.JScrollPane referenciasCatastralesJScrollPane;
    private javax.swing.JTable referenciasCatastralesJTable;
    private javax.swing.JPanel representaAJPanel;
    private javax.swing.JLabel responsableJLabel;
    private javax.swing.JTextField responsableJTField;
    private javax.swing.JButton salirJButton;
    private javax.swing.JLabel servicioExpedienteJLabel;
    private javax.swing.JTextField servicioExpedienteJTField;
    private javax.swing.JCheckBox silencioJCheckBox;
    private javax.swing.JLabel silencioJLabel;
    private javax.swing.JPanel solicitudJPanel;
    private javax.swing.JLabel horaJLabel;
    private javax.swing.JLabel telefonoRepresentaAJLabel;
    private javax.swing.JTextField telefonoRepresentaAJTField;
    private javax.swing.JLabel telefonoTitularJLabel;
    private javax.swing.JTextField telefonoTitularJTField;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoObraJLabel;
    private javax.swing.JTextField tipoObraJTextField;
    private javax.swing.JTextField tipoViaJTextField;
    private javax.swing.JTextField tipoViaNotificacionRepresentaAJTField;
    private javax.swing.JTextField tipoViaNotificacionTitularJTField;
    private javax.swing.JLabel tipoViaRepresentaAJLabel;
    private javax.swing.JLabel tipoViaTitularJLabel;
    private javax.swing.JPanel titularJPanel;
    private javax.swing.JLabel tramitacionJLabel;
    private javax.swing.JTextField tramitacionJTextField;
    private javax.swing.JLabel expedienteObraJLabel;
    private javax.swing.JLabel necesitaObraJLabel;
    private javax.swing.JLabel viaNotificacionRepresentaAJLabel;
    private javax.swing.JTextField viaNotificacionRepresentaAJTField;
    private javax.swing.JLabel viaNotificacionTitularJLabel;
    private javax.swing.JTextField viaNotificacionTitularJTField;
    private javax.swing.JButton tableToMapAllJButton;
	private javax.swing.JButton tableToMapOneJButton;
    private javax.swing.JButton jButtonGenerarFicha;
    private javax.swing.JTextField entregadaAJTField;
    private javax.swing.JLabel entregadaAJLabel;
    private javax.swing.JButton generarFichaJButton;
    private JPanelCallesAfectadas jPanelCallesAfectadas;
    private javax.swing.JLabel afectaJLabel;
    private javax.swing.JButton alfrescoJButton;
    
    // End of variables declaration//GEN-END:variables

}
