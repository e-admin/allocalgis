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

package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarParcelas02 extends JPanel implements WizardPanel
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

    private static final int DUPLICAR = 2;

    private static final int BORRAR = 0;

    private static final int CANCELAR = 1;

    private WizardContext wizardContext;
    
    private HashMap associationsDomains = null;
    
    private String fieldName;
    
    private String relleno = "00000";


    public GeopistaImportarParcelas02()
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
        setName(aplicacion.getI18nString("importar.usuario.catastro.almacenar"));
        
        jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
        scpErrores.setBounds(new Rectangle(134, 52, 595, 442));
        this.setLayout(null);
        lblDatos.setBounds(new Rectangle(135, 25, 260, 20));

        lblDatos.setText(aplicacion.getI18nString("importar.usuario.paso4.log"));
        lblImagen.setIcon(IconLoader.icon("catastro.png"));
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
        //Falla esta línea wizardContext.previousEnabled(false);
        geopistaEditor3 = (GeopistaEditor) blackImportar.get("editorGeo");
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
                                    
                                    boolean firingEvents = false;
                                    SchemaValidator validator = new SchemaValidator(null);
                                    boolean manualModification = ((Boolean) blackImportar.get("mostrarError")).booleanValue();
                                    String cadena = "";
                                    totalInsertados = 0;
                                    totalNoInsertados = 0;
                                    cadenaTexto = "";
                                    

                                    capaSistema = (GeopistaLayer) geopistaEditor3.getLayerManager().getLayer((String) blackImportar.get("nombreTabla"));
                                    GeopistaSchema featureSchema = (GeopistaSchema) capaSistema.getFeatureCollectionWrapper().getFeatureSchema();

                                    int selectedValue = 0;
                                    if (capaSistema.getFeatureCollectionWrapper().getFeatures().size() != 0)
                                    {
                                        //Object[] possibleValues = { aplicacion.getI18nString("GeopistaImportacionLog.Duplicar"), aplicacion.getI18nString("GeopistaImportacionLog.Borrar"), aplicacion.getI18nString("GeopistaImportacionLog.Cancelar") };
                                        Object[] possibleValues = { aplicacion.getI18nString("GeopistaImportacionLog.Actualizar"), aplicacion.getI18nString("GeopistaImportacionLog.Cancelar") };                                        selectedValue = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
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

                                    
                                    GeopistaLayer sourceImportLayer = (GeopistaLayer) blackImportar.get("sourceImportLayer");

                                    FeatureSchema esquemaTemp = sourceImportLayer.getFeatureCollectionWrapper().getFeatureSchema();
                                   
                                    final String refCatastralAttName = featureSchema.getAttributeByColumn("referencia_catastral");
                                    
                                    capaSistema.setActiva(false);
                                    capaSistema.setVisible(true);
                                    Comparator geopistaFeatureComparator = new Comparator(){
                                        public int compare(Object o1, Object o2) {
                                			Feature f1 = (Feature) o1;
                                			Feature f2 = (Feature) o2;
                                			
                                			String f1ReferenciaCatastral = f1.getString(refCatastralAttName);
                                			String f2ReferenciaCatastral = f2.getString(refCatastralAttName);
                                			
                                			return f1ReferenciaCatastral.compareToIgnoreCase(f2ReferenciaCatastral);
                                		}
                                    };
                                    

                                    List listaLayer = sourceImportLayer.getFeatureCollectionWrapper().getFeatures();
                                    List featureSystemList = capaSistema.getFeatureCollectionWrapper().getFeatures();
                                    
                                    setSearchableList(featureSystemList, geopistaFeatureComparator);
                                    
                                    Iterator itLayer = listaLayer.iterator();

                                    geopistaValidatePlugIn.setMakeInsertion(false);
                                    
                                    
                                    
                                    while (itLayer.hasNext())
                                    {
                                        progressDialog.report(totalInsertados + totalNoInsertados, listaLayer.size(), aplicacion.getI18nString("ImportandoEntidad"));
                                        try
                                        {
                                            Feature f = (Feature) itLayer.next();
                                            String delegacion = String.valueOf(((Integer)f.getAttribute("DELEGACIO")).intValue());
                                            String municipio = String.valueOf(((Integer)f.getAttribute("MUNICIPIO")).intValue());
                                            
                                            municipio = relleno.substring(0,3-municipio.length())+municipio; 
                                            
                                            String masa = (String)f.getAttribute("MASA");
                                            if(masa.length()>3) masa = masa.substring(masa.length()-3);
                                            String parcela	 = (String)f.getAttribute("PARCELA");
                                            if(parcela.length()<5) parcela= relleno.substring(0,5-parcela.length())+parcela;
                                            String refCatastral = delegacion + municipio + "A" + masa + parcela;
                                            
                                            
                                            /*
                                             * Busca la referencia catastral entre las existentes ya en el mapa
                                             */
                                            
                                            
                                            Comparator featureComparator = new Comparator(){
                                                public int compare(Object o1, Object o2) {
                                        			Feature f1 = (Feature) o1;
                                        			Feature f2 = (Feature) o2;
                                        			                              			
                                        			String f1ReferenciaCatastral = f1.getString(refCatastralAttName);
                                        			String delegacion = String.valueOf(((Integer)f2.getAttribute("DELEGACIO")).intValue());
                                                    String municipio = String.valueOf(((Integer)f2.getAttribute("MUNICIPIO")).intValue());
                                                    
                                                    municipio = relleno.substring(0,3-municipio.length())+municipio; 
                                                    
                                                    String masa = (String)f2.getAttribute("MASA");
                                                    if(masa.length()>3) masa = masa.substring(masa.length()-3);
                                                    String parcela	 = (String)f2.getAttribute("PARCELA");
                                                    if(parcela.length()<5) parcela= relleno.substring(0,5-parcela.length())+parcela;
                                                    String f2ReferenciaCatastral = delegacion + municipio + "A" + masa + parcela;
                                                    
                                        			return f1ReferenciaCatastral.compareToIgnoreCase(f2ReferenciaCatastral);
                                        		}
                                            };
                                            
                                            boolean makeInsertion = false;
                                            Feature featureToAdd = searchRefCatastral(f, featureComparator);
                                            
                                            // Si NO la ha encontrado actualizar los valores y dejar el ID.
                                            if (featureToAdd == null)
                                            {             
                                                featureToAdd = new BasicFeature(featureSchema);
                                                featureToAdd.setAttribute(refCatastralAttName, refCatastral);
                                                makeInsertion=true;
                                            }
                                            
                                            featureToAdd.setGeometry(f.getGeometry());// geometria
                                            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_via"), getTrimString(f,"VIA"));
                                            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipo"), getTrimString(f,"TIPO"));
                                            DateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
                                            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("fecha_alta"), formatter1.parse(f.getString("FECHAALTA")));
                                            
                                            if(!f.getString("FECHABAJA").trim().equals("99999999")){
                                                featureToAdd.setAttribute(featureSchema.getAttributeByColumn("fecha_baja"), formatter1.parse(f.getString("FECHABAJA")));  
                                            }
                                            
                                           //featureToAdd.setAttribute(featureSchema.getAttributeByColumn("fecha_baja"), getTrimString(f,"FECHABAJA"));
                                            
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
                                            
                                            if (makeInsertion&&validateResult)
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
                                    Object lastResfreshValue = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
                                    try
                                    {
                                        driverProperties.put(Constantes.REFRESH_INSERT_FEATURES, new Boolean(false));

                                        geopistaServerDataSource.getConnection().executeUpdate(capaSistema.getDataSourceQuery().getQuery(), capaSistema.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
                                    } finally
                                    {
                                        if (lastResfreshValue != null)
                                        {
                                            driverProperties.put(Constantes.REFRESH_INSERT_FEATURES, lastResfreshValue);
                                        } else
                                        {
                                            driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
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
                            
                            /**
                             * genera la cache para realizar las busquedas de catastro
                             * @see GeopistaMostrarParcelas.searchRefCatastral
                             */
							private Feature[] cache;
                            private void setSearchableList(List listaLayerParcelas,Comparator comparator)
                            {
                            cache = new Feature[listaLayerParcelas.size()];
                            
                            int i=0;
                            for (Iterator features = listaLayerParcelas.iterator(); features
									.hasNext();)
								{
								Feature feature = (Feature) features.next();
								cache[i++]=feature;
								}
                            Arrays.sort(cache,comparator);
                            }
							/**
							 * utiliza una cache para realizar la busqueda por identificador catastral
							 * @param featureSchema
							 * @param listaLayerParcelas
							 * @param refCat
							 * @return
							 */
                            
                            private Feature searchRefCatastral(Feature refCatFeature, Comparator comparator)
							{
							        int featureIndex = Arrays.binarySearch(cache, refCatFeature,comparator);
							        if(featureIndex<0) return null;
							        
							        return  cache[featureIndex];
							}
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);

        finalMessage((String) blackImportar.get("nombreTabla"));

        
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
        wizardContext = wd;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    
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

    public void exiting()
    {
        geopistaEditor3.reset();
        geopistaEditor3 = null;
        
        // TODO Auto-generated method stub
        
    }

}
