/**
 * Map.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.dwr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.directwebremoting.WebContextFactory;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisWMSException;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.GeopistaMap;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.wms.WMSConfigurator;
import com.localgis.web.wm.exceptions.LocalgisInfoException;
import com.localgis.web.wm.exceptions.LocalgisWaitException;
import com.localgis.web.wm.struts.beans.EntidadSupramunicipalBean;
import com.localgis.web.wm.struts.beans.EntidadesSupramunicipalesList;
import com.localgis.web.wm.util.EntidadSupramunicipalComparator;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.wm.util.MapComparator;

public class Map {

    private static final String TYPE_PUBLIC = "publico";
    private static final String TYPE_PRIVATE = "privado";
	private Logger logger = Logger.getLogger(Map.class);
	
	private static HashMap listaProcesos =new HashMap();
    
	public void publishMaps(String mapsToPublish,String mapsToRemove, String type,int mapDefault) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisWMSException, LocalgisInvalidParameterException, LocalgisDBException  {
		try {
            Boolean publicMap = new Boolean(false);
            if ((type != null) && (type.equals(TYPE_PUBLIC))) {
                publicMap = new Boolean(true);
            } else if ((type != null) && (type.equals(TYPE_PRIVATE))) {
                publicMap = new Boolean(false);
            }
            Integer idEntidad = (Integer) WebContextFactory.get().getSession().getAttribute("idEntidad");
            GeopistaMap[] geopistaMapsToPublish = buildGeopistaMaps(mapsToPublish);
            LocalgisMap[] localgisMapsToRemove = buildLocalgisMaps(mapsToRemove);
            logger.debug("Maps to Publish");
            for (int i = 0; i < geopistaMapsToPublish.length; i++) {
                logger.debug("Geopista Map id: " + geopistaMapsToPublish[i].getIdMap() + " name: " + geopistaMapsToPublish[i].getName());
            }
            logger.debug("Maps to Remove");
            for (int i = 0; i < localgisMapsToRemove.length; i++) {
                logger.debug("Localgis Map id: " + localgisMapsToRemove[i].getMapid() + " name: " + localgisMapsToRemove[i].getName());
            }
            LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
            localgisMapsConfigurationManager.publishAndRemoveMapsFromWMSServer(geopistaMapsToPublish, localgisMapsToRemove, null, idEntidad, publicMap, mapDefault == -1 ? null : new Integer(
                    mapDefault),false);
		} catch (LocalgisConfigurationException e) {
			logger.error("LocalgisConfigurationException publicando mapas.", e);
			throw e;
		} catch (LocalgisInitiationException e) {
			logger.error("LocalgisInitiationException publicando mapas.", e);
			throw e;
		} catch (LocalgisInvalidParameterException e) {
			logger.error("LocalgisInvalidParameterException publicando mapas.", e);
			throw e;
		} catch (LocalgisDBException e) {
			logger.error("LocalgisDBException publicando mapas.", e);
			throw e;
		}
//		finally{
//	    	Locale.setDefault(new Locale("es", "ES"));
//	    	logger.error(Locale.getDefault());
//		}
	}
	


    public Hashtable getMaps(String type) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisInvalidParameterException, LocalgisDBException  {
    	try {
            Hashtable hashtable = new Hashtable();
            Boolean publicMap = new Boolean(false);
            if ((type != null) && (type.equals(TYPE_PUBLIC))) {
                publicMap = new Boolean(true);
            } else if ((type != null) && (type.equals(TYPE_PRIVATE))) {
                publicMap = new Boolean(false);
            }
            Integer idEntidad = (Integer) WebContextFactory.get().getSession().getAttribute("idEntidad");
            LocalgisMapsConfigurationManager localgisMapsConfigurationManager;
			localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
            List geopistaAvailableMapsList = localgisMapsConfigurationManager.getAvailableMaps(idEntidad);
            List localgisPublishedMapsList = localgisMapsConfigurationManager.getPublishedMaps(idEntidad, publicMap);
            ArrayList toRemove = new ArrayList();
            for (Iterator iteratorAvailable = geopistaAvailableMapsList.iterator(); iteratorAvailable.hasNext();) {
                GeopistaMap geopistaMap = (GeopistaMap) iteratorAvailable.next();
                if (geopistaMap.getName() == null) {
                    geopistaMap.setName("Nombre Desconocido " + "ID " + geopistaMap.getIdMap());
                }
                for (Iterator iteratorPublished = localgisPublishedMapsList.iterator(); iteratorPublished.hasNext();) {
                    LocalgisMap localgisMap = (LocalgisMap) iteratorPublished.next();
                    if (geopistaMap.getIdMap().equals(localgisMap.getMapidgeopista())) {

                        toRemove.add(geopistaMap);
                    } else {
                        if (localgisMap.getName() == null) {
                            localgisMap.setName("Nombre Desconocido " + "ID " + localgisMap.getMapid());
                        }
                    }
                }
            }
            geopistaAvailableMapsList.removeAll(toRemove);
            MapComparator mapComparator = new MapComparator();
            Collections.sort(geopistaAvailableMapsList, mapComparator);
            Collections.sort(localgisPublishedMapsList, mapComparator);
            hashtable.put("availableMaps", geopistaAvailableMapsList);
            hashtable.put("publishedMaps", localgisPublishedMapsList);
            return hashtable;
    	} catch (LocalgisConfigurationException e) {
			logger.error("LocalgisConfigurationException obteniendo mapas.", e);
			throw e;
		} catch (LocalgisInitiationException e) {
			logger.error("LocalgisInitiationException obteniendo mapas.", e);
			throw e;
		} catch (LocalgisInvalidParameterException e) {
			logger.error("LocalgisInvalidParameterException obteniendo mapas.", e);
			throw e;
		} catch (LocalgisDBException e) {
			logger.error("LocalgisDBException obteniendo mapas.", e);
			throw e;
		}
    }
    
    
	
    private GeopistaMap[] buildGeopistaMaps(String maps) {
    	StringTokenizer stringTokenizer = new StringTokenizer(maps,",");
    	Vector geopistaMapVector = new Vector();
    	while (stringTokenizer.hasMoreTokens()) {
    		String mapInfo = stringTokenizer.nextToken();
        	GeopistaMap geopistaMap = new GeopistaMap();
        	String[] splits = mapInfo.split(";");
        	geopistaMap.setIdMap(new Integer(splits[0]));
        	if (splits.length == 3) {
        	    geopistaMap.setName(splits[2]);
        	}
        	else {
        	    geopistaMap.setName(splits[1]);
        	}
        	geopistaMapVector.add(geopistaMap);
    	}
    	return (GeopistaMap[]) geopistaMapVector.toArray(new GeopistaMap[geopistaMapVector.size()]);
    }

    private LocalgisMap[] buildLocalgisMaps(String maps) {
        StringTokenizer stringTokenizer = new StringTokenizer(maps,",");
        Vector localgisMapVector = new Vector();
        while (stringTokenizer.hasMoreTokens()) {
            String mapInfo = stringTokenizer.nextToken();
            LocalgisMap localgisMap = new LocalgisMap();
            String[] splits = mapInfo.split(";");
            localgisMap.setMapid(new Integer(splits[0]));
            if (splits.length == 3) {
                localgisMap.setMapidgeopista(new Integer(splits[1]));
                localgisMap.setName(splits[2]);
            }
            else {
                localgisMap.setName(splits[1]);
            }
            localgisMapVector.add(localgisMap);
        }
        return (LocalgisMap[]) localgisMapVector.toArray(new LocalgisMap[localgisMapVector.size()]);
    }
    
    
    private GeopistaMap[] buildGeopistaMaps(String maps,boolean publicarTodasEntidades) {
        StringTokenizer stringTokenizer = new StringTokenizer(maps,",");
        Vector geopistaMapVector = new Vector();
        while (stringTokenizer.hasMoreTokens()) {
            String mapInfo = stringTokenizer.nextToken();
            GeopistaMap geopistaMap = new GeopistaMap();
            String[] splits = mapInfo.split(";");
            
            //Cuando queremos publicar para todas las entidades dejamos vacio
            //el identificador de mapa para que genere uno nuevo
            //if (!publicarTodasEntidades)
            //	localgisMap.setMapid(new Integer(splits[0]));
            
            geopistaMap.setIdMap(new Integer(splits[1]));
            if (splits.length == 3) {
            	//geopistaMap.setIdMap(new Integer(splits[1]));
            	geopistaMap.setName(splits[2]);
            }
            else {
            	geopistaMap.setName(splits[1]);
            }
            geopistaMapVector.add(geopistaMap);
        }
        return (GeopistaMap[]) geopistaMapVector.toArray(new GeopistaMap[geopistaMapVector.size()]);
    }
    
    public void republishMaps(String mapsToRepublish, String type,boolean publicarTodasEntidades,boolean borrar) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisWMSException, LocalgisInvalidParameterException, LocalgisDBException, LocalgisWaitException  {
    	try {
            Boolean publicMap = new Boolean(false);
            if ((type != null) && (type.equals(TYPE_PUBLIC))) {
                publicMap = new Boolean(true);
            } else if ((type != null) && (type.equals(TYPE_PRIVATE))) {
                publicMap = new Boolean(false);
            }
            Integer idEntidad = (Integer) WebContextFactory.get().getSession().getAttribute("idEntidad");
            LocalgisMap[] localgisMapsToRepublish = buildLocalgisMaps(mapsToRepublish);
            GeopistaMap[] geopistaMapsToPublish = buildGeopistaMaps(mapsToRepublish,publicarTodasEntidades);
            logger.debug("Maps to Republish");
            for (int i = 0; i < localgisMapsToRepublish.length; i++) {
                logger.debug("Localgis Map id: " + localgisMapsToRepublish[i].getMapid() + " name: " + localgisMapsToRepublish[i].getName());
            }
            
            if (publicarTodasEntidades){
            	if (localgisMapsToRepublish.length>1){
            		throw new LocalgisInvalidParameterException("Solo se permite republicar un mapa para todas las entidades");
            	}
            	
            	ProcessPublicacion processPublicacion=null;
            	if(!borrar)
            		processPublicacion=new ProcessPublicacion(geopistaMapsToPublish,publicMap,borrar);
            	else
            		processPublicacion=new ProcessPublicacion(localgisMapsToRepublish,publicMap,borrar);
            	processPublicacion.start();
            	
            	//Incluimos en una lista de procesos el nuevo proceso.
            	listaProcesos.put(geopistaMapsToPublish[0].getIdMap(),processPublicacion);

            	throw new LocalgisWaitException("El proceso de Publicacion tarda en ejecutarse. Espere a que se publiquen todos los mapas");

            }
            

            LocalgisMapsConfigurationManager localgisMapsConfigurationManager;
			localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
            localgisMapsConfigurationManager.publishAndRemoveMapsFromWMSServer(null, null, localgisMapsToRepublish, idEntidad, publicMap, null,false);
			} catch (LocalgisConfigurationException e) {
				logger.error("LocalgisConfigurationException republicando mapas.", e);
				throw e;
			} catch (LocalgisInitiationException e) {
				logger.error("LocalgisInitiationException republicando mapas.", e);
				throw e;
			} catch (LocalgisWMSException e) {
				logger.error("LocalgisWMSException republicando mapas.", e);
				throw e;
			} catch (LocalgisInvalidParameterException e) {
				logger.error("LocalgisInvalidParameterException republicando mapas.", e);
				throw e;
			} catch (LocalgisDBException e) {
				logger.error("LocalgisDBException republicando mapas.", e);
				throw e;
			}
    }
    
    
    private EntidadesSupramunicipalesList getEntidades() throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisDBException {
        EntidadesSupramunicipalesList entidadesList = new EntidadesSupramunicipalesList();
        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
        List geopistaEntidadesList = localgisMapsConfigurationManager.getEntidadesSupramunicipales();
        for (Iterator iterator = geopistaEntidadesList.iterator(); iterator
				.hasNext();) {
			GeopistaEntidadSupramunicipal item = (GeopistaEntidadSupramunicipal) iterator.next();
			EntidadSupramunicipalBean entidad = new EntidadSupramunicipalBean();
			entidad.setId(item.getIdEntidad());
			entidad.setName(item.getNombreoficial());
			entidadesList.setEntidad(entidad);
		}
        Collections.sort(entidadesList.getEntidades(),new EntidadSupramunicipalComparator());
        return entidadesList;
    }
   
    
    public void getInfoMap(String mapsToGetInfo,String type) throws Exception  {
    	try {
    		
    		String status;
    		
    		Boolean publicMap = new Boolean(false);
    		if ((type != null) && (type.equals(TYPE_PUBLIC))) {
                publicMap = new Boolean(true);
            } else if ((type != null) && (type.equals(TYPE_PRIVATE))) {
                publicMap = new Boolean(false);
            }
    		
            Integer idEntidad = (Integer) WebContextFactory.get().getSession().getAttribute("idEntidad");
            LocalgisMap[] localgisMapsToGetInfo = buildLocalgisMaps(mapsToGetInfo);
            logger.debug("Maps to Get Info");
            for (int i = 0; i < localgisMapsToGetInfo.length; i++) {
                logger.debug("Localgis Map id: " + localgisMapsToGetInfo[i].getMapid() + " name: " + localgisMapsToGetInfo[i].getName());
            }
            
            if (localgisMapsToGetInfo.length>1){
        		throw new LocalgisInvalidParameterException("Solo se permite consultar la informacion de un mapa");
        	}
            
            LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
            List entidadesPublicadas=localgisMapsConfigurationManager.getEntidadesPublicadas(localgisMapsToGetInfo[0].getMapidgeopista(),publicMap);
            status="\nEntidades actuales con el mapa publicado:"+entidadesPublicadas.size()+"\n";

            

            String urlMapServer =LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager().getMapServerURL(idEntidad, localgisMapsToGetInfo[0].getMapid(), publicMap, WMSConfigurator.ALL_REQUESTS);
            status+="\n+ URL MapServer:"+urlMapServer+"\n";
            
            if (listaProcesos.get(localgisMapsToGetInfo[0].getMapidgeopista())!=null){
            	ProcessPublicacion process= (ProcessPublicacion)listaProcesos.get(localgisMapsToGetInfo[0].getMapidgeopista());
            	if (process.isAlive()){
            		logger.info("Esta vivo");
            		status+=process.getStatus();
            		logger.info("Status:"+status);
            		throw new LocalgisInfoException("Informacion de estado:"+status);
            	}
            	else{
            		logger.info("El proceso ya termino");
            		throw new LocalgisInfoException("Informacion de estado:"+status);
            	}
            }
            else{
            	logger.info("El proceso no existe");
            	throw new LocalgisInfoException("Informacion de estado:"+status);
            }
            

			} catch (Exception e) {
				//logger.error("LocalgisException consultando mapas.", e);
				throw e;
			}
    }
    
    
    /**
     * Publicacion del mapa para todas las entidades.
     * @author fjgarcia
     *
     */
    class ProcessPublicacion extends Thread{
    	
    	boolean processContinue=true;
		private GeopistaMap[] geopistaMapsToRepublish;
		private LocalgisMap[] localgisMapsToRemove;
		private Boolean publicMap;
		
		int mapasPublicados=0;
		int mapasAPublicar=0;
    	
		String erroresPublicacion="";
		private Boolean borrar;
		
    	public ProcessPublicacion(GeopistaMap[] geopistaMapsToRepublish,Boolean publicMap,Boolean borrar){
    		this.geopistaMapsToRepublish=geopistaMapsToRepublish;
    		this.publicMap=publicMap;
    		this.borrar=borrar;
		}
    	public ProcessPublicacion(LocalgisMap[] localgisMapsToRemove,Boolean publicMap,Boolean borrar){
    		this.localgisMapsToRemove=localgisMapsToRemove;
    		this.publicMap=publicMap;
    		this.borrar=borrar;
		}
    	public void terminateProcess(){
    		processContinue=false;
    	}
    	
    	public String getStatus(){
    		String estado="Estado de Publicacion: "+mapasPublicados+"/"+mapasAPublicar;
    		estado+="\n Errores:"+erroresPublicacion;
    		
    		return estado;
    		
    	}
		public void run(){
	
			logger.info("Iniciando proceso de publicacion/despublicacion");
			EntidadesSupramunicipalesList entidadList = null;
			try {
				entidadList = getEntidades();
				mapasAPublicar=entidadList.getEntidades().size();
					Iterator it=entidadList.getEntidades().iterator();
					EntidadSupramunicipalBean entidadSupramunicipal=null;
					while (it.hasNext()){
						try{
							entidadSupramunicipal=(EntidadSupramunicipalBean)it.next();
							int entidadSeleccionada=entidadSupramunicipal.getId();
							if (!borrar)
								logger.info("Publicando mapa para la entidad:"+entidadSeleccionada+" Nombre:"+entidadSupramunicipal.getName());
							else
								logger.info("Despublicando mapa para la entidad:"+entidadSeleccionada+" Nombre:"+entidadSupramunicipal.getName());
							LocalgisMapsConfigurationManager localgisMapsConfigurationManager;
							localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
							
							if (!borrar)
								localgisMapsConfigurationManager.publishAndRemoveMapsFromWMSServer(geopistaMapsToRepublish, null, null, entidadSeleccionada, publicMap, null,true);
							else
								localgisMapsConfigurationManager.publishAndRemoveMapsFromWMSServer(null, localgisMapsToRemove, null, entidadSeleccionada, publicMap, null,true);

							if (!borrar)								
								logger.info("Mapa publicado para la entidad:"+entidadSeleccionada);
							else
								logger.info("Mapa despublicado para la entidad:"+entidadSeleccionada);
					        mapasPublicados++;
						}
						catch (Exception e){
							logger.info("Error al publicar el mapa para la entidad:"+entidadSupramunicipal.getName());
							if (entidadSupramunicipal!=null)
								erroresPublicacion+=" ("+entidadSupramunicipal.getId()+"-"+entidadSupramunicipal.getName()+")";
						}
					}
					return;				
			}
			catch (Exception e){
				e.printStackTrace();
				String message = "No se pudo obtener la información de municipios, debido a un problema de acceso a la base de datos.";
			}
			
			logger.info("Finalizando proceso de publicacion");
		
		}
			
    }	
    
	
}
