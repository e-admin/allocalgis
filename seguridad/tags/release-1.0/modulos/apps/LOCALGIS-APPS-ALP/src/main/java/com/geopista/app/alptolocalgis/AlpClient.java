package com.geopista.app.alptolocalgis;

import java.util.*;
import java.util.zip.GZIPInputStream;
import java.io.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import com.geopista.app.AppContext;
import com.geopista.app.alptolocalgis.beans.ConstantesAlp;
import com.geopista.app.alptolocalgis.beans.OperacionAlp;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.security.SecurityManager;

/**
 * Clase que implementa en la parte cliente la conexion con la parte servidora. Los diferentes metodos son para las
 * diferentes acciones en base de datos. Se crear un ACQuery para cada caso, se introduce una constante indicando la
 * accion que se quiere realizar y se añaden los paramtros a la hash params que seran recogidos en el servlet segun
 * la accion indicada, y se hara la peticion a base de datos. Los objetos enviados deben ser serializables.
 * */

public class AlpClient
{

    private static final Log logger= LogFactory.getLog(AlpClient.class);
    private String sUrl=null;
    public static final String	mensajeXML	= "mensajeXML";
    public static final String	idMunicipio	= "idMunicipio";
    public static final String	idEntidad	= "idEntidad";
    public static final String	IdApp		= "IdApp";

    /**
     * Inicializa la conexion al servidor
     * @param sUrl del servidor
     */
    public AlpClient(String sUrl)
    {
        this.sUrl=sUrl;
    }

    /**
     * Devuelve un listado con las operaciones de ALP a realizar
     * @return Collection con objetos OperacionAlp
     * @throws Exception
     */
    public Collection getOperacionesAlp() throws Exception
    {
        Collection listaOperaciones= null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesAlp.ACTION_OBTENER_OPERACIONES);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaOperaciones= new ArrayList();
        try
        {
            for(;;)
            {
                Object operacionAlp= readObject(ois);
                if (operacionAlp instanceof OperacionAlp)
                {
                    listaOperaciones.add((OperacionAlp)operacionAlp);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getOperacionesAlp()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaOperaciones;
    }
    
    public int getIdMapa(String nombreMapa) throws Exception
    {
        int idMapa = -1;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesAlp.ACTION_OBTENER_IDMAPA);
        params.put(ConstantesAlp.NOMBREMAPA,nombreMapa);
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
                logger.error("getOperacionesAlp()" + ode.getMessage(), ode);
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
    
    public Collection removeOperacionAlp(Integer idOperacion) throws Exception
    {
        Collection listaOperaciones= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesAlp.ACTION_ELIMINAR_OPERACIONES);
        params.put(ConstantesAlp.IDOPERACION,idOperacion);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaOperaciones= new ArrayList();
        try
        {
            for(;;)
            {
                Object operacionAlp= readObject(ois);
                if (operacionAlp instanceof OperacionAlp)
                {
                    listaOperaciones.add((OperacionAlp)operacionAlp);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getOperacionesAlp()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaOperaciones;
    }
    
    public Collection getOperacionesAlp(String filtro) throws Exception
    {
        Collection listaOperaciones= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesAlp.ACTION_OBTENER_OPERACIONES_FILTRO);
        params.put(ConstantesAlp.FILTRO,filtro);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaOperaciones= new ArrayList();
        try
        {
            for(;;)
            {
                Object operacionAlp= readObject(ois);
                if (operacionAlp instanceof OperacionAlp)
                {
                    listaOperaciones.add((OperacionAlp)operacionAlp);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getOperacionesAlp()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaOperaciones;
    }

    /**
     * Envía la consulta con el mensaje sMensaje, a la url indicada también por parámetro sUrl.
     * @param sUrl url a la que enviar la consulta.
     * @param sMensaje mensaje a enviar.
     * @return InputStream.
     * @throws Exception
     */
    public static InputStream enviarConsulta(String sUrl, String sMensaje) throws Exception
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
    private static InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword, Vector files) throws Exception
    {
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else
        {
			if (com.geopista.security.SecurityManager.getIdSesion() == null){
				if (logger.isDebugEnabled()){
					logger.debug("enviar(String, String, String, String) - Usuario no autienticado");
				}
                creds = new UsernamePasswordCredentials("GUEST", "");
            }else
               creds = new UsernamePasswordCredentials(com.geopista.security.SecurityManager.getIdSesion(), com.geopista.security.SecurityManager.getIdSesion());
		}
		//create a singular HttpClient object
		org.apache.commons.httpclient.HttpClient client= AppContext.getHttpClient();
		
		//establish a connection within 5 seconds
		//client.setConnectionTimeout(5000);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null)
        {
			client.getState().setCredentials(null, null, creds);
		}
        
        /* -- MultipartPostMethod -- */
        org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);

        if (com.geopista.security.SecurityManager.getIdEntidad()!=null)
        {
             method.addParameter(idEntidad, SecurityManager.getIdEntidad());
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
}
