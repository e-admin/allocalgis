/**
 * MapImageExpressionManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geopista.app.reports.maps;


/**
 *
 * @author jpolo
 */
public class MapImageExpressionManager {
	
	public static String getExpression(MapImageSettings mapImageSettings){
		if (mapImageSettings.getMapSelectionType() == MapImageConstants.MAP_SELECTION_TYPE_INTERACTIVE){
			return getExpressionForInteractiveMap(mapImageSettings);
		}
		else if (mapImageSettings.getMapSelectionType() == MapImageConstants.MAP_SELECTION_TYPE_PARAMETRIC){
			return getExpressionForParametricMap(mapImageSettings);
		}
		else {
			return null;
		}
	}
	
	public static String getExpressionForParametricMap(MapImageSettings mapImageSettings){
		StringBuffer sbExpression = new StringBuffer();
        
        sbExpression.append(MapImageConstants.MAP_IMAGE_MANAGER_CLASS_NAME);        
        sbExpression.append(".getParametricMapImage(\"");
        sbExpression.append(mapImageSettings.getImageKey());
        if (mapImageSettings.getMapSelectionIdName() != null){
        	if (mapImageSettings.getMapSelectionIdType() == MapImageConstants.MAP_SELECTION_ID_TYPE_FIELD){
        		sbExpression.append("\",$F{");
        	}
        	else {
        		sbExpression.append("\",$P{");
        	}   
        	sbExpression.append(mapImageSettings.getMapSelectionIdName());
        	sbExpression.append("},\"");
        }
        else {
        	sbExpression.append("\",\"\",\"");
        }
        sbExpression.append(mapImageSettings.getScale());
        sbExpression.append("\",");
//        if (mapImageSettings.getIdMapImageType() != null) {
//            sbExpression.append("\"");
//            sbExpression.append(mapImageSettings.getIdMapImageType());
//            sbExpression.append("\"");
//        } else {
//            sbExpression.append("\"\"");
//        }
        sbExpression.append("\"");
        sbExpression.append(mapImageSettings.getMapId());
        sbExpression.append("\",\"");
        sbExpression.append(mapImageSettings.getCapa());
        sbExpression.append("\",\"");
        sbExpression.append(mapImageSettings.getTabla());
        sbExpression.append("\",\"");
        sbExpression.append(mapImageSettings.getColumna());
        sbExpression.append("\"");

        if (mapImageSettings.getLayers()!=null)
        	sbExpression.append(",\""+mapImageSettings.getLayers()+"\"");
        else
            sbExpression.append(",\"\"");

        
        if (mapImageSettings.getIdUnicoImagen()!=null)
        	sbExpression.append(",\""+mapImageSettings.getIdUnicoImagen()+"\"");
        else
            sbExpression.append(",\"\"");
        
        if (mapImageSettings.getWidth()!=0)
        	sbExpression.append(","+mapImageSettings.getWidth()+"");
        else
            sbExpression.append(",");

        if (mapImageSettings.getHeight()!=0)
        	sbExpression.append(","+mapImageSettings.getHeight()+"");
        else
            sbExpression.append(",");

        sbExpression.append(",$P{ID_ENTIDAD}");

        sbExpression.append(")");
        
        return sbExpression.toString();
	}
	
	public static String getExpressionForInteractiveMap(MapImageSettings mapImageSettings){
		StringBuffer sbExpression = new StringBuffer();
        
        sbExpression.append(MapImageConstants.MAP_IMAGE_MANAGER_CLASS_NAME);        
        sbExpression.append(".getInteractiveMapImage(\"");
        sbExpression.append(mapImageSettings.getImageKey());
        if (mapImageSettings.getMapSelectionIdName() != null){
        	sbExpression.append("\",$P{");        
        	sbExpression.append(mapImageSettings.getMapSelectionIdName());
        	sbExpression.append("},\"");
        }
        else {
        	sbExpression.append("\",\"\",\"");
        }
        sbExpression.append(mapImageSettings.getScale());
        sbExpression.append("\",");

        
        sbExpression.append("\"");
        sbExpression.append(mapImageSettings.getMapId());
        sbExpression.append("\",\"");
        sbExpression.append(mapImageSettings.getCapa());
        sbExpression.append("\",\"");
        sbExpression.append(mapImageSettings.getAtributo());
        sbExpression.append("\",\"");
        sbExpression.append(mapImageSettings.getTabla());
        sbExpression.append("\",\"");
        sbExpression.append(mapImageSettings.getColumna());
        sbExpression.append("\"");
        
        if (mapImageSettings.getLayers()!=null)
        	sbExpression.append(",\""+mapImageSettings.getLayers()+"\"");
        else
            sbExpression.append(",\"\"");
        
        
        if (mapImageSettings.getIdUnicoImagen()!=null)
        	sbExpression.append(",\""+mapImageSettings.getIdUnicoImagen()+"\"");
        else
            sbExpression.append(",\"\"");
        
        if (mapImageSettings.getWidth()!=0)
        	sbExpression.append(","+mapImageSettings.getWidth()+"");
        else
            sbExpression.append(",");

        if (mapImageSettings.getHeight()!=0)
        	sbExpression.append(","+mapImageSettings.getHeight()+"");
        else
            sbExpression.append(",");

        sbExpression.append(",$P{ID_ENTIDAD}");

        
        sbExpression.append(")");
        return sbExpression.toString();
	}
	
