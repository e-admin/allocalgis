package com.geopista.app.plantillas;

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
import com.geopista.plugin.Constantes;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.vividsolutions.jump.task.TaskMonitor;

/**
 * Clase que implementa en la parte cliente la conexion con la parte servidora. Los diferentes metodos son para las
 * diferentes acciones en base de datos. Se crear un ACQuery para cada caso, se introduce una constante indicando la
 * accion que se quiere realizar y se añaden los paramtros a la hash params que seran recogidos en el servlet segun
 * la accion indicada, y se hara la peticion a base de datos. Los objetos enviados deben ser serializables.
 * */

public class LocalGISPlantillasClient
{

    private static final Log logger= LogFactory.getLog(LocalGISPlantillasClient.class);
    private String sUrl=null;
    public static final String	mensajeXML	= "mensajeXML";
    public static final String	idMunicipio	= "idMunicipio";
    public static final String	IdApp		= "IdApp";

    /**
     * Inicializa la conexion al servidor
     * @param sUrl del servidor
     */
    public LocalGISPlantillasClient(String sUrl)
    {
        this.sUrl=sUrl;
    }

    /**
     * Devuelve un listado con las operaciones de ALP a realizar
     * @return Collection con objetos OperacionAlp
     * @throws Exception
     */
    
    

    
    

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
        else
            throw new Exception(sStatusLine);
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
        query.setAction(Constantes.ACTION_GET_ID_USUARIO);
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
    
    public Collection getPlantillas(String path,String filtro,String patron,ArrayList patrones) throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesLocalGISPlantillas.ACTION_GET_PLANTILLAS);
        Hashtable params= new Hashtable();
        params.put(ConstantesLocalGISPlantillas.KEY_PATH_PLANTILLAS, path);
        params.put(ConstantesLocalGISPlantillas.KEY_ID_MUNICIPIO, AppContext.getIdMunicipio());
        if(filtro!=null)
        	params.put(ConstantesLocalGISPlantillas.FILTRO , filtro);
        if(patron!=null)
        	params.put(ConstantesLocalGISPlantillas.PATRON, patron);
        if(patrones!=null)
        	params.put(ConstantesLocalGISPlantillas.PATRONES, patrones);
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
