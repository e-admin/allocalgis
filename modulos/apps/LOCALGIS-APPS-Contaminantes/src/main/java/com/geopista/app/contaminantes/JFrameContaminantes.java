/**
 * JFrameContaminantes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.contaminantes;

import java.awt.Cursor;
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

import org.apache.log4j.Logger;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import com.geopista.app.contaminantes.panel.CAddressJDialog;
import com.geopista.app.contaminantes.panel.JPanelActividad;
import com.geopista.app.licencias.CConstantesLicencias;
import com.geopista.app.printer.FichasDisponibles;
import com.geopista.app.printer.GeopistaPrintableContaminantes;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.CAnexo;
import com.geopista.protocol.contaminantes.Contaminante;
import com.geopista.protocol.contaminantes.Historico;
import com.geopista.protocol.contaminantes.Inspeccion;
import com.geopista.protocol.contaminantes.NumeroPolicia;
import com.geopista.protocol.contaminantes.OperacionesContaminantes;
import com.geopista.server.administradorCartografia.CancelException;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author angeles
 */
public class JFrameContaminantes extends JInternalFrameShowMap {
	Logger logger = Logger.getLogger(JFrameContaminantes.class);
	private ResourceBundle messages;
	private JFrame parent;
	private boolean modoEdicion = false;
	private Contaminante actividad;
	private static final String layerName = "actividades_contaminantes";


