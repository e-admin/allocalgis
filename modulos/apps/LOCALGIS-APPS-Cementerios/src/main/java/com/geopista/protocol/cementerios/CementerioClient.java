/**
 * CementerioClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;


/**
 * Clase que implementa las operaciones que se pueden llevar a cabo en BD desde las aplicaciones cliente
 */

public class CementerioClient {
    private static final Log logger= LogFactory.getLog(CementerioClient.class);

    public static final String	mensajeXML	= "mensajeXML";
    public static final String	idMunicipio	= "idMunicipio";
    public static final String	IdApp		= "IdApp";


    private String sUrl=null;
    private SecurityManager sm=null;

    /**
     * Inicializa la conexion al servidor
     * @param sUrl del servidor
     */
    public CementerioClient(String sUrl){
        this.sm=SecurityManager.getSm();
        this.sUrl=sUrl;
    }

    public CementerioClient(String sUrl, SecurityManager sm){
        this.sm=sm;
        this.sUrl=sUrl;
    }

    private Object readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException,ACException{
       Object oRet=null;
       oRet=ois.readObject();
       if (oRet instanceof ACException){
           throw (ACException)oRet;
       }

       return oRet;
   }



    /**
     * Retorna los elementos del cementerio en BD en funcion de la accion
     * @param action accion que ejecuta el servlet
     * @param patron
     * @param features
     * @return coleccion de elementos de cementerio correspondiente a la accion
     * @throws Exception
     */
    public Collection getElementosCementerio(int action, String superpatron, String patron, String cadena, Collection filtro, Object[] features,  Integer idCementerio) throws Exception{
    Collection cRet=null;
    
    ACQuery query= new ACQuery();
    
    Object[] idFeatures= new ArrayList().toArray();
    Object[] idLayers= new ArrayList().toArray();

    if (features!=null){
        idFeatures= new Object[features.length];
        idLayers= new Object[features.length];
        for (int i=0;i<features.length;i++){
            GeopistaFeature feature= (GeopistaFeature)features[i];
            idFeatures[i]= (String)feature.getSystemId();
            idLayers[i]= (String)feature.getLayer().getSystemId();
        }
    }

    query.setAction(action);
    
    Hashtable params= new Hashtable();
    
    /* NO SE PUEDEN PASAR VALORES null */
    
    params.put(Const.KEY_SUPERPATRON, superpatron);
    params.put(Const.KEY_PATRON, patron);
    params.put(Const.KEY_CADENA_BUSQUEDA, cadena!=null?cadena:"");
    params.put(Const.KEY_FILTRO_BUSQUEDA, filtro!=null?filtro.toArray():new ArrayList().toArray());    
    params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
    params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
    params.put(Const.KEY_FECHA_VERSION, Const.fechaVersion);
    params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    params.put(Const.KEY_IDCEMENTERIO, idCementerio);
    query.setParams(params);
    
    StringWriter swQuery= new StringWriter();
    ByteArrayOutputStream baos= new ByteArrayOutputStream();
    new ObjectOutputStream(baos).writeObject(query);
    Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    
    InputStream in= enviarCementerio(sUrl, swQuery.toString());
    
    ObjectInputStream ois= new ObjectInputStream(in);
    cRet= new ArrayList();
    try{
        for(;;){
            Object obj= readObject(ois);
            if (obj instanceof UnidadEnterramientoBean){
                cRet.add((UnidadEnterramientoBean)obj);
            }else if (obj instanceof ConcesionBean){
            	cRet.add((ConcesionBean)obj);
            }else if (obj instanceof BloqueBean){
            	cRet.add((BloqueBean)obj);
			}else if (obj instanceof DifuntoBean){
				cRet.add((DifuntoBean)obj);
			}else if (obj instanceof PersonaBean){
				cRet.add((PersonaBean)obj);
			}else if (obj instanceof TarifaBean){
				cRet.add((TarifaBean)obj);
			}else if (obj instanceof InhumacionBean){
				cRet.add((InhumacionBean)obj);
			}else if (obj instanceof ExhumacionBean){
				cRet.add((ExhumacionBean)obj);
			}else if (obj instanceof IntervencionBean){
				cRet.add((IntervencionBean)obj);
			}
            
        }
    }catch(OptionalDataException ode){
        if (ode.eof!=true)
            logger.error("getElementosCementerio()" + ode.getMessage(), ode);
    }catch (EOFException ee){
    }finally{
        try{ois.close();}catch(Exception e){};
    }
    return cRet;
   }
    
