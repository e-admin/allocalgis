package com.geopista.server.administradorCartografia;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;
import com.geopista.model.LayerFamily;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.WMSLayer;

/** Datos de un mapa para el administrador de cartografia */
public class ACMap implements Serializable, IACMap{
    /**
	 * 
	 */
	private static final long	serialVersionUID	= 3675121612249989115L;
	String id;
    String name;
    String xml;
    byte[] image;
    Date timeStamp = null;
    int idMunicipio;
    int idEntidad;
    Hashtable layerFamilies=null;
    Collection layerStyles=null;
    //Hashtable MapServerLayers=null;
    Collection MapServerLayers=null;
    String idEntidadSeleccionada;
	String locale;
    
   


	public ACMap(){
    }

    public ACMap(GeopistaMap gpMap) throws IOException{
        this.id=gpMap.getSystemId();
        this.name = gpMap.getName();
        this.xml=gpMap.getDescriptor();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write((RenderedImage)toBufferedImage(gpMap.getThumbnail()),"png",baos);
        image=baos.toByteArray();
        this.timeStamp = gpMap.getTimeStamp();
        this.layerStyles=gpMap.getLayersStylesRelation();
        this.layerFamilies=new Hashtable();
        this.MapServerLayers = new ArrayList();
        this.idMunicipio=gpMap.getIdMunicipio();
        this.idEntidad=gpMap.getIdEntidad();
        this.idEntidadSeleccionada=gpMap.getIdEntidadSeleccionada();
        int iFamilyPos=0;
        
        
        boolean bFound = false;
        List lstLayerablesOriginal= gpMap.getLayerManager().getLayerables(Layerable.class);
        for (int i=0; i<lstLayerablesOriginal.size(); i++){
        	bFound = false;
        	Object anObjectTemp = (Object) lstLayerablesOriginal.get(i);
        	if (anObjectTemp instanceof GeopistaLayer){
        		// If it's a GeopistaLayer
        		GeopistaLayer aGeopistaLayer = (GeopistaLayer) anObjectTemp;
        		// We create an ACLayerFamily (This object will e added to the layerFamilies Hashtable) at the end of the process. 
        		ACLayerFamily anACLayerFamily = new ACLayerFamily();
        		// We do a loop on all the Layer Families that has the gpMap
        		for (Iterator it=((GeopistaLayerManager)gpMap.getLayerManager()).getCategories().iterator();it.hasNext();) {
        			LayerFamily aLayerFamily = (LayerFamily)it.next();
        			// if that Layer Family is a SystemLayerFamily
        			if(aLayerFamily.isSystemLayerFamily()){
        				ACLayerFamily anACLayerFamilyTemp = new ACLayerFamily(aLayerFamily);
        				anACLayerFamilyTemp.setName(aLayerFamily.getName());
        				List lstLayerablesTemp = aLayerFamily.getLayerables();
        				for (int j=0; j<lstLayerablesTemp.size(); j++){
        					GeopistaLayer aGeopistaLayerTemp = (GeopistaLayer) lstLayerablesTemp.get(j);
        					if (aGeopistaLayerTemp.getName().equalsIgnoreCase(aGeopistaLayer.getName())){
        						anACLayerFamily = anACLayerFamilyTemp; 
        						bFound = true; 
        					}
        				}
        				
        			}
        		}
        		if (bFound){
        			// We must check that the layer hasn't been added previously in the table
        			if (layerFamilies!=null){
        	            int iFamilies=layerFamilies.size();
        	            ACLayerFamily family=null;
        	            int index = 0;
        	            while (iFamilies>0){
        	            	family = (ACLayerFamily) layerFamilies.get(new Integer(index));
        	            	// We don't do a conventional for loop as the Hashtable layerFamilies can have indices setted to null. 
        	            	// Thanks to the indices, we can know the orden of the layers. 
        	            	// ex: If a layer is at the position 2 of the hashtable, it is the second layer shown to the user. 
        	            	if (family!=null){
        	            		if (family.getName().equalsIgnoreCase(anACLayerFamily.getName())){
        	            			bFound = false;
        	            		}
        	            		iFamilies--;
        	            	}
        	            	index++;
        	            }
        	        }
        			if (bFound){
        				layerFamilies.put(new Integer(++iFamilyPos), anACLayerFamily);
        			}
        		}
        	}else if(anObjectTemp instanceof WMSLayer){      
        		int iWMS = lstLayerablesOriginal.indexOf(anObjectTemp);
        		MapServerLayers.add(new ACWMSLayer((WMSLayer)anObjectTemp, iWMS+1 ));
        		
        	}
        }
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getLayerStyles()
	 */
    @Override
	public Collection getLayerStyles() {
        return layerStyles;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setLayerStyleData(java.util.Collection)
	 */
    @Override
	public void setLayerStyleData(Collection layerStyleData) {
        this.layerStyles = layerStyleData;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getLayerFamilies()
	 */
    @Override
	public Hashtable getLayerFamilies() {
        return layerFamilies;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setLayerFamilies(java.util.Hashtable)
	 */
    @Override
	public void setLayerFamilies(Hashtable layerFamilies) {
        this.layerFamilies = layerFamilies;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getId()
	 */
    @Override
	public String getId() {
        return id;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setId(java.lang.String)
	 */
    @Override
	public void setId(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getName()
	 */
    @Override
	public String getName() {
        return name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setName(java.lang.String)
	 */
    @Override
	public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getXml()
	 */
    @Override
	public String getXml() {
        return xml;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setXml(java.lang.String)
	 */
    @Override
	public void setXml(String xml) {
        this.xml = xml;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getImage()
	 */
    @Override
	public Object getImage() {
        return image;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setImage(byte[])
	 */
    @Override
	public void setImage(byte[] image) {
        this.image = image;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#gettimeStamp()
	 */
    @Override
	public Date gettimeStamp() {
		return timeStamp;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#settimeStamp(java.sql.Timestamp)
	 */
	@Override
	public void settimeStamp(java.sql.Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	// This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
            e.printStackTrace();
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#convert(java.util.Hashtable, java.util.Hashtable, java.util.Date, java.lang.String)
	 */
    @Override
	public GeopistaMap convert(Hashtable htLayers,Hashtable htStyleXMLs, Date hora, String sLocale){
        GeopistaMap mRet=new GeopistaMap();
        mRet.setIdEntidad(this.getIdEntidad());
        mRet.setSystemId(String.valueOf(this.id));
        mRet.setSystemMap(true);
        mRet.setName(this.name);
        mRet.setTimeStamp(hora);	// David
        mRet.setMapProjection(getMapProjection(this.xml));
        ILayerManager layerManager=mRet.getLayerManager();
        if (image!=null && image.length>0)
            mRet.setThumbnail(Toolkit.getDefaultToolkit().createImage((byte[])image));
        if (layerFamilies!=null){
            int iFamilies=layerFamilies.size();
            ACLayerFamily family=null;
            int index = 0;
            int posicion = 1;
            while (iFamilies>0){
            	family = (ACLayerFamily) layerFamilies.get(new Integer(index));
            	// We don't do a conventional for loop as the Hashtable layerFamilies can have indices setted to null. 
            	// Thanks to the indices, we can know the orden of the layers. 
            	// ex: If a layer is at the position 2 of the hashtable, it is the second layer shown to the user. 
            	if (family!=null){
            		family.convert(layerManager, posicion, htLayers, htStyleXMLs);
            		iFamilies--;
            		posicion++;
            	}
            	index++;
            }
        }

        /* if (layerFamilies!=null){
        ACLayerFamily family=null;
        for ( Enumeration enumerationElement = layerFamilies.elements(); enumerationElement.hasMoreElements();){
            family = (ACLayerFamily)enumerationElement.nextElement();
            family.convert(layerManager,i,htLayers,htStyleXMLs);
        }
        int iFamilies=layerFamilies.size();
        ACLayerFamily family=null;
        for (int i=0;i<iFamilies;i++){
            family=(ACLayerFamily)layerFamilies.get(new Integer(i));
            family.convert(layerManager,i,htLayers,htStyleXMLsR);
        }
    }   */
        
        
        if(MapServerLayers != null){
            ArrayList list = new ArrayList();
            for (Iterator it = MapServerLayers.iterator(); it.hasNext();){
            	Object object = it.next();
            	if (object instanceof ACWMSLayer){
                    ACWMSLayer acwmsLayer=null;
	                acwmsLayer=(ACWMSLayer)object;
	                if (acwmsLayer.getName()==null)
	                	acwmsLayer.setName("defaultwms");
	                acwmsLayer.convert(layerManager);
            	}else if (object instanceof ACDynamicLayer){
            		ACDynamicLayer dynamicLayer=null;
            		dynamicLayer=(ACDynamicLayer)object;
	                GeopistaSchema schema=dynamicLayer.buildSchema(sLocale);
	                FeatureDataset features=new FeatureDataset(schema);
	                dynamicLayer.convertWMS((LayerManager)layerManager,features);
            	}
             //   WMSLayer layer = acwmsLayer.convert(layerManager);
             //   if(layer != null)
             //       list.add(layer);
            }
            //if(!list.isEmpty())
             //   mRet.setWMSLayers(list);
        }

      
        
        return mRet;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getIdMunicipio()
	 */
    @Override
	public int getIdMunicipio()
    {
        return idMunicipio;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setIdMunicipio(int)
	 */
    @Override
	public void setIdMunicipio(int idMunicipio)
    {
        this.idMunicipio = idMunicipio;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getIdEntidad()
	 */
    @Override
	public int getIdEntidad()
    {
        return idEntidad;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setIdEntidad(int)
	 */
    @Override
	public void setIdEntidad(int idEntidad)
    {
        this.idEntidad = idEntidad;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getMapServerLayers()
	 */
    @Override
	public Collection getMapServerLayers() {
        return MapServerLayers;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setMapServerLayers(java.util.Collection)
	 */
    @Override
	public void setMapServerLayers(Collection mapServerLayers) {
        MapServerLayers = mapServerLayers;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getMapProjection(java.lang.String)
	 */
    @Override
	public String getMapProjection(String projection){
    	if (projection != null){
		    try{
		  	  //En esta descripción también se encuentra la proyección, por tanto, fijo la proyección
		  	  SAXBuilder builder = new SAXBuilder(false);
		  	  byte currentXMLBytes[] = projection.getBytes();
		  	  ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes); 
		  	  Document docNew = builder.build(byteArrayInputStream);
		  	  Element rootElement = docNew.getRootElement();
		  	  Element elemento = (Element)rootElement.getChild("mapProjection");
		  	  if (!elemento.getText().equals("")&&!elemento.getText().equals("Unspecified")){
		  		  return(elemento.getText());
		  	  }
		  	  return "";
		    }catch(JDOMException e){
		  	  e.printStackTrace();
		  	  return "";
		    }catch (Exception e){
		  	  e.printStackTrace();
		  	  return "";
		    }
    	}else
    		return "";
    	
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getIdEntidadSeleccionada()
	 */
    @Override
	public String getIdEntidadSeleccionada() {
		return idEntidadSeleccionada;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setIdEntidadSeleccionada(java.lang.String)
	 */
	@Override
	public void setIdEntidadSeleccionada(String idEntidadSeleccionada) {
		this.idEntidadSeleccionada = idEntidadSeleccionada;
	}    
	

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#getLocale()
	 */
	@Override
	public String getLocale() {
		return locale;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMap#setLocale(java.lang.String)
	 */
	@Override
	public void setLocale(String locale) {
		this.locale = locale;
	}
}
