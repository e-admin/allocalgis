package com.geopista.app.eiel;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import com.geopista.app.AppContext;
import com.geopista.app.alptolocalgis.beans.ConstantesAlp;
import com.geopista.app.eiel.beans.FeatureEIELSimple;
import com.geopista.app.eiel.beans.WorkflowEIEL;
import com.geopista.app.eiel.beans.filter.LCGCampoCapaEIEL;
import com.geopista.app.eiel.beans.filter.LCGNodoEIEL;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;

/**
 * Clase que implementa en la parte cliente la conexion con la parte servidora. Los diferentes metodos son para las
 * diferentes acciones en base de datos. Se crear un ACQuery para cada caso, se introduce una constante indicando la
 * accion que se quiere realizar y se añaden los paramtros a la hash params que seran recogidos en el servlet segun
 * la accion indicada, y se hara la peticion a base de datos. Los objetos enviados deben ser serializables.
 * */

public class LocalGISEIELClient
{

    private static final Log logger= LogFactory.getLog(LocalGISEIELClient.class);
    private String sUrl=null;
    public static final String	mensajeXML	= "mensajeXML";
    public static final String	idMunicipio	= "idMunicipio";
    public static final String	IdApp		= "IdApp";

    /**
     * Inicializa la conexion al servidor
     * @param sUrl del servidor
     */
    public LocalGISEIELClient(String sUrl)
    {
        this.sUrl=sUrl;
    }

    /**
     * Devuelve un listado con las operaciones de ALP a realizar
     * @return Collection con objetos OperacionAlp
     * @throws Exception
     */
    
    
    public int getIdMapa(String nombreMapa) throws Exception
    {
        int idMapa = -1;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_OBTENER_IDMAPA);
        params.put(ConstantesLocalGISEIEL_LCGIII.NOMBREMAPA,nombreMapa);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            for(;;)
            {
                Object identificadorMapa= readObject(ois);
                if ((identificadorMapa instanceof Integer)&&(identificadorMapa!=null))
                {
                    idMapa = ((Integer)identificadorMapa).intValue();
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getOperacionesEIEL()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return idMapa;
    }
    
    public ArrayList getMapas(String patronNombreMapa,String locale) throws Exception
    {
        ArrayList listadoMapas = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_OBTENER_MAPAS);
        params.put(ConstantesLocalGISEIEL_LCGIII.Locale, locale);
        params.put(ConstantesLocalGISEIEL_LCGIII.NOMBREMAPA,patronNombreMapa);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            listadoMapas= (ArrayList)readObject(ois);            
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getOperacionesEIEL()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listadoMapas;
    }
    
