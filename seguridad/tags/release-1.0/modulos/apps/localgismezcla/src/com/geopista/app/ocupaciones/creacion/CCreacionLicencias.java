package com.geopista.app.ocupaciones.creacion;
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
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.ocupaciones.CAddressJDialog;
import com.geopista.app.ocupaciones.CCalendarJDialog;
import com.geopista.app.ocupaciones.CConstantesOcupaciones;
import com.geopista.app.ocupaciones.CDatosReferenciaCatastralJDialog;
import com.geopista.app.ocupaciones.CMainOcupaciones;
import com.geopista.app.ocupaciones.CPersonaJDialog;
import com.geopista.app.ocupaciones.CSearchLicenciasObraJDialog;
import com.geopista.app.ocupaciones.CUtilidades;
import com.geopista.app.ocupaciones.CUtilidadesComponentes;
import com.geopista.app.ocupaciones.IMultilingue;
import com.geopista.app.ocupaciones.consulta.CConsultaLicencias;
import com.geopista.app.ocupaciones.estructuras.Estructuras;
import com.geopista.app.ocupaciones.tableModels.CListaAnexosTableModel;
import com.geopista.app.ocupaciones.tableModels.CReferenciasCatastralesTableModel;
import com.geopista.app.ocupaciones.utilidades.ComboBoxRenderer;
import com.geopista.app.ocupaciones.utilidades.ComboBoxTableEditor;
import com.geopista.app.ocupaciones.utilidades.TextFieldRenderer;
import com.geopista.app.ocupaciones.utilidades.TextFieldTableEditor;
import com.geopista.app.utilidades.GeoPistaFileFilter;
import com.geopista.app.utilidades.JDialogConfiguracion;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.app.utilidades.estructuras.CellRendererEstructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxRendererEstructuras;
import com.geopista.app.utilidades.um.filteredtext.JTimeTextField;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.contaminantes.NumeroPolicia;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.licencias.CAnexo;
import com.geopista.protocol.licencias.CDatosNotificacion;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.CViaNotificacion;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.tipos.CTipoAnexo;
import com.geopista.protocol.ocupacion.CDatosOcupacion;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author avivar
 */
public class CCreacionLicencias extends javax.swing.JInternalFrame implements IMultilingue{


	Logger logger = Logger.getLogger(CConsultaLicencias.class);
    private final String layerName="ocupaciones";
    private Hashtable nuevasFeatures= new Hashtable();
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
    private String _numeroViaRepresentaA= "";
	private String _portalRepresentaA = "";
	private String _plantaRepresentaA = "";
	private String _escaleraRepresentaA = "";
	private String _letraRepresentaA = "";
	private String _cPostalRepresentaA = "";
	private String _municipioRepresentaA = "";
	private String _provinciaRepresentaA = "";
	private int _seNotificaRepresentaA = 0;
	private boolean _flagRepresentante = false;
    private boolean emailRepresentanteObligatorio= false;


	/**
	 * Datos de la solicitud
	 */
	// Tendra un valor u otro en funcion del TabbedPane seleccionado (CConstantesOcupaciones.ObraMayor)
    private String _motivo = "";
	private String _asunto = "";
	private String _observaciones = "";

/**
	 * Modelo para el componente listaAnexosJTable
	 */
	private DefaultTableModel _listaAnexosTableModel;

    private long maxSizeFilesUploaded= 0;

	/**
	 * Creates new form CCreacionLicencias
	 */
	public CCreacionLicencias(JFrame desktopFrame) {
		CCreacionLicencias.desktop=desktopFrame;
		CUtilidadesComponentes.menuLicenciasSetEnabled(false, CCreacionLicencias.desktop);

		initComponents();
		initComboBoxesEstructuras();

		configureComponents();

		fechaSolicitudJTField.setText(showToday());

		estadoExpedienteEJCBox.setEnabled(false);
        estadoExpedienteEJCBox.setEditable(true);

		CUtilidadesComponentes.showGeopistaEditor(desktop,editorMapaJPanel, CMainOcupaciones.geopistaEditor, true);

        CMainOcupaciones.geopistaEditor.removeAllGeopistaListener();
		CMainOcupaciones.geopistaEditor.addGeopistaListener(new GeopistaListener() {

			public void selectionChanged(AbstractSelection abtractSelection) {
				logger.info("selectionChanged");
			}

			public void featureAdded(FeatureEvent e) {
                try
                {
                    Collection features = e.getFeatures();
                    GeopistaLayer layer= (GeopistaLayer)CMainOcupaciones.geopistaEditor.getLayerManager().getLayer(layerName);
                    for (Iterator i = features.iterator(); i.hasNext();) {
                        Feature f = (Feature) i.next();

                        GeopistaFeature geoF = ShowMaps.saveFeature(f, layer, layerName,com.geopista.protocol.CConstantesComando.adminCartografiaUrl);
                        if (geoF.getSystemId() == null || geoF.getSystemId().length() <= 0) {
                            JOptionPane optionPane = new JOptionPane(CMainOcupaciones.literales.getString("CCreacionLicencias.insertError"), JOptionPane.ERROR_MESSAGE);
                            JDialog dialog = optionPane.createDialog(CCreacionLicencias.desktop, "ERROR");
                            dialog.show();
                            CMainOcupaciones.geopistaEditor.deleteSelectedFeatures();
                        } else {
                        	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(CCreacionLicencias.desktop, null);
                        	progressDialog.setTitle(CMainOcupaciones.literales.getString("TaskMonitor.Wait"));
                        	progressDialog.report(CMainOcupaciones.literales.getString("direccionMasCercana.progressdialog.text"));
                        	final Runnable runnable = new Runnable(){
                        		private Feature f;
                        		GeopistaFeature geoF;

                        		public Runnable ctor(Feature f, GeopistaFeature geoF){
                        			this.f = f;
                        			this.geoF = geoF;
                        			return this;
                        		}

                        		public void run(){
                        			/* añadimos el documento a la lista */
                        			try{
                        				((GeopistaFeature) f).setSystemId(geoF.getSystemId());
                        				f.setAttribute(0,new Integer(geoF.getSystemId()));
                        				nuevasFeatures.put(geoF.getSystemId(),f);
                        				refreshFeatureSelection(layerName, 0,geoF.getSystemId());
                        				NumeroPolicia numeroPolicia= new NumeroPolicia();
                        				//String geom = f.getAttribute(3).toString();
                        				String geom = transformFeatureGeom((GeopistaFeature) geoF);
                        				CResultadoOperacion resultado= COperacionesLicencias.getDireccionMasCercana(geom);

                        				if (resultado.getResultado()) {
                        					numeroPolicia= (NumeroPolicia)resultado.getVector().elementAt(0);
                        					JOptionPane optionPane = new JOptionPane(CMainOcupaciones.literales.getString("direccionMasCercana.encontrada.text")+" "+numeroPolicia.toString(), JOptionPane.INFORMATION_MESSAGE);
                        					JDialog dialog = optionPane.createDialog(CCreacionLicencias.desktop, "INFO");
                        					dialog.show();
                        				}
                        				else {
                        					JOptionPane optionPane = new JOptionPane(CMainOcupaciones.literales.getString("direccionMasCercana.noencontrada.text"), JOptionPane.INFORMATION_MESSAGE);
                        					JDialog dialog = optionPane.createDialog(CCreacionLicencias.desktop, "INFO");
                        					dialog.show();
                        					logger.info("No se ha encontrado la direccion: "+resultado.getDescripcion());
                        				}

                        				referenciasCatastralesJTableModel.addRow(new Object[]{
                                                geoF.getSystemId(),
                                                new Long(((Number)geoF.getAttribute(2)).longValue()),
                                                numeroPolicia.getTipovia()==null?"":numeroPolicia.getTipovia(),
                                                numeroPolicia.getNombrevia()==null?"":numeroPolicia.getNombrevia(),
                                                numeroPolicia.getRotulo()==null?"":numeroPolicia.getRotulo(),
                                                "","","","",""});
                        			}
                        			catch(Exception e){
                        				// No hacemos nada
                        				return;
                        			}
                        			finally{
                        				progressDialog.setVisible(false);
                        			}
                        		}

								private String transformFeatureGeom(GeopistaFeature targetFeature) throws Exception {

									ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
									String sridInicial = AppContext.getApplicationContext().getClient().getSRIDDefecto(true, Integer.parseInt(iSesion.getIdEntidad()));
 							        String sridDefecto = AppContext.getApplicationContext().getClient().getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
 							        CoordinateSystem inCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridDefecto));
						            CoordinateSystem outCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridInicial));
			                        Feature currentFeatureTransform = (Feature) targetFeature.clone();
			                        Geometry geom = currentFeatureTransform.getGeometry();
			                        geom.setSRID(inCoord.getEPSGCode());
			                        if (inCoord.getEPSGCode() != outCoord.getEPSGCode())
			                        	Reprojector.instance().reproject(geom,inCoord, outCoord);
			                        geom.setSRID(outCoord.getEPSGCode());
				                        currentFeatureTransform.setGeometry(geom);
									return currentFeatureTransform.getGeometry().toString();
								}
                        	}.ctor(f, geoF);

                        	progressDialog.addComponentListener(new ComponentAdapter(){
                                public void componentShown(ComponentEvent e){
                                    new Thread(runnable).start();
                                }
                        	});

                        	GUIUtil.centreOnWindow(progressDialog);
                        	progressDialog.setVisible(true);
                        }
                    }
                    logger.info("featureAdded");
                }catch(Exception ex)
                {
                    logger.error("Excepcion: ",ex);
                }
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