    /**
     * Retorna los elementos del cementerio en BD en funcion de la accion
     * @param action accion que ejecuta el servlet
     * @param patron
     * @param features
     * @return coleccion de elementos de cementerio correspondiente a la accion
     * @throws Exception
     */
    public Collection getAllElems(int action, String superpatron, String patron, String cadena, Collection filtro, Object[] features, Integer idCementerio) throws Exception{
    Collection cRet=null;
    
    ACQuery query= new ACQuery();
    
    Object[] idFeatures= new ArrayList().toArray();
    Object[] idLayers= new ArrayList().toArray();

    if (features!=null){
        idFeatures= new Object[features.length];
        idLayers= new Object[features.length];
        for (int i=0;i<features.length;i++){
            GeopistaFeature feature= (GeopistaFeature)features[i];
            idFeatures[i]= (String)feature.getSystemId();
            idLayers[i]= (String)feature.getLayer().getSystemId();
        }
    }

    query.setAction(action);
    
    Hashtable params= new Hashtable();
    
    /* NO SE PUEDEN PASAR VALORES null */
    
    params.put(Const.KEY_SUPERPATRON, superpatron);
    params.put(Const.KEY_PATRON, patron);
    params.put(Const.KEY_CADENA_BUSQUEDA, cadena!=null?cadena:"");
    params.put(Const.KEY_FILTRO_BUSQUEDA, filtro!=null?filtro.toArray():new ArrayList().toArray());    
    params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
    params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
    params.put(Const.KEY_FECHA_VERSION, Const.fechaVersion);
    params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    params.put(Const.KEY_IDCEMENTERIO,idCementerio );
    query.setParams(params);
    
    StringWriter swQuery= new StringWriter();
    ByteArrayOutputStream baos= new ByteArrayOutputStream();
    new ObjectOutputStream(baos).writeObject(query);
    Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    
    InputStream in= enviarCementerio(sUrl, swQuery.toString());
    
    ObjectInputStream ois= new ObjectInputStream(in);
    cRet= new ArrayList();
    try{
        for(;;){
            Object obj= readObject(ois);
            if (obj instanceof UnidadEnterramientoBean){
                cRet.add((UnidadEnterramientoBean)obj);
            }
	        else if (obj instanceof PersonaBean){
	        	cRet.add((PersonaBean)obj);
	        }
	        else if (obj instanceof TarifaBean){
		          cRet.add((TarifaBean)obj);
		    }
	        else if (obj instanceof ConcesionBean){
		          cRet.add((ConcesionBean)obj);
		    }
	        else if (obj instanceof BloqueBean){
		          cRet.add((BloqueBean)obj);
		    }
	        else if (obj instanceof DifuntoBean){
		          cRet.add((DifuntoBean)obj);
		    }
	        else if (obj instanceof InhumacionBean){
		          cRet.add((InhumacionBean)obj);
		    }
	        else if (obj instanceof ExhumacionBean){
		          cRet.add((ExhumacionBean)obj);
		    }
	        else if (obj instanceof HistoricoDifuntoBean){
		          cRet.add((HistoricoDifuntoBean)obj);
		    }
	        else if (obj instanceof HistoricoPropiedadBean){
		          cRet.add((HistoricoPropiedadBean)obj);
		    }
	        else if (obj instanceof IntervencionBean){
		          cRet.add((IntervencionBean)obj);
		    }
        }
    }catch(OptionalDataException ode){
        if (ode.eof!=true)
            logger.error("getElementosCementerio()" + ode.getMessage(), ode);
    }catch (EOFException ee){
    }finally{
        try{ois.close();}catch(Exception e){};
    }
    return cRet;
   }


