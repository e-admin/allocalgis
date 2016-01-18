package com.geopista.app.acteconomicas;

import com.geopista.ui.wizard.WizardPanel;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.plugin.zoom.ZoomToFullExtentPlugIn;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.dialogs.DocumentDialog;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.SchemaValidator;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.io.WKTReader;



import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.*;

import java.util.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 05-jun-2006
 * Time: 10:27:52
 */
public class MostrarActividadesEconomicas extends JPanel implements  WizardPanel{
    /**
     * Logger for this class
     */
    Logger	logger	= Logger.getLogger(MostrarActividadesEconomicas.class);
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = aplicacion.getBlackboard();
    private JScrollPane scpErrores = new JScrollPane();
    private JLabel lblImagen = new JLabel();
    private JLabel lblTipoFichero = new JLabel();
    private JComboBox cmbTipoInfo = new JComboBox();
    private GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();
    private GeopistaEditor geopistaEditor = null;
    //private GeopistaLayer capaParcelasFichero = null;
    private GeopistaLayer capaActEconomicas = null;

    private static final int CANCELAR = 1;

    private JScrollPane jScrollPane2 = new JScrollPane();
    private JEditorPane jedResumen = new JEditorPane();
    private int numeroRegistrosLeidos = 1;
    private String cadenaTexto = "";

    private int registrosErroneos = 0;
    private int registrosCorrectos = 0;
    private WizardContext wizardContext;

