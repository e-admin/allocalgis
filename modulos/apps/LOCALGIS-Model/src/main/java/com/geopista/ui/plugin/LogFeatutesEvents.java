/**
 * LogFeatutesEvents.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaMapCustomConverter;
import com.geopista.model.IGeopistaLayer;
import com.geopista.model.IGeopistaMap;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.model.FeatureEvent;

public class LogFeatutesEvents
{

    
    private HashMap totalEvents = null;
    private String logFileName = null;
	private IGeopistaLayer	layer;
	private IGeopistaMap	map;

    public LogFeatutesEvents(IGeopistaMap map, IGeopistaLayer lyr) throws Exception
    {
    this.layer=lyr;
    this.map=map;
        load();
    }
    
    public LogFeatutesEvents(String logFileName,IGeopistaLayer lyr) throws Exception
    {
        this.layer=lyr;
        if(logFileName!=null)
        {
	        File tempFile = new File(logFileName);
	        String basePath = tempFile.getParent();
	        tempFile = new File(basePath,layer.getSystemId()+".log");
	        logFileName = tempFile.getAbsolutePath();
        }
	    this.logFileName = logFileName;
        load();
    }
    
    

    /**
     * @param map
     * @param lyr
     * @return
     */
    private String createFilePath(IGeopistaMap map, IGeopistaLayer lyr)
    {
     
        if(map==null) return null;
        if(map.getProjectFile()==null) return null;
        String logBasePath = map.getProjectFile().getParent();
        String idSystem = lyr.getSystemId();
        File logBasePathFile = new File(logBasePath,idSystem+".log");
        return logBasePathFile.getAbsolutePath();
    }



    public void load() throws Exception
    {
        if (logFileName==null)
        	this.logFileName = createFilePath(map,layer);
        
        File file = null;
        if(logFileName!=null)
            file = new File(logFileName);
        
        if(logFileName==null || !file.exists())
        {
            totalEvents = new HashMap();
            return;
        }

        PersistentLog loadEvents = null;
        try
        {
        	
        	InputStream is = new FileInputStream(file);
        	
        	String stringReader = FileUtil.parseISToStringUTF8(is);

            XML2Java converter = new XML2Java();
            converter.addCustomConverter(Date.class, GeopistaMapCustomConverter
                    .getMapDateCustomConverter());
            loadEvents = (PersistentLog) converter.read(stringReader,
                    com.geopista.ui.plugin.PersistentLog.class);
            totalEvents = (HashMap) loadEvents.getListEvents();
        } 
        catch(Exception e){
        	
        }        	
    }
        
    public void setLogFileName (String name)
    {
        this.logFileName = name;
        
    }
    /**
     * Intenta crear el fichero para volcar el registro de eventos.
     * Si no lo consigue vuelve ignorando la solicitud.
     * 
     * @throws Exception
     */

    public void save() throws Exception
    {
    if (logFileName==null)
    	this.logFileName = createFilePath(map,layer);
    if (logFileName==null)return;
        PersistentLog saveEvents = new PersistentLog();
        saveEvents.setListEvents(totalEvents);
        File saveEventsFile = new File(logFileName);
        StringWriter stringWriterXml = new StringWriter();
        try
        {
            Java2XML converter = new Java2XML();
            
            converter.addCustomConverter(Date.class, GeopistaMapCustomConverter
                    .getMapDateCustomConverter());
            
            converter.write(saveEvents, "Events", stringWriterXml);
            FileUtil.setContents(saveEventsFile.getAbsolutePath(), stringWriterXml
                    .toString());
            
        }  finally
        {
            stringWriterXml.flush();
        }
    }
    
    
    public void processEvent(FeatureEvent featureEvent)
    {
        
        
        Collection changedFeatures = featureEvent.getFeatures();
        Iterator changedFeaturesIterator = changedFeatures.iterator();
        while (changedFeaturesIterator.hasNext())
        {
            Feature currentFeature = (Feature) changedFeaturesIterator.next();

            // Si no Geopista Feature no se puede utilizar, aunque aqui no
            // deberia llegar ninguna
            // Feature que no sea GeopistaFeature
            if (!(currentFeature instanceof GeopistaFeature))
                continue;

            GeopistaFeature currentGeopistaFeature = (GeopistaFeature) currentFeature;

            String featureSystemId = currentGeopistaFeature.getSystemId();
            
            
            //Si la feature es nueva y se ha modificado no hacemos nada para que no se 
            //borre el evento ADDED
            LogEvent oldEvent = (LogEvent)totalEvents.get(featureSystemId);
            if(oldEvent!=null)
            {
                if(oldEvent.getEventType().equals("ADDED"))
                {
                    if(featureEvent.getType().toString().equals("GEOMETRY MODIFIED")||featureEvent.getType().toString().equals("ATTRIBUTES MODIFIED"))
                    {
                        continue;
                    }
                    if(featureEvent.getType().toString().equals("DELETED"))
                    {
                        totalEvents.remove(featureSystemId);
                        continue;
                    }
                }
            }
                

            // Construimos el evento
            LogEvent logEvent = new LogEvent();

            // introducimos el tipo de evento: modificado, borrado o insertado.
            // El identificador
            // de sistema y la fecha de la operacion
            logEvent.setEventType(featureEvent.getType().toString());
            logEvent.setFeatureId(featureSystemId);
            logEvent.setTimeStamp(new Date());

            // lo introducimos el la lista de modificaciones
            totalEvents.put(featureSystemId, logEvent);

        }

    }
    
    
    public Collection getNewFeatures()
    {
        List newFeaturesList = new ArrayList();
        Set totalEventsSet = totalEvents.keySet();
        Iterator totalEventsIterator = totalEventsSet.iterator();
        while(totalEventsIterator.hasNext())
        {
            Object currentKey = totalEventsIterator.next();
            LogEvent currentLogEvent = (LogEvent) totalEvents.get(currentKey);
            if(currentLogEvent.getEventType().equals("ADDED"))
            {
                newFeaturesList.add(currentLogEvent);
            }
        }
        
        return newFeaturesList;
    }
    
    public Collection getModifiedFeatures()
    {
        List newFeaturesList = new ArrayList();
        Set totalEventsSet = totalEvents.keySet();
        Iterator totalEventsIterator = totalEventsSet.iterator();
        while(totalEventsIterator.hasNext())
        {
            Object currentKey = totalEventsIterator.next();
            LogEvent currentLogEvent = (LogEvent) totalEvents.get(currentKey);
            if(currentLogEvent.getEventType().equals("GEOMETRY MODIFIED")||currentLogEvent.getEventType().equals("ATTRIBUTES MODIFIED"))
            {
                newFeaturesList.add(currentLogEvent);
            }
        }
        
        return newFeaturesList;
    }
    
    public Collection getDeletedFeatures()
    {
        List newFeaturesList = new ArrayList();
        Set totalEventsSet = totalEvents.keySet();
        Iterator totalEventsIterator = totalEventsSet.iterator();
        while(totalEventsIterator.hasNext())
        {
            Object currentKey = totalEventsIterator.next();
            LogEvent currentLogEvent = (LogEvent) totalEvents.get(currentKey);
            if(currentLogEvent.getEventType().equals("DELETED"))
            {
                newFeaturesList.add(currentLogEvent);
            }
        }
        
        return newFeaturesList;
    }
    
}
