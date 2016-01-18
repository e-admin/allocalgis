/**
 * GeopistaMostrarSubParcelas.java
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
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.control.ISesion;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

public class GeopistaMostrarSubParcelas extends JPanel implements WizardPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(GeopistaMostrarSubParcelas.class);

    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private Blackboard blackboard = aplicacion.getBlackboard();

    private JScrollPane scpErrores = new JScrollPane();

    private JLabel lblImagen = new JLabel();

    private JLabel lblTipoFichero = new JLabel();

    private JComboBox cmbTipoInfo = new JComboBox();

    private GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();

    private GeopistaEditor geopistaEditor = null;

    private GeopistaLayer capaParcelasFichero = null;

    private GeopistaLayer capaParcelasBD = null;

    private static final int CANCELAR = 1;

    private JScrollPane jScrollPane2 = new JScrollPane();

    private JEditorPane jedResumen = new JEditorPane();

    private int numeroRegistrosLeidos = 1;

    private String cadenaTexto = "";

    private int registrosErroneos = 0;
    private int registrosCorrectos = 0;
    private WizardContext wizardContext;

    //private ArrayList totalInsertFeatures = null;

    public GeopistaMostrarSubParcelas()
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
						                setName(aplicacion.getI18nString("importar.asistente.subparcelas.titulo.2"));
						                geopistaEditor = (GeopistaEditor) blackboard.get("geopistaEditorInfoReferencia");
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

    private void jbInit() throws Exception
    {
        this.setLayout(null);

        scpErrores.setBounds(new java.awt.Rectangle(130,50,600,323));
        scpErrores.getViewport().setLayout(null);

        cmbTipoInfo.addItem(aplicacion.getI18nString("importar.informe.construcciones"));
        cmbTipoInfo.addItem(aplicacion.getI18nString("importar.informe.cultivos"));

        lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        lblTipoFichero.setText(aplicacion
                .getI18nString("importar.informacion.referencia.tipo.fichero"));
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


        this.setSize(750, 550);
        jScrollPane2.getViewport().add(jedResumen, null);
        this.add(jScrollPane2, null);
        this.add(cmbTipoInfo, null);
        this.add(lblTipoFichero, null);
        this.add(lblImagen, null);
        scpErrores.getViewport().add(geopistaEditor, null);
        this.add(scpErrores, null);

    }

    public void enteredFromLeft(Map dataMap)
    {
    	wizardContext.previousEnabled(false);
    	jedResumen.setContentType("text/html");
    	this.jedResumen.setEditable(false);

    	/*try
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
        }*/

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
    						pdLoadCapa.report(aplicacion.getI18nString("CargandoMapaInforeferencia"));
    						// Leemos el mapa
    						capaParcelasFichero = (GeopistaLayer) blackboard.get("capaParcelasInfoReferencia");
    						if(capaParcelasFichero!=null) geopistaEditor.getLayerManager().remove(capaParcelasFichero);
    						// Cargar la capa auxiliar.
    						try {
    							geopistaEditor.loadMap(aplicacion.getString("url.mapa.inforeferencia"));
    						}catch(Exception e)
    						{
    							pdLoadCapa.setVisible(false);
    							JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("errorCargaMapa"));
    							throw e;
    						}

    						cmbTipoInfo.removeAllItems();
    						cmbTipoInfo.addItem((String) blackboard
    								.get("tipoSubparcela"));
    						String selectedModule = (String) blackboard
    						.get("tipoSubparcela");

    						if(selectedModule.equals(aplicacion.getI18nString("importar.informe.construcciones"))){
    							capaParcelasBD = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("Construcciones");
    						}
    						else if(selectedModule.equals(aplicacion.getI18nString("importar.informe.cultivos"))){
    							capaParcelasBD = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("Cultivos");
    						}
    						capaParcelasBD.setActiva(false);
    						capaParcelasBD.setVisible(false);

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
    							capaParcelasBD.getLayerManager().setFiringEvents(firingEvents);
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

    	try
    	{
    		if (capaParcelasBD.getFeatureCollectionWrapper().getFeatures().get(0)!=null)
    		{
    			Object[] possibleValues = { aplicacion.getI18nString("GeopistaImportacionLog.Actualizar"), aplicacion.getI18nString("GeopistaImportacionLog.Cancelar") };
    			int selectedValue = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
    					possibleValues, possibleValues[0]);
    			if (selectedValue == CANCELAR)
    			{
    				wizardContext.cancelWizard();
    				return;
    			}
    			int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
    					aplicacion.getI18nString("GeopistaImportacionLog.Borrar"),
    					"",
    					JOptionPane.OK_OPTION,
    					JOptionPane.QUESTION_MESSAGE,
    					null, null, null);
    		//	if(n == JOptionPane.NO_OPTION) return;

    			if(n == JOptionPane.OK_OPTION){
	    			String selectedModule = (String) blackboard
	    			.get("tipoSubparcela");

	    			if(selectedModule.equals(aplicacion.getI18nString("importar.informe.construcciones"))){
	    				deleteConstruccionesBD();
	    			}
	    			else if(selectedModule.equals(aplicacion.getI18nString("importar.informe.cultivos"))){
	    				deleteCultivosBD();
	    			}
    			}
    		}}catch(Exception e){}

    		String selectedModule = (String) blackboard
    		.get("tipoSubparcela");

    		if(selectedModule.equals(aplicacion.getI18nString("importar.informe.construcciones"))){
    			if (!validarFeaturesConstrucciones()) return;
    		}
    		else if(selectedModule.equals(aplicacion.getI18nString("importar.informe.cultivos"))){
    			if (!validarFeaturesCultivos()) return;
    		}

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
    							
    							//EPSG
    							CoordinateSystem originalEPSG=null;
    							try {
    								originalEPSG=(CoordinateSystem)blackboard.get("selectedImportEPSG");						
    								blackboard.remove("selectedImportEPSG");
    							} catch (Exception e1) {
    							}

    							GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capaParcelasBD
    							.getDataSourceQuery().getDataSource();
    							Map driverProperties = geopistaServerDataSource.getProperties();
    							Object lastResfreshValue = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
    							try
    							{
    								driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,new Boolean(false));
    								driverProperties.put("selectedImportEPSG",originalEPSG);
    								GeopistaConnection geoConn = (GeopistaConnection)geopistaServerDataSource.getConnection();
			            			ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY);
    		                    	String sridDefecto = geoConn.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
    		                    	DriverProperties driverPropertiesUpdate = geoConn.getDriverProperties();
    		                    	driverPropertiesUpdate.put("srid_destino",new Integer(sridDefecto));
    		                    	new GeopistaConnection(driverPropertiesUpdate)
    								.executeUpdate(capaParcelasBD.getDataSourceQuery().getQuery(),
    										capaParcelasBD.getFeatureCollectionWrapper().getUltimateWrappee(), pdShowCapa);
    							}finally
    							{
    								if(lastResfreshValue!=null)
    								{
    									driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,lastResfreshValue);
    								}
    								else
    								{
    									driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
    								}
    							}

    							cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.progreso.numero.leidos")
    							+ (numeroRegistrosLeidos-1);
    							cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.progreso.numero.insertados")
    							+ registrosCorrectos;
    							cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.progreso.numero.no.insertados")
    							+ registrosErroneos;
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
    		capaParcelasBD.getLayerManager().setFiringEvents(true);
    		capaParcelasBD.setVisible(true);
    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        blackboard.put("capaParcelasInfoReferencia",null);
        geopistaEditor = null;
        capaParcelasBD = null;
        capaParcelasFichero = null;
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
       //System.exit(0);

    }
    private boolean validarFeaturesConstrucciones()
    {
    	try
    	{
    		boolean manualModification = ((Boolean) blackboard.get("mostrarError")).booleanValue();
    		capaParcelasBD.getLayerManager().setFiringEvents(false);
    		geopistaValidatePlugIn.setMakeInsertion(false);
    		//sacamos el esquema de la capa parcelas
    		GeopistaSchema featureSchema = (GeopistaSchema)capaParcelasBD.getFeatureCollectionWrapper().getFeatureSchema();
    		// Extraemos los nombres en el locale actual de los atributos

    		//final String refCatAttName = (featureSchema.getAttributeByColumn("Masa") + featureSchema.getAttributeByColumn("Parcela")).trim();

    		// Configures cache of features
    		//pdShowCapa.report(aplicacion.getI18nString("importar.parcelas.ImportandoDatos"));
    		//Feature[] cacheFeaturesBD=setSearchableList(capaParcelasBD.getFeatureCollectionWrapper().getFeatures(),
    		//		new GeopistaFeatureComparator(refCatAttName));

    		List listaFeaturesFichero = capaParcelasFichero.getFeatureCollectionWrapper().getFeatures();
    		for (Iterator itFichero=listaFeaturesFichero.iterator();itFichero.hasNext();)
    		{

    			boolean makeInsertion = false;

    			Feature currentFeature =null;
    			//  TODO Si no hay que borra todas las subparcelas habría que hacer algo así
    			//  por ahora borramos las subparcelas
    			//  currentFeature = searchRefCatastral(cacheFeaturesBD, f, new FeatureComparator(refCatAttName));
    			// Si NO la ha encontrado actualizar los valores y dejar el ID.
    			if (currentFeature == null)
    			{
    				//String refCat = (masa + parcela /*+ hoja*/).trim();
    				currentFeature = new BasicFeature(featureSchema);
    				//currentFeature.setAttribute(refCatAttName, refCat);
    				makeInsertion=true;
    			}

    			//pdShowCapa.report(numeroRegistrosLeidos,listaFeaturesFichero.size(),aplicacion.getI18nString("importar.parcelas.ImportandoEntidad"));
    			Feature f = (Feature) itFichero.next();
    			//String fid = "";//f.getString("FID").trim();

    			String constru = null;
    			try{
    				String construAttName = featureSchema.getAttributeByColumn("constru");
    				constru = f.getString("CONSTRU");
    				currentFeature.setAttribute(construAttName,constru);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener el Constru. Se considera Constru Null : Feature f = "
    							+ f + ", "+f.getString("CONSTRU"));
    				}
    			}

    			String tipo = null;
    			try{
    				String tipoAttName = featureSchema.getAttributeByColumn("tipo");
    				tipo = f.getString("TIPO").trim();
    				currentFeature.setAttribute(tipoAttName,tipo);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener el Tipo. Se considera Tipo Null : Feature f = "
    							+ f + ", "+f.getString("TIPO"));
    				}
    			}

    			String masa = null;
    			try{
    				String masaAttName = featureSchema.getAttributeByColumn("masa");
    				masa = f.getString("MASA").trim();
    				currentFeature.setAttribute(masaAttName,masa);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la Masa. Se considera Masa Null : Feature f = "
    							+ f + ", "+f.getString("MASA"));
    				}
    			}

    			String parcela = null;
    			try{
    				String parcelaAttName = featureSchema.getAttributeByColumn("parcela");
    				parcela = f.getString("PARCELA").trim();
    				currentFeature.setAttribute(parcelaAttName,parcela);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la Parcela. Se considera Parcela Null : Feature f = "
    							+ f + ", "+f.getString("PARCELA"));
    				}
    			}

    			String mapa = null;
    			try{
    				String mapaAttName = featureSchema.getAttributeByColumn("mapa");
    				mapa = f.getString("MAPA").trim();
    				currentFeature.setAttribute(mapaAttName,mapa);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener el Mapa. Se considera Mapa Null : Feature f = "
    							+ f + ", "+f.getString("MAPA"));
    				}
    			}

    			Double coorX = null;
    			try{
    				String coorxAttName = featureSchema.getAttributeByColumn("coorx");
    				coorX = new Double(f.getString("COORX"));
    				currentFeature.setAttribute(coorxAttName,coorX);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la coordenada X. Se considera CoorX Null : Feature f = "
    							+ f + ", "+f.getString("COORX"));
    				}
    			}

    			Double coorY = null;
    			try{
    				String cooryAttName = featureSchema.getAttributeByColumn("coory");
    				coorY = new Double(f.getString("COORY"));
    				currentFeature.setAttribute(cooryAttName,coorY);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la coordenada Y. Se considera CoorY Null : Feature f = "
    							+ f + ", "+f.getString("COORY"));
    				}
    			}

    			Integer numSymbol = null;
    			try{
    				String numsymbolAttName = featureSchema.getAttributeByColumn("numsymbol");
    				numSymbol = new Integer(f.getString("NUMSYMBOL"));
    				currentFeature.setAttribute(numsymbolAttName,numSymbol);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener el NumSymbol. Se considera NumSymbol Null : Feature f = "
    							+ f + ", "+f.getString("NUMSYMBOL"));
    				}
    			}

    			Double area = null;
    			try{
    				String areaAttName = featureSchema.getAttributeByColumn("area");
    				area = new Double(f.getString("AREA"));
    				currentFeature.setAttribute(areaAttName,area);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener el area. Se considera area Null : Feature f = "
    							+ f + ", "+f.getString("AREA"));
    				}
    			}

    			DateFormat sourceDateFormat = new SimpleDateFormat("yyyyMMdd");
    			Date dateAlta = null;
    			try
    			{
    				String fechaAltaAttName = featureSchema.getAttributeByColumn("fechaalta");
    				dateAlta = (Date) sourceDateFormat.parse(f.getString("FECHAALTA"));
    				currentFeature.setAttribute(fechaAltaAttName,dateAlta);

    			}catch(Exception e)
    			{
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al traspasar la fecha. Se considera fecha Null : Feature f = "
    							+ f + ", "+f.getString("FECHAALTA"));
    				}
    			}
    			Date dateBaja = null;

    			try
    			{
    				if(!f.getString("FECHABAJA").trim().equals("99999999"))
    				{
    					String fechaBajaAttnName = featureSchema.getAttributeByColumn("fechabaja");
    					dateBaja = (Date) sourceDateFormat.parse(f.getString("FECHABAJA"));
    					currentFeature.setAttribute(fechaBajaAttnName,dateBaja);
    				}
    			}catch(Exception e)
    			{
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al traspasar la fecha. Se considera " +
    							"fecha Null : Feature f = "+ f+ ", "+f.getString("FECHABAJA"));
    				}
    			}

    			try{
    				currentFeature.setGeometry(f.getGeometry());
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al traspasar la Geometría. Se considera " +
    							"geometría Null : Feature f = "+ f+ ", "+f.getGeometry());
    				}
    			}

    			((GeopistaFeature) currentFeature).setLayer(capaParcelasBD);
    			/**
    			 * Valida manualmente.
    			 */
    			boolean validateResult = false;
    			boolean cancelImport = false;
    			SchemaValidator validator= new SchemaValidator(null);
    			while(!(validateResult = validator.validateFeature(currentFeature)))
    			{
    				if(!manualModification) break;
    				FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(capaParcelasBD, currentFeature);
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
    			if(validateResult)
    				registrosCorrectos++;
    			else
    				registrosErroneos++;
    			if(cancelImport==true) break;
    			if(makeInsertion&&validateResult)
    			{
    				capaParcelasBD.getFeatureCollectionWrapper().add(currentFeature);
    			}
    			numeroRegistrosLeidos++;
    		}
    	} catch (Exception e){
    		logger.error("Error",e);
    		JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("SeHanProducidoErrores"));
    		wizardContext.cancelWizard();
    		return false;
    	}
    	return true;
    }


    private boolean validarFeaturesCultivos()
    {
    	try
    	{
    		boolean manualModification = ((Boolean) blackboard.get("mostrarError")).booleanValue();
    		capaParcelasBD.getLayerManager().setFiringEvents(false);
    		geopistaValidatePlugIn.setMakeInsertion(false);
    		//sacamos el esquema de la capa parcelas
    		GeopistaSchema featureSchema = (GeopistaSchema)capaParcelasBD.getFeatureCollectionWrapper().getFeatureSchema();
    		// Extraemos los nombres en el locale actual de los atributos

    		//final String refCatAttName = (featureSchema.getAttributeByColumn("Masa") + featureSchema.getAttributeByColumn("Parcela")).trim();

    		// Configures cache of features
    		//pdShowCapa.report(aplicacion.getI18nString("importar.parcelas.ImportandoDatos"));
    		//Feature[] cacheFeaturesBD=setSearchableList(capaParcelasBD.getFeatureCollectionWrapper().getFeatures(),
    		//		new GeopistaFeatureComparator(refCatAttName));

    		List listaFeaturesFichero = capaParcelasFichero.getFeatureCollectionWrapper().getFeatures();
    		for (Iterator itFichero=listaFeaturesFichero.iterator();itFichero.hasNext();)
    		{
    			boolean makeInsertion = false;

    			Feature currentFeature =null;
    			//  TODO Si no hay que borra todas las subparcelas habría que hacer algo así
    			//  por ahora borramos las subparcelas
    			//  currentFeature = searchRefCatastral(cacheFeaturesBD, f, new FeatureComparator(refCatAttName));
    			// Si NO la ha encontrado actualizar los valores y dejar el ID.
    			if (currentFeature == null)
    			{
    				//String refCat = (masa + parcela /*+ hoja*/).trim();
    				currentFeature = new BasicFeature(featureSchema);
    				//currentFeature.setAttribute(refCatAttName, refCat);
    				makeInsertion=true;
    			}

    			//pdShowCapa.report(numeroRegistrosLeidos,listaFeaturesFichero.size(),aplicacion.getI18nString("importar.parcelas.ImportandoEntidad"));
    			Feature f = (Feature) itFichero.next();
    			//String fid = "";//f.getString("FID").trim();

    			String subparce = null;
    			try{
    				String subparceAttName = featureSchema.getAttributeByColumn("subparce");
    				subparce = f.getString("SUBPARCE");
    				currentFeature.setAttribute(subparceAttName,subparce);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la subparcela. Se considera Subparce Null : Feature f = "
    							+ f + ", "+f.getString("SUBPARCE"));
    				}
    			}

    			String tipo = null;
    			try{
    				String tipoAttName = featureSchema.getAttributeByColumn("tipo");
    				tipo = f.getString("TIPO").trim();
    				currentFeature.setAttribute(tipoAttName,tipo);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener el Tipo. Se considera Tipo Null : Feature f = "
    							+ f + ", "+f.getString("TIPO"));
    				}
    			}

    			String masa = null;
    			try{
    				String masaAttName = featureSchema.getAttributeByColumn("masa");
    				masa = f.getString("MASA").trim();
    				currentFeature.setAttribute(masaAttName,masa);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la Masa. Se considera Masa Null : Feature f = "
    							+ f + ", "+f.getString("MASA"));
    				}
    			}

    			String hoja = null;
    			try{
    				String hojaAttName = featureSchema.getAttributeByColumn("hoja");
    				hoja = f.getString("HOJA").trim();
    				currentFeature.setAttribute(hojaAttName,hoja);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la Hoja. Se considera Hoja Null : Feature f = "
    							+ f + ", "+f.getString("HOJA"));
    				}
    			}

    			String parcela = null;
    			try{
    				String parcelaAttName = featureSchema.getAttributeByColumn("parcela");
    				parcela = f.getString("PARCELA").trim();
    				currentFeature.setAttribute(parcelaAttName,parcela);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la Parcela. Se considera Parcela Null : Feature f = "
    							+ f + ", "+f.getString("PARCELA"));
    				}
    			}

    			String mapa = null;
    			try{
    				String mapaAttName = featureSchema.getAttributeByColumn("mapa");
    				mapa = f.getString("MAPA").trim();
    				currentFeature.setAttribute(mapaAttName,mapa);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener el Mapa. Se considera Mapa Null : Feature f = "
    							+ f + ", "+f.getString("MAPA"));
    				}
    			}

    			Double coorX = null;
    			try{
    				String coorxAttName = featureSchema.getAttributeByColumn("coorx");
    				coorX = new Double(f.getString("COORX"));
    				currentFeature.setAttribute(coorxAttName,coorX);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la coordenada X. Se considera CoorX Null : Feature f = "
    							+ f + ", "+f.getString("COORX"));
    				}
    			}

    			Double coorY = null;
    			try{
    				String cooryAttName = featureSchema.getAttributeByColumn("coory");
    				coorY = new Double(f.getString("COORY"));
    				currentFeature.setAttribute(cooryAttName,coorY);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener la coordenada Y. Se considera CoorY Null : Feature f = "
    							+ f + ", "+f.getString("COORY"));
    				}
    			}

    			Integer numSymbol = null;
    			try{
    				String numsymbolAttName = featureSchema.getAttributeByColumn("numsymbol");
    				numSymbol = new Integer(f.getString("NUMSYMBOL"));
    				currentFeature.setAttribute(numsymbolAttName,numSymbol);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener el NumSymbol. Se considera NumSymbol Null : Feature f = "
    							+ f + ", "+f.getString("NUMSYMBOL"));
    				}
    			}

    			Double area = null;
    			try{
    				String areaAttName = featureSchema.getAttributeByColumn("area");
    				area = new Double(f.getString("AREA"));
    				currentFeature.setAttribute(areaAttName,area);
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al obtener el area. Se considera area Null : Feature f = "
    							+ f + ", "+f.getString("AREA"));
    				}
    			}

    			DateFormat sourceDateFormat = new SimpleDateFormat("yyyyMMdd");
    			Date dateAlta = null;
    			try
    			{
    				String fechaAltaAttName = featureSchema.getAttributeByColumn("fechaalta");
    				dateAlta = (Date) sourceDateFormat.parse(f.getString("FECHAALTA"));
    				currentFeature.setAttribute(fechaAltaAttName,dateAlta);
    			}catch(Exception e)
    			{
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al traspasar la fecha. Se considera fecha Null : Feature f = "
    							+ f + ", "+f.getString("FECHAALTA"));
    				}
    			}
    			Date dateBaja = null;

    			try
    			{
    				if(!f.getString("FECHABAJA").trim().equals("99999999"))
    				{
    					String fechaBajaAttnName = featureSchema.getAttributeByColumn("fechabaja");
    					dateBaja = (Date) sourceDateFormat.parse(f.getString("FECHABAJA"));
    					currentFeature.setAttribute(fechaBajaAttnName,dateBaja);
    				}
    			}catch(Exception e)
    			{
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al traspasar la fecha. Se considera " +
    							"fecha Null : Feature f = "+ f+ ", "+f.getString("FECHABAJA"));
    				}
    			}

    			try{
    				currentFeature.setGeometry(f.getGeometry());
    			}catch(Exception e){
    				if (logger.isDebugEnabled()){
    					logger.debug("run() - Error al traspasar la Geometría. Se considera " +
    							"geometría Null : Feature f = "+ f+ ", "+f.getGeometry());
    				}
    			}

    			((GeopistaFeature) currentFeature).setLayer(capaParcelasBD);
    			/**
    			 * Valida manualmente.
    			 */
    			boolean validateResult = false;
    			boolean cancelImport = false;
    			SchemaValidator validator= new SchemaValidator(null);
    			while(!(validateResult = validator.validateFeature(currentFeature)))
    			{
    				if(!manualModification) break;
    				FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(capaParcelasBD, currentFeature);
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
    			if(validateResult)
    				registrosCorrectos++;
    			else
    				registrosErroneos++;
    			if(cancelImport==true) break;
    			if(makeInsertion&&validateResult)
    			{
    				capaParcelasBD.getFeatureCollectionWrapper().add(currentFeature);
    			}
    			numeroRegistrosLeidos++;
    		}
    	} catch (Exception e){
    		logger.error("Error",e);
    		JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("SeHanProducidoErrores"));
    		wizardContext.cancelWizard();
    		return false;
    	}
    	return true;
    }

    /**
     * utiliza una cache para realizar la busqueda por identificador catastral
      * @param refCatFeature
      * @param comparator
      * @return
      */

    private  Feature searchRefCatastral(Feature[] cache,Feature refCatFeature, Comparator comparator)
    {
            int featureIndex = Arrays.binarySearch(cache, refCatFeature,comparator);
            if(featureIndex<0) return null;
            return  cache[featureIndex];
    }

    private Feature[] setSearchableList(List listaLayerParcelas,Comparator comparator)
   {
       Feature[] cache = new Feature[listaLayerParcelas.size()];
       int i=0;
       for (Iterator features = listaLayerParcelas.iterator(); features.hasNext();)
       {
           Feature feature = (Feature) features.next();
           cache[i++]=feature;
        }
        Arrays.sort(cache,comparator);
        return cache;
   }

    /* método xa obtener las subparcelas a borrar */
    private void deleteSubparcelasBD()
    {
        Connection con = null;
        try
        {
            con = getDBConnection();
            PreparedStatement ps = con.prepareStatement("borrarSubparcelas");
            ps.setString(1, com.geopista.security.SecurityManager.getIdMunicipio());
            ps.execute();
        }
        catch(Exception e)
        {
            logger.error("Error al borrar las subparcelas ", e);
        }
    }

    /* método xa obtener las subparcelas a borrar */
    private void deleteConstruccionesBD()
    {
        Connection con = null;
        try
        {
            con = getDBConnection();
            PreparedStatement ps = con.prepareStatement("MCborrarConstrucciones");
            ps.setString(1, com.geopista.security.SecurityManager.getIdMunicipio());
            ps.execute();
        }
        catch(Exception e)
        {
            logger.error("Error al borrar las construcciones ", e);
        }
    }

    private void deleteCultivosBD()
    {
        Connection con = null;
        try
        {
            con = getDBConnection();
            PreparedStatement ps = con.prepareStatement("MCborrarCultivos");
            ps.setString(1, com.geopista.security.SecurityManager.getIdMunicipio());
            ps.execute();
        }
        catch(Exception e)
        {
            logger.error("Error al borrar los cultivos ", e);
        }
    }

    /* obtenemos la conexion a la BD */
    public Connection getDBConnection()
    {
        Connection conn = null;
        try
        {
            // Quitamos los drivers
            Enumeration e = DriverManager.getDrivers();
            while (e.hasMoreElements())
            {
                DriverManager.deregisterDriver((Driver) e.nextElement());
            }
            DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());
            String sConn = aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_URL);
            conn = DriverManager.getConnection(sConn);
            AppContext app = (AppContext) AppContext.getApplicationContext();
            conn = app.getConnection();
            conn.setAutoCommit(false);
        } catch (Exception e)
        {
            return null;
        }
        return conn;
   }

} // de la clase general

 class GeopistaFeatureComparator  implements Comparator
 {      private String refCatAttName;
        public GeopistaFeatureComparator(String refCatAttName)
        {
              this.refCatAttName=refCatAttName;
        }
        public int compare(Object o1, Object o2)
        {
             Feature f1 = (Feature) o1;
             Feature f2 = (Feature) o2;
             String f1ReferenciaCatastral = f1.getString(refCatAttName);
             String f2ReferenciaCatastral = f2.getString(refCatAttName);
             return f1ReferenciaCatastral.compareToIgnoreCase(f2ReferenciaCatastral);
        }
 };
 class FeatureComparator implements Comparator
 {
        private String refCatAttName;
        public FeatureComparator(String refCatAttName)
        {
              this.refCatAttName=refCatAttName;
        }
        public int compare(Object o1, Object o2) {
        Feature f1 = (Feature) o1;
        Feature f2 = (Feature) o2;
        String f1ReferenciaCatastral = f1.getString(refCatAttName);
        String masa = f2.getString("MASA");
        //String hoja = f2.getString("HOJA");
        String fid = "";//f2.getString("FID");
        String parcela = f2.getString("PARCELA");
        String f2ReferenciaCatastral = (masa + parcela + fid/*+ hoja*/).trim();
        return f1ReferenciaCatastral.compareToIgnoreCase(f2ReferenciaCatastral);}
};