    public Collection getCamposElem(int action, String superpatron, String patron) throws Exception{
        Collection cRet=null;
        
        ACQuery query= new ACQuery();
        query.setAction(action);
        
        Hashtable params= new Hashtable();
        
        /* NO SE PUEDEN PASAR VALORES null */
        
        params.put(Const.KEY_SUPERPATRON, superpatron);
        params.put(Const.KEY_PATRON, patron);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        
        InputStream in= enviarCementerio(sUrl, swQuery.toString());
        
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            for(;;){
                Object obj= readObject(ois);
                if (obj instanceof CampoFiltro){
                    cRet.add((CampoFiltro)obj);
                }
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getCampos de Elemento()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
       } 
    
    
    public Object getPlaza(int action, long id) throws Exception{

    	ACQuery query= new ACQuery();
    	Object obj = null;

    	query.setAction(action);
    	Hashtable params= new Hashtable();
    	
    	params.put(Const.KEY_IDELEMENTOCEMENTERIO, id);    	
    	params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	
    	InputStream in= enviarCementerio(sUrl, swQuery.toString());
    	
    	ObjectInputStream ois= new ObjectInputStream(in);

    	try{
    		for(;;){
    			obj= readObject(ois);
    		}
    		
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("getPlaza()" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}
    	return obj;
    }
    
    
    public Collection getHistoricos(int action, String superpatron, String patron, String cadena, Collection filtro, Object[] features,  Integer idCementerio, Object object) throws Exception{
        Collection cRet=null;
        
        ACQuery query= new ACQuery();
        
        Object[] idFeatures= new ArrayList().toArray();
        Object[] idLayers= new ArrayList().toArray();

        if (features!=null){
            idFeatures= new Object[features.length];
            idLayers= new Object[features.length];
            for (int i=0;i<features.length;i++){
                GeopistaFeature feature= (GeopistaFeature)features[i];
                idFeatures[i]= (String)feature.getSystemId();
                idLayers[i]= (String)feature.getLayer().getSystemId();
            }
        }

        query.setAction(action);
        
        Hashtable params= new Hashtable();
        
        /* NO SE PUEDEN PASAR VALORES null */
        
        params.put(Const.KEY_SUPERPATRON, superpatron);
        params.put(Const.KEY_PATRON, patron);
        params.put(Const.KEY_CADENA_BUSQUEDA, cadena!=null?cadena:"");
        params.put(Const.KEY_FILTRO_BUSQUEDA, filtro!=null?filtro.toArray():new ArrayList().toArray());    
        params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
        params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
        params.put(Const.KEY_FECHA_VERSION, Const.fechaVersion);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(Const.KEY_IDCEMENTERIO, idCementerio);
        params.put(Const.KEY_ID_DIFUNTO, object);
        query.setParams(params);
        
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        
        InputStream in= enviarCementerio(sUrl, swQuery.toString());
        
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            for(;;){
                Object obj= readObject(ois);
                if (obj instanceof HistoricoDifuntoBean){
                    cRet.add((HistoricoDifuntoBean)obj);
                }
                if (obj instanceof HistoricoPropiedadBean){
                    cRet.add((HistoricoPropiedadBean)obj);
                }
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getElementosCementerio()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
       }
    
    public Object getCementerio(int action, Integer id) throws Exception{

    	ACQuery query= new ACQuery();
    	Object obj = null;
    	if(id != null){
	    	query.setAction(action);
	    	Hashtable params= new Hashtable();
	    	
	    	params.put(Const.KEY_IDELEMENTOCEMENTERIO, id);    	
	    	params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
	    	query.setParams(params);
	    	StringWriter swQuery= new StringWriter();
	    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
	    	new ObjectOutputStream(baos).writeObject(query);
	    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
	    	
	    	InputStream in= enviarCementerio(sUrl, swQuery.toString());
	    	
	    	ObjectInputStream ois= new ObjectInputStream(in);
	
	    	try{
	    		for(;;){
	    			obj= readObject(ois);
	    		}
	    		
	    	}catch(OptionalDataException ode){
	    		if (ode.eof!=true)
	    			logger.error("getPlaza()" + ode.getMessage(), ode);
	    	}catch (EOFException ee){
	    	}finally{
	    		try{ois.close();}catch(Exception e){};
	    	}
		}
    	return obj;
    }
    
    

//    /**
//     * Retorna los bienes de inventario en BD en funcion de la accion
//     * @param action accion que ejecuta el servlet
//     * @param patron
//     * @param features
//     * @return coleccion de bienes de inventario correspondiente a la accion
//     * @throws Exception
//     */
//    public Object getUnidadEnterramiento(int action, String superpatron,String patron, long id) throws Exception{
//
//    	ACQuery query= new ACQuery();
//    	Object obj = null;
//
//    	query.setAction(action);
//    	Hashtable params= new Hashtable();
//    	/* NO SE PUEDEN PASAR VALORES null */
//    	if (superpatron!=null)
//        	params.put(Const.KEY_SUPERPATRON, superpatron);
//    	params.put(Const.KEY_PATRON, patron);
//    	
//    	params.put(Const.KEY_IDELEMENTOCEMENTERIO, id);    	
//    	params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
//    	query.setParams(params);
//    	StringWriter swQuery= new StringWriter();
//    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
//    	new ObjectOutputStream(baos).writeObject(query);
//    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
//    	InputStream in= enviarCementerio(sUrl, swQuery.toString());
//    	ObjectInputStream ois= new ObjectInputStream(in);
//
//    	try{
//    		for(;;){
//    			obj= readObject(ois);
//    		}
//    		
//    	}catch(OptionalDataException ode){
//    		if (ode.eof!=true)
//    			logger.error("getBienInventario()" + ode.getMessage(), ode);
//    	}catch (EOFException ee){
//    	}finally{
//    		try{ois.close();}catch(Exception e){};
//    	}
//    	return obj;
//    }
//
//    public Object eliminarUnidadEnterramiento(Object[] features, Object obj) throws Exception{
//        if (obj == null){
//            throw new Exception("Objeto no valido");
//        }
//
//        Object unidadEnterramiento= null;
//        Object[] idFeatures= new Object[features.length];
//        Object[] idLayers= new Object[features.length];
//        for (int i=0;i<features.length;i++){
//            GeopistaFeature feature= (GeopistaFeature)features[i];
//            idFeatures[i]= (String)feature.getSystemId();
//            idLayers[i]= (String)feature.getLayer().getSystemId();
//        }
//        ACQuery query= new ACQuery();
//        
//        query.setAction(Const.ACTION_DELETE);
//        Hashtable params= new Hashtable();
//        params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
//        params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
//        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
//        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
//        
//        query.setParams(params);
//
//        StringWriter swQuery= new StringWriter();
//        ByteArrayOutputStream baos= new ByteArrayOutputStream();
//        new ObjectOutputStream(baos).writeObject(query);
//        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
//
//        InputStream in= enviarCementerio(sUrl, swQuery.toString());
//        
//        ObjectInputStream ois= new ObjectInputStream(in);
//        try{
//        	unidadEnterramiento= readObject(ois);
//        }catch(OptionalDataException ode){
//            if (ode.eof!=true)
//                logger.error("eliminarUnidad(Object obj)" + ode.getMessage(), ode);
//        }catch (EOFException ee){
//        }finally{
//            try{ois.close();}catch(Exception e){};
//        }
//
//        return unidadEnterramiento;
//     }
//
//    
//
//    
//    
//    /**
//     * Marca como BORRADO un bien en BD
//     * @param obj bien a marcar como borrado
//     */
//    public Object borrarUnidadEnterramiento(Object obj) throws Exception{
//        if (obj == null){
//            throw new Exception("UnidadEnterramiento no valido");
//        }
//
//        Object unidadEnterramiento= null;
//        ACQuery query= new ACQuery();
//        query.setAction(Const.ACTION_DELETE);
//        Hashtable params= new Hashtable();
//        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
//        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
//        query.setParams(params);
//        StringWriter swQuery= new StringWriter();
//        ByteArrayOutputStream baos= new ByteArrayOutputStream();
//        new ObjectOutputStream(baos).writeObject(query);
//        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
//        InputStream in= enviarCementerio(sUrl, swQuery.toString());
//        ObjectInputStream ois= new ObjectInputStream(in);
//        try{
//        	unidadEnterramiento= readObject(ois);
//        }catch(OptionalDataException ode){
//            if (ode.eof!=true)
//                logger.error("borrarInventario(Object obj)" + ode.getMessage(), ode);
//        }catch (EOFException ee){
//        }finally{
//            try{ois.close();}catch(Exception e){};
//        }
//
//        return unidadEnterramiento;
//     }

    public Collection getPlantillasJR(String path) throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_PLANTILLAS);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_PATH_PLANTILLAS, path);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarCementerio(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            for(;;){
                cRet.add(readObject(ois));
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getPlantillasJR()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }

    /*CONCESIONES*/

    /**
     * insertConcesion
     */
    public Object insertConcesion(Object[] features, Object obj, Vector files) throws Exception{
        
    	if (obj == null){
            throw new Exception("Concesion no valida");
        }
        Object concesion= null;
        ACQuery query= new ACQuery();
        
        query.setAction(Const.ACTION_INSERT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        
        InputStream in= enviarCementerio(sUrl, swQuery.toString(), files);
        
        ObjectInputStream ois= new ObjectInputStream(in);
        
        try{
        	concesion= readObject(ois); 
        	
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("insertConcesion(Object[] features, Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return concesion;
     }
    
    /**
     * UpdateConcesion
	 */
    public Object updateConcesion(Object obj, Vector files) throws Exception{
        if (obj == null){
            throw new Exception("Concesion no valido");
        }

        Object concesion= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_UPDATE);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarCementerio(sUrl, swQuery.toString(), files);
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
        	concesion= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("updateconcesion(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return concesion;
     }

    /**
     * deleteConcesion
     */
    public Object deleteConcesion(Object obj,Vector files) throws Exception{
        if (obj == null){
            throw new Exception("Concesion no valido");
        }

        Object concesion= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_DELETE);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarCementerio(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
        	concesion= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("deleteConcesion(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return concesion;
     }

    
    
    /*********************************************  GENERICOS **********************************************************/
     /**
     * generico insert
     */
    public Object insert(Object[] features, Object obj, Vector files, Integer idCementerio) throws Exception{
    	Object objeto;
    	if (features == null){
    		objeto = insertWithoutFeature(obj,files, idCementerio); 
    	}else{
    		objeto = insertWithFeature(features, obj,files, idCementerio);
    	}
    	return objeto;
    }
    
    
    public Object insertWithoutFeature(Object obj, Vector files, Integer idCementerio) throws Exception{
        if (obj == null){
            throw new Exception("Inserccion no valida");
        }
        Object objeto= null;
        
        ACQuery query= new ACQuery();
        
        query.setAction(Const.ACTION_INSERT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(Const.KEY_IDCEMENTERIO, idCementerio);
        
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        
        InputStream in= enviarCementerio(sUrl, swQuery.toString(), files);
        ObjectInputStream ois= new ObjectInputStream(in);
        
        try{
        	objeto= readObject(ois); 
        	
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("insertCementerio(Object[] features, Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return objeto;
     }
    
    
    /**
     * insert sin features
     * @param obj
     * @param files
     * @return
     * @throws Exception
     */
    public Object insertWithFeature(Object[] features, Object obj, Vector files, Integer idCementerio) throws Exception{
        if (obj == null){
            throw new Exception("Inserccion no valida");
        }
        Object objeto= null;
        Object[] idFeatures= new Object[features.length];
        Object[] idLayers= new Object[features.length];
        for (int i=0;i<features.length;i++){
            GeopistaFeature feature= (GeopistaFeature)features[i];
            idFeatures[i]= (String)feature.getSystemId();
            idLayers[i]= (String)feature.getLayer().getSystemId();
        }
        ACQuery query= new ACQuery();
        
        query.setAction(Const.ACTION_INSERT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
        params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(Const.KEY_IDCEMENTERIO, idCementerio);
        
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        
        InputStream in= enviarCementerio(sUrl, swQuery.toString(), files);
        ObjectInputStream ois= new ObjectInputStream(in);
        
        try{
        	objeto= readObject(ois); 
        	
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("insertCementerio(Object[] features, Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return objeto;
     }
    
    /**
     * UpdateTitular
	 */
    public Object update(Object obj, Vector files) throws Exception{
        if (obj == null){
            throw new Exception("update de ojeto  no valido");
        }

        Object objeto= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_UPDATE);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarCementerio(sUrl, swQuery.toString(), files);
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
        	objeto= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("updateconcesion(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return objeto;
     }

    /**
     * deleteConcesion
     */
    public Object delete(Object[] features, Object obj, Vector files, Integer idCementerio) throws Exception{
        if (obj == null){
            throw new Exception("Concesion no valido");
        }

        
        Object objeto= null;
        Object[] idFeatures= new Object[features.length];
        Object[] idLayers= new Object[features.length];
        for (int i=0;i<features.length;i++){
            GeopistaFeature feature= (GeopistaFeature)features[i];
            idFeatures[i]= (String)feature.getSystemId();
            idLayers[i]= (String)feature.getLayer().getSystemId();
        }
        ACQuery query= new ACQuery();
        
        query.setAction(Const.ACTION_DELETE);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
        params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(Const.KEY_IDCEMENTERIO, idCementerio);
        
        
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarCementerio(sUrl, swQuery.toString());
        
        ObjectInputStream ois= new ObjectInputStream(in);
        
        try{
        	objeto= readObject(ois);
        	
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("deleteConcesion(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return objeto;
     }
    
    public InputStream enviarCementerio(String sUrl, String sMensaje) throws Exception{
          return enviar(sUrl, sMensaje, null, null, null);
    }

    public InputStream enviarCementerio(String sUrl, String sMensaje, Vector files) throws Exception{
          return enviar(sUrl, sMensaje, null, null, files);
    }


    private  InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword, Vector files)
            throws Exception{
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else {
			if (sm.getIdSesionNS() == null){
				if (logger.isDebugEnabled()){
					logger.debug("enviar(String, String, String, String) - Usuario no autienticado");
				}
                creds = new UsernamePasswordCredentials("GUEST", "");
            }else
               creds = new UsernamePasswordCredentials(sm.getIdSesionNS(), sm.getIdSesionNS());
		}
		//create a singular HttpClient object
		org.apache.commons.httpclient.HttpClient client= AppContext.getHttpClient();

		//establish a connection within 5 seconds
		//client.setConnectionTimeout(5000);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null) {
			client.getState().setCredentials(null, null, creds);
		}

        /* -- MultipartPostMethod -- */
        org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);

        if (sm.getIdMunicipioNS()!=null){
             method.addParameter(idMunicipio, sm.getIdMunicipioNS());
        }
        if (SecurityManager.getIdApp()!=null){
			 method.addParameter(IdApp, sm.getIdAppNS());
        }
        if (sMensaje!=null){
            //method.addParameter(mensajeXML, sMensaje);
            method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
        }

        /** Necesario para la aplicacion de Inventario. En la aplicacion se pasa un vector files a insertar o modificar */
        if (files != null){
            for (int i=0; i<files.size(); i++){
                File file= (File)files.get(i);
                /** Debido a que el nombre del fichero puede contener acentos. */
                try{method.addParameter(URLEncoder.encode(i+"FILE"+file.getName(), "ISO-8859-1"), file);}catch(java.io.FileNotFoundException e){/** Fichero vacio */}
            }
        }

        method.setFollowRedirects(false);

		//execute the method
		byte[] responseBody = null;
		try {
			client.executeMethod(method);
			responseBody = method.getResponseBody();
		} catch (HttpException he) {
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
			logger.error(
					"enviar(String, String, String, String) - Unable to connect to '"
							+ sUrl + "'", ioe);
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		if (logger.isDebugEnabled()){
			logger
					.debug("enviar(String, String, String, String) - Request Path: "
							+ method.getPath());
		}
		if (logger.isDebugEnabled()){
			logger
					.debug("enviar(String, String, String, String) - Request Query: "
							+ method.getQueryString());
		}
		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
				if (logger.isDebugEnabled()){
					logger.debug("enviar(String, String, String, String)"
							+ requestHeaders[i]);
				}
		}

		//write out the response headers
		if (logger.isDebugEnabled()){
			logger.debug("enviar(String, String, String, String) - *** Response ***");
		}
		if (logger.isDebugEnabled()){
			logger.debug("enviar(String, String, String, String) - Status Line: "
							+ method.getStatusLine());
		}
        int iStatusCode = method.getStatusCode();
        String sStatusLine=method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			if (logger.isDebugEnabled()){
				logger.debug("enviar(String, String, String, String)"
						+ responseHeaders[i]);
			}
		}

		//clean up the connection resources
		method.releaseConnection();
		method.recycle();
        if (iStatusCode==200){
	        return new GZIPInputStream(new ByteArrayInputStream(responseBody));
        }
        else
            throw new Exception(sStatusLine);
	}


    public Collection getReferenciasCatastrales(int action, String patron) throws Exception{
        
    	Collection cRet=null;
        ACQuery query= new ACQuery();
                
        query.setAction(action);
        Hashtable params= new Hashtable();
        /* NO SE PUEDEN PASAR VALORES null */
        
        params.put(Const.KEY_PATRON, patron);      
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarCementerio(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            for(;;){
                Object obj= readObject(ois);
                if (obj instanceof CReferenciaCatastral){
                    cRet.add((CReferenciaCatastral)obj);
                }
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getReferenciasCatastrales()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
       }

    /**
     * Calcula la hora en base de datos
     */
    public Object getHora(int action) throws Exception{

    	ACQuery query= new ACQuery();
    	Object obj = null;

    	query.setAction(action);
    	Hashtable params= new Hashtable();
    	params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarCementerio(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);

    	try{ 
    		for(;;){
    			obj= readObject(ois);
    		}
    		
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("getBienInventario()" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}
    	return obj;
    }
    
    /**
     * Metodo que devuelve si un bien de inventario esta o no bloqueado
     * @param obj bien de inventario
     * @return usuario que bloquea el bien de inventario
     * @throws Exception
     */
    public String bloqueado(Object obj) throws Exception{
        if (obj == null){
            throw new Exception("cementerio no valido");
        }

        String bloqueado= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_BLOQUEADO);
        
        Hashtable params= new Hashtable();
        params.put(Const.KEY_IDELEMENTOCEMENTERIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        
        InputStream in= enviarCementerio(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
            bloqueado= (String)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("bloqueado(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return bloqueado;
   }

    /**
     * Bloquea/Desbloquea un bien de inventario
     * @param obj bien a bloquear/desbloquear
     * @return bien actualizado
     * @throws Exception
     */
    public Object bloquearUnidadEnterramiento(Object obj, boolean b) throws Exception{
        if (obj == null){
            throw new Exception("unidad no valido");
        }
        Object bien= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_BLOQUEAR_UNIDAD_ENTE);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_CEMENTERIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarCementerio(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
            bien= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("bloquearcementerio(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return bien;
   }

	public BienBean borrarInventario(Object elemSeleccionado) {
		// TODO Auto-generated method stub
		return null;
	}

    
}
