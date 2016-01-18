package com.geopista.app.licencias.actividad.consulta;

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

import com.geopista.app.licencias.documentacion.DocumentacionLicenciasJPanel;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.utilidades.*;
import com.geopista.app.licencias.tableModels.*;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.licencias.*;
import com.geopista.app.licencias.actividad.datosActividad.DatosActividadJPanel;
import com.geopista.app.licencias.actividad.CConstantesLicenciasActividad;
import com.geopista.app.licencias.actividad.MainActividad;
import com.geopista.app.printer.GeopistaPrintable;
import com.geopista.app.printer.FichasDisponibles;
import com.geopista.editor.GeopistaEditor;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.*;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import org.apache.log4j.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * @author charo
 */
public class CConsultaLicencias extends javax.swing.JInternalFrame implements IMultilingue{

	private Vector _vNotificaciones = new Vector();
	private Vector _vEventos = new Vector();
	private Vector _vHistorico = new Vector();

    //private Vector _vReferenciasCatastrales= new Vector();

	Logger logger = Logger.getLogger(CConsultaLicencias.class);
    private ResourceBundle literales;

    /** Ordenacion de tablas */
    TableSorted notificacionesSorted= new TableSorted();
    TableSorted eventosSorted= new TableSorted();
    TableSorted historicoSorted= new TableSorted();
    int notificacionesHiddenCol= 6;
    int eventosHiddenCol= 5;
    int historicoHiddenCol= 4;


	/**
	 * Creates new form CCreacionLicencias
	 */
	public CConsultaLicencias(final JFrame desktop, final String numExpediente, final ResourceBundle literales) {
        this.literales=literales;
		this.desktop = desktop;
        CUtilidadesComponentes.menuLicenciasSetEnabled(false, this.desktop);

        /***para sacar la ventana de espera**/
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
                                    configureComponents();
                                    annadirPestanas(literales);
                                    renombrarComponentes(literales);

                                    /** Solicitud */
                                    setEnabledDatosSolicitud(false);
                                    /** datos actividad */
                                    datosActividadJPanel.setEnabled(false);
                                    /** Expediente */
                                    setEnabledDatosExpediente(false);
                                    /** Solicitante */
                                    setEnabledDatosSolicitante(false);
                                    /** Representante */
                                    setEnabledDatosRepresentante(false);
                                    /** Tecnico */
                                    setEnabledDatosTecnico(false);
                                    /** Promotor */
                                    setEnabledDatosPromotor(false);
                                    progressDialog.report(literales.getString("Licencias.Tag1"));
                                     if (CUtilidadesComponentes.showGeopistaMap(desktop,editorMapaJPanel, MainActividad.geopistaEditor, 273, false))
                                    {
                                        GeopistaLayer layer=(GeopistaLayer)MainActividad.geopistaEditor.getLayerManager().getLayer("parcelas");
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

                                    /** Para poder cargar la consulta en caso de que en la modificacion el expediente este bloqueado. */
                                    if (numExpediente != null) {
                                        numExpedienteJTField.setText(numExpediente);
                                        consultarJButtonActionPerformed();
                                    }
                                }finally{
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

		if (MainActividad.geopistaEditor == null) MainActividad.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");
        buscarExpedienteJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);

        generarFichaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoGenerarFicha);

