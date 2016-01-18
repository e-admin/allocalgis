/**
 * GeopistaImportarParcelasMapa.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

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

public class GeopistaImportarParcelasMapa extends JPanel implements WizardPanel
{

    private GeopistaLayer capaSistema = null;

    private JPanel pnlVentana = new JPanel();

    private String cadena;

    private String tipoInf;

    private JButton btnDetener = new JButton();

    private JOptionPane opCuadroDialogo;

    private JLabel lblDatos = new JLabel();

    private GeopistaLayer layer5 = null;

    public String titulo;

    private JLabel lblImagen = new JLabel();

    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    private String rutaMapa;

    public int entidad = 0;

    public int id_elemento = 0;

    public String id_elemento_St;

    private JSeparator jSeparator4 = new JSeparator();

    private JSeparator jSeparator5 = new JSeparator();

    private GeopistaEditor geopistaEditor2 = null;

    public String nombreCampo;

    public String nombreCapa;

    private String nombreMapa;

    private String cadenaTexto;

    public String dominio;

    public String rutaFic;

    private Blackboard blackImportar = aplicacion.getBlackboard();

    public GeopistaImportarParcelasMapa()
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

    private void jbInit() throws Exception
    {
        
    }

    public void enteredFromLeft(Map dataMap)
    {
        setName(aplicacion.getI18nString("importar.ambitos.planeamiento.titulo.3"));
        geopistaEditor2 = (GeopistaEditor) blackImportar.get("editorGeo");
        
        this.setLayout(null);
        lblImagen.setIcon(IconLoader.icon("catastro.png"));
        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        geopistaEditor2.setBounds(new Rectangle(135, 20, 595, 295));
        geopistaEditor2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setSize(750, 600);
        geopistaEditor2.setBounds(new java.awt.Rectangle(134, 52, 595, 442));
        geopistaEditor2.showLayerName(true);
        geopistaEditor2.addCursorTool("Zoom In/Out",
                "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
        geopistaEditor2.addCursorTool("Pan",
                "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
        geopistaEditor2.addCursorTool("Measure",
                "com.vividsolutions.jump.workbench.ui.cursortool.MeasureTool");

        lblDatos.setBounds(new Rectangle(135, 25, 260, 20));
        nombreMapa = aplicacion.getI18nString("importar.usuario.mapa.catastro");

        lblDatos.setText(nombreMapa);
        jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
        this.add(jSeparator4, null);
        this.add(lblDatos, null);
        jSeparator5.setBounds(new Rectangle(135, 505, 605, 2));
        this.add(jSeparator5, null);
        this.add(geopistaEditor2, null);
        this.add(lblImagen, null);

        this.add(btnDetener, null);
        
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
        return "2";
    }

    public String getInstructions()
    {
        return "";
    }

    public boolean isInputValid()
    {
        return true;
    }

    private String nextID = "3";

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

    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

}
