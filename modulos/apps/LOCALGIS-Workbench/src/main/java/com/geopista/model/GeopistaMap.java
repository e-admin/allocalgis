/**
 * GeopistaMap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.Task;

/**
 * @author juacas
 *
 */
public class GeopistaMap extends Task implements IGeopistaMap , Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3177036775326150504L;
    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();

	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(GeopistaMap.class);
	
	private boolean isExtracted=false; // true si el mapa está extraído en almacenamiento local
	private Date timeStamp=null; // Timestamp del guardado
	private Image thumbnail=null;
	private String basePath;
	private String systemId="";
	private HashMap orderLayers = new HashMap();
	private boolean isSystemMap = false;
	private Date extractionTimeStamp = null;
	private String geometryEnvelope = "";
    private int idMunicipio; 
    private int idEntidad; 
    
    private String idEntidadSeleccionada;
 	
	/**
	 * Mapa compuesto por un conjunto de capas y una descripción SLD de estilos de pintado
	 * a partir del cual se configuran los estilos
	 */
	public GeopistaMap() {
		//super();
	    setIdMunicipio(AppContext.getIdMunicipio());
		setIdEntidad(AppContext.getIdEntidad());
//    layerManager.setCoordinateSystem(PredefinedCoordinateSystems.createUTMNorth(30));
	}

  public GeopistaMap(WorkbenchContext context) {
		//super();
    setIdMunicipio(AppContext.getIdMunicipio());
	setIdEntidad(AppContext.getIdEntidad());
//    layerManager.setCoordinateSystem(PredefinedCoordinateSystems.createUTMNorth(30));
    layerManager.addLayerListener((LayerListener) context.getWorkbench());

	}



/**
 * Recrea el mapa a partir de un directorio local
 * @param FileName
 * @return
 * @throws Exception
 */
public static GeopistaMap getMap(String FileName) throws Exception
{
	File file = new File(FileName);
	String basePath = file.getParent();
    FileReader reader = null;
    GeopistaMap sourceMap = null;

    try
    {
     
      reader = new FileReader(file);
      XML2Java converter = new XML2Java();
          converter.addCustomConverter(Date.class, GeopistaMapCustomConverter.getMapDateCustomConverter());

      sourceMap = (GeopistaMap) converter.read(reader, com.geopista.model.GeopistaMap.class);
      sourceMap.setBasePath(basePath);
    }
    catch(Exception e)
	{
		if (logger.isDebugEnabled())
		{
		logger.debug("getMap(String) - El fichero " + FileName
				+ "no se puede leer. " + e.getLocalizedMessage());
		}
		throw e;
	}
    finally {
    	reader.close();
    }

    return sourceMap;
}

public static GeopistaMap getMapUTF8(String FileName) throws Exception
{
	  File file = new File(FileName);
	  String basePath = file.getParent();
	  GeopistaMap sourceMap = null;   
	  InputStream is;
	  try {

		  is = new FileInputStream(file);

		 String stringReader = FileUtil.parseISToStringUTF8(is);
		
		  XML2Java converter = new XML2Java();
		  converter.addCustomConverter(Date.class, GeopistaMapCustomConverter.getMapDateCustomConverter());

		  sourceMap = (GeopistaMap) converter.read(stringReader, com.geopista.model.GeopistaMap.class);
		  sourceMap.setBasePath(basePath);
	  }
	  catch(Exception e)
	  {
		  is=null;
		  if (logger.isDebugEnabled())
		  {
			  logger.debug("getMap(String) - El fichero " + FileName
					  + "no se puede leer. " + e.getLocalizedMessage());
		  }
		  throw e;
	  }
	  
	  return sourceMap;
}





public static GeopistaMap getMapISO88591(String FileName) throws Exception
{
	  File file = new File(FileName);
	  String basePath = file.getParent();
	  GeopistaMap sourceMap = null;    
	  try {

		  InputStream is = new FileInputStream(file);

		  String stringReader = FileUtil.parseISO88591(is);
		  
		  XML2Java converter = new XML2Java();
		  converter.addCustomConverter(Date.class, GeopistaMapCustomConverter.getMapDateCustomConverter());

		  sourceMap = (GeopistaMap) converter.read(stringReader, com.geopista.model.GeopistaMap.class);
		  sourceMap.setBasePath(basePath);
	  }
	  catch(Exception e)
	  {
		  if (logger.isDebugEnabled())
		  {
			  logger.debug("getMap(String) - El fichero " + FileName
					  + "no se puede leer. " + e.getLocalizedMessage());
		  }
		  throw e;
	  }
	  
	  return sourceMap;
}

  
  private GeopistaLayerManager layerManager=new GeopistaLayerManager();
  private String description;
  private String mapUnits="";
  private String mapScale="";
  private String mapProjection="";
  private String mapSrid="";