    public String getNombreMapa(Integer idMapa) throws Exception
    {
        String nombreMapa = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_OBTENER_NOMBREMAPA);
        params.put(ConstantesLocalGISEIEL_LCGIII.IDMAPA,idMapa);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            for(;;)
            {
                Object nameMapa= readObject(ois);
                if ((nameMapa instanceof String)&&(nameMapa!=null))
                {
                    nombreMapa = (String) nameMapa;
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getOperacionesEIEL()()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return nombreMapa;
    }
    
    public ArrayList getDominiosEIEL(String domain,String locale) throws Exception
    {        
    	ArrayList lstDomains = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_OBTENER_DOMINIO);
        params.put(ConstantesLocalGISEIEL_LCGIII.ID_DOMAIN, domain);
    	params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    	if (locale!=null)
    		params.put(ConstantesLocalGISEIEL_LCGIII.Locale, locale);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            for(;;)
            {
                Object listaDomains= readObject(ois);
                if ((listaDomains!=null) && (listaDomains instanceof ArrayList))
                {
                	lstDomains = ((ArrayList)listaDomains);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getTablasEIEL()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
       
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return lstDomains;
    }
    
    public ArrayList getLstElementos(String filter, String tipoElemento,boolean noGeoReferenciados) throws Exception
    {
    	return getLstElementos(filter,tipoElemento,noGeoReferenciados,0,null,null);
	}

    public ArrayList getLstElementos(String filter, String tipoElemento,boolean noGeoReferenciados,int municipioSeleccionado,
    		String nombreTabla, ArrayList camposEspecificos) throws Exception
    {        
    	ArrayList lstDatos = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_OBTENER_ELEMENTO);
        params.put(ConstantesLocalGISEIEL_LCGIII.FILTRO, filter);
        params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO, tipoElemento);
        params.put(ConstantesLocalGISEIEL_LCGIII.OPERACION_FILTRAR_REFERENCIADOS, noGeoReferenciados);
        if (municipioSeleccionado!=0){
        	params.put(ConstantesLocalGISEIEL_LCGIII.MUNICIPIO_SELECCIONADO,municipioSeleccionado);
        }
        if (nombreTabla!=null)
        	params.put(ConstantesLocalGISEIEL_LCGIII.NOMBRE_TABLA, nombreTabla);
        if (camposEspecificos!=null)
        	params.put(ConstantesLocalGISEIEL_LCGIII.CAMPOS_ESPECIFICOS, camposEspecificos);
        
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            for(;;)
            {
                Object lista= readObject(ois);
                if ((lista!=null) && (lista instanceof ArrayList))
                {
                	lstDatos = ((ArrayList)lista);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getDATOSEIEL()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return lstDatos;
    }
    
    public ArrayList getLstUsuariosEntidad(String idEntidad) throws Exception{     
    	ArrayList lstDatos = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_OBTENER_USUARIOS_ENTIDAD);
        params.put(ConstantesLocalGISEIEL_LCGIII.ID_ENTIDAD, idEntidad);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            for(;;)
            {
                Object lista= readObject(ois);
                if ((lista!=null) && (lista instanceof ArrayList))
                {
                	lstDatos = ((ArrayList)lista);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getDATOSEIEL()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return lstDatos;
    }
    
    public ArrayList getLstElementosVersionados(String filter, String tipoElemento,Object elemento, boolean noGeoReferenciados) throws Exception
    {        
    	ArrayList lstDatos = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_OBTENER_ELEMENTOS_VERSIONADOS);
        params.put(ConstantesLocalGISEIEL_LCGIII.FILTRO, filter);
        params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO, tipoElemento);
        if (elemento !=null)
        	params.put(ConstantesLocalGISEIEL_LCGIII.OBJECT, elemento);
        
        params.put(ConstantesLocalGISEIEL_LCGIII.OPERACION_FILTRAR_REFERENCIADOS, noGeoReferenciados);
        params.put(ConstantesLocalGISEIEL_LCGIII.OPERACION_FILTRAR_VERSION, true);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            for(;;)
            {
                Object lista= readObject(ois);
                if ((lista!=null) && (lista instanceof ArrayList))
                {
                	lstDatos = ((ArrayList)lista);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getDATOSEIEL()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return lstDatos;
    }    
    public Collection getIdsFeatures(Object object, String tipoElemento) throws Exception
    {
    	return getIdsFeatures(object,tipoElemento,true,null);
    }
    
    public Collection getIdsFeatures(Object object, String tipoElemento,boolean soloId,String nombreTabla) throws Exception
    {        
    	Collection lstDatos = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_FEATURES);
        params.put(ConstantesLocalGISEIEL_LCGIII.OBJECT, object);
        params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO, tipoElemento);
        params.put(ConstantesLocalGISEIEL_LCGIII.NOMBRE_TABLA, nombreTabla);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            for(;;)
            {
                Object lista= readObject(ois);
                if ((lista!=null) && (lista instanceof Collection))
                {
                	lstDatos = ((ArrayList)lista);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getFeaturesEIEL()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        

        if (soloId){
        	
            ArrayList lstDatosSoloId=new ArrayList();       
            if (lstDatos!=null){
	            Iterator it=lstDatos.iterator();
	        	while (it.hasNext()){
	        		FeatureEIELSimple feature=(FeatureEIELSimple)it.next();
	        		lstDatosSoloId.add(feature.getId());
	        	}
            }
        	return lstDatosSoloId;
        }
        else        
        	return lstDatos;
    }
      
    public Object bloquearElemento(Object obj, boolean b, String tipoElemento) throws Exception{
        if (obj == null){
            throw new Exception("Elemento no valido");
        }
        Object elemento= null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_BLOQUEAR_ELEMENTO);
        Hashtable params= new Hashtable();
        params.put(ConstantesLocalGISEIEL_LCGIII.OBJECT, obj);
        params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO,tipoElemento);
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_VALOR_BLOQUEO_ELEMENTO, new Boolean(b));
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
            elemento= readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("bloquearElemento(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return elemento;
   }
    
    public String bloqueado(Object obj, String tipoElemento) throws Exception{
        
    	if (obj == null){
            throw new Exception("Elemento no valido");
        }
        String bloqueado= null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_BLOQUEADO_ELEMENTO);
        Hashtable params= new Hashtable();
        params.put(ConstantesLocalGISEIEL_LCGIII.OBJECT, obj);
        params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO, tipoElemento);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
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
    
