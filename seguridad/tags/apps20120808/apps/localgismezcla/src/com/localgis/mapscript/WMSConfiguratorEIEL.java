package com.localgis.mapscript;

import java.io.File;

import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.MS_LAYER_TYPE;
import edu.umn.gis.mapscript.layerObj;



public class WMSConfiguratorEIEL {

    public static void main(String[] args) {
        applyStylesToMap();
    }

    private static void applyStylesToMap() {
        String filenameMap = "eiel/eiel_municipio.map";;
        mapObj mapObj = new mapObj(filenameMap);

        int numLayers = mapObj.getNumlayers();
        MS_LAYER_TYPE[] typeLayers = new MS_LAYER_TYPE[numLayers];
        for (int i = 0; i < numLayers; i++) {
            typeLayers[i] = mapObj.getLayer(i).getType();
        }

        File f=new File("eiel/tmp");
        f.mkdirs();
        System.out.println("Cargando estilos");
        String stylesURL = "http://213.164.35.10:8082/docs/estilos.xml";
        mapObj.applySLDURL(stylesURL);

        for (int i = 0; i < numLayers; i++) {
            layerObj layer = mapObj.getLayer(i);
            layer.setType(typeLayers[i]);
        }

        mapObj.save(filenameMap+".new");
        
    }

}
