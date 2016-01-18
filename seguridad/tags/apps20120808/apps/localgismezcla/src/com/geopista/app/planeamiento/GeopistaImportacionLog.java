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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportacionLog extends JPanel implements WizardPanel
{
    private AbstractValidator validator = new SchemaValidator(null);

    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    private Blackboard blackImportar = aplicacion.getBlackboard();

    private JScrollPane scpErrores = new JScrollPane();

    private JEditorPane ediError = new JEditorPane();

    private GeopistaLayer sourceImportLayer = null;

    private JPanel pnlVentana = new JPanel();

    private JLabel lblImportar = new JLabel();

    private GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();

    private GeopistaLayer capaSistema = null;

    private JLabel lblImagen = new JLabel();

    private GeopistaEditor geopistaEditor3 = null;

    private JSeparator jSeparator4 = new JSeparator();

    private JSeparator jSeparator5 = new JSeparator();

    private JLabel lblDatos = new JLabel();

    private boolean validaFeature;

    private int totalInsertados = 0;

    private int totalNoInsertados = 0;

    private String cadenaTexto;

    private Collection totalFeaturesToInsert = null;

    private static final int DUPLICAR = 0;

    private static final int BORRAR = 1;

    private static final int CANCELAR = 2;

    private WizardContext wizardContext;
    
    private HashMap associationsDomains = null;
    
    private String fieldName;

    public GeopistaImportacionLog()
        {
            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

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

        setName(aplicacion.getI18nString("importar.ambitos.planeamiento.titulo.4"));
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
        scpErrores.setBounds(new Rectangle(134, 52, 595, 442));
        this.setLayout(null);
        lblDatos.setBounds(new Rectangle(135, 25, 260, 20));

        lblDatos.setText(aplicacion.getI18nString("importar.usuario.paso4.log"));
        lblImagen.setIcon(IconLoader.icon("planeamiento.png"));
        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        ediError.setContentType("text/html");
        this.setSize(750, 600);

        jSeparator5.setBounds(new Rectangle(135, 505, 605, 2));
        scpErrores.getViewport().add(ediError, null);
        this.add(scpErrores, null);
        this.add(lblDatos, null);
        this.add(lblImagen, null);
        this.add(jSeparator4, null);
        this.add(jSeparator5, null);
    }

    public void enteredFromLeft(Map dataMap)
    {
        wizardContext.previousEnabled(false);
        geopistaEditor3 = (GeopistaEditor) blackImportar.get("editorPlaneamiento");
        geopistaEditor3.addPlugIn(geopistaValidatePlugIn);
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), geopistaEditor3.getContext().getErrorHandler());

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
                                boolean isMakeInsertion = geopistaValidatePlugIn.isMakeInsertion();
                                try
                                {
                                    
                                    associationsDomains = (HashMap) blackImportar.get("AssociationsDomains");
                                    blackImportar.put("AssociationsDomains",null);
                                    fieldName = (String) blackImportar.get("fieldName");
                                            
                                    boolean firingEvents = false;
                                    SchemaValidator validator = new SchemaValidator(null);
                                    boolean manualModification = ((Boolean) blackImportar.get("mostrarError")).booleanValue();
                                    String cadena = "";
                                    totalInsertados = 0;
                                    totalNoInsertados = 0;
                                    cadenaTexto = "";
                                    

                                    capaSistema = (GeopistaLayer) geopistaEditor3.getLayerManager().getLayer((String) blackImportar.get("nombreTablaSelec"));
                                    GeopistaSchema featureSchema = (GeopistaSchema) capaSistema.getFeatureCollectionWrapper().getFeatureSchema();

                                    int selectedValue = 0;
                                    if (capaSistema.getFeatureCollectionWrapper().getFeatures().size() != 0)
                                    {
                                        Object[] possibleValues = { aplicacion.getI18nString("GeopistaImportacionLog.Duplicar"), aplicacion.getI18nString("GeopistaImportacionLog.Borrar"), aplicacion.getI18nString("GeopistaImportacionLog.Cancelar") };
                                        selectedValue = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
                                                possibleValues, possibleValues[0]);

                                    }

                                    if (selectedValue == CANCELAR)
                                    {
                                        wizardContext.cancelWizard();
                                        return;
                                    }

                                    GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capaSistema.getDataSourceQuery().getDataSource();
                                    firingEvents = capaSistema.getLayerManager().isFiringEvents();
                                    capaSistema.getLayerManager().setFiringEvents(false);

                                    if (selectedValue == BORRAR)
                                    {
                                        progressDialog.report(aplicacion.getI18nString("GeopistaImportarToponimosPanel.BorrandoRegistros"));
                                        Iterator lastFeaturesIter = capaSistema.getFeatureCollectionWrapper().getFeatures().iterator();
                                        while (lastFeaturesIter.hasNext())
                                        {
                                            GeopistaFeature deleteCurrentFeature = (GeopistaFeature) lastFeaturesIter.next();
                                            deleteCurrentFeature.setDeleted(true);
                                        }

                                        geopistaServerDataSource.getConnection().executeUpdate(capaSistema.getDataSourceQuery().getQuery(), capaSistema.getFeatureCollectionWrapper().getUltimateWrappee(), null);
                                        capaSistema.getFeatureCollectionWrapper().removeAll(capaSistema.getFeatureCollectionWrapper().getFeatures());

                                    }
                                    GeopistaLayer sourceImportLayer = (GeopistaLayer) geopistaEditor3.getLayerManager().getLayer("sourceImportLayer");

                                    FeatureSchema esquemaTemp = sourceImportLayer.getFeatureCollectionWrapper().getFeatureSchema();

                                    capaSistema.setActiva(false);
                                    capaSistema.setVisible(true);

                                    List listaLayer = sourceImportLayer.getFeatureCollectionWrapper().getFeatures();
                                    Iterator itLayer = listaLayer.iterator();

                                    geopistaValidatePlugIn.setMakeInsertion(false);
                                    String nombreTablaSelec = (String) blackImportar.get("nombreTablaSelec");
                                    String tipoFichero = (String) blackImportar.get("tipoF");
                                    String identificadorPlan = null;
                                    if (blackImportar.get("identificadorPlan") != null)
                                    {
                                        identificadorPlan = blackImportar.get("identificadorPlan").toString().trim();
                                    }
                                    while (itLayer.hasNext())
                                    {
                                        progressDialog.report(totalInsertados + totalNoInsertados, listaLayer.size(), aplicacion.getI18nString("ImportandoEntidad"));
                                        try
                                        {
                                            Feature f = (Feature) itLayer.next();

                                            GeopistaFeature featureToAdd = new GeopistaFeature(featureSchema);
                                            featureToAdd.setGeometry(f.getGeometry());// geometria

                                            if (nombreTablaSelec.equals("ambitos_gestion"))
                                            {
                                                featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idplan"), Integer.valueOf(identificadorPlan));// idplan
                                                if (tipoFichero.equals("shp"))
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"DESCRIPCIO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idtipget"), getTrimString(f,"IDTIPGET"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idtramit"), getTrimString(f,"IDTRAMIT"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"NUMERO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("ejecutado"), getTrimString(f,"EJECUTADO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("nombre"), getTrimString(f,"NOMBRE"));
                                                } else
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"Descripcion"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idtipget"), getTrimString(f,"IDtipget"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idtramit"), getTrimString(f,"IDTramit"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"Numero"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("ejecutado"), getTrimString(f,"Ejecutado"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("nombre"), getTrimString(f,"Nombre"));
                                                }
                                            }
                                            if (nombreTablaSelec.equals("ambitos_planeamiento"))
                                            {

                                                featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idplan"), Integer.valueOf(identificadorPlan));// idplan
                                                if (tipoFichero.equals("shp"))
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idambplanetipo"), getTrimString(f,"IDAMBPLAN"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("indice_edificabilidad"), f.getAttribute("INDICEEDIF"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("edificabilidad_m2"), f.getAttribute("EDIFICABIL"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("asumido"), getTrimString(f,"ASUMID"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"DESCRIPCIO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idtramit"), getTrimString(f,"IDTRAMIT"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idusoglo"), getTrimString(f,"IDUSOGLO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idclasif"), Integer.valueOf(getTrimString(f,"IDCLASIF")));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"NUMERO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("nombre"), getTrimString(f,"NOMBRE"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("normativa"), getTrimString(f,"NORMATIVA"));
                                                } else
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idambplanetipo"), getTrimString(f,"IDambplanetipo"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("indice_edificabilidad"), f.getAttribute("Indice_edificabilidad"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("edificabilidad_m2"), f.getAttribute("Edificabilidad_m2"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("asumido"), getTrimString(f,"Asumido"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"Descripcion"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idtramit"), getTrimString(f,"IDTramit"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idusoglo"), getTrimString(f,"IDusoglo"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idclasif"), Integer.valueOf(getTrimString(f,"IDClasif")));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"Numero"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("nombre"), getTrimString(f,"Nombre"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("normativa"), getTrimString(f,"Normativa"));
                                                }
                                            }
                                            if (nombreTablaSelec.equals("calificacion_suelo"))
                                            {
                                                featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idplan"), Integer.valueOf(identificadorPlan));// idplan
                                                if (tipoFichero.equals("shp"))
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("ordenanza"), getTrimString(f,"IDORDENANZ"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("plantaspermit"), getTrimString(f,"PLANTASPER"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("patiointerior"), f.getAttribute("PATIOINT"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("aticopermisible"), getTrimString(f,"ATICOPERMI"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("normativageneral"), getTrimString(f,"NORMATIVA"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"DESCRIPCIO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("edif"), f.getAttribute("EDIF"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("edif_m2"), f.getAttribute("EDIF_M2"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("ocupacion"), f.getAttribute("OCUPACION"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("alt_max"), f.getAttribute("ALT_MAX"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("fondomax"), f.getAttribute("FONDOMAX"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("retranq"), f.getAttribute("RETRANQ"));
                                                } else
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("ordenanza"), getTrimString(f,"Ordenanza"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("plantaspermit"), getTrimString(f,"Plantaspermit"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("patiointerior"), f.getAttribute("PatioInterior"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("aticopermisible"), getTrimString(f,"AticoPermisible"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("normativageneral"), getTrimString(f,"NormativaGeneral"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"Descripcion"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("edif"), f.getAttribute("Edif"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("edif_m2"), f.getAttribute("Edif_m2"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("ocupacion"), f.getAttribute("Ocupacion"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("alt_max"), f.getAttribute("Alt_max"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("fondomax"), f.getAttribute("Fondomax"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("retranq"), f.getAttribute("Retranq"));
                                                }
                                            }

                                            if (nombreTablaSelec.equals("clasificacion_suelo"))
                                            {

                                                featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idplan"), Integer.valueOf(identificadorPlan));// idplan
                                                if (tipoFichero.equals("shp"))
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idclasiftipo"), getTrimString(f,"IDCLASIF"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idusoglo"), getTrimString(f,"IDUSOGLO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("categoria"), getTrimString(f,"CATEGORIA"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"NUMERO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("normativa"), getTrimString(f,"NORMATIVA"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"DESCRIPCIO"));
                                                } else
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idclasiftipo"), getTrimString(f,"IDClasifTipo"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idusoglo"), getTrimString(f,"Idusoglo"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("categoria"), getTrimString(f,"Categoria"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"Numero"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("normativa"), getTrimString(f,"Normativa"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"Descripcio"));
                                                }

                                            }
                                            if (nombreTablaSelec.equals("tabla_planeamiento"))
                                            {
                                                if (tipoFichero.equals("shp"))
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("Fechaprobacion"), (Date)f.getAttribute("FECHAAPROB"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"DESCRIPCIO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoplan"), getTrimString(f,"TIPOPLAN"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("nombre"), getTrimString(f,"NOMBRE"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"NUMERO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("Fechaalta"), (Date)f.getAttribute("FECHAALTA"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("Fechabaja"), (Date)f.getAttribute("FECHABAJA"));
                                                } else
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("Fechaprobacion"), (Date)f.getAttribute("FechaAprobacion"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"Descripcion"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoplan"), getTrimString(f,"Tipoplan"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("nombre"), getTrimString(f,"Nombre"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"Numero"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("Fechaalta"), (Date)f.getAttribute("FechaAlta"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("Fechabaja"), (Date)f.getAttribute("FechaBaja"));
                                                }
                                            }

                                            if (nombreTablaSelec.equals("sistemas_generales"))
                                            {
                                                featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idplan"), Integer.valueOf(identificadorPlan));// idplan
                                                if (tipoFichero.equals("jml"))
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idusoglo"), getTrimString(f,"IDUSOGLO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"NUMERO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("adscripcion"), getTrimString(f,"ADSCRIPCION"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("edificabilidad"), f.getAttribute("EDIFICABILIDAD"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("nombre"), getTrimString(f,"NOMBRE"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("normativa"), getTrimString(f,"NORMATIVA"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"DESCRIPCION"));
                                                } else
                                                {
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("idusoglo"), getTrimString(f,"IDUSOGLO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("numero"), getTrimString(f,"NUMERO"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("adscripcion"), getTrimString(f,"ADSCRIP"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("edificabilidad"), f.getAttribute("EDIFICABIL"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("nombre"), getTrimString(f,"NOMBRE"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("normativa"), getTrimString(f,"NORMATIVA"));
                                                    featureToAdd.setAttribute(featureSchema.getAttributeByColumn("descripcion"), getTrimString(f,"DESCRIPCIO"));
                                                }
                                            }

                                            ((GeopistaFeature) featureToAdd).setLayer(capaSistema);

                                            boolean validateResult = false;
                                            boolean cancelImport = false;

                                            while (!(validateResult = validator.validateFeature(featureToAdd)))
                                            {
                                                if (!manualModification)
                                                {
                                                    break;
                                                }
                                                FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(capaSistema, featureToAdd);
                                                if (featureDialog.wasOKPressed())
                                                {
                                                    Feature clonefeature = featureDialog.getModifiedFeature();
                                                    featureToAdd.setAttributes(clonefeature.getAttributes());
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
                                                totalInsertados++;
                                            } else
                                            {
                                                totalNoInsertados++;
                                            }
                                            
                                            if(cancelImport==true) break;
                                            
                                            if (validateResult)
                                            {
                                                capaSistema
                                                        .getFeatureCollectionWrapper()
                                                        .add(featureToAdd);
                                            }

                                        } catch (Exception e)
                                        {
                                            totalNoInsertados = totalNoInsertados + 1;
                                            e.printStackTrace();
                                        }

                                    }// end while

                                    progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
                                    Map driverProperties = geopistaServerDataSource.getProperties();
                                    Object lastResfreshValue = driverProperties.get(GeopistaConnection.REFRESH_INSERT_FEATURES);
                                    try
                                    {
                                        driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES, new Boolean(false));

                                        geopistaServerDataSource.getConnection().executeUpdate(capaSistema.getDataSourceQuery().getQuery(), capaSistema.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
                                    } finally
                                    {
                                        if (lastResfreshValue != null)
                                        {
                                            driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES, lastResfreshValue);
                                        } else
                                        {
                                            driverProperties.remove(GeopistaConnection.REFRESH_INSERT_FEATURES);
                                        }
                                    }

                                } catch (Exception exc)
                                {
                                    exc.printStackTrace();
                                    JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("SeHanProducidoErrores"));
                                    wizardContext.cancelWizard();
                                    return;
                                } finally
                                {
                                    progressDialog.setVisible(false);
                                    geopistaValidatePlugIn.setMakeInsertion(isMakeInsertion);
                                    capaSistema.getLayerManager().setFiringEvents(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);

        finalMessage((String) blackImportar.get("nombreTablaSelec"));
    }

    private void finalMessage(String modulo)
    {
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
        String date = (String) formatter.format(new Date());
        cadenaTexto = "<p><b>" + aplicacion.getI18nString(modulo) + "</b></p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("GeopistaImportacionLog.fecha") + ":</b>" + date + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.usuario.paso4.Registros") + ":</b>" + (totalInsertados+totalNoInsertados) + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.usuario.paso4.Insertados") + ":</b>" + totalInsertados + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.usuario.paso4.NoInsertados") + ":</b>" + totalNoInsertados + "</p>";
        ediError.setText(cadenaTexto);
        ediError.setVisible(true);
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
        return "4";
    }

    public String getInstructions()
    {
        return "";
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
    
    private String getTrimString(Feature feature, String attributeName)
    {
         String trimValueString = feature.getString(attributeName).trim();
        
        
        if(associationsDomains!=null)
        {
            if(attributeName.equals(fieldName))
            {
                String newDomainValue = (String) associationsDomains.get(trimValueString);
                if(newDomainValue!=null)
                {
                    return newDomainValue;
                }
            }
        }
        
        return trimValueString;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

}