	/**
	 * Creates new form JFrameContaminantes
	 */
	public JFrameContaminantes(final ResourceBundle messages, JFrame frame, String idActividad) {
		this.parent = frame;
		this.messages = messages;
        /** dialogo de espera de carga */
        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(parent, null);
        progressDialog.setTitle(messages.getString("Licencias.Tag1"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){
                        /* añadimos el documento a la lista */
                        try{
                            progressDialog.report(messages.getString("Licencias.Tag1"));
		                    initComponents();
                        }catch(Exception e){
                            logger.error("Error ", e);
                            ErrorDialog.show(parent, "ERROR", "ERROR", StringUtil.stackTrace(e));
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

		if (idActividad != null) initData(idActividad);
		changeScreenLang(messages);
		enabled(false);
        valoresBusqueda= new Hashtable();
	}

    public static String getLayername() {
        return layerName;
    }


	public void zoom() {
		if (geopistaEditor != null) geopistaEditor.zoomToSelected();
	}

	public void initData(String idActividad) {
		parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			actividad = (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).getActividad(idActividad);
			if (actividad == null) {
				logger.warn("No existe la actividad contaminante en el sistema");
				actividad = new Contaminante();
			} else {
				logger.info("Actividad: " + actividad.getAsunto());
                boolean permitido=true;
                if (CMainContaminantes.acl!=null)
                          permitido=CMainContaminantes.acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_CONTAMINANTES));
	         	jButtonBorrar.setEnabled(permitido);
				jButtonEdit.setEnabled(permitido);
                jButtonGenerarFicha.setEnabled(true);
			}
			jPanelActividad.load(actividad);
			refreshFeatureSelection(layerName, actividad.getId());


		} catch (Exception e) {
			logger.error("Error al buscar la actividad: ",e);
			JOptionPane optionPane = new JOptionPane(e.toString()+" "+e.getMessage(), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(parent, "ERROR al inicializar la lista de actividades contaminantes");
			dialog.show();
		}
		parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

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
		jPanelBotonera = new javax.swing.JPanel();
		jButtonBuscar = new javax.swing.JButton();
		jButtonEdit = new javax.swing.JButton();
		jButtonBorrar = new javax.swing.JButton();
		jButtonSave = new javax.swing.JButton();
		jButtonCancel = new javax.swing.JButton();
		jPanelActividad = new JPanelActividad(messages, parent);
		jPanelMapa = new javax.swing.JPanel();
		jPanelContenedorMapa = new javax.swing.JPanel();
		jPanelTotal = new javax.swing.JPanel();
		jButtonSalir = new javax.swing.JButton();
		jTabbedPaneDatos = new javax.swing.JTabbedPane();
		jTabbedPaneDatos.setBorder(new javax.swing.border.EtchedBorder());
        jButtonGenerarFicha = new javax.swing.JButton();


		jPanelPrincipal.setLayout(new java.awt.BorderLayout());


		jPanelBotonera.setLayout(new AbsoluteLayout());


		jPanelBotonera.add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 100, 20));
		jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buscarCalle();
			}
		});


		jPanelBotonera.add(jButtonEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 5, 100, 20));
		jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editar();
			}
		});
		jPanelBotonera.add(jButtonBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 5, 100, 20));
		jButtonBorrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrar(true);
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
        try
        {
            ClassLoader cl =this.getClass().getClassLoader();
            Icon icon= new javax.swing.ImageIcon(cl.getResource("img/contaminante.gif"));
            jTabbedPaneDatos.addTab(messages.getString("JFrameContaminantes.jTabbedPaneDatos"), icon,jPanelActividad);
        }catch(Exception e)
        {
            jTabbedPaneDatos.addTab(messages.getString("JFrameContaminantes.jTabbedPaneDatos"),jPanelActividad);
        }

        jPanelPrincipal.add(jPanelBotonera, java.awt.BorderLayout.SOUTH);
		jPanelPrincipal.add(jTabbedPaneDatos, java.awt.BorderLayout.WEST);


		jPanelMapa.setLayout(new java.awt.BorderLayout());
		jPanelMapa.setBorder(new javax.swing.border.EtchedBorder());

		jPanelContenedorMapa.setLayout(new java.awt.GridLayout(1, 0));


		jPanelMapa.add(jPanelContenedorMapa, java.awt.BorderLayout.CENTER);
        if (CMainContaminantes.geopistaEditor == null) CMainContaminantes.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");
        geopistaEditor= CMainContaminantes.geopistaEditor;
        //layer = ShowMaps.showLayer(com.geopista.app.contaminantes.Constantes.url,geopistaEditor, layerName, permitido, new String[]{"parcelas", "numeros_policia",layerName}, new Color(64, 0, 0));   //"parcelas","base_cartografica",

        /** comprobamos que ya haya sido cargado el mapa para no volver a hacerlo y ocultamos las capas correspondientes */
        Object[] visibles= new Object[1];
        visibles[0]= layerName;
        Object[] novisibles= new Object[2];
        novisibles[0]= JFrameArbolado.getLayername();
        novisibles[1]= JFrameVertedero.getLayername();

        if ((geopistaEditor.getLayerManager().getLayers() == null) || (geopistaEditor.getLayerManager().getLayers().size() == 0))
        {
            if (ShowMaps.showMap(com.geopista.app.contaminantes.Constantes.url,geopistaEditor,
                    JFrameGeneracionPlanos.getMapID(), true,parent,null)==null)
            {
                 new JOptionPane("No existe el mapa "+JFrameGeneracionPlanos.getMapID()+" en el sistema. \nContacte con el administrador."
                            , JOptionPane.ERROR_MESSAGE).createDialog(parent, "ERROR").show();
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
			public void selectionChanged(IAbstractSelection abtractSelection) {
				logger.info("selectionChanged");
				String idSelected = refreshListSelection(layerName);
				if (idSelected != null) {
					initData(idSelected);
				}
			}

			public void featureAdded(FeatureEvent e) {
				//aqui tengo que añadir la feature
				Collection features = e.getFeatures();
				for (Iterator i = features.iterator(); i.hasNext();) {
					Feature f = (Feature) i.next();
					GeopistaFeature geoF = (GeopistaFeature)f;//ShowMaps.saveFeature(f, layer, layerName,com.geopista.app.contaminantes.Constantes.url);
                    boolean idValido=false;
                    try {new Integer(geoF.getSystemId());idValido=true;}catch(Exception ex){}
                    if (geoF.getSystemId()==null || geoF.getSystemId().length()<=0 || !idValido)
                    {
						JOptionPane optionPane = new JOptionPane(messages.getString("JFrameContaminantes.insertError"), JOptionPane.ERROR_MESSAGE);
						JDialog dialog = optionPane.createDialog(parent, "ERROR");
						dialog.show();
                        actividad=null;
						//geopistaEditor.deleteSelectedFeatures();
					} else {
                      	actividad = new Contaminante();
						actividad.setId(geoF.getSystemId());
						try {
							actividad.setId_tipo(((BigDecimal) geoF.getAttribute(1)).toString());
						} catch (Exception ex) {
						}
						;
						try {
							actividad.setId_razon(((BigDecimal) geoF.getAttribute(2)).toString());
						} catch (Exception ex) {
						}
						;
						actividad.setNumeroAdm((String) geoF.getAttribute(3));
						actividad.setAsunto((String) geoF.getAttribute(4));
                        try
                        {
                            new OperacionesContaminantes((com.geopista.app.contaminantes.Constantes.url)).gestionarHistorico(new Historico(new Integer(actividad.getId()).intValue(), com.geopista.app.contaminantes.Constantes.ADD, com.geopista.app.contaminantes.Constantes.CONTAMINANTES));
                        }catch(Exception ex){}

						//  try{actividad.setfInicio((Date)geoF.getAttribute(5));}catch(Exception ex){};
						// try{actividad.setfFin((Date)geoF.getAttribute(6));}catch(Exception ex){};
						/*try {
							actividad.setTipovia(((BigDecimal) geoF.getAttribute(5)).toString());
						} catch (Exception ex) {
						}
						;
						actividad.setNombrevia((String) geoF.getAttribute(6));
						try {
							actividad.setNumerovia(((BigDecimal) geoF.getAttribute(7)).toString());
						} catch (Exception ex) {
						}
						;
						try {
							actividad.setCpostal(((BigDecimal) geoF.getAttribute(8)).toString());
						} catch (Exception ex) {
						}
						;*/
						((GeopistaFeature) f).setSystemId(geoF.getSystemId());
//						f.setAttribute(0, new Integer(geoF.getSystemId()));
						initData(geoF.getSystemId());

						editar();
						refreshFeatureSelection(layerName, geoF.getSystemId());
						
						final TaskMonitorDialog progressDialog = new TaskMonitorDialog(parent, null);
						progressDialog.setTitle(CMainContaminantes.messages.getString("TaskMonitor.Wait"));
                    	progressDialog.report(CMainContaminantes.messages.getString("direccionMasCercana.progressdialog.text"));                        	
                    	final Runnable runnable = new Runnable(){
                    		private Feature f;
                    		GeopistaFeature geoF;

                    		public Runnable ctor(Feature f, GeopistaFeature geoF){
                    			this.f = f;
                    			this.geoF = geoF;
                    			return this;
                    		}

                    		public void run(){
                    			try {
                                    CResultadoOperacion resultado = (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).getDireccionMasCercana(actividad.getId());
                                    if (resultado.getResultado()) {
                                        NumeroPolicia numeroPolicia= (NumeroPolicia)resultado.getVector().elementAt(0);
                                        JOptionPane optionPane = new JOptionPane(CMainContaminantes.messages.getString("direccionMasCercana.encontrada.text")+" "+numeroPolicia.toString(), JOptionPane.INFORMATION_MESSAGE);
        			                    JDialog dialog = optionPane.createDialog(parent, "INFO");
        			                    dialog.show();
                                        actividad.setTipovia(numeroPolicia.getTipovia());
                                        actividad.setNombrevia(numeroPolicia.getNombrevia());
                                        actividad.setNumerovia(numeroPolicia.getRotulo());
                                        (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).saveActividad(actividad);
                                    }
                                    else {
                                    	JOptionPane optionPane = new JOptionPane(CMainContaminantes.messages.getString("direccionMasCercana.noencontrada.text"), JOptionPane.INFORMATION_MESSAGE);
                    					JDialog dialog = optionPane.createDialog(parent, "INFO");
                    					dialog.show();
                                        logger.info("No se ha encontrado la direccion: "+resultado.getDescripcion());
                                    }
                                    
                                    jPanelActividad.load(actividad);
                                }catch(Exception ex){
                                	// No hacemos nada
                                }
                                finally{
                    				progressDialog.setVisible(false);
                    			}
                    		}
                    	}.ctor(f, geoF);
                    	
                    	progressDialog.addComponentListener(new ComponentAdapter(){
                            public void componentShown(ComponentEvent e){
                                new Thread(runnable).start();
                            }
                    	});
                    	
                    	GUIUtil.centreOnWindow(progressDialog);
                    	progressDialog.setVisible(true);  
                    	
//                        try
//                        {
//                            CResultadoOperacion resultado= (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).getDireccionMasCercana(actividad.getId());
//                            if (resultado.getResultado())
//                            {
//                                NumeroPolicia numeroPolicia= (NumeroPolicia)resultado.getVector().elementAt(0);
//                                JOptionPane optionPane = new JOptionPane("La direccion Postal más cercana es: "+numeroPolicia.toString(), JOptionPane.INFORMATION_MESSAGE);
//			                    JDialog dialog = optionPane.createDialog(parent, "INFO");
//			                    dialog.show();
//                                actividad.setTipovia(numeroPolicia.getTipovia());
//                                actividad.setNombrevia(numeroPolicia.getNombrevia());
//                                actividad.setNumerovia(numeroPolicia.getRotulo());
//                                (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).saveActividad(actividad);
//                            }
//                            else
//                            {
//                                logger.info("No se ha encontrado la direccion: "+resultado.getDescripcion());
//                            }
//                        }catch(Exception ex){}
//						jPanelActividad.load(actividad);
					}
				}
				//logger.info("featureAdded");
			}

			public void featureRemoved(FeatureEvent e) {
                //borrar(false);
				//logger.info("featureRemoved");
			}

			public void featureModified(FeatureEvent e) {
			/*	Collection features = e.getFeatures();
				for (Iterator i = features.iterator(); i.hasNext();) {
					Feature f = (Feature) i.next();
					GeopistaFeature geoF = ShowMaps.updateFeature(f, layer, layerName,com.geopista.app.contaminantes.Constantes.url);
					refreshFeatureSelection(layerName, ((GeopistaFeature) f).getSystemId());
				} */
				//logger.info("featureModified");
			}

			public void featureActioned(IAbstractSelection abtractSelection) {
				logger.info("featureActioned");
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
		getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.WEST);
		getContentPane().add(jPanelMapa, java.awt.BorderLayout.CENTER);
		getContentPane().add(jPanelTotal, java.awt.BorderLayout.SOUTH);
		pack();
	}//GEN-END:initComponents

	private void buscarCalle() {
	  	valoresBusqueda.clear();
		CAddressJDialog dialog = new CAddressJDialog(this.parent, true, messages);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();
        valoresBusqueda=dialog.getValoresBusqueda();
    	refreshBusquedaSelection(this,geopistaEditor);

	}


	private void editar() {
        refreshFeatureSelection(layerName, actividad.getId());
		geopistaEditor.zoomToSelected();
		enabled(true);
	}

	private void cancelar() {
		Vector vAnexos = null;
		enabled(false);
		/** como la operacion ha sido cancelada, actualizamos los valores de los estados de inspecciones y anexos */
		if (actividad.getInspecciones() != null) {
			Vector vInspecciones = new Vector();
			for (Enumeration e = actividad.getInspecciones().elements(); e.hasMoreElements();) {
				Inspeccion inspeccion = (Inspeccion) e.nextElement();
				inspeccion.setEstado(-1);
				if (inspeccion.getAnexos() != null) {
					vAnexos = new Vector();
					for (Enumeration e1 = inspeccion.getAnexos().elements(); e1.hasMoreElements();) {
						CAnexo anexo = (CAnexo) e1.nextElement();
						/** si el anexo ha sido annadido, como la operacion ha sido cancelada no lo insertamos */
						if (anexo.getEstado() != CConstantesLicencias.CMD_ANEXO_ADDED) {
							anexo.setEstado(-1);
							vAnexos.addElement(anexo);
						}
					}
				}
				inspeccion.setAnexos(vAnexos);
				vInspecciones.addElement(inspeccion);
			}
			actividad.setInspecciones(vInspecciones);
		}

		jPanelActividad.load(actividad);
		refreshFeatureSelection(layerName, actividad.getId());
	}

	private void borrar(boolean mostrarMensaje) {
		if (actividad == null || actividad.getId() == null) {
            if (mostrarMensaje)
            {
			    JOptionPane optionPane = new JOptionPane(messages.getString("JFrameActividad.mensaje.actividadnoseleccionada"), JOptionPane.INFORMATION_MESSAGE);
			    JDialog dialog = optionPane.createDialog(this, "INFO");
			    dialog.show();
			    return;
            }
		}
		refreshFeatureSelection(layerName, actividad.getId());
        if (mostrarMensaje)
        {
		    int n = JOptionPane.showOptionDialog(this,
				messages.getString("JFrameActividad.mensaje.eliminaractividad") + " " + actividad.getId(),
				"",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
	    	if (n == JOptionPane.NO_OPTION) return;
        }

		CResultadoOperacion result;
		try {
			result = (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).deleteActividad(actividad);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception al eliminar residuo en base de datos: " + sw.toString());
			result = new CResultadoOperacion(false, e.getMessage());
		}
		if (result.getResultado()) {
			ShowMaps.deleteFeature(geopistaEditor);
            try
            {
                new OperacionesContaminantes((com.geopista.app.contaminantes.Constantes.url)).gestionarHistorico(new Historico(new Integer(actividad.getId()).intValue(), com.geopista.app.contaminantes.Constantes.BORRAR, com.geopista.app.contaminantes.Constantes.CONTAMINANTES));
            }catch(Exception ex){}

			actividad = null;
            if (mostrarMensaje)
            {
			    JOptionPane optionPane = new JOptionPane(messages.getString("JFrameActividad.mensaje.actividadeliminado"), JOptionPane.INFORMATION_MESSAGE);
			    JDialog dialog = optionPane.createDialog(this, "");
			    dialog.show();
            }
			enabled(false);
			jPanelActividad.load(actividad);
			;
		} else {
			JOptionPane optionPane = new JOptionPane(result.getDescripcion(), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.show();
		}
	}

	private void salvar() {
		if (!modoEdicion) return;
		try {

			Contaminante auxActividad = jPanelActividad.guardarCambios();
			CResultadoOperacion result = null;
			String sMensaje = "";
			try {
                result = (new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).saveActividad(auxActividad);
				sMensaje = messages.getString("JDialogInspectores.mensaje.inspectoractualizado");

			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				logger.error("Exception al grabar en base de datos un nuevo contacto: " + sw.toString());
				result = new CResultadoOperacion(false, e.getMessage());
			}
			if (result.getResultado()) {
				try {
					auxActividad = (Contaminante) result.getVector().elementAt(0);
				} catch (Exception e) {
					java.io.StringWriter sw = new java.io.StringWriter();
					java.io.PrintWriter pw = new java.io.PrintWriter(sw);
					e.printStackTrace(pw);
					logger.error("Error al coger el objeto inspector: " + sw.toString());
				}

				logger.debug("Identificador del inspector insertado:" + auxActividad.getId());
				actividad = auxActividad;
                try
                {
                    new OperacionesContaminantes((com.geopista.app.contaminantes.Constantes.url)).gestionarHistorico(new Historico(new Integer(actividad.getId()).intValue(), com.geopista.app.contaminantes.Constantes.MODIFICAR, com.geopista.app.contaminantes.Constantes.CONTAMINANTES));
                }catch(Exception ex){}

				jPanelActividad.load(actividad);
				refreshFeatureSelection(layerName, actividad.getId());
				enabled(false);
				Collection cgeo = geopistaEditor.getSelection();
				for (Iterator i = cgeo.iterator(); i.hasNext();) {
					GeopistaFeature gf = (GeopistaFeature) i.next();
					try {
						if (gf.getSystemId().equals(actividad.getId())) {
							gf.setAttribute(1, (Number) new Integer(actividad.getId_tipo()));
							gf.setAttribute(2, (Number) new Integer(actividad.getId_razon()));
							gf.setAttribute(3, actividad.getNumeroAdm());
							gf.setAttribute(4, actividad.getAsunto());
							/*gf.setAttribute(5, (Number) new Integer(actividad.getTipovia()));
							gf.setAttribute(6, actividad.getNombrevia());
							try {
								gf.setAttribute(7, (Number) new Integer(actividad.getNumerovia()));
							} catch (Exception e) {
							}
							try {
								gf.setAttribute(8, (Number) new Integer(actividad.getCpostal()));
							} catch (Exception e) {
							} */
						}
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						logger.error("Exception: " + sw.toString());
					}
				}
			} else {
                /** Comprobamos que no se haya excedido el maximo FileUploadBase.SizeLimitExceededException */
               if (result.getDescripcion().equalsIgnoreCase("FileUploadBase.SizeLimitExceededException")){
                   JOptionPane optionPane= new JOptionPane(messages.getString("AnexosJPanel.Message1"), JOptionPane.ERROR_MESSAGE);
                   JDialog dialog =optionPane.createDialog(this,"ERROR");
                   dialog.show();
               }else{
				JOptionPane optionPane = new JOptionPane("Revise los datos introducidos: " + result.getDescripcion(), JOptionPane.ERROR_MESSAGE);
				//JDialog dialog = optionPane.createDialog(this, "INFO");
				JDialog dialog =optionPane.createDialog(this,"ERROR");
				dialog.show();
               }
			}
		} catch (Exception ex) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Excepcion una actividad contaminante: " + sw.toString());
			JOptionPane optionPane = new JOptionPane(ex, JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.show();
		}

	}

	public void salir() {
		dispose();
	}

	public void enabled(boolean bValor) {
		modoEdicion = bValor;
		jButtonCancel.setEnabled(bValor);
        boolean permitido=true;
        if (CMainContaminantes.acl!=null)
            permitido=CMainContaminantes.acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_CONTAMINANTES));
		jButtonEdit.setEnabled(!bValor && (actividad != null) && permitido);
        jButtonGenerarFicha.setEnabled(!bValor && (actividad != null));
		jButtonBorrar.setEnabled(!bValor && (actividad != null) && permitido);
		jButtonSave.setEnabled(bValor && permitido);
		jPanelActividad.enabled(bValor);
	}
     private void  generarFicha()
     {
         try
         {
             if (actividad==null) return;
             actividad.setEstructuraTipoContaminacion(com.geopista.app.contaminantes.Estructuras.getListaTipo());
             actividad.setEstructuraTipoRazon(com.geopista.app.contaminantes.Estructuras.getListaRazonEstudio());
             actividad.setLocale(com.geopista.app.contaminantes.Constantes.Locale);
		     new GeopistaPrintableContaminantes().printObjeto(FichasDisponibles.fichacontaminante, actividad , Contaminante.class, getGeopistaEditor().getLayerViewPanel(), GeopistaPrintableContaminantes.FICHA_CONTAMINANTE_PLANOS);
         }catch(Exception e)
         {
             logger.error("Excepción al imprimir la ficha: ",e);
         }

     }
	public void changeScreenLang(ResourceBundle messages) {
        try
        {
		    this.messages = messages;
		    setTitle(messages.getString("JFrameContaminantes.title"));//"Actividades contaminantes");
		    jButtonBuscar.setText(messages.getString("JFrameContaminantes.jButtonBuscar"));//Buscar");
        	jButtonEdit.setText(messages.getString("JFrameContaminantes.jButtonEdit"));//Editar");
		    jButtonSave.setText(messages.getString("JFrameContaminantes.jButtonSave"));//Salvar");
		    jButtonBorrar.setText(messages.getString("JFrameContaminantes.jButtonBorrar"));//Borrar");
		    jButtonCancel.setText(messages.getString("JFrameContaminantes.jButtonCancel"));//Cancel");
		    jButtonSalir.setText(messages.getString("JFrameContaminantes.jButtonSalir"));//"Salir");
		    jPanelContenedorMapa.setBorder(new javax.swing.border.TitledBorder(messages.getString("JFrameContaminantes.jPanelContenedorMapa")));//"Mapa"));
		    jTabbedPaneDatos.setTitleAt(0, messages.getString("JFrameContaminantes.jTabbedPaneDatos"));
            jButtonGenerarFicha.setText(messages.getString("JFrameGeneracionPlanos.jButtonGenerarFicha"));//"Fecha Fin:");
		    jPanelActividad.changeScreenLang(messages);

            jButtonCancel.setToolTipText(messages.getString("JFrameContaminantes.jButtonCancel"));
            jButtonBuscar.setToolTipText(messages.getString("JFrameContaminantes.jButtonBuscar"));
            jButtonEdit.setToolTipText(messages.getString("JFrameContaminantes.jButtonEdit"));
            jButtonSalir.setToolTipText(messages.getString("JFrameContaminantes.jButtonSalir"));
            jButtonSave.setToolTipText(messages.getString("JFrameContaminantes.jButtonSave"));
            jButtonBorrar.setToolTipText(messages.getString("JFrameContaminantes.jButtonBorrar"));
            jButtonGenerarFicha.setToolTipText(messages.getString("JFrameGeneracionPlanos.jButtonGenerarFicha"));
        }catch(Exception e)
        {
            logger.error("Error al cargar la etiquetas:",e);
        }
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonCancel;
	private javax.swing.JButton jButtonBuscar;
	private javax.swing.JButton jButtonEdit;
	private javax.swing.JButton jButtonSalir;
	private javax.swing.JButton jButtonSave;
	private javax.swing.JButton jButtonBorrar;

	private JPanelActividad jPanelActividad;
	private javax.swing.JPanel jPanelBotonera;
	private javax.swing.JPanel jPanelContenedorMapa;
	private javax.swing.JPanel jPanelMapa;
	private javax.swing.JPanel jPanelPrincipal;
	private javax.swing.JPanel jPanelTotal;
	private javax.swing.JTabbedPane jTabbedPaneDatos;
    private javax.swing.JButton jButtonGenerarFicha;


	// End of variables declaration//GEN-END:variables

}