    public MostrarActividadesEconomicas()
    {
    	//System.out.println("Mostrando Actividades Economicas");
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {

                    new Thread(new Runnable()
                        {
                            public void run()
                            {
						            try
						            {
						                setName(aplicacion.getI18nString("importar.asistente.parcelas.titulo.2"));
						                //System.out.println("Creando Editor");
						                geopistaEditor = new GeopistaEditor("workbench-properties-simple.xml");
						                jbInit();
						            } catch (Exception e)
						            {
						                e.printStackTrace();
						            }
						            finally
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


    public void add(InputChangedListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void remove(InputChangedListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getTitle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getID() {
        return "3" ;
    }

    public String getInstructions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isInputValid() {
        return true;  
    }

    public void setWizardContext(WizardContext wd) {
        wizardContext=wd;
    }

    public String getNextID() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setNextID(String nextID) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void exiting() {

    	//System.out.println("MostrarActividadesEconomicas.exiting");
        if(geopistaEditor!=null)
        {
            geopistaEditor.destroy();
            geopistaEditor.removeAll();
        }
        else{
        	//System.out.println("Geopista Editor es null");
        }
        geopistaEditor=null;
        logger	= null;
        aplicacion = null;
        if (blackboard!=null) blackboard.put("listaActividades",null);
        blackboard = null;
        if (scpErrores!=null) scpErrores.removeAll();
        scpErrores=null;
        lblImagen = null;
        lblTipoFichero = null;
        cmbTipoInfo = null;
        geopistaValidatePlugIn = null;
        capaActEconomicas = null;
        if (jScrollPane2!=null) jScrollPane2.removeAll();
        jScrollPane2= null;
        jedResumen = null;
        cadenaTexto = null;
        wizardContext =null;
        removeAll();
              //To change body of implemented methods use File | Settings | File Templates.
    }              //private ArrayList totalInsertFeatures = null;
    private void jbInit() throws Exception
    {
        this.setLayout(null);

        scpErrores.setBounds(new java.awt.Rectangle(130,50,600,323));
        scpErrores.getViewport().setLayout(null);

        cmbTipoInfo.addItem(aplicacion.getI18nString("importar.informe.acteconomicas"));

        lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        lblTipoFichero.setText(aplicacion.getI18nString("importar.informacion.referencia.tipo.fichero"));
        lblTipoFichero.setBounds(new Rectangle(136, 22, 140, 20));

        cmbTipoInfo.setBackground(new Color(255, 255, 255));
        cmbTipoInfo.setBounds(new Rectangle(283, 20, 290, 20));
        geopistaEditor.setBounds(new Rectangle(130,50,600,323));

        jScrollPane2.setBounds(new java.awt.Rectangle(131,379,600,132));

        geopistaEditor.addCursorTool("Zoom In/Out",
                "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
        geopistaEditor.addCursorTool("Pan",
                "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
        ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
        geopistaEditor.addPlugIn(zoomToFullExtentPlugIn);

        geopistaEditor.addPlugIn(geopistaValidatePlugIn);


        this.setSize(750, 600);
        jScrollPane2.getViewport().add(jedResumen, null);
        this.add(jScrollPane2, null);
        this.add(cmbTipoInfo, null);
        this.add(lblTipoFichero, null);
        this.add(lblImagen, null);
        scpErrores.getViewport().add(geopistaEditor, null);
        this.add(scpErrores, null);
        addAyudaOnline();

    }

  	/**
  	 * Ayuda Online
  	 * 
  	 */
  	private void addAyudaOnline() {
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("F1"), "action F1");

		this.getActionMap().put("action F1", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
	 			String uriRelativa = "/Geocuenca:ImportarActividadesEconomicas#Visualizar_asociaciones";
				GeopistaBrowser.openURL(aplicacion.getString("ayuda.geopista.web")
						+ uriRelativa);
			}
		});		
  	}
    
    
    public void enteredFromLeft(Map dataMap)
    {
        wizardContext.previousEnabled(false);
        jedResumen.setContentType("text/html");
        this.jedResumen.setEditable(false);
        try
        {
            // Iniciamos la ayuda
            String helpHS = "ayuda.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this, "InformacionReferenciaDatosCatastrosAlmacenados", hs);
        } catch (Exception excp)
        {
        }

        final TaskMonitorDialog pdLoadCapa = new TaskMonitorDialog(aplicacion
                .getMainFrame(), geopistaEditor.getContext().getErrorHandler());
        pdLoadCapa.setTitle(aplicacion.getI18nString("ImportandoDatos"));
        pdLoadCapa.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                   public void run()
                   {
                          boolean lastValue = geopistaValidatePlugIn.isMakeInsertion();
                          boolean firingEvents = false;
                          try
                          {
                               numeroRegistrosLeidos = 1;
                               registrosCorrectos = 0;
                               registrosErroneos = 0;
                               pdLoadCapa.report(aplicacion.getI18nString("CargandoMapaActEconomicas"));
                                    // Leemos el mapa
                               //capaParcelasFichero = (GeopistaLayer) blackboard.get("capaParcelasInfoReferencia");
                               //if(capaParcelasFichero!=null) geopistaEditor.getLayerManager().remove(capaParcelasFichero);
                               // Cargar la capa auxiliar.
                               try {
                                    geopistaEditor.loadMap(aplicacion.getString("url.mapa.actividadeseconomicas"));
                               }catch(Exception e)
                               {
                                   pdLoadCapa.setVisible(false);
                                   JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("errorCargaMapa"));
                                   throw e;
                                }
                                capaActEconomicas = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("actividades_economicas");
                                capaActEconomicas.setActiva(false);
                                capaActEconomicas.setVisible(false);
                            }catch (Exception e){
                                logger.error("Error",e);
                                pdLoadCapa.setVisible(false);
                                JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("SeHanProducidoErrores"));
                                wizardContext.cancelWizard();
                                return;
                            }finally{
                                try
                                {
	                                    geopistaValidatePlugIn.setMakeInsertion(lastValue);
                                        if (capaActEconomicas!=null)
	                                        capaActEconomicas.getLayerManager().setFiringEvents(firingEvents);
                                }catch(Exception e){}
                                   pdLoadCapa.setVisible(false);
                                }
                            }
                }).start();
                }
            });
           GUIUtil.centreOnWindow(pdLoadCapa);
           pdLoadCapa.setVisible(true);
           while (pdLoadCapa.isVisible()) try {Thread.sleep(100);}catch(Exception e){};

