package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.model.DynamicLayer;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.workbench.model.LayerManager;



/** Datos de una capa para el interfaz con el administrador de cartografia */
public class ACDynamicLayer extends ACLayer implements Serializable, IACDynamicLayer{
	
	private static final Log	logger	= LogFactory.getLog(ACDynamicLayer.class);


    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private Integer id;
    private String service;
    private String url;
    private List params = new ArrayList();
    private String srs;
    private String format;
    private String version;
    private String time;
    private boolean isActiva;
    private int position;
    
    /**Tabla hash con los estilos seleccionados por el usuario para cada capa*/
    private HashMap styles = new HashMap();;

	private static final long	serialVersionUID	= 5351002207616871739L;
    
	public ACDynamicLayer(int id,String name,String systemName,String selectQuery,String url,String time){
    	this(id,name,systemName,selectQuery,url);
    	this.time = time;
    }
    
	public ACDynamicLayer(int id,String name,String systemName,String selectQuery,String url){
	    super(id,name,systemName,selectQuery);
	    try{
	    	StringTokenizer st = new StringTokenizer(url,"?");
	    	if (st.hasMoreTokens()){
	    		this.url = (String)st.nextToken()+"?";
	    		String request = (String)st.nextToken();
	    		String capa = searchToken(request,"LAYERS");
	    		this.params.add(capa);
	    		srs = searchToken(request,"SRS");
	    		format = searchToken(request,"FORMAT");
	        	version = searchToken(request,"VERSION");
	        	service = searchToken(request,"SERVICE");
	    		styles.put(capa, searchToken(request,"STYLES"));
	    	}
	    	else{
	    		url = "";
				srs = "EPSG:23030";
				format = "image/png";
		    	version = "1.1.1";
		    	service = "wms";
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }


    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDynamicLayer#convertWMS(com.vividsolutions.jump.workbench.model.LayerManager, com.vividsolutions.jump.feature.FeatureDataset)
	 */
    @Override
	public void convertWMS(LayerManager layerManager, FeatureDataset features) {
        try{
            String name =  (String)params.get(0); //El nombre de la Categoria sera el primero de las capas WMS referenciadas
            layerManager.addCategory(name); //Se debe a que las posiciones empiezan desde 0
            DynamicLayer dynamicLayer = convertDynamic(layerManager);
            dynamicLayer.setFeatureCollection(features);
            layerManager.addLayerable(name, dynamicLayer,this.positionOnMap);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDynamicLayer#convertDynamic(com.vividsolutions.jump.workbench.model.LayerManager)
	 */
    @Override
	public DynamicLayer convertDynamic(LayerManager layerManager){
        if (layerManager!=null)
        	srs = "EPSG:"+layerManager.getCoordinateSystem().getEPSGCode();
        
        logger.info("Loading Dynamic Layer:"+url);
    	DynamicLayer lRet=new DynamicLayer(url, srs, format, version,time,params,styles);
        lRet.setId_LayerDataBase(this.id_layer);
        lRet.setSystemId(this.systemName);
        lRet.setName(this.name);
        lRet.setActiva(this.isActive);
        lRet.setVisible(this.isVisible);
        lRet.setEditable(this.isEditable);
        lRet.setFieldExtendedForm(this.extendedForm);
        lRet.setRevisionActual(this.revisionActual);
        lRet.setVersionable(this.isVersionable);
        lRet.setTime(this.time);
        lRet.setUltimaRevision(this.getUltimaRevision());
        if (layerManager!=null){
            lRet.setLayerManager(layerManager);
            if (styleXML!=null)
                applyStyle(lRet,layerManager,styleXML);
        }
        return lRet;
    }
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDynamicLayer#getSelectedStyles()
	 */
	@Override
	public HashMap getSelectedStyles() {
		return styles;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDynamicLayer#setSelectedStyles(java.util.HashMap)
	 */
	@Override
	public /*static */void setSelectedStyles(HashMap ss) {
		styles = ss;
	}

	private String searchToken(String request,String token){
		StringTokenizer st = new StringTokenizer(request,"&");
		while(st.hasMoreTokens()){
			String subcadena = ((String)st.nextToken());
			if (subcadena.toUpperCase().startsWith(token)){
	    		String[] subcadenaTroz = subcadena.split("=");
	    		if (subcadenaTroz.length==2){
	    			return subcadenaTroz[1];
	    		}
			}
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDynamicLayer#getTime()
	 */
	@Override
	public String getTime() {
		return this.time;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDynamicLayer#setTime(java.lang.String)
	 */
	@Override
	public void setTime(String time) {
		this.time = time;
	}

}
