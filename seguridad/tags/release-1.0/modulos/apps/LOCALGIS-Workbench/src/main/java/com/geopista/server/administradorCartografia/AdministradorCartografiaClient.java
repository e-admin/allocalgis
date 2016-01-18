package com.geopista.server.administradorCartografia;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.model.IGeopistaMap;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ISesion;
import com.geopista.security.SecurityManager;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.io.WKTReader;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.LayerManager;


 
public class AdministradorCartografiaClient  implements IAdministradorCartografiaClient{

    protected SecurityManager sm=null;
	public static final String	mensajeXML	= "mensajeXML";

	public static final String	idMunicipio	= "idMunicipio";

	public static final String	IdApp		= "IdApp";

    /**
	 * Logger for this class
	 */
	//private static final Log	logger	= LogFactory.getLog(AdministradorCartografiaClient.class);
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AdministradorCartografiaClient.class);

	public static final int	LOCK_LAYER_ERROR			= -1;

	public static final int	LOCK_LAYER_LOCKED			= 0;

	public static final int	LOCK_LAYER_OWN				= 1;

	public static final int	LOCK_LAYER_OTHER			= 2;

	public static final int	UNLOCK_LAYER_ERROR			= -1;

	public static final int	UNLOCK_LAYER_UNLOCKED		= 0;

	public static final int	UNLOCK_LAYER_NOTLOCKED		= 3;

	public static final int	LOCK_FEATURE_ERROR			= -1;

	public static final int	LOCK_FEATURE_LOCKED			= 0;

	public static final int	LOCK_FEATURE_OWN			= 4;

	public static final int	LOCK_FEATURE_OTHER			= 5;

	public static final int	UNLOCK_FEATURE_ERROR		= -1;

	public static final int	UNLOCK_FEATURE_UNLOCKED		= 0;

	public static final int	UNLOCK_FEATURE_NOTLOCKED	= 6;

	public static final int	MAX_NUM_FEATURE_UPLOAD		= 100;
	
	
	protected String sUrl=null;
     public AdministradorCartografiaClient(String sUrl){
        this.sm=SecurityManager.getSm();
        this.sUrl=sUrl;
    }

    public AdministradorCartografiaClient(String sUrl, SecurityManager sm){
        this.sm=sm;
        this.sUrl=sUrl;
    }

    public String getSRIDDefecto(boolean defecto, int idEntidad){
        String sridDefecto = "";
    	try{
	        ACQuery query=new ACQuery();
	        if (defecto)
	        	query.setAction(Const.ACTION_SRID_DEFECTO);
	        else
	        	query.setAction(Const.ACTION_SRID_INICIAL);
            Hashtable ht=new Hashtable();
            ht.put(Const.KEY_ID_ENTIDAD,idEntidad);
	        query.setParams(ht);
	        StringWriter swQuery=new StringWriter();
	        ByteArrayOutputStream baos=new ByteArrayOutputStream();
	        new ObjectOutputStream(baos).writeObject(query);
	        Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
	        InputStream in=enviarPlano(sUrl,swQuery.toString());
	        ObjectInputStream ois=new ObjectInputStream(in);
	        try{
	        	sridDefecto=(String)readObject(ois);
	        }catch(OptionalDataException ode){
	            if (ode.eof!=true)
					logger.error("getSRIDDefecto()" + ode.getMessage(), ode);
	        }catch (EOFException ee){
	        }finally{
	            try{ois.close();}catch(Exception e){};
	        }
        }catch(Exception e){
			logger.error("getSRIDDefecto()", e);
        }
    	return sridDefecto;
	}
    	
    
    protected Object readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException,ACException{
        Object oRet=null;
        // TODO: Comprobar tipos y errores
        oRet=ois.readObject();
        
        if (oRet instanceof ACException){
            throw (ACException)oRet;
        }
        return oRet;
    }

    /** Carga la lista de mapas del Administrador de Cartografia */
    public Collection getMaps(String sLocale){
        Collection cRet=null;
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_MAPS);
            Hashtable ht=new Hashtable();
            ht.put(Const.KEY_LOCALE,sLocale);
            ht.put(Const.KEY_MUNICIPIO,sm.getIdMunicipioNS());
            query.setParams(ht);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            cRet=new ArrayList();
            try{
                IACMap acMap=null;
                for(;;){
                    acMap=(IACMap)readObject(ois);
                    cRet.add(acMap.convert(null,null,acMap.gettimeStamp(),sLocale));
                }
            }catch(OptionalDataException ode){
                if (ode.eof!=true)
					logger.error("getMaps(String)" + ode.getMessage(), ode);
            }catch (EOFException ee){
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(Exception e){
			logger.error("getMaps(String)", e);
        }
        return cRet;
    }
    
    /** Carga la lista de usuarios del Administrador de Cartografia */
    public Object getUsers(){
    	Object userList=null;
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_GETUSERS);
//            Hashtable ht=new Hashtable();
//            ht.put(Const.KEY_MUNICIPIO,idMunicipio);
//            query.setParams(ht);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                userList=readObject(ois);
            }catch(OptionalDataException ode){
                if (ode.eof!=true)
					logger.error("getUsers()" + ode.getMessage(), ode);
            }catch (EOFException ee){
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(Exception e){
			logger.error("getUsers()", e);
        }
        return userList;
    }
    
    /** Carga la lista de usuarios que tienen permiso de lectura sobra las capas pasadas desde Administrador de Cartografia */
    public Object getUsersPermLayers(List<Integer> listLayers){
    	Object userList=null;
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_GETUSERSPERMLAYERS);
            Hashtable ht=new Hashtable();
//            ht.put(Const.KEY_MUNICIPIO,idMunicipio);
            ht.put(Const.KEY_ARRAY_CAPAS,listLayers);
            query.setParams(ht);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                userList=readObject(ois);
            }catch(OptionalDataException ode){
                if (ode.eof!=true)
					logger.error("getUsersPermLayers(List<String> listLayers)" + ode.getMessage(), ode);
            }catch (EOFException ee){
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(Exception e){
			logger.error("getUsersPermLayers(List<String> listLayers)", e);
        }
        return userList;
    }
    
    /** Crea un nuevo proyecto de extracción en base de datos */
    public void createExtractProject(Object eProject){
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_CREATEEXTRACTPROJECT);
            Hashtable ht=new Hashtable();
            ht.put(Const.KEY_EXTRACT_PROJECT,eProject);
            query.setParams(ht);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
        }catch(Exception e){
			logger.error("createExtractProject(ExtractionProject eProject, List<Integer> idExtractLayersList) " + e, e);
        }
    }

    /**
     * Borrar un proyecto de la base de Datos
     * @param eProject
     */
    public void deleteExtractProject(Object eProject){
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_DELETEEXTRACTPROJECT);
            Hashtable ht=new Hashtable();
            ht.put(Const.KEY_EXTRACT_PROJECT,eProject);
            query.setParams(ht);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
        }catch(Exception e){
			logger.error("deleteExtractProject" + e, e);
        }
    }
    
    /** Obtiene una lista de proyectos de extracción */
    public List<Object> getExtractProjects(int idMapa){
    	List<Object> extractProjects=null;
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_GETEXTRACTPROJECTS);
            Hashtable ht=new Hashtable();
            ht.put(Const.KEY_MAP, idMapa);