           if (capaActEconomicas!=null && capaActEconomicas.getFeatureCollectionWrapper().getFeatures().size() != 0)
           {
                  Object[] possibleValues = { aplicacion.getI18nString("GeopistaImportacionLog.Actualizar"), aplicacion.getI18nString("GeopistaImportacionLog.Cancelar") };
                  int selectedValue = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
                                                possibleValues, possibleValues[0]);
                  if (selectedValue == CANCELAR)
                  {
                        wizardContext.cancelWizard();
                        return;
                  }
                  //Borramos las que ya existan
                  deleteActividades();
           }

        if (!validarFeatures()) return;


        final TaskMonitorDialog pdShowCapa = new TaskMonitorDialog(aplicacion
                     .getMainFrame(), geopistaEditor.getContext().getErrorHandler());
        pdShowCapa.setTitle(aplicacion.getI18nString("ImportandoDatos"));
        pdShowCapa.addComponentListener(new ComponentAdapter(){
                 public void componentShown(ComponentEvent e){
                     new Thread(new Runnable(){
                        public void run()
                        {
                            try
                            {
                                    pdShowCapa.report(aplicacion.getI18nString("importar.parcelas.GrabandoDatosBaseDatos"));
                                    GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capaActEconomicas
                                            .getDataSourceQuery().getDataSource();
                                    Map driverProperties = geopistaServerDataSource.getProperties();
                                    Object lastResfreshValue = driverProperties.get(GeopistaConnection.REFRESH_INSERT_FEATURES);
                                    try
                                    {
	                                    driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,new Boolean(false));
	                                    geopistaServerDataSource.getConnection()
	                                            .executeUpdate(capaActEconomicas.getDataSourceQuery().getQuery(),
	                                                    capaActEconomicas.getFeatureCollectionWrapper().getUltimateWrappee(), pdShowCapa);
                                    }finally
                                    {
                                        if(lastResfreshValue!=null)
                                        {
                                            driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,lastResfreshValue);
                                        }
                                        else
                                        {
                                            driverProperties.remove(GeopistaConnection.REFRESH_INSERT_FEATURES);
                                        }
                                    }

                                    int registrosIniciales= ((Integer)(blackboard.get("totalActividades")!=null?blackboard.get("totalActividades"):new Integer(0))).intValue();
                                    cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.progreso.numero.leidos")
                                            + registrosIniciales;//(numeroRegistrosLeidos-1);
                                    cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.progreso.numero.insertados")
                                            + registrosCorrectos;
                                    cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.progreso.numero.no.insertados")
                                            + (registrosIniciales-registrosCorrectos);//registrosErroneos;
                                   cadenaTexto = cadenaTexto
                                            + aplicacion.getI18nString("importar.progreso.fecha.fin")
                                            + (String) new SimpleDateFormat("dd-MMM-yy hh:mm:ss").format(new Date());
                                    jedResumen.setText(cadenaTexto);

                                    // fin pasar los valores
                               } catch (Exception e){
                                    logger.error("Error",e);
                                     pdShowCapa.setVisible(false);
                                    JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("SeHanProducidoErrores"));
                                    wizardContext.cancelWizard();
                                    return;
                               }finally{
                               try
                               {
                                    //geopistaValidatePlugIn.setMakeInsertion(lastValue);
                                    //capaParcelas.getLayerManager().setFiringEvents(firingEvents);
                               }catch(Exception e){}
                                    pdShowCapa.setVisible(false);
                               }
                            }


                }).start();
                }
            });
           GUIUtil.centreOnWindow(pdShowCapa);
           pdShowCapa.setVisible(true);
           capaActEconomicas.setVisible(true);
           blackboard.put("listaActividades",null);
    }

    /**
     * Called when the user presses Next on this panel
     */
   public void exitingToRight() throws Exception
    {
	   //System.out.println("MostrarActividadesEconomicas.exitingToRight");
        if (blackboard!=null) blackboard.put("capaParcelasInfoReferencia",null);
        if(geopistaEditor!=null)
        {
            geopistaEditor.destroy();
            geopistaEditor.removeAll();
        }
        geopistaEditor = null;
        capaActEconomicas = null;
    }

    private boolean validarFeatures()
    {
        try
               {
                      boolean manualModification = ((Boolean) (blackboard.get("mostrarError")!=null?blackboard.get("mostrarError"):new Boolean(false))).booleanValue();
                      capaActEconomicas.getLayerManager().setFiringEvents(false);
                      geopistaValidatePlugIn.setMakeInsertion(false);
                      //sacamos el esquema de la capa parcelas
                      GeopistaSchema featureSchema = (GeopistaSchema)capaActEconomicas.getFeatureCollectionWrapper().getFeatureSchema();
                      // Extraemos los nombres en el locale actual de los atributos

                      String refTipoviaine = featureSchema.getAttributeByColumn("tipoviaine");
                      String refNombreviaine = featureSchema.getAttributeByColumn("nombreviaine");
                      String refRotulo = featureSchema.getAttributeByColumn("rotulo");
                      String refNif = featureSchema.getAttributeByColumn("nif");
                      String refSexo = featureSchema.getAttributeByColumn("sexo");
                      String refNombre_empresa =featureSchema.getAttributeByColumn("nombre_empresa");
                      String refNombre_comercial =featureSchema.getAttributeByColumn("nombre_comercial");
                        String refCodigo_postal =featureSchema.getAttributeByColumn("codigo_postal");
                        String refTelefono=featureSchema.getAttributeByColumn("telefono");
                        String refFax=featureSchema.getAttributeByColumn("fax");
                        String refForma_juridica=featureSchema.getAttributeByColumn("forma_juridica");
                        String refActividad_principal =featureSchema.getAttributeByColumn("actividad_principal");
                        String refActividades =featureSchema.getAttributeByColumn("actividades");
                        String refSede_social=featureSchema.getAttributeByColumn("sede_social");
                        String refSucursales=featureSchema.getAttributeByColumn("sucursales");
                        String refLink_empresa=featureSchema.getAttributeByColumn("link_empresa");
                        String refFecha_constitucion=featureSchema.getAttributeByColumn("fecha_constitucion");
                        String refEmpleados=featureSchema.getAttributeByColumn("empleados");
                        String refVolumen_negocio=featureSchema.getAttributeByColumn("volumen_negocio");
                        String refExporta_importa=featureSchema.getAttributeByColumn("exporta_importa");
                        String refCargo1=featureSchema.getAttributeByColumn("cargo1");
                        String refSexo1=featureSchema.getAttributeByColumn("sexo1");
                        String refNombre1=featureSchema.getAttributeByColumn("nombre1");
                        String refCargo2=featureSchema.getAttributeByColumn("cargo2");
                        String refSexo2=featureSchema.getAttributeByColumn("sexo2");
                        String refNombre2=featureSchema.getAttributeByColumn("nombre2");
                        String refCargo3=featureSchema.getAttributeByColumn("cargo3");
                        String refSexo3=featureSchema.getAttributeByColumn("sexo3");
                        String refNombre3=featureSchema.getAttributeByColumn("nombre3");
                        String refCargo4=featureSchema.getAttributeByColumn("cargo4");
                        String refSexo4=featureSchema.getAttributeByColumn("sexo4");
                        String refNombre4=featureSchema.getAttributeByColumn("nombre4");
                        String refCargo5=featureSchema.getAttributeByColumn("cargo5");
                        String refSexo5=featureSchema.getAttributeByColumn("sexo5");
                        String refNombre5 =featureSchema.getAttributeByColumn("nombre5");


                     // Configures cache of features
                         //TODO si se puede tener un identificador unico hacer esto
                        //sino habrá que borrar las peatures
                        //Feature[] cacheFeaturesBD=setSearchableList(capaActEconomicas.getFeatureCollectionWrapper().getFeatures(),
                        //                                         new GeopistaFeatureComparator(refCatAttName));

                        ArrayList listaActividades=(ArrayList) blackboard.get("listaActividades");
                        if (listaActividades==null) return false;
                        for (Iterator itFichero=listaActividades.iterator();itFichero.hasNext();)
                        {
                            //pdShowCapa.report(numeroRegistrosLeidos,listaFeaturesFichero.size(),aplicacion.getI18nString("importar.parcelas.ImportandoEntidad"));
                            DatosImportarActividades datosActividad = (DatosImportarActividades) itFichero.next();

                            boolean makeInsertion = false;

                            /*Feature currentFeature = searchRefCatastral(cacheFeaturesBD, f, new FeatureComparator(refCatAttName));
                             // Si NO la ha encontrado actualizar los valores y dejar el ID.
                            if (currentFeature == null)
                            {*/
                                Feature currentFeature = new BasicFeature(featureSchema);
                                currentFeature.setAttribute(refTipoviaine,datosActividad.getTipoviaine()!=null?datosActividad.getTipoviaine():"NE");
                                currentFeature.setAttribute(refNombreviaine,datosActividad.getNombreviaine());
                                currentFeature.setAttribute(refRotulo,datosActividad.getRotulo());
                                currentFeature.setAttribute(refNif,datosActividad.getNif());
                                currentFeature.setAttribute(refSexo,datosActividad.getSexo());
                                currentFeature.setAttribute(refNombre_empresa ,datosActividad.getNombre_empresa());
                                currentFeature.setAttribute(refNombre_comercial ,datosActividad.getNombre_comercial());
                                currentFeature.setAttribute(refCodigo_postal ,datosActividad.getCodigo_postal());
                                currentFeature.setAttribute(refTelefono,datosActividad.getTelefono());
                                currentFeature.setAttribute(refFax,datosActividad.getFax());
                                currentFeature.setAttribute(refForma_juridica,datosActividad.getForma_juridica());
                                currentFeature.setAttribute(refActividad_principal,datosActividad.getActividad_principal());
                                currentFeature.setAttribute(refActividades ,datosActividad.getActividades());
                                currentFeature.setAttribute(refSede_social,datosActividad.getSede_social());
                                currentFeature.setAttribute(refSucursales,datosActividad.getSucursales());
                                currentFeature.setAttribute(refLink_empresa,datosActividad.getLink_empresa());
                                currentFeature.setAttribute(refFecha_constitucion,datosActividad.getFecha_constitucion());
                                currentFeature.setAttribute(refEmpleados,datosActividad.getEmpleados());
                                currentFeature.setAttribute(refVolumen_negocio,datosActividad.getVolumen_negocio());
                                currentFeature.setAttribute(refExporta_importa,datosActividad.getExporta_importa());
                                currentFeature.setAttribute(refCargo1,datosActividad.getCargo1());
                                currentFeature.setAttribute(refSexo1,datosActividad.getSexo1());
                                currentFeature.setAttribute(refNombre1,datosActividad.getNombre1());
                                currentFeature.setAttribute(refCargo2,datosActividad.getCargo2());
                                currentFeature.setAttribute(refSexo2,datosActividad.getSexo2());
                                currentFeature.setAttribute(refNombre2,datosActividad.getNombre2());
                                currentFeature.setAttribute(refCargo3,datosActividad.getCargo3());
                                currentFeature.setAttribute(refSexo3,datosActividad.getSexo3());
                                currentFeature.setAttribute(refNombre3,datosActividad.getNombre3());
                                currentFeature.setAttribute(refCargo4,datosActividad.getCargo4());
                                currentFeature.setAttribute(refSexo4,datosActividad.getSexo4());
                                currentFeature.setAttribute(refNombre4,datosActividad.getNombre4());
                                currentFeature.setAttribute(refCargo5,datosActividad.getCargo5());
                                currentFeature.setAttribute(refSexo5,datosActividad.getSexo5());
                                currentFeature.setAttribute(refNombre5,datosActividad.getNombre5());

                                makeInsertion=true;
                            //}

                            //TODO Para probar luego hay que quitarlo
                            if (datosActividad.getGeometria()==null)
                            {
                                WKTReader wktReader=new WKTReader();
                                FeatureCollection fc=wktReader.read(new StringReader("POINT(573397.675 4436889.54)"));
                                Feature jumpFeature=(Feature)fc.iterator().next();
                                currentFeature.setGeometry(jumpFeature.getGeometry());
                            }
                            else
                                currentFeature.setGeometry(datosActividad.getGeometria());

                            ((GeopistaFeature) currentFeature).setLayer(capaActEconomicas);
                    /**
                     * Valida manualmente.
                     */
                            boolean validateResult = false;
                            boolean cancelImport = false;
                            SchemaValidator validator= new SchemaValidator(null);
                            while(!(validateResult = validator.validateFeature(currentFeature)))
                            {
                                if(!manualModification) break;
                                FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(capaActEconomicas, currentFeature);
                                if (featureDialog.wasOKPressed())
                                {
                                        Feature clonefeature = featureDialog.getModifiedFeature();
                                        currentFeature.setAttributes(clonefeature.getAttributes());
                                }
                                else
                                {
                                        Object[] possibleValues = { aplicacion.getI18nString("CancelarEsteElemento"), aplicacion.getI18nString("CancelarTodaLaImportacion"), aplicacion.getI18nString("IgnorarFuturosErrores") };
                                        int selectedValueCancel = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
                                                possibleValues, possibleValues[0]);
                                        if(selectedValueCancel==2) manualModification=false;
                                        if(selectedValueCancel==1) cancelImport=true;
                                        break;
                                }
                            }
                            if(validateResult ||  !manualModification)
                            {
                                registrosCorrectos++;
                                validateResult=true;
                            }
                            else
                                registrosErroneos++;
                            if(cancelImport==true) break;
                            if(makeInsertion&&validateResult)
                            {
                                    capaActEconomicas.getFeatureCollectionWrapper().add(currentFeature);
                            }
                            numeroRegistrosLeidos++;
                        }
                        listaActividades.clear();
               } catch (Exception e){
                        logger.error("Error",e);
                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("SeHanProducidoErrores"));
                        wizardContext.cancelWizard();
                        return false;
              }
              return true;
       }

    /**
       * Borra las anteriores estadisticas para el municipio
       * @return
       */
      public boolean deleteActividades()
      {
          try
          {
              Connection conn= AppContext.getApplicationContext().getConnection();
              //primero comprobamos si existe o no una estadistica para esa parcela
              PreparedStatement s = null;
              s = conn.prepareStatement("deleteActividadesEconomicas");
              s.setString(1,AppContext.getApplicationContext().getString("geopista.DefaultCityId"));
              s.execute();
              s.close();
              conn.close();
          }catch(Exception ex)
             {
                 logger.error("Error al borrar las estadistica del municipio",ex);
                 return false;
             }
             return true;

      }

}