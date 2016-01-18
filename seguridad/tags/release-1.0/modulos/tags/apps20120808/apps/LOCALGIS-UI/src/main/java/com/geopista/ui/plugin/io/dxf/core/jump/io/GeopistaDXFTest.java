package com.geopista.ui.plugin.io.dxf.core.jump.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import com.geopista.ui.plugin.io.dxf.reader.DxfFile;

public class GeopistaDXFTest {
	   public void start()
	    {
	        //GeopistaDxfConverter converter = new GeopistaDxfConverter();
	        GeopistaDxfConverter converter = new GeopistaDxfConverter();
	        //FeatureCollectionDxfConverter converter2 = new DxfConverter();
	        DxfFile dxfFile = null;
	        try
	        {
	        	
	        	//Si el DXF tiene el esquema de Geopista luego podemos recuperar
	        	//las capas, llamando a converter.getLayers(), si no es asi
	        	//y es un fichero DXF normal (No Geopista) hay que hacerlo
	        	//de otra manera recuperando todas las entidades que tiene
	        	//y obteniendo las capas disponibles.
	        	InputStream dxf1 = loadFile("DXF_SinGeopistaEsquema.txt");
	        	InputStream dxf2 = loadFile("DXF_ConGeopistaEsquema.txt");
	        	
	            dxfFile = new DxfFile(dxf1);
	        
	            converter.convert(dxfFile);
	                                    
	            ArrayList solucion = new ArrayList();
	            String sLayer;
	            
	            
	            // Obtener FeatureCollections para cada Layer
                // Creamos las geometrias que pertenecen a capas (layers) definidas
                // en los esquemas de geopista (llamamos al conversor de geopista - GeopistaDxfConverter)         
		        for(Enumeration eLayers = converter.getLayers(); eLayers.hasMoreElements();){
		            sLayer = (String)eLayers.nextElement();
	        		System.out.println("Capa:" + sLayer);	        	
		        }
		        
	            // Creamos las geometrias que no pertenecen a ninguna capa (layer) definida
                // en los esquemas de geopista (llamamos al conversor de agil - FeatureCollectionDxfConverter)
		        int iPoints=((FeatureCollectionDxfConverter)converter).getPointFeatureCollection().size();
		        Iterator it=converter.getPointFeatureKeysCollection().iterator();
		        while (it.hasNext()){
		        	String clave=(String)it.next();
		        	System.out.println("ClavePoint:"+clave);
		        }
		        int iLines=((FeatureCollectionDxfConverter)converter).getLineFeatureCollection().size();
		        it=converter.getLineFeatureKeysCollection().iterator();
		        while (it.hasNext()){
		        	String clave=(String)it.next();
		        	System.out.println("ClaveLine:"+clave);
		        }
		        int iPolygons=((FeatureCollectionDxfConverter)converter).getPolygonFeatureCollection().size();
		        
		        int iText=((FeatureCollectionDxfConverter)converter).getTextFeatureKeysCollection().size();
		        it=converter.getTextFeatureKeysCollection().iterator();
		        while (it.hasNext()){
		        	String clave=(String)it.next();
		        	System.out.println("ClaveText:"+clave);
		        }
		        System.out.println("Puntos:"+iPoints);
		        System.out.println("Lineas:"+iLines);
		        System.out.println("Poligonos:"+iPolygons);
		        System.out.println("Textos:"+iText);
		        /*Iterator pointsIterator = ((FeatureCollectionDxfConverter)converter).getPointFeatureCollection().iterator();
                createDataSourceQuery(solucion, pointsIterator, (FeatureCollectionDxfConverter)converter,dxfFile);

                Iterator linesIterator = converter.getLineFeatureCollection().iterator();
                createDataSourceQuery(solucion, linesIterator, (FeatureCollectionDxfConverter)converter,dxfFile);

                Iterator polygonsIterator = converter.getPolygonFeatureCollection().iterator();
                createDataSourceQuery(solucion, polygonsIterator, (FeatureCollectionDxfConverter)converter,dxfFile);

                Iterator textIterator = converter.getTextFeatureCollection().iterator();
                createDataSourceQuery(solucion, textIterator, (FeatureCollectionDxfConverter)converter,dxfFile);
		        */
	        }
	        catch(FileNotFoundException e1)
	        {
	        	System.out.println("Error:"+e1.getMessage());
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }

	    }
		public static InputStream loadFile(String name) throws FileNotFoundException{		
			InputStream inputStream = null;
		    inputStream = GeopistaDXFTest.class.getResourceAsStream(name);
		    if (inputStream==null)
		    	throw new FileNotFoundException("No existe el fichero:"+name);
		    return inputStream;

		}
	    public static void main(String args[])
	    {
	        (new GeopistaDXFTest()).start();
	    }
}
