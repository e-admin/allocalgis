package com.geopista.server.administradorCartografia;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.WMSLayer;


public class ACWMSLayer implements Serializable{

    private static final long	serialVersionUID	= 5351002207616871739L;
    private Integer id;
    private String service;
    private String url;
    
    private String name;

    private List params;
    private String srs;
    private String format;
    private String version;
    private boolean isActiva;
    private boolean isVisible;
    private int position;
    /**Tabla hash con los estilos seleccionados por el usuario para cada capa*/
    private HashMap styles;

    public ACWMSLayer(int id, String service, String url, String params, String srs, String format, 
    		String version, boolean activa, boolean visible, int position, String styles, String wmsName) {
        this.id = new Integer(id);
        this.service = service;
        this.url = url;
        this.params = buildLayerNamesList(params);
        this.srs = srs;
        this.format = format;
        this.version = version;
        this.isActiva = activa;
        this.isVisible = visible;
        this.position = position;
        this.styles=buildStylesMap(styles);
        this.name=wmsName;
    }

    public ACWMSLayer(WMSLayer layer, int positionOnTheMap) {
        if(layer.getId() != null)
            this.id = layer.getId();

        this.service = "wms";
        this.url = layer.getServerURL();
        this.params = layer.getLayerNames();
        this.srs = layer.getSRS();
        this.format = layer.getFormat();
        this.version = layer.getWmsVersion();
        this.isActiva = layer.isVisible();
        this.isVisible = layer.isVisible();
        this.position = positionOnTheMap;
        this.styles=layer.getSelectedStyles();
        this.name=layer.getName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isActiva() {
        return isActiva;
    }

    public void setActiva(boolean activa) {
        isActiva = activa;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public List getParams() {
        return params;
    }

    public void setParams(List params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private List buildLayerNamesList(String commaSeparatedLayerNamesList){
        ArrayList layerNames = new ArrayList();
        if(commaSeparatedLayerNamesList.indexOf(",") < 0){
            layerNames.add(commaSeparatedLayerNamesList);
        }
        else{
            String[] names = commaSeparatedLayerNamesList.split(",");
            for(int i=0; i<names.length; i++){
                layerNames.add(names[i]);
            }
        }
        return layerNames;
    }
    
    
    private HashMap buildStylesMap(String styles){
    	HashMap stylesMap=new HashMap();
    	if(styles!=null){
    	if(styles.indexOf(",") < 0){//si sólo hay una capa y un estilo
    		String layerName=(String) this.params.get(0);
    		stylesMap.put(layerName,styles);
        }
    	else{
    		 String[] theStyles = styles.split(",");
             for(int i=0; i<theStyles.length; i++){
            	 String layerName=(String) params.get(i);
            	 System.out.println(theStyles[i]);
                 stylesMap.put(layerName,theStyles[i]);
             }	
    	}
    		
    	}//fin if
    	return stylesMap;	
    }
    

    public String getCommaSeparatedLayerNamesList(){
        String list = "";

        for (Iterator it = params.iterator(); it.hasNext();){
            if(list.equals(""))
               list = (String)it.next();
            else
                list = list + "," + (String)it.next();
        }

        return list;
    }
    
    
    
    
    public String getCommaSeparatedStylesList(){
        String list = "";
       if(styles!=null){
    	 
        for (Iterator it = params.iterator(); it.hasNext();){
        	String layerName=(String)it.next();
        	 String style = (String) styles.get(layerName);
        	 
        	 if(style==null){
        		 list+="default";
        		 if(it.hasNext())
        			 list+=",";
        	 }
        	 
        	 else{
        		 if(it.hasNext())
        			 list+=style+",";
        		 else
        			 list+=style;
        	 }
        		 
            
        }//fin del for
       
       }
        return list;
    }
    
    

    public void convert(ILayerManager layerManager) {
    	
        try{
//            String name =  (String)params.get(0); //El nombre de la Categoria sera el primero de las capas WMS referenciadas
        	
//        	String name = layerManager.
            layerManager.addCategory(name); //Se debe a que las posiciones empiezan desde 0
            WMSLayer wmsLayer=new WMSLayer(layerManager, url, srs, params , format, version,styles, name);
            wmsLayer.setId(this.id);
            wmsLayer.setVisible(this.isVisible);
            layerManager.addLayerable(name, wmsLayer, position-1);
        }
        catch(Exception e){
        	
        	//Aunque falle al cargar la capa la añadimos al panel
        	//En algun momento funcionara.
             try {
				WMSLayer wmsLayer= new WMSLayer(layerManager, url, srs, params , format, version,styles, name,false);
				 wmsLayer.setId(this.id);
				 wmsLayer.setVisible(this.isVisible);
				 layerManager.addLayerable(name, wmsLayer, position-1);
			} catch (IOException e1) {
				// TODO Auto-generated catch blockº
				e1.printStackTrace();
			}        	
             e.printStackTrace();
        }
    }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}


}
