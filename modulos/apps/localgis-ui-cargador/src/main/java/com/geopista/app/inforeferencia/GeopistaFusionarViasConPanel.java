/**
 * GeopistaFusionarViasConPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.analysis.GeopistaFusionViasStandalonePlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * GeopistaFusionarViasPanel Panel que carga el mapa de inforeferencia para
 * fusionar los tramos de via
 */

public class GeopistaFusionarViasConPanel extends JPanel implements WizardPanel
{
    ApplicationContext aplicacion = AppContext.getApplicationContext();

    Blackboard blackBoard = aplicacion.getBlackboard();

    public String myID;

    public String siguiente;    

    private boolean acceso;

    private JLabel lblImagen = new JLabel();

    private JPanel pnlMap = new JPanel();

    private GeopistaEditor geopistaEditor1 = null;

    private JLabel lblOpcion = new JLabel();
    
    private String nextID = "4";

    private WizardContext wizardContext;

    public GeopistaFusionarViasConPanel()
        {

            jbInit();

        }

    private void jbInit()
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
                                    setName(aplicacion
                                            .getI18nString("importar.asistente.callejero.titulo.3"));
                                    setSize(new Dimension(537, 390));
                                    setLayout(null);
                                    setBounds(new Rectangle(10, 10, 750, 600));
                                    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                                    lblImagen.setBorder(BorderFactory.createEmptyBorder(
                                            0, 0, 0, 0));
                                    pnlMap.setBounds(new Rectangle(135, 95, 595, 415));
                                    pnlMap.setBorder(BorderFactory
                                            .createBevelBorder(BevelBorder.LOWERED));
                                    pnlMap.setLayout(null);
                                    geopistaEditor1.setBounds(new Rectangle(40, 30, 135,
                                            130));
                                    lblOpcion.setBounds(new Rectangle(135, 65, 595, 25));
                                    add(lblOpcion, null);
                                    add(lblImagen, null);
                                    pnlMap.add(geopistaEditor1, null);
                                    add(pnlMap, null);
                                } catch (Exception e)
                                {

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

    public void enteredFromLeft(Map dataMap)
    {
        wizardContext.previousEnabled(false);
        lblOpcion.setText(aplicacion.getI18nString("importar.plugin.fusion.etiqueta"));
        GeopistaPermission geopistaPerm = new GeopistaPermission(
                "Geopista.InfReferencia.FusionTramosVias");
        acceso = aplicacion.checkPermission(geopistaPerm, "Informacion de Referencia");
        if (acceso)
        {

            pnlMap.setBounds(new Rectangle(25, 20, 768, 640));
            pnlMap.setLayout(null);

            geopistaEditor1 = (GeopistaEditor) blackBoard
                    .get("geopistaEditorEnlazarPoliciaCalles");
            geopistaEditor1.setSize(new Dimension(512, 512));
            geopistaEditor1.showLayerName(true);

            //GeopistaFusionViasPlugIn geopistaFusionViasPlugIn = new GeopistaFusionViasPlugIn();
            GeopistaFusionViasStandalonePlugIn geopistaFusionViasPlugIn = new GeopistaFusionViasStandalonePlugIn();
            geopistaEditor1.addPlugIn(geopistaFusionViasPlugIn);

            lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
            lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
            lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

            
            geopistaEditor1.setVisible(true);
            geopistaEditor1.setBounds(150, 0, 512, 512);
            geopistaEditor1.setPreferredSize(new Dimension(512, 512));
            pnlMap.add(lblImagen, null);
            pnlMap.add(geopistaEditor1, null);
            this.add(pnlMap, null);
        }
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
        Object listViasINEInsertadas = blackBoard.get("viasINEInsertadas");
        listViasINEInsertadas = null;
        geopistaEditor1 = null;
        
        // TODO Auto-generated method stub
        
    }

    

    
}
