/**
 * GenerateMapFile.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

	    quitarEstilos(layer);
         
        }
        
        mapObj.save(ficheroMap);
       
	}

         private void quitarEstilos(layerObj layer){

for (int j=0 ; j < layer.getNumclasses(); j++){
            		classObj layerClass = layer.getClass(j);            		            		
            		/*
                     * Para cada capa se debe modificar la expresion de TEXT si existe.
                     * En MapServer6 cambia el texto de ([texto]) a 
                     * expresiones como '[texto]' o como "[texto]"
                     *
                     */
            		if (layerClass.getTextString() != null && layerClass.getTextString().startsWith("([")){
            			if (layerClass.getTextString().equals("([Elemento Temporal])")){ 
            				layer.removeClass(j);
            				//layer.getClass(j).setText(("Elemento Temporal"));
            			}            			
            		}
            	}
            	
            	//Lo hacemos dos veces para temporal y publicable porque cuando borras el class se mueve la posicion y no
            	//pilla la siguiente
            	
            	for (int j=0 ; j < layer.getNumclasses(); j++){
            		classObj layerClass = layer.getClass(j);            		            		
            		/*
                     * Para cada capa se debe modificar la expresion de TEXT si existe.
                     * En MapServer6 cambia el texto de ([texto]) a 
                     * expresiones como '[texto]' o como "[texto]"
                     *
                     */
            		if (layerClass.getTextString() != null && layerClass.getTextString().startsWith("([")){
            			if (layerClass.getTextString().equals("([Elemento Publicable])")){       
            				layer.removeClass(j);
            				//layer.getClass(j).setText(("Elemento Publicable"));
            			}
            		}
            	}
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

