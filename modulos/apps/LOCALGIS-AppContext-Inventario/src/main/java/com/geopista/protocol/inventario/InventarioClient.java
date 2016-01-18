/**
 * InventarioClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;
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
import com.geopista.app.UserPreferenceConstants;
import com.geopista.feature.GeopistaFeature;
import com.geopista.protocol.Version;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.util.CancelListener;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 17-jul-2006
 * Time: 9:11:12
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que implementa las operaciones que se pueden llevar a cabo en BD desde las aplicaciones cliente
 */

public class InventarioClient {
    private static final Log logger= LogFactory.getLog(InventarioClient.class);

    public static final String	mensajeXML	= "mensajeXML";
    public static final String	idMunicipio	= "idMunicipio";
    public static final String	IdApp		= "IdApp";


    private String sUrl=null;
    private SecurityManager sm=null;

	private TaskMonitor taskMonitor;

    /**
     * Inicializa la conexion al servidor
     * @param sUrl del servidor
     */
    public InventarioClient(String sUrl){
        this.sm=SecurityManager.getSm();
        this.sUrl=sUrl;
    }

    public InventarioClient(String sUrl, SecurityManager sm){
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
     * Inserta en BD el bien asociandolo a la lista de features
     * @param features a las que se asocia el inmueble
     * @param obj bien a insertar en BD
     */
    public Object insertInventario(Object[] features, Object obj) throws Exception{
        return insertInventario(features, obj, null);
    }
    public Object insertInventario(Object[] features, Object obj, Vector files) throws Exception{
        if (obj == null){
            throw new Exception("Inventario no valido");
        }
        Object bien= null;
        Object[] idFeatures;
        Object[] idLayers;
        if (features!=null){
	        idFeatures= new Object[features.length];
	        idLayers= new Object[features.length];
	        for (int i=0;i<features.length;i++){
	            GeopistaFeature feature= (GeopistaFeature)features[i];
	            idFeatures[i]= (String)feature.getSystemId();
	            idLayers[i]= (String)feature.getLayer().getSystemId();
	        }
        }
        else{
        	idFeatures= new Object[0];
	        idLayers= new Object[0];
        }
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_INSERT_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
        params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
        params.put(Const.KEY_INVENTARIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString(), files);
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
            bien= readObject(ois); 
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("insertInventario(Object[] features, Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        	logger.error("SE ha producido un error en la insercion del inventario");
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        
        
		// TODO: Llamada al método de Sicalwin para        
        //insertBienSICALWIN(obj, files, idFeatures, idLayers);
        return bien;
     }

	private void insertBienSICALWIN(Object obj, Vector files,
			Object[] idFeatures, Object[] idLayers) {
		ResourceBundle rb = null;
        String urlSicalwin;
        try{
        	ACQuery query2= new ACQuery();
            query2.setAction(Const.ACTION_INSERT_INVENTARIO_SICALWIN);
            Hashtable params2= new Hashtable();
            params2.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
            params2.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
            params2.put(Const.KEY_INVENTARIO, obj);
            params2.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
            query2.setParams(params2);
            
            StringWriter swQuery2= new StringWriter();
            ByteArrayOutputStream baos2= new ByteArrayOutputStream();
            new ObjectOutputStream(baos2).writeObject(query2);
            Marshaller.marshal(new ACMessage(baos2.toByteArray()), swQuery2);
            rb = ResourceBundle.getBundle("SICALWIN");
            urlSicalwin = rb.getString("sicalwin.url")+":"+rb.getString("sicalwin.port");
            InputStream in2= enviarInventario(urlSicalwin, swQuery2.toString(), files);
        }catch(Exception e){
			logger.error("Ha fallado la llamada al metodo de insercion de un bien en la aplicacion SICALWIN");
			//TODO Temporalmente quitamos el mensaje de alerta.
			//JOptionPane.showMessageDialog(null,  rb.getString("sicalwin.error.mensaje"),rb.getString("sicalwin.error.ventana"), JOptionPane.ERROR_MESSAGE);
        }
	}

    /**
     * Actualiza en BD el inventario
     * @param obj bien a actualizar
     */
    public Object updateInventario(Object obj) throws Exception{
        return updateInventario(obj, null);
    }
    public Object updateInventario(Object obj, Vector files) throws Exception{
        if (obj == null){
            throw new Exception("Inventario no valido");
        }

        Object bien= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_UPDATE_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_INVENTARIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString(), files);
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
            bien= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("updateInventario(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return bien;
     }
    
    /**
     * Devuelve un listado con la información de los bienes
     * @param action
     * @param superpatron
     * @param patron
     * @param cadena
     * @param filtro
     * @param features
     * @param allData
     * @return
     * @throws Exception
     */
    public Collection getBienesInventario(int action, String superpatron, String patron, String cadena, 
			Collection filtro, Object[] features,ConfigParameters configParameters) throws Exception{
    	return getBienesInventario(action,superpatron,patron,cadena,filtro,features,configParameters,false);
    }
    public Collection getBienesInventario(int action, String superpatron, String patron, String cadena, 
    										Collection filtro, Object[] features,ConfigParameters configParameters,boolean withAllData) throws Exception{
	    Collection cRet=null;
	    ACQuery query= new ACQuery();
	    Object[] idFeatures= new ArrayList().toArray();
	    Object[] idLayers= new ArrayList().toArray();
	
	    if (features!=null && features.length>0){
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
	    params.put(Const.KEY_SUPERPATRON, superpatron!=null?superpatron:"");
	    params.put(Const.KEY_PATRON, patron!=null?patron:"");
	    params.put(Const.KEY_CADENA_BUSQUEDA, cadena!=null?cadena:"");
	    params.put(Const.KEY_FILTRO_BUSQUEDA, filtro!=null?filtro.toArray():new ArrayList().toArray());    
	    params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
	    params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
	    //params.put(Const.KEY_FECHA_VERSION, (version==null||version.getFecha()==null?"":version.getFecha()));
	    params.put(Const.KEY_FECHA_VERSION, Const.fechaVersion);
	    params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
	    params.put("WITH_ALL_DATA", withAllData);
	    
	    if (configParameters!=null){
		    params.put("LIMIT", configParameters.getLimit());
		    params.put("OFFSET", configParameters.getOffset());
	    }
	    
	    query.setParams(params);
	    StringWriter swQuery= new StringWriter();
	    ByteArrayOutputStream baos= new ByteArrayOutputStream();
	    new ObjectOutputStream(baos).writeObject(query);
	    Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
	    InputStream in= enviarInventario(sUrl, swQuery.toString());
	    ObjectInputStream ois= new ObjectInputStream(in);
	    cRet= new ArrayList();
	    try{
	        for(;;){
	            Object obj= readObject(ois);
	            if (obj instanceof InmuebleBean){
	            	//logger.info("Numero de inventario:"+((InmuebleBean)obj).getNumInventario());
	                cRet.add((InmuebleBean)obj);
	            }else if (obj instanceof MuebleBean){
	                cRet.add((MuebleBean)obj);
	            }else if (obj instanceof DerechoRealBean){
	                cRet.add((DerechoRealBean)obj);
	            }else if (obj instanceof ValorMobiliarioBean){
	                cRet.add((ValorMobiliarioBean)obj);
	            }else if (obj instanceof CreditoDerechoBean){
	                cRet.add((CreditoDerechoBean)obj);
	            }else if (obj instanceof SemovienteBean){
	                cRet.add((SemovienteBean)obj);
	            }else if (obj instanceof ViaBean){
	                cRet.add((ViaBean)obj);
	            }else if (obj instanceof VehiculoBean){
	                cRet.add((VehiculoBean)obj);
	            }
	            else if (obj instanceof BienBean){
	                cRet.add((BienBean)obj);
	            }
	        }
	    }catch(OptionalDataException ode){
	        if (ode.eof!=true)
	            logger.error("getBienesInventario()" + ode.getMessage(), ode);
	    }catch (EOFException ee){
	    }finally{
	        try{ois.close();}catch(Exception e){};
	    }
	    return cRet;
   }
    
    private Version getVersion(){
    	try{
    		Version version=(Version)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.VERSION);
    		if (version!=null && version.getFecha()!=null) return version;
    		String hora = (String)getHora(Const.ACTION_GET_HORA);
    		String fechaVersion = (String) new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" "+hora;
    		logger.info("Actualizando version fecha:"+fechaVersion);
    		version = new Version();
    		version.setFecha(fechaVersion);
    		version.setFeaturesActivas(true);
    		AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.VERSION,version);
    		return version;
    	}catch(Exception ex){
    		logger.error("Error al obtener la version",ex);
    		return null;
    	}

    }
    
    /**
     * Retorna los datos de los bienes revertibles
     * @param action accion que ejecuta el servlet
     * @param patron
     * @param features
     * @return coleccion de bienes de inventario correspondiente a la accion
     * @throws Exception
     */
    public Collection<BienRevertible> getBienesRevertibles(String patron, String cadena, Collection filtro,ConfigParameters configParameters) throws Exception{
    	return getBienesRevertibles(patron, cadena, filtro, configParameters,false);
    }
    public Collection<BienRevertible> getBienesRevertibles(String patron, String cadena, Collection filtro,ConfigParameters configParameters,boolean withAllData) throws Exception{
    Collection cRet=null;
    ACQuery query= new ACQuery();
   
    query.setAction(Const.ACTION_GET_BIENES_REVERTIBLES);
    Hashtable params= new Hashtable();
    /* NO SE PUEDEN PASAR VALORES null */
    if (patron != null) params.put(Const.KEY_PATRON, patron);
    params.put(Const.KEY_CADENA_BUSQUEDA, cadena!=null?cadena:"");
    params.put(Const.KEY_FILTRO_BUSQUEDA, filtro!=null?filtro.toArray():new ArrayList().toArray());    
    Version version=getVersion();
    params.put(Const.KEY_FECHA_VERSION, (version==null||version.getFecha()==null?"":version.getFecha()));  
    //params.put(Const.KEY_FECHA_VERSION, Const.fechaVersion);
    //logger.info("Fecha version bien revertible:"+version.getFecha());
    params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    params.put("WITH_ALL_DATA", withAllData);
    
    if (configParameters!=null){
	    params.put("LIMIT", configParameters.getLimit());
	    params.put("OFFSET", configParameters.getOffset());
    }
    
    query.setParams(params);
    StringWriter swQuery= new StringWriter();
    ByteArrayOutputStream baos= new ByteArrayOutputStream();
    new ObjectOutputStream(baos).writeObject(query);
    Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    InputStream in= enviarInventario(sUrl, swQuery.toString());
    ObjectInputStream ois= new ObjectInputStream(in);
    cRet= new ArrayList();
    try{
        for(;;){
            Object obj= readObject(ois);
            cRet.add(obj);
        }
    }catch(OptionalDataException ode){
        if (ode.eof!=true)
            logger.error("getBienesRevertibles()" + ode.getMessage(), ode);
    }catch (EOFException ee){
    }finally{
        try{ois.close();}catch(Exception e){};
    }
    return cRet;
   }

    /**
     * Retorna los datos de los bienes revertibles
     * @param action accion que ejecuta el servlet
     * @param patron
     * @param features
     * @return coleccion de bienes de inventario correspondiente a la accion
     * @throws Exception
     */
    public Collection<Lote> getLotes(String patron, String cadena, Collection filtro,ConfigParameters configParameters) throws Exception{
    Collection cRet=null;
    ACQuery query= new ACQuery();
   
    query.setAction(Const.ACTION_GET_LOTES);
    Hashtable params= new Hashtable();
    /* NO SE PUEDEN PASAR VALORES null */
    if (patron != null) params.put(Const.KEY_PATRON, patron);
    params.put(Const.KEY_CADENA_BUSQUEDA, cadena!=null?cadena:"");
    params.put(Const.KEY_FILTRO_BUSQUEDA, filtro!=null?filtro.toArray():new ArrayList().toArray());    
    Version version=getVersion();
    params.put(Const.KEY_FECHA_VERSION, (version==null||version.getFecha()==null?"":version.getFecha()));  
    //params.put(Const.KEY_FECHA_VERSION, Const.fechaVersion);
    params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    
    if (configParameters!=null){
	    params.put("LIMIT", configParameters.getLimit());
	    params.put("OFFSET", configParameters.getOffset());
    }
    
    query.setParams(params);
    StringWriter swQuery= new StringWriter();
    ByteArrayOutputStream baos= new ByteArrayOutputStream();
    new ObjectOutputStream(baos).writeObject(query);
    Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    InputStream in= enviarInventario(sUrl, swQuery.toString());
    ObjectInputStream ois= new ObjectInputStream(in);
    cRet= new ArrayList();
    try{
        for(;;){
            Object obj= readObject(ois);
            cRet.add(obj);
        }
    }catch(OptionalDataException ode){
        if (ode.eof!=true)
            logger.error("getLotes()" + ode.getMessage(), ode);
    }catch (EOFException ee){
    }finally{
        try{ois.close();}catch(Exception e){};
    }
    return cRet;
   }
    
    public Object getBienInventario(int action,String patron, long idInmueble, long revision,long revisionExpirada) throws Exception{

    	return getBienInventario(action, null,patron, idInmueble, revision,revisionExpirada);
    }
    /**
     * Retorna los bienes de inventario en BD en funcion de la accion
     * @param action accion que ejecuta el servlet
     * @param patron
     * @param features
     * @return coleccion de bienes de inventario correspondiente a la accion
     * @throws Exception
     */
    public BienBean getBienInventario(int action, String superpatron,String patron, long idInmueble, long revision,long revisionExpirada) throws Exception{

    	ACQuery query= new ACQuery();
    	Object obj = null;

    	query.setAction(action);
    	Hashtable params= new Hashtable();
    	/* NO SE PUEDEN PASAR VALORES null */
    	if (superpatron!=null)
        	params.put(Const.KEY_SUPERPATRON, superpatron);
    	params.put(Const.KEY_PATRON, patron);
    	params.put(Const.KEY_IDINMUEBLE, idInmueble);    
    	params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    	params.put(Const.KEY_IDREVISION, revision);
    	params.put(Const.KEY_IDREVISIONEXPIRADA, revisionExpirada);
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarInventario(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);

    	try{
    		for(;;){
    			obj= readObject(ois);
    			if (revision==-1)
    				return (BienBean)obj;
    			else
	    			if (((BienBean)obj).getRevisionActual()==revision){
	    				return (BienBean)obj;
	    			} 
    		}
    		
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("getBienInventario()" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}
    	return null;
    }

    /**
     * Devuelve una un Bien por el numero de inventario 
     * * @param action accion que ejecuta el servlet
     * @param patron
     * @param features
     * @return coleccion de bienes de inventario correspondiente a la accion
     * @throws Exception
     */
    public BienBean getBienByNumInventario(String numInventario) throws Exception{

    	ACQuery query= new ACQuery();
    	Object obj = null;

    	query.setAction(Const.ACTION_GET_BIENES);
    	Hashtable params= new Hashtable();
    	String[] filtro = new String [1];
    	filtro[0]=" and numinventario='"+numInventario+"' ";
    	params.put(Const.KEY_FILTRO_BUSQUEDA, filtro);    
    	params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarInventario(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);

    	try{
    		for(;;){
    			obj= readObject(ois);
    			return (BienBean)obj;
    		}
    		
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("getBien()" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}
    	return null;
    }
    /**
     * Devuelve una un BienRevertible por el numero de inventario 
     * @param action accion que ejecuta el servlet
     * @param patron
     * @param features
     * @return coleccion de bienes de inventario correspondiente a la accion
     * @throws Exception
     */
    public BienRevertible getBienRevertibleByNumInventario(String numInventario) throws Exception{

    	ACQuery query= new ACQuery();
    	Object obj = null;

    	query.setAction(Const.ACTION_GET_BIENES_REVERTIBLES);
    	Hashtable params= new Hashtable();
    	CampoFiltro[] arrayFiltro = new CampoFiltro [1];
    	CampoFiltro campoFiltro=new CampoFiltro();
    	arrayFiltro[0]=campoFiltro;
    	campoFiltro.setNombre("num_inventario");
        campoFiltro.setTabla("bien_revertible");
        campoFiltro.setVarchar();
        campoFiltro.setOperador("=");
    	campoFiltro.setValorVarchar(numInventario);
    	params.put(Const.KEY_FILTRO_BUSQUEDA, arrayFiltro);    
    	params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarInventario(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);

    	try{
    		for(;;){
    			obj= readObject(ois);
    			return (BienRevertible)obj;
    		}
    		
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("getBien()" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}
    	return null;
    }

    /**
     * Devuelve las cuentas contables existenten en BD
     * @return lista de cuentas contables
     */
    public Collection getCuentasContables() throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_CUENTAS_CONTABLES);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            CuentaContable cuentaContable= null;
            for(;;){
                cuentaContable= (CuentaContable)readObject(ois);
                cRet.add(cuentaContable);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getCuentasContables()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }


    /**
     * Devuelve las cuentas de amortizacion existenten en BD
     * @return lista de cuentas de amortizacion
     */
    public Collection getCuentasAmortizacion() throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_CUENTAS_AMORTIZACION);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            CuentaAmortizacion cuentaAmortizacion= null;
            for(;;){
                cuentaAmortizacion= (CuentaAmortizacion)readObject(ois);
                cRet.add(cuentaAmortizacion);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getCuentasAmortizacion()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }

    /**
     * Devuelve la lista de compannias de seguros existenten en BD
     * @return lista de seguros
     */
    public Collection getCompanniasSeguros() throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_COMPANNIAS_SEGUROS);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            CompanniaSeguros compannia= null;
            for(;;){
                compannia= (CompanniaSeguros)readObject(ois);
                cRet.add(compannia);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getCompanniasSeguros()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }

    /**
     * Marca como BORRADO un bien en BD
     * @param obj bien a marcar como borrado
     */
    public Object borrarInventario(Object obj) throws Exception{
        if (obj == null){
            throw new Exception("Inventario no valido");
        }

        Object bien= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_BORRAR_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_INVENTARIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              bien= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("borrarInventario(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return bien;
     }

    /**
     * Elimina un bien en BD
     * @param obj a eliminar de BD
     */
    public Object eliminarInventario(Object[] features, Object obj) throws Exception{
        if (obj == null){
            throw new Exception("Inventario no valido");
        }

        Object bien= null;
        ACQuery query= new ACQuery();
        Object[] idFeatures= null; 
        Object[] idLayers= null;
        if (features!=null){
        	idFeatures= new Object[features.length];
            idLayers= new Object[features.length];
            
        	for (int i=0;i<features.length;i++){
        		GeopistaFeature feature= (GeopistaFeature)features[i];
        		idFeatures[i]= (String)feature.getSystemId();
        		idLayers[i]= (String)feature.getLayer().getSystemId();
        	}
        }

        query.setAction(Const.ACTION_ELIMINAR_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_INVENTARIO, obj);
        if (idFeatures!=null)
        	params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
        if (idLayers!=null)
            params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);    
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              bien= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("eliminarInventario(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return bien;
     }

    /**
     * Elimina un bien en BD
     * @param obj a eliminar de BD
     */
    public boolean eliminarInventarioNoRecover(Object obj) throws Exception{
        if (obj == null){
            throw new Exception("Inventario no valido");
        }

        ACQuery query= new ACQuery();

        query.setAction(Const.ACTION_ELIMINAR_NORECOVER_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_INVENTARIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
        	return (Boolean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("eliminarInventario(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return false;
     }

    /**
     * Elimina un bien en BD
     * @param obj a eliminar de BD
     */
    public boolean eliminarTodoInventario() throws Exception{
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_ELIMINAR_TODO_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              return (Boolean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("eliminarInventario(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return false;
     }


    /**
     * Recupera un bien dado de baja con anterioridad
     * @param obj bien recuperardo
     */
    public Object recuperarInventario(Object obj) throws Exception{
        if (obj == null){
            throw new Exception("Inventario no valido");
        }

        Object bien= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_RECUPERAR_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_INVENTARIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
        	bien= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("recuperarInventario(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return bien;
     }

    /**
     * Bloquea/Desbloquea un bien de inventario
     * @param obj bien a bloquear/desbloquear
     * @return bien actualizado
     * @throws Exception
     */
    public Object bloquearInventario(Object obj, boolean b) throws Exception{
        if (obj == null){
            throw new Exception("Inventario no valido");
        }
        Object bien= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_BLOQUEAR_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_INVENTARIO, obj);
        params.put(Const.KEY_VALOR_BLOQUEO_INVENTARIO, new Boolean(b));
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
            bien= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("bloquearInventario(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return bien;
   }


    /**
     * Metodo que devuelve si un bien de inventario esta o no bloqueado
     * @param obj bien de inventario
     * @return usuario que bloquea el bien de inventario
     * @throws Exception
     */
    public String bloqueado(Object obj) throws Exception{
        if (obj == null){
        	logger.error("Inventaro no valido, el objeto seleccionado es null");
            throw new Exception("Inventario no valido");
        }

        String bloqueado= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_BLOQUEADO_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_INVENTARIO, obj);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
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
        InputStream in= enviarInventario(sUrl, swQuery.toString());
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



    public InputStream enviarInventario(String sUrl, String sMensaje) throws Exception{
    	return enviar(sUrl, sMensaje, null, null, null,taskMonitor);
    }
    public InputStream enviarInventario(String sUrl, String sMensaje,TaskMonitor taskMonitor) throws Exception{
        return enviar(sUrl, sMensaje, null, null, null,taskMonitor);
  }

    
    public InputStream enviarInventario(String sUrl, String sMensaje, Vector files) throws Exception{
          return enviar(sUrl, sMensaje, null, null, files,taskMonitor);
    }
    public InputStream enviarInventario(String sUrl, String sMensaje, Vector files,TaskMonitor taskMonitor) throws Exception{
        return enviar(sUrl, sMensaje, null, null, files,taskMonitor);
  }


    private  InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword, Vector files,TaskMonitor taskMonitor)
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
		client.setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null) {
			client.getState().setCredentials(null, null, creds);
		}

        /* -- MultipartPostMethod -- */
        final org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);

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
			
			if (taskMonitor!=null){
				((TaskMonitorDialog)taskMonitor).addCancelListener(new CancelListener() {
					public void doCancel(){    
						logger.info("Cancelando proceso de carga");
						method.abort();
					}
				});
			}
			
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
        InputStream in= enviarInventario(sUrl, swQuery.toString());
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

    
    public static void main(String args[]){
    	
    	String url="http://localgis:8081/Geopista/AdministradorCartografiaServlet/InventarioServlet";
    
        try {
			String sUrlPrefix="http://localhost:8081/Geopista/";
			com.geopista.security.SecurityManager.setsUrl(sUrlPrefix);
			com.geopista.security.SecurityManager.login("syssuperuser", "sysgeopass","Geopista");
			
			InventarioClient inventarioClient=new InventarioClient(url);

			String superPatron=Const.SUPERPATRON_BIENES;
			
			String patron=Const.PATRON_VIAS_PUBLICAS_URBANAS;
			int action=0;
			long idRevision=200;
			long idRevisionExpirada=Long.parseLong("9999999999");
			if (patron.equals(Const.PATRON_SEMOVIENTES)){
				long idBien=6;
				action=Const.ACTION_GET_SEMOVIENTES;
		    	Object obj = inventarioClient.getBienInventario(action, superPatron,patron, idBien, idRevision,idRevisionExpirada);
		    	if (obj!=null)
		    		logger.info("Identificacion recogida:"+((SemovienteBean)obj).getNumInventario());
		    	else
			    	logger.info("El bien no existe en el sistema");
			}
			else if (patron.equals(Const.PATRON_VEHICULOS)){
				long idBien=7;
				action=Const.ACTION_GET_VEHICULO;
		    	Object obj = inventarioClient.getBienInventario(action, superPatron,patron, idBien, idRevision,idRevisionExpirada);
		    	if (obj!=null)
		    		logger.info("Identificacion recogida:"+((VehiculoBean)obj).getNumInventario());
		    	else
		    		logger.info("El bien no existe en el sistema");
			}
			else if (patron.equals(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
				long idBien=1;
				action=Const.ACTION_GET_CREDITO_DERECHO;
		    	Object obj = inventarioClient.getBienInventario(action, superPatron,patron, idBien, idRevision,idRevisionExpirada);
		    	if (obj!=null)
		    		logger.info("Identificacion recogida:"+((CreditoDerechoBean)obj).getNumInventario());
		    	else
		    		logger.info("El bien no existe en el sistema");
			}
			else if (patron.equals(Const.PATRON_DERECHOS_REALES)){
				long idBien=1;
				action=Const.ACTION_GET_DERECHO_REAL;
		    	Object obj = inventarioClient.getBienInventario(action, superPatron,patron, idBien, idRevision,idRevisionExpirada);
		    	if (obj!=null)
		    		logger.info("Identificacion recogida:"+((DerechoRealBean)obj).getNumInventario());
		    	else
		    		logger.info("El bien no existe en el sistema");
			}
			else if (patron.equals(Const.PATRON_MUEBLES_HISTORICOART)){
				long idBien=124;
				action=Const.ACTION_GET_MUEBLE;
		    	Object obj = inventarioClient.getBienInventario(action, superPatron,patron, idBien, idRevision,idRevisionExpirada);
		    	if (obj!=null)
		    		logger.info("Identificacion recogida:"+((MuebleBean)obj).getNumInventario());
		    	else
		    		logger.info("El bien no existe en el sistema");
			}			
			else if (patron.equals(Const.PATRON_VALOR_MOBILIARIO)){
				long idBien=124;
				action=Const.ACTION_GET_VALOR_MOBILIARIO;
		    	Object obj = inventarioClient.getBienInventario(action, superPatron,patron, idBien, idRevision,idRevisionExpirada);
		    	if (obj!=null)
		    		logger.info("Identificacion recogida:"+((ValorMobiliarioBean)obj).getNumInventario());
		    	else
		    		logger.info("El bien no existe en el sistema");
			}				
			else if (patron.equals(Const.PATRON_VIAS_PUBLICAS_URBANAS)){
				long idBien=140;
				action=Const.ACTION_GET_VIA;
		    	Object obj = inventarioClient.getBienInventario(action, superPatron,patron, idBien, idRevision,idRevisionExpirada);
		    	if (obj!=null)
		    		logger.info("Identificacion recogida:"+((ViaBean)obj).getNumInventario());
		    	else
		    		logger.info("El bien no existe en el sistema");
			}				
			else if (patron.equals(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
				long idBien=140;
				action=Const.ACTION_GET_VIA;
		    	Object obj = inventarioClient.getBienInventario(action, superPatron,patron, idBien, idRevision,idRevisionExpirada);
		    	if (obj!=null)
		    		logger.info("Identificacion recogida:"+((ViaBean)obj).getNumInventario());
		    	else
		    		logger.info("El bien no existe en el sistema");
			}
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.exit(1);
    	
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
    	InputStream in= enviarInventario(sUrl, swQuery.toString());
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

	public void setTaskMonitor(TaskMonitor taskMonitor) {
		this.taskMonitor=taskMonitor;		
	}
	
	public void returnInsertIntegEIELInventario(InventarioEIELBean object) throws Exception {
      
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_INSERT_INTEG_EIEL_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(Const.KEY_INVENTARIO_EIEL, object);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        
	}
	
	public void associateBienesInventario(BienBean[] bienesSeleccionados) throws Exception {
		for(int i = 0;i<bienesSeleccionados.length;i++){
			try{
				
				updateLayerFeatureInventario(bienesSeleccionados[i]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private Object updateLayerFeatureInventario(Object object) throws Exception {
		if (object == null){
            throw new Exception("Inventario no valido");
        }

        Object bien= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_UPDATE_LAYER_FEATURE_BIENES);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_INVENTARIO, object);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              bien= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("borrarInventario(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return bien;
	}

	/**
     * Calcula la hora en base de datos
     */
    public Object getDate(int action) throws Exception{

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
    	InputStream in= enviarInventario(sUrl, swQuery.toString());
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

	public Collection getBienesPreAlta() throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_BIENES_PREALTA);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            BienPreAltaBean bienPA = null;
            for(;;){
            	bienPA= (BienPreAltaBean)readObject(ois);
                cRet.add(bienPA);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getBienesPreAlta()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }

    public Hashtable getComprobarIntegEIELInventario(BienBean bien) throws Exception{
    	Hashtable hEIEL =null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_COMPROBAR_INTEG_EIEL);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(Const.KEY_BIEN, bien);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        hEIEL = new Hashtable();
        try{
            Vector vComp = null;
            int i = 0;
            for(;;){
            	vComp = (Vector)readObject(ois);
            	hEIEL.put(i, vComp);
            	i++;
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getComprobarIntegEIELInventario()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return hEIEL;
     }
	
    public Hashtable getElementosComprobarIntegEIELInventario(long idBien) throws Exception{
    	Hashtable hEIEL =null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_ELEMENTOS_COMPROBAR_INTEG_EIEL);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(Const.KEY_IDBIEN, idBien);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        hEIEL = new Hashtable();
        try{
            Vector vComp = null;
            int i = 0;
            for(;;){
            	vComp = (Vector)readObject(ois);
            	hEIEL.put(i, vComp);
            	i++;
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getElementosComprobarIntegEIELInventario()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return hEIEL;
     }
    
	public Hashtable getDatosEIELSinAsociar() throws Exception{
        Hashtable hEIEL =null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_EIEL_SIN_ASOCIAR);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        hEIEL = new Hashtable();
        try{
            InventarioEIELBean eiel = null;
            for(;;){
            	eiel = (InventarioEIELBean)readObject(ois);
            	hEIEL.put(eiel.getUnionClaveEIEL(), eiel);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getDatosEIELSinAsociar()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return hEIEL;
     }

	public Hashtable getDatosEIELBien(long idBien) throws Exception{
        Hashtable hEIEL =null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_EIEL_ASOCIADOS);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(Const.KEY_IDBIEN, idBien);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        hEIEL = new Hashtable();
        try{
            InventarioEIELBean eiel = null;
            for(;;){
            	eiel = (InventarioEIELBean)readObject(ois);
            	hEIEL.put(eiel.getUnionClaveEIEL(), eiel);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getDatosEIELBien()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return hEIEL;
     }

	
	public ArrayList getNumInventario(int epigInventario) throws Exception{

        ArrayList lstNumInventario = new ArrayList();
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_NUMEROS_INVENTARIO);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(Const.KEY_EPIG_INVENTARIO, epigInventario);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
        	Inventario eielInventario = new Inventario();
            for(;;){
            	eielInventario = (Inventario)readObject(ois);
            	lstNumInventario.add(eielInventario);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getNumInventarior()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return lstNumInventario;
     }

	
	
	//Inserta un bien y borra el bien en prealta que exista
	public Object deleteBienPA(Object[] features, Object obj, BienPreAltaBean bienPA,Vector files)  throws Exception{
	      if (obj == null){
	            throw new Exception("Inventario no valido");
	        }
	        Object bien= null;
	        //No se le van a asociar geometrias en este paso
	        Object[] idFeatures= new Object[features.length];
	        Object[] idLayers= new Object[features.length];
//	        for (int i=0;i<features.length;i++){
//	            GeopistaFeature feature= (GeopistaFeature)features[i];
//	            idFeatures[i]= (String)feature.getSystemId();
//	            idLayers[i]= (String)feature.getLayer().getSystemId();
//	        }
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_INSERT_BIEN_PREALTA);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
	        params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
	        params.put(Const.KEY_INVENTARIO, obj);
	        params.put(Const.KEY_PREALTA, bienPA);
	        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
	        query.setParams(params);
	        StringWriter swQuery= new StringWriter();
	        ByteArrayOutputStream baos= new ByteArrayOutputStream();
	        new ObjectOutputStream(baos).writeObject(query);
	        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
	        InputStream in= enviarInventario(sUrl, swQuery.toString(), files);
	        ObjectInputStream ois= new ObjectInputStream(in);
	        try{
	            bien= readObject(ois); 
	        }catch(OptionalDataException ode){
	            if (ode.eof!=true)
	                logger.error("Error al borrar u bien en prealta" + ode.getMessage(), ode);
	        }catch (EOFException ee){
	        	logger.error("SE ha producido un error en la insercion del inventario");
	        }finally{
	            try{ois.close();}catch(Exception e){};
	        }
	        
	        
			// TODO: Llamada al método de Sicalwin para
	        
	        //insertBienSICALWIN(obj, files, idFeatures, idLayers);
	        return bien;
	}

	public void updateDatosValoracion(Double valorUrbano, Double valorRustico) throws Exception {
		
		if (valorUrbano == null || valorRustico ==null ){
            throw new Exception("valores introducidos no válidos");
        }
      
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_UPDATE_DATOS_VALORACION);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_VALOR_URBANO, valorUrbano);
        params.put(Const.KEY_VALOR_RUSTICO, valorRustico);
        params.put(Const.KEY_ID_ENTIDAD, AppContext.getIdEntidad());
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        
	}

	public Double getValorMetro(String keyValor) throws Exception {
	    	Object valor_metro = null;
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_GET_VALOR_METRO);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_PATRON, keyValor);
	        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
	        params.put(Const.KEY_ID_ENTIDAD, AppContext.getIdEntidad());
	        query.setParams(params);
	        StringWriter swQuery= new StringWriter();
	        ByteArrayOutputStream baos= new ByteArrayOutputStream();
	        new ObjectOutputStream(baos).writeObject(query);
	        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
	        InputStream in= enviarInventario(sUrl, swQuery.toString());
	        ObjectInputStream ois= new ObjectInputStream(in);
	        try{
	        	
	        		valor_metro= readObject(ois);
	        	
	        }catch(OptionalDataException ode){
	        	if (ode.eof!=true)
	        		logger.error("getBienInventario()" + ode.getMessage(), ode);
	        }catch (EOFException ee){
	        	logger.error("Se ha producido un error en al leer el valor del metro");

	        }finally{
	        	try{ois.close();}catch(Exception e){};
	        }
	        return (Double) valor_metro;
	}

	public Collection<HistoricoAmortizacionesBean> getBienesAmortizables(int action, String superpatron, String patron, String cadena, 
			Collection filtro,ConfigParameters configParameters) throws Exception {
		 Collection cRet=null;
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_GET_BIENES_AMORTIZABLES);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
		    /* NO SE PUEDEN PASAR VALORES null */
		    params.put(Const.KEY_SUPERPATRON, superpatron!=null?superpatron:"");
		    params.put(Const.KEY_PATRON, patron!=null?patron:"");
		    params.put(Const.KEY_CADENA_BUSQUEDA, cadena!=null?cadena:"");
		    params.put(Const.KEY_FILTRO_BUSQUEDA, filtro!=null?filtro.toArray():new ArrayList().toArray());    
		    //params.put(Const.KEY_FECHA_VERSION, (version==null||version.getFecha()==null?"":version.getFecha()));
		    params.put(Const.KEY_FECHA_VERSION, Const.fechaVersion);
		    params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
		    
		    if (configParameters!=null){
			    params.put("LIMIT", configParameters.getLimit());
			    params.put("OFFSET", configParameters.getOffset());
		    }
		    
		    query.setParams(params);
	        StringWriter swQuery= new StringWriter();
	        ByteArrayOutputStream baos= new ByteArrayOutputStream();
	        new ObjectOutputStream(baos).writeObject(query);
	        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
	        InputStream in= enviarInventario(sUrl, swQuery.toString());
	        ObjectInputStream ois= new ObjectInputStream(in);
	        cRet= new ArrayList();
	        try{
	            HistoricoAmortizacionesBean bienHa = null;
	            for(;;){
	            	bienHa= (HistoricoAmortizacionesBean)readObject(ois);
	                cRet.add(bienHa);
	            }
	        }catch(OptionalDataException ode){
	            if (ode.eof!=true)
	                logger.error("getBienesAmortizables()" + ode.getMessage(), ode);
	        }catch (EOFException ee){
	        }finally{
	            try{ois.close();}catch(Exception e){};
	        }
	        return cRet;
	}

	public ListaHistoricoAmortizaciones insertBienesAmortizables(Integer Anio) throws Exception {
		ListaHistoricoAmortizaciones listaHA=null;
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_INSERT_BIENES_AMORTIZABLES);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_ANIO_AMORTIZACION, Anio);
	        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
	        query.setParams(params);
	        StringWriter swQuery= new StringWriter();
	        ByteArrayOutputStream baos= new ByteArrayOutputStream();
	        new ObjectOutputStream(baos).writeObject(query);
	        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
	        InputStream in= enviarInventario(sUrl, swQuery.toString());
	        ObjectInputStream ois= new ObjectInputStream(in);
	        try{
	        	listaHA= (ListaHistoricoAmortizaciones)readObject(ois);
	        }catch(OptionalDataException ode){
	            if (ode.eof!=true)
	                logger.error("InsertBienesAmortizables()" + ode.getMessage(), ode);
	        }catch (EOFException ee){
	        }finally{
	            try{ois.close();}catch(Exception e){};
	        }
	        return listaHA;
		
	}

	public ListaHistoricoAmortizaciones getHistoricoBienAmortizable(long idBien,String patron)throws Exception {
		ListaHistoricoAmortizaciones listaHA=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_BIENES_AMORTIZADOS);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_IDBIEN, idBien);
        params.put(Const.KEY_PATRON,patron);
        params.put(Const.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarInventario(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
        	listaHA= (ListaHistoricoAmortizaciones)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getHistoricoBienAmortizable()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return listaHA;
	}

}


