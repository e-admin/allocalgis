/*
 * 
 * Created on 28-mar-2005 by juacas
 *
 * 
 */
package com.geopista.bean;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaContext;
import com.geopista.feature.AutoFieldDomain;
import com.geopista.feature.BooleanDomain;
import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.Column;
import com.geopista.feature.DateDomain;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.NumberDomain;
import com.geopista.feature.SchemaValidator;
import com.geopista.feature.StringDomain;
import com.geopista.feature.Table;
import com.geopista.feature.TreeDomain;
import com.geopista.feature.ValidationError;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.style.sld.model.impl.actions.GetUserStyleAction;
import com.geopista.ui.GEOPISTAWorkbenchContext;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.dialogs.FeatureFrame;
import com.geopista.ui.dialogs.GeopistaListaMapasDialog;
import com.geopista.ui.dialogs.GeopistaSaveBookmark;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.geopista.ui.plugin.LogFeatutesEvents;


/**
 * Implementación simulada para el desarrollo de las 
 * aplicaciones en VBA
 * @author juacas
 *
 */
public class GeopistaBeanProxy implements IGEOPISTABean
{
private static final String	CARGA_DEL_MAPA_CANCELADA	= "Carga del mapa cancelada";
private static final String	EDICION_CANCELADA	= "Edición cancelada.";
private static final String	FEATURE_NO_INICIALIZADA	= "Feature no inicializada.";
private static final String	SCHEMA_NO_INICIALIZADO	= "Esquema no inicializado.";
private static final String SAVE_LOGGER_PROBLEM= "Problemas al Salvar el Log de la capa";
private HashMap layers=new HashMap(); // Storage for session layers
private HashMap schemas=new HashMap(); // Storege for Schemas 
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(GeopistaBeanProxy.class);

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 1L;

	private JLabel iconoGeopista = null;

	private GeopistaLayer	activeLayer=null;

	private GeopistaFeature	tempFeature;

	private GeopistaMap	mapGeopista;
	private GeopistaMapProxy mapProxy=new GeopistaMapProxy();

	private static int contador=0;

