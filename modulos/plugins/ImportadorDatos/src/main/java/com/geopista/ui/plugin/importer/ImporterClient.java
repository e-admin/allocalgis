/**
 * ImporterClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.importer;

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
import com.geopista.app.alptolocalgis.beans.ConstantesAlp;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.ui.plugin.importer.beans.ConstantesImporter;
import com.geopista.ui.plugin.importer.beans.NodoDominio;

/**
 * Clase que implementa en la parte cliente la conexion con la parte servidora. Los diferentes metodos son para las
 * diferentes acciones en base de datos. Se crear un ACQuery para cada caso, se introduce una constante indicando la
 * accion que se quiere realizar y se añaden los paramtros a la hash params que seran recogidos en el servlet segun
 * la accion indicada, y se hara la peticion a base de datos. Los objetos enviados deben ser serializables.
 * */

public class ImporterClient
{

    private static final Log logger= LogFactory.getLog(ImporterClient.class);
    private String sUrl=null;
    public static final String	mensajeXML	= "mensajeXML";
    public static final String	idMunicipio	= "idMunicipio";
    public static final String	IdApp		= "IdApp";

    /**
     * Inicializa la conexion al servidor
     * @param sUrl del servidor
     */
    public ImporterClient(String sUrl)
    {
        this.sUrl=sUrl;
    }

    /**
     * Devuelve un listado con los valores del dominio que se pasa por parámetro
     * @return Collection con objetos DomainNode
     * @throws Exception
     */
    public ArrayList<NodoDominio> getDomainValues(int idDomain, String locale) throws Exception
    {
    	ArrayList<NodoDominio> listaDomainNodes= null;
        ACQuery query= new ACQuery();
        query.setAction(ConstantesImporter.ACTION_GET_DOMAIN_VALUES);
        Hashtable params= new Hashtable();
        params.put(ConstantesImporter.IDDOMAIN, new Integer(idDomain));
        params.put(ConstantesImporter.IDLOCALE, locale);
        query.setParams(params);
        
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaDomainNodes= new ArrayList<NodoDominio>();
        try
        {
            for(;;)
            {
                Object node= readObject(ois);
                if (node instanceof NodoDominio)
                {
                    listaDomainNodes.add((NodoDominio)node);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getDomainValues()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaDomainNodes;
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
		client.setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null)
        {
			client.getState().setCredentials(null, null, creds);
		}
        
        /* -- MultipartPostMethod -- */
        org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);

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
