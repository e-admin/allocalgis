package com.localgis.mapscript;

import java.io.File;

import edu.umn.gis.mapscript.MS_LAYER_TYPE;
import edu.umn.gis.mapscript.classObj;
import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;

public class GenerateMapFile {
	
	private String ficheroMap;
	private String stylesURL;

	public GenerateMapFile(String ficheroMap, String stylesURL){
		this.ficheroMap=ficheroMap;
		this.stylesURL=stylesURL;
	}
	
	public void generate(){
        mapObj mapObj = new mapObj(ficheroMap);

        int numLayers = mapObj.getNumlayers();
        MS_LAYER_TYPE[] typeLayers = new MS_LAYER_TYPE[numLayers];
        for (int i = 0; i < numLayers; i++) {
            typeLayers[i] = mapObj.getLayer(i).getType();
        }

        mapObj.applySLDURL(stylesURL);
        /*
         * Para cada capa reestablecemos el tipos para evitar modificaciones y ordenamos los class
         * segun las escalas, siguiendo estas reglas:
         */
        for (int i = 0; i < numLayers; i++) {
            layerObj layer = mapObj.getLayer(i);
            layer.setType(typeLayers[i]); 
            sortClasses(layer);
        }
        
        mapObj.save(ficheroMap);
       
	}
	
	 private void sortClasses(layerObj layer) {
	        int numClasses = layer.getNumclasses();
	        boolean swapped;
	        int i = numClasses;
	        do {
	            swapped = false;
	            i = i - 1;
	            for (int j = 0; j < i; j++) {
	                classObj class1 = layer.getClass(j);
	                classObj class2 = layer.getClass(j+1);
	                if ((class2.getMaxscaledenom() < class1.getMaxscaledenom()) ||
	                    (class1.getMaxscaledenom() == class2.getMaxscaledenom() && class2.getMinscaledenom() > class1.getMinscaledenom()) ||
	                    (class1.getMaxscaledenom() == class2.getMaxscaledenom() && class1.getMinscaledenom() == class2.getMinscaledenom() && class2.getExpressionString() != null && class1.getExpressionString() == null)) {
	                    layer.moveClassDown(j);
	                    swapped = true;
	                }
	            }
	        } while (swapped);
	    }
	
	public static void main (String args[]){
		String ficheroMap=args[0];
		String urlEstilos=args[1];
		
		new GenerateMapFile(ficheroMap,urlEstilos).generate();
		
	}

}
