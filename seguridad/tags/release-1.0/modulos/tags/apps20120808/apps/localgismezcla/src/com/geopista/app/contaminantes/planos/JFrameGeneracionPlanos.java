
package com.geopista.app.contaminantes.planos;
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

import com.geopista.app.printer.GeopistaPrintable;
import com.geopista.app.printer.FichasDisponibles;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.app.contaminantes.actividades.JDialogBuscarContaminantes;
import com.geopista.app.contaminantes.actividades.JFrameContaminantes;
import com.geopista.app.contaminantes.vertederos.JDialogBuscarVertedero;
import com.geopista.app.contaminantes.vertederos.JFrameVertedero;
import com.geopista.app.contaminantes.arbolado.JDialogBuscarArbolado;
import com.geopista.app.contaminantes.arbolado.JFrameArbolado;
import com.geopista.app.contaminantes.CMainContaminantes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.contaminantes.Vertedero;
import com.geopista.protocol.contaminantes.OperacionesContaminantes;
import com.geopista.protocol.contaminantes.Contaminante;
import com.geopista.protocol.contaminantes.Arbolado;
import com.geopista.server.administradorCartografia.CancelException;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.util.StringUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.lang.reflect.Method;


/**
 * @author angeles
 */
public class JFrameGeneracionPlanos extends javax.swing.JInternalFrame {
  	Logger logger = Logger.getLogger(JFrameGeneracionPlanos.class);
    private ResourceBundle messages;
	private JFrame padre;

    private static final int idMap = 241;
    private static final String layerNameActividades=JFrameContaminantes.getLayername();
    private static final String layerNameVertederos=JFrameVertedero.getLayername();
    private static final String layerNameArboleda=JFrameArbolado.getLayername();
    private GeopistaLayer layerActividades=null;
    private GeopistaLayer layerArboleda=null;
    private GeopistaLayer layerVertederos=null;
    private Contaminante actividad= null;
    private Vertedero vertedero= null;
    private Arbolado zona_arbolada= null;