private TaskComponent taskFrame=null;

  public ILayerManager getLayerManager() {
        return layerManager;
    }
    
	public static void main(String args[]) throws Exception
	{
		StringWriter stringWriter = new StringWriter();
		GeopistaMap map = new GeopistaMap();
		map.setName("Prueba");
		map.setExtracted(true);
		map.setTimeStamp(new Date());
	        try {
	            Java2XML converter =new Java2XML();
	            converter.addCustomConverter(Date.class, GeopistaMapCustomConverter.getMapDateCustomConverter());
         
	            converter.write(map, "GEOPISTAMap", stringWriter);
	        } finally {
	            stringWriter.flush();
	        }
			if (logger.isDebugEnabled())
			{
			logger.debug("main(String)" + stringWriter.toString());
			}
	}
	/**
	 * @return Returns the isExtracted.
	 */
	public boolean isExtracted() {
		return isExtracted;
	}
	/**
	 * @param isExtracted The isExtracted to set.
	 */
	public void setExtracted(boolean isExtracted) {
		this.isExtracted = isExtracted;
	}
	/**
	 * @return Returns the timeStamp.
	 */
	public Date getTimeStamp() {
		// devuelve el timeStamp del guardado.
		//return timeStamp==null?timeStamp=new Date():timeStamp;
		return this.timeStamp;
	}
	/**
	 * @param timeStamp The timeStamp to set.
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String newDescription)
  {
  	description = newDescription;
  }
	/**
	 * @return Returns the thumbnail.
	 */
	public Image getThumbnail()
	{
      if(thumbnail ==null)
      {
        thumbnail = IconLoader.icon("app-icon.gif").getImage();
      }
  		return thumbnail;
	}

  public void loadThumbnail()
  {
   
        try
        {
          File rutaThumb = new File(this.getBasePath(),"thumb.png");
          thumbnail  = new ImageIcon(rutaThumb.getAbsolutePath()).getImage();
        }catch(Exception e)
        {
          thumbnail = IconLoader.icon("app-icon.gif").getImage();
        }
          
  }

  
	/**
	 * @param thumbnail The thumbnail to set.
	 */
	public void setThumbnail(Image thumbnail)
	{
		this.thumbnail = thumbnail;
	}
	/**
	 * @return Returns the basePath.
	 */
	public String getBasePath()
	{
		return basePath;
	}
	/**
	 * @param basePath The basePath to set.
	 */
	public void setBasePath(String basePath)
	{
		this.basePath = basePath;
	}
	/**
	 * @return Returns the systemId.
	 */
	public String getSystemId()
	{
		return systemId;
	}
	/**
	 * @param systemId The systemId to set.
	 */
	public void setSystemId(String systemId)
	{
		this.systemId = systemId;
	}

  public String getMapUnits()
  {
    return mapUnits;
  }

  public void setMapUnits(String newMapUnits)
  {
    mapUnits = newMapUnits;
  }

  public String getMapScale()
  {
    return mapScale;
  }

  public void setMapScale(String newMapScale)
  {
    mapScale = newMapScale;
  }
  /**
   * devuelve el nombre de la proyección utilizada en el mapa.
   * @return
   */
	public String getMapProjection()
	{
	return mapProjection;
	//return getMapCoordinateSystem().getName();
	}
	
	public String getMapSrid() {
	return mapSrid;
	}
	
	public void setMapSrid(String mapSrid) {
		this.mapSrid = mapSrid;
	}


/**
 * Configura la proyección utilizada en el mapa
 * Sucesivas adiciones de capas provocarán la reproyección de su contenido.
 * @param coo
 */
