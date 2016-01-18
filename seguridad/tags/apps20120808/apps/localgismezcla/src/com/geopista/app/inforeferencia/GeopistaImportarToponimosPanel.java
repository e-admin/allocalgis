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

package com.geopista.app.inforeferencia;

import com.geopista.app.AppContext;

import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;

import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;

import java.util.List;
import javax.swing.border.BevelBorder;
import java.awt.Color;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;

import javax.swing.JEditorPane;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JProgressBar;

/**
 * GeopistaImportarToponimosPanel
 * 
 * Esta clase se encarga de realizar la importación de un fichero de topónimos
 * al mapa. Dicho fichero debe ser un fichero shapefile de puntos.
 * 
 */
public class GeopistaImportarToponimosPanel extends JPanel implements WizardPanel

{
    private int numeroRegistrosLeidos = 1;

    private String cadenaTexto = "";

    private JLabel lblImagen = new JLabel();

    private GeopistaEditor geopistaEditor1 = new GeopistaEditor();

    private JScrollPane jScrollPane2 = new JScrollPane();

    private javax.swing.JLabel jLabel = null;

    private GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();

    private JEditorPane ediError = new JEditorPane();

    private GeopistaLayer importLayer;

    private GeopistaLayer capaToponimos = null;

    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    private Blackboard blackboard = aplicacion.getBlackboard();

    private JEditorPane jedResumen = new JEditorPane();

    private static final int DUPLICAR = 0;

    private static final int BORRAR = 1;

    private static final int CANCELAR = 2;

    private int registrosErroneos = 0;

    private int registrosCorrectos = 0;

    private WizardContext wizardContext;

    public GeopistaImportarToponimosPanel()

        {
            try
            {
                jbInit();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    private void jbInit() throws Exception
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
                                    setLayout(null);
                                    cadenaTexto = "";
                                    setName(aplicacion
                                            .getI18nString("importar.asistente.toponimos.titulo.2"));
                                    lblImagen.setIcon(IconLoader
                                            .icon("inf_referencia.png"));
                                    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                                    lblImagen.setBorder(BorderFactory.createLineBorder(
                                            Color.black, 1));
                                    // Colocamos el panel de resumen de
                                    // importación

                                    jScrollPane2.setBounds(new java.awt.Rectangle(127,
                                            415, 600, 94));
                                    geopistaEditor1.setBounds(128, 23, 591, 384);
                                    jScrollPane2.getViewport().add(jedResumen, null);

                                    add(geopistaEditor1, null);
                                    add(lblImagen, null);
                                    add(jScrollPane2, null);
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

    }// jbinit

    public void enteredFromLeft(Map dataMap)
    {
        wizardContext.previousEnabled(false);
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), geopistaEditor1.getContext().getErrorHandler());

        progressDialog.setTitle(aplicacion.getI18nString("ImportandoDatos"));

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

