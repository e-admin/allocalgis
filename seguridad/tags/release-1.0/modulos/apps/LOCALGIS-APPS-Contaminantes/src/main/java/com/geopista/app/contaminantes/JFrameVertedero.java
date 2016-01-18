package com.geopista.app.contaminantes;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.netbeans.lib.awtextra.AbsoluteLayout;

import com.geopista.app.contaminantes.panel.CAddressJDialog;
import com.geopista.app.contaminantes.panel.JPanelVertedero;
import com.geopista.app.printer.FichasDisponibles;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.Historico;
import com.geopista.protocol.contaminantes.OperacionesContaminantes;
import com.geopista.protocol.contaminantes.Vertedero;
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


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 18-oct-2004
 * Time: 17:10:22
 */
public class JFrameVertedero extends JInternalFrameShowMap implements LayerListener {

	private ResourceBundle messages;
	private JFrame frame;
	private boolean modoEdicion = false;
	VertederoTableModel modelVertedero;
	Vector listaVertederos = null;
	private TableSorted sorter;
	private Vertedero vertederoSelected = null;
    private static final String layerName="Vertedero";


	/**
	 * Creates new form JFrameArbolado
	 */
	public JFrameVertedero(final ResourceBundle messages, final JFrame frame) {
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
    }

    public static String getLayername() {
        return layerName;
    }
	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() throws CancelException{//GEN-BEGIN:initComponents
		jPanelPrincipal = new javax.swing.JPanel();
		jPanelTotal = new javax.swing.JPanel();
		jPanelBotonera = new javax.swing.JPanel();

		jButtonBuscar = new javax.swing.JButton();
		jButtonEdit = new javax.swing.JButton();
        jButtonGenerarFicha = new javax.swing.JButton();
		jButtonSave = new javax.swing.JButton();
		jButtonCancel = new javax.swing.JButton();
		jButtonDelete = new javax.swing.JButton();
		jButtonSalir = new javax.swing.JButton();
		jTabbedPaneDatos = new javax.swing.JTabbedPane();
		jPanelVertedero = new JPanelVertedero(this.frame, this.messages);
		jPanelListado = new javax.swing.JPanel();
		jScrollPaneListado = new javax.swing.JScrollPane();
		jTableListado = new javax.swing.JTable();
		jPanelMapa = new javax.swing.JPanel();
		jPanelContenedorMapa = new javax.swing.JPanel();


		jPanelPrincipal.setLayout(new java.awt.BorderLayout());




		jPanelBotonera.setLayout(new AbsoluteLayout());


		jPanelBotonera.add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 100, 20));
		jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonBuscarActionPerformed();
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
				eliminar(true);
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
		jPanelPrincipal.add(jPanelBotonera, java.awt.BorderLayout.SOUTH);
		jTabbedPaneDatos.setBorder(new javax.swing.border.EtchedBorder());
		jPanelListado.setLayout(new java.awt.BorderLayout());
		actualizarModelo();
		jScrollPaneListado.setViewportView(jTableListado);
		jPanelListado.add(jScrollPaneListado, java.awt.BorderLayout.CENTER);
		ListSelectionModel rowSM = jTableListado.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				seleccionarVertedero(e);
			}
		});

		JPanel auxPanel = new JPanel();
		auxPanel.setLayout(new java.awt.BorderLayout());
		auxPanel.add(jPanelVertedero, java.awt.BorderLayout.SOUTH);
		auxPanel.add(jPanelListado, java.awt.BorderLayout.CENTER);
        try
        {
            ClassLoader cl =this.getClass().getClassLoader();
            Icon icon= new javax.swing.ImageIcon(cl.getResource("img/vertedero.jpg"));
            jTabbedPaneDatos.addTab(messages.getString("JFrameVertedero.jTabbedPaneDatos"), icon,auxPanel);
        }catch(Exception e)
        {
           jTabbedPaneDatos.addTab(messages.getString("JFrameVertedero.jTabbedPaneDatos"), auxPanel);
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

        /** comprobamos que ya haya sido cargado el mapa para no volver a hacerlo y ocultamos las capas correspondientes */
        Object[] visibles= new Object[1];
        visibles[0]= layerName;
        Object[] novisibles= new Object[2];
        novisibles[0]= JFrameContaminantes.getLayername();
        novisibles[1]= JFrameArbolado.getLayername();


        if ((geopistaEditor.getLayerManager().getLayers() == null) || (geopistaEditor.getLayerManager().getLayers().size() == 0))
        {
            if(ShowMaps.showMap(com.geopista.app.contaminantes.Constantes.url,geopistaEditor,
                     JFrameGeneracionPlanos.getMapID(), true,frame,null)==null)
            {
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
			   String idSelected=refreshListSelection(layerName);
                if (idSelected!=null)
                {
                    seleccionarFila(idSelected);
                }
			}

			public void featureAdded(FeatureEvent e) {
                //aqui tengo que añadir la feature
                Collection features=e.getFeatures();
                for(Iterator i=features.iterator();i.hasNext();)
                {
                    Feature f=(Feature)i.next();
                    Vertedero vertedero= new Vertedero();
                    GeopistaFeature geoF=(GeopistaFeature)f;//ShowMaps.saveFeature(f,layer, layerName,com.geopista.app.contaminantes.init.Constantes.url);
                    boolean idValido=false;
                    try {new Integer(geoF.getSystemId());idValido=true;}catch(Exception ex){}
                    if (geoF.getSystemId()==null || geoF.getSystemId().length()<=0 || !idValido)
                    {
                            JOptionPane optionPane = new JOptionPane(messages.getString("JFrameVertedero.insertError"), JOptionPane.ERROR_MESSAGE);
			                JDialog dialog = optionPane.createDialog(frame, "ERROR");
			                dialog.show();
                            vertederoSelected=null;
                            //geopistaEditor.deleteSelectedFeatures();
                    }
                    else
                    {
                        vertedero.setId(geoF.getSystemId());
                        vertedero.setTipo((String)geoF.getAttribute(1));
                        vertedero.setTitularidad((String)geoF.getAttribute(2));
                        vertedero.setgAdm((String)geoF.getAttribute(3));
                        vertedero.setpExistentes((String)geoF.getAttribute(4));
                        try{vertedero.setCapacidad(new Long(((BigDecimal)geoF.getAttribute(5)).longValue()));}catch(Exception ex){}
                        try{vertedero.setgOcupacion(new Float(((BigDecimal)geoF.getAttribute(6)).floatValue()));}catch(Exception ex){}
                        vertedero.setPosiAmplia((String)geoF.getAttribute(7));
                        vertedero.setEstado((String)geoF.getAttribute(8));
                        try{vertedero.setVidaUtil(new Integer(((BigDecimal)geoF.getAttribute(9)).intValue()));}catch(Exception ex){}
                        listaVertederos.add(vertedero);
                        actualizarModelo();
                        ((GeopistaFeature)f).setSystemId(vertedero.getId());
                        //f.setAttribute(0,new Integer(vertedero.getId()));
                        seleccionarFila(vertedero.getId());
                        editar();
                        refreshFeatureSelection(layerName,vertederoSelected.getId());
                        try
                        {
                            new OperacionesContaminantes((com.geopista.app.contaminantes.Constantes.url)).gestionarHistorico(new Historico(new Integer(vertederoSelected.getId()).intValue(), com.geopista.app.contaminantes.Constantes.ADD, com.geopista.app.contaminantes.Constantes.VERTEDEROS));
                        }catch(Exception ex){}

                    }
                }
    		    logger.info("featureAdded");
			}

			public void featureRemoved(FeatureEvent e) {
				//eliminar(false);
                logger.info("featureRemoved");
			}

			public void featureModified(FeatureEvent e) {
              /*  Collection features=e.getFeatures();
                for(Iterator i=features.iterator();i.hasNext();)
                {
                    Feature f=(Feature)i.next();
                    Vertedero aux= getVertedero(((GeopistaFeature)f).getSystemId());
                    grabarEnFeature(f,aux);
                    GeopistaFeature geoF=ShowMaps.updateFeature(f,layer,layerName,com.geopista.app.contaminantes.init.Constantes.url);
                    refreshFeatureSelection(layerName,aux.getId());
                }*/
    		    logger.info("featureModified");
			}

			public void featureActioned(IAbstractSelection abtractSelection) {
				logger.info("featureActioned");
			}

		});


		geopistaEditor.addLayerListener(this);
		changeScreenLang(messages);
		jPanelPrincipal.setMinimumSize(new Dimension(600, 600));

		getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.WEST);
		getContentPane().add(jPanelMapa, java.awt.BorderLayout.CENTER);
		getContentPane().add(jPanelTotal, java.awt.BorderLayout.SOUTH);
		enabled(false);
		pack();
	}//GEN-END:initComponents


	private void jButtonBuscarActionPerformed() {
		logger.info("Inicio.");
        valoresBusqueda.clear();
		CAddressJDialog dialog = new CAddressJDialog(this.frame, true, messages);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();
        valoresBusqueda=dialog.getValoresBusqueda();
        refreshBusquedaSelection(this,geopistaEditor);

	}

	private void editar() {
		if (vertederoSelected == null) {
			JOptionPane optionPane = new JOptionPane(messages.getString("JFrameVertedero.mensaje.vernoseleccionado"), JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "INFO");
			dialog.show();
			return;
		}
		enabled(true);
	}

	public void cancelar() {
		if (!modoEdicion) return;
		int n = JOptionPane.showOptionDialog(this,
				messages.getString("JFrameVertedero.mensaje.desechar"),
				"INFO",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (n == JOptionPane.NO_OPTION) return;
		enabled(false);
		jPanelVertedero.load(vertederoSelected);

	}

	private void initListas() {

		try {
			listaVertederos = (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).getVertederos();
			if (listaVertederos == null)
				logger.warn("No existen vertederos en el sistema");
			else
				logger.info("Numero de vertederos: " + listaVertederos.size());

		} catch (Exception e) {
			logger.error("Error al inicializa la lista de vertederos: " + e.toString());
			JOptionPane optionPane = new JOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(frame, "ERROR al inicializar la lista de vertederos");
			dialog.show();
		}
	}

	private void salir() {
		if (modoEdicion) {
			int n = JOptionPane.showOptionDialog(this,
					messages.getString("JFrameVertedero.mensaje.desechar"),
					"INFO",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (n == JOptionPane.NO_OPTION) return;
		}

		dispose();
	}

	public void changeScreenLang(ResourceBundle messages) {
        try
        {
            this.messages = messages;
            setTitle(messages.getString("JFrameVertedero.title"));//"Vertederos\n");
            jButtonBuscar.setText(messages.getString("JFrameVertedero.jButtonBuscar"));//"Editar");
            jButtonEdit.setText(messages.getString("JFrameVertedero.jButtonEdit"));//"Editar");
            jButtonGenerarFicha.setText(messages.getString("JFrameGeneracionPlanos.jButtonGenerarFicha"));//"Fecha Fin:");
		    jButtonSave.setText(messages.getString("JFrameVertedero.jButtonSave"));//"Salvar");
            jButtonCancel.setText(messages.getString("JFrameVertedero.jButtonCancel"));//"Cancelar");
            jButtonSalir.setText(messages.getString("JFrameVertedero.jButtonSalir"));//"Salir");
            jButtonDelete.setText(messages.getString("JFrameVertedero.jButtonDelete"));//"Delete");
            jPanelContenedorMapa.setBorder(new javax.swing.border.TitledBorder(messages.getString("JFrameVertedero.jPanelContenedorMapa")));//"Mapa"));
            jTabbedPaneDatos.setTitleAt(0, messages.getString("JFrameVertedero.jTabbedPaneDatos"));
            VertederoTableModel.setColumnNames(new String[]{messages.getString("JPanelVertedero.VertederoTableModel.col0"), messages.getString("JPanelVertedero.VertederoTableModel.col1"), messages.getString("JPanelVertedero.VertederoTableModel.col2")});
            jPanelVertedero.changeScreenLang(messages);
            actualizarModelo();

            jButtonBuscar.setToolTipText(messages.getString("JFrameVertedero.jButtonBuscar"));
            jButtonEdit.setToolTipText(messages.getString("JFrameVertedero.jButtonEdit"));
            jButtonGenerarFicha.setToolTipText(messages.getString("JFrameGeneracionPlanos.jButtonGenerarFicha"));
		    jButtonSave.setToolTipText(messages.getString("JFrameVertedero.jButtonSave"));
            jButtonCancel.setToolTipText(messages.getString("JFrameVertedero.jButtonCancel"));
            jButtonSalir.setToolTipText(messages.getString("JFrameVertedero.jButtonSalir"));
            jButtonDelete.setToolTipText(messages.getString("JFrameVertedero.jButtonDelete"));
        }catch(Exception e)
        {
            logger.error("Error al cargar las etiquetas: ",e);
        }
 	}

	private void actualizarModelo() {
		modelVertedero = new VertederoTableModel();
		modelVertedero.setModelData(listaVertederos);
		sorter = new TableSorted(modelVertedero);
		sorter.setTableHeader(jTableListado.getTableHeader());
		jTableListado.setModel(sorter);
		jTableListado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	}


	public void categoryChanged(CategoryEvent event) {
		System.out.println("La Categoria ha cambiado");
	}

	public void featuresChanged(FeatureEvent event) {

		System.out.println("La feature ha cambiado");
	}

	public void enabled(boolean bValor) {
		modoEdicion = bValor;
        boolean permitido=true;
        if (CMainContaminantes.acl!=null)
            permitido=CMainContaminantes.acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_CONTAMINANTES));
	    jButtonEdit.setEnabled(!bValor && permitido);
		jButtonDelete.setEnabled(!bValor && permitido);
        jButtonGenerarFicha.setEnabled(!bValor);
		jButtonSave.setEnabled(bValor && permitido);
		jButtonCancel.setEnabled(bValor);
		jPanelVertedero.setEnabled(bValor);

	}

	private void seleccionarVertedero(ListSelectionEvent e) {
		if (modoEdicion) {
			int n = JOptionPane.showOptionDialog(this,
					messages.getString("JFrameVertedero.mensaje.desechar"),
					"INFO",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (n == JOptionPane.NO_OPTION)
				return;
			else
				enabled(false);
		}
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
		} else {
			int selectedRow = lsm.getMinSelectionIndex();
			String idVertedero = (String) sorter.getValueAt(selectedRow, VertederoTableModel.idIndex);
			vertederoSelected = getVertedero(idVertedero);
			if (vertederoSelected == null) {
				JOptionPane optionPane = new JOptionPane(messages.getString("JFrameVertedero.mensaje.vernoencontrado"), JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(this, "INFO");
				dialog.show();
			}
            else
                refreshFeatureSelection(layerName,vertederoSelected.getId());

			jPanelVertedero.load(vertederoSelected);
            boolean permitido=true;
            if (CMainContaminantes.acl!=null)
                permitido=CMainContaminantes.acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_CONTAMINANTES));
	        jButtonEdit.setEnabled(permitido);
            jButtonGenerarFicha.setEnabled(true);
		}
	}
    private void seleccionarFila(String idSelected)
    {
        vertederoSelected = getVertedero(idSelected);
	    jPanelVertedero.load(vertederoSelected);
		enabled(false);
        for (int i = 0; i < jTableListado.getRowCount(); i++)
        {
            String listId = (String) jTableListado.getValueAt(i, 0);
            logger.debug("listId: -" + listId + "-");
            if (listId.equalsIgnoreCase(idSelected)) {
                jTableListado.changeSelection(i, i + 1, false, false);
            }
        }
    }

	private void salvar() {
		if (!modoEdicion) return;
		try {
			if (!jPanelVertedero.validar()) {
				return;
			}
			jPanelVertedero.guardarCambios();
			CResultadoOperacion result = null;
			String sMensaje = "";
			try {
				result = (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).saveVertedero(jPanelVertedero.getVertedero());
				sMensaje = messages.getString("JFrameVertedero.mensaje.vertederoactualizado");

			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				logger.error("Exception al grabar en base de datos un nuevo contacto: " + sw.toString());
				result = new CResultadoOperacion(false, e.getMessage());
			}
			if (result.getResultado()) {
				try {
					jPanelVertedero.setVertedero((Vertedero) result.getVector().elementAt(0));
				} catch (Exception e) {
					java.io.StringWriter sw = new java.io.StringWriter();
					java.io.PrintWriter pw = new java.io.PrintWriter(sw);
					e.printStackTrace(pw);
					logger.error("Error al coger el objeto vertedero: " + sw.toString());
				}

				logger.debug("Identificador del vertedero insertado:" + jPanelVertedero.getVertedero().getId());

				vertederoSelected = jPanelVertedero.getVertedero();
                try
                {
                     new OperacionesContaminantes((com.geopista.app.contaminantes.Constantes.url)).gestionarHistorico(new Historico(new Integer(vertederoSelected.getId()).intValue(), com.geopista.app.contaminantes.Constantes.MODIFICAR, com.geopista.app.contaminantes.Constantes.VERTEDEROS));
                }catch(Exception ex){}

				Vertedero ins = getVertedero(vertederoSelected.getId());
				ins.copy(vertederoSelected);
				enabled(false);
				jPanelVertedero.load(vertederoSelected);
                loadFeature(vertederoSelected);
                listaVertederos=actualizarListado(vertederoSelected,false);
				actualizarModelo();
                Collection cgeo=geopistaEditor.getSelection();
                for (Iterator i=cgeo.iterator();i.hasNext();)
                {
                    GeopistaFeature gf=(GeopistaFeature)i.next();
                    try
                    {
                        if (gf.getSystemId().equals(vertederoSelected.getId()))
                        {
                            gf.setFireDirtyEvents(false);
                            gf.setAttribute(1,vertederoSelected.getTipo());
                            gf.setAttribute(2,vertederoSelected.getTitularidad());
                            gf.setAttribute(3,vertederoSelected.getgAdm());
                            gf.setAttribute(4,vertederoSelected.getpExistentes());
                            gf.setAttribute(5,(Number)vertederoSelected.getCapacidad());
                            gf.setAttribute(6,(Number)(new Integer(vertederoSelected.getgOcupacion().intValue())));
                            gf.setAttribute(7,vertederoSelected.getPosiAmplia());
                            gf.setAttribute(8,vertederoSelected.getEstado());
                            gf.setAttribute(9,(Number)vertederoSelected.getVidaUtil());
                        }
                    }catch(Exception e)
                    {
                         StringWriter sw = new StringWriter();
                         PrintWriter pw = new PrintWriter(sw);
                         e.printStackTrace(pw);
                         logger.error("Exception: " + sw.toString());
                    }
                }
                JOptionPane optionPane = new JOptionPane(sMensaje, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog(this, "INFO");
                dialog.show();

			} else {
				JOptionPane optionPane = new JOptionPane(result.getDescripcion(), JOptionPane.ERROR_MESSAGE);
				JDialog dialog = optionPane.createDialog(this, "INFO");
				dialog.show();
			}
		} catch (Exception ex) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Excepcion al modificar el vertedero: " + sw.toString());
			JOptionPane optionPane = new JOptionPane(ex, JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.show();
		}

	}
    private void loadFeature(Vertedero vertedero)
    {
                //actualizamos la feature
        Collection cgeo=geopistaEditor.getSelection();
        if (cgeo==null) return;
        if (cgeo.iterator().hasNext())
        {
              GeopistaFeature gf=(GeopistaFeature)cgeo.iterator().next();
              try
              {
                    if (gf.getSystemId().equals(vertedero.getId()))
                    {

                    }
              }catch(Exception e)
              {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.error("Exception: " + sw.toString());
              }
        }
    }
    public void grabarEnFeature(Feature gf, Vertedero vertedero)
    {
           gf.setAttribute(0,new Integer(vertedero.getId()));
           gf.setAttribute(1,vertedero.getTipo());
           gf.setAttribute(2,vertedero.getTitularidad());
           gf.setAttribute(3,vertedero.getgAdm());
           gf.setAttribute(4,vertedero.getpExistentes());
           gf.setAttribute(5,vertedero.getCapacidad());
           gf.setAttribute(6,vertedero.getgOcupacion());
           gf.setAttribute(7,vertedero.getPosiAmplia());
           gf.setAttribute(8,vertedero.getEstado());
           gf.setAttribute(9,vertedero.getVidaUtil());
    }
	public void eliminar(boolean mostrarMensaje) {
		if (vertederoSelected == null) {
			JOptionPane optionPane = new JOptionPane(messages.getString("JFrameVertedero.mensaje.vernoseleccionado"), JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "INFO");
			dialog.show();
			return;
		}
        if (mostrarMensaje)
        {
            refreshFeatureSelection(layerName,vertederoSelected.getId());
		    int n = JOptionPane.showOptionDialog(this,
				messages.getString("JFrameVertedero.mensaje.eliminarvertedero"),
				"",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		    if (n == JOptionPane.NO_OPTION) return;
        }

		CResultadoOperacion result;
		try {
			result=(new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).deleteVertedero(vertederoSelected);
			//result = new CResultadoOperacion(true, "");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception al eliminar residuo en base de datos: " + sw.toString());
			result = new CResultadoOperacion(false, e.getMessage());
		}
		if (result.getResultado()) {
            listaVertederos=actualizarListado(vertederoSelected,true);
            try
            {
                new OperacionesContaminantes((com.geopista.app.contaminantes.Constantes.url)).gestionarHistorico(new Historico(new Integer(vertederoSelected.getId()).intValue(), com.geopista.app.contaminantes.Constantes.BORRAR  , com.geopista.app.contaminantes.Constantes.VERTEDEROS));
             }catch(Exception ex){}
			vertederoSelected = null;
            if (mostrarMensaje)
            {
			    JOptionPane optionPane = new JOptionPane(messages.getString("JFrameVertedero.mensaje.vertederoeliminado"), JOptionPane.INFORMATION_MESSAGE);
			    JDialog dialog = optionPane.createDialog(this, "");
			    dialog.show();
            }
			enabled(false);
			jPanelVertedero.load(vertederoSelected);
			actualizarModelo();
            ShowMaps.deleteFeature(geopistaEditor);
		} else{

			JOptionPane optionPane = new JOptionPane(result.getDescripcion(), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.show();
		}
	}

	private Vertedero getVertedero(String idVertedero) {
		if (listaVertederos == null || idVertedero == null) return null;
		for (Enumeration e = listaVertederos.elements(); e.hasMoreElements();) {
			Vertedero aux = (Vertedero) e.nextElement();
			if (idVertedero.equalsIgnoreCase(aux.getId()))
				return aux;
		}
		return null;
	}

	public void layerChanged(LayerEvent event) {
		System.out.println("El layer ha cambiado: Evento:" + event.getType());
		// if (event.getType()==);
	}


    private Vector actualizarListado(Vertedero vertedero, boolean borrar) {
		Vector vector = new Vector();
		for (int i = 0; i < listaVertederos.size(); i++) {
			Vertedero auxVertedero = (Vertedero) listaVertederos.elementAt(i);
			if (auxVertedero.getId().equalsIgnoreCase(vertedero.getId())) {
				if (!borrar) {
					vector.add(vertedero);
				}
				continue;
			}
			vector.add(auxVertedero);
		}
		return vector;
	}
     private void  generarFicha()
     {
         try
         {
             if (vertederoSelected==null) return;
             new GeopistaPrintableContaminantes().printObjeto(FichasDisponibles.fichavertedero, vertederoSelected , Vertedero.class, getGeopistaEditor().getLayerViewPanel(), GeopistaPrintableContaminantes.FICHA_CONTAMINANTE_VERTEDERO);
         }catch(Exception e)
         {
             logger.error("Excepción al imprimir la ficha: ",e);
         }
    }
	private javax.swing.JButton jButtonBuscar;

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonGenerarFicha;
	private javax.swing.JButton jButtonCancel;
	private javax.swing.JButton jButtonSave;
	private javax.swing.JButton jButtonSalir;
	private javax.swing.JButton jButtonDelete;
	private javax.swing.JPanel jPanelBotonera;
	private javax.swing.JPanel jPanelContenedorMapa;
	private JPanelVertedero jPanelVertedero;
	private javax.swing.JPanel jPanelListado;
	private javax.swing.JPanel jPanelMapa;
	private javax.swing.JPanel jPanelPrincipal;
	private javax.swing.JPanel jPanelTotal;
	private javax.swing.JScrollPane jScrollPaneListado;
	private javax.swing.JTabbedPane jTabbedPaneDatos;
	private javax.swing.JTable jTableListado;
	// End of variables declaration//GEN-END:variables


}