	public static MapImageSettings parseExpression(String expression){
        int mapSelectionType = getMapSelectionTypeFromExpression(expression);
        switch (mapSelectionType) {
        case MapImageConstants.MAP_SELECTION_TYPE_INTERACTIVE:
            return parseExpressionInteractive(expression);
        case MapImageConstants.MAP_SELECTION_TYPE_PARAMETRIC:
            return parseExpressionParametric(expression);
        default:
            return null;
        }
	}
	
	public static MapImageSettings parseExpressionInteractive(String expression) {
        String scale = getScaleFromExpression(expression);
        String mapSelectionIdName = getSelectionIdNameFromExpression(expression);
        String imageKey = getImageKeyFromExpression(expression);
        int mapId = getSelectMapaFromExpressionInteractive(expression);
        int mapSelectionIdType = getSelectionIdTypeFromExpresion(expression);
        String capa = getCapaFromExpressionInteractive(expression);
        String atributo = getAtributoFromExpressionInteractive(expression);
        String tabla = getTablaFromExpressionInteractive(expression);
        String columna = getColumnaFromExpressionInteractive(expression);
        String layers = getLayersFromExpressionInteractive(expression);
        String idUnicoImagen = getIdUnicoImagenFromExpressionInteractive(expression);
        int width = getParametro(expression,10);
        int height = getParametro(expression,11);
        
        MapImageSettings mapImageSettings = new MapImageSettings(); 
        mapImageSettings.setScale(scale);
        mapImageSettings.setImageKey(imageKey);
        mapImageSettings.setMapSelectionIdName(mapSelectionIdName);
        mapImageSettings.setMapSelectionIdType(mapSelectionIdType);
        mapImageSettings.setMapSelectionType(MapImageConstants.MAP_SELECTION_TYPE_INTERACTIVE);
        mapImageSettings.setMapId(mapId);
        mapImageSettings.setCapa(capa);
        mapImageSettings.setAtributo(atributo);
        mapImageSettings.setTabla(tabla);
        mapImageSettings.setColumna(columna);
        mapImageSettings.setLayers(layers);
        mapImageSettings.setIdUnicoImagen(idUnicoImagen);
        mapImageSettings.setWidth(width);
        mapImageSettings.setHeight(height);
        return mapImageSettings;
	}

	public static MapImageSettings parseExpressionParametric(String expression) {
        //String idMapImageType = getIdMapImageTypeFromExpressionParametric(expression);
        String scale = getScaleFromExpression(expression);
        String mapSelectionIdName = getSelectionIdNameFromExpression(expression);
        String imageKey = getImageKeyFromExpression(expression);
        int mapSelectionIdType = getSelectionIdTypeFromExpresion(expression);
        int mapId = getSelectMapaFromExpressionParametric(expression);
        String capa = getCapaFromExpressionParametric(expression);
        String tabla = getTablaFromExpressionParametric(expression);
        String columna = getColumnaFromExpressionParametric(expression);
        String layers = getLayersFromExpressionParametric(expression);
        String idUnicoImagen = getIdUnicoImagenFromExpressionParametric(expression);
        int width = getParametro(expression,10);
        int height = getParametro(expression,11);
        
        MapImageSettings mapImageSettings = new MapImageSettings(); 
        //mapImageSettings.setIdMapImageType(idMapImageType);
        mapImageSettings.setScale(scale);
        mapImageSettings.setImageKey(imageKey);
        mapImageSettings.setMapSelectionIdName(mapSelectionIdName);
        mapImageSettings.setMapSelectionIdType(mapSelectionIdType);
        mapImageSettings.setMapSelectionType(MapImageConstants.MAP_SELECTION_TYPE_PARAMETRIC);
        mapImageSettings.setMapId(mapId);
        mapImageSettings.setCapa(capa);
        mapImageSettings.setTabla(tabla);
        mapImageSettings.setColumna(columna);
        mapImageSettings.setLayers(layers);
        mapImageSettings.setIdUnicoImagen(idUnicoImagen);
        mapImageSettings.setWidth(width);
        mapImageSettings.setHeight(height);

        return mapImageSettings;
	}