                                boolean lastValue = geopistaValidatePlugIn
                                        .isMakeInsertion();
                                boolean manualModification = ((Boolean) blackboard
                                        .get("mostrarError")).booleanValue();
                                boolean firingEvents = false;
                                try
                                {
                                    numeroRegistrosLeidos = 1;
                                    registrosCorrectos = 0;
                                    registrosErroneos = 0;
                                    progressDialog.report(aplicacion
                                            .getI18nString("CargandoMapaToponimos"));
                                    // Leemos el mapa
                                    // Recoger todas las features del layer08
                                    // auxiliar
                                    // Pasarlas a la capa de toponimos
                                    jedResumen.setContentType("text/html");
                                    jedResumen.setEditable(false);

                                    if (capaToponimos == null)
                                    {
                                        try
                                        {
                                            geopistaEditor1.loadMap(aplicacion
                                                    .getString("url.mapa.toponimos"));
                                        } catch (Exception e)
                                        {
                                            JOptionPane.showMessageDialog(aplicacion
                                                    .getMainFrame(), aplicacion
                                                    .getI18nString("errorCargaMapa"));
                                            throw e;
                                        }
                                        geopistaEditor1
                                                .addCursorTool("Zoom In/Out",
                                                        "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
                                        geopistaEditor1
                                                .addCursorTool("Pan",
                                                        "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
                                        ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
                                        geopistaEditor1.addPlugIn(zoomToFullExtentPlugIn);
                                        geopistaEditor1.addPlugIn(geopistaValidatePlugIn);
                                        geopistaEditor1.showLayerName(true);

                                        // seleccionamos la capa de parcelas.
                                        capaToponimos = (GeopistaLayer) geopistaEditor1
                                                .getLayerManager().getLayer("toponimos");
                                        capaToponimos.setActiva(true);
                                        capaToponimos.setVisible(true);
                                    }

                                    importLayer = (GeopistaLayer) geopistaEditor1
                                            .loadData((String) blackboard
                                                    .get("importarToponimos"), "");

                                    importLayer.setActiva(false);
                                    importLayer.addStyle(new BasicStyle(new Color(64, 64,
                                            64)));
                                    importLayer.setVisible(true);

                                    int selectedValue = 0;
                                    if (capaToponimos.getFeatureCollectionWrapper()
                                            .getFeatures().size() != 0)
                                    {
                                        Object[] possibleValues = {
                                                aplicacion
                                                        .getI18nString("GeopistaImportarToponimosPanel.Duplicar"),
                                                aplicacion
                                                        .getI18nString("GeopistaImportarToponimosPanel.Borrar"),
                                                aplicacion
                                                        .getI18nString("GeopistaImportarToponimosPanel.Cancelar") };
                                        selectedValue = JOptionPane
                                                .showOptionDialog(
                                                        aplicacion.getMainFrame(),
                                                        aplicacion
                                                                .getI18nString("GeopistaImportarToponimosPanel.LaCapaContieneFeatures"),
                                                        aplicacion
                                                                .getI18nString("GeopistaImportarToponimosPanel.BorrarDuplicar"),
                                                        0, JOptionPane.QUESTION_MESSAGE,
                                                        null, possibleValues,
                                                        possibleValues[0]);

                                    }
                                    GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capaToponimos
                                            .getDataSourceQuery().getDataSource();

                                    if (selectedValue != CANCELAR)
                                    {
                                        Iterator itLayer = importLayer
                                                .getFeatureCollectionWrapper()
                                                .getFeatures().iterator();

                                        boolean lastInsertionState = geopistaValidatePlugIn
                                                .isMakeInsertion();
                                        geopistaValidatePlugIn.setMakeInsertion(false);

                                        firingEvents = capaToponimos.getLayerManager()
                                                .isFiringEvents();
                                        capaToponimos.getLayerManager().setFiringEvents(
                                                false);

                                        if (selectedValue == BORRAR)
                                        {
                                            progressDialog
                                                    .report(aplicacion
                                                            .getI18nString("GeopistaImportarToponimosPanel.BorrandoRegistros"));
                                            Iterator lastFeaturesIter = capaToponimos
                                                    .getFeatureCollectionWrapper()
                                                    .getFeatures().iterator();
                                            while (lastFeaturesIter.hasNext())
                                            {
                                                GeopistaFeature deleteCurrentFeature = (GeopistaFeature) lastFeaturesIter
                                                        .next();
                                                deleteCurrentFeature.setDeleted(true);
                                            }

                                            geopistaServerDataSource
                                                    .getConnection()
                                                    .executeUpdate(
                                                            capaToponimos
                                                                    .getDataSourceQuery()
                                                                    .getQuery(),
                                                            capaToponimos
                                                                    .getFeatureCollectionWrapper()
                                                                    .getUltimateWrappee(),
                                                            progressDialog);
                                            capaToponimos
                                                    .getFeatureCollectionWrapper()
                                                    .removeAll(
                                                            capaToponimos
                                                                    .getFeatureCollectionWrapper()
                                                                    .getFeatures());

                                        }

                                        GeopistaSchema featureSchema = (GeopistaSchema) capaToponimos
                                                .getFeatureCollectionWrapper()
                                                .getFeatureSchema();

                                        String schemaToponimo = featureSchema
                                                .getAttributeByColumn("toponimo");
                                        String schemaTipo = featureSchema
                                                .getAttributeByColumn("tipo");
                                        String schemaFuente = featureSchema
                                                .getAttributeByColumn("fuente");
                                        String schemaSubtipo = featureSchema
                                                .getAttributeByColumn("subtipo");
                                        String schemaIdioma = featureSchema
                                                .getAttributeByColumn("idioma");

                                        while (itLayer.hasNext())
                                        {
                                            progressDialog
                                                    .report(
                                                            numeroRegistrosLeidos,
                                                            importLayer
                                                                    .getFeatureCollectionWrapper()
                                                                    .getFeatures().size(),
                                                            aplicacion
                                                                    .getI18nString("ImportandoEntidad"));
                                            Feature f = (Feature) itLayer.next();

                                            GeopistaFeature currentFeature = new GeopistaFeature(
                                                    featureSchema);

                                            currentFeature.setGeometry(f.getGeometry());

                                            currentFeature.setAttribute(schemaToponimo, f
                                                    .getString("TOPONIMO"));
                                            currentFeature.setAttribute(schemaTipo,
                                                    f.getAttribute("TIPO"));
                                            currentFeature.setAttribute(schemaFuente, f
                                                    .getString("FUENTE"));
                                            currentFeature.setAttribute(schemaSubtipo,
                                                   f.getAttribute("SUBTIPO"));
                                            currentFeature.setAttribute(schemaIdioma, f
                                                    .getString("IDIOMA"));

                                            ((GeopistaFeature) currentFeature)
                                                    .setLayer(capaToponimos);

                                            boolean validateResult = false;
                                            boolean cancelImport = false;

                                            SchemaValidator validator = new SchemaValidator(
                                                    null);

                                            while (!(validateResult = validator
                                                    .validateFeature(currentFeature)))
                                            {
                                                if (!manualModification)
                                                {
                                                    break;
                                                }
                                                FeatureDialog featureDialog = GeopistaUtil
                                                        .showFeatureDialog(capaToponimos,
                                                                currentFeature);
                                                if (featureDialog.wasOKPressed())
                                                {
                                                    Feature clonefeature = featureDialog
                                                            .getModifiedFeature();
                                                    currentFeature
                                                            .setAttributes(clonefeature
                                                                    .getAttributes());
                                                } else
                                                {
                                                    Object[] possibleValues = { aplicacion.getI18nString("CancelarEsteElemento"), aplicacion.getI18nString("CancelarTodaLaImportacion"), aplicacion.getI18nString("IgnorarFuturosErrores") };
                                                    int selectedValueCancel = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
                                                            possibleValues, possibleValues[0]);
                                                    if(selectedValueCancel==2) manualModification=false;
                                                    if(selectedValueCancel==1) cancelImport=true;
                                                    break;
                                                }

                                            }

                                            if (validateResult)
                                            {
                                                registrosCorrectos++;
                                            } else
                                            {
                                                registrosErroneos++;
                                            }
                                            if(cancelImport==true) break;
                                            /**
                                             * Fin de la validación manual
                                             */
                                            if (validateResult)
                                            {
                                                capaToponimos
                                                        .getFeatureCollectionWrapper()
                                                        .add(currentFeature);
                                            }
                                            numeroRegistrosLeidos++;
                                        }

                                        geopistaValidatePlugIn
                                                .setMakeInsertion(lastInsertionState);
                                        progressDialog.report(aplicacion
                                                .getI18nString("GrabandoDatosBaseDatos"));

                                        Map driverProperties = geopistaServerDataSource
                                                .getProperties();
                                        Object lastResfreshValue = driverProperties
                                                .get(GeopistaConnection.REFRESH_INSERT_FEATURES);
                                        try
                                        {
                                            driverProperties
                                                    .put(
                                                            GeopistaConnection.REFRESH_INSERT_FEATURES,
                                                            new Boolean(false));
                                            geopistaServerDataSource
                                                    .getConnection()
                                                    .executeUpdate(
                                                            capaToponimos
                                                                    .getDataSourceQuery()
                                                                    .getQuery(),
                                                            capaToponimos
                                                                    .getFeatureCollectionWrapper()
                                                                    .getUltimateWrappee(),
                                                            progressDialog);
                                        } finally
                                        {
                                            if (lastResfreshValue != null)
                                            {
                                                driverProperties
                                                        .put(
                                                                GeopistaConnection.REFRESH_INSERT_FEATURES,
                                                                lastResfreshValue);
                                            } else
                                            {
                                                driverProperties
                                                        .remove(GeopistaConnection.REFRESH_INSERT_FEATURES);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        wizardContext.cancelWizard();
                                        return;
                                    }
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(aplicacion
                                            .getMainFrame(), aplicacion
                                            .getI18nString("SeHanProducidoErrores"));
                                    wizardContext.cancelWizard();
                                    return;
                                } finally
                                {
                                    try
                                    {
                                        geopistaValidatePlugIn
                                                .setMakeInsertion(lastValue);
                                        capaToponimos.getLayerManager().setFiringEvents(
                                                firingEvents);
                                    } catch (Exception e)
                                    {
                                    }
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);

        // ponemos en el panel los valores de registros leidos y fecha y Hora
        String fechaFinalizacion = "";
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
        String date = (String) formatter.format(new Date());
        fechaFinalizacion = date;
        //
        cadenaTexto = "";
        cadenaTexto = cadenaTexto
                + aplicacion.getI18nString("importar.progreso.numero.leidos")
                + (numeroRegistrosLeidos - 1)
                + aplicacion.getI18nString("importar.progreso.numero.insertados")
                + registrosCorrectos
                + aplicacion.getI18nString("importar.progreso.numero.no.insertados")
                + registrosErroneos
                + aplicacion.getI18nString("importar.progreso.fecha.fin")
                + fechaFinalizacion;

        jedResumen.setText(cadenaTexto);

    }

    public void exitingToRight() throws Exception
    {
        geopistaEditor1 = null;
        capaToponimos = null;
        importLayer = null;
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
        return " ";
    }

    public boolean isInputValid()
    {
        return true;
    }

    private String nextID = null;

    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }

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