public void setMapCoordinateSystem(CoordinateSystem coo)
{
getLayerManager().setCoordinateSystem(coo);

}
  public CoordinateSystem getMapCoordinateSystem()
  {
    return getLayerManager().getCoordinateSystem();
  }

 
  public void setMapProjection(String name)
  {
 
	  mapProjection=name;
  }

  public String getDescriptor()
  {
    Element root = new Element("mapDescriptor");
    Element description = new Element("description");
    Element localMapUnits = new Element("mapUnits");
    Element localMapScale = new Element("mapScale");
    Element localMapProjection = new Element("mapProjection");
    Element localMapSrid = new Element("mapSrid");
    Element localMapName = new Element("mapName");        

    root.addContent(description);
    root.addContent(localMapUnits);
    root.addContent(localMapScale);
    root.addContent(localMapProjection);
    root.addContent(localMapSrid);
    root.addContent(localMapName);    

    description.setText(this.description);
    localMapUnits.setText(this.mapUnits);
    localMapScale.setText(this.mapScale);
    localMapProjection.setText(getMapProjection());
    localMapSrid.setText(getMapSrid());
    localMapName.setText(super.getName());
    
    Document doc = new Document(root); 

    XMLOutputter serializer = new XMLOutputter();
    String outputXml = serializer.outputString(doc);


    return outputXml;
  }

  public void setDescriptor(String xmlDescriptor) throws JDOMException, IOException
  {

    SAXBuilder builder = new SAXBuilder(false);
    InputStream inStream = new ByteArrayInputStream(xmlDescriptor.getBytes());
    Document doc = builder.build(inStream);
    Element raiz = doc.getRootElement();
    Element descriptionElement = raiz.getChild("description");
    Element mapUnitsElement = raiz.getChild("mapUnits");
    Element mapScaleElement = raiz.getChild("mapScale");
    Element mapProjectionElement = raiz.getChild("mapProjection");
    Element mapSrid = raiz.getChild("mapSrid");
    Element mapNameElement = raiz.getChild("mapName");    

    String localDescription = descriptionElement.getText();
    String localMapUnits = mapUnitsElement.getText();
    String localMapScale = mapScaleElement.getText();
    String localMapProjection = mapProjectionElement.getText();
    String srid = mapSrid.getText();
    String localMapName = mapNameElement.getText();

    this.description = localDescription;
    this.mapUnits = localMapUnits;
    this.mapScale = localMapScale;
    setMapProjection(localMapProjection);
    setMapSrid(srid);
    super.setName(localMapName);
  }

  public List getSystemCategories()
  {
    return layerManager.getSystemCategories();
  }

  public Collection getLayersStylesRelation()
  {
      ArrayList stylesRelations = new ArrayList();
      List systemLayers = layerManager.getSystemCategories();
      Iterator systemLayersIter = systemLayers.iterator();
      while(systemLayersIter.hasNext())
      {
        LayerFamily actualLayerFamily = (LayerFamily) systemLayersIter.next();

        String idLayerFamily = actualLayerFamily.getSystemId();
        
        List layerFamilyLayers = actualLayerFamily.getLayerables();
        Iterator layerFamilyLayersIter = layerFamilyLayers.iterator();
        while(layerFamilyLayersIter.hasNext())
        {
          GeopistaLayer actualLayer = (GeopistaLayer)layerFamilyLayersIter.next();

          LayerStyleData layerStyleData = new LayerStyleData();
          String idLayer = actualLayer.getSystemId();
          SLDStyle sldStyle = (SLDStyle) actualLayer.getStyle(SLDStyle.class);
          String styleName = null;
          String idStyle = null;
          if(sldStyle!=null)
          {
            styleName = sldStyle.getCurrentStyleName();
            idStyle = sldStyle.getSystemId();
          }
          
          String layerPosition = String.valueOf(layerManager.indexOf(actualLayer));
          layerStyleData.setIdLayer(idLayer);
          layerStyleData.setIdLayerFamily(idLayerFamily);
          layerStyleData.setIdStyle(idStyle);
          layerStyleData.setLayerPosition(layerPosition);
          layerStyleData.setStyleName(styleName);
          layerStyleData.setActive(actualLayer.isActiva());
          layerStyleData.setVisible(actualLayer.isVisible());
          layerStyleData.setEditable(actualLayer.isEditable());
          stylesRelations.add(layerStyleData);
        }
      }
      return stylesRelations;
  }

/**
 * @param frame
 */
public void setTaskFrame(TaskComponent frame)
{
	this.taskFrame=frame;	
}
public TaskComponent getTaskComponent()
{
	return taskFrame;
}




public void setOrdersLayer(Map orderLayers) {
    this.orderLayers = (HashMap)orderLayers;
    
}