//            ht.put(Const.KEY_MUNICIPIO, idMunicipio);
            query.setParams(ht);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
            	extractProjects=(List<Object>)readObject(ois);
            }catch(OptionalDataException ode){
                if (ode.eof!=true)
					logger.error("getExtractProjects(String projectName)" + ode.getMessage(), ode);
            }catch (EOFException ee){
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(Exception e){
			logger.error("getExtractProjects(String projectName))", e);
        }
        return extractProjects;
    }
    
    /** Crea un nuevo proyecto de extracción en base de datos */
    public void setAssignCellsExtractProject(String idProyectoExtract, HashMap<String, String> celdasUsuarios){
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_SET_ASSIGNCELLSEXTRACTPROJECT);
            Hashtable ht=new Hashtable();
            ht.put(Const.KEY_ID_PROJECT,idProyectoExtract);
            ht.put(Const.KEY_CELLS_USER,celdasUsuarios);
            query.setParams(ht);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
        }catch(Exception e){
			logger.error("assignCellsExtractProject(String idProyectoExtract, List<String> celdasUsuarios) " + e, e);
        }
    }
    
    /** Obtiene una hash con las celdas y usuarios asignados para un proyecto de extracción */
    public HashMap<String, String> getAssignCellsExtractProject(String idProyectoExtract){
    	HashMap<String, String> celdasUsuarios=null;
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_GET_ASSIGNCELLSEXTRACTPROJECT);
            Hashtable ht=new Hashtable();
            ht.put(Const.KEY_ID_PROJECT, idProyectoExtract);
            query.setParams(ht);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
            	celdasUsuarios=(HashMap<String, String>)readObject(ois);
            }catch(OptionalDataException ode){
                if (ode.eof!=true)
					logger.error("getAssignCellsExtractProject(String idProyectoExtract)" + ode.getMessage(), ode);
            }catch (EOFException ee){
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(Exception e){
			logger.error("getAssignCellsExtractProject(String idProyectoExtract)", e);
        }
        return celdasUsuarios;
    }
    /** Carga de un mapa del administrador de cartografia */
    public GeopistaMap loadMap(IGeopistaMap map, String sLocale, Geometry g, FilterNode fn) throws ACException, Exception{
    	return loadMap(map,sLocale,g,fn);
    }
    
    /** Carga de un mapa del administrador de cartografia */
    public IGeopistaMap loadMap(IGeopistaMap map, String sLocale, Geometry g, FilterNode fn,TaskMonitor monitor) throws ACException, Exception{
        IGeopistaMap mRet=map;
        try{
            long startMils=Calendar.getInstance().getTimeInMillis();
            logger.info("Loading map:"+map.getName());
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_MAP);
            Properties p=new Properties();
            p.setProperty(Const.KEY_LOCALE,sLocale);
            p.setProperty(Const.KEY_MAP,map.getSystemId());
            ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
            p.setProperty(Const.KEY_MUNICIPIO,iSesion.getIdMunicipio());
            if (AppContext.getApplicationContext().getBlackboard().get(AppContext.VERSION) != null)
            	p.put(Const.KEY_VERSION, AppContext.getApplicationContext().getBlackboard().get(AppContext.VERSION));
            query.setParams(p);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            
            logger.info("Loading Map PASO1:"+map.getName()+" Size of Message:"+swQuery.getBuffer().length()+" bytes");
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            IACMap acMap=(IACMap)readObject(ois);
            ois.close();
            Hashtable htLayers=new Hashtable();
            Hashtable htStyleXMLs=new Hashtable();
            

            for (Iterator it=acMap.getLayerFamilies().values().iterator();it.hasNext();){
                IACLayerFamily acFamily=(IACLayerFamily)it.next();
				if (acFamily!=null){
					for (Iterator itLayers=acFamily.getLayers().values().iterator();itLayers.hasNext();){
						IACLayer acLayer=(IACLayer)itLayers.next();
						
						
						//SATEC. Control del SRID a utilizar
						Integer srid_destino=-1;
						if ((AppContext.getApplicationContext().getBlackboard().get(Const.KEY_USE_SAME_SRID)!=null) &&
								((Integer)AppContext.getApplicationContext().getBlackboard().get(Const.KEY_USE_SAME_SRID)==1)){
							srid_destino=Integer.parseInt((String)AppContext.getApplicationContext().getBlackboard().get(Const.KEY_SRID_MAPA),10);
						}
						else{
							 srid_destino = Integer.valueOf(CoordinateSystemRegistry.instance(new Blackboard()).get(acMap.getMapProjection(acMap.getXml())).getEPSGCode());
							 AppContext.getApplicationContext().getBlackboard().put(Const.KEY_SRID_MAPA, String.valueOf(srid_destino));
						}
						
						
                        GeopistaLayer layer=loadLayer(null, acLayer.getSystemName(),sLocale,g,fn, true, srid_destino,monitor);
                        layer.setActiva(acLayer.isActive());
                        layer.setVisible(acLayer.isVisible());
//                        layer.setEditable(acLayer.isEditable());
                        if (layer!=null){
                            String sStyleXML=acLayer.getStyleXML();
                            if (sStyleXML!=null)
                            {
                                htStyleXMLs.put(String.valueOf(acLayer.getId_layer()),sStyleXML);
                                htLayers.put(String.valueOf(acLayer.getId_layer()),layer);
                            }
                        }
                    }
                }
            }
    		long endMils=Calendar.getInstance().getTimeInMillis();
			logger.info("Map Loaded Step 1:"+map.getName()+" :"+(endMils-startMils)+" mils");

            mRet=acMap.convert(htLayers,htStyleXMLs,map.getTimeStamp(),sLocale);
            //mRet.setName(map.getName());
            mRet.setSystemId(map.getSystemId());
            //mRet.setThumbnail(map.getThumbnail());
            //mRet.setTimeStamp(new Date());
			logger.info("Map Loaded Step 2:"+map.getName()+" :"+(endMils-startMils)+" mils");
        }catch(ACException ace){
			logger.error("loadMap(GeopistaMap, String, Geometry, FilterNode)",ace);
            throw ace;
        }catch(Exception e){
        	//System.out.println("PASO");
			logger.error("loadMap(GeopistaMap, String, Geometry, FilterNode)",e);
            throw e;
        }
        return mRet;
    }

    /** Bloquea y carga una region de una capa en el administrador de cartografia, aplicando
     *  un filtro adicional.
     */
    public GeopistaLayer extractLayer(String sLayer, String sLocale, Geometry g, FilterNode fn) throws LockException,ACException,NoIDException,CancelException{
        GeopistaLayer glRet=null;
        switch(lockLayer(sLayer,g)){
            case LOCK_LAYER_LOCKED:
                glRet=loadLayer(sLayer,sLocale,g,fn);
                break;
            default:
                // TODO: Comunicar error
                break;
        }
        return glRet;
    }


    public GeopistaLayer loadLayer(String sLayer, String sLocale, Geometry g, FilterNode fn) throws ACException,NoIDException,CancelException{
        return loadLayer(null,sLayer,sLocale,g,fn);
    }
    
    public GeopistaLayer loadLayer(IGeopistaMap gpMap,String sLayer, String sLocale, Geometry g,
    			FilterNode fn) throws ACException,NoIDException,CancelException{
    	
    	return loadLayer(gpMap, sLayer, sLocale, g, fn, true, null,null);
    }
    

    public GeopistaLayer loadLayer(IGeopistaMap gpMap, String sLayer, String sLocale, Geometry g,
            FilterNode fn, Integer sridDestino) throws ACException, NoIDException,CancelException {
        return loadLayer(gpMap, sLayer, sLocale, g, fn, true, sridDestino,null);}

    public GeopistaLayer loadLayer(IGeopistaMap gpMap, String sLayer, String sLocale, Geometry g,
            FilterNode fn, Integer sridDestino,TaskMonitor monitor) throws ACException, NoIDException,CancelException {
        return loadLayer(gpMap, sLayer, sLocale, g, fn, true, sridDestino,monitor);}

    public GeopistaLayer loadLayer(IGeopistaMap gpMap,String sLayer, String sLocale, Geometry g, FilterNode fn, 
			boolean bValidateData, Integer sridDestino,TaskMonitor monitor) throws ACException,NoIDException,CancelException{
    	
        return loadLayer(gpMap, sLayer, sLocale, g, fn, true, sridDestino,monitor,false);
    }

    
    /** Carga de una capa del administrador de cartografia con filtros geometricos y alfanumerico. */
    public GeopistaLayer loadLayer(IGeopistaMap gpMap,String sLayer, String sLocale, Geometry g, FilterNode fn, 
    			boolean bValidateData, Integer sridDestino,TaskMonitor monitor,boolean test) throws ACException,NoIDException,CancelException{
        GeopistaLayer lRet=null;
        ILayerManager layerManager= (gpMap!=null)? gpMap.getLayerManager() : null;
    	//long revisionActual = 0;
        try{
        	long startMils=Calendar.getInstance().getTimeInMillis();
        	logger.info("Loading layer:"+sLayer);
        	ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_LAYER);
            Hashtable p=new Hashtable();
            p.put(Const.KEY_LOCALE,sLocale);
            p.put(Const.KEY_LAYER,sLayer);

            if (g!=null)
                p.put(Const.KEY_GEOMETRY,"SRID="+g.getSRID()+";"+g.toText());
            p.put(Const.KEY_FILTER,fn);
            p.put(Const.KEY_VALIDATE_DATA, new Boolean(bValidateData));
            
            ISesion iSesion=null;
            if (test)
            	iSesion=sm.getSession();
            else
            	iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
            p.put(Const.KEY_MUNICIPALITIES, iSesion.getAlMunicipios());
            if (sridDestino != null ) {
                p.put(Const.KEY_SRID, sridDestino);
            }
            if (AppContext.getApplicationContext().getBlackboard().get(AppContext.VERSION) != null)
            	p.put(Const.KEY_VERSION, AppContext.getApplicationContext().getBlackboard().get(AppContext.VERSION));
            
            if(AppContext.getApplicationContext().getBlackboard().get(Const.KEY_LOAD_FEATURE_LAYER) != null && 
            		(Integer)AppContext.getApplicationContext().getBlackboard().get(Const.KEY_LOAD_FEATURE_LAYER)==0){
            	 p.put(Const.KEY_LOAD_FEATURE_LAYER,0);
            }
            
            query.setParams(p);
            
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            
            logger.info("Loading layer PASO1:"+sLayer+" Size of Message:"+swQuery.getBuffer().length()+" bytes");
            InputStream in=enviarPlano(sUrl,swQuery.toString(),monitor,test);     
            
            ACUtil.initSizeOf();
            
            ObjectInputStream ois=new ObjectInputStream(in);
            IACLayer acLayer=(IACLayer)readObject(ois);
			long endMils=Calendar.getInstance().getTimeInMillis();
			logger.info("Layer loaded Step 1:"+sLayer+" :"+(endMils-startMils)+" mils. Size of layer:"+ACUtil.deepSizeOf(acLayer)+" bytes");

			long featuresSize=0;
			
			
			int numElementos=0;
            try{
                GeopistaSchema schema=acLayer.buildSchema(sLocale);
                FeatureDataset features=new FeatureDataset(schema);
                if (g == null && acLayer instanceof IACDynamicLayer){
                	IACDynamicLayer dynamicLayer=null;
            		dynamicLayer=(IACDynamicLayer)acLayer;
            		lRet = dynamicLayer.convertDynamic((LayerManager)layerManager);
            		lRet.setDinamica(true);
            		//lRet.setEditable(false);
                }else
                	lRet=acLayer.convert((LayerManager)layerManager);
                try{
                	if (lRet.isDinamica() == false){
                		
		                for(;;){
		                	IACFeature acFeature=(IACFeature)readObject(ois);
		                	featuresSize+=ACUtil.deepSizeOf(acFeature);		
		                    GeopistaFeature gf=acFeature.convert(schema);
		                    if (gf!=null){
		                     	gf.setFireDirtyEvents(false);
		                        gf.setNew(false);
		                        gf.setLayer(lRet);
		                        gf.setDirty(false);
	                            gf.setLockedFeature(true);
	                            gf.setFireDirtyEvents(true);
	                            features.add(gf);
	                        }
		                    else{
		                    	try {
									logger.error("Geometria erronea en la capa:"+sLayer+" Error de Geometria:"+acFeature.getError());
								} catch (Exception e) {}
		                    }
		    				numElementos++;
		    				if (numElementos % 10000 ==0)
		    					logger.info("Cargando elementos de la capa..:"+sLayer+" Elementos:"+numElementos);

	                    }
                	}
                }catch(OptionalDataException ode){
                    if (ode.eof!=true)
						logger.error(
								"loadLayer(GeopistaMap, String, String, Geometry, FilterNode)"
										+ ode.getMessage(), ode);
                }catch (EOFException ee){
                }
                lRet.setFeatureCollection(features);
    			long endMils2=Calendar.getInstance().getTimeInMillis();
    			logger.info("Layer loaded Step 2:"+sLayer+" :"+(endMils2-startMils)+" mils"+ " Size:"+featuresSize/(1024*1000)+" Mb ("+featuresSize+") Elementos:"+numElementos);

               
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(NoIDException nie){
            throw nie;
        }catch(ACException e){
            //e.printStackTrace();
            lRet=new ACLayer(-1,"error","error","error").convert((LayerManager)layerManager);
            throw e;
        }catch(Exception e){
        	if ((monitor!=null) && (monitor.isCancelRequested())){
        		logger.warn("Carga de mapa cancelada");
				throw new CancelException();
        	}
			logger.error("loadLayer(GeopistaMap, String, String, Geometry, FilterNode)",e);
			
        }
        return lRet;
    }

   
    
    
    public IGeopistaLayer loadLayer(IGeopistaMap gpMap,IGeopistaLayer lRet, String sLocale, Geometry g, FilterNode fn, boolean bValidateData, Integer sridDestino) throws ACException,NoIDException{
        ILayerManager layerManager= (gpMap!=null)? gpMap.getLayerManager() : null;
    	long revisionActual = 0;
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_LAYER);
            Hashtable p=new Hashtable();
            p.put(Const.KEY_LOCALE,sLocale);
            p.put(Const.KEY_LAYER,lRet.getSystemId());
            if (g!=null)
                p.put(Const.KEY_GEOMETRY,"SRID="+g.getSRID()+";"+g.toText());
            p.put(Const.KEY_FILTER,fn);
            p.put(Const.KEY_VALIDATE_DATA, new Boolean(bValidateData));
            ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
            p.put(Const.KEY_MUNICIPALITIES, iSesion.getAlMunicipios());
            if (sridDestino != null ) {
                p.put(Const.KEY_SRID, sridDestino);
            }
            if (AppContext.getApplicationContext().getBlackboard().get(AppContext.VERSION) != null)
            	p.put(Const.KEY_VERSION, AppContext.getApplicationContext().getBlackboard().get(AppContext.VERSION));
            query.setParams(p);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);

            IACLayer acLayer=(IACLayer)readObject(ois);
            try{
                GeopistaSchema schema=acLayer.buildSchema(sLocale);
                FeatureDataset features=new FeatureDataset(schema);
                if (g == null && acLayer instanceof IACDynamicLayer){
            		((DynamicLayer)lRet).setTime(((IACDynamicLayer)acLayer).getTime());
                }
                lRet.setRevisionActual(acLayer.getRevisionActual());
               /* }else
                	lRet=acLayer.convert(layerManager);*/
                try{
                	if (lRet.isDinamica() == false){
		                for(;;){
		                	IACFeature acFeature=(IACFeature)readObject(ois);
		                    GeopistaFeature gf=acFeature.convert(schema);
		                    if (gf!=null){
		                     	gf.setFireDirtyEvents(false);
		                        gf.setNew(false);
		                        gf.setLayer(lRet);
		                        gf.setDirty(false);
	                            gf.setLockedFeature(true);
	                            gf.setFireDirtyEvents(true);
	                            features.add(gf);
	                        }
	                    }
                	}
                }catch(OptionalDataException ode){
                    if (ode.eof!=true)
						logger.error(
								"loadLayer(GeopistaMap, String, String, Geometry, FilterNode)"
										+ ode.getMessage(), ode);
                }catch (EOFException ee){
                }
                lRet.setFeatureCollection(features);
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(NoIDException nie){
            throw nie;
        }catch(ACException e){
            //e.printStackTrace();
            lRet=new ACLayer(-1,"error","error","error").convert((LayerManager)layerManager);
            throw e;
        }catch(Exception e){
			logger
					.error(
							"loadLayer(GeopistaMap, String, String, Geometry, FilterNode)",
							e);
        }
        return lRet;
    }
    
    private GeopistaFeature loadFeature(IGeopistaLayer layer, int iID, String sLocale, Integer sridDestino) throws NoIDException{
    	return loadFeature(layer, iID, sLocale, true, sridDestino);
    }
    
    /** Devuelve una lista de varias features del administrador de cartografia */
    public List loadFeatures(IGeopistaLayer layer, List iIDs, String sLocale, Integer sridDestino) throws NoIDException{
    	Iterator itFeatures = iIDs.iterator();
    	List listaFeatures = new ArrayList();
    	while (itFeatures.hasNext()){
    		listaFeatures.add(loadFeature(layer, Integer.parseInt((String)itFeatures.next()), sLocale, true, sridDestino));
    	}
    	return listaFeatures;
    }
    
    /** Carga de un feature del administrador de cartografia */
    private GeopistaFeature loadFeature(IGeopistaLayer layer, int iID, String sLocale, boolean bValidateData, Integer sridDestino) throws NoIDException{
        GeopistaFeature gfRet=null;
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_FEATURE);
            Hashtable p=new Hashtable();
            p.put(Const.KEY_LOCALE,sLocale);
            p.put(Const.KEY_LAYER,layer.getSystemId());
            p.put(Const.KEY_FILTER,FilterLeaf.equal(ACLayer.findPrimaryTable(layer)+".ID",new Integer(iID)));
            p.put(Const.KEY_VALIDATE_DATA, new Boolean(bValidateData));
            ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
            p.put(Const.KEY_MUNICIPALITIES, iSesion.getAlMunicipios());
            if (sridDestino != null ) {
                p.put(Const.KEY_SRID, sridDestino);
            }
            query.setParams(p);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                try{
                    for(;;){
                        IACFeature acFeature=(IACFeature)readObject(ois);
                        gfRet=acFeature.convert((GeopistaSchema)layer.getFeatureCollectionWrapper().getFeatureSchema());
                        if (gfRet!=null)
                        {
                            gfRet.setFireDirtyEvents(false);
                            gfRet.setLayer(layer);
                            gfRet.setSystemId(String.valueOf(iID));
                            gfRet.setNew(false);
                            gfRet.setLockedFeature(true);
                            gfRet.setFireDirtyEvents(true);
                        }
                    }
                }catch(OptionalDataException ode){
                    if (ode.eof!=true)
						logger.error("loadFeature(GeopistaLayer, int, String)"
								+ ode.getMessage(), ode);
                }catch (EOFException ee){
                }
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(NoIDException nie){
        }catch(ACException ace){
            //e.printStackTrace();
            //throw ace;
            return null;
        }catch(Exception e){
			logger.error("loadFeature(GeopistaLayer, int, String)", e);
        }
        return gfRet;
    }



    /** Bloquea una capa del administrador de cartografia
     *  */
    public int lockLayer(String sLayer, Geometry g) throws LockException,ACException{
        int iRet=-1;
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_LAYER_LOCK);
            Hashtable p=new Hashtable();
            p.put(Const.KEY_LAYER,sLayer);
            if (g!=null)
                p.put(Const.KEY_GEOMETRY,g.toText());
            query.setParams(p);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                try{
                    iRet=ois.readInt();
                    if (iRet!=LOCK_LAYER_LOCKED)
                        throw new LockException("LockException: "+iRet);
                }catch(OptionalDataException ode){
                    if (ode.eof!=true)
						logger.error("lockLayer(String, Geometry)"
								+ ode.getMessage(), ode);
                }catch (EOFException ee){
                }
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(LockException e){
            throw e;
        }catch(ACException ace){
            throw ace;
            //return LOCK_LAYER_ERROR;
        }catch(Exception e){
			logger.error("lockLayer(String, Geometry)", e);
        }
        return iRet;
    }

    /** Desbloquea una capa del administrador */
    public int unlockLayer(String sLayer) throws ACException{
        int iRet=-1;
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_LAYER_UNLOCK);
            Hashtable p=new Hashtable();
            p.put(Const.KEY_LAYER,sLayer);
            query.setParams(p);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                try{
                    iRet=ois.readInt();
                }catch(OptionalDataException ode){
                    if (ode.eof!=true)
						logger.error("unlockLayer(String)" + ode.getMessage(),
								ode);
                }catch (EOFException ee){
                }
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(ACException ace){
            throw ace;
            //return UNLOCK_LAYER_ERROR;
        }catch(Exception e){
			logger.error("unlockLayer(String)", e);
        }
        return iRet;
    }
    /** Bloquea una lista de features del administrador de cartografia 
     * @throws Exception*/    
    public int lockFeature(String sLayer, int iFeatureId) throws Exception{
    	ArrayList layers=new ArrayList();
    	ArrayList features=new ArrayList();
    	layers.add(sLayer);
    	features.add(new Integer(iFeatureId));
    	FeatureLockResult result=lockFeature(layers,features);
    	return result.getLockResultCode();
    }
    
    /** Bloquea una lista de features del administrador de cartografia 
     * @throws Exception*/    
    public FeatureLockResult lockFeature(List layers, List featureIds) throws Exception{ 
    	FeatureLockResult featureLockResult = null;
    	
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_FEATURE_LOCK);
            Hashtable p=new Hashtable();
            p.put(Const.KEY_LAYER, layers);
            p.put(Const.KEY_FEATURE, featureIds);
            query.setParams(p);
            
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                try{
                	featureLockResult = (FeatureLockResult) ois.readObject();                	
                } catch(OptionalDataException ode){
                    if (ode.eof!=true){
						logger.error("lockFeature(String, int)"
								+ ode.getMessage(), ode);
                    }
                } catch (EOFException ee){
                }
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        } catch(ACException ace){            
        	throw new LockException(ace.getMessage());
        }
        
        return featureLockResult;
    }
    /** Bloquea una lista de features del administrador de cartografia 
     * @throws Exception*/    
    public int unlockFeature(String sLayer, int iFeatureId) throws Exception{
    	ArrayList layers=new ArrayList();
    	ArrayList features=new ArrayList();
    	layers.add(sLayer);
    	features.add(new Integer(iFeatureId));
    	List result=unlockFeature(layers,features);
    	int size=0;
    	if (result!=null)
    		size=result.size();
    	return size;
    }  

    /* Desbloqueo de un feature */
    public List unlockFeature(List layers, List featureIds) throws Exception{
        List unlockResult = null;
        
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_FEATURE_UNLOCK);
            Hashtable p=new Hashtable();
            p.put(Const.KEY_LAYER,layers);
            p.put(Const.KEY_FEATURE,featureIds);
            query.setParams(p);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                try{
                    unlockResult = (List) ois.readObject();
                }catch(OptionalDataException ode){
                    if (ode.eof!=true)
						logger.error("unlockFeature(String, int)"
								+ ode.getMessage(), ode);
                }catch (EOFException ee){
                }
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(ACException ace){
            //throw ace;
            //return UNLOCK_FEATURE_ERROR;
        }
        
        return unlockResult;
    }

    /** Guarda en el servidor un array de features */
    public Collection uploadFeatures(String sLocale, Object[] aGeopistaFeatures) throws ACException,NoIDException{
        return  uploadFeatures(sLocale, aGeopistaFeatures, true, false, null);
    }
    public Collection uploadFeatures(String sLocale, Object[] aGeopistaFeatures,Integer sridDestino) throws ACException,NoIDException{
        return  uploadFeatures(sLocale, aGeopistaFeatures, true, false, null,sridDestino);
    }
    public Collection uploadFeatures(String sLocale, Object[] aGeopistaFeatures, boolean bLoadData) throws ACException,NoIDException{
        return  uploadFeatures(sLocale, aGeopistaFeatures, bLoadData, false, null);
    }
    
    public Collection uploadFeatures(String locale,
			Object[] sourceGeopistaFeatures, boolean loadData,
			TaskMonitor monitor) throws ACException, NoIDException {
    	return  uploadFeatures(locale, sourceGeopistaFeatures, loadData, false, monitor);
	}
    
    public Collection uploadFeatures(String sLocale, Object[] sourceGeopistaFeatures, 
            boolean bLoadData, boolean bValidateData, TaskMonitor monitor) throws ACException,NoIDException {
        return  uploadFeatures(sLocale, sourceGeopistaFeatures, bLoadData, bValidateData, monitor, null);
    }
    

    /** Guarda en el servidor un array de features */
    public Collection uploadFeatures(String sLocale, Object[] sourceGeopistaFeatures, 
    		boolean bLoadData, boolean bValidateData, TaskMonitor monitor, Integer sridDestino) throws ACException,NoIDException{
        ArrayList alRet=new ArrayList();
        
        //hacemos esto para enviar unicamente las features modificadas, nuevas o borradas
        ArrayList dirtyFeaturesArrayList = new ArrayList();
        for(int n=0;n<sourceGeopistaFeatures.length;n++)
        {
            if(((GeopistaFeature)sourceGeopistaFeatures[n]).isNew()||((GeopistaFeature)sourceGeopistaFeatures[n]).isDirty()||((GeopistaFeature)sourceGeopistaFeatures[n]).isDeleted())
            {
           		dirtyFeaturesArrayList.add(sourceGeopistaFeatures[n]); 
            }
        }
        if(dirtyFeaturesArrayList.size()==0) return alRet;
        Object[] dirtyFeaturesArray = dirtyFeaturesArrayList.toArray();
        
        for (int j=0; j<=(dirtyFeaturesArray.length/MAX_NUM_FEATURE_UPLOAD);j++)
        {
   	     if (monitor!=null)
        	monitor.report(j*MAX_NUM_FEATURE_UPLOAD,dirtyFeaturesArray.length,"Enviando.");
   
            int iFeaturesEnviar=MAX_NUM_FEATURE_UPLOAD;
            if (dirtyFeaturesArray.length/((j+1)*MAX_NUM_FEATURE_UPLOAD)==0)
                iFeaturesEnviar=dirtyFeaturesArray.length-(j*MAX_NUM_FEATURE_UPLOAD);

            ACFeatureUpload[] aUpload=new ACFeatureUpload[iFeaturesEnviar];
            int z=0;
            for (int i=j*MAX_NUM_FEATURE_UPLOAD;i<(j*MAX_NUM_FEATURE_UPLOAD)+iFeaturesEnviar;i++)
            {
	                aUpload[z]=new ACFeatureUpload((GeopistaFeature)dirtyFeaturesArray[i]);
	                z++;
            }
            GeopistaLayer layer=(GeopistaLayer)((GeopistaFeature)dirtyFeaturesArray[0]).getLayer();
            
            if (layer == null)
            {
                layer = (GeopistaLayer)((GeopistaSchema)((GeopistaFeature)dirtyFeaturesArray[0]).getSchema()).getGeopistalayer();
            }
            
            try{
                ACQuery query=new ACQuery();
                query.setAction(Const.ACTION_FEATURE_UPLOAD);
                Hashtable p=new Hashtable();
                
                //***************************************
                //Modificacion para la EIEL (Temporal)
                //***************************************
                if(AppContext.getApplicationContext().getBlackboard().get(Const.ESTADO_VALIDACION) != null && 
                		(Integer)AppContext.getApplicationContext().getBlackboard().get(Const.ESTADO_VALIDACION)==Const.ESTADO_PUBLICABLE_MOVILIDAD){
                	 p.put(Const.KEY_ESTADO_VALIDACION,Const.ESTADO_PUBLICABLE_MOVILIDAD);
                }
                else if (AppContext.getApplicationContext().getBlackboard().get(AppContext.PERMISOS) != null){
                	Hashtable permisos=	(Hashtable)AppContext.getApplicationContext().getBlackboard().get(AppContext.PERMISOS);
                	if(permisos.containsKey(Const.PERM_PUBLICADOR_EIEL)){
                		 if (AppContext.getApplicationContext().getBlackboard().get(Const.ESTADO_VALIDACION) != null){
                			 int estadoValidacion=	(Integer)AppContext.getApplicationContext().getBlackboard().get(Const.ESTADO_VALIDACION);
                			  p.put(Const.KEY_ESTADO_VALIDACION,estadoValidacion);
                		 }
                		 else
                			 p.put(Const.KEY_ESTADO_VALIDACION,Const.ESTADO_TEMPORAL);
            		}  
                	else if(permisos.containsKey(Const.PERM_VALIDADOR_EIEL)){
                		if (AppContext.getApplicationContext().getBlackboard().get(Const.ESTADO_VALIDACION) != null){
               			 	int estadoValidacion=	(Integer)AppContext.getApplicationContext().getBlackboard().get(Const.ESTADO_VALIDACION);
               			 	p.put(Const.KEY_ESTADO_VALIDACION,estadoValidacion);
                		}
                		else
                			p.put(Const.KEY_ESTADO_VALIDACION,Const.ESTADO_VALIDO);
                	}
                }
                
                
                p.put(Const.KEY_LOCALE,sLocale);
                if (AppContext.getApplicationContext().getBlackboard().get(AppContext.IMPORTACIONES) != null)
                	p.put(Const.KEY_IMPORTACIONES,AppContext.getApplicationContext().getBlackboard().get(AppContext.IMPORTACIONES));
                p.put(Const.KEY_LAYER,layer.getSystemId());
                p.put(Const.KEY_UPLOAD,aUpload);
                p.put(Const.KEY_MUNICIPALITIES, ((ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY)).getAlMunicipios());
                p.put(Const.KEY_LOAD_DATA, new Boolean(bLoadData));
                p.put(Const.KEY_VALIDATE_DATA, new Boolean(bValidateData));
                p.put(Const.KEY_MUNICIPIO, ((ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY)).getIdMunicipio());
                if (AppContext.getApplicationContext().getBlackboard().get(AppContext.VERSION) != null)
                	p.put(Const.KEY_VERSION, AppContext.getApplicationContext().getBlackboard().get(AppContext.VERSION));
                if (AppContext.getApplicationContext().getBlackboard().get(AppContext.SRID_INICIAL) != null)
                	if (AppContext.getApplicationContext().getBlackboard().get(AppContext.SRID_INICIAL) instanceof String)
                		p.put(Const.INITIAL_SRID, Integer.valueOf((String)AppContext.getApplicationContext().getBlackboard().get(AppContext.SRID_INICIAL)));
                	else
                		p.put(Const.INITIAL_SRID, AppContext.getApplicationContext().getBlackboard().get(AppContext.SRID_INICIAL));
                
                
                //Vamos a controlar si el SRID del mapa que tenemos cargado es el mismo que el del ultimo mapa que podria no ser asi
                
                if (sridDestino != null ) {
                    p.put(Const.KEY_SRID, sridDestino);
                }
                
                
                query.setParams(p);
                StringWriter swQuery=new StringWriter();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                new ObjectOutputStream(baos).writeObject(query);
                Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
                
                logger.info("Uploading Feature:"+layer.getSystemId()+" Size of Message:"+swQuery.getBuffer().length()+" bytes");
                InputStream in=enviarPlano(sUrl,swQuery.toString());

                if (bLoadData) //Recogemos los datos del servidor si se han cargado
                {
                    ObjectInputStream ois=new ObjectInputStream(in);
                    try{

                        GeopistaFeature gf=null;
                        int iIndex=j*MAX_NUM_FEATURE_UPLOAD;
                        for(;;){
                            Object o=readObject(ois); // El layer ya lo tenemos...
                            if (o instanceof IACFeature){
                                gf=((IACFeature)o).convert((GeopistaSchema)layer.getFeatureCollectionWrapper().getFeatureSchema());
                                if (gf!=null)
                                {
                                    gf.setLayer(layer);
                                    gf.setNew(false);
                                    gf.setDirty(false);
                                    alRet.add(gf);
                                    while (((GeopistaFeature)dirtyFeaturesArray[iIndex]).isDeleted())
                                        iIndex++;
                                     copyValues(gf,(GeopistaFeature)dirtyFeaturesArray[iIndex++]);
                                }
                          }
                     }
                    }catch(OptionalDataException ode){
                    if (ode.eof!=true)
                        logger.error("uploadFeatures(String, Object[])"
                                + ode.getMessage(), null);
                 }catch (EOFException ee){
                	 //ee.printStackTrace();
                }finally{
                    try{ois.close();}catch(Exception e){};
                }
              }else //actualizamos los valores de las features
              {
                    for(int iIndex=j*MAX_NUM_FEATURE_UPLOAD;iIndex<(j*MAX_NUM_FEATURE_UPLOAD)+iFeaturesEnviar;iIndex++)
                    {
                        ((GeopistaFeature)dirtyFeaturesArray[iIndex]).setNew(false);
                        ((GeopistaFeature)dirtyFeaturesArray[iIndex]).setDirty(false);
                    }
                    try
                    {
                        ObjectInputStream ois=new ObjectInputStream(in);
                        Object o=readObject(ois);
                    }catch(ACException acex)
                    {  throw acex;
                    }catch(Exception e){}
              }
            }catch(NoIDException nie){
                throw nie;
            }catch(ACException ace){
                throw ace;
            }catch(Exception e){
                logger.error("uploadFeatures(String, Object[])", e);
            }

        }
        return alRet;
    }

    private static void copyValues(GeopistaFeature src, GeopistaFeature dst){
    	
    	//bloqueo la feature para que no salten eventos de actualización ya que en este caso no hacen falta
    	dst.setFireDirtyEvents(false);
    	dst.setLockedFeature(false);
        dst.setAttributes(src.getAttributes());
        dst.setSystemId(src.getSystemId());
        dst.setNew(src.isNew());
        dst.setDirty(src.isDirty());
        dst.setLockedFeature(src.isLockedFeature());
        dst.setFireDirtyEvents(true);
    }

    /** Guarda un estilo en la BD (puede afectar a mas de una capa) */
    public void uploadStyle(IGeopistaLayer gLayer) throws ACException{
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_STYLE_UPLOAD);
            Hashtable p=new Hashtable();
            SLDStyleImpl style= (SLDStyleImpl)gLayer.getStyle(SLDStyle.class);
            p.put(Const.KEY_LAYER,gLayer.getSystemId());
            p.put(Const.KEY_STYLE,style.getSLD());
            query.setParams(p);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                readObject(ois); // El layer ya lo tenemos...
            }catch(OptionalDataException ode){
               if (ode.eof!=true)
					logger.error("uploadStyle(GeopistaLayer)"
							+ ode.getMessage(), ode);
            }catch (EOFException ee){
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(ACException ace){
            throw ace;
        }catch(Exception e){
			logger.error("uploadStyle(GeopistaLayer)", e);
        }
    }

    /** Actualiza los features modificados, agregados o borrados de una capa y la
        desbloquea. Devuelve los features insertados */
    public Collection synchronize(GeopistaLayer gLayer,String sLocale,Geometry g) throws ACException,LockException,NoIDException{
        //if (lockLayer(gLayer.getSystemId(),g)!=LOCK_FEATURE_OWN)
        //    throw new LockException("Not Locked: "+gLayer.getSystemId());
        Collection cRet=null;
        ArrayList alFeatures=new ArrayList();
        for(Iterator it=gLayer.getFeatureCollectionWrapper().getFeatures().iterator();it.hasNext();){
            GeopistaFeature feature=(GeopistaFeature)it.next();
            if (feature.isDirty() || feature.isNew() || feature.isDeleted())
                alFeatures.add(feature);
        }
        if (alFeatures.size()>0)
            cRet=uploadFeatures(sLocale,alFeatures.toArray());
        unlockLayer(gLayer.getSystemId());
        return cRet;
    }


    /** Carga de los IDs de features modificados de una capa */
    public int[] getModifiedFeatureIDs(int iLayer, long lDate){
        int[] aiRet=null;
        ACQuery query=new ACQuery();
        query.setAction(Const.ACTION_MODIFIED_FEATURES);
        Hashtable p=new Hashtable();
        p.put(Const.KEY_LAYER,String.valueOf(iLayer));
        p.put(Const.KEY_DATE,String.valueOf(lDate));
        query.setParams(p);
        StringWriter swQuery=new StringWriter();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectInputStream ois=null;
        try{
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ois=new ObjectInputStream(in);
            aiRet=(int[])readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
			logger.error("getModifiedFeatureIDs(int, long)" + ode.getMessage(),
					ode);
        }catch (EOFException ee){
        }catch(Exception e){
			logger.error("getModifiedFeatureIDs(int, long)", e);
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return aiRet;
    }

    /** Carga de los IDs de las layerfamilies de un mapa */
    public int[] getLayerFamilyIDs(IGeopistaMap map){
        int[] aiRet=null;
        ACQuery query=new ACQuery();
        query.setAction(Const.ACTION_LAYERFAMILY_IDS_BYMAP);
        Hashtable p=new Hashtable();
        p.put(Const.KEY_MAP,map.getSystemId());
        query.setParams(p);
        StringWriter swQuery=new StringWriter();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectInputStream ois=null;
        try{
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ois=new ObjectInputStream(in);
            aiRet=(int[])readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
			logger.error("getLayerFamilyIDs(GeopistaMap)" + ode.getMessage(),
					ode);
        }catch (EOFException ee){
        }catch(Exception e){
			logger.error("getLayerFamilyIDs(GeopistaMap)", e);
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return aiRet;
    }

    /** Carga de los IDs de las layerfamilies de un mapa */
    public int[] getLayerFamilyIDs(){
        int[] aiRet=null;
        ACQuery query=new ACQuery();
        query.setAction(Const.ACTION_LAYERFAMILY_IDS);
        query.setParams(new Hashtable());
        StringWriter swQuery=new StringWriter();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectInputStream ois=null;
        try{
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ois=new ObjectInputStream(in);
            aiRet=(int[])readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
			logger.error("getLayerFamilyIDs()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }catch(Exception e){
			logger.error("getLayerFamilyIDs()", e);
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return aiRet;
    }

    public IACLayerFamily[] getLayerFamilies(){
    	IACLayerFamily[] aACLayerFamilies=null;
        ACQuery query=new ACQuery();
        query.setAction(Const.ACTION_LAYERFAMILIES);
        query.setParams(new Hashtable());
        StringWriter swQuery=new StringWriter();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectInputStream ois=null;
        try{
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ois=new ObjectInputStream(in);
            aACLayerFamilies=(ACLayerFamily[])readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
			logger.error("getLayerFamilies()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }catch(Exception e){
			logger.error("getLayerFamilies()", e);
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return aACLayerFamilies;
    }


    /** Carga de los IDs de layers de una layerfamily */
    public String[] getLayerIDs(IGeopistaMap map,int iLayerFamilyPosition) throws ACException, Exception{
        String[] asRet=null;
        ACQuery query=new ACQuery();
        query.setAction(Const.ACTION_LAYER_IDS_BYMAP);
        Hashtable p=new Hashtable();
        p.put(Const.KEY_MAP,map.getSystemId());
        p.put(Const.KEY_POSITION,String.valueOf(iLayerFamilyPosition));
        query.setParams(p);
        StringWriter swQuery=new StringWriter();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectInputStream ois=null;
        try{
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ois=new ObjectInputStream(in);
            asRet=(String[])readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
			logger.error("getLayerIDs(GeopistaMap, int)" + ode.getMessage(),
					ode);
        }catch (EOFException ee){
        }catch(ACException ace){
			logger.error("getLayerIDs(GeopistaMap, int)",ace);
            throw ace;
        }catch(Exception e){
			logger.error("getLayerIDs(GeopistaMap, int)", e);
			throw e;
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return asRet;
    }

    /** Carga de los IDs de layers de una layerfamily */
    public String[] getLayerIDs(int iLayerFamilyID) throws ACException, Exception{
        String[] asRet=null;
        ACQuery query=new ACQuery();
        query.setAction(Const.ACTION_LAYER_IDS_BYLAYERFAMILY);
        Hashtable p=new Hashtable();
        p.put(Const.KEY_LAYERFAMILY,String.valueOf(iLayerFamilyID));
        query.setParams(p);
        StringWriter swQuery=new StringWriter();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectInputStream ois=null;
        try{
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ois=new ObjectInputStream(in);
            asRet=(String[])readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
			logger.error("getLayerIDs(int)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }catch(ACException ace){
			logger.error("getLayerIDs(GeopistaMap, int)",ace);
            throw ace;
        }catch(Exception e){
			logger.error("getLayerIDs(int)", e);
			throw e;
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return asRet;
    }

    /** Asigna el ID de mapa devuelto en caso de insercion */
    public IGeopistaMap saveMapMuni(IGeopistaMap map,String sLocale)throws ACException{
        return saveMap(map,sLocale,true);
    }
    public IGeopistaMap saveMap(IGeopistaMap map,String sLocale)throws ACException{
        return saveMap(map,sLocale,false);
    }
    public IGeopistaMap saveMap(IGeopistaMap map,String sLocale, boolean bMuni)throws ACException{
        try{
            ACQuery query=new ACQuery();
            query.setAction((bMuni?Const.ACTION_MAP_UPLOAD_MUNI:Const.ACTION_MAP_UPLOAD));
            Hashtable p=new Hashtable();
            p.put(Const.KEY_MAP_UPLOAD,new ACMap(map));
            p.put(Const.KEY_LOCALE,sLocale);
            query.setParams(p);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                Object oResponse=readObject(ois); // comprobar excepcion
                if (oResponse instanceof Integer)
                    map.setSystemId(String.valueOf(((Integer)oResponse).intValue()));
            }catch(OptionalDataException ode){
               if (ode.eof!=true)
					logger.error("saveMap(GeopistaMap, String)"
							+ ode.getMessage(), ode);
            }catch (EOFException ee){
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(ACException ace){
            throw ace;
        }catch(Exception e){
			logger.error("saveMap(GeopistaMap, String)", e);
        }
        return map;
    }
    
    public FeatureCollection getGeoRefAddress(String tipoVia, String via, String num_poli, String sLocale){
    	return getGeoRefAddress(tipoVia, via, num_poli, sLocale, true);
    }
    
    public FeatureCollection getGeoRefAddress(String tipoVia, String via, String num_poli, String sLocale, boolean bValidateData){
           FeatureCollection features = null;
           try{
               ACQuery query=new ACQuery();
               query.setAction(Const.ACTION_GEOREFADD);
               Hashtable ht=new Hashtable();
               if (tipoVia!=null) ht.put(Const.KEY_TIPO_VIA,tipoVia);
               if (via!=null) ht.put(Const.KEY_VIA,via);
               if (num_poli!=null) ht.put(Const.KEY_NUM_POLI,num_poli);
               ht.put(Const.KEY_LOCALE,sLocale);
               ht.put(Const.KEY_VALIDATE_DATA, new Boolean(bValidateData));
               query.setParams(ht);
               StringWriter swQuery=new StringWriter();
               ByteArrayOutputStream baos=new ByteArrayOutputStream();
               new ObjectOutputStream(baos).writeObject(query);
               Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
               InputStream in=enviarPlano(sUrl,swQuery.toString());
               ObjectInputStream ois=new ObjectInputStream(in);

               IACLayer acLayer=(IACLayer)readObject(ois);
               try{
                    GeopistaSchema schema=acLayer.buildSchema(sLocale);
                    features=new FeatureDataset(schema);
                    try{
                       for(;;){
                        IACFeature acFeature=(IACFeature)readObject(ois);
                           GeopistaFeature gf=acFeature.convert(schema);
                           if (gf!=null){
                               features.add(gf);
                           }
                        }
                    }
                     catch(OptionalDataException ode){
                          if (ode.eof!=true)
                          logger.error("getGeoRefAddress" + ode.getMessage(), ode);
                    }finally{
                   try{ois.close();}catch(Exception e){};
                    }
                    return features;
               }catch (EOFException ee){
               }finally{
                   try{ois.close();}catch(Exception e){};
               }
           }catch(Exception e){
               logger.error("getMaps(String)", e);
           }
           return features;
       }


    public void deleteMap(IGeopistaMap map,String sLocale) throws ACException{
        try{
            ACQuery query=new ACQuery();
            query.setAction(Const.ACTION_MAP_DELETE);
            Hashtable p=new Hashtable();
            p.put(Const.KEY_LOCALE,sLocale);
            p.put(Const.KEY_MAP,map.getSystemId());
            query.setParams(p);
            StringWriter swQuery=new StringWriter();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
            InputStream in=enviarPlano(sUrl,swQuery.toString());
            ObjectInputStream ois=new ObjectInputStream(in);
            try{
                Object oResponse=readObject(ois);
            }catch(OptionalDataException ode){
                if (ode.eof!=true)
					logger.error("deleteMap(GeopistaMap)" + ode.getMessage(),
							ode);
            }catch (EOFException ee){
            }finally{
                try{ois.close();}catch(Exception e){};
            }
        }catch(ACException ace){
            throw ace;
        }catch(Exception e){
			logger.error("deleteMap(GeopistaMap)", e);
        }
    }

  	private CResultadoOperacion enviar(String sUrl, String sMensaje) throws Exception{
        try
        {
		    InputStream sr = enviar(sUrl, sMensaje, null, null,null);
             try {
			    return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class,new InputStreamReader(sr));
		    } catch (Exception e) {
		    	return new CResultadoOperacion(false, "Error al hacer el casting:" + sr);
		    }
        }catch (Exception e)
        {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
            logger.error("Se ha producido un Excepcion al enviar los datos: "+sw.toString());
            throw (e);
        }
	}
  	/**
  	 * Modificamos el metodo para que reintente cuando se corta la conexion con el
  	 * Administrador de Cartografia.
  	 * @param sUrl
  	 * @param sMensaje
  	 * @return
  	 * @throws Exception
  	 */
    public  InputStream enviarPlano(String sUrl, String sMensaje) throws Exception{   
    	return enviarPlano(sUrl,sMensaje,null);
    }
    
    /**
  	 * Modificamos el metodo para que reintente cuando se corta la conexion con el
  	 * Administrador de Cartografia.
  	 * @param sUrl
  	 * @param sMensaje
  	 * @return
  	 * @throws Exception
  	 */
    public  InputStream enviarPlano(String sUrl, String sMensaje,TaskMonitor monitor) throws Exception{    	
    	return enviarPlano(sUrl,sMensaje,monitor,false);
    }

    public  InputStream enviarPlano(String sUrl, String sMensaje,TaskMonitor monitor,boolean test) throws Exception{    	
    		InputStream is=null;
    		try{
    			is=enviar(sUrl, sMensaje, null, null,monitor,test);
    		}
    		catch (AuthenticationException e ){
    			throw e;
    			//TODO Lo quitamos porque se debe de realizar con SSO
    			/*try {
    				logger.error("Volviendo a conectar contra el Administrador de Cartografia");
    				//TODO Lo quitamos porque se debe de realizar con SSO
					//sm.reloginNS();
					//is=enviar(sUrl, sMensaje, null, null,monitor);
    				
				} catch (Exception e1) {
                    AppContext.getApplicationContext().setUserPreference("geopista.user.login", null);
					e1.printStackTrace();
					throw new Exception();
				}*/
    		}
    		return is;
    }
   

    /**
     * Se envia la peticion al servidor.
     * @param sUrl
     * @param sMensaje
     * @param sUserName
     * @param sPassword
     * @return
     * @throws Exception
     */
    private InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword,TaskMonitor monitor)throws Exception{
    	return enviar(sUrl,sMensaje,sUserName,sPassword,monitor,false);
    }

    private InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword,TaskMonitor monitor,boolean test)
            throws Exception{
    	SecurityManager.setSm(sm);
		Credentials creds = null;
		//logger.info("Enviando con los siguiente datos."+sUserName+ " Password:"+sPassword);
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else {
			if (sm.getIdSesionNS() == null)
            {
				if (logger.isDebugEnabled())					
					logger.debug("enviar(String, String, String, String) - Usuario no autenticado");					
                creds = new UsernamePasswordCredentials("GUEST", "");
              }
            else
               creds = new UsernamePasswordCredentials(sm.getIdSesionNS(), sm.getIdSesionNS());
		}
		//logger.error("Credentials:"+creds);
		//create a singular HttpClient object
		org.apache.commons.httpclient.HttpClient client;
		if (test)
			client = AppContext.getHttpClient(sm);
		else
			client = AppContext.getHttpClient();
		
		if (AppContext.getApplicationContext() != null){
			int port = Integer.parseInt(AppContext.getApplicationContext().getString(AppContext.PORT_ADMCAR));
			Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), port);
			Protocol.registerProtocol("https", easyhttps);
	    	client.getHostConfiguration().setHost(sUrl, port, easyhttps);
    	}

		//establish a connection within 5 seconds
		//client.setConnectionTimeout(5000);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null) {
			client.getState().setCredentials(null, null, creds);
		}

        /* -- PostMethod --  */
        /*
		HttpMethod method = null;

		//create a method object
		method = new PostMethod(sUrl);
        if (SecurityManager.getIdMunicipio()!=null)
            ((PostMethod) method).addParameter(idMunicipio, SecurityManager.getIdMunicipio());
	    if (sMensaje!=null)
		    ((PostMethod) method).addParameter(mensajeXML, sMensaje);
        if (SecurityManager.getIdApp()!=null)
			((PostMethod) method).addParameter(IdApp, SecurityManager.getIdApp());
		method.setFollowRedirects(true);
        */
        /**/

        /* -- MultipartPostMethod -- */

        org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl+"?"+Const.KEY_MUNICIPIO+"="+sm.getIdMunicipioNS());

        if (sm.getIdMunicipioNS()!=null){
        	method.addParameter(idMunicipio, sm.getIdMunicipioNS());
        	
             //method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(idMunicipio, sm.getIdMunicipioNS()));
        }
        if (sm.getIdAppNS()!=null){
			 method.addParameter(IdApp, sm.getIdAppNS());
        }
        if (sMensaje!=null){
             //mPost.addParameter(mensajeXML, sMensaje);
            method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
        }
        method.setFollowRedirects(false);
        
        /**/

		//execute the method
		byte[] responseBody = null;
		try {
			ProcessCancel processCancel=null;
			if (monitor!=null){
				processCancel=new ProcessCancel(monitor,method);
				processCancel.start();
			}
			
			logger.debug("Sending request....");			
			client.executeMethod(method);
			logger.debug("Waiting for response....");			

			responseBody = method.getResponseBody();
			
			if (processCancel!=null)
				processCancel.terminateProcess();
			
			//responseBody=IOUtils.toByteArray(method.getResponseBodyAsStream()); 
		} catch (HttpException he) {
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
			logger.error("enviar(String, String, String, String) - Unable to connect to '"+ sUrl + "'", ioe);
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		if (logger.isDebugEnabled())			
			logger.debug("enviar(String, String, String, String) - Request Path: "+ method.getPath());			
		if (logger.isDebugEnabled())		
			logger.debug("enviar(String, String, String, String) - Request Query: "+ method.getQueryString());
		
		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
			if (logger.isDebugEnabled())					
				logger.debug("enviar(String, String, String, String)"
						+ requestHeaders[i]);					
		}

		//write out the response headers
		if (logger.isDebugEnabled())			
			logger.debug("enviar(String, String, String, String) - *** Response ***");			
		if (logger.isDebugEnabled())			
			logger.debug("enviar(String, String, String, String) - Status Line: "+ method.getStatusLine());
		
        int iStatusCode = method.getStatusCode();
        String sStatusLine=method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			if (logger.isDebugEnabled())
				logger.debug("enviar(String, String, String, String)"+ responseHeaders[i]);
		}

		//clean up the connection resources
		method.releaseConnection();
		method.recycle();
        if (iStatusCode==200)
        {
	        return new GZIPInputStream(new ByteArrayInputStream(responseBody));
        }
        else if (iStatusCode==401)
        {
            sm.unLoggedNS();
            throw new AuthenticationException(sStatusLine);
        }
        else{
            throw new Exception(sStatusLine);
        }
	}

    


    public IGeopistaLayer searchByAttribute(IGeopistaMap gpMap, IGeopistaLayer lRet, String sLocale,
            FilterNode fn, String attributeName, String attributeValue,
            boolean bValidateData, Integer sridDestino) throws ACException, NoIDException {
        ILayerManager layerManager= (gpMap!=null)? gpMap.getLayerManager() : null;
        try{
            ACQuery query = new ACQuery();
            query.setAction(Const.ACTION_SEARCH_ATTRIBUTE);
            Hashtable p = new Hashtable();
            p.put(Const.KEY_LOCALE, sLocale);
            p.put(Const.KEY_LAYER, lRet.getSystemId().toLowerCase());            
            p.put(Const.KEY_VALIDATE_DATA, new Boolean(bValidateData));
            p.put(Const.KEY_FILTER, fn);
            ISesion iSesion = (ISesion) AppContext.getApplicationContext().getBlackboard().get(
                    AppContext.SESION_KEY);
            p.put(Const.KEY_MUNICIPALITIES, iSesion.getAlMunicipios());
            if (sridDestino != null) {
                p.put(Const.KEY_SRID, sridDestino);
            }
            p.put(Const.KEY_ATTRIBUTE_NAME, attributeName);
            p.put(Const.KEY_ATTRIBUTE_VALUE, attributeValue);
            
            query.setParams(p);
            StringWriter swQuery = new StringWriter();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(query);
            Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
            InputStream in = enviarPlano(sUrl, swQuery.toString());
            ObjectInputStream ois = new ObjectInputStream(in);

            IACLayer acLayer = (IACLayer) readObject(ois);
            try {
                GeopistaSchema schema = acLayer.buildSchema(sLocale);
                FeatureDataset features = new FeatureDataset(schema);
                if (acLayer instanceof IACDynamicLayer){
                    IACDynamicLayer dynamicLayer=null;
                    dynamicLayer=(IACDynamicLayer)acLayer;
                    lRet = dynamicLayer.convertDynamic((LayerManager)layerManager);
                }else
                    lRet=acLayer.convert((LayerManager)layerManager);
                try {
                    for (;;) {
                        IACFeature acFeature = (IACFeature) readObject(ois);
                        GeopistaFeature gf = acFeature.convert(schema);
                        if (gf != null) {
                            gf.setFireDirtyEvents(false);
                            gf.setNew(false);
                            gf.setLayer(lRet);
                            gf.setDirty(false);
                            gf.setLockedFeature(true);
                            gf.setFireDirtyEvents(true);
                            features.add(gf);
                        }
                    }
                } catch (OptionalDataException ode) {
                    if (ode.eof != true)
                        logger.error("searchByAttribute(GeopistaMap, GeopistaLayer, String, Geometry, FilterNode, String, String, boolean, Integer)"
                                + ode.getMessage(), ode);
                } catch (EOFException ee) {
                }
                lRet.setFeatureCollection(features);
            } finally {
                try {
                    ois.close();
                } catch (Exception e) {
                }
            }
        } catch (NoIDException nie) {
            throw nie;
        } catch (ACException e) {
            lRet = new ACLayer(-1, "error", "error", "error").convert((LayerManager)layerManager);
            throw e;
        } catch (Exception e) {
            logger.error("searchByAttribute(GeopistaMap, GeopistaLayer, String, Geometry, FilterNode, String, String, boolean, Integer)", e);
        }
        return lRet;
    }
    
    /**
     * Obtiene la geometría que tiene un municipio
     * @param codMunicipio
     * @return
     */
    public Geometry obtenerGeometriaMunicipio(int codMunicipio){
        Geometry geom = null;
    	try{
	        ACQuery query=new ACQuery();
	        query.setAction(Const.ACTION_MUNICIPIO_GEOMETRY);
	        Hashtable ht = new Hashtable();
	        ht.put(Const.KEY_MUNICIPIO_GEOMETRY,codMunicipio);
	        query.setParams(ht);
	        StringWriter swQuery=new StringWriter();
	        ByteArrayOutputStream baos=new ByteArrayOutputStream();
	        new ObjectOutputStream(baos).writeObject(query);
	        Marshaller.marshal(new ACMessage(baos.toByteArray()),swQuery);
	        InputStream in=enviarPlano(sUrl,swQuery.toString());
	        ObjectInputStream ois=new ObjectInputStream(in);
	        try{
	        	geom=(Geometry)readObject(ois);
	        }catch(OptionalDataException ode){
	            if (ode.eof!=true)
					logger.error("obtenerGeometriaMunicipio()" + ode.getMessage(), ode);
	        }catch (EOFException ee){
	        }finally{
	            try{ois.close();}catch(Exception e){};
	        }
        }catch(Exception e){
			logger.error("obtenerGeometriaMunicipio()", e);
        }
    	return geom;
    }
    
    
    /**
     * Verificacion de Cancelacion del Proceso
     * @author satec
     *
     */
    class ProcessCancel extends Thread{
    	
    	boolean processContinue=true;
    	TaskMonitor monitor;
    	org.apache.commons.httpclient.HttpMethodBase method;
    	
    	public ProcessCancel(TaskMonitor monitor,org.apache.commons.httpclient.HttpMethodBase  method){
    		this.monitor=monitor;
    		this.method=method;
		}
    	public void terminateProcess(){
    		processContinue=false;
    	}
		public void run(){
	
			logger.info("Iniciando proceso de carga");
			
			while (processContinue){
				try {
					
					if (monitor.isCancelRequested()){
						logger.info("Solicitud de cancelacion recibida");
						method.abort();
						processContinue=false;
					}
					else
						Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			logger.info("Finalizando proceso de carga");
		
		}
			
    }	
    
    /** Ejemplo de uso del cliente */
    public static void main(String args[])throws Exception{
        // Instanciacion del cliente
        String sUrlPrefix="http://localhost:8081/Geopista/";
        com.geopista.security.SecurityManager.setsUrl(sUrlPrefix);
        //com.geopista.security.SecurityManager.login("SYSSUPERUSER", "SYSPASSWORD",
        com.geopista.security.SecurityManager.login("INTRANET", "intranet",//"PRUEBAS", "pruebas",
                                               "Geopista");
        /*I*/
        /*com.geopista.protocol.document.DocumentClient dClient= new com.geopista.protocol.document.DocumentClient(sUrlPrefix+"AdministradorCartografiaServlet/"+"DocumentServlet");
        /*java.text.SimpleDateFormat sdf= new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date date= sdf.parse("2006-05-16");
        com.geopista.protocol.document.DocumentBean documento= new com.geopista.protocol.document.DocumentBean();
        documento.setId(124);
        documento.setFechaEntradaSistema(date);

        byte[] b= dClient.getAttachedByteStream(documento);
        if (b==null) System.out.println("GetDocumentAction.doGetDocument CONTENIDO NULL");
        System.out.println("GetDocumentAction.doGetDocument LENGTH="+b.length);
        /*F*/
        AdministradorCartografiaClient acClient =new AdministradorCartografiaClient (sUrlPrefix+"/AdministradorCartografiaServlet");
        // Feature de un municipio para probar el filtro geometrico
        Feature f=(Feature)new WKTReader().read(new StringReader("POLYGON ((385586 4701116, 397937 4701116, 397937 4723004, 385586 4723004, 385586 4701116))")).getFeatures().get(0);
        Geometry gMunicipio=f.getGeometry();
        f=(Feature)new WKTReader().read(new StringReader("POLYGON((390459.31 4717052.79,390436.969 4717062.146,390437.97 4717064.81,390442.09 4717075.77,390453.09 4717071.87,390454.08 4717071.52,390461.56 4717068.8,390464.75 4717067.46,390463.91 4717064.62,390461.29 4717057.88,390459.61 4717053.56,390459.31 4717052.79))")).getFeatures().get(0);
        Geometry gParcela=f.getGeometry();
        GeopistaLayer layer=acClient.loadLayer("ambitos_gestion","es_ES",gMunicipio,FilterLeaf.equal("1",new Integer(1)));
        //GeopistaLayer layer=acClient.loadLayer(new GeopistaMap(),"seccionespostales","es_ES",gMunicipio,FilterLeaf.equal("1",new Integer(1)));
        GeopistaFeature gf=acClient.loadFeature(layer,6,"es_ES",null);
        //Object[] aoMaps=acClient.getMaps("es_ES").toArray();
        //Collection cMaps=acClient.getMaps("es_ES");
        //Iterator it=cMaps.iterator();
        //it.next();
        //GeopistaMap map1=acClient.loadMap((GeopistaMap)aoMaps[aoMaps.length-1],"es_ES",gMunicipio,FilterLeaf.equal("1",new Integer(1)));
        //GeopistaMap map2=acClient.loadMap((GeopistaMap)it.next(),"es_ES",gMunicipio,FilterLeaf.equal("1",new Integer(1)));
        //System.out.println(acClient.lockLayer("seccionespostales",gMunicipio));
        //System.out.println(acClient.unlockLayer(34083,10));
        //System.out.println(acClient.lockFeature("seccionespostales",99));
        //System.out.println(acClient.unlockFeature(34083,10,1));
        //int[] aiFamilies=acClient.getLayerFamilyIDs();
        //gf.setAttribute(0,new Double(81642.43));
        //System.out.println(gf.getAttribute(0));
        gf.setDirty(true);
        //int[] aFeatureIDs=acClient.getModifiedFeatureIDs(10,1);
        //gf.setNew(true);
        //gf.setSystemId(null);
        //acClient.lockFeature("seccionespostales",99);
        acClient.uploadFeatures("es_ES",new GeopistaFeature[]{gf});
        //acClient.unlockFeature("seccionespostales",99);
        //((GeopistaFeature)layer.getFeatureCollectionWrapper().getFeatures().get(1)).setDirty(true);
        //acClient.synchronize(layer,"es_ES",gMunicipio);
        //String [] asLayersByMap=acClient.getLayerIDs((GeopistaMap)aoMaps[0],1);
        //String [] asLayersByFamily=acClient.getLayerIDs(1);
        //acClient.saveMap(map1,"es_ES");
        //Collection cStyles=map1.getLayersStylesRelation();
        //map1.setSystemId(null);
        //acClient.saveMap(map1,"es_ES");
        //ACLayerFamily[] aFamilies=acClient.getLayerFamilies();
		if (logger.isDebugEnabled())
			{
			logger.debug("main(String) - OK");
			}
    }

	@Override
	public Collection synchronize(IGeopistaLayer gLayer, String sLocale,
			Geometry g) throws ACException, LockException, NoIDException {
		// TODO Auto-generated method stub
		return null;
	}





}
