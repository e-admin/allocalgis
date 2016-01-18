package com.geopista.app.catastro.intercambio.edicion;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.geopista.app.catastro.model.beans.ASCCatastro;
import com.geopista.app.catastro.model.beans.PlantaCatastro;
import com.geopista.app.catastro.model.beans.UsoCatastro;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;

public class AscWriterCatastro {

	private static Logger logger = Logger.getLogger(AscWriterCatastro.class);
	
	/** Fijamos un numero maximo de plantas. El problema es que no sabemos
	 	en principio cuantas capas podrían haber creado los usuarios. Este
	 	numero creemos es suficiente.*/
	int MAX_NUM_CAPAS=100;
	
	Hashtable listaCapasASC=new Hashtable();
	
	/**
	 * Iteramos por las capas pero de una forma mas logica. En lugar de iterar
	 * por todas, iteramos primero por la Planta General PG- y luego por todas las
	 * plantas significativas PSXX-. Solo iteramos por las significativas por las 
	 * siguietnes, que son las que afectan al ASC.
	 * PS01-AS (Superficie)
	 * PS01-AU (Texto/Codigo de destino)
	 * PS01-AL (Codigos de locales) (Por esto en principio no vamos a iterar)
	 * 
	 * @return
	 */
	public void write(ASCCatastro ascCatastro, LayerManager manager) {

        //Recorremos todas las features de todas las capas que llamen PSXX-AU y para
        //cada una de ellas buscamos en la capa PSXX-AS cual es su valor de superficie.
        //Como resultamos tenemos una tabla hash con el nombre de la capa de clave y dentro
        //una tabla hash con elementos del tipo AA25 valor 10.

        //Plantas significativas
        String tipoPlantasSign[]={"LP","LI","AU","AS","AL","TL","TP","TO","CO"};



        String LAYER_CODIGOS_DESTINO="AU";
        String LAYER_SUPERFICIE_CODIGO_DESTINO="AS";

        for (int i=1;i<MAX_NUM_CAPAS;i++){
            String filledLayer=this.fillWithCeros(String.valueOf(i),2); 
            String codigosDestinoLayer="PS"+filledLayer+"-"+LAYER_CODIGOS_DESTINO;
            String superficieLayer="PS"+filledLayer+"-"+LAYER_SUPERFICIE_CODIGO_DESTINO;
            Layer layerCodigosDestino=manager.getLayer(codigosDestinoLayer);
            Layer layerSuperficies=manager.getLayer(superficieLayer);
            //Comprobamos si los códigos son en plan PS01-AU y PS01-AS o PS1-AU y PS1-AS
            if ((layerCodigosDestino==null) || (layerSuperficies==null)){
                filledLayer=String.valueOf(i); 
                codigosDestinoLayer="PS"+filledLayer+"-"+LAYER_CODIGOS_DESTINO;
                superficieLayer="PS"+filledLayer+"-"+LAYER_SUPERFICIE_CODIGO_DESTINO;
                layerCodigosDestino=manager.getLayer(codigosDestinoLayer);
                layerSuperficies=manager.getLayer(superficieLayer);
            }
            //Si alguna de las capas no existe continuamos
            if ((layerCodigosDestino==null) || (layerSuperficies==null)){
                continue;
            }
            Hashtable listaFeatures=null;
            if (listaCapasASC.get(codigosDestinoLayer)!=null)
                listaFeatures=(Hashtable)listaCapasASC.get(codigosDestinoLayer);
            else{
                listaFeatures=new Hashtable();
                listaCapasASC.put(codigosDestinoLayer,listaFeatures);
            }
            FeatureCollection fcCodigosDestino=layerCodigosDestino.getFeatureCollectionWrapper().getWrappee();
            FeatureCollection fcSuperficies = layerSuperficies.getFeatureCollectionWrapper().getUltimateWrappee();

            for (Iterator it = fcCodigosDestino.getFeatures().iterator(); it.hasNext();) {
                Feature sourceFeature = (Feature) it.next();
                if (!(sourceFeature.getGeometry() instanceof GeometryCollection)) {
                    String s = sourceFeature.getGeometry().getGeometryType();
                    if (s.equals("Point"))
                    {
                        if(sourceFeature.getAttribute("TEXTO")!=null && !((String)sourceFeature.getAttribute("TEXTO")).equalsIgnoreCase(""))
                        {
                            String texto=(String)sourceFeature.getAttribute("TEXTO");
                            if(texto!= null){
                                //No tratamos los textos con valor "PTO" porque en el ASC
                                //no tiene correspondencia.
                                if (!texto.startsWith("PTO")){
                                    Feature actualNearestFeature = nearestFeaturesBrute(sourceFeature, fcSuperficies);
                                    if (actualNearestFeature!=null){
                                        String superficie=(String)actualNearestFeature.getAttribute("TEXTO");
                                        if (superficie!=null){
                                            //Comprobamos si ya existe el texto introducido en la capa. Si
                                            //aparece mas de una vez tenemos que sumar las features.
                                            if ((listaFeatures.get(texto)!=null))
                                            {
                                                try {
                                                    String superficieAlmacenada=(String)listaFeatures.get(texto);
                                                    int sumaSuperficies=Integer.parseInt(superficieAlmacenada,10)+Integer.parseInt(superficie,10);
                                                    listaFeatures.put(texto, String.valueOf(sumaSuperficies));
                                                } catch (NumberFormatException e) {
                                                    listaFeatures.put(texto, superficie);
                                                }
                                            }
                                            else
                                                listaFeatures.put(texto, superficie);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    GeometryCollection gc = (GeometryCollection) sourceFeature.getGeometry();
                    //System.out.println("Geometry CASO2");
                    //for (int i = 0; i < gc.getNumGeometries(); i++) {}
                }
            }
        }
        //showFeatures();


        //Revisamos las plantas que habia originariamente
        //y le cargamos la nueva superficie obtenida leyendo el DXF.
        Vector listaCapasBorrar=new Vector();
        if(ascCatastro!= null && ascCatastro.getLstPlantas() != null ){
            for(int i =0;i<ascCatastro.getLstPlantas().size();i++){
                String filledLayer=this.fillWithCeros(String.valueOf(i+1),2);
                String codigosDestinoLayer="PS"+filledLayer+"-"+LAYER_CODIGOS_DESTINO;

                PlantaCatastro plCas= (PlantaCatastro)ascCatastro.getLstPlantas().get(i);
                //Verificamos si la capa sigue disponible en el DXF.
                //Si no lo esta lo incluimos en el vector de capas a borrar.
                Layer layerCodigos=manager.getLayer(codigosDestinoLayer);
                //Comprobamos si los códigos son en plan PS01-AU y PS01-AS o PS1-AU y PS1-AS
                if (layerCodigos==null){
                    filledLayer=String.valueOf(i+1);
                    codigosDestinoLayer="PS"+filledLayer+"-"+LAYER_CODIGOS_DESTINO;

                    plCas= (PlantaCatastro)ascCatastro.getLstPlantas().get(i);
                    //Verificamos si la capa sigue disponible en el DXF.
                    //Si no lo esta lo incluimos en el vector de capas a borrar.
                    layerCodigos=manager.getLayer(codigosDestinoLayer);
                }
                if (layerCodigos==null){
                    //System.out.println("Planta a borrar:"+plCas.getNombre());
                    listaCapasBorrar.add(plCas);
                    continue; 
                }
                Vector listaUsosBorrar=new Vector();
                for(int j=0; j<plCas.getLstUsos().size();j++)
                {
                    UsoCatastro uso= (UsoCatastro)plCas.getLstUsos().get(j);
                    String codigoUso=uso.getCodigoUso();
                    String nuevaSuperficie=getSuperficieFromDXF(codigosDestinoLayer,codigoUso);
                    if (nuevaSuperficie!=null){
                        String formatedSuperficie= String.valueOf(uso.getSuperficieDestinada());
                        try{
                            formatedSuperficie=this.fillWithCeros(nuevaSuperficie,7);
                        }
                        catch (Exception e){
                        }
                        uso.setSuperficieDestinada(Long.parseLong(formatedSuperficie));
                        //Borramos la feature evaluada de la hash de elementos recogidos del DXF. El objetivo
                        //es que en esa hash al final solo queden los nuevos incorporados.
                        removeFeatureFromDXF(codigosDestinoLayer,codigoUso);
                    }
                    else{
                        //Si ya no existe el uso en el fichero DXF lo eliminamos del ASC original.
                        //Lo metemos en un vector temporal para posteriormente eliminarlo.
                        listaUsosBorrar.add(uso);
                        System.out.println("Feature a borrar:"+uso.getCodigoUso()+" Superficie:"+uso.getSuperficieDestinada());
                    }
                }

                //Borramos los usos que han desaparecido del vector de casos de uso.
                for (int indice=0;indice<listaUsosBorrar.size();indice++){
                    UsoCatastro uso= (UsoCatastro)listaUsosBorrar.get(indice);
                    plCas.getLstUsos().remove(uso);
                }
                //**********************************************************************
                //Si el usuario incluyó mas usos los añadimos directamente en el fichero
                //**********************************************************************
                if (listaCapasASC.get(codigosDestinoLayer)!=null){
                    Hashtable listaFeaturesNuevas=(Hashtable)listaCapasASC.get(codigosDestinoLayer);
                    Iterator it=listaFeaturesNuevas.keySet().iterator();
                    while (it.hasNext()){
                        String nombreUso=(String)it.next();
                        String superficie=(String)listaFeaturesNuevas.get(nombreUso);
                        logger.info("Feature a añadir:"+nombreUso+" Superficie:"+superficie);
                        UsoCatastro uso=new UsoCatastro(nombreUso,Long.parseLong(superficie));
                        plCas.getLstUsos().add(uso);
                    }
                    //Borramos la capa de la lista de capas extraida del DXF. No se
                    //borra del DXF.
                    removeCapaFromDXF(codigosDestinoLayer);
                }
            }
        }

        //Borramos capas que han desaparecido del vector de capas en el ASC.
        for (int indice=0;indice<listaCapasBorrar.size();indice++){
            PlantaCatastro planta= (PlantaCatastro)listaCapasBorrar.get(indice);
            System.out.println("Borrando:"+planta.getNombre());
            ascCatastro.getLstPlantas().remove(planta);
        }

        if(ascCatastro != null){
            //Intentamos añadir aquellas capas que se hayan creado en el editor y que sigan el formato
            //PSXX-AU.
            Iterator it=listaCapasASC.keySet().iterator();
            while (it.hasNext()){
                String nombrePlanta=(String)it.next();
                logger.info("Planta a añadir:"+nombrePlanta);
                PlantaCatastro plCas=new PlantaCatastro();
                plCas.setNombre(nombrePlanta);
                plCas.setNumPlantasReales(1);
                ArrayList listaUsos=new ArrayList();
                Hashtable listaUsosNuevoCapa=(Hashtable)listaCapasASC.get(nombrePlanta);
                Iterator it1=listaUsosNuevoCapa.keySet().iterator();
                while (it1.hasNext()){
                    String uso=(String)it1.next();
                    UsoCatastro newUso=new UsoCatastro(uso,Long.parseLong((String)listaUsosNuevoCapa.get(uso)));
                    listaUsos.add(newUso);
                }

                plCas.setLstUsos(listaUsos);
                ascCatastro.getLstPlantas().add(plCas);
            }
            //Si hay mas plantas incrementamos el numero de plantas disponibles
        }
    }

	private String getSuperficieFromDXF(String nombreCapa,String codigoUso){
		String superficie=null;	
		if (listaCapasASC.get(nombreCapa)!=null){
			Hashtable listaTextos=(Hashtable)listaCapasASC.get(nombreCapa);
			if (listaTextos.get(codigoUso)!=null)
				superficie=(String)listaTextos.get(codigoUso);
		}
		return superficie;
	}
	
	private void removeCapaFromDXF(String nombreCapa){
		listaCapasASC.remove(nombreCapa);
	}	
	private void removeFeatureFromDXF(String nombreCapa,String codigoUso){
		if (listaCapasASC.get(nombreCapa)!=null){
			Hashtable listaTextos=(Hashtable)listaCapasASC.get(nombreCapa);
			listaTextos.remove(codigoUso);
		}
	}	
	
	private void showFeatures(){
		
		Iterator it=listaCapasASC.keySet().iterator();
		while (it.hasNext()){
			String nombreCapa=(String)it.next();
			Hashtable h=(Hashtable)listaCapasASC.get(nombreCapa);
			System.out.println("CAPA:"+nombreCapa+ " Elementos:"+h.size());			
			System.out.println("--------------------------");			
			Iterator it2=h.keySet().iterator();
			while (it2.hasNext()){
				String nombreElemento=(String)it2.next();
				String valor=(String)h.get(nombreElemento);
				System.out.println("Texto:"+nombreElemento+ " Superficie:"+valor);
			}
		}
	}
	
	private Feature nearestFeaturesBrute(Feature sourceFeature, FeatureCollection collectionWorkFeatures)
	{
		Geometry sourceFeatureGeometry = sourceFeature.getGeometry();
		
		ArrayList nearStreetNumbers = new ArrayList();
		// obtiene la lista de todas las features
		List newWorkFeatures = collectionWorkFeatures.getFeatures();
		Feature candidate=null;
		boolean onlyclosest=true;
		double distance=Double.MAX_VALUE;
		Iterator workFeaturesIter = newWorkFeatures.iterator();
		while(workFeaturesIter.hasNext())
			{
			Feature currentFeature = (Feature) workFeaturesIter.next();
			double currDist= sourceFeatureGeometry.distance(currentFeature.getGeometry());
			if (onlyclosest && currDist<distance) // select only the closest
				{
				candidate=currentFeature;
				distance=currDist;
				}			
			}		
		return candidate;
	}
	
    /*private String fillWithCeros(int id){
    	String filledLayer=String.valueOf(id);    
    	if (id<10)
    		filledLayer="0"+String.valueOf(id);
    	return filledLayer;	
    }*/	

    private String fillWithCeros(String campo,int longitud){
    	StringBuffer resultado=new StringBuffer(campo);
    	int longitudCampo=campo.length();
    	
    	int resto=longitud-longitudCampo;    	
    	for (int i=0;i<resto;i++){
    		resultado.insert(0,"0");
    	}
    	resultado.setLength(resultado.length());
    	return resultado.toString();	
    }	

}