	public static boolean isMapImageExpression(String expression){
        if (expression.startsWith(MapImageConstants.MAP_IMAGE_MANAGER_CLASS_NAME)){
            return true;
        }
        
        return false;
    }
    
    public static boolean isInteractiveMapImageExpression(String expression){
    	int mapType = getMapSelectionTypeFromExpression(expression);
    	if (mapType == -1 || mapType != MapImageConstants.MAP_SELECTION_TYPE_INTERACTIVE){
    		return false;
    	}
    	return true;
    }
    
    public static boolean isParametricMapImageExpression(String expression){
    	int mapType = getMapSelectionTypeFromExpression(expression);
    	if (mapType == -1 || mapType != MapImageConstants.MAP_SELECTION_TYPE_PARAMETRIC){
    		return false;
    	}
    	return true;
    }

    private static int getMapSelectionTypeFromExpression(String expression){
        String interactivePrefix = MapImageConstants.MAP_IMAGE_MANAGER_CLASS_NAME + ".getInteractiveMapImage(\"";
        String parametricPrefix = MapImageConstants.MAP_IMAGE_MANAGER_CLASS_NAME + ".getParametricMapImage(\"";        
        
        if (expression.startsWith (interactivePrefix)){
            return MapImageConstants.MAP_SELECTION_TYPE_INTERACTIVE;
        }
        else if (expression.startsWith(parametricPrefix)){
            return MapImageConstants.MAP_SELECTION_TYPE_PARAMETRIC;
        }
        else {                
            return -1;
        }        
    }

	private static String getImageKeyFromExpression(String expression) {
		int beginIndex = expression.indexOf("(\"");
		if (beginIndex == -1){
			return null;
		}
		int endIndex = expression.indexOf("\",");
		
		String imageKey = expression.substring(beginIndex+2, endIndex);
		return imageKey;
	}

    private static String getIdMapImageTypeFromExpressionParametric(String expression){
        // Nos situamos en el ultimo parametro para saltar el idMunicipio
        int lastIndex = expression.lastIndexOf(",");
        if (lastIndex < 1) {
            return null;
        }
        int firstIndex = expression.lastIndexOf(",\"", lastIndex-1);

        if (firstIndex < 0 || lastIndex < 0){
            return null;
        }
        
        String idMapImageType = expression.substring(firstIndex+2, lastIndex - 1);
                
        return idMapImageType;
    }