	public JFrameGeneracionPlanos(final ResourceBundle messages, JFrame frame) {
		this.padre = frame;
		this.messages = messages;
        initComponents();
        /** dialogo de espera de carga */
        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(padre, null);
        progressDialog.setTitle(messages.getString("Licencias.Tag1"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){
                        /* añadimos el documento a la lista */
                        try{
                            progressDialog.report(messages.getString("Licencias.Tag1"));
                    		configureComponets();
                        }catch(Exception e){
                            logger.error("Error ", e);
                            ErrorDialog.show(padre, "ERROR", "ERROR", StringUtil.stackTrace(e));
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

        changeScreenLang(messages);
		//CUtilidadesComponentes.showGeopistaEditor(editorMapaJPanel, geopistaEditor, true);
        pruebaLeer();
	}
     public static int getMapID() {
        return idMap;
    }
	private boolean configureComponets () throws CancelException{
        if (CMainContaminantes.geopistaEditor == null) CMainContaminantes.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");
        geopistaEditor= CMainContaminantes.geopistaEditor;
        /** comprobamos que ya haya sido cargado el mapa para no volver a hacerlo y visualizamos TODAS las capas correspondientes */
        if ((geopistaEditor.getLayerManager().getLayers() != null) && (geopistaEditor.getLayerManager().getLayers().size() > 0)){
            layerActividades=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer(layerNameActividades);
            layerVertederos=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer(layerNameVertederos);
            layerArboleda=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer(layerNameArboleda);
            ShowMaps.allLayersVisible(geopistaEditor);
            geopistaEditor.getSelectionManager().clear();
            try{geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();}catch(Exception e){}
            jPanelMapa.add(geopistaEditor);
        }else{
            if (ShowMaps.showMap(com.geopista.app.contaminantes.init.Constantes.url,geopistaEditor, idMap, false,padre,null)!=null){
                layerActividades=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer(layerNameActividades);
                layerVertederos=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer(layerNameVertederos);
                layerArboleda=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer(layerNameArboleda);
                ShowMaps.allLayersVisible(geopistaEditor);
                jPanelMapa.add(geopistaEditor);
            }else{
                new JOptionPane("No existe el "+idMap+" en el sistema. \nContacte con el administrador."
                        , JOptionPane.ERROR_MESSAGE).createDialog(padre, "ERROR").show();
            }
        }

        try
        {
            ClassLoader cl =this.getClass().getClassLoader();
            ImageIcon iconoZoom = new javax.swing.ImageIcon(cl.getResource("img/zoom.gif"));
            jButtonBuscarExpediente.setIcon(iconoZoom);
        }catch(Exception ex)
        {
            logger.error("No se encuentra el icono:",ex);

        }

		return true;
	}


	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroupOpciones = new javax.swing.ButtonGroup();
        templateJScrollPane = new javax.swing.JScrollPane();
        jPanelPrincipal = new javax.swing.JPanel();
        jPanelBotonera = new javax.swing.JPanel();
        salirJButton = new javax.swing.JButton();
        jButtonEncuadrar = new javax.swing.JButton();
        jButtonGenerarFicha = new javax.swing.JButton();
        jPanelMapa = new javax.swing.JPanel();
        generarJButton = new javax.swing.JButton();
        jPanelOpciones = new javax.swing.JPanel();
        jRadioButtonContaminantes = new javax.swing.JRadioButton();
        jRadioButtonVertederos = new javax.swing.JRadioButton();
        jRadioButtonArboledas = new javax.swing.JRadioButton();
        jButtonBuscarExpediente = new javax.swing.JButton();
        jTextFieldExpediente= new javax.swing.JTextField();
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));
        setClosable(true);

        jPanelPrincipal.setLayout(new BorderLayout());


        jPanelBotonera.setBorder(new javax.swing.border.EtchedBorder());
        salirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salir();
            }
        });

        
        jButtonEncuadrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seleccionarElemento();
            }
        });

        jButtonGenerarFicha.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        generarFicha();
                    }
         });
        
        generarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generar();
            }
        });

        jButtonEncuadrar.setEnabled(false);
        jButtonGenerarFicha.setEnabled(false);
        jPanelBotonera.add(jButtonEncuadrar);
        jPanelBotonera.add(jButtonGenerarFicha);
        jPanelBotonera.add(generarJButton);
        jPanelBotonera.add(salirJButton);
        jPanelPrincipal.add(jPanelBotonera, BorderLayout.SOUTH);
        jPanelMapa.setLayout(new java.awt.GridLayout(1, 0));
        jPanelPrincipal.add(jPanelMapa, BorderLayout.CENTER);
        

        jPanelOpciones.setPreferredSize(new Dimension(250,250));
        
        jPanelOpciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelOpciones.add(jRadioButtonContaminantes,new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));
        buttonGroupOpciones.add(jRadioButtonContaminantes);
        jRadioButtonContaminantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualizarElementos();
            }
        });

        jPanelOpciones.add(jRadioButtonVertederos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));
        buttonGroupOpciones.add(jRadioButtonVertederos);
        jRadioButtonVertederos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualizarElementos();
            }
        });

        jPanelOpciones.add(jRadioButtonArboledas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));
        buttonGroupOpciones.add(jRadioButtonArboledas);
        jRadioButtonArboledas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualizarElementos();
            }
        });
        jPanelOpciones.add(jButtonBuscarExpediente, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        jTextFieldExpediente.setEditable(false);
        jPanelOpciones.add(jTextFieldExpediente, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 190, -1));

        jButtonBuscarExpediente.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonBuscarExpediente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscar();
            }
        });
        jPanelPrincipal.add(jPanelOpciones, BorderLayout.WEST);
        templateJScrollPane.setViewportView(jPanelPrincipal);
        getContentPane().add(templateJScrollPane);
    }



    private void generar() {
        try
        {
            String plantilla=null;
            if (jRadioButtonContaminantes.isSelected()) {
				plantilla= FichasDisponibles.planocontaminante;
			} else if (jRadioButtonVertederos.isSelected()) {
                plantilla= FichasDisponibles.planovertedero;
			} else if (jRadioButtonArboledas.isSelected()) {
                plantilla= FichasDisponibles.planoarboleda;
			}
            if (plantilla!=null)
                new GeopistaPrintable().printObjeto(plantilla, null , null, geopistaEditor.getLayerViewPanel(), GeopistaPrintable.FICHA_CONTAMINANTE_PLANOS);
            else
            {
                //mostrar mensaje de que debe seleccionar
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

	}
    private void generarFicha() {

		try {
            seleccionarElemento();
			if (jRadioButtonContaminantes.isSelected() && actividad != null) {
                actividad.setEstructuraTipoContaminacion(com.geopista.app.contaminantes.estructuras.Estructuras.getListaTipo());
                actividad.setEstructuraTipoRazon(com.geopista.app.contaminantes.estructuras.Estructuras.getListaRazonEstudio());
                actividad.setLocale(com.geopista.app.contaminantes.init.Constantes.Locale);
				new GeopistaPrintable().printObjeto(FichasDisponibles.fichacontaminante, actividad , Contaminante.class, geopistaEditor.getLayerViewPanel(), GeopistaPrintable.FICHA_CONTAMINANTE_PLANOS);
			} else if (jRadioButtonVertederos.isSelected() && vertedero != null) {
                new GeopistaPrintable().printObjeto(FichasDisponibles.fichavertedero, vertedero , Vertedero.class, geopistaEditor.getLayerViewPanel(), GeopistaPrintable.FICHA_CONTAMINANTE_PLANOS);
   			} else if (jRadioButtonArboledas.isSelected() && zona_arbolada!=null) {
                new GeopistaPrintable().printObjeto(FichasDisponibles.fichaarbolado, zona_arbolada , Arbolado.class, geopistaEditor.getLayerViewPanel(), GeopistaPrintable.FICHA_CONTAMINANTE_PLANOS);
			}
		} catch (Exception ex) {
			logger.error("Exception al mostrar las features: " ,ex);
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

	private void seleccionarElemento() {

		try {
			if (jRadioButtonContaminantes.isSelected() && actividad!=null) {
				ShowMaps.refreshFeatureSelection(geopistaEditor,layerActividades,actividad.getId());
                jTextFieldExpediente.setText("ID: "+ actividad.getId());
			} else if (jRadioButtonVertederos.isSelected() && vertedero!=null) {
                ShowMaps.refreshFeatureSelection(geopistaEditor,layerVertederos,vertedero.getId());
                jTextFieldExpediente.setText("ID: "+vertedero.getId());
			} else if (jRadioButtonArboledas.isSelected() && zona_arbolada!=null) {
                ShowMaps.refreshFeatureSelection(geopistaEditor,layerArboleda,zona_arbolada.getId());
                jTextFieldExpediente.setText("ID: "+zona_arbolada.getId());
			}
		} catch (Exception ex) {
			logger.error("Exception al mostrar las features: " ,ex);
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }


	private boolean refreshFeatureSelection(GeopistaLayer layer) {
		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			geopistaEditor.getSelectionManager().clear();
			logger.info("geopistaLayer: " + layer);
			for (Iterator i=layer.getFeatureCollectionWrapper().getFeatures().iterator();i.hasNext();) {
					Feature feature = (Feature) i.next();
					geopistaEditor.select(layer, feature);
			}
			geopistaEditor.zoomToSelected();
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return true;
		} catch (Exception ex) {

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			logger.error("Exception al mostrar las features: " ,ex);
			return false;
		}
	}


	public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje);
	}

	private void salir() {
		dispose();
	}
    private void buscar()
    {
        if (jRadioButtonContaminantes.isSelected())
       	    buscarContaminantes();
        else if (jRadioButtonVertederos.isSelected())
            buscarVertedero();
        else if (jRadioButtonArboledas.isSelected())
           buscarArboleda();
        if (actividad!=null || vertedero!=null || zona_arbolada!=null)
        {
            jButtonEncuadrar.setEnabled(true);
            jButtonGenerarFicha.setEnabled(true);
        }
    }
    private void visualizarElementos()
    {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                actividad=null;
                vertedero=null;
                zona_arbolada=null;
                jTextFieldExpediente.setText("");
                jButtonEncuadrar.setEnabled(false);
                jButtonGenerarFicha.setEnabled(false);
                geopistaEditor.getSelectionManager().clear();
                if (jRadioButtonContaminantes.isSelected()) {
                    refreshFeatureSelection(layerActividades);
                    layerActividades.setActiva(true);
                    layerActividades.setVisible(true);
                    layerVertederos.setActiva(false);
                    layerVertederos.setVisible(false);
                    layerArboleda.setActiva(false);
                    layerArboleda.setVisible(false);
                } else if (jRadioButtonVertederos.isSelected()) {
                    refreshFeatureSelection(layerVertederos);
                    layerActividades.setActiva(false);
                    layerActividades.setVisible(false);
                    layerVertederos.setActiva(true);
                    layerVertederos.setVisible(true);
                    layerArboleda.setActiva(false);
                    layerArboleda.setVisible(false);
                } else if (jRadioButtonArboledas.isSelected()) {
                    refreshFeatureSelection(layerArboleda);
                    layerActividades.setActiva(false);
                    layerActividades.setVisible(false);
                    layerVertederos.setActiva(false);
                    layerVertederos.setVisible(false);
                    layerArboleda.setActiva(true);
                    layerArboleda.setVisible(true);
                }
                geopistaEditor.zoomToSelected();
            } catch (Exception ex) {
                logger.error("Exception al mostrar las features: " ,ex);
            }

            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void buscarVertedero()
    {
            // Aqui añado lo de buscar vertedero
            JDialogBuscarVertedero dialogBuscar = new JDialogBuscarVertedero(padre, true, messages);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            dialogBuscar.setSize(525, 500);
            dialogBuscar.setLocation(d.width / 2 - dialogBuscar.getSize().width / 2, d.height / 2 - dialogBuscar.getSize().height / 2);
            dialogBuscar.setResizable(false);
            dialogBuscar.show();
            if (dialogBuscar.isOkSelected())
            {
                 vertedero=dialogBuscar.getVertederoSelected();
                 seleccionarElemento();
            }
            dialogBuscar = null;
    }
    private void buscarArboleda()
    {
            // Aqui añado lo de buscar vertedero
            JDialogBuscarArbolado dialogBuscar = new JDialogBuscarArbolado(padre, true, messages);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            dialogBuscar.setSize(525, 500);
            dialogBuscar.setLocation(d.width / 2 - dialogBuscar.getSize().width / 2, d.height / 2 - dialogBuscar.getSize().height / 2);
            dialogBuscar.setResizable(false);
            dialogBuscar.show();
            if (dialogBuscar.isOkSelected())
            {
                 zona_arbolada=dialogBuscar.getArboladoSelected();
                 seleccionarElemento();
            }
            dialogBuscar = null;
    }

    private void buscarContaminantes()
    {
            // Aqui añado lo de buscar actividades contaminantes
            JDialogBuscarContaminantes dialogBuscar = new JDialogBuscarContaminantes(padre, true, messages,padre);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            dialogBuscar.setSize(525, 500);
            dialogBuscar.setLocation(d.width / 2 - dialogBuscar.getSize().width / 2, d.height / 2 - dialogBuscar.getSize().height / 2);
            dialogBuscar.setResizable(false);
            dialogBuscar.show();
            dialogBuscar = null;
    }
    public void initDataActividad(String idActividad) {
        padre.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            actividad = (new OperacionesContaminantes(com.geopista.app.contaminantes.init.Constantes.url)).getActividad(idActividad);
            if (actividad == null) {
                logger.warn("No existen la actividad en el sistema");
                return;
            }
            seleccionarElemento();
        } catch (Exception e) {
            logger.error("Error al buscar la actividad: " + e.toString());
            JOptionPane optionPane = new JOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog(padre, "ERROR al inicializar la actividad");
            dialog.show();
        }
        padre.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }

    public void changeScreenLang(ResourceBundle messages) {

            try {
                setTitle(messages.getString("JFrameGeneracionPlanos.title"));//"Generación de planos");
                jPanelOpciones.setBorder(new javax.swing.border.TitledBorder(messages.getString("JFrameGeneracionPlanos.jPanelOpciones")));//("Tipo de plano"));
                jPanelMapa.setBorder(new javax.swing.border.TitledBorder(messages.getString("jMenuItemArboladoInformes.jPanelContenedorMapa")));
                jRadioButtonContaminantes.setText(messages.getString("CMainContaminantes.jMenuActividades"));
                jRadioButtonVertederos.setText(messages.getString("CMainContaminantes.jMenuVertederos"));
                jRadioButtonArboledas.setText(messages.getString("jMenuItemArboladoInformes.mensajeTittle1"));
                jButtonBuscarExpediente.setText(messages.getString("JFrameVertedero.jButtonBuscar"));
                jButtonEncuadrar.setText(messages.getString("JFrameGeneracionPlanos.jButtonEncuadrar"));//Encuadrar"));
                jButtonGenerarFicha.setText(messages.getString("JFrameGeneracionPlanos.jButtonGenerarFicha"));//Generar ficha"));
                salirJButton.setText(messages.getString("JFrameGeneracionPlanos.jButtonSalir"));
                generarJButton.setText(messages.getString("JFrameGeneracionPlanos.jButtonGenerar"));//Generar plano"));

                jButtonBuscarExpediente.setToolTipText(messages.getString("JFrameVertedero.jButtonBuscar"));
                jButtonEncuadrar.setToolTipText(messages.getString("JFrameGeneracionPlanos.jButtonEncuadrar"));
                jButtonGenerarFicha.setToolTipText(messages.getString("JFrameGeneracionPlanos.jButtonGenerarFicha"));
                salirJButton.setToolTipText(messages.getString("JFrameGeneracionPlanos.jButtonSalir"));
                generarJButton.setToolTipText(messages.getString("JFrameGeneracionPlanos.jButtonGenerar"));
            } catch (Exception ex) {

                logger.error("Error al cargar las etiquetas: ",ex);
            }
        }

    private void pruebaLeer()
    {
        Vertedero vertedero = new Vertedero();
        vertedero.setTipo("Hola");
        vertedero.setCapacidad(new Long(12));
        //vertedero.getTipo();
        //vertedero.getCapacidad();
        try
        {
            Class[] argumentTypes = {  };
            Class thisClass = Vertedero.class;
            Method aMethod =thisClass.getMethod("getTipo",argumentTypes);
            Object[] arguments = { };
            Object resultado=aMethod.invoke(vertedero, arguments);
            System.out.println("Resultado1: "+resultado.toString());

            Method aMethod2 =thisClass.getMethod("getCapacidad",argumentTypes);
            Object resultado2=aMethod2.invoke(vertedero, arguments);
            System.out.println("Resultado2: "+resultado2.toString());

        }
        catch (java.lang.NoSuchMethodException e1)
        {
            e1.printStackTrace();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }


	private GeopistaEditor geopistaEditor;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelBotonera;
    private javax.swing.JButton jButtonBuscarExpediente;
    private javax.swing.ButtonGroup buttonGroupOpciones;
    private javax.swing.JPanel jPanelMapa;
    private javax.swing.JRadioButton jRadioButtonContaminantes;
    private javax.swing.JTextField jTextFieldExpediente;
    private javax.swing.JRadioButton jRadioButtonVertederos;
    private javax.swing.JRadioButton jRadioButtonArboledas;
    private javax.swing.JButton generarJButton;
    private javax.swing.JPanel jPanelOpciones;
    private javax.swing.JButton jButtonEncuadrar;
    private javax.swing.JButton jButtonGenerarFicha;
    private javax.swing.JButton salirJButton;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JScrollPane templateJScrollPane;

    // End of variables declaration//GEN-END:variables



}
