package com.localgis.index;


import java.sql.Connection;

import java.sql.SQLException;



import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.geopista.app.SimpleAppContext;
import java.util.concurrent.*;


public class LocalgisLayerIndexer {
	private static final long LIMIT = 2000;
	private static Logger log = Logger.getLogger(LocalgisLayerIndexer.class);
	
	private static SimpleAppContext acontext=new SimpleAppContext();
	
	private static String entityIgnoreList="";
	private static String entityIncludeList="";
	
	private static String layerIgnoreList="";
	private static String layerIncludeList="";
	
	private static int INDEXER_WORKERS=1;
	
	
	
	
	
	public static void proccessCmdLineArguments(String args[]){
		
		int iarg=0;
		
		for (String s:args){
			if (s.toLowerCase().contains("entity.ignore=")) {
				try {	entityIgnoreList=Util.getArgumentTokenValue(s);	}catch(Exception ex){}				
			}
			if (s.toLowerCase().contains("entity.include=")) {
				try {	entityIncludeList=Util.getArgumentTokenValue(s);	}catch(Exception ex){}				
			}	
			if (s.toLowerCase().contains("layer.ignore=")) {
				try {	layerIgnoreList=Util.getArgumentTokenValue(s);	}catch(Exception ex){}				
			}	
			if (s.toLowerCase().contains("layer.include=")) {
				try {	layerIncludeList=Util.getArgumentTokenValue(s);	}catch(Exception ex){}				
			}			
			
			iarg++;
		}		
	}



	private int numEntidad=0;
	
	public synchronized int getNumEntidad(){
		return numEntidad;
	}
	public synchronized int nextNumEntidad() {
		return numEntidad++;
		
	}
	
	private  static  List<String> entidades;
	private static int entidadesIndex=0;
	
	/**
	 * Devuelve el idEntidad almacenado en la lista entidades y modifica el índice entidadesIndex
	 * de modo que la próxima llamada a este método retorne el siguiente
	 * @return
	 */
	private  synchronized static String nextIdEntidad() {
		String idEntidad;
		try {
		   idEntidad=(String) entidades.get(entidadesIndex);
		}catch(IndexOutOfBoundsException e){
			idEntidad="";
		}
		entidadesIndex++;
		return idEntidad;
	}
	
	private static int threadsRunning(List<Thread> tg){
		int running=0;
		for (Thread thread : tg) {
			if (thread.isAlive()) {
				running++;
			}
			else {

			}
		}	
		return running;
	}

	private static void index(String entityIgnoreList, String entityIncludeList, String layerIgnoreList, String layerIncludeList  ){
		
		try {
			Connection conn=SimpleAppContext.getJDBCConnection();
			entidades=LayerIndexer.getGeopistaEntidades(conn);

			List<Thread> tg = new ArrayList<Thread>();

			int running=0;			

			String idEntidad=nextIdEntidad();
			
			int entidadesProcesadas=0;

            //Puesto que un thread puede estar activo hasta que no lo limpie el GC
			//añadimos el control de entidadesProcesadas

			while (idEntidad!=null && idEntidad.length()>0&& entidadesProcesadas<entidades.size() ){

				if (Util.included(entityIncludeList,idEntidad) &&!Util.included(entityIgnoreList,idEntidad)){    

					try {
						LayerIndexer p1=new LayerIndexer(entityIgnoreList,entityIncludeList,layerIgnoreList,layerIncludeList );
						p1.setParameters(SimpleAppContext.getJDBCConnection(), idEntidad);


						while (threadsRunning(tg)>INDEXER_WORKERS ) {
							System.out.print(".");
						}
						Thread t1=new Thread(p1);
						t1.start();
						tg.add(t1);
						entidadesProcesadas++;

						try {Thread.sleep(2000); }catch (Exception ex){}

					}catch(Exception ex) {
						log.error(ex.getMessage());
						ex.printStackTrace();
					}
				}
				idEntidad=nextIdEntidad();
			}
			for (Thread thread : tg) {thread.join();}
			conn.close();
		}catch (SQLException sqle) {
			log.error("Error: "+sqle.getMessage());
			sqle.printStackTrace();
		}catch (InterruptedException ie ){
			log.error("Error en threads "+ie.getMessage());
		}
		
	}
	
	public static void main(String args[]) {
		log.info("########################## INICIO INDEXACION Layers LocalGIS #####################");
		try {


			entityIgnoreList=SimpleAppContext.getString("entity.ignore");
			entityIncludeList=SimpleAppContext.getString("entity.include");
			
			layerIgnoreList=SimpleAppContext.getString("layer.ignore");
			layerIncludeList=SimpleAppContext.getString("layer.include");	
			try {
			   INDEXER_WORKERS=new Integer(SimpleAppContext.getString("indexer.workers")).intValue();
			}catch(NumberFormatException nfe){
				INDEXER_WORKERS=1;
			}
			
			
			
			proccessCmdLineArguments(args);
			LayerIndexer ind = new LayerIndexer(entityIgnoreList,entityIncludeList,layerIgnoreList,layerIncludeList );
			index(entityIgnoreList,entityIncludeList,layerIgnoreList,layerIncludeList );




		}catch (Exception ex){
			ex.printStackTrace();
		}
		log.info("Indexación FINALIZADA");
	}
}