	private ArrayList errorMessages=new ArrayList();
	/**
	 * 
	 */
	public GeopistaBeanProxy()
	{
if (logger.isDebugEnabled())
	{
	logger.debug("TestingBeanProxy() - Entrada en TestingBeanProxy");
	}
try{
    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
}catch(Exception e){
	if (logger.isDebugEnabled())
		{
		logger
				.debug(
						"GEOPISTABeanProxy() - no se puede instanciar Windows Look&feel",
						e);
		}
};
//System.out.println("Logg:"+LogFactory.getFactory());
//System.out.println("Logg:"+logger);
//PropertyConfigurator.configureAndWatch("c:\\log4j.properties");
AppContext.getApplicationContext().isOnline(); // Fuerza sondeo HeartBeat
	//	initialize();
	
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.GEOFeatureBean#setLayer(java.lang.String)
	 */
	public boolean setLayer(String layerName)
	{
	resetErrorMessages();
	GeopistaLayer lyr=(GeopistaLayer) layers.get(layerName);
	if (lyr==null)
		{
		lyr= new GeopistaLayer();
		lyr.setName(layerName);
		lyr.setSystemId(layerName);
		
		try{
			
			lyr.activateLogger(mapGeopista.getProjectFile().getAbsolutePath());
			}
			catch (Exception e)
			{
			    e.printStackTrace();
			    
			}
		
		}
	activeLayer=lyr;
	
	
	//	Guarda un log de eventos
	
	
	
	layers.put(layerName,lyr);
	createWorkFeature();
	
	
	
	
	return true;
	}

	/**
	 * Create a temporal feature from active Layer
	 */
	private void createWorkFeature()
	{
	
	GeopistaSchema sch = (GeopistaSchema) _getSchema();
	if (sch==null)
		{
		errorMessages.add(SCHEMA_NO_INICIALIZADO);
		tempFeature=new GeopistaFeature();
		return;
		}
	GeopistaFeature ftr = new GeopistaFeature(sch);
	tempFeature=ftr;
	}

	/**  Set the schema of current layer and resets current temporal feature 
 
 @param sch  */
	private void setSchema(GeopistaSchema sch)
	{
	schemas.put(activeLayer.getName(), sch);
	createWorkFeature();
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.GEOFeatureBean#setAttribute(java.lang.String, java.lang.Object)
	 */
	public boolean setAttribute(String name, Object value)
	{
	resetErrorMessages();
	try
		{
		_getFeature().setAttribute(name, value);
		//notifyCall();
		return true;
		}
	catch (NullPointerException e)
	{
	errorMessages.add(FEATURE_NO_INICIALIZADA);

		logger.debug("setAttribute() - Feature no inicializada.");
		
	}
	catch (IllegalArgumentException e)
		{
		errorMessages.add("Atributo no existente: "+name);
		logger.debug("setAttribute(String name = " + name
				+ ") - Atributo no existente");
		
		}
	return false;
	}

	
	/* (non-Javadoc)
	 * @see com.geopista.bean.GEOFeatureBean#setAttribute(java.lang.String, java.lang.Object)
	 */
	public boolean setAttributeAsString(String name, String value)
	{
	    
	    Object obj = new Object();
	    obj = (Object) value;
	    return setAttribute(name, obj);
	    
	}

	
		
	
	/* (non-Javadoc)
	 * @see com.geopista.bean.GEOFeatureBean#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name)
	{
	resetErrorMessages();
	GeopistaFeature ftr=_getFeature();
	if (ftr!=null)
		{
		return ftr.getAttribute(name);
		}
	else
		{
		errorMessages.add(FEATURE_NO_INICIALIZADA);
	return null;
	}
		
	
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.GEOFeatureBean#edit()
	 */
	public boolean edit()
	{
	resetErrorMessages();
		if (logger.isDebugEnabled())
			{
			logger.debug("edit() - Lanza Edición de Feature");
			}


FeatureFrame fd=new FeatureFrame("prueba",tempFeature);
		
fd.buildDialog();
		 
ImageIcon icon=IconLoader.icon("logo_geopista.png");
		 fd.setSideBarImage(icon);
		 
//		   fd.setSideBarDescription("<b>Toda la estructura del formulario</b>, incluyendo su validación y comportamiento dinámico se obtiene de la información del sistema:" +
//		   		"<ul><li>esquema de datos</li><li>dominios.</li></ul>");
//	   	 	 
		   fd.setVisible(true);
		   
	while(fd.isVisible())
	{
	    try
	    {
	        Thread.sleep(2000);
	    }catch(InterruptedException e)
	    {
	     e.printStackTrace();   
	    }
	}
		 if (fd.wasOKPressed())
		 	{
		 	tempFeature = (GeopistaFeature) fd.getModifiedFeature();
		 	return true;
		 	}
		 else
		 	{
		 	errorMessages.add(EDICION_CANCELADA);
		 	return false;
		 	}
	}

	/**
	 * @return
	 */
	private JFrame getFrame()
	{
	if (fr==null)
		{
		fr = new JFrame("GeopistaProxy");
		fr.setBounds(0,0,20,20);
	fr.setFocusable(true);
	fr.setFocusableWindowState(true);
	fr.requestFocus();
		}
	return fr;
	}
	/**
	 * Crea una configuración para simular el funcionamiento del ActiveX
	 * Todas las acciones de mapas se realizarán con el fichero
	 * c:\\testing.dxf
	 */
private boolean testing=false;
	private static final String	LAYER_NO_INICIALIZADO	= "Layer no inicializado.";
	private JFrame	fr;
	
public void createTestConfig()
{
testing=true;
/**
 * Construct some default domains
 */
	StringDomain string1Domain= new StringDomain("20[.{3,8}]","Solo 20 caracteres.");
	NumberDomain pesoDomain=new NumberDomain("[0:80]##.##","prueba");
	BooleanDomain instalado1SN= new BooleanDomain("Instalado:", "Se ha instalado correctamente. S/N");
	NumberDomain peque;
	NumberDomain grande;
	TreeDomain calibresDomain = new TreeDomain("Calibres","Diámetros de tubería");
	  	calibresDomain.addChild(peque = new NumberDomain("[0.00:12.00]##.##","Tubo pequeño."));
	  	calibresDomain.addChild(grande = new NumberDomain("[12.01:45.00]","Tubo grande."));
		
	  peque.addChild(new CodedEntryDomain("tubito","Pequeño tubo") );
	  peque.addChild(new CodedEntryDomain("tubillo","pequeñillo tubillo") );
	  grande.addChild(new CodedEntryDomain("Cacho tubo","cacho cilindro") );
	  grande.addChild(new CodedEntryDomain("Tubazo","tubería padre") );
	 
	  
	  StringDomain stringDomain= new StringDomain("20[.*]","Solo 20 caracteres.");
	  BooleanDomain instaladoSN= new BooleanDomain("Instalado:", "Se ha instalado correctamente. S/N");
	  StringDomain cascajo, sillares;
	  TreeDomain materialesDomain = new TreeDomain("Materiales","Materiales de la construcción");
	  materialesDomain.addChild(cascajo = new CodedEntryDomain("Casc","Cantería."));
	  materialesDomain.addChild(sillares = new CodedEntryDomain("Sill","Sillares."));
	  
	  cascajo.addChild(new CodedEntryDomain("CF","Cascajo fino (Zahorra)") );
	  cascajo.addChild(new CodedEntryDomain("CG","Cascajo grueso (cantos)") );
	  sillares.addChild(new CodedEntryDomain("SM","Sillar de mármol. Piedra fina tipo uno") );
	  sillares.addChild(new CodedEntryDomain("SC","Sillar de cuarcita gruesa tipo luxe.") );
	  
	  CodeBookDomain cbPropiedad=new CodeBookDomain("Propietarios","Lista de propietarios de la construcción");
	  cbPropiedad.addChild(new CodedEntryDomain("PRIV","Privada"));
	  cbPropiedad.addChild(new CodedEntryDomain("PUBL","Pública"));
	  
	  
	  DateFormat fecha= DateFormat.getDateInstance();
//	  try {
//	//	Date fe=fecha.parse("10 agosto 2003");
//	} catch (ParseException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
	  DateDomain fechaDomain= new DateDomain("[19/04/1973:NOW]dd/MM/yy","Fecha de inicio de obras");
	  AutoFieldDomain autoFormulaDomain=new AutoFieldDomain("FORMULA:Peso*Calibre:#.###,00", "Cálculo producto");
	  AutoFieldDomain autoAreaDomain=new AutoFieldDomain("AREA:#.###,00", "Área Feature");
	/**
	   * Construct the databasemodel
	   */
	  Table	table1= new Table("Usuarios","usuarios registrados en el sistema");
	  Table	table2= new Table("Pesos","pesos y medidas");
	  Table	table3= new Table("Materiales","Materiales de la fabricación.");
	  
	  Column nameCol= new Column("Name","Nombre_Usuario",table1,string1Domain);
	  Column pesoCol= new Column("Peso","Peso del sujeto",table2,pesoDomain);
	  Column denomCaliCol= new Column("denominacion","Denominacion del calibre.",table3,calibresDomain);
	  Column calibreCol= new Column("Calibre","Calibre tubos.",table3,calibresDomain);
	  Column restauradoCol= new Column("Restaurado","Estado de la restauración.",table3,instalado1SN);
	  calibresDomain.attachColumnToLevel(calibreCol,0);
	  calibresDomain.attachColumnToLevel(denomCaliCol,1);
	
	  Column col1= new Column("Name","Nombre_Usuario",table1,stringDomain);
	  Column fechaCol= new Column("FechaInst","Fecha instalación",table2,fechaDomain);
	  Column submaterialCol= new Column("Subtipo material","Subtipo de material utilizado.",table3,materialesDomain);
	  Column materialCol= new Column("material","Material de la edificación.",table3,materialesDomain);
	  Column col5= new Column("Instalación","Reclamaciones en la instalación.",table3,instaladoSN);
	  Column col6= new Column("Propiedad","Propiedad de la construcción (col).",table3,cbPropiedad);
	  Column col7= new Column("Area", "área de la Feature",table3, autoAreaDomain);
	  Column col8= new Column("Producto", "producto",table3, autoFormulaDomain);
	  
	  materialesDomain.attachColumnToLevel(materialCol,0);
	  materialesDomain.attachColumnToLevel(submaterialCol,1);
	  
	
	  GeopistaSchema gSchema= new GeopistaSchema();
	  gSchema.addAttribute("Peso",AttributeType.FLOAT, pesoCol,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("Calibre",AttributeType.STRING, calibreCol,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("DenomCalib", AttributeType.STRING, denomCaliCol,GeopistaSchema.READ_WRITE);
	 
	  gSchema.addAttribute("Instalado",AttributeType.INTEGER, restauradoCol,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("Campo 1",AttributeType.STRING, col1,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("fecha 2",AttributeType.DATE, fechaCol,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("SubtipoMatt", AttributeType.STRING, submaterialCol,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("TipoMatt",AttributeType.STRING, materialCol,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("Restaurado",AttributeType.INTEGER, col5,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("Propiedad",AttributeType.STRING, col6,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("Area", AttributeType.DOUBLE, col7,GeopistaSchema.READ_WRITE);
	  gSchema.addAttribute("Producto", AttributeType.DOUBLE, col8,GeopistaSchema.READ_WRITE);
//	
	  tempFeature = new GeopistaFeature(gSchema);
}
	/* (non-Javadoc)
	 * @see com.geopista.bean.GEOFeatureBean#reset()
	 */
	public void reset()
	{
	resetErrorMessages();
	
	createWorkFeature();
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.GEOMapBean#getMapName()
	 */
	public String getMapName()
	{
	if (mapGeopista==null)
		selectMapInteractively();
	
	return mapGeopista==null?null:mapGeopista.getName();
	}

	

	/* (non-Javadoc)
	 * @see com.geopista.bean.GEOMapBean#getMapPath()
	 */
	public String getMapPath()
	{
	GeopistaMap map =getMap();
	if (map!=null && map.isExtracted())
		return map.getBasePath();
	else
		return null;
	}
	private void notifyCall()
	{
	JOptionPane.showMessageDialog(null,GeopistaUtil.where(2));
	}
	
 

	/* (non-Javadoc)
	 * @see com.geopista.bean.GEOFeatureBean#getSchema()
	 */
	public GeopistaSchema getSchema()
	{
	resetErrorMessages();
	return _getSchema();
	}
	private GeopistaSchema _getSchema()
	{
	
	if (activeLayer==null)
		{
		errorMessages.add(LAYER_NO_INICIALIZADO);
		return null;
		}
	String layerId=activeLayer.getName();
	GeopistaSchema sch= (GeopistaSchema) schemas.get(layerId);
	errorMessages.add(SCHEMA_NO_INICIALIZADO);
	return sch; 
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#extractMap(java.lang.String, java.lang.Double, java.lang.Double, java.lang.Double, java.lang.Double, java.lang.String)
	 */
	public GeopistaMap extractMap(String mapName, Double longIni, Double latIni, Double longEnd, Double latEnd, String format)
	{
	resetErrorMessages();
	if (mapGeopista!=null)
		{
		if (mapName.equals(mapGeopista.getName()))
			//return getMapProxy();
			return mapGeopista;
		}
	else
		{
		getMap();
		}
	return mapGeopista;
	
	}
	/**
	 * @return
	 */
	private GeopistaMapProxy getMapProxy()
	{
	mapProxy.setBasePath(mapGeopista==null?null:mapGeopista.getBasePath());
	mapProxy.setExtracted(mapGeopista==null?false:mapGeopista.isExtracted());
	mapProxy.setName(mapGeopista==null?null:mapGeopista.getName());
	
	return mapProxy;
	}
/**
 * Obtiene el mapa de pruebas en la modalidad de pruebas.
 */
	public GeopistaMap getMap()
	{
	resetErrorMessages();
	if (testing)
		if (mapGeopista==null)
			selectMapInteractively();
	return mapGeopista;
	}
	/**
	 * Open user dialog to select a map from Geopista repository or local storage
	 */
	private boolean selectMapInteractively()
	{
	resetErrorMessages();
	//TODO: Implementar recogida de errores 
	GeopistaListaMapasDialog listaMapasDialog = new GeopistaListaMapasDialog(null);
	if (testing)
		listaMapasDialog.setTitle("TEST-MODE");
	GEOPISTAWorkbenchContext ctx= new GEOPISTAWorkbenchContext(null)
	{
		public ErrorHandler getErrorHandler()
		{
		return null;
		}
	};
	PlugInContext context=new PlugInContext(ctx,null,null,null, null);
    // pedimos la ruta de mapa para cargarlo
    listaMapasDialog.getMap(context);

    // si la ruta es nula devolvemos false para que no se inicie el hilo de
    // carga
    
    if (testing)
    	{
    	mapGeopista=new GeopistaMap();
    	mapGeopista.setBasePath("c:\\test.dxf");
    	mapGeopista.setSystemId("testID");
    	}
    else{
    mapGeopista = listaMapasDialog.getMapGeopista();
    }    
    listaMapasDialog.dispose();
    if (mapGeopista==null)
    	{
    	errorMessages.add(CARGA_DEL_MAPA_CANCELADA);
    	return false; // Cancelado
    	}
    else
    	return true;
	}
	public GeopistaFeature getFeature()
	{
	resetErrorMessages();
	return _getFeature();
	}
	public GeopistaFeature _getFeature()
	{
	
	if (tempFeature==null)
		createWorkFeature();
	return tempFeature;
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#deleteFeature(java.lang.String)
	 */
	public boolean removeFeature(String sysId)
	{
	    
	    //Se crea la nueva Feature
	    FeatureSchema fs= this.getSchema();
	    GeopistaFeature gf=new GeopistaFeature(fs);	    
	    gf.setSystemId(sysId);
	   
	    Collection cf = new ArrayList();
	    cf.add(gf);
	   
	    FeatureEvent fe = new FeatureEvent(cf, FeatureEventType.DELETED, activeLayer, null);
	    	    
	    //Log
	    LogFeatutesEvents lfe = activeLayer.getLogger();
	    lfe.processEvent(fe);
	    
	        
	    
	    return true;
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#modifiedFeature(java.lang.String)
	 */
	public void modifiedFeature(String sysId)
	{
	  
	    
	    //Se crea la nueva Feature
	    FeatureSchema fs= this.getSchema();
	    GeopistaFeature gf=new GeopistaFeature(fs);	    
	    gf.setSystemId(sysId);
	   
	    Collection cf = new ArrayList();
	    cf.add(gf);
	   
	    FeatureEvent fe = new FeatureEvent(cf, FeatureEventType.ATTRIBUTES_MODIFIED, activeLayer, null);
	    	    
	    //Log
	    LogFeatutesEvents lfe = activeLayer.getLogger();
	    lfe.processEvent(fe);
	    
	    
	   
	    
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#checkFeature(boolean)
	 */
	public boolean checkFeature(Boolean interactive)
	{
	SchemaValidator val=new SchemaValidator((GeopistaContext) AppContext.getApplicationContext());
	boolean result= val.validateFeature(_getFeature());
	if (result==false)
		{
		
		for (Iterator iter = val.getErrorListIterator(); iter.hasNext();)
			{
			ValidationError element = (ValidationError) iter.next();
			errorMessages.add(element.message);
			}
		}
	return result;
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#getMapFormat()
	 */
	public String getMapFormat()
	{
	
	return "DXF";
	}

	

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#syncMap()
	 */
	public boolean syncMap()
	{
	    boolean allCorrect = true;
	// TODO Auto-generated method stub
	    Collection localLayers = layers.values();
	    Iterator localLayersIterator = localLayers.iterator();
	    while (localLayersIterator.hasNext())
	    {
	        GeopistaLayer currentLayer = (GeopistaLayer)localLayersIterator.next();
	        try
		    {
		        currentLayer.getLogger().save();
		    }catch(Exception e)
		    {
		        e.printStackTrace();
		        allCorrect = false;
		        errorMessages.add(SAVE_LOGGER_PROBLEM+" "+currentLayer.getName());
		    }
	        
	    }
	    return allCorrect;
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#getGeometry()
	 */
	public Geometry getGeometry()
	{
	resetErrorMessages();
	return tempFeature==null?null:tempFeature.getGeometry();
	}
/**
* vacía la lista de errores de la operación
 */
private void resetErrorMessages()
{
errorMessages.clear();
}
	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#getWKTGeometry()
	 */
	public String getWKTGeometry()
	{
	    resetErrorMessages();
	    
	    String wr;
	    WKTWriter writer = new WKTWriter();
	    wr=writer.write(getGeometry());
	    	    
	    return wr; 
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#setGeometry(java.lang.String)
	 */
	public boolean setGeometry(String geometryWKT)
	{
	    resetErrorMessages();
	    
	    WKTReader reader = new WKTReader();
	    
	    try{
	        
	        tempFeature.setGeometry(reader.read(geometryWKT));
	        
	    }catch(ParseException pe)
	    {
	        pe.printStackTrace();
	        return false;
	        
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	        return false;
	    }
	    
	
	
	return true;
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#getLayers()
	 */
	public GeopistaLayer[] getLayers()
	{
	GeopistaLayer[] layerarray=new GeopistaLayer[0];
	return (GeopistaLayer[]) getMap().getLayerManager().getLayers().toArray(layerarray);
	}

	/* La implementación actual asume que el mapa se encuentra en
	 * un path fijo predefinido. GeopistaDatos\export.dxf
	 * 
	 * @see com.geopista.bean.IGEOPISTABean#selectMap()
	 */
	public GeopistaMap selectMap()
	{
	resetErrorMessages();
	boolean acepted=true;//selectMapInteractively();
	mapGeopista=new GeopistaMap();
	mapGeopista.setBasePath(AppContext.getApplicationContext().getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,"/",false));
	mapGeopista.setName("extract.dxf");
	mapGeopista.setExtracted(true);
	mapGeopista.setSystemMap(true);
	mapGeopista.setProjectFile(new File(AppContext.getApplicationContext().getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,"/",false)+"extract.dxf"));

	//TODO: Informar de los errores ocurridos. El caso de Cancelación hay que anotarlo en la lista de mensajes.
	return acepted?mapGeopista:null;
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#setSchema(java.lang.String)
	 */
	public boolean setSchema(String XMLGeopistaSchema)
	{
	resetErrorMessages();
	
	if (activeLayer==null)
		{
		errorMessages.add(LAYER_NO_INICIALIZADO);
		return false;
		}
    try
		{
			Reader reader = new StringReader(XMLGeopistaSchema);
			
			    XML2Java converter = new XML2Java();
			    GeopistaSchema sch = (GeopistaSchema) converter.read(reader, GeopistaSchema.class);
			    
			setSchema(sch);
			
		}
	catch (Exception e)
		{
		
		logger.error("setSchema(String): No se puede parsear el esquema.", e);
		return false;
		}
	return true;
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#newFeature(java.lang.String)
	 */
	public boolean newFeature(String handle)
	{
	    
	    contador++;   
	    
	    //Identificador de la feature
	    String id;
	    
	    if (handle.equals("NoInicializado"))
	    {
	        id= handle + System.currentTimeMillis() + contador;
	    }
	    else
	    {
	        id= handle;
	    }
	    
	    //Se crea la nueva Feature	       
	    FeatureSchema fs= this.getSchema();
	    GeopistaFeature gf=new GeopistaFeature(fs);
	    gf.setSystemId(id);
	    
	    Collection cf = new ArrayList();
	    cf.add(gf);
	    
	    //Se añade al conjunto de features existentes
	    FeatureEvent fe = new FeatureEvent(cf, FeatureEventType.ADDED, activeLayer, null);
	    
	    
	    //Log
	    LogFeatutesEvents lfe = activeLayer.getLogger();
	    lfe.processEvent(fe);
	    
	    
	    
	return false;
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#checkGeometry(com.vividsolutions.jts.geom.Geometry)
	 */
	public boolean checkGeometry(Geometry geometry)
	{
	    //Compruba si la geometria que se le pasa por parámetro es compatible con la de la capa
	    
	    if (getSchema() instanceof GeopistaSchema)
        {
            GeometryFactory factory = new GeometryFactory();
            GeopistaSchema geopistaSchema = (GeopistaSchema) getSchema();
            int geometryIndex = getSchema().getGeometryIndex();
            String name = getSchema().getAttributeName(geometryIndex);

            //GEOMETRYTYPE -> 1:point   //3: linestring   //5: polygon    //6:collection    //0: geometry
            //geopistaSchema.getColumnByAttribute(geometryIndex).getTable().setGeometryType(1);
            int layerGeometryType = geopistaSchema.getColumnByAttribute(
                    geometryIndex).getTable().getGeometryType();

            
            switch (layerGeometryType)
                {
                case Table.COLLECTION:
                    if (geometry.getClass() != GeometryCollection.class)
                    {
                        Geometry[] arrayGeometryCollection = new Geometry[] { geometry };
                        geometry = factory
                                .createGeometryCollection(arrayGeometryCollection);
                        setAttribute(name, geometry);
                        return true;
                    }
                    else
                    {
                        return true;
                    }
                   

                case Table.MULTIPOLYGON:
                    if (geometry.getClass() == Polygon.class)
                    {
                        Polygon[] arrayGeometryMultiPoligon = new Polygon[] { (Polygon) geometry };
                        geometry = factory
                                .createMultiPolygon(arrayGeometryMultiPoligon);
                        
                        setAttribute(name, geometry);
                        return true;
                    } else if (geometry.getClass() == MultiPolygon.class)
                    {
                        return true;
                        
                    } else
                    {
                        return false;
                        //throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                    
                    }
                   
                case Table.MULTILINESTRING:
                    if (geometry.getClass() == LineString.class)
                    {
                        LineString[] arrayGeometryMultiLineString = new LineString[] { (LineString) geometry };
                        geometry = factory
                                .createMultiLineString(arrayGeometryMultiLineString);
                        
                        setAttribute(name, geometry);
                        return true;
                        
                    } else if (geometry.getClass() == MultiLineString.class)
                    {
                        return true;
                        
                    } else
                    {
                        return false;
                        //throw new IllegalArgumentException();
                    }
                    

                case Table.MULTIPOINT:
                    if (geometry.getClass() == Point.class)
                    {
                        Point[] arrayGeometryMultiPoint = new Point[] { (Point) geometry };
                        geometry = factory
                                .createMultiPoint(arrayGeometryMultiPoint);
                        
                        setAttribute(name, geometry);
                        return true;
                        
                    } else if (geometry.getClass() == MultiPoint.class)
                    {
                        return true;	
                        
                    } else
                    {
                        return false;
                        //throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                        
                    }
                    
                case Table.POINT:
                    if (geometry.getClass() != Point.class)
                    {
                        return false;
                        //throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                    }
                    else
                    {
                        return true;
                    }
                   

                case Table.LINESTRING:
                    if (geometry.getClass() != LineString.class)
                    {
                        return false;
                        //throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                        
                    }
                    else
                    {
                        return true;
                    }
                   

                case Table.POLYGON:
                    if (geometry.getClass() != Polygon.class)
                    {
                        return false;
                        //throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                    }
                    else
                    {
                        return true;
                    }
                    
                case Table.GEOMETRY:
                    return true;

                default:
                    throw new IllegalArgumentException("Geometria desconocida");
                }
        } 	    

	    return false;
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#checkGeometry(java.lang.String)
	 */
	public boolean checkGeometry(String WKTgeom)
	{
	    //GeopistaBeanProxy gbp= new GeopistaBeanProxy();
	    
	    
	    this.setGeometry(WKTgeom);
	    Geometry geom = this.getGeometry();
	    
	    return checkGeometry(geom);
	    
	}

	/* (non-Javadoc)
	 * @see com.geopista.bean.IGEOPISTABean#getErrorMessages()
	 */
	public String[] getErrorMessages()
	{
	final String[] a=new String[0]; // para informar del tipo a la función toArray
	return (String[]) errorMessages.toArray(a);
	}
}