        initTablas();
		return true;
    }
    private void initTablas(){
        try
        {
		    String[] columnNamesNotificaciones = {literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum6"),
                                              literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum1"),
											  literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum2"),
											  literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum3"),
											  literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum4"),
											  literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum5"),
                                              "HIDDEN"};

		    CNotificacionesConsultaTableModel.setColumnNames(columnNamesNotificaciones);
        }catch(Exception e)
        {
            logger.error("Error al inicializa cabeceras:",e);
        }
		notificacionesJTableModel = new CNotificacionesConsultaTableModel();
        notificacionesSorted= new TableSorted(notificacionesJTableModel);
        notificacionesSorted.setTableHeader(notificacionesJTable.getTableHeader());
		notificacionesJTable.setModel(notificacionesSorted);
		notificacionesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		notificacionesJTable.setCellSelectionEnabled(false);
		notificacionesJTable.setColumnSelectionAllowed(false);
		notificacionesJTable.setRowSelectionAllowed(true);
        notificacionesJTable.getTableHeader().setReorderingAllowed(false);

        notificacionesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i=0; i< notificacionesJTable.getColumnCount(); i++){
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



        try
        {
		    String[] columnNamesEventos = {literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum1"),
									   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum2"),
									   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum3"),
									   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum4"),
									   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum5"),
                                       "HIDDEN"};

		    CEventosConsultaTableModel.setColumnNames(columnNamesEventos);
        }catch(Exception e)
        {
               logger.error("Error al inicializa cabeceras:",e);
        }

		_eventosTableModel = new CEventosConsultaTableModel();
        eventosSorted= new TableSorted(_eventosTableModel);
        eventosSorted.setTableHeader(eventosJTable.getTableHeader());
		eventosJTable.setModel(eventosSorted);
		eventosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		eventosJTable.setCellSelectionEnabled(false);
		eventosJTable.setColumnSelectionAllowed(false);
		eventosJTable.setRowSelectionAllowed(true);
        eventosJTable.getTableHeader().setReorderingAllowed(false);

        eventosJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i=0; i< eventosJTable.getColumnCount(); i++){
            TableColumn column = eventosJTable.getColumnModel().getColumn(i);
            if (i ==2){
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



        try
        {
            String[] columnNamesHistorico = {//literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum1"),
                                             literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum2"),
                                             literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum3"),
                                             literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum4"),
                                             literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum5"),
                                             "HIDDEN"};

            CHistoricoModificacionTableModel.setColumnNames(columnNamesHistorico);
        }catch(Exception e)
        {
               logger.error("Error al inicializa cabeceras:",e);
        }

		_historicoTableModel = new CHistoricoModificacionTableModel();
        historicoSorted= new TableSorted(_historicoTableModel);
        historicoSorted.setTableHeader(historicoJTable.getTableHeader());                        
		historicoJTable.setModel(historicoSorted);
		historicoJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		historicoJTable.setCellSelectionEnabled(false);
		historicoJTable.setColumnSelectionAllowed(false);
		historicoJTable.setRowSelectionAllowed(true);
        historicoJTable.getTableHeader().setReorderingAllowed(false);

        historicoJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i=0; i< historicoJTable.getColumnCount(); i++){
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




        try
        {
		    String[] columnNamesRef = {literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum1"),
								   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum2"),
								   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum3"),
								   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum4"),
								   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum5"),
								   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum6"),
								   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum7"),
								   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum8"),
								   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum9"),
                                   literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum10")};

		    referenciasCatastralesJTableModel = new CSearchDialogTableModel(columnNamesRef);
            referenciasCatastralesJTable.setModel(referenciasCatastralesJTableModel);

        }catch(Exception e)
        {
               logger.error("Error al inicializa cabeceras:",e);
        }



		referenciasCatastralesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		referenciasCatastralesJTable.setCellSelectionEnabled(false);
		referenciasCatastralesJTable.setColumnSelectionAllowed(false);
		referenciasCatastralesJTable.setRowSelectionAllowed(true);
        referenciasCatastralesJTable.getTableHeader().setReorderingAllowed(false);

        referenciasCatastralesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i=0; i< referenciasCatastralesJTable.getColumnCount(); i++){
            TableColumn column = referenciasCatastralesJTable.getColumnModel().getColumn(i);

            if (i==1){
                column.setMinWidth(75);
                column.setMaxWidth(150);
                column.setWidth(75);
                column.setPreferredWidth(75);
            }else if (i>2){
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
        jButtonWorkFlow= new javax.swing.JButton();
        modificarJButton = new javax.swing.JButton();
        publicarJButton = new javax.swing.JButton();
        datosSolicitudJTabbedPane = new javax.swing.JTabbedPane();
        jTabbedPaneSolicitud= new JTabbedPane();
        expedienteJPanel = new javax.swing.JPanel();
        estadoExpedienteJLabel = new javax.swing.JLabel();
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
        estadoExpedienteJTextField = new javax.swing.JTextField();
        silencioJLabel = new javax.swing.JLabel();
        notaJLabel = new javax.swing.JLabel();
        observacionesExpedienteJScrollPane = new javax.swing.JScrollPane();
        observacionesExpedienteJTArea = new javax.swing.JTextArea();
        tramitacionJTextField = new javax.swing.JTextField();
        responsableJLabel = new javax.swing.JLabel();
        responsableJTextField = new javax.swing.JTextField();
        solicitudJPanel = new javax.swing.JPanel();
        datosSolicitudJPanel = new javax.swing.JPanel();
        tipoObraJLabel = new javax.swing.JLabel();
         jCheckBoxActividadNoCalificada= new javax.swing.JCheckBox();
        unidadTJLabel = new javax.swing.JLabel();
        unidadRJLabel = new javax.swing.JLabel();
        motivoJLabel = new javax.swing.JLabel();
        asuntoJLabel = new javax.swing.JLabel();
        fechaSolicitudJLabel = new javax.swing.JLabel();
        observacionesJLabel = new javax.swing.JLabel();
        unidadTJTField = new javax.swing.JTextField();
        unidadRJTField = new javax.swing.JTextField();
        motivoJTField = new javax.swing.JTextField();
        asuntoJTField = new javax.swing.JTextField();
        fechaSolicitudJTField = new javax.swing.JTextField();
        observacionesJScrollPane = new javax.swing.JScrollPane();
        observacionesJTArea = new javax.swing.JTextArea();
        tasaJLabel = new javax.swing.JLabel();
        tipoObraJTextField = new javax.swing.JTextField();
        numRegistroJTField = new javax.swing.JTextField();
        numRegistroJLabel = new javax.swing.JLabel();
        impuestoJLabel = new javax.swing.JLabel();
        fechaLimiteObraJLabel = new javax.swing.JLabel();
        fechaLimiteObraJTField = new javax.swing.JTextField();
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
        faxTecnicoJTField = new javax.swing.JTextField();
        telefonoTecnicoJTField = new javax.swing.JTextField();
        movilTecnicoJTField = new javax.swing.JTextField();
        emailTecnicoJTField = new javax.swing.JTextField();
        nombreViaTecnicoJTField = new javax.swing.JTextField();
        numeroViaTecnicoJTField = new javax.swing.JTextField();
        plantaTecnicoJTField = new javax.swing.JTextField();
        portalTecnicoJTField = new javax.swing.JTextField();
        escaleraTecnicoJTField = new javax.swing.JTextField();
        letraTecnicoJTField = new javax.swing.JTextField();
        cPostalTecnicoJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioTecnicoJTField = new javax.swing.JTextField();
        provinciaTecnicoJTField = new javax.swing.JTextField();
        notificarTecnicoJCheckBox = new javax.swing.JCheckBox();
        notificarTecnicoJLabel = new javax.swing.JLabel();
        viaNotificacionTecnicoJTField = new javax.swing.JTextField();
        tipoViaNotificacionTecnicoJTField = new javax.swing.JTextField();
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
        faxPromotorJTField = new javax.swing.JTextField();
        telefonoPromotorJTField = new javax.swing.JTextField();
        movilPromotorJTField = new javax.swing.JTextField();
        emailPromotorJTField = new javax.swing.JTextField();
        nombreViaPromotorJTField = new javax.swing.JTextField();
        numeroViaPromotorJTField = new javax.swing.JTextField();
        plantaPromotorJTField = new javax.swing.JTextField();
        portalPromotorJTField = new javax.swing.JTextField();
        escaleraPromotorJTField = new javax.swing.JTextField();
        letraPromotorJTField = new javax.swing.JTextField();
        cPostalPromotorJTField = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        municipioPromotorJTField = new javax.swing.JTextField();
        provinciaPromotorJTField = new javax.swing.JTextField();
        notificarPromotorJCheckBox = new javax.swing.JCheckBox();
        notificarPromotorJLabel = new javax.swing.JLabel();
        viaNotificacionPromotorJTField = new javax.swing.JTextField();
        tipoViaNotificacionPromotorJTField = new javax.swing.JTextField();
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
        entregadaAJTField = new javax.swing.JTextField();
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
        generarFichaJButton = new javax.swing.JButton();
        editorMapaJPanel = new javax.swing.JPanel();
        /** Documentacion **/
        documentacionJPanel= new DocumentacionLicenciasJPanel(literales);
        documentacionJPanel.setConsulta();
        /** Informes **/
        jPanelInformes= new JPanelInformes(desktop, literales);
        /** Resolucion **/
        jPanelResolucion = new JPanelResolucion(desktop,literales);
        jLabelFinaliza= new JLabel();
        jTextFieldFinaliza= new JTextField();
        jTextFieldFinaliza.setEditable(false);

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

        templateJPanel.setLayout(new BorderLayout());

        botoneraJPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        salirJButton.setText("Salir");
        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirJButtonActionPerformed();
            }
        });

        modificarJButton.setText("Modificar");
        modificarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarJButtonActionPerformed();
            }
        });

        modificarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(modificarJButton);
        jButtonGenerarFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                        generarFicha();
                    }
        });
        
        publicarJButton.setText("Publicar");
        publicarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publicarJButtonActionPerformed();
            }
        });
        publicarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(publicarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 90, -1));

        
        botoneraJPanel.add(jButtonGenerarFicha, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 90, -1));
        jButtonGenerarFicha.setPreferredSize(new Dimension(120,30));

        jButtonWorkFlow.addActionListener(new java.awt.event.ActionListener() {
                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                              verWorkFlow();
                          }
        });
        jButtonWorkFlow.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(jButtonWorkFlow, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 90, -1));

        salirJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 90, -1));

        templateJPanel.add(botoneraJPanel, BorderLayout.SOUTH);

        datosSolicitudJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        datosSolicitudJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));

        jTabbedPaneSolicitud.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPaneSolicitud.setFont(new java.awt.Font("Arial", 0, 10));

        expedienteJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expedienteJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Expediente"));
        expedienteJPanel.setAutoscrolls(true);
        estadoExpedienteJLabel.setText("Estado:");
        expedienteJPanel.add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 160, 20));

        numExpedienteJLabel.setText("Num. Expediente:");
        expedienteJPanel.add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 160, 20));

        servicioExpedienteJLabel.setText("Servicio Encargado:");
        expedienteJPanel.add(servicioExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 121, 160, 20));

        tramitacionJLabel.setText("Tipo de Tramitaci\u00f3n:");
        expedienteJPanel.add(tramitacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 142, 160, 20));

        asuntoExpedienteJLabel.setText("Asunto:");
        expedienteJPanel.add(asuntoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 163, 160, 20));

        fechaAperturaJLabel.setText("Fecha Apertura:");
        expedienteJPanel.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 184, 160, 20));

        observacionesExpedienteJLabel.setText("Observaciones:");
        expedienteJPanel.add(observacionesExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 170, 20));

        expedienteJPanel.add(servicioExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 121, 300, -1));

        expedienteJPanel.add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 280, -1));

        expedienteJPanel.add(asuntoExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 163, 300, -1));

        expedienteJPanel.add(fechaAperturaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 184, 300, -1));

        inicioJLabel.setText("Forma de inicio:");
        expedienteJPanel.add(inicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 205, 160, 20));

        expedienteJPanel.add(inicioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 205, 300, -1));

        silencioJCheckBox.setEnabled(false);
        expedienteJPanel.add(silencioJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 226, -1, -1));

        buscarExpedienteJButton.setIcon(new javax.swing.ImageIcon(""));
        buscarExpedienteJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarExpedienteJButtonActionPerformed();
            }
        });

        expedienteJPanel.add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 20, 20));

        consultarJButton.setText("Consultar");
        consultarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarJButtonActionPerformed();
            }
        });

        expedienteJPanel.add(consultarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 90, -1));

        expedienteJPanel.add(estadoExpedienteJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 300, -1));

        silencioJLabel.setText("Silencio administrativo:");
        expedienteJPanel.add(silencioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 226, 160, 20));
        expedienteJPanel.add(jTextFieldFinaliza, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 550, 300, -1));
        expedienteJPanel.add(jLabelFinaliza, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, -1, -1));

        notaJLabel.setFont(new java.awt.Font("Arial", 0, 10));
        notaJLabel.setText("*Nota: Chequeado para silencio administrativo positivo.");
        expedienteJPanel.add(notaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 226, 270, 20));

        observacionesExpedienteJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        observacionesExpedienteJTArea.setEditable(false);
        observacionesExpedienteJTArea.setLineWrap(true);
        observacionesExpedienteJTArea.setRows(3);
        observacionesExpedienteJTArea.setTabSize(4);
        observacionesExpedienteJTArea.setWrapStyleWord(true);
        observacionesExpedienteJTArea.setBorder(null);
        observacionesExpedienteJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        observacionesExpedienteJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        observacionesExpedienteJScrollPane.setViewportView(observacionesExpedienteJTArea);
        expedienteJPanel.add(observacionesExpedienteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 300, 90));

        expedienteJPanel.add(tramitacionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 142, 300, -1));

        expedienteJPanel.add(jPanelResolucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 370, 500, 175));

        responsableJLabel.setText("Responsable:");
        expedienteJPanel.add(responsableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 170, 20));
        expedienteJPanel.add(responsableJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 300, -1));

        solicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder("Datos solicitud"));
        datosSolicitudJPanel.setAutoscrolls(true);
        tipoObraJLabel.setText("Tipo Obra:");
        datosSolicitudJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 41, 150, 20));

        unidadTJLabel.setText("Unidad Tramitadora:");
        datosSolicitudJPanel.add(unidadTJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 62, 150, 20));

        unidadRJLabel.setText("Unidad de Registro:");
        datosSolicitudJPanel.add(unidadRJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 83, 150, 20));

        motivoJLabel.setText("Motivo:");
        datosSolicitudJPanel.add(motivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 104, 150, 20));

        asuntoJLabel.setText("Asunto:");
        datosSolicitudJPanel.add(asuntoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 125, 150, 20));

        fechaSolicitudJLabel.setText("Fecha Solicitud:");
        datosSolicitudJPanel.add(fechaSolicitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 146, 150, 20));

        observacionesJLabel.setText("Observaciones:");
        datosSolicitudJPanel.add(observacionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 150, 20));

        datosSolicitudJPanel.add(unidadTJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 62, 310, -1));

        datosSolicitudJPanel.add(unidadRJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 83, 310, -1));

        datosSolicitudJPanel.add(motivoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 104, 310, -1));

        datosSolicitudJPanel.add(asuntoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 125, 310, -1));

        datosSolicitudJPanel.add(fechaSolicitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 146, 90, -1));

        observacionesJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        observacionesJTArea.setEditable(false);
        observacionesJTArea.setLineWrap(true);
        observacionesJTArea.setRows(3);
        observacionesJTArea.setTabSize(4);
        observacionesJTArea.setWrapStyleWord(true);
        observacionesJTArea.setBorder(null);
        observacionesJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        observacionesJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        observacionesJScrollPane.setViewportView(observacionesJTArea);

        datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 310, 80));

        /** cnae */
        cnaeJLabel = new javax.swing.JLabel();
        datosSolicitudJPanel.add(cnaeJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 167, 150, 20));
        cnaeTField= new com.geopista.app.utilidades.TextField(16);
        datosSolicitudJPanel.add(cnaeTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 167, 310, -1));

        /** tasa */
        tasaJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tasaJLabel.setText("Tasa:");
        datosSolicitudJPanel.add(tasaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 188, 90, 20));
        tasaTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
        datosSolicitudJPanel.add(tasaTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 188, 100, -1));

        /** impuesto */
        impuestoJLabel.setText("Impuesto:");
        datosSolicitudJPanel.add(impuestoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 188, 150, 20));
        impuestoTextField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Integer(99999999), true);
        datosSolicitudJPanel.add(impuestoTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 188, 100, -1));


        datosSolicitudJPanel.add(tipoObraJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 41, 180, -1));
        datosSolicitudJPanel.add(jCheckBoxActividadNoCalificada, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 40, -1, -1));
        jCheckBoxActividadNoCalificada.setEnabled(false);

        numRegistroJTField.setEnabled(false);
        datosSolicitudJPanel.add(numRegistroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 310, -1));

        numRegistroJLabel.setText("Num. Registro:");
        datosSolicitudJPanel.add(numRegistroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 20));

        fechaLimiteObraJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        fechaLimiteObraJLabel.setText("Fecha L\u00edmite Obra:");
        datosSolicitudJPanel.add(fechaLimiteObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 146, 110, 20));

        datosSolicitudJPanel.add(fechaLimiteObraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 146, 90, -1));

        solicitudJPanel.add(datosSolicitudJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 300));

        emplazamientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder("Emplazamiento"));
        nombreViaJLabel.setText("Tipo v\u00eda / Nombre V\u00eda:");
        emplazamientoJPanel.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, 20));

        tipoViaJTextField.setEnabled(false);
        emplazamientoJPanel.add(tipoViaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 110, -1));

        nombreViaJTextField.setEnabled(false);
        emplazamientoJPanel.add(nombreViaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 190, -1));

        numeroViaJLabel.setText("N\u00ba / Portal / Planta /Letra:");
        emplazamientoJPanel.add(numeroViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 41, 130, 20));

        numeroViaJTextField.setEnabled(false);
        emplazamientoJPanel.add(numeroViaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 41, 70, -1));

        portalJTextField.setEnabled(false);
        emplazamientoJPanel.add(portalJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 41, 70, -1));

        plantaJTextField.setEnabled(false);
        emplazamientoJPanel.add(plantaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 41, 70, -1));

        letraJTextField.setEnabled(false);
        emplazamientoJPanel.add(letraJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 41, 70, -1));

        cPostalJLabel.setText("C.P. / Municipio: ");
        emplazamientoJPanel.add(cPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 62, 130, 20));

        cPostalJTextField.setEnabled(false);
        emplazamientoJPanel.add(cPostalJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 62, 70, -1));

        municipioJTextField.setEnabled(false);
        emplazamientoJPanel.add(municipioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 62, 230, -1));

        provinciaJLabel.setText("Provincia:");
        emplazamientoJPanel.add(provinciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 83, 130, 20));

        provinciaJTextField.setEnabled(false);
        emplazamientoJPanel.add(provinciaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 83, 310, -1));

        referenciasCatastralesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Referencia catastral", "Tipo Vía", "Vía Pública", "Nº"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        referenciasCatastralesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        referenciasCatastralesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                referenciasCatastralesJTableMouseClicked();
            }
        });

        referenciasCatastralesJScrollPane.setViewportView(referenciasCatastralesJTable);

        emplazamientoJPanel.add(referenciasCatastralesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 470, 120));

        solicitudJPanel.add(emplazamientoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 305, 518, 242));

        titularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTitularJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNITitularJLabel.setText("(*) DNI/CIF:");
        datosPersonalesTitularJPanel.add(DNITitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 20));

        datosPersonalesTitularJPanel.add(DNITitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 300, -1));

        nombreTitularJLabel.setText("(*) Nombre:");
        datosPersonalesTitularJPanel.add(nombreTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 120, 20));

        datosPersonalesTitularJPanel.add(nombreTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 51, 300, -1));

        apellido1TitularJLabel.setText("Apellido1:");
        datosPersonalesTitularJPanel.add(apellido1TitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 120, 20));

        apellido2TitularJLabel2.setText("Apellido2:");
        datosPersonalesTitularJPanel.add(apellido2TitularJLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 120, 20));

        datosPersonalesTitularJPanel.add(apellido1TitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 72, 300, -1));

        datosPersonalesTitularJPanel.add(apellido2TitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 93, 300, -1));

        titularJPanel.add(datosPersonalesTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 127));

        datosNotificacionTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionTitularJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionTitularJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionTitularJPanel.add(viaNotificacionTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 20));

        faxTitularJLabel.setText("Fax:");
        datosNotificacionTitularJPanel.add(faxTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 120, 20));

        telefonoTitularJLabel.setText("Tel\u00e9fono:");
        datosNotificacionTitularJPanel.add(telefonoTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 120, 20));

        movilTitularJLabel.setText("M\u00f3vil:");
        datosNotificacionTitularJPanel.add(movilTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 120, 20));

        emailTitularJLabel.setText("Email:");
        datosNotificacionTitularJPanel.add(emailTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 120, 20));

        tipoViaTitularJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionTitularJPanel.add(tipoViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 120, 20));

        nombreViaTitularJLabel.setText("(*) Nombre:");
        datosNotificacionTitularJPanel.add(nombreViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 120, 20));

        numeroViaTitularJLabel.setText("(*) N\u00famero:");
        datosNotificacionTitularJPanel.add(numeroViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 175, 120, 20));

        portalTitularJLabel.setText("Portal:");
        datosNotificacionTitularJPanel.add(portalTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 238, 120, 20));

        plantaTitularJLabel.setText("Planta:");
        datosNotificacionTitularJPanel.add(plantaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 196, 120, 20));

        escaleraTitularJLabel.setText("Escalera:");
        datosNotificacionTitularJPanel.add(escaleraTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 259, 120, 20));

        letraTitularJLabel.setText("Letra:");
        datosNotificacionTitularJPanel.add(letraTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 217, 120, 20));

        cPostalTitularJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionTitularJPanel.add(cPostalTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 285, 120, 20));

        municipioTitularJLabel.setText("(*) Municipio:");
        datosNotificacionTitularJPanel.add(municipioTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 306, 120, 20));

        provinciaTitularJLabel.setText("(*) Provincia:");
        datosNotificacionTitularJPanel.add(provinciaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 327, 120, 20));

        datosNotificacionTitularJPanel.add(faxTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 41, 300, -1));

        datosNotificacionTitularJPanel.add(telefonoTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 62, 300, -1));

        datosNotificacionTitularJPanel.add(movilTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 83, 300, -1));

        datosNotificacionTitularJPanel.add(emailTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 104, 300, -1));

        datosNotificacionTitularJPanel.add(nombreViaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 146, 300, -1));

        datosNotificacionTitularJPanel.add(numeroViaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 175, 150, -1));

        datosNotificacionTitularJPanel.add(plantaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 196, 150, -1));

        datosNotificacionTitularJPanel.add(portalTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 238, 150, -1));

        datosNotificacionTitularJPanel.add(escaleraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 259, 150, -1));

        datosNotificacionTitularJPanel.add(letraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 217, 150, -1));

        datosNotificacionTitularJPanel.add(cPostalTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 285, 300, -1));

        datosNotificacionTitularJPanel.add(municipioTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 306, 300, -1));

        datosNotificacionTitularJPanel.add(provinciaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 327, 300, -1));

        notificarTitularJCheckBox.setEnabled(false);
        datosNotificacionTitularJPanel.add(notificarTitularJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 360, -1, -1));

        notificarTitularJLabel.setText("Notificar propietario:");
        datosNotificacionTitularJPanel.add(notificarTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 120, 20));

        datosNotificacionTitularJPanel.add(viaNotificacionTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 300, -1));

        datosNotificacionTitularJPanel.add(tipoViaNotificacionTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 125, 300, -1));

        titularJPanel.add(datosNotificacionTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 127, 518, 420));

        representaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNIRepresenaAJLabel.setText("(*) DNI/CIF:");
        datosPersonalesRepresentaAJPanel.add(DNIRepresenaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 20));

        datosPersonalesRepresentaAJPanel.add(DNIRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 300, -1));

        nombreRepresentaAJLabel.setText("(*) Nombre:");
        datosPersonalesRepresentaAJPanel.add(nombreRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 120, 20));

        datosPersonalesRepresentaAJPanel.add(nombreRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 51, 300, -1));

        apellido1RepresentaAJLabel.setText("Apellido1:");
        datosPersonalesRepresentaAJPanel.add(apellido1RepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 120, 20));

        apellido2RepresentaAJLabel.setText("Apellido2:");
        datosPersonalesRepresentaAJPanel.add(apellido2RepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 120, 20));

        datosPersonalesRepresentaAJPanel.add(apellido1RepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 72, 300, -1));

        datosPersonalesRepresentaAJPanel.add(apellido2RepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 93, 300, -1));

        representaAJPanel.add(datosPersonalesRepresentaAJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 127));

        datosNotificacionRepresentaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionRepresentaAJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionRepresentaAJPanel.add(viaNotificacionRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 20));

        faxRepresentaAJLabel.setText("Fax:");
        datosNotificacionRepresentaAJPanel.add(faxRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 120, 20));

        telefonoRepresentaAJLabel.setText("Tel\u00e9fono:");
        datosNotificacionRepresentaAJPanel.add(telefonoRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 120, 20));

        movilRepresentaAJLabel.setText("M\u00f3vil:");
        datosNotificacionRepresentaAJPanel.add(movilRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 120, 20));

        emailRepresentaAJLabel.setText("Email:");
        datosNotificacionRepresentaAJPanel.add(emailRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 120, 20));

        tipoViaRepresentaAJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionRepresentaAJPanel.add(tipoViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 120, 20));

        nombreViaRepresentaAJLabel.setText("(*) Nombre:");
        datosNotificacionRepresentaAJPanel.add(nombreViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 120, 20));

        numeroViaRepresentaAJLabel.setText("(*) N\u00famero:");
        datosNotificacionRepresentaAJPanel.add(numeroViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 175, 120, 20));

        portalRepresentaAJLabel.setText("Portal:");
        datosNotificacionRepresentaAJPanel.add(portalRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 238, 120, 20));

        plantaRepresentaAJLabel.setText("Planta:");
        datosNotificacionRepresentaAJPanel.add(plantaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 196, 120, 20));

        escaleraRepresentaAJLabel.setText("Escalera:");
        datosNotificacionRepresentaAJPanel.add(escaleraRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 259, 120, 20));

        letraRepresentaAJLabel.setText("Letra:");
        datosNotificacionRepresentaAJPanel.add(letraRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 217, 120, 20));

        cPostalRepresentaAJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionRepresentaAJPanel.add(cPostalRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 285, 120, 20));

        municipioRepresentaAJLabel.setText("(*) Municipio:");
        datosNotificacionRepresentaAJPanel.add(municipioRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 306, 120, 20));

        provinciaRepresentaAJLabel.setText("(*) Provincia:");
        datosNotificacionRepresentaAJPanel.add(provinciaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 327, 120, 20));

        datosNotificacionRepresentaAJPanel.add(faxRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 41, 300, -1));

        datosNotificacionRepresentaAJPanel.add(telefonoRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 62, 300, -1));

        datosNotificacionRepresentaAJPanel.add(movilRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 83, 300, -1));

        datosNotificacionRepresentaAJPanel.add(emailRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 104, 300, -1));

        datosNotificacionRepresentaAJPanel.add(nombreViaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 146, 300, -1));

        datosNotificacionRepresentaAJPanel.add(numeroViaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 175, 150, -1));

        datosNotificacionRepresentaAJPanel.add(plantaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 196, 150, -1));

        datosNotificacionRepresentaAJPanel.add(portalRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 238, 150, -1));

        datosNotificacionRepresentaAJPanel.add(escaleraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 259, 150, -1));

        datosNotificacionRepresentaAJPanel.add(letraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 217, 150, -1));

        datosNotificacionRepresentaAJPanel.add(cPostalRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 285, 300, -1));

        datosNotificacionRepresentaAJPanel.add(municipioRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 306, 300, -1));

        datosNotificacionRepresentaAJPanel.add(provinciaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 327, 300, -1));

        notificarRepresentaAJCheckBox.setEnabled(false);
        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 360, -1, -1));

        notificarRepresentaAJLabel.setText("Notificar representante:");
        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 130, 20));

        datosNotificacionRepresentaAJPanel.add(viaNotificacionRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 300, -1));

        datosNotificacionRepresentaAJPanel.add(tipoViaNotificacionRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 125, 300, -1));

        representaAJPanel.add(datosNotificacionRepresentaAJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 127, 518, 420));

        tecnicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTecnicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNITecnicoJLabel.setText("(*) DNI/CIF:");
        datosPersonalesTecnicoJPanel.add(DNITecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 20));

        datosPersonalesTecnicoJPanel.add(DNITecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 300, -1));

        nombreTecnicoJLabel.setText("(*) Nombre:");
        datosPersonalesTecnicoJPanel.add(nombreTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 120, 20));

        datosPersonalesTecnicoJPanel.add(nombreTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 41, 300, -1));

        apellido1TecnicoJLabel.setText("Apellido1:");
        datosPersonalesTecnicoJPanel.add(apellido1TecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 120, 20));

        apellido2TecnicoJLabel.setText("Apellido2:");
        datosPersonalesTecnicoJPanel.add(apellido2TecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 120, 20));

        datosPersonalesTecnicoJPanel.add(apellido1TecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 62, 300, -1));

        datosPersonalesTecnicoJPanel.add(apellido2TecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 83, 300, -1));

        colegioTecnicoJLabel.setText("(*) Colegio:");
        datosPersonalesTecnicoJPanel.add(colegioTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 120, 20));

        visadoTecnicoJLabel.setText("(*) Visado:");
        datosPersonalesTecnicoJPanel.add(visadoTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 120, 20));

        titulacionTecnicoJLabel.setText("Titulaci\u00f3n:");
        datosPersonalesTecnicoJPanel.add(titulacionTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 120, 20));

        datosPersonalesTecnicoJPanel.add(colegioTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 104, 300, -1));

        datosPersonalesTecnicoJPanel.add(visadoTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 125, 300, -1));

        datosPersonalesTecnicoJPanel.add(titulacionTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 146, 300, -1));

        tecnicoJPanel.add(datosPersonalesTecnicoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 175));

        datosNotificacionTecnicoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionTecnicoJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionTecnicoJPanel.add(viaNotificacionTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 20));

        faxTecnicoJLabel.setText("Fax:");
        datosNotificacionTecnicoJPanel.add(faxTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 120, 20));

        telefonoTecnicoJLabel.setText("Tel\u00e9fono:");
        datosNotificacionTecnicoJPanel.add(telefonoTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 120, 20));

        movilTecnicoJLabel.setText("M\u00f3vil:");
        datosNotificacionTecnicoJPanel.add(movilTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 120, 20));

        emailTecnicoJLabel.setText("Email:");
        datosNotificacionTecnicoJPanel.add(emailTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 120, 20));

        tipoViaTecnicoJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionTecnicoJPanel.add(tipoViaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 120, 20));

        nombreViaTecnicoJLabel.setText("(*) Nombre:");
        datosNotificacionTecnicoJPanel.add(nombreViaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 120, 20));

        numeroViaTecnicoJLabel.setText("(*) N\u00famero:");
        datosNotificacionTecnicoJPanel.add(numeroViaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 120, 20));

        portalTecnicoJLabel.setText("Portal:");
        datosNotificacionTecnicoJPanel.add(portalTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 233, 120, 20));

        plantaTecnicoJLabel.setText("Planta:");
        datosNotificacionTecnicoJPanel.add(plantaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 191, 120, 20));

        escaleraTecnicoJLabel.setText("Escalera:");
        datosNotificacionTecnicoJPanel.add(escaleraTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 254, 120, 20));

        letraTecnicoJLabel.setText("Letra:");
        datosNotificacionTecnicoJPanel.add(letraTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 212, 120, 20));

        cPostalTecnicoJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionTecnicoJPanel.add(cPostalTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 120, 20));

        municipioTecnicoJLabel.setText("(*) Municipio:");
        datosNotificacionTecnicoJPanel.add(municipioTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 301, 120, 20));

        provinciaTecnicoJLabel.setText("(*) Provincia:");
        datosNotificacionTecnicoJPanel.add(provinciaTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 322, 120, 20));

        datosNotificacionTecnicoJPanel.add(faxTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 41, 300, -1));

        datosNotificacionTecnicoJPanel.add(telefonoTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 62, 300, -1));

        datosNotificacionTecnicoJPanel.add(movilTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 83, 300, -1));

        datosNotificacionTecnicoJPanel.add(emailTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 104, 300, -1));

        datosNotificacionTecnicoJPanel.add(nombreViaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 146, 300, -1));

        datosNotificacionTecnicoJPanel.add(numeroViaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 150, -1));

        datosNotificacionTecnicoJPanel.add(plantaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 191, 150, -1));

        datosNotificacionTecnicoJPanel.add(portalTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 233, 150, -1));

        datosNotificacionTecnicoJPanel.add(escaleraTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 254, 150, -1));

        datosNotificacionTecnicoJPanel.add(letraTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 212, 150, -1));

        datosNotificacionTecnicoJPanel.add(cPostalTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 300, -1));

        datosNotificacionTecnicoJPanel.add(municipioTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 301, 300, -1));

        datosNotificacionTecnicoJPanel.add(provinciaTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 322, 300, -1));

        notificarTecnicoJCheckBox.setEnabled(false);
        notificarTecnicoJCheckBox.setVisible(false);
        datosNotificacionTecnicoJPanel.add(notificarTecnicoJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 345, -1, -1));

        notificarTecnicoJLabel.setVisible(false);
        notificarTecnicoJLabel.setText("Notificar t\u00e9cnico:");
        datosNotificacionTecnicoJPanel.add(notificarTecnicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 345, 130, 20));

        datosNotificacionTecnicoJPanel.add(viaNotificacionTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 300, -1));

        datosNotificacionTecnicoJPanel.add(tipoViaNotificacionTecnicoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 125, 300, -1));

        tecnicoJPanel.add(datosNotificacionTecnicoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 175, 518, 372));

        promotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        DNIPromotorJLabel.setText("(*) DNI/CIF:");
        datosPersonalesPromotorJPanel.add(DNIPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 20));

        datosPersonalesPromotorJPanel.add(DNIPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 300, -1));

        nombrePromotorJLabel.setText("(*) Nombre:");
        datosPersonalesPromotorJPanel.add(nombrePromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 120, 20));

        datosPersonalesPromotorJPanel.add(nombrePromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 41, 300, -1));

        apellido1PromotorJLabel.setText("Apellido1:");
        datosPersonalesPromotorJPanel.add(apellido1PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 120, 20));

        apellido2PromotorJLabel.setText("Apellido2:");
        datosPersonalesPromotorJPanel.add(apellido2PromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 120, 20));

        datosPersonalesPromotorJPanel.add(apellido1PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 62, 300, -1));

        datosPersonalesPromotorJPanel.add(apellido2PromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 83, 300, -1));

        colegioPromotorJLabel.setText("(*) Colegio:");
        datosPersonalesPromotorJPanel.add(colegioPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 120, 20));

        visadoPromotorJLabel.setText("(*) Visado:");
        datosPersonalesPromotorJPanel.add(visadoPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 120, 20));

        titulacionPromotorJLabel.setText("Titulaci\u00f3n:");
        datosPersonalesPromotorJPanel.add(titulacionPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 120, 20));

        datosPersonalesPromotorJPanel.add(colegioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 104, 300, -1));

        datosPersonalesPromotorJPanel.add(visadoPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 125, 300, -1));

        datosPersonalesPromotorJPanel.add(titulacionPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 146, 300, -1));

        promotorJPanel.add(datosPersonalesPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 518, 175));

        datosNotificacionPromotorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionPromotorJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        viaNotificacionPromotorJLabel.setText("V\u00eda Notificaci\u00f3n:");
        datosNotificacionPromotorJPanel.add(viaNotificacionPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 120, 20));

        faxPromotorJLabel.setText("Fax:");
        datosNotificacionPromotorJPanel.add(faxPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 41, 120, 20));

        telefonoPromotorJLabel.setText("Tel\u00e9fono:");
        datosNotificacionPromotorJPanel.add(telefonoPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 120, 20));

        movilPromotorJLabel.setText("M\u00f3vil:");
        datosNotificacionPromotorJPanel.add(movilPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 83, 120, 20));

        emailPromotorJLabel.setText("Email:");
        datosNotificacionPromotorJPanel.add(emailPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 120, 20));

        tipoViaPromotorJLabel.setText("(*) Tipo V\u00eda:");
        datosNotificacionPromotorJPanel.add(tipoViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 120, 20));

        nombreViaPromotorJLabel.setText("(*) Nombre:");
        datosNotificacionPromotorJPanel.add(nombreViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 146, 120, 20));

        numeroViaPromotorJLabel.setText("(*) N\u00famero:");
        datosNotificacionPromotorJPanel.add(numeroViaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 120, 20));

        portalPromotorJLabel.setText("Portal:");
        datosNotificacionPromotorJPanel.add(portalPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 233, 120, 20));

        plantaPromotorJLabel.setText("Planta:");
        datosNotificacionPromotorJPanel.add(plantaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 191, 120, 20));

        escaleraPromotorJLabel.setText("Escalera:");
        datosNotificacionPromotorJPanel.add(escaleraPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 254, 120, 20));

        letraPromotorJLabel.setText("Letra:");
        datosNotificacionPromotorJPanel.add(letraPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 212, 120, 20));

        cPostalPromotorJLabel.setText("(*) C\u00f3digo Postal:");
        datosNotificacionPromotorJPanel.add(cPostalPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 120, 20));

        municipioPromotorJLabel.setText("(*) Municipio:");
        datosNotificacionPromotorJPanel.add(municipioPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 301, 120, 20));

        provinciaPromotorJLabel.setText("(*) Provincia:");
        datosNotificacionPromotorJPanel.add(provinciaPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 322, 120, 20));

        datosNotificacionPromotorJPanel.add(faxPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 41, 300, -1));

        datosNotificacionPromotorJPanel.add(telefonoPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 62, 300, -1));

        datosNotificacionPromotorJPanel.add(movilPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 83, 300, -1));

        datosNotificacionPromotorJPanel.add(emailPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 104, 300, -1));

        datosNotificacionPromotorJPanel.add(nombreViaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 146, 300, -1));

        datosNotificacionPromotorJPanel.add(numeroViaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 150, -1));

        datosNotificacionPromotorJPanel.add(plantaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 191, 150, -1));

        datosNotificacionPromotorJPanel.add(portalPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 233, 150, -1));

        datosNotificacionPromotorJPanel.add(escaleraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 254, 150, -1));

        datosNotificacionPromotorJPanel.add(letraPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 212, 150, -1));

        datosNotificacionPromotorJPanel.add(cPostalPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 300, -1));

        datosNotificacionPromotorJPanel.add(municipioPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 301, 300, -1));

        datosNotificacionPromotorJPanel.add(provinciaPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 322, 300, -1));

        notificarPromotorJCheckBox.setEnabled(false);
        notificarPromotorJCheckBox.setVisible(false);
        datosNotificacionPromotorJPanel.add(notificarPromotorJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, -1, -1));

        notificarPromotorJLabel.setVisible(false);
        notificarPromotorJLabel.setText("Notificar promotor:");
        datosNotificacionPromotorJPanel.add(notificarPromotorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 130, 20));

        datosNotificacionPromotorJPanel.add(viaNotificacionPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 300, -1));

        datosNotificacionPromotorJPanel.add(tipoViaNotificacionPromotorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 125, 300, -1));

        promotorJPanel.add(datosNotificacionPromotorJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 175, 518, 372));

        notificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder("Notificaciones"));
        notificacionesJScrollPane.setEnabled(false);
        notificacionesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
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

        datosNotificacionesJPanel.add(notificacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 503, 320));

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

        entregadaAJLabel.setText("Entregada A:");
        datosNotificacionJPanel.add(entregadaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 180, 20));

        entregadaAJTField.setEditable(false);
        entregadaAJTField.setBorder(null);
        datosNotificacionJPanel.add(entregadaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 250, 20));

        datosNotificacionesJPanel.add(datosNotificacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 503, 190));

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
        eventosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
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

        datosEventosJPanel.add(eventosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 503, 430));

        descEventoJScrollPane.setBorder(new javax.swing.border.TitledBorder("Descripci\u00f3n  del Evento Seleccionado"));
        descEventoJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        descEventoJTArea.setEnabled(false);
        descEventoJTArea.setLineWrap(true);
        descEventoJTArea.setRows(3);
        descEventoJTArea.setTabSize(4);
        descEventoJTArea.setWrapStyleWord(true);
        descEventoJTArea.setBorder(null);
        descEventoJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        descEventoJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        descEventoJScrollPane.setViewportView(descEventoJTArea);

        datosEventosJPanel.add(descEventoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 465, 503, 90));

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
        historicoJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
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

        datosHistoricoJPanel.add(historicoJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 450, 430));

        apunteJScrollPane.setBorder(new javax.swing.border.TitledBorder("Apunte del Hist\u00f3rico Seleccionado"));
        apunteJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        apunteJTArea.setEnabled(false);
        apunteJTArea.setLineWrap(true);
        apunteJTArea.setRows(3);
        apunteJTArea.setTabSize(4);
        apunteJTArea.setWrapStyleWord(true);
        apunteJTArea.setBorder(null);
        apunteJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        apunteJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        apunteJScrollPane.setViewportView(apunteJTArea);

        datosHistoricoJPanel.add(apunteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 450, 90));

        historicoJPanel.add(datosHistoricoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 579));

        generarFichaJButton.setIcon(new javax.swing.ImageIcon(""));
        generarFichaJButton.setToolTipText("");
        generarFichaJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        generarFichaJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        generarFichaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarFichaJButtonActionPerformed();
            }
        });

        historicoJPanel.add(generarFichaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 20, 20));

        templateJPanel.add(datosSolicitudJTabbedPane, BorderLayout.WEST);

        editorMapaJPanel.setLayout(new java.awt.GridLayout(1, 0));

        editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder("Mapa"));
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
            CUtilidadesComponentes.generarFichaHistorico(this.desktop, _vHistorico, expedienteLicencia, solicitudLicencia, selectedFile, literales);
        }
    }//GEN-LAST:event_generarFichaJButtonActionPerformed

    private void historicoJTableMouseDragged() {//GEN-FIRST:event_historicoJTableMouseDragged
        mostrarHistoricoSeleccionado();
    }//GEN-LAST:event_historicoJTableMouseDragged

    private void historicoJTableFocusGained() {//GEN-FIRST:event_historicoJTableFocusGained
        mostrarHistoricoSeleccionado();
    }//GEN-LAST:event_historicoJTableFocusGained
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
         mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableFocusGained

    private void notificacionesJTableMouseDragged() {//GEN-FIRST:event_notificacionesJTableMouseDragged
        mostrarNotificacionSeleccionada();
    }//GEN-LAST:event_notificacionesJTableMouseDragged
    private void notificacionesJTableKeyTyped(){
        mostrarNotificacionSeleccionada();
    }
    private void notificacionesJTableKeyPressed() {
        mostrarNotificacionSeleccionada();
    }
    private void notificacionesJTableKeyReleased() {
        mostrarNotificacionSeleccionada();
    }


    private void referenciasCatastralesJTableMouseClicked() {//GEN-FIRST:event_referenciasCatastralesJTableMouseClicked
        int row = referenciasCatastralesJTable.getSelectedRow();
        if (row != -1) {
            CConstantesLicenciasActividad.referencia.setReferenciaCatastral((String)referenciasCatastralesJTableModel.getValueAt(row, 0));
            CConstantesLicenciasActividad.referencia.setTipoVia((String)referenciasCatastralesJTableModel.getValueAt(row, 1));
            CConstantesLicenciasActividad.referencia.setNombreVia((String)referenciasCatastralesJTableModel.getValueAt(row, 2));
            CConstantesLicenciasActividad.referencia.setPrimerNumero((String)referenciasCatastralesJTableModel.getValueAt(row, 3));
            CConstantesLicenciasActividad.referencia.setPrimeraLetra((String)referenciasCatastralesJTableModel.getValueAt(row, 4));
            CConstantesLicenciasActividad.referencia.setBloque((String)referenciasCatastralesJTableModel.getValueAt(row, 5));
            CConstantesLicenciasActividad.referencia.setEscalera((String)referenciasCatastralesJTableModel.getValueAt(row, 6));
            CConstantesLicenciasActividad.referencia.setPlanta((String)referenciasCatastralesJTableModel.getValueAt(row, 7));
            CConstantesLicenciasActividad.referencia.setPuerta((String)referenciasCatastralesJTableModel.getValueAt(row, 8));
            CConstantesLicenciasActividad.referencia.setCPostal((String)referenciasCatastralesJTableModel.getValueAt(row, 9));
            CUtilidadesComponentes.showDatosReferenciaCatastralDialog(desktop, false, literales);
        }

    }//GEN-LAST:event_referenciasCatastralesJTableMouseClicked

    private void generarFicha()
    {
        try
        {
            if (expedienteLicencia == null)
            {
                mostrarMensaje(literales.getString("CConsultaLicenciasForm.mensaje1"));
                return;
            }
            expedienteLicencia.setEstructuraEstado(Estructuras.getListaEstados());
            expedienteLicencia.setEstructuraTipoObra(Estructuras.getListaTiposActividad());
            expedienteLicencia.setLocale(literales.getLocale().toString());
            expedienteLicencia.setSolicitud(solicitudLicencia);
            expedienteLicencia.setEstructuraTipoLicencia(Estructuras.getListaTiposLicenciaActividad());

               new GeopistaPrintable().printObjeto(FichasDisponibles.fichalicenciaactividad, expedienteLicencia , CExpedienteLicencia.class, MainActividad.geopistaEditor.getLayerViewPanel(), GeopistaPrintable.FICHA_LICENCIAS_ACTIVIDAD_CONSULTA);
        } catch (Exception ex) {
            logger.error("Exception al mostrar las features: " ,ex);
        }
    }

	private void formInternalFrameClosed() {//GEN-FIRST:event_formInternalFrameClosed
		CConstantesLicencias.helpSetHomeID = "licenciasIntro";
        CUtilidadesComponentes.menuLicenciasSetEnabled(true, this.desktop);

	}//GEN-LAST:event_formInternalFrameClosed

	private void historicoJTableMouseClicked() {//GEN-FIRST:event_historicoJTableMouseClicked
        mostrarHistoricoSeleccionado();
	}//GEN-LAST:event_historicoJTableMouseClicked

	private void eventosJTableMouseClicked() {//GEN-FIRST:event_eventosJTableMouseClicked
		mostrarEventoSeleccionado();
	}//GEN-LAST:event_eventosJTableMouseClicked

	private void notificacionesJTableMouseClicked() {//GEN-FIRST:event_notificacionesJTableMouseClicked
	    mostrarNotificacionSeleccionada();
	}//GEN-LAST:event_notificacionesJTableMouseClicked

	private void modificarJButtonActionPerformed() {//GEN-FIRST:event_modificarJButtonActionPerformed

		if ((numExpedienteJTField.getText() == null) || (numExpedienteJTField.getText().trim().equals(""))) {
			mostrarMensaje(literales.getString("CConsultaLicenciasForm.mensaje1"));
			return;
		}

        /** Comprobamos si el expediente esta bloqueado */
        if (CUtilidadesComponentes.expedienteBloqueado(numExpedienteJTField.getText().trim(), literales.getLocale().toString())){
            if (CUtilidadesComponentes.mostrarMensajeBloqueo(this, literales, true) != 0){
                return;
            }
        }


		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));


		dispose();

		com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias modificacionLicencias = new com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias(desktop, numExpedienteJTField.getText(),literales, false);
		((IMainLicencias) desktop).mostrarJInternalFrame(modificacionLicencias);
    	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


	}//GEN-LAST:event_modificarJButtonActionPerformed

	private void publicarJButtonActionPerformed(){
		
		if (expedienteLicencia == null)
        {
            //mostrarMensaje("Es necesario consultar primero por un expediente.");
			mostrarMensaje(literales.getString("CActualizarIdSigemLicenciasForm.ConsultarExpediente"));
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

	public void cargarReferenciasCatastralesJTable(CSolicitudLicencia solicitudLicencia) {
		try {

			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
			Vector referenciasCatastrales = solicitudLicencia.getReferenciasCatastrales();
			logger.info("referenciasCatastrales: " + referenciasCatastrales);
            MainActividad.geopistaEditor.getSelectionManager().clear();

			if ((referenciasCatastrales == null) || (referenciasCatastrales.isEmpty())) {
				logger.info("No hay referenciasCatastrales para la licencia.");
                refreshFeatureSelection();
				return;
			}

			for (int i = 0; i < referenciasCatastrales.size(); i++) {
				CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) referenciasCatastrales.elementAt(i);

               String tipoVia= "";
                if ((referenciaCatastral.getTipoVia() != null) && (referenciaCatastral.getTipoVia().trim().length() > 0)){
                    tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(referenciaCatastral.getTipoVia()).getTerm(literales.getLocale().toString());
                }

				referenciasCatastralesJTableModel.addRow(new String[]{referenciaCatastral.getReferenciaCatastral(),
																	  tipoVia,
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


	private void consultarJButtonActionPerformed() {//GEN-FIRST:event_consultarJButtonActionPerformed


		try {

			if ((numExpedienteJTField.getText() == null) || (numExpedienteJTField.getText().trim().equals(""))) {
				return;
			}

            /** Cancelacion de busqueda por parte del dialogo de busqueda de expediente. */
            if (CConstantesLicencias.searchCanceled){
                CConstantesLicencias.searchCanceled= false;
                return;
            }

            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Vector tiposLicencia= new Vector();
            CTipoLicencia tipoLicencia= new CTipoLicencia(new Integer(CConstantesLicencias.LicenciasActividad).intValue(), "", "");
            tiposLicencia.add(tipoLicencia);
            tipoLicencia= new CTipoLicencia(new Integer(CConstantesLicencias.LicenciasActividadNoCalificada).intValue(), "", "");
            tiposLicencia.add(tipoLicencia);

			CResultadoOperacion resultadoOperacion = COperacionesLicencias.getExpedienteLicencia(numExpedienteJTField.getText(), literales.getLocale().toString(), tiposLicencia);

            if (!resultadoOperacion.getResultado()){
                mostrarMensaje(literales.getString("CConsultaLicenciasForm.mensaje2"));
                clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            if ((resultadoOperacion.getSolicitudes()!=null) && (resultadoOperacion.getExpedientes()!=null)){
			    solicitudLicencia = (CSolicitudLicencia) resultadoOperacion.getSolicitudes().get(0);
			    expedienteLicencia = (CExpedienteLicencia) resultadoOperacion.getExpedientes().get(0);
            }else{
                mostrarMensaje(literales.getString("CConsultaLicenciasForm.mensaje2"));
                clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            if ((solicitudLicencia == null) || (expedienteLicencia == null)){
                mostrarMensaje(literales.getString("CConsultaLicenciasForm.mensaje2"));
                clearScreen();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }

			//***********************************************
			//** Datos de expediente.
			//*******************************************************
			estadoExpedienteJTextField.setText(((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(expedienteLicencia.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()));
			servicioExpedienteJTField.setText(expedienteLicencia.getServicioEncargado());
			tramitacionJTextField.setText(((DomainNode)Estructuras.getListaTiposTramitacion().getDomainNode(new Integer(expedienteLicencia.getTipoTramitacion().getIdTramitacion()).toString())).getTerm(literales.getLocale().toString()));
			asuntoExpedienteJTField.setText(expedienteLicencia.getAsunto());
			fechaAperturaJTField.setText(CUtilidades.formatFecha(expedienteLicencia.getFechaApertura()));
			inicioJTField.setText(expedienteLicencia.getFormaInicio());
			if (expedienteLicencia.getSilencioAdministrativo().equals("1")) {
				silencioJCheckBox.setSelected(true);
			}
            if (expedienteLicencia.getResponsable() != null){
                responsableJTextField.setText(expedienteLicencia.getResponsable());
            }else{
                responsableJTextField.setText("");
            }

			observacionesExpedienteJTArea.setText(expedienteLicencia.getObservaciones());

			//***********************************************
			//** Datos de solicitud.
			//*******************************************************
            tipoObraJTextField.setText(((DomainNode)Estructuras.getListaTiposActividad().getDomainNode(new Integer(solicitudLicencia.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString()));
            jCheckBoxActividadNoCalificada.setSelected(solicitudLicencia.getTipoLicencia().getIdTipolicencia()==CConstantesLicencias.ActividadesNoCalificadas);

			numRegistroJTField.setText(solicitudLicencia.getNumAdministrativo());
			unidadTJTField.setText(solicitudLicencia.getUnidadTramitadora());
			unidadRJTField.setText(solicitudLicencia.getUnidadDeRegistro());
			motivoJTField.setText(solicitudLicencia.getMotivo());
			asuntoJTField.setText(solicitudLicencia.getAsunto());
			fechaSolicitudJTField.setText(CUtilidades.formatFecha(solicitudLicencia.getFecha()));
            if (solicitudLicencia.getFechaLimiteObra() != null){
                fechaLimiteObraJTField.setText(CUtilidades.formatFecha(solicitudLicencia.getFechaLimiteObra()));
            }else{
                fechaLimiteObraJTField.setText("");
            }

            if (cnaeTField.getText() != null){
                cnaeTField.setText(expedienteLicencia.getCNAE());
            }else cnaeTField.setText("");

           	tasaTextField.setNumber(new Double(solicitudLicencia.getTasa()));
            impuestoTextField.setNumber(new Double(solicitudLicencia.getImpuesto()));
			observacionesJTArea.setText(solicitudLicencia.getObservaciones());

            String tipoVia= "";
            if ((solicitudLicencia.getTipoViaAfecta() != null) && (solicitudLicencia.getTipoViaAfecta().trim().length() > 0)){
                tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(solicitudLicencia.getTipoViaAfecta()).getTerm(literales.getLocale().toString());
            }
            tipoViaJTextField.setText(tipoVia);

			nombreViaJTextField.setText(solicitudLicencia.getNombreViaAfecta());
			numeroViaJTextField.setText(solicitudLicencia.getNumeroViaAfecta());
			portalJTextField.setText(solicitudLicencia.getPortalAfecta());
			plantaJTextField.setText(solicitudLicencia.getPlantaAfecta());
            try{
			    cPostalJTextField.setNumber(new Integer(solicitudLicencia.getCpostalAfecta()));
            }catch(Exception e){cPostalJTextField.setText("");}
			municipioJTextField.setText(solicitudLicencia.getMunicipioAfecta());
			provinciaJTextField.setText(solicitudLicencia.getProvinciaAfecta());

            initTablas();
			cargarReferenciasCatastralesJTable(solicitudLicencia);
			refreshFeatureSelection();

            /** datos de actividad */
            if (solicitudLicencia.getDatosActividad() != null){
                datosActividadJPanel.load(solicitudLicencia.getDatosActividad());
            }

            /** Documentacion */
            documentacionJPanel.load(expedienteLicencia,solicitudLicencia,false);
            /** Informes */
            jPanelInformes.setInformes(expedienteLicencia.getInformes(),solicitudLicencia.getIdSolicitud(), false);
            /** Resolucion */
            jPanelResolucion.load(expedienteLicencia.getResolucion());
            jPanelResolucion.setEnabled(false);

            /** Finalizacion del expediente */
            if (expedienteLicencia.getTipoFinalizacion()!=null)
                jTextFieldFinaliza.setText(((DomainNode)Estructuras.getListaTiposFinalizacion().getDomainNode(new Integer(expedienteLicencia.getTipoFinalizacion().getIdFinalizacion()).toString())).getTerm(literales.getLocale().toString()));
            else
                jTextFieldFinaliza.setText("");
			//***********************************************
			//** Datos titular.
			//***********************************************
			DNITitularJTField.setText(solicitudLicencia.getPropietario().getDniCif());
			nombreTitularJTField.setText(solicitudLicencia.getPropietario().getNombre());
			apellido1TitularJTField.setText(solicitudLicencia.getPropietario().getApellido1());
			apellido2TitularJTField.setText(solicitudLicencia.getPropietario().getApellido2());

            viaNotificacionTitularJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(solicitudLicencia.getPropietario().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(literales.getLocale().toString()));
            if (new Integer(solicitudLicencia.getPropietario().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString().equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
                emailTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailTitularJLabel.text")));
            }else{
                emailTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailTitularJLabel.text"));
            }

			faxTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getFax());
			telefonoTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getTelefono());
			movilTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getMovil());
			emailTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getEmail());
            try
            {
                tipoViaNotificacionTitularJTField.setText(((DomainNode)Estructuras.getListaTiposViaINE().getDomainNode(solicitudLicencia.getPropietario().getDatosNotificacion().getTipoVia())).getTerm(literales.getLocale().toString()));
            }catch(Exception e)
            {
                tipoViaNotificacionTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getTipoVia());
            }
			nombreViaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getNombreVia());
			numeroViaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getNumeroVia());
			plantaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getPlanta());
			plantaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getPlanta());
			letraTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getLetra());
			portalTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getPortal());
			escaleraTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getEscalera());
            try{
			    cPostalTitularJTField.setNumber(new Integer(solicitudLicencia.getPropietario().getDatosNotificacion().getCpostal()));
            }catch(Exception e){cPostalTitularJTField.setText("");}
			municipioTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getMunicipio());
			provinciaTitularJTField.setText(solicitudLicencia.getPropietario().getDatosNotificacion().getProvincia());
			if (solicitudLicencia.getPropietario().getDatosNotificacion().getNotificar().equals("1")) {
				notificarTitularJCheckBox.setSelected(true);
			}



			//***********************************************
			//** Datos representante.
			//*******************************************************
			if (solicitudLicencia.getRepresentante() != null) {

				DNIRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDniCif());
				nombreRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getNombre());
				apellido1RepresentaAJTField.setText(solicitudLicencia.getRepresentante().getApellido1());
				apellido2RepresentaAJTField.setText(solicitudLicencia.getRepresentante().getApellido2());

                viaNotificacionRepresentaAJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(solicitudLicencia.getRepresentante().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(literales.getLocale().toString()));
                if (new Integer(solicitudLicencia.getRepresentante().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString().equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
                    emailRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailRepresentaAJLabel.text")));
                }else{
                    emailRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailRepresentaAJLabel.text"));
                }

				faxRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getFax());
				telefonoRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getTelefono());
				movilRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getMovil());
				emailRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getEmail());
                try
                {
                    tipoViaNotificacionRepresentaAJTField.setText(((DomainNode)Estructuras.getListaTiposViaINE().getDomainNode(solicitudLicencia.getRepresentante().getDatosNotificacion().getTipoVia())).getTerm(literales.getLocale().toString()));
                }catch(Exception e)
                {
                    tipoViaNotificacionRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getTipoVia());
                }
				nombreViaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getNombreVia());
				numeroViaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getNumeroVia());
				plantaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getPlanta());
				plantaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getPlanta());
				letraRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getLetra());
				portalRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getPortal());
				escaleraRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getEscalera());
                try{
				    cPostalRepresentaAJTField.setNumber(new Integer(solicitudLicencia.getRepresentante().getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalRepresentaAJTField.setText("");}
				municipioRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getMunicipio());
				provinciaRepresentaAJTField.setText(solicitudLicencia.getRepresentante().getDatosNotificacion().getProvincia());
				if (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equals("1")) {
					notificarRepresentaAJCheckBox.setSelected(true);
				}
			}



			//***********************************************
			//** Datos tecnico.
			//*******************************************************
            if ((solicitudLicencia.getTecnicos() != null) && (solicitudLicencia.getTecnicos().size() > 0)) {
                CPersonaJuridicoFisica tecnico= (CPersonaJuridicoFisica)solicitudLicencia.getTecnicos().get(0);
                if (tecnico != null){
                    DNITecnicoJTField.setText(tecnico.getDniCif());
                    nombreTecnicoJTField.setText(tecnico.getNombre());
                    apellido1TecnicoJTField.setText(tecnico.getApellido1());
                    apellido2TecnicoJTField.setText(tecnico.getApellido2());
                    colegioTecnicoJTField.setText(tecnico.getColegio());
                    visadoTecnicoJTField.setText(tecnico.getVisado());
                    titulacionTecnicoJTField.setText(tecnico.getTitulacion());

                    viaNotificacionTecnicoJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(tecnico.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(literales.getLocale().toString()));
                    if (new Integer(tecnico.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString().equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
                        emailTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailTecnicoJLabel.text")));
                    }else{
                        emailTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailTecnicoJLabel.text"));
                    }

                    faxTecnicoJTField.setText(tecnico.getDatosNotificacion().getFax());
                    telefonoTecnicoJTField.setText(tecnico.getDatosNotificacion().getTelefono());
                    movilTecnicoJTField.setText(tecnico.getDatosNotificacion().getMovil());
                    emailTecnicoJTField.setText(tecnico.getDatosNotificacion().getEmail());
                    try
                    {
                        tipoViaNotificacionTecnicoJTField.setText(((DomainNode)Estructuras.getListaTiposViaINE().getDomainNode(tecnico.getDatosNotificacion().getTipoVia())).getTerm(literales.getLocale().toString()));
                    }catch(Exception e)
                    {
                        tipoViaNotificacionTecnicoJTField.setText(tecnico.getDatosNotificacion().getTipoVia());
                    }
                    nombreViaTecnicoJTField.setText(tecnico.getDatosNotificacion().getNombreVia());
                    numeroViaTecnicoJTField.setText(tecnico.getDatosNotificacion().getNumeroVia());
                    plantaTecnicoJTField.setText(tecnico.getDatosNotificacion().getPlanta());
                    plantaTecnicoJTField.setText(tecnico.getDatosNotificacion().getPlanta());
                    letraTecnicoJTField.setText(tecnico.getDatosNotificacion().getLetra());
                    portalTecnicoJTField.setText(tecnico.getDatosNotificacion().getPortal());
                    escaleraTecnicoJTField.setText(tecnico.getDatosNotificacion().getEscalera());
                    try{
                        cPostalTecnicoJTField.setNumber(new Integer(tecnico.getDatosNotificacion().getCpostal()));
                    }catch(Exception e){cPostalTecnicoJTField.setText("");}
                    municipioTecnicoJTField.setText(tecnico.getDatosNotificacion().getMunicipio());
                    provinciaTecnicoJTField.setText(tecnico.getDatosNotificacion().getProvincia());
                    if (tecnico.getDatosNotificacion().getNotificar().equals("1")) {
                        notificarTecnicoJCheckBox.setSelected(true);
                    }
                }
            }



			//***********************************************
			//** Datos Promotor.
			//*******************************************************
            if (solicitudLicencia.getPromotor() != null) {
                DNIPromotorJTField.setText(solicitudLicencia.getPromotor().getDniCif());
                nombrePromotorJTField.setText(solicitudLicencia.getPromotor().getNombre());
                apellido1PromotorJTField.setText(solicitudLicencia.getPromotor().getApellido1());
                apellido2PromotorJTField.setText(solicitudLicencia.getPromotor().getApellido2());
                colegioPromotorJTField.setText(solicitudLicencia.getPromotor().getColegio());
                visadoPromotorJTField.setText(solicitudLicencia.getPromotor().getVisado());
                titulacionPromotorJTField.setText(solicitudLicencia.getPromotor().getTitulacion());

                viaNotificacionPromotorJTField.setText(((DomainNode)Estructuras.getListaViasNotificacion().getDomainNode(new Integer(solicitudLicencia.getPromotor().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString())).getTerm(literales.getLocale().toString()));
                if (new Integer(solicitudLicencia.getPromotor().getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString().equalsIgnoreCase(CConstantesLicencias.patronNotificacionEmail)){
                    emailPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailPromotorJLabel.text")));
                }else{
                    emailPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailPromotorJLabel.text"));
                }

                faxPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getFax());
                telefonoPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getTelefono());
                movilPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getMovil());
                emailPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getEmail());
                try
                {
                    tipoViaNotificacionPromotorJTField.setText(((DomainNode)Estructuras.getListaTiposViaINE().getDomainNode(solicitudLicencia.getPromotor().getDatosNotificacion().getTipoVia())).getTerm(literales.getLocale().toString()));
                }catch(Exception e)
                {
                    tipoViaNotificacionPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getTipoVia());
                }
                nombreViaPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getNombreVia());
                numeroViaPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getNumeroVia());
                plantaPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getPlanta());
                plantaPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getPlanta());
                letraPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getLetra());
                portalPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getPortal());
                escaleraPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getEscalera());
                try{
                    cPostalPromotorJTField.setNumber(new Integer(solicitudLicencia.getPromotor().getDatosNotificacion().getCpostal()));
                }catch(Exception e){cPostalPromotorJTField.setText("");}
                municipioPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getMunicipio());
                provinciaPromotorJTField.setText(solicitudLicencia.getPromotor().getDatosNotificacion().getProvincia());
                if (solicitudLicencia.getPromotor().getDatosNotificacion().getNotificar().equals("1")) {
                    notificarPromotorJCheckBox.setSelected(true);
                }
            }

			//***********************************************
			//** Datos notificaciones
			//***********************************************
			CUtilidadesComponentes.clearTable(notificacionesJTableModel);
            // Annadimos a la tabla el editor ComboBox en la primera columna (estado)
            _vNotificaciones = expedienteLicencia.getNotificaciones();
            if ((_vNotificaciones != null) && (_vNotificaciones.size() > 0)) {
                for (int i=0; i < _vNotificaciones.size(); i++) {
                    CNotificacion notificacion = (CNotificacion) _vNotificaciones.get(i);
                    try{
                        int tipoEstado = notificacion.getEstadoNotificacion().getIdEstado();
                        int tipoNotificacion= notificacion.getTipoNotificacion().getIdTipoNotificacion();
                        String descTipoNotificacion= "";
                        try{
                            descTipoNotificacion=((DomainNode)Estructuras.getListaTiposNotificacion().getDomainNode(new Integer(tipoNotificacion).toString())).getTerm(literales.getLocale().toString());
                        }catch(Exception e){
                            logger.error("Tipo de notificación no encontrado:",e);
                        }

                        Object[] rowData = {descTipoNotificacion,
                                            ((DomainNode)Estructuras.getListaEstadosNotificacion().getDomainNode(new Integer(tipoEstado).toString())).getTerm(literales.getLocale().toString()),
                                            CUtilidades.formatFechaH24(notificacion.getPlazoVencimiento()),
                                            notificacion.getPersona().getDniCif(),
                                            CUtilidades.formatFechaH24(notificacion.getFechaNotificacion()),
                                            CUtilidades.formatFechaH24(notificacion.getFecha_reenvio()),
                                            new Integer(i)};
                        notificacionesJTableModel.addRow(rowData);
                    }catch (Exception e){
                        logger.error("ERROR cargar fila NOTIFICACIONES "+e);
                    }
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
				for (int i=0; i < _vEventos.size(); i++) {
					CEvento evento = (CEvento) _vEventos.get(i);
                    try{
                        JCheckBox check = (JCheckBox) ((CheckBoxTableEditor) eventosJTable.getCellEditor(i, 2)).getComponent();

                        if (evento.getRevisado().equalsIgnoreCase("1"))
                            check.setSelected(true);
                        else
                            check.setSelected(false);
                        _eventosTableModel.addRow(new Object[]{new Long(evento.getIdEvento()).toString(), CUtilidades.formatFechaH24(evento.getFechaEvento()), new Boolean(check.isSelected()), evento.getRevisadoPor(), evento.getContent(), new Integer(i)});
                    }catch(Exception e){
                        logger.error("ERROR cargar fila EVENTOS "+e);
                    }
				}
			}

			//***********************************************
			//** Datos historico
			//***********************************************
            _vHistorico= new Vector();
            Vector aux= expedienteLicencia.getHistorico();
            if ((aux != null) && (aux.size() > 0)) 
            for (int i=0; i<aux.size(); i++){
                CHistorico histAux= (CHistorico)aux.get(i);
                try{
                    String apunte= histAux.getApunte();
                    if ((histAux.getSistema().equalsIgnoreCase("1")) && (histAux.getGenerico() == 0)){
                        /** Componemos el apunte, de forma multilingue */
                        apunte+= " " +
                                ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(histAux.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()) + ".";
                       histAux.setApunte(apunte);
                    }
                    _vHistorico.add(histAux);
                }catch(Exception e){
                    logger.error("ERROR al cargar fila HISTORICO "+e);
                }
            }

			if ((_vHistorico != null) && (_vHistorico.size() > 0)) {
				for (int i=0; i<_vHistorico.size(); i++) {
					CHistorico historico = (CHistorico) _vHistorico.get(i);
                    try{
                        _historicoTableModel.addRow(new Object[]{ //new Long(historico.getIdHistorico()).toString(),
                                                                 ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
                                                                 historico.getUsuario(),
                                                                 CUtilidades.formatFechaH24(historico.getFechaHistorico()),
                                                                 historico.getApunte(),
                                                                 new Integer(i)});
                    }catch (Exception e){
                        logger.error("ERROR al cargar fila HISTORICO "+e);
                    }
				}
			}
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		} catch (Exception ex) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

		}


	}//GEN-LAST:event_consultarJButtonActionPerformed


	private boolean clearScreen() {

		//***********************************************
		//** Datos de expediente.
		//*******************************************************
		estadoExpedienteJTextField.setText("");
		servicioExpedienteJTField.setText("");
		asuntoExpedienteJTField.setText("");
		fechaAperturaJTField.setText("");
        responsableJTextField.setText("");
      	inicioJTField.setText("");
		silencioJCheckBox.setSelected(false);

        tramitacionJTextField.setText("");

		observacionesExpedienteJTArea.setText("");

		//***********************************************
		//** Datos de solicitud.
		//*******************************************************
		numRegistroJTField.setText("");
		unidadTJTField.setText("");
		unidadRJTField.setText("");
		motivoJTField.setText("");
		asuntoJTField.setText("");
		fechaSolicitudJTField.setText("");
        fechaLimiteObraJTField.setText("");
		tasaTextField.setText("€0.00");
        impuestoTextField.setText("€0.00");
		observacionesJTArea.setText("");
		tipoViaJTextField.setText("");
		nombreViaJTextField.setText("");
		numeroViaJTextField.setText("");
		portalJTextField.setText("");
		plantaJTextField.setText("");
		cPostalJTextField.setText("");
		municipioJTextField.setText("");
		provinciaJTextField.setText("");
        cnaeTField.setText("");

		CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
		MainActividad.geopistaEditor.getSelectionManager().clear();

        /** datos de actividad */
        datosActividadJPanel.load(null);


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
		//** Datos tecnico.
		//*******************************************************
		DNITecnicoJTField.setText("");
		nombreTecnicoJTField.setText("");
		apellido1TecnicoJTField.setText("");
		apellido2TecnicoJTField.setText("");
		colegioTecnicoJTField.setText("");
		visadoTecnicoJTField.setText("");
		titulacionTecnicoJTField.setText("");

        viaNotificacionTecnicoJTField.setText("");
		faxTecnicoJTField.setText("");
		telefonoTecnicoJTField.setText("");
		movilTecnicoJTField.setText("");
		emailTecnicoJTField.setText("");

        tipoViaNotificacionTecnicoJTField.setText("");
		nombreViaTecnicoJTField.setText("");
		numeroViaTecnicoJTField.setText("");
		plantaTecnicoJTField.setText("");
		plantaTecnicoJTField.setText("");
		letraTecnicoJTField.setText("");
		portalTecnicoJTField.setText("");
		escaleraTecnicoJTField.setText("");
		cPostalTecnicoJTField.setText("");
		municipioTecnicoJTField.setText("");
		provinciaTecnicoJTField.setText("");
		notificarTecnicoJCheckBox.setSelected(false);

		//***********************************************
		//** Datos Promotor.
		//*******************************************************
		DNIPromotorJTField.setText("");
		nombrePromotorJTField.setText("");
		apellido1PromotorJTField.setText("");
		apellido2PromotorJTField.setText("");
		colegioPromotorJTField.setText("");
		visadoPromotorJTField.setText("");
		titulacionPromotorJTField.setText("");
        viaNotificacionPromotorJTField.setText("");

        tipoViaNotificacionPromotorJTField.setText("");
		faxPromotorJTField.setText("");
		telefonoPromotorJTField.setText("");
		movilPromotorJTField.setText("");
		emailPromotorJTField.setText("");

		nombreViaPromotorJTField.setText("");
		numeroViaPromotorJTField.setText("");
		plantaPromotorJTField.setText("");
		plantaPromotorJTField.setText("");
		letraPromotorJTField.setText("");
		portalPromotorJTField.setText("");
		escaleraPromotorJTField.setText("");
		cPostalPromotorJTField.setText("");
		municipioPromotorJTField.setText("");
		provinciaPromotorJTField.setText("");
		notificarPromotorJCheckBox.setSelected(false);

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

    public void setEnabledDatosSolicitud(boolean b){
        tipoObraJTextField.setEnabled(b);
        numRegistroJTField.setEnabled(b);
        unidadTJTField.setEnabled(b);
        unidadRJTField.setEnabled(b);
        motivoJTField.setEnabled(b);
        asuntoJTField.setEnabled(b);
        fechaSolicitudJTField.setEnabled(b);
        fechaLimiteObraJTField.setEnabled(b);
        tasaTextField.setEnabled(b);
        impuestoTextField.setEnabled(b);
        observacionesJTArea.setEnabled(b);
        tipoViaJTextField.setEnabled(b);
        nombreViaJTextField.setEnabled(b);
        numeroViaJTextField.setEnabled(b);
        portalJTextField.setEnabled(b);
        plantaJTextField.setEnabled(b);
        cPostalJTextField.setEnabled(b);
        municipioJTextField.setEnabled(b);
        provinciaJTextField.setEnabled(b);
        cnaeTField.setEnabled(b);
    }

    public void setEnabledDatosExpediente(boolean b){
        estadoExpedienteJTextField.setEnabled(b);
        servicioExpedienteJTField.setEnabled(b);
        asuntoExpedienteJTField.setEnabled(b);
        fechaAperturaJTField.setEnabled(b);
        responsableJTextField.setEnabled(b);
        inicioJTField.setEnabled(b);
        silencioJCheckBox.setEnabled(b);
        tramitacionJTextField.setEnabled(b);
        observacionesExpedienteJTArea.setEnabled(b);
    }

    public void setEnabledDatosSolicitante(boolean b){
        DNITitularJTField.setEnabled(b);
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


    public void setEnabledDatosTecnico(boolean b){
        DNITecnicoJTField.setEnabled(b);
        nombreTecnicoJTField.setEnabled(b);
        apellido1TecnicoJTField.setEnabled(b);
        apellido2TecnicoJTField.setEnabled(b);
        colegioTecnicoJTField.setEnabled(b);
        visadoTecnicoJTField.setEnabled(b);
        titulacionTecnicoJTField.setEnabled(b);

        viaNotificacionTecnicoJTField.setEnabled(b);
        faxTecnicoJTField.setEnabled(b);
        telefonoTecnicoJTField.setEnabled(b);
        movilTecnicoJTField.setEnabled(b);
        emailTecnicoJTField.setEnabled(b);

        tipoViaNotificacionTecnicoJTField.setEnabled(b);
        nombreViaTecnicoJTField.setEnabled(b);
        numeroViaTecnicoJTField.setEnabled(b);
        plantaTecnicoJTField.setEnabled(b);
        plantaTecnicoJTField.setEnabled(b);
        letraTecnicoJTField.setEnabled(b);
        portalTecnicoJTField.setEnabled(b);
        escaleraTecnicoJTField.setEnabled(b);
        cPostalTecnicoJTField.setEnabled(b);
        municipioTecnicoJTField.setEnabled(b);
        provinciaTecnicoJTField.setEnabled(b);
        notificarTecnicoJCheckBox.setEnabled(b);

    }

    public void setEnabledDatosPromotor(boolean b){
        DNIPromotorJTField.setEnabled(b);
        nombrePromotorJTField.setEnabled(b);
        apellido1PromotorJTField.setEnabled(b);
        apellido2PromotorJTField.setEnabled(b);
        colegioPromotorJTField.setEnabled(b);
        visadoPromotorJTField.setEnabled(b);
        titulacionPromotorJTField.setEnabled(b);
        viaNotificacionPromotorJTField.setEnabled(b);

        tipoViaNotificacionPromotorJTField.setEnabled(b);
        faxPromotorJTField.setEnabled(b);
        telefonoPromotorJTField.setEnabled(b);
        movilPromotorJTField.setEnabled(b);
        emailPromotorJTField.setEnabled(b);

        nombreViaPromotorJTField.setEnabled(b);
        numeroViaPromotorJTField.setEnabled(b);
        plantaPromotorJTField.setEnabled(b);
        plantaPromotorJTField.setEnabled(b);
        letraPromotorJTField.setEnabled(b);
        portalPromotorJTField.setEnabled(b);
        escaleraPromotorJTField.setEnabled(b);
        cPostalPromotorJTField.setEnabled(b);
        municipioPromotorJTField.setEnabled(b);
        provinciaPromotorJTField.setEnabled(b);
        notificarPromotorJCheckBox.setEnabled(b);
    }


	private void buscarExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarExpedienteJButtonActionPerformed

        CUtilidadesComponentes.showSearchDialog(desktop, new Integer(CConstantesLicencias.Actividades).toString(), "-1", "-1", "", true, literales);
        if ((numExpedienteJTField.getText() != null) && (numExpedienteJTField.getText().trim().length() > 0)){
            if ((CConstantesLicencias.searchValue != null) && (CConstantesLicencias.searchValue.trim().length() > 0)){
                /** se ha cancelado en el dialogo de busqueda */
                numExpedienteJTField.setText(CConstantesLicencias.searchValue);
            }
        }else{
            numExpedienteJTField.setText(CConstantesLicencias.searchValue);
        }

		consultarJButtonActionPerformed();

	}//GEN-LAST:event_buscarExpedienteJButtonActionPerformed

	private void salirJButtonActionPerformed() {//GEN-FIRST:event_salirJButtonActionPerformed
		CConstantesLicencias.helpSetHomeID = "licenciasIntro";
		this.dispose();
	}//GEN-LAST:event_salirJButtonActionPerformed


	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(desktop, mensaje);
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
			//System.out.println("Fecha Original: " + fecha);
			//System.out.println("Parsed date: " + date.toString());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public Date getDate(String fecha) {
		try {
			if (esDate(fecha))
				return CConstantesLicencias.df.parse(fecha);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean excedeLongitud(String campo, int maxLengthPermitida) {
		if (campo.length() > maxLengthPermitida)
			return true;
		else
			return false;
	}

    private void annadirPestanas(ResourceBundle literales){

        try{
            datosSolicitudJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.expedienteJPanel.TitleTab")), CUtilidadesComponentes.iconoExpediente, expedienteJPanel);
        }catch(Exception e){
            datosSolicitudJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.expedienteJPanel.TitleTab")), expedienteJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.SubTitleTab")), CUtilidadesComponentes.iconoSolicitud, solicitudJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.SubTitleTab")), solicitudJPanel);
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
            jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.titularJPanel.TitleTab")), CUtilidadesComponentes.iconoPersona, titularJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.titularJPanel.TitleTab")), titularJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.representaAJPanel.TitleTab"), CUtilidadesComponentes.iconoRepresentante, representaAJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.representaAJPanel.TitleTab"), representaAJPanel);
        }

        try{
            jTabbedPaneSolicitud.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.TitleTab"), CUtilidadesComponentes.iconoPersona, tecnicoJPanel);
        }catch(Exception e){
             jTabbedPaneSolicitud.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.TitleTab"), tecnicoJPanel);
        }
        try{
            jTabbedPaneSolicitud.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.promotorJPanel.TitleTab"), CUtilidadesComponentes.iconoPersona, promotorJPanel);
        }catch(Exception e){
            jTabbedPaneSolicitud.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.promotorJPanel.TitleTab"), promotorJPanel);
        }
        try{
            datosSolicitudJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.TitleTab")), CUtilidadesComponentes.iconoSolicitud, jTabbedPaneSolicitud);
        }catch(Exception e){
            datosSolicitudJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.TitleTab")), jTabbedPaneSolicitud);
        }

        try{
            datosSolicitudJTabbedPane.addTab(literales.getString("DocumentacionLicenciasJPanel.title"), CUtilidadesComponentes.iconoDocumentacion, documentacionJPanel);
        }catch(Exception e){
            datosSolicitudJTabbedPane.addTab(literales.getString("DocumentacionLicenciasJPanel.title"), documentacionJPanel);
        }

        try{
            datosSolicitudJTabbedPane.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJPanel.TitleTab"), CUtilidadesComponentes.iconoNotificacion, notificacionesJPanel);
        }catch(Exception e){
            datosSolicitudJTabbedPane.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJPanel.TitleTab"), notificacionesJPanel);
        }

        try{
            datosSolicitudJTabbedPane.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJPanel.TitleTab"), CUtilidadesComponentes.iconoEvento, eventosJPanel);
        }catch(Exception e){
            datosSolicitudJTabbedPane.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJPanel.TitleTab"), eventosJPanel);
        }

        try{
            datosSolicitudJTabbedPane.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJPanel.TitleTab"), CUtilidadesComponentes.iconoHistorico, historicoJPanel);
        }catch(Exception e){
            datosSolicitudJTabbedPane.addTab(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJPanel.TitleTab"), historicoJPanel);
        }

        try{
             datosSolicitudJTabbedPane.addTab(literales.getString("JPanelInformes.jPanelInforme"),CUtilidadesComponentes.iconoInformes  , jPanelInformes);
        }catch(Exception e){
             datosSolicitudJTabbedPane.addTab(literales.getString("JPanelInformes.jPanelInforme"), jPanelInformes);
        }

    }


	public void renombrarComponentes(ResourceBundle literales) {
        this.literales=literales;

        try{
            setTitle(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.JInternalFrame.title"));

            /** Pestanas */
            datosSolicitudJTabbedPane.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.expedienteJPanel.TitleTab")));
            jTabbedPaneSolicitud.setTitleAt(0, CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.SubTitleTab")));
            jTabbedPaneSolicitud.setTitleAt(1, literales.getString("DatosActividadJPanel.SubTitleTab"));
            jTabbedPaneSolicitud.setTitleAt(2, CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.titularJPanel.TitleTab")));
            jTabbedPaneSolicitud.setTitleAt(3, literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.representaAJPanel.TitleTab"));
            jTabbedPaneSolicitud.setTitleAt(4, literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tecnicoJPanel.TitleTab"));
            jTabbedPaneSolicitud.setTitleAt(5, literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.promotorJPanel.TitleTab"));
            datosSolicitudJTabbedPane.setTitleAt(1, CUtilidadesComponentes.annadirAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.solicitudJPanel.TitleTab")));
            datosSolicitudJTabbedPane.setTitleAt(2, literales.getString("DocumentacionLicenciasJPanel.title"));
            datosSolicitudJTabbedPane.setTitleAt(3, literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJPanel.TitleTab"));
            datosSolicitudJTabbedPane.setTitleAt(4, literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJPanel.TitleTab"));
            datosSolicitudJTabbedPane.setTitleAt(5, literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJPanel.TitleTab"));
            datosSolicitudJTabbedPane.setTitleAt(6, literales.getString("JPanelInformes.jPanelInforme"));

            /** Expediente */
            inicioJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.inicioJLabel.text"));
            consultarJButton.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.consultarJButton.text"));
            silencioJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.silencioJLabel.text"));
            notaJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notaJLabel.text"));
            expedienteJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.expedienteJPanel.TitleBorder")));
            numExpedienteJLabel.setText(literales.getString("CConsultaLicenciasForm.numExpedienteJLabel.text"));//Num. Expediente:");
            asuntoExpedienteJLabel.setText(literales.getString("CConsultaLicenciasForm.asuntoExpedienteJLabel.text"));//Asunto
            estadoExpedienteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.estadoExpedienteJLabel.text")));//Estado
            servicioExpedienteJLabel.setText(literales.getString("CConsultaLicenciasForm.servicioExpedienteJLabel.text"));//Servicio encargado:
            tramitacionJLabel.setText(literales.getString("CConsultaLicenciasForm.tramitacionJLabel.text"));//Tipo tramitacion:
            fechaAperturaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.fechaAperturaJLabel.text")));//Fecha apertura:
            responsableJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.responsableJLabel.text"));
            observacionesExpedienteJLabel.setText(literales.getString("CConsultaLicenciasForm.observacionesExpedienteJLabel.text"));//Fecha apertura:)

            /** cnae */
            cnaeJLabel.setText(literales.getString("cnaeJLabel.text"));

            /** Solicitud */
            datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosSolicitudJPanel.TitleBorder")));
            numRegistroJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.numRegistroJLabel.text"));
            tipoObraJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tipoObraJLabel.text")));
            jCheckBoxActividadNoCalificada.setText(literales.getString("jCheckBoxActividadNoCalificada"));
            unidadTJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.unidadTJLabel.text"));
            unidadRJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.unidadRJLabel.text"));
            motivoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.motivoJLabel.text"));
            asuntoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.asuntoJLabel.text"));
            fechaSolicitudJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.fechaSolicitudJLabel.text")));
            fechaLimiteObraJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.fechaLimiteObraJLabel.text"));
            observacionesJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.observacionesJLabel.text"));
            tasaJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tasaJLabel.text"));
            impuestoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.impuestoJLabel.text"));
            emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emplazamientoJPanel.TitleBorder")));
            nombreViaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tipoViaJLabel.text"), literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.nombreViaJLabel.text")));
            numeroViaJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.numeroViaJLabel.text"));
            cPostalJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.cPostalJLabel.text"));
            provinciaJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.provinciaJLabel.text"));

            /** Datos Actividad */
            datosActividadJPanel.renameComponents(literales);

            /** Propietario */
            datosPersonalesTitularJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosPersonalesTitularJPanel.TitleBorder")));
            DNITitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.DNITitularJLabel.text")));
            nombreTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.nombreTitularJLabel.text")));
            apellido1TitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.apellido1TitularJLabel.text"));
            apellido2TitularJLabel2.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.apellido2TitularJLabel.text"));
            datosNotificacionTitularJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosNotificacionTitularJPanel.TitleTab")));
            viaNotificacionTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.viaNotificacionTitularJLabel.text"));
            faxTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.faxTitularJLabel.text"));
            telefonoTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.telefonoTitularJLabel.text"));
            movilTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.movilTitularJLabel.text"));
            emailTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailTitularJLabel.text"));
            tipoViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tipoViaTitularJLabel.text")));
            nombreViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.nombreViaTitularJLabel.text")));
            numeroViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.numeroViaTiularJLabel.text")));
            portalTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.portalTitularJLabel.text"));
            plantaTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.plantaTitularJLabel.text"));
            escaleraTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.escaleraTitularJLabel.text"));
            letraTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.letraTitularJLabel.text"));
            cPostalTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.cPostalTitularJLabel.text")));
            municipioTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.municipioTitularJLabel.text")));
            provinciaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.provinciaTitularJLabel.text")));
            notificarTitularJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificarTitularJLabel.text"));

            /** Representante */
            datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosPersonalesRepresentaAJPanel.TitleBorder")));
            DNIRepresenaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.DNIRepresentaAJLabel.text")));
            nombreRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.nombreRepresentaAJLabel.text")));
            apellido1RepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.apellido1RepresentaAJLabel.text"));
            apellido2RepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.apellido2RepresentaAJLabel.text"));
            datosNotificacionRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosNotificacionRepresentaAJPanel.TitleTab")));
            viaNotificacionRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.viaNotificacionRepresentaAJLabel.text"));
            faxRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.faxRepresentaAJLabel.text"));
            telefonoRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.telefonoRepresentaAJLabel.text"));
            movilRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.movilRepresentaAJLabel.text"));
            emailRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailRepresentaAJLabel.text"));
            tipoViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tipoViaRepresentaAJLabel.text")));
            nombreViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.nombreViaRepresentaAJLabel.text")));
            numeroViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.numeroViaTiularJLabel.text")));
            portalRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.portalRepresentaAJLabel.text"));
            plantaRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.plantaRepresentaAJLabel.text"));
            escaleraRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.escaleraRepresentaAJLabel.text"));
            letraRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.letraRepresentaAJLabel.text"));
            cPostalRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.cPostalRepresentaAJLabel.text")));
            municipioRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.municipioRepresentaAJLabel.text")));
            provinciaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.provinciaRepresentaAJLabel.text")));
            notificarRepresentaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificarRepresentaAJLabel.text"));

            /** Tecnico */
            datosPersonalesTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosPersonalesTecnicoJPanel.TitleBorder")));
            DNITecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.DNITecnicoJLabel.text")));
            nombreTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.nombreTecnicoJLabel.text")));
            apellido1TecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.apellido1TecnicoJLabel.text"));
            apellido2TecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.apellido2TecnicoJLabel.text"));
            colegioTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.colegioTecnicoJLabel.text"));
            visadoTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.visadoTecnicoJLabel.text"));
            titulacionTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.titulacionTecnicoJLabel.text"));
            datosNotificacionTecnicoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosNotificacionTecnicoJPanel.TitleTab")));
            viaNotificacionTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.viaNotificacionTecnicoJLabel.text"));
            faxTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.faxTecnicoJLabel.text"));
            telefonoTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.telefonoTecnicoJLabel.text"));
            movilTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.movilTecnicoJLabel.text"));
            emailTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailTecnicoJLabel.text"));
            tipoViaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tipoViaTecnicoJLabel.text")));
            nombreViaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.nombreViaTecnicoJLabel.text")));
            numeroViaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.numeroViaTiularJLabel.text")));
            portalTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.portalTecnicoJLabel.text"));
            plantaTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.plantaTecnicoJLabel.text"));
            escaleraTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.escaleraTecnicoJLabel.text"));
            letraTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.letraTecnicoJLabel.text"));
            cPostalTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.cPostalTecnicoJLabel.text")));
            municipioTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.municipioTecnicoJLabel.text")));
            provinciaTecnicoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.provinciaTecnicoJLabel.text")));
            notificarTecnicoJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificarTecnicoJLabel.text"));

            /** Promotor */
            datosPersonalesPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosPersonalesPromotorJPanel.TitleBorder")));
            DNIPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.DNIPromotorJLabel.text")));
            nombrePromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.nombrePromotorJLabel.text")));
            apellido1PromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.apellido1PromotorJLabel.text"));
            apellido2PromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.apellido2PromotorJLabel.text"));
            colegioPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.colegioPromotorJLabel.text"));
            visadoPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.visadoPromotorJLabel.text"));
            titulacionPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.titulacionPromotorJLabel.text"));
            datosNotificacionPromotorJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosNotificacionPromotorJPanel.TitleTab")));
            viaNotificacionPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.viaNotificacionPromotorJLabel.text"));
            faxPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.faxPromotorJLabel.text"));
            telefonoPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.telefonoPromotorJLabel.text"));
            movilPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.movilPromotorJLabel.text"));
            emailPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.emailPromotorJLabel.text"));
            tipoViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.tipoViaPromotorJLabel.text")));
            nombreViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.nombreViaPromotorJLabel.text")));
            numeroViaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.numeroViaTiularJLabel.text")));
            portalPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.portalPromotorJLabel.text"));
            plantaPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.plantaPromotorJLabel.text"));
            escaleraPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.escaleraPromotorJLabel.text"));
            letraPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.letraPromotorJLabel.text"));
            cPostalPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.cPostalPromotorJLabel.text")));
            municipioPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.municipioPromotorJLabel.text")));
            provinciaPromotorJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.provinciaPromotorJLabel.text")));
            notificarPromotorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificarPromotorJLabel.text"));

            /** Documentacion */
            documentacionJPanel.renombrarComponentes(literales);


            /** notificaciones */
            datosNotificacionesJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosNotificacionesJPanel.TitleBorder")));
            datosNotificacionJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosNotificacionJPanel.TitleBorder")));
            datosNombreApellidosJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosNombreApellidosJLabel.text"));
            datosDireccionJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosDireccionJLabel.text"));
            datosCPostalJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosCPostalJLabel.text"));
            datosNotificarPorJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosNotificarPorJLabel.text"));
            entregadaAJLabel.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosEntregadaAJLabel.text"));

            /** Eventos */
            datosEventosJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosEventosJPanel.TitleBorder")));
            descEventoJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.descEventoJScrollPane.TitleBorder")));

            /** Historico */
            datosHistoricoJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.datosHistoricoJPanel.TitleBorder")));
            apunteJScrollPane.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.apunteJScrollPane.TitleBorder")));

            salirJButton.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.salirJButton.text"));
            modificarJButton.setText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.modificarJButton.text"));
            publicarJButton.setText(literales.getString("CConsultaLicenciasForm.publicarJButton.text"));
            editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.editorMapaJPanel.TitleBorder")));
            generarFichaJButton.setToolTipText(literales.getString("CMantenimientoHistorico.generarFichaJButton.setToolTipText.text"));
            jButtonGenerarFicha.setText(literales.getString("CMainLicencias.jButtonGenerarFicha"));//"Generar Ficha");
            jButtonWorkFlow.setText(literales.getString("CMainLicencias.jButtonWorkFlow"));//"Ver Workflow");
            jLabelFinaliza.setText(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.finalizaJLable.text"));

            jPanelInformes.changeScreenLang(literales);
            jPanelResolucion.changeScreenLang(literales);

            salirJButton.setToolTipText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.salirJButton.text"));
            modificarJButton.setToolTipText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.modificarJButton.toolTipText"));
            publicarJButton.setToolTipText(literales.getString("CConsultaLicenciasForm.publicarJButton.toolTipText"));
            generarFichaJButton.setToolTipText(literales.getString("CMantenimientoHistorico.generarFichaJButton.setToolTipText.text"));
            jButtonGenerarFicha.setToolTipText(literales.getString("CMainLicenciasForm.generarFichaJButton.setToolTipText"));
            jButtonWorkFlow.setToolTipText(literales.getString("CMainLicenciasForm.verWorkFlowJButton.setToolTipText"));
            consultarJButton.setToolTipText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.consultarJButton.text"));
            buscarExpedienteJButton.setToolTipText(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.buscarExpedienteJButton.text"));

            /** Headers de la tabla eventos */
            TableColumn tableColumn= eventosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum1"));
            tableColumn= eventosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum2"));
            tableColumn= eventosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum3"));
            tableColumn= eventosJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum4"));
            tableColumn= eventosJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.eventosJTable.colum5"));

            /** Headers tabla Notificaciones */
            tableColumn= notificacionesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum6"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum1"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum2"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum3"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum4"));
            tableColumn= notificacionesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.notificacionesJTable.colum5"));

            /** Headers tabla Historico */
            tableColumn= historicoJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum2"));
            tableColumn= historicoJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum3"));
            tableColumn= historicoJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum4"));
            tableColumn= historicoJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.historicoJTable.colum5"));

            /** Headers tabla referencias catastrales */
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum1"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum2"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum3"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum4"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum5"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum6"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum7"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum8"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum9"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(9);
            tableColumn.setHeaderValue(literales.getString("CConsultaLicenciasForm.ObraMenorJMenuItem.referenciasCatastralesJTable.colum10"));

        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
	}

	private boolean refreshFeatureSelection() {

		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			MainActividad.geopistaEditor.getSelectionManager().clear();

			GeopistaLayer geopistaLayer = (GeopistaLayer) MainActividad.geopistaEditor.getLayerManager().getLayer("parcelas");

            for (int i=0; i<referenciasCatastralesJTable.getModel().getRowCount(); i++){
                String refCatastral= (String) referenciasCatastralesJTable.getValueAt(i,0);
         		Collection collection = CUtilidadesComponentes.searchByAttribute(geopistaLayer, "Referencia catastral", refCatastral);

				Iterator it = collection.iterator();
				if (it.hasNext()) {
					Feature feature = (Feature) it.next();
					MainActividad.geopistaEditor.select(geopistaLayer, feature);
				}
			}

			MainActividad.geopistaEditor.zoomToSelected();

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

    public void mostrarNotificacionSeleccionada(){
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

            if (notificacion.getEntregadaA() != null)
                entregadaAJTField.setText(notificacion.getEntregadaA());
            else entregadaAJTField.setText("");

        }else{
            clearDatosNotificacion();
        }

    }

    public void clearDatosNotificacion(){
        datosNombreApellidosJTField.setText("");
        datosDireccionJTField.setText("");
        datosCPostalJTField.setText("");
        datosNotificarPorJTField.setText("");
        entregadaAJTField.setText("");
    }


    private void verWorkFlow()
    {
    	try
        {
            if (expedienteLicencia == null)
            {
                mostrarMensaje(literales.getString("CConsultaLicenciasForm.mensaje1"));
                return;
            }

           logger.info("Mostrando workflow para el estado: "+expedienteLicencia.getEstado().getIdEstado());
           JDialogWorkFlow dialogImagen = new JDialogWorkFlow(desktop, expedienteLicencia.getEstado().getIdEstado(),solicitudLicencia.getTipoLicencia().getIdTipolicencia());
           Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
           dialogImagen.setSize(590,720);
           dialogImagen.setLocation(d.width/2 - dialogImagen.getSize().width/2, d.height/2 - dialogImagen.getSize().height/2);
           dialogImagen.setResizable(false);
           dialogImagen.show();
           dialogImagen.setEstado(expedienteLicencia.getEstado().getIdEstado()) ;
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
            CHistorico historico = (CHistorico) _vHistorico.get(((Integer)historicoSorted.getValueAt(row, historicoHiddenCol)).intValue());
            apunteJTArea.setText(historico.getApunte());
        }else{
            apunteJTArea.setText("");
        }
    }

    public void setEnabledModificarJButton(boolean b){
            modificarJButton.setEnabled(b);
    }


    public void setEnabledPublicarJButton(boolean b){
        publicarJButton.setEnabled(b);
    }



	private JFrame desktop;
	private CSolicitudLicencia solicitudLicencia;
    CExpedienteLicencia expedienteLicencia;
	private DefaultTableModel notificacionesJTableModel;
	private DefaultTableModel referenciasCatastralesJTableModel;
	private DefaultTableModel _eventosTableModel;
	private DefaultTableModel _historicoTableModel;

    /** tasa e impuesto */
    private com.geopista.app.utilidades.JNumberTextField tasaTextField;
    private com.geopista.app.utilidades.JNumberTextField impuestoTextField;

    /** pestanna de documentacion de una solicitud (documentacion requerida, anexos...) */
    private DocumentacionLicenciasJPanel documentacionJPanel;

    /** pestanna de los datos de actividad */
    private com.geopista.app.licencias.actividad.datosActividad.DatosActividadJPanel datosActividadJPanel;

    /** cnae */
    private com.geopista.app.utilidades.TextField cnaeTField;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DNIPromotorJLabel;
    private javax.swing.JTextField DNIPromotorJTField;
    private javax.swing.JLabel DNIRepresenaAJLabel;
    private javax.swing.JTextField DNIRepresentaAJTField;
    private javax.swing.JLabel DNITecnicoJLabel;
    private javax.swing.JTextField DNITecnicoJTField;
    private javax.swing.JLabel DNITitularJLabel;
    private javax.swing.JTextField DNITitularJTField;
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
    private javax.swing.JScrollPane apunteJScrollPane;
    private javax.swing.JTextArea apunteJTArea;
    private javax.swing.JLabel asuntoExpedienteJLabel;
    private javax.swing.JTextField asuntoExpedienteJTField;
    private javax.swing.JLabel asuntoJLabel;
    private javax.swing.JTextField asuntoJTField;
    private javax.swing.JPanel botoneraJPanel;
    private javax.swing.JButton buscarExpedienteJButton;
    private javax.swing.JLabel cPostalJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalJTextField;
    private javax.swing.JLabel cPostalPromotorJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalPromotorJTField;
    private javax.swing.JLabel cPostalRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalRepresentaAJTField;
    private javax.swing.JLabel cPostalTecnicoJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalTecnicoJTField;
    private javax.swing.JLabel cPostalTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalTitularJTField;
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
    private javax.swing.JPanel datosNotificacionRepresentaAJPanel;
    private javax.swing.JPanel datosNotificacionTecnicoJPanel;
    private javax.swing.JPanel datosNotificacionTitularJPanel;
    private javax.swing.JPanel datosNotificacionesJPanel;
    private javax.swing.JLabel datosNotificarPorJLabel;
    private javax.swing.JTextField datosNotificarPorJTField;
    private javax.swing.JPanel datosPersonalesPromotorJPanel;
    private javax.swing.JPanel datosPersonalesRepresentaAJPanel;
    private javax.swing.JPanel datosPersonalesTecnicoJPanel;
    private javax.swing.JPanel datosPersonalesTitularJPanel;
    private javax.swing.JPanel datosSolicitudJPanel;
    private javax.swing.JTabbedPane datosSolicitudJTabbedPane;
    private javax.swing.JTabbedPane jTabbedPaneSolicitud= new JTabbedPane();
    private javax.swing.JScrollPane descEventoJScrollPane;
    private javax.swing.JTextArea descEventoJTArea;
    private javax.swing.JPanel editorMapaJPanel;
    private javax.swing.JLabel emailPromotorJLabel;
    private javax.swing.JTextField emailPromotorJTField;
    private javax.swing.JLabel emailRepresentaAJLabel;
    private javax.swing.JTextField emailRepresentaAJTField;
    private javax.swing.JLabel emailTecnicoJLabel;
    private javax.swing.JTextField emailTecnicoJTField;
    private javax.swing.JLabel emailTitularJLabel;
    private javax.swing.JTextField emailTitularJTField;
    private javax.swing.JPanel emplazamientoJPanel;
    private javax.swing.JLabel entregadaAJLabel;
    private javax.swing.JTextField entregadaAJTField;
    private javax.swing.JLabel escaleraPromotorJLabel;
    private javax.swing.JTextField escaleraPromotorJTField;
    private javax.swing.JLabel escaleraRepresentaAJLabel;
    private javax.swing.JTextField escaleraRepresentaAJTField;
    private javax.swing.JLabel escaleraTecnicoJLabel;
    private javax.swing.JTextField escaleraTecnicoJTField;
    private javax.swing.JLabel escaleraTitularJLabel;
    private javax.swing.JTextField escaleraTitularJTField;
    private javax.swing.JLabel estadoExpedienteJLabel;
    private javax.swing.JTextField estadoExpedienteJTextField;
    private javax.swing.JPanel eventosJPanel;
    private javax.swing.JScrollPane eventosJScrollPane;
    private javax.swing.JTable eventosJTable;
    private javax.swing.JPanel expedienteJPanel;
    private javax.swing.JLabel faxPromotorJLabel;
    private javax.swing.JTextField faxPromotorJTField;
    private javax.swing.JLabel faxRepresentaAJLabel;
    private javax.swing.JTextField faxRepresentaAJTField;
    private javax.swing.JLabel faxTecnicoJLabel;
    private javax.swing.JTextField faxTecnicoJTField;
    private javax.swing.JLabel faxTitularJLabel;
    private javax.swing.JTextField faxTitularJTField;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JTextField fechaAperturaJTField;
    private javax.swing.JLabel fechaLimiteObraJLabel;
    private javax.swing.JTextField fechaLimiteObraJTField;
    private javax.swing.JLabel fechaSolicitudJLabel;
    private javax.swing.JTextField fechaSolicitudJTField;
    private javax.swing.JButton generarFichaJButton;
    private javax.swing.JPanel historicoJPanel;
    private javax.swing.JScrollPane historicoJScrollPane;
    private javax.swing.JTable historicoJTable;
    private javax.swing.JLabel impuestoJLabel;
    private javax.swing.JLabel inicioJLabel;
    private javax.swing.JTextField inicioJTField;
    private javax.swing.JTextField letraJTextField;
    private javax.swing.JLabel letraPromotorJLabel;
    private javax.swing.JTextField letraPromotorJTField;
    private javax.swing.JLabel letraRepresentaAJLabel;
    private javax.swing.JTextField letraRepresentaAJTField;
    private javax.swing.JLabel letraTecnicoJLabel;
    private javax.swing.JTextField letraTecnicoJTField;
    private javax.swing.JLabel letraTitularJLabel;
    private javax.swing.JTextField letraTitularJTField;
    private javax.swing.JButton modificarJButton;
    private javax.swing.JButton publicarJButton;
    private javax.swing.JLabel motivoJLabel;
    private javax.swing.JTextField motivoJTField;
    private javax.swing.JLabel movilPromotorJLabel;
    private javax.swing.JTextField movilPromotorJTField;
    private javax.swing.JLabel movilRepresentaAJLabel;
    private javax.swing.JTextField movilRepresentaAJTField;
    private javax.swing.JLabel movilTecnicoJLabel;
    private javax.swing.JTextField movilTecnicoJTField;
    private javax.swing.JLabel movilTitularJLabel;
    private javax.swing.JTextField movilTitularJTField;
    private javax.swing.JTextField municipioJTextField;
    private javax.swing.JLabel municipioPromotorJLabel;
    private javax.swing.JTextField municipioPromotorJTField;
    private javax.swing.JLabel municipioRepresentaAJLabel;
    private javax.swing.JTextField municipioRepresentaAJTField;
    private javax.swing.JLabel municipioTecnicoJLabel;
    private javax.swing.JTextField municipioTecnicoJTField;
    private javax.swing.JLabel municipioTitularJLabel;
    private javax.swing.JTextField municipioTitularJTField;
    private javax.swing.JLabel nombrePromotorJLabel;
    private javax.swing.JTextField nombrePromotorJTField;
    private javax.swing.JLabel nombreRepresentaAJLabel;
    private javax.swing.JTextField nombreRepresentaAJTField;
    private javax.swing.JLabel nombreTecnicoJLabel;
    private javax.swing.JTextField nombreTecnicoJTField;
    private javax.swing.JLabel nombreTitularJLabel;
    private javax.swing.JTextField nombreTitularJTField;
    private javax.swing.JLabel nombreViaJLabel;
    private javax.swing.JTextField nombreViaJTextField;
    private javax.swing.JLabel nombreViaPromotorJLabel;
    private javax.swing.JTextField nombreViaPromotorJTField;
    private javax.swing.JLabel nombreViaRepresentaAJLabel;
    private javax.swing.JTextField nombreViaRepresentaAJTField;
    private javax.swing.JLabel nombreViaTecnicoJLabel;
    private javax.swing.JTextField nombreViaTecnicoJTField;
    private javax.swing.JLabel nombreViaTitularJLabel;
    private javax.swing.JTextField nombreViaTitularJTField;
    private javax.swing.JLabel notaJLabel;
    private javax.swing.JPanel notificacionesJPanel;
    private javax.swing.JScrollPane notificacionesJScrollPane;
    private javax.swing.JTable notificacionesJTable;
    private javax.swing.JCheckBox notificarPromotorJCheckBox;
    private javax.swing.JLabel notificarPromotorJLabel;
    private javax.swing.JCheckBox notificarRepresentaAJCheckBox;
    private javax.swing.JLabel notificarRepresentaAJLabel;
    private javax.swing.JCheckBox notificarTecnicoJCheckBox;
    private javax.swing.JLabel notificarTecnicoJLabel;
    private javax.swing.JCheckBox notificarTitularJCheckBox;
    private javax.swing.JLabel notificarTitularJLabel;
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JLabel numRegistroJLabel;
    private javax.swing.JTextField numRegistroJTField;
    private javax.swing.JLabel numeroViaJLabel;
    private javax.swing.JTextField numeroViaJTextField;
    private javax.swing.JLabel numeroViaPromotorJLabel;
    private javax.swing.JTextField numeroViaPromotorJTField;
    private javax.swing.JLabel numeroViaRepresentaAJLabel;
    private javax.swing.JTextField numeroViaRepresentaAJTField;
    private javax.swing.JLabel numeroViaTecnicoJLabel;
    private javax.swing.JTextField numeroViaTecnicoJTField;
    private javax.swing.JLabel numeroViaTitularJLabel;
    private javax.swing.JTextField numeroViaTitularJTField;
    private javax.swing.JLabel observacionesExpedienteJLabel;
    private javax.swing.JScrollPane observacionesExpedienteJScrollPane;
    private javax.swing.JTextArea observacionesExpedienteJTArea;
    private javax.swing.JLabel observacionesJLabel;
    private javax.swing.JScrollPane observacionesJScrollPane;
    private javax.swing.JTextArea observacionesJTArea;
    private javax.swing.JTextField plantaJTextField;
    private javax.swing.JLabel plantaPromotorJLabel;
    private javax.swing.JTextField plantaPromotorJTField;
    private javax.swing.JLabel plantaRepresentaAJLabel;
    private javax.swing.JTextField plantaRepresentaAJTField;
    private javax.swing.JLabel plantaTecnicoJLabel;
    private javax.swing.JTextField plantaTecnicoJTField;
    private javax.swing.JLabel plantaTitularJLabel;
    private javax.swing.JTextField plantaTitularJTField;
    private javax.swing.JTextField portalJTextField;
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
    private javax.swing.JTextField provinciaJTextField;
    private javax.swing.JLabel provinciaPromotorJLabel;
    private javax.swing.JTextField provinciaPromotorJTField;
    private javax.swing.JLabel provinciaRepresentaAJLabel;
    private javax.swing.JTextField provinciaRepresentaAJTField;
    private javax.swing.JLabel provinciaTecnicoJLabel;
    private javax.swing.JTextField provinciaTecnicoJTField;
    private javax.swing.JLabel provinciaTitularJLabel;
    private javax.swing.JTextField provinciaTitularJTField;
    private javax.swing.JScrollPane referenciasCatastralesJScrollPane;
    private javax.swing.JTable referenciasCatastralesJTable;
    private javax.swing.JPanel representaAJPanel;
    private javax.swing.JLabel responsableJLabel;
    private javax.swing.JTextField responsableJTextField;
    private javax.swing.JButton salirJButton;
    private javax.swing.JLabel servicioExpedienteJLabel;
    private javax.swing.JTextField servicioExpedienteJTField;
    private javax.swing.JCheckBox silencioJCheckBox;
    private javax.swing.JLabel silencioJLabel;
    private javax.swing.JPanel solicitudJPanel;
    private javax.swing.JLabel tasaJLabel;
    private javax.swing.JPanel tecnicoJPanel;
    private javax.swing.JLabel telefonoPromotorJLabel;
    private javax.swing.JTextField telefonoPromotorJTField;
    private javax.swing.JLabel telefonoRepresentaAJLabel;
    private javax.swing.JTextField telefonoRepresentaAJTField;
    private javax.swing.JLabel telefonoTecnicoJLabel;
    private javax.swing.JTextField telefonoTecnicoJTField;
    private javax.swing.JLabel telefonoTitularJLabel;
    private javax.swing.JTextField telefonoTitularJTField;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoObraJLabel;
    private javax.swing.JCheckBox jCheckBoxActividadNoCalificada;
    private javax.swing.JTextField tipoObraJTextField;
    private javax.swing.JTextField tipoViaJTextField;
    private javax.swing.JTextField tipoViaNotificacionPromotorJTField;
    private javax.swing.JTextField tipoViaNotificacionRepresentaAJTField;
    private javax.swing.JTextField tipoViaNotificacionTecnicoJTField;
    private javax.swing.JTextField tipoViaNotificacionTitularJTField;
    private javax.swing.JLabel tipoViaPromotorJLabel;
    private javax.swing.JLabel tipoViaRepresentaAJLabel;
    private javax.swing.JLabel tipoViaTecnicoJLabel;
    private javax.swing.JLabel tipoViaTitularJLabel;
    private javax.swing.JLabel titulacionPromotorJLabel;
    private javax.swing.JTextField titulacionPromotorJTField;
    private javax.swing.JLabel titulacionTecnicoJLabel;
    private javax.swing.JTextField titulacionTecnicoJTField;
    private javax.swing.JPanel titularJPanel;
    private javax.swing.JLabel tramitacionJLabel;
    private javax.swing.JTextField tramitacionJTextField;
    private javax.swing.JLabel unidadRJLabel;
    private javax.swing.JTextField unidadRJTField;
    private javax.swing.JLabel unidadTJLabel;
    private javax.swing.JTextField unidadTJTField;
    private javax.swing.JLabel viaNotificacionPromotorJLabel;
    private javax.swing.JTextField viaNotificacionPromotorJTField;
    private javax.swing.JLabel viaNotificacionRepresentaAJLabel;
    private javax.swing.JTextField viaNotificacionRepresentaAJTField;
    private javax.swing.JLabel viaNotificacionTecnicoJLabel;
    private javax.swing.JTextField viaNotificacionTecnicoJTField;
    private javax.swing.JLabel viaNotificacionTitularJLabel;
    private javax.swing.JTextField viaNotificacionTitularJTField;
    private javax.swing.JLabel visadoPromotorJLabel;
    private javax.swing.JTextField visadoPromotorJTField;
    private javax.swing.JLabel visadoTecnicoJLabel;
    private javax.swing.JTextField visadoTecnicoJTField;
    private javax.swing.JButton jButtonGenerarFicha;
    private javax.swing.JButton jButtonWorkFlow;
    private JPanelInformes jPanelInformes;
    private JPanelResolucion jPanelResolucion;
    private javax.swing.JLabel jLabelFinaliza;
    private javax.swing.JTextField jTextFieldFinaliza;
    private javax.swing.JLabel cnaeJLabel;
    // End of variables declaration//GEN-END:variables

}
