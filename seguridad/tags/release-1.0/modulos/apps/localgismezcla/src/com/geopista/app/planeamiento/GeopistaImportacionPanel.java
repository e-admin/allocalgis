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

package com.geopista.app.planeamiento;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.Timer;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportacionPanel extends JPanel implements WizardPanel
{

    private GeopistaLayer capaSistema = null;

    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    private Blackboard blackImportar = aplicacion.getBlackboard();

    private GeopistaLayer sourceImportaLayer = null;

    private JPanel pnlVentana = new JPanel();

    private JButton btnDetener = new JButton();

    private JOptionPane opCuadroDialogo;

    public static Timer timer;

    // public static GeopistaLongTask task;
    private int a = 0;

    public String titulo;

    private JLabel lblImagen = new JLabel();

    private JLabel lblDatos = new JLabel();

    private GeopistaEditor geopistaEditor3 = new GeopistaEditor(aplicacion
            .getString("url.herramientas.gestoreventos"));

    private JSeparator jSeparator4 = new JSeparator();

    private JSeparator jSeparator5 = new JSeparator();
    
    private WizardContext wizardContext = null;

    public GeopistaImportacionPanel()
        {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {

                    // Wait for the dialog to appear before starting the
                    // task. Otherwise
                    // the task might possibly finish before the dialog
                    // appeared and the
                    // dialog would never close. [Jon Aquino]
                    new Thread(new Runnable()
                        {
                            public void run()
                            {

						            try
						            {
						                jbInit();
						            } catch (Exception e)
						            {
						                e.printStackTrace();
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

    private void jbInit() throws Exception
    {
        setName(aplicacion
                .getI18nString("importar.ambitos.planeamiento.titulo.3"));

        String tipoImp = (String) blackImportar.get("tipoImport");

        try
        {
            String helpHS = "ayuda.hs";
            HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
            HelpBroker hb = hs.createHelpBroker();
            if (tipoImp.equals("municipal"))
            {
                hb.enableHelpKey(this, "planeamientoImportarAmbitosPlaneamiento03", hs);
            } else
            {
                hb.enableHelpKey(this, "planeamientoImportarAmbitosGestion03", hs);
            }

        } catch (Exception excp)
        {
        }
        jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
        this.setLayout(null);
        lblDatos.setBounds(new Rectangle(135, 25, 260, 20));

        lblDatos.setText(aplicacion.getI18nString("importar.usuario.paso3.mapa"));
        lblImagen.setIcon(IconLoader.icon("planeamiento.png"));
        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        geopistaEditor3.setBounds(new java.awt.Rectangle(134, 52, 595, 442));
        geopistaEditor3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ediInfo.setContentType("text/html");
        this.setSize(750, 600);
        geopistaEditor3.showLayerName(true);
        geopistaEditor3.addCursorTool("Zoom In/Out",
                "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
        geopistaEditor3.addCursorTool("Pan",
                "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
        geopistaEditor3.addCursorTool("Measure",
                "com.vividsolutions.jump.workbench.ui.cursortool.MeasureTool");
        jSeparator5.setBounds(new Rectangle(135, 505, 605, 2));
        this.add(geopistaEditor3, null);
        this.add(lblDatos, null);
        this.add(lblImagen, null);
        this.add(jSeparator4, null);
        this.add(jSeparator5, null);
    }

    public void enteredFromLeft(Map dataMap)
    {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), geopistaEditor3.getContext().getErrorHandler());

        progressDialog.setTitle(aplicacion.getI18nString("GeopistaImportacionPanel.CargandoMapaPlaneamiento"));

        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {

                    // Wait for the dialog to appear before starting the
                    // task. Otherwise
                    // the task might possibly finish before the dialog
                    // appeared and the
                    // dialog would never close. [Jon Aquino]
                    new Thread(new Runnable()
                        {
                            public void run()
                            {
                                progressDialog.report(aplicacion.getI18nString("GeopistaImportacionPanel.CargandoMapaPlaneamiento"));
                                geopistaEditor3.getLayerManager().remove(
                                        sourceImportaLayer);
                                int i = 0;
                                String ruta = (String) blackImportar
                                        .get("rutaFicheroImportar");
                                String cadena = "";
                                try
                                {
                                    if(capaSistema==null)
                                    {
                                        try
                                        {
                                            geopistaEditor3.loadMap(aplicacion
	                                            .getString("url.mapa.planeamiento"));
                                        }catch(Exception e)
                                        {
                                            JOptionPane
                                            .showMessageDialog(
                                                    aplicacion
                                                            .getMainFrame(),
                                                    aplicacion
                                                            .getI18nString("errorCargaMapa"));
                                    throw e;
                                        }
	                                    blackImportar.put("editorPlaneamiento",
	                                            geopistaEditor3);
	                                    try
	                                    {
	                                    capaSistema = (GeopistaLayer) geopistaEditor3
	                                            .getLayerManager().getLayer(
	                                                    (String) blackImportar
	                                                            .get("nombreTablaSelec"));
	                                    }catch(Exception e)
	                                    {
	                                        JOptionPane
                                            .showMessageDialog(
                                                    aplicacion
                                                            .getMainFrame(),
                                                    aplicacion
                                                            .getI18nString("errorCargaCapaImportacion"));
                                    throw e;
	                                        
	                                    }
                                    }
                                    capaSistema.setActiva(false);
                                    capaSistema.setVisible(true);
                                    
                                    sourceImportaLayer = (GeopistaLayer) geopistaEditor3
                                            .loadData(ruta, "Planeamiento");
                                    sourceImportaLayer.setSystemId("sourceImportLayer");
                                    sourceImportaLayer.setActiva(false);
                                    sourceImportaLayer.setVisible(true);

                                    ediInfo.setText(cadena);

                                } catch (Exception exc)
                                {
                                    exc.printStackTrace();
                                    JOptionPane.showMessageDialog(aplicacion
                                            .getMainFrame(), aplicacion
                                            .getI18nString("SeHanProducidoErrores"));
                                    wizardContext.cancelWizard();
                                    return;
                                } finally
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

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {

    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
    public void add(InputChangedListener listener)
    {

    }

    public void remove(InputChangedListener listener)
    {

    }

    public String getTitle()
    {
        return this.getName();
    }

    public String getID()
    {
        return "3";
    }

    public String getInstructions()
    {
        return "";
    }

    public boolean isInputValid()
    {
        return true;
    }

    private String nextID = "4";

    private JEditorPane ediInfo = new JEditorPane();

    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {
        return nextID;
    }

    public void setWizardContext(WizardContext wd)
    {
        this.wizardContext = wd;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

}
