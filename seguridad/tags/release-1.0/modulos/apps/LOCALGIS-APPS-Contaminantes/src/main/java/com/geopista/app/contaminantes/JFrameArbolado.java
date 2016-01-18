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

package com.geopista.app.contaminantes;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.netbeans.lib.awtextra.AbsoluteLayout;

import com.geopista.app.contaminantes.panel.CAddressJDialog;
import com.geopista.app.printer.FichasDisponibles;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.Arbolado;
import com.geopista.protocol.contaminantes.Historico;
import com.geopista.protocol.contaminantes.OperacionesContaminantes;
import com.geopista.server.administradorCartografia.CancelException;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * @author angeles
 */
public class JFrameArbolado extends JInternalFrameShowMap implements LayerListener {

	private ResourceBundle messages;
	private JFrame frame;
	ArboladoTableModel modelArbolado;
	Vector listaArbolados = null;
	private TableSorted sorter;
	private String arboladoID;



    private static final String layerName = "Arboleda";

	/**
	 * Creates new form JFrameArbolado
	 */
	public JFrameArbolado(final ResourceBundle messages, final JFrame frame) {
		this.frame = frame;
		this.messages = messages;
        /** dialogo de espera de carga */
        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(frame, null);
        progressDialog.setTitle(messages.getString("Licencias.Tag1"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){
                        /* añadimos el documento a la lista */
                        try{
                            progressDialog.report(messages.getString("Licencias.Tag2"));
                    		initListas();
                            progressDialog.report(messages.getString("Licencias.Tag1"));
                            initComponents();
                        }catch(Exception e){
                            logger.error("Error ", e);
                            ErrorDialog.show(frame, "ERROR", "ERROR", StringUtil.stackTrace(e));
                            return;
                        }finally{
                            progressDialog.setVisible(false);
                        }
                    }
              }).start();
          }
       });
       GUIUtil.centreOnWindow(progressDialog);
       progressDialog.setVisible(true);

       valoresBusqueda = new Hashtable();
  	   enabled(false);
       changeScreenLang(messages);

    }
    public static String getLayername() {
        return layerName;
    }
	private void seleccionarFila(String id) {
		for (int i = 0; i < jTableListado.getRowCount(); i++) {
			String listId = (String) jTableListado.getValueAt(i, 0);
			logger.debug("listId: -" + listId + "-");
			if (listId.equalsIgnoreCase(id)) {
				jTableListado.changeSelection(i, i + 1, false, false);
			}
		}
	}


	private void editar() {
        repaint();
		int row = jTableListado.getSelectedRow();
		if (row == -1) {
			logger.warn("El usuario ha de seleccionar una fila, antes de su edicion.");
			return;
		}
		enabled(true);
		seleccionar();
	}

	private void salvar() {

		try {
			Object[] options = {"Si", "No"};
			int n = JOptionPane.showOptionDialog(this,
					messages.getString("JFrameArbolado.mensage.grabar")
					,
					"Importante", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, options, options[1]);
			if (n == JOptionPane.NO_OPTION) return;
			enabled(false);

			Arbolado arbolado = new Arbolado();
			arbolado.setId(arboladoID);
			arbolado.setObs(jTextAreaObs.getText());
			arbolado.setExt(jTextFieldExtension.getNumber().floatValue());
            arbolado.setPlantadoPor(jTextFieldPlantador.getText());
            if (jComboBoxTipo.getSelectedIndex()!=-1)
                arbolado.setIdTipo(jComboBoxTipo.getSelectedPatron());
            if (jTextFieldFechaPlantacion.getText().length()>0)
                arbolado.setFechaPlanta(jButtonFechaPlantacion.getCalendar().getTime());
            if (jTextFieldFechaTala.getText().length()>0)
                            arbolado.setFechaUltimaTala(jButtonFechaTala.getCalendar().getTime());

			CResultadoOperacion resultadoOperacion = new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url).saveArbolado(arbolado);
			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return;
			}
            new OperacionesContaminantes((com.geopista.app.contaminantes.Constantes.url)).gestionarHistorico(new Historico(new Integer(arbolado.getId()).intValue(), com.geopista.app.contaminantes.Constantes.MODIFICAR, com.geopista.app.contaminantes.Constantes.ARBOLEDAS));
			listaArbolados = actualizarListado(arbolado, false);
			actualizarModelo();
			Collection cgeo = geopistaEditor.getSelection();
			for (Iterator i = cgeo.iterator(); i.hasNext();) {
				GeopistaFeature gf = (GeopistaFeature) i.next();
				try {
					if (gf.getSystemId().equals(arboladoID)) {
						gf.setAttribute(1, jTextFieldExtension.getNumber());
						gf.setAttribute(2, jTextAreaObs.getText());
					}
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					logger.error("Exception: " + sw.toString());
				}
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


	private void cancelar() {
		int row = jTableListado.getSelectedRow();
		if (row != -1) {
			jTextAreaObs.setText((String) jTableListado.getValueAt(row, 1));
			jTextFieldExtension.setNumber(new Float((String) jTableListado.getValueAt(row, 2)));
		}
		enabled(false);
	}


	private void delete(boolean bMostrarMensaje) {

		try {
			if (arboladoID == null) return;
			seleccionar();
            if (bMostrarMensaje)
            {
			    Object[] options = {"Si", "No"};
			    int n = JOptionPane.showOptionDialog(this,messages.getString("JFrameArbolado.mensage.borrar"),
					"Importante",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
					null, options,options[1]); //default button title
        		if (n != 0) {return;}
            }
			enabled(false);
            Arbolado arbolado = new Arbolado();
			arbolado.setId(arboladoID);
			arbolado.setObs(jTextAreaObs.getText());
			arbolado.setExt(jTextFieldExtension.getNumber().floatValue());
			CResultadoOperacion resultadoOperacion = new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url).deleteArbolado(arbolado);
			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			}
			listaArbolados = actualizarListado(arbolado, true);

			jTextAreaObs.setText("");
			jTextFieldExtension.setText("");
            jTextFieldFechaTala.setText("");
            jTextFieldFechaPlantacion.setText("");
            jTextFieldPlantador.setText("");

			actualizarModelo();
			arboladoID = null;
			ShowMaps.deleteFeature(geopistaEditor);
            new OperacionesContaminantes((com.geopista.app.contaminantes.Constantes.url)).gestionarHistorico(new Historico(new Integer(arbolado.getId()).intValue(), com.geopista.app.contaminantes.Constantes.BORRAR, com.geopista.app.contaminantes.Constantes.ARBOLEDAS));
		} catch (Exception ex) {
			logger.error("Exception: " + ex.toString());
		}
		actualizarModelo();
	}

	private void enabled(boolean bValor) {
        boolean permitido=true;
        if (CMainContaminantes.acl!=null)
              permitido=CMainContaminantes.acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_CONTAMINANTES));
	    jTextAreaObs.setEditable(bValor);
		jTextFieldExtension.setEditable(bValor);
		jButtonSave.setEnabled(bValor && permitido);
		jButtonCancel.setEnabled(bValor);
		jButtonDelete.setEnabled(!bValor && permitido);
		jButtonEdit.setEnabled(!bValor && permitido);
        jButtonGenerarFicha.setEnabled(!bValor);
        jTextFieldFechaPlantacion.setEditable(false);
        jTextFieldFechaTala.setEditable(false);
        jTextFieldPlantador.setEditable(bValor);
        jComboBoxTipo.setEnabled(bValor);
        jButtonFechaPlantacion.setEnabled(bValor);
        jButtonFechaTala.setEnabled(bValor);
	}


	private void jButtonBuscar() {
		valoresBusqueda.clear();
		CAddressJDialog dialog = new CAddressJDialog(this.frame, true, messages);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();
        valoresBusqueda=dialog.getValoresBusqueda();
		refreshBusquedaSelection(this, geopistaEditor);

	}


	private void seleccionar() {
		try {
			int row = jTableListado.getSelectedRow();
			if (row != -1) {
               	arboladoID = (String) jTableListado.getValueAt(row, 0);
                Arbolado auxArbolado = getArbolado(arboladoID);
                jTextAreaObs.setText(auxArbolado.getObs());
				jTextFieldExtension.setNumber(new Float(auxArbolado.getExt()));
                jTextFieldPlantador.setText(auxArbolado.getPlantadoPor());
                if (auxArbolado.getFechaPlanta()!=null)
                {
                    Calendar auxCalendar = Calendar.getInstance();
                    auxCalendar.setTime(auxArbolado.getFechaPlanta());
                    jButtonFechaPlantacion.setFecha(auxCalendar);
                }
                else
                    jTextFieldFechaPlantacion.setText("");
                if (auxArbolado.getFechaUltimaTala()!=null)
                {
                   Calendar auxCalendar = Calendar.getInstance();
                   auxCalendar.setTime(auxArbolado.getFechaUltimaTala());
                   jButtonFechaTala.setFecha(auxCalendar);
                }
                else
                    jTextFieldFechaTala.setText("");
                if (auxArbolado.getIdTipo()!=null)
                   jComboBoxTipo.setSelectedPatron(auxArbolado.getIdTipo());
                else
                   jComboBoxTipo.setSelectedIndex(0);
				refreshFeatureSelection(layerName, arboladoID);
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() throws CancelException{//GEN-BEGIN:initComponents
        while (!com.geopista.app.contaminantes.Estructuras.isCargada()) {
            if (!com.geopista.app.contaminantes.Estructuras.isIniciada()) com.geopista.app.contaminantes.Estructuras.cargarEstructuras();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
		jPanelPrincipal = new javax.swing.JPanel();
		jPanelTotal = new javax.swing.JPanel();
		jPanelBotonera = new javax.swing.JPanel();

		jPanelBotonera.setLayout(new AbsoluteLayout());


		jButtonBuscar = new javax.swing.JButton();
		jButtonEdit = new javax.swing.JButton();
        jButtonGenerarFicha=new javax.swing.JButton();
		jButtonDelete = new javax.swing.JButton();
		jButtonSave = new javax.swing.JButton();
		jButtonCancel = new javax.swing.JButton();
		jButtonSalir = new javax.swing.JButton();


		jPanelBotonera.add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 100, 20));
		jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonBuscar();
			}
		});


		jPanelBotonera.add(jButtonEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 5, 100, 20));
		jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editar();
			}
		});
		jPanelBotonera.add(jButtonDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 5, 100, 20));
		jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				delete(true);
			}
		});

		jPanelBotonera.add(jButtonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 5, 100, 20));
		jButtonSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				salvar();
			}
		});
		jPanelBotonera.add(jButtonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 5, 100, 20));
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelar();
			}
		});



		jTabbedPaneDatos = new javax.swing.JTabbedPane();
		jPanelDatos = new javax.swing.JPanel();
		jLabelObs = new javax.swing.JLabel();
        jLabelTipo = new javax.swing.JLabel();
        jComboBoxTipo = new ComboBoxEstructuras(com.geopista.app.contaminantes.Estructuras.getListaTipoZonaArbolada(),
                          null,com.geopista.app.contaminantes.Constantes.Locale);
        jLabelPlantador = new javax.swing.JLabel();
        jTextFieldPlantador = new javax.swing.JTextField();
        jLabelFechaPlantacion = new javax.swing.JLabel();
        jTextFieldFechaPlantacion = new JFormattedTextField((new SimpleDateFormat("dd-MM-yyyy")));
        jButtonFechaPlantacion = new CalendarButton(jTextFieldFechaPlantacion);
        jLabelFechaTala = new javax.swing.JLabel();
        jTextFieldFechaTala = new JFormattedTextField((new SimpleDateFormat("dd-MM-yyyy")));
        jButtonFechaTala = new CalendarButton(jTextFieldFechaTala);
		jScrollPaneObs = new javax.swing.JScrollPane();
		jTextAreaObs = new TextPane(1000);
		jLabelExtension = new javax.swing.JLabel();
		jTextFieldExtension = new JNumberTextField(JNumberTextField.REAL);
		jPanelListado = new javax.swing.JPanel();
		jScrollPaneListado = new javax.swing.JScrollPane();
		jTableListado = new javax.swing.JTable();
		jTableListado.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				enabled(false);
				seleccionar();
			}
		});


		jPanelMapa = new javax.swing.JPanel();
		jPanelContenedorMapa = new javax.swing.JPanel();


		jPanelPrincipal.setLayout(new java.awt.BorderLayout());


        jButtonGenerarFicha.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            generarFicha();
          }
        });
        jPanelTotal.add(jButtonGenerarFicha);


		jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				salir();
			}
		});


		jPanelTotal.add(jButtonSalir);
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salir();
            }
        });

		jPanelPrincipal.add(jPanelBotonera, java.awt.BorderLayout.SOUTH);
		jTabbedPaneDatos.setBorder(new javax.swing.border.EtchedBorder());
		jPanelDatos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanelDatos.add(jLabelObs, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));
        jScrollPaneObs.setViewportView(jTextAreaObs);
		jPanelDatos.add(jScrollPaneObs, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 450, 40));

		jPanelDatos.add(jLabelExtension, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));
		jPanelDatos.add(jTextFieldExtension, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 90, -1));
        jPanelDatos.add(jLabelTipo,  new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 80, -1, -1));
        jPanelDatos.add(jComboBoxTipo,  new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 160, -1));

        jPanelDatos.add(jLabelPlantador,  new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));
        jPanelDatos.add(jTextFieldPlantador,  new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 320, -1));


        jPanelDatos.add(jLabelFechaPlantacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));
        jPanelDatos.add(jTextFieldFechaPlantacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 80, -1));
        jPanelDatos.add(jButtonFechaPlantacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 140, 20, 20));
        jPanelDatos.add(jLabelFechaTala,  new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 140, -1, -1));
        jPanelDatos.add(jTextFieldFechaTala,  new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 140, 80, -1));
        jPanelDatos.add(jButtonFechaTala, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 20, 20));


		jPanelListado.setLayout(new java.awt.BorderLayout());
		actualizarModelo();
		jScrollPaneListado.setViewportView(jTableListado);
		jPanelListado.add(jScrollPaneListado, java.awt.BorderLayout.CENTER);
		JPanel auxPanel = new JPanel();
		auxPanel.setLayout(new java.awt.BorderLayout());
		auxPanel.add(jPanelDatos, java.awt.BorderLayout.SOUTH);
		auxPanel.add(jPanelListado, java.awt.BorderLayout.CENTER);
        try
        {
            ClassLoader cl =this.getClass().getClassLoader();
            Icon icon= new javax.swing.ImageIcon(cl.getResource("img/arbolado.jpg"));
            jTabbedPaneDatos.addTab("Listado", icon,auxPanel);
        }catch(Exception e)
        {
            jTabbedPaneDatos.addTab("Listado", auxPanel);
        }
		jPanelPrincipal.add(jTabbedPaneDatos, java.awt.BorderLayout.CENTER);
		jPanelMapa.setLayout(new java.awt.BorderLayout());
		jPanelMapa.setBorder(new javax.swing.border.EtchedBorder());
		jPanelContenedorMapa.setLayout(new java.awt.GridLayout(1, 0));
		jPanelMapa.add(jPanelContenedorMapa, java.awt.BorderLayout.CENTER);
        if (CMainContaminantes.geopistaEditor == null) CMainContaminantes.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");
        geopistaEditor= CMainContaminantes.geopistaEditor;

        boolean permitido=true;
        if (CMainContaminantes.acl!=null)
                   permitido=CMainContaminantes.acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_CONTAMINANTES));
		//layer = ShowMaps.showLayer(geopistaEditor, layerName, permitido, new String[]{"parcelas", "numeros_policia",layerName}, new Color(0, 128, 128));   //"parcelas","base_cartografica",

        /** comprobamos que ya haya sido cargado el mapa para no volver a hacerlo y ocultamos las capas correspondientes */
        Object[] visibles= new Object[1];
        visibles[0]= layerName;
        Object[] novisibles= new Object[2];
        novisibles[0]= JFrameContaminantes.getLayername();
        novisibles[1]= JFrameVertedero.getLayername();

        if ((geopistaEditor.getLayerManager().getLayers() == null) || (geopistaEditor.getLayerManager().getLayers().size() == 0)){
            if (ShowMaps.showMap(com.geopista.app.contaminantes.Constantes.url,geopistaEditor,
                     JFrameGeneracionPlanos.getMapID(), true,frame,null)==null){
                new JOptionPane("No existe el mapa "+JFrameGeneracionPlanos.getMapID()+" en el sistema. \nContacte con el administrador."
                        , JOptionPane.ERROR_MESSAGE).createDialog(frame, "ERROR").show();
                return;
            }
        }

        ShowMaps.allLayersEditable(geopistaEditor,false);
        layer=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer(layerName);
        layer.setEditable(true);
        layer.setActiva(true);
        ShowMaps.setLayersVisible(geopistaEditor, visibles, novisibles);
        geopistaEditor.getSelectionManager().clear();
        try{geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();}catch(Exception e){}
        jPanelContenedorMapa.add(geopistaEditor);


        geopistaEditor.removeAllGeopistaListener();
		geopistaEditor.addGeopistaListener(new GeopistaListener() {

			public void selectionChanged(AbstractSelection abtractSelection) {
				logger.info("selectionChanged");
				String idSelected = refreshListSelection(layerName);
				if (idSelected != null) {
					seleccionarFila(idSelected);
					seleccionar();
					enabled(false);
				}
			}

			public void featureAdded(FeatureEvent e) {
				//aqui tengo que añadir la feature
				Collection features = e.getFeatures();
				for (Iterator i = features.iterator(); i.hasNext();) {
					Feature f = (Feature) i.next();
					Arbolado arbolado = new Arbolado();
					GeopistaFeature geoF = (GeopistaFeature)f;//ShowMaps.saveFeature(f, layer, layerName,com.geopista.app.contaminantes.Constantes.url);
                    boolean idValido=false;
                    try {new Integer(geoF.getSystemId());idValido=true;}catch(Exception ex){}
                    if (geoF.getSystemId()==null || geoF.getSystemId().length()<=0 || !idValido)
                    {	JOptionPane optionPane = new JOptionPane(messages.getString("JFrameContaminantes.insertError"), JOptionPane.ERROR_MESSAGE);
						JDialog dialog = optionPane.createDialog(frame, "ERROR");
						dialog.show();
                        arboladoID = null;
						//geopistaEditor.deleteSelectedFeatures();
					} else {
						arbolado.setId(geoF.getSystemId());
						arbolado.setObs((String) geoF.getAttribute(2));
						try {
							arbolado.setExt(((Number) geoF.getAttribute(1)).floatValue());
						} catch (Exception ex) {
						}
						listaArbolados.add(arbolado);
						actualizarModelo();
						seleccionarFila(arbolado.getId());
						enabled(false);
						seleccionar();
						((GeopistaFeature) f).setSystemId(arbolado.getId());
                        try
                        {
                            new OperacionesContaminantes((com.geopista.app.contaminantes.Constantes.url)).gestionarHistorico(new Historico(new Integer(arbolado.getId()).intValue(), com.geopista.app.contaminantes.Constantes.ADD, com.geopista.app.contaminantes.Constantes.ARBOLEDAS));
                        }catch(Exception ex){}
		//				f.setAttribute(0, new Integer(arbolado.getId()));

					}
				}
				logger.info("featureAdded");

			}

			public void featureRemoved(FeatureEvent e) {
                //delete(false);
				logger.info("featureRemoved");

			}

			public void featureModified(FeatureEvent e) {
				/*Collection features = e.getFeatures();
				for (Iterator i = features.iterator(); i.hasNext();) {
					Feature f = (Feature) i.next();
					Arbolado aux = getArbolado(((GeopistaFeature) f).getSystemId());
					f.setAttribute(0, new Integer(aux.getId()));
					f.setAttribute(1, new Float(aux.getExt()));
					f.setAttribute(2, aux.getObs());
					//GeopistaFeature geoF = ShowMaps.updateFeature(f, layer, layerName,com.geopista.app.contaminantes.Constantes.url);
					seleccionar();
				} */
                logger.info("featureModified");
			}

			public void featureActioned(IAbstractSelection abtractSelection) {
				logger.info("featureActioned");
			}

		});

		jPanelPrincipal.setMinimumSize(new Dimension(600, 600));

		getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.WEST);
		getContentPane().add(jPanelMapa, java.awt.BorderLayout.CENTER);
		getContentPane().add(jPanelTotal, java.awt.BorderLayout.SOUTH);

		pack();
	}//GEN-END:initComponents

	private void initListas() {

		try {
			listaArbolados = (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).getArbolados();
			if (listaArbolados == null)
				logger.warn("No existen zonas arboladas en el sistema");
			else
				logger.info("Numero de zonas arboladas: " + listaArbolados.size());

		} catch (Exception e) {
			logger.error("Error al inicializa la lista de zonas arboladas: " + e.toString());
			JOptionPane optionPane = new JOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(frame, messages.getString("JFrameArbolado.JOptionPane.msg1.text"));
			dialog.show();
		}
	}

	private void salir() {
		dispose();
	}


	private Vector actualizarListado(Arbolado newArbolado, boolean borrar) {

		Vector vector = new Vector();

		for (int i = 0; i < listaArbolados.size(); i++) {
			Arbolado arbolado = (Arbolado) listaArbolados.elementAt(i);

			if (newArbolado.getId().equalsIgnoreCase(arbolado.getId())) {
				if (!borrar) {
					vector.add(newArbolado);
				}
				continue;
			}
			vector.add(arbolado);

		}

		return vector;

	}

	private void actualizarModelo() {
		modelArbolado = new ArboladoTableModel();
		modelArbolado.setModelData(listaArbolados);
		sorter = new TableSorted(modelArbolado);
		sorter.setTableHeader(jTableListado.getTableHeader());
		jTableListado.setModel(sorter);
		jTableListado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


	public void categoryChanged(CategoryEvent event) {
		System.out.println("La Categoria ha cambiado");
	}

	public void featuresChanged(FeatureEvent event) {

		int j = 0;
		//for (;event.getFeatures().iterator().hasNext();)
		//{
		Feature feature = (Feature) event.getFeatures().iterator().next();
		System.out.println(j + " Feature: " + feature + ". Numero de atributos: " + feature.getAttributes().length);
		for (int i = 0; i < feature.getAttributes().length;) {
			j++;
			System.out.println("Iteracion i: " + i + ". Atributo: " + feature.getAttribute(i));
			i++;
			if (j > 1000) return;
		}
		//}
		System.out.println("La feature ha cambiado");
	}

	public void layerChanged(LayerEvent event) {
		//System.out.println("El layer ha cambiado: Evento:" + event.getType());
		// if (event.getType()==);
	}

	private Arbolado getArbolado(String idArbolado) {
		if (listaArbolados == null || idArbolado == null) return null;
		for (Enumeration e = listaArbolados.elements(); e.hasMoreElements();) {
			Arbolado aux = (Arbolado) e.nextElement();
			if (idArbolado.equalsIgnoreCase(aux.getId()))
				return aux;
		}
		return null;
	}
    private void  generarFicha()
    {
        try
        {
            if (getArbolado(arboladoID)==null) return;
            new GeopistaPrintableContaminantes().printObjeto(FichasDisponibles.fichaarbolado, getArbolado(arboladoID) , Arbolado.class, getGeopistaEditor().getLayerViewPanel(), GeopistaPrintableContaminantes.FICHA_CONTAMINANTE_ARBOLADO);
        }catch(Exception e)
        {
            logger.error("Excepción al imprimir la ficha: ",e);
        }
   }


	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonBuscar;
	private javax.swing.JButton jButtonEdit;
	private javax.swing.JButton jButtonSave;
	private javax.swing.JButton jButtonDelete;
	private javax.swing.JButton jButtonCancel;
	private javax.swing.JButton jButtonSalir;
	private javax.swing.JLabel jLabelExtension;
	private javax.swing.JLabel jLabelObs;
	private javax.swing.JPanel jPanelBotonera;
	private javax.swing.JPanel jPanelContenedorMapa;
	private javax.swing.JPanel jPanelDatos;
	private javax.swing.JPanel jPanelListado;
	private javax.swing.JPanel jPanelMapa;
	private javax.swing.JPanel jPanelPrincipal;
	private javax.swing.JPanel jPanelTotal;
	private javax.swing.JScrollPane jScrollPaneListado;
	private javax.swing.JScrollPane jScrollPaneObs;
	private javax.swing.JTabbedPane jTabbedPaneDatos;
	private javax.swing.JTable jTableListado;
	private TextPane jTextAreaObs;
	private JNumberTextField jTextFieldExtension;
    private javax.swing.JLabel  jLabelTipo;
    private ComboBoxEstructuras jComboBoxTipo;
    private javax.swing.JLabel   jLabelPlantador;
    private javax.swing.JTextField   jTextFieldPlantador;
    private javax.swing.JLabel   jLabelFechaPlantacion;
    private JFormattedTextField   jTextFieldFechaPlantacion;
    private CalendarButton   jButtonFechaPlantacion;
    private javax.swing.JLabel  jLabelFechaTala;
    private JFormattedTextField  jTextFieldFechaTala;
    private CalendarButton   jButtonFechaTala;
    private javax.swing.JButton jButtonGenerarFicha;
	// End of variables declaration//GEN-END:variables


	public void changeScreenLang(ResourceBundle messages) {
		this.messages = messages;

		try {
			setTitle(messages.getString("JFrameArbolado.JInternalFrame.title"));
			jPanelContenedorMapa.setBorder(new javax.swing.border.TitledBorder(messages.getString("JFrameArbolado.jPanelContenedorMapa.TitleBorder")));
			jLabelExtension.setText(messages.getString("JFrameArbolado.jLabelExtension.text"));
			jLabelObs.setText(messages.getString("JFrameArbolado.jLabelObs.text"));
			jButtonBuscar.setText(messages.getString("JFrameArbolado.jButtonBuscar.text"));
			jButtonEdit.setText(messages.getString("JFrameArbolado.jButtonEdit.text"));
			jButtonSave.setText(messages.getString("JFrameArbolado.jButtonSave.text"));
			jButtonCancel.setText(messages.getString("JFrameArbolado.jButtonCancel.text"));
			jButtonDelete.setText(messages.getString("JFrameArbolado.jButtonDelete.text"));
			jButtonSalir.setText(messages.getString("JFrameArbolado.jButtonSalir.text"));
            jButtonGenerarFicha.setText(messages.getString("JFrameGeneracionPlanos.jButtonGenerarFicha"));//"Fecha Fin:");
			jTabbedPaneDatos.setTitleAt(0, messages.getString("JFrameArbolado.jTabbedPaneDatos.tab0.text"));

            jButtonBuscar.setToolTipText(messages.getString("JFrameArbolado.jButtonBuscar.text"));
            jButtonEdit.setToolTipText(messages.getString("JFrameArbolado.jButtonEdit.text"));
            jButtonSave.setToolTipText(messages.getString("JFrameArbolado.jButtonSave.text"));
            jButtonCancel.setToolTipText(messages.getString("JFrameArbolado.jButtonCancel.text"));
            jButtonDelete.setToolTipText(messages.getString("JFrameArbolado.jButtonDelete.text"));
            jButtonSalir.setToolTipText(messages.getString("JFrameArbolado.jButtonSalir.text"));
            jButtonGenerarFicha.setToolTipText(messages.getString("JFrameGeneracionPlanos.jButtonGenerarFicha"));//"Fecha Fin:");
            jButtonFechaTala.setToolTipText(messages.getString("JFrameArbolado.jButtonFechaTala.text"));
            jButtonFechaPlantacion.setToolTipText(messages.getString("JFrameArbolado.jButtonFechaPlantacion.text"));

			ArboladoTableModel.setColumnNames(new String[]{messages.getString("JFrameArbolado.modelArbolado.col0.text"), messages.getString("JFrameArbolado.modelArbolado.col1.text"), messages.getString("JFrameArbolado.modelArbolado.col2.text")});
            jLabelTipo.setText(messages.getString("JFrameArbolado.Tipo"));
            jLabelPlantador.setText(messages.getString("JFrameArbolado.PlantadoPor"));
            jLabelFechaPlantacion.setText(messages.getString("JFrameArbolado.FechaPlantación"));
            jLabelFechaTala.setText(messages.getString("JFrameArbolado.FechaUltimaTala"));
            actualizarModelo();

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}
}