public Map getOrdersLayer() {
    //This method needs to be public because it is called by Java2XML [Jon Aquino 11/13/2003]
    
    //I was returning a Collections.unmodifiableMap before, but
    //Java2XML couldn't open it after saving it (can't instantiate
    //java.util.Collections$UnmodifiableMap). [Jon Aquino]
    List listOrderLayers = this.getLayerManager().getLayerables(Layerable.class);
    Iterator listOrderLayersIterator = listOrderLayers.iterator();
    int positionCounter = 0;
    while(listOrderLayersIterator.hasNext())
    {
    Object layerable = listOrderLayersIterator.next();
    if (layerable instanceof GeopistaLayer)
		{
		IGeopistaLayer currentLayer = (IGeopistaLayer) layerable;

        orderLayers.put(currentLayer.getSystemId(),new Integer(positionCounter++));
		}
    else
    	{
    	Layerable currentLayer = (Layerable) layerable;

        orderLayers.put(currentLayer.getName(),new Integer(positionCounter++));
		
    	}
       
    }
    return orderLayers;
}

public static GeopistaMap getMapDXF(String FileName) throws Exception
{
	
	File file = new File(FileName);
	String basePath = file.getParent();
	String name = file.getName();
	
	String upperName = name.toUpperCase();
	String[] lstName = upperName.split(".DXF");
	upperName = lstName[0];
	GeopistaMap sourceMap = new GeopistaMap();

	sourceMap.setBasePath(basePath);
	sourceMap.setDescription(upperName);
	
	sourceMap.setName(name);
	sourceMap.setSystemId(upperName);
	
	return sourceMap;
}

    /**
     * @return Returns the isSystemMap.
     */
    public boolean isSystemMap()
    {
        return isSystemMap;
    }
    /**
     * @param isSystemMap The isSystemMap to set.
     */
    public void setSystemMap(boolean isSystemMap)
    {
        this.isSystemMap = isSystemMap;
    }
    /**
     * @return Returns the extractionTimeStamp.
     */
    public Date getExtractionTimeStamp()
    {
        return extractionTimeStamp;
    }
    /**
     * @param extractionTimeStamp The extractionTimeStamp to set.
     */
    public void setExtractionTimeStamp(Date extractionTimeStamp)
    {
        this.extractionTimeStamp = extractionTimeStamp;
    }
    /**
     * @return Returns the geometryEnvelope.
     */
    public String getGeometryEnvelope()
    {
        return geometryEnvelope;
    }
    /**
     * @param geometryEnvelope The geometryEnvelope to set.
     */
    public void setGeometryEnvelope(String geometryEnvelope)
    {
        this.geometryEnvelope = geometryEnvelope;
    }

    /**
     * @return Returns the idMunicipio.
     */
    public int getIdMunicipio()
    {
        return idMunicipio;
    }

    /**
     * @param idMunicipio The idMunicipio to set.
     */
    public void setIdMunicipio(int idMunicipio)
    {
        this.idMunicipio = idMunicipio;
    }
    
    /**
     * @return Returns the idEntidad.
     */
    public int getIdEntidad()
    {
        return idEntidad;
    }

    /**
     * @param idEntidad The idEntidad to set.
     */
    public void setIdEntidad(int idEntidad)
    {
        this.idEntidad = idEntidad;
    }


    public static GeopistaMap getResumeMap(String FileName) throws Exception
	{
	
		File file = new File(FileName);
		String basePath = file.getParent();
		String name = file.getName();
	
		String upperName = name.toUpperCase();
		String[] lstName = upperName.split(".DXF");
		upperName = lstName[0];
		GeopistaMap sourceMap = new GeopistaMap();
	
		sourceMap.setBasePath(basePath);
		sourceMap.setDescription(upperName);
	
		sourceMap.setName(name);
	
		return sourceMap;
	}
    
    public String getIdEntidadSeleccionada() {
		return idEntidadSeleccionada;
	}

	public void setIdEntidadSeleccionada(String idEntidadSeleccionada) {
		this.idEntidadSeleccionada = idEntidadSeleccionada;
	}

	public static class Collator1Comparator implements Comparator  {

        private Collator collator= Collator.getInstance(new java.util.Locale("es", "ES"));

        public int compare(Object obj1, Object obj2) {
            CollationKey key1= collator.getCollationKey(((GeopistaMap)obj1).getName());
            CollationKey key2= collator.getCollationKey(((GeopistaMap)obj2).getName());
            return key1.compareTo(key2);
        }
    }
    public static class Collator2Comparator implements Comparator  {

        private Collator collator= Collator.getInstance(new java.util.Locale("es", "ES"));

        public int compare(Object obj1, Object obj2) {
            CollationKey key1= collator.getCollationKey(((GeopistaMap)obj1).getName());
            CollationKey key2= collator.getCollationKey(((GeopistaMap)obj2).getName());
            int diferencia=key1.compareTo(key2);
            if (diferencia>0)
            	return -1;
            else if (diferencia<0)
            	return 1;
            else
            	return 0;

        }
    }
}