    private static String getScaleFromExpression(String expression){
    	// Entre la segunda y la tercera coma
    	int firstIndex = 0;
    	for (int i=0; i<2 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        String scale = expression.substring(firstIndex+2, lastIndex-1);
        return scale;
    }

    private static int getSelectMapaFromExpressionInteractive(String expression){
    	// Entre la tercera y cuarta coma
    	int firstIndex = 0;
    	for (int i=0; i<3 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return -1;
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return -1;
        int mapId = -1;
        String id = expression.substring(firstIndex+2, lastIndex-1);
        if (id.matches("[0-9]*")){
        	mapId = Integer.parseInt(id);
        }
        return mapId;
    }
    
    private static String getSelectionIdNameFromExpression(String expression){
    	String selectionId = getSelectionIdFromExpression(expression).trim();
    	
    	if (selectionId.indexOf("$P{") != -1 || selectionId.indexOf("$F{") != -1){
    		String selectionIdName = selectionId.substring(3, selectionId.length()-1);    		
    		return selectionIdName;
    	}
    	
    	return null;
    }
    
    private static int getSelectionIdTypeFromExpresion(String expression){
        String selectionId = getSelectionIdFromExpression(expression).trim();
        if (selectionId.startsWith("$P{")) {
            return MapImageConstants.MAP_SELECTION_ID_TYPE_PARAMETER;
        } else if (selectionId.startsWith("$F{")) {
            return MapImageConstants.MAP_SELECTION_ID_TYPE_FIELD;
        } else {
            return -1;
        }
    }
    
    private static String getSelectionIdFromExpression(String expression){
        int firstIndex = expression.indexOf(",");
        if (firstIndex < 0){
            return null;
        }
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0){
            return null;
        }
        String selectionId = expression.substring(firstIndex+1, lastIndex).trim();
        return selectionId;
    }
    
    private static String getCapaFromExpressionInteractive(String expression) {
    	// Entre la cuarta y quinta coma
    	int firstIndex = 0;
    	for (int i=0; i<4 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }

    private static String getAtributoFromExpressionInteractive(String expression) {
    	// Entre la quinta y sexta coma
    	int firstIndex = 0;
    	for (int i=0; i<5 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }

    private static String getTablaFromExpressionInteractive(String expression) {
    	// Entre la sexta y septima coma
    	int firstIndex = 0;
    	for (int i=0; i<6 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }

    private static String getColumnaFromExpressionInteractive(String expression) {
    	// Ultimo
//    	int firstIndex = expression.lastIndexOf(",");
//    	int lastIndex = expression.lastIndexOf(")");
//    	if (firstIndex < 0 || lastIndex < 0) return "";
//    	
//    	return expression.substring(firstIndex+2, lastIndex-1);
    	// Entre la septima y octava coma
    	int firstIndex = 0;
    	for (int i=0; i<7 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }
    private static String getLayersFromExpressionInteractive(String expression) {
    	// Ultimo
//    	int firstIndex = expression.lastIndexOf(",");
//    	int lastIndex = expression.lastIndexOf(")");
//    	if (firstIndex < 0 || lastIndex < 0) return "";
//    	
//    	return expression.substring(firstIndex+2, lastIndex-1);
    	// Entre la septima y octava coma
    	int firstIndex = 0;
    	for (int i=0; i<8 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }
    
    private static String getIdUnicoImagenFromExpressionInteractive(String expression) {
    	// Ultimo
//    	int firstIndex = expression.lastIndexOf(",");
//    	int lastIndex = expression.lastIndexOf(")");
//    	if (firstIndex < 0 || lastIndex < 0) return "";
//    	
//    	return expression.substring(firstIndex+2, lastIndex-1);
    	// Entre la septima y octava coma
    	int firstIndex = 0;
    	for (int i=0; i<9 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }
    
    private static int getParametro(String expression,int indice){
    	// Entre la tercera y cuarta coma
    	int firstIndex = 0;
    	for (int i=0; i<indice && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return -1;
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return -1;
        int mapId = -1;
        String id = expression.substring(firstIndex+1, lastIndex);
        if (id.matches("[0-9]*")){
        	mapId = Integer.parseInt(id);
        }
        return mapId;
    }
    
    
    
    
    
    private static int getSelectMapaFromExpressionParametric(String expression){
    	// Entre la tercera y cuarta coma
    	int firstIndex = 0;
    	for (int i=0; i<3 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return -1;
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return -1;
        int mapId = -1;
        String id = expression.substring(firstIndex+2, lastIndex-1);
        if (id.matches("[0-9]*")){
        	mapId = Integer.parseInt(id);
        }
        return mapId;
    }

    private static String getCapaFromExpressionParametric(String expression) {
    	// Entre la cuarta y quinta coma
    	int firstIndex = 0;
    	for (int i=0; i<4 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }

    private static String getTablaFromExpressionParametric(String expression) {
    	// Entre la quinta y sexta coma
    	int firstIndex = 0;
    	for (int i=0; i<5 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }

    private static String getColumnaFromExpressionParametric(String expression) {
    	// Entre la sexta y septima coma
    	int firstIndex = 0;
    	for (int i=0; i<6 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }
    private static String getLayersFromExpressionParametric(String expression) {
    	//
    	int firstIndex = 0;
    	for (int i=0; i<7 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }
    
    private static String getIdUnicoImagenFromExpressionParametric(String expression) {
    	// 
    	int firstIndex = 0;
    	for (int i=0; i<8 && firstIndex >= 0; i++) {
    		firstIndex = expression.indexOf(",", firstIndex + 1);
    	}
    	if (firstIndex < 0) return "";
        int lastIndex = expression.indexOf(",", firstIndex+1);
        if (lastIndex < 0) return "";

        return expression.substring(firstIndex+2, lastIndex-1);
    }
}
