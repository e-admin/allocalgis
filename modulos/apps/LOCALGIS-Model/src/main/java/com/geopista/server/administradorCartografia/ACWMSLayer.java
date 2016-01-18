/**
 * ACWMSLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.loadEIELData.vo.EIELLayer;
import com.geopista.model.IExternalLayer;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.IWMSLayer;



public class ACWMSLayer implements Serializable,IACWMSLayer{

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    public static final String EIEL_SERVICE="eiel";

	
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
    
    private boolean isTemplate;
    private String descripcion;
    //atributos que sólo se utilizarán para las capas externa
    private String layerId;
    private String table;
    private String idField;
    private String geometryField;

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

    public ACWMSLayer(IWMSLayer layer, int positionOnTheMap) {
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
    
    /**construye una ACWMSLayer a partir de una capa externa
     * @param exLayer Capa externa
     * @param position Posición de la capa en el mapa
     */
    public ACWMSLayer (IExternalLayer exLayer, int position) {
        this.service = EIEL_SERVICE;
        
        if(AppContext.getIdMunicipio()==0)
        	this.url=aplicacion.getString(UserPreferenceConstants.MAPSERVER_URL)+"//mapserver/"+"/eiel/mapserv?";
        
        else{
        	String ineMunicipio=String.valueOf(AppContext.getIdMunicipio());
        	String municipio=ineMunicipio.substring(2,ineMunicipio.length());
        	this.url =aplicacion.getString(UserPreferenceConstants.MAPSERVER_URL)+"//mapserver/"+municipio+"/eiel/mapserv?";
        }
                
        this.params = buildLayerNamesList (filterExternalLayerName(exLayer.getExternalId()));
        this.layerId=exLayer.getExternalId();
        this.srs =  "EPSG:23029";
        this.version="1.1.1";
        this.format = "image/png";
        this.isActiva = true;
        this.isVisible = exLayer.isVisible();
        this.position = position;
        this.styles=buildStylesMap("default");
        this.isTemplate=false;
        this.table=exLayer.getTabla();
        this.idField=exLayer.getId_column();
        this.geometryField=exLayer.getGeometry_column();
    }

    public ACWMSLayer(ACWMSLayerBase acWmsLayerBase) {
		
		this.id = acWmsLayerBase.getId();
        this.service = acWmsLayerBase.getService();
        this.url = acWmsLayerBase.getUrl();
        this.params = acWmsLayerBase.getParams();
        this.srs = acWmsLayerBase.getSrs();
        this.format = acWmsLayerBase.getFormat();
        this.version = acWmsLayerBase.getVersion();
        this.isActiva = acWmsLayerBase.isActiva();
        this.isVisible = acWmsLayerBase.isVisible();
        this.position = acWmsLayerBase.getPosition();
        this.styles=acWmsLayerBase.getStyles();
        this.name=acWmsLayerBase.getName();
	}

	/**Filtra los nombres de las capas externas (ó capas de la EIEL), eliminando comas, tildes y eñes. Con el fin
     * de que puedan ser visualizadas en la guía urbana, mediante el servidor de mapas de la EIEL.
     * @param Nombre original de la capa externa.
     * @return Nombre de la capa externa filtrado.
     */
    private String filterExternalLayerName(String externalLayerName){
    	/**FILTRADO DE COMAS:
         */
         String layerName = externalLayerName.replace(',',' ');//sustituímos las comas por espacios en blanco
        
        /**FILTRADO DE TILDES:
         */
        layerName = layerName.replace('á', 'a');
        layerName = layerName.replace('é', 'e');
        layerName = layerName.replace('í', 'i');
        layerName = layerName.replace('ó', 'o');
        layerName = layerName.replace('ú', 'u');
        
        /**FILTRADO DE EÑES:
         */
        layerName = layerName.replace('ñ', 'n');
        
        return layerName;
    }//fin del método
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
    
    



	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
    public boolean isTemplate() {
		return isTemplate;
		
	}

	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}
	
	

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTable() {
		return table;
	}



	public void setTable(String table) {
		this.table = table;
	}



	public String getIdField() {
		return idField;
	}



	public void setIdField(String idField) {
		this.idField = idField;
	}



	public String getGeometryField() {
		return geometryField;
	}



	public void setGeometryField(String geometryField) {
		this.geometryField = geometryField;
	}
	
	
	public HashMap getStyles() {
		return styles;
	}

	public void setStyles(HashMap styles) {
		this.styles = styles;
	}

	/**Convierte la capa wms en una capa de la eiel, retornará null si la capa wms no representa a una capa eiel
	 * @return
	 */
	public EIELLayer convert(){
		if(this.service.equals(EIEL_SERVICE)){
		EIELLayer eielLayer = new EIELLayer();
		eielLayer.setId(layerId);
		eielLayer.setTable(table);
		eielLayer.setIdField(idField);
		eielLayer.setGeometryField(geometryField);
		return eielLayer;
		}
		return null;
	}

}