    public String eliminarElemento(Object obj, Collection lstFeatures, String idlayer, String tipoElemento) throws Exception{
       
    	if (obj == null){
            throw new Exception("Elemento no valido");
        }
        String bloqueado= null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_ELIMINAR_ELEMENTO);
        Hashtable params= new Hashtable();
        params.put(ConstantesLocalGISEIEL_LCGIII.OBJECT, obj);
        params.put(ConstantesLocalGISEIEL_LCGIII.FEATURES, lstFeatures!=null?lstFeatures:(new ArrayList()));
        if (idlayer!=null)
        	params.put(ConstantesLocalGISEIEL_LCGIII.ID_LAYER, idlayer);
        params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO, tipoElemento);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
            bloqueado= (String)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("eliminar(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return bloqueado;
   }

    public void insertarElemento(Object obj, String idLayer, String tipoElemento) throws Exception{

    	if (obj == null){
    		throw new Exception("Elemento no valido");
    	}
    	
    	if (obj instanceof WorkflowEIEL){
        	if(ConstantesLocalGISEIEL_LCGIII.permisos.containsKey(ConstantesLocalGISEIEL_LCGIII.PERM_PUBLICADOR_EIEL)){
        		((WorkflowEIEL)obj).setEstadoValidacion(ConstantesLocalGISEIEL_LCGIII.ESTADO_TEMPORAL);
    		}    		
    	}
    	
    	String bloqueado= null;
    	ACQuery query= new ACQuery();
    	query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_INSERTAR_ELEMENTO);
    	Hashtable params= new Hashtable();
    	params.put(ConstantesLocalGISEIEL_LCGIII.OBJECT, obj);
    	if (idLayer!=null)
    		params.put(ConstantesLocalGISEIEL_LCGIII.ID_LAYER, idLayer);
    	params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO, tipoElemento);
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarConsulta(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);
    	try{
    		bloqueado= (String)readObject(ois);
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("insert(Object obj)" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}

    }
    
    public void calcularIndices() throws Exception{
        logger.info("Iniciando calcular indices");
    	String respuesta= null;
    	ACQuery query= new ACQuery();

    	query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_CALCULAR_INDICES_MUNICIPALES);

    	Hashtable params= new Hashtable();
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarConsulta(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);
    	try{
    		respuesta = (String)readObject(ois);
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("insert(Object obj)" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}

    }
    
    public void calcularIndices(int defaultAction) throws Exception{
        logger.info("Iniciando calcular indices");
    	String respuesta= null;
    	ACQuery query= new ACQuery();
    	/** Original COTESA 
    	query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_CALCULAR_INDICES_MUNICIPALES);
    	*/
    	//Nuevo modelo indicadores
    	query.setAction(defaultAction);
    	Hashtable params= new Hashtable();
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarConsulta(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);
    	try{
    		respuesta = (String)readObject(ois);
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("insert(Object obj)" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}

    }
    
    public void calcularObras() throws Exception{

    	String respuesta= null;
    	ACQuery query= new ACQuery();
    	query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_CALCULAR_INDICES_OBRAS);
    	Hashtable params= new Hashtable();
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarConsulta(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);
    	try{
    		respuesta = (String)readObject(ois);
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("insert(Object obj)" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}

    }

    /**
     * Envía la consulta con el mensaje sMensaje, a la url indicada también por parámetro sUrl.
     * @param sUrl url a la que enviar la consulta.
     * @param sMensaje mensaje a enviar.
     * @return InputStream.
     * @throws Exception
     */
    public InputStream enviarConsulta(String sUrl, String sMensaje) throws Exception
    {
          return enviar(sUrl, sMensaje, null, null, null);
    }

    /**
     * Envía la consulta al servidor.
     * @param sUrl url a la que enviar la consulta.
     * @param sMensaje mensaje a enviar.
     * @param sUserName nombre del usuario.
     * @param sPassword password del usuario.
     * @param files
     * @return InputStream
     * @throws Exception
     */
    private InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword, Vector files) throws Exception
    {
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else
        {
			if (com.geopista.security.SecurityManager.getIdSesion() == null){
				if (logger.isDebugEnabled()){
					logger.debug("enviar(String, String, String, String) - Usuario no autenticado");
				}
                creds = new UsernamePasswordCredentials("GUEST", "");
            }else
               creds = new UsernamePasswordCredentials(com.geopista.security.SecurityManager.getIdSesion(), com.geopista.security.SecurityManager.getIdSesion());
		}
		//create a singular HttpClient object
		//org.apache.commons.httpclient.HttpClient client= new HttpClient();
		org.apache.commons.httpclient.HttpClient client= AppContext.getHttpClient();

		
		//establish a connection within 5 seconds
		client.setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null)
        {
			client.getState().setCredentials(null, null, creds);
		}
        
        /* -- MultipartPostMethod -- */
		org.apache.commons.httpclient.methods.MultipartPostMethod method;
		if (SecurityManager.getIdMunicipio()==null)
			method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);
		else
			method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl+"?idMunicipio="+SecurityManager.getIdMunicipio());
		//System.out.println("Metodo abortado:"+method.isAborted());
        
        if (com.geopista.security.SecurityManager.getIdMunicipio()!=null)
        {
             method.addParameter(idMunicipio, SecurityManager.getIdMunicipio());
        }
        if (SecurityManager.getIdApp()!=null)
        {
			 method.addParameter(IdApp, SecurityManager.getIdApp());
        }
        if (sMensaje!=null)
        {
            method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
        }
        
        method.setFollowRedirects(false);

		//execute the method
		byte[] responseBody = null;
		try {
			AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
			TaskMonitor monitor=(TaskMonitor)aplicacion.getBlackboard().get("TASK_MONITOR");
			ProcessCancel processCancel=null;			
			if (monitor!=null){				
				if (monitor!=null){
					processCancel=new ProcessCancel(monitor,method);
					processCancel.start();
				}
			}
			
			
			client.executeMethod(method);
			responseBody = method.getResponseBody();
			

			if (processCancel!=null)
				processCancel.terminateProcess();
			
		} catch (HttpException he) {
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
			logger.error(
					"enviar(String, String, String, String) - Unable to connect to '"
							+ sUrl + "'", ioe);		
			AppContext.releaseResources();
			logger.error("Liberando recursos....");
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
		if (logger.isDebugEnabled())
        {
			logger.debug("enviar(String, String, String, String) - *** Response ***");
		}
		if (logger.isDebugEnabled())
        {
			logger.debug("enviar(String, String, String, String) - Status Line: "
							+ method.getStatusLine());
		}
        int iStatusCode = method.getStatusCode();
        String sStatusLine=method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++)
        {
			if (logger.isDebugEnabled())
            {
				logger.debug("enviar(String, String, String, String)"
						+ responseHeaders[i]);
			}
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
        	//Si estaba logeado le desconectamos
        	//if (SecurityManager.isOnlyLogged())
        	SecurityManager.unLogged();
            throw new Exception("ERROR EN LOS PERMISOS. CAUSAS: " +
                                        "\n\t-Usuario no autenticado " +
                                        "\n\t-Usuario no tiene permisos" +
                                        "\n\t-Se ha perdido la sesión con el servidor" +
                                        "\nReinicie la aplicación y vuelva a intentarlo");
        }
        else{      
            throw new Exception(sStatusLine);
        }
	}


    /**
     * Lee la respuesta del servidor.
     * @param ois ObjetInputStream
     * @return Object devuelto por el servidor.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws ACException
     */
    private Object readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException, ACException
    {
       Object oRet=null;
       oRet=ois.readObject();
       if (oRet instanceof ACException)
       {
           throw (ACException)oRet;
       }
       return oRet;
   }
    
    /**
     * Metodo que devuelve el id del usuario para cargalo posteriormente en la blackboard y que este accesible en la
     * parte cliente.
     *
     * @return String El id del usuario.
     * @throws Exception
     * */
    public String inicializaBlackBoard() throws Exception
    {
        String idUser=null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesAlp.ACTION_GET_ID_USUARIO);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            idUser = (String)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("inicializaBlackBoard()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return idUser;
    }
    