			public void featureActioned(AbstractSelection abtractSelection) {
				logger.info("featureActioned");
			}

		});

		municipioJTField.setText(CConstantesOcupaciones.Municipio);
		provinciaJTField.setText(CConstantesOcupaciones.Provincia);

		cargarTiposAnexosJTable();

		eliminarJButton.setEnabled(false);
        abrirJButton.setEnabled(false);

	}


	private void configureComponents() {

		long tiempoInicial = new Date().getTime();
		if (CMainOcupaciones.geopistaEditor == null) CMainOcupaciones.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");
		logger.info("TIME (new GeopistaEditor()): " + (new Date().getTime() - tiempoInicial));

		String[] columnNamesAnexos = {CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.listaAnexosJTable.column1"),
									  CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.listaAnexosJTable.column2"),
									  CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.listaAnexosJTable.column3")};

		CListaAnexosTableModel.setColumnNames(columnNamesAnexos);
		_listaAnexosTableModel = new CListaAnexosTableModel();
		listaAnexosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaAnexosJTable.setModel(_listaAnexosTableModel);
		listaAnexosJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int j = 0; j < listaAnexosJTable.getColumnCount(); j++) {
			TableColumn column = listaAnexosJTable.getColumnModel().getColumn(j);
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


		referenciasCatastralesJTableModel = new CReferenciasCatastralesTableModel(new String[]{CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column1"),
                                                                               CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column10"),
																			   CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column2"),
																			   CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column3"),
																			   CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column4"),
																			   CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column5"),
																			   CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column6"),
																			   CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column7"),
																			   CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column8"),
																			   CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column9")});

		referenciasCatastralesJTable.setModel(referenciasCatastralesJTableModel);
		referenciasCatastralesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		referenciasCatastralesJTable.setCellSelectionEnabled(false);
		referenciasCatastralesJTable.setColumnSelectionAllowed(false);
		referenciasCatastralesJTable.setRowSelectionAllowed(true);

		referenciasCatastralesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        referenciasCatastralesJTable.getTableHeader().setReorderingAllowed(false);
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
            new CellRendererEstructuras(CMainOcupaciones.literales.getLocale().toString(),com.geopista.app.ocupaciones.estructuras.Estructuras.getListaTiposViaINE());
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

        fechaSolicitudJTField.setEnabled(false);

        fechaSolicitudJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		nombreJButton.setIcon(CUtilidadesComponentes.iconoZoom);

		tableToMapOneJButton.setIcon(CUtilidadesComponentes.iconoFlecha);
		tableToMapAllJButton.setIcon(CUtilidadesComponentes.iconoDobleFlecha);

		buscarDNITitularJButton.setIcon(CUtilidadesComponentes.iconoZoom);
		buscarDNIRepresentaAJButton.setIcon(CUtilidadesComponentes.iconoZoom);

        buscarNumExpedienteJButton.setIcon(CUtilidadesComponentes.iconoZoom);

		numSillasJNTField = new JNumberTextField(JNumberTextField.NUMBER, new Integer(999999), true);
		datosSolicitudJPanel.add(numSillasJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 120, 120, -1));

		numMesasJNTField = new JNumberTextField(JNumberTextField.NUMBER, new Integer(999999), true);
		datosSolicitudJPanel.add(numMesasJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 150, -1));

		areaOcupacionJNTField = new JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
		datosSolicitudJPanel.add(areaOcupacionJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 130, -1));

		m2AceraJNTField = new JNumberTextField(JNumberTextField.REAL, new Integer(99999), true, 2);
		datosSolicitudJPanel.add(m2AceraJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 100, -1));

		m2CalzadaJNTField = new JNumberTextField(JNumberTextField.REAL, new Integer(99999), true, 2);
		datosSolicitudJPanel.add(m2CalzadaJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 100, -1));

		m2AparcamientoJNTField = new JNumberTextField(JNumberTextField.REAL, new Integer(99999), true, 2);
		datosSolicitudJPanel.add(m2AparcamientoJNTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 100, -1));


		numSillasJNTField.setNumber(new Integer(0));
		numMesasJNTField.setNumber(new Integer(0));
		areaOcupacionJNTField.setNumber(new Double(0));
		m2AceraJNTField.setNumber(new Double(0));
		m2CalzadaJNTField.setNumber(new Double(0));
		m2AparcamientoJNTField.setNumber(new Double(0));


		horaInicioJTimeTField = new JTimeTextField(JTimeTextField.BASE_HM);
		datosSolicitudJPanel.add(horaInicioJTimeTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, 50, -1));
		horaFinJTimeTField = new JTimeTextField(JTimeTextField.BASE_HM);
		datosSolicitudJPanel.add(horaFinJTimeTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 100, 50, -1));

		horaInicioJTimeTField.setText("00:00");
		horaFinJTimeTField.setText("00:00");

        municipioJTField.setText(CConstantesOcupaciones.Municipio);
        provinciaJTField.setText(CConstantesOcupaciones.Provincia);

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
        emplazamientoJPanel.add(deleteGeopistaFeatureJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 200, 20, 20));

        /** Anexos */
        abrirJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoAbrir);
        annadirJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoAdd);
        eliminarJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoRemove);

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
        numExpedienteJTextField = new javax.swing.JTextField();
        motivoJTField = new javax.swing.JTextField();
        asuntoJTField = new javax.swing.JTextField();
        fechaSolicitudJTField = new javax.swing.JTextField();
        observacionesJScrollPane = new javax.swing.JScrollPane();
        observacionesJTArea = new javax.swing.JTextArea();
        fechaSolicitudJButton = new javax.swing.JButton();
        tasaJLabel = new javax.swing.JLabel();
        afectaCalzadaJCheckBox = new javax.swing.JCheckBox();
        unidadTJLabel1 = new javax.swing.JLabel();
        afectaAceraJCheckBox = new javax.swing.JCheckBox();
        afectaAparcamientoJCheckBox = new javax.swing.JCheckBox();
        necesitaObraJCheckBox = new javax.swing.JCheckBox();
        metros2JLabel = new javax.swing.JLabel();
        areaOcupacionJLabel = new javax.swing.JLabel();
        numMesasJLabel = new javax.swing.JLabel();
        areaOcupacionJLabel2 = new javax.swing.JLabel();
        numSillasJLabel = new javax.swing.JLabel();
        buscarNumExpedienteJButton = new javax.swing.JButton();
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
        tableToMapOneJButton = new javax.swing.JButton();
        tableToMapAllJButton = new javax.swing.JButton();
        anexosJPanel = new javax.swing.JPanel();
        annadirJButton = new javax.swing.JButton();
        eliminarJButton = new javax.swing.JButton();
        listaAnexosJScrollPane = new javax.swing.JScrollPane();
        listaAnexosJTable = new javax.swing.JTable();
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
        botoneraJPanel = new javax.swing.JPanel();
        aceptarJButton = new javax.swing.JButton();
        cancelarJButton = new javax.swing.JButton();
        editorMapaJPanel = new javax.swing.JPanel();
        deleteGeopistaFeatureJButton= new javax.swing.JButton();
        borrarRepresentanteJButton = new javax.swing.JButton();
        areaOcupacionJButton = new javax.swing.JButton();
        afectaJLabel = new javax.swing.JLabel();
        abrirJButton = new javax.swing.JButton();

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

        templateJPanel.setLayout(new BorderLayout());//org.netbeans.lib.awtextra.AbsoluteLayout());

        obraMayorJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));


        obraMayorJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder("Datos solicitud"));
        datosSolicitudJPanel.setAutoscrolls(true);
        datosSolicitudJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 100, 20));
        datosSolicitudJPanel.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 20));
        datosSolicitudJPanel.add(unidadTJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 20));

        unidadRJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(unidadRJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 100, 20));
        datosSolicitudJPanel.add(motivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 100, 20));
        asuntoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(asuntoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, 100, 20));
        fechaSolicitudJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(fechaSolicitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 100, 20));
        datosSolicitudJPanel.add(observacionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 100, 20));
        datosSolicitudJPanel.add(numExpedienteJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, 100, -1));
        datosSolicitudJPanel.add(motivoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 150, -1));
        datosSolicitudJPanel.add(asuntoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 80, 120, -1));

        fechaSolicitudJTField.setEditable(true);
        datosSolicitudJPanel.add(fechaSolicitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, 100, -1));

        observacionesJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        observacionesJTArea.setLineWrap(true);
        observacionesJTArea.setRows(3);
        observacionesJTArea.setTabSize(4);
        observacionesJTArea.setWrapStyleWord(true);
        observacionesJTArea.setBorder(null);
        observacionesJTArea.setMaximumSize(new java.awt.Dimension(102, 51));
        observacionesJTArea.setMinimumSize(new java.awt.Dimension(102, 51));
        observacionesJTArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                observacionesJTAreaKeyReleased();
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                observacionesJTAreaKeyTyped();
            }
        });

        observacionesJScrollPane.setViewportView(observacionesJTArea);

        datosSolicitudJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 182, 390, 40));

        fechaSolicitudJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaSolicitudJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        fechaSolicitudJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaSolicitudJButtonActionPerformed();
            }
        });

        datosSolicitudJPanel.add(fechaSolicitudJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 40, 20, 20));

        areaOcupacionJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoCalculadora);
        areaOcupacionJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        areaOcupacionJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        areaOcupacionJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcularArea();
            }
        });
        datosSolicitudJPanel.add(areaOcupacionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, 20, 20));


        tasaJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(tasaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, 90, 20));
        datosSolicitudJPanel.add(afectaCalzadaJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 140, 100, 20));
        datosSolicitudJPanel.add(unidadTJLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 100, 20));

        datosSolicitudJPanel.add(afectaAceraJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 120, 20));

        datosSolicitudJPanel.add(afectaAparcamientoJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, 100, 20));

        datosSolicitudJPanel.add(necesitaObraJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 40, 20));
        datosSolicitudJPanel.add(metros2JLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 100, 20));
        datosSolicitudJPanel.add(areaOcupacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 100, 20));
        datosSolicitudJPanel.add(numMesasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 20));
        datosSolicitudJPanel.add(afectaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 100, 20));
        datosSolicitudJPanel.add(areaOcupacionJLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 100, 20));
        numSillasJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        datosSolicitudJPanel.add(numSillasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, 100, 20));
        buscarNumExpedienteJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarNumExpedienteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarNumExpedienteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarNumExpedienteJButtonActionPerformed();
            }
        });
        datosSolicitudJPanel.add(buscarNumExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 60, 20, 20));
        obraMayorJPanel.add(datosSolicitudJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 230));
        emplazamientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        emplazamientoJPanel.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 20));
        emplazamientoJPanel.add(numeroViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 150, 20));
        emplazamientoJPanel.add(cPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 20));
        emplazamientoJPanel.add(provinciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 150, 20));

        municipioJTField.setEnabled(false);
        emplazamientoJPanel.add(municipioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 240, -1));

        provinciaJTField.setEnabled(false);
        emplazamientoJPanel.add(provinciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 330, -1));

        referenciasCatastralesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                referenciasCatastralesJTableMouseClicked();
            }
        });

        referenciasCatastralesJScrollPane.setViewportView(referenciasCatastralesJTable);

        emplazamientoJPanel.add(referenciasCatastralesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 470, 110));

        nombreJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        nombreJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        nombreJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(nombreJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 20, 20));

        tableToMapOneJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableToMapOneJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        tableToMapOneJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableToMapOneJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(tableToMapOneJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 110, 20, 20));

        tableToMapAllJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableToMapAllJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        tableToMapAllJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableToMapAllJButtonActionPerformed();
            }
        });

        emplazamientoJPanel.add(tableToMapAllJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 130, 20, 20));

        obraMayorJPanel.add(emplazamientoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 520, 230));

        anexosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        annadirJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                annadirJButtonMouseClicked();
            }
        });

        anexosJPanel.add(annadirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, 20, 20));

        eliminarJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eliminarJButtonMouseClicked();
            }
        });

        anexosJPanel.add(eliminarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 20, 20));

        abrirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirJButtonActionPerformed();
            }
        });

        anexosJPanel.add(abrirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 20, 20));


        listaAnexosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                listaAnexosJTableFocusGained();
            }
        });

        listaAnexosJScrollPane.setViewportView(listaAnexosJTable);

        //anexosJPanel.add(listaAnexosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 400, 60));
        anexosJPanel.add(listaAnexosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 470, 80));

        obraMayorJPanel.add(anexosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, 520, 110));

        titularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosPersonalesTitularJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesTitularJPanel.add(DNITitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));
        DNITitularJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNITitularJTFieldKeyReleased();
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNITitularJTFieldKeyTyped();
            }
        });

        datosPersonalesTitularJPanel.add(DNITitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 280, -1));
        datosPersonalesTitularJPanel.add(nombreTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, 20));
        datosPersonalesTitularJPanel.add(nombreTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 300, -1));
        datosPersonalesTitularJPanel.add(apellido1TitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 81, 130, 20));
        datosPersonalesTitularJPanel.add(apellido2TitularJLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 102, 130, 20));
        datosPersonalesTitularJPanel.add(apellido1TitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 81, 300, -1));
        datosPersonalesTitularJPanel.add(apellido2TitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 102, 300, -1));
        buscarDNITitularJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNITitularJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNITitularJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNITitularJButtonActionPerformed();
            }
        });

        datosPersonalesTitularJPanel.add(buscarDNITitularJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 20, 20));
        titularJPanel.add(datosPersonalesTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 140));
        datosNotificacionTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosNotificacionTitularJPanel.add(viaNotificacionTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));
        datosNotificacionTitularJPanel.add(faxTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 130, 20));
        datosNotificacionTitularJPanel.add(telefonoTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 130, 20));
        datosNotificacionTitularJPanel.add(movilTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 130, 20));
        datosNotificacionTitularJPanel.add(emailTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 130, 20));
        datosNotificacionTitularJPanel.add(tipoViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, 20));
        datosNotificacionTitularJPanel.add(nombreViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 161, 130, 20));
        datosNotificacionTitularJPanel.add(numeroViaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 182, 130, 20));
        datosNotificacionTitularJPanel.add(portalTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 245, 130, 20));
        datosNotificacionTitularJPanel.add(plantaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 203, 130, 20));
        datosNotificacionTitularJPanel.add(escaleraTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 266, 130, 20));
        datosNotificacionTitularJPanel.add(letraTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 224, 130, 20));
        datosNotificacionTitularJPanel.add(cPostalTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 130, 20));
        datosNotificacionTitularJPanel.add(municipioTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 311, 130, 20));
        datosNotificacionTitularJPanel.add(provinciaTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 332, 130, 20));
        datosNotificacionTitularJPanel.add(faxTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 51, 300, -1));
        datosNotificacionTitularJPanel.add(telefonoTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 72, 300, -1));
        datosNotificacionTitularJPanel.add(movilTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 93, 300, -1));
        datosNotificacionTitularJPanel.add(emailTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 114, 300, -1));

        datosNotificacionTitularJPanel.add(nombreViaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 161, 300, -1));
        datosNotificacionTitularJPanel.add(numeroViaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 182, 150, -1));
        datosNotificacionTitularJPanel.add(plantaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 203, 150, -1));
        datosNotificacionTitularJPanel.add(portalTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 245, 150, -1));
        datosNotificacionTitularJPanel.add(escaleraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 266, 150, -1));
        datosNotificacionTitularJPanel.add(letraTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 224, 150, -1));
        datosNotificacionTitularJPanel.add(cPostalTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 300, -1));

        datosNotificacionTitularJPanel.add(municipioTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 311, 300, -1));
        datosNotificacionTitularJPanel.add(provinciaTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 332, 300, -1));
        notificarTitularJCheckBox.setSelected(true);
        notificarTitularJCheckBox.setEnabled(false);
        datosNotificacionTitularJPanel.add(notificarTitularJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 353, 30, -1));
        datosNotificacionTitularJPanel.add(notificarTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 353, 120, 20));
        titularJPanel.add(datosNotificacionTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 520, 420));
        representaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosPersonalesRepresentaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Personales"));
        datosPersonalesRepresentaAJPanel.add(DNIRepresenaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));
        DNIRepresentaAJTField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIRepresentaAJTFieldKeyReleased();
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIRepresentaAJTFieldKeyTyped();
            }
        });

        datosPersonalesRepresentaAJPanel.add(DNIRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 280, -1));
        datosPersonalesRepresentaAJPanel.add(nombreRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, 20));
        datosPersonalesRepresentaAJPanel.add(nombreRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 300, -1));
        datosPersonalesRepresentaAJPanel.add(apellido1RepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 81, 130, 20));
        datosPersonalesRepresentaAJPanel.add(apellido2RepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 102, 130, 20));
        datosPersonalesRepresentaAJPanel.add(apellido1RepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 81, 300, -1));
        datosPersonalesRepresentaAJPanel.add(apellido2RepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 102, 300, -1));
        buscarDNIRepresentaAJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buscarDNIRepresentaAJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        buscarDNIRepresentaAJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIRepresentaAJButtonActionPerformed();
            }
        });

        datosPersonalesRepresentaAJPanel.add(buscarDNIRepresentaAJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 20, 20));

        borrarRepresentanteJButton.setIcon(CUtilidadesComponentes.iconoDeleteParcela);
        borrarRepresentanteJButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        borrarRepresentanteJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        borrarRepresentanteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarRepresentanteJButtonActionPerformed();
            }
        });

        datosPersonalesRepresentaAJPanel.add(borrarRepresentanteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 20, 20));
        representaAJPanel.add(datosPersonalesRepresentaAJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 140));
        datosNotificacionRepresentaAJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosNotificacionRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder("Datos Notificaci\u00f3n"));
        datosNotificacionRepresentaAJPanel.add(viaNotificacionRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 20));
        datosNotificacionRepresentaAJPanel.add(faxRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 51, 130, 20));
        datosNotificacionRepresentaAJPanel.add(telefonoRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 130, 20));
        datosNotificacionRepresentaAJPanel.add(movilRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 130, 20));
        datosNotificacionRepresentaAJPanel.add(emailRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 130, 20));
        datosNotificacionRepresentaAJPanel.add(tipoViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, 20));
        datosNotificacionRepresentaAJPanel.add(nombreViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 161, 130, 20));
        datosNotificacionRepresentaAJPanel.add(numeroViaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 182, 130, 20));
        datosNotificacionRepresentaAJPanel.add(portalRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 245, 130, 20));
        datosNotificacionRepresentaAJPanel.add(plantaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 203, 130, 20));
        datosNotificacionRepresentaAJPanel.add(escaleraRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 266, 130, 20));
        datosNotificacionRepresentaAJPanel.add(letraRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 224, 130, 20));
        datosNotificacionRepresentaAJPanel.add(cPostalRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 130, 20));
        datosNotificacionRepresentaAJPanel.add(municipioRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 311, 130, 20));
        datosNotificacionRepresentaAJPanel.add(provinciaRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 332, 130, 20));
        datosNotificacionRepresentaAJPanel.add(faxRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 51, 300, -1));
        datosNotificacionRepresentaAJPanel.add(telefonoRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 72, 300, -1));
        datosNotificacionRepresentaAJPanel.add(movilRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 93, 300, -1));
        datosNotificacionRepresentaAJPanel.add(emailRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 114, 300, -1));
        datosNotificacionRepresentaAJPanel.add(nombreViaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 161, 300, -1));
        datosNotificacionRepresentaAJPanel.add(numeroViaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 182, 150, -1));
        datosNotificacionRepresentaAJPanel.add(plantaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 203, 150, -1));
        datosNotificacionRepresentaAJPanel.add(portalRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 245, 150, -1));
        datosNotificacionRepresentaAJPanel.add(escaleraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 266, 150, -1));
        datosNotificacionRepresentaAJPanel.add(letraRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 224, 150, -1));
        datosNotificacionRepresentaAJPanel.add(cPostalRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 300, -1));

        datosNotificacionRepresentaAJPanel.add(municipioRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 311, 300, -1));

        datosNotificacionRepresentaAJPanel.add(provinciaRepresentaAJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 332, 300, -1));
        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 353, 30, -1));
        datosNotificacionRepresentaAJPanel.add(notificarRepresentaAJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 353, 120, 20));
        representaAJPanel.add(datosNotificacionRepresentaAJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 520, 420));
        templateJPanel.add(obraMayorJTabbedPane, BorderLayout.WEST);//new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 590));
        botoneraJPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        aceptarJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aceptarJButtonMouseClicked();
            }
        });

        aceptarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(aceptarJButton);//, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 90, -1));
        cancelarJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelarJButtonMouseClicked();
            }
        });
        cancelarJButton.setPreferredSize(new Dimension(120,30));
        botoneraJPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 90, -1));

        templateJPanel.add(botoneraJPanel, BorderLayout.SOUTH);//new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 560, 40));

        editorMapaJPanel.setLayout(new java.awt.GridLayout(1, 0));

        editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder("Mapa"));
        templateJPanel.add(editorMapaJPanel, BorderLayout.CENTER);//new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 12, 480, 580));

        templateJScrollPane.setViewportView(templateJPanel);

        getContentPane().add(templateJScrollPane);

    }//GEN-END:initComponents

    private void buscarNumExpedienteJButtonActionPerformed() {//GEN-FIRST:event_buscarNumExpedienteJButtonActionPerformed
        CSearchLicenciasObraJDialog dialog= CUtilidadesComponentes.showSearchLicenciasObraDialog(CCreacionLicencias.desktop);
        if (dialog != null){
            if ((dialog.getNumExpedienteSeleccionado() != null) && (dialog.getNumExpedienteSeleccionado().trim().length() > 0)){
                numExpedienteJTextField.setText(dialog.getNumExpedienteSeleccionado());
            }
        }

    }//GEN-LAST:event_buscarNumExpedienteJButtonActionPerformed


	private void formInternalFrameClosed() {
        borrarFeaturesSinGrabar();
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
		CUtilidadesComponentes.menuLicenciasSetEnabled(true, CCreacionLicencias.desktop);
	}

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
        tipoOcupacionEJCBox = new ComboBoxEstructuras(Estructuras.getListaTipoOcupacion(), null, CMainOcupaciones.currentLocale.toString(), false);
		datosSolicitudJPanel.add(tipoOcupacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 390, 20));

        estadoExpedienteEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadosOcupacion(), null, CMainOcupaciones.currentLocale.toString(), false);
        estadoExpedienteEJCBox.setSelectedPatron(CConstantesOcupaciones.patronAperturaExpediente);
        datosSolicitudJPanel.add(estadoExpedienteEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 150, 20));

		viaNotificacionTitularEJCBox = new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainOcupaciones.currentLocale.toString(), false);
        viaNotificacionTitularEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionTitularEJCBoxActionPerformed();}});

		datosNotificacionTitularJPanel.add(viaNotificacionTitularEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 300, 20));
		tipoViaNotificacionTitularEJCBox = new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainOcupaciones.currentLocale.toString(), false);
		datosNotificacionTitularJPanel.add(tipoViaNotificacionTitularEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 300, 20));

		viaNotificacionRepresentaAEJCBox = new ComboBoxEstructuras(Estructuras.getListaViasNotificacion(), null, CMainOcupaciones.currentLocale.toString(), false);
        viaNotificacionRepresentaAEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viaNotificacionRepresentaAEJCBoxActionPerformed();}});

		datosNotificacionRepresentaAJPanel.add(viaNotificacionRepresentaAEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 300, 20));
		tipoViaNotificacionRepresentaAEJCBox = new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, CMainOcupaciones.currentLocale.toString(), false);
		datosNotificacionRepresentaAJPanel.add(tipoViaNotificacionRepresentaAEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 300, 20));

        tipoViaINEEJCBox= new ComboBoxEstructuras(com.geopista.app.ocupaciones.estructuras.Estructuras.getListaTiposViaINE(), null, CMainOcupaciones.currentLocale.toString(), true);
        tipoViaINEEJCBox.setSelectedIndex(0);
        emplazamientoJPanel.add(tipoViaINEEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 110, -1));
	}

    private void viaNotificacionTitularEJCBoxActionPerformed() {
        String index= viaNotificacionTitularEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesOcupaciones.patronNotificacionEmail)){
            emailTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailTitularJLabel.text")));
            emailTitularObligatorio= true;
        }else{
            emailTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailTitularJLabel.text"));
            emailTitularObligatorio= false;
        }

    }

    private void viaNotificacionRepresentaAEJCBoxActionPerformed() {

        String index= viaNotificacionRepresentaAEJCBox.getSelectedPatron();
        if (index.equalsIgnoreCase(CConstantesOcupaciones.patronNotificacionEmail)){
            emailRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailRepresentaAJLabel.text")));
            emailRepresentanteObligatorio= true;
        }else{
            emailRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailRepresentaAJLabel.text"));
            emailRepresentanteObligatorio= false;
        }

    }


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
                CDatosReferenciaCatastralJDialog dialog= CUtilidadesComponentes.showDatosReferenciaCatastralDialog(CCreacionLicencias.desktop, referencia, true);

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

	private void tableToMapOneJButtonActionPerformed() {//GEN-FIRST:event_tableToMapOneJButtonActionPerformed

		if (referenciasCatastralesJTable.getSelectedRow() == -1) {
			logger.info("El usuario ha de seleccionar primero una fila");
			return;
		}
		String value = (String) referenciasCatastralesJTableModel.getValueAt(referenciasCatastralesJTable.getSelectedRow(), 0);
		refreshFeatureSelection(layerName,0,value);


	}//GEN-LAST:event_tableToMapOneJButtonActionPerformed

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


	private void nombreJButtonActionPerformed() {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CAddressJDialog dialog= CUtilidadesComponentes.showAddressDialog(CCreacionLicencias.desktop);
        if (dialog != null){
            Hashtable h= dialog.getReferencias();
            try{
                Vector vBusqueda=new Vector();
                for (Enumeration e= h.elements(); e.hasMoreElements();){
                      CReferenciaCatastral referenciaCatastral= (CReferenciaCatastral)e.nextElement();
                      vBusqueda.add(referenciaCatastral.getReferenciaCatastral());
                }
                refreshFeatureSelection("numeros_policia",1, vBusqueda);
            } catch (Exception ex) {
                logger.error("Exception: " + ex.toString());
            }
        }
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void buscarDNIRepresentaAJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIRepresentaAJButtonActionPerformed

		logger.info("Inicio.");

		CPersonaJDialog dialog= CUtilidadesComponentes.showPersonaDialog(CCreacionLicencias.desktop);
        if (dialog != null){
            CPersonaJuridicoFisica persona = dialog.getPersona();
            if ((persona != null) && (persona.getDniCif() != null)) {
                DNIRepresentaAJTField.setText(persona.getDniCif());
                nombreRepresentaAJTField.setText(persona.getNombre());
                apellido1RepresentaAJTField.setText(persona.getApellido1());
                apellido2RepresentaAJTField.setText(persona.getApellido2());

                /** Datos de Notificacion */
                faxRepresentaAJTField.setText(persona.getDatosNotificacion().getFax());
                telefonoRepresentaAJTField.setText(persona.getDatosNotificacion().getTelefono());
                movilRepresentaAJTField.setText(persona.getDatosNotificacion().getMovil());
                emailRepresentaAJTField.setText(persona.getDatosNotificacion().getEmail());
                nombreViaRepresentaAJTField.setText(persona.getDatosNotificacion().getNombreVia());
                numeroViaRepresentaAJTField.setText(persona.getDatosNotificacion().getNumeroVia());
                plantaRepresentaAJTField.setText(persona.getDatosNotificacion().getPlanta());
                portalRepresentaAJTField.setText(persona.getDatosNotificacion().getPortal());
                escaleraRepresentaAJTField.setText(persona.getDatosNotificacion().getEscalera());
                letraRepresentaAJTField.setText(persona.getDatosNotificacion().getLetra());
                cPostalRepresentaAJTField.setText(persona.getDatosNotificacion().getCpostal());
                municipioRepresentaAJTField.setText(persona.getDatosNotificacion().getMunicipio());
                provinciaRepresentaAJTField.setText(persona.getDatosNotificacion().getProvincia());
                try{
                    tipoViaNotificacionRepresentaAEJCBox.setSelectedPatron(persona.getDatosNotificacion().getTipoVia());
                }catch (Exception e){
                    DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(persona.getDatosNotificacion().getTipoVia());
                    if (auxNode!=null)
                            tipoViaNotificacionRepresentaAEJCBox.setSelected(auxNode.getIdNode());
                }
                try{
                    viaNotificacionRepresentaAEJCBox.setSelectedPatron(new Integer(persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                }catch (Exception e){
                    DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                    if (auxNode!=null)
                            viaNotificacionRepresentaAEJCBox.setSelected(auxNode.getIdNode());
                }


            }
        }

	}//GEN-LAST:event_buscarDNIRepresentaAJButtonActionPerformed

	private void buscarDNITitularJButtonActionPerformed() {//GEN-FIRST:event_buscarDNITitularJButtonActionPerformed

		logger.info("Inicio.");

		CPersonaJDialog dialog= CUtilidadesComponentes.showPersonaDialog(CCreacionLicencias.desktop);
        CPersonaJuridicoFisica persona= dialog.getPersona();
		if ((persona != null) && (persona.getDniCif() != null)) {
			logger.info("persona.getDniCif(): " + persona.getDniCif());
			DNITitularJTField.setText(persona.getDniCif());
			nombreTitularJTField.setText(persona.getNombre());
			apellido1TitularJTField.setText(persona.getApellido1());
			apellido2TitularJTField.setText(persona.getApellido2());

            /** Datos de Notificacion */
            faxTitularJTField.setText(persona.getDatosNotificacion().getFax());
            telefonoTitularJTField.setText(persona.getDatosNotificacion().getTelefono());
            movilTitularJTField.setText(persona.getDatosNotificacion().getMovil());
            emailTitularJTField.setText(persona.getDatosNotificacion().getEmail());
            nombreViaTitularJTField.setText(persona.getDatosNotificacion().getNombreVia());
            numeroViaTitularJTField.setText(persona.getDatosNotificacion().getNumeroVia());
            plantaTitularJTField.setText(persona.getDatosNotificacion().getPlanta());
            portalTitularJTField.setText(persona.getDatosNotificacion().getPortal());
            escaleraTitularJTField.setText(persona.getDatosNotificacion().getEscalera());
            letraTitularJTField.setText(persona.getDatosNotificacion().getLetra());
            cPostalTitularJTField.setText(persona.getDatosNotificacion().getCpostal());
            municipioTitularJTField.setText(persona.getDatosNotificacion().getMunicipio());
            provinciaTitularJTField.setText(persona.getDatosNotificacion().getProvincia());
            try{
                tipoViaNotificacionTitularEJCBox.setSelectedPatron(persona.getDatosNotificacion().getTipoVia());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(persona.getDatosNotificacion().getTipoVia());
                if (auxNode!=null)
                        tipoViaNotificacionTitularEJCBox.setSelected(auxNode.getIdNode());
            }
            try{
                viaNotificacionTitularEJCBox.setSelectedPatron(new Integer(persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
            }catch (Exception e){
                DomainNode auxNode=Estructuras.getListaViasNotificacion().getDomainNodeByTraduccion(new Integer(persona.getDatosNotificacion().getViaNotificacion().getIdViaNotificacion()).toString());
                if (auxNode!=null)
                        viaNotificacionTitularEJCBox.setSelected(auxNode.getIdNode());
            }



		}

	}//GEN-LAST:event_buscarDNITitularJButtonActionPerformed


	private void fechaSolicitudJButtonActionPerformed() {//GEN-FIRST:event_fechaSolicitudJButtonActionPerformed
		logger.info("Inicio.");
		CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(CCreacionLicencias.desktop);
        if (dialog != null){
            String fecha= dialog.getFechaSelected();
            if ((fecha != null) && (!fecha.trim().equals(""))) {
                fechaSolicitudJTField.setText(fecha);
            }
        }

	}//GEN-LAST:event_fechaSolicitudJButtonActionPerformed

    private void calcularArea() {
        long total=0;
        for (int i = 0; i < referenciasCatastralesJTable.getRowCount(); i++)
        {
            Object objeto=referenciasCatastralesJTableModel.getValueAt(i, 1);
            if (objeto!=null)
               try{total+=((Long)objeto).longValue();}catch(Exception e){}
        }
        areaOcupacionJNTField.setNumber(new Long(total));
    }



	private void DNIRepresentaAJTFieldKeyReleased() {//GEN-FIRST:event_DNIRepresentaAJTFieldKeyReleased
		if (DNIRepresentaAJTField.getDocument() != null) {
			if (DNIRepresentaAJTField.getText().length() <= CConstantesOcupaciones.MaxLengthDNI) {
				_DNI_CIF_RepresentaA = DNIRepresentaAJTField.getText();
			} else if (DNIRepresentaAJTField.getText().length() > CConstantesOcupaciones.MaxLengthDNI) {
				DNIRepresentaAJTField.setText(_DNI_CIF_RepresentaA);
			}
		}
	}//GEN-LAST:event_DNIRepresentaAJTFieldKeyReleased

	private void DNIRepresentaAJTFieldKeyTyped() {//GEN-FIRST:event_DNIRepresentaAJTFieldKeyTyped
		if (DNIRepresentaAJTField.getDocument() != null) {
			if (DNIRepresentaAJTField.getText().length() <= CConstantesOcupaciones.MaxLengthDNI) {
				_DNI_CIF_RepresentaA = DNIRepresentaAJTField.getText();
			} else if (DNIRepresentaAJTField.getText().length() > CConstantesOcupaciones.MaxLengthDNI) {
				DNIRepresentaAJTField.setText(_DNI_CIF_RepresentaA);
			}
		}
	}//GEN-LAST:event_DNIRepresentaAJTFieldKeyTyped

	private void DNITitularJTFieldKeyReleased() {//GEN-FIRST:event_DNITitularJTFieldKeyReleased
		if (DNITitularJTField.getDocument() != null) {
			if (DNITitularJTField.getText().length() <= CConstantesOcupaciones.MaxLengthDNI) {
				_DNI_CIF_Titular = DNITitularJTField.getText();
			} else if (DNITitularJTField.getText().length() > CConstantesOcupaciones.MaxLengthDNI) {
				DNITitularJTField.setText(_DNI_CIF_Titular);
			}
		}
	}//GEN-LAST:event_DNITitularJTFieldKeyReleased

	private void DNITitularJTFieldKeyTyped() {//GEN-FIRST:event_DNITitularJTFieldKeyTyped
		if (DNITitularJTField.getDocument() != null) {
			if (DNITitularJTField.getText().length() <= CConstantesOcupaciones.MaxLengthDNI) {
				_DNI_CIF_Titular = DNITitularJTField.getText();
			} else if (DNITitularJTField.getText().length() > CConstantesOcupaciones.MaxLengthDNI) {
				DNITitularJTField.setText(_DNI_CIF_Titular);
			}
		}
	}//GEN-LAST:event_DNITitularJTFieldKeyTyped


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


    private void borrarRepresentanteJButtonActionPerformed() {//GEN-FIRST:event_borrarRepresentanteJButtonActionPerformed

        if (CUtilidadesComponentes.hayDatosPersonaJuridicoFisica(DNIRepresentaAJTField.getText().trim(),
                nombreRepresentaAJTField.getText().trim(),
                nombreViaRepresentaAJTField.getText().trim(),
                numeroViaRepresentaAJTField.getText().trim(),
                cPostalRepresentaAJTField.getText().trim(),
                municipioRepresentaAJTField.getText().trim(),
                provinciaRepresentaAJTField.getText().trim())){
            /** mostramos ventana de confirmacion */
            int ok = JOptionPane.showConfirmDialog(CCreacionLicencias.desktop, CMainOcupaciones.literales.getString("CCreacionLicenciasForm.representanteJPanel.borrarRepresentanteJButton.Message"), CMainOcupaciones.literales.getString("CCreacionLicenciasForm.representanteJPanel.borrarRepresentanteJButton.tittle"), JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                borrarCamposRepresentante();
            }
        }else{
            borrarCamposRepresentante();
        }
    }//GEN-LAST:event_borrarRepresentanteJButtonActionPerformed


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
	private void cancelarJButtonMouseClicked() {//GEN-FIRST:event_cancelarJButtonMouseClicked
        borrarFeaturesSinGrabar();
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
		try {
			if (rellenaCamposObligatorios()) {
				/** Comprobamos los datos de entrada */

				/** Datos del propietario, representante, tecnico y promotor */
				if (datosTitularOK() && datosRepresentaAOK()) {
					/** Datos de la solicitud */
                    CEstado estado= new CEstado();
                    estado.setIdEstado(new Integer(estadoExpedienteEJCBox.getSelectedPatron()).intValue());

					// TRAZAS-----------------------------------------
					/*
					System.out.println("indexSelected="+_estado + " valorSelected="+estadoJCBox.getSelectedItem());
					for (int t=0; t<_vEstado.size(); t++){
						 CEstado e= (CEstado)_vEstado.get(t);
						 System.out.println("t="+t+ " valor="+e.getDescripcion());
					}
					*/
					//-------------------------------------------------
					//  CTipoObra tipoObra = new CTipoObra((new Integer(tipoObraEJCBox.getSelectedPatron())).intValue(), "", "");
					//  System.out.println("****************************INDEX="+new Integer(tipoObraEJCBox.getSelectedPatron()));


					_motivo = motivoJTField.getText();
					if (excedeLongitud(_motivo, CConstantesOcupaciones.MaxLengthMotivo)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje2"));
						return;
					}
					_asunto = asuntoJTField.getText();
					if (excedeLongitud(_asunto, CConstantesOcupaciones.MaxLengthAsunto)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje3"));
						return;
					}

					//***********************************************
					//** Chequeamos el formato de las horas
					//***********************************************
                    /*
                    Date dHoraInicio = new Date();
                    Date dHoraFin = new Date();
                    */
                    Date dHoraInicio= null;
                    Date dHoraFin= null;

                    String sHoraInicio = horaInicioJTimeTField.getText().trim();
                    try {
                        if (sHoraInicio.length() > 0){
                            dHoraInicio = new SimpleDateFormat("HH:mm").parse(sHoraInicio);
                        }
                    } catch (Exception e) {
                        mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje19"));
                        return;
                    }

                    String sHoraFin = horaFinJTimeTField.getText().trim();
                    try {
                        if (sHoraFin.length() > 0){
                            dHoraFin = new SimpleDateFormat("HH:mm").parse(sHoraFin);
                        }
                    } catch (Exception e) {
                        mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje19"));
                        return;
                    }

					_observaciones = observacionesJTArea.getText();
					if (excedeLongitud(_observaciones, CConstantesOcupaciones.MaxLengthObservaciones)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje5"));
						return;
					}

					Vector vListaAnexos = new Vector();
					int numRows = listaAnexosJTable.getRowCount();

					for (int i = 0; i < numRows; i++) {
						String nomFichero = (String) listaAnexosJTable.getValueAt(i, 0);
						ComboBoxRendererEstructuras renderBox = (ComboBoxRendererEstructuras) listaAnexosJTable.getCellRenderer(i, 1);
						listaAnexosJTable.prepareRenderer(renderBox, i, 1);
						int indice = new Integer(renderBox.getSelectedPatron()).intValue();

						TextFieldRenderer renderText = (TextFieldRenderer) listaAnexosJTable.getCellRenderer(i, 2);
						listaAnexosJTable.prepareRenderer(renderText, i, 2);
						String descripcion = renderText.getText();

						CTipoAnexo tipoAnexo = new CTipoAnexo(indice, "", "");
						CAnexo anexo = new CAnexo(tipoAnexo, nomFichero, descripcion);

						try {
							//***************************************************
							//** Lectura del contenido del anexo
							//*********************************************
							File file = new File(nomFichero);
							logger.info("file: " + file);
							logger.info("file.exists(): " + file.exists());
							logger.info("file.getAbsolutePath(): " + file.getAbsolutePath());
							logger.info("file.getAbsolutePath().length(): " + file.getAbsolutePath().length());

                            anexo.setFileName(file.getName());
                            /* MultipartPostMethod - en lugar de enviar el contenido, enviamos el path absoluto del fichero - */
                            anexo.setPath(file.getAbsolutePath());
                            /**/

                            /** -- MultipartPostMethod: comentamos para no enviar el contenido. Enviamos el file directamente. */
                            /*
							byte[] content = getBytesFromFile(file);
                            anexo.setContent(content);
                            */

							vListaAnexos.addElement(anexo);
						} catch (Exception ex) {
							logger.error("Exception: " + ex.toString());
						}
					}
					logger.info("Anexos añadidos. vListaAnexos: " + vListaAnexos);

					this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

					//***********************************************
					//** Datos del titular
					//*****************************************

					int viaNotificacionIndex = new Integer(viaNotificacionTitularEJCBox.getSelectedPatron()).intValue();
					CViaNotificacion viaNotificacion = new CViaNotificacion(viaNotificacionIndex, "");
					if (_seNotificaRepresentaA == 0)
						_seNotificaTitular = 1;

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
					CPersonaJuridicoFisica representante = new CPersonaJuridicoFisica();

					if (_flagRepresentante) {
						viaNotificacionIndex = new Integer(viaNotificacionRepresentaAEJCBox.getSelectedPatron()).intValue();
						viaNotificacion = new CViaNotificacion(viaNotificacionIndex, "");

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
					} else
						representante = null;


					CExpedienteLicencia expedienteLicencia = new CExpedienteLicencia(estado);

                    //Añadimos los parámetros de la configuracion
                    expedienteLicencia.setServicioEncargado(JDialogConfiguracion.getServicioOcupaciones());
                    expedienteLicencia.setFormaInicio(JDialogConfiguracion.getInicioOcupaciones());
                    expedienteLicencia.setResponsable(JDialogConfiguracion.getResponsableOcupaciones());

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
                        numero= null;
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

					CSolicitudLicencia solicitudLicencia = new CSolicitudLicencia(null,
							null,
							propietario,
							representante,
							null,
							null,
							"numAdm",
							"codigoEntrada",
							null,
							null,
							motivoJTField.getText(),
							asuntoJTField.getText(),
                            CUtilidades.getDate(fechaSolicitudJTField.getText()),
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
							observacionesJTArea.getText(),
							vListaAnexos,
							referenciasCatastrales);
                  	CDatosOcupacion datosOcupacion = new CDatosOcupacion();
					datosOcupacion.setTipoOcupacion(Integer.parseInt(tipoOcupacionEJCBox.getSelectedPatron()));

					datosOcupacion.setNecesitaObra("0");
					if (necesitaObraJCheckBox.isSelected()) {
						datosOcupacion.setNecesitaObra("1");
					}

					datosOcupacion.setNumExpediente(numExpedienteJTextField.getText());
					datosOcupacion.setHoraInicio(dHoraInicio);
					datosOcupacion.setHoraFin(dHoraFin);

					datosOcupacion.setNumMesas(numMesasJNTField.getNumber().intValue());
					datosOcupacion.setNumSillas(numSillasJNTField.getNumber().intValue());

					datosOcupacion.setAfectaAcera("0");
					if (afectaAceraJCheckBox.isSelected()) {
						datosOcupacion.setAfectaAcera("1");
					}

					datosOcupacion.setAfectaCalzada("0");
					if (afectaCalzadaJCheckBox.isSelected()) {
						datosOcupacion.setAfectaCalzada("1");
					}

					datosOcupacion.setAfectaAparcamiento("0");
					if (afectaAparcamientoJCheckBox.isSelected()) {
						datosOcupacion.setAfectaAparcamiento("1");
					}

					try {
						datosOcupacion.setAreaOcupacion(areaOcupacionJNTField.getNumber().doubleValue());
					} catch (Exception ex) {
						logger.warn("There was an error on a field.");
						datosOcupacion.setAreaOcupacion(0);
					}
					try {
						datosOcupacion.setM2acera(m2AceraJNTField.getNumber().doubleValue());
					} catch (Exception ex) {
						logger.warn("There was an error on a field.");
						datosOcupacion.setM2acera(0);
					}

					try {
						datosOcupacion.setM2calzada(m2CalzadaJNTField.getNumber().doubleValue());

					} catch (Exception ex) {
						logger.warn("There was an error on a field.");
						datosOcupacion.setM2calzada(0);
					}
					try {
						datosOcupacion.setM2aparcamiento(m2AparcamientoJNTField.getNumber().doubleValue());
					} catch (Exception ex) {
						logger.warn("There was an error on a field.");
						datosOcupacion.setM2aparcamiento(0);
					}


					solicitudLicencia.setDatosOcupacion(datosOcupacion);

					CResultadoOperacion resultado = COperacionesLicencias.crearExpediente(solicitudLicencia, expedienteLicencia);
                    if (resultado.getResultado())
                    {
                        /** Es necesario pasar el id_solicitud (numeric) y no el num_expediente (varchar) */
                        String idSolicitud= "";
                        Vector vExpediente= resultado.getExpedientes();
                        if ((vExpediente != null) && (vExpediente.size() > 0)){
                            idSolicitud= new Long(((CExpedienteLicencia)vExpediente.get(0)).getIdSolicitud()).toString();
                        }

                        GeopistaLayer geopistaLayer = (GeopistaLayer) CMainOcupaciones.geopistaEditor.getLayerManager().getLayer(layerName);
                        for (Enumeration e=nuevasFeatures.elements(); e.hasMoreElements();)
                        {
                            Feature f = (Feature)e.nextElement();
                            f.setAttribute(1,idSolicitud);
                            ShowMaps.updateFeature(f,geopistaLayer,layerName,com.geopista.protocol.CConstantesComando.adminCartografiaUrl);
                        }
                    }
                    logger.info("resultado.getResultado(): " + resultado.getResultado());
					logger.info("resultado.getDescripcion(): " + resultado.getDescripcion());

					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                    if (resultado.getResultado()){
					    mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje16") +  " " + resultado.getDescripcion());
                        nuevasFeatures=new Hashtable();
                        clearAll();
                    }
                    else{
                        /** Comprobamos que no se haya excedido el maximo FileUploadBase.SizeLimitExceededException */
                       if (resultado.getDescripcion().equalsIgnoreCase("FileUploadBase.SizeLimitExceededException")){
                           JOptionPane optionPane= new JOptionPane(CMainOcupaciones.literales.getString("AnexosJPanel.Message1"), JOptionPane.ERROR_MESSAGE);
                           JDialog dialog =optionPane.createDialog(CCreacionLicencias.desktop,"ERROR");
                           dialog.show();
                       }else{
                         /* ERROR al crear el expediente */
                         JOptionPane optionPane= new JOptionPane(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.Message1")+" \n"+resultado.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                         JDialog dialog =optionPane.createDialog(CCreacionLicencias.desktop,"ERROR");
                         dialog.show();
                       }
                    }
				}
			} else {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.SolicitudTab.mensaje17"));
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}//GEN-LAST:event_aceptarJButtonMouseClicked


	private void listaAnexosJTableFocusGained() {//GEN-FIRST:event_listaAnexosJTableFocusGained
		int selected = listaAnexosJTable.getSelectedRow();
		if (selected != -1) {
			eliminarJButton.setEnabled(true);
            abrirJButton.setEnabled(true);
		}
	}//GEN-LAST:event_listaAnexosJTableFocusGained

	private void eliminarJButtonMouseClicked() {//GEN-FIRST:event_eliminarJButtonMouseClicked
		if (eliminarJButton.isEnabled()) {
			int selected = listaAnexosJTable.getSelectedRow();
			if (selected != -1) {
                int ok= JOptionPane.showConfirmDialog(CCreacionLicencias.desktop, CMainOcupaciones.literales.getString("Licencias.confirmarBorrado"), CMainOcupaciones.literales.getString("Licencias.tittle"), JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.NO_OPTION) return;

				_listaAnexosTableModel.removeRow(selected);

				/** Comprobamos si algun item de la lista queda seleccionado.
				 *  Si es asi, habilitamos el boton Eliminar, si no, le deshabilitamos
				 */
				if (listaAnexosJTable.getModel().getRowCount() > 0) {
					if (listaAnexosJTable.getSelectedRow() != -1) {
						eliminarJButton.setEnabled(true);
                        abrirJButton.setEnabled(true);
					} else {
						eliminarJButton.setEnabled(false);
                        abrirJButton.setEnabled(false);
					}
				} else {
					eliminarJButton.setEnabled(false);
                    abrirJButton.setEnabled(false);
				}
			} else { // no ha seleccionado ningun item
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}//GEN-LAST:event_eliminarJButtonMouseClicked

    public void setMaxSizeFilesUploaded(long size){
        this.maxSizeFilesUploaded= size;
    }

    public long getMaxSizeFilesUploaded(){
        return maxSizeFilesUploaded;
    }


	private void annadirJButtonMouseClicked() {//GEN-FIRST:event_annadirJButtonMouseClicked
		/** Permite annadir varios elementos a la lista */
		//listaAnexosJList.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listaAnexosJTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JFileChooser chooser = new JFileChooser();
		GeoPistaFileFilter filter = new GeoPistaFileFilter();
		filter.addExtension("doc");
		filter.addExtension("txt");
		filter.setDescription("Fichero DOC & TXT");
		chooser.setFileFilter(filter);
		/** Permite multiples selecciones */
		chooser.setMultiSelectionEnabled(true);

		int returnVal = chooser.showOpenDialog(obraMayorJPanel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = chooser.getSelectedFiles();
			System.out.println("Fichero(s) seleccionado " + selectedFiles.length);
			if (selectedFiles.length > 0) {
				for (int i = 0; i < selectedFiles.length; i++) {
					logger.info("\t" + selectedFiles[i].getPath());
					logger.info("Abrimos el fichero: " + selectedFiles[i].getAbsolutePath());
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
                                JDialog dialog =optionPane.createDialog(CCreacionLicencias.desktop,"ERROR");
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
				}
			}
		}
		/** Solo se podra seleccionar un elemento de la lista */
		listaAnexosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}//GEN-LAST:event_annadirJButtonMouseClicked

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

        File f = new File(fileName);
        String tmpFile= f.getAbsolutePath();

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


	private void observacionesJTAreaKeyReleased() {//GEN-FIRST:event_observacionesJTAreaKeyReleased
		if (observacionesJTArea.getDocument() != null) {
			if (observacionesJTArea.getText().length() <= CConstantesOcupaciones.MaxLengthObservaciones) {
				_observaciones = observacionesJTArea.getText();
			} else if (observacionesJTArea.getText().length() > CConstantesOcupaciones.MaxLengthObservaciones) {
				observacionesJTArea.setText(_observaciones);
				System.out.println(_observaciones);
			}
		}
	}//GEN-LAST:event_observacionesJTAreaKeyReleased

	private void observacionesJTAreaKeyTyped() {//GEN-FIRST:event_observacionesJTAreaKeyTyped
		if (observacionesJTArea.getDocument() != null) {
			if (observacionesJTArea.getText().length() <= CConstantesOcupaciones.MaxLengthObservaciones) {
				_observaciones = observacionesJTArea.getText();
			} else if (observacionesJTArea.getText().length() > CConstantesOcupaciones.MaxLengthObservaciones) {
				observacionesJTArea.setText(_observaciones);
			}
		}
	}//GEN-LAST:event_observacionesJTAreaKeyTyped

	/*******************************************************************/
/*                         Metodos propios                         */
	/**
	 * ****************************************************************
	 */
	public void cargarTiposAnexosJTable() {
		if (Estructuras.getListaTiposAnexo().getLista().size() > 0) {
			// Annadimos a la tabla el editor ComboBox en la segunda columna
			int vColIndexCB = 1;
			TableColumn col2 = listaAnexosJTable.getColumnModel().getColumn(vColIndexCB);
			col2.setCellEditor(new ComboBoxTableEditor(new ComboBoxEstructuras(Estructuras.getListaTiposAnexo(), null, CMainOcupaciones.currentLocale.toString(), false)));
			col2.setCellRenderer(new ComboBoxRendererEstructuras(Estructuras.getListaTiposAnexo(), null, CMainOcupaciones.currentLocale.toString(), false));

		} else {
			String[] values = {""};
			// Annadimos a la tabla el editor ComboBox en la segunda columna
			int vColIndexCB = 1;
			TableColumn col2 = listaAnexosJTable.getColumnModel().getColumn(vColIndexCB);
			col2.setCellEditor(new ComboBoxTableEditor(new JComboBox(values)));
			col2.setCellRenderer(new ComboBoxRenderer(values));
		}

		// Annadimos a la tabla el editor TextField en la tercera columna
		int vColIndexTF = 2;
		TableColumn col3 = listaAnexosJTable.getColumnModel().getColumn(vColIndexTF);
		col3.setCellEditor(new TextFieldTableEditor());
		col3.setCellRenderer(new TextFieldRenderer());
	}

	public boolean datosTitularOK() {

		try {
			if (excedeLongitud(_DNI_CIF_Titular, CConstantesOcupaciones.MaxLengthDNI)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje1"));
				return false;
			}
			if (excedeLongitud(_nombreTitular, CConstantesOcupaciones.MaxLengthNombre)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje2"));
				return false;
			}
			if (excedeLongitud(_apellido1Titular, CConstantesOcupaciones.MaxLengthApellido) || excedeLongitud(_apellido2Titular, CConstantesOcupaciones.MaxLengthApellido)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje3"));
				return false;
			}
			_emailTitular = emailTitularJTField.getText();
			if (excedeLongitud(_emailTitular, CConstantesOcupaciones.MaxLengthEmail)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje6"));
				return false;
			}
			_nombreViaTitular = nombreViaTitularJTField.getText();
			if (excedeLongitud(_nombreViaTitular, CConstantesOcupaciones.MaxLengthNombreVia)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje8"));
				return false;
			}
			_portalTitular = portalTitularJTField.getText();
			if (_portalTitular.length() > 0) {
				if (excedeLongitud(_portalTitular, CConstantesOcupaciones.MaxLengthPortal)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje11"));
					return false;
				}
			}
			_plantaTitular = plantaTitularJTField.getText();
			if (_plantaTitular.length() > 0) {
                if (excedeLongitud(_plantaTitular, CConstantesOcupaciones.MaxLengthPlanta)) {
                    mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje13"));
                    return false;
                }
			}
			_escaleraTitular = escaleraTitularJTField.getText();
			if (excedeLongitud(_escaleraTitular, CConstantesOcupaciones.MaxLengthPlanta)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje14"));
				return false;
			}
			_letraTitular = letraTitularJTField.getText();
			if (excedeLongitud(_letraTitular, CConstantesOcupaciones.MaxLengthLetra)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje15"));
				return false;
			}
			_municipioTitular = municipioTitularJTField.getText();
			if (excedeLongitud(_municipioTitular, CConstantesOcupaciones.MaxLengthMunicipio)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje18"));
				return false;
			}

			_provinciaTitular = provinciaTitularJTField.getText();
			if (excedeLongitud(_provinciaTitular, CConstantesOcupaciones.MaxLengthProvincia)) {
				mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.PropietarioTab.mensaje19"));
				return false;
			}


		} catch (Exception ex) {
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

		try {
			/** Comprobamos que el propietario tenga representante */
			if (_flagRepresentante) {
				if (excedeLongitud(_DNI_CIF_RepresentaA, CConstantesOcupaciones.MaxLengthDNI)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje1"));
					return false;
				}
				if (excedeLongitud(_nombreRepresentaA, CConstantesOcupaciones.MaxLengthNombre)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje2"));
					return false;
				}
				if (excedeLongitud(_apellido1RepresentaA, CConstantesOcupaciones.MaxLengthApellido) || excedeLongitud(_apellido2RepresentaA, CConstantesOcupaciones.MaxLengthApellido)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje3"));
					return false;
				}
				_emailRepresentaA = emailRepresentaAJTField.getText();
				if (excedeLongitud(_emailRepresentaA, CConstantesOcupaciones.MaxLengthEmail)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje6"));
					return false;
				}
				_nombreViaRepresentaA = nombreViaRepresentaAJTField.getText();
				if (excedeLongitud(_nombreViaRepresentaA, CConstantesOcupaciones.MaxLengthNombreVia)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje8"));
					return false;
				}
				_portalRepresentaA = portalRepresentaAJTField.getText();
				if (_portalRepresentaA.length() > 0) {
					if (excedeLongitud(_portalRepresentaA, CConstantesOcupaciones.MaxLengthPortal)) {
						mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje11"));
						return false;
					}
				}
				_plantaRepresentaA = plantaRepresentaAJTField.getText();
				if (_plantaRepresentaA.length() > 0) {
                    if (excedeLongitud(_plantaRepresentaA, CConstantesOcupaciones.MaxLengthPlanta)) {
                        mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje13"));
                        return false;
                    }
				}
				_escaleraRepresentaA = escaleraRepresentaAJTField.getText();
				if (excedeLongitud(_escaleraRepresentaA, CConstantesOcupaciones.MaxLengthPlanta)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje14"));
					return false;
				}
				_letraRepresentaA = letraRepresentaAJTField.getText();
				if (excedeLongitud(_letraRepresentaA, CConstantesOcupaciones.MaxLengthLetra)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje15"));
					return false;
				}
				_municipioRepresentaA = municipioRepresentaAJTField.getText();
				if (excedeLongitud(_municipioRepresentaA, CConstantesOcupaciones.MaxLengthMunicipio)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje18"));
					return false;
				}

				_provinciaRepresentaA = provinciaRepresentaAJTField.getText();
				if (excedeLongitud(_provinciaRepresentaA, CConstantesOcupaciones.MaxLengthProvincia)) {
					mostrarMensaje(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.RepresentanteTab.mensaje19"));
					return false;
				}
			} else {
				borrarCamposRepresentante();
			}

			boolean notificar = notificarRepresentaAJCheckBox.isSelected();
			if (notificar)
				_seNotificaRepresentaA = 1;
			else
				_seNotificaRepresentaA = 0;

		} catch (Exception ex) {
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
		if ((_DNI_CIF_Titular.trim().length() == 0) || (_nombreTitular.trim().length() == 0)) return false;

		if ((_nombreViaTitular.trim().length() == 0) ||
				(_numeroViaTitular.trim().length() == 0) || (_cPostalTitular.trim().length() == 0) ||
				(_municipioTitular.trim().length() == 0) || (_provinciaTitular.trim().length() == 0))
			return false;
        /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
        if (emailTitularObligatorio){
            if (emailTitularJTField.getText().trim().length() == 0) return false;
        }


		/** Comprobamos los datos del representado */
		// leemos los datos referentes al representado
		_DNI_CIF_RepresentaA = DNIRepresentaAJTField.getText();
		/** si se inserta el DNI consideramos que el propietario tiene representante */
		if (_DNI_CIF_RepresentaA.trim().length() > 0)
			_flagRepresentante = true;
		else
			_flagRepresentante = false;
		if (_flagRepresentante) {
			_nombreRepresentaA = nombreRepresentaAJTField.getText();
			_nombreViaRepresentaA = nombreViaRepresentaAJTField.getText();
            _numeroViaRepresentaA= numeroViaRepresentaAJTField.getText().trim();
			_cPostalRepresentaA = cPostalRepresentaAJTField.getText();
			_municipioRepresentaA = municipioRepresentaAJTField.getText();
			_provinciaRepresentaA = provinciaRepresentaAJTField.getText();
			if ((_DNI_CIF_RepresentaA.trim().length() == 0) || (_nombreRepresentaA.trim().length() == 0)) return false;

			if ((_nombreViaRepresentaA.trim().length() == 0) || (_numeroViaRepresentaA.trim().length() == 0) ||
					(_cPostalRepresentaA.trim().length() == 0) || (_municipioRepresentaA.trim().length() == 0) ||
					(_provinciaRepresentaA.trim().length() == 0))
				return false;
            /** si ha seleccionado el tipo de notificacion por email, el campo email es obligatorio */
            if (emailRepresentanteObligatorio){
                if (emailRepresentaAJTField.getText().trim().length() == 0) return false;
            }

		}

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


	}


	public void buscarRellenarDatosPersonaConDNI(String dni) {
		// Falta por rellenar

	}

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(CCreacionLicencias.desktop, mensaje);
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
			Date date = CConstantesOcupaciones.df.parse(fecha);
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


	public void renombrarComponentes() {

		try {
			/** Solicitud */
			setTitle(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.JInternalFrame.title"));
            try
            {
                ClassLoader cl =this.getClass().getClassLoader();
                Icon icon= new javax.swing.ImageIcon(cl.getResource("img/solicitud.jpg"));
                obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.obraMayorJPanel.TitleTab")), icon,obraMayorJPanel);
            }catch(Exception e)
            {
                obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.obraMayorJPanel.TitleTab")), obraMayorJPanel);
            }
			datosSolicitudJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosSolicitudJPanel.TitleBorder")));
			estadoJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.estadoJLabel.text")));
			tipoObraJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tipoObraJLabel.text")));
			unidadTJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.unidadTJLabel.text"));
			unidadRJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.unidadRJLabel.text"));
			motivoJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.motivoJLabel.text"));
			asuntoJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.asuntoJLabel.text"));
			fechaSolicitudJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.fechaSolicitudJLabel.text")));
			observacionesJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.observacionesJLabel.text"));
			tasaJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tasaJLabel.text"));

            areaOcupacionJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.areaOcupacionJLabel.text"));
            numMesasJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.numMesasJLabel.text"));
            numSillasJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.numSillasJLabel.text"));
            afectaAceraJCheckBox.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.afectaAceraJCheckBox.text"));
            afectaAparcamientoJCheckBox.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.afectaAparcamientoJCheckBox.text"));
            afectaCalzadaJCheckBox.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.afectaCalzadaJCheckBox.text"));
            areaOcupacionJLabel2.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.areaOcupacionJLabel2.text"));
            afectaJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.afectaJLabel.text"));

			emplazamientoJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emplazamientoJPanel.TitleBorder")));
			nombreViaJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tipoViaJLabel.text"), CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreViaJLabel.text")));
			numeroViaJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.numeroViaJLabel.text"));
			cPostalJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cPostalJLabel.text"));
			provinciaJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.provinciaJLabel.text"));
            municipioJTField.setText(CConstantesOcupaciones.Municipio);
            provinciaJTField.setText(CConstantesOcupaciones.Provincia);


			anexosJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.anexosJPanel.TitleBorder")));
			//annadirJButton.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.annadirJButton.text"));
			//eliminarJButton.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.eliminarJButton.text"));

			/** Propietario */
			datosPersonalesTitularJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosPersonalesTitularJPanel.TitleBorder")));
			DNITitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.DNITitularJLabel.text")));
			nombreTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreTitularJLabel.text")));
			apellido1TitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido1TitularJLabel.text"));
			apellido2TitularJLabel2.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido2TitularJLabel.text"));
			datosNotificacionTitularJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosNotificacionTitularJPanel.TitleBorder")));
			viaNotificacionTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.viaNotificacionTitularJLabel.text"));
			faxTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.faxTitularJLabel.text"));
			telefonoTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.telefonoTitularJLabel.text"));
			movilTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.movilTitularJLabel.text"));
			emailTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailTitularJLabel.text"));
			tipoViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tipoViaTitularJLabel.text")));
			nombreViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreViaTitularJLabel.text")));
			numeroViaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.numeroViaTiularJLabel.text")));
			portalTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.portalTitularJLabel.text"));
			plantaTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.plantaTitularJLabel.text"));
			escaleraTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.escaleraTitularJLabel.text"));
			letraTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.letraTitularJLabel.text"));
			cPostalTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cPostalTitularJLabel.text")));
			municipioTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.municipioTitularJLabel.text")));
			provinciaTitularJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.provinciaTitularJLabel.text")));
			notificarTitularJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.notificarTitularJLabel.text"));

            /** Headers de la tabla eventos */
            TableColumn tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column1"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column10"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column2"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column3"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(4);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column4"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(5);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column5"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(6);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column6"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(7);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column7"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(8);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column8"));
            tableColumn= referenciasCatastralesJTable.getColumnModel().getColumn(9);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.referenciasCatastralesJTable.column9"));

            tableColumn= listaAnexosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.listaAnexosJTable.column1"));
            tableColumn= listaAnexosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.listaAnexosJTable.column2"));
            tableColumn= listaAnexosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.listaAnexosJTable.column3"));

            try
            {
                ClassLoader cl =this.getClass().getClassLoader();
                Icon icon= new javax.swing.ImageIcon(cl.getResource("img/persona.jpg"));
                obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.titularJPanel.TitleTab")), icon,titularJPanel);

            }catch(Exception e)
            {
                obraMayorJTabbedPane.addTab(CUtilidadesComponentes.annadirAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.titularJPanel.TitleTab")), titularJPanel);
            }

			/** Representante */
			datosPersonalesRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosPersonalesRepresentaAJPanel.TitleBorder")));
			DNIRepresenaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.DNIRepresentaAJLabel.text")));
			nombreRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreRepresentaAJLabel.text")));
			apellido1RepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido1RepresentaAJLabel.text"));
			apellido2RepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.apellido2RepresentaAJLabel.text"));
			datosNotificacionRepresentaAJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.datosNotificacionRepresentaAJPanel.TitleBorder")));
			viaNotificacionRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.viaNotificacionRepresentaAJLabel.text"));
			faxRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.faxRepresentaAJLabel.text"));
			telefonoRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.telefonoRepresentaAJLabel.text"));
			movilRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.movilRepresentaAJLabel.text"));
			emailRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.emailRepresentaAJLabel.text"));
			tipoViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.tipoViaRepresentaAJLabel.text")));
			nombreViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.nombreViaRepresentaAJLabel.text")));
			numeroViaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.numeroViaTiularJLabel.text")));
			portalRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.portalRepresentaAJLabel.text"));
			plantaRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.plantaRepresentaAJLabel.text"));
			escaleraRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.escaleraRepresentaAJLabel.text"));
			letraRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.letraRepresentaAJLabel.text"));
			cPostalRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cPostalRepresentaAJLabel.text")));
			municipioRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.municipioRepresentaAJLabel.text")));
			provinciaRepresentaAJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.provinciaRepresentaAJLabel.text")));
			notificarRepresentaAJLabel.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.notificarRepresentaAJLabel.text"));
            try
             {
                 ClassLoader cl =this.getClass().getClassLoader();
                 Icon icon= new javax.swing.ImageIcon(cl.getResource("img/representante.jpg"));
                 obraMayorJTabbedPane.addTab(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.representaAJPanel.TitleTab"), icon,representaAJPanel);
             }catch(Exception e)
             {
                obraMayorJTabbedPane.addTab(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.representaAJPanel.TitleTab"), representaAJPanel);
             }

            buscarDNIRepresentaAJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.buscarDNIRepresentaAJButton.text"));
            buscarDNITitularJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.buscarDNITitularJButton.text"));
            buscarNumExpedienteJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.buscarNumExpedienteJButton.text"));
            fechaSolicitudJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.fechaSolicitudJButton.text"));
            nombreJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.nombreJButton.text"));
            borrarRepresentanteJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.borrarRepresentanteJButton.text"));
            areaOcupacionJButton.setToolTipText(CMainOcupaciones.literales.getString("toolTipText.areaOcupacionJButton.text"));
            eliminarJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.eliminarJButton.toolTipText.text"));
            annadirJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.annadirJButton.toolTipText.text"));
            abrirJButton.setToolTipText(CMainOcupaciones.literales.getString("AnexosJPanel.abrirJButton.toolTipText.text"));
            tableToMapAllJButton.setToolTipText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.tableToMapAllJButton.toolTip.text"));
            tableToMapOneJButton.setToolTipText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.tableToMapOneJButton.toolTip.text"));
            deleteGeopistaFeatureJButton.setToolTipText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.deleteGeopistaFeatureJButton.toolTip.text"));
            aceptarJButton.setToolTipText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.aceptarJButton.text"));
            cancelarJButton.setToolTipText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cancelarJButton.text"));

			aceptarJButton.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.aceptarJButton.text"));
			cancelarJButton.setText(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.cancelarJButton.text"));
			editorMapaJPanel.setBorder(new javax.swing.border.TitledBorder(CMainOcupaciones.literales.getString("CCreacionLicenciasForm.ObraMayorJMenuItem.editorMapaJPanel.TitleBorder")));

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}



    public void clearAll() {

        try{
            /** Solicitud */
            tipoOcupacionEJCBox.setSelectedIndex(0);
            necesitaObraJCheckBox.setSelected(false);
            numExpedienteJTextField.setText("");
            motivoJTField.setText("");
            asuntoJTField.setText("");
            fechaSolicitudJTField.setText(showToday());
            numSillasJNTField.setNumber(new Integer(0));
            numMesasJNTField.setNumber(new Integer(0));
            areaOcupacionJNTField.setNumber(new Double(0));
            m2AceraJNTField.setNumber(new Double(0));
            m2CalzadaJNTField.setNumber(new Double(0));
            m2AparcamientoJNTField.setNumber(new Double(0));
            horaInicioJTimeTField.setText("00:00");
            horaFinJTimeTField.setText("00:00");
            afectaAparcamientoJCheckBox.setSelected(false);
            afectaAceraJCheckBox.setSelected(false);
            afectaCalzadaJCheckBox.setSelected(false);
            observacionesJTArea.setText("");
            CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
            CUtilidadesComponentes.clearTable(_listaAnexosTableModel);
            CMainOcupaciones.geopistaEditor.getSelectionManager().clear();

            /** Emplazamiento */
            tipoViaINEEJCBox.setSelectedIndex(0);
            nombreViaTField.setText("");
            numeroViaNumberTField.setText("");
            portalViaTField.setText("");
            plantaViaTField.setText("");
            letraViaTField.setText("");
            cpostalViaTField.setText("");
            municipioJTField.setText(CConstantesOcupaciones.Municipio);
            provinciaJTField.setText(CConstantesOcupaciones.Provincia);

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



        }catch(Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }

	private DefaultTableModel referenciasCatastralesJTableModel;
	private static JFrame desktop;
	private ComboBoxEstructuras tipoOcupacionEJCBox;
    private ComboBoxEstructuras estadoExpedienteEJCBox;
	private ComboBoxEstructuras viaNotificacionTitularEJCBox;
	private ComboBoxEstructuras viaNotificacionRepresentaAEJCBox;

	private ComboBoxEstructuras tipoViaNotificacionTitularEJCBox;
	private ComboBoxEstructuras tipoViaNotificacionRepresentaAEJCBox;

	private JNumberTextField numSillasJNTField;
	private JNumberTextField numMesasJNTField;
	private JNumberTextField areaOcupacionJNTField;
	private JNumberTextField m2AceraJNTField;
	private JNumberTextField m2CalzadaJNTField;
	private JNumberTextField m2AparcamientoJNTField;

	private JTimeTextField horaInicioJTimeTField;
	private JTimeTextField horaFinJTimeTField;

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
    private javax.swing.JLabel DNIRepresenaAJLabel;
    private javax.swing.JTextField DNIRepresentaAJTField;
    private javax.swing.JLabel DNITitularJLabel;
    private javax.swing.JTextField DNITitularJTField;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JCheckBox afectaAceraJCheckBox;
    private javax.swing.JCheckBox afectaAparcamientoJCheckBox;
    private javax.swing.JCheckBox afectaCalzadaJCheckBox;
    private javax.swing.JPanel anexosJPanel;
    private javax.swing.JButton annadirJButton;
    private javax.swing.JLabel apellido1RepresentaAJLabel;
    private javax.swing.JTextField apellido1RepresentaAJTField;
    private javax.swing.JLabel apellido1TitularJLabel;
    private javax.swing.JTextField apellido1TitularJTField;
    private javax.swing.JLabel apellido2RepresentaAJLabel;
    private javax.swing.JTextField apellido2RepresentaAJTField;
    private javax.swing.JLabel apellido2TitularJLabel2;
    private javax.swing.JTextField apellido2TitularJTField;
    private javax.swing.JLabel areaOcupacionJLabel;
    private javax.swing.JLabel areaOcupacionJLabel2;
    private javax.swing.JLabel asuntoJLabel;
    private javax.swing.JTextField asuntoJTField;
    private javax.swing.JPanel botoneraJPanel;
    private javax.swing.JButton buscarDNIRepresentaAJButton;
    private javax.swing.JButton buscarDNITitularJButton;
    private javax.swing.JButton buscarNumExpedienteJButton;
    private javax.swing.JLabel cPostalJLabel;
    private javax.swing.JLabel cPostalRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalRepresentaAJTField;
    private javax.swing.JLabel cPostalTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField cPostalTitularJTField;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JPanel datosNotificacionRepresentaAJPanel;
    private javax.swing.JPanel datosNotificacionTitularJPanel;
    private javax.swing.JPanel datosPersonalesRepresentaAJPanel;
    private javax.swing.JPanel datosPersonalesTitularJPanel;
    private javax.swing.JPanel datosSolicitudJPanel;
    private javax.swing.JPanel editorMapaJPanel;
    private javax.swing.JButton eliminarJButton;
    private javax.swing.JLabel emailRepresentaAJLabel;
    private javax.swing.JTextField emailRepresentaAJTField;
    private javax.swing.JLabel emailTitularJLabel;
    private javax.swing.JTextField emailTitularJTField;
    private javax.swing.JPanel emplazamientoJPanel;
    private javax.swing.JLabel escaleraRepresentaAJLabel;
    private javax.swing.JTextField escaleraRepresentaAJTField;
    private javax.swing.JLabel escaleraTitularJLabel;
    private javax.swing.JTextField escaleraTitularJTField;
    private javax.swing.JLabel estadoJLabel;
    private javax.swing.JLabel faxRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxRepresentaAJTField;
    private javax.swing.JLabel faxTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField faxTitularJTField;
    private javax.swing.JButton fechaSolicitudJButton;
    private javax.swing.JLabel fechaSolicitudJLabel;
    private javax.swing.JTextField fechaSolicitudJTField;
    private javax.swing.JLabel letraRepresentaAJLabel;
    private javax.swing.JTextField letraRepresentaAJTField;
    private javax.swing.JLabel letraTitularJLabel;
    private javax.swing.JTextField letraTitularJTField;
    private javax.swing.JScrollPane listaAnexosJScrollPane;
    private javax.swing.JTable listaAnexosJTable;
    private javax.swing.JLabel metros2JLabel;
    private javax.swing.JLabel motivoJLabel;
    private javax.swing.JTextField motivoJTField;
    private javax.swing.JLabel movilRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilRepresentaAJTField;
    private javax.swing.JLabel movilTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField movilTitularJTField;
    private javax.swing.JTextField municipioJTField;
    private javax.swing.JLabel municipioRepresentaAJLabel;
    private javax.swing.JTextField municipioRepresentaAJTField;
    private javax.swing.JLabel municipioTitularJLabel;
    private javax.swing.JTextField municipioTitularJTField;
    private javax.swing.JCheckBox necesitaObraJCheckBox;
    private javax.swing.JButton nombreJButton;
    private javax.swing.JLabel nombreRepresentaAJLabel;
    private javax.swing.JTextField nombreRepresentaAJTField;
    private javax.swing.JLabel nombreTitularJLabel;
    private javax.swing.JTextField nombreTitularJTField;
    private javax.swing.JLabel nombreViaJLabel;
    private javax.swing.JLabel nombreViaRepresentaAJLabel;
    private javax.swing.JTextField nombreViaRepresentaAJTField;
    private javax.swing.JLabel nombreViaTitularJLabel;
    private javax.swing.JTextField nombreViaTitularJTField;
    private javax.swing.JCheckBox notificarRepresentaAJCheckBox;
    private javax.swing.JLabel notificarRepresentaAJLabel;
    private javax.swing.JCheckBox notificarTitularJCheckBox;
    private javax.swing.JLabel notificarTitularJLabel;
    private javax.swing.JTextField numExpedienteJTextField;
    private javax.swing.JLabel numMesasJLabel;
    private javax.swing.JLabel numSillasJLabel;
    private javax.swing.JLabel numeroViaJLabel;
    private javax.swing.JLabel numeroViaRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaRepresentaAJTField;
    private javax.swing.JLabel numeroViaTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaTitularJTField;
    private javax.swing.JPanel obraMayorJPanel;
    private javax.swing.JTabbedPane obraMayorJTabbedPane;
    private javax.swing.JLabel observacionesJLabel;
    private javax.swing.JScrollPane observacionesJScrollPane;
    private javax.swing.JTextArea observacionesJTArea;
    private javax.swing.JLabel plantaRepresentaAJLabel;
    private javax.swing.JTextField plantaRepresentaAJTField;
    private javax.swing.JLabel plantaTitularJLabel;
    private javax.swing.JTextField plantaTitularJTField;
    private javax.swing.JLabel portalRepresentaAJLabel;
    private javax.swing.JTextField portalRepresentaAJTField;
    private javax.swing.JLabel portalTitularJLabel;
    private javax.swing.JTextField portalTitularJTField;
    private javax.swing.JLabel provinciaJLabel;
    private javax.swing.JTextField provinciaJTField;
    private javax.swing.JLabel provinciaRepresentaAJLabel;
    private javax.swing.JTextField provinciaRepresentaAJTField;
    private javax.swing.JLabel provinciaTitularJLabel;
    private javax.swing.JTextField provinciaTitularJTField;
    private javax.swing.JScrollPane referenciasCatastralesJScrollPane;
    private javax.swing.JTable referenciasCatastralesJTable;
    private javax.swing.JPanel representaAJPanel;
    private javax.swing.JButton tableToMapAllJButton;
    private javax.swing.JButton tableToMapOneJButton;
    private javax.swing.JLabel tasaJLabel;
    private javax.swing.JLabel telefonoRepresentaAJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoRepresentaAJTField;
    private javax.swing.JLabel telefonoTitularJLabel;
    private com.geopista.app.utilidades.JNumberTextField telefonoTitularJTField;
    private javax.swing.JPanel templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JLabel tipoObraJLabel;
    private javax.swing.JLabel tipoViaRepresentaAJLabel;
    private javax.swing.JLabel tipoViaTitularJLabel;
    private javax.swing.JPanel titularJPanel;
    private javax.swing.JLabel unidadRJLabel;
    private javax.swing.JLabel unidadTJLabel;
    private javax.swing.JLabel unidadTJLabel1;
    private javax.swing.JLabel viaNotificacionRepresentaAJLabel;
    private javax.swing.JLabel viaNotificacionTitularJLabel;
    private javax.swing.JButton deleteGeopistaFeatureJButton;
    private javax.swing.JButton borrarRepresentanteJButton;
    private javax.swing.JButton areaOcupacionJButton;
    private javax.swing.JLabel afectaJLabel;
    private javax.swing.JButton abrirJButton;

    // End of variables declaration//GEN-END:variables

}
