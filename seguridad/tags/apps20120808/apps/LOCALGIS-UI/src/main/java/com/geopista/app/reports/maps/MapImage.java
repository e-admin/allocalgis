package com.geopista.app.reports.maps;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.reports.localgiscoreutils.LocalgisManagerBuilderSingleton;
import com.geopista.app.utilidades.UtilsDrivers;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.model.LocalgisLayerExt;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.Scale;

public class MapImage extends BufferedImage{
	
	private static final Log logger = LogFactory.getLog(MapImage.class);
	
	private final static Color FILL_COLOR = new Color(255,255,255, 85);
    private final static Color LINE_COLOR = Color.black;
    
	private String scale;
	
	public MapImage(int width, int height, Object selectionId, String scale, Integer idEntidad, int mapId, String layer, String table, String column){
		this(width,height,selectionId,scale,idEntidad,mapId,layer,table,column,null);
	}
	public MapImage(int width, int height, Object selectionId, String scale, Integer idEntidad, int mapId, String layer, String table, String column,String layersSeleccionadas){
		super(width, height, BufferedImage.TYPE_INT_RGB);
		this.scale = scale;
		
		
		String driver = AppContext.getApplicationContext().getString("conexion.driver");
		try {
			UtilsDrivers.registerDriver(driver);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		
		LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getLocalgisManagerBuilder(); 

		String mapUrlString = null;	
		try {
			AppContext aplicacion = (AppContext) AppContext.getApplicationContext();		
			
			//Si el mapa que viene en la imagen no existe intentamos buscar aquel que venga en las propiedades
			//mapa.informes o mapa.informes.alternativo
			Integer idMapGU=null;
			try {
				logger.info("Buscando mapa para imprimir imagen:"+mapId+ " Identificador de entidad:"+idEntidad);
				idMapGU = localgisManagerBuilder.getLocalgisMapManager().getIdMapByIdGeopista(mapId, idEntidad, false);
			} catch (Exception e1) {
				try {
					logger.info("Mapa para imprimir no encontrado. Buscando alternativo:"+mapId);
					Integer mapaInformes1 = (Integer)aplicacion.getBlackboard().get("mapa.informes");
					if (mapaInformes1!=null)
						idMapGU = localgisManagerBuilder.getLocalgisMapManager().getIdMapByIdGeopista(mapaInformes1, idEntidad, false);
				} catch (Exception e) {
					Integer mapaInformes2 = (Integer)aplicacion.getBlackboard().get("mapa.informes.alternativo");
					if (mapaInformes2!=null)
						idMapGU = localgisManagerBuilder.getLocalgisMapManager().getIdMapByIdGeopista(mapaInformes2, idEntidad, false);
				}
			}
			
			LocalgisMap localgisMap = localgisManagerBuilder.getLocalgisMapManager().getPublishedMap(idMapGU); 
	    	Integer srid = Integer.valueOf(localgisMap.getSrid());

			Scale mapScale = null;
			if (scale.equals(MapImageConstants.SCALE_TYPE_AUTOMATIC)){
                mapScale = localgisManagerBuilder.getLocalgisUtilsManager().getReportScale(table, column, selectionId, width, height, srid);
//			    if (mapImageType == null) {
//	                mapScale = localgisManagerBuilder.getLocalgisUtilsManager().getReportScaleForParcelaByReferenciaCatastral(selectionId.toString(), width, height);
//			    } else {
//                    mapScale = localgisManagerBuilder.getLocalgisUtilsManager().getReportScale(mapImageType.getTable(), mapImageType.getColumn(), selectionId, width, height);
//			    }
			}
			else {
				int scaleNumerator = 1;
				int scaleDenominator = 1000;
				try {
					scaleNumerator = Integer.parseInt(scale.substring(0, scale.indexOf(":")));
					scaleDenominator = Integer.parseInt(scale.substring(scale.indexOf(":")+1));				
				} catch (Exception e){
					e.printStackTrace();
				}
				mapScale = new Scale(scaleNumerator, scaleDenominator);
			}

			boolean usarWMSExternos=true;
			try{
				String cargarWMSExternos = (String)aplicacion.getBlackboard().get("informes.usarwmsexternos");
				if (cargarWMSExternos.equalsIgnoreCase("SI"))
					usarWMSExternos=true;
				else
					usarWMSExternos=false;
			}
			catch (Exception e){
				
			}
			
			
			String layers=null;
			if (layersSeleccionadas!=null){				
				layers=layersSeleccionadas.replaceAll(";", ",");
			}
			else{
				//Obtenemos el mapa privado
				List mapLayers = localgisManagerBuilder.getLocalgisMapManager().getMapLayersByIdGeopistaAndEntidad(mapId,idEntidad,0);
				StringBuffer sbLayers = new StringBuffer("");
				if (mapLayers != null && !mapLayers.isEmpty()) {
					Iterator itLayers = mapLayers.iterator();
					for (int i=0; itLayers.hasNext(); i++) {
						LocalgisLayerExt layerExt = (LocalgisLayerExt) itLayers.next();
						if (i>0) sbLayers.append(",");
						sbLayers.append(layerExt.getLayername());
					}
				}
				layers = sbLayers.toString();
			}
			
			if (!usarWMSExternos){
				//Quitamos el pnoa y catastro
				layers=layers.replaceAll("lcg_pnoa,", "");
				layers=layers.replaceAll("lcg_pnoa", "");
				layers=layers.replaceAll("catastro,", "");
				layers=layers.replaceAll("catastro", "");
			}
			
				
			
            mapUrlString = localgisManagerBuilder.getLocalgisURLsManager().getURLReportMap(idEntidad, table, column, selectionId, localgisMap, false, layers, mapScale, width, height);
//			if (mapImageType == null) {
//	            mapUrlString = localgisManagerBuilder.getLocalgisURLsManager().getURLReportMapByReferenciaCatastral(selectionId.toString(), mapScale, width, height);
//			} else {
//                mapUrlString = localgisManagerBuilder.getLocalgisURLsManager().getURLReportMap(idMunicipio, mapImageType.getTable(), mapImageType.getColumn(), selectionId, mapImageType.getLayers(), mapImageType.getStyle(), mapScale, width, height);
//			}

            System.out.println("URL del mapa:"+mapUrlString);
            logger.info("URL del Mapa:"+mapUrlString);
			mapUrlString = mapUrlString.replaceAll(" ", "%20");
			URL mapUrl = new URL(mapUrlString);
			BufferedImage mapImage = ImageIO.read(mapUrl);
			Graphics2D g2d = (Graphics2D) this.getGraphics();
			g2d.drawImage(mapImage, 0, 0, Color.white, null);
			
			//drawScale();
			//drawImageBorder();
		} catch (Exception e) {
			System.out.println("URL:"+mapUrlString);
			e.printStackTrace();
		}
	}
	
		
	private void drawImageBorder(){
		Graphics2D graphics = (Graphics2D) this.getGraphics();
		Rectangle2D imageBorder = new Rectangle2D.Double(
				0, 0, getWidth()-1, getHeight()-1);
		graphics.setColor(LINE_COLOR);		
		graphics.draw(imageBorder);
	}
	
	private void drawScale(){		 
	    Graphics2D graphics = (Graphics2D) this.getGraphics();
	    Rectangle2D scaleRectangle = new Rectangle2D.Double(
	    		0, new Integer(this.getHeight()-20).doubleValue(),
	    		new Integer(this.getWidth()).doubleValue(), new Integer(20).doubleValue());
	    graphics.setColor(FILL_COLOR);
	    graphics.fill(scaleRectangle);
	    Line2D scaleBorderLine = new Line2D.Double(
	    		0, new Integer(this.getHeight()-20).doubleValue(),
	    		new Integer(this.getWidth()).doubleValue(), new Integer(this.getHeight()-20).doubleValue());
	    graphics.setColor(LINE_COLOR);
	    graphics.draw(scaleBorderLine);
	    Font scaleFont = new Font("Dialog", Font.PLAIN, 10);	    
	    TextLayout scaleTextLayout = new TextLayout(scale, scaleFont, graphics.getFontRenderContext());
	    scaleTextLayout.draw(graphics, 5, this.getHeight() - 5);

	}
}