//    public Collection getPlantillas(String path,String filtro,String patron,ArrayList patrones) throws Exception{
//        Collection cRet=null;
//        ACQuery query= new ACQuery();
//        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_PLANTILLAS);
//        Hashtable params= new Hashtable();
//        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_PATH_PLANTILLAS, path);
//        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
//        if(filtro!=null)
//        	params.put(ConstantesLocalGISEIEL_LCGIII.FILTRO , filtro);
//        if(patron!=null)
//        	params.put(ConstantesLocalGISEIEL_LCGIII.PATRON, patron);
//        if(patrones!=null)
//        	params.put(ConstantesLocalGISEIEL_LCGIII.PATRONES, patrones);
//        query.setParams(params);
//        StringWriter swQuery= new StringWriter();
//        ByteArrayOutputStream baos= new ByteArrayOutputStream();
//        new ObjectOutputStream(baos).writeObject(query);
//        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
//        InputStream in= enviarConsulta(sUrl, swQuery.toString());
//        ObjectInputStream ois= new ObjectInputStream(in);
//        cRet= new ArrayList();
//        try{
//            for(;;){
//                cRet.add(readObject(ois));
//            }
//        }catch(OptionalDataException ode){
//            if (ode.eof!=true)
//                logger.error("getPlantillas()" + ode.getMessage(), ode);
//        }catch (EOFException ee){
//        }finally{
//            try{ois.close();}catch(Exception e){};
//        }
//        return cRet;
//     }
    
    
    public Collection getPlantillasCuadros(String path,String filtro) throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_PLANTILLAS_CUADROS);
        Hashtable params= new Hashtable();
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_PATH_PLANTILLAS, path);
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        if(filtro!=null)
        	params.put(ConstantesLocalGISEIEL_LCGIII.FILTRO , filtro);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            for(;;){
                cRet.add(readObject(ois));
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getPlantillas()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }
    
    
    public byte[] exportMPT(String fase) throws Exception
    {        
    	byte[] respuestaFile=null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_EXPORT_MPT);
        params.put(ConstantesLocalGISEIEL_LCGIII.FASE_MPT, fase);
        //params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO, tipoElemento);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
    	try{
    		respuestaFile = (byte[])readObject(ois);
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("insert(Object obj)" + ode.getMessage(), ode);
    	}catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return respuestaFile;
    }
    
    
 public ArrayList obtenerPoblamientosMPT(String provincia,ArrayList lstIdMunicipios) throws Exception{
    	
    	ArrayList respuesta= null;
    	ACQuery query= new ACQuery();
    	query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_POBLAMIENTOS_MPT);
    	Hashtable params= new Hashtable();
    	params.put(ConstantesLocalGISEIEL_LCGIII.LST_ID_MUNICIPIO_CUADROS_MPT, lstIdMunicipios);
    	params.put(ConstantesLocalGISEIEL_LCGIII.PROVINCIA_MPT, provincia);
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarConsulta(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);
    	try{
    		respuesta = (ArrayList)readObject(ois);
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("insert(Object obj)" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}
    	//return respuesta;
    	
    	return respuesta;
    }
    
    public ArrayList obtenerCuadrosMPT() throws Exception{
    	
    	ArrayList respuesta= null;
    	ACQuery query= new ACQuery();
    	query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_CUADROS_MPT);
    	Hashtable params= new Hashtable();
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarConsulta(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);
    	try{
    		respuesta = (ArrayList)readObject(ois);
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("insert(Object obj)" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}
    	//return respuesta;
    	
    	return respuesta;
    }
    
    public ArrayList obtenerValidacionesMPT() throws Exception{
    	
    	ArrayList respuesta= null;
    	ACQuery query= new ACQuery();
    	query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_VALIDACIONES_MPT);
    	Hashtable params= new Hashtable();
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarConsulta(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);
    	try{
    		respuesta = (ArrayList)readObject(ois);
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("insert(Object obj)" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}

    	return respuesta;
    }
    
    public byte[]  validacionMPT(String fase, int cuadro, ArrayList lstValCuadros) throws Exception{

    	byte[]  respuesta= null;
    	ACQuery query= new ACQuery();
    	query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_VALIDACION_MPT);
    	Hashtable params= new Hashtable();
    	params.put(ConstantesLocalGISEIEL_LCGIII.FASE_MPT, fase);
    	params.put(ConstantesLocalGISEIEL_LCGIII.CUADRO_VALIDACION_MPT, cuadro);
    	params.put(ConstantesLocalGISEIEL_LCGIII.CUADRO_VALIDACIONES_CUADROS_MPT, lstValCuadros);
    	params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarConsulta(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);
    	try{
    		respuesta = (byte[] )readObject(ois);
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("insert(Object obj)" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}
    	return respuesta;
    }
    
    
    public Object publicarElemento(Object obj, String tipoElemento) throws Exception{
        if (obj == null){
            throw new Exception("Elemento no valido");
        }
        Object cRet= null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_PUBLICAR_ELEMENTO);
        Hashtable params= new Hashtable();
        params.put(ConstantesLocalGISEIEL_LCGIII.OBJECT, obj);
        params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO,tipoElemento);
        cRet=enviar(query,params);     
   
        return cRet;
   }
    
    public Object validarElemento(Object obj, String tipoElemento) throws Exception{
        if (obj == null){
            throw new Exception("Elemento no valido");
        }
        Object cRet= null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_VALIDAR_ELEMENTO);
        Hashtable params= new Hashtable();
        params.put(ConstantesLocalGISEIEL_LCGIII.OBJECT, obj);
        params.put(ConstantesLocalGISEIEL_LCGIII.TIPO_ELEMENTO,tipoElemento);
        cRet=enviar(query,params);     
   
        return cRet;
   }

    
    public Collection<Object> getNodosEIEL(String nodo,String locale) throws Exception{
        Collection<Object> cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_NODOS_EIEL);
        Hashtable<String,Object> params= new Hashtable<String,Object>();
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_NODO_EIEL, nodo);
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(ConstantesLocalGISEIEL_LCGIII.Locale, locale);
        cRet=enviar(query,params);     
               
       //Traducimos los nombres por si vinieran vacios.
       Iterator<Object> it=cRet.iterator();
       while (it.hasNext()){
		LCGNodoEIEL nodoEIEL=(LCGNodoEIEL)it.next();
		if (nodoEIEL.getTraduccion()==null){
			String traduccion=I18N.get("LocalGISEIEL", nodoEIEL.getTagTraduccion());
			nodoEIEL.setTraduccion(traduccion);
			}
       }
        return cRet;
     }

	public HashMap<String, Object> getNodosEIELByName(String nodo, String locale)
			throws Exception {

		Collection<Object> cRet = null;
		HashMap<String, Object> nodosByName = new HashMap();
		cRet = getNodosEIEL(nodo, locale);

		// Lo almacenamos en una tabla Hash
		Iterator<Object> it = cRet.iterator();
		while (it.hasNext()) {
			LCGNodoEIEL nodoEIEL = (LCGNodoEIEL) it.next();
			String clave = nodoEIEL.getNodo();
			
			//Para aquellos casos en los que la capa y la clave no son iguales
			//duplicamos el nodo y le fijamos los mismos valores que la clave
			//Se utiliza para la herramienta GeopistaSelectEIELTool que necesita como clave
			//la capa
			if (nodoEIEL.getLayer()!=null)
				nodosByName.put(nodoEIEL.getLayer(), nodoEIEL);
			nodosByName.put(clave, nodoEIEL);
		}
		return nodosByName;
	}
    
    
    public Collection<Object> getCamposCapaEIEL(String nodo,String locale) throws Exception{
        Collection<Object> cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_CAMPOS_CAPA_EIEL);
        Hashtable<String,Object> params= new Hashtable<String,Object>();
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_NODO_EIEL, nodo);
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(ConstantesLocalGISEIEL_LCGIII.Locale, locale);
        cRet=enviar(query,params);   
        
        //Traducimos los nombres por si vinieran vacios.
        Iterator<Object> it=cRet.iterator();
		while (it.hasNext()){
			LCGCampoCapaEIEL campoCapaEIEL=(LCGCampoCapaEIEL)it.next();
			if (campoCapaEIEL.getTraduccion()==null){
				String traduccion=I18N.get("LocalGISEIEL", campoCapaEIEL.getTagTraduccion());
				if (traduccion.equals(campoCapaEIEL.getTagTraduccion())){
					logger.warn("El campo "+campoCapaEIEL.getTagTraduccion()+" parece no tener traduccion");
				}
				campoCapaEIEL.setTraduccion(traduccion);
			}
		}
        return cRet;
     }
    
    public Collection<Object> getNucleosEIEL() throws Exception{
        Collection<Object> cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_NUCLEOS_MUNICIPIO);
        Hashtable<String,Object> params= new Hashtable<String,Object>();
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        cRet=enviar(query,params);   
        
       
        return cRet;
     }
    
    
    public Collection<Object> getIndicadoresEIEL() throws Exception{
        Collection<Object> cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_INDICADORES_EIEL);
        Hashtable<String,Object> params= new Hashtable<String,Object>();
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        cRet=enviar(query,params);   
        
       
        return cRet;
     }
    
    public Collection<Object> getMunicipiosEIEL() throws Exception{
        Collection<Object> cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_MUNICIPIOS_EIEL);
        Hashtable<String,Object> params= new Hashtable<String,Object>();
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        cRet=enviar(query,params);   
        
       
        return cRet;
     }
    
    
    public HashMap getNumElementosPendientes(String nodo,String locale) throws Exception{
        Collection<Object> cRet=null;
        HashMap map=new HashMap();
        
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISEIEL_LCGIII.ACTION_GET_NUM_ELEMENTOS_PENDIENTES);
        Hashtable<String,Object> params= new Hashtable<String,Object>();
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_NODO_EIEL, nodo);
        params.put(ConstantesLocalGISEIEL_LCGIII.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        params.put(ConstantesLocalGISEIEL_LCGIII.Locale, locale);
        cRet=enviar(query,params);     
               
       //Traducimos los nombres por si vinieran vacios.
       Iterator<Object> it=cRet.iterator();
       while (it.hasNext()){
			LCGNodoEIEL nodoEIEL=(LCGNodoEIEL)it.next();
			if (nodoEIEL.getTraduccion()==null){
				String traduccion=I18N.get("LocalGISEIEL", nodoEIEL.getTagTraduccion());
				nodoEIEL.setTraduccion(traduccion);
			}
			map.put(nodoEIEL.getClave(),nodoEIEL);
       }
        return map;
     }
    
    /**
     * Envio de parametros al servidor
     * @param query
     * @param params
     * @return
     * @throws Exception
     */
	private Collection<Object> enviar(ACQuery query,
			Hashtable<String, Object> params) throws Exception {

		Collection<Object> cRet = null;
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = enviarConsulta(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = new ArrayList<Object>();
		try {
			for (;;) {
				cRet.add(readObject(ois));
			}
		} catch (OptionalDataException ode) {
			if (ode.eof != true)
				logger.error("enviar:" + ode.getMessage(), ode);
		} catch (EOFException ee) {} 
		finally {
			try {ois.close();} catch (Exception e) {};
		}
		return cRet;
	}

	private Object enviar2(ACQuery query,
			Hashtable<String, Object> params) throws Exception {

		Object cRet = null;
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = enviarConsulta(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		try {
			cRet= readObject(ois);
		} catch (OptionalDataException ode) {
			if (ode.eof != true)
				logger.error("enviar2:" + ode.getMessage(), ode);
		} catch (EOFException ee) {} 
		finally {
			try {ois.close();} catch (Exception e) {};
		}
		return cRet;
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
	
			//logger.info("Iniciando proceso de carga");
			
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
			//logger.info("Finalizando proceso de carga");
		
		}
			
    }	
}
