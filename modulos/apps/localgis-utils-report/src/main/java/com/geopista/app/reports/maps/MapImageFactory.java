/**
 * MapImageFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.maps;

import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRReport;

import com.geopista.app.reports.ReportsManager;
import com.geopista.app.reports.ScaleUtils;
import com.geopista.app.utilidades.UtilsDrivers;
import com.localgis.web.core.LocalgisManagerBuilder;

public class MapImageFactory {

    private static JRReport report = null;
    private static HashMap interactiveMapImageScales = null;
    
    private static final Log logger = LogFactory.getLog(MapImageFactory.class);

    public static void init(JRReport baseReport) {
        MapImageFactory.report = baseReport;
        interactiveMapImageScales = new HashMap();
        try {
			UtilsDrivers.registerDriver(null);
			//DriverManager.setLogWriter(new PrintWriter((System.err)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public static void setExecuteFromWeb(Integer mapaInformes,Integer mapaInfomesAlternativo,
    		String cargarWMSExternos,LocalgisManagerBuilder localgisManagerBuilder){
    	MapImage.setExecuteFromWeb(true);
    	MapImage.setMapaInformes(mapaInformes);
    	MapImage.setMapaInformesAlternativo(mapaInfomesAlternativo);
    	MapImage.setCargarWMSExternos(cargarWMSExternos);
    	MapImage.setLocalgisManagerBuilder(localgisManagerBuilder);
    }
   
    /**
     * Metodo al que se llamara por reflection para obtener la imagen de un mapa parametrico
     * @param imageKey
     * @param selectionId
     * @param scale
     * @param idMapImageType
     * @return
     */
    public static MapImage getParametricMapImage(String imageKey, Object selectionId, String scale, String mapId, String layer, String table, String column, String idEntidad) {
    	return getParametricMapImage(imageKey,selectionId,scale,mapId,layer,table,column,null,null,0,0,idEntidad);
    }
    
    public static MapImage getParametricMapImage(String imageKey, Object selectionId, String scale, String mapId, String layer, String table, String column, String layers,String idUnicoImagen,int width,int height,String idEntidad) {

        try {
        	
        	if (width==0 && height==0){
    			
    			JRImage image = ReportsManager.getInstance().getImageElementByKey(report, imageKey);

    			if (image == null) {
    			    return null;
    			}

    			width = image.getWidth(); 
    			height = image.getHeight();
        	}
			
      
        	// Convierto el ancho y el alto de la imagen a un tamaño de imagen de manera
        	// que el area en unidades metricas de la imagen en pantalla sea igual
        	// al area en unidades metricas de la imagen en papel.
        	int screenWidth = ScaleUtils.convertPaperPixelsToServerImagePixels(width);
        	int screenHeight = ScaleUtils.convertPaperPixelsToServerImagePixels(height);
        	
			//MapImageType mapImageType = (MapImageType)ReportsManager.getInstance().getMapImageTypes().get(idMapImageType);
			
			Integer idEntidadInteger;
			try {
			    idEntidadInteger = new Integer(idEntidad);
			} catch (NumberFormatException e) {
			    idEntidadInteger = null;
			}
			        
			// La resolucion de imagenes en el servidor y la utilizada internamente
			// por JasperReport es de 72dpi, por lo que no hay que hacer conversion 
			// de escala
			//return new MapImage(width, height, selectionId, scale, mapImageType, idMunicipioInteger);
			return new MapImage(screenWidth, screenHeight, selectionId, scale, idEntidadInteger, Integer.parseInt(mapId), layer, table, column,layers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    /**
     * Metodo al que se llamara por reflection para obtener la imagen de un mapa interactivo
     * @param imageKey
     * @param selectionId
     * @param scale
     * @param idMapImageType
     * @return
     */
    public static MapImage getInteractiveMapImage(String imageKey, String selectionId, String scale, String mapId, String layer, String attribute, String table, String column, String idEntidad){
    	return getInteractiveMapImage(imageKey,selectionId,scale,mapId,layer,attribute,table,column,null,null,0,0,idEntidad);
    }
    
    
    public static MapImage getInteractiveMapImage(String imageKey, String selectionId, String scale, String mapId, String layer, String attribute, String table, String column, String layers,String idUnicoImagen,int width,int height,String idEntidad){


    	if (width==0 && height==0){
			
			JRImage image = ReportsManager.getInstance().getImageElementByKey(report, imageKey);

			if (image == null) {
			    return null;
			}

			width = image.getWidth(); 
			height = image.getHeight();
    	}
    	

    	// Convierto el ancho y el alto de la imagen a un tamaño de imagen de manera
    	// que el area en unidades metricas de la imagen en pantalla sea igual
    	// al area en unidades metricas de la imagen en papel.
    	int screenWidth = ScaleUtils.convertPaperPixelsToServerImagePixels(width);
    	int screenHeight = ScaleUtils.convertPaperPixelsToServerImagePixels(height);
    	
    	String finalScale;
    	if (scale.equals(MapImageConstants.SCALE_TYPE_INTERACTIVE)){
    		logger.info("Clave de escala:"+imageKey);
    		System.out.println("Clave de escala:"+imageKey);
    		if (interactiveMapImageScales.size()>0)
    			finalScale = (String) interactiveMapImageScales.get(imageKey);
    		else
    			finalScale=scale;
    		logger.info("Escala final:"+finalScale);
    		System.out.println("Escala final:"+finalScale);
    	}
    	else {
    		finalScale = scale;
    	}

    	Integer idEntidadInteger;
        try {
            idEntidadInteger = new Integer(idEntidad);
        } catch (NumberFormatException e) {
            idEntidadInteger = null;
        }

    	return new MapImage(screenWidth, screenHeight, selectionId, finalScale, idEntidadInteger, Integer.parseInt(mapId), layer, table, column);
    }
    
    public static void setInteractiveMapImageScale(String imageKey, String scale){
    	interactiveMapImageScales.put(imageKey, scale);
    }
}